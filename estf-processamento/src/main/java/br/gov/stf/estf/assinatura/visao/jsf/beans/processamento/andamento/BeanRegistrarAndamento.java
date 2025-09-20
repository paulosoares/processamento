package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.icu.util.Calendar;

import br.gov.stf.estf.assinatura.deslocamento.origemdestino.ResultSuggestionOrigemDestino;
import br.gov.stf.estf.assinatura.relatorio.service.impl.ProcessamentoRelatorioServiceLocal;
import br.gov.stf.estf.assinatura.stficp.RequisicaoAssinaturaDocumentoComunicacao;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ambiente.BeanAmbiente;
import br.gov.stf.estf.assinatura.visao.util.InfoPecaVinculadoAndamentoDTO;
import br.gov.stf.estf.assinatura.visao.util.externo.SistemasExternosUtils;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.ControleVotoDto;
import br.gov.stf.estf.documento.model.util.ResultadoControleVotoPDF;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.judiciario.NumeroProcesso;
import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia.TipoOcorrenciaConstante;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia.TipoProcessoDependenciaEnum;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.ProcessoDependenciaUrlExterna;
import br.gov.stf.estf.entidade.processostf.SituacaoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.estf.localizacao.model.service.OrigemService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.service.ProcessoException;
import br.gov.stf.estf.processostf.model.service.exception.AIApensadoNaoREException;
import br.gov.stf.estf.processostf.model.service.exception.AndamentoNaoAutorizadoException;
import br.gov.stf.estf.processostf.model.service.exception.DesapensadoException;
import br.gov.stf.estf.processostf.model.service.exception.LancamentoIndevidoException;
import br.gov.stf.estf.processostf.model.service.exception.ProcessoApensadoAOutroException;
import br.gov.stf.estf.processostf.model.service.exception.ValidationException;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfoImpl;
import br.gov.stf.estf.processostf.model.util.IdentificacaoProcessoResolver;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.EqualCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.SearchCriterion;
import br.gov.stf.framework.model.service.ServiceException;

public class BeanRegistrarAndamento extends AbstractBeanRegistrarAndamento {
		
	private static final String SIM = "Sim";
	private static final String NAO = "Não";

	private static final Object PROCESSO = new Object();

	private static final Object PETICAO_SELECIONADA = new Object();
	private static final Object NUMERO_PECAS = new Object();
	private static final Object ANDAMENTOS_PROCESSO = new Object();
	private static final Object ANDAMENTOS_PROCESSO_TOTAL = new Object();
	private static final Object PERMITE_ANDAMENTO = new Object();
	private static final Object ANDAMENTO_PROCESSO_SELECIONADO = new Object();
	private static final Object AUTORIZADO_LANCAR_ANDAMENTO_INDEVIDO = new Object();
	private static final Object LANCAMENTO_INDEVIDO_EXCEPTION = new Object();
	private static final Object LISTAS_PECAS_DTO = new Object();
	private static final Object LISTA_ORIGINAL = new Object();

	private static final Object DATA_AUTUACAO = new Object();
	private static final Object PROCEDENCIA = new Object();
	private static final Object MIDIA = new Object();
	private static final Object CODIGO_ANDAMENTO_SELECIONADO = new Object();
	private static final Object SUCESSO = new Object();
	private static final Object TEM_CERTIDAO = new Object();
	private static final Object ANDAMENTO_PROCESSO_GERADO = new Object();
	private static final Object PECA_SELECIONADA = new Object();
	private static final Object PECAS_EXPANDIDAS = new Object();
	
    private static final Object COMUNICACAO = new Object();
    
	public static final Object PROCESSO_INCIDENTE_TRANS_BEAN = new Object();
	
	private static final Object NOME_PDF_BAIXA = new Object();

	private static final int NUMERO_DE_ANDAMENTOS_PROCESSO_VISIVEIS = 10;
	@SuppressWarnings("unused")
	private static final String NOME_SECRETARIO = "Patrícia Pereira de Moura Martins";
	@SuppressWarnings("unused")
	private static final String DESCRICAO_CARGO = "Secretária Judiciária";
	private static final String SISTEMA_PROCESSAMENTO = "PROCESSAMENTO";
	private static final String LISTA_ANDAMENTOS_REMESSA = "mnidois.remessa.andamento.lista";
	private static final String ENDERECO_REMESSA_MNIDOIS = "mnidois.fila.enviar.manifestacao";
	private static final String MSG_COMUNICAO_REQUISAO_PROCESSO = "De ordem, a Secretaria Judiciária requisita o retorno dos autos ao STF";
	private static final String MSG_COMUNICAO_INDEVIDO_VISTA = "Notificação de Lançamento Indevido em Vista à PGR";
	private static final String MSG_COMUNICAO_INDEVIDO_VISTA_AGU = "Notificação de Lançamento Indevido em Vista ao AGU";
	private static final String MSG_COMUNICAO_INDEVIDO_VISTA_INTIMACAO = "Notificação de Lançamento Indevido em Vista à PGR Para Fins de Intimação";
	private static final String MSG_COMUNICAO_INDEVIDO_AUTOS_DISP = "Notificação de Lançamento Indevido em Autos disponibilizados à autoridade policial";
	private static final int ORGAO_EXTERNO = 3;
	private static final int ORGAO_INTERNO = 2;
	private static final Long ANDAMENTO_VISTA_AGU = 8302L;
	private static final Long ANDAMENTO_VISTAPGR = 8303L;
	private static final Long ANDAMENTO_AUTOS_DISPONIBILIZADOS = 8561L;
	private static final Long AUTOS_REQUISITADOSPGR = 8541L;
	private static final Long AUTOS_REQUISITADOS_AGU = 8558L;
	private static final Long ANDAMENTO_VISTAPGR_INTIMACAO = 8507L;
	private static final Long ANDAMENTO_INDEVIDO = 7700L;
	private static final int LIMITE_LISTA_PECAS = 3;	
	private static final Long ANDAMENTO_VISTA_AO_MINISTRO = 8304L;
	private static final Long ANDAMENTO_CANCELAMENTO_PROTOCOLO = 7116L;
	
	
	private static final String MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO = "MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO";
	private static final Object PROCESSO_INATIVO = new Object();
	private static final Object ACORDAOS_PENDENTES_DE_PUBLICACAO = new Object(); 

	private static final String TIPO_MEIO_FISICO = "F";

	private static final long serialVersionUID = 1L;
	private String peticaoSelecionada;
	private AndamentoProcesso andamentoProcessoSelecionado;
	private TextoAndamentoProcesso textoAndamentoProcesso;

	private BeanAmbiente beanAmbiente;
	private String procedencia;

	private String dataAutuacao;
	private String midia;
	private boolean permiteAndamento;
	private Integer numeroPecas;
	private List<AndamentoProcesso> andamentosProcesso;
	private List<AndamentoProcesso> andamentosProcessoTotal;
	private List<PecaProcessoEletronico> listaPecasProcessoEletronico;
	private HtmlDataTable tabelaAndamentos;
	ArrayList<SelectItem> pecasSelectItems = new ArrayList<SelectItem>();

    private static final Object NUMERO_USUARIOS_DIFERENTE_1 = new Object();

    private String msgLancamentoIndevidoException;
	private Boolean setorAutorizadoLancarAndamentoIndevido;
	private boolean lancamentoIndevidoException = false;
	
	private List<ProcessoDependencia> apensos;
	private List<ProcessoDependencia> apensadosAo;
	private List<Processo> processosApenso = new ArrayList<Processo>();
	private List<ProcessoDependenciaUrlExterna> urlExternas;
	private List<ProcessoDependenciaUrlExterna> urlExternasAo;
	private boolean cancelarLancamentoIndevido = false;
	private boolean contemPecasAndamento = false;
	
	private List<TextoAndamentoProcesso> textoAndamentoProcessos;
	private ArquivoEletronico arquivoEletronico;
	private DocumentoEletronico documentoEletronico;
	
	private Comunicacao comunicacao;
	
	private Boolean obsInternaAtualizadaPorOutroUsuario;
	private Long codigoAndamentoSelecionado;
	private Long codigoAndamentoSelecionadoParaCertidaoDeBaixa;
	private boolean sucesso = false;
	private boolean temCertidao = false;
	private boolean listaPecasExpandida = false ;
	
	private boolean lancarAndamentoEAssinarDocumento = false;

	private List<PecaDTO> listaPecasDTO;

	List<PecaProcessoEletronico> listaOriginal = new ArrayList<PecaProcessoEletronico>();	
	
	@Autowired
	DeslocaProcessoService deslocaProcessoService;
	
	@Autowired
	ProcessoDependenciaService processoDependenciaService;
	
	@Autowired
	private OrigemService origemService;
	
	private boolean processoInativo = false;
	
	private boolean existemAcordaosPendentesDePublicacao = false;
	
	private String mensagemDeRestricaoRegistroDeAndamento = null;
	
	private String mensagemApensos;
	
	public static final Long CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO = 600000857L;
	
	private ArrayList<SelectItem> pecasSelectItens;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void restaurarSessao() {
		checkParamFrame();
		if ((Boolean) getAtributo(SUCESSO) == null) {
			setAtributo(SUCESSO, false);
		}
		sucesso = (Boolean) getAtributo(SUCESSO);
		if ((Boolean) getAtributo(TEM_CERTIDAO) == null) {
			setAtributo(TEM_CERTIDAO, false);
		}
		temCertidao = (Boolean) getAtributo(TEM_CERTIDAO);
		
		nomePDFBaixa = (String) getAtributo(NOME_PDF_BAIXA);
				
		codigoAndamentoSelecionado = (Long) getAtributo(CODIGO_ANDAMENTO_SELECIONADO);
		setAndamentoProcessoGerado((AndamentoProcesso) getAtributo(ANDAMENTO_PROCESSO_GERADO));
		processo = (Processo) getAtributo(PROCESSO);
		
		peticaoSelecionada = (String) getAtributo(PETICAO_SELECIONADA);
		andamentoProcessoSelecionado = (AndamentoProcesso) getAtributo(ANDAMENTO_PROCESSO_SELECIONADO);
		numeroPecas = (Integer) getAtributo(NUMERO_PECAS);
		andamentosProcesso = (List<AndamentoProcesso>) getAtributo(ANDAMENTOS_PROCESSO);
		andamentosProcessoTotal = (List<AndamentoProcesso>) getAtributo(ANDAMENTOS_PROCESSO_TOTAL);
		dataAutuacao = (String) getAtributo(DATA_AUTUACAO);
		procedencia = (String) getAtributo(PROCEDENCIA);
		midia = (String) getAtributo(MIDIA);
		
		listaPecasDTO = getAtributo(LISTAS_PECAS_DTO) == null ? getListaPecasDTO() : (List<PecaDTO>) getAtributo(LISTAS_PECAS_DTO);
		pecaSeleciona = (Integer) getAtributo(PECA_SELECIONADA);
		listaOriginal = (List<PecaProcessoEletronico>)getAtributo(LISTA_ORIGINAL);
		if ((Boolean) getAtributo(PECAS_EXPANDIDAS) == null) {
			setAtributo(PECAS_EXPANDIDAS, false);
		}
		listaPecasExpandida = (Boolean) getAtributo(PECAS_EXPANDIDAS);

		comunicacao = (Comunicacao) getAtributo(COMUNICACAO);
		
		if (listaPecasProcessoEletronico == null){
			listaPecasProcessoEletronico = new ArrayList<PecaProcessoEletronico>();
		}
		
		permiteAndamento = (Boolean) getAtributo(PERMITE_ANDAMENTO) == null ? false : (Boolean) getAtributo(PERMITE_ANDAMENTO);
		
		lancamentoIndevidoException = (Boolean) getAtributo(LANCAMENTO_INDEVIDO_EXCEPTION) == null ? false
				: (Boolean) getAtributo(LANCAMENTO_INDEVIDO_EXCEPTION);
		setorAutorizadoLancarAndamentoIndevido = (Boolean) getAtributo(AUTORIZADO_LANCAR_ANDAMENTO_INDEVIDO);
		
		if( getAtributo(PROCESSO_INCIDENTE_TRANS_BEAN) != null){
			IdentificacaoProcessoResolver idResolver = new IdentificacaoProcessoResolver();
			ObjetoIncidente obIncidente = (ObjetoIncidente) getAtributo(PROCESSO_INCIDENTE_TRANS_BEAN);
			
				if(obIncidente != null){
					setIdentificacaoProcesso(obIncidente.getIdentificacao());	
					try {
						Processo processo = getProcessoService().recuperarProcesso(idResolver.getSigla(obIncidente.getIdentificacao()), idResolver.getNumero(obIncidente.getIdentificacao()));
						setIncidenteSelecionado(processo);
					} catch (ProcessoException e) {
						log.error("Processo nao valido.", e);
						reportarErro("O Processo deve ser válido. ", e.getMessage());
					} catch (ServiceException e) {
						log.error("Nao foi possivel recuperar o processo!.", e);
						reportarErro("Não foi possível recuperar o processo. ", e.getMessage());
					}	
				}
		}
        if ((Boolean) getAtributo(NUMERO_USUARIOS_DIFERENTE_1) == null) {
            setAtributo(NUMERO_USUARIOS_DIFERENTE_1, false);
        }
        setNumeroDeUsuariosDiferente1((Boolean) getAtributo(NUMERO_USUARIOS_DIFERENTE_1));
        mensagemDeRestricaoRegistroDeAndamento = (String)getAtributo(MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO);
        
        if ((Boolean) getAtributo(PROCESSO_INATIVO) == null) {
			setAtributo(PROCESSO_INATIVO, false);
		} else {
			processoInativo = (Boolean) getAtributo(PROCESSO_INATIVO);
		}
        
        if ((Boolean) getAtributo(ACORDAOS_PENDENTES_DE_PUBLICACAO) == null) {
			setAtributo(ACORDAOS_PENDENTES_DE_PUBLICACAO, false);
		} else {
			existemAcordaosPendentesDePublicacao = (Boolean) getAtributo(ACORDAOS_PENDENTES_DE_PUBLICACAO);
		}
		super.restaurarSessao();
	}

	private void checkParamFrame() {
		String seqObjetoIncidente = getRequestParamFrame("seqObjetoIncidente");
		if (seqObjetoIncidente != null) {
			ObjetoIncidente<?> oi = null;
			try {
				oi = getObjetoIncidenteService().recuperarPorId(Long.parseLong(seqObjetoIncidente));
			} catch (Exception e) {
				log.error("Erro ao recuperar objeto incidente pelo id!.", e);
				reportarErro("Erro ao recuperar objeto incidente pelo id!", e.getMessage());
			}
			setIncidenteSelecionado(oi);
		}
	}

	public void atualizarSessao() {
		setAtributo(SUCESSO, sucesso);
		setAtributo(TEM_CERTIDAO, temCertidao);
		setAtributo(NOME_PDF_BAIXA, nomePDFBaixa);
		setAtributo(ANDAMENTO_PROCESSO_GERADO, getAndamentoProcessoGerado());
		setAtributo(PROCESSO, processo);
		setAtributo(PETICAO_SELECIONADA, peticaoSelecionada);
		setAtributo(ANDAMENTO_PROCESSO_SELECIONADO, andamentoProcessoSelecionado);
		setAtributo(NUMERO_PECAS, numeroPecas);
		setAtributo(ANDAMENTOS_PROCESSO, andamentosProcesso);
		setAtributo(ANDAMENTOS_PROCESSO_TOTAL, andamentosProcessoTotal);

		setAtributo(PERMITE_ANDAMENTO, permiteAndamento);
		setAtributo(AUTORIZADO_LANCAR_ANDAMENTO_INDEVIDO, setorAutorizadoLancarAndamentoIndevido);
		setAtributo(LANCAMENTO_INDEVIDO_EXCEPTION, lancamentoIndevidoException);
		setAtributo(DATA_AUTUACAO, dataAutuacao);
		setAtributo(PROCEDENCIA, procedencia);
		setAtributo(MIDIA, midia);
		
		setAtributo(PROCESSO_INCIDENTE_TRANS_BEAN, null);
		setAtributo(CODIGO_ANDAMENTO_SELECIONADO, codigoAndamentoSelecionado);
		setAtributo(NUMERO_USUARIOS_DIFERENTE_1, isNumeroDeUsuariosDiferente1());
		setAtributo(COMUNICACAO, comunicacao);
		setAtributo(MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO, mensagemDeRestricaoRegistroDeAndamento);
		setAtributo(PROCESSO_INATIVO, processoInativo);
		setAtributo(PECAS_EXPANDIDAS, false);
		super.atualizarSessao();

	}

	public void limpar(ActionEvent evt) {
		processo = null;
		comunicacao = null;
		identificacaoProcesso = "";
		processoSelecionado = null;
		processosSelecionados = null;
		andamentoProcessoSelecionado = null;
		peticaoSelecionada = "";
		numeroPecas = null;
		andamentosProcesso = null;
		andamentosProcessoTotal = null;
		processoOutraRelatoria = false;
		ministroRelatorAposentado = false;
		lancamentoIndevidoException = false;
		processoFindo = false;
		permiteAndamento = false;
		setorAutorizadoLancarAndamentoIndevido = false;
		idOrigemDecisao = null;
		idPresidenteInterino = null;
		dataAutuacao = "";
		procedencia = "";
		midia = "";
		mensagemDeRestricaoRegistroDeAndamento = new String();
		listaPecasProcessoEletronico = new ArrayList<PecaProcessoEletronico>();
		limparInformacoesAndamentoSelecionado();
	}

	@Override
	protected void doVerificarAndamento() throws ServiceException {
		AndamentoProcessoService andamentoProcessoService = getAndamentoProcessoService();
		precisaConfirmacaoLancarAndamento = andamentoProcessoService.precisaConfirmacaoLancarAndamento(andamentoSelecionado, processo);
	}

	private void atualizaSucesso(boolean valor){
		setSucesso(valor);
		inputSucesso.setValue(sucesso);
	}
	private void atualizaTemCertidao(boolean valor){
		setTemCertidao(valor);
		inputTemCertidao.setValue(temCertidao);
	}
	
	public void registrarAndamentoEAssinar(ActionEvent event) throws IOException, SQLException{
		setLancarAndamentoEAssinarDocumento(true);
		setAtributo(FEEDBACKS, null);
		registrarAndamento(null);
	}
	
	@SuppressWarnings("unused")
	public void registrarAndamento(ActionEvent event) throws IOException, SQLException {
		feedbacks = new ArrayList<String>();
		atualizaTemCertidao(false);
		atualizaSucesso(false);
		atualizarSessao();
		setAtributo("requisicao", null);
		try {
			if (andamentoSelecionado == null) {
				reportarAviso("Favor selecione um andamento para ser registrado.");
			} else {
				incidentes = new ArrayList<ObjetoIncidente<?>>();
				incidentes.add(incidenteSelecionado);
				setCodigoAndamentoSelecionado(andamentoSelecionado.getId());
	            setNumeroDeUsuariosDiferente1(false);
	             
				if ((processo.getTipoMeio().equals("E"))
						&& ((codigoAndamentoSelecionado.equals(7104L))
						 || (codigoAndamentoSelecionado.equals(7101L)) 
						 || codigoAndamentoSelecionado.equals(7108L))) {
					atualizaTemCertidao(true);
					atualizaSucesso(true);
				} else {
					if(isAndamentoPermitido()){
						
						//TODO aplicar normalização para toda a lista de incidentes
						normalizaPecasObjetoIncidente(incidenteSelecionado);		
						
    					if (codigoAndamentoSelecionado.equals(8527L)){ //determinada a suspensão nacional
    						if (!verificarInicioSuspensaoNacional(processo))
    							return;
    					}
    					else if (codigoAndamentoSelecionado.equals(8528L)){ //encerrada a suspensão nacional
    						if (!encerrarSuspensaoNacional(processo))
    							return;
    					}
						
    					if(andamentoSelecionado.getId().equals(ANDAMENTO_VISTAPGR_INTIMACAO) && pecaSelecionada == null){
							reportarAviso("Para registrar este andamento é necessário selecionar algum Despacho/Decisão.");
							return;
						}

    					if( ( andamentoSelecionado.getId().equals(ANDAMENTO_VISTAPGR) || 
      						   andamentoSelecionado.getId().equals(ANDAMENTO_VISTA_AGU) ) 
      							&& processo.getTipoMeio().equals("E") ){
      						verificarProcessoEFisicoEmVista(processo); 
						}
      					
      					if( ( andamentoSelecionado.getId().equals(ANDAMENTO_AUTOS_DISPONIBILIZADOS) && processo.getTipoMeio().equals("F") )){
      						reportarAviso("Favor selecione um andamento para ser registrado.");
 						}
      					
    					
    					if( andamentoSelecionado.getId().equals(ANDAMENTO_CANCELAMENTO_PROTOCOLO) && processo.getTipoMeio().equals("E") ) {
    						DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
    						Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
    						if ( ultimoDeslocamento != null && !ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO.longValue())) {
    							List<Processo> processosApenso = recuperarApensos(processo);
    						//	deslocarProcesso(getSetorUsuarioAutenticado().getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
    							deslocarProcesso(CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO, processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
    							}
    						Processo processoAndamento = (Processo) incidenteSelecionado;
							
										processoAndamento.setSituacao(SituacaoProcesso.PROCESSO_FINDO);
										getProcessoService().flushSession();
    					}
    					

    					AndamentoProcesso andamentoProcesso = null;
    					try {
							AndamentoProcessoInfoImpl andamentoProcessoInfo = montarAndamentoProcessoInfo();
							andamentoProcesso = getAndamentoProcessoService().salvarAndamento(andamentoProcessoInfo, processo, incidenteSelecionado);
    					} catch (Exception e) {
    						reportarErro("Não foi possível registrar um andamento. ", e.getMessage());
    						getAndamentoProcessoService().excluir(andamentoProcesso);
    						atualizaSucesso(false);
    						log.error("Erro ao registrar um andamento.", e);
    						
    						limparInformacoesAndamentoSelecionado();
						}
	
						comunicacao = null;
						
						if(codigoAndamentoSelecionado.equals(ANDAMENTO_VISTA_AGU))
							comunicacao = gerarComunicacaoVistaAGU(andamentoProcesso, false);
						
						if(codigoAndamentoSelecionado.equals(ANDAMENTO_VISTAPGR))
							comunicacao = gerarComunicacaoVistaPGR(andamentoProcesso, false);
						
						if(codigoAndamentoSelecionado.equals(ANDAMENTO_AUTOS_DISPONIBILIZADOS))
							comunicacao = gerarComunicacaoAutosDisponibilizados(andamentoProcesso, false);
						
						if(codigoAndamentoSelecionado.equals(ANDAMENTO_VISTAPGR_INTIMACAO))
							comunicacao = gerarComunicacaoVistaPGR(andamentoProcesso, true);
						
						if(codigoAndamentoSelecionado.equals(AUTOS_REQUISITADOSPGR))
							comunicacao = gerarComunicacaoAutosRequisitadosPGR();
					
						if(codigoAndamentoSelecionado.equals(AUTOS_REQUISITADOS_AGU))
							comunicacao = gerarComunicacaoAutosRequisitadosAGU();
						
    					if (codigoAndamentoSelecionado.equals(8527L)){
    						iniciarSuspensaoNacional(processo, andamentoProcesso);
    					}
    					
					    if (comunicacao != null) {
				          associarAndamentoProcessoComunicacao(andamentoProcesso, comunicacao);
				          criarComunicacaoObjetoIncidente(processo.getId(), comunicacao, andamentoProcesso, FlagProcessoLote.P);
					    }
						
						if(getAndamentoGeraDocumento()){
							TipoPecaProcesso tipoPecaProcesso = tipoPecaProcessoDoAndamentoSelecionado();
							Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());				
							ModeloComunicacao modelo = recuperarModeloComunicacaoDoAndamento();
	
							ArrayList<ComunicacaoDocumento> listaDocumentoPendentesAssinatura = new ArrayList<ComunicacaoDocumento>();
							InfoPecaVinculadoAndamentoDTO info = new InfoPecaVinculadoAndamentoDTO.Builder().setObjetoIncidente(isPecaDeMerito() ? processo : incidenteSelecionado)
									.setUsuario(usuario)
									.setSetorUsuario(getSetorUsuarioAutenticado())
									.setAndamentoProcesso(andamentoProcesso)
									.setTipoPecaProcesso(tipoPecaProcesso)
									.setModeloComunicacao(modelo)
									.setIdAndamentoSelecionado(idAndamentoSelecionado())
									.setListaPartes(getListaInforPartes(isPecaDeMerito() ? processo : incidenteSelecionado))
									.setDescricaoPeca(tipoPecaProcesso.getDescricao())
									.builder();
							ComunicacaoDocumentoResult comDoc = getProcessamentoRelatorioService().gerarPecaVinculadaAoAndamentoSelecionado(info);	
							listaDocumentoPendentesAssinatura.add(new ComunicacaoDocumento(comDoc));	
							if(isLancarAndamentoEAssinarDocumento()){
								assinarDocumento(listaDocumentoPendentesAssinatura);
							}
	
						}
						
						//se o processo estiver na situação K e for recebido novamente pelo tribunal ele deve se tornar ativo novamente
						if(codigoAndamentoSelecionado.equals(8100L) && incidenteSelecionado instanceof Processo){
							Processo processoAndamento = (Processo) incidenteSelecionado;
							if(processoAndamento.getSituacao() == SituacaoProcesso.DETERMINADA_DEVOLUCAO) {
								if(processoAndamento.getRelatorIncidenteId() != null ) {
									if (processoAndamento.getRelatorIncidenteId().longValue() == Ministro.COD_MINISTRO_PRESIDENTE.longValue()){
										processoAndamento.setSituacao(SituacaoProcesso.REGISTRADO_PRESIDENTE);
										getProcessoService().flushSession();
									}else{
										processoAndamento.setSituacao(SituacaoProcesso.DISTRIBUIDO);
										getProcessoService().flushSession();
									}
								} else {
									processoAndamento.setSituacao(SituacaoProcesso.AUTUADO);
									getProcessoService().flushSession();
								}
							}
						
						}
						if((codigoAndamentoSelecionado.equals(7105L) && incidenteSelecionado instanceof Processo) || (codigoAndamentoSelecionado.equals(7106L) && incidenteSelecionado instanceof Processo)){
							Processo processoAndamento = (Processo) incidenteSelecionado;
							processoAndamento.setSituacao(SituacaoProcesso.PROCESSO_FINDO);
							processoAndamento.setFiltroEmTramitacao(false);
							getProcessoService().flushSession();
						}
	
						setAndamentoProcessoGerado(andamentoProcesso);	
						
					    if(incidenteSelecionado.getEletronico() && (isDeslocarAutomaticamente() || idMinistroSelecionado != null)){
					    	deslocarProcessoApensos(incidenteSelecionado);
						}			
												
						if(andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTA_AO_MINISTRO))
							deslocarProcessoVistaMinistro(processo,andamentoProcesso);
					    
						
    					if( andamentoSelecionado.getId().equals(ANDAMENTO_VISTAPGR) && processo.getTipoMeio().equals("E") ) {
    						DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
    						Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
    						if ( ultimoDeslocamento != null && ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(ORGAO_EXTERNO) && !ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_ORGAO_PGR)) {
    							List<Processo> processosApenso = recuperarApensos(processo);
    							deslocarProcesso(getSetorUsuarioAutenticado().getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
    							deslocarProcessosApenso(processosApenso,usuario.getSetor());
    							}
    						
    						try {
    							DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
    							if(deslocaProcesso!=null && !deslocaProcesso.getCodigoOrgaoDestino().equals(Setor.CODIGO_SETOR_PGR)) {
    								ArrayList<Long> processos = new ArrayList<Long>();
    								processos.add(processo.getId());
    					
    								Guia guia = new Guia();
    								Guia.GuiaId guiaId = new Guia.GuiaId();
    								guia.setId(guiaId);
    					
    								guia.setCodigoOrgaoOrigem(deslocaProcesso.getGuia().getCodigoOrgaoDestino());
    								guia.setCodigoOrgaoDestino(Setor.CODIGO_SETOR_PGR);
    								
    								guia.setTipoOrgaoOrigem(deslocaProcesso.getGuia().getTipoOrgaoDestino());
    								guia.setTipoOrgaoDestino(DeslocaProcesso.TIPO_ORGAO_EXTERNO);
    					
    								String numAnoGuia = getObjetoIncidenteService().inserirDeslocamento(guia, processos, true);
    								
    								if (numAnoGuia == null) {
    									throw new ServiceException("Erro ao efetuar o deslocamento");
    								}
    								
    								deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
    								
    								getDeslocaProcessoService().atualizaAndamento(deslocaProcesso, andamentoProcesso.getId());
    								
    								//deslocaProcesso.setAndamentoProcesso(andamentoProcesso);
    					
    								getDeslocaProcessoService().alterar(deslocaProcesso);
    							}
    						} catch (SQLException e) {
    							e.printStackTrace();
    							throw new ServiceException("Erro ao efetuar o deslocamento: "
    									+ e.getMessage());
    						}
    						
    					}
						
						
    					if( andamentoSelecionado.getId().equals(ANDAMENTO_VISTA_AGU)  && processo.getTipoMeio().equals("E") ) {
    						DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
    						Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
    						if ( ultimoDeslocamento != null && ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(ORGAO_EXTERNO) && !ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_ORGAO_AGU)) {
    							List<Processo> processosApenso = recuperarApensos(processo);
    							deslocarProcesso(getSetorUsuarioAutenticado().getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
    							deslocarProcessosApenso(processosApenso,usuario.getSetor());
    							}
    						
    						try {
    							DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
    							if(deslocaProcesso!=null && !deslocaProcesso.getCodigoOrgaoDestino().equals(Setor.CODIGO_SETOR_AGU)) {
    								ArrayList<Long> processos = new ArrayList<Long>();
    								processos.add(processo.getId());
    					
    								Guia guia = new Guia();
    								Guia.GuiaId guiaId = new Guia.GuiaId();
    								guia.setId(guiaId);
    					
    								guia.setCodigoOrgaoOrigem(deslocaProcesso.getGuia().getCodigoOrgaoDestino());
    								guia.setCodigoOrgaoDestino(Setor.CODIGO_SETOR_AGU);
    								
    								guia.setTipoOrgaoOrigem(deslocaProcesso.getGuia().getTipoOrgaoDestino());
    								guia.setTipoOrgaoDestino(DeslocaProcesso.TIPO_ORGAO_EXTERNO);
    					
    								String numAnoGuia = getObjetoIncidenteService().inserirDeslocamento(guia, processos, true);
    					
    								if (numAnoGuia == null) {
    									throw new ServiceException("Erro ao efetuar o deslocamento");
    								}
    					
    								deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);

    								getDeslocaProcessoService().atualizaAndamento(deslocaProcesso, andamentoProcesso.getId());
    								
    								// deslocaProcesso.setAndamentoProcesso(andamentoProcesso);
    					
    								getDeslocaProcessoService().alterar(deslocaProcesso);
    							}
    						} catch (SQLException e) {
    							e.printStackTrace();
    							throw new ServiceException("Erro ao efetuar o deslocamento: "
    									+ e.getMessage());
    						}
    					}
    					
    					
						posRegistrarAndamento(andamentoProcesso);
	
						atualizaTemCertidao(false);
						atualizaSucesso(true);					
					}else{
						setAtributo(FEEDBACKS, feedbacks);
					}
				}
			}
		} catch (DesapensadoException e) {
			atualizaSucesso(false);
			log.error("Erro ao desapensar processo.", e);
			reportarAviso("Algo deu errado ao desapensar processo!", e.getMessage());
			limparInformacoesAndamentoSelecionado();
		} catch (ProcessoApensadoAOutroException e) {
			atualizaSucesso(false);
			log.error("Algo deu errado ao desapensar processo!", e);
			reportarAviso("Algo deu errado ao desapensar processo!", e.getMessage());
			limparInformacoesAndamentoSelecionado();
		} catch (AndamentoNaoAutorizadoException e) {
			atualizaSucesso(false);
			log.error("Andamento não autorizado para o setor!", e);
			reportarAviso("Andamento não autorizado para o setor!", e.getMessage());
			limparInformacoesAndamentoSelecionado();
		} catch (AIApensadoNaoREException e) {
			atualizaSucesso(false);
			log.error("Procsso da classe AI nao apensado a RE!", e);
			reportarAviso("Procsso da classe AI nao apensado a RE!", e.getMessage());
			limparInformacoesAndamentoSelecionado();
		} catch (ServiceException e) {
			atualizaSucesso(false);
			log.error("Erro ao registrar o andamento!", e);
			reportarErro("Erro ao registrar o andamento!", e.getMessage());
			limparInformacoesAndamentoSelecionado();
		} catch (Exception e) {
			limparInformacoesAndamentoSelecionado();
			atualizaSucesso(false);
			log.error("Erro ao registrar o andamento!", e);
			reportarErro("Erro ao registrar o andamento!", e.getMessage());
			e.printStackTrace();
		} finally {
			confirmaDeslocamento = false;
			processoDifereSetorUsuario = false;	
			inputAndamento.setValue(null);
			comunicacao = null;
			observacao = "";
			observacaoInterna = "";
			
			renderCheckBoxEditarDestinoBaixa = false;
			
			atualizarSessao();
		}
	}
			
	private void associarAndamentoProcessoComunicacao(AndamentoProcesso andamentoProcesso, Comunicacao comunicacao) throws ServiceException {
		AndamentoProcessoComunicacao andamentoProcessoComunicacao = new AndamentoProcessoComunicacao();
		andamentoProcessoComunicacao.setAndamentoProcesso(andamentoProcesso);
		andamentoProcessoComunicacao.setComunicacao(comunicacao);
		andamentoProcessoComunicacao.setTipoVinculoAndamento(TipoVinculoAndamento.RELACIONADO);
		getAndamentoProcessoComunicacaoService().salvar(andamentoProcessoComunicacao);
	}	
	
	   /**
	    * Cria um objeto do tipo ComunicacaoIncidente para intimação e o associa à comunicação informada.
	    * @param comunicacao
	    * @param pdf
	    * @param usuario
	    * @return
	    * @throws ServiceException
	    */
	  @SuppressWarnings({ "deprecation", "rawtypes" })
	private ComunicacaoIncidente criarComunicacaoObjetoIncidente(Long idObjetoIncidente, Comunicacao comunicacao, AndamentoProcesso andamentoProcesso, FlagProcessoLote tipoVinculo) throws ServiceException {
	      ObjetoIncidente objetoIncidente = getObjetoIncidenteService().recuperarPorId(idObjetoIncidente);
	      ComunicacaoIncidente comunicacaoIncidente = new ComunicacaoIncidente();
	      comunicacaoIncidente.setObjetoIncidente(objetoIncidente);
	      comunicacaoIncidente.setTipoVinculo(tipoVinculo);
	      comunicacaoIncidente.setComunicacao(comunicacao);
	      comunicacaoIncidente.setAndamentoProcesso(andamentoProcesso);
	      return  getComunicacaoIncidenteService().salvar(comunicacaoIncidente);
	   }	
	
    private boolean verificarInicioSuspensaoNacional(Processo processo){
    	try{
	    	List<ProcessoTema> listaProcessoTema = getProcessoTemaService().pesquisarProcessoTema(null, processo.getSiglaClasseProcessual(), processo.getNumeroProcessual(),
					null, TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo(), null);
			if (listaProcessoTema == null || listaProcessoTema.isEmpty())
				throw new ServiceException("O processo não está vinculado a nenhum tema como LEADING CASE para suspensão nacional.");						
			if (listaProcessoTema.size() > 1)
				throw new ServiceException("O processo está vinculado a mais de um tema como LEADING CASE");
			
			ProcessoTema processoTema = listaProcessoTema.get(0);
			
			if (processoTema.getTema().getSuspensaoNacionalAtual() != null)
				throw new ServiceException("Já há uma suspensão nacional registrada para o tema.");			
    	}catch(ServiceException e){
    		log.error("Não foi possível verificar suspensao nacional para o processo!.", e);
    		reportarErro("Não foi possível verificar suspensao nacional para o processo!.", e.getMessage());
    		return false;
    	}
    	return true;
    }
    
    private boolean iniciarSuspensaoNacional(Processo processo, AndamentoProcesso andamento){
    	try{
    		List<ProcessoTema> listaProcessoTema = getProcessoTemaService().pesquisarProcessoTema(null, processo.getSiglaClasseProcessual(), processo.getNumeroProcessual(),
    				null, TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo(), null);
    		if (listaProcessoTema == null || listaProcessoTema.isEmpty())
    			throw new ServiceException("O processo não está vinculado a nenhum tema para suspensão nacional.");
    		Calendar data = Calendar.getInstance();
    		data.set(Calendar.HOUR_OF_DAY, 0);
    		data.set(Calendar.MINUTE, 0);
    		data.set(Calendar.SECOND, 0);
    		
    		for (ProcessoTema processoTema : listaProcessoTema){
    			getTemaService().registarSuspensaoNacional(processoTema.getTema(), processo, data.getTime(), andamento);
    		}    		
    	}catch(ServiceException e){
    		log.error("Nao foi possível iniciar a suspensao nacional para o processo!.", e);
    		reportarErro("Não foi possível iniciar a suspensao nacional para o processo!.", e.getMessage());
    		return false;
    	}
    	return true;
    }
    
    private boolean encerrarSuspensaoNacional(Processo processo){
    	try{
	    	List<ProcessoTema> listaProcessoTema = getProcessoTemaService().pesquisarProcessoTema(null, processo.getSiglaClasseProcessual(), processo.getNumeroProcessual(), null, null, null);
			if (listaProcessoTema == null || listaProcessoTema.isEmpty())
				throw new ServiceException("O processo não está vinculado a nenhum tema para suspensão nacional.");
			Calendar data = Calendar.getInstance();
    		data.set(Calendar.HOUR_OF_DAY, 23);
    		data.set(Calendar.MINUTE, 59);
    		data.set(Calendar.SECOND, 59);
    		
    		for (ProcessoTema processoTema : listaProcessoTema){
    			getTemaService().encerrarSuspensaoNacional(processoTema.getTema(), processo, data.getTime());
    		}    		
			
    	}catch(ServiceException e){
    		log.error("Nao foi possível encerrar a suspensao nacional para o processo!.", e);
    		reportarErro("Não foi possível encerrar a suspensao nacional para o processo!.", e.getMessage());
    	}
    	return true;
    }    

	public String chamarAssinador(){
		RequisicaoAssinaturaDocumentoComunicacao requisicao = (RequisicaoAssinaturaDocumentoComunicacao) getAtributo("requisicao");	
		reportarFeedbacks();
		if(requisicao == null){			
			return null;
		}
		setRequestValue(RequisicaoAssinaturaDocumentoComunicacao.REQUISICAO_ASSINADOR, requisicao);
		setAtributo("requisicao", null);
		setLancarAndamentoEAssinarDocumento(false);
		return "assinarServlet";
	}

	private AndamentoProcesso getUltimoAndamentoProcesso() {
		return andamentosProcessoTotal.isEmpty() ? null : andamentosProcessoTotal.get(andamentosProcessoTotal.size() - 1);
	}

	private AndamentoProcessoInfoImpl montarAndamentoProcessoInfo() throws ServiceException {
		
		Peticao peticao = null;
		if (andamentoSelecionado.getId() == 8246) {
			
			List<Peticao> peticoes = pesquisaPeticaoSuggestionBox(peticaoSelecionada);
			
			if (peticoes.size() == 0 || peticoes.size() > 1) {
				reportarErro("A petição '" + peticaoSelecionada + "' não existe.");
				throw new ServiceException("A petição '" + peticaoSelecionada + "' não existe.");
			}
			peticao = peticoes.get(0);
			if(peticao.getObjetoIncidenteVinculado() !=null && peticao.getObjetoIncidenteVinculado().getPrincipal().getId() != incidentes.get(0).getId()) {
				reportarErro("A petição '" + peticaoSelecionada + "' não está vinculada ao processo.");
				throw new ServiceException("A petição '" + peticaoSelecionada + "' não está vinculada ao processo.");
			}
			observacao = peticao.getNumeroPeticao() + "/" + peticao.getAnoPeticao();

			if (peticao.getObjetoIncidenteVinculado() == null || !peticao.getObjetoIncidenteVinculado().getId().equals(incidentes.get(0).getId())) {
				reportarErro("Petição informada não está vinculada ao processo!");
				throw new ServiceException("Petição informada não está vinculada ao processo!");
			}
			// Verificar se a petição já foi juntada ao processo.
			if (getProcessoDependenciaService().isPeticaoJuntada((Processo) incidentes.get(0).getPrincipal(), peticao)) {
				reportarErro("A petição " + peticao.getIdentificacao() + " já foi juntada ao processo " +  incidentes.get(0).getPrincipal().getIdentificacao() + ".");
				throw new ServiceException("A petição " + peticao.getIdentificacao() + " já foi juntada ao processo " +  incidentes.get(0).getPrincipal().getIdentificacao() + ".");
			}

		}else if(LISTA_ANDAMENTOS_REQUER_TEMA.contains(andamentoSelecionado.getId())){
			observacao = "";
			if(getListaProcessoTema() != null && !getListaProcessoTema().isEmpty()){
				for(ProcessoTema pt : getListaProcessoTema()){
					observacao += "Tema nº " + pt.getTema().getNumeroSequenciaTema() + " - " + pt.getIdentificacaoSimples() + ", ";
				}
				observacao = observacao.substring(0, observacao.length()-2);
			}
		}
		
		AndamentoProcessoInfoImpl andamentoProcessoInfo = new AndamentoProcessoInfoImpl();
		andamentoProcessoInfo.setAndamento(andamentoSelecionado);
		andamentoProcessoInfo.setCodigoUsuario(getUser().getUsername().toUpperCase());
		andamentoProcessoInfo.setIdOrigemDecisao(idOrigemDecisao);
		andamentoProcessoInfo.setIdPresidenteInterino(idPresidenteInterino);
		andamentoProcessoInfo.setIdTipoDevolucao(idTipoDevolucao);
		andamentoProcessoInfo.setObservacao(observacao);
		andamentoProcessoInfo.setObservacaoInterna(observacaoInterna);
		andamentoProcessoInfo.setSetor(getSetorUsuarioAutenticado());
		andamentoProcessoInfo.setPeticao(peticao);
		andamentoProcessoInfo.setComunicacao(comunicacao);
		
		if(isConsiderarIncidentesComoProcessosPrincipais()){
			if(incidentes != null){
				if (processosSelecionados == null) processosSelecionados = new ArrayList<Processo>();
				for(ObjetoIncidente<?> inc : incidentes){
					if(inc instanceof Processo)
						processosSelecionados.add((Processo) inc);
				}			
			}
		}
		
		if (processoSelecionado != null) {
			if (processosSelecionados == null) processosSelecionados = new ArrayList<Processo>();
			processosSelecionados.add((Processo) processoSelecionado);
		}

		andamentoProcessoInfo.setProcessosPrincipais(processosSelecionados);
		andamentoProcessoInfo.setUltimoAndamento(getUltimoAndamentoProcesso());
		
		if (origemSelecionada != null) {
			HistoricoProcessoOrigem historicoProcessoOrigem = origensCadastradas.get(origemSelecionada);
			escolherOrigemPrincipal(historicoProcessoOrigem);
			
			andamentoProcessoInfo.setOrigem(historicoProcessoOrigem.getOrigem());
		}
		
		andamentoProcessoInfo.setProcessosTemas(getListaProcessoTema());
		return andamentoProcessoInfo;
	}

	public void cancelarRegistroAndamento(ActionEvent event) {
		setTabelaProcessoTema(null);
		limparInformacoesAndamentoSelecionado();
		limparInformacoesModais();
	}
	

	public void recuperarPecasAndamento(ActionEvent even){
		AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();
		andamentoProcessoSelecionado = andamentoProcesso;
		List<PecaProcessoEletronico> listaPecaRecuperada = new ArrayList<PecaProcessoEletronico>();
		if (andamentoProcesso.getPossuiSeqDocumento()){
			listaPecaRecuperada = recuperarPecasAndamento(andamentoProcessoSelecionado);
			
		}
		
		if(listaPecaRecuperada.isEmpty()){
			reportarAviso("Nenhum registro encontrado");
		}else{
			for (PecaProcessoEletronico peca : listaPecaRecuperada){
				listaPecasProcessoEletronico.add(formataCampoDescricaoPeca(peca));
			}
			setContemPecasAndamento(true);
			reportarInformacao("Foram encontrados "+listaPecasProcessoEletronico.size()+" registros");
		}
	}
	
	/**
	 * Funcionalidade criada para que o PDF não venha no título com caracteres desconfigurados.
	 * @param peca
	 * @return
	 */
	public PecaProcessoEletronico formataCampoDescricaoPeca(PecaProcessoEletronico peca){
		PecaProcessoEletronico pecaP = peca;
		String descricao = null;
		if(pecaP.getDescricaoPeca() == null){
			descricao = pecaP.getTipoPecaProcesso().getDescricao();
		} else{
			descricao = pecaP.getDescricaoPeca();
		}		
		String descricaoPecaSemFormatacao = descricao.replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e").replaceAll("[îìíï]", "i").replaceAll("[õôòóö]", "o")
		.replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÀÁÄ]", "A").replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I").replaceAll("[ÕÔÒÓÖ]", "O")
		.replaceAll("[ÛÙÚÜ]", "U").replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n').replace('Ñ', 'N').replaceAll("!", "")
		.replaceAll("\\[\\´\\`\\?!\\@\\#\\$\\%\\¨\\*", " ").replaceAll("\\(\\)\\=\\{\\}\\[\\]\\~\\^\\]", " ")
		.replaceAll("[\\.\\;\\-\\_\\+\\'\\ª\\º\\:\\;\\/]", " ");
		pecaP.setDescricaoPeca(descricaoPecaSemFormatacao);
		return pecaP;
	}
	
	public void reportAction(ActionEvent event) {
		reportPecas();
	}
	
	public void reportPecas() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-disposition",
				String.format("attachment; filename=\"%s.pdf\"", documentoEletronico.getId()));
		response.setContentType("application/x-pdf");
		ByteArrayInputStream input = new ByteArrayInputStream(documentoEletronico.getArquivo());

		try {
			IOUtils.copy(input, response.getOutputStream());
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} finally {
			IOUtils.closeQuietly(input);
		}

		facesContext.responseComplete();
	}
	
	@SuppressWarnings("unused")
	private void imprimir(String nomePdf) throws ServiceException,  IOException {
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
	
	public void imprimirCertidao() throws ServiceException,  IOException {
		InputStream is = null;
		OutputStream os = null;

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=" + nomePDFBaixa);

		try {
			is = new FileInputStream(nomePDFBaixa);
			response.setHeader("Content-Length", String.valueOf(is.available()));
			os = response.getOutputStream();
			IOUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
			File fileToDelete = new File(nomePDFBaixa);
			fileToDelete.delete();
		}
		facesContext.responseComplete();
	}

	public void recuperarApensosAction(ActionEvent event) {

		try {
			apensos = getProcessoDependenciaService().recuperarTodosApensos(processo);
			urlExternas = dependenciaUrlExternas(apensos);
			apensadosAo = getProcessoDependenciaService().recuperarTodosApensadosAo(processo);
			urlExternasAo = dependenciaUrlExternasAo(apensadosAo);
		} catch (Exception e) {
			log.error("Nao foi possível recuperar apensos do processo!", e);
			reportarErro("Não foi possível recuperar apensos do processo!", e.getMessage());
		}

	}
	
	private List<ProcessoDependenciaUrlExterna> dependenciaUrlExternasAo(List<ProcessoDependencia> dependencias){
		List<ProcessoDependenciaUrlExterna> dependenciaUrlExterna = new ArrayList<ProcessoDependenciaUrlExterna>();
		for (ProcessoDependencia apensoAo : dependencias) {
			ProcessoDependenciaUrlExterna urlExterna = new ProcessoDependenciaUrlExterna();
			urlExterna.setProcessoDependencia(apensoAo);
			urlExterna.setConsultaUrlExterna(SistemasExternosUtils.gerarLinkEJudConsulta(getBeanAmbiente().getNomeServidorPadrao(), apensoAo.getIdObjetoIncidenteVinculado()));
			dependenciaUrlExterna.add(urlExterna);
		}
		return dependenciaUrlExterna;
	}
	
	
	private List<ProcessoDependenciaUrlExterna> dependenciaUrlExternas(List<ProcessoDependencia> dependencias){
		List<ProcessoDependenciaUrlExterna> dependenciaUrlExterna = new ArrayList<ProcessoDependenciaUrlExterna>();
		for (ProcessoDependencia apenso : dependencias) {
			ProcessoDependenciaUrlExterna urlExterna = new ProcessoDependenciaUrlExterna();
			urlExterna.setProcessoDependencia(apenso);
			urlExterna.setConsultaUrlExterna(SistemasExternosUtils.gerarLinkEJudConsulta(getBeanAmbiente().getNomeServidorPadrao(), apenso.getIdObjetoIncidente()));
			dependenciaUrlExterna.add(urlExterna);
		}
		return dependenciaUrlExterna;
	}
	
    public void posRegistrarAndamento(AndamentoProcesso andamentoProcesso) throws ServiceException {
    	try{ 
	    	// VERIFICAR E REMETER PARA A FILA DE ENVIO DE MANIFESTAÇÃO DO MNIDOIS
			if (processo.getTipoMeio().equals("E")){
				DeslocaProcesso ultimoDeslocamentoProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(andamentoProcesso.getObjetoIncidente());
				
				if (ultimoDeslocamentoProcesso !=null){
					Long codigoOrgaoDestino = ultimoDeslocamentoProcesso.getCodigoOrgaoDestino();
					if (isAndamentoRemessaMniDois(andamentoProcesso) && getOrigemService().isOrigemAptaParaParaNotificacao(codigoOrgaoDestino)) {
						remeterProcesso(andamentoProcesso, processo.getPrincipal().getId());
					}
				}
			}
	    } finally {
	        String mensagem = "Andamento lançado com sucesso!";
	        posRegistrarAndamento(andamentoProcesso, mensagem);
    	}
    }

	private boolean isAndamentoRemessaMniDois(AndamentoProcesso andamentoProcesso) throws ServiceException {
		
		String andamentoRemessaMniDois = getListaAndamentoRemessaMniDois();
		boolean isAndamentoRemessaMniDois = false;

		ArrayList<String> listaAndamentosRemessaMniDois = new ArrayList<String>(Arrays.asList(andamentoRemessaMniDois.split("\\s*,\\s*"))); 
		
		for(String andamento : listaAndamentosRemessaMniDois) {
			if (andamentoProcesso.getCodigoAndamento().toString().equals(andamento)){
				isAndamentoRemessaMniDois = true;
				break;
			}
		}

		return isAndamentoRemessaMniDois;		
	}
	
    private String getListaAndamentoRemessaMniDois() throws ServiceException {
    	try {
    		ConfiguracaoSistema configuracaoSistema = this.getConfiguracaoSistemaService().recuperarValor(SISTEMA_PROCESSAMENTO, LISTA_ANDAMENTOS_REMESSA);
			return configuracaoSistema.getValor();
		} catch (ServiceException e) {
			throw new ServiceException("Erro ao recuperar lista de andamento para remessa.", e);
		}	
	}	
    
	private void remeterProcesso(AndamentoProcesso andamentoProcesso, Long seqIncidenteProcesso) throws ServiceException {
		URI url = this.getRequestService().buildUrl(ENDERECO_REMESSA_MNIDOIS, seqIncidenteProcesso.toString(), andamentoProcesso.getId().toString());
		try {
			this.getRequestService().requestServiceGet(url, String.class);
			reportarInformacao("Processo enviado para a fila de remessa com sucesso!");
		} catch (Exception e) {
			reportarInformacao("Processo não foi enviado para a fila. O envio será feito posteriormente.");		}
	}
    
    public void posRegistrarAndamento(AndamentoProcesso andamentoProcesso, String mensagem) throws ServiceException {
        processoFindo = getProcessoService().isProcessoFindo(processo);
        limparInformacoesAndamentoSelecionado();
        andamentosProcessoTotal.add(andamentoProcesso);
        reportarInformacao(mensagem);
        atualizarListaAndamentos();
    }

	public List<Peticao> pesquisaPeticaoSuggestionBox(Object suggest) {
		try {
			String identificacao = suggest.toString();
			Long numero = null;
			Short ano = null;
			if (identificacao.indexOf("/") > 0) {
				numero = new Long(identificacao.substring(0, identificacao.indexOf("/")));
				ano = new Short(identificacao.substring(identificacao.indexOf("/") + 1));
			} else {
				numero = new Long(identificacao);
			}

			Peticao exemplo = new Peticao();
			exemplo.setNumeroPeticao(numero);
			exemplo.setAnoPeticao(ano);
			return getPeticaoService().pesquisarPorExemplo(exemplo);

		} catch (NumberFormatException e) {
			log.error("Nao foi possivel realizar uma conversao numerica!", e);
			reportarErro("Nao foi possivel realizar uma conversao numerica!", e.getMessage());
			return new ArrayList<Peticao>();
		} catch (Exception e) {
			log.error("Algo deu errado na pesquisa de petição.!", e);
			reportarErro("Algo deu errado na pesquisa de petição.!", e.getMessage());
			return new ArrayList<Peticao>();
		}
	}

	public void selecionarProcesso(Object object) {
		Processo processo = (Processo) object;
		if (processo != null){
			processoInativo = isProcessoInativo();
			setAtributo(PROCESSO_INATIVO, processoInativo);
		}		
	}

	@Override
	protected List<Andamento> doPesquisarAndamentosParaLista() throws ServiceException {
		return getAndamentoService().pesquisarAndamentosAutorizados(getSetorUsuarioAutenticado().getId(), processo);
	}

	public void pesquisarProcessoAction(Processo processo) {
		setProcesso(processo);
	}

	public void atualizarAndamento(ActionEvent evt) {
		try {
			if (getAndamentoProcessoService().podeEditarObservacao(andamentoProcessoSelecionado, getUser().getUsername())) {
				andamentoProcessoSelecionado.setDescricaoObservacaoAndamento(observacao);
			}
			andamentoProcessoSelecionado.setDescricaoObservacaoInterna(observacaoInterna);

			getAndamentoProcessoService().salvar(andamentoProcessoSelecionado);
			reportarAviso("Andamento do processo atualizado com sucesso.");

			limparCamposObservacao();
		} catch (Exception e) {
			log.error("Algo deu errado ao atualizar um andamento!", e);
			reportarErro("Algo deu errado ao atualizar um andamento!", e.getMessage());
		}
	}

	private void limparCamposObservacao() {
		observacaoInterna = "";
		observacao = "";
	}
	
	private String recuperarObsInterna() throws ServiceException {
		return getAndamentoProcessoService().recuperarObsInterna(andamentoProcessoSelecionado.getId());
	}

	public void atualizarAndamentoSelecionado(ActionEvent evt) throws ServiceException {
		AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();
		andamentoProcessoSelecionado = andamentoProcesso;

		setAtributo(ANDAMENTO_PROCESSO_SELECIONADO, andamentoProcessoSelecionado);
		observacaoInterna = andamentoProcesso.getDescricaoObservacaoInterna();
		String observacaoInternaAtualizada = recuperarObsInterna();
		if (observacaoInternaAtualizada == null) {
			observacaoInternaAtualizada = "";
		}
		obsInternaAtualizadaPorOutroUsuario = false;
		if (observacaoInterna != null && !observacaoInterna.equals(observacaoInternaAtualizada)) {
			obsInternaAtualizadaPorOutroUsuario = true;
		}
		observacaoInterna = observacaoInternaAtualizada;
		observacao = andamentoProcesso.getDescricaoObservacaoAndamento();
	}
	
	public String getDescricaoObservacaoAndamento() {
		AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();
		String observacao = andamentoProcesso.getDescricaoObservacaoAndamento();
		
		return observacao == null ? "" : observacao.replaceAll("\n", "<br>");
	}

	public void atualizarAndamentoSelecionadoParaLancamentoIndevido() {
		atualizarAndamentoSelecionadoParaLancamentoIndevido(null);
	}
	
	@SuppressWarnings("rawtypes")
	public void atualizarAndamentoSelecionadoParaLancamentoIndevido(ActionEvent evt) {

		AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();

		try {
			getAndamentoProcessoService().verificarLancamentoIndevido(andamentoProcesso);
			
			andamentoProcessoSelecionado = andamentoProcesso;
			setAtributo(ANDAMENTO_PROCESSO_SELECIONADO, andamentoProcessoSelecionado);
			
			List<SearchCriterion> search = new ArrayList<SearchCriterion>();
			search.add(new EqualCriterion<Long>("numProcesso", andamentoProcesso.getNumProcesso()));
			search.add(new EqualCriterion<String>("sigClasseProces", andamentoProcesso.getSigClasseProces()));
			search.add(new EqualCriterion<Long>("numeroSequencia", andamentoProcesso.getNumeroSequenciaErrado()));
			
			List<AndamentoProcesso> andamentoProcessosAnteriores = getAndamentoProcessoService().pesquisarPorExemplo( new AndamentoProcesso(), search);
			
			if (!andamentoProcessosAnteriores.isEmpty()) {
				for (Iterator iterator = andamentoProcessosAnteriores.iterator(); iterator.hasNext();) {
					AndamentoProcesso andamentoProcesso2 = (AndamentoProcesso) iterator.next();
 					if(andamentoProcesso2.getCodigoAndamento() == 7107 && andamentoProcesso2.getLancamentoIndevido() == true ) {
						reportarAviso("Não é possível excluir o andamento de lançamento indevido quando o mesmo indevidou uma reautuação. Favor proceder com uma nova reautuação.");
						lancamentoIndevidoException = true;
						setMsgLancamentoIndevidoException("Não é possível excluir o andamento de lançamento indevido quando o mesmo indevidou uma reautuação. Favor proceder com uma nova reautuação.");
					}
				}
			} else {
				setAtributo(ANDAMENTO_PROCESSO_SELECIONADO, andamentoProcessoSelecionado);
				setCancelarLancamentoIndevido(andamentoProcesso.getTipoAndamento().getId().equals(Andamentos.LANCAMENTO_INDEVIDO.getId()));	
			}
		} catch(ServiceException e) {
			setMsgLancamentoIndevidoException(e.getMessage());
			lancamentoIndevidoException = true;
			log.error("Nao foi possivel atualizar o andamento para lancamento indevido!", e);
			reportarErro("Nao foi possivel atualizar o andamento para lancamento indevido!", e.getMessage());
		}
		
	}

	@SuppressWarnings("deprecation")
	public void lancarOuCancelarAndamentoIndevido(ActionEvent evt) {

		try {
			AndamentoProcesso andamentoProcesso = andamentoProcessoSelecionado;
			
			IncidentePreferencia preferenciaRemover = null;
			IncidentePreferencia preferenciaInserir = null;
			
			if (getAndamentoService().podeLancarAndamentoIndevido(getSetorUsuarioAutenticado().getId(), andamentoProcesso.getTipoAndamento(), this, processo)) {

				if (andamentoProcesso.getTipoAndamento().getId().equals(Andamento.Andamentos.LANCAMENTO_INDEVIDO.getId())) {
					
					Long codAndamentoInvalidado = getAndamentoProcessoService().recuperarCodAndamentoPorNumeroSequencia(processo,andamentoProcesso.getNumeroSequenciaErrado());
					
					cancelarAndamentoIndevido(andamentoProcesso);
					
					//se o andamento que foi invalidado foi do tipo convertido em eletronico
					if(codAndamentoInvalidado==8249){
						try {
							//alterar tipo meio processo para eletronico
							processo.setTipoMeioProcesso(TipoMeioProcesso.ELETRONICO);
							processo.setTipoMeio("E");
							midia = "Eletrônico";
							
							List<IncidentePreferencia> preferencias = processo.getPreferencias();
							
							IncidentePreferencia incidentePreferencia = null;
							
							for (IncidentePreferencia incidente : preferencias) {
								if(incidente.getTipoPreferencia().getSigla().equalsIgnoreCase("CE")){									
									// pesquisar se já existe um incidente preferencia do tipo convertido em eletronico
									incidentePreferencia = incidente;
									break;
								}								 
							}
							
							if(incidentePreferencia == null){
								// inserir incidente preferencia do tipo convertido em eletronico
								preferenciaInserir =  new IncidentePreferencia();
								preferenciaInserir.setObjetoIncidente(processo);
								TipoIncidentePreferencia tipo = new TipoIncidentePreferencia();
								tipo.setId(13L); // tipo incidente preferencia convertido em eletronico
								tipo.setSigla("CE");
								tipo.setDescricao("Convertido em processo eletrônico");
								preferenciaInserir.setTipoPreferencia(tipo);
							
								getIncidentePreferenciaService().inserirPreferenciaConvertidoEletronico(preferenciaInserir);
								
							}	
							
						} catch (ServiceException e) {
							log.error("Nao foi possivel invalidar o lançamento indevido!", e);
							reportarErro("Não foi possível invalidar o lançamento indevido!", e.getMessage());
						}
					}
					
					if(codAndamentoInvalidado==7107){
						processo.setSituacao(SituacaoProcesso.PROCESSO_FINDO);
					}
					
					reportarAviso("Andamento indevido cancelado com sucesso!");					
				} else {
					
					andamentoProcesso = getAndamentoProcessoService().recuperarPorId(andamentoProcesso.getId());
					
					AndamentoProcesso andamentoIndevido = lancarAndamentoIndevido(andamentoProcesso);

					getComunicacaoIncidenteService().limparSessao();

				    AndamentoProcessoComunicacao andamentoProcessoComunicacaoExcluir = null;
				    Comunicacao comunicacaoExcluir = null;
				    boolean comunicacaoLida = false;
				    
					if( andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTAPGR) || andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTAPGR_INTIMACAO) ||
							andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTA_AGU)){
					    andamentoProcessoComunicacaoExcluir = getAndamentoProcessoComunicacaoService().recuperarPorAndamento(andamentoProcesso.getId());
					    if (andamentoProcessoComunicacaoExcluir !=null && andamentoProcessoComunicacaoExcluir != null && andamentoProcessoComunicacaoExcluir.getComunicacao() != null){
						    comunicacaoExcluir = getComunicacaoService().recuperarPorId(andamentoProcessoComunicacaoExcluir.getComunicacao().getId());
						    
						    if (comunicacaoExcluir != null && comunicacaoExcluir.getDataRecebimento() != null) {
						    	comunicacaoLida = true;
						    }
					    }    
					}
					
						DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
						if ( ultimoDeslocamento != null && ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(ORGAO_EXTERNO) 
								&& (ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_ORGAO_PGR) && (andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTAPGR_INTIMACAO) || andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTAPGR)) 
								|| (ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_ORGAO_AGU) && (andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTA_AGU) )))) {
								Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
									List<Processo> processosApenso = recuperarApensos(processo);
									deslocarProcesso(getSetorUsuarioAutenticado().getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
									deslocarProcessosApenso(processosApenso,usuario.getSetor());
						}
					
					if( andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_AUTOS_DISPONIBILIZADOS)){
					    andamentoProcessoComunicacaoExcluir = getAndamentoProcessoComunicacaoService().recuperarPorAndamento(andamentoProcesso.getId());
					    if (andamentoProcessoComunicacaoExcluir != null && andamentoProcessoComunicacaoExcluir.getComunicacao() != null){
						    comunicacaoExcluir = getComunicacaoService().recuperarPorId(andamentoProcessoComunicacaoExcluir.getComunicacao().getId());
						    
						    if (comunicacaoExcluir != null && comunicacaoExcluir.getDataRecebimento() != null) {
						    	comunicacaoLida = true;
						    }
					    }    
					}
					
					Comunicacao comunicacao = null;
					if( andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTAPGR) && comunicacaoLida ){
						comunicacao = gerarComunicacaoIndevidoVistaPGR(false);
					}
					
					if( andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTAPGR_INTIMACAO) && comunicacaoLida){
						comunicacao = gerarComunicacaoIndevidoVistaPGR(true);
					}
					
					if( andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTA_AGU) && comunicacaoLida){
						comunicacao = gerarComunicacaoIndevidoVistaAGU();
					}
					
					if( andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_AUTOS_DISPONIBILIZADOS) && comunicacaoLida){
						comunicacao = gerarComunicacaoIndevidoAutosDisp();
					}
					
				    if (comunicacao != null) {
			          associarAndamentoProcessoComunicacao(andamentoIndevido, comunicacao);
			          criarComunicacaoObjetoIncidente(processo.getId(), comunicacao, andamentoIndevido, FlagProcessoLote.P);
				    }
					
				    if (andamentoProcessoComunicacaoExcluir !=null && andamentoProcessoComunicacaoExcluir.getComunicacao() != null && comunicacaoExcluir != null){
					    List<PecaProcessoEletronicoComunicacao> listaPecasExcluir = getPecaProcessoEletronicoComunicacaoService().pesquisarPecasPelaComunicacao(comunicacaoExcluir);
					    for (PecaProcessoEletronicoComunicacao pecaExcluir : listaPecasExcluir) {
					    	if (pecaExcluir.getPecaProcessoEletronico() != null && 
					    			( (andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTAPGR)) 
					    					|| (andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_VISTA_AGU))
					    					|| (andamentoProcesso.getCodigoAndamento().equals(ANDAMENTO_AUTOS_DISPONIBILIZADOS)) ) )
				    			getPecaProcessoEletronicoService().excluir(pecaExcluir.getPecaProcessoEletronico());
					    	
					    	getPecaProcessoEletronicoComunicacaoService().excluir(pecaExcluir);
						}
					    
				    	getAndamentoProcessoComunicacaoService().excluir(andamentoProcessoComunicacaoExcluir);
				    	
				    	DocumentoComunicacao documentoComunicacao = getDocumentoComunicacaoService().recuperarNaoCancelado(comunicacaoExcluir);
				    	
				    	if (documentoComunicacao!=null)
				    		getDocumentoComunicacaoService().excluir(documentoComunicacao);
				    	
			    		getComunicacaoService().excluir(comunicacaoExcluir);
				    }
					
					// se o andamento que deseja ser invalidado for do tipo convertido em eletronico
					if(andamentoProcesso.getCodigoAndamento()==8249){
						try {
							//alterar tipo meio processo para fisico
							processo.setTipoMeioProcesso(TipoMeioProcesso.FISICO);
							processo.setTipoMeio(TIPO_MEIO_FISICO);
							midia = "Físico";
							
							List<IncidentePreferencia> preferencias = processo.getPreferencias();
							
							for (IncidentePreferencia incidente : preferencias) {
								if(incidente.getTipoPreferencia().getSigla().equalsIgnoreCase("CE")){									
									//remover incidente preferencia convertido em eletronico
									preferenciaRemover = incidente;
									break;
								}								 
							}
							
							if(preferenciaRemover != null){
								getIncidentePreferenciaService().removerPreferenciaConvertidoEletronico(preferenciaRemover);
							}
						} catch (ServiceException e) {
							log.error("Nao foi possivel remover as preferencias ao invalidar o lançamento indevido!", e);
							reportarErro("Não foi possével remover as preferencias ao invalidar o lançamento indevido!", e.getMessage());
						}
					}
					
					
					if(andamentoProcesso.getCodigoAndamento()==7105 || andamentoProcesso.getCodigoAndamento()==7106){
						try {
							if(processo.getRelatorIncidenteId() != null ) {
								if (processo.getRelatorIncidenteId().longValue() == Ministro.COD_MINISTRO_PRESIDENTE.longValue()){
									processo.setSituacao(SituacaoProcesso.REGISTRADO_PRESIDENTE);
									getProcessoService().flushSession();
								}else{
									processo.setSituacao(SituacaoProcesso.DISTRIBUIDO);
									getProcessoService().flushSession();
								}
							} else {
									processo.setSituacao(SituacaoProcesso.AUTUADO);
									getProcessoService().flushSession();
							}
							
						} catch (ServiceException e) {
							log.error("Erro ao invalidar andamento!", e);
							reportarErro("Erro ao invalidar andamento!", e.getMessage());
						}
					}
					
					if(andamentoProcesso.getCodigoAndamento()==7107){
						// Finaliza a dependencia
						List<SearchCriterion> search = new ArrayList<SearchCriterion>();
						search.add(new EqualCriterion<Long>("andamentoProcesso", andamentoProcesso.getId()));
						
						List<ProcessoDependencia> proocessoDependencias = getProcessoDependenciaService().pesquisarPorExemplo( new ProcessoDependencia(), search);
						
						proocessoDependencias.get(0).setDataFimDependencia(new Date());
						
						getProcessoService().flushSession();
						
						search = new ArrayList<SearchCriterion>();
						search.add(new EqualCriterion<String>("sigClasseProcessoReautuado", processo.getSiglaClasseProcessual()));
						search.add(new EqualCriterion<Long>("numeroProcessoReautuado", processo.getNumeroProcessual()));
						
						List<NumeroProcesso> numeroProcessos = getNumeroProcessoService().pesquisarPorExemplo(new NumeroProcesso(), search);

						numeroProcessos.get(0).setSigClasseProcessoReautuado(null);
						numeroProcessos.get(0).setNumeroProcessoReautuado(null);
						numeroProcessos.get(0).setTipoReautuacao(null);

						
						getProcessoService().flushSession();
						
						// Altera a SituProcessoDependenciaação do Processo
						if(processo.getRelatorIncidenteId() != null ) {
							if (processo.getRelatorIncidenteId().longValue() == Ministro.COD_MINISTRO_PRESIDENTE.longValue()){
								processo.setSituacao(SituacaoProcesso.REGISTRADO_PRESIDENTE);
							}else{
								processo.setSituacao(SituacaoProcesso.DISTRIBUIDO);
							}
						} else {
								processo.setSituacao(SituacaoProcesso.AUTUADO);
								getProcessoService().flushSession();
						}
					}			
					
					if(LISTA_ANDAMENTOS_REQUER_TEMA.contains(andamentoProcesso.getCodigoAndamento())){
						List<Long> listaNumTema = new ArrayList<Long>();
						if(andamentoProcesso.getObservacao() != null){
							String[] temasEnvolvidos = andamentoProcesso.getObservacao().split(",");
							
							for(String temaEnvolvido : temasEnvolvidos){
								String[] temasProcessos = temaEnvolvido.split("-");
								String temaProcesso = temasProcessos[0];
								String[] infoTemaProcesso = temaProcesso.split("nº");
								listaNumTema.add(Long.parseLong(infoTemaProcesso[1].trim()));
								
							}
						}
						
						if(!listaNumTema.isEmpty()){
							for(Long numTema : listaNumTema){
								getProcessoTemaService().removerProcessoTema(processo.getId(), numTema);
							}
						}
					}
					
					reportarAviso("Andamento indevido registrado com sucesso!");					
				}
				
				getProcessoService().flushSession();
				// O processo pode deixar se der findo se o andamento que foi cancelado foi o de
				// processo findo.
				processoFindo = getProcessoService().isProcessoFindo(processo);
				atualizarSessao();

				atualizarListaAndamentos();
				limparCamposObservacao();
				
				// remover ou inserir preferencia convertido em eletronico da lista de preferencias do processo.
				if(preferenciaRemover != null){
					processo.getPreferencias().remove(preferenciaRemover);								
				}
				if(preferenciaInserir != null){
					processo.getPreferencias().add(preferenciaInserir);
				}
				
			}
		} catch (LancamentoIndevidoException e) {
			setMsgLancamentoIndevidoException(e.getMessage());
			lancamentoIndevidoException = true;
			log.error("Erro ao lançar ou cancelar o andamento indevido!", e);
			reportarErro("Erro ao lançar ou cancelar o andamento indevido!", e.getMessage());

		} catch (Exception e) {
			setMsgLancamentoIndevidoException("Erro ao lançar ou cancelar o andamento indevido!" + e.getMessage());
			lancamentoIndevidoException = true;
			log.error("Erro ao lançar ou cancelar o andamento indevido!", e);
			reportarErro("Erro ao lançar ou cancelar o andamento indevido!", e.getMessage());
		}
	}
	
	private void cancelarAndamentoIndevido(AndamentoProcesso andamentoProcessoIndevido) throws ServiceException, LancamentoIndevidoException {

		getAndamentoProcessoService().cancelarLancamentoAndamentoIndevido(andamentoProcessoIndevido, andamentosProcessoTotal);

	}

	private AndamentoProcesso lancarAndamentoIndevido(AndamentoProcesso andamentoProcesso) throws ServiceException, LancamentoIndevidoException {

		AndamentoProcesso andamentoIndevido = null;
		andamentoIndevido = getAndamentoProcessoService().salvarAndamentoIndevido(processo, andamentoProcesso, getSetorUsuarioAutenticado(),
				getUser().getUsername(), getUltimoAndamentoProcesso(), observacao, observacaoInterna, incidenteSelecionado);
		andamentosProcessoTotal.add(andamentoIndevido);
		return andamentoIndevido;
	}

	public boolean getPodeLancarAndamentoIndevido() {
		try {
			AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();
			
			if (setorAutorizadoLancarAndamentoIndevido == null) {
				setorAutorizadoLancarAndamentoIndevido = getAndamentoService().podeLancarAndamentoIndevido(getSetorUsuarioAutenticado().getId(),
						andamentoProcesso.getTipoAndamento(), this);
				setAtributo(AUTORIZADO_LANCAR_ANDAMENTO_INDEVIDO, setorAutorizadoLancarAndamentoIndevido);
			}

			boolean lancamentoIndevido = andamentoProcesso.getLancamentoIndevido();
	
			if ( (andamentoProcesso.getCodigoAndamento().equals(AUTOS_REQUISITADOSPGR)) || (andamentoProcesso.getCodigoAndamento().equals(AUTOS_REQUISITADOS_AGU)) )
				return false;
			
			
			return setorAutorizadoLancarAndamentoIndevido && !lancamentoIndevido 
			&& (andamentoProcesso.getTipoAndamento().isAndamentoAutomaticoPlenarioVirtual() ? isUsuarioRegistrarAndamentoIndevidoRG() : true) && isUsuarioRegistrarAndamentoIndevido();

		
		} catch (ServiceException e) {
			log.error("Nao é possível lancar andamento indevido!", e);
			reportarErro("Nao é possível lancar andamento indevido!", e.getMessage());
			return false;
		}
	}

	public String getStyleEditarAndamentoProcesso() {
		AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();
		
		return (isUsuarioMaster() || isUsuarioEditarAndamentoProcesso()) && 
				!andamentoProcesso.getCodigoAndamento().equals(Andamento.Andamentos.LANCAMENTO_INDEVIDO.getId()) ? "" : "visibility:hidden";
	}

	public String getStyleEditarObservacao() {

		boolean podeEditarObservacao = andamentoProcessoSelecionado == null ? false : getAndamentoProcessoService().podeEditarObservacao(
				andamentoProcessoSelecionado, getUser().getUsername());

		return podeEditarObservacao ? "opacity: 1" : "opacity: 0.4";
	}

	public String getStyleLancamentoIndevido() {
		AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();
		return andamentoProcesso.getLancamentoIndevido() ? " text-decoration:line-through; " : "";
	}

	public String getUrlImagemLancamentoIndevido() {
		return !getPodeLancarAndamentoIndevido() ? "../../images/borracha_disabled.png" : "../../images/borracha.png";
	}

	public String getUrlImagemEditarAndamento() {
		return !getPodeLancarAndamentoIndevido() ? "../../images/botaAlterarv_disabled.gif" : "../../images/botaAlterarv.gif";
	}
	
	public String getTitleEditarLancamentoIndevido(){
		AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();
		
		String title = "";
		
		if(!setorAutorizadoLancarAndamentoIndevido){
			title = "Usuário não autorizado a editar andamento indevido.";
		}else if(!getPodeLancarAndamentoIndevido()){
			title = "Não é possível editar. Já lançado como indevido." ;			
		} if (andamentoProcesso.getCodigoAndamento().equals(AUTOS_REQUISITADOSPGR)) {
			title = "Este andamento não permite edição." ;
		} else {
			title = "Editar as informações do andamento do processo";
		}
		return  title;
	}

	public String getTitleLancamentoIndevido() {
		String title = "";
		AndamentoProcesso andamentoProcesso = (AndamentoProcesso) tabelaAndamentos.getRowData();
		if (processoFindo && !andamentoProcesso.isAndamentoProcessoFindo()) {
			title = "Processo Findo";
		} else if (!setorAutorizadoLancarAndamentoIndevido) {
			title = "Usuário não autorizado para lançar andamento indevido";
		} else if (andamentoProcesso.getCodigoAndamento().equals(Andamentos.LANCAMENTO_INDEVIDO.getId())) {
			title = "Desfazer andamento indevido";
		} else if (andamentoProcesso.getLancamentoIndevido()) {
			title = "Já lançado como indevido";
		} else if (andamentoProcesso.getTipoAndamento().isAndamentoAutomaticoPlenarioVirtual() && !isUsuarioRegistrarAndamentoIndevidoRG()) {
			title = "Usuário não autorizado para lançar andamento indevido para o andamento " + andamentoProcesso.getTipoAndamento().getId();
		} else if (!isUsuarioRegistrarAndamentoIndevido()) {
			title = "Usuário não autorizado para lançar andamento indevido";
		} else if (andamentoProcesso.getCodigoAndamento().equals(AUTOS_REQUISITADOSPGR)) {
			title = "Este andamento não permite lançamento indevido.";
		} else {
			title = "Lançar andamento indevido";
		}
		return title;
	}

	public String getIdentificacao() {

		AndamentoProcesso andamento = (AndamentoProcesso) tabelaAndamentos.getRowData();

		try {
			return getAndamentoProcessoService().getSituacaoAndamento(andamento);
		} catch (ServiceException e) {
			log.error("Algo deu errado ao recuperar a situacao do andamento!", e);
			reportarErro("Algo deu errado ao recuperar a situacao do andamento!", e.getMessage());
			return "";
		}
	}

	public boolean isDisabledNovoAndamento() {
		return andamentosAutorizados.size() == 0;
	}

	public boolean isDisabledEditarAndamento() {
		return andamentoProcessoSelecionado == null ? true : !getAndamentoProcessoService().podeEditarObservacao(andamentoProcessoSelecionado, getUser().getUsername());
	}

	protected void doAdicionarProcesso(Processo processo) throws ServiceException {

		permiteAndamento = false;
		if (andamentosProcesso != null) {
			andamentosProcesso.clear();
		}
		procedencia = getProcedenciaService().pesquisarProcedencia(processo.getId());
		dataAutuacao = DateFormat.getDateInstance().format(processo.getDataAutuacao());
		midia = processo.getIsEletronico() ? "Eletrônico" : "Físico";
		contarPecas();
		preencherAndamentos();
		carregarAndamentosAutorizados();
		permiteAndamento = true;
		numeroUnicoProcesso = processo.getNumeroUnicoProcesso();
		
		try {
			getObjetoIncidenteService().registrarLogSistema(processo.getId(), "CONSULTA_PROCESSO", "Pesquisar Processo para lançamento de andamento",processo.getId(),"JUDICIARIO.PROCESSO");
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	private void preencherAndamentos() throws ServiceException {
		
		andamentosProcessoTotal = getAndamentoProcessoService().pesquisarAndamentoProcesso(processo.getSiglaClasseProcessual(), processo.getNumeroProcessual());	
		andamentosProcesso = new ArrayList<AndamentoProcesso>();
		preencherResumoAndamentos();
	}

	public String getTextoExpandirRetrairAndamentosProcesso() {

		return andamentosProcessoTotal.size() <= NUMERO_DE_ANDAMENTOS_PROCESSO_VISIVEIS ? ""
				: andamentosProcesso.size() == NUMERO_DE_ANDAMENTOS_PROCESSO_VISIVEIS ? "Mostrar todos" : "Mostrar resumo";
	}

	public void expandirRetrairAndamentosProcesso(ActionEvent event) {
		if (andamentosProcesso.size() == NUMERO_DE_ANDAMENTOS_PROCESSO_VISIVEIS) {
			preencherTodosAndamentos();
		} else {
			preencherResumoAndamentos();
		}
	}

	public void atualizarListaAndamentos() {
		if (andamentosProcesso.size() == NUMERO_DE_ANDAMENTOS_PROCESSO_VISIVEIS) {
			preencherResumoAndamentos();
		} else {
			preencherTodosAndamentos();
		}
	}

	private void preencherTodosAndamentos() {

		andamentosProcesso.clear();
		for (int i = andamentosProcessoTotal.size() - 1; i >= 0; i--) {
			andamentosProcesso.add(andamentosProcessoTotal.get(i));
		}
	}

	private void preencherResumoAndamentos() {

		andamentosProcesso.clear();
		int count = 1;
		for (int i = andamentosProcessoTotal.size() - 1; i >= 0 && count <= NUMERO_DE_ANDAMENTOS_PROCESSO_VISIVEIS; i--) {
			andamentosProcesso.add(andamentosProcessoTotal.get(i));
			count++;
		}
	}
	
	private List<PecaProcessoEletronico> recuperarPecasAndamento(AndamentoProcesso andamentosProcesso){
		List<PecaProcessoEletronico> pecasProcessoEletronico = new ArrayList<PecaProcessoEletronico>();
		List<Long> listaSeqDocumentos = new ArrayList<Long>();
		 if (andamentosProcesso.getListaTextoAndamentoProcessos() != null && 
				andamentosProcesso.getListaTextoAndamentoProcessos().size() > 0){
			 for (TextoAndamentoProcesso tap : andamentosProcesso.getListaTextoAndamentoProcessos()){
				 if (tap.getSeqDocumento() != null){
					listaSeqDocumentos.add(tap.getSeqDocumento()); 
				 }
			 }
		 }
		 
		 try {
			 ArquivoProcessoEletronicoService service = getArquivoProcessoEletronicoService();
			 pecasProcessoEletronico = service.pesquisarPecasPelosDocumentos(listaSeqDocumentos);			 
			 for (PecaProcessoEletronico peca : pecasProcessoEletronico){
					peca.setDocumentos(getArquivoProcessoEletronicoService().recuperarDocumentosPeca(peca.getId()));
			 }
			 if(!pecasProcessoEletronico.isEmpty()){
				 Iterator<PecaProcessoEletronico> pecas = pecasProcessoEletronico.iterator();
				 while(pecas.hasNext()){
					 PecaProcessoEletronico peca = pecas.next();
					 if(peca.getDocumentos() == null || peca.getDocumentos().isEmpty()){
						 pecas.remove();
					 }
				 }
			 }
		} catch (Exception e) {
			log.error("Não foi possível recuperar as pecas associadas ao andamento!", e);
			reportarErro("Não foi possível recuperar as pecas associadas ao andamento!", e.getMessage());
		}
		
		return pecasProcessoEletronico;
	}
	
	private void contarPecas() {
		try {
			ArquivoProcessoEletronicoService service = getArquivoProcessoEletronicoService();
			setNumeroPecas(service.countPecasProcesso(processo));

		} catch (ServiceException e) {
			log.error("Nao foi possivel contar as pecas associadas ao processo!", e);
			reportarErro("Não foi possível contar as pecas associadas ao processo!" + e.getMessage());
		}
	}
	
	public String getPreferencias() {
		String siglas = "";
		List<IncidentePreferencia> preferencias = processo.getPreferencias();

		for (IncidentePreferencia incidentePreferencia : preferencias) {
			String sigla = incidentePreferencia.getTipoPreferencia().getSigla();
			siglas += siglas.equals("") ? sigla : " " + sigla; 
		}
		return siglas;
	}
	
	public String getTitlePreferencias() {
		String title = "";
		List<IncidentePreferencia> preferencias = processo.getPreferencias();

		for (IncidentePreferencia incidentePreferencia : preferencias) {
			String descricao = incidentePreferencia.getTipoPreferencia().getDescricao();
			title = title.equals("") ? descricao : " " + descricao; 
		}
		return title;
	}

	public String getAltBotaoNovoAndamento() {
		return "Registrar um novo andamento";
	}

	public String getTitleBotaoNovoAndamento() {
		return getAltBotaoNovoAndamento();
	}

	public String getStyleBotaoNovoAndamento() {
		return isDisabledNovoAndamento() ? "opacity: 0.4" : "opacity: 1";
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public String getMsgLancamentoIndevidoException() {
		return msgLancamentoIndevidoException;
	}

	public void setMsgLancamentoIndevidoException(String msgLancamentoIndevidoException) {
		this.msgLancamentoIndevidoException = msgLancamentoIndevidoException;
	}

	public boolean isLancamentoIndevidoException() {
		return lancamentoIndevidoException;
	}

	public void setLancamentoIndevidoException(boolean lancamentoIndevidoException) {
		this.lancamentoIndevidoException = lancamentoIndevidoException;
	}

	public void setNumeroPecas(int numeroPecas) {
		this.numeroPecas = numeroPecas;
	}

	public String getNumeroPecas() {
		return Integer.toString(numeroPecas);
	}

	public boolean isTemPecas() {
		return numeroPecas != null && numeroPecas > 0;
	}

	@SuppressWarnings("deprecation")
	public boolean isTemApensos() {
		return processo.getQuantidadeApensos() != null && processo.getQuantidadeApensos() > 0;
	}

	public boolean isPermiteAndamento() {
		return permiteAndamento;
	}

	public void setPermiteAndamento(boolean permiteAndamento) {
		this.permiteAndamento = permiteAndamento;
	}

	public String getMidia() {
		return midia;
	}

	public void setMidia(String midia) {
		this.midia = midia;
	}

	public List<AndamentoProcesso> getAndamentosProcesso() {
		return andamentosProcesso;
	}

	public void setAndamentosProcesso(List<AndamentoProcesso> andamentosProcesso) {
		this.andamentosProcesso = andamentosProcesso;
	}

	public String getDataAutuacao() {
		return dataAutuacao;
	}

	public void setDataAutuacao(String dataAutuacao) {
		this.dataAutuacao = dataAutuacao;
	}

	public BeanRegistrarAndamento() {
		restaurarSessao();
	}

	public void setTabelaAndamentos(HtmlDataTable tabelaAndamentos) {
		this.tabelaAndamentos = tabelaAndamentos;
	}

	public HtmlDataTable getTabelaAndamentos() {
		return tabelaAndamentos;
	}

	public void setPeticaoSelecionada(String peticaoSelecionada) {
		this.peticaoSelecionada = peticaoSelecionada;
		atualizarSessao();
	}

	public String getPeticaoSelecionada() {
		return peticaoSelecionada;
	}

	public void setAndamentoProcessoSelecionado(AndamentoProcesso andamentoProcessoSelecionado) {
		this.andamentoProcessoSelecionado = andamentoProcessoSelecionado;
	}

	public AndamentoProcesso getAndamentoProcessoSelecionado() {
		return andamentoProcessoSelecionado;
	}

	public void setApensos(List<ProcessoDependencia> apensos) {
		this.apensos = apensos;
	}

	public List<ProcessoDependencia> getApensos() {
		return apensos;
	}

	public void setApensadosAo(List<ProcessoDependencia> apensadosAo) {
		this.apensadosAo = apensadosAo;
	}

	public List<ProcessoDependencia> getApensadosAo() {
		return apensadosAo;
	}

	public void setCancelarLancamentoIndevido(boolean cancelarLancamentoIndevido) {
		this.cancelarLancamentoIndevido = cancelarLancamentoIndevido;
	}

	public boolean isCancelarLancamentoIndevido() {
		return cancelarLancamentoIndevido;
	}

	public void setUrlExternas(List<ProcessoDependenciaUrlExterna> urlExternas) {
		this.urlExternas = urlExternas;
	}

	public List<ProcessoDependenciaUrlExterna> getUrlExternas() {
		return urlExternas;
	}

	public void setUrlExternasAo(List<ProcessoDependenciaUrlExterna> urlExternasAo) {
		this.urlExternasAo = urlExternasAo;
	}

	public List<ProcessoDependenciaUrlExterna> getUrlExternasAo() {
		return urlExternasAo;
	}

	public void setBeanAmbiente(BeanAmbiente beanAmbiente) {
		this.beanAmbiente = beanAmbiente;
	}

	public BeanAmbiente getBeanAmbiente() {
		return beanAmbiente;
	}

	public void setTextoAndamentoProcesso(TextoAndamentoProcesso textoAndamentoProcesso) {
		this.textoAndamentoProcesso = textoAndamentoProcesso;
	}

	public TextoAndamentoProcesso getTextoAndamentoProcesso() {
		return textoAndamentoProcesso;
	}

	public void setTextoAndamentoProcessos(List<TextoAndamentoProcesso> textoAndamentoProcessos) {
		this.textoAndamentoProcessos = textoAndamentoProcessos;
	}

	public List<TextoAndamentoProcesso> getTextoAndamentoProcessos() {
		return textoAndamentoProcessos;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}

	public void setDocumentoEletronico(DocumentoEletronico documentoEletronico) {
		this.documentoEletronico = documentoEletronico;
	}

	public DocumentoEletronico getDocumentoEletronico() {
		return documentoEletronico;
	}


	public void setContemPecasAndamento(boolean contemPecasAndamento) {
		this.contemPecasAndamento = contemPecasAndamento;
	}

	public boolean getContemPecasAndamento() {
		return contemPecasAndamento;
	}

	public List<PecaProcessoEletronico> getListaPecasProcessoEletronico() {
		return listaPecasProcessoEletronico;
	}

	public void setListaPecasProcessoEletronico(
			List<PecaProcessoEletronico> listaPecasProcessoEletronico) {
		this.listaPecasProcessoEletronico = listaPecasProcessoEletronico;
	}
	
	public Processo getObjetoIncidentePrincipal (){ 
		return ((Processo) incidenteSelecionado.getPrincipal());
	}
	
	public void salvarNumeroUnicoProcesso(ActionEvent event) {
		try {
			processo.setNumeroUnicoProcesso(numeroUnicoProcesso);
			getProcessoService().alterar(processo);
			reportarInformacao("Número único alterado com sucesso!");
		} catch (ServiceException e) {
			log.error("Nao foi possivel salvar o numero unico do processo!", e);
			reportarErro("Não foi possível salvar o número único do processo!", e.getMessage());
		}
	}

	public Boolean getObsInternaAtualizadaPorOutroUsuario() {
		return obsInternaAtualizadaPorOutroUsuario;
	}

	public void setObsInternaAtualizadaPorOutroUsuario(
			Boolean obsInternaAtualizadaPorOutroUsuario) {
		this.obsInternaAtualizadaPorOutroUsuario = obsInternaAtualizadaPorOutroUsuario;
	}

	public Long getCodigoAndamentoSelecionado() {
		return codigoAndamentoSelecionado;
	}

	public void setCodigoAndamentoSelecionado(Long codigoAndamentoSelecionado) {
		this.codigoAndamentoSelecionado = codigoAndamentoSelecionado;
		this.codigoAndamentoSelecionadoParaCertidaoDeBaixa = codigoAndamentoSelecionado;
		setAtributo(CODIGO_ANDAMENTO_SELECIONADO, codigoAndamentoSelecionado);
		verificaPendencias();
	}
	
	public void setAndamentoSelecionado(Andamento andamentoSelecionado) {
		super.setAndamentoSelecionado(andamentoSelecionado);
		if (andamentoSelecionado != null)
			setCodigoAndamentoSelecionado(andamentoSelecionado.getId());
	}

	public boolean getSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	public boolean getTemCertidao() {
		return temCertidao;
	}

	public void setTemCertidao(boolean temCertidao) {
		this.temCertidao = temCertidao;
	}
		
		
	public void gerarAndamentoCertidaoBaixa() throws IOException, Exception {
		
		if (!sucesso) {return;}
		if (!processo.getTipoMeio().equals("E")) {
			return;
		}
		if ((!codigoAndamentoSelecionado.equals(7104L)) && (!codigoAndamentoSelecionado.equals(7101L)) && !codigoAndamentoSelecionado.equals(7108L)) {
			return;
		}
		
		if (origemSelecionada != null) {
			// A origem selecionada deve ser setada como a principal.
			HistoricoProcessoOrigem historicoProcessoOrigem = origensCadastradas.get(origemSelecionada);
			escolherOrigemPrincipal(historicoProcessoOrigem);
		}
		
		AndamentoProcessoInfoImpl andamentoProcessoInfo = montarAndamentoProcessoInfo();

		gerarCertidaoBaixa(processo, andamentoProcessoInfo);
		
		// Deslocar o Processo e atualizar o Andamento com a Guia
		if(!(andamentoProcessoInfo.getProcesso() != null 
				&& andamentoProcessoInfo != null 
				&& andamentoProcessoInfo != null  
				&& andamentoProcessoInfo != null 
				&& (andamentoProcessoInfo.getAndamento().getId() == Andamentos.REMESSA_EXTERNA_DOS_AUTOS.getId().longValue()
				||  andamentoProcessoInfo.getAndamento().getId()  == Andamentos.BAIXA_AO_ARQUIVO_DO_STF_GUIA_NO.getId().longValue()
				||  andamentoProcessoInfo.getAndamento().getId()  == Andamentos.BAIXA_DEFINITIVA_DOS_AUTOS.getId().longValue()
				||  andamentoProcessoInfo.getAndamento().getId()  == Andamentos.REMESSA_AO_JUIZO_COMPETENTE.getId().longValue())
				&& (deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo).getGuia().getCodigoOrgaoDestino().longValue() != Setor.CODIGO_SETOR_PGR.longValue()
				&& deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo).getGuia().getTipoOrgaoDestino().longValue() == Guia.CODIGO_ORIGEM_EXTERNO
				&& ((processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo().longValue() 
				&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null)
				||	(processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo().longValue() 
				&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null)
				||	(processoDependenciaService.getProcessoVinculador(processo).getTipoDependenciaProcesso() == TipoProcessoDependenciaEnum.REAUTUADO.getCodigo().longValue() 
				&& processoDependenciaService.getProcessoVinculador(processo).getDataFimDependencia() == null ))))){
			
			DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
			Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
			if ( ultimoDeslocamento != null && ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(ORGAO_EXTERNO) && !ultimoDeslocamento.getCodigoOrgaoDestino().equals(andamentoProcessoInfo.getOrigem().getId())) {
				List<Processo> processosApenso = recuperarApensos(processo);
				deslocarProcesso(getSetorUsuarioAutenticado().getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, null);
				deslocarProcessosApenso(processosApenso,usuario.getSetor());
				}
			
			try {
				AndamentoProcesso andamentoProcesso = getAndamentoProcessoService().recuperarUltimoAndamento(processo);
				DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
				if(deslocaProcesso!=null && !deslocaProcesso.getCodigoOrgaoDestino().equals(andamentoProcessoInfo.getOrigem().getId())) {
					ArrayList<Long> processos = new ArrayList<Long>();
					processos.add(processo.getId());
		
					Guia guia = new Guia();
					Guia.GuiaId guiaId = new Guia.GuiaId();
					guia.setId(guiaId);
		
					guia.setCodigoOrgaoOrigem(deslocaProcesso.getGuia().getCodigoOrgaoDestino());
					guia.setCodigoOrgaoDestino(andamentoProcessoInfo.getOrigem().getId());
					
					guia.setTipoOrgaoOrigem(deslocaProcesso.getGuia().getTipoOrgaoDestino());
					guia.setTipoOrgaoDestino(DeslocaProcesso.TIPO_ORGAO_EXTERNO);
		
					String numAnoGuia = getObjetoIncidenteService().inserirDeslocamento(guia, processos, true);
					
					if (numAnoGuia == null) {
						throw new ServiceException("Erro ao efetuar o deslocamento");
					}
					
					deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
					if(andamentoProcesso != null && andamentoProcesso.getCodigoAndamento() == andamentoProcessoInfo.getAndamento().getId())
						getDeslocaProcessoService().atualizaAndamento(deslocaProcesso,andamentoProcesso.getId() );
			
					String observacao = andamentoProcesso.getDescricaoObservacaoAndamento();
					observacao = observacao.equals("") ? "" : observacao + "\n";
					observacao = observacao + "Guia: " + deslocaProcesso.getGuia().getNumeroGuia() + "/" + deslocaProcesso.getGuia().getAnoGuia() + " - " + andamentoProcessoInfo.getOrigem().getDescricao();
					andamentoProcesso.setDescricaoObservacaoAndamento(observacao);
					getAndamentoProcessoService().alterarObsAndamento(andamentoProcesso.getId(), observacao);
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new ServiceException("Erro ao efetuar o deslocamento: "
						+ e.getMessage());
			}
			
		}
		
	}

	public boolean isLancarAndamentoEAssinarDocumento() {
		return lancarAndamentoEAssinarDocumento;
	}

	public void setLancarAndamentoEAssinarDocumento(boolean lancarAndamentoEAssinarDocumento) {
		this.lancarAndamentoEAssinarDocumento = lancarAndamentoEAssinarDocumento;
	}

	public boolean isProcessoInativo() {
		DeslocaProcesso ultimoDeslocamento = null;
		try {
			ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
			if ((ultimoDeslocamento!=null) && (ultimoDeslocamento.getCodigoOrgaoDestino().equals(CODIGO_SETOR_ACERVO_ELETRONICO_INATIVO)))
				processoInativo = true;
			else
				processoInativo = false;
		} catch (ServiceException e) {
			log.error("Nao foi possivel verificar se o processo estar inativo!", e);
			reportarErro("Não foi possível verificar se o processo estar inativo", e.getMessage());
		}
		return processoInativo;
	}

	public void setProcessoInativo(boolean processoInativo) {
		this.processoInativo = processoInativo;
	}
	
	public boolean isExistemAcordaosPendentesDePublicacao() {
		existemAcordaosPendentesDePublicacao = false;
		ControleVotoDto cvDto = new ControleVotoDto.Builder().setCvNaoLiberado(Boolean.FALSE)
							.setCvNaoRecebido(Boolean.FALSE).setCvAcordaoNaoPublicado(Boolean.FALSE)
							.setCvSemAcaoUmMinistro(Boolean.FALSE).setCvPorTodosOsMinistros(Boolean.FALSE)
							.setJulgamentoFinalizado(Boolean.FALSE).setRepercussaoGeral(Boolean.FALSE)
							.setCvDocAtivos(Boolean.FALSE).setCvCompleto(Boolean.FALSE).setIdObjetoIncidente(processo.getId())
							.builder();
			try {
				List<ResultadoControleVotoPDF> cvs = getControleVotoService().pesquisar(cvDto);
				
				for (ResultadoControleVotoPDF resultadoControleVotoPDF : cvs) {
					if (resultadoControleVotoPDF.getControleVoto().getObjetoIncidente().getDataPublicacao() == null){
						existemAcordaosPendentesDePublicacao = true;
						break;
					}
				}
			} catch (ServiceException e1) {
				log.error("Nao foi possivel verificar se existem acordaos pendendes de publicacao!", e1);
				reportarErro("Nao foi possivel verificar se existem acordaos pendendes de publicacao!", e1.getMessage());

			} catch (ValidationException e1) {
				log.error("Nao foi possivel verificar se existem acordaos pendendes de publicacao!", e1);
				reportarErro("Nao foi possivel verificar se existem acordaos pendendes de publicacao!", e1.getMessage());
			}
		
		return existemAcordaosPendentesDePublicacao;
	}

	public void setExistemAcordaosPendentesDePublicacao(
			boolean existemAcordaosPendentesDePublicacao) {
		this.existemAcordaosPendentesDePublicacao = existemAcordaosPendentesDePublicacao;
	}

	public String getMensagemDeRestricaoRegistroDeAndamento() {
		return mensagemDeRestricaoRegistroDeAndamento;
	}

	public void setMensagemDeRestricaoRegistroDeAndamento(
			String mensagemDeRestricaoRegistroDeAndamento) {
		this.mensagemDeRestricaoRegistroDeAndamento = mensagemDeRestricaoRegistroDeAndamento;
	}
	
	@Override
	public void setIncidenteSelecionado(ObjetoIncidente<?> incidenteSelecionado) {
		limparCamposObservacao();
		super.setIncidenteSelecionado(incidenteSelecionado);
		if (processo != null){
			processoInativo = isProcessoInativo();
			setAtributo(PROCESSO_INATIVO, processoInativo);
			setCodigoAndamentoSelecionado(null);
			setAndamentoSelecionado(null);
		}
	}
	
	private void verificaPendencias(){
		//Verifica se existe documentos assinados e não publicados somente para os andamentos da lista abaixo. 
		if  (codigoAndamentoSelecionado!=null && CODIGOS_AVISO_PARA_DOCUMENTOS_ASSINADOS_E_NAO_PUBLICADOS.contains(codigoAndamentoSelecionado.intValue())){
				existemAcordaosPendentesDePublicacao = isExistemAcordaosPendentesDePublicacao();
				setAtributo(ACORDAOS_PENDENTES_DE_PUBLICACAO, existemAcordaosPendentesDePublicacao);
		} else {
			existemAcordaosPendentesDePublicacao = false;
		}
		
		if (processoInativo && existemAcordaosPendentesDePublicacao){
			mensagemDeRestricaoRegistroDeAndamento = "O processo encontra-se na situação \"Inativo\".<br/>";
			mensagemDeRestricaoRegistroDeAndamento += "Há acórdão pendente de publicação neste processo.<br/><br/>";
			mensagemDeRestricaoRegistroDeAndamento += "Deseja continuar?";
		} else if (processoInativo) {
			mensagemDeRestricaoRegistroDeAndamento = "O processo encontra-se na situação \"Inativo\".<br/><br/>Deseja continuar?";
		} else if (existemAcordaosPendentesDePublicacao) {
			mensagemDeRestricaoRegistroDeAndamento = "Há acórdão pendente de publicação neste processo.<br/><br/>Deseja continuar?";
		} else if (codigoAndamentoSelecionado != null && codigoAndamentoSelecionado.equals(AUTOS_REQUISITADOSPGR)) {
			mensagemDeRestricaoRegistroDeAndamento = "O andamento 8541 (Autos Requisitados à PGR) desloca, de imediato, o processo ao STF e não é passível de invalidação posterior.<br/><br/>Deseja continuar?";
		} else {
			mensagemDeRestricaoRegistroDeAndamento = null;
		}
		
		setAtributo(MENSAGEM_DE_RESTRICAO_REGISTRO_DE_ANDAMENTO, mensagemDeRestricaoRegistroDeAndamento);
	}
	
	public boolean isAlertaApensos(){
		try{
			List<ProcessoDependencia> apensos = getProcessoDependenciaService().recuperarTodosApensadosAo(processo);
			if (!processo.getTipoMeio().equals("E") || (!isTemApensos() && (apensos == null || apensos.isEmpty())))
				return false;
			List<Long> andamentosBaixa = new ArrayList<Long>();
			andamentosBaixa.add(new Long(BAIXA_AUTOS_DILIGENCIAS));
			andamentosBaixa.add(new Long(BAIXA_EXTENA_AUTOS));
			andamentosBaixa.add(new Long(BAIXA_DEFINITIVA));
			andamentosBaixa.add(new Long(BAIXA_REMESSA_JUIZO));
			andamentosBaixa.add(new Long(BAIXA_AO_ARQUIVO));
			if (codigoAndamentoSelecionado == null || !andamentosBaixa.contains(codigoAndamentoSelecionado))
				return false;
			if (isTemApensos())
				setMensagemApensos("Este processo possui apenso(s). Deseja continuar?");
			else{
				Processo pr = getProcessoService().recuperarPorId(apensos.get(0).getIdObjetoIncidenteVinculado());
				setMensagemApensos("Este processo está apensado ao processo " + pr.getIdentificacao() +". Deseja continuar?");
			}
			return true;
		}catch(ServiceException e){
			log.error("Nao foi possivel verificar os apensos do processo!", e);
			reportarErro("Nao foi possivel verificar os apensos do processo!", e.getMessage());
			return false;
		}
	}
	
	public String getMensagemApensos(){
		return mensagemApensos;
	}
	
	public void setMensagemApensos(String msg){
		this.mensagemApensos = msg;
	}

	public Long getCodigoAndamentoSelecionadoParaCertidaoDeBaixa() {
		return codigoAndamentoSelecionadoParaCertidaoDeBaixa;
	}

	public void setCodigoAndamentoSelecionadoParaCertidaoDeBaixa(
			Long codigoAndamentoSelecionadoParaCertidaoDeBaixa) {
		this.codigoAndamentoSelecionadoParaCertidaoDeBaixa = codigoAndamentoSelecionadoParaCertidaoDeBaixa;
	}

	/**
	 * Se o parametro precisaVerificarCodigoOrigem é false é porque foi escolhido um destino de baixa e não é necessáio abrir a tela de escolha de baixa, logo este destino deve ser validado.
	 * @return
	 */
	public boolean isValidarDadosBaixa(){
		boolean validar  = false;
		if(this.andamentoSelecionado != null){
			if (!this.precisaVerificarCodigoOrigem && BeanRegistrarAndamentoParaVariosProcessos.CODIGOS_BAIXA_REMESSA.contains(this.andamentoSelecionado.getId()))
				validar = true;
		}
		return validar;
	}

	public List<PecaDTO> getListaPecasDTO() {
		
		if (incidenteSelecionado != null) {
			listaPecasDTO = converterPecasDto(incidenteSelecionado.getId(), listaPecasExpandida);
		}
		else {
			listaPecasDTO = null;
		}
		setAtributo(LISTAS_PECAS_DTO, listaPecasDTO);
		return listaPecasDTO;
	}
	
	private List<PecaDTO> converterPecasDto(Long idProcessoIncidente, boolean listaExpandida) {
		List<PecaDTO> lista = new ArrayList<PecaDTO>();
		List<PecaDTO> listaOrdenada = new ArrayList<PecaDTO>();
		if (idProcessoIncidente != null ) {
			try {

				listaOriginal = getPecaProcessoEletronicoLocalService().pesquisarPecasAndamento8507(idProcessoIncidente);
				
				if(listaOriginal.isEmpty())
				setAtributo(LISTA_ORIGINAL, listaOriginal);
				Integer qtdPecas = listaOriginal.size();
				
				for (PecaProcessoEletronico pecaProcessoEletronico : listaOriginal) {
					if (!pecaProcessoEletronico.getDocumentos().isEmpty()) {
						lista.add(preencherDTOPecas(pecaProcessoEletronico,qtdPecas ));
					}
				}

			} catch (ServiceException e) {
				log.error("Nao foi possivel pesquisar as pecas do processo!", e);
				reportarErro("Não foi possível pesquisar as pecas do processo!", e.getMessage());

			}
		}

		listaOrdenada = ordenarListaPorDataDecrescente(lista);

		if (listaOrdenada != null && listaOrdenada.size() > LIMITE_LISTA_PECAS &&  !listaExpandida) {
			for (int i = listaOrdenada.size(); i > LIMITE_LISTA_PECAS ; i--) {
				listaOrdenada.remove(i-1);	
			}
		}
		
		for (int i=0; i<listaOrdenada.size(); i++) {
			if (listaOrdenada.get(i).getIsUtilizadaEmVista().equals(NAO)) {
				pecaSelecionada = i;
				break;
			}
		}

		return listaOrdenada;
	}

	private List<PecaDTO> ordenarListaPorDataDecrescente(
			List<PecaDTO> listaPecas) {
		if (listaPecas != null && !listaPecas.isEmpty()) {
			Collections.sort(listaPecas, new Comparator<PecaDTO>() {
				@Override
				public int compare(PecaDTO pecaDto1, PecaDTO pecaDto2) {
					int resultado = 0;
					if (pecaDto1.getNumeroOrdemPeca() == null) {
						resultado = 1;
					} else if (pecaDto2.getNumeroOrdemPeca() == null) {
						resultado = -1;
					} else if (pecaDto1.getNumeroOrdemPeca() != null
							&& pecaDto2.getNumeroOrdemPeca() != null) {
						resultado = pecaDto2.getNumeroOrdemPeca().compareTo(
								pecaDto1.getNumeroOrdemPeca());
					}
					return resultado;
				}
			});
		}
		return listaPecas;
	}
    

	private PecaDTO preencherDTOPecas(PecaProcessoEletronico pe, Integer qtdPecas) throws ServiceException {
		PecaDTO dto = new PecaDTO();
		dto.setIsUtilizadaEmVista(NAO);
		dto.setChecked(false);
		dto.setAndamentoProtocolo(pe.getAndamentoProtocolo());
		dto.setDescricaoPeca(pe.getDescricaoPeca());
		dto.setDocumentos(pe.getDocumentos());
		dto.setDocumentosEletronicos(pe.getDocumentosEletronicos());
		dto.setId(pe.getId());
		dto.setLembretes(pe.getLembretes());
		dto.setNumeroOrdemPeca(pe.getNumeroOrdemPeca());
		dto.setNumeroPagFim(pe.getNumeroPagFim());
		dto.setNumeroPagInicio(pe.getNumeroPagInicio());
		dto.setObjetoIncidente(pe.getObjetoIncidente());
		dto.setSetor(pe.getSetor());
		dto.setTipoOrigemPeca(pe.getTipoOrigemPeca());
		dto.setTipoPecaProcesso(pe.getTipoPecaProcesso());
		dto.setTipoSituacaoPeca(pe.getTipoSituacaoPeca());
		dto.setDataInclusao(pe.getDataInclusao());
		dto.setUsuarioInclusao(pe.getUsuarioInclusao());
		dto.setDataAlteracao(pe.getDataAlteracao());
		dto.setUsuarioAlteracao(pe.getUsuarioAlteracao());
		if (dto.getDocumentos() != null && !dto.getDocumentos().isEmpty()) {
			ArquivoProcessoEletronico arquivoProcessoEletronico = dto.getDocumentos().get(0);
			String url = montaUrlDownload(arquivoProcessoEletronico);
			dto.setUrlDownloadPeca(url);
			if(qtdPecas < 500) {
				dto.setIsUtilizadaEmVista(getIsUtilizaEmVista(pe.getDocumentos().get(0).getDocumentoEletronico().getId()));
			}
		}
		return dto;
	}
	
	public List<SelectItem> getPecasSelectItens() {
		pecasSelectItens = new ArrayList<SelectItem>();
		if (listaPecasDTO != null) {
			for (int i = 0; i < listaPecasDTO.size(); i++) {
				pecasSelectItens.add(new SelectItem(i, ""));
			}
		}
		return pecasSelectItens;
	}
	
	public String getIsUtilizaEmVista(Long idDocumentoPeca) throws ServiceException{
		List<TextoAndamentoProcesso> pecasComVista = getPecaProcessoEletronicoLocalService().pesquisarPecasUtilizadasEmVista(incidenteSelecionado.getId());
		Boolean isVista = false;
		for(TextoAndamentoProcesso texto : pecasComVista){
			if(idDocumentoPeca.equals(texto.getSeqDocumento()) ){
				isVista = true;
			}
		}
		return isVista ? SIM : NAO;
	}
	
	public void verificarProcessoEFisicoEmVista(Processo processo) throws Exception{
		List<Processo> apensos = recuperarApensos(processo);
		for(Processo processoApenso : apensos ){
			if(processoApenso.getTipoMeio().equals(TIPO_MEIO_FISICO)){
				throw new ServiceException("Erro ao lançar o andamento Vista. Existem processos físicos apensados a esse processo eletrônico");		
			}
		}	
	}
	
	public List<Processo> recuperarApensos(Processo processo) throws Exception {
		List<ProcessoDependencia> apensos = getProcessoDependenciaService().recuperarApensos(processo);
	
		for (ProcessoDependencia apenso: apensos ) {
			Processo processoApenso = getProcessoService().recuperarProcesso(apenso.getClasseProcesso(), apenso.getNumeroProcesso());
			processosApenso.add(processoApenso);
			recuperarApensos(processoApenso);
		}
		return processosApenso;
		
	}
	
	private Comunicacao gerarComunicacaoVistaAGU(AndamentoProcesso andamentoProcesso, boolean intimacao) throws ServiceException, IOException, DaoException {
		
		String descricaoComunicacao = "COMUNICAÇÃO DE VISTA";
		Long numeroComunicacao = 142L;
		ModeloComunicacaoEnum modeloComunicacao = ModeloComunicacaoEnum.VISTA_A_AGU;
		Collection<Long> objetoIncedentes = Arrays.asList(processo.getId());
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteService().recuperarPorId(processo.getId());
		
		List<PecaProcessoEletronico> listaPecaRecuperada = null;
		DocumentoEletronico documentoEletronicoComunicacao = null;
		
		String nomeProcesso = processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual(); 
		String nomeArq = "TERMO_DE_VISTA_" + nomeProcesso.replaceAll(" ", "") + "_";
		byte[] arquivoEletronico = getProcessamentoRelatorioService().gerarPdfComunicacaoDeVista(incidenteSelecionado,usuario.getSetor(), ProcessamentoRelatorioServiceLocal.DESTINATARIO_AGU);
		nomePDFBaixa = getProcessamentoRelatorioService().gerarPDF(nomeArq, arquivoEletronico);
		String siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_VISTA_AGU;
		ArquivoProcessoEletronico arquivoProcessoEletronico = getArquivoProcessoEletronicoService().salvarPecaEletronica(arquivoEletronico, siglaTipoPeca, objetoIncidente, usuario.getSetor(), andamentoProcesso);
		getArquivoProcessoEletronicoService().flushSession();
		listaPecaRecuperada = Arrays.asList(arquivoProcessoEletronico.getPecaProcessoEletronico());
		documentoEletronicoComunicacao = arquivoProcessoEletronico.getDocumentoEletronico();
		getAndamentoProcessoService().flushSession();
		
		
		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(numeroComunicacao);
		List<Origem> origem = getOrigemService().recuperarApenasAgu();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();

		if (processo.getTipoMeio().equals("E"))
			return getComunicacaoServiceLocal().criarComunicacaoIntimacao(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modeloComunicacao, objetoIncedentes, TipoFaseComunicacao.ENVIADO, listaPecaRecuperada, null, numeroDocumento, descricaoComunicacao, null, documentoEletronicoComunicacao); 
		
		return null;
	}
	
	private Comunicacao gerarComunicacaoVistaPGR(AndamentoProcesso andamentoProcesso, boolean intimacao) throws ServiceException, IOException {
		
		String descricaoComunicacao = "COMUNICAÇÃO DE VISTA";
		Long numeroComunicacao = 142L;
		ModeloComunicacaoEnum modeloComunicacao = ModeloComunicacaoEnum.VISTA_A_PGR;
		Collection<Long> objetoIncedentes = Arrays.asList(processo.getId());
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteService().recuperarPorId(processo.getId());
		
		List<PecaProcessoEletronico> listaPecaRecuperada = null;
		DocumentoEletronico documentoEletronicoComunicacao = null;
		
		if (intimacao) {
			descricaoComunicacao = "COMUNICAÇÃO DE VISTA PARA FINS DE INTIMAÇÃO";
			numeroComunicacao = 121L;
			modeloComunicacao = ModeloComunicacaoEnum.INTIMACAO_DESPACHO_DECISAO_ACORDAO;
			
			PecaDTO pecaDTO = null;
			
			if (pecaSelecionada != null) {
				pecaDTO = listaPecasDTO.get(pecaSelecionada);
				
				for(PecaProcessoEletronico pecaProcessoEletronico: listaOriginal){
					if(pecaProcessoEletronico.getId() == pecaDTO.getId()){
						listaPecaRecuperada = Arrays.asList(pecaProcessoEletronico);
					}
				}
			}
			
			TextoAndamentoProcesso textoAndamentoProcesso =  new TextoAndamentoProcesso();
			textoAndamentoProcesso.setAndamentoProcesso(andamentoProcesso);
			textoAndamentoProcesso.setDataInclusao(new Date());
			
			if (pecaDTO != null)
				textoAndamentoProcesso.setSeqDocumento(pecaDTO.getDocumentos().get(0).getDocumentoEletronico().getId());
			
			getTextoAndamentoProcessoService().persistirTextoAndamentoProcesso(textoAndamentoProcesso);
			
		} else {
			String nomeProcesso = processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual(); 
			String nomeArq = "TERMO_DE_VISTA_" + nomeProcesso.replaceAll(" ", "") + "_";
			byte[] arquivoEletronico = getProcessamentoRelatorioService().gerarPdfComunicacaoDeVista(incidenteSelecionado,usuario.getSetor(), ProcessamentoRelatorioServiceLocal.DESTINATARIO_PGR);
			nomePDFBaixa = getProcessamentoRelatorioService().gerarPDF(nomeArq, arquivoEletronico);
			String siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_VISTA_PGR;
			ArquivoProcessoEletronico arquivoProcessoEletronico = getArquivoProcessoEletronicoService().salvarPecaEletronica(arquivoEletronico, siglaTipoPeca, objetoIncidente, usuario.getSetor(), andamentoProcesso);
			getArquivoProcessoEletronicoService().flushSession();
			listaPecaRecuperada = Arrays.asList(arquivoProcessoEletronico.getPecaProcessoEletronico());
			documentoEletronicoComunicacao = arquivoProcessoEletronico.getDocumentoEletronico();
			getAndamentoProcessoService().flushSession();
		}
		
		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(numeroComunicacao);
		List<Origem> origem = getOrigemService().recuperarApenasPgr();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();

		if (processo.getTipoMeio().equals("E"))
		   return getComunicacaoServiceLocal().criarComunicacaoIntimacao(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modeloComunicacao, objetoIncedentes, TipoFaseComunicacao.ENVIADO, listaPecaRecuperada, null, numeroDocumento, descricaoComunicacao, null, documentoEletronicoComunicacao); 
		
		return null;
	}
	
	private Comunicacao gerarComunicacaoAutosDisponibilizados(AndamentoProcesso andamentoProcesso, boolean intimacao) throws ServiceException, IOException {
		
		String descricaoComunicacao = "AUTOS DISPONIBILIZADOS à AUTORIDADE POLICIAL";
		Long numeroComunicacao = 123L;
		ModeloComunicacaoEnum modeloComunicacao = ModeloComunicacaoEnum.NOTIFICACAO_AUTOS_DISPONIBILIZADOS;
		Collection<Long> objetoIncedentes = Arrays.asList(processo.getId());
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteService().recuperarPorId(processo.getId());
		
		List<PecaProcessoEletronico> listaPecaRecuperada = null;
		DocumentoEletronico documentoEletronicoComunicacao = null;
		
		if (intimacao) {
			descricaoComunicacao = "AUTOS DISPONIBILIZADOS à AUTORIDADE POLICIAL";
			numeroComunicacao = 123L;
			modeloComunicacao = ModeloComunicacaoEnum.INTIMACAO_DESPACHO_DECISAO_ACORDAO;
			
			PecaDTO pecaDTO = null;
			
			if (pecaSelecionada != null) {
				pecaDTO = listaPecasDTO.get(pecaSelecionada);
				
				for(PecaProcessoEletronico pecaProcessoEletronico: listaOriginal){
					if(pecaProcessoEletronico.getId() == pecaDTO.getId()){
						listaPecaRecuperada = Arrays.asList(pecaProcessoEletronico);
					}
				}
			}
			
			TextoAndamentoProcesso textoAndamentoProcesso =  new TextoAndamentoProcesso();
			textoAndamentoProcesso.setAndamentoProcesso(andamentoProcesso);
			textoAndamentoProcesso.setDataInclusao(new Date());
			
			if (pecaDTO != null)
				textoAndamentoProcesso.setSeqDocumento(pecaDTO.getDocumentos().get(0).getDocumentoEletronico().getId());
			
			getTextoAndamentoProcessoService().persistirTextoAndamentoProcesso(textoAndamentoProcesso);
			
		} else {
			String nomeProcesso = processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual(); 
			String nomeArq = "AUTOS_DISPONIBILIZADOS_" + nomeProcesso.replaceAll(" ", "") + "_";
			byte[] arquivoEletronico = getProcessamentoRelatorioService().gerarPdfComunicacaoAutosDisp(incidenteSelecionado,usuario.getSetor(), ProcessamentoRelatorioServiceLocal.DESTINATARIO_DPF);
			nomePDFBaixa = getProcessamentoRelatorioService().gerarPDF(nomeArq, arquivoEletronico);
			String siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_TERMO_AUTOS_DISP;
			ArquivoProcessoEletronico arquivoProcessoEletronico = getArquivoProcessoEletronicoService().salvarPecaEletronica(arquivoEletronico, siglaTipoPeca, objetoIncidente, usuario.getSetor(), andamentoProcesso);
			getArquivoProcessoEletronicoService().flushSession();
			listaPecaRecuperada = Arrays.asList(arquivoProcessoEletronico.getPecaProcessoEletronico());
			documentoEletronicoComunicacao = arquivoProcessoEletronico.getDocumentoEletronico();
			getAndamentoProcessoService().flushSession();
		}
		 
		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(numeroComunicacao);
		List<Origem> origem = getOrigemService().recuperarApenasDpf();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();

		if (processo.getTipoMeio().equals("E"))
		   return getComunicacaoServiceLocal().criarComunicacaoAutosDisponibilizados(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modeloComunicacao, objetoIncedentes, TipoFaseComunicacao.ENVIADO, listaPecaRecuperada, null, numeroDocumento, descricaoComunicacao, null, documentoEletronicoComunicacao); 
		
		return null;
	}

	
	private Comunicacao gerarComunicacaoIndevidoVistaPGR(boolean intimacao) throws Exception {

		String mensagem = MSG_COMUNICAO_INDEVIDO_VISTA;
		ModeloComunicacaoEnum modelo = ModeloComunicacaoEnum.NOTIFICACAO_INDEVIDO_VISTA;
				
		if (intimacao) {
			mensagem = MSG_COMUNICAO_INDEVIDO_VISTA_INTIMACAO;
			modelo = ModeloComunicacaoEnum.NOTIFICACAO_INDEVIDO_VISTA_INTIMACAO;
		}
		
		Comunicacao comunicacao = null;

		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(123L);
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		List<Origem> origem = getOrigemService().recuperarApenasPgr();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();
		Collection<Long> objetoIncedentes = Arrays.asList(processo.getId());
		
		comunicacao = getComunicacaoServiceLocal().criarComunicacaoIntimacao(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modelo, objetoIncedentes, TipoFaseComunicacao.ENVIADO, null, null, numeroDocumento, mensagem, null, null);
		
		if (!intimacao) {
			Setor setor = getMapeamentoClasseSetorService().recuperarSetorDeDestinoDoDeslocamento(processo);
			List<Processo> processosApenso = recuperarApensos(processo);
			deslocarProcesso(setor.getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, ANDAMENTO_INDEVIDO);
			deslocarProcessosApenso(processosApenso,setor);	
		}
		
		return comunicacao;
	}
	
	private Comunicacao gerarComunicacaoIndevidoAutosDisp() throws Exception {

		String mensagem = MSG_COMUNICAO_INDEVIDO_AUTOS_DISP;
		ModeloComunicacaoEnum modelo = ModeloComunicacaoEnum.NOTIFICACAO_INDEVIDO_AUTOS_DISPONIBILIZADOS;
				
		Comunicacao comunicacao = null;

		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(123L);
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		List<Origem> origem = getOrigemService().recuperarApenasDpf();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();
		Collection<Long> objetoIncedentes = Arrays.asList(processo.getId());
		
		comunicacao = getComunicacaoServiceLocal().criarComunicacaoIndevidoAutosDisp(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modelo, objetoIncedentes, TipoFaseComunicacao.ENVIADO, null, null, numeroDocumento, mensagem, null, null);

		return comunicacao;
	}
	
	private Comunicacao gerarComunicacaoIndevidoVistaAGU() throws Exception {

		String mensagem = MSG_COMUNICAO_INDEVIDO_VISTA_AGU;
		ModeloComunicacaoEnum modelo = ModeloComunicacaoEnum.NOTIFICACAO_INDEVIDO_VISTA_AGU;
				
		Comunicacao comunicacao = null;

		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(123L);
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		List<Origem> origem = getOrigemService().recuperarApenasAgu();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();
		Collection<Long> objetoIncedentes = Arrays.asList(processo.getId());
		
		comunicacao = getComunicacaoServiceLocal().criarComunicacaoIntimacao(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null, modelo, objetoIncedentes, TipoFaseComunicacao.ENVIADO, null, null, numeroDocumento, mensagem, null, null);
		
		Setor setor = getMapeamentoClasseSetorService().recuperarSetorDeDestinoDoDeslocamento(processo);
		List<Processo> processosApenso = recuperarApensos(processo);
		deslocarProcesso(setor.getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, ANDAMENTO_INDEVIDO);
		deslocarProcessosApenso(processosApenso,setor);	
		
		return comunicacao;
	}

	public Comunicacao gerarComunicacaoAutosRequisitadosPGR() throws Exception{
		
		Comunicacao comunicacaoIntimacao = null;
		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(123L);
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		List<Origem> origem = getOrigemService().recuperarApenasPgr();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();
		Collection<Long> objetoIncedentes = Arrays.asList(processo.getId());		
		
		comunicacaoIntimacao = getComunicacaoServiceLocal().criarComunicacaoIntimacao(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null , ModeloComunicacaoEnum.NOTIFICACAO_REQUISICAO_PROCESSO_SEJ ,objetoIncedentes, TipoFaseComunicacao.ENVIADO , null, null , numeroDocumento, MSG_COMUNICAO_REQUISAO_PROCESSO, null, null);

		Setor setor = getMapeamentoClasseSetorService().recuperarSetorDeDestinoDoDeslocamento(processo);

		DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
	
		if ( ultimoDeslocamento != null && ultimoDeslocamento.getCodigoOrgaoDestino().equals(Setor.CODIGO_SETOR_PGR) ) {
			List<Processo> processosApenso = recuperarApensos(processo);
			deslocarProcesso(setor.getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, andamentoSelecionado.getId());
			deslocarProcessosApenso(processosApenso,setor);
		}
		
		return comunicacaoIntimacao;
	}
	
	public Comunicacao gerarComunicacaoAutosRequisitadosAGU() throws Exception{
		
		Comunicacao comunicacaoIntimacao = null;
		Long numeroDocumento = getTipoComunicacaoLocalService().gerarProximoNumeroComunicacao(123L);
		Usuario usuario = getUsuarioService().recuperarUsuario(getUser().getUsername().toUpperCase());
		List<Origem> origem = getOrigemService().recuperarApenasAgu();
		Long pessoaDestinataria = origem.get(0).getPessoa().getId();
		Collection<Long> objetoIncedentes = Arrays.asList(processo.getId());		
		
		comunicacaoIntimacao = getComunicacaoServiceLocal().criarComunicacaoIntimacao(new Date(), usuario.getSetor(), usuario.getId(), pessoaDestinataria !=null ? pessoaDestinataria : null , 
				ModeloComunicacaoEnum.NOTIFICACAO_REQUISICAO_PROCESSO_SEJ ,objetoIncedentes, TipoFaseComunicacao.ENVIADO , null, null , numeroDocumento, MSG_COMUNICAO_REQUISAO_PROCESSO, null, null);

		Setor setor = getMapeamentoClasseSetorService().recuperarSetorDeDestinoDoDeslocamento(processo);

		DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
	
		if ( ultimoDeslocamento != null && ultimoDeslocamento.getCodigoOrgaoDestino().equals(Setor.CODIGO_SETOR_AGU) ) {
			List<Processo> processosApenso = recuperarApensos(processo);
			deslocarProcesso(setor.getId(), processo, ORGAO_EXTERNO, ORGAO_INTERNO, andamentoSelecionado.getId());
			deslocarProcessosApenso(processosApenso,setor);
		} else {
			throw new ServiceException("Este processo não está deslocado para a AGU");
		}
		
		return comunicacaoIntimacao;
	}
	
	public void deslocarProcessosApenso(List<Processo> processosApenso , Setor setor) throws Exception {
		
		for (Processo processoApenso: processosApenso ) {
			DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processoApenso);
			if(ultimoDeslocamento.getCodigoOrgaoDestino() != setor.getId()){
				deslocarProcesso(setor.getId(), processoApenso, ORGAO_EXTERNO, ORGAO_INTERNO, andamentoSelecionado.getId());
			}
		}	
	}

	public void setListaPecasDTO(List<PecaDTO> listaPecasDTO) {
		this.listaPecasDTO = listaPecasDTO;
	}

	public void setNumeroPecas(Integer numeroPecas) {
		this.numeroPecas = numeroPecas;
	}
	
	public Integer getIndexPeca(){
		return tabelaPecasSelecionadas.getRowIndex();
	}
	
	public HtmlDataTable getTabelaPecasSelecionadas() {
		return tabelaPecasSelecionadas;
	}

	public void setTabelaPecasSelecionadas(
			HtmlDataTable tabelaPecaSelelecionadas) {
		this.tabelaPecasSelecionadas = tabelaPecaSelelecionadas;
	}
	
	public void expandirListaPecas(ActionEvent e) {
		setAtributo(PECAS_EXPANDIDAS, true);
		this.listaPecasExpandida = true;
	}
	
	public boolean getExibirExpandirListaPecas() {
		return listaOriginal != null && listaOriginal.size() > LIMITE_LISTA_PECAS;
	}
	
	/**
	 * disponibiliza a pesquisa para o componente suggestion na interface
	 * o método pesquisarDestinatario() está implementado no AssinadorBaseBean
	 */
	@SuppressWarnings("unchecked")
	public List<ResultSuggestionOrigemDestino> pesquisarDestinatarioAction(Object value) {
		return  pesquisarOrigem(value);
	}
	
}
