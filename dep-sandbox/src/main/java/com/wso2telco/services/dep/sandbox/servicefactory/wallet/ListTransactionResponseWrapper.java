package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import com.wso2telco.services.dep.sandbox.dao.model.custom.ListTransactionDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class ListTransactionResponseWrapper extends AbstractReturnWrapperDTO {

	private ListTransactionDTO listPaymentDTO;

	@Override
	public Object getResponse() {
		if (getRequestError() != null) {
			return getRequestError();
		}
		return getListPaymentDTO();
	}

	public ListTransactionDTO getListPaymentDTO() {
		return listPaymentDTO;
	}

	public void setListPaymentDTO(ListTransactionDTO listPaymentDTO) {
		this.listPaymentDTO = listPaymentDTO;
	}

}
