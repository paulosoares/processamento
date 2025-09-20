package br.gov.stf.estf.processostf.model.dataaccess.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProtocoloDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AndamentoProtocoloDaoJDBC extends GenericHibernateDao<AndamentoProtocolo, Long> implements AndamentoProtocoloDao {

	public AndamentoProtocoloDaoJDBC() {
		super(AndamentoProtocolo.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3655467480289758062L;

	public Long recuperarUltimoNumeroSequencia(Protocolo protocolo) throws DaoException {
		Long sequencia = null;
		try {

			StringBuffer sql = new StringBuffer();
			sql.append(" select max(num_sequencia) as num_seq from stf.andamento_protocolo ");
			sql.append(" where num_protocolo = ? ");
			sql.append(" and ano_protocolo = ? ");

			PreparedStatement stm = retrieveSession().connection().prepareStatement(sql.toString());
			stm.setLong(1, protocolo.getNumeroProtocolo());
			stm.setShort(2, protocolo.getAnoProtocolo());

			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				sequencia = rs.getLong("num_seq");
			}
			stm.close();
			rs.close();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return sequencia;
	}

	private Long getNextSeq(Connection con, String nomeSeq) throws SQLException, HibernateException, DaoException {
		StringBuffer sql = new StringBuffer(" select " + nomeSeq + ".NEXTVAL from dual ");
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql.toString());
		Long nextVal = null;
		if (rs.next()) {
			nextVal = rs.getLong("NEXTVAL");
		}
		stm.close();
		rs.close();
		return nextVal;
	}

	@Override
	public AndamentoProtocolo incluir(AndamentoProtocolo andamentoProtocolo) throws DaoException {
		try {

			Connection con = retrieveSession().connection();

			StringBuffer sql = new StringBuffer();
			sql
					.append(" insert into stf.andamento_protocolo (cod_andamento, dat_andamento, ano_protocolo, num_protocolo, dat_hora_sistema, dsc_obser_and, num_sequencia, cod_setor, sig_usuario, flg_valido, seq_andamento_protocolo) ");
			sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");

			PreparedStatement ps = retrieveSession().connection().prepareStatement(sql.toString());
			Date dataAtual = new Date(new java.util.Date().getTime());
			ps.setLong(1, andamentoProtocolo.getCodigoAndamento());
			ps.setDate(2, dataAtual);
			ps.setShort(3, andamentoProtocolo.getProtocolo().getAnoProtocolo());
			ps.setLong(4, andamentoProtocolo.getProtocolo().getNumeroProtocolo());
			ps.setDate(5, dataAtual);
			ps.setString(6, andamentoProtocolo.getDescricaoObservacaoAndamento());
			ps.setLong(7, andamentoProtocolo.getNumeroSequencia());
			ps.setLong(8, andamentoProtocolo.getSetor().getId());
			ps.setString(9, andamentoProtocolo.getSiglaUsuario().toUpperCase());
			ps.setBoolean(10, andamentoProtocolo.getValido());
			Long seqAndamentoProtocolo = getNextSeq(con, "stf.seq_andamento_protocolo");
			ps.setLong(11, seqAndamentoProtocolo);

			ps.executeUpdate();
			ps.close();

			andamentoProtocolo.setId(seqAndamentoProtocolo);

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return andamentoProtocolo;
	}

	public void persistirAndamentoProtocolo(AndamentoProtocolo andamentoProtocolo) throws DaoException {
		Session sessao = retrieveSession();
		
		try {
			
			sessao.persist(andamentoProtocolo);
			sessao.flush();
			
		} catch ( Exception e) {
			throw new DaoException(e);  
		}

	}

}
