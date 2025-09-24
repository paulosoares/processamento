package br.jus.stf.estf.decisao.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.ApplicationFactory;

// TODO: refatorar para usar o spring-webmvc em vez do servlet
@Controller
public class RecuperarSessaoController extends HttpServlet {

	private static final long serialVersionUID = -1966350281237303814L;

	SessaoService sessaoService;

	private WebApplicationContext context;

	private ServletContext sc;

	public RecuperarSessaoController() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		sc = request.getSession().getServletContext();
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		sessaoService = (SessaoService) getService("sessaoService");
		
		String dataParam = request.getParameter("dataLiberacao");
		String colegiadoParam = request.getParameter("colegiado");
		String ignorarCpcParam = request.getParameter("ignorarCpc");

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);

		SimpleDateFormat sdfResquest = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfResponse = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		try {
			Calendar dataLiberacaoCalendar = new GregorianCalendar();
			dataLiberacaoCalendar.setTime(sdfResquest.parse(dataParam));
			Colegiado colegiado = new Colegiado();
			colegiado.setId(colegiadoParam);
			
			Sessao sessao = sessaoService.recuperarSessao(dataLiberacaoCalendar, colegiado, Boolean.parseBoolean(ignorarCpcParam), TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO);
			
			ObjectMapper mapper = new ObjectMapper();
			
			Map<String, String> r = new LinkedHashMap<String, String>();
			
			if (sessao != null && sessao.getId()!= null)
				r.put("id", String.valueOf(sessao.getId()));
			
			r.put("dataInicio", sdfResponse.format(sessao.getDataPrevistaInicio()));
			r.put("dataFim", sdfResponse.format(sessao.getDataPrevistaFim()));
			
			String jsonString = mapper.writeValueAsString(r);
			response.getWriter().print(jsonString);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public Object getService(String nomeServico) {
		return ApplicationFactory.getInstance().getServiceLocator(context).getService(nomeServico);
	}
}