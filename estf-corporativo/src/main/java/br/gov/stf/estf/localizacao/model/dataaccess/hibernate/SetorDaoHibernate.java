package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.localizacao.model.dataaccess.SetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.Flag;

@SuppressWarnings("unchecked")
@Repository
public class SetorDaoHibernate extends GenericHibernateDao<Setor, Long> implements SetorDao {

	private static final String SIGLA_GABINETE_VICE_PRESIDENCIA = "GVPR";
	/**
	 * 
	 */
	private static final long serialVersionUID = -3043124466884566254L;

	public SetorDaoHibernate() {
		super(Setor.class);
	}

	public List<Setor> pesquisar(Integer[] codigoCapitulo) throws DaoException {
		List<Setor> lista = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Setor.class);
			c.add(Restrictions.in("codigoCapitulo", codigoCapitulo));
			c.add(Restrictions.eq("ativo", true));
			lista = c.list();
		} catch (HibernateException e) {
			throw new DaoException(e);
		}
		return lista;
	}

	public List<Setor> pesquisar(Long[] listaCodigoSetor) throws DaoException {
		List<Setor> listaSetor = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Setor.class);
			c.add(Restrictions.in("id", listaCodigoSetor));
			c.add(Restrictions.eq("ativo", true));
			listaSetor = c.list();
		} catch (HibernateException e) {
			throw new DaoException(e);
		}
		return listaSetor;
	}

	public List<Setor> recuperarSetorMinistrosAtivos() throws DaoException {
		List<Setor> lista = new LinkedList<Setor>();
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Ministro.class);
			Criteria cSetor = c.createCriteria("setor");
			cSetor.add(Restrictions.eq("ativo", true));
			cSetor.addOrder(Order.asc("nome"));
			List<Ministro> listaMinistros = c.list();

			for (Ministro min : listaMinistros) {
				lista.add(min.getSetor());
			}

		} catch (HibernateException e) {
			throw new DaoException(e);
		}
		return lista;
	}

	public Boolean alterarGuiaSetor(Setor setor) throws DaoException {
		Boolean resultado = Boolean.FALSE;
		// Setor setorIncrementarGuia = null;

		try {
			Session session = retrieveSession();
			session.evict(setor);

			incrementaGuia(setor, session);

			session.flush();
			session.close();
			resultado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException(e);
		}

		return resultado;
	}

	public void incrementarGuiaDoSetor(Setor setor) throws DaoException {
		try {
			Session session = retrieveSession();
			incrementaGuia(setor, session);
		} catch (HibernateException e) {
			throw new DaoException(e);
		}
	}

	private void incrementaGuia(Setor setor, Session session) {
		Setor setorIncrementarGuia;
		Criteria criteria = session.createCriteria(Setor.class);

		if (setor != null)
			criteria.add(Restrictions.eq("id", setor.getId()));

		criteria.add(Restrictions.eq("ativo", true));
		setorIncrementarGuia = (Setor) criteria.uniqueResult();

		session.lock(setorIncrementarGuia, LockMode.UPGRADE);
		atualizarDadosDaGuiaNoSetor(setor, setorIncrementarGuia);
		session.saveOrUpdate(setorIncrementarGuia);
	}

	private void atualizarDadosDaGuiaNoSetor(Setor setorOriginal, Setor setorIncrementarGuia) {
		short ano = (short) Calendar.getInstance().get(Calendar.YEAR);
		// Se o ano da guia for igual ao atual, só incrementa o valor
		if (setorIncrementarGuia.getNumeroAnoGuia() != null && setorIncrementarGuia.getNumeroAnoGuia().equals(ano)) {
			setorIncrementarGuia.setNumeroProximaGuia(setorIncrementarGuia.getNumeroProximaGuia() + 1);
		} else {
			// Caso contrário, deve atualizar o ano e colocar o número da guia
			// para 1
			setorIncrementarGuia.setNumeroAnoGuia(ano);
			setorIncrementarGuia.setNumeroProximaGuia(1L);
		}
	}

	public List<Setor> pesquisarSetoresGabinete() throws DaoException {
		List<Setor> setores = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("select s from Ministro m join m.setor s where m.dataAfastamento is null");

			Query q = session.createQuery(hql.toString());
			setores = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return setores;
	}

	public Setor recuperarSetorPorId(Long id) throws DaoException {
		Setor setores = null;

		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append("select s from Setores s where s.id = " + id);

			Query q = session.createQuery(hql.toString());
			setores = (Setor) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return setores;
	}

	public List<Setor> recuperarSetorPorIdOuDescricao(String id) throws DaoException {

		List<Setor> setores = null;

		try {
			Session session = retrieveSession();

			StringBuilder sql = new StringBuilder();
			String hql = "select s from Setor s where (s.id like '%" + id + "%' or s.nome like '%" + id.toUpperCase() + "%') and s.ativo = 'S'";

			Query q = session.createQuery(hql);
			setores = q.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return setores;
	}

	// o parâmetro deslocaProcesso indica quando true que deverá filtrar apenas os setores de destinos válidos para deslocamento de processo
	public List<Setor> recuperarSetorPorIdOuDescricao(String id, boolean deslocaProcesso) throws DaoException {

		List<Setor> setores = Collections.emptyList();

		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT s FROM Setor s ");			
			hql.append("WHERE s.ativo = 'S' ");
			
			if(id != null && !id.trim().isEmpty()){
				hql.append("AND (s.id LIKE :id OR s.nome LIKE :idUpperCase) ");
			}
			
			if (deslocaProcesso) {
				hql.append("AND s.deslocaProcesso = 'S' ");
			} else {
				hql.append("AND s.deslocaProcesso = 'N' ");
			}

			Query q = session.createQuery(hql.toString());
			
			if(id != null && !id.trim().isEmpty()){
				q.setParameter("id", "%" + id + "%");
				q.setParameter("idUpperCase", "%" + id.toUpperCase() + "%");
			}
			
			setores = q.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return setores;
	}
	
	// o parâmetro deslocaProcesso indica quando true que deverá filtrar apenas os setores de destinos válidos para deslocamento de processo - ativos e inativos
	public List<Setor> recuperarSetorPorIdOuDescricaoAtivoInativo(String id) throws DaoException {

		List<Setor> setores = null;
		
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT s FROM Setor s ");
			hql.append("WHERE s.id LIKE :id OR s.nome LIKE :idUpperCase ");

			Query q = session.createQuery(hql.toString());
			q.setParameter("id", "%" + id + "%");
			q.setParameter("idUpperCase", "%" + id.toUpperCase() + "%");
			
			
//			String hql = "select s from Setor s where (s.id like '%" + id + "%' or s.nome like '%" + id.toUpperCase() + "%') ";
//			Query q = session.createQuery(hql);
			setores = q.list();
			
			return setores;

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
	
	
	public Setor recuperarSetor(Long id, String sigla) throws DaoException {

		Session session = retrieveSession();

		Setor localizacao = null;

		try {

			Criteria criteria = session.createCriteria(Setor.class);

			if (id != null)
				criteria.add(Restrictions.eq("id", id));

			if (sigla != null && !sigla.trim().equals(""))
				criteria.add(Restrictions.eq("sigla", sigla));

			localizacao = (Setor) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return localizacao;
	}

	public Boolean alterarSetor(Setor localizacao) throws DaoException {

		Session sessao = retrieveSession();

		try {
			sessao.update(localizacao);

			return Boolean.TRUE;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public List<Setor> pesquisarSetores(Boolean ativo) throws DaoException {

		List<Setor> listaSetoresSTF = null;

		Session sessao = retrieveSession();
		try {
			StringBuffer hql = new StringBuffer();
			hql.append("select s from Setor s");
			
			if (ativo != null)
				hql.append(" where s.ativo = :ativo");
			
			hql.append(" ORDER BY s.nome ASC, s.id DESC ");
			Query q = sessao.createQuery(hql.toString());

			if (ativo != null) {
				if (ativo.booleanValue()) {
					q.setString("ativo", "S");
				} else {
					q.setString("ativo","N");
				}
			}
			listaSetoresSTF = q.list();

		} catch (HibernateException ex) {
			throw new DaoException("HibernateException", ex);
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return listaSetoresSTF;
	}

	public List<Setor> pesquisarSetores(Long id, String Sigla, String siglaTipoConfiguracaoSetor, Boolean localizacaoAtivo,
			Boolean configuracaoAtiva) throws DaoException {

		List<Setor> listaSetoresSTF = null;

		Session sessao = retrieveSession();
		try {
			StringBuffer hql = new StringBuffer("SELECT s FROM Setor s ,");

			if (configuracaoAtiva != null
					|| (siglaTipoConfiguracaoSetor != null && siglaTipoConfiguracaoSetor.trim().length() > 0)) {
				hql.append("s.tiposConfiguracao tc");
			}

			hql.append(" WHERE (1=1)");

			if (id != null) {
				hql.append(" AND s.id = " + id);
			}

			if (Sigla != null && Sigla.trim().length() > 0) {
				hql.append(" AND s.sigla = '" + Sigla + "'");
			}

			if (siglaTipoConfiguracaoSetor != null && siglaTipoConfiguracaoSetor.trim().length() > 0) {
				hql.append(" AND tc.sigla = '" + siglaTipoConfiguracaoSetor + "'");
			}

			if (localizacaoAtivo != null) {

				if (localizacaoAtivo) {
					hql.append(" AND s.flagAtivo = '" + Flag.SIM + "'");
				} else {
					hql.append(" AND s.flagAtivo = '" + Flag.NAO + "'");
				}
			}

			if (configuracaoAtiva != null) {

				if (configuracaoAtiva) {
					hql.append(" AND tc.ativo = '" + Flag.SIM + "'");
				} else {
					hql.append(" AND tc.ativo = '" + Flag.NAO + "'");
				}
			}

			Query q = sessao.createQuery(hql.toString());
			listaSetoresSTF = q.list();

		} catch (HibernateException ex) {
			throw new DaoException("HibernateException", ex);
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return listaSetoresSTF;
	}

	public List<Setor> pesquisarSetoresEGab() throws DaoException {

		List<Setor> listaSetoresEGab = null;

		Session sessao = retrieveSession();
		try {
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT DISTINCT s FROM Setor s ");
			hql.append(" INNER JOIN s.tiposConfiguracao tc ");
			hql.append(" WHERE ");
			hql.append(" tc.sigla IN ('EGAB', 'EGAB-E') ");
			hql.append(" ORDER BY s.nome ");

			Query query = sessao.createQuery(hql.toString());

			listaSetoresEGab = query.list();
		} catch (HibernateException ex) {
			throw new DaoException("HibernateException", ex);
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaSetoresEGab;
	}

	/**
	 * Método que recupera todos os gabinetes ativos, incluindo o gabinete da Presidencia e Vice.
	 * @return
	 * @throws DaoException 
	 */
	public List<Setor> pesquisarGabinetesComPresidenciaEVice() throws DaoException {
		Session sessao = retrieveSession();
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT DISTINCT s FROM Ministro m ");
		hql.append(" INNER JOIN m.setor s ");
		hql.append(" WHERE ");
		//Adiciona condição de ser ministro ativo
		hql.append(" m.dataAfastamento is null ");
		//Adiciona condição de vice-presidência
		hql.append(" OR s.sigla = :siglaVicePresidencia ");
		hql.append(" ORDER BY s.sigla ");

		Query query = sessao.createQuery(hql.toString());
		query.setString("siglaVicePresidencia", SIGLA_GABINETE_VICE_PRESIDENCIA);
		return query.list();

	}

	public List<Setor> pesquisarSetores(String sigla, String nome, Boolean ativo, Boolean somenteGabinetesPresidencia)
			throws DaoException {
		List listaSetoresSTF = null;

		Session sessao = retrieveSession();
		try {
			StringBuffer hql = new StringBuffer();
			hql.append("select s from Setor s");
			hql.append(" WHERE 1=1");

			if (sigla != null && sigla.trim().length() > 0) {
				hql.append(" AND s.sigla like '" + sigla.toUpperCase() + "'");
			}

			if (nome != null && nome.trim().length() > 0) {
				hql.append(" AND s.nome like '%" + nome.toUpperCase() + "%'");
			}

			if (somenteGabinetesPresidencia != null && somenteGabinetesPresidencia.booleanValue()) {
				hql.append("and (s.sigla LIKE 'GM%' or s.sigla LIKE 'PRES%')");
			}

			if (ativo != null) {
				if (ativo.booleanValue()) {
					hql.append(" and s.ativo ='S' ");
				} else {
					hql.append(" and s.ativo ='N' ");
				}
			}

			hql.append(" ORDER BY s.sigla ");

			Query q = sessao.createQuery(hql.toString());
			listaSetoresSTF = q.list();

		} catch (HibernateException ex) {
			throw new DaoException("HibernateException", ex);
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return listaSetoresSTF;
	}

	public Boolean incluirConfiguracaoSetor(ConfiguracaoSetor configuracaoSetor) throws DaoException {

		Session sessao = retrieveSession();

		try {
			sessao.save(configuracaoSetor);

			return Boolean.TRUE;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public Boolean excluirConfiguracaoSetor(ConfiguracaoSetor configuracaoSetor) throws DaoException {

		Session sessao = retrieveSession();

		try {
			sessao.delete(configuracaoSetor);
			sessao.flush();

			return Boolean.TRUE;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	public List<Setor> pesquisarSetoresDeslocaComunicacao(boolean deslocaComunicaco) throws DaoException{
		List<Setor> listaSetores = Collections.emptyList();
		Session sessao = retrieveSession();
		
		try{
			
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT s FROM Setor s ");
			hql.append(" WHERE 1=1 ");
			if(deslocaComunicaco){
				hql.append(" AND s.deslocaComunicacao = 'S' ");
			}else{
				hql.append(" AND s.deslocaComunicacao = 'N' ");
			}
			hql.append(" ORDER BY s.nome");

			Query q = sessao.createQuery(hql.toString());
		
			listaSetores = q.list();
			
			
		}catch(RuntimeException e){
			throw new DaoException("RunTimeException", e);
		}
	
		return listaSetores;
	}
	
	@Override
	public List<Setor> pesquisarSetoresAtivosDeslocaComunicacao(boolean deslocaComunicaco) throws DaoException{
		List<Setor> listaSetores = Collections.emptyList();
		Session sessao = retrieveSession();
		
		try{
			
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT s FROM Setor s ");
			hql.append(" WHERE s.ativo = 'S'");
			if(deslocaComunicaco){
				hql.append(" AND s.deslocaComunicacao = 'S' ");
			}else{
				hql.append(" AND s.deslocaComunicacao = 'N' ");
			}
			hql.append(" ORDER BY s.nome");

			Query q = sessao.createQuery(hql.toString());
		
			listaSetores = q.list();
			
			
		}catch(RuntimeException e){
			throw new DaoException("RunTimeException", e);
		}
	
		return listaSetores;
	}

	@Override
	public List<Setor> pesquisarSetoresPorDescricao(String id,Boolean ativo,Boolean deslocaProcesso)
			throws DaoException {
		List<Setor> listaSetores = Collections.emptyList();
		Session sessao = retrieveSession();
		try{
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT s FROM Setor s ");
			hql.append("WHERE upper(s.nome) LIKE upper('%"+ id +"%') ");
			if(ativo != null){
				if(ativo){
					hql.append("AND s.ativo = 'S' ");
				}else{
					hql.append("AND s.ativo = 'N' ");
				}
				
			}
			if(deslocaProcesso != null){
				if (deslocaProcesso) {
					hql.append("AND s.deslocaProcesso = 'S' ");
				} else {
					hql.append("AND s.deslocaProcesso = 'N' ");
				}
			}
			hql.append("ORDER BY s.nome ASC ");
			
			Query query = sessao.createQuery(hql.toString());
			
			listaSetores = query.list();
		
		}catch(HibernateException ex) {
			throw new DaoException("HibernateException",ex);
		}catch(RuntimeException e){
			throw new DaoException("RunTimeException", e);
		}
		
		return listaSetores;
	}

	@Override
	public List<Setor> pesquisarSetoresPorID(Long id, Boolean ativo,Boolean deslocaProcesso)
			throws DaoException {
		List<Setor> listaSetores = Collections.emptyList();
		Session sessao = retrieveSession();
		try{
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT s FROM Setor s ");
			hql.append("WHERE s.id LIKE ('%" + id + "') AND s.id LIKE ('" + id + "%') " );
			
			if(ativo != null){
				if(ativo){
					hql.append("AND s.ativo = 'S' ");
				}else{
					hql.append("AND s.ativo = 'N' ");
				}
				
			}
			if(deslocaProcesso != null){
				if (deslocaProcesso) {
					hql.append("AND s.deslocaProcesso = 'S' ");
				} else {
					hql.append("AND s.deslocaProcesso = 'N' ");
				}
			}
			hql.append("ORDER BY s.id ASC ");
			
			Query query = sessao.createQuery(hql.toString());
			
			listaSetores = query.list();
		
		}catch(HibernateException ex) {
			throw new DaoException("HibernateException",ex);
		}catch(RuntimeException e){
			throw new DaoException("RunTimeException", e);
		}
		
		return listaSetores;
	}
}
