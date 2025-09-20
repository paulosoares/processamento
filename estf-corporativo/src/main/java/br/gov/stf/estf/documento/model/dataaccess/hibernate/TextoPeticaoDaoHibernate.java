package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TextoPeticaoDao;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TextoPeticaoDaoHibernate extends GenericHibernateDao<TextoPeticao, Long> 
			implements TextoPeticaoDao {

	public TextoPeticaoDaoHibernate() {
		super(TextoPeticao.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8743186534268082583L;

	@SuppressWarnings("unchecked")
	public List<TextoPeticao> pesquisar(Integer numMateria, Short anoMateria) throws DaoException {
		List<TextoPeticao> resp = null;
		
		try {
			Session session = retrieveSession();
//			Criteria c = session.createCriteria(TextoPeticao.class);
//			
//			c.add( Restrictions.eq("numeroMateria", numMateria));
//			c.add( Restrictions.eq("anoMateria", anoMateria));
//			c.add( Restrictions.eq("objetoIncidente.tipoObjetoIncidente", TipoObjetoIncidente.PETICAO.getCodigo()) );
//			
//			resp = c.list();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT tp FROM TextoPeticao tp join fetch tp.objetoIncidente ");
			hql.append(" WHERE tp.numeroMateria = :numeroMateria ");
			hql.append(" AND tp.anoMateria = :anoMateria ");
			hql.append(" AND tp.objetoIncidente.tipoObjetoIncidente = :tipoObjetoIncidente ");
			
			Query q = session.createQuery(hql.toString());
			q.setInteger("numeroMateria", numMateria);
			q.setShort("anoMateria", anoMateria);
			q.setString("tipoObjetoIncidente", TipoObjetoIncidente.PETICAO.getCodigo());
			
			resp = q.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return resp;
	}

	
}
