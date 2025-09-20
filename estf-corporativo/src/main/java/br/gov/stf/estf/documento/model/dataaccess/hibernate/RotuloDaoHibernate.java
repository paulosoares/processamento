/**
 * 
 */
package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.RotuloDao;
import br.gov.stf.estf.entidade.documento.AbrangenciaRotulo;
import br.gov.stf.estf.entidade.documento.Rotulo;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 28.11.2013
 */
@Repository
public class RotuloDaoHibernate extends GenericHibernateDao<Rotulo, Long> implements RotuloDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8722493314390247656L;

	public RotuloDaoHibernate() {
		super(Rotulo.class);
	}

	@Override
	public List<Rotulo> pesquisarRotulos(ObjetoIncidente<?> objetoIncidente, Setor setor) throws DaoException {
		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("SELECT rotulo FROM RotuloObjetoIncidente roi ");
			hql.append("JOIN roi.id.rotulo rotulo ");
			hql.append("WHERE roi.id.objetoIncidente.id = :objetoIncidente ");
			if (setor != null) {
				hql.append("AND (rotulo.abrangencia = :abrangencia OR rotulo.setor.id = :setor) ");
			} else {
				hql.append("AND rotulo.abrangencia = :abrangencia ");
			}

			Query query = session.createQuery(hql.toString());
			query.setLong("objetoIncidente", objetoIncidente.getId());
			query.setString("abrangencia", AbrangenciaRotulo.INSTITUCIONAL.getCodigo());
			
			if (setor != null) {
				query.setLong("setor", setor.getId());
			}

			return query.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
