package com.wso2telco.services.dep.sandbox.dao;

import java.util.List;

import com.wso2telco.services.dep.sandbox.dao.model.domain.APIServiceCalls;
import com.wso2telco.services.dep.sandbox.dao.model.domain.APITypes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.Attributes;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public interface GenaricDAO {
    
    public User getUser(String username);

    public List<ManageNumber> getWhitelisted(int userid, List numbers);

    public boolean isWhiteListedSenderAddress(int userId, String shortCode);

    public APITypes getAPIType(String api);

    public APIServiceCalls getServiceCall(int apiId, String service);
    
    public Attributes getAttribute(String attribute);
    
    public AttributeDistribution getAttributeDistribution(int apiServiceId, int attributeId);
    
    public void saveAttributeValue(AttributeValues valueObj);

    public AttributeValues getAttributeValue(AttributeDistribution distributionObj);


}
