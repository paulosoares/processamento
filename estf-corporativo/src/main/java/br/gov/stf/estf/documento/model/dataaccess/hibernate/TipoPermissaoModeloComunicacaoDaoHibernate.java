package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TipoPermissaoModeloComunicacaoDao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoPermissaoModeloComunicacaoDaoHibernate extends
		GenericHibernateDao<TipoPermissaoModeloComunicacao, Long> implements
		TipoPermissaoModeloComunicacaoDao {

	private static final long serialVersionUID = 3720182788909426176L;

	public TipoPermissaoModeloComunicacaoDaoHibernate() {
		super(TipoPermissaoModeloComunicacao.class);
	}

	@Override
	public List<TipoPermissaoModeloComunicacao> pesquisarPermissoes(
			Setor setor, boolean incluirInstitucional) throws DaoException {
		List<TipoPermissaoModeloComunicacao> permissoes = Collections
				.emptyList();

		try {
			Order[] order = new Order[] { Order.asc("descricao") };

			if (setor != null) {
				// Onde o setor é nulo ou igual ao pesquisado
				Criterion restricoes = Restrictions.or(
						Restrictions.isNull("setor"),
						Restrictions.eq("setor", setor));

				if (!incluirInstitucional) {
					// excluir permissão Institucional, pela ID
					restricoes = Restrictions
							.and(restricoes,
									Restrictions
											.ne("id",
													TipoPermissaoModeloComunicacao.CODIGO_PERMISSAO_INSTITUCIONAL));
				}

				permissoes = findByCriteria(order, restricoes);
			} else {
				permissoes = findByCriteria(order);
			}
		} catch (HibernateException exception) {
			throw new DaoException(convertHibernateAccessException(exception));
		} catch (RuntimeException exception) {
			throw new DaoException(exception);
		}

		return permissoes;
	}

	@Override
	public List<TipoPermissaoModeloComunicacao> pesquisarPermissoes(
			String descricaoPermissao, Boolean exatamenteIgual)
			throws DaoException {
		List<TipoPermissaoModeloComunicacao> permissoes = Collections
				.emptyList();

		try {
			Order[] order = new Order[] { Order.asc("descricao") };

			if (!StringUtils.isEmpty(descricaoPermissao)) {
				if (BooleanUtils.isTrue(exatamenteIgual)) {
					permissoes = findByCriteria(order,
							Restrictions.eq("descricao", descricaoPermissao));
				} else {
					permissoes = findByCriteria(order, Restrictions.like(
							"descricao", '%' + descricaoPermissao + '%'));
				}
			} else {
				permissoes = findByCriteria(order);
			}

		} catch (HibernateException exception) {
			throw new DaoException(convertHibernateAccessException(exception));
		} catch (RuntimeException exception) {
			throw new DaoException(exception);
		}

		return permissoes;
	}
	
	public TipoPermissaoModeloComunicacao recuperarPorId(Long idPermissao) throws DaoException {
		TipoPermissaoModeloComunicacao tipoPermissao = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("select c from TipoPermissaoModeloComunicacao c where c.id = " + idPermissao);

			Query q = session.createQuery(hql.toString());
			tipoPermissao = (TipoPermissaoModeloComunicacao) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return tipoPermissao;
	}
}
