package com.wso2telco.services.dep.sandbox.dao;

import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public interface GenaricDAO {
	 public User getUser(String username);
	 public List<ManageNumber>  getWhitelisted(int userid, List numbers);
	 public boolean isWhiteListedSenderAddress(int userId, String shortCode);
}
