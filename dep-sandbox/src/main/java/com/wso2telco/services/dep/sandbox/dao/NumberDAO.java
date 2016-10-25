package com.wso2telco.services.dep.sandbox.dao;

import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;

public interface NumberDAO {
	
    
    public void saveManageNumbers(ManageNumber manageNumber) throws Exception;
    
    public List<ManageNumber> getManageNumbers(int userId) throws Exception;

}
