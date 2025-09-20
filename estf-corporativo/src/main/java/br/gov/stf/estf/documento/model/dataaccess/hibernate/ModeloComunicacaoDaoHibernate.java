package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.ModeloComunicacaoDao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ModeloComunicacaoDaoHibernate extends
		GenericHibernateDao<ModeloComunicacao, Long> implements
		ModeloComunicacaoDao {

	public ModeloComunicacaoDaoHibernate() {
		super(ModeloComunicacao.class);
	}

	private static final long serialVersionUID = 6681276661132124981L;

	@Override
	public List<ModeloComunicacao> pesquisar(String nomeModelo,
			Long tipoComunicacao, Long tipoPermissao, String flagAtivo)
			throws DaoException {
		try {
			return pesquisar(nomeModelo, tipoComunicacao, tipoPermissao, null,
					flagAtivo);
		} catch (Exception exception) {
			throw new DaoException(exception);
		}
	}

	@Override
	public List<ModeloComunicacao> pesquisar(String nomeModelo,
			Long tipoComunicacao, Setor setor, String flagAtivo)
			throws DaoException {
		try {
			return pesquisar(nomeModelo, tipoComunicacao, null, setor,
					flagAtivo);
		} catch (Exception exception) {
			throw new DaoException(exception);
		}
	}

	@SuppressWarnings("unchecked")
	private List<ModeloComunicacao> pesquisar(String nomeModelo,
			Long tipoComunicacao, Long tipoPermissao, Setor setor,
			String flagAtivo) throws DaoException {
		StringBuffer hql = new StringBuffer();
		Session session = retrieveSession();
		hql.append(" SELECT mc FROM ModeloComunicacao mc, ").append(
				" WHERE (1=1) ");

		if (nomeModelo != null && nomeModelo.length() > 0) {
			nomeModelo = "%" + nomeModelo.replace(' ', '%').toUpperCase();
			hql.append(" AND UPPER(mc.dscModelo) LIKE (:nomeDoModelo) ");
		}

		if (tipoComunicacao != null && tipoComunicacao > 0L) {
			hql.append(" AND mc.tipoComunicacao = :tipoDoModelo ");
		}

		if (tipoPermissao != null) {
			hql.append(" AND mc.tipoPermissao = :tipoPermissao ");
		}

		if (setor != null) {
			hql.append(" AND mc.tipoPermissao.setor = :setor ");
		}

		if (flagAtivo != null && flagAtivo.length() > 0L) {
			hql.append(" AND mc.flagAtivo = :flagAtivo ");
		}

		hql.append(" ORDER BY mc.tipoComunicacao.descricao, mc.dscModelo ");

		List<ModeloComunicacao> lmc = null;

		Query q = session.createQuery(hql.toString());

		if (nomeModelo != null && nomeModelo.length() > 0L) {
			nomeModelo = "%" + nomeModelo.replace(' ', '%')
			+ "%";
			q.setString("nomeDoModelo", nomeModelo);
		}

		if (tipoComunicacao != null && tipoComunicacao > 0L) {
			q.setLong("tipoDoModelo", tipoComunicacao);
		}

		if (tipoPermissao != null) {
			q.setLong("tipoPermissao", tipoPermissao);
		}

		if (setor != null) {
			q.setLong("setor", setor.getId());
		}

		if (flagAtivo != null && flagAtivo.length() > 0L) {
			q.setString("flagAtivo", flagAtivo);
		}

		lmc = q.list();

		return lmc;
	}
	
	
	@Override
	public List<TipoComunicacao> pesquisarTipoComunicacaoPeloSetorPermissao(Setor setor) throws DaoException {
		StringBuffer hql = new StringBuffer();
		Session session = retrieveSession();
		hql.append(" SELECT DISTINCT mc.tipoComunicacao FROM ModeloComunicacao mc, ").append(
				" WHERE (1=1) ");

		if (setor != null) {
			hql.append(" AND (mc.tipoPermissao.setor IS NULL ");
			hql.append(" OR mc.tipoPermissao.setor = :setor) ");
		}

		hql.append(" AND mc.flagAtivo = 'S' ");

		hql.append(" ORDER BY mc.tipoComunicacao.descricao ");

		List<TipoComunicacao> listaTipoComunicaoPermissao = null;

		Query q = session.createQuery(hql.toString());


		if (setor != null) {
			q.setLong("setor", setor.getId());
		}

		listaTipoComunicaoPermissao = q.list();

		return listaTipoComunicaoPermissao;
	}

	@Override
	public List<TipoComunicacao> pesquisarTipoComunicacao() throws DaoException {
		StringBuffer hql = new StringBuffer();
		Session session = retrieveSession();
		hql.append(" SELECT DISTINCT mc.tipoComunicacao FROM ModeloComunicacao mc ").append( " WHERE (1=1) ");
		hql.append(" ORDER BY mc.tipoComunicacao.descricao ");

		List<TipoComunicacao> listaTipoComunicaoPermissao = null;

		Query q = session.createQuery(hql.toString());


		listaTipoComunicaoPermissao = q.list();

		return listaTipoComunicaoPermissao;
	}

	public ModeloComunicacao pesquisarModeloEscolhido(Long idModelo,
			TipoComunicacao tipo) throws DaoException {

		ModeloComunicacao modeloDocumento = new ModeloComunicacao();
		Session session = retrieveSession();

		try {

			Criteria criteria = session.createCriteria(ModeloComunicacao.class,
					"mc");

			criteria.add(Restrictions.eq("mc.id", idModelo));
			criteria.add(Restrictions.eq("mc.tipoComunicacao", tipo));

			modeloDocumento = (ModeloComunicacao) criteria.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return modeloDocumento;

	}



}
