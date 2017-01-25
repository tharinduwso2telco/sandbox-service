package com.wso2telco.services.dep.sandbox.servicefactory.wallet;

import com.wso2telco.services.dep.sandbox.dao.model.custom.BalanceLookupDTO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.ErrorResponseDTO;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractReturnWrapperDTO;

public class BalanceLookupResponseWrapper extends AbstractReturnWrapperDTO {

	private BalanceLookupDTO balanceLookupDTO;

	@Override
	public Object getResponse() {
		if (getRequestError() == null) {
			return getBalanceLookupDTO();
		}else{
			ErrorResponseDTO response= new ErrorResponseDTO(getRequestError());
			return response;
		}
	}

	public BalanceLookupDTO getBalanceLookupDTO() {
		return balanceLookupDTO;
	}

	public void setBalanceLookupDTO(BalanceLookupDTO balanceLookupDTO) {
		this.balanceLookupDTO = balanceLookupDTO;
	}
}
