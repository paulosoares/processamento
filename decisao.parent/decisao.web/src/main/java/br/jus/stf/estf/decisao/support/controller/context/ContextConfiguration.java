package br.jus.stf.estf.decisao.support.controller.context;

import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.Component;

import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.web.comunicacao.ComunicacaoFacesBean;
import br.jus.stf.estf.decisao.pesquisa.web.incidente.IncidenteFacesBean;
import br.jus.stf.estf.decisao.pesquisa.web.incidente.ListaIncidentesFacesBean;
import br.jus.stf.estf.decisao.pesquisa.web.texto.ListaTextosFacesBean;
import br.jus.stf.estf.decisao.pesquisa.web.texto.TextoFacesBean;

/**
 * Armazena metainformações relacionadas aos possíveis contextos de apresentação.
 * 
 * @author Rodrigo Barreiros
 * @since 04.05.2010
 */
public class ContextConfiguration {
	
	private static final Map<Class<?>, ContextConfiguration> beans = new HashMap<Class<?>, ContextConfiguration>();
	
	private Class<?> beanClass;
	private String viewPage;
	private String listPage;
	
	static {
		beans.put(ObjetoIncidenteDto.class, new ContextConfiguration(IncidenteFacesBean.class, "/pesquisa/incidente.xhtml", "/pesquisa/incidentes.xhtml"));
		beans.put(ListaIncidentesDto.class, new ContextConfiguration(ListaIncidentesFacesBean.class, null, "/pesquisa/listas.xhtml"));
		beans.put(ListaTextosDto.class, new ContextConfiguration(ListaTextosFacesBean.class, null, "/pesquisa/listas.xhtml"));
		beans.put(TextoDto.class, new ContextConfiguration(TextoFacesBean.class, "/pesquisa/texto.xhtml", "/pesquisa/textos.xhtml"));
		beans.put(ComunicacaoDto.class, new ContextConfiguration(ComunicacaoFacesBean.class, null, "/pesquisa/comunicacoes.xhtml"));
	}
	
	private ContextConfiguration(Class<?> beanClass, String viewPage, String listPage) {
		this.beanClass = beanClass;
		this.viewPage = viewPage;
		this.listPage = listPage;
	}

	@SuppressWarnings("unchecked")
	public static <T> FacesBean<T> getFacesBean(Class<T> clazz) {
		return (FacesBean<T>) Component.getInstance(beans.get(clazz).beanClass);
	}
	
	public static <T> String getViewPage(Class<T> clazz) {
		return beans.get(clazz).viewPage;
	}
	
	public static <T> String getListPage(Class<T> clazz) {
		return beans.get(clazz).listPage;
	}
	
}
