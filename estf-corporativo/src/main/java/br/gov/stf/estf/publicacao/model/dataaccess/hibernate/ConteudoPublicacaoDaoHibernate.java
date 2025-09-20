package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.estf.publicacao.model.dataaccess.ConteudoPublicacaoDao;
import br.gov.stf.estf.publicacao.model.util.ConteudoPublicacaoDynamicRestriction;
import br.gov.stf.estf.publicacao.model.util.DadosDePublicacaoDynamicQuery;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.util.query.QueryBuilder;

@SuppressWarnings("unchecked")
@Repository
public class ConteudoPublicacaoDaoHibernate extends GenericHibernateDao<ConteudoPublicacao, Long> implements
		ConteudoPublicacaoDao {

	public ConteudoPublicacaoDaoHibernate() {
		super(ConteudoPublicacao.class);
	}

	private static final long serialVersionUID = -8189422730587505510L;
	
	/**
	 * Pesquisar Materias 
	 * @param codigoCapitulo
	 * @param codigoMateria pesquisa apenas um codigo de Materia
	 * @param listCodigosMateria array com varios codigos de materia
	 * @param codigoConteudo
	 * @param numero
	 * @param anoMateria
	 * @param dataCriacao
	 * @param isPublicado se a data de publicação está ou não em branco
	 * @param isComposto se a data de composição parcial está ou não em branco
	 * @param tipoSessao
	 * @return
	 * @throws DaoException
	 */
	public List<ConteudoPublicacao> pesquisarMateria(Integer codigoCapitulo
													,Integer codigoMateria
													,List<Integer> listCodigosMateria
													,Integer codigoConteudo
													,Integer numero
													,Short anoMateria
													,Date dataCriacao
													,Boolean isPublicado
													,Boolean isComposto
													,TipoSessao tipoSessao) throws DaoException {
		List<ConteudoPublicacao> lista = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ConteudoPublicacao.class);
			if (codigoCapitulo != null) {
				c.add(Restrictions.eq("codigoCapitulo", codigoCapitulo));
			}
			if (codigoMateria != null) {
				c.add(Restrictions.eq("codigoMateria", codigoMateria));
			}else{
				if (listCodigosMateria != null) {
					c.add(Restrictions.in("codigoMateria", listCodigosMateria));
				}
			}
			if (codigoConteudo != null) {
				c.add(Restrictions.eq("codigoConteudo", codigoConteudo));
			}
			if (numero != null) {
				c.add(Restrictions.eq("numero", numero));
			}
			if (anoMateria != null) {
				c.add(Restrictions.eq("ano", anoMateria));
			}
			if (dataCriacao != null) {
				c.add(Restrictions.eq("dataCriacao", dataCriacao));
			}

			if (isPublicado != null) {
				if (isPublicado) {
					c.add(Restrictions.isNotNull("dataComposicaoDj"));
				} else {
					c.add(Restrictions.isNull("dataComposicaoDj"));
				}
			}
			
			if (isComposto != null) {
				if (isComposto) {
					c.add(Restrictions.isNotNull("dataComposicaoParcial"));
				} else {
					c.add(Restrictions.isNull("dataComposicaoParcial"));
				}
			}
			
			if (tipoSessao != null) {
				c.add(Restrictions.eq("tipoSessao", tipoSessao));
			}
			
			c.addOrder(Order.desc("numero"));

			lista = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return lista;
	}
	
	public List<ConteudoPublicacao> pesquisarMateriaDataCriacao(Date dataCriacao) throws DaoException {
		
		List<ConteudoPublicacao> cp = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cp FROM ConteudoPublicacao cp ");
			hql.append(" WHERE cp.dataCriacao = :dataCriacao ");
			
			Query q = session.createQuery( hql.toString() );
			q.setDate("dataCriacao", dataCriacao);
			
			cp = q.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return cp;
	}
	
	public List<ConteudoPublicacao> pesquisarMateriaChaveAntiga(Integer numero, Short anoMateria, Integer codigoCapitulo, Integer... codigoMateria)	throws DaoException {
		List<ConteudoPublicacao> cp = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cp FROM ConteudoPublicacao cp ");
			hql.append(" WHERE cp.numero = :numero ");
			hql.append(" AND cp.ano = :anoMateria ");
			hql.append(" AND cp.codigoCapitulo = :codigoCapitulo ");
			hql.append(" AND cp.codigoMateria in (:codigoMateria) ");
			hql.append(" AND cp.codigoConteudo = 50 ");

			Query q = session.createQuery(hql.toString());
			q.setInteger("numero", numero);
			q.setShort("anoMateria", anoMateria);
			q.setInteger("codigoCapitulo", codigoCapitulo);
			q.setParameterList("codigoMateria", codigoMateria);

			cp = q.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return cp;
	}
	
	
	public ConteudoPublicacao pesquisarMateriaNaoCompostaNaoPublicadaPorSessao(Sessao sessao) throws DaoException {
	
		Session session = retrieveSession();

		try {
			Criteria c = session.createCriteria(ConteudoPublicacao.class, "cp");
			c.add(Restrictions.eq("cp.sessao", sessao));
			c.add(Restrictions.isNull("dataComposicaoDj"));
			c.add(Restrictions.isNull("dataComposicaoParcial"));

			return (ConteudoPublicacao) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public List<ConteudoPublicacao> pesquisar(Integer codigoCapitulo
			                                 ,Integer codigoMateria
			                                 ,Integer codigoConteudo
			                                 ,Integer numero
			                                 ,Short ano
			                                 ,Date dataAta
			                                 ,Boolean isPublicado) throws DaoException {
		
		List<ConteudoPublicacao> lista = this.pesquisarMateria(codigoCapitulo
				                                       ,codigoMateria
				                                       ,null
				                                       ,codigoConteudo
				                                       ,numero
				                                       ,ano
				                                       ,dataAta
				                                       ,isPublicado
				                                       ,null
				                                       ,null); 
		
		return lista;
	}
	
	public List<ConteudoPublicacao> recuperar(Integer codigoCapitulo
			                                 ,List<Integer> codigosMateria
			                                 ,Integer codigoConteudo
			                                 ,Short anoMateria
			                                 ,Date dataCriacao
			                                 ,Boolean capituloJaComposto) throws DaoException {
		
		List<ConteudoPublicacao> lista = this.pesquisarMateria(codigoCapitulo
													   ,null
													   ,codigosMateria
													   ,codigoConteudo
													   ,null
													   ,anoMateria
													   ,dataCriacao
													   ,null
													   ,capituloJaComposto
													   ,null);
		
		return lista;
	}
	
	public List<ConteudoPublicacao> pesquisar(Integer codigoCapitulo
											 ,Integer codigoMateria
											 ,Integer codigoConteudo
											 ,Short ano
											 ,Integer numero
											 ,Date dataAta
											 ,Boolean isPublicado) throws DaoException {
		
		//No Codigo antigo isPublicado fazia referencia ao dataComposicaoParcial
		List<ConteudoPublicacao> lista = this.pesquisarMateria(codigoCapitulo
															  ,codigoMateria
															  ,null
															  ,codigoConteudo
															  ,numero
															  ,ano
															  ,dataAta
															  ,null
															  ,isPublicado
															  ,null);
		return lista;
	}
	
	/**
	 * 
	 * @param codigoCapitulo
	 * @param codigosMateria
	 * @param codigoConteudo
	 * @param ano
	 * @return
	 * @throws DaoException
	 */
	public List<ConteudoPublicacao> pesquisar(Integer codigoCapitulo
											 ,List<Integer> codigosMateria
											 ,Integer codigoConteudo
											 ,Short ano
											 ,Boolean capituloJaComposto) throws DaoException {
		
		List<ConteudoPublicacao> lista = this.pesquisarMateria(codigoCapitulo
															  ,null
															  ,codigosMateria
															  ,codigoConteudo
															  ,null
															  ,ano
															  ,null
															  ,null
															  ,capituloJaComposto
															  ,null);
		return lista;
	}
	
	public List<ConteudoPublicacao> pesquisarMateriasDJ(Long idPublicacao) throws DaoException {
		List<ConteudoPublicacao> resp = null;

		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cp FROM ConteudoPublicacao cp ");
			hql.append(" WHERE cp.publicacao.id = ? ");

			Query q = session.createQuery(hql.toString());
			q.setLong(0, idPublicacao);

			resp = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resp;
	}

	public List<ConteudoPublicacao> pesquisarMateriasPublicacaoAntigo(Long idPublicacao) throws DaoException {
		List<ConteudoPublicacao> resp = null;

		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cp FROM ConteudoPublicacao cp ");
			hql.append(" WHERE cp.publicacao.id = ? ");
			// MODIFICADO PARA TIRAR TEXTOS DE FECHAMENTO QUE NAO FAZER PARTE DO
			// DJ
			hql.append(" AND cp.codigoConteudo NOT IN (91,92) ");
			hql.append(" ORDER BY cp.estruturaPublicacao.ordemImpressao ASC, cp.ano ASC, cp.numero ASC ");

			Query q = session.createQuery(hql.toString());
			q.setLong(0, idPublicacao);

			resp = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resp;
	}

	public List<ConteudoPublicacao> pesquisarMateriasPublicacao() throws DaoException {
		List<ConteudoPublicacao> materias = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cp FROM ConteudoPublicacao cp ");
			hql.append(" WHERE cp.dataComposicaoDj IS NULL ");
			hql.append(" AND cp.dataComposicaoParcial IS NOT NULL ");
			// MODIFICADO PARA TIRAR TEXTOS DE FECHAMENTO QUE NAO FAZER PARTE DO
			// DJ
			hql.append(" AND cp.codigoConteudo NOT IN (91,92) ");
			hql.append(" ORDER BY cp.estruturaPublicacao.ordemImpressao ASC, cp.ano ASC, cp.numero ASC ");

			Query q = session.createQuery(hql.toString());

			materias = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return materias;
	}

	public List<ConteudoPublicacao> pesquisarMateriasPrevia(Date dataComposicao) throws DaoException {
		List<ConteudoPublicacao> materias = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cp FROM ConteudoPublicacao cp, EstruturaPublicacao ep ");
			hql.append(" WHERE cp.codigoCapitulo = ep.id.codigoCapitulo ");
			hql.append(" AND cp.codigoMateria = ep.id.codigoMateria ");
			if (dataComposicao == null) {
				hql.append(" AND cp.dataComposicaoDj IS NULL ");
				hql.append(" AND cp.dataComposicaoParcial IS NOT NULL ");
			} else {
				hql.append(" AND TO_CHAR(cp.dataComposicaoDj, 'dd/mm/yyyy') = :dataComposicao ");
			}

			hql.append(" AND ep.id.codigoConteudo = 0 ");
			hql.append(" ORDER BY ep.ordemImpressao ASC, cp.ano ASC, cp.numero ASC ");

			Query q = session.createQuery(hql.toString());

			if (dataComposicao != null) {
				q.setString("dataComposicao", new SimpleDateFormat("dd/MM/yyyy").format(dataComposicao));
			}

			materias = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return materias;
	}

	public Date recuperarDataPrevistaPublicacao(Integer codigoCapitulo, Integer codigoMateria, Integer numero, Short ano,
			Date dataCriacao) throws DaoException {
		Date dataPrevista = null;
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT p.dataPrevistaDj FROM ConteudoPublicacao cp, Publicacao p ");
			hql.append(" WHERE cp.dataComposicaoDj = p.dataComposicaoDj ");
			hql.append(" AND cp.ano = ? ");
			hql.append(" AND cp.numero = ? ");
			hql.append(" AND cp.dataCriacao = ? ");
			hql.append(" AND cp.codigoCapitulo = ? ");
			hql.append(" AND cp.codigoMateria = ? ");
			hql.append(" AND cp.codigoConteudo = ? ");

			Query q = session.createQuery(hql.toString());
			q.setShort(0, ano);
			q.setInteger(1, numero);
			q.setDate(2, dataCriacao);
			q.setInteger(3, codigoCapitulo);
			q.setInteger(4, codigoMateria);
			q.setInteger(5, 50);

			dataPrevista = (Date) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return dataPrevista;
	}

	public ConteudoPublicacao consultarDadosDaPublicacaoDoTexto(DadosDePublicacaoDynamicQuery consulta) throws DaoException {
		String hql = getHqlDeConteudoDeProcessoPublicado();
		QueryBuilder montadorDeQuery = new QueryBuilder(retrieveSession(), hql, consulta);
		Query query = montadorDeQuery.getQuery();
		return (ConteudoPublicacao) query.uniqueResult();
	}

	private String getHqlDeConteudoDeProcessoPublicado() {
		return "SELECT cp from ConteudoPublicacao cp, ProcessoPublicado pp";
	}

	public List<ConteudoPublicacao> pesquisar(ConteudoPublicacaoDynamicRestriction consultaDinamica) throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(ConteudoPublicacao.class,
				ConteudoPublicacaoDynamicRestriction.ALIAS_CONTEUDO_PUBLICACAO);
		consultaDinamica.addCriteriaRestrictions(criteria);
		return criteria.list();
	}
	
	public ConteudoPublicacao pesquisarUltimaMateriaOInoAcordao(ObjetoIncidente<?> oi)  throws DaoException{
		
		List<ConteudoPublicacao> cp = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT cp FROM ConteudoPublicacao cp, ProcessoPublicado pp ");
			hql.append(" WHERE cp.codigoCapitulo = pp.codigoCapitulo ");
			hql.append(" AND cp.codigoMateria = pp.codigoMateria ");
			hql.append(" AND cp.numero = pp.numeroMateria ");
			hql.append(" AND cp.ano = pp.anoMateria ");
			hql.append(" AND cp.codigoConteudo = 50 ");
			hql.append(" AND (cp.codigoCapitulo = 5 OR (cp.codigoCapitulo = 2 and cp.codigoMateria in (7,11)))");
			hql.append(" AND pp.objetoIncidente = :oi ");
			hql.append(" ORDER BY cp.dataCriacao desc");

			Query q = session.createQuery(hql.toString());
			q.setLong("oi", oi.getId());

			cp = q.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return (cp.size() >= 1? cp.get(0) : null);
		
	}

}
