/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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

package com.wso2telco.dep.tpservice.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wso2telco.dep.tpservice.pool.PoolFactory;
import com.wso2telco.dep.tpservice.pool.TokenPool;
import com.wso2telco.dep.tpservice.util.exception.TokenException;

@Api(value = "/tokenservice", description = "This service provide a rest service for providing a valid token")
@Path("/tokenservice")
public class TokenPoolService {
	 
	@GET
	@Path("/{ownerID}")
	@ApiOperation(value = "Get the valid token", notes = "API for return the contact by given the id", response = String.class)
	public String get(
			@ApiParam(value = "owner Id of token to fetch", required = true) @PathParam("ownerID") String id) {
		// TODO: Implementation for HTTP GET request

		try {
			TokenPool pool = PoolFactory.getInstance().getTokenPool(id);
			return pool.accqureToken().getAccessToken();
		} catch (TokenException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Hello from WSO2 MSF4J";
	}

	@POST
	@Path("/refresh/{wonerid}/{tokenID}")
	@ApiOperation(value = "New token details", notes = "API for update token create ")
	@ApiResponses({ 
				@ApiResponse(code = 201, message = "Successfully created the contact"),
				@ApiResponse(code = 400, message = "Bad Request ") })

	public void post(@ApiParam(value = "Owner of the token ", required = true) @PathParam("wonerid") String wonerid) {
		// TODO: Implementation for HTTP POST request
		System.out.println("POST invoked");
	}

	@PUT
	@Path("/refresh/{wonerid}/{tokenID}")
	@ApiOperation(value = "Re -fresh  token ", notes = "Re genarate the token using refresh token ")
	public void put(@ApiParam(value = "token id to update ", required = true) @PathParam("tokenid") String tokenid,
			@ApiParam(value = "new refreshTime to update ", required = true) @FormParam("refreshTime") String refreshTime) {
		// TODO: Implementation for HTTP PUT request
		System.out.println("PUT invoked");
	}

	@DELETE
	@Path("/{tokenID}")
	@ApiOperation(value = "Delete token ", notes = "Delete token ")
	public void delete(
			@ApiParam(value = "token Id  to delete ", required = true) @PathParam("tokenid") String tokenid) {
		// TODO: Implementation for HTTP DELETE request
		System.out.println("DELETE invoked");
	}
}
