/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.services.dep.sandbox.dao;

import com.wso2telco.services.dep.sandbox.dao.model.domain.AttributeValues;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;

public interface PaymentDAO {

    public AttributeValues checkDuplicateValue(String serviceCall, String value, String attributeName, String tableName)
            throws Exception;
    public Integer saveAttributeValue(AttributeValues valueObj) throws Exception;
    public AttributeValues getResponse(Integer id) throws Exception;
    public boolean saveManageNumbers(ManageNumber manageNumber) throws Exception;
}
