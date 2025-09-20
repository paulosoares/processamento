package br.gov.stf.estf.intimacao.visao;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.intimacao.model.dataaccess.TipoMeioIntimacaoEnum;
import br.gov.stf.estf.intimacao.model.service.exception.AndamentoNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoGerouIntimacaoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteSemAceiteInitmacaoEletronicaException;
import br.gov.stf.estf.intimacao.model.service.exception.PecaNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.PessoaSemUsuarioException;
import br.gov.stf.estf.intimacao.model.service.exception.ProcessoNaoEletronicoException;
import br.gov.stf.estf.intimacao.model.service.exception.TipoModeloComunicacaoEnumInvalidoException;
import br.gov.stf.estf.intimacao.visao.dto.ParteIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.ParteProcessoIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.estf.intimacao.visao.dto.PecaIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.ProcessoIntimacaoDto;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Rodrigo.S.Araujo
 *
 */
public class BeanPartesGerarIntimacao extends AssinadorBaseBean {

	private static final long serialVersionUID = 1L;
	private String processosSemSetor = "";
	private int qntProcessosSemSetor = 0;

	private static final Log LOG = LogFactory
			.getLog(BeanPartesGerarIntimacao.class);

	private static final String KEY_MAP_PARTE_PROCESSO = BeanPartesGerarIntimacao.class
			.getCanonicalName() + ".mapPartes";
	private static final String KEY_LISTA_PARTE = BeanPartesGerarIntimacao.class
			.getCanonicalName() + ".listaPartes";

	private Date dataPublicacao = new Date();
	private TipoMeioIntimacaoEnum tipoMeioIntimacaoEnum;
	private HtmlDataTable tabelaPartes;

	private TreeMap<String, ParteProcessoIntimacaoDto> mapaIdPessoaParteProcesso = new TreeMap<String, ParteProcessoIntimacaoDto>();

	private List<ParteIntimacaoDto> partes;
	private List<SelectItem> listaTipoIntimacao;

	private static final String CHAVE_RESPONSAVEL_ASSINATURA = "titular.secretaria.judiciaria";
	private static final String CHAVE_CARGO_RESPONSAVEL_ASSINATURA = "cargo.secretaria.judiciaria";

	private String responsavelAssinatura;
	private String cargoResponsavelAssinatura;
	private boolean todasPartesSelecionadas;

	public BeanPartesGerarIntimacao() {
		this.listaTipoIntimacao = carregarTipoIntimacao();
	}

	public List<ParteIntimacaoDto> getPartes() {
		return partes;
	}

	public void setPartes(List<ParteIntimacaoDto> partes) {
		this.partes = partes;
		if (this.partes != null) {
			Collections.sort(this.partes, new Comparator<ParteIntimacaoDto>() {
				@Override
				public int compare(
						ParteIntimacaoDto parteProcessoIntimacaoDto2,
						ParteIntimacaoDto parteProcessoIntimacaoDto1) {
					return parteProcessoIntimacaoDto2.getNomeParte().trim().compareTo(
							parteProcessoIntimacaoDto1.getNomeParte().trim());
				}
			});
		}
	}

	public void pesquisarPartes() throws ServiceException {
		try {
			if (dataPublicacao == null) {
				reportarAviso("Informe uma data para a pesquisa!");
			} else {
				partes = getPartesGerarIntimacaoLocalService().listarPartes(dataPublicacao, null, null, null, tipoMeioIntimacaoEnum); 

				setPartes(partes);

				processosSemSetor = "Os processos a seguir não serão intimados pois não possuem setores definidos: ";
				for (ParteIntimacaoDto parteIntimacaoDto : partes) {
					parteIntimacaoDto.agrupar();
					for (ProcessoIntimacaoDto processoIntimacaoDto : parteIntimacaoDto
							.getProcessos()) {
						if (processoIntimacaoDto.getCodigoSetor() == null
								|| processoIntimacaoDto.getDescricaoSetor() == null) {
							processosSemSetor += processoIntimacaoDto
									.getSiglaClasseProcesso()
									+ " "
									+ processoIntimacaoDto.getNumeroProcesso()
									+ ", ";
							qntProcessosSemSetor++;
						}
					}
				}
				processosSemSetor = processosSemSetor.substring(0,
						processosSemSetor.length() - 2);
				processosSemSetor += ".";

				if (this.getPartes() == null || this.getPartes().isEmpty()) {
					reportarAviso("Nenhum documento encontrado com a data informada.");
				}
			}
			atualizaSessao();
			responsavelAssinatura = getConfiguracaoSistemaService()
					.recuperarValor(CHAVE_RESPONSAVEL_ASSINATURA);
			cargoResponsavelAssinatura = getConfiguracaoSistemaService()
					.recuperarValor(CHAVE_CARGO_RESPONSAVEL_ASSINATURA);
		} catch (ServiceException se) {
			String data = new SimpleDateFormat("dd/MM/yyyy")
					.format(dataPublicacao);
			reportarErro("Erro ao buscar partes do DJ da data informada ("
					+ data + ").", se, LOG);
		}
	}

	public void inicialisarTelaGerarIntimacaoFisica(ActionEvent e) {
		this.setDataPublicacao(null);
		this.setTipoMeioComunicacaoEnum(null);
		this.mapaIdPessoaParteProcesso = null;
		this.partes = null;
		this.setPartes(null);
		this.setTabelaPartes(null);
		this.setResponsavelAssinatura(null);
		this.setCargoResponsavelAssinatura(null);
		this.setTodasPartesSelecionadas(false);
	}

	private List<SelectItem> carregarTipoIntimacao() {
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();

		listaCombo.add(new SelectItem(null, "Todas"));

		for (TipoMeioIntimacaoEnum integracaoEnum : TipoMeioIntimacaoEnum
				.values()) {
			listaCombo.add(new SelectItem(integracaoEnum, integracaoEnum
					.getDescricao()));
		}

		return listaCombo;
	}

	// ------------------------ SESSAO ------------------------
	@PostConstruct
	public void restaurarSessao() {
		if (getAtributo(KEY_MAP_PARTE_PROCESSO) == null) {
			setAtributo(KEY_MAP_PARTE_PROCESSO, mapaIdPessoaParteProcesso);
		}

		setMapPessoa((TreeMap<String, ParteProcessoIntimacaoDto>) getAtributo(KEY_MAP_PARTE_PROCESSO));
		if (getAtributo(KEY_LISTA_PARTE) == null) {
			setAtributo(KEY_LISTA_PARTE, partes);
		}
	}

	public void atualizaSessao() {
		applyStateInHttpSession();
		setAtributo(KEY_MAP_PARTE_PROCESSO, mapaIdPessoaParteProcesso);
		setAtributo(KEY_LISTA_PARTE, partes);
		setTodasPartesSelecionadas(false);
	}
	
	
	public void gerar() throws ServiceException {
		List<ParteIntimacaoDto> partesSelecionadas = extrairExisteParteSelecionada();

		List<Comunicacao> comunicacoesFisicas = new ArrayList<Comunicacao>();
		List<Comunicacao> comunicacoesEletronicas = new ArrayList<Comunicacao>();

		if (!partesSelecionadas.isEmpty()) {
			Setor setor = getSetorUsuarioAutenticado();
			Map<Long, TipoPecaProcesso> mapaIdTipoPecaProcesso = new HashMap<Long, TipoPecaProcesso>();
			for (ParteIntimacaoDto parteIntimacaoDto : partesSelecionadas) {
				Parte parte = new Parte();
				parte.setId(parteIntimacaoDto.getSeqPessoa());
				parte.setSeqJurisdicionado(parteIntimacaoDto.getSeqPessoa());
				parte.setNomeJurisdicionado(parteIntimacaoDto.getNomeParte());

				DocumentoEletronico documentoAcordao = null;

				Set<PecaDTO> pecasAgruparEIntimarEletronicos = new HashSet<PecaDTO>();
				Set<PecaDTO> pecasAgruparEIntimarFisicos = new HashSet<PecaDTO>();
				Set<PecaDTO> pecasDespachoDecisaoAcordao = new HashSet<PecaDTO>();
				Set<PecaDTO> pecas = new HashSet<PecaDTO>();

				for (ProcessoIntimacaoDto processoIntimacaoDto : parteIntimacaoDto
						.getProcessos()) {

					pecas.clear();
					
					if ( !processoIntimacaoDto.getSelected() ) {
						continue;
					}

					for (PecaIntimacaoDto pecaIntimacaoDto : processoIntimacaoDto
							.getPecas()) {
						if (processoIntimacaoDto.getCodigoSetor() == null
								|| processoIntimacaoDto.getDescricaoSetor() == null) {
							continue;
						}

						PecaDTO pecaDto = new PecaDTO();
						pecaDto.setId(pecaIntimacaoDto
								.getSeqPecaProcessoEletronico());
						pecaDto.setDataAlteracao(parteIntimacaoDto
								.getDataDivulgacao());

						if (pecaIntimacaoDto.getIdTipoPecaProcessual() != null) {
							TipoPecaProcesso tipoPecaProcesso = mapaIdTipoPecaProcesso
									.get(pecaIntimacaoDto
											.getIdTipoPecaProcessual());
							if (tipoPecaProcesso == null) {
								tipoPecaProcesso = getTipoPecaProcessoService()
										.recuperarPorId(
												pecaIntimacaoDto
														.getIdTipoPecaProcessual());
							}
							if (tipoPecaProcesso == null) {
								LOG.info("Nenhum tipo de peça informado.");
							}
							pecaDto.setTipoPecaProcesso(tipoPecaProcesso);
						}

						Processo processo = new Processo();
						processo.setNumeroProcessual(processoIntimacaoDto
								.getNumeroProcesso());
						processo.setTipoMeioProcesso(TipoMeioProcesso
								.valueOf(processoIntimacaoDto
										.getTipoMeioProcesso()));
						processo.setId(processoIntimacaoDto
								.getSeqObjetoIncidente());

						Classe classeProcessual = new Classe();
						classeProcessual.setId(processoIntimacaoDto
								.getSiglaClasseProcesso());
						classeProcessual.setDescricao(processoIntimacaoDto
								.getDescricaoClasseProcessual());

						processo.setClasseProcessual(classeProcessual);

						Setor setorRecebimento = new Setor();
						setorRecebimento.setId(processoIntimacaoDto
								.getCodigoSetor());
						setorRecebimento.setNome(processoIntimacaoDto
								.getDescricaoSetor());
						processo.setSetorRecebimento(setorRecebimento);

						pecaDto.setObjetoIncidente(processo);

						pecaDto.setSeqDocumentoPeca(pecaIntimacaoDto
								.getSeqDocumento());

						pecaDto.setSeqDocumentoAcordaoPeca(pecaIntimacaoDto
								.getSeqDocumentoAcordao());
						pecas.add(pecaDto);
					}


					if (parteIntimacaoDto.getTipoMeioIntimacao()
							.equalsIgnoreCase("E")
							&& (parteIntimacaoDto.getIsUsuarioExterno() != null && parteIntimacaoDto
									.getIsUsuarioExterno()
									.equalsIgnoreCase("S"))
							&& !processoIntimacaoDto.getTipoMeioProcesso()
									.equals("F")) {
						pecasDespachoDecisaoAcordao.addAll(pecas);
					} else {
						if (processoIntimacaoDto.getTipoMeioProcesso().equals(
								"F")) {
							pecasAgruparEIntimarFisicos.addAll(pecas);
						} else {
							pecasAgruparEIntimarEletronicos.addAll(pecas);
						}
					}
				}

				try {
					if (!pecasAgruparEIntimarFisicos.isEmpty()) {
						comunicacoesFisicas.addAll(getIntimacaoLocalService()
								.criarIntimacoesFisicas(
										parteIntimacaoDto.getSeqPessoa(),
										getUser().getUsername(),
										parteIntimacaoDto
												.getModeloComunicacaoEnum(),
										parteIntimacaoDto.getDataDivulgacao(),
										parte, setor,
										pecasAgruparEIntimarFisicos,
										getResponsavelAssinatura(),
										getCargoResponsavelAssinatura(),
										parteIntimacaoDto.getNumeroDJ(),
										documentoAcordao, TipoMeioProcesso.FISICO));
					}
					if (!pecasAgruparEIntimarEletronicos.isEmpty()) {
						comunicacoesFisicas.addAll(getIntimacaoLocalService()
								.criarIntimacoesFisicas(
										parteIntimacaoDto.getSeqPessoa(),
										getUser().getUsername(),
										parteIntimacaoDto
												.getModeloComunicacaoEnum(),
										parteIntimacaoDto.getDataDivulgacao(),
										parte, setor,
										pecasAgruparEIntimarEletronicos,
										getResponsavelAssinatura(),
										getCargoResponsavelAssinatura(),
										parteIntimacaoDto.getNumeroDJ(),
										documentoAcordao, TipoMeioProcesso.ELETRONICO));
					}
					if (!pecasDespachoDecisaoAcordao.isEmpty()) {
						criarIntimacoes(
								pecasDespachoDecisaoAcordao,
								ModeloComunicacaoEnum.INTIMACAO_DESPACHO_DECISAO_ACORDAO,
								parteIntimacaoDto.getSeqPessoa(),
								comunicacoesEletronicas, setor,
								//parteIntimacaoDto.getDataDivulgacao(),
								new Date(),
								TipoFaseComunicacao.ENVIADO,
								"COMUNICAÇÃO ELETRÔNICA", documentoAcordao);
					}
				} catch (ParteNaoPertencenteProcessoException e) {
					reportarErro("Erro ao gerar intimação física.", e, LOG);
				} catch (ParteNaoGerouIntimacaoException e) {
					reportarErro("Erro ao gerar intimação física.", e, LOG);
				} catch (Exception e) {
					reportarErro("Erro ao gerar intimação física.", e, LOG);
				}
			}
			gerarMensagemResultadoComunicacao(comunicacoesFisicas,
					comunicacoesEletronicas);
		} else {
			reportarAviso("Para realizar a geração selecione uma parte.");
		}
		partesSelecionadas.clear();
		pesquisarPartes();		
	}

	private void criarIntimacoes(
			Collection<PecaDTO> listaPecaProcessoEletronico,
			ModeloComunicacaoEnum modeloComunicacaoEnum,
			Long codigoPessoaDestinatario, List<Comunicacao> comunicacoes,
			Setor setor, Date dataEnvio,
			TipoFaseComunicacao tipoFaseComunicacao,
			String descricaoComunicacao, DocumentoEletronico documentoAcordao)
			throws ParteSemAceiteInitmacaoEletronicaException,
			ProcessoNaoEletronicoException,
			AndamentoNaoPertencenteProcessoException,
			PessoaSemUsuarioException, ServiceException,
			ParteNaoPertencenteProcessoException,
			PecaNaoPertencenteProcessoException,
			TipoModeloComunicacaoEnumInvalidoException {
		for (PecaDTO pecaDto : listaPecaProcessoEletronico) {
			Set<Long> hashSetObjetoIncidente = new HashSet<Long>();
			Processo processo = (Processo) pecaDto.getObjetoIncidente();
			hashSetObjetoIncidente.add(processo.getId());

			comunicacoes.add(getIntimacaoLocalService().criarIntimacao(
					getUser().getUsername(), setor, dataEnvio,
					modeloComunicacaoEnum, hashSetObjetoIncidente,
					codigoPessoaDestinatario,
					new ArrayList<PecaProcessoEletronico>(),
					new ArrayList<AndamentoProcesso>(), tipoFaseComunicacao,
					descricaoComunicacao, documentoAcordao));
		}
	}

	private void gerarMensagemResultadoComunicacao(
			List<Comunicacao> comunicacoesFisicas,
			List<Comunicacao> comunicacoesEletronicas) {
		if (!comunicacoesFisicas.isEmpty()
				|| !comunicacoesEletronicas.isEmpty()) {
			reportarAviso("Comunicação de intimação da(s) parte(s) selecionada(s) criada com sucesso.");
		} else {
			reportarFalha("Não foi possível a criação da comunicação de intimação com a(s) parte(s) selecionada(s).");
		}
	}

	private List<ParteIntimacaoDto> extrairExisteParteSelecionada()
			throws ServiceException {
		List<ParteIntimacaoDto> partesSelecionadas = new ArrayList<ParteIntimacaoDto>();
		for (ParteIntimacaoDto parteIntimacaoDto : partes) {
			if (parteIntimacaoDto.getSelected()) {
				partesSelecionadas.add(parteIntimacaoDto);
			}
		}
		atualizaSessao();
		return partesSelecionadas;
	}

	/**
	 * Recupera o Objeto pelo id do próprio objeto incidente
	 *
	 * @param idObjInc
	 * @return
	 */
	public ObjetoIncidente<?> recuperaObjetoIncidente(Long idObjInc) {
		ObjetoIncidenteService objetoIncidenteService = getObjetoIncidenteService();
		ObjetoIncidente<?> objIncidente = null;

		try {
			objIncidente = objetoIncidenteService.recuperarPorId(idObjInc);
		} catch (ServiceException e) {
			reportarErro(MessageFormat.format(
					"Erro ao pesquisar objeto incidente: {0}.", idObjInc), e,
					LOG);
		}

		return objIncidente;
	}

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public HtmlDataTable getTabelaPartes() {
		return tabelaPartes;
	}

	public void setTabelaPartes(HtmlDataTable tabelaPartes) {
		this.tabelaPartes = tabelaPartes;
	}

	public boolean isTodasPartesSelecionadas() {
		return todasPartesSelecionadas;
	}

	public void setTodasPartesSelecionadas(boolean todasPartesSelecionadas) {
		this.todasPartesSelecionadas = todasPartesSelecionadas;
	}

	public String getResponsavelAssinatura() {
		return responsavelAssinatura;
	}

	public void setResponsavelAssinatura(String responsavelAssinatura) {
		this.responsavelAssinatura = responsavelAssinatura;
	}

	public String getCargoResponsavelAssinatura() {
		return cargoResponsavelAssinatura;
	}

	public void setCargoResponsavelAssinatura(String cargoResponsavelAssinatura) {
		this.cargoResponsavelAssinatura = cargoResponsavelAssinatura;
	}

	public TreeMap<String, ParteProcessoIntimacaoDto> getMapPessoa() {
		return mapaIdPessoaParteProcesso;
	}

	public void setMapPessoa(
			TreeMap<String, ParteProcessoIntimacaoDto> mapPessoa) {
		this.mapaIdPessoaParteProcesso = mapPessoa;
	}

	public List<SelectItem> getListaTipoIntimacao() {
		return listaTipoIntimacao;
	}

	public void setListaTipoIntimacao(List<SelectItem> listaTipoIntimacao) {
		this.listaTipoIntimacao = listaTipoIntimacao;
	}

	public TipoMeioIntimacaoEnum getTipoMeioComunicacaoEnum() {
		return tipoMeioIntimacaoEnum;
	}

	public void setTipoMeioComunicacaoEnum(
			TipoMeioIntimacaoEnum tipoMeioComunicacaoEnum) {
		this.tipoMeioIntimacaoEnum = tipoMeioComunicacaoEnum;
	}

	public String getProcessosSemSetor() {
		return processosSemSetor;
	}

	public void setProcessosSemSetor(String processosSemSetor) {
		this.processosSemSetor = processosSemSetor;
	}

	public int getQntProcessosSemSetor() {
		return qntProcessosSemSetor;
	}

	public void setQntProcessosSemSetor(int qntProcessosSemSetor) {
		this.qntProcessosSemSetor = qntProcessosSemSetor;
	}

}