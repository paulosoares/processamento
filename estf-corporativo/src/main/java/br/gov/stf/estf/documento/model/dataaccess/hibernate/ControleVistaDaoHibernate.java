package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.ControleVistaDao;
import br.gov.stf.estf.entidade.documento.ControleVista;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.hibernate.ObjetoIncidenteDaoHibernate;
import br.gov.stf.estf.processostf.model.dataaccess.hibernate.ProcessoDaoHibernate;
import br.gov.stf.estf.processostf.model.util.ConstanteAndamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;


@Repository
public class ControleVistaDaoHibernate extends GenericHibernateDao<ControleVista, Long>	implements ControleVistaDao {

	@Autowired
	private ObjetoIncidenteDaoHibernate objetoIncidenteDaoHibernate;
	
	@Autowired
	private ProcessoDaoHibernate processoDaoHibernate;
	
	public ControleVistaDaoHibernate() {
		super(ControleVista.class);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<ControleVista> recuperar(String siglaClasseProcessual, Long numeroProcesso, Long codigoMinistro) throws DaoException {
		Session session = retrieveSession();
		List<ControleVista> lista = null;
		List<String> andamentos = new ArrayList<String>();
		/*
		 * Adiciona quais andamentos devem ser levado em consideração.
		 * ISSUE 941
		 * */
		andamentos.add(Andamentos.VISTA_AA_MINISTRA.getId().toString());
		andamentos.add(Andamentos.VISTA_AO_MINISTRO.getId().toString());
		andamentos.add(Andamentos.VISTA_AOS_MINISTROS.getId().toString());
		andamentos.add(Andamentos.VISTA_AOS_MINISTROS_DEVOLUCAO.getId().toString());
		try {
			Criteria c = session.createCriteria(ControleVista.class);
			if (siglaClasseProcessual != null && siglaClasseProcessual.trim().length() > 0) {
				c.add(Restrictions.eq("siglaClasseProcessual", siglaClasseProcessual));
			}
			if (numeroProcesso != null && numeroProcesso > 0) {
				c.add(Restrictions.eq("numeroProcessual", numeroProcesso));
			}
			if (codigoMinistro != null && codigoMinistro > 0) {
				c.add(Restrictions.eq("codigoMinistro", codigoMinistro));
			}
			c.add((Restrictions.in("codigoAndamento", andamentos)));			
			lista = c.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return lista;
	}

	@Override
	public List<ControleVista> recuperar(String siglaClasseProcessual, Long numeroProcesso) throws DaoException {
		return recuperar(siglaClasseProcessual, numeroProcesso, null);
	}

	@Override
	public List<ControleVista> recuperar(Long objetoIncidente) throws DaoException {
		Session session = retrieveSession();
		List<ControleVista> lista = null;
		List<String> andamentos = new ArrayList<String>();
		/*
		 * Adiciona quais andamentos devem ser levado em consideração.
		 * ISSUE 941
		 * */
		andamentos.add(Andamentos.VISTA_AA_MINISTRA.getId().toString());
		andamentos.add(Andamentos.VISTA_AO_MINISTRO.getId().toString());
		andamentos.add(Andamentos.VISTA_AOS_MINISTROS.getId().toString());
		andamentos.add(Andamentos.VISTA_AOS_MINISTROS_DEVOLUCAO.getId().toString());
		
		try {
			ObjetoIncidente incidente = objetoIncidenteDaoHibernate.recuperarPorId(objetoIncidente);
			
			if (incidente!= null) {
				
				Processo principal = processoDaoHibernate.recuperarPorId(incidente.getPrincipal().getId());
				
				/*StringBuffer hql = new StringBuffer();
				hql.append(" SELECT cv, FROM ControleVista cv, AndamentoProcesso ap");
				hql.append(" WHERE ap.codigoAndamento in (:andamentos) ");
				hql.append(" AND ap.sigClasseProces = cv.siglaClasseProcessual ");
				hql.append(" AND ap.numProcesso = cv.numeroProcessual ");
				hql.append(" AND ap.codigoAndamento = cv.codigoAndamento ");
				hql.append(" AND ap.numeroSequencia = cv.numeroSequencia ");
				hql.append(" AND ap.sigClasseProces = :sigClasseProces ");
				hql.append(" AND ap.numProcesso = :numProcesso ");
						
				Query c = session.createQuery(hql.toString());
				c.setString("sigClasseProces", principal.getSiglaClasseProcessual());		
				c.setLong("numProcesso", principal.getNumeroProcessual());
				c.setParameterList("andamentos", andamentos);
				*/
				
				StringBuffer hql = new StringBuffer();
				hql.append(" SELECT cv, FROM ControleVista cv, AndamentoProcesso ap");
				hql.append(" WHERE ap.codigoAndamento in (:andamentos) ");
				hql.append(" AND ap.sigClasseProces = cv.siglaClasseProcessual ");
				hql.append(" AND ap.numProcesso = cv.numeroProcessual ");
				hql.append(" AND ap.codigoAndamento = cv.codigoAndamento ");
				hql.append(" AND ap.numeroSequencia = cv.numeroSequencia ");
				hql.append(" AND ap.objetoIncidente = :objetoIncidente  ");
				Query c = session.createQuery(hql.toString());
				c.setLong("objetoIncidente", objetoIncidente);
				c.setParameterList("andamentos", andamentos);
				lista = c.list();
				
			}
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return lista;
	}

	@Override
	public List<ControleVista> listarVistasVencidas() throws DaoException {
		List<ControleVista> lista = new ArrayList<ControleVista>();

		try {
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cv FROM ControleVista cv");
			hql.append(" WHERE cv.codigoAndamento = " + ConstanteAndamento.VISTA_MINISTRO.getCodigo());
			hql.append(" AND cv.dataInicio IS NOT NULL");
			hql.append(" AND cv.dataDevolucao IS NULL");
			hql.append(" AND cv.ativo = 'S'");
			hql.append(" AND cv.dataAndamento >= TO_DATE ('19/01/2023 00:00:00', 'DD/MM/YYYY HH24:MI:SS')");
			hql.append(" AND cv.dataFim < SYSDATE");

			Query c = retrieveSession().createQuery(hql.toString());
			lista = c.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return lista;
	}

}
