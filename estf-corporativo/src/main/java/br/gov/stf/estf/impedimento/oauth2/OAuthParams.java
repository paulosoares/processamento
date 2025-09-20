package br.gov.stf.estf.impedimento.oauth2;

public class OAuthParams {
	
	private String tokenLocation;
	private String clientId;
	private String clientSecret;
	private String resourceServer;
	private String timeout;
	private String scope;
	
	public String getTokenLocation() {
		return tokenLocation;
	}
	public void setTokenLocation(String tokenLocation) {
		this.tokenLocation = tokenLocation;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getResourceServer() {
		return resourceServer;
	}
	public void setResourceServer(String resourceServer) {
		this.resourceServer = resourceServer;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
}
