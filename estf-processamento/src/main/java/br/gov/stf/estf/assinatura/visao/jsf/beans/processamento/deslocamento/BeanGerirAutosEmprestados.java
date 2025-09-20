package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.Hibernate;

import br.gov.stf.estf.assinatura.relatorio.RelatorioGerirCargaAutos;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.jurisdicionado.EmprestimoAutosProcesso;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumSituacaoEmprestimo;
import br.gov.stf.estf.entidade.jurisdicionado.util.EmprestimoAutosResult;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.jurisdicionado.model.exception.JurisdicionadoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanGerirAutosEmprestados extends AssinadorBaseBean {

	private static final String SIGLA_ADVOGADO = "ADV";
	private static final long serialVersionUID = 1L;

	private Long codigoOrgaoDestino;
	private Long idSituacao;
	private Long objetoIncidente;
	private String siglaNumeroProcesso;
	private String classeSelecionada;
	private String tipoRemessa;
	private String nomeJurisdicionado;
	private String obsInvalidarCobranca;
	private Boolean renderizaTabelaCarga;
	@KeepStateInHttpSession
	private List<SelectItem> itensSituacaoAutos;
	private Date dataInicial;
	private Date dataFinal;
	private List<CheckableDataTableRowWrapper> listaAutosEmprestados;
	private List<EmprestimoAutosResult> listaSelecionadaAutos;
	private List<EmprestimoAutosResult> listaEmprestimoPesquisa;
	private org.richfaces.component.html.HtmlDataTable tabelaAutosEmprestados;
	private Processo processo;
	private EmprestimoAutosResult emprestimoAutosResult;
	private static List<String> classes;

	// ---------------------- VARIAVEIS DE SESSAO ----------------------------//
	private static final Object CLASSE_SELECIONADA = new Object();
	private static final Object OBS_INVALIDA_COBRANCA = new Object();
	private static final Object DATA_INICIAL = new Object();
	private static final Object DATA_FINAL = new Object();
	private static final Object TIPO_REMESSA = new Object();
	private static final Object LISTA_SELECIONADA_AUTOS = new Object();
	private static final Object LISTA_EMPRESTIMO_PESQ = new Object();
	private static final String KEY_LISTA_AUTOS_EMPRESTADOS = BeanGerirAutosEmprestados.class.getCanonicalName() + ".listaAutosEmprestados";
	private static final Object CODIGO_ORGAO_DESTINO = new Object();
	private static final Object RENDERIZA_TABELA_CARGA = new Object();
	private static final Object OBJETOINCIDENTE = new Object();
	private static final Object ITENS_SITUACAO_AUTOS = new Object();
	private static final Object ID_SITUACAO_AUTOS = new Object();

	@SuppressWarnings("unchecked")
	private void restaurarSessao() {

		if (getAtributo(KEY_LISTA_AUTOS_EMPRESTADOS) == null) {
			setAtributo(KEY_LISTA_AUTOS_EMPRESTADOS, new ArrayList<CheckableDataTableRowWrapper>());
		} else {
			setListaAutosEmprestados((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_AUTOS_EMPRESTADOS));
		}
		
		if (getAtributo(OBJETOINCIDENTE) == null) {
			setAtributo(OBJETOINCIDENTE, objetoIncidente);
		}
		setObjetoIncidente((Long) getAtributo(OBJETOINCIDENTE));
		
		if (getAtributo(LISTA_EMPRESTIMO_PESQ) == null) {
			setAtributo(LISTA_EMPRESTIMO_PESQ, new ArrayList<EmprestimoAutosResult>());
		}
		setListaEmprestimoPesquisa((List<EmprestimoAutosResult>) getAtributo(LISTA_EMPRESTIMO_PESQ));
		
		if (getAtributo(OBS_INVALIDA_COBRANCA) == null) {
			setAtributo(OBS_INVALIDA_COBRANCA, "");
		}
		setObsInvalidarCobranca((String) getAtributo(OBS_INVALIDA_COBRANCA));
		
		if (getAtributo(LISTA_SELECIONADA_AUTOS) == null) {
			setAtributo(LISTA_SELECIONADA_AUTOS, new ArrayList<EmprestimoAutosProcesso>());
		}
		setListaSelecionadaAutos((List<EmprestimoAutosResult>) getAtributo(LISTA_SELECIONADA_AUTOS));

		if (getAtributo(RENDERIZA_TABELA_CARGA) == null) {
			setAtributo(RENDERIZA_TABELA_CARGA, false);
		}
		setRenderizaTabelaCarga((Boolean) getAtributo(RENDERIZA_TABELA_CARGA));
		
		if (getAtributo(ITENS_SITUACAO_AUTOS) == null) {
			setAtributo(ITENS_SITUACAO_AUTOS, carregarComboSituacaoAutos());
		}
		setItensSituacaoAutos((List<SelectItem>) getAtributo(ITENS_SITUACAO_AUTOS));
		
		if (getAtributo(ID_SITUACAO_AUTOS) == null) {
			setAtributo(ID_SITUACAO_AUTOS, null);
		}
		setIdSituacao((Long) getAtributo(ID_SITUACAO_AUTOS));
		
		setDataInicial((Date) getAtributo(DATA_INICIAL));
		setDataFinal((Date) getAtributo(DATA_FINAL));

		setTipoRemessa((String) getAtributo(TIPO_REMESSA));
		setCodigoOrgaoDestino((Long) getAtributo(CODIGO_ORGAO_DESTINO));
	}

	// ----------------------- ATUALIZACAO --------------------- //
	
	public BeanGerirAutosEmprestados(){
		restaurarSessao();
	}
	
	public void atualizaSessao() {
		setAtributo(CLASSE_SELECIONADA, classeSelecionada);
		setAtributo(LISTA_SELECIONADA_AUTOS, listaSelecionadaAutos);
		setAtributo(DATA_INICIAL, dataInicial);
		setAtributo(DATA_FINAL, dataFinal);
		setAtributo(KEY_LISTA_AUTOS_EMPRESTADOS, listaAutosEmprestados);
		setAtributo(ID_SITUACAO_AUTOS, idSituacao);
		setAtributo(ITENS_SITUACAO_AUTOS, itensSituacaoAutos);
		setAtributo(TIPO_REMESSA, tipoRemessa);
		setAtributo(CODIGO_ORGAO_DESTINO, codigoOrgaoDestino);
		setAtributo(RENDERIZA_TABELA_CARGA, renderizaTabelaCarga);
		setAtributo(OBS_INVALIDA_COBRANCA, obsInvalidarCobranca);
		setAtributo(LISTA_EMPRESTIMO_PESQ, listaEmprestimoPesquisa);
	}


	
	// -------------------- ACTiONS ---------------------------------------------- //
	
	public void pesquisarAutosAction(ActionEvent evt){
		pesquisarAutos();
		atualizaSessao();
	}
	
	public void cobrarAutosAction(ActionEvent evt){
		cobrarAutos();
		atualizaSessao();
	}
	
	public void invalidarCargaAction(ActionEvent evt){
		invalidarCarga();
		atualizaSessao();
	}
	
	public void invalidarCobrancaAction(ActionEvent evt){
		invalidarCobranca();
		atualizaSessao();
	}
	
	
	public void novaCargaAutosAction(ActionEvent evt){
		novaCargaAutos();
		atualizaSessao();
	}
	
	public void imprimirCargaAutosAction(ActionEvent evt){
		imprimirAutos();
		atualizaSessao();
	}
	
	public void limparAutos(ActionEvent evt){
		limpar();
		atualizaSessao();
	}
	
	public void exibeObservacaoCobrancaAction(ActionEvent evt){
		exibeObservacaoCobranca();
		atualizaSessao();
	}
	
	public void marcarTodosAutos(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaAutosEmprestados);
		setListaAutosEmprestados(listaAutosEmprestados);
	}
	
	public void atualizarMarcacao(ActionEvent evt) {
		setListaAutosEmprestados(listaAutosEmprestados);
	}
	
	
	
	// -------------------------- METHODS ----------------------------- //

	public void limpar() {
		setListaSelecionadaAutos(null);
		setListaAutosEmprestados(null);
		dataInicial = null;
		dataFinal = null;
		setNomeJurisdicionado("");
		setSiglaNumeroProcesso("");
		setObsInvalidarCobranca("");
		setRenderizaTabelaCarga(false);
		setObjetoIncidente(null);
		setIdSituacao(null);
		setItensSituacaoAutos(carregarComboSituacaoAutos());
		setListaEmprestimoPesquisa(new ArrayList<EmprestimoAutosResult>());
	}
	
	public void pesquisarAutos(){
		
		try {
			listaEmprestimoPesquisa = getEmprestimoAutosProcessoService().pesquisarAutos(nomeJurisdicionado, objetoIncidente, dataInicial, dataFinal, idSituacao);
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar os autos emprestados: " + e.getMessage());
		}
		
		if (listaEmprestimoPesquisa.size() == 0){
			reportarAviso("Nenhum registro encontrado");
		}else{
			setRenderizaTabelaCarga(true);
		}
		setListaAutosEmprestados(getCheckableDataTableRowWrapperList(listaEmprestimoPesquisa));
	}
	
	@SuppressWarnings({ "rawtypes"})
	public List pesquisarIncidentesPrincipal(Object value) {

		String siglaNumero = null;
		List<ObjetoIncidente<?>> incidentes = null;
		if (value != null)
			siglaNumero = value.toString();

		if (StringUtils.isNotVazia(siglaNumero)) {
			try {
				String sigla = ProcessoParser.getSigla(siglaNumero);
				Long lNumero = ProcessoParser.getNumero(siglaNumero);

				if (StringUtils.isNotVazia(sigla) && lNumero != null) {
					sigla = converterClasse(sigla, classes);

					if (sigla == null) {
						reportarAviso("Classe processual não encontrada: " + sigla);
						return null;
					}

					processo = getProcessoService().recuperarProcesso(sigla, lNumero);

					if (processo != null) {
						incidentes = recuperarIncidentes(processo.getId());
					}
				}
			} catch (NumberFormatException e) {
				reportarErro("Número de processo inválido: " + siglaNumero);
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar os incidentes do processo: " + siglaNumero);
			}
		}
		atualizaSessao();
		return incidentes;
	}

	
	/**
	 * método responsável por incrementar as cobranças e salvar no banco
	 */
	public void cobrarAutos(){
		
		listaSelecionadaAutos = retornarItensSelecionados(listaAutosEmprestados);
		
		if (listaSelecionadaAutos != null && listaSelecionadaAutos.size() == 0){
			reportarAviso("É necessário selecionar um ou mais registro(s).");
			return;
		}
		
		Usuario usu = new Usuario();
		
		try {
			usu = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		}catch (ServiceException e) {
			reportarErro("Erro ao localizar o usuário.");
		}
		
		for (EmprestimoAutosResult emp : listaSelecionadaAutos){
			
			if (emp.getSituacaoEmprestimo().equals(EnumSituacaoEmprestimo.DEVOLVIDO) ||
					emp.getSituacaoEmprestimo().equals(EnumSituacaoEmprestimo.DEVOLVIDO_ATRASO)){
				reportarAviso("Não é possível realizar cobrança de processo já devolvido.");
				return;
			}
			try {
				getEmprestimoAutosProcessoService().salvarCobranca(emp, getSetorUsuarioAutenticado(), usu, obsInvalidarCobranca);
			}catch (Exception e) {
				reportarErro("Erro ao cobrar o(s) auto(s).");
				return;
			}
		}
		
		reportarInformacao("Cobrança realizada com sucesso.");
	}
	
	
	/**
	 * Método responsável pela invalidação da carga selecionada.
	 * O deslocamento selecionado será excluído.
	 * Caso a guia possua somente um deslocamento, ela será excluída.
	 */
	public void invalidarCarga(){
		
		listaSelecionadaAutos = retornarItensSelecionados(listaAutosEmprestados);
		
		if (listaSelecionadaAutos != null && listaSelecionadaAutos.size() == 0){
			reportarAviso("É necessário selecionar um ou mais registro(s).");
			return;
		}
		
		Usuario usu = new Usuario();
		
		try {
			usu = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		}catch (ServiceException e) {
			reportarErro("Erro ao localizar o usuário.");
		}
		
		for (EmprestimoAutosResult emp : listaSelecionadaAutos){
			
			try {
				Hibernate.initialize(emp.getEmprestimoAutosProcesso().getDeslocaRetirada());
				getEmprestimoAutosProcessoService().excluirCarga(emp, getSetorUsuarioAutenticado(), usu);
			} catch (Exception e) {
				reportarErro("Erro ao invalidar a carga.");
				return;
			}

		}
		
		listaAutosEmprestados.removeAll(listaSelecionadaAutos);
		reportarInformacao("Carga invalidada com sucesso.");
		
	}
	
	
	/**
	 * Invalida o andamento de cobrança do empréstimos e diminue a quantidade
	 */
	public void invalidarCobranca(){
		listaSelecionadaAutos = retornarItensSelecionados(listaAutosEmprestados);
		
		if (listaSelecionadaAutos != null && listaSelecionadaAutos.size() == 0){
			reportarAviso("É necessário selecionar um ou mais registro(s).");
			return;
		}
		
		Usuario usu = new Usuario();
		
		try {
			usu = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		}catch (ServiceException e) {
			reportarErro("Erro ao localizar o usuário.");
		}
		
		for (EmprestimoAutosResult emp : listaSelecionadaAutos){
			
			try {
				getEmprestimoAutosProcessoService().excluirCobranca(emp, getSetorUsuarioAutenticado(), usu);
			} catch (JurisdicionadoException e) {
				reportarErro(e.toString());
				return;
			} catch (ServiceException e) {
				reportarErro(e.toString());
				return;
			} 
		}
		
		reportarInformacao("Cobrança invalidada com sucesso.");
	}
	
	
	public void exibeObservacaoCobranca(){
		
		listaSelecionadaAutos = retornarItensSelecionados(listaAutosEmprestados);
		
		if (listaSelecionadaAutos != null && listaSelecionadaAutos.size() == 0){
			reportarAviso("É necessário selecionar um ou mais registro(s).");
			return;
		}
		
		for (EmprestimoAutosResult result : listaSelecionadaAutos){
			if (result.getEmprestimoAutosProcesso().getQuantidadeCobrancaDevolucao() == null){
				if (result.getEmprestimoAutosProcesso().getNomeAutorizadoDaCarga().trim().length() > 0){
					setObsInvalidarCobranca("Contato efetuado nessa data com Sr(a). " + result.getEmprestimoAutosProcesso().getNomeAutorizadoDaCarga() + ". 1ª cobrança realizada.");
				}else{
					setObsInvalidarCobranca("Contato efetuado nessa data com Sr(a). " + result.getEmprestimoAutosProcesso().getNomeAutorizadorDaCarga() + ". 1ª cobrança realizada.");
				}
			}else{
				Integer quantidade = 0;
				quantidade = result.getEmprestimoAutosProcesso().getQuantidadeCobrancaDevolucao()+ 1;
				if (result.getEmprestimoAutosProcesso().getNomeAutorizadoDaCarga().trim().length() > 0){
					setObsInvalidarCobranca("Contato reiterado nessa data com Sr(a). " + result.getEmprestimoAutosProcesso().getNomeAutorizadoDaCarga() + ". " + quantidade + " cobrança(s) realizada(s).");
				}else{
					setObsInvalidarCobranca("Contato reiterado nessa data com Sr(a). " + result.getEmprestimoAutosProcesso().getNomeAutorizadorDaCarga() + ". " + quantidade + " cobrança(s) realizada(s).");
				}
				
			}
		}
	}
	
	public String novaCargaAutos(){
		return "autorizarCargaAutos";
	}
	
	
	/**
	 * Gera relatório em pdf
	 */
	public void imprimirAutos() {
		
		List<RelatorioGerirCargaAutos> listaAutos = new ArrayList<RelatorioGerirCargaAutos>();
		
		if (listaAutosEmprestados == null ||  listaAutosEmprestados.size() == 0){
			reportarAviso("Lista vazia.");
			return;
		}
		
		for (EmprestimoAutosResult result : listaEmprestimoPesquisa){
			RelatorioGerirCargaAutos rel = new RelatorioGerirCargaAutos();
			if (result.getEmprestimoAutosProcesso().getNomeAutorizadoDaCarga().trim().length() > 0){
				rel.setNomeAutorizado(result.getEmprestimoAutosProcesso().getNomeAutorizadoDaCarga());
			}else{
				rel.setNomeAutorizado("");
			}
			if (result.getEmprestimoAutosProcesso().getNomeAutorizadorDaCarga().trim().length() > 0) {
				rel.setNomeAutorizador(result.getEmprestimoAutosProcesso().getNomeAutorizadorDaCarga());
			}else{
				rel.setNomeAutorizador("");
			}
			
			try {
				if (result.getEmprestimoAutosProcesso().getPapelJurisdicionado() != null){
					rel.setOab(result.getEmprestimoAutosProcesso().getPapelJurisdicionado().getJurisdicionado().getOab());
				}else if (result.getEmprestimoAutosProcesso().getAssociacaoJurisdicionado().getMembro().getJurisdicionado().getOab() != null){
					rel.setOab(result.getEmprestimoAutosProcesso().getAssociacaoJurisdicionado().getMembro().getJurisdicionado().getOab());
				}else{
					rel.setOab("");
				}
				if (result.getEmprestimoAutosProcesso().getDataEmprestimo() != null){
						rel.setDataCarga(formatarData(result.getEmprestimoAutosProcesso().getDataEmprestimo()));
				}else{
					rel.setDataCarga("");
				}
				if (result.getEmprestimoAutosProcesso().getDataDevolucaoPrevista() != null){
					rel.setDataPrevista(formatarData(result.getEmprestimoAutosProcesso().getDataDevolucaoPrevista()));
				}else{
					rel.setDataPrevista("");
				}
				if (result.getEmprestimoAutosProcesso().getDeslocaDevolucao() != null){
					rel.setDataDevolucao(formatarData(result.getEmprestimoAutosProcesso().getDeslocaDevolucao().getDataRecebimento()));
				}else{
					rel.setDataDevolucao("");
				}
				if (result.getContatosJurisdicionado()!= null){
					rel.setContatosJurisdicionado(result.getContatosJurisdicionado());
				}else{
					rel.setContatosJurisdicionado("");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			String siglaNumeroProcesso = result.getEmprestimoAutosProcesso().getDeslocaRetirada().getClasseProcesso() +
			" " + result.getEmprestimoAutosProcesso().getDeslocaRetirada().getNumeroProcesso();
			rel.setSiglaNumeroProcesso(siglaNumeroProcesso);
			rel.setSituacao(result.getSituacaoEmprestimo().getDescricao());
			if (result.getEmprestimoAutosProcesso().getQuantidadeCobrancaDevolucao() != null){
				rel.setNumeroCobranca(result.getEmprestimoAutosProcesso().getQuantidadeCobrancaDevolucao());				
			}else{
				rel.setNumeroCobranca(0);
			}
			listaAutos.add(rel);
		}
		
		try {
			byte[] arquivo = null;
			arquivo = getProcessamentoRelatorioService().criarRelatorioGerirAutos(listaAutos);
			ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
			mandarRespostaDeDownloadDoArquivo(input);
		} catch (ServiceException e) {
			reportarErro("Erro ao imprimir o relatório: " + e.getMessage());
		}

	}

	private void mandarRespostaDeDownloadDoArquivo(ByteArrayInputStream input) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-disposition", "attachment; filename=\"RelatorioAutosEmprestados.pdf\"");
		response.setContentType("application/pdf");
		try {
			IOUtils.copy(input, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		facesContext.responseComplete();
	}
	
	
	public List<SelectItem> carregarComboSituacaoAutos(){
		List<SelectItem> listaSituacaoAutos = new ArrayList<SelectItem>();
		listaSituacaoAutos.add(new SelectItem(null, null));
		listaSituacaoAutos.add(new SelectItem(EnumSituacaoEmprestimo.DEVOLVIDO.getCodigo(), EnumSituacaoEmprestimo.DEVOLVIDO.getDescricao()));
		listaSituacaoAutos.add(new SelectItem(EnumSituacaoEmprestimo.DEVOLVIDO_ATRASO.getCodigo(), EnumSituacaoEmprestimo.DEVOLVIDO_ATRASO.getDescricao()));
		listaSituacaoAutos.add(new SelectItem(EnumSituacaoEmprestimo.DIA_DEVOLUCAO.getCodigo(), EnumSituacaoEmprestimo.DIA_DEVOLUCAO.getDescricao()));
		listaSituacaoAutos.add(new SelectItem(EnumSituacaoEmprestimo.EM_ATRASO.getCodigo(), EnumSituacaoEmprestimo.EM_ATRASO.getDescricao()));
		listaSituacaoAutos.add(new SelectItem(EnumSituacaoEmprestimo.EM_CURSO.getCodigo(), EnumSituacaoEmprestimo.EM_CURSO.getDescricao()));
		return listaSituacaoAutos;
	}
	
	// formatar data
	public String formatarData(Date data) throws ParseException {
		if (data == null)
			return null;
		return DateFormatUtils.format(data, "dd/MM/yyyy");
	}


	// ------------------ Gets e Sets ------------------------- //

	public String getClasseSelecionada() {
		return classeSelecionada;
	}

	public void setClasseSelecionada(String classeSelecionada) {
		this.classeSelecionada = classeSelecionada;
	}

	public List<SelectItem> getItensSituacaoAutos() {
		return itensSituacaoAutos;
	}

	public void setItensSituacaoAutos(List<SelectItem> itensSituacaoAutos) {
		this.itensSituacaoAutos = itensSituacaoAutos;
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

	public String getTipoRemessa() {
		if (tipoRemessa == null) {
			tipoRemessa = SIGLA_ADVOGADO;
		}
		return tipoRemessa;
	}

	public void setTipoRemessa(String tipoRemessa) {
		this.tipoRemessa = tipoRemessa;
	}

	public List<CheckableDataTableRowWrapper> getListaAutosEmprestados() {
		return listaAutosEmprestados;
	}

	public void setListaAutosEmprestados(List<CheckableDataTableRowWrapper> listaAutosEmprestados) {
		this.listaAutosEmprestados = listaAutosEmprestados;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaAutosEmprestados() {
		return tabelaAutosEmprestados;
	}

	public void setTabelaAutosEmprestados(org.richfaces.component.html.HtmlDataTable tabelaAutosEmprestados) {
		this.tabelaAutosEmprestados = tabelaAutosEmprestados;
	}

	public void setCodigoOrgaoDestino(Long codigoOrgaoDestino) {
		setAtributo(CODIGO_ORGAO_DESTINO, codigoOrgaoDestino);
		this.codigoOrgaoDestino = codigoOrgaoDestino;
	}

	public Long getCodigoOrgaoDestino() {
		return codigoOrgaoDestino;
	}

	public String getNomeJurisdicionado() {
		return nomeJurisdicionado;
	}

	public void setNomeJurisdicionado(String nomeJurisdicionado) {
		this.nomeJurisdicionado = nomeJurisdicionado;
	}

	public Long getIdSituacao() {
		return idSituacao;
	}

	public void setIdSituacao(Long idSituacao) {
		this.idSituacao = idSituacao;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public String getSiglaNumeroProcesso() {
		return siglaNumeroProcesso;
	}

	public void setSiglaNumeroProcesso(String siglaNumeroProcesso) {
		this.siglaNumeroProcesso = siglaNumeroProcesso;
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public EmprestimoAutosResult getEmprestimoAutosResult() {
		return emprestimoAutosResult;
	}

	public void setEmprestimoAutosResult(EmprestimoAutosResult emprestimoAutosResult) {
		this.emprestimoAutosResult = emprestimoAutosResult;
	}

	public List<EmprestimoAutosResult> getListaSelecionadaAutos() {
		return listaSelecionadaAutos;
	}

	public void setListaSelecionadaAutos(
			List<EmprestimoAutosResult> listaSelecionadaAutos) {
		this.listaSelecionadaAutos = listaSelecionadaAutos;
	}

	public List<EmprestimoAutosResult> getListaEmprestimoPesquisa() {
		return listaEmprestimoPesquisa;
	}

	public void setListaEmprestimoPesquisa(
			List<EmprestimoAutosResult> listaEmprestimoPesquisa) {
		this.listaEmprestimoPesquisa = listaEmprestimoPesquisa;
	}

	public Boolean getRenderizaTabelaCarga() {
		return renderizaTabelaCarga;
	}

	public void setRenderizaTabelaCarga(Boolean renderizaTabelaCarga) {
		this.renderizaTabelaCarga = renderizaTabelaCarga;
	}

	public String getObsInvalidarCobranca() {
		return obsInvalidarCobranca;
	}

	public void setObsInvalidarCobranca(String obsInvalidarCobranca) {
		this.obsInvalidarCobranca = obsInvalidarCobranca;
	}

}