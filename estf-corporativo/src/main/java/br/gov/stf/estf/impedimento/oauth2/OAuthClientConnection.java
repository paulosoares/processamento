package br.gov.stf.estf.impedimento.oauth2;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientConnection {

	@Autowired
	private OAuthParams oAuthParams;
	
	public String getAccessToken() throws OAuthSystemException, OAuthProblemException {
		OAuthClientRequest request = OAuthClientRequest.tokenLocation(oAuthParams.getTokenLocation()).setScope(oAuthParams.getScope())
				.setGrantType(GrantType.CLIENT_CREDENTIALS).setClientId(oAuthParams.getClientId())
				.setClientSecret(oAuthParams.getClientSecret()).buildQueryMessage();
		
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		
		OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);
		System.getProperties().setProperty("sun.net.client.defaultConnectTimeout", oAuthParams.getTimeout());
		System.getProperties().setProperty("sun.net.client.defaultReadTimeout", oAuthParams.getTimeout());
		return oAuthResponse.getAccessToken();
	}

	// Desabilita a validação de certificado
	static {
		disableSslVerification();
	}
	
	private static void disableSslVerification() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}
}
