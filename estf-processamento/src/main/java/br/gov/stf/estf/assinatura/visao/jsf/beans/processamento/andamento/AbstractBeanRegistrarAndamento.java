package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.service.exception.ProcessoComOrigemGenericaException;
import br.gov.stf.estf.assinatura.service.exception.ProcessoSemOrigemDefinidaException;
import br.gov.stf.estf.assinatura.stficp.RequisicaoAssinaturaDocumentoComunicacao;
import br.gov.stf.estf.assinatura.stfoffice.editor.DadosComunsDoCabecalhoHandler;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoDevolucao;
import br.gov.stf.estf.entidade.processostf.TipoHistorico;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia.TipoPreferenciaContante;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.exception.MinistroRelatorAposentadoException;
import br.gov.stf.estf.processostf.model.service.exception.ProcessoOutraRelatoriaException;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfoImpl;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.AscendantOrder;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.EqualCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.SearchCriterion;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;
import br.jus.stf.assinadorweb.api.util.PageRefresher;
import br.jus.stf.estf.montadortexto.SpecParte;
import br.jus.stf.util.jasperreports.UtilJasperReports;
import net.sf.jasperreports.engine.JRException;

public abstract class AbstractBeanRegistrarAndamento extends AssinadorBaseBean {

	private static final long serialVersionUID = 4394983261873029200L;
	
	private static final Object MINISTROS_PRESIDENTE_INTERINO = new Object();
	protected static int BAIXA_AUTOS_DILIGENCIAS = 7100;	
	public static int BAIXA_EXTENA_AUTOS = 7101;
	protected static int BAIXA_AO_ARQUIVO = 7103;
	protected static int BAIXA_DEFINITIVA = 7104;
	protected static int BAIXA_REMESSA_JUIZO = 7108;
	
	private static Long CONCLUSAO_PRESIDENCIA = 8201L;
	private static Long CONCLUSAO_VICEPRESIDENCIA = 8202L;
	private static Long CONCLUSAO_RELATOR = 8203L;
	private static Long CONCLUSAO_REVISOR = 8204L;
	private static Long CONCLUSAO_RELATOR_PARA_ACORDAO = 8238L;
	private static Long VISTA_A_PEGR_PARA_INTIMACAO = 8507L;
	
	private static Long CODIGO_SETOR_PRESIDENCIA = 600000179L;
	private static Long CODIGO_SETOR_VICE_PRESIDENCIA = 600000716L;
	private static Long CODIGO_COORDENADORIA_DE_GESTAO_DA_INFORMACAO_MEMORIA_INSTITUCIONAL_E_MUSEU = 600001106L;
	private static final int ORGAO_INTERNO = 2;
	
	private static List<Long> CODIGOS_ANDAMENTOS_IMPLICA_DESLOCAMENTO = new ArrayList<Long>(Arrays.asList(CONCLUSAO_PRESIDENCIA, CONCLUSAO_VICEPRESIDENCIA, CONCLUSAO_RELATOR, CONCLUSAO_REVISOR, CONCLUSAO_RELATOR_PARA_ACORDAO, new Long(BAIXA_AO_ARQUIVO)));
	private static List<String> PROCESSOS_RECURSAIS = new ArrayList<String>(Arrays.asList("AI","ARE","RE"));

	public static final String CHAVE_CODIGO_PESSOA_PGR = "usuario.pgr";
	public static final String CHAVE_CODIGO_PESSOA_AGU = "usuario.agu";
	public static final Long CODIGO_ORGAO_PGR = 23l;
	public static final Long CODIGO_ORGAO_AGU = 26l;
	public static final Long COD_ORGAO_STJ = 5186L;
    public static final Integer CODIGO_TIPO_ORGAO_ORIGEM = 2;
    public static final Integer CODIGO_TIPO_ORGAO_DESTINO = 3;		
	
	public static final int CODIGO_CERTIDAO_TRANSITO_JULGADO = 8219;
	public static final int CODIGO_CONTRARRAZOES = 8523;
	public static final int CODIGO_CONTRARRAZOES_DECURSO_PRAZO = 8543;
	public static final int CODIGO_RESPOSTA_DECURSO_PRAZO = 8544;
	public static final int CODIGO_AUTOS_DISPONIBILIZADOS = 8561;
	public static final int CODIGO_CONTRAMINUTA = 8524;
	private static final int CODIGO_CONTRAMINUTA_DECURSO_PRAZO = 4543;
	
	protected static List<Integer> CODIGOS_AVISO_PARA_DOCUMENTOS_ASSINADOS_E_NAO_PUBLICADOS = new ArrayList<Integer>(Arrays.asList(BAIXA_EXTENA_AUTOS, BAIXA_AO_ARQUIVO, BAIXA_DEFINITIVA, CODIGO_CERTIDAO_TRANSITO_JULGADO));
	
	public static final Long ID_TIPO_RECURSO_EMBARGO_DECLARACAO = 15L;	
	public static final Long ID_TIPO_RECURSO_AGRAVO_REGIMENTAL = 4L;
	
	public static final String FEEDBACKS = "feedbacks";
			
	protected static List<Long> LISTA_ANDAMENTOS_REQUER_TEMA = new ArrayList<Long>(Arrays.asList(6503L,6505L,6510L));
	protected static List<Long> LISTA_ANDAMENTOS_REQUER_PROCESSOS_PRINCIPAIS = new ArrayList<Long>(Arrays.asList(6503L,6505L,6510L));

	private final Object SOLICITAR_PROCESSO_E_OBSERVACAO = new Object();
	private final Object SOLICITAR_PROCESSOS = new Object();
	private final Object SOLICITAR_TEMA = new Object();
	private final Object SOLICITAR_PETICAO = new Object();
	private final Object SOLICITAR_ORIGEM_DECISAO = new Object();
	private final Object SOLICITAR_TIPO_DEVOLUCAO = new Object();
	private final Object SOLICITAR_PRESIDENTE_INTERINO = new Object();
	private static final Object DESLOCAR_AUTOMATICAMENTE = new Object();
	private static final Object ORIGENS_DECISAO = new Object();
	private static final Object ORIGEM_DECISAO = new Object();
	private static final Object ORIGEM_DEVOLUCAO = new Object();
	private static final Object ORIGENS_DEVOLUCAO = new Object();
	private static final Object ORIGENS_CADASTRADAS = new Object();
	private static final Object ORGAO = new Object();
	private static final Object ORGAOS = new Object();
	private static final Object PROCEDENCIAS = new Object();
	private static final Object PROCEDENCIA = new Object();
	private static final Object PRESIDENTE_INTERINO = new Object();
	private static final Object ANDAMENTOS = new Object();
	private static final Object ANDAMENTO_SELECIONADO = new Object();
	private static final Object DESCRICAO_ANDAMENTO = new Object();
	private static final Object IDENTIFICACAO_PROCESSO = new Object();
	private static final Object INCIDENTE_SELECIONADO = new Object();
	private static final Object PROCESSO_OUTRA_RELATORIA = new Object();
	private static final Object MINISTRO_RELATOR_APOSENTADO = new Object();
	private static final Object PROCESSO_FINDO = new Object();
	private static final Object PROCESSO_SELECIONADO = new Object();
	private static final Object PROCESSOS_SELECIONADOS = new Object();
	private static final Object PROCESSOS_TEMAS_SELECIONADOS = new Object();
	private static final Object TIPOS_DEVOLUCAO = new Object();
	private static final Object MINISTROS = new Object();
	private static final Object OBSERVACAO = new Object();
	private static final Object OBSERVACAO_INTERNA = new Object();
	private static final Object PROCESSO_FINDO_LISTA = new Object();
	private static final Object MSG_CONFIRMACAO = new Object();
	private static final Object CONFIRMA_DESLOCAMENTO = new Object();
	private static final Object PROCESSO_DIFERE_SETOR_USUARIO = new Object();
	private static final Object PRECISA_VERIFICAR_CODIGO_ORIGEM = new Object();
	private static final Object ORIGEM_SELECIONADA = new Object();
	

	protected HtmlDataTable tabelaOrigensCadastradas;
	protected HtmlDataTable tabelaProcessosSelecionados;
	protected HtmlDataTable tabelaPecasSelecionadas;
	protected HtmlInputText inputAndamento;
	protected HtmlInputHidden inputSolicitarPresidenteInterino;
	protected HtmlInputHidden inputSolicitarProcessoEObservacao;
	protected HtmlInputHidden inputSolicitarProcessos;
	protected HtmlInputHidden inputSolicitarTema;
	protected HtmlInputHidden inputSolicitarOrigemDecisao;
	protected HtmlInputHidden inputSolicitarTipoDevolucao;
	protected HtmlInputHidden inputSolicitarPeticao;
	protected HtmlInputHidden inputPrecisaVerificarCodigoOrigem;
	protected HtmlInputHidden inputProcessoFindo;
	protected HtmlInputHidden inputProcessoDifereSetorUsuario;
	protected HtmlInputHidden inputSucesso;
	protected HtmlInputHidden inputTemCertidao;
	protected HtmlInputHidden inputPrecisaListarDecisoes;
	protected List<SelectItem> origensDecisao;
	protected List<SelectItem> origensDevolucao;
	protected List<HistoricoProcessoOrigem> origensCadastradas;
	protected String numeroOrigem;
	protected String siglaOrigem;
	
	@KeepStateInHttpSession
	protected Integer origemSelecionada;
	
	protected Integer pecaSelecionada;
	protected Integer pecaSeleciona;
	
	protected List<SelectItem> orgaos;
	protected List<SelectItem> procedencias;
	protected List<HashMap<String, Object>> infoDeslocamentos;

	protected Long idOrigemDecisao;
	protected Long idOrgao;
	protected Long idOrigemDevolucao;
	protected Long idProcedencia;
	protected Andamento andamentoSelecionado;
	protected String descricaoAndamento;
	protected List<Andamento> andamentosAutorizados;
	protected List<SpecParte> listaSpecPartes;
	protected Processo processo;
	protected boolean confirmaDeslocamento = false;
	protected boolean processoFindo = false;
	protected boolean processoOutraRelatoria = false;
	protected boolean processoDifereSetorUsuario = false;
	protected boolean ministroRelatorAposentado = false;
	protected boolean solicitarProcessoEObservacao = false;
	protected boolean solicitarProcessos = false;
	protected Boolean solicitarTema = false;
	protected boolean solicitarPeticao = false;
	protected boolean solicitarOrigemDecisao = false;
	protected boolean solicitarTipoDevolucao = false;
	protected boolean solicitarPresidenteInterino = false;
	protected boolean precisaConfirmacaoLancarAndamento = false;
	protected boolean precisaVerificarCodigoOrigem = false;
	protected boolean precisaListarDecisoes = false;
	protected boolean processoFindoLista = false;
	protected boolean deslocarAutomaticamente = false;

	protected String mensagemConfirmacaoLancarAndamento;
	protected String identificacaoProcesso;
	protected String msgConfirmacaoAndamento = "";
	protected Integer quantidadeApensos;
	protected Object processoSelecionado;
	protected List<Processo> processosSelecionados;
	protected List<SelectItem> tiposDevolucao;
	protected Long idTipoDevolucao;
	protected List<SelectItem> ministrosPresidenteInterino;
	protected Long idPresidenteInterino;
	protected String observacaoInterna;
	protected String observacao;
	protected String numeroUnicoProcesso;
	protected ObjetoIncidente<?> incidenteSelecionado;

	protected List<ObjetoIncidente<?>> incidentes;
	
	protected List<String> feedbacks;
	
	protected Map<Processo, ObjetoIncidente<?>> mapaIncidentes;	
	protected List<SelectItem> ministros;
	protected Long idMinistroSelecionado;
	
	protected Long numeroTema;
	protected ProcessoTema processoTemaSelecionado;
	protected List<ProcessoTema> listaProcessoTema;
	private HtmlDataTable tabelaProcessoTema;
	
	public static final String NOME_SECRETARIO = "Patrícia Pereira de Moura Martins";
	public static final String DESCRICAO_CARGO = "Secretária Judiciária";
	
	private AndamentoProcesso andamentoProcessoGerado;
	String nomePDFBaixa = "";
	private boolean numeroDeUsuariosDiferente1 = false;
    public static final Object NUMERO_USUARIOS_DIFERENTE_1 = new Object();
    
    protected static final String PROCESSO_COM_ORIGEM_GENERICA = "O processo [%s] possui origem genérica cadastrada como destino de baixa/remessa.";
    protected static final String PROCESSO_SEM_ORIGEM_DEFINIDA = "O processo [%s] não possui origem cadastrada como destino de baixa/remessa.";
    
    protected boolean editarDestinoDeBaixa = true;
    protected boolean renderCheckBoxEditarDestinoBaixa = true;

	public void atualizarSessao() {
		setAtributo(SOLICITAR_PROCESSO_E_OBSERVACAO, solicitarProcessoEObservacao);
		setAtributo(SOLICITAR_PROCESSOS, solicitarProcessos);
		setAtributo(SOLICITAR_TEMA, solicitarTema);
		setAtributo(SOLICITAR_PETICAO, solicitarPeticao);
		setAtributo(SOLICITAR_ORIGEM_DECISAO, solicitarOrigemDecisao);
		setAtributo(SOLICITAR_TIPO_DEVOLUCAO, solicitarTipoDevolucao);
		setAtributo(SOLICITAR_PRESIDENTE_INTERINO, solicitarPresidenteInterino);
		setAtributo(DESLOCAR_AUTOMATICAMENTE, deslocarAutomaticamente);
		setAtributo(ORIGEM_DECISAO, idOrigemDecisao);
		setAtributo(ORIGEM_DEVOLUCAO, idOrigemDevolucao);
		setAtributo(ORIGENS_DEVOLUCAO, origensDevolucao);
		setAtributo(ORGAO, idOrgao);
		setAtributo(ORGAOS, orgaos);
		setAtributo(PROCEDENCIA, idProcedencia);
		setAtributo(PROCEDENCIAS, procedencias);
		setAtributo(PRESIDENTE_INTERINO, idPresidenteInterino);
		setAtributo(ANDAMENTOS, andamentosAutorizados);
		setAtributo(IDENTIFICACAO_PROCESSO, identificacaoProcesso);
		setAtributo(INCIDENTE_SELECIONADO, incidenteSelecionado);
		setAtributo(PROCESSO_OUTRA_RELATORIA, processoOutraRelatoria);
		setAtributo(MINISTRO_RELATOR_APOSENTADO, ministroRelatorAposentado);
		setAtributo(PROCESSO_FINDO, processoFindo);
		setAtributo(PROCESSO_SELECIONADO, processoSelecionado);
		setAtributo(PROCESSOS_SELECIONADOS, processosSelecionados);
		setAtributo(PROCESSOS_TEMAS_SELECIONADOS, listaProcessoTema);
		setAtributo(OBSERVACAO, observacao);
		setAtributo(OBSERVACAO_INTERNA, observacaoInterna);
		setAtributo(ANDAMENTO_SELECIONADO, andamentoSelecionado);
		setAtributo(DESCRICAO_ANDAMENTO, descricaoAndamento);
		setAtributo(PROCESSO_FINDO_LISTA, processoFindoLista);
		setAtributo(MSG_CONFIRMACAO, mensagemConfirmacaoLancarAndamento);
		setAtributo(CONFIRMA_DESLOCAMENTO, confirmaDeslocamento);
		setAtributo(PROCESSO_DIFERE_SETOR_USUARIO, processoDifereSetorUsuario);
		setAtributo(ORIGEM_SELECIONADA, origemSelecionada);
		setAtributo(PRECISA_VERIFICAR_CODIGO_ORIGEM, precisaVerificarCodigoOrigem);
	}

	@SuppressWarnings("unchecked")
	public void restaurarSessao() {
		ministrosPresidenteInterino = getAtributo(MINISTROS_PRESIDENTE_INTERINO) == null ? carregarMinistrosPresidenteInterino() : (List<SelectItem>) getAtributo(MINISTROS_PRESIDENTE_INTERINO);
		origensDecisao = getAtributo(ORIGENS_DECISAO) == null ? carregarOrigensDecisao(andamentoSelecionado) : (List<SelectItem>) getAtributo(ORIGENS_DECISAO);
		origensDevolucao = getAtributo(ORIGENS_DEVOLUCAO) == null ? carregarOrigensDevolucao() : (List<SelectItem>) getAtributo(ORIGENS_DEVOLUCAO);
		origensCadastradas = getAtributo(ORIGENS_CADASTRADAS) == null ? carregarOrigensCadastradas() : (List<HistoricoProcessoOrigem>) getAtributo(ORIGENS_CADASTRADAS);
		orgaos = getAtributo(ORGAOS) == null ? carregarOrgaos() : (List<SelectItem>) getAtributo(ORGAOS);
		procedencias = getAtributo(PROCEDENCIAS) == null ? carregarProcedencias() : (List<SelectItem>) getAtributo(PROCEDENCIAS);
		idOrigemDecisao = (Long) getAtributo(ORIGEM_DECISAO);
		idOrigemDevolucao = (Long) getAtributo(ORIGEM_DEVOLUCAO);
		idOrgao = (Long) getAtributo(ORGAO);
		idProcedencia = (Long) getAtributo(PROCEDENCIA);
		idPresidenteInterino = (Long) getAtributo(PRESIDENTE_INTERINO);
		solicitarProcessoEObservacao = (Boolean) getAtributo(SOLICITAR_PROCESSO_E_OBSERVACAO) == null ? false : (Boolean) getAtributo(SOLICITAR_PROCESSO_E_OBSERVACAO);
		deslocarAutomaticamente = (Boolean) getAtributo(DESLOCAR_AUTOMATICAMENTE) == null ? false : (Boolean) getAtributo(DESLOCAR_AUTOMATICAMENTE);
		solicitarProcessos = (Boolean) getAtributo(SOLICITAR_PROCESSOS) == null ? false : (Boolean) getAtributo(SOLICITAR_PROCESSOS);
		solicitarTema = (Boolean) getAtributo(SOLICITAR_TEMA) == null ? false : (Boolean) getAtributo(SOLICITAR_TEMA);
		solicitarPeticao = (Boolean) getAtributo(SOLICITAR_PETICAO) == null ? false : (Boolean) getAtributo(SOLICITAR_PETICAO);
		solicitarOrigemDecisao = (Boolean) getAtributo(SOLICITAR_ORIGEM_DECISAO) == null ? false : (Boolean) getAtributo(SOLICITAR_ORIGEM_DECISAO);
		solicitarTipoDevolucao = (Boolean) getAtributo(SOLICITAR_TIPO_DEVOLUCAO) == null ? false : (Boolean) getAtributo(SOLICITAR_TIPO_DEVOLUCAO);
		solicitarPresidenteInterino = (Boolean) getAtributo(SOLICITAR_PRESIDENTE_INTERINO) == null ? false : (Boolean) getAtributo(SOLICITAR_PRESIDENTE_INTERINO);
		andamentosAutorizados = (List<Andamento>) getAtributo(ANDAMENTOS) == null ? new ArrayList<Andamento>() : (List<Andamento>) getAtributo(ANDAMENTOS);
		identificacaoProcesso = (String) getAtributo(IDENTIFICACAO_PROCESSO);
		incidenteSelecionado = (ObjetoIncidente<?>) getAtributo(INCIDENTE_SELECIONADO);
		processoOutraRelatoria = (Boolean) getAtributo(PROCESSO_OUTRA_RELATORIA) == null ? false : (Boolean) getAtributo(PROCESSO_OUTRA_RELATORIA);
		ministroRelatorAposentado = (Boolean) getAtributo(MINISTRO_RELATOR_APOSENTADO) == null ? false : (Boolean) getAtributo(MINISTRO_RELATOR_APOSENTADO);
		processoFindo = (Boolean) getAtributo(PROCESSO_FINDO) == null ? false : (Boolean) getAtributo(PROCESSO_FINDO);
		processoSelecionado = (Processo) getAtributo(PROCESSO_SELECIONADO);
		processosSelecionados = (List<Processo>) getAtributo(PROCESSOS_SELECIONADOS);
		listaProcessoTema = (List<ProcessoTema>) getAtributo(PROCESSOS_TEMAS_SELECIONADOS);
		tiposDevolucao = getAtributo(TIPOS_DEVOLUCAO) == null ? carregarTiposDevolucao() : (List<SelectItem>) getAtributo(TIPOS_DEVOLUCAO);
		ministros = getAtributo(MINISTROS) == null ? carregarListaMinistros() : (List<SelectItem>) getAtributo(MINISTROS);
		observacao = (String) getAtributo(OBSERVACAO);
		observacaoInterna = (String) getAtributo(OBSERVACAO_INTERNA);
		andamentoSelecionado = (Andamento) getAtributo(ANDAMENTO_SELECIONADO);
		descricaoAndamento = (String) getAtributo(DESCRICAO_ANDAMENTO);
		processoFindoLista = (Boolean) getAtributo(PROCESSO_FINDO_LISTA) == null ? false : (Boolean) getAtributo(PROCESSO_FINDO_LISTA);
		mensagemConfirmacaoLancarAndamento = (String) getAtributo(MSG_CONFIRMACAO) == null ? "" : (String) getAtributo(MSG_CONFIRMACAO);
		confirmaDeslocamento = (Boolean) getAtributo(CONFIRMA_DESLOCAMENTO) == null ? false : (Boolean) getAtributo(CONFIRMA_DESLOCAMENTO);
		processoDifereSetorUsuario = (Boolean) getAtributo(PROCESSO_DIFERE_SETOR_USUARIO) == null ? false : (Boolean) getAtributo(PROCESSO_DIFERE_SETOR_USUARIO);
		precisaVerificarCodigoOrigem = (Boolean) getAtributo(PRECISA_VERIFICAR_CODIGO_ORIGEM) == null ? false : (Boolean) getAtributo(PRECISA_VERIFICAR_CODIGO_ORIGEM);
		origemSelecionada = (Integer) getAtributo(ORIGEM_SELECIONADA);
	}
	
	public void verificarAndamento(ActionEvent event){
		verificarAndamento();
	}
	
	protected Origem recuperarOrigemBaixaAutomatica(Processo processo) throws ProcessoSemOrigemDefinidaException, ProcessoComOrigemGenericaException{
		Integer indice = verificarBaixaAutomatica(processo);
		if (indice != null)
			return origensCadastradas.get(indice).getOrigem();
		
		return null;
	}
	
	protected Integer verificarBaixaAutomatica(Processo processo) throws ProcessoSemOrigemDefinidaException, ProcessoComOrigemGenericaException{
		
		
		if (this.andamentoSelecionado != null){
			this.processo = processo;
			carregarOrigensCadastradas();
			
			//A baixa automática só está disponível para processos eletrônicos. Os códigos de baixa são 7101, 7104 e 7108
			if (processo!=null) 
				if (processo.isEletronico()){
					if(BeanRegistrarAndamentoParaVariosProcessos.CODIGOS_BAIXA_REMESSA.contains(this.andamentoSelecionado.getId())){

						//Achou o último remetente (FLG_REMETENTE = S)
						HistoricoProcessoOrigem hpo = getUltimoRemetente(origensCadastradas);
						
						if (hpo!=null && hpo.getRemetente()!=null && hpo.getRemetente().equals(Boolean.TRUE)){
							
							//Se o último remetente for STJ, verifica se vai ser devolvido para o mesmo 
							if (hpo.getOrigem().getId().equals(COD_ORGAO_STJ)){
								
								//Busca o destinatario marcado com S. Destino definido por esse registro (checkbox desmarcado e habilitado); 
								HistoricoProcessoOrigem hisDestinoIndicadoPeloSTJ = buscaDestinoFlagSim(origensCadastradas);
								
								//Se o registro existe e é S, devolve para a origem dele.
								if ( hisDestinoIndicadoPeloSTJ != null ){
									//Testado
									setPrecisaVerificarCodigoOrigem(false);
									return origensCadastradas.indexOf( hisDestinoIndicadoPeloSTJ );
								//Se a FLG_DESTINATÁRIO é NULL, não há regra, obriga a escolha em tela.
								} else {
									//Testado
									setPrecisaVerificarCodigoOrigem(true);
								}
							//Caso o último remetente não seja o STJ, devolver para o remetente marcado com S. Destino definido (checkbox desmarcado e habilitado);
							} else {
								//Verifica se a origem encontrada é generica. Se for genérica, significa que não está definida. 
								//Para processo individuais abre a tela para a escolha manual. Para vários processos, impede o lançamento
								if (ehOrigemGenerica(hpo)){
									//Testado
									setPrecisaVerificarCodigoOrigem(true);
									throw new ProcessoComOrigemGenericaException(String.format(PROCESSO_COM_ORIGEM_GENERICA, processo.getIdentificacao()));
								} else {//Se não é generica
									//Testado
									setPrecisaVerificarCodigoOrigem(false);
									return origensCadastradas.indexOf(hpo);
								}
							}
						//Se a FLG_REMETENTE = N ou NULL, não há destino definido, o usuário deve definir (checkbox marcado e desabilitado = obriga a definição manual)
						} else{
							//Testado
							setPrecisaVerificarCodigoOrigem(true);
							throw new ProcessoSemOrigemDefinidaException(String.format(PROCESSO_SEM_ORIGEM_DEFINIDA, processo.getIdentificacao()));
						}
					} else //Para processos físicos
						//Testado
						setPrecisaVerificarCodigoOrigem(false);
				} else {
					if(BeanRegistrarAndamentoParaVariosProcessos.CODIGOS_BAIXA_REMESSA.contains(this.andamentoSelecionado.getId()))
						//Testado
						setPrecisaVerificarCodigoOrigem(true);
					else
						//Testado
						setPrecisaVerificarCodigoOrigem(false);
				}
		}
		
		return null;
	}
	
	private boolean ehOrigemGenerica(HistoricoProcessoOrigem historico){
		
		for (Long codOrigem : BeanRegistrarAndamentoParaVariosProcessos.ORIGENS_GENERICAS){
			if (codOrigem.equals(historico.getOrigem().getId())){
				return true;
			}
		}
		return false;
	}
	
	private HistoricoProcessoOrigem buscaDestinoFlagSim(List<HistoricoProcessoOrigem> origensCadastradas){
		if (origensCadastradas.size() <= 0)
			return null;
		for (HistoricoProcessoOrigem historicoProcessoOrigem : origensCadastradas) {
			if (historicoProcessoOrigem.getDestinatario().equals(Boolean.TRUE))
				return historicoProcessoOrigem;
		}
		
		return null;
		
	}
	private HistoricoProcessoOrigem getUltimoRemetente(List<HistoricoProcessoOrigem> origensCadastradas){
		if (origensCadastradas.size() <= 0)
			return null;
		
		HistoricoProcessoOrigem hpoIdentificado = null;
		for (HistoricoProcessoOrigem historicoProcessoOrigem : origensCadastradas) {
			if (historicoProcessoOrigem.getRemetente()!=null && historicoProcessoOrigem.getRemetente().equals(Boolean.TRUE))
				if (hpoIdentificado == null){
					hpoIdentificado = historicoProcessoOrigem;
				}else{
					if ( historicoProcessoOrigem.getId() > hpoIdentificado.getId() ){
						hpoIdentificado = historicoProcessoOrigem;
					}
				}
		}
		return hpoIdentificado;
	}

	public void setIdPresidenteInterino(Long idPresidenteInterino) {
		this.idPresidenteInterino = idPresidenteInterino;
		atualizarSessao();
	}

	public Long getIdPresidenteInterino() {
		return idPresidenteInterino;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
		setAtributo(OBSERVACAO, observacao);
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacaoInterna(String observacaoInterna) {
		this.observacaoInterna = observacaoInterna;
		setAtributo(OBSERVACAO_INTERNA, observacaoInterna);
	}

	public String getObservacaoInterna() {
		return observacaoInterna;
	}

	public void setMinistrosPresidenteInterino(
			List<SelectItem> ministrosPresidenteInterino) {
		this.ministrosPresidenteInterino = ministrosPresidenteInterino;
	}

	public List<SelectItem> getMinistrosPresidenteInterino() {
		return ministrosPresidenteInterino;
	}

	public Long getIdTipoDevolucao() {
		return idTipoDevolucao;
	}

	public void setIdTipoDevolucao(Long idTipoDevolucao) {
		this.idTipoDevolucao = idTipoDevolucao;
	}

	public void setTiposDevolucao(List<SelectItem> tiposDevolucao) {
		this.tiposDevolucao = tiposDevolucao;
	}

	public List<SelectItem> getTiposDevolucao() {
		return tiposDevolucao;
	}

	public void setProcessoSelecionado(Object processoSelecionado) {
		this.processoSelecionado = processoSelecionado;
		atualizarSessao();
	}

	public Object getProcessoSelecionado() {
		return processoSelecionado;
	}

	public List<Processo> getProcessosSelecionados() {
		return processosSelecionados;
	}

	public void setAdicionarProcessoSelecionado(Object processoSelecionado) {
		this.processoSelecionado = processoSelecionado;
		atualizarSessao();
	}

	public void adicionarProcessoSelecionado(ActionEvent event) {
		if (processosSelecionados == null) {
			processosSelecionados = new ArrayList<Processo>();
		}
		processosSelecionados.add((Processo) processoSelecionado);
		processoSelecionado = null;
		quantidadeApensos = null;
		atualizarSessao();
	}

	public void setProcessosSelecionados(List<Processo> processosSelecionados) {
		this.processosSelecionados = processosSelecionados;
		atualizarSessao();
	}

	public void setQuantidadeApensos(Integer quantidadeApensos) {
		this.quantidadeApensos = quantidadeApensos;
		if (processoSelecionado != null)
			((Processo) processoSelecionado)
					.setQuantidadeApensosFixo(quantidadeApensos);
	}

	public Integer getQuantidadeApensos() {
		if (processoSelecionado != null) {
			if (((Processo) processoSelecionado).getQuantidadeApensosFixo() == null) {
				return 0;
			} else {
				return ((Processo) processoSelecionado)
						.getQuantidadeApensosFixo();
			}
		} else {
			return quantidadeApensos;
		}
	}

	public boolean isMinistroRelatorAposentado() {
		return ministroRelatorAposentado;
	}

	public void setMinistroRelatorAposentado(boolean ministroRelatorAposentado) {
		this.ministroRelatorAposentado = ministroRelatorAposentado;
	}

	public void setOrigensDecisao(List<SelectItem> origensDecisao) {
		this.origensDecisao = origensDecisao;
	}

	public List<SelectItem> getOrigensDecisao() {
		return origensDecisao;
	}

	public void setInputAndamento(HtmlInputText inputAndamento) {
		this.inputAndamento = inputAndamento;
	}

	public HtmlInputText getInputAndamento() {
		return inputAndamento;
	}

	public void setAndamentoSelecionado(Andamento andamentoSelecionado) {
		this.renderCheckBoxEditarDestinoBaixa = true;
		this.andamentoSelecionado = andamentoSelecionado;
		if(andamentoSelecionado != null && andamentoSelecionado.getId() != null && andamentoSelecionado.getDescricao() != null){
			this.descricaoAndamento = andamentoSelecionado.getId() + " - " + andamentoSelecionado.getDescricao();
			setListaProcessoTema(null);
			verificarAndamento();
			Integer verificarBaixaAutomatica = null;
			try{
				verificarBaixaAutomatica = verificarBaixaAutomatica(this.processo);
			} catch (ProcessoComOrigemGenericaException  e1){
				setOrigemSelecionada( null );
			} catch (ProcessoSemOrigemDefinidaException  e2){
				setOrigemSelecionada( null );
			}
			setOrigemSelecionada( verificarBaixaAutomatica ); 
			atualizarSessao();
		}
	}

	public Andamento getAndamentoSelecionado() {
		return andamentoSelecionado;
	}

	public void setInputSolicitarPresidenteInterino(
			HtmlInputHidden inputSolicitarPresidenteInterino) {
		this.inputSolicitarPresidenteInterino = inputSolicitarPresidenteInterino;
	}

	public HtmlInputHidden getInputSolicitarPresidenteInterino() {
		return inputSolicitarPresidenteInterino;
	}

	public List<Andamento> getAndamentosAutorizados() {
		if (andamentosAutorizados == null || andamentosAutorizados.size() == 0) {
			carregarAndamentosAutorizados();
		}
		return andamentosAutorizados;

	}

	public void setAndamentosAutorizados(List<Andamento> andamentosAutorizados) {
		this.andamentosAutorizados = andamentosAutorizados;
	}

	public void setIdOrigemDecisao(Long idOrigemDecisao) {
		this.idOrigemDecisao = idOrigemDecisao;
		atualizarSessao();
	}

	public Long getIdOrigemDecisao() {
		return idOrigemDecisao;
	}

	public void setProcessoOutraRelatoria(boolean processoOutraRelatoria) {
		this.processoOutraRelatoria = processoOutraRelatoria;
	}

	public boolean isProcessoOutraRelatoria() {
		return processoOutraRelatoria;
	}

	public void setSolicitarPeticao(String solicitarPeticao) {
		this.solicitarPeticao = new Boolean(solicitarPeticao);
	}

	public String getSolicitarPeticao() {
		return new Boolean(solicitarPeticao).toString();
	}

	public String getSolicitarTipoDevolucao() {
		return new Boolean(solicitarTipoDevolucao).toString();
	}

	public void setSolicitarTipoDevolucao(String solicitarTipoDevolucao) {
		this.solicitarTipoDevolucao = new Boolean(solicitarTipoDevolucao);
	}

	public String getSolicitarOrigemDecisao() {
		return new Boolean(solicitarOrigemDecisao).toString();
	}

	public void setSolicitarOrigemDecisao(String solicitarOrigemDecisao) {
		this.solicitarOrigemDecisao = new Boolean(solicitarOrigemDecisao);
	}

	public String getSolicitarPresidenteInterino() {
		return new Boolean(solicitarPresidenteInterino).toString();
	}

	public void setSolicitarPresidenteInterino(
			String solicitarPresidenteInterino) {
		this.solicitarPresidenteInterino = new Boolean(
				solicitarPresidenteInterino);
	}

	public void setPrecisaConfirmacaoLancarAndamento(
			String precisaConfirmacaoLancarAndamento) {

		this.precisaConfirmacaoLancarAndamento = new Boolean(
				precisaConfirmacaoLancarAndamento);
	}

	public String getPrecisaConfirmacaoLancarAndamento() {
		return new Boolean(precisaConfirmacaoLancarAndamento).toString();
	}

	public String getSolicitarProcessoEObservacao() {
		return new Boolean(solicitarProcessoEObservacao).toString();
	}

	public void setSolicitarProcessoEObservacao(
			String solicitarProcessoEObservacao) {
		this.solicitarProcessoEObservacao = new Boolean(
				solicitarProcessoEObservacao);
	}

	public String getSolicitarProcessos() {
		return new Boolean(solicitarProcessos).toString();
	}

	public void setSolicitarProcessos(String solicitarProcessos) {
		this.solicitarProcessos = new Boolean(solicitarProcessos);
	}
	
	public String getSolicitarTema() {
		return new Boolean(solicitarTema).toString();
	}

	public void setSolicitarTema(String solicitarTema) {
		this.solicitarTema = new Boolean(solicitarTema);
	}	

	public void setMensagemConfirmacaoLancarAndamento(
			String mensagemConfirmacaoLancarAndamento) {
		this.mensagemConfirmacaoLancarAndamento = mensagemConfirmacaoLancarAndamento;
	}

	public String getMensagemConfirmacaoLancarAndamento() {
		return mensagemConfirmacaoLancarAndamento;
	}

	public void setMsgConfirmacaoAndamento(String msgConfirmacaoAndamento) {
		if (msgConfirmacaoAndamento.equals("")) {
			this.msgConfirmacaoAndamento = msgConfirmacaoAndamento;
		} else {
			this.msgConfirmacaoAndamento = this.msgConfirmacaoAndamento + " "
					+ msgConfirmacaoAndamento;
		}
	}

	public String getConfirmacaoAndamento() {
		return new Boolean(processoOutraRelatoria || ministroRelatorAposentado)
				.toString();
	}

	public void setConfirmacaoAndamento(String confirmacaoAndamento) {
	}

	public String getMsgConfirmacaoAndamento() {
		return msgConfirmacaoAndamento;
	}

	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		this.identificacaoProcesso = identificacaoProcesso;
		atualizarSessao();
	}

	
	public ObjetoIncidente<?> getIncidenteSelecionado() {
		return incidenteSelecionado;
	}

	public void setIncidenteSelecionado(ObjetoIncidente<?> incidenteSelecionado) {
		try{
			this.incidenteSelecionado = incidenteSelecionado;
			Processo processo = ((Processo) incidenteSelecionado.getPrincipal());
			setProcesso(processo);
			setIdentificacaoProcesso(incidenteSelecionado.getIdentificacao());
			setAndamentoSelecionado(null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Processo getObjetoIncidentePrincipal (){ 
		return ((Processo) incidenteSelecionado.getPrincipal());
	}
	
	public void setProcessoFindo(boolean processoFindo) {
		this.processoFindo = processoFindo;
	}

	public boolean isProcessoFindo() {
		return processoFindo;
	}

	public String getIdentificacaoProcesso() {
		return identificacaoProcesso;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		try {
			this.processo = processo;
			identificacaoProcesso = processo.getIdentificacao();
			adicionarProcesso(processo);
			atualizarSessao();
		} catch (Exception e) {
		}
	}

	protected void adicionarProcesso(Processo processoSelecionado) throws ServiceException {
		processoOutraRelatoria = false;
		ministroRelatorAposentado = false;
		try {
			if (processoSelecionado == null) {
				reportarAviso("Processo não encontrado.");
			} else {
				getAndamentoProcessoService().verificarSituacaoProcesso(processoSelecionado);
				verificarSetorUsuario(processoSelecionado, getSetorUsuarioAutenticado());
				verificarMinistroRelatorAposentado(processoSelecionado);
				processoFindo = getProcessoService().isProcessoFindo(processoSelecionado);
				if (inputProcessoFindo != null) {
					inputProcessoFindo.setValue(processoFindo);
				}
				if (processoFindo) {
					reportarAviso("Este é um PROCESSO FINDO!");
				}
				doAdicionarProcesso(processoSelecionado);
				carregarOrigensCadastradas();
				carregaListaDePartes(processoSelecionado);
				if(getPodeDeslocarAutomaticamente(mapaIncidentes.get(processoSelecionado))){
					processoSelecionado.setObjetoIncidentePodeSerDeslocado(true);
					processoSelecionado.setDeslocarObjetoIncidente(true);
				}
			}
		} catch (ServiceException e) {
			reportarErro("Ocorreu um erro ao adicionar o processo!", e.getMessage());
			log.error("Erro ao adicionar processo.", e);
		}
	}
	
	public boolean isPecaDeMerito() {
		switch(idAndamentoSelecionado().intValue()){
		case CODIGO_CONTRARRAZOES:
			return false;
		case CODIGO_CONTRAMINUTA:
			return false;
		case CODIGO_RESPOSTA_DECURSO_PRAZO:
			return false;
		case CODIGO_CONTRARRAZOES_DECURSO_PRAZO:
			return false;
		default:
			return true;
		}		
	}
	
	public boolean isAndamentoPermitido() throws ServiceException {	
		for(ObjetoIncidente<?> inc : incidentes){
			switch(idAndamentoSelecionado().intValue()){
			case CODIGO_CONTRARRAZOES:
				return validarLancamentoContrarrazao(inc);
			case CODIGO_CONTRAMINUTA:
				return validarLancamentoContraminuta(inc);
			case CODIGO_CONTRARRAZOES_DECURSO_PRAZO:
				return validarLancamentoContrarrazao(inc);
			case CODIGO_RESPOSTA_DECURSO_PRAZO:
				return validarLancamentoContraminuta(inc);
			case CODIGO_AUTOS_DISPONIBILIZADOS:
				return validarLancamentoAutosDisponibilizados(inc);
			}
			
			if(inc != null && inc.getEletronico() && getAndamentoSelecionadoImplicaEmDeslocamentoAutomatico()){
				return validarDeslocamento(inc);
			}
		}
		return true;		
	}
	



	@SuppressWarnings("unchecked")
	public void reportarFeedbacks() {
		if(getAtributo(FEEDBACKS) != null){
			feedbacks = (List<String>) getAtributo(FEEDBACKS);
			if(feedbacks != null && !feedbacks.isEmpty()){
				for(String s : feedbacks){
					reportarAviso(s);
				}
			}
			setAtributo(FEEDBACKS, null);
		}
	}
	
	public Long idAndamentoSelecionado() {
		if(getAndamentoSelecionado() != null){
			return getAndamentoSelecionado().getId();
		}
		return null;
	}

	private boolean validarLancamentoAutosDisponibilizados(ObjetoIncidente<?> incidenteSelecionado) {
		if(incidenteSelecionado.getTipoMeio().equals(TipoMeioProcesso.FISICO.getCodigo())) {
		feedbacks.add("Não foi registrado o andamento para " + incidenteSelecionado.getIdentificacao() + ". O andamento selecionado somente pode ser usado para processos eletrônicos.");
		setAtributo(FEEDBACKS, feedbacks);
		return false;
		}else{
			return true;
		}
	}
	
	private boolean validarLancamentoContraminuta(ObjetoIncidente<?> incidenteSelecionado) {
		if(incidenteSelecionado instanceof RecursoProcesso){
			Long idTipoRecurso = ((RecursoProcesso) incidenteSelecionado).getTipoRecursoProcesso().getId();
			if(idTipoRecurso.equals(ID_TIPO_RECURSO_EMBARGO_DECLARACAO)){
				return true;
			}
		}
		feedbacks.add("Não foi registrado o andamento para " + incidenteSelecionado.getIdentificacao() + ". O andamento selecionado somente pode ser usado para Embargos de Declaração.");
		setAtributo(FEEDBACKS, feedbacks);
		return false;
	}

	private boolean validarLancamentoContrarrazao(ObjetoIncidente<?> incidenteSelecionado) {
		if(incidenteSelecionado instanceof RecursoProcesso){
			Long idTipoRecurso = ((RecursoProcesso) incidenteSelecionado).getTipoRecursoProcesso().getId();
			if(idTipoRecurso.equals(ID_TIPO_RECURSO_AGRAVO_REGIMENTAL)){
				return true;
			}
		}
		feedbacks.add("Não foi registrado o andamento para " + incidenteSelecionado.getIdentificacao() + ". O andamento selecionado somente pode ser usado para Agravo Regimental.");
		setAtributo(FEEDBACKS, feedbacks);
		return false;
	}	
	
	@SuppressWarnings("deprecation")
	public ModeloComunicacao recuperarModeloComunicacaoDoAndamento() throws ServiceException {
		ModeloComunicacao modelo = null;
		String descricaoModelo = null;
		if(idAndamentoSelecionado() != null){
			switch(idAndamentoSelecionado().intValue()){
				case CODIGO_CERTIDAO_TRANSITO_JULGADO:
					descricaoModelo = "Certidão Trânsito Julgado";
					break;
				case CODIGO_CONTRARRAZOES:
					descricaoModelo = "Contrarrazão de Agravo";
					break;
				case CODIGO_CONTRAMINUTA:
					descricaoModelo = "Contraminuta de Embargo";
					break;
				case CODIGO_CONTRAMINUTA_DECURSO_PRAZO:
					descricaoModelo = "Contraminuta de Embargo";
					break;
				case CODIGO_CONTRARRAZOES_DECURSO_PRAZO:
					descricaoModelo = "Decurso de prazo para contrarrazões";
					break;
				case CODIGO_RESPOSTA_DECURSO_PRAZO:
					descricaoModelo = "Decurso de prazo para resposta";
					break;
			}
		}
		
		if(descricaoModelo != null){		
			List<SearchCriterion> search = new ArrayList<SearchCriterion>();
			search.add(new EqualCriterion<String>("dscModelo", descricaoModelo));
	
			List<ModeloComunicacao> modelos = getModeloComunicacaoService().pesquisarPorExemplo(new ModeloComunicacao(), search);
			
			
			if(modelos != null && !modelos.isEmpty()){
				modelo = modelos.get(0);
			}
		}
		return modelo;
	}
	
	public boolean getAndamentoGeraDocumento() {
		if(idAndamentoSelecionado() != null){
			switch(idAndamentoSelecionado().intValue()){
				case CODIGO_CERTIDAO_TRANSITO_JULGADO:
					return true;
				case CODIGO_CONTRARRAZOES:
					return true;
				case CODIGO_CONTRARRAZOES_DECURSO_PRAZO:
					return true;
				case CODIGO_CONTRAMINUTA:
					return true;
				case CODIGO_CONTRAMINUTA_DECURSO_PRAZO:
					return true;
				case CODIGO_RESPOSTA_DECURSO_PRAZO:
					return true;
			}
		}
		return false;
	}
	
	protected TipoPecaProcesso tipoPecaProcessoDoAndamentoSelecionado() throws ServiceException {
		if(idAndamentoSelecionado() != null){
			switch(idAndamentoSelecionado().intValue()){
				case CODIGO_CERTIDAO_TRANSITO_JULGADO:
					return getTipoPecaProcessoService().recuperar("CERTTRAN");
				case CODIGO_CONTRARRAZOES:
					return getTipoPecaProcessoService().recuperar("CONTRRAZAO");
				case CODIGO_CONTRARRAZOES_DECURSO_PRAZO:
					return getTipoPecaProcessoService().recuperar("DECUR_CONTRRAZAO");
				case CODIGO_CONTRAMINUTA:
					return getTipoPecaProcessoService().recuperar("CONTRMINUTA");
				case CODIGO_CONTRAMINUTA_DECURSO_PRAZO:
					return getTipoPecaProcessoService().recuperar("CONTRMINUTA");
				case CODIGO_RESPOSTA_DECURSO_PRAZO:
					return getTipoPecaProcessoService().recuperar("DECUR_RESPOSTA");
			}
		}
		return null;
	}
	
	protected List<InforParte> getListaInforPartes(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		carregaListaDePartes(objetoIncidente);
		List<InforParte> partes = new ArrayList<InforParte>();
		
		for(SpecParte parte : getListaSpecPartes()){
			InforParte ip = new InforParte();
			ip.setTipoParte(parte.getTipo());
			ip.setNomeParte(parte.getComplemento() != null ? parte.getNome().concat(" ").concat(parte.getComplemento()) : parte.getNome());
			partes.add(ip);
		}
		
		return partes;
	}
	
	protected void assinarDocumento(List<ComunicacaoDocumento> listaDocumentos) throws ServiceException {

		if (!CollectionUtils.isVazia(listaDocumentos)) {
			ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
			List<DocumentoPDF<DocumentoComunicacao>> arquivos = null;

			try {
				arquivos = comunicacaoServiceLocal.assinar(listaDocumentos);
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			} catch (ServiceLocalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (CollectionUtils.isNotVazia(arquivos)) {
				RequisicaoAssinaturaDocumentoComunicacao requisicao = new RequisicaoAssinaturaDocumentoComunicacao();
				requisicao.setDocumentos(arquivos);
				requisicao.setPageRefresher((PageRefresher) getRefreshController());

				setAtributo("requisicao", requisicao);
			}
		}
	}
	
	public void normalizaPecasObjetoIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException, DaoException{
		if(isAndamentoBaixa()){
			List<PecaProcessoEletronico> listaPecasPendentes = getPecaProcessoEletronicoService().pecaProcessoEletronicoPendenteVisualizacao(objetoIncidente);
			if(listaPecasPendentes != null && !listaPecasPendentes.isEmpty()){
				for( PecaProcessoEletronico ppv : listaPecasPendentes){
					for(ArquivoProcessoEletronico doc : ppv.getDocumentos()){
						getDocumentoEletronicoService().alterarTipoDeAcessoDoDocumento(doc.getDocumentoEletronico(), TipoDeAcessoDoDocumento.PUBLICO);
					}
				}
			}
		}
	}
	
	private boolean isAndamentoBaixa() {
		if(getAndamentoSelecionado() != null 
				&& getAndamentoSelecionado().getId().intValue() > 0
				&& AndamentoProcesso.CODIGOS_ANDAMENTOS_BAIXA.contains(getAndamentoSelecionado().getId())){
			return true;
		}
		return false;
	}
	
    public void deslocarProcesso(Long codigoOrgaoDestino, ObjetoIncidente<?> objetoIncidente, Integer codigoTipoOrgaoOrigem, Integer codigoTipoOrgaoDestino, Long codigoAndamentoSelecionado ) throws ServiceException {
        DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(objetoIncidente);
        Long codigoOrgaoOrigemAtualProcesso = deslocaProcesso.getCodigoOrgaoDestino();
        getDeslocaProcessoServiceLocal().deslocarProcesso(objetoIncidente.getId(),
                										  codigoOrgaoOrigemAtualProcesso, 
                										  codigoOrgaoDestino,
                										  codigoTipoOrgaoOrigem, 
                										  codigoTipoOrgaoDestino, 
                										  true,
                										  codigoAndamentoSelecionado);
    }	
	
    public void deslocarProcessoApensos(Long codigoOrgaoDestino, ObjetoIncidente<?> objetoIncidente, Integer codigoTipoOrgaoOrigem, Integer codigoTipoOrgaoDestino, Long codigoAndamentoSelecionado ) throws ServiceException {
        DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(objetoIncidente);
        Long codigoOrgaoOrigemAtualProcesso = deslocaProcesso.getCodigoOrgaoDestino();
        getDeslocaProcessoServiceLocal().deslocarProcessoApensos(objetoIncidente.getId(),
                										  codigoOrgaoOrigemAtualProcesso, 
                										  codigoOrgaoDestino,
                										  codigoTipoOrgaoOrigem, 
                										  codigoTipoOrgaoDestino, 
                										  true,
                										  codigoAndamentoSelecionado);
    }	
	
	public void deslocarProcesso(ObjetoIncidente<?> objetoIncidente) {
		try {
			Setor setorDestino = paraOndeDeslocar(objetoIncidente);

			if(setorDestino == null){
				feedbacks.add("Não foi possível deslocar o processo " + objetoIncidente.getIdentificacao() + ". Setor destino não recuperado.");
				setAtributo(FEEDBACKS, feedbacks);
			}else{
				Long idSetorProcesso = getDeslocaProcessoService().pesquisarSetorUltimoDeslocamento((Processo) objetoIncidente);
				if(idSetorProcesso != null){
					if(idSetorProcesso.equals(getSetorUsuarioAutenticado().getId())){						
						deslocarProcesso(setorDestino.getId(), objetoIncidente, 2, 2, getAndamentoSelecionado().getId());
						reportarInformacao(objetoIncidente.getIdentificacao() + " deslocado com sucesso.");
					}else{
						if(!idSetorProcesso.equals(setorDestino.getId())){
							feedbacks.add("Não foi possível deslocar o processo " + objetoIncidente.getIdentificacao() + " porque ele não está no seu setor.");
							setAtributo(FEEDBACKS, feedbacks);
						}
					}
				}
			}
		} catch (ServiceException e) {
			reportarAviso("Não foi possível fazer o deslocamento do " + objetoIncidente.getIdentificacao() + ". " + e.getCause().getMessage());
		}
	}
	
	public void deslocarProcessoApensos(ObjetoIncidente<?> objetoIncidente) {
		try {
			Setor setorDestino = paraOndeDeslocar(objetoIncidente);

			if(setorDestino == null){
				feedbacks.add("Não foi possível deslocar o processo " + objetoIncidente.getIdentificacao() + ". Setor destino não recuperado.");					
				setAtributo(FEEDBACKS, feedbacks);
			}else{
				Long idSetorProcesso = getDeslocaProcessoService().pesquisarSetorUltimoDeslocamento((Processo) objetoIncidente);
				if(idSetorProcesso != null){
					if(idSetorProcesso.equals(getSetorUsuarioAutenticado().getId())){						
						deslocarProcessoApensos(setorDestino.getId(), objetoIncidente, 2, 2, getAndamentoSelecionado().getId());
						reportarInformacao(objetoIncidente.getIdentificacao() + " deslocado com sucesso.");
					}else{
						if(!idSetorProcesso.equals(setorDestino.getId())){
							feedbacks.add("Não foi possível deslocar o processo " + objetoIncidente.getIdentificacao() + " porque ele não está no seu setor.");
							setAtributo(FEEDBACKS, feedbacks);
						}
					}
				}
			}
		} catch (ServiceException e) {
			reportarAviso("Não foi possível fazer o deslocamento do " + objetoIncidente.getIdentificacao() + ". " + e.getCause().getMessage());
		}
	}
	
	private Setor paraOndeDeslocar(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		Setor setorDestino = null;
		if(andamentoSelecionado.getId().equals(CONCLUSAO_PRESIDENCIA) && isProcessoOriginarioOuRecursalComPreferenciaCriminal(objetoIncidente)){
			setorDestino = getSetorService().recuperarPorId(CODIGO_SETOR_PRESIDENCIA);			
		}else if(andamentoSelecionado.getId().equals(CONCLUSAO_VICEPRESIDENCIA)){
			setorDestino = getSetorService().recuperarPorId(CODIGO_SETOR_VICE_PRESIDENCIA);
		}else if(andamentoSelecionado.getId().equals(CONCLUSAO_RELATOR)){
			setorDestino = getProcesso().getMinistroRelatorAtual().getSetor();
		}else if(andamentoSelecionado.getId().equals(CONCLUSAO_REVISOR) || andamentoSelecionado.getId().equals(CONCLUSAO_RELATOR_PARA_ACORDAO)){
			if(getIdMinistroSelecionado() != null && getIdMinistroSelecionado().intValue() > 0){
				Ministro revisor = getMinistroSelecionado();
				if(revisor != null){
					setorDestino = revisor.getSetor();
				}
			}
		}else if(andamentoSelecionado.getId().equals(new Long(BAIXA_AO_ARQUIVO))){
			setorDestino = getSetorService().recuperarPorId(CODIGO_COORDENADORIA_DE_GESTAO_DA_INFORMACAO_MEMORIA_INSTITUCIONAL_E_MUSEU);
		}
		
		return setorDestino;
	}
	
	public void deslocarProcessoVistaMinistro(ObjetoIncidente<?> objetoIncidente, AndamentoProcesso andamento) throws InterruptedException {
		try {
			
			if(idOrigemDecisao !=null && objetoIncidente.getTipoMeio().equals("E") ){
				OrigemAndamentoDecisao origemAndamentoDecisao = getOrigemAndamentoDecisaoService().recuperarPorId(idOrigemDecisao);
				Setor setorDestino = origemAndamentoDecisao.getSetor();
				DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(objetoIncidente);
				
				if(ultimoDeslocamento != null && !ultimoDeslocamento.getCodigoOrgaoDestino().equals(setorDestino.getId())){
					if ( ultimoDeslocamento != null && !ultimoDeslocamento.getCodigoOrgaoDestino().equals(getSetorUsuarioAutenticado().getId()) ) {
						deslocarProcessoApensos(getSetorUsuarioAutenticado().getId(), objetoIncidente, ultimoDeslocamento.getGuia().getTipoOrgaoDestino(), ORGAO_INTERNO, andamento.getId());
					}
					// A procedure oracle JUDICIARIO.PRC_DESLOCA_INCIDENTE_APENSOS
					Thread.sleep(1000);
					if(getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(objetoIncidente).getGuia().getCodigoOrgaoDestino().equals(getSetorUsuarioAutenticado().getId())) {
						deslocarProcessoApensos(setorDestino.getId(), objetoIncidente, ORGAO_INTERNO, ORGAO_INTERNO, andamento.getId());
					}
				}
			}
		} catch (ServiceException e) {
			reportarAviso("Não foi possível fazer o deslocamento do " + objetoIncidente.getIdentificacao() + ". " + e.getCause().getMessage());
		}
	}	
	
	private boolean isProcessoOriginarioOuRecursalComPreferenciaCriminal(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		if(objetoIncidente instanceof Processo){	
			if(!PROCESSOS_RECURSAIS.contains(((Processo)objetoIncidente).getClasseProcessual().getId())){
				return true; //eh originario
			}
			if(PROCESSOS_RECURSAIS.contains(((Processo)objetoIncidente).getClasseProcessual().getId())){
				for(IncidentePreferencia ip : objetoIncidente.getPreferencias()){
					if(ip.getTipoPreferencia().getId().equals(Long.valueOf(TipoPreferenciaContante.CRIMINAL.getCodigo()))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	protected void recuperarObjetoIncidente(ObjetoIncidente objetoIncidente, String identificacaoProcesso) {
		List<ObjetoIncidente<?>> incidentesProcesso = new ArrayList<ObjetoIncidente<?>>();
        incidentesProcesso.addAll(recuperarIncidentes(objetoIncidente.getId()));
        recuperarObjetoIncidente(incidentesProcesso, identificacaoProcesso, objetoIncidente);
	}
	

	protected void recuperarObjetoIncidente(Processo processo, String identificacaoProcesso) {
		List<ObjetoIncidente<?>> incidentesProcesso = new ArrayList<ObjetoIncidente<?>>();
        incidentesProcesso.addAll(recuperarIncidentes(processo.getId()));
        recuperarObjetoIncidente(incidentesProcesso, identificacaoProcesso, processo);
	}
	
	protected void recuperarObjetoIncidente(List<ObjetoIncidente<?>> incidentesProcesso, String identificacaoProcesso, ObjetoIncidente<?> objIncidente) {
//		List<ObjetoIncidente<?>> incidentesProcesso = new ArrayList<ObjetoIncidente<?>>();
//        incidentesProcesso.addAll(recuperarIncidentes(processo.getId()));

		List<ObjetoIncidente<?>> objetosIncidentes = new ArrayList<ObjetoIncidente<?>>(incidentesProcesso);

		if (objetosIncidentes.size() > 1) {
			for (ObjetoIncidente<?> objetoIncidente : objetosIncidentes) {
				if (!objetoIncidente.getIdentificacao().replaceAll("\\s+","").equalsIgnoreCase(identificacaoProcesso.replaceAll("\\s+",""))) {
					incidentesProcesso.remove(objetoIncidente);
				}
			}
		}

		if(incidentesProcesso != null && !incidentesProcesso.isEmpty()){
			if(mapaIncidentes == null){
				mapaIncidentes = new HashMap<Processo, ObjetoIncidente<?>>();
			}
			if(objIncidente instanceof Processo && mapaIncidentes.get(objIncidente) == null){
				mapaIncidentes.put((Processo) objIncidente,incidentesProcesso.get(0));
			}
		}
	}
	
	
	private boolean validarDeslocamento(ObjetoIncidente<?> incidente) throws ServiceException {

		Setor setorPresidencia = getSetorService().recuperarPorId(CODIGO_SETOR_PRESIDENCIA);
			Setor setorVicePresidencia = getSetorService().recuperarPorId(CODIGO_SETOR_VICE_PRESIDENCIA);
			for(ObjetoIncidente<?> inc : incidentes){
				Setor setorDestino = null;
				if(andamentoSelecionado.getId().equals(CONCLUSAO_PRESIDENCIA)){
					setorDestino = setorPresidencia;
				}else if(andamentoSelecionado.getId().equals(CONCLUSAO_VICEPRESIDENCIA)){
					setorDestino = setorVicePresidencia;
				}else if(andamentoSelecionado.getId().equals(CONCLUSAO_RELATOR)){
					Ministro ministroRelatorAtual = ((Processo)incidente).getMinistroRelatorAtual();
					if (ministroRelatorAtual == null)
						reportarErro("Processo " + inc.getIdentificacao() + " sem relator ");
					else{
						setorDestino = ministroRelatorAtual.getSetor();
					}
				}else if(isDeslocarAutomaticamente() && (andamentoSelecionado.getId().equals(CONCLUSAO_REVISOR) || andamentoSelecionado.getId().equals(CONCLUSAO_RELATOR_PARA_ACORDAO))){
					if(getIdMinistroSelecionado() != null && getIdMinistroSelecionado().intValue() > 0){
						Ministro revisor = getMinistroSelecionado();
						if(revisor != null){
							setorDestino = revisor.getSetor();
						}
					}else{
						reportarAviso("Necessário selecionar um ministro.");	
						return false;
					}
				}

				
				if(setorDestino == null){
					feedbacks.add("Não foi possível deslocar o processo " + inc.getIdentificacao() + ". O setor destino não pode ser recuperado.");
					setAtributo(FEEDBACKS, feedbacks);
				}
			}
			
		return true;
	}

	private Ministro getMinistroSelecionado() {
		try {
			return getMinistroService().recuperarPorId(getIdMinistroSelecionado());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public boolean getAndamentoSelecionadoImplicaEmDeslocamentoAutomatico() {		
		return andamentoSelecionado != null && CODIGOS_ANDAMENTOS_IMPLICA_DESLOCAMENTO.contains(andamentoSelecionado.getId());
	}
	
	
	protected void verificaAndamentoSelecionado() {
		Object codigoAndamentoDigitado = getInputAndamento().getValue();
		if(codigoAndamentoDigitado == null || andamentoSelecionado == null || andamentoSelecionado.getId() == null){
			setAndamentoSelecionado(null);
		}else{
			try {
				String[] valorDoInput = ((String) codigoAndamentoDigitado).split("-");
				
				if(!(Long.valueOf((valorDoInput[0]).trim()).equals(andamentoSelecionado.getId()))) {
					andamentoSelecionado = null;
					reportarAviso("O andamento não foi corretamente selecionado.");
					getInputAndamento().setValue(null);
				}
			} catch (Exception e) {
				andamentoSelecionado = null;
				reportarAviso("O andamento não foi corretamente selecionado.");
				getInputAndamento().setValue(null);
			}
		}
	}

	/**
	 * Métodos que devem ser executados ao se pesquisar o processo
	 * 
	 * @param processo
	 * @throws ServiceException
	 */
	protected abstract void doAdicionarProcesso(Processo processo) throws ServiceException;

	public void carregaListaDePartes(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		CabecalhoObjetoIncidente cabecalhoObjIncidente = new CabecalhoObjetoIncidente();
		DadosComunsDoCabecalhoHandler dadosCabecalho = new DadosComunsDoCabecalhoHandler();
		List<SpecParte> listaSpecParteCapitalizada = new ArrayList<SpecParte>();
		// busca o cabeçalho do objeto incidente
		try {
			cabecalhoObjIncidente = getCabecalhoObjetoIncidenteService().recuperarCabecalho(objetoIncidente.getId());
		} catch (ServiceException e) {
			throw new ServiceException("Erro ao recuperar o cabeçalho objeto incidente.");
		}

		if (cabecalhoObjIncidente != null) {

			try {
				dadosCabecalho.setSpecCabecalho(getCabecalhoObjetoIncidenteService().getSpecCabecalho(cabecalhoObjIncidente));
			} catch (ServiceException e) {
				throw new ServiceException("Erro ao montar as informações da parte.");
			}
		}

		if (dadosCabecalho.getSpecCabecalho() != null) {
			if (dadosCabecalho.getSpecCabecalho().getPartes() != null) {
				listaSpecParteCapitalizada = dadosCabecalho.getSpecCabecalho().getPartes();
			}
		}

		if (listaSpecParteCapitalizada != null && listaSpecParteCapitalizada.size() > 0) {
			listaSpecPartes = new ArrayList<SpecParte>();
			for (SpecParte specP : listaSpecParteCapitalizada) {
				SpecParte specTemp = new SpecParte(specP.getTipo().toUpperCase(), specP.getNome().toUpperCase(), specP.getComplemento().toUpperCase());
				specTemp.setCapitalizar(false);
				listaSpecPartes.add(specTemp);
			}
		}

	}

	private void verificarSetorUsuario(Processo processo, Setor setor)
			throws ServiceException {

		try {
			getAndamentoProcessoService().verificarSetorUsuario(processo,
					setor, this);

		} catch (ProcessoOutraRelatoriaException e) {
			setMsgConfirmacaoAndamento(e.getMessage());
			processoOutraRelatoria = true;
		}

	}

	private void verificarMinistroRelatorAposentado(Processo processo) {

		try {
			getAndamentoProcessoService().verificarMinistroRelatorAposentado(
					processo);

		} catch (MinistroRelatorAposentadoException e) {
			setMsgConfirmacaoAndamento(e.getMessage());
			ministroRelatorAposentado = true;
		}
	}

	/**
	 * Métodos
	 */

	protected void limparInformacoesModais() {
		quantidadeApensos = null;
		processoSelecionado = null;
		processosSelecionados = null;
		idOrigemDecisao = null;
		idTipoDevolucao = null;
		idPresidenteInterino = null;
		idOrgao = null;
		idProcedencia = null;
		idOrigemDevolucao = null;
		orgaos = null;
		procedencias = null;
		origensDevolucao = null;
		setListaProcessoTema(null);
		atualizarSessao();
	}

	protected void limparInformacoesAndamentoSelecionado() {
		precisaConfirmacaoLancarAndamento = false;
		mensagemConfirmacaoLancarAndamento = "";
		inputAndamento.setValue("");
		solicitarProcessoEObservacao = false;
		solicitarProcessos = false;
		solicitarTema = false;
		solicitarPeticao = false;
		solicitarOrigemDecisao = false;
		solicitarTipoDevolucao = false;
		solicitarPresidenteInterino = false;
		andamentoSelecionado = null;
		processosSelecionados = null;
		idOrigemDecisao = null;
		idPresidenteInterino = null;
		observacao = "";
		observacaoInterna = "";
		deslocarAutomaticamente = false;
		atualizarSessao();
	}

	protected void verificarAndamento() {
		try {

			AndamentoProcessoService andamentoProcessoService = getAndamentoProcessoService();

			mensagemConfirmacaoLancarAndamento = andamentoProcessoService.getMensagemConfirmacao(andamentoSelecionado);
			setAtributo(MSG_CONFIRMACAO, mensagemConfirmacaoLancarAndamento);
			solicitarProcessoEObservacao = andamentoProcessoService.precisaProcessoPrincipal(andamentoSelecionado) && !isAndamentoSelecionadoRequerTema();
			solicitarProcessos = andamentoProcessoService.precisaProcessosPrincipais(andamentoSelecionado) && !isAndamentoSelecionadoRequerTema();
			solicitarTema = andamentoProcessoService.precisaProcessoPrincipal(andamentoSelecionado) && isAndamentoSelecionadoRequerTema();
			solicitarPeticao = andamentoProcessoService.precisaPeticao(andamentoSelecionado);
			solicitarOrigemDecisao = andamentoProcessoService.precisaOrigemDecisao(andamentoSelecionado,getSetorUsuarioAutenticado());
			carregarOrigensDecisao(andamentoSelecionado);
			solicitarTipoDevolucao = andamentoProcessoService.precisaTipoDevolucao(andamentoSelecionado);
			precisaVerificarCodigoOrigem = andamentoProcessoService.precisaVerificarCodigoOrigem(andamentoSelecionado);
			precisaListarDecisoes = VISTA_A_PEGR_PARA_INTIMACAO.equals(andamentoSelecionado.getId());
			verificarPresidenteInterino(null);

			inputSolicitarProcessoEObservacao.setValue(solicitarProcessoEObservacao);
			inputSolicitarProcessos.setValue(solicitarProcessos);
			inputSolicitarTema.setValue(solicitarTema);
			inputSolicitarPeticao.setValue(solicitarPeticao);
			inputSolicitarOrigemDecisao.setValue(solicitarOrigemDecisao);
			inputSolicitarTipoDevolucao.setValue(solicitarTipoDevolucao);
			inputPrecisaVerificarCodigoOrigem.setValue(precisaVerificarCodigoOrigem);
			inputPrecisaListarDecisoes.setValue(precisaListarDecisoes);

			doVerificarAndamento();

			verificaProcessoDifereSetorUsuario(processo);
			
			if(getAndamentoSelecionadoImplicaEmDeslocamentoAutomatico()){
				setDeslocarAutomaticamente(true);
			}else{
				setDeslocarAutomaticamente(false);
			}

		} catch (Exception e) {
			reportarErro("Erro ao registrar um andamento. ", e.getMessage());

		} finally {
			atualizarSessao();
		}

	}

	private Boolean isAndamentoSelecionadoRequerTema() {		
		return LISTA_ANDAMENTOS_REQUER_TEMA.contains(andamentoSelecionado.getId());
	}

	protected void carregarAndamentosAutorizados() {

		try {
			andamentosAutorizados = doPesquisarAndamentosParaLista();
			setAtributo(ANDAMENTOS, andamentosAutorizados);

		} catch (ServiceException e) {
			reportarErro(e.getMessage());
		}

	}

	protected abstract List<Andamento> doPesquisarAndamentosParaLista()
			throws ServiceException;

	/**
	 * Método que permite que outras verificações sejam feitas no andamento
	 */
	protected void doVerificarAndamento() throws ServiceException {
		//
	}

	public void verificarPresidenteInterino(ActionEvent event) {

		try {
			solicitarPresidenteInterino = getAndamentoProcessoService()
					.precisaPresidenteInterino(idOrigemDecisao,
							getSetorUsuarioAutenticado(), andamentoSelecionado);
			atualizarSessao();
			inputSolicitarPresidenteInterino
					.setValue(solicitarPresidenteInterino);

		} catch (Exception e) {
			reportarErro("Erro ao registrar um andamento. ", e.getMessage());

		}
	}

	protected List<SelectItem> carregarOrigensDecisao(
			Andamento andamentoSelecionado) {

		origensDecisao = new ArrayList<SelectItem>();
		if (andamentoSelecionado != null) {
			try {
				List<OrigemAndamentoDecisao> origens = getAndamentoProcessoService()
						.getOrigensDecisao(andamentoSelecionado);

				origensDecisao.add(new SelectItem(-1, ""));
				for (OrigemAndamentoDecisao origemAndamentoDecisao : origens) {
					origensDecisao.add(new SelectItem(origemAndamentoDecisao
							.getId(), origemAndamentoDecisao.getDescricao()));
				}

			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar origens decisão. ",
						e.getMessage());
			}
		}

		setAtributo(ORIGENS_DECISAO, origensDecisao);
		return origensDecisao;
	}

	protected List<SelectItem> carregarOrgaos() {
		orgaos = new ArrayList<SelectItem>();
		try {
			List<Orgao> orgaosPesquisa = getOrgaoService().pesquisarOrgaosAtivos();

			orgaos.add(new SelectItem(null, ""));
			for (Iterator iterator = orgaosPesquisa.iterator(); iterator.hasNext();) {
				Orgao orgao = (Orgao) iterator.next();
				orgaos.add(new SelectItem(orgao.getId(), orgao.getDescricao()));
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar órgãos.", e.getMessage());
		}

		setAtributo(ORGAOS, orgaos);
		return orgaos;
	}

	protected List<SelectItem> carregarOrigensDevolucao() {
		origensDevolucao = new ArrayList<SelectItem>();

		if (idOrgao != null && idProcedencia != null) {
			try {
				Orgao orgao = getOrgaoService().recuperarPorId(idOrgao);
				Procedencia procedencia = getProcedenciaService().recuperarPorId(idProcedencia);
				List<Origem> origens = getOrigemService().pesquisarOrigensAtivas(orgao, procedencia);
				List<Orgao> orgaos = getOrgaoService().pesquisarOrgaosAtivos();

				origensDevolucao.add(new SelectItem(null, ""));
				for (Iterator iterator = origens.iterator(); iterator.hasNext();) {
					Origem origem = (Origem) iterator.next();
					origensDevolucao.add(new SelectItem(origem.getId(), origem.getDescricao()));
				}
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar origens.", e.getMessage());
			}
		}

		setAtributo(ORIGENS_DEVOLUCAO, origensDevolucao);
		return origensDevolucao;
	}
	protected List<HistoricoProcessoOrigem> carregarOrigensCadastradas(Processo processo) {
		this.processo = processo;
		return carregarOrigensCadastradas();
	}

	protected List<HistoricoProcessoOrigem> carregarOrigensCadastradas() {
		origensCadastradas = new ArrayList<HistoricoProcessoOrigem>();
		if (processo != null) {
			try {
				origensCadastradas = getHistoricoProcessoOrigemService()
						.recuperarTodosPorObjetoIncidente(processo.getId());
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar as origens cadastradas.",
						e.getMessage());
			}
		}
		setAtributo(ORIGENS_CADASTRADAS, origensCadastradas);
		return origensCadastradas;
	}

	protected List<SelectItem> carregarProcedencias() {
		procedencias = new ArrayList<SelectItem>();
		if (idOrgao != null) {
			try {
				Orgao orgao = getOrgaoService().recuperarPorId(idOrgao);
				List<Procedencia> listaProcedencias = getProcedenciaService()
						.pesquisarProcedenciasComOrigemAtiva(orgao);

				procedencias.add(new SelectItem(null, ""));
				for (Iterator iterator = listaProcedencias.iterator(); iterator
						.hasNext();) {
					Procedencia procedencia = (Procedencia) iterator.next();
					procedencias.add(new SelectItem(procedencia.getId(),
							procedencia.getSiglaProcedencia()));
				}
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar procedências.", e.getMessage());
			}
		}
		
		if(procedencias !=null || procedencias.isEmpty()) {
				try {
					
					List<Procedencia> listaProcedencias = getProcedenciaService()
							.pesquisarProcedenciasAtivas();

					procedencias.add(new SelectItem(null, ""));
					for (Iterator iterator = listaProcedencias.iterator(); iterator
							.hasNext();) {
						Procedencia procedencia = (Procedencia) iterator.next();
						procedencias.add(new SelectItem(procedencia.getId(),
								procedencia.getSiglaProcedencia()));
					}
				} catch (ServiceException e) {
					reportarErro("Erro ao pesquisar procedências.", e.getMessage());
				}
			}
		setAtributo(PROCEDENCIAS, procedencias);
		return procedencias;
	}

	public List<Andamento> pesquisaAndamentosSuggestionBox(Object suggest) {

		List<Andamento> andamentos = new ArrayList<Andamento>();

		String codigo = suggest.toString();
		String descricao = "";

		if (suggest.toString().length() == 14) {
			String siglaNumeroTrezeCarac = codigo.substring(0,
					codigo.length() - 1);
			codigo = siglaNumeroTrezeCarac;
		}

		if (codigo.indexOf(" - ") > -1) {
			codigo = suggest.toString().substring(0, codigo.indexOf(" - "));
			descricao = codigo;
		} else {
			descricao = codigo;
		}

		for (Iterator iterator = getAndamentosAutorizados().iterator(); iterator
				.hasNext();) {
			Andamento andamento = (Andamento) iterator.next();

			if (andamento.getId().toString().indexOf(codigo) > -1
					|| StringUtils.retirarAcentos(
							andamento.getDescricao().toUpperCase())
							.indexOf(
									StringUtils.retirarAcentos(descricao
											.toUpperCase())) > -1) {
				andamentos.add(andamento);
			}
		}

		return andamentos;
	}

	public List pesquisaSuggestionBox(Object suggest) {

		String codigo = "";
		codigo = suggest.toString();

		if (codigo.toString().length() == 14) {
			String siglaNumeroTrezeCarac = codigo.substring(0,
					codigo.length() - 1);
			codigo = siglaNumeroTrezeCarac;
		}

		try {
			List<Processo> processos = getProcessoService().pesquisarProcesso(codigo.toUpperCase());
			processos.removeAll(Collections.singletonList(null));
			incidentes = new ArrayList<ObjetoIncidente<?>>();

			for (Processo processo : processos) {
				incidentes.addAll(recuperarIncidentes(processo.getId()));
			}

			List<ObjetoIncidente<?>> objetosIncidentes = new ArrayList<ObjetoIncidente<?>>(incidentes);

			if (processos.size() > 1) {
				for (ObjetoIncidente<?> objetoIncidente : objetosIncidentes) {
					if (!objetoIncidente.getTipoObjetoIncidente().getCodigo().equals(1)) {
						incidentes.remove(objetoIncidente);
					}
				}
			}

			return incidentes;
		} catch (Exception e) {
			reportarErro("Erro na pesquisa processual. ", e.getMessage());
			return new ArrayList<Processo>();
		}
	}
	
	public List pesquisaSuggestionBoxTema(Object suggest) {

		String codigo = "";
		codigo = suggest.toString();

		if (codigo.toString().length() == 14) {
			String siglaNumeroTrezeCarac = codigo.substring(0,
					codigo.length() - 1);
			codigo = siglaNumeroTrezeCarac;
		}

		try {
			List<ProcessoTema> processosTema = getProcessoTemaService().pesquisarProcessoTemaLCase(Long.parseLong(codigo), null, null, null, 1L);
		
			return processosTema;
		} catch (Exception e) {
			reportarErro("Erro na pesquisa processual. ", e.getMessage());
			return new ArrayList<Processo>();
		}
	}	


	public List pesquisaSuggestionBoxProcesso(Object suggest) {

		String codigo = "";
		codigo = suggest.toString();

		if (codigo.toString().length() == 14) {
			String siglaNumeroTrezeCarac = codigo.substring(0,
					codigo.length() - 1);
			codigo = siglaNumeroTrezeCarac;
		}

		try {
			List<Processo> processos = getProcessoService().pesquisarProcesso(codigo.toUpperCase());
			processos.removeAll(Collections.singletonList(null));
			incidentes = new ArrayList<ObjetoIncidente<?>>();

			for (Processo processo : processos) {
				incidentes.addAll(recuperarIncidentes(processo.getId()));
			}

			List<ObjetoIncidente<?>> objetosIncidentes = new ArrayList<ObjetoIncidente<?>>(incidentes);

			if (objetosIncidentes.size() > 1) {
				for (ObjetoIncidente<?> objetoIncidente : objetosIncidentes) {
					if (!objetoIncidente.getTipoObjetoIncidente().getCodigo().equalsIgnoreCase("PR")) {
						incidentes.remove(objetoIncidente);
					}
				}
			}

			return incidentes;
		} catch (Exception e) {
			reportarErro("Erro na pesquisa processual. ", e.getMessage());
			return new ArrayList<Processo>();
		}
	}
	
	private List<SelectItem> carregarTiposDevolucao() {

		ArrayList<SelectItem> tiposDevolucao = new ArrayList<SelectItem>();
		try {
			List<TipoDevolucao> devolucoes = getTipoDevolucaoService()
					.pesquisar(new AscendantOrder("descricao"));

			tiposDevolucao.add(new SelectItem(-1, ""));
			for (TipoDevolucao tipoDevolucao : devolucoes) {
				tiposDevolucao.add(new SelectItem(tipoDevolucao.getId(),
						tipoDevolucao.getDescricao()));
			}

		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar tipos devolução. ", e.getMessage());
		}

		setAtributo(TIPOS_DEVOLUCAO, tiposDevolucao);
		return tiposDevolucao;
	}

	private List<SelectItem> carregarMinistrosPresidenteInterino() {
		ministrosPresidenteInterino = new ArrayList<SelectItem>();

		try {
			List<Ministro> ministros = getMinistroService()
					.pesquisarMinistrosPresidenteInterino();
			ministrosPresidenteInterino.add(new SelectItem(-1, ""));
			for (Ministro ministro : ministros) {
				ministrosPresidenteInterino.add(new SelectItem(
						ministro.getId(), ministro.getNome()));
			}

		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar ministros. ", e.getMessage());
		}

		setAtributo(MINISTROS_PRESIDENTE_INTERINO, ministrosPresidenteInterino);
		return ministrosPresidenteInterino;
	}

	public List<ObjetoIncidente<?>> recuperarIncidentes(Long id) {
		List<ObjetoIncidente<?>> incidentes = Collections.emptyList();
		ObjetoIncidenteService objetoIncidenteService = getObjetoIncidenteService();

		try {
			incidentes = objetoIncidenteService.pesquisar(id, TipoObjetoIncidente.PROCESSO,TipoObjetoIncidente.RECURSO);
		} catch (ServiceException exception) {
			reportarErro(MessageFormat.format("Erro ao recuperar incidentes.",id));
		}

		return incidentes;
	}

	public void inserirOrigem(ActionEvent e) {
		if(idOrigemDevolucao != null) {
			if (verificaDuplicidadeOrigem()) {
				try {
					Origem origem = getOrigemService().recuperarPorId(idOrigemDevolucao);
					Procedencia procedencia = getOrigemService().pesquisarProcedenciaPadrao(origem);
					HistoricoProcessoOrigem hpo = new HistoricoProcessoOrigem();
					hpo.setOrigem(origem);
					if(procedencia != null) {
						hpo.setProcedencia(procedencia);
					}
					hpo.setOrigemEletronica(false);
					hpo.setProcessoInicial(false);
					hpo.setPrincipal(false);
					hpo.setDestinatario(false);
					hpo.setTipoHistorico(TipoHistorico.ORIGEM);
					hpo.setObjetoIncidente(processo.getId());
					getHistoricoProcessoOrigemService().incluir(hpo);
	
					origensCadastradas.add(hpo);
	
					initModalVerificarOrigem();
					
					reportarInformacao("Origem adicionada");

				} catch (ServiceException ex) {
					reportarErro("Erro ao adicionar origem. " + ex.getMessage());
				}
			}else {
				reportarErro("Origem já cadastrada.");				
			}}else {
				reportarAviso("Favor pesquisar e selecionar uma origem (campo origem)");	
			}
	}

	/*
	 * verificar se o setor do processo difere do usuário e colhe as informações
	 * necessários posteriormente no deslocamento. é chamado pelo
	 * verificarAndamento()
	 */
	public void verificaProcessoDifereSetorUsuario(Processo processo)
			throws ServiceException {

		//Parte do principio que esta situacao nao eh satisfeitas.
		setProcessoDifereSetorUsuario(false);
		inputProcessoDifereSetorUsuario.setValue(false);

		// a crítica somente é válida para alguns andamentos específicos de
		// baixa
		if (BAIXA_DEFINITIVA != andamentoSelecionado.getId().intValue() && 
			BAIXA_EXTENA_AUTOS != andamentoSelecionado.getId().intValue() && 
			BAIXA_REMESSA_JUIZO != andamentoSelecionado.getId().intValue() 
			) {
			return;
		}
		// somente processo eletrônico
		if (processo == null || "F".equals(processo.getTipoMeio())) {
			return;
		}
		DeslocaProcesso deslocaProcesso = getDeslocaProcessoService()
				.recuperarUltimoDeslocamentoProcesso(processo);
		Long idSetorProcesso = deslocaProcesso.getCodigoOrgaoDestino();
		Long idSetorUsuario = getSetorUsuarioAutenticado().getId();
		
		if (!idSetorProcesso.equals(idSetorUsuario)) {
			setProcessoDifereSetorUsuario(true);
			inputProcessoDifereSetorUsuario.setValue(true);
		} 
		setAtributo(PROCESSO_DIFERE_SETOR_USUARIO, processoDifereSetorUsuario);
	}
	
	public void alterarPrecisaVerificarCodigoOrigem(){
		this.setPrecisaVerificarCodigoOrigem(!precisaVerificarCodigoOrigem);
		this.atualizarSessao();
	}

	public void confirmarDeslocamento(ActionEvent event) {
		setConfirmaDeslocamento(true);
		setAtributo(CONFIRMA_DESLOCAMENTO, confirmaDeslocamento);
	}

	public void vazio(ActionEvent event) {
	}

	public String getDescricaoAndamento() {
		return descricaoAndamento;
	}

	public void setDescricaoAndamento(String descricaoAndamento) {
		this.descricaoAndamento = descricaoAndamento;
	}

	public HtmlInputHidden getInputSolicitarProcessoEObservacao() {
		return inputSolicitarProcessoEObservacao;
	}

	public void setInputSolicitarProcessoEObservacao(
			HtmlInputHidden inputSolicitarProcessoEObservacao) {
		this.inputSolicitarProcessoEObservacao = inputSolicitarProcessoEObservacao;
	}

	public HtmlInputHidden getInputSolicitarProcessos() {
		return inputSolicitarProcessos;
	}

	public void setInputSolicitarProcessos(
			HtmlInputHidden inputSolicitarProcessos) {
		this.inputSolicitarProcessos = inputSolicitarProcessos;
	}
	
	public HtmlInputHidden getInputSolicitarTema() {
		return inputSolicitarTema;
	}
	
	public void setInputSolicitarTema(HtmlInputHidden inputSolicitarTema) {
		this.inputSolicitarTema = inputSolicitarTema;
	}

	public HtmlInputHidden getInputSolicitarOrigemDecisao() {
		return inputSolicitarOrigemDecisao;
	}

	public void setInputSolicitarOrigemDecisao(
			HtmlInputHidden inputSolicitarOrigemDecisao) {
		this.inputSolicitarOrigemDecisao = inputSolicitarOrigemDecisao;
	}

	public HtmlInputHidden getInputSolicitarTipoDevolucao() {
		return inputSolicitarTipoDevolucao;
	}

	public void setInputSolicitarTipoDevolucao(
			HtmlInputHidden inputSolicitarTipoDevolucao) {
		this.inputSolicitarTipoDevolucao = inputSolicitarTipoDevolucao;
	}

	public HtmlInputHidden getInputSolicitarPeticao() {
		return inputSolicitarPeticao;
	}

	public void setInputSolicitarPeticao(HtmlInputHidden inputSolicitarPeticao) {
		this.inputSolicitarPeticao = inputSolicitarPeticao;
	}

	public HtmlInputHidden getInputPrecisaVerificarCodigoOrigem() {
		return inputPrecisaVerificarCodigoOrigem;
	}

	public void setInputPrecisaVerificarCodigoOrigem(
			HtmlInputHidden inputPrecisaVerificarCodigoOrigem) {
		this.inputPrecisaVerificarCodigoOrigem = inputPrecisaVerificarCodigoOrigem;
	}

	public boolean isPrecisaVerificarCodigoOrigem() {
		return precisaVerificarCodigoOrigem;
	}

	public void setPrecisaVerificarCodigoOrigem(boolean precisaVerificarCodigoOrigem) {
		this.precisaVerificarCodigoOrigem = precisaVerificarCodigoOrigem;
		this.inputPrecisaVerificarCodigoOrigem.setValue(precisaVerificarCodigoOrigem);
	}

	public List<SelectItem> getOrigensDevolucao() {
		return origensDevolucao;
	}

	public void setOrigensDevolucao(List<SelectItem> origensDevolucao) {
		this.origensDevolucao = origensDevolucao;
		setAtributo(ORIGEM_DEVOLUCAO, idOrigemDevolucao);
	}

	public Long getIdOrigemDevolucao() {
		return idOrigemDevolucao;
	}

	public void setIdOrigemDevolucao(Long idOrigemDevolucao) {
		this.idOrigemDevolucao = idOrigemDevolucao;
		setAtributo(ORIGEM_DEVOLUCAO, idOrigemDevolucao);
		carregarProcedencias();
	}

	public List<SelectItem> getProcedencias() {
		return procedencias;
	}

	public void setProcedencias(List<SelectItem> procedencias) {
		this.procedencias = procedencias;
	}

	public Long getIdProcedencia() {
		return idProcedencia;
	}

	public void setIdProcedencia(Long idProcedencia) {
		this.idProcedencia = idProcedencia;
		setAtributo(PROCEDENCIA, idProcedencia);
		if (idProcedencia != null) {
			carregarOrigensDevolucao();
		} else {
			idOrigemDevolucao = null;
			origensDevolucao.clear();
		}
	}

	public List<ObjetoIncidente<?>> getIncidentes() {
		return incidentes;
	}

	public void setIncidentes(List<ObjetoIncidente<?>> incidentes) {
		this.incidentes = incidentes;
	}

	public List<SelectItem> getOrgaos() {
		return orgaos;
	}

	public void setOrgaos(List<SelectItem> orgaos) {
		this.orgaos = orgaos;
	}

	public Long getIdOrgao() {
		return idOrgao;
	}

	public void setIdOrgao(Long idOrgao) {

		this.idOrgao = idOrgao;
		setAtributo(ORGAO, idOrgao);

		if (idOrgao != null) {
			carregarProcedencias();
			idProcedencia = null;
			idOrigemDevolucao = null;
			origensDevolucao.clear();
			atualizarSessao();
		} else {
			initModalVerificarOrigem();
		}
	}

	private void initModalVerificarOrigem() {
		idOrgao = null;
		idProcedencia = null;
		idOrigemDevolucao = null;
		procedencias.clear();
		origensDevolucao.clear();
		numeroOrigem = "";
		siglaOrigem = "";
		atualizarSessao();
	}

	public List<HistoricoProcessoOrigem> getOrigensCadastradas() {
		return origensCadastradas;
	}

	public List<SelectItem> getOrigensCadastradasSelectItens() {

		ArrayList<SelectItem> origensCadastradasSelectItems = new ArrayList<SelectItem>();
		if (origensCadastradas != null) {
			for (int i = 0; i < origensCadastradas.size(); i++) {
				origensCadastradasSelectItems.add(new SelectItem(i, ""));
			}
		}

		return origensCadastradasSelectItems;
	}

	public int getIndexOrigemCadastrada() {
		return tabelaOrigensCadastradas.getRowIndex();
	}

	public void setOrigensCadastradas(
			List<HistoricoProcessoOrigem> origensCadastradas) {
		this.origensCadastradas = origensCadastradas;
	}

	public String getIsOrigemIntegrada() {
		HistoricoProcessoOrigem historicoOrigem = (HistoricoProcessoOrigem) tabelaOrigensCadastradas
				.getRowData();

		try {
			Boolean isIntegrada = getOrigemService().isOrigemIntegrada(
					historicoOrigem.getOrigem());
			return isIntegrada ? "Sim" : "Não";

		} catch (NullPointerException e) {
			reportarErro("Erro ao verificar se a origem está integrada: Processo não possui origem.");
			return null;
		} catch (ServiceException e) {
			reportarErro("Erro ao verificar se a origem está integrada: "
					+ e.getMessage());
			return null;
		}
	}

	/**
	 * Marca uma origem como destinatário e o resto das origens como remetente
	 * Atualizado em 10.09.2015 
	 * @param historicoOrigem
	 */
	public void escolherOrigemPrincipal(HistoricoProcessoOrigem historicoOrigem) {
		try {
			historicoOrigem.setDestinatario(true);
			getHistoricoProcessoOrigemService().alterar(historicoOrigem);

			// Setar as outras origens como não destinatário.
			for (HistoricoProcessoOrigem origem : origensCadastradas) {
				if (!origem.getId().equals(historicoOrigem.getId())) {
					origem.setDestinatario(false);
					getHistoricoProcessoOrigemService().alterar(origem);
				}
			}

		} catch (ServiceException e1) {
			reportarErro("Erro ao selecionar a origem como destinatário.");
		}
	}

	public void escolherOrigemPrincipal(ActionEvent e) {
		HistoricoProcessoOrigem historicoOrigemSelecionado = (HistoricoProcessoOrigem) tabelaOrigensCadastradas
				.getRowData();
		escolherOrigemPrincipal(historicoOrigemSelecionado);
	}

	public boolean getUnicaBaixa() throws ServiceException {
		try {
			if (tabelaOrigensCadastradas != null) {
				HistoricoProcessoOrigem historicoProcessoOrigem = origensCadastradas
						.get(origemSelecionada);
				if (historicoProcessoOrigem.getOrigem() != null) {
					if (getDeslocaProcessoService().isBaixadoParaOrigem(
							processo, historicoProcessoOrigem.getOrigem(),
							andamentoSelecionado)) {
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			return true;
		}
	}

	public boolean getTemOrigemSelecionada() {

		return origemSelecionada != null;
	}

	public boolean getVerificarNumeroUnico() {
		if (processo != null)
			return getProcessoService().validarNumeroUnico(numeroUnicoProcesso);
		else
			return false;
	}

	public boolean getVerificaNumUnico() {
		if (origemSelecionada != null) {
			return !StringUtils.isVazia(processo.getNumeroUnicoProcesso());
		}
		return false;

	}

	public boolean getVerificaNumOrigem() {
		if (origemSelecionada != null) {
			HistoricoProcessoOrigem historicoProcessoOrigem = origensCadastradas
					.get(origemSelecionada);

			return !StringUtils.isVazia(historicoProcessoOrigem
					.getNumeroProcessoOrigem());
		}
		return false;
	}

	public boolean verificaOrigemModificadaParaSTJ() throws ServiceException {
		System.out.println(" -------------->verificaOrigemModificadaParaSTJ()");
		if (origemSelecionada != null) {
			HistoricoProcessoOrigem historicoProcessoOrigem = origensCadastradas
					.get(origemSelecionada);
			if (historicoProcessoOrigem.getOrigem() == null) {
				reportarAviso("Não existe origem para este processo");
				comportamentoBotao(false, "idBotaoValidaBaixa", true);
				return false;
			}
			if (historicoProcessoOrigem.getOrigem().getId().equals(new Long(7))
					|| historicoProcessoOrigem.getOrigem().getId()
							.equals(new Long(5186))) {
				HistoricoProcessoOrigem historicoRecuperado = getHistoricoProcessoOrigemService()
						.recuperarOrigemInicialSTJ(
								historicoProcessoOrigem.getObjetoIncidente());
				if (historicoRecuperado.getSiglaClasseOrigem() == null
						|| historicoRecuperado.getSiglaClasseOrigem().trim()
								.equals("")
						|| historicoRecuperado.getNumeroProcessoOrigem().trim()
								.equals("")) {
					comportamentoBotao(true, "idBotaoValidaBaixa", true);
					return true;
				}
				if (!(historicoRecuperado.getSiglaClasseOrigem().equals(
						historicoProcessoOrigem.getSiglaClasseOrigem())
						&& historicoRecuperado.getNumeroProcessoOrigem()
								.equals(historicoProcessoOrigem
										.getNumeroProcessoOrigem()) && historicoRecuperado
						.getProcedencia()
						.getSiglaProcedencia()
						.equals(historicoProcessoOrigem.getProcedencia()
								.getSiglaProcedencia()))) {
					reportarAviso("Não é permitida baixa para origem diferente da origem enviada eletronicamente pelo STJ");
					comportamentoBotao(false, "idBotaoValidaBaixa", true);
					return false;
				}

			} else {
				comportamentoBotao(true, "idBotaoValidaBaixa", true);
				return true;
			}
		}
		comportamentoBotao(true, "idBotaoValidaBaixa", true);
		return false;
	}

	// habilita ou desabilita botões
	private void comportamentoBotao(boolean habilitar, String id,
			boolean estendido) {
		try {
			int i = 0;
			UIComponent form = FacesContext.getCurrentInstance().getViewRoot()
					.findComponent("formModal");
			while (form.findComponent(id) == null) {
				form = form.getParent();
				i++;
				if (i > 20) {
					throw new Exception(
							"Componente de interface não localizado");
				}
			}
			if (habilitar) {
				form.findComponent(id).getAttributes().put("disabled", false);
				form.findComponent(id)
						.getAttributes()
						.put("styleClass",
								estendido ? "BotaoPadraoEstendido"
										: "BotaoPadrao");
			} else {
				form.findComponent(id).getAttributes().put("disabled", true);
				form.findComponent(id)
						.getAttributes()
						.put("styleClass",
								estendido ? "BotaoPadraoEstendidoInativo"
										: "BotaoPadraoInativo");
			}
		} catch (Exception e) {
			reportarErro("Erro ao recuperar componente de interface");
		}
	}

	public boolean verificaDuplicidadeOrigem() {
		for (Iterator iterator = origensCadastradas.iterator(); iterator
				.hasNext();) {
			HistoricoProcessoOrigem hpo = (HistoricoProcessoOrigem) iterator
					.next();
			if (hpo.getOrigem() != null && hpo.getOrigem().getId().equals(idOrigemDevolucao)
					&& hpo.getProcedencia().getId().equals(idProcedencia))
				return false;
		}
		return true;
	}

	public boolean getOrigemEstaIntegrada() {

		if (origemSelecionada != null) {
			HistoricoProcessoOrigem historicoProcessoOrigem = origensCadastradas
					.get(origemSelecionada);

			try {
				return getOrigemService().isOrigemIntegrada(
						historicoProcessoOrigem.getOrigem());

			} catch (ServiceException e) {
				reportarErro("Erro ao verificar integração da origem: "
						+ e.getMessage());
				return false;
			}
		} else {
			return false;
		}
	}

	public void excluirOrigem(ActionEvent e) {
		HistoricoProcessoOrigem historicoOrigem = (HistoricoProcessoOrigem) tabelaOrigensCadastradas
				.getRowData();

		try {
			getHistoricoProcessoOrigemService().excluir(historicoOrigem);
			origensCadastradas.remove(historicoOrigem);
		} catch (ServiceException ex) {
			reportarErro("Erro ao excluir origem: " + ex.getMessage());
		}
	}

	public void excluirProcessoSelecionado(ActionEvent e) {
		Processo processo = (Processo) tabelaProcessosSelecionados.getRowData();

		processosSelecionados.remove(processo);
	}

	public String getIsOrigemPrincipal() {
		HistoricoProcessoOrigem historicoOrigem = (HistoricoProcessoOrigem) tabelaOrigensCadastradas
				.getRowData();

		Boolean isPrincipal = historicoOrigem.getPrincipal();
		return (isPrincipal == null || !isPrincipal) ? "Não" : "Sim";
	}
	
	public String getIsOrigemDestinatario() {
		HistoricoProcessoOrigem historicoOrigem = (HistoricoProcessoOrigem) tabelaOrigensCadastradas
				.getRowData();

		Boolean isDestinatario = historicoOrigem.getDestinatario();
		return (isDestinatario == null || !isDestinatario) ? "Não" : "Sim";
	}
	
	public String getStyleSimNao(){
		HistoricoProcessoOrigem historicoOrigem = (HistoricoProcessoOrigem) tabelaOrigensCadastradas
				.getRowData();
		Boolean isDestinatario = historicoOrigem.getDestinatario();
		return (isDestinatario==null || !historicoOrigem.getDestinatario()?"":"color:red");
	}

	public HtmlDataTable getTabelaOrigensCadastradas() {
		return tabelaOrigensCadastradas;
	}

	public void setTabelaOrigensCadastradas(
			HtmlDataTable tabelaOrigensCadastradas) {
		this.tabelaOrigensCadastradas = tabelaOrigensCadastradas;
	}

	public List<SpecParte> getListaSpecPartes() {
		return listaSpecPartes;
	}

	public void setListaSpecPartes(List<SpecParte> listaSpecPartes) {
		this.listaSpecPartes = listaSpecPartes;
	}

	public Integer getOrigemSelecionada() {
		return origemSelecionada;
	}

	public void setOrigemSelecionada(Integer origemSelecionada) {
		System.out.println("origemSelecionada --------------> " + origemSelecionada);
		this.origemSelecionada = origemSelecionada;
		setAtributo(ORIGEM_SELECIONADA, origemSelecionada);
		atualizarSessao();
	}

	public String getNumeroOrigem() {
		return numeroOrigem;
	}

	public void setNumeroOrigem(String numeroOrigem) {
		this.numeroOrigem = numeroOrigem;
	}

	public String getSiglaOrigem() {
		return siglaOrigem;
	}

	public void setSiglaOrigem(String siglaOrigem) {
		this.siglaOrigem = siglaOrigem;
	}

	public HtmlDataTable getTabelaProcessosSelecionados() {
		return tabelaProcessosSelecionados;
	}

	public void setTabelaProcessosSelecionados(
			HtmlDataTable tabelaProcessosSelecionados) {
		this.tabelaProcessosSelecionados = tabelaProcessosSelecionados;
	}

	public String getNumeroUnicoProcesso() {
		if (numeroUnicoProcesso == null && processo != null)
			return processo.getNumeroUnicoProcesso();
		return numeroUnicoProcesso;
	}

	public void setNumeroUnicoProcesso(String numeroUnicoProcesso) {
		this.numeroUnicoProcesso = numeroUnicoProcesso;
	}

	public HtmlInputHidden getInputProcessoFindo() {
		return inputProcessoFindo;
	}

	public void setInputProcessoFindo(HtmlInputHidden inputProcessoFindo) {
		this.inputProcessoFindo = inputProcessoFindo;
	}

	public boolean isProcessoFindoLista() {
		if (processosSelecionados != null && processosSelecionados.size() > 0) {
			for (Processo processo : processosSelecionados) {
				try {
					if (getProcessoService().isProcessoFindo(processo))
						return true;
				} catch (ServiceException e) {
					reportarErro(e.getMessage());
				}
			}
		}
		return false;
	}

	public void setProcessoFindoLista(boolean processoFindoLista) {
		this.processoFindoLista = processoFindoLista;
	}

	public boolean isProcessoDifereSetorUsuario() {
		return processoDifereSetorUsuario;
	}

	public void setProcessoDifereSetorUsuario(boolean processoDifereSetorUsuario) {
		this.processoDifereSetorUsuario = processoDifereSetorUsuario;
	}

	public HtmlInputHidden getInputProcessoDifereSetorUsuario() {
		return inputProcessoDifereSetorUsuario;
	}

	public void setInputProcessoDifereSetorUsuario(
			HtmlInputHidden inputProcessoDifereSetorUsuario) {
		this.inputProcessoDifereSetorUsuario = inputProcessoDifereSetorUsuario;
	}

	public boolean isConfirmaDeslocamento() {
		return confirmaDeslocamento;
	}

	public void setConfirmaDeslocamento(boolean confirmaDeslocamento) {
		this.confirmaDeslocamento = confirmaDeslocamento;
	}

	public HtmlInputHidden getInputSucesso() {
		return inputSucesso;
	}

	public void setInputSucesso(HtmlInputHidden inputSucesso) {
		this.inputSucesso = inputSucesso;
	}

	public HtmlInputHidden getInputTemCertidao() {
		return inputTemCertidao;
	}

	public void setInputTemCertidao(HtmlInputHidden inputTemCertidao) {
		this.inputTemCertidao = inputTemCertidao;
	}

	public boolean isDeslocarAutomaticamente() {
		return deslocarAutomaticamente;
	}

	public void setDeslocarAutomaticamente(boolean deslocarAutomaticamente) {
		this.deslocarAutomaticamente = deslocarAutomaticamente;
	}
	
	public boolean getPodeDeslocarAutomaticamente(){
		return getPodeDeslocarAutomaticamente(incidenteSelecionado);
	}
	
	public boolean getPodeDeslocarAutomaticamente(ObjetoIncidente<?> objetoIncidente){
		return getAndamentoSelecionadoImplicaEmDeslocamentoAutomatico() 
				&& objetoIncidenteNaSecaoDoUsuario(objetoIncidente) 
				&& objetoIncidente.getEletronico()				
				&& destinoProcessoDiversoAoDoUsuario(objetoIncidente);
	}

	private boolean destinoProcessoDiversoAoDoUsuario(ObjetoIncidente<?> objetoIncidente) {
		try {
			Setor destino = paraOndeDeslocar(objetoIncidente);
			if(destino != null){
				return !getSetorUsuarioAutenticado().getId().equals(destino.getId());
			}
			if(andamentoSelecionado.getId().equals(CONCLUSAO_REVISOR) || andamentoSelecionado.getId().equals(CONCLUSAO_RELATOR_PARA_ACORDAO)){
				return true;
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean objetoIncidenteNaSecaoDoUsuario(ObjetoIncidente objetoIncidente) {
		try {
			if(objetoIncidente instanceof Processo){
				Long idSetorProcesso = getDeslocaProcessoService().pesquisarSetorUltimoDeslocamento((Processo) objetoIncidente);
				if(idSetorProcesso != null){
					if(idSetorProcesso.equals(getSetorUsuarioAutenticado().getId())){	
						return true;
					}
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean getMostrarMensagemProcessoEmSetorDiverso(){
		if(incidenteSelecionado != null){
			return !objetoIncidenteNaSecaoDoUsuario(incidenteSelecionado.getPrincipal());
		}
		return false;
	}

	public List<SelectItem> getMinistros() {
		return ministros;
	}

	public void setMinistros(List<SelectItem> ministros) {
		this.ministros = ministros;
	}
	
	public boolean getSelecionadoAndamento8204ou8238(){
		return getIncidenteSelecionado() != null 
			&& getIncidenteSelecionado().getEletronico() 
			&& getAndamentoSelecionado() != null 
			&& (andamentoSelecionado.getId().equals(CONCLUSAO_REVISOR) || andamentoSelecionado.getId().equals(CONCLUSAO_RELATOR_PARA_ACORDAO));
	}
	
	@SuppressWarnings("unchecked")
	private List<SelectItem> carregarListaMinistros() {

		ArrayList<SelectItem> listaMinistros = new ArrayList<SelectItem>();
		try {
			List<Ministro> ministros = getMinistroService().pesquisarMinistrosAtivos();
			Collections.sort(ministros, new NomeMinistroCrescente());
			
			listaMinistros.add(new SelectItem(-1, "Selecione"));
			for (Ministro min : ministros) {
				listaMinistros.add(new SelectItem(min.getId(), min.getNome()));
			}
			
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar ministros ativos. ", e.getMessage());
		}

		setAtributo(MINISTROS, listaMinistros);
		return listaMinistros;
	}
	
	public Long getIdMinistroSelecionado() {
		return idMinistroSelecionado;
	}
	
	public void setIdMinistroSelecionado(Long idMinistroSelecionado) {
		this.idMinistroSelecionado = idMinistroSelecionado;
	}
	
	private static class NomeMinistroCrescente implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			Ministro m1 = (Ministro) o1;
			Ministro m2 = (Ministro) o2;
			return m1.getNome().compareToIgnoreCase(m2.getNome());
		}
	}
	
	public Long getNumeroTema() {
		return numeroTema;
	}
	
	public void setNumeroTema(Long numeroTema) {
		this.numeroTema = numeroTema;
	}

	public ProcessoTema getProcessoTemaSelecionado() {
		return processoTemaSelecionado;
	}

	@SuppressWarnings("unchecked")
	public void setProcessoTemaSelecionado(ProcessoTema processoTemaSelecionado) {
		listaProcessoTema = (List<ProcessoTema>) getAtributo(PROCESSOS_TEMAS_SELECIONADOS);
		if(listaProcessoTema == null){
			listaProcessoTema = new ArrayList<ProcessoTema>();
		}
		if(!listaProcessoTema.contains(processoTemaSelecionado)){
			listaProcessoTema.add(processoTemaSelecionado);
		}
		
		setListaProcessoTema(listaProcessoTema);
	}

	public List<ProcessoTema> getListaProcessoTema() {
		return listaProcessoTema;
	}

	public void setListaProcessoTema(List<ProcessoTema> listaProcessoTema) {
		setAtributo(PROCESSOS_TEMAS_SELECIONADOS, listaProcessoTema);
		this.listaProcessoTema = listaProcessoTema;
	}
	
	public int getQuantidadeProcessosTemasSelecionados(){
		if(getListaProcessoTema() != null){
			return getListaProcessoTema().size();
		}
		return 0;
	}
	
	public void setQuantidadeProcessosTemasSelecionados(int valor){
		
	}
	
	public void removerTema(ActionEvent evt) throws ServiceException {
		ProcessoTema processoTema = (ProcessoTema) tabelaProcessoTema.getRowData();
		listaProcessoTema.remove(processoTema);
		setAtributo(PROCESSOS_TEMAS_SELECIONADOS, listaProcessoTema);
	}

	public HtmlDataTable getTabelaProcessoTema() {
		return tabelaProcessoTema;
	}

	public void setTabelaProcessoTema(HtmlDataTable tabelaProcessoTema) {
		this.tabelaProcessoTema = tabelaProcessoTema;
	}	
	
	protected boolean isConsiderarIncidentesComoProcessosPrincipais() {		
		return LISTA_ANDAMENTOS_REQUER_PROCESSOS_PRINCIPAIS.contains(andamentoSelecionado.getId());
	}

	public void gerarCertidaoBaixa(Processo processo, AndamentoProcessoInfoImpl andamentoProcessoInfo) throws ServiceException, Exception {
		try {
			String idProcesso = processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual();
			String descOrigem = "";
			String titulo = "";
			String corpo = "";
			String siglaTipoPeca = "";
			String nomeSecretario = NOME_SECRETARIO;
			String descricaoCargo = DESCRICAO_CARGO;
			
			if (andamentoProcessoInfo.getOrigem() != null) {
				descOrigem = andamentoProcessoInfo.getOrigem().getDescricao();
			}
			
			if (andamentoProcessoInfo.getAndamento().getId().equals(7104L)) {
				titulo = "TERMO DE BAIXA DEFINITIVA";
				corpo = "Faço a baixa deste processo e a transmissão eletrônica das peças processuais ao (à) " + descOrigem + ".";
				siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_TERMO_BAIXA;
			} else if (andamentoProcessoInfo.getAndamento().getId().equals(7101L) || andamentoProcessoInfo.getAndamento().getId().equals(7108L)) {
				titulo = "TERMO DE REMESSA EXTERNA";
				corpo = "Faço a remessa destes autos com a transmissão eletrônica das peças processuais ao (à) " + descOrigem + ".";
				siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_TERMO_REMESSA;
			}
			if (siglaTipoPeca.equals("")) {
				throw new ServiceException("Não foi possível recuperar a sigla do tipo da peça.");
			}
			
			ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteService().recuperarPorId(processo.getId()); 
			Setor setor	= getSetorUsuarioAutenticado();
			
			// Recuperar o caminho do brasao
			String pathBrasao = getProcessamentoRelatorioService().recuperarPathImagens("images/brasao.gif"); 
			String pathAssinatura = getProcessamentoRelatorioService().recuperarPathImagens("images/assinatura_secretario_judiciario.JPG"); 
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PATH_BRASAO", pathBrasao);
			params.put("PATH_ASSINATURA", pathAssinatura);
			params.put("ID_PROCESSO", idProcesso);
			params.put("TITULO", titulo);
			params.put("CORPO", corpo);
			params.put("NOME_SECRETARIO", nomeSecretario);
			params.put("DESCRICAO_CARGO", descricaoCargo);
	
			String relatorio = "relatorios/RelatorioTermoBaixa.jasper";
			System.out.println("relatorio:"+relatorio);
			System.out.println("PATH_BRASAO:"+params.get("PATH_BRASAO"));
			System.out.println("PATH_ASSINATURA:"+params.get("PATH_ASSINATURA"));
			System.out.println("ID_PROCESSO:"+params.get("ID_PROCESSO"));
			System.out.println("TITULO:"+params.get("TITULO"));
			System.out.println("CORPO:"+params.get("CORPO"));
			System.out.println("NOME_SECRETARIO:"+params.get("NOME_SECRETARIO"));
			System.out.println("DESCRICAO_CARGO:"+params.get("DESCRICAO_CARGO"));
			byte[] resultRel = UtilJasperReports.criarRelatorioPdf(relatorio, null, params);
			
			String nomeArq = "TERMO_BAIXA_"+idProcesso.replaceAll(" ", "") + "_";
			
			if (resultRel!=null && andamentoProcessoInfo !=null){
				// Salvar registros no banco
				AndamentoProcesso andamentoProcesso = getAndamentoProcessoService().salvarAndamento(andamentoProcessoInfo, processo, incidenteSelecionado);
				getArquivoProcessoEletronicoService().salvarPecaEletronica(resultRel, siglaTipoPeca, objetoIncidente, setor, andamentoProcesso);
				nomePDFBaixa = getProcessamentoRelatorioService().gerarPDF(nomeArq, resultRel);
				andamentoProcesso.setListaTextoAndamentoProcessos(getTextoAndamentoProcessoService().recuperarTextoAndamentoProcesso(andamentoProcesso.getId(),null));
				setAndamentoProcessoGerado(andamentoProcesso);
				
	            if (isNumeroDeUsuariosDiferente1()) {
	                posRegistrarAndamento(andamentoProcesso, "Andamento lançado com sucesso! Nenhuma comunicação foi gerada pois a origem possui um número de usuários diferente de 1.");
	                setAtributo(NUMERO_USUARIOS_DIFERENTE_1, true);
	            } else {
	                posRegistrarAndamento(andamentoProcesso);
	            }
			
			} else{
				throw new ServiceException("Ocorreu um erro ao montar o Termo de Baixa/Remessa!");
			}
		} catch (NullPointerException e) {
			throw new ServiceException("Ocorreu um erro ao montar o Termo de Baixa/Remessa!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			throw new ServiceException("Ocorreu um erro ao montar o Termo de Baixa/Remessa!", e);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public AndamentoProcesso getAndamentoProcessoGerado() {
		return andamentoProcessoGerado;
	}

	public void setAndamentoProcessoGerado(AndamentoProcesso andamentoProcessoGerado) {
		this.andamentoProcessoGerado = andamentoProcessoGerado;
	}
	
	public String getNomePDFBaixa() {
		return nomePDFBaixa;
	}

	public void setNomePDFBaixa(String nomePDFBaixa) {
		this.nomePDFBaixa = nomePDFBaixa;
	}

	public boolean isNumeroDeUsuariosDiferente1() {
		return numeroDeUsuariosDiferente1;
	}

	public void setNumeroDeUsuariosDiferente1(boolean numeroDeUsuariosDiferente1) {
		this.numeroDeUsuariosDiferente1 = numeroDeUsuariosDiferente1;
	}
	
	public boolean getRenderCheckBoxEditarDestinoBaixa(){
		return  (renderCheckBoxEditarDestinoBaixa && andamentoSelecionado != null && BeanRegistrarAndamentoParaVariosProcessos.CODIGOS_BAIXA_REMESSA.contains(andamentoSelecionado.getId()));
	}
	
	public abstract void posRegistrarAndamento(AndamentoProcesso andamentoProcesso) throws ServiceException;
	public abstract void posRegistrarAndamento(AndamentoProcesso andamentoProcesso, String mensagem) throws ServiceException;
	
	public HtmlInputHidden getInputPrecisaListarDecisoes() {
		return inputPrecisaListarDecisoes;
	}

	public void setInputPrecisaListarDecisoes(
			HtmlInputHidden inputPrecisaListarDecisoes) {
		this.inputPrecisaListarDecisoes = inputPrecisaListarDecisoes;
	}

	public boolean isPrecisaListarDecisoes() {
		return precisaListarDecisoes;
	}

	public void setPrecisaListarDecisoes(boolean precisaListarDecisoes) {
		this.precisaListarDecisoes = precisaListarDecisoes;
	}

	public Integer getPecaSelecionada() {
		return pecaSelecionada;
	}

	public void setPecaSelecionada(Integer pecaSelecionada) {
		this.pecaSelecionada = pecaSelecionada;
	}
}
