package br.jus.stf.estf.decisao.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.ApplicationFactory;
import br.jus.stf.estf.decisao.objetoincidente.service.ReferendarDecisaoService;
import br.jus.stf.estf.decisao.support.security.Principal;

// TODO: refatorar para usar o spring-webmvc em vez do servlet
@Controller
public class ReferendarDecisaoController extends HttpServlet {

	private static final long serialVersionUID = -1966350281237303814L;

	ReferendarDecisaoService referendarDecisaoService;

	private WebApplicationContext context;

	private ServletContext sc;

	public ReferendarDecisaoController() {
		super();
	}

	private List<ReferendarDecisaoResultadoDto> referendarDecisao(List<ReferendarDecisaoDto> lista, Principal principal) {
		try {
			referendarDecisaoService = (ReferendarDecisaoService) getService("referendarDecisaoService");
			List<ReferendarDecisaoResultadoDto> result = referendarDecisaoService.referendarDecisao(lista, principal);
			return result;
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		sc = request.getSession().getServletContext();
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);

		List<ReferendarDecisaoDto> lista = carregarEntidades(request);

		List<ReferendarDecisaoResultadoDto> resultado = referendarDecisao(lista, getPrincipal());

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(resultado);
		response.getWriter().print(jsonString);
	}

	private List<ReferendarDecisaoDto> carregarEntidades(HttpServletRequest request) throws IOException, JsonParseException, JsonMappingException {
		String json = IOUtils.toString(request.getReader());

		ObjectMapper mapper = new ObjectMapper();
	    mapper.setSerializationInclusion(Include.NON_NULL);
		List<ReferendarDecisaoDto> lista = mapper.readValue(json, new TypeReference<List<ReferendarDecisaoDto>>() {
		});

		return lista;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	public Object getService(String nomeServico) {
		return ApplicationFactory.getInstance().getServiceLocator(context).getService(nomeServico);
	}

	/**
	 * Recupera o usuário autenticado. Esse usuário é encapsulado em um objeto Principal que contém as credenciais do usuário.
	 * 
	 * @return o principal
	 */
	protected Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}