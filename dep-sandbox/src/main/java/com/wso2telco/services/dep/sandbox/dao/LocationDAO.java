package com.wso2telco.services.dep.sandbox.dao;

import com.wso2telco.services.dep.sandbox.dao.model.domain.Locationparam;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public interface LocationDAO {
	public Locationparam queryLocationParam(Integer userId) ;
	public boolean saveTransaction(String address, Double requestedAccuracy, String tranStatus, User user)
			throws Exception ;

}
