package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.TipoConfiguracaoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoConfiguracaoSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.Flag;

@Repository
public class TipoConfiguracaoSetorDaoHibernate extends GenericHibernateDao<TipoConfiguracaoSetor, Long> 
implements TipoConfiguracaoSetorDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5578780779292845624L;

	public TipoConfiguracaoSetorDaoHibernate () {
		super(TipoConfiguracaoSetor.class);
	}

	public TipoConfiguracaoSetor recuperarTipoConfiguracaoSetor(Long id)
	throws DaoException
	{
		Session sessao = retrieveSession();
		
		TipoConfiguracaoSetor tipoConfiguracaoRecuperado = null;
		
		try
		{
			Criteria criteria = sessao.createCriteria(TipoConfiguracaoSetor.class);
			
			criteria.add(Restrictions.eq("id", id));
			
			tipoConfiguracaoRecuperado = (TipoConfiguracaoSetor) criteria.uniqueResult();
		}
		catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }
		
		return tipoConfiguracaoRecuperado;
	}
	
	public List<TipoConfiguracaoSetor> pesquisarTipoConfiguracaoSetor(String sigla, Boolean ativo, 
			Long idSetorResticao, Boolean restringirUtilizaEGab, Boolean localizacao, Boolean configuracaoSetor)
	throws DaoException
	{
		Session sessao = retrieveSession();
		
		List<TipoConfiguracaoSetor> tiposConfiguracaoPesquisados = null;
		
		try{
			StringBuffer hql = new StringBuffer(" SELECT tcs FROM TipoConfiguracaoSetor tcs ");
			
			if( localizacao != null && localizacao.booleanValue() )
				hql.append(" , Setor s ");
				
			if( configuracaoSetor != null && configuracaoSetor.booleanValue() )
				hql.append(" , ConfiguracaoSetor cs ");
				
			hql.append(" WHERE 1=1");
			
			if( localizacao != null && localizacao.booleanValue() && 
					configuracaoSetor != null && configuracaoSetor.booleanValue())
	            hql.append(" AND s.id = cs.setor.id ");
			
			if( configuracaoSetor != null && configuracaoSetor.booleanValue() )
	            hql.append(" AND cs.tipoConfiguracaoSetor = tcs.id ");
			
			if( idSetorResticao != null && 
					((localizacao != null && localizacao.booleanValue()) && 
					(configuracaoSetor != null && configuracaoSetor.booleanValue())))
	            hql.append(" AND s.id = " + idSetorResticao);
			
			
			if(sigla != null && !sigla.trim().equals(""))
				hql.append(" AND tcs.sigla = '" + sigla + "'");
			
			if(ativo != null)
				hql.append(" AND tcs.ativo = '" + Flag.getValor(ativo)  + "'");
			
			if(idSetorResticao != null && ((localizacao == null) && (configuracaoSetor == null)))
				hql.append(" AND tcs.id NOT IN(SELECT tcs2.id FROM Setor st" +
						" LEFT JOIN st.tiposConfiguracao tcs2" +
						" WHERE st.id = "  + idSetorResticao + ")");
			
			if( restringirUtilizaEGab != null && restringirUtilizaEGab.booleanValue() ){
				hql.append(" AND tcs.sigla NOT IN ( 'EGAB' , 'EGAB-E' )");
			}
			
			Query query = sessao.createQuery(hql.toString());
			
			tiposConfiguracaoPesquisados = query.list(); 
		}
		catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }
		
		return tiposConfiguracaoPesquisados;
	}
	
}
