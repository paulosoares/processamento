package br.gov.stf.estf.assinatura.visao.jsf.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.ClasseConversaoService;
import br.gov.stf.estf.processostf.model.service.ClasseService;
import br.gov.stf.estf.processostf.model.service.TipoIncidenteJulgamentoService;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.AscendantOrder;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.AbstractJsfFacesBean;

/**
 * 
 * @author Rodrigo.Lisboa
 */
public class DadosBean extends AbstractJsfFacesBean {

	private static final long serialVersionUID = 8397546615576232473L;

	private String classes;
	private List<SelectItem> listaTipoTextoDocumento;
	private List<SelectItem> listaMinistros;
	private List<SelectItem> listaTiposJulgamento;

	public DadosBean() throws ServiceException {
		if (StringUtils.isVazia(classes)) {
			List<Classe> lista = getClasseService().pesquisar();
			List<ClasseConversao> listaAntiga = getClasseConversaoService().pesquisar();

			StringBuffer s = new StringBuffer();
			s.append("classes = new Array(");
			for (int i = 0; i < lista.size(); i++) {
				Classe c = (Classe) lista.get(i);
				s.append("'");
				s.append(c.getId());
				s.append("'");
				if (i != (lista.size() - 1))
					s.append(",");
			}
			s.append(");\n");

			s.append("classesAntigas = new Array(");
			for (int i = 0; i < listaAntiga.size(); i++) {
				ClasseConversao c = (ClasseConversao) listaAntiga.get(i);
				s.append("'");
				s.append(c.getId());
				s.append("'");
				if (i != (listaAntiga.size() - 1))
					s.append(",");
			}
			s.append(");\n");
			s.append("classesNovas = new Array(");
			for (int i = 0; i < listaAntiga.size(); i++) {
				ClasseConversao c = (ClasseConversao) listaAntiga.get(i);
				s.append("'");
				s.append(c.getClasseNova());
				s.append("'");
				if (i != (listaAntiga.size() - 1))
					s.append(",");
			}
			s.append(");\n");
			classes = s.toString();
		}

		if (CollectionUtils.isVazia(listaMinistros)) {
			List<Ministro> ministros = getMinistroService().pesquisarMinistrosAtivos();
			if (ministros != null && ministros.size() > 0) {
				listaMinistros = new ArrayList<SelectItem>();
				listaMinistros.add(new SelectItem("", ""));
				for (Ministro m : ministros) {
					listaMinistros.add(new SelectItem(m.getId(), m.getNome()));
				}
			}
		}

		if (listaTiposJulgamento == null) {
			List<TipoIncidenteJulgamento> tiposJulgamento = getTipoIncidenteJulgamentoService().pesquisar(new AscendantOrder("descricao"));
			if (tiposJulgamento != null && tiposJulgamento.size() > 0) {
				listaTiposJulgamento = new ArrayList<SelectItem>();
				listaTiposJulgamento.add(new SelectItem(null, ""));
				for (TipoIncidenteJulgamento tj : tiposJulgamento) {
					listaTiposJulgamento.add(new SelectItem(tj.getId(), tj.getDescricao()));
				}
			}
		}

		if (CollectionUtils.isVazia(listaTipoTextoDocumento)) {
			List<TipoTexto> tipos = new ArrayList<TipoTexto>();
			tipos.add(TipoTexto.OFICIO);
			tipos.add(TipoTexto.CERTIDAO_DE_DATA);
			tipos.add(TipoTexto.CERTIDAO_DE_PUBLICACAO);
			// List<TipoTexto> tipos =
			// getTipoTextoService().pesquisar(EspecieTexto.CERTIDAO,
			// EspecieTexto.INFORMACAO, EspecieTexto.OFICIO);
			if (tipos != null && tipos.size() > 0) {
				listaTipoTextoDocumento = new ArrayList<SelectItem>();
				listaTipoTextoDocumento.add(new SelectItem("", ""));
				for (TipoTexto tj : tipos) {
					listaTipoTextoDocumento.add(new SelectItem(tj.getCodigo(), tj.getDescricao()));
				}
			}
		}
	}

	private MinistroService getMinistroService() {
		return (MinistroService) getService("ministroService");
	}

	private ClasseService getClasseService() {
		return (ClasseService) getService("classeService");
	}

	private ClasseConversaoService getClasseConversaoService() {
		return (ClasseConversaoService) getService("classeConversaoService");
	}

	private TipoIncidenteJulgamentoService getTipoIncidenteJulgamentoService() {
		return (TipoIncidenteJulgamentoService) getService("tipoIncidenteJulgamentoService");
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public List<SelectItem> getListaTipoTextoDocumento() {
		return listaTipoTextoDocumento;
	}

	public void setListaTipoTextoDocumento(List<SelectItem> listaTipoTextoDocumento) {
		this.listaTipoTextoDocumento = listaTipoTextoDocumento;
	}

	public List<SelectItem> getListaMinistros() {
		return listaMinistros;
	}

	public void setListaMinistros(List<SelectItem> listaMinistros) {
		this.listaMinistros = listaMinistros;
	}

	public List<SelectItem> getListaTiposJulgamento() {
		return listaTiposJulgamento;
	}

	public void setListaTiposJulgamento(List<SelectItem> listaTiposJulgamento) {
		this.listaTiposJulgamento = listaTiposJulgamento;
	}

}
