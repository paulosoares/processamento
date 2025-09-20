package br.gov.stf.estf.assinatura.visao.jsf.beans.usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.richfaces.component.UITree;
import org.richfaces.component.html.HtmlDataTable;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.assinardocumentos.BeanAssinarDocumentos;
import br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.manterdocumentos.BeanManterDocumentos;
import br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento.BeanRegistrarAndamento;
import br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento.BeanRemeterDocumento;
import br.gov.stf.estf.assinatura.visao.util.ItemControleSearchData;
import br.gov.stf.estf.assinatura.visao.util.OrdenacaoUtils;
import br.gov.stf.estf.assinatura.visao.util.TreeBuilder;
import br.gov.stf.estf.assinatura.visao.util.TreeBuilder.NoGenericoUsuario;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.processostf.ItemControle;
import br.gov.stf.estf.entidade.processostf.PrescricaoReu;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ReferenciaPrescricao;
import br.gov.stf.estf.entidade.processostf.TipoControle;
import br.gov.stf.estf.entidade.processostf.TipoSituacaoControle;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.processostf.model.util.ItemControleResult;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.security.AcegiSecurityUtils;
import br.gov.stf.framework.util.Constants;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanUsuario extends AssinadorBaseBean {

	private static final long serialVersionUID = 882460254456920404L;

	private static final Log LOG = LogFactory.getLog(BeanUsuario.class);

	private static final Object NOME_TABELA_CONTROLE = new Object();
	private static final Object CHAVE_CONTROLE = new Object();
	private static final Object LISTA_ITENS_CONTROLE = new Object();
	private static final Object LISTA_CONTROLE = new Object();
	private static final Object LISTA_TIPO_CONTROLE = new Object();
	private static final Object LISTA_USUARIO_CONTROLE = new Object();
	private static final Object LISTA_SETORES_GABIENTES = new Object();
	private static final Object ITENS_CONTROLE = new Object();
	private static final Object ITENS_CONTROLE_ORIGINAL = new Object();
	private static final Object RENDERIZA_ITENS_CONTROLE = new Object();
	private static final Object RENDERIZA_ARV_CONTROLE = new Object();
	private static final Object RENDERED_PANEL_CONTROLES = new Object();
	private static final Object RENDERED_ARVORE_USUARIOS = new Object();
	private static final Object CODIGO_SETOR_ATUAL = new Object();

	private static final Object ARVORE_CONTROLE = new Object();
	private static final Object ARVORE_EXPEDIENTE_EXPEDIDO = new Object();
	private static final Object ARVORE_ITEN_USUARIO = new Object();

	public static final String RS_ADM_ASSINADOR = "RS_ADM_ASSINADOR";
	public static final String RS_MASTER_PROCESSAMENTO = "RS_MASTER_PROCESSAMENTO";
	public static final String RS_EDICAO_MODELOS_INSTITUCIONAIS = "RS_EDICAO_MODELOS_INSTITUCIONAIS";
	public static final String RS_EDICAO_MODELOS = "RS_EDICAO_MODELOS";
	public static final String RS_EDICAO_TEXTOS = "RS_EDICAO_TEXTOS";
	public static final String RS_ELABORACAO_TEXTOS = "RS_ELABORACAO_TEXTOS";
	public static final String RS_GESTAO_TEXTOS = "RS_GESTAO_TEXTOS";
	public static final String RS_REVISAO_TEXTOS = "RS_REVISAO_TEXTOS";
	public static final String RS_ASSINATURA_TEXTOS = "RS_ASSINATURA_TEXTOS";
	public static final String RS_ACOMPANHAMENTO_TEXTOS = "RS_ACOMPANHAMENTO_TEXTOS";
	public static final String RS_EXPEDICAO_TEXTOS = "RS_EXPEDICAO_TEXTOS";
	public static final String RS_FINALIZAR_TEXTOS = "RS_FINALIZAR_TEXTOS";
	public static final String RS_CONSULTA_TEXTOS = "RS_CONSULTA_TEXTOS";
	public static final String RS_CONSULTA_TEXTOS_ASSINADOS = "RS_CONSULTA_TEXTOS_ASSINADOS";
	public static final String RS_PENDENTE_PUBLICA = "RS_PENDENTE_PUBLICA";
	public static final String RS_PUBLICA_PENDENTE = "RS_PUBLICA_PENDENTE";
	public static final String RS_ADM_PECAS = "RS_ADM_PECAS";
	public static final String RS_GABINETE_SEJ = "RS_GABINETE_SEJ";
	public static final String RS_MANTER_CONTROLE_PROC = "RS_MANTER_CONTROLE_PROCESSAMENTO";
	public static final String RS_MANTER_GRUPO_USUARIOS = "RS_MANTER_GRUPO_USUARIOS";
	public static final String RS_EDITAR_ANDAMENTO_PROCESSO = "RS_EDITAR_ANDAMENTO_PROCESSO";
	public static final String RS_REGISTRAR_ANDAMENTO_DISTRIBUIDO_FORA_DO_SETOR = "RS_REGISTRAR_ANDAMENTO_DISTRIBUIDO_FORA_DO_SETOR";
	public static final String RS_REGISTRAR_ANDAMENTO_NAO_DISTRIBUIDO = "RS_REGISTRAR_ANDAMENTO_NAO_DISTRIBUIDO";
	public static final String RS_REGISTRAR_ANDAMENTO_INDEVIDO = "RS_REGISTRAR_ANDAMENTO_INDEVIDO";
	public static final String RS_REGISTRAR_ANDAMENTO_INDEVIDO_RG = "RS_REGISTRAR_ANDAMENTO_INDEVIDO_RG";
	public static final String RS_AUTORIZAR_BAIXA = "RS_AUTORIZAR_BAIXA";
	public static final String RS_PERIODO_EXCLUSAO_MINISTRO = "RS_PERIODO_EXCLUSAO_MINISTRO";
	public static final String RS_EXPEDICAO_CONSULTA = "RS_EXPEDICAO_CONSULTA";
	public static final String RS_EXPEDICAO_REMESSA = "RS_EXPEDICAO_REMESSA";
	public static final String RS_EXPEDICAO_ADM = "RS_EXPEDICAO_ADM";
	

	// #################### DECLARAÇÃO VARIÁVEIS #########################

	private UsuarioAssinatura usuario;
	private Long setorAtual;
	private Long chaveControle;
	private String chaveControleUsuario;
	private Long chaveControleExpedienteExpedidos;
	private String idUsuarioContr;
	private String nomeControleTabela;

	private Boolean exibirListaSetores = Boolean.FALSE;

	private Boolean renderedItensControle;
	private Boolean exibePainelDeControles;
	private Boolean renderedArvoreUsuarios;
	private Boolean renderedArvoreControle;

	private Integer qtdeDocumentosAssinar;
	private Integer qtdeDocumentosEnviar;
	private Integer qtdeDocumentosAssinados;
	private Integer qtdeDocumentosCorrecao;
	private Integer qtdeDocumentosExpedicao;

	MinistroPresidente ministroPresidente = null;

	private Map<Long, String> controles = new HashMap<Long, String>();
	// private Set<Map.Entry<Integer, String>> acoes = new HashSet<Map.Entry<Integer, String>>();
	private HtmlDataTable tabelaItensControle;
	private SortedMap<TipoControle, List<ItemControleResult>> itensControlesUsuario;
	private SortedMap<TipoControle, List<ItemControleResult>> itensControlesUsuarioOriginal;
	private List<CheckableDataTableRowWrapper> listaItensControle;
	private List<SelectItem> listaSetoresGabinetes;
	private List<SelectItem> listaUsuariosControles;
	private List<Long> listaTesteTipoControles;
	private List<PrescricaoReu> prescricaoReu;

	private TreeNode arvoreControle;
	private TreeNode arvoreExpedienteExpedido;
	private TreeNode arvoreItensUsuario;
	private boolean dadosCarregados;
	// ###################################################################

	public BeanUsuario() {
		//restaurarSessao();
		dadosCarregados = false;
	}

	public boolean isCarregaDados() {
		if (!dadosCarregados) {
			restaurarSessao();
		}
		return true;
	}

	public void restaurarSessao() {
		dadosCarregados = true;

		usuario = (UsuarioAssinatura) getUser();

		if (usuario.getAtivo()) {
			setSessionValue(Constants.SECURITY_ENABLED_KEY, Boolean.TRUE);
			setSessionValue(Constants.USER_KEY, usuario);
		}

		if (getAtributo(LISTA_CONTROLE) == null) {
			setAtributo(LISTA_CONTROLE, new HashMap<Long, String>());
		}
		setControles((Map<Long, String>) getAtributo(LISTA_CONTROLE));

		if (getAtributo(ARVORE_CONTROLE) == null) {
			setAtributo(ARVORE_CONTROLE, new TreeNodeImpl());
		}
		setArvoreControle((TreeNode) getAtributo(ARVORE_CONTROLE));

		if (getAtributo(ARVORE_EXPEDIENTE_EXPEDIDO) == null) {
			setAtributo(ARVORE_EXPEDIENTE_EXPEDIDO, new TreeNodeImpl());
		}
		setArvoreExpedienteExpedido((TreeNode) getAtributo(ARVORE_EXPEDIENTE_EXPEDIDO));

		if (getAtributo(ARVORE_ITEN_USUARIO) == null) {
			setAtributo(ARVORE_ITEN_USUARIO, new TreeNodeImpl());
		}
		setArvoreItensUsuario((TreeNode) getAtributo(ARVORE_ITEN_USUARIO));

		if (getAtributo(CODIGO_SETOR_ATUAL) == null) {
			setAtributo(CODIGO_SETOR_ATUAL, usuario.getSetor().getId());
		}
		setSetorAtual((Long) getAtributo(CODIGO_SETOR_ATUAL));

		if (getAtributo(LISTA_ITENS_CONTROLE) == null) {
			setAtributo(LISTA_ITENS_CONTROLE, new ArrayList<ItemControle>());
		}
		setListaItensControle((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_ITENS_CONTROLE));

		if (getAtributo(LISTA_TIPO_CONTROLE) == null) {
			setAtributo(LISTA_TIPO_CONTROLE, new ArrayList<Long>());
		}
		setListaTesteTipoControles((List<Long>) getAtributo(LISTA_TIPO_CONTROLE));

		if (getAtributo(LISTA_USUARIO_CONTROLE) == null) {
			setAtributo(LISTA_USUARIO_CONTROLE, new LinkedList<SelectItem>());
		}
		setListaUsuariosControles((List<SelectItem>) getAtributo(LISTA_USUARIO_CONTROLE));

		if (getAtributo(LISTA_SETORES_GABIENTES) == null) {
			setAtributo(LISTA_SETORES_GABIENTES, carregarComboSetores());
		}
		setListaSetoresGabinetes((List<SelectItem>) getAtributo(LISTA_SETORES_GABIENTES));

		if (getAtributo(RENDERIZA_ITENS_CONTROLE) == null) {
			setAtributo(RENDERIZA_ITENS_CONTROLE, false);
		}
		setRenderedItensControle((Boolean) getAtributo(RENDERIZA_ITENS_CONTROLE));

		if (getAtributo(RENDERED_ARVORE_USUARIOS) == null) {
			setAtributo(RENDERED_ARVORE_USUARIOS, false);
		}
		setRenderedArvoreUsuarios((Boolean) getAtributo(RENDERED_ARVORE_USUARIOS));

		if (getAtributo(RENDERIZA_ARV_CONTROLE) == null) {
			setAtributo(RENDERIZA_ARV_CONTROLE, false);
		}
		setRenderedArvoreControle((Boolean) getAtributo(RENDERIZA_ARV_CONTROLE));

		if (getAtributo(RENDERED_PANEL_CONTROLES) == null) {
			setAtributo(RENDERED_PANEL_CONTROLES, false);
		}
		setExibePainelDeControles((Boolean) getAtributo(RENDERED_PANEL_CONTROLES));

		if (getAtributo(CHAVE_CONTROLE) == null) {
			setAtributo(CHAVE_CONTROLE, null);
		}
		setChaveControle((Long) getAtributo(CHAVE_CONTROLE));

		if (getAtributo(NOME_TABELA_CONTROLE) == null) {
			setAtributo(NOME_TABELA_CONTROLE, null);
		}
		setNomeControleTabela((String) getAtributo(NOME_TABELA_CONTROLE));

		setItensControlesUsuario((SortedMap<TipoControle, List<ItemControleResult>>) getAtributo(ITENS_CONTROLE));
		setItensControlesUsuarioOriginal((SortedMap<TipoControle, List<ItemControleResult>>) getAtributo(ITENS_CONTROLE_ORIGINAL));
	}

	public void atualizarSessao() {
		setAtributo(LISTA_CONTROLE, controles);
		setAtributo(ARVORE_CONTROLE, arvoreControle);
		setAtributo(ARVORE_EXPEDIENTE_EXPEDIDO, arvoreExpedienteExpedido);
		setAtributo(ARVORE_ITEN_USUARIO, arvoreItensUsuario);
		setAtributo(LISTA_TIPO_CONTROLE, listaTesteTipoControles);
		setAtributo(LISTA_ITENS_CONTROLE, listaItensControle);
		setAtributo(LISTA_USUARIO_CONTROLE, listaUsuariosControles);
		setAtributo(LISTA_SETORES_GABIENTES, listaSetoresGabinetes);
		setAtributo(RENDERIZA_ITENS_CONTROLE, renderedItensControle);
		setAtributo(RENDERIZA_ARV_CONTROLE, renderedArvoreControle);
		setAtributo(RENDERED_PANEL_CONTROLES, exibePainelDeControles);
		setAtributo(RENDERED_ARVORE_USUARIOS, renderedArvoreUsuarios);
		setAtributo(CHAVE_CONTROLE, chaveControle);
		setAtributo(ITENS_CONTROLE, itensControlesUsuario);
		setAtributo(ITENS_CONTROLE_ORIGINAL, itensControlesUsuarioOriginal);
		setAtributo(NOME_TABELA_CONTROLE, nomeControleTabela);
		setAtributo(CODIGO_SETOR_ATUAL, setorAtual);
	}

	// #################### ACTIONS #######################################
	/**
	 * Altera o setor do usuário
	 */
	public void alterarSetor() {
		Setor setor = null;
		try {
			setor = getSetorService().recuperarPorId(setorAtual);
			usuario.setSetor(setor);
			setExibePainelDeControles(false);
			if (itensControlesUsuario != null && itensControlesUsuario.size() == 1) {
				setRenderedArvoreUsuarios(false);
			}
			atualizarSessao();
			reportarInformacao("Setor alterado com sucesso!");

		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Não foi possível trocar o Setor do usuário.");
		}
	}

	/**
	 * Link que possui na coluna usuário para que ordene a lista de acordo com a ordem alfabética no nome do usuário.
	 */
	public void ordenaListaPeloUsuarioEItensNaoAssumidosAction(ActionEvent evt) {

		List<ItemControleSearchData> lista = new LinkedList<ItemControleSearchData>();
		for (CheckableDataTableRowWrapper linha : listaItensControle) {
			lista.add((ItemControleSearchData) linha.getWrappedObject());

		}

		if (lista != null && lista.size() > 0) {
			OrdenacaoUtils.listaOrdenaUsuarioItemControle(lista);
			setListaItensControle(getCheckableDataTableRowWrapperList(lista));
		}
		atualizarSessao();
	}

	public void atribuirControleAoUsuario(ActionEvent evt) {
		List<CheckableDataTableRowWrapper> listaItensControleSelecionados = retornarItensCheckableSelecionados(listaItensControle);

		if (listaItensControleSelecionados != null && listaItensControleSelecionados.size() > 0) {
			for (CheckableDataTableRowWrapper check : listaItensControleSelecionados) {
				ItemControleSearchData itemSearchData = (ItemControleSearchData) check.getWrappedObject();
				realizaAcaoSolicitada(1, idUsuarioContr, true, itemSearchData.getItemControleResult());
			}

		} else {
			reportarAviso("Um item deve ser selecionado.");
			return;
		}
		reportarAviso("Atribuição feita com sucesso.");
	}

	public void marcarTodosItensControles(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaItensControle);
		setListaItensControle(listaItensControle);
	}

	public void atualizarMarcacao(ActionEvent evt) {
		setListaItensControle(listaItensControle);
	}

	// ######################### METHODS ##################################

	public List<SelectItem> carregarComboSetores() {
		ministroPresidente = verificarGabineteAtualPresidencia();
		List<SelectItem> listaSetores = new LinkedList<SelectItem>();
		List<SelectItem> listaOrdenadaPeloSetorUsuario = new ArrayList<SelectItem>();
		if (isMaster()) {
			listaSetores.addAll(buscaSetoresConformePerfilUsuario(true));
		} else {
			listaSetores.addAll(buscaSetoresConformePerfilUsuario(false));
		}

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

	public void pesquisaControlesExistentes() {
		if (AcegiSecurityUtils.isUserInRole(BeanUsuario.RS_MANTER_CONTROLE_PROC)) {
			setRenderedArvoreControle(true);
		} else {
			setRenderedArvoreControle(false);
		}

		buscaItensControlesDoUsuario();
		if (itensControlesUsuario != null && itensControlesUsuario.size() > 0) {
			montaArvoreControle();
			montaArvoreExpedienteExpedidos();
		}
		atualizarSessao();
	}

	/**
	 * Atualiza a tabela item_controle chamando a package
	 * 
	 * @author ViniciusK
	 */
	public void chamaPackageItemControle() {
		try {
			getItemControleService().limparSessao();
			getItemControleService().chamaPackageItemControle();
		} catch (ServiceException e) {
			reportarErro("Erro ao chamar a package.");
			e.printStackTrace();
		}
		pesquisaControlesExistentes();
		realizaAcaoControle();
	}

	/**
	 * Monta o mapa com os controles e a lista de itens associados.
	 */
	public void buscaItensControlesDoUsuario() {

		boolean permissaoGabineteSEJ = verificaSePossuiPermissaoSej();
		boolean permissaoManterGrupos = verificaSePossuiPermissaoManterGrupos();
		// boolean permissaoManterControleProcessamento = verificaSePossuiPermissaoManterControleProc();
		itensControlesUsuario = null;

		try {
			itensControlesUsuario = getItemControleService().buscaListaDeItemUsuario(usuario.getUsername().toUpperCase(), usuario.getSetor(),
					permissaoGabineteSEJ, permissaoManterGrupos);
		} catch (ServiceException e) {
			reportarErro("Erro ao busca a lista de controle referente ao usuário.", e, LOG);
		}

		if (itensControlesUsuario == null) {
			setExibePainelDeControles(false);
		} else {
			setExibePainelDeControles(true);
		}

		setItensControlesUsuarioOriginal(itensControlesUsuario);
		setListaItensControle(null);
		//atualizarSessao();
	}

	public List<SelectItem> buscaSetoresConformePerfilUsuario(Boolean perfilMaster) {
		List<Setor> setores = new ArrayList<Setor>();
		List<SelectItem> listaSetores = new LinkedList<SelectItem>();
		try {
			if (perfilMaster) {				
				setores.addAll(getSetorService().pesquisarSetores(true));
			} else {
				setores.addAll(getUsuarioService().pesquisarSetoresEGab(usuario.getUsername()));
			}
			if (setores != null && setores.size() > 0) {
				for (Setor s : setores) {
					listaSetores.add(new SelectItem(s.getId(), s.getNome() + " - " + s.getId()));
				}
			}
			if (ministroPresidente != null && perfilMaster) {
				Setor setor = getSetorService().recuperarPorId(Setor.CODIGO_SETOR_PRESIDENCIA);

				listaSetores.add(new SelectItem(ministroPresidente.getId().getMinistro().getSetor().getId(), ministroPresidente.getId().getMinistro().getSetor().getNome() + " - " +  ministroPresidente.getId().getMinistro().getSetor().getId()));

				if (usuario.getSetor().getId() != setor.getId() && listaSetores != null || usuario.getSetor().getId().equals(Setor.CODIGO_SETOR_PRESIDENCIA)) {
					Collections.reverse(listaSetores);
				}
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao busca a lista de setores.", e, LOG);
		}
		return listaSetores;
	}

	public String realizaAcaoControle() {
		
		if (chaveControle == TreeBuilder.CHAVE_CUMPRIMENTO_DESPACHO_DECISAO) {
			
			TipoControle tipo = buscaTipoControle(chaveControle);
			setItensControlesUsuario(itensControlesUsuarioOriginal);
			mostraItensControle(tipo);
			montaArvoreUsuarios(1L, tipo);
			
		} else if (chaveControle == TreeBuilder.CHAVE_DOCUMENTOS_PARA_ASSINAR) {
			
			return "assinarDocumentos";
			
		} else if (chaveControle == TreeBuilder.CHAVE_DOCUMENTOS_PARA_ENVIAR) {
			
			return "aguardandoAssinatura";

		} else if (chaveControle == TreeBuilder.CHAVE_DOCUMENTOS_PARA_CORRECAO) {
			setAtributo(BeanManterDocumentos.PESQUISAR_AO_CARREGAR_PAGINA, true);
			return "manterDocumentos";

		} else if (chaveControle == TreeBuilder.CHAVE_DOCUMENTOS_ASSINADOS) {
			setAtributo(BeanAssinarDocumentos.CODIGO_FASE_DOCUMENTO_SELECIONADO, 5L);
			return "assinarDocumentos";
			
		} else if (chaveControle == TreeBuilder.CHAVE_DOCUMENTOS_EXPEDICAO) {
			
			return "expedirDocumentos";
			
		} else if (chaveControle == TreeBuilder.CHAVE_DOCUMENTOS_PARA_REVISAR) {
			
			return "aguardandoRevisao";
			
		} else if (chaveControle == 9L) {
			
			return "expedirDocumentos";
		}
		atualizarSessao();
		return null;
	}

	public TipoControle buscaTipoControle(Long chaveC) {
		try {
			return getTipoControleService().recuperarPorId(chaveControle);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao carregar a lista de itens.");
			return null;
		}
	}

	/**
	 * Mostra na tela os itens do controle selecionado
	 */
	public void mostraItensControle(TipoControle tipo) {

		setListaItensControle(getCheckableDocumentoListItemControle(itensControlesUsuario.get(tipo)));
		setRenderedItensControle(Boolean.TRUE);
		setNomeControleTabela(" - " + tipo.getDescricao() + " - Data de Atualização: " + DateTimeHelper.getDataHoraString(tipo.getDataAtualizacao()));
		atualizarSessao();
	}

	/**
	 * Cria a árvores de controles
	 */
	public void montaArvoreControle() {
		int countArvorePai = 1;

		TreeBuilder treeBuilder = new TreeBuilder();
		for (TipoControle tc : itensControlesUsuario.keySet()) {
			treeBuilder.addNote(tc, countArvorePai, itensControlesUsuario.get(tc).size());
			countArvorePai++;
		}
		setArvoreControle(treeBuilder.getTreeNodeRaiz());
	}

	/**
	 * Monta a árvores de expedientes expedidos
	 */
	public void montaArvoreExpedienteExpedidos() {
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
		Boolean perfilAssinatura = false;
		Boolean perfilElaboracao = false;
		TreeBuilder arvoreExpExpedidos = new TreeBuilder();
		if ((AcegiSecurityUtils.isUserInRole(BeanUsuario.RS_ELABORACAO_TEXTOS)) && (!AcegiSecurityUtils.isUserInRole(BeanUsuario.RS_MASTER_PROCESSAMENTO))
				&& (!AcegiSecurityUtils.isUserInRole(BeanUsuario.RS_GESTAO_TEXTOS))) {
			perfilElaboracao = true;
		}

		// Os usuários com perfil de assinatura não visualizam o painel para Expedição
		// Os usuáros com perfil de expedição não visualizam o painel de assinados e aguardando
		// encaminhamento
		if (AcegiSecurityUtils.isUserInRole(BeanUsuario.RS_ASSINATURA_TEXTOS)) {
			perfilAssinatura = true;
		}
		arvoreExpExpedidos.addNoteGenerico("Expediente Expedidos", 1L, true);
		arvoreExpExpedidos.addChildrenExpedido(usuarioAssinatura, perfilElaboracao, perfilAssinatura, 1);
		setArvoreExpedienteExpedido(arvoreExpExpedidos.getTreeNodeRaiz());
		atualizarSessao();
	}

	/**
	 * Mostra a árvore com o usuário associados aos itens controle
	 */
	public void montaArvoreUsuarios(Long controle, TipoControle tipo) {

		Integer quantidadeItensPorUsuario = 0; // quantitativo de itens que estão associados ao
												// usuário
		Integer quantidadeItensSemUsuario = 0; // quantitativo de itens que não estão associados ao
												// usuário
		int contadorFilhosArvoreUsuario = 1; // contador para a criação da árvore de usuários.
		SortedMap<String, Integer> mapaUsuarioEQuantitativoOrdenado = new TreeMap<String, Integer>();
		LinkedHashMap<String, Integer> mapaUsuarioEQuantitativo = new LinkedHashMap<String, Integer>(); // mapa
																										// que
																										// armazena
																										// os
																										// usuários
																										// encontrados
																										// e
																										// o
																										// seu
																										// quantitativo
																										// de
																										// itens
																										// associados.
		String usuarioComparator = null;
		if (itensControlesUsuario.get(tipo) != null) {
			for (ItemControleResult itemResult : itensControlesUsuario.get(tipo)) {
				quantidadeItensPorUsuario = 1;
				if (itemResult.getItemControle().getUsuario() != null) {
					usuarioComparator = itemResult.getItemControle().getUsuario().getId();
					for (ItemControleResult result : itensControlesUsuario.get(tipo)) {
						if (result.getItemControle().getUsuario() != null) {
							if (usuarioComparator.equalsIgnoreCase(result.getItemControle().getUsuario().getId())) {
								mapaUsuarioEQuantitativoOrdenado.put(itemResult.getItemControle().getUsuario().getId(), quantidadeItensPorUsuario);
								quantidadeItensPorUsuario++;
							}
						}
					}
				} else {
					++quantidadeItensSemUsuario;
				}
			}
		}

		if (quantidadeItensSemUsuario > 0 || quantidadeItensPorUsuario > 0) {
			mapaUsuarioEQuantitativo.putAll(mapaUsuarioEQuantitativoOrdenado);
			mapaUsuarioEQuantitativo.put("SEM ATRIBUIÇÃO", quantidadeItensSemUsuario);
			mapaUsuarioEQuantitativo.put("TODOS", itensControlesUsuario.get(tipo).size());
		}

		if (mapaUsuarioEQuantitativo != null && mapaUsuarioEQuantitativo.size() > 0) {
			setRenderedArvoreUsuarios(Boolean.TRUE); // render que mostrar a árvore de usuários
			TreeBuilder treeBuilder = new TreeBuilder();
			String cabecalho = "Usuarios";
			treeBuilder.addNoteGenerico(cabecalho, 0L, true); // adiciona o cabeçalho da árvore
			for (String ui : mapaUsuarioEQuantitativo.keySet()) {
				NoGenericoUsuario usuarioItens = new NoGenericoUsuario();
				usuarioItens.setChaveU(ui);
				usuarioItens.setDescricaoU(ui + ": " + mapaUsuarioEQuantitativo.get(ui).toString());
				treeBuilder.addChildrenGenerico(usuarioItens, contadorFilhosArvoreUsuario); // monta
																							// os
																							// filhos
																							// da
																							// árvore
																							// usuários
				contadorFilhosArvoreUsuario++;
			}
			treeBuilder.criaRaizArvoreGenerica(1); // cria o nó raiz da árvore
			setArvoreItensUsuario(treeBuilder.getTreeNodeRaiz());
		}

		atualizarSessao();
	}

	/**
	 * Carrega a tabela de itens somente com os itens do usuário clicado.
	 */
	public String carregaItensUsuario() {
		TipoControle tipo = buscaTipoControle(chaveControle);
		List<ItemControleResult> listaControleR = new LinkedList<ItemControleResult>();
		setItensControlesUsuario(itensControlesUsuarioOriginal);

		for (ItemControleResult itemResult : itensControlesUsuario.get(tipo)) {
			if (chaveControleUsuario.equalsIgnoreCase("TODOS")) {
				listaControleR.add(itemResult);
			} else if (chaveControleUsuario.equalsIgnoreCase("SEM ATRIBUIÇÃO")) {
				if (itemResult.getItemControle().getUsuario() == null) {
					listaControleR.add(itemResult);
				}
			}
			if (itemResult.getItemControle().getUsuario() != null) {
				if (itemResult.getItemControle().getUsuario().getId().equalsIgnoreCase(chaveControleUsuario)) {
					listaControleR.add(itemResult);
				}
			}
		}
		itensControlesUsuario = new TreeMap<TipoControle, List<ItemControleResult>>();
		itensControlesUsuario.put(tipo, listaControleR);
		mostraItensControle(tipo);
		atualizarSessao();
		return null;
	}

	/**
	 * Assume o iten controle
	 */
	public void assumirControle() {
		realizaAcaoSolicitada(1, usuario.getUsername().toUpperCase(), false, null);
	}

	/**
	 * Fecha o item controle assumido
	 */
	public void fecharControle() {
		realizaAcaoSolicitada(2, null, false, null);
	}

	/**
	 * Chama a tela de manter documentos para gerar o expediente. Nesta funcionalidade ele leva o objeto incidente.
	 */
	public String gerarExpediente() {
		return realizaAcaoSolicitada(3, null, false, null);
	}

	public String gerarAndamento() {
		return realizaAcaoSolicitada(4, null, false, null);
	}

	public String gerarDeslocamento() {
		return realizaAcaoSolicitada(5, null, false, null);
	}

	public String realizaAcaoSolicitada(int chaveAcao, String sigUsuario, boolean ehAtribuicao, ItemControleResult itemControleAtribuicao) {
		ItemControleSearchData itemControleSearch = new ItemControleSearchData();
		ItemControleResult itemControleResult = new ItemControleResult();
		TipoSituacaoControle tipo = new TipoSituacaoControle();
		CheckableDataTableRowWrapper itemCheckable = null;
		if (!ehAtribuicao) {
			itemControleSearch = (ItemControleSearchData) ((CheckableDataTableRowWrapper) tabelaItensControle.getRowData()).getWrappedObject();
			itemControleResult = itemControleSearch.getItemControleResult();
			itemCheckable = ((CheckableDataTableRowWrapper) tabelaItensControle.getRowData());
		} else {
			itemControleResult = itemControleAtribuicao;
		}
		ItemControle item = itemControleResult.getItemControle();
		switch (chaveAcao) {
		case 1:
			// assumir item controle
			try {
				Usuario usu = getUsuarioService().recuperarUsuario(sigUsuario.toUpperCase());
				tipo = recuperaTipoSituacaoControle("EM ANÁLISE");
				item.setTipoSituacaoControle(tipo);
				item.setUsuario(usu);
				gravaItemControle(item);
			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao buscar o sig do usuário ou o tipo de situacao.");
				return null;
			}
			reportarAviso("Item controle associado.");
			break;

		case 2:
			// fechar o item controle
			if (item.getTipoSituacaoControle().getId() == 1L) {
				reportarAviso("É necessário assumir o controle.");
			} else {
				try {
					tipo = recuperaTipoSituacaoControle("FECHADO");
					item.setTipoSituacaoControle(tipo);
					gravaItemControle(item);
					for (int index = 0; listaItensControle.size() > index; index++) {
						if (listaItensControle.get(index).getIndice() == itemCheckable.getIndice()) {
							listaItensControle.remove(index);
						}
					}

				} catch (ServiceException e) {
					e.printStackTrace();
					reportarErro("Erro ao buscar o tipo de situacao.");
					return null;
				}
				reportarAviso("Item controle fechado.");
			}
			break;

		case 3:
			if (item.getTipoSituacaoControle().getId() != 21L) {
				reportarAviso("É necessário assumir o item.");
			} else {
				setAtributo(BeanManterDocumentos.OBJETO_INCIDENTE_TRANS_BEAN, item.getObjetoIncidente());
				return "manterDocumentos";
			}
			break;
		case 4:
			if (item.getTipoSituacaoControle().getId() != 21L) {
				reportarAviso("É necessário assumir o item.");
			} else {
				setAtributo(BeanRegistrarAndamento.PROCESSO_INCIDENTE_TRANS_BEAN, item.getObjetoIncidente());
				return "registrarAndamento";
			}
			break;
		case 5:
			if (item.getTipoSituacaoControle().getId() != 21L) {
				reportarAviso("É necessário assumir o item.");
			} else {
				setAtributo(BeanRemeterDocumento.REMETER_INCIDENTE_TRANS_BEAN, item.getObjetoIncidente());
				return "remeterDocumento";
			}
		}
		atualizarSessao();
		return null;
	}

	/**
	 * Recupera o tipo de situação pela sua descrição
	 */
	private TipoSituacaoControle recuperaTipoSituacaoControle(String dscTipoSituacao) throws ServiceException {
		TipoSituacaoControle tipoSC = getTipoSituacaoControleService().pesquisarSituacaoControle(dscTipoSituacao);
		return tipoSC;
	}

	/**
	 * Grava o item controle alterado
	 */
	private void gravaItemControle(ItemControle itemC) {
		try {
			getItemControleService().salvar(itemC);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao alterar a situação do item controle.");
			return;
		}
	}

	/**
	 * Renderiza o paine de controle e a lista de setores
	 */
	public void renderizarPainelDeControle() {
		pesquisaControlesExistentes();
		setExibePainelDeControles(true);
		atualizarSessao();
	}

	/**
	 * Cria a lista de usuários para serem mostrados na combo de reatribuição do item controle
	 * 
	 * @param evt
	 */
	public void listarComboUsuarios(ActionEvent evt) {
		List<UsuarioEGab> listaUsuarioEgab = new LinkedList<UsuarioEGab>();

		try {
			listaUsuarioEgab = getUsuarioService().pesquisarUsuarios(usuario.getSetor().getId(), null, true);
		} catch (ServiceException e) {
			reportarErro("Erro ao listar os grupos do  usuário");
			e.printStackTrace();
		}

		listaUsuariosControles.clear();
		listaUsuariosControles.add(new SelectItem(null, null));
		if (listaUsuarioEgab.size() > 0) {
			for (UsuarioEGab usu : listaUsuarioEgab) {
				listaUsuariosControles.add(new SelectItem(usu.getUsuario().getId(), usu.getUsuario().getNome()));
			}
		}
		atualizarSessao();
	}

	/**
	 * Preenche os dados do modalpanel com o nome do jurisdicionado e a data de prescrição.
	 */
	public void carregaInformacoesPrescricao() {
		ItemControleSearchData itemControleSearch = (ItemControleSearchData) ((CheckableDataTableRowWrapper) tabelaItensControle.getRowData()).getWrappedObject();
		ItemControleResult itemControleResult = itemControleSearch.getItemControleResult();

		Processo processo = (Processo) itemControleResult.getItemControle().getObjetoIncidente().getPrincipal();
		// Prescrição Réu
		Hibernate.initialize(processo.getReferenciaPrescricao());
		if (processo.getReferenciaPrescricao() != null && processo.getReferenciaPrescricao().size() > 0) {
			Hibernate.initialize(processo.getReferenciaPrescricao().get(0).getPrescricaoReus());
			ReferenciaPrescricao referenciaPrescricao = processo.getReferenciaPrescricao().get(0);
			prescricaoReu = referenciaPrescricao.getPrescricaoReus();
			for (PrescricaoReu pr : prescricaoReu) {
				Hibernate.initialize(pr.getJurisdicionado());
			}
		}
	}

	/**
	 * Verifica se o usuário tem perfil para listar todos os itens controle
	 */
	public boolean verificaSePossuiPermissaoSej() {
		boolean permissao = false;
		if ((AcegiSecurityUtils.isUserInRole(BeanUsuario.RS_GABINETE_SEJ))) {
			permissao = true;
		}
		return permissao;
	}

	/**
	 * Verifica se o usuário tem perfil MANTER_CONTR0LE_PROCESSAMENTO
	 */
	public boolean verificaSePossuiPermissaoManterControleProc() {
		boolean permissao = false;
		if ((AcegiSecurityUtils.isUserInRole(BeanUsuario.RS_MANTER_CONTROLE_PROC))) {
			permissao = true;
		}
		return permissao;
	}

	/**
	 * Verifica se o usuário tem perfil para listar todos os usuários que constam na tabela do egab pelo setor.
	 */
	public boolean verificaSePossuiPermissaoManterGrupos() {
		boolean permissao = false;
		if ((AcegiSecurityUtils.isUserInRole(BeanUsuario.RS_MANTER_GRUPO_USUARIOS))) {
			permissao = true;
		}
		return permissao;
	}

	public Boolean expandeNoArvore(UITree tree) {
		return true;
	}

	// #################### GET AND SETS ##################################

	public UsuarioAssinatura getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioAssinatura usuario) {
		this.usuario = usuario;
	}

	public Long getSetorAtual() {
		return setorAtual;
	}

	public void setSetorAtual(Long setorAtual) {
		this.setorAtual = setorAtual;
	}

	public List<SelectItem> getListaSetoresGabinetes() {
		return listaSetoresGabinetes;
	}

	public void setListaSetoresGabinetes(List<SelectItem> listaSetoresGabinetes) {
		this.listaSetoresGabinetes = listaSetoresGabinetes;
	}

	public boolean isAdmin() {
		return isUsuarioAdmin();
	}

	public boolean isMaster() {
		return isUsuarioMaster();
	}

	public Boolean getExibirListaSetores() {
		return exibirListaSetores;
	}

	public void setExibirListaSetores(Boolean exibirListaSetores) {
		this.exibirListaSetores = exibirListaSetores;
	}

	public Integer getQtdeDocumentosAssinar() {
		return qtdeDocumentosAssinar;
	}

	public void setQtdeDocumentosAssinar(Integer qtdeDocumentosAssinar) {
		this.qtdeDocumentosAssinar = qtdeDocumentosAssinar;
	}

	public Integer getQtdeDocumentosEnviar() {
		return qtdeDocumentosEnviar;
	}

	public void setQtdeDocumentosEnviar(Integer qtdeDocumentosEnviar) {
		this.qtdeDocumentosEnviar = qtdeDocumentosEnviar;
	}

	public Integer getQtdeDocumentosAssinados() {
		return qtdeDocumentosAssinados;
	}

	public void setQtdeDocumentosAssinados(Integer qtdeDocumentosAssinados) {
		this.qtdeDocumentosAssinados = qtdeDocumentosAssinados;
	}

	public Integer getQtdeDocumentosCorrecao() {
		return qtdeDocumentosCorrecao;
	}

	public void setQtdeDocumentosCorrecao(Integer qtdeDocumentosCorrecao) {
		this.qtdeDocumentosCorrecao = qtdeDocumentosCorrecao;
	}

	public Integer getQtdeDocumentosExpedicao() {
		return qtdeDocumentosExpedicao;
	}

	public void setQtdeDocumentosExpedicao(Integer qtdeDocumentosExpedicao) {
		this.qtdeDocumentosExpedicao = qtdeDocumentosExpedicao;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaItensControle() {
		return tabelaItensControle;
	}

	public void setTabelaItensControle(org.richfaces.component.html.HtmlDataTable tabelaItensControle) {
		this.tabelaItensControle = tabelaItensControle;
	}

	public List<CheckableDataTableRowWrapper> getListaItensControle() {
		return listaItensControle;
	}

	public void setListaItensControle(List<CheckableDataTableRowWrapper> listaItensControle) {
		this.listaItensControle = listaItensControle;
	}

	public Boolean getRenderedItensControle() {
		return renderedItensControle;
	}

	public void setRenderedItensControle(Boolean renderedItensControle) {
		this.renderedItensControle = renderedItensControle;
	}

	public SortedMap<TipoControle, List<ItemControleResult>> getItensControlesUsuario() {
		return itensControlesUsuario;
	}

	public void setItensControlesUsuario(SortedMap<TipoControle, List<ItemControleResult>> itensControlesUsuario) {
		this.itensControlesUsuario = itensControlesUsuario;
	}

	public Long getChaveControle() {
		return chaveControle;
	}

	public void setChaveControle(Long chaveControle) {
		this.chaveControle = chaveControle;
	}

	public static Object getRenderedPanelControles() {
		return RENDERED_PANEL_CONTROLES;
	}

	public Boolean getExibePainelDeControles() {
		return exibePainelDeControles;
	}

	public void setExibePainelDeControles(Boolean exibePainelDeControles) {
		this.exibePainelDeControles = exibePainelDeControles;
	}

	public String getNomeControleTabela() {
		return nomeControleTabela;
	}

	public void setNomeControleTabela(String nomeControleTabela) {
		this.nomeControleTabela = nomeControleTabela;
	}

	public List<Long> getListaTesteTipoControles() {
		return listaTesteTipoControles;
	}

	public void setListaTesteTipoControles(List<Long> listaTesteTipoControles) {
		this.listaTesteTipoControles = listaTesteTipoControles;
	}

	public Map<Long, String> getControles() {
		return controles;
	}

	public void setControles(Map<Long, String> controles) {
		this.controles = controles;
	}

	public List<SelectItem> getListaUsuariosControles() {
		return listaUsuariosControles;
	}

	public void setListaUsuariosControles(List<SelectItem> listaUsuariosControles) {
		this.listaUsuariosControles = listaUsuariosControles;
	}

	public String getIdUsuarioContr() {
		return idUsuarioContr;
	}

	public void setIdUsuarioContr(String idUsuarioContr) {
		this.idUsuarioContr = idUsuarioContr;
	}

	public TreeNode getArvoreControle() {
		return arvoreControle;
	}

	public void setArvoreControle(TreeNode arvoreControle) {
		this.arvoreControle = arvoreControle;
	}

	public TreeNode getArvoreItensUsuario() {
		return arvoreItensUsuario;
	}

	public void setArvoreItensUsuario(TreeNode arvoreItensUsuario) {
		this.arvoreItensUsuario = arvoreItensUsuario;
	}

	public Boolean getRenderedArvoreUsuarios() {
		return renderedArvoreUsuarios;
	}

	public void setRenderedArvoreUsuarios(Boolean renderedArvoreUsuarios) {
		this.renderedArvoreUsuarios = renderedArvoreUsuarios;
	}

	public Long getChaveControleExpedienteExpedidos() {
		return chaveControleExpedienteExpedidos;
	}

	public void setChaveControleExpedienteExpedidos(Long chaveControleExpedienteExpedidos) {
		this.chaveControleExpedienteExpedidos = chaveControleExpedienteExpedidos;
	}

	public List<PrescricaoReu> getPrescricaoReu() {
		return prescricaoReu;
	}

	public void setPrescricaoReu(List<PrescricaoReu> prescricaoReu) {
		this.prescricaoReu = prescricaoReu;
	}

	public String getChaveControleUsuario() {
		return chaveControleUsuario;
	}

	public void setChaveControleUsuario(String chaveControleUsuario) {
		this.chaveControleUsuario = chaveControleUsuario;
	}

	public SortedMap<TipoControle, List<ItemControleResult>> getItensControlesUsuarioOriginal() {
		return itensControlesUsuarioOriginal;
	}

	public void setItensControlesUsuarioOriginal(SortedMap<TipoControle, List<ItemControleResult>> itensControlesUsuarioOriginal) {
		this.itensControlesUsuarioOriginal = itensControlesUsuarioOriginal;
	}

	public TreeNode getArvoreExpedienteExpedido() {
		return arvoreExpedienteExpedido;
	}

	public void setArvoreExpedienteExpedido(TreeNode arvoreExpedienteExpedido) {
		this.arvoreExpedienteExpedido = arvoreExpedienteExpedido;
	}

	public Boolean getRenderedArvoreControle() {
		return renderedArvoreControle;
	}

	public void setRenderedArvoreControle(Boolean renderedArvoreControle) {
		this.renderedArvoreControle = renderedArvoreControle;
	}

}
