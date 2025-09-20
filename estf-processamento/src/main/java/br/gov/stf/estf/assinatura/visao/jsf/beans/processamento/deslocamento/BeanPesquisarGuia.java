package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.Hibernate;

import br.gov.stf.estf.assinatura.deslocamento.origemdestino.ResultSuggestionOrigemDestino;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.PeticaoParser;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.PropertyComparator;
import br.gov.stf.estf.assinatura.visao.util.TipoOrdenacao;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao.DeslocaPeticaoId;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanPesquisarGuia extends AssinadorBaseBean {
	
	/**
	 * bean da pesquisarGuia.jsp: realiza pesquisa de guias e detalha a guia com possibilidade de impressão, 
	 * alteração do destinatário, observação, bem como, possilita a inclusão e exclusão de itens da guia.
	 * Para o detalhamento com a posibilidade de edição conforme descrito acima a guia deverá ter sido criada
	 * pelo mesmo usuário e deve estar em trânsito. 
	 */
	public static final Object LISTA_GUIAS = new Object();
	public static final Object NUM_GUIA = new Object();
	public static final Object DESCRICAO_DESTINO = new Object();
	public static final Object TIPO_GUIA = new Object();
	public static final Object DESCRICAO_GUIA =  new Object();
	public static final Object RESPOSTA_EXCLUIR_GUIA = new Object();
	public static final Object TEM_PERMISSAO_ALTERAR = new Object();
	public static final Long COD_SETOR_COMPOSICAO_E_CONTROLE_DE_ACORDAOS = (long) 600000903;
	public static final Long COD_SETOR_BAIXA_E_EXPEDICAO = (long) 600000902;

	private static final long serialVersionUID = 1L;
	private static List<String> classes;
	private static final String RECEBER = "A receber";
	private static final String RECEBIDA = "Guia recebida";
	private static final String RECEBIDA_PARCIAL = "Guia recebida parcialmente";
	private static final String EM_TRANSITO = "Em Trânsito";
	private static final int LIMITE_TENTATIVA = 20;
	private static final Object ANO_GUIA = new Object();
	private static final Object DATA_ENVIO = new Object();
	private static final Object PETICAO_PROCESSO = new Object();
	private static final Object DESCRICAO_LOTACAO = new Object();
	private static final Object CODIGO_LOTACAO = new Object();
	private static final Object CODIGO_DESCRICAO_LOTACAO = new Object();
	private static final Object CHECK_ORIGEM_SETOR_USUARIO = new Object();
	private static final Object CHECK_DESTINO_SETOR_USUARIO = new Object();
	private static final Object GUIA = new Object();
	private static final Object LISTA_PROCESSOS_DA_GUIA = new Object();
	private static final Object LISTA_PETICOES_DA_GUIA = new Object();
	private static final Object POSTAL = new Object();
	private static final Object CODIGO_NOVO_DESTINO_POPUP = new Object();
	private static final Object TIPO_NOVO_DESTINO_POPUP = new Object();
	private static final Object ANO_GUIA_POPUP = new Object();
	private static final Object CODIGO_ORIGEM_POPUP = new Object();
	private static final Object CODIGO_DESTINO_POPUP = new Object();
	private static final Object DESCRICAO_ORIGEM_POPUP = new Object();
	private static final Object DESCRICAO_DESTINO_POPUP = new Object();
	private static final Object OBSERVACAO_POPUP = new Object();

	private String numGuia;
	private String anoGuia;
	private String descricaoGuia;
	private String identificacaoProcessoPeticao;
	private Date dataEnvio;
	private String peticaoProcesso;
	private String tipoGuia;
	private String descricaoDestino;
	private String descricaoOrigem;
	private Long codigoDestino;
	private Long codigoOrigem;
	private List<CheckableDataTableRowWrapper> listaGuias;
	private org.richfaces.component.html.HtmlDataTable tabelaGuias;
	private org.richfaces.component.html.HtmlDataTable tabelaProcessos;
	private org.richfaces.component.html.HtmlDataTable tabelaPeticoes;
	private Boolean chkDestinoLotacaoUsuario;
	private Boolean chkOrigemLotacaoUsuario;

	private String descricaoLotacao;
	private Long codigoLotacao;
	private String codigoDescricaoLotacao;
	private String hintSituacaoGuia;

	// dados para pesquisa da guia
	private String anoPeticao;
	private String numeroPeticao;
	private String siglaProcesso;
	private String numeroProcesso;
	private boolean guiaRecebida;
	private boolean isProcesso;
	private boolean isPeticao;
	private boolean pesquisarTodos;
	// dados para a popup de edição de guia
	private Guia guia;
	private org.richfaces.component.html.HtmlModalPanel popupEditarGuia;
	private String mensagemAvisoPopup;
	private String mensagemErroPopup;
	private String mensagemInfoPopup;
	private Long numeroGuiaPopup;
	private Short anoGuiaPopup;
	private Long codigoOrigemPopup;
	private String descricaoOrigemPopup;
	private Long codigoDestinoPopup;
	private String descricaoDestinoPopup;
	private Long codigoNovoDestinoPopup;
	private String descricaoNovoDestinoPopup;
	private Short tipoNovoDestinoPopup;
	private String observacaoPopup;
	private List<DeslocaProcesso> listaProcessosDaGuia;
	private List<DeslocaPeticao> listaPeticoesDaGuia;
	private Integer quantidadeVinculados; // apensos
	private Boolean postal;
	private Short tipoDestino;
	private String idProcessoPeticaoPopup;
	private String respostaExcluirGuia;
	private Boolean temPermissaoAlterar;

	
	public BeanPesquisarGuia() throws ServiceException {
		restaurarSessao();
	}
	
	private void atualizarSessao() {
		setAtributo(GUIA, guia);
		setAtributo(NUM_GUIA, numGuia);
		setAtributo(ANO_GUIA, anoGuia);
		setAtributo(DATA_ENVIO, dataEnvio);
		setAtributo(PETICAO_PROCESSO, peticaoProcesso);
		setAtributo(DESCRICAO_DESTINO, descricaoDestino);
		setAtributo(LISTA_GUIAS, listaGuias);
		setAtributo(DESCRICAO_LOTACAO, descricaoLotacao);
		setAtributo(CODIGO_LOTACAO, codigoLotacao);
		setAtributo(CODIGO_DESCRICAO_LOTACAO, codigoDescricaoLotacao);
		setAtributo(TIPO_GUIA, tipoGuia);
		setAtributo(CHECK_ORIGEM_SETOR_USUARIO, chkOrigemLotacaoUsuario);
		setAtributo(CHECK_DESTINO_SETOR_USUARIO, chkDestinoLotacaoUsuario);
		setAtributo(DESCRICAO_GUIA, descricaoGuia);
		setAtributo(TIPO_GUIA, tipoGuia);
		setAtributo(LISTA_PROCESSOS_DA_GUIA, listaProcessosDaGuia);
		setAtributo(LISTA_PETICOES_DA_GUIA, listaPeticoesDaGuia);
		setAtributo(POSTAL, postal);
		setAtributo(CODIGO_NOVO_DESTINO_POPUP, codigoNovoDestinoPopup);
		setAtributo(TIPO_NOVO_DESTINO_POPUP, tipoNovoDestinoPopup);
		setAtributo(ANO_GUIA_POPUP, anoGuiaPopup);
		setAtributo(CODIGO_ORIGEM_POPUP, codigoOrigemPopup);
		setAtributo(CODIGO_DESTINO_POPUP, codigoDestinoPopup);
		setAtributo(DESCRICAO_ORIGEM_POPUP, descricaoOrigemPopup);
		setAtributo(DESCRICAO_DESTINO_POPUP, descricaoDestinoPopup);
		setAtributo(OBSERVACAO_POPUP, observacaoPopup);
		setAtributo(RESPOSTA_EXCLUIR_GUIA, respostaExcluirGuia);
		setAtributo(TEM_PERMISSAO_ALTERAR, temPermissaoAlterar);
	}
	
	public Long recuperarCodigoSetorUsuario() throws ServiceException {
		return getSetorUsuarioAutenticado().getId();
	}
	
	public String recuperarSetorUsuario() throws ServiceException {
		return getSetorUsuarioAutenticado().getNome();
	}

	private Integer obterAnoCorrente() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
	
	@SuppressWarnings("unchecked")
	private void restaurarSessao() throws ServiceException {
		Integer anoCorrente = obterAnoCorrente();

		if (getAtributo(LISTA_PROCESSOS_DA_GUIA) == null) {
			setAtributo(LISTA_PROCESSOS_DA_GUIA, new ArrayList<DeslocaProcesso>());
		} 
		setListaProcessosDaGuia((List<DeslocaProcesso>) getAtributo(LISTA_PROCESSOS_DA_GUIA));

		if (getAtributo(LISTA_PETICOES_DA_GUIA) == null) {
			setAtributo(LISTA_PETICOES_DA_GUIA, new ArrayList<DeslocaPeticao>());
		} 
		setListaPeticoesDaGuia((List<DeslocaPeticao>) getAtributo(LISTA_PETICOES_DA_GUIA));

		if (getAtributo(GUIA) == null) {
			setAtributo(GUIA, new Guia());
		} 
		setGuia((Guia) getAtributo(GUIA));

		if (getAtributo(LISTA_GUIAS) == null) {
			setAtributo(LISTA_GUIAS, new ArrayList<CheckableDataTableRowWrapper>());
		} else {
			setListaGuias((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_GUIAS));
		}

		if (getAtributo(ANO_GUIA) == null) {
			setAnoGuia(anoCorrente.toString());
		} else {
			setAnoGuia((getAtributo(ANO_GUIA).toString()));
		}

		if (getAtributo(DESCRICAO_LOTACAO) == null) {
			setDescricaoLotacao(recuperarSetorUsuario());
		} else {
			setDescricaoLotacao((String) getAtributo(DESCRICAO_LOTACAO));
		}

		if (getAtributo(TIPO_GUIA) == null) {
			setAtributo(TIPO_GUIA, "PRO");
			setAtributo(DESCRICAO_GUIA, "Processo");
		}

		if (getAtributo(CODIGO_LOTACAO) == null) {
			setCodigoLotacao(recuperarCodigoSetorUsuario());
			setAtributo(CODIGO_LOTACAO, codigoLotacao);
		} else {
			setCodigoLotacao((Long) getAtributo(CODIGO_LOTACAO));
		}

		codigoDescricaoLotacao = codigoLotacao + " - " + descricaoLotacao;
		setCodigoDescricaoLotacao(codigoDescricaoLotacao);
		setNumGuia((String) getAtributo(NUM_GUIA));
		setDataEnvio((Date) getAtributo(DATA_ENVIO));
		setPeticaoProcesso((String) getAtributo(PETICAO_PROCESSO));
		setDescricaoDestino((String) getAtributo(DESCRICAO_DESTINO));
		setTipoGuia((String) getAtributo(TIPO_GUIA));
		setDescricaoGuia((String) getAtributo(DESCRICAO_GUIA));
		setCodigoNovoDestinoPopup((Long) getAtributo(CODIGO_NOVO_DESTINO_POPUP));
		setTipoNovoDestinoPopup((Short) getAtributo(TIPO_NOVO_DESTINO_POPUP));

		if (getAtributo(CHECK_ORIGEM_SETOR_USUARIO) == null) {
			setChkOrigemLotacaoUsuario(Boolean.FALSE);
		} else {
			setChkOrigemLotacaoUsuario((Boolean) getAtributo(CHECK_ORIGEM_SETOR_USUARIO));
		}

		if (getAtributo(CHECK_DESTINO_SETOR_USUARIO) == null) {
			setChkDestinoLotacaoUsuario(Boolean.FALSE);
		} else {
			setChkDestinoLotacaoUsuario((Boolean) getAtributo(CHECK_DESTINO_SETOR_USUARIO));
		}
		setPostal((Boolean) getAtributo(POSTAL));
		if (getAtributo(RESPOSTA_EXCLUIR_GUIA) == null) {
			setAtributo(RESPOSTA_EXCLUIR_GUIA, "N");
		}
		setRespostaExcluirGuia((String) getAtributo(RESPOSTA_EXCLUIR_GUIA));
		
		if (getAtributo(TEM_PERMISSAO_ALTERAR) == null) {
			setTemPermissaoAlterar(Boolean.FALSE);
		} else {
			setTemPermissaoAlterar((Boolean) getAtributo(TEM_PERMISSAO_ALTERAR));
		}
	}
	
	public Boolean getTemMensagemInfoPopup() {
		if (getMensagemInfoPopup() == null || getMensagemInfoPopup().trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}
	public Boolean getTemMensagemAvisoPopup() {
		if (getMensagemAvisoPopup() == null || getMensagemAvisoPopup().trim().equals("")) {
			return false;
		} else {
			return true;
		}
		
	}
	public Boolean getTemMensagemErroPopup() {
		if (getMensagemErroPopup() == null || getMensagemErroPopup().trim().equals("")) {
			return false;
		} else {
			return true;
		}
		
	}
	
	public Boolean getDesabilitarAlteracaoDestino() {
		if (guia == null || guia.getTipoOrgaoDestino() == null) {
			return true;
		}
		if (guia.getTipoOrgaoDestino().equals(new Integer(1))) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List pesquisarOrigemDestino(Object value) {
		List<ResultSuggestionOrigemDestino> listaDestinatarios = new ArrayList<ResultSuggestionOrigemDestino>();
		try {

			if (value == null) {
				return null;
			}
			if (value.toString().trim() == "") {
				return null;
			}

			List<OrigemDestino> origensDestinos = getOrigemDestinoService().recuperarPorIdOuDescricao(value.toString());
			for (Object objeto : origensDestinos) {
					ResultSuggestionOrigemDestino result = new ResultSuggestionOrigemDestino();
					result.setOrigemDestino((OrigemDestino) objeto);
					listaDestinatarios.add(result);
			}

		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar a origem ou destino da guia: " + value.toString());
		}
		return listaDestinatarios;
	}
	
	@SuppressWarnings("rawtypes")
	public List pesquisarDestinatario(Object value) {
		try {

			List<ResultSuggestionOrigemDestino> listaDestinatarios = new ArrayList<ResultSuggestionOrigemDestino>();

			if (value == null) {
				return null;
			}
			if (value.toString().trim() == "") {
				return null;
			}
			
//			List<OrigemDestino> origensDestinos = new ArrayList<OrigemDestino>();
			
			if (guia.getTipoOrgaoOrigem().equals(new Integer(1))) {
				return null;
			}
			List<OrigemDestino> origensDestinos = getOrigemDestinoService().recuperarPorIdOuDescricao(value.toString(), guia.getTipoOrgaoDestino());
			for (Object objeto : origensDestinos) {
					ResultSuggestionOrigemDestino result = new ResultSuggestionOrigemDestino(); 
					result.setOrigemDestino((OrigemDestino) objeto);
					listaDestinatarios.add(result);
			}
			
			return listaDestinatarios;
			
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar a origem ou destino da guia: " + value.toString());
			return null;
		}
	}
	
	private Boolean isVazio(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof Long) {
			if (obj.equals(new Long(0))) {
				return true;
			}
		}
		if (obj instanceof String) {
			if ( obj.toString().trim().equals("") )  {
				return true;
			}
		}
		if (obj instanceof Date) {
			if ( obj.toString().trim().equals("") )  {
				return true;
			}
		}
		return false;
	}
	
	
	// habilita ou desabilita botões
	private void comportamentoBotao(boolean habilitar, String id, boolean estendido) {
		try {
			int i = 0;
			UIComponent form = FacesContext.getCurrentInstance().getViewRoot().findComponent("formPopup");
			while (form.findComponent(id) == null){
				form = form.getParent();
				i++;
				if (i > LIMITE_TENTATIVA) {
					throw new Exception("Componente de interface não localizado");
				}
			}
			if (habilitar) {
				form.findComponent(id).getAttributes().put("disabled", false);
				form.findComponent(id).getAttributes().put("styleClass", estendido?"BotaoPadraoEstendido":"BotaoPadrao");
			} else {
				form.findComponent(id).getAttributes().put("disabled", true);
				form.findComponent(id).getAttributes().put("styleClass", estendido?"BotaoPadraoEstendidoInativo":"BotaoPadraoInativo");
			}
		} catch (Exception e) {
			reportarErro("Erro ao recuperar componente de interface");
		}
	}
	
	// habilita ou desabilita caixa de texto
	private void comportamentoInput(boolean habilitar, String id) {
		try {
			int i = 0;
			UIComponent form = FacesContext.getCurrentInstance().getViewRoot().findComponent("formPopup");
			while (form.findComponent(id) == null){
				form = form.getParent();
				i++;
				if (i > LIMITE_TENTATIVA) {
					throw new Exception("Componente de interface não localizado");
				}
			}
			if(habilitar){
				form.findComponent(id).getAttributes().put("onfocus", "");				
			} else{
				form.findComponent(id).getAttributes().put("onfocus", "this.blur()");
			}
		} catch (Exception e) {
			reportarErro("Erro ao recuperar componente de interface");
		}
	}
	
	/**
	 * Retirar os caracteres incorretos lidos pela caneta ótica se houver 
	 */
	private String filtraCampoProcesso(Object value) {

		String siglaNumero = null;
		String siglaNumeroF = "";
		Long lNumero = null;
		String lNumeroPet = null;
		final int TAMANHO_MAXIMO = 14;
		
		String codigo = "";
		codigo = value.toString();
		
		if (codigo.toString().length() == TAMANHO_MAXIMO){
			String siglaNumeroTrezeCarac = codigo.substring(0, codigo.length()-1); 
			codigo = siglaNumeroTrezeCarac;
		}
		
		if (codigo != null) {
			siglaNumero = codigo.toString();
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
				reportarErro("Erro ao converter a classe");
			}
		}
		return siglaNumeroF;
	}
	
	/**
	 * Insere um processo em uma guia, chamado pela tela
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SQLException 
	 */
	private DeslocaProcesso inserirProcesso(Processo processo) throws ServiceException, SQLException {

		try {
			
			DeslocaProcesso novoDeslocaProcesso = new DeslocaProcesso();
			DeslocaProcessoId novoDeslocaProcessoId = new DeslocaProcessoId();
			novoDeslocaProcessoId.setAnoGuia(guia.getId().getAnoGuia());
			novoDeslocaProcessoId.setNumeroGuia(guia.getId().getNumeroGuia());
			novoDeslocaProcessoId.setCodigoOrgaoOrigem(guia.getId().getCodigoOrgaoOrigem());
			novoDeslocaProcessoId.setProcesso(processo);
			
			novoDeslocaProcesso.setId(novoDeslocaProcessoId);
			novoDeslocaProcesso.setCodigoOrgaoDestino(guia.getCodigoOrgaoDestino());
			novoDeslocaProcesso.setGuia(guia);
			// quantidades
			List<ProcessoDependencia> processosApensos = getProcessoDependenciaService().recuperarApensos(processo);
			if (processosApensos != null) {
				novoDeslocaProcesso.setQuantidadeApensos((short) processosApensos.size());
			}
			if (processo.getQuantidadeJuntadasLinha() == null) {
				processo.setQuantidadeJuntadasLinha((short) 0);
			}
			if (processo.getQuantidadeVolumes() == null) {
				processo.setQuantidadeVolumes((int)0); 
			}
			novoDeslocaProcesso.setQuantidadeJuntadaLinha(processo.getQuantidadeJuntadasLinha());
			novoDeslocaProcesso.setQuantidadeVolumes(processo.getQuantidadeVolumes().shortValue());
			novoDeslocaProcesso.setUltimoDeslocamento(false);
			novoDeslocaProcesso.setNumeroSequencia(getDeslocaProcessoService().recuperarUltimaSequencia(guia));
			// de acordo com os outros deslocamentos da guia mantêm o recebimento manual ou automático.
			DeslocaProcesso deslocamentoAtual = getDeslocaProcessoService().recuperarDeslocamentoProcesso(guia);
			if (deslocamentoAtual.getDataRecebimento() == null) {
				novoDeslocaProcesso.setTipoDeslocamento("DE");
				novoDeslocaProcesso.setDataRecebimento(null);
				novoDeslocaProcesso.setUltimoDeslocamento(false);
			} else {
				novoDeslocaProcesso.setTipoDeslocamento("EL");
				novoDeslocaProcesso.setDataRecebimento(new Date());
				novoDeslocaProcesso.setUltimoDeslocamento(true);
			}
			
			getObjetoIncidenteService().inserirProcessoPeticaoNaGuia(guia,	novoDeslocaProcesso);
		} catch (ServiceException e) {
			setMensagemErroPopup("Erro na inclusão do processo.");
			atualizaInformacoesPopup();
		}
		return getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
	}
	/**
	 * Insere uma petição em uma guia, chamado pela tela
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SQLException 
	 */
	private DeslocaPeticao inserirPeticao(Peticao peticao) throws ServiceException, SQLException {

		ArrayList<Long> objetos = new ArrayList<Long>();
		objetos.add(peticao.getId());
		try {
			DeslocaPeticao novoDeslocaPeticao = new DeslocaPeticao();
			DeslocaPeticaoId novoDeslocaPeticaoId = new DeslocaPeticaoId();
			novoDeslocaPeticaoId.setAnoGuia(guia.getId().getAnoGuia());
			novoDeslocaPeticaoId.setNumeroGuia(guia.getId().getNumeroGuia());
			novoDeslocaPeticaoId.setCodigoOrgaoOrigem(guia.getId().getCodigoOrgaoOrigem());
			novoDeslocaPeticaoId.setPeticao(peticao);
			
			novoDeslocaPeticao.setId(novoDeslocaPeticaoId);
			novoDeslocaPeticao.setCodigoOrgaoDestino(guia.getCodigoOrgaoDestino());
			novoDeslocaPeticao.setGuia(guia);
			novoDeslocaPeticao.setUltimoDeslocamento(false);
			novoDeslocaPeticao.setNumeroSequencia(getDeslocamentoPeticaoService().recuperarUltimaSequencia(guia));
			
			getObjetoIncidenteService().inserirProcessoPeticaoNaGuia(guia,	novoDeslocaPeticao);
		} catch (ServiceException e) {
			setMensagemErroPopup("Erro na inclusão da petição.");
			atualizaInformacoesPopup();
		}
		return getDeslocamentoPeticaoService().recuperarUltimoDeslocamentoPeticao(peticao);
	}

	public void inserirProcessoPeticaoAction(ActionEvent evt) throws SQLException {
		try {
			// retirar os caracteres errados lidos pela caneta ótica
			idProcessoPeticaoPopup = filtraCampoProcesso(idProcessoPeticaoPopup.trim());
			if (idProcessoPeticaoPopup == null || idProcessoPeticaoPopup.trim().equals("") ) {
				return;
			}
			// descobre o tipo da guia
			String tipo = descobrirTipoGuia(idProcessoPeticaoPopup);
			if (tipo == null){
				setMensagemErroPopup("Processo inválido: " + idProcessoPeticaoPopup);
				atualizaInformacoesPopup();
				return;
			}
			// não permitir a inserção de documentos não compatíveis com a guia atual
			if ( (listaProcessosDaGuia != null && listaProcessosDaGuia.size() > 0) || 
					(listaPeticoesDaGuia != null && listaPeticoesDaGuia.size() > 0) ) {
				//Guia guiaAtual = (Guia) listaGuias.get(0).getWrappedObject();
				String tipoGuiaAtual = guia.getTipoGuia();
				if (tipo.equals("PRO") && !tipoGuiaAtual.equals("PRO") ) {
					setMensagemAvisoPopup("Não é possível inserir um Processo Físico em uma guia de " + descricaoGuia);
					atualizaInformacoesPopup();
					return;
				}
				if (tipo.equals("PRE") && !tipoGuiaAtual.equals("PRE") ) {
					setMensagemAvisoPopup("Não é possível inserir um Processo Eletrônico em uma guia de " + descricaoGuia);
					atualizaInformacoesPopup();
					return;
				}
				if (tipo.equals("PET") && !tipoGuiaAtual.equals("PET") ) {
					Long numPeticao = PeticaoParser.getNumeroPeticao(idProcessoPeticaoPopup);
					Short anoPeticao = PeticaoParser.getAnoPeticao(idProcessoPeticaoPopup);
					Peticao petEncontrada = getPeticaoService().recuperarPeticao(numPeticao, anoPeticao);
					if (((petEncontrada.getPendenteDigitalizacao() != null && petEncontrada.getPendenteDigitalizacao()) || 
							(petEncontrada.getRemessaIndevida() != null && petEncontrada.getRemessaIndevida())) && tipoGuiaAtual.equals("PEE")) {
						setMensagemAvisoPopup("Não é possível inserir uma petição Pendente de Digitalização ou marcada como Remessa Indevida em uma guia de Petição eletrônica.");
						atualizaInformacoesPopup();
						return;
					} else {
						setMensagemAvisoPopup("Não é possível inserir uma Petição Física em uma guia de " + descricaoGuia);
						atualizaInformacoesPopup();
						return;
					}
				}
				if (tipo.equals("PEE") && !tipoGuiaAtual.equals("PEE") ) {
					setMensagemAvisoPopup("Não é possível inserir uma Petição Eletrônica em uma guia de " + descricaoGuia);
					atualizaInformacoesPopup();
					return;
				}
			}
		
			if ( tipo.equals("PRE") && (guia.getTipoOrgaoDestino().equals(new Integer(1))) ) {
				setMensagemAvisoPopup("Não é possível deslocar processo eletrônico a um advogado.");
				atualizaInformacoesPopup();
				return;
			}
			
			// verificar a existência do processo ou petição
			Processo processo = null;
			Peticao peticao = null;
			if (tipo.equals("PRO") || tipo.equals("PRE")) {
			   Long numeroProcesso = ProcessoParser.getNumero(idProcessoPeticaoPopup);
			   String siglaProcesso = ProcessoParser.getSigla(idProcessoPeticaoPopup);
			   processo = getProcessoService().recuperarProcesso(siglaProcesso, numeroProcesso);
			} else {
			   Long numeroPeticao = PeticaoParser.getNumeroPeticao(idProcessoPeticaoPopup);
			   Short anoPeticao = PeticaoParser.getAnoPeticao(idProcessoPeticaoPopup);
			   peticao = getPeticaoService().recuperarPeticao(numeroPeticao, anoPeticao);
			}
			
			if (processo == null && peticao == null) {
				setMensagemAvisoPopup("Processo ou petição não existe.");
				atualizaInformacoesPopup();
				return;
			}

			boolean jaExisteProcessoPeticao = false;
			if (tipo.equals("PRO") || tipo.equals("PRE")) { // processo (físico ou
				// somente poderá ser deslocado para baixa quando houver autorização getBaixa() == true OU null
				if ( guia.getCodigoOrgaoDestino().equals(COD_SETOR_BAIXA_E_EXPEDICAO) ) {
					boolean podeBaixa = false;
					if (processo.getBaixa() == null) {
						podeBaixa = true;
					} else if (processo.getBaixa()) {
						podeBaixa = true;
					} else if (!processo.getBaixa()) {
						podeBaixa = false;
					}
					
					if (!podeBaixa) {
						setMensagemAvisoPopup("Processo " + processo.getSiglaClasseProcessual() + "/" + processo.getNumeroProcessual() + " está bloqueado para Baixa!");
						atualizaInformacoesPopup();
						return;
					}	
				}
				
				// criticar se setor de composição e controle de arcórdãos está movimentando para outro setor
				// quando o processo ainda não foi pulicado
				if (codigoLotacao.equals(COD_SETOR_COMPOSICAO_E_CONTROLE_DE_ACORDAOS)) {
					if (!getProcessoPublicadoService().isPublicado(processo.getPrincipal())) {
						setMensagemAvisoPopup("Processo sem publicação de acórdão!");
						atualizaInformacoesPopup();
						return;
					}
				}
				
				DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
				if (deslocaProcesso != null) {
					if (deslocaProcesso.getDataRecebimento() == null) {
						setMensagemAvisoPopup("O processo " + deslocaProcesso.getClasseProcesso() + "/" + deslocaProcesso.getNumeroProcesso() + " está em trânsito.");
						atualizaInformacoesPopup();
						return;
					}
					// verificar se o processo que está sendo incluído não está no setor do usuário 
					Long ultimoSetorDeslocado = deslocaProcesso.getCodigoOrgaoDestino();
					if ( (ultimoSetorDeslocado == null) || (!ultimoSetorDeslocado.equals(codigoLotacao)) ){
						setMensagemAvisoPopup("O processo " + processo.getSiglaClasseProcessual() + "-" + processo.getNumeroProcessual() + " não se encontra no setor do usuário.");
						atualizaInformacoesPopup();
						return;
					}
				}

				// veririfica se o processo que está sendo incluído é apenso de outro (principal)
				ProcessoDependencia processoDependencia = getProcessoDependenciaService().getProcessoVinculador(processo); 
				if (processoDependencia != null) {
					setMensagemAvisoPopup("Não é possível deslocar o processo " + 
							processoDependencia.getClasseProcesso() + "-" + 
							processoDependencia.getNumeroProcesso() + ", pois ele está apensado ao processo " +
							processoDependencia.getClasseProcessoVinculador() + "-" + 
							processoDependencia.getNumeroProcessoVinculador() + "."
							);
					atualizaInformacoesPopup();
					return;
				}

				// verifica se o processo já foi incluido
				for (DeslocaProcesso itemGuia: listaProcessosDaGuia) {
					if (itemGuia.getId().getProcesso().equals(processo)) {
						jaExisteProcessoPeticao = true;
						break;
					}
				}
				if (jaExisteProcessoPeticao) {
					setMensagemAvisoPopup("Processo já adicionado à guia atual.");
					atualizaInformacoesPopup();
				} else {
					// Inserir processo na lista.
					DeslocaProcesso novoDeslocamento = inserirProcesso(processo);
					Hibernate.initialize(novoDeslocamento.getId().getProcesso().getMinistroRelatorAtual().getNome());
					listaProcessosDaGuia.add(novoDeslocamento);
					atualizaInformacoesPopup();
				}

			} else { // petição
				// Crítica do setor do usuário <> do setor atual da petição
				if (peticao == null) {
					setMensagemAvisoPopup("Petição inexistente");
					atualizaInformacoesPopup();
				}
				// Crítica de petição em trânsito
				DeslocaPeticao deslocaPeticao = getDeslocamentoPeticaoService().recuperarUltimoDeslocamentoPeticao(peticao);
				if (deslocaPeticao != null) { // neste caso a petição não teve um deslocamento inicial (petição antiga).
					if (deslocaPeticao.getDataRecebimento() == null) {
						setMensagemAvisoPopup("A petição " + deslocaPeticao.getNumeroPeticao() + "/" + deslocaPeticao.getAnoPeticao() + " está em trânsito.");
						atualizaInformacoesPopup();
						return;
					}
					Long ultimoSetorDeslocado = deslocaPeticao.getCodigoOrgaoDestino();
					if ( (ultimoSetorDeslocado == null) || (!ultimoSetorDeslocado.equals(codigoLotacao)) ){
						setMensagemAvisoPopup("A petição " + peticao.getNumeroPeticao() + "/" + peticao.getAnoPeticao() + " não encontra-se no setor do usuário.");
						atualizaInformacoesPopup();
						return;
					}
				}

				for (DeslocaPeticao itemGuia: listaPeticoesDaGuia) {
					if (itemGuia.getId().getPeticao().equals(processo)) {
						jaExisteProcessoPeticao = true;
						break;
					}
				}
				if (jaExisteProcessoPeticao) {
					setMensagemAvisoPopup("Petição já adicionado à guia atual.");
					atualizaInformacoesPopup();
				} else {
					// Inserir processo na lista.
					DeslocaPeticao novoDeslocamento = inserirPeticao(peticao);
					Hibernate.initialize(peticao.getObjetoIncidenteVinculado().getPrincipal().getIdentificacao());
					listaPeticoesDaGuia.add(novoDeslocamento);
					atualizaInformacoesPopup();
				}
			}
			
			atualizarSessao();

		} catch (ServiceException e) {
			setMensagemErroPopup("Erro ao inserir objeto incidente: " + e.getMessage());
			atualizaInformacoesPopup();
		}
		
	}
	
	public void receberGuia(ActionEvent evt) throws ServiceException {
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaGuias.getRowData();
		Guia guia = (Guia) chkDataTable.getWrappedObject();

		boolean impedimento = false;

		try {
			// o destino deve ser o setor do usuário recebedor
			if (!codigoLotacao.equals(guia.getCodigoOrgaoDestino())) {
				impedimento = true;
			}
			boolean deslocou = false;
			if (guia.getTipoGuia().equals("PET") || guia.getTipoGuia().equals("PET")) {
				List<DeslocaPeticao> deslocaPeticaos = getDeslocamentoPeticaoService().recuperarDeslocamentoPeticaos(guia);
				try {
					for (DeslocaPeticao deslocaPeticao : deslocaPeticaos) {
						if (deslocaPeticao.getId().getPeticao().getTipoObjetoIncidente() == null ) {
							reportarAviso("Erro ao recuperar informações do(s) processo(s).");	
							return ;
						}
					}} catch (Exception e) {
						reportarAviso("Existem processos classificados como 'Oculto'. Para receber a guia será necessário possuir esse perfil.");	
						return ;
				}
				recebePeticao(deslocaPeticaos, impedimento);
			} else {
				try {
					List<DeslocaProcesso> deslocaProcessos = getDeslocaProcessoService().recuperarDeslocamentoProcessos(guia);
					for (DeslocaProcesso deslocaProcesso : deslocaProcessos) {
						if (deslocaProcesso.getId().getProcesso().getTipoObjetoIncidente() == null ) {
							reportarAviso("Erro ao recuperar informações do(s) processo(s).");	
							return ;
						}
						
					}
					deslocou = recebeProcesso(deslocaProcessos, impedimento);
					} catch (Exception e) {
						e.printStackTrace();
						reportarAviso("Existem processos classificados como 'Oculto'. Para receber a guia será necessário possuir esse perfil.");	
						return ;
				}
			}
			atualizarSessao();
			if (deslocou) {
				reportarInformacao("Guia " + guia.getId().getNumeroGuia() + "/" + guia.getId().getAnoGuia() + " recebida com sucesso!");
				guia.setSituacao(RECEBIDA);
			} else {
				reportarAviso("Guia " + guia.getId().getNumeroGuia() + "/" + guia.getId().getAnoGuia() + " já foi recebida!");
			}
			
		} catch (ServiceException e) {
			reportarErro("Erro ao executar o recebimento da guia: " + e.getMessage());
		}
		atualizarSessao();
	}

	private boolean recebePeticao(List<DeslocaPeticao> deslocaPeticaos, boolean impedimento) throws ServiceException {
		boolean deslocou = false;
		if (deslocaPeticaos.size() == 0 || deslocaPeticaos.get(0).getDataRecebimento() != null) {
			impedimento = true;
		}
		if (!impedimento) {
			for (DeslocaPeticao objeto : deslocaPeticaos) {
				getDeslocamentoPeticaoService().salvarRecebimentoPeticao(objeto);
				deslocou = true;
			}
		}
		return deslocou;
	}

	private boolean recebeProcesso(List<DeslocaProcesso> deslocaProcessos, boolean impedimento) throws ServiceException {
		boolean deslocou = false;
		if (deslocaProcessos.size() == 0 || deslocaProcessos.get(0).getDataRecebimento() != null) {
			impedimento = true;
		}
		boolean todosProcessosRecebidos;
		todosProcessosRecebidos = true;
		for (DeslocaProcesso deslocaProcessosGuia : deslocaProcessos) {
			if (deslocaProcessosGuia.getDataRecebimento() == null ) {
				todosProcessosRecebidos = false;
			}
		}
		if ((!impedimento) || (todosProcessosRecebidos == false)) {
			for (DeslocaProcesso objeto : deslocaProcessos) {
				if (objeto.getDataRecebimento() == null) {
				getDeslocaProcessoService().receberProcesso(objeto);
				// verificar se existe apenso ao processo
				List<ProcessoDependencia> processosApensos = getProcessoDependenciaService().recuperarApensos(objeto.getId().getProcesso());
				if (processosApensos != null && processosApensos.size() > 0) {
					for (ProcessoDependencia dependencia : processosApensos) {
						 Processo processoApenso = getProcessoService().recuperarProcesso(dependencia.getClasseProcesso(), dependencia.getNumeroProcesso());
						 DeslocaProcesso deslocaApenso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processoApenso);
						 getDeslocaProcessoService().receberProcesso(deslocaApenso);
					}
				}
				//
				}
				deslocou = true;
			}
		}
		return deslocou;
	}
	
	public void pesquisarGuiaAction(ActionEvent evt) throws Exception {
		pesquisarGuia();
		atualizarSessao();
	}
	
	private void pesquisarGuia() throws ServiceException {
		try {

			if (BooleanUtils.isTrue(chkDestinoLotacaoUsuario)) {
				codigoDestino = codigoLotacao;
			} else if(StringUtils.isNotVazia(descricaoDestino)){
				codigoDestino = Long.parseLong(descricaoDestino.split(" - ")[0]);
			}
			
			if (BooleanUtils.isTrue(chkOrigemLotacaoUsuario)) {
				codigoOrigem = codigoLotacao;
			} else if(StringUtils.isNotVazia(descricaoOrigem)){
				codigoOrigem = Long.parseLong(descricaoOrigem.split(" - ")[0]);
			}
			
			peticaoProcesso = identificacaoProcessoPeticao;
			if(peticaoProcesso == null || peticaoProcesso.isEmpty()){
				setPesquisarTodos(true);
			}
			if (!isVazio(anoGuia) && isVazio(numGuia) && isVazio(codigoDestino) && 
					isVazio(codigoOrigem) && isVazio(peticaoProcesso) && isVazio(dataEnvio) ) {
				reportarAviso("Por favor, além do ano informe um parâmetro a mais.");
				return;
			}
			if (isVazio(anoGuia) && isVazio(numGuia) && isVazio(codigoDestino) && 
					isVazio(codigoOrigem) && isVazio(peticaoProcesso) && isVazio(dataEnvio) ) {
				reportarErro("Por favor, informe pelo menos um dos parâmetros de pesquisa.");
			} else {
				Processo processo = null;
				Peticao peticao = null;

				tipoGuia = (String) getAtributo(TIPO_GUIA);

				Guia guia = new Guia();
				GuiaId guiaId = new GuiaId();

				if (anoGuia != null && anoGuia != "") {
					setAnoGuia(anoGuia.trim());
					setAnoGuia(anoGuia.replace("\n", ""));
					guiaId.setAnoGuia(Short.valueOf(anoGuia));
				}
				if (numGuia != null && numGuia != "") {
					setNumGuia(numGuia.trim());
					setNumGuia(numGuia.replace("\n", ""));
					guiaId.setNumeroGuia(Long.parseLong(numGuia));
				}

				if (codigoOrigem != null && codigoOrigem != 0) {
					guiaId.setCodigoOrgaoOrigem(codigoOrigem);
				}
				guia.setCodigoOrgaoDestino(codigoDestino);
				guia.setDataRemessa(dataEnvio);
				guia.setId(guiaId);

				if (peticaoProcesso != null && peticaoProcesso != "") {
					trataIdentificador(peticaoProcesso);
					if (numeroProcesso != null) {
						setProcesso(true);
						processo = getProcessoService().recuperarProcesso(siglaProcesso, new Long(numeroProcesso));
					}
					if (numeroProcesso == null) {
						setPeticao(true);
						peticao = getPeticaoService().recuperarPeticao(new Long(numeroPeticao), new Short(anoPeticao));	
					}
					if ((processo == null || processo.getNumeroProcessual() == null) && (peticao == null || peticao.getNumeroPeticao() == null)) {
						reportarAviso("O 'Processo/Petição' informado não existe. Informe sigla + numero para processo ou numero + ano para petição.");
						return;
					}
				}
				List<Guia> guias = getGuiaService().recuperarGuia(guia, processo, peticao);
				for(Guia g : guias){
					g.getDescricaoDestino();
				}
				atualizarListaGuias(guias); //atualiza na lista a data de recebimento e o tipo da guia. 
				atualizarSessao();
				if (listaGuias.size() == 0) {
					reportarAviso("Nenhuma guia encontrada para o(s) parâmetro(s) informado(s).");
				}
			}
		} catch (NumberFormatException e) {
			reportarErro("Erro ao pesquisar a guia. Favor informar valores numéricos para o ano, o número da guia, processo ou petição: " + e.getMessage());
		}
	}
	
	public void atualizarOrigemSetorUsuario() {
		if (BooleanUtils.isFalse(getChkOrigemLotacaoUsuario())) {
			setCodigoOrigem(getSetorUsuarioAutenticado().getId());
			setDescricaoOrigem(getCodigoDescricaoLotacao());
			chkOrigemLotacaoUsuario = true;

			setCodigoDestino(null);
			setDescricaoDestino("");
			chkDestinoLotacaoUsuario = false;
			
		} else {
			setCodigoOrigem(null);
			setDescricaoOrigem("");
			chkOrigemLotacaoUsuario = false;
		}

		atualizarSessao();
	}

	public void atualizarDestinoSetorUsuario() {
		if (BooleanUtils.isFalse(chkDestinoLotacaoUsuario)) {
			setCodigoDestino(getSetorUsuarioAutenticado().getId());
			setDescricaoDestino(getCodigoDescricaoLotacao());
			chkDestinoLotacaoUsuario = true;

			setCodigoOrigem(null);
			setDescricaoOrigem("");
			chkOrigemLotacaoUsuario = false;
		} else {
			setCodigoDestino(null);
			setDescricaoDestino("");
			chkDestinoLotacaoUsuario = false;
		}

		atualizarSessao();
	}

	
	public void atualizarListaGuias(List<Guia>guias) throws ServiceException {
		List<Guia> guiasAtualizadas = new ArrayList<Guia>();
		Iterator<Guia> itGuia = guias.iterator();
		while (itGuia.hasNext()) {
			Guia objGuia = (Guia) itGuia.next();
			objGuia.setTipoGuia(recuperarTipoGuia(objGuia));
			objGuia.setDataRecebimento(recuperarDataRecebimento(objGuia));
			objGuia.setSituacao(recuperarSituacaoGuia(objGuia));
			// atualiza o nome do advogado: se a carga foi efetuada pelo sistema antigo o serviço irá trazer o nome da tabela antiga, senão irá buscá-lo na nova estrutura de dados.
			if (objGuia.getTipoOrgaoDestino().equals(new Integer(1))) {
			   String nomeAdvogadoDestino = getEmprestimoAutosProcessoService().getNomeAdvogadoOuAutorizado(objGuia);
			   objGuia.setDescricaoDestinoAdvogado(nomeAdvogadoDestino);
			} else if (objGuia.getTipoOrgaoOrigem().equals(new Integer(1))) {
			   // recupera o nome do advogado: o método irá trazer o nome da tabela antiga STF.ADVOGADOS se não encontrar emprestimos para a referida guia.
			   // se tiver emprestimo irá trazê-lo da JUDICIARIO.JURISDICIONADO
			   String nomeAdvogadoOrigem = getEmprestimoAutosProcessoService().getNomeAdvogadoOuAutorizado(objGuia);
			   if (nomeAdvogadoOrigem != null && !nomeAdvogadoOrigem.trim().equals("")) {
				   objGuia.setDescricaoOrigemAdvogado(nomeAdvogadoOrigem);
			   }
			}
			guiasAtualizadas.add(objGuia);
			Collections.sort(guiasAtualizadas, new PropertyComparator<Guia>(TipoOrdenacao.ASCENDENTE, "dataInclusao"));
		}
		listaGuias.clear();
		listaGuias.addAll(getCheckableDataTableRowWrapperList(guiasAtualizadas));
		
	}
	
	private String recuperarDataRecebimento(Guia guia) throws ServiceException {
		String dataRecebimento = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
		if (guia.getTipoGuia().equals("PRO")) {
			List<DeslocaProcesso> dataRecebimentoProcesso = getDeslocaProcessoService().pesquisarDataRecebimentoGuiaProcesso(guia);
			if (dataRecebimentoProcesso.size() > 0) {
				DeslocaProcesso delocaProcesso = dataRecebimentoProcesso.get(0);
				dataRecebimento = df.format(delocaProcesso.getDataRecebimento());
			}
		} else {
			List<DeslocaPeticao> dataRecebimentoPeticao = getDeslocamentoPeticaoService().pesquisarDataRecebimentoGuiaPeticao(guia);
			if (dataRecebimentoPeticao.size() > 0) {
				DeslocaPeticao peticao = dataRecebimentoPeticao.get(0);
				dataRecebimento = df.format(peticao.getDataRecebimento());
			}
		}
		
		atualizarSessao();
		return dataRecebimento;
	}
	
	private String recuperarTipoGuia(Guia guia) throws ServiceException {
		if (getGuiaService().isPeticao(guia)) {
			return "PET";
		} else {
			return "PRO";
		}
	}
	
	private String recuperarSituacaoGuia(Guia guia) throws ServiceException {
		
// 		Não há necessidade de tratar guias vazias pois elas não aparecem na pesquisa de guias.
		
//		DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarDeslocamentoProcesso(guia);
//		DeslocaPeticao deslocaPeticao = getDeslocamentoPeticaoService().recuperarDeslocamentoPeticao(guia);
		
//		if (deslocaProcesso == null && deslocaPeticao == null) {
//			return GUIA_VAZIA;
//		}
		boolean todosProcessosRecebidos;
		todosProcessosRecebidos = true;
		boolean todosProcessosNaoRecebidos;
		todosProcessosNaoRecebidos = true;
		List<DeslocaProcesso> deslocaProcessos = getDeslocaProcessoService().recuperarDeslocamentoProcessos(guia);
		for (DeslocaProcesso deslocaProcessosGuia : deslocaProcessos) {
			if (deslocaProcessosGuia.getDataRecebimento() == null ) {
				todosProcessosRecebidos = false;
			}else{
				todosProcessosNaoRecebidos = false;
			}
		}
		if ( (todosProcessosRecebidos == false || guia.getDataRecebimento() == null || guia.getDataRecebimento().trim().equals("")) && (guia.getCodigoOrgaoDestino().equals(codigoLotacao)) ) {
			if (!todosProcessosRecebidos && !todosProcessosNaoRecebidos){
				return RECEBIDA_PARCIAL;	
			}else{
				return RECEBER;
			}
			
		} else if ( (guia.getDataRecebimento() == null || guia.getDataRecebimento().trim().equals("")) && (!guia.getCodigoOrgaoDestino().equals(codigoLotacao)) ) {
			return EM_TRANSITO;
		} else if ( (guia.getDataRecebimento() != null && !guia.getDataRecebimento().trim().equals("")) ) {
			return RECEBIDA;
		} else {
			return "";
		}
	}
	
	public String getDestinatarioBaixa(){
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaGuias.getRowData();
		Guia guia = (Guia) chkDataTable.getWrappedObject();
		if (guia.getEnderecoDestinatario() != null) {
			return " / " + guia.getEnderecoDestinatario().getDestinatario().getId() + "-" +
					guia.getEnderecoDestinatario().getDestinatario().getNomDestinatario();
		} else {
			return "";
		}
	}
	
	public String getDescricaoQuantidadeItens() {
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaGuias.getRowData();
		Guia guia = (Guia) chkDataTable.getWrappedObject();
		if (guia.getTipoGuia().equals("PRO")) {
			if (guia.getQuantidadeInternaProcesso() > 1) {
				return "Processos";
			} else {
				return "Processo";
			}
		} else {
			if (guia.getQuantidadeInternaProcesso() > 1) {
				return "Petições";
			} else {
				return "Petição";
			}
		}
	}

	
	// limpa o valor do codigoDestino (utilizado na pesquisa das guias)
	public void limparDestinoPesquisa() {
		setCodigoDestino(null);
		setChkDestinoLotacaoUsuario(false);
		atualizarSessao();
	}

	public void limparOrigemPesquisa() {
		setCodigoOrigem(null);
		setChkOrigemLotacaoUsuario(false);
		atualizarSessao();
	}

	/*
	 * Permite habilitar/desabilitar (reenderiza) o icone de recebimento da guia. 
	 * Somente quando satisfeita a regra: sem data de recebimento e destino da guia o mesmo que a lotação do usuário. 
	 */
	public Boolean getPodeReceberGuia() {
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaGuias.getRowData();
		Guia guia = (Guia) chkDataTable.getWrappedObject();
//		if ((guia.getDataRecebimento() == null || guia.getDataRecebimento().trim().equals("")) && guia.getCodigoOrgaoDestino().equals(codigoLotacao)) {
		if (guia.getSituacao().equals(RECEBER) || guia.getSituacao().equals(RECEBIDA_PARCIAL)) {
			return true;
		} else {
			return false;
		}
	}

	
	public void trataIdentificador(String valor) {
		valor = valor.replaceAll("\t", "");  
		valor = valor.replaceAll("\n", "");  
		valor = valor.replaceAll(" ", "");

		boolean isNumero = true;
		if (valor != null && valor.trim().length() > 0) {
			valor = valor.trim();
			if (valor.length() == 14){
				valor = valor.substring(0, 13);
			}
			for (int i = 0; i < valor.length(); i++) {
				Character caractere = valor.charAt(i);
				if (Character.isDigit(caractere)) {
					// isNumero = false;
					break;
				} else {
					isNumero = false;
				}
			}
			// se número obter o ano
			if (isNumero) {
				StringBuilder strOriginal = new StringBuilder(valor.toString().replace("/", "").trim());
				if (strOriginal.length() <= 4) {
					setAnoPeticao(null);
					setNumeroPeticao(null);
					setSiglaProcesso(null);
					setNumeroProcesso(null);
				} // somente prossegue se existir pelo menos um digito do numero
					// e o quantro do ano
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
				setAnoPeticao(ano.toString());
				setNumeroPeticao(numero.toString());
				setSiglaProcesso(null);
				setNumeroProcesso(null);
			} else {

				char[] caracteres = valor.toCharArray();
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
						if (!Character.isLetter(caracteres[i])) {
							sigla = valor.substring(inicioContagem, i);
							break;
						} else if (i == caracteres.length - 1) {
							sigla = valor.substring(inicioContagem, i + 1);
							break;
						}
					}
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
							numero = valor.substring(inicioContagem, i);
							break;
						} else if (i == caracteres.length - 1) {
							numero = valor.substring(inicioContagem, i + 1);
							break;
						}
					}
				}
				String apresentaSigla = null;
				if (sigla != null && sigla.trim().length() > 0 && numero != null && numero.trim().length() > 0) {
					Long lNumero = null;
					try {
						lNumero = new Long(numero);
					} catch (NumberFormatException e) {
						reportarErro("Número de processo inválido: " + numero);
					}

					try {
						apresentaSigla = sigla;
						sigla = converterClasse(sigla);
					} catch (ServiceException e) {
						reportarErro("Erro ao converter classe processual: " + apresentaSigla);
					}

					if (sigla == null) {
						reportarAviso("Classe processual não encontrada: " + apresentaSigla);
					}

					try {
						Processo processo = getProcessoService().recuperarProcesso(sigla, lNumero);
						if (processo == null) {
							reportarErro("Processo não encontrado!");
						}
					} catch (ServiceException e) {
						reportarErro("Erro ao recuperar processo: " + sigla + "/" + lNumero);
					}

					setNumeroProcesso(numero);
					setSiglaProcesso(sigla);
					setAnoPeticao(null);
					setNumeroPeticao(null);
				}
			}
		} else { // se o parâmetro 'valor' estiver vazio
			setNumeroProcesso(null);
			setSiglaProcesso(null);
			setAnoPeticao(null);
			setNumeroPeticao(null);
		}
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
	
	private void limpaMensagens() {
		setMensagemAvisoPopup("");
		setMensagemErroPopup("");
		setMensagemInfoPopup("");
	}

	public void prepararParaEdicaoAction(ActionEvent evt) throws ServiceException {
		limpaMensagens();
		
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaGuias.getRowData();
		Guia guia = (Guia) chkDataTable.getWrappedObject();
		
		if (guia == null || guia.getId() == null) {
			setMensagemInfoPopup("Guia removida!");
			comportamentoBotao(false, "btnSalvar", false);
			comportamentoBotao(false, "btnExcluir", false);
			comportamentoBotao(false, "btnImprimir", false);
			comportamentoBotao(true, "btnFechar", false);
			comportamentoInput(false, "itNovoDestinoPopup");
			comportamentoInput(false, "itEditarObservacao");
			return;
		}
		
		setGuia(guia);
		setAnoGuiaPopup(guia.getId().getAnoGuia());
		setNumeroGuiaPopup(guia.getId().getNumeroGuia());
		setCodigoOrigemPopup(guia.getId().getCodigoOrgaoOrigem());
		setDescricaoOrigemPopup(guia.getDescricaoOrigem());
		setCodigoDestinoPopup(guia.getCodigoOrgaoDestino());
		setDescricaoDestinoPopup(guia.getDescricaoDestino());
		setObservacaoPopup(guia.getObservacao());
		DeslocaProcesso deslocaProcesso =  getDeslocaProcessoService().recuperarDeslocamentoProcesso(guia);
		DeslocaPeticao deslocaPeticao = null;
		if (deslocaProcesso == null) {
			deslocaPeticao = getDeslocamentoPeticaoService().recuperarDeslocamentoPeticao(guia);
		}
		if (deslocaPeticao == null && deslocaProcesso == null) {
			setMensagemAvisoPopup("Guia excluída!");
			comportamentoInput(false, "itNovoDestinoPopup");
			comportamentoBotao(false, "btnSalvar", false);
			comportamentoBotao(false, "btnExcluir", false);
			comportamentoInput(false, "itEditarObservacao");
			atualizarSessao();
			return;
		}
		String tipoGuia = "";
		
		if (deslocaPeticao != null) {
		   tipoGuia = descobrirTipoGuia(deslocaPeticao.getId().getPeticao());
		} else {
		   tipoGuia = descobrirTipoGuia(deslocaProcesso.getId().getProcesso().getPrincipal());
		}
		if (tipoGuia.equals("PRO")) {
			setDescricaoGuia("Processo Físico");
		} else if (tipoGuia.equals("PRE")) {
			setDescricaoGuia("Processo Eletrônico");
		} else if (tipoGuia.equals("PET")) {
			setDescricaoGuia("Petição Física");
		} else if (tipoGuia.equals("PEE")) {
			setDescricaoGuia("Petição Eletrônica");
		}

		// veririficar se o usuário tem permissão para a edição e habilitar os campos
		Guia guiaPermissao = new Guia();
		GuiaId guiaIdPermissao = new GuiaId();
		
		guiaIdPermissao.setAnoGuia(guia.getAnoGuia());
		guiaIdPermissao.setNumeroGuia(guia.getId().getNumeroGuia());
		guiaIdPermissao.setCodigoOrgaoOrigem( codigoLotacao );
		guiaPermissao.setId(guiaIdPermissao);
		guiaPermissao.setTipoGuia(guia.getTipoGuia());
		
		if (getSetorUsuarioAutenticado().getId().longValue() == guia.getCodigoOrgaoOrigem().longValue()){
			temPermissaoAlterar = getGuiaService().temPermissaoAlterarGuia(guiaPermissao);
		}else{
			temPermissaoAlterar = false;
		}
		if (temPermissaoAlterar) {
			comportamentoInput(true, "itNovoDestinoPopup");
			comportamentoBotao(true, "btnSalvar", false);
			comportamentoBotao(true, "btnExcluir", false);
			comportamentoInput(true, "itEditarObservacao");
		} else {
			comportamentoInput(false, "itNovoDestinoPopup");
			comportamentoBotao(false, "btnSalvar", false);
			comportamentoBotao(false, "btnExcluir", false);
			comportamentoInput(false, "itEditarObservacao");
		}

		listaProcessosDaGuia.clear();
		listaPeticoesDaGuia.clear();
		if (deslocaPeticao == null) {
			listaProcessosDaGuia = getDeslocaProcessoService().recuperarDeslocamentoProcessos(guia);
			for (DeslocaProcesso desloca: listaProcessosDaGuia) {
				Hibernate.initialize(desloca.getId().getProcesso().getMinistroRelatorAtual());
			}
		} else {
			listaPeticoesDaGuia =  getDeslocamentoPeticaoService().recuperarDeslocamentoPeticaos(guia);
			for (DeslocaPeticao desloca: listaPeticoesDaGuia) {
				Hibernate.initialize(desloca.getId());
				if (desloca.getId() != null) {
					Hibernate.initialize(desloca.getId().getPeticao());
				}
				Hibernate.initialize(desloca.getId().getPeticao().getAnoPeticao());
				Hibernate.initialize(desloca.getId().getPeticao().getNumeroPeticao());
				Hibernate.initialize(desloca.getId().getPeticao().getObjetoIncidenteVinculado());
				if (desloca.getId().getPeticao().getObjetoIncidenteVinculado() != null) {
					Hibernate.initialize(desloca.getId().getPeticao().getObjetoIncidenteVinculado().getPrincipal());
					if (desloca.getId().getPeticao().getObjetoIncidenteVinculado().getPrincipal() != null) {
						Hibernate.initialize(desloca.getId().getPeticao().getObjetoIncidenteVinculado().getPrincipal().getIdentificacao());
					}
				}
			}
		}
		
		atualizarSessao();
	}
	
	public String getSituacaoGuiaPopup(){
		if (guia == null) {
			return "Desconhecida";
		} 
		if (guia.getDataRecebimento() == null || guia.getDataRecebimento().trim().equals("")) {
			return "Em trânsito";
		} else {
			return "Recebida em " + guia.getDataRecebimento();
		}
		
	}
	
	private Boolean temPermissaoAlteracao() throws ServiceException{
		DeslocaProcesso deslocaProcesso;
		DeslocaPeticao deslocaPeticao;
		Date dataRecebimento = null;

		if (guia.getId() == null) {
			return false;
		}
		deslocaProcesso = getDeslocaProcessoService().recuperarDeslocamentoProcesso(guia);
		if (deslocaProcesso == null) {
			deslocaPeticao = getDeslocamentoPeticaoService().recuperarDeslocamentoPeticao(guia);
			if (deslocaPeticao == null) {
				return false;
			}
			dataRecebimento = deslocaPeticao.getDataRecebimento();
		} else {
			dataRecebimento = deslocaProcesso.getDataRecebimento();
		}
		
		if ( (dataRecebimento == null) && (guia.getId().getCodigoOrgaoOrigem().equals(new Long(codigoLotacao))) ){
			return true;
		} else {
			return false;
		}
		
	}
	
	public void limparPopup(){
		 guia = new Guia();
		 mensagemAvisoPopup = "";
		 mensagemErroPopup = "";
		 mensagemInfoPopup = "";
		 numeroGuiaPopup = null;
		 anoGuiaPopup = null;
		 codigoOrigemPopup = null;
		 descricaoOrigemPopup = "";
		 codigoDestinoPopup = null;
		 descricaoDestinoPopup = "";
		 codigoNovoDestinoPopup = null;
		 descricaoNovoDestinoPopup = "";
		 observacaoPopup = "";
		 listaProcessosDaGuia = new ArrayList<DeslocaProcesso>();
		 listaPeticoesDaGuia = new ArrayList<DeslocaPeticao>();
		 atualizarSessao();
	}
	
	public boolean getDesabilitarPostal() {
		// somente habilita o componente se o tipo for 3
		if (guia == null || guia.getCodigoOrgaoDestino() == null) {
			return true;
		}
		if (guia.getTipoOrgaoDestino() == 3) {
			return false;
		} else {
			return true;
		}
	}
	
	private void atualizaInformacoesPopup() {
		setNumeroGuiaPopup(guia.getId().getNumeroGuia());
		setAnoGuiaPopup(guia.getId().getAnoGuia());
		setCodigoOrigemPopup(guia.getId().getCodigoOrgaoOrigem());
		setCodigoDestinoPopup(guia.getCodigoOrgaoDestino());
		setDescricaoOrigemPopup(guia.getDescricaoOrigem());
		setDescricaoDestinoPopup(guia.getDescricaoDestino());
		atualizarSessao();
	}

	/**
	 * Atualiza Guia
	 * 
	 * @throws ServiceException
	 * */
	public void atualizarGuia(ActionEvent evt) throws ServiceException {
		boolean alterouDeslocamento = false;
		boolean alterouGuia = false;

//		setNumeroGuiaPopup(guia.getId().getNumeroGuia());
//		setAnoGuiaPopup(guia.getId().getAnoGuia());
//		setCodigoOrigemPopup(guia.getId().getCodigoOrgaoOrigem());
//		setCodigoDestinoPopup(guia.getId().getCodigoOrgaoOrigem());
//		setDescricaoOrigemPopup(guia.getDescricaoOrigem());
//		setDescricaoDestinoPopup(guia.getDescricaoDestino());
//		atualizarSessao();
		atualizaInformacoesPopup();
		
		// somente altera se foi informado novo destino
		if (codigoNovoDestinoPopup != null && codigoNovoDestinoPopup > 0) {
			if (guia.getCodigoOrgaoOrigem().equals(codigoNovoDestinoPopup)) {
				setMensagemAvisoPopup("O novo destino da guia não pode ser igual a origem.");
				return;
			}
			guia.setCodigoOrgaoDestino(codigoNovoDestinoPopup);
			guia.setTipoOrgaoDestino(new Integer(tipoNovoDestinoPopup));
			alterouDeslocamento = true;
		}
		// somente altera se for diferente de null
		if (observacaoPopup != null) {
			guia.setObservacao(observacaoPopup);
			getGuiaService().salvar(guia);
			alterouGuia = true;
		}
		
		if (alterouDeslocamento) {
			//getGuiaService().alterarGuia(guia);
			getGuiaService().salvar(guia);
			if (guia.getTipoGuia().equals("PRO") || guia.getTipoGuia().equals("PRE")) {
				List<DeslocaProcesso> processos = recuperarDeslocamentosProcesso();
				for (DeslocaProcesso deslocaProcesso : processos) {
					deslocaProcesso.setCodigoOrgaoDestino(guia
							.getCodigoOrgaoDestino());
					getDeslocaProcessoService().alterar(deslocaProcesso);
				}
			} else {
				List<DeslocaPeticao> peticoes = recuperarDeslocamentosPeticoes();
				for (DeslocaPeticao deslocaPeticao : peticoes) {
					deslocaPeticao.setCodigoOrgaoDestino(guia
							.getCodigoOrgaoDestino());
					getDeslocamentoPeticaoService().alterar(deslocaPeticao);
				}
			}
			
			if (codigoNovoDestinoPopup != null && codigoNovoDestinoPopup > 0) {
				setCodigoDestinoPopup(guia.getCodigoOrgaoDestino());
				String nomeDestino = "";
				if (guia.getTipoOrgaoDestino().equals(new Integer(1))) {
				    nomeDestino = getJurisdicionadoService().recuperarPorId(codigoNovoDestinoPopup).getNome();
				} else if (guia.getTipoOrgaoDestino().equals(new Integer(2))) {
				    nomeDestino = getSetorService().recuperarPorId(codigoNovoDestinoPopup).getNome();
				} else if (guia.getTipoOrgaoDestino().equals(new Integer(3))) {
				    nomeDestino = getOrigemService().recuperarPorId(codigoNovoDestinoPopup).getDescricao();
				}
				setDescricaoDestinoPopup(nomeDestino);
				guia.setDescricaoDestinoSetor(nomeDestino);
			}
		}
		if (alterouGuia || alterouDeslocamento) {
			setMensagemInfoPopup("Guia atualizada com sucesso.");
		}
		atualizarSessao();
		
	}
	
	private List<DeslocaPeticao> recuperarDeslocamentosPeticoes() {
		List<DeslocaPeticao> peticoes = Collections.emptyList();
		try {
			peticoes = getDeslocamentoPeticaoService()
					.recuperarDeslocamentoPeticaos(this.guia);
		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar as petições. ");
			return peticoes;
		}
		return peticoes;
	}

	private List<DeslocaProcesso> recuperarDeslocamentosProcesso() {
		List<DeslocaProcesso> processos = Collections.emptyList();
		try {
			processos = getDeslocaProcessoService()
					.recuperarDeslocamentoProcessos(this.guia);
		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar os processos. ");
			return processos;
		}
		return processos;
	}

	
	public void excluirGuia(ActionEvent evt) {

		try {
			if (!respostaExcluirGuia.equals("S")) {
				atualizaInformacoesPopup();
				return;
			}
			setNumeroGuiaPopup(guia.getId().getNumeroGuia());
			setAnoGuiaPopup(guia.getId().getAnoGuia());
			setCodigoOrigemPopup(guia.getId().getCodigoOrgaoOrigem());
			setCodigoDestinoPopup(guia.getId().getCodigoOrgaoOrigem());
			setDescricaoOrigemPopup(guia.getDescricaoOrigem());
			setDescricaoDestinoPopup(guia.getDescricaoDestino());
			
			getObjetoIncidenteService().excluirGuia(guia);
			setMensagemInfoPopup("Guia excluída com sucesso.");
			comportamentoInput(false, "itNovoDestinoPopup");
			comportamentoBotao(false, "btnSalvar", false);
			comportamentoBotao(false, "btnExcluir", false);
			comportamentoBotao(false, "btnImprimir", false);
			comportamentoInput(false, "itEditarObservacao");
			guia.setSituacao("Guia Removida");
			atualizarSessao();

		} catch (DaoException e) {
			setMensagemErroPopup("Erro ao excluir a guia.");
		} catch (ServiceException e) {
			setMensagemErroPopup("Erro ao excluir a guia.");
		}
	}
	
	// gerar o stream do PDF para impressão
	private ByteArrayInputStream gerarStream(){
		List<Guia> guias = new ArrayList<Guia>();
		guias.add(guia);
		boolean bPostal = false;
		byte[] arquivo = null;
		try {
			if (postal == null) {
				bPostal = false;
			} else {
				bPostal = postal.booleanValue();
			}
			if (guia.getTipoGuia().equals("PRO")) {
				if (guia.getTipoOrgaoDestino().equals(new Integer("1"))) { // advogado retirada
					arquivo = getProcessamentoRelatorioService()
					.criarRelatorioGuiaRetiradaAutosProcesso(
							guia.getId().getNumeroGuia(),
							guia.getId().getAnoGuia(),
							guia.getId().getCodigoOrgaoOrigem(), false);
				} else if (guia.getTipoOrgaoOrigem().equals(new Integer("1"))) { // advogado devolução
					arquivo = getProcessamentoRelatorioService()
							.criarRelatorioGuiaDevolucaoAutosProcesso(guias);
				} else {
					arquivo = getProcessamentoRelatorioService()
					.criarRelatorioGuiaDeslocamentoProcesso(
							guia.getId().getNumeroGuia(),
							guia.getId().getAnoGuia(),
							guia.getId().getCodigoOrgaoOrigem(), bPostal);
				}
			} else {
				arquivo = getProcessamentoRelatorioService()
						.criarRelatorioGuiaDeslocamentoPeticao(
								guias);
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao imprimir a guia: " + e.getMessage());
		}
		ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
		return input;
	}
	
	public void imprimirGuia() {
			mandarRespostaDeDownloadDoArquivo(gerarStream());
	}
	
	private void mandarRespostaDeDownloadDoArquivo(ByteArrayInputStream input) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		response.setHeader("Content-disposition",
				"attachment; filename=\"GuiaDeDeslocamentoJudiciario.pdf\"");
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
	
	public void limparPopupAction(ActionEvent evt) {
		limparPopup();
	}

	public boolean getIsApenso() throws ServiceException {
		
		// atualiza a quantidade de apensos que o processo possui (se houver)
		DeslocaProcesso deslocaProcesso = (DeslocaProcesso) tabelaProcessos.getRowData();
		Processo processo = (Processo)  deslocaProcesso.getId().getProcesso();
		setQuantidadeVinculados(getProcessoDependenciaService().getQuantidadeVinculados(processo));
		
		// retorna se o processo é um vinculado a outro (se é um apenso)
		return getProcessoDependenciaService().isApenso(processo);
	}
	
	// apenas usuários com permissão: vide regra em temPermissaoAlteracao().
	// false = não pode (desabilita componente); true = pode (habilita).
	public Boolean getPodeEditar() throws ServiceException {
		if (guia == null) {
			return false;
		}
		return temPermissaoAlteracao();
	}
	
	public void removerItemProcessoAction(ActionEvent evt) throws ServiceException {
		boolean excluiu = false;
		DeslocaProcesso deslocaProcesso = (DeslocaProcesso) tabelaProcessos.getRowData();
		getDeslocaProcessoService().excluir(deslocaProcesso);
		excluiu = true;
		listaProcessosDaGuia.remove(deslocaProcesso);
		if (listaProcessosDaGuia.size() == 0) {
			setMensagemInfoPopup("Guia removida!");
			comportamentoBotao(false, "btnSalvar", false);
			comportamentoBotao(false, "btnExcluir", false);
			comportamentoBotao(false, "btnImprimir", false);
			comportamentoBotao(true, "btnFechar", false);
			if (listaGuias.size() > 0) {
				listaGuias.clear();
			}
			// se não sobrou nenhum deslocamento excluir a guia também
			getGuiaService().excluir(guia);
		} else {
			if (excluiu && guia.getQuantidadeInternaProcesso() > 0) {
			    guia.setQuantidadeInternaProcesso(guia.getQuantidadeInternaProcesso()-1);
				getGuiaService().salvar(guia);
			}
		}
		atualizaInformacoesPopup();
	}

	public void removerItemPeticaoAction(ActionEvent evt) throws ServiceException {
		boolean excluiu = false;
		DeslocaPeticao deslocaPeticao = (DeslocaPeticao) tabelaPeticoes.getRowData();
		getDeslocamentoPeticaoService().excluir(deslocaPeticao);
		excluiu = true;
		listaPeticoesDaGuia.remove(deslocaPeticao);
		if (listaPeticoesDaGuia.size() == 0) {
			setMensagemInfoPopup("Guia removida!");
			comportamentoBotao(false, "btnSalvar", false);
			comportamentoBotao(false, "btnExcluir", false);
			comportamentoBotao(false, "btnImprimir", false);
			comportamentoBotao(true, "btnFechar", false);
			if (listaGuias.size() > 0) {
				listaGuias.clear();
			}
			// se não sobrou nenhum deslocamento excluir a guia também
			getGuiaService().excluir(guia);
		} else {
			if (excluiu && guia.getQuantidadeInternaProcesso() > 0) {
			    guia.setQuantidadeInternaProcesso(guia.getQuantidadeInternaProcesso()-1);
				getGuiaService().salvar(guia);
			}
		}
		atualizaInformacoesPopup();
	}

	public String getNumGuia() {
		return numGuia;
	}

	public void setNumGuia(String numGuia) {
		this.numGuia = numGuia;
	}

	public String getAnoGuia() {
		return anoGuia;
	}

	public void setAnoGuia(String anoGuia) {
		this.anoGuia = anoGuia;
	}

	public String getDescricaoGuia() {
		return descricaoGuia;
	}

	public void setDescricaoGuia(String descricaoGuia) {
		this.descricaoGuia = descricaoGuia;
	}

	public String getIdentificacaoProcessoPeticao() {
		return identificacaoProcessoPeticao;
	}

	public void setIdentificacaoProcessoPeticao(String identificacaoProcessoPeticao) {
		this.identificacaoProcessoPeticao = identificacaoProcessoPeticao;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getPeticaoProcesso() {
		return peticaoProcesso;
	}

	public void setPeticaoProcesso(String peticaoProcesso) {
		this.peticaoProcesso = peticaoProcesso;
	}

	public String getTipoGuia() {
		return tipoGuia;
	}

	public void setTipoGuia(String tipoGuia) {
		this.tipoGuia = tipoGuia;
	}

	public String getDescricaoDestino() {
		return descricaoDestino;
	}

	public void setDescricaoDestino(String descricaoDestino) {
		this.descricaoDestino = descricaoDestino;
	}

	public String getDescricaoOrigem() {
		return descricaoOrigem;
	}

	public void setDescricaoOrigem(String descricaoOrigem) {
		this.descricaoOrigem = descricaoOrigem;
	}

	public Long getCodigoDestino() {
		return codigoDestino;
	}

	public void setCodigoDestino(Long codigoDestino) {
		this.codigoDestino = codigoDestino;
	}

	public Long getCodigoOrigem() {
		return codigoOrigem;
	}

	public void setCodigoOrigem(Long codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public List<CheckableDataTableRowWrapper> getListaGuias() {
		return listaGuias;
	}

	public void setListaGuias(List<CheckableDataTableRowWrapper> listaGuias) {
		this.listaGuias = listaGuias;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaGuias() {
		return tabelaGuias;
	}

	public void setTabelaGuias(org.richfaces.component.html.HtmlDataTable tabelaGuias) {
		this.tabelaGuias = tabelaGuias;
	}

	public Boolean getChkDestinoLotacaoUsuario() {
		return chkDestinoLotacaoUsuario;
	}

	public void setChkDestinoLotacaoUsuario(Boolean chkDestinoLotacaoUsuario) {
		this.chkDestinoLotacaoUsuario = chkDestinoLotacaoUsuario;
	}

	public Boolean getChkOrigemLotacaoUsuario() {
		return chkOrigemLotacaoUsuario;
	}

	public void setChkOrigemLotacaoUsuario(Boolean chkOrigemLotacaoUsuario) {
		this.chkOrigemLotacaoUsuario = chkOrigemLotacaoUsuario;
	}

	public String getDescricaoLotacao() {
		return descricaoLotacao;
	}

	public void setDescricaoLotacao(String descricaoLotacao) {
		this.descricaoLotacao = descricaoLotacao;
	}

	public Long getCodigoLotacao() {
		return codigoLotacao;
	}

	public void setCodigoLotacao(Long codigoLotacao) {
		this.codigoLotacao = codigoLotacao;
	}

	public String getCodigoDescricaoLotacao() {
		return codigoDescricaoLotacao;
	}

	public void setCodigoDescricaoLotacao(String codigoDescricaoLotacao) {
		this.codigoDescricaoLotacao = codigoDescricaoLotacao;
	}

	public String getAnoPeticao() {
		return anoPeticao;
	}

	public void setAnoPeticao(String anoPeticao) {
		this.anoPeticao = anoPeticao;
	}

	public String getNumeroPeticao() {
		return numeroPeticao;
	}

	public void setNumeroPeticao(String numeroPeticao) {
		this.numeroPeticao = numeroPeticao;
	}

	public String getSiglaProcesso() {
		return siglaProcesso;
	}

	public void setSiglaProcesso(String siglaProcesso) {
		this.siglaProcesso = siglaProcesso;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public boolean isGuiaRecebida() {
		return guiaRecebida;
	}

	public void setGuiaRecebida(boolean guiaRecebida) {
		this.guiaRecebida = guiaRecebida;
	}

	public boolean isProcesso() {
		return isProcesso;
	}

	public void setProcesso(boolean isProcesso) {
		this.isProcesso = isProcesso;
	}

	public boolean isPeticao() {
		return isPeticao;
	}

	public void setPeticao(boolean isPeticao) {
		this.isPeticao = isPeticao;
	}

	public boolean isPesquisarTodos() {
		return pesquisarTodos;
	}

	public void setPesquisarTodos(boolean pesquisarTodos) {
		this.pesquisarTodos = pesquisarTodos;
	}

	public static List<String> getClasses() {
		return classes;
	}

	public static void setClasses(List<String> classes) {
		BeanPesquisarGuia.classes = classes;
	}

	public String getHintSituacaoGuia() {
		return hintSituacaoGuia;
	}

	public void setHintSituacaoGuia(String hintSituacaoGuia) {
		this.hintSituacaoGuia = hintSituacaoGuia;
	}

	public org.richfaces.component.html.HtmlModalPanel getPopupEditarGuia() {
		return popupEditarGuia;
	}

	public void setPopupEditarGuia(org.richfaces.component.html.HtmlModalPanel popupEditarGuia) {
		this.popupEditarGuia = popupEditarGuia;
	}

	public String getMensagemAvisoPopup() {
		return mensagemAvisoPopup;
	}

	public void setMensagemAvisoPopup(String mensagemAvisoPopup) {
		this.mensagemAvisoPopup = mensagemAvisoPopup;
	}

	public String getMensagemErroPopup() {
		return mensagemErroPopup;
	}

	public void setMensagemErroPopup(String mensagemErroPopup) {
		this.mensagemErroPopup = mensagemErroPopup;
	}

	public String getMensagemInfoPopup() {
		return mensagemInfoPopup;
	}

	public void setMensagemInfoPopup(String mensagemInfoPopup) {
		this.mensagemInfoPopup = mensagemInfoPopup;
	}

	public Long getNumeroGuiaPopup() {
		return numeroGuiaPopup;
	}

	public void setNumeroGuiaPopup(Long numeroGuiaPopup) {
		this.numeroGuiaPopup = numeroGuiaPopup;
	}

	public Short getAnoGuiaPopup() {
		return anoGuiaPopup;
	}

	public void setAnoGuiaPopup(Short anoGuiaPopup) {
		this.anoGuiaPopup = anoGuiaPopup;
	}

	public Long getCodigoOrigemPopup() {
		return codigoOrigemPopup;
	}

	public void setCodigoOrigemPopup(Long codigoOrigemPopup) {
		this.codigoOrigemPopup = codigoOrigemPopup;
	}

	public String getDescricaoOrigemPopup() {
		return descricaoOrigemPopup;
	}

	public void setDescricaoOrigemPopup(String descricaoOrigemPopup) {
		this.descricaoOrigemPopup = descricaoOrigemPopup;
	}

	public Long getCodigoDestinoPopup() {
		return codigoDestinoPopup;
	}

	public void setCodigoDestinoPopup(Long codigoDestinoPopup) {
		this.codigoDestinoPopup = codigoDestinoPopup;
	}

	public String getDescricaoDestinoPopup() {
		return descricaoDestinoPopup;
	}

	public void setDescricaoDestinoPopup(String descricaoDestinoPopup) {
		this.descricaoDestinoPopup = descricaoDestinoPopup;
	}

	public Long getCodigoNovoDestinoPopup() {
		return codigoNovoDestinoPopup;
	}

	public void setCodigoNovoDestinoPopup(Long codigoNovoDestinoPopup) {
		this.codigoNovoDestinoPopup = codigoNovoDestinoPopup;
		setAtributo(CODIGO_NOVO_DESTINO_POPUP, codigoNovoDestinoPopup);
	}

	public String getDescricaoNovoDestinoPopup() {
		return descricaoNovoDestinoPopup;
	}

	public void setDescricaoNovoDestinoPopup(String descricaoNovoDestinoPopup) {
		this.descricaoNovoDestinoPopup = descricaoNovoDestinoPopup;
	}

	public String getObservacaoPopup() {
		return observacaoPopup;
	}

	public void setObservacaoPopup(String observacaoPopup) {
		this.observacaoPopup = observacaoPopup;
	}

	public Guia getGuia() {
		return guia;
	}

	public void setGuia(Guia guia) {
		this.guia = guia;
	}

	public List<DeslocaProcesso> getListaProcessosDaGuia() {
		return listaProcessosDaGuia;
	}

	public void setListaProcessosDaGuia(List<DeslocaProcesso> listaProcessosDaGuia) {
		this.listaProcessosDaGuia = listaProcessosDaGuia;
	}

	public List<DeslocaPeticao> getListaPeticoesDaGuia() {
		return listaPeticoesDaGuia;
	}

	public void setListaPeticoesDaGuia(List<DeslocaPeticao> listaPeticoesDaGuia) {
		this.listaPeticoesDaGuia = listaPeticoesDaGuia;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaProcessos() {
		return tabelaProcessos;
	}

	public void setTabelaProcessos(org.richfaces.component.html.HtmlDataTable tabelaProcessos) {
		this.tabelaProcessos = tabelaProcessos;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaPeticoes() {
		return tabelaPeticoes;
	}

	public void setTabelaPeticoes(org.richfaces.component.html.HtmlDataTable tabelaPeticoes) {
		this.tabelaPeticoes = tabelaPeticoes;
	}

	public Integer getQuantidadeVinculados() {
		return quantidadeVinculados;
	}

	public void setQuantidadeVinculados(Integer quantidadeVinculados) {
		this.quantidadeVinculados = quantidadeVinculados;
	}

	public Short getTipoNovoDestinoPopup() {
		return tipoNovoDestinoPopup;
	}

	public void setTipoNovoDestinoPopup(Short tipoNovoDestinoPopup) {
		this.tipoNovoDestinoPopup = tipoNovoDestinoPopup;
		setAtributo(TIPO_NOVO_DESTINO_POPUP, tipoNovoDestinoPopup);
	}

	public Boolean getPostal() {
		return postal;
	}

	public void setPostal(Boolean postal) {
		this.postal = postal;
	}

	public Short getTipoDestino() {
		return tipoDestino;
	}

	public void setTipoDestino(Short tipoDestino) {
		this.tipoDestino = tipoDestino;
	}

	public String getIdProcessoPeticaoPopup() {
		return idProcessoPeticaoPopup;
	}

	public void setIdProcessoPeticaoPopup(String idProcessoPeticaoPopup) {
		this.idProcessoPeticaoPopup = idProcessoPeticaoPopup;
	}

	public String getRespostaExcluirGuia() {
		return respostaExcluirGuia;
	}

	public void setRespostaExcluirGuia(String respostaExcluirGuia) {
		setAtributo(RESPOSTA_EXCLUIR_GUIA, respostaExcluirGuia);
		this.respostaExcluirGuia = respostaExcluirGuia;
	}

	public Boolean getTemPermissaoAlterar() {
		return temPermissaoAlterar;
	}

	public void setTemPermissaoAlterar(Boolean temPermissaoAlterar) {
		this.temPermissaoAlterar = temPermissaoAlterar;
	}

}
