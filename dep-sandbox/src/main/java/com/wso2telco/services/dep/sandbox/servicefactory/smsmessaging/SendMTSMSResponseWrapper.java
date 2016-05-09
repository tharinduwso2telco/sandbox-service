package com.wso2telco.services.dep.sandbox.servicefactory.smsmessaging;

import com.wso2telco.oneapivalidation.exceptions.RequestError;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class SendMTSMSResponseWrapper extends AbstractReturnWrapperDTO {

	private OutboundSMSMessageResponseBean outboundSMSMessageResponseBean = null;
	
	public OutboundSMSMessageResponseBean getOutboundSMSMessageResponseBean() {
		return outboundSMSMessageResponseBean;
	}

	public void setOutboundSMSMessageResponseBean(OutboundSMSMessageResponseBean outboundSMSMessageResponseBean) {
		this.outboundSMSMessageResponseBean = outboundSMSMessageResponseBean;
	}

	@Override
	public Object getResponse() {
		
		SendMTSMSResponse response = new SendMTSMSResponse(getRequestError(), outboundSMSMessageResponseBean);
		return response; 
	}

	class SendMTSMSResponse {

		private RequestError requestError;
		private OutboundSMSMessageResponseBean outboundSMSMessageResponseBean;

		SendMTSMSResponse(RequestError requestError, OutboundSMSMessageResponseBean outboundSMSMessageResponseBean) {
			this.requestError = requestError;
			this.outboundSMSMessageResponseBean = outboundSMSMessageResponseBean;
		}

		public RequestError getRequestError() {
			return requestError;
		}

		public void setRequestError(RequestError requestError) {
			this.requestError = requestError;
		}

		public OutboundSMSMessageResponseBean getOutboundSMSMessageResponseBean() {
			return outboundSMSMessageResponseBean;
		}

		public void setOutboundSMSMessageResponseBean(OutboundSMSMessageResponseBean outboundSMSMessageResponseBean) {
			this.outboundSMSMessageResponseBean = outboundSMSMessageResponseBean;
		}
	}
}
