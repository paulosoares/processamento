package br.gov.stf.estf.assinatura.visao.jsf.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.richfaces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.ODSingleXMLDocument;

import br.gov.stf.estf.assinatura.deslocamento.origemdestino.ResultSuggestionOrigemDestino;
import br.gov.stf.estf.assinatura.relatorio.service.ProcessamentoRelatorioService;
import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.service.AndamentoProcessoServiceLocal;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.service.DeslocaProcessoServiceLocal;
import br.gov.stf.estf.assinatura.service.TagsLivresServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentosexterno.BeanConsultaExterna;
import br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentosexterno.ConsultaExternaVO;
import br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento.RequestService;
import br.gov.stf.estf.assinatura.visao.jsf.beans.usuario.BeanUsuario;
import br.gov.stf.estf.assinatura.visao.servlet.DocumentoDownloadServlet;
import br.gov.stf.estf.assinatura.visao.util.CheckableDataTableRowWrapperDeslocaProcesso;
import br.gov.stf.estf.assinatura.visao.util.IdentificaoProcessualComunicacaoTableRowComparator;
import br.gov.stf.estf.assinatura.visao.util.ItemControleSearchData;
import br.gov.stf.estf.assinatura.visao.util.PeticaoParser;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.RefreshController;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.assinatura.visao.util.constantes.Constantes;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.configuracao.model.service.AlertaSistemaService;
import br.gov.stf.estf.configuracao.model.service.ConfiguracaoSistemaService;
import br.gov.stf.estf.converter.DocumentConverterService;
import br.gov.stf.estf.corp.model.service.LogradouroService;
import br.gov.stf.estf.corp.model.service.MunicipioService;
import br.gov.stf.estf.corp.model.service.UnidadeFederacaoService;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.service.DeslocamentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoPeticaoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.PermissaoDeslocamentoService;
import br.gov.stf.estf.documento.model.service.TagsLivresUsuarioService;
import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.documento.model.service.TextoDiversoService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.documento.model.service.TipoComunicacaoESTFService;
import br.gov.stf.estf.documento.model.service.TipoComunicacaoService;
import br.gov.stf.estf.documento.model.service.TipoPecaProcessoService;
import br.gov.stf.estf.documento.model.service.TipoPermissaoModeloComunicacaoService;
import br.gov.stf.estf.documento.model.service.TipoTagsLivresUsuarioService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Destinatario;
import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoPrescricaoParte;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.expedicao.model.service.ConfiguracaoEncaminhamentoService;
import br.gov.stf.estf.expedicao.model.service.ContratoPostagemService;
import br.gov.stf.estf.expedicao.model.service.DestinatarioListaRemessaService;
import br.gov.stf.estf.expedicao.model.service.ExpedicaoRelatorioService;
import br.gov.stf.estf.expedicao.model.service.ListaRemessaService;
import br.gov.stf.estf.expedicao.model.service.RemessaService;
import br.gov.stf.estf.expedicao.model.service.RemetenteService;
import br.gov.stf.estf.expedicao.model.service.TipoComunicacaoExpedicaoService;
import br.gov.stf.estf.expedicao.model.service.TipoEmbalagemService;
import br.gov.stf.estf.expedicao.model.service.TipoServicoService;
import br.gov.stf.estf.expedicao.model.service.UnidadePostagemService;
import br.gov.stf.estf.expedicao.model.service.VwEnderecoService;
import br.gov.stf.estf.expedicao.model.service.VwServidorAssinadorService;
import br.gov.stf.estf.intimacao.model.service.AndamentoProcessoComunicacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.IntimacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.ParteLocalService;
import br.gov.stf.estf.intimacao.model.service.PartesGerarIntimacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.PecaProcessoEletronicoLocalService;
import br.gov.stf.estf.intimacao.model.service.ProcessoLocalService;
import br.gov.stf.estf.intimacao.model.service.TipoComunicacaoLocalService;
import br.gov.stf.estf.julgamento.model.service.ProcessoTemaService;
import br.gov.stf.estf.julgamento.model.service.TemaService;
import br.gov.stf.estf.jurisdicionado.model.service.AssociacaoJurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.EmprestimoAutosProcessoService;
import br.gov.stf.estf.jurisdicionado.model.service.EnderecoJurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.IdentificacaoPessoaService;
import br.gov.stf.estf.jurisdicionado.model.service.JurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.PapelJurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.TelefoneJurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.TipoIdentificacaoService;
import br.gov.stf.estf.jurisdicionado.model.service.TipoJurisdicionadoService;
import br.gov.stf.estf.localizacao.model.service.AdvogadoService;
import br.gov.stf.estf.localizacao.model.service.ContatoDestinatarioService;
import br.gov.stf.estf.localizacao.model.service.DestinatarioService;
import br.gov.stf.estf.localizacao.model.service.EnderecoDestinatarioService;
import br.gov.stf.estf.localizacao.model.service.OrigemDestinoService;
import br.gov.stf.estf.localizacao.model.service.OrigemService;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.log.model.service.LogControleProcessService;
import br.gov.stf.estf.ministro.model.service.ExclusaoDistribuicaoService;
import br.gov.stf.estf.ministro.model.service.MinistroPresidenteService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processosetor.model.service.PeticaoSetorService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoComunicacaoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.estf.processostf.model.service.CategoriaService;
import br.gov.stf.estf.processostf.model.service.ClasseService;
import br.gov.stf.estf.processostf.model.service.ControlarDeslocaIncidenteService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.DeslocamentoPeticaoService;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.estf.processostf.model.service.HistoricoProcessoOrigemService;
import br.gov.stf.estf.processostf.model.service.IncidenteJulgamentoService;
import br.gov.stf.estf.processostf.model.service.IncidentePreferenciaService;
import br.gov.stf.estf.processostf.model.service.ItemControleService;
import br.gov.stf.estf.processostf.model.service.MapeamentoClasseSetorService;
import br.gov.stf.estf.processostf.model.service.NumeroProcessoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.OrgaoService;
import br.gov.stf.estf.processostf.model.service.OrigemAndamentoDecisaoService;
import br.gov.stf.estf.processostf.model.service.ParteService;
import br.gov.stf.estf.processostf.model.service.PeticaoService;
import br.gov.stf.estf.processostf.model.service.PrescricaoReuService;
import br.gov.stf.estf.processostf.model.service.ProcedenciaService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.service.ProcessoIntegracaoService;
import br.gov.stf.estf.processostf.model.service.ProcessoInteresseService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.service.RecursoProcessoService;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.estf.processostf.model.service.TipoControleService;
import br.gov.stf.estf.processostf.model.service.TipoDevolucaoService;
import br.gov.stf.estf.processostf.model.service.TipoSituacaoControleService;
import br.gov.stf.estf.processostf.model.service.VerificadorPerfilService;
import br.gov.stf.estf.processostf.model.util.ItemControleResult;
import br.gov.stf.estf.publicacao.model.service.ProcessoPublicadoService;
import br.gov.stf.estf.usuario.model.service.IntegracaoDocumentoService;
import br.gov.stf.estf.usuario.model.service.TipoGrupoControleService;
import br.gov.stf.estf.usuario.model.service.TipoGrupoUsuarioControleService;
import br.gov.stf.estf.usuario.model.service.UsuarioExternoService;
import br.gov.stf.estf.usuario.model.service.UsuarioIncidentePesquisaService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.AbstractJsfFacesBean;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class AssinadorBaseBean extends AbstractJsfFacesBean implements VerificadorPerfilService {

	public static String VISUALIZAR_DOCUMENTO_PDF = "visualizarDocumentoPDF";

	private static final long serialVersionUID = -698723512003850252L;

	public static final long CODIGO_INTIMACAO_ELETRONICA_DISPONIBILIZADA = 8531L;
    public static final long CODIGO_CITACAO_ELETRONICA_DISPONIBILIZADA = 8529L;

	private static final Log LOG = LogFactory.getLog(AssinadorBaseBean.class);

	private static final TipoObjetoIncidente[] TIPOS_OBJETOS_INCIDENTES_PERMITIDOS = { TipoObjetoIncidente.PROCESSO, TipoObjetoIncidente.RECURSO,
			TipoObjetoIncidente.INCIDENTE_JULGAMENTO };

	private String appVersion;

	private Long seqObjetoIncidente;
	private Long seqDocumentoPeca;
	private String nomeDocumentoDownload;
	private byte[] conteudoDocumentoDownload;
	private PecaProcessoEletronicoComunicacao valor;
	private IdentificaoProcessualComunicacaoTableRowComparator idProcessualComunicacaoComparator;
	private RefreshController refreshController;
	
	private static final String SISTEMA_PROCESSAMENTO = "PROCESSAMENTO";
	private static final String LISTA_SETORES_DESLOCAMENTO = "lista.setores.deslocamento.";
	protected Long idSetorSalaOficiais = 600000687L;
	protected String idUsuarioAtribuicao;


	// ----------------- GETTERS DE SERVICES -----------------

	public RefreshController getRefreshController() {
		if (refreshController == null) {
			refreshController = (RefreshController) getService("refreshController");
		}

		return refreshController;
	}

    protected AndamentoProcessoComunicacaoLocalService getAndamentoProcessoComunicacaoLocalServicee() {
        return (AndamentoProcessoComunicacaoLocalService) getService("andamentoProcessoComunicacaoLocalService");
    }
	
    protected AndamentoProcessoComunicacaoService getAndamentoProcessoComunicacaoService() {
        return (AndamentoProcessoComunicacaoService) getService("andamentoProcessoComunicacaoService");
    }
	
	protected PartesGerarIntimacaoLocalService getPartesGerarIntimacaoLocalService() {
		return (PartesGerarIntimacaoLocalService) getService("partesGerarIntimacaoLocalService");
	}

	protected IntimacaoLocalService getIntimacaoLocalService() {
		return (IntimacaoLocalService) getService("intimacaoLocalService");
	}

    protected ParteLocalService getParteLocalService() {
        return (ParteLocalService) getService("parteLocalServiceIntimacao");
    }

    protected ProcessoLocalService getProcessoLocalService() {
        return (ProcessoLocalService) getService("processoLocalServiceIntimacao");
    }

    protected LogControleProcessService getLogControleProcessService() {
		return (LogControleProcessService) getService("logControleProcessService");
	}

	protected IntegracaoDocumentoService getIntegracaoDocumentoService() {
		return (IntegracaoDocumentoService) getService("integracaoDocumentoService");
	}

	protected ComunicacaoServiceLocal getComunicacaoServiceLocal() {
		return (ComunicacaoServiceLocal) getService("comunicacaoServiceLocal");
	}

	protected TagsLivresServiceLocal getTagsLivresServiceLocal() {
		return (TagsLivresServiceLocal) getService("tagsLivresServiceLocal");
	}

	protected ProcessamentoRelatorioService getProcessamentoRelatorioService() {
		return (ProcessamentoRelatorioService) getService("processamentoRelatorioService");
	}
	
	protected ExpedicaoRelatorioService getExpedicaoRelatorioService() {
		return (ExpedicaoRelatorioService) getService("relatorioService");
	}

	protected DeslocamentoPeticaoService getDeslocamentoPeticaoService() {
		return (DeslocamentoPeticaoService) getService("deslocamentoPeticaoService");
	}

	protected GuiaService getGuiaService() {
		return (GuiaService) getService("guiaService");
	}

	protected ControlarDeslocaIncidenteService getControlarDeslocaIncidenteService() {
		return (ControlarDeslocaIncidenteService) getService("controlarDeslocaIncidenteService");
	}

	protected PeticaoService getPeticaoService() {
		return (PeticaoService) getService("peticaoService");
	}

	protected DeslocaProcessoService getDeslocaProcessoService() {
		return (DeslocaProcessoService) getService("deslocaProcessoService");
	}
	
    protected DeslocaProcessoServiceLocal getDeslocaProcessoServiceLocal() {
        return (DeslocaProcessoServiceLocal) getService("deslocaProcessoServiceLocal");
    }	

	protected JurisdicionadoService getJurisdicionadoService() {
		return (JurisdicionadoService) getService("jurisdicionadoService");
	}

	protected OrigemService getOrigemService() {
		return (OrigemService) getService("origemService");
	}

	protected OrgaoService getOrgaoService() {
		return (OrgaoService) getService("orgaoService");
	}

	protected AdvogadoService getAdvogadoService() {
		return (AdvogadoService) getService("advogadoService");
	}

	protected SetorService getSetorService() {
		return (SetorService) getService("setorService");
	}

	protected TextoService getTextoService() {
		return (TextoService) getService("textoService");
	}

	protected OrigemDestinoService getOrigemDestinoService() {
		return (OrigemDestinoService) getService("origemDestinoService");
	}

	protected PrescricaoReuService getPrescricaoReuService() {
		return (PrescricaoReuService) getService("prescricaoReuService");
	}
	
	protected UsuarioIncidentePesquisaService getUsuarioIncidentePesquisaService() {
		return (UsuarioIncidentePesquisaService) getService("usuarioIncidentePesquisaService");
	}

	protected ProcessoTemaService getProcessoTemaService() {
		return (ProcessoTemaService) getService("processoTemaService");
	}
	
    protected TemaService getTemaService(){
    	return (TemaService) getService("temaService");
    }
    
	protected RequestService getRequestService() {
		   return (RequestService) getService("requestService");
		}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getComunicacaoServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected ComunicacaoService getComunicacaoService() {
		return (ComunicacaoService) getService("comunicacaoService");
	}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getComunicacaoServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected DeslocamentoComunicacaoService getDeslocamentoComunicacaoService() {
		return (DeslocamentoComunicacaoService) getService("deslocamentoComunicacaoService");
	}

	protected TipoDevolucaoService getTipoDevolucaoService() {
		return (TipoDevolucaoService) getService("tipoDevolucaoService");
	}

	protected MinistroService getMinistroService() {
		return (MinistroService) getService("ministroService");
	}

	protected MinistroPresidenteService getMinistroPresidenteService() {
		return (MinistroPresidenteService) getService("ministroPresidenteService");
	}

	protected ProcessoService getProcessoService() {
		return (ProcessoService) getService("processoService");
	}
	
	protected IncidentePreferenciaService getIncidentePreferenciaService() {
		return (IncidentePreferenciaService) getService("incidentePreferenciaService");
	}
	
	protected AndamentoProcessoService getAndamentoProcessoService() {
		return (AndamentoProcessoService) getService("andamentoProcessoService");
	}
	
	protected AndamentoProcessoServiceLocal getAndamentoProcessoServiceLocal() {
		return (AndamentoProcessoServiceLocal) getService("andamentoProcessoServiceLocal");
	}

	protected AndamentoService getAndamentoService() {
		return (AndamentoService) getService("andamentoService");
	}

	protected DocumentoTextoService getDocumentoTextoService() {
		return (DocumentoTextoService) getService("documentoTextoService");
	}

	protected DocumentoTextoPeticaoService getDocumentoTextoPeticaoService() {
		return (DocumentoTextoPeticaoService) getService("documentoTextoPeticaoService");
	}

	protected DocumentoEletronicoService getDocumentoEletronicoService() {
		return (DocumentoEletronicoService) getService("documentoEletronicoService");
	}

	protected PecaProcessoEletronicoService getPecaProcessoEletronicoService() {
		return (PecaProcessoEletronicoService) getService("pecaProcessoEletronicoService");
	}
    
    protected TextoAndamentoProcessoService getTextoAndamentoProcessoService() {
    	return (TextoAndamentoProcessoService) getService("textoAndamentoProcessoService");
    }

    protected PecaProcessoEletronicoLocalService getPecaProcessoEletronicoLocalService() {
        return (PecaProcessoEletronicoLocalService) getService("pecaProcessoEletronicoLocalService");
    }

    protected ArquivoProcessoEletronicoService getArquivoProcessoEletronicoService() {
		return (ArquivoProcessoEletronicoService) getService("arquivoProcessoEletronicoService");
	}

	protected RecursoProcessoService getRecursoProcessoService() {
		return (RecursoProcessoService) getService("recursoProcessoService");
	}

	protected ObjetoIncidenteService getObjetoIncidenteService() {
		return (ObjetoIncidenteService) getService("objetoIncidenteService");
	}

	protected IncidenteJulgamentoService getIncidenteJulgamentoService() {
		return (IncidenteJulgamentoService) getService("incidenteJulgamentoService");
	}

	protected TextoDiversoService getTextoDiversoService() {
		return (TextoDiversoService) getService("textoDiversoService");
	}

	protected ClasseService getClasseService() {
		return (ClasseService) getService("classeService");
	}

	protected DestinatarioService getDestinatarioService() {
		return (DestinatarioService) getService("destinatarioService");
	}
	
	protected PermissaoDeslocamentoService getPermissaoDeslocamentoService() {
		return (PermissaoDeslocamentoService) getService("permissaoDeslocamentoService");
	}

	protected EnderecoDestinatarioService getEnderecoDestinatarioService() {
		return (EnderecoDestinatarioService) getService("enderecoDestinatarioService");
	}
	
	protected ExclusaoDistribuicaoService getExclusaoDistribuicaoService() {
		return (ExclusaoDistribuicaoService) getService("exclusaoDistribuicaoService");
	}
	
	protected ConfiguracaoSistemaService getConfiguracaoSistemaService() {
		return (ConfiguracaoSistemaService) getService("configuracaoSistemaService");
	}
	
	protected AlertaSistemaService getAlertaSistemaService() {
		return (AlertaSistemaService) getService("alertaSistemaService");
	}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getComunicacaoServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected ModeloComunicacaoService getModeloComunicacaoService() {
		return (ModeloComunicacaoService) getService("modeloComunicacaoService");
	}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getComunicacaoServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected TipoPermissaoModeloComunicacaoService getTipoPermissaoModeloComunicacaoService() {
		return (TipoPermissaoModeloComunicacaoService) getService("tipoPermissaoModeloComunicacaoService");
	}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getComunicacaoServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected DocumentoComunicacaoService getDocumentoComunicacaoService() {
		return (DocumentoComunicacaoService) getService("documentoComunicacaoService");
	}

	protected ParteService getParteoService() {
		return (ParteService) getService("parteService");
	}

	protected SituacaoMinistroProcessoService getSituacaoMinistroProcessoService() {
		return (SituacaoMinistroProcessoService) getService("situacaoMinistroProcessoService");
	}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getComunicacaoServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected TipoComunicacaoService getTipoComunicacaoService() {
		return (TipoComunicacaoService) getService("tipoComunicacaoService");
	}
    
	protected TipoComunicacaoLocalService getTipoComunicacaoLocalService() {
        return (TipoComunicacaoLocalService) getService("tipoComunicacaoLocalService");
    }
 
	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getComunicacaoServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected FaseComunicacaoService getFaseComunicacaoService() {
		return (FaseComunicacaoService) getService("faseComunicacaoService");
	}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getComunicacaoServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected ComunicacaoIncidenteService getComunicacaoIncidenteService() {
		return (ComunicacaoIncidenteService) getService("comunicacaoIncidenteService");
	}

	protected OrigemAndamentoDecisaoService getOrigemAndamentoDecisaoService() {
		return (OrigemAndamentoDecisaoService) getService("origemAndamentoDecisaoService");
	}

	protected MapeamentoClasseSetorService getMapeamentoClasseSetorService() {
		return (MapeamentoClasseSetorService) getService("mapeamentoClasseSetorService");
	}

	protected DocumentConverterService getConverterService() {
		return (DocumentConverterService) getService("converterService");
	}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getTagsLivresServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected TagsLivresUsuarioService getTagsLivresUsuarioService() {
		return (TagsLivresUsuarioService) getService("tagsLivresUsuarioService");
	}

	/**
	 * @deprecated Utilizar o serviço local (acessível através do método {@link #getTagsLivresServiceLocal()}) para realizar as operações que seriam feitas
	 *             através deste.
	 */
	@Deprecated
	protected TipoTagsLivresUsuarioService getTipoTagsLivresUsuarioService() {
		return (TipoTagsLivresUsuarioService) getService("tipoTagsLivresUsuarioService");
	}

	protected PecaProcessoEletronicoComunicacaoService getPecaProcessoEletronicoComunicacaoService() {
		return (PecaProcessoEletronicoComunicacaoService) getService("pecaProcessoEletronicoComunicacaoService");
	}

	protected CategoriaService getCategoriaService() {
		return (CategoriaService) getService("categoriaService");
	}

	protected TipoControleService getTipoControleService() {
		return (TipoControleService) getService("tipoControleService");
	}

	protected ItemControleService getItemControleService() {
		return (ItemControleService) getService("itemControleService");
	}

	protected TipoGrupoControleService getTipoGrupoControleService() {
		return (TipoGrupoControleService) getService("tipoGrupoControleService");
	}

	protected TipoGrupoUsuarioControleService getTipoGrupoUsuarioControleService() {
		return (TipoGrupoUsuarioControleService) getService("tipoGrupoUsuarioControleService");
	}

	protected UsuarioService getUsuarioService() {
		return (UsuarioService) getService("usuarioService");
	}

	protected TipoSituacaoControleService getTipoSituacaoControleService() {
		return (TipoSituacaoControleService) getService("tipoSituacaoControleService");
	}

	protected HistoricoProcessoOrigemService getHistoricoProcessoOrigemService() {
		return (HistoricoProcessoOrigemService) getService("historicoProcessoOrigemService");
	}

	protected ProcedenciaService getProcedenciaService() {
		return (ProcedenciaService) getService("procedenciaService");
	}

	protected ProcessoDependenciaService getProcessoDependenciaService() {
		return (ProcessoDependenciaService) getService("processoDependenciaService");
	}

	protected CabecalhoObjetoIncidenteService getCabecalhoObjetoIncidenteService() {
		return (CabecalhoObjetoIncidenteService) getService("cabecalhoObjetoIncidenteService");
	}

	protected UsuarioExternoService getUsuarioExternoService() {
		return (UsuarioExternoService) getService("usuarioExternoService");
	}

	protected ProcessoIntegracaoService getProcessoIntegracaoService() {
		return (ProcessoIntegracaoService) getService("processoIntegracaoService");
	}

	protected TipoComunicacaoESTFService getTipoComunicacaoESTFService() {
		return (TipoComunicacaoESTFService) getService("tipoComunicacaoServiceESTF");
	}

	protected ContatoDestinatarioService getContatoDestinatarioService() {
		return (ContatoDestinatarioService) getService("contatoDestinatarioService");
	}

	protected ProcessoPublicadoService getProcessoPublicadoService() {
		return (ProcessoPublicadoService) getService("processoPublicadoService");
	}

	protected EnderecoJurisdicionadoService getEnderecoJurisdicionadoService() {
		return (EnderecoJurisdicionadoService) getService("enderecoJurisdicionadoService");
	}
	
	protected TelefoneJurisdicionadoService getTelefoneJurisdicionadoService() {
		return (TelefoneJurisdicionadoService) getService("telefoneJurisdicionadoService");
	}
	
	protected TipoIdentificacaoService getTipoIdentificacaoService() {
		return (TipoIdentificacaoService) getService("tipoIdentificacaoService");
	}
	
	protected PapelJurisdicionadoService getPapelJurisdicionadoService() {
		return (PapelJurisdicionadoService) getService("papelJurisdicionadoService");
	}
	
	protected TipoJurisdicionadoService getTipoJurisdicionadoService() {
		return (TipoJurisdicionadoService) getService("tipoJurisdicionadoService");
	}
	
	protected IdentificacaoPessoaService getIdentificacaoPessoaService() {
		return (IdentificacaoPessoaService) getService("identificacaoPessoaService");
	}
	
	protected AssociacaoJurisdicionadoService getAssociacaoJurisdicionadoService() {
		return (AssociacaoJurisdicionadoService) getService("associacaoJurisdicionadoService");
	}
	
	protected EmprestimoAutosProcessoService getEmprestimoAutosProcessoService(){
		return (EmprestimoAutosProcessoService) getService("emprestimoAutosProcessoService");
	}
	
	protected ProcessoInteresseService getProcessoInteresseService(){
		return (ProcessoInteresseService) getService("processoInteresseService");
	}

	protected PeticaoSetorService getPeticaoSetorService(){
		return (PeticaoSetorService) getService("peticaoSetorService");
	}

	protected UnidadePostagemService getUnidadePostagemService() {
		return (UnidadePostagemService) getService("unidadePostagemService");
	}

	protected TipoComunicacaoExpedicaoService getTipoComunicacaoExpedicaoService() {
		return (TipoComunicacaoExpedicaoService) getService("tipoComunicacaoExpedicaoService");
	}

	protected TipoServicoService getTipoServicoService() {
		return (TipoServicoService) getService("tipoServicoService");
	}

	protected RemessaService getRemessaService() {
		return (RemessaService) getService("remessaService");
	}

	protected ListaRemessaService getListaRemessaService() {
		return (ListaRemessaService) getService("listaRemessaService");
	}

	protected DestinatarioListaRemessaService getDestinatarioListaRemessaService() {
		return (DestinatarioListaRemessaService) getService("destinatarioListaRemessaService");
	}

	protected LogradouroService getLogradouroService() {
		return (LogradouroService) getService("logradouroService");
	}

	protected MunicipioService getMunicipioService() {
		return (MunicipioService) getService("municipioService");
	}

	protected UnidadeFederacaoService getUnidadeFederacaoService() {
		return (UnidadeFederacaoService) getService("unidadeFederacaoService");
	}

	protected TipoEmbalagemService getTipoEmbalagemService() {
		return (TipoEmbalagemService) getService("tipoEmbalagemService");
	}

	protected RemetenteService getRemetenteService() {
		return (RemetenteService) getService("remetenteService");
	}

	protected ContratoPostagemService getContratoPostagemService() {
		return (ContratoPostagemService) getService("contratoPostagemService");
	}

	protected VwEnderecoService getVwEnderecoService() {
		return (VwEnderecoService) getService("vwEnderecoService");
	}

	protected ConfiguracaoEncaminhamentoService getConfiguracaoEncaminhamentoService() {
		return (ConfiguracaoEncaminhamentoService) getService("configuracaoEncaminhamentoService");
	}

	protected VwServidorAssinadorService getVwServidorAssinadorService() {
		return (VwServidorAssinadorService) getService("vwServidorAssinadorService");
	}
	
	protected TipoPecaProcessoService getTipoPecaProcessoService(){
		return (TipoPecaProcessoService) getService("tipoPecaProcessoService");
	}
	
	protected NumeroProcessoService getNumeroProcessoService(){
		return (NumeroProcessoService) getService("numeroProcessoService");
	}
	
	protected ControleVotoService getControleVotoService(){
		return (ControleVotoService) getService("controleVotoService");
	}

	// ----------------------- MÉTODOS UTILITÁRIOS -----------------------

	/**
	 * Obtém uma lista de CheckableDataTableRowWrapper que empacotem os objetos ComunicacaoDocumentoResult existentes na lista passada por parâmetro.
	 */
	public List<CheckableDataTableRowWrapper> getCheckableDocumentoList(List<ComunicacaoDocumentoResult> documentos) {
		List<ComunicacaoDocumento> listaDocumentos = new ArrayList<ComunicacaoDocumento>();

		if (documentos != null && documentos.size() > 0) {
			for (ComunicacaoDocumentoResult documento : documentos) {
				listaDocumentos.add(new ComunicacaoDocumento(documento));
			}
		}

		return getCheckableDataTableRowWrapperList(listaDocumentos);
	}

	/**
	 * Encapsula um objeto em um CheckableDataTableRowWrapper.<br />
	 * @author EdvaldoO
	 * @param Object
	 * @return CheckableDataTableRowWrapper
	 */
	protected CheckableDataTableRowWrapper wrappedObjectInCheckableDataTableRowWrapper(Object object) {
		  return new CheckableDataTableRowWrapper(object);
	  }
	  
	public List<CheckableDataTableRowWrapper> getCheckableDocumentoListItemControle(List<ItemControleResult> itensControleResult) {
		List<ItemControleSearchData> listaItensControle = new ArrayList<ItemControleSearchData>();
		for (ItemControleResult itemControleResult : itensControleResult) {
			listaItensControle.add(new ItemControleSearchData(itemControleResult));
		}
		return getCheckableDataTableRowWrapperList(listaItensControle);
	}

	public List<CheckableDataTableRowWrapper> getCheckableDocumentoListProcessoPrescricao(List<ProcessoPrescricaoParte> processos) {
		List<ProcessoPrescricaoParte> listaProcPresc = new ArrayList<ProcessoPrescricaoParte>();
		if (processos != null && processos.size() > 0) {
			for (ProcessoPrescricaoParte pr : processos) {
				listaProcPresc.add(pr);
			}
		}
		return getCheckableDataTableRowWrapperList(listaProcPresc);
	}

	protected List getCheckableDataTableRowWrapperDeslocaProcesso(List originalList) {
		List<CheckableDataTableRowWrapperDeslocaProcesso> newList = new ArrayList<CheckableDataTableRowWrapperDeslocaProcesso>();

		if (originalList != null) {
			for (int i = 0; i < originalList.size(); ++i)
				newList.add(new CheckableDataTableRowWrapperDeslocaProcesso(originalList.get(i), i));
		}

		return newList;
	}

	/**
	 * Devolve um arquivo de PDF como resposta de uma requisição.
	 */
	protected void setPDFResponse(byte[] array, String nome) {
		try {
			HttpServletResponse response = (HttpServletResponse) getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=" + nome + ".pdf");
			ServletOutputStream outputStream = response.getOutputStream();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(array);

			IOUtils.copy(inputStream, outputStream);
			IOUtils.closeQuietly(inputStream);

			response.flushBuffer();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public List pesquisarOrigem(Object value) {
		try {

			List<ResultSuggestionOrigemDestino> listaDestinatarios = new ArrayList<ResultSuggestionOrigemDestino>();

			if (value == null) {
				return null;
			}
			if (value.toString().trim() == "") {
				return null;
			}

			// recupera a lista de origemDestino e adiciona os objetos a cada objeto result (utilizado no suggestion)
			// ResultSuggestionOrigemDestiono 1<---->1 OrigemDestino (um para um)

			List<OrigemDestino> origensDestinos = getOrigemDestinoService().recuperarPorIdOuDescricaoSetoresEOrgaosExternos(value.toString());

			for (Object objeto : origensDestinos) {
				ResultSuggestionOrigemDestino result = new ResultSuggestionOrigemDestino();
				result.setOrigemDestino((OrigemDestino) objeto);
				listaDestinatarios.add(result);
			}

			return listaDestinatarios;

		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar o destinatário: " + value.toString());
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public List pesquisarOrgao(Object value) {

		if (value == null) {
			return null;
		}
		if (value.toString().trim() == "") {
			return null;
		}

		List<Orgao> listOrgaos = new ArrayList<Orgao>();
		try {
			if (value != null) {
				if (NumberUtils.soNumeros(value.toString())) {
					listOrgaos.add(getOrgaoService().recuperarPorId(Long.parseLong(value.toString())));
				} else {
					listOrgaos.addAll(getOrgaoService().pesquisarPelaDescricaoOrgaosAtivos(value.toString()));
				}
			}

			return listOrgaos;

		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar o destinatário: " + value.toString());
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public List pesquisarProcedencia(Object value) {
		if (value == null) {
			return null;
		}
		if (value.toString().trim() == "") {
			return null;
		}

		// recupera a lista de Procedência e adiciona os objetos a cada objeto result (utilizado no suggestion)
		List<Procedencia> listProcedencia = new ArrayList<Procedencia>();
		try {
			if (NumberUtils.soNumeros(value.toString())) {
				listProcedencia.add(getProcedenciaService().recuperarPorId(Long.parseLong(value.toString())));
			} else {
				listProcedencia.addAll(getProcedenciaService().pesquisarProcedenciasDescricaoAtivas(value.toString().toUpperCase()));
			}
			return listProcedencia;

		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar o destinatário: " + value.toString());
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public List pesquisarDestinatario(Object value) {

		if (value == null) {
			return null;
		}
		if (value.toString().trim() == "") {
			return null;
		}

		// recupera a lista de Destinatario (JUDICIARIO.DESTINATARIO_ORIGEM) e adiciona os objetos a cada objeto result (utilizado no suggestion)

		List<Destinatario> listDestinatario = new ArrayList<Destinatario>();
		try {
			if (value != null) {
				if (NumberUtils.soNumeros(value.toString())) {
					listDestinatario.add(getDestinatarioService().recuperarPorId(Long.parseLong(value.toString())));
				} else {
					listDestinatario.addAll(getDestinatarioService().pesquisarDestinatarioDescricao(value.toString()));
				}
			}

			return listDestinatario;

		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar o destinatário: " + value.toString());
			return null;
		}
	}

	public List<ResultSuggestionOrigemDestino> pesquisarOrigensDestino(Object value) {
		try {

			List<ResultSuggestionOrigemDestino> listaDestinatarios = new ArrayList<ResultSuggestionOrigemDestino>();

			if (value == null) {
				return null;
			}
			if (value.toString().trim() == "") {
				return null;
			}

			// recupera a lista de origemDestino e adiciona os objetos a cada objeto result (utilizado no suggestion)
			// ResultSuggestionOrigemDestiono 1<---->1 OrigemDestino (um para um)
			
			List<OrigemDestino> origensDestinos = new ArrayList<OrigemDestino>();
			if(value != null){
				if( NumberUtils.soNumeros(value.toString())){
					origensDestinos = getOrigemDestinoService().recuperarPorId(value.toString(), true, true);
				}else {
					origensDestinos = getOrigemDestinoService().recuperarPorDescricao(value.toString(), true, true);
				}
			}
			
			for (Object objeto : origensDestinos) {
				ResultSuggestionOrigemDestino result = new ResultSuggestionOrigemDestino(); 
				result.setOrigemDestino((OrigemDestino) objeto);
				listaDestinatarios.add(result);
			}
			
			return listaDestinatarios;
			
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar o destinatário: " + value.toString());
			return null;
		}
	}

	// pesquisa tendo como entrada a petição
	@SuppressWarnings("rawtypes")
	public List pesquisarIncidentesPeticao(Object value) throws ServiceException, NumberFormatException {

		List<ObjetoIncidente> listaPeticoes = new ArrayList<ObjetoIncidente>();
		try {
			String valor = value.toString().trim();
			valor = valor.replace("/", "");
			valor = valor.replace(" ", "");
			valor = valor.replace("\t", "");
			valor = valor.replace("\n", "");

			StringBuilder strOriginal = new StringBuilder(valor);
			// StringBuilder strOriginal = new StringBuilder(value.toString().replace("/",
			// "").trim());

			if (strOriginal.length() <= 4) {
				return null;
			} // somente prossegue se existir pelo menos um digito do numero e o ano
			StringBuilder strInvertida = strOriginal.reverse();

			// recuperar o ano da petição na string
			String anoInvertido = strInvertida.toString().trim();
			anoInvertido = anoInvertido.substring(0, 4);
			StringBuilder ano = new StringBuilder(anoInvertido);
			ano = ano.reverse();

			// recuperar o resto da string (numero da petição)
			String numeroInvertido = strInvertida.toString();
			numeroInvertido = numeroInvertido.substring(4, numeroInvertido.length());
			StringBuilder numero = new StringBuilder(numeroInvertido);
			numero = numero.reverse();

			// cria um objeto peticao para ser parametro na pesquisa
			PeticaoService peticaoService = getPeticaoService();
			// recupera a petição e o processo (principal)
			Peticao peticao = peticaoService.recuperarPeticao(Long.parseLong(numero.toString()), Short.parseShort(ano.toString()));
			if (peticao == null) {
				return null;
			}

			ObjetoIncidente obIncidente = peticao.getObjetoIncidente();
			listaPeticoes.add(obIncidente);

			return listaPeticoes;

		} catch (NumberFormatException e) {
			reportarErro("Favor informar número e ano da petição no formato '99999/AAAA' ou '99999AAAA'.");
			return listaPeticoes;
		} catch (ServiceException e) {
			reportarErro("Ocorreu um erro na pesquisa da petição: " + e.getMessage());
			return listaPeticoes;
		}
	}
	
	/**
	 * descobre o tipo da guia (o meio da petição ou processo) pela informação digitada ou objeto incidente
	 * @param value - numero+ano da petição ou sigla+numero do processo ou um objetoIncidente
	 * @return PRE = Guia de Processo Eletrônico 
	 * 		   PRO = Guia de Processo Físico
	 * 		   PET = Guia de Petição Física
	 * 		   PEE = Guia Petição Eletrônica
	 */
	public String descobrirTipoGuia(Object value) {
		String tipo;
		Processo proc = null;
		Peticao pet = null;
		try {
			if (value instanceof ObjetoIncidente){
				ObjetoIncidente<?> objetoIncidente = (ObjetoIncidente<?>) value;
				if (objetoIncidente.getTipoObjetoIncidente().getCodigo().equals("PA")) {
					// é petição
					pet = getPeticaoService().recuperarPeticao(objetoIncidente.getId());
				} else {
					proc = getProcessoService().recuperarPorId(objetoIncidente.getId());
				}
			} else {
				Character primeiroCaracter = ((String) value).charAt(0);
				if (!Character.isDigit(primeiroCaracter)) {
					Long numProc = ProcessoParser.getNumero(value.toString());
					String classProc = ProcessoParser.getSigla(value.toString());
					proc = getProcessoService().recuperarProcesso(classProc, numProc);
				} else {
					Long numeroPet = PeticaoParser.getNumeroPeticao(value.toString());
					Short anoPet = PeticaoParser.getAnoPeticao(value.toString());
					pet = getPeticaoService().recuperarPeticao(numeroPet, anoPet);
				}
			}
			if (proc == null && pet == null) {
				return null;
			}
			if (proc != null) {
				if (proc.getTipoMeio().equals("E")) {
					tipo = "PRE";
				} else {
					tipo = "PRO";
				}
			} else {
				// verifica se o processo vinculado à petição é do tipo meio Eletrônico ou Físico.
				if (pet.getObjetoIncidenteVinculado() == null) {
					return "PET";
				}
				Processo processoVinculado = null;
				Peticao peticaoVinculada = null;
				// se o objeto vinculado for um processo
				if (pet.getObjetoIncidenteVinculado().getTipoObjetoIncidente().getCodigo().equals("PR")) {
					processoVinculado = getProcessoService().recuperarPorId(pet.getObjetoIncidenteVinculado().getId());
				} else {
					peticaoVinculada = getPeticaoService().recuperarPeticao(pet.getObjetoIncidenteVinculado().getId());
				}
				if (processoVinculado == null || peticaoVinculada != null) {
					tipo = "PET";
				// quando pendente de digitalização ou remessa indevida a petição é considerada física.	
				} else if ( (pet.getPendenteDigitalizacao()!=null && pet.getPendenteDigitalizacao()) ) {
					tipo = "PET";
				} else if ( (pet.getRemessaIndevida()!=null && pet.getRemessaIndevida()) ) {
					tipo = "PET";
				} else if ( processoVinculado.getTipoMeio().equals("E") ) {
					tipo = "PEE";
				} else if (processoVinculado.getTipoMeio().equals("F")) {
					tipo = "PET";
				} else {
					tipo = "PET";
				}
			}
			return tipo;
		} catch (ServiceException e) {
			reportarErro("Ocorreu um erro na recuperação do tipo da guia: " + e.getMessage());
			return null;
		}

	}

	public ODSingleXMLDocument geraODSingleXMLDocumentoParaProcura(byte[] conteudoModelo) {

		ByteArrayInputStream byteArray = new ByteArrayInputStream(conteudoModelo);

		ODPackage pacote = null;

		try {
			pacote = new ODPackage(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pacote.toSingle();
	}

	/**
	 * Utilizar ao invés deste um dos métodos:
	 * 
	 * <ul>
	 * <li>{@link #retornarItensCheckableSelecionados(List)}</li>
	 * <li>{@link #retornarItensSelecionados(List)}</li>
	 * </ul>
	 */
	@Override
	@Deprecated
	public List retornarItensSelecionados(List lista, Boolean retornarCheckable) {
		return super.retornarItensSelecionados(lista, retornarCheckable);
	}

	/**
	 * Obtém os itens selecionados em uma lista de CheckableDataTableRowWrapper que empacotem os itens.
	 * 
	 * @param <T>
	 * @param listaCheckable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> retornarItensSelecionados(List<? extends CheckableDataTableRowWrapper> listaCheckable) {
		return super.retornarItensSelecionados(listaCheckable, false);
	}

	/**
	 * Obtem os objetos CheckableDataTableRowWrapper selecionados em uma lista.
	 * 
	 * @param <W>
	 *            Tipo de wrapper, filho da classe CheckableDataTableRowWrapper
	 * @param listaCheckable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <W extends CheckableDataTableRowWrapper> List<W> retornarItensCheckableSelecionados(List<W> listaCheckable) {
		List<W> selecionados = super.retornarItensSelecionados(listaCheckable, true);

		// resolve o bug na hora de remover itens da lista - os itens vem sem ID
		for (W w : selecionados) {
			Object obj = w.getWrappedObject();

			if (obj instanceof ComunicacaoDocumento) {
				ComunicacaoDocumento comunicacaoDocumento = (ComunicacaoDocumento) obj;
				DocumentoComunicacao documentoComunicacao = comunicacaoDocumento.getDocumentoComunicacao();
				Comunicacao comunicacao = comunicacaoDocumento.getComunicacao();

				if (documentoComunicacao != null) {
					w.setId(documentoComunicacao.getId());
				} else if (comunicacao != null) {
					w.setId(comunicacao.getId());
				}
			}
		}

		return selecionados;
	}

	/**
	 * Obtém uma lista de objetos CheckableDataTableRowWrapper a partir de uma lista de quaisquer objetos.<br />
	 * <br />
	 * 
	 * <b>NOTA:</b> Para fins de comodidade, este método é sobrescrito de modo que declare retornar uma lista de CheckableDataTableRowWrapper ao invés de uma
	 * lista genérica, como está no método original.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List<CheckableDataTableRowWrapper> getCheckableDataTableRowWrapperList(List originalList) {
		return super.getCheckableDataTableRowWrapperList(originalList);
	}

	public MinistroPresidente verificarGabineteAtualPresidencia() {

		MinistroPresidente ministroPresidente = null;

		try {
			ministroPresidente = getMinistroPresidenteService().recuperarMinistroPresidenteAtual();
		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar o Ministro Presidente.", e, LOG);
		}

		return ministroPresidente;
	}

	// ----------------------- USUÁRIO & SETOR -----------------------

	protected Setor getSetorUsuarioAutenticado() {
		return ((UsuarioAssinatura) getUser()).getSetor();
	}

	public Boolean getIsSetorPresidencia() {
		if (getSetorUsuarioAutenticado().getId().equals(Setor.CODIGO_SETOR_PRESIDENCIA)) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	protected boolean isUsuarioAdmin() {
		return isUserInRole(BeanUsuario.RS_ADM_ASSINADOR);
	}

	protected boolean isUsuarioAssinaturaTextos() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_ASSINATURA_TEXTOS);
	}

	protected boolean isUsuarioEditorTextos() {
		return isUserInRole(BeanUsuario.RS_EDICAO_TEXTOS);
	}

	protected boolean isUsuarioRevisorTextos() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_REVISAO_TEXTOS);
	}

	protected boolean isUsuarioElaboracaoTextos() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_ELABORACAO_TEXTOS);
	}

	protected boolean isUsuarioGestorTextos() {
		return isUserInRole(BeanUsuario.RS_GESTAO_TEXTOS);
	}

	protected boolean isUsuarioInstitucional() {
		return isUserInRole(BeanUsuario.RS_EDICAO_MODELOS_INSTITUCIONAIS);
	}

	public boolean isUsuarioMaster() {
		return isUserInRole(BeanUsuario.RS_MASTER_PROCESSAMENTO);
	}

	public boolean isUsuarioEditarAndamentoProcesso() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_EDITAR_ANDAMENTO_PROCESSO);
	}

	@Override
	public boolean isUsuarioRegistrarAndamentoDistribuidoForaSetor() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_REGISTRAR_ANDAMENTO_DISTRIBUIDO_FORA_DO_SETOR);
	}

	@Override
	public boolean isUsuarioRegistrarAndamentoNaoDistribuido() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_REGISTRAR_ANDAMENTO_NAO_DISTRIBUIDO);
	}

	@Override
	public boolean isUsuarioRegistrarAndamentoIndevidoRG() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_REGISTRAR_ANDAMENTO_INDEVIDO_RG);
	}

	public boolean isUsuarioRegistrarAndamentoIndevido() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_REGISTRAR_ANDAMENTO_INDEVIDO);
	}

	protected boolean isUsuarioPecaPendentePublica() {
		return isUsuarioAdminPecas() || isUserInRole(BeanUsuario.RS_PENDENTE_PUBLICA);
	}

	protected boolean isUsuarioPecaPublicaPendente() {
		return isUsuarioAdminPecas() || isUserInRole(BeanUsuario.RS_PUBLICA_PENDENTE);
	}

	protected boolean isUsuarioAdminPecas() {
		return isUsuarioMaster() || isUserInRole(BeanUsuario.RS_ADM_PECAS);
	}

	protected boolean isUsuarioGabineteSEJ() {
		return isUserInRole(BeanUsuario.RS_GABINETE_SEJ);
	}

	// ---------------- COMBOS UTILIZADAS PELOS OUTROS BEANS ---------------

	protected List<SelectItem> carregarComboRecursos(Long numeroProcesso, String siglaClasse) throws ServiceException {
		List<SelectItem> itens = Collections.nCopies(1, new SelectItem(0L, ""));
		if (numeroProcesso != null && numeroProcesso > 0 && siglaClasse != null && siglaClasse.trim().length() > 0) {
			RecursoProcessoService recursoProcessoService = getRecursoProcessoService();
			List<RecursoProcesso> recursos = recursoProcessoService.pesquisar(siglaClasse, numeroProcesso);
			if (recursos != null && recursos.size() > 0) {
				itens = new ArrayList<SelectItem>();
				itens.add(new SelectItem(0L, ""));
				for (RecursoProcesso rp : recursos) {
					TipoRecurso tr = rp.getTipoRecursoProcesso();
					itens.add(new SelectItem(tr.getId(), tr.getDescricao()));
				}
				return itens;
			}

		}
		return itens;
	}

	public List<SelectItem> carregarComboTipoSituacaoPeca() {
		List<SelectItem> tiposSituacaoPeca = new LinkedList<SelectItem>();

		tiposSituacaoPeca.add(new SelectItem("TODAS", "Todas"));
		tiposSituacaoPeca.add(new SelectItem(DocumentoEletronico.TIPO_ACESSO_INTERNO, "Pendentes"));
		tiposSituacaoPeca.add(new SelectItem(DocumentoEletronico.TIPO_ACESSO_PUBLICO, "Públicas"));

		return tiposSituacaoPeca;
	}

	public List<Ministro> carregarComboMinistros() {

		List<Ministro> listaMinistros = new LinkedList<Ministro>();
		MinistroService ministroService = getMinistroService();
		try {
			listaMinistros = ministroService.pesquisarMinistrosAtivos();
		} catch (ServiceException e) {
			reportarErro("Erro ao carregar a combo de Ministros");
		}
		return listaMinistros;
	}

	public List<SelectItem> carregarComboMinistros1() {

		List<Ministro> listaMinistros = new LinkedList<Ministro>();
		List<SelectItem> listaMinistros1 = new LinkedList<SelectItem>();
		MinistroService ministroService = getMinistroService();
		try {
			listaMinistros = ministroService.pesquisarMinistrosAtivos();
		} catch (ServiceException e) {
			reportarErro("Erro ao carregar a combo de Ministros");
		}

		if (listaMinistros != null && listaMinistros.size() > 0) {
			for (Ministro min : listaMinistros) {
				listaMinistros1.add(new SelectItem(min.getNomeMinistroCapsulado(true), min.getNomeMinistroCapsulado(true)));
			}
		}

		return listaMinistros1;
	}

	/**
	 * Carrega uma lista de itens contendo os tipos de permissões existentes
	 * 
	 * @param incluirInstitucional
	 *            define se a permissão Institucional será incluída - este parâmetro será ignorado caso o usuário seja Master ou Institucional
	 * @param selecionarPermissaoPadrao
	 * @return
	 */
	public List<SelectItem> carregarComboTipoPermissao(boolean incluirInstitucional, boolean selecionarPermissaoPadrao) {
		List<SelectItem> lista = new LinkedList<SelectItem>();

		try {
			TipoPermissaoModeloComunicacaoService tipoPermissaoService = getTipoPermissaoModeloComunicacaoService();
			List<TipoPermissaoModeloComunicacao> tiposPermissoes = null;

			// JIRA PROCESSAMENTO-430 -- PEDIDO DA SEJ - PATRICIAP - SECRETARIA JUDICIARIA
			// if (isUsuarioInstitucional() || isUsuarioMaster()) {
			// todas as permissões
			tiposPermissoes = tipoPermissaoService.pesquisarPermissoes(null, true);
			// } else {
			// tiposPermissoes = tipoPermissaoService.pesquisarPermissoes(getSetorUsuarioAutenticado(), incluirInstitucional);
			// }

			if (selecionarPermissaoPadrao) {
				selecionarPermissaoPadrao(tiposPermissoes);
			}

			for (TipoPermissaoModeloComunicacao tipoPermissao : tiposPermissoes) {
				lista.add(new SelectItem(tipoPermissao.getId(), tipoPermissao.getDescricao()));
			}
		} catch (ServiceException exception) {
			reportarErro("Erro ao recuperar permissões existentes", exception.getLocalizedMessage());
		}

		return lista;
	}

	// a permissão padrão será movida para o início da lista (índice 0)
	private void selecionarPermissaoPadrao(List<TipoPermissaoModeloComunicacao> tiposPermissoes) {
		TipoPermissaoModeloComunicacao tipoPermissaoPadrao = null;

		for (TipoPermissaoModeloComunicacao tipoPermissaoModeloComunicacao : tiposPermissoes) {
			if (isUsuarioInstitucional() || isUsuarioMaster()) {
				if (tipoPermissaoModeloComunicacao.isInstitucional()) {
					tipoPermissaoPadrao = tipoPermissaoModeloComunicacao;
					break;
				}
			} else if (getSetorUsuarioAutenticado().equals(tipoPermissaoModeloComunicacao.getSetor())) {
				tipoPermissaoPadrao = tipoPermissaoModeloComunicacao;
				break;
			}
		}

		if (tipoPermissaoPadrao != null) {
			tiposPermissoes.remove(tipoPermissaoPadrao);
			tiposPermissoes.add(0, tipoPermissaoPadrao);
		}
	}

	public List<SelectItem> carregarComboAndamentoModelo() {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		lista.add(new SelectItem(null, null));
		lista.add(new SelectItem(8450L, "COMUNICAÇÃO ASSINADA - 8450"));
		lista.add(new SelectItem(7800L, "CERTIDÃO - 7800"));
		lista.add(new SelectItem(8219L, "TRANSITADO(A) EM JULGADO - 8219"));
		lista.add(new SelectItem(8504L, "ATO ORDINATÓRIO - 8504"));
		lista.add(new SelectItem(8215L, "DECORRIDO O PRAZO - 8215"));
		return lista;
	}

	public List<SelectItem> carregarComboSetoresDestino(Boolean todos, Boolean parametro) {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		List<Setor> listaSetores = Collections.emptyList();
		if(parametro) {
			try {
					ConfiguracaoSistema configuracaoSistema = this.getConfiguracaoSistemaService().recuperarValor(SISTEMA_PROCESSAMENTO, LISTA_SETORES_DESLOCAMENTO + getSetorUsuarioAutenticado().getId());
					if(configuracaoSistema != null) {
						String[] listaSetoresSEJ = configuracaoSistema.getValor().split(",");
						if(listaSetoresSEJ.length > 0) {
							for(String iSetor : listaSetoresSEJ) {
								Long cSetor = Long.parseLong(iSetor);
								Setor setor = getSetorService().recuperarPorId(cSetor);
								if (setor != null) {
									lista.add(new SelectItem(cSetor, setor.getNome()));
								}
							}
						}
					}
				} catch (ServiceException exception) {
					reportarErro("Erro ao pesquisar setores.", exception.getLocalizedMessage());
				}
		}
		if (lista.size() == 0L) {
			if (!todos) {
				lista.add(new SelectItem(null, null));
				lista.add(new SelectItem(new Long(600000902L), "EXPEDIÇÃO DE COMUNICAÇÕES PROCESSUAIS E AUTOS DE PROCESSOS FÍSICOS"));
				lista.add(new SelectItem(new Long(600000627L), "SECRETARIA JUDICIÁRIA"));
			}
			else{
				lista.add(new SelectItem(null, null));
				try {
						listaSetores = getSetorService().pesquisarSetoresAtivosDeslocaComunicacao(true);
					} catch (ServiceException exception) {
						reportarErro("Erro ao pesquisar setores.", exception.getLocalizedMessage());
					}
					for (Setor setor : listaSetores) {
						lista.add(new SelectItem(setor.getId(), setor.getNome()));
					}
				}
			}
		return lista;
	}
	
	public List<SelectItem> carregarComboUsuariosDoSetor(Long codigoSetor) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		List<Usuario> listaUsuarios = Collections.emptyList();
	
		lista.add(new SelectItem("", ""));
		
		if(codigoSetor != null){
			try {
				listaUsuarios = getUsuarioService().pesquisaUsuario(null, null, null, codigoSetor);
			} catch (ServiceException exception) {
				reportarErro("Erro ao pesquisar usuários.", exception.getLocalizedMessage());
			}
	
			for (Usuario usuario : listaUsuarios) {
				lista.add(new SelectItem(usuario.getId(), usuario.getNome()));
			}
		}

		return lista;
	}	

	
	public List<SelectItem> carregarComboUsuariosDoSetorEgab(Long codigoSetor) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		List<Usuario> listaUsuarios = Collections.emptyList();
	
		lista.add(new SelectItem("", ""));
		
		if(codigoSetor != null){
			try {
				listaUsuarios = getUsuarioService().pesquisarUsuariosEgabPlantao(codigoSetor, null);
			} catch (ServiceException exception) {
				reportarErro("Erro ao pesquisar usuários.", exception.getLocalizedMessage());
			}
	
			for (Usuario usuario : listaUsuarios) {
				lista.add(new SelectItem(usuario.getId(), usuario.getNome()));
			}
			lista.add(new SelectItem("PLANTONISTA", "* PLANTONISTA"));
		}

		return lista;
	}
	

	/**
	 * Método utilitário que deve ser utilizado quando for desejado uma lista de SelectItems vazia, uma vez que a utilização de "new List<SelectItem>()"
	 * diretamente, apenas, pode gerar uma exceção durante a renderização da página (java.util.NoSuchElementException).
	 * 
	 * @param incluirItemBranco
	 *            define se um item em branco deve ser adicionado à lista
	 */
	public List<SelectItem> montarListaSelectItemsVazia(boolean incluirItemBranco) {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		if (incluirItemBranco) {
			lista.add(new SelectItem(null, ""));
		}
		return lista;
	}

	public List<SelectItem> montarListaSelectItemsVazia() {
		return montarListaSelectItemsVazia(true);
	}

	public List<SelectItem> carregaComboTipoGrupoControle() {
		List<TipoGrupoControle> listaTipoGrupoControleTemp = null;
		List<SelectItem> listaTGC = new LinkedList<SelectItem>();
		try {
			listaTipoGrupoControleTemp = getTipoGrupoControleService().pesquisarTipoGrupoControle(null);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		if (listaTipoGrupoControleTemp != null) {

			for (TipoGrupoControle tgc : listaTipoGrupoControleTemp) {
				listaTGC.add(new SelectItem(tgc, tgc.getDscTipoGrupoControle()));
			}
		}
		return listaTGC;
	}

	/**
	 * Carrega uma lista de fases de documento que podem ser exibidas na tela de expedição de documentos.
	 */
	public List<SelectItem> carregarComboFaseSituacaoDocumentoExpedicao() {
		List<SelectItem> lista = new LinkedList<SelectItem>();

		lista.add(new SelectItem(TipoFaseComunicacao.ASSINADO.getCodigoFase(), TipoFaseComunicacao.ASSINADO.getDescricao()));

		return lista;
	}

	/**
	 * Carrega uma lista contendo todas as fases de documento existentes.
	 */
	public List<SelectItem> carregarComboFaseSituacaoDocumento() {
		List<SelectItem> lista = new LinkedList<SelectItem>();

		lista.add(new SelectItem(null, ""));

		TipoFaseComunicacao[] tiposFases = TipoFaseComunicacao.values();
		for (TipoFaseComunicacao tipoFaseComunicacao : tiposFases) {
			//if (!tipoFaseComunicacao.getCodigoFase().equals(TipoFaseComunicacao.RESTRITOS.getCodigoFase()))
			lista.add(new SelectItem(tipoFaseComunicacao.getCodigoFase(), tipoFaseComunicacao.getDescricao()));
		}

		return lista;
	}

	/**
	 * Carrega uma lista de fases de documento que podem ser exibidas na tela de assinatura de documentos.
	 */
	public List<SelectItem> carregarComboFaseSituacaoDocumentoAssinatura() {
		List<SelectItem> lista = new LinkedList<SelectItem>();

		lista.add(new SelectItem(TipoFaseComunicacao.AGUARDANDO_ASSINATURA.getCodigoFase(), TipoFaseComunicacao.AGUARDANDO_ASSINATURA.getDescricao()));
		lista.add(new SelectItem(TipoFaseComunicacao.ASSINADO.getCodigoFase(), TipoFaseComunicacao.ASSINADO.getDescricao()));
		lista.add(new SelectItem(TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO.getCodigoFase(),
				TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO.getDescricao()));

		return lista;
	}

	/**
	 * Carrega uma lista de itens de tipos de modelos.
	 * 
	 * Caso o codigo do tipo de permissão passado seja nulo, a pesquisa será feita pelo setor do usuário.
	 */
	public List<SelectItem> carregarComboTipoModelos() {
		List<TipoComunicacao> listaTipoComunicacaoTemp = null;
		List<SelectItem> lista = new LinkedList<SelectItem>();

		lista.add(new SelectItem(null, ""));
		try {
			Setor setor = null;
			// usuários institucionais e Master tem acesso a todos
			if (!isUsuarioInstitucional() && !isUsuarioMaster()) {
				setor = getSetorUsuarioAutenticado();
			}
			// listaTipoComunicacaoTemp = tipoComunicacaoService.pesquisarListaTiposModelos(null, setor);
			listaTipoComunicacaoTemp = getModeloComunicacaoService().pesquisarTipoComunicacaoPeloSetorPermissao(setor);
		} catch (ServiceException e) {
			reportarErro("Erro ao montar combo de tipos de modelos.");
		}
		if (listaTipoComunicacaoTemp != null && listaTipoComunicacaoTemp.size() > 0) {
			for (TipoComunicacao tc : listaTipoComunicacaoTemp) {
				lista.add(new SelectItem(tc.getId(), tc.getDescricao()));
			}
		}

		return lista;
	}

	public List<SelectItem> carregarComboTipoModelosTodos() {
		List<TipoComunicacao> listaTipoComunicacaoTemp = null;
		List<SelectItem> lista = new LinkedList<SelectItem>();

		lista.add(new SelectItem(null, ""));
		try {
			Setor setor = null;
			// usuários institucionais e Master tem acesso a todos
			if (!isUsuarioInstitucional() && !isUsuarioMaster()) {
				setor = getSetorUsuarioAutenticado();
			}
			//listaTipoComunicacaoTemp = getModeloComunicacaoService().pesquisarListaTiposModelos(null, setor);
			listaTipoComunicacaoTemp = getModeloComunicacaoService().pesquisarTipoComunicacao();
		} catch (ServiceException e) {
			reportarErro("Erro ao montar combo de tipos de modelos.");
		}
		if (listaTipoComunicacaoTemp != null && listaTipoComunicacaoTemp.size() > 0) {
			for (TipoComunicacao tc : listaTipoComunicacaoTemp) {
				lista.add(new SelectItem(tc.getId(), tc.getDescricao()));
			}
		}

		return lista;
	}

	/**
	 * Monta a combo de tipo modelos de acordo com os modelos pesquisados anteriormente
	 * 
	 * @param listaModeloComunicacaoPelaPermissao
	 * @return
	 */
	public List<SelectItem> carregarComboTipoModelosPelaListaDeModelos(List<ModeloComunicacao> listaModeloComunicacaoPelaPermissao) {
		List<SelectItem> lista = new LinkedList<SelectItem>();
		lista.add(new SelectItem(null, ""));
		List<TipoComunicacao> listaCompTipoComunicacao = new ArrayList<TipoComunicacao>();
		Boolean compTipoModelo = true;
		for (ModeloComunicacao mcon : listaModeloComunicacaoPelaPermissao) {
			for (TipoComunicacao comp : listaCompTipoComunicacao) {
				if (!mcon.getTipoComunicacao().getId().equals(comp.getId())) {
					compTipoModelo = true;
				} else {
					compTipoModelo = false;
					break;
				}
			}
			if (compTipoModelo) {
				listaCompTipoComunicacao.add(mcon.getTipoComunicacao());
			}
		}

		for (TipoComunicacao tc : listaCompTipoComunicacao) {
			lista.add(new SelectItem(tc.getId(), tc.getDescricao()));
		}
		return lista;
	}

	/**
	 * Método utilitário para reportar uma violação de regra de negócio para o usuário.
	 * 
	 * @param exception
	 */
	protected void reportarAviso(RegraDeNegocioException exception) {
		reportarAviso(exception.getLocalizedMessage());

		if (LOG.isDebugEnabled()) {
			LOG.debug("Reportando aviso.", exception);
		}
	}

	// ------------------ MÉTODOS DE REGRAS DE NEGÓCIO -------------------

	/**
	 * Gera a fase para de "PDF Cancelado" para a comunicação e salva a observação do cancelamento.
	 * 
	 * @throws ServiceException
	 */
	public void geraFasePdfCancelado(Comunicacao comunicacao, String anotacaoCancelamento) throws ServiceException {
		FaseComunicacaoService faseComunicacaoService = getFaseComunicacaoService();
		faseComunicacaoService.incluirFase(TipoFaseComunicacao.CORRECAO, comunicacao, anotacaoCancelamento, null);
	}

	/**
	 * Método que loga um erro no log e também o notifica para o usuário, na tela. Os beans que herdam deste podem utilizar esse método, passando também o Log
	 * utilizado por eles.
	 * 
	 * @param mensagemErro
	 *            mensagem que será logada
	 * @param erro
	 *            a exceção que ocorreu (opcional)
	 * @param log
	 *            o log utilizado (obrigatório)
	 */
	protected void reportarErro(String mensagemErro, Throwable erro, Log log) {
		Validate.notNull(log, "Log nulo");

		if (log.isErrorEnabled()) {
			log.error(mensagemErro, erro);
		}

		String mensagemErroDetalhe = erro != null ? erro.getLocalizedMessage() : "";
		if (mensagemErroDetalhe.contains("DaoException")) {
			// solução temporária enquanto forem lançadas ServiceException sem uma mensagem
			// específica
			mensagemErroDetalhe = ExceptionUtils.getRootCauseMessage(erro);
		}

		reportarErro(mensagemErro, "Detalhes do erro: " + mensagemErroDetalhe);
	}

	  /**
     * Método responsável em abrir o PDF das peças eletrônicas vinculadas ao
     * documento.
     */
    public void report() {
        nomeDocumentoDownload = valor.getPecaProcessoEletronico().getTipoPecaProcesso().getDescricao();
        conteudoDocumentoDownload = valor.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronico().getArquivo();
       
        	  downloadArquivoTipo(valor.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronicoView().getTipoArquivo().getExtensoes().replace(".",""));

        	  try {
        		  getObjetoIncidenteService().registrarLogSistema(valor.getComunicacao().getObjetoIncidenteUnico(), "CONSULTA_PECA", "Fazer Download Peça: "+ valor.getPecaProcessoEletronico().getTipoPecaProcesso().getDescricao(), valor.getPecaProcessoEletronico().getId(), "JUDICIARIO.PECA_PROCESSUAL");
        	  }catch (Exception e) {
        		  e.printStackTrace();
				// TODO: handle exception
			}
    }

    
    public void obterPecaArquivoPDF(){
        DocumentoEletronicoService documentoEletronicoService = getDocumentoEletronicoService();
    	try{
    		conteudoDocumentoDownload = documentoEletronicoService.recuperarArquivo(getSeqDocumentoPeca());
    	}catch(ServiceException se){
    		reportarErro("Erro ao buscar o documento da peça.", se, LOG);
    	}
    	downloadArquivoPdf();	
    }
    
    /**
     * Método responsável em abrir um arquivo PDF.
     */
    public void downloadArquivoPdf() {
        downloadArquivo("application/x-pdf", "pdf");
    }

    public void downloadArquivoTipo(String Tipo) {
        downloadArquivo("application/"+Tipo, Tipo);
    }
    
    /**
     * Método responsável em abrir um arquivo.
     */
    private void downloadArquivo(String contentTypeDocumentoDownload, String extensaoArquivo) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        String nome = nomeDocumentoDownload == null || nomeDocumentoDownload.isEmpty() ? "arquivo" : nomeDocumentoDownload;
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s." + extensaoArquivo + "\"", nome));
        response.setContentType(contentTypeDocumentoDownload);
        ByteArrayInputStream input = new ByteArrayInputStream(conteudoDocumentoDownload);

        try {
            IOUtils.copy(input, response.getOutputStream());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            IOUtils.closeQuietly(input);
        }

        facesContext.responseComplete();
    }

	/**
	 * Monta a URL do PDF vinculado.
	 */
	public String montaUrlDownload(ArquivoProcessoEletronico arquivo) {
		HttpServletRequest request = (HttpServletRequest) getRequest();
		String url = request.getContextPath() + "/documento?" + DocumentoDownloadServlet.ARQUIVO_DOCUMENTO_PARAM + "="
				+ arquivo.getDocumentoEletronico().getId();
		return url;
	}

	public String montaUrlDownloadComunicacaoPecaProcessoEletronico() {
		// TODO É preciso definir qual o arquivoprocessoeletronico que deve ser
		// utilizado.
		return montaUrlDownload(valor.getPecaProcessoEletronico().getDocumentos().get(0));
	}

	public List<Processo> pesquisaSuggestionBox(Object suggest) {
		try {
			return getProcessoService().pesquisarProcesso(suggest.toString());
		} catch (Exception e) {
			reportarErro("Erro na pesquisa processual. ", e.getMessage());
			return new ArrayList<Processo>();
		}
	}

	public List<Peticao> pesquisaSuggestionBoxPeticao(Long numero, Short ano) {
		try {
			return getPeticaoService().recuperarPeticoes(numero, ano);
		} catch (Exception e) {
			reportarErro("Erro na pesquisa da petição. ", e.getMessage());
			return new ArrayList<Peticao>();
		}
	}
	
	public String retirarCaracteresNaoNumeros(String valor){
		StringBuffer result = new StringBuffer();
		for(int i = 0 ; i < valor.length() ; i++){
			if(Character.isDigit(valor.charAt(i))){
				result.append(valor.charAt(i));
			}
		}
		return result.toString();
	}

	// --- MÉTODOS DELEGADOS ÀS SERVICES, UTILIZADOS PELOS OUTROS BEANS ----

	public List<IncidenteJulgamento> recuperarObjetosIncidentes(String sigla, Long numero) {

		List<IncidenteJulgamento> lista = new ArrayList<IncidenteJulgamento>();
		IncidenteJulgamentoService incidenteJulgamentoService = getIncidenteJulgamentoService();
		try {
			lista = incidenteJulgamentoService.recuperarIdObjetoIncidentes(sigla, numero);
		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar os Objetos Incidentes");
		}

		return lista;
	}

	public String consultarProcessoEJudConsulta() {
		BeanConsultaExterna beanConsultaExterna = BeanConsultaExterna.getInstanciaJSF();
		ConsultaExternaVO consultaExternaVO = criarConsultaExternaVO(getSeqObjetoIncidente());
		beanConsultaExterna.setConsultaExternaVO(consultaExternaVO);
		String urlConsultaExterna = beanConsultaExterna.consultarObjetoIncidenteEJudConsulta();
		setRequestValue(Constantes.CHAVE_URL_CONSULTA_EXTERNA, urlConsultaExterna);

		if (LOG.isDebugEnabled()) {
			LOG.debug(MessageFormat.format("Acessando sistema externo: {0}", urlConsultaExterna));
		}

		return "consultaExterna";
	}
	
	public String consultarProcessoDigital() throws ServiceException {
		BeanConsultaExterna beanConsultaExterna = BeanConsultaExterna.getInstanciaJSF();
		ConsultaExternaVO consultaExternaVO = criarConsultaExternaVO(getSeqObjetoIncidente());
		beanConsultaExterna.setConsultaExternaVO(consultaExternaVO);
		Processo proc = getProcessoService().recuperarPorId(consultaExternaVO.getSeqObjetoIncidente());
		String urlConsultaExterna = beanConsultaExterna.consultarObjetoIncidenteDigital(proc.getSiglaClasseProcessual() + proc.getNumeroProcessual().toString());
		setRequestValue(Constantes.CHAVE_URL_CONSULTA_EXTERNA, urlConsultaExterna);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug(MessageFormat.format("Acessando sistema externo: {0}", urlConsultaExterna));
		}

		return "consultaExterna";
	}
	
	public String consultarProcessoDigitalPecas() throws ServiceException {
		BeanConsultaExterna beanConsultaExterna = BeanConsultaExterna.getInstanciaJSF();
		ConsultaExternaVO consultaExternaVO = criarConsultaExternaVO(getSeqObjetoIncidente());
		beanConsultaExterna.setConsultaExternaVO(consultaExternaVO);
		Processo proc = getProcessoService().recuperarPorId(consultaExternaVO.getSeqObjetoIncidente());
		String urlConsultaExterna = beanConsultaExterna.consultarObjetoIncidenteDigitalPecas(proc.getSiglaClasseProcessual() + proc.getNumeroProcessual().toString());
		setRequestValue(Constantes.CHAVE_URL_CONSULTA_EXTERNA, urlConsultaExterna);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug(MessageFormat.format("Acessando sistema externo: {0}", urlConsultaExterna));
		}

		return "consultaExterna";
	}
	
	public String consultarObjetoIncidenteSupremo() {
		BeanConsultaExterna beanConsultaExterna = BeanConsultaExterna.getInstanciaJSF();
		ConsultaExternaVO consultaExternaVO = criarConsultaExternaVO(getSeqObjetoIncidente());
		beanConsultaExterna.setConsultaExternaVO(consultaExternaVO);
		String urlConsultaExterna = beanConsultaExterna.consultarObjetoIncidenteSupremo();
		setRequestValue(Constantes.CHAVE_URL_CONSULTA_EXTERNA, urlConsultaExterna);

		if (LOG.isDebugEnabled()) {
			LOG.debug(MessageFormat.format("Acessando sistema externo: {0}", urlConsultaExterna));
		}

		return "consultaExterna";
	}      

	public String consultarProcessoAndamento() {
		BeanConsultaExterna beanConsultaExterna = BeanConsultaExterna.getInstanciaJSF();
		ConsultaExternaVO consultaExternaVO = criarConsultaExternaVO(getSeqObjetoIncidente());
		beanConsultaExterna.setConsultaExternaVO(consultaExternaVO);
		String urlConsultaExterna = beanConsultaExterna.consultarProcessoAndamento();
		setRequestValue(Constantes.CHAVE_URL_CONSULTA_EXTERNA, urlConsultaExterna);

		if (LOG.isDebugEnabled()) {
			LOG.debug(MessageFormat.format("Acessando sistema externo: {0}", urlConsultaExterna));
		}

		return "consultaExterna";
	}

	private ConsultaExternaVO criarConsultaExternaVO(Object object) {
		ConsultaExternaVO consultaExternaVO = null;

		if (object instanceof ObjetoIncidente<?>) {
			ObjetoIncidente<?> objetoIncidente = (ObjetoIncidente<?>) object;
			consultaExternaVO = ConsultaExternaVO.criarAPartirDe(objetoIncidente.getId());
		} else if (object instanceof Long) {
			Long seqObjetoIncidente = (Long) object;
			consultaExternaVO = ConsultaExternaVO.criarAPartirDe(seqObjetoIncidente);
		}

		return consultaExternaVO;
	}

	public List<ObjetoIncidente<?>> recuperarIncidentes(Long id) {
		List<ObjetoIncidente<?>> incidentes = Collections.emptyList();
		ObjetoIncidenteService objetoIncidenteService = getObjetoIncidenteService();

		try {
			incidentes = objetoIncidenteService.pesquisar(id, TIPOS_OBJETOS_INCIDENTES_PERMITIDOS);
		} catch (ServiceException exception) {
			reportarErro(MessageFormat.format("Erro ao recuperar incidentes.", id), exception, LOG);
		}

		return incidentes;
	}

	// --------------------- GETTERS & SETTERS -----------------------

	public PecaProcessoEletronicoComunicacao getValor() {
		return valor;
	}

	public void setValor(PecaProcessoEletronicoComunicacao valor) {
		this.valor = valor;
	}

	public String getAppVersion() {
		// TODO Solução temporária
		if (appVersion == null) {
			appVersion = (String) getService("appVersion");
		}

		return appVersion;
	}

	public void setAppVersion(String versaoAplicacao) {
		this.appVersion = versaoAplicacao;
	}

	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}

	public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}

	public IdentificaoProcessualComunicacaoTableRowComparator getIdProcessualComunicacaoComparator() {
		if (idProcessualComunicacaoComparator == null) {
			idProcessualComunicacaoComparator = new IdentificaoProcessualComunicacaoTableRowComparator();
		}

		return idProcessualComunicacaoComparator;
	}

	protected String converterClasse(String classe, List<String> classes) throws ServiceException {
		try {
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
		} catch (ServiceException e) {
			throw new ServiceException("Erro ao converter classe processual: " + classe);
		}

		return null;
	}
	
	protected String getRequestParamFrame(String param) {
				
		Map<String, String> mapa = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		boolean isFrame = Boolean.parseBoolean(mapa.get("frame"));
		return isFrame ? mapa.get(param) : null;
	}

	protected String getNomeUsuarioAutenticado() throws ServiceException {
		return getNomeUsuario(getUser().getUsername());
	}

	protected String getNomeUsuario(String login) throws ServiceException {
		UsuarioService usuarioService = getUsuarioService();
		Usuario usuario = usuarioService.recuperarPorId(login);
		String nomeUsuario = usuario.getNome();
		return nomeUsuario;
	}

	public Long getSeqDocumentoPeca() {
		return seqDocumentoPeca;
	}

	public void setSeqDocumentoPeca(Long seqDocumentoPeca) {
		this.seqDocumentoPeca = seqDocumentoPeca;
	}

	public String getNomeDocumentoDownload() {
		return nomeDocumentoDownload;
	}

	public void setNomeDocumentoDownload(String nomeDocumentoDownload) {
		this.nomeDocumentoDownload = nomeDocumentoDownload;
	}

	public byte[] getConteudoDocumentoDownload() {
		return conteudoDocumentoDownload;
	}

	public void setConteudoDocumentoDownload(byte[] conteudoDocumentoDownload) {
		this.conteudoDocumentoDownload = conteudoDocumentoDownload;
	}
	
	protected String recuperarTextoDocumento(byte[] documento) throws IOException{
		if (documento == null)
			return "";
		PDDocument doc = PDDocument.load(documento);
		PDFTextStripper stripper = new PDFTextStripper();				
		
		String texto = stripper.getText(doc);
		doc.close();
		return texto;
	}
	
	protected String getRecuperarTextoDocumento(HtmlDataTable tabelaDocumentos){
		
		ComunicacaoDocumento comunicacao =(ComunicacaoDocumento) ((CheckableDataTableRowWrapper)tabelaDocumentos.getRowData()).getWrappedObject();
		String texto = null;		
		try{			
			if (comunicacao == null)  
				return "";			
			DocumentoComunicacao dc = getDocumentoComunicacaoService().recuperarPorId(comunicacao.getDocumentoComunicacao().getId());
			if (dc == null || dc.getDocumentoEletronico() == null)
				return "";
			byte[] documento = dc.getDocumentoEletronico().getArquivo();
			texto = recuperarTextoDocumento(documento);
		}catch(IOException ee){
			texto = "Erro ao recuperar conteúdo do documento.";
		}catch(ServiceException ee){
			texto = "Erro ao recuperar conteúdo do documento.";
		}
		return texto;
		
	}
	
	public Boolean getSalaOficiais() {
		return (getSetorUsuarioAutenticado().getId().equals(idSetorSalaOficiais));	
	}
	
	public String getIdUsuarioAtribuicao() {
		return idUsuarioAtribuicao;
	}

	public void setIdUsuarioAtribuicao(String idUsuarioAtribuicao) {
		this.idUsuarioAtribuicao = idUsuarioAtribuicao;
	}
	
}
