package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.ControleVotoDao;
import br.gov.stf.estf.documento.model.util.ControleVotoDto;
import br.gov.stf.estf.documento.model.util.ControleVotoDynamicQuery;
import br.gov.stf.estf.documento.model.util.ControleVotoDynamicRestriction;
import br.gov.stf.estf.documento.model.util.IConsultaDeControleDeVotoInteiroTeor;
import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoConfidencialidade;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.util.query.QueryBuilder;
import br.gov.stf.framework.model.util.query.SQLOrder;

@Repository
public class ControleVotoDaoHibernate extends GenericHibernateDao<ControleVoto, Long> implements ControleVotoDao {

	private static final long serialVersionUID = -4958664420772533712L;
	Log log = LogFactory.getLog(ControleVotoDaoHibernate.class);

	public ControleVotoDaoHibernate() {
		super(ControleVoto.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ControleVoto> pesquisarControleVoto(ControleVotoDynamicRestriction consulta) throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(ControleVoto.class,
				ControleVotoDynamicRestriction.ALIAS_CONTROLE_VOTO);
		consulta.addCriteriaRestrictions(criteria);
		return criteria.list();
	}

	public List<Object[]> pesquisar(ControleVotoDto controleVotoDto) throws DaoException {
		//funcao utilizada também no processamento ao lancar andamento 8219 verificar acordao pendentes de publicacao 

		List<Object[]> votos = null;
		try {

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cv, dt FROM ControleVoto cv, DocumentoTexto dt ");
			hql.append(" JOIN FETCH cv.objetoIncidente oi ");
			hql.append(" LEFT JOIN FETCH cv.textoControleVoto t ");
			hql.append(" WHERE 1 = 1 ");

			if (controleVotoDto.getTipoTexto() != null)
				hql.append(" AND cv.tipoTexto = :tipoTexto ");

			if (controleVotoDto.isDocAtivos()) {
				hql.append(" AND cv.tipoSituacaoTexto <> :tipoSituacaoTexto ");
			}

			if (controleVotoDto.isJulgamentoFinalizado() || controleVotoDto.isRepercussaoGeral()) {
				hqlRestricaoObjetoIncidenteComEmenta(controleVotoDto, hql);
			}
			hqlRestricaoSemAcaoApenasUmMinistro(controleVotoDto, hql);
			hqlRestricaoComTodosOsMinistros(controleVotoDto, hql);

			hql.append(" AND t = dt.texto (+) ");
			hql.append(" AND dt.tipoSituacaoDocumento (+) not in (:situacaoDocumento) ");
			
			//hql.append(" AND cv.objetoIncidente = ap.objetoIncidente(+) ");
			//hql.append(" AND ap.codigoAndamento(+) IN (:codigosAndamento) ");

			if (controleVotoDto.getTipoProcesso() != null) {
				if (controleVotoDto.getTipoProcesso().equals("e")) {
					hql.append(" AND cv.objetoIncidente.principal.tipoMeio = 'E' ");
				}
				if (controleVotoDto.getTipoProcesso().equals("f")) {
					hql.append(" AND NVL(cv.objetoIncidente.principal.tipoMeio,'F') = 'F' ");
				}
			}
			
			if (controleVotoDto.getSegredoDeJustica() != null && !controleVotoDto.getSegredoDeJustica().equals("")) {
				if (controleVotoDto.getSegredoDeJustica().equals("S")) {
					hql.append(" AND cv.objetoIncidente.tipoConfidencialidade = :tipoConfidencialidade ");
				} else if (controleVotoDto.getSegredoDeJustica().equals("N")) {
					hql.append(
							" AND (cv.objetoIncidente.tipoConfidencialidade <> :tipoConfidencialidade OR cv.objetoIncidente.tipoConfidencialidade IS NULL) ");
				}
			}
			
			if( controleVotoDto.getCvAnotacoes() != null && !controleVotoDto.getCvAnotacoes().equals("")) {
				hql.append(" AND lower(cv.anotacoes) like :anotacoes ");
			}
			
			if(controleVotoDto.getIdObjetoIncidente() != null) {
				hql.append("AND cv.objetoIncidente = :idObjetoIncidente ");		
			}
		
//			if (controleVotoDto.getConsultaObjetoIncidente().isJoin()) {
//				 hql.append(controleVotoDto.getConsultaObjetoIncidente().getQueryObjetoIncidente("cv.objetoIncidente"));
//
//				if (verificarAndamentosDuplicados(controleVotoDto.getConsultaObjetoIncidente().getObjetoIncidente())) {
//					hql.append(
//							" AND ap.id = (SELECT max(apr.id) FROM AndamentoProcesso apr WHERE apr.codigoAndamento (+) IN (:codigosAndamento) ");
//					hql.append(" AND apr.objetoIncidente.id = "
//							+ controleVotoDto.getConsultaObjetoIncidente().getObjetoIncidente() + ")");
//				}
//			}

			if ("menor".equals(controleVotoDto.getOperadorDataSessao())) {
				hql.append(" AND cv.dataSessao < :dataSessaoMenor ");
			} else if ("igual a".equals(controleVotoDto.getOperadorDataSessao())) {
				hql.append(" AND cv.dataSessao between :dataSessaoMenor and :dataSessaoMaior ");
			} else if ("entre".equals(controleVotoDto.getOperadorDataSessao())) {
				hql.append(" AND cv.dataSessao between :dataSessaoMenor and :dataSessaoMaior ");
			}

			if (controleVotoDto.getCodigoMinistro() != null && controleVotoDto.getCodigoMinistro() > 0) {
				hql.append(" AND cv.ministro.id = :codigoMinistro ");
			}

			if (controleVotoDto.getOperadorDataGenerica() != null) {
				if ("menor".equals(controleVotoDto.getOperadorDataGenerica())) {
					if (controleVotoDto.isLiberado()) {
						hql.append(" AND cv.dataPublico < :dataGenericaMenor ");
					}
					if (controleVotoDto.isRecebido()) {
						hql.append(" AND cv.dataRecebimentoGab < :dataGenericaMenor ");
					}
					if (controleVotoDto.isPublicado()) {
						hql.append(" AND cv.objetoIncidente.dataPublicacao < :dataGenericaMenor ");
					}
				}
				if ("entre".equals(controleVotoDto.getOperadorDataGenerica())) {
					if (controleVotoDto.isLiberado()) {
						hql.append(" AND cv.dataPublico between :dataGenericaMenor and :dataGenericaMaior ");
					}
					if (controleVotoDto.isRecebido()) {
						hql.append(" AND cv.dataRecebimentoGab between :dataGenericaMenor and :dataGenericaMaior ");
					}
					if (controleVotoDto.isPublicado()) {
						hql.append(" AND cv.objetoIncidente.dataPublicacao between :dataGenericaMenor and :dataGenericaMaior ");
					}
				}
				if ("tudo".equals(controleVotoDto.getOperadorDataGenerica())) {
					if (controleVotoDto.isLiberado()) {
						hql.append(" AND cv.dataPublico is not null ");
					}
					if (controleVotoDto.isRecebido()) {
						hql.append(" AND cv.dataRecebimentoGab is not null ");
					}
					if (controleVotoDto.isPublicado()) {
						hql.append(" AND cv.objetoIncidente.dataPublicacao is not null ");
					}
				}
			}

			if (controleVotoDto.isNaoLiberado()) {
				hql.append(" AND cv.dataPublico is null ");
			}

			if (controleVotoDto.isNaoRecebido()) {
				hql.append(" AND cv.dataRecebimentoGab is null ");
			}

			if (controleVotoDto.isNaoPublicado()) {
				hql.append(" AND cv.objetoIncidente.dataPublicacao is null ");
			}

			if (controleVotoDto.getCodigoColegiado() != null) {
				hql.append(" AND cv.sessao = :codigoTipoSessao ");
			}
			
			if(controleVotoDto.getExclusivoDigital() != null) {
				hql.append("AND cv.sessaoJulgamento.exclusivoDigital = :exclusivoDigital");
			}
			
			if(controleVotoDto.getExtratoAta() != null) {
				extratoAta(controleVotoDto, hql);
			}

			hqlRestricaoControleVotoCompleto(controleVotoDto, hql);

			if (controleVotoDto.getTipoAmbientePersonalizado() != null){
				if(controleVotoDto.getTipoAmbientePersonalizado().equals("P")) {
					ambientePresencial(controleVotoDto, hql);
				}
			}
			
			if (controleVotoDto.getTipoAmbientePersonalizado() != null){
				if(controleVotoDto.getTipoAmbientePersonalizado().equals("VU")) {
					ambienteVirtualUnanime(controleVotoDto, hql);
				}
			}
			if (controleVotoDto.getTipoAmbientePersonalizado() != null){
				if(controleVotoDto.getTipoAmbientePersonalizado().equals("VNU")) {
					ambienteVirtualNaoUnanime(controleVotoDto, hql);
				}
			}
			
			if (controleVotoDto.getInteiroTeorGerado() != null) {
				if (controleVotoDto.getInteiroTeorGerado() == Boolean.TRUE)
					hql.append(" AND EXISTS ");
				else
					hql.append(" AND NOT EXISTS ");

				hql.append(
						" (SELECT FROM PecaProcessoEletronico ppe WHERE ppe.objetoIncidente = cv.objetoIncidente AND ppe.tipoPecaProcesso.id = "
								+ TipoPecaProcesso.CODIGO_INTEIRO_TEOR + " AND ppe.tipoSituacaoPeca <> "
								+ TipoSituacaoPeca.EXCLUIDA.getCodigo() + ") ");
			}

			hqlRestricaoLeadingCase(controleVotoDto, hql);
			
			hql.append(" ORDER BY cv.objetoIncidente.id ASC, cv.sequenciaVoto ASC ");

			//Query q = controleVotoDto.getConsultaObjetoIncidente().createQuery(session, hql.toString());
			
			Query q = retrieveSession().createQuery(hql.toString());

			if (controleVotoDto.isJulgamentoFinalizado()) {
				q.setParameter("tipoTextoEmenta", TipoTexto.CODIGO_EMENTA);
				q.setParameter("tipoTextoEmentaRG", TipoTexto.CODIGO_EMENTA_SOBRE_REPERCUSSAO_GERAL);
			}
			if (controleVotoDto.isRepercussaoGeral()) {
				q.setParameter("tipoTextoEmentaRG", TipoTexto.CODIGO_EMENTA_SOBRE_REPERCUSSAO_GERAL);
			}
			
			if (controleVotoDto.getSegredoDeJustica() != null && (controleVotoDto.getSegredoDeJustica().equals("S")  || controleVotoDto.getSegredoDeJustica().equals("N"))) {
				q.setParameter("tipoConfidencialidade", TipoConfidencialidade.SEGREDO_JUSTICA.getCodigo());
			}

			if(controleVotoDto.getCvAnotacoes() != null &&  !controleVotoDto.getCvAnotacoes().equals("")) {
				q.setParameter("anotacoes", "%"+ controleVotoDto.getCvAnotacoes().toLowerCase() +"%");
			}
			
			if(controleVotoDto.getExclusivoDigital() != null) {
				q.setParameter("exclusivoDigital", controleVotoDto.getExclusivoDigital());
			}
			
			if(controleVotoDto.getIdObjetoIncidente() != null) {
				q.setParameter("idObjetoIncidente", controleVotoDto.getIdObjetoIncidente());
			}

			Long[] situacoesDocumento = { TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO.getCodigo(),
					TipoSituacaoDocumento.CANCELADO_AUTOMATICAMENTE.getCodigo() };
			q.setParameterList("situacaoDocumento", situacoesDocumento);

			if (controleVotoDto.getTipoTexto() != null)
				q.setParameter("tipoTexto", controleVotoDto.getTipoTexto());

			if (controleVotoDto.isDocAtivos()) {
				q.setParameter("tipoSituacaoTexto", TipoSituacaoTexto.CANCELADO.getCodigo());
			}

		//	Long[] codigosAndamento = { 7900L/* , 7915L */ };
		//	q.setParameterList("codigosAndamento", codigosAndamento);

			if ("menor".equals(controleVotoDto.getOperadorDataSessao())) {
				q.setDate("dataSessaoMenor", dataFinalTratada(controleVotoDto.getDataSessaoMenor()));
			} else if ("igual a".equals(controleVotoDto.getOperadorDataSessao())) {
				q.setDate("dataSessaoMenor", dataInicialTratada(controleVotoDto.getDataSessaoMenor()));
				q.setDate("dataSessaoMaior", dataFinalTratada(controleVotoDto.getDataSessaoMenor()));
			} else if ("entre".equals(controleVotoDto.getOperadorDataSessao())) {
				q.setDate("dataSessaoMenor", dataInicialTratada(controleVotoDto.getDataSessaoMenor()));
				q.setDate("dataSessaoMaior", dataFinalTratada(controleVotoDto.getDataSessaoMaior()));
			}

			if (controleVotoDto.getDataGenericaMenor() != null) {
				if (controleVotoDto.isLiberado() || controleVotoDto.isRecebido() || controleVotoDto.isPublicado() || controleVotoDto.isJulgamentoFinalizado()) {
					if ("menor".equals(controleVotoDto.getOperadorDataGenerica())) {
						q.setDate("dataGenericaMenor", dataFinalTratada(controleVotoDto.getDataGenericaMenor()));
					} else if ("igual a".equals(controleVotoDto.getOperadorDataGenerica())) {
						q.setDate("dataGenericaMenor", dataInicialTratada(controleVotoDto.getDataGenericaMenor()));
						q.setDate("dataGenericaMaior", dataFinalTratada(controleVotoDto.getDataGenericaMenor()));
					} else if ("entre".equals(controleVotoDto.getOperadorDataGenerica())) {
						q.setDate("dataGenericaMenor", dataInicialTratada(controleVotoDto.getDataGenericaMenor()));
						q.setDate("dataGenericaMaior", dataFinalTratada(controleVotoDto.getDataGenericaMaior()));
					}
				}
			}

			if (controleVotoDto.getCodigoMinistro() != null && controleVotoDto.getCodigoMinistro() > 0) {
				q.setLong("codigoMinistro", controleVotoDto.getCodigoMinistro());
			}

			if (controleVotoDto.getCodigoColegiado() != null) {
				if ("1T".contains(controleVotoDto.getCodigoColegiado())) {
					q.setParameter("codigoTipoSessao", TipoSessaoControleVoto.PRIMEIRA_TURMA.getCodigo());
				}
				if ("2T".contains(controleVotoDto.getCodigoColegiado())) {
					q.setParameter("codigoTipoSessao", TipoSessaoControleVoto.SEGUNDA_TURMA.getCodigo());
				}
				if ("TP".contains(controleVotoDto.getCodigoColegiado())) {
					q.setParameter("codigoTipoSessao", TipoSessaoControleVoto.PLENARIO.getCodigo());
				}
			}
			
			q.setMaxResults(4000);
			q.setTimeout(900); // 5 minutos
			votos = q.list();
		} catch (GenericJDBCException e) {
			throw new DaoException(
					"Excedido limite de tempo para a realização da pesquisa. Utilize filtros a fim de diminuir a quantidade de registros a serem recuperados.");
//		} catch (SQLTimeoutException e) {
//			throw new DaoException(
//					"Excedido limite de tempo para a realização da pesquisa. Utilize filtros a fim de diminuir a quantidade de registros a serem recuperados.");
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return votos;
	}

	private void extratoAta(ControleVotoDto controleVotoDto, StringBuffer hql) {
		if (controleVotoDto.getExtratoAta().equals("S"))
			hql.append(" AND ");
		else
			hql.append(" AND NOT ");
		
		hql.append("EXISTS ");
		hql.append(" (SELECT  ");
		hql.append("    FROM DocumentoTexto dtt ");
		hql.append("   WHERE dtt.tipoDocumentoTexto.id = 50 AND dtt.tipoSituacaoDocumento IN (3, 11) ");
		hql.append("         AND dtt.texto IN ");
		hql.append("                 (SELECT MAX (te) ");
		hql.append("                    FROM Texto te ");
		hql.append("                   WHERE te.objetoIncidente = cv.objetoIncidente ");
		hql.append("                         AND te.tipoTexto IN (50, 55))) ");
		
	}			

	private void ambienteVirtualNaoUnanime(ControleVotoDto controleVotoDto, StringBuffer hql) {
		// Julgamento Virtual
		hql.append(" AND EXISTS ");
		hql.append("    (SELECT ");
		hql.append(" 	FROM ");
		hql.append(" 	   Sessao s, JulgamentoProcesso jp ");
		hql.append(" 	WHERE ");
		hql.append(" 	   s.id = jp.sessao ");
		hql.append(" 	   AND s.tipoJulgamentoVirtual = 2 ");
		hql.append(" 	   AND jp.objetoIncidente = cv.objetoIncidente) ");
		// Presencial e Repercussão Geral
		hql.append(" AND NOT EXISTS ");
		hql.append("    (SELECT ");
		hql.append(" 	FROM ");
		hql.append(" 	   Sessao s, JulgamentoProcesso jp ");
		hql.append(" 	WHERE ");
		hql.append(" 	   s.id = jp.sessao ");
		hql.append(" 	   AND (s.tipoJulgamentoVirtual IS NULL OR s.tipoJulgamentoVirtual = 1) ");
		hql.append(" 	   AND jp.objetoIncidente = cv.objetoIncidente) ");
		// Lista Presencial
		hql.append(" AND NOT EXISTS ");
		hql.append("    (SELECT ");
		hql.append(" 	FROM ");
		hql.append(" 	   Sessao s, ListaJulgamento lj, ProcessoListaJulgamento plj ");
		hql.append(" 	WHERE ");
		hql.append(" 	   s.id = lj.sessao ");
		hql.append(" 	   AND lj.id = plj.listaJulgamento ");
		hql.append(" 	   AND s.tipoAmbiente = 'F' ");
		hql.append(" 	   AND plj.objetoIncidente = cv.objetoIncidente) ");
		// Tem ementa, acórdão, relatório e voto e nada mais
		hql.append(" AND NOT (EXISTS ");
		hql.append("         (SELECT ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto IN (80)) ");
		hql.append(" AND EXISTS ");
		hql.append("         (SELECT ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto IN (70)) ");
		hql.append(" AND EXISTS ");
		hql.append("         (SELECT ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto IN (100)) ");
		hql.append(" AND EXISTS ");
		hql.append("         (SELECT ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto IN (200)) ");
		hql.append(" AND (SELECT COUNT (*) ");
		hql.append("        FROM ControleVoto vv ");
		hql.append("       WHERE vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("             AND vv.tipoSituacaoTexto <> 2) = 4 ");
		// relator e redator é o mesmo ministro
		hql.append(" AND (SELECT vv.ministro.id ");
		hql.append("        FROM ControleVoto vv ");
		hql.append("       WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("             AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("             AND vv.tipoTexto = 100) = ");
		hql.append("         (SELECT vv.ministro.id ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto = 80)) ");
	}

	private void ambientePresencial(ControleVotoDto controleVotoDto, StringBuffer hql) {
		//Presencial e Repercussão Geral ");
		hql.append(" AND (EXISTS ");
		hql.append("    (SELECT ");
		hql.append(" 	FROM ");
		hql.append(" 	   Sessao s, JulgamentoProcesso jp ");
		hql.append(" 	WHERE ");
		hql.append(" 	   s.id = jp.sessao ");
		hql.append(" 	   AND (s.tipoJulgamentoVirtual IS NULL OR s.tipoJulgamentoVirtual = 1) ");
		hql.append(" 	   AND jp.objetoIncidente = cv.objetoIncidente) ");
		//Lista Presencial
		hql.append("	OR EXISTS ");
		hql.append("    (SELECT ");
		hql.append(" 	FROM ");
		hql.append(" 	   Sessao s, ListaJulgamento lj, ProcessoListaJulgamento plj ");
		hql.append(" 	WHERE ");
		hql.append(" 	   s.id = lj.sessao ");
		hql.append(" 	   AND lj.id = plj.listaJulgamento ");
		hql.append(" 	   AND s.tipoAmbiente = 'F' ");
		hql.append(" 	   AND plj.objetoIncidente = cv.objetoIncidente)) ");
	}

	private void ambienteVirtualUnanime(ControleVotoDto controleVotoDto, StringBuffer hql) {
		//Julgamento Virtual
		hql.append(" AND (EXISTS ");
		hql.append("    (SELECT ");
		hql.append(" 	FROM ");
		hql.append(" 	   Sessao s, JulgamentoProcesso jp ");
		hql.append(" 	WHERE ");
		hql.append(" 	   s.id = jp.sessao ");
		hql.append(" 	   AND s.tipoJulgamentoVirtual = 2 ");
		hql.append(" 	   AND jp.objetoIncidente = cv.objetoIncidente) ");
		//Presencial e Repercussão Geral
		hql.append(" AND NOT EXISTS ");
		hql.append("    (SELECT ");
		hql.append(" 	FROM ");
		hql.append(" 	   Sessao s, JulgamentoProcesso jp ");
		hql.append(" 	WHERE ");
		hql.append(" 	   s.id = jp.sessao ");
		hql.append(" 	   AND (s.tipoJulgamentoVirtual IS NULL OR s.tipoJulgamentoVirtual = 1) ");
		hql.append(" 	   AND jp.objetoIncidente = cv.objetoIncidente) ");
		//Lista Presencial
		hql.append(" AND NOT EXISTS ");
		hql.append("    (SELECT ");
		hql.append(" 	FROM ");
		hql.append(" 	   Sessao s, ListaJulgamento lj, ProcessoListaJulgamento plj ");
		hql.append(" 	WHERE ");
		hql.append(" 	   s.id = lj.sessao ");
		hql.append(" 	   AND lj.id = plj.listaJulgamento ");
		hql.append(" 	   AND s.tipoAmbiente = 'F' ");
		hql.append(" 	   AND plj.objetoIncidente = cv.objetoIncidente) ");
		// Tem ementa, acórdão, relatório e voto e nada mais
		hql.append(" AND EXISTS ");
		hql.append("         (SELECT ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto IN (80)) ");
		hql.append(" AND EXISTS ");
		hql.append("         (SELECT ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto IN (70)) ");
		hql.append(" AND EXISTS ");
		hql.append("         (SELECT ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto IN (100)) ");
		hql.append(" AND EXISTS ");
		hql.append("         (SELECT ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto IN (200)) ");
		hql.append(" AND (SELECT COUNT (*) ");
		hql.append("        FROM ControleVoto vv ");
		hql.append("       WHERE vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("             AND vv.tipoSituacaoTexto <> 2) = 4 ");
		// relator e redator é o mesmo ministro
		hql.append(" AND (SELECT vv.ministro.id ");
		hql.append("        FROM ControleVoto vv ");
		hql.append("       WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("             AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("             AND vv.tipoTexto = 100) = ");
		hql.append("         (SELECT vv.ministro.id ");
		hql.append("            FROM ControleVoto vv ");
		hql.append("           WHERE     vv.objetoIncidente = cv.objetoIncidente ");
		hql.append("                 AND vv.tipoSituacaoTexto <> 2 ");
		hql.append("                 AND vv.tipoTexto = 80)) ");
	}
	
	private void hqlRestricaoLeadingCase(ControleVotoDto controleVotoDto, StringBuffer hql) {
		if(controleVotoDto.isLeadingCase()) {
			hql.append(" and exists ("); 
			hql.append("        select"); 
			hql.append("            proc_tema.tipoOcorrencia"); 
			hql.append("        FROM");
			hql.append("            ProcessoTema as proc_tema,"); 
			hql.append("            Processo as proc,");
			hql.append("            TipoOcorrencia as tipo_oc");
			hql.append("        WHERE");
			hql.append("            proc_tema.siglaClasse = proc.siglaClasseProcessual"); 
			hql.append("            AND   proc_tema.numProcesso = proc.numeroProcessual");
			hql.append("            AND   proc_tema.tipoOcorrencia = tipo_oc.id");
			hql.append("            AND   tipo_oc.id = ");
			hql.append(TipoOcorrencia.TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo()); 
			hql.append("            AND   proc.id = cv.seqObjetoIncidente"); 
			hql.append(" )");
		}
	}

	private void hqlRestricaoControleVotoCompleto(ControleVotoDto controleVotoDto, StringBuffer hql) {
		if (controleVotoDto.isControleVotosCompleto()) {
			hql.append(" AND cv.dataPublico IS NOT NULL ");
			hql.append(" AND cv.dataRecebimentoGab IS NOT NULL ");
			hql.append(" AND cv.objetoIncidente NOT IN ("
					+ "SELECT cvTemp.objetoIncidente FROM ControleVoto cvTemp "
					+ "WHERE cvTemp.objetoIncidente = cv.objetoIncidente AND cvTemp.tipoSituacaoTexto <> " + TipoSituacaoTexto.CANCELADO.getCodigo() + " AND (cvTemp.dataPublico IS NULL OR cvTemp.dataRecebimentoGab IS NULL)) ");
			
			if (controleVotoDto.isRepercussaoGeral()) {
				// O processo deve possuir Ementa de Repercussão Geral
				hql.append(" AND EXISTS ( "
						+ " SELECT SYSDATE FROM ControleVoto cvTemp "
						+ " WHERE cvTemp.objetoIncidente = cv.objetoIncidente AND (cvTemp.tipoTexto = " + TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL.getCodigo() + " AND cvTemp.texto IS NOT NULL)) ");
				
				// O processo deve possuir Decisão de Repercussão Geral
				hql.append(" AND EXISTS ( "
						+ " SELECT SYSDATE FROM ControleVoto cvTemp "
						+ " WHERE cvTemp.objetoIncidente = cv.objetoIncidente AND (cvTemp.tipoTexto = " + TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL.getCodigo() + " AND cvTemp.texto IS NOT NULL)) ");
			} else {


				// O processo deve possuir Acórdão
				hql.append(" AND EXISTS ( "
						+ " SELECT SYSDATE FROM ControleVoto cvTemp "
						+ " WHERE cvTemp.objetoIncidente = cv.objetoIncidente AND (cvTemp.tipoTexto = " + TipoTexto.ACORDAO.getCodigo() + " AND cvTemp.texto IS NOT NULL)) ");
				
				// O processo deve possuir Relatório
				hql.append(" AND EXISTS ( "
						+ " SELECT SYSDATE FROM ControleVoto cvTemp "
						+ " WHERE cvTemp.objetoIncidente = cv.objetoIncidente AND (cvTemp.tipoTexto = " + TipoTexto.RELATORIO.getCodigo() + " AND cvTemp.texto IS NOT NULL)) ");
				
				// O processo deve possuir Voto
				hql.append(" AND EXISTS ( "
						+ " SELECT SYSDATE FROM ControleVoto cvTemp "
						+ " WHERE cvTemp.objetoIncidente = cv.objetoIncidente AND (cvTemp.tipoTexto = " + TipoTexto.VOTO.getCodigo() + " AND cvTemp.texto IS NOT NULL)) ");
			}
		}
	}

	private void hqlRestricaoTipoAmbiente(ControleVotoDto controleVotoDto, StringBuffer hql) {
		if (controleVotoDto.getTipoAmbiente() == null)
			return;
		hql.append(" AND ");
		if (controleVotoDto.getTipoAmbiente().getSigla().equals(TipoAmbienteConstante.PRESENCIAL.getSigla()))
			hql.append (" NOT ");
		hql.append(" exists (select lj.id FROM ListaJulgamento lj ");
		hql.append(" where lj.sessao.tipoAmbiente = '");
		hql.append(TipoAmbienteConstante.VIRTUAL.getSigla() + "'");

		if (controleVotoDto.isRepercussaoGeral()) {
			hql.append(" and lj.sessao.tipoJulgamentoVirtual = 1 ");
		}
		else{
			hql.append(" and lj.sessao.tipoJulgamentoVirtual = 2 ");				
		}

		hql.append(
				" and exists ( select envolvido.id from EnvolvidoSessao envolvido where lj.sessao.id = envolvido.sessao.id and ( envolvido.ministro.id = cv.ministro.id ");
		if (controleVotoDto.getIsMinistroPresidente() || controleVotoDto.getCodigoMinistro() == null ){
			hql.append(" OR envolvido.ministro.id = " + controleVotoDto.getCodigoMinistroPresidente());
		}
		hql.append(" ))");

		if (controleVotoDto.getCodigoColegiado() != null && controleVotoDto.getTipoAmbiente().getSigla().equals(TipoAmbienteConstante.VIRTUAL.getSigla())) {
			hql.append(" and lj.sessao.colegiado.id = :codigoColegiado ");
		}			


		hql.append(
				" and exists (select processoListaJulgamento.id from ProcessoListaJulgamento processoListaJulgamento  where "
						+ " processoListaJulgamento.listaJulgamento.id = lj.id"
						+ " and processoListaJulgamento.objetoIncidente.id = cv.objetoIncidente.id ) ");

		hql.append(")");		
	}

	private void hqlRestricaoSemAcaoApenasUmMinistro(ControleVotoDto controleVotoDto, StringBuffer hql) {
		if (controleVotoDto.isSemAcaoApenasUmMinistro()) {
			hql.append(" AND cv.objetoIncidente.id IN ");
			hql.append(" (SELECT cvTemp.objetoIncidente.id FROM ControleVoto cvTemp ");

			hql.append(" WHERE 1=1 ");
			if (controleVotoDto.isNaoLiberado()) {
				hql.append(" AND cvTemp.dataPublico is null ");
			}

			if (controleVotoDto.isNaoRecebido()) {
				hql.append(" AND cvTemp.dataRecebimentoGab is null ");
			}

			if (controleVotoDto.isNaoPublicado()) {
				hql.append(" AND cvTemp.objetoIncidente.dataPublicacao is null ");
			}

			hql.append(" GROUP BY cvTemp.objetoIncidente.id ");
			hql.append(" HAVING count(distinct cvTemp.ministro.id) = 1 ) ");
		}
	}

	private void hqlRestricaoComTodosOsMinistros(ControleVotoDto controleVotoDto, StringBuffer hql) {
		if (controleVotoDto.isPorTodosOsMinistros()) {
			hql.append(" AND cv.objetoIncidente.id NOT IN ");
			hql.append(" (SELECT cvTemp.objetoIncidente.id FROM ControleVoto cvTemp ");

			hql.append(" WHERE cv.objetoIncidente.id = cvTemp.objetoIncidente.id ");
			if (controleVotoDto.isLiberado()) {
				hql.append(" AND cvTemp.dataPublico is null ");
			}

			if (controleVotoDto.isRecebido()) {
				hql.append(" AND cvTemp.dataRecebimentoGab is null ");
			}

			if (controleVotoDto.isPublicado()) {
				hql.append(" AND cvTemp.objetoIncidente.dataPublicacao is null ");
			}

			hql.append(" ) ");
		}
	}

	private void hqlRestricaoObjetoIncidenteComEmenta(ControleVotoDto controleVotoDto, StringBuffer hql) {
		if(controleVotoDto.isRepercussaoGeral()){
			hql.append(" AND cv.objetoIncidente.id IN (SELECT cvEmenta.objetoIncidente.id FROM ControleVoto cvEmenta WHERE cvEmenta.objetoIncidente.id = cv.objetoIncidente.id AND cvEmenta.tipoTexto = :tipoTextoEmentaRG) ");
		}
		if(controleVotoDto.isJulgamentoFinalizado()){
			hql.append(" AND cv.objetoIncidente.id IN (SELECT cvEmenta.objetoIncidente.id FROM ControleVoto cvEmenta WHERE cvEmenta.objetoIncidente.id = cv.objetoIncidente.id AND cvEmenta.tipoTexto IN ( :tipoTextoEmenta, :tipoTextoEmentaRG) ");
			
			
			if("menor".equals(controleVotoDto.getOperadorDataGenerica())){
				hql.append(" AND cvEmenta.dataSessao < :dataGenericaMenor ");
			}
			if("entre".equals(controleVotoDto.getOperadorDataGenerica())){
				hql.append(" AND cvEmenta.dataSessao between :dataGenericaMenor and :dataGenericaMaior ");
			}		
			
			hql.append(" ) ");
		}
	}

	private java.sql.Date dataInicialTratada(Date dataInicial) {
		Calendar data = Calendar.getInstance();
		data.setTime(dataInicial);
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.SECOND, 0);
		data.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(data.getTime().getTime());
	}

	private java.sql.Date dataFinalTratada(Date dataFinal) {
		Calendar data = Calendar.getInstance();
		data.setTime(dataFinal);
		data.set(Calendar.HOUR_OF_DAY, 23);
		data.set(Calendar.MINUTE, 59);
		data.set(Calendar.SECOND, 59);
		data.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(data.getTime().getTime());
	}

	@Override
	public List<ControleVoto> pesquisarControleVoto(Long sequencialObjetoIncidente, Long idMinistro, Date dataSessao,
			TipoTexto tipoTexto, Long idTexto) throws DaoException {

		ControleVotoDynamicRestriction controleVotoDynamicRestriction = new ControleVotoDynamicRestriction();
		controleVotoDynamicRestriction.setSequencialObjetoIncidente(sequencialObjetoIncidente);
		controleVotoDynamicRestriction.setCodigoDoMinistro(idMinistro, true);
		controleVotoDynamicRestriction.setDataSessao(dataSessao);
		controleVotoDynamicRestriction.setTipoTexto(tipoTexto);
		controleVotoDynamicRestriction.setIdTexto(idTexto);

		return pesquisarControleVoto(controleVotoDynamicRestriction);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ControleVoto> pesquisarControleVoto(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter,
			boolean pesquisarObjetosDoProcesso, boolean filtrarPeloSetorComposicaoAcordao) throws DaoException {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT oEntidade FROM ControleVoto oEntidade, DocumentoTexto dt, Texto t ");
		hql.append("WHERE 1=1 ");
		hql.append("  AND t.objetoIncidente = oEntidade.objetoIncidente ");
		hql.append("  AND t.ministro = oEntidade.ministro ");
		hql.append("  AND t.tipoTexto = oEntidade.tipoTexto ");
		hql.append("  AND t.sequenciaVoto = oEntidade.sequenciaVoto ");
		hql.append("  AND t.dataSessao = oEntidade.dataSessao ");
		hql.append("  AND t.id = dt.texto.id  ");
		hql.append("  AND oEntidade.tipoSituacaoTexto = :tipoSituacaoTexto ");
		hql.append("  AND oEntidade.dataPublico is not null ");
		hql.append("  AND dt.tipoSituacaoDocumento IN  (:tiposSituacaoDocumento) ");
		hql.append("  AND oEntidade.seqObjetoIncidente = :objetoIncidente ");
		hql.append("ORDER BY oEntidade.sequenciaVoto ASC, ");
		hql.append("  oEntidade.dataSessao ASC");

		Query query = retrieveSession().createQuery(hql.toString());

		query.setLong("tipoSituacaoTexto", controleVotoSearchFilter.getTipoSituacaoTexto().getCodigo());
		query.setParameterList("tiposSituacaoDocumento",
				Arrays.asList(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo(),
						TipoSituacaoDocumento.ASSINADO_MANUALMENTE.getCodigo()));
		query.setLong("objetoIncidente", controleVotoSearchFilter.getIdObjetoIncidente());

		List list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ControleVoto> pesquisarControleVoto2(ControleVotoDynamicQuery consultaDinamica) throws DaoException {
		StringBuffer hql = montaHqlDeConsultaDeControleVoto();
		consultaDinamica.addOrder(ControleVotoDynamicQuery.ALIAS_CONTROLE_VOTO + ".dataSessao", SQLOrder.ASCENDENTE);
		consultaDinamica.addOrder(ControleVotoDynamicQuery.ALIAS_CONTROLE_VOTO + ".sequenciaVoto", SQLOrder.ASCENDENTE);
		QueryBuilder builder = new QueryBuilder(retrieveSession(), hql.toString(), consultaDinamica);
		Query query = builder.getQuery();
		return query.list();
	}

	private StringBuffer montaHqlDeConsultaDeControleVoto() {
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT ");
		hql.append(ControleVotoDynamicQuery.ALIAS_CONTROLE_VOTO);
		hql.append(" FROM ");
		hql.append(ControleVoto.class.getSimpleName());
		hql.append(" ");
		hql.append(ControleVotoDynamicQuery.ALIAS_CONTROLE_VOTO);
		hql.append(" ");
		return hql;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ControleVoto> pesquisarControleVoto(ObjetoIncidente<?> objetoIncidente, Ministro ministro,
			Date dataSessao) throws DaoException {
		List<ControleVoto> votos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ControleVoto.class);
			if (objetoIncidente != null) {
				c.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));
			}
			if (ministro != null) {
				c.add(Restrictions.eq("ministro.id", ministro.getId()));
			}
			if (dataSessao != null) {
				c.add(Restrictions.eq("dataSessao", dataSessao));
			}

			c.addOrder(Order.asc("sequenciaVoto"));

			votos = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return votos;
	}

	public Boolean verificarAndamentosDuplicados(Long objetoIncidente) throws DaoException {
		Integer cont = null;
		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();

			sql.append("SELECT NVL (COUNT (seq_andamento_processo),0) as qtde");
			sql.append("  FROM stf.andamento_processos ap");
			sql.append(" WHERE ap.cod_andamento = :cod_andamento");
			sql.append(" AND ap.seq_objeto_incidente = :seq_objeto_incidente");

			PreparedStatement ps = session.connection().prepareStatement(sql.toString());
			ps.setLong(1, 7900L);
			ps.setLong(2, objetoIncidente);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				cont = rs.getInt("qtde");
			}

			rs.close();
			ps.close();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		if (cont > 1) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long recuperarUltimaSequenciaVoto(ObjetoIncidente objetoIncidente) throws DaoException {
		Long seq = null;
		try {
			Session session = retrieveSession();
			ObjetoIncidente<?> recurso = objetoIncidente;
			while (!(recurso instanceof Processo || recurso instanceof RecursoProcesso)) {
				recurso = recurso.getPai();
			}

			StringBuffer sql = new StringBuffer();

			sql.append("    SELECT MAX (cv.seq_voto) seq_voto ");
			sql.append("      FROM ( ");
			sql.append("    SELECT seq_objeto_incidente ");
			sql.append("      FROM judiciario.objeto_incidente oi ");
			sql.append("START WITH oi.seq_objeto_incidente = ? ");
			sql.append("CONNECT BY PRIOR oi.seq_objeto_incidente = oi.seq_objeto_incidente_pai ");
			sql.append("       AND oi.tip_objeto_incidente = ?");
			sql.append("           ) a ");
			sql.append("INNER JOIN stf.controle_votos cv ");
			sql.append("        ON cv.seq_objeto_incidente = a.seq_objeto_incidente ");

			PreparedStatement ps = session.connection().prepareStatement(sql.toString());
			ps.setLong(1, recurso.getId()); // seq_objeto_incidente
			ps.setString(2, "IJ"); // tip_objeto_incidente

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				seq = rs.getLong("seq_voto");
			}

			rs.close();
			ps.close();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return seq;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ControleVoto recuperar(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro)
			throws DaoException {
		ControleVoto voto = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ControleVoto.class);
			c.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("ministro.id", ministro.getId()));
			c.add(Restrictions.eq("tipoTexto", tipoTexto));

			voto = (ControleVoto) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return voto;
	}
	
	@Override
	public List<ControleVoto> pesquisarControleVotoPorTipoTexto(ObjetoIncidente<?> oi,
			TipoTexto... tiposTextos) throws DaoException {

			List<ControleVoto> cv = null;
			try {
				Criteria c = retrieveSession().createCriteria(ControleVoto.class);
				c.add(Restrictions.eq("objetoIncidente", oi));
				c.add(Restrictions.in("tipoTexto", tiposTextos));
				
				cv = c.list();
			} catch (Exception e) {
				throw new DaoException(e);
			}
			return cv;
	}
	
	@Override
	public List<ControleVoto> pesquisarControleVotoPorMinistro(ObjetoIncidente<?> oi,
			Ministro ministro) throws DaoException {

			List<ControleVoto> cv = null;
			try {
				Criteria c = retrieveSession().createCriteria(ControleVoto.class);
				c.add(Restrictions.eq("objetoIncidente", oi));
				c.add(Restrictions.eq("ministro", ministro));
				
				cv = c.list();
			} catch (Exception e) {
				throw new DaoException(e);
			}
			return cv;
	}
	
	@Override
	public ControleVoto recuperarControleDeVotoSemSessao(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws DaoException {
		ControleVoto voto = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ControleVoto.class);
			c.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("ministro.id", ministro.getId()));
			c.add(Restrictions.eq("tipoTexto", tipoTexto));
			c.add(Restrictions.isNull("sessaoJulgamento"));

			voto = (ControleVoto) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return voto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Long> pesquisarControleVotoPDFNaoAssinado(ObjetoIncidente objetoIncidente) throws DaoException {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT cv.id FROM ControleVoto cv ");
		hql.append(" JOIN cv.textoControleVoto.documentosTexto dt ");
		hql.append(" JOIN dt.documentoEletronicoView de ");
		hql.append(" WHERE cv.tipoSituacaoTexto IN ( :situacoesTexto ) ");
		hql.append(" AND cv.objetoIncidente.id = :objetoIncidente ");
		hql.append(" AND de.descricaoStatusDocumento <> :statusDocumento ");
		hql.append(" AND dt.tipoSituacaoDocumento IN ( :situacoesDocumento ) ");

		List<Long> cvs = null;

		try {
			Session session = retrieveSession();
			Query q = session.createQuery(hql.toString());
			q.setParameterList("situacoesTexto", new Long[] { TipoSituacaoTexto.ATIVO_NO_CONTROLE_DE_VOTOS.getCodigo(),
					TipoSituacaoTexto.REVISADO.getCodigo() });
			q.setParameter("objetoIncidente", objetoIncidente.getId());
			q.setString("statusDocumento", DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO);
			q.setParameterList("situacoesDocumento",
					new Long[] { TipoSituacaoDocumento.REVISADO.getCodigo(), TipoSituacaoDocumento.GERADO.getCodigo(),
							TipoSituacaoDocumento.LIBERADO_PARA_REVISAO.getCodigo(),
							TipoSituacaoDocumento.SOMENTE_REVISADO.getCodigo() });

			cvs = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return cvs;
	}

	@Override
	public void sincronizaControleVotoComTexto(Long seqTexto, Long seqVotos, Date dataSessao) throws DaoException {
		try {
			String stringSeqTexto = String.valueOf(seqTexto);
			String stringSeqVoto = String.valueOf(seqVotos);
			String stringDataSessao = DataUtil.date2String(dataSessao, false);

			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE STF.TEXTOS t  SET ");
			sql.append("  DAT_SESSAO  = TO_DATE('" + stringDataSessao + "', 'DD/MM/YYYY')");
			sql.append(" ,T.SEQ_VOTOS = " + stringSeqVoto);
			sql.append(" WHERE t.SEQ_TEXTOS = " + stringSeqTexto);

			String stringSQL = sql.toString();
			Query query = session.createSQLQuery(stringSQL);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public ControleVoto recuperarControleVoto(Long idControleVoto) throws DaoException {
		ControleVoto cv = recuperarPorId(idControleVoto);
		Hibernate.initialize(cv.getObjetoIncidente());
		Hibernate.initialize(cv.getObjetoIncidente().getPrincipal());
		return cv;
	}
	
	public Boolean existemCompletos() throws DaoException{
		
		Integer cont = null;
		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT ");
			sql.append("    COUNT (*) qtde ");
			sql.append(" FROM "); 
			sql.append("    judiciario.vwmt_objeto_incidente_sigiloso ois ");
			sql.append(" WHERE ");
			sql.append("    ois.tip_objeto_incidente IN ('PR', 'RC', 'IJ') ");
			sql.append("    AND ois.num_nivel_sigilo > 2 ");
			//tem ementa
			sql.append("    AND EXISTS ");
			sql.append("           (SELECT ");
			sql.append("               NULL ");
			sql.append("            FROM ");
			sql.append("               stf.controle_votos v ");
			sql.append("            WHERE ");
			sql.append("               v.cod_tipo_texto IN (80, 87) AND v.seq_tipo_situacao_texto <> 2 AND v.seq_objeto_incidente = ois.seq_objeto_incidente) ");
			//não está em ata de publicação 
			sql.append("    AND NOT EXISTS ");
			sql.append("               (SELECT ");
			sql.append("                   NULL ");
			sql.append("                FROM ");
			sql.append("                   stf.processo_publicados pp ");
			sql.append("                WHERE ");
			sql.append("                   pp.seq_objeto_incidente = ois.seq_objeto_incidente ");
			sql.append("                   AND (pp.cod_capitulo = 5 OR (pp.cod_capitulo = 2 AND pp.cod_materia IN (7, 11)))) ");
			sql.append("    AND (CASE ");
			sql.append("            WHEN NOT EXISTS ");
			sql.append("                    (SELECT ");
			sql.append("                        NULL ");
			sql.append("                     FROM ");
			sql.append("                        stf.controle_votos v ");
			sql.append("                     WHERE ");
			sql.append("                        v.seq_tipo_situacao_texto <> 2 AND v.dat_publico IS NULL AND v.seq_objeto_incidente = ois.seq_objeto_incidente) ");
			sql.append("                 AND (SELECT ");
			sql.append("                         listagg (v.cod_tipo_texto, ', ') WITHIN GROUP (ORDER BY v.cod_tipo_texto) ");
			sql.append("                      FROM ");
			sql.append("                         stf.controle_votos v ");
			sql.append("                      WHERE ");
			sql.append("                         v.seq_tipo_situacao_texto <> 2 AND v.cod_tipo_texto IN (70, 80, 100, 200) AND v.seq_objeto_incidente = ois.seq_objeto_incidente) = '70, 80, 100, 200' THEN 'S' ");
			sql.append("            ELSE 'N' ");
			sql.append("         END) = 'S' ");			

			PreparedStatement ps = session.connection().prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				cont = rs.getInt("qtde");
			}

			rs.close();
			ps.close();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		if (cont > 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public Boolean existemSigilosos() throws DaoException{
		Integer cont = null;
		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT NVL(COUNT(ois.seq_objeto_incidente), 0) as qtde ");
			sql.append("   FROM judiciario.vwmt_objeto_incidente_sigiloso ois ");
			sql.append(" WHERE ois.tip_objeto_incidente IN ('PR', 'RC', 'IJ') ");
			sql.append("        AND ois.num_nivel_sigilo > 2 ");
			// tem ementa
			sql.append("        AND EXISTS ");
			sql.append("                (SELECT NULL ");
			sql.append("                   FROM stf.controle_votos v ");
			sql.append("                  WHERE     v.cod_tipo_texto IN (80, 87) ");
			sql.append("                        AND v.seq_tipo_situacao_texto <> 2 ");
			sql.append("                        AND v.seq_objeto_incidente = ois.seq_objeto_incidente) ");
			// não está em ata de publicação
			sql.append("        AND NOT EXISTS ");
			sql.append("                    (SELECT NULL ");
			sql.append("                       FROM stf.processo_publicados pp ");
			sql.append("                      WHERE pp.seq_objeto_incidente = ois.seq_objeto_incidente ");
			sql.append("                            AND (pp.cod_capitulo = 5 ");
			sql.append("                                 OR (pp.cod_capitulo = 2 ");
			sql.append("                                     AND pp.cod_materia IN (7, 11)))) ");

			PreparedStatement ps = session.connection().prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				cont = rs.getInt("qtde");
			}

			rs.close();
			ps.close();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		if (cont > 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}

	}
	
	public List<Object>  verificarSigilosos() throws DaoException{
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("SELECT judiciario.fnc_processo_referencia (ois.seq_objeto_incidente) dsc_processo_referencia, ");
		hql.append("       (SELECT m.nom_ministro ");
		hql.append("          FROM stf.ministros m ");
		hql.append("         WHERE m.cod_ministro = ois.cod_relator_incidente AND ois.num_nivel_sigilo = 3) ");
		hql.append("           nom_relator_incidente, ois.num_nivel_sigilo, ");
		hql.append("       (SELECT listagg (tt.dsc_tipo || '(' || m.sig_ministro  || ')', ', ') ");
		hql.append("                   WITHIN GROUP (ORDER BY v.seq_voto) ");
		hql.append("          FROM stf.controle_votos v, stf.tipo_textos tt, stf.ministros m ");
		hql.append("         WHERE     v.cod_tipo_texto = tt.cod_tipo_texto ");
		hql.append("               AND v.seq_objeto_incidente = ois.seq_objeto_incidente ");
		hql.append("               AND v.cod_ministro = m.cod_ministro ");
		hql.append("               AND v.dat_publico IS NOT NULL ");
		hql.append("               AND v.seq_tipo_situacao_texto <> 2) ");
		hql.append("           lista_doc_liberado, ");
		hql.append("       (SELECT listagg (tt.dsc_tipo || '(' || m.sig_ministro || ')', ', ') ");
		hql.append("                   WITHIN GROUP (ORDER BY v.seq_voto) ");
		hql.append("          FROM stf.controle_votos v, stf.tipo_textos tt, stf.ministros m ");
		hql.append("         WHERE     v.cod_tipo_texto = tt.cod_tipo_texto ");
		hql.append("               AND v.seq_objeto_incidente = ois.seq_objeto_incidente ");
		hql.append("               AND v.cod_ministro = m.cod_ministro ");
		hql.append("               AND v.dat_publico IS NULL ");
		hql.append("               AND v.seq_tipo_situacao_texto <> 2) ");
		hql.append("           lista_doc_nao_liberado ");
		hql.append("  FROM judiciario.vwmt_objeto_incidente_sigiloso ois ");
		hql.append(" WHERE ois.tip_objeto_incidente IN ('PR', 'RC', 'IJ') AND ois.num_nivel_sigilo > 2 ");
		// tem ementa
		hql.append("       AND EXISTS ");
		hql.append("               (SELECT NULL ");
		hql.append("                  FROM stf.controle_votos v ");
		hql.append("                 WHERE     v.cod_tipo_texto IN (80, 87) ");
		hql.append("                       AND v.seq_tipo_situacao_texto <> 2 ");
		hql.append("                       AND v.seq_objeto_incidente = ois.seq_objeto_incidente) ");
		// não está em ata de publicação
		hql.append("       AND NOT EXISTS ");
		hql.append("                   (SELECT NULL ");
		hql.append("                      FROM stf.processo_publicados pp ");
		hql.append("                     WHERE pp.seq_objeto_incidente = ois.seq_objeto_incidente ");
		hql.append("                           AND (pp.cod_capitulo = 5 ");
		hql.append("                                OR (pp.cod_capitulo = 2 AND pp.cod_materia IN (7, 11)))) ");
		
		List<Object> acordaos3e4 = null;

		try {
			Session session = retrieveSession();
			Query query = session.createSQLQuery(hql.toString());
			acordaos3e4 = query.list();
		
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return acordaos3e4;
	}

}
