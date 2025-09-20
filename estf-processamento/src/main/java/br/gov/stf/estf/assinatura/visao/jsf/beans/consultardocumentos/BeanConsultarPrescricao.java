package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentos;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
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

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoPrescricaoParte;
import br.gov.stf.estf.processostf.model.service.PrescricaoReuService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanConsultarPrescricao extends AssinadorBaseBean {

	private static final long serialVersionUID = -8059606167034592897L;
	private static final Log LOG = LogFactory.getLog(BeanConsultarPrescricao.class);

	// ##################### VARIAVEIS DE SESSAO #############################

	private static final String KEY_LISTA_PROCESSOS = BeanConsultarPrescricao.class.getCanonicalName() + ".listaProcessos";
	private static final Object LISTA_MINISTROS = new Object();
	private static final Object NOME_DESTINATARIO = new Object();
	private static final Object CODIGO_DESTINATARIO = new Object();
	private static final Object DESC_DESTINO = new Object();
	private static final Object ITENS_PENA = new Object();
	private static final Object OBJETOINCIDENTE = new Object();
	
	// ################################# VARIAVEIS ###############################	

	private Processo processo;
	private Long idMinistro;
	private Ministro ministro;
	private Long objetoIncidente;
	
	private List<SelectItem> itensMinistros;
	private List<SelectItem> itensPena;
	private List<Ministro> listaMinistros;
	private Short tipoDestino;
	private Long numeroProcesso;
	private Long codigoFaseDocumento;
	private Long anoNumeracaoUnica;
	private Long codigoSetorAtual;
	private Long codigoDestinatario;
	private String siglaClasseProcesso;
	private String siglaNumeroProcesso;
	private String descricaoMinistro;
	private String nomDestinatario;
	private String descDestino;
	private String codigoPena;
	private Date dataInicial;
	private Date dataFinal;
	private Boolean filtroEmTramitacao;

	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> listaProcessos;

	private HtmlDataTable tabelaProcessos;
	
	private static List<String> classes;

	public BeanConsultarPrescricao() {
		restaurarSessao();
	}

	
	// ################################# SESSION ###############################	
	
	@SuppressWarnings("unchecked")
	private void restaurarSessao() {
		restoreStateOfHttpSession();

		if (getAtributo(ITENS_PENA) == null) {
			setAtributo(ITENS_PENA, carregarComboPenas());
		}
		setItensPena((List<SelectItem>) getAtributo(ITENS_PENA));
		
		if (getAtributo(LISTA_MINISTROS) == null) {
			setAtributo(LISTA_MINISTROS, carregarComboMinistros());
		}
		
		setListaMinistros((List<Ministro>) getAtributo(LISTA_MINISTROS));

		setListaProcessos((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_PROCESSOS));

		setNomDestinatario((String) getAtributo(NOME_DESTINATARIO));
		
		setDescDestino((String) getAtributo(DESC_DESTINO));
		
		if(getAtributo(CODIGO_DESTINATARIO) != null){
			setCodigoDestinatario((Long) getAtributo(CODIGO_DESTINATARIO));
		}
		
		if (getAtributo(OBJETOINCIDENTE) == null) {
			setAtributo(OBJETOINCIDENTE, objetoIncidente);
		}
		setObjetoIncidente((Long) getAtributo(OBJETOINCIDENTE));

		atualizaSessao();

	}

	public void atualizaSessao() {
		applyStateInHttpSession();
		setAtributo(KEY_LISTA_PROCESSOS, listaProcessos);
		setAtributo(LISTA_MINISTROS, listaMinistros);
		setAtributo(ITENS_PENA, itensPena);
		setAtributo(NOME_DESTINATARIO, nomDestinatario);
		setAtributo(CODIGO_DESTINATARIO, codigoDestinatario);

	}

	// ################################# ACTION ###############################

	public void atualizaSessaoAction(ActionEvent evt) {
		atualizaSessao();
	}
	
	public void pesquisarProcessosPrescricaoAction(ActionEvent evt) throws ParseException{
		pesquisaProcessosPrescricao();
		atualizaSessao();
	}
	
	public void limparPrescricaoAction(ActionEvent evt) {
		limparCampos();
		atualizaSessao();
	}
	

	// ############################## METHODS ################################

	public void limparCampos() {
		setListaProcessos(null);
		setSiglaNumeroProcesso(null);
		setListaMinistros(null);
		setCodigoFaseDocumento(null);
		setDescricaoMinistro("");
		setNomDestinatario("");
		setDataInicial(null);
		setDataFinal(null);
		setCodigoPena(null);
		setObjetoIncidente(null);
		setDescricaoMinistro(null);
	}
	
	// formatar data
	public String formatarData(Date data) throws ParseException {
		if (data == null)
			return null;
		return DateFormatUtils.format(data, "dd/MM/yyyy");
	}
	
	
	/**
	 * Compara o valor digitado com a os dados já pesquisados do banco de dados
	 */
	public List pesquisarMinistros(Object value){
		List<Ministro> listaMinistroSelecionado = new LinkedList<Ministro>();
		if (listaMinistros != null){
			for(Ministro min : listaMinistros){
				if (min.getNome().toUpperCase().contains(value.toString().toUpperCase())){
					listaMinistroSelecionado.add(min);
				}
			}
			
		}
		return listaMinistroSelecionado;
	}
	
	
	public static int dataDiff(java.util.Date dataLow, java.util.Date dataHigh){  
		  
	     GregorianCalendar startTime = new GregorianCalendar();  
	     GregorianCalendar endTime = new GregorianCalendar();  
	       
	     GregorianCalendar curTime = new GregorianCalendar();  
	     GregorianCalendar baseTime = new GregorianCalendar();  
	  
	     startTime.setTime(dataLow);  
	     endTime.setTime(dataHigh);  
	       
	     int dif_multiplier = 1;  
	       
	     // Verifica a ordem de inicio das datas  
	     if( dataLow.compareTo( dataHigh ) < 0 ){  
	         baseTime.setTime(dataHigh);  
	         curTime.setTime(dataLow);  
	         dif_multiplier = 1;  
	     }else{  
	         baseTime.setTime(dataLow);  
	         curTime.setTime(dataHigh);  
	         dif_multiplier = -1;  
	     }  
	       
	     int result_years = 0;  
	     int result_months = 0;  
	     int result_days = 0;  
	  
	     // Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import acumulando  
	     // no total de dias. Ja leva em consideracao ano bissesto  
	     while( curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR) ||  
	            curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH)  )  
	     {  
	           
	         int max_day = curTime.getActualMaximum( GregorianCalendar.DAY_OF_MONTH );  
	         result_months += max_day;  
	         curTime.add(GregorianCalendar.MONTH, 1);  
	           
	     }  
	       
	     // Marca que é um saldo negativo ou positivo  
	     result_months = result_months*dif_multiplier;  
	       
	       
	     // Retirna a diferenca de dias do total dos meses  
	     result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));  
	       
	     return result_years+result_months+result_days;  
	}  
	

	public void pesquisaProcessosPrescricao() throws ParseException {
		
		List<ProcessoPrescricaoParte> listaProcessoPresc = new LinkedList<ProcessoPrescricaoParte>();
		PrescricaoReuService prescricaoReuService = getPrescricaoReuService();
		
		if (dataInicial != null && dataFinal == null){
			reportarAviso("Preencha a data final na consulta.");
			return;
		}else if (dataInicial == null && dataFinal != null){
			reportarAviso("Preencha a data inicial na consulta.");
			return;
		}
		
		
		//zera a variável codigoDestinatario caso o usuário limpe o campo na tela
		if (nomDestinatario == null || nomDestinatario.trim().length() == 0){
			codigoDestinatario = null;
			reportarAviso("Não é possível realizar a pesquisa. O campo  \"Localização Atual\" é obrigatório.");			
			return;
		}
		//zera a variável descricaoMinistro caso o usuário limpe o campo na tela
		if (descricaoMinistro == null || descricaoMinistro.trim().length() == 0)
			idMinistro = null;
		//zera a variável siglaNumeroProcesso caso o usuário limpe o campo na tela
		if (siglaNumeroProcesso == null || siglaNumeroProcesso.trim().length() == 0)
			objetoIncidente = null;
		
		try {
			listaProcessoPresc = prescricaoReuService.pesquisarProcessosPrescricao(objetoIncidente, dataInicial, dataFinal, codigoDestinatario,
					idMinistro, codigoPena, false);

			if (CollectionUtils.isVazia(listaProcessoPresc)) {
				reportarAviso("Nenhum processo encontrado.");
			} else {
				reportarInformacao(MessageFormat.format("Foi(ram) encontrado(s) {0} processo(s).", listaProcessoPresc.size()));
			}
		} catch (ServiceException exception) {
			reportarErro("Erro ao pesquisar processos prescrição.", exception, LOG);
			return;
		} 
		
		setListaProcessos(getCheckableDocumentoListProcessoPrescricao(listaProcessoPresc));
		
	}
	
	
	@SuppressWarnings({ "rawtypes" })
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
					setNumeroProcesso(lNumero);
					setSiglaClasseProcesso(sigla);

					if (processo != null) {
						incidentes = recuperarIncidentes(processo.getId());
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				reportarErro("Número de processo inválido: " + siglaNumero);
			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao pesquisar os incidentes do processo: " + siglaNumero);
			}
		}
		atualizaSessao();
		return incidentes;
	}
	
	public List<SelectItem> carregarComboPenas(){
		
		List<SelectItem> listaPenas = new ArrayList<SelectItem>();
		listaPenas.add(new SelectItem(null, null));
		listaPenas.add(new SelectItem("A", "Abstrata"));
		listaPenas.add(new SelectItem("C", "Concreta"));
		return listaPenas;
	}
	
	

	// ######################## GETS AND SETs ###############################

	public HtmlDataTable getTabelaProcessos() {
		return tabelaProcessos;
	}

	public void setTabelaProcessos(HtmlDataTable tabelaProcessos) {
		this.tabelaProcessos = tabelaProcessos;
	}

	public List<CheckableDataTableRowWrapper> getListaProcessos() {
		return listaProcessos;
	}

	public void setListaProcessos(List<CheckableDataTableRowWrapper> listaProcessos) {
		this.listaProcessos = listaProcessos;
	}

	public Long getCodigoFaseDocumento() {
		return codigoFaseDocumento;
	}

	public void setCodigoFaseDocumento(Long codigoFaseDocumento) {
		this.codigoFaseDocumento = codigoFaseDocumento;
	}

	public String getSiglaNumeroProcesso() {
		return siglaNumeroProcesso;
	}

	public void setSiglaNumeroProcesso(String siglaNumeroProcesso) {
		this.siglaNumeroProcesso = siglaNumeroProcesso;
	}

	public Long getCodigoSetorAtual() {
		return codigoSetorAtual;
	}

	public void setCodigoSetorAtual(Long codigoSetorAtual) {
		this.codigoSetorAtual = codigoSetorAtual;
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

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getSiglaClasseProcesso() {
		return siglaClasseProcesso;
	}

	public void setSiglaClasseProcesso(String siglaClasseProcesso) {
		this.siglaClasseProcesso = siglaClasseProcesso;
	}


	public List<SelectItem> getItensMinistros() {
		return itensMinistros;
	}


	public void setItensMinistros(List<SelectItem> itensMinistros) {
		this.itensMinistros = itensMinistros;
	}


	public Long getIdMinistro() {
		return idMinistro;
	}

	public void setIdMinistro(Long idMinistro) {
		this.idMinistro = idMinistro;
	}

	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public String getDescricaoMinistro() {
		return descricaoMinistro;
	}

	public void setDescricaoMinistro(String descricaoMinistro) {
		this.descricaoMinistro = descricaoMinistro;
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public List<Ministro> getListaMinistros() {
		return listaMinistros;
	}

	public void setListaMinistros(List<Ministro> listaMinistros) {
		this.listaMinistros = listaMinistros;
	}

	public Long getCodigoDestinatario() {
		return codigoDestinatario;
	}

	public void setCodigoDestinatario(Long codigoDestinatario) {
		this.codigoDestinatario = codigoDestinatario;
	}

	public String getNomDestinatario() {
		return nomDestinatario;
	}

	public void setNomDestinatario(String nomDestinatario) {
		this.nomDestinatario = nomDestinatario;
	}

	public Short getTipoDestino() {
		return tipoDestino;
	}

	public void setTipoDestino(Short tipoDestino) {
		this.tipoDestino = tipoDestino;
	}

	public String getDescDestino() {
		return descDestino;
	}


	public void setDescDestino(String descDestino) {
		this.descDestino = descDestino;
	}


	public List<SelectItem> getItensPena() {
		return itensPena;
	}


	public void setItensPena(List<SelectItem> itensPena) {
		this.itensPena = itensPena;
	}


	public String getCodigoPena() {
		return codigoPena;
	}


	public void setCodigoPena(String codigoPena) {
		this.codigoPena = codigoPena;
	}


	public Boolean getFiltroEmTramitacao() {
		return filtroEmTramitacao;
	}


	public void setFiltroEmTramitacao(Boolean filtroEmTramitacao) {
		this.filtroEmTramitacao = filtroEmTramitacao;
	}

}
