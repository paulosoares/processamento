package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.io.IOException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.lf5.util.StreamUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.ArquivoEletronicoDao;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ArquivoEletronicoPersister;
import br.gov.stf.estf.entidade.documento.ArquivoEletronicoView;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ArquivoEletronicoDaoHibernate extends GenericHibernateDao<ArquivoEletronico, Long> implements ArquivoEletronicoDao {

	private static final String CODIGO_CARREGAR_BLOQUEAR_ARQUIVO = "{call DOC.PKG_RECUPERA_DOCUMENTO.PRC_RECUPERA_DOC_BLOQUEIO(?,?,?,?)}";
	private static final String CODIGO_DESBLOQUEAR_ARQUIVO = "{call DOC.PKG_RECUPERA_DOCUMENTO.prc_desbloqueia_doc (?)}";
	private static final String CODIGO_DESBLOQUEAR_ARQUIVO_ADMIN = "{call DOC.PKG_RECUPERA_DOCUMENTO.prc_desbloqueia_doc_admin (?)}";

	public ArquivoEletronicoDaoHibernate() {
		super(ArquivoEletronico.class);
	}

	private static final long serialVersionUID = 1050795798145722571L;

	public ArquivoEletronico recuperarArquivoEletronico(Long id) throws DaoException {

		ArquivoEletronico arquivoEletronico = null;
		try {
			Session session = retrieveSession();

			Connection connection = session.connection();
			CallableStatement stmt = connection.prepareCall(ArquivoEletronicoPersister.CODIGO_LOAD_STORED_PROCEDURE_CALL);

			stmt.setLong(1, id.longValue());
			stmt.execute();
			stmt.close();

			Criteria criteria = session.createCriteria(ArquivoEletronico.class);
			criteria.add(Restrictions.eq("id", id));
			arquivoEletronico = (ArquivoEletronico) criteria.uniqueResult();

			connection.close();
		}

		catch (Exception e) {
			throw new DaoException(e);
		}

		return arquivoEletronico;
	}

	/**
	 * Recupera um documento específico no formato binário e bloqueia o
	 * documento 
	 * - Verifica inicialmente se este documento está bloqueado. 
	 * - Se o documento estiver bloqueado, retorna o usuário que bloqueou. 
	 * Sempre retorna o documento, independente do bloqueio.
	 * 
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public ArquivoEletronico recuperarBloquearArquivoEletronico(Long id) throws DaoException {
		try {
			CallableStatement stmt = montaChamadaDaProcedure(CODIGO_CARREGAR_BLOQUEAR_ARQUIVO);
			registraParametrosDaProcedureDeBloqueio(id, stmt);
			stmt.execute();
			ArquivoEletronico arquivoResultado = carregaDadosDoArquivoEletronico(id, stmt);
			stmt.close();
			return arquivoResultado;
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}

	private void registraParametrosDaProcedureDeBloqueio(Long id, CallableStatement stmt) throws SQLException {

		stmt.setLong(1, id.longValue());
		stmt.registerOutParameter(2, Types.BLOB);
		stmt.registerOutParameter(3, Types.VARCHAR);
		stmt.registerOutParameter(4, Types.DATE);
	}

	private ArquivoEletronico carregaDadosDoArquivoEletronico(Long id, CallableStatement stmt) throws SQLException, IOException {
		ArquivoEletronico arquivo = new ArquivoEletronico();
		arquivo.setId(id);
		carregaConteudoDoArquivo(stmt, arquivo);
		arquivo.setUsuarioBloqueio(stmt.getString(3));
		arquivo.setDataBloqueio(stmt.getDate(4));
		return arquivo;
	}

	private void carregaConteudoDoArquivo(CallableStatement stmt, ArquivoEletronico arquivo) throws SQLException, IOException {
		Blob conteudo = stmt.getBlob(2);
		if (conteudo != null) {
			arquivo.setConteudo(StreamUtils.getBytes(conteudo.getBinaryStream()));
		}
	}

	/**
	 * Desbloqueia um documento.
	 * Se o documento estiver bloqueado e o usuário da conexão for o mesmo usuário que bloqueou o documento, então desbloqueia.
	 * Se o documento estiver bloqueado por outro usuário, lança exceção
	 * @param id
	 * @throws DaoException
	 */
	public void desbloquearArquivoEletronico(Long id) throws DaoException {
		try {
			CallableStatement call = montaChamadaDaProcedure(CODIGO_DESBLOQUEAR_ARQUIVO);
			call.setLong(1, id);
			call.execute();
		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}
	
	/**
	 * Desbloqueia um documento independetemente do usuário que o tenha bloqueado
	 * @param id
	 * @throws DaoException
	 */
	public void desbloquearArquivoEletronicoAdmin(Long id) throws DaoException {
		try {
			CallableStatement call = montaChamadaDaProcedure(CODIGO_DESBLOQUEAR_ARQUIVO_ADMIN);
			call.setLong(1, id);
			call.execute();
		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	private CallableStatement montaChamadaDaProcedure(String codigoDaProcedure) throws DaoException, SQLException {
		SessionImplementor session = (SessionImplementor) retrieveSession();
		CallableStatement stmt = session.getBatcher().prepareCallableStatement(codigoDaProcedure);
		return stmt;
	}

	@Override
	public ArquivoEletronicoView recuperarArquivoEletronicoViewPeloId(Long id) throws DaoException {
		Session session = retrieveSession();
		String alias = "ae";
		Criteria criteria = session.createCriteria(ArquivoEletronicoView.class, alias);
		criteria.add(Restrictions.eq(alias + ".id", id));
		return (ArquivoEletronicoView) criteria.uniqueResult();
	}

}
