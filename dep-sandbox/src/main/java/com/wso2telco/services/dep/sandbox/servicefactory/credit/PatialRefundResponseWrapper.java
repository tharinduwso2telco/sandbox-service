package com.wso2telco.services.dep.sandbox.servicefactory.credit;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundResponseBean;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class PatialRefundResponseWrapper extends AbstractReturnWrapperDTO {
	
	private RefundResponseBean refundResponseBean;

	@Override
	public Object getResponse() {

		if (getRequestError() == null) {
			return refundResponseBean;
		} else {
			ErrorResponseDTO response = new ErrorResponseDTO(getRequestError());
			return response;
		}
	}

	public RefundResponseBean getRefundResponseBean() {
		return refundResponseBean;
	}

	public void setRefundResponseBean(RefundResponseBean refundResponseBean) {
		this.refundResponseBean = refundResponseBean;
	}
	

}
