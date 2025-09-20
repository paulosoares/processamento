package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Agendamento.AgendamentoId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.util.AgendamentoDynamicRestriction;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AgendamentoDao extends GenericDao<Agendamento, AgendamentoId> {

	List<Agendamento> consultaAgendamentos(AgendamentoDynamicRestriction consulta) throws DaoException;

	public Agendamento recuperar(Integer codigoCapitulo, Integer codigoMateria, ObjetoIncidente objetoIncidente)
			throws DaoException;

	List<Object[]> pesquisar(ObjetoIncidente<?> objetoIncidente, Ministro minisroRelator, Ministro ministro,
			Integer colegiado, Integer materiaAgendamento, String flgRepercussaoGeral, Boolean processosForaIndice,
			Date dataJulgamento) throws DaoException;

	Long consultaQuantidadeProcessosAgendadosSemSessao() throws DaoException;

	Long consultaQuantidadeProcessosAgendadosSemSessao(TipoColegiadoConstante colegiado) throws DaoException;

	List<Agendamento> pesquisar(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public List<Agendamento> recuperar(Integer codigoCapitulo, Integer tipoAgenda, Long ministroRelator, String siglaClasse, String numeroProcesso,
			String vistaa) throws DaoException;
}
