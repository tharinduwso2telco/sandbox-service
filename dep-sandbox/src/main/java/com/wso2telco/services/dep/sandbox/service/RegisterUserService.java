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

package com.wso2telco.services.dep.sandbox.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wso2telco.services.dep.sandbox.dao.UserDAO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class RegisterUserService {

    @POST
    @Path("/user/{userName}/register")
    public Response registerUser(@PathParam("userName") String userName) {
	UserDAO usrD = new UserDAO();
	User usr;
	try {
	    usr = usrD.getUser(userName);
	} catch (Exception e) {
	    return Response.status(Response.Status.BAD_REQUEST).entity("User Registration Failed..!").build();
	    
	}
	if (usr == null) {
	    User user = new User();
	    user.setUserName(userName);
	    user.setUserStatus(1);
	    usrD.saveUser(user);
	    return Response.status(Response.Status.OK).entity("User Successfully Registered..!").build();
	} else {
	    return Response.status(Response.Status.BAD_REQUEST).entity("User Already Registered..!").build();

	}
    }
}
