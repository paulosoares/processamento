/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.AtalhoLegislacao;
import br.gov.stf.estf.entidade.jurisprudencia.LegislacaoIncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.TipoLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AtalhoLegislacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
@Repository
public class AtalhoLegislacaoDaoHibernate extends GenericHibernateDao<AtalhoLegislacao, AtalhoLegislacao.AtalhoLegislacaoId> implements AtalhoLegislacaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3491253453859898086L;

	public AtalhoLegislacaoDaoHibernate() {
		super(AtalhoLegislacao.class);
	}

	@Override
	public AtalhoLegislacao recuperar(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(AtalhoLegislacao.class);
			c.add(Restrictions.eq("tipoNorma.sigla", legislacaoIncidenteAnalise.getTipoLegislacao().getSigla()));
			c.add(Restrictions.eq("id.ano", legislacaoIncidenteAnalise.getAno()));
			
			String numero = legislacaoIncidenteAnalise.getNumero();
			
			if(NumberUtils.isNumber(numero)){
				DecimalFormat df = new DecimalFormat("000000");
				c.add(Restrictions.eq("numero", df.format(Long.valueOf(numero))));
			}
			else c.add(Restrictions.eq("numero", numero));
			
			return (AtalhoLegislacao) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AtalhoLegislacao> pesquisarAtalhosLegislacao(String sigla, Long ano) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(AtalhoLegislacao.class);
			if (sigla != null && sigla.trim().length() > 0) {
				c.add(Restrictions.eq("id.sigla", sigla.toUpperCase()));
			}
			if (ano != null && ano > 0) {
				c.add(Restrictions.eq("id.ano", ano));
			}
			c.addOrder(Order.asc("id.sigla"));
			c.addOrder(Order.asc("id.ano"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public AtalhoLegislacao recuperar(String numero, Long ano) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(AtalhoLegislacao.class);
			c.add(Restrictions.eq("id.ano", ano));
			
			if(NumberUtils.isNumber(numero)){
				DecimalFormat df = new DecimalFormat("000000");
				c.add(Restrictions.eq("numero", df.format(Long.valueOf(numero))));
			}
			else c.add(Restrictions.eq("numero", numero));
			
			return (AtalhoLegislacao) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public AtalhoLegislacao recuperar(String numero, Long ano, TipoLegislacao norma, TipoLegislacao ambito) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(AtalhoLegislacao.class);
			
			if(NumberUtils.isNumber(numero)){
				DecimalFormat df = new DecimalFormat("000000");
				c.add(Restrictions.eq("numero", df.format(Long.valueOf(numero))));
			}
			else c.add(Restrictions.eq("numero", numero));
			
			c.add(Restrictions.eq("id.ano", ano));
			
			c.add(Restrictions.eq("tipoNorma", norma));
			
			c.add(Restrictions.eq("tipoEscopoLegislacao", ambito));
			
			return (AtalhoLegislacao) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
