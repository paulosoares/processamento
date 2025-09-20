package br.gov.stf.estf.repercussaogeral.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia.TipoOcorrenciaConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso.TipoSituacaoVoto;
import br.gov.stf.estf.entidade.localizacao.ConfiguracaoAndamentoSetor.ConstantSigAndamentoSetor;
import br.gov.stf.estf.entidade.ministro.AfastamentoMinistroView;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.processostf.model.util.ConstanteAndamento;
import br.gov.stf.estf.processostf.model.util.ConstanteGrupoAndamento;
import br.gov.stf.estf.repercussaogeral.model.dataaccess.RepercussaoGeralDao;
import br.gov.stf.estf.repercussaogeral.model.util.RepercussaoGeralSearchData;
import br.gov.stf.estf.repercussaogeral.model.util.RepercussaoGeralSearchData.AndamentoRG;
import br.gov.stf.estf.repercussaogeral.model.util.RepercussaoGeralSearchData.Situacao;
import br.gov.stf.estf.repercussaogeral.model.util.RepercussaoGeralSearchData.SituacaoRG;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.BaseEntity;
import br.gov.stf.framework.model.entity.Flag;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.util.SearchData;
import br.gov.stf.framework.util.SearchResult;

@SuppressWarnings("unchecked")
@Repository
public class RepercussaoGeralDaoHibernate extends
		GenericHibernateDao<BaseEntity, Long> implements RepercussaoGeralDao {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3496550252746578283L;

	public RepercussaoGeralDaoHibernate() {
		super(BaseEntity.class);
	}

	@SuppressWarnings("null")
	public SearchResult pesquisarRepercussaoGeralSQL(
			RepercussaoGeralSearchData searchData, String ordem)
			throws DaoException {

		Session session = retrieveSession();

		try {

			if (searchData != null) {

				StringBuffer sql = new StringBuffer();
				StringBuffer sqlCampos = new StringBuffer();
				StringBuffer sqlCount = new StringBuffer();
				StringBuffer sqlOrder = new StringBuffer();

				sql.append(" FROM julgamento.processo_tema pt JOIN judiciario.tema t ON pt.seq_tema = t.seq_tema ");
				sql.append(" JOIN julgamento.tipo_ocorrencia toc ON pt.cod_tipo_ocorrencia = toc.cod_tipo_ocorrencia ");
				sql.append(" JOIN judiciario.vw_processo_relator vw ON vw.sig_classe_proces = pt.sig_classe_proces ");
				sql.append(" AND vw.num_processo = pt.num_processo ");

				if (searchData.suspensaoNacional){
					sql.append(" INNER JOIN judiciario.suspensao_nacional_tema spt on spt.seq_tema = t.seq_tema ");
					sql.append(" AND spt.dat_final_suspensao is null ");
					sql.append(" INNER JOIN stf.andamento_processos ap on ap.seq_andamento_processo = spt.seq_andamento_processo ");
					sql.append(" AND ap.flg_LANCAMENTO_INDEVIDO = 'N' ");
				}
				if (searchData.getPesquisaAssunto()) {
					sql.append(" JOIN stf.assunto_processo ap ON ap.seq_objeto_incidente = vw.seq_objeto_incidente ");

					if (searchData.codigoAssunto != null)
						sql.append(" AND ap.cod_assunto = "
								+ searchData.codigoAssunto);

					if (SearchData.stringNotEmpty(searchData.descricaoAssunto
							.trim())) {

						sql.append(" JOIN stf.assuntos ass ON ap.cod_assunto = ass.cod_assunto ");

						searchData.descricaoAssunto = searchData.descricaoAssunto
								.replace("|", "\\|");
						searchData.descricaoAssunto = searchData.descricaoAssunto
								.replace("(", "");
						searchData.descricaoAssunto = searchData.descricaoAssunto
								.replace(")", "");

						sql.append(" AND contains(ass.dsc_assunto_completo,'"
								+ searchData.descricaoAssunto.trim()
								+ "') > 0 ");
					}
				}

				if (searchData.getPesquisaLegislacao()) {
					sql.append(" JOIN stf.legislacao_processo lp ON lp.seq_objeto_incidente = vw.seq_objeto_incidente ");

					if (searchData.numeroLegislacaoPesquisa != null) {
						sql.append(" AND lp.nu_legislacao = "
								+ searchData.numeroLegislacaoPesquisa);
					}

					if (searchData.numeroAnoPesquisa != null) {
						sql.append(" AND lp.ano_legislacao = "
								+ searchData.numeroAnoPesquisa);
					}

					if (SearchData
							.stringNotEmpty(searchData.numeroArtigoPesquisa)) {
						sql.append(" AND lp.artigo = '"
								+ searchData.numeroArtigoPesquisa + "' ");
					}

					if (SearchData
							.stringNotEmpty(searchData.numeroIncisoPesquisa)) {
						sql.append(" AND lp.inciso =  '"
								+ searchData.numeroIncisoPesquisa + "' ");
					}

					if (SearchData
							.stringNotEmpty(searchData.numeroParagrafoPesquisa)) {
						sql.append(" AND lp.paragrafo = '"
								+ searchData.numeroParagrafoPesquisa + "' ");
					}

					if (SearchData
							.stringNotEmpty(searchData.numeroAlineaPesquisa)) {
						sql.append(" AND lp.alinea = '"
								+ searchData.numeroAlineaPesquisa + "' ");
					}

					if (searchData.normaPesquisa != null
							&& searchData.normaPesquisa > 0) {
						sql.append(" JOIN stf.norma_processo np ON lp.cod_norma = np.cod_norma ");
						sql.append(" AND np.cod_norma = "
								+ searchData.normaPesquisa);
					}
				}

				if (SearchData.stringNotEmpty(searchData.tipoJulgamento)) {

					if (searchData.tipoJulgamento.equalsIgnoreCase("QO"))
						sql.append(" AND pt.tipo_julgamento IN ('QO-RG','QO') ");
					else if (searchData.tipoJulgamento.equalsIgnoreCase("RG")) {
						sql.append(" AND pt.tipo_julgamento = 'RG' ");
					}
				}

				if (searchData.getPesquisaJulgamentoProcesso()) {
					
					if ((searchData.situacaoRG != null)	&& (searchData.situacaoRG == SituacaoRG.TODOS)) {
						
						StringBuffer sqlAndamento = new StringBuffer();
						sqlAndamento.append(" AND EXISTS ");
						sqlAndamento.append(" 	(SELECT an.cod_andamento an FROM stf.andamento_processos an ");
						sqlAndamento.append("    	  WHERE an.sig_classe_proces = pt.sig_classe_proces ");
						sqlAndamento.append("    	      AND an.num_processo = pt.num_processo ");
						sqlAndamento.append("   		  AND an.flg_lancamento_indevido <> 'S'");
						sql.append(sqlAndamento);
						sql.append(" AND an.cod_andamento  IN ("
								+ AndamentoRG.HARG.getCodigoAndamento() + ", "
								+ AndamentoRG.NAOHARG.getCodigoAndamento()+ ", "
								+ AndamentoRG.NAOHARGQC.getCodigoAndamento()+ ", "
								+ AndamentoRG.HARGJM.getCodigoAndamento()+ ","
								+ AndamentoRG.AIPROVIDOJMRG.getCodigoAndamento()+", "
								+ AndamentoRG.SUBSTITUIDORG.getCodigoAndamento() + ", " 
								+ AndamentoRG.EMJULGAMENTO.getCodigoAndamento() + " )");
						
						if (SearchData.hasDate(searchData.dataInicioDateRange)) {
							sql.append(" AND (an.dat_andamento BETWEEN TO_DATE ('"
									+ searchData.dataInicioDateRange
											.getDateHourInitialString()
									+ "', 'DD/MM/YYYY hh24:mi:ss') ");
							sql.append("      AND TO_DATE ('"
									+ searchData.dataInicioDateRange
											.getDateHourFinalString()
									+ "', 'DD/MM/YYYY hh24:mi:ss'))  ) ");
						} else {
							sql.append(")");
						}
						
					}
					
					if (searchData.situacaoRG != null) {
						StringBuffer sqlAndamento = new StringBuffer();
						
						if (!SituacaoRG.TODOS.equals(searchData.situacaoRG) && !SituacaoRG.EM_JULGAMENTO.equals(searchData.situacaoRG)) {
							sqlAndamento.append(" AND EXISTS ");
							sqlAndamento.append(" 	(SELECT an.cod_andamento an FROM stf.andamento_processos an ");
							sqlAndamento.append("    	  WHERE an.sig_classe_proces = pt.sig_classe_proces ");
							sqlAndamento.append("    	      AND an.num_processo             = pt.num_processo ");
							sqlAndamento.append("   		  AND an.flg_lancamento_indevido <> 'S' AND an.cod_andamento IN ");
						}
						
						if (SituacaoRG.EM_JULGAMENTO.equals(searchData.situacaoRG)) {
							sql.append("AND exists  (SELECT 1 "
									+ "FROM julgamento.julgamento_processo jp, julgamento.sessao s "
									+ "WHERE jp.sig_classe_proces = pt.sig_classe_proces and jp.num_processo = pt.num_processo and "
									+ "s.seq_sessao = jp.seq_sessao "
									+ "AND s.dat_inicio IS NOT NULL "
									+ "AND s.dat_fim IS NULL "
									+ "AND jp.tipo_julgamento LIKE '%RG%')");
						}
						
						if (searchData.situacaoRG == SituacaoRG.MATERIA_INFRA) {

							sql.append(sqlAndamento);
							sql.append("(");
							sql.append(AndamentoRG.NAOHARGQC
									.getCodigoAndamento());
							sql.append(")");

						}
						if (searchData.situacaoRG == SituacaoRG.SEM_REPERCUSSAO) {

							sql.append(sqlAndamento);
							sql.append("(");
							sql.append(AndamentoRG.NAOHARG
									.getCodigoAndamento());
							sql.append(")");

						}
						if (searchData.situacaoRG == SituacaoRG.COM_REPERCUSSAO) {

							sql.append(sqlAndamento);
							sql.append("(");
							sql.append(AndamentoRG.HARG.getCodigoAndamento() + "," 
							+ AndamentoRG.HARGJM.getCodigoAndamento() + "," 
							+ AndamentoRG.AIPROVIDOJMRG.getCodigoAndamento() + ","
							+ AndamentoRG.SUBSTITUIDORG.getCodigoAndamento());
							sql.append(")");

						}
						if (searchData.situacaoRG == SituacaoRG.JULGADAS) {

							sql.append(sqlAndamento);
							sql.append("(");
							sql.append(
									AndamentoRG.HARG.getCodigoAndamento() + "," 
									+ AndamentoRG.HARGJM.getCodigoAndamento() + "," 
									+ AndamentoRG.AIPROVIDOJMRG.getCodigoAndamento() + ","
									+ AndamentoRG.NAOHARG.getCodigoAndamento() + "," 
									+ AndamentoRG.NAOHARGQC.getCodigoAndamento() + "," 
									+ AndamentoRG.SUBSTITUIDORG.getCodigoAndamento());
							sql.append(")");

						}
					}

					if (SearchData.hasDate(searchData.dataInicioDateRange)) {
						sql.append(" AND (an.dat_andamento BETWEEN TO_DATE ('" + searchData.dataInicioDateRange.getDateHourInitialString() + "', 'DD/MM/YYYY hh24:mi:ss') ");
						sql.append("      AND TO_DATE ('" + searchData.dataInicioDateRange.getDateHourFinalString() + "', 'DD/MM/YYYY hh24:mi:ss')  )) ");
					}
				}

				if (searchData.publicados != null) {

					sql.append(searchData.publicados ? "AND EXISTS"
							: "AND NOT EXISTS");
					sql.append("(SELECT pp.seq_processo_publicados FROM stf.processo_publicados pp, ");
					sql.append("  stf.materias m, stf.data_publicacoes dp, judiciario.objeto_incidente obj ");
					sql.append(" WHERE pp.seq_objeto_incidente = obj.seq_objeto_incidente ");
					sql.append(" AND obj.seq_objeto_incidente_principal = vw.seq_objeto_incidente ");
					sql.append(" AND m.cod_capitulo = "
							+ EstruturaPublicacao.COD_CAPITULO_PLENARIO);
					sql.append(" AND m.cod_materia = "
							+ EstruturaPublicacao.COD_MATERIA_REPERCUSSAO_GERAL);
					sql.append(" AND m.cod_conteudo = "
							+ EstruturaPublicacao.COD_CONTEUDO_RELACAO_PROCESSO);
					sql.append(" AND dp.dat_publicacao_dj IS NOT NULL  AND pp.cod_capitulo = m.cod_capitulo");
					sql.append(" AND pp.cod_materia = m.cod_materia AND pp.num_materia = m.num_materia ");
					sql.append(" AND pp.ano_materia = m.ano_materia AND m.seq_data_publicacoes = dp.seq_data_publicacoes) ");
				}

				if (searchData.meritoJulgado != null) {

					sql.append(searchData.meritoJulgado ? " AND EXISTS"
							: " AND NOT EXISTS");
					sql.append("  ( SELECT csa.cod_andamento  FROM egab.configuracao_setor_andamento csa,");
					sql.append(" 					  egab.configuracao_andamento_setor cas, stf.andamento_processos ap ");
					sql.append("    WHERE csa.seq_configuracao_anda_setor = cas.seq_configuracao_anda_setor ");
					sql.append("    AND ap.cod_andamento = csa.cod_andamento AND cas.sig_configuracao_anda_setor in ('JMA','JMP') ");
					sql.append("    AND ap.flg_lancamento_indevido <> 'S' AND ap.seq_objeto_incidente = vw.seq_objeto_incidente ) ");
				}

				if (SearchData.stringNotEmpty(searchData.siglaClasse)) {
					sql.append(" AND pt.sig_classe_proces = '"
							+ searchData.siglaClasse + "'");
				}

				if (searchData.numeroProcesso != null) {
					sql.append(" AND pt.num_processo = "
							+ searchData.numeroProcesso);
				}

				if (searchData.codigoMinistroRelator != null
						&& searchData.codigoMinistroRelator > 0) {
					sql.append(" JOIN stf.sit_min_processos mn ON vw.seq_objeto_incidente = mn.seq_objeto_incidente ");
					sql.append(" AND mn.flg_relator_atual = 'S' AND mn.cod_ministro = "
							+ searchData.codigoMinistroRelator);
				}

				if (searchData.numeroTema != null && searchData.numeroTema > 0L)
					sql.append(" AND t.num_tema = " + searchData.numeroTema);

				sql.append("    AND pt.cod_tipo_ocorrencia = 1 ");
				sql.append(" and t.cod_tipo_tema = 1 ");
				sql.append("    AND t.num_tema is not null ");

				if (ordem.equalsIgnoreCase("Processo"))
					sqlOrder.append(" ORDER BY pt.sig_classe_proces, pt.num_processo asc  ");
				else {
					if (ordem.equalsIgnoreCase("Número Tema"))
						sqlOrder.append(" ORDER BY t.num_tema asc  ");
					else {
						if (ordem.equalsIgnoreCase("Tempo Restante"))
							sqlOrder.append(" ORDER BY pt.dat_ocorrencia desc  ");
						else {
							sqlOrder.append(" ORDER BY t.num_tema asc  ");
						}
					}
				}

				/************************************************************************************************
				 * Se for utilizado o índice textual do Assunto, pode existir
				 * mais de um assunto para o processo, e isso tratá o Processo
				 * Tema duplicado na consulta, para evitar isso utilizamos o
				 * DISTINCT
				 * *********************************************************************************************/
				if (SearchData.stringNotEmpty(searchData.descricaoAssunto
						.trim())) {

					sqlCampos
							.append(" SELECT DISTINCT seq_processo_tema, seq_objeto_incidente, cod_tipo_ocorrencia, seq_tema, dat_ocorrencia, sig_classe_proces, num_processo, tipo_julgamento, num_tema FROM ( ");
					sqlCampos
							.append(" SELECT pt.seq_processo_tema, pt.seq_objeto_incidente, pt.cod_tipo_ocorrencia, pt.seq_tema, pt.dat_ocorrencia, pt.sig_classe_proces, pt.num_processo, pt.tipo_julgamento, t.num_tema ");
					sqlCampos.append(sql);
					sqlCampos.append(" )");
					
					sqlOrder.delete(0, sqlOrder.length());
					
					if (ordem.equalsIgnoreCase("Processo"))
						sqlOrder.append(" ORDER BY sig_classe_proces, num_processo asc  ");
					else {
						if (ordem.equalsIgnoreCase("Número Tema"))
							sqlOrder.append(" ORDER BY num_tema asc  ");
						else {
							if (ordem.equalsIgnoreCase("Tempo Restante"))
								sqlOrder.append(" ORDER BY dat_ocorrencia desc  ");
							else {
								sqlOrder.append(" ORDER BY num_tema asc  ");
							}
						}
					}
					sqlCampos.append(sqlOrder);					
					
					sqlCount.append(" SELECT count(DISTINCT seq_processo_tema) FROM ( ");
					sqlCount.append(" SELECT pt.seq_processo_tema ");
					sqlCount.append(sql);
					sqlCount.append(" )");
				} else {
					sqlCampos
							.append("SELECT pt.seq_processo_tema, pt.seq_objeto_incidente, pt.cod_tipo_ocorrencia, pt.seq_tema, pt.dat_ocorrencia, pt.sig_classe_proces, pt.num_processo, pt.tipo_julgamento ");
					sqlCampos.append(sql);
					sqlCampos.append(sqlOrder);

					sqlCount.append("SELECT count(*) ");
					sqlCount.append(sql);
				}

				Query countQuery = null;
				SQLQuery collectionQuery = null;

				countQuery = session.createSQLQuery(sqlCount.toString());

				collectionQuery = session.createSQLQuery(sqlCampos.toString());
				collectionQuery.addEntity(ProcessoTema.class);

				Long totalSize = 0L;
				if (searchData.getTotalResult() != null
						&& searchData.getTotalResult() > 0L) {
					totalSize = searchData.getTotalResult();
				} else {
					totalSize = Long.parseLong(countQuery.uniqueResult()
							.toString());
				}

				List<ProcessoTema> lista = new ArrayList<ProcessoTema>();
				List<ProcessoTema> result = new ArrayList<ProcessoTema>();

				if (totalSize > 0) {

					/* O banco de dados nao retorna a lista de processos correta
					 * quando o pageMaxResult é igual a 15. A melhor solucao
					 * encontrada foi incrementar o pageMaxResult em 1 e remover
					 * o ultimo item da lista antes de retorna-la. Como o
					 * hibernate monitora todas as mudanças de seus objetos, a
					 * lista deve ser duplicada antes da remoção do útlmo ítem. */
					if (totalSize > searchData.getPageData().getPageMaxResult()
							&& searchData.isPaging()) {
						collectionQuery.setMaxResults(searchData.getPageData()
								.getPageMaxResult() + 1);
						collectionQuery.setFirstResult(searchData.getPageData()
								.getFirstResult());
					}

					lista = collectionQuery.list();
					// Duplica a lista e remove o último elemento.
					result = new LinkedList<ProcessoTema>(lista);
					if (result.size() == (searchData.getPageData()
							.getPageMaxResult() + 1))
						result.remove(result.size() - 1);

				}

				SearchResult sr = new SearchResult(searchData, totalSize,
						result);
				return sr;

			}
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return null;
	}

	// TODO Alterar para pesquisar na nova tabela de julgamento
	public SearchResult pesquisarRepercussaoGeral(RepercussaoGeralSearchData searchData) throws DaoException {

		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer(" FROM Processo p ");

			if (searchData != null) {

				if (searchData.getPesquisaAssunto())
					hql.append(" JOIN p.assuntos a ");

				if (searchData.getPesquisaLegislacao())
					hql.append(" , LegislacaoProcesso lp ");

				// if criado para contemplar a busca dos processos da pesquisa externa pelo o número do tema
				if (searchData.numeroTema != null && searchData.numeroTema > 0L && !searchData.getPesquisaJulgamentoProcesso())
					hql.append(" , ObjetoIncidente oi " + ", ProcessoTema pt " + " , Tema t ");

				if (searchData.getPesquisaJulgamentoProcesso()) {
					hql.append(" , JulgamentoProcesso jp "
							+ " JOIN jp.objetoIncidente oi, "
							+ "      IncidenteJulgamento ij, "
							+ " 	  ProcessoTema pt, Tema t ");
					
					if (searchData.getPesquisaSituacaoJulgamento())
						hql.append(" JOIN jp.listaSituacaoJulgamento sj ");

					if (SearchData.stringNotEmpty(searchData.tipoJulgamento) && searchData.tipoJulgamento.equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL)) {

						hql.append(" WHERE jp.sessao.tipoAmbiente = '"
								+ TipoAmbienteConstante.VIRTUAL.getSigla()
								+ "' " + " AND ij.principal.id = p.id ");
					} else {
						hql.append(" WHERE ij.principal.id = p.id");
					}

					hql.append(" AND oi.id = ij.id ");

					if ((searchData.siglaClasse != null || searchData.numeroProcesso != null) && (searchData.tipoJulgamento != null && searchData.tipoJulgamento.length() > 0)) {
						if (!(searchData.tipoJulgamento.equals("QO"))) {
							hql.append(" AND ij.tipoJulgamento.sigla <> 'QO'");
						}
					} else if ((searchData.siglaClasse != null && searchData.numeroProcesso != null)
							&& (searchData.tipoJulgamento == null || searchData.tipoJulgamento.length() == 0)) {
						hql.append(" AND ij.tipoJulgamento.sigla <> 'QO'");
					}

					if (searchData.numeroTema != null && searchData.numeroTema > 0L) {
						hql.append(" AND oi.id = pt.objetoIncidente.id ");
						hql.append(" AND pt.tema.id = t.id ");
					}

				} else {
					hql.append(" WHERE (1=1) ");
				}

				if (searchData.getPesquisaJulgamentoProcesso()) {
					hql.append(" AND oi.id = pt.objetoIncidente.id ");
					hql.append(" AND pt.tema.id = t.id ");
				}

				if (searchData.numeroTema != null && searchData.numeroTema > 0L
						&& !searchData.getPesquisaJulgamentoProcesso()) {
					hql.append(" AND p.id = oi.principal.id "
							+ " AND oi.id = pt.objetoIncidente.id  "
							+ " AND pt.tema.id = t.id ");
				}

				if (searchData.getPesquisaLegislacao()) {
					hql.append(" AND lp.objetoIncidente.principal.id = p.id ");
				}

				if (SearchData.stringNotEmpty(searchData.siglaClasse)) {
					hql.append(" AND p.siglaClasseProcessual =:siglaClasse");
				}

				if (searchData.numeroProcesso != null) {
					hql.append(" AND p.numeroProcessual = :numeroProcesso");
				}

				if (searchData.classesProcessuais != null && searchData.classesProcessuais.size() > 0) {
					hql.append(" AND p.siglaClasseProcessual in(:classesProcessuais)");
				}

				if (SearchData.stringNotEmpty(searchData.tipoJulgamento)) {
					if (searchData.tipoJulgamento.equals(TipoIncidenteJulgamento.SIGLA_QUESTAO_ORDEM)) {
						hql.append(" AND ij.siglaCadeiaIncidente like(:tipoJulgamento)");
					} else {
						hql.append(" AND ij.tipoJulgamento.sigla = :tipoJulgamento");
					}
				}

				if (SearchData.stringNotEmpty(searchData.tipoJulgamentoDiferente)) {
					hql.append(" AND ij.tipoJulgamento.sigla <> :tipoJulgamentoDiferente");
				}

				if (searchData.getPesquisaSituacaoJulgamento()) {
					hql.append(" AND sj.atual = 'S' AND sj.tipoSituacaoJulgamento.id in(:idSituacoes) ");
				}

				if (SearchData.hasDate(searchData.dataInicioDateRange)) {
					hql.append(" AND jp.sessao.dataInicio >= to_date( :dataInicioInitial , 'DD/MM/YYYY HH24:MI:SS') ");
					hql.append(" AND jp.sessao.dataInicio <= to_date( :dataInicioFinal , 'DD/MM/YYYY HH24:MI:SS') ");
				}

				if (searchData.codigoAssunto != null) {
					hql.append(" AND a.id = :codigoAssunto");
				}

				if (searchData.numeroTema != null && searchData.numeroTema > 0L) {
					hql.append(" AND t.numeroSequenciaTema = :numeroSequenciaTema");
				}

				if (SearchData.stringNotEmpty(searchData.descricaoAssunto)) {
					searchData.descricaoAssunto = searchData.descricaoAssunto
							.replace("|", "\\|");
					searchData.descricaoAssunto = searchData.descricaoAssunto
							.replace("(", "");
					searchData.descricaoAssunto = searchData.descricaoAssunto
							.replace(")", "");
					hql.append(" AND contains(a.descricaoCompleta,'"
							+ searchData.descricaoAssunto.trim() + "%') > 1");
				}

				hql.append(" AND p.repercussaoGeral = '" + Flag.SIM + "'");

				if (searchData.publicados != null) {

					hql.append(searchData.publicados ? "AND EXISTS"
							: "AND NOT EXISTS");

					hql.append("(SELECT pp.id "
							+ "  FROM ProcessoPublicado pp, "
							+ "       ConteudoPublicacao m, "
							+ "       Publicacao dp "
							+ " WHERE pp.objetoIncidente.principal.id = p.id"
							+ " AND m.codigoCapitulo = "
							+ EstruturaPublicacao.COD_CAPITULO_PLENARIO
							+ " AND m.codigoMateria = "
							+ EstruturaPublicacao.COD_MATERIA_REPERCUSSAO_GERAL
							+ " AND m.codigoConteudo = "
							+ EstruturaPublicacao.COD_CONTEUDO_RELACAO_PROCESSO
							+ " AND dp.dataPublicacaoDj is not null"
							+ " AND pp.codigoCapitulo = m.codigoCapitulo "
							+ " AND pp.codigoMateria = m.codigoMateria"
							+ " AND pp.numeroMateria = m.numero"
							+ " AND pp.anoMateria = m.ano"
							+ " AND m.publicacao.id = dp.id)");
				}

				if (searchData.codigoMinistroRelator != null) {
					hql.append(" AND p.ministroRelatorAtual.id = :codigoMinistroRelator ");
				}

				if (searchData.situacao != null && searchData.situacao.equals(Situacao.MANIFESTACAO_PENDENTE_EM_JULGAMENTO) && searchData.codigoMinistroAutenticado != null) {
					hql.append(" AND NOT EXISTS "
							+ "(SELECT vjp.id "
							+ " FROM VotoJulgamentoProcesso vjp "
							+ " JOIN vjp.julgamentoProcesso.listaSituacaoJulgamento sjp"
							+ " WHERE vjp.ministro.id = :codigoMinistroAutenticado "
							+ " 	AND vjp.julgamentoProcesso.sessao.tipoAmbiente = '"
							+ TipoAmbienteConstante.VIRTUAL.getSigla()
							+ "'"
							+ "	AND vjp.julgamentoProcesso.objetoIncidente.principal.id = p.id"
							+ "   AND sjp.atual = 'S'"
							+ "   AND sjp.tipoSituacaoJulgamento. ="
							+ TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO
									.getCodigo() + "  ) ");
				}

				if (searchData.situacao != null && searchData.situacao.equals(Situacao.JULGAMENTO_NAO_INICIADO)) {

					hql.append(" AND NOT EXISTS ( SELECT jp.id "
							+ "				       FROM JulgamentoProcesso jp "
							+ "                      JOIN jp.objetoIncidente ij"
							+ "			          WHERE ij.principal.id = p.id"
							+ "					  	AND jp.sessao.tipoAmbiente = '"
							+ TipoAmbienteConstante.VIRTUAL.getSigla() + "')");
				}
				
				reanaliseQuery(searchData, hql);
				
				if (searchData.haRepercussaoGeral != null) {
					hql.append(" AND jp.andamentoProcesso.tipoAndamento.id = :codigoAndamentoDecisao");
				}

				if (searchData.baixados != null) {
					List<Long> listaBaixados = pesquisaAndamentBaixa();
					if (listaBaixados != null && listaBaixados.size() > 0) {
						hql.append(searchData.baixados ? " AND EXISTS" : " AND NOT EXISTS");
						hql.append(" (SELECT ap.id "
								+ " FROM AndamentoProcesso ap "
								+ " WHERE ap.objetoIncidente.id = p.id "
								+ " AND ap.lancamentoIndevido <> 'S'"
								+ " AND ap.ultimoAndamento = 'S'"
								+ " AND ap.tipoAndamento.id IN"
								+ SearchData.inClause(listaBaixados, false)
								+ ")");
						}
				}

				if (searchData.normaPesquisa != null
						&& searchData.normaPesquisa > 0) {
					hql.append(" AND lp.normaProcesso.id = :normaPesquisa");
				}

				if (searchData.numeroLegislacaoPesquisa != null) {
					hql.append(" AND lp.numeroLegislacao = :numeroLegislacaoPesquisa");
				}

				if (searchData.numeroAnoPesquisa != null) {
					hql.append(" AND lp.anoLegislacao = :numeroAnoPesquisa");
				}

				if (SearchData.stringNotEmpty(searchData.numeroArtigoPesquisa)) {
					hql.append(" AND lp.artigo = :numeroArtigoPesquisa");
				}

				if (SearchData.stringNotEmpty(searchData.numeroIncisoPesquisa)) {
					hql.append(" AND lp.inciso = :numeroIncisoPesquisa");
				}

				if (SearchData
						.stringNotEmpty(searchData.numeroParagrafoPesquisa)) {
					hql.append(" AND lp.paragrafo = :numeroParagrafoPesquisa");
				}

				if (SearchData.stringNotEmpty(searchData.numeroAlineaPesquisa)) {
					hql.append(" AND lp.alinea = :numeroAlineaPesquisa ");
				}

				if (searchData.meritoJulgado != null) {
					hql.append(searchData.meritoJulgado ? " AND EXISTS"
							: " AND NOT EXISTS");
					hql.append(" (SELECT ap.tipoAndamento.id "
							+ "  FROM AndamentoProcesso ap, "
							+ " WHERE p.id = ap.objetoIncidente.principal.id"
							+ " AND EXISTS ( SELECT a.id "
							+ " 		FROM ConfiguracaoAndamentoSetor c "
							+ " 		JOIN c.tipoAndamentos a "
							+ " 		WHERE c.sigla = '"
							+ ConstantSigAndamentoSetor.JULGAMENTO_MERITO_PROCESSO
									.getSigla()
							+ "'"
							+ "		AND a.id = ap.tipoAndamento.id ) "
							+ " AND NOT EXISTS (  "
							+ " 		SELECT apind.tipoAndamento.id "
							+ " 		 FROM AndamentoProcesso apind "
							+ " 		WHERE apind.objetoIncidente.principal.id = p.id "
							+ " 		AND apind.tipoAndamento.id IN ("
							+ ConstanteAndamento.LANCAMENTO_INDEVIDO1
									.getCodigo()
							+ ","
							+ ConstanteAndamento.LANCAMENTO_INDEVIDO2
									.getCodigo()
							+ ")"
							+ " 		AND apind.numeroSequenciaErrado = ap.numeroSequencia ))");
				}

				if (searchData.getPesquisaJulgamentoProcesso()) {
					hql.append(" ORDER BY jp.sessao.dataPrevistaInicio ");
				}
			}

			Query countQuery = null;
			Query collectionQuery = null;

			countQuery = session.createQuery(" SELECT COUNT(p.id)" + hql.toString());
			collectionQuery = session.createQuery(" SELECT p " + hql.toString());

			if (SearchData.stringNotEmpty(searchData.siglaClasse)) {
				countQuery.setParameter("siglaClasse", searchData.siglaClasse);
				collectionQuery.setParameter("siglaClasse", searchData.siglaClasse);
			}
			if (searchData.numeroProcesso != null) {
				countQuery.setParameter("numeroProcesso", searchData.numeroProcesso);
				collectionQuery.setParameter("numeroProcesso", searchData.numeroProcesso);
			}

			if (searchData.numeroTema != null && searchData.numeroTema > 0L) {
				countQuery.setParameter("numeroSequenciaTema", searchData.numeroTema);
				collectionQuery.setParameter("numeroSequenciaTema", searchData.numeroTema);
			}

			if (searchData.classesProcessuais != null && searchData.classesProcessuais.size() > 0) {
				countQuery.setParameterList("classesProcessuais", searchData.classesProcessuais);
				collectionQuery.setParameterList("classesProcessuais", searchData.classesProcessuais);
			}

			if (SearchData.stringNotEmpty(searchData.tipoJulgamento)) {
				String tpJulg = searchData.tipoJulgamento.equals(TipoIncidenteJulgamento.SIGLA_QUESTAO_ORDEM) ? "%" + searchData.tipoJulgamento + "%" : searchData.tipoJulgamento;
				countQuery.setParameter("tipoJulgamento", tpJulg);
				collectionQuery.setParameter("tipoJulgamento", tpJulg);
			}

			if (SearchData.stringNotEmpty(searchData.tipoJulgamentoDiferente)) {
				countQuery.setParameter("tipoJulgamentoDiferente", searchData.tipoJulgamentoDiferente);
				collectionQuery.setParameter("tipoJulgamentoDiferente", searchData.tipoJulgamentoDiferente);
			}

			if (searchData.codigoAssunto != null) {
				countQuery.setParameter("codigoAssunto", searchData.codigoAssunto);
				collectionQuery.setParameter("codigoAssunto", searchData.codigoAssunto);
			}
			if (searchData.codigoMinistroRelator != null) {
				countQuery.setParameter("codigoMinistroRelator", searchData.codigoMinistroRelator);
				collectionQuery.setParameter("codigoMinistroRelator", searchData.codigoMinistroRelator);
			}
			if (searchData.codigoMinistroAutenticado != null) {
				countQuery.setParameter("codigoMinistroAutenticado", searchData.codigoMinistroAutenticado);
				collectionQuery.setParameter("codigoMinistroAutenticado", searchData.codigoMinistroAutenticado);
			}
			if (searchData.normaPesquisa != null && searchData.normaPesquisa > 0) {
				countQuery.setParameter("normaPesquisa", searchData.normaPesquisa);
				collectionQuery.setParameter("normaPesquisa", searchData.normaPesquisa);
			}
			if (searchData.numeroLegislacaoPesquisa != null) {
				countQuery.setParameter("numeroLegislacaoPesquisa", searchData.numeroLegislacaoPesquisa);
				collectionQuery.setParameter("numeroLegislacaoPesquisa", searchData.numeroLegislacaoPesquisa);
			}
			if (searchData.numeroAnoPesquisa != null) {
				countQuery.setParameter("numeroAnoPesquisa", searchData.numeroAnoPesquisa);
				collectionQuery.setParameter("numeroAnoPesquisa", searchData.numeroAnoPesquisa);
			}
			if (SearchData.stringNotEmpty(searchData.numeroArtigoPesquisa)) {
				countQuery.setParameter("numeroArtigoPesquisa", searchData.numeroArtigoPesquisa);
				collectionQuery.setParameter("numeroArtigoPesquisa", searchData.numeroArtigoPesquisa);
			}
			if (SearchData.stringNotEmpty(searchData.numeroIncisoPesquisa)) {
				countQuery.setParameter("numeroIncisoPesquisa", searchData.numeroIncisoPesquisa);
				collectionQuery.setParameter("numeroIncisoPesquisa", searchData.numeroIncisoPesquisa);
			}
			if (SearchData.stringNotEmpty(searchData.numeroParagrafoPesquisa)) {
				countQuery.setParameter("numeroParagrafoPesquisa", searchData.numeroParagrafoPesquisa);
				collectionQuery.setParameter("numeroParagrafoPesquisa", searchData.numeroParagrafoPesquisa);
			}
			if (SearchData.stringNotEmpty(searchData.numeroAlineaPesquisa)) {
				countQuery.setParameter("numeroAlineaPesquisa", searchData.numeroAlineaPesquisa);
				collectionQuery.setParameter("numeroAlineaPesquisa", searchData.numeroAlineaPesquisa);
			}
			if (SearchData.hasDate(searchData.dataInicioDateRange)) {
				countQuery.setParameter("dataInicioInitial", searchData.dataInicioDateRange.getDateHourInitialString());
				collectionQuery.setParameter("dataInicioInitial", searchData.dataInicioDateRange.getDateHourInitialString());
				countQuery.setParameter("dataInicioFinal", searchData.dataInicioDateRange.getDateHourFinalString());
				collectionQuery.setParameter("dataInicioFinal", searchData.dataInicioDateRange.getDateHourFinalString());
			}

			if (searchData.haRepercussaoGeral != null) {
				Long codigoAndamentoDecisao = searchData.haRepercussaoGeral ? ConstanteAndamento.DECISAO_EXISTENCIA_REPERCUSSAO_GERAL.getCodigo()
						: ConstanteAndamento.DECISAO_INEXISTENCIA_REPERCUSSAO_GERAL.getCodigo();
				countQuery.setParameter("codigoAndamentoDecisao", codigoAndamentoDecisao);
				collectionQuery.setParameter("codigoAndamentoDecisao", codigoAndamentoDecisao);
			}

			if (searchData.getPesquisaSituacaoJulgamento()) {
				List<String> idSituacoes = new LinkedList<String>();
				if (searchData.situacao.equals(Situacao.EM_JULGAMENTO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ABERTO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.JULGADO_EM_JULGAMENTO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ABERTO.getCodigo());
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.JULGADO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.SUSPENSO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.SUSPENSO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.MANIFESTACAO_PENDENTE_EM_JULGAMENTO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.AGENDADO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.AGENDADO.getCodigo());
				}

				countQuery.setParameterList("idSituacoes", idSituacoes);
				collectionQuery.setParameterList("idSituacoes", idSituacoes);
			}

			Long totalSize = 0L;
			if (searchData.getTotalResult() != null && searchData.getTotalResult() > 0L) {
				totalSize = searchData.getTotalResult();
			} else {
				totalSize = (Long) countQuery.uniqueResult();
			}

			List<Processo> lista = null;
			List<Processo> result = null;

			if (totalSize > 0) {

				/*
				 * O banco de dados nao retorna a lista de processos correta quando o
				 * pageMaxResult é igual a 15. A melhor solucao encontrada foi incrementar o
				 * pageMaxResult em 1 e remover o ultimo item da lista antes de retorna-la. Como
				 * o hibernate monitora todas as mudanças de seus objetos, a lista deve ser
				 * duplicada antes da remoção do útlmo ítem.
				 */
				if (totalSize > searchData.getPageData().getPageMaxResult() && searchData.isPaging()) {
					collectionQuery.setMaxResults(searchData.getPageData().getPageMaxResult() + 1);
					collectionQuery.setFirstResult(searchData.getPageData().getFirstResult());
				}

				lista = collectionQuery.list();
				// Duplica a lista e remove o último elemento.
				result = new LinkedList<Processo>(lista);
				if (result.size() == (searchData.getPageData().getPageMaxResult() + 1))
					result.remove(result.size() - 1);

			}

			SearchResult sr = new SearchResult(searchData, totalSize, result);
			return sr;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	private void reanaliseQuery(RepercussaoGeralSearchData searchData, StringBuffer hql) {
		if (searchData.situacao != null && searchData.situacao.equals(Situacao.REANALISE)) {
			// Não tem segundo julgamento de RG
			hql.append(" AND NOT EXISTS (SELECT jp.id FROM JulgamentoProcesso jp JOIN jp.objetoIncidente ij WHERE ij.principal.id = p.id "
					+ " AND jp.tipoJulgamento = '" + TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO + "')");
			
			// Mas (já tem julgamento de RG) ou (é leading case e possui andamento 8251)
			hql.append(" AND (EXISTS (SELECT jp.id FROM JulgamentoProcesso jp JOIN jp.objetoIncidente ij WHERE ij.principal.id = p.id AND jp.tipoJulgamento = '" + TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL + "')");
			
			hql.append(" OR (EXISTS (SELECT ap.id FROM AndamentoProcesso ap WHERE ap.sigClasseProces = :siglaClasse AND ap.numProcesso = :numeroProcesso AND ap.codigoAndamento = 8251) "
					 + " AND EXISTS (SELECT pt.id FROM ProcessoTema pt WHERE pt.tipoOcorrencia = " + TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo() + ")))");
		}
	}

	// TODO Alterar para pesquisar na nova tabela de julgamento
	public SearchResult pesquisarRepercussaoGeralPlenarioVirtual(RepercussaoGeralSearchData searchData) throws DaoException {

		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer(" FROM Processo p ");

			if (searchData != null) {
				if (searchData.getPesquisaAssunto()) {
					hql.append(" JOIN p.assuntos a ");
				}

				if (searchData.getPesquisaJulgamentoProcesso()) {
					hql.append(", JulgamentoProcesso jp JOIN jp.objetoIncidente oi, IncidenteJulgamento ij");
					
					if (searchData.getPesquisaSituacaoJulgamento())
						hql.append(" JOIN jp.listaSituacaoJulgamento sj ");

					hql.append(" WHERE ij.principal.id = p.id AND oi.id = ij.id ");
					
					if (TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL.equals(searchData.tipoJulgamento)
							|| TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO.equals(searchData.tipoJulgamento))
						hql.append(" AND jp.sessao.tipoAmbiente = '" + TipoAmbienteConstante.VIRTUAL.getSigla() + "' ");

					if ((searchData.siglaClasse != null || searchData.numeroProcesso != null) && (searchData.tipoJulgamento != null && searchData.tipoJulgamento.length() > 0)) {
						if (!(searchData.tipoJulgamento.equals("QO")))
							hql.append(" AND ij.tipoJulgamento.sigla <> 'QO'");
						
					} else if ((searchData.siglaClasse != null && searchData.numeroProcesso != null) && (searchData.tipoJulgamento == null || searchData.tipoJulgamento.length() == 0)) {
						hql.append(" AND ij.tipoJulgamento.sigla <> 'QO'");
					}

				} else {
					hql.append(" WHERE 1=1 ");
				}
				
				if (Boolean.TRUE.equals(searchData.getExclusivoDigital()))
					hql.append(" AND jp.sessao.exclusivoDigital = 'S' ");
				
				if (Boolean.FALSE.equals(searchData.getExclusivoDigital()))
					hql.append(" AND jp.sessao.exclusivoDigital = 'N' ");
				

				if (searchData.getPesquisaLegislacao()) {
					hql.append(" AND lp.objetoIncidente.principal.id = p.id ");
				}

				if (SearchData.stringNotEmpty(searchData.siglaClasse)) {
					hql.append(" AND p.siglaClasseProcessual =:siglaClasse");
				}

				if (searchData.numeroProcesso != null) {
					hql.append(" AND p.numeroProcessual = :numeroProcesso");
				}

				if (searchData.classesProcessuais != null
						&& searchData.classesProcessuais.size() > 0) {
					hql.append(" AND p.siglaClasseProcessual in(:classesProcessuais)");
				}

				if (SearchData.stringNotEmpty(searchData.tipoJulgamento)) {
					if (searchData.tipoJulgamento.equals(TipoIncidenteJulgamento.SIGLA_QUESTAO_ORDEM)) {
						hql.append(" AND ij.siglaCadeiaIncidente like(:tipoJulgamento)");
					} else {
						hql.append(" AND ij.tipoJulgamento.sigla IN (:tipoJulgamento)");
					}
				}

				if (SearchData.stringNotEmpty(searchData.tipoJulgamentoDiferente))
					hql.append(" AND ij.tipoJulgamento.sigla <> :tipoJulgamentoDiferente");

				if (searchData.getPesquisaSituacaoJulgamento()) {
					hql.append(" AND sj.atual = 'S' AND sj.tipoSituacaoJulgamento.id in(:idSituacoes) ");
				}

				if (SearchData.hasDate(searchData.dataInicioDateRange)) {
					hql.append(" AND jp.sessao.dataInicio >= to_date( :dataInicioInitial , 'DD/MM/YYYY HH24:MI:SS') ");
					hql.append(" AND jp.sessao.dataInicio <= to_date( :dataInicioFinal , 'DD/MM/YYYY HH24:MI:SS') ");
				}

				hql.append(" AND p.repercussaoGeral = 'S'");

				if (searchData.codigoMinistroRelator != null) {
					hql.append(" AND p.ministroRelatorAtual.id = :codigoMinistroRelator ");
				}

				if (searchData.situacao != null && searchData.situacao.equals(Situacao.MANIFESTACAO_PENDENTE_EM_JULGAMENTO) && searchData.codigoMinistroAutenticado != null) {
					hql.append(" AND NOT EXISTS "
							+ "(SELECT vjp.id "
							+ " FROM VotoJulgamentoProcesso vjp "
							+ " JOIN vjp.julgamentoProcesso.listaSituacaoJulgamento sjp"
							+ " WHERE vjp.ministro.id = :codigoMinistroAutenticado "
							+ " 	AND vjp.julgamentoProcesso.sessao.tipoAmbiente = 'V'"
//							+ "	AND vjp.julgamentoProcesso.objetoIncidente.principal.id = p.id"
							+ "	AND vjp.julgamentoProcesso = jp"
							+ "   AND sjp.atual = 'S'"
							+ "   AND sjp.tipoSituacaoJulgamento.id ="
							+ TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo() + "  ) ");
				}

				if (searchData.situacao != null && searchData.situacao.equals(Situacao.JULGAMENTO_NAO_INICIADO)) {

					hql.append(" AND NOT EXISTS ( SELECT jp.id "
							+ "				       FROM JulgamentoProcesso jp "
							+ "                      JOIN jp.objetoIncidente ij"
							+ "			          WHERE ij.principal.id = p.id"
							+ "					  	AND jp.sessao.tipoAmbiente = '" + TipoAmbienteConstante.VIRTUAL.getSigla() 
							+ "'                     AND jp.sessao.tipoJulgamentoVirtual = " + TipoJulgamentoVirtual.REPERCUSSAO_GERAL.getId()
							+ ")");
				}
				
				reanaliseQuery(searchData, hql);

				if (searchData.haRepercussaoGeral != null) {
					hql.append(" AND jp.andamentoProcesso.tipoAndamento.id = :codigoAndamentoDecisao");
				}

				if (searchData.baixados != null) {
					List<Long> listaBaixados = pesquisaAndamentBaixa();
					if (listaBaixados != null && listaBaixados.size() > 0) {
						hql.append(searchData.baixados ? " AND EXISTS"
								: " AND NOT EXISTS");
						hql.append(" (SELECT ap.id "
								+ " FROM AndamentoProcesso ap "
								+ " WHERE ap.objetoIncidente.id = p.id "
								+ " AND ap.lancamentoIndevido <> 'S'"
								+ " AND ap.ultimoAndamento = 'S'"								
								+ " AND ap.tipoAndamento.id IN"
								+ SearchData.inClause(listaBaixados, false)
								+ ")");

					}
				}

				if (searchData.numeroAnoPesquisa != null) {
					hql.append(" AND lp.anoLegislacao = :numeroAnoPesquisa");
				}

				if (searchData.getPesquisaJulgamentoProcesso()) {
					hql.append(" ORDER BY jp.sessao.dataPrevistaInicio, p.siglaClasseProcessual, p.numeroProcessual ");
				}

			}

			Query countQuery = null;
			Query collectionQuery = null;

			countQuery = session.createQuery(" SELECT COUNT(p.id)" + hql.toString());
			collectionQuery = session.createQuery(" SELECT p " + hql.toString());
			
			if (Situacao.JULGADO.equals(searchData.situacao)) {
				Long[] idsAndamentosJulgamentoMerito = {6307L, 6308L, 6309L, 8562L};
				countQuery.setParameterList("idsAndamentosJulgamentoMerito", idsAndamentosJulgamentoMerito);
				collectionQuery.setParameterList("idsAndamentosJulgamentoMerito", idsAndamentosJulgamentoMerito);
			}
			
			if (SearchData.stringNotEmpty(searchData.siglaClasse)) {
				countQuery.setParameter("siglaClasse", searchData.siglaClasse);
				collectionQuery.setParameter("siglaClasse", searchData.siglaClasse);
			}
			if (searchData.numeroProcesso != null) {
				countQuery.setParameter("numeroProcesso", searchData.numeroProcesso);
				collectionQuery.setParameter("numeroProcesso", searchData.numeroProcesso);
			}

			if (searchData.classesProcessuais != null && searchData.classesProcessuais.size() > 0) {
				countQuery.setParameterList("classesProcessuais", searchData.classesProcessuais);
				collectionQuery.setParameterList("classesProcessuais", searchData.classesProcessuais);
			}

			if (SearchData.stringNotEmpty(searchData.tipoJulgamento)) {
				String tpJulg = searchData.tipoJulgamento.equals(TipoIncidenteJulgamento.SIGLA_QUESTAO_ORDEM) ? "%" + searchData.tipoJulgamento + "%" : searchData.tipoJulgamento;
				
				if (TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL.equals(tpJulg)) {
					countQuery.setParameterList("tipoJulgamento", Arrays.asList(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL, TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO));
					collectionQuery.setParameterList("tipoJulgamento", Arrays.asList(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL, TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO));
				} else {
					countQuery.setParameter("tipoJulgamento", tpJulg);
					collectionQuery.setParameter("tipoJulgamento", tpJulg);
				}
			}

			if (SearchData.stringNotEmpty(searchData.tipoJulgamentoDiferente)) {
				countQuery.setParameter("tipoJulgamentoDiferente", searchData.tipoJulgamentoDiferente);
				collectionQuery.setParameter("tipoJulgamentoDiferente", searchData.tipoJulgamentoDiferente);
			}

			if (searchData.codigoMinistroRelator != null) {
				countQuery.setParameter("codigoMinistroRelator", searchData.codigoMinistroRelator);
				collectionQuery.setParameter("codigoMinistroRelator", searchData.codigoMinistroRelator);
			}
			if (searchData.codigoMinistroAutenticado != null) {
				countQuery.setParameter("codigoMinistroAutenticado", searchData.codigoMinistroAutenticado);
				collectionQuery.setParameter("codigoMinistroAutenticado", searchData.codigoMinistroAutenticado);
			}
			if (searchData.numeroAnoPesquisa != null) {
				countQuery.setParameter("numeroAnoPesquisa", searchData.numeroAnoPesquisa);
				collectionQuery.setParameter("numeroAnoPesquisa", searchData.numeroAnoPesquisa);
			}
			if (SearchData.hasDate(searchData.dataInicioDateRange)) {
				countQuery.setParameter("dataInicioInitial", searchData.dataInicioDateRange.getDateHourInitialString());
				collectionQuery.setParameter("dataInicioInitial", searchData.dataInicioDateRange.getDateHourInitialString());
				countQuery.setParameter("dataInicioFinal", searchData.dataInicioDateRange.getDateHourFinalString());
				collectionQuery.setParameter("dataInicioFinal", searchData.dataInicioDateRange.getDateHourFinalString());
			}

			if (searchData.haRepercussaoGeral != null) {
				Long codigoAndamentoDecisao = searchData.haRepercussaoGeral ? ConstanteAndamento.DECISAO_EXISTENCIA_REPERCUSSAO_GERAL.getCodigo()
						: ConstanteAndamento.DECISAO_INEXISTENCIA_REPERCUSSAO_GERAL.getCodigo();
				countQuery.setParameter("codigoAndamentoDecisao", codigoAndamentoDecisao);
				collectionQuery.setParameter("codigoAndamentoDecisao", codigoAndamentoDecisao);
			}

			if (searchData.getPesquisaSituacaoJulgamento()) {
				List<String> idSituacoes = new LinkedList<String>();
				if (searchData.situacao.equals(Situacao.EM_JULGAMENTO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ABERTO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.JULGADO_EM_JULGAMENTO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ABERTO.getCodigo());
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.JULGADO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.SUSPENSO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.SUSPENSO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.MANIFESTACAO_PENDENTE_EM_JULGAMENTO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
				} else if (searchData.situacao.equals(Situacao.AGENDADO)) {
					idSituacoes.add(TipoSitucacaoJulgamentoConstant.AGENDADO.getCodigo());
				}

				countQuery.setParameterList("idSituacoes", idSituacoes);
				collectionQuery.setParameterList("idSituacoes", idSituacoes);
			}

			Long totalSize = 0L;
			if (searchData.getTotalResult() != null && searchData.getTotalResult() > 0L) {
				totalSize = searchData.getTotalResult();
			} else {
				totalSize = (Long) countQuery.uniqueResult();
			}

			List<Processo> lista = null;
			List<Processo> result = null;

			if (totalSize > 0) {

				/*
				 * O banco de dados nao retorna a lista de processos correta quando o
				 * pageMaxResult é igual a 15. A melhor solucao encontrada foi incrementar o
				 * pageMaxResult em 1 e remover o ultimo item da lista antes de retorna-la. Como
				 * o hibernate monitora todas as mudanças de seus objetos, a lista deve ser
				 * duplicada antes da remoção do útlmo ítem.
				 */
				if (totalSize > searchData.getPageData().getPageMaxResult() && searchData.isPaging()) {
					collectionQuery.setMaxResults(searchData.getPageData().getPageMaxResult() + 1);
					collectionQuery.setFirstResult(searchData.getPageData().getFirstResult());
				}

				lista = collectionQuery.list();
				// Duplica a lista e remove o último elemento.
				result = new LinkedList<Processo>(lista);
				if (result.size() == (searchData.getPageData().getPageMaxResult() + 1))
					result.remove(result.size() - 1);

			}

			SearchResult sr = new SearchResult(searchData, totalSize, result);
			return sr;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	@SuppressWarnings("deprecation")
	private List<Long> pesquisaAndamentBaixa() throws DaoException {

		Session session = retrieveSession();
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			connection = session.connection();
			List<Long> lista = new LinkedList<Long>();

			StringBuffer sql = new StringBuffer(
					"select cod_andamento from stf.andamentos "
							+ " where seq_grupo_andamento "
							+ " in(SELECT a.seq_grupo_andamento "
							+ " FROM stf.grupo_andamento a "
							+ " start with seq_grupo_andamento IN("
							+ ConstanteGrupoAndamento.BAIXA_NAO_DEFINITIVA
									.getCodigo()
							+ " ,"
							+ ConstanteGrupoAndamento.FINALIZADO.getCodigo()
							+ ")"
							+ " connect by prior seq_grupo_andamento = seq_pai_grupo_andamento)");
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql.toString());

			while (rs.next()) {
				lista.add(rs.getLong("cod_andamento"));
			}

			return lista;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (Exception e) {
			throw new DaoException("Exception", e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new DaoException("SQLException Finally", e);
			}
		}

	}

	public List<JulgamentoProcesso> pesquisarRepercussaoGeralFinalizadoTeste(
			Long idObjetoIncidente) throws DaoException {
		Session session = retrieveSession();
		List<JulgamentoProcesso> lista = null;
		try {

			String hql = "SELECT jp " + " FROM JulgamentoProcesso jp "
					+ " JOIN jp.sessao s " + " WHERE s.id = :objetoIncidente";

			Query q = session.createQuery(hql);
			q.setLong("objetoIncidente", idObjetoIncidente);

			lista = (List<JulgamentoProcesso>) q.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return lista;
	}

	public List<JulgamentoProcesso> pesquisarRepercussaoGeralFinalizadosSemAndamento()
			throws DaoException {
		Session session = retrieveSession();
		List<JulgamentoProcesso> lista = null;
		try {

			String hql = "SELECT  jp "
					+ " FROM JulgamentoProcesso jp"
					+ " JOIN jp.sessao s"
					+ " JOIN jp.listaSituacaoJulgamento sj"
					+ " WHERE "
					+ " s.tipoAmbiente = :ambinete "
					+ " AND s.dataPrevistaFim < SYSDATE  "
					+ " AND sj.atual = 'S' "
					+ " AND sj.tipoSituacaoJulgamento.id IN(:situacao1,:situacao2) "
					+ " AND jp.tipoJulgamento IN(:tipoJulgamento)"
					+ " AND s.exclusivoDigital = 'N' "
					+ " ORDER BY jp.sessao.dataInicio";

			Query q = session.createQuery(hql);
			q.setString("ambinete", TipoAmbienteConstante.VIRTUAL.getSigla());
			q.setString("situacao1", TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
			q.setString("situacao2", TipoSitucacaoJulgamentoConstant.EM_ABERTO.getCodigo());
			
			q.setParameterList("tipoJulgamento", Arrays.asList(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL, TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL, TipoIncidenteJulgamento.SIGLA_MERITO, 
					TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO, TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO));

			lista = (List<JulgamentoProcesso>) q.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return lista;

	}

	public List<Ministro> recuperarMinistrosNaoManifesto(Long idObjetoIncidente, Long idSessao) throws DaoException {

		Session session = retrieveSession();
		List<Ministro> listaMinNaoManifesto = null;
		try {

			StringBuilder hql = new StringBuilder("SELECT DISTINCT m FROM Ministro m, EnvolvidoSessao e");
			hql.append(" WHERE 1=1 ");
			hql.append(" AND m.id <> 1 AND m.id = e.ministro.id AND e.sessao.id = :idSessao ");
			hql.append(" AND NOT EXISTS (");
			hql.append(" SELECT vjp.id FROM JulgamentoProcesso jp");
			hql.append(" JOIN jp.listaVotoJulgamentoProcesso vjp");
			hql.append(" WHERE jp.objetoIncidente.principal.id = :idObjetoIncidente");
			hql.append(" AND jp.sessao = e.sessao ");
			hql.append(" AND vjp.ministro.id = m.id )");

			Query q = session.createQuery(hql.toString());

			q.setLong("idObjetoIncidente", idObjetoIncidente);
			q.setLong("idSessao", idSessao);

			listaMinNaoManifesto = (List<Ministro>) q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaMinNaoManifesto;

	}

	public List<AfastamentoMinistroView> recuperarAfastamentoMinistro(
			List<Ministro> ministros, Date datInicio) throws DaoException {

		Session session = retrieveSession();
		List<AfastamentoMinistroView> listaMinNaoManifesto = null;
		try {

			StringBuilder hql = new StringBuilder("");
			hql.append(" SELECT vwam ");
			hql.append("  FROM AfastamentoMinistroView vwam");
			hql.append(" WHERE vwam.matricula in :listaMatMinistro ");
			hql.append(" AND vwam.dataInicio >= to_date( :datInicioAfast , 'DD/MM/YYYY HH24:MI:SS')");

			Query q = session.createQuery(hql.toString());
			List<String> matMinistro = new LinkedList<String>();
			for (Ministro ministro : ministros) {
				if (ministro.getUsuario() != null) {
					matMinistro.add(ministro.getUsuario().getMatricula());
				}
			}
			q.setString("listaMatMinistro",
					SearchData.inClause(matMinistro, false));
			q.setString("datInicioAfast",
					DateTimeHelper.getDataString(datInicio) + " 00:00:00");

			listaMinNaoManifesto = (List<AfastamentoMinistroView>) q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaMinNaoManifesto;

	}

	public List<JulgamentoProcesso> pesquisarRepercussaoGeralRecesso(
			Date dataInicioRecesso, Date dataFimRecesso) throws DaoException {

		Session session = retrieveSession();
		try {

			StringBuilder hql = new StringBuilder("select jp ");
			hql.append(" from JulgamentoProcesso jp ");
			hql.append(" join jp.objetoIncidente oi ");
			hql.append(" join jp.listaSituacaoJulgamento sj ");
			hql.append(" join jp.sessao s ");
			hql.append(" where sj.tipoSituacaoJulgamento.id = :situacao ");
			hql.append(" and sj.atual = 'S' ");
			hql.append(" and jp.tipoJulgamento = :tipoJulgamento ");
			hql.append(" and s.dataPrevistaFim >= to_date(:dataInicioRecesso,'DD/MM/YYYY HH24:MI:SS')");
			hql.append(" and s.dataPrevistaFim <= to_date(:dataFimRecesso,'DD/MM/YYYY HH24:MI:SS')");

			Query q = session.createQuery(hql.toString());

			q.setString("situacao",
					TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
			q.setString("tipoJulgamento",
					TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL);
			q.setString("dataInicioRecesso",
					DateTimeHelper.getDataString(dataInicioRecesso)
							+ " 23:59:59");
			q.setString("dataFimRecesso",
					DateTimeHelper.getDataString(dataFimRecesso) + " 23:59:59");

			return q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	public List<JulgamentoProcesso> processoPendentesMinistroNotificacao(
			Long idMinistro) throws DaoException {
		Session session = retrieveSession();
		try {

			String hql = "SELECT jp " + "  FROM JulgamentoProcesso jp, "
					+ "       SituacaoJulgamento s, " + "       Sessao se"
					+ " WHERE " + " jp.tipoJulgamento = :tipoJulgamento "
					+ " AND jp.sessao.id = se.id"
					+ " AND jp.id = s.julgamentoProcesso.id"
					+ " AND se.tipoAmbiente = :tipoAmbiente"
					+ " AND s.tipoSituacaoJulgamento.id = :situacaoJulgamento "
					+ " AND s.atual = 'S' " + " AND NOT EXISTS( SELECT vj.id "
					+ " 					FROM VotoJulgamentoProcesso vj "
					+ " 				  WHERE vj.julgamentoProcesso.id = jp.id "
					+ " 				  AND vj.ministro.id = :idMinistro "
					+ " 				  AND vj.tipoSituacaoVoto = :situacaoVoto )"
					+ " ORDER BY se.dataPrevistaInicio ";

			Query q = session.createQuery(hql);

			q.setString("situacaoJulgamento",
					TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo());
			q.setString("tipoJulgamento",
					TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL);
			q.setString("situacaoVoto", TipoSituacaoVoto.VALIDO.getSigla());
			q.setString("tipoAmbiente",
					TipoAmbienteConstante.VIRTUAL.getSigla());
			q.setLong("idMinistro", idMinistro);

			return q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public List<Ministro> recuperarMinistroSemManifestacaoComTexto(
			Long idIncidenteJulgamento) throws DaoException {
		Session session = retrieveSession();
		try {

			String hql = "SELECT t.ministro "
					+ "  FROM JulgamentoProcesso jp, "
					+ "       Texto t "
					+ " WHERE jp.objetoIncidente.id = t.objetoIncidente.id "
					+ "   AND jp.objetoIncidente.id = :idIncidenteJulgamento "
					+ "   AND t.tipoTexto = :tipoTexto "

					+ "   AND t.tipoFaseTextoDocumento in (:codigosFasesTexto) " // /retorna
																					// somente
																					// manifestações
																					// assinadas,
																					// liberadas
																					// para
																					// publicação,
																					// publicadas
																					// e
																					// juntadas.

					+ "   AND t.ministro.id <> :ministroPresidente"
					+ "   AND NOT EXISTS ( " + "          SELECT vjp.id "
					+ "            FROM VotoJulgamentoProcesso vjp "
					+ "           WHERE vjp.julgamentoProcesso.id = jp.id "
					+ "             AND vjp.ministro.id = t.ministro.id "
					+ "             AND vjp.tipoSituacaoVoto = :situacaoVoto)";

			Query q = session.createQuery(hql);
			q.setLong("idIncidenteJulgamento", idIncidenteJulgamento);
			q.setParameter("tipoTexto",
					TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL.getCodigo());

			// Retorna somente manifestações assinadas, liberadas para
			// publicação, publicadas e juntadas.
			Collection<Long> codigosPermitidos = new ArrayList<Long>();
			codigosPermitidos.add(FaseTexto.ASSINADO.getCodigoFase());
			codigosPermitidos
					.add(FaseTexto.LIBERADO_PUBLICACAO.getCodigoFase());
			codigosPermitidos.add(FaseTexto.PUBLICADO.getCodigoFase());
			codigosPermitidos.add(FaseTexto.JUNTADO.getCodigoFase());

			q.setParameterList("codigosFasesTexto", codigosPermitidos);
			q.setString("situacaoVoto", TipoSituacaoVoto.VALIDO.getSigla());
			q.setLong("ministroPresidente", Ministro.COD_MINISTRO_PRESIDENTE);

			return q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public List<JulgamentoProcesso> recuperarJulgamentoSemManifestacaoComTexto()
			throws DaoException {

		Session session = retrieveSession();
		try {

			StringBuilder hql = new StringBuilder(
					"SELECT jp "
							+ " FROM JulgamentoProcesso jp "
							+ " JOIN jp.listaSituacaoJulgamento sj JOIN FETCH jp.textos t JOIN FETCH jp.objetoIncidente.principal p JOIN FETCH jp.processoListaJulgamento plj JOIN FETCH plj.objetoIncidente oi "
							+ " WHERE sj.atual = 'S' "
							+ " AND sj.tipoSituacaoJulgamento.id = :situacaoJulgamento "
							+ " AND jp.tipoJulgamento = :tipoJulgamento ");

			hql.append(" AND EXISTS ( SELECT t.id "
					+ " 				 FROM JulgamentoProcesso jps, Texto t "
					+ " 			   WHERE jps.objetoIncidente.id = t.objetoIncidente.id "
					+ " 				 AND jps.id = jp.id "
					+ " 			     AND t.tipoTexto = :textoMRG "
					+ " 				 AND NOT EXISTS ( SELECT vjp.id "
					+ "       							FROM VotoJulgamentoProcesso vjp "
					+ "      							  WHERE vjp.julgamentoProcesso.id = jps.id"
					+ "        							AND vjp.tipoSituacaoVoto = :situacaoVoto "
					+ "        							AND vjp.ministro.id = t.ministro.id)) ");

			hql.append(" AND NOT EXISTS ( SELECT d.id "
					+ " 					    FROM DocumentoTexto d"
					+ " 					  WHERE d.texto.objetoIncidente.id = jp.objetoIncidente.id "
					+ " 				        AND d.tipoSituacaoDocumento = :situacaoDocumento "
					+ " 					    AND d.texto.tipoTexto = :textoDRG "
					+ " 					    AND d.texto.publico = 'S')");

			Query q = session.createQuery(hql.toString());

			q.setParameter("textoMRG",
					TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL.getCodigo());
			q.setString("situacaoVoto", TipoSituacaoVoto.VALIDO.getSigla());
			q.setString("situacaoJulgamento",
					TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo());
			q.setString("tipoJulgamento",
					TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL);
			q.setLong("textoDRG",
					TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL.getCodigo());
			q.setLong("situacaoDocumento",
					TipoSituacaoDocumento.GERADO.getCodigo());

			return q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	public List<Usuario> buscaUsuariosNotificacao(String dsc_perfil)
			throws DaoException {

		Session session = retrieveSession();

		try {
			String hql = " SELECT u " + " FROM Perfil per "
					+ " JOIN per.usuarios u  "
					+ " 	WHERE per.descricao = :descricao "
					+ " 	AND u.nome <> :nome ";

			Query q = session.createQuery(hql);

			q.setString("descricao", dsc_perfil);
			q.setString("nome", "AGENDADOREGAB");

			return q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	/**
	 * Método responsavel em buscar o número máximo do tema para poder inserir
	 * um novo tema
	 * 
	 * @author ViniciusK
	 * @throws SQLException
	 * @throws HibernateException
	 */
	public Long buscarMaxNumeroTema() throws DaoException {
		Long numeroMaximo = null;
		try {
			Session session = retrieveSession();
			String hql = " SELECT MAX (t.numeroSequenciaTema) "
					+ " FROM Tema t WHERE t.tipoTema = 1 ";

			numeroMaximo = (Long) session.createQuery(hql).uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return numeroMaximo;
	}

	public String pesquisarDecisaoRGPackage(Long idObjetoIncidente)
			throws DaoException {

		String decisaoRG = "";
		try {

			CallableStatement stmt = null;
			Session session = retrieveSession();

			Connection connection = session.connection();
			stmt = connection.prepareCall("{?=call INTERNET.PKG_REPERCUSSAO_GERAL.FNC_CONSULTA_SITUACAO_RG (?)}");
			stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
			stmt.setLong(2, idObjetoIncidente);			
			stmt.execute();
			decisaoRG = stmt.getString(1);			
			stmt.close();
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		}

		return decisaoRG;
	}
	
	public Long pesquisarManifestacaoRGPackage(String siglaClasse, Long numeroProcesso, Long objetoIncidente) throws DaoException {

	Long idTexto = 0L;
try {

	CallableStatement stmt = null;
	Session session = retrieveSession();

	Connection connection = session.connection();
	stmt = connection.prepareCall("{?=call INTERNET.PKG_REPERCUSSAO_GERAL.FNC_OBTER_MANIFESTACAO(?,?,?,?)}");
	stmt.registerOutParameter(1, java.sql.Types.BIGINT);	
	stmt.setLong(2, objetoIncidente);
	stmt.setLong(3, numeroProcesso);
	stmt.setString(4, siglaClasse);
	stmt.setString(5, "N");
	stmt.execute();
	idTexto = stmt.getLong(1);			
	stmt.close();
	
} catch (HibernateException e) {
	throw new DaoException("HibernateException",
			SessionFactoryUtils.convertHibernateAccessException(e));
} catch (RuntimeException e) {
	throw new DaoException("RuntimeException", e);
} catch (SQLException e) {
	throw new DaoException("SQLException", e);
}

return idTexto;
}	
	
}