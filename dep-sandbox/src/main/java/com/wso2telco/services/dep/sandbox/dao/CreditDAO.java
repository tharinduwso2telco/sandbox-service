package com.wso2telco.services.dep.sandbox.dao;

import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;

public interface CreditDAO {
	
	public void updateNumberBalance(ManageNumber manageNumber)throws Exception;

}
