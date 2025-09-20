package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoPublicadoMinistroQuery;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.util.ProcessoPublicadoRelatorioQuery;
import br.gov.stf.estf.processostf.model.util.ConsultaObjetoIncidente;
import br.gov.stf.estf.processostf.model.util.ObjetoIncidenteComConfidencialidadeQuery;
import br.gov.stf.estf.publicacao.model.dataaccess.ProcessoPublicadoDao;
import br.gov.stf.estf.publicacao.model.util.ProcessoEmPautaDynamicRestriction;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoResult;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoVO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@SuppressWarnings("unchecked")
@Repository
public class ProcessoPublicadoDaoHibernate extends GenericHibernateDao<ProcessoPublicado, Long> implements ProcessoPublicadoDao {

	private static final long serialVersionUID = -8293346488357379027L;

	public ProcessoPublicadoDaoHibernate() {
		super(ProcessoPublicado.class);
	}
	
	public Boolean isPublicado(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		Session session;
		try {
			session = retrieveSession();
			StringBuilder hql = new StringBuilder();
			
			hql.append("select count(*) from ProcessoPublicado pp ");
			hql.append("where pp.objetoIncidente.id IN ( select oi.id from ObjetoIncidente oi where oi.principal.id = ");
			hql.append(objetoIncidente.getId() + " and oi.tipoObjetoIncidente in ('PR','RC','IJ') )");
			
			Query query = session.createQuery(hql.toString());
			Long totalPublicado = (Long)query.uniqueResult();
			if (totalPublicado == 0) {
				return false;
			} else {
				return true;
			}
		} catch (DaoException e) {
			throw new DaoException(e);
		}

	}


	public List<ProcessoPublicado> pesquisarProcessosAta(Integer capitulo, Integer materia, Short ano, Integer numero,
			String siglaProcessual, Long numeroProcessual, Long tipoRecurso, Long tipoJulgamento, Date dataSessao,
			Long codigoSetor, Boolean recuperarOcultos) throws DaoException {
		List<ProcessoPublicado> result = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer(" SELECT pp FROM ProcessoPublicado pp ");

			if (dataSessao != null) {
				hql.append(", ConteudoPublicacao cp ");
			}

			hql.append(" JOIN FETCH pp.objetoIncidente oi ");
			hql.append(" JOIN FETCH pp.objetoIncidente.principal  ");
			hql.append(" WHERE 1=1 ");

			if (dataSessao != null) {
				hql.append(" AND pp.codigoCapitulo = cp.codigoCapitulo ");
				hql.append(" AND pp.codigoMateria = cp.codigoMateria ");
				hql.append(" AND pp.anoMateria = cp.ano ");
				hql.append(" AND pp.numeroMateria = cp.numero ");
				hql.append(" AND cp.codigoConteudo = :codigoConteudo ");
				hql.append(" AND cp.dataCriacao = :dataCriacao ");
			}

			if (codigoSetor != null) {
				hql.append(" AND NOT EXISTS ( ");
				hql.append(" SELECT ap FROM AndamentoProcesso ap  ");
				hql.append(" WHERE pp.objetoIncidente.id = ap.objetoIncidente.id ");
				hql.append(" AND ap.setor.id = :codigoSetor )");
			}

			if (capitulo != null && capitulo > 0) {
				hql.append(" AND pp.codigoCapitulo = :codigoCapitulo ");
			}
			if (materia != null && materia > 0) {
				hql.append("AND pp.codigoMateria = :codigoMateria ");
			}
			if (ano != null && ano > 0) {
				hql.append("AND pp.anoMateria = :anoMateria ");
			}
			if (numero != null && numero > 0) {
				hql.append("AND pp.numeroMateria = :numeroMateria ");
			}
			if (siglaProcessual != null && siglaProcessual.trim().length() > 0) {
				hql.append("AND pp.objetoIncidente.principal.classeProcessual.id = :siglaClasseProcessual ");
			}
			if (numeroProcessual != null && numeroProcessual > 0) {
				hql.append("AND pp.objetoIncidente.principal.numeroProcessual = :numeroProcesso ");
			}
			if (tipoRecurso != null && tipoRecurso > 0) {
				hql.append("AND pp.tipoRecurso.id = :tipoRecurso ");
			}
			if (tipoJulgamento != null && tipoJulgamento > 0) {
				hql.append("AND pp.tipoJulgamento.id = :tipoJulgamento ");
			}

			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("oi", recuperarOcultos));
			
			Query query = session.createQuery(hql.toString());

			if (capitulo != null && capitulo > 0) {
				query.setInteger("codigoCapitulo", capitulo);
			}
			if (materia != null && materia > 0) {
				query.setInteger("codigoMateria", materia);
			}
			if (ano != null && ano > 0) {
				query.setShort("anoMateria", ano);
			}
			if (numero != null && numero > 0) {
				query.setInteger("numeroMateria", numero);
			}
			if (siglaProcessual != null && siglaProcessual.trim().length() > 0) {
				query.setString("siglaClasseProcessual", siglaProcessual);
			}
			if (numeroProcessual != null && numeroProcessual > 0) {
				query.setLong("numeroProcesso", numeroProcessual);
			}
			if (tipoRecurso != null && tipoRecurso > 0) {
				query.setLong("tipoRecurso", tipoRecurso);
			}
			if (tipoJulgamento != null && tipoJulgamento > 0) {
				query.setLong("tipoJulgamento", tipoJulgamento);
			}
			if (dataSessao != null) {
				query.setDate("dataCriacao", dataSessao);
				query.setInteger("codigoConteudo", 50);
			}
			if (codigoSetor != null) {
				query.setLong("codigoSetor", codigoSetor);
			}

			result = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	public List<ProcessoPublicado> pesquisarProcessosDj(Integer capitulo, Integer materia, Short ano, Integer numero,
			Boolean recuperarOcultos)
			throws DaoException {
		List<ProcessoPublicado> lista = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT pp FROM ProcessoPublicado pp ");
			hql.append("JOIN FETCH pp.objetoIncidente oi ");			
			hql.append("JOIN FETCH oi.principal ");
			hql.append("WHERE pp.codigoCapitulo = ? ");
			hql.append("AND pp.codigoMateria = ? ");
			hql.append("AND pp.anoMateria = ? ");
			hql.append("AND pp.numeroMateria = ? ");

			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("oi", recuperarOcultos));			
			
			
			Query query = session.createQuery(hql.toString());
			query.setInteger(0, capitulo);
			query.setInteger(1, materia);
			query.setShort(2, ano);
			query.setInteger(3, numero);

			lista = query.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return lista;
	}

	public List<ProcessoPublicado> pesquisarSessaoEspecial(Boolean recuperarOcultos, String... siglaClasseProcessual) throws DaoException {
		List<ProcessoPublicado> processos = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT pp FROM ProcessoPublicado pp, ConteudoPublicacao cp, Processo p ");
			hql.append(" JOIN FETCH pp.objetoIncidente oi  ");
			hql.append(" JOIN FETCH oi.principal pr ");

			hql.append(" WHERE cp.ano = pp.anoMateria ");
			hql.append(" AND cp.numero = pp.numeroMateria ");
			hql.append(" AND cp.codigoCapitulo = pp.codigoCapitulo ");
			hql.append(" AND cp.codigoMateria = pp.codigoMateria ");

			hql.append(" AND cp.dataComposicaoParcial IS NOT NULL ");
			hql.append(" AND cp.dataComposicaoDj IS NULL ");

			hql.append(" AND cp.codigoConteudo = ? ");
			hql.append(" AND pp.codigoMateria in (?, ? ) ");
			hql.append(" AND pp.codigoCapitulo = ? ");
			
			hql.append(" AND pr = p ");
			hql.append(" AND p.siglaClasseProcessual IN ( :classes ) ");
			
			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("oi", recuperarOcultos));			
			
			Query q = session.createQuery(hql.toString());

			q.setInteger(0, 50);
			q.setInteger(1, 3);
			q.setInteger(2, 4);
			q.setInteger(3, EstruturaPublicacao.COD_CAPITULO_PLENARIO);
			q.setParameterList("classes", siglaClasseProcessual);

			processos = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return processos;
	}

	public List<Object[]> pesquisarProcessosAta(ProcessoPublicadoMinistroQuery query, ConsultaObjetoIncidente coi,
			Boolean recuperarOcultos) throws DaoException {

		List<Object[]> result = null;

		Date dataSessao = query.getDataSessao();
		Long codigoSetor = query.getCodigoSetor();
		Integer capitulo = query.getCapitulo();
		Integer[] materias = query.getMateria();
		Short ano = query.getAno();
		Integer numero = query.getNumero();
		Long codigoMinistroRelator = query.getCodigoMinistroRelator();
		Long codigoAndamento = query.getCodigoAndamento();
		String tipoMeioProcesso = query.getTipoMeioProcesso();

		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer(
					" SELECT pp, p.ministroRelatorAtual , t FROM ProcessoPublicado pp, Processo p, Texto t, ConteudoPublicacao cp ");

			if (codigoAndamento != null && codigoAndamento > 0) {
				hql.append(" , AndamentoProcesso ap ");
			}
			
			hql.append(" JOIN FETCH pp.objetoIncidente oi ");
			hql.append(" JOIN FETCH oi.principal pr ");
			hql.append(" JOIN FETCH t ");

			hql.append(" WHERE 1=1 ");

			hql.append(" AND pp.codigoCapitulo = cp.codigoCapitulo ");
			hql.append(" AND pp.codigoMateria = cp.codigoMateria ");
			hql.append(" AND pp.anoMateria = cp.ano ");
			hql.append(" AND pp.numeroMateria = cp.numero ");
			hql.append(" AND cp.codigoConteudo = :codigoConteudo ");
			
			//hql.append(" AND cp.publicacao.formatoOnline = 'N' ");
			
			hql.append(" AND pr = p ");

			if (codigoMinistroRelator != null && codigoMinistroRelator > 0) {
				hql.append(" AND p.ministroRelatorAtual.id = :ministroRelator ");
			}
			
			if (tipoMeioProcesso != null && !tipoMeioProcesso.equals("T")){
				hql.append(" AND p.tipoMeioProcesso= :siglaTipoMeioProcesso ");
			}

			// RECUPERAR O TEXTO DE DECISAO DO PROCESSO
			hql.append(" AND t.objetoIncidente = oi ");
			hql.append(" AND t.tipoTexto = :tipoTexto ");
			hql.append(" AND t.dataSessao = cp.dataCriacao ");

			if (dataSessao != null) {
				hql.append(" AND cp.dataCriacao = :dataCriacao ");
			}

			// LISTA OS PROCESSOS QUE NÃO POSSUEM ANDAMENTO
			if (codigoSetor != null && codigoSetor > 0) {
				hql.append(" AND NOT EXISTS ( ");
				hql.append(" SELECT ap FROM AndamentoProcesso ap  ");
				hql.append(" WHERE pp.objetoIncidente = ap.objetoIncidente ");
				hql.append(" AND ap.setor.id = :codigoSetor ");
				hql.append(" AND ap.dataAndamento >= cp.dataCriacao ) ");
			}

			// RECUPERAR PELO ANDAMENTO
			if (codigoAndamento != null && codigoAndamento > 0) {
				hql.append(" AND oi = ap.objetoIncideten ");
				hql.append(" AND ap.codigoAndamento = :codigoAndamento ");
			}

			if (capitulo != null && capitulo > 0) {
				hql.append(" AND pp.codigoCapitulo = :codigoCapitulo ");
			}

			if (materias != null && materias.length > 0) {
				hql.append(" AND pp.codigoMateria IN ( :codigoMateria ) ");
			}

			if (ano != null && ano > 0) {
				hql.append(" AND pp.anoMateria = :anoMateria ");
			}

			if (numero != null && numero > 0) {
				hql.append(" AND pp.numeroMateria = :numeroMateria ");
			}


			if ( coi.isJoin() ) {
				hql.append( coi.getQueryObjetoIncidente("pp.objetoIncidente") );
			}
			
			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("oi", recuperarOcultos));
			

			Query q = coi.createQuery(session, hql.toString());
			
			q.setLong("tipoTexto", TipoTexto.DECISAO.getCodigo());
			q.setInteger("codigoConteudo", 50);

			if (capitulo != null && capitulo > 0) {
				q.setInteger("codigoCapitulo", capitulo);
			}

			if (materias != null && materias.length > 0) {
				q.setParameterList("codigoMateria", materias);
			}

			if (ano != null && ano > 0) {
				q.setShort("anoMateria", ano);
			}

			if (numero != null && numero > 0) {
				q.setInteger("numeroMateria", numero);
			}

			if (dataSessao != null) {
				q.setDate("dataCriacao", dataSessao);
			}
			if (codigoSetor != null) {
				q.setLong("codigoSetor", codigoSetor);
			}

			if (codigoMinistroRelator != null && codigoMinistroRelator > 0) {
				q.setLong("ministroRelator", codigoMinistroRelator);
			}
			
			if (tipoMeioProcesso != null && !tipoMeioProcesso.equals("T")){
				q.setString("siglaTipoMeioProcesso", tipoMeioProcesso);
			}

			if (codigoAndamento != null && codigoAndamento > 0) {
				q.setLong("codigoAndamento", codigoAndamento);
			}

			result = (List<Object[]>) q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	public List<Object[]> pesquisarProcessosAtaSemTexto(ProcessoPublicadoMinistroQuery query, ConsultaObjetoIncidente coi,
			Boolean recuperarOcultos) throws DaoException {
		List<Object[]> result = null;

		Date dataSessao = query.getDataSessao();
		Long codigoSetor = query.getCodigoSetor();
		Integer capitulo = query.getCapitulo();
		Integer[] materias = query.getMateria();
		Short ano = query.getAno();
		Integer numero = query.getNumero();
		Long codigoMinistroRelator = query.getCodigoMinistroRelator();
		String tipoMeioProcesso = query.getTipoMeioProcesso();
		String confidencialidade = query.getConfidencialidade();

		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer(
					" SELECT pp, p.ministroRelatorAtual FROM ProcessoPublicado pp, Processo p ");

			hql.append(", ConteudoPublicacao cp ");
			
			hql.append(" JOIN FETCH pp.objetoIncidente oi ");
			hql.append(" JOIN FETCH pp.objetoIncidente.principal  ");
			hql.append(" LEFT JOIN FETCH pp.objetoIncidente.anterior ");
			
			hql.append(" WHERE 1=1 ");

			hql.append(" AND pp.codigoCapitulo = cp.codigoCapitulo ");
			hql.append(" AND pp.codigoMateria = cp.codigoMateria ");
			hql.append(" AND pp.anoMateria = cp.ano ");
			hql.append(" AND pp.numeroMateria = cp.numero ");
			hql.append(" AND cp.codigoConteudo = 50 ");
			
			//hql.append(" AND cp.publicacao.formatoOnline = 'N' ");
			
			if (dataSessao != null) {
				hql.append(" AND cp.dataCriacao = :dataSessao ");
			}
			
			if(confidencialidade != null) {
				hql.append(" AND oi.tipoConfidencialidade = :confidencialidade");
			}

			// RECUPERAR O MINISTRO RELATOR DO PROCESSO
			hql.append(" AND pp.objetoIncidente.principal = p ");

			if (codigoMinistroRelator != null && codigoMinistroRelator > 0) {
				hql.append(" AND p.ministroRelatorAtual.id = :ministroRelator ");
			}
			
			if (tipoMeioProcesso != null && !tipoMeioProcesso.equals("T")){
				hql.append(" AND p.tipoMeioProcesso= :siglaTipoMeioProcesso ");
			}

			// LISTA OS PROCESSOS QUE NÃO POSSUEM ANDAMENTO
			if (codigoSetor != null && codigoSetor > 0) {
				hql.append(" AND NOT EXISTS ( ");
				hql.append(" SELECT ap FROM AndamentoProcesso ap  ");
				hql.append(" WHERE pp.objetoIncidente = ap.objetoIncidente ");
				hql.append(" AND ap.setor.id = :codigoSetor ");
				hql.append(" AND ap.dataAndamento >= cp.dataCriacao ) ");
			}

			if (capitulo != null && capitulo > 0) {
				hql.append(" AND pp.codigoCapitulo = :codigoCapitulo ");
			}
			if (materias != null && materias.length > 0) {
				hql.append(" AND pp.codigoMateria IN ( :codigoMateria ) ");
			}
			if (ano != null && ano > 0) {
				hql.append(" AND pp.anoMateria = :anoMateria ");
			}
			if (numero != null && numero > 0) {
				hql.append(" AND pp.numeroMateria = :numeroMateria ");
			}
			
			if ( coi.isJoin() ) {
				hql.append( coi.getQueryObjetoIncidente("pp.objetoIncidente") );
			}
			
			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("oi", recuperarOcultos));			

			Query q = coi.createQuery(session, hql.toString());
			
			if (capitulo != null && capitulo > 0) {
				q.setInteger("codigoCapitulo", capitulo);
			}
			if (materias != null && materias.length > 0) {
				q.setParameterList("codigoMateria", materias);
			}
			if (ano != null && ano > 0) {
				q.setShort("anoMateria", ano);
			}
			if (numero != null && numero > 0) {
				q.setInteger("numeroMateria", numero);
			}
			if (dataSessao != null) {
				q.setDate("dataSessao", dataSessao);
			}
			if(confidencialidade != null) {
				q.setString("confidencialidade", confidencialidade);
			}
			
			if (codigoSetor != null) {
				q.setLong("codigoSetor", codigoSetor);
			}
			if (codigoMinistroRelator != null && codigoMinistroRelator > 0) {
				q.setLong("ministroRelator", codigoMinistroRelator);
			}
			if (tipoMeioProcesso != null && !tipoMeioProcesso.equals("T")){
				q.setString("siglaTipoMeioProcesso", tipoMeioProcesso);
			}

			result = (List<Object[]>) q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return result;
	}

	public List<ProcessoPublicado> pesquisarProcessosEmPautaDeJulgamento(ProcessoEmPautaDynamicRestriction consultaDinamica)
			throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(ProcessoPublicado.class,
				ProcessoEmPautaDynamicRestriction.ALIAS_PROCESSO_PUBLICADO);
		criteria.createAlias(ProcessoEmPautaDynamicRestriction.ALIAS_PROCESSO_PUBLICADO + ".objetoIncidente", "oi");
		
		consultaDinamica.addCriteriaRestrictions(criteria);
		
		ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_Criteria(criteria, "oi", consultaDinamica.getRecuperarOcultos());
		
		return criteria.list();
	}
	
	public List<ProcessoPublicado> pesquisarProcessosRelatorio(ProcessoPublicadoRelatorioQuery query) throws DaoException {
		List<ProcessoPublicado> processos = null;
		
		Integer codigoCapitulo = query.getCapitulo();
		Integer codigoMateria = query.getMateria();
		Integer numero = query.getNumero();
		Short ano = query.getAno();
		Date dataSessao = query.getDataSessao();
		ProcessoPublicadoRelatorioQuery.TipoOrdenacao tipoOrdenacao = query.getTipoOrdenacao();
		
		try {
			
			
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer(" SELECT pp FROM ProcessoPublicado pp ");
			
			if ( ProcessoPublicadoRelatorioQuery.TipoOrdenacao.MINISTRO.equals( tipoOrdenacao ) ) {
				hql.append(" , Processo p ");
			}
			
			if ( dataSessao!=null ) {
				hql.append(" , ConteudoPublicacao cp ");
			}

			hql.append(" JOIN FETCH pp.objetoIncidente oi ");
			
			hql.append(" WHERE 1=1 ");
			
			if ( dataSessao!=null ) {
				hql.append(" AND pp.codigoCapitulo = cp.codigoCapitulo ");
				hql.append(" AND pp.codigoMateria = cp.codigoMateria ");
				hql.append(" AND pp.anoMateria = cp.ano ");
				hql.append(" AND pp.numeroMateria = cp.numero ");
				hql.append(" AND cp.codigoConteudo = 50 ");
			}

			// RECUPERAR O MINISTRO RELATOR DO PROCESSO
			if ( ProcessoPublicadoRelatorioQuery.TipoOrdenacao.MINISTRO.equals( tipoOrdenacao ) ) {
				hql.append(" AND pp.objetoIncidente.principal = p ");
			}

			if (dataSessao != null) {
				hql.append(" AND cp.dataCriacao = :dataSessao ");
			}

			if (codigoCapitulo != null && codigoCapitulo > 0) {
				hql.append(" AND pp.codigoCapitulo = :codigoCapitulo ");
			}
			
			if (codigoMateria != null && codigoMateria > 0) {
				hql.append(" AND pp.codigoMateria = :codigoMateria ");
			}
			
			if (ano != null && ano > 0) {
				hql.append(" AND pp.anoMateria = :anoMateria ");
			}
			
			if (numero != null && numero > 0) {
				hql.append(" AND pp.numeroMateria = :numeroMateria ");
			}
			
			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("oi", query.getRecuperarOcultos()));
			
			
			if ( ProcessoPublicadoRelatorioQuery.TipoOrdenacao.MINISTRO.equals( tipoOrdenacao ) ) {
				hql.append(" ORDER BY p.ministroRelatorAtual.id ASC  ");
			} 
			
			Query q = session.createQuery(hql.toString());

			if (codigoCapitulo != null && codigoCapitulo > 0) {
				q.setInteger("codigoCapitulo", codigoCapitulo);
			}
			
			if (codigoMateria != null && codigoMateria > 0) {
				q.setInteger("codigoMateria", codigoMateria);
			}
			
			if (ano != null && ano > 0) {
				q.setShort("anoMateria", ano);
			}
			
			if (numero != null && numero > 0) {
				q.setInteger("numeroMateria", numero);
			}
			
			if (dataSessao != null) {
				q.setDate("dataSessao", dataSessao);
			}
			
			processos = q.list();			
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return processos;
	}
	
	public ProcessoPublicado recuperar(Integer codigoCapitulo,
			Integer codigoMateria, Integer numeroMateria, Short anoMateria,
			String siglaProcesso, Long numeroProcesso, Long tipoRecurso,
			Long tipoJulgamento,
			Boolean recuperarOcultos) throws DaoException {
		ProcessoPublicado processo = null;
		try{
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ProcessoPublicado.class);
			c.createAlias("objetoIncidente", "oi");
			c.add( Restrictions.eq("codigoCapitulo", codigoCapitulo) );
			c.add( Restrictions.eq("codigoMateria", codigoMateria) );
			c.add( Restrictions.eq("numeroMateria", numeroMateria) );
			c.add( Restrictions.eq("anoMateria", anoMateria) );
			c.add( Restrictions.eq("classeProcessual.id", siglaProcesso) );
			c.add( Restrictions.eq("processo.id.numeroProcessual", numeroProcesso) );
			c.add( Restrictions.eq("tipoRecurso.id", tipoRecurso) );
			c.add( Restrictions.eq("tipoJulgamento.id", tipoJulgamento) );
			
			ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_Criteria(c, "oi", recuperarOcultos);
			
			processo = (ProcessoPublicado) c.uniqueResult();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return processo;
	}
	
	
	public ProcessoPublicado recuperar (Integer codigoCapitulo, Integer codigoMateria, 
									    Integer numeroMateria, Short anoMateria, 
									    ObjetoIncidente<?> objetoIncidente,
									    ArquivoEletronico arquivoEletronicoTexto) throws DaoException {
		ProcessoPublicado processo = null;
		try{
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ProcessoPublicado.class);
			if ( codigoCapitulo != null ) {
				c.add( Restrictions.eq("codigoCapitulo", codigoCapitulo) );	
			}
			if ( codigoMateria != null ) {
				c.add( Restrictions.eq("codigoMateria", codigoMateria) );	
			}
			if( numeroMateria != null ){
				c.add( Restrictions.eq("numeroMateria", numeroMateria) );	
			}
			if ( anoMateria != null ){
				c.add( Restrictions.eq("anoMateria", anoMateria) );
			}
			c.add( Restrictions.eq("arquivoEletronicoTexto", arquivoEletronicoTexto));
			c.add( Restrictions.eq("objetoIncidente", objetoIncidente));
			processo = (ProcessoPublicado) c.uniqueResult();			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return processo;
		
		
	}


	public List<ProcessoPublicado> pesquisar(Integer codigoCapitulo,
			Integer codigoMateria, ObjetoIncidente objetoIncidente,
			Boolean recuperarOcultos)
			throws DaoException {
		List<ProcessoPublicado> processos = null;
		
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ProcessoPublicado.class);
			c.createAlias("objetoIncidente", "oi");
			c.add( Restrictions.eq("codigoCapitulo", codigoCapitulo) );
			c.add( Restrictions.eq("codigoMateria", codigoMateria) );
			c.add( Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()) );
			
			ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_Criteria(c, "oi", recuperarOcultos);
			
			processos = c.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return processos;
	}

	public List<ProcessoPublicadoResult> pesquisarProcessoPublicadoAcordao(
			Date dataPublicacao, Short numeroDJe, Short anoDJe)
			throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT new br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoResult(oi, pr, ministro, t.dataSessao, publicacao.dataPublicacaoDj, prim) ");
			hql.append(" FROM ObjetoIncidente oi, Ministro ministro, Processo pr, Publicacao publicacao, ConteudoPublicacao cp, ProcessoPublicado pp, Texto t, ControleVoto cv, ProcessoImagem prim ");
			hql.append(" WHERE oi.principal = pr ");
			hql.append(" AND pr.ministroRelatorAtual = ministro ");
			hql.append(" AND oi = pp.objetoIncidente ");
			hql.append(" AND pp.codigoCapitulo = cp.codigoCapitulo ");
			hql.append(" AND pp.codigoMateria = cp.codigoMateria ");
			hql.append(" AND pp.anoMateria = cp.ano ");
			hql.append(" AND pp.numeroMateria = cp.numero ");
			hql.append(" AND cp.codigoCapitulo = :codigoCapitulo ");
			hql.append(" AND cp.codigoConteudo = :codigoConteudo ");
			hql.append(" AND publicacao = cp.publicacao ");
			hql.append(" AND t.objetoIncidente = oi ");
			hql.append(" AND t.dataSessao IS NOT NULL ");
			hql.append(" AND t.publico = :flgPublico ");
			hql.append(" AND t.tipoTexto = :tipoTexto ");
			hql.append(" AND cv.dataSessao = t.dataSessao ");
			hql.append(" AND cv.objetoIncidente = t.objetoIncidente ");
			hql.append(" AND cv.ministro = t.ministro ");
			hql.append(" AND cv.tipoTexto = t.tipoTexto ");
			hql.append(" AND cv.tipoSituacaoTexto = :tipoSituacaoTexto ");
			hql.append(" AND prim.objetoIncidente (+) = oi ");
			hql.append(" AND publicacao.dataPublicacaoDj IS NOT NULL ");
			
			if (dataPublicacao != null) {
				hql.append(" AND publicacao.dataPublicacaoDj = :dataPublicacao ");
			}
			if (numeroDJe != null && numeroDJe > 0) {
				hql.append(" AND publicacao.numeroEdicaoDje = :numeroDJe ");
			}
			if (anoDJe != null && anoDJe > 0) {
				hql.append(" AND publicacao.anoEdicaoDje = :anoDJe ");
			}
			
			hql.append(" ORDER BY pr.siglaClasseProcessual, pr.numeroProcessual ");
			
			Query q = session.createQuery(hql.toString());
			
			q.setInteger("codigoCapitulo", EstruturaPublicacao.COD_CAPITULO_ACORDAOS);
			q.setInteger("codigoConteudo", EstruturaPublicacao.COD_CONTEUDO_RELACAO_PROCESSO);
			q.setLong("tipoTexto", TipoTexto.ACORDAO.getCodigo());
			q.setString("flgPublico", "S");
			q.setLong("tipoSituacaoTexto", TipoSituacaoTexto.ATIVO_NO_CONTROLE_DE_VOTOS.getCodigo());
			if (dataPublicacao != null) {
				q.setDate("dataPublicacao", dataPublicacao);
			}
			if (numeroDJe != null && numeroDJe > 0) {
				q.setShort("numeroDJe", numeroDJe);
			}
			if (anoDJe != null && anoDJe > 0) {
				q.setShort("anoDJe", anoDJe);
			}
			
			return q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public List<ProcessoPublicadoVO> pesquisar(Date dataInicialPublicacaoDje, Date dataFinalPublicacaoDJe) throws DaoException {
		try {
			Session sessao = retrieveSession();			
			StringBuilder sqlBuilder = new StringBuilder();
			
			sqlBuilder.append(" SELECT DISTINCT (pi.seq_objeto_incidente) AS seqObjetoIncidente, c.sig_classe AS siglaClasse, ")
				.append("c.cmp_classe AS complementoClasse, pi.num_processo AS numeroProcesso, pp.num_materia AS numeroMateria, pp.ano_materia AS anoMateria, ")
				.append("pub.dat_publicacao_dj AS dataPublicacaoDj, pub.dat_divulgacao_dje AS dataDivulgacaoDje, pi.tip_colecao AS tipoColecao ")
				.append(" FROM stf.data_publicacoes pub, stf.materias cp, stf.processo_publicados pp, stf.processos_img pi, stf.classe_unif c ")
				.append(" WHERE (1 = 1) ")
				.append(" AND pub.dat_publicacao_dj BETWEEN ? AND ? ")
				.append(" AND cp.seq_data_publicacoes = pub.seq_data_publicacoes ")
				.append(" AND pp.cod_capitulo = cp.cod_capitulo ")
				.append(" AND pp.cod_materia = cp.cod_materia ")
				.append(" AND pp.num_materia = cp.num_materia ")
				.append(" AND pp.ano_materia = cp.ano_materia ")
				.append(" AND pi.seq_objeto_incidente = pp.seq_objeto_incidente ")
				.append(" AND pi.cod_classe = c.cod_classe ")
				.append(" AND cp.cod_conteudo = ? ");
			
			SQLQuery query = sessao.createSQLQuery(sqlBuilder.toString());
			
			query.addScalar("seqObjetoIncidente", Hibernate.LONG).addScalar("siglaClasse").addScalar("complementoClasse")
				.addScalar("numeroProcesso", Hibernate.LONG).addScalar("numeroMateria", Hibernate.INTEGER).addScalar("anoMateria", Hibernate.INTEGER)
				.addScalar("dataPublicacaoDj", Hibernate.DATE).addScalar("dataDivulgacaoDje", Hibernate.DATE).addScalar("tipoColecao");
			
			query.setDate(0, dataInicialPublicacaoDje);
			query.setDate(1, dataFinalPublicacaoDJe);
			query.setLong(2, EstruturaPublicacao.COD_CONTEUDO_RELACAO_PROCESSO);
			
			query.setResultTransformer(Transformers.aliasToBean(ProcessoPublicadoVO.class));
			
			return query.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}


	@Override
	public List<ProcessoPublicado> pesquisarProcessosPublicados(Integer codigoCapitulo,
			ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ProcessoPublicado.class);
			
			c.add(Restrictions.eq("codigoCapitulo", codigoCapitulo));
			c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
			
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public Boolean existeObjetoIncidente(ProcessoPublicado processo) throws DaoException{
		try {
			
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer(" SELECT count(*) FROM ProcessoPublicado pp INNER JOIN pp.objetoIncidente WHERE pp.id = :id");
			
			Query query = session.createQuery(hql.toString());

			if (processo != null) {
				query.setLong("id", processo.getId());
			
				Long result =  0L;
				result = (Long) query.uniqueResult();
			
				if(result>0){
					return true;
				}
			}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}