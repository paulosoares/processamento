package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.TipoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoAssociacao;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.AssociacaoJurisdicionadoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AssociacaoJurisdicionadoDaoHibernate extends GenericHibernateDao<AssociacaoJurisdicionado, Long> 
		implements AssociacaoJurisdicionadoDao {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AssociacaoJurisdicionadoDaoHibernate(){
		super(AssociacaoJurisdicionado.class);
	}
	
	@Override
	public AssociacaoJurisdicionado recuperarPorGrupoMembro(PapelJurisdicionado grupo, PapelJurisdicionado membro, EnumTipoAssociacao tipoAssociacao) throws DaoException {
		try {
			Session session = retrieveSession();
			//EnumTipoAssociacao tipos = EnumTipoAssociacao.valueOf((long) 2);

			Criteria c = session.createCriteria(AssociacaoJurisdicionado.class)
		    	.add( Restrictions.eq("grupo.id", grupo.getId()) )
		    	.add( Restrictions.eq("membro.id", membro.getId()) )
		    	.add( Restrictions.eq("tipoAssociacao", tipoAssociacao) );
			
			AssociacaoJurisdicionado associacao = (AssociacaoJurisdicionado) c.uniqueResult();
			return associacao;

		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	
	@Override
	public List<AssociacaoJurisdicionado> pesquisarAssociacoesMembro(Jurisdicionado jurisdicionado) throws DaoException {
	try {

		Session session = retrieveSession();
		StringBuilder sql = new StringBuilder();
		sql.append("FROM AssociacaoJurisdicionado a ");
		sql.append(" WHERE a.tipoAssociacao in (2,3)");
		sql.append(" AND a.grupo.jurisdicionado.id = " + jurisdicionado.getId() );
		sql.append(" ORDER BY a.membro.id");
		Query sqlQuery = session.createQuery(sql.toString());

		List<AssociacaoJurisdicionado> associacoes = sqlQuery.list();
		
		return associacoes;
		//return associacoes;
	} catch ( HibernateException e ) {
		throw new HibernateException(e);
	} catch ( Exception e ) {
		throw new DaoException(e);
	}
		
	}
	
	
	@Override
	public List<AssociacaoJurisdicionado> pesquisarAssociacoes(Jurisdicionado jurisdicionado) throws DaoException {
	try {

		StringBuilder sql = new StringBuilder();
		List<Object[]> listaAssociacoes = null;
		List<AssociacaoJurisdicionado> listaAssociacao = new ArrayList<AssociacaoJurisdicionado>();

		/*
		 * Ordem dos campos
		 * 
		 0 associacao.SEQ_ASSOCIACAO_JURISDICIONADO, 
		 1 associacao.TIP_ASSOCIACAO, 
		 2 associacao.SEQ_JURISDICIONADO_GRUPO, 
		 3 associacao.SEQ_ASSOCIADO_MEMBRO
		 4 grupo.SEQ_PAPEL_JURISDICIONADO, 
		 5 grupo.SEQ_TIPO_JURISDICIONADO, 
		 6 grupo.SEQ_JURISDICIONADO, 
		 7 GRUPO.TIP_PRAZO, 
		 8 GRUPO.FLG_PADRAO, 
		 9 GRUPO.TIP_INTIMACAO
		 10 MEMBRO.SEQ_PAPEL_JURISDICIONADO, 
		 11 MEMBRO.SEQ_TIPO_JURISDICIONADO, 
		 12 MEMBRO.SEQ_JURISDICIONADO, 
		 13 MEMBRO.TIP_PRAZO, 
		 14 MEMBRO.FLG_PADRAO, 
		 15 MEMBRO.TIP_INTIMACAO
		 16 TIPOJURISGRUPO.SEQ_TIPO_JURISDICIONADO, 
		 17 TIPOJURISGRUPO.DSC_TIPO_JURISDICIONADO,
		 18 TIPOJURISMEMBRO.SEQ_TIPO_JURISDICIONADO, 
		 19 TIPOJURISMEMBRO.DSC_TIPO_JURISDICIONADO,
		 20 JURISGRUPO.NOM_JURISDICIONADO, 
		 21 JURISMEMBRO.NOM_JURISDICIONADO, 
		 22 IDENTIFICACAOJURISMEMBRO.DSC_IDENTIFICACAO, 
		 23 IDENTIFICACAOJURISGRUPO.DSC_IDENTIFICACAO, 
		 24 IDENTIFICACAOJURISMEMBRO.SEQ_IDENTIFICACAO_PESSOA, 
		 25 IDENTIFICACAOJURISGRUPO.SEQ_IDENTIFICACAO_PESSOA 

		 * 
		 * 
		 */
		
		sql.append("SELECT associacao.SEQ_ASSOCIACAO_JURISDICIONADO, associacao.TIP_ASSOCIACAO, associacao.SEQ_JURISDICIONADO_GRUPO, associacao.SEQ_ASSOCIADO_MEMBRO,");
		sql.append("   grupo.SEQ_PAPEL_JURISDICIONADO as GRUP_SEQ_PAPEL_JURISDICIONADO, grupo.SEQ_TIPO_JURISDICIONADO AS GRUP_SEQ_TIPO_JURISDICIONADO,");
		sql.append("   grupo.SEQ_JURISDICIONADO AS GRUP_SEQ_JURISDICIONADO, GRUPO.TIP_PRAZO AS GRUP_TIP_PRAZO, GRUPO.FLG_PADRAO AS GRUP_FLG_PADRAO, GRUPO.TIP_INTIMACAO AS GRUP_TIP_INTIMACAO,");
		sql.append("   MEMBRO.SEQ_PAPEL_JURISDICIONADO as MEMB_SEQ_PAPEL_JURISDICIONADO, MEMBRO.SEQ_TIPO_JURISDICIONADO as MEMB_SEQ_TIPO_JURISDICIONADO,");
		sql.append("   MEMBRO.SEQ_JURISDICIONADO as MEMB_SEQ_JURISDICIONADO, MEMBRO.TIP_PRAZO as MEMB_TIP_PRAZO, MEMBRO.FLG_PADRAO as MEMB_FLG_PADRAO, MEMBRO.TIP_INTIMACAO as MEMB_TIP_INTIMACAO,");
		sql.append("   TIPOJURISGRUPO.SEQ_TIPO_JURISDICIONADO as TPJG_SEQ_TIPO_JURISDICIONADO, TIPOJURISGRUPO.DSC_TIPO_JURISDICIONADO as TPJG_DSC_TIPO_JURISDICIONADO,");
		sql.append("   TIPOJURISMEMBRO.SEQ_TIPO_JURISDICIONADO as TPJM_SEQ_TIPO_JURISDICIONADO, TIPOJURISMEMBRO.DSC_TIPO_JURISDICIONADO as TPJM_DSC_TIPO_JURISDICIONADO,");
		sql.append("   JURISGRUPO.NOM_JURISDICIONADO AS JG_NOM_JURISDICIONADO, JURISMEMBRO.NOM_JURISDICIONADO AS JM_NOM_JURISDICIONADO, ");
		sql.append("   IDENTIFICACAOJURISMEMBRO.DSC_IDENTIFICACAO as IDJM_DSC_IDENTIFICACAO, IDENTIFICACAOJURISGRUPO.DSC_IDENTIFICACAO as IDJG_DSC_IDENTIFICACAO, ");
		sql.append("   IDENTIFICACAOJURISMEMBRO.SEQ_IDENTIFICACAO_PESSOA as IDJM_SEQ_IDENTIFICACAO_PESSOA, ");
		sql.append("   IDENTIFICACAOJURISGRUPO.SEQ_IDENTIFICACAO_PESSOA as IDJG_SEQ_IDENTIFICACAO_PESSOA");
	       
		sql.append(" FROM JUDICIARIO.ASSOCIACAO_JURISDICIONADO associacao, ");
		sql.append("  JUDICIARIO.PAPEL_JURISDICIONADO grupo, ");
		sql.append("  JUDICIARIO.PAPEL_JURISDICIONADO membro,");
		sql.append("  JUDICIARIO.JURISDICIONADO jurisGrupo,");
		sql.append("  JUDICIARIO.JURISDICIONADO jurisMembro,");
		sql.append("  JUDICIARIO.TIPO_JURISDICIONADO tipoJurisMembro,");
		sql.append("  JUDICIARIO.TIPO_JURISDICIONADO tipoJurisGrupo,");
		sql.append("  JUDICIARIO.IDENTIFICACAO_PESSOA identificacaoJurisMembro,");
		sql.append("  JUDICIARIO.IDENTIFICACAO_PESSOA identificacaoJurisGrupo ");

//		sql.append(" WHERE (associacao.TIP_ASSOCIACAO in (2 , 3))");
		sql.append(" WHERE 1=1 ");
		sql.append("  AND (grupo.SEQ_JURISDICIONADO = " + jurisdicionado.getId().toString() +  " OR membro.SEQ_JURISDICIONADO = " + jurisdicionado.getId().toString() + ") "); 
		sql.append("  and associacao.SEQ_JURISDICIONADO_GRUPO=grupo.SEQ_PAPEL_JURISDICIONADO ");
		sql.append("  and associacao.SEQ_ASSOCIADO_MEMBRO=membro.SEQ_PAPEL_JURISDICIONADO");
		
		sql.append("  and MEMBRO.SEQ_JURISDICIONADO = jurisMembro.SEQ_JURISDICIONADO");
		sql.append("  and GRUPO.SEQ_JURISDICIONADO = jurisGrupo.SEQ_JURISDICIONADO");
		
		sql.append("  AND GRUPO.SEQ_TIPO_JURISDICIONADO = TIPOJURISGRUPO.SEQ_TIPO_JURISDICIONADO");
		sql.append("  AND MEMBRO.SEQ_TIPO_JURISDICIONADO = TIPOJURISMEMBRO.SEQ_TIPO_JURISDICIONADO");
	    
		sql.append("  and tipoJurisMembro.SIG_TIPO_JURISDICIONADO in ('ADV','EST','PREPO')");
	    sql.append("  and tipoJurisGrupo.SIG_TIPO_JURISDICIONADO in ('ADV','PART')");
	    sql.append("  AND MEMBRO.SEQ_JURISDICIONADO = identificacaoJurisMembro.seq_jurisdicionado");
	    // os membros (estagiários, advogados e prepostos sempre terão CPF
	    // os grupos quando entidades governamentais podem não possuir identidade alguma, portanto left join 
   	    sql.append("  AND GRUPO.SEQ_JURISDICIONADO = identificacaoJurisGrupo.seq_jurisdicionado (+) ");
   	 	sql.append("  AND identificacaoJurisMembro.SEQ_TIPO_IDENTIFICACAO = 2");
 		sql.append("  AND identificacaoJurisGrupo.SEQ_TIPO_IDENTIFICACAO (+) = 2");
 		sql.append("  AND associacao.flg_ativo = 'S'");

		sql.append(" order by  grupo.SEQ_PAPEL_JURISDICIONADO");

        SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
        
        listaAssociacoes = sqlQuery.list();
        
		for (Object[] registro : listaAssociacoes) {
			AssociacaoJurisdicionado associacao = new AssociacaoJurisdicionado();
			// obtem o id da AssociacaoJurisdicionado
			associacao.setId( NumberUtils.createLong(registro[0].toString()) );
			// obtem o tipo da Associacao
			associacao.setTipoAssociacao(EnumTipoAssociacao.valueOf(NumberUtils.createLong( registro[1].toString() )));
			
			// obtem o grupo da Associacao
			PapelJurisdicionado papelGrupo = new PapelJurisdicionado();
			papelGrupo.setId(NumberUtils.createLong(registro[2].toString()) );
			papelGrupo.setPadrao (registro[8].toString().equals("S"));
			papelGrupo.setTipoPrazo((String) registro[7]);
			// obtem o jurisdicionado do papel grupo
			Jurisdicionado jurisGrupo = new Jurisdicionado();
			jurisGrupo.setId(NumberUtils.createLong(registro[6].toString()) );
			jurisGrupo.setNome((String) registro[20]);
			// obtem a identificação CPF
			IdentificacaoPessoa identificacaoGrupo = new IdentificacaoPessoa();
			if (registro[25] != null) {
				identificacaoGrupo.setId(NumberUtils.createLong( registro[25].toString() ));
				identificacaoGrupo.setDescricaoIdentificacao((String) registro[23]);
			}
			List<IdentificacaoPessoa> identificadoresGrupo = new ArrayList<IdentificacaoPessoa>();
			identificadoresGrupo.add(identificacaoGrupo);
			jurisGrupo.setIdentificadoresJurisdicionado(identificadoresGrupo);
			// obtem o tipo do grupo (Parte ou Advogado)
			TipoJurisdicionado tipoGrupo = new TipoJurisdicionado();
			tipoGrupo.setId(NumberUtils.createLong( registro[16].toString()) );
			tipoGrupo.setDescricaoTipoJurisdicionado((String) registro[17]); 
			
			papelGrupo.setTipoJurisdicionado(tipoGrupo);
			papelGrupo.setJurisdicionado(jurisGrupo);
			
			associacao.setGrupo(papelGrupo);
			
			// obtem o membro da Associacao
			PapelJurisdicionado papelMembro = new PapelJurisdicionado();
			papelMembro.setId( NumberUtils.createLong(registro[10].toString()) );
			papelMembro.setPadrao(registro[14].toString().equals("S")); 
			papelMembro.setTipoPrazo((String) registro[13]);
			// obtem o jurisdicionado membro
			Jurisdicionado jurisMembro = new Jurisdicionado();
			jurisMembro.setId( NumberUtils.createLong(registro[12].toString()) );
			jurisMembro.setNome((String) registro[21]);
			// obtem a identificação CPF
			IdentificacaoPessoa identificacaoMembro = new IdentificacaoPessoa();
			identificacaoMembro.setId(NumberUtils.createLong(registro[24].toString()));
			identificacaoMembro.setDescricaoIdentificacao((String) registro[22]);
			List<IdentificacaoPessoa> identificadoresMembro = new ArrayList<IdentificacaoPessoa>();
			identificadoresMembro.add(identificacaoMembro);
			jurisMembro.setIdentificadoresJurisdicionado(identificadoresMembro);
			// obtem o tipo do membro (Advogado, Estagiário ou Preposto)
			TipoJurisdicionado tipoMembro = new TipoJurisdicionado();
			tipoMembro.setId(NumberUtils.createLong(registro[18].toString()));
			tipoMembro.setDescricaoTipoJurisdicionado((String) registro[19]); 
			
			papelMembro.setTipoJurisdicionado(tipoMembro);
			papelMembro.setJurisdicionado(jurisMembro);
			
			associacao.setMembro(papelMembro);
			
			listaAssociacao.add(associacao);
		}
		
		return listaAssociacao;
		//return associacoes;
	} catch ( HibernateException e ) {
		throw new HibernateException(e);
	} catch ( Exception e ) {
		throw new DaoException(e);
	}
		
	}
}
