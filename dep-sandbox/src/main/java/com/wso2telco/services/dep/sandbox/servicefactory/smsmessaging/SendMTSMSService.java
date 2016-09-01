package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dep.oneapivalidation.service.impl.smsmessaging.ValidateSendSms;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.OutboundSMSMessageRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.SendMTSMSRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SMSMessagingParam;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

class SendMTSMSService extends AbstractRequestHandler<SendMTSMSRequestWrapperDTO> {

	Gson gson = new GsonBuilder().serializeNulls().create();
	SendMTSMSRequestWrapperDTO extendedRequestDTO = null;
	SendMTSMSResponseWrapper responseWrapperDTO = null;
	private SMSMessagingDAO smsMessagingDAO = null;

	{
		LOG = LogFactory.getLog(SendMTSMSService.class);
		smsMessagingDAO = DaoFactory.getSMSMessagingDAO();
	}

	@Override
	protected Returnable process(SendMTSMSRequestWrapperDTO extendedRequestDTO) throws Exception {

		try {

			User user = extendedRequestDTO.getUser();
			SMSMessagingParam smsMessagingParam = smsMessagingDAO.getSMSMessagingParam(user.getId());

			String senderAddress = extendedRequestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest()
					.getSenderAddress();
			if (extendedRequestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest().getSenderAddress()
					.contains("tel:+")) {
				senderAddress = extendedRequestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest()
						.getSenderAddress().replace("tel:+", "").trim();
			} else if (extendedRequestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest()
					.getSenderAddress().contains("tel:")) {
				senderAddress = extendedRequestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest()
						.getSenderAddress().replace("tel:", "").trim();
			}

			if (!dao.isWhiteListedSenderAddress(user.getId(), senderAddress)) {

				responseWrapperDTO
						.setRequestError(
								constructRequestError(SERVICEEXCEPTION, "SVC0001",
										"A service error occurred. Error code is %1",
										extendedRequestDTO.getOutboundSMSMessageRequestBean()
												.getOutboundSMSMessageRequest().getSenderAddress()
												+ " Not Provisioned"));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			} else {

				OutboundSMSMessageRequestBean requestBean = extendedRequestDTO.getOutboundSMSMessageRequestBean();

				String mtSMSTransactionId = smsMessagingDAO.saveSendSMSTransaction(
						requestBean.getOutboundSMSMessageRequest().getSenderAddress(),
						requestBean.getOutboundSMSMessageRequest().getAddress().toArray().toString(),
						requestBean.getOutboundSMSMessageRequest().getOutboundSMSTextMessage().getMessage(),
						requestBean.getOutboundSMSMessageRequest().getClientCorrelator(),
						requestBean.getOutboundSMSMessageRequest().getSenderName(),
						requestBean.getOutboundSMSMessageRequest().getReceiptRequest().getNotifyURL(),
						requestBean.getOutboundSMSMessageRequest().getReceiptRequest().getCallbackData(), 0, "success",
						1, null, null, user, smsMessagingParam.getDeliveryStatus());

				if (mtSMSTransactionId == null) {

					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0001",
							"A service error occurred. Error code is %1", "Access failure for API"));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				} else {

					OutboundSMSMessageResponseBean responseBean = new OutboundSMSMessageResponseBean();

					OutboundSMSMessageResponseBean.OutboundSMSMessageRequest smsMessageResponse = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest();
					smsMessageResponse.setAddress(requestBean.getOutboundSMSMessageRequest().getAddress());

					OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList responseDeliveryInfoList = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList();

					List<OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList.DeliveryInfo> deliveryInforArrayList = new ArrayList<OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList.DeliveryInfo>();
					Iterator<String> addressReader = requestBean.getOutboundSMSMessageRequest().getAddress().iterator();
					while (addressReader.hasNext()) {

						OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList.DeliveryInfo responseDeliveryInfo = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList.DeliveryInfo();
						responseDeliveryInfo.setAddress(addressReader.next());
						responseDeliveryInfo.setDeliveryStatus(smsMessagingParam.getDeliveryStatus());

						deliveryInforArrayList.add(responseDeliveryInfo);
					}
					responseDeliveryInfoList.setDeliveryInfo(deliveryInforArrayList);
					responseDeliveryInfoList.setResourceURL(getinSideResourceURL(mtSMSTransactionId).toString());
					smsMessageResponse.setDeliveryInfoList(responseDeliveryInfoList);

					smsMessageResponse.setSenderAddress(requestBean.getOutboundSMSMessageRequest().getSenderAddress());
					OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.OutboundSMSTextMessage responseSMSTextMessage = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.OutboundSMSTextMessage();
					responseSMSTextMessage.setMessage(
							requestBean.getOutboundSMSMessageRequest().getOutboundSMSTextMessage().getMessage());
					smsMessageResponse.setOutboundSMSTextMessage(responseSMSTextMessage);

					smsMessageResponse
							.setClientCorrelator(requestBean.getOutboundSMSMessageRequest().getClientCorrelator());

					OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.ReceiptRequest responseReceiptRequest = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.ReceiptRequest();
					responseReceiptRequest.setNotifyURL(
							requestBean.getOutboundSMSMessageRequest().getReceiptRequest().getNotifyURL());
					responseReceiptRequest.setCallbackData(
							requestBean.getOutboundSMSMessageRequest().getReceiptRequest().getCallbackData());
					smsMessageResponse.setReceiptRequest(responseReceiptRequest);

					smsMessageResponse.setSenderName(requestBean.getOutboundSMSMessageRequest().getSenderName());
					smsMessageResponse.setResourceURL(getoutSideResourceURL(mtSMSTransactionId).toString());

					responseBean.setOutboundSMSMessageRequest(smsMessageResponse);

					responseWrapperDTO.setOutboundSMSMessageResponseBean(responseBean);
					responseWrapperDTO.setHttpStatus(Status.OK);
				}
			}
		} catch (Exception e) {

			throw e;
		}

		return responseWrapperDTO;
	}

	@Override
	protected boolean validate(SendMTSMSRequestWrapperDTO wrapperDTO) throws Exception {

		ValidateSendSms validator = new ValidateSendSms();
		validator.validate(gson.toJson(wrapperDTO.getOutboundSMSMessageRequestBean()));
		return false;
	}

	@Override
	protected Returnable getResponseDTO() {

		return responseWrapperDTO;
	}

	@Override
	protected List<String> getAddress() {

		List<String> addresses = new ArrayList<String>();
		addresses = extendedRequestDTO.getOutboundSMSMessageRequestBean().getOutboundSMSMessageRequest().getAddress();
		return addresses;
	}

	@Override
	protected void init(SendMTSMSRequestWrapperDTO extendedRequestDTO) throws Exception {

		responseWrapperDTO = new SendMTSMSResponseWrapper();
		this.extendedRequestDTO = extendedRequestDTO;
	}

	private StringBuilder getinSideResourceURL(final String mtSMSTransactionId) {

		StringBuilder inSideResourceURLBuilder = new StringBuilder();
		try {

			inSideResourceURLBuilder.append("http://wso2telco.sandbox.com");
			inSideResourceURLBuilder.append("/smsmessaging/");
			inSideResourceURLBuilder.append(extendedRequestDTO.getApiVersion());
			inSideResourceURLBuilder.append("/outbound/");
			inSideResourceURLBuilder.append(extendedRequestDTO.getShortCode());
			inSideResourceURLBuilder.append("/requests/");
			inSideResourceURLBuilder.append(mtSMSTransactionId);
			inSideResourceURLBuilder.append("/deliveryInfos");
		} catch (Exception e) {

			throw e;
		}

		return inSideResourceURLBuilder;
	}

	private StringBuilder getoutSideResourceURL(final String mtSMSTransactionId) {

		StringBuilder outSideResourceURLBuilder = new StringBuilder();
		try {

			outSideResourceURLBuilder.append("http://wso2telco.sandbox.com");
			outSideResourceURLBuilder.append("/smsmessaging/");
			outSideResourceURLBuilder.append(extendedRequestDTO.getApiVersion());
			outSideResourceURLBuilder.append("/outbound/");
			outSideResourceURLBuilder.append(extendedRequestDTO.getShortCode());
			outSideResourceURLBuilder.append("/requests/");
			outSideResourceURLBuilder.append(mtSMSTransactionId);
		} catch (Exception e) {

			throw e;
		}

		return outSideResourceURLBuilder;
	}
}
