package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.processostf.model.dataaccess.OrgaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class OrgaoDaoHibernate extends GenericHibernateDao<Orgao, Long> implements OrgaoDao { 

	private static final long serialVersionUID = 1L;

	public OrgaoDaoHibernate() {
		super(Orgao.class);
	}

	@Override
	public List<Orgao> pesquisarOrgaosAtivos() throws DaoException {

		Session session = retrieveSession();
		String hql = "select distinct o from Orgao o, RegiaoOrgao ro, RegiaoProcedencia rp,  Origem or where " +
					 "o.id = ro.orgao and ro.id = rp.regiaoOrgao and ro.id = or.regiaoOrgao " + " and or.ativo = 'S' order by o.descricao";
		
		Query query = session.createQuery(hql);
		
		List<Orgao> orgaos = query.list();  
		
		return orgaos;
	}

	@Override
	public List<Orgao> pesquisarPelaDescricaoOrgaosAtivos(String descricao) throws DaoException {
		
		List<Orgao> listaOrgaoDescricao = new ArrayList<Orgao>();
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT DISTINCT o FROM Orgao o, RegiaoOrgao ro, RegiaoProcedencia rp,  Origem or ") ;
			hql.append(" WHERE ");
			hql.append(" o.id = ro.orgao AND ro.id = rp.regiaoOrgao AND ro.id = or.regiaoOrgao ");
			if (descricao != null){
				hql.append(" AND o.descricao LIKE :descricao ");
			}
			hql.append(" AND or.ativo = 'S' order by o.descricao ");
			
			Query query = session.createQuery(hql.toString());
			if (descricao != null){
				query.setString("descricao", "%" + descricao.toUpperCase() + "%");				
			}

			listaOrgaoDescricao = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return listaOrgaoDescricao;
	}
}
