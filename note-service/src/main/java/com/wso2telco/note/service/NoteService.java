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

package com.wso2telco.note.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.note.dao.NoteServiceDAO;
import com.wso2telco.note.dao.model.NoteDTO;
import com.wso2telco.note.exception.NoteException;
import com.wso2telco.note.service.util.YAMLReader;

/**
 * This is the Microservice resource class. See
 * <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/
 * wso2/msf4j#getting-started</a> for the usage of annotations.
 *
 * @since 1.8.0-SNAPSHOT
 */

@Path("/noteservice")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NoteService {

	private static final Log log = LogFactory.getLog(NoteService.class);

	@POST
	public Response addNote(NoteDTO note) {

		Gson gson = new GsonBuilder().serializeNulls().create();
		NoteServiceDAO noteServiceDAO = new NoteServiceDAO();
		Status responseCode = null;
		String responseString = null;

		log.debug("NoteService addNote -> incoming request body : " + gson.toJson(note));
		try {

			noteServiceDAO.addNote(note);
			responseCode = Response.Status.OK;
			responseString = gson.toJson(note);
		} catch (NoteException e) {

			responseCode = Response.Status.BAD_REQUEST;
			responseString = "{" + "\"errorCode\":\"" + e.getErrorCode() + "\"," + "\"errorMessage\":\""
					+ e.getErrorVariable() + "\"" + "}";
			log.error("Error in NoteService addNote : " + e.getMessage());
		}

		log.debug("NoteService addNote -> response code : " + responseCode);
		log.debug("NoteService addNote -> response body : " + responseString);
		return Response.status(responseCode).entity(responseString).build();
	}

	@GET
	@Path("/{clientRef}/{noteTypeDid}")
	public Response getNote(@PathParam("clientRef") String clientRef, @PathParam("noteTypeDid") int noteTypeDid) {

		log.debug("getNote -> clientRef : " + clientRef + " noteTypeDid : " + noteTypeDid);
		Gson gson = new GsonBuilder().serializeNulls().create();
		NoteServiceDAO noteServiceDAO = new NoteServiceDAO();
		NoteDTO note = null;
		Status responseCode = null;
		String responseString = null;

		try {

			note = noteServiceDAO.getNote(clientRef, noteTypeDid);
			responseCode = Response.Status.OK;
			responseString = gson.toJson(note);
		} catch (NoteException e) {

			responseCode = Response.Status.BAD_REQUEST;
			responseString = "{" + "\"errorCode\":\"" + e.getErrorCode() + "\"," + "\"errorMessage\":\""
					+ e.getErrorVariable() + "\"" + "}";
			log.error("Error in NoteService getNote : " + e.getMessage());
		}

		log.debug("NoteService getNote -> response code : " + responseCode);
		log.debug("NoteService getNote -> response body : " + responseString);
		return Response.status(responseCode).entity(responseString).build();
	}
}
