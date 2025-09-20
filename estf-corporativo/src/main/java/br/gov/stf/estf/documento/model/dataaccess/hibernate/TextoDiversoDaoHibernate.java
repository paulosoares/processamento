package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TextoDiversoDao;
import br.gov.stf.estf.entidade.documento.TextoDiverso;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TextoDiversoDaoHibernate extends GenericHibernateDao<TextoDiverso, Long> 
	implements TextoDiversoDao {

	public TextoDiversoDaoHibernate() {
		super(TextoDiverso.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2623600302980827284L;

	@SuppressWarnings("unchecked")
	public List<TextoDiverso> pesquisar(Long codigoSetor, TipoTexto ...tiposTexto)
			throws DaoException {
		List<TextoDiverso> textoDiverso = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT td FROM TextoDiverso td, SetorTextoDiverso std ");
			hql.append(" WHERE std.setor.id = ? ");
			
			if ( tiposTexto!=null && tiposTexto.length>0 ) {
				hql.append(" AND td.tipoTexto IN ( :tipos )");				
			}
			
			hql.append(" AND std.textoDiverso.id = td.id ");    
			hql.append(" ORDER BY td.descricao ASC ");
			
			Query q = session.createQuery( hql.toString() );
			
			q.setLong(0, codigoSetor);
			
			if ( tiposTexto!=null && tiposTexto.length>0 ) {
				Long[] codigos = new Long[tiposTexto.length];
				for ( int i=0 ; i<tiposTexto.length ; i++ ) {
					codigos[i] = tiposTexto[i].getCodigo();
				}
				q.setParameterList("tipos", codigos);
			}
			
			textoDiverso = q.list();
		} catch ( Exception e ) {
			throw new DaoException("Erro em query",e);
		}
		return textoDiverso;
	}

	public TextoDiverso recuperar(TipoTexto tipoTexto) throws DaoException {
		TextoDiverso textoDiverso = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TextoDiverso.class);
			c.add( Restrictions.eq("tipoTexto", tipoTexto) );
			textoDiverso = (TextoDiverso) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return textoDiverso;
	}
	
	public TextoDiverso recuperar( String descricaoTextoDiverso, TipoTexto tipoTexto) throws DaoException {
		Session session = retrieveSession();
		TextoDiverso textoDiverso = null;
		
		try {
			Criteria c = session.createCriteria(TextoDiverso.class);
			
			if( descricaoTextoDiverso != null && descricaoTextoDiverso.trim().length() > 0 )
				c.add( Restrictions.eq("descricao", descricaoTextoDiverso) );
				
			if( tipoTexto != null )
				c.add( Restrictions.eq("tipoTexto", tipoTexto) );
			
			textoDiverso = (TextoDiverso) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return textoDiverso;
	}
	
	@SuppressWarnings("unchecked")
	public List<TextoDiverso> pesquisar(Long codigoSetor, String descricao) throws DaoException {
		List<TextoDiverso> textoDiverso = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT td FROM TextoDiverso td ");
			
			if( codigoSetor != null )
				hql.append(" , SetorTextoDiverso std ");
			
			if( descricao != null && descricao.trim().length() > 0 )
				hql.append(" , td.tipoTexto tt ");
			
			hql.append(" WHERE 1=1 ");
			
			if( codigoSetor != null ) {
				hql.append(" AND std.textoDiverso.id = td.id ");
				hql.append(" AND std.setor.id = :codigoSetor ");
			}
				
			if( descricao != null && descricao.trim().length() > 0 ) {
				hql.append(" AND (td.descricao = :descricao OR td.tipoTexto.descricao = :descricao ) ");				
			}
			
			hql.append(" ORDER BY td.descricao ASC, td.tipoTexto.descricao ASC ");
			
			Query q = session.createQuery( hql.toString() );
			
			if( codigoSetor != null )
				q.setLong("codigoSetor", codigoSetor);
			
			if( descricao != null && descricao.trim().length() > 0 ) {
				q.setString("descricao", descricao);
				q.setString("descricao", descricao);
			}

			
			textoDiverso = q.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return textoDiverso;
		}	

}
