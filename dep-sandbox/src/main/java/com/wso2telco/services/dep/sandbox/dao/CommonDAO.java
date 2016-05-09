package com.wso2telco.services.dep.sandbox.dao;

import org.hibernate.Session;
import com.wso2telco.services.dep.sandbox.dao.model.domain.SenderAddress;

public class CommonDAO extends AbstractDAO {

	public boolean isWhiteListedSenderAddress(int userId, String shortCode){
		
		SenderAddress senderAddress = null;
		
		Session session = getSession();
		
		senderAddress = (SenderAddress) session.createQuery("from SenderAddress where user.id = ? and shortCode = ?").setInteger(0, userId).setString(1, shortCode).uniqueResult();
		if (senderAddress != null) {
			
            return true;
        }
		
		return false;
	}
}
