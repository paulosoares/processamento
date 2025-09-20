package br.gov.stf.estf.processostf.model.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.util.ComparatorDeslocaProcesso;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.processostf.model.dataaccess.DeslocaProcessoDao;
import br.gov.stf.estf.processostf.model.service.ControlarDeslocaIncidenteService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.service.exception.NaoExistemDeslocamentosException;
import br.gov.stf.estf.processostf.model.util.DeslocaProcessoDynamicQuery;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("deslocaProcessoService")
public class DeslocaProcessoServiceImpl
		extends
		GenericServiceImpl<DeslocaProcesso, DeslocaProcessoId, DeslocaProcessoDao>
		implements DeslocaProcessoService {

	@Autowired
	private ProcessoDependenciaService processoDependenciaService;

	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;

	@Autowired
	private GuiaService guiaService;

	@Autowired
	private ControlarDeslocaIncidenteService controlarDeslocaIncidenteService;
	
	@Autowired
	private SetorService setorService;

	public DeslocaProcessoServiceImpl(DeslocaProcessoDao dao) {
		super(dao);
	}

	/**
	 * O método que deve ser utilizado é
	 * recuperarUltimoDeslocamentoProcesso(String siglaClasse, Long
	 * numeroProcesso)
	 * 
	 * @param siglaClasse
	 * @param numeroProcesso
	 * @return
	 * @throws ServiceException
	 * @throws NaoExistemDeslocamentosException
	 */
	@Deprecated
	public DeslocaProcesso consultaUltimoDeslocamentoDoProcesso(
			String classeDoProcesso, Long numeroDoProcesso)
			throws ServiceException, NaoExistemDeslocamentosException {
		try {
			List<DeslocaProcesso> deslocamentos = dao
					.consultaDeslocamentoOrdenadoGuiaDecrescente(
							classeDoProcesso, numeroDoProcesso);
			if (deslocamentos.size() == 0) {
				throw new NaoExistemDeslocamentosException(
						"Não existem deslocamentos para o processo "
								+ classeDoProcesso + " " + numeroDoProcesso
								+ ".");
			}
			return deslocamentos.get(0);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * O método que deve ser utilizado é
	 * recuperarUltimoDeslocamentoProcesso(String siglaClasse, Long
	 * numeroProcesso)
	 * 
	 * @param siglaClasse
	 * @param numeroProcesso
	 * @return
	 * @throws ServiceException
	 * @throws NaoExistemDeslocamentosException
	 */
	@Deprecated
	public DeslocaProcesso recuperarUltimoDeslocaProcesso(String siglaClasse,
			Long numeroProcesso) throws ServiceException {
		return recuperarUltimoDeslocamentoProcesso(siglaClasse, numeroProcesso);
	}

	public void persistirDeslocaProcesso(DeslocaProcesso DeslocaProcesso)
			throws ServiceException {

		try {
			dao.persistirDeslocamentoProcesso(DeslocaProcesso);
		} catch (DaoException e) {

			throw new ServiceException(e.getMessage());
		}
	}

	public void atualizaAndamento(DeslocaProcesso deslocaProcesso, Long idAndamentoProcesso)
			throws ServiceException {

		try {
			dao.atualizaAndamento(deslocaProcesso, idAndamentoProcesso);
		} catch (DaoException e) {

			throw new ServiceException(e.getMessage());
		}
	}
	
	
	public void atualizarDeslocamento(DeslocaProcesso deslocaProcesso, Long idProcesso)
			throws ServiceException {

		try {
			dao.atualizaAndamento(deslocaProcesso, idProcesso);
		} catch (DaoException e) {

			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	public DeslocaProcesso recuperarUltimoDeslocamentoProcesso(
			String siglaClasse, Long numeroProcesso) throws ServiceException {
		DeslocaProcessoDynamicQuery consultaDinamica = new DeslocaProcessoDynamicQuery();
		consultaDinamica.setNumeroProcesso(numeroProcesso);
		consultaDinamica.setSiglaClasseProcessual(siglaClasse);
		return recuperaUltimoDeslocamento(consultaDinamica);
	}

	/**
	 * Recupera o ultimo deslocamento de um objeto incidente. Como o
	 * Deslocamento tem como base o Processo, o dado utilizado para filtrar é o
	 * getPrincipal() do ObjetoIncidente.
	 * 
	 * @param objetoIncidente
	 * @return
	 * @throws ServiceException
	 */
	public DeslocaProcesso recuperarUltimoDeslocamentoProcesso(
			ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		DeslocaProcessoDynamicQuery consultaDinamica = new DeslocaProcessoDynamicQuery();
		consultaDinamica.setObjetoIncidente(objetoIncidente);
		return recuperaUltimoDeslocamento(consultaDinamica);
	}

	private DeslocaProcesso recuperaUltimoDeslocamento(
			DeslocaProcessoDynamicQuery consultaDinamica)
			throws ServiceException {
		try {
			consultaDinamica.setUltimoDeslocamento(true);
			return dao.recuperarUltimoDeslocamento(consultaDinamica);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void persistirDeslocamentoProcesso(
			DeslocaProcesso deslocamentoProcesso) throws ServiceException {

		try {
			dao.persistirDeslocamentoProcesso(deslocamentoProcesso);
		} catch (DaoException e) {

			throw new ServiceException(e.getMessage());
		}
	}

	public void receberProcesso(DeslocaProcesso deslocamentoProcesso)
			throws ServiceException {

		try {
			// coloca a data de recebimento no deslocamento
			deslocamentoProcesso.setDataRecebimento(new Date());
			deslocamentoProcesso.setUltimoDeslocamento(true);
			dao.salvar(deslocamentoProcesso);
			dao.atualizarDeslocamento(deslocamentoProcesso.getSeqDeslocaProcesso(), deslocamentoProcesso.getId().getProcesso().getId());
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	// retorna apenas um elemento de uma lista
	public DeslocaProcesso recuperarDeslocamentoProcesso(Guia guia)
			throws ServiceException {

		try {
			List<DeslocaProcesso> deslocaProcessos = dao
					.recuperarDeslocamentoProcessos(guia);
			if (deslocaProcessos.size() > 0) {
				return deslocaProcessos.get(0);
			} else {
				return null;
			}
		} catch (DaoException e) {

			throw new ServiceException(e.getMessage());
		}
	}

	public List<DeslocaProcesso> recuperarDeslocamentoProcessos(Guia guia)
			throws ServiceException {

		try {
			List<DeslocaProcesso> deslocaProcessos = dao
					.recuperarDeslocamentoProcessos(guia);
			for (DeslocaProcesso dp : deslocaProcessos) {
				if (processoDependenciaService.getQuantidadeVinculados(dp.getId().getProcesso()) == 0){
					dp.getId().getProcesso().setQuantidadeApensos(0);
				}
				if (dp.getId().getProcesso().getQuantidadeJuntadasLinha() == null) {
					dp.getId().getProcesso()
							.setQuantidadeJuntadasLinha(Short.parseShort("0"));
				}
				if (dp.getId().getProcesso() != null && dp.getId().getProcesso().getMinistroRelatorAtual() != null) {
					dp.setNomeMinistroRelatorAtual(dp.getId().getProcesso().getMinistroRelatorAtual().getNome());
				}
			}
			return deslocaProcessos;
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<DeslocaProcesso> recuperarDeslocamentoProcessosRecebimentoExterno(
			Guia guia) throws ServiceException {
		try {
			return dao.recuperarDeslocamentoProcessosRecebimentoExterno(guia);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Long pesquisarSetorUltimoDeslocamento(Processo processo)
			throws ServiceException {

		try {
			return dao.pesquisarSetorUltimoDeslocamento(processo);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Setor pesquisarSetorUltimoDeslocamento(Long seqObjetoIncidente) throws ServiceException {
		try {
			Long codSetor = dao.pesquisarSetorUltimoDeslocamento(seqObjetoIncidente);
			if(codSetor != null){
				return setorService.recuperarPorId(codSetor);
			}else{
				return null;
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public void removerProcesso(DeslocaProcesso processo)
			throws ServiceException {
		try {
			dao.removerProcesso(processo);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<DeslocaProcesso> pesquisarDataRecebimentoGuiaProcesso(Guia guia)
			throws ServiceException {
		try {
			return dao.pesquisarDataRecebimentoGuiaProcesso(guia);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public DeslocaProcesso recuperarUltimoDeslocamentoProcesso(Processo processo)
			throws ServiceException {
		try {
			return dao.recuperarUltimoDeslocamentoProcesso(processo);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<DeslocaProcesso> recuperarProcessosPeloSetor(Long codigoSetor)
			throws ServiceException {
		try {
			return dao.recuperarProcessosPeloSetor(codigoSetor);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void deslocaProcessoSetor(Long codigoOrgaoOrigem,
			Long codigoOrgaoDestino, String tipoOrgaoOrigem,
			String tipoOrgaoDestino, Long seqObjetoIncidente)
			throws ServiceException {
		try {
			dao.deslocaProcessoSetor(codigoOrgaoOrigem, codigoOrgaoDestino,
					tipoOrgaoOrigem, tipoOrgaoDestino, seqObjetoIncidente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------

	@Override
	public void insereProcesso(String chkTipoOrgao, String tipoGuia,
			Long codigoLotacao, Processo processoSelecionado)
			throws ServiceException, RegraDeNegocioException {

		if (tipoGuia.equals("PRE") && (chkTipoOrgao.equals("ADV"))) {
			throw new RegraDeNegocioException(
					"Não é possível deslocar processo eletrônico a um advogado.");
		}

		if (codigoLotacao == null) {
			throw new RegraDeNegocioException(
					"Origem do deslocamento inválida. Verifique a lotação do usuário.");
		}

		if (verificaSeProcessoExiste(processoSelecionado) == false) {
			throw new RegraDeNegocioException("Processo inexistente.");
		}

		verificaEstadoProcesso(processoSelecionado, codigoLotacao);
	}

	private boolean verificaSeProcessoExiste(Processo processoSelecionado)
			throws ServiceException {
		return recuperaObjetoIncidente(processoSelecionado).getPrincipal() != null;
	}

	private void verificaEstadoProcesso(Processo processo, Long codigoLotacao)
			throws RegraDeNegocioException, ServiceException {
		DeslocaProcesso deslocaProcesso = recuperaDeslocamentoProcesso(processo);

		if (deslocaProcesso == null) {
			throw new RegraDeNegocioException("O processo "
					+ processo.getSiglaClasseProcessual() + "-"
					+ processo.getNumeroProcessual()
					+ " não encontra-se no setor do usuário.");
		}

		if (deslocaProcesso.getDataRecebimento() == null) {
			throw new RegraDeNegocioException("O processo "
					+ deslocaProcesso.getClasseProcesso() + "/"
					+ deslocaProcesso.getNumeroProcesso()
					+ " está em trânsito.");
		}

		ProcessoDependencia processoDependencia = verificaProcessoApensoDoPrincipal(processo);
		if (processoDependencia != null) {
			throw new RegraDeNegocioException(
					"Não é possível deslocar o processo "
							+ processoDependencia.getClasseProcesso() + "-"
							+ processoDependencia.getNumeroProcesso()
							+ ", pois ele está apensado ao processo "
							+ processoDependencia.getClasseProcessoVinculador()
							+ "-"
							+ processoDependencia.getNumeroProcessoVinculador()
							+ ".");
		}

		// if ((deslocaProcesso.getCodigoOrgaoDestino() == null) ||
		// (!deslocaProcesso.equals(codigoLotacao))) {
		// throw new RegraDeNegocioException("O processo " +
		// processo.getSiglaClasseProcessual() +
		// "-" + processo.getNumeroProcessual() +
		// " não encontra-se no setor do usuário.");
		// }
	}

	private DeslocaProcesso recuperaDeslocamentoProcesso(Processo processo)
			throws ServiceException {
		return recuperarUltimoDeslocamentoProcesso(processo);
	}

	private ProcessoDependencia verificaProcessoApensoDoPrincipal(
			Processo processo) throws ServiceException {
		return processoDependenciaService.getProcessoVinculador(processo);
	}

	private ObjetoIncidente<?> recuperaObjetoIncidente(
			Processo processoSelecionado) throws ServiceException {
		return objetoIncidenteService.recuperarPorId(processoSelecionado
				.getId());
	}

	@Override
	public void deslocarProcesso(Processo processo, Long codigoOrgaoOrigem,
			Long codigoOrgaoDestino, Integer tipoOrgaoOrigem,
			Integer tipoOrgaoDestino) throws ServiceException {
		try {
			dao.deslocarProcesso(processo, codigoOrgaoOrigem,
					codigoOrgaoDestino, tipoOrgaoOrigem, tipoOrgaoDestino);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public DeslocaProcesso recuperaDeslocamentoProcessoRecebimento(
			DeslocaProcesso deslocaProcesso, Long setorUsuario)
			throws ServiceException {
		try {
			return dao.recuperaDeslocamentoProcessoRecebimento(deslocaProcesso,
					setorUsuario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void inserirBaixa(Processo processo, Origem origem, Setor setor,
			AndamentoProcesso andamentoProcesso) throws ServiceException {

		deslocarProcessoParaSetorUsuario(processo, setor);
		darBaixaProcesso(processo, origem, setor, andamentoProcesso);
	}

	@Override
	public void deslocarProcessoParaSetorUsuario(Processo processo, Setor setor)
			throws ServiceException {

		DeslocaProcesso deslocaProcesso = recuperarUltimoDeslocamentoProcesso(processo);

		try {
			// Se o processo não estiver no setor do usuário que vai fazer a
			// baixa.
			if (!deslocaProcesso.getGuia().getCodigoOrgaoDestino().equals(setor.getId())) {
				ArrayList<Long> processos = new ArrayList<Long>();
				processos.add(processo.getId());

				Guia guia = new Guia();
				Guia.GuiaId guiaId = new Guia.GuiaId();
				guiaId.setCodigoOrgaoOrigem(deslocaProcesso.getCodigoOrgaoDestino());
				guia.setId(guiaId);
				guia.setTipoOrgaoOrigem(deslocaProcesso.getGuia().getTipoOrgaoDestino());
				guia.setCodigoOrgaoDestino(setor.getId());
				guia.setTipoOrgaoDestino(DeslocaProcesso.TIPO_ORGAO_INTERNO);

				String numAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, processos, true);
				if (numAnoGuia == null) {
					throw new ServiceException("Erro ao deslocar o processo para o setor do usuário.");
				}
			}
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void darBaixaProcesso(Processo processo, Origem origem,
			Long codigoOrigem, Integer tipoOrgaoOrigem,
			AndamentoProcesso andamentoProcesso) throws ServiceException {
		try {
			DeslocaProcesso deslocaProcesso = recuperarUltimoDeslocamentoProcesso(processo);

			ArrayList<Long> processos = new ArrayList<Long>();
			processos.add(processo.getId());

			Guia guia = new Guia();
			Guia.GuiaId guiaId = new Guia.GuiaId();

			guiaId.setCodigoOrgaoOrigem(codigoOrigem);
			guia.setId(guiaId);

			guia.setTipoOrgaoOrigem(tipoOrgaoOrigem);
			
			if(origem != null){
				guia.setCodigoOrgaoDestino(origem.getId());
			}
			guia.setTipoOrgaoDestino(DeslocaProcesso.TIPO_ORGAO_EXTERNO);

			String numAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, processos, true);

			if (numAnoGuia == null) {
				throw new ServiceException("Erro ao efetuar o deslocamento");
			}

			deslocaProcesso = recuperarUltimoDeslocamentoProcesso(processo);
			deslocaProcesso.setAndamentoProcesso(andamentoProcesso);

			alterar(deslocaProcesso);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServiceException("Erro ao efetuar o deslocamento: "
					+ e.getMessage());
		}
	}
	
	@Override
	public String darBaixaProcesso(Guia guia, ArrayList<Long> processos) throws ServiceException {
		
		try {
			//deslocaProcesso = recuperarUltimoDeslocamentoProcesso(processo);
			
			String numAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, processos, true);
			if (numAnoGuia == null) {
				throw new ServiceException("Erro ao efetuar o deslocamento");
			}
			System.out.println("Guia: "+ numAnoGuia);
			return numAnoGuia;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServiceException("Erro ao efetuar o deslocamento: "
					+ e.getMessage());
		}
	}


	@Override
	public void darBaixaProcesso(Processo processo, Origem origem, Setor setor,
			AndamentoProcesso andamentoProcesso) throws ServiceException {

		darBaixaProcesso(processo, origem, setor.getId(),
				DeslocaProcesso.TIPO_ORGAO_INTERNO, andamentoProcesso);
	}

	@Override
	public void devolverDeslocamento(Processo processo) throws ServiceException {

		DeslocaProcesso deslocaProcesso = recuperarUltimoDeslocamentoProcesso(processo);
		Guia guiaProcesso = deslocaProcesso.getGuia();

		try {
			ArrayList<Long> processos = new ArrayList<Long>();
			processos.add(processo.getId());

			Guia guia = new Guia();
			Guia.GuiaId guiaId = new Guia.GuiaId();
			guiaId.setCodigoOrgaoOrigem(guiaProcesso.getCodigoOrgaoDestino());
			guia.setId(guiaId);
			guia.setTipoOrgaoOrigem(deslocaProcesso.getGuia()
					.getTipoOrgaoDestino());
			guia.setCodigoOrgaoDestino(guiaProcesso.getId()
					.getCodigoOrgaoOrigem());
			guia.setTipoOrgaoDestino(deslocaProcesso.getGuia()
					.getTipoOrgaoOrigem());

			String numAnoGuia = objetoIncidenteService.inserirDeslocamento(
					guia, processos, true);
			if (numAnoGuia == null) {
				throw new ServiceException(
						"Erro ao deslocar o processo para o setor do usuário.");
			}
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	public List<DeslocaProcesso> recuperaPorProcessoOrigemExterna(
			Processo processo) throws ServiceException {
		try {
			return dao.recuperaPorProcessoOrigemExterna(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void alterarDeslocaProcessosPorAndamento(
			DeslocaProcesso deslocaProcesso,
			AndamentoProcesso andamentoProcesso, Setor setor, Origem origem)
			throws ServiceException {
		if (origem == null) {
			throw new ServiceException("Destino não informado.");
		}
		if (setor == null) {
			throw new ServiceException("Setor do usuário não informado.");
		}
		if (deslocaProcesso == null) {
			throw new ServiceException("Deslocamento não informado.");
		}
		if (andamentoProcesso == null) {
			throw new ServiceException("Andamento não informado.");
		}
		try {
			Guia guiaDoDeslocamentoProcesso = deslocaProcesso.getGuia();
			guiaDoDeslocamentoProcesso.setPaginacao(false);
			List<DeslocaProcesso> listaDeslocaProcessos = recuperarDeslocamentoProcessos(guiaDoDeslocamentoProcesso);
			
			Guia guia = new Guia();
			GuiaId gId = new GuiaId();
			guia.setId(gId);
			
			// Origem é um setor do STF
			guia.getId().setCodigoOrgaoOrigem(setor.getId());
			guia.setTipoOrgaoOrigem(DeslocaProcesso.TIPO_ORGAO_INTERNO);
			
			// O destino sempre será um orgão externo
			guia.setCodigoOrgaoDestino(origem.getId());
			guia.setTipoOrgaoDestino(DeslocaProcesso.TIPO_ORGAO_EXTERNO);

			Guia novaguia = guiaService.geraGuiaVazia(guia);
			Integer numSeq = 1;
			
			dao.alterarPkDeslocaProcesso(deslocaProcesso, novaguia, andamentoProcesso, numSeq);
			
			if (listaDeslocaProcessos.size() != 1) {
				listaDeslocaProcessos.remove(deslocaProcesso);
				
				Collections.sort(listaDeslocaProcessos, new ComparatorDeslocaProcesso(true));
				
				int numeroSequencia = 1;
				
				for(DeslocaProcesso desProcesso : listaDeslocaProcessos){
					desProcesso.setNumeroSequencia(numeroSequencia);
					alterar(desProcesso);
					numeroSequencia++;
				}
				
				guiaDoDeslocamentoProcesso.setQuantidadeProcesso(numeroSequencia);
				guiaService.alterar(guiaDoDeslocamentoProcesso);
			}else{
				
				guiaDoDeslocamentoProcesso.setQuantidadeProcesso(0);
				guiaService.alterar(guiaDoDeslocamentoProcesso);
			}
			
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean isBaixadoParaOrigem(Processo processo, Origem origem,
			Andamento andamento) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return dao.isBaixadoParaOrigem(processo, origem, andamento);
		} catch (DaoException e) {
			throw new ServiceException("Não foi possível verificar a baixa do processo.",e);
		}
	}

	@Override
	public Integer recuperarUltimaSequencia(Guia guia) throws ServiceException {
		try {
			return dao.recuperarUltimaSequencia(guia);
		} catch (DaoException e) {
			throw new ServiceException("Não foi possível recuperar a última sequencia do deslocamento");
		}
	}
	
	@Override
	public DeslocaProcesso recuperarUltimaRemessaProcesso(String siglaProcesso, Long numeroProcesso) throws ServiceException {
		try {
			return dao.recuperarUltimaRemessaProcesso(siglaProcesso, numeroProcesso);
		} catch (DaoException e) {
			throw new ServiceException("Não foi possível recuperar a última remessa do processo");
		}
	}
	
}
