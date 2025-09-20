package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.EmprestimoAutosProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.EmprestimoAutosProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class EmprestimoAutosProcessoDaoHibernate extends GenericHibernateDao<EmprestimoAutosProcesso, Long>
	implements EmprestimoAutosProcessoDao {

	private static final long serialVersionUID = 5134499306418006125L;

	public EmprestimoAutosProcessoDaoHibernate() {
		super(EmprestimoAutosProcesso.class);
	}
	
	public List<EmprestimoAutosProcesso> pesquisarAutos(String nomeJurisdicionado, Long objetoIncidente, Date dataInicial,
			Date dataFinal, Long idSituacao) throws DaoException {
		
		List<EmprestimoAutosProcesso> emprestimos = null;
		
		try {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT DISTINCT ep.* ");
			sql.append("  FROM JUDICIARIO.EMPRESTIMO_AUTOS_PROCESSO ep ");
			
			if (objetoIncidente != null){
				sql.append("       ,STF.DESLOCA_PROCESSOS desl ");
			}
			
			if (nomeJurisdicionado != null && nomeJurisdicionado.trim().length() > 0){
				sql.append("       ,JUDICIARIO.ASSOCIACAO_JURISDICIONADO ASSOC ");
			}

			sql.append(" WHERE (1=1)  ");
			
			if (nomeJurisdicionado != null && nomeJurisdicionado.trim().length() > 0){			       
                sql.append("   AND ep.seq_associacao_jurisdicionado = assoc.seq_associacao_jurisdicionado(+)");                		
				sql.append("        AND (EXISTS");
				sql.append("                 (SELECT 1");
				sql.append("                    FROM judiciario.associacao_jurisdicionado aj,");
				sql.append("                         judiciario.papel_jurisdicionado pp,");
				sql.append("                         judiciario.jurisdicionado ju");
				sql.append("                   WHERE aj.seq_associado_membro = pp.seq_papel_jurisdicionado");
				sql.append("                         AND pp.seq_jurisdicionado = ju.seq_jurisdicionado");
				sql.append("                         AND UPPER (ju.nom_jurisdicionado) LIKE :nome");
				sql.append("                         AND aj.seq_associacao_jurisdicionado =");
				sql.append("                                 assoc.seq_associacao_jurisdicionado)");
				sql.append("             OR EXISTS");
				sql.append("                    (SELECT 1");
				sql.append("                       FROM judiciario.associacao_jurisdicionado aj,");
				sql.append("                            judiciario.papel_jurisdicionado pp,");
				sql.append("                            judiciario.jurisdicionado ju");
				sql.append("                      WHERE aj.seq_jurisdicionado_grupo =");
				sql.append("                                pp.seq_papel_jurisdicionado");
				sql.append("                            AND pp.seq_jurisdicionado = ju.seq_jurisdicionado");
				sql.append("                            AND UPPER (ju.nom_jurisdicionado) LIKE :nome");				
				sql.append("                            AND aj.seq_associacao_jurisdicionado =");
				sql.append("                                    assoc.seq_associacao_jurisdicionado)");
				sql.append("             OR EXISTS");
				sql.append("                    (SELECT 1");
				sql.append("                       FROM judiciario.papel_jurisdicionado pp,");
				sql.append("                            judiciario.jurisdicionado ju");
				sql.append("                      WHERE ep.seq_papel_juris_advogado =");
				sql.append("                                pp.seq_papel_jurisdicionado");
				sql.append("                            AND pp.seq_jurisdicionado = ju.seq_jurisdicionado");
				sql.append("                            AND UPPER (ju.nom_jurisdicionado) LIKE :nome))");				
			}
			
			if (objetoIncidente != null){
				sql.append(" AND ep.SEQ_DESLOCAMENTO_RETIRADA = desl.SEQ_DESLOCA_PROCESSOS ");
				sql.append(" AND desl.SEQ_OBJETO_INCIDENTE =:objetoIncidente ");
			}

			if( dataInicial != null && dataFinal != null) {
				sql.append(" AND TO_CHAR(ep.DAT_EMPRESTIMO,'dd/mm/yyyy') >= TO_DATE(:dataInicial,'dd/mm/yyyy') ");
				sql.append(" AND TO_CHAR(ep.DAT_EMPRESTIMO,'dd/mm/yyyy') <= TO_DATE(:dataFinal,'dd/mm/yyyy') ");
			}
			
//			if (idSituacao == null){
//				sql.append("  AND ROWNUM < 50 ");
//			}

			SQLQuery q = retrieveSession().createSQLQuery(sql.toString());
			
			
			if (nomeJurisdicionado != null && nomeJurisdicionado.trim().length() > 0){
				q.setParameter("nome", "%" + nomeJurisdicionado.toUpperCase() + "%");
			}
			
			if (objetoIncidente!= null){
				q.setLong("objetoIncidente", objetoIncidente);
			}
			
			if (dataInicial != null && dataFinal != null){
				q.setString("dataInicial", new SimpleDateFormat("dd/MM/yyyy").format(dataInicial));
				q.setString("dataFinal", new SimpleDateFormat("dd/MM/yyyy").format(dataFinal));
			}
			q.addEntity(EmprestimoAutosProcesso.class);
			emprestimos = q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return emprestimos;
	}
	
	@Override
	public Boolean existeEmprestimoParaAssociacao(AssociacaoJurisdicionado associacao) throws DaoException {
	try {	
		Session session = retrieveSession();

		Criteria c = session.createCriteria(EmprestimoAutosProcesso.class)
	    	.add( Restrictions.eq("associacaoJurisdicionado.id", associacao.getId())
			   );
		
		List<EmprestimoAutosProcesso> emprestimos = c.list();
		if (emprestimos == null) {
			return false;
		}
		if (emprestimos != null && emprestimos.size() > 0) {
			return true;
		}

	} catch (Exception e) {
		throw new DaoException(e);
	}
	return false;
		
	}

	@Override
	public EmprestimoAutosProcesso recuperarEmprestimoPorDeslocamento(DeslocaProcesso deslocaProcesso) throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT ep FROM EmprestimoAutosProcesso ep ");
			hql.append(" WHERE (1=1)  ");
			hql.append(" AND ep.deslocaRetirada.seqDeslocaProcesso = " + deslocaProcesso.getSeqDeslocaProcesso() );
			Query q = session.createQuery(hql.toString());
			EmprestimoAutosProcesso emprestimo = (EmprestimoAutosProcesso) q.uniqueResult();
			return emprestimo;
		} catch (Exception e) {
			throw new DaoException("Erro na recuperação do emprestipo pelo deslocamento.");
		}
	}
	
	@Override
	public Boolean existeEmprestimoNaGuiaDeAutos(Guia guia) throws DaoException {
		try {
			if ( !guia.getTipoOrgaoDestino().equals(new Integer(1)) && (!guia.getTipoOrgaoOrigem().equals(new Integer(1))) ) {
				throw new DaoException("Guia não específica para empréstimo de autos.");
			}
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT COUNT(*) ");
			hql.append("FROM JUDICIARIO.EMPRESTIMO_AUTOS_PROCESSO em");
			if (guia.getTipoOrgaoDestino().equals(new Integer(1))) {
				hql.append("  WHERE EM.SEQ_DESLOCAMENTO_RETIRADA IN");
			} else {
				hql.append("  WHERE EM.SEQ_DESLOCAMENTO_DEVOLUCAO IN");
			}
			hql.append("    (SELECT DES.SEQ_DESLOCA_PROCESSOS");
			hql.append("     FROM STF.DESLOCA_PROCESSOS des");
			hql.append("     WHERE DES.ANO_GUIA = " + guia.getId().getAnoGuia());
			hql.append("       AND DES.NUM_GUIA = " + guia.getId().getNumeroGuia());
			hql.append("       AND DES.COD_ORGAO_ORIGEM = " + guia.getId().getCodigoOrgaoOrigem() + ")");

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(hql.toString());

//			Long total = (Long) sqlQuery.uniqueResult();
			Object objTotal = sqlQuery.uniqueResult();
			Long total = NumberUtils.createLong(objTotal.toString());
			
			if (total != null && total > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new DaoException("Erro ao verificar o empréstimio na retirada.");
		}
	}
	
	@Override
	public Boolean existeEmprestimoParaObjetoIncidente(Long idObjetoIncidente, boolean devolucao) throws DaoException {
		try {
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT COUNT(*) ");
			hql.append("FROM JUDICIARIO.EMPRESTIMO_AUTOS_PROCESSO em");
			if (!devolucao) {
				hql.append("  WHERE EM.SEQ_DESLOCAMENTO_RETIRADA IN");
			} else {
				hql.append("  WHERE EM.SEQ_DESLOCAMENTO_DEVOLUCAO IN");
			}
			hql.append("    (SELECT DES.SEQ_DESLOCA_PROCESSOS");
			hql.append("     FROM STF.DESLOCA_PROCESSOS des");
			hql.append("     WHERE DES.SEQ_OBJETO_INCIDENTE = " + idObjetoIncidente + ")" );

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(hql.toString());

			Object objTotal = sqlQuery.uniqueResult();
			Long total = NumberUtils.createLong(objTotal.toString());
			
			if (total != null && total > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new DaoException("Erro ao verificar o empréstimio na retirada.");
		}
	}
	
	
	@Override
	public String getNomeAdvogadoOuAutorizado(Guia guia, boolean existeEmprestimo) throws DaoException {
		try {
			StringBuffer hql = new StringBuffer();
			if (!existeEmprestimo) { // não possui empréstimo - recuperar 
				if (guia.getTipoOrgaoDestino().equals((Integer)1)) { // GUIA DE RETIRADA 
					hql.append("select A.NOM_ADV from STF.ADVOGADOS A where A.COD_ADV = " + guia.getCodigoOrgaoDestino());
				} else if (guia.getTipoOrgaoOrigem().equals((Integer)1)) { // GUIA DE DEVOLUÇÃO
					hql.append("select A.NOM_ADV from STF.ADVOGADOS A where A.COD_ADV = " + guia.getCodigoOrgaoOrigem());
				}
			} else {
				if (guia.getTipoOrgaoDestino().equals((Integer)1)) { // GUIA DE RETIRADA
					hql.append(" SELECT DISTINCT DECODE(JURIS_GRUPO.SEQ_JURISDICIONADO, NULL, ADV_ORIGEM.NOM_JURISDICIONADO, JURIS_GRUPO.NOM_JURISDICIONADO || ' (' || ADV_ORIGEM.NOM_JURISDICIONADO || ')') AS descricaoOrgaoOrigem"); 
					hql.append("	FROM STF.DESLOCA_PROCESSOS DP,");
					hql.append("	     JUDICIARIO.EMPRESTIMO_AUTOS_PROCESSO EMPRESTIMO_RETIRADA,");
					hql.append("	     JUDICIARIO.JURISDICIONADO ADV_ORIGEM,");
					hql.append("	     JUDICIARIO.PAPEL_JURISDICIONADO PAPEL_GRUPO,");
					hql.append("	     JUDICIARIO.JURISDICIONADO JURIS_GRUPO,");
					hql.append("	     JUDICIARIO.ASSOCIACAO_JURISDICIONADO ASSOCIACAO");
					hql.append("	WHERE DP.COD_ORGAO_DESTINO = ADV_ORIGEM.SEQ_JURISDICIONADO");
					hql.append("	  AND DP.SEQ_DESLOCA_PROCESSOS = EMPRESTIMO_RETIRADA.SEQ_DESLOCAMENTO_RETIRADA");
					hql.append("	  AND EMPRESTIMO_RETIRADA.SEQ_ASSOCIACAO_JURISDICIONADO = ASSOCIACAO.SEQ_ASSOCIACAO_JURISDICIONADO (+)");
					hql.append("	  AND ASSOCIACAO.SEQ_JURISDICIONADO_GRUPO = PAPEL_GRUPO.SEQ_PAPEL_JURISDICIONADO (+)");
					hql.append("	  AND PAPEL_GRUPO.SEQ_JURISDICIONADO = JURIS_GRUPO.SEQ_JURISDICIONADO (+)");
					hql.append("	  AND dp.ANO_GUIA = " + guia.getId().getAnoGuia());
					hql.append("	  AND dp.NUM_GUIA = " + guia.getId().getNumeroGuia()); 
					hql.append("	  AND dp.COD_ORGAO_ORIGEM = " + guia.getId().getCodigoOrgaoOrigem());
				} else if (guia.getTipoOrgaoOrigem().equals((Integer)1)) { // GUIA DE DEVOLUÇÃO
					hql.append("SELECT DISTINCT DECODE(JURIS_GRUPO.SEQ_JURISDICIONADO, NULL, ADV_ORIGEM.NOM_JURISDICIONADO, JURIS_GRUPO.NOM_JURISDICIONADO || ' (' || ADV_ORIGEM.NOM_JURISDICIONADO || ')') AS descricaoOrgaoOrigem");
					hql.append("  FROM STF.DESLOCA_PROCESSOS DP,");
					hql.append("     JUDICIARIO.EMPRESTIMO_AUTOS_PROCESSO EMPRESTIMO_DEVOLUCAO,");
					hql.append("     JUDICIARIO.JURISDICIONADO ADV_ORIGEM,");
					hql.append("     JUDICIARIO.PAPEL_JURISDICIONADO PAPEL_GRUPO,");
					hql.append("     JUDICIARIO.JURISDICIONADO JURIS_GRUPO,");
					hql.append("     JUDICIARIO.ASSOCIACAO_JURISDICIONADO ASSOCIACAO");
					hql.append("  WHERE DP.COD_ORGAO_ORIGEM = ADV_ORIGEM.SEQ_JURISDICIONADO");
					hql.append("   AND DP.SEQ_DESLOCA_PROCESSOS = EMPRESTIMO_DEVOLUCAO.SEQ_DESLOCAMENTO_DEVOLUCAO");
					hql.append("   AND EMPRESTIMO_DEVOLUCAO.SEQ_ASSOCIACAO_JURISDICIONADO = ASSOCIACAO.SEQ_ASSOCIACAO_JURISDICIONADO (+)");
					hql.append("   AND ASSOCIACAO.SEQ_JURISDICIONADO_GRUPO = PAPEL_GRUPO.SEQ_PAPEL_JURISDICIONADO (+)");
					hql.append("   AND PAPEL_GRUPO.SEQ_JURISDICIONADO = JURIS_GRUPO.SEQ_JURISDICIONADO (+)");
					hql.append("   AND dp.ANO_GUIA = " + guia.getId().getAnoGuia());
					hql.append("   AND dp.NUM_GUIA = " + guia.getId().getNumeroGuia());  
					hql.append("   AND dp.COD_ORGAO_ORIGEM = " + guia.getId().getCodigoOrgaoOrigem()); 
				}
			}
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(hql.toString());
			String nome = (String) sqlQuery.uniqueResult();
			return nome;
		} catch (Exception e) {
			throw new DaoException("Erro na recuperação do nome do advogado/autorizado.");
		}
		
	}
	
	
}
