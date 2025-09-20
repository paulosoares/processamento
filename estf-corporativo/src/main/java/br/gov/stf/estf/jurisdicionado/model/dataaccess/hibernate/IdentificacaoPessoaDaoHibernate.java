package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.IdentificacaoPessoaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class IdentificacaoPessoaDaoHibernate extends GenericHibernateDao<IdentificacaoPessoa, Long>
		implements IdentificacaoPessoaDao {

	private static final long serialVersionUID = 5134499306418006125L;

	public IdentificacaoPessoaDaoHibernate() {
		super(IdentificacaoPessoa.class);
	}

	@Override
	public List<IdentificacaoPessoa> verificaExistenciaCadastro(String identificacao,
			TipoIdentificacao tipo) throws DaoException {
		
		List<IdentificacaoPessoa> listaI = new ArrayList<IdentificacaoPessoa>();
		
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(IdentificacaoPessoa.class);

			c.add(Restrictions
					.eq("descricaoIdentificacao", identificacao.toUpperCase()));
			
			c.add(Restrictions
					.eq("tipoIdentificacao.id", tipo.getId()));
			
			listaI = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaI;
	}

}
