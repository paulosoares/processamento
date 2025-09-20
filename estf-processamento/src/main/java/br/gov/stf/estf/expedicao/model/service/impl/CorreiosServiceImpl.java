package br.gov.stf.estf.expedicao.model.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.gov.stf.estf.configuracao.model.service.ConfiguracaoSistemaService;
import br.gov.stf.estf.correios.dto.PrePostagemResquest;
import br.gov.stf.framework.model.service.ServiceException;

@Service("correiosService")
public class CorreiosServiceImpl implements CorreiosService {

	private Log logger = LogFactory.getLog(getClass());
	private RestTemplate restTemplate = new RestTemplate();

	// Constantes para buscar as informações no banco de dados
	private static final String SISTEMA_PROCESSAMENTO = "PROCESSAMENTO";
	private static final String CHAVE_API_URL = "correios.api.url";
	private static final String CHAVE_API_LOGIN = "correios.api.login";
	private static final String CHAVE_API_SENHA = "correios.api.senha";
	private static final String CHAVE_API_NUMERO_CARCAO = "correios.api.numerocartao";

	private String apiUrl = "";
	private String usuario = "";
	private String senha = "";
	private String numeroCartao = "";
	private String token;
	private String expiraEm;
	private String numeroContrato;
	private String cnpj;
	private List<String> servicos;

	long lastTime = new Date().getTime() - 1000;

	public ConfiguracaoSistemaService configuracaoSistemaService;

	public CorreiosServiceImpl(ConfiguracaoSistemaService configuracaoSistemaService) {
		
		try {
			apiUrl = configuracaoSistemaService.recuperarValor(CHAVE_API_URL);
			usuario = configuracaoSistemaService.recuperarValor(CHAVE_API_LOGIN);
			senha = configuracaoSistemaService.recuperarValor(CHAVE_API_SENHA);
			numeroCartao = configuracaoSistemaService.recuperarValor(CHAVE_API_NUMERO_CARCAO);
		} catch (Exception e) {
			logger.error("Falha ao recuperar parâmetros do DNA: " + e.getMessage(), e);
		}

		// Produção
		// apiUrl = "https://api.correios.com.br";
		// usuario = "livraria";
		// senha = "mFNORgwzVSi3qhiXzDkhdLO8ejorUdysKUdhFtWu";
		// numeroCartao = "0076903303";

		// Homologação
//		apiUrl = "https://apihom.correios.com.br";
//		usuario = "00531640000128";
//		senha = "cGfGwxqjngz7QvIiwOFCiGrBEwlIUpa2DpQhZmEd";
//		numeroCartao = "0076903338";
		
		restTemplate.setInterceptors(getInterceptors());
	}

	private List<ClientHttpRequestInterceptor> getInterceptors() {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();

		ClientHttpRequestInterceptor interceptor1 = new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

				if (!isTokenValido())
					autenticarCartaoPostagem();

				request.getHeaders().set("Authorization", "Bearer " + token);

				return execution.execute(request, body);
			}
		};

		interceptors.add(interceptor1);
		return interceptors;
	}

	private boolean isTokenValido() {
		try {
			if (token == null || token.isEmpty() || expiraEm == null)
				return false;

			Date agora = new Date();

			// Calcula o horário de "janela de renovação"
			Calendar limiteRenovacao = Calendar.getInstance();
			limiteRenovacao.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(expiraEm));
			limiteRenovacao.add(Calendar.MINUTE, -30); // 30 minutos antes de expirar o último token

			// Verifica se já estamos dentro da janela de renovação
			return (!agora.after(limiteRenovacao.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return true;
	}

	private void autenticarCartaoPostagem() {

		// Não permite executar este método mais do que 1 vez por segundo para evitar
		// erro HTTP 429 - Too Many Requests.
		long agora = new Date().getTime();

		if (agora - lastTime < 1000)
			return;
		else
			lastTime = agora;

		if (isTokenValido())
			return;

		// Login com Authorization Basic passando o número do cartão
		String plainCreds = usuario + ":" + senha;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic " + base64Creds);

		JSONObject request = new JSONObject();
		request.put("numero", numeroCartao);

		HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

		try {
			restTemplate = new RestTemplate();

			// faz o login
			ResponseEntity<String> loginResponse = restTemplate.exchange(getUri("/token/v1/autentica/cartaopostagem"), HttpMethod.POST, entity, String.class);

			JSONObject response = new JSONObject(loginResponse.getBody());

			// seta os atributos restantes
			token = response.getString("token");
			expiraEm = response.getString("expiraEm");
			cnpj = response.getString("cnpj");
			numeroContrato = ((JSONObject) response.get("cartaoPostagem")).getString("contrato");

			restTemplate.setInterceptors(getInterceptors());
		} catch (Exception e) {
			logger.error("Falha ao autenticar na API dos Correios: " + e.getMessage(), e);
		}
	}

	private URI getUri(String path, String... args) {
		try {
			return new URI(String.format(apiUrl + path, args));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getServicos() throws ServiceException {
		if (servicos == null || servicos.isEmpty()) {
			autenticarCartaoPostagem();
			try {
				URI uri = getUri("/meucontrato/v1/empresas/%s/contratos/%s/servicos?page=0&size=500", cnpj, numeroContrato);
				ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, String.class);

				JSONObject body = new JSONObject(response.getBody());
				JSONArray itensArray = (JSONArray) body.get("itens");

				servicos = new ArrayList<String>();

				for (int i = 0; i < itensArray.length(); i++)
					servicos.add(((JSONObject) itensArray.get(i)).getString("codigo"));
			} catch (RestClientException e) {
				try {
					JSONArray erros = new JSONObject(new String(((HttpClientErrorException) e).getResponseBodyAsByteArray(), "UTF-8")).getJSONArray("msgs");
					throw new ServiceException(erros.toString());
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		}

		return servicos;
	}

	@Override
	public PrePostagem enviar(PrePostagemResquest prePostagemRequest) throws ServiceException {
		try {
			URI uri = getUri("/prepostagem/v1/prepostagens");
			logger.info("REQUEST: " + new JSONObject(prePostagemRequest));
			HttpEntity<PrePostagemResquest> entity = new HttpEntity<PrePostagemResquest>(prePostagemRequest);
			ResponseEntity<PrePostagem> prePostagem = restTemplate.exchange(uri, HttpMethod.POST, entity, PrePostagem.class);
			logger.info("RESPONSE: " + new JSONObject(prePostagem));
			return prePostagem.getBody();
		} catch (RestClientException e) {
			try {
				JSONArray erros = new JSONObject(new String(((HttpClientErrorException) e).getResponseBodyAsByteArray(), "UTF-8")).getJSONArray("msgs");
				throw new ServiceException(erros.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
}
