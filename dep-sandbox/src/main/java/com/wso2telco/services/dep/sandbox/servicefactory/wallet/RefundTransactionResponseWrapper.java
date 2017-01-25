package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RefundTransactionDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class RefundTransactionResponseWrapper extends AbstractReturnWrapperDTO {

	private RefundTransactionDTO refundTransactionDTO;

	public RefundTransactionDTO getRefundTransactionDTO() {
		return refundTransactionDTO;
	}

	public void setRefundTransactionDTO(RefundTransactionDTO refundTransactionDTO) {
		this.refundTransactionDTO = refundTransactionDTO;
	}

	@Override
	public Object getResponse() {
		if (getRequestError() == null) {
			return refundTransactionDTO;
		} else {
			ErrorResponseDTO response = new ErrorResponseDTO(getRequestError());
			return response;
		}
	}

}
