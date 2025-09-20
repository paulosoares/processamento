package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.deslocamento;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.ObjectNotFoundException;

import br.gov.stf.estf.assinatura.deslocamento.origemdestino.ResultSuggestionOrigemDestino;
import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.CheckableDataTableRowWrapperDeslocaProcesso;
import br.gov.stf.estf.assinatura.visao.util.PeticaoParser;
import br.gov.stf.estf.assinatura.visao.util.PropertyComparator;
import br.gov.stf.estf.assinatura.visao.util.TipoOrdenacao;
import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.localizacao.Advogado;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.SituacaoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanReceberExterno extends AssinadorBaseBean {

	public static final String KEY_LISTA_DOCUMENTOS = BeanReceberExterno.class.getCanonicalName() + ".listaDocumentos";
	public static final String KEY_LISTA_GUIA = BeanReceberExterno.class.getCanonicalName() + ".listaGuia";
	public static final Object NUM_GUIA = new Object();
	public static final Object CHK_TIPO_PESQUISA = new Object();
	public static final Object CODIGO_SETOR_PROCESSO = new Object();
	public static final Object PROCESSO_INSERT = new Object();
	public static final Object SUCESSO = new Object();
	public static final Object EXIBE_POPUP_GUIA = new Object();
	public static final Object GUIAS_PARA_IMPRESSAO = new Object();

	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(BeanReceberExterno.class);
	private static final Object ANO_GUIA = new Object();
	private static final Object CHK_TIPO_ORIGEM = new Object();
	private static final Object DESCRICAO_ORIGEM_RECEBIMENTO = new Object();
	private static final Object CODIGO_ORIGEM = new Object();
	private static final Object DESCRICAO_ORIGEM = new Object();
	private static final Object CODIGO_LOTACAO = new Object();
	private static List<String> classes;

	//@KeepStateInHttpSession
	private String numGuia;
	private String anoGuia;
	private String chkTipoOrigem;
	private String chkTipoPesquisa;
	private String descricaoOrigemRecebimento;
	private String descricaoOrigem;
	private String identificacaoProcesso;
	private String siglaProcesso;
	private Long numeroProcesso;
	private Long codigoOrigemPesquisa;
	private Long codigoOrigem;
	private Processo processo;
	private Long objetoIncidente;
	private boolean adicionaProcesso = false;
	private boolean pesquisaProcesso = false;
	private boolean enableModalPanel = false;
	private boolean enablePanelRecebimentoProcesso = false;
	private boolean pesquisaGuiaProcesso = false;
	private boolean pesquisaGuiaPeticao = false;
	private String sucesso = "N";
	private List<Guia> guiasParaImpressao;
	
	private List<CheckableDataTableRowWrapperDeslocaProcesso> listaDocumentos;
	private org.richfaces.component.html.HtmlDataTable tabelaDocumentos;
//	@KeepStateInHttpSession
	private Long codigoDestino;
	private List<CheckableDataTableRowWrapper> listaGuia;
	private org.richfaces.component.html.HtmlDataTable tabelaGuias;
	private Long codigoLotacao;
	private Long codigoSetorProcesso;
	private String exibePopupGuia;

	@SuppressWarnings("unchecked")
	private void restaurarSessao() throws ServiceException {
//		restoreStateOfHttpSession();

		Calendar cal = Calendar.getInstance();
		int ano_atual = cal.get(Calendar.YEAR);
		Integer anoCorrente = ano_atual;

		if (getAtributo(KEY_LISTA_DOCUMENTOS) == null) {
			setAtributo(KEY_LISTA_DOCUMENTOS, new ArrayList<CheckableDataTableRowWrapper>());
		} else {
			setListaDocumentos((List<CheckableDataTableRowWrapperDeslocaProcesso>) getAtributo(KEY_LISTA_DOCUMENTOS));
		}
		if (getAtributo(KEY_LISTA_GUIA) == null) {
			setAtributo(KEY_LISTA_GUIA, new ArrayList<CheckableDataTableRowWrapper>());
		} else {
			setListaGuia((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_GUIA));
		}
		if (getAtributo(ANO_GUIA) == null) {
			setAnoGuia(anoCorrente.toString());
		} else {
			setAnoGuia((getAtributo(ANO_GUIA).toString()));
		}
		setNumGuia((String) getAtributo(NUM_GUIA));

		if (getAtributo(CHK_TIPO_ORIGEM) == null) {
			setAtributo(CHK_TIPO_ORIGEM, "SET");
		}
		if (getAtributo(CODIGO_LOTACAO) == null) {
			setCodigoLotacao(getSetorUsuarioAutenticado().getId());
		} else {
			setCodigoLotacao((Long) getAtributo(CODIGO_LOTACAO));
		}
		setChkTipoOrigem((String) getAtributo(CHK_TIPO_ORIGEM));

		if (getAtributo(CHK_TIPO_ORIGEM).equals("SET")) {
			setDescricaoOrigemRecebimento("Setor do STF");
		} else if (getAtributo(CHK_TIPO_ORIGEM).equals("ADV")) {
			setDescricaoOrigemRecebimento("Destino da Carga de Autos");
		} else if (getAtributo(CHK_TIPO_ORIGEM).equals("ORG")) {
			setDescricaoOrigemRecebimento("Órgão Externo");
		}
		if (getAtributo(CODIGO_ORIGEM) != null) {
			setCodigoOrigem((Long) getAtributo(CODIGO_ORIGEM));
		}
		if (getAtributo(CHK_TIPO_PESQUISA) == null) {
			setAtributo(CHK_TIPO_PESQUISA, "PRO");
		}
		setChkTipoPesquisa((String) getAtributo(CHK_TIPO_PESQUISA));
		if (getAtributo(CODIGO_SETOR_PROCESSO) != null) {
			setCodigoSetorProcesso((Long) getAtributo(CODIGO_SETOR_PROCESSO));
		}
		if (getAtributo(PROCESSO_INSERT) != null) {
			setProcesso((Processo) getAtributo(PROCESSO_INSERT));
		}
		if (getAtributo(DESCRICAO_ORIGEM) != null) {
			setDescricaoOrigem((String) getAtributo(DESCRICAO_ORIGEM));
		}
		setSucesso((String) getAtributo(SUCESSO));
		if (getAtributo(EXIBE_POPUP_GUIA) == null) {
			setAtributo(EXIBE_POPUP_GUIA, "N");
		}
		setExibePopupGuia((String) getAtributo(EXIBE_POPUP_GUIA));

		if (getAtributo(GUIAS_PARA_IMPRESSAO) == null) {
			setAtributo(GUIAS_PARA_IMPRESSAO, new ArrayList<Guia>());
		}
		setGuiasParaImpressao((List<Guia>) getAtributo(GUIAS_PARA_IMPRESSAO));
	}

	public BeanReceberExterno() throws ServiceException {
		restaurarSessao();
		getEnablePesquisa();
	}

	private void atualizaSessao() {
//		keepStateInHttpSession();
		setAtributo(SUCESSO, sucesso);
		setAtributo(NUM_GUIA, numGuia);
		setAtributo(ANO_GUIA, anoGuia);
		setAtributo(CHK_TIPO_ORIGEM, chkTipoOrigem);
		setAtributo(CHK_TIPO_PESQUISA, chkTipoPesquisa);
		setAtributo(CODIGO_ORIGEM, codigoOrigem);
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		setAtributo(KEY_LISTA_GUIA, listaGuia);
		setAtributo(CODIGO_LOTACAO, codigoLotacao);
		setAtributo(CODIGO_SETOR_PROCESSO, codigoSetorProcesso);
		if(processo != null){
			setAtributo(PROCESSO_INSERT, processo);
		}

		if (chkTipoOrigem.equals("SET")) {
			setDescricaoOrigemRecebimento("Setor do STF");
		} else if (chkTipoOrigem.equals("ADV")) {
			setDescricaoOrigemRecebimento("Destino da Carga de Autos");
		} else if (chkTipoOrigem.equals("ORG")) {
			setDescricaoOrigemRecebimento("Órgão Externo");
		}
		setAtributo(DESCRICAO_ORIGEM_RECEBIMENTO, descricaoOrigemRecebimento);

		if (descricaoOrigem != null && !descricaoOrigem.isEmpty()) {
			setAtributo(DESCRICAO_ORIGEM, descricaoOrigem);
		}
		setAtributo(EXIBE_POPUP_GUIA, exibePopupGuia);
		if (guiasParaImpressao != null) {
			setAtributo(GUIAS_PARA_IMPRESSAO, guiasParaImpressao);
		}
	}
	
	// obtem o tamanho da tabela 
	public int getTotalLinha() {
		if (listaDocumentos != null) {
			return listaDocumentos.size();
		} else if (listaGuia != null) {
			return listaGuia.size();
		} else {
			return 1;
		}
	}

	public boolean getIsSetorStf() {
		if (chkTipoOrigem.equals("SET")) {
			return true;
		}
		return false;
	}

	public boolean getIsOutrosSetores() {
		if (chkTipoOrigem.equals("ADV") || chkTipoOrigem.equals("ORG")) {
			return true;
		}
		return false;
	}

	public String getTitleBotaoPesquisar() {
		if ((chkTipoOrigem.equals("ADV") || chkTipoOrigem.equals("ORG")) && descricaoOrigem != null) {
			return "Listar Processos";
		}
		return "Listar Guia";
	}

	public boolean getIsDisablePesquisar() {
		if (chkTipoOrigem.equals("SET") && descricaoOrigem != null && numGuia != null && anoGuia != null) {
			return true;
		} else if ((chkTipoOrigem.equals("ADV") || chkTipoOrigem.equals("ADV")) && descricaoOrigem != null) {
			return true;
		}
		return false;
	}
	
	
	public void insereProcessoParaRecebimentoAction(ActionEvent evt){
		insereProcessoParaRecebimento();
	}
	
	public String getHintDestino() {
		if (getAtributo(CHK_TIPO_ORIGEM).equals("SET")) {
			return "Setor do STF";
		} else if (getAtributo(CHK_TIPO_ORIGEM).equals("ADV")) {
			return "Destino da Carga de Autos: pode ser um advogado, seu representante autorizado, entidade governamental ou preposto.";
		} else if (getAtributo(CHK_TIPO_ORIGEM).equals("ORG")) {
			return "Órgão Externo";
		}
		return "";
	}

	public void pesquisarProcessos() {
		pesquisaProcesso = true;
		Long codigoSetor = codigoOrigem;
		List<DeslocaProcesso> auxiliar = new ArrayList<DeslocaProcesso>();
		try {
			List<DeslocaProcesso> deslocaProcessos = getDeslocaProcessoService().recuperarProcessosPeloSetor(codigoSetor);
			for (DeslocaProcesso deslocaProcesso : deslocaProcessos) {

				try {
					/*a verificação deslocaProcesso.getId().getProcesso().getTipoMeioProcesso() está sendo realizada pelo fato de gerar um erro caso 
					não seja verificado, em que este se refere a objectNotFoundException, ou seja, o seqObjetoIncidente existe apenas na tabela
					deslocaProcesso, de modo que quando o hibernate realiza a busca, não encontra o seqObjetoIncidente na tabela processo e gera uma exceção.*/
					if (deslocaProcesso.getId().getProcesso() != null && deslocaProcesso.getId().getProcesso().getTipoMeioProcesso() != null) {
						auxiliar.add(deslocaProcesso);
					}
				} catch (ObjectNotFoundException e) {
					reportarErro(e.getMessage());
				}
			}

			listaDocumentos = getCheckableDataTableRowWrapperDeslocaProcesso(auxiliar);
			if (listaDocumentos.isEmpty()) {
				reportarAviso("Nenhum registro encontrado");
			} else {
				reportarInformacao("Foram encontrados " + listaDocumentos.size() + " registros");
			}
		} catch (Exception e) {
			reportarErro("Ocorreu um erro ao pesquisar processo encontrado." + e.getMessage());
		}
		codigoSetorProcesso = codigoSetor;
		setAtributo(CODIGO_SETOR_PROCESSO, codigoSetorProcesso);
		atualizaSessao();
	}
	
	// insere petições SOMENTE para recebimento externo
	public void inserePeticaoParaRecebimento(String idPeticao){
		// atualizar a listaGuia
		try {
			//pesquisarGuia();
			Short ano = PeticaoParser.getAnoPeticao(idPeticao);
			Long numero = PeticaoParser.getNumeroPeticao(idPeticao);
			Peticao peticao = getPeticaoService().recuperarPeticao(numero, ano);
			List<DeslocaPeticao> deslocaPeticoes = getDeslocamentoPeticaoService().recuperarDeslocamentoPeticaoRecebimentoExterno(peticao);
			if (deslocaPeticoes != null){
				pesquisaGuiaPeticao = true;
				pesquisaGuiaProcesso = false;
				chkTipoPesquisa = "PET";
			} else {
				reportarErro("Guia não localizada!");
				return;
			}
			
			DeslocaPeticao ultimoDeslocamentoPeticao = getDeslocamentoPeticaoService().recuperarUltimoDeslocamentoPeticao(peticao);
			if (ultimoDeslocamentoPeticao.getGuia().getTipoOrgaoDestino().equals(2) && !chkTipoOrigem.equals("SET")) {
				reportarAviso("Para recebimento interno (quando o destino é o setor do STF) clique no tipo de recebimento 'Setor do STF' e informe a guia.");
				return;
			}
			
//			if (!ultimoDeslocamentoPeticao.getCodigoOrgaoDestino().equals(codigoOrigem)) {
//				reportarAviso("Petição não localizada na origem informada ou petição já recebida!");
//				return;
//			}
			
			pesquisaGuiaPeticao = true;
			if (listaGuia == null) {
				listaGuia = new ArrayList<CheckableDataTableRowWrapper>();
			}
			
			for (CheckableDataTableRowWrapper check : listaGuia) {
				if ( ((DeslocaPeticao)check.getWrappedObject()).getId().equals(deslocaPeticoes.get(0).getId()) ) {
					reportarAviso("Petição já adicionada à lista.");
					return;
				}
			}
			listaGuia.addAll(getCheckableDataTableRowWrapperList(deslocaPeticoes));
		
			for (CheckableDataTableRowWrapper check : listaGuia) {
				DeslocaPeticao deslocaPeticao = (DeslocaPeticao) check.getWrappedObject();
				deslocaPeticao.setAnoPeticao(deslocaPeticao.getId().getPeticao().getAnoPeticao().longValue());
				deslocaPeticao.setNumeroPeticao(deslocaPeticao.getId().getPeticao().getNumeroPeticao().longValue());
				check.setWrappedObject(deslocaPeticao);
				check.setChecked(true);
			}
			atualizaSessao();
		} catch (Exception e) {
			reportarErro("Guia não localizada!");
		}
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Insere o processo para recebimento. Neste caso o recebimento é de órgãos externos ou carga de autos.
	 */
	public void insereProcessoParaRecebimento() {
		String descricao = null;
		adicionaProcesso = true;
		
		if (identificacaoProcesso == null || identificacaoProcesso.trim().length() == 0){
			reportarAviso("A sigla e número do processo deverão ser preenchidos.");
			return;
		}
		
		try {
			identificacaoProcesso = identificacaoProcesso.toString();
			identificacaoProcesso = identificacaoProcesso.toString().replaceAll("\t", "");  
			identificacaoProcesso = identificacaoProcesso.toString().replaceAll("\n", "");  
			identificacaoProcesso = identificacaoProcesso.toString().replaceAll(" ", "");
			identificacaoProcesso = identificacaoProcesso.toString().replaceAll("-", "");
			
			if (identificacaoProcesso.length() == 14){
				String siglaNumeroTrezeCarac = identificacaoProcesso.substring(0, identificacaoProcesso.length()-1); 
				identificacaoProcesso = siglaNumeroTrezeCarac;
			}
			
			String tipo = descobrirTipoGuia(identificacaoProcesso);
			if (tipo == null) {
				reportarAviso("O formato válido para processo é Sigla e Número. Ex.: RE 1234. Para petições é Número e Ano. Ex.: 1234 2013");
				return;
			}
			if (tipo.equals("PET") || tipo.equals("PEE")) {
				inserePeticaoParaRecebimento(identificacaoProcesso);
				return;
			}
			
			ObjetoIncidente obIncidente = pesquisarIncidentesPrincipal(identificacaoProcesso, tipo);
			
			if (obIncidente == null) {
				reportarAviso("Processo ou petição não existe.");
				return;
			}
			objetoIncidente = obIncidente.getId();
	
			ObjetoIncidente<?> objetoInc = getObjetoIncidenteService().recuperarPorId(objetoIncidente);
			
			Processo processo = (Processo) objetoInc.getPrincipal();
			if (processo == null) {
				reportarAviso("Processo inexistente.");
				return;
			}
			
			DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
			if (ultimoDeslocamento == null) {
				reportarAviso("O processo " + processo.getSiglaClasseProcessual() + "-" + processo.getNumeroProcessual() + " não possui deslocamento inicial");
				return;
			}
			// no recebimento de entidades externas a origem deve ser setor do STF e o destino NÃO.
			Setor setorOrigem = getSetorService().recuperarPorId(ultimoDeslocamento.getId().getCodigoOrgaoOrigem());
			Setor setorDestino = getSetorService().recuperarPorId(ultimoDeslocamento.getCodigoOrgaoDestino());
			if (setorOrigem == null || setorDestino != null || ultimoDeslocamento.getDataRecebimento() == null) {
				reportarAviso("O processo " + processo.getSiglaClasseProcessual() + "-" + processo.getNumeroProcessual() + " não é de origem externa ou já foi recebido");
				return;
			}
			
			Processo processoSelecionado = processo;
			setAtributo(PROCESSO_INSERT, processo);

			if (listaDocumentos == null) {
				listaDocumentos = Collections.emptyList();
			}

			Iterator itensLista = listaDocumentos.iterator();
			while (itensLista.hasNext()) {
				Object element = itensLista.next();
				Processo proc = (Processo) ((DeslocaProcesso) ((CheckableDataTableRowWrapper) element).getWrappedObject()).getId().getProcesso();
				if (proc.getIdentificacao().equals(processo.getIdentificacao())) {
					reportarAviso("Processo já adicionado.");
					return;
				}
				DeslocaProcesso itemDesloca = (DeslocaProcesso) ((CheckableDataTableRowWrapper) element).getWrappedObject();
				Integer tipoDestinoItem = itemDesloca.getGuia().getTipoOrgaoDestino();
				Integer tipoDestinoUltimo = ultimoDeslocamento.getGuia().getTipoOrgaoDestino();
				if (!tipoDestinoItem.equals(tipoDestinoUltimo)) {
					if (tipoDestinoUltimo.equals(1)) {
						reportarAviso("O processo está em grupo de recebimento diferente: Carga de autos. Favor receber um grupo por vez.");
 					} else if (tipoDestinoUltimo.equals(3)) {
						reportarAviso("O processo está em grupo de recebimento diferente: Órgãos externos. Favor receber um grupo por vez.");
 					} else {
						reportarAviso("O processo está em grupo de recebimento diferente. Favor receber um grupo por vez.");
 					}
					return;
				}
			}

			getDeslocaProcessoService().insereProcesso(chkTipoOrigem, "PRO", codigoLotacao, processoSelecionado);

			descricaoOrigem = recuperaSetorProcesso(processo);
			
//			Efetuado a limpeza do campo Processo, tanto para Advogado e Orgão Externo
			identificacaoProcesso = null;
			
			if (descricaoOrigem != null && !descricaoOrigem.trim().equals("")) {
				setAtributo(DESCRICAO_ORIGEM, descricaoOrigem);
				DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
				tratarQuantidades(deslocaProcesso);
				CheckableDataTableRowWrapperDeslocaProcesso wrapper = new CheckableDataTableRowWrapperDeslocaProcesso(deslocaProcesso);
				wrapper.setChecked(true);
				listaDocumentos.add(wrapper);
				
				List<ProcessoDependencia> apensos = getProcessoDependenciaService().recuperarApensos(processo);
				if (apensos.size()>0) {
					adicionarApensos(apensos);
				}
			} else {
				descricao = verificaDescricaoDestino();	
				reportarErro("O processo " + processo.getIdentificacao() + " não foi deslocado para um " + descricao);
				return;
			}
			
		} catch (RegraDeNegocioException e) {
			reportarAviso(e);
		} catch (ServiceException e) {
			reportarErro("Ocorreu um erro: " + e.getLocalizedMessage(), e, LOG);
		}
		atualizaSessao();
	}
	
	private void adicionarApensos(List<ProcessoDependencia> apensos) throws ServiceException{
		if (!chkTipoOrigem.equals("ADV")) {return;}
		// recuperar os apensos
		List<DeslocaProcesso> deslocaProcessosApensos = new ArrayList<DeslocaProcesso>();
		// transformar todos os apensos (ProcessoDependencia) no tipo DeslocaProcesso para inserir na lista 
		for (ProcessoDependencia apenso : apensos) {
		    Processo processoApenso = getProcessoService().recuperarPorId(apenso.getIdObjetoIncidente());
			DeslocaProcesso deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processoApenso);
			deslocaProcessosApensos.add(deslocaProcesso);
			tratarQuantidades(deslocaProcesso);
			CheckableDataTableRowWrapperDeslocaProcesso wrapper = new CheckableDataTableRowWrapperDeslocaProcesso(deslocaProcesso);
			wrapper.setChecked(true);
			listaDocumentos.add(wrapper);
			
			List<ProcessoDependencia> apensosDeApenso = getProcessoDependenciaService().recuperarApensos(processoApenso);
			adicionarApensos(apensosDeApenso);
		}
	}
	
	private void tratarQuantidades(DeslocaProcesso deslocaProcesso) {
		if (deslocaProcesso.getId().getProcesso().getQuantidadeJuntadasLinha() == null) {
			deslocaProcesso.getId().getProcesso().setQuantidadeJuntadasLinha((short)0);
		}
		if (deslocaProcesso.getId().getProcesso().getQuantidadeVolumes() == null) {
			deslocaProcesso.getId().getProcesso().setQuantidadeVolumes((int)0); 
		}
		if (deslocaProcesso.getId().getProcesso().getQuantidadeApensosFixo() == null) {
			deslocaProcesso.getId().getProcesso().setQuantidadeApensosFixo((int)0); 
		}
		if (deslocaProcesso.getQuantidadeApensos() == null) {
			deslocaProcesso.setQuantidadeApensos((short)0); 
		}
	}
	
	public boolean getIsApenso() throws ServiceException {

		CheckableDataTableRowWrapperDeslocaProcesso chkDataTable = (CheckableDataTableRowWrapperDeslocaProcesso) tabelaDocumentos.getRowData();
		
		if (chkDataTable.getWrappedObject() instanceof DeslocaProcesso) {
			DeslocaProcesso deslocaProcesso = (DeslocaProcesso) chkDataTable.getWrappedObject();
			Processo processo = (Processo)  deslocaProcesso.getId().getProcesso();
			return getProcessoDependenciaService().isApenso(processo);
		} else {
			return false;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ObjetoIncidente pesquisarIncidentesPrincipal(Object value, String tipoGuia) throws ServiceException {
		List<ObjetoIncidente> incidentes = null;


		if (tipoGuia.equals("PRO") || (tipoGuia.equals("PRE"))) {
			incidentes = pesquisarIncidentesProcesso(value, tipoGuia);
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
	public List pesquisarIncidentesProcesso(Object value, String tipoGuia) {
		
		if (value == null) {
			return null;
		}

		String apresentaSigla = "";
		String siglaNumero = "";
		siglaNumero = value.toString();
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
					reportarErro("Número de processo inválido: " + numero);
					return null;
				}

				try {
					apresentaSigla = sigla;
					sigla = converterClasse(sigla);
				} catch (ServiceException e) {
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
	
	private String verificaDescricaoDestino() {
		String descricao;
		if (chkTipoOrigem.equals("ADV")) {
			descricao = "advogado.";
		} else if (chkTipoOrigem.equals("ORG")) {
			descricao = "orgão externo.";
		} else {
			descricao = "setor.";
		}
		return descricao;
	}
	
	public String getDescricaoOrigemExterna(){
		CheckableDataTableRowWrapperDeslocaProcesso objeto = (CheckableDataTableRowWrapperDeslocaProcesso) getTabelaDocumentos().getRowData();
		try {
			return recuperaSetorProcesso(((DeslocaProcesso) objeto.getWrappedObject()).getId().getProcesso());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String recuperaSetorProcesso(Processo processo) throws ServiceException {
		Long codigoSetor = null;
		DeslocaProcesso ultimoDeslocamento = null;
		try {
			if (processo == null) {
				reportarErro("Processo não encontrado.");
			} else {
				ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
				if (ultimoDeslocamento == null) {
					reportarErro("Não foi possível recuperar o último deslocamento do processo " + processo.getSiglaClasseProcessual() + "-" + processo.getNumeroProcessual());
				}
				codigoSetor = ultimoDeslocamento.getCodigoOrgaoDestino();
				codigoOrigem = codigoSetor; // será a origem no deslocamento de devolução
			}
		} catch (Exception e) {
			reportarErro("Erro ao recuperar o código do setor.");
		}

		if (ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(1)) {
			chkTipoOrigem = "ADV";
			return recuperaDescricaoAdvogadoProcesso(codigoSetor);
		} else if (ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(2)) {
			chkTipoOrigem = "SET";
			return recuperaDescricaoProcessoOrgaoInterno(codigoSetor);
		} else {
			chkTipoOrigem = "ORG";
			return recuperaDescricaoOrgaoExternoProcesso(codigoSetor);
		}
		
	}

	public String recuperaDescricaoOrgaoExternoProcesso(Long codigoSetor) {
		Origem origem = new Origem();
		try {
			origem = getOrigemService().recuperarPorId(codigoSetor);
			if (origem == null) {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return codigoSetor + " - " + origem.getDescricao();
	}

	public String recuperaDescricaoProcessoOrgaoInterno(Long codigoSetor) {
		Setor setor = new Setor();
		try {
			setor = getSetorService().recuperarPorId(codigoSetor);
			if (setor == null) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codigoSetor + " " + setor.getNome();
	}

	//TODO
	public String recuperaDescricaoAdvogadoProcesso(Long codigoSetor) throws ServiceException {
		Jurisdicionado advogado;
		if (codigoSetor == null){
			return null;
		}
		try {
			advogado = getJurisdicionadoService().recuperarPorId(codigoSetor);
			if (advogado == null) {
				if (processo != null) {
				    DeslocaProcesso ultimoDeslocamento = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
				    if (ultimoDeslocamento != null) {
				    	// 1- Advogado, 2- Interno, 3- Externo.
				    	if (ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(2)) {
				    		reportarAviso("O processo " + processo.getSiglaClasseProcessual() + "-" + processo.getNumeroProcessual() + " deve ser recebido de um setor do STF.");
				    	} else if (ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(3)) {
				    		reportarAviso("O processo " + processo.getSiglaClasseProcessual() + "-" + processo.getNumeroProcessual() + " deve ser recebido de um órgão externo.");
				    	}
				    } else {
			    		reportarAviso("O processo não pôde ser localizado");
				    }
				} else {
		    		reportarAviso("O processo não pôde ser localizado");
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		if (advogado == null) {
			Advogado advMAP = getAdvogadoService().recuperarPorId(codigoSetor);
			return codigoSetor + " - " + advMAP.getNome();
		}
		return codigoSetor + " - " + advogado.getNome();
	}

	private Long recuperaCodigoSetorProcesso(Processo processo) throws Exception {
		return getDeslocaProcessoService().pesquisarSetorUltimoDeslocamento(processo);
	}
	
	public void atualizaDadosPesquisaAction(ActionEvent evt) {
		if (chkTipoOrigem.equals("SET")) {
			comportamentoInput(true, "panelPesquisaOrigem", "form");
		} else {
			comportamentoInput(false, "panelPesquisaOrigem", "form");
		}
		atualizaSessaoAction(evt);
	}

	// exibe ou esconde o compoente
	private void comportamentoInput(boolean visivel, String idComp, String idForm) {
		try {
			int i = 0;
			UIComponent form = FacesContext.getCurrentInstance().getViewRoot().findComponent(idForm);
			while (form.findComponent(idComp) == null){
				form = form.getParent();
				i++;
				if (i > 20) {
					throw new Exception("Componente de interface não localizado");
				}
			}
			if (visivel) {
				form.findComponent(idComp).getAttributes().put("style", "visibility:visible");
			} else {
				form.findComponent(idComp).getAttributes().put("style", "visibility:hidden");
			}
		} catch (Exception e) {
			reportarErro("Erro ao recuperar componente de interface");
		}
	}

	// remove as guias antigas do list de guias para impressao e as retorna
	private List<Guia> getGuiasAntigasDoList() throws ServiceException{
		List<Guia> guiasAntigas = new ArrayList<Guia>();
		for (Guia guia: guiasParaImpressao) {
			if (!getEmprestimoAutosProcessoService().existeEmprestimoNaGuiaDeAutos(guia)) {
				guiasAntigas.add(guia);
			}
		}
		guiasParaImpressao.removeAll(guiasAntigas);
		return guiasAntigas;
	}
	
	
	public void gerarPDFGuiaInLine() throws IOException{
		try {
			if (guiasParaImpressao == null || guiasParaImpressao.size() == 0) {
				return;
			}
			
			// determinar o tipo da guia
			boolean tipoPeticao = true;
			tipoPeticao = getGuiaService().isPeticao(guiasParaImpressao.get(0));
			
			String arquivo = "";
			if (!tipoPeticao) {
				if (chkTipoOrigem.equals("ADV")) {
					List<Guia> guiasAntigas = getGuiasAntigasDoList();
					if (guiasAntigas.size() > 0) {
						//arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaAntigaDevolucaoAutosProcesso(guiasAntigas);
						arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaAntigaDevolucaoProcessosNaPastaTemp(guiasAntigas);
					}
					if (guiasParaImpressao.size() > 0) {
						//arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDevolucaoAutosProcesso(guiasParaImpressao);
						arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDevolucaoProcessosNaPastaTemp(guiasParaImpressao);
					}
					
				} else {
					//arquivo = getProcessamentoRelatorioService().criarRelatorioVariasGuiasDeslocamentoProcesso(guiasParaImpressao);
					arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaRecebimentoProcessosNaPastaTemp(guiasParaImpressao);
				}
			} else {
				//arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDeslocamentoPeticao(guiasParaImpressao);
				arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaRecebimentoPeticoesNaPastaTemp(guiasParaImpressao);
			}
			mandarRespostaInline(arquivo);
		} catch (ServiceException e) {
			reportarErro("Erro ao imprimir a guia: " + e.getMessage());
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

	public void imprimir(ActionEvent evt) {
		try {
			if (guiasParaImpressao == null || guiasParaImpressao.size() == 0) {
				return;
			}
			
			// determinar o tipo da guia
			boolean tipoPeticao = true;
			tipoPeticao = getGuiaService().isPeticao(guiasParaImpressao.get(0));
			
			byte[] arquivo = null;
			if (!tipoPeticao) {
				if (chkTipoOrigem.equals("ADV")) {
					List<Guia> guiasAntigas = getGuiasAntigasDoList();
					if (guiasAntigas.size() > 0) {
						arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaAntigaDevolucaoAutosProcesso(guiasAntigas);
					}
					if (guiasParaImpressao.size() > 0) {
						arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDevolucaoAutosProcesso(guiasParaImpressao);
					}
					
				} else {
					arquivo = getProcessamentoRelatorioService().criarRelatorioVariasGuiasDeslocamentoProcesso(guiasParaImpressao);
				}
			} else {
				arquivo = getProcessamentoRelatorioService().criarRelatorioGuiaDeslocamentoPeticao(guiasParaImpressao);
			}

			ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
			mandarRespostaDeDownloadDoArquivo(input);
		} catch (ServiceException e) {
			reportarErro("Erro ao imprimir a guia: " + e.getMessage());
		}
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

	public void salvarRecebimentoDeVariasOrigens(ActionEvent evt) {
		try {
			List<DeslocaProcesso> listaDeslocamentosParaRecebimento = new ArrayList<DeslocaProcesso>();
			Map<Long, String> observacoes = new HashMap<Long, String>();
			
			ArrayList<Long> arrayObjIncidente = new ArrayList<Long>();
			
			for (CheckableDataTableRowWrapperDeslocaProcesso lista: listaDocumentos) {
				DeslocaProcesso ultimoDesloca = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso( ((DeslocaProcesso) lista.getWrappedObject()).getId().getProcesso() );
				if (ultimoDesloca.getGuia().getTipoOrgaoDestino().equals(2)) {
					reportarAviso("O processo " + ultimoDesloca.getId().getProcesso().getSiglaClasseProcessual() + " " + ultimoDesloca.getId().getProcesso().getNumeroProcessual() + " já foi recebido.");
					return;
				}
				listaDeslocamentosParaRecebimento.add((DeslocaProcesso) lista.getWrappedObject());
				
				arrayObjIncidente.add(ultimoDesloca.getId().getProcesso().getId());
			}
			
			Collections.sort(listaDeslocamentosParaRecebimento, new PropertyComparator<DeslocaProcesso>(TipoOrdenacao.ASCENDENTE, "guia.codigoOrgaoDestino"));
			for (DeslocaProcesso deslocaProcesso: listaDeslocamentosParaRecebimento) {
				if (chkTipoOrigem.equals("ADV")) {
					observacoes.put(deslocaProcesso.getId().getProcesso().getId(), getEmprestimoAutosProcessoService().getNomeAdvogadoOuAutorizado(deslocaProcesso.getGuia()));
				} else {
					observacoes.put(deslocaProcesso.getId().getProcesso().getId(), deslocaProcesso.getGuia().getDescricaoDestino());
				}
			}

			Usuario usuario = new Usuario();
			usuario.setId(getUser().getUsername());
			usuario.setSetor(((UsuarioAssinatura) getUser()).getSetor());

			guiasParaImpressao.clear();
			guiasParaImpressao = getEmprestimoAutosProcessoService().salvarRecebimentoDeVariasOrigens(listaDeslocamentosParaRecebimento, usuario, chkTipoOrigem.equals("ADV"), observacoes );
		    // se a operação teve sucesso atualizar as datas de recebimento pendentes e remover os itens da lista
			listaDocumentos.clear();
			
			//se o processo estiver na situação K e for recebido novamente pelo tribunal ele deve se tornar ativo novamente
			for (Long seqObjetoIncidente : arrayObjIncidente) {
				Processo processoRecebido = getProcessoService().recuperarPorId(seqObjetoIncidente);
				if (chkTipoOrigem.equals("ORG")) {
					
					if(processoRecebido.getSituacao() == SituacaoProcesso.DETERMINADA_DEVOLUCAO) {
						if(processoRecebido.getRelatorIncidenteId() != null ) {
							if (processoRecebido.getRelatorIncidenteId().longValue() == Ministro.COD_MINISTRO_PRESIDENTE.longValue()){
								processoRecebido.setSituacao(SituacaoProcesso.REGISTRADO_PRESIDENTE);
								getProcessoService().flushSession();
							}else{
								processoRecebido.setSituacao(SituacaoProcesso.DISTRIBUIDO);
								getProcessoService().flushSession();
							}
						} else {
						//	processoRecebido.setSituacao(SituacaoProcesso.CADASTRAMENTO);
						//	getProcessoService().flushSession();
							processoRecebido.setSituacao(SituacaoProcesso.AUTUADO);	
							getProcessoService().flushSession();
						}						
					}
				}
			}
			
			
			reportarInformacao("Documentos recebidos com sucesso.");
			enablePanelRecebimentoProcesso = true;
			atualizaSessao();
		} catch (ServiceException e){
			reportarErro("Erro no recebimento de várias origens." + e.getMessage());
		}
	}

	/*
	public void salvarRecebimentoVariasOrigens(ActionEvent evt) {
		// vai segmentar um list origem por origem e para cada um chamar o método salvarRecebimentoDocumentos() passando o list segmentado.
		int i = 0;
		Long origemAnterior = 0L;
		guiasParaImpressao.clear();
		List<CheckableDataTableRowWrapperDeslocaProcesso> listaReceber = new ArrayList<CheckableDataTableRowWrapperDeslocaProcesso>();
		try {
			for (CheckableDataTableRowWrapperDeslocaProcesso lista: listaDocumentos) {
				i ++;
				if (i == 1){
					origemAnterior = ((DeslocaProcesso) lista.getWrappedObject()).getCodigoOrgaoDestino();
				}
				if (!origemAnterior.equals(((DeslocaProcesso) lista.getWrappedObject()).getCodigoOrgaoDestino())){
					salvarRecebimentoDocumentos(listaReceber);
					listaReceber.clear();
					origemAnterior = ((DeslocaProcesso) lista.getWrappedObject()).getCodigoOrgaoDestino();
				}
				listaReceber.add( lista );
			}
			if (listaReceber.size() > 0) {
				salvarRecebimentoDocumentos(listaReceber);
			}
		} catch (Exception e) {
			reportarErro("Erro ao receber processo.");
			return;
		}
		
	    // se a operação teve sucesso atualizar as datas de recebimento pendentes e remover os itens da lista
		listaDocumentos.removeAll(listaReceber);
		reportarInformacao("Documentos recebidos com sucesso.");
		enablePanelRecebimentoProcesso = true;
		atualizaSessao();

	}
	*/
	
	public void salvarRecebimentoDocumentos(ActionEvent evt){
		try {
			salvarRecebimentoDocumentos(listaDocumentos);
		} catch (Exception e) {
			reportarErro("Erro ao receber processo.");
		}
		listaDocumentos.removeAll(listaDocumentos);
		reportarInformacao("Documentos recebidos com sucesso.");
		enablePanelRecebimentoProcesso = true;
		atualizaSessao();


	}

	private void salvarRecebimentoDocumentos(List<CheckableDataTableRowWrapperDeslocaProcesso> listaReceber) throws ServiceException {
		List<CheckableDataTableRowWrapperDeslocaProcesso> listaTabela = retornarItensCheckableSelecionados(listaReceber);

		if (listaTabela.size() == 0) {
			reportarAviso("Selecione pelo menos um processo para recebimento.");
			return;
		}
		ArrayList<Long> arrayObjIncidente = new ArrayList<Long>();
		
    	// gerar coleção de seq_objeto_incidente a ser deslocada para o parâmetro do método
		for (CheckableDataTableRowWrapperDeslocaProcesso check : listaTabela) {
			Processo processo = ((DeslocaProcesso) check.getWrappedObject()).getId().getProcesso();
			arrayObjIncidente.add(processo.getId());
		}
			
		Usuario usuario = new Usuario();
		usuario.setId(getUser().getUsername());
		usuario.setSetor(((UsuarioAssinatura) getUser()).getSetor());
		String observacao = null;
		// para manter a compatibilidade com as cargas dos autos com a entidade Advogado (como é feito no MAP) deve procurar se a origem ainda está em advogado
		// antes de pesquisar em jurisdicionado como será feito a partir do eSTF-Processamento
		int tipoOrigemDestino = 0;
		if (chkTipoOrigem.equals("ADV")) {
			tipoOrigemDestino = 1;
		} else if (chkTipoOrigem.equals("SET")) {
			tipoOrigemDestino = 2;
		} else if (chkTipoOrigem.equals("ORG")) {
			tipoOrigemDestino = 3;
		}
		// recupera a origem do processo
		setCodigoOrigem( ((DeslocaProcesso) listaTabela.get(0).getWrappedObject()).getCodigoOrgaoDestino() );
		OrigemDestino origemAdvogado = getOrigemDestinoService().recuperarPorId(codigoOrigem, tipoOrigemDestino);
		if (origemAdvogado == null) {
			Jurisdicionado origemJurisdicionado = getJurisdicionadoService().recuperarPorId(codigoOrigem);
			observacao = origemJurisdicionado.getNome();
		} else {
			observacao = origemAdvogado.getDescricao();
		}
		Guia guia = getEmprestimoAutosProcessoService().salvarRecebimento(arrayObjIncidente, usuario, observacao, chkTipoOrigem.equals("ADV"));
		
		guiasParaImpressao.add(guia);
		setAtributo(GUIAS_PARA_IMPRESSAO, guiasParaImpressao);
		
		try {
			Processo processo = ((DeslocaProcesso) listaTabela.get(0).getWrappedObject()).getId().getProcesso();
			DeslocaProcesso ultimo = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo);
			codigoOrigem = ultimo.getId().getCodigoOrgaoOrigem();
			numGuia = ultimo.getGuia().getNumeroGuia().toString();
			anoGuia = ultimo.getGuia().getAnoGuia().toString();
			setSucesso("S");
		} catch (ServiceException e) {
			reportarErro("Erro ao ao descobrir o deslocamento. " + e.getMessage());
		}
		
	}

	public String verificaTipoOrgaoOrigem() {
		if (getAtributo(CHK_TIPO_ORIGEM).equals("SET")) {
			return "2";
		} else if (getAtributo(CHK_TIPO_ORIGEM).equals("ADV")) {
			return "1";
		}
		return "3";
	}

	public List<Processo> pesquisaSuggestionBox(Object suggest) {
		adicionaProcesso = true;
		return super.pesquisaSuggestionBox(suggest.toString().toUpperCase());
	}

	/**
	 * Pesquisa guias para recebimento
	 * @throws ServiceException
	 * @throws NumberFormatException
	 */
	public void pesquisarGuia() throws ServiceException, NumberFormatException {
		Guia guia = new Guia();

		if (anoGuia.trim().isEmpty()) {
			reportarErro("O ano da guia não pode ser vazio.");
			return;
		}

		if (numGuia.trim().isEmpty()) {
			reportarErro("O número da guia não pode ser vazio.");
			return;
		}
		
		if (numGuia.indexOf("/") > 0) {
			reportarErro("O ano da guia deve ser informado através do campo 'Ano' ao lado do 'Número da Guia'. Informar apenas valores numéricos.");
			return;
		}

		if (codigoOrigem == null) {
			reportarErro("A origem da guia não pode ser vazia.");
			return;
		}
		
		guia.setNumeroGuia(Long.parseLong(numGuia));
		guia.setAnoGuia(Short.valueOf((anoGuia)));
		guia.setCodigoOrgaoOrigem(codigoOrigem);
		try {
				pesquisaGuiaProcesso = true;
				List<DeslocaProcesso> listDeslocaProcesso = getDeslocaProcessoService().recuperarDeslocamentoProcessosRecebimentoExterno(guia);
				
				try {
					for (DeslocaProcesso deslocaProcesso : listDeslocaProcesso) {
						if (deslocaProcesso.getId().getProcesso().getTipoObjetoIncidente() == null ) {
							reportarAviso("Erro ao recuperar informações do(s) processo(s).");	
							return ;
						}
					}} catch (Exception e) {
						reportarAviso("Existem processos classificados como 'Oculto'. Para receber a guia será necessário possuir esse perfil.");	
						return ;
				}
				
				List<CheckableDataTableRowWrapper> listGuia = getCheckableDataTableRowWrapperList(getDeslocaProcessoService().recuperarDeslocamentoProcessosRecebimentoExterno(guia));
				if(listGuia.isEmpty()){
					pesquisaGuiaProcesso = false;
					pesquisaGuiaPeticao = true;
					listGuia = getCheckableDataTableRowWrapperList(getDeslocamentoPeticaoService().recuperarDeslocamentoPeticaoRecebimentoExterno(guia));
					if (listGuia != null && listGuia.size()>0){
						chkTipoPesquisa = "PET";
					}
				}
				
			for (CheckableDataTableRowWrapper check : listGuia) {
				if (chkTipoPesquisa.equals("PET")) {
					DeslocaPeticao deslocaPeticao = (DeslocaPeticao) check.getWrappedObject();
					deslocaPeticao.setAnoPeticao(deslocaPeticao.getId().getPeticao().getAnoPeticao().longValue());
					deslocaPeticao.setNumeroPeticao(deslocaPeticao.getId().getPeticao().getNumeroPeticao().longValue());
					check.setWrappedObject(deslocaPeticao);
				}
				check.setChecked(true);
			}
			listaGuia = listGuia;
			if (listaGuia.isEmpty()) {
				reportarInformacao("Nenhum registro encontrado.");
			} else {
				if (chkTipoPesquisa.equals("PET")) {
					getProcessoPrincipalGuiaPeticao();
				}
				reportarInformacao("Foram encontrados " + listaGuia.size() + " registros.");
			}
		} catch (NumberFormatException e) {
			reportarAviso("Os campos 'Número da Guia' e 'Ano' devem conter apenas números.");
		} catch (Exception e) {
			reportarAviso("Erro ao pesquisar a guia");
		}
		atualizaSessao();
	}

	public String getProcessoPrincipalGuiaPeticao() throws ServiceException {
		if (tabelaGuias == null) {
			return "";
		}
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaGuias.getRowData();
		if (chkDataTable.getWrappedObject() instanceof DeslocaPeticao) {
			DeslocaPeticao deslocaPeticao = (DeslocaPeticao) chkDataTable.getWrappedObject();
			Peticao peticao = deslocaPeticao.getId().getPeticao();
			if (peticao.getObjetoIncidenteVinculado() == null) {
				return "";
			} else {
				return peticao.getObjetoIncidenteVinculado().getIdentificacao();
			}
		} else {
			return "";
		}
	}

	public List<CheckableDataTableRowWrapper> getCheckableGuiaList(List<Guia> listaGuia) {
		return getCheckableDataTableRowWrapperList(listaGuia);
	}

	public List pesquisarDestinatario(Object value) {
		pesquisaProcesso = true;
		try {

			List<ResultSuggestionOrigemDestino> listaDestinatarios = new ArrayList<ResultSuggestionOrigemDestino>();
			@SuppressWarnings("rawtypes")
			List destinatarios = new ArrayList();

			if (value == null || value.toString().trim() == "") {
				return null;
			}

			switch (chkTipoOrigem.charAt(0)) {
			case 'S':
				if(NumberUtils.soNumeros(value.toString())){
					destinatarios = getSetorService().pesquisarSetoresPorId(Long.parseLong(value.toString()), true,true);
				}else{
					destinatarios = getSetorService().pesquisarSetoresPorDescricao(value.toString(), true,true);
				}
				
				break;
			case 'A':
				destinatarios = getJurisdicionadoService().recuperarJurisdicionadoDosEmprestimosNaoDevolvidos(value);
				break;
			case 'O':
				if(NumberUtils.soNumeros(value.toString())){
					destinatarios = getOrigemService().recuperarOrigemPorId(Long.parseLong(value.toString()), true);
				}else{
					destinatarios =  getOrigemService().recuperarOrigemPorDescricao(value.toString(), true);
				}
				break;
			default:
			}
			if (destinatarios != null) {
				for (Object objeto : destinatarios) {
					listaDestinatarios.add(ResultSuggestionOrigemDestino.build(objeto));
				}
			}
			return listaDestinatarios;
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar o destinatário: " + value.toString());
			return null;
		}
	}
	
	public Boolean getImpedirMarcacao(){
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaGuias.getRowData();
		if (chkDataTable.getWrappedObject() instanceof DeslocaPeticao){
			return false;
		} else {
			return true;
		}
		
	}

	/**
	 * Recebimento externo de petições.
	 */
	public void salvarRecebimentoPeticaoVariasOrigens(){
		int i = 0;
		Long origemAnterior = 0L;
		guiasParaImpressao.clear();
		exibePopupGuia = "N";

		List<CheckableDataTableRowWrapper> listaReceber = new ArrayList<CheckableDataTableRowWrapper>();
		try {
			for (CheckableDataTableRowWrapper lista: listaGuia) {
				i ++;
				if (i == 1){
					origemAnterior = ((DeslocaPeticao) lista.getWrappedObject()).getCodigoOrgaoDestino();
				}
				if (!origemAnterior.equals(((DeslocaPeticao) lista.getWrappedObject()).getCodigoOrgaoDestino())){
					salvarRecebimentoPeticao(listaReceber);
					listaReceber.clear();
					origemAnterior = ((DeslocaPeticao) lista.getWrappedObject()).getCodigoOrgaoDestino();
				}
				listaReceber.add( lista );
			}
			if (listaReceber.size() > 0) {
				salvarRecebimentoPeticao(listaReceber);
			}
		} catch (Exception e) {
			reportarErro("Erro ao receber petição.");
			return;
	    } finally {
			pesquisaGuiaPeticao = true;
			pesquisaGuiaProcesso = false;
			atualizaSessao();
	    }
		if (numGuia != null && anoGuia != null) {
			exibePopupGuia = "S";
		}
		reportarInformacao("Petição(ões) recebida(s) com sucesso!");
	}
	
	
	
	// salvar o recebimento de órgão EXTERNO de petições
	// o recebimento interno de petições e processos é tratado pelo salvarRecebimentoGuia()
	public void salvarRecebimentoPeticao(List<CheckableDataTableRowWrapper> listaReceber) {
		try {
			ArrayList<Long> arrayObjIncidente = new ArrayList<Long>();
			
			if (listaReceber == null || listaReceber.size()==0) {
				reportarErro("Guia não encontrada!");
				return;
			}
			Usuario usuario = new Usuario();
			usuario.setId(getUser().getUsername());
			usuario.setSetor(((UsuarioAssinatura) getUser()).getSetor());

			for (CheckableDataTableRowWrapper chkDeslocaPeticao: listaReceber) {
				DeslocaPeticao deslocaPeticao = (DeslocaPeticao) chkDeslocaPeticao.getWrappedObject();
				arrayObjIncidente.add(deslocaPeticao.getId().getPeticao().getId());
			}

			setCodigoOrigem( ((DeslocaPeticao) listaReceber.get(0).getWrappedObject()).getCodigoOrgaoDestino() );
			Guia guia = getEmprestimoAutosProcessoService().salvarRecebimentoPeticoes(arrayObjIncidente, usuario);

			numGuia = guia.getNumeroGuia().toString();
			anoGuia = guia.getAnoGuia().toString();
			if (guia.getId() == null) {
				guia.setId(new GuiaId());
			}
			guia.getId().setAnoGuia(guia.getAnoGuia());
			guia.getId().setNumeroGuia(guia.getNumeroGuia());
			guia.getId().setCodigoOrgaoOrigem(codigoOrigem);
			guia.setCodigoOrgaoOrigem(codigoOrigem);
			guia.setCodigoOrgaoDestino(codigoLotacao);
			guiasParaImpressao.add(guia);
			atualizaSessao();
		} catch (Exception e) {
			reportarErro("Erro ao receber petições!");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void salvarRecebimentoGuia(ActionEvent evt) throws ServiceException {
		
		// se for recebimento externo de petição chamar método específico
		String tipo = "";
		if (listaGuia == null || listaGuia.size() == 0) {
			return;
		}
		if (listaGuia.get(0).getWrappedObject() instanceof DeslocaPeticao) {
			tipo = "PET";
		}
		// se petição e órgão externo, irá chamar o método específico para recebimento externo de petições
		if (tipo.equals("PET") && (!chkTipoOrigem.equals("SET"))) { 
			salvarRecebimentoPeticaoVariasOrigens();
			return;
		}
		
		exibePopupGuia = "N"; // não exibe popup de impressão de guia para os recebimentos de trânsito interno
		
		Guia guia = new Guia();
		guia.setAnoGuia(Short.valueOf(anoGuia));
		List<CheckableDataTableRowWrapper> listaProcessoPeticaoGuia = listaGuia;
		
		GuiaId guiaId = new GuiaId();
		guiaId.setAnoGuia((new Short(anoGuia)));
		guiaId.setNumeroGuia(new Long(numGuia));
		guiaId.setCodigoOrgaoOrigem(codigoOrigem);
		guia.setId(guiaId);
		
		Guia criticarGuia = getGuiaService().recuperarPorId(guiaId);
		
		if (!getSetorUsuarioAutenticado().getId().equals(criticarGuia.getCodigoOrgaoDestino())) {
			reportarAviso("O destino da guia é diferente do setor do usuário");
			finalizaRecebimento();
			atualizaSessao();
			return;
		}
		
		try {
			List<DeslocaProcesso> deslocaProcessos = new ArrayList<DeslocaProcesso>();
			List<DeslocaPeticao> deslocaPeticoes = new ArrayList<DeslocaPeticao>();
			for (CheckableDataTableRowWrapper processoPeticaoGuia : listaProcessoPeticaoGuia) {
				if (processoPeticaoGuia.getWrappedObject().getClass().equals(DeslocaProcesso.class)) {
					deslocaProcessos.add((DeslocaProcesso) processoPeticaoGuia.getWrappedObject());
				} else {
					deslocaPeticoes.add((DeslocaPeticao) processoPeticaoGuia.getWrappedObject());
				}
			}
			if (deslocaProcessos.size() > 0) {
				getObjetoIncidenteService().salvarRecebimentoProcessos(deslocaProcessos);
			} else {
				getObjetoIncidenteService().salvarRecebimentoPeticoes(deslocaPeticoes);
			}
			setSucesso("S");

		} catch (Exception e) {
			reportarErro("Erro ao receber guia. " + e.getMessage());
			return;
		}

		reportarInformacao("Guia " + guia.getId().getNumeroGuia() + "/" + guia.getId().getAnoGuia() + " recebida com sucesso!");
		finalizaRecebimento();
		atualizaSessao();
	}

	public String recuperarSetorUsuario() throws ServiceException {
		return getSetorUsuarioAutenticado().getNome();
	}

	public Long recuperarCodigoSetorUsuario() throws ServiceException {
		return getSetorUsuarioAutenticado().getId();
	}

	public void atualizaBotaoAction(ActionEvent evt) {
		getTitleBotaoPesquisar();
	}

	public void limparOrigemPesquisa() {
		setCodigoOrigem(null);
		atualizaSessao();
	}

	public void pesquisarAction(ActionEvent evt) throws Exception {
		setSucesso("N");
		if (chkTipoOrigem.equals("SET")) {
			pesquisarGuia();
		} else {
			if (descricaoOrigem.isEmpty()) {
				reportarAviso("Adicione uma origem para realizar a pesquisa.");
				return;
			} else {
				pesquisarProcessos();
			}
		}
		atualizaSessao();
	}

	public boolean getEnablePesquisa() {
		if (chkTipoOrigem.equals("SET") && descricaoOrigem == null) {
			return true;
		}
		return false;
	}
	
	public Boolean getDesabilitarMarcarTodos() {
		if (listaDocumentos == null || listaDocumentos.size() == 0) {
			return true;
		} else {
			return false;
		}
			
	}

	public void marcarTodosDocumentos(ActionEvent evt) {
		if (listaDocumentos == null || listaDocumentos.size() == 0) {
			return;
		}
		marcarOuDesmarcarTodas(listaDocumentos);
		setListaDocumentos(listaDocumentos);
	}

	public void visualizaTabelaProcesso(ActionEvent evt) {
		adicionaProcesso = true;
		atualizaSessao();
	}

	public void atualizaSessaoAction(ActionEvent evt) {
		if(listaDocumentos == null || chkTipoOrigem.equals("ADV") ||  chkTipoOrigem.equals("ORG") || chkTipoOrigem.equals("SET")){
			finalizaRecebimento();
		}
		atualizaSessao();
	}

	public String insereProcessoListaAlterada() throws ServiceException {
		listaDocumentos = null;
		listaDocumentos = new ArrayList<CheckableDataTableRowWrapperDeslocaProcesso>();
		codigoSetorProcesso = null;
			
		if(getAtributo(PROCESSO_INSERT) != null){
			descricaoOrigem = recuperaSetorProcesso((Processo) getAtributo(PROCESSO_INSERT));
			try {
				codigoOrigem = recuperaCodigoSetorProcesso((Processo) getAtributo(PROCESSO_INSERT));
			} catch (Exception e) {
				reportarErro("Erro ao rescuperar o código Origem.", e.getLocalizedMessage());
			}	
			
			identificacaoProcesso = ((Processo) getAtributo(PROCESSO_INSERT)).getIdentificacao();
		}
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		atualizaSessao();
		insereProcessoParaRecebimento();
		return null;
	}

	public void atualizaTipoPesquisa(ActionEvent evt) {
		atualizaSessao();
	}

	public void limparPesquisaAction(ActionEvent evt) {
		limparPesquisa();
	}
	public void limparPesquisa() {
		getIsSetorStf();
		getIsOutrosSetores();
		getTitleBotaoPesquisar();
		numGuia = null;
		descricaoOrigem = null;
		codigoOrigem = null;
		codigoSetorProcesso = null;
		listaDocumentos = null;
		listaDocumentos = new ArrayList<CheckableDataTableRowWrapperDeslocaProcesso>();
		listaGuia = null;
		identificacaoProcesso = null;
		sucesso="N";
		enablePanelRecebimentoProcesso = false;
		atualizaSessao();
	}
	
	public void finalizaRecebimento() {
		getIsSetorStf();
		getIsOutrosSetores();
		getTitleBotaoPesquisar();
		descricaoOrigem = null;
		codigoOrigem = null;
		codigoSetorProcesso = null;
		listaDocumentos = null;
		listaDocumentos = new ArrayList<CheckableDataTableRowWrapperDeslocaProcesso>();
		listaGuia = null;
		identificacaoProcesso = null;
		//numGuia = null;
		atualizaSessao();
	}

	public void limparOrigemPesquisaAction(ActionEvent evt) {
		limparOrigemPesquisa();
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaGuias() {
		return tabelaGuias;
	}

	public void setTabelaGuias(org.richfaces.component.html.HtmlDataTable tabelaGuias) {
		this.tabelaGuias = tabelaGuias;
	}

	public String getNumGuia() {
		return numGuia;
	}

	public void setNumGuia(String numGuia) {
		this.numGuia = numGuia;
		setAtributo(NUM_GUIA, numGuia);
	}

	public String getAnoGuia() {
		return anoGuia;
	}

	public void setAnoGuia(String anoGuia) {
		this.anoGuia = anoGuia;
	}

	public List<CheckableDataTableRowWrapper> getListaGuia() {
		return listaGuia;
	}

	public void setListaGuia(List<CheckableDataTableRowWrapper> listaGuia) {
		setAtributo(KEY_LISTA_GUIA, listaGuia);
		this.listaGuia = listaGuia;
	}

	public void setCodigoDestino(Long codigoDestino) {
		this.codigoDestino = codigoDestino;
	}

	public Long getCodigoDestino() {
		return codigoDestino;
	}

	public void setCodigoLotacao(Long codigoLotacao) {
		this.codigoLotacao = codigoLotacao;
	}

	public Long getCodigoLotacao() {
		return codigoLotacao;
	}

	public String getDescricaoOrigem() {
		return descricaoOrigem;
	}

	public void setDescricaoOrigem(String descricaoOrigem) {
		this.descricaoOrigem = descricaoOrigem;
		atualizaSessao();
	}

	public Long getCodigoOrigem() {
		return codigoOrigem;
	}

	public void setCodigoOrigem(Long codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
		atualizaSessao();
	}

	public void setDescricaoOrigemRecebimento(String descricaoOrigemRecebimento) {
		this.descricaoOrigemRecebimento = descricaoOrigemRecebimento;
	}

	public String getDescricaoOrigemRecebimento() {
		return descricaoOrigemRecebimento;
	}

	public void setChkTipoOrigem(String chkTipoOrigem) {
		this.chkTipoOrigem = chkTipoOrigem;
	}

	public String getChkTipoOrigem() {
		return chkTipoOrigem;
	}

	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		this.identificacaoProcesso = identificacaoProcesso;
	}

	public String getIdentificacaoProcesso() {
		return identificacaoProcesso;
	}

	public void setListaDocumentos(List<CheckableDataTableRowWrapperDeslocaProcesso> listaDocumentos) {
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		this.listaDocumentos = listaDocumentos;
	}

	public List<CheckableDataTableRowWrapperDeslocaProcesso> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setCodigoOrigemPesquisa(Long codigoOrigemPesquisa) {
		this.codigoOrigemPesquisa = codigoOrigemPesquisa;
	}

	public Long getCodigoOrigemPesquisa() {
		return codigoOrigemPesquisa;
	}

	public void setTabelaDocumentos(org.richfaces.component.html.HtmlDataTable tabelaDocumentos) {
		this.tabelaDocumentos = tabelaDocumentos;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaDocumentos() {
		return tabelaDocumentos;
	}

	public void setProcesso(Processo processo) {
		setAtributo(PROCESSO_INSERT, processo);
		this.processo = processo;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setAdicionaProcesso(boolean adicionaProcesso) {
		this.adicionaProcesso = adicionaProcesso;
	}

	public boolean isAdicionaProcesso() {
		return adicionaProcesso;
	}

	public void setPesquisaProcesso(boolean pesquisaProcesso) {
		this.pesquisaProcesso = pesquisaProcesso;
	}

	public boolean isPesquisaProcesso() {
		return pesquisaProcesso;
	}

	public void setCodigoSetorProcesso(Long codigoSetorProcesso) {
		this.codigoSetorProcesso = codigoSetorProcesso;
	}

	public Long getCodigoSetorProcesso() {
		return codigoSetorProcesso;
	}

	public void setEnableModalPanel(boolean enableModalPanel) {
		this.enableModalPanel = enableModalPanel;
	}

	public boolean getEnableModalPanel() {
		return enableModalPanel;
	}

	public void setEnablePanelRecebimentoProcesso(boolean enablePanelRecebimentoProcesso) {
		this.enablePanelRecebimentoProcesso = enablePanelRecebimentoProcesso;
	}

	public boolean getEnablePanelRecebimentoProcesso() {
		return enablePanelRecebimentoProcesso;
	}

	public void setPesquisaGuiaProcesso(boolean pesquisaGuiaProcesso) {
		this.pesquisaGuiaProcesso = pesquisaGuiaProcesso;
	}

	public boolean getPesquisaGuiaProcesso() {
		return pesquisaGuiaProcesso;
	}

	public void setPesquisaGuiaPeticao(boolean pesquisaGuiaPeticao) {
		this.pesquisaGuiaPeticao = pesquisaGuiaPeticao;
	}

	public boolean getPesquisaGuiaPeticao() {
		return pesquisaGuiaPeticao;
	}

	public void setChkTipoPesquisa(String chkTipoPesquisa) {
		this.chkTipoPesquisa = chkTipoPesquisa;
	}

	public String getChkTipoPesquisa() {
		return chkTipoPesquisa;
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

	public void setSucesso(String sucesso) {
		setAtributo(SUCESSO, sucesso);
		this.sucesso = sucesso;
	}

	public String getSucesso() {
		return sucesso;
	}

	public void setExibePopupGuia(String exibePopupGuia) {
		this.exibePopupGuia = exibePopupGuia;
	}

	public String getExibePopupGuia() {
		return exibePopupGuia;
	}

	public List<Guia> getGuiasParaImpressao() {
		return guiasParaImpressao;
	}

	public void setGuiasParaImpressao(List<Guia> guiasParaImpressao) {
		this.guiasParaImpressao = guiasParaImpressao;
	}

}