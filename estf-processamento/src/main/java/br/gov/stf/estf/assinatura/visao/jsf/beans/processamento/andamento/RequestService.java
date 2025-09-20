package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento;

/**
 * Classe com os mecanismos de acesso Rest ao Supremo.Autuação
 * 
 * @author claudinei.silvestre
 * @since 02.10.2015
 */
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.groovy.control.messages.ExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.gov.stf.estf.configuracao.model.service.ConfiguracaoSistemaService;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.security.user.UserHolder;

/**
 * Classe com os mecanismos de acesso Rest ao Supremo.Autuação
 * 
 * @author wellington.galdino
 * @since 04.04.2017
 */

@Service("requestService")
public class RequestService {

   private static final String SISTEMA_PROCESSAMENTO = "PROCESSAMENTO";
   private static final String AUTORIZACAO = "Autorizacao";
   private static final String URL_SERVICO = "ambiente.url";
   private static final String ENDERECO_API_MNIDOIS = "mnidois.api.endereco";
   private static final String CHAVE_PROCESSAMENTO_MNI = "475T5mopGcVwks09jJ13Bn23EBpxQYId";
   private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON. getSubtype(), Charset.forName("utf8"));
   private static final Logger LOGGER = LoggerFactory.getLogger(RequestService.class.getName());
   
   @Autowired
   private ConfiguracaoSistemaService configuracaoSistemaService;

   /**
    * Servico que faz um GET somente para recuperar o Token
    * 
    * @param url
    * @param classe
    * @return o objeto definido em ResponseEntity
    * @throws ExceptionMessage
    */
   public <T> T requestServiceGet(URI uriServico, Class<T> classObjetoRetorno) throws Exception {
      RequestEntity<Void> request = buildGetRequest(uriServico);
      return exchangeRequest(request, classObjetoRetorno);
   }

   public <T> T requestServiceGet(URI uriServico, ParameterizedTypeReference<T> responseType) throws Exception {
      RequestEntity<Void> request = buildGetRequest(uriServico);
      return exchangeRequest(request, responseType);
   }

   /**
    * Serviço que faz um POST para qualquer path RESTFull
    * 
    * @param url
    * @param objeto
    * @param classDoObjeto
    * @return o pr?prio objeto processado
    */
   public <T> T requestServicePost(URI uriServico, Object body, Class<T> classObjetoRetorno) throws Exception {
      RequestEntity<Object> request = buildPostResquest(uriServico, body);
      return exchangeRequest(request, classObjetoRetorno);
   }

   public <T> T requestServicePut(URI uriServico, Class<T> responseType) throws Exception {
      RequestEntity<Void> requisicaoServico = buildHeader(uriServico, RequestEntity.put(uriServico)).build();
      return exchangeRequest(requisicaoServico, responseType);
   }

   public URI buildUrl(String requestServico, String... pathVariable) {
      return buildUrlQueryString(requestServico, null, pathVariable);
   }

   public URI buildUrlQueryString(String requestServico, Map<String, Object> queryString, String... pathVariable) {
      try {
         String url = formatarUrl(requestServico, pathVariable);
         UriComponentsBuilder uriBuild = UriComponentsBuilder.fromHttpUrl(url);
         if (queryString != null && !queryString.isEmpty()) {
            for (Entry<String, Object> entry : queryString.entrySet()) {
               uriBuild.queryParam(entry.getKey(), StringUtil.toString(entry.getValue()));
            }
         }
               
         URI uri = new URI(url);

         return uri;
      } catch (Exception e) {
         throw new IllegalArgumentException("A URI não foi construída por problemas nos argumentos. Verificar as configurações globais.", e);
      }
   }

   /**
    * Recebe um objeto array e transforma em um List<objeto>
    * 
    * @param lista
    * @return
    */

   public <T> List<T> toList(T... lista) {
      if (lista == null || lista.length == 0) {
         return Collections.emptyList();
      }
      List<T> list = Arrays.asList(lista);
      list.removeAll(Collections.singleton(null));
      return list;
   }

   /**
    * Gera uma URL qualquer que n?o esteja armazenada no arquivo de configura??o
    * (DNA)
    * 
    * @param url
    * @return
    * @throws MalformedURLException
    */

   public URL buildFullUrl(String url) throws MalformedURLException {
      return new URL(url);
   }

   public String getUrl(String request) {
      String servico = null;
      String urlServico = null;
      String contexto = null;
      try {
         servico = configuracaoSistemaService.recuperarValor(request);
         if (StringUtil.isEmpty(servico)) {
            LOGGER.error("O serviço " + request + " não está registrado.");
         }
         
         ConfiguracaoSistema configuracaoSistema = configuracaoSistemaService.recuperarValor(SISTEMA_PROCESSAMENTO, URL_SERVICO);
         urlServico = configuracaoSistema.getValor();
         contexto =  configuracaoSistemaService.recuperarValor(ENDERECO_API_MNIDOIS);
      } catch (ServiceException e) {
         LOGGER.error("Erro ao configurar servico rest atuacao!", e);
      }

      return urlServico + contexto + servico;
   }

   private String formatarUrl(String requestServico, String... pathVariable) {
      String url = getUrl(requestServico);
      if (pathVariable != null && pathVariable.length > 0)
         return String.format(url, (Object[]) pathVariable);
      return url;
   }

   private void logRestException(HttpStatusCodeException hsce) {
      StringBuilder builder = new StringBuilder();

      builder.append("\n\n########## PROCESSAMENTO #########\n\n").append("\nErro ao consultar servico REST em: ".toUpperCase())
            .append(hsce.getResponseHeaders().getLocation() != null ? hsce.getResponseHeaders().getLocation().toString() : "").append("\n\nResumo do erro: ".toUpperCase())
            .append(hsce.getMessage()).append("\n\nCorpo da resposta: \n".toUpperCase()).append(hsce.getResponseBodyAsString()).append("\n\n########## PROCESSAMENTO #########\n\n");

      LOGGER.info(builder.toString());
   }

   private <T> T exchangeRequest(RequestEntity<?> request, ParameterizedTypeReference<T> responseType) throws Exception {
      try {
         RestTemplate restTemplate = new RestTemplate();
         return restTemplate.exchange(request, responseType).getBody();
      } catch (HttpStatusCodeException hsce) {
         throw new Exception(hsce.getMessage(), hsce.getCause());
      } catch (ResourceAccessException ee) {
         LOGGER.error(ee.getMessage());
         throw new Exception();
      } catch (RestClientException restException) {
         LOGGER.error(restException.getMessage());
         throw new Exception();
      }
   }

   private <T> T exchangeRequest(RequestEntity<?> request, Class<T> clazz) throws Exception {
      try {
         RestTemplate restTemplate = new RestTemplate();
         return restTemplate.exchange(request, clazz).getBody();
      } catch (HttpStatusCodeException hsce) {
         logRestException(hsce);
         throw new Exception(hsce.getMessage(), hsce.getCause());
      } catch (ResourceAccessException ee) {
         LOGGER.info(ee.getMessage());
         throw new Exception();
      } catch (RestClientException restException) {
         LOGGER.error(restException.getMessage());
         throw new Exception();
      }
   }

   private RequestEntity<Object> buildPostResquest(URI uri, Object body) {
      BodyBuilder build = (BodyBuilder) buildHeader(uri, RequestEntity.post(uri));
      return build.body(body);
   }

   private RequestEntity<Void> buildGetRequest(URI uri) {
      return buildHeader(uri, RequestEntity.get(uri)).build();
   }

   private HeadersBuilder<?> buildHeader(URI uri, HeadersBuilder<?> build) {
      String token = null;
      try {
         token = gerarTokenAutuacao();
      } catch (ServiceException e) {
    	  LOGGER.error("Erro ao gerar o token autuacao", e.getMessage());
      }
      return build.accept(APPLICATION_JSON_UTF8).header(AUTORIZACAO, token);
   }

   private String gerarTokenAutuacao() throws ServiceException {
      String usuarioAutorizado = UserHolder.get().getUsername();
      String token = Jwts.builder() //
            .setSubject(usuarioAutorizado) 
            .signWith(SignatureAlgorithm.HS256, CHAVE_PROCESSAMENTO_MNI) //
            .compact();
      return token;
   }

   public RestTemplate restTemplate() {
      return new RestTemplate();
   }
   
}
