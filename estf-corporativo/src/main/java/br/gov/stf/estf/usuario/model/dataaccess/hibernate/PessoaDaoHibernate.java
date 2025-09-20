package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.estf.usuario.model.dataaccess.PessoaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PessoaDaoHibernate  extends GenericHibernateDao<Pessoa, Long> implements PessoaDao{
	

	private static final long serialVersionUID = -576864687364257672L;

	public PessoaDaoHibernate() {
		super(Pessoa.class);
	}
	
	public Pessoa recuperarPorId(Long codigoPessoaDestinatario) throws DaoException {
		Session session = retrieveSession();
		Pessoa pessoa = null;		
		try {
			Criteria criteria = session.createCriteria(Pessoa.class);
			criteria.add(Restrictions.eq("id", codigoPessoaDestinatario));

			pessoa = (Pessoa) criteria.uniqueResult();
		}
		catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return pessoa;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> recuperarApenasPartesIntegradas(List<Parte> partes) throws DaoException {
		List<Pessoa> listaParte = new ArrayList<Pessoa>();
		
		try {
			if (partes != null && partes.size() > 0){
				Session session = retrieveSession();
				StringBuffer sql = new StringBuffer();
				
				sql.append(
						" SELECT {p.*} " + 
						"  FROM corporativo.pessoa p, " + 
						"       corporativo.grupo g, " + 
						"       corporativo.vinculo v "+ 
						"  WHERE p.seq_pessoa = g.seq_pessoa_representada " + 
						"       AND g.seq_grupo = v.seq_grupo " + 
						"       AND ((v.flg_ativo = 'S'" + 
						"       AND p.flg_cadastro_ratificado = 'S'" + 
						"       AND p.flg_intimacao_pessoal = 'S'" + 
						"       AND p.tip_meio_intimacao = 'E'" + 
						"       AND p.seq_pessoa IN (:idPartes)) OR p.seq_pessoa IN (:idPGR))");
				
				Query q = session.createSQLQuery(sql.toString()).addEntity("p", Pessoa.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				List<Long> idPartes = new ArrayList<Long>();
				
				for(Parte parte : partes)
					idPartes.add(parte.getSeqJurisdicionado());
				
				q.setParameterList("idPartes", idPartes);
				q.setParameterList("idPGR", Arrays.asList(Pessoa.PROCURADOR_GERAL_DA_REPUBLICA,Pessoa.MINISTERIO_PUBLICO_DA_UNIAO));
				
				listaParte = q.list();
			}
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return listaParte;
	}
	
}
