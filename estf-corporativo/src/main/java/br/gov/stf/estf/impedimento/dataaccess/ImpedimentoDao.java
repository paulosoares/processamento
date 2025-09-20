package br.gov.stf.estf.impedimento.dataaccess;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.impedimento.oauth2.OAuthClientConnection;
import br.gov.stf.estf.impedimento.oauth2.OAuthParams;
import br.gov.stf.framework.model.service.ServiceException;

@Repository
public class ImpedimentoDao {
	
	@Autowired
	OAuthClientConnection oAuthClientConnection;
	
	@Autowired
	private OAuthParams oAuthParams;
	
	private Log logger = LogFactory.getLog(getClass());

	private String resource = "/impedimentos/api/impedimentos/ministros/{ministroId}/processos/{objetoIncidenteId}";

	public boolean temImpedimento(Long codMinistro, Long objetoIncidente) throws ServiceException {
		String url = oAuthParams.getResourceServer() + resource
				.replaceAll("\\{ministroId\\}",codMinistro.toString())
				.replaceAll("\\{objetoIncidenteId\\}", objetoIncidente.toString());
		
		int code = 0;
		
		try {
			String token = oAuthClientConnection.getAccessToken();
			if (token != null) {
				url += "?access_token=" + token;
				GetMethod getMethod = new GetMethod(url);
				
				HttpClientParams params = new HttpClientParams();
				params.setSoTimeout(Integer.valueOf(oAuthParams.getTimeout()));
				
				code = new HttpClient(params).executeMethod(getMethod);
				
				if (code == HttpStatus.SC_OK)
					return true;
				
				if (code == HttpStatus.SC_NOT_FOUND)
					return false;
			}
		} catch (Exception e) {
			logger.error("Erro ao acessar o servi�o do STF Digital: " + url);
			throw new ServiceException("N�o ser� poss�vel verificar se h� impedimento/supei��o para este processo/incidente. Servi�o de verifica��o de impedimentos temporariamente indispon�vel");
		}
		
		logger.error("Erro ao acessar o servi�o do STF Digital: " + url);
		throw new ServiceException("N�o ser� poss�vel verificar se h� impedimento/supei��o para este processo/incidente. O servi�o de verifica��o de impedimentos n�o conseguiu processar este processo/incidente.");
	}

}
