package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.PecaProcessoEletronicoComunicacaoDao;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PecaProcessoEletronicoComunicacaoDaoHibernate extends GenericHibernateDao<PecaProcessoEletronicoComunicacao, Long> 
	implements PecaProcessoEletronicoComunicacaoDao{
	
	private static final long serialVersionUID = 716343932423396137L;

	public PecaProcessoEletronicoComunicacaoDaoHibernate() {
		super(PecaProcessoEletronicoComunicacao.class);
	}
	
	public List<PecaProcessoEletronicoComunicacao> pesquisarPecasPelaComunicacao(Comunicacao comunicacao)
			throws DaoException {
		List<PecaProcessoEletronicoComunicacao> pecas = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PecaProcessoEletronicoComunicacao.class);

			c.add(Restrictions.eq("comunicacao.id", comunicacao.getId()));
			pecas = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return pecas;
	}

}
