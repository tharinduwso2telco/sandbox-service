/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.services.dep.sandbox.servicefactory.user;


import com.wso2telco.services.dep.sandbox.dao.DaoFactory;
import com.wso2telco.services.dep.sandbox.dao.NumberDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.NumberDetailsRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.servicefactory.AbstractRequestHandler;
import com.wso2telco.services.dep.sandbox.servicefactory.AddressIgnorerable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class RetrieveNumberDetailsServiceHandler extends AbstractRequestHandler<NumberDetailsRequestWrapperDTO> implements AddressIgnorerable {

    private NumberDetailsRequestWrapperDTO requestWrapperDTO;
    private RetrieveNumberDetailsServiceResponseWrapper responseWrapper;
    private NumberDAO numberDAO;

    {
        LOG = LogFactory.getLog(RetrieveNumberDetailsServiceHandler.class);
        numberDAO = DaoFactory.getNumberDAO();
    }

    @Override
    protected Returnable getResponseDTO() {
        return responseWrapper;
    }

    @Override
    protected List<String> getAddress() {
        return null;
    }

    @Override
    protected boolean validate(NumberDetailsRequestWrapperDTO wrapperDTO) throws Exception {
        return true;
    }

    @Override
    protected Returnable process(NumberDetailsRequestWrapperDTO extendedRequestDTO) throws Exception {

        if (responseWrapper.getRequestError() != null) {
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            return responseWrapper;
        }

        List<ManageNumber> numberList = new ArrayList<ManageNumber>();
        int userId = extendedRequestDTO.getUser().getId();
        //Getting data from database
        try {
            for (ManageNumber manageNumber : numberDAO.getManageNumbers(userId)) {
                numberList.add(manageNumber);
            }

            JSONArray JSONArray = new JSONArray(numberList);
            responseWrapper.setManageNumber(JSONArray.toString());
            responseWrapper.setHttpStatus(Response.Status.OK);
        } catch (Exception ex) {
            LOG.error(
                    "###USER### Error in processing number detail retrieve request. ",
                    ex);
            responseWrapper.setHttpStatus(Response.Status.BAD_REQUEST);
            throw ex;
        }
        return responseWrapper;
    }

    @Override
    protected void init(NumberDetailsRequestWrapperDTO extendedRequestDTO) throws Exception {

        requestWrapperDTO = extendedRequestDTO;
        responseWrapper = new RetrieveNumberDetailsServiceResponseWrapper();
    }
}






































