package com.wso2telco.services.dep.sandbox.dao.model.custom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;

import org.apache.http.HttpRequest;

import com.wso2telco.services.dep.sandbox.dao.model.domain.User;
import com.wso2telco.services.dep.sandbox.util.RequestType;

public class RequestDTO implements Serializable {

	private static final long serialVersionUID = -57225936985453608L;

	private static Map<String, HttpMethod> httpMethodMap = new HashMap<String, HttpMethod>();

	private RequestType requestType;
	protected HttpServletRequest httpRequest;
	final String JWT_TOKEN = "x-jwt-assertion";
	private String apiVersion;
	// TODO:This need to replace with custom dto,instead of domain object
	private User user;

	/**
	 * @return the httpRequest
	 */
	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public String getRequestPath() {

		return httpRequest.getRequestURI();
	}

	public boolean isPost() {
		return HttpMethod.POST.equals(httpRequest.getMethod());
	}

	public boolean isGet() {
		return HttpMethod.GET.equals(httpRequest.getMethod());
	}

	public boolean isPut() {
		return HttpMethod.PUT.equals(httpRequest.getMethod());
	}

	public boolean isDelete() {
		return HttpMethod.DELETE.equals(httpRequest.getMethod());
	}

	public String getSandbox() {
		return httpRequest.getHeader("sandbox");
	}

	public String getJWTToken() {

		return httpRequest.getHeader(JWT_TOKEN);

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
}
