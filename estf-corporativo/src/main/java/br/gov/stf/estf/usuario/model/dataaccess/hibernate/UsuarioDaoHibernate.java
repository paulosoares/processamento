package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.CargaClasseProcessualTipoJulgamento;
import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.estf.entidade.usuario.GrupoUsuario;
import br.gov.stf.estf.entidade.usuario.PessoaEmail;
import br.gov.stf.estf.entidade.usuario.PessoaTelefone;
import br.gov.stf.estf.entidade.usuario.TipoConfiguracaoUsuario;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.usuario.UsuarioDistribuicao;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioDao;
import br.gov.stf.estf.usuario.model.util.TipoUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;

@Repository
public class UsuarioDaoHibernate extends GenericHibernateDao<Usuario, String> implements UsuarioDao { 
    private static final long serialVersionUID = 1L;
    public UsuarioDaoHibernate() {
    	super(Usuario.class);
    }
	@SuppressWarnings("unchecked")
	public List<Usuario> pesquisaUsuario(String id, String nome, Boolean ativo,
			Long idSetor) throws DaoException {
		
		List<Usuario> lista;
		
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Usuario.class);
			
			if( SearchData.stringNotEmpty(id) ){
				c.add( Restrictions.eq("id", id) );
			}
			
			if( SearchData.stringNotEmpty(nome) ){
				nome = nome.replace("|", "\\|");
				c.add(Restrictions.ilike("nome", nome.trim(), MatchMode.ANYWHERE));
			}

			if( ativo != null ){
				c.add( Restrictions.eq("ativo", ativo) );
			}
			
			if( idSetor != null ){
				c.add( Restrictions.eq("setor.id", idSetor) );
			}
			c.addOrder(Order.asc("nome"));
			lista = c.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return lista;
	}
	
	public Usuario recuperarUsuario(String sigla) 
	throws DaoException {

		Session session = retrieveSession();

		Usuario usuario = null;

		try {

			Criteria criteria = session.createCriteria(Usuario.class);

			criteria.add(Restrictions.eq("id", sigla));

			usuario = (Usuario) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return usuario;
	}

	public GrupoUsuario recuperarGrupoUsuario(Long idGrupo) 
	throws DaoException {
		Session session = retrieveSession();

		GrupoUsuario grupoUsuario = null;

		try {

			Criteria criteria = session.createCriteria(GrupoUsuario.class);

			criteria.add(Restrictions.eq("id", idGrupo));

			grupoUsuario = (GrupoUsuario) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return grupoUsuario;		
	}
	
	public UsuarioEGab recuperarUsuarioEGab(String sigla, Boolean padrao)
	throws DaoException {

		Session session = retrieveSession();

		List<UsuarioEGab> listaUsuarios = null;

		UsuarioEGab usuarioEGab = null;

		try {

			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT u FROM UsuarioEGab u ");
			hql.append(" WHERE ");
			hql.append(" u.usuario.id = '" + sigla.toUpperCase() + "' ");

			if(padrao != null && Boolean.TRUE.booleanValue() == padrao.booleanValue()){
				hql.append(" AND u.setorAtual='S'");
			}

			Query query = session.createQuery(hql.toString());

			listaUsuarios = query.list();  

			if(listaUsuarios != null && listaUsuarios.size() == 1){			
				usuarioEGab = listaUsuarios.get(0);
			}
			else if(listaUsuarios != null && listaUsuarios.size() > 1){
				for(UsuarioEGab usuario : listaUsuarios){
					if(usuario.getSetorAtual() == Boolean.TRUE.booleanValue()){
						usuarioEGab = usuario;
					}
				}	
				if(usuarioEGab == null)
					usuarioEGab = listaUsuarios.get(0);
			}
			else if(listaUsuarios == null || listaUsuarios.size() <= 0){
				usuarioEGab = null;
			}				
		}

		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}			

		return usuarioEGab;
	}	

	public List<UsuarioEGab> recuperarTodosUsuariosEGab(String sigla)
	throws DaoException {

		Session session = retrieveSession();

		List<UsuarioEGab> listaUsuarios = null;		

		try {

			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT u FROM UsuarioEGab u ");
			hql.append(" WHERE ");
			hql.append(" u.usuario.id = '" + sigla.toUpperCase() + "' ");				

			Query query = session.createQuery(hql.toString());

			listaUsuarios = query.list();  

			if(listaUsuarios == null || listaUsuarios.size() <= 0){			
				listaUsuarios = null;
			}

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}			

		return listaUsuarios;
	}	

	public Boolean alterarUsuarioPadraoEGab(UsuarioEGab usuarioEGab) 
	throws DaoException{

		Boolean alterado = Boolean.FALSE;
		Session sessao = retrieveSession();

		try {

			sessao.update(usuarioEGab);
			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;		
	}	

	public List<UsuarioEGab> pesquisarUsuarioSecao(Long codigoSetor, Long codigoSecao, boolean usuarioNaoCadastrado) throws DaoException {
		Session session = retrieveSession();

		List<UsuarioEGab> usuarios = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			StringBuffer sql = new StringBuffer (
					" SELECT usu.sig_usuario " +
			" FROM stf.usuarios usu, EGAB.usuario_setor ust WHERE (1=1) ");

			if(codigoSetor != null) {
				sql.append("AND ust.COD_SETOR = "+codigoSetor);		
			}

			sql.append(" AND usu.sig_usuario = ust.sig_usuario" );
			sql.append(" AND usu.flg_ativo = 'S' ");
			sql.append(" AND usu.SIG_USUARIO ");

			if(usuarioNaoCadastrado&&codigoSetor != null){
				sql.append(" NOT IN ( SELECT usec.sig_usuario from "+ 
						" 	egab.usuario_secao usec, "+
						" 	egab.secao_setor ss "+
						" 	WHERE ss.cod_setor = "+ codigoSetor + 
						"         AND ss.flg_ativo = 'S' " +
						" 	      AND ss.seq_secao_setor = usec.seq_secao_setor )");
			}
			else {
				sql.append(" IN (SELECT DISTINCT us.sig_usuario " +
						"    FROM EGAB.secao_setor s, EGAB.usuario_secao us" +
				"    WHERE (1=1)");

				if(codigoSecao!=null){
					sql.append(" AND s.seq_secao ="+codigoSecao + 
					" AND s.seq_secao_setor = us.seq_secao_setor ) ");
				}

			}

			sql.append(" ORDER BY usu.nom_usuario ");


			//System.out.println(hql.toString());
			List lista = new ArrayList();
			stmt = session.connection().createStatement();
			rs = stmt.executeQuery(sql.toString());

			StringBuffer in = null;

			while(rs.next()){
				if(in == null) {
					in = new StringBuffer("IN ('" + rs.getString("sig_usuario") + "'");
				}
				else {
					in.append(",'" + rs.getString("sig_usuario") + "'");
				}
			}	

			if(in != null) {

				in.append(") ");

				StringBuffer hql2 = new StringBuffer(" SELECT u FROM UsuarioEGab u WHERE " +
						" u.usuario.id " + in.toString() + " ");

				if(codigoSetor != null) {
					hql2.append("AND u.setor.id = " + codigoSetor);
				}

				hql2.append(" ORDER BY u.usuario.nome ");

				Query query = session.createQuery(hql2.toString());

				usuarios = query.list();
			}

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		catch(Exception e){
			throw new DaoException("Exception", e);
		} finally{
			try{
				if( rs != null )
					rs.close();
				if( stmt != null )
					stmt.close();
			}catch(SQLException e){
				throw new DaoException("SQLException Finally", e);
			}
		}

		return usuarios;		
	}


	public List<Usuario> pesquisarUsuariosEgab(Long codigoSetor, Boolean ativo) throws DaoException {

		List<Usuario> usuarios = null;
		
		StringBuffer sql = new StringBuffer();
		
		try{
			sql.append(" SELECT U.* FROM STF.USUARIOS U ");
		    	
    		sql.append(" , EGAB.USUARIO_SETOR US ");
    		sql.append(" WHERE U.SIG_USUARIO = US.SIG_USUARIO ");
			if( codigoSetor != null){
				sql.append(" AND US.COD_SETOR = "+codigoSetor);
			}
			if( ativo != null ) {

				String strAtivo = null;
				if(ativo != null) {
					if( ativo.booleanValue() ) {
						strAtivo = "S";
					}else {
						strAtivo = "N";}
				}
				sql.append(" AND US.FLG_SETOR_ATUAL = '"+strAtivo+"'");
			}
	    	
			sql.append(" ORDER BY U.SIG_USUARIO ");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
			
			
			sqlQuery.addEntity(Usuario.class);
			usuarios = sqlQuery.list();
	    	
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		
		return usuarios;
	}

	
	public List<Usuario> pesquisarUsuariosEgabPlantao(Long codigoSetor, Boolean ativo) throws DaoException {

		List<Usuario> usuarios = null;
		
		StringBuffer sql = new StringBuffer();
		
		try{
			sql.append(" SELECT u.* ");
			sql.append("  FROM stf.usuarios u, ");
			sql.append("      egab.usuario_setor us, " );
			sql.append("      egab.grupo_usuario gu, "); 
			sql.append("       egab.usuario_grupo ug ");
			sql.append(" WHERE     u.sig_usuario = us.sig_usuario " );
			sql.append("      AND u.sig_usuario = ug.sig_usuario " );
			sql.append("       AND gu.seq_grupo_usuario = ug.seq_grupo_usuario " );
			sql.append("       AND us.cod_setor = " + codigoSetor );
			sql.append("       AND GU.dsc_grupo like '%ATRIBUI% DOS RESPONS%VEIS PELOS MANDADOS%' "); 
			
		
			if( ativo != null ) {

				String strAtivo = null;
				if(ativo != null) {
					if( ativo.booleanValue() ) {
						strAtivo = "S";
					}else {
						strAtivo = "N";}
				}
				sql.append(" AND US.FLG_SETOR_ATUAL = '"+strAtivo+"'");
			}
	    	
			sql.append(" ORDER BY U.SIG_USUARIO ");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
			
			
			sqlQuery.addEntity(Usuario.class);
			usuarios = sqlQuery.list();
	    	
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		
		return usuarios;
	}

	public List<UsuarioEGab> pesquisarUsuarios(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo) throws DaoException {

		Session session = retrieveSession();

		List<UsuarioEGab> usuarios = null;

		try {

			StringBuffer hql = new StringBuffer(
					" SELECT DISTINCT u " +
					" FROM UsuarioEGab u " +
					" JOIN FETCH u.usuario us " +
			" WHERE 1 = 1 ");

			if( codigoSetor != null)
				hql.append(" AND u.setor.id = "+codigoSetor);

			if( tipoUsuario != null)
				hql.append(" AND u.tipoUsuario = '"+tipoUsuario.name()+"'");

			if( ativo != null ) {

				String strAtivo = null;

				if( ativo.booleanValue() )
					strAtivo = "S";
				else
					strAtivo = "N";

				hql.append(" AND us.ativo = '"+strAtivo+"'");
			}

			hql.append(" ORDER BY us.nome ");

			Query query = session.createQuery(hql.toString());

			usuarios = query.list();                

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return usuarios;		
	}

	public List<UsuarioEGab> pesquisarUsuariosGrupo(Long grupoId, Boolean usuarioAtivo) throws DaoException {

		Session session = retrieveSession();

		List<UsuarioEGab> usuarios = null;

		try {

			StringBuffer hql = new StringBuffer(
					" SELECT DISTINCT u " +
					" FROM UsuarioEGab u " +
					" JOIN FETCH u.usuario us " +
					" JOIN u.grupos g " +
			" WHERE 1 = 1 ");

			if( grupoId != null)
				hql.append(" AND g.id = "+grupoId);

			if( usuarioAtivo != null ) {

				String strAtivo = null;

				if( usuarioAtivo.booleanValue() )
					strAtivo = "S";
				else
					strAtivo = "N";

				hql.append(" AND us.ativo = '"+strAtivo+"'");
			}

			hql.append(" ORDER BY us.nome ");

			Query query = session.createQuery(hql.toString());

			usuarios = query.list();              

			/*
        	Criteria criteria = session.createCriteria(UsuarioEGab.class, "u");

        	if( usuarioAtivo != null )
        		criteria.add(Restrictions.eq("u.usuario.ativo", usuarioAtivo));        	

        	if( grupoId != null)
        		criteria = criteria.createCriteria("u.grupos").add( Restrictions.eq("id", grupoId));


        	usuarios = criteria.list();
			 */

			//Query query = session.createQuery();
			//List result = query.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return usuarios;		
	}	

	@Override
	public List<GrupoUsuario> pesquisarGruposUsuario(Long codigoSetor, String descricaoGrupo) throws DaoException {
		return pesquisarGruposUsuario(codigoSetor, descricaoGrupo, null);
	}
	
	@Override
	public List<GrupoUsuario> pesquisarGruposUsuario(Long codigoSetor, String descricaoGrupo, Boolean ativo) throws DaoException {
		Session session = retrieveSession();

		List<GrupoUsuario> grupos = null;

		try {

			StringBuffer hql = new StringBuffer(" SELECT g " +
					" FROM GrupoUsuario g " +
			" WHERE (1=1)");

			if( codigoSetor != null)
				hql.append(" AND g.setor.id = "+ codigoSetor);

			if( descricaoGrupo != null && !descricaoGrupo.equals("") )
				hql.append(" AND g.descricao LIKE '%"+ descricaoGrupo.toUpperCase()+"%' ");

			if (ativo != null) {
				if (ativo == Boolean.TRUE)
					hql.append(" AND g.ativo = 'S' ");
				else 
					hql.append(" AND g.ativo = 'N' ");
			}
				
			Query query = session.createQuery(hql.toString());
			grupos = query.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return grupos;		
	}

	public Boolean verificarUniciadadeGrupoUsuario(String descricao,Long idSetor)throws DaoException {
		Session session = retrieveSession();

		Boolean retorno;

		try {

			StringBuffer hql = new StringBuffer(" SELECT g FROM GrupoUsuario g " +
			" WHERE (1=1) ");

			if( idSetor != null)
				hql.append(" AND g.setor.id = "+ idSetor);

			if( descricao != null && !descricao.equals("") )
				hql.append(" AND g.descricao = '"+ descricao.toUpperCase()+"'");

			Query query = session.createQuery(hql.toString());
			List lista = query.list();
			if(lista !=null&&lista.size()>0){
				retorno = Boolean.TRUE;
			}else{
				retorno = Boolean.FALSE;
			}
		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return retorno;	
	}

	public List<UsuarioDistribuicao> pesquisarUsuariosDistribuicao(Long codigoSetor, TipoUsuario tipoUsuario, 
			Boolean ativo, 
			Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual, Boolean zeraContagem, String dataContagem) throws DaoException {

		Session session = retrieveSession();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;

		List<UsuarioDistribuicao> usuarios = new LinkedList<UsuarioDistribuicao>();

		try {

			StringBuffer sql = new StringBuffer(
					" SELECT DISTINCT                                             "+
					"    u.SIG_USUARIO,						                      "+
					"    u.NOM_USUARIO,                                           "+
					"    u.FLG_ATIVO,                                             "+
					"    u.NUM_MATRICULA,                                         "+
					"    us.TIP_USUARIO_SETOR,                                    "+
					"    s.COD_SETOR,                                             "+
					"    s.DSC_SETOR,                                             "+
					"    s.SIG_SETOR,                                             "+
					"    (SELECT COUNT(*) FROM EGAB.processo_setor ps             "+
					"      INNER JOIN EGAB.historico_distribuicao hd              "+
					"         ON hd.seq_historico_distribuicao =                  "+
					"            ps.seq_distribuicao_atual                        "+ 
					"      INNER JOIN EGAB.usuario_setor us                       "+
					"         ON us.sig_usuario = hd.sig_usuario_analise          ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {
				sql.append( "LEFT JOIN (SELECT sig_classe_proces, num_processo, cod_recurso, seq_objeto_incidente "+ 
				"	FROM judiciario.recurso_processo rp 								"+
				"	WHERE rp.dat_interposicao = 								"+
				"	(SELECT MAX (dat_interposicao) 								"+
				"	FROM judiciario.recurso_processo rpmax 							"+
				"	WHERE rpmax.seq_objeto_incidente = rp.seq_objeto_incidente)) usp 				"+
				"	ON ps.seq_objeto_incidente = usp.seq_objeto_incidente " );
				
//				sql.append( "LEFT JOIN (SELECT sig_classe_proces, num_processo, cod_recurso "+ 
//							"	FROM stf.recurso_processos rp 								"+
//							"	WHERE rp.dat_interposicao = 								"+
//							"	(SELECT MAX (dat_interposicao) 								"+
//							"	FROM stf.recurso_processos rpmax 							"+
//							"	WHERE rpmax.sig_classe_proces = rp.sig_classe_proces 		"+
//							"	AND rpmax.num_processo = rp.num_processo)) usp 				"+
//							"	ON ps.sig_classe_proces = usp.sig_classe_proces AND ps.num_processo = usp.num_processo " );
			}
					
			sql.append( "      WHERE hd.sig_usuario_analise = u.sig_usuario "+
						"        AND ps.cod_setor = us.cod_setor            "+
						"        AND ps.cod_setor = ?						");
						
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {			
				sql.append( " and not exists (select * from judiciario.incidente_julgamento ij, judiciario.tipo_recurso tr where " +
				"                                  ij.seq_tipo_recurso = tr.seq_tipo_recurso and tip_objeto_incidente='IJ' AND " +
				"                                  sig_tipo_recurso='MC' and ij.seq_objeto_incidente = ps.seq_objeto_incidente)	"+
				"	AND ((usp.cod_recurso IS NULL or usp.cod_recurso = 0) 									"+
				"				OR (usp.cod_recurso IS NOT NULL AND ps.seq_objeto_incidente = usp.seq_objeto_incidente))		");				
				
//				sql.append( " 	AND ps.tip_julgamento <> '" + TipoIncidenteJulgamento.SIGLA_MEDIDA_CAUTELAR + "' 	"+
//							"	AND ((usp.cod_recurso IS NULL AND ps.cod_recurso = 0) 									"+
//							"				OR (usp.cod_recurso IS NOT NULL AND ps.cod_recurso = usp.cod_recurso))		");
			}

			if( contagemFimTramite != null && contagemFimTramite.booleanValue() ) 
				sql.append(" AND ps.dat_fim_tramite IS NULL ");

			// A pedido da PSTF (ANDRELN) a contagem da carga será zerada a partir de fevereiro. 
			if( contagemSaidaSetor != null && contagemSaidaSetor.booleanValue() ) {
				sql.append(" AND ps.dat_saida_setor IS NULL ");          
			}
			// A pedido da PSTF (ANDRELN) a contagem da carga será zerada a partir de fevereiro. 
			if( zeraContagem != null && zeraContagem.booleanValue()  ) {
				if (dataContagem != null && dataContagem != "") {
					sql.append(" AND hd.dat_distribuicao >= to_date ('" + dataContagem + "','dd/mm/yyyy') ");
				}
			}
			
			
			if( codigoSetor != null ) {
				sql.append(           	
						"    AND ps.cod_setor = "+codigoSetor);
			}

			sql.append(" ) AS carga,                             "+
					"    (SELECT SUM(ps.num_indicador_gravidade * ps.num_indicador_urgencia * ps.num_indicador_tendencia) "+ 
					"       FROM EGAB.processo_setor ps                 "+
					"      INNER JOIN EGAB.historico_distribuicao hd    "+   
					"         ON hd.seq_historico_distribuicao =        "+
					"            ps.seq_distribuicao_atual              "+
					"      INNER JOIN EGAB.usuario_setor us             "+
					"         ON us.sig_usuario = hd.sig_usuario_analise ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {
				sql.append( "LEFT JOIN (SELECT sig_classe_proces, num_processo, cod_recurso, seq_objeto_incidente "+ 
							"	FROM judiciario.recurso_processo rp 								"+
							"	WHERE rp.dat_interposicao = 								"+
							"	(SELECT MAX (dat_interposicao) 								"+
							"	FROM judiciario.recurso_processo rpmax 							"+
							"	WHERE rpmax.seq_objeto_incidente = rp.seq_objeto_incidente)) usp 	"+
							"	ON ps.seq_objeto_incidente = usp.seq_objeto_incidente " );
			}		
					
			sql.append( "      WHERE hd.sig_usuario_analise = u.sig_usuario "+
						"        AND ps.cod_setor = us.cod_setor            "+
						"        AND ps.cod_setor = ?						");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {		
				sql.append( " and not exists (select * from judiciario.incidente_julgamento ij, judiciario.tipo_recurso tr where " +
						"                                  ij.seq_tipo_recurso = tr.seq_tipo_recurso and tip_objeto_incidente='IJ' AND " +
						"                                  sig_tipo_recurso='MC' and ij.seq_objeto_incidente = ps.seq_objeto_incidente)	"+
						"	AND ((usp.cod_recurso IS NULL or usp.cod_recurso = 0) 									"+
						"				OR (usp.cod_recurso IS NOT NULL AND ps.seq_objeto_incidente = usp.seq_objeto_incidente))		");						
				
//				sql.append( " 	AND ps.tip_julgamento <> '" + TipoIncidenteJulgamento.SIGLA_MEDIDA_CAUTELAR + "' 	"+
//							"	AND ((usp.cod_recurso IS NULL AND ps.cod_recurso = 0) 									"+
//							"				OR (usp.cod_recurso IS NOT NULL AND ps.cod_recurso = usp.cod_recurso))		");
			}			

			if( contagemFimTramite != null && contagemFimTramite.booleanValue() ) 
				sql.append(" AND ps.dat_fim_tramite IS NULL ");

			// A pedido da PSTF (ANDRELN) a contagem da carga será zerada a partir de fevereiro. 
			if( contagemSaidaSetor != null && contagemSaidaSetor.booleanValue() ) {
				sql.append(" AND ps.dat_saida_setor IS NULL ");          
			}
			// A pedido da PSTF (ANDRELN) a contagem da carga será zerada a partir de fevereiro. 
			if( zeraContagem != null && zeraContagem.booleanValue()) {
				if (dataContagem != null && dataContagem != "") {
					sql.append(" AND hd.dat_distribuicao >= to_date ('" + dataContagem + "','dd/mm/yyyy') ");
				}
			}

			if( codigoSetor != null ) {
				sql.append(           	
						"    AND ps.cod_setor = "+codigoSetor);
			}
			
			sql.append(" ) AS peso                        "+
					" FROM stf.usuarios u						  "+
					" INNER JOIN EGAB.usuario_setor us            "+
					" ON us.sig_usuario = u.sig_usuario           "+
					" INNER JOIN stf.setores s                    "+ 
					" ON s.cod_setor = us.cod_setor               "+ 
			" WHERE 1=1                                   ");


			if( codigoSetor != null ) {
				sql.append(
				"    AND us.COD_SETOR=?                       ");
			}

			if( tipoUsuario != null ) {
				sql.append(
				"    AND us.TIP_USUARIO_SETOR=?                       ");
			}           	

			if( ativo != null ) {
				sql.append(           	
				"    AND u.FLG_ATIVO=?        	              ");
			}
			sql.append(           	
			"    ORDER BY u.nom_usuario    	              ");

//			System.out.println("SQL pesquisa usuários distribuição: " + sql.toString());

			conn = session.connection();

			pstmt =  conn.prepareStatement(sql.toString());

			int index = 1;
			if( codigoSetor != null ) {
				pstmt.setLong(index++, codigoSetor);
				pstmt.setLong(index++, codigoSetor);
				pstmt.setLong(index++, codigoSetor);
			}

			if( tipoUsuario != null ) {
				pstmt.setString(index++, tipoUsuario.getCodigo());
			}            	

			if( ativo != null ) {
				if( ativo == Boolean.TRUE )
					pstmt.setString(index++, "S");
				else 
					pstmt.setString(index++, "N");
			}

			rs = pstmt.executeQuery();

			while( rs.next() ) {
				UsuarioDistribuicao usuario = new UsuarioDistribuicao();

				usuario.getUsuario().setId(rs.getString("SIG_USUARIO"));
				usuario.getUsuario().setNome(rs.getString("NOM_USUARIO"));

				if( "S".equalsIgnoreCase(rs.getString("FLG_ATIVO")))
					usuario.getUsuario().setAtivo(Boolean.TRUE);
				else if( "N".equalsIgnoreCase(rs.getString("FLG_ATIVO")))
					usuario.getUsuario().setAtivo(Boolean.FALSE);

				usuario.getUsuario().setMatricula(rs.getString("NUM_MATRICULA"));

				if( TipoUsuario.AN.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.AN);
				else if( TipoUsuario.TC.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.TC);           		
				else if( TipoUsuario.AS.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.AS);
				else if( TipoUsuario.OU.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.OU);           		
				else if( TipoUsuario.IN.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.IN);           		
				
				usuario.setCarga(rs.getLong("carga"));
				
				/*if( usuario.getCarga() != null && usuario.getCarga() > 0 ) {
					usuario.setListaCargaClasseProcessualTipoJulgamento( recuperarCargaClasse(codigoSetor, tipoUsuario, usuario.getUsuario().getSigla(), contagemSaidaSetor, contagemFimTramite, cargaDistribuicaoFaseAtual) );
				}*/
				
				
				usuario.setCargaComPeso(rs.getLong("peso"));

				Setor setor = new Setor();
				setor.setId(rs.getLong("COD_SETOR"));
				setor.setSigla(rs.getString("SIG_SETOR"));
				setor.setNome(rs.getString("DSC_SETOR"));

				usuario.setSetor(setor);

				usuarios.add(usuario);
			}

			//Query query = session.createQuery();
			//List result = query.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( SQLException e ) {
			throw new DaoException("SQLException", e);
		}        
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}finally{
			try{
				if( pstmt != null )
					pstmt.close();
				if( rs != null )
					rs.close();
			}catch(SQLException e){
				throw new DaoException("SQLException Finally", e);
			}
		}
		
		return usuarios;		
	}
	
	public List<CargaClasseProcessualTipoJulgamento> recuperarCargaClasse( Long codigoSetor, TipoUsuario tipoUsuario, String siglaUsuario, Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual ) throws DaoException {
		List<CargaClasseProcessualTipoJulgamento> resultado = new LinkedList<CargaClasseProcessualTipoJulgamento>();
		
		Session session = retrieveSession();					
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT ");
			sql.append(" ps.sig_classe_proces, usp.tip_julgamento, COUNT (ps.sig_classe_proces) AS carga ");
			// sql.append(" SUM (ps.num_indicador_gravidade * ps.num_indicador_urgencia * ps.num_indicador_tendencia) peso ");
			sql.append("   FROM stf.usuarios u, egab.usuario_setor us, stf.setores s, egab.historico_distribuicao hd, egab.processo_setor ps ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {
				sql.append( "LEFT JOIN (SELECT sig_classe_proces, num_processo, cod_recurso, seq_objeto_incidente "+ 
							"	FROM judiciario.recurso_processo rp 								"+
							"	WHERE rp.dat_interposicao = 								"+
							"	(SELECT MAX (dat_interposicao) 								"+
							"	FROM judiciario.recurso_processo rpmax 							"+
							"	WHERE rpmax.seq_objeto_incidente = rp.seq_objeto_incidente)) usp 				"+
							"	ON ps.seq_objeto_incidente = usp.seq_objeto_incidente " );
			}		
			
			sql.append("   WHERE ");
			sql.append("     	us.sig_usuario = u.sig_usuario ");
			sql.append("     	AND s.cod_setor = us.cod_setor ");
			sql.append("     	AND ps.cod_setor = us.cod_setor ");
			sql.append("     	AND us.sig_usuario = hd.sig_usuario_analise ");
			sql.append("     	AND ps.seq_distribuicao_atual = hd.seq_historico_distribuicao ");
			sql.append("     	AND us.cod_setor = " + codigoSetor + " ");
			sql.append("     	AND us.tip_usuario_setor = '" + tipoUsuario.getCodigo() + "' ");
			sql.append("     	AND us.sig_usuario = '" + siglaUsuario + "' ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {			
				sql.append( " and not exists (select * from judiciario.incidente_julgamento ij, judiciario.tipo_recurso tr where " +
						"                                  ij.seq_tipo_recurso = tr.seq_tipo_recurso and tip_objeto_incidente='IJ' AND " +
						"                                  sig_tipo_recurso='MC' and ij.seq_objeto_incidente = ps.seq_objeto_incidente)	"+
						"	AND ((usp.cod_recurso IS NULL or usp.cod_recurso = 0) 									"+
						"				OR (usp.cod_recurso IS NOT NULL AND ps.seq_objeto_incidente = usp.seq_objeto_incidente))		");						
				
//				sql.append( " 	AND ps.tip_julgamento <> '" + TipoIncidenteJulgamento.SIGLA_MEDIDA_CAUTELAR + "' 	"+
//							"	AND ((usp.cod_recurso IS NULL AND ps.cod_recurso = 0) 									"+
//							"				OR (usp.cod_recurso IS NOT NULL AND ps.cod_recurso = usp.cod_recurso))		");
			}			
			
			if( contagemFimTramite != null && contagemFimTramite.booleanValue() ) 
				sql.append("    AND ps.dat_fim_tramite IS NULL ");

			if( contagemSaidaSetor != null && contagemSaidaSetor.booleanValue() ) 
				sql.append("    AND ps.dat_saida_setor IS NULL ");
						
			sql.append(" GROUP BY usp.sig_classe_proces, usp.tip_julgamento ");
			sql.append(" ORDER BY usp.sig_classe_proces, usp.tip_julgamento, carga ");
			
//			System.out.println("SQL recuperação carga por sigla e julgamento: " + sql.toString());
			
			stmt = session.connection().createStatement();
			stmt.executeQuery( sql.toString() );
			rs = stmt.getResultSet();					
			
			while( rs.next() ) {
				CargaClasseProcessualTipoJulgamento carga = new CargaClasseProcessualTipoJulgamento();
				carga.setClasseProcessual(rs.getString("sig_classe_proces"));
				carga.setTipoJulgamento(rs.getString("tip_julgamento"));
				carga.setCarga(rs.getLong("carga"));
				resultado.add(carga);
			}
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try{
				if( stmt != null )
					stmt.close();
				if( rs != null )
					rs.close();
			}catch(SQLException e ){
				throw new DaoException("Erro ao executar o comando SQL", e);
			}
			
		}		
		return resultado;
	}

	public UsuarioDistribuicao recuperarUsuarioDistribuicao(String siglaUsuario,Long idSetor,
			Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual) 
	throws DaoException {
		Session session = retrieveSession();

		UsuarioDistribuicao usuario = null;
		Connection conn = null;
		PreparedStatement pstmt =  null;
		ResultSet rs = null;


		try {

			StringBuffer sql = new StringBuffer(
					" SELECT                                            "+
					"    u.SIG_USUARIO,                                 "+
					"    u.NOM_USUARIO,                                 "+
					"    u.FLG_ATIVO,                                   "+
					"    u.NUM_MATRICULA,                               "+
					"    us.TIP_USUARIO_SETOR,                          "+
					"    s.COD_SETOR,                                   "+
					"    s.DSC_SETOR,                                   "+
					"    s.SIG_SETOR,                                   "+
					"    (SELECT COUNT(*) FROM EGAB.processo_setor ps   "+
					"      INNER JOIN EGAB.historico_distribuicao hd    "+
					"         ON hd.seq_historico_distribuicao =        "+
					"            ps.seq_distribuicao_atual              "+ 
					"      INNER JOIN EGAB.usuario_setor us             "+
					"         ON us.sig_usuario = hd.sig_usuario_analise"+
					" 	   INNER JOIN JUDICIARIO.objeto_incidente oi    "+ 
					"         on ps.seq_objeto_incidente = oi.seq_objeto_incidente ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {
				sql.append( "LEFT JOIN (SELECT sig_classe_proces, num_processo, cod_recurso, seq_objeto_incidente "+ 
							"	FROM judiciario.recurso_processo rp 								"+
							"	WHERE rp.dat_interposicao = 								"+
							"	(SELECT MAX (dat_interposicao) 								"+
							"	FROM judiciario.recurso_processo rpmax 							"+
							"	WHERE rpmax.seq_objeto_incidente = rp.seq_objeto_incidente)) usp 				"+
							"	ON ps.seq_objeto_incidente = usp.seq_objeto_incidente " );
			}
			
			sql.append( "      WHERE hd.sig_usuario_analise = u.sig_usuario "+
						"        AND ps.cod_setor = us.cod_setor            ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {			
				sql.append( " and not exists (select * from judiciario.incidente_julgamento ij, judiciario.tipo_recurso tr where " +
						"                                  ij.seq_tipo_recurso = tr.seq_tipo_recurso and tip_objeto_incidente='IJ' AND " +
						"                                  sig_tipo_recurso='MC' and ij.seq_objeto_incidente = ps.seq_objeto_incidente)	"+
						"	AND ((usp.cod_recurso IS NULL or usp.cod_recurso = 0) 									"+
						"				OR (usp.cod_recurso IS NOT NULL AND ps.seq_objeto_incidente = usp.seq_objeto_incidente))		");		
				
//				sql.append( " 	AND ps.tip_julgamento <> '" + TipoIncidenteJulgamento.SIGLA_MEDIDA_CAUTELAR + "' 	"+
//							"	AND ((usp.cod_recurso IS NULL AND ps.cod_recurso = 0) 									"+
//							"				OR (usp.cod_recurso IS NOT NULL AND ps.cod_recurso = usp.cod_recurso))		");
			}			

			if( contagemFimTramite != null && contagemFimTramite.booleanValue() ) 
				sql.append(" AND ps.dat_fim_tramite IS NULL ");

			// A pedido da PSTF (ANDRELN) a contagem da carga será zerada a partir de fevereiro. 
			if( contagemSaidaSetor != null && contagemSaidaSetor.booleanValue() ) {
				sql.append(" AND ps.dat_saida_setor IS NULL ");          
			}          

			if( idSetor != null ) {
				sql.append(           	
						"    AND ps.cod_setor = "+idSetor);
			}

			sql.append(" ) AS carga,                             "+
					"    (SELECT SUM(ps.num_indicador_gravidade * ps.num_indicador_urgencia * ps.num_indicador_tendencia) "+ 
					"       FROM EGAB.processo_setor ps                 "+
					"      INNER JOIN EGAB.historico_distribuicao hd    "+   
					"         ON hd.seq_historico_distribuicao =        "+
					"            ps.seq_distribuicao_atual              "+
					"      INNER JOIN EGAB.usuario_setor us             "+
					"         ON us.sig_usuario = hd.sig_usuario_analise "+
					" 	   INNER JOIN JUDICIARIO.objeto_incidente oi    "+ 
					"         on ps.seq_objeto_incidente = oi.seq_objeto_incidente ");
			
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {
				sql.append( "LEFT JOIN (SELECT sig_classe_proces, num_processo, cod_recurso, seq_objeto_incidente "+ 
							"	FROM judiciario.recurso_processo rp 								"+
							"	WHERE rp.dat_interposicao = 								"+
							"	(SELECT MAX (dat_interposicao) 								"+
							"	FROM judiciario.recurso_processo rpmax 							"+
							"	WHERE rpmax.seq_objeto_incidente = rp.seq_objeto_incidente)) usp 				"+
							"	ON ps.seq_objeto_incidente = usp.seq_objeto_incidente " );
			}
			
			sql.append( "      WHERE hd.sig_usuario_analise = u.sig_usuario "+
						"        AND ps.cod_setor = us.cod_setor            ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {			
				sql.append( " and not exists (select * from judiciario.incidente_julgamento ij, judiciario.tipo_recurso tr where " +
						"                                  ij.seq_tipo_recurso = tr.seq_tipo_recurso and tip_objeto_incidente='IJ' AND " +
						"                                  sig_tipo_recurso='MC' and ij.seq_objeto_incidente = ps.seq_objeto_incidente)	"+
						"	AND ((usp.cod_recurso IS NULL or usp.cod_recurso = 0) 									"+
						"				OR (usp.cod_recurso IS NOT NULL AND ps.seq_objeto_incidente = usp.seq_objeto_incidente))		");		
				
//				sql.append( " 	AND ps.tip_julgamento <> '" + TipoIncidenteJulgamento.SIGLA_MEDIDA_CAUTELAR + "' 	"+
//							"	AND ((usp.cod_recurso IS NULL AND ps.cod_recurso = 0) 									"+
//							"				OR (usp.cod_recurso IS NOT NULL AND ps.cod_recurso = usp.cod_recurso))		");
			}			

			if( contagemFimTramite != null && contagemFimTramite.booleanValue() ) 
				sql.append(" AND ps.dat_fim_tramite IS NULL ");

			// A pedido da PSTF (ANDRELN) a contagem da carga será zerada a partir de fevereiro. 
			if( contagemSaidaSetor != null && contagemSaidaSetor.booleanValue() ) {
				sql.append(" AND ps.dat_saida_setor IS NULL ");          
			}   

			if( idSetor != null ) {
				sql.append(           	
						"    AND ps.cod_setor = "+idSetor);
			}

			sql.append(" ) AS peso                        "+
					" FROM stf.usuarios u                         "+
					" INNER JOIN EGAB.usuario_setor us            "+
					" ON us.sig_usuario = u.sig_usuario           "+
					" INNER JOIN stf.setores s                    "+ 
					" ON s.cod_setor = us.cod_setor               "+ 
					" WHERE 1=1                                   ");            

			if( siglaUsuario != null ) {
				sql.append(           	
						"    AND u.sig_usuario = '"+siglaUsuario+"'");
			}

			if( idSetor != null ) {
				sql.append(           	
						"    AND us.cod_setor = "+idSetor);
			}

			sql.append(
					"	GROUP BY u.sig_usuario, u.nom_usuario, u.flg_ativo,    "+
					"    u.num_matricula, us.cod_setor, us.tip_usuario_setor,  "+
					"    s.cod_setor, s.dsc_setor, s.sig_setor           	   "	 	
			);

//			System.out.println("SQL recupera usuário distribuição: "+sql.toString());
			
			conn = session.connection();
			pstmt =  conn.prepareStatement(sql.toString());

			/*if( siglaUsuario != null ) {
				pstmt.setString(1, siglaUsuario);
			}

			if( idSetor != null ) {
				pstmt.setLong(1, idSetor);
			}*/

			rs = pstmt.executeQuery();

			if( rs.next() ) {
				usuario = new UsuarioDistribuicao();

				usuario.getUsuario().setId(rs.getString("SIG_USUARIO"));
				usuario.getUsuario().setNome(rs.getString("NOM_USUARIO"));

				if( "S".equalsIgnoreCase(rs.getString("FLG_ATIVO")))
					usuario.getUsuario().setAtivo(Boolean.TRUE);
				else if( "N".equalsIgnoreCase(rs.getString("FLG_ATIVO")))
					usuario.getUsuario().setAtivo(Boolean.FALSE);

				usuario.getUsuario().setMatricula(rs.getString("NUM_MATRICULA"));

				if( TipoUsuario.AN.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.AN);
				else if( TipoUsuario.TC.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.TC);           		
				else if( TipoUsuario.AS.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.AS);
				else if( TipoUsuario.OU.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.OU);           		
				else if( TipoUsuario.IN.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.IN);           		

				usuario.setCarga(rs.getLong("carga"));
				usuario.setCargaComPeso(rs.getLong("peso"));

				Setor setor = new Setor();
				setor.setId(rs.getLong("COD_SETOR"));
				setor.setSigla(rs.getString("SIG_SETOR"));
				setor.setNome(rs.getString("DSC_SETOR"));

				usuario.setSetor(setor);
			}
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( SQLException e ) {
			throw new DaoException("SQLException", e);
		}	    
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}finally{
			try{
				if( pstmt != null )
					pstmt.close();
				if( rs != null )
					rs.close();
			}catch(SQLException e){
				throw new DaoException("SQLException Finally", e);
			}
		}

		return usuario;		
	}

	public List<UsuarioDistribuicao> pesquisarUsuariosDistribuicaoGrupo(Long grupoId, Long idSetor,
			Boolean usuarioAtivo,
			Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual,  Boolean zeraContagem, String dataContagem) throws DaoException {

		Session session = retrieveSession();

		Connection conn = null;
		PreparedStatement pstmt =  null;
		ResultSet rs = null;

		List<UsuarioDistribuicao> usuarios = new LinkedList<UsuarioDistribuicao>();

		try {

			StringBuffer sql = new StringBuffer(
					" SELECT                                            "+
					"    u.SIG_USUARIO,                                 "+
					"    u.NOM_USUARIO,                                 "+
					"    u.FLG_ATIVO,                                   "+
					"    u.NUM_MATRICULA,                               "+
					"    us.TIP_USUARIO_SETOR,                          "+
					"    s.COD_SETOR,                                   "+
					"    s.DSC_SETOR,                                   "+
					"    s.SIG_SETOR,                                   "+
					"    (SELECT COUNT(*) FROM EGAB.processo_setor ps   "+
					"      INNER JOIN EGAB.historico_distribuicao hd    "+
					"         ON hd.seq_historico_distribuicao =        "+
					"            ps.seq_distribuicao_atual              "+ 
					"      INNER JOIN EGAB.usuario_setor us             "+
					"         ON us.sig_usuario = hd.sig_usuario_analise ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {
				sql.append( "LEFT JOIN (SELECT sig_classe_proces, num_processo, cod_recurso, seq_objeto_incidente "+ 
							"	FROM judiciario.recurso_processo rp 								"+
							"	WHERE rp.dat_interposicao = 								"+
							"	(SELECT MAX (dat_interposicao) 								"+
							"	FROM judiciario.recurso_processo rpmax 							"+
							"	WHERE rpmax.seq_objeto_incidente = rp.seq_objeto_incidente)) usp 				"+
							"	ON ps.seq_objeto_incidente = usp.seq_objeto_incidente " );
			}		
			
			sql.append( "      WHERE hd.sig_usuario_analise = u.sig_usuario "+
						"        AND ps.cod_setor = us.cod_setor            ");  
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {			
				sql.append( " and not exists (select * from judiciario.incidente_julgamento ij, judiciario.tipo_recurso tr where " +
						"                                  ij.seq_tipo_recurso = tr.seq_tipo_recurso and tip_objeto_incidente='IJ' AND " +
						"                                  sig_tipo_recurso='MC' and ij.seq_objeto_incidente = ps.seq_objeto_incidente)	"+
						"	AND ((usp.cod_recurso IS NULL or usp.cod_recurso = 0) 									"+
						"				OR (usp.cod_recurso IS NOT NULL AND ps.seq_objeto_incidente = usp.seq_objeto_incidente))		");						
				
//				sql.append( " 	AND ps.tip_julgamento <> '" + TipoIncidenteJulgamento.SIGLA_MEDIDA_CAUTELAR + "' 	"+
//							"	AND ((usp.cod_recurso IS NULL AND ps.cod_recurso = 0) 									"+
//							"				OR (usp.cod_recurso IS NOT NULL AND ps.cod_recurso = usp.cod_recurso))		");
			}			

			if( contagemFimTramite != null && contagemFimTramite.booleanValue() ) 
				sql.append(" AND ps.dat_fim_tramite IS NULL ");

			if( contagemSaidaSetor != null && contagemSaidaSetor.booleanValue() ) 
				sql.append(" AND ps.dat_saida_setor IS NULL ");   
			
			if( idSetor != null ) {
				sql.append(           	
						"    AND ps.cod_setor = "+idSetor);
			}			

			sql.append(" ) AS carga,                             "+
					"    (SELECT SUM(ps.num_indicador_gravidade * ps.num_indicador_urgencia * ps.num_indicador_tendencia) "+ 
					"       FROM EGAB.processo_setor ps                 "+
					"      INNER JOIN EGAB.historico_distribuicao hd    "+   
					"         ON hd.seq_historico_distribuicao =        "+
					"            ps.seq_distribuicao_atual              "+
					"      INNER JOIN EGAB.usuario_setor us             "+
					"         ON us.sig_usuario = hd.sig_usuario_analise ");
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {
				sql.append( "LEFT JOIN (SELECT sig_classe_proces, num_processo, cod_recurso, seq_objeto_incidente "+ 
							"	FROM judiciario.recurso_processo rp 								"+
							"	WHERE rp.dat_interposicao = 								"+
							"	(SELECT MAX (dat_interposicao) 								"+
							"	FROM judiciario.recurso_processo rpmax 							"+
							"	WHERE rpmax.seq_objeto_incidente = rp.seq_objeto_incidente)) usp 				"+
							"	ON ps.seq_objeto_incidente = usp.seq_objeto_incidente " );
			}			
			
			sql.append( "      WHERE hd.sig_usuario_analise = u.sig_usuario "+
						"        AND ps.cod_setor = us.cod_setor            ");   
			
			if( cargaDistribuicaoFaseAtual != null && cargaDistribuicaoFaseAtual.booleanValue() ) {			
				sql.append( " and not exists (select * from judiciario.incidente_julgamento ij, judiciario.tipo_recurso tr where " +
						"                                  ij.seq_tipo_recurso = tr.seq_tipo_recurso and tip_objeto_incidente='IJ' AND " +
						"                                  sig_tipo_recurso='MC' and ij.seq_objeto_incidente = ps.seq_objeto_incidente)	"+
						"	AND ((usp.cod_recurso IS NULL or usp.cod_recurso = 0) 									"+
						"				OR (usp.cod_recurso IS NOT NULL AND ps.seq_objeto_incidente = usp.seq_objeto_incidente))		");		
				
//				sql.append( " 	AND ps.tip_julgamento <> '" + TipoIncidenteJulgamento.SIGLA_MEDIDA_CAUTELAR + "' 	"+
//							"	AND ((usp.cod_recurso IS NULL AND ps.cod_recurso = 0) 									"+
//							"				OR (usp.cod_recurso IS NOT NULL AND ps.cod_recurso = usp.cod_recurso))		");
			}			

			if( contagemFimTramite != null && contagemFimTramite.booleanValue() ) 
				sql.append(" AND ps.dat_fim_tramite IS NULL ");

			if( contagemSaidaSetor != null && contagemSaidaSetor.booleanValue() ) 
				sql.append(" AND ps.dat_saida_setor IS NULL ");  
			
			

			if( idSetor != null ) {
				sql.append(           	
						"    AND ps.cod_setor = "+idSetor);
			}
			
			sql.append(" ) AS peso                        "+
					" FROM stf.usuarios u                         "+
					" INNER JOIN EGAB.usuario_setor us            "+
					" ON us.sig_usuario = u.sig_usuario           "+
					" INNER JOIN stf.setores s                    "+ 
					" ON s.cod_setor = us.cod_setor               "+ 
					" INNER JOIN EGAB.usuario_grupo ug            "+
					" ON ug.sig_usuario = u.sig_usuario           "+                    
			" WHERE 1=1                                   ");            

			if( grupoId != null ) {
				sql.append(
				"    AND ug.seq_grupo_usuario=?               ");
			}

			if( usuarioAtivo != null ) {
				sql.append(           	
				"    AND u.FLG_ATIVO=?        	              ");
			}

			if( idSetor != null ){
				sql.append(" AND us.cod_setor= ? ");
			}

			sql.append(
					"	GROUP BY u.sig_usuario, u.nom_usuario, u.flg_ativo,    "+
					"    u.num_matricula, us.cod_setor, us.tip_usuario_setor,  "+
					"    s.cod_setor, s.dsc_setor, s.sig_setor           	   "+
					"    order by u.nom_usuario   "
			);

//			System.out.println("SQL pesquisa usuarios distribuição grupo:"+sql.toString());
			
			conn = session.connection();
			pstmt =  conn.prepareStatement(sql.toString());

			int index = 1;
			if( grupoId != null ) {
				pstmt.setLong(index++, grupoId);
			}

			if( usuarioAtivo != null ) {
				if( usuarioAtivo == Boolean.TRUE )
					pstmt.setString(index++, "S");
				else 
					pstmt.setString(index++, "N");
			}

			if( idSetor != null ) {
				pstmt.setLong(index++, idSetor);
			}

			rs = pstmt.executeQuery();

			while( rs.next() ) {
				UsuarioDistribuicao usuario = new UsuarioDistribuicao();

				usuario.getUsuario().setId(rs.getString("SIG_USUARIO"));
				usuario.getUsuario().setNome(rs.getString("NOM_USUARIO"));

				if( "S".equalsIgnoreCase(rs.getString("FLG_ATIVO")))
					usuario.getUsuario().setAtivo(Boolean.TRUE);
				else if( "N".equalsIgnoreCase(rs.getString("FLG_ATIVO")))
					usuario.getUsuario().setAtivo(Boolean.FALSE);

				usuario.getUsuario().setMatricula(rs.getString("NUM_MATRICULA"));

				if( TipoUsuario.AN.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.AN);
				else if( TipoUsuario.TC.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.TC);           		
				else if( TipoUsuario.AS.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.AS);
				else if( TipoUsuario.OU.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.OU);           		
				else if( TipoUsuario.IN.getCodigo().equalsIgnoreCase(rs.getString("TIP_USUARIO_SETOR")))
					usuario.setTipoUsuario(TipoUsuario.IN);           		

				usuario.setCarga(rs.getLong("carga"));
				usuario.setCargaComPeso(rs.getLong("peso"));

				Setor setor = new Setor();
				setor.setId(rs.getLong("COD_SETOR"));
				setor.setSigla(rs.getString("SIG_SETOR"));
				setor.setNome(rs.getString("DSC_SETOR"));

				usuario.setSetor(setor);

				usuarios.add(usuario);
			}
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( SQLException e ) {
			throw new DaoException("SQLException", e);
		}	    
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}finally{
			try{
				if( pstmt != null )
					pstmt.close();
				if( rs != null )
					rs.close();
			}catch(SQLException e){
				throw new DaoException("SQLException Finally", e);
			}
		}

		return usuarios;		
	}		


	public Boolean persistirGrupoUsuario(GrupoUsuario grupoUsuario) 
	throws DaoException{

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(grupoUsuario);
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;		
	}

	public Boolean persistirUsuarioEGab( UsuarioEGab usuarioEGab ) 
	throws DaoException{

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist( usuarioEGab );
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;		
	}

	@Override
	public Boolean adicionarUsuarioGrupo(UsuarioEGab usuario, GrupoUsuario grupoUsuario) throws DaoException {
		
		Session session = retrieveSession();

		Boolean usuarioInserido = null;

		try {
			int resultado;
			
			Connection con = session.connection();

			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO EGAB.USUARIO_GRUPO (SEQ_GRUPO_USUARIO, SIG_USUARIO, COD_SETOR) ");
			sql.append(" VALUES (?, ?, ?)");

			PreparedStatement ps = con.prepareStatement(sql.toString());

			ps.setLong(1, grupoUsuario.getId());
			ps.setString(2,usuario.getUsuario().getId());
			ps.setLong(3, grupoUsuario.getSetor().getId());

			resultado = ps.executeUpdate();
			ps.close();
			
			usuarioInserido = resultado > 0;
			
		} catch (SQLException exception) {
			throw new DaoException("SQLException", exception);
		} catch (HibernateException exception) {
			throw new DaoException("HibernateException", convertHibernateAccessException(exception));
		} catch (RuntimeException exception) {
			throw new DaoException("RuntimeException", exception);
		}

		return usuarioInserido;   	
		
	}
	
	@Override
	public Boolean removeUsuarioGrupo(UsuarioEGab usuario, GrupoUsuario grupoUsuario) throws DaoException {
		
		Session session = retrieveSession();

		Boolean usuarioRemovido = null;
		
		try {
			int resultado;
			
			Connection con = session.connection();

			StringBuffer sql = new StringBuffer();
			sql.append(" DELETE FROM EGAB.USUARIO_GRUPO WHERE SIG_USUARIO = ? and SEQ_GRUPO_USUARIO = ? ");
			
			PreparedStatement ps = con.prepareStatement(sql.toString());

			ps.setString(1,usuario.getUsuario().getId());
			ps.setLong(2, grupoUsuario.getId());

			resultado = ps.executeUpdate();
			ps.close();
			
			usuarioRemovido = resultado > 0;
			
		} catch (SQLException exception) {
			throw new DaoException("SQLException", exception);
		} catch (HibernateException exception) {
			throw new DaoException("HibernateException", convertHibernateAccessException(exception));
		} catch (RuntimeException exception) {
			throw new DaoException("RuntimeException", exception);
		}

		return usuarioRemovido; 
	}
	
	

	public Boolean excluirGrupoUsuario(GrupoUsuario grupoUsuario)throws  DaoException{

		Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);

		try {
			session.delete(grupoUsuario);
			session.flush();

			return Boolean.TRUE;
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));

		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	
	}

	public Boolean excluirUsuarioEGab(UsuarioEGab usuarioEGab)
	throws DaoException{

		Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);

		try{
			session.delete( usuarioEGab );
			session.flush();

			return Boolean.TRUE;

		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}  


	}
	public List<Usuario> pesquisarUsuariosSTF( Long codigoSetor , TipoUsuario tipoUsuario ,  Boolean ativo , Boolean cadastradosEGab )
	throws DaoException{

		Session session = retrieveSession();

		List<Usuario> usuarios = null;

		try {

			StringBuffer hql = new StringBuffer(
					" SELECT u " +
					" FROM Usuario u " +
			" WHERE 1 = 1 ");

			if( codigoSetor != null)
				hql.append(" AND u.setor.id = "+codigoSetor);

			if( tipoUsuario != null)
				hql.append(" AND u.tipoUsuario = '"+tipoUsuario.name()+"'");

			if( ativo != null ) {

				String strAtivo = null;

				if( ativo.booleanValue() )
					strAtivo = "S";
				else
					strAtivo = "N";

				hql.append(" AND u.ativo = '"+strAtivo+"'");
			}
			if( cadastradosEGab != null ){
				if( cadastradosEGab.booleanValue()){
					hql.append(" AND u.id not in( select us.usuario.id from UsuarioEGab us " +
					" where us.setor.id = u.setor.id ) ");
				}
			}

			hql.append(" ORDER BY u.nome ");

			Query query = session.createQuery(hql.toString());

			usuarios = query.list(); 

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return usuarios;		
	}

	public List<Setor> pesquisarSetoresEGab(String usuario) throws DaoException {

		List<Setor> listaSetoresEGab = null;

		Session sessao = retrieveSession();
		try{
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT DISTINCT s FROM Setor s, UsuarioEGab u INNER JOIN s.tiposConfiguracao tc ");

			hql.append(" WHERE 1=1 ");

			hql.append(" AND tc.sigla IN ('EGAB', 'EGAB-E') ");

			hql.append(" AND s.id = u.setor.id ");

			hql.append(" AND u.usuario.id = '" + usuario.toUpperCase() + "' "); 

			hql.append(" AND u.usuario.id IS NOT NULL ");

			hql.append(" ORDER BY s.nome ");

			Query query = sessao.createQuery( hql.toString() );

			listaSetoresEGab = query.list();         
		}
		catch( HibernateException ex ){
			throw new DaoException( "HibernateException" , ex );
		}
		catch(RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}       

		return listaSetoresEGab;        
	}

	public Boolean excluirUsuarioSecao(SecaoSetor secaoSetor) 
	throws DaoException{
		Session session = retrieveSession();
		StringBuffer sql = new StringBuffer();
		Boolean alterado = Boolean.FALSE;


		Statement stmt =  null;
		ResultSet rs = null;
		try{

			sql.append( " delete " );
			sql.append( " egab.usuario_secao " );
			sql.append( " where seq_secao_setor = " + secaoSetor.getId() );

			stmt = session.connection().createStatement();
			rs = stmt.executeQuery( sql.toString() );

			if( rs.next() ){
				alterado = Boolean.TRUE; 
			}

			return alterado;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		catch(Exception e){
			throw new DaoException("Exception", e);
		}finally{
			try{
				if( stmt != null )
					stmt.close();
				if( rs != null )
					rs.close();

			}catch(SQLException e){
				throw new DaoException("SQLException Finally", e);
			}
		}	 
	}

	public Boolean recuperarUsuarioSecao(SecaoSetor secaoSetor) 
	throws DaoException{
		Session session = retrieveSession();
		StringBuffer sql = new StringBuffer();
		Boolean existe = Boolean.FALSE;

		Statement stmt = null;
		ResultSet rs = null;
		try{

			sql.append( " select us.seq_secao_setor " );
			sql.append( " from egab.usuario_secao us " );
			sql.append( " where us.seq_secao_setor = " + secaoSetor.getId() );

			stmt = session.connection().createStatement();
			rs = stmt.executeQuery( sql.toString() );

			if( rs.next() ){
				existe = Boolean.TRUE; 
			}

			return existe;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		catch(Exception e){
			throw new DaoException("Exception", e);
		}finally{
			try{
				if( rs != null )
					rs.close();
				if( stmt != null )
					stmt.close();
			}catch(SQLException e){
				throw new DaoException("SQLException Finally", e);
			}
		}	
	}

	public Boolean persistirConfiguracaoUsuario(
			ConfiguracaoUsuario configuracaoUsuario)
			throws DaoException {
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist( configuracaoUsuario );
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;		
	}

	@SuppressWarnings("unchecked")
	public List<ConfiguracaoUsuario> pesquisarConfiguracaoUsuario(Long id,
			String sigUsuario, Long codigoTipoConfiguracao)
			throws DaoException {
		Session session = retrieveSession();
		List<ConfiguracaoUsuario> lista = null;
		try{

			Criteria criteria = session.createCriteria(ConfiguracaoUsuario.class);
			
			if( id != null )
				criteria.add(Restrictions.eq("id", id));
			
			if( sigUsuario != null && sigUsuario.trim().length() > 0 )
				criteria.add(Restrictions.eq("usuario.id", sigUsuario));
			
			if( codigoTipoConfiguracao != null )
				criteria.add(Restrictions.eq("tipoConfiguracaoUsuario.id", codigoTipoConfiguracao ));
			
			lista = criteria.list();
			
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return lista;
	}

	public TipoConfiguracaoUsuario recuperarTipoConfiguracaoUsuario(Long id)
			throws DaoException {
		Session session = retrieveSession();
		TipoConfiguracaoUsuario tipo = null;
		try{

			Criteria criteria = session.createCriteria(TipoConfiguracaoUsuario.class);
			
			if( id != null )
				criteria.add(Restrictions.eq("id", id));
			
			tipo = (TipoConfiguracaoUsuario) criteria.uniqueResult();
			
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return tipo;
	}

	@Override
	public Set<GrupoUsuario> pesquisarGruposUsuario(Usuario usuario) throws DaoException {
		Session session = retrieveSession();

		try {

			try {
				CallableStatement stmt = null;
				Connection connection = session.connection();
				// Registrando usuário via procedure de seguranca...
				stmt = connection.prepareCall("{call GLOBAL.PKG_SEGURANCA.PRC_SEGURANCA(?,?,?)}");
				stmt.setString(1, "ESTFDECISAO");
				stmt.setString(2, usuario.getId());
				stmt.setString(3, null);
				stmt.execute();
	        } catch (Exception e) {
	        	e.printStackTrace();
			}
			
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT gu FROM GrupoUsuario gu ");
			hql.append(" INNER JOIN gu.usuarios u ");
			hql.append(" WHERE ");
			hql.append(" gu.ativo = 'S' ");
			hql.append(" AND gu.setor.id = " + usuario.getSetor().getId());
			hql.append(" AND u.usuario.id = '" + usuario.getId() + "'");

			Query query = session.createQuery(hql.toString());
			
			return new HashSet<GrupoUsuario>(query.list());

		} catch(HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} 
	}
	
	public List<PessoaTelefone> pesquisarTelefones(Long seqPessoa) throws DaoException{
		List<PessoaTelefone> resultado = null;
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria( PessoaTelefone.class, "p" );
			c.add(Restrictions.eq("p.pessoa.id", seqPessoa));
			resultado = (List<PessoaTelefone>) c.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}		
		
		return resultado;
	}
	
	public List<PessoaEmail> pesquisarEmails(Long seqPessoa) throws DaoException{
		List<PessoaEmail> resultado = null;
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria( PessoaEmail.class, "p" );
			c.add(Restrictions.eq("p.pessoa.id", seqPessoa));
			resultado = (List<PessoaEmail>) c.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}		
		
		return resultado;
	}
}
