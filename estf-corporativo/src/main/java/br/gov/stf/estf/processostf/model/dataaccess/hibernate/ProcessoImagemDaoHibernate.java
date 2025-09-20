package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ClasseUnificada;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem.ProcessoImagemId;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoImagemDao;
import br.gov.stf.estf.processostf.model.util.ProcessoImagemSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchResult;

@Repository
public class ProcessoImagemDaoHibernate extends GenericHibernateDao<ProcessoImagem, Long> implements ProcessoImagemDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 614872804022444219L;

	public ProcessoImagemDaoHibernate() {
		super(ProcessoImagem.class);
	}

	public ProcessoImagem recuperarProcessoImagem(ObjetoIncidente<?> objetoIncidente) throws DaoException {

		Session session = retrieveSession();
		ProcessoImagem processoImagem = null;
		StringBuffer hql = new StringBuffer();

		try {

			hql.append(" SELECT pi FROM ProcessoImagem pi WHERE ");
			hql.append(" pi.flgLiberado = :flgLiberado ");

			if (objetoIncidente != null && objetoIncidente.getId() != 0)
				hql.append(" AND pi.objetoIncidente = :objetoIncidente ");
			
			

			Query q = session.createQuery(hql.toString());

			q.setString("flgLiberado", "S");
			if (objetoIncidente != null && objetoIncidente.getId() > 0)
				q.setLong("objetoIncidente", objetoIncidente.getId());

			processoImagem = (ProcessoImagem) q.uniqueResult();

		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return processoImagem;
	}
	
	public ProcessoImagem recuperarProcessoImagemLiberadoPorId(ProcessoImagemId id) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();

		hql.append(" SELECT pi FROM ProcessoImagem pi WHERE");
		hql.append(" pi.flgLiberado = :flgLiberado and");
		hql.append(" pi.id.codClasse = :codClasse and");
		hql.append(" pi.id.numProcesso = :numProcesso and");
		hql.append(" pi.id.numEmenta = :numEmenta and");
		hql.append(" pi.id.numTomo = :numTomo and");
		hql.append(" pi.id.tipColecao = :tipColecao ");

		Query q = session.createQuery(hql.toString());

		q.setString("flgLiberado", "S");
		q.setInteger("codClasse", id.getCodClasse());
		q.setLong("numProcesso", id.getNumProcesso());
		q.setLong("numEmenta", id.getNumEmenta());
		q.setLong("numTomo", id.getNumTomo());
		q.setString("tipColecao", id.getTipColecao());

		return (ProcessoImagem) q.uniqueResult();
	}

	public SearchResult<ProcessoImagem> pesquisarProcessoImagem(ProcessoImagemSearchData processoImagemSearchData) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(ProcessoImagem.class);
		if (processoImagemSearchData.objetoIncidente != null) {
			criteria.add(Restrictions.eq("objetoIncidente.id", processoImagemSearchData.objetoIncidente));
		}

		return pesquisarComPaginacaoCriteria(processoImagemSearchData, criteria);
	}

	@Override
	public ClasseUnificada recuperarClasseUnificada(ProcessoImagem processoImagem) throws DaoException {
		ClasseUnificada classeUnificada = null;
		Session session = retrieveSession();

		try {
			StringBuilder sqlBuilder = new StringBuilder();

			sqlBuilder.append("SELECT cu.* FROM STF.CLASSE_UNIF cu ").append(" WHERE cu.cod_classe = ? ");

			SQLQuery query = session.createSQLQuery(sqlBuilder.toString());

			query.setInteger(0, processoImagem.getId().getCodClasse());
			query.addEntity(ClasseUnificada.class);

			classeUnificada = (ClasseUnificada) query.uniqueResult();
		} catch (Exception exception) {
			throw new DaoException(exception);
		}

		return classeUnificada;
	}

	@SuppressWarnings("unchecked")
	public List<ProcessoImagem> pesquisarProcessoImagemPorClasseNumero(String siglaClasse, Long numeroProcesso) throws DaoException {

		Session session = retrieveSession();
		
		String sql = "SELECT pi.*" + "  FROM stf.processos_img pi INNER JOIN"
				+ " stf.classe_unif classeunif ON (pi.cod_classe = classeunif.cod_classe)"
				+ " WHERE pi.num_processo = :numeroProcesso AND classeunif.sig_classe = :siglaClasse";

		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(ProcessoImagem.class);
		query.setParameter("numeroProcesso", numeroProcesso);
		query.setParameter("siglaClasse", siglaClasse);

		return (List<ProcessoImagem>) query.list();
	}
}
