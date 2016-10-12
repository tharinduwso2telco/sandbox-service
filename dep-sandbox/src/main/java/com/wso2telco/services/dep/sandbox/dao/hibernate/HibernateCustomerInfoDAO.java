/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.services.dep.sandbox.dao.hibernate;

import com.wso2telco.services.dep.sandbox.dao.CustomerInfoDAO;


public class HibernateCustomerInfoDAO extends AbstractDAO implements CustomerInfoDAO {/*
    {
	LOG = LogFactory.getLog(HibernateCustomerInfoDAO.class);
    }

	@Override
	public void saveAttributeMap(AttributeMap attributeMap) throws Exception {
		try {
			saveOrUpdate(attributeMap);
		} catch (Exception ex) {
			LOG.error("Error in SaveAttributeMap " + ex);
			throw ex;
		}
		
	}

	@Override
	public List<AttributeMap> getAttributeMaps(String tobject, String ownerdid, Attribute attribute) throws Exception {
		
		Session session = getSession();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		List<AttributeMap> attributeMaps = new ArrayList<AttributeMap>();
		try{
			StringBuilder hqlQueryBuilder = new StringBuilder();
			if(tobject != null && ownerdid != null){
				hqlQueryBuilder.append("from AttributeMap am ");
				hqlQueryBuilder.append("where ");
				hqlQueryBuilder.append("am.tobject = :tobject");
				hqlQueryBuilder.append(" AND am.ownerdid = :ownerdid");
				parameterMap.put("tobject", tobject);
				parameterMap.put("ownerdid", ownerdid);
				
				if(attribute != null){
					hqlQueryBuilder.append(" AND am.attribute = :attribute");
					parameterMap.put("attribute", attribute);
				}
				
				Query query = session.createQuery(hqlQueryBuilder.toString());
				
				Set<Entry<String, Object>> entrySet = parameterMap.entrySet();
				
				for (Entry<String, Object> entry : entrySet) {
					query.setParameter(entry.getKey(), entry.getValue());
				}
				
				attributeMaps = (List<AttributeMap>) query.getResultList();
			}
			
		}catch(Exception ex){
			LOG.error("Error in getAttributeMaps " + ex);
			throw ex;
		}
		return attributeMaps;
	}

	@Override
	public Attribute getAttribute(String attributeName) throws Exception {
		
		Attribute attribute = null;
		try{
			Session session = getSession();
			attribute = (Attribute) session.createQuery("from Attribute where attribute = :attribute").setParameter("attribute", attributeName)
				    .getSingleResult();
		}catch(NoResultException e){
		    return null;	
		}catch(Exception ex){
			LOG.error("Error in getAttribute " + ex);
			throw ex;
		}
		return attribute;
	}


	
	
	
*/}