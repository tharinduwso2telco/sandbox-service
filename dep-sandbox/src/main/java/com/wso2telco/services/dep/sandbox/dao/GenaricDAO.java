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
    

    public List<ManageNumber> getWhitelisted(int userid, List numbers);

    public boolean isWhiteListedSenderAddress(int userId, String shortCode);

    public APITypes getAPIType(String api)throws Exception;
    
    public List<APITypes> getAllAPIType()throws Exception;

    public APIServiceCalls getServiceCall(int apiId, String service)throws Exception;
    
    public List<APIServiceCalls> getAllServiceCall(int apiId)throws Exception;
    
    public Attributes getAttribute(String attribute)throws Exception;
    
    public AttributeDistribution getAttributeDistribution(int apiServiceId, int attributeId)throws Exception;
    
    public void saveAttributeValue(AttributeValues valueObj)throws Exception;

    public AttributeValues getAttributeValue(AttributeDistribution distributionObj)throws Exception;
    
    public List<AttributeValues> getAttributeListValue(List<AttributeDistribution> distributionObj)throws Exception;

    public List<AttributeDistribution> getAttributeDistributionByServiceCall(int apiId, int serviceId) throws Exception;

    public void saveAttributeValue(List<AttributeValues> attributeValues)throws Exception;
    
    public AttributeValues getAttributeValue(AttributeDistribution distributionObj, int ownerdid) throws Exception ;

    public ManageNumber getMSISDN (String msisdn, String imsi, String mcc, String mnc, String userId) throws Exception;

}
