package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoAndamentoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.ConfiguracaoAndamentoSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ConfiguracaoAndamentoSetorDaoHibernate
extends GenericHibernateDao<ConfiguracaoAndamentoSetor, Long>
implements ConfiguracaoAndamentoSetorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfiguracaoAndamentoSetorDaoHibernate() {
		super(ConfiguracaoAndamentoSetor.class);
	} 

	public ConfiguracaoAndamentoSetor recuperarConfiguracaoAndamentoSetor(Long id) throws DaoException{
		Session session = retrieveSession();
		ConfiguracaoAndamentoSetor conf = null;

		try {

			Criteria criteria = session.createCriteria(ConfiguracaoAndamentoSetor.class);

			criteria.add(Restrictions.idEq(id));

			conf = (ConfiguracaoAndamentoSetor) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return conf;
	}

	public List<ConfiguracaoAndamentoSetor> pesquisarConfiguracaoAndamentoSetor(Long id, String descricao, Long idSetor,Boolean ativo)
	throws DaoException{
		Session session = retrieveSession();
		List<ConfiguracaoAndamentoSetor> listaConf = null;

		try {

			Criteria criteria = session.createCriteria(ConfiguracaoAndamentoSetor.class);
			 
			if( id != null){
				 criteria.add(Restrictions.eq("id",id));
			}
			
			if( descricao != null && descricao.trim().length() > 0 ){
				 criteria.add(Restrictions.eq("descricao",descricao));
			}
			
			if( idSetor != null){
				 criteria.add(Restrictions.eq("setor.id",idSetor));
			}
			
			if( ativo != null){
					criteria.add(Restrictions.eq("ativo",ativo));
			}

			listaConf = criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return listaConf;
	}

	public Boolean persistirConfiguracaoAndamentoSetor(ConfiguracaoAndamentoSetor configuracaoAndamentoSetor) 
	throws DaoException{

		Session session = retrieveSession();

		try {

			session.persist(configuracaoAndamentoSetor);
			session.flush();
			return true;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	
	}

	public Boolean excluirConfiguracaoAndamentoSetor(ConfiguracaoAndamentoSetor configuracaoAndamentoSetor)
	throws DaoException{

		Session session = retrieveSession();

		try {

			session.delete(configuracaoAndamentoSetor);
			session.flush();
			return Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	
	}

}