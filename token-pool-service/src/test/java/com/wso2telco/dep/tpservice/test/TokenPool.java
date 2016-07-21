package com.wso2telco.dep.tpservice.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TokenPool implements Runnable{

	private Log log = LogFactory.getLog(TokenPool.class);
	protected boolean connected = false;
	private String owner;
	private String url;
	int count=0;

	public TokenPool(String owner, String url) {
		this.url = url;
		this.owner = owner;

	}

	public void print( ) throws Exception {
		String message=getAccessToken();
		disableCertificateValidation();
			System.out.println("Access Token Generated"+owner+ "  " + message + "  " + OwnerManager.total++);
		// JSONObject jsontoken = new JSONObject(a);
		// String response =(jsontoken.get(("RestResponse")).toString());
		//log.info(response);
			makeRequest();
			OwnerManager.totalcount.incrementAndGet();
	}

	/**
	 * @get the value of token
	 */
	protected String getAccessToken() throws Exception {
		// String param1="owner1";
		//String url = "http://localhost:8181/tokenservice/owner1";
		//String url = url + "/" + owner;
		String token = null;
		// String.format(param1,"owner1");
		URL obj = new URL(url + owner);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		token = response.toString();
		return token;
	}

	/**
	 * @Make an API call
	 */
	protected String makeRequest() throws Exception {
		String neturl = "https://localhost:8243/countryapi/1.0";
		URL obj = new URL(neturl);
		StringBuffer retStr = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

		try {
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("Authorization", "Bearer "
					+ getAccessToken());
					connection.setUseCaches(false);

			// filter out invalid http codes
			if ((connection.getResponseCode() == Status.OK.getStatusCode())
					|| (connection.getResponseCode() == Status.CREATED
							.getStatusCode())) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
				
			}

			br = new BufferedReader(new InputStreamReader(is));
			String output;
			while ((output = br.readLine()) != null) {
				retStr.append(output);
			}
		} catch (Exception e) {
			log.error(e);
			// throw new TokenException();
		} finally {
		}
		try {
			br.close();
		} catch (IOException e) {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return retStr.toString();
	}

	/**
	 * @method invocation for SSL certificate skipping
	 */
	public void disableCertificateValidation() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		// Ignore differences between given hostname and certificate hostname
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		try {
			print();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
