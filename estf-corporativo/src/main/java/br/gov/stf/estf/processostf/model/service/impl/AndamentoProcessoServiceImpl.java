package br.gov.stf.estf.processostf.model.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia.TipoOcorrenciaConstante;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.GrupoAndamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.SituacaoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.julgamento.model.service.TemaService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProcessoDao;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.OrigemAndamentoDecisaoService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.service.RecursoProcessoService;
import br.gov.stf.estf.processostf.model.service.TipoDevolucaoService;
import br.gov.stf.estf.processostf.model.service.VerificadorPerfilService;
import br.gov.stf.estf.processostf.model.service.exception.AndamentoNaoAutorizadoException;
import br.gov.stf.estf.processostf.model.service.exception.LancamentoIndevidoException;
import br.gov.stf.estf.processostf.model.service.exception.MinistroRelatorAposentadoException;
import br.gov.stf.estf.processostf.model.service.exception.ProcessoApensadoAOutroException;
import br.gov.stf.estf.processostf.model.service.exception.ProcessoNaoAutuadoException;
import br.gov.stf.estf.processostf.model.service.exception.ProcessoOutraRelatoriaException;
import br.gov.stf.estf.processostf.model.service.handler.AndamentoProcessoHandler;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfo;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos.ProcessoEAndamentoProcesso;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.security.AcegiSecurityUtils;

@Service("andamentoProcessoService")
public class AndamentoProcessoServiceImpl extends
		GenericServiceImpl<AndamentoProcesso, Long, AndamentoProcessoDao>
		implements AndamentoProcessoService {

	private Map<String, AndamentoProcessoHandler> andamentoHandlers;

	@Autowired
	private MinistroService ministroService;

	@Autowired
	private DeslocaProcessoService deslocaProcessoService;

	@Autowired
	private AndamentoService andamentoService;

	@Autowired
	private ProcessoDependenciaService processoDependenciaService;

	@Autowired
	private TipoDevolucaoService tipoDevolucaoService;

	@Autowired
	private OrigemAndamentoDecisaoService origemAndamentoDecisaoService;

	@Autowired
	private RecursoProcessoService recursoProcessoService;

	@Autowired
	private ProcessoService processoService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private TemaService temaService;
	
	private Long[] andamentosSemEdicaoObservacao = {7501L, 7502L, 7503L, 7504L, 7505L, 7506L, 7507L, 7508L};

	public AndamentoProcessoServiceImpl(AndamentoProcessoDao dao) {
		super(dao);
	}

	@SuppressWarnings("rawtypes")
	/**
	 * recupera o ultimo numero do andamento. SE deseja o proximo numero utilize o metodo recuperarUltimoNumeroSequencia
	 */
	public Long recuperarUltimoNumeroSequencia(ObjetoIncidente processo) throws ServiceException {
		Long seq = null;
		try {
			seq = dao.recuperarUltimoNumeroSequencia(processo);
			if (seq == null) {
				seq = 0L;
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return seq;
	}
	
	@SuppressWarnings("rawtypes")
	public Long recuperarProximoNumeroSequencia(ObjetoIncidente objetoIncidente) throws ServiceException {
		Long numSequencia = -1L;
		try {
			numSequencia = this.recuperarUltimoNumeroSequencia(objetoIncidente);
			numSequencia++;
		} catch (ServiceException e) {
			throw new ServiceException("Erro ao recuperar o último número de sequência dos andamentos do "+ objetoIncidente.getIdentificacao(), e);
		}
		return numSequencia;
	}

	public AndamentoProcesso recuperarUltimoAndamento(Processo processo)
			throws ServiceException {
		AndamentoProcesso ap = null;
		try {
			ap = dao.recuperarUltimoAndamento(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return ap;
	}

	public Long persistirAndamentoProcesso(AndamentoProcesso andamentoProcesso)
			throws ServiceException {
		try {
			return dao.persistirAndamentoProcesso(andamentoProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public AndamentoProcesso recuperarAndamentoProcesso(String sigla,
			Long numero, Long codigoTipoAndamento, Long numeroPeticao,
			Short anoPeticao) throws ServiceException {
		AndamentoProcesso andamentoProcesso = null;

		try {
			List andamentos = dao.pesquisarAndamentoProcesso(sigla, numero,
					codigoTipoAndamento);
			if (andamentos != null && andamentos.size() > 0) {
				for (Iterator it = andamentos.iterator(); it.hasNext();) {
					AndamentoProcesso andamento = (AndamentoProcesso) it.next();
					String obs = andamento.getDescricaoObservacaoAndamento();
					if (obs.indexOf(numeroPeticao + "/" + anoPeticao) != -1) {
						andamentoProcesso = andamento;
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return andamentoProcesso;
	}

	public OrigemAndamentoDecisao recuperarOrigemAndamentoDecisao(Long id,
			String descricao, Long codigoSetor, Long codigoMinistro,
			Boolean ativo) throws ServiceException {
		try {
			return dao.recuperarOrigemAndamentoDecisao(id, descricao,
					codigoSetor, codigoMinistro, ativo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public AndamentoProcesso recuperarUltimoAndamentoProcesso(
			String siglaClasse, Long numero) throws ServiceException {
		AndamentoProcesso andamentoProcesso = null;
		try {
			andamentoProcesso = dao.recuperarUltimoAndamentoProcesso(
					siglaClasse, numero);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return andamentoProcesso;
	}

	public List<AndamentoProcesso> recuperarAndamentoProcessoSetor(
			String sigla, Long numero, Long setor) throws ServiceException {

		try {
			List<AndamentoProcesso> lista = dao
					.pesquisarAndamentoProcessoSetor(sigla, numero, setor);
			return lista;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Boolean verificaAndamentoProcesso(String siglaProcessual,
			Long numeroProcessual, Long codigoAndamento)
			throws ServiceException {

		try {
			return dao.verificaAndamentoProcesso(siglaProcessual,
					numeroProcessual, codigoAndamento);
		} catch (DaoException e) {
			throw new ServiceException(e);

		}

	}

	public List<AndamentoProcesso> recuperarAndamentoProcesso(
			Long codigoAndamento, Date dataInicial, Date dataFinal)
			throws ServiceException {
		try {
			return dao.pesquisarAndamentoProcesso(codigoAndamento, dataInicial,
					dataFinal);
		} catch (DaoException e) {
			throw new ServiceException(e);

		}

	}

   public AndamentoProcesso recuperarAndamentoProcesso(Long seqAndamentoProcesso) throws ServiceException {
      try {
         return dao.recuperarAndamentoProcesso(seqAndamentoProcesso);
      } catch (DaoException e) {
         throw new ServiceException(e);
      }

   }

   public void atualizarAndamentoProcesso(AndamentoProcesso andamentoProcesso) throws ServiceException {
      try {
         dao.atualizarAndamentoProcesso(andamentoProcesso);
      } catch (DaoException e) {
         throw new ServiceException(e);
      }

   }

   public Boolean verificarAndamentoProcessoNaoIndevido(String siglaProcessual, Long numeroProcessual, Long codigoAndamento) throws ServiceException {

		try {
         return dao.verificarAndamentoProcessoNaoIndevido(siglaProcessual, numeroProcessual, codigoAndamento);
		} catch (DaoException e) {
			throw new ServiceException(e);

		}
	}   

   @Override
public Long recuperarQuantidadeAndamentoProcessoNaoIndevido(String siglaProcessual,
		Long numeroProcessual, Long codigoAndamento) throws ServiceException {
	   try{
		   return dao.recuperarQuantidadeAndamentoProcesso(siglaProcessual, numeroProcessual, codigoAndamento, Boolean.FALSE);
	   }
	   catch(DaoException e){
		   throw new ServiceException(e);
	   }
}

public void verificarSetorUsuario(Processo processo, Setor setor,
		   VerificadorPerfilService verificadorPerfilService)
				   throws AndamentoNaoAutorizadoException,
				   ProcessoOutraRelatoriaException, ServiceException {

	   // Processos distribuídos.
	   if (processoService.isProcessoDistribuido(processo)) {

		   Long idSetorProcesso = deslocaProcessoService.pesquisarSetorUltimoDeslocamento(processo);

		   // O usuário não está lotado no setor do último deslocamento do
		   // processo?
		   if (idSetorProcesso != null && !idSetorProcesso.equals(setor.getId())) { 
			   if (!verificadorPerfilService.isUsuarioRegistrarAndamentoDistribuidoForaSetor()) {
				   throw new LancamentoIndevidoException(
						   "Andamento não autorizado. O processo " + processo.getSiglaClasseProcessual() + "/"+ processo.getNumeroProcessual() +" não está deslocado para o setor do usuário atual. O usuário não tem autorização para registrar andamentos aos processos que não se encontram no seu setor fisicamente.");
			   }
		   }

	   } else { // Processos não distribuídos.
		   if (!verificadorPerfilService.isUsuarioRegistrarAndamentoNaoDistribuido()) {
			   throw new ServiceException(
					   "Andamento não autorizado. O usuário não tem autorização para registrar andamentos aos processos que não foram distribuídos.");
		   }
	   }
   }

   public void verificarMinistroRelatorAposentado(Processo processo) throws MinistroRelatorAposentadoException {
      Ministro ministro = processo.getMinistroRelatorAtual();
      if (ministro != null && ministro.getDataAfastamento() != null) {
         throw new MinistroRelatorAposentadoException("Relator do processo aposentado.");
		}
	}

   public void verificarSituacaoProcesso(Processo processo) throws ServiceException {

		boolean permiteAndamento = processo.getSituacao() != SituacaoProcesso.PROTOCOLADO
				&& processo.getSituacao() != SituacaoProcesso.CADASTRAMENTO
				&& processo.getSituacao() != SituacaoProcesso.CHAMADA;

      if (!permiteAndamento) {
       //  if (processo.getSituacao() == SituacaoProcesso.PROTOCOLADO || processo.getSituacao() == SituacaoProcesso.CADASTRAMENTO) {
       //     throw new ProcessoNaoAutuadoException("O processo " + processo.getIdentificacao() + " não está Autuado!");
        // }
            
         if (processo.getSituacao() == SituacaoProcesso.CHAMADA) {
            throw new ServiceException("Processo sendo preparado para Distribuição!");
         }
            
      }
	}

	@Override
   public List<AndamentoProcesso> pesquisarAndamentoProcesso(String sigla, Long numero) throws ServiceException {

		try {
			//dao.flushSession();
			return dao.pesquisarAndamentoProcesso(sigla, numero);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void alterarAndamento(AndamentoProcesso ap) throws ServiceException {

		try {
			dao.alterarAndamento(ap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String getSituacaoAndamento(AndamentoProcesso andamentoProcesso) throws ServiceException {

      AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamentoProcesso.getTipoAndamento());
      return andamentoProcessoHandler.getSituacao(andamentoProcesso);
	}

	public void setAndamentoHandlers(Map<String, AndamentoProcessoHandler> andamentoHandlers) {
		this.andamentoHandlers = andamentoHandlers;
	}

	public Map<String, AndamentoProcessoHandler> getAndamentoHandlers() {
		return andamentoHandlers;
	}

	public void verificarLancamentoIndevido(AndamentoProcesso andamentoProcesso)
			throws ServiceException {
      AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamentoProcesso.getTipoAndamento());
      andamentoProcessoHandler.verificarLancamentoIndevido(andamentoProcesso);
	}

	private String formataDataPtBR(Date data){
		return new SimpleDateFormat("dd/MM/yyyy").format(data);
	}
	
	public AndamentoProcesso salvarAndamentoIndevido(
			Processo processoAndamento, AndamentoProcesso andamentoProcesso,
			Setor setor, String codigoUsuario,
			AndamentoProcesso ultimoAndamento, String observacao,
			String observacaoInterna, ObjetoIncidente<?> objetoIncidente)
			throws ServiceException, LancamentoIndevidoException {

      AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamentoProcesso.getTipoAndamento());

      andamentoProcessoHandler.verificarLancamentoIndevido(andamentoProcesso);
      andamentoProcessoHandler.verificarLancamentoDispositivo(andamentoProcesso);

      Andamento andamentoInvalido = andamentoService.recuperarPorId(Andamentos.LANCAMENTO_INDEVIDO.getId());
      String observacaoCompleta = formataDataPtBR(andamentoProcesso.getDataAndamento()) + " - " + andamentoProcesso.getTipoAndamento().getDescricao()
            + "\nJustificativa: " + observacao;

		AndamentoProcesso andamentoProcessoIndevido = criarAndamentoProcesso(
				andamentoInvalido, 
				(Processo) andamentoProcesso.getObjetoIncidente().getPrincipal(), 
				setor,
				codigoUsuario, 
				andamentoProcesso.getNumeroSequencia(), 
				null,
				null, 
				null, 
				ultimoAndamento, 
				observacaoCompleta,
				observacaoInterna, 
				objetoIncidente);

		persistirAndamentoProcesso(andamentoProcessoIndevido);

      andamentoProcesso.setLancamentoIndevido(true);
      andamentoProcesso.setUltimoAndamento(false);
      persistirAndamentoProcesso(andamentoProcesso);

      processoDependenciaService.finalizarDependenciasProcesso(andamentoProcesso, andamentoProcessoIndevido);

      andamentoProcessoHandler.lancarAndamentoIndevido(processoAndamento, andamentoProcesso, andamentoProcessoIndevido);

		return andamentoProcessoIndevido;
	}

	private AndamentoProcesso criarAndamentoProcesso(Andamento tipoAndamento,
			Processo processo, Setor setor, String codigoUsuario,
			Long numeroSeqErrado, Long idTipoDevolucao,
			Long idPresidenteInterino, Long idOrigemDecisao,
			AndamentoProcesso ultimoAndamento, String observacao,
			String observacaoInterna, ObjetoIncidente<?> objetoIncidente)
			throws ServiceException {

		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();

		Date data = new Date();

		andamentoProcesso.setTipoAndamento(tipoAndamento);
		andamentoProcesso.setCodigoAndamento(tipoAndamento.getId());
		andamentoProcesso.setSetor(setor);
		andamentoProcesso.setCodigoUsuario(codigoUsuario.toUpperCase());
		andamentoProcesso.setDataAndamento(data);
		andamentoProcesso.setSigClasseProces(processo.getSiglaClasseProcessual());
		andamentoProcesso.setNumProcesso(processo.getNumeroProcessual());
		andamentoProcesso.setDataHoraSistema(data);
		andamentoProcesso.setDescricaoObservacaoAndamento(observacao);
		andamentoProcesso.setDescricaoObservacaoInterna(observacaoInterna);
		andamentoProcesso.setNumeroSequencia(recuperarProximoNumeroSequencia(processo));
		andamentoProcesso.setNumeroSequenciaErrado(numeroSeqErrado);
		andamentoProcesso.setUltimoAndamento(true);
		andamentoProcesso.setLancamentoIndevido(false);
		andamentoProcesso.setObjetoIncidente(objetoIncidente);
		andamentoProcesso.setUsuarioAlteracao(codigoUsuario);
		andamentoProcesso.setUsuarioInclusao(codigoUsuario);
		andamentoProcesso.setDataAlteracao(data);
		andamentoProcesso.setDataInclusao(data);

		OrigemAndamentoDecisao origemDecisao = getOrigemDecisao(tipoAndamento,idOrigemDecisao, setor);
		andamentoProcesso.setOrigemAndamentoDecisao(origemDecisao);

      if (objetoIncidente != null && objetoIncidente.getTipoObjetoIncidente() != null
            && objetoIncidente.getTipoObjetoIncidente().equals(TipoObjetoIncidente.RECURSO)) {
         RecursoProcesso rp = recursoProcessoService.recuperarPorId(objetoIncidente.getId());
         andamentoProcesso.setRecurso(rp.getCodigoRecurso());
      } else {
         andamentoProcesso.setRecurso(0L);
      }
		
      if (idPresidenteInterino != null) {

         // Se ambas origem decisão e presidente interino for 1-Presidência,
         // deixar o presidente
         // interino como null.
         if (!origemDecisao.getId().equals(OrigemAndamentoDecisao.ConstanteOrigemDecisao.PRESIDENCIA.getCodigo())
               || !idPresidenteInterino.equals(Ministro.COD_MINISTRO_PRESIDENTE)) {

            andamentoProcesso.setPresidenteInterino(ministroService.recuperarPorId(idPresidenteInterino));
         }
      }

		if (idTipoDevolucao != null)
			andamentoProcesso.setTipoDevolucao(tipoDevolucaoService
					.recuperarPorId(idTipoDevolucao));

		return andamentoProcesso;
	}

   public void cancelarLancamentoAndamentoIndevido(AndamentoProcesso andamentoProcessoIndevido, List<AndamentoProcesso> andamentosProcesso) throws ServiceException, LancamentoIndevidoException {

		// Andamento a ser recuperado.
		AndamentoProcesso andamentoProcesso = andamentosProcesso.get((int) (andamentoProcessoIndevido.getNumeroSequenciaErrado() - 1));

      AndamentoProcessoHandler andamentoHandler = getAndamentoProcessoHandler(andamentoProcesso.getTipoAndamento());
      andamentoHandler.verificarCancelamentoAndamentoIndevido();

		// Desfazer de trás pra frente.
		processoDependenciaService.reabrirDependenciasProcesso(andamentoProcesso);
		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamentoProcesso.getTipoAndamento());
		andamentoProcessoHandler.cancelarAndamentoIndevido(andamentoProcesso);


		// Excluindo o andamento indevido.
		andamentoProcessoIndevido.setNumeroSequenciaErrado(null);
		andamentosProcesso.remove(andamentoProcessoIndevido);
		excluir(andamentoProcessoIndevido);

		andamentoProcesso.setLancamentoIndevido(false);
		persistirAndamentoProcesso(andamentoProcesso);

		// Atualizando o sequencial dos andamentos.
		for (long i = 0; i < andamentosProcesso.size(); i++) {

			AndamentoProcesso andamento = andamentosProcesso.get((int) i);
			// Se houve mudança no sequencial após a remoção do andamento
			// indevido.
			if (andamento.getNumeroSequencia() != i + 1) {
				andamento.setNumeroSequencia(i + 1);
				persistirAndamentoProcesso(andamento);
			}
		}

		// Atualizando a flag do último andamento do processo.
		AndamentoProcesso ultimoAndamento = andamentosProcesso.get(andamentosProcesso.size() - 1);
		if (!ultimoAndamento.getUltimoAndamento()) {
			ultimoAndamento.setUltimoAndamento(true);
			persistirAndamentoProcesso(ultimoAndamento);
		}
	}

	public boolean precisaProcessoPrincipal(Andamento andamento) {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		return andamentoProcessoHandler.precisaProcessoPrincipal();
	}

	@Override
	public boolean precisaProcessosPrincipais(Andamento andamento) {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		return andamentoProcessoHandler.precisaProcessosPrincipais();
	}

	@Override
	public boolean precisaPeticao(Andamento andamento) {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		return andamentoProcessoHandler.precisaPeticao();
	}

	@Override
	public boolean precisaOrigemDecisao(Andamento andamento, Setor setor) throws ServiceException {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		// Se o setor do usuário for um setor com origem de decisão, não é
		// necessário verificar se o
		// andamento é de decisão, pois nesse caso a origem de decisão vai ser
		// do próprio setor.
		// Exceto para 7600,7601, grupos 9 e 53(Grupos de pedido de vista), onde
		// a seleção de origem decisão é obrigatória.
		if (andamentoProcessoHandler.isOrigemDecisaoObrigatoriaIndependenteSetorComDecisao(andamento)) {
			return true;
		} else {

			OrigemAndamentoDecisao origemDecisao = origemAndamentoDecisaoService
					.pesquisarOrigemDecisao(setor);

			if (origemDecisao == null) {
				return andamentoProcessoHandler.precisaOrigemDecisao(andamento);
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean precisaPresidenteInterino(Long idOrigemDecisao, Setor setor, Andamento andamento) throws ServiceException {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		OrigemAndamentoDecisao origemDecisao = getOrigemDecisao(andamento, idOrigemDecisao, setor);
		boolean isPresidencia = origemDecisao == null ? false : OrigemAndamentoDecisao.ConstanteOrigemDecisao.PRESIDENCIA.getCodigo().equals(origemDecisao.getId());

		return isPresidencia;
	}

	@Override
	public List<OrigemAndamentoDecisao> getOrigensDecisao(Andamento andamento) throws ServiceException {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		return andamentoProcessoHandler.getOrigensDecisao(andamento);
	}

	private AndamentoProcessoHandler getAndamentoProcessoHandler(Andamento andamento) {

		AndamentoProcessoHandler andamentoProcessoHandler = andamentoHandlers.get(andamento.getId().toString());
		if (andamentoProcessoHandler == null)
			andamentoProcessoHandler = new AndamentoProcessoHandler(
					origemAndamentoDecisaoService, andamentoService,
					recursoProcessoService, this);

		return andamentoProcessoHandler;
	}

	@Override
	public boolean precisaTipoDevolucao(Andamento andamento) {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		return andamentoProcessoHandler.precisaTipoDevolucao();
	}

	@Override
	public boolean precisaConfirmacaoLancarAndamento(Andamento andamento, Processo processoAndamento) throws ServiceException {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		return andamentoProcessoHandler.precisaConfirmacaoLancarAndamento(processoAndamento);
	}

	@Override
	public String getMensagemConfirmacao(Andamento andamento) {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		return andamentoProcessoHandler.getMensagemConfirmacao();
	}

	/**
	 * Método que permite salvar os andamentos em lote
	 * 
	 * @param andamentoProcessoInfo
	 * @param processosSelecionados
	 * @return
	 * @throws ServiceException
	 */
   public void salvarAndamentoParaVariosProcessos(AndamentoProcessoInfo andamentoProcessoInfo, List<Processo> processosSelecionados) throws ServiceException {
      for (Processo processo : processosSelecionados) {
         salvarAndamento(andamentoProcessoInfo, processo, processo);
      }

	}

	@Override
	public AndamentoProcesso salvarAndamento(AndamentoProcessoInfo andamentoProcessoInfo, Processo processoAndamento, ObjetoIncidente<?> objetoIncidente) throws ServiceException {

		List<Processo> processosPrincipais = andamentoProcessoInfo.getProcessosPrincipais();
		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamentoProcessoInfo.getAndamento());

		AndamentoProcesso andamentoProcesso = criarAndamentoProcesso(
				andamentoProcessoInfo.getAndamento(), processoAndamento,
				andamentoProcessoInfo.getSetor(),
				andamentoProcessoInfo.getCodigoUsuario(), null,
				andamentoProcessoInfo.getIdTipoDevolucao(),
				andamentoProcessoInfo.getIdPresidenteInterino(),
				andamentoProcessoInfo.getIdOrigemDecisao(),
				andamentoProcessoInfo.getUltimoAndamento(),
				andamentoProcessoInfo.getObservacao(),
				andamentoProcessoInfo.getObservacaoInterna(), objetoIncidente);

		/*
		 * ATENÇÃO DESENVOLVEDOR: se precisar alteração a quantidade de parâmetos do preRegistroAndamento
		 * considerar que as classes tipo HANDLER podem ter esse método sobrescrito com ações específicas.
		 * É necessário, neste caso, alterar a assinatura do método nessas classes a fim de que as mesmas 
		 * sejam executadas no registro de andamento.
		 */
		andamentoProcessoHandler.preRegistroAndamento(andamentoProcesso,
				processoAndamento, processosPrincipais,
				andamentoProcessoInfo.getPeticao(),
				andamentoProcessoInfo.getSetor(),
				andamentoProcessoInfo.getCodigoUsuario(),
				andamentoProcessoInfo.getOrigem());

		persistirAndamentoProcesso(andamentoProcesso);

		finalizarSobrestamentos(andamentoProcessoInfo.getAndamento(),
				processoAndamento, andamentoProcesso);

		/*
		 * ATENÇÃO DESENVOLVEDOR: se precisar alteração a quantidade de parâmetos do posRegistroAndamento
		 * considerar que as classes tipo HANDLER podem ter esse método sobrescrito com ações específicas.
		 * É necessário, neste caso, alterar a assinatura do método nessas classes a fim de que as mesmas 
		 * sejam executadas no registro de andamento.
		 */
		andamentoProcessoHandler.posRegistroAndamento(andamentoProcesso,
				processoAndamento, processosPrincipais,
				andamentoProcessoInfo.getPeticao(),
				andamentoProcessoInfo.getSetor(),
				andamentoProcessoInfo.getCodigoUsuario(),
				andamentoProcessoInfo.getOrigem(),
				andamentoProcessoInfo.getComunicacao());

		if (processosPrincipais != null) {
			for (Processo processo : processosPrincipais) {
				processoService.salvar(processo);
			}
		}
		try {
		   processoService.salvar(processoAndamento);
		} catch(Exception e){
		   e.printStackTrace();
		}
		
		if(andamentoProcessoInfo.getProcessosTemas() != null && !andamentoProcessoInfo.getProcessosTemas().isEmpty()){
			try{
				List<TipoOcorrencia> listaTipoOcorrencia = temaService.pesquisarTipoOcorrencia(TipoOcorrenciaConstante.PROCESSO_RELACIONADO_A_TEMA_PARA_DEVOLUCAO.getCodigo(), null);
				
				for(ProcessoTema pt : andamentoProcessoInfo.getProcessosTemas()){
					Tema tema = temaService.recuperarPorId(pt.getTema().getId());
					tema.getProcessosTema().iterator();
					for(Processo p : processosPrincipais){
						boolean adicionarProcessoTema = true;
						for(ProcessoTema ptAux : tema.getProcessoRelacionados()){
							if(ptAux.getObjetoIncidente().getId().equals(p.getId())){
								adicionarProcessoTema = false;
							}
						}
						if(adicionarProcessoTema){						
							ProcessoTema proc = new ProcessoTema();
							proc.setDataOcorrencia(Calendar.getInstance().getTime());
							proc.setTema(pt.getTema());						
							proc.setTipoOcorrencia(listaTipoOcorrencia.get(0));
							proc.setObjetoIncidente(p);
							tema.getProcessosTema().add(proc);						
							temaService.alterar(tema);
						}
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return andamentoProcesso;
	}
	
	@Override
	public String salvarAndamentoBaixa(List<ContainerGuiaProcessos> containerDeGuias) throws ServiceException {
		String numAnoGuia = null;
		
		//Recupera o Handle de baixa de processos.
		AndamentoProcessoHandler andamentoProcessoHandler = andamentoHandlers.get("7101");
		
		//Para cada guia e seus respectivos processos executa o seguinte
		for (ContainerGuiaProcessos containerGuia : containerDeGuias) {
			
			//Faz um loop para criar todos os andamentosProcesso
			for (ProcessoEAndamentoProcesso pap : containerGuia.getProcessosEAndamentosProcessos()){

				//Recuperar dados de apoio
				AndamentoProcessoInfo andamentoProcessoInfo = pap.getAndamentoProcessoInfo();
				
				//Monta o andamentoProcesso
				AndamentoProcesso andamentoProcesso = criarAndamentoProcesso(
						andamentoProcessoInfo.getAndamento(), pap.getProcesso(),
						andamentoProcessoInfo.getSetor(),
						andamentoProcessoInfo.getCodigoUsuario(), null,
						andamentoProcessoInfo.getIdTipoDevolucao(),
						andamentoProcessoInfo.getIdPresidenteInterino(),
						andamentoProcessoInfo.getIdOrigemDecisao(),
						andamentoProcessoInfo.getUltimoAndamento(),
						andamentoProcessoInfo.getObservacao(),
						andamentoProcessoInfo.getObservacaoInterna(), pap.getIncidenteSelecionado());
				persistirAndamentoProcesso(andamentoProcesso);
				pap.setAndamentoProcesso(andamentoProcesso);
				
				List<Processo> processosPrincipais = andamentoProcessoInfo.getProcessosPrincipais();
				
				if(andamentoProcessoInfo.getProcessosTemas() != null && !andamentoProcessoInfo.getProcessosTemas().isEmpty()){
					try{
						List<TipoOcorrencia> listaTipoOcorrencia = temaService.pesquisarTipoOcorrencia(TipoOcorrenciaConstante.PROCESSO_RELACIONADO_A_TEMA_PARA_DEVOLUCAO.getCodigo(), null);
						
						for(ProcessoTema pt : andamentoProcessoInfo.getProcessosTemas()){
							Tema tema = temaService.recuperarPorId(pt.getTema().getId());
							tema.getProcessosTema().iterator();
							for(Processo p : processosPrincipais){
								boolean adicionarProcessoTema = true;
								for(ProcessoTema ptAux : tema.getProcessoRelacionados()){
									if(ptAux.getObjetoIncidente().getId().equals(p.getId())){
										adicionarProcessoTema = false;
									}
								}
								if(adicionarProcessoTema){						
									ProcessoTema proc = new ProcessoTema();
									proc.setDataOcorrencia(Calendar.getInstance().getTime());
									proc.setTema(pt.getTema());						
									proc.setTipoOcorrencia(listaTipoOcorrencia.get(0));
									proc.setObjetoIncidente(p);
									tema.getProcessosTema().add(proc);						
									temaService.alterar(tema);
								}
							}
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			numAnoGuia = andamentoProcessoHandler.posRegistroAndamento(containerGuia);
		}
		return numAnoGuia;
	}
	

	private void finalizarSobrestamentos(Andamento andamento,
			Processo processoAndamento, AndamentoProcesso andamentoProcesso)
			throws ServiceException {

		if (isAndamentoDecisaoFinal(andamento)
				&& !possuiAndamentoJulgamentoOuMerito(processoAndamento)) {
			processoDependenciaService
					.finalizarSobrestamentos(andamentoProcesso);
		}
	}

	private boolean isAndamentoDecisaoFinal(Andamento andamento) {
		Long grupoAndamento = andamento.getGrupoAndamento();
		return grupoAndamento != null
				&& grupoAndamento == GrupoAndamento.GruposAndamento.DECISAO_FINAL
						.getCodigo();
	}

	private boolean possuiAndamentoJulgamentoOuMerito(Processo processoAndamento)
			throws ServiceException {

		try {
			return dao.possuiAndamentoJulgamentoOuMerito(processoAndamento);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Verifica se o andamento é de decisão e atribui a origem da decisão ao
	 * andamento do processo.
	 * 
	 * @throws ServiceException
	 */
	private OrigemAndamentoDecisao getOrigemDecisao(Andamento andamento,
			Long idOrigemDecisao, Setor setor) throws ServiceException {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);
		OrigemAndamentoDecisao origemDecisao = null;
		if (idOrigemDecisao == null) {
			if (andamentoProcessoHandler.precisaOrigemDecisao(andamento)) {
				origemDecisao = origemAndamentoDecisaoService
						.pesquisarOrigemDecisao(setor);
			}
		} else {
			origemDecisao = origemAndamentoDecisaoService
					.recuperarPorId(idOrigemDecisao);
		}
		return origemDecisao;
	}

	@Override
	public void verificarPossibilidadeApensamento(Processo processoAndamento,
			Processo processoPrincipal) throws ServiceException {

		// Verificar se esse processo já foi apensado.
		if (processoDependenciaService.isProcessoApensado(processoAndamento,processoPrincipal)) {
			StringBuffer sb = new StringBuffer();
			sb.append("O processo '");
			sb.append(processoAndamento.getSiglaClasseProcessual()).append(" ");
			sb.append(processoAndamento.getNumeroProcessual());
			sb.append("' já foi apensado ao processo '");
			sb.append(processoPrincipal.getSiglaClasseProcessual()).append(" ");
			sb.append(processoPrincipal.getNumeroProcessual()).append("'.");
			throw new ProcessoApensadoAOutroException(sb.toString());
		}
		
		if (processoAndamento.getId().longValue() == processoPrincipal.getId().longValue()) {
			throw new AndamentoNaoAutorizadoException("Não é possível apensar um processo a ele mesmo.");
		}

		Long idSetorProcessoAndamento = deslocaProcessoService.pesquisarSetorUltimoDeslocamento(processoAndamento);
		Long idSetorProcessoPrincipal = deslocaProcessoService.pesquisarSetorUltimoDeslocamento(processoPrincipal);

		if (idSetorProcessoAndamento == null || idSetorProcessoPrincipal == null || !idSetorProcessoAndamento.equals(idSetorProcessoPrincipal)) {
			StringBuffer sb = new StringBuffer();
			sb.append("Não foi possível apensar o processo  '");
			sb.append(processoAndamento.getSiglaClasseProcessual()).append(" ");
			sb.append(processoAndamento.getNumeroProcessual());
			sb.append("' ao processo '");
			sb.append(processoPrincipal.getSiglaClasseProcessual()).append(" ");
			sb.append(processoPrincipal.getNumeroProcessual()).append("', pois eles não se encontram no mesmo setor.");			
			throw new AndamentoNaoAutorizadoException(sb.toString());
		}
	}

	@Override
	public boolean podeEditarObservacao(AndamentoProcesso andamentoProcesso, String userName) {

		if (Arrays.asList(andamentosSemEdicaoObservacao).contains(andamentoProcesso.getCodigoAndamento()))
			return false;
		try {
			if (usuarioService.hasRoleEditarObservacao(userName) || AcegiSecurityUtils.isUserInRole("RS_MASTER_PROCESSAMENTO")) {
				return true;
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		Date dataAndamento = andamentoProcesso.getDataAndamento();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		Long timeAgora = calendar.getTimeInMillis();
		Long timeAndamento = dataAndamento.getTime();

		// Dois dias em milisegundos = 2 * 24 * 60 * 60 * 1000
		Long periodoMaximo = 172800000L;

		return (timeAgora - timeAndamento) < periodoMaximo;
	}

	@Override
	public boolean isLancadoPorDispositivo(AndamentoProcesso andamentoProcesso)
			throws ServiceException {

		try {
			return dao.isLancadoPorDispositivo(andamentoProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean precisaVerificarCodigoOrigem(Andamento andamento)
			throws ServiceException {

		AndamentoProcessoHandler andamentoProcessoHandler = getAndamentoProcessoHandler(andamento);

		return andamentoProcessoHandler.precisaVerificarCodigoOrigem();
	}

	@Override
	public List<AndamentoProcesso> pesquisarAvisosNaoCriados(Long andamento,
			String observacao, Boolean processoOriginario, Date dataInicial,
			Date dataFinal, Boolean andamentoExpedito, String siglaProcesso,
			Long numProcesso) throws ServiceException {
		try {
			return dao.pesquisarAvisosNaoCriados(andamento, observacao,
					processoOriginario, dataInicial, dataFinal,
					andamentoExpedito,siglaProcesso,numProcesso);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	@Override
	public AndamentoProcesso recuperarUltimoAndamentoSelecionado(
			Long numeroProcesso, String classeProcesso, Long codigoAndamento)
			throws ServiceException {
		AndamentoProcesso andamentoProcesso = null;

		try {
			andamentoProcesso = dao.recuperarUltimoAndamentoSelecionado(numeroProcesso, classeProcesso,
					codigoAndamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return andamentoProcesso;
	}
	
	@Override
	public AndamentoProcesso recuperarUltimoAndamentoSelecionadoData(Long idProcesso, Long codigoAndamento, Date dataInicio, Date dataFinal)
			throws ServiceException {
		AndamentoProcesso andamentoProcesso = null;

		try {
			andamentoProcesso = dao.recuperarUltimoAndamentoSelecionadoData(idProcesso, codigoAndamento, dataInicio, dataFinal);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return andamentoProcesso;
	}
	
	@Override
	public String recuperarObsInterna(Long idAndamentoProcesso) throws ServiceException {
		try {
			return dao.recuperarObsInterna(idAndamentoProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Long recuperarCodAndamentoPorNumeroSequencia(Processo processo, Long numeroSequenciaErrado) throws ServiceException {
		try {
			return dao.recuperarCodAndamentoPorNumeroSequencia(processo,numeroSequenciaErrado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public AndamentoProcesso gerarAndamentoBasico(Long codigoAndamento, ObjetoIncidente<?> oi, Usuario usuario, String descricaoObs,
			String descricaoObsInterna) throws ServiceException {

		Long numSeq;

		try {
			numSeq = recuperarProximoNumeroSequencia(oi);
			AndamentoProcesso andamentoProcesso = new AndamentoProcesso();

			andamentoProcesso.setCodigoAndamento(codigoAndamento);
			andamentoProcesso.setCodigoUsuario(usuario.getId().toUpperCase());
			andamentoProcesso.setDataAndamento(new Date());
			andamentoProcesso.setDataHoraSistema(new Date());
			andamentoProcesso.setSetor(usuario.getSetor());
			andamentoProcesso.setObjetoIncidente(oi);
			andamentoProcesso.setDescricaoObservacaoAndamento(descricaoObs);
			andamentoProcesso.setNumeroSequencia(numSeq);
			andamentoProcesso.setDescricaoObservacaoInterna(descricaoObsInterna);
			andamentoProcesso.setLancamentoIndevido(false);
			
			return andamentoProcesso = salvar(andamentoProcesso);

		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void excluirTodosOsAndamentos(ObjetoIncidente<?> referendo) throws ServiceException {
		List<AndamentoProcesso> andamentos = recuperarTodosAndamentos(referendo);

		try {
			for (AndamentoProcesso ap : andamentos)
				dao.excluir(ap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<AndamentoProcesso> recuperarTodosAndamentos(ObjetoIncidente<?> referendo) throws ServiceException {
		try {
			return dao.recuperarTodosAndamentos(referendo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void alterarObsAndamento(Long seqAndamento, String obs) throws ServiceException {
		try {
			 dao.alterarObsAndamento(seqAndamento, obs);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
