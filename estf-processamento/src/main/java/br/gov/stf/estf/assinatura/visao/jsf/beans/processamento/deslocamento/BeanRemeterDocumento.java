package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import br.gov.stf.estf.assinatura.deslocamento.origemdestino.ResultSuggestionOrigemDestino;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.PeticaoParser;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.localizacao.Destinatario;
import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.service.ProcessoException;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfoImpl;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

/**
 * @author RicardoLe
 *
 */
public class BeanRemeterDocumento extends AssinadorBaseBean {

	private static final long serialVersionUID = 1L;
	
	@KeepStateInHttpSession
	private String idDocumento;
	private Long objetoIncidente;
	private String siglaProcesso;
	private Long numeroProcesso;
	private Processo processo;
	private String nomDestinatario;
	private Long codigoDestinatario;
	private String tipoRemessa; // define o destino (setor ou órgão externo). Domínio: SET/ORG
	private String observacao;
	private String tipoGuia; // define se a guia é de processo ou petição. Domínio: PRO/PET/PRE
	private String nomRelator;
	private String numGuia;
	private String anoGuia;
	private String descricaoGuia;
	private String descricaoDestino;
	private String tipoAnteriorGuia;
	private Long codigoLotacao;
	private Boolean postal;
	
	// flags de hiddens para criticas do deslocamento utilizados pelas funções de script
	private String destinoDiferenteRelator;
	private String peticoesPendentesDeTratamento;
	private String peticoesNaoJuntadas;
	private String mensagemConfirmacao;
	private String processosCriticaRelator; // processos com relatorio divergente do destino
	private String processosCriticaPetPendente; // processos com petições pendente de tratamento pelo eGab
	private String processosCriticaNaoJuntadoPet; // processos sem petições juntadas
	
	private String sucesso; // representa persistencia sem erros "S" ou "N" quando não foi possível persistir o deslocamento.
	private Boolean habilitaDesabilita;

	private Boolean visualizarGuia;
	private Short tipoDestino;
	private String descDestino;
	// atributos utilizados na seleção do endereço para o deslocamento de baixa e expedição
	private String enderecoRemessa;
	private Long codigoDestinatarioBaixaExpedicao;
	private String nomDestinatarioBaixaExpedicao;
	private List<EnderecoDestinatario> listaEnderecos;
	private Long seqEndereco;
	// atributos para apresentação do endereço na tela
	private String panelHeader;
	private String panelLogradouro;
	private String panelNumLocalizacao;
	private String panelBairro;
	private String panelMunicipio;
	private String panelUf;
	private Integer panelCep;
	private Boolean naoEncontrouEndereco;

	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> listaDocumento;
	private List<CheckableDataTableRowWrapper> listaDependencia;
	private List<CheckableDataTableRowWrapper> listaDependenciaFinal;
	private List<String> listaControleIcones;

	private org.richfaces.component.html.HtmlDataTable tabelaDocumentos;
	private org.richfaces.component.html.HtmlDataTable tabelaDependencias;
	private org.richfaces.component.html.HtmlDataTable tabelaEnderecos;

	// ---------------------- VARIAVEIS DE SESSAO ----------------------------//
	private static final String KEY_LISTA_DOCUMENTOS = BeanRemeterDocumento.class.getCanonicalName() + ".listaDocumento";
	private static final String KEY_LISTA_DEPENDENCIAS = BeanRemeterDocumento.class.getCanonicalName() + ".listaDependencia";
	private static final String KEY_LISTA_DEPENDENCIAS_FINAL = BeanRemeterDocumento.class.getCanonicalName() + ".listaDependenciaFinal";
	
	private static final Object SEQ_ENDERECO = new Object();
	private static final Object LISTA_ENDERECOS = new Object();
	private static final Object LISTA_CONTROLE_ICONES = new Object();
	private static final Object OBJETOINCIDENTE = new Object();
	private static final Object SIGLA_PROCESSO = new Object();
	private static final Object NUMERO_PROCESSO = new Object();
	private static final Object NOME_DESTINATARIO = new Object();
	private static final Object CODIGO_DESTINATARIO = new Object();
	private static final Object TIPO_REMESSA = new Object();
	private static final Object OBSERVACAO = new Object();
	private static final Object TIPO_GUIA = new Object();
	private static final Object NOME_RELATOR = new Object();
	private static final Object NUM_GUIA = new Object();
	private static final Object ANO_GUIA = new Object();
	private static final Object DESCRICAO_GUIA = new Object();
	private static final Object DESCRICAO_DESTINO = new Object();
	private static final Object TIPO_ANTERIOR_GUIA = new Object();
	private static final Object CODIGOLOTACAO = new Object();
	private static final Object POSTAL = new Object();
	private static final Object DESTINO_DIFERENTE_RELATOR = new Object();
	private static final Object SUCESSO = new Object();
	private static final Object ID_DOCUMENTO = new Object();
	private static final Object PROCESSOS_CRITICA_RELATOR = new Object();
	private static final Object PROCESSOS_CRITICA_PET_PENDENTE = new Object();
	private static final Object PROCESSOS_CRITICA_NAO_JUNTADO_PET = new Object();
	private static final Object PETICOES_PENDENTES_TRATAMENTO = new Object();
	private static final Object PETICOES_NAO_JUNTADAS = new Object();
	
	private static final Object DESC_DESTINO = new Object();
	private static final Object ENDERECO_REMESSA = new Object();
	private static final Object CODIGO_DESTINATARIO_BAIXA_EXPEDICAO = new Object();
	private static final Object NOM_DESTINATARIO_BAIXA_EXPEDICAO = new Object();

	private static final Object PANEL_HEADER = new Object();
	private static final Object PANEL_LOGRADOURO = new Object();
	private static final Object PANEL_NUM_LOCALIZACAO = new Object();
	private static final Object PANEL_BAIRRO = new Object();
	private static final Object PANEL_MUNICIPIO = new Object();
	private static final Object PANEL_UF = new Object();
	private static final Object PANEL_CEP = new Object();
	private static final Object NAO_ENCONTROU_ENDERECO = new Object();
	
	public static final Object REMETER_INCIDENTE_TRANS_BEAN = new Object();
	
	public static final Long COD_SETOR_COMPOSICAO_E_CONTROLE_DE_ACORDAOS = (long) 600000903;
	public static final Long COD_SETOR_BAIXA_E_EXPEDICAO = (long) 600000902;
	public static final Long COD_SETOR_ARQUIVO = (long) 600000789;
	
	public static final String TIPO_INCLUSAO_PROCESSO = "PROCESSO";
	public static final String TIPO_INCLUSAO_IMPORTACAO = "IMPORTACAO";

	private List<String> classes;
	private List<ObjetoIncidente<?>> listaProcessoImportacao;
	private String tipoInclusao;

	public static final Long CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO = 600000857L;
	private String mensagemDeRestricaoRegistroDeAndamento = null;
	private static final String MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO = "MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO";

	// ----------------------------------------------------------------------//

	public BeanRemeterDocumento() {
		restaurarSessao();
		setVisualizarGuia(false);
	}
	
	@SuppressWarnings("unchecked")
	private void restaurarPanelEndereco(){
		setPanelHeader((String) getAtributo(PANEL_HEADER));
		setPanelLogradouro((String) getAtributo(PANEL_LOGRADOURO));
		setPanelNumLocalizacao((String) getAtributo(PANEL_NUM_LOCALIZACAO));
		setPanelBairro((String) getAtributo(PANEL_BAIRRO));
		setPanelMunicipio((String) getAtributo(PANEL_MUNICIPIO));
		setPanelUf((String) getAtributo(PANEL_UF));
		setPanelCep((Integer) getAtributo(PANEL_CEP));
		if (getAtributo(NAO_ENCONTROU_ENDERECO) == null) {
			setNaoEncontrouEndereco(false);
		} else {
			setNaoEncontrouEndereco((Boolean) getAtributo(NAO_ENCONTROU_ENDERECO));
		}
		
		if (getAtributo(LISTA_ENDERECOS) == null) {
			setAtributo(LISTA_ENDERECOS, new ArrayList<EnderecoDestinatario>());
		} else {
			setListaEnderecos((List<EnderecoDestinatario>) getAtributo(LISTA_ENDERECOS));
		}
		setSeqEndereco((Long) getAtributo(SEQ_ENDERECO));
		setEnderecoRemessa((String) getAtributo(ENDERECO_REMESSA));
		setCodigoDestinatarioBaixaExpedicao((Long) getAtributo(CODIGO_DESTINATARIO_BAIXA_EXPEDICAO));
		setNomDestinatarioBaixaExpedicao((String) getAtributo(NOM_DESTINATARIO_BAIXA_EXPEDICAO));
	}

	// ------------------------ SESSAO ------------------------

	@SuppressWarnings("unchecked")
	private void restaurarSessao() {
		
		checkParamFrame();
		restaurarPanelEndereco();
		
		if (getAtributo(OBJETOINCIDENTE) == null) {
			setAtributo(OBJETOINCIDENTE, objetoIncidente);
		}

		if (getAtributo(KEY_LISTA_DOCUMENTOS) == null) {
			setAtributo(KEY_LISTA_DOCUMENTOS, new ArrayList<CheckableDataTableRowWrapper>());
		} else {
			setListaDocumento((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));
		}

		if (getAtributo(KEY_LISTA_DEPENDENCIAS) == null) {
			setAtributo(KEY_LISTA_DEPENDENCIAS, new ArrayList<CheckableDataTableRowWrapper>());
		} else {
			setListaDependencia((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DEPENDENCIAS));
		}
		
		if(getAtributo(KEY_LISTA_DEPENDENCIAS_FINAL) == null){
			setAtributo(KEY_LISTA_DEPENDENCIAS_FINAL, new ArrayList<CheckableDataTableRowWrapper>());
		}else{
			setListaDependenciaFinal((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DEPENDENCIAS_FINAL));
		}
		// Inicializa tipo_guia para "processo" e tipo_remessa com destino para "setor do STF"
		if (getAtributo(TIPO_GUIA) == null) {
			setAtributo(TIPO_GUIA, "PRO");
		}
		if (getAtributo(TIPO_REMESSA) == null) {
			setAtributo(TIPO_REMESSA, "SET");
		}

		if (getAtributo(LISTA_CONTROLE_ICONES) == null) {
			setListaControleIcones((List<String>) new ArrayList<String>());
		} else {
			setListaControleIcones((List<String>) getAtributo(LISTA_CONTROLE_ICONES));
		}
		
		setObjetoIncidente((Long) getAtributo(OBJETOINCIDENTE));
		setNumeroProcesso((Long) getAtributo(NUMERO_PROCESSO));
		setIdDocumento((String) getAtributo(ID_DOCUMENTO));
		setSiglaProcesso((String) getAtributo(SIGLA_PROCESSO));
		setNomDestinatario((String) getAtributo(NOME_DESTINATARIO));
		setDescDestino((String) getAtributo(DESC_DESTINO));
		if(getAtributo(CODIGO_DESTINATARIO) != null){
			setCodigoDestinatario((Long) getAtributo(CODIGO_DESTINATARIO));
		}
		setTipoRemessa((String) getAtributo(TIPO_REMESSA));
		setObservacao((String) getAtributo(OBSERVACAO));
		setTipoAnteriorGuia((String) getAtributo(TIPO_ANTERIOR_GUIA));
		setTipoGuia((String) getAtributo(TIPO_GUIA));
		setNomRelator((String) getAtributo(NOME_RELATOR));
		setNumGuia((String) getAtributo(NUM_GUIA));
		setAnoGuia((String) getAtributo(ANO_GUIA));
		if (getAtributo(TIPO_GUIA).equals("PRO")) {
			setDescricaoGuia("Processo Físico");
		} else if (getAtributo(TIPO_GUIA).equals("PRE")) {
			setDescricaoGuia("Processo Eletrônico");
		} else if (getAtributo(TIPO_GUIA).equals("PET")) {
			setDescricaoGuia("Petição Física");
		} else if (getAtributo(TIPO_GUIA).equals("PEE")) {
			setDescricaoGuia("Petição Eletrônica");
		}

		if (getAtributo(TIPO_REMESSA).equals("SET")) {
			setDescricaoDestino("Setor do STF");
		} else if (getAtributo(TIPO_REMESSA).equals("ORG")) {
			setDescricaoDestino("Órgão Externo");
		}

		if (getAtributo(CODIGOLOTACAO) == null) {
			setCodigoLotacao(getSetorUsuarioAutenticado().getId());
		} else {
			setCodigoLotacao((Long) getAtributo(CODIGOLOTACAO));
		}
		setPostal((Boolean) getAtributo(POSTAL));
		if (getAtributo(DESTINO_DIFERENTE_RELATOR) == null) {
			setDestinoDiferenteRelator("N");
		} else {
			setDestinoDiferenteRelator((String) getAtributo(DESTINO_DIFERENTE_RELATOR));
		}
		if (getAtributo(PETICOES_PENDENTES_TRATAMENTO) == null) {
			setPeticoesPendentesDeTratamento("N");
		} else {
			setPeticoesPendentesDeTratamento((String) getAtributo(PETICOES_PENDENTES_TRATAMENTO));
		}
		if (getAtributo(PETICOES_NAO_JUNTADAS) == null) {
			setPeticoesNaoJuntadas("N");
		} else {
			setPeticoesNaoJuntadas((String) getAtributo(PETICOES_NAO_JUNTADAS));
		}
		setProcessosCriticaRelator((String) getAtributo(PROCESSOS_CRITICA_RELATOR));
		setProcessosCriticaPetPendente((String) getAtributo(PROCESSOS_CRITICA_PET_PENDENTE));
		setProcessosCriticaNaoJuntadoPet((String) getAtributo(PROCESSOS_CRITICA_NAO_JUNTADO_PET));

		setSucesso((String) getAtributo(SUCESSO));

		if (getAtributo(REMETER_INCIDENTE_TRANS_BEAN) != null) {

			ObjetoIncidente<?> obIncidente = (ObjetoIncidente<?>) getAtributo(REMETER_INCIDENTE_TRANS_BEAN);
			if (obIncidente != null) {
				setTipoRemessa("SET");
				
				String tipo = descobrirTipoGuia(obIncidente);
				setTipoGuia(tipo);
				if (tipo.equals("PRO")) {
					setDescricaoGuia("Processo Físico");
				} else if (tipo.equals("PRE")) {
					setDescricaoGuia("Processo Eletrônico");
				} else if (tipo.equals("PET")) {
					setDescricaoGuia("Petição Física");
				} else if (tipo.equals("PEE")) {
					setDescricaoGuia("Petição Eletrônico");
				}
				
				setIdDocumento(obIncidente.getIdentificacao()); // INPUT
				setObjetoIncidente(obIncidente.getId()); // É NESCESSARIO PARA O
															// insereItemDocumento();
				try {
					if (getAtributo(KEY_LISTA_DOCUMENTOS) == null) {
						if (listaDocumento != null) {
							listaDocumento.clear();
						}
						setAtributo(KEY_LISTA_DOCUMENTOS, new ArrayList<CheckableDataTableRowWrapper>());
						setListaDocumento((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));
					} else {
						if (listaDocumento != null) {
							listaDocumento.clear();
						}
						setListaDocumento((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));
					}
					insereItemDocumento();
				} catch (ServiceException e) {
					reportarAviso("Erro de transição. Mensagem do erro: " + e.getMessage());
				}
			}
		}
		mensagemDeRestricaoRegistroDeAndamento = (String)getAtributo(MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO);
	}

	// ----------------------- ATUALIZACAO --------------------- //
	
	public void atualizarTipoInclusao(ActionEvent evt) {
		limparDadosTipoInclusao();
	}
	
	private void limparDadosTipoInclusao() {

	}
	
	public String getEstiloVisibilidadeImportacao(){
		return getEstiloVisibilidadeInclusaoPor(TIPO_INCLUSAO_IMPORTACAO);
	}

	private String getEstiloVisibilidadeInclusaoPor(String tipoInclusaoReferencia) {
		if (!tipoInclusaoReferencia.equals(tipoInclusao)) {
			return "display:none";
		}
		return "";

	}

	public String getEstiloVisibilidadeProcesso() {
		return getEstiloVisibilidadeInclusaoPor(TIPO_INCLUSAO_PROCESSO);
	}

	private void atualizaSessaoEndereco() {
		setAtributo(SEQ_ENDERECO, seqEndereco); 
		setAtributo(NOM_DESTINATARIO_BAIXA_EXPEDICAO, nomDestinatarioBaixaExpedicao); 
		setAtributo(CODIGO_DESTINATARIO_BAIXA_EXPEDICAO, codigoDestinatarioBaixaExpedicao); 
		setAtributo(ENDERECO_REMESSA, enderecoRemessa); 
		setAtributo(LISTA_ENDERECOS, listaEnderecos);
		setAtributo(PANEL_HEADER, panelHeader);
		setAtributo(PANEL_LOGRADOURO, panelLogradouro);
		setAtributo(PANEL_BAIRRO, panelBairro);
		setAtributo(PANEL_NUM_LOCALIZACAO, panelNumLocalizacao);
		setAtributo(PANEL_MUNICIPIO, panelMunicipio);
		setAtributo(PANEL_CEP, panelCep);
		setAtributo(PANEL_UF, panelUf);
		setAtributo(NAO_ENCONTROU_ENDERECO, naoEncontrouEndereco);
	}
	
	private void atualizaSessaoTipoGuia(){
		if (tipoGuia.equals("PRO")) {
			setDescricaoGuia("Processo Físico");
		} else if (tipoGuia.equals("PRE")) {
			setDescricaoGuia("Processo Eletrônico");
		} else if (tipoGuia.equals("PET")) {
			setDescricaoGuia("Petição Física");
		} else if (getAtributo(TIPO_GUIA).equals("PEE")) {
			setDescricaoGuia("Petição Eletrônica");
		}
		setAtributo(DESCRICAO_GUIA, descricaoGuia);
	}
	
	private void atualizaSessaoTipoRemessa(){
		if (tipoRemessa.equals("SET")) {
			setDescricaoDestino("Setor do STF");
		} else if (tipoRemessa.equals("ORG")) {
			setDescricaoDestino("Órgão Externo");
		}
	}
	
	public void atualizaSessao() {
		setAtributo(LISTA_CONTROLE_ICONES, listaControleIcones);
		setAtributo(OBJETOINCIDENTE, objetoIncidente);
		setAtributo(SIGLA_PROCESSO, siglaProcesso);
		setAtributo(ID_DOCUMENTO, idDocumento);
		setAtributo(NUMERO_PROCESSO, numeroProcesso);
		setAtributo(NOME_DESTINATARIO, nomDestinatario);
		setAtributo(CODIGO_DESTINATARIO, codigoDestinatario);
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumento);
		setAtributo(KEY_LISTA_DEPENDENCIAS, listaDependencia);
		setAtributo(KEY_LISTA_DEPENDENCIAS_FINAL, listaDependenciaFinal);
		setAtributo(TIPO_REMESSA, tipoRemessa);
		setAtributo(OBSERVACAO, observacao);
		setAtributo(TIPO_ANTERIOR_GUIA, tipoAnteriorGuia);
		setAtributo(TIPO_GUIA, tipoGuia);
		setAtributo(NOME_RELATOR, nomRelator);
		setAtributo(NUM_GUIA, numGuia);
		setAtributo(ANO_GUIA, anoGuia);
		setAtributo(REMETER_INCIDENTE_TRANS_BEAN, null);

		setAtributo(DESCRICAO_DESTINO, descricaoDestino);
		setAtributo(CODIGOLOTACAO, codigoLotacao);
		setAtributo(POSTAL, postal);
		setAtributo(DESTINO_DIFERENTE_RELATOR, destinoDiferenteRelator);
		setAtributo(PETICOES_PENDENTES_TRATAMENTO, peticoesPendentesDeTratamento);
		setAtributo(PETICOES_NAO_JUNTADAS, peticoesNaoJuntadas);
		setAtributo(PROCESSOS_CRITICA_RELATOR, processosCriticaRelator);
		setAtributo(PROCESSOS_CRITICA_PET_PENDENTE, processosCriticaPetPendente);
		setAtributo(PROCESSOS_CRITICA_NAO_JUNTADO_PET, processosCriticaNaoJuntadoPet);
		setAtributo(SUCESSO, sucesso);
		setAtributo(DESC_DESTINO, descDestino);
		atualizaSessaoEndereco();
		atualizaSessaoTipoGuia();
		atualizaSessaoTipoRemessa();
	}

	// ------------------------ METHODS ------------------------
	
	public Integer getQuantidadeVinculados() throws ServiceException{
		ProcessoDependenciaService dependenciaService = getProcessoDependenciaService();
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();
		Processo processo = (Processo) chkDataTable.getWrappedObject();
		if (processo == null) {
			reportarErro("Não foi possível recuperar o processo.");
		}
		if (dependenciaService == null) {
			reportarErro("Não foi possível recuperar a quantidade de vinculados ao processo.");
			return null;
		} else {
			return dependenciaService.getQuantidadeVinculados(processo);
		}
		
	}
	
	public String getExibeLabelGuia(){
		if (listaDocumento != null && listaDocumento.size()>0) {
			return descricaoGuia;
		} else {
			return "";
		}
	}
	
	public Integer getTotalVinculos() throws ServiceException {
		Integer total = getQuantidadeVinculados();
		if ( total == 0) {
			return null;
		} else {
			return total;
		}
	}

	private Processo obterProcessoSelecionado() {
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();
		Processo processo = (Processo) chkDataTable.getWrappedObject();
		return processo;
	}
	
	public String getImagemMaisMenos() {

		// recuperar os apensos do processo
		Processo processo = obterProcessoSelecionado();
		
		if (listaControleIcones == null || listaControleIcones.size() == 0) {
			return "/images/switch_plus.gif";
		}
		
		for (int i=0; i<=listaControleIcones.size()-1; i++) {
			String processoStatus = listaControleIcones.get(i);
			// quebrar em sigla + processo + simbolo (+/-)
			String[] partes = processoStatus.split("\\/");
			
			if (processo.getSiglaClasseProcessual().equals(partes[0]) && 
				processo.getNumeroProcessual().equals(Long.parseLong((partes[1]))) &&
			    partes[2].equals("+")) {
				return "/images/switch_plus.gif";
			} else if (processo.getSiglaClasseProcessual().equals(partes[0]) && 
					processo.getNumeroProcessual().equals(Long.parseLong((partes[1]))) &&
				    partes[2].equals("-"))	{
				return "/images/switch_minus.gif";
			}
		}
		return "/images/switch_plus.gif";
		
	}
	
	private Peticao obterPeticaoSelecionada(){
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();
		Peticao peticao = (Peticao) chkDataTable.getWrappedObject();
		return peticao;
	}
	
	public String getProcessoVinculadoPeticao() throws ServiceException {
		Peticao peticao = obterPeticaoSelecionada();
		// petição sem processo
		if (peticao.getObjetoIncidenteVinculado() == null || peticao.getObjetoIncidenteVinculado() instanceof Protocolo) {
			return "";
		}
		Processo processo = getProcessoService().recuperarPorId(peticao.getObjetoIncidenteVinculado().getId());
		if (processo == null) {
			return "";
		} else {
			return processo.getIdentificacao();
		}
	}
		
	public boolean getIsProcessoVinculadoEletronico() throws ServiceException {
		Peticao peticao = obterPeticaoSelecionada();
		// petição sem processo
		if (peticao.getObjetoIncidenteVinculado() == null || peticao.getObjetoIncidenteVinculado() instanceof Protocolo) {
			return false;
		}
		Processo processo = getProcessoService().recuperarPorId(peticao.getObjetoIncidenteVinculado().getId());
		if (processo == null) {
			return false;
		} else if(processo.getTipoMeio().equals("E")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean getIsApenso() throws ServiceException {
		Processo processo = obterProcessoSelecionado();
		return getProcessoDependenciaService().isApenso(processo);
	}
	
	public void insereApensosNaLista(ActionEvent evt) throws Exception {
		
		// recuperar o processo principal
		Processo processo = obterProcessoSelecionado();
		
		// recuperar os apensos
		List<ProcessoDependencia> apensos = getProcessoDependenciaService().recuperarApensos(processo);
		List<Processo> processos = new ArrayList<Processo>();
		// transformar todos os apensos no tipo processo para inserir na lista 
		for (ProcessoDependencia apenso : apensos) {
		    Processo processoApenso = getProcessoService().recuperarPorId(apenso.getIdObjetoIncidente());
		    processos.add(processoApenso);
		}

		listaDependencia.clear();
		listaDependencia.addAll(getCheckableDataTableRowWrapperList(processos));
	
		if(!listaDependenciaFinal.containsAll(listaDependencia)){
			listaDependenciaFinal.addAll(listaDependencia);
		}
		// verificar se o processo principal já foi expandido
		boolean expandido = false;
		for (int i=0; i<=listaDocumento.size()-1; i++) {
			for (int j=0; j<=listaDependencia.size()-1; j++) {
				if (listaDocumento.get(i).equals(listaDependencia.get(j))) {
					expandido = true;
				}
			}
		}
		
		listaControleIcones.remove(processo.getSiglaClasseProcessual() + "/" + processo.getNumeroProcessual() + "/+");
		listaControleIcones.remove(processo.getSiglaClasseProcessual() + "/" + processo.getNumeroProcessual() + "/-");
		if (!expandido) {
			// Inserir processo na lista imediatamente após o processo principal
			listaDocumento.addAll(tabelaDocumentos.getRowIndex()+1, listaDependencia);
			listaControleIcones.add(processo.getSiglaClasseProcessual() + "/" + processo.getNumeroProcessual() + "/-");
		} else {
			listaDocumento.removeAll(listaDependencia);
			listaControleIcones.add(processo.getSiglaClasseProcessual() + "/" + processo.getNumeroProcessual() + "/+");
		}
		atualizaSessao();
		
	}
	
	public void recuperarEnderecos() throws ServiceException{
		List<EnderecoDestinatario> enderecos = getEnderecoDestinatarioService().recuperarEnderecoDoDestinatario(codigoDestinatarioBaixaExpedicao);
		if (enderecos == null || enderecos.size() == 0) {
			naoEncontrouEndereco = true;
		} else {
			naoEncontrouEndereco = false;
		}
		
		setListaEnderecos(enderecos);
		atualizaSessao();
	}
	
	public void limpaEnderecos(ActionEvent evt) {
		setListaEnderecos(null);
		atualizaSessao();
	}
	
	public void limpaEnderecoSelecionado(ActionEvent evt) {
		setEnderecoRemessa("Nenhum endereço foi selecionado.");
		setSeqEndereco(null);
	
		setPanelHeader(null);
		setPanelLogradouro(null);
		setPanelNumLocalizacao(null);
		setPanelBairro(null);
		setPanelCep(null);
		setPanelMunicipio(null);
		setPanelUf(null);
		
		atualizaSessao();
	}
	
	public Boolean getTemEndereco(){
		return (seqEndereco == null) ? false : true;
	}
	
	/**
	 * disponibiliza a pesquisa para o componente suggestion na interface
	 * o método pesquisarDestinatario() está implementado no AssinadorBaseBean
	 * @throws ServiceException 
	 */
	public List<Destinatario> pesquisarDestinatarioBaixaExpedicaoAction(Object value) throws ServiceException {
		return getDestinatarioService().recuperarDestinatarioDaOrigem(codigoDestinatario, value.toString());
	}
	
	/**
	 * disponibiliza a pesquisa para o componente suggestion na interface
	 * o método pesquisarDestinatario() está implementado no AssinadorBaseBean
	 */
	@SuppressWarnings("unchecked")
	public List<ResultSuggestionOrigemDestino> pesquisarDestinatarioAction(Object value) {
		return  pesquisarOrigem(value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ObjetoIncidente pesquisarIncidentesPrincipal(Object value) throws ServiceException {
		List<ObjetoIncidente> incidentes = null;
		
		// descobre o tipo da guia
		//tipoGuia = descobrirTipoGuia(value);
		//setAtributo(TIPO_GUIA, tipoGuia);


		if (tipoGuia.equals("PRO") || (tipoGuia.equals("PRE"))) {
			incidentes = pesquisarIncidentesProcesso(value);
		} else {
			if (value.toString().length() > 4) { // se digitou algo a mais que o ano
				incidentes = pesquisarIncidentesPeticao(value);
				atualizaSessao();
			}
		}
		if (incidentes != null) {
		   return incidentes.get(0);
		} else {
		   return null;	
		}
	}

	// pesquisa tendo como entrada o processo
	@SuppressWarnings("rawtypes")
	public List pesquisarIncidentesProcesso(Object value) {

		if (value == null) {
			return null;
		}
		String apresentaSigla = "";
		String siglaNumero = value.toString();
		siglaNumero = siglaNumero.replace(".", "").trim();

		if (siglaNumero.trim().length() > 0) {
			char[] caracteres = siglaNumero.toCharArray();
			String sigla = null;
			int i = 0;
			boolean comecouContagem = false;
			int inicioContagem = 0;
			for (; i < caracteres.length; i++) {
				if (Character.isLetter(caracteres[i]) && !comecouContagem) {
					inicioContagem = i;
					comecouContagem = true;
				}
				if (comecouContagem) {
					// if (!Character.isLetter(caracteres[i])) {
					if (Character.isDigit(caracteres[i])) {
						sigla = siglaNumero.substring(inicioContagem, i);
						break;
					} else if (i == caracteres.length - 1) {
						sigla = siglaNumero.substring(inicioContagem, i + 1);
						break;
					}
				}
			}
			if (sigla != null) {
				sigla = sigla.trim();
			}

			String numero = null;
			comecouContagem = false;
			inicioContagem = 0;
			for (; i < caracteres.length; i++) {
				if (Character.isDigit(caracteres[i]) && !comecouContagem) {
					inicioContagem = i;
					comecouContagem = true;
				}
				if (comecouContagem) {
					if (!Character.isDigit(caracteres[i])) {
						numero = siglaNumero.substring(inicioContagem, i);
						break;
					} else if (i == caracteres.length - 1) {
						numero = siglaNumero.substring(inicioContagem, i + 1);
						break;
					}
				}
			}

			if (sigla != null && sigla.trim().length() > 0 && numero != null && numero.trim().length() > 0) {
				Long lNumero = null;
				try {
					lNumero = new Long(numero);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					reportarErro("Número de processo inválido: " + numero);
					return null;
				}

				try {
					apresentaSigla = sigla;
					sigla = converterClasse(sigla);
				} catch (ServiceException e) {
					e.printStackTrace();
					reportarErro("Erro ao converter classe processual: " + apresentaSigla);
				}

				if (sigla == null) {
					reportarAviso("Classe processual não encontrada: " + apresentaSigla);
					return null;
				}

				processo = null;

				try {
					// processo = getProcessoService().recuperarProcesso(sigla, lNumero);
					Processo filtroPesquisa = new Processo();
					filtroPesquisa.setNumeroProcessual(lNumero);
					filtroPesquisa.setSiglaClasseProcessual(sigla);
					if (tipoGuia.equals("PRE")) {
						filtroPesquisa.setTipoMeioProcesso(TipoMeioProcesso.ELETRONICO);
					} else {
						filtroPesquisa.setTipoMeioProcesso(TipoMeioProcesso.FISICO);
					}

					List<Processo> processos = getProcessoService().pesquisarPorExemplo(filtroPesquisa);
					if (processos.size() != 0) {
						processo = processos.get(0);
					}else{
						filtroPesquisa.setMateriaConstitucional(true);
						processos = getProcessoService().pesquisarPorExemplo(filtroPesquisa);
						if (processos.size() != 0) {
							processo = processos.get(0);
						}
					}
				} catch (ProcessoException e) {
					return null;
				} catch (ServiceException e) {
					e.printStackTrace();
					reportarErro("Erro ao recuperar processo: " + apresentaSigla + "/" + lNumero);
					return null;
				}

				setNumeroProcesso(lNumero);
				setSiglaProcesso(sigla);

				if (processo != null) {

					ObjetoIncidenteService objetoIncidenteService = getObjetoIncidenteService();
					List<ObjetoIncidente<?>> incidentes = null;
					try {
						incidentes = objetoIncidenteService.pesquisar(processo.getId(), TipoObjetoIncidente.PROCESSO);
					} catch (ServiceException e) {
						e.printStackTrace();
						reportarErro("Erro ao pesquisar os incidentes do processo: " + processo.getIdentificacao());
					}
					atualizaSessao();
					return incidentes;
				}
			}
		}
		atualizaSessao();
		return null;
	}

	private String converterClasse(String classe) throws ServiceException {
		if (classes == null) {
			classes = new ArrayList<String>();
			List<Classe> classesNova = getClasseService().pesquisar();
			for (Classe cl : classesNova) {
				classes.add(cl.getId());
			}
		}

		for (String cl : classes) {
			if (cl.toUpperCase().equals(classe.toUpperCase())) {
				return cl;
			}
		}

		return null;
	}
	
	// recupera uma petição vinculada a outra: uma petição pode ter além de processo uma outra petição vinculada este método é utilizado para recuperar esse informação na coluna
	// do datatable quando se insere uma petição para deslocamento.
	public String getPeticaoVinculadaPeticao() throws ServiceException {
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();
		String idPeticaoVinculada = "";
		Peticao peticaoVinculada = null;
		if (chkDataTable.getWrappedObject() instanceof Peticao) {
			Peticao peticao = (Peticao) chkDataTable.getWrappedObject();
			if (peticao.getObjetoIncidenteVinculado() != null) {
				peticaoVinculada = getPeticaoService().recuperarPeticao(peticao.getObjetoIncidenteVinculado().getId());
			}
			if ( peticaoVinculada != null ) {
				idPeticaoVinculada = peticaoVinculada.getIdentificacao();
			}
		}
		return idPeticaoVinculada;
	}
	
	
	/**
	 insere um documento (processo ou petição) na listaDocumento que está
	 sendo referenciada na tabela jsf.
	 * 
	 */
	
	public void insereItemDocumento() throws ServiceException {
		try {
			
			if (codigoDestinatario == null || codigoDestinatario == 0) {
				reportarAviso("É necessário informar o destinatário antes de informar o processo.");
				return;
			}
			
			// verifica se setor do usuário pode remeter para o destino
			if(!getPermissaoDeslocamentoService().isDeslocamentoAutorizado(codigoLotacao, codigoDestinatario)){
				reportarAviso("O setor do usuário não tem autorização para deslocar para o destino informado.");
				return;
			}		
			
			// retirar os caracteres errados lidos pela caneta ótica
			idDocumento = filtraCampoProcesso(idDocumento.trim());
			if (idDocumento == null || idDocumento.equals("")) {
				return;
			}
			
			// descobre o tipo da guia
			String tipo = descobrirTipoGuia(idDocumento);
			
			if (tipo == null){
				reportarErro("Processo ou petição inválida: " + idDocumento);
				return;
			}
			
			// não permitir a inserção de documentos não compatíveis com a guia atual
			if (listaDocumento != null && listaDocumento.size() > 0) {
				if (tipo.equals("PRO") && !tipoAnteriorGuia.equals("PRO") ) {
					reportarAviso("Não é possível inserir um Processo Físico em uma guia de " + descricaoGuia);
					return;
				} 
				if (tipo.equals("PRE") && !tipoAnteriorGuia.equals("PRE") ) {
					reportarAviso("Não é possível inserir um Processo Eletrônico em uma guia de " + descricaoGuia);
					return;
				}
				// se é uma tentativa de incluir uma petição física em um guia de outro tipo, então verifica se ela está pendente de digitalização ou é remessa indevida.
				// se for então apresenta a mensagem apropriada se a guia é de petição eletrônica, senão a mensagem é a padrão.
				if (tipo.equals("PET") && !tipoAnteriorGuia.equals("PET") ) {
					Long numPeticao = PeticaoParser.getNumeroPeticao(idDocumento);
					Short anoPeticao = PeticaoParser.getAnoPeticao(idDocumento);
					Peticao petEncontrada = getPeticaoService().recuperarPeticao(numPeticao, anoPeticao);
					if (((petEncontrada.getPendenteDigitalizacao() != null && petEncontrada.getPendenteDigitalizacao()) || 
							(petEncontrada.getRemessaIndevida() != null && petEncontrada.getRemessaIndevida())) && tipoAnteriorGuia.equals("PEE")) {
						reportarAviso("Não é possível inserir uma petição Pendente de Digitalização ou marcada como Remessa Indevida em uma guia de Petição eletrônica.");
						return;
					} else {
						reportarAviso("Não é possível inserir uma Petição Física em uma guia de " + descricaoGuia);
						return;
					}
				}
				if (tipo.equals("PEE") && !tipoAnteriorGuia.equals("PEE") ) {
					reportarAviso("Não é possível inserir uma Petição Eletrônica em uma guia de " + descricaoGuia);
					return;
				}
			}
			
			tipoGuia = tipo;
			
			setAtributo(TIPO_GUIA, tipoGuia);

			if (tipoGuia.equals("PRO")) {
				setDescricaoGuia("Processo Físico");
			} else if (tipoGuia.equals("PRE")) {
				setDescricaoGuia("Processo Eletrônico");
			} else if (tipoGuia.equals("PET")) {
				setDescricaoGuia("Petição Física");
			} else if (tipoGuia.equals("PEE")) {
				setDescricaoGuia("Petição Eletrônica");
			}
			setAtributo(DESCRICAO_GUIA, descricaoGuia);
			
			if ((tipoRemessa.equals("ADV"))) {
				reportarAviso("Para deslocamentos para advogados, favor utilizar a funcionalidade de carga de autos.");
				return;
			}
			
			if (codigoLotacao == null){
				reportarAviso("Origem do deslocamento inválida. Verifique a lotação do usuário.");
				return;
			}
			
			ObjetoIncidente<?> obIncidente = pesquisarIncidentesPrincipal(idDocumento);
			if (obIncidente == null) {
				reportarAviso("Processo ou petição não existe.");
				return;
			}
			objetoIncidente = obIncidente.getId();
			
			ObjetoIncidente<?> objetoInc = getObjetoIncidenteService().recuperarPorId(objetoIncidente);
			if (listaDocumento == null) {
				listaDocumento = Collections.emptyList();
			}

			setTipoAnteriorGuia(tipoGuia);

			if (tipoGuia.equals("PRO") || tipoGuia.equals("PRE")) { // processo (físico ou
																	// eletronico)
				Processo processo = (Processo) objetoInc.getPrincipal();
				if (processo == null) {
					reportarAviso("Processo inexistente.");
					return;
				}
				
				// somente poderá ser deslocado para baixa quando houver autorização getBaixa() == true OU null
/*				if ( codigoDestinatario.equals(COD_SETOR_BAIXA_E_EXPEDICAO) ) {
					
					if (getProcessoService().isBloqueadoBaixa(processo)) {
						reportarAviso("Processo " + processo.getSiglaClasseProcessual() + "/" + processo.getNumeroProcessual() + " está bloqueado para Baixa!");
						return;
					}	
				}
*/				
				// criticar se setor de composição e controle de acórdãos está movimentando para outro setor
				// quando o processo ainda não foi pulicado
				if (codigoLotacao.equals(COD_SETOR_COMPOSICAO_E_CONTROLE_DE_ACORDAOS)) {
					if (!getProcessoPublicadoService().isPublicado(obIncidente)) {
						reportarAviso("Processo sem publicação de acórdão!");
						return;
					}
				}
				DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
				if (deslocaProcesso != null) {
					if (deslocaProcesso.getDataRecebimento() == null) {
						reportarAviso("O processo " + deslocaProcesso.getClasseProcesso() + "/" + deslocaProcesso.getNumeroProcesso() + " está em trânsito.");
						return;
					}
					// verificar se o processo que está sendo incluído não está no setor do usuário 
					Long ultimoSetorDeslocado = deslocaProcesso.getCodigoOrgaoDestino();
					if ( (ultimoSetorDeslocado == null) || (!ultimoSetorDeslocado.equals(codigoLotacao)) ){
						reportarAviso("O processo " + processo.getSiglaClasseProcessual() + "-" + processo.getNumeroProcessual() + " não se encontra no setor do usuário.");
						return;
					}
					// somente critica quando já existe deslocamento anterior, 
					// pois tem que ser possível inserir um primeiro deslocamento com a mesma origem e destino.
					if (codigoLotacao.equals(codigoDestinatario)) {
						reportarAviso("Destino e origem não podem ser iguais.");
						return;
					}
				}

				// veririfica se o processo que está sendo incluído é apenso de outro (principal)
				ProcessoDependencia processoDependencia = getProcessoDependenciaService().getProcessoVinculador(processo); 
				if (processoDependencia != null) {
					reportarAviso("Não é possível deslocar o processo " + 
							processoDependencia.getClasseProcesso() + "-" + 
							processoDependencia.getNumeroProcesso() + ", pois ele está apensado ao processo " +
							processoDependencia.getClasseProcessoVinculador() + "-" + 
							processoDependencia.getNumeroProcessoVinculador() + "."
							);
					return;
				}

				// verifica se o processo já foi incluido
				if (listaDocumento.contains(new CheckableDataTableRowWrapper(processo))) {
					reportarAviso("Processo já incluído à guia atual.");
				} else {
					// Inserir processo na lista.
					CheckableDataTableRowWrapper check = new CheckableDataTableRowWrapper(processo);
					listaDocumento.add(check);
				}

			} else { // petição

				// Crítica do setor do usuário <> do setor atual da petição
				Peticao peticao = getPeticaoService().recuperarPeticao(objetoIncidente);
				if (peticao == null) {
					reportarAviso("Petição inexistente");
				}
				// Crítica de petição em trânsito
				DeslocaPeticao deslocaPeticao = getDeslocamentoPeticaoService().recuperarUltimoDeslocamentoPeticao(peticao);
				if (deslocaPeticao != null) { // neste caso a petição não teve um deslocamento inicial (petição antiga).
					if (deslocaPeticao.getDataRecebimento() == null) {
						reportarAviso("A petição " + peticao.getNumeroPeticao() + "/" + peticao.getAnoPeticao() + " está em trânsito.");
						return;
					}
					Long ultimoSetorDeslocado = deslocaPeticao.getCodigoOrgaoDestino();
					if ( (ultimoSetorDeslocado == null) || (!ultimoSetorDeslocado.equals(codigoLotacao)) ){
						reportarAviso("A petição " + peticao.getNumeroPeticao() + "/" + peticao.getAnoPeticao() + " não encontra-se no setor do usuário.");
						return;
					}
				}
				//
				// determinar se a petição (objeto incidente) já foi incluído
				if (listaDocumento.contains(new CheckableDataTableRowWrapper(objetoInc))) {
					reportarAviso("Petição já adicionada à guia atual.");
				} else {
					CheckableDataTableRowWrapper check = new CheckableDataTableRowWrapper(objetoInc);
					listaDocumento.add(check);
				}
			}
			montarMensagemRestricao();
			atualizaSessao();

		} catch (ServiceException e) {
			reportarErro("Erro ao inserir objeto incidente: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Retirar os caracteres incorretos lidos pela caneta ótica se houver 
	 */
	public String filtraCampoProcesso(Object value) {

		String siglaNumero = null;
		String siglaNumeroF = "";
		Long lNumero = null;
		String lNumeroPet = null;
		
		String codigo = "";
		codigo = value.toString();

		int totalSpaces = codigo.length()-codigo.replaceAll(" ","").length();
		
		// remove o último caracter para resolver o problema do código de barra da etiqueta antiga.
		if (codigo.length() == 14 && totalSpaces == 4){
			siglaNumero = codigo.substring(0, codigo.length()-1); 
		} else {
			siglaNumero = codigo;
		}
		
		if (siglaNumero != null && !siglaNumero.trim().equals("")) {
		    siglaNumero = siglaNumero.replaceAll("\t", "");  
		    siglaNumero = siglaNumero.replaceAll("\n", "");  
		    siglaNumero = siglaNumero.replaceAll(" ", "");
		}
		
		
		if (StringUtils.isNotVazia(siglaNumero)) {
			try {
				String sigla = ProcessoParser.getSigla(siglaNumero);
				
				if (siglaNumero.contains("/")){
					lNumeroPet = ProcessoParser.getNumeroPet(siglaNumero);
				}else{
					lNumero = ProcessoParser.getNumero(siglaNumero);
					
				}

				if (StringUtils.isNotVazia(sigla) || lNumero != null || lNumeroPet != null) {
					if (sigla != null){
						sigla = converterClasse(sigla, classes);
						if (sigla == null) {
							reportarErro("A classe do processo não foi localizada! Favor verificar.");
							return null;
						}
						siglaNumeroF = sigla;
					}
					
					if (lNumeroPet != null){
						siglaNumeroF = siglaNumeroF + lNumeroPet;
					}
					
					if (lNumero != null){
						siglaNumeroF = siglaNumeroF + lNumero;
					}
				}
			}  catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao converter a classe");
			}
		}
		return siglaNumeroF;
	}

	@SuppressWarnings("unchecked")
	public void removerDocumento() {

		@SuppressWarnings("deprecation")
		List<CheckableDataTableRowWrapper> selecionados = retornarItensSelecionados(listaDocumento, true);

		if (selecionados == null || selecionados.size() == 0) {
			reportarAviso("Selecione pelo menos um documento.");
			return;
		}
		listaDocumento.removeAll(selecionados);
		atualizaSessao();
		reportarAviso("Documento(s) removido(s) com sucesso!");
	}

	// recupera os apensos de um determinado processo
	public void recuperarApensos() throws Exception {

		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();
		Processo processo = (Processo) chkDataTable.getWrappedObject();

		List<ProcessoDependencia> apensos = getProcessoDependenciaService().recuperarApensos(processo);

		listaDependencia.clear();
		listaDependencia.addAll(getCheckableDependenciaList(apensos));
		atualizaSessao();
		processo = null;
		apensos = null;
	}

	/**
	 * Obtém uma lista de CheckableDataTableRowWrapper que empacotem os objetos
	 * ProcessoDependencia existentes na lista passada por parâmetro.
	 */
	public List<CheckableDataTableRowWrapper> getCheckableDependenciaList(List<ProcessoDependencia> listaDependentes) {
		return getCheckableDataTableRowWrapperList(listaDependentes);
	}
	
	/*
	 * Lançamento de andamentos
	 * 
	 */
	public void registraAndamentoProcesso(DeslocaProcesso deslocaProcesso, Guia guia, Long codigoAndamento) {
		try {
			AndamentoProcessoInfoImpl andamentoProcessoInfo = new AndamentoProcessoInfoImpl();
			Andamento andamento = getAndamentoService().recuperarPorId(codigoAndamento);
			andamentoProcessoInfo.setAndamento(andamento);
			andamentoProcessoInfo.setCodigoUsuario(getUser().getUsername().toUpperCase());
			Setor setorUsuario = getSetorUsuarioAutenticado();
			String observacao = descDestino + " - Guia " + guia.getNumeroGuia() + "/" + guia.getAnoGuia() + " (Origem: " + setorUsuario.getNome() + ")" ;
			andamentoProcessoInfo.setObservacao(observacao);
			andamentoProcessoInfo.setSetor(setorUsuario);
			Processo processoAndamento = getProcessoService().recuperarProcesso(deslocaProcesso.getClasseProcesso(), deslocaProcesso.getNumeroProcesso());
			
			List<Processo> processosPrincipais = new ArrayList<Processo>();
			processosPrincipais.add(processoAndamento);
			andamentoProcessoInfo.setProcessosPrincipais(processosPrincipais);
			ObjetoIncidente <?>objetoIncidente = getObjetoIncidenteService().recuperarPorId(processoAndamento.getId());
			getAndamentoProcessoService().salvarAndamento(andamentoProcessoInfo, processoAndamento, objetoIncidente);
			
		} catch (ProcessoException e) {
			reportarErro("Não foi possível recuperar o processo para o registro do andamento de autos emprestados");
		} catch (ServiceException e) {
			reportarErro("Não foi possível gerar o andamento de autos emprestados");
		}
		
	}
	
	private boolean temDocumento() {
		return (listaDocumento != null && listaDocumento.size() > 0);
	}
	
	private String validarCamposObrigatorios() {
		if ( codigoDestinatario == null && (temDocumento()) ) {
			return "Favor informar o destino. Destino e processo/petição são informações obrigatórias para o deslocamento.";
		} else if ( codigoDestinatario != null && (!temDocumento()) ) {
			return "Favor informar o processo ou petição. Destino e processo/petição são informações obrigatórias para o deslocamento.";
		} else if ( codigoDestinatario == null && (!temDocumento()) ) {
			return "Favor informar o destino e o processo ou petição. Destino e processo/petição são informações obrigatórias para o deslocamento.";
		} else {
			return "";
		}
	}

	/**
	 * Faz o deslocamento de processos ou petições a partir dos dados obtidos da tabela.
	 * @throws ServiceException 
	 * @throws SQLException 
	 */
	public void salvarDeslocamento() throws ServiceException, SQLException {
		int tipoOrgaoDestino = 0;
		try {
			String msgCritica = validarCamposObrigatorios();
			if (!msgCritica.equals("")) {
				reportarAviso(msgCritica);
				return;
			} else {
				Setor setorOrigem = getSetorService().recuperarPorId(codigoLotacao);
				Setor setorDestino = getSetorService().recuperarPorId(codigoDestinatario);
				if (setorOrigem == null && setorDestino == null) {
					reportarAviso("Um setor do STF deve ser a origem ou destino do deslocamento.");
					return;
				}
				
				// limpar da lista os apensos porque o método inserirDeslocamento já desloca os apensos se houver.
				List<CheckableDataTableRowWrapper> listaParaDeslocamento = new ArrayList<CheckableDataTableRowWrapper>(listaDocumento);
				listaParaDeslocamento.removeAll(listaDependenciaFinal);
				
				// gerar coleção de seq_objeto_incidente a ser deslocada para o parâmetro do método
				ArrayList<Long> arrayObjIncidente = new ArrayList<Long>();
				Iterator<CheckableDataTableRowWrapper> iLista = listaParaDeslocamento.iterator();
				while (iLista.hasNext()) {
					Object objeto = ((CheckableDataTableRowWrapper) iLista.next()).getWrappedObject();
					if (objeto instanceof Processo) {
						arrayObjIncidente.add(((Processo) objeto).getId());
					} else {
						arrayObjIncidente.add(((ObjetoIncidente<?>) objeto).getId());
					}
				}
				// Tipo de órgão de destino. Domínio: 2- Interno, 3- Externo.
				if (tipoRemessa.equals("SET")) {
					tipoOrgaoDestino = 2;
				} else if (tipoRemessa.equals("ORG")) {
					tipoOrgaoDestino = 3;
				} else {
					reportarAviso("O tipo do destino deve ser um setor do STF ou um órgão externo.");
					return;
				}
				Guia guia = new Guia();
				GuiaId guiaId = new GuiaId();
				guiaId.setCodigoOrgaoOrigem(codigoLotacao);
				guia.setId(guiaId);
				guia.setTipoOrgaoOrigem(2);
				guia.setCodigoOrgaoDestino(codigoDestinatario);
				guia.setTipoOrgaoDestino(tipoOrgaoDestino);
				guia.setObservacao(observacao);
				
				if (seqEndereco != null && seqEndereco != 0) {
					EnderecoDestinatario endereco = getEnderecoDestinatarioService().recuperarPorId(seqEndereco);
					guia.setEnderecoDestinatario(endereco);
				}
				
				ObjetoIncidenteService objetoIncidenteService = getObjetoIncidenteService();
				String numAnoGuia = "";
				boolean comRecebimento = false;
				// faz deslocamento SEM recebimento quando for destino interno ao STF 
				if (tipoRemessa.equals("SET")) { 
					comRecebimento = codigoLotacao.equals(codigoDestinatario); //ou COM recebimento se for um deslocamento inicial origem = destino 
				} else { // faz deslocamento com recebimento quando for destino externo ao STF
					comRecebimento = true;
				}
				numAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, arrayObjIncidente, comRecebimento);
				if (numAnoGuia == null) {
					throw new ServiceException("Erro ao efetuar o deslocamento");
				}
				String numero_guia = numAnoGuia.substring(0, numAnoGuia.indexOf("/"));
				String ano_guia = numAnoGuia.substring(numAnoGuia.indexOf("/") + 1, numAnoGuia.length());
				numGuia = numero_guia;
				anoGuia = ano_guia;
				setAtributo(NUM_GUIA, numGuia);
				setAtributo(ANO_GUIA, anoGuia);
				sucesso = "S";
				setAtributo(SUCESSO, sucesso);
				reportarInformacao("Deslocamento Efetuado! Guia " + numero_guia + "/" + ano_guia + " gerada com sucesso.");
				setVisualizarGuia(true);
				lancaConclusoAoRelator(setorOrigem, setorDestino);
				//lancaBaixaAutorizada
			}
		} catch (ServiceException e) {
			sucesso = "N";
			setAtributo(SUCESSO, sucesso);
			reportarErro("Não foi possível deslocar. " + ExceptionUtils.getMessage(e));
			e.printStackTrace();
		}

	}
	
	
	public void lancaConclusoAoRelator(Setor setorOrigem, Setor setorDestino) {
		// tratar a questão do andamento 8203 - Concluso ao relator
		if (!tipoGuia.equals("PRO") && !tipoGuia.equals("PRE")) {return;} // somente processos 
		if (!setorOrigem.getFlagDistribuicao()) {return;} // somente setores que distribui
		try {
			// regra válida somente quando o destino for gabinete
			if (!getSetorService().isSetorGabinete(setorDestino)) {return;}
			
			// recuperar o ID da guia
			GuiaId guiaId = new GuiaId();
			guiaId.setAnoGuia(new Short(anoGuia));
			guiaId.setNumeroGuia(new Long(numGuia));
			guiaId.setCodigoOrgaoOrigem(codigoLotacao);
			// recuperar a guia deslocada
			Guia guia = getGuiaService().recuperarPorId(guiaId);
			
			List<DeslocaProcesso> deslocaProcessos = getDeslocaProcessoService().recuperarDeslocamentoProcessos(guia);
			for (DeslocaProcesso dp : deslocaProcessos) {

				Processo processoDeslocado = dp.getId().getProcesso();
				
				// verifica se o processo deslocado possui como último andamento um dos seguintes andamentos: 7501, 7502, 7503, 7504, 7505
				AndamentoProcesso andamentoProcesso = getAndamentoProcessoService().recuperarUltimoAndamento(processoDeslocado);
				if (andamentoProcesso.getCodigoAndamento().equals(new Long("7501")) ||
					andamentoProcesso.getCodigoAndamento().equals(new Long("7502")) ||
					andamentoProcesso.getCodigoAndamento().equals(new Long("7503")) ||
					andamentoProcesso.getCodigoAndamento().equals(new Long("7504")) ||
					andamentoProcesso.getCodigoAndamento().equals(new Long("7505")) ||
					andamentoProcesso.getCodigoAndamento().equals(new Long("7800"))) {
					if (!getProcessoDependenciaService().isApenso(processoDeslocado)) {
							// lançar 8203 - Concluso ao(à) Relator(a) 
						registraAndamentoProcesso(dp, guia, new Long("8203"));
					}
				}
			}
			
		} catch (Exception e) {
			reportarErro("Não foi possível verificar ou lançar andamentos relacionados ao deslocamento.");
		}

	}
	

	public void limpaSessao() {
		seqEndereco = null;
		numGuia = "";
		anoGuia = "";
		codigoDestinatario = null;
		listaDocumento = null;
		listaDependencia = null;
		observacao = null;
		tipoGuia = "PRO";
		descricaoGuia = "Processo Físico";
		tipoRemessa = "SET";
		descricaoDestino = "Setor do STF";
		nomDestinatario = "";
		sucesso = "N";
		atualizaSessao();

	}

	private ByteArrayInputStream gerarPDFGuia(){
		List<Guia> guias = new ArrayList<Guia>();
		try {
			byte[] arquivo = null;
			if (postal == null) {
				postal = false;
			}
			if (tipoGuia.equals("PRO") || tipoGuia.equals("PRE")) {
				arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDeslocamentoProcesso(new Long(numGuia), new Short(anoGuia),
						new Long(codigoLotacao), postal);
			} else {
				Guia guia = new Guia();
				guia.setId(new GuiaId());
				guia.getId().setAnoGuia(new Short(anoGuia));
				guia.getId().setNumeroGuia(new Long(numGuia));
				guia.getId().setCodigoOrgaoOrigem(new Long(codigoLotacao));
				guias.add(guia);
				arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDeslocamentoPeticao(guias);
			}

			return new ByteArrayInputStream(arquivo);
			
		} catch (ServiceException e) {
			reportarErro("Erro ao gerar PDF da guia: " + e.getMessage());
		}
		return null;
		
	}
	
	public String imprimirGuia() {
		mandarRespostaDeDownloadDoArquivo(gerarPDFGuia());
		return null;
	}
	
	private void mandarRespostaDeDownloadDoArquivo(ByteArrayInputStream input) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-disposition", "attachment; filename=\"GuiaDeDeslocamentoJudiciario.pdf\"");
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
	
	public void gerarPDFGuiaInLine(){
		List<Guia> guias = new ArrayList<Guia>();
		try {
			String arquivo = "";
			if (postal == null) {
				postal = false;
			}
			if (tipoGuia.equals("PRO") || tipoGuia.equals("PRE")) {
				arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDeslocamentoProcessoNaPastaTemp(new Long(numGuia), new Short(anoGuia),
						new Long(codigoLotacao), postal);
			} else {
				Guia guia = new Guia();
				guia.setId(new GuiaId());
				guia.getId().setAnoGuia(new Short(anoGuia));
				guia.getId().setNumeroGuia(new Long(numGuia));
				guia.getId().setCodigoOrgaoOrigem(new Long(codigoLotacao));
				guias.add(guia);
				arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDeslocamentoPeticaoNaPastaTemp(guias);
			}
			mandarRespostaInline(arquivo);
		} catch (Exception e) {
			reportarErro("Erro ao gerar PDF da guia: " + e.getMessage());
		}
	}
	
	private void mandarRespostaInline(String nomePdf) throws ServiceException,  IOException {
		InputStream is = null;
		OutputStream os = null;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=" + nomePdf);

		try {
			is = new FileInputStream(nomePdf);
			response.setHeader("Content-Length", String.valueOf(is.available()));
			os = response.getOutputStream();
			IOUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
			File fileToDelete = new File(nomePdf);
			fileToDelete.delete();
		}
		facesContext.responseComplete();
	}

	public boolean getHabilitarPostal() {
		if (tipoRemessa.equals("SET")) {
			return true;
		} else {
			return false;
		}
	}
	
	// diz a interface se os compontentes podem ser habilitados ou não (quando o deslocamento finalizar)
	// pois se o deslocamento foi efetuado o usuário não poderá editá-lo, apenas será possível clicar em 'novo deslocamento'
	public boolean getHabilitaAdicao(){
		if(sucesso != null && sucesso.equals("S")){
			return true;
		}
		return false;
	}
	
	// -- crítica do setor destino diferente do setor do relator 
	public boolean isDestinoDiferenteRelator(Processo processo) throws ServiceException {
		Ministro ministro = processo.getMinistroRelatorAtual();
		//ObjetoIncidente<?> incidente = processo.getPrincipal();
		//Ministro ministro = incidente.getRelatorIncidente();
		if (ministro == null) {
			return false;
		}
		Setor setor = ministro.getSetor();
		
		Setor setorOrigem = getSetorService().recuperarPorId(codigoDestinatario);
		
		// a crítica é válida somente para os destinos Gabinetes de Ministros
		if (getSetorService().isSetorGabinete(setorOrigem)) {
			if (!setor.getId().equals(codigoDestinatario)) {
				return true;
			}
		} 
		return false;
	}
	
	// para realização de teste
	public void validarDestinoDiferenteRelatorTest() {
    	setDestinoDiferenteRelator("N");
    	return;
	}
	
	// verifica se entre os processos adicionados algum possui o setor do relator diferente do destino da guia.
	public void validarDestinoDiferenteRelator() throws ServiceException {
		
    	setDestinoDiferenteRelator("N");
    	setProcessosCriticaRelator("");
    	
		if (codigoDestinatario == null) {return;}
		if (listaDocumento == null) {return;}
		if (listaDocumento.isEmpty()) {return;}
		if (tipoGuia.equals("PEE")) {return;}
		if (tipoGuia.equals("PET")) {return;}
		if (!tipoRemessa.equals("SET")) {return;}
		Iterator<?> iLista = listaDocumento.iterator();
		while (iLista.hasNext()) {
			Object objeto = ((CheckableDataTableRowWrapper) iLista.next()).getWrappedObject();
			if (objeto instanceof Processo) {
			    if (isDestinoDiferenteRelator((Processo) objeto)) {
			    	setDestinoDiferenteRelator("S");
			    	Processo ProcessoCriticado = (Processo) objeto;
			    	setProcessosCriticaRelator(processosCriticaRelator + ProcessoCriticado.getSiglaClasseProcessual() + "/" + ProcessoCriticado.getNumeroProcessual() + ",");
			    }
			}
		}
		// no jsp o caractere ',' será substituído pelo \n para ocorrer a quebra de linha
		if (!processosCriticaRelator.equals("")) {
			setProcessosCriticaRelator("," + processosCriticaRelator);
		}
		atualizaSessao();
    }
	
	private void tratarMensagemConfirmacao() {
		if (COD_SETOR_BAIXA_E_EXPEDICAO.equals(codigoDestinatario)) {
			setMensagemConfirmacao("Confirma o deslocamento para Baixa?");
		} else if (COD_SETOR_ARQUIVO.equals(codigoDestinatario)) {
			setMensagemConfirmacao("Conforma o deslocamento para o Arquivo?");
		} else if (tipoRemessa.equals("ORG")) {
			setMensagemConfirmacao("Conforma o deslocamento para a Origem?");
		} else {
			setMensagemConfirmacao("Conforma o deslocamento?");
		}
	}
	
	// verifica para os processos eletrônicos se as petições vinculadas estão pendentes de tratamento
	public void validarProcessosComPeticoesPendentes() throws ServiceException{
    	setProcessosCriticaPetPendente("");
    	setPeticoesPendentesDeTratamento("N");
		if (codigoDestinatario == null) {return;}
		if (listaDocumento == null) {return;}
		if (listaDocumento.isEmpty()) {return;}
		if (!tipoGuia.equals("PRE")) {return;}

		//if ( !codigoDestinatario.equals(COD_SETOR_BAIXA_E_EXPEDICAO) && (!codigoLotacao.equals(COD_SETOR_BAIXA_E_EXPEDICAO) && !tipoRemessa.equals("ORG")) ){
		//	return;
		//}
		
		if ( (codigoDestinatario.equals(COD_SETOR_BAIXA_E_EXPEDICAO)) || (codigoLotacao.equals(COD_SETOR_BAIXA_E_EXPEDICAO) && tipoRemessa.equals("ORG")) ) {
			Iterator<CheckableDataTableRowWrapper> iLista = listaDocumento.iterator();
			while (iLista.hasNext()) {
				Object objeto = ((CheckableDataTableRowWrapper) iLista.next()).getWrappedObject();
				if (objeto instanceof Processo) {
					Processo processo = ((Processo) objeto).getPrincipal();
					if (getPeticaoSetorService().isPeticaoPendenteTratamento(processo)) {
						setPeticoesPendentesDeTratamento("S");
				    	Processo ProcessoCriticado = (Processo) objeto;
				    	setProcessosCriticaPetPendente(processosCriticaPetPendente + ProcessoCriticado.getSiglaClasseProcessual() + "/" + ProcessoCriticado.getNumeroProcessual() + ",");
					}
				}
			}
			// no jsp o caracter ',' será substituído pelo \n para ocorrer a quebra de linha
			if (!processosCriticaPetPendente.equals("")) {
				tratarMensagemConfirmacao();
				setProcessosCriticaPetPendente("," + processosCriticaPetPendente);
			}
			atualizaSessao();
		}
	}
	
	// verifica se existem petições que não foi juntada ao processo físico
	public void validarProcessosSemJuntadaPeticao() throws ServiceException{
    	setProcessosCriticaNaoJuntadoPet("");
    	setPeticoesNaoJuntadas("N");
		if (codigoDestinatario == null) {return;}
		if (listaDocumento == null) {return;}
		if (listaDocumento.isEmpty()) {return;}
		if (!tipoGuia.equals("PRO")) {return;}

//		if ( !codigoDestinatario.equals(COD_SETOR_BAIXA_E_EXPEDICAO) && (!codigoLotacao.equals(COD_SETOR_BAIXA_E_EXPEDICAO) && !tipoRemessa.equals("ORG")) ){
//			return;
//		}
		
		if ( (codigoDestinatario.equals(COD_SETOR_BAIXA_E_EXPEDICAO)) || (codigoLotacao.equals(COD_SETOR_BAIXA_E_EXPEDICAO) && tipoRemessa.equals("ORG")) ) {
			Iterator<CheckableDataTableRowWrapper> iLista = listaDocumento.iterator();
			while (iLista.hasNext()) {
				Object objeto = ((CheckableDataTableRowWrapper) iLista.next()).getWrappedObject();
				if (objeto instanceof Processo) {
					// andamentos de juntada à petição
					long[] codigoAndamento = {8246, 8001, 8003, 8004, 8005, 8006};
					Processo processo = ((Processo) objeto).getPrincipal();
					boolean existeAndamentos = false;
					for (int i=0; i<=5; i++) {
						if (getAndamentoProcessoService().verificaAndamentoProcesso(processo.getSiglaClasseProcessual(), processo.getNumeroProcessual(), codigoAndamento[i])) {
							existeAndamentos = true;
						}
					}
					if (!existeAndamentos) {
				    	setPeticoesNaoJuntadas("S");
				    	Processo ProcessoCriticado = (Processo) objeto;
				    	setProcessosCriticaNaoJuntadoPet(processosCriticaNaoJuntadoPet + ProcessoCriticado.getSiglaClasseProcessual() + "/" + ProcessoCriticado.getNumeroProcessual() + ",");
					}
				}
			}
			// no jsp o ',' será substituído pelo \n para ocorrer a quebra de linha
			if (!processosCriticaNaoJuntadoPet.equals("")) {
				tratarMensagemConfirmacao();
				setProcessosCriticaNaoJuntadoPet("," + processosCriticaNaoJuntadoPet);
			}
			atualizaSessao();
		}
	}
	
	
	// permiter ativar ou desativar (no caso de apensos) o icone para exclusão.
	// isso é necessário, pois a exclusão da linha na tabela se dará através do processo principal
	public boolean getHabilitarExclusao() {
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();
		Processo processo = (Processo) chkDataTable.getWrappedObject();
		for (int i=0; i<=listaDependencia.size()-1; i++) {
			
			Processo apenso = (Processo) listaDependencia.get(i).getWrappedObject();
			
			if (processo.getSiglaClasseProcessual().equals(apenso.getSiglaClasseProcessual()) && 
				processo.getNumeroProcessual().equals(apenso.getNumeroProcessual())) {
				return true;
			}
		}
		return false;
	}
	
	
	public void removerItem() throws ServiceException {

		// recuperar o processo principal
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();
		if (!tipoGuia.equals("PET") && !tipoGuia.equals("PEE")) {
			Processo processo = (Processo) chkDataTable.getWrappedObject();
			
			// recuperar os apensos
			List<ProcessoDependencia> apensos = getProcessoDependenciaService().recuperarApensos(processo);
			List<Processo> processos = new ArrayList<Processo>();
			// transformar todos os apensos no tipo processo para inserir na lista 
			for (ProcessoDependencia apenso : apensos) {
			    Processo processoApenso = getProcessoService().recuperarPorId(apenso.getIdObjetoIncidente());
			    processos.add(processoApenso);
			}
			// se existir apnesos na tabela removê-los também
			if (processos.size() > 0 ){
				listaDocumento.removeAll(getCheckableDataTableRowWrapperList(processos));
			}
		}
		//CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaDocumentos.getRowData();
		// remover o processo principal
		listaDocumento.remove(chkDataTable);
		atualizaSessao();

	}

	private EnderecoDestinatario atualizaPanelEndereco() {
		EnderecoDestinatario enderecoSelecionado = (EnderecoDestinatario) tabelaEnderecos.getRowData();
		setPanelHeader("Detalhe do Destinatário: " + enderecoSelecionado.getDestinatario().getNomDestinatario());
		setPanelLogradouro(enderecoSelecionado.getLogradouro());
		setPanelNumLocalizacao(enderecoSelecionado.getNumeroLocalizacao());
		setPanelBairro(enderecoSelecionado.getBairro());
		setPanelCep(enderecoSelecionado.getCep());
		setPanelMunicipio(enderecoSelecionado.getMunicipio());
		setPanelUf(enderecoSelecionado.getUf());
		atualizaSessao();
		return enderecoSelecionado;
	}
	
	
	public void importarListaExportadaPeloEGab(ActionEvent evt){		
		try {
			listaProcessoImportacao = getObjetoIncidenteService().pesquisarListaImportacaoUsuario(getUser().getUsername().toUpperCase());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}	
	
	public Integer getQuantidadeListaExportadaPeloEGab(){
		if(listaProcessoImportacao != null){
			return listaProcessoImportacao.size();
		}
		return 0;
	}
	
	public void processarImportacaoListaExportadaPeloEGab(ActionEvent evt){		
		try {		
			if (codigoDestinatario == null || codigoDestinatario == 0) {
				reportarAviso("É necessário informar o destinatário antes de informar o processo.");
				return;
			}
			listaProcessoImportacao = getObjetoIncidenteService().pesquisarListaImportacaoUsuario(getUser().getUsername().toUpperCase());
			if(listaProcessoImportacao != null && !listaProcessoImportacao.isEmpty()){
				for(ObjetoIncidente oi : listaProcessoImportacao){
					if(oi instanceof Processo){
						idDocumento = ((Processo) oi).getSiglaClasseProcessual().concat(" ").concat(((Processo) oi).getNumeroProcessual().toString());
						insereItemDocumento();
					}
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}	
	
	// ----------------------- ACTIONS ----------------------------------

	// recupera o endereço selecionado pelo link na pagina e o atualiza na sessao.
	public void selecionaEnderecoAction(ActionEvent evt) {

		EnderecoDestinatario enderecoSelecionado = atualizaPanelEndereco();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Detalhe do destinatário: ");
		strBuilder.append(enderecoSelecionado.getDestinatario().getNomDestinatario());
		strBuilder.append(": <br/>");
		if (enderecoSelecionado.getLogradouro() != null ) {
			strBuilder.append(enderecoSelecionado.getLogradouro());
		}
		if (enderecoSelecionado.getNumeroLocalizacao() != null) {
			strBuilder.append(" - N° ");
			strBuilder.append(enderecoSelecionado.getNumeroLocalizacao());
		}
		if (enderecoSelecionado.getBairro() != null) {
			strBuilder.append(" <br/> ");
			strBuilder.append(enderecoSelecionado.getBairro());
		}
		if (enderecoSelecionado.getComplemento() != null) {
			strBuilder.append(" <br/> ");
			strBuilder.append(enderecoSelecionado.getComplemento());
		}
		if (enderecoSelecionado.getMunicipio() != null) {
			strBuilder.append(" <br/> ");
			strBuilder.append(enderecoSelecionado.getMunicipio());
		}
		if (enderecoSelecionado.getUf() != null) {
			strBuilder.append(" - ");
			strBuilder.append(enderecoSelecionado.getUf());
		}
		if (enderecoSelecionado.getCep() != null) {
			strBuilder.append("<br/> CEP ");
			strBuilder.append(enderecoSelecionado.getCep());
		}
		
		setEnderecoRemessa(strBuilder.toString());
		setSeqEndereco(enderecoSelecionado.getId());
		setAtributo(SEQ_ENDERECO, seqEndereco); 
		setAtributo(ENDERECO_REMESSA, enderecoRemessa);
		limparPopupEndereco();
	}
	
	public void limparPopupEndereco(){
		// limpa os atributos para a próxima pesquisa (se houver)
		setCodigoDestinatarioBaixaExpedicao(null);
		setNomDestinatarioBaixaExpedicao(null);
		setListaEnderecos(null);
		setNaoEncontrouEndereco(false);
		
		//atualizaSessao();
		setAtributo(CODIGO_DESTINATARIO_BAIXA_EXPEDICAO, codigoDestinatarioBaixaExpedicao);
		setAtributo(NOM_DESTINATARIO_BAIXA_EXPEDICAO, nomDestinatarioBaixaExpedicao);
		setAtributo(LISTA_ENDERECOS, listaEnderecos);
		setAtributo(NAO_ENCONTROU_ENDERECO, naoEncontrouEndereco);
		
	}
	
	public Boolean getMostraMensagemEnderecoNaoLocalizado() {
		if (codigoDestinatarioBaixaExpedicao != null && listaEnderecos == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public void limparPopupEnderecoAction(ActionEvent evt){
		limparPopupEndereco();
	}
	
	public void removerItemAction(ActionEvent evt) throws Exception {
		removerItem();
	}
	
	public void recuperarApensosAction(ActionEvent evt) throws Exception {
		recuperarApensos();
	}

	public void salvarDeslocamentoAction(ActionEvent evt) throws Exception {
		salvarDeslocamento();
	}

	public void atualizarMarcacao(ActionEvent evt) {
		setListaDocumento(listaDocumento);
	}

	public void excluirDocumentosSelecionadosAction(ActionEvent evt) {
		removerDocumento();
		atualizaSessao();
	}

	public void marcarTodosDocumentos(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaDocumento);
		setListaDocumento(listaDocumento);
	}

	public void limpaSessaoAction(ActionEvent evt) {
		limpaSessao();
	}

	public void imprimirGuiaAction(ActionEvent evt) {
		imprimirGuia();
	}
	
	public void atualizaTipoDocumentoAction(ActionEvent evt) {
		atualizaSessao();
	}
	
	public void atualizarAction(ActionEvent evt) {
		atualizaSessao();
	}
	
	public void validarAction(ActionEvent evt) throws ServiceException {
		validarDestinoDiferenteRelator();
		validarProcessosComPeticoesPendentes();
		validarProcessosSemJuntadaPeticao();
	}
	
	public void insereItemDocumentoAction(ActionEvent evt) throws ServiceException {
		insereItemDocumento();
	}
	
	private void checkParamFrame() {
		String seqObjetoIncidente = getRequestParamFrame("seqObjetoIncidente");
		if (seqObjetoIncidente != null) {
			try {
				setObjetoIncidente(Long.parseLong(seqObjetoIncidente));
			} catch (NumberFormatException e) {
				reportarErro("Objeto incidente inválido!", e.getMessage());
			}
		}
	}

	// --------------------------- GET AND SET --------------------------

	public org.richfaces.component.html.HtmlDataTable getTabelaDocumentos() {
		return tabelaDocumentos;
	}

	public void setTabelaDocumentos(org.richfaces.component.html.HtmlDataTable tabelaDocumentos) {
		this.tabelaDocumentos = tabelaDocumentos;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaDependencias() {
		return tabelaDependencias;
	}

	public void setTabelaDependencias(org.richfaces.component.html.HtmlDataTable tabelaDependencias) {
		this.tabelaDependencias = tabelaDependencias;
	}

	public String getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
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

	public String getNomDestinatario() {
		return nomDestinatario;
	}

	public void setNomDestinatario(String nomDestinatario) {
		setAtributo(NOME_DESTINATARIO, nomDestinatario);
		this.nomDestinatario = nomDestinatario;
	}

	public Long getCodigoDestinatario() {
		return codigoDestinatario;
	}

	public void setCodigoDestinatario(Long codigoDestinatario) {
//		if ((codigoLotacao != null) && (codigoDestinatario.equals(codigoLotacao))) {
//			reportarAviso("Não é possível deslocar com o destino igual a origem.");
			// iguala o destino com a lotação para a crítica do salvar impedir
//			this.codigoDestinatario = codigoLotacao;
//			setAtributo(CODIGO_DESTINATARIO, codigoLotacao);
//		} else {
			this.codigoDestinatario = codigoDestinatario;
			setAtributo(CODIGO_DESTINATARIO, codigoDestinatario);
//		}
		if(getAtributo(REMETER_INCIDENTE_TRANS_BEAN) != null){
			atualizaSessao();
		}
		
	}

	public List<CheckableDataTableRowWrapper> getListaDependencia() {
		return listaDependencia;
	}

	public void setListaDependencia(List<CheckableDataTableRowWrapper> listaDependencia) {
		setAtributo(KEY_LISTA_DEPENDENCIAS, listaDependencia);
		this.listaDependencia = listaDependencia;
	}

	public List<CheckableDataTableRowWrapper> getListaDocumento() {
		return listaDocumento;
	}

	public void setListaDocumento(List<CheckableDataTableRowWrapper> listaDocumento) {
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumento);
		this.listaDocumento = listaDocumento;
	}

	public void setTipoRemessa(String tipoRemessa) {
		setAtributo(TIPO_REMESSA, tipoRemessa);
		this.tipoRemessa = tipoRemessa;
	}

	public String getTipoRemessa() {
		return this.tipoRemessa;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getObservacao() {
		return this.observacao;
	}

	public void setTipoGuia(String tipoGuia) {
		this.tipoGuia = tipoGuia;
	}

	public String getTipoGuia() {
		return this.tipoGuia;
	}

	public void setNomRelator(String nomRelator) {
		this.nomRelator = nomRelator;
	}

	public String getNomRelator() {
		return this.nomRelator;
	}

	public void setNumGuia(String numGuia) {
		this.numGuia = numGuia;
	}

	public String getNumGuia() {
		return this.numGuia;
	}

	public String getAnoGuia() {
		return anoGuia;
	}

	public void setAnoGuia(String anoGuia) {
		this.anoGuia = anoGuia;
	}

	public void setDescricaoGuia(String descricaoGuia) {
		this.descricaoGuia = descricaoGuia;
	}

	public String getDescricaoGuia() {
		return descricaoGuia;
	}

	public void setDescricaoDestino(String descricaoDestino) {
		this.descricaoDestino = descricaoDestino;
	}

	public String getDescricaoDestino() {
		return descricaoDestino;
	}

	public void setTipoAnteriorGuia(String tipoAnteriorGuia) {
		this.tipoAnteriorGuia = tipoAnteriorGuia;
	}

	public String getTipoAnteriorGuia() {
		return tipoAnteriorGuia;
	}

	public void setCodigoLotacao(Long codigoLotacao) {
		this.codigoLotacao = codigoLotacao;
	}

	public Long getCodigoLotacao() {
		return codigoLotacao;
	}

	public void setPostal(Boolean postal) {
		this.postal = postal;
	}

	public Boolean getPostal() {
		return postal;
	}

	public String getDestinoDiferenteRelator() {
		return destinoDiferenteRelator;
	}

	public void setDestinoDiferenteRelator(String destinoDiferenteRelator) {
		this.destinoDiferenteRelator = destinoDiferenteRelator;
	}

	public void setSucesso(String sucesso) {
		this.sucesso = sucesso;
	}

	public String getSucesso() {
		return sucesso;
	}

	public void setHabilitaDesabilita(Boolean habilitaDesabilita) {
		this.habilitaDesabilita = habilitaDesabilita;
	}

	public Boolean getHabilitaDesabilita() {
		return habilitaDesabilita;
	}

	public void setVisualizarGuia(Boolean visualizarGuia) {
		this.visualizarGuia = visualizarGuia;
	}

	public Boolean getVisualizarGuia() {
		return visualizarGuia;
	}

	public void setTipoDestino(Short tipoDestino) {
		if (tipoDestino == 1) {
			setTipoRemessa("ADV");
		} else if (tipoDestino == 2) {
			setTipoRemessa("SET");
		} else {
			setTipoRemessa("ORG");
		}
		this.tipoDestino = tipoDestino;
	}

	public Short getTipoDestino() {
		return tipoDestino;
	}
	
	public String getProcessosCriticaRelator() {
		return processosCriticaRelator;
	}

	public void setProcessosCriticaRelator(String processosCriticaRelator) {
		this.processosCriticaRelator = processosCriticaRelator;
	}

	public String getDescDestino() {
		return descDestino;
	}

	public void setDescDestino(String descDestino) {
		this.descDestino = descDestino;
		setAtributo(DESC_DESTINO, descDestino);
	}

	public void setListaControleIcones(List<String> listaControleIcones) {
		this.listaControleIcones = listaControleIcones;
	}

	public List<String> getListaControleIcones() {
		return listaControleIcones;
	}
	
	public String getRowClasses() throws ServiceException {
		StringBuilder rowClasses = new StringBuilder();
		
		Iterator<CheckableDataTableRowWrapper> ite = listaDocumento.iterator();
		
		while(ite.hasNext()) {
			Processo processo = (Processo) ite.next().getWrappedObject();
			if (!getProcessoDependenciaService().isApenso(processo)) {
				rowClasses.append("DataTableRow");
			} else {
				rowClasses.append("DataTableRowInativa");
			}
			if (ite.hasNext()) {
				rowClasses.append(",");
			}
		}
		
		return rowClasses.toString();
	}

	public List<CheckableDataTableRowWrapper> getListaDependenciaFinal() {
		return listaDependenciaFinal;
	}

	public void setListaDependenciaFinal(
			List<CheckableDataTableRowWrapper> listaDependenciaFinal) {
		this.listaDependenciaFinal = listaDependenciaFinal;
	}

	public void setEnderecoRemessa(String enderecoRemessa) {
		this.enderecoRemessa = enderecoRemessa;
	}

	public String getEnderecoRemessa() {
		return enderecoRemessa;
	}

	public void setCodigoDestinatarioBaixaExpedicao(Long codigoDestinatarioBaixaExpedicao) {
		this.codigoDestinatarioBaixaExpedicao = codigoDestinatarioBaixaExpedicao;
		setAtributo(CODIGO_DESTINATARIO_BAIXA_EXPEDICAO, codigoDestinatarioBaixaExpedicao);
	}

	public Long getCodigoDestinatarioBaixaExpedicao() {
		return codigoDestinatarioBaixaExpedicao;
	}

	public void setNomDestinatarioBaixaExpedicao(String nomDestinatarioBaixaExpedicao) {
		this.nomDestinatarioBaixaExpedicao = nomDestinatarioBaixaExpedicao;
		if (nomDestinatarioBaixaExpedicao != null) {
			setAtributo(NOM_DESTINATARIO_BAIXA_EXPEDICAO, nomDestinatarioBaixaExpedicao);
		}
	}

	public String getNomDestinatarioBaixaExpedicao() {
		return nomDestinatarioBaixaExpedicao;
	}

	public void setListaEnderecos(List<EnderecoDestinatario> listaEnderecos) {
		this.listaEnderecos = listaEnderecos;
	}

	public List<EnderecoDestinatario> getListaEnderecos() {
		return listaEnderecos;
	}

	public void setTabelaEnderecos(org.richfaces.component.html.HtmlDataTable tabelaEnderecos) {
		this.tabelaEnderecos = tabelaEnderecos;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaEnderecos() {
		return tabelaEnderecos;
	}

	public void setSeqEndereco(Long seqEndereco) {
		this.seqEndereco = seqEndereco;
	}

	public Long getSeqEndereco() {
		return seqEndereco;
	}

	public void setPanelHeader(String panelHeader) {
		this.panelHeader = panelHeader;
	}

	public String getPanelHeader() {
		return panelHeader;
	}

	public void setPanelLogradouro(String panelLogradouro) {
		this.panelLogradouro = panelLogradouro;
	}

	public String getPanelLogradouro() {
		return panelLogradouro;
	}
	public String getPanelNumLocalizacao() {
		return panelNumLocalizacao;
	}

	public void setPanelNumLocalizacao(String panelNumLocalizacao) {
		this.panelNumLocalizacao = panelNumLocalizacao;
	}

	public String getPanelBairro() {
		return panelBairro;
	}

	public void setPanelBairro(String panelBairro) {
		this.panelBairro = panelBairro;
	}

	public String getPanelMunicipio() {
		return panelMunicipio;
	}

	public void setPanelMunicipio(String panelMunicipio) {
		this.panelMunicipio = panelMunicipio;
	}

	public String getPanelUf() {
		return panelUf;
	}

	public void setPanelUf(String panelUf) {
		this.panelUf = panelUf;
	}

	public Integer getPanelCep() {
		return panelCep;
	}

	public void setPanelCep(Integer panelCep) {
		this.panelCep = panelCep;
	}

	public void setNaoEncontrouEndereco(Boolean naoEncontrouEndereco) {
		this.naoEncontrouEndereco = naoEncontrouEndereco;
	}

	public Boolean getNaoEncontrouEndereco() {
		return naoEncontrouEndereco;
	}

	public String getPeticoesPendentesDeTratamento() {
		return peticoesPendentesDeTratamento;
	}

	public void setPeticoesPendentesDeTratamento(
			String peticoesPendentesDeTratamento) {
		this.peticoesPendentesDeTratamento = peticoesPendentesDeTratamento;
	}

	public String getPeticoesNaoJuntadas() {
		return peticoesNaoJuntadas;
	}

	public void setPeticoesNaoJuntadas(String peticoesNaoJuntadas) {
		this.peticoesNaoJuntadas = peticoesNaoJuntadas;
	}

	public String getMensagemConfirmacao() {
		return mensagemConfirmacao;
	}

	public void setMensagemConfirmacao(String mensagemConfirmacao) {
		this.mensagemConfirmacao = mensagemConfirmacao;
	}

	public String getProcessosCriticaPetPendente() {
		return processosCriticaPetPendente;
	}

	public void setProcessosCriticaPetPendente(
			String processosCriticaPetPendente) {
		this.processosCriticaPetPendente = processosCriticaPetPendente;
	}

	public String getProcessosCriticaNaoJuntadoPet() {
		return processosCriticaNaoJuntadoPet;
	}

	public void setProcessosCriticaNaoJuntadoPet(
			String processosCriticaNaoJuntadoPet) {
		this.processosCriticaNaoJuntadoPet = processosCriticaNaoJuntadoPet;
	}

	public List<ObjetoIncidente<?>> getListaProcessoImportacao() {
		return listaProcessoImportacao;
	}

	public void setListaProcessoImportacao(
			List<ObjetoIncidente<?>> listaProcessoImportacao) {
		this.listaProcessoImportacao = listaProcessoImportacao;
	}
	
	public String getTipoInclusao() {
		if (tipoInclusao == null) {
			tipoInclusao = TIPO_INCLUSAO_PROCESSO;
		}

		return tipoInclusao;
	}

	public void setTipoInclusao(String tipoInclusao) {
		this.tipoInclusao = tipoInclusao;
	}	

	public String getMensagemDeRestricaoRegistroDeAndamento() {
		return mensagemDeRestricaoRegistroDeAndamento;
	}

	public void setMensagemDeRestricaoRegistroDeAndamento(
			String mensagemDeRestricaoRegistroDeAndamento) {
		this.mensagemDeRestricaoRegistroDeAndamento = mensagemDeRestricaoRegistroDeAndamento;
	}
	
	private void montarMensagemRestricao(){
		mensagemDeRestricaoRegistroDeAndamento = null;
		if (listaDocumento != null){
			for (CheckableDataTableRowWrapper elemento: listaDocumento){
				if (isProcessoInativo(elemento)){
					String fechoFrase = " encontra(m)-se na situação \"Inativo\". Deseja continuar?"; 
					if (mensagemDeRestricaoRegistroDeAndamento == null){
						mensagemDeRestricaoRegistroDeAndamento = new String("O(s) processo(s) " + 
					    processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual().toString() + fechoFrase);
					} else {
						mensagemDeRestricaoRegistroDeAndamento = mensagemDeRestricaoRegistroDeAndamento.substring(0, mensagemDeRestricaoRegistroDeAndamento.indexOf(fechoFrase));
						mensagemDeRestricaoRegistroDeAndamento += ", " + processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual().toString() + fechoFrase;
					}
				}
			}
			setAtributo(MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO, mensagemDeRestricaoRegistroDeAndamento);			
		} else {
			mensagemDeRestricaoRegistroDeAndamento = null;
		}
	}
	
	public boolean isProcessoInativo(CheckableDataTableRowWrapper processoOuPeticao) {
		try {
			if (processoOuPeticao.getWrappedObject() instanceof Processo){
				DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso((Processo)processoOuPeticao.getWrappedObject());
				if ((ultimoDeslocamento!=null) && (ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO)))
					return true;
			} else {
				DeslocaPeticao ultimoDeslocamento = getDeslocamentoPeticaoService().recuperarUltimoDeslocamentoPeticao((Peticao)processoOuPeticao.getWrappedObject());
				if ((ultimoDeslocamento!=null) && (ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO)))
					return true;			
			}
		} catch (ServiceException e) {
		}
				
		return false;
	}
}
