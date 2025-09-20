package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.InscricoesJulgamento;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoRepresentante;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.ManifestacaoRepresentanteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ManifestacaoRepresentanteDaoHibernate extends GenericHibernateDao<ManifestacaoRepresentante, Long> implements ManifestacaoRepresentanteDao {

	public ManifestacaoRepresentanteDaoHibernate() {
		super(ManifestacaoRepresentante.class);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 2937675074715839304L;

	@Override
	public List<ManifestacaoRepresentante> listarManifestacoesPorIncidente(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(ManifestacaoRepresentante.class);
		criteria.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));

		List<ManifestacaoRepresentante> lista = criteria.list();

		return lista;
	}

	public List<InscricoesJulgamento> recuperarInscritosSessaoJulgamento(Long sessaoId, boolean sustentacaoOral, boolean participacaoEmJulgamento,
			boolean julgamentoPresencial, boolean julgamentoVideoConferencia) throws DaoException {

		Session session = retrieveSession();
		List<InscricoesJulgamento> resultado = new ArrayList<InscricoesJulgamento>();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT NEW br.gov.stf.estf.entidade.InscricoesJulgamento(mr.sustentacaoOralPresencial, mr.participacaoJulgamentoPresencial, mr.dataEnvio, "
					+ " p1.nome, p2.nome, oi.id, pr.siglaClasseProcessual, pr.numeroProcessual, m.nome, s.id, c.descricao, "
					+ " pp.ordem, tpp.descricao, pp.documento, p1.id) ");
			hql.append(" FROM ManifestacaoRepresentante mr, Pessoa p1, Pessoa p2, ObjetoIncidente oi, Processo pr, Ministro m,  "
					+ " Sessao s, Colegiado c, PecaProcessual pp, TipoPecaProcessual tpp ");
			hql.append(" WHERE 1 = 1 ");
			hql.append(" AND mr.representante.id = p1.id ");
			hql.append(" AND mr.representado.id = p2.id ");
			hql.append(" AND mr.objetoIncidente.id = oi.id ");
			hql.append(" AND oi.id = pr.id ");
			hql.append(" AND m.id = oi.relatorIncidenteId ");
			hql.append(" AND mr.sessao.id = s.id ");
			hql.append(" AND c.id = s.colegiado.id ");
			hql.append(" AND mr.pecaSubstabelecimento = pp.id ");
			hql.append(" AND pp.tipoPecaProcessual.id = tpp.id ");
			
			hql.append(" AND mr.sustentacaoOralPresencial = " + (sustentacaoOral?"'S'":"'N'"));
			hql.append(" AND mr.participacaoJulgamentoPresencial = " + (participacaoEmJulgamento?"'S'":"'N'"));
			
			if ((julgamentoVideoConferencia) && (julgamentoPresencial)){
				hql.append(" AND (lower(s.observacao) like ('" + Sessao.VIDEO_CONFERENCIA + "') ");
				hql.append(" OR lower(s.observacao) like ('" + Sessao.VIDEO_CONFERENCIA + "')) ");
			} else {
				if (julgamentoVideoConferencia)
					hql.append(" AND lower(s.observacao) like ('" + Sessao.VIDEO_CONFERENCIA + "')");
				if (julgamentoPresencial)
					hql.append(" AND lower(s.observacao) like ('" + Sessao.PRESENCIAL + "')");
			}
			

			Query q = session.createQuery(hql.toString());

			resultado.addAll(q.list());

		} catch (Exception e) {
			throw new DaoException(e);
		}

		hql = new StringBuffer();
		try {
			hql.append(" SELECT NEW br.gov.stf.estf.entidade.InscricoesJulgamento(mr.sustentacaoOralPresencial, mr.participacaoJulgamentoPresencial, mr.dataEnvio, "
					+ " p1.nome, p2.nome, oi.id, pr.siglaCadeiaIncidente, pr.numeroProcessual, m.nome, s.id, c.descricao, "
					+ " pp.ordem, tpp.descricao, pp.documento, p1.id) ");
			hql.append(" FROM ManifestacaoRepresentante mr, Pessoa p1, Pessoa p2, ObjetoIncidente oi, IncidenteJulgamento pr, Ministro m,  "
					+ " Sessao s, Colegiado c, PecaProcessual pp, TipoPecaProcessual tpp ");
			hql.append(" WHERE 1 = 1 ");
			hql.append(" AND mr.representante.id = p1.id ");
			hql.append(" AND mr.representado.id = p2.id ");
			hql.append(" AND mr.objetoIncidente.id = oi.id ");
			hql.append(" AND oi.id = pr.id ");
			hql.append(" AND m.id = oi.relatorIncidenteId ");
			hql.append(" AND mr.sessao.id = s.id ");
			hql.append(" AND c.id = s.colegiado.id ");
			hql.append(" AND mr.pecaSubstabelecimento = pp.id ");
			hql.append(" AND pp.tipoPecaProcessual.id = tpp.id ");
			
			hql.append(" AND mr.sustentacaoOralPresencial = " + (sustentacaoOral?"'S'":"'N'"));
			hql.append(" AND mr.participacaoJulgamentoPresencial = " + (participacaoEmJulgamento?"'S'":"'N'"));

			Query q = session.createQuery(hql.toString());

			resultado.addAll(q.list());

		} catch (Exception e) {
			throw new DaoException(e);
		}

		hql = new StringBuffer();
		try {
			hql.append(" SELECT NEW br.gov.stf.estf.entidade.InscricoesJulgamento(mr.sustentacaoOralPresencial, mr.participacaoJulgamentoPresencial, mr.dataEnvio, "
					+ " p1.nome, p2.nome, oi.id, pr.siglaCadeiaIncidente, pr.numeroProcessual, m.nome, s.id, c.descricao, "
					+ " pp.ordem, tpp.descricao, pp.documento, p1.id) ");
			hql.append(" FROM ManifestacaoRepresentante mr, Pessoa p1, Pessoa p2, ObjetoIncidente oi, RecursoProcesso pr, Ministro m,  "
					+ " Sessao s, Colegiado c, PecaProcessual pp, TipoPecaProcessual tpp ");
			hql.append(" WHERE 1 = 1 ");
			hql.append(" AND mr.representante.id = p1.id ");
			hql.append(" AND mr.representado.id = p2.id ");
			hql.append(" AND mr.objetoIncidente.id = oi.id ");
			hql.append(" AND oi.id = pr.id ");
			hql.append(" AND m.id = oi.relatorIncidenteId ");
			hql.append(" AND mr.sessao.id = s.id ");
			hql.append(" AND c.id = s.colegiado.id ");
			hql.append(" AND mr.pecaSubstabelecimento = pp.id ");
			hql.append(" AND pp.tipoPecaProcessual.id = tpp.id ");
			
			hql.append(" AND mr.sustentacaoOralPresencial = " + (sustentacaoOral?"'S'":"'N'"));
			hql.append(" AND mr.participacaoJulgamentoPresencial = " + (participacaoEmJulgamento?"'S'":"'N'"));

			Query q = session.createQuery(hql.toString());

			resultado.addAll(q.list());

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resultado;
	}
}