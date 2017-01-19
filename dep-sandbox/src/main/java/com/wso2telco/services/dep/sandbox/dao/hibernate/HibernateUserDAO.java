/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licenses this file to you under  the Apache License, Version 2.0 (the "License");
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

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.wso2telco.services.dep.sandbox.dao.UserDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public class HibernateUserDAO extends AbstractDAO implements UserDAO {

	{
		LOG = LogFactory.getLog(HibernateUserDAO.class);
	}
	public User getUser(final String username) throws Exception {
		User user = null;
		try {
			Session sess = getSession();
			user = (User) sess.createQuery("from User where userName = :userName").setParameter("userName", username)
					.getSingleResult();

		} catch (NoResultException ex) {
			LOG.debug("No user found as : "+username);
		} catch (NonUniqueResultException ex2) {
			LOG.warn("Duplicate user found like : "+username);
		} catch (HibernateException e) {
			throw e;
		}
		return user;
	}

	public boolean saveUser(final User user) throws Exception {

		try {
			saveOrUpdate(user);

		} catch (final Exception e) {
			LOG.error("Error in save user...!", e);
			return false;
		}

		return true;
	}

}
