package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import java.util.*;

import javax.ws.rs.core.Response.Status;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;
import com.wso2telco.services.dep.sandbox.dao.model.domain.*;
import com.wso2telco.services.dep.sandbox.servicefactory.*;
import com.wso2telco.services.dep.sandbox.util.CommonUtil;
import com.wso2telco.services.dep.sandbox.util.ServiceName;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.SMSMessagingDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.QuerySMSDeliveryStatusRequestWrapperDTO;
import org.json.JSONArray;
import org.json.JSONObject;

class QuerySMSDeliveryStatusService extends AbstractRequestHandler<QuerySMSDeliveryStatusRequestWrapperDTO> implements AddressIgnorerable {


	public static final String OUTBOUND_SMS_MESSAGE_REQUEST= "outboundSMSMessageRequest";
	public static  final String DELIVERY_INFO_LIST = "deliveryInfoList";
	public static  final String DELIVERY_INFO =  "deliveryInfo";
	public static  final String ADDRESS = "address";
	public static  final String DELIVERY_STATUS = "deliveryStatus";
	public static  final String RESOURCE_URL="resourceURL";
	public static  final String SENDER_ADDRESS ="senderAddress";
	public static  final String PARAM_SENDER_ADDRESS = "sender_address";
	public static  final String PARAM_TRANSACTION_ID ="transaction_id";
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
               validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,PARAM_SENDER_ADDRESS,shortCode));
               validationRulesList.add(new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY,PARAM_TRANSACTION_ID,transactionId));
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
			JSONObject jsonObject = null;
			QuerySMSDeliveryStatusResponseBean responseBean = new QuerySMSDeliveryStatusResponseBean();
			APITypes apiType = dao.getAPIType(extendedRequestDTO.getRequestType().toString().toLowerCase());
			String serviceCall = ServiceName.QuerySMSStatus.toString();
			APIServiceCalls apiService = dao.getServiceCall(apiType.getId(), serviceCall);
			String previousServiceCall = ServiceName.SendSMS.toString();
		 APIServiceCalls previousApiService = dao.getServiceCall(apiType.getId(),previousServiceCall);

			ArrayList<HashMap<String,String>> deliveryStatusArraylist = new ArrayList();

			String mtSMSTransactionIdParts[] = extendedRequestDTO.getMtSMSTransactionId().split("-");

			String sendersAddress = null;
			String deliveryStat = null;
			String resourceUrl = null;
			String recieverAddress = null;
			String trimmedAddress = null;

			String senderAddress = extendedRequestDTO.getShortCode();
			if (extendedRequestDTO.getShortCode().contains("tel:+")) {
				senderAddress = getLastMobileNumber(extendedRequestDTO.getShortCode());

			} else if (extendedRequestDTO.getShortCode().contains("tel:")) {
				senderAddress = getLastMobileNumber(extendedRequestDTO.getShortCode());


			}

			int deliveryStatus = 0;
			int type = 0;
			int apiId = 0;
			String jsonRequestString = null;

			if(mtSMSTransactionIdParts.length !=2)
			{
				 previousSMSDeliveryDetails = smsMessagingDAO.getPrevSMSDeliveryDataByTransId(Integer.valueOf(mtSMSTransactionIdParts[0]));
			}
			else if (mtSMSTransactionIdParts.length ==2)
			{
				previousSMSDeliveryDetails = smsMessagingDAO.getPrevSMSDeliveryDataByTransId(Integer.valueOf(mtSMSTransactionIdParts[1]));
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

				if(deliveryStatus == MessageProcessStatus.Success.getValue() && type == MessageType.Response.getValue() && apiId == previousApiService.getApiServiceCallId() && messageLogId== Integer.valueOf(newTransactionId))
				{

						jsonRequestString = previousSMSDeliveryDetails.getRequest();
					    jsonObject = new JSONObject(jsonRequestString);
						JSONObject jsonChildObj = (JSONObject) jsonObject.getJSONObject(OUTBOUND_SMS_MESSAGE_REQUEST);
						JSONObject jsoninnerChild = (JSONObject) jsonChildObj.getJSONObject(DELIVERY_INFO_LIST);

						String address = null;
						String status = null;

						JSONArray deliveryListArray = jsoninnerChild.getJSONArray(DELIVERY_INFO);
						String arrayList = deliveryListArray.toString();
						for (int i = 0; i < deliveryListArray.length(); i++) {
							HashMap<String, String> delivaryStatusHashmap = new HashMap<>();
							JSONObject jsonArrayList = deliveryListArray.getJSONObject(i);
							address = jsonArrayList.optString(ADDRESS);
							status = jsonArrayList.optString(DELIVERY_STATUS);
							delivaryStatusHashmap.put(ADDRESS, address);
							delivaryStatusHashmap.put(DELIVERY_STATUS, status);
							deliveryStatusArraylist.add(delivaryStatusHashmap);

						}

						deliveryStat = status;
						recieverAddress = address;
						resourceUrl = jsonChildObj.getString(RESOURCE_URL);
						sendersAddress = jsonChildObj.getString(SENDER_ADDRESS);

					trimmedAddress = getLastMobileNumber(sendersAddress);

				}


			}


			if (previousSMSDeliveryDetails == null) {

				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002",
						"Invalid input value for message part %1", extendedRequestDTO.getMtSMSTransactionId()));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			} else if (!dao.isWhiteListedSenderAddress(user.getId(), senderAddress)) {

				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0001",
						"A service error occurred. Error code is %1",
						extendedRequestDTO.getShortCode() + " Not Provisioned"));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			} else if (!senderAddress.equals(trimmedAddress)) {

				responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0004",
						"No valid addresses provided in message part %1", extendedRequestDTO.getShortCode()));
				responseWrapperDTO.setHttpStatus(Status.BAD_REQUEST);
			} else {



					String shortCodes[] = recieverAddress.replace("[", "").replace("]", "")
							.split(",");
					String deliveryStatusArray[] = deliveryStat.split(",");

					QuerySMSDeliveryStatusResponseBean.DeliveryInfoList responseDeliveryInfoList = new QuerySMSDeliveryStatusResponseBean.DeliveryInfoList();

					List<QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo> deliveryInforArrayList = new ArrayList();



					for(int i=0; i<deliveryStatusArraylist.size();i++)
					{
						QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo responseDeliveryInfos = new QuerySMSDeliveryStatusResponseBean.DeliveryInfoList.DeliveryInfo();
						responseDeliveryInfos.setAddress(deliveryStatusArraylist.get(i).get(ADDRESS));
						responseDeliveryInfos.setDeliveryStatus(deliveryStatusArraylist.get(i).get(DELIVERY_STATUS));
						deliveryInforArrayList.add(responseDeliveryInfos);

					}

					responseDeliveryInfoList.setDeliveryInfo(deliveryInforArrayList);
					responseDeliveryInfoList
							.setResourceURL(resourceUrl);


					responseBean.setDeliveryInfoList(responseDeliveryInfoList);


					responseWrapperDTO.setQuerySMSDeliveryStatusResponseBean(responseBean);
					responseWrapperDTO.setHttpStatus(Status.OK);

				int messageLogId = saveResponse(senderAddress,responseWrapperDTO.getQuerySMSDeliveryStatusResponseBean(),apiService,MessageProcessStatus.Success);

				if(messageLogId == 0)
				{
					responseWrapperDTO.setRequestError(constructRequestError(SERVICEEXCEPTION, "SVC0002",
							"A service error occurred. Error code is %1", "process failure of Response of the QuerySMSDeliveryStatus"));
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

   private int saveResponse(String endUserIdPath, QuerySMSDeliveryStatusResponseBean responseBean, APIServiceCalls
		   apiServiceCalls, MessageProcessStatus status) throws Exception {
	   Gson gson = new Gson();
	   String jsonString = gson.toJson(responseBean);
	   MessageLog messageLog =new MessageLog();
	   messageLog.setRequest(jsonString);
	   messageLog.setServicenameid(apiServiceCalls.getApiServiceCallId());
	   messageLog.setUserid(extendedRequestDTO.getUser().getId());
	   messageLog.setType(MessageType.Response.getValue());
	   messageLog.setValue(endUserIdPath);
	   messageLog.setReference("shortcode");
	   messageLog.setMessageTimestamp(new Date());
	   messageLog.setStatus(status.getValue());
	   int i = loggingDAO.saveMessageLog(messageLog);
	   return  i;

   }
}
