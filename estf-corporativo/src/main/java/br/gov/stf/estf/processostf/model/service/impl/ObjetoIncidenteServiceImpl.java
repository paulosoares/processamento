package br.gov.stf.estf.processostf.model.service.impl;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ControlarDeslocaIncidente;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao.DeslocaPeticaoId;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.enuns.SituacaoIncidenteJulgadoOuNao;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.processostf.model.dataaccess.ObjetoIncidenteDao;
import br.gov.stf.estf.processostf.model.service.ControlarDeslocaIncidenteService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.DeslocamentoPeticaoService;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PeticaoService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.util.ConsultaObjetoIncidente;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.SearchResult;

@SuppressWarnings("rawtypes")
@Service("objetoIncidenteService")
public class ObjetoIncidenteServiceImpl extends GenericServiceImpl<ObjetoIncidente, Long, ObjetoIncidenteDao> implements
		ObjetoIncidenteService {

	@Autowired
	ControlarDeslocaIncidenteService controlarDeslocaIncidenteService;

	@Autowired
	GuiaService guiaService;

	@Autowired
	SetorService setorService;

	@Autowired
	DeslocaProcessoService deslocaProcessoService;

	@Autowired
	DeslocamentoPeticaoService deslocaPeticaoService;

	@Autowired
	ProcessoDependenciaService processoDependenciaService;

	@Autowired
	PeticaoService peticaoService;

	public ObjetoIncidenteServiceImpl(ObjetoIncidenteDao objetoIncidenteDao) {
		super(objetoIncidenteDao);
	}

	public ObjetoIncidente<?> recuperar(String siglaClasse, Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento)
			throws ServiceException {
		ObjetoIncidente<?> objetoIncidente = null;

		if (siglaClasse == null || siglaClasse.trim().length() == 0 || numeroProcesso == null || numeroProcesso == 0) {
			throw new ServiceException("Parâmetros inválidos.");
		}

		try {

			objetoIncidente = dao.recuperar(new ConsultaObjetoIncidente(siglaClasse, numeroProcesso, tipoRecurso,
					tipoJulgamento));

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return objetoIncidente;
	}

	public ObjetoIncidente<?> recuperar(Short ano, Long numero) throws ServiceException {
		ObjetoIncidente<?> objetoIncidente = null;

		if ((ano == null || ano < 0) || (numero == null || numero < 0))
			throw new ServiceException("Parâmetros inválidos.");

		try {
			objetoIncidente = dao.recuperar(ano, numero);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return objetoIncidente;
	}

	public ObjetoIncidente recuperar(Peticao peticao) throws ServiceException {
		ObjetoIncidente objetoIncidente = null;

		try {
			objetoIncidente = dao.recuperar(peticao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return objetoIncidente;
	}

	public SearchResult<ObjetoIncidente<?>> pesquisar(String siglaClasse, Long numeroProcesso) throws ServiceException {
		return null;
	}

	public List<ObjetoIncidente<?>> pesquisarObjetosComTextosIguais(List<Texto> textosIguais) throws ServiceException {
		return null;
	}

	@Override
	public List<ObjetoIncidente<?>> pesquisar(Long idObjetoIncidentePrincipal, TipoObjetoIncidente... tiposPermitidos)
			throws ServiceException {
		List<ObjetoIncidente<?>> objetosIncidentes = Collections.emptyList();

		try {
			objetosIncidentes = dao.pesquisar(idObjetoIncidentePrincipal, tiposPermitidos);
			Collections.sort(objetosIncidentes, new ObjetoIncidenteComparator());
		} catch (DaoException e) {
			throw new ServiceException(MessageFormat.format(
					"Erro ao pesquisar incidentes a partir do incidente principal. ID = {0}.",
					idObjetoIncidentePrincipal), e);
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}

		return objetosIncidentes;

	}

	public List<ObjetoIncidente<?>> pesquisar(Long idObjetoIncidentePrincipal) throws ServiceException {

		try {
			List<ObjetoIncidente<?>> objs = dao.pesquisar(idObjetoIncidentePrincipal);
			Collections.sort(objs, new ObjetoIncidenteComparator());
			return objs;
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}

	}

	private static class ObjetoIncidenteComparator implements Comparator<ObjetoIncidente<?>> {

		public int compare(ObjetoIncidente<?> obj, ObjetoIncidente<?> obj2) {
			if (obj != null && obj2 != null) {
				String id1 = obj.getIdentificacao();
				String id2 = obj2.getIdentificacao();

				// Verificação necessária devido a PeticaoEletronica retornar null
				if (StringUtils.isNotBlank(id1) && StringUtils.isNotBlank(id2)) {
					return obj.getIdentificacao().compareTo(obj2.getIdentificacao());
				} else {
					return 0;
				}
			} else {
				return 0;
			}
		}
	}

	// chamar a ControlarDelocaIncidenteService.insereObjetoIncidente();
	// chamar a GuiaService.callDeslocamento();
	// (PKG_DESLOCA_OBJETO_INCIDENTE.PRC_DESLOCA_OBJETO_INCIDENTE)
	public String inserirDeslocamento(Guia guia, ArrayList objetos, Long codigoSetorUsuario) throws ServiceException,
			SQLException {
		return inserirDeslocamento(guia, objetos, codigoSetorUsuario, false);
	}

	// chamar a ControlarDelocaIncidenteService.insereObjetoIncidente();
	// chamar a GuiaService.callDeslocamento();
	// (PKG_DESLOCA_OBJETO_INCIDENTE.PRC_DESLOCA_OBJETO_INCIDENTE)
	/**
	 * Deve ser utilizado o método 	inserirDeslocamento(Guia guia, ArrayList objetos, boolean recAutomatico)
	 */
	@Deprecated
	@Override
	public String inserirDeslocamento(Guia guia, ArrayList objetos, Long codigoSetorUsuario,
			Boolean recebimentoAutomatico) throws ServiceException, SQLException {
		String numAnoGuia = "";
		try {
			for (int i = 0; i <= objetos.size() - 1; i++) {
				ControlarDeslocaIncidente controlarDeslocaIncidente = new ControlarDeslocaIncidente();
				controlarDeslocaIncidente.setId((Long) objetos.get(i));
				controlarDeslocaIncidenteService.insereObjetoIncidente(controlarDeslocaIncidente);
			}

			numAnoGuia = guiaService.callDeslocamento(guia, codigoSetorUsuario, recebimentoAutomatico);
			return numAnoGuia;
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void excluirGuia(Guia guia) throws DaoException, ServiceException {

		List<DeslocaPeticao> deslocaPeticoes = deslocaPeticaoService.recuperarDeslocamentoPeticaos(guia);
		List<DeslocaProcesso> deslocaProcessos;
		if (deslocaPeticoes == null || deslocaPeticoes.size() == 0) {
			deslocaProcessos = deslocaProcessoService.recuperarDeslocamentoProcessos(guia);
			for (DeslocaProcesso deslocaProcesso : deslocaProcessos) {
				deslocaProcessoService.excluir(deslocaProcesso);
			}
		} else {
			for (DeslocaPeticao deslocaPeticao : deslocaPeticoes) {
				deslocaPeticaoService.excluir(deslocaPeticao);
			}
		}
		guiaService.excluir(guia);
	}

	@Override
	public void excluirItemProcessoNaGuia(DeslocaProcesso processo) throws ServiceException {
		try {
			guiaService.cancelarGuiaProcesso(processo);

		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void excluirItemPeticaoNaGuia(DeslocaPeticao peticao) throws ServiceException {
		try {
			guiaService.cancelarGuiaPeticao(peticao);
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void inserirProcessoPeticaoNaGuia(Guia guia, ArrayList objetos) throws ServiceException {
		String tipoObjetoIncidente = "";
		try {
			for (int i = 0; i <= objetos.size() - 1; i++) {
				ControlarDeslocaIncidente controlarDeslocaIncidente = new ControlarDeslocaIncidente();
				controlarDeslocaIncidente.setId((Long) objetos.get(i));
				controlarDeslocaIncidenteService.insereObjetoIncidente(controlarDeslocaIncidente);
			}
			if (guia.getTipoGuia() == "PRO") {
				tipoObjetoIncidente = "PR";
			} else {
				tipoObjetoIncidente = "PA";
			}
			guiaService.callInserirProcessoPeticaoNaGuia(guia, tipoObjetoIncidente);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void inserirProcessoPeticaoNaGuia(Guia guia, Object deslocamento) throws ServiceException {
		try {
			if (deslocamento instanceof DeslocaProcesso) {
				deslocaProcessoService.salvar((DeslocaProcesso) deslocamento);
			} else if (deslocamento instanceof DeslocaPeticao) {
				deslocaPeticaoService.salvar((DeslocaPeticao) deslocamento);
			} else {
				throw new ServiceException("Tipo de objeto não esperado!");
			}

		} catch (ServiceException e) {
			e.printStackTrace();
		}
		guia.setQuantidadeInternaProcesso(guia.getQuantidadeInternaProcesso() + 1);
		guiaService.salvar(guia);
	}
	
	/**
	 * Faz o deslocamento de objetos incidentes
	 * @param guia
	 * @param objetos
	 * @param recAutomatico
	 * @return ano/numero da guia
	 * @throws ServiceException
	 * @throws SQLException
	 * @throws RegraDeNegocioException 
	 */
	@Override
	public String inserirDeslocamento(Guia guia, ArrayList objetos, boolean recAutomatico) throws ServiceException, SQLException {
		return inserirDeslocamento(guia, objetos, recAutomatico, false);
	}

	/**
	 * Faz o deslocamento de objetos incidentes
	 * @param guia
	 * @param objetos
	 * @param recAutomatico
	 * @return ano/numero da guia
	 * @throws ServiceException
	 * @throws SQLException
	 * @throws RegraDeNegocioException 
	 */
	@Override
	public String inserirDeslocamento(Guia guia, ArrayList objetos, boolean recAutomatico, boolean deslocamentoAutomatico) throws ServiceException, SQLException {
		String numAnoGuia = "";
		Calendar cal = Calendar.getInstance();
		int ordemGuia = 1;
		try {

			// criticas
			if (guia.getCodigoOrgaoDestino() == null) {
				throw new ServiceException("Destino da guia não informado.");
			}
			if (guia.getId() == null) {
				GuiaId gId = new GuiaId();
				guia.setId(gId);
			}

			boolean semOrigem = false;
			if (guia.getId().getCodigoOrgaoOrigem() == null) {
				semOrigem = true;
			}

			for (int i = 0; i <= objetos.size() - 1; i++) {
				ObjetoIncidente oi = recuperarPorId((Long) objetos.get(i));
				if (oi == null) {
					throw new ServiceException("Não foi possível encontrar o objeto:" + objetos.get(i));
				}
				Peticao itemPeticao;
				Processo itemProcesso;
				if (oi.getTipoObjetoIncidente().getCodigo().equals("PA")) {
					itemPeticao = peticaoService.recuperarPeticao(recuperarPorId((Long) objetos.get(i)).getId());
					itemProcesso = null;
				} else {
					itemProcesso = (Processo) recuperarPorId((Long) objetos.get(i));
					itemPeticao = null;
				}

				if (itemProcesso == null && itemPeticao == null) {
					throw new ServiceException("A guia possui um item (processo/petição) inválido:" + objetos.get(i));
				}

				// é um processo
				if (itemProcesso != null) {
					// origem do deslocamento
					DeslocaProcesso ultimoDeslocamento = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(itemProcesso);
					// se o último deslocamento não existir, então é um delocamento inicial sem
					// critica, senão faz a crítica
					if (ultimoDeslocamento != null) {
						// se não foi passado a origem então o deslocamento é para recebimento
						// externo
						if (guia.getId().getCodigoOrgaoOrigem() == null || semOrigem) {
							// deve-se recuperar a origem no destino do último deslocamento.
							guia.getId().setCodigoOrgaoOrigem(ultimoDeslocamento.getCodigoOrgaoDestino());
							guia.setTipoOrgaoOrigem(ultimoDeslocamento.getGuia().getTipoOrgaoDestino());
						} else {
							// se foi passado a origem e possui um deslocamento anterior, então deve
							// criticar, senão é um deslocamento inicial
							if (!ultimoDeslocamento.getCodigoOrgaoDestino().equals(guia.getId().getCodigoOrgaoOrigem())) {
								throw new ServiceException("O processo " + itemProcesso.getSiglaClasseProcessual()
										+ "/" + itemProcesso.getNumeroProcessual()
										+ " está em um setor diferente do setor de origem do deslocamento.");
							}
						}
						if (ultimoDeslocamento.getDataRecebimento() == null) {
							throw new ServiceException("O processo " + itemProcesso.getSiglaClasseProcessual() + "/"
									+ itemProcesso.getNumeroProcessual() + " está em trânsito.");
						}
						// somente criticar destino = origem quando existir um deslocamento anterior
						if ((guia.getCodigoOrgaoDestino() != null) && (guia.getId().getCodigoOrgaoOrigem() != null)) {
							if (guia.getCodigoOrgaoDestino().equals(guia.getId().getCodigoOrgaoOrigem())) {
								throw new ServiceException("Destino e origem do deslocamento não podem ser iguais.");
							}
						}

					} else { // se não existe um deslocamento anterior
						if (guia.getId().getCodigoOrgaoOrigem() == null) { // e também não existe
																			// uma origem
							throw new ServiceException("Setor origem não informado");
						}
					}

					ProcessoDependencia processoDependencia = processoDependenciaService.getProcessoVinculador(itemProcesso);
					if (processoDependencia != null && !(guia.getCodigoOrgaoDestino().equals(Setor.CODIGO_SETOR_PGR.longValue()) || guia.getCodigoOrgaoDestino().equals(Setor.CODIGO_SETOR_AGU.longValue()))) {
						throw new ServiceException("Não é possível deslocar o processo "
								+ processoDependencia.getClasseProcesso() + "-"
								+ processoDependencia.getNumeroProcesso() + ", pois ele está apensado ao processo "
								+ processoDependencia.getClasseProcessoVinculador() + "-"
								+ processoDependencia.getNumeroProcessoVinculador() + ".");
					}
					
					
					if(deslocamentoAutomatico){
						List<ProcessoDependencia> listaApensos = processoDependenciaService.recuperarApensos(itemProcesso);
						if(listaApensos != null && !listaApensos.isEmpty()){
							for(ProcessoDependencia pd : listaApensos){
								ObjetoIncidente<?> objInc = recuperarPorId(pd.getIdObjetoIncidente());
								if(objInc.getEletronico() != itemProcesso.getEletronico()){
									StringBuffer sb = new StringBuffer();
									sb.append("Não é possível deslocar o processo ");
									sb.append(itemProcesso.getEletronico() ? "eletrônico " : "físico ");
									sb.append(itemProcesso.getIdentificacao()).append(", pois o processo apensado ");
									sb.append(objInc.getIdentificacao() + " é ");
									sb.append(objInc.getEletronico() ? "eletrônico." : "físico.");
									throw new ServiceException(sb.toString());
								}
							}
						}
					}
					

				}
				// é uma petição
				if (itemPeticao != null) {
					// origem do deslocamento
					DeslocaPeticao ultimoDeslocamento = deslocaPeticaoService
							.recuperarUltimoDeslocamentoPeticao(itemPeticao);
					// se o último deslocamento não existir, então é um delocamento inicial, senão
					// faz a crítica
					if (ultimoDeslocamento != null) {
						// se não foi passado a origem então o deslocamento é para recebimento
						// externo
						if (guia.getId().getCodigoOrgaoOrigem() == null || semOrigem) {
							// deve-se recuperar a origem no destino do último deslocamento.
							guia.getId().setCodigoOrgaoOrigem(ultimoDeslocamento.getCodigoOrgaoDestino());
							guia.setTipoOrgaoOrigem(ultimoDeslocamento.getGuia().getTipoOrgaoDestino());
						} else {
							// se foi passado a origem e possui um deslocamento anterior, então deve
							// criticar, senão é um deslocamento inicial
							if (!ultimoDeslocamento.getCodigoOrgaoDestino().equals(guia.getId().getCodigoOrgaoOrigem())) {
								throw new ServiceException("A petição " + itemPeticao.getNumeroPeticao() + "/"
										+ itemPeticao.getAnoPeticao()
										+ "está em um setor diferente da origem do deslocamento: ");
							}
						}
						if (ultimoDeslocamento.getDataRecebimento() == null) {
							throw new ServiceException("A petição " + itemPeticao.getNumeroPeticao() + "/"
									+ itemPeticao.getAnoPeticao() + " está em trânsito.");
						}
					} else {
						if (guia.getId().getCodigoOrgaoOrigem() == null) { // e também não existe
																			// uma origem
							throw new ServiceException("Setor origem não informado");
						}

					}
				}
			}

			// criar a guia
			Guia novaGuia = guiaService.geraGuiaVazia(guia);
			// atualiza a guia com a quantidade
			GuiaId gId = new GuiaId();
			gId.setAnoGuia(novaGuia.getAnoGuia());
			gId.setNumeroGuia(novaGuia.getNumeroGuia());
			gId.setCodigoOrgaoOrigem(novaGuia.getCodigoOrgaoOrigem());
			novaGuia = guiaService.recuperarPorId(gId);
			novaGuia.setQuantidadeInternaProcesso(objetos.size());
			// atualiza o endereço para baixa e expedição
			novaGuia.setEnderecoDestinatario(guia.getEnderecoDestinatario());
			guiaService.salvar(novaGuia);

			for (int i = 0; i <= objetos.size() - 1; i++) {
				ObjetoIncidente objetoIncidente = recuperarPorId((Long) objetos.get(i));
				if (objetoIncidente.getTipoObjetoIncidente().getCodigo().equals("PA")) {
					// desloca petição
					Peticao peticao = peticaoService.recuperarPeticao(objetoIncidente.getId());
					if (peticao == null) {
						throw new ServiceException("Não foi possível recuperar a petição");
					}
					DeslocaPeticao deslocaPeticao = new DeslocaPeticao();
					deslocaPeticao.setGuia(novaGuia);
					deslocaPeticao.setCodigoOrgaoDestino(novaGuia.getCodigoOrgaoDestino());
					// recebimento automático (recebe junto com o deslocamento)
					if ((isRecebimentoAutomatico(objetoIncidente)) || recAutomatico) {
						deslocaPeticao.setDataRecebimento(new Date());
						deslocaPeticao.setUltimoDeslocamento(true);
					} else {
						deslocaPeticao.setDataRecebimento(null);
						deslocaPeticao.setUltimoDeslocamento(false);
					}
					deslocaPeticao.setNumeroSequencia(ordemGuia);
					ordemGuia++;

					DeslocaPeticaoId deslocaPeticaoId = new DeslocaPeticaoId();
					deslocaPeticaoId.setAnoGuia(novaGuia.getAnoGuia());
					deslocaPeticaoId.setNumeroGuia(novaGuia.getNumeroGuia());
					deslocaPeticaoId.setCodigoOrgaoOrigem(novaGuia.getId().getCodigoOrgaoOrigem());
					deslocaPeticaoId.setPeticao(peticao);

					deslocaPeticao.setId(deslocaPeticaoId);
					deslocaPeticaoService.salvar(deslocaPeticao);
				} else {
					// desloca processo
					DeslocaProcesso deslocaProcesso = new DeslocaProcesso();
					deslocaProcesso.setGuia(novaGuia);
					deslocaProcesso.setCodigoOrgaoDestino(novaGuia.getCodigoOrgaoDestino());
					// É nescessário esperar 1s entre uma inserção e outra
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (recAutomatico || isRecebimentoAutomatico(objetoIncidente)) {
						deslocaProcesso.setTipoDeslocamento("EL");
						deslocaProcesso.setDataRecebimento(new Date());
						deslocaProcesso.setUltimoDeslocamento(true);
					} else {
						deslocaProcesso.setTipoDeslocamento("DE");
						deslocaProcesso.setDataRecebimento(null);
						deslocaProcesso.setUltimoDeslocamento(false);
					}
					deslocaProcesso.setNumeroSequencia(ordemGuia);
					ordemGuia++;
					// seta o ID do deslocamento
					Processo processo = (Processo) objetoIncidente.getPrincipal();
					if (processo == null) {
						throw new ServiceException("Não foi possível recuperar o processo");
					}
					DeslocaProcessoId deslocaProcessoId = new DeslocaProcessoId();
					deslocaProcessoId.setNumeroGuia(novaGuia.getNumeroGuia());
					deslocaProcessoId.setAnoGuia(novaGuia.getAnoGuia());
					deslocaProcessoId.setCodigoOrgaoOrigem(novaGuia.getId().getCodigoOrgaoOrigem());
					deslocaProcessoId.setProcesso(processo);
					deslocaProcesso.setId(deslocaProcessoId);
					deslocaProcesso.setClasseProcesso(processo.getSiglaClasseProcessual());
					deslocaProcesso.setNumeroProcesso(processo.getNumeroProcessual());
					// recuperar os apensados ao processo (se existir)
					List<ProcessoDependencia> processosApensos = processoDependenciaService
							.recuperarApensos((Processo) objetoIncidente.getPrincipal());
					// quantidades
					if (processosApensos != null) {
						deslocaProcesso.setQuantidadeApensos((short) processosApensos.size());
					}
					Short qtdJuntada = 0;
					Short qtdVolumes = 0;
					if (processo.getQuantidadeJuntadasLinha() != null) {
						qtdJuntada = processo.getQuantidadeJuntadasLinha();
					}
					if (processo.getQuantidadeVolumes() != null) {
						qtdVolumes = processo.getQuantidadeVolumes().shortValue();
					}
					deslocaProcesso.setQuantidadeJuntadaLinha(qtdJuntada);
					deslocaProcesso.setQuantidadeVolumes( qtdVolumes);
					//
					deslocaProcessoService.salvar(deslocaProcesso);

					if (processosApensos != null && processosApensos.size() > 0) {
						ordemGuia = deslocarApenso(processosApensos, novaGuia, deslocaProcesso, ordemGuia);
					}
				}
			}
			numAnoGuia = novaGuia.getNumeroGuia().toString() + "/" + novaGuia.getAnoGuia().toString();
			return numAnoGuia;
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 
	 * @param listaApensos
	 * @param novaGuia
	 * @param deslocaProcesso
	 * @param ordemGuia
	 * @return retorna um int que representa a próxima sequencia do deslocamento na guia
	 * @throws ServiceException
	 */
	private int deslocarApenso(List<ProcessoDependencia> listaApensos, Guia novaGuia, DeslocaProcesso deslocaProcesso,
			int ordemGuia) throws ServiceException {
		try {
			for (ProcessoDependencia processoDependencia : listaApensos) {
				DeslocaProcesso deslocaProcessoApenso = new DeslocaProcesso();
				deslocaProcessoApenso.setGuia(novaGuia);
				deslocaProcessoApenso.setCodigoOrgaoDestino(novaGuia.getCodigoOrgaoDestino());

				ObjetoIncidente objetoIncidenteApenso = recuperarPorId(processoDependencia.getIdObjetoIncidente());
				if (objetoIncidenteApenso == null) {
					throw new ServiceException("Processo vinculado não encontrado.");
				}
				Processo processoApenso = (Processo) objetoIncidenteApenso;
				DeslocaProcesso ultimoDeslocamentoApenso = deslocaProcessoService
						.recuperarUltimoDeslocamentoProcesso(processoApenso);
				// se o último setor for inativo e o objeto estiver em trânsito fazer o recebimento
				// antes
				Setor ultimoSetorDeslocado = setorService.recuperarPorId(ultimoDeslocamentoApenso
						.getCodigoOrgaoDestino());
				if (ultimoDeslocamentoApenso.getDataRecebimento() == null && ultimoSetorDeslocado.getAtivo() == false) {
					doRecebimentoEmSetorInativo(ultimoDeslocamentoApenso);
				} else if (ultimoDeslocamentoApenso.getDataRecebimento() == null) {
					throw new ServiceException("Processo vinculado " + processoApenso.getSiglaClasseProcessual() + "-"
							+ processoApenso.getNumeroProcessual() + " está em trânsito.");
				}
				deslocaProcessoApenso.setTipoDeslocamento("AP");
				deslocaProcessoApenso.setDataRecebimento(deslocaProcesso.getDataRecebimento());
				deslocaProcessoApenso.setNumeroSequencia(ordemGuia);
				ordemGuia++;
				deslocaProcessoApenso.setUltimoDeslocamento(false);

				// seta o ID do deslocamento de apensos
				DeslocaProcessoId deslocaProcessoApensoId = new DeslocaProcessoId();
				deslocaProcessoApensoId.setNumeroGuia(novaGuia.getNumeroGuia());
				deslocaProcessoApensoId.setAnoGuia(novaGuia.getAnoGuia());
				deslocaProcessoApensoId.setCodigoOrgaoOrigem(novaGuia.getId().getCodigoOrgaoOrigem());
				deslocaProcessoApensoId.setProcesso(processoApenso);
				deslocaProcessoApenso.setId(deslocaProcessoApensoId);
				//
				deslocaProcessoApenso.setClasseProcesso(processoApenso.getSiglaClasseProcessual());
				deslocaProcessoApenso.setNumeroProcesso(processoApenso.getNumeroProcessual());

				// recuperar os apensados ao apenso (se existir)
				List<ProcessoDependencia> apensosAoApenso = processoDependenciaService.recuperarApensos(processoApenso);
				// gravar a quantidade no apenso pai
				if (apensosAoApenso != null) {
					deslocaProcesso.setQuantidadeApensos((short) apensosAoApenso.size());
				}
				deslocaProcessoService.salvar(deslocaProcessoApenso);
				if (apensosAoApenso != null && apensosAoApenso.size() > 0) {
					ordemGuia = deslocarApenso(apensosAoApenso, novaGuia, deslocaProcesso, ordemGuia);
				}
			}
			return ordemGuia;
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	// recebimento de processos em trânsito
	private void doRecebimentoEmSetorInativo(DeslocaProcesso ultimoDeslocaProcessoApenso) throws ServiceException {
		try {

			ultimoDeslocaProcessoApenso.setDataRecebimento(new Date());
			deslocaProcessoService.salvar(ultimoDeslocaProcessoApenso);

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Descrobre, através do objeto incidente, se o recebimento é automático e retorna true ou false
	 * Se o objeto incidente for uma petição as seguintes regras deverão ser observadas:
	 *   - Quando o processo da petição for meio eletrônico: recebimento automático.
	 *   - Quando o processo da petição for meio físico: recebimento físico.
	 *   
	 *   Conforme visto acima, a regra segue o meio do processo (físico ou eletrônico)

	 * Seguem abaixo duas exceções, que NÃO devem seguir o meio do Processo:
	 *   - Marcada como Remessa indevida - recebimento manual. 
	 *   - Pendente de digitalização - recebimento manual.
	 *   
	 * Se o objeto incidente for um processo as regras serão as seguintes:
	 * 	  - Processo com o tipo_meio = "E"
	 * 	  - Objeto incidente tipo "RC" - Recurso
	 * 
	 * O prâmetro "recAutomatico" irá sobrepor as regras acima: quando passado como true o recebimento será automático e
	 * nenhuma das regras será considerada.  

	 * @param objetoIncidente
	 * @return true (receber automaticamente) / false (NÃO receber automaticamente)
	 * @throws ServiceException 
	 */
	private Boolean isRecebimentoAutomatico(ObjetoIncidente objetoIncidente) throws ServiceException {

		// Petição
		if (objetoIncidente.getTipoObjetoIncidente().getCodigo().equals("PA")) {

			Peticao peticao = peticaoService.recuperarPeticao(objetoIncidente.getId());
			if (peticao == null) {
				throw new ServiceException("Não foi possível recuperar a petição");
			}

			// se o objeto incidente vinculado não é um processo. A petiçaõ está vinculada a outra petição e neste caso o tratamento será de processo físico.
			if (peticao.getObjetoIncidenteVinculado() != null && !peticao.getObjetoIncidenteVinculado().getTipoObjetoIncidente().getCodigo().equals("PR")) {
				return false;
			}
			
			// Petição marcada como remessa indevida ou pendente de digitalização. Recebimento físico.
			if (peticaoService.isPendenteDigitalizacao(peticao) || peticaoService.isRemessaIndevida(peticao)) {
				return false;
			}
			
			ObjetoIncidente processo = peticao.getObjetoIncidenteVinculado();
			// Se por algum motivo não encontrou o processo. trata como físico.
			if (processo ==null){
				return false;
			}
			
			// por fim, segue o meio do processo.
			if (processo.getTipoMeio().equals("F")) {
					return false; // físico
				} else {
					return true; // eletrônico
				}											
		} else { // Processo
			if (objetoIncidente.getTipoMeio().equals("E") || (objetoIncidente.getTipoObjetoIncidente().equals("RC"))) {
				return true;
			} else {
				return false;
			}
		}

	}

	/**
	 * Faz o recebimento de apensos. 
	 * Quando existir apenso de apenso faz-se a chamada recursiva. 
	 * 
	 * @param processosApensos
	 * @throws ServiceException
	 */
	private void salvarRecebimentoApensos(List<ProcessoDependencia> processosApensos) throws ServiceException {
		if (processosApensos != null && processosApensos.size() > 0) {
			for (ProcessoDependencia dependencia : processosApensos) {
//				 Processo processoApenso = processoService.recuperarProcesso(dependencia.getClasseProcesso(), dependencia.getNumeroProcesso());
//				 Processo processoApenso = deslocaProcessoService.recuperar
						 
//				 DeslocaProcesso deslocaApenso = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processoApenso);
				 DeslocaProcesso deslocaApenso = deslocaProcessoService.recuperarUltimaRemessaProcesso( dependencia.getClasseProcesso(), dependencia.getNumeroProcesso() );
				 deslocaProcessoService.receberProcesso(deslocaApenso);
				 
				 List<ProcessoDependencia> apensosDeApensos = processoDependenciaService.recuperarApensos(deslocaApenso.getId().getProcesso());
				 if (apensosDeApensos != null && apensosDeApensos.size() > 0) {
					 salvarRecebimentoApensos(apensosDeApensos);
				 }
			}
		}
	}
	
	/**
	 * Método que persiste o recebimento de petições e processos no trâmite interno e externo de e para órgãos externos.
	 * NÃO é escopo desse método o recebimento de autos emprestados.
	 * @param documentos
	 * @throws ServiceException
	 * @throws DaoException 
	 */
	@Override
	public void salvarRecebimentoProcessos(List<DeslocaProcesso> documentos) throws ServiceException {
		for (DeslocaProcesso documento : documentos) {
			deslocaProcessoService.receberProcesso(documento);
			// verifica se há apensos para receber também
//			List<ProcessoDependencia> processosApensos = processoDependenciaService.recuperarApensos( documento.getId().getProcesso() );
//			salvarRecebimentoApensos(processosApensos);
		}
		try {
			dao.flushSession();
		} catch  (Exception e) {
			throw new ServiceException(e);
		}
	}
	@Override
	public void salvarRecebimentoPeticoes(List<DeslocaPeticao> documentos) throws ServiceException {
		for (DeslocaPeticao documento : documentos) {
			deslocaPeticaoService.salvarRecebimentoPeticao( documento );
		}
		try {
			dao.flushSession();
		} catch  (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public void registrarLogSistema(ObjetoIncidente objetoIncidente, String dscTransacao, String dscFuncionalidade, Long chaveTabela, String nomeTabela) throws ServiceException{
		try {
			dao.registrarLogSistema(objetoIncidente, dscTransacao, dscFuncionalidade, chaveTabela, nomeTabela);
		} catch  (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	public void registrarLogSistema(Long idObjetoIncidente, String dscTransacao, String dscFuncionalidade, Long chaveTabela, String nomeTabela) throws ServiceException{
		try {
			dao.registrarLogSistema(idObjetoIncidente, dscTransacao, dscFuncionalidade, chaveTabela, nomeTabela);
		} catch  (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public SituacaoIncidenteJulgadoOuNao recuperarSituacaoJulgamentoIncidente(Long idObjetoIncidente) throws ServiceException {
		try {
			if (dao.isIncidenteJulgado(idObjetoIncidente)) {
				return SituacaoIncidenteJulgadoOuNao.JULGADO;
			} else {
				return SituacaoIncidenteJulgadoOuNao.NAO_JULGADO;
			}
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar situação de julgamento do incidente.", e);
		}
	}
	
	
	
	@Override
	public boolean isObjetoIncidenteJulgado(ObjetoIncidente<?> incidente) throws ServiceException {
		return recuperarSituacaoJulgamentoIncidente(incidente.getId()).equals(SituacaoIncidenteJulgadoOuNao.JULGADO);
	}
	
	@Override
	public List<ObjetoIncidente<?>> pesquisarListaImportacaoUsuario(String usuario) throws ServiceException {
		try {
			
			return dao.pesquisarListaImportacaoUsuario(usuario);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar lista de objetos a serem importados.", e);
		}
	}
	
	/**
	 * Converte o objetoIncidente do proxy em um objeto real. Este método ajuda a resolver um problema de conflito de proxy com o javaassist.
	 * @param objetoIncidente que usa o proxy do javaassist 
	 * @return
	 * @throws ClassCastException
	 */
	@Override
	public ObjetoIncidente<?> deproxy(ObjetoIncidente<?> objetoIncidente) throws ClassCastException {
		   if (objetoIncidente instanceof HibernateProxy) {
		      return ObjetoIncidente.class.cast(((HibernateProxy) objetoIncidente).getHibernateLazyInitializer().getImplementation());
		   }
		   return ObjetoIncidente.class.cast(objetoIncidente);
	}
	
	@Override
	public List<String> getListaPreferenciasComMarcacoesPorObjetoIncidente(Long codObjetoIncidente) throws ServiceException{
		if(codObjetoIncidente != null && codObjetoIncidente.intValue() > 0){
			SortedSet<String> lista = new TreeSet<String>();
			recuperarListaPreferenciaRecursiva(lista, codObjetoIncidente, true);
			return new ArrayList<String>(lista);
		}
		return null;
	}
	
	@Override
	public List<String> getListaPreferenciasPorObjetoIncidente(Long codObjetoIncidente) throws ServiceException{
		if(codObjetoIncidente != null && codObjetoIncidente.intValue() > 0){
			SortedSet<String> lista = new TreeSet<String>();
			recuperarListaPreferenciaRecursiva(lista, codObjetoIncidente, false);
			return new ArrayList<String>(lista);
		}
		return null;
	}	
	
	private void recuperarListaPreferenciaRecursiva(SortedSet<String> lista, Long codObjetoIncidente, boolean incluirMarcacoes) throws ServiceException {
		ObjetoIncidente<?> oi = recuperarPorId(codObjetoIncidente);
		
		if(oi instanceof Processo && incluirMarcacoes){			
			if(Boolean.TRUE.equals(((Processo)oi).getRepercussaoGeral())){
				lista.add("Repercussão Geral");
			}
			
			if(Boolean.TRUE.equals(((Processo)oi).getPrincipal().getMateriaConstitucional())){
				lista.add("Matéria Constitucional");
			}			
		}

		if(oi.getTipoConfidencialidade() != null){
			lista.add(oi.getTipoConfidencialidade().getDescricao());
		}

		if(oi != null && oi.getPreferencias() != null){
			Collections.sort(oi.getPreferencias(), new Comparator<IncidentePreferencia>(){
				@Override
				public int compare(IncidentePreferencia o1, IncidentePreferencia o2) {
					return o1.getTipoPreferencia().getDescricao().compareTo(o2.getTipoPreferencia().getDescricao());
				}						
			});
			for(IncidentePreferencia ip : oi.getPreferencias()){
				lista.add(ip.getTipoPreferencia().getDescricao());
			}
		}
		if(oi.getPai() != null){
			recuperarListaPreferenciaRecursiva(lista, oi.getPai().getId(), incluirMarcacoes);
		}
	}
}
