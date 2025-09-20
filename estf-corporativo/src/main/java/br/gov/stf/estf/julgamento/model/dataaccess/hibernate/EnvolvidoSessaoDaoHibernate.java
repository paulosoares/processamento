package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.EnvolvidoSessao;
import br.gov.stf.estf.entidade.julgamento.TipoCompetenciaEnvolvido;
import br.gov.stf.estf.julgamento.model.dataaccess.EnvolvidoSessaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class EnvolvidoSessaoDaoHibernate extends GenericHibernateDao<EnvolvidoSessao, Long> implements EnvolvidoSessaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3331057537592313131L;

	public EnvolvidoSessaoDaoHibernate() {
		super(EnvolvidoSessao.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<EnvolvidoSessao> pesquisar( String nomeEnvolvido, Boolean ministro, Boolean ministroSubstituto, Long idSessao, Long... codCompetencia ) throws DaoException {
		Session session = retrieveSession();
		
		try {
			Criteria c = session.createCriteria( EnvolvidoSessao.class, "es" );
			
			if( nomeEnvolvido != null && nomeEnvolvido.trim().length() > 0 )
				c = c.createAlias("es.envolvido", "e", CriteriaSpecification.INNER_JOIN).setFetchMode("e", FetchMode.JOIN);
			
			if( nomeEnvolvido != null && nomeEnvolvido.trim().length() > 0 )
				c.add( Restrictions.like("e.nome", "%" + nomeEnvolvido.toUpperCase() + "%") );
			
			if( codCompetencia != null || (ministro != null && ministro) || (ministroSubstituto != null && ministroSubstituto) )
				c = c.createAlias("es.tipoCompetenciaEnvolvido", "tce", CriteriaSpecification.INNER_JOIN).setFetchMode("tce", FetchMode.JOIN);
			
			if( codCompetencia != null )
				c.add( Restrictions.in("tce.id", codCompetencia) );
			
			if( (ministro != null && ministro) || (ministroSubstituto != null && ministroSubstituto) )
				c = c.createAlias("es.ministro", "m", CriteriaSpecification.INNER_JOIN).setFetchMode("m", FetchMode.JOIN);
			
			if( ministro != null && ministro ) {
				c.add( Restrictions.isNotNull("m.id") );
				c.add( Restrictions.or(Restrictions.eq("tce.id", TipoCompetenciaEnvolvido.TipoAtuacaoConstante.MINISTRO.getCodigo()), 
						Restrictions.eq("tce.id", TipoCompetenciaEnvolvido.TipoAtuacaoConstante.MINISTRO_PRESIDENTE.getCodigo())) );
			}
			
			if( ministroSubstituto != null && ministroSubstituto ) {
				c.add( Restrictions.isNotNull("m.id") );				
				c.add( Restrictions.eq("tce.id", TipoCompetenciaEnvolvido.TipoAtuacaoConstante.MINISTRO_SUBSTITUTO.getCodigo()) );				
			}
			
			if( idSessao != null )
				c = c.createAlias("es.sessao", "s", CriteriaSpecification.INNER_JOIN).setFetchMode("s", FetchMode.JOIN);
			
			if( idSessao != null )
				c.add( Restrictions.eq("s.id", idSessao) );
				
			if( (ministro != null && ministro) || (ministroSubstituto != null && ministroSubstituto) )
				c.addOrder( Order.asc("m.dataPosse") );
			
			return c.list();
				
		} catch ( Exception e ) {
			throw new DaoException( e );
		}
	}
	
	public EnvolvidoSessao recuperar( Long idSessao, Long codTipoCompetenciaEnvolvido ) throws DaoException {
		Session session = retrieveSession();
		
		try {
			Criteria c = session.createCriteria( EnvolvidoSessao.class, "es" );
			
			if( codTipoCompetenciaEnvolvido != null && codTipoCompetenciaEnvolvido > 0 )
				c = c.createAlias("es.tipoCompetenciaEnvolvido", "tce", CriteriaSpecification.INNER_JOIN).setFetchMode("tce", FetchMode.JOIN);
			
			if( codTipoCompetenciaEnvolvido != null && codTipoCompetenciaEnvolvido > 0 )
				c.add( Restrictions.eq("tce.id", codTipoCompetenciaEnvolvido) );
			
			if( idSessao != null &&  idSessao > 0)
				c = c.createAlias("es.sessao", "s", CriteriaSpecification.INNER_JOIN).setFetchMode("s", FetchMode.JOIN);
			
			if( idSessao != null &&  idSessao > 0 )
				c.add( Restrictions.eq("s.id", idSessao) );
			
			return (EnvolvidoSessao) c.uniqueResult();
				
		} catch ( Exception e ) {
			throw new DaoException( e );
		}
	}
	
}
