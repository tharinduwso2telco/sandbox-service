package com.wso2telco.services.dep.sandbox.servicefactory;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.logging.Level;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.simple.JSONObject;

import com.wso2telco.services.dep.sandbox.dao.UserDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

import io.netty.handler.codec.http.HttpRequest;


public abstract class AbstractRequestHandler<E2 extends RequestDTO> implements RequestHandleable<RequestDTO> {
	Log LOG ;
	protected UserDAO userDAO = new UserDAO();

	public final Returnable execute(final RequestDTO requestDTO) throws Exception {
		
		  	String sandboxusr = requestDTO.getSandbox();
	        LOG.debug("Sandbox user : " + sandboxusr);
	        User user = userDAO.getUser(sandboxusr);
	        
	        if (sandboxusr == null) {
	            sandboxusr = getProfileIdFromRequest(request);
	        }

	        user =
	        		
		
		return process((E2) requestDTO);

	}

	protected abstract Returnable process(final E2 extendedRequestDTO) throws Exception;
	
	
	
	private  String getProfileIdFromRequest(RequestDTO requestDTO) {

       
        String jwtsubs = null;
        final String authtoken = requestDTO.getAuthtoken(); 
        System.out.println("AuthToken: " + requestDTO.getAuthtoken());
        
        if (authtoken != null) {
            String[] jwttoken = authtoken.split("\\.");

            Decoder decoder = Base64.getDecoder();
            byte[] decoded = decoder.decode(jwttoken[1].getBytes());
            String jwtbody = new String(decoded);

            JSONObject jwtobj = null;
            try {
                jwtobj = new org.json.JSONObject(jwtbody);
                jwtsubs = jwtobj.get("http://wso2.org/claims/subscriber").toString();
            } catch (JSONException ex) {
                LOG.error("",ex);
            }
            LOG.debug("requestedUser: " + jwtsubs);

            //replyData = Getters.getProfileIdByUsername(jwtsubs);
        }
        //Method Body has to implement!
        return jwtsubs;
    }

	
}
