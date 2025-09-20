package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ibm.icu.util.Calendar;

import br.gov.stf.estf.documento.model.dataaccess.ComunicacaoDao;
import br.gov.stf.estf.documento.model.util.ComunicacaoSearch;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.InCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.SearchCriterion;

@Repository
public class ComunicacaoDaoHibernate extends GenericHibernateDao<Comunicacao, Long> implements ComunicacaoDao {

	private static final long serialVersionUID = 783467297668831177L;

	public ComunicacaoDaoHibernate() {
		super(Comunicacao.class);
	}

	@Override
	public int pesquisarPainelControle(Long idFaseComunicacao, Long idSetor, String usuarioCriacao) throws DaoException {
		Integer quantidadeComunicacoes = null;

		try {

			Setor setor = new Setor();
			setor.setId(idSetor);

			List<Long> situacaoDoPdf = new ArrayList<Long>();
			if ((idFaseComunicacao == TipoFaseComunicacao.AGUARDANDO_ASSINATURA.getCodigoFase())
					|| idFaseComunicacao == TipoFaseComunicacao.PDF_GERADO.getCodigoFase() 
					|| idFaseComunicacao == TipoFaseComunicacao.EM_REVISAO.getCodigoFase() 
					|| idFaseComunicacao == TipoFaseComunicacao.REVISADO.getCodigoFase()) {
				// procurar os documentos que está com a situação de PDF gerado e prontos para assinar
				situacaoDoPdf.add(TipoSituacaoDocumento.GERADO.getCodigo());
			} else if (idFaseComunicacao == TipoFaseComunicacao.ASSINADO.getCodigoFase()) {
				// buscar os documentos com PDF assinado e fase ASSINADO
				situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo());
				situacaoDoPdf.add(TipoSituacaoDocumento.JUNTADO.getCodigo());
			} else if (idFaseComunicacao == TipoFaseComunicacao.AGUARDANDO_ASSINATURA_MINISTRO.getCodigoFase()) {
				// buscar os documentos onde o PDF pode estar assinado ou não.
				situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo());
				situacaoDoPdf.add(TipoSituacaoDocumento.GERADO.getCodigo());
			} else if (idFaseComunicacao == TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO.getCodigoFase()) {
				// buscar os documentos com PDF assinado e fase Aguardando Encaminhamento para o
				// eSTF-Decisão
				situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo());
			}

			FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
			filtro.setSetor(setor);
			filtro.setFaseDocumento(idFaseComunicacao);
			filtro.setListaTipoSituacaoDocumento(situacaoDoPdf);
			quantidadeComunicacoes = pesquisarDocumentosAssinaturaCount(filtro);
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return quantidadeComunicacoes;
	}

	/**
	 * Pesquisa os documentos existentes
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Comunicacao> pesquisarDocumentos(Long idObjetoIncidente, Long codigoModelo, Setor setor, String usuario,
			Boolean buscaSomenteDocumentoFaseGerados) throws DaoException {

		List listaDocumentos = null;
		StringBuffer sql = new StringBuffer();
		Session session = retrieveSession();

		try {
			
			sql.append("   SELECT DISTINCT(c.seq_comunicacao), dc.seq_documento_comunicacao ");
			sql.append("    FROM judiciario.comunicacao c ");
			sql.append("   JOIN judiciario.fase_comunicacao f ON f.seq_comunicacao = c.seq_comunicacao ");
			sql.append("   JOIN judiciario.comunicacao_objeto_incidente coi ON coi.seq_comunicacao = c.seq_comunicacao ");
			sql.append("   JOIN judiciario.objeto_incidente oi ON (oi.seq_objeto_incidente = coi.seq_objeto_incidente AND oi.tip_objeto_incidente IN ('PR','IJ','RC')) ");
			if (codigoModelo != null && codigoModelo != 0L) {
				sql.append(" JOIN judiciario.modelo_comunicacao mc ON (c.SEQ_MODELO_COMUNICACAO = mc.SEQ_MODELO_COMUNICACAO AND mc.SEQ_MODELO_COMUNICACAO = :idModelo) ");
			}
			sql.append(" LEFT JOIN judiciario.documento_comunicacao dc ON dc.seq_comunicacao = c.seq_comunicacao AND nvl(dc.seq_tipo_situacao_documento, 0) != :situacao  ");
			sql.append("  LEFT JOIN judiciario.deslocamento_comunicacao ds ON ds.seq_comunicacao = c.seq_comunicacao ");
			sql.append("    WHERE coi.tip_vinculo = 'P'  ");
			sql.append("    AND (c.cod_setor = :codSetor OR (ds.cod_setor = :codSetor AND ds.dat_saida IS NULL)) ");	
			sql.append("    AND f.flg_fase_atual = 'S' ");
			
			if (buscaSomenteDocumentoFaseGerados) {
				sql.append(" AND f.seq_tipo_fase_comunicacao = 2 ");
			} else {
				sql.append(" AND f.seq_tipo_fase_comunicacao IN (1, 2, 3) ");
			}			
			
			if (usuario != null) {
				sql.append(" AND c.SIG_USUARIO_CRIACAO = :usuario ");
			}
			
			if (idObjetoIncidente != null && idObjetoIncidente != 0L) {
				sql.append(" AND oi.SEQ_OBJETO_INCIDENTE = :idObjetoIncidente ");
			}
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			sqlQuery.setLong("situacao", 9L);

			if (idObjetoIncidente != null && idObjetoIncidente != 0L) {
				sqlQuery.setLong("idObjetoIncidente", idObjetoIncidente.longValue());
			}

			if (setor != null) {
				sqlQuery.setLong("codSetor", setor.getId());
			}

			if (usuario != null) {
				sqlQuery.setString("usuario", usuario);
			}

			if (codigoModelo != null) {
				sqlQuery.setLong("idModelo", codigoModelo);
			}

			listaDocumentos = sqlQuery.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaDocumentos;
	}
	
	/**
	 * Pesquisa os documentos existentes
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Comunicacao> pesquisarDocumentosCorrecao(Setor setor, String usuario) throws DaoException {

		List listaDocumentos = null;
		StringBuffer sql = new StringBuffer();

		try {
			sql.append(" SELECT c.seq_comunicacao, dc.seq_documento_comunicacao ");
			sql.append(" FROM JUDICIARIO.COMUNICACAO c, JUDICIARIO.DOCUMENTO_COMUNICACAO dc ");
			sql.append(" , DOC.VW_DOCUMENTO vw ");

			sql.append(" , JUDICIARIO.COMUNICACAO_OBJETO_INCIDENTE coi, ");
			sql.append(" JUDICIARIO.OBJETO_INCIDENTE ob ");
			sql.append(" , JUDICIARIO.PROCESSO po");
			
			sql.append(" WHERE dc.SEQ_DOCUMENTO = vw.SEQ_DOCUMENTO(+) ");
			sql.append(" AND ( (dc.SEQ_COMUNICACAO(+) = c.SEQ_COMUNICACAO) ");
			sql.append(" AND (dc.SEQ_TIPO_SITUACAO_DOCUMENTO(+)<> :situacao) ");

			// pesquisa as comunicações que estão no setor ou as que estão deslocadas para o Setor
			sql.append(" AND ((c.seq_comunicacao IN (select dc.seq_comunicacao from JUDICIARIO.deslocamento_comunicacao dc where dc.cod_setor = :codSetor and dc.dat_saida is null))");
			sql.append(" OR (c.COD_SETOR = :codSetor)) ");
			
			sql.append(" AND (c.SEQ_COMUNICACAO = coi.SEQ_COMUNICACAO) ");
			sql.append(" AND (coi.SEQ_OBJETO_INCIDENTE = ob.SEQ_OBJETO_INCIDENTE) ");
			sql.append(" AND (ob.SEQ_OBJETO_INCIDENTE_PRINCIPAL = po.SEQ_OBJETO_INCIDENTE) ");
			sql.append(" AND (ob.TIP_OBJETO_INCIDENTE IN ('PR','IJ','RC')) ");
			sql.append(" AND (coi.TIP_VINCULO = 'P')) ");

			if (usuario != null) {
				sql.append(" AND (c.SIG_USUARIO_CRIACAO = :usuario) ");
			}

			sql.append("AND (c.seq_comunicacao IN (SELECT fs.seq_comunicacao ");
			sql.append(" FROM JUDICIARIO.fase_comunicacao fs  ");
			sql.append(" WHERE fs.seq_comunicacao = c.seq_comunicacao ");
			sql.append("  AND fs.flg_fase_atual = 'S' ");

			sql.append(" AND fs.seq_tipo_fase_comunicacao =  3 )) ");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			sqlQuery.setLong("situacao", 9L);

			
			if (setor != null) {
				sqlQuery.setLong("codSetor", setor.getId());
			}

			if (usuario != null) {
				sqlQuery.setString("usuario", usuario);
			}

			
			listaDocumentos = sqlQuery.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaDocumentos;
	}
	
	/**
	 * Pesquisa os quantidade documentos existentes
	 */
	@Override
	public int pesquisarDocumentosCount(Setor setor, Boolean buscaSomenteDocumentoFaseGerados) throws DaoException {

		BigDecimal quantidadeDocumentos;
		StringBuffer sql = new StringBuffer();

		try {
			sql.append(" SELECT COUNT(DISTINCT c.SEQ_COMUNICACAO) 					");
			sql.append(" FROM JUDICIARIO.COMUNICACAO C								");
			sql.append(" 	 ,JUDICIARIO.COMUNICACAO_OBJETO_INCIDENTE COI 			");
			sql.append(" 	 ,JUDICIARIO.OBJETO_INCIDENTE OI 						");
			sql.append(" 	 ,JUDICIARIO.FASE_COMUNICACAO FS						");
			sql.append(" 	 ,JUDICIARIO.DESLOCAMENTO_COMUNICACAO DC				");
			sql.append(" WHERE 1 = 1 												");
			sql.append("  AND C.SEQ_COMUNICACAO = COI.SEQ_COMUNICACAO				");
			sql.append("  AND COI.SEQ_OBJETO_INCIDENTE = OI.SEQ_OBJETO_INCIDENTE 	");
			sql.append("  AND C.SEQ_COMUNICACAO = FS.SEQ_COMUNICACAO				");
			sql.append("  AND C.SEQ_COMUNICACAO = DC.SEQ_COMUNICACAO(+)				");
			sql.append("  AND COI.TIP_VINCULO = 'P'									");
			sql.append("  AND OI.TIP_OBJETO_INCIDENTE IN ('PR','IJ','RC')			");
			sql.append("  AND FS.FLG_FASE_ATUAL = 'S'								");
			sql.append("  AND FS.SEQ_TIPO_FASE_COMUNICACAO = 2						");
			sql.append("  AND ( (C.COD_SETOR = :codSetor ) OR						");
			sql.append("  	    (DC.COD_SETOR = :codSetor AND DC.DAT_SAIDA IS NULL)	");	
			sql.append("  	  )														");	
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			if (setor != null) {
				sqlQuery.setLong("codSetor", setor.getId());
			}

			quantidadeDocumentos = (BigDecimal) sqlQuery.uniqueResult();
						
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return quantidadeDocumentos.intValue();
	}
	
	/**
	 * Pesquisa os quantidade documentos existentes
	 */
	@Override
	public int pesquisarDocumentosCorrecaoCount(Setor setor, String username) throws DaoException {

		BigDecimal quantidadeDocumentos;
		StringBuffer sql = new StringBuffer();

		try {
			sql.append(" SELECT COUNT(DISTINCT c.SEQ_COMUNICACAO) ");
			sql.append(" FROM JUDICIARIO.COMUNICACAO C								");
			sql.append(" 	 ,JUDICIARIO.COMUNICACAO_OBJETO_INCIDENTE COI 			");
			sql.append(" 	 ,JUDICIARIO.OBJETO_INCIDENTE OI 						");
			sql.append(" 	 ,JUDICIARIO.FASE_COMUNICACAO FS						");
			sql.append(" 	 ,JUDICIARIO.DESLOCAMENTO_COMUNICACAO DC				");
			sql.append(" WHERE 1 = 1 												");
			sql.append("  AND C.SEQ_COMUNICACAO = COI.SEQ_COMUNICACAO				");
			sql.append("  AND COI.SEQ_OBJETO_INCIDENTE = OI.SEQ_OBJETO_INCIDENTE 	");
			sql.append("  AND C.SEQ_COMUNICACAO = FS.SEQ_COMUNICACAO				");
			sql.append("  AND C.SEQ_COMUNICACAO = DC.SEQ_COMUNICACAO(+)				");
			sql.append("  AND COI.TIP_VINCULO = 'P'									");
			sql.append("  AND OI.TIP_OBJETO_INCIDENTE IN ('PR','IJ','RC')			");
			sql.append("  AND FS.FLG_FASE_ATUAL = 'S'								");
			sql.append("  AND FS.SEQ_TIPO_FASE_COMUNICACAO = 3						");
			sql.append("  AND ( (C.COD_SETOR = :codSetor ) OR						");
			sql.append("  	    (DC.COD_SETOR = :codSetor AND DC.DAT_SAIDA IS NULL)	");	
			sql.append("  	  )														");	

			if (username != null) {
				sql.append(" AND c.SIG_USUARIO_CRIACAO = :usuario ");
			}
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());


			if (username != null) {
				sqlQuery.setString("usuario", username);
			}
			
			if (setor != null) {
				sqlQuery.setLong("codSetor", setor.getId());
			}

			quantidadeDocumentos = (BigDecimal) sqlQuery.uniqueResult();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return quantidadeDocumentos.intValue();
	}
	
	

	/**
	 * Pesquisa os documentos existentes e disponíveis para assinatura
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Comunicacao> pesquisarDocumentosAssinatura(FiltroPesquisarDocumentosAssinatura filtro) throws DaoException {

		List listaDocumentos = null;
		StringBuffer sql = new StringBuffer();

		try {
			sql.append(" SELECT c.SEQ_COMUNICACAO, dc.SEQ_DOCUMENTO_COMUNICACAO, ");
			sql.append(" (SELECT NUM_ORDEM_PECA FROM JUDICIARIO.PECA_PROCESSUAL WHERE SEQ_OBJETO_INCIDENTE = COI.SEQ_OBJETO_INCIDENTE AND SEQ_DOCUMENTO = DC.SEQ_DOCUMENTO  AND SEQ_TIPO_SITUACAO_PECA IN (2,3) and rownum =1) as NUM_ORDEM_PECA , ");
			sql.append(" (SELECT SEQ_TIPO_SITUACAO_PECA FROM JUDICIARIO.PECA_PROCESSUAL WHERE SEQ_OBJETO_INCIDENTE = COI.SEQ_OBJETO_INCIDENTE AND SEQ_DOCUMENTO = DC.SEQ_DOCUMENTO AND SEQ_TIPO_SITUACAO_PECA IN (2,3) and rownum =1) as SEQ_TIPO_SITUACAO_PECA,  ");
			sql.append(" (SELECT SEQ_PECA_PROCESSUAL FROM JUDICIARIO.PECA_PROCESSUAL WHERE SEQ_OBJETO_INCIDENTE = COI.SEQ_OBJETO_INCIDENTE AND SEQ_DOCUMENTO = DC.SEQ_DOCUMENTO AND SEQ_TIPO_SITUACAO_PECA IN (2,3) and rownum =1) as PECA ");
			sql.append("  FROM JUDICIARIO.COMUNICACAO c, ");
			sql.append("       JUDICIARIO.DOCUMENTO_COMUNICACAO dc, ");
			sql.append("       JUDICIARIO.FASE_COMUNICACAO fs, ");
			sql.append("       JUDICIARIO.COMUNICACAO_OBJETO_INCIDENTE coi, ");
			sql.append("       JUDICIARIO.OBJETO_INCIDENTE ob, ");
			sql.append("	   JUDICIARIO.PROCESSO po, ");
			sql.append("	   JUDICIARIO.DESLOCAMENTO_COMUNICACAO desl ");

			sql.append(" WHERE c.SEQ_COMUNICACAO = dc.SEQ_COMUNICACAO ");
			
			if (filtro.getSetor() != null) {
					sql.append(" AND  c.SEQ_COMUNICACAO = desl.SEQ_COMUNICACAO AND desl.COD_SETOR = :codSetor and desl.DAT_SAIDA is null");
				}else {
					sql.append(" AND  c.SEQ_COMUNICACAO = desl.SEQ_COMUNICACAO AND desl.COD_SETOR > 0 and desl.DAT_SAIDA is null");
			}
			
			

			sql.append(" AND (c.SEQ_COMUNICACAO = coi.SEQ_COMUNICACAO) ");
			sql.append(" AND (coi.SEQ_OBJETO_INCIDENTE = ob.SEQ_OBJETO_INCIDENTE) ");
			sql.append(" AND (ob.SEQ_OBJETO_INCIDENTE_PRINCIPAL = po.SEQ_OBJETO_INCIDENTE) ");
			sql.append(" AND (ob.TIP_OBJETO_INCIDENTE IN ('PR','IJ','RC')) ");
			sql.append(" AND (coi.TIP_VINCULO = 'P') ");

			sql.append(" AND dc.SEQ_TIPO_SITUACAO_DOCUMENTO <> :situacao ");
			sql.append(" AND dc.SEQ_TIPO_SITUACAO_DOCUMENTO IN (:tipoSituacaoDocumento) ");

			sql.append(" AND fs.SEQ_COMUNICACAO = c.SEQ_COMUNICACAO ");
			sql.append(" AND fs.FLG_FASE_ATUAL = 'S' ");
			sql.append(" AND fs.SEQ_TIPO_FASE_COMUNICACAO = :tipoFaseComunicacao ");
			sql.append(" AND fs.seq_comunicacao  not in (select coi2.seq_comunicacao from judiciario.comunicacao_objeto_incidente coi2 where coi2.seq_objeto_incidente not in (SELECT oi2.seq_objeto_incidente FROM judiciario.objeto_incidente oi2, judiciario.objeto_incidente oi3 where oi2.seq_objeto_incidente_principal = oi3.seq_objeto_incidente)) ");
			
			if (filtro.getIds() != null && filtro.getIds().size() > 0) {
				sql.append(" AND c.SEQ_COMUNICACAO IN (:idsComunicacoes) ");
			}
			
			if(filtro.getDataDocumento() != null){
				if(filtro.getTela() != null && filtro.getTela().equals("expedirDocumentos")){
					sql.append(" AND fs.DAT_INCLUSAO BETWEEN :dataInicio AND :dataFim ");
				}
				if(filtro.getTela() != null && filtro.getTela().equals("assinarDocumentos")){
					sql.append(" AND desl.DAT_ENTRADA BETWEEN :dataInicio AND :dataFim ");
				}
			}

			if (filtro.isApenasSigilosos() ) {
				sql.append(" AND ( ob.TIP_CONFIDENCIALIDADE in ('OC','SG') ) ");
			}

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			if (filtro.getSetor() != null) {
				sqlQuery.setLong("codSetor", filtro.getSetor().getId());
			}

			if (filtro.getFaseDocumento() != null && filtro.getFaseDocumento() > 0L) {
				sqlQuery.setLong("tipoFaseComunicacao", filtro.getFaseDocumento());
			}

			if (filtro.getListaTipoSituacaoDocumento() != null && filtro.getListaTipoSituacaoDocumento().size() > 0L) {
				sqlQuery.setParameterList("tipoSituacaoDocumento", filtro.getListaTipoSituacaoDocumento());
			}

			if (filtro.getIds() != null && filtro.getIds().size() > 0) {
				sqlQuery.setParameterList("idsComunicacoes", filtro.getIds());
			}
			
			if (filtro.getDataDocumento() != null){
				Calendar dataIni = Calendar.getInstance();
				dataIni.setTime(filtro.getDataDocumento());
				dataIni.set(Calendar.HOUR_OF_DAY, 0);
				dataIni.set(Calendar.MINUTE, 0);				
				dataIni.set(Calendar.SECOND, 0);
				dataIni.set(Calendar.MILLISECOND, 0);
				sqlQuery.setTimestamp("dataInicio", dataIni.getTime());
				
				Calendar datafim = Calendar.getInstance();
				datafim.setTime(filtro.getDataDocumento());				
				datafim.set(Calendar.MILLISECOND, 0);
				datafim.set(Calendar.HOUR_OF_DAY, 23);
				datafim.set(Calendar.MINUTE, 59);
				datafim.set(Calendar.SECOND, 59);
				sqlQuery.setTimestamp("dataFim", datafim.getTime());
			}
			
			sqlQuery.setLong("situacao", 9L);

			listaDocumentos = sqlQuery.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaDocumentos;
	}
	
	@Override
	public int pesquisarDocumentosAssinaturaCount(FiltroPesquisarDocumentosAssinatura filtro) throws DaoException {
		try {
			BigDecimal quantidadeDocumentos;
			StringBuffer sql = new StringBuffer();

			sql.append(" SELECT COUNT(DISTINCT c.SEQ_COMUNICACAO) ");
			sql.append("  FROM JUDICIARIO.COMUNICACAO c, ");
			sql.append("       JUDICIARIO.DOCUMENTO_COMUNICACAO dc, ");
			sql.append("       JUDICIARIO.FASE_COMUNICACAO fs, ");
			sql.append("       JUDICIARIO.COMUNICACAO_OBJETO_INCIDENTE coi, ");
			sql.append("       JUDICIARIO.OBJETO_INCIDENTE ob, ");
			sql.append("	   JUDICIARIO.PROCESSO po, ");
			sql.append("	   JUDICIARIO.DESLOCAMENTO_COMUNICACAO desl ");

			sql.append(" WHERE c.SEQ_COMUNICACAO = dc.SEQ_COMUNICACAO ");
			if (filtro.getSetor() != null) {
				sql.append(" AND  c.SEQ_COMUNICACAO = desl.SEQ_COMUNICACAO AND desl.COD_SETOR = :codSetor and desl.DAT_SAIDA is null");
			}else {
				sql.append(" AND  c.SEQ_COMUNICACAO = desl.SEQ_COMUNICACAO AND desl.COD_SETOR > 0 and desl.DAT_SAIDA is null");
			}
			//sql.append(" AND  c.SEQ_COMUNICACAO = desl.SEQ_COMUNICACAO AND desl.COD_SETOR = :codSetor and desl.DAT_SAIDA is null");

			sql.append(" AND (c.SEQ_COMUNICACAO = coi.SEQ_COMUNICACAO) ");
			sql.append(" AND (coi.SEQ_OBJETO_INCIDENTE = ob.SEQ_OBJETO_INCIDENTE) ");
			sql.append(" AND (ob.SEQ_OBJETO_INCIDENTE_PRINCIPAL = po.SEQ_OBJETO_INCIDENTE) ");
			sql.append(" AND (ob.TIP_OBJETO_INCIDENTE IN ('PR','IJ','RC')) ");
			sql.append(" AND (coi.TIP_VINCULO = 'P') ");
			
			sql.append(" AND dc.SEQ_TIPO_SITUACAO_DOCUMENTO <> :situacao ");
			
			if (CollectionUtils.isNotEmpty(filtro.getListaTipoSituacaoDocumento())) {
				sql.append(" AND dc.SEQ_TIPO_SITUACAO_DOCUMENTO IN (:tipoSituacaoDocumento) ");
			}

			sql.append(" AND fs.SEQ_COMUNICACAO = c.SEQ_COMUNICACAO ");
			sql.append(" AND fs.FLG_FASE_ATUAL = 'S' ");
			sql.append(" AND fs.SEQ_TIPO_FASE_COMUNICACAO = :tipoFaseComunicacao");
			
			if(filtro.getDataDocumento() != null){
				if(filtro.getTela() != null && filtro.getTela().equals("expedirDocumentos")){
					sql.append(" AND fs.DAT_INCLUSAO BETWEEN :dataInicio AND :dataFim ");
				}
				if(filtro.getTela() != null && filtro.getTela().equals("assinarDocumentos")){
					sql.append(" AND desl.DAT_ENTRADA BETWEEN :dataInicio AND :dataFim ");
				}
			}

			if( filtro.isApenasSigilosos() ){
				sql.append(" AND (ob.TIP_CONFIDENCIALIDADE IN ('OC','SG')) ");
			}

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			if (filtro.getSetor() != null) {
				sqlQuery.setLong("codSetor", filtro.getSetor().getId());
			}

			if (filtro.getFaseDocumento() != null && filtro.getFaseDocumento() > 0L) {
				sqlQuery.setLong("tipoFaseComunicacao", filtro.getFaseDocumento());
			}

			if (CollectionUtils.isNotEmpty(filtro.getListaTipoSituacaoDocumento())) {
				sqlQuery.setParameterList("tipoSituacaoDocumento", filtro.getListaTipoSituacaoDocumento());
			}
			
			if (filtro.getDataDocumento() != null){
				Calendar dataIni = Calendar.getInstance();
				dataIni.setTime(filtro.getDataDocumento());
				dataIni.set(Calendar.HOUR_OF_DAY, 0);
				dataIni.set(Calendar.MINUTE, 0);
				dataIni.set(Calendar.SECOND, 0);
				dataIni.set(Calendar.MILLISECOND, 0);
				sqlQuery.setTimestamp("dataInicio", dataIni.getTime());
				
				Calendar dataFim = Calendar.getInstance();
				dataFim.setTime(filtro.getDataDocumento());
				dataFim.set(Calendar.HOUR_OF_DAY, 23);
				dataFim.set(Calendar.MINUTE, 59);
				dataFim.set(Calendar.SECOND, 59);
				dataFim.set(Calendar.MILLISECOND, 0);
				sqlQuery.setTimestamp("dataFim", dataFim.getTime());
			}			

			sqlQuery.setLong("situacao", 9L);
			
			quantidadeDocumentos = (BigDecimal) sqlQuery.uniqueResult();
			return quantidadeDocumentos.intValue();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Pesquisa os documentos elaborados por unidade com base na data de pesquisa e no setor
	 */
	@Override
	public List pesquisarDocumentosUnidade(Date dataPesquisa, Long idSetor) throws DaoException {
		List listaDocumentos = null;
		StringBuffer sql = new StringBuffer();

		try {

			sql.append("			SELECT c.seq_comunicacao, ");
			sql.append("		      s.dsc_setor, ");
			sql.append("		       DECODE (tfc.dsc_tipo_fase_comunicacao,");
			sql.append("		               'PDF gerado', 'Elaborado',");
			sql.append("		               'Aguardando assinatura', 'Enviado para Assinatura')");
			sql.append("		  FROM judiciario.comunicacao c,");
			sql.append("		       stf.desloca_processos dp,");
			sql.append("		       stf.setores s,");
			sql.append("		       judiciario.comunicacao_objeto_incidente coi,");
			sql.append("		       judiciario.processo p,");
			sql.append("		       judiciario.objeto_incidente oi,");
			sql.append("		       judiciario.fase_comunicacao fc,");
			sql.append("		       judiciario.tipo_fase_comunicacao tfc");
			sql.append("		 WHERE     coi.seq_comunicacao = c.seq_comunicacao");
			sql.append("		       AND coi.seq_objeto_incidente = oi.seq_objeto_incidente");
			sql.append("		       AND coi.tip_vinculo = 'P'");
			sql.append("		       AND p.seq_objeto_incidente = oi.seq_objeto_incidente_principal");
			sql.append("		       AND dp.sig_classe_proces = p.sig_classe_proces");
			sql.append("		       AND dp.num_processo = p.num_processo");
			sql.append("		       AND dp.flg_ultimo_deslocamento = 'S'");
			sql.append("		       AND dp.cod_orgao_destino = s.cod_setor");
			sql.append("		       AND c.cod_setor = :idSetor");
			sql.append("		       AND fc.seq_fase_comunicacao =");
			sql.append("		               (SELECT MAX (fcii.seq_fase_comunicacao)");
			sql.append("		                  FROM judiciario.fase_comunicacao fcii");
			sql.append("		                 WHERE fcii.seq_tipo_fase_comunicacao IN (1, 2, 4)");
			sql.append("		                       AND fcii.seq_comunicacao = c.seq_comunicacao");
			sql.append("		                       AND fcii.dat_lancamento >=");
			sql.append("		                               TO_DATE ('" +  new SimpleDateFormat("dd/MM/yyyy").format(dataPesquisa) + " 00:00:00',");
			sql.append("		                                        'DD/MM/YYYY HH24:MI:SS')");
			sql.append("		                       AND fcii.dat_lancamento <=");
			sql.append("		                               TO_DATE ('" +  new SimpleDateFormat("dd/MM/yyyy").format(dataPesquisa) + " 23:59:59',");
			sql.append("		                                        'DD/MM/YYYY HH24:MI:SS'))");
			sql.append("		       AND tfc.seq_tipo_fase_comunicacao = fc.seq_tipo_fase_comunicacao");
			sql.append("		ORDER BY c.seq_comunicacao");

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			sqlQuery.setLong("idSetor", idSetor);

			listaDocumentos = sqlQuery.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaDocumentos;
	}

	/**
	 * Pesquisa os documentos elaborados com base no Processo ou na Fase
	 */
	@Override
	public List<Comunicacao> pesquisarDocumentosElaborados(String siglaClasse, Long numeroProcesso, Long codigoFase, Long idSetorAtual, Long idSetorFase, Long numeracaoUnica,
			Long anoNumeracaoUnica, String dataInicial, String dataFinal) throws DaoException {

		List<Comunicacao> listaDocumentos = null;
		StringBuffer sql = new StringBuffer();
		
		try {

			sql.append(" SELECT DISTINCT c.seq_comunicacao, dc.seq_documento_comunicacao 	");
			sql.append(" FROM JUDICIARIO.COMUNICACAO c									 	");
			sql.append(" 	, JUDICIARIO.DOCUMENTO_COMUNICACAO dc 						 	");

			if (siglaClasse != null || numeroProcesso != null) {
				sql.append("	, JUDICIARIO.COMUNICACAO_OBJETO_INCIDENTE coi 				");
				sql.append("    , JUDICIARIO.OBJETO_INCIDENTE ob							");
				sql.append("    , JUDICIARIO.PROCESSO P										");
			}

			sql.append(" WHERE dc.SEQ_COMUNICACAO(+) = c.SEQ_COMUNICACAO	 				");
			sql.append(" AND dc.SEQ_TIPO_SITUACAO_DOCUMENTO(+)<> :situacao	 				");

			if (numeracaoUnica != null && numeracaoUnica > 0L) {
				sql.append(" AND c.num_comunicacao = :numeracaoUnica ");
			}

			if (anoNumeracaoUnica != null && anoNumeracaoUnica > 0L) {
				sql.append(" AND to_char(c.dat_inclusao, 'YYYY') = :anoNumeracaoUnica ");
				sql.append(" AND c.num_comunicacao IS NOT NULL ");
			}

			if (siglaClasse != null && siglaClasse != "") {
				sql.append(" AND upper(p.sig_classe_proces) = :siglaClasse ");
			}

			if (numeroProcesso != null && numeroProcesso > 0L) {
				sql.append(" AND p.num_processo = :numeroProcesso ");
			}

			if (siglaClasse != null || numeroProcesso != null) {
				sql.append(" AND c.SEQ_COMUNICACAO = coi.SEQ_COMUNICACAO 							");
				sql.append(" AND coi.SEQ_OBJETO_INCIDENTE = ob.SEQ_OBJETO_INCIDENTE 				");
				sql.append(" AND p.seq_objeto_incidente = ob.seq_objeto_incidente_principal 		");
			}

//			if ( codigoFase != null &&  ((dataInicial != null && !dataInicial.trim().isEmpty()) || (dataFinal != null && !dataFinal.trim().isEmpty()))) {
			if ( codigoFase != null ) {
				sql.append(" AND EXISTS (															");
				sql.append(" SELECT 1 FROM JUDICIARIO.FASE_COMUNICACAO fc2							");
				sql.append(" WHERE fc2.seq_comunicacao = c.seq_comunicacao							");
				sql.append(" AND fc2.seq_tipo_fase_comunicacao = :tipoFaseComunicacao				");
				//if ( (dataInicial == null || dataInicial.trim().isEmpty()) && (dataFinal == null || dataFinal.trim().isEmpty())) {
					sql.append(" AND fc2.flg_fase_atual = 'S'										");
				//}
				if (dataInicial != null && dataInicial.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(fc2.DAT_LANCAMENTO) >= TO_DATE(:dtInicial,'dd/mm/yyyy') 	");

				}
				if (dataFinal != null && dataFinal.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(fc2.DAT_LANCAMENTO) <= TO_DATE(:dtFinal,'dd/mm/yyyy') 	");
				}
				
				if ( idSetorFase != null ) {
					sql.append(" AND EXISTS (																	");
					sql.append(" SELECT 1 FROM JUDICIARIO.DESLOCAMENTO_COMUNICACAO DC2							");
					sql.append(" WHERE DC2.SEQ_COMUNICACAO = FC2.SEQ_COMUNICACAO 								");
					sql.append(" AND DC2.COD_SETOR = :idSetorFase												");
					sql.append(" AND FC2.DAT_LANCAMENTO BETWEEN DC2.DAT_ENTRADA AND NVL(DC2.DAT_SAIDA,SYSDATE)  ");
					sql.append(" )																				");
				}
				
				sql.append(" )																					");
			}


			if ( idSetorAtual != null ) {
				sql.append(" AND EXISTS (															");
				sql.append(" SELECT 1 FROM JUDICIARIO.DESLOCAMENTO_COMUNICACAO DC2					");
				sql.append(" WHERE DC2.SEQ_COMUNICACAO = C.SEQ_COMUNICACAO 							");
				sql.append(" AND DC2.DAT_SAIDA IS NULL 												");
				sql.append(" AND DC2.COD_SETOR = :idSetorAtual										");
				if ( codigoFase == null && dataInicial != null && dataInicial.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(DC2.DAT_ENTRADA) >= TO_DATE(:dtInicial,'dd/mm/yyyy') 	");

				}
				if ( codigoFase == null && dataFinal != null && dataFinal.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(DC2.DAT_ENTRADA) <= TO_DATE(:dtFinal,'dd/mm/yyyy') 	");
				}
				
				sql.append(" )																		");
			}

			if ( codigoFase == null && idSetorFase == null && idSetorAtual == null) {
				sql.append(" AND EXISTS (															");
				sql.append(" SELECT 1 FROM JUDICIARIO.FASE_COMUNICACAO FC3							");
				sql.append(" WHERE FC3.SEQ_COMUNICACAO = C.SEQ_COMUNICACAO 							");
				
				if (dataInicial != null && dataInicial.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(fc3.DAT_LANCAMENTO) >= TO_DATE(:dtInicial,'dd/mm/yyyy') 	");

				}
				if (dataFinal != null && dataFinal.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(fc3.DAT_LANCAMENTO) <= TO_DATE(:dtFinal,'dd/mm/yyyy') 	");
				}
				
				sql.append(" )																		");
			}


			sql.append(" AND ROWNUM <=250 ");
			sql.append(" ORDER BY c.seq_comunicacao ");

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			sqlQuery.setLong("situacao", 9L);

			if (dataInicial != null && dataInicial.trim().isEmpty() == false) {
				sqlQuery.setString("dtInicial", dataInicial);
			}
		
			if (dataFinal != null && dataFinal.trim().isEmpty() == false) {
				sqlQuery.setString("dtFinal", dataFinal);
			}

			if (siglaClasse != null && siglaClasse != "") {
				sqlQuery.setString("siglaClasse", siglaClasse);
			}

			if (numeroProcesso != null && numeroProcesso > 0L) {
				sqlQuery.setLong("numeroProcesso", numeroProcesso);
			}

			if (codigoFase != null && codigoFase > 0L) {
				sqlQuery.setLong("tipoFaseComunicacao", codigoFase);
			}

			if (numeracaoUnica != null && numeracaoUnica > 0L) {
				sqlQuery.setLong("numeracaoUnica", numeracaoUnica);
			}
			if (anoNumeracaoUnica != null && anoNumeracaoUnica > 0L) {
				sqlQuery.setString("anoNumeracaoUnica", anoNumeracaoUnica.toString());
			}

			if (idSetorAtual != null && idSetorAtual > 0L) {
				sqlQuery.setLong("idSetorAtual", idSetorAtual);
			}

			if (idSetorFase != null && idSetorFase > 0L) {
				sqlQuery.setLong("idSetorFase", idSetorFase);
			}

			listaDocumentos = sqlQuery.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaDocumentos;
	}

	
	@Override
	public List<Comunicacao> pesquisarDocumentosElaboradosSigilosos(String siglaClasse, Long numeroProcesso, Long codigoFase, Long idSetorAtual, Long idSetorFase, Long numeracaoUnica,
			Long anoNumeracaoUnica, String dataInicial, String dataFinal) throws DaoException {

		List<Comunicacao> listaDocumentos = null;
		StringBuffer sql = new StringBuffer();
		
		try {
			/*
			sql.append(" SELECT DISTINCT c.seq_comunicacao, dc.seq_documento_comunicacao 	");
			sql.append(" FROM JUDICIARIO.COMUNICACAO c									 	");
			sql.append(" 	, JUDICIARIO.DOCUMENTO_COMUNICACAO dc 						 	");

			if (siglaClasse != null || numeroProcesso != null) {
				sql.append("	, JUDICIARIO.COMUNICACAO_OBJETO_INCIDENTE coi 				");
				sql.append("    , JUDICIARIO.OBJETO_INCIDENTE ob							");
				sql.append("    , JUDICIARIO.PROCESSO P										");
			}

			sql.append(" WHERE dc.SEQ_COMUNICACAO(+) = c.SEQ_COMUNICACAO	 				");
			sql.append(" AND dc.SEQ_TIPO_SITUACAO_DOCUMENTO(+)<> :situacao	 				");

			if (numeracaoUnica != null && numeracaoUnica > 0L) {
				sql.append(" AND c.num_comunicacao = :numeracaoUnica ");
			}

			if (anoNumeracaoUnica != null && anoNumeracaoUnica > 0L) {
				sql.append(" AND to_char(c.dat_inclusao, 'YYYY') = :anoNumeracaoUnica ");
				sql.append(" AND c.num_comunicacao IS NOT NULL ");
			}

			if (siglaClasse != null && siglaClasse != "") {
				sql.append(" AND upper(p.sig_classe_proces) = :siglaClasse ");
			}

			if (numeroProcesso != null && numeroProcesso > 0L) {
				sql.append(" AND p.num_processo = :numeroProcesso ");
			}

			if (siglaClasse != null || numeroProcesso != null) {
				sql.append(" AND c.SEQ_COMUNICACAO = coi.SEQ_COMUNICACAO 							");
				sql.append(" AND coi.SEQ_OBJETO_INCIDENTE = ob.SEQ_OBJETO_INCIDENTE 				");
				sql.append(" AND p.seq_objeto_incidente = ob.seq_objeto_incidente_principal 		");
			}


			if ( codigoFase != null ) {
				sql.append(" AND EXISTS (															");
				sql.append(" SELECT 1 FROM JUDICIARIO.FASE_COMUNICACAO fc2							");
				sql.append(" WHERE fc2.seq_comunicacao = c.seq_comunicacao							");
				sql.append(" AND fc2.seq_tipo_fase_comunicacao = :tipoFaseComunicacao				");
				//if ( (dataInicial == null || dataInicial.trim().isEmpty()) && (dataFinal == null || dataFinal.trim().isEmpty())) {
					sql.append(" AND fc2.flg_fase_atual = 'S'										");
				//}
				if (dataInicial != null && dataInicial.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(fc2.DAT_LANCAMENTO) >= TO_DATE(:dtInicial,'dd/mm/yyyy') 	");

				}
				if (dataFinal != null && dataFinal.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(fc2.DAT_LANCAMENTO) <= TO_DATE(:dtFinal,'dd/mm/yyyy') 	");
				}
				
				if ( idSetorFase != null ) {
					sql.append(" AND EXISTS (																	");
					sql.append(" SELECT 1 FROM JUDICIARIO.DESLOCAMENTO_COMUNICACAO DC2							");
					sql.append(" WHERE DC2.SEQ_COMUNICACAO = FC2.SEQ_COMUNICACAO 								");
					sql.append(" AND DC2.COD_SETOR = :idSetorFase												");
					sql.append(" AND FC2.DAT_LANCAMENTO BETWEEN DC2.DAT_ENTRADA AND NVL(DC2.DAT_SAIDA,SYSDATE)  ");
					sql.append(" )																				");
				}
				
				sql.append(" )																					");
			}


			if ( idSetorAtual != null ) {
				sql.append(" AND EXISTS (															");
				sql.append(" SELECT 1 FROM JUDICIARIO.DESLOCAMENTO_COMUNICACAO DC2					");
				sql.append(" WHERE DC2.SEQ_COMUNICACAO = C.SEQ_COMUNICACAO 							");
				sql.append(" AND DC2.DAT_SAIDA IS NULL 												");
				sql.append(" AND DC2.COD_SETOR = :idSetorAtual										");
				if ( codigoFase == null && dataInicial != null && dataInicial.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(DC2.DAT_ENTRADA) >= TO_DATE(:dtInicial,'dd/mm/yyyy') 	");

				}
				if ( codigoFase == null && dataFinal != null && dataFinal.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(DC2.DAT_ENTRADA) <= TO_DATE(:dtFinal,'dd/mm/yyyy') 	");
				}
				
				sql.append(" )																		");
			}

			if ( codigoFase == null && idSetorFase == null && idSetorAtual == null) {
				sql.append(" AND EXISTS (															");
				sql.append(" SELECT 1 FROM JUDICIARIO.FASE_COMUNICACAO FC3							");
				sql.append(" WHERE FC3.SEQ_COMUNICACAO = C.SEQ_COMUNICACAO 							");
				
				if (dataInicial != null && dataInicial.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(fc3.DAT_LANCAMENTO) >= TO_DATE(:dtInicial,'dd/mm/yyyy') 	");

				}
				if (dataFinal != null && dataFinal.trim().isEmpty() == false) {
					sql.append(" AND TRUNC(fc3.DAT_LANCAMENTO) <= TO_DATE(:dtFinal,'dd/mm/yyyy') 	");
				}
				
				sql.append(" )																		");
			}


			sql.append(" AND ROWNUM <=250 ");
			sql.append(" ORDER BY c.seq_comunicacao ");

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			sqlQuery.setLong("situacao", 9L);

			if (dataInicial != null && dataInicial.trim().isEmpty() == false) {
				sqlQuery.setString("dtInicial", dataInicial);
			}
		
			if (dataFinal != null && dataFinal.trim().isEmpty() == false) {
				sqlQuery.setString("dtFinal", dataFinal);
			}

			if (siglaClasse != null && siglaClasse != "") {
				sqlQuery.setString("siglaClasse", siglaClasse);
			}

			if (numeroProcesso != null && numeroProcesso > 0L) {
				sqlQuery.setLong("numeroProcesso", numeroProcesso);
			}

			if (codigoFase != null && codigoFase > 0L) {
				sqlQuery.setLong("tipoFaseComunicacao", codigoFase);
			}

			if (numeracaoUnica != null && numeracaoUnica > 0L) {
				sqlQuery.setLong("numeracaoUnica", numeracaoUnica);
			}
			if (anoNumeracaoUnica != null && anoNumeracaoUnica > 0L) {
				sqlQuery.setString("anoNumeracaoUnica", anoNumeracaoUnica.toString());
			}

			if (idSetorAtual != null && idSetorAtual > 0L) {
				sqlQuery.setLong("idSetorAtual", idSetorAtual);
			}

			if (idSetorFase != null && idSetorFase > 0L) {
				sqlQuery.setLong("idSetorFase", idSetorFase);
			}
			
			*/
			
			
			sql.append(" SELECT pr.sig_classe_proces, pr.num_processo, ");
			sql.append("  (CASE ");
			sql.append("   WHEN oi.num_nivel_sigilo = 0 THEN 'Público' ");
			sql.append("   WHEN oi.num_nivel_sigilo = 1 THEN 'Segredo de Justiça (1)' ");
			sql.append("   WHEN oi.num_nivel_sigilo = 2 THEN 'Sigilo Moderado (2)' ");
			sql.append("   WHEN oi.num_nivel_sigilo = 3 THEN 'Sigilo Padrão (3)' ");
			sql.append("   WHEN oi.num_nivel_sigilo = 4 THEN 'Sigilo Máximo (4)' ");
			sql.append("   ELSE '' ");
			sql.append("   END) ");
			sql.append(" AS sigilo, ");
			sql.append(" (SELECT m.nom_ministro FROM stf.ministros m WHERE m.cod_ministro = oi.cod_relator_incidente and oi.num_nivel_sigilo = 3) AS relator, "); 
			sql.append(" se.dsc_setor, "); 
			sql.append("  to_char(dc.dat_entrada, 'DD/MM/YYYY HH24:MI') as entrada ");
			sql.append("  FROM judiciario.fase_comunicacao fc, ");
			sql.append("       judiciario.comunicacao_objeto_incidente oc, ");
			sql.append("       judiciario.vwmt_objeto_incidente_sigiloso oi, ");
			sql.append("       judiciario.vwmt_processo_sigiloso pr, ");
			sql.append("       judiciario.comunicacao co, ");
			sql.append("       judiciario.deslocamento_comunicacao dc, ");
			sql.append("       stf.setores se ");
			sql.append(" WHERE     fc.seq_tipo_fase_comunicacao IN (:situacao) ");
			sql.append("       AND fc.flg_fase_atual = 'S' ");
			sql.append("       AND fc.seq_comunicacao = oc.seq_comunicacao ");
			sql.append("       AND oc.seq_objeto_incidente = oi.seq_objeto_incidente ");
			sql.append("       AND oi.seq_objeto_incidente_principal = pr.seq_objeto_incidente ");
			sql.append("       AND pr.flg_tramitacao = 'S' ");
			sql.append("       AND co.seq_comunicacao = fc.seq_comunicacao ");
			sql.append("       AND dc.seq_comunicacao = fc.seq_comunicacao ");
			sql.append("       and dc.cod_setor in (600000687,600000902) ");
			sql.append("       AND dc.dat_saida IS NULL ");
			sql.append("       AND dc.cod_setor = se.cod_setor ");
			sql.append("       AND oi.num_nivel_sigilo IN (0,1,2,3,4) ");

			
			if (dataInicial != null && dataInicial.trim().isEmpty() == false) {
				sql.append(" AND TRUNC(fc.dat_lancamento) >= TO_DATE(:dtInicial,'dd/mm/yyyy') 	");

			}
			if (dataFinal != null && dataFinal.trim().isEmpty() == false) {
				sql.append(" AND TRUNC(fc.dat_lancamento) <= TO_DATE(:dtFinal,'dd/mm/yyyy') 	");
			}
			
			
			if (siglaClasse != null && siglaClasse != "") {
				sql.append("       AND upper(pr.sig_classe_proces) =upper(trim(' "+ siglaClasse +"')) ");
			}
			
			if (numeroProcesso != null && numeroProcesso > 0) {
				sql.append("       AND upper(pr.num_processo ) =upper(trim(' "+ numeroProcesso +"')) ");
			}
			
			sql.append(" AND ROWNUM <=250 ");
			sql.append("       order by dc.dat_entrada");
			
		

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
			if (dataInicial != null && dataInicial.trim().isEmpty() == false) {
				sqlQuery.setString("dtInicial", dataInicial);
			}
		
			if (dataFinal != null && dataFinal.trim().isEmpty() == false) {
				sqlQuery.setString("dtFinal", dataFinal);
			}
			sqlQuery.setLong("situacao", 5L);
			
			
			
			listaDocumentos = sqlQuery.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaDocumentos;
	}
	
	@Override
	public Comunicacao recuperarPorId(Long idComunicacao) throws DaoException {
		Comunicacao comunicacao = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("select c from Comunicacao c where c.id = " + idComunicacao);

			Query q = session.createQuery(hql.toString());
			comunicacao = (Comunicacao) q.uniqueResult();
			
			session.refresh(comunicacao);

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return comunicacao;
	}

	public List pesquisarDocumentosAssinadosPorPeriodo(Long codigoSetor, String usuario, String dataInicial, String dataFinal) throws DaoException{
		List listaDocumentos = null;
		StringBuffer sql = new StringBuffer();
		Session session = retrieveSession();
		
		try {
			sql.append("SELECT fac.seq_comunicacao AS seq_comunicacao,  ");
			sql.append("	   doc.seq_documento_comunicacao as seq_documento_comunicacao,  ");
			sql.append("	   (SELECT DECODE(seq_objeto_incidente_principal, null,seq_objeto_incidente,seq_objeto_incidente_principal) ");
			sql.append("            FROM judiciario.objeto_incidente ");
			sql.append("			     WHERE seq_objeto_incidente =  coi.seq_objeto_incidente) ");
			sql.append("			AS seq_objeto_incidente_processo, ");
			sql.append("	   (SELECT CONCAT (CONCAT (sig_classe_proces, ' '), TO_CHAR (num_processo)) ");
			sql.append(" 		   	FROM judiciario.processo p ");
			sql.append("	        	WHERE p.seq_objeto_incidente = coi.seq_objeto_incidente ");
			sql.append("	            UNION ");
			sql.append("	            SELECT CONCAT (CONCAT (sig_classe_proces, ' '), CONCAT (CONCAT (TO_CHAR (num_processo), ' '), REPLACE (sig_cadeia_incidente,CONCAT (TRIM (sig_classe_proces), '-'),''))) ");
			sql.append("	                FROM judiciario.recurso_processo rp ");
			sql.append("	                WHERE rp.seq_objeto_incidente = coi.seq_objeto_incidente ");
			sql.append("	            UNION ");
			sql.append("	            SELECT CONCAT (CONCAT (sig_classe_proces, ' '),CONCAT (CONCAT (TO_CHAR (num_processo), ' '), REPLACE (sig_cadeia_incidente, CONCAT (TRIM (sig_classe_proces), '-'),''))) ");
			sql.append("	                FROM judiciario.incidente_julgamento ij ");
			sql.append("	                WHERE ij.seq_objeto_incidente = coi.seq_objeto_incidente) ");
			sql.append("	       AS processo, ");
			sql.append("	       coc.dsc_comunicacao AS documento, ");
			sql.append("	       fac.dat_lancamento AS data_assinatura, ");
			sql.append("	       fac.usu_inclusao AS assinador, ");
			sql.append("	       (SELECT ftc.dsc_tipo_fase_comunicacao FROM judiciario.tipo_fase_comunicacao ftc WHERE ftc.seq_tipo_fase_comunicacao IN ");
			sql.append("	           (SELECT seq_tipo_fase_comunicacao FROM judiciario.fase_comunicacao WHERE seq_comunicacao = coi.seq_comunicacao AND flg_fase_atual = 'S')) ");
			sql.append("	       AS fase_atual, ");
			sql.append("	       (SELECT dat_lancamento FROM judiciario.fase_comunicacao WHERE seq_comunicacao = coi.seq_comunicacao AND flg_fase_atual = 'S') "); 
			sql.append("	       AS data_fase, ");
			sql.append("	       (SELECT usu_inclusao FROM judiciario.fase_comunicacao WHERE seq_comunicacao = coi.seq_comunicacao AND flg_fase_atual = 'S') ");
			sql.append("	       AS ultimo ");
			sql.append("FROM judiciario.comunicacao_objeto_incidente coi, ");
			sql.append("	judiciario.fase_comunicacao fac, ");
			sql.append("	judiciario.comunicacao coc, ");
			sql.append("	judiciario.documento_comunicacao doc ");
			sql.append("WHERE     coc.seq_comunicacao = coi.seq_comunicacao ");
			sql.append("	AND coi.seq_comunicacao = fac.seq_comunicacao ");
			sql.append("	AND doc.seq_comunicacao = fac.seq_comunicacao ");
			sql.append("	AND fac.seq_tipo_fase_comunicacao = 5 ");
			sql.append("	AND doc.seq_tipo_situacao_documento <> 9 ");
			
			if ((usuario != null && usuario.trim().isEmpty() == false)){
				sql.append("	AND fac.usu_inclusao = :usuario  ");
			}else{
				sql.append("	AND fac.usu_inclusao IN (SELECT SIG_USUARIO FROM STF.USUARIOS WHERE COD_SETOR=:codigoSetor) ");
			}
			
			if ((dataInicial != null && dataInicial.trim().isEmpty() == false) && (dataFinal != null && dataFinal.trim().isEmpty() == false)) {
				sql.append("	AND fac.dat_inclusao BETWEEN TO_DATE(:dataInicial,'dd/mm/yyyy HH24:MI:SS') AND TO_DATE(:dataFinal,'dd/mm/yyyy HH24:MI:SS')  ");
			} 
			
			sql.append(" ORDER BY data_assinatura desc ");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			if ((usuario != null && usuario.trim().isEmpty() == false)){
				sqlQuery.setString("usuario", usuario);
			} else{
				sqlQuery.setLong("codigoSetor", codigoSetor);
			}

			if ((dataInicial != null && dataInicial.trim().isEmpty() == false) && (dataFinal != null && dataFinal.trim().isEmpty() == false)) {
				sqlQuery.setString("dataInicial", dataInicial+" 00:00:00");
				sqlQuery.setString("dataFinal", dataFinal+" 23:59:59");
			} 
			
			listaDocumentos = sqlQuery.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaDocumentos;
	}	
	
	@Override
	public List<Comunicacao> comunicacoesDoPeriodo(ComunicacaoSearch search) throws DaoException {

		try {

			Session session = retrieveSession();
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT c.seq_comunicacao ");
			sql.append("   FROM judiciario.comunicacao c ");
			sql.append("   JOIN judiciario.comunicacao_objeto_incidente coi ON coi.seq_comunicacao = c.seq_comunicacao ");
			sql.append("   JOIN judiciario.objeto_incidente oi ON oi.seq_objeto_incidente = coi.seq_objeto_incidente ");
			sql.append("  WHERE c.num_comunicacao = :numeroComunicacao ");
			if (search != null && search.getDataInicio() != null && search.getDataFim() != null) {
				sql.append("    AND c.dat_inclusao BETWEEN TO_DATE(:dataInicial,'dd/mm/yyyy HH24:MI:SS') AND TO_DATE(:dataFinal,'dd/mm/yyyy HH24:MI:SS') ");
			}
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
			sqlQuery.setLong("numeroComunicacao",search.getNumeroComunicacao());
			
			if (search != null && search.getDataInicio() != null && search.getDataFim() != null) {
				SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
				sqlQuery.setString("dataInicial", formatDate.format(search.getDataInicio())+" 00:00:00");
				sqlQuery.setString("dataFinal", formatDate.format(search.getDataFim())+" 23:59:59");
			} 

			List listaComunicacoes = sqlQuery.list();
			
			if(listaComunicacoes == null || listaComunicacoes.isEmpty()){
				return new ArrayList<Comunicacao>();
			}
			
			List<Long> idsCom = new ArrayList<Long>();
			for( Object x : listaComunicacoes){
				idsCom.add(Long.parseLong(((BigDecimal) x).toString()));
			}
			
			List<SearchCriterion> searchCriterion = new ArrayList<SearchCriterion>();
			searchCriterion.add(new InCriterion<Long>("id", idsCom));			
			return pesquisarPorExemplo(new Comunicacao(), searchCriterion);

		} catch (Exception e) {
			throw new DaoException(e);
		}

	}
	
	@Override
	public List pesquisarProcessosPorDocumento(Long tipoComunicacao, Long numeroDocumento, Long anoDocumento) throws DaoException {

		List listaProcessos = null;
		StringBuffer sql = new StringBuffer();
		Session session = retrieveSession();

		try {

			sql.append(" SELECT DISTINCT p.seq_objeto_incidente, pr.sig_classe_proces, pr.num_processo ");
			sql.append("   FROM judiciario.comunicacao c  ");
			sql.append("   JOIN judiciario.documento_comunicacao dc          ON dc.seq_comunicacao = c.seq_comunicacao AND dc.seq_tipo_situacao_documento <> 9 ");
			
			sql.append("   JOIN judiciario.modelo_comunicacao mc             ON c.seq_modelo_comunicacao = mc.seq_modelo_comunicacao ");
			sql.append("   JOIN expedicao.tipo_comunicacao_de_para tc        ON tc.seq_tipo_comunicacao = mc.seq_tipo_comunicacao AND tc.seq_tipo_comunicacao_expedicao = :tipoComunicacao "); 			
			sql.append("   JOIN judiciario.fase_comunicacao fc               ON fc.seq_comunicacao = c.seq_comunicacao AND fc.flg_fase_atual = 'S' ");
			sql.append("   JOIN judiciario.comunicacao_objeto_incidente coi  ON c.seq_comunicacao = coi.seq_comunicacao ");
			sql.append("   JOIN judiciario.objeto_incidente ob               ON coi.seq_objeto_incidente = ob.seq_objeto_incidente ");
			sql.append("   JOIN judiciario.processo p                        ON p.seq_objeto_incidente = ob.seq_objeto_incidente_principal ");
			sql.append("   JOIN judiciario.vw_processo_relator pr            ON p.seq_objeto_incidente = pr.seq_objeto_incidente ");
			sql.append("  WHERE c.num_comunicacao = :numeroDocumento AND TO_CHAR (c.dat_inclusao, 'YYYY') = :anoDocumento				 ");
			sql.append(" ORDER BY  p.seq_objeto_incidente ");

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
			
			sqlQuery.setLong("tipoComunicacao", tipoComunicacao);
			sqlQuery.setLong("numeroDocumento", numeroDocumento);
			sqlQuery.setLong("anoDocumento", anoDocumento);
			
			listaProcessos = sqlQuery.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaProcessos;
	}
	
	@Override
	public Comunicacao obterUltimaComunicacao(ObjetoIncidente objetoIncidente, Pessoa pessoa, ModeloComunicacao modeloComunicacao) throws DaoException {
		try {
			String hql = " SELECT max(c)"
					+ " FROM Comunicacao c"
					+ " JOIN c.comunicacaoIncidente ci"
					+ " WHERE ci.objetoIncidente = :objetoIncidente"
					+ " AND c.pessoaDestinataria = :pessoa"
					+ " AND c.modeloComunicacao = :modeloComunicacao";

			SQLQuery query = retrieveSession().createSQLQuery(hql.toString());
			query.setEntity("pessoa", pessoa);
			query.setEntity("modeloComunicacao", modeloComunicacao);
			query.setEntity("objetoIncidente", objetoIncidente);
			
			return (Comunicacao) query.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}	
	
public void atualizarSituacaoPecaProcessual(Long idComunicacao) throws DaoException {
		
		Session session = retrieveSession();
		try {
		
		StringBuffer sql = new StringBuffer("UPDATE judiciario.peca_processual " + 
				"   SET seq_tipo_situacao_peca = 3 " + 
				" WHERE seq_peca_processual IN " + 
				"           (SELECT pp.seq_peca_processual " + 
				"              FROM judiciario.documento_comunicacao dc, " + 
				"                   judiciario.comunicacao co, " + 
				"                   judiciario.peca_processual pp " + 
				"             WHERE     dc.seq_comunicacao = co.seq_comunicacao " + 
				"                   AND pp.seq_documento = dc.seq_documento " + 
				"                   AND co.seq_comunicacao = "+ idComunicacao +
				" 					and pp.seq_tipo_situacao_peca = 2 " + 
				"                   AND dc.seq_tipo_situacao_documento IN (3, 4, 7,11))");
	
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
