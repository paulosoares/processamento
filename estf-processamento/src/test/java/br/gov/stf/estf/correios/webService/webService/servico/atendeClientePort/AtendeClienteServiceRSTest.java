package br.gov.stf.estf.correios.webService.webService.servico.atendeClientePort;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AtendeClienteServiceRSTest {
// Dados para auteticação PRODUÇÃO
//	public static String apiUrl = "https://api.correios.com.br";
 //	public static String USUARIO = "livraria";
 //	public static String SENHA = "mFNORgwzVSi3qhiXzDkhdLO8ejorUdysKUdhFtWu";
 //	public static String NUMERO_CARTAO = "0076903303";
	
	// Dados para auteticação HOMOLOGAÇÃO
	public static String apiUrl = "https://apihom.correios.com.br";
	//public static String USUARIO = "00531640000128";
	public static String USUARIO = "livraria";
	public static String SENHA = "cGfGwxqjngz7QvIiwOFCiGrBEwlIUpa2DpQhZmEd";
	public static String NUMERO_CARTAO = "0076903303";
		
	// Dados recuperados da API
	public static String TOKEN;
	public static String EXPIRA_EM;
	public static String NUMERO_CONTRATO;
	public static String CNPJ;
	public static List<String> SERVICOS;
	
	//@Test
	public void autenticarCartaoPostagemTest() {
		autenticarCartaoPostagem();
	}
	
	public void autenticarCartaoPostagem() {
		try {
			URI url = new URI(apiUrl + "/token/v1/autentica/cartaopostagem");
			
			String plainCreds = USUARIO+":"+SENHA;
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
			String base64Creds = new String(base64CredsBytes);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Basic " + base64Creds);

			JSONObject request = new JSONObject();
			request.put("numero", NUMERO_CARTAO);
			
			HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

			String loginResponse = new RestTemplate().postForObject(url, entity, String.class);
			
		    JSONObject response = new JSONObject(loginResponse);
		    
		    TOKEN = response.getString("token");
		    EXPIRA_EM = response.getString("expiraEm");
		    
		    CNPJ = response.getString("cnpj");
		    NUMERO_CONTRATO = ((JSONObject) response.get("cartaoPostagem")).getString("contrato");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void validarToken() {
		tokenValido(TOKEN, EXPIRA_EM);
	}
	
	public boolean tokenValido(String token, String expiraEm) {
		
		if (token == null || expiraEm == null)
			return false;
		
		try {
			Date hoje = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date expiraEmDate = sdf.parse(expiraEm);
			
			Long limite = expiraEmDate.getTime() - 30*60*1000; // meia hora antes
			expiraEmDate.setTime(limite);
			
			return hoje.before(expiraEmDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//@Test
	public void recuperarContrato() {
		
		if (!tokenValido(TOKEN, EXPIRA_EM))
			autenticarCartaoPostagem();
		
		try {
			URI url = new URI(apiUrl + "/meucontrato/v1/empresas/"+CNPJ+"/contratos?status=ATIVO&vigente=S");
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + TOKEN);
			
			HttpEntity<String> entity = new HttpEntity<String>(null, headers);

			ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
			
			JSONObject body = new JSONObject(new JSONArray(response.getBody()).get(0).toString());
			NUMERO_CONTRATO = body.getString("nuContrato");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void recuperarServicosTest() {
		recuperarServicos();
	}
	
	public List<String> recuperarServicos() {
		List<String> servicos = new ArrayList<String>();

		if (!tokenValido(TOKEN, EXPIRA_EM))
			autenticarCartaoPostagem();
		
		try {
			URI url = new URI(apiUrl + "/meucontrato/v1/empresas/"+CNPJ+"/contratos/"+NUMERO_CONTRATO+"/servicos?page=0&size=500");
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Bearer " + TOKEN);
			
			HttpEntity<String> entity = new HttpEntity<String>(null, headers);

			ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
			
			JSONObject body = new JSONObject(response.getBody());
			JSONArray itensArray = (JSONArray) body.get("itens");
			
			for (int i=0; i<itensArray.length(); i++)
				servicos.add(((JSONObject)itensArray.get(i)).getString("codigo"));
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return servicos;
	}
			
	
}
