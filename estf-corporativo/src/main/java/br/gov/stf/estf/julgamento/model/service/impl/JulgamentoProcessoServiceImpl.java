package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.TipoVoto;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso.TipoSituacaoVoto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.julgamento.model.dataaccess.JulgamentoProcessoDao;
import br.gov.stf.estf.julgamento.model.dataaccess.ListaJulgamentoDao;
import br.gov.stf.estf.julgamento.model.service.InformacaoPautaProcessoService;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.service.VotoJulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.util.JulgamentoProcessoSearchData;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.SearchResult;

@Service("julgamentoProcessoService")
public class JulgamentoProcessoServiceImpl extends
		GenericServiceImpl<JulgamentoProcesso, Long, JulgamentoProcessoDao>
		implements JulgamentoProcessoService {

	//Retirado em função do ESESSOES-634
	//private ListaJulgamentoService listaJulgamentoService;

	private AgendamentoService agendamentoService;

	private InformacaoPautaProcessoService informacaoPautaProcessoService;
	
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	private ListaJulgamentoDao listaJulgamentoServiceDAO;

	@Autowired
	private VotoJulgamentoProcessoService votoJulgamentoProcessoService;
	
	public JulgamentoProcessoServiceImpl(JulgamentoProcessoDao dao
			                            //Retirado em função do ESESSOES-634
									    //,ListaJulgamentoService listaJulgamentoService
									    ,AgendamentoService agendamentoService) {
		super(dao);
		//Retirado em função do ESESSOES-634
		//this.listaJulgamentoService = listaJulgamentoService;
		this.agendamentoService = agendamentoService;
	}

	public JulgamentoProcessoServiceImpl(JulgamentoProcessoDao dao
										//Retirado em função do ESESSOES-634
										//,ListaJulgamentoService listaJulgamentoService
										,AgendamentoService agendamentoService
										,InformacaoPautaProcessoService informacaoPautaProcessoService) {
		super(dao);
		//Retirado em função do ESESSOES-634
		//this.listaJulgamentoService = listaJulgamentoService;
		this.agendamentoService = agendamentoService;
		this.informacaoPautaProcessoService = informacaoPautaProcessoService;
	}

	public List<JulgamentoProcesso> pesquisarProcessosSessao(Long idSessao)
			throws ServiceException {

		try {
			return dao.pesquisarProcessosSessao(idSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public JulgamentoProcesso recuperarJulgamentoProcesso(Long id,
			Long idObjetoIncidente, TipoAmbienteConstante tipoAmbienteSessao,
			String colegiado) throws ServiceException {
		try {
			return dao.recuperarJulgamentoProcesso(id, idObjetoIncidente,
					tipoAmbienteSessao, colegiado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public SearchResult<JulgamentoProcesso> pesquisarJulgamentoProcesso(
			JulgamentoProcessoSearchData searchData) throws ServiceException {
		try {
			return dao.pesquisarJulgamentoProcesso(searchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<JulgamentoProcesso> pesquisarJulgamentoProcesso(Long idOpjetoIncidentePrincipal, Long idMinistro, String... sigTipoRecursos) throws ServiceException {
		try {
			List<JulgamentoProcesso> lista = dao.pesquisarJulgamentoProcesso(idOpjetoIncidentePrincipal, idMinistro, sigTipoRecursos);
			
			if (lista != null)
				for (JulgamentoProcesso jp : lista)
					jp.setObjetoIncidente(objetoIncidenteService.recuperarPorId(jp.getObjetoIncidente().getId()));
			return lista;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoRG(Long idOpjetoIncidentePrincipal, Long idMinistro, String... sigTipoRecursos) throws ServiceException {
		try {
			return dao.pesquisarJulgamentoProcessoRG(idOpjetoIncidentePrincipal, idMinistro, sigTipoRecursos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<ProcessoImagem> recuperarInteiroTeor(Long idObjetoIncidente,
			Processo processo) throws ServiceException {
		try {
			return dao.recuperarInteiroTeor(idObjetoIncidente, processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<ProcessoImagem> recuperarInteiroTeorRG(Long idObjetoIncidente,
			Processo processo) throws ServiceException {
		try {
			return dao.recuperarInteiroTeorRG(idObjetoIncidente, processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void definirSessaoJulgamento(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao, String idUsuario, Setor setor, boolean lancarAndamentoApresentadoEmMesa)
			throws ServiceException {
		definirSessaoJulgamento(objetoIncidente, sessao, idUsuario, setor, null, lancarAndamentoApresentadoEmMesa);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void definirSessaoJulgamento(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao, String idUsuario, Setor setor, Integer ordemSessao, boolean lancarAndamentoApresentadoEmMesa)
			throws ServiceException {
		Set<Long> processados = new HashSet<Long>();

		atualizarJulgamentoProcesso(objetoIncidente, sessao, processados,
				idUsuario, setor, ordemSessao, lancarAndamentoApresentadoEmMesa);

		flushSession();
	}

	@Override
	public void definirSessaoJulgamento(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao, String idUsuario, Setor setor, Integer ordemSessao)
			throws ServiceException {
		definirSessaoJulgamento(objetoIncidente, sessao, idUsuario, setor, ordemSessao, true);
	}

	private void atualizarJulgamentoProcesso(
			ObjetoIncidente<?> objetoIncidente, Sessao sessao,
			Set<Long> processados, String idUsuario, Setor setor,
			Integer ordemSessao, boolean lancarAndamentoApresentadoEmMesa) throws ServiceException {
		excluirJulgamentoProcessoNaoJulgado(objetoIncidente, sessao);

		Agendamento agendamento = agendamentoService.recuperar(null, null,
				objetoIncidente);

		Integer codigoCapituloSessao = TipoColegiadoConstante.valueOfSigla(
				sessao.getColegiado().getId()).getCodigoCapitulo();
		if (agendamento == null) {
			agendamentoService.incluirAgendamentoControlePauta(objetoIncidente,
					sessao, idUsuario, setor, lancarAndamentoApresentadoEmMesa);
		} else if (!agendamento.getId().getCodigoCapitulo()
				.equals(codigoCapituloSessao)) {
			agendamento.getId().setCodigoCapitulo(codigoCapituloSessao);
			agendamentoService.salvar(agendamento);
		}

		JulgamentoProcesso julgamentoProcesso = recuperar(objetoIncidente,
				sessao);

		if (julgamentoProcesso == null) {
			julgamentoProcesso = new JulgamentoProcesso();
			julgamentoProcesso.setObjetoIncidente(objetoIncidente);
			julgamentoProcesso.setSessao(sessao);
			julgamentoProcesso
					.setSituacaoProcessoSessao(TipoSituacaoProcessoSessao.NAO_JULGADO);
		}
		if (ordemSessao == null) {
			ordemSessao = definirOrdemSessao(sessao);
		}
		julgamentoProcesso.setOrdemSessao(ordemSessao);
		salvar(julgamentoProcesso);
		processados.add(objetoIncidente.getId());

		/*
		 * Verifica se o Processo encontra-se em lista de Julgamento conjunto,
		 * caso afirmativo, os processos também serão movidos para a Sessão de
		 * destino.
		 */
		List<ObjetoIncidente<?>> listaObjetosIncidente = pesquisarProcessosIrmaosParaDefinicaoSessaoJulgamento(
				objetoIncidente, sessao, ordemSessao);
		for (ObjetoIncidente<?> oi : listaObjetosIncidente) {
			if (!processados.contains(oi.getId())) {
				atualizarJulgamentoProcesso(oi, sessao, processados, idUsuario,
						setor, ordemSessao, lancarAndamentoApresentadoEmMesa);
			}
		}
	}

	@Override
	public Integer definirOrdemSessao(Sessao sessao) throws ServiceException {
		Integer ordemSessao = recuperarUltimaOrdemSessao(sessao);
		if (ordemSessao == null) {
			ordemSessao = new Integer(1);
		} else {
			ordemSessao++;
		}
		return ordemSessao;
	}

	private Integer recuperarUltimaOrdemSessao(Sessao sessao)
			throws ServiceException {
		try {
			return dao.recuperarUltimaOrdemSessao(sessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void excluirJulgamentoProcessoNaoJulgado(
			ObjetoIncidente<?> objetoIncidente, Sessao sessao)
			throws ServiceException {
		try {
			List<JulgamentoProcesso> naoJulgados = pesquisarJulgamentoProcessoNaoJulgado(
					objetoIncidente, sessao);
			for (JulgamentoProcesso jp : naoJulgados) {
				refresh(jp);
				dao.atualizarOrdenacaoProcessos(jp, null);
			}
			if (naoJulgados != null && naoJulgados.size() > 0) {
				sessao.getListaJulgamentoProcesso().removeAll(naoJulgados);
				excluirTodos(naoJulgados);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private List<JulgamentoProcesso> pesquisarJulgamentoProcessoNaoJulgado(
			ObjetoIncidente<?> objetoIncidente, Sessao sessao)
			throws ServiceException {
		try {
			return dao.pesquisarJulgamentoProcessoNaoJulgado(objetoIncidente,
					sessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private List<ObjetoIncidente<?>> pesquisarProcessosIrmaosParaDefinicaoSessaoJulgamento(
			ObjetoIncidente<?> objetoIncidente, Sessao sessao,
			Integer ordemSessaoProcessosJulgamentoConjunto)
			throws ServiceException {
		List<ObjetoIncidente<?>> lista = new ArrayList<ObjetoIncidente<?>>();
		List<ObjetoIncidente<?>> listaJulgamentoConjunto = informacaoPautaProcessoService
				.recuperarProcessosJulgamentoConjunto(objetoIncidente, false);
		for (ObjetoIncidente<?> oi : listaJulgamentoConjunto) {
			JulgamentoProcesso jp = recuperar(oi, sessao);
			if (ordemSessaoProcessosJulgamentoConjunto == null
					|| jp == null
					|| jp.getOrdemSessao() == null
					|| !ordemSessaoProcessosJulgamentoConjunto.equals(jp
							.getOrdemSessao())) {
				lista.add(oi);
			}
		}
		return lista;
	}

	@Override
	@Transactional
	public void definirSessaoJulgamento(ListaJulgamento listaJulgamento,
			Sessao sessao, String idUsuario, Setor setor)
			throws ServiceException {
		Set<Long> processados = new HashSet<Long>();

		for (ObjetoIncidente<?> oi : listaJulgamento.getElementos()) {
			if (!processados.contains(oi.getId())) {
				incluirJulgamentoProcesso(oi, sessao, processados, idUsuario,
						setor);
			}
		}

	}

	/**
	 * @param objetoIncidente
	 * @param sessao
	 * @throws ServiceException
	 */
	private void incluirJulgamentoProcesso(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao, Set<Long> processados, String idUsuario, Setor setor)
			throws ServiceException {
		excluirJulgamentoProcessoNaoJulgado(objetoIncidente, sessao);

		if (!existeAgendamento(objetoIncidente, sessao)) {
			agendamentoService.incluirAgendamentoControlePauta(objetoIncidente,
					sessao, idUsuario, setor, false);
		}

		JulgamentoProcesso julgamentoProcesso = new JulgamentoProcesso();
		julgamentoProcesso.setObjetoIncidente(objetoIncidente);
		julgamentoProcesso.setSessao(sessao);
		incluir(julgamentoProcesso);
		processados.add(objetoIncidente.getId());

		List<ObjetoIncidente<?>> listaObjetosIncidente = pesquisarProcessosIrmaosParaDefinicaoSessaoJulgamento(
				objetoIncidente, sessao, true);

		for (ObjetoIncidente<?> oi : listaObjetosIncidente) {
			if (!processados.contains(oi.getId())) {
				incluirJulgamentoProcesso(oi, sessao, processados, idUsuario,
						setor);
			}
		}
	}

	private List<ObjetoIncidente<?>> pesquisarProcessosIrmaosParaDefinicaoSessaoJulgamento(
			ObjetoIncidente<?> objetoIncidente, Sessao sessao,
			boolean incluirProcessoEmSessao) throws ServiceException {
		//List<ListaJulgamento> listasJulgamento = listaJulgamentoService.pesquisar(objetoIncidente);
		List<ListaJulgamento> listasJulgamento = this.listaJulgamentoServicePesquisa(objetoIncidente);
		List<ObjetoIncidente<?>> lista = new ArrayList<ObjetoIncidente<?>>();
		for (ListaJulgamento listaJulgamento : listasJulgamento) {
			for (ObjetoIncidente<?> oi : listaJulgamento.getElementos()) {
				JulgamentoProcesso jp = recuperar(oi, sessao, new Date());
				if (incluirProcessoEmSessao) {
					if (jp == null) {
						lista.add(objetoIncidente);
					}
				} else {
					if (jp != null) {
						lista.add(objetoIncidente);
					}
				}

			}
		}
		return lista;
	}

	List<ListaJulgamento> listaJulgamentoServicePesquisa(ObjetoIncidente<?> objetoIncidente) throws ServiceException{
		try {
			return listaJulgamentoServiceDAO.pesquisar(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private boolean existeAgendamento(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao) throws ServiceException {
		Agendamento agendamento = agendamentoService
				.recuperar(defineCodigoCapitulo(sessao),
						Agendamento.COD_MATERIA_AGENDAMENTO_JULGAMENTO,
						objetoIncidente);
		return agendamento != null;
	}

	private Integer defineCodigoCapitulo(Sessao sessao) {
		if (sessao.getColegiado().getId().equals(Colegiado.TRIBUNAL_PLENO)) {
			return EstruturaPublicacao.COD_CAPITULO_PLENARIO;
		} else if (sessao.getColegiado().getId()
				.equals(Colegiado.PRIMEIRA_TURMA)) {
			return EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA;
		} else if (sessao.getColegiado().getId()
				.equals(Colegiado.SEGUNDA_TURMA)) {
			return EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA;
		}

		return null;
	}

	@Override
	public JulgamentoProcesso recuperar(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao) throws ServiceException {
		try {
			return dao.recuperar(objetoIncidente, sessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public JulgamentoProcesso recuperar(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao, Date dataBase) throws ServiceException {
		try {
			return dao.recuperar(objetoIncidente, sessao, dataBase);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void removerJulgamentoProcesso(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao) throws ServiceException {
		Set<Long> processados = new HashSet<Long>();

		excluirJulgamentoProcesso(objetoIncidente, sessao, processados);

		flushSession();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void excluirJulgamentoProcesso(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao, Set<Long> processados) throws ServiceException {
		JulgamentoProcesso julgamentoProcesso = recuperar(objetoIncidente,
				sessao);

		reordenarProcessos(julgamentoProcesso, null);

		if (julgamentoProcesso != null) {
			excluir(julgamentoProcesso);
		}
		processados.add(objetoIncidente.getId());

		List<ObjetoIncidente<?>> listaObjetosIncidente = pesquisarProcessosIrmaosParaDefinicaoSessaoJulgamento(
				objetoIncidente, sessao, null);

		for (ObjetoIncidente<?> oi : listaObjetosIncidente) {
			if (!processados.contains(oi.getId())) {
				excluirJulgamentoProcesso(oi, sessao, processados);
			}
		}
	}

	@Override
	public JulgamentoProcesso pesquisaSessaoNaoFinalizada(
			ObjetoIncidente<?> objetoIncidente,
			TipoAmbienteConstante tipoAmbiente) throws ServiceException {
		try {
			return dao.pesquisaSessaoNaoFinalizada(objetoIncidente,
					tipoAmbiente);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}
	
	@Override
	public JulgamentoProcesso pesquisaUltimoJulgamentoProcesso(ObjetoIncidente<?> oi) throws ServiceException {
		return pesquisaUltimoJulgamentoProcesso(oi, null, null, null);
	}
	
	@Override
	public JulgamentoProcesso pesquisaUltimoJulgamentoProcesso(ObjetoIncidente<?> oi, Ministro ministroDestaque, TipoSituacaoProcessoSessao situacaoProcessoSessao, Boolean destaqueCancelado) throws ServiceException {
		try {
			JulgamentoProcesso jp = dao.pesquisaUltimoJulgamentoProcesso(oi, ministroDestaque, situacaoProcessoSessao, destaqueCancelado);
			
			if (jp != null 
				&& jp.getProcessoListaJulgamento() != null 
				&& jp.getProcessoListaJulgamento().getListaJulgamento() != null
				&& jp.getProcessoListaJulgamento().getListaJulgamento().getListaProcessoListaJulgamento() != null) {
					Set<ProcessoListaJulgamento> lplj = jp.getProcessoListaJulgamento().getListaJulgamento().getListaProcessoListaJulgamento();
	
					for (ProcessoListaJulgamento plj : lplj)
						Hibernate.initialize(plj.getObjetoIncidente().getIdentificacao());
			}

			return jp;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void reordenarProcessos(JulgamentoProcesso julgamentoProcessoBase,
			Integer ordemDestino) throws ServiceException {
		try {
			refresh(julgamentoProcessoBase);
			dao.atualizarOrdenacaoProcessos(julgamentoProcessoBase,
					ordemDestino);
			julgamentoProcessoBase.setOrdemSessao(ordemDestino);
			salvar(julgamentoProcessoBase);

			if (ordemDestino != null) {
				List<ObjetoIncidente<?>> processosJulgamentoConjunto = informacaoPautaProcessoService
						.recuperarProcessosJulgamentoConjunto(
								julgamentoProcessoBase.getObjetoIncidente(),
								false);
				List<JulgamentoProcesso> jps = new ArrayList<JulgamentoProcesso>();
				if (processosJulgamentoConjunto != null) {
					for (ObjetoIncidente<?> oi : processosJulgamentoConjunto) {
						JulgamentoProcesso jp = recuperar(oi,
								julgamentoProcessoBase.getSessao());
						jp.setOrdemSessao(ordemDestino);
						jps.add(jp);
					}
				}
				alterarTodos(jps);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void refresh(JulgamentoProcesso julgamentoProcesso)
			throws ServiceException {
		try {
			dao.refresh(julgamentoProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoListaJulgamento(ListaJulgamento listaJulgamento) throws ServiceException {
		try {

			return dao.pesquisarJulgamentoProcessoListaJulgamento(listaJulgamento);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public Map<TipoVoto, List<VotoJulgamentoProcesso>> agruparVotosJulgamentoProcessoPorTipo(
			JulgamentoProcesso julgamentoProcesso) {
		Map<TipoVoto, List<VotoJulgamentoProcesso>> mapaVotos = null;
		for (VotoJulgamentoProcesso voto : julgamentoProcesso.getListaVotoJulgamentoProcesso()){
			if (voto.getTipoSituacaoVoto().equals(TipoSituacaoVoto.RASCUNHO.getSigla()) || voto.getTipoSituacaoVoto().equals(TipoSituacaoVoto.CANCELADO.getSigla()))
				continue;
			if(mapaVotos == null)
				mapaVotos = new HashMap<TipoVoto, List<VotoJulgamentoProcesso>>();
			TipoVoto tipoVoto = voto.getTipoVoto();
			if (!mapaVotos.containsKey(tipoVoto)){
				mapaVotos.put(tipoVoto, new LinkedList<VotoJulgamentoProcesso>());
			}
			mapaVotos.get(tipoVoto).add(voto);
		
		}	
		return mapaVotos;
	}
	

	public JulgamentoProcesso clonarJulgamentoProcesso(Long julgamentoProcessoId, Long idSessao) throws DaoException {
		return clonarJulgamentoProcesso(julgamentoProcessoId, idSessao, true);
	}
	
	public JulgamentoProcesso clonarJulgamentoProcesso(Long julgamentoProcessoId, Long idSessao, Boolean clonarRascunhos) throws DaoException {
		return dao.clonarJulgamentoProcesso(julgamentoProcessoId, idSessao, clonarRascunhos);
	}

	@Override
	public List<VotoJulgamentoProcesso> recuperarVotosProcesso(Long julgamentoProcessoId) throws ServiceException {

		try {
			return dao.recuperarVotosProcesso(julgamentoProcessoId);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Transactional
	@Override
	public void cancelarAgendamento(List<JulgamentoProcesso> lista) throws ServiceException {
		for (JulgamentoProcesso jp : lista) {
			if (jp.getListaVotoJulgamentoProcesso() != null)
				votoJulgamentoProcessoService.excluirTodos(jp.getListaVotoJulgamentoProcesso());
			
			excluir(jp);
		}
	}
}
