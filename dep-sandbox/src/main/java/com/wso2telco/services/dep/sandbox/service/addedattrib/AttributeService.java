package com.wso2telco.services.dep.sandbox.service.addedattrib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ThrowableError;
import com.wso2telco.services.dep.sandbox.dao.AttribDAO;
import com.wso2telco.services.dep.sandbox.dao.hibernate.HibernateAttributeDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeDistribution;
import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import com.wso2telco.services.dep.sandbox.util.ServiceName;

public class AttributeService {
	/**
	 * remove the exsisting attribute values for given api and service ,tObject,and tObjectDid
	 * @param attributeVsValue
	 * @param serviceType
	 * @param tObject
	 * @param tObjectDid
	 * @throws BusinessException
	 */
	public void saveOrUpdate(final Map<String, String> attributeVsValue,
								final ServiceName serviceType,
								final RequestType api,
								final String tObject,
								final int tObjectDid) throws BusinessException{
		AttribDAO dao = new HibernateAttributeDAO();
		/**
		 * load attribute distribution for given api and service
		 */
		List<AttributeDistribution>	distributions = dao.loadServiceAttributes( serviceType, api);
		
		/**
		 * if no attribute distribution define , need to define the meta data fist
		 */
		if(distributions==null || distributions.isEmpty()){
			throw new BusinessException(new ThrowableError() {
				
											@Override
											public String getMessage() {
												return "No attributes defined for given "+api +" | service "+serviceType;
											}
											
											@Override
											public String getCode() {
												return "ATRIB0001";
											}
										});
		}
		
		//If not prepare attribute value for save
		List<AttributeValues> attributeValues = new ArrayList<AttributeValues>();
		for (AttributeDistribution attributeDistribution : distributions) {
			
			//maching value found for the attribute
			if(attributeVsValue.containsKey(attributeDistribution.getAttribute().getAttributeName())){
				AttributeValues valueObj  = new AttributeValues();
				valueObj.setAttributeDistribution( attributeDistribution);
				
				valueObj.setOwnerdid(tObjectDid);
				valueObj.setTobject(tObject);
				valueObj.setValue(attributeVsValue.get(attributeDistribution.getAttribute().getAttributeName() ));
				attributeValues.add(valueObj);
			}
		}
		
		dao.saveOrUpdateAttributesValues(attributeValues, serviceType, api, tObject, tObjectDid);
		
	}
}
