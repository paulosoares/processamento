package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObservacaoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.ObservacaoProcessoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ObservacaoProcessoDaoHibernate extends
		GenericHibernateDao<ObservacaoProcesso, Long> implements
		ObservacaoProcessoDao {

	private static final long serialVersionUID = 1807000470180605142L;

	public ObservacaoProcessoDaoHibernate() {
		super(ObservacaoProcesso.class);
	}

	@Override
	public ObservacaoProcesso pesquisarObservacaoProcesso(
			ObjetoIncidente<?> objetoIncidente, Setor setor) {
		Query query = getSession()
				.createQuery(
						"from ObservacaoProcesso ob where ob.objetoIncidente = :oi and ob.setor = :setor");
		query.setParameter("oi", objetoIncidente.getPrincipal());
		query.setParameter("setor", setor);
		return (ObservacaoProcesso) query.uniqueResult();
	}

}
