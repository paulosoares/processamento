package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PecaInformacaoPautaProcesso;
import br.gov.stf.estf.julgamento.model.dataaccess.PecaInformacaoPautaProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PecaInformacaoPautaProcessoDaoHibernate extends GenericHibernateDao<PecaInformacaoPautaProcesso, Long> implements PecaInformacaoPautaProcessoDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PecaInformacaoPautaProcessoDaoHibernate() {
		super(PecaInformacaoPautaProcesso.class);
	}

	@Override
	public List<PecaInformacaoPautaProcesso> recuperar(InformacaoPautaProcesso informacaoPautaProcesso, boolean somenteDocumentoExterno) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PecaInformacaoPautaProcesso.class);
			c.add( Restrictions.eq("informacaoPautaProcesso", informacaoPautaProcesso) );
			if ( somenteDocumentoExterno ){
				c.add( Restrictions.isNotNull("arquivo") );
			}
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}	
	}
	
	@Override
	public List<PecaInformacaoPautaProcesso> recuperar(InformacaoPautaProcesso informacaoPautaProcesso, PecaProcessoEletronico pecaProcessoEletronico) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PecaInformacaoPautaProcesso.class);
			c.add( Restrictions.eq("informacaoPautaProcesso.id", informacaoPautaProcesso.getId()) );
			c.add( Restrictions.eq("pecaProcessoEletronico.id", pecaProcessoEletronico.getId()) );
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}	
	}

}
