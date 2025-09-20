package br.gov.stf.estf.processostf.model.service.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Agendamento.AgendamentoId;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.model.util.IConsultaBasicaDeProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.AgendamentoDao;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.exception.AgendamentoNaoDefinidoException;
import br.gov.stf.estf.processostf.model.util.AgendamentoDynamicRestriction;
import br.gov.stf.estf.processostf.model.util.AgendamentoObjetoIncidenteResult;
import br.gov.stf.estf.processostf.model.util.IConsultaDeAgendamento;
import br.gov.stf.estf.publicacao.model.util.IConsultaDePautaDeJulgamento;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("agendamentoService")
public class AgendamentoServiceImpl extends GenericServiceImpl<Agendamento, AgendamentoId, AgendamentoDao>
		implements AgendamentoService {
	private final ObjetoIncidenteService objetoIncidenteService;

	private final AndamentoProcessoService andamentoProcessoService;

	public AgendamentoServiceImpl(AgendamentoDao dao, ObjetoIncidenteService objetoIncidenteService,
			AndamentoProcessoService andamentoProcessoService) {
		super(dao);
		this.objetoIncidenteService = objetoIncidenteService;
		this.andamentoProcessoService = andamentoProcessoService;
	}

	public Agendamento consultarAgendamento(IConsultaDeAgendamento consulta)
			throws ServiceException, AgendamentoNaoDefinidoException {
		try {
			AgendamentoDynamicRestriction consultaDinamica = montaConsultaDeAgendamento(consulta);
			List<Agendamento> agendamentos = dao.consultaAgendamentos(consultaDinamica);
			// Deve identificar unicamente o agendamento
			
			if (agendamentos.size() == 1)
				return agendamentos.get(0);
			
			if (agendamentos.size() == 0)
				throw new AgendamentoNaoDefinidoException("Não foi encontrado agendamento para este processo!");
			
			if (agendamentos.size() > 1)
				throw new AgendamentoNaoDefinidoException("Foi encontrado mais de um agendamento para este processo!");
			
			throw new AgendamentoNaoDefinidoException("Os dados da consulta não foram suficientes para definir o agendamento!");
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<Agendamento> consultaAgendamentos(IConsultaDeAgendamento consulta) throws ServiceException {
		try {
			AgendamentoDynamicRestriction consultaDinamica = montaConsultaDeAgendamento(consulta);
			return dao.consultaAgendamentos(consultaDinamica);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private AgendamentoDynamicRestriction montaConsultaDeAgendamento(IConsultaDeAgendamento consulta) {
		AgendamentoDynamicRestriction consultaDinamica = carregaDadosBasicosDoProcesso(consulta);
		consultaDinamica.setCodigoDoMinistro(consulta.getCodigoDoMinistro());
		consultaDinamica.setSequenciaisObjetosIncidentes(consulta.getSequenciaisObjetosIncidentes());
		return consultaDinamica;
	}

	private AgendamentoDynamicRestriction carregaDadosBasicosDoProcesso(IConsultaBasicaDeProcesso consulta) {
		AgendamentoDynamicRestriction consultaDinamica = new AgendamentoDynamicRestriction();
		consultaDinamica.setSequencialObjetoIncidente(consulta.getSequencialObjetoIncidente());
		return consultaDinamica;
	}

	public List<Agendamento> consultaAgendamentosParaPauta(IConsultaDePautaDeJulgamento consulta)
			throws ServiceException {
		try {
			AgendamentoDynamicRestriction consultaDinamica = carregaDadosBasicosDoProcesso(consulta);
			consultaDinamica.setCodigoMateria(consulta.getCodigoDaMateria());
			return dao.consultaAgendamentos(consultaDinamica);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public Agendamento recuperar(Integer codigoCapitulo, Integer codigoMateria, String siglaClasseProcessual,
			Long numeroProcessual, Long tipoRecurso, Long tipoJulgamento) throws ServiceException {

		ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperar(siglaClasseProcessual, numeroProcessual,
				tipoRecurso, tipoJulgamento);
		Agendamento agendamento = null;
		try {
			agendamento = dao.recuperar(codigoCapitulo, codigoMateria, objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return agendamento;
	}

	public Agendamento recuperar(Integer codigoCapitulo, Integer codigoMateria, ObjetoIncidente objetoIncidente)
			throws ServiceException {
		Agendamento agendamento = null;
		try {
			agendamento = dao.recuperar(codigoCapitulo, codigoMateria, objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return agendamento;
	}

	@Override
	public List<Agendamento> pesquisar(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.pesquisar(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<AgendamentoObjetoIncidenteResult> pesquisar(ObjetoIncidente<?> objetoIncidente,
			Ministro ministroRelator, Ministro ministro, Integer colegiado, Integer materiaAgendamento,
			String flgRepercussaoGeral, Boolean processosForaIndice, Date dataJulgamento) throws ServiceException {
		List<AgendamentoObjetoIncidenteResult> resultado = new ArrayList<AgendamentoObjetoIncidenteResult>();
		try {
			List<Object[]> agendamentos = dao.pesquisar(objetoIncidente, ministroRelator, ministro, colegiado,
					materiaAgendamento, flgRepercussaoGeral, processosForaIndice, dataJulgamento);
			for (Object[] res : agendamentos) {
				resultado.add(new AgendamentoObjetoIncidenteResult((Agendamento) res[0], (ObjetoIncidente<?>) res[1],
						(Processo) res[2], (Integer) res[3], (Sessao) res[4]));
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return resultado;
	}

	@Override
	public Long consultaQuantidadeProcessosAgendadosSemSessao() throws ServiceException {
		try {
			return dao.consultaQuantidadeProcessosAgendadosSemSessao();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Long consultaQuantidadeProcessosAgendadosSemSessao(TipoColegiadoConstante colegiado)
			throws ServiceException {
		try {
			return dao.consultaQuantidadeProcessosAgendadosSemSessao(colegiado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	// @Override
	// public void incluirAgendamentoControlePauta(Agendamento novoAgendamento,
	// String idUsuario, Setor setor) throws ServiceException {
	// incluir(novoAgendamento);
	//
	// Long numeroUltimoAndamento =
	// andamentoProcessoService.recuperarUltimoNumeroSequencia(novoAgendamento.getObjetoIncidente());
	// numeroUltimoAndamento++;
	// Date dataAtual = new Date();
	// AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
	// andamentoProcesso.setCodigoAndamento(AndamentoProcesso.COD_ANDAMENTO_APRESENTACAO_MESA_JULG);
	// andamentoProcesso.setObjetoIncidente(novoAgendamento.getObjetoIncidente());
	// andamentoProcesso.setCodigoUsuario(idUsuario);
	// andamentoProcesso.setDataAndamento(dataAtual);
	// andamentoProcesso.setDataHoraSistema(dataAtual);
	// andamentoProcesso.setDescricaoObservacaoAndamento(defineObservacao(novoAgendamento,
	// dataAtual));
	// andamentoProcesso.setNumeroSequencia(numeroUltimoAndamento);
	// andamentoProcesso.setSetor(setor);
	// andamentoProcessoService.incluir(andamentoProcesso);
	// }

	@Override
	public void incluirAgendamentoControlePauta(ObjetoIncidente<?> objetoIncidente, Sessao sessao, String idUsuario,
			Setor setor, boolean lancarAndamentoApresentadoEmMesa) throws ServiceException {

		Agendamento agendamento;

		agendamento = new Agendamento();
		AgendamentoId agendamentoId = new AgendamentoId();
		agendamentoId.setCodigoCapitulo(defineCodigoCapitulo(sessao));
		agendamentoId.setCodigoMateria(Agendamento.COD_MATERIA_AGENDAMENTO_JULGAMENTO);
		agendamentoId.setObjetoIncidente(objetoIncidente.getId());
		agendamento.setObjetoIncidente(objetoIncidente);
		agendamento.setId(agendamentoId);
		// TODO - O ministro deve ser o relator do incidente
		agendamento.setMinistro(((Processo) objetoIncidente.getPrincipal()).getMinistroRelatorAtual());
		// TODO - O Paulo havia dito que seria null, mas o campo é not null.
		// Verificar posteriormente.
		agendamento.setVista(Boolean.FALSE);
		// TODO - Observação deverá ser obtida das informações de pauta
		agendamento.setObservacaoProcesso(null);
		agendamento.setJulgado(false);
		// TODO - Verificar se é o valor correto
		agendamento.setDirigida(Boolean.FALSE);

		flushSession();
		limparSessao();

		incluir(agendamento);

		if (lancarAndamentoApresentadoEmMesa) {
			Long numeroUltimoAndamento = andamentoProcessoService
					.recuperarProximoNumeroSequencia(agendamento.getObjetoIncidente());
			Date dataAtual = new Date();
			AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
			andamentoProcesso.setCodigoAndamento(AndamentoProcesso.COD_ANDAMENTO_APRESENTACAO_MESA_JULG);
			andamentoProcesso.setObjetoIncidente(agendamento.getObjetoIncidente());
			andamentoProcesso.setCodigoUsuario(idUsuario);
			andamentoProcesso.setDataAndamento(dataAtual);
			andamentoProcesso.setDataHoraSistema(dataAtual);
			andamentoProcesso.setDescricaoObservacaoAndamento(defineObservacao(agendamento, dataAtual));
			andamentoProcesso.setNumeroSequencia(numeroUltimoAndamento);
			andamentoProcesso.setSetor(setor);
			andamentoProcessoService.incluir(andamentoProcesso);
		}
	}

	private Integer defineCodigoCapitulo(Sessao sessao) {
		if (sessao.getColegiado().getId().equals(Colegiado.TRIBUNAL_PLENO)) {
			return EstruturaPublicacao.COD_CAPITULO_PLENARIO;
		} else if (sessao.getColegiado().getId().equals(Colegiado.PRIMEIRA_TURMA)) {
			return EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA;
		} else if (sessao.getColegiado().getId().equals(Colegiado.SEGUNDA_TURMA)) {
			return EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA;
		}

		return null;
	}

	private String defineObservacao(Agendamento agendamento, Date dataAtual) {
		String observacao = "";
		if (agendamento.getId().getCodigoCapitulo().equals(EstruturaPublicacao.COD_CAPITULO_PLENARIO)) {
			observacao = "Pleno";
		} else if (agendamento.getId().getCodigoCapitulo().equals(EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA)) {
			observacao = "1ª Turma";
		} else if (agendamento.getId().getCodigoCapitulo().equals(EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA)) {
			observacao = "2ª Turma";
		}

		String siglaCadeiaIncidente = "";
		if (agendamento.getObjetoIncidente() instanceof RecursoProcesso) {
			siglaCadeiaIncidente = " - "
					+ ((RecursoProcesso) agendamento.getObjetoIncidente()).getSiglaCadeiaIncidente();
		} else if (agendamento.getObjetoIncidente() instanceof IncidenteJulgamento) {
			siglaCadeiaIncidente = " - "
					+ ((IncidenteJulgamento) agendamento.getObjetoIncidente()).getSiglaCadeiaIncidente();
		}

		observacao += " em " + DataUtil.date2String(dataAtual,true) + siglaCadeiaIncidente;
		return observacao;
	}

	@Override
	public List<Agendamento> recuperar(Integer codigoCapitulo, Integer tipoAgenda, Long ministroRelator, String siglaClasse, String numeroProcesso, String vista)
			throws ServiceException {
		try {
			return dao.recuperar(codigoCapitulo, tipoAgenda, ministroRelator, siglaClasse, numeroProcesso, vista);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
