package br.gov.stf.estf.processosetor.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processosetor.ControleDistribuicao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processosetor.model.dataaccess.DistribuicaoProcessoSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class DistribuicaoProcessoSetorDaoHibernate extends GenericHibernateDao<ControleDistribuicao, Long> implements DistribuicaoProcessoSetorDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6285887569705078448L;

	public DistribuicaoProcessoSetorDaoHibernate () {
		super(ControleDistribuicao.class);
	}
	
	public Boolean persistirMapaDistribuicao(ControleDistribuicao mapaDistribuicao) throws DaoException{
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(mapaDistribuicao);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}

	@SuppressWarnings("unchecked")
	public List<ControleDistribuicao> pesquisarControleDistribuicao(Long id,
			Long idGrupoUsuario, String sigClasse, String tipoJulgamento,
			String sigUsuario) throws DaoException {
		Session session = retrieveSession();

		List<ControleDistribuicao> lista = null;

		try {

			Criteria criteria = session.createCriteria(ControleDistribuicao.class);

			if (sigClasse != null && !sigClasse.equals("")) {
				criteria.add(Restrictions.eq("siglaClasseProcessual", sigClasse));
			}

			if (id != null) {
				criteria.add(Restrictions.eq("id", id));
			}

			if (idGrupoUsuario != null) {
				criteria.add(Restrictions.eq("grupoUsuario.id", idGrupoUsuario));
			}

			if (tipoJulgamento != null && tipoJulgamento.trim().length() > 0) {
				criteria.add(Restrictions.eq("tipoJulgamento", tipoJulgamento));
			}
			
			if (sigUsuario != null && sigUsuario.trim().length() > 0) {
				criteria.add(Restrictions.eq("usuario.id", sigUsuario));
			}

			lista = criteria.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return lista;
	}

	public Boolean excluirControleDistribuicao(
			ControleDistribuicao controleDistribuicao)
			throws DaoException {
		Boolean excluido = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.delete(controleDistribuicao);
			session.flush();

			excluido = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return excluido;
	}

	@Override
	public Long pesquisarGrupoDistribuicao(Processo processo) throws DaoException {

		Session session = retrieveSession();
		String sql = "select d.SEQ_GRUPO_DISTRIBUICAO from judiciario.distribuicao d, judiciario.incidente_distribuicao i, judiciario.processo p " +
			"where d.flg_ultima_distribuicao = 'S' and d.seq_incidente_distribuicao = i.seq_incidente_distribuicao and " +
			"i.seq_objeto_incidente = p.seq_objeto_incidente and p.sig_classe_proces = :siglaProcesso and p.num_processo = :numeroProcesso";
		
		Query query = session.createSQLQuery(sql);
		
		query.setString("siglaProcesso", processo.getSiglaClasseProcessual());
		query.setLong("numeroProcesso", processo.getNumeroProcessual());
		
		Number result = (Number)query.uniqueResult();
		
		return result == null ? null : result.longValue();
	}

	@Override
	public void subtrairMapaDistribuicao(Long quantidade, Long grupoDistribuicao, Ministro ministro) throws DaoException {

		Session session = retrieveSession();
		String sql = "UPDATE judiciario.mapa_distribuicao SET qtd_processo = qtd_processo - :quantidade " +
			"WHERE seq_grupo_distribuicao = :grupoDistribuicao and cod_ministro = :ministro";
			
		Query query = session.createSQLQuery(sql);
		query.setLong("quantidade", quantidade);
		query.setLong("grupoDistribuicao", grupoDistribuicao);
		query.setLong("ministro", ministro.getId());
		
		int rowsAffected = query.executeUpdate();
		
		if (rowsAffected == 0) {
			throw new DaoException("Não foi possível atualizar o mapa de distribuição, pois não foi encontrado um mapa para o ministro '"+ministro.getNome() + 
					"' no grupo '" + grupoDistribuicao + "'");
		}
	}
	
}
