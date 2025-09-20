package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.DeslocamentoComunicacaoDao;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DeslocamentoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class DeslocamentoComunicacaoDaoHibernate extends GenericHibernateDao<DeslocamentoComunicacao, Long> 
implements DeslocamentoComunicacaoDao{
	
	private static final long serialVersionUID = -1177829070152206842L;

	public DeslocamentoComunicacaoDaoHibernate() {
		super(DeslocamentoComunicacao.class);
	}

	public void incluirDeslocamento(Comunicacao comunicacao, Setor setor) throws DaoException{
		
		Session session = retrieveSession();
		
		try {
			atualizarDataSaida(comunicacao.getId());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		try {
			DeslocamentoComunicacao deslocamentoComunicacao = new DeslocamentoComunicacao();
			Date dataEntrada = new Date(System.currentTimeMillis());
			
			deslocamentoComunicacao.setComunicacao(comunicacao);
			deslocamentoComunicacao.setSetor(setor);
			deslocamentoComunicacao.setDataEntrada(dataEntrada);			
			session.save(deslocamentoComunicacao);
			
			session.flush();

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	
		
	}

	private void atualizarDataSaida (Long idComunicacao) throws DaoException {
		
		Session session = retrieveSession();
		try {
		
		StringBuffer sql = new StringBuffer("update judiciario.deslocamento_comunicacao set dat_saida=sysdate where dat_saida is null and seq_comunicacao ="+idComunicacao);
	
		PreparedStatement psUp = session.connection().prepareStatement(sql.toString());
		psUp.executeUpdate(sql.toString());
		psUp.close();

		}catch(SQLException e){
			throw new DaoException("SQLException",e);
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		  
	}
}
