package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.manterdocumentos;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.jopendocument.dom.ODSingleXMLDocument;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.service.TagsLivresServiceLocal;
import br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirDocumento;
import br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirTexto;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.assinatura.visao.util.OrdenacaoUtils;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoComunicacaoService;
import br.gov.stf.estf.documento.model.service.TagsLivresUsuarioService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Categoria;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.estf.processostf.model.service.CategoriaService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ParteService;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.entity.Flag;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;
import br.gov.stf.stfoffice.editor.jnlp.AbrirDocumento.ArgumentosAbrirDocumento;
import br.gov.stf.stfoffice.editor.web.requisicao.jnlp.RequisicaoJnlpDocumento;
import br.gov.stf.stfoffice.web.requisicao.jnlp.RequisicaoJnlp;
import br.jus.stf.estf.montadortexto.ParteProcessoSecretaria;
import br.jus.stf.estf.montadortexto.SpecDadosDocumentoSecretaria;
import br.jus.stf.estf.montadortexto.tools.TextoUtil;

public class BeanManterDocumentos extends AssinadorBaseBean {

	private static final long serialVersionUID = -8059606167034592897L;
	private static final Log LOG = LogFactory.getLog(BeanManterDocumentos.class);


	// ######################### VARIAVES DE SESSAO #########################

	private static final String KEY_LISTA_DOCUMENTOS = BeanManterDocumentos.class.getCanonicalName() + ".listaDocumentos";
	private static final String KEY_LISTA_PECA_PROCESSO_ELET = BeanManterDocumentos.class.getCanonicalName() + ".listaPecaProcessoEletronico";
	private static final String KEY_LISTA_PECA_PROCESSO_ELET_VINCULADAS = BeanManterDocumentos.class.getCanonicalName()
			+ ".listaPecaProcessoEletronicoVinculadas";
	private static final String KEY_DOCUMENTO_EDITAR = BeanManterDocumentos.class.getCanonicalName() + ".documentoEditar";
	private static final String KEY_LISTA_RECURSOS = BeanManterDocumentos.class.getCanonicalName() + ".listaRecursosProcesso";
	private static final String KEY_LISTA_ARQ_PROCESSO_ELET = BeanManterDocumentos.class.getCanonicalName() + ".listaVinculadaPecaAoDocumento";
	private static final Object OBJETOINCIDENTE = new Object();
	private static final Object OBJETO_INCIDENTE_NOVO = new Object();
	private static final Object OBJETO_INCIDENTE_LOTE = new Object();
	private static final Object OBJETOINCIDENTEMODAL = new Object();
	private static final Object RENDERED_NOVOTEXTO = new Object();
	private static final Object RENDERED_BOTAO_NOVO_TEXTO = new Object();
	private static final Object RENDERED_TELA_TAG_LIVRES = new Object();
	private static final Object RENDERED_COMBO_AUTOR = new Object();
	private static final Object RENDERED_COMBO_REU = new Object();
	private static final Object RENDERED_COMBO_INTIMANDO = new Object();
	private static final Object RENDERED_COMBO_CITANDA = new Object();
	private static final Object RENDERED_COMBO_ADVOGADO = new Object();
	private static final Object RENDERED_COMBO_PARTES = new Object();
	private static final Object RENDERED_BOTAO_VINCULAR_PECA = new Object();
	private static final Object RENDERED_ADICIONA_INTIMANDO = new Object();
	private static final Object RENDERED_BOTAO_ADICIONA_INTIMANDO = new Object();
	private static final Object RENDERED_ADICIONA_REU = new Object();
	private static final Object RENDERED_ADICIONA_CITANDO = new Object();
	private static final Object RENDERED_BOTAO_ADICIONA_REU = new Object();
	private static final Object RENDERED_BOTAO_ADICIONA_CITANDO = new Object();
	private static final Object RENDERED_TELA_PARTES_SELECIONADAS = new Object();
	private static final Object RENDERED_LISTA_PECAS_VINCULADAS = new Object();
	private static final Object RENDERED_TELA_PROCESSO_LOTE = new Object();
	private static final Object RENDERED_DESABILITA_MENU_MODELO = new Object();
	private static final Object RENDERED_TRANS_OBJETO_INCIDENTE = new Object();
	private static final Object RENDERED_OBS_ANDAMENTO = new Object();
	private static final Object ITENS_CATEGORIA_PARTES_AUTOR = new Object();
	private static final Object ITENS_CATEGORIA_PARTES_REU = new Object();
	private static final Object ITENS_CATEGORIA_PARTES_INTIMANDO = new Object();
	private static final Object ITENS_CATEGORIA_PARTES_CITANDA = new Object();
	private static final Object ITENS_CATEGORIA_PARTES_ADVOGADO = new Object();
	private static final Object ITENS_CATEGORIA_PARTES = new Object();
	private static final Object RENDERED_PECA_VINCULADA = new Object();
	private static final Object RENDERED_PECA_VINCULADA_NOVAMENTE = new Object();
	private static final Object ITENS_TIPOS_PERMISSOES = new Object();
	private static final Object ITENSTIPOMODELOS = new Object();
	private static final Object ITENSMODELOS = new Object();
	private static final Object CODIGO_TIPO_PERMISSAO = new Object();
	private static final Object CODIGOTIPOMODELO = new Object();
	private static final Object CODIGOMODELO = new Object();
	private static final Object ITENSMODELOSPESQ = new Object();
	private static final Object ITENSSETORESDESTINO = new Object();
	private static final Object MODELO_COMUNICACAO = new Object();
	private static final Object LISTA_TAGS_LIVRES = new Object();
	private static final Object LISTA_TAGS = new Object();
	private static final Object NOME_INTIMANDO_ADICIONADO = new Object();
	private static final Object NOME_REU_ADICIONADO = new Object();
	private static final Object NOME_CITANDO_ADICIONADO = new Object();
	private static final Object COMDOC_TEMP_ROWDATA = new Object();
	private static final Object LISTA_PROCESSO_LOTE = new Object();
	private static final Object PROCESSO_PESQUISADO_PRINCIPAL = new Object();
	private static final Object PROCESSO_PESQUISADO_LOTE = new Object();
	private static final Object SIG_CLASSE_PROCES_PESQ_PRINCIPAL = new Object();
	private static final Object NUM_PROCESSO_PESQ_PRINCIPAL = new Object();
	private static final Object SIG_CLASSE_PROCES_PESQ_LOTE = new Object();
	private static final Object NUM_PROCESSO_PESQ_LOTE = new Object();
	private static final Object RENDERED_COMPONENTES_ALTERACAO = new Object();
	private static final Object LISTA_OBJETO_INCIDENTE_LOTE = new Object();
	private static final Object LISTA_PECAS = new Object();

	public static final Object OBJETO_INCIDENTE_TRANS_BEAN = new Object();
	
	public static final Object PESQUISAR_AO_CARREGAR_PAGINA = new Object();

	private static List<String> classes;

	// ######################### VARIÁVEIS #########################

	private Long codigoTipoPermissao;
	private Long codigoTipoModelo;
	private Long codigoModelo;
	private Long codigoModeloPesq;
	private Long objetoIncidente;
	private Long objetoIncidenteNovo;
	private Long objetoIncidenteLote;
	private Long objetoIncidenteModal;
	private Long numeroProcessoPesqPrincipal;
	private Long numeroProcessoPesqLote;
	private Long idProcessoLoteObjetoIncidente;
	private Long codigoRecursoTexto;
	private Long codigoMinistroTexto;
	private Long idSetorDestino;
	private Long codigoMinistroEditar;
	private String siglaNumeroProcesso;
	private String siglaNumeroProcessoLote;
	private String nomeDoDocumento;
	private String nomeCategoriaAutor;
	private String nomeCategoriaReu;
	private String nomeCategoriaIntimando;
	private String nomeCategoriaCitanda;
	private String nomeIntimandoAdicionado;
	private String nomeReuAdicionado;
	private String nomeCitandoAdicionado;
	private String nomeCategoriaAdvogado;
	private String nomeCategoriaParte;
	private String siglaClassePesqPrincipal;
	private String siglaClassePesqLote;
	private String obsDocumento;
	private String tipoJulgamentoTexto;
	private String observacaoTexto;
	private String observacaoAndamento;
	private String anotacaoCancelamento;
	private String observacaoComunicacaoAlterada;
	private ObjetoIncidente<?> objetoIncidenteProcurado;

	private Map<String, String> conjuntoTagsLivres;
	private SortedMap<String, String> conjuntoTags;
	private Map<String, String> conjuntoPartesSelecionadas;

	@KeepStateInHttpSession
	private Boolean renderizaNovoTexto;
	private Boolean renderedBotaoNovoTexto;
	private Boolean renderedTelaDeTagsLivreDePreenchimento;
	private Boolean renderedComboAutores;
	private Boolean renderedComboReus;
	private Boolean renderedComboIntimandos;
	private Boolean renderedComboCitanda;
	private Boolean renderedComboAdvogados;
	private Boolean renderedComboParte;
	private Boolean checkpesquisaAutor;
	private Boolean renderedVincularPecaAoDocumento;
	private Boolean renderedVincularPecaNovamenteDocumento;
	private Boolean renderedBotaoVincularPeca;
	private Boolean renderedAdicionaIntimando;
	private Boolean renderedAdicionaReu;
	private Boolean renderedAdicionaCitando;
	private Boolean renderedTelaDePartesSelecionadas;
	private Boolean renderedBotaoAdicionarIntimando;
	private Boolean renderedBotaoAdicionarReu;
	private Boolean renderedBotaoAdicionarCitando;
	private Boolean renderedListaPecasVinculadas;
	private Boolean renderedComponentesAlteracao;
	private Boolean renderedTelaProcessoLote;
	private Boolean renderedDesabilitaMenuModelo;
	private Boolean renderedTransObjetoIncidente;
	private Boolean renderedAlteraObsAndamento;

	@KeepStateInHttpSession
	private Comunicacao documentoEditar;
	private ModeloComunicacao modeloComunicacao;
	private String nomeDocumentoEditar;
	private ComunicacaoDocumento comDocumentoTemporariaRowData;
	private Processo processo;
	private Processo processoLote;

	private List<SelectItem> itensTiposPermissoes;
	private List<SelectItem> itensTipoModelos;
	@KeepStateInHttpSession
	private List<SelectItem> listaRecursosProcesso;
	private List<SelectItem> itensModelos;
	private List<SelectItem> itensModelosPesq;
	private List<SelectItem> itensSetoresDestino;
	private List<SelectItem> itensCategoriaAutor;
	private List<SelectItem> itensCategoriaReu;
	private List<SelectItem> itensCategoriaIntimando;
	private List<SelectItem> itensCategoriaCitanda;
	private List<SelectItem> itensCategoriaAdvogado;
	private List<SelectItem> itensCategoriaParte;
	private List<SelectItem> itensProcessoEmLote;

	private List<ObjetoIncidente<?>> listaObjetoIncidenteLote;
	private List<ArquivoProcessoEletronico> listaVinculadaPecaAoDocumento;
	private List<ArquivoProcessoEletronico> listaPecasProcessoLote;
	private List<ModeloComunicacao> listaModeloComunicacaoPelaPermissao;

	// ------------------------ VARIAVEIS DA TABELA ------------------------

	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> listaDocumentos;
	private List<CheckableDataTableRowWrapper> listaPecaProcessoEletronico;
	private List<CheckableDataTableRowWrapper> listaPecaProcessoEletronicoVinculadas;

	private org.richfaces.component.html.HtmlDataTable tabelaDocumentos;
	private org.richfaces.component.html.HtmlDataTable tabelaPecaProcessoEletronico;
	private HtmlDataTable tabelaPecaProcessoEletronicoVinculadas;

	// ------------------------ SESSAO ------------------------

	@PostConstruct
	@SuppressWarnings({ "unchecked", "unused" })
	private void restaurarSessao() {
		restoreStateOfHttpSession();

		if (listaRecursosProcesso == null) {
			listaRecursosProcesso = new ArrayList<SelectItem>();
			listaRecursosProcesso.add(new SelectItem(0L, ""));
			setListaRecursosProcesso(listaRecursosProcesso);
		}

		if (documentoEditar == null) {
			documentoEditar = new Comunicacao();
			setDocumentoEditar(documentoEditar);
		}

		if (getAtributo(RENDERED_NOVOTEXTO) == null) {
			setAtributo(RENDERED_NOVOTEXTO, true);
		}
		setRenderizaNovoTexto((Boolean) getAtributo(RENDERED_NOVOTEXTO));

		if (getAtributo(NOME_INTIMANDO_ADICIONADO) == null) {
			setAtributo(NOME_INTIMANDO_ADICIONADO, new String());
		}
		setNomeIntimandoAdicionado((String) getAtributo(NOME_INTIMANDO_ADICIONADO));

		if (getAtributo(NOME_REU_ADICIONADO) == null) {
			setAtributo(NOME_REU_ADICIONADO, new String());
		}
		setNomeReuAdicionado((String) getAtributo(NOME_REU_ADICIONADO));

		if (getAtributo(NOME_CITANDO_ADICIONADO) == null) {
			setAtributo(NOME_CITANDO_ADICIONADO, new String());
		}
		setNomeCitandoAdicionado((String) getAtributo(NOME_CITANDO_ADICIONADO));

		if (getAtributo(RENDERED_BOTAO_NOVO_TEXTO) == null) {
			setAtributo(RENDERED_BOTAO_NOVO_TEXTO, false);
		}
		setRenderedBotaoNovoTexto((Boolean) getAtributo(RENDERED_BOTAO_NOVO_TEXTO));

		if (getAtributo(RENDERED_TELA_TAG_LIVRES) == null) {
			setAtributo(RENDERED_TELA_TAG_LIVRES, false);
		}
		setRenderedTelaDeTagsLivreDePreenchimento((Boolean) getAtributo(RENDERED_TELA_TAG_LIVRES));

		if (getAtributo(RENDERED_COMBO_AUTOR) == null) {
			setAtributo(RENDERED_COMBO_AUTOR, false);
		}
		setRenderedComboAutores((Boolean) getAtributo(RENDERED_COMBO_AUTOR));

		if (getAtributo(RENDERED_COMBO_REU) == null) {
			setAtributo(RENDERED_COMBO_REU, false);
		}
		setRenderedComboReus((Boolean) getAtributo(RENDERED_COMBO_REU));

		if (getAtributo(RENDERED_COMBO_INTIMANDO) == null) {
			setAtributo(RENDERED_COMBO_INTIMANDO, false);
		}
		setRenderedComboIntimandos((Boolean) getAtributo(RENDERED_COMBO_INTIMANDO));

		if (getAtributo(RENDERED_COMBO_CITANDA) == null) {
			setAtributo(RENDERED_COMBO_CITANDA, false);
		}
		setRenderedComboCitanda((Boolean) getAtributo(RENDERED_COMBO_CITANDA));

		if (getAtributo(RENDERED_COMBO_ADVOGADO) == null) {
			setAtributo(RENDERED_COMBO_ADVOGADO, false);
		}
		setRenderedComboAdvogados((Boolean) getAtributo(RENDERED_COMBO_ADVOGADO));

		if (getAtributo(RENDERED_COMBO_PARTES) == null) {
			setAtributo(RENDERED_COMBO_PARTES, false);
		}
		setRenderedComboParte((Boolean) getAtributo(RENDERED_COMBO_PARTES));

		if (getAtributo(RENDERED_BOTAO_VINCULAR_PECA) == null) {
			setAtributo(RENDERED_BOTAO_VINCULAR_PECA, true);
		}
		setRenderedBotaoVincularPeca((Boolean) getAtributo(RENDERED_BOTAO_VINCULAR_PECA));

		if (getAtributo(RENDERED_PECA_VINCULADA) == null) {
			setAtributo(RENDERED_PECA_VINCULADA, false);

		}
		setRenderedVincularPecaAoDocumento((Boolean) getAtributo(RENDERED_PECA_VINCULADA));

		if (getAtributo(RENDERED_ADICIONA_INTIMANDO) == null) {
			setAtributo(RENDERED_ADICIONA_INTIMANDO, false);

		}
		setRenderedAdicionaIntimando((Boolean) getAtributo(RENDERED_ADICIONA_INTIMANDO));

		if (getAtributo(RENDERED_ADICIONA_REU) == null) {
			setAtributo(RENDERED_ADICIONA_REU, false);

		}
		setRenderedAdicionaReu((Boolean) getAtributo(RENDERED_ADICIONA_REU));

		if (getAtributo(RENDERED_ADICIONA_CITANDO) == null) {
			setAtributo(RENDERED_ADICIONA_CITANDO, false);

		}
		setRenderedAdicionaCitando((Boolean) getAtributo(RENDERED_ADICIONA_CITANDO));

		if (getAtributo(RENDERED_TELA_PARTES_SELECIONADAS) == null) {
			setAtributo(RENDERED_TELA_PARTES_SELECIONADAS, false);

		}
		setRenderedTelaDePartesSelecionadas((Boolean) getAtributo(RENDERED_TELA_PARTES_SELECIONADAS));

		if (getAtributo(RENDERED_PECA_VINCULADA_NOVAMENTE) == null) {
			setAtributo(RENDERED_PECA_VINCULADA_NOVAMENTE, false);
		}
		setRenderedVincularPecaNovamenteDocumento((Boolean) getAtributo(RENDERED_PECA_VINCULADA_NOVAMENTE));

		if (getAtributo(RENDERED_BOTAO_ADICIONA_INTIMANDO) == null) {
			setAtributo(RENDERED_BOTAO_ADICIONA_INTIMANDO, true);
		}
		setRenderedBotaoAdicionarIntimando((Boolean) getAtributo(RENDERED_BOTAO_ADICIONA_INTIMANDO));

		if (getAtributo(RENDERED_BOTAO_ADICIONA_REU) == null) {
			setAtributo(RENDERED_BOTAO_ADICIONA_REU, true);
		}
		setRenderedBotaoAdicionarReu((Boolean) getAtributo(RENDERED_BOTAO_ADICIONA_REU));

		if (getAtributo(RENDERED_BOTAO_ADICIONA_CITANDO) == null) {
			setAtributo(RENDERED_BOTAO_ADICIONA_CITANDO, true);
		}
		setRenderedBotaoAdicionarCitando((Boolean) getAtributo(RENDERED_BOTAO_ADICIONA_CITANDO));

		if (getAtributo(RENDERED_LISTA_PECAS_VINCULADAS) == null) {
			setAtributo(RENDERED_LISTA_PECAS_VINCULADAS, false);
		}
		setRenderedListaPecasVinculadas((Boolean) getAtributo(RENDERED_LISTA_PECAS_VINCULADAS));

		if (getAtributo(RENDERED_TELA_PROCESSO_LOTE) == null) {
			setAtributo(RENDERED_TELA_PROCESSO_LOTE, false);
		}
		setRenderedTelaProcessoLote((Boolean) getAtributo(RENDERED_TELA_PROCESSO_LOTE));

		if (getAtributo(RENDERED_COMPONENTES_ALTERACAO) == null) {
			setAtributo(RENDERED_COMPONENTES_ALTERACAO, false);
		}
		setRenderedComponentesAlteracao((Boolean) getAtributo(RENDERED_COMPONENTES_ALTERACAO));

		if (getAtributo(RENDERED_DESABILITA_MENU_MODELO) == null) {
			setAtributo(RENDERED_DESABILITA_MENU_MODELO, false);
		}
		setRenderedDesabilitaMenuModelo((Boolean) getAtributo(RENDERED_DESABILITA_MENU_MODELO));

		if (getAtributo(ITENS_TIPOS_PERMISSOES) == null) {
			setAtributo(ITENS_TIPOS_PERMISSOES, montarTiposPermissoes());
		}
		setItensTiposPermissoes((List<SelectItem>) getAtributo(ITENS_TIPOS_PERMISSOES));

		if (getAtributo(RENDERED_TRANS_OBJETO_INCIDENTE) == null) {
			setAtributo(RENDERED_TRANS_OBJETO_INCIDENTE, false);
		}
		setRenderedTransObjetoIncidente((Boolean) getAtributo(RENDERED_TRANS_OBJETO_INCIDENTE));
		
		if (getAtributo(RENDERED_OBS_ANDAMENTO) == null) {
			setAtributo(RENDERED_OBS_ANDAMENTO, false);
		}
		setRenderedAlteraObsAndamento((Boolean) getAtributo(RENDERED_OBS_ANDAMENTO));		

		if (getAtributo(ITENSMODELOS) == null) {
			setAtributo(ITENSMODELOS, montarModelosFiltrados());
		}
		setItensModelos((List<SelectItem>) getAtributo(ITENSMODELOS));
		
		if (getAtributo(ITENSTIPOMODELOS) == null) {
			setAtributo(ITENSTIPOMODELOS, carregarComboTipoModelosPelaListaDeModelos(listaModeloComunicacaoPelaPermissao));
		}
		setItensTipoModelos((List<SelectItem>) getAtributo(ITENSTIPOMODELOS));

		if (getAtributo(ITENS_CATEGORIA_PARTES_AUTOR) == null) {
			setAtributo(ITENS_CATEGORIA_PARTES_AUTOR, montarListaSelectItemsVazia());
		}
		setItensCategoriaAutor((List<SelectItem>) getAtributo(ITENS_CATEGORIA_PARTES_AUTOR));

		if (getAtributo(ITENS_CATEGORIA_PARTES_REU) == null) {
			setAtributo(ITENS_CATEGORIA_PARTES_REU, montarListaSelectItemsVazia());
		}
		setItensCategoriaReu((List<SelectItem>) getAtributo(ITENS_CATEGORIA_PARTES_REU));

		if (getAtributo(ITENS_CATEGORIA_PARTES_INTIMANDO) == null) {
			setAtributo(ITENS_CATEGORIA_PARTES_INTIMANDO, montarListaSelectItemsVazia());
		}
		setItensCategoriaIntimando((List<SelectItem>) getAtributo(ITENS_CATEGORIA_PARTES_INTIMANDO));

		if (getAtributo(ITENS_CATEGORIA_PARTES_CITANDA) == null) {
			setAtributo(ITENS_CATEGORIA_PARTES_CITANDA, montarListaSelectItemsVazia());
		}
		setItensCategoriaCitanda((List<SelectItem>) getAtributo(ITENS_CATEGORIA_PARTES_CITANDA));

		if (getAtributo(ITENS_CATEGORIA_PARTES_ADVOGADO) == null) {
			setAtributo(ITENS_CATEGORIA_PARTES_ADVOGADO, montarListaSelectItemsVazia());
		}
		setItensCategoriaAdvogado((List<SelectItem>) getAtributo(ITENS_CATEGORIA_PARTES_ADVOGADO));

		if (getAtributo(ITENS_CATEGORIA_PARTES) == null) {
			setAtributo(ITENS_CATEGORIA_PARTES, montarListaSelectItemsVazia());
		}
		setItensCategoriaParte((List<SelectItem>) getAtributo(ITENS_CATEGORIA_PARTES));

		if (getAtributo(KEY_LISTA_ARQ_PROCESSO_ELET) == null) {
			setAtributo(KEY_LISTA_ARQ_PROCESSO_ELET, new LinkedList<ArquivoProcessoEletronico>());
		}
		setListaVinculadaPecaAoDocumento((List<ArquivoProcessoEletronico>) getAtributo(KEY_LISTA_ARQ_PROCESSO_ELET));

		if (getAtributo(OBJETOINCIDENTE) == null) {
			setAtributo(OBJETOINCIDENTE, objetoIncidente);
		}
		setObjetoIncidente((Long) getAtributo(OBJETOINCIDENTE));

		if (getAtributo(OBJETO_INCIDENTE_NOVO) == null) {
			setAtributo(OBJETO_INCIDENTE_NOVO, objetoIncidenteNovo);
		}
		setObjetoIncidenteNovo((Long) getAtributo(OBJETO_INCIDENTE_NOVO));

		if (getAtributo(OBJETO_INCIDENTE_LOTE) == null) {
			setAtributo(OBJETO_INCIDENTE_LOTE, objetoIncidenteLote);
		}
		setObjetoIncidenteLote((Long) getAtributo(OBJETO_INCIDENTE_LOTE));

		if (getAtributo(OBJETOINCIDENTEMODAL) == null) {
			setAtributo(OBJETOINCIDENTEMODAL, objetoIncidenteModal);
		}
		setObjetoIncidente((Long) getAtributo(OBJETOINCIDENTEMODAL));

		if (getAtributo(CODIGO_TIPO_PERMISSAO) == null) {
			setAtributo(CODIGO_TIPO_PERMISSAO, codigoTipoPermissao);
		}
		setCodigoTipoPermissao((Long) getAtributo(CODIGO_TIPO_PERMISSAO));

		if (getAtributo(CODIGOTIPOMODELO) == null) {
			setAtributo(CODIGOTIPOMODELO, codigoTipoModelo);
		}
		setCodigoTipoModelo((Long) getAtributo(CODIGOTIPOMODELO));

		if (getAtributo(CODIGOMODELO) == null) {
			setAtributo(CODIGOMODELO, codigoModelo);
		}
		setCodigoModelo((Long) getAtributo(CODIGOMODELO));

		if (getAtributo(ITENSMODELOSPESQ) == null) {
			setAtributo(ITENSMODELOSPESQ, montarModelos());
		}
		setItensModelosPesq((List<SelectItem>) getAtributo(ITENSMODELOSPESQ));

		if (getAtributo(ITENSSETORESDESTINO) == null) {
			setAtributo(ITENSSETORESDESTINO, carregarComboSetoresDestino(true,false));
		}
		setItensSetoresDestino((List<SelectItem>) getAtributo(ITENSSETORESDESTINO));

		if (getAtributo(MODELO_COMUNICACAO) == null) {
			setAtributo(MODELO_COMUNICACAO, new ModeloComunicacao());
		}
		setModeloComunicacao((ModeloComunicacao) getAtributo(MODELO_COMUNICACAO));

		if (getAtributo(LISTA_TAGS_LIVRES) == null) {
			setAtributo(LISTA_TAGS_LIVRES, new HashMap<Long, String>());
		}
		setConjuntoTagsLivres((Map<String, String>) getAtributo(LISTA_TAGS_LIVRES));

		if (getAtributo(LISTA_TAGS) == null) {
			setAtributo(LISTA_TAGS, new TreeMap<String, String>());
		}
		setConjuntoTags((SortedMap<String, String>) getAtributo(LISTA_TAGS));

		if (getAtributo(COMDOC_TEMP_ROWDATA) == null) {
			setAtributo(COMDOC_TEMP_ROWDATA, new ComunicacaoDocumento());
		}
		setComDocumentoTemporariaRowData((ComunicacaoDocumento) getAtributo(COMDOC_TEMP_ROWDATA));

		if (getAtributo(LISTA_PROCESSO_LOTE) == null) {
			setAtributo(LISTA_PROCESSO_LOTE, montarListaSelectItemsVazia(false));
		}
		setItensProcessoEmLote((List<SelectItem>) getAtributo(LISTA_PROCESSO_LOTE));

		if (getAtributo(LISTA_OBJETO_INCIDENTE_LOTE) == null) {
			setAtributo(LISTA_OBJETO_INCIDENTE_LOTE, new ArrayList<ObjetoIncidente<?>>());
		}
		setListaObjetoIncidenteLote((List<ObjetoIncidente<?>>) getAtributo(LISTA_OBJETO_INCIDENTE_LOTE));

		if (getAtributo(LISTA_PECAS) == null) {
			setAtributo(LISTA_PECAS, new LinkedList<ArquivoProcessoEletronico>());
		}
		setListaPecasProcessoLote((List<ArquivoProcessoEletronico>) getAtributo(LISTA_PECAS));

		setProcesso((Processo) getAtributo(PROCESSO_PESQUISADO_PRINCIPAL));
		setNumeroProcessoPesqPrincipal((Long) getAtributo(NUM_PROCESSO_PESQ_PRINCIPAL));
		setSiglaClassePesqPrincipal((String) getAtributo(SIG_CLASSE_PROCES_PESQ_PRINCIPAL));
		setProcessoLote((Processo) getAtributo(PROCESSO_PESQUISADO_LOTE));
		setNumeroProcessoPesqLote((Long) getAtributo(NUM_PROCESSO_PESQ_LOTE));
		setSiglaClassePesqLote((String) getAtributo(SIG_CLASSE_PROCES_PESQ_LOTE));

		setListaDocumentos((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));

		setListaPecaProcessoEletronico((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_PECA_PROCESSO_ELET));

		setListaPecaProcessoEletronicoVinculadas((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_PECA_PROCESSO_ELET_VINCULADAS));

		if( getAtributo(OBJETO_INCIDENTE_TRANS_BEAN) != null ){
			ObjetoIncidente obIncidente = (ObjetoIncidente) getAtributo(OBJETO_INCIDENTE_TRANS_BEAN);
			if( obIncidente != null ){
				setObjetoIncidenteNovo(obIncidente.getPrincipal().getId());
				setSiglaNumeroProcesso(obIncidente.getIdentificacao());
				setAtributo(OBJETO_INCIDENTE_NOVO, objetoIncidenteNovo);
				setAtributo(RENDERED_TRANS_OBJETO_INCIDENTE, true);
			}else{
				setAtributo(RENDERED_TRANS_OBJETO_INCIDENTE, false);
			}
			setAtributo(OBJETO_INCIDENTE_TRANS_BEAN, null);
			setRenderedTransObjetoIncidente((Boolean)getAtributo(RENDERED_TRANS_OBJETO_INCIDENTE));
		}		
		
		if( getAtributo(PESQUISAR_AO_CARREGAR_PAGINA) != null && (Boolean)getAtributo(PESQUISAR_AO_CARREGAR_PAGINA) == true ){
			setAtributo(PESQUISAR_AO_CARREGAR_PAGINA, null);
			pesquisarDocumentosCorrecao();			
		}
		
		atualizaSessao();

		applyStateInHttpSession();

	}

	public void atualizaSessao() {
		setAtributo(OBJETOINCIDENTE, objetoIncidente);
		setAtributo(OBJETO_INCIDENTE_NOVO, objetoIncidenteNovo);
		setAtributo(OBJETO_INCIDENTE_LOTE, objetoIncidenteLote);
		setAtributo(NOME_INTIMANDO_ADICIONADO, nomeIntimandoAdicionado);
		setAtributo(NOME_REU_ADICIONADO, nomeReuAdicionado);
		setAtributo(NOME_CITANDO_ADICIONADO, nomeCitandoAdicionado);
		setAtributo(RENDERED_NOVOTEXTO, renderizaNovoTexto);
		setAtributo(ITENS_TIPOS_PERMISSOES, itensTiposPermissoes);
		setAtributo(ITENSTIPOMODELOS, itensTipoModelos);
		setAtributo(ITENSMODELOS, itensModelos);
		setAtributo(ITENS_CATEGORIA_PARTES_AUTOR, itensCategoriaAutor);
		setAtributo(ITENS_CATEGORIA_PARTES_REU, itensCategoriaReu);
		setAtributo(ITENS_CATEGORIA_PARTES_INTIMANDO, itensCategoriaIntimando);
		setAtributo(ITENS_CATEGORIA_PARTES_CITANDA, itensCategoriaCitanda);
		setAtributo(ITENS_CATEGORIA_PARTES_ADVOGADO, itensCategoriaAdvogado);
		setAtributo(ITENS_CATEGORIA_PARTES, itensCategoriaParte);
		setAtributo(OBJETOINCIDENTEMODAL, objetoIncidenteModal);
		setAtributo(CODIGO_TIPO_PERMISSAO, codigoTipoPermissao);
		setAtributo(CODIGOTIPOMODELO, codigoTipoModelo);
		setAtributo(CODIGOMODELO, codigoModelo);
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		setAtributo(KEY_LISTA_PECA_PROCESSO_ELET, listaPecaProcessoEletronico);
		setAtributo(ITENSSETORESDESTINO, itensSetoresDestino);
		setAtributo(MODELO_COMUNICACAO, modeloComunicacao);
		setAtributo(LISTA_TAGS_LIVRES, conjuntoTagsLivres);
		setAtributo(LISTA_TAGS, conjuntoTags);
		setAtributo(RENDERED_BOTAO_NOVO_TEXTO, renderedBotaoNovoTexto);
		setAtributo(RENDERED_TELA_TAG_LIVRES, renderedTelaDeTagsLivreDePreenchimento);
		setAtributo(RENDERED_COMBO_AUTOR, renderedComboAutores);
		setAtributo(RENDERED_COMBO_REU, renderedComboReus);
		setAtributo(RENDERED_BOTAO_VINCULAR_PECA, renderedBotaoVincularPeca);
		setAtributo(RENDERED_COMBO_INTIMANDO, renderedComboIntimandos);
		setAtributo(RENDERED_COMBO_CITANDA, renderedComboCitanda);
		setAtributo(RENDERED_COMBO_ADVOGADO, renderedComboAdvogados);
		setAtributo(RENDERED_COMBO_PARTES, renderedComboParte);
		setAtributo(RENDERED_PECA_VINCULADA, renderedVincularPecaAoDocumento);
		setAtributo(RENDERED_ADICIONA_INTIMANDO, renderedAdicionaIntimando);
		setAtributo(RENDERED_ADICIONA_REU, renderedAdicionaReu);
		setAtributo(RENDERED_ADICIONA_CITANDO, renderedAdicionaCitando);
		setAtributo(RENDERED_TELA_PARTES_SELECIONADAS, renderedTelaDePartesSelecionadas);
		setAtributo(RENDERED_PECA_VINCULADA_NOVAMENTE, renderedVincularPecaNovamenteDocumento);
		setAtributo(RENDERED_BOTAO_ADICIONA_INTIMANDO, renderedBotaoAdicionarIntimando);
		setAtributo(RENDERED_BOTAO_ADICIONA_REU, renderedBotaoAdicionarReu);
		setAtributo(RENDERED_BOTAO_ADICIONA_CITANDO, renderedBotaoAdicionarCitando);
		setAtributo(RENDERED_LISTA_PECAS_VINCULADAS, renderedListaPecasVinculadas);
		setAtributo(RENDERED_TELA_PROCESSO_LOTE, renderedTelaProcessoLote);
		setAtributo(RENDERED_DESABILITA_MENU_MODELO, renderedDesabilitaMenuModelo);
		setAtributo(RENDERED_TRANS_OBJETO_INCIDENTE, renderedTransObjetoIncidente);
		setAtributo(RENDERED_OBS_ANDAMENTO, renderedAlteraObsAndamento);
		setAtributo(KEY_LISTA_ARQ_PROCESSO_ELET, listaVinculadaPecaAoDocumento);
		setAtributo(KEY_LISTA_PECA_PROCESSO_ELET_VINCULADAS, listaPecaProcessoEletronicoVinculadas);
		setAtributo(COMDOC_TEMP_ROWDATA, comDocumentoTemporariaRowData);
		setAtributo(LISTA_PROCESSO_LOTE, itensProcessoEmLote);
		setAtributo(SIG_CLASSE_PROCES_PESQ_PRINCIPAL, siglaClassePesqPrincipal);
		setAtributo(NUM_PROCESSO_PESQ_PRINCIPAL, numeroProcessoPesqPrincipal);
		setAtributo(PROCESSO_PESQUISADO_PRINCIPAL, processo);
		setAtributo(SIG_CLASSE_PROCES_PESQ_LOTE, siglaClassePesqLote);
		setAtributo(NUM_PROCESSO_PESQ_LOTE, numeroProcessoPesqLote);
		setAtributo(PROCESSO_PESQUISADO_LOTE, processoLote);
		setAtributo(RENDERED_COMPONENTES_ALTERACAO, renderedComponentesAlteracao);
		setAtributo(LISTA_OBJETO_INCIDENTE_LOTE, listaObjetoIncidenteLote);
		setAtributo(LISTA_PECAS, listaPecasProcessoLote);

		applyStateInHttpSession();

	}

	// ------------------------ ACTIONS ------------------------

	public void adicionaNomeParaParteSelecionadaAction(ActionEvent evt) {
		adicionaNomeParaParteSelecionada();
		atualizaSessao();
	}

	public void alterarVinculoDePecasComunicacaoAction(ActionEvent evt) {
		alterarVinculoDePecasComunicacao();
		atualizaSessao();
	}

	public void atualizarMarcacao(ActionEvent evt) {
		setListaDocumentos(listaDocumentos);
	}

	public void atualizaSessaoAction(ActionEvent evt) {
			if (renderedTransObjetoIncidente){
			verificaECarregaPecaVinculadaETagLivre();
		}
	
		atualizaSessao();
	}

	public void cancelaPDFDocumentoAction(ActionEvent evt) {
		cancelaPDFDocumento();
		atualizaSessao();
	}

	public void criaListaPecasAoDocumentoAction(ActionEvent evt) {
		criaListaPecasAoDocumento();
		atualizaSessao();
	}

	public void encaminharDocumentosAction(ActionEvent evt) {
		encaminharDocumentos();
		atualizaSessao();
	}

	public void excluirDocumentosSelecionadosAction(ActionEvent evt) {
		removerDocumento();
		atualizaSessao();
	}

	public void gravarValoresDasTagsAction(ActionEvent evt) {
		gravarValoresDasTags();
		atualizaSessao();
	}

	public void limparTelaAction(ActionEvent evt) {
		limpaTela();
		atualizaSessao();
	}

	public void localizaTagPreDefinidasAction(ActionEvent evt) {
		localizaTagPreDefinidasEPartes();
		atualizaSessao();
	}

	public void marcarTodosTextos(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaDocumentos);
		setListaDocumentos(listaDocumentos);
	}

	public void marcarTodasPecasVinculadas(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaPecaProcessoEletronico);
		setListaPecaProcessoEletronico(listaPecaProcessoEletronico);
	}

	public void pesquisarDocumentosAction(ActionEvent evt) {
		pesquisarDocumentos();
		atualizaSessao();
	}

	public void procurarModelosPeloTipoDoModeloAction(ActionEvent evt) {
		setItensModelos(montarModelosFiltrados());
		atualizaSessao();
	}

	public void procurarModelosPeloTipoPermissaoAction(ActionEvent evt) {
		setItensModelos(montarModelosFiltrados());
		procurarTiposModelos();
		atualizaSessao();
	}

	public void renderizaCampoAdicionaIntimandoAction(ActionEvent evt) {
		setRenderedAdicionaIntimando(true);
		setRenderedBotaoAdicionarIntimando(false);
		atualizaSessao();
	}

	public void renderizaCampoAdicionaReuAction(ActionEvent evt) {
		setRenderedAdicionaReu(true);
		setRenderedBotaoAdicionarReu(false);
		atualizaSessao();
	}

	public void renderizaCampoAdicionaCitandoAction(ActionEvent evt) {
		setRenderedAdicionaCitando(true);
		setRenderedBotaoAdicionarCitando(false);
		atualizaSessao();
	}

	public void renderizaTelaCriaDocumentosAction(ActionEvent evt) {
		setRenderizaNovoTexto(true);
		setListaDocumentos(null);
		setSiglaNumeroProcesso("");
		atualizaSessao();
	}

	/**
	 * Adiciona o nome do processo na lista de processos para serem mostrados na tela e adiciona os objetos incidentes para serem armazenados na tabela
	 * associativa Comunicacao Incidente
	 */
	public void adicionaListaProcessoAction(ActionEvent evt) {
		adicionaListaProcessoAction();
		atualizaSessao();
	}

	public void retiraProcessoDaListaAction(ActionEvent evt) {
		retiraProcessoDaLista();
		atualizaSessao();
	}

	public void verificaECarregaPecaVinculadaETagLivreAction(ActionEvent evt) {
		verificaECarregaPecaVinculadaETagLivre();
		atualizaSessao();
	}

	public void vincularPecasNovamenteAoDocumentoAction(ActionEvent evt) {
		vincularPecasNovamenteAoDocumento();
		atualizaSessao();
	}

	public void editarDocumento(ActionEvent evt) {
		if (!isUsuarioEditorTextos() && !isUsuarioMaster()) {
			reportarAviso("Usuário sem permissão para editar documentos");
			return;
		}

		ComunicacaoDocumento documentoComunicacao = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaDocumentos.getRowData()).getWrappedObject();
		setDocumentoEditar(documentoComunicacao.getComunicacao());
		setNomeDocumentoEditar(documentoComunicacao.getComunicacao().getDscNomeDocumento());
	}

	@SuppressWarnings("deprecation")
	public void salvarTextoEditado(ActionEvent evt) {
		if (isTextoModificado()) {

			documentoEditar.setDscNomeDocumento(nomeDocumentoEditar);

			ComunicacaoService comunicacaoService = getComunicacaoService();
			try {
				comunicacaoService.alterar(documentoEditar);
				reportarInformacao("Texto atualizado com sucesso.");
			} catch (ServiceException e) {
				reportarErro("Erro ao salvar texto");
				return;
			}
		}
	}

	private boolean isTextoModificado() {
		boolean modificado = false;
		if (!documentoEditar.getDscNomeDocumento().equals(nomeDocumentoEditar)) {
			modificado = true;
		}

		return modificado;
	}

	public void recuperaLinhaParaModalPanel(ActionEvent evt) {
		comDocumentoTemporariaRowData = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaDocumentos.getRowData()).getWrappedObject();
		atualizaSessao();
	}
	
	public void editaObservacaoComunicacaoAction(ActionEvent evt){
		comDocumentoTemporariaRowData = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaDocumentos.getRowData()).getWrappedObject();
		setObservacaoComunicacaoAlterada(comDocumentoTemporariaRowData.getComunicacao().getObsComunicacao());
		atualizaSessao();
	}
	
	@SuppressWarnings("deprecation")
	public void alteraObsComunicacaoAction(ActionEvent evt){
		Comunicacao comObsAlterada = comDocumentoTemporariaRowData.getComunicacao();
		comObsAlterada.setObsComunicacao(observacaoComunicacaoAlterada);
		try {
			getComunicacaoService().salvar(comObsAlterada);
		} catch (Exception e) {
			reportarErro("Erro ao alterar a observação do documento.");
		}
	}

	// ---------------------------- METHODS ----------------------------

	public String abrirDocumento() {
		getComunicacaoServiceLocal().atualizarSessao();
		ComunicacaoDocumento documentoComunicacao = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaDocumentos.getRowData()).getWrappedObject();
		Comunicacao documento = documentoComunicacao.getComunicacao();
		
		if(documento.getArquivoEletronico() == null){
			reportarAviso("Esse tipo de modelo não permite edição.");
			return null;
		}

		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();

		RequisicaoAbrirDocumento req = new RequisicaoAbrirDocumento();
		req.setArquivoEletronico(documento.getArquivoEletronico());
		req.setNomeDocumento(documento.getDscNomeDocumento());
		req.setComunicacao(documento);
		req.setUser(usuarioAssinatura.getUsername());
		req.setGerarPDF(true);

		// Verifica se o usuário tem perfil para alterar o texto
		if (!isUsuarioEditorTextos() && !isUsuarioMaster()) {
			req.setReadOnly(true);
		} else {
			req.setReadOnly(false);
		}

		req.setTipoSalvar(ArgumentosAbrirDocumento.TIPO_ACAO_SALVAR_SERVIDOR);
		req.setDocumento(documento.getArquivoEletronico().getConteudo());

		setRequestValue(RequisicaoJnlp.REQUISICAO_JNLP, req);

		return "stfOfficeServlet";
	}

	/**
	 * Adiciona o nome da parte na combo para que o usuário possa selecionar o nome adicionado
	 */
	private void adicionaNomeParaParteSelecionada() {

		if (nomeIntimandoAdicionado != null && !nomeIntimandoAdicionado.isEmpty() && renderedAdicionaIntimando) {
			itensCategoriaIntimando.add(new SelectItem(nomeIntimandoAdicionado, nomeIntimandoAdicionado));
			setRenderedBotaoAdicionarIntimando(true);
			setRenderedAdicionaIntimando(false);
		} else if (nomeReuAdicionado != null && nomeReuAdicionado.trim().length() > 0L && renderedAdicionaReu) {
			itensCategoriaReu.add(new SelectItem(nomeReuAdicionado, nomeReuAdicionado));
			setRenderedBotaoAdicionarReu(true);
			setRenderedAdicionaReu(false);
		} else if (nomeCitandoAdicionado != null && nomeCitandoAdicionado.trim().length() > 0L && renderedAdicionaCitando) {
			itensCategoriaCitanda.add(new SelectItem(nomeCitandoAdicionado, nomeCitandoAdicionado));
			setRenderedBotaoAdicionarCitando(true);
			setRenderedAdicionaCitando(false);
		}
	}

	/**
	 * Altera dos dados referente a comunicação selecionada
	 */
	//TODO
	@SuppressWarnings("deprecation")
	private void alterarVinculoDePecasComunicacao() {

		ComunicacaoIncidenteService comunicacaoIncidenteService = getComunicacaoIncidenteService();

		if ((!isUsuarioEditorTextos()) && (!isUsuarioMaster())) {
			reportarAviso("Usuário sem permissão para alterar documentos");
			return;
		}

		ComunicacaoDocumento comunicacaoDocumento = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaDocumentos.getRowData()).getWrappedObject();

		if (comunicacaoDocumento.getComunicacao().getPecasProcessoEletronico() != null
				&& comunicacaoDocumento.getComunicacao().getPecasProcessoEletronico().size() > 0) {
			setRenderedVincularPecaAoDocumento(true);
			setObjetoIncidenteNovo(comunicacaoDocumento.getComunicacao().getObjetoIncidenteUnico().getId());

			List<ComunicacaoIncidente> listaCi = null;
			List<ObjetoIncidente<?>> listaOb = new ArrayList<ObjetoIncidente<?>>();
			List<PecaProcessoEletronicoComunicacao> listaPecaProcessoComunicacaoSelecionada = comunicacaoDocumento.getComunicacao().getPecasProcessoEletronico();

			try {
				listaCi = comunicacaoIncidenteService.verificaSeExisteProcessosVinculados(comunicacaoDocumento.getComunicacao());
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar a lista de processos vinculados.", e, LOG);
			}

			if (CollectionUtils.isNotVazia(listaCi)) {
				for (ComunicacaoIncidente ci : listaCi) {
					listaOb.add(ci.getObjetoIncidente());
				}
			}

			setRenderedVincularPecaNovamenteDocumento(true);
			setRenderedBotaoVincularPeca(false);
			pesquisarPecasPeloObjetoIncidente(objetoIncidenteNovo, false, listaOb, listaPecaProcessoComunicacaoSelecionada);
			setDocumentoEditar(comunicacaoDocumento.getComunicacao());
		}
	}

	public void cancelaPDFDocumento() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();

		try {
			comunicacaoServiceLocal.cancelarPDF(comDocumentoTemporariaRowData.getDocumentoComunicacao(), anotacaoCancelamento, usuarioAssinatura, true);
			reportarInformacao("PDF(s) cancelado(s) com sucesso!");
			getRefreshController().executarRefreshPagina();
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Não foi possível cancelar o(s) documento(s) selecionado(s).", exception, LOG);
		}
	}

	/**
	 * Método responsável em criar o novo texto a partir do modelo.
	 */
	@SuppressWarnings("deprecation")
	public String criarNovoTexto() {
		// busca o usuário logado
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();

		// serviços
		SituacaoMinistroProcessoService sitMinProcessoService = getSituacaoMinistroProcessoService();
		TagsLivresUsuarioService tagsLivresUsuarioService = getTagsLivresUsuarioService();

		// envia o conjunto de autoridades para o montador verificar
		// se existe o respectivo campo no modelo de documento

		Ministro ministroRelator = new Ministro();

		// classe reponsánvel em armazenar os valores que serão carregados no
		// documento
		SpecDadosDocumentoSecretaria dados = new SpecDadosDocumentoSecretaria();

		tipoJulgamentoTexto = StringUtils.getStringNulaSeVazia(tipoJulgamentoTexto);
		codigoMinistroTexto = NumberUtils.getLongNuloSeIgualZero(codigoMinistroTexto);
		nomeDoDocumento = StringUtils.getStringNulaSeVazia(nomeDoDocumento);

		// é obrigatória a escolha de um tipo de modelo e modelo
		if (codigoTipoModelo == null || codigoTipoModelo == 0L) {
			reportarAviso("Favor escolher um tipo de modelo e modelo.");
			return null;
		}

		// a escolha do modelo é obrigatória para que o sistema
		// possa abrir o documento de acordo com o tipo de modelo
		if (NumberUtils.isZero(codigoModelo)) {
			reportarAviso("Favor escolher o modelo");
			return null;
		}

		if (NumberUtils.isZero(objetoIncidenteNovo)) {
			reportarAviso("Favor selecionar um processo.");
			return null;
		}

		if (modeloComunicacao == null) {
			reportarAviso("Selecione um modelo de documento.");
			return null;
		} else {
			setNomeDoDocumento(modeloComunicacao.getDscModelo());
		}
		
		if (renderedAlteraObsAndamento && observacaoAndamento.trim().length() == 0){
			reportarAviso("Preencha a observação do andamento.");
			return null;
		}
		
		// preenche o conjunto com os valores das tags
		if (CollectionUtils.isNotVazia(conjuntoTags)) {
			gravarValoresDasTags();
		}

		List<TagsLivresUsuario> listaDeAutoridades = new ArrayList<TagsLivresUsuario>();

		try {
			listaDeAutoridades = tagsLivresUsuarioService.pesquisarNomeRotuloOuDescricao(null, 2L, null);
		} catch (ServiceException e) {
			reportarErro("Erro ao buscar a lista de autoridades.");
		}

		Map<String, String> conjuntoAutoridade = montaConjuntoDeAutoridades(listaDeAutoridades);

		// declaração das listas que serão utilizadas
		List<ParteProcessoSecretaria>  lista = buscaListaDePartesSelecionadas(null);

		// recuperar o ministro relator do processo
		if (objetoIncidenteProcurado != null) {
			try {
				ministroRelator = sitMinProcessoService.recuperarMinistroRelatorAtual(objetoIncidenteProcurado);
			} catch (Exception e) {
				reportarErro("Erro ao pesquisar relator atual");
			}
		}

		// carrega o objeto dados com os valores pesquisados.
		if (processo != null) {
			dados.setSiglaProcesso(siglaClassePesqPrincipal);
			dados.setNumeroProcesso(numeroProcessoPesqPrincipal);
			if (isProcesso(objetoIncidenteProcurado)) {
				dados.setSiglaProcessoExtenso(TextoUtil.capitalizar(processo.getClasseProcessual().getDescricao()));
			} else {
				dados.setSiglaProcessoExtenso(TextoUtil.capitalizar(objetoIncidenteProcurado.getDescricao()));
			}

			if (ministroRelator != null) {
				dados.setMinistroRelator(ministroRelator.getNomeMinistroCapsulado(true));
				dados.setNumeroTurmaJulgamento(ministroRelator.getCodigoTurma());
				dados.setNomeRelatorSemVocativo(ministroRelator.getNomeMinistroCapsulado(false));
			} else {
				dados.setMinistroRelator("Presidência");
				dados.setNomeRelatorSemVocativo("Presidência");
			}
		}else{
     		preencheDadosProcessos(getSiglaNumeroProcesso());
     		
     		dados.setSiglaProcesso(siglaClassePesqPrincipal);
			dados.setNumeroProcesso(numeroProcessoPesqPrincipal);
			if (isProcesso(objetoIncidenteProcurado)) {
				dados.setSiglaProcessoExtenso(TextoUtil.capitalizar(processo.getClasseProcessual().getDescricao()));
			} else {
				dados.setSiglaProcessoExtenso(TextoUtil.capitalizar(objetoIncidenteProcurado.getDescricao()));
			}

			if (ministroRelator != null) {
				dados.setMinistroRelator(ministroRelator.getNomeMinistroCapsulado(true));
				dados.setNumeroTurmaJulgamento(ministroRelator.getCodigoTurma());
				dados.setNomeRelatorSemVocativo(ministroRelator.getNomeMinistroCapsulado(false));
			} else {
				dados.setMinistroRelator("Presidência");
				dados.setNomeRelatorSemVocativo("Presidência");
			}
     	}

		dados.setNomeModeloDocumento(TextoUtil.capitalizar(modeloComunicacao.getTipoComunicacao().getDescricao()));

		dados.setListaDePartes(lista);

		// seta os valores das tag livres se existirem
		dados.setListaTagsLivres(conjuntoTagsLivres);

		// seta os valores das tags de autoridades;
		dados.setListaDeAutoridades(conjuntoAutoridade);

		// mapa das partes selecionadas nas combos quando o modelo possuir a
		// flag de partes
		conjuntoPartesSelecionadas = new HashMap<String, String>();

		// se houver valor na variável de autor, será carregado para o montador
		// texto
		if (StringUtils.isNotVazia(nomeCategoriaAutor)) {
			NomeEIdJurisdicionado nomeIdAutor = new NomeEIdJurisdicionado();
			nomeIdAutor = retiraIdDaCategoriaNoString(nomeCategoriaAutor);
			conjuntoPartesSelecionadas.put("NOME_AUTOR", nomeIdAutor.getNome());
		} else if (renderedComboAutores) {
			reportarAviso("Selecione um autor da combo de autores.");
			return null;
		} else {
			conjuntoPartesSelecionadas.put("NOME_AUTOR", null);
		}

		// se houver valor na variável de réu, será carregado para o montador
		// texto
		if (StringUtils.isNotVazia(nomeCategoriaReu)) {
			NomeEIdJurisdicionado nomeIdReu = new NomeEIdJurisdicionado();
			nomeIdReu = retiraIdDaCategoriaNoString(nomeCategoriaReu);
			conjuntoPartesSelecionadas.put("NOME_REU", nomeIdReu.getNome());
		} else if (renderedComboReus) {
			reportarAviso("Selecione um réu da combo de réus.");
			return null;
		} else {
			conjuntoPartesSelecionadas.put("NOME_REU", null);
		}

		// se houver valor na variável de cintando, será carregado para o
		// montador texto
		if (StringUtils.isNotVazia(nomeCategoriaCitanda)) {
			NomeEIdJurisdicionado nomeIdCitanda = new NomeEIdJurisdicionado();
			nomeIdCitanda = retiraIdDaCategoriaNoString(nomeCategoriaCitanda);
			conjuntoPartesSelecionadas.put("NOME_CITANDA", nomeIdCitanda.getNome());
		} else if (renderedComboCitanda) {
			reportarAviso("Selecione um citando da combo de citandos.");
			return null;
		} else {
			conjuntoPartesSelecionadas.put("NOME_CITANDA", null);
		}

		// se houver valor na variável de advogado, será carregado para o
		// montador texto
		if (StringUtils.isNotVazia(nomeCategoriaAdvogado)) {
			NomeEIdJurisdicionado nomeIdAdvogado = new NomeEIdJurisdicionado();
			nomeIdAdvogado = retiraIdDaCategoriaNoString(nomeCategoriaAdvogado);
			conjuntoPartesSelecionadas.put("NOME_ADVOGADO", nomeIdAdvogado.getNome());
		} else if (renderedComboAdvogados) {
			reportarAviso("Selecione um advogado da combo de advogados.");
			return null;
		} else {
			conjuntoPartesSelecionadas.put("NOME_ADVOGADO", null);
		}

		if (StringUtils.isNotVazia(nomeCategoriaParte)) {
			NomeEIdJurisdicionado nomeIdParte = new NomeEIdJurisdicionado();
			nomeIdParte = retiraIdDaCategoriaNoString(nomeCategoriaParte);
			conjuntoPartesSelecionadas.put("NOME_PARTE", nomeIdParte.getNome());
			conjuntoPartesSelecionadas.put("NOME_CATEGORIA", nomeIdParte.getId());
		} else if (renderedComboParte) {
			reportarAviso("Selecione uma parte da combo de partes.");
			return null;
		} else {
			conjuntoPartesSelecionadas.put("NOME_PARTE", null);
			conjuntoPartesSelecionadas.put("NOME_CATEGORIA", null);
		}

		// preenche o a tag intimando com o valor selecionado na combo
		if (StringUtils.isNotVazia(nomeCategoriaIntimando)) {
			NomeEIdJurisdicionado nomeIdIntimando = new NomeEIdJurisdicionado();
			nomeIdIntimando = retiraIdDaCategoriaNoString(nomeCategoriaIntimando);
			conjuntoPartesSelecionadas.put("NOME_INTIMANDO", nomeIdIntimando.getNome());
		} else if (renderedComboIntimandos) {
			reportarAviso("Selecione um intimando da combo de intimandos ou adicione um novo.");
			return null;
		} else {
			conjuntoPartesSelecionadas.put("NOME_INTIMANDO", null);
		}

		dados.setListaCategoriaSelecionada(conjuntoPartesSelecionadas);

		// seta os nomes dos processsos na lista para serem enviado ao montador
		// texto
		if (CollectionUtils.isNotVazia(listaObjetoIncidenteLote)) {
			List<String> listaNomeProcesso = new LinkedList<String>();

			// Inclui o nome do ObjetoIncidente principal
			ObjetoIncidente<?> objInc = recuperaObjetoIncidente(objetoIncidenteNovo);
			listaNomeProcesso.add(construirNomeProcesso(objInc));

			for (ObjetoIncidente<?> obLote : listaObjetoIncidenteLote) {
				listaNomeProcesso.add(construirNomeProcesso(obLote));
			}

			dados.setListaProcessosLote(listaNomeProcesso);
		}
		
		if(modeloComunicacao != null){
			if (modeloComunicacao.getFlagVinculoPecaProcessoElet().toString().equalsIgnoreCase("S") && listaVinculadaPecaAoDocumento.isEmpty()) {
				reportarAviso("Vincule uma peça ao documento.");
				return null;
				}
		}

		// carrega a requisição com os valores pré-definidos
		RequisicaoAbrirTexto req = new RequisicaoAbrirTexto(codigoModelo, codigoMinistroTexto, nomeDoDocumento, objetoIncidenteProcurado, dados,
				usuarioAssinatura, usuarioAssinatura.getUsername().toUpperCase(), listaVinculadaPecaAoDocumento, listaObjetoIncidenteLote);

		if (processo == null || modeloComunicacao.getFlagPartes().getDescricao().equals(FlagGenericaModeloComunicacao.N.getDescricao())) {
			dados.setExisteCabecalho(false);
		} else {
			dados.setExisteCabecalho(true);
		}
		// varre o documento procurando a tag de numeração única. Caso encontre ele irá atualizar o
		// a numeração na tabela tipo_modelo_comunicacao e o numero do documento na tabela comunicacao
		getComunicacaoServiceLocal().atualizarSessao();
		ODSingleXMLDocument single = geraODSingleXMLDocumentoParaProcura(modeloComunicacao.getArquivoEletronico().getConteudo());

		if (single.asString().contains("@@NUMERO_DO_DOCUMENTO@@")) {
			req.setIsNumeracaoUnica(true);
		}else{
			req.setIsNumeracaoUnica(false);
		}

		req.setArquivoEletronico(modeloComunicacao.getArquivoEletronico());
		req.setDocumento(modeloComunicacao.getArquivoEletronico().getConteudo());
		req.setNomeDocumento(nomeDoDocumento);
		req.setUsuarioCriacao(usuarioAssinatura.getUsername().toUpperCase());
		req.setGerarPDF(true);

		// Verifica se o usuário tem perfil para alterar o texto
		if (!isUsuarioEditorTextos() && !isUsuarioMaster()) {
			req.setReadOnly(true);
		} else {
			req.setReadOnly(false);
		}
		
		req.setObsComunicacao(obsDocumento);
		
		if (observacaoAndamento != null && observacaoAndamento.trim().length() > 0){
			req.setObservacaoAndamento(observacaoAndamento);			
		}

		req.setTipoSalvar(ArgumentosAbrirDocumento.TIPO_ACAO_SALVAR_SERVIDOR);

		setRequestValue(RequisicaoJnlpDocumento.REQUISICAO_JNLP, req);

		limpaTela();

		try {
			getObjetoIncidenteService().registrarLogSistema(req.getObjetoIncidenteProcurado(), "CONSULTA_PROCESSO", "Criar Expediente: "+req.getNomeDoDocumento(), req.getObjetoIncidenteProcurado().getId(), "JUDICIARIO.PROCESSO");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "stfOfficeServlet";
	}

	private boolean isProcesso(ObjetoIncidente<?> objetoIncidente) {
		return objetoIncidente.getTipoObjetoIncidente().equals(TipoObjetoIncidente.PROCESSO);
	}

	/*
	 * FORMATO: Caso possua relator: ADI XX – Relator Min. Yyyy Sem relator: ADI XX - Presidência (Definido pela SEJ em 02/01/2012)
	 */
	private String construirNomeProcesso(ObjetoIncidente<?> objetoIncidente) {
		StringBuilder nomeProcesso = new StringBuilder();
		Ministro ministro = null;

		try {
			ministro = getMinistroService().recuperarMinistroRelatorIncidente(objetoIncidente);
		} catch (ServiceException e) {
			reportarErro(MessageFormat.format("Erro ao pesquisar o ministro associado ao processo: {0}.", objetoIncidente.getIdentificacao()), e, LOG);
		}

		nomeProcesso.append(objetoIncidente.getIdentificacao());
		if (ministro != null) {
			String nomeMinistroRelator = ministro.getNome() != null ? ministro.getNomeMinistroCapsulado(true, false, true) : "";
			nomeProcesso.append(" - Relator ").append(nomeMinistroRelator);
		} else {
			nomeProcesso.append(" - Presidência");
		}

		return nomeProcesso.toString();
	}

	/**
	 * A string possui nome e id concatenados. Este método retira o id e retorna somente o nome da parte
	 */
	public NomeEIdJurisdicionado retiraIdDaCategoriaNoString(String nomeCategoriaComId) {

		StringBuffer nomeCategoriaSemId = new StringBuffer();
		StringBuffer idCategoria = new StringBuffer();

		int i;
		for (i = 0; i < nomeCategoriaComId.length(); i++) {
			if (nomeCategoriaComId.charAt(i) != '#') {
				nomeCategoriaSemId.append(nomeCategoriaComId.charAt(i));
			} else {
				break;
			}
		}

		for (int j = i + 1; j < nomeCategoriaComId.length(); j++) {
			idCategoria.append(nomeCategoriaComId.charAt(j));
		}

		NomeEIdJurisdicionado nomeId = new NomeEIdJurisdicionado(idCategoria.toString(), nomeCategoriaSemId.toString());

		return nomeId;
	}

	public class NomeEIdJurisdicionado {

		private String id;
		private String nome;

		public NomeEIdJurisdicionado() {
		}

		public NomeEIdJurisdicionado(String id, String nome) {
			this.id = id;
			this.nome = nome;
		}

		public String getId() {
			return id;
		}

		public String getNome() {
			return nome;
		}
	}

	private void encaminharDocumentos() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		}

		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

		final boolean permitirGabinete = true;
		final boolean incluirFaseNoDeslocamento = true;
		final boolean naoIncluirFaseDeslocamentoSeNaoForGabinete = false;

		TipoFaseComunicacao[] tiposFasesPermitidos = { TipoFaseComunicacao.PDF_GERADO, TipoFaseComunicacao.REVISADO };

		try {
			comunicacaoServiceLocal.encaminharParaAssinaturaSetor(selecionados, idSetorDestino, permitirGabinete, incluirFaseNoDeslocamento,
					naoIncluirFaseDeslocamentoSeNaoForGabinete, tiposFasesPermitidos);

			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);
			listaDocumentos.removeAll(selecionadosCheckable);
			reportarInformacao("Documento(s) encaminhados(s) com sucesso!");
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao encaminhar documento(s).", exception, LOG);
		}
	}

	/**
	 * Método responsável em localizar no documento as tags de preenchimento livre
	 */
	public void localizaTagPreDefinidasEPartes() {
		getComunicacaoServiceLocal().atualizarSessao();
		ODSingleXMLDocument single = geraODSingleXMLDocumentoParaProcura(modeloComunicacao.getArquivoEletronico().getConteudo());

		// verifica se o modelo possui a flag de tag livres. Caso possua flag
		// marcada como 'S'
		// será verificado quais são as tags incluidas e mostrará na tela as
		// tags a serem preenchidas.
		if (modeloComunicacao.getFlagCampoLivre().equals(FlagGenericaModeloComunicacao.S)) {
			List<TagsLivresUsuario> listaTagsLivres = null;
			setRenderedTelaDeTagsLivreDePreenchimento(false);

			listaTagsLivres = recuperarTodasTagsLivres();

			// faz sort do map para deixar ordenado
			conjuntoTags = new TreeMap<String, String>();

			for (TagsLivresUsuario tags : listaTagsLivres) {
				if (single.asString().contains(tags.getCodigoRotulo())) {
					conjuntoTags.put(tags.getNomeRotulo(), "");
					preencheValorSeExisteTagNoDocumento(true);
				}
			}

			if (!renderedTelaDeTagsLivreDePreenchimento) {
				reportarAviso("O modelo não possui campos livres de preenchimento.");
				// exibe o botão de criar um novo documento
			}
		}

		// Procura no modelo do documento se exisite a tag de partes
		if (modeloComunicacao.getFlagPartes().equals(FlagGenericaModeloComunicacao.S)) {
			localizaTagDePartes(single);
		}
	}

	private List<TagsLivresUsuario> recuperarTodasTagsLivres() {
		List<TagsLivresUsuario> listaTagsLivres = Collections.emptyList();
		TagsLivresServiceLocal tagsLivresServiceLocal = getTagsLivresServiceLocal();

		try {
			listaTagsLivres = tagsLivresServiceLocal.pesquisarTodasTagsLivres();
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar lista de tags livres.", exception, LOG);
		}

		return listaTagsLivres;
	}
	
	private void pesquisarDocumentosCorrecao() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		List<ComunicacaoDocumentoResult> documentos = Collections.emptyList();

		try {
			UsuarioAssinatura usuario;

			if (isUsuarioGestorTextos() || isUsuarioMaster()) {
				usuario = null;
			} else {
				usuario = (UsuarioAssinatura) getUser();
			}

			documentos = comunicacaoServiceLocal.pesquisarDocumentosCorrecao(getSetorUsuarioAutenticado(), usuario);

			if (CollectionUtils.isVazia(documentos)) {
				reportarAviso("Nenhum documento encontrado com os parâmetros informados.");
				limpaTela();
				return;
			} else {
				setRenderizaNovoTexto(false);
				reportarInformacao(MessageFormat.format("Foi(ram) encontrado(s) {0} documento(s) com os parâmentos informados.", documentos.size()));
			}
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar documentos.", exception, LOG);
		}

		setRenderedBotaoNovoTexto(false);
		OrdenacaoUtils.ordenarListaComunicacaoDocumentoResultProcesso(documentos);
		setListaDocumentos(getCheckableDocumentoList(documentos));
	}

	private void pesquisarDocumentos() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		List<ComunicacaoDocumentoResult> documentos = Collections.emptyList();

		codigoModelo = NumberUtils.getLongNuloSeIgualZero(codigoModeloPesq);

		try {
			UsuarioAssinatura usuario;

			if (isUsuarioGestorTextos() || isUsuarioMaster()) {
				usuario = null;
			} else {
				usuario = (UsuarioAssinatura) getUser();
			}
				documentos = comunicacaoServiceLocal.pesquisarDocumentos(objetoIncidente, codigoModelo, getSetorUsuarioAutenticado(), usuario);
			if (CollectionUtils.isVazia(documentos)) {
				reportarAviso("Nenhum documento encontrado com os parâmetros informados.");
				limpaTela();
				return;
			} else {
				setRenderizaNovoTexto(false);
				reportarInformacao(MessageFormat.format("Foi(ram) encontrado(s) {0} documento(s) com os parâmentos informados.", documentos.size()));
			}
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar documentos.", exception, LOG);
		}

		setRenderedBotaoNovoTexto(false);
		OrdenacaoUtils.ordenarListaComunicacaoDocumentoResultProcesso(documentos);
		setListaDocumentos(getCheckableDocumentoList(documentos));
	}

	public void limpaTela() {
		setRenderedComboIntimandos(false);
		setRenderedComboParte(false);
		setSiglaNumeroProcesso("");
		setSiglaClassePesqPrincipal("");
		setNomeDoDocumento("");
		setSiglaClassePesqLote("");
		setObsDocumento("");
		setObjetoIncidenteProcurado(null);
		setObjetoIncidente(null);
		setCodigoTipoPermissao(null);
		setCodigoModelo(null);
		setCodigoTipoModelo(null);
		setCodigoModeloPesq(null);
		setNumeroProcessoPesqPrincipal(null);
		setNumeroProcessoPesqLote(null);
		setModeloComunicacao(new ModeloComunicacao());
		setDocumentoEditar(new Comunicacao());
		setNomeIntimandoAdicionado(new String());
		setNomeCitandoAdicionado(new String());
		setNomeReuAdicionado(new String());
		setListaDocumentos(null);
		setListaVinculadaPecaAoDocumento(null);
		setListaPecaProcessoEletronicoVinculadas(null);
		setListaObjetoIncidenteLote(null);
		setListaPecaProcessoEletronico(null);
		setRenderizaNovoTexto(true);
		setRenderedBotaoNovoTexto(Boolean.FALSE);
		setRenderedComboAutores(Boolean.FALSE);
		setRenderedVincularPecaAoDocumento(Boolean.FALSE);
		setRenderedComboReus(Boolean.FALSE);
		setRenderedComboCitanda(Boolean.FALSE);
		setRenderedComboAdvogados(Boolean.FALSE);
		setRenderedBotaoVincularPeca(Boolean.TRUE);
		setRenderedVincularPecaNovamenteDocumento(Boolean.FALSE);
		setRenderedAdicionaIntimando(Boolean.FALSE);
		setRenderedAdicionaCitando(Boolean.FALSE);
		setRenderedAdicionaReu(Boolean.FALSE);
		setRenderedTelaDePartesSelecionadas(Boolean.FALSE);
		setRenderedBotaoAdicionarIntimando(Boolean.TRUE);
		setRenderedListaPecasVinculadas(Boolean.FALSE);
		setRenderedTelaProcessoLote(Boolean.FALSE);
		setRenderedComponentesAlteracao(Boolean.FALSE);
		setRenderedDesabilitaMenuModelo(Boolean.FALSE);
		setRenderedAlteraObsAndamento(Boolean.FALSE);
		setItensModelos(montarListaSelectItemsVazia());
		setItensCategoriaReu(montarListaSelectItemsVazia());
		setItensCategoriaAdvogado(montarListaSelectItemsVazia());
		setItensCategoriaAutor(montarListaSelectItemsVazia());
		setItensProcessoEmLote(montarListaSelectItemsVazia(false));
		preencheValorSeExisteTagNoDocumento(Boolean.FALSE);
	}

	private void procurarTiposModelos() {
		setItensTipoModelos(carregarComboTipoModelosPelaListaDeModelos(listaModeloComunicacaoPelaPermissao));
		setCodigoTipoModelo(null);
		setItensModelos(montarModelosFiltrados());
	}

	private void removerDocumento() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("Selecione pelo menos um documento para ser excluído.");
		} else {
			ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

			try {
				comunicacaoServiceLocal.excluir(selecionados);

				List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);
				listaDocumentos.removeAll(selecionadosCheckable);

				reportarInformacao("Documento(s) excluído(s) com sucesso!");
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			} catch (ServiceLocalException exception) {
				reportarErro("Erro ao excluir documento(s).", exception, LOG);
			}
		}
	}

	public void adicionaListaProcessoAction() {
		if (objetoIncidenteLote != null) {
			ObjetoIncidente<?> objInc = recuperaObjetoIncidente(objetoIncidenteLote);

			if (listaObjetoIncidenteLote.contains(objInc)) {
				reportarAviso("O processo selecionado já foi adicionado à lista.");
			} else if (processo.equals(objInc)) {
				reportarAviso("O processo selecionado corresponde ao processo principal.");
			} else {
				itensProcessoEmLote.add(new SelectItem(objInc.getId(), objInc.getIdentificacao()));
				listaObjetoIncidenteLote.add(objInc);
				pesquisarPecasPeloObjetoIncidente(objInc.getId(), true, null, null);
				setSiglaNumeroProcessoLote("");
				setRenderedComponentesAlteracao(true);
			}
			objetoIncidenteLote = null;
		} else {
			reportarAviso("Favor selecionar um processo.");
		}
	}

	/**
	 * Retira os processos do listBox
	 */
	public void retiraProcessoDaLista() {
		if (CollectionUtils.isVazia(itensProcessoEmLote)) {
			reportarAviso("A lista já está vazia.");
			return;
		}

		for (SelectItem obj : itensProcessoEmLote) {
			if (obj.getValue().equals(idProcessoLoteObjetoIncidente)) {
				itensProcessoEmLote.remove(obj);
				break;
			}
		}

		if (CollectionUtils.isNotVazia(listaObjetoIncidenteLote)) {
			List<ObjetoIncidente<?>> listaSemObjetoIncidente = new LinkedList<ObjetoIncidente<?>>();
			listaSemObjetoIncidente.addAll(listaObjetoIncidenteLote);

			for (ObjetoIncidente<?> obLote : listaObjetoIncidenteLote) {
				if (obLote.getId().equals(idProcessoLoteObjetoIncidente)) {
					listaSemObjetoIncidente.remove(obLote);
					removeDaListaDePecaPeloObjetoIncidente(obLote);
				}
			}
			setListaObjetoIncidenteLote(listaSemObjetoIncidente);
		}
	}

	private ObjetoIncidente<?> getObjetoIncidente(ArquivoProcessoEletronico peca) {
		return peca.getPecaProcessoEletronico().getObjetoIncidente();
	}

	// ------------------- PEÇAS ----------------------

	/**
	 * Cria a lista de peças de acordo com os itens de peças selecionadas
	 */
	private void criaListaPecasAoDocumento() {
		listaVinculadaPecaAoDocumento = retornarItensSelecionados(listaPecaProcessoEletronico);

		if (listaVinculadaPecaAoDocumento != null && listaVinculadaPecaAoDocumento.size() > 0) {
			List<ObjetoIncidente<?>> objetoIncidentesQueNaoExistemPecasVinculadas = new LinkedList<ObjetoIncidente<?>>();
			List<ObjetoIncidente<?>> listaPrincipalELote = new LinkedList<ObjetoIncidente<?>>();
			Boolean encontraPecaReferenteAoProcesso = false;

			ObjetoIncidente<?> objInc = recuperaObjetoIncidente(objetoIncidenteNovo);
			listaPrincipalELote.add(objInc);
			listaPrincipalELote.addAll(listaObjetoIncidenteLote);

			// Sempre deverá existir ao menos uma peça vinculada para cada
			// processo selecionado (lote e principal)
			for (ObjetoIncidente<?> ob : listaPrincipalELote) {
				encontraPecaReferenteAoProcesso = false;
				// varre a lista de peças que foram setadas
				for (ArquivoProcessoEletronico ape : listaVinculadaPecaAoDocumento) {
					if (getObjetoIncidente(ape).getPrincipal().equals(ob.getPrincipal())) {
						encontraPecaReferenteAoProcesso = true;
						break;
					}
				}

				if (!encontraPecaReferenteAoProcesso) {
					objetoIncidentesQueNaoExistemPecasVinculadas.add(ob);
				}
			}

			/*
			 * Tenta encontrar na lista de todas as peças exibidas no sistema (lote e processo principal) alguma peça que possui o objeto incidente igual ao da
			 * lista de objetoIncidentes não encontrados. Este procedimeto está sendo realizado, pois o sistema deverá vincular ao menos uma peça de cada
			 * processo selecionado. O procedimento acima foi realizado, pois podem existir processos que não possuem peças.
			 */
			if (objetoIncidentesQueNaoExistemPecasVinculadas.size() > 0) {
				for (ObjetoIncidente<?> ob : objetoIncidentesQueNaoExistemPecasVinculadas) {
					for (ArquivoProcessoEletronico apel : listaPecasProcessoLote) {
						if (getObjetoIncidente(apel).equals(ob.getPrincipal())) {
							reportarAviso("Incluir ao menos uma peça do processo " + ob.getIdentificacao());
							return;
						}
					}
				}
			}
			reportarAviso("Vínculo salvo com sucesso.");
			mostraTabelaListaPecasVinculadas();
		} else {
			reportarAviso("Nenhuma peça foi marcada.");
		}
	}

	/**
	 * Verifica se o modelo possui a flag para vincular as peças do processo ao documento que será gerado.
	 */
	private void verificaECarregaPecaVinculadaETagLivre() {
		modeloComunicacao = null;

		// carrega o atributo modeloComunicacao de acordo com o tipo
		// e código do modelo
		carregarModeloSelecionado();

		if (modeloComunicacao == null) {
			reportarErro("Favor selecionar tipo e modelo.");
			return;
		}

		// seta o nome do modelo
		setNomeDoDocumento(modeloComunicacao.getDscModelo());

		if (modeloComunicacao.isProcessoLote()) {
			setRenderedTelaProcessoLote(true);
		}

		if (modeloComunicacao.possuiPecasVinculadas()) {
			// pesquisa as peças de acordo com o processo selecionado
			pesquisarPecasPeloObjetoIncidente(objetoIncidenteNovo, false, null, null);
		} else {
			setRenderedVincularPecaAoDocumento(false);
		}
		
		if (modeloComunicacao.getFlagAlterarObsAndamento() == (FlagGenericaModeloComunicacao.S)){
			setRenderedAlteraObsAndamento(true);
		}

		localizaTagPreDefinidasEPartes();
		setRenderedBotaoNovoTexto(true);
		setRenderedDesabilitaMenuModelo(true);
	}

	@SuppressWarnings("deprecation")
	public void carregarModeloSelecionado() {
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();

		try {
			if (codigoModelo != null && codigoTipoModelo != null) {
				modeloComunicacao = modeloComunicacaoService.pesquisarModeloEscolhido(codigoModelo, codigoTipoModelo);
				Hibernate.initialize(modeloComunicacao.getArquivoEletronico());
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar setor padrão.");
		}
	}

	/**
	 * Desvincula as peças já cadastras e vincula as novas peças selecionadas
	 */
	public void vincularPecasNovamenteAoDocumento() {
		PecaProcessoEletronicoComunicacaoService pecaProcessoEletronicoComunicacaoService = getPecaProcessoEletronicoComunicacaoService();

		listaVinculadaPecaAoDocumento = retornarItensSelecionados(listaPecaProcessoEletronico);

		try {
			pecaProcessoEletronicoComunicacaoService.alterarPecasVinculadasComunicacao(listaVinculadaPecaAoDocumento, documentoEditar);
		} catch (Exception e) {
			reportarErro("Erro ao alterar as peças vinculadas ao documento");
			return;
		}

		reportarAviso("Alteração salva com sucesso.");
	}

	/**
	 * Mostra na tela a lista de peças que o usuário vinculou
	 */
	public void mostraTabelaListaPecasVinculadas() {
		setRenderedListaPecasVinculadas(true);
		setListaPecaProcessoEletronicoVinculadas(getCheckableDataTableRowWrapperList(listaVinculadaPecaAoDocumento));
	}

	// -------------------- MÉTODOS QUE CARREGAM COMBOS --------------------

	/**
	 * Método responsável em carregar todas as combos que o modelo possue de forma a trazer a categoria e o nome do jurisdicionado
	 * 
	 * @param listaPartesSelecionadas
	 * @return
	 */
	public List<SelectItem> carregaComboDeCategoria(List<Long> listaPartesSelecionadas) {
		List<SelectItem> listaCategoria = new LinkedList<SelectItem>();
		List<ParteProcessoSecretaria> listaPartes = new LinkedList<ParteProcessoSecretaria>();

		if (objetoIncidenteNovo != null && objetoIncidenteNovo != 0L) {
			listaPartes = buscaListaDePartesSelecionadas(listaPartesSelecionadas);
		} else {
			setRenderedComboAutores(false);
			setRenderedComboReus(false);
			setRenderedComboAdvogados(false);
			setRenderedComboCitanda(false);
			setRenderedComboParte(false);
			atualizaSessao();
			reportarAviso("Favor escolha um processo.");
			return null;
		}

		if (listaPartes != null && listaPartes.size() > 0) {
			listaCategoria.add(new SelectItem(null, null));
			for (ParteProcessoSecretaria parte : listaPartes) {
				// o nome é concatenado com o caracter '#' para que depois a
				// categoria possa ser desmembrada do nome
				// caso exista a tag de categoria a ser preenchida no documento.
				listaCategoria.add(new SelectItem(parte.getNomeJuridiscionado() + "#" + parte.getCategoria(), parte.getCategoria() + " - "
						+ parte.getNomeJuridiscionado()));
			}
		} else {
			//reportarAviso("O processo não possui parte(s).");
			// retira a seleção caso nao encontre nemhuma parte
			setSiglaNumeroProcesso("");
		}

		return listaCategoria;
	}

	/**
	 * Busca a lista de parte pelo objetoIncidente digitado na tela
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ParteProcessoSecretaria> buscaListaDePartesSelecionadas(List<Long> partes) {
		ParteService parteService = getParteoService();
		ParteProcessoSecretaria parteProcesso;
		List<ParteProcessoSecretaria> lista = new LinkedList<ParteProcessoSecretaria>();
		List<Parte> listaPartes = null;

		// Busca as partes relacionados ao objeto incidente
		if (objetoIncidenteNovo != null && objetoIncidenteNovo != 0L) {
			objetoIncidenteProcurado = recuperaObjetoIncidente(objetoIncidenteNovo);
			// busca partes relacionadas
			try {
				listaPartes = parteService.pesquisarPartes(objetoIncidenteNovo, partes);
			} catch (Exception e) {
				reportarErro("Erro ao carregar a lista de partes");
			}
		}
		// carrega a lista de partes
		if (listaPartes != null && listaPartes.size() > 0) {
			for (Parte parte : listaPartes) {
				// cria a lista de ParteProcessoSecretaria
				parteProcesso = new ParteProcessoSecretaria();
				parteProcesso.setCodigoCategoria(parte.getCategoria().getId());
				parteProcesso.setNomeJuridiscionado(parte.getNomeJurisdicionado());
				parteProcesso.setCategoria(parte.getCategoria().getDescricao());
				parteProcesso.setNumeroOrdem(parte.getNumeroOrdem());
				lista.add(parteProcesso);
			}

			Collections.sort(lista, new OrdenacaoUtils.ListaParteOrdenator());
		}
		return lista;
	}

	/**
	 * monta o mapa das autoridades para ser enviado ao montador texto
	 * 
	 * @param listaAutoridades
	 * @return
	 */
	public Map<String, String> montaConjuntoDeAutoridades(List<TagsLivresUsuario> listaAutoridades) {

		Map<String, String> conjunto = new HashMap<String, String>();

		for (TagsLivresUsuario aut : listaAutoridades) {
			// particularidade solicitada pelo usuário que quando for presidente
			// de umas das turmas a palavra "ministro(a)" deverá vir primeiro
			if (aut.getNomeRotulo().equals("MINISTRO_PRESIDENTE_PRIMEIRA_TURMA") || aut.getNomeRotulo().equals("MINISTRO_PRESIDENTE_SEGUNDA_TURMA")) {
				String ministoConcat = "Ministro(a) " + aut.getDscTagLivres();
				conjunto.put(aut.getNomeRotulo(), ministoConcat);
			} else {
				conjunto.put(aut.getNomeRotulo(), aut.getDscTagLivres());
			}
		}

		return conjunto;
	}

	public List<SelectItem> montarTiposPermissoes() {
		List<SelectItem> tiposPermissoes = carregarComboTipoPermissao(true, true);
		// o 1º ítem é o tipo de permissão padrão
		codigoTipoPermissao = (Long) tiposPermissoes.get(0).getValue();
		return tiposPermissoes;
	}

	/**
	 * Monta a lista de modelos de acordo com o tipo de modelo selecionado.
	 * 
	 * Caso nenhum tipo seja selecionado, serão exibidos todos, de acordo com o tipo de permissão selecionado.
	 */
	@SuppressWarnings("deprecation")
	public List<SelectItem> montarModelosFiltrados() {
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();
		
		listaModeloComunicacaoPelaPermissao = new LinkedList<ModeloComunicacao>();
		List<SelectItem> listaModelos = new LinkedList<SelectItem>();
		listaModelos.add(new SelectItem(null, ""));

		try {
			listaModeloComunicacaoPelaPermissao = modeloComunicacaoService.pesquisar(null, codigoTipoModelo, codigoTipoPermissao, Flag.SIM);
			for (ModeloComunicacao modeloComunicacao : listaModeloComunicacaoPelaPermissao) {
				listaModelos.add(new SelectItem(modeloComunicacao.getId(), modeloComunicacao.getDscModelo()));
			}
		} catch (ServiceException exception) {
			reportarErro("Erro ao carregar a lista de modelos.", exception.getLocalizedMessage());
		}

		return listaModelos;
	}

	@SuppressWarnings("deprecation")
	public List<SelectItem> montarModelos() {
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();

		List<SelectItem> listaModelos = new LinkedList<SelectItem>();
		listaModelos.add(new SelectItem(null, ""));

		try {
			List<ModeloComunicacao> modelos = null;

			if (isUsuarioInstitucional() || isUsuarioMaster()) {
				modelos = modeloComunicacaoService.pesquisar(null, null, (Long) null, Flag.SIM);
			} else {
				modelos = modeloComunicacaoService.pesquisar(null, null, getSetorUsuarioAutenticado(), Flag.SIM);
			}

			for (ModeloComunicacao modeloComunicacao : modelos) {
				listaModelos.add(new SelectItem(modeloComunicacao.getId(), 
						modeloComunicacao.getTipoComunicacao().getDescricao() + " - " +  modeloComunicacao.getDscModelo()
						+ " (" + modeloComunicacao.getTipoPermissao().getDescricao() + ")"));
			}
		} catch (ServiceException exception) {
			reportarErro("Erro ao carregar a lista de modelos.", exception.getLocalizedMessage());
		}

		return listaModelos;
	}

	/**
	 * Remove os incidentes da comunicação. Método criado para não guardar lixo caso a comunicação possua processos vinculados.
	 */
	@SuppressWarnings("deprecation")
	public void removeComunicacaoIncidente(Comunicacao comunicacao) {
		ComunicacaoService comunicacaoService = getComunicacaoService();
		comunicacao.getComunicacaoIncidente().clear();

		try {
			comunicacaoService.salvar(comunicacao);
		} catch (ServiceException e) {
			reportarErro("Erro ao excluir os processos do documento.", e, LOG);
		}

	}

	public void pesquisarPecasPeloObjetoIncidente(Long obId, Boolean adicionaPecaProcessoLote, List<ObjetoIncidente<?>> listaLote,
			List<PecaProcessoEletronicoComunicacao> listaPecaComunicacaoSelecionada) {

		if (NumberUtils.isZero(objetoIncidenteNovo)) {
			reportarAviso("Selecione um processo.");
			return;
		}

		// limpa a lista de peças vinculadas para que não carregue os dados da
		// lista criada pelo documento anterior.
		setListaPecaProcessoEletronicoVinculadas(null);
		setListaPecaProcessoEletronico(null);

		ObjetoIncidente<?> objInc = recuperaObjetoIncidente(obId);

		if (!adicionaPecaProcessoLote) {
			listaPecasProcessoLote = pesquisaPecasPeloObIncidene(objInc);
		} else {
			carregaListaDePecasDoObjetoIncidente(objInc);
		}

		// somente entrará neste if quando o usuário desvincular as peças já
		// criadas para o documento
		if (CollectionUtils.isNotVazia(listaLote)) {
			for (ObjetoIncidente<?> ob : listaLote) {
				carregaListaDePecasDoObjetoIncidente(ob);
			}
		}
		
		if (listaPecaComunicacaoSelecionada != null && listaPecaComunicacaoSelecionada.size() > 0){
			List<CheckableDataTableRowWrapper> listaTodasPecas = new ArrayList<CheckableDataTableRowWrapper>();
			List<CheckableDataTableRowWrapper> listaPecasESelecionadas = new ArrayList<CheckableDataTableRowWrapper>();
			listaTodasPecas.addAll(getCheckableDataTableRowWrapperList(listaPecasProcessoLote));
			Boolean possuiItemSelecionado;
			for (CheckableDataTableRowWrapper check : listaTodasPecas){
				possuiItemSelecionado = false;
				for (PecaProcessoEletronicoComunicacao peca : listaPecaComunicacaoSelecionada){
					if (!peca.getExcluida() && peca.getPecaProcessoEletronico().getDocumentos().get(0).equals((ArquivoProcessoEletronico) check.getWrappedObject())){
						check.setChecked(true);
						listaPecasESelecionadas.add(check);	
						possuiItemSelecionado = true;
					}
				}
				if (!possuiItemSelecionado){
					listaPecasESelecionadas.add(check);	
				}
			}
			setListaPecaProcessoEletronico(listaPecasESelecionadas);
		}else{
			setListaPecaProcessoEletronico(getCheckableDataTableRowWrapperList(listaPecasProcessoLote));
		}

		if (CollectionUtils.isVazia(listaPecasProcessoLote)) {
			reportarAviso("Nenhuma peça encontrada.");
		} else {
			setRenderedVincularPecaAoDocumento(true);
		}
	}

	/**
	 * Carrega a lista de peças do objeto incidente
	 * 
	 * @param ob
	 */
	private void carregaListaDePecasDoObjetoIncidente(ObjetoIncidente<?> ob) {
		List<ArquivoProcessoEletronico> listaPecasTemporarias = pesquisaPecasPeloObIncidene(ob);

		if (!listaPecasProcessoLote.containsAll(listaPecasTemporarias)) {
			listaPecasProcessoLote.addAll(listaPecasTemporarias);
		}
	}

	public List<ArquivoProcessoEletronico> pesquisaPecasPeloObIncidene(ObjetoIncidente<?> ob) {

		List<ArquivoProcessoEletronico> pecas = null;

		ArquivoProcessoEletronicoService service = getArquivoProcessoEletronicoService();

		try {
			// service.limparSessao();
			// busca as peças de acordo com o objeto incidente principal.
			pecas = service.pesquisarPecasPeloIdObjetoIncidente(ob.getPrincipal().getId());
		} catch (ServiceException e1) {
			reportarErro("Erro ao pesquisar as peças vinculadas.", e1, LOG);
		}
		// }

		return pecas;
	}

	/**
	 * Remove da lista as peças adicionadas dos processos em lote
	 * 
	 * @param obIncidente
	 */
	public void removeDaListaDePecaPeloObjetoIncidente(ObjetoIncidente<?> obIncidente) {

		// lista criada para poder remover as peças encotradas. Objeto criado
		// para corrigir o erro de concurrentException
		List<ArquivoProcessoEletronico> listaSemPecasEncontradas = new LinkedList<ArquivoProcessoEletronico>();

		if (CollectionUtils.isNotVazia(listaPecasProcessoLote)) {
			listaSemPecasEncontradas.addAll(listaPecasProcessoLote);
			for (ArquivoProcessoEletronico ape : listaPecasProcessoLote) {
				ObjetoIncidente<?> objetoIncidente = getObjetoIncidente(ape);

				if (objetoIncidente instanceof Protocolo) {
					// casos em que as peças do protocolo do processo foram adicionadas
					objetoIncidente = ObjetoIncidenteUtil.getProcesso(objetoIncidente);
				}

				if (objetoIncidente.equals(obIncidente)) {
					listaSemPecasEncontradas.remove(ape);
				}
			}

			setListaPecasProcessoLote(listaSemPecasEncontradas);
			setListaPecaProcessoEletronico(getCheckableDataTableRowWrapperList(listaSemPecasEncontradas));
		}
	}

	/**
	 * Recupera o Objeto pelo id do próprio objeto incidente
	 * 
	 * @param idObjInc
	 */
	public ObjetoIncidente<?> recuperaObjetoIncidente(Long idObjInc) {
		ObjetoIncidenteService objetoIncidenteService = getObjetoIncidenteService();
		ObjetoIncidente<?> objIncidente = null;

		try {
			objIncidente = objetoIncidenteService.recuperarPorId(idObjInc);
		} catch (ServiceException e) {
			reportarErro(MessageFormat.format("Erro ao pesquisar objeto incidente: {0}.", idObjInc), e, LOG);
		}

		return objIncidente;
	}

	/**
	 * Localiza as tags de autores e reus
	 * 
	 * @param xmlDocument
	 */
	public void localizaTagDePartes(ODSingleXMLDocument xmlDocument) {
		List<Long> listaCategoriaGenerica;

		// solicitação feita pela Secretaria Judiciaria
		if (xmlDocument.asString().contains("@@NOME_AUTOR_SELECIONADO@@")) {
			listaCategoriaGenerica = new ArrayList<Long>();

			// TODO a pedido da área solicitante estamos criando hashcode a
			// lista de autores especificados
			// AVISO: poderá ocorrer problema pois os códigos em homologação
			// podem ser diferente
			// dos códigos em produção.
			listaCategoriaGenerica = criaListaDeAutoresSelecionados();
			itensCategoriaAutor = carregaComboDeCategoria(listaCategoriaGenerica);
			if (itensCategoriaAutor != null && itensCategoriaAutor.size() > 0) {
				setRenderedComboAutores(true);
			} else {
				setRenderedComboAutores(false);
				reportarAviso("Não existe(m) autor(es) para este processo.");
			}
		}

		if (xmlDocument.asString().contains("@@NOME_REU_SELECIONADO@@")) {
			listaCategoriaGenerica = new ArrayList<Long>();
			// TODO a pedido da área solicitante estamos criando hashcode a
			// lista de réus especificados
			// AVISO: poderá ocorrer problema pois os códigos em homologação
			// podem ser diferentes
			// dos códigos em produção.
			listaCategoriaGenerica = criaListaDeReusSelecionados();
			itensCategoriaReu = carregaComboDeCategoria(listaCategoriaGenerica);
			if (itensCategoriaReu != null && itensCategoriaReu.size() > 0) {
				setRenderedComboReus(true);
			} else {
				setRenderedComboReus(false);
				reportarAviso("Não existe(m) réu(s) para este processo.");
			}
		}

		if (xmlDocument.asString().contains("@@NOME_INTIMANDO_SELECIONADO@@")) {
			listaCategoriaGenerica = new ArrayList<Long>();
			// TODO a pedido da área solicitante estamos criando hashcode a
			// lista de intimandos especificados
			// AVISO: poderá ocorrer problema pois os códigos em homologação
			// podem ser diferentes
			// dos códigos em produção.
			listaCategoriaGenerica = criaListaDeIntimandosSelecionados();
			itensCategoriaIntimando = carregaComboDeCategoria(listaCategoriaGenerica);
			if (itensCategoriaIntimando != null && itensCategoriaIntimando.size() > 0) {
				setRenderedComboIntimandos(true);
			} else {
				setRenderedComboIntimandos(false);
				reportarAviso("Não existe(m) intimando(s) para este processo.");
			}
		}

		if (xmlDocument.asString().contains("@@PARTE_CITANDA_SELECIONADA@@")) {
			listaCategoriaGenerica = new ArrayList<Long>();
			// TODO a pedido da área solicitante estamos criando hashcode a
			// lista de citandos especificados
			// AVISO: poderá ocorrer problema pois os códigos em homologação
			// podem ser diferentes
			// dos códigos em produção. A lista de partes citandas é a mesma de
			// réus.
			listaCategoriaGenerica = criaListaDeReusSelecionados();
			itensCategoriaCitanda = carregaComboDeCategoria(listaCategoriaGenerica);
			if (itensCategoriaCitanda != null && itensCategoriaCitanda.size() > 0) {
				setRenderedComboCitanda(true);
			} else {
				setRenderedComboCitanda(false);
				reportarAviso("Não existe(m) parte(s) citanda(s) para este processo.");
			}
		}

		if (xmlDocument.asString().contains("@@ADVOGADO_PARTE_SELECIONADO@@")) {
			listaCategoriaGenerica = new ArrayList<Long>();
			// TODO a pedido da área solicitante estamos criando hashcode a
			// lista de advogados especificados
			// AVISO: poderá ocorrer problema pois os códigos em homologação
			// podem ser diferentes
			// dos códigos em produção.
			listaCategoriaGenerica = criaListaDeAdvogadosSelecionados();
			itensCategoriaAdvogado = carregaComboDeCategoria(listaCategoriaGenerica);
			if (itensCategoriaAdvogado != null && itensCategoriaAdvogado.size() > 0) {
				setRenderedComboAdvogados(true);
			} else {
				setRenderedComboAdvogados(false);
				reportarAviso("Não existe(m) parte(s) advogado(a)(s) para este processo.");
			}
		}

		if (xmlDocument.asString().contains("@@NOME_PARTE_SELECIONADA@@")) {
			listaCategoriaGenerica = new ArrayList<Long>();
			// TODO a pedido da área solicitante estamos criando hashcode a
			// lista de partes especificadas
			// AVISO: poderá ocorrer problema pois os códigos em homologação
			// podem ser diferentes
			// dos códigos em produção.
			listaCategoriaGenerica = criaListaPartesSelecionadas();
			itensCategoriaParte = carregaComboDeCategoria(listaCategoriaGenerica);
			if (itensCategoriaParte != null && itensCategoriaParte.size() > 0) {
				setRenderedComboParte(true);
			} else {
				setRenderedComboParte(false);
				reportarAviso("Não existe(m) parte(s) selecionada(s) para este processo.");
			}
		}

		if (renderedComboAutores || renderedComboReus || renderedComboCitanda || renderedComboIntimandos || renderedComboAdvogados || renderedComboParte) {
			setRenderedTelaDePartesSelecionadas(true);
		}
	}

	/**
	 * Método responsável em criar a lista de Autores quando existir a tag de @@NOME_AUTOR_SELECIONADO@@ no modelo de documento.
	 * 
	 * @return
	 */
	private List<Long> criaListaDeAutoresSelecionados() {
		List<Long> listaAutores = new ArrayList<Long>();
		listaAutores.add(211L); // AUTOR
		listaAutores.add(247L); // REQUERENTE
		listaAutores.add(205L); // AGRAVANTE
		listaAutores.add(209L); // ARGUENTE
		listaAutores.add(243L); // RECORRENTE
		listaAutores.add(238L); // QUERELANTE
		listaAutores.add(250L); // SUSCITANTE
		listaAutores.add(214L); // COMUNICANTE
		listaAutores.add(219L); // EXCIPIENTE
		listaAutores.add(236L); // PACIENTE
		listaAutores.add(224L); // IMPETRANTE
		listaAutores.add(299L); // PROPONENTE
		listaAutores.add(240L); // RECLAMANTE
		listaAutores.add(226L); // IMPUGNANTE
		listaAutores.add(234L); // OPOENTE
		listaAutores.add(221L); // EXEQUENTE
		listaAutores.add(217L); // EMBARGANTE

		return listaAutores;
	}

	/**
	 * Método responsável em criar a lista de Reus quando existir a tag de @@NOME_REU_SELECIONADO@@ no modelo de documento.
	 * 
	 * @return
	 */
	private List<Long> criaListaDeReusSelecionados() {
		List<Long> listaReus = new ArrayList<Long>();
		listaReus.add(228L); // INTERESSADO
		listaReus.add(248L); // REU
		listaReus.add(204L); // AGRAVADO
		listaReus.add(208L); // ARGUIDO
		listaReus.add(237L); // QUERELADO
		listaReus.add(242L); // RECORRIDO
		listaReus.add(249L); // SUSCITADO
		listaReus.add(218L); // EXCEPTO
		listaReus.add(222L); // EXTRADITANDO
		listaReus.add(212L); // COATOR
		listaReus.add(223L); // IMPETRADO
		listaReus.add(246L); // REQUERIDO
		listaReus.add(239L); // RECLAMADO
		listaReus.add(225L); // IMPUGNADO
		listaReus.add(235L); // OPOSTO
		listaReus.add(220L); // EXECUTADO
		listaReus.add(216L); // EMBARGADO
		listaReus.add(232L); // LITISCONSORTES PASSIVOS

		return listaReus;
	}

	/**
	 * Lista de advogados selecionados quanto existir a tag de @@ADVOGADO_PARTE_SELECIONADO@@
	 * 
	 * @return
	 */
	private List<Long> criaListaDeAdvogadosSelecionados() {
		List<Long> listaAdvogados = new ArrayList<Long>();
		listaAdvogados.add(257L);
		listaAdvogados.add(202L);
		listaAdvogados.add(203L);

		return listaAdvogados;
	}

	/**
	 * Cria a lista de intimandos. Esta lista é a junção da lista de reus selecionados e dos autores selecionados.
	 * 
	 * @return
	 */
	private List<Long> criaListaDeIntimandosSelecionados() {

		List<Long> listaIntimados = new ArrayList<Long>();

		for (Long idCategoriaParteAutor : criaListaDeAutoresSelecionados()) {
			listaIntimados.add(idCategoriaParteAutor);
		}

		for (Long idCategoriaParteReu : criaListaDeReusSelecionados()) {
			listaIntimados.add(idCategoriaParteReu);
		}

		return listaIntimados;
	}

	private List<Long> criaListaPartesSelecionadas() {
		CategoriaService categoriaService = getCategoriaService();
		List<Categoria> listaCategoria = new ArrayList<Categoria>();
		try {
			listaCategoria = categoriaService.pesquisar(null, null, true);
		} catch (ServiceException e) {
			reportarErro("Erro ao busca a lista de categorias.");
		}

		List<Long> listaIds = new ArrayList<Long>();

		for (Categoria ca : listaCategoria) {
			listaIds.add(ca.getId());
		}

		return listaIds;
	}

	/**
	 * Recupera as chaves do mapa de tags livres encontradas no modelo
	 * 
	 * @author ViniciusK
	 */
	public List<String> getListaDeTags() {
		return new ArrayList<String>(conjuntoTags.keySet());
	}

	/**
	 * Grava o(s) código(s) fixo(s) e o(s) valor(es) digitados pelo usuário no conjunto "conjuntoTagsLivres" para ser passado para o montadorTexto
	 */
	public void gravarValoresDasTags() {

		conjuntoTagsLivres = new HashMap<String, String>();

		for (String chave : conjuntoTags.keySet()) {
			conjuntoTagsLivres.put(chave, conjuntoTags.get(chave));
		}
	}

	/**
	 * Seta o valor do rendered para validar se a tela deverá aparecer com os campos livres de preenchimento
	 * 
	 * @param valorRenderedDaTela
	 * @author ViniciusK
	 */
	public void preencheValorSeExisteTagNoDocumento(Boolean valorRenderedDaTela) {
		setRenderedTelaDeTagsLivreDePreenchimento(valorRenderedDaTela);
	}

	@SuppressWarnings({ "rawtypes"})
	public List pesquisarIncidentesLotes(Object value) {
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

					processoLote = getProcessoService().recuperarProcesso(sigla, lNumero);
					setNumeroProcessoPesqLote(lNumero);
					setSiglaClassePesqLote(sigla);

					if (processoLote != null) {
						incidentes = recuperarIncidentes(processoLote.getId());
					}
				}
			} catch (NumberFormatException e) {
				reportarErro("Número de processo inválido: " + siglaNumero, e, LOG);
			} catch (ServiceException e) {
				reportarErro("Erro na pequisa do processo: " + siglaNumero, e, LOG);
			}
		}

		atualizaSessao();
		return incidentes;
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

					setListaVinculadaPecaAoDocumento(null);
					setListaPecaProcessoEletronicoVinculadas(null);
					
					processo = getProcessoService().recuperarProcesso(sigla, lNumero);
					setNumeroProcessoPesqPrincipal(lNumero);
					setSiglaClassePesqPrincipal(sigla);

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

	public void preencheDadosProcessos(String siglaNumeroProcesso){
		
		try{
			String sigla = ProcessoParser.getSigla(siglaNumeroProcesso);
			Long lNumero = ProcessoParser.getNumero(siglaNumeroProcesso);
			
			if (StringUtils.isNotVazia(sigla) && lNumero != null) {
				sigla = converterClasse(sigla, classes);
			
				processo = getProcessoService().recuperarProcesso(sigla, lNumero);
				setNumeroProcessoPesqPrincipal(lNumero);
				setSiglaClassePesqPrincipal(sigla);
			}
			
		} catch (NumberFormatException e) {
			reportarErro("Número de processo inválido: " + siglaNumeroProcesso );
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar os incidentes do processo " + siglaNumeroProcesso );
		}
		
		atualizaSessao();
		
	}


	public String getDocumentoDownloadURL() {
		CheckableDataTableRowWrapper wrapper = (CheckableDataTableRowWrapper) tabelaPecaProcessoEletronico.getRowData();
		ArquivoProcessoEletronico arquivo = (ArquivoProcessoEletronico) wrapper.getWrappedObject();
		String url = montaUrlDownload(arquivo);
		return url;
	}

	// -------------------- GETTERS & SETTERS --------------------

	public String getTipoJulgamentoTexto() {
		return tipoJulgamentoTexto;
	}

	public void setTipoJulgamentoTexto(String tipoJulgamentoTexto) {
		this.tipoJulgamentoTexto = tipoJulgamentoTexto;
	}

	public Long getCodigoRecursoTexto() {
		return codigoRecursoTexto;
	}

	public void setCodigoRecursoTexto(Long codigoRecursoTexto) {
		this.codigoRecursoTexto = codigoRecursoTexto;
	}

	public Long getCodigoMinistroTexto() {
		return codigoMinistroTexto;
	}

	public void setCodigoMinistroTexto(Long codigoMinistroTexto) {
		this.codigoMinistroTexto = codigoMinistroTexto;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaDocumentos() {
		return tabelaDocumentos;
	}

	public void setTabelaDocumentos(org.richfaces.component.html.HtmlDataTable tabelaDocumentos) {
		this.tabelaDocumentos = tabelaDocumentos;
	}

	public List<CheckableDataTableRowWrapper> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<CheckableDataTableRowWrapper> listaDocumentos) {
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		this.listaDocumentos = listaDocumentos;
	}

	public List<SelectItem> getListaRecursosProcesso() {
		return listaRecursosProcesso;
	}

	public void setListaRecursosProcesso(List<SelectItem> listaRecursosProcesso) {
		setAtributo(KEY_LISTA_RECURSOS, listaRecursosProcesso);
		this.listaRecursosProcesso = listaRecursosProcesso;
	}

	public String getObservacaoTexto() {
		return observacaoTexto;
	}

	public void setObservacaoTexto(String observacaoTexto) {
		this.observacaoTexto = observacaoTexto;
	}

	public Comunicacao getDocumentoEditar() {
		return documentoEditar;
	}

	public void setDocumentoEditar(Comunicacao documentoEditar) {
		setAtributo(KEY_DOCUMENTO_EDITAR, documentoEditar);
		this.documentoEditar = documentoEditar;
	}

	public Long getCodigoMinistroEditar() {
		return codigoMinistroEditar;
	}

	public void setCodigoMinistroEditar(Long codigoMinistroEditar) {
		this.codigoMinistroEditar = codigoMinistroEditar;
	}

	public String getNomeDocumentoEditar() {
		return nomeDocumentoEditar;
	}

	public void setNomeDocumentoEditar(String nomeDocumentoEditar) {
		this.nomeDocumentoEditar = nomeDocumentoEditar;
	}

	public ObjetoIncidente<?> getObjetoIncidenteProcurado() {
		return objetoIncidenteProcurado;
	}

	public void setObjetoIncidenteProcurado(ObjetoIncidente<?> objetoIncidenteProcurado) {
		this.objetoIncidenteProcurado = objetoIncidenteProcurado;
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public String getSiglaNumeroProcesso() {
		return siglaNumeroProcesso;
	}

	public void setSiglaNumeroProcesso(String siglaNumeroProcesso) {
		this.siglaNumeroProcesso = siglaNumeroProcesso;
	}

	public Boolean getRenderizaNovoTexto() {
		return renderizaNovoTexto;
	}

	public void setRenderizaNovoTexto(Boolean renderizaNovoTexto) {
		this.renderizaNovoTexto = renderizaNovoTexto;
	}

	public List<SelectItem> getItensTiposPermissoes() {
		return itensTiposPermissoes;
	}

	public void setItensTiposPermissoes(List<SelectItem> itensTiposPermissoes) {
		this.itensTiposPermissoes = itensTiposPermissoes;
	}

	public List<SelectItem> getItensTipoModelos() {
		return itensTipoModelos;
	}

	public void setItensTipoModelos(List<SelectItem> itensTipoModelos) {
		this.itensTipoModelos = itensTipoModelos;
	}

	public Long getCodigoModelo() {
		return codigoModelo;
	}

	public void setCodigoModelo(Long codigoModelo) {
		this.codigoModelo = codigoModelo;
	}

	public Long getCodigoTipoModelo() {
		return codigoTipoModelo;
	}

	public void setCodigoTipoModelo(Long codigoTipoModelo) {
		this.codigoTipoModelo = codigoTipoModelo;
	}

	public Long getCodigoTipoPermissao() {
		return codigoTipoPermissao;
	}

	public void setCodigoTipoPermissao(Long codigoTipoPermissao) {
		this.codigoTipoPermissao = codigoTipoPermissao;
	}

	public List<SelectItem> getItensModelos() {
		return itensModelos;
	}

	public void setItensModelos(List<SelectItem> itensModelos) {
		this.itensModelos = itensModelos;
	}

	public String getNomeDoDocumento() {
		return nomeDoDocumento;
	}

	public void setNomeDoDocumento(String nomeDoDocumento) {
		this.nomeDoDocumento = nomeDoDocumento;
	}

	public Long getObjetoIncidenteModal() {
		return objetoIncidenteModal;
	}

	public void setObjetoIncidenteModal(Long objetoIncidenteModal) {
		this.objetoIncidenteModal = objetoIncidenteModal;
	}

	public Long getCodigoModeloPesq() {
		return codigoModeloPesq;
	}

	public void setCodigoModeloPesq(Long codigoModeloPesq) {
		this.codigoModeloPesq = codigoModeloPesq;
	}

	public List<SelectItem> getItensModelosPesq() {
		return itensModelosPesq;
	}

	public void setItensModelosPesq(List<SelectItem> itensModelosPesq) {
		this.itensModelosPesq = itensModelosPesq;
	}

	public String getAnotacaoCancelamento() {
		return anotacaoCancelamento;
	}

	public void setAnotacaoCancelamento(String anotacaoCancelamento) {
		this.anotacaoCancelamento = anotacaoCancelamento;
	}

	public Long getIdSetorDestino() {
		return idSetorDestino;
	}

	public void setIdSetorDestino(Long idSetorDestino) {
		this.idSetorDestino = idSetorDestino;
	}

	public List<SelectItem> getItensSetoresDestino() {
		return itensSetoresDestino;
	}

	public void setItensSetoresDestino(List<SelectItem> itensSetoresDestino) {
		this.itensSetoresDestino = itensSetoresDestino;
	}

	public ModeloComunicacao getModeloComunicacao() {
		return modeloComunicacao;
	}

	public void setModeloComunicacao(ModeloComunicacao modeloComunicacao) {
		this.modeloComunicacao = modeloComunicacao;
	}

	public Boolean getRenderedTelaDeTagsLivreDePreenchimento() {
		return renderedTelaDeTagsLivreDePreenchimento;
	}

	public void setRenderedTelaDeTagsLivreDePreenchimento(Boolean renderedTelaDeTagsLivreDePreenchimento) {
		this.renderedTelaDeTagsLivreDePreenchimento = renderedTelaDeTagsLivreDePreenchimento;
	}

	public Map<String, String> getConjuntoTagsLivres() {
		return conjuntoTagsLivres;
	}

	public void setConjuntoTagsLivres(Map<String, String> conjuntoTagsLivres) {
		this.conjuntoTagsLivres = conjuntoTagsLivres;
	}

	public SortedMap<String, String> getConjuntoTags() {
		return conjuntoTags;
	}

	public void setConjuntoTags(SortedMap<String, String> conjuntoTags) {
		this.conjuntoTags = conjuntoTags;
	}

	public Long getObjetoIncidenteNovo() {
		return objetoIncidenteNovo;
	}

	public void setObjetoIncidenteNovo(Long objetoIncidenteNovo) {
		this.objetoIncidenteNovo = objetoIncidenteNovo;
	}

	public Boolean getRenderedBotaoNovoTexto() {
		return renderedBotaoNovoTexto;
	}

	public void setRenderedBotaoNovoTexto(Boolean renderedBotaoNovoTexto) {
		this.renderedBotaoNovoTexto = renderedBotaoNovoTexto;
	}

	public Boolean getCheckpesquisaAutor() {
		return checkpesquisaAutor;
	}

	public void setCheckpesquisaAutor(Boolean checkpesquisaAutor) {
		this.checkpesquisaAutor = checkpesquisaAutor;
	}

	public Boolean getRenderedComboAutores() {
		return renderedComboAutores;
	}

	public void setRenderedComboAutores(Boolean renderedComboAutores) {
		this.renderedComboAutores = renderedComboAutores;
	}

	public Boolean getRenderedVincularPecaAoDocumento() {
		return renderedVincularPecaAoDocumento;
	}

	public void setRenderedVincularPecaAoDocumento(Boolean renderedVincularPecaAoDocumento) {
		this.renderedVincularPecaAoDocumento = renderedVincularPecaAoDocumento;
	}

	public List<CheckableDataTableRowWrapper> getListaPecaProcessoEletronico() {
		return listaPecaProcessoEletronico;
	}

	public void setListaPecaProcessoEletronico(List<CheckableDataTableRowWrapper> listaPecaProcessoEletronico) {
		setAtributo(KEY_LISTA_PECA_PROCESSO_ELET, listaPecaProcessoEletronico);
		this.listaPecaProcessoEletronico = listaPecaProcessoEletronico;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaPecaProcessoEletronico() {
		return tabelaPecaProcessoEletronico;
	}

	public void setTabelaPecaProcessoEletronico(org.richfaces.component.html.HtmlDataTable tabelaPecaProcessoEletronico) {
		this.tabelaPecaProcessoEletronico = tabelaPecaProcessoEletronico;
	}

	public List<ArquivoProcessoEletronico> getListaVinculadaPecaAoDocumento() {
		return listaVinculadaPecaAoDocumento;
	}

	public void setListaVinculadaPecaAoDocumento(List<ArquivoProcessoEletronico> listaVinculadaPecaAoDocumento) {
		this.listaVinculadaPecaAoDocumento = listaVinculadaPecaAoDocumento;
	}

	public Map<String, String> getConjuntoPartesSelecionadas() {
		return conjuntoPartesSelecionadas;
	}

	public void setConjuntoPartesSelecionadas(Map<String, String> conjuntoPartesSelecionadas) {
		this.conjuntoPartesSelecionadas = conjuntoPartesSelecionadas;
	}

	public String getNomeCategoriaAutor() {
		return nomeCategoriaAutor;
	}

	public void setNomeCategoriaAutor(String nomeCategoriaAutor) {
		this.nomeCategoriaAutor = nomeCategoriaAutor;
	}

	public List<SelectItem> getItensCategoriaAutor() {
		return itensCategoriaAutor;
	}

	public void setItensCategoriaAutor(List<SelectItem> itensCategoriaAutor) {
		this.itensCategoriaAutor = itensCategoriaAutor;
	}

	public String getNomeCategoriaReu() {
		return nomeCategoriaReu;
	}

	public void setNomeCategoriaReu(String nomeCategoriaReu) {
		this.nomeCategoriaReu = nomeCategoriaReu;
	}

	public List<SelectItem> getItensCategoriaReu() {
		return itensCategoriaReu;
	}

	public void setItensCategoriaReu(List<SelectItem> itensCategoriaReu) {
		this.itensCategoriaReu = itensCategoriaReu;
	}

	public Boolean getRenderedComboReus() {
		return renderedComboReus;
	}

	public void setRenderedComboReus(Boolean renderedComboReus) {
		this.renderedComboReus = renderedComboReus;
	}

	public Boolean getRenderedVincularPecaNovamenteDocumento() {
		return renderedVincularPecaNovamenteDocumento;
	}

	public void setRenderedVincularPecaNovamenteDocumento(Boolean renderedVincularPecaNovamenteDocumento) {
		this.renderedVincularPecaNovamenteDocumento = renderedVincularPecaNovamenteDocumento;
	}

	public String getNomeCategoriaIntimando() {
		return nomeCategoriaIntimando;
	}

	public void setNomeCategoriaIntimando(String nomeCategoriaIntimando) {
		this.nomeCategoriaIntimando = nomeCategoriaIntimando;
	}

	public List<SelectItem> getItensCategoriaIntimando() {
		return itensCategoriaIntimando;
	}

	public void setItensCategoriaIntimando(List<SelectItem> itensCategoriaIntimando) {
		this.itensCategoriaIntimando = itensCategoriaIntimando;
	}

	public Boolean getRenderedComboIntimandos() {
		return renderedComboIntimandos;
	}

	public void setRenderedComboIntimandos(Boolean renderedComboIntimandos) {
		this.renderedComboIntimandos = renderedComboIntimandos;
	}

	public String getNomeCategoriaCitanda() {
		return nomeCategoriaCitanda;
	}

	public void setNomeCategoriaCitanda(String nomeCategoriaCitanda) {
		this.nomeCategoriaCitanda = nomeCategoriaCitanda;
	}

	public Boolean getRenderedComboCitanda() {
		return renderedComboCitanda;
	}

	public void setRenderedComboCitanda(Boolean renderedComboCitanda) {
		this.renderedComboCitanda = renderedComboCitanda;
	}

	public List<SelectItem> getItensCategoriaCitanda() {
		return itensCategoriaCitanda;
	}

	public void setItensCategoriaCitanda(List<SelectItem> itensCategoriaCitanda) {
		this.itensCategoriaCitanda = itensCategoriaCitanda;
	}

	public Boolean getRenderedBotaoVincularPeca() {
		return renderedBotaoVincularPeca;
	}

	public void setRenderedBotaoVincularPeca(Boolean renderedBotaoVincularPeca) {
		this.renderedBotaoVincularPeca = renderedBotaoVincularPeca;
	}

	public Boolean getRenderedAdicionaIntimando() {
		return renderedAdicionaIntimando;
	}

	public void setRenderedAdicionaIntimando(Boolean renderedAdicionaIntimando) {
		this.renderedAdicionaIntimando = renderedAdicionaIntimando;
	}

	public Boolean getRenderedTelaDePartesSelecionadas() {
		return renderedTelaDePartesSelecionadas;
	}

	public void setRenderedTelaDePartesSelecionadas(Boolean renderedTelaDePartesSelecionadas) {
		this.renderedTelaDePartesSelecionadas = renderedTelaDePartesSelecionadas;
	}

	public String getNomeIntimandoAdicionado() {
		return nomeIntimandoAdicionado;
	}

	public void setNomeIntimandoAdicionado(String nomeIntimandoAdicionado) {
		this.nomeIntimandoAdicionado = nomeIntimandoAdicionado;
	}

	public Boolean getRenderedBotaoAdicionarIntimando() {
		return renderedBotaoAdicionarIntimando;
	}

	public void setRenderedBotaoAdicionarIntimando(Boolean renderedBotaoAdicionarIntimando) {
		this.renderedBotaoAdicionarIntimando = renderedBotaoAdicionarIntimando;
	}

	public String getNomeCategoriaAdvogado() {
		return nomeCategoriaAdvogado;
	}

	public void setNomeCategoriaAdvogado(String nomeCategoriaAdvogado) {
		this.nomeCategoriaAdvogado = nomeCategoriaAdvogado;
	}

	public List<SelectItem> getItensCategoriaAdvogado() {
		return itensCategoriaAdvogado;
	}

	public void setItensCategoriaAdvogado(List<SelectItem> itensCategoriaAdvogado) {
		this.itensCategoriaAdvogado = itensCategoriaAdvogado;
	}

	public Boolean getRenderedComboAdvogados() {
		return renderedComboAdvogados;
	}

	public void setRenderedComboAdvogados(Boolean renderedComboAdvogados) {
		this.renderedComboAdvogados = renderedComboAdvogados;
	}

	public String getNomeCategoriaParte() {
		return nomeCategoriaParte;
	}

	public void setNomeCategoriaParte(String nomeCategoriaParte) {
		this.nomeCategoriaParte = nomeCategoriaParte;
	}

	public Boolean getRenderedComboParte() {
		return renderedComboParte;
	}

	public void setRenderedComboParte(Boolean renderedComboParte) {
		this.renderedComboParte = renderedComboParte;
	}

	public List<SelectItem> getItensCategoriaParte() {
		return itensCategoriaParte;
	}

	public void setItensCategoriaParte(List<SelectItem> itensCategoriaParte) {
		this.itensCategoriaParte = itensCategoriaParte;
	}

	public String getNomeReuAdicionado() {
		return nomeReuAdicionado;
	}

	public void setNomeReuAdicionado(String nomeReuAdicionado) {
		this.nomeReuAdicionado = nomeReuAdicionado;
	}

	public Boolean getRenderedBotaoAdicionarReu() {
		return renderedBotaoAdicionarReu;
	}

	public void setRenderedBotaoAdicionarReu(Boolean renderedBotaoAdicionarReu) {
		this.renderedBotaoAdicionarReu = renderedBotaoAdicionarReu;
	}

	public Boolean getRenderedAdicionaReu() {
		return renderedAdicionaReu;
	}

	public void setRenderedAdicionaReu(Boolean renderedAdicionaReu) {
		this.renderedAdicionaReu = renderedAdicionaReu;
	}

	public String getNomeCitandoAdicionado() {
		return nomeCitandoAdicionado;
	}

	public void setNomeCitandoAdicionado(String nomeCitandoAdicionado) {
		this.nomeCitandoAdicionado = nomeCitandoAdicionado;
	}

	public Boolean getRenderedAdicionaCitando() {
		return renderedAdicionaCitando;
	}

	public void setRenderedAdicionaCitando(Boolean renderedAdicionaCitando) {
		this.renderedAdicionaCitando = renderedAdicionaCitando;
	}

	public Boolean getRenderedBotaoAdicionarCitando() {
		return renderedBotaoAdicionarCitando;
	}

	public void setRenderedBotaoAdicionarCitando(Boolean renderedBotaoAdicionarCitando) {
		this.renderedBotaoAdicionarCitando = renderedBotaoAdicionarCitando;
	}

	public Boolean getRenderedListaPecasVinculadas() {
		return renderedListaPecasVinculadas;
	}

	public void setRenderedListaPecasVinculadas(Boolean renderedListaPecasVinculadas) {
		this.renderedListaPecasVinculadas = renderedListaPecasVinculadas;
	}

	public List<CheckableDataTableRowWrapper> getListaPecaProcessoEletronicoVinculadas() {
		return listaPecaProcessoEletronicoVinculadas;
	}

	public void setListaPecaProcessoEletronicoVinculadas(List<CheckableDataTableRowWrapper> listaPecaProcessoEletronicoVinculadas) {
		setAtributo(KEY_LISTA_PECA_PROCESSO_ELET_VINCULADAS, listaPecaProcessoEletronicoVinculadas);
		this.listaPecaProcessoEletronicoVinculadas = listaPecaProcessoEletronicoVinculadas;
	}

	public HtmlDataTable getTabelaPecaProcessoEletronicoVinculadas() {
		return tabelaPecaProcessoEletronicoVinculadas;
	}

	public void setTabelaPecaProcessoEletronicoVinculadas(HtmlDataTable tabelaPecaProcessoEletronicoVinculadas) {
		this.tabelaPecaProcessoEletronicoVinculadas = tabelaPecaProcessoEletronicoVinculadas;
	}

	public ComunicacaoDocumento getComDocumentoTemporariaRowData() {
		return comDocumentoTemporariaRowData;
	}

	public void setComDocumentoTemporariaRowData(ComunicacaoDocumento comDocumentoTemporariaRowData) {
		this.comDocumentoTemporariaRowData = comDocumentoTemporariaRowData;
	}

	public List<SelectItem> getItensProcessoEmLote() {
		return itensProcessoEmLote;
	}

	public void setItensProcessoEmLote(List<SelectItem> itensProcessoEmLote) {
		this.itensProcessoEmLote = itensProcessoEmLote;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Long getNumeroProcessoPesqPrincipal() {
		return numeroProcessoPesqPrincipal;
	}

	public void setNumeroProcessoPesqPrincipal(Long numeroProcessoPesqPrincipal) {
		this.numeroProcessoPesqPrincipal = numeroProcessoPesqPrincipal;
	}

	public String getSiglaClassePesqPrincipal() {
		return siglaClassePesqPrincipal;
	}

	public void setSiglaClassePesqPrincipal(String siglaClassePesqPrincipal) {
		this.siglaClassePesqPrincipal = siglaClassePesqPrincipal;
	}

	public List<ObjetoIncidente<?>> getListaObjetoIncidenteLote() {
		return listaObjetoIncidenteLote;
	}

	public void setListaObjetoIncidenteLote(List<ObjetoIncidente<?>> listaObjetoIncidenteLote) {
		this.listaObjetoIncidenteLote = listaObjetoIncidenteLote;
	}

	public Boolean getRenderedComponentesAlteracao() {
		return renderedComponentesAlteracao;
	}

	public void setRenderedComponentesAlteracao(Boolean renderedComponentesAlteracao) {
		this.renderedComponentesAlteracao = renderedComponentesAlteracao;
	}

	public Long getIdProcessoLoteObjetoIncidente() {
		return idProcessoLoteObjetoIncidente;
	}

	public void setIdProcessoLoteObjetoIncidente(Long idProcessoLoteObjetoIncidente) {
		this.idProcessoLoteObjetoIncidente = idProcessoLoteObjetoIncidente;
	}

	public Boolean getRenderedTelaProcessoLote() {
		return renderedTelaProcessoLote;
	}

	public void setRenderedTelaProcessoLote(Boolean renderedTelaProcessoLote) {
		this.renderedTelaProcessoLote = renderedTelaProcessoLote;
	}

	public Long getObjetoIncidenteLote() {
		return objetoIncidenteLote;
	}

	public void setObjetoIncidenteLote(Long objetoIncidenteLote) {
		this.objetoIncidenteLote = objetoIncidenteLote;
	}

	public Processo getProcessoLote() {
		return processoLote;
	}

	public void setProcessoLote(Processo processoLote) {
		this.processoLote = processoLote;
	}

	public Long getNumeroProcessoPesqLote() {
		return numeroProcessoPesqLote;
	}

	public void setNumeroProcessoPesqLote(Long numeroProcessoPesqLote) {
		this.numeroProcessoPesqLote = numeroProcessoPesqLote;
	}

	public String getSiglaClassePesqLote() {
		return siglaClassePesqLote;
	}

	public void setSiglaClassePesqLote(String siglaClassePesqLote) {
		this.siglaClassePesqLote = siglaClassePesqLote;
	}

	public String getSiglaNumeroProcessoLote() {
		return siglaNumeroProcessoLote;
	}

	public void setSiglaNumeroProcessoLote(String siglaNumeroProcessoLote) {
		this.siglaNumeroProcessoLote = siglaNumeroProcessoLote;
	}

	public Boolean getRenderedDesabilitaMenuModelo() {
		return renderedDesabilitaMenuModelo;
	}

	public void setRenderedDesabilitaMenuModelo(Boolean renderedDesabilitaMenuModelo) {
		this.renderedDesabilitaMenuModelo = renderedDesabilitaMenuModelo;
	}

	public List<ArquivoProcessoEletronico> getListaPecasProcessoLote() {
		return listaPecasProcessoLote;
	}

	public void setListaPecasProcessoLote(List<ArquivoProcessoEletronico> listaPecasProcessoLote) {
		this.listaPecasProcessoLote = listaPecasProcessoLote;
	}

	public Boolean getRenderedTransObjetoIncidente() {
		return renderedTransObjetoIncidente;
	}

	public void setRenderedTransObjetoIncidente(Boolean renderedTransObjetoIncidente) {
		this.renderedTransObjetoIncidente = renderedTransObjetoIncidente;
	}

	public String getObsDocumento() {
		return obsDocumento;
	}

	public void setObsDocumento(String obsDocumento) {
		this.obsDocumento = obsDocumento;
	}

	public String getObservacaoComunicacaoAlterada() {
		return observacaoComunicacaoAlterada;
	}

	public void setObservacaoComunicacaoAlterada(
			String observacaoComunicacaoAlterada) {
		this.observacaoComunicacaoAlterada = observacaoComunicacaoAlterada;
	}

	public Boolean getRenderedAlteraObsAndamento() {
		return renderedAlteraObsAndamento;
	}

	public void setRenderedAlteraObsAndamento(Boolean renderedAlteraObsAndamento) {
		this.renderedAlteraObsAndamento = renderedAlteraObsAndamento;
	}

	public String getObservacaoAndamento() {
		return observacaoAndamento;
	}

	public void setObservacaoAndamento(String observacaoAndamento) {
		this.observacaoAndamento = observacaoAndamento;
	}
	
	public String getRecuperarTextoDocumento(){		
		return getRecuperarTextoDocumento(tabelaDocumentos);
		
	}

	
	
}
