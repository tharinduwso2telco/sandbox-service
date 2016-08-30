package com.wso2telco.services.dep.sandbox.servicefactory;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.ws.rs.core.Response.Status;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.json.JSONException;
import org.json.JSONObject;

import com.wso2telco.dep.oneapivalidation.exceptions.PolicyException;
import com.wso2telco.dep.oneapivalidation.exceptions.RequestError;
import com.wso2telco.dep.oneapivalidation.exceptions.ServiceException;
import com.wso2telco.services.dep.sandbox.dao.AbstractDAO;
import com.wso2telco.services.dep.sandbox.dao.model.custom.RequestDTO;
import com.wso2telco.services.dep.sandbox.dao.model.domain.ManageNumber;
import com.wso2telco.services.dep.sandbox.dao.model.domain.User;

public abstract class AbstractRequestHandler<E2 extends RequestDTO> implements RequestHandleable<RequestDTO> {
	protected Log LOG;
	protected AbstractDAO dao;

	/**
	 * internally used to indicate the type of exception being stored is a
	 * ServiceException
	 */
	protected static final int SERVICEEXCEPTION = 1;
	/**
	 * internally used to indicate the type of exception being stored is a
	 * PolicyException
	 */
	protected static final int POLICYEXCEPTION = 2;

	public final Returnable execute(final RequestDTO requestDTO) throws Exception {
		E2 wrapperDTO = (E2) requestDTO;
		init(wrapperDTO);

		/**
		 * load user from httpheader .get("sandbox")
		 */
		String sandboxusr = requestDTO.getSandbox();
		LOG.debug("Sandbox user : " + sandboxusr);

		/**
		 * load user based on JWT_TOKEN
		 */
		if (sandboxusr == null) {
			sandboxusr = getProfileIdFromRequest(requestDTO);
		}

		/**
		 * load user domain object from db
		 */
		final User user = dao.getUser(sandboxusr);

		requestDTO.setUser(user);

		validate(wrapperDTO);
		
		/**
		 * if the current instance does not contain address
		 */
		if (!(this instanceof AddressIgnorerable)) {
			
			List<String> userNotWhiteListed = getNotWhitelistedNumbers(user);
			if (userNotWhiteListed != null && !userNotWhiteListed.isEmpty()) {
				Returnable responseDTO = getResponseDTO();

				LOG.debug("Location parameters are empty");
				responseDTO.setRequestError(
						constructRequestError(SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1",
								StringUtils.join(userNotWhiteListed.toArray()) + " Not Whitelisted"));
				responseDTO.setHttpStatus(Status.BAD_REQUEST);
				return responseDTO;
			}
		}

		return process(wrapperDTO);

	}

	private List<String> getNotWhitelistedNumbers(User user) throws Exception {
		List<String> userNumList = new ArrayList<String>();
		List<String> enterdList = getAddress();
		for (String address : enterdList) {
			userNumList.add(getLastMobileNumber(address).trim());
		}

		List<ManageNumber> wlnumber = dao.getWhitelisted(user.getId(), userNumList);

		if (wlnumber != null) {
			for (ManageNumber manageNumber : wlnumber) {
				userNumList.remove(manageNumber.getNumber().trim());
			}
		}

		return userNumList;
	}

	protected abstract Returnable getResponseDTO();

	protected abstract List<String> getAddress();

	protected abstract boolean validate(E2 wrapperDTO) throws Exception;

	protected abstract Returnable process(final E2 extendedRequestDTO) throws Exception;

	protected abstract void init(final E2 extendedRequestDTO) throws Exception;

	protected String getLastMobileNumber(String str) {
		return str.substring(Math.max(0, str.length() - 11));
	}

	private String getProfileIdFromRequest(RequestDTO requestDTO) {

		String jwtsubs = null;
		final String authtoken = requestDTO.getJWTToken();
		LOG.debug("AuthToken: " + requestDTO.getJWTToken());

		if (authtoken != null) {
			String[] jwttoken = authtoken.split("\\.");

			Decoder decoder = Base64.getDecoder();
			byte[] decoded = decoder.decode(jwttoken[1].getBytes());
			String jwtbody = new String(decoded);

			JSONObject jwtobj = null;
			try {
				jwtobj = new JSONObject(jwtbody);
				jwtsubs = jwtobj.get("http://wso2.org/claims/subscriber").toString();
			} catch (JSONException ex) {
				LOG.error("", ex);
			}
			LOG.debug("requestedUser: " + jwtsubs);

			// replyData = Getters.getProfileIdByUsername(jwtsubs);
		}
		// Method Body has to implement!
		return jwtsubs;
	}

	protected RequestError constructRequestError(int type, String messageId, String text, String variable) {
		RequestError error = new RequestError();
		if (type == SERVICEEXCEPTION) {
			ServiceException serviceException = new ServiceException(messageId, text, variable);
			error.setServiceException(serviceException);

		} else if (type == POLICYEXCEPTION) {
			PolicyException policyException = new PolicyException(messageId, text, variable);
			error.setPolicyException(policyException);
		}
		return error;
	}
}
