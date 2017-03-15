package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.model.domain.MessageLog;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
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
import org.json.JSONArray;
import org.json.JSONObject;

class QuerySMSDeliveryStatusService extends AbstractRequestHandler<QuerySMSDeliveryStatusRequestWrapperDTO> implements AddressIgnorerable {

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

	    String shortCode = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getShortCode());
	    String transactionId = CommonUtil.getNullOrTrimmedValue(wrapperDTO.getMtSMSTransactionId());
		List<ValidationRule> validationRulesList = new ArrayList<>();

		try
		{
               validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,"sender_address",shortCode));
               validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,"transaction_id",transactionId));
		}
		catch (CustomException ex)
		{

			LOG.error("###QuerySMSDeliveryStatus### Error in Validations. ", ex);
			responseWrapperDTO.setRequestError(
					constructRequestError(SERVICEEXCEPTION, ex.getErrcode(), ex.getErrmsg(), ex.getErrvar()[0]));
			return false;
		}


		return true;
	}

	@Override
	protected Returnable process(QuerySMSDeliveryStatusRequestWrapperDTO extendedRequestDTO) throws Exception {

		try {

			User user = extendedRequestDTO.getUser();
			int userId = user.getId();
			String id = Integer.toString(userId);
			SMSRequestLog previousSMSRequestDetails = null;
			MessageLog previousSMSDeliveryDetails = null;
			ArrayList<HashMap<String,String>> deliveryStatusArraylist = new ArrayList();

			String mtSMSTransactionIdParts[] = extendedRequestDTO.getMtSMSTransactionId().split("-");

			String sendersAddress = null;
			String deliveryStat = null;
			String resourceUrl = null;
			String recieverAddress = null;

			String senderAddress = extendedRequestDTO.getShortCode();
			if (extendedRequestDTO.getShortCode().contains("tel:+")) {
				senderAddress = extendedRequestDTO.getShortCode().replace("tel:+", "").trim();

			} else if (extendedRequestDTO.getShortCode().contains("tel:")) {
				senderAddress = extendedRequestDTO.getShortCode().replace("tel:", "").trim();

			}

			int deliveryStatus = 0;
			int type = 0;
			int apiId = 0;
			String jsonRequestString = null;

			if(mtSMSTransactionIdParts.length !=2)
			{
				 previousSMSDeliveryDetails = smsMessagingDAO.getPreviousSMSDeliveryDetailsByMtSMSTransactionId(Integer.valueOf(mtSMSTransactionIdParts[0]));
			}
			else if (mtSMSTransactionIdParts.length ==2)
			{
				previousSMSDeliveryDetails = smsMessagingDAO.getPreviousSMSDeliveryDetailsByMtSMSTransactionId(Integer.valueOf(mtSMSTransactionIdParts[1]));
			}


			if(previousSMSDeliveryDetails != null)
			{
				 deliveryStatus = previousSMSDeliveryDetails.getStatus();
				 type = previousSMSDeliveryDetails.getType();
			 apiId = previousSMSDeliveryDetails.getServicenameid();

			 int messageLogId = previousSMSDeliveryDetails.getId();

				String transactionId[] =extendedRequestDTO.getMtSMSTransactionId().split("-");
				String newTransactionId = null;
				if(transactionId.length != 2)
				{
					newTransactionId = transactionId[0];
				}
				if(transactionId.length == 2)
				{
					newTransactionId = transactionId[1];
				}

				if(deliveryStatus == 1 && type == 1 && apiId == 14 && messageLogId== Integer.valueOf(newTransactionId))
				{
                  //ToDo use enums for the hard coded values in JSON objects
						jsonRequestString = previousSMSDeliveryDetails.getRequest();
						JSONObject jsonObject = new JSONObject(jsonRequestString);
						JSONObject jsonChildObj = (JSONObject) jsonObject.getJSONObject("outboundSMSMessageRequest");
						JSONObject jsoninnerChild = (JSONObject) jsonChildObj.getJSONObject("deliveryInfoList");

						String address = null;
						String status = null;

						JSONArray deliveryListArray = jsoninnerChild.getJSONArray("deliveryInfo");
						String arrayList = deliveryListArray.toString();
						for (int i = 0; i < deliveryListArray.length(); i++) {
							HashMap<String, String> delivaryStatusHashmap = new HashMap<>();
							JSONObject jsonArrayList = deliveryListArray.getJSONObject(i);
							address = jsonArrayList.optString("address");
							status = jsonArrayList.optString("deliveryStatus");
							delivaryStatusHashmap.put("address", address);
							delivaryStatusHashmap.put("deliveryStatus", status);
							deliveryStatusArraylist.add(delivaryStatusHashmap);

						}

						deliveryStat = status;
						recieverAddress = address;
						//deliveryStatuss =  jsonObject.getString("deliveryStatus");
						resourceUrl = jsonChildObj.getString("resourceURL");

						sendersAddress = jsonChildObj.getString("senderAddress");
						//sendersAddress = address.toString();



				}
			}


			if(mtSMSTransactionIdParts.length != 2)
			{
				previousSMSRequestDetails = smsMessagingDAO
						.getPreviousSMSRequestDetailsBySMSId(Integer.parseInt(mtSMSTransactionIdParts[0]));
			}
			if(mtSMSTransactionIdParts.length == 2)
			{
				previousSMSRequestDetails = smsMessagingDAO
						.getPreviousSMSRequestDetailsBySMSId(Integer.parseInt(mtSMSTransactionIdParts[1]));
			}


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
			} else if (!senderAddress.equals(sendersAddress)) {

				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0004",
						"No valid addresses provided in message part %1", extendedRequestDTO.getShortCode()));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			} else {

				boolean queryDeliveryStatusTransactionStatus = smsMessagingDAO.saveQueryDeliveryStatusTransaction(
						extendedRequestDTO.getShortCode(), null, null, null, null, null, null, 0, "success", 5,
						null, null, user, extendedRequestDTO.getMtSMSTransactionId());

				boolean delivaryStatusResponseStatus = smsMessagingDAO.saveDeliveryStatusResponse(jsonRequestString,deliveryStatus,type,apiId,
						userId,"shortcode",sendersAddress);
				if(!delivaryStatusResponseStatus)
				{
					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002",
							"A service error occurred. Error code is %1", "process failure of Response of the QuerySMSDeliveryStatus"));
				}
				if (!queryDeliveryStatusTransactionStatus ) {

					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0001",
							"A service error occurred. Error code is %1", "Access failure for API"));
					responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
				} else {




					String shortCodes[] = recieverAddress.replace("[", "").replace("]", "")
							.split(",");
					String deliveryStatusArray[] = deliveryStat.split(",");

					QuerySMSDeliveryStatusResponseBean responseBean = new QuerySMSDeliveryStatusResponseBean();

					QuerySMSDeliveryStatusResponseBean.DeliveryInfoList responseDeliveryInfoList = new QuerySMSDeliveryStatusResponseBean.DeliveryInfoList();

					List<QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo> deliveryInforArrayList = new ArrayList<QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo>();



					for(int i=0; i<deliveryStatusArraylist.size();i++)
					{
						QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo responseDeliveryInfos = new QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo();
						responseDeliveryInfos.setAddress(deliveryStatusArraylist.get(i).get("address"));
						responseDeliveryInfos.setDeliveryStatus(deliveryStatusArraylist.get(i).get("deliveryStatus"));
						deliveryInforArrayList.add(responseDeliveryInfos);

					}
				/*	for (int i = 0; i < shortCodes.length; i++) {

						QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo responseDeliveryInfo = new QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo();

						responseDeliveryInfo.setAddress(shortCodes[i]);
						responseDeliveryInfo.setDeliveryStatus(deliveryStatusArray[i]);

						deliveryInforArrayList.add(responseDeliveryInfo);
					}*/

					responseDeliveryInfoList.setDeliveryInfo(deliveryInforArrayList);
					responseDeliveryInfoList
							.setResourceURL(resourceUrl);

					responseBean.setDeliveryInfoList(responseDeliveryInfoList);

					responseWrapperDTO.setQuerySMSDeliveryStatusResponseBean(responseBean);
					responseWrapperDTO.setHttpStatus(Status.OK);
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
