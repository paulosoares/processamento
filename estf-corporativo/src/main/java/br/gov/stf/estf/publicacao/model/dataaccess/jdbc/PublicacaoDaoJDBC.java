package br.gov.stf.estf.publicacao.model.dataaccess.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.jdbc.Batcher;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoIntegracao;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.localizacao.model.service.AdvogadoService;
import br.gov.stf.estf.processostf.model.service.IntegracaoOrgaoParteService;
import br.gov.stf.estf.publicacao.model.dataaccess.PublicacaoDao;
import br.gov.stf.estf.publicacao.model.util.AdvogadoVO;
import br.gov.stf.estf.publicacao.model.util.PecaVO;
import br.gov.stf.estf.publicacao.model.util.ProcessoProtocoloPublicacaoSearchData;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoVO;
import br.gov.stf.estf.publicacao.model.util.ProtocoloVO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.util.SearchData;
import br.gov.stf.framework.util.SearchResult;

@SuppressWarnings("deprecation")
@Repository
public class PublicacaoDaoJDBC extends GenericHibernateDao<Publicacao, Long>
		implements PublicacaoDao {

	private static final int CODIGO_PECA_INTEIRO_TEOR = 1221;
	private static final String TIPO_ACESSO_INTERNO = "INT";
	private static final String SIGLA_CANCELADO = "CAN";
	private static final int CODIGO_TEXTO_DESPACHO = 1060;
	private static final int CODIGO_TEXTO_DECISAO_MONOCRATICA = 1065;
	private final CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;
	private AdvogadoService advogadoService;
	private IntegracaoOrgaoParteService integracaoOrgaoParteService;

	public PublicacaoDaoJDBC(
			CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService) {
		super(Publicacao.class);
		this.cabecalhoObjetoIncidenteService = cabecalhoObjetoIncidenteService;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5654290542115281701L;

	@SuppressWarnings("unchecked")
	public List<Publicacao> pesquisarProcessoPublicado(String siglaClasse,
			Integer numero, Long codigoRecurso, String tipoJulgamento)
			throws DaoException {
		List<Publicacao> publicacoes = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql
					.append(" SELECT p FROM ProcessoPublicado pp, ConteudoPublicacao cp, Publicacao p ");
			hql.append(" WHERE pp.processo.id.numeroProcessual = ? ");
			hql.append(" AND pp.classeProcessual.id = ? ");
			hql.append(" AND pp.tipoRecurso.id = ? ");
			hql.append(" AND pp.tipoJulgamento.id = ? ");

			hql.append(" AND pp.codigoCapitulo = cp.codigoCapitulo ");
			hql.append(" AND pp.codigoMateria = cp.codigoMateria ");
			hql.append(" AND pp.anoMateria = cp.ano ");
			hql.append(" AND pp.numeroMateria = cp.numero ");

			hql.append(" AND cp.publicacao.id = p.id ");

			hql
					.append(" ORDER BY p.anoEdicaoDje DESC, p.numeroEdicaoDje DESC ");

			Query q = session.createQuery(hql.toString());

			q.setString(0, siglaClasse);
			q.setInteger(1, numero);
			q.setLong(2, codigoRecurso);
			q.setString(3, tipoJulgamento);

			publicacoes = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return publicacoes;
	}

	@SuppressWarnings("unchecked")
	public Short recuperarNumeroUltimoDj() throws DaoException {
		Short numero = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Publicacao.class);
			c.setProjection( Projections.max("numeroEdicaoDje") );
			c.add(Restrictions.eq("anoEdicaoDje", (short)Calendar.getInstance().get(Calendar.YEAR) ));
			
			numero = (Short) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return numero;
	}

	@SuppressWarnings("unchecked")
	public List<Publicacao> pesquisarDjNaoPublicado() throws DaoException {
		List<Publicacao> publicacoes = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Publicacao.class);

			c.add(Restrictions.isNull("dataPublicacaoDj"));
			c.addOrder(Order.asc("anoEdicaoDje"));
			c.addOrder(Order.asc("numeroEdicaoDje"));

			publicacoes = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return publicacoes;
	}

	@Override
	public Publicacao alterar(Publicacao entity) throws DaoException {
		try {
			Session session = retrieveSession();
			session.saveOrUpdate(entity);
			session.flush();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return entity;
	}

	public Publicacao recuperar(Short anoEdicaoDje, Short numeroEdicaoDje)
			throws DaoException {
		Publicacao publicacao = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Publicacao.class);
			c.add(Restrictions.eq("anoEdicaoDje", anoEdicaoDje));
			c.add(Restrictions.eq("numeroEdicaoDje", numeroEdicaoDje));
			publicacao = (Publicacao) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return publicacao;
	}

	public void inserirProcessoIntegracao(int andamentoProcesso, int origem,
			int processo, int seqPeca, int parte, java.sql.Date dataAtual)
			throws DaoException {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into estf.processo_integracao ");
		sql.append(" ( seq_processo_integracao, seq_objeto_incidente, ");
		sql
				.append(" seq_andamento_processo, cod_orgao, tip_situacao, seq_peca_proc_eletronico, cod_parte ) ");
		sql.append(" values (?, ?, ?, ?, ?, ?, ?) ");

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			int seq = getNextSeq("estf.seq_processo_integracao");
			ps.setInt(1, seq);
			ps.setInt(2, processo);
			ps.setInt(3, andamentoProcesso);
			ps.setInt(4, origem);
			ps.setString(5, ProcessoIntegracao.TIPO_SITUACAO_NAO_ENVIADO);
			ps.setInt(6, seqPeca);
			ps.setInt(7, parte);

			ps.executeUpdate();
			ps.close();

			inserirLogProcessoIntegracao(seqPeca, seq, dataAtual);

		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	private void inserirLogProcessoIntegracao(int seqPeca,
			int processoIntegracao, java.sql.Date dataAtual)
			throws HibernateException, SQLException, DaoException {
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO estf.log_processo_integracao ");
		sql
				.append(" (seq_log_processo_integracao, dsc_log_processo_integracao, dat_log_processo_integracao, seq_processo_integracao ) ");
		sql.append(" VALUES (?, ?, ?, ?) ");

		PreparedStatement ps = getSessionImplementor().getBatcher()
				.prepareStatement(sql.toString());
		int seq = getNextSeq("estf.seq_log_processo_integracao");
		ps.setInt(1, seq);
		ps.setString(2, "Inserido pelo programa eSTF-Publicação - Peça "
				+ seqPeca);
		ps.setDate(3, dataAtual);
		ps.setInt(4, processoIntegracao);

		ps.executeUpdate();
		ps.close();

	}

	public void inserirControlePrazoIntimacao(int andamentoProcesso,
			java.sql.Date dataHoje, long idParte) throws DaoException {

		StringBuffer sql = new StringBuffer();
		sql.append(" insert into stf.controle_prazo_intimacao ");
		sql
				.append(" (seq_controle_prazo_intimacao, seq_andamento_processo, dat_intimacao, cod_parte, qtd_prazo) ");
		sql.append(" values (?, ?, ?, ?, ?) ");

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setInt(1, getNextSeq("stf.seq_controle_prazo_intimacao"));
			ps.setInt(2, andamentoProcesso);
			ps.setDate(3, dataHoje);
			ps.setLong(4, idParte);
			ps.setInt(5, 10);

			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	public int recuperarSeqObjetoIncidenteConfirmacao(int objetoIncidente,
			String tipo) throws DaoException {
		if (tipo.equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO.getCodigo())) {
			StringBuffer sql = new StringBuffer();
			sql
					.append(" SELECT oipai.seq_objeto_incidente, oipai.tip_objeto_incidente ");
			sql
					.append("   FROM judiciario.objeto_incidente oi, judiciario.objeto_incidente oipai ");
			sql.append("  WHERE oi.seq_objeto_incidente = ? ");
			sql
					.append("    AND oipai.seq_objeto_incidente = oi.seq_objeto_incidente_pai ");

			try {

				PreparedStatement ps = getSessionImplementor().getBatcher()
						.prepareStatement(sql.toString());
				ps.setInt(1, objetoIncidente);

				ResultSet rs = ps.executeQuery();

				int novoObjetoIncidente = -1;
				String novoTipo = null;

				if (rs.next()) {
					novoObjetoIncidente = rs.getInt("seq_objeto_incidente");
					novoTipo = rs.getString("tip_objeto_incidente");
				}

				rs.close();
				ps.close();
				return recuperarSeqObjetoIncidenteConfirmacao(
						novoObjetoIncidente, novoTipo);
			} catch (SQLException e) {
				throw new DaoException(e);
			}

		}

		return objetoIncidente;

	}

	private SessionImplementor getSessionImplementor() throws DaoException {
		return (SessionImplementor) retrieveSession();
	}

	public void alterarSituacaoPecaInteiroTeor(int objetoIncidente,
			java.sql.Date dataPublicacao, Long idProcessoPublicado) throws DaoException {
		try {
			DaoQueryResult daoQueryResult = consultaPecasDeInteiroTeorParaAlteracaoDaSituacao(objetoIncidente);
			
			ResultSet rs = daoQueryResult.rs;
			PreparedStatement ps = daoQueryResult.ps;
			while (rs.next()) {
				juntaEPublicaPecaEletronica(rs);
				inserirProcessoImg(objetoIncidente, dataPublicacao, rs.getInt("seq_documento"), idProcessoPublicado);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	private DaoQueryResult consultaPecasDeInteiroTeorParaAlteracaoDaSituacao(
			int objetoIncidente) throws SQLException, DaoException {
		StringBuffer sql = montaSQLPecaInteiroTeor();
		PreparedStatement ps = getSessionImplementor().getBatcher()
				.prepareStatement(sql.toString());
		ps.setInt(1, objetoIncidente);
		ps.setLong(2, TipoPecaProcesso.CODIGO_INTEIRO_TEOR);
		ps.setLong(3, TipoSituacaoPeca.PENDENTE.getCodigo());
		ResultSet rs = ps.executeQuery();
		
		DaoQueryResult daoQueryResult = new DaoQueryResult();
		daoQueryResult.rs = rs;
		daoQueryResult.ps = ps;
		
		//ps.close();
		return daoQueryResult;
	}

	private StringBuffer montaSQLPecaInteiroTeor() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ppe.seq_peca_proc_eletronico, ape.seq_documento ");
		sql
				.append(" FROM stf.peca_processo_eletronico ppe, stf.arquivo_processo_eletronico ape ");
		sql.append(" WHERE ppe.seq_objeto_incidente_completo = ? ");
		sql.append(" AND ppe.seq_tipo_peca = ? ");
		sql.append(" AND ppe.seq_tipo_situacao_peca = ? ");
		sql
				.append(" AND ppe.seq_peca_proc_eletronico = ape.seq_peca_proc_eletronico ");
		return sql;
	}

	private void juntaEPublicaPecaEletronica(ResultSet rs) throws SQLException, DaoException {
		StringBuffer sqlUpdatePeca = montaSQLDeAlteracaoDeSituacaoDaPeca();
		PreparedStatement psUp = getSessionImplementor().getBatcher()
				.prepareStatement(sqlUpdatePeca.toString());
		psUp.setLong(1, TipoSituacaoPeca.JUNTADA.getCodigo());
		psUp.setInt(2, rs.getInt("seq_peca_proc_eletronico"));
		psUp.executeUpdate();
		psUp.close();
		alterarDocumentoPublico(rs.getInt("seq_documento"));
	}

	private void inserirProcessoImg(int objetoIncidente, Date dataPublicacao,
			int seqDocumento, Long idProcessoPublicado) throws SQLException, HibernateException,
			DaoException {

		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert
				.append(" insert into stf.processos_img (num_ementa, num_tomo, seq_objeto_incidente, dat_publicacao, tip_colecao, flg_liberado, seq_documento, seq_processo_publicados) ");
		sqlInsert
				.append(" values ( 9999, 0, "
						+ objetoIncidente + ", to_date('"
						+ DateTimeHelper.getDataString(dataPublicacao)
						+ "','dd/mm/yyyy'), 'ELETRONICO', 'S', " + seqDocumento
						+ ", " + idProcessoPublicado
						+ " ) ");

		PreparedStatement psInsert = getSessionImplementor().getBatcher()
				.prepareStatement(sqlInsert.toString());
		psInsert.executeUpdate();
		psInsert.close();

	}

	public void alterarTipoAcessoDocumento(ProcessoPublicadoVO processoPublicado)
			throws DaoException {
		try {
			DaoQueryResult daoQueryResult = consultaDocumentosParaAlterarTipoDeAcesso(processoPublicado);
			
			ResultSet resultado = daoQueryResult.rs;
			PreparedStatement ps = daoQueryResult.ps;
			while (resultado.next()) {
				alterarDocumentoPublico(resultado.getInt("seq_documento"));
			}
			
			resultado.close();
			ps.close();
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	private DaoQueryResult consultaDocumentosParaAlterarTipoDeAcesso(
			ProcessoPublicadoVO processoPublicado) throws DaoException, SQLException {
		String sql = getSqlDocumentosParaTornarPublico();
		SessionImplementor session = (SessionImplementor) retrieveSession();
		PreparedStatement ps = session.getBatcher().prepareSelectStatement(sql);
		
		adicionaParametrosDePesquisa(processoPublicado, ps);
		ResultSet resultado = ps.executeQuery();
		
		DaoQueryResult daoQueryResult = new DaoQueryResult();
		daoQueryResult.rs = resultado;
		daoQueryResult.ps = ps;
		
		return daoQueryResult;
	}

	private void adicionaParametrosDePesquisa(
			ProcessoPublicadoVO processoPublicado, PreparedStatement ps)
			throws SQLException {
		ps.setInt(1, processoPublicado.getNumeroMateria());
		ps.setInt(2, processoPublicado.getAnoMateria());
		ps.setLong(3, processoPublicado.getObjetoIncidente());
		ps.setString(4, TIPO_ACESSO_INTERNO);
		ps.setString(5, SIGLA_CANCELADO);
		ps.setInt(6, CODIGO_TEXTO_DESPACHO);
		ps.setInt(7, CODIGO_TEXTO_DECISAO_MONOCRATICA);
	}

	private String getSqlDocumentosParaTornarPublico() {
		return "SELECT DISTINCT vd.seq_documento"
				+ " FROM stf.processo_publicados pp INNER JOIN judiciario.objeto_incidente oipp"
				+ " ON oipp.seq_objeto_incidente = pp.seq_objeto_incidente"
				+ " INNER JOIN judiciario.objeto_incidente oippe"
				+ " ON oippe.seq_objeto_incidente_principal = oipp.seq_objeto_incidente_principal"
				+ " INNER JOIN stf.peca_processo_eletronico ppe"
				+ " ON ppe.seq_objeto_incidente = oippe.seq_objeto_incidente_principal"
				+ " INNER JOIN stf.arquivo_processo_eletronico ape"
				+ " ON ape.seq_peca_proc_eletronico = ppe.seq_peca_proc_eletronico"
				+ " INNER JOIN doc.vw_documento vd ON vd.seq_documento = ape.seq_documento"
				+ " WHERE pp.num_materia = ?" + " AND pp.ano_materia = ?"
				+ " AND pp.seq_objeto_incidente = ?" + " AND vd.tip_acesso = ?"
				+ " AND vd.dsc_status_documento <> ?"
				+ " AND ppe.seq_tipo_peca IN (?, ?)";
	}

	public void alterarSituacaoPecaEletronica(int objetoIncidente,
			int seqArquivoEletronico) throws DaoException {
		try {
			DaoQueryResult daoQueryResult = consultaPecasParaAlteracaoDaSituacao(
					objetoIncidente, seqArquivoEletronico);
			
			ResultSet rs = daoQueryResult.rs;
			PreparedStatement ps = daoQueryResult.ps;
			while (rs.next()) {
				juntaEPublicaPecaEletronica(rs);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	private DaoQueryResult consultaPecasParaAlteracaoDaSituacao(int objetoIncidente,
			int seqArquivoEletronico) throws SQLException, DaoException {
		StringBuffer sql = montaSQLConsultaDePecaEletronica();
		PreparedStatement ps = getSessionImplementor().getBatcher()
				.prepareStatement(sql.toString());
		ps.setInt(1, objetoIncidente);
		ps.setInt(2, seqArquivoEletronico);
		ps
				.setString(3,
						DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO);
		ResultSet rs = ps.executeQuery();
		//ps.close();
		DaoQueryResult daoQueryResult = new DaoQueryResult();
		daoQueryResult.ps = ps;
		daoQueryResult.rs = rs;
		
		return daoQueryResult;
	}

	private static class DaoQueryResult {
		ResultSet rs = null;
		PreparedStatement ps = null;
	}
	
	private StringBuffer montaSQLConsultaDePecaEletronica() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ppe.seq_peca_proc_eletronico, doc.seq_documento ");
		sql.append(" FROM stf.textos t, ");
		sql.append(" stf.documento_texto dt, ");
		sql.append(" stf.arquivo_processo_eletronico ape, ");
		sql.append(" stf.peca_processo_eletronico ppe, ");
		sql.append(" doc.vw_documento doc ");
		sql.append(" WHERE t.seq_objeto_incidente = ? ");
		sql.append(" AND t.seq_arquivo_eletronico = ? ");
		sql.append(" AND doc.dsc_status_documento = ? ");
		sql.append(" AND t.seq_textos = dt.seq_textos ");
		sql.append(" AND dt.seq_documento = doc.seq_documento ");
		sql.append(" AND dt.seq_documento = ape.seq_documento ");
		sql
				.append(" AND ape.seq_peca_proc_eletronico = ppe.seq_peca_proc_eletronico ");
		return sql;
	}

	private StringBuffer montaSQLDeAlteracaoDeSituacaoDaPeca() {
		StringBuffer sqlUpdatePeca = new StringBuffer();
		sqlUpdatePeca.append(" UPDATE stf.peca_processo_eletronico ");
		sqlUpdatePeca.append(" SET seq_tipo_situacao_peca = ? ");
		sqlUpdatePeca.append(" WHERE seq_peca_proc_eletronico = ? ");
		return sqlUpdatePeca;
	}

	private void alterarDocumentoPublico(int seqDocumento) throws DaoException,
			HibernateException, SQLException {
		CallableStatement csSelect = getSessionImplementor().getBatcher()
				.prepareCallableStatement(
						"{call DOC.PKG_DOCUMENTO_CAS.prc_muda_tipo_acesso(?,?)}");
		csSelect.setInt(1, seqDocumento);
		csSelect.setString(2, TipoDeAcessoDoDocumento.PUBLICO.getChave());
		csSelect.execute();
		csSelect.close();
	}

	public List<ProtocoloVO> pesquisarProtocolosRepublicadosConfirmacao(
			Long seqDataPublicacao) throws DaoException {
		StringBuffer sql = new StringBuffer();

		sql
				.append(" SELECT pp.seq_objeto_incidente FROM stf.protocolo_publicado pp, stf.materias m, stf.data_publicacoes dp ");
		sql.append(" WHERE pp.seq_materias = m.seq_materias ");
		sql
				.append(" AND m.cod_capitulo = 6 AND m.cod_materia = 13 AND m.cod_conteudo = 50 ");
		sql.append(" AND m.seq_data_publicacoes = dp.seq_data_publicacoes ");
		sql.append(" AND dp.seq_data_publicacoes = ? ");

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setLong(1, seqDataPublicacao);

			ResultSet rs = ps.executeQuery();
			List<ProtocoloVO> protocolos = new ArrayList<ProtocoloVO>();
			while (rs.next()) {
				protocolos.add(new ProtocoloVO(rs));
			}
			ps.close();
			rs.close();

			return protocolos;

		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	public void inserirAndamentoProtocolo(int codigoAndamento,
			java.sql.Date dataAndamento, java.sql.Date dataHoraSistema,
			int numeroSequencia, int objetoIncidente, String siglaUsuario,
			long codigoSetor, String descricaoObservacaoProtocolo)
			throws DaoException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" insert into stf.andamento_protocolo (cod_andamento, dat_andamento, seq_objeto_incidente, dat_hora_sistema, dsc_obser_and, num_sequencia, cod_setor, sig_usuario, flg_valido, seq_andamento_protocolo) ");
		sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setInt(1, codigoAndamento);
			ps.setDate(2, dataAndamento);
			ps.setInt(3, objetoIncidente);
			ps.setDate(4, dataHoraSistema);
			ps.setString(5, descricaoObservacaoProtocolo);
			ps.setInt(6, numeroSequencia);
			ps.setLong(7, codigoSetor);
			ps.setString(8, siglaUsuario.toUpperCase());
			ps.setBoolean(9, false);
			int seqAndamentoProtocolo = getNextSeq("stf.seq_andamento_protocolo");
			ps.setInt(10, seqAndamentoProtocolo);

			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	public int recuperarProximoNumeroSequenciaProtocolo(int objetoIncidente)
			throws DaoException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT MAX (ap.num_sequencia) AS num_sequencia ");
		sql.append(" FROM stf.andamento_protocolo ap ");
		sql.append(" WHERE ap.seq_objeto_incidente = ? ");

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());

			ps.setInt(1, objetoIncidente);

			ResultSet rs = ps.executeQuery();

			int numSequencia = 0;
			if (rs.next()) {
				numSequencia = rs.getInt("num_sequencia");
			}

			ps.close();
			rs.close();
			return ++numSequencia;

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public List<ProtocoloVO> pesquisarProtocolosConfirmacao(
			Long seqDataPublicacao) throws DaoException {

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tp.seq_objeto_incidente FROM stf.texto_peticaos tp ");
		sql.append(" INNER JOIN judiciario.objeto_incidente oipt ON oipt.seq_objeto_incidente = tp.seq_objeto_incidente,  ");
		sql.append("    stf.materias m, stf.data_publicacoes dp ");
		sql.append(" WHERE oipt.tip_objeto_incidente = '" + TipoObjetoIncidente.PROTOCOLO + "' ");
		sql.append(" AND tp.num_materia = m.num_materia ");
		sql.append(" AND tp.ano_materia = m.ano_materia ");
		sql.append(" AND m.cod_capitulo = 6 AND m.cod_materia = 12 AND m.cod_conteudo = 50 ");
		sql.append(" AND m.seq_data_publicacoes = dp.seq_data_publicacoes ");
		sql.append(" AND dp.seq_data_publicacoes = ? ");
		

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setLong(1, seqDataPublicacao);

			ResultSet rs = ps.executeQuery();
			List<ProtocoloVO> protocolos = new ArrayList<ProtocoloVO>();
			while (rs.next()) {
				protocolos.add(new ProtocoloVO(rs));
			}
			ps.close();
			rs.close();
			return protocolos;

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public void inserirTextoAndamento(int objetoIncidente,
			int seqArquivoEletronico, int seqAndamentoProcesso)
			throws DaoException {

		try {
			List<Integer> textos = pesquisarTextos(objetoIncidente,
					seqArquivoEletronico);
			inserirTextoAndamento(textos, seqAndamentoProcesso);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public void inserirTextoAndamentoAcordao(int objetoIncidente,
			int seqAndamentoProcesso) throws DaoException {
		try {

			List<Integer> textos = pesquisarTextosAcordao(objetoIncidente);
			inserirTextoAndamento(textos, seqAndamentoProcesso);

		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	private void inserirTextoAndamento(List<Integer> textos,
			int seqAndamentoProcesso) throws SQLException, HibernateException,
			DaoException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" INSERT INTO stf.textos_andamento_processos (seq_textos_andamento_processos, seq_textos, seq_andamento_processo ) ");
		sql.append(" VALUES (?, ?, ?) ");
		PreparedStatement ps = getSessionImplementor().getBatcher()
				.prepareStatement(sql.toString());
		ps.setInt(3, seqAndamentoProcesso);
		for (Integer seqTextos : textos) {
			int seqTextoAndamentoProcesso = getNextSeq("stf.seq_textos_andamento_processos");
			ps.setInt(1, seqTextoAndamentoProcesso);
			ps.setInt(2, seqTextos);
			ps.executeUpdate();
		}
		ps.close();
	}

	private List<Integer> pesquisarTextos(int objetoIncidente,
			int seqArquivoEletronico) throws DaoException, HibernateException,
			SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select seq_textos  ");
		sql.append(" from stf.textos ");
		sql.append(" where seq_objeto_incidente = ? ");
		sql.append(" and seq_arquivo_eletronico = ? ");
		sql.append(" and flg_publico = 'S' ");

		PreparedStatement ps = getSessionImplementor().getBatcher()
				.prepareStatement(sql.toString());
		ps.setInt(1, objetoIncidente);
		ps.setInt(2, seqArquivoEletronico);

		ResultSet rs = ps.executeQuery();
		List<Integer> textos = new ArrayList<Integer>();
		while (rs.next()) {
			textos.add(rs.getInt("seq_textos"));
		}

		ps.close();
		rs.close();

		return textos;
	}

	private List<Integer> pesquisarTextosAcordao(int objetoIncidente)
			throws DaoException, HibernateException, SQLException {

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT   t.seq_textos ");
		sql.append(" FROM stf.textos t ");
		sql.append(" WHERE t.seq_objeto_incidente = ? ");
		sql.append(" AND t.cod_tipo_texto IN (50, 80) ");
		sql.append(" AND t.flg_publico = 'S' ");
		sql.append(" AND t.dat_sessao = ");
		sql.append(" (SELECT MAX (tt.dat_sessao) ");
		sql.append(" FROM stf.textos tt ");
		sql.append(" WHERE tt.seq_objeto_incidente = t.seq_objeto_incidente ");
		sql.append(" AND tt.cod_tipo_texto = t.cod_tipo_texto ) ");
		sql.append(" ORDER BY t.cod_tipo_texto ");

		PreparedStatement ps = getSessionImplementor().getBatcher()
				.prepareStatement(sql.toString());
		ps.setInt(1, objetoIncidente);

		ResultSet rs = ps.executeQuery();

		List<Integer> textos = new ArrayList<Integer>();
		while (rs.next()) {
			textos.add(rs.getInt("seq_textos"));
		}

		ps.close();
		rs.close();

		return textos;
	}

	public void alterarDeslocamentoProcessoEletronico(int objetoIncidente,
			long codigoSetor) {
		// TODO DESLOCA O PROCESSO ELETRÔNICO

	}

	public int inserirAndamentoProcesso(int codigoAndamento,
			String siglaUsuario, java.sql.Date dataAndamento,
			java.sql.Date dataHoraSistema, String descricaoObservacao,
			int numeroSequencia, int objetoIncidente, long codigoSetor)
			throws DaoException {

		StringBuffer sql = new StringBuffer();
		sql
				.append(" insert into stf.andamento_processos (cod_andamento, dat_andamento, seq_objeto_incidente, dat_hora_sistema, dsc_obser_and, num_sequencia, cod_setor, cod_usuario, seq_andamento_processo ) ");
		sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ? ) ");

		int seqAndamentoProcesso = -1;

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setInt(1, codigoAndamento);
			ps.setDate(2, dataAndamento);
			ps.setInt(3, objetoIncidente);
			ps.setDate(4, dataHoraSistema);
			ps.setString(5, descricaoObservacao);
			ps.setInt(6, numeroSequencia);
			ps.setLong(7, codigoSetor);
			ps.setString(8, siglaUsuario.toUpperCase());
			seqAndamentoProcesso = getNextSeq("stf.seq_andamento_processo");
			ps.setInt(9, seqAndamentoProcesso);

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new DaoException("Erro ao inserir andamento "
					+ codigoAndamento + " para o objeto " + objetoIncidente
					+ " com sequencia " + numeroSequencia, e);
		}

		return seqAndamentoProcesso;

	}

	private int getNextSeq(String nomeSeq) throws SQLException,
			HibernateException, DaoException {
		StringBuffer sql = new StringBuffer(" select " + nomeSeq
				+ ".NEXTVAL from dual ");
		PreparedStatement stm = getSessionImplementor().getBatcher()
				.prepareStatement(sql.toString());
		ResultSet rs = stm.executeQuery();
		int nextVal = 0;
		if (rs.next()) {
			nextVal = rs.getInt("NEXTVAL");
		}
		stm.close();
		rs.close();
		return nextVal;
	}

	public int recuperarProximoNumeroSequenciaProcesso(int objetoIncidente)
			throws DaoException {
		StringBuffer sql = new StringBuffer();
		// sql.append(" SELECT MAX (ap.num_sequencia) AS num_sequencia ");
		// sql.append(" FROM stf.andamento_processos ap, judiciario.objeto_incidente oiap, judiciario.objeto_incidente oi ");
		// sql.append(" WHERE ap.seq_objeto_incidente = oiap.seq_objeto_incidente ");
		// sql.append(" AND oiap.seq_objeto_incidente_principal = oi.seq_objeto_incidente_principal ");
		// sql.append(" AND oi.seq_objeto_incidente = ? ");

		sql.append(" SELECT MAX (ap.num_sequencia) AS num_sequencia ");
		sql.append("   FROM stf.andamento_processos ap, ");
		sql.append("        judiciario.processo p, ");
		sql.append("        judiciario.objeto_incidente oi ");
		sql.append("  WHERE ap.num_processo = p.num_processo ");
		sql.append("    AND ap.sig_classe_proces = p.sig_classe_proces ");
		sql
				.append("    AND p.seq_objeto_incidente = oi.seq_objeto_incidente_principal ");
		sql.append("    AND oi.seq_objeto_incidente = ? ");

		try {

			SessionImplementor sessionImpl = getSessionImplementor();
			Batcher batcher = sessionImpl.getBatcher();
			//batcher.closeStatements()
			
			PreparedStatement ps = batcher.prepareStatement(sql.toString());
			ps.setInt(1, objetoIncidente);

			ResultSet rs = ps.executeQuery();

			int numSequencia = 0;
			if (rs.next()) {
				numSequencia = rs.getInt("num_sequencia");
			}

			//ps.close();
			rs.close();
			batcher.closeStatement(ps);
			return ++numSequencia;

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public List<ProcessoPublicadoVO> pesquisarProcessosConfirmacao(
			Long seqDataPublicacao) throws DaoException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT pp.cod_materia, pp.cod_capitulo, pp.num_materia, ");
		sql.append(" pp.ano_materia, pp.seq_processo_publicados, oi.seq_objeto_incidente, ");
		sql.append(" m.dat_criacao, ");
		sql.append(" p.tip_meio_processo, oi.tip_objeto_incidente, dp.num_edicao_dje, dp.dat_divulgacao_dje, pp.seq_arquivo_eletronico_texto ");
		sql.append(" FROM stf.processo_publicados pp ");
		sql.append(" join judiciario.objeto_incidente oi on oi.seq_objeto_incidente = pp.seq_objeto_incidente ");
		sql.append(" join judiciario.processo p on p.seq_objeto_incidente = oi.seq_objeto_incidente_principal ");
		sql.append(" join stf.materias m on  ");
		sql.append(" m.cod_capitulo = pp.cod_capitulo ");
		sql.append(" AND m.cod_materia = pp.cod_materia ");
		sql.append(" AND m.num_materia = pp.num_materia ");
		sql.append(" AND m.ano_materia = pp.ano_materia ");
		sql.append(" AND m.cod_conteudo = ? ");
		sql.append(" join stf.data_publicacoes dp on  ");
		sql.append(" dp.seq_data_publicacoes = m.seq_data_publicacoes ");
		sql.append(" WHERE dp.seq_data_publicacoes = ? ");

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher().prepareStatement(sql.toString());
			ps.setInt(1, 50);
			ps.setLong(2, seqDataPublicacao);

			ResultSet rs = ps.executeQuery();

			List<ProcessoPublicadoVO> processos = new ArrayList<ProcessoPublicadoVO>();
			while (rs.next()) {
				processos.add(new ProcessoPublicadoVO(rs));
			}

			ps.close();
			rs.close();

			return processos;

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public void atualizarDataPublicacao(long seqDataPublicacao, int numeroDJ)
			throws DaoException {

		StringBuffer sql = new StringBuffer();
		sql
				.append(" update stf.data_publicacoes set dat_publicacao_dj = ? , num_dj = ? ");
		sql.append(" where seq_data_publicacoes = ? ");

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
			ps.setInt(2, numeroDJ);
			ps.setLong(3, seqDataPublicacao);

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	public SearchResult pesquisarProcessoProtocoloPublicacao(
			ProcessoProtocoloPublicacaoSearchData sd) throws DaoException {
		Session session = retrieveSession();
		try {

			if (sd != null) {

				StringBuffer hql = new StringBuffer(" FROM ");
				if (sd.getRetornoProcessoPublicado()) {
					hql.append(" ProcessoPublicado p");
					hql.append(" JOIN FETCH p.conteudosPublicacao cp ");
					if (sd.getPossuiInformacaoProcessual()) {
						hql.append(" ,Processo pr ");
						hql.append(" WHERE pr.id = p.objetoIncidente.id AND ");
					} else {
						hql.append(" WHERE ");
					}
				} else {
					hql.append(" ProtocoloPublicado p");
					hql.append(" JOIN p.conteudoPublicacao cp "
							+ " JOIN p.protocolo pr");
					hql.append(" WHERE ");
				}
				hql.append("     cp.codigoCapitulo = :codigoCapitulo "
						+ " AND cp.codigoConteudo = :codigoConteudo ");

				if (sd.listaCodigoMateria != null
						&& sd.listaCodigoMateria.size() > 0)
					hql
							.append(" AND cp.codigoMateria in "
									+ SearchData.inClause(
											sd.listaCodigoMateria, false));

				if (sd.numeroMateria != null)
					hql.append(" AND cp.numero = :numeroMateria");

				if (sd.anoMateria != null)
					hql.append(" AND cp.ano = :anoMateria");

				if (sd.getRetornoProcessoPublicado()) {

					if (SearchData.stringNotEmpty(sd.siglaClasseProcessual))
						hql
								.append(" AND pr.siglaClasseProcessual = :siglaClasseProcessual");

					if (sd.numeroProcesso != null)
						hql
								.append(" AND pr.numeroProcessual = :numeroProcesso");

				} else {

					if (sd.numeroProtocolo != null)
						hql
								.append(" AND pr.numeroProtocolo = :numeroProtocolo");

					if (sd.anoProtocolo != null)
						hql.append(" AND pr.anoProtocolo = :anoProtocolo");

				}

				// ########################## PARÂMETROS HQL
				// ########################## //

				Query collectionQuery = session.createQuery(" SELECT p "
						+ hql.toString());
				Query countQuery = session.createQuery(" SELECT COUNT(p.id)"
						+ hql.toString().replace(
								"JOIN FETCH p.conteudosPublicacao cp",
								"JOIN p.conteudosPublicacao cp"));

				if (sd.codigoCapitulo != null) {
					countQuery
							.setParameter("codigoCapitulo", sd.codigoCapitulo);
					collectionQuery.setParameter("codigoCapitulo",
							sd.codigoCapitulo);
				}

				if (sd.codigoConteudo != null) {
					countQuery
							.setParameter("codigoConteudo", sd.codigoConteudo);
					collectionQuery.setParameter("codigoConteudo",
							sd.codigoConteudo);
				}

				if (sd.numeroMateria != null) {
					countQuery.setParameter("numeroMateria", sd.numeroMateria);
					collectionQuery.setParameter("numeroMateria",
							sd.numeroMateria);
				}

				if (sd.anoMateria != null) {
					countQuery.setParameter("anoMateria", sd.anoMateria);
					collectionQuery.setParameter("anoMateria", sd.anoMateria);
				}

				if (SearchData.stringNotEmpty(sd.siglaClasseProcessual)) {
					countQuery.setParameter("siglaClasseProcessual",
							sd.siglaClasseProcessual);
					collectionQuery.setParameter("siglaClasseProcessual",
							sd.siglaClasseProcessual);
				}

				if (sd.numeroProcesso != null) {
					countQuery
							.setParameter("numeroProcesso", sd.numeroProcesso);
					collectionQuery.setParameter("numeroProcesso",
							sd.numeroProcesso);
				}

				if (sd.numeroProtocolo != null) {
					countQuery.setParameter("numeroProtocolo",
							sd.numeroProtocolo);
					collectionQuery.setParameter("numeroProtocolo",
							sd.numeroProtocolo);
				}

				if (sd.anoProtocolo != null) {
					countQuery.setParameter("anoProtocolo", sd.anoProtocolo);
					collectionQuery.setParameter("anoProtocolo",
							sd.anoProtocolo);
				}

				Long totalSize = 0L;
				if (sd.getTotalResult() != null && sd.getTotalResult() > 0L) {
					totalSize = sd.getTotalResult();
				} else {
					totalSize = (Long) countQuery.uniqueResult();
				}
				List<Processo> result = null;

				if (totalSize > 0) {

					if (totalSize > sd.getPageData().getPageMaxResult()
							&& sd.isPaging()) {
						collectionQuery.setMaxResults(sd.getPageData()
								.getPageMaxResult());
						collectionQuery.setFirstResult(sd.getPageData()
								.getFirstResult());
					}

					result = collectionQuery.list();
				}

				SearchResult sr = new SearchResult(sd, totalSize, result);
				return sr;
			}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils
					.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return null;
	}

	public List<AdvogadoVO> pesquisarAdvogadosIntimaveisDJ(long seqDJ)
			throws DaoException {
		StringBuffer sql = new StringBuffer();

		sql
				.append(" SELECT ji.seq_jurisdicionado, ji.nom_jurisdicionado, ji.seq_objeto_incidente, ");
		sql.append("        iop.cod_origem, m.dat_composicao_parcial, ");
		sql
				.append("        oi.seq_objeto_incidente_principal, m.cod_capitulo, oi.tip_objeto_incidente ");
		sql
				.append("   FROM judiciario.vw_jurisdicionado_incidente ji INNER JOIN stf.processo_publicados pp ");
		sql
				.append("        ON ji.seq_objeto_incidente = pp.seq_objeto_incidente ");
		sql.append("        INNER JOIN judiciario.objeto_incidente oi ");
		sql
				.append("        ON ji.seq_objeto_incidente = oi.seq_objeto_incidente ");
		sql.append("        INNER JOIN judiciario.vw_processo_relator pr ");
		sql
				.append("        ON oi.seq_objeto_incidente_principal = pr.seq_objeto_incidente ");
		sql.append("        LEFT JOIN estf.integracao_orgao_parte iop ");
		sql.append("        ON iop.cod_parte = ji.seq_jurisdicionado ");
		sql.append("        INNER JOIN stf.materias m ");
		sql.append("        ON pp.cod_capitulo = m.cod_capitulo ");
		sql.append("      AND pp.cod_materia = m.cod_materia ");
		sql.append("      AND pp.ano_materia = m.ano_materia ");
		sql.append("      AND pp.num_materia = m.num_materia ");
		sql.append("      AND m.cod_conteudo = 50 ");
		sql.append("        INNER JOIN stf.data_publicacoes dp ");
		sql
				.append("        ON m.seq_data_publicacoes = dp.seq_data_publicacoes ");
		sql.append("  WHERE dp.seq_data_publicacoes = ? ");
		
		// Linha comentada a pedido do Rafael Rabelo da Seção de Sistemas do Processamento Judiciário.
		//sql.append("    AND ji.cod_categoria = ? ");
		sql.append("    AND pr.cod_relator_atual IS NOT NULL ");
		sql.append("    AND ji.tip_meio_intimacao = ? ");

		List<AdvogadoVO> advogados = new ArrayList<AdvogadoVO>();

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setLong(1, seqDJ);
			//ps.setInt(2, 202); //202
			ps.setString(2, "E");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				advogados.add(new AdvogadoVO(rs));
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}

		return advogados;
	}

	public void alterarSituacaoPecaJuntada(int seqPeca) throws DaoException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" Update stf.peca_processo_eletronico set SEQ_TIPO_SITUACAO_PECA = ? ");
		sql.append(" where SEQ_PECA_PROC_ELETRONICO = ? ");

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setInt(1, 3);
			ps.setInt(2, seqPeca);

			ps.executeUpdate();

			ps.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	public List<PecaVO> pesquisarPecas(int objetoIncidente,
			java.sql.Date dataComposicao) throws DaoException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" SELECT DISTINCT pe.seq_peca_proc_eletronico, pe.seq_tipo_situacao_peca ");
		sql
				.append("            FROM stf.processo_publicados pp INNER JOIN stf.materias m ");
		sql
				.append("                 ON (    m.cod_capitulo = pp.cod_capitulo ");
		sql.append("                     AND m.cod_materia = pp.cod_materia ");
		sql.append("                     AND m.num_materia = pp.num_materia ");
		sql.append("                     AND m.ano_materia = pp.ano_materia ");
		sql.append("                    ) ");
		sql.append("                 INNER JOIN stf.textos t ");
		sql
				.append("                 ON (    t.seq_objeto_incidente = pp.seq_objeto_incidente ");
		sql.append("                     AND t.seq_arquivo_eletronico = ");
		sql
				.append("                                                pp.seq_arquivo_eletronico_texto ");
		sql
				.append("                     AND t.dat_composicao_parcial = m.dat_composicao_parcial ");
		sql.append("                    ) ");
		sql
				.append("                 INNER JOIN stf.documento_texto dt ON dt.seq_textos = ");
		sql
				.append("                                                                   t.seq_textos ");
		sql
				.append("                 LEFT JOIN stf.arquivo_processo_eletronico ae ");
		sql.append("                 ON ae.seq_documento = dt.seq_documento ");
		sql
				.append("                 LEFT JOIN stf.peca_processo_eletronico pe ");
		sql
				.append("                 ON pe.seq_peca_proc_eletronico = ae.seq_peca_proc_eletronico ");
		sql.append("           WHERE dt.seq_tipo_situacao_documento = 3 ");
		sql.append("             AND m.cod_conteudo = 50 ");
		sql.append("             AND pe.seq_tipo_situacao_peca IN (2, 3) ");
		sql.append("             AND (   (    m.cod_capitulo = 5 ");
		sql.append("                      AND m.cod_materia IN (1, 2, 3) ");
		sql
				.append("                      AND dt.cod_tipo_documento_texto = 50 ");
		sql.append("                     ) ");
		sql
				.append("                  OR (m.cod_capitulo = 6 AND m.cod_materia IN (2, 3, 7, 10)) ");
		sql.append("                 ) ");
		sql.append("             AND pp.seq_objeto_incidente = ? ");
		sql.append("             AND m.dat_composicao_parcial = ? ");

		List<PecaVO> pecas = new ArrayList<PecaVO>();

		try {

			PreparedStatement ps = getSessionImplementor().getBatcher()
					.prepareStatement(sql.toString());
			ps.setInt(1, objetoIncidente);
			ps.setDate(2, dataComposicao);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				pecas.add(new PecaVO(rs));
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new DaoException(e);
		}

		return pecas;

	}

	@Override
	public void refresh(Publicacao publicacao) throws DaoException {
		retrieveSession().refresh(publicacao);
	}
}
