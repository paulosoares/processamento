package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso.TipoSituacaoVoto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.VotoJulgamentoProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class VotoJulgamentoProcessoDaoHibernate extends GenericHibernateDao<VotoJulgamentoProcesso, Long> implements VotoJulgamentoProcessoDao {

	public VotoJulgamentoProcessoDaoHibernate() {
		super(VotoJulgamentoProcesso.class);
	}

	private static final long serialVersionUID = -8009044434836319117L;

	public Long getProximaOrdemVoto(JulgamentoProcesso julgamentoProcesso) throws DaoException{
		Session session;
		try {
			session = retrieveSession();
			
			StringBuffer hql = new StringBuffer(" SELECT max(vjp.numeroOrdemVotoSessao) FROM VotoJulgamentoProcesso vjp ");
			hql.append(" FROM VotoJulgamentoProcesso vjp");
			hql.append(" WHERE vjp.julgamentoProcesso = :julgamentoProcesso ");
			
			Query q = session.createQuery(hql.toString());
			q.setParameter("julgamentoProcesso", julgamentoProcesso);
			
			
			return (Long) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public boolean temVotoMinistroProcesso(ObjetoIncidente objetoIncidente, Ministro ministro) throws DaoException {
		Session session;
		try {
			session = retrieveSession();
			
			StringBuffer hql = new StringBuffer(" SELECT count(vjp.id) ");
			hql.append(" FROM VotoJulgamentoProcesso vjp ");
			hql.append(" WHERE vjp.tipoSituacaoVoto = :tipoSituacaoVoto ");
			hql.append(" AND vjp.julgamentoProcesso.objetoIncidente = :objetoIncidente ");
			hql.append(" AND vjp.ministro = :ministro ");
			
			Query q = session.createQuery(hql.toString());
			q.setParameter("tipoSituacaoVoto", TipoSituacaoVoto.VALIDO.getSigla());
			q.setParameter("objetoIncidente", objetoIncidente);
			q.setParameter("ministro", ministro);
			
			Long qtd = (Long) q.uniqueResult();
			
			return (qtd > 0);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<VotoJulgamentoProcesso> listarRascunhosDoMinistroNaLista(Ministro ministro, ListaJulgamento listaJulgamento) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer(" FROM VotoJulgamentoProcesso vjp ");
			hql.append(" WHERE vjp.tipoSituacaoVoto = 'R' ");
			hql.append(" AND vjp.ministro = :ministro ");
			hql.append(" AND vjp.julgamentoProcesso.processoListaJulgamento.listaJulgamento = :listaJulgamento ");
			
			Query q = session.createQuery(hql.toString());
			q.setParameter("ministro", ministro);
			q.setParameter("listaJulgamento", listaJulgamento);
			
			
			return (List<VotoJulgamentoProcesso>) q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
