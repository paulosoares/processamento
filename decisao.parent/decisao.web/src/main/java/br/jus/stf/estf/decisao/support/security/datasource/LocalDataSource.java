package br.jus.stf.estf.decisao.support.security.datasource;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import br.jus.stf.estf.decisao.support.security.Principal;

/**
 * Utilizado para validar a conexão retornada pelo Data Source verificando
 * se o usuário associado à conexão pela package de segurança é igual
 * ao usuário logado.
 * 
 * @author Rodrigo Barreiros
 * @since 25.08.2010
 */
public class LocalDataSource implements DataSource {

	private static final Log logger = LogFactory.getLog(LocalDataSource.class);
	
	private static final String SIGLA_SISTEMA = "ESTFDECISAO";
	
	private DataSource dataSource;
	
	/**
	 * Constroi o data source local encapsulando o data source original
	 * 
	 * @param dataSource o data source original
	 */
	public LocalDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		// Recuperando conexão disponibilizada pelo data source...
		Connection connection = dataSource.getConnection();
		
		// Recuperando objeto de autenticação do Spring Security...
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// Algumas operações poderão acontecer antes da autenticação (authentication == null).
		if (authentication != null) {
			// Registrando usuário logado, na conexão retornada do data source...
	        register(connection, authentication);
	        
	        // Se usuário registrado com sucesso, criamos um wrapper para a conexão real.
	        // Esse wrapper vai fornecer comportamento específico para o método "connection.close", 
	        // limpando o contexto de segurança antes de fechar a conexão.
	        connection = new LocalConnection(connection);
	        
	        // Comparando o usuário registrado no banco com o usuário logado...
	        validate(connection, authentication);
		}
		
		// Retornando conexão...
		return connection;
	}

	/**
	 * Registra o usuário logado na conexão recuperada do data source.
	 * 
	 * @param connection a conexão disponibilizada pelo data source.
	 * @param authentication o objeto contendo os dados de autenticação.
	 */
	public void register(Connection connection, Authentication authentication) throws SQLException {
		String loggedUser = null;
		
		if (authentication.getPrincipal() instanceof String)
			loggedUser = ((String) authentication.getPrincipal()).toLowerCase().trim();
		
		if (authentication.getPrincipal() instanceof Principal)
			loggedUser = ((Principal) authentication.getPrincipal()).getUsuario().getId().trim();
		
		CallableStatement stmt = null;
		String host = getHost();
		try {
			// Registrando usuário via procedure de seguranca...
			stmt = connection.prepareCall("{call GLOBAL.PKG_SEGURANCA.PRC_SEGURANCA(?,?,?)}");
			stmt.setString(1, SIGLA_SISTEMA);
			stmt.setString(2, loggedUser);
			stmt.setString(3, host);
			
			stmt.execute();
        } catch (SQLException e) {
        	throw new SQLException(String.format("Problemas ao tentar executar a procedure de seguranca (sistema=%s, usuario=%s, host=%s).", SIGLA_SISTEMA, loggedUser, host), e);
		} finally {
			JdbcUtils.closeStatement(stmt);
		}
	}

	/**
	 * Recupera o host de origem da requisição.
	 * 
	 * @return o host de origem
	 */
	private String getHost() {
		String host = null;
		try {
			host = ((HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest")).getLocalAddr();
		} catch (PolicyContextException e) {
			logger.warn("Nao foi possivel recuperar o host de origem da requisicao, setando null na chamada a package de seguranca...", e);
		}
		return host;
	}	
	
	/**
	 * Valida se o usuário registrado na conexão é igual ao usuário logado. 
	 * 
	 * @param connection a conexão disponibilizada pelo data source.
	 * @param authentication o objeto contendo os dados de autenticação.
	 */
	private void validate(Connection connection, Authentication authentication) throws SQLException {
		String connectionUser = getConnectionUser(connection);

		String loggedUser = null;
		
		if (authentication.getPrincipal() instanceof String)
			loggedUser = ((String) authentication.getPrincipal()).toLowerCase().trim();
		
		if (authentication.getPrincipal() instanceof Principal)
			loggedUser = ((Principal) authentication.getPrincipal()).getUsuario().getId().trim().toLowerCase();
		
		
		// Se o usuário registrado no banco for diferente do usuário logado, devemos indicar o erro.
		if (!loggedUser.equalsIgnoreCase(connectionUser)) {
			JdbcUtils.closeConnection(connection);
			throw new IllegalStateException(
				String.format(
					"Usuario da conexao (%s) diferente do usuario logado (%s).", 
					connectionUser, 
					loggedUser
				)
			);
		}
	}

	/**
	 * Recupera o usuário associado à conexão pela package de segurança.
	 * 
	 * @param connection a conexão com a base
	 * @return o usuário da conexão
	 */
	private String getConnectionUser(Connection connection) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("select sys_context('context_global', 'sig_usuario') from dual");
            rs = stmt.executeQuery();
            if (rs.next()) {
            	return rs.getString(1).trim().toLowerCase();
            }
        } catch (SQLException e) {
        	throw new SQLException("Problemas ao tentar recuperar o usuario registrado na conexao.", e);
		} finally {
			JdbcUtils.closeStatement(stmt);
			JdbcUtils.closeResultSet(rs);
        }
    	return null;
	}

	/**
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		return dataSource.getConnection(username, password);
	}

	/**
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	/**
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	/**
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	public void setLoginTimeout(int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}

	/**
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	public void setLogWriter(PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}

	/**
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	/**
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}
	
	/**
	 * Retorna o Data Source encapsulado.
	 * 
	 * @return o Data Source de origem
	 */
	public DataSource getWrapped() {
		return dataSource;
	}

}
