package com.wso2telco.services.dep.sandbox.dao.hibernate;

import org.apache.commons.logging.LogFactory;

import com.wso2telco.services.dep.sandbox.dao.CreditDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;

public class HibernateCreditDAO extends AbstractDAO implements CreditDAO{
	
	{
		LOG = LogFactory.getLog(HibernateCreditDAO.class);
	}

	@Override
	public void updateNumberBalance(ManageNumber manageNumber) throws Exception {
		
		try {		
			super.saveOrUpdate(manageNumber);		
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("###CREDIT### Error in updateNumberBalance " + ex);
		}
	}	

}
