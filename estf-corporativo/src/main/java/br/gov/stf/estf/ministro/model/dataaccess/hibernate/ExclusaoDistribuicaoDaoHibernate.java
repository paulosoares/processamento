package br.gov.stf.estf.ministro.model.dataaccess.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.ministro.ExclusaoDistribuicao;
import br.gov.stf.estf.entidade.processostf.TipoExclusaoDistribuicao;
import br.gov.stf.estf.ministro.model.dataaccess.ExclusaoDistribuicaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ExclusaoDistribuicaoDaoHibernate extends GenericHibernateDao<ExclusaoDistribuicao, Long> 
       implements ExclusaoDistribuicaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExclusaoDistribuicaoDaoHibernate() {
		super(ExclusaoDistribuicao.class);
	}
	
	@Override
	public Boolean existeAusenciaPorPresidenteNoPeriodo(Date dtInicio, Date dtFim) throws DaoException {
		try {
			Session session = retrieveSession();
			List<TipoExclusaoDistribuicao> tiposExclusao = new ArrayList<TipoExclusaoDistribuicao>();
			tiposExclusao.add(TipoExclusaoDistribuicao.valueOf((long) 1));
			tiposExclusao.add(TipoExclusaoDistribuicao.valueOf((long) 2));
			tiposExclusao.add(TipoExclusaoDistribuicao.valueOf((long) 4));
			
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			dtInicio = sd.parse(sd.format(dtInicio));
			dtFim = sd.parse(sd.format(dtFim));

			Criteria c = session.createCriteria(ExclusaoDistribuicao.class)
		    	.add( Restrictions.in("tipoExclusaoDistribuicao", tiposExclusao))
				.add( Restrictions.or(
			       Restrictions.between("dataInicioPeriodo", dtInicio, dtFim), 
				   Restrictions.between("dataFimPeriodo", dtInicio, dtFim))
				   );
			
			List<ExclusaoDistribuicao> exclusoes = c.list();
			if (exclusoes == null) {
				return false;
			}
			if (exclusoes != null && exclusoes.size() > 0) {
				return true;
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return false;
	}
	
	@Override
	public Boolean existePeriodoParaMinistro(Date dtInicio, Date dtFim, Long codMinistro) throws DaoException {
		try {
			Session session = retrieveSession();

			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			dtInicio = sd.parse(sd.format(dtInicio));
			dtFim = sd.parse(sd.format(dtFim));

			Criteria c = session.createCriteria(ExclusaoDistribuicao.class)
		    	.add( Restrictions.eq("ministro.id", codMinistro))
				.add( Restrictions.or(
			       Restrictions.between("dataInicioPeriodo", dtInicio, dtFim), 
				   Restrictions.between("dataFimPeriodo", dtInicio, dtFim))
				   );
			
			List<ExclusaoDistribuicao> exclusoes = c.list();
			if (exclusoes == null) {
				return false;
			}
			if (exclusoes != null && exclusoes.size() > 0) {
				return true;
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return false;
	}
	
	@Override
	public void inserirExclusao(ExclusaoDistribuicao exclusao) throws DaoException {
		try {
			Session session = retrieveSession();
//			Criteria c = session.createCriteria(Advogado.class);
//			c.add( Restrictions.eq("parte", parte) );
			
//			advogado = (Advogado) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}
	public void alterarExclusao(ExclusaoDistribuicao exclusao) throws DaoException {
		try {
			Session session = retrieveSession();
//			Criteria c = session.createCriteria(Advogado.class);
//			c.add( Restrictions.eq("parte", parte) );
			
//			advogado = (Advogado) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public void removerExclusao(ExclusaoDistribuicao exclusao) throws DaoException {
		try {
			Session session = retrieveSession();
//			Criteria c = session.createCriteria(Advogado.class);
//			c.add( Restrictions.eq("parte", parte) );
			
//			advogado = (Advogado) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<ExclusaoDistribuicao> recuperarExclusao(ExclusaoDistribuicao exclusao) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ExclusaoDistribuicao.class);
			
			// tratar os parametros date, zerar timestamp
			if (exclusao.getDataFimPeriodo() != null && exclusao.getDataInicioPeriodo() != null) {
				SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
				Date dtInicio = sd.parse(sd.format(exclusao.getDataInicioPeriodo()));
				Date dtFim = sd.parse(sd.format(exclusao.getDataFimPeriodo()));
				
				c.add( Restrictions.le("dataInicioPeriodo", dtFim)); 
				c.add( Restrictions.ge("dataFimPeriodo", dtInicio));
				
			}
			if (exclusao.getMinistro() != null) {
				c.add( Restrictions.eq("ministro.id", exclusao.getMinistro().getId()));
			}
			if (exclusao.getTipoExclusaoDistribuicao() != null) {
				c.add( Restrictions.eq("tipoExclusaoDistribuicao", exclusao.getTipoExclusaoDistribuicao()) );
			}
			c.addOrder(Order.desc("dataFimPeriodo"));
			
			List<ExclusaoDistribuicao> exclusoes = c.list();
			
			return exclusoes;
		} catch ( HibernateException e ) {
			throw new HibernateException(e);
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}

	

}
