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
package com.wso2telco.services.dep.sandbox.service;

import com.wordnik.swagger.annotations.*;
import com.wso2telco.services.dep.sandbox.dao.model.custom.LocationRequestWrapperDTO;
import com.wso2telco.services.dep.sandbox.exception.SandboxException;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestBuilderFactory;
import com.wso2telco.services.dep.sandbox.servicefactory.RequestHandleable;
import com.wso2telco.services.dep.sandbox.servicefactory.Returnable;
import com.wso2telco.services.dep.sandbox.util.RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/location/v1")
@Produces({MediaType.APPLICATION_JSON})
@Api(value = "location", description = "Rest Service for Location API")
public class LocationService {

    Log LOG = LogFactory.getLog(LocationService.class);

    @GET
    @Path("/queries/location")
    @ApiOperation(value = "locationService", notes = "locationService", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sandbox", value = "Authorization token", required = true, dataType = "string", paramType = "header") })
    public Response location(
            @ApiParam(value = "address", required = true) @QueryParam("address") String address, @ApiParam(value = "requestedAccuracy", required = true) @QueryParam("requestedAccuracy") String requestedAccuracy,
            @Context HttpServletRequest request) {
        LOG.debug("address={address}&requestedAccuracy={requestedAccuracy} invorked :" + address +" " +requestedAccuracy);
        LocationRequestWrapperDTO requestDTO = new LocationRequestWrapperDTO();
        requestDTO.setHttpRequest(request);
        requestDTO.setAddress(address);
        requestDTO.setRequestedAccuracy(requestedAccuracy);
        requestDTO.setRequestType(RequestType.LOCATION);

        RequestHandleable handler = RequestBuilderFactory.getInstance(requestDTO);
        Returnable returnable = null;

        try {
            returnable = handler.execute(requestDTO);
            Response response = Response.status(returnable.getHttpStatus()).entity(returnable.getResponse()).build();
            LOG.debug("Location SERVICE RESPONSE : " + response);
            return response;
        } catch (Exception ex) {
            LOG.error("Location SERVICE ERROR : ", ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    SandboxException.SandboxErrorType.SERVICE_ERROR.getCode() + " " + SandboxException.SandboxErrorType.SERVICE_ERROR.getMessage())
                    .build();
        }

    }



}
