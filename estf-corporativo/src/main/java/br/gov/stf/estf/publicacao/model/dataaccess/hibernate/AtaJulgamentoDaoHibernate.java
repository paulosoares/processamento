package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.publicacao.AtaJulgamento;
import br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta;
import br.gov.stf.estf.entidade.publicacao.AtaJulgamento.TipoAta;
import br.gov.stf.estf.publicacao.model.dataaccess.AtaJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * Implementação Hibernate para <code>AtaJulgamentoDao</code>.
 * 
 * @author Rodrigo Barreiros
 * @since 08.06.2009
 */
@Repository
public class AtaJulgamentoDaoHibernate extends GenericHibernateDao<AtaJulgamento, AtaJulgamento.AtaJulgamentoId> implements AtaJulgamentoDao {

	private static final long serialVersionUID = -5265796048648067376L;
	
	public AtaJulgamentoDaoHibernate() {
		super(AtaJulgamento.class);
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.publicacao.model.dataaccess.AtaJulgamentoDao#recuperarPor(java.util.Date, int, br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta)
	 */
	@Override
	public AtaJulgamento recuperarPor(Date dataSessao, int colegiado, CategoriaAta categoria) {
		try {
			Criteria criteria = retrieveSession().createCriteria(getPersistentClass());
			if (categoria.equals(CategoriaAta.ATA)) {
				criteria.add(Restrictions.eq("id.tipoAta", TipoAta.A));
			}else{
				if (categoria.equals(CategoriaAta.ATAORDINARIA)) {
					criteria.add(Restrictions.eq("id.tipoAta", TipoAta.AO));
				}else{
					if (categoria.equals(CategoriaAta.ATAEXTRAORDINARIA)) {
						criteria.add(Restrictions.eq("id.tipoAta", TipoAta.AE));
					}else{
						if (categoria.equals(CategoriaAta.PAUTA)) {
								criteria.add(Restrictions.eq("id.tipoAta", TipoAta.P));
						}else{
							if (categoria.equals(CategoriaAta.INDICE)) {
								criteria.add(Restrictions.eq("id.tipoAta", TipoAta.I));
							}else{
								if (categoria.equals(CategoriaAta.SESSAOVIRTUAL)) {
									criteria.add(Restrictions.eq("id.tipoAta", TipoAta.AV));
								}
								}
							}
						}
					}
				}
			criteria.add(Restrictions.eq("id.codigoSessao", colegiado));
			criteria.add(Restrictions.eq("id.dataSessao", dataSessao));
			
			return (AtaJulgamento) criteria.uniqueResult();
		} catch (DaoException e) {
			throw new RuntimeException(e);
		}
	}
	
}
