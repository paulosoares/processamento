package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.modelos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirModelo;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.documento.model.service.TipoComunicacaoService;
import br.gov.stf.estf.documento.model.service.TipoPermissaoModeloComunicacaoService;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaAcessoDocumento;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.framework.model.entity.Flag;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;
import br.gov.stf.stfoffice.editor.jnlp.AbrirDocumento.ArgumentosAbrirDocumento;
import br.gov.stf.stfoffice.editor.web.requisicao.jnlp.RequisicaoJnlpDocumento;
import br.gov.stf.stfoffice.web.requisicao.jnlp.RequisicaoJnlp;
import br.jus.stf.estf.montadortexto.tools.ClassPathTextoSource;

public class BeanAdministrarModelos extends AssinadorBaseBean {

	private static final long serialVersionUID = -9084010517176733495L;
	private static final Log LOG = LogFactory.getLog(BeanAdministrarModelos.class);

	// ******************* VARIAVEIS DE SESSAO *********************

	private static final Object ITENSMINISTROS = new Object();
	private static final Object ITENS_TIPOS_PERMISSOES = new Object();
	private static final Object ITENS_TIPOS_MODELOS = new Object();
	private static final Object ITENS_ANDAMENTO_MODELO = new Object();
	private static final Object LISTAMODELOS = new Object();
	private static final Object MODELO_COMUNICACAO = new Object();
	private static final Object ITENSSETORESDESTINO = new Object();
	private static final Object RENDERED_PESQUISA = new Object();
	private static final Object RENDERED_ALTERA_MODELO = new Object();
	private static final Object RENDERED_DESABILITA_MENU = new Object();
	private static final Object ITENS_TIPOS_PERMISSOES_ALTERACAO = new Object();
	private static final Object ITENS_TIPOS_MODELOS_ALTERACAO = new Object();

	private static final Object ID_TIPO_PERMISSAO = new Object();
	private static final Object ID_TIPO_COMUNICACAO = new Object();
	@SuppressWarnings("unused")
	private static final Object ID_TIPO_PERMISSAO_ALTERACAO = new Object();
	@SuppressWarnings("unused")
	private static final Object ID_TIPO_COMUNICACAO_ALTERACAO = new Object();

	// ************************ VARIÁVEIS *************************

	private Long idTipoPermissao;
	private Long idTipoComunicacao;
	private Long idMinistros;
	private Long idAndamentoModelo;
	private String nomeModelo;
	private String nomeModeloProcura;
	private ModeloComunicacao modeloCom;
	private Boolean flagAssinaturaMinistro;
	private Boolean flagCampoLivre;
	private Boolean flagPartes;
	private Boolean flagPecas;
	private Boolean flagDuasAssinaturas;
	private Boolean flagProcessoLote;
	private Boolean flagRestringeAcessoPeca;
	private Boolean flagEncaminharParaDJe;
	private Boolean flagAlterarObsAndamento;
	private Boolean	flagSemAndamento;
	private Boolean flagJuntadaPecaProc;
	private Boolean renderedPesquisa;
	private Boolean renderedAlterarModelo;
	private Boolean renderedDesabilitaMenu;
	private Long idSetorDestino;

	private Long idTipoPermissaoAlteracao;
	private Long idTipoComunicacaoAlteracao;

	private List<SelectItem> itensTiposPermissoes;
	private List<SelectItem> itensTiposModelos;
	private List<SelectItem> itensMinistros;
	private List<SelectItem> itensSetoresDestino;
	private List<SelectItem> itensAndamentoModelo;

	private List<SelectItem> itensTiposPermissoesAlteracao;
	private List<SelectItem> itensTiposModelosAlteracao;

	private List<CheckableDataTableRowWrapper> listaModelos;
	private HtmlDataTable tabelaModelos;

	// ############################### SESSAO ############################# //

	public BeanAdministrarModelos() {
		restaurarSessao();
	}

	@SuppressWarnings("unchecked")
	public void restaurarSessao() {

		if (getAtributo(ITENSMINISTROS) == null) {
			setAtributo(ITENSMINISTROS, carregarComboMinistros());
		}
		setItensMinistros((List<SelectItem>) getAtributo(ITENSMINISTROS));

		if (getAtributo(MODELO_COMUNICACAO) == null) {
			setAtributo(MODELO_COMUNICACAO, new ModeloComunicacao());
		}
		setModeloCom((ModeloComunicacao) getAtributo(MODELO_COMUNICACAO));

		if (getAtributo(ITENS_TIPOS_PERMISSOES) == null) {
			setAtributo(ITENS_TIPOS_PERMISSOES, carregarComboTipoPermissao(false, false));
		}
		setItensTiposPermissoes((List<SelectItem>) getAtributo(ITENS_TIPOS_PERMISSOES));

		if (getAtributo(ITENS_TIPOS_MODELOS) == null) {
			setAtributo(ITENS_TIPOS_MODELOS, carregarComboTipoModelos());
		}
		setItensTiposModelos((List<SelectItem>) getAtributo(ITENS_TIPOS_MODELOS));

		if (getAtributo(ITENS_TIPOS_PERMISSOES_ALTERACAO) == null) {
			setAtributo(ITENS_TIPOS_PERMISSOES_ALTERACAO, carregarComboTipoPermissao(false, false));
		}
		setItensTiposPermissoesAlteracao((List<SelectItem>) getAtributo(ITENS_TIPOS_PERMISSOES_ALTERACAO));

		if (getAtributo(ITENS_TIPOS_MODELOS_ALTERACAO) == null) {
			setAtributo(ITENS_TIPOS_MODELOS_ALTERACAO, carregarComboTipoModelosTodos());
		}
		setItensTiposModelosAlteracao((List<SelectItem>) getAtributo(ITENS_TIPOS_MODELOS_ALTERACAO));

		if (getAtributo(ITENS_ANDAMENTO_MODELO) == null) {
			setAtributo(ITENS_ANDAMENTO_MODELO, carregarComboAndamentoModelo());
		}
		setItensAndamentoModelo((List<SelectItem>) getAtributo(ITENS_ANDAMENTO_MODELO));

		if (getAtributo(ITENSSETORESDESTINO) == null) {
			setAtributo(ITENSSETORESDESTINO, carregarComboSetoresDestino(false,false));
		}
		setItensSetoresDestino((List<SelectItem>) getAtributo(ITENSSETORESDESTINO));

		if (getAtributo(RENDERED_PESQUISA) == null) {
			setAtributo(RENDERED_PESQUISA, Boolean.FALSE);
		}
		setRenderedPesquisa((Boolean) getAtributo(RENDERED_PESQUISA));

		if (getAtributo(RENDERED_ALTERA_MODELO) == null) {
			setAtributo(RENDERED_ALTERA_MODELO, Boolean.FALSE);
		}
		setRenderedAlterarModelo((Boolean) getAtributo(RENDERED_ALTERA_MODELO));

		if (getAtributo(RENDERED_DESABILITA_MENU) == null) {
			setAtributo(RENDERED_DESABILITA_MENU, Boolean.TRUE);
		}
		setRenderedDesabilitaMenu((Boolean) getAtributo(RENDERED_DESABILITA_MENU));

		if (getAtributo(ID_TIPO_PERMISSAO) == null) {
			setAtributo(ID_TIPO_PERMISSAO, 0L);
		}
		setIdTipoPermissao((Long) getAtributo(ID_TIPO_PERMISSAO));

		if (getAtributo(ID_TIPO_COMUNICACAO) == null) {
			setAtributo(ID_TIPO_COMUNICACAO, 0L);
		}
		setIdTipoComunicacao((Long) getAtributo(ID_TIPO_COMUNICACAO));

		setListaModelos((List<CheckableDataTableRowWrapper>) getAtributo(LISTAMODELOS));
		
		atualizarSessao();

	}

	public void atualizarSessao() {
		setAtributo(ITENSMINISTROS, itensMinistros);
		setAtributo(ITENS_TIPOS_PERMISSOES, itensTiposPermissoes);
		setAtributo(ITENS_TIPOS_MODELOS, itensTiposModelos);
		setAtributo(ITENS_ANDAMENTO_MODELO, itensAndamentoModelo);
		setAtributo(LISTAMODELOS, listaModelos);
		setAtributo(MODELO_COMUNICACAO, modeloCom);
		setAtributo(ITENSSETORESDESTINO, itensSetoresDestino);
		setAtributo(RENDERED_PESQUISA, renderedPesquisa);
		setAtributo(RENDERED_ALTERA_MODELO, renderedAlterarModelo);
		setAtributo(RENDERED_DESABILITA_MENU, renderedDesabilitaMenu);
		setAtributo(ITENS_TIPOS_PERMISSOES_ALTERACAO, itensTiposPermissoesAlteracao);
		setAtributo(ITENS_TIPOS_MODELOS_ALTERACAO, itensTiposModelosAlteracao);
		setAtributo(ID_TIPO_PERMISSAO, idTipoPermissao);
		setAtributo(ID_TIPO_COMUNICACAO, idTipoComunicacao);

	}

	// *************************** ACTIONS ***************************


	public void pesquisarModelosAction(ActionEvent evt) {
		pesquisarModelos();
		atualizarSessao();
	}

	public void alterarModeloAction(ActionEvent evt) {
		alterarModelo();
		setItensTiposModelosAlteracao(pesquisarModeloPorPermissao(null));
		atualizarSessao();
	}

	public void excluirModelosAction(ActionEvent evt) {
		excluirModelos();
		atualizarSessao();
	}

	public void limparTelaAction(ActionEvent evt) {
		limparTela();
		atualizarSessao();
	}

	public void salvarModeloAlteradoAction(ActionEvent evt) {
		salvarModeloAlterado();
		atualizarSessao();
	}

	public void desabilitarCampoDeEncaminhamentoAction(ActionEvent evt) {
		if (getRenderedDesabilitaMenu() == false) {
			setRenderedDesabilitaMenu(true);
		} else {
			setRenderedDesabilitaMenu(false);
		}
		atualizarSessao();
	}

	
	public void alterarLancaAndamentoAction(ActionEvent evt) {
		setIdAndamentoModelo(null);
		atualizarSessao();
	}
	
	
	// ************************ METHODS ************************

	/**
	 * Método responsável pela pesquisa dos modelos existentes.
	 */
	public void pesquisarModelos() {

		@SuppressWarnings("deprecation")
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();
		List<ModeloComunicacao> lista = new ArrayList<ModeloComunicacao>();

		try {
			if (!isUsuarioInstitucional() && !isUsuarioMaster() && idTipoPermissao == null) {
				lista = modeloComunicacaoService.pesquisar(nomeModeloProcura, idTipoComunicacao,
						getSetorUsuarioAutenticado(), Flag.SIM);
			} else {
				lista = modeloComunicacaoService.pesquisar(nomeModeloProcura, idTipoComunicacao, idTipoPermissao,
						Flag.SIM);
			}

		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar os modelos");
			e.printStackTrace();
		}

		if (CollectionUtils.isVazia(lista)) {
			reportarAviso("Nenhum modelo encontrado");
		}

		setListaModelos(getCheckableDataTableRowWrapperList(lista));
	}

	/**
	 * Método para limpar os atributos da tela
	 */
	public void limparTela() {
		setRenderedPesquisa(false);
		setRenderedAlterarModelo(false);
		setModeloCom(new ModeloComunicacao());
		setNomeModeloProcura("");
		setIdTipoComunicacao(null);
		setIdTipoPermissao(null);
		setListaModelos(null);
	}

	/**
	 * Método responsável em rederizar os atributos de preenchimento do novo modelo na tela
	 */
	public void renderizaTelaNovoModelo() {
		setRenderedPesquisa(true);
		setRenderedAlterarModelo(false);
		setListaModelos(null);
		setNomeModelo(nomeModeloProcura);		
		atualizarSessao();
	}

	/**
	 * Método reponsável pela exclusão lógica do modelo
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void excluirModelos() {
		@SuppressWarnings("deprecation")
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();
		modeloCom = (ModeloComunicacao) ((CheckableDataTableRowWrapper) tabelaModelos.getRowData()).getWrappedObject();

		if ((modeloCom.getTipoPermissao().getId() == TipoPermissaoModeloComunicacao.CODIGO_PERMISSAO_INSTITUCIONAL)
				&& (!isUsuarioInstitucional()) && (!isUsuarioMaster())) {
			reportarAviso("Usuário sem permissão para excluir modelos institucionais");
			return;
		}

		modeloCom.setFlagAtivo(Flag.NAO);

		try {
			modeloComunicacaoService.salvar(modeloCom);
			listaModelos.remove(modeloCom);
			setAtributo(LISTAMODELOS, listaModelos);
			reportarAviso("Modelo removido com sucesso!");
		} catch (ServiceException se) {
			se.printStackTrace();
			reportarErro("Erro ao remover o modelo.");
		}
		pesquisarModelos();
	}

	/**
	 * Método responsável em editar os modelos elaborados pela Secretaria Judiciária.
	 */
	public String editarModelo() {
		getComunicacaoServiceLocal().atualizarSessao();

		modeloCom = (ModeloComunicacao) ((CheckableDataTableRowWrapper) tabelaModelos.getRowData()).getWrappedObject();
		RequisicaoAbrirModelo requisicaoAbrirModelo = new RequisicaoAbrirModelo(modeloCom);

		if (modeloCom.getTipoPermissao().getId() == TipoPermissaoModeloComunicacao.CODIGO_PERMISSAO_INSTITUCIONAL
				&& (!isUsuarioInstitucional() && !isUsuarioMaster())) {
			reportarAviso("Usuário sem permissão para editar modelos institucionais");
			return "";
		}

		requisicaoAbrirModelo.setArquivoEletronico(modeloCom.getArquivoEletronico());
		requisicaoAbrirModelo.setDocumento(modeloCom.getArquivoEletronico().getConteudo());
		requisicaoAbrirModelo.setNomeDocumento("Modelo " + modeloCom.getTipoComunicacao().getDescricao());
		requisicaoAbrirModelo.setReadOnly(false);
		requisicaoAbrirModelo.setGerarPDF(false);
		requisicaoAbrirModelo.setTipoSalvar(ArgumentosAbrirDocumento.TIPO_ACAO_SALVAR_SERVIDOR);

		setRequestValue(RequisicaoJnlp.REQUISICAO_JNLP, requisicaoAbrirModelo);

		return "stfOfficeServlet";
	}

	/**
	 * Cria os novos modelos
	 * @throws ModeloNaoEncontradoException 
	 */
	@SuppressWarnings("deprecation")
	public String criarNovoModelo() throws ModeloNaoEncontradoException {
		RequisicaoAbrirModelo req = new RequisicaoAbrirModelo();
		
		try {
			
			if ((idSetorDestino == null || idSetorDestino == 0L) && (flagAssinaturaMinistro)) {
				reportarAviso("É necessário informar o Setor de Destino para os documentos que necessitam de assinatura do Ministro.");
				return null;
			}
					
			if(nomeModelo.equals("")){
				reportarAviso("Insira um nome ao criar um novo modelo.");
				return null;
			}
			
			req.setNomeDocumento(nomeModelo);
			// req.setArquivoEletronico(arquivo);
			req.setDocumento(recuperaModeloPadrao());
			req.setGerarPDF(false);
			req.setReadOnly(false);
			req.setTipoSalvar(ArgumentosAbrirDocumento.TIPO_ACAO_SALVAR_SERVIDOR);

			req.setIdSetorDestino(idSetorDestino);

			if (idTipoComunicacaoAlteracao == null) {
				reportarAviso("Selecione um tipo de modelo.");
				return null;
			} else {
				req.setTipoComunicacao(getTipoComunicacaoService().recuperarPorId(idTipoComunicacaoAlteracao));
	
			}

			if (idAndamentoModelo == null && !flagSemAndamento) {
				reportarAviso("Selecione um andamento.");
				return null;
			} else {
				if(!flagSemAndamento) {
					req.setAndamentoModelo(getAndamentoService().recuperarPorId(idAndamentoModelo));
				}
			}

			if (idTipoPermissaoAlteracao == null) {
				reportarAviso("Selecione um tipo de permissão para o modelo.");
				return null;
			} else {
				req.setTipoPermissaoModeloComunicacao(getTipoPermissaoModeloComunicacaoService().recuperarPorId(
							idTipoPermissaoAlteracao));
			}

			if (flagAssinaturaMinistro) {
				req.setFlagAssinaturaMinistro(Flag.SIM);
			} else {
				req.setFlagAssinaturaMinistro(Flag.NAO);
			}

			if (flagPartes) {
				req.setFlagPartes(Flag.SIM);
			} else {
				req.setFlagPartes(Flag.NAO);
			}

			if (flagPecas) {
				req.setFlagPecaProcessoEletronico(Flag.SIM);
			} else {
				req.setFlagPecaProcessoEletronico(Flag.NAO);
			}

			if (flagDuasAssinaturas) {
				req.setFlagDuasAssinatura(Flag.SIM);
			} else {
				req.setFlagDuasAssinatura(Flag.NAO);
			}

			if (flagProcessoLote) {
				req.setFlagProcessoLote(Flag.SIM);
			} else {
				req.setFlagProcessoLote(Flag.NAO);
			}

			if (flagRestringeAcessoPeca) {
				req.setFlagRestringePeca("I");
			} else {
				req.setFlagRestringePeca("P");
			}

			if (flagEncaminharParaDJe) {
				req.setFlagEncaminharParaDJe(Flag.SIM);
			} else {
				req.setFlagEncaminharParaDJe(Flag.NAO);
			}
			
			if (flagAlterarObsAndamento){
				req.setFlagAlterarObsAndamento(Flag.SIM);
			}else{
				req.setFlagAlterarObsAndamento(Flag.NAO);
			}

			if (flagJuntadaPecaProc){
				req.setFlagJuntadaPecaProc(Flag.SIM);
			}else{
				req.setFlagJuntadaPecaProc(Flag.NAO);
			}

			
			if (flagSemAndamento){
				req.setFlagSemAndamento(Flag.SIM);
			}else{
				req.setFlagSemAndamento(Flag.NAO);
			}

			
			
			req.setFlgAtivo(Flag.SIM);

			// seta o campo caso o modelo possua tags livres de preenchimento
			if (flagCampoLivre) {
				req.setFlagCampoLivre(Flag.SIM);
			} else {
				req.setFlagCampoLivre(Flag.NAO);
			}

			if (flagAssinaturaMinistro && (idSetorDestino == null || idSetorDestino == 0L)) {
				reportarAviso("Selecione um setor de saída.");
				return null;
			}

			setRequestValue(RequisicaoJnlpDocumento.REQUISICAO_JNLP, req);

			return "stfOfficeServlet";
		} catch (Exception e) {
			reportarErro("Erro ao criar modelo.", e, LOG);
			throw new ModeloNaoEncontradoException(e);
		}
	}

	/**
	 * Método responsável em alterar os dados do modelo
	 */
	public void alterarModelo() {

		modeloCom = (ModeloComunicacao) ((CheckableDataTableRowWrapper) tabelaModelos.getRowData()).getWrappedObject();

		setNomeModelo(modeloCom.getDscModelo());

		if (modeloCom.getFlagPartes().equals(FlagGenericaModeloComunicacao.N)) {
			setFlagPartes(false);
		} else {
			setFlagPartes(true);
		}

		if (modeloCom.getFlagAssinaturaMinistro().equals(FlagGenericaModeloComunicacao.N)) {
			setFlagAssinaturaMinistro(false);
			setRenderedDesabilitaMenu(true);
		} else {
			setFlagAssinaturaMinistro(true);
			setRenderedDesabilitaMenu(false);
		}

		if (modeloCom.getFlagCampoLivre().equals(FlagGenericaModeloComunicacao.N)) {
			setFlagCampoLivre(false);
		} else {
			setFlagCampoLivre(true);
		}

		if (modeloCom.getFlagVinculoPecaProcessoElet().equals(FlagGenericaModeloComunicacao.N)) {
			setFlagPecas(false);
		} else {
			setFlagPecas(true);
		}

		if (modeloCom.getFlagDuasAssinaturas().equals(FlagGenericaModeloComunicacao.N)) {
			setFlagDuasAssinaturas(false);
		} else {
			setFlagDuasAssinaturas(true);
		}

		if (modeloCom.getFlagProcessoLote().equals(FlagGenericaModeloComunicacao.N)) {
			setFlagProcessoLote(false);
		} else {
			setFlagProcessoLote(true);
		}

		// verifica se a peça do documento está sinalizada como pública ou não
		if (modeloCom.getFlagTipoAcessoDocumentoPeca().equals(FlagGenericaAcessoDocumento.P)) {
			setFlagRestringeAcessoPeca(false);
		} else {
			setFlagRestringeAcessoPeca(true);
		}

		if (modeloCom.getFlagEncaminharParaDJe() == FlagGenericaModeloComunicacao.N) {
			setFlagEncaminharParaDJe(Boolean.FALSE);
		} else {
			setFlagEncaminharParaDJe(Boolean.TRUE);
		}
		
		if (modeloCom.getFlagAlterarObsAndamento() == FlagGenericaModeloComunicacao.N) {
			setFlagAlterarObsAndamento(Boolean.FALSE);
		} else {
			setFlagAlterarObsAndamento(Boolean.TRUE);
		}

		if (modeloCom.getFlagJuntadaPecaProc() == FlagGenericaModeloComunicacao.N) {
			setFlagJuntadaPecaProc(Boolean.FALSE);
		} else {
			setFlagJuntadaPecaProc(Boolean.TRUE);
		}
		
		if (modeloCom.getFlagSemAndamento() == FlagGenericaModeloComunicacao.N) {
			setFlagSemAndamento(Boolean.FALSE);
		} else {
			setFlagSemAndamento(Boolean.TRUE);
		}
		
		
		setIdTipoPermissaoAlteracao(modeloCom.getTipoPermissao() != null ? modeloCom.getTipoPermissao().getId() : null);
		setIdTipoComunicacaoAlteracao(modeloCom.getTipoComunicacao() != null ? modeloCom.getTipoComunicacao().getId()
				: null);
		setIdSetorDestino(modeloCom.getSetorDestino() != null ? modeloCom.getSetorDestino().getId() : null);
		setIdAndamentoModelo(modeloCom.getAndamento() != null ? modeloCom.getAndamento().getId() : null);

		setRenderedPesquisa(true);
		setRenderedAlterarModelo(true);
	}

	/**
	 * Método responsável em salvar as alterações em modelo já criado.
	 */
	@SuppressWarnings("deprecation")
	public void salvarModeloAlterado() {

		if (modeloCom == null) {
			reportarAviso("Selecione um modelo.");
		}

		TipoComunicacaoService tipoComunicacaoService = getTipoComunicacaoService();
		SetorService setorService = getSetorService();
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();
		AndamentoService andamentoService = getAndamentoService();
		TipoPermissaoModeloComunicacaoService permissaoService = getTipoPermissaoModeloComunicacaoService();

		if(nomeModelo.equals("")){
			reportarAviso("Insira um nome ao alterar um modelo.");
			return ;
		}
		modeloCom.setDscModelo(nomeModelo);

		if (flagAssinaturaMinistro) {
			modeloCom.setFlagAssinaturaMinistro(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagAssinaturaMinistro(FlagGenericaModeloComunicacao.N);
		}

		if (flagCampoLivre) {
			modeloCom.setFlagCampoLivre(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagCampoLivre(FlagGenericaModeloComunicacao.N);
		}

		if (flagPartes) {
			modeloCom.setFlagPartes(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagPartes(FlagGenericaModeloComunicacao.N);
		}

		if (flagPecas) {
			modeloCom.setFlagVinculoPecaProcessoElet(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagVinculoPecaProcessoElet(FlagGenericaModeloComunicacao.N);
		}

		if (flagDuasAssinaturas) {
			modeloCom.setFlagDuasAssinaturas(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagDuasAssinaturas(FlagGenericaModeloComunicacao.N);
		}

		if (flagProcessoLote) {
			modeloCom.setFlagProcessoLote(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagProcessoLote(FlagGenericaModeloComunicacao.N);
		}

		if (flagRestringeAcessoPeca) {
			modeloCom.setFlagTipoAcessoDocumentoPeca(FlagGenericaAcessoDocumento.I);
		} else {
			modeloCom.setFlagTipoAcessoDocumentoPeca(FlagGenericaAcessoDocumento.P);
		}

		if (flagEncaminharParaDJe) {
			modeloCom.setFlagEncaminharParaDJe(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagEncaminharParaDJe(FlagGenericaModeloComunicacao.N);
		}
		
		if (flagAlterarObsAndamento) {
			modeloCom.setFlagAlterarObsAndamento(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagAlterarObsAndamento(FlagGenericaModeloComunicacao.N);
		}

		if (flagSemAndamento) {
			modeloCom.setFlagSemAndamento(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagSemAndamento(FlagGenericaModeloComunicacao.N);
		}
		
		if (flagJuntadaPecaProc) {
			modeloCom.setFlagJuntadaPecaProc(FlagGenericaModeloComunicacao.S);
		} else {
			modeloCom.setFlagJuntadaPecaProc(FlagGenericaModeloComunicacao.N);
		}
		
		
		TipoComunicacao tipoCo = null;
		Setor setor = null;
		Andamento andamento = null;
		TipoPermissaoModeloComunicacao tipoPermissao = null;

		if (idTipoPermissaoAlteracao == 0L) {
			reportarErro("Selecione um tipo de permissão.");
			return;
		}

		try {
			tipoPermissao = permissaoService.recuperarPorId(idTipoPermissaoAlteracao);
		} catch (ServiceException e) {
			e.printStackTrace();

		}

		if (idTipoComunicacaoAlteracao == 0L) {
			reportarErro("Selecione um tipo de modelo.");
			return;
		}

		try {
			tipoCo = tipoComunicacaoService.recuperarPorId(idTipoComunicacaoAlteracao);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao pesquisa o tipo de modelo.");
		}

		if ((idSetorDestino == null || idSetorDestino == 0L)
				&& modeloCom.getFlagAssinaturaMinistro().equals(FlagGenericaModeloComunicacao.S)) {
			reportarErro("Selecione um setor de destino.");
			return;
		}

		if (idSetorDestino != null && idSetorDestino > 0L) {
			try {
				setor = setorService.recuperarPorId(idSetorDestino);

			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao pesquisa o tipo de modelo.");
			}
		}

		if ((idAndamentoModelo == null || idAndamentoModelo == 0L ) && !flagSemAndamento ) {
			reportarErro("Selecione um andamento.");
			return;
		}

		try {
			andamento = andamentoService.recuperarPorId(idAndamentoModelo);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao pesquisar o andamento selecionado.");
		}

		modeloCom.setTipoPermissao(tipoPermissao);
		modeloCom.setTipoComunicacao(tipoCo);
		modeloCom.setSetorDestino(setor);
		modeloCom.setAndamento(andamento);

		try {
			modeloComunicacaoService.salvar(modeloCom);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao alterar o modelo.");
		}
		setRenderedPesquisa(false);
		reportarAviso("Modelo alterado com sucesso.");

	}

	public void fecharModelo() {
		// setRenderedPesquisa(false);
		setRenderedAlterarModelo(false);
		atualizarSessao();
	}


	@SuppressWarnings("deprecation")
	private List<SelectItem> pesquisarModeloPorPermissao(Long idPermissao) {
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();
		List<ModeloComunicacao> listaModelo = new ArrayList<ModeloComunicacao>();

		try {
			listaModelo = modeloComunicacaoService.pesquisar(null, null, idPermissao, Flag.SIM);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao carregar a lista de modelos.");
		}

		List<SelectItem> listaModeloSelectItem = carregarComboTipoModelosPelaListaDeModelos(listaModelo);

		return listaModeloSelectItem;
	}

	/**
	 * Método responsável em buscar o modelo padrão localizado no classpath
	 * 
	 * @throws ModeloNaoEncontradoException
	 */
	public byte[] recuperaModeloPadrao() throws ModeloNaoEncontradoException {
		try {
			ClassPathTextoSource modeloClassPath = new ClassPathTextoSource("modelo.odt");
			return modeloClassPath.getByteArray();
		} catch (IOException e) {
			throw new ModeloNaoEncontradoException("Erro ao carregar o modelo do classpath", e);
		}
	}
	
	public void procurarTiposModelosPeloTipoPermissaoAction(ActionEvent evt) {
		setItensTiposModelos(carregarComboTipoModelos());
		atualizarSessao();
	}

	// **************************** GET & SET ******************* //

	public Long getIdTipoPermissao() {
		return idTipoPermissao;
	}

	public void setIdTipoPermissao(Long idTipoPermissao) {
		this.idTipoPermissao = idTipoPermissao;
	}

	public Long getIdTipoComunicacao() {
		return idTipoComunicacao;
	}

	public void setIdTipoComunicacao(Long idTipoComunicacao) {
		this.idTipoComunicacao = idTipoComunicacao;
	}

	public Long getIdMinistros() {
		return idMinistros;
	}

	public void setIdMinistros(Long idMinistros) {
		this.idMinistros = idMinistros;
	}

	public List<SelectItem> getItensAndamentoModelo() {
		return itensAndamentoModelo;
	}

	public void setItensAndamentoModelo(List<SelectItem> itensAndamentoModelo) {
		this.itensAndamentoModelo = itensAndamentoModelo;
	}

	public List<SelectItem> getItensTiposPermissoes() {
		return itensTiposPermissoes;
	}

	public void setItensTiposPermissoes(List<SelectItem> itensTiposPermissoes) {
		this.itensTiposPermissoes = itensTiposPermissoes;
	}

	public List<SelectItem> getItensTiposModelos() {
		return itensTiposModelos;
	}

	public void setItensTiposModelos(List<SelectItem> itensTiposModelos) {
		this.itensTiposModelos = itensTiposModelos;
	}

	public List<SelectItem> getItensMinistros() {
		return itensMinistros;
	}

	public void setItensMinistros(List<SelectItem> itensMinistros) {
		this.itensMinistros = itensMinistros;
	}

	public List<CheckableDataTableRowWrapper> getListaModelos() {
		return listaModelos;
	}

	public void setListaModelos(List<CheckableDataTableRowWrapper> listaModelos) {
		this.listaModelos = listaModelos;
	}

	public HtmlDataTable getTabelaModelos() {
		return tabelaModelos;
	}

	public void setTabelaModelos(HtmlDataTable tabelaModelos) {
		this.tabelaModelos = tabelaModelos;
	}

	public String getNomeModelo() {
		return nomeModelo;
	}

	public void setNomeModelo(String nomeModelo) {
		this.nomeModelo = nomeModelo;
	}

	public String getNomeModeloProcura() {
		return nomeModeloProcura;
	}

	public void setNomeModeloProcura(String nomeModeloProcura) {
		this.nomeModeloProcura = nomeModeloProcura;
	}

	public ModeloComunicacao getModeloCom() {
		return modeloCom;
	}

	public void setModeloCom(ModeloComunicacao modeloCom) {
		this.modeloCom = modeloCom;
	}

	public Boolean getFlagAssinaturaMinistro() {
		return flagAssinaturaMinistro;
	}

	public void setFlagAssinaturaMinistro(Boolean flagAssinaturaMinistro) {
		this.flagAssinaturaMinistro = flagAssinaturaMinistro;
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

	public Boolean getRenderedPesquisa() {
		return renderedPesquisa;
	}

	public void setRenderedPesquisa(Boolean renderedPesquisa) {
		this.renderedPesquisa = renderedPesquisa;
	}

	public Boolean getRenderedAlterarModelo() {
		return renderedAlterarModelo;
	}

	public void setRenderedAlterarModelo(Boolean renderedAlterarModelo) {
		this.renderedAlterarModelo = renderedAlterarModelo;
	}

	public Boolean getRenderedDesabilitaMenu() {
		return renderedDesabilitaMenu;
	}

	public void setRenderedDesabilitaMenu(Boolean renderedDesabilitaMenu) {
		this.renderedDesabilitaMenu = renderedDesabilitaMenu;
	}

	public Boolean getFlagCampoLivre() {
		return flagCampoLivre;
	}

	public void setFlagCampoLivre(Boolean flagCampoLivre) {
		this.flagCampoLivre = flagCampoLivre;
	}

	public Boolean getFlagPartes() {
		return flagPartes;
	}

	public void setFlagPartes(Boolean flagPartes) {
		this.flagPartes = flagPartes;
	}

	public Boolean getFlagPecas() {
		return flagPecas;
	}

	public void setFlagPecas(Boolean flagPecas) {
		this.flagPecas = flagPecas;
	}

	public Boolean getFlagDuasAssinaturas() {
		return flagDuasAssinaturas;
	}

	public void setFlagDuasAssinaturas(Boolean flagDuasAssinaturas) {
		this.flagDuasAssinaturas = flagDuasAssinaturas;
	}

	public Boolean getFlagProcessoLote() {
		return flagProcessoLote;
	}

	public void setFlagProcessoLote(Boolean flagProcessoLote) {
		this.flagProcessoLote = flagProcessoLote;
	}

	public Boolean getFlagRestringeAcessoPeca() {
		return flagRestringeAcessoPeca;
	}

	public void setFlagRestringeAcessoPeca(Boolean flagRestringeAcessoPeca) {
		this.flagRestringeAcessoPeca = flagRestringeAcessoPeca;
	}

	public Boolean getFlagEncaminharParaDJe() {
		return flagEncaminharParaDJe;
	}

	public void setFlagEncaminharParaDJe(Boolean flagEncaminharParaDJe) {
		this.flagEncaminharParaDJe = flagEncaminharParaDJe;
	}

	public Long getIdAndamentoModelo() {
		return idAndamentoModelo;
	}

	public void setIdAndamentoModelo(Long idAndamentoModelo) {
		this.idAndamentoModelo = idAndamentoModelo;
	}

	public List<SelectItem> getItensTiposPermissoesAlteracao() {
		return itensTiposPermissoesAlteracao;
	}

	public void setItensTiposPermissoesAlteracao(List<SelectItem> itensTiposPermissoesAlteracao) {
		this.itensTiposPermissoesAlteracao = itensTiposPermissoesAlteracao;
	}

	public List<SelectItem> getItensTiposModelosAlteracao() {
		return itensTiposModelosAlteracao;
	}

	public void setItensTiposModelosAlteracao(List<SelectItem> itensTiposModelosAlteracao) {
		this.itensTiposModelosAlteracao = itensTiposModelosAlteracao;
	}

	public Long getIdTipoPermissaoAlteracao() {
		return idTipoPermissaoAlteracao;
	}

	public void setIdTipoPermissaoAlteracao(Long idTipoPermissaoAlteracao) {
		this.idTipoPermissaoAlteracao = idTipoPermissaoAlteracao;
	}

	public Long getIdTipoComunicacaoAlteracao() {
		return idTipoComunicacaoAlteracao;
	}

	public void setIdTipoComunicacaoAlteracao(Long idTipoComunicacaoAlteracao) {
		this.idTipoComunicacaoAlteracao = idTipoComunicacaoAlteracao;
	}

	public Boolean getFlagAlterarObsAndamento() {
		return flagAlterarObsAndamento;
	}

	public void setFlagAlterarObsAndamento(Boolean flagAlterarObsAndamento) {
		this.flagAlterarObsAndamento = flagAlterarObsAndamento;
	}


	public Boolean getFlagSemAndamento() {
		return flagSemAndamento;
	}

	public void setFlagSemAndamento(Boolean flagSemAndamento) {
		this.flagSemAndamento = flagSemAndamento;
	}


	public Boolean getFlagJuntadaPecaProc() {
		return flagJuntadaPecaProc;
	}

	public void setFlagJuntadaPecaProc(Boolean flagJuntadaPecaProc) {
		this.flagJuntadaPecaProc = flagJuntadaPecaProc;
	}


	// ------------------------------ EXCEPTION ------------------------------
	// //
	private class ModeloNaoEncontradoException extends Exception {

		private static final long serialVersionUID = 3577289844020998869L;

		@SuppressWarnings("unused")
		public ModeloNaoEncontradoException() {
			super();
		}

		public ModeloNaoEncontradoException(String message, Throwable cause) {
			super(message, cause);
		}

		@SuppressWarnings("unused")
		public ModeloNaoEncontradoException(String message) {
			super(message);
		}

		public ModeloNaoEncontradoException(Throwable cause) {
			super(cause);
		}
	}
}
