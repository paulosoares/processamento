package br.gov.stf.estf.intimacao.beans;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.intimacao.entidade.TipoMeioComunicacaoEnum;
import br.gov.stf.estf.intimacao.model.dataaccess.TipoMeioIntimacaoEnum;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoGerouIntimacaoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.visao.dto.ParteProcessoIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Rodrigo.S.Araujo
 *
 */
public class PartesGerarIntimacaoBean extends AssinadorBaseBean {

	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(PartesGerarIntimacaoBean.class);

	private static final String KEY_MAP_PARTE_PROCESSO = PartesGerarIntimacaoBean.class.getCanonicalName() + ".mapPartes";
	private static final String KEY_LISTA_PARTE = PartesGerarIntimacaoBean.class.getCanonicalName() + ".listaPartes";

	private Date dataPublicacao = new Date();
	private TipoMeioIntimacaoEnum tipoMeioIntimacaoEnum;
	private HtmlDataTable tabelaPartes;

	private TreeMap<String, ParteProcessoIntimacaoDto> mapaIdPessoaParteProcesso = new TreeMap<String, ParteProcessoIntimacaoDto>();

	private List<ParteProcessoIntimacaoDto> partes;
	private List<ParteProcessoIntimacaoDto> listaPartes;
	private List<SelectItem> listaTipoIntimacao;

	private String responsavelAssinatura;
	private String cargoResponsavelAssinatura;
	private boolean todasPartesSelecionadas;

	public PartesGerarIntimacaoBean() {
		this.listaTipoIntimacao = carregarTipoIntimacao();
	}

	public void pesquisarPartes() throws ServiceException {
		try {
			partes = getPartesGerarIntimacaoLocalService().listarPartes(dataPublicacao, tipoMeioIntimacaoEnum);
			montarEstruturaPartes();
			setListaPartes(new ArrayList<ParteProcessoIntimacaoDto>(mapaIdPessoaParteProcesso.values()));

			if (this.getListaPartes() == null || this.getListaPartes().isEmpty()) {
				reportarAviso("Nenhum documento encontrado com a data informada.");
			} else {
				for (ParteProcessoIntimacaoDto parteProcessoIntimacaoDto : listaPartes) {
					adicionarProcessos(parteProcessoIntimacaoDto,parteProcessoIntimacaoDto.getMapProcessosFisicos());
					adicionarProcessos(parteProcessoIntimacaoDto,parteProcessoIntimacaoDto.getMapProcessosEletronicos());
				}
			}
			atualizaSessao();
		} catch (ServiceException se) {
			String data = new SimpleDateFormat("dd/MM/yyyy").format(dataPublicacao);
			reportarErro("Erro ao buscar partes do DJ da data informada (" + data + ").", se, LOG);
		}
	}

	private List<SelectItem> carregarTipoIntimacao() {
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();

		listaCombo.add(new SelectItem(null, "Todas"));

		for (TipoMeioComunicacaoEnum integracaoEnum : TipoMeioComunicacaoEnum.values()) {
			listaCombo.add(new SelectItem(integracaoEnum, integracaoEnum.getDescricao()));
		}

		return listaCombo;
	}

	
	private void montarEstruturaPartes() throws ServiceException {
		mapaIdPessoaParteProcesso.clear();
		for (ParteProcessoIntimacaoDto parteProcessoIntimacaoDto : partes) {
			parteProcessoIntimacaoDto.setMapProcessosFisicos(new TreeMap<String, List<PecaDTO>>());
			parteProcessoIntimacaoDto.setMapProcessosEletronicos(new TreeMap<String, List<PecaDTO>>());

			if (mapaIdPessoaParteProcesso.get(String.valueOf(parteProcessoIntimacaoDto.getSeqPessoa())) == null) {
				if (TipoMeioProcesso.valueOf(parteProcessoIntimacaoDto.getTipoMeioProcesso()) == TipoMeioProcesso.FISICO) {
					TreeMap<String, List<PecaDTO>> mapProcessosFisicos = new TreeMap<String, List<PecaDTO>>();
					parteProcessoIntimacaoDto.setMapProcessosFisicos(adicionarMapProcessos(mapProcessosFisicos,parteProcessoIntimacaoDto));
				} else {
					TreeMap<String, List<PecaDTO>> mapProcessosEletronicos = new TreeMap<String, List<PecaDTO>>();
					parteProcessoIntimacaoDto.setMapProcessosEletronicos(adicionarMapProcessos(mapProcessosEletronicos,parteProcessoIntimacaoDto));
				}
				
				mapaIdPessoaParteProcesso.put(String.valueOf(parteProcessoIntimacaoDto.getSeqPessoa()),parteProcessoIntimacaoDto);
			} else if (TipoMeioProcesso.valueOf(parteProcessoIntimacaoDto.getTipoMeioProcesso()) == TipoMeioProcesso.FISICO) {
				TreeMap<String, List<PecaDTO>> mapProcessosFisicos = mapaIdPessoaParteProcesso.get(String.valueOf(parteProcessoIntimacaoDto.getSeqPessoa())).getMapProcessosFisicos();
				adicionarMapProcessos(mapProcessosFisicos,parteProcessoIntimacaoDto);
			} else {
				TreeMap<String, List<PecaDTO>> mapProcessosEletronicos = mapaIdPessoaParteProcesso.get(String.valueOf(parteProcessoIntimacaoDto.getSeqPessoa())).getMapProcessosEletronicos();
				adicionarMapProcessos(mapProcessosEletronicos,parteProcessoIntimacaoDto);
			}
		}
	}

	private TreeMap<String, List<PecaDTO>> adicionarMapProcessos(TreeMap<String, List<PecaDTO>> mapProcessos, ParteProcessoIntimacaoDto parteProcessoIntimacaoDto) throws ServiceException {
		if (mapProcessos.get(String.valueOf(parteProcessoIntimacaoDto.getNumeroProcesso())) == null) {
			List<PecaDTO> listaPeca = new ArrayList<PecaDTO>();

			PecaDTO pecaDto = adicionarPecaProcesso(parteProcessoIntimacaoDto);
			listaPeca.add(pecaDto);

			mapProcessos.put(String.valueOf(parteProcessoIntimacaoDto.getNumeroProcesso()), listaPeca);
		} else {
			List<PecaDTO> listaPeca = mapProcessos.get(String.valueOf(parteProcessoIntimacaoDto.getNumeroProcesso()));

			PecaDTO pecaDto = adicionarPecaProcesso(parteProcessoIntimacaoDto);
			listaPeca.add(pecaDto);

			mapProcessos.put(String.valueOf(parteProcessoIntimacaoDto.getNumeroProcesso()), listaPeca);
		}

		return mapProcessos;
	}

	private PecaDTO adicionarPecaProcesso(ParteProcessoIntimacaoDto parteProcessoIntimacaoDto) throws ServiceException {

        PecaDTO pecaDto = new PecaDTO();
		
		pecaDto.setId(parteProcessoIntimacaoDto.getSeqPecaProcessoEletronico()); 
		pecaDto.setDataAlteracao(parteProcessoIntimacaoDto.getDataDivulgacao()); 
		
		TipoPecaProcesso tipoPecaProcesso = new TipoPecaProcesso(); 
		tipoPecaProcesso.setDescricao(parteProcessoIntimacaoDto.getDescricaoTipoPecaProcessual());

		pecaDto.setTipoPecaProcesso(tipoPecaProcesso);  
		
		
		Processo processo = new Processo();
		processo.setNumeroProcessual(parteProcessoIntimacaoDto.getNumeroProcesso());
		processo.setTipoMeioProcesso(TipoMeioProcesso.valueOf(parteProcessoIntimacaoDto.getTipoMeioProcesso()));
		processo.setId(parteProcessoIntimacaoDto.getSeqObjetoIncidente());

		Classe classeProcessual = new Classe();
		classeProcessual.setId(parteProcessoIntimacaoDto.getSiglaClasseProcesso());
		classeProcessual.setDescricao(parteProcessoIntimacaoDto.getDescricaoClasseProcessual());

		processo.setClasseProcessual(classeProcessual);

		Setor setorRecebimento = new Setor();
		setorRecebimento.setId(parteProcessoIntimacaoDto.getCodigoSetor());
		setorRecebimento.setNome(parteProcessoIntimacaoDto.getDescricaoSetor());
		processo.setSetorRecebimento(setorRecebimento);
			
		pecaDto.setObjetoIncidente(processo);  
		
		pecaDto.setSeqDocumentoPeca(parteProcessoIntimacaoDto.getSeqDocumento());
	
		parteProcessoIntimacaoDto.getListaProcessos().add(pecaDto); 


		return pecaDto;
	}

	// ------------------------ SESSAO ------------------------
	@PostConstruct
	public void restaurarSessao() {
		if (getAtributo(KEY_MAP_PARTE_PROCESSO) == null) {
			setAtributo(KEY_MAP_PARTE_PROCESSO, mapaIdPessoaParteProcesso);
		}

		setMapPessoa((TreeMap<String, ParteProcessoIntimacaoDto>) getAtributo(KEY_MAP_PARTE_PROCESSO));
		if (getAtributo(KEY_LISTA_PARTE) == null) {
			setAtributo(KEY_LISTA_PARTE, listaPartes);
		}
	}

	public void atualizaSessao() {
		applyStateInHttpSession();
		setAtributo(KEY_MAP_PARTE_PROCESSO, mapaIdPessoaParteProcesso);
		setAtributo(KEY_LISTA_PARTE, listaPartes);
		setTodasPartesSelecionadas(false);
	}

	public void gerar() throws ServiceException {
		Set<ParteProcessoIntimacaoDto> partesSelecionadas = verificarExisteParteSelecionada();

		List<Comunicacao> comunicacoesFisicas = new ArrayList<Comunicacao>();
		List<Comunicacao> comunicacoesEletronicas = new ArrayList<Comunicacao>();

		if (!partesSelecionadas.isEmpty()) {
			Setor setor = getSetorUsuarioAutenticado();
			for (ParteProcessoIntimacaoDto parteProcessoIntimacaoDto : partesSelecionadas) {
				try {
					Parte parte = new Parte();

					ObjetoIncidente<?> objetoIncidente = recuperaObjetoIncidente(parteProcessoIntimacaoDto.getSeqObjetoIncidente());

					parte.setId(parteProcessoIntimacaoDto.getSeqParteProcessual());
					parte.setSeqJurisdicionado(parteProcessoIntimacaoDto.getSeqPessoa());
					parte.setNomeJurisdicionado(parteProcessoIntimacaoDto.getNomeParte());
					parte.setObjetoIncidente(objetoIncidente);

					comunicacoesFisicas.addAll(getIntimacaoLocalService().criarIntimacoesFisicas(
									getUser().getUsername(),
									parteProcessoIntimacaoDto.getModeloComunicacaoEnum(),
									parteProcessoIntimacaoDto.getDataDivulgacao(),
									parte,
									setor,
									parteProcessoIntimacaoDto.getMapProcessosFisicos(),
									getResponsavelAssinatura(),
									getCargoResponsavelAssinatura(),
									parteProcessoIntimacaoDto.getNumeroDJ(), 
									TipoMeioProcesso.FISICO));

					comunicacoesEletronicas.addAll(getIntimacaoLocalService().criarIntimacoesFisicas(
									getUser().getUsername(),
									parteProcessoIntimacaoDto.getModeloComunicacaoEnum(),
									parteProcessoIntimacaoDto.getDataDivulgacao(),
									parte,
									setor,
									parteProcessoIntimacaoDto.getMapProcessosEletronicos(),
									getResponsavelAssinatura(),
									getCargoResponsavelAssinatura(),
									parteProcessoIntimacaoDto.getNumeroDJ(),
									TipoMeioProcesso.FISICO));

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

	public Set<ParteProcessoIntimacaoDto> verificarExisteParteSelecionada()
			throws ServiceException {
		Set<ParteProcessoIntimacaoDto> partesSelecionadas = new HashSet<ParteProcessoIntimacaoDto>();
		Set<String> keys = mapaIdPessoaParteProcesso.keySet();
		for (String key : keys) {
			ParteProcessoIntimacaoDto parteProcessoIntimacaoDto = mapaIdPessoaParteProcesso
					.get(key);
			if (parteProcessoIntimacaoDto.isSelected()) {
				partesSelecionadas.add(parteProcessoIntimacaoDto);
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

	public void marcarTodos() {
		if (isTodasPartesSelecionadas()) {
			for (ParteProcessoIntimacaoDto dto : listaPartes) {
				dto.setSelected(true);
			}
		} else {
			for (ParteProcessoIntimacaoDto dto : listaPartes) {
				dto.setSelected(false);
			}
		}
	}

	public List<ParteProcessoIntimacaoDto> getListaPartes() {
		return listaPartes;
	}

	private void adicionarProcessos(ParteProcessoIntimacaoDto parteProcessoIntimacaoDto, Map<String, List<PecaDTO>> mapProcesso) throws ServiceException {

		for (Map.Entry<String, List<PecaDTO>> entry : mapProcesso.entrySet()) {

			List<PecaDTO> listaPecaProcesso = entry.getValue();

			for (PecaDTO pecaDto : listaPecaProcesso) {

				Processo processo = ((Processo) pecaDto.getObjetoIncidente());

				if (processo.getTipoMeioProcesso() == TipoMeioProcesso.FISICO) {
					parteProcessoIntimacaoDto.getListaProcessoFisico().add(processo.getClasseProcessual().getId() + " "	+ processo.getNumeroProcessual());
				} else {
					parteProcessoIntimacaoDto.getListaProcessoEletronico().add(processo.getClasseProcessual().getId() + " "	+ processo.getNumeroProcessual());
				}

				if (parteProcessoIntimacaoDto.getListaProcessos().size() == 1) {
					PecaDTO pecaDtoAux = (PecaDTO) parteProcessoIntimacaoDto.getListaProcessos().get(0);
					if (pecaDtoAux.getId() != pecaDto.getId())
						parteProcessoIntimacaoDto.getListaProcessos().add(pecaDto);
				} else {
					parteProcessoIntimacaoDto.getListaProcessos().add(pecaDto);
				}
			}

		}
	}

	public void setListaPartes(List<ParteProcessoIntimacaoDto> listaPartes) {
		this.listaPartes = listaPartes;
		if (this.listaPartes != null) {
			Collections.sort(this.listaPartes,
					new Comparator<ParteProcessoIntimacaoDto>() {
						@Override
						public int compare(
								ParteProcessoIntimacaoDto parteProcessoIntimacaoDto2,
								ParteProcessoIntimacaoDto parteProcessoIntimacaoDto1) {
							return parteProcessoIntimacaoDto2.getNomeParte()
									.compareTo(
											parteProcessoIntimacaoDto1
													.getNomeParte());
						}
					});
		}
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

	public TipoMeioIntimacaoEnum getTipoMeioIntimacaoEnum() {
		return tipoMeioIntimacaoEnum;
	}

	public void setTipoMeioIntimacaoEnum(TipoMeioIntimacaoEnum tipoMeioIntimacaoEnum) {
		this.tipoMeioIntimacaoEnum = tipoMeioIntimacaoEnum;
	}


}
