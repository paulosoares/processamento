package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.util.EnderecoView;
import br.gov.stf.estf.localizacao.model.dataaccess.EnderecoDestinatarioDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class EnderecoDestinatarioDaoHibernate extends 
              GenericHibernateDao<EnderecoDestinatario, Long> implements EnderecoDestinatarioDao {

	public EnderecoDestinatarioDaoHibernate() {
		super(EnderecoDestinatario.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<EnderecoDestinatario> recuperarEnderecoDoDestinatario(Long codDestinatario) throws DaoException {
		List<EnderecoDestinatario> enderecos = Collections.emptyList();

		try {
			
			if (codDestinatario == null) {return null;}
			
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT e FROM EnderecoDestinatario e ");
			hql.append("WHERE (e.destinatario.id = :codDestinatario)");

			Query q = session.createQuery(hql.toString());
			q.setParameter("codDestinatario", codDestinatario);
			
			enderecos = q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return enderecos;
	}

	@Override
	public EnderecoView recuperarEnderecoView(Integer cep)
			throws DaoException {
		try {
			Session session = retrieveSession();
			
			if( cep == null )
				return null;
			
			Statement stmt = session.connection().createStatement();
			String sql = " select nom_logradouro,num_cep,nom_bairro,nom_municipio,sig_uf " +
						 " from corp.vw_endereco " +
						 " where num_cep = "+cep;
			ResultSet rs = stmt.executeQuery(sql);
			while( rs.next() ){
				EnderecoView end = new EnderecoView();
				end.bairro = rs.getString("nom_bairro");
				end.cep = rs.getInt("num_cep");
				end.cidade = rs.getString("nom_municipio");
				end.logradouro = rs.getString("nom_logradouro");
				end.UF = rs.getString("sig_uf");
				return end;
			}
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return null;
	}
	
	
	public List<String> pesquisarUF() throws DaoException {
		List<String> list = new LinkedList<String>();
		
		try {
			Session session = retrieveSession();
			
			Statement stmt = session.connection().createStatement();
			String sql = "SELECT sig_unidade_federacao FROM corp.unidade_federacao";
			ResultSet rs = stmt.executeQuery(sql);
			while( rs.next() ){
				list.add(rs.getString("sig_unidade_federacao"));
			}
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return list;
	}

}
