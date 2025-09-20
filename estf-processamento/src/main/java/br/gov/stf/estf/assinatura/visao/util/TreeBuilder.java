package br.gov.stf.estf.assinatura.visao.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.processostf.TipoControle;
import br.gov.stf.framework.model.service.ServiceException;

public class TreeBuilder extends AssinadorBaseBean {

	public static final long CHAVE_DOCUMENTOS_PARA_REVISAR = 8L;
	public static final long CHAVE_DOCUMENTOS_EXPEDICAO = 7L;
	public static final long CHAVE_DOCUMENTOS_ASSINADOS = 6L;
	public static final long CHAVE_DOCUMENTOS_PARA_CORRECAO = 5L;
	public static final long CHAVE_DOCUMENTOS_PARA_ENVIAR = 4L;
	public static final long CHAVE_DOCUMENTOS_PARA_ASSINAR = 3L;
	public static final long CHAVE_CUMPRIMENTO_DESPACHO_DECISAO = 1L;

	private static final Log LOG = LogFactory.getLog(TreeBuilder.class);

	private TreeNode<?> treeNodeRaiz;
	private TreeNodeImpl pai;

	public TreeBuilder() {
		this.treeNodeRaiz = new TreeNodeImpl();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addNote(Object obTree, int countTree, Integer totalItensUsuario) {
		TreeNodeImpl paiN = new TreeNodeImpl();
		if (obTree instanceof TipoControle) {
			TipoControle tipoC = (TipoControle) obTree;
			// CUMPRIMENTO DE DESPACHO E DECISAO
			if (tipoC.getId() == 1L) {
				ControleDeItens cumprimentoDespDecisao = new ControleDeItens();
				cumprimentoDespDecisao.setChave(1L);
				cumprimentoDespDecisao.setDescricao("Cumprimento de Despacho e Decisão (" + totalItensUsuario.toString() + ")");
				paiN.setData(cumprimentoDespDecisao);
				treeNodeRaiz.addChild(countTree, paiN);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addNoteGenerico(String cabecalho, Long countTree, boolean insereNoFilho) {
		pai = new TreeNodeImpl();
		NoGenerico noGenerico = new NoGenerico();
		noGenerico.setChave(countTree);
		noGenerico.setDescricao(cabecalho);
		pai.setData(noGenerico);
		if (!insereNoFilho) {
			treeNodeRaiz.addChild(countTree, pai);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addChildrenGenerico(Object noGenerico, int countTree) {
		TreeNodeImpl filho1 = new TreeNodeImpl();
		filho1.setData(noGenerico);
		pai.addChild(countTree, filho1);
	}

	public void criaRaizArvoreGenerica(int countNoPai) {
		treeNodeRaiz.addChild(countNoPai, pai);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addChildrenExpedido(UsuarioAssinatura usuarioAssinatura, Boolean perfilElaboracao, Boolean perfilAssinatura, int countTree) {
		
		TreeNodeImpl treeNode;

		if (isUsuarioGestorTextos() || isUsuarioRevisorTextos()) {
			treeNode = new TreeNodeImpl();
			treeNode.setData(criaExpedienteExpedidos(usuarioAssinatura, CHAVE_DOCUMENTOS_PARA_REVISAR));
			pai.addChild(0, treeNode);
		}
		
		treeNode = new TreeNodeImpl();
		treeNode.setData(criaExpedienteExpedidos(usuarioAssinatura, CHAVE_DOCUMENTOS_PARA_ASSINAR));
		pai.addChild(1, treeNode);

		treeNode = new TreeNodeImpl();
		treeNode.setData(criaExpedienteExpedidos(usuarioAssinatura, CHAVE_DOCUMENTOS_PARA_ENVIAR));
		pai.addChild(2, treeNode);

		treeNode = new TreeNodeImpl();
		treeNode.setData(criaExpedienteExpedidos(usuarioAssinatura, CHAVE_DOCUMENTOS_PARA_CORRECAO));
		pai.addChild(3, treeNode);

		if (perfilAssinatura) {
			treeNode = new TreeNodeImpl();
			treeNode.setData(criaExpedienteExpedidos(usuarioAssinatura, CHAVE_DOCUMENTOS_ASSINADOS));
			pai.addChild(4, treeNode);
		} else {
			treeNode = new TreeNodeImpl();
			treeNode.setData(criaExpedienteExpedidos(usuarioAssinatura, CHAVE_DOCUMENTOS_EXPEDICAO));
			pai.addChild(5, treeNode);
		}
		treeNodeRaiz.addChild(countTree, pai);
	}

	public ControleDeItens criaExpedienteExpedidos(UsuarioAssinatura usuarioAssinatura, Long chaveExpExpedidos) {
		ControleDeItens expedienteExpedidos = new ControleDeItens();

		try {
			if (chaveExpExpedidos == CHAVE_DOCUMENTOS_PARA_ASSINAR) {
				
				criarItemExpedientesExpedidos(CHAVE_DOCUMENTOS_PARA_ASSINAR, "Documentos para assinar: ", expedienteExpedidos, usuarioAssinatura, 
						TipoFaseComunicacao.AGUARDANDO_ASSINATURA);
				
			} else if (chaveExpExpedidos == CHAVE_DOCUMENTOS_PARA_ENVIAR) {
				
				criarItemExpedientesExpedidos(CHAVE_DOCUMENTOS_PARA_ENVIAR, "Documentos para enviar: ", expedienteExpedidos, usuarioAssinatura, TipoFaseComunicacao.PDF_GERADO,
						TipoFaseComunicacao.REVISADO);
				
			} else if (chaveExpExpedidos == CHAVE_DOCUMENTOS_PARA_CORRECAO) {
				
				criarItemExpedientesExpedidos(CHAVE_DOCUMENTOS_PARA_CORRECAO, "Documentos para correção: ", expedienteExpedidos, usuarioAssinatura, TipoFaseComunicacao.CORRECAO);
				
			} else if (chaveExpExpedidos == CHAVE_DOCUMENTOS_ASSINADOS) {
				
				criarItemExpedientesExpedidos(CHAVE_DOCUMENTOS_ASSINADOS, "Documentos assinados e não deslocados: ", expedienteExpedidos, usuarioAssinatura, TipoFaseComunicacao.ASSINADO);
				
			} else if (chaveExpExpedidos == CHAVE_DOCUMENTOS_EXPEDICAO) {
				
				criarItemExpedientesExpedidos(CHAVE_DOCUMENTOS_EXPEDICAO, "Documentos para expedição: ", expedienteExpedidos, usuarioAssinatura, TipoFaseComunicacao.ASSINADO);
				
			} else if (chaveExpExpedidos == CHAVE_DOCUMENTOS_PARA_REVISAR) {
				
				criarItemExpedientesExpedidos(CHAVE_DOCUMENTOS_PARA_REVISAR, "Documentos para revisar: ", expedienteExpedidos, usuarioAssinatura, TipoFaseComunicacao.EM_REVISAO);
			}

			return expedienteExpedidos;
		} catch (ServiceException e) {
			LOG.error("Não foi possível recuperar o painel de controle.", e);
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	private void criarItemExpedientesExpedidos(Long chave, String descricao, ControleDeItens expedienteExpedidos,
			UsuarioAssinatura usuarioAssinatura, TipoFaseComunicacao... faseComunicacao) throws ServiceException {
		expedienteExpedidos.setChave(chave);
		
		Integer quantidadeExpedientes = 0;
		for (TipoFaseComunicacao tipoFaseComunicacao : faseComunicacao) {
			if(TipoFaseComunicacao.PDF_GERADO.equals(tipoFaseComunicacao)){
				quantidadeExpedientes += getComunicacaoService().pesquisarDocumentosCount(usuarioAssinatura.getSetor(),true);				
			} else if(TipoFaseComunicacao.CORRECAO.equals(tipoFaseComunicacao)){
				String username = "";
				if (isUsuarioGestorTextos() || isUsuarioMaster()) {
					username = null;
				} else {
					username = usuarioAssinatura.getUsername().toUpperCase();					
				}				
				quantidadeExpedientes += getComunicacaoService().pesquisarDocumentosCorrecaoCount(usuarioAssinatura.getSetor(),username);				
			}else{
				quantidadeExpedientes += getComunicacaoService().pesquisarPainelControle(tipoFaseComunicacao, usuarioAssinatura.getSetor(), null);
			}
		}
		expedienteExpedidos.setDescricao(descricao + " " + quantidadeExpedientes.toString());
	}

	// ********************************* INNER CLASS *******************************
	/**
	 * Classe utilitária que empacota tipos de controle. Criada
	 * com a função mostrar na tela a árvore de controles e poder no 
	 * momento do clique do usuário fazer o filtro para o método de pesquisa
	 * do itens.
	 */
	public static class ControleDeItens {

		public Long chave;
		public String descricao;

		public ControleDeItens() {

		}

		public Long getChave() {
			return chave;
		}

		public void setChave(Long chave) {
			this.chave = chave;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}

	/**
	 * Classe utilitária que empacota os usuários localizados na 
	 * lista de itens encontrados.
	 */
	public static class NoGenerico {

		public Long chave;
		public String descricao;

		public NoGenerico() {

		}

		public Long getChave() {
			return chave;
		}

		public void setChave(Long chave) {
			this.chave = chave;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}
	
	
	/**
	 * Classe utilitária que empacota os usuários localizados na 
	 * lista de itens encontrados.
	 */
	public static class NoGenericoUsuario {

		public String chaveU;
		public String descricaoU;

		public NoGenericoUsuario() {

		}

		public String getChaveU() {
			return chaveU;
		}

		public void setChaveU(String chaveU) {
			this.chaveU = chaveU;
		}

		public String getDescricaoU() {
			return descricaoU;
		}

		public void setDescricaoU(String descricaoU) {
			this.descricaoU = descricaoU;
		}
	}

	// ********************************* GET AND SET *******************************

	public TreeNode<?> getTreeNodeRaiz() {
		return treeNodeRaiz;
	}

	public void setTreeNodeRaiz(TreeNode<?> treeNodeRaiz) {
		this.treeNodeRaiz = treeNodeRaiz;
	}

	public TreeNodeImpl getPai() {
		return pai;
	}

	public void setPai(TreeNodeImpl pai) {
		this.pai = pai;
	}

}
