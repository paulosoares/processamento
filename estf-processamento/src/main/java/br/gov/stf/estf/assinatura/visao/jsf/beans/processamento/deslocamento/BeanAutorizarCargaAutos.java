package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.EmprestimoAutosProcesso;
import br.gov.stf.estf.entidade.jurisdicionado.EnderecoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.TelefoneJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.estf.entidade.jurisdicionado.TipoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoAssociacao;
import br.gov.stf.estf.entidade.jurisdicionado.util.JurisdicionadoResult;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

/**
 * 
 * @author Ricardo Zonta Leão
 *
 */
public class BeanAutorizarCargaAutos extends AssinadorBaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object CLASSE_NUMERO_PROCESSO = new Object();
	private static final Object LISTA_PROCESSOS = new Object();
	private static final Object LISTA_RESPONSAVEIS = new Object();
	private static final Object NOME_RESPONSAVEL = new Object();
	private static final Object CODIGO_RESPONSAVEL = new Object();
	private static final Object CODIGO_RESPONSAVEL_SELECIONADO = new Object();
	private static final Object CPF_RESPONSAVEL = new Object();
	private static final Object PAPEL_RESPONSAVEL = new Object();
	private org.richfaces.component.html.HtmlDataTable tabelaProcessosInteresse;
	private org.richfaces.component.html.HtmlDataTable tabelaProcessos;
	private org.richfaces.component.html.HtmlDataTable tabelaResponsaveis;
	private static final Object CODIGO_AUTORIZADOR = new Object();
	private static final Object LISTA_DEPENDENCIAS = new Object();
	private static final Object DATA_DEVOLUCAO = new Object();
	private static final Object NUMERO_ANO_GUIA = new Object();
	private static final Object EXISTE_VINCULO = new Object();
	private static final Object LISTA_AUTORIZADORES = new Object();
	private static final Object TEM_MAIS_AUTORIZADORES = new Object();
	private static final Object CODIGO_AUTORIZADOR_SELECIONADO = new Object();
	private static final Object LISTA_SEQ_VINCULOS_EXCLUIDOS = new Object();
	private static final Object SUCESSO = new Object();
	private static final Object ADVOGADO = new Object();
	private static final Object MENSAGEM_POPUP = new Object();
	private static final Object OBS_DESLOCAMENTO = new Object();
	private static final Object CHK_ENTIDADE_GOVERNAMENTAL = new Object();
	private static final Object LISTA_SEQ_OBJETOS_INCIDENTES = new Object();
	private static final Object NOVO_AUTORIZADO = new Object();
	private static final Object ITENS_UF_OAB = new Object();
	private static final Object NOVO_AUTORIZADO_SALVO = new Object();

	@KeepStateInHttpSession
	private String classeNumeroProcesso;
	private List<CheckableDataTableRowWrapper> listaProcessos;
	private String nomeResponsavel;
	// idenficador único na grid dos responsáveis formado pelo codigo do autorizado concatenado com o codigo do autorizador
	private Long codigoResponsavelSelecionado;
	private Long codigoResponsavel;
	private String cpfResponsavel;
	private String papelResponsavel;
	private List<CheckableDataTableRowWrapper> listaResponsaveis;
	private Long codigoAutorizador;
	private String aviso;
	private List<CheckableDataTableRowWrapper> listaDependencias;
	private List<SelectItem> listaAutorizadores;
	private Date dataDevolucao;
	private String numeroAnoGuia;
	private String existeVinculo;
	private String temMaisAutorizadores;
	private Long codigoAutorizadorSelecionado;
	private String hintExclusao;
	private String iconeExclusao;
	private List<Long> listaSeqVinculosExcluidos;
	private Boolean sucesso;
	private String obsDeslocamento; // observação do deslocamento
	
	private Jurisdicionado advogado;
	private String mensagemPopup;
	private Boolean chkEntidadeGovernamental;
	private List<Long> listaSeqObjetosIncidentes;
	
	// novo autorizado
	private String tipoPapelNovoAutorizado;
	private Jurisdicionado novoAutorizado;
	private String cpfNovoAutorizado;
	private Boolean existeCadastro;
	private Boolean isCPFErrado;
//	private HtmlInputHidden inputVerificaCadastroPessoa;
	private Jurisdicionado jurisdicionadoJaCadastrado;
	private String obsNovoAutorizado;
	private Date dataValidadeCadastro;
	private List<SelectItem> itensUfOAB;
	private String ufOAB;
	private String oabNovoAutorizado;
	private String rgNovoAutorizado;
	private boolean novoAutorizadoSalvo = false;
	
	
	public BeanAutorizarCargaAutos() {
		restauraSessao();
	}
	
	private void atualizaSessao(){
		setAtributo(CLASSE_NUMERO_PROCESSO, classeNumeroProcesso);
		setAtributo(LISTA_PROCESSOS, listaProcessos);
		setAtributo(LISTA_RESPONSAVEIS, listaResponsaveis);
		setAtributo(NOME_RESPONSAVEL, nomeResponsavel);
		setAtributo(CODIGO_RESPONSAVEL, codigoResponsavel);
		setAtributo(CODIGO_RESPONSAVEL_SELECIONADO, codigoResponsavelSelecionado);
		setAtributo(CPF_RESPONSAVEL, cpfResponsavel);
		setAtributo(PAPEL_RESPONSAVEL, papelResponsavel);
		setAtributo(CODIGO_AUTORIZADOR, codigoAutorizador);
		setAtributo(LISTA_DEPENDENCIAS, listaDependencias);
		setAtributo(DATA_DEVOLUCAO, dataDevolucao);
		setAtributo(NUMERO_ANO_GUIA, numeroAnoGuia);
		setAtributo(EXISTE_VINCULO, existeVinculo);
		setAtributo(LISTA_AUTORIZADORES, listaAutorizadores);
		setAtributo(TEM_MAIS_AUTORIZADORES, temMaisAutorizadores);
		setAtributo(CODIGO_AUTORIZADOR_SELECIONADO, codigoAutorizadorSelecionado);
		setAtributo(LISTA_SEQ_VINCULOS_EXCLUIDOS, listaSeqVinculosExcluidos);
		setAtributo(SUCESSO, sucesso);
		setAtributo(ADVOGADO, advogado);
		setAtributo(MENSAGEM_POPUP, mensagemPopup);
		setAtributo(OBS_DESLOCAMENTO, obsDeslocamento);
		setAtributo(CHK_ENTIDADE_GOVERNAMENTAL, chkEntidadeGovernamental);
		setAtributo(LISTA_SEQ_OBJETOS_INCIDENTES, listaSeqObjetosIncidentes);
		setAtributo(NOVO_AUTORIZADO, novoAutorizado);
		setAtributo(ITENS_UF_OAB, itensUfOAB);
		setAtributo(NOVO_AUTORIZADO_SALVO, novoAutorizadoSalvo);
	}
	
	@SuppressWarnings("unchecked")
	private void restauraSessao(){
		setClasseNumeroProcesso((String) getAtributo(CLASSE_NUMERO_PROCESSO));
		if (getAtributo(LISTA_PROCESSOS) == null) {
			setAtributo(LISTA_PROCESSOS, new ArrayList<CheckableDataTableRowWrapper>());
		}
		setListaProcessos((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_PROCESSOS));
		if (getAtributo(LISTA_RESPONSAVEIS) == null) {
			setAtributo(LISTA_RESPONSAVEIS, new ArrayList<CheckableDataTableRowWrapper>());
		}
		setListaResponsaveis((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_RESPONSAVEIS));
		if (getAtributo(LISTA_DEPENDENCIAS) == null) {
			setAtributo(LISTA_DEPENDENCIAS, new ArrayList<CheckableDataTableRowWrapper>());
		}
		setListaDependencias((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_DEPENDENCIAS));
		setNomeResponsavel((String) getAtributo(NOME_RESPONSAVEL));
		setCodigoResponsavelSelecionado((Long) getAtributo(CODIGO_RESPONSAVEL_SELECIONADO));
		setCodigoResponsavel((Long) getAtributo(CODIGO_RESPONSAVEL));
		setCpfResponsavel((String) getAtributo(CPF_RESPONSAVEL));
		setPapelResponsavel((String) getAtributo(PAPEL_RESPONSAVEL));
		setCodigoAutorizador((Long) getAtributo(CODIGO_AUTORIZADOR));
		setDataDevolucao((Date) getAtributo(DATA_DEVOLUCAO));
		setNumeroAnoGuia((String) getAtributo(NUMERO_ANO_GUIA));
		if (getAtributo(EXISTE_VINCULO) == null) {
			setAtributo(EXISTE_VINCULO, "N");
		}
		setExisteVinculo((String) getAtributo(EXISTE_VINCULO));
		if (getAtributo(LISTA_AUTORIZADORES) == null) {
			setAtributo(LISTA_AUTORIZADORES, new ArrayList<JurisdicionadoResult>());
		}
		setListaAutorizadores((List<SelectItem>) getAtributo(LISTA_AUTORIZADORES));
		if (getAtributo(TEM_MAIS_AUTORIZADORES) == null) {
			setAtributo(TEM_MAIS_AUTORIZADORES, "N");
		}
		setTemMaisAutorizadores((String) getAtributo(TEM_MAIS_AUTORIZADORES));
		setCodigoAutorizadorSelecionado((Long) getAtributo(CODIGO_AUTORIZADOR_SELECIONADO));
		if (getAtributo(LISTA_SEQ_VINCULOS_EXCLUIDOS) == null) {
			setAtributo(LISTA_SEQ_VINCULOS_EXCLUIDOS, new ArrayList<Long>());
		}
		setListaSeqVinculosExcluidos((List<Long>) getAtributo(LISTA_SEQ_VINCULOS_EXCLUIDOS));
		if (getAtributo(SUCESSO) == null) {
			setAtributo(SUCESSO, false);
		}
		setSucesso((Boolean) getAtributo(SUCESSO));
		setAdvogado((Jurisdicionado) getAtributo(ADVOGADO));
		if (getAtributo(MENSAGEM_POPUP) == null) {
			setAtributo(MENSAGEM_POPUP, "");
		}
		setMensagemPopup((String) getAtributo(MENSAGEM_POPUP));
		setObsDeslocamento((String) getAtributo(OBS_DESLOCAMENTO));
		if (getAtributo(CHK_ENTIDADE_GOVERNAMENTAL) == null) {
			setAtributo(CHK_ENTIDADE_GOVERNAMENTAL, false);
		}
		setChkEntidadeGovernamental((Boolean) getAtributo(CHK_ENTIDADE_GOVERNAMENTAL));
		if (getAtributo(LISTA_SEQ_OBJETOS_INCIDENTES) == null) {
			setAtributo(LISTA_SEQ_OBJETOS_INCIDENTES, new ArrayList<Long>());
		}
		setListaSeqObjetosIncidentes((List<Long>) getAtributo(LISTA_SEQ_OBJETOS_INCIDENTES));
		
		if (getAtributo(NOVO_AUTORIZADO) == null) {
			setAtributo(NOVO_AUTORIZADO, new Jurisdicionado());
		}
		setNovoAutorizado((Jurisdicionado) getAtributo(NOVO_AUTORIZADO));
		if( getAtributo(ITENS_UF_OAB) == null ){
			setAtributo(ITENS_UF_OAB,montarListaUf());
		}
		setItensUfOAB((List<SelectItem>) getAtributo(ITENS_UF_OAB));

		if (getAtributo(NOVO_AUTORIZADO_SALVO) == null) {
			setAtributo(NOVO_AUTORIZADO_SALVO, false);
		}
		setNovoAutorizadoSalvo((Boolean) getAtributo(NOVO_AUTORIZADO_SALVO));
	}
	
	public void atualizarChkEntidadeGovernamental() {
		setAtributo(CHK_ENTIDADE_GOVERNAMENTAL, chkEntidadeGovernamental);
	}
	
	public Boolean getTemMensagemPopup() {
		if ((getMensagemPopup() == null) || (getMensagemPopup().equals(""))) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<JurisdicionadoResult> pesquisarResponsavel(Object value) throws ServiceException {
		List<JurisdicionadoResult> listaResult;
		if (chkEntidadeGovernamental) {
//			listaResult = getJurisdicionadoService().pesquisarResultEntidadeGovernamental(value, listaSeqObjetosIncidentes);
			listaResult = getJurisdicionadoService().pesquisarResultEntidadeGovernamental(value, null);
		} else {
			listaResult = getJurisdicionadoService().pesquisarResult(value);
		}
		return listaResult;
		
	}
	
	
	public void incluirProcessoNaLista(ActionEvent evt) throws Exception{
		
		if (classeNumeroProcesso == null || classeNumeroProcesso.trim().equals("")) {
			return;
		}
		
		Long numeroProcesso = ProcessoParser.getNumero(classeNumeroProcesso);
		String classeProcesso = ProcessoParser.getSigla(classeNumeroProcesso);
		
		Processo processo = getProcessoService().recuperarProcesso(classeProcesso, numeroProcesso);
		String msgCritica = processoNoSetorDoUsuario(processo);
		if (msgCritica != null){
			reportarAviso(msgCritica);
			return;
		}
		if (processo.getTipoMeio().equals("E")) {
			reportarAviso("A carga somente pode ser realizada para processos físicos");
			return;
		}
		msgCritica = null;
		msgCritica = verificaProcessoApenso(processo);
		if (msgCritica != null) {
			reportarAviso(msgCritica);
			return;
		}
		
		CheckableDataTableRowWrapper chk = new CheckableDataTableRowWrapper(processo);
		if (listaProcessos == null) {
			listaProcessos = new ArrayList<CheckableDataTableRowWrapper>();
		}
		msgCritica = processoJaAdicionado(processo, listaProcessos);
		if (msgCritica != null) {
			reportarAviso(msgCritica);
			return;
		}
		listaProcessos.add(chk);
		if (listaSeqObjetosIncidentes == null) {
			listaSeqObjetosIncidentes = new ArrayList<Long>();
		}
		listaSeqObjetosIncidentes.add(processo.getId());
		// recuperar os apensos
		listaDependencias.clear();
		recuperarApensos((Processo) chk.getWrappedObject());
		if (listaDependencias.size() > 0) {
			listaProcessos.addAll(listaDependencias);
		}
		atualizaSessao();
	}
	
	// veririfica se o processo que está sendo incluído é apenso de outro (principal)
	public String verificaProcessoApenso(Processo processo) {
		try{
		ProcessoDependencia processoDependencia = getProcessoDependenciaService().getProcessoVinculador(processo); 
		if (processoDependencia != null) {
			return "Não é possível deslocar o processo " + 
					processoDependencia.getClasseProcesso() + "-" + 
					processoDependencia.getNumeroProcesso() + ", pois ele está apensado ao processo " +
					processoDependencia.getClasseProcessoVinculador() + "-" + 
					processoDependencia.getNumeroProcessoVinculador() + ".";
		} else {
			return null;
		}
		} catch (Exception e) {
			return null;
		}
	}
	
	public void confirmarAutorizador(ActionEvent evt) throws ServiceException {
			confirmarAutorizador();
		try {
		} catch (Exception e) {
			reportarErro("Não foi possível inserir o autorizador.");
		}
	}
	
	private void confirmarAutorizador() throws ServiceException{
		JurisdicionadoResult novoAutorizado = getJurisdicionadoService().pesquisarResultPorId(codigoResponsavel);
		novoAutorizado.setAutorizador(false);
		novoAutorizado.setCodigoAutorizador(codigoAutorizadorSelecionado);
		novoAutorizado.setIdAssociacao(null);
		String idAutorizadoAutorizador = codigoResponsavel.toString() + codigoAutorizadorSelecionado.toString();
		novoAutorizado.setIdAutorizadoAutorizador(new Long(idAutorizadoAutorizador));

		if (membroJaIncluido(novoAutorizado, listaResponsaveis)) {
			reportarAviso("Autorizado já incluído.");
			return;
		}
		if (novoAutorizado.getId().equals(codigoAutorizadorSelecionado)) {
			reportarAviso("Autorizado e autorizador são iguais.");
			return;
		}
		CheckableDataTableRowWrapper wrapperAutorizado = new CheckableDataTableRowWrapper(novoAutorizado, novoAutorizado.getIdAutorizadoAutorizador());
		// recuperar o autorizador
		CheckableDataTableRowWrapper chkAutorizador = recuperarChkResponsavelSelecionado(codigoAutorizadorSelecionado);
		if (chkAutorizador == null) {
			reportarErro("Não foi possível recuperar o autorizador na inserção do autorizado");
		}
		listaResponsaveis.add(listaResponsaveis.indexOf(chkAutorizador)+1, wrapperAutorizado);
	}
	
	private String processoJaAdicionado(Processo processo, List<CheckableDataTableRowWrapper> processos) {
		for (CheckableDataTableRowWrapper checkable: processos) {
			Processo item = (Processo) checkable.getWrappedObject();
			if (item.getId().equals(processo.getId())) {
				return "Processo já adicionado.";
			}
		}
		return null;
	}
	
	// verifica se o processo encontra-se no setor do usuário que está realizando a carga
	private String processoNoSetorDoUsuario(Processo processo) throws ServiceException{
		try {
			DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
			if (deslocaProcesso != null) {
				if (deslocaProcesso.getDataRecebimento() == null) {
					return "O processo " + deslocaProcesso.getClasseProcesso() + "/" + deslocaProcesso.getNumeroProcesso() + " está em trânsito.";
				}
				// verificar se o processo que está sendo incluído não está no setor do usuário 
				Long ultimoSetorDeslocado = deslocaProcesso.getCodigoOrgaoDestino();
				if ( (ultimoSetorDeslocado == null) || (!ultimoSetorDeslocado.equals(getSetorUsuarioAutenticado().getId())) ){
					return "O processo " + processo.getSiglaClasseProcessual() + "-" + processo.getNumeroProcessual() + " não se encontra no setor do usuário " + getUser().getUsername();
				}
			}
		} catch (Exception e) {
			reportarErro("Erro na validação do processo.");
		}
		return null;
		
	}
	
	
	// recupera os apensos de um determinado processo
	public void recuperarApensos(Processo processo) throws Exception {

		List<Processo> processos = new ArrayList<Processo>();
		List<ProcessoDependencia> apensos = getProcessoDependenciaService().recuperarApensos(processo);

		// transforma o processoDependencia em processo e adiciona na lista
		for (ProcessoDependencia apenso: apensos ) {
			Processo processoApenso = getProcessoService().recuperarProcesso(apenso.getClasseProcesso(), apenso.getNumeroProcesso());
			processos.add(processoApenso);
			listaDependencias.add(new CheckableDataTableRowWrapper(processoApenso));
			recuperarApensos(processoApenso);
		}
	}
	
	public boolean getIsApenso() throws ServiceException {
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaProcessos.getRowData();
		Processo processo = (Processo) chkDataTable.getWrappedObject();
		return getProcessoDependenciaService().isApenso(processo);
	}
	
	// retorna true se um autorizado teve emprestimo, ou seja, se sua associação autorizador->autorizado foi utilizada 
	// em um empréstimo 
	public boolean getAssociacaoTemEmprestimo() throws ServiceException{
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaResponsaveis.getRowData();
		JurisdicionadoResult autorizado = (JurisdicionadoResult) chkDataTable.getWrappedObject();
		if (autorizado.getIdAssociacao() == null) {
			return false;
		}
		AssociacaoJurisdicionado associacao = getAssociacaoJurisdicionadoService().recuperarPorId(autorizado.getIdAssociacao());
		return getEmprestimoAutosProcessoService().existeEmprestimoParaAssociacao(associacao);
	}
	
	// método utilizado ao selecionar um item do suggestion do responsável
	// responsável por recuperar a árvore (se já existir vínculos entre autorizadores e autorizados)
	public void recuperarMembros() throws ServiceException{
		// não recuperar o responsável autorizador e os autorizados se já foi recuperado
		if (listaResponsaveis.size() > 0) {
			atualizaSessao();
			return;
		}
		if (codigoResponsavel == null) {
			reportarAviso("Favor selecionar um responsável através das sugestões oferecidas quando um CPF, código ou nome for digitado.");
			return;
		}
		Jurisdicionado jurisdicionado = new Jurisdicionado();
		jurisdicionado.setId(codigoResponsavel);
		if (chkEntidadeGovernamental) {
			jurisdicionado.setEntidadeGovernamental(true);
		} else {
			jurisdicionado.setEntidadeGovernamental(false);
		}
		
		List<JurisdicionadoResult> arvoreResponsavel = new ArrayList<JurisdicionadoResult>();
		List<JurisdicionadoResult> listaAutorizadosAdvogado = new ArrayList<JurisdicionadoResult>();
		List<CheckableDataTableRowWrapper> listaChkAdvogado = new ArrayList<CheckableDataTableRowWrapper>();
		
		listaAutorizadores.clear();
		listaAutorizadosAdvogado.clear();
		//
		List<AssociacaoJurisdicionado> listaArvore = getAssociacaoJurisdicionadoService().pesquisarAssociacoes(jurisdicionado);
		for (AssociacaoJurisdicionado associacao: listaArvore) {
			if (!grupoJaIncluido(associacao, arvoreResponsavel)) {
				JurisdicionadoResult responsavelGrupo = new JurisdicionadoResult();
				responsavelGrupo.setId(associacao.getGrupo().getJurisdicionado().getId());
				responsavelGrupo.setNome(associacao.getGrupo().getJurisdicionado().getNome());
				responsavelGrupo.setCpf(associacao.getGrupo().getJurisdicionado().getIdentificadoresJurisdicionado().get(0).getDescricaoIdentificacao());
				responsavelGrupo.setPapel(associacao.getGrupo().getTipoJurisdicionado().getDescricaoTipoJurisdicionado());
				responsavelGrupo.setAutorizador(true);
				responsavelGrupo.setIdAutorizadoAutorizador(associacao.getGrupo().getJurisdicionado().getId());
				responsavelGrupo.setIdAssociacao(associacao.getId());
				responsavelGrupo.setIdPapel(associacao.getGrupo().getId());
				
				arvoreResponsavel.add(responsavelGrupo);
				codigoAutorizador = responsavelGrupo.getId();
				CheckableDataTableRowWrapper wrapperAutorizador = new CheckableDataTableRowWrapper(responsavelGrupo, responsavelGrupo.getIdAutorizadoAutorizador());
				listaResponsaveis.add(wrapperAutorizador);
				//
				if (listaAutorizadores == null) {
					listaAutorizadores = new ArrayList<SelectItem>();
				}
				SelectItem selectItem = new SelectItem(responsavelGrupo.getId(), responsavelGrupo.getNome());
				adicionarAutorizadorNoSelectItem(selectItem);
			}
			JurisdicionadoResult responsavelMembro = new JurisdicionadoResult();

			responsavelMembro.setId(associacao.getMembro().getJurisdicionado().getId());
			responsavelMembro.setNome(associacao.getMembro().getJurisdicionado().getNome());
			responsavelMembro.setCpf(associacao.getMembro().getJurisdicionado().getIdentificadoresJurisdicionado().get(0).getDescricaoIdentificacao());
			responsavelMembro.setPapel(associacao.getMembro().getTipoJurisdicionado().getDescricaoTipoJurisdicionado());
			responsavelMembro.setAutorizador(false);
			responsavelMembro.setCodigoAutorizador(associacao.getGrupo().getJurisdicionado().getId());
			String idAutorizadoAutorizador = associacao.getMembro().getJurisdicionado().getId().toString() + associacao.getGrupo().getJurisdicionado().getId().toString();
			responsavelMembro.setIdAutorizadoAutorizador(new Long(idAutorizadoAutorizador));
			responsavelMembro.setIdAssociacao(associacao.getId());
			responsavelMembro.setIdPapel(associacao.getMembro().getId());

			arvoreResponsavel.add(responsavelMembro);
			CheckableDataTableRowWrapper wrapperAutorizado = new CheckableDataTableRowWrapper(responsavelMembro, responsavelMembro.getIdAutorizadoAutorizador());
			listaResponsaveis.add(wrapperAutorizado);
			// armazena os autorizados que são advogados
			if (associacao.getMembro().getTipoJurisdicionado().getDescricaoTipoJurisdicionado().equals("Advogado")) {
				JurisdicionadoResult autorizadoAdvogado = new JurisdicionadoResult();
				autorizadoAdvogado.setAutorizador(true);
				autorizadoAdvogado.setCpf(responsavelMembro.getCpf());
				autorizadoAdvogado.setId(responsavelMembro.getId());
				autorizadoAdvogado.setIdAssociacao(responsavelMembro.getIdAssociacao());
				autorizadoAdvogado.setIdAutorizadoAutorizador(responsavelMembro.getId());
				autorizadoAdvogado.setIdPapel(responsavelMembro.getIdPapel());
				autorizadoAdvogado.setNome(responsavelMembro.getNome());
				autorizadoAdvogado.setPapel(responsavelMembro.getPapel());
				listaAutorizadosAdvogado.add(autorizadoAdvogado);
				// para poder selecionar na interface quando existir mais de um autorizador
				SelectItem selectItem = new SelectItem(autorizadoAdvogado.getId(), autorizadoAdvogado.getNome());
				adicionarAutorizadorNoSelectItem(selectItem);
				CheckableDataTableRowWrapper chkAutorizadorAdvogado = new CheckableDataTableRowWrapper(autorizadoAdvogado, autorizadoAdvogado.getIdAutorizadoAutorizador());
				CheckableDataTableRowWrapper chkAutorizadoAdvogado = new CheckableDataTableRowWrapper(responsavelMembro, responsavelMembro.getIdAutorizadoAutorizador());
				adicionarAdvogadoNaLista(chkAutorizadorAdvogado, chkAutorizadoAdvogado);
			}
		}
		
		if (listaResponsaveis.size() == 0) {
			setExisteVinculo("N");
			comportamentoBotao(true, "btnAddAutorizador", true);
			comportamentoBotao(false, "btnAddAutorizado", true);
			comportamentoBotao(false, "btnSalvarVinculo", true);
		} else {
			setExisteVinculo("S");
			comportamentoBotao(false, "btnAddAutorizador", true);
			comportamentoBotao(true, "btnAddAutorizado", true);
			comportamentoBotao(true, "btnSalvarVinculo", true);
			comportamentoBotao(true, "btnNovaPessoa", true);
			chkEntidadeGovernamental = false; // desmarca a opção entidade governamental para a pesquisa
		}
		// acrescenta os autorizados advogados também no inicio da lista como opção para serem utilizados como autorizadores
		if (listaAutorizadosAdvogado.size()>0) {
			for (JurisdicionadoResult autorizadoAdvogado: listaAutorizadosAdvogado) {
//				listaResponsaveis.add(0, new CheckableDataTableRowWrapper(autorizadoAdvogado, autorizadoAdvogado.getIdAutorizadoAutorizador()));
				listaChkAdvogado.add(0, new CheckableDataTableRowWrapper(autorizadoAdvogado, autorizadoAdvogado.getIdAutorizadoAutorizador()));
			}
		}
		
//		listaResponsaveis.removeAll(listaChkAdvogado); // remove antes para não haver duplicidades.
//		listaResponsaveis.addAll(listaChkAdvogado);
		
		if (listaAutorizadores.size()>1) {
			setTemMaisAutorizadores("S");
		} else {
			setTemMaisAutorizadores("N");
		}
		atualizaSessao();
	}
	
	// verifica se já existe antes de adicionar o elemento na lista
	private void adicionarAutorizadorNoSelectItem(SelectItem novoSelectItem) {
		boolean jaExiste = false;
		for (SelectItem selectItem: listaAutorizadores) {
			if (selectItem.getValue().equals(novoSelectItem.getValue())) {
				jaExiste = true;
			}
		}
		if (!jaExiste) {
			listaAutorizadores.add(novoSelectItem);
		}
	}
	// verifica se já existe antes de adicionar o elemento na lista
	// autorizadorAdvogado = novo item que será adicionado = um advogado que é autorizado irá ser incluido como autorizador.
	// autorizadoAdvogado = o item para o qual o novo item será incluído. Um autorizado advogado.
	private void adicionarAdvogadoNaLista(CheckableDataTableRowWrapper autorizadorAdvogado, CheckableDataTableRowWrapper autorizadoAdvogado) {
		boolean jaExiste = false;
		for (CheckableDataTableRowWrapper checkable: listaResponsaveis) {
			if (checkable.getId().equals(autorizadorAdvogado.getId())) {
				jaExiste = true;
			}
		}
		if (!jaExiste) {
			int indice = listaResponsaveis.indexOf(autorizadoAdvogado);
			for (int i=indice; i<=listaResponsaveis.size(); i--) {
				JurisdicionadoResult itemJurisdicionadoResult = (JurisdicionadoResult) listaResponsaveis.get(i).getWrappedObject(); 
				if (itemJurisdicionadoResult.getAutorizador()) {
					listaResponsaveis.add(i, autorizadorAdvogado);
					break;
				}
			}
		}
	}
	
	
	/**
	 * Verifica se o grupo (responsável autorizador) já foi incluído na árvore
	 * @param associacao
	 * @param listaResult
	 * @return booleano
	 */
	public Boolean grupoJaIncluido(AssociacaoJurisdicionado associacao, List<JurisdicionadoResult> listaResult ) {
		Boolean encontrou = false;
		for (JurisdicionadoResult itemResult: listaResult) {
			if ( itemResult.getId().equals(associacao.getGrupo().getJurisdicionado().getId()) ) {
				if ( itemResult.getAutorizador() ) {
					encontrou = true;
					break;
				} else {
					encontrou = false;
				}
			} else {
				encontrou = false;
			}
		}
		return encontrou;
	}
	
	/**
	 * Verifica se o membro (responsável autorizado) já foi incluído para o mesmo autorizador.
	 * @param associacao
	 * @param listaResult
	 * @return booleano
	 */
	public Boolean membroJaIncluido(JurisdicionadoResult autorizado, List<CheckableDataTableRowWrapper> listaResult ) {
		Boolean encontrou = false;
		for (CheckableDataTableRowWrapper itemResult: listaResult) {
			JurisdicionadoResult itResult = (JurisdicionadoResult) itemResult.getWrappedObject();
			if ( itResult.getIdAutorizadoAutorizador().equals(autorizado.getIdAutorizadoAutorizador()) ) {
				if ( !itResult.getAutorizador() && (itResult.getCodigoAutorizador().equals(autorizado.getCodigoAutorizador())) ) {
					encontrou = true;
					break;
				} else {
					encontrou = false;
				}
			} else {
				encontrou = false;
			}
		}
		return encontrou;
	}
	
	public void limparReponsaveis(ActionEvent evt) {
		listaResponsaveis.clear();
		nomeResponsavel = null;
		codigoResponsavel = null;
		codigoAutorizador = null;
		codigoAutorizadorSelecionado = null;
		codigoResponsavelSelecionado = null;
		listaSeqVinculosExcluidos.clear();
		comportamentoBotao(true, "btnAddAutorizador", true);
		comportamentoBotao(false, "btnAddAutorizado", true);
		comportamentoBotao(false, "btnNovaPessoa", true);

		atualizaSessao();
	}
	
	public void atualizarSessao(ActionEvent evt){
		atualizaSessao();
	}
	
	
	public void removerProcessoAction(ActionEvent evt) throws Exception{
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaProcessos.getRowData();
		listaProcessos.remove(chkDataTable);
		
		// remover apensos
		listaDependencias.clear();
		recuperarApensos((Processo) chkDataTable.getWrappedObject());
		listaProcessos.removeAll(listaDependencias);
		//
		atualizaSessao();
	}
	
	public void removerResponsavelAction(ActionEvent evt) throws ServiceException{
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaResponsaveis.getRowData();
		// exclusão não é possível para o autorizador (pai)
		if ( ((JurisdicionadoResult) chkDataTable.getWrappedObject()).getAutorizador() ) {
			return;
		}
		int totalAntesDaExclusao = listaResponsaveis.size();
		listaResponsaveis.remove(chkDataTable);
		// recuperar o id do vinculo para persistir a exclusão
		if (totalAntesDaExclusao > listaResponsaveis.size()) {
			JurisdicionadoResult responsavelRemovido = (JurisdicionadoResult) chkDataTable.getWrappedObject();
			listaSeqVinculosExcluidos.add( responsavelRemovido.getIdAssociacao() );
		}
		atualizaSessao();
	}
	
	// habilita ou desabilita botões
	private void comportamentoBotao(boolean habilitar, String id, boolean estendido) {
		try {
			int i = 0;
//			UIComponent form = evt.getComponent().getParent();
			UIComponent form = FacesContext.getCurrentInstance().getViewRoot().findComponent("form");
			while (form.findComponent(id) == null){
				form = form.getParent();
				i++;
				if (i > 20) {
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
//			UIComponent form = evt.getComponent().getParent();
			UIComponent form = FacesContext.getCurrentInstance().getViewRoot().findComponent("form");
			while (form.findComponent(id) == null){
				form = form.getParent();
				i++;
				if (i > 20) {
					throw new Exception("Componente de interface não localizado");
				}
			}
			form.findComponent(id).getAttributes().put("disabled", !habilitar);
//			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("foo:bar");
		} catch (Exception e) {
			reportarErro("Erro ao recuperar componente de interface");
		}
	}
	
	public Boolean getTemMensagem(){
		return getAviso() == null ? false: true;
	}

	// método utilizado pelo botão "Adicionar Autorizador" na inclusão manual de um responsável autorizador
	public void adicionarAutorizador(ActionEvent evt) throws ServiceException{
		if (listaResponsaveis.size()>0) { // se já tem informação na grid significa que um autorizador já foi incluído/recuperado
			atualizaSessao();
			return;
		}
		adicionaJurisdicionadoNaTabelaDeAutorizacao(true);
		// desabilitar o botão após a inclusão
		if (listaResponsaveis.size()>0){
			comportamentoBotao(false, "btnAddAutorizador", true);
			comportamentoBotao(true, "btnAddAutorizado", true);
			comportamentoBotao(true, "btnNovaPessoa", true);
		}
		setAviso(null);
	}
	
	// método utilizado pelo botão "Adicionar Autorizado" na inclusão manual de um responsável autorizado
	public void adicionarAutorizado(ActionEvent evt) throws ServiceException{
		if (listaResponsaveis.size()==0) { // um autorizado somente pode ser incluído se um autorizador tb já tenha sido incluído.
			atualizaSessao();
			reportarAviso("Não é possível adicionar um autorizado sem antes adicionar um autorizador.");
			return;
		}
		adicionaJurisdicionadoNaTabelaDeAutorizacao(false);
		setAviso(null);
	}

	private void adicionaJurisdicionadoNaTabelaDeAutorizacao(boolean isAutorizador) throws ServiceException {
		if (codigoResponsavel == null) {
			reportarAviso("Favor selecionar um responsável através das sugestões oferecidas quando um CPF, código ou nome for digitado.");
			return;
		}
		JurisdicionadoResult responsavel;
		if (chkEntidadeGovernamental) {
			responsavel = getJurisdicionadoService().pesquisarResultEntidadeGovernamentalPorId(codigoResponsavel);
		} else {
			responsavel = getJurisdicionadoService().pesquisarResultPorId(codigoResponsavel);
		}
		// upper no nome
		responsavel.setNome(responsavel.getNome().toUpperCase());
		//
		responsavel.setAutorizador(isAutorizador);
		if (!isAutorizador) {
			if (responsavel.getId().equals(codigoAutorizador) && (listaAutorizadores.size()==1) ) {
				reportarAviso("Autorizado e autorizador são iguais.");
				return;
			}
			String idAutorizadoAutorizador = responsavel.getId().toString() + codigoAutorizador.toString();
			responsavel.setCodigoAutorizador(codigoAutorizador);
			responsavel.setIdAutorizadoAutorizador(new Long(idAutorizadoAutorizador));
			if (membroJaIncluido(responsavel, listaResponsaveis)) {
				reportarAviso("Autorizado já incluído.");
				return;
			}
		} else {
			// se o autorizador não for advogado
			if (!getPapelJurisdicionadoService().recuperarPorId(responsavel.getIdPapel()).getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV")) {
				if (!responsavel.getEntidadeGovernamental()) {
					reportarAviso("O autorizador deve ser um advogado ou uma entidade governamental.");
					return;
				}
			}

			codigoAutorizador = responsavel.getId();
			responsavel.setIdAutorizadoAutorizador(responsavel.getId());
			//
			//listaAutorizadores.add(responsavel);
		}
		// somente adiciona se for autorizador ou autorizado com apenas um autorizador
		if (isAutorizador || (!isAutorizador && temMaisAutorizadores.equals("N"))) {
			CheckableDataTableRowWrapper wrapper = new CheckableDataTableRowWrapper(responsavel, responsavel.getIdAutorizadoAutorizador());
			//
			listaResponsaveis.add(wrapper);
		}
		setExisteVinculo("S");
//		comportamentoBotao(false, "btnLimparGridResponsavel", true);
		if (listaResponsaveis.size()>1) {
			comportamentoBotao(true, "btnSalvarVinculo", true);
		} else {
			comportamentoBotao(false, "btnSalvarVinculo", true);
		}
		
		chkEntidadeGovernamental = false;

		atualizaSessao();
	}
	
	// desabilita/habilita o chkbox do campo entidade governamental
	public Boolean getDesabilitaChkEntidadesGovernamentais() {
		if (listaResponsaveis.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// faz a recuperação do checkable a partir do idAutorizadoAutorizador (codigoResponsavelSelecionado)
	private CheckableDataTableRowWrapper recuperarChkResponsavelSelecionado(Long codResponsavelSelecionado){
		for (CheckableDataTableRowWrapper itemLista: listaResponsaveis) {
			JurisdicionadoResult itemJurisdicionadoResult = (JurisdicionadoResult) itemLista.getWrappedObject();
			if (itemJurisdicionadoResult.getIdAutorizadoAutorizador().equals(codResponsavelSelecionado)) {
				return itemLista;
			}
		}
		return null;
	}
	
	// faz a recuperação do jurisdicionado a partir do idAutorizadoAutorizador (codigoResponsavelSelecionado)
	private JurisdicionadoResult recuperarResponsavelSelecionado(Long codResponsavelSelecionado){
		for (CheckableDataTableRowWrapper itemLista: listaResponsaveis) {
			JurisdicionadoResult itemJurisdicionadoResult = (JurisdicionadoResult) itemLista.getWrappedObject();
			if (itemJurisdicionadoResult.getIdAutorizadoAutorizador().equals(codResponsavelSelecionado)) {
				return itemJurisdicionadoResult;
			}
		}
		return null;
	}
	
	// recupera o pai (autorizador) do autorizado selecionado.
	private JurisdicionadoResult recuperarPaiResponsavelSelecionado(Long codResponsavelSelecionado){
		for (CheckableDataTableRowWrapper itemLista: listaResponsaveis) {
			JurisdicionadoResult itemJurisdicionadoResult = (JurisdicionadoResult) itemLista.getWrappedObject();
			if (itemJurisdicionadoResult.getIdAutorizadoAutorizador().equals(codResponsavelSelecionado)) {
				if (itemJurisdicionadoResult.getAutorizador()) {
					return null;
				} else {
					JurisdicionadoResult jurisdicionadoResultPai = recuperarResponsavelSelecionado (itemJurisdicionadoResult.getCodigoAutorizador());
					return jurisdicionadoResultPai;
				}
			}
		}
		return null;
	}
	
	// retorna uma associação a partir dos papeis (autorizador (pai) e autorizado (filho))
	private AssociacaoJurisdicionado recuperarAssociacao(PapelJurisdicionado papelPai, PapelJurisdicionado papelFilho) throws ServiceException {
		try {
			EnumTipoAssociacao tipoAssociacao = null;
			if (papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV") 
					&& papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV")) {
				tipoAssociacao = EnumTipoAssociacao.ADVOGADO_ADVOGADO;
			} else if (papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV")
					&& papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("EST")) {
				tipoAssociacao = EnumTipoAssociacao.ADVOGADO_ESTAGIARIO;
			} else if (papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PART")
					&& papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("EST")) {
				tipoAssociacao = EnumTipoAssociacao.PARTE_ESTAGIARIO;
			} else if (papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PART")
					&& papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV")) {
				tipoAssociacao = EnumTipoAssociacao.PARTE_ADVOGADO;
			} else if (papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PART")
					&& papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PREPO")) {
				tipoAssociacao = EnumTipoAssociacao.PARTE_PREPOSTO;
			} else if (papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV")
					&& papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PREPO")) {
				tipoAssociacao = EnumTipoAssociacao.ADVOGADO_PREPOSTO;
			} else {
				throw new ServiceException("Não foi possível identificar a associação!");
			}
			return getAssociacaoJurisdicionadoService().recuperarPorGrupoMembro(papelPai, papelFilho, tipoAssociacao);
		} catch (Exception e) {
			reportarErro("Não foi possível recuperar a associação entre jurisdicionados.");
			//return null;
			throw new ServiceException(e);
		}
		
	}
	
	/**
	 * imprimir guia de retirada de processos
	 * @param evt
	 */
	
	public void imprimirGuia(ActionEvent evt) {
		try {
			if (numeroAnoGuia == null) {
				throw new Exception("Guia para impressão não localizada!");
			}
			String numero_guia = numeroAnoGuia.substring(0, numeroAnoGuia.indexOf("/"));
			String ano_guia = numeroAnoGuia.substring(numeroAnoGuia.indexOf("/") + 1, numeroAnoGuia.length());
	
			byte[] arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaRetiradaAutosProcesso(new Long(numero_guia), new Short(ano_guia),
					new Long(getSetorUsuarioAutenticado().getId()), false);
			ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
			mandarRespostaDeDownloadDoArquivo(input, "GuiaRetiradaAutos_" + numero_guia + "_" + ano_guia + ".pdf");
		
		} catch (Exception e) {
			reportarErro("Ocorreu um erro na impressão da guia. " + e.getMessage());
		}
	}

	private void mandarRespostaDeDownloadDoArquivo(ByteArrayInputStream input, String nomeRelatorio) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-disposition", "attachment; filename=\"" + nomeRelatorio + "\"");
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

	/**
	 * Retorna a situação atual para a situação inicial
	 * @param evt
	 */
	public void novaCarga(ActionEvent evt) {
		listaResponsaveis.clear();
		listaAutorizadores.clear();
		listaProcessos.clear();
		listaDependencias.clear();
		listaSeqVinculosExcluidos.clear();
		nomeResponsavel = null;

		classeNumeroProcesso = null;
		codigoResponsavelSelecionado = null;
		codigoResponsavel = null;
		cpfResponsavel = null;
		papelResponsavel = null;
		codigoAutorizador = null;
		aviso = null;
		dataDevolucao = null;
		numeroAnoGuia = null;
		existeVinculo = "N";
		temMaisAutorizadores = "N";
		codigoAutorizadorSelecionado = null;
		hintExclusao = null;
		iconeExclusao = null;
		sucesso = false;
		obsDeslocamento = null;
		comportamentoBotao(true, "btnSalvarCarga", true);
		comportamentoBotao(false, "btnImprimirGuia", true);
		comportamentoBotao(false, "btnNovaCarga", false);
		comportamentoBotao(true, "btnAddAutorizador", true);
		comportamentoBotao(false, "btnAddAutorizado", true);
		comportamentoInput(true, "idProcesso");
		comportamentoInput(true, "itResponsavel");
		comportamentoInput(true, "itDataDevolucao");
		comportamentoInput(true, "btnIncluirProcesso");
		comportamentoBotao(true, "btnLimparGridResponsavel", true);
		comportamentoBotao(false, "btnSalvarVinculo", true);
		comportamentoBotao(false, "btnNovaPessoa", true);
		
		atualizaSessao();
	}
	
	/**
	 * Consiste em salvar o vínculo entre Advogao-Advogado, Advogado-Estagiário, Advogado-Preposto ou Parte-Advogado.
	 * O salvarCarga também faz essa persistência, porém junto com a carga (deslocamento, andamento e empréstimo).
	 * Esse método executa somente a persistência do vínculo sem efetivar a carga.
	 * @param evt
	 * @throws ServiceException
	 */
	public void salvarVinculo(ActionEvent evt) throws ServiceException {
		salvarVinculo();
	}
	
	private void salvarVinculo(){
		try {
			List<AssociacaoJurisdicionado> associacaoParaExclusao = getAssociacaoParaExclusao();
			List<AssociacaoJurisdicionado> associacaoParaInclusao = getAssociacaoParaInclusao();
			getEmprestimoAutosProcessoService().salvarVinculo(associacaoParaInclusao, associacaoParaExclusao);
			
			if (associacaoParaExclusao.size() > 0 && associacaoParaInclusao.size() == 0) {
				reportarInformacao("Vínculo entre autorizador e autorizado removido com sucesso!");
			} else {
				reportarInformacao("Vínculo entre autorizador e autorizado salvo com sucesso!");
			}
			// faz a atualização da árvore com os novos dados persistidos
			if (associacaoParaExclusao.size() > 0) {
			   atualizaArvore( associacaoParaExclusao.get(0) );
			} else if (associacaoParaInclusao.size() > 0) {
			   atualizaArvore( associacaoParaInclusao.get(0) );
			}
			listaSeqVinculosExcluidos.clear();
		} catch (Exception e) {
			reportarAviso("Erro ao salvar os dados da tela. " + e.getMessage());
		}
	}
	
	private void atualizaArvore(AssociacaoJurisdicionado associacao) throws ServiceException {
		listaResponsaveis.clear();
		codigoResponsavel = associacao.getGrupo().getJurisdicionado().getId();
		recuperarMembros();
		atualizaSessao();
	}
	
	
	/**
	 * 
	 * @author Ricardo Zonta Leão
	 * 
	 * Salvar consiste em:
	 * - vincular os autorizados com o autorizador, caso o vínculo não exista;
	 * - persistir um emprestimo e vincular a pessoa selecionada (autorizado ou autorizador);
	 * - criar um deslocamento para a pessoa selecionada com o tipo Advogado e com recebimento automático;
	 * - criar um lançamento (8209 - Autos emprestados...) com a observação = "<Nome do Advogado> - <Guia/Ano>")
	 * @param evt
	 * @throws ServiceException 
	 */
	public void salvarCarga(ActionEvent evt) {
		setSucesso(false);
		try {
			
			if (listaProcessos.size() == 0) {
				reportarAviso("Para realizar a carga deve-se adicionar o(s) processo(s)");
				return;
			}
			
			if (codigoResponsavelSelecionado == null) {
				reportarAviso("Para realizar a carga um responsável deverá ser selecionado.");
				return;
			}
			if (dataDevolucao == null) {
				reportarAviso("Para realizar a carga a data para devolução deverá ser informada.");
				return;
			}
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");

			if (sd.parse(sd.format(dataDevolucao)).before(sd.parse(sd.format(new Date())))) {
				reportarAviso("Data para devolução não pode ser menor que a data atual.");
				return;
			}
			
			// reunir informações do usuário
			Usuario usuario = new Usuario();
			usuario.setId(getUser().getUsername().toUpperCase());
			usuario.setSetor(getSetorUsuarioAutenticado());
			//
			// reunir informações do empréstimo
			JurisdicionadoResult responsavelSelecionado = recuperarResponsavelSelecionado(codigoResponsavelSelecionado);
			EmprestimoAutosProcesso emprestimo = new EmprestimoAutosProcesso();
			PapelJurisdicionado papelJurisdicionadoSelecionado = getPapelJurisdicionadoService().recuperarPorId( responsavelSelecionado.getIdPapel() );
			Jurisdicionado jurisdicionadoAutorizador = null;
			// se for autorizador - não tem associação
			if (responsavelSelecionado.getAutorizador()) {
				// atualiza o jurisdicionado no objeto papel
				jurisdicionadoAutorizador = getJurisdicionadoService().recuperarPorId(responsavelSelecionado.getId());
				papelJurisdicionadoSelecionado.setJurisdicionado(jurisdicionadoAutorizador);
				emprestimo.setPapelJurisdicionado(papelJurisdicionadoSelecionado);
			// se for autorizado
			} else {
				JurisdicionadoResult jurisdicionadoResultPai = recuperarPaiResponsavelSelecionado(codigoResponsavelSelecionado);
				PapelJurisdicionado papelAutorizador = getPapelJurisdicionadoService().recuperarPorId( jurisdicionadoResultPai.getIdPapel() );
				//
				AssociacaoJurisdicionado associacao = recuperarAssociacao(papelAutorizador, papelJurisdicionadoSelecionado);
				// se null então não recuperou a associação e deve ser persistida uma nova associação.
				if (associacao == null) { 
					associacao = new AssociacaoJurisdicionado();
					associacao.setGrupo(papelAutorizador);
					associacao.setMembro(papelJurisdicionadoSelecionado);
					associacao.setId(null);
					associacao.setAtivo(true);
				}
				emprestimo.setAssociacaoJurisdicionado(associacao);
			}
			
			emprestimo.setDataDevolucaoPrevista(dataDevolucao);
			emprestimo.setDataEmprestimo(new Date());
			//
			// reunir informações da guia de deslocamento
			// limpar da lista os apensos porque o método inserirDeslocamento já desloca os apensos se houver.
			List<CheckableDataTableRowWrapper> listaParaDeslocamento = new ArrayList<CheckableDataTableRowWrapper>(listaProcessos);
			listaParaDeslocamento.removeAll(listaDependencias);
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
			Guia guia = new Guia();
			GuiaId guiaId = new GuiaId();
			guiaId.setCodigoOrgaoOrigem(getSetorUsuarioAutenticado().getId());
			guia.setId(guiaId);
			guia.setTipoOrgaoOrigem(2);
			guia.setCodigoOrgaoDestino(responsavelSelecionado.getId());
			guia.setTipoOrgaoDestino(1); // advogado
			guia.setObservacao(obsDeslocamento);

			// efetuar a carga
			GuiaId idGuiaGerada = getEmprestimoAutosProcessoService().salvarCarga(emprestimo, guia, arrayObjIncidente, getAssociacaoParaInclusao(), getAssociacaoParaExclusao(), usuario);
			reportarInformacao("Empréstimo realizado com sucesso! Guia gerada: " + idGuiaGerada.getNumeroGuia().toString() + "/" + idGuiaGerada.getAnoGuia().toString() );
			setSucesso(true);
			
			// tratar os botões da situação 'pós-carga'
			comportamentoInput(false, "idProcesso");
			comportamentoInput(false, "itResponsavel");
			comportamentoInput(false, "itDataDevolucao");
			comportamentoInput(false, "btnIncluirProcesso");
			comportamentoBotao(false, "btnAddAutorizador", true);
			comportamentoBotao(false, "btnAddAutorizado", true);
			comportamentoBotao(false, "btnLimparGridResponsavel", true);
			if (idGuiaGerada != null) {
				numeroAnoGuia = idGuiaGerada.getNumeroGuia().toString() + "/" + idGuiaGerada.getAnoGuia().toString();
			}
			if (numeroAnoGuia != null) {
				comportamentoBotao(true, "btnImprimirGuia", true);
			} else {
				comportamentoBotao(false, "btnImprimirGuia", true);
			}
			comportamentoBotao(false, "btnSalvarCarga", true);
			comportamentoBotao(false, "btnSalvarVinculo", true);
			comportamentoBotao(true, "btnNovaCarga", false);
			atualizaSessao();
		} catch (ServiceException e) {
			reportarAviso("Erro ao salvar os dados da tela. " + e.getMessage());
			e.printStackTrace();
		} catch (ParseException e) {
			reportarAviso("Falha na conversão da data da carga!");
			e.printStackTrace();
		}
	}
	
	/**
	 * monta a lista de UFs
	 * @return
	 */
	private List<SelectItem> montarListaUf(){
		List<SelectItem> lista = new LinkedList<SelectItem>();
		lista.add(new SelectItem("",""));
		try {
			List<String> ufs = getEnderecoDestinatarioService().pesquisarUF();
			if( ufs != null && ufs.size() > 0 ){
				for(String uf : ufs){
					lista.add(new SelectItem(uf,uf));
				}
			}
			
		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar UF.");
		}
		return lista; 
	}

	
	/**
	 * cria um novo papel para o jurisdicionado
	 */
	private void inserePapelDoJurisdicionado() throws ServiceException{
		PapelJurisdicionado papel = new PapelJurisdicionado();
		papel.setJurisdicionado(novoAutorizado);
		papel.setPadrao(true);
		
		
		if (tipoPapelNovoAutorizado.equals("E")){ // estagiário
			papel.setTipoJurisdicionado(buscaPapelJurisdicionado("EST"));
		}else if (tipoPapelNovoAutorizado.equals("P")){ // preposto
			papel.setTipoJurisdicionado(buscaPapelJurisdicionado("PREPO"));
		}else{ // advogado
			papel.setTipoJurisdicionado(buscaPapelJurisdicionado("ADV"));
		}
		List<PapelJurisdicionado> papeis = new ArrayList<PapelJurisdicionado>();
		papeis.add(papel);
		novoAutorizado.setPapeisJurisdicionado(papeis);		
		novoAutorizado.getPapeisJurisdicionado().add(papel);
	}
	
	private TipoJurisdicionado buscaPapelJurisdicionado(String papel){
		try {
			return getTipoJurisdicionadoService().buscaTipoPelaSigla(papel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
	
	public void verificaSeExisteJurisdicionadoAction(ActionEvent evt){
		verificaSeExisteJurisdicionado();
	}
	
	public void prepararPopupAutorizado(ActionEvent evt){
		prepararPopupAutorizado();
	}
	
	private void prepararPopupAutorizado() {
		if (novoAutorizado == null) {
			novoAutorizado = new Jurisdicionado();
		}
		novoAutorizado.setId(null);
		novoAutorizado.setNome("");
		novoAutorizado.setEmail("");
		tipoPapelNovoAutorizado = null;
		cpfNovoAutorizado = null;
		obsNovoAutorizado = null;
		dataValidadeCadastro = null;
		ufOAB = null;
		oabNovoAutorizado = null;
		rgNovoAutorizado = null;
		atualizaSessao();
	}
	
	public void salvarAutorizado(ActionEvent evt) throws ServiceException{
		setNovoAutorizadoSalvo(false);
		if ( (tipoPapelNovoAutorizado == null) || ("AEP".indexOf(tipoPapelNovoAutorizado) == -1) ){
			reportarAviso("Um tipo de autorizado deve ser selecionado.");
			return;
		}
		if (novoAutorizado == null) {
			reportarAviso("Autorizado não encontrado");
			return;
		}
		if (novoAutorizado.getNome() == null) {
			reportarAviso("Favor informar o nome do autorizado");
			return;
		}
		if (novoAutorizado.getEmail() == null) {
			reportarAviso("Favor informar o e-mail do autorizado");
			return;
		}
		verificaSeExisteJurisdicionado();
		if (existeCadastro == null) {
			return;
		}
		if (existeCadastro) {
			reportarAviso("O CPF informado já está cadastrado.");
			return;
		}
		if ("AE".indexOf(tipoPapelNovoAutorizado) == -1) {
			if (obsDeslocamento == null) {
				reportarAviso("identificação da OAB é obrigatória para Advogados e Estagiários.");
				return;
			}
		}
		novoAutorizado.setAtivo(true);
		novoAutorizado.setObservacao(obsNovoAutorizado);
		// criar um id para o CPF
		TipoIdentificacao tiCPF = getTipoIdentificacaoService().pesquisaPelaSigla("CPF");
		IdentificacaoPessoa ipCPF = new IdentificacaoPessoa();
		ipCPF.setDescricaoIdentificacao(cpfNovoAutorizado);
		ipCPF.setJurisdicionado(novoAutorizado);
		ipCPF.setTipoIdentificacao(tiCPF);

		List<IdentificacaoPessoa> idsPessoa = new ArrayList<IdentificacaoPessoa>();
		idsPessoa.add(ipCPF);
		
		// criar um id para o OAB
		if (oabNovoAutorizado != null && !oabNovoAutorizado.trim().equals("")){
			TipoIdentificacao tiOAB = getTipoIdentificacaoService().pesquisaPelaSigla("OAB");
			IdentificacaoPessoa ipOAB = new IdentificacaoPessoa(); 
			ipOAB.setDescricaoIdentificacao(oabNovoAutorizado);
			ipOAB.setJurisdicionado(novoAutorizado);
			ipOAB.setTipoIdentificacao(tiOAB);
			ipOAB.setSiglaUfOrgaoExpedidor(ufOAB);
			idsPessoa.add(ipOAB);
		}
		// criar um id para o RG
		if (rgNovoAutorizado != null && !rgNovoAutorizado.trim().equals("")){
			TipoIdentificacao tiRG = getTipoIdentificacaoService().pesquisaPelaSigla("RG");
			IdentificacaoPessoa ipRG = new IdentificacaoPessoa(); 
			ipRG.setDescricaoIdentificacao(rgNovoAutorizado);
			ipRG.setJurisdicionado(novoAutorizado);
			ipRG.setTipoIdentificacao(tiRG);
			idsPessoa.add(ipRG);
		}
		novoAutorizado.setIdentificadoresJurisdicionado(idsPessoa);
		
		// recuperar informações do autorizador para complementar o cadastro
		// se a tela possui vários autorizadores então recuperar o selecionado
		if (codigoAutorizadorSelecionado != null) {
			codigoAutorizador = codigoAutorizadorSelecionado;
		}
		Jurisdicionado autorizador = getJurisdicionadoService().recuperarPorId(codigoAutorizador);
		if (autorizador == null) {
			reportarErro("Não foi possível recuperar informações do autorizador");
			return;
		}
		List<EnderecoJurisdicionado> enderecos = autorizador.getEnderecosJurisdicionado();
		List<TelefoneJurisdicionado> telefones = autorizador.getTelefonesJurisdicionado();

		List<EnderecoJurisdicionado> enderecosNovoAutorizado = new ArrayList<EnderecoJurisdicionado>();
		List<TelefoneJurisdicionado> telefonesNovoAutorizado = new ArrayList<TelefoneJurisdicionado>();
		for (EnderecoJurisdicionado endereco: enderecos){
			
			EnderecoJurisdicionado enderecoNovoAutorizado = new EnderecoJurisdicionado();
			enderecoNovoAutorizado.setBairro(endereco.getBairro());
			enderecoNovoAutorizado.setCep(endereco.getCep());
			enderecoNovoAutorizado.setComplemento(endereco.getComplemento());
			enderecoNovoAutorizado.setJurisdicionado(novoAutorizado);
			enderecoNovoAutorizado.setLogradouro(endereco.getLogradouro());
			enderecoNovoAutorizado.setMunicipio(endereco.getMunicipio());
			enderecoNovoAutorizado.setNumero(endereco.getNumero());
			enderecoNovoAutorizado.setPais(endereco.getPais());
			enderecoNovoAutorizado.setTipoMunicipio(endereco.getTipoMunicipio());
			enderecoNovoAutorizado.setUF(endereco.getUF());
			
			enderecosNovoAutorizado.add(enderecoNovoAutorizado);
		}
		for (TelefoneJurisdicionado telefone: telefones){
			
			TelefoneJurisdicionado telefoneNovoAutorizado = new TelefoneJurisdicionado();
			telefoneNovoAutorizado.setDDD(telefone.getDDD());
			telefoneNovoAutorizado.setDDI(telefone.getDDI());
			telefoneNovoAutorizado.setJurisdicionado(novoAutorizado);
			telefoneNovoAutorizado.setNumero(telefone.getNumero());
			telefoneNovoAutorizado.setObservacao(telefone.getObservacao());
			telefoneNovoAutorizado.setRamal(telefone.getRamal());
			telefoneNovoAutorizado.setTipoTelefone(telefone.getTipoTelefone());
			
			telefonesNovoAutorizado.add(telefoneNovoAutorizado);
			
		}
		novoAutorizado.setEnderecosJurisdicionado(enderecosNovoAutorizado);
		novoAutorizado.setTelefonesJurisdicionado(telefonesNovoAutorizado);
		novoAutorizado.setEntidadeGovernamental(false);
		inserePapelDoJurisdicionado();
		Jurisdicionado autorizadoSalvo = getJurisdicionadoService().salvar(novoAutorizado);
		codigoResponsavel = autorizadoSalvo.getId();
		if (listaAutorizadores.size() == 0) {
			adicionaJurisdicionadoNaTabelaDeAutorizacao(false);
		} else {
			confirmarAutorizador();
		}
		setNovoAutorizadoSalvo(true);
		salvarVinculo();
	}
	
	// verificar se existe exclusões de vinculo a serem persistidas e retorna a lista
	public List<AssociacaoJurisdicionado> getAssociacaoParaExclusao() throws ServiceException {
		try {
			List<AssociacaoJurisdicionado> listaParaExclusao = new ArrayList<AssociacaoJurisdicionado>();
			for (Long itemSeqVinculo: listaSeqVinculosExcluidos) {
				AssociacaoJurisdicionado associacaoParaExcluir = getAssociacaoJurisdicionadoService().recuperarPorId(itemSeqVinculo);
					listaParaExclusao.add(associacaoParaExcluir);
			}
			return listaParaExclusao;
		} catch (Exception e) {
			reportarErro("Não foi possível recuperar as associações para exclusão.");
			throw new ServiceException("Erro ao excluir o autorizado: " + e.getMessage());
		}
		
	}
	
	public Long getIdAssociacao(PapelJurisdicionado grupo, PapelJurisdicionado membro, EnumTipoAssociacao tipoAssociacao) throws ServiceException {
		AssociacaoJurisdicionado associacao = getAssociacaoJurisdicionadoService().recuperarPorGrupoMembro(grupo, membro, tipoAssociacao);
		if (associacao == null) {
			return null;
		} else {
			return associacao.getId();
		}
	}
	
	// retorna a lista de associações a serem incluidas
	public List<AssociacaoJurisdicionado> getAssociacaoParaInclusao() throws ServiceException {
		try {
			if (getListaResponsaveis().size() == 1) { // se for um unico responsável então não existe associação
				return new ArrayList<AssociacaoJurisdicionado>();
			}

			List<AssociacaoJurisdicionado> listaParaInclusao = new ArrayList<AssociacaoJurisdicionado>();

			if (getListaResponsaveis().size()>1) {
				AssociacaoJurisdicionado associacao = new AssociacaoJurisdicionado();
				associacao.setAtivo(true);
				PapelJurisdicionado papelPai = null;
				PapelJurisdicionado papelFilho = null;
				for (CheckableDataTableRowWrapper itemLista: listaResponsaveis) {
					JurisdicionadoResult itemJurisdicionadoResult = (JurisdicionadoResult) itemLista.getWrappedObject();

					// se for o pai então recuperar o papel
					if (itemJurisdicionadoResult.getAutorizador()) { 
						papelPai = getPapelJurisdicionadoService().recuperarPorId(itemJurisdicionadoResult.getIdPapel());
						associacao.setGrupo(papelPai);
					} else { // ser um autorizado e...
						// se necessita persistir (não possui vinculo ainda ou o vínculo já existe, porém está inativado)
						if (itemJurisdicionadoResult.getIdAssociacao() == null) {
							// setar o papel na associação para persistência do vinculo
							papelFilho = getPapelJurisdicionadoService().recuperarPorId(itemJurisdicionadoResult.getIdPapel());
							if ((papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV")) &&
							    (papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV"))){
							
								associacao.setTipoAssociacao(EnumTipoAssociacao.ADVOGADO_ADVOGADO);
								
							} else if ((papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV")) &&
									   (papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("EST"))){
	
								associacao.setTipoAssociacao(EnumTipoAssociacao.ADVOGADO_ESTAGIARIO);
									
							} else if ((papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV")) &&
									   (papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PREPO"))){
	
								associacao.setTipoAssociacao(EnumTipoAssociacao.ADVOGADO_PREPOSTO);
									
							} else if ((papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PART")) &&
									   (papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("ADV"))){
	
								associacao.setTipoAssociacao(EnumTipoAssociacao.PARTE_ADVOGADO);
								
							} else if ((papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PART")) &&
									   (papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("EST"))){
	
								associacao.setTipoAssociacao(EnumTipoAssociacao.PARTE_ESTAGIARIO);
							} else if ((papelPai.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PART")) &&
									   (papelFilho.getTipoJurisdicionado().getSiglaTipoJurisdicionado().equals("PREPO"))){
	
								associacao.setTipoAssociacao(EnumTipoAssociacao.PARTE_PREPOSTO);
							}
							associacao.setMembro(papelFilho);
							// recupera o id da associação porque pode ser de uma associação que foi inativada: flag ativo = false
							// se realmente for uma nova associação o id ficará nulo como já estava antes.
							associacao.setId(getIdAssociacao(associacao.getGrupo(), 
									                         associacao.getMembro(), 
									                         associacao.getTipoAssociacao()));
						}
					}
					if (papelFilho != null) {
						listaParaInclusao.add(associacao);
						papelFilho = null;
						associacao = new AssociacaoJurisdicionado();
						associacao.setAtivo(true);
						associacao.setGrupo(papelPai);
					}
				}
			}
			return listaParaInclusao;
		} catch (Exception e) {
			reportarErro("Não foi possível recuperar as associações para exclusão.");
			throw new ServiceException("Erro ao excluir o autorizado: " + e.getMessage());
		}
		
	}
	
	// novo autorizado
	public void atualizaLabelOAB(ActionEvent evt) throws Exception {
		UIComponent form = FacesContext.getCurrentInstance().getViewRoot().findComponent("formNovoAutorizado");
		int i = 0;
		while (form.findComponent("obrigatorioOAB") == null){
			form = form.getParent();
			i++;
			if (i > 20) {
				throw new Exception("Componente de interface não localizado");
			}
		}
		if (tipoPapelNovoAutorizado == null || !tipoPapelNovoAutorizado.equals("P")) {
			form.findComponent("obrigatorioOAB").getAttributes().put("style", "visibility:visible");
		} else {
			form.findComponent("obrigatorioOAB").getAttributes().put("style", "visibility:hidden");
		}
		
	}
	private void verificaSeExisteJurisdicionado(){
		IdentificacaoPessoa ip = null;
		cpfNovoAutorizado = retirarCaracteresNaoNumeros(cpfNovoAutorizado);
		if ((cpfNovoAutorizado.trim().length() != 11) || (!NumberUtils.soNumeros(cpfNovoAutorizado)) ) {
			setIsCPFErrado(true);
			reportarAviso("CPF incorreto.");
			return;
		}
		if (cpfNovoAutorizado.trim().length() > 0){
			if (!NumberUtils.validacaoCPF(cpfNovoAutorizado)){
				setIsCPFErrado(true);
				reportarAviso("CPF incorreto.");
				return;
			}else{
				setIsCPFErrado(false);
			}
			
			try {
				ip = getIdentificacaoPessoaService().recuperarPrimeiroCadastro(cpfNovoAutorizado, "CPF");
			} catch (Exception e) {
				reportarErro(e.getMessage());
			}
			
			if (ip != null){
				existeCadastro = true;
				setJurisdicionadoJaCadastrado(ip.getJurisdicionado());
			} else {
				existeCadastro = false;
			}
			
//			inputVerificaCadastroPessoa.setValue(existeCadastro);
		}else{
			reportarAviso("O CPF deve ser preenchido.");
			return;
		}
	}

	

	
	// getters/setters
	public void setClasseNumeroProcesso(String classeNumeroProcesso) {
		this.classeNumeroProcesso = classeNumeroProcesso;
	}

	public String getClasseNumeroProcesso() {
		return classeNumeroProcesso;
	}

	public void setListaProcessos(List<CheckableDataTableRowWrapper> listaProcessos) {
		this.listaProcessos = listaProcessos;
	}

	public List<CheckableDataTableRowWrapper> getListaProcessos() {
		return listaProcessos;
	}

	public void setListaResponsaveis(List<CheckableDataTableRowWrapper> listaResponsaveis) {
		this.listaResponsaveis = listaResponsaveis;
	}

	public List<CheckableDataTableRowWrapper> getListaResponsaveis() {
		return listaResponsaveis;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setCodigoResponsavel(Long codigoResponsavel) {
		this.codigoResponsavel = codigoResponsavel;
	}

	public Long getCodigoResponsavel() {
		return codigoResponsavel;
	}

	public String getCpfResponsavel() {
		return cpfResponsavel;
	}

	public void setCpfResponsavel(String cpfResponsavel) {
		this.cpfResponsavel = cpfResponsavel;
	}

	public String getPapelResponsavel() {
		return papelResponsavel;
	}

	public void setPapelResponsavel(String papelResponsavel) {
		this.papelResponsavel = papelResponsavel;
	}
	
	public void setTabelaProcessos(org.richfaces.component.html.HtmlDataTable tabelaProcessos) {
		this.tabelaProcessos = tabelaProcessos;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaProcessos() {
		return tabelaProcessos;
	}

	public void setTabelaResponsaveis(org.richfaces.component.html.HtmlDataTable tabelaResponsaveis) {
		this.tabelaResponsaveis = tabelaResponsaveis;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaResponsaveis() {
		return tabelaResponsaveis;
	}

	public void setCodigoResponsavelSelecionado(Long codigoResponsavelSelecionado) {
		this.codigoResponsavelSelecionado = codigoResponsavelSelecionado;
	}

	public Long getCodigoResponsavelSelecionado() {
		return codigoResponsavelSelecionado;
	}
	
	public void setCodigoAutorizador(Long codigoAutorizador) {
		this.codigoAutorizador = codigoAutorizador;
	}

	public Long getCodigoAutorizador() {
		return codigoAutorizador;
	}

	public void setAviso(String aviso) {
		this.aviso = aviso;
	}

	public String getAviso() {
		return aviso;
	}

	public void setListaDependencias(List<CheckableDataTableRowWrapper> listaDependencias) {
		this.listaDependencias = listaDependencias;
	}

	public List<CheckableDataTableRowWrapper> getListaDependencias() {
		return listaDependencias;
	}

	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public Date getDataDevolucao() {
		return dataDevolucao;
	}

	public void setNumeroAnoGuia(String numeroAnoGuia) {
		this.numeroAnoGuia = numeroAnoGuia;
	}

	public String getNumeroAnoGuia() {
		return numeroAnoGuia;
	}

	public void setExisteVinculo(String existeVinculo) {
		this.existeVinculo = existeVinculo;
	}

	public String getExisteVinculo() {
		return existeVinculo;
	}

	public void setListaAutorizadores(List<SelectItem> listaAutorizadores) {
		this.listaAutorizadores = listaAutorizadores;
	}

	public List<SelectItem> getListaAutorizadores() {
		return listaAutorizadores;
	}

	public void setTemMaisAutorizadores(String temMaisAutorizadores) {
		this.temMaisAutorizadores = temMaisAutorizadores;
	}

	public String getTemMaisAutorizadores() {
		return temMaisAutorizadores;
	}

	public void setCodigoAutorizadorSelecionado(
			Long codigoAutorizadorSelecionado) {
		this.codigoAutorizadorSelecionado = codigoAutorizadorSelecionado;
	}

	public Long getCodigoAutorizadorSelecionado() {
		return codigoAutorizadorSelecionado;
	}

	public void setHintExclusao(String hintExclusao) {
		this.hintExclusao = hintExclusao;
	}

	public String getHintExclusao() {
		return hintExclusao;
	}

	public void setIconeExclusao(String iconeExclusao) {
		this.iconeExclusao = iconeExclusao;
	}

	public String getIconeExclusao() {
		return iconeExclusao;
	}

	public void setListaSeqVinculosExcluidos(List<Long> listaSeqVinculosExcluidos) {
		this.listaSeqVinculosExcluidos = listaSeqVinculosExcluidos;
	}

	public List<Long> getListaSeqVinculosExcluidos() {
		return listaSeqVinculosExcluidos;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}

	public Boolean getSucesso() {
		return sucesso;
	}


	public void setTabelaProcessosInteresse(org.richfaces.component.html.HtmlDataTable tabelaProcessosInteresse) {
		this.tabelaProcessosInteresse = tabelaProcessosInteresse;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaProcessosInteresse() {
		return tabelaProcessosInteresse;
	}

	public void setAdvogado(Jurisdicionado advogado) {
		this.advogado = advogado;
	}

	public Jurisdicionado getAdvogado() {
		return advogado;
	}

	public void setMensagemPopup(String mensagemPopup) {
		this.mensagemPopup = mensagemPopup;
		setAtributo(MENSAGEM_POPUP, mensagemPopup);

	}

	public String getMensagemPopup() {
		return mensagemPopup;
	}

	public String getObsDeslocamento() {
		return obsDeslocamento;
	}

	public void setObsDeslocamento(String obsDeslocamento) {
		this.obsDeslocamento = obsDeslocamento;
	}

	public Boolean getChkEntidadeGovernamental() {
		return chkEntidadeGovernamental;
	}

	public void setChkEntidadeGovernamental(Boolean chkEntidadeGovernamental) {
		this.chkEntidadeGovernamental = chkEntidadeGovernamental;
	}

	public List<Long> getListaSeqObjetosIncidentes() {
		return listaSeqObjetosIncidentes;
	}

	public void setListaSeqObjetosIncidentes(List<Long> listaSeqObjetosIncidentes) {
		this.listaSeqObjetosIncidentes = listaSeqObjetosIncidentes;
	}

	public String getTipoPapelNovoAutorizado(){
		return this.tipoPapelNovoAutorizado;
	}
	
	public void setTipoPapelNovoAutorizado(String tipoPapelNovoAutorizado){
		this.tipoPapelNovoAutorizado = tipoPapelNovoAutorizado;
	}
	
	public Jurisdicionado getNovoAutorizado() {
		return novoAutorizado;
	}

	public void setNovoAutorizado(Jurisdicionado novoAutorizado) {
		this.novoAutorizado = novoAutorizado;
	}

	public String getCpfNovoAutorizado() {
		return cpfNovoAutorizado;
	}

	public void setCpfNovoAutorizado(String cpfNovoAutorizado) {
		this.cpfNovoAutorizado = cpfNovoAutorizado;
	}

	public Boolean getExisteCadastro() {
		return existeCadastro;
	}

	public void setExisteCadastro(Boolean existeCadastro) {
		this.existeCadastro = existeCadastro;
	}

	public Boolean getIsCPFErrado() {
		return isCPFErrado;
	}

	public void setIsCPFErrado(Boolean isCPFErrado) {
		this.isCPFErrado = isCPFErrado;
	}

//	public HtmlInputHidden getInputVerificaCadastroPessoa() {
//		return inputVerificaCadastroPessoa;
//	}

//	public void setInputVerificaCadastroPessoa(
//			HtmlInputHidden inputVerificaCadastroPessoa) {
//		this.inputVerificaCadastroPessoa = inputVerificaCadastroPessoa;
//	}

	public Jurisdicionado getJurisdicionadoJaCadastrado() {
		return jurisdicionadoJaCadastrado;
	}

	public void setJurisdicionadoJaCadastrado(Jurisdicionado jurisdicionadoJaCadastrado) {
		this.jurisdicionadoJaCadastrado = jurisdicionadoJaCadastrado;
	}
	public String getObsNovoAutorizado() {
		return obsNovoAutorizado;
	}

	public void setObsNovoAutorizado(String obsNovoAutorizado) {
		this.obsNovoAutorizado = obsNovoAutorizado;
	}

	public Date getDataValidadeCadastro() {
		return dataValidadeCadastro;
	}

	public void setDataValidadeCadastro(Date dataValidadeCadastro) {
		this.dataValidadeCadastro = dataValidadeCadastro;
	}
	public List<SelectItem> getItensUfOAB() {
		return itensUfOAB;
	}

	public void setItensUfOAB(List<SelectItem> itensUfOAB) {
		this.itensUfOAB = itensUfOAB;
	}

	public String getUfOAB() {
		return ufOAB;
	}

	public void setUfOAB(String ufOAB) {
		this.ufOAB = ufOAB;
	}

	public String getOabNovoAutorizado() {
		return oabNovoAutorizado;
	}

	public void setOabNovoAutorizado(String oabNovoAutorizado) {
		this.oabNovoAutorizado = oabNovoAutorizado;
	}

	public String getRgNovoAutorizado() {
		return rgNovoAutorizado;
	}

	public void setRgNovoAutorizado(String rgNovoAutorizado) {
		this.rgNovoAutorizado = rgNovoAutorizado;
	}

	public boolean getNovoAutorizadoSalvo() {
		return novoAutorizadoSalvo;
	}

	public void setNovoAutorizadoSalvo(boolean novoAutorizadoSalvo) {
		this.novoAutorizadoSalvo = novoAutorizadoSalvo;
	}

}
