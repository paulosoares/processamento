package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentos;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.security.ExpedienteAssinadoResult;
import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanConsultarDocumentosAssinados extends AssinadorBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5708659231479403237L;
	private static final Log LOG = LogFactory.getLog(BeanConsultarDocumentosAssinados.class);
	
	// ##################### VARIAVEIS DE SESSAO #############################
	
	private static final String KEY_LISTA_DOCUMENTOS = BeanConsultarDocumentosAssinados.class.getCanonicalName() + ".listaDocumentos";
	private static final Object ITENSSETORES = new Object();
	private static final Object ITENSUSUARIOS = new Object();
	
	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> listaDocumentos;
	
	private Date dataInicial;
	private Date dataFinal;
	
	@KeepStateInHttpSession
	private Long codigoSetorAtual;
	
	private String siglaUsuarioAtual;
	
	private List<SelectItem> itensUsuarios;
	private List<SelectItem> itensSetores;
	
	private HtmlDataTable tabelaDocumentos;
	
	MinistroPresidente ministroPresidente = null;
	
	public BeanConsultarDocumentosAssinados() {
		restaurarSessao();
	}
	
	@SuppressWarnings("unchecked")
	private void restaurarSessao() {
		restoreStateOfHttpSession();
		
		if (getAtributo(ITENSSETORES) == null) {			
			setAtributo(ITENSSETORES, carregarComboSetores());
		}

		setItensSetores((List<SelectItem>) getAtributo(ITENSSETORES));
		
		if (getAtributo(ITENSUSUARIOS) == null) {
			setAtributo(ITENSUSUARIOS, carregarComboUsuariosDoSetor(codigoSetorAtual));			
		}
		setAtributo(ITENSUSUARIOS, carregarComboUsuariosDoSetor(codigoSetorAtual));
		
		setItensUsuarios((List<SelectItem>) getAtributo(ITENSUSUARIOS));
		
		setListaDocumentos((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));
		
		atualizaSessao();

	}

	public void atualizaSessao() {
		applyStateInHttpSession();
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		
	}
	// ################################# ACTION ###############################

	public void pesquisarDocumentosAction(ActionEvent evt) throws ParseException {
		pesquisarDocumentos();
		atualizaSessao();
	}

	public void atualizaSessaoAction(ActionEvent evt) {
		atualizaSessao();
	}
	
	public void limparSessaoAction(ActionEvent evt) {
		limparCampos();
	}
	
	// ############################## METHODS ################################
	
	
	public List<SelectItem> carregarComboSetores() {
		ministroPresidente = verificarGabineteAtualPresidencia();
		List<SelectItem> listaOrdenadaPeloSetorUsuario = new ArrayList<SelectItem>();
	
		listaOrdenadaPeloSetorUsuario.add(new SelectItem(null, ""));
		
		List<SelectItem> listaSetores = buscaSetoresConformePerfilUsuario(isUsuarioMaster());
		
		if (listaSetores != null && listaSetores.size() > 0) {
			// É nescessario o reverse pois ele carrega lista em ordem decrescente.
			Collections.reverse(listaSetores);
			List<SelectItem> listaDeRecebimentoDosOutrosSetor = new ArrayList<SelectItem>();
			for (SelectItem si : listaSetores) {
				listaDeRecebimentoDosOutrosSetor.add(new SelectItem(si.getValue(), si.getLabel()));
			}
			listaOrdenadaPeloSetorUsuario.addAll(listaDeRecebimentoDosOutrosSetor);
		}

		return listaOrdenadaPeloSetorUsuario;
	}
	
	
	public List<SelectItem> buscaSetoresConformePerfilUsuario(Boolean perfilMaster) {
		List<Setor> setores = new ArrayList<Setor>();
		List<SelectItem> listaSetores = new LinkedList<SelectItem>();
		
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
		
		try {
			if (perfilMaster) {				
				setores.addAll(getSetorService().pesquisarSetores(null));
			} else {
				setores.addAll(getUsuarioService().pesquisarSetoresEGab(usuarioAssinatura.getUsername()));
			}
			if (setores != null && setores.size() > 0) {
				for (Setor s : setores) {
					listaSetores.add(new SelectItem(s.getId(), s.getNome() + " - " + s.getId()));
				}
			}
			if (ministroPresidente != null && perfilMaster) {
				Setor setor = getSetorService().recuperarPorId(Setor.CODIGO_SETOR_PRESIDENCIA);

				listaSetores.add(new SelectItem(ministroPresidente.getId().getMinistro().getSetor().getId(), ministroPresidente.getId().getMinistro().getSetor().getNome()));

				if (usuarioAssinatura.getSetor().getId() != setor.getId() && listaSetores != null || usuarioAssinatura.getSetor().getId().equals(Setor.CODIGO_SETOR_PRESIDENCIA)) {
					Collections.reverse(listaSetores);
				}
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao busca a lista de setores.", e, LOG);
		}
		return listaSetores;
	}

	public void limparCampos() {
		setListaDocumentos(null);
		setDataInicial(null);
		setDataFinal(null);
		setSiglaUsuarioAtual("");
		setCodigoSetorAtual(null);
		setItensUsuarios(carregarComboUsuariosDoSetor(codigoSetorAtual));
		
		atualizaSessao();
	}
	
	public String formatarData(Date data) throws ParseException {
		if (data == null){
			return null;
		}
		return DateFormatUtils.format(data, "dd/MM/yyyy");
	}

	public void pesquisarDocumentos() throws ParseException {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		List<ExpedienteAssinadoResult> documentos = null;
		String dtIni = formatarData(dataInicial);
		String dtFim = formatarData(dataFinal);
		GregorianCalendar primeiraData = new GregorianCalendar();
		GregorianCalendar segundaData = new GregorianCalendar();
		
		if((!StringUtils.isVazia(dtIni)) && (!StringUtils.isVazia(dtFim))){
			primeiraData.setTime(getDataInicial());
			segundaData.setTime(getDataFinal());
			if((segundaData.getTimeInMillis() - primeiraData.getTimeInMillis()) < 0){
				reportarAviso("A data final deve ser maior que a data inicial.");
				return;
			}
		}
		else{
			reportarAviso("O campo 'Período' é obrigatório.");
			return;
		}
		
		if(NumberUtils.isNullOuMenorIgualZero(codigoSetorAtual)){
			reportarAviso("O campo 'Setor' é obrigatório.");
			return;
		}
		
		try {
			
			documentos = comunicacaoServiceLocal.pesquisarComunicacoesAssinadasPorPeriodo(codigoSetorAtual,siglaUsuarioAtual,dtIni, dtFim);

			if (CollectionUtils.isVazia(documentos)) {
				reportarAviso("Nenhum documento encontrado.");			
			} else {
				reportarInformacao(MessageFormat.format("Foi(ram) encontrado(s) {0} documento(s).", documentos.size()));
			}
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar Expedientes Assinados.", exception, LOG);
		}
		
		setListaDocumentos(getCheckableDataTableRowWrapperList(documentos));
	}
	// ######################## GETS AND SETs ###############################

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
	
	public Date getDataInicial() {
		return dataInicial;
	}
	
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	public Date getDataFinal() {
		return dataFinal;
	}
	
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public String getSiglaUsuarioAtual() {
		return siglaUsuarioAtual;
	}

	public void setSiglaUsuarioAtual(String siglaUsuarioAtual) {
		this.siglaUsuarioAtual = siglaUsuarioAtual;
	}

	public Long getCodigoSetorAtual() {
		return codigoSetorAtual;
	}

	public void setCodigoSetorAtual(Long codigoSetorAtual) {
		this.codigoSetorAtual = codigoSetorAtual;
	}
	
	public List<SelectItem> getItensUsuarios() {
		return itensUsuarios;
	}

	public void setItensUsuarios(List<SelectItem> itensUsuarios) {
		this.itensUsuarios = itensUsuarios;
	}

	public List<SelectItem> getItensSetores() {
		return itensSetores;
	}

	public void setItensSetores(List<SelectItem> itensSetores) {
		this.itensSetores = itensSetores;
	}	
	
	public void alterarSetor(ActionEvent event){		
		
		setItensUsuarios(carregarComboUsuariosDoSetor(codigoSetorAtual));
		
		atualizaSessao();
	}

	public void visualizarPDF(ActionEvent e) {
		
		CheckableDataTableRowWrapper checkableDataTableRowWrapper = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();

		ExpedienteAssinadoResult documento = (ExpedienteAssinadoResult) checkableDataTableRowWrapper.getWrappedObject();
		
		setPDFResponse(documento.getDocumentoComunicacao().getDocumentoEletronico().getArquivo(), documento.getIdentificacaoComunicacao().replaceAll(" ", "_"));
	}

	
}
