/*******************************************************************************
 * Copyright (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) 
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

package com.wso2telco.ussdstub.main;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.ussdstub.pojo.Request;

/**
 * The Class Service.
 * @author Yasith Lokuge
 */
@Path("/ussd")
public class Service { 
    
    /**
     * Post.
     *
     * @param request the request
     * @param senderAddress the sender address
     * @return the response
     * @throws RemoteException the remote exception
     * @throws SQLException the SQL exception
     * @throws Exception the exception
     */
    @POST
    @Path("outbound/{senderAddress}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(Request request, @PathParam("senderAddress") String senderAddress) throws RemoteException, SQLException, Exception{              
        
    	Gson gson = new GsonBuilder().serializeNulls().create();
    	String jsonBody = gson.toJson(request);  	
    	
    	Endpoints endpoints = new Endpoints();
    	return endpoints.sendUSSDOneAPI(jsonBody, senderAddress);		
    }
    
}
