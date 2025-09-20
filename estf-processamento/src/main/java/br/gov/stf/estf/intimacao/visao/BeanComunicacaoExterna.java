package br.gov.stf.estf.intimacao.visao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.intimacao.model.service.exception.AndamentoNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteSemAceiteInitmacaoEletronicaException;
import br.gov.stf.estf.intimacao.model.service.exception.PecaNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.PessoaSemUsuarioException;
import br.gov.stf.estf.intimacao.model.service.exception.ProcessoNaoEletronicoException;
import br.gov.stf.estf.intimacao.model.service.exception.TipoModeloComunicacaoEnumInvalidoException;
import br.gov.stf.estf.intimacao.model.vo.TipoRecebimentoComunicacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.AndamentosDTO;
import br.gov.stf.estf.intimacao.visao.dto.ComunicacaoExternaDTO;
import br.gov.stf.estf.intimacao.visao.dto.ParteDTO;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.estf.intimacao.visao.dto.ProcessoObjetoIncidenteDTO;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;


/**
 * Bean que implementa o CRUD de comunicação externa (intimações, Citações).
 *
 * @author roberio.fernandes
 */
public class BeanComunicacaoExterna extends AssinadorBaseBean {

	private static final long serialVersionUID = -6063123040744534373L;

	private String retornoProcesso;
	private Processo processo;
	private Integer rowIndexDataTableResultado;
	private Long idProcesso;
	private Long idProcessoAndamentos;
	private Long idProcessoPecas;
	private Long idProcessoPartesIntimaveisCarregadas;
	private Long idProcessoPartesNaoIntimaveisCarregadas;
	private Long numeroProcessoPesqPrincipal;
	private Long idComunicacao;
	private Long tipoIncidentePreferencia;
	private List<SelectItem> IncidentePreferencia;
	private String siglaClassePesqPrincipal;
	private List<String> classes;
	private List<SelectItem> listaTipoModeloDocumento;
	private List<SelectItem> tiposComunicacoesGeracao;
	private ModeloComunicacaoEnum modeloComunicacaoEnum;
	private ObjetoIncidente<ObjetoIncidente<?>> objetoIncidente;
	private List<AndamentoProcesso> listaAndamento;
	private List<AndamentosDTO> listaAndamentoDto;

	private List<PecaProcessoEletronico> listaPeca;
	private List<PecaDTO> listaPecasDTO;

	private List<Parte> listaParte;
	private List<ParteDTO> listaPartesDTO;
	private List<ParteDTO> listaPartesDTONaoIntimaveis;
	private boolean statusBotao = false;

	private HtmlDataTable dataTableListaComunicacao;

	/**
	 * Listas da tela pesquisa
	 */
	private List<SelectItem> listaOrgaoIntimado;
	private List<SelectItem> listaSituacao;
	private List<SelectItem> listaTipoComunicacao;
	private Date periodoInicio;
	private Date periodoFim;
	private List<ComunicacaoExternaDTO> listaComunicacaoExterna;
	private Boolean setorAutorizado;

	/**
	 * Atributos da tela pesquisa
	 */
	private String orgaoIntimado;
	private TipoRecebimentoComunicacaoEnum tipoSituacao;
	private String descricaoTipoComunicacao;

	/**
	 * Campos tela pesquisaela.
	 */
	private List<Object> listaResultadoPesquisa;
	private List<SelectItem> listaUsuariosExternos;
	private Integer usuarioExterno;
	private Integer tipoComunicacao;

	/**
	 * Campos compartilhados pelas telas de pesquisa e cadastro
	 */
	private List<SelectItem> listaTipoProcessoIntegracao;
	private String tipoProcessoIntegracao;

	private List<Object> listaProcesso;

	/**
	 * Campos tela cadastro
	 */
	private List<Object> listaTipoPermissao;
	private List<Object> listaModeloDocumento;
	private Object tipoPermissaoSelecionado;
	private Object tipoModeloDocumentoSelecionado;
	private Object modeloDocumentoSelecionado;
	private Object processoSelecionado;
	private List<Object> partesSelecionadas;
	private List<Object> pecasSelecionadas;
	private List<Object> pecasAndamentos;
	private String parteSelecionada = "";
	private boolean selecionarTodasPartes;
	private boolean selecionarTodasPecas;
	private boolean selecionarTodosAndamentos;
	private final String SISTEMA_PROCESSAMENTO = "PROCESSAMENTO";
	private final String SETOR_DOC_RESTRITOS = "codigo.setor.doc.restritos.menu";

	public BeanComunicacaoExterna() {
		this.listaSituacao = carregarTipoProcessoIntegracao();
		inicialisarTelaPesquisaECadastro(null);
	}

	/**
	 * Inicializa os dados da tela de pesquisa e cadastro.
	 */
	public void inicialisarTelaPesquisaECadastro(ActionEvent e) {
		idProcesso = null;
		idProcessoAndamentos = null;
		idProcessoPecas = null;
		idProcessoPartesIntimaveisCarregadas = null;
		idProcessoPartesNaoIntimaveisCarregadas = null;
		periodoInicio = null;
		periodoFim = null;
		listaResultadoPesquisa = null;
		orgaoIntimado = null;
		listaOrgaoIntimado = null;
		listaTipoComunicacao = null;
		listaTipoPermissao = null;
		listaTipoModeloDocumento = null;
		listaModeloDocumento = null;
		tipoPermissaoSelecionado = null;
		tipoModeloDocumentoSelecionado = null;
		modeloDocumentoSelecionado = null;
		processoSelecionado = null;
		listaParte = null;
		partesSelecionadas = null;
		listaPeca = null;
		pecasSelecionadas = null;
		listaAndamento = null;
		pecasAndamentos = null;
		listaPartesDTO = null;
		listaPecasDTO = null;
		listaAndamentoDto = null;
		selecionarTodasPartes = false;
		selecionarTodasPecas = false;
		selecionarTodosAndamentos = false;
		parteSelecionada = "";

		limparTelaPesquisa();
	}

	private List<SelectItem> carregaUsuariosExternos() throws ServiceException {
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();
		listaCombo.add(new SelectItem(null, null));
		List<ParteDTO> listaPartesProcessoEletronico;
		try {
			listaPartesProcessoEletronico = getParteLocalService()
					.listarPartesIntimacaoEletronica();
			Set<Long> partesAdicionadas = new HashSet<Long>();
			for (ParteDTO parte : listaPartesProcessoEletronico) {
				if (!partesAdicionadas.contains(parte.getSeqJurisdicionado())) {
					listaCombo.add(new SelectItem(parte.getSeqJurisdicionado(),
							parte.getNomeJurisdicionado()));
					partesAdicionadas.add(parte.getSeqJurisdicionado());
				}
			}
			return listaCombo;
		} catch (DaoException e) {
			return null;
		}
	}

	public void pesquisar(ActionEvent e) {
		try {

			String descricaoTipoComunicacao = montarDescricaoParaQueryIN(getDescricaoTipoComunicacao());

			String descricaoModelo = null;
			if (modeloComunicacaoEnum != null) {
				descricaoModelo = modeloComunicacaoEnum.getDescricaoModelo();
			}
			List<ComunicacaoExternaDTO> lista = getIntimacaoLocalService()
					.pesquisarComunicacaoExterna(orgaoIntimado, tipoSituacao,
							descricaoTipoComunicacao, descricaoModelo,
							periodoInicio, periodoFim, idProcesso,
							tipoIncidentePreferencia);
			carregarDescricaoProcessoIncidentesDaComunicacao(lista);
			setListaComunicacaoExterna(lista);
			if (this.getListaComunicacaoExterna() == null
					|| this.getListaComunicacaoExterna().isEmpty()) {
				reportarAviso("Nenhuma comunicação encontrada com os dados informados.");
			}
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
	}

	private void carregarDescricaoProcessoIncidentesDaComunicacao(
			List<ComunicacaoExternaDTO> listaComunicacao) {
		try {
			for (ComunicacaoExternaDTO comunicacaoDTO : listaComunicacao) {
				ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteService()
						.recuperarPorId(comunicacaoDTO.getIdProcessoIncidente());
				comunicacaoDTO
						.setProcesso(recuperarDescricaoTipoObjetoIncidente(objetoIncidente));
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public String recuperarDescricaoTipoObjetoIncidente(
			ObjetoIncidente<?> objetoIncidente) {
		String identificacaoIncidente = " ";

		if(objetoIncidente == null){
			return identificacaoIncidente;
		}
		
		if (objetoIncidente.getTipoObjetoIncidente() != null && objetoIncidente.getTipoObjetoIncidente().getCodigo() != null && 
				(TipoObjetoIncidente.PETICAO.getCodigo().equals(objetoIncidente.getTipoObjetoIncidente().getCodigo())
				|| TipoObjetoIncidente.PROTOCOLO.getCodigo().equals(objetoIncidente.getTipoObjetoIncidente().getCodigo())
				|| TipoObjetoIncidente.SUMULA.getCodigo().equals(objetoIncidente.getTipoObjetoIncidente().getCodigo()))) {
			identificacaoIncidente = objetoIncidente.getPrincipal().getIdentificacao()+ " " + objetoIncidente.getIdentificacao();
		} else {
			identificacaoIncidente = objetoIncidente.getIdentificacao();
		}

		return identificacaoIncidente;
	}

	private String montarDescricaoParaQueryIN(String descricaoTipoComunicacao) {
		StringBuilder resultado = new StringBuilder();

		if (descricaoTipoComunicacao == null) {
			for (String tipo : ModeloComunicacaoEnum
					.getDescricoesTipoComunicacao()) {
				resultado.append("'");
				resultado.append(tipo);
				resultado.append("'");
				resultado.append(",");
			}

			resultado.deleteCharAt(resultado.length() - 1);
		} else {
			resultado.append("'");
			resultado.append(descricaoTipoComunicacao);
			resultado.append("'");
		}

		return resultado.toString();
	}

	private List<SelectItem> carregarTipoProcessoIntegracao() {
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();

		for (TipoRecebimentoComunicacaoEnum integracaoEnum : TipoRecebimentoComunicacaoEnum
				.values()) {
			listaCombo.add(new SelectItem(integracaoEnum, integracaoEnum
					.getDescricao()));
		}

		listaCombo.add(new SelectItem(null, "Todos"));

		return listaCombo;
	}

	public String gerarIntimacaoEletronica() {
		inicialisarTelaPesquisaECadastro(null);
		return "gerarIntimacaoEletronica";
	}

	public void novaPesquisa(ActionEvent e) {
		limparTelaPesquisa();
	}

	public void parteSelecionada(ActionEvent event) {
		parteSelecionada = "Selecionada";
	}

	public void enviarComunicacao(ActionEvent e) {
		String username = getUser().getUsername();
		Setor setor = getSetorUsuarioAutenticado();
		Date dataIntimacao = new Date();
		listaPeca = retornarPecaProcessoEletronicoMarcados();
		listaAndamento = retornarAndamentosMarcados();
		listaParte = retornarPartesMarcadas();
		Set<Long> objIncidente = new HashSet<Long>();
		objIncidente.add(idProcesso);
		List<Comunicacao> listaComunicacao = new ArrayList<Comunicacao>();
		for (Parte parte : listaParte) {
			try {
				listaComunicacao.add(getIntimacaoLocalService().criarIntimacao(
						username, setor, dataIntimacao, modeloComunicacaoEnum,
						objIncidente, parte.getId(), listaPeca, listaAndamento,
						TipoFaseComunicacao.ENVIADO, "COMUNICAÇÃO ELETRÔNICA", null));
			} catch (TipoModeloComunicacaoEnumInvalidoException e1) {
				reportarErro(e1.getMessage());
			} catch (ProcessoNaoEletronicoException e1) {
				reportarErro(e1.getMessage());
			} catch (ParteSemAceiteInitmacaoEletronicaException e1) {
				reportarErro(e1.getMessage());
			} catch (PessoaSemUsuarioException e1) {
				reportarErro("Parte " + parte.getNomeJurisdicionado()
						+ " sem usuário.");
			} catch (ParteNaoPertencenteProcessoException e1) {
				reportarErro(e1.getMessage());
			} catch (PecaNaoPertencenteProcessoException e1) {
				reportarErro(e1.getMessage());
			} catch (AndamentoNaoPertencenteProcessoException e1) {
				reportarErro(e1.getMessage());
			} catch (ServiceException e1) {
				reportarErro(e1.getMessage());
			}
		}

		inicialisarTelaPesquisaECadastro(null);
		reportarInformacao("Comunicação de intimação criada com sucesso.");
	}

	@SuppressWarnings("rawtypes")
	public List pesquisarIncidentesPrincipal(Object value) {
		String siglaNumero = null;
		List<ObjetoIncidente<?>> incidentes = null;
		List<ProcessoObjetoIncidenteDTO> processoIncidentesDTO = null;
		if (value != null) {
			siglaNumero = value.toString();
		}
		if (StringUtils.isNotVazia(siglaNumero)) {
			try {
				String sigla = ProcessoParser.getSigla(siglaNumero);
				Long lNumero = ProcessoParser.getNumero(siglaNumero);
				incidentes = carregarProcesso(sigla, lNumero, siglaNumero);
				processoIncidentesDTO = carregarObjetosIncidentesDoProcesso(incidentes);
			} catch (NumberFormatException e) {
				reportarErro("Número de processo inválido: " + siglaNumero);
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar os incidentes do processo: "
						+ siglaNumero);
			}
		}
		return processoIncidentesDTO;
	}

	private List<ProcessoObjetoIncidenteDTO> carregarObjetosIncidentesDoProcesso(
			List<ObjetoIncidente<?>> objetosIncidentes) {
		List<ProcessoObjetoIncidenteDTO> processoIncidentesDTO = null;
		if (objetosIncidentes != null && !objetosIncidentes.isEmpty()) {
			processoIncidentesDTO = new ArrayList<ProcessoObjetoIncidenteDTO>();
			for (ObjetoIncidente<?> objetoIncidente : objetosIncidentes) {
				ProcessoObjetoIncidenteDTO procObjetoIncidenteDTO = new ProcessoObjetoIncidenteDTO();
				procObjetoIncidenteDTO.setId(objetoIncidente.getId());
				procObjetoIncidenteDTO.setIdProcessoPrincipal(objetoIncidente
						.getPrincipal().getId());
				procObjetoIncidenteDTO.setTipoObjetoIncidente(objetoIncidente
						.getTipoObjetoIncidente());
				procObjetoIncidenteDTO.setTipoMeio(objetoIncidente
						.getPrincipal().getTipoMeio());
				procObjetoIncidenteDTO
						.setIdentificacao(recuperarDescricaoTipoObjetoIncidente(objetoIncidente));

				processoIncidentesDTO.add(procObjetoIncidenteDTO);
			}
			return ordenarIncidentesProcessoPorTipoIncidente(processoIncidentesDTO);
		}
		return processoIncidentesDTO;
	}

	private List<ProcessoObjetoIncidenteDTO> ordenarIncidentesProcessoPorTipoIncidente(
			List<ProcessoObjetoIncidenteDTO> processoIncidentesDTO) {

		List<ProcessoObjetoIncidenteDTO> processosPorOrdemTipoIncidente = new LinkedList<ProcessoObjetoIncidenteDTO>();

		TipoObjetoIncidente ordemClassificacaoIipoIncidentes[] = {
				TipoObjetoIncidente.PROCESSO, TipoObjetoIncidente.RECURSO,
				TipoObjetoIncidente.INCIDENTE_JULGAMENTO,
				TipoObjetoIncidente.PETICAO,
				TipoObjetoIncidente.PETICAO_ELETRONICA,
				TipoObjetoIncidente.PROTOCOLO, TipoObjetoIncidente.SUMULA };

		for (TipoObjetoIncidente tipoIncidente : ordemClassificacaoIipoIncidentes) {
			ProcessoObjetoIncidenteDTO processoTipoIncidente = new ProcessoObjetoIncidenteDTO();
			processoTipoIncidente.setTipoObjetoIncidente(tipoIncidente);
			if (processoIncidentesDTO.contains(processoTipoIncidente)) {
				for (ProcessoObjetoIncidenteDTO incidente : processoIncidentesDTO) {
					if (incidente.getTipoObjetoIncidente().getCodigo()
							.equals(tipoIncidente.getCodigo())) {
						processosPorOrdemTipoIncidente.add(incidente);
					}
				}
			}
		}

		return processosPorOrdemTipoIncidente;
	}

	private List<ObjetoIncidente<?>> carregarProcesso(String sigla,
			Long lNumero, String siglaNumero) throws ServiceException {
		List<ObjetoIncidente<?>> incidentes = null;
		if (StringUtils.isNotVazia(sigla) && lNumero != null) {
			String siglaConvertida = converterClasse(sigla, classes);
			if (siglaConvertida == null) {
				reportarAviso("Classe processual não encontrada: " + sigla);
			} else {
				incidentes = new ArrayList<ObjetoIncidente<?>>();

				List<Processo> processos = getProcessoService()
						.pesquisarProcesso(siglaNumero.toUpperCase());
				processos.removeAll(Collections.singletonList(null));

				for (Processo processo : processos) {
					if (processo.isEletronico()) {
						incidentes = getProcessoLocalService()
								.recuperarIncidentesDoProcessoEletronico(
										processo.getId());
					}
				}

				setNumeroProcessoPesqPrincipal(lNumero);
				setSiglaClassePesqPrincipal(siglaConvertida);

			}
		}
		return incidentes;
	}

	public SelectItem[] getTiposComunicacao() {
		SelectItem[] items;
		int i = 0;

		if (descricaoTipoComunicacao != null) {
			items = new SelectItem[ModeloComunicacaoEnum
					.getModeloComunicacaoEletronicaEnum(
							descricaoTipoComunicacao).size()];
			for (ModeloComunicacaoEnum t : ModeloComunicacaoEnum
					.getModeloComunicacaoEletronicaEnum(descricaoTipoComunicacao)) {

				items[i++] = new SelectItem(t, t.getDescricaoModelo());
			}
		} else {
			List<ModeloComunicacaoEnum> lista = new ArrayList<ModeloComunicacaoEnum>();
			for (ModeloComunicacaoEnum tp : ModeloComunicacaoEnum
					.getModeloComunicacaoEletronicaEnum()) {
				lista.add(tp);
			}
			items = new SelectItem[lista.size()];
			for (ModeloComunicacaoEnum t : ModeloComunicacaoEnum
					.getModeloComunicacaoEletronicaEnum()) {
				items[i++] = new SelectItem(t, t.getDescricaoModelo());
			}
		}
		return items;
	}

	public SelectItem[] getTiposComunicacoesGeracao() {
		List<ModeloComunicacaoEnum> modelos = Arrays.asList(ModeloComunicacaoEnum.ATO_ORDINATORIO, ModeloComunicacaoEnum.CITACAO, ModeloComunicacaoEnum.INTIMACAO_DESPACHO_DECISAO_ACORDAO, ModeloComunicacaoEnum.NOTIFICACAO_DE_PAUTA);
		
		SelectItem[] items = new SelectItem[modelos.size()];
		
		for (int i=0; i<modelos.size(); i++)
			items[i] = new SelectItem(modelos.get(i), modelos.get(i).getDescricaoModelo());

		return items;
	}

	public SelectItem[] getDescricoesTipoComunicacao() {
		SelectItem[] items = new SelectItem[ModeloComunicacaoEnum
				.getDescricoesTipoComunicacao().size()];
		int i = 0;
		for (String descricao : ModeloComunicacaoEnum
				.getDescricoesTipoComunicacao()) {
			items[i++] = new SelectItem(descricao, descricao);
		}
		return items;
	}

	public void limparTelaPesquisa() {
		setRetornoProcesso(null);
		setProcesso(null);
		setIdProcesso(null);
		setPeriodoInicio(null);
		setPeriodoFim(null);
		setOrgaoIntimado(null);
		setNumeroProcessoPesqPrincipal(null);
		setSiglaClassePesqPrincipal(null);
		setClasses(null);
		setObjetoIncidente(null);
		setListaAndamento(null);
		setListaAndamentoDto(null);
		setListaPeca(null);
		setListaPecasDTO(null);
		setListaParte(null);
		setListaPartesDTO(null);
		setListaPartesDTONaoIntimaveis(null);
		setListaComunicacaoExterna(null);
		setModeloComunicacaoEnum(null);
		setListaAndamentoDto(null);
		setListaPartesDTO(null);
		setListaPecasDTO(null);
		setTipoSituacao(null);
		setSelecionarTodasPartes(false);
		setSelecionarTodasPecas(false);
		setSelecionarTodosAndamentos(false);
		setParteSelecionada("");
		setTipoIncidentePreferencia(null);
		setDescricaoTipoComunicacao(null);
	}

	private List<AndamentoProcesso> retornarAndamentosMarcados() {
		List<AndamentoProcesso> lista = new ArrayList<AndamentoProcesso>();
		for (AndamentosDTO dto : receberAdamentosDtoMarcado()) {
			lista.add(preencheAndamentoProcesso(dto));
		}
		return lista;
	}

	private List<PecaProcessoEletronico> retornarPecaProcessoEletronicoMarcados() {
		List<PecaProcessoEletronico> lista = new ArrayList<PecaProcessoEletronico>();
		for (PecaDTO dto : receberPecasDtoMarcado()) {
			lista.add(preenchePecaProcessoEletronico(dto));
		}
		return lista;
	}

	private List<Parte> retornarPartesMarcadas() {
		List<Parte> lista = new ArrayList<Parte>();
		for (ParteDTO dto : receberPartesDtoMarcada()) {
			lista.add(preencheParte(dto));
		}
		return lista;
	}

	private List<PecaDTO> receberPecasDtoMarcado() {
		List<PecaDTO> lista = new ArrayList<PecaDTO>();
		PecaDTO pecasDto;
		@SuppressWarnings("rawtypes")
		Iterator iterator = listaPecasDTO.iterator();
		while (iterator.hasNext()) {
			pecasDto = (PecaDTO) iterator.next();
			if (pecasDto.isChecked()) {
				lista.add(pecasDto);
			}
		}
		return lista;
	}

	private List<AndamentosDTO> receberAdamentosDtoMarcado() {
		List<AndamentosDTO> lista = new ArrayList<AndamentosDTO>();
		AndamentosDTO andamentosDTO;
		@SuppressWarnings("rawtypes")
		Iterator iterator = listaAndamentoDto.iterator();
		while (iterator.hasNext()) {
			andamentosDTO = (AndamentosDTO) iterator.next();
			if (andamentosDTO.isChecked()) {
				lista.add(andamentosDTO);
			}
		}
		return lista;
	}

	private List<ParteDTO> receberPartesDtoMarcada() {
		List<ParteDTO> lista = new ArrayList<ParteDTO>();
		ParteDTO partesDTO;
		@SuppressWarnings("rawtypes")
		Iterator iterator = listaPartesDTO.iterator();
		while (iterator.hasNext()) {
			partesDTO = (ParteDTO) iterator.next();
			if (partesDTO.isChecked()) {
				lista.add(partesDTO);
			}
		}
		return lista;
	}

	private List<AndamentosDTO> converterAndamentoDto(Long idProcessoIncidente) {
		statusBotao = true;
		List<AndamentosDTO> lista = listaAndamentoDto;
		try {
			if (idProcessoIncidente != null
					&& !idProcessoIncidente.equals(idProcessoAndamentos)) {
				lista = new ArrayList<AndamentosDTO>();

				listaAndamento = getAndamentoProcessoComunicacaoLocalServicee()
						.pesquisarAndamentosProcessoIncidente(
								idProcessoIncidente);

				for (AndamentoProcesso andamentos : listaAndamento) {
					lista.add(preecheDTOAndamento(andamentos));
				}

				idProcessoAndamentos = idProcessoIncidente;
			}
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		return lista;
	}

	private List<PecaDTO> converterPecasDto(Long idProcessoIncidente) {
		List<PecaDTO> lista = listaPecasDTO;
		if (idProcessoIncidente != null
				&& !idProcessoIncidente.equals(idProcessoPecas)) {
			lista = new ArrayList<PecaDTO>();
			try {

				listaPeca = getPecaProcessoEletronicoLocalService()
						.pesquisarPecasPorProcessoIncidente(
								idProcessoIncidente, false);

				for (PecaProcessoEletronico pecaProcessoEletronico : listaPeca) {
					lista.add(preencherDTOPecas(pecaProcessoEletronico));
				}

				idProcessoPecas = idProcessoIncidente;

			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return ordenarListaPorDataDecrescente(lista);
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

	private List<ParteDTO> converterPartesIntimaveisDto(Long idProcesso) {
		List<ParteDTO> listaParteDTO = listaPartesDTO;
		if (idProcesso != null
				&& !idProcesso.equals(idProcessoPartesIntimaveisCarregadas)) {
			try {
				listaParteDTO = getParteLocalService().listarPartes(true,
						idProcesso);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			idProcessoPartesIntimaveisCarregadas = idProcesso;
		}
		return listaParteDTO;

	}

	private List<ParteDTO> converterPartesNaoIntimaveisDto(Long idProcesso) {
		List<ParteDTO> listaParteDTO = listaPartesDTONaoIntimaveis;
		if (idProcesso != null
				&& !idProcesso.equals(idProcessoPartesNaoIntimaveisCarregadas)) {
			try {
				listaParteDTO = getParteLocalService().listarPartes(false,
						idProcesso);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			idProcessoPartesNaoIntimaveisCarregadas = idProcesso;
		}
		return listaParteDTO;
	}

	private PecaDTO preencherDTOPecas(PecaProcessoEletronico pe) {
		PecaDTO dto = new PecaDTO();
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
			ArquivoProcessoEletronico arquivoProcessoEletronico = dto
					.getDocumentos().get(0);
			String url = montaUrlDownload(arquivoProcessoEletronico);
			dto.setUrlDownloadPeca(url);
		}
		return dto;
	}

	private PecaProcessoEletronico preenchePecaProcessoEletronico(PecaDTO dto) {
		PecaProcessoEletronico pe = new PecaProcessoEletronico();

		pe.setAndamentoProtocolo(dto.getAndamentoProtocolo());
		pe.setDescricaoPeca(dto.getDescricaoPeca());
		pe.setDocumentos(dto.getDocumentos());
		pe.setDocumentosEletronicos(dto.getDocumentosEletronicos());
		pe.setId(dto.getId());
		pe.setLembretes(dto.getLembretes());
		pe.setNumeroOrdemPeca(dto.getNumeroOrdemPeca());
		pe.setNumeroPagFim(dto.getNumeroPagFim());
		pe.setNumeroPagInicio(dto.getNumeroPagInicio());
		pe.setObjetoIncidente(dto.getObjetoIncidente());
		pe.setSetor(dto.getSetor());
		pe.setTipoOrigemPeca(dto.getTipoOrigemPeca());
		pe.setTipoPecaProcesso(dto.getTipoPecaProcesso());
		pe.setTipoSituacaoPeca(dto.getTipoSituacaoPeca());
		pe.setDataInclusao(dto.getDataInclusao());
		pe.setUsuarioInclusao(dto.getUsuarioInclusao());
		pe.setDataAlteracao(dto.getDataAlteracao());
		pe.setUsuarioAlteracao(dto.getUsuarioAlteracao());

		return pe;
	}

	private Parte preencheParte(ParteDTO dto) {
		Parte parte = new Parte();

		parte.setCategoria(dto.getCategoria());
		parte.setDscTipoImpressao(dto.getDscTipoImpressao());
		parte.setId(dto.getId());
		parte.setLogin(dto.getLogin());
		parte.setNomeJurisdicionado(dto.getNomeJurisdicionado());
		parte.setNumeroOrdem(dto.getNumeroOrdem());
		parte.setObjetoIncidente(dto.getObjetoIncidente());
		parte.setSeqJurisdicionado(dto.getSeqJurisdicionado());

		return parte;
	}

	private AndamentoProcesso preencheAndamentoProcesso(AndamentosDTO dto) {
		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();

		andamentoProcesso.setCodigoAndamento(dto.getCodigoAndamento());
		andamentoProcesso.setCodigoUsuario(dto.getCodigoUsuario());
		andamentoProcesso.setDataAlteracao(dto.getDataAlteracao());
		andamentoProcesso.setDataAndamento(dto.getDataAlteracao());
		andamentoProcesso.setDataHoraSistema(dto.getDataHoraSistema());
		andamentoProcesso.setDataInclusao(dto.getDataInclusao());
		andamentoProcesso.setDescricaoObservacaoAndamento(dto
				.getDescricaoObservacaoAndamento());
		andamentoProcesso.setDescricaoObservacaoInterna(dto
				.getDescricaoObservacaoInterna());
		andamentoProcesso.setId(dto.getId());
		andamentoProcesso.setLancamentoIndevido(dto.getLancamentoIndevido());
		andamentoProcesso.setListaTextoAndamentoProcessos(dto
				.getListaTextoAndamentoProcessos());
		andamentoProcesso.setNumeroSequencia(dto.getNumeroSequencia());
		andamentoProcesso.setNumeroSequenciaErrado(dto
				.getNumeroSequenciaErrado());
		andamentoProcesso.setNumProcesso(dto.getNumProcesso());
		andamentoProcesso.setObjetoIncidente(dto.getObjetoIncidente());
		andamentoProcesso.setOrigemAndamentoDecisao(dto
				.getOrigemAndamentoDecisao());
		andamentoProcesso.setPresidenteInterino(dto.getPresidenteInterino());
		andamentoProcesso.setRecurso(dto.getRecurso());
		andamentoProcesso.setSetor(dto.getSetor());
		andamentoProcesso.setSigClasseProces(dto.getSigClasseProces());
		andamentoProcesso.setTipoAndamento(dto.getTipoAndamento());
		andamentoProcesso.setTipoDevolucao(dto.getTipoDevolucao());
		andamentoProcesso.setUltimoAndamento(dto.getUltimoAndamento());
		andamentoProcesso.setUsuarioAlteracao(dto.getUsuarioAlteracao());
		andamentoProcesso.setUsuarioInclusao(dto.getUsuarioInclusao());

		return andamentoProcesso;
	}

	private AndamentosDTO preecheDTOAndamento(AndamentoProcesso a) {
		AndamentosDTO dto = new AndamentosDTO();
		dto.setChecked(false);
		dto.setCodigoAndamento(a.getCodigoAndamento());
		dto.setCodigoUsuario(a.getCodigoUsuario());
		dto.setDataAlteracao(a.getDataAlteracao());
		dto.setDataAndamento(a.getDataAndamento());
		dto.setDataHoraSistema(a.getDataHoraSistema());
		dto.setDataInclusao(a.getDataInclusao());
		dto.setDescricaoObservacaoAndamento(a.getDescricaoObservacaoAndamento());
		dto.setDescricaoObservacaoInterna(a.getDescricaoObservacaoInterna());
		dto.setId(a.getId());
		dto.setLancamentoIndevido(a.getLancamentoIndevido());
		dto.setListaTextoAndamentoProcessos(a.getListaTextoAndamentoProcessos());
		dto.setNumeroSequencia(a.getNumeroSequencia());
		dto.setNumeroSequenciaErrado(a.getNumeroSequenciaErrado());
		dto.setNumProcesso(a.getNumProcesso());
		dto.setObjetoIncidente(a.getObjetoIncidente());
		dto.setOrigemAndamentoDecisao(a.getOrigemAndamentoDecisao());
		dto.setPresidenteInterino(a.getPresidenteInterino());
		dto.setRecurso(a.getRecurso());
		dto.setSetor(a.getSetor());
		dto.setSigClasseProces(a.getSigClasseProces());
		dto.setTipoAndamento(a.getTipoAndamento());
		dto.setTipoDevolucao(a.getTipoDevolucao());
		dto.setUltimoAndamento(a.getUltimoAndamento());
		dto.setUsuarioAlteracao(a.getUsuarioAlteracao());
		dto.setUsuarioInclusao(a.getUsuarioInclusao());

		return dto;
	}

	public Integer getRowIndexDataTableResultado() {
		return rowIndexDataTableResultado;
	}

	public void setRowIndexDataTableResultado(Integer rowIndexDataTableResultado) {
		this.rowIndexDataTableResultado = rowIndexDataTableResultado;
	}

	public Long getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}

	public Long getIdComunicacao() {
		return idComunicacao;
	}

	public void setIdComunicacao(Long idComunicacao) {
		this.idComunicacao = idComunicacao;
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

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public String getRetornoProcesso() {
		return retornoProcesso;
	}

	public void setRetornoProcesso(String retornoProcesso) {
		this.retornoProcesso = retornoProcesso;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public List<AndamentoProcesso> getListaAndamento() {
		return listaAndamento;
	}

	public void setListaAndamento(List<AndamentoProcesso> listaAndamento) {
		this.listaAndamento = listaAndamento;
	}

	public List<AndamentosDTO> getListaAndamentoDto() {
		listaAndamentoDto = converterAndamentoDto(getIdProcesso());
		return listaAndamentoDto;
	}

	public void setListaAndamentoDto(List<AndamentosDTO> listaAndamentoDto) {
		this.listaAndamentoDto = listaAndamentoDto;
	}

	public List<PecaProcessoEletronico> getListaPeca() {
		return listaPeca;
	}

	public void setListaPeca(List<PecaProcessoEletronico> listaPeca) {
		this.listaPeca = listaPeca;
	}

	public List<PecaDTO> getListaPecasDTO() {
		listaPecasDTO = converterPecasDto(getIdProcesso());
		return listaPecasDTO;
	}

	public void setListaPecasDTO(List<PecaDTO> listaPecasDTO) {
		this.listaPecasDTO = listaPecasDTO;
	}

	public List<Parte> getListaParte() {
		return listaParte;
	}

	public void setListaParte(List<Parte> listaParte) {
		this.listaParte = listaParte;
	}

	public List<ParteDTO> getListaPartesDTO() {
		listaPartesDTO = converterPartesIntimaveisDto(getIdProcesso());
		return listaPartesDTO;
	}

	public void setListaPartesDTO(List<ParteDTO> listaPartesDTO) {
		this.listaPartesDTO = listaPartesDTO;
	}

	public List<ParteDTO> getListaPartesDTONaoIntimaveis() {
		listaPartesDTONaoIntimaveis = converterPartesNaoIntimaveisDto(getIdProcesso());
		return listaPartesDTONaoIntimaveis;
	}

	public void setListaPartesDTONaoIntimaveis(
			List<ParteDTO> listaPartesDTONaoIntimaveis) {
		this.listaPartesDTONaoIntimaveis = listaPartesDTONaoIntimaveis;
	}

	public ObjetoIncidente<ObjetoIncidente<?>> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(
			ObjetoIncidente<ObjetoIncidente<?>> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public List<SelectItem> getListaOrgaoIntimado() throws ServiceException {
		if (listaOrgaoIntimado == null) {
			listaOrgaoIntimado = carregaUsuariosExternos();
		}
		return listaOrgaoIntimado;
	}

	public void setListaOrgaoIntimado(List<SelectItem> listaOrgaoIntimado) {
		this.listaOrgaoIntimado = listaOrgaoIntimado;
	}

	public List<SelectItem> getListaSituacao() {
		return listaSituacao;
	}

	public void setListaSituacao(List<SelectItem> listaSituacao) {
		this.listaSituacao = listaSituacao;
	}

	public String getOrgaoIntimado() {
		return orgaoIntimado;
	}

	public void setOrgaoIntimado(String orgaoIntimado) {
		this.orgaoIntimado = orgaoIntimado;
	}

	public TipoRecebimentoComunicacaoEnum getTipoSituacao() {
		return tipoSituacao;
	}

	public void setTipoSituacao(TipoRecebimentoComunicacaoEnum tipoSituacao) {
		this.tipoSituacao = tipoSituacao;
	}

	public ModeloComunicacaoEnum getModeloComunicacaoEnum() {
		return modeloComunicacaoEnum;
	}

	public void setModeloComunicacaoEnum(
			ModeloComunicacaoEnum modeloComunicacaoEnum) {
		this.modeloComunicacaoEnum = modeloComunicacaoEnum;
	}

	public Integer getTipoComunicacao() {
		return tipoComunicacao;
	}

	public void setTipoComunicacao(Integer tipoComunicacao) {
		this.tipoComunicacao = tipoComunicacao;
	}

	public String getTipoProcessoIntegracao() {
		return tipoProcessoIntegracao;
	}

	public void setTipoProcessoIntegracao(String tipoProcessoIntegracao) {
		this.tipoProcessoIntegracao = tipoProcessoIntegracao;
	}

	public List<SelectItem> getListaTipoProcessoIntegracao() {
		return listaTipoProcessoIntegracao;
	}

	public void setListaTipoProcessoIntegracao(
			List<SelectItem> listaTipoProcessoIntegracao) {
		this.listaTipoProcessoIntegracao = listaTipoProcessoIntegracao;
	}

	public Integer getUsuarioExterno() {
		return usuarioExterno;
	}

	public void setUsuarioExterno(Integer usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFim() {
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		this.periodoFim = periodoFim;
	}

	public List<Object> getListaResultadoPesquisa() {
		return listaResultadoPesquisa;
	}

	public void setListaResultadoPesquisa(List<Object> listaResultadoPesquisa) {
		this.listaResultadoPesquisa = listaResultadoPesquisa;
	}

	public List<SelectItem> getListaTipoComunicacao() {
		return listaTipoComunicacao;
	}

	public void setListaTipoComunicacao(List<SelectItem> listaTipoComunicacao) {
		this.listaTipoComunicacao = listaTipoComunicacao;
	}

	public List<Object> getListaProcesso() {
		return listaProcesso;
	}

	public void setListaProcesso(List<Object> listaProcesso) {
		this.listaProcesso = listaProcesso;
	}

	public List<Object> getListaTipoPermissao() {
		return listaTipoPermissao;
	}

	public void setListaTipoPermissao(List<Object> listaTipoPermissao) {
		this.listaTipoPermissao = listaTipoPermissao;
	}

	public List<SelectItem> getListaTipoModeloDocumento() {
		return listaTipoModeloDocumento;
	}

	public void setListaTipoModeloDocumento(
			List<SelectItem> listaTipoModeloDocumento) {
		this.listaTipoModeloDocumento = listaTipoModeloDocumento;
	}

	public List<Object> getListaModeloDocumento() {
		return listaModeloDocumento;
	}

	public void setListaModeloDocumento(List<Object> listaModeloDocumento) {
		this.listaModeloDocumento = listaModeloDocumento;
	}

	public Object getTipoPermissaoSelecionado() {
		return tipoPermissaoSelecionado;
	}

	public void setTipoPermissaoSelecionado(Object tipoPermissaoSelecionado) {
		this.tipoPermissaoSelecionado = tipoPermissaoSelecionado;
	}

	public Object getTipoModeloDocumentoSelecionado() {
		return tipoModeloDocumentoSelecionado;
	}

	public void setTipoModeloDocumentoSelecionado(
			Object tipoModeloDocumentoSelecionado) {
		this.tipoModeloDocumentoSelecionado = tipoModeloDocumentoSelecionado;
	}

	public Object getModeloDocumentoSelecionado() {
		return modeloDocumentoSelecionado;
	}

	public void setModeloDocumentoSelecionado(Object modeloDocumentoSelecionado) {
		this.modeloDocumentoSelecionado = modeloDocumentoSelecionado;
	}

	public Object getProcessoSelecionado() {
		return processoSelecionado;
	}

	public void setProcessoSelecionado(Object processoSelecionado) {
		this.processoSelecionado = processoSelecionado;
	}

	public List<Object> getPartesSelecionadas() {
		return partesSelecionadas;
	}

	public void setPartesSelecionadas(List<Object> partesSelecionadas) {
		this.partesSelecionadas = partesSelecionadas;
	}

	public List<Object> getPecasSelecionadas() {
		return pecasSelecionadas;
	}

	public void setPecasSelecionadas(List<Object> pecasSelecionadas) {
		this.pecasSelecionadas = pecasSelecionadas;
	}

	public List<Object> getPecasAndamentos() {
		return pecasAndamentos;
	}

	public void setPecasAndamentos(List<Object> pecasAndamentos) {
		this.pecasAndamentos = pecasAndamentos;
	}

	/**
	 * @return the listaUsuariosExternos
	 */
	public List<SelectItem> getListaUsuariosExternos() {
		return listaUsuariosExternos;
	}

	/**
	 * @param listaUsuariosExternos
	 *            the listaUsuariosExternos to set
	 */
	public void setListaUsuariosExternos(List<SelectItem> listaUsuariosExternos) {
		this.listaUsuariosExternos = listaUsuariosExternos;
	}

	public List<ComunicacaoExternaDTO> getListaComunicacaoExterna() {
		return listaComunicacaoExterna;
	}

	public void setListaComunicacaoExterna(
			List<ComunicacaoExternaDTO> listaComunicacaoExterna) {
		this.listaComunicacaoExterna = listaComunicacaoExterna;
	}

	public boolean isStatusBotao() {
		return statusBotao;
	}

	public void setStatusBotao(boolean statusBotao) {
		this.statusBotao = statusBotao;
	}

	public String getParteSelecionada() {
		return parteSelecionada;
	}

	public void setParteSelecionada(String parteSelecionada) {
		this.parteSelecionada = parteSelecionada;
	}

	public boolean isSelecionarTodasPartes() {
		return selecionarTodasPartes;
	}

	public void setSelecionarTodasPartes(boolean selecionarTodasPartes) {
		this.selecionarTodasPartes = selecionarTodasPartes;
	}

	public boolean isSelecionarTodasPecas() {
		return selecionarTodasPecas;
	}

	public void setSelecionarTodasPecas(boolean selecionarTodasPecas) {
		this.selecionarTodasPecas = selecionarTodasPecas;
	}

	public boolean isSelecionarTodosAndamentos() {
		return selecionarTodosAndamentos;
	}

	public void setSelecionarTodosAndamentos(boolean selecionarTodosAndamentos) {
		this.selecionarTodosAndamentos = selecionarTodosAndamentos;
	}

	public String getDescricaoTipoComunicacao() {
		return descricaoTipoComunicacao;
	}

	public void setDescricaoTipoComunicacao(String descricaoTipoComunicacao) {
		this.descricaoTipoComunicacao = descricaoTipoComunicacao;
	}

	public HtmlDataTable getDataTableListaComunicacao() {
		return dataTableListaComunicacao;
	}

	public void setDataTableListaComunicacao(
			HtmlDataTable dataTableListaComunicacao) {
		this.dataTableListaComunicacao = dataTableListaComunicacao;
	}

	public void varificarSeParteFoiSelecionada(ActionEvent e) {
		this.setParteSelecionada("");
		for (ParteDTO dto : listaPartesDTO) {
			if (dto.isChecked()) {
				this.setParteSelecionada("Selecionada");
				break;
			}
		}
	}

	public void marcarDesmarcarPartes(ActionEvent e) {
		this.setParteSelecionada("");

		if (isSelecionarTodasPartes()) {
			this.setParteSelecionada("Selecionada");
		}

		for (ParteDTO dto : listaPartesDTO) {
			dto.setChecked(isSelecionarTodasPartes());
		}
	}

	public void marcarDesmarcarPecas(ActionEvent e) {
		for (PecaDTO dto : listaPecasDTO) {
			dto.setChecked(isSelecionarTodasPecas());
		}
	}

	public void marcarDesmarcarAndamentos(ActionEvent e) {
		for (AndamentosDTO dto : listaAndamentoDto) {
			dto.setChecked(isSelecionarTodosAndamentos());
		}
	}

	@SuppressWarnings("deprecation")
	public void cancelarIntimacao(ActionEvent e) {
		Comunicacao comunicacao = null;
		
		try {
			comunicacao = getComunicacaoService().recuperarPorId(idComunicacao);

			if (isComunicacaoCancelada(comunicacao)) {
				reportarErro("Essa comunicação já foi cancelada!");
			} else {	
				
				getComunicacaoServiceLocal().inserirFaseComunicacao(comunicacao);
				listaComunicacaoExterna.remove(rowIndexDataTableResultado.intValue());
				
				reportarAviso("Comunicação cancelada com sucesso.");
				
				cancelarAndamentoDaIntimacao(comunicacao);				
				cancelarDeslocamentoProcessoDaComunicacao(comunicacao);
			}
		} catch (ServiceException se) {
			reportarErro("Erro ao cancelar comunicação para intimação id {"
					+ comunicacao.getId() + "}.", se, null);
		} catch (RegraDeNegocioException rne) {
			reportarErro("Erro ao cancelar comunicação para intimação id {"
					+ comunicacao.getId() + "}.", rne, null);
		}

	}
	
	private void cancelarAndamentoDaIntimacao(Comunicacao comunicacao) throws ServiceException {
		Long idCodigoAndamento = null;
    	
    	if(comunicacao.getModeloComunicacao().getDscModelo().equals(ModeloComunicacaoEnum.CITACAO.getDescricaoModelo())){
    		idCodigoAndamento = CODIGO_CITACAO_ELETRONICA_DISPONIBILIZADA;
    	}else
    		idCodigoAndamento = CODIGO_INTIMACAO_ELETRONICA_DISPONIBILIZADA;	
    	
		AndamentoProcessoComunicacao andamentoProcessoComunicacao = getAndamentoProcessoComunicacaoLocalServicee().
				recuperarAndamentoProcessoGeradoPelaComunicacao(comunicacao.getId(), idCodigoAndamento);
		
		if(andamentoProcessoComunicacao != null){
			Processo processoPrincipal = getProcessoService().recuperarPorId(
				andamentoProcessoComunicacao.getAndamentoProcesso().getObjetoIncidente().getPrincipal().getId());
		
			getAndamentoProcessoService().salvarAndamentoIndevido(processoPrincipal, 
					andamentoProcessoComunicacao.getAndamentoProcesso(), getSetorUsuarioAutenticado(), 
					getUser().getUsername(), null, "Cancelamento da intimação/citação eletrônica.", null, 
					andamentoProcessoComunicacao.getAndamentoProcesso().getObjetoIncidente());
			
			reportarAviso("Andamento gerado da comunicação cancelado com sucesso.");
		}		
	}

	private void cancelarDeslocamentoProcessoDaComunicacao(Comunicacao comunicacao) {		
		DeslocaProcesso deslocaProcesso = null;	
		
		try {			
			Processo processo = getProcessoService().recuperarPorId(idProcesso);
			Long codigoOrigem = getComunicacaoServiceLocal().recuperarCodigoOrigemDestinatario(comunicacao.getId());
			
			deslocaProcesso = getDeslocaProcessoService().recuperarUltimoDeslocamentoProcesso(processo.getPrincipal());
			
			if(deslocaProcesso != null){			
				if(deslocaProcesso.getAndamentoProcesso().getCodigoAndamento().equals(
						Andamentos.BAIXA_DEFINITIVA_DOS_AUTOS.getId()) &&					
						deslocaProcesso.getCodigoOrgaoDestino().equals(codigoOrigem) &&
						deslocaProcesso.getDataRecebimento() == null ){
					
					getDeslocaProcessoService().excluir(deslocaProcesso);
					getGuiaService().excluir(deslocaProcesso.getGuia());
					
					reportarAviso("Deslocamento do processo da comunicação cancelado com sucesso.");
				}			
			}
		} catch (ServiceException e) {		
			e.printStackTrace();
		}		
	}
	
	private Boolean isComunicacaoCancelada(Comunicacao comunicacao) {
		boolean resultado = false;
		for (FaseComunicacao c2 : comunicacao.getFases()) {
			if (c2.getTipoFase().equals(TipoFaseComunicacao.EXCLUIDO)) {
				resultado = true;
				break;
			}
		}
		return resultado;
	}

	public Long getTipoIncidentePreferencia() {
		return tipoIncidentePreferencia;
	}

	public void setTipoIncidentePreferencia(Long tipoIncidentePreferencia) {
		this.tipoIncidentePreferencia = tipoIncidentePreferencia;
	}

	public List<SelectItem> getIncidentePreferencia() {

		List<TipoIncidentePreferencia> preferencias = null;
		try {
			preferencias = getIntimacaoLocalService().buscaTodasPreferencias();
		} catch (DaoException e) {
			reportarErro("Erro ao buscar preferências", e, null);
		}

		List<SelectItem> items = new ArrayList<SelectItem>(preferencias.size());

		for (TipoIncidentePreferencia p : preferencias) {
			items.add(new SelectItem(p.getId(), p.getDescricao()));
		}
		return items;
	}

	public void setIncidentePreferencia(List<SelectItem> incidentePreferencia) {
		IncidentePreferencia = incidentePreferencia;
	}

	
	public Boolean getSetorAutorizado() throws ServiceException {
		ConfiguracaoSistema configuracaoSistema = getConfiguracaoSistemaService().recuperarValor(SISTEMA_PROCESSAMENTO, SETOR_DOC_RESTRITOS);
		if(getSetorUsuarioAutenticado() != null && configuracaoSistema != null &&  configuracaoSistema.getValor().toString().contains(getSetorUsuarioAutenticado().getId().toString())) {
			return true;
		}
		return false;
		
	
	}
	
}
