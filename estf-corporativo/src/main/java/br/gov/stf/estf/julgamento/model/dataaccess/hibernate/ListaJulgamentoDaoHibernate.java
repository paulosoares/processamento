/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.TipoManifestacao;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoJulgamento;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.ListaJulgamentoDao;
import br.gov.stf.estf.julgamento.model.util.ListaJulgamentoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchResult;

@Repository
public class ListaJulgamentoDaoHibernate extends GenericHibernateDao<ListaJulgamento, Long> implements ListaJulgamentoDao{

	private static final long serialVersionUID = -1284591112480055967L;

	public ListaJulgamentoDaoHibernate() {
		super(ListaJulgamento.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ListaJulgamento> pesquisarListaPorObjetoIncidenteSessao(ObjetoIncidente<?> objetoIncidente,
			String colegiado) throws DaoException {

		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ListaJulgamento.class);
			c.createAlias("elementos", "oi");
			c.createAlias("sessao", "s");
			c.add(Restrictions.eq("oi.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("s.colegiado.id", colegiado));	
			return (List<ListaJulgamento>) c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ListaJulgamento> pesquisar(ObjetoIncidente<?> objetoIncidente)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ListaJulgamento.class);
			c.createAlias("elementos", "oi");
			c.add(Restrictions.eq("oi.id", objetoIncidente.getId()));
			return (List<ListaJulgamento>) c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ListaJulgamento> pesquisar(ObjetoIncidente<?> objetoIncidente, boolean pesquisarApenasListasPrevistasParaJulgamento)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ListaJulgamento.class);
			c.createAlias("elementos", "oi");
			c.createAlias("sessao", "s");
			c.add(Restrictions.eq("oi.id", objetoIncidente.getId()));
			if (pesquisarApenasListasPrevistasParaJulgamento) {
				c.add(Restrictions.isNull("s.dataFim"));
			}
			return (List<ListaJulgamento>) c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ListaJulgamento> pesquisar(Ministro ministro)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ListaJulgamento.class);
			c.createAlias("sessao", "s");
			if (ministro != null){
				c.add(Restrictions.eq("ministro.id", ministro.getId()));				
			}
			c.add(Restrictions.isNull("s.dataFim"));
			c.addOrder(Order.asc("s.dataInicio"));
			return (List<ListaJulgamento>) c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public Integer recuperarMaiorOrdemSessao(Sessao sessao) throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT max(lj.ordemSessao) FROM ListaJulgamento lj ");
			hql.append(" WHERE lj.sessao.id = :idSessao ");
			
			Query q = session.createQuery(hql.toString());
			q.setLong("idSessao", sessao.getId());			
			return (Integer) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public Integer recuperarMaiorOrdemSessaoMinistro(Sessao sessao, Ministro ministro) throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT max(lj.ordemSessaoMinistro) FROM ListaJulgamento lj ");
			hql.append(" WHERE lj.sessao.id = :idSessao ");
			hql.append(" AND lj.ministro.id = :idMinistro");
			
			Query q = session.createQuery(hql.toString());
			q.setLong("idSessao", sessao.getId());			
			q.setLong("idMinistro", ministro.getId());
			return (Integer) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public Integer recuperarMaiorOrdemSessaoMinistroListaJulgamentoAno(Ministro ministro, short ano) throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT max(lj.ordemSessao) FROM ListaJulgamento lj ");
			hql.append(" inner join lj.sessao s ");
			hql.append(" WHERE lj.ministro.id = :idMinistro ");
			hql.append(" AND s.ano = :ano ");
			
			Query q = session.createQuery(hql.toString());
			q.setLong("idMinistro", ministro.getId());
			q.setShort("ano", ano);
			return (Integer) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ListaJulgamento> pesquisarPorColegiado(
			TipoColegiadoConstante colegiado) throws DaoException {
		// TODO Auto-generated method stub
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ListaJulgamento.class);
			c.createAlias("sessao", "s");
			if (colegiado != null){
				c.add(Restrictions.eq("s.colegiado.id", colegiado.getSigla()));				
			}
			c.add(Restrictions.isNull("s.dataFim"));
			c.addOrder(Order.asc("s.dataInicio"));
			c.addOrder(Order.asc("ordemSessao"));
			return (List<ListaJulgamento>) c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long consultaQuantidadeListasSemSessao(
			TipoColegiadoConstante colegiado) throws DaoException {
		Session session;
		try {
			session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT count(lj) FROM ListaJulgamento lj ");
			hql.append(" WHERE lj.sessao IS NULL ");
			
			Query q = session.createQuery(hql.toString());
			
			return (Long) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public void atualizarOrdenacaoListas(
			ListaJulgamento listaJulgamentoBase, Integer ordemDestino) throws DaoException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE julgamento.lista_julgamento lj ");
			if (ordemDestino == null || (listaJulgamentoBase.getOrdemSessao() != null && listaJulgamentoBase.getOrdemSessao() < ordemDestino)) {
				sql.append(" SET lj.num_ordem_sessao = lj.num_ordem_sessao - 1 ");
				sql.append(" WHERE lj.seq_sessao = :idSessao ");
				sql.append(" AND lj.num_ordem_sessao > :ordemBase ");
				if (ordemDestino != null) {
					sql.append(" AND lj.num_ordem_sessao <= :ordemDestino ");
				}
			} else if (listaJulgamentoBase.getOrdemSessao() == null || listaJulgamentoBase.getOrdemSessao() > ordemDestino){
				sql.append(" SET lj.num_ordem_sessao = lj.num_ordem_sessao + 1 ");
				sql.append(" WHERE lj.seq_sessao = :idSessao ");
				sql.append(" AND lj.num_ordem_sessao >= :ordemDestino ");
				if (listaJulgamentoBase.getOrdemSessao() != null) {
					sql.append(" AND lj.num_ordem_sessao < :ordemBase ");
				}
			}
		
			SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
			sqlQuery.setLong("idSessao", listaJulgamentoBase.getSessao().getId());
			if (listaJulgamentoBase.getOrdemSessao() != null) {
				sqlQuery.setInteger("ordemBase", listaJulgamentoBase.getOrdemSessao());
			}
			if (ordemDestino != null) {
				sqlQuery.setInteger("ordemDestino", ordemDestino);
			}
			
			sqlQuery.executeUpdate();		
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void refresh(ListaJulgamento listaJulgamento) throws DaoException {
		retrieveSession().refresh(listaJulgamento);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SearchResult pesquisarListaJulgamentoPlenarioVirtual(ListaJulgamentoSearchData searchData) throws DaoException {

		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer(" FROM ListaJulgamento lj ");
			
			hql.append(" LEFT JOIN FETCH lj.sessao s ");
			
			if (searchData.getTipoSituacaoJulgamento() != TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant.AGENDADO) {
				hql.append(" LEFT JOIN FETCH s.colegiado ");
				hql.append(" LEFT JOIN FETCH lj.listaProcessoListaJulgamento as lplj ");
				hql.append(" LEFT JOIN FETCH lplj.julgamentoProcesso jp ");
				hql.append(" LEFT JOIN FETCH jp.listaVotoJulgamentoProcesso ");
//				hql.append(" LEFT JOIN FETCH jp.objetoIncidente oi ");
//				hql.append(" LEFT JOIN FETCH oi.principal ");	
			}

			hql.append(" WHERE 1=1 ");
			hql.append(" AND lj.sessao.tipoAmbiente = '"+TipoAmbienteConstante.VIRTUAL.getSigla() + "'");
			
			if (searchData.getTipoSituacaoJulgamento()!= null) {
				if (searchData.getTipoSituacaoJulgamento() == TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO) {
					hql.append(" and lj.sessao.dataPrevistaInicio <= Sysdate and lj.sessao.dataPrevistaFim >= Sysdate ");		
				}
				
				if (searchData.getTipoSituacaoJulgamento() == TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant.AGENDADO) {
					hql.append(" and lj.sessao.dataPrevistaInicio > Sysdate ");		
				}
				
				if (searchData.getTipoSituacaoJulgamento() == TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant.FINALIZADO) {
					hql.append(" and lj.sessao.dataPrevistaFim < Sysdate ");		
				}
			}
			if (searchData.getIdSessao()!= null) {
				hql.append(" and lj.sessao.id IN (" + searchData.getIdSessao() + ")");
			}
			
			if (searchData.getTipoJulgamentoVirtual() != null){
				hql.append(" and lj.sessao.tipoJulgamentoVirtual = " + searchData.getTipoJulgamentoVirtual().getId());
			}
			
			if (searchData.getDataSessao()!= null) {
					hql.append(" and trunc(:dataSessao) between trunc(lj.sessao.dataPrevistaInicio) and trunc(lj.sessao.dataPrevistaFim) ");										
			}
			
			if (searchData.getCodigoMinistroParticipante() != null) {
				hql.append(" and exists (select envolvido.id from EnvolvidoSessao envolvido where lj.sessao.id = envolvido.sessao.id and envolvido.ministro.id = "+ searchData.getCodigoMinistroParticipante() + ") ");								
			}
			
			if (null!=searchData.isPrimeiraTurma() || null!=searchData.isSegundaTurma() || null!=searchData.isPlenario()) {
				String hqlColegiado = "";
				hql.append(" and lj.sessao.colegiado.id in (");
				
				if(null!=searchData.isPrimeiraTurma() && searchData.isPrimeiraTurma()) {					
					hqlColegiado = "'1T'";
				}
				
				if(null!=searchData.isSegundaTurma() && searchData.isSegundaTurma()) {
					if (!hqlColegiado.equals(""))
						hqlColegiado = hqlColegiado + ",";
						hqlColegiado = hqlColegiado + "'2T'";
				}
				
				if(null!=searchData.isPlenario() && searchData.isPlenario()) {
					if (!hqlColegiado.equals(""))
						hqlColegiado = hqlColegiado + ",";
					hqlColegiado = hqlColegiado + "'TP'";
				}
				
				hql.append(hqlColegiado);
				hql.append(") ");					
			}
						
			if (searchData.getCodigoMinistroRelator() !=null) {
				hql.append(" and lj.ministro.id = " + searchData.getCodigoMinistroRelator());
			}
					
			if (searchData.isVotoPendente()!=null && searchData.getCodigoMinistroParticipante() != null ) {
				hql.append(" AND lj.id ");
				
				if (searchData.isVotoPendente().equals(Boolean.TRUE))
					hql.append(" IN ");
				else
					hql.append(" NOT IN ");
				
				hql.append("  (SELECT lj.id "
				+ "            FROM lj.listaProcessoListaJulgamento lplj "
				+ "            WHERE lj.ministro.id <> " + searchData.getCodigoMinistroParticipante()
				+ "            AND (lplj.objetoIncidente NOT IN (SELECT voto.julgamentoProcesso.objetoIncidente "
				+ "                                             FROM VotoJulgamentoProcesso voto "
				+ "                                             WHERE voto.julgamentoProcesso = lplj.julgamentoProcesso "
				+ "                                             AND voto.ministro.id = "+ searchData.getCodigoMinistroParticipante() 
				+ "                                             AND voto.tipoSituacaoVoto = 'V')"
				+ "			   AND NOT EXISTS (SELECT jp FROM JulgamentoProcesso jp where jp = lplj.julgamentoProcesso and "
				+ "												(jp.ministroDestaque = "+searchData.getCodigoMinistroParticipante()+" OR"
				+ " 											 jp.ministroVista = "+searchData.getCodigoMinistroParticipante()+") )"
				+ "					) "
				+ "           )");
				
			}
			
			if ((searchData.getSiglaClasseProcessual() != null && !searchData.getSiglaClasseProcessual().equals("") )|| (searchData.getNumeroProcesso() != null && searchData.getNumeroProcesso() > 0L)) {
				hql.append(" and exists (select processoListaJulgamento.id from ProcessoListaJulgamento processoListaJulgamento, Processo processo where "
						+ " processoListaJulgamento.listaJulgamento.id = lj.id"
						+ " and processo.id = processoListaJulgamento.objetoIncidente.principal.id");
				if (searchData.getSiglaClasseProcessual() != null ) {					
					hql.append(" and processo.classeProcessual = '"+searchData.getSiglaClasseProcessual() + "'");					
				}				
				if (searchData.getNumeroProcesso() != null ) {
					hql.append(" and processo.numeroProcessual = '"+searchData.getNumeroProcesso() + "'");
				}
				hql.append(")");
			}
			
			if (searchData.isPedidoDestaque() || searchData.isPedidoVista()) {
				hql.append(" AND EXISTS (FROM lj.listaProcessoListaJulgamento lplj JOIN lplj.julgamentoProcesso jp WHERE ");
				
				if (searchData.isPedidoDestaque())
					hql.append(" (jp.situacaoProcessoSessao = '" + TipoSituacaoProcessoSessao.DESTAQUE.getSigla() + "')");
				
				if (searchData.isPedidoDestaque() && searchData.isPedidoVista()) 
					hql.append(" OR ");
				
				if (searchData.isPedidoVista())
					hql.append(" (jp.situacaoProcessoSessao = '" + TipoSituacaoProcessoSessao.SUSPENSO.getSigla() + "')");
				
				hql.append(") ");
			}
			
			if (searchData.isSustentacaoOral() && !searchData.isQuestaoFato())
				hql.append(" AND EXISTS (FROM lj.listaProcessoListaJulgamento as plj JOIN plj.manifestacoes as mr WHERE mr.tipoManifestacao = '"+TipoManifestacao.SUSTENTACAO_ORAL.getCodigo()+"')");
			
			if (!searchData.isSustentacaoOral() && searchData.isQuestaoFato())
				hql.append(" AND EXISTS (FROM lj.listaProcessoListaJulgamento as plj JOIN plj.manifestacoes as mr WHERE mr.tipoManifestacao = '"+TipoManifestacao.QUESTAO_FATO.getCodigo()+"')");
			
			if (searchData.isSustentacaoOral() && searchData.isQuestaoFato())
				hql.append(" AND EXISTS (FROM lj.listaProcessoListaJulgamento as plj JOIN plj.manifestacoes as mr WHERE mr.tipoManifestacao = '"+TipoManifestacao.SUSTENTACAO_ORAL.getCodigo()+"' OR mr.tipoManifestacao = '"+TipoManifestacao.QUESTAO_FATO.getCodigo()+ "')");
			
			if (Boolean.TRUE.equals(searchData.getJulgamentoTese()))
				hql.append(" AND lj.julgamentoTese = 'S' ");
			
			if (Boolean.TRUE.equals(searchData.getJulgamentoModulacao()))
				hql.append(" AND lj.julgamentoModulacao = 'S' ");
			
			hql.append(" AND s.exclusivoDigital = 'N' ");
				
			Query collectionQuery = session.createQuery("SELECT lj " + hql.toString() + " ORDER BY lj.sessao.colegiado, lj.sessao.dataInicio, lj.sessao.dataPrevistaInicio, lj.sessao.id, lj.ministro, lj.ordemSessaoMinistro");
			
			if (searchData.getDataSessao()!= null) {
				collectionQuery.setParameter("dataSessao", searchData.getDataSessao());
			}
			
			Long totalSize = 0L;
			
			List<ListaJulgamento> result = collectionQuery.list();
			Set<ListaJulgamento> lista = new LinkedHashSet<ListaJulgamento>(result);
			
			if (searchData.getTotalResult() != null && searchData.getTotalResult() > 0L) {
				totalSize = searchData.getTotalResult();
			} else {
				totalSize = Long.valueOf(String.valueOf(lista.size()));
			}

			if (totalSize > 0) {

				/* O banco de dados nao retorna a lista de processos correta
				 * quando o pageMaxResult é igual a 15. A melhor solucao
				 * encontrada foi incrementar o pageMaxResult em 1 e remover o
				 * ultimo item da lista antes de retorna-la. Como o hibernate
				 * monitora todas as mudanças de seus objetos, a lista deve ser
				 * duplicada antes da remoção do útlmo ítem. */
				if (totalSize > searchData.getPageData().getPageMaxResult() && searchData.isPaging()) {
					collectionQuery.setMaxResults(searchData.getPageData().getPageMaxResult() + 1);
					collectionQuery.setFirstResult(searchData.getPageData().getFirstResult());
				}
				
				result = new LinkedList<ListaJulgamento>(lista);
			}

			SearchResult sr = new SearchResult(searchData, totalSize, result);
			return sr;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ListaJulgamento> pesquisarListasDeJulgamentoPorDataInicioSessao(ListaJulgamento listaJulgamentoExample, Date dataInicio, Date dataFim) {
		try {
			Criteria criteria = retrieveSession()
					.createCriteria(ListaJulgamento.class, "lj").add(Example.create(listaJulgamentoExample))
					.createCriteria("lj.sessao", "sessao").add(Example.create(listaJulgamentoExample.getSessao()));
			
			if (listaJulgamentoExample.getMinistro() != null)
				criteria.add(Restrictions.eq("lj.ministro.id", listaJulgamentoExample.getMinistro().getId()));
			
			if (listaJulgamentoExample != null && listaJulgamentoExample.getSessao() != null 
					&& listaJulgamentoExample.getSessao().getColegiado() != null && listaJulgamentoExample.getSessao().getColegiado().getId() != null)
				criteria.add(Restrictions.eq("sessao.colegiado.id",listaJulgamentoExample.getSessao().getColegiado().getId()));
			
			// filtro por data de início da sessao
			if (dataInicio != null && dataFim != null)
				criteria.add(Restrictions.or(Restrictions.between("sessao.dataInicio", dataInicio, dataFim), Restrictions.between("sessao.dataPrevistaInicio", dataInicio, dataFim)));
			
			// Ordenação
			criteria.addOrder(Order.asc("sessao.dataInicio"))
					.addOrder(Order.asc("sessao.dataPrevistaInicio"))
					.addOrder(Order.asc("sessao.colegiado.id"))
					.addOrder(Order.asc("lj.ordemSessao"));
			
			return criteria.list();
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ListaJulgamento pesquisar(ObjetoIncidente<?> objetoIncidente, JulgamentoProcesso julgamentoProcesso) throws DaoException {
		try {
			StringBuffer hql = new StringBuffer("SELECT lj FROM ListaJulgamento lj");
			hql.append(" JOIN lj.listaProcessoListaJulgamento plj ");
			hql.append(" JOIN plj.julgamentoProcesso jp ");  
			/*hql.append(" where lj.id = plj.listaJulgamento.id ");
			hql.append(" and plj.julgamentoProcesso.id = jp.id ");*/
			hql.append(" WHERE jp.id = :julgamentoProcesso");
			hql.append(" and jp.objetoIncidente = :objetoIncidente");
			
			Session session = retrieveSession();
			Query createQuery = session.createQuery(hql.toString());
			createQuery.setParameter("julgamentoProcesso", julgamentoProcesso);
			createQuery.setParameter("objetoIncidente", objetoIncidente);
			
			return (ListaJulgamento) createQuery.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long obterQuantidadeRascunhosPorMinistro(ListaJulgamento listaJulgamento, Ministro ministro) throws DaoException {
		try {
			String hql = "SELECT count(*) FROM VotoJulgamentoProcesso vjp "
					+ " JOIN vjp.julgamentoProcesso jp "
					+ " JOIN jp.processoListaJulgamento plj "
					+ " WHERE plj.listaJulgamento = :listaJulgamento "
					+ " AND vjp.ministro = :ministro";
			
			Session session = retrieveSession();
			Query createQuery = session.createQuery(hql);
			createQuery.setParameter("listaJulgamento", listaJulgamento);
			createQuery.setParameter("ministro", ministro);
			
			return (Long) createQuery.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
