package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentos;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.assinatura.BeanAssinatura;
import br.gov.stf.estf.assinatura.visao.util.OrdenacaoUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanConsultarDocumentosUnidade extends AssinadorBaseBean {

	private static final long serialVersionUID = -8059606167034592897L;
	private static final Log LOG = LogFactory.getLog(BeanConsultarDocumentosUnidade.class);

	// ##################### VARIAVEIS DE SESSAO ###########################

	private static final String KEY_LISTA_DOCUMENTOS = BeanConsultarDocumentosUnidade.class.getCanonicalName() + ".listaDocumentos";
	public static final Object ITENS_FASE_DOCUMENTO = BeanAssinatura.class.getCanonicalName() + ".itensFaseDocumento";

	private Date dataPesquisa;
	private Date dataAtual;

	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> listaDocumentos;

	private HtmlDataTable tabelaDocumentos;

	public BeanConsultarDocumentosUnidade() {
		restaurarSessao();
		dataAtual = new Date();
	}

	@SuppressWarnings("unchecked")
	private void restaurarSessao() {
		restoreStateOfHttpSession();
		setListaDocumentos((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));
		atualizarSessao();
	}

	public void atualizarSessao() {
		applyStateInHttpSession();
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
	}

	// ############################# ACTION ################################

	public void pesquisarDocumentosAction(ActionEvent evt) {
		pesquisarDocumentos();
		atualizarSessao();
	}

	public void atualizaSessaoAction(ActionEvent evt) {
		atualizarSessao();
	}

	// ############################# METHODS #################################

	public void limparCampos() {
		setListaDocumentos(null);
		setDataPesquisa(null);
		atualizarSessao();
	}

	public void pesquisarDocumentos() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		List<ComunicacaoDocumentoResult> documentos = null;

		try {
			documentos = comunicacaoServiceLocal.pesquisarDocumentos(dataPesquisa, getSetorUsuarioAutenticado());

			if (CollectionUtils.isVazia(documentos)) {
				reportarAviso("Nenhum documento encontrado.");
			} else {
				reportarInformacao(MessageFormat.format("Foi(ram) encontrado(s) {0} documento(s).", documentos.size()));
			}
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar os documentos.", exception, LOG);
		}
		
		OrdenacaoUtils.ordenarListaComunicacaoDocumentoResultProcesso(documentos);
		setListaDocumentos(getCheckableDataTableRowWrapperList(documentos));
	}

	public void validarData(FacesContext context, UIComponent component, Object value) {
		Date data = (Date) value;

		if (data.after(dataAtual)) {
			throw new ValidatorException(new FacesMessage("Não é possível pesquisar por data futura.", ""));
		}
	}

	// ########################### GETS AND SETs #############################

	public HtmlDataTable getTabelaDocumentos() {
		return tabelaDocumentos;
	}

	public void setTabelaDocumentos(HtmlDataTable tabelaDocumentos) {
		this.tabelaDocumentos = tabelaDocumentos;
	}

	public List<CheckableDataTableRowWrapper> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<CheckableDataTableRowWrapper> listaDocumentos) {
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		this.listaDocumentos = listaDocumentos;
	}

	public String getListaDocumentoSize() {
		return listaDocumentos.size() + "";
	}

	public Date getDataPesquisa() {
		return dataPesquisa;
	}

	public void setDataPesquisa(Date dataPesquisa) {
		this.dataPesquisa = dataPesquisa;
	}

}
