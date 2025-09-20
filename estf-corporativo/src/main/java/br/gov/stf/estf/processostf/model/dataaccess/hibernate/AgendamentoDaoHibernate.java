package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Agendamento.AgendamentoId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.AgendamentoDao;
import br.gov.stf.estf.processostf.model.util.AgendamentoDynamicRestriction;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;

@Repository
public class AgendamentoDaoHibernate extends
		GenericHibernateDao<Agendamento, AgendamentoId> implements
		AgendamentoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6653523378429596296L;

	public AgendamentoDaoHibernate() {
		super(Agendamento.class);
	}

	@SuppressWarnings("unchecked")
	public List<Agendamento> consultaAgendamentos(
			AgendamentoDynamicRestriction consulta) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(Agendamento.class,
				AgendamentoDynamicRestriction.ALIAS_AGENDAMENTO);
		consulta.addCriteriaRestrictions(criteria);
		return criteria.list();
	}

	public Agendamento recuperar(Integer codigoCapitulo, Integer codigoMateria,
			ObjetoIncidente objetoIncidente) throws DaoException {
		Agendamento agendamento = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Agendamento.class);
			if (codigoCapitulo != null) {
				c.add( Restrictions.eq("id.codigoCapitulo", codigoCapitulo) );
			}
			if (codigoMateria != null) {
				c.add( Restrictions.eq("id.codigoMateria", codigoMateria) );
			}
			c.add( Restrictions.eq("id.objetoIncidente", objetoIncidente.getId()) );
			
			agendamento = (Agendamento) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return agendamento;
	}
	
	@SuppressWarnings("unchecked")
	public List<Agendamento> recuperar(Integer codigoCapitulo, Integer tipoAgenda, Long ministroRelator, String siglaClasse, 
			String numeroProcesso, String vista) throws DaoException{
		
		Session session;
		try {
			session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" select a from Agendamento a, ObjetoIncidente oi where a.objetoIncidente.id = oi.id ");
			
			if(codigoCapitulo != null) {
				hql.append(" and a.id.codigoCapitulo = :codigoCapitulo");
			}
			
			if(tipoAgenda != null) {
				hql.append(" and a.id.codigoMateria = :tipoAgenda");
			}
			
			if(ministroRelator != null) {
				hql.append(" and a.ministroRelator = :ministroRelator");
			}
			if(siglaClasse != null) {
				hql.append( " and  a.objetoIncidente.principal.classeProcessual.id = :siglaClasse");
			}
			if(numeroProcesso != null && numeroProcesso.length() > 1) {
				hql.append(" and a.objetoIncidente.principal.numeroProcessual like :numeroProcesso");
			}
			if(vista != null) {
				hql.append(" and a.vista = :vista");
			}
			
			Query q = session.createQuery(hql.toString());
			
			if(codigoCapitulo != null) {
				q.setInteger("codigoCapitulo", codigoCapitulo);
			}
			
			if(tipoAgenda != null) {
				q.setInteger("tipoAgenda", tipoAgenda);
			}
			
			if(ministroRelator != null) {
				q.setLong("ministroRelator", ministroRelator);
			}
			if(siglaClasse != null) {
				q.setString("siglaClasse", siglaClasse);
			}
			if(numeroProcesso != null && numeroProcesso.length() > 1) {
				q.setString("numeroProcesso","%" + numeroProcesso + "%");
			}
			if(vista != null) {
				q.setString("vista", vista);
			}
			
				List<Agendamento> agendamentos = q.list();
				
				return agendamentos;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> pesquisar(ObjetoIncidente<?> objetoIncidente,
			Ministro ministroRelator, Ministro ministro, Integer colegiado,
			Integer materiaAgendamento, String flgRepercussaoGeral,
			Boolean processosForaIndice, Date dataJulgamento)
			throws DaoException {
		Session session;
		try {
			session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT a, oi, pr, a.id.codigoCapitulo, s ");
			hql.append(" FROM ObjetoIncidente oi, Processo pr, Agendamento a, JulgamentoProcesso jp, Sessao s ");
			hql.append(" WHERE oi.principal = pr ");
			if (processosForaIndice) {
				hql.append(" AND a.id.objetoIncidente(+) = oi ");
			} else {
				hql.append(" AND a.id.objetoIncidente = oi ");
			}
			
			if (dataJulgamento != null) {
				hql.append(" AND s = jp.sessao ");
				hql.append(" AND jp.objetoIncidente = oi ");
			} else {
				hql.append(" AND s(+) = jp.sessao ");
				hql.append(" AND jp.objetoIncidente(+) = oi ");
			}
			
			if (objetoIncidente != null) {
				hql.append(" AND oi.id = :idObjetoIncidente ");
			}
			if (ministroRelator != null) {
				hql.append(" AND pr.ministroRelatorAtual.id = :idMinistroRelator ");
			}
			if (!processosForaIndice) {
				if (ministro != null) {
					hql.append(" AND a.ministro.id = :idMinistro ");
				}
				if (colegiado != null && colegiado.intValue() > 0) {
					hql.append(" AND a.id.codigoCapitulo = :colegiado ");
				}
				if (materiaAgendamento != null && materiaAgendamento.intValue() > 0) {
					hql.append(" AND a.id.codigoMateria = :materiaAgendamento ");
				}
			}
			if (flgRepercussaoGeral != null && flgRepercussaoGeral.length() > 0) {
				hql.append(" AND pr.repercussaoGeral = :repercussaoGeral ");
			}
			if (dataJulgamento != null) {
				hql.append(" AND s.dataInicio >= TO_DATE(:dataJulgamentoInicio, 'DD/MM/YYYY HH24:MI:SS') ");
				hql.append(" AND s.dataInicio <= TO_DATE(:dataJulgamentoFim, 'DD/MM/YYYY HH24:MI:SS') ");
			}
			
			hql.append(" ORDER BY pr.siglaClasseProcessual, pr.numeroProcessual ");
			
			Query q = session.createQuery(hql.toString());
			
			if (objetoIncidente != null) {
				q.setLong("idObjetoIncidente", objetoIncidente.getId());
			}
			if (ministroRelator != null) {
				q.setLong("idMinistroRelator", ministroRelator.getId());
			}
			if (!processosForaIndice) {
				if (ministro != null) {
					q.setLong("idMinistro", ministro.getId());
				}
				if (colegiado != null && colegiado.intValue() > 0) {
					q.setInteger("colegiado", colegiado);
				}
				if (materiaAgendamento != null && materiaAgendamento.intValue() > 0) {
					q.setInteger("materiaAgendamento", materiaAgendamento);
				}
			}
			if (flgRepercussaoGeral != null) {
				q.setString("repercussaoGeral", flgRepercussaoGeral);
				
			}
			if (dataJulgamento != null) {
				q.setString("dataJulgamentoInicio", DateTimeHelper.getDataString(dataJulgamento) + " 00:00:00");
				q.setString("dataJulgamentoFim", DateTimeHelper.getDataString(dataJulgamento) + " 23:59:59");
			}
			
			return (List<Object[]>) q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long consultaQuantidadeProcessosAgendadosSemSessao(TipoColegiadoConstante tipoColegiado)
			throws DaoException {
		Session session;
		try {
			session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT count(oi) FROM Agendamento a, ObjetoIncidente oi ");
			hql.append(" WHERE a.objetoIncidente.id = oi.id ");
			if (tipoColegiado != null) {
				hql.append(" AND a.id.codigoCapitulo = :codigoCapitulo ");
			}
			hql.append(" AND a.id.codigoMateria = :codigoMateria ");
			// Não existe processo associado a sessão aberta
			hql.append(" AND NOT EXISTS (SELECT jp.objetoIncidente.id FROM JulgamentoProcesso jp");
			hql.append(" 	WHERE jp.objetoIncidente.id = oi.id ");
			hql.append("    AND jp.sessao.dataFim IS NULL ");
			if (tipoColegiado != null) {
				hql.append(" AND jp.sessao.colegiado = :colegiado ");
			}
			hql.append(" )"); 
			
			hql.append(" AND NOT EXISTS (SELECT plj.objetoIncidente.id FROM ProcessoListaJulgamento plj, ListaJulgamento lj ");
			hql.append("    WHERE plj.objetoIncidente.id = oi.id ");
			hql.append("    AND plj.listaJulgamento.id = lj.id ");
			hql.append("    AND lj.sessao.dataFim IS NULL ");
			if (tipoColegiado != null) {
				hql.append(" AND lj.sessao.colegiado = :colegiado ");
			}
			hql.append(" ) ");
			
			/* Adicionado para evitar que os processos já julgados, continuem a ser listado no índice. */
			/* Comentado em 03/10/2012 para resolver issue ESESSOES-270*/
//			hql.append(" AND NOT EXISTS (SELECT jp5 FROM JulgamentoProcesso jp5 ");
//			hql.append(" WHERE jp5.sessao.dataFim IS NOT NULL ");
//			hql.append(" AND jp5.objetoIncidente.id = oi.id ");
//			hql.append(" AND jp5.situacaoProcessoSessao = :situacaoProcessoSessao) ");
//			
//			hql.append(" AND NOT EXISTS (SELECT plj5 FROM ProcessoListaJulgamento plj5 ");
//			hql.append(" WHERE plj5.listaJulgamento.sessao.dataFim IS NOT NULL ");
//			hql.append(" AND plj5.objetoIncidente.id = oi.id ");
//			hql.append(" AND plj5.listaJulgamento.julgado = :julgado) ");
			
			Query q = session.createQuery(hql.toString());
			
			if (tipoColegiado != null) {
				q.setInteger("codigoCapitulo", tipoColegiado.getCodigoCapitulo());
				q.setString("colegiado", tipoColegiado.getSigla());
			}
			q.setInteger("codigoMateria", Agendamento.COD_MATERIA_AGENDAMENTO_JULGAMENTO);
//			q.setString("situacaoProcessoSessao", TipoSituacaoProcessoSessao.JULGADO.getSigla());
//			q.setString("julgado", "S");
			
			return (Long) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long consultaQuantidadeProcessosAgendadosSemSessao() throws DaoException {
		Session session;
		try {
			session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT count(distinct oi) FROM Agendamento a, ObjetoIncidente oi ");
			hql.append(" WHERE a.objetoIncidente.id = oi.id ");
			hql.append(" AND a.julgado = :julgado ");
			hql.append(" AND NOT EXISTS (SELECT jp.objetoIncidente.id FROM JulgamentoProcesso jp, Sessao s ");
			hql.append(" 	WHERE jp.objetoIncidente.id = oi.id ");
			hql.append(" 	AND jp.sessao.id = s.id ");
			hql.append(" 	AND s.dataInicio >= TO_DATE(:dataBase, 'DD/MM/YYYY HH24:MI:SS')");
			hql.append("");
			hql.append(" )"); 
			
			Query q = session.createQuery(hql.toString());
			
			q.setString("julgado", "N");
			q.setString("dataBase", DateTimeHelper.getDataString(new Date()) + " 00:00:00");
			
			return (Long) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Agendamento> pesquisar(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Agendamento.class);
			c.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
