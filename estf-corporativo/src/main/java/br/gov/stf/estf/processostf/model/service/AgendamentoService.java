package br.gov.stf.estf.processostf.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Agendamento.AgendamentoId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.AgendamentoDao;
import br.gov.stf.estf.processostf.model.service.exception.AgendamentoNaoDefinidoException;
import br.gov.stf.estf.processostf.model.util.AgendamentoObjetoIncidenteResult;
import br.gov.stf.estf.processostf.model.util.IConsultaDeAgendamento;
import br.gov.stf.estf.publicacao.model.util.IConsultaDePautaDeJulgamento;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AgendamentoService extends GenericService<Agendamento, AgendamentoId, AgendamentoDao> {

	List<Agendamento> consultaAgendamentos(IConsultaDeAgendamento consulta) throws ServiceException;

	public Agendamento recuperar(Integer codigoCapitulo, Integer codigoMateria, String siglaClasseProcessual,
			Long numeroProcessual, Long tipoRecurso, Long tipoJulgamento) throws ServiceException;

	List<Agendamento> consultaAgendamentosParaPauta(IConsultaDePautaDeJulgamento consulta) throws ServiceException;

	Agendamento consultarAgendamento(IConsultaDeAgendamento consulta)
			throws ServiceException, AgendamentoNaoDefinidoException;

	Agendamento recuperar(Integer codigoCapitulo, Integer codigoMateria, ObjetoIncidente objetoIncidente)
			throws ServiceException;

	List<AgendamentoObjetoIncidenteResult> pesquisar(ObjetoIncidente<?> objetoIncidente, Ministro ministroRelator,
			Ministro ministro, Integer colegiado, Integer materiaAgendamento, String flgRepercussaoGeral,
			Boolean processosForaIndice, Date dataJulgamento) throws ServiceException;

	Long consultaQuantidadeProcessosAgendadosSemSessao() throws ServiceException;

	Long consultaQuantidadeProcessosAgendadosSemSessao(TipoColegiadoConstante colegiado) throws ServiceException;

	void incluirAgendamentoControlePauta(ObjetoIncidente<?> objetoIncidente, Sessao sessao, String idUsuario,
			Setor setor, boolean lancarAndamentoApresentadoEmMesa) throws ServiceException;

	List<Agendamento> pesquisar(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	public List<Agendamento> recuperar(Integer codigoCapitulo, Integer tipoAgenda, Long ministroRelator, String siglaClasse, String numeroProcesso,
			String vista) throws ServiceException;

}
