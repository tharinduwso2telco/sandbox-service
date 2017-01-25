package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.MakePaymentDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class MakePaymentResponseWrapper extends AbstractReturnWrapperDTO {

	private MakePaymentDTO makePaymentDTO;

	public MakePaymentDTO getMakePaymentDTO() {
		return makePaymentDTO;
	}

	public void setMakePaymentDTO(MakePaymentDTO makePaymentDTO) {
		this.makePaymentDTO = makePaymentDTO;
	}

	@Override
	public Object getResponse() {
		if (getRequestError() == null) {
			return makePaymentDTO;
		} else {
			ErrorResponseDTO response = new ErrorResponseDTO(getRequestError());
			return response;

		}
	}

}
