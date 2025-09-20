package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.util.Calendar;

import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.documento.model.util.ComunicacaoSearch;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.entidade.RemessaVolume;
import br.gov.stf.estf.expedicao.entidade.TipoEmbalagem;
import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.entidade.UnidadePostagem;
import br.gov.stf.estf.expedicao.model.dataaccess.ListaRemessaDao;
import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;
import br.gov.stf.estf.expedicao.model.service.ListaRemessaService;
import br.gov.stf.estf.expedicao.model.service.PostagemService;
import br.gov.stf.estf.expedicao.model.util.FinalizarRemessaDTO;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.estf.expedicao.model.util.TipoEntregaEnum;
import br.gov.stf.estf.localizacao.model.service.OrigemService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfoImpl;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("listaRemessaService")
public class ListaRemessaServiceImpl extends GenericServiceImpl<ListaRemessa, Long, ListaRemessaDao> implements ListaRemessaService {

	public static final long serialVersionUID = 1L;

	@Autowired
	private PostagemService postagemService;

	@Autowired
	private GuiaService guiaService;

	@Autowired
	private DeslocaProcessoService deslocaProcessoService;

	@Autowired
	private ProcessoService processoService;

	@Autowired
	private AndamentoProcessoService andamentoProcessoService;

	@Autowired
	private FaseComunicacaoService faseComunicacaoService;

	@Autowired
	private ComunicacaoService comunicacaoService;

	@Autowired
	private OrigemService origemService;

	@Autowired
	private PecaProcessoEletronicoService pecaProcessoEletronicoService;

	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;

	protected ListaRemessaServiceImpl(ListaRemessaDao dao) {
		super(dao);
	}

	@Override
	public ListaRemessa incluir(ListaRemessa entidade) throws ServiceException {
		entidade.setDataCriacao(new Date());
		verificarEntidadeEnviada(entidade);
		validarListaRemessa(entidade);
		ajustarRelacionamentosBidirecionais(entidade);
		if (TipoEntregaEnum.CORREIOS.equals(entidade.getTipoEntrega()) && entidade.getContratoPostagem() == null) {
			throw new ServiceException("Lista de remessa com tipo de serviço 'Correios' sem Contrato de Postagem vinculado.");
		}
		if (entidade.getTipoEntrega().equals(TipoEntregaEnum.CORREIOS)) {
			postagemService.gerarEtiquetas(entidade);
		}
		int anoCorrente = Util.anoCorrente();
		Long numeroListaRemessa;
		try {
			numeroListaRemessa = dao.gerarNumeroListaRemessa(anoCorrente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		entidade.setAnoListaRemessa(anoCorrente);
		entidade.setNumeroListaRemessa(numeroListaRemessa);
		return super.incluir(entidade);
	}

	@Override
	public ListaRemessa alterar(ListaRemessa entidade) throws ServiceException {
		validarListaRemessa(entidade);
		ajustarRelacionamentosBidirecionais(entidade);
		return super.alterar(entidade);
	}

	@Override
	public ListaRemessa salvar(ListaRemessa entidade) throws ServiceException {
		ListaRemessa resultado;
		if (entidade.getId() == null) {
			resultado = incluir(entidade);
		} else {
			resultado = alterar(entidade);
		}
		return resultado;
	}

	@Override
	public void excluir(ListaRemessa entidade) throws ServiceException {
		verificarEntidadeEnviada(entidade);
		super.excluir(entidade);
	}

	private void ajustarRelacionamentosBidirecionais(ListaRemessa listaRemessa) {
		for (Remessa remessa : listaRemessa.getRemessas()) {
			remessa.setListaRemessa(listaRemessa);
			for (RemessaVolume volume : remessa.getVolumes()) {
				volume.setRemessa(remessa);
			}
		}
	}

	private void verificarEntidadeEnviada(ListaRemessa entidade) throws ServiceException {
		if (entidade.getId() != null && entidade.getDataEnvio() != null) {
			throw new ServiceException("Esta entidade não pode ser alterada ou excluída pois já foi enviada.");
		}
	}

	private void validarListaRemessa(ListaRemessa entidade) throws ServiceException {
		TipoEntregaEnum tipoEntrega = entidade.getTipoEntrega();
		UnidadePostagem unidadePostagem = entidade.getUnidadePostagem();
		boolean isListaCorreios = TipoEntregaEnum.CORREIOS.equals(tipoEntrega);
		if (tipoEntrega == null) {
			throw new ServiceException("Tipo de entrega não identificada.");
		}
		if (unidadePostagem != null && !isListaCorreios) {
			throw new ServiceException("Unidade de postagem inválida.");
		}
		if (isListaNulaOuVazia(entidade.getRemessas())) {
			throw new ServiceException("Lista sem remessas.");
		}
		validarDatas(entidade);
		for (Remessa remessa : entidade.getRemessas()) {
			validarTiposServicoRemessas(entidade, remessa);
			if (!TipoEntregaEnum.MALOTE.equals(tipoEntrega)) {
				validarVolumes(remessa.getVolumes());
				if (remessa.getNumero().trim().isEmpty()) {
					throw new ServiceException("O número do endereço do destinatário da da remessa deve ser informado.");
				}
			}
		}
	}

	private void validarVolumes(List<RemessaVolume> volumes) throws ServiceException {
		for (RemessaVolume remessaVolume : volumes) {
			if (remessaVolume.getPesoGramas() < 1) {
				throw new ServiceException("O peso dos volumes deve ser maiores que zero.");
			}
		}
	}

	private void validarDatas(ListaRemessa entidade) throws ServiceException {
		if (verificarDataMaior(entidade.getDataCriacao(), entidade.getDataEnvio())) {
			throw new ServiceException("Data de criação maior que data de envio.");
		}
		if (verificarDataMaior(entidade.getDataCriacao(), entidade.getDataFinalizacao())) {
			throw new ServiceException("Data de criação maior que data de finalização.");
		}
		if (verificarDataMaior(entidade.getDataEnvio(), entidade.getDataFinalizacao())) {
			throw new ServiceException("Data de envio maior que data de finalização.");
		}
	}

	private boolean verificarDataMaior(Date dataInicio, Date dataFim) {
		Date dataInicioAux = dataInicio == null ? null : Util.inicioDia(dataInicio);
		Date dataFimAux = dataFim == null ? null : Util.inicioDia(dataFim);
		return dataInicioAux != null && dataFimAux != null && dataInicioAux.compareTo(dataFimAux) > 0;
	}

	private boolean isListaNulaOuVazia(List<?> lista) {
		return lista == null || lista.isEmpty();
	}

	private void validarTiposServicoRemessas(ListaRemessa listaRemessa, Remessa remessa) throws ServiceException {
		TipoServico tipoServicoLista = listaRemessa.getTipoServico();
		if (isListaNulaOuVazia(remessa.getVolumes())) {
			throw new ServiceException("Remessa sem volume.");
		}
		TipoEmbalagem tipoEmbalagem = remessa.getTipoEmbalagem();
		List<TipoEmbalagem> tiposEmbalagemTipoServico = tipoServicoLista.getTiposEmbalagem();
		boolean isListaCorreios = TipoEntregaEnum.CORREIOS.equals(listaRemessa.getTipoEntrega());
		if (isListaCorreios) {
			if (tipoEmbalagem == null || !tiposEmbalagemTipoServico.contains(tipoEmbalagem)) {
				throw new ServiceException("Embalagem inválida.");
			}
		} else {
			if (tipoEmbalagem != null) {
				throw new ServiceException("Embalagem inválida.");
			}
		}
		validarTiposServico(isListaCorreios, remessa, tipoServicoLista);
	}

	private void validarTiposServico(boolean isListaCorreios, Remessa remessa, TipoServico tipoServicoLista) throws ServiceException {
		List<TipoServico> tiposServicoRemessa = remessa.getTiposServico();
		boolean isListaTipoServicosVazia = isListaNulaOuVazia(tiposServicoRemessa);
		if (isListaCorreios && isListaTipoServicosVazia) {
			throw new ServiceException("Nenhum tipo de serviço especificado na Remessa.");
		} else if (!isListaCorreios && !isListaTipoServicosVazia) {
			throw new ServiceException("Esta remessa não deve possuir tipo de serviço.");
		}
		validarTiposServicoObrigatoriosNecessarios(tipoServicoLista, tiposServicoRemessa);
	}

	private void validarTiposServicoObrigatoriosNecessarios(TipoServico tipoServicoLista, List<TipoServico> tiposServicoRemessa) throws ServiceException {
		List<TipoServico> tiposServicoObrigatoriosLista = tipoServicoLista.getTiposServicoObrigatorios();
		if (!tiposServicoRemessa.containsAll(tiposServicoObrigatoriosLista)) {
			throw new ServiceException("Tipo Serviço obrigatório não informado.");
		}
		for (TipoServico tipoServicoRemessa : tiposServicoRemessa) {
			TipoServico tipoServicoNecessario = tipoServicoLista.getTipoServicoNecessarioAoAdicional(tipoServicoRemessa);
			if (tipoServicoNecessario != null && !tiposServicoRemessa.contains(tipoServicoNecessario)) {
				throw new ServiceException("Tipo Serviço necessário não informado.");
			}
		}
	}

	@Override
	public List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws ServiceException {
		try {
			return dao.pesquisar(pesquisaListaRemessaDto);
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto, boolean enviada) throws ServiceException {
		try {
			return dao.pesquisar(pesquisaListaRemessaDto, enviada);
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public ListaRemessa pesquisar(long numeroListaRemessa, int ano) throws ServiceException {
		try {
			return dao.pesquisar(numeroListaRemessa, ano);
		} catch (DaoException ex) {
			throw new ServiceException(ex);
		}
	}

	@Override
	public void finalizarListaRemessa(FinalizarRemessaDTO finalizarListaRemessaDTO) throws ServiceException, DaoException {
		if (finalizarListaRemessaDTO != null && finalizarListaRemessaDTO.getListaRemessa() != null) {
			ListaRemessa listaRemessa = recuperarPorId(finalizarListaRemessaDTO.getListaRemessa().getId());
			for (Remessa remessa : listaRemessa.getRemessas()) {
				registrarOperacoesParaFinalizacaoRemessa(remessa, finalizarListaRemessaDTO);
			}
		}
		System.out.println("Finalizando lista " + finalizarListaRemessaDTO.getListaRemessa().getNumeroListaRemessaAnoFormato());
		System.out.println(" ");
		super.alterar(finalizarListaRemessaDTO.getListaRemessa());
		flushSession();
	}

	@Override
	public void reabrirListaRemessa(ListaRemessa listaRemessa) throws ServiceException {
		listaRemessa.setDataEnvio(null);
		super.alterar(listaRemessa);
		flushSession();
	}

	private void registrarOperacoesParaFinalizacaoRemessa(Remessa r, FinalizarRemessaDTO finalizarListaRemessaDTO) throws ServiceException, DaoException {
		Map<Long, Guia> mapProcessoGuia = new HashMap<Long, Guia>();
		List<Processo> listaProcessos = new ArrayList<Processo>();
		if (r.getGuiaDeslocamento() != null && !r.getGuiaDeslocamento().trim().isEmpty()) {
			String[] guias = r.getGuiaDeslocamento().split(";");
			for (String g : guias) {
				String[] guiaDeslocamento = g.split("/");
				if (guiaDeslocamento.length == 2) {
					try {
						GuiaId guiaId = new GuiaId();
						guiaId.setNumeroGuia(Long.parseLong(guiaDeslocamento[0].trim()));
						guiaId.setAnoGuia(Short.valueOf(guiaDeslocamento[1].trim()));
						guiaId.setCodigoOrgaoOrigem(finalizarListaRemessaDTO.getSetorUsuarioAutenticado().getId());
						Guia guia = guiaService.recuperarPorId(guiaId);
						if (guia != null) {
							List<DeslocaProcesso> listaDeProcessos = deslocaProcessoService.recuperarDeslocamentoProcessos(guia);
							for (DeslocaProcesso dp : listaDeProcessos) {
								listaProcessos.add(dp.getId().getProcesso());
								mapProcessoGuia.put(dp.getId().getProcesso().getId(), guia);
							}
						}
					} catch (NumberFormatException e) {

					}
				}
			}
		} else {
			if (r.getVinculo() != null) {
				String[] listaProc = r.getVinculo().split(";");
				for (String pr : listaProc) {
					Processo proc = recuperarProcesso(pr.trim());
					if (proc != null) {
						listaProcessos.add(proc);
					}
				}
			}
		}

		if (!listaProcessos.isEmpty()) {
			Iterator<Processo> lista = listaProcessos.iterator();
			while (lista.hasNext()) {
				Processo processo = lista.next();
				processo = processoService.recuperarPorId(processo.getId());
				if (finalizarListaRemessaDTO.getAndamento() != null && isAndamentoSelecionadoReferenteGuia(finalizarListaRemessaDTO.getAndamento().getId())) {
					normalizaPecasObjetoIncidente(processo);
				}
				registrarFaseFinalizada(r, processo, finalizarListaRemessaDTO.getIdUsuarioLogado());

				if (finalizarListaRemessaDTO.getAndamento() != null) {
					AndamentoProcessoInfoImpl andamentoProcessoInfo = montarAndamentoProcessoInfo(processo, r, mapProcessoGuia, finalizarListaRemessaDTO);
					registrarAndamento(processo, andamentoProcessoInfo);
				}
				System.out.println("Processo afetado: " + processo.getIdentificacaoCompleta());
			}
		}
	}

	public void normalizaPecasObjetoIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException, DaoException {
		List<PecaProcessoEletronico> listaPecasPendentes = pecaProcessoEletronicoService.pecaProcessoEletronicoPendenteVisualizacao(objetoIncidente);
		if (listaPecasPendentes != null && !listaPecasPendentes.isEmpty()) {
			for (PecaProcessoEletronico ppv : listaPecasPendentes) {
				for (ArquivoProcessoEletronico doc : ppv.getDocumentos()) {
					documentoEletronicoService.alterarTipoDeAcessoDoDocumento(doc.getDocumentoEletronico(), TipoDeAcessoDoDocumento.PUBLICO);
				}
			}
		}
	}

	private void registrarAndamento(Processo processo, AndamentoProcessoInfoImpl andamentoProcessoInfo) throws ServiceException {
		andamentoProcessoService.salvarAndamento(andamentoProcessoInfo, processo, processo);
	}

	private void registrarFaseFinalizada(Remessa r, Processo processo, String idUsuario) throws ServiceException {
		Comunicacao comunicacao = recuperarComunicacaoDaRemessa(r, processo);
		if (comunicacao != null) {
			try {
				comunicacaoService.atualizarSituacaoPecaProcessual(comunicacao.getId());
			} catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
			faseComunicacaoService.incluirFase(TipoFaseComunicacao.FINALIZADO, comunicacao, null, idUsuario.toUpperCase());
		}
	}

	private Comunicacao recuperarComunicacaoDaRemessa(Remessa r, Processo p) throws ServiceException {
		try {
			if (r.getNumeroComunicacao() == null || r.getNumeroComunicacao().trim().isEmpty()) {
				return null;
			}
			String[] nrCom = r.getNumeroComunicacao().replaceAll("[^0-9?/]", "").trim().split("/");
			if (nrCom.length != 2) {
				return null;
			}

			Integer ano = Integer.parseInt(nrCom[1]);

			if (ano == null || (ano.intValue() < 1900 || ano.intValue() > 2100)) {
				return null;
			}

			Calendar ini = Calendar.getInstance();
			ini.set(Integer.parseInt(nrCom[1]), 0, 1); // 01/01/anoInformado
			Calendar fim = Calendar.getInstance();
			fim.set(Integer.parseInt(nrCom[1]), 11, 31); // 31/12/anoInformado

			ComunicacaoSearch comunicacaoSearch = new ComunicacaoSearch();
			comunicacaoSearch.setNumeroComunicacao(Long.parseLong(nrCom[0]));
			comunicacaoSearch.setDataInicio(ini.getTime());
			comunicacaoSearch.setDataFim(fim.getTime());

			List<Comunicacao> listaCom = comunicacaoService.comunicacoesDoPeriodo(comunicacaoSearch);

			if (listaCom != null && !listaCom.isEmpty()) {
				for (Comunicacao com : listaCom) {
					if (com.getComunicacaoIncidente() != null && !com.getComunicacaoIncidente().isEmpty()) {
						for (ComunicacaoIncidente ci : com.getComunicacaoIncidente()) {
							if (ci.getObjetoIncidente().getIdentificacao().startsWith(p.getIdentificacao())) {
								return com;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return null;
	}

	private Processo recuperarProcesso(String vinculo) throws ServiceException {
		if (vinculo != null && !vinculo.isEmpty()) {
			String classProc = ProcessoParser.getSigla(vinculo);
			Long numProc = ProcessoParser.getNumero(vinculo);

			if (StringUtils.isVazia(classProc) || numProc == null) {
				return null;
			}

			return processoService.recuperarProcesso(classProc, numProc);
		}
		return null;
	}

	private AndamentoProcessoInfoImpl montarAndamentoProcessoInfo(Processo processo, Remessa remessa, Map<Long, Guia> mapProcessoGuia, FinalizarRemessaDTO finalizarListaRemessaDTO)
			throws ServiceException {
		AndamentoProcessoInfoImpl andamentoProcessoInfo = new AndamentoProcessoInfoImpl();
		andamentoProcessoInfo.setAndamento(finalizarListaRemessaDTO.getAndamento());
		andamentoProcessoInfo.setCodigoUsuario(finalizarListaRemessaDTO.getIdUsuarioLogado().toUpperCase());

		// Considera como sendo processo sigiloso somente se houver, além de
		// confidencialidade informada, o andamento EXPEDIDO (7317)
		boolean processoSigiloso = processo.getTipoConfidencialidade() != null && finalizarListaRemessaDTO.getAndamento().getId().equals(7317L);

		StringBuffer obs = new StringBuffer();

		Guia guia = null;

		if (isAndamentoSelecionadoReferenteGuia(finalizarListaRemessaDTO.getAndamento().getId())) {
			if (!mapProcessoGuia.isEmpty()) {
				guia = mapProcessoGuia.get(processo.getId());
				obs.append(" - ");
				obs.append("Guia nº " + guia.getId().getNumeroGuia() + "/" + guia.getId().getAnoGuia());
			}
		}

		if (remessa.getTipoComunicacao() != null) {
			obs.append(" - ");
			obs.append(remessa.getTipoComunicacao().getDescricao());
		}
		if (remessa.getNumeroComunicacao() != null) {
			obs.append(" ".concat(remessa.getNumeroComunicacao()));
		}
		if (remessa.getDescricaoPrincipal() != null && !processoSigiloso) {
			obs.append(" - ");
			if (remessa.getDescricaoAnterior() != null) {
				obs.append(remessa.getDescricaoAnterior() + " ");
			}
			obs.append(remessa.getDescricaoPrincipal());
			if (remessa.getDescricaoPosterior() != null) {
				obs.append(" " + remessa.getDescricaoPosterior());
			}
		}
		if (remessa.getObservacao() != null && !processoSigiloso) {
			obs.append(" - ");
			obs.append(remessa.getObservacao());
		}

		if ("Correios".equals(finalizarListaRemessaDTO.getListaRemessa().getDescricaoTipoEntrega())) {
			obs.append(" - ");
			for (RemessaVolume vol : remessa.getVolumes()) {
				obs.append(vol.getNumeroEtiquetaCorreios()).append(" ");
			}
		}
		if ("Malote".equals(finalizarListaRemessaDTO.getListaRemessa().getDescricaoTipoEntrega())) {
			obs.append(" - Malote: ");
			obs.append(remessa.getMalote());
			obs.append(" - Lacre: ");
			obs.append(remessa.getLacre());
		}
		String dataRemessa = DateFormatUtils.format(finalizarListaRemessaDTO.getListaRemessa().getDataEnvio(), "dd/MM/yyyy");
		obs.append(" - Data da Remessa: " + dataRemessa);

		andamentoProcessoInfo.setObservacao(obs.toString().substring(3));
		andamentoProcessoInfo.setSetor(finalizarListaRemessaDTO.getSetorUsuarioAutenticado());

		if (guia != null) {
			Origem origem = origemService.recuperarPorId(guia.getId().getCodigoOrgaoOrigem());
			andamentoProcessoInfo.setOrigem(origem);
		} else {
			andamentoProcessoInfo.setAndamento(finalizarListaRemessaDTO.getAndamentoDefaut());
		}

		List<Processo> processosSelecionados = new ArrayList<Processo>();
		processosSelecionados.add(processo);
		andamentoProcessoInfo.setProcessosPrincipais(processosSelecionados);
		return andamentoProcessoInfo;
	}

	private boolean isAndamentoSelecionadoReferenteGuia(Long idAndamento) {
		return idAndamento != null && (idAndamento.equals(7100L) || idAndamento.equals(7101L) || idAndamento.equals(7104L) || idAndamento.equals(7108L));
	}

}