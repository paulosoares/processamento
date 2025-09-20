package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AndamentoDaoHibernate extends
		GenericHibernateDao<Andamento, Long> implements AndamentoDao {

	public AndamentoDaoHibernate() {
		super(Andamento.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5828281070407186206L;

	@SuppressWarnings("unchecked")
	public List<Andamento> pesquisar(Long codigoSetor) throws DaoException {
		List<Andamento> andamentos = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT aa.tipoAndamento ");
			hql.append(" FROM AutorizaAndamento aa ");
			hql.append(" WHERE aa.id.codigoSetor = :codigoSetor ");
			hql.append(" ORDER BY aa.tipoAndamento.descricao ASC");
			Query q = session.createQuery(hql.toString());
			q.setLong("codigoSetor", codigoSetor);

			andamentos = q.list();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return andamentos;
	}
	
    public List<Andamento> pesquisarTipoAndamento(Long id, String descricao, Boolean ativo) 
    throws DaoException {

    	Session session = retrieveSession();
	    List<Andamento> listaTipoAndamento = null;
	    try {
	    	Criteria criteria = session.createCriteria(Andamento.class);

	    	if(id!=null && id>0){
                criteria.add(Restrictions.eq("id", id));
            }
            
            if(descricao != null && !descricao.equals("")){            	
                criteria.add(Restrictions.ilike("descricao", "%"+descricao.toUpperCase()+"%"));
            }
            
            if(ativo != null)
            	criteria.add(Restrictions.eq("andamento", ativo));

            criteria.addOrder(Order.desc("descricao"));
            listaTipoAndamento = criteria.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
	    return listaTipoAndamento;    	
    }
    public Andamento recuperarTipoAndamento(Long id, String descricao) throws DaoException{

    	Session session = retrieveSession();
    	Andamento tipoAndamento = null;
	    try {
	    	Criteria criteria = session.createCriteria(Andamento.class);

	    	if(id != null && id > 0){
                criteria.add(Restrictions.eq("id", id));
            }
            
            if(descricao != null && !descricao.equals("")){            	
                criteria.add(Restrictions.ilike("descricao", "%"+descricao.toUpperCase()+"%"));
            }

            tipoAndamento =(Andamento) criteria.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
	    return tipoAndamento;    	
    }

    public boolean podeLancarAndamentoIndevido(Long codigoSetor) throws DaoException {
		List andamentos = null;
		
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
		
			// Apenas os andamentos autorizados para o setor.
			hql.append(" SELECT count(*) ");
			hql.append(" FROM AutorizaAndamento aa ");
			hql.append(" WHERE aa.id.codigoSetor = :codigoSetor ");
			hql.append(" AND aa.tipoAndamento.id = :codLancamentoIndevido");

			Query q = session.createQuery(hql.toString());
			q.setLong("codigoSetor", codigoSetor);
			q.setLong("codLancamentoIndevido", Andamentos.LANCAMENTO_INDEVIDO.getId());

			andamentos = q.list();
			
			return andamentos != null && ((Long)andamentos.get(0)).longValue() > 0;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
    }

	@Override
	public List<Andamento> pesquisarAndamentosAutorizadosParaLote(Setor setor) throws DaoException {
		List<Andamento> andamentos = null;
		
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
		
			// Apenas os andamentos autorizados para o setor.
			hql.append(" SELECT DISTINCT aa.tipoAndamento ");
			hql.append(" FROM AutorizaAndamento aa ");
			hql.append(" WHERE aa.id.codigoSetor = :codigoSetor ");
			
			hql.append(" AND aa.tipoAndamento.andamentoEspecial <> 'S'");

			hql.append(" AND aa.tipoAndamento.andamento = 'S'");
			
			// Filtrar os andamentos de recurso, pois a aplicação eSTF-Processamento não gera recurso.
			hql.append(" AND aa.tipoAndamento.recurso = 'N'");
			
			// Apenas para andamentos de processo.
			hql.append(" AND aa.id.tipoObjeto = 'PRO' ");

			// Filtrar andamento que não podem lanaçar em lote por causa do seu procedimento.
			hql.append(" AND aa.tipoAndamento.id not in (7600,7601,7700,8527,8528,8507) ");
			
			hql.append(" ORDER BY aa.tipoAndamento.descricao ASC");

			Query q = session.createQuery(hql.toString());
			q.setLong("codigoSetor", setor.getId());

			andamentos = q.list();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return andamentos;
	}

	public List<Andamento> pesquisarAndamentosAutorizados(Long codigoSetor) throws DaoException {
	
		List<Andamento> andamentos = null;
		
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
		
			// Apenas os andamentos autorizados para o setor.
			hql.append(" SELECT DISTINCT aa.tipoAndamento ");
			hql.append(" FROM AutorizaAndamento aa ");
			hql.append(" WHERE aa.id.codigoSetor = :codigoSetor ");
			
			// Filtrar os andamentos de Interposição, Distribuição, Redistribuição e Registro.
			hql.append(" AND aa.tipoAndamento.andamentoEspecial = 'N'");
			
			// Filtrar os andamentos de recurso, pois a aplicação eSTF-Processamento não gera recurso.
			hql.append(" AND aa.tipoAndamento.recurso = 'N'");
			
			// Filtrar andamento de lançamento indevido.
			hql.append(" AND aa.tipoAndamento.id not in (7700) ");
			
			// Apenas para andamentos de processo.
			hql.append(" AND aa.id.tipoObjeto = 'PRO' ");
			
			hql.append(" ORDER BY aa.tipoAndamento.descricao ASC");

			Query q = session.createQuery(hql.toString());
			q.setLong("codigoSetor", codigoSetor);

			andamentos = q.list();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return andamentos;
	}
	
	public List<Andamento> pesquisarAndamentosAutorizadosMock() throws DaoException {
		
		List<Andamento> andamentos = new java.util.ArrayList<Andamento>();
		
		Andamento andamento1 = new Andamento();
		andamento1.setId(6221L);
		andamento1.setDescricao("Homologada a desistência");
		
		Andamento andamento2 = new Andamento();
		andamento2.setId(6225L);
		andamento2.setDescricao("Não provido");
		
		Andamento andamento3 = new Andamento();
		andamento3.setId(6229L);
		andamento3.setDescricao("Procedente em parte");
		
		Andamento andamento4 = new Andamento();
		andamento4.setId(7106L);
		andamento4.setDescricao("Processo findo");
		
		Andamento andamento5 = new Andamento();
		andamento5.setId(2309L);
		andamento5.setDescricao("PROCESSO FINDO");
		
		Andamento andamento6 = new Andamento();
		andamento6.setId(7401L);
		andamento6.setDescricao("Reiterado pedido de informações");
		
		Andamento andamento7 = new Andamento();
		andamento7.setId(6247L);
		andamento7.setDescricao("Agravo provido");

		Andamento andamento8 = new Andamento();
		andamento8.setId(6249L);
		andamento8.setDescricao("Agravo provido e RE pendente de julgamento");

		andamentos.add(andamento7);
		andamentos.add(andamento8);
		andamentos.add(andamento1);
		andamentos.add(andamento2);
		andamentos.add(andamento3);
		andamentos.add(andamento4);
		andamentos.add(andamento5);
		andamentos.add(andamento6);
	
		return andamentos;
	}
	
	public boolean isNotificadoTribunalOrigem(Long seqAndamentoProcesso, Long codAndamento){
		try{
			Session session = retrieveSession();
			String sql;
			sql = "SELECT count(*) as qtd FROM estf.processo_integracao pi WHERE pi.seq_andamento_processo = :seqAndamentoProcesso";
			
			SQLQuery query = session.createSQLQuery(sql);
			
			query.setLong("seqAndamentoProcesso", seqAndamentoProcesso);
			
			List isProcessoIntegrado = query.list();
			
			if(codAndamento ==  7101L || codAndamento == 7104L || codAndamento == 7108L) {
				if(isProcessoIntegrado.get(0).toString().equals("1")){
					return true;
				} 
			}
			
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isGrupoDecisao(Andamento andamento) throws DaoException {

	    try {
			Session session = retrieveSession();
			String sql = "select count(*) from stf.andamentos a where a.cod_andamento = :idAndamento and a.SEQ_GRUPO_ANDAMENTO in (select SEQ_GRUPO_ANDAMENTO from stf.grupo_andamento ga where " +
					"ga.SEQ_GRUPO_ANDAMENTO = 6 or ga.SEQ_PAI_GRUPO_ANDAMENTO = 6)";

			SQLQuery query = session.createSQLQuery(sql);
			query.setLong("idAndamento", andamento.getId());
			List grupoAndamentos = query.list();
			
			return grupoAndamentos != null && ((Number)grupoAndamentos.get(0)).longValue() > 0;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
