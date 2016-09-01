package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging.ValidateDeliveryStatus;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.QuerySMSDeliveryStatusRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSDeliveryStatus;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSRequestLog;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

class QuerySMSDeliveryStatusService extends AbstractRequestHandler<QuerySMSDeliveryStatusRequestWrapperDTO> {

	Gson gson = new GsonBuilder().serializeNulls().create();
	QuerySMSDeliveryStatusRequestWrapperDTO extendedRequestDTO = null;
	QuerySMSDeliveryStatusResponseWrapper responseWrapperDTO = null;
	private SMSMessagingDAO smsMessagingDAO = null;

	{
		LOG = LogFactory.getLog(QuerySMSDeliveryStatusService.class);
		smsMessagingDAO =DaoFactory.getSMSMessagingDAO();
	}

	@Override
	protected boolean validate(QuerySMSDeliveryStatusRequestWrapperDTO wrapperDTO) throws Exception {

		ValidateDeliveryStatus validator = new ValidateDeliveryStatus();
		String[] params = { wrapperDTO.getShortCode(), wrapperDTO.getMtSMSTransactionId() };
		validator.validate(params);
		return false;
	}

	@Override
	protected Returnable process(QuerySMSDeliveryStatusRequestWrapperDTO extendedRequestDTO) throws Exception {

		try {

			User user = extendedRequestDTO.getUser();

			String mtSMSTransactionIdParts[] = extendedRequestDTO.getMtSMSTransactionId().split("-");

			String previousShortCode = null;
			String previousDeliveryStatus = null;

			String senderAddress = extendedRequestDTO.getShortCode();
			if (extendedRequestDTO.getShortCode().contains("tel:+")) {
				senderAddress = extendedRequestDTO.getShortCode().replace("tel:+", "").trim();
			} else if (extendedRequestDTO.getShortCode().contains("tel:")) {
				senderAddress = extendedRequestDTO.getShortCode().replace("tel:", "").trim();
			}

			SMSDeliveryStatus previousSMSDeliveryDetails = smsMessagingDAO
					.getPreviousSMSDeliveryDetailsByMtSMSTransactionId(extendedRequestDTO.getMtSMSTransactionId());
			if (previousSMSDeliveryDetails != null) {
				previousShortCode = previousSMSDeliveryDetails.getSenderAddress();
				previousDeliveryStatus = previousSMSDeliveryDetails.getDeliveryStatus();
			}

			if (mtSMSTransactionIdParts.length != 2) {

				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002",
						"Invalid input value for message part %1", extendedRequestDTO.getMtSMSTransactionId()));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			} else {

				SMSRequestLog previousSMSRequestDetails = smsMessagingDAO
						.getPreviousSMSRequestDetailsBySMSId(Integer.parseInt(mtSMSTransactionIdParts[1]));

				if (previousSMSRequestDetails == null) {

					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002",
							"Invalid input value for message part %1", extendedRequestDTO.getMtSMSTransactionId()));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				} else if (!dao.isWhiteListedSenderAddress(user.getId(), senderAddress)) {

					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0001",
							"A service error occurred. Error code is %1",
							extendedRequestDTO.getShortCode() + " Not Provisioned"));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				} else if (previousSMSDeliveryDetails == null) {

					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002",
							"Invalid input value for message part %1", extendedRequestDTO.getMtSMSTransactionId()));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				} else if (!extendedRequestDTO.getShortCode().equals(previousShortCode)) {

					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0004",
							"No valid addresses provided in message part %1", extendedRequestDTO.getShortCode()));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				} else {

					boolean queryDeliveryStatusTransactionStatus = smsMessagingDAO.saveQueryDeliveryStatusTransaction(
							extendedRequestDTO.getShortCode(), null, null, null, null, null, null, 0, "success", 5,
							null, null, user, extendedRequestDTO.getMtSMSTransactionId());

					if (!queryDeliveryStatusTransactionStatus) {

						responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0001",
								"A service error occurred. Error code is %1", "Access failure for API"));
						responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
					} else {

						String shortCodes[] = previousSMSRequestDetails.getAddresses().replace("[", "").replace("]", "")
								.split(",");

						QuerySMSDeliveryStatusResponseBean responseBean = new QuerySMSDeliveryStatusResponseBean();

						QuerySMSDeliveryStatusResponseBean.DeliveryInfoList responseDeliveryInfoList = new QuerySMSDeliveryStatusResponseBean.DeliveryInfoList();

						List<QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo> deliveryInforArrayList = new ArrayList<QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo>();

						for (int i = 0; i < shortCodes.length; i++) {

							QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo responseDeliveryInfo = new QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo();

							responseDeliveryInfo.setAddress(shortCodes[i]);
							responseDeliveryInfo.setDeliveryStatus(previousDeliveryStatus);

							deliveryInforArrayList.add(responseDeliveryInfo);
						}

						responseDeliveryInfoList.setDeliveryInfo(deliveryInforArrayList);
						responseDeliveryInfoList
								.setResourceURL(getresourceURL(extendedRequestDTO.getMtSMSTransactionId()).toString());

						responseBean.setDeliveryInfoList(responseDeliveryInfoList);

						responseWrapperDTO.setQuerySMSDeliveryStatusResponseBean(responseBean);
						responseWrapperDTO.setHttpStatus(Status.OK);
					}
				}
			}
		} catch (Exception e) {

			throw e;
		}

		return responseWrapperDTO;
	}

	@Override
	protected Returnable getResponseDTO() {

		return responseWrapperDTO;
	}

	@Override
	protected List<String> getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init(QuerySMSDeliveryStatusRequestWrapperDTO extendedRequestDTO) throws Exception {

		responseWrapperDTO = new QuerySMSDeliveryStatusResponseWrapper();
		this.extendedRequestDTO = extendedRequestDTO;
	}

	private StringBuilder getresourceURL(final String mtSMSTransactionId) {

		StringBuilder resourceURLBuilder = new StringBuilder();
		try {

			resourceURLBuilder.append("http://wso2telco.sandbox.com");
			resourceURLBuilder.append("/smsmessaging/");
			resourceURLBuilder.append(extendedRequestDTO.getApiVersion());
			resourceURLBuilder.append("/outbound/");
			resourceURLBuilder.append(extendedRequestDTO.getShortCode());
			resourceURLBuilder.append("/requests/");
			resourceURLBuilder.append(mtSMSTransactionId);
			resourceURLBuilder.append("/deliveryInfos");
		} catch (Exception e) {

			throw e;
		}

		return resourceURLBuilder;
	}
}
