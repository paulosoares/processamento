package br.gov.stf.estf.jurisdicionado.model.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.EmprestimoAutosProcesso;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.TelefoneJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumSituacaoEmprestimo;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoAssociacao;
import br.gov.stf.estf.entidade.jurisdicionado.util.EmprestimoAutosResult;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.EmprestimoAutosProcessoDao;
import br.gov.stf.estf.jurisdicionado.model.exception.JurisdicionadoException;
import br.gov.stf.estf.jurisdicionado.model.service.AssociacaoJurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.EmprestimoAutosProcessoService;
import br.gov.stf.estf.jurisdicionado.model.service.JurisdicionadoService;
import br.gov.stf.estf.jurisdicionado.model.service.PapelJurisdicionadoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfoImpl;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("emprestimoAutosProcessoService")
public class EmprestimoAutosProcessoServiceImpl extends GenericServiceImpl<EmprestimoAutosProcesso, Long, EmprestimoAutosProcessoDao> implements
		EmprestimoAutosProcessoService {

	private final AndamentoService andamentoService;
	private final ObjetoIncidenteService objetoIncidenteService;
	private final AndamentoProcessoService andamentoProcessoService;
	private final ProcessoService processoService;
	private final AssociacaoJurisdicionadoService associacaoJurisdicionadoService;
	private final DeslocaProcessoService deslocaProcessoService;
	private final JurisdicionadoService jurisdicionadoService;
	private final ProcessoDependenciaService processoDependenciaService;

	public EmprestimoAutosProcessoServiceImpl(EmprestimoAutosProcessoDao dao, AndamentoService andamentoService, ObjetoIncidenteService objetoIncidenteService,
			AndamentoProcessoService andamentoProcessoService, ProcessoService processoService, PapelJurisdicionadoService papelJurisdicionadoService,
			AssociacaoJurisdicionadoService associacaoJurisdicionadoService, DeslocaProcessoService deslocaProcessoService, JurisdicionadoService jurisdicionadoService,
			ProcessoDependenciaService processoDependenciaService) {
		super(dao);
		this.andamentoService = andamentoService;
		this.objetoIncidenteService = objetoIncidenteService;
		this.andamentoProcessoService = andamentoProcessoService;
		this.processoService = processoService;
		this.associacaoJurisdicionadoService = associacaoJurisdicionadoService;
		this.deslocaProcessoService = deslocaProcessoService;
		this.jurisdicionadoService = jurisdicionadoService;
		this.processoDependenciaService = processoDependenciaService;
	}
	
	@Override
	// salvar recebimento de vários órgaos externos ou de vários advogados (um grupo de cada vez)
	public List<Guia> salvarRecebimentoDeVariasOrigens(List<DeslocaProcesso> deslocaProcessos, Usuario usuario, Boolean isAdvogado, Map<Long,String> observacoes) throws ServiceException {
		// vai segmentar um list origem por origem e para cada um chamar o método salvarRecebimentoDaMesmaOrigem() passando o list segmentado.
		int i = 0;
		Long origemAnterior = 0L;
		List<DeslocaProcesso> listaGrupoReceber = new ArrayList<DeslocaProcesso>();
		List<Guia> listaGuiasDevolucao = new ArrayList<Guia>();
		String observacao = "";
		for (DeslocaProcesso lista: deslocaProcessos) {
			i ++;
			if (i == 1){
				origemAnterior = lista.getCodigoOrgaoDestino();
			}
			if ( !origemAnterior.equals(lista.getCodigoOrgaoDestino()) ){
				observacao = observacoes.get(listaGrupoReceber.get(0).getId().getProcesso().getId());
				listaGuiasDevolucao.add( salvarRecebimentoDaMesmaOrigem(listaGrupoReceber, usuario, isAdvogado, observacao) );
				listaGrupoReceber.clear();
				origemAnterior = lista.getCodigoOrgaoDestino();
			}
			listaGrupoReceber.add( lista );
		}
		if (listaGrupoReceber.size() > 0) {
			observacao = observacoes.get(listaGrupoReceber.get(0).getId().getProcesso().getId());
			listaGuiasDevolucao.add( salvarRecebimentoDaMesmaOrigem(listaGrupoReceber, usuario, isAdvogado, observacao) );
		}
		
		return listaGuiasDevolucao;
	}
	
	private Guia salvarRecebimentoDaMesmaOrigem(List<DeslocaProcesso> listaGrupo, Usuario usuario, Boolean isAdvogado, String observacao) throws ServiceException{
		ArrayList<Long> arrayObjIncidente = new ArrayList<Long>();
		
    	// gerar coleção de seq_objeto_incidente a ser deslocada para o parâmetro do método
		for (DeslocaProcesso deslocaProcesso : listaGrupo) {
			arrayObjIncidente.add(deslocaProcesso.getId().getProcesso().getId());
		}
		Guia guia = salvarRecebimento(arrayObjIncidente, usuario, observacao, isAdvogado);
		
		return guia;
	}

	@Override
	public Guia salvarRecebimento(ArrayList<Long> listaSeqObjetosIncidentes, Usuario usuario, String observacao, boolean advogado) throws ServiceException {
		try {
			// recupera a lista de processos e seus respectivos deslocamentos do array
			List<Processo> processos = new ArrayList<Processo>();
			List<DeslocaProcesso> deslocamentos = new ArrayList<DeslocaProcesso>();
			List<Long> apensos = new ArrayList<Long>();
			for (Long seqObjetoIncidente : listaSeqObjetosIncidentes) {
				Processo processoEmprestado = processoService.recuperarPorId(seqObjetoIncidente);
				processos.add(processoEmprestado);
				deslocamentos.add(deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processoEmprestado));
				if (processoDependenciaService.isApenso(processoEmprestado)) {
					apensos.add(processoEmprestado.getId());
				}
			}
			// gera o deslocamento de retorno dos processos com recebimento automático
			Guia guia = new Guia();
			guia.setCodigoOrgaoDestino(usuario.getSetor().getId());
			guia.setTipoOrgaoDestino(2);
			
			// retirar os apensos antes do deslocamento de devolução
			listaSeqObjetosIncidentes.removeAll(apensos);
			
			String numAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, listaSeqObjetosIncidentes, true);
			if (numAnoGuia == null) {
				throw new ServiceException("Erro ao efetuar o deslocamento");
			}
			String numero_guia = numAnoGuia.substring(0, numAnoGuia.indexOf("/"));
			String ano_guia = numAnoGuia.substring(numAnoGuia.indexOf("/") + 1, numAnoGuia.length());
			guia.getId().setAnoGuia(new Short(ano_guia));
			guia.getId().setNumeroGuia(new Long(numero_guia));

			// recuperar os deslocamentos da guia gerada
			List<DeslocaProcesso> deslocamentosDevolucao = deslocaProcessoService.recuperarDeslocamentoProcessos(guia);

			// limpa a lista para acrescentar novamente todos os processos inclusive os apensos que foram deslocados.
			processos.clear();
			for (DeslocaProcesso deslocaProcesso: deslocamentosDevolucao){
				processos.add(processoService.recuperarPorId(deslocaProcesso.getId().getProcesso().getId()));
			}
			
			// gera o andamento
			salvarAndamentoRecebimento(guia, observacao, usuario, processos, deslocamentosDevolucao.get(0).getId().getCodigoOrgaoOrigem(), advogado);

			// atualizar o emprestimo - para recebimentos de advogados
			if (advogado) {

				for (DeslocaProcesso desloca : deslocamentos) {
					for (DeslocaProcesso deslocaDevolucao : deslocamentosDevolucao) {
						if (desloca.getNumeroProcesso().equals(deslocaDevolucao.getNumeroProcesso())
								&& desloca.getClasseProcesso().equals(deslocaDevolucao.getClasseProcesso())) {
							EmprestimoAutosProcesso emprestimo = recuperarEmprestimoPorDeslocamento(desloca);
							if (emprestimo != null) {
								emprestimo.setDeslocaDevolucao(deslocaDevolucao);
								dao.salvar(emprestimo);
							}
						}
					}
				}
			}
			return guia;
		} catch (Exception e) {
			throw new ServiceException("Não foi possível finalizar o recebimento.");
		}
	}
	
	private void salvarAndamentoRecebimento(Guia guia, String observacao, Usuario usuario, 
			List<Processo> processos, 
			Long codigoOrigemRecebimento, 
			Boolean advogado) throws ServiceException {
		
		AndamentoProcessoInfoImpl andamentoProcessoInfo = new AndamentoProcessoInfoImpl();
		andamentoProcessoInfo.setCodigoUsuario(usuario.getId().toUpperCase());
		andamentoProcessoInfo.setProcessosPrincipais(processos);
		andamentoProcessoInfo.setSetor(usuario.getSetor());
		andamentoProcessoInfo.setUltimoAndamento(null);
		observacao = observacao + " - Guia " + guia.getId().getNumeroGuia() + "/" + guia.getId().getNumeroGuia();
		andamentoProcessoInfo.setObservacao(observacao);
		AndamentoProcesso ap = andamentoProcessoService.recuperarUltimoAndamentoSelecionadoData(processos.get(0).getId(),7100L,null,null);
		if (!advogado && !codigoOrigemRecebimento.equals(new Long(23)) && ap == null ) {
			// Gerar andamento para recebimento de orgãos somente órgãos externos diferentes da PGR
			Andamento andamento = andamentoService.recuperarPorId(8100L);
			andamentoProcessoInfo.setAndamento(andamento);

		} else {
			// Gerar andamento para recebimento de advogados.
			Andamento andamento = andamentoService.recuperarPorId(8217L);
			andamentoProcessoInfo.setAndamento(andamento);
		}

		andamentoProcessoService.salvarAndamentoParaVariosProcessos(andamentoProcessoInfo, processos);
		
	}

	@Override
	public EmprestimoAutosProcesso recuperarEmprestimoPorDeslocamento(DeslocaProcesso deslocaProcesso) throws ServiceException {
		try {
			return dao.recuperarEmprestimoPorDeslocamento(deslocaProcesso);
		} catch (Exception e) {
			throw new ServiceException("Ocorreu um erro ao recuperar o emprestimo.");
		}
	}

	@Override
	public List<EmprestimoAutosResult> pesquisarAutos(String nomeJurisdicionado, Long objetoIncidente, Date dataInicial, Date dataFinal, Long idSituacao)
			throws ServiceException {

		List<EmprestimoAutosProcesso> emprestimos = null;

		try {
			emprestimos = dao.pesquisarAutos(nomeJurisdicionado, objetoIncidente, dataInicial, dataFinal, idSituacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		List<EmprestimoAutosResult> listaEmprestimoAutosSituacao = new LinkedList<EmprestimoAutosResult>();

		for (EmprestimoAutosProcesso emp : emprestimos) {

			EmprestimoAutosResult result = new EmprestimoAutosResult();
			result.setEmprestimoAutosProcesso(emp);

			try {
				if (emp.getDataDevolucaoPrevista() != null) {
					SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
					if ((emp.getDataDevolucaoPrevista().compareTo(sd.parse(sd.format(new Date()))) < 0) && emp.getDeslocaDevolucao() == null) {
						result.setSituacaoEmprestimo(EnumSituacaoEmprestimo.EM_ATRASO);
					} else if ((emp.getDataDevolucaoPrevista().compareTo(sd.parse(sd.format(new Date()))) == 0) && emp.getDeslocaDevolucao() == null) {
						result.setSituacaoEmprestimo(EnumSituacaoEmprestimo.DIA_DEVOLUCAO);
					} else if ((emp.getDataDevolucaoPrevista().compareTo(sd.parse(sd.format(new Date()))) > 0) && emp.getDeslocaDevolucao() == null) {
						result.setSituacaoEmprestimo(EnumSituacaoEmprestimo.EM_CURSO);
					}
				}

				if (emp.getDeslocaDevolucao() != null) {
					SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
					if (sd.parse(sd.format(emp.getDeslocaDevolucao().getDataRecebimento())).compareTo(emp.getDataDevolucaoPrevista()) <= 0) {
						result.setSituacaoEmprestimo(EnumSituacaoEmprestimo.DEVOLVIDO);
					} else if (sd.parse(sd.format(emp.getDeslocaDevolucao().getDataRecebimento())).compareTo(emp.getDataDevolucaoPrevista()) > 0) {
						result.setSituacaoEmprestimo(EnumSituacaoEmprestimo.DEVOLVIDO_ATRASO);
					}
				}

				StringBuffer buffer = new StringBuffer();
				if (emp.getPapelJurisdicionado() != null) {
					if (emp.getPapelJurisdicionado().getJurisdicionado().getTelefonesJurisdicionado() != null) {
						for (TelefoneJurisdicionado tel : emp.getPapelJurisdicionado().getJurisdicionado().getTelefonesJurisdicionado()) {
							if(tel.getTipoTelefone() != null){
								buffer.append(tel.getTipoTelefone().getDescricao() + ": ");
							} else {
								buffer.append("Telefone: ");	
							}
							buffer.append(tel.getDDD() + " " + tel.getNumero() + " ");
						}
					}
				} else if (emp.getAssociacaoJurisdicionado().getMembro().getJurisdicionado() != null) {
					if (emp.getAssociacaoJurisdicionado().getMembro().getJurisdicionado().getTelefonesJurisdicionado() != null) {
						for (TelefoneJurisdicionado tel : emp.getAssociacaoJurisdicionado().getMembro().getJurisdicionado().getTelefonesJurisdicionado()) {
							if(tel.getTipoTelefone() != null){
								buffer.append(tel.getTipoTelefone().getDescricao() + ": ");
							} else {
								buffer.append("Telefone: ");	
							}
							buffer.append(tel.getDDD() + " " + tel.getNumero() + " ");
						}
					}
				}
				result.setContatosJurisdicionado(buffer.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (idSituacao != null) {
				if (result.getSituacaoEmprestimo().getCodigo().equals(idSituacao)) {
					listaEmprestimoAutosSituacao.add(result);
				}
			} else {
				listaEmprestimoAutosSituacao.add(result);
			}
		}

		return listaEmprestimoAutosSituacao;
	}

	@Override
	public Boolean existeEmprestimoParaAssociacao(AssociacaoJurisdicionado associacao) throws ServiceException {
		try {
			return dao.existeEmprestimoParaAssociacao(associacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void excluirCarga(EmprestimoAutosResult emp, Setor setorAutenticado, Usuario usuario) throws ServiceException, SQLException, JurisdicionadoException {

		Long idJurisdicionado = null;
		Guia guia = new Guia();
		GuiaId guiaId = new GuiaId();

		if (emp.getEmprestimoAutosProcesso().getPapelJurisdicionado()!= null) {
			idJurisdicionado = emp.getEmprestimoAutosProcesso().getPapelJurisdicionado().getJurisdicionado().getId();
		} else {
			idJurisdicionado = emp.getEmprestimoAutosProcesso().getAssociacaoJurisdicionado().getMembro().getJurisdicionado().getId();
		}

		if (emp.getEmprestimoAutosProcesso().getDeslocaRetirada().getAndamentoProcesso().getLancamentoIndevido() == true) {
			throw new JurisdicionadoException("Não é possível invalidar este empréstimo, pois o mesmo já está invalidado.");
		}

		guiaId.setCodigoOrgaoOrigem(idJurisdicionado); // aqui deverá ser o codigo do advogado
		guia.setId(guiaId);
		guia.setTipoOrgaoOrigem(1); // advgogado
		guia.setCodigoOrgaoDestino(setorAutenticado.getId()); // setor do usuário que está recebendo de volta.
		guia.setTipoOrgaoDestino(2); // stf

		ArrayList<Long> arrayObjIncidente = new ArrayList<Long>();
		arrayObjIncidente.add(emp.getEmprestimoAutosProcesso().getDeslocaRetirada().getId().getProcesso().getId());
		String numAnoGuia = null;

		try {
			numAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, arrayObjIncidente, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (numAnoGuia == null) {
			throw new ServiceException("Erro ao efetuar o deslocamento");
		}

		// AndamentoProcesso ultimoAndamentoProcesso = new AndamentoProcesso();

		try {
			andamentoProcessoService.salvarAndamentoIndevido(emp.getEmprestimoAutosProcesso().getDeslocaRetirada().getId().getProcesso(), emp
					.getEmprestimoAutosProcesso().getDeslocaRetirada().getAndamentoProcesso(), setorAutenticado, usuario.getId(), null,
					"Carga invalidada pelo Sistema estf-Processamento", "Carga invalidada pelo Sistema estf-Processamento", emp.getEmprestimoAutosProcesso()
							.getDeslocaRetirada().getId().getProcesso());
		} catch (Exception e) {
			throw new ServiceException("Erro ao salvar o andamento indevido.", e);
		}

		try {
			dao.excluir(emp.getEmprestimoAutosProcesso());
		} catch (Exception e) {
			throw new ServiceException("Erro ao excluir o empréstimo.");
		}

	}

	public void salvarCobranca(EmprestimoAutosResult emp, Setor setorAutenticado, Usuario usuario, String observacao) throws ServiceException, SQLException {

		EmprestimoAutosProcesso emprestimoAutosProcesso = new EmprestimoAutosProcesso();
		emprestimoAutosProcesso = emp.getEmprestimoAutosProcesso();
		if (emprestimoAutosProcesso.getQuantidadeCobrancaDevolucao() != null) {
			emprestimoAutosProcesso.setQuantidadeCobrancaDevolucao(emprestimoAutosProcesso.getQuantidadeCobrancaDevolucao() + 1);
		} else {
			emprestimoAutosProcesso.setQuantidadeCobrancaDevolucao(1);
		}

		// atualiza o banco com o incremento na quantidade de cobranças
		try {
			dao.alterar(emprestimoAutosProcesso);
		} catch (Exception e) {
			throw new ServiceException("Erro ao alterar a quatidade de cobranças.");
		}

		AndamentoProcessoInfoImpl andamentoProcessoInfo = new AndamentoProcessoInfoImpl();
		// código 8214 - Cobrada a devolução dos autos
		Andamento andamento = new Andamento();

		try {
			andamento = andamentoService.recuperarPorId(new Long("8214"));
		} catch (Exception e) {
			throw new ServiceException("Erro recuperar o andamento de cobrança");
		}

		andamentoProcessoInfo.setAndamento(andamento);
		andamentoProcessoInfo.setCodigoUsuario(usuario.getId().toUpperCase());
		andamentoProcessoInfo.setObservacao(observacao);
		andamentoProcessoInfo.setSetor(setorAutenticado);

		Processo processoAndamento = new Processo();

		try {
			processoAndamento = processoService.recuperarProcesso(emprestimoAutosProcesso.getDeslocaRetirada().getClasseProcesso(), emprestimoAutosProcesso
					.getDeslocaRetirada().getNumeroProcesso());
		} catch (Exception e) {
			throw new ServiceException("Erro recuperar o andamento processo.");
		}

		List<Processo> processosPrincipais = new ArrayList<Processo>();
		processosPrincipais.add(processoAndamento);
		andamentoProcessoInfo.setProcessosPrincipais(processosPrincipais);

		ObjetoIncidente<?> objetoIncidente = null;

		try {
			objetoIncidente = objetoIncidenteService.recuperarPorId(processoAndamento.getId());
		} catch (Exception e) {
			throw new ServiceException("Erro recuperar o objeto incidente pesquisado.");
		}

		try {
			andamentoProcessoService.salvarAndamento(andamentoProcessoInfo, processoAndamento, objetoIncidente);
		} catch (Exception e) {
			throw new ServiceException("Erro ao gerar o andamento de cobrança.");
		}
	}

	@Override
	public void excluirCobranca(EmprestimoAutosResult emp, Setor setorAutenticado, Usuario usu) throws ServiceException, JurisdicionadoException {

		EmprestimoAutosProcesso emprestimoAutosProcesso = new EmprestimoAutosProcesso();
		emprestimoAutosProcesso = emp.getEmprestimoAutosProcesso();

		if (emprestimoAutosProcesso.getQuantidadeCobrancaDevolucao() == null || emprestimoAutosProcesso.getQuantidadeCobrancaDevolucao() == 0) {
			throw new JurisdicionadoException("Não é possivel reduzir o valor se o atributo é zero.");
		}

		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		AndamentoProcesso ultimoAndamentoProcesso = new AndamentoProcesso();

		try {
			andamentoProcesso = andamentoProcessoService.recuperarUltimoAndamentoSelecionado(emp.getEmprestimoAutosProcesso().getDeslocaRetirada()
					.getNumeroProcesso(), emp.getEmprestimoAutosProcesso().getDeslocaRetirada().getClasseProcesso(), 8214L);
			ultimoAndamentoProcesso = andamentoProcessoService.recuperarUltimoAndamento(emp.getEmprestimoAutosProcesso().getDeslocaRetirada().getId()
					.getProcesso());
		} catch (Exception e) {
			throw new ServiceException("Erro ao recuperar o último andamento.");
		}

		try {
			andamentoProcessoService.salvarAndamentoIndevido(emp.getEmprestimoAutosProcesso().getDeslocaRetirada().getId().getProcesso(), andamentoProcesso,
					setorAutenticado, usu.getId(), ultimoAndamentoProcesso, "Cobrança invalidada pelo sistema estf-Processamento",
					"Cobrança invalidada pelo sistema estf-Processamento", emp.getEmprestimoAutosProcesso().getDeslocaRetirada().getId().getProcesso());
		} catch (Exception e) {
			throw new ServiceException("Erro ao salvar o andamento indevido.");
		}

		emprestimoAutosProcesso.setQuantidadeCobrancaDevolucao(emprestimoAutosProcesso.getQuantidadeCobrancaDevolucao() - 1);

		// atualiza o banco com o incremento na quantidade de cobranças
		try {
			dao.alterar(emprestimoAutosProcesso);
		} catch (Exception e) {
			throw new ServiceException("Erro ao alterar a quatidade de cobranças.");
		}

	}

	/**
	 * @author Ricardo Zonta Leão
	 * 
	 *         Este método realiza a persistência da carga que consiste em:
	 *         - salvar empréstimo
	 *         - salvar deslocamento
	 *         - salvar associações
	 *         - salvar andamento - código 8209
	 * @param emprestimo
	 *            - objeto do tipo EmprestimoAutosProcesso com as informações em comum para gravação do(s) emprestimo(s) (um para cada deslocamento)
	 *            - dataDevolucaoPrevista - data da devolução que poderá ser persistida em vários empréstimo
	 *            - papelJurisdicionado - papel do jurisdicionado para o qual será efetuado o empréstimo (o autorizador)
	 *            será null se existir associacaoJurisdicionado e vice-versa.
	 *            - associacaoJurisdicionado - associação para o qual será efetuado o empréstimo (o autorizado)
	 *            será null se existir papelJurisdicionado e vice-versa.
	 * @param guia
	 *            - objeto guia com algumas informações para a realização do deslocamento: codigoOrgaoOrigem e codigoOrgaoDestino.
	 * @param listaSeqObjetosIncidentes
	 *            - lista de sequences de objetos incidentes para o deslocamento
	 * @param associacaoParaIncluir
	 *            - novas associações para inclusão
	 * @param associacaoParaExcluir
	 *            - associações maracadas para exclusão
	 * 
	 * @return String: "<numero guia>/<ano guia>"
	 */
	@Override
	public GuiaId salvarCarga(EmprestimoAutosProcesso emprestimo, Guia guia, ArrayList<Long> listaSeqObjetosIncidentes,
			List<AssociacaoJurisdicionado> associacaoParaIncluir, List<AssociacaoJurisdicionado> associacaoParaExcluir, Usuario usuario)
			throws ServiceException {

		salvarVinculo(associacaoParaIncluir, associacaoParaExcluir);
		List<DeslocaProcesso> deslocamentos = salvarDeslocamento(guia, listaSeqObjetosIncidentes);
		salvarEmprestimo(deslocamentos, emprestimo);
		String nomeResponsavel = recuperarNomeResponsavel(emprestimo);
		salvarAndamentoDeCarga(deslocamentos, usuario, nomeResponsavel);
		return deslocamentos.get(0).getGuia().getId();

	}

	/**
	 * 
	 * Faz a atualização as associações (vínculos) entre autorizador e autorizado.
	 * 
	 * @param associacaoParaIncluir
	 * @param associacaoParaExcluir
	 * @throws ServiceException
	 */
	@Override
	public void salvarVinculo(List<AssociacaoJurisdicionado> associacaoParaIncluir, List<AssociacaoJurisdicionado> associacaoParaExcluir)
			throws ServiceException {
		try {

			// excluir associações (exclusão lógica)
			for (AssociacaoJurisdicionado associacao : associacaoParaExcluir) {
				associacao.setAtivo(false);
				associacaoJurisdicionadoService.salvar(associacao);
			}

			// incluir associações
			for (AssociacaoJurisdicionado associacao : associacaoParaIncluir) {
				associacao.setAtivo(true);
				associacaoJurisdicionadoService.salvar(associacao);
			}

		} catch (Exception e) {
			throw new ServiceException("Erro ao salvar o(s) vínculo(s): " + e.getMessage());
		}
	}

	/**
	 * 
	 * Faz o lançamento de um andamento 8209 - Autos emprestados para cada processo deslocado na carga
	 * 
	 * @param deslocamentos
	 *            - a lista de deslocamentos gerados na carga
	 * @param usuario
	 *            - informações do usuário utilizadas na persistência do andamento
	 * @param nomeResponsavel
	 *            - nome do advogado/estariário que será armazenado na observação do andamento
	 * @throws ServiceException
	 */
	private void salvarAndamentoDeCarga(List<DeslocaProcesso> deslocamentos, Usuario usuario, String nomeResponsavel) throws ServiceException {

		for (DeslocaProcesso deslocamento : deslocamentos) {
			AndamentoProcessoInfoImpl andamentoProcessoInfo = new AndamentoProcessoInfoImpl();
			Andamento andamento = andamentoService.recuperarPorId(new Long("8209"));
			andamentoProcessoInfo.setAndamento(andamento);
			andamentoProcessoInfo.setCodigoUsuario(usuario.getId().toUpperCase());

			Setor setorUsuario = usuario.getSetor();

			String observacao = nomeResponsavel + " - Guia " + deslocamento.getGuia().getNumeroGuia() + "/" + deslocamento.getGuia().getAnoGuia()
					+ " (Origem: " + setorUsuario.getNome() + ")";
			andamentoProcessoInfo.setObservacao(observacao);
			andamentoProcessoInfo.setSetor(setorUsuario);
			Processo processoAndamento = processoService.recuperarProcesso(deslocamento.getClasseProcesso(), deslocamento.getNumeroProcesso());

			List<Processo> processosPrincipais = new ArrayList<Processo>();
			processosPrincipais.add(processoAndamento);
			andamentoProcessoInfo.setProcessosPrincipais(processosPrincipais);
			ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperarPorId(processoAndamento.getId());
			AndamentoProcesso ultimoAndamentoProcesso = andamentoProcessoService.recuperarUltimoAndamento(processoAndamento);
			if (ultimoAndamentoProcesso == null) {
				throw new ServiceException("O processo " + processoAndamento.getSiglaClasseProcessual() + "/ " +
			                               processoAndamento.getNumeroProcessual() + " não possui andamentos!");
			}
			
			AndamentoProcesso andamentoProcesso = andamentoProcessoService.salvarAndamento(andamentoProcessoInfo, processoAndamento, objetoIncidente);
			// atualizar o andamento no deslocamento
			deslocamento.setAndamentoProcesso(andamentoProcesso);
			deslocaProcessoService.salvar(deslocamento);
		}

	}

	/**
	 * 
	 * Faz a ataulização as associações (vínculos) entre autorizador e autorizado.
	 * 
	 * @param deslocamentos
	 *            - lista de deslocamentos gerados na carga
	 * @param emprestimo
	 *            - informações em comum de todos os empréstimos que será usada para gerar os empréstimos
	 * @throws ServiceException
	 */
	public void salvarEmprestimo(List<DeslocaProcesso> deslocamentos, EmprestimoAutosProcesso emprestimo) throws ServiceException {

		try {
			AssociacaoJurisdicionado associacao = null;

			if (emprestimo.getPapelJurisdicionado() == null) {
				// recuperar a associação se esta não havia sido persistida quando o parâmetro foi passado (no caso de associações novas)
				if (emprestimo.getAssociacaoJurisdicionado().getId() == null) {
					associacao = recuperarAssociacao(emprestimo.getAssociacaoJurisdicionado().getGrupo(), emprestimo.getAssociacaoJurisdicionado().getMembro());
					if (associacao == null) {
						throw new ServiceException("Não foi possível recuperar a associação.");
					}
					emprestimo.setAssociacaoJurisdicionado(associacao);
				}
			}

			for (DeslocaProcesso deslocamento : deslocamentos) {
				EmprestimoAutosProcesso novoEmprestimo = new EmprestimoAutosProcesso();

				if (emprestimo.getPapelJurisdicionado() == null) {
					// recuperar o papel do pai e do filho
					novoEmprestimo.setAssociacaoJurisdicionado(emprestimo.getAssociacaoJurisdicionado());
				} else {
					novoEmprestimo.setPapelJurisdicionado(emprestimo.getPapelJurisdicionado());
				}

				// se o emprestimo for para um autorizado então recuperar o vinculo
				novoEmprestimo.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
				novoEmprestimo.setDataEmprestimo(new Date());

				novoEmprestimo.setDeslocaRetirada(deslocamento);

				EmprestimoAutosProcesso emprestimoSalvo = dao.salvar(novoEmprestimo);
				
				emprestimoSalvo = dao.recuperarPorId(emprestimoSalvo.getId());
				
				novoEmprestimo = new EmprestimoAutosProcesso();
			}

		} catch (Exception e) {
			throw new ServiceException("Ocorreu um erro ao tentar salvar o empréstimo: " + e.getMessage());
		}

	}
	
	/**
	 * Retorna o nome do jurisdicionado para qual o emprétimo foi realizado.
	 * @param emprestimo
	 * @return
	 * @throws ServiceException
	 */
	private String recuperarNomeResponsavel(EmprestimoAutosProcesso emprestimo) throws ServiceException {
		try {
			PapelJurisdicionado advogado = emprestimo.getPapelJurisdicionado();
			AssociacaoJurisdicionado associacao = emprestimo.getAssociacaoJurisdicionado();
			
			if (advogado == null){
				Jurisdicionado jurisGrupo = jurisdicionadoService.recuperarPorId(associacao.getGrupo().getJurisdicionado().getId()); 
				Jurisdicionado jurisMembro = jurisdicionadoService.recuperarPorId(associacao.getMembro().getJurisdicionado().getId()); 
				return jurisGrupo.getNome().toUpperCase() + " (" + jurisMembro.getNome().toUpperCase() + ")" ;
			}else{
				Jurisdicionado jurisAdvogado = jurisdicionadoService.recuperarPorId(advogado.getJurisdicionado().getId());
				return jurisAdvogado.getNome().toUpperCase();
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param guia
	 *            - repassa para o método informações do destino e origem da guia
	 * @param listaSeqObjetosIncidentes
	 *            - as sequences dos objetos incidentes que serão deslocados
	 * @return
	 * @throws ServiceException
	 */

	private List<DeslocaProcesso> salvarDeslocamento(Guia guia, ArrayList<Long> listaSeqObjetosIncidentes) throws ServiceException {
		try {
			String numeroAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, listaSeqObjetosIncidentes, true);
			if (numeroAnoGuia == null) {
				throw new ServiceException("Erro ao efetuar o deslocamento.");
			}
			String numero_guia = numeroAnoGuia.substring(0, numeroAnoGuia.indexOf("/"));
			String ano_guia = numeroAnoGuia.substring(numeroAnoGuia.indexOf("/") + 1, numeroAnoGuia.length());
			guia.setAnoGuia(new Short(ano_guia));
			guia.setNumeroGuia(new Long(numero_guia));

			// recuperar os deslocamentos da guia gerada
			List<DeslocaProcesso> deslocamentos = deslocaProcessoService.recuperarDeslocamentoProcessos(guia);
			return deslocamentos;
		} catch (Exception e) {
			throw new ServiceException("Ocorreu um erro ao tentar salvar o deslocamento: " + e.getMessage());
		}

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
			return associacaoJurisdicionadoService.recuperarPorGrupoMembro(papelPai, papelFilho, tipoAssociacao);
		} catch (Exception e) {
			throw new ServiceException("Erro ao recuperar associações: " + e);
		}

	}

	/**
	 * Trata do recebimento de petições que foram deslocadas para entidades externas ao STF
	 * 
	 * @param listaSeqObjetosIncidentes
	 * @param usuario
	 * @throws ServiceException
	 */
	@Override
	public Guia salvarRecebimentoPeticoes(ArrayList<Long> listaSeqObjetosIncidentes, Usuario usuario) throws ServiceException {
		try {
			// gera o deslocamento de devolução das petições com recebimento automático
			Guia guia = new Guia();
			guia.setCodigoOrgaoDestino(usuario.getSetor().getId());
			guia.setTipoOrgaoDestino(2);
			//
			String numAnoGuia = objetoIncidenteService.inserirDeslocamento(guia, listaSeqObjetosIncidentes, true);
			if (numAnoGuia == null) {
				throw new ServiceException("Erro ao efetuar o deslocamento");
			}
			String numero_guia = numAnoGuia.substring(0, numAnoGuia.indexOf("/"));
			String ano_guia = numAnoGuia.substring(numAnoGuia.indexOf("/") + 1, numAnoGuia.length());

			Guia guiaDevolucao = new Guia();
			guiaDevolucao.setNumeroGuia(new Long(numero_guia));
			guiaDevolucao.setAnoGuia(new Short(ano_guia));
			return guiaDevolucao;

		} catch (Exception e) {
			throw new ServiceException("Não foi possível finalizar o recebimento.");
		}
	}
	
	@Override
	public String getNomeAdvogadoOuAutorizado(Guia guia) throws ServiceException {
		try {
			return dao.getNomeAdvogadoOuAutorizado(guia, dao.existeEmprestimoNaGuiaDeAutos(guia));
		} catch (Exception e) {
			throw new ServiceException("Não foi possível recuperar o nome.");
		}
	}
	
	@Override
	public Boolean existeEmprestimoNaGuiaDeAutos(Guia guia) throws ServiceException {
		try {
			return dao.existeEmprestimoNaGuiaDeAutos(guia);
		} catch (Exception e) {
			throw new ServiceException("Não foi possível verificar se existe o empréstimo na guia");
		}
	}

	@Override
	public Boolean existeEmprestimoParaObjetoIncidente(Long idObjetoIncidente, boolean devolucao) throws ServiceException {
		try {
			return dao.existeEmprestimoParaObjetoIncidente(idObjetoIncidente, devolucao);
		} catch (Exception e) {
			throw new ServiceException("Não foi possível verificar se existe o empréstimo para o processo");
		}
	}


}
