package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentos;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.assinatura.BeanAssinatura;
import br.gov.stf.estf.assinatura.visao.util.OrdenacaoUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanConsultarDocumentosElaborados extends AssinadorBaseBean {

	private static final long serialVersionUID = -8059606167034592897L;
	private static final Log LOG = LogFactory.getLog(BeanConsultarDocumentosElaborados.class);

	// ##################### VARIAVEIS DE SESSAO #############################

	private static final String KEY_LISTA_DOCUMENTOS = BeanConsultarDocumentosElaborados.class.getCanonicalName() + ".listaDocumentos";
	public static final Object ITENS_FASE_DOCUMENTO = BeanAssinatura.class.getCanonicalName() + ".itensFaseDocumento";
	private static final Object ITENSSETORES = new Object();
	private static final Object CODIGO_SITUACAO = new Object();
	private static final Object DATA_INICIO = new Object();
	private static final Object DATA_FINAL = new Object();
	private static final Object CODIGO_SETOR = new Object();
	private static final Object OPCAO_SETOR = new Object();
	private final String SISTEMA_PROCESSAMENTO = "PROCESSAMENTO";
	private final String SETOR_DOC_RESTRITOS = "codigo.setor.doc.restritos.consulta";
	
	private List<SelectItem> itensFaseDocumento;
	private List<SelectItem> itensSetores;
	private Long codigoFaseDocumento;
	private Long codigoSetorAtual;
	private String siglaProcesso;
	private Long numeroProcesso;
	private Long numeracaoUnica;
	private Long anoNumeracaoUnica;
	private Date dataInicial;
	private Date dataFinal;
	private Integer opcaoLocal = new Integer(1);
	private int periodo;

	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> listaDocumentos;

	private HtmlDataTable tabelaDocumentos;

	public BeanConsultarDocumentosElaborados() {
		restaurarSessao();
	}

	@SuppressWarnings("unchecked")
	private void restaurarSessao() {
		restoreStateOfHttpSession();

		if (getAtributo(ITENSSETORES) == null) {
			setAtributo(ITENSSETORES, carregarComboSetoresDestino(true,false));
		}
		setItensSetores((List<SelectItem>) getAtributo(ITENSSETORES));

		setListaDocumentos((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));
		
		if (getAtributo(ITENS_FASE_DOCUMENTO) == null) {
			setAtributo(ITENS_FASE_DOCUMENTO, carregarComboFaseSituacaoDocumento());
		}
		setItensFaseDocumento((List<SelectItem>) getAtributo(ITENS_FASE_DOCUMENTO));
		
		codigoFaseDocumento = (Long) getAtributo(CODIGO_SITUACAO);
		codigoSetorAtual = (Long) getAtributo(CODIGO_SETOR);

		dataInicial = (Date) getAtributo(DATA_INICIO);
		dataFinal = (Date) getAtributo(DATA_FINAL);
		
		if (getAtributo(OPCAO_SETOR) != null)
			opcaoLocal = (Integer) getAtributo(OPCAO_SETOR);
		else
			opcaoLocal = new Integer(1);

		atualizaSessao();

	}

	public void atualizaSessao() {
		applyStateInHttpSession();
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		setAtributo(ITENS_FASE_DOCUMENTO, itensFaseDocumento);
		setAtributo(CODIGO_SITUACAO, codigoFaseDocumento);
		setAtributo(CODIGO_SETOR, codigoSetorAtual);
		setAtributo(DATA_INICIO, dataInicial);
		setAtributo(DATA_FINAL, dataFinal);
		setAtributo(OPCAO_SETOR, opcaoLocal);
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
		limparTodosCampos();
	}
	
	// ############################## METHODS ################################

	public void limparCampos() {
		setListaDocumentos(null);
		atualizaSessao();
	}
	
	public void limparTodosCampos() {
		setListaDocumentos(null);
		setSiglaProcesso(null);
		setNumeroProcesso(null);
		setCodigoFaseDocumento(null);
		setDataInicial(null);
		setDataFinal(null);
		setNumeracaoUnica(null);
		setCodigoSetorAtual(null);
		atualizaSessao();
	}
	
	// formatar data
	public String formatarData(Date data) throws ParseException {
		if (data == null)
			return null;
		return DateFormatUtils.format(data, "dd/MM/yyyy");
	}

	@SuppressWarnings("unchecked")
	public void pesquisarDocumentos() throws ParseException {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		List<ComunicacaoDocumentoResult> documentos = null;
		String sigla = siglaProcesso.toUpperCase();
		String dtIni = formatarData(dataInicial);
		String dtFim = formatarData(dataFinal);
		GregorianCalendar primeiraData = new GregorianCalendar();
		GregorianCalendar segundaData = new GregorianCalendar();
		
		if (StringUtils.isVazia(siglaProcesso) && (NumberUtils.isNullOuMenorIgualZero(numeroProcesso))
			&& (NumberUtils.isNullOuMenorIgualZero(codigoFaseDocumento)) && (NumberUtils.isNullOuMenorIgualZero(codigoSetorAtual))
			&& (NumberUtils.isNullOuMenorIgualZero(numeracaoUnica)) && (NumberUtils.isNullOuMenorIgualZero(anoNumeracaoUnica))
			&& (StringUtils.isVazia(dtIni)) && (StringUtils.isVazia(dtFim))){
			reportarAviso("Não há critérios para a pesquisa!");
			reportarAviso("O campo 'Período' é obrigatório. E é necessário informá-lo juntamente com algum outro campo.");
			return;
		}

		if ( NumberUtils.isNullOuMenorIgualZero(codigoFaseDocumento) && !NumberUtils.isNullOuMenorIgualZero(opcaoLocal) && (opcaoLocal == 2) ) {
			reportarAviso("O campo 'Situação' é obrigatório quando a opção de pesquisa é o setor que gerou a situação.");
			return;
		}

/*		
		if ( NumberUtils.isNullOuMenorIgualZero(codigoSetorAtual) && !NumberUtils.isNullOuMenorIgualZero(opcaoLocal) && (opcaoLocal == 1) ) {
			reportarAviso("O campo 'Setor' é obrigatório quando a opção de pesquisa é o setor de deslocamento atual.");
			return;
		}
*/
		if ( (!StringUtils.isVazia(siglaProcesso) || !NumberUtils.isNullOuMenorIgualZero(numeroProcesso))
			|| (!NumberUtils.isNullOuMenorIgualZero(numeracaoUnica) || (!NumberUtils.isNullOuMenorIgualZero(anoNumeracaoUnica)))){
			try {
			
			if (opcaoLocal == 1)
				documentos = comunicacaoServiceLocal.pesquisarComunicacoes(sigla, numeroProcesso, codigoFaseDocumento, 
								codigoSetorAtual, null, numeracaoUnica, anoNumeracaoUnica, dtIni, dtFim);
			else
				documentos = comunicacaoServiceLocal.pesquisarComunicacoes(sigla, numeroProcesso, codigoFaseDocumento, 
						null, codigoSetorAtual, numeracaoUnica, anoNumeracaoUnica, dtIni, dtFim);
			

			if (CollectionUtils.isVazia(documentos)) {
				reportarAviso("Nenhum documento encontrado.");
			} else	{
				reportarInformacao(MessageFormat.format("Foi(ram) encontrado(s) {0} documento(s).", documentos.size()));
			}
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar Expedientes Elaborados.", exception, LOG);
		}

			if(documentos!=null && documentos.size() > 0){
				OrdenacaoUtils.ordenarListaComunicacaoDocumentoResultProcesso(documentos);
			}
					
			setListaDocumentos(getCheckableDocumentoList(documentos));
			return;
		}

		else if((!StringUtils.isVazia(dtIni)) && (!StringUtils.isVazia(dtFim)) && 
				(	(!NumberUtils.isNullOuMenorIgualZero(numeracaoUnica)) ||
					(!NumberUtils.isNullOuMenorIgualZero(codigoFaseDocumento)) || 
					(!StringUtils.isVazia(siglaProcesso)) || 
					(!NumberUtils.isNullOuMenorIgualZero(numeroProcesso))  || 
					(!NumberUtils.isNullOuMenorIgualZero(codigoSetorAtual))	|| 
					(!NumberUtils.isNullOuMenorIgualZero(numeracaoUnica)) ||	
					(!NumberUtils.isNullOuMenorIgualZero(anoNumeracaoUnica)))){
			primeiraData.setTime(getDataInicial());
			segundaData.setTime(getDataFinal());
			setPeriodo(calculaPeriodo(primeiraData, segundaData));
		}
		else{
			reportarAviso("O campo 'Período' é obrigatório. É necessário informá-lo juntamente com algum outro campo.");
			return;
		}
		
		if (periodo>30){
			reportarAviso("O período informado deve ser equivalente a, no máximo, 30 dias.");
			return;
		}
		
		if (anoNumeracaoUnica != null && numeracaoUnica == null){
			reportarAviso("A numeração deverá estar preenchida");
			return;
		}
		
		try {
			if (opcaoLocal == 1)
				documentos = comunicacaoServiceLocal.pesquisarComunicacoes(sigla, numeroProcesso, codigoFaseDocumento, 
						codigoSetorAtual, null, numeracaoUnica, anoNumeracaoUnica, dtIni, dtFim);
			else
				documentos = comunicacaoServiceLocal.pesquisarComunicacoes(sigla, numeroProcesso, codigoFaseDocumento, 
						null, codigoSetorAtual, numeracaoUnica, anoNumeracaoUnica, dtIni, dtFim);

			if (CollectionUtils.isVazia(documentos)) {
				reportarAviso("Nenhum documento encontrado.");
			} else if (documentos != null && documentos.size()>= 250){
				reportarAviso("A consulta retornou muitos registros e por esse motivo foi limitada a 250 registros. Refine a pesquisa e tente novamente.");
			} else {
				reportarInformacao(MessageFormat.format("Foi(ram) encontrado(s) {0} documento(s).", documentos.size()));
			}
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar Expedientes Elaborados.", exception, LOG);
		}

		if(documentos!=null && documentos.size() > 0){
			OrdenacaoUtils.ordenarListaComunicacaoDocumentoResultProcesso(documentos);
		}
			
		setListaDocumentos(getCheckableDocumentoList(documentos));
	}
	
	//calcula o periodo maximo para consulta
	
	public int calculaPeriodo(Calendar primeiraData, Calendar segundaData){
		long periodo = segundaData.getTimeInMillis() - primeiraData.getTimeInMillis();

		return (int) (periodo/(1000*60*60*24));
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

	public List<SelectItem> getItensFaseDocumento() {
		return itensFaseDocumento;
	}

	public void setItensFaseDocumento(List<SelectItem> itensFaseDocumento) {
		this.itensFaseDocumento = itensFaseDocumento;
	}

	public Long getCodigoFaseDocumento() {
		return codigoFaseDocumento;
	}

	public void setCodigoFaseDocumento(Long codigoFaseDocumento) {
		this.codigoFaseDocumento = codigoFaseDocumento;
	}

	public String getSiglaProcesso() {
		return siglaProcesso;
	}

	public void setSiglaProcesso(String siglaProcesso) {
		this.siglaProcesso = siglaProcesso;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public List<SelectItem> getItensSetores() {
		return itensSetores;
	}

	public void setItensSetores(List<SelectItem> itensSetores) {
		this.itensSetores = itensSetores;
	}

	public Long getCodigoSetorAtual() {
		return codigoSetorAtual;
	}

	public void setCodigoSetorAtual(Long codigoSetorAtual) {
		this.codigoSetorAtual = codigoSetorAtual;
	}

	public Long getNumeracaoUnica() {
		return numeracaoUnica;
	}

	public void setNumeracaoUnica(Long numeracaoUnica) {
		this.numeracaoUnica = numeracaoUnica;
	}

	public Long getAnoNumeracaoUnica() {
		return anoNumeracaoUnica;
	}

	public void setAnoNumeracaoUnica(Long anoNumeracaoUnica) {
		this.anoNumeracaoUnica = anoNumeracaoUnica;
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

	public int getPeriodo(){
		return periodo;
	}
	
	public void setPeriodo(int periodo){
		this.periodo = periodo;
	}

	public Integer getOpcaoLocal() {
		return opcaoLocal;
	}

	public void setOpcaoLocal(Integer opcaoLocal) {
		this.opcaoLocal = opcaoLocal;
	}

	public boolean isExibirColunaSetorFase(){
		return (codigoFaseDocumento != null);
	}
	
	public boolean isSetorPermitidoRestrito() throws ServiceException {
		try {
			ConfiguracaoSistema configuracaoSistema = getConfiguracaoSistemaService()
					.recuperarValor(SISTEMA_PROCESSAMENTO, SETOR_DOC_RESTRITOS);
			if (configuracaoSistema != null) {
				return configuracaoSistema.getValor().contains(getSetorUsuarioAutenticado().getId().toString());
			} else {
				return false;
			}
		} catch (ServiceException e) {
			throw new ServiceException("Erro ao recuperar código do setor de restritos.", e);
		}
	}

}
