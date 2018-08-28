package com.dashboard.restservicedashboard.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.HashMap;
import java.util.Map;



public abstract class AlmAuthentication {

	public final static String APPLICATION_JSON = "application/json";
	public final static String ACCEPT = "accept";
	public final static String CONTENT = "Content-Type";
	public final static int OK_STATUS_CODE = 200;
	public final static String COOKIE = "Set-Cookie";

	private static final String LOGIN_ENDPOINT = "api/authentication/sign-in";
	private static final String ALM_URL = "https://springqcent.saas.hpe.com/qcbin/";

	public static final Map<String, String> headerMap;
	static
	{
		headerMap = new HashMap<String, String>();
		headerMap.put(ACCEPT, APPLICATION_JSON);
		headerMap.put(CONTENT, APPLICATION_JSON);
	}

	
	private static String getUrlLogin() {
		return ALM_URL + LOGIN_ENDPOINT;
	}	

	public static boolean checkAuth(String username, String password) {
		String loginUrl = getUrlLogin();
		HttpResponse<JsonNode> postResponse = null;
		try {
			postResponse = Unirest.post(loginUrl)
					.basicAuth(username, password)
					.headers(headerMap).asJson();
		} catch (UnirestException e) {
			return false;
		}
		if (OK_STATUS_CODE == postResponse.getStatus()) {
			return true;
		}
		System.out.println(postResponse.getStatus());
		throw new RuntimeException("Error in authentication");
	}

}
