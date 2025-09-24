package br.jus.stf.estf.decisao.texto.persistence.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.pretty.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TipoPermissaoTexto;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.mapper.TextoMapper;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.texto.persistence.TextoDao;

/**
 * Implementação JDBC para a DAO local de textos.
 * 
 * @author Rodrigo Barreiros
 * @since 15.04.2010
 */
@Repository
public class TextoDaoImpl implements TextoDao {

	private final Log logger = LogFactory.getLog(TextoDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	private boolean showSql = false;

	/**
	 * @see br.jus.stf.estf.decisao.texto.persistence.TextoDao#recuperarTextos(br.gov.stf.estf.entidade.processostf.ObjetoIncidente, br.gov.stf.estf.entidade.ministro.Ministro, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<TextoDto> recuperarTextos(ObjetoIncidente<?> objetoIncidente, Ministro ministro,
			boolean textosDoMinistro, Principal principal, boolean incluirTextosDisponibilizados) {
		// Montando lista de projeções: resultado do select...
		StringBuffer query = new StringBuffer("select ");
		query.append("texto.SEQ_TEXTOS id, processo.SIG_CLASSE_PROCES sigla, processo.NUM_PROCESSO numero, oi.SEQ_OBJETO_INCIDENTE idObjetoIncidente, texto.FLG_LIBERACAO_ANTECIPADA liberacaoAntecipada, texto.COD_TIPO_VOTO tipoVoto, ");
		query.append("texto.COD_TIPO_TEXTO codTipoTexto, texto.OBS_TEXTO observacao, texto.COD_TIPO_FASE_TEXTO codFase, ");
		query.append("texto.FLG_TEXTOS_IGUAIS textosIguais, texto.DAT_INCLUSAO dataInclusao, texto.DAT_ALTERACAO dataAlteracao, texto.FLG_FAVORITO_GABINETE favoritoNoGabinete, ");
		query.append("texto.SEQ_ARQUIVO_ELETRONICO seqArquivoEletronico, ministro.SIG_MINISTRO ministro, ministro.NOM_MINISTRO nomeMinistro, ministro.COD_SETOR idSetorMinistro, usuario.NOM_USUARIO responsavel, ");
		query.append("COALESCE (JUDICIARIO.PKG_CONSULTA.FNC_SIGLA_CADEIA(oi.SEQ_OBJETO_INCIDENTE, oi.TIP_OBJETO_INCIDENTE), ' ') cadeia, ");
		query.append("(select max(DAT_FASE) from DOC.FASE_TEXTO_PROCESSO where SEQ_TEXTOS = texto.SEQ_TEXTOS) as dataFase, ");
		query.append("tipoTexto.DSC_TIPO as tipo, texto.COD_MINISTRO as idMinistro, texto.FLG_PUBLICO as publico, ");
		query.append("texto.SEQ_VOTOS as sequenciaVotos, texto.DAT_SESSAO as dataSessao, controle_votos.SESSAO as tipoSessao, controle_votos.SEQ_TIPO_SITUACAO_TEXTO as situacaoTexto, ");
		query.append("texto.TIP_RESTRICAO tipoRestricao , texto.USU_INCLUSAO idUsuarioInclusao, processo.TIP_MEIO_PROCESSO tipoMeioProcesso, texto.FLG_ORIGEM_DIGITAL origemDigital, texto.FLG_JULGAMENTO_DIGITAL julgamentoDigital, ");

		// Comentado para otimizar a busca de textos. A observacao agora é buscada pelo método adicionarObservacaoFase()
		//query.append("(select TXT_OBSERVACAO from DOC.FASE_TEXTO_PROCESSO ftp where ftp.SEQ_FASE_TEXTO_PROCESSO = (select max(SEQ_FASE_TEXTO_PROCESSO) from DOC.FASE_TEXTO_PROCESSO ftp2 where ftp2.SEQ_TEXTOS = texto.SEQ_TEXTOS and ftp2.TXT_OBSERVACAO IS NOT NULL and length(ftp2.TXT_OBSERVACAO) > 0)) observacaoFase, ");
		query.append("null observacaoFase, ");
		
		// Comentado para otimizar a busca de textos. A observacao agora é buscada pelo método adicionarPermissaoGrupo()
		// query.append(" CASE WHEN texto.seq_grupo_responsavel IS NOT NULL THEN (SELECT 'GRUPO' FROM egab.usuario_grupo  WHERE sig_usuario = " + "'"+ principal.getUsuario().getId().toUpperCase() + "'" + "  AND ROWNUM = 1) ELSE 'USUARIO' END permgrupo, ");
		query.append("null permgrupo, ");
		
		// Comentado para otimizar a busca de textos. A observacao agora é buscada pelo método adicionarNomeGrupo()
		// query.append(" (SELECT gu.dsc_grupo FROM egab.grupo_usuario gu WHERE gu.seq_grupo_usuario =  texto.seq_grupo_responsavel) nomeGrupo, ");
		query.append("null nomeGrupo, ");

		query.append(" CASE WHEN texto.USU_AUTOR_INTELECTUAL IS NOT NULL THEN texto.USU_AUTOR_INTELECTUAL ELSE TO_CHAR(texto.seq_grupo_responsavel) END idResponsavel ");

		// Adicionando lista de tabelas envolvidas...
		query.append("from STF.TEXTOS texto, STF.USUARIOS usuario, JUDICIARIO.VW_PROCESSO_RELATOR processo, ");
		// query.append("JUDICIARIO.OBJETO_INCIDENTE oi, STF.MINISTROS ministro, STF.TIPO_TEXTOS tipoTexto, STF.CONTROLE_VOTOS, egab.grupo_usuario gu ");
		query.append("JUDICIARIO.OBJETO_INCIDENTE oi, STF.MINISTROS ministro, STF.TIPO_TEXTOS tipoTexto, STF.CONTROLE_VOTOS ");

		// Adicionando lista de joins...
		query.append("where texto.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE and ");
		query.append("texto.COD_MINISTRO = ministro.COD_MINISTRO and ");
		query.append("texto.USU_AUTOR_INTELECTUAL = usuario.SIG_USUARIO (+) and ");
		query.append("texto.COD_TIPO_TEXTO = tipoTexto.COD_TIPO_TEXTO and ");
		query.append("processo.SEQ_OBJETO_INCIDENTE = oi.SEQ_OBJETO_INCIDENTE_PRINCIPAL and ");
		query.append("texto.SEQ_VOTOS = controle_votos.SEQ_VOTO (+) and ");
		query.append("texto.COD_MINISTRO = controle_votos.COD_MINISTRO (+) and ");
		query.append("texto.SEQ_OBJETO_INCIDENTE = controle_votos.SEQ_OBJETO_INCIDENTE (+) and ");
		query.append("texto.COD_TIPO_TEXTO = controle_votos.COD_TIPO_TEXTO (+) and ");
		query.append("texto.DAT_SESSAO = controle_votos.DAT_SESSAO (+) and ");
		//query.append("texto.seq_grupo_responsavel = gu.seq_grupo_usuario (+) and ");

		// Adicionando lista de restrições...
//		query.append("texto.FLG_ORIGEM_DIGITAL <> 'S' AND ");
		query.append("oi.SEQ_OBJETO_INCIDENTE = ? AND (( ");
		if (!textosDoMinistro) {
			if (ministro != null) {
				query.append("((texto.COD_MINISTRO = ?) OR (texto.COD_MINISTRO <> ? and texto.FLG_PUBLICO = 'S')) and ");
			} else {
				query.append("texto.FLG_PUBLICO = 'S' and ");
			}
		} else {
			if (ministro != null) {
				query.append("texto.COD_MINISTRO = ?  and ");
			} else {
				query.append("texto.COD_MINISTRO is null and ");
			}
		}
		query.append("tipoTexto.COD_TIPO_TEXTO <> ?) ");
		
		if (incluirTextosDisponibilizados)
			query.append(" OR texto.FLG_LIBERACAO_ANTECIPADA = 'S'");
		
		query.append(") ");

		// Adicionando lista de restrições...
		query.append("order by sequenciaVotos, tipo, observacao ");

		// Lista de parâmetros...
		Object[] params = montaParametrosDaPesquisa(principal, objetoIncidente, ministro, textosDoMinistro);
		
		if (showSql) {
			logger.info(new Formatter(query.toString()).format());
		}

		// Disparando consulta, recuperando e retornando resultados...
		List<TextoDto> lista = jdbcTemplate.query(query.toString(), params, new TextoMapper());
		
		// Quebra a lista de 1000 em 1000 para evitar problemas na cláusula IN do Oracle
		if (lista != null && !lista.isEmpty()) {
			int tam = lista.size();
			int divisoes = (int) Math.ceil(tam / 1000.0);
			
			for (int i=0; i<divisoes; i++) {
				int inicio = 1000*i;
				int fim = inicio+1000;
				fim = (fim>tam-1)?tam-1:fim;
				
				adicionarObservacaoFase(lista.subList(inicio, fim));
				adicionarPermissaoGrupo(lista.subList(inicio, fim), principal);
				adicionarNomeGrupo(lista.subList(inicio, fim));
			}
		}
		
		return lista;
	}
	
	private void adicionarObservacaoFase(final List<TextoDto> lista) {
		List<Long> listaIds = new ArrayList<Long>();
		
		for (TextoDto textoDto : lista)
			listaIds.add(textoDto.getId());
		
		if (listaIds != null && !listaIds.isEmpty()) {
			String ids = StringUtils.join(listaIds,",");
				jdbcTemplate.query("SELECT ftp.SEQ_TEXTOS,ftp.TXT_OBSERVACAO FROM (SELECT ROW_NUMBER() OVER ( PARTITION BY seq_textos ORDER BY dat_fase desc ) AS RN, MOD (ROWNUM, 2) AS moda, ftp1.* FROM (select * from doc.fase_texto_processo where seq_textos in ("+ids+")) ftp1 ) ftp WHERE RN = 1", new RowMapper() {
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long seqTexto = rs.getLong("SEQ_TEXTOS");
					String txtObservacao = rs.getString("TXT_OBSERVACAO");
					
					for (TextoDto textoDto : lista)
						if (textoDto.getId().equals(seqTexto))
							textoDto.setObservacaoFase(txtObservacao);
					
					return null;
				}
			});
		}
	}
	
	private void adicionarPermissaoGrupo(final List<TextoDto> lista, Principal principal) {
		List<Long> listaIds = new ArrayList<Long>();
		
		for (TextoDto textoDto : lista)
			listaIds.add(textoDto.getId());
		
		if (listaIds != null && !listaIds.isEmpty()) {
			String ids = StringUtils.join(listaIds,",");
			String sql = "SELECT texto.seq_textos, CASE WHEN texto.seq_grupo_responsavel IS NOT NULL THEN (SELECT 'GRUPO' FROM egab.usuario_grupo WHERE sig_usuario = " + "'"+ principal.getUsuario().getId().toUpperCase() + "'" + " AND ROWNUM = 1)  ELSE 'USUARIO' END permgrupo FROM STF.TEXTOS texto WHERE texto.seq_textos IN ("+ids+") ";
			jdbcTemplate.query(sql, new RowMapper() {
				
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long seqTexto = rs.getLong("SEQ_TEXTOS");
					
					for (TextoDto textoDto : lista)
						if (textoDto.getId().equals(seqTexto) && rs.getString("permgrupo") != null)
							textoDto.setTipoPermissaoTexto(TipoPermissaoTexto.valueOf(rs.getString("permgrupo")));
					
					return null;
				}
			});
		}
	}

	private void adicionarNomeGrupo(final List<TextoDto> lista) {
		List<Long> listaIds = new ArrayList<Long>();
		
		for (TextoDto textoDto : lista)
			listaIds.add(textoDto.getId());
		
		if (listaIds != null && !listaIds.isEmpty()) {
			String ids = StringUtils.join(listaIds,",");
			String sql = "SELECT texto.seq_textos, (SELECT gu.dsc_grupo FROM egab.grupo_usuario gu WHERE gu.seq_grupo_usuario =  texto.seq_grupo_responsavel) nomeGrupo FROM STF.TEXTOS texto WHERE texto.seq_textos IN ("+ids+")";
			jdbcTemplate.query(sql, new RowMapper() {
				
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					Long seqTexto = rs.getLong("SEQ_TEXTOS");
					
					for (TextoDto textoDto : lista)
						if (textoDto.getId().equals(seqTexto)) {
							textoDto.setNomeGrupo(rs.getString("nomeGrupo"));
							if (textoDto.getResponsavel() == null || textoDto.getResponsavel().isEmpty())
								textoDto.setResponsavel(rs.getString("nomeGrupo"));
						}
					return null;
				}
			});
		}
	}
	
	protected Object[] montaParametrosDaPesquisa(Principal principal, ObjetoIncidente<?> objetoIncidente, Ministro ministro,
			boolean textosDoMinistro) {
		if (textosDoMinistro) {
			if (ministro != null) {
				return new Object[] {objetoIncidente.getId(), ministro.getId(), TipoTexto.DECISAO.getCodigo() };
			} else {
				return new Object[] {objetoIncidente.getId(), TipoTexto.DECISAO.getCodigo() };
			}
		}
		
		if (ministro != null) {
			return new Object[] {objetoIncidente.getId(), ministro.getId(), ministro.getId(), TipoTexto.DECISAO.getCodigo() };
		} else {
			return new Object[] {objetoIncidente.getId(), TipoTexto.DECISAO.getCodigo() };
		}
	}

	@Override
	public void marcarComoFavoritos(final List<Long> idsTextos) throws DaoException {
		try {
			String queryString = "UPDATE STF.TEXTOS t SET t.FLG_FAVORITO_GABINETE = 'S' WHERE t.SEQ_TEXTOS IN (:ids)";
			SQLQuery query = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
			query.setParameterList("ids", idsTextos);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
	}

	@Override
	public void desmarcarComoFavoritos(List<Long> idsTextos) throws DaoException {
		try {
			String queryString = "UPDATE STF.TEXTOS t SET t.FLG_FAVORITO_GABINETE = 'N' WHERE t.SEQ_TEXTOS IN (:ids)";
			SQLQuery query = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(queryString);
			query.setParameterList("ids", idsTextos);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
	}

	@Override
	public List<Texto> recuperarListaTextos(Collection<TextoDto> dtos) throws DaoException {
		try {		
			List<Long> idsTextos = new ArrayList<Long>();
			for (TextoDto textoDto : dtos){
				idsTextos.add(textoDto.getId());
			}
			Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("FROM Texto t WHERE t.id in (:ids)");
			query.setParameterList("ids", idsTextos);
			return query.list();		
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}		
	}

}
