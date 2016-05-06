package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.model.custom.OutboundSMSMessageRequestBean;
import com.wso2telco.services.dep.sandbox.dao.model.custom.SendMTSMSRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;

class SendMTSMSService extends AbstractRequestHandler<SendMTSMSRequestWrapperDTO> {
	{
		LOG = LogFactory.getLog(SendMTSMSService.class);
	}
	@Override
	protected Returnable process(SendMTSMSRequestWrapperDTO extendedRequestDTO) throws Exception {

		try {

			User user = extendedRequestDTO.getUser();

			System.out.println("---------------------------" + user.getUserName());

			OutboundSMSMessageRequestBean requestBean = extendedRequestDTO.getOutboundSMSMessageRequestBean();

			OutboundSMSMessageResponseBean responseBean = new OutboundSMSMessageResponseBean();
			OutboundSMSMessageResponseBean.OutboundSMSMessageRequest smsMessageResponse = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest();
			smsMessageResponse.setAddress(requestBean.getOutboundSMSMessageRequest().getAddress());

			OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList responseDeliveryInfoList = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList();

			List<OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList.DeliveryInfo> deliveryInforArrayList = new ArrayList<OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList.DeliveryInfo>();
			Iterator<String> addressReader = requestBean.getOutboundSMSMessageRequest().getAddress().iterator();
			while (addressReader.hasNext()) {

				OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList.DeliveryInfo responseDeliveryInfo = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.DeliveryInfoList.DeliveryInfo();
				responseDeliveryInfo.setAddress(addressReader.next());
				responseDeliveryInfo.setDeliveryStatus("sent");

				deliveryInforArrayList.add(responseDeliveryInfo);
			}
			responseDeliveryInfoList.setDeliveryInfo(deliveryInforArrayList);
			responseDeliveryInfoList.setResourceURL(
					"http://example.com/smsmessaging/v1/outbound/tel:26451/requests/1426568269358SM108605/deliveryInfos");

			smsMessageResponse.setSenderAddress(requestBean.getOutboundSMSMessageRequest().getSenderAddress());
			OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.OutboundSMSTextMessage responseSMSTextMessage = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.OutboundSMSTextMessage();
			responseSMSTextMessage
					.setMessage(requestBean.getOutboundSMSMessageRequest().getOutboundSMSTextMessage().getMessage());
			smsMessageResponse.setOutboundSMSTextMessage(responseSMSTextMessage);

			smsMessageResponse.setClientCorrelator(requestBean.getOutboundSMSMessageRequest().getClientCorrelator());

			OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.ReceiptRequest responseReceiptRequest = new OutboundSMSMessageResponseBean.OutboundSMSMessageRequest.ReceiptRequest();
			responseReceiptRequest
					.setNotifyURL(requestBean.getOutboundSMSMessageRequest().getReceiptRequest().getNotifyURL());
			responseReceiptRequest
					.setCallbackData(requestBean.getOutboundSMSMessageRequest().getReceiptRequest().getCallbackData());
			smsMessageResponse.setReceiptRequest(responseReceiptRequest);

			smsMessageResponse.setSenderName(requestBean.getOutboundSMSMessageRequest().getSenderName());
			smsMessageResponse.setResourceURL(
					"http://example.com/smsmessaging/v1/outbound/tel:26451/requests/1426568269358SM108605");

			responseBean.setOutboundSMSMessageRequest(smsMessageResponse);
		} catch (Exception e) {

			throw e;
		}

		return null;
	}

	@Override
	protected boolean validate(SendMTSMSRequestWrapperDTO wrapperDTO) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
