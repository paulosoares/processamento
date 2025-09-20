package br.gov.stf.estf.processostf.model.util;

import org.apache.commons.lang.BooleanUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import br.gov.stf.estf.entidade.processostf.TipoConfidencialidade;

public class ObjetoIncidenteComConfidencialidadeQuery {

	public static final String ALIAS_PROCESSO = "p";
	public static final String ALIAS_OBJETO_INCIDENTE = "oi";
	
	/**
	 * Restri��o em HQL/JPQL para impedir a busca de objetos incidentes ocultos.
	 * 
	 * O retorno dessa fun��o dever� ser adicionado � cl�usula WHERE do HQL/JPQL original.
	 * 
	 * IMPORTANTE: a cl�usula original dever� ter uma refer�ncia (alias) do objeto incidente
	 * que dever� ser passada como par�metro.
	 * 
	 * @param aliasObjetoIncidente O alias do objeto incidente na cl�usula original.
	 * @return A String com a cl�usula que restringe a recupera��o de processo ocultos.
	 * 
	 * @author Tiago da Costa Peixoto
	 */
	public static final String restringirProcessoOculto_HQL_JPQL(String aliasObjetoIncidente, Boolean recuperarOcultos) {

		if (BooleanUtils.isTrue(recuperarOcultos)) {
			return " (1 = 1) ";
		}

		StringBuffer hql = new StringBuffer();
		
		/* 
		 * Restri��o para n�o recuperar processos ocultos
		 * -- IMPORTANTE: Pela defini��o da Dra. Ana L�cia, Secret�ria Judici�ria,
		 * -- somente processos ocultos n�o dever�o ser inclu�dos. Os demais, 
		 * -- SJ (Segredo de Justi�a) e SG (Sigiloso) N�O devem ficar restritos.
		 * -- Definido em 08/04/2010 
		 */
		hql.append(" ( ");
		hql.append(aliasObjetoIncidente + ".tipoConfidencialidade IS NULL ");
		hql.append(" OR ");
		hql.append(aliasObjetoIncidente + ".tipoConfidencialidade NOT IN ('"+TipoConfidencialidade.OCULTO.getCodigo()+"','"+TipoConfidencialidade.SIGILOSO.getCodigo()+"')");
		hql.append(" ) ");		

		
		/*
		// Restri��o para n�o recuperar processos ocultos		
		hql.append(" (" + ALIAS_OBJETO_INCIDENTE + ".tipoConfidencialidade IS NULL ");
		hql.append(" OR ");
		hql.append(" " + ALIAS_OBJETO_INCIDENTE + ".tipoConfidencialidade NOT IN ('"+
				TipoConfidencialidade.OCULTO.getCodigo()+"', '"+
				TipoConfidencialidade.SIGILOSO.getCodigo()+"', '"+
				TipoConfidencialidade.SEGREDO_JUSTICA.getCodigo()+"')) ");			
		//hql.append("        p.tipoConfidencialidade NOT IN (:oculto, :sigiloso, :segredoJustica)) ");
		hql.append(" AND ad.id = :adid");		
		
		// Restri��o para n�o recuperar processos ocultos
		// -- IMPORTANTE: Pela defini��o da Dra. Ana L�cia, Secret�ria Judici�ria, 
		// -- somente processos ocultos n�o dever�o ser inclu�dos 
		query.setString("oculto", TipoConfidencialidade.OCULTO.getCodigo());
		//query.setString("sigiloso", TipoConfidencialidade.SIGILOSO.getCodigo());
		//query.setString("segredoJustica", TipoConfidencialidade.SEGREDO_JUSTICA.getCodigo());
		*/		
		
		return hql.toString();
	}
	
	/**
	 * Restri��o em Criteria (Hibernate/JPA) para impedir a busca de objetos incidentes ocultos.
	 * 
	 * IMPORTANTE: o criteria original dever� ter uma refer�ncia (alias) do objeto incidente
	 * que dever� ser passada como par�metro.	 
	 * 
	 * @param criteria O criteria original sobre o qual a restri��o ser� aplicada.
	 * @param aliasObjetoIncidente O alias do objeto incidente no criteria original.
	 * 
	 * @author Tiago da Costa Peixoto
	 */
	public static final void restringirProcessoOculto_Criteria(Criteria criteria, String aliasObjetoIncidente, Boolean recuperarOcultos) {
		criteria.add(restringirProcessoOculto_Criterion(aliasObjetoIncidente, recuperarOcultos));
	}
	
	/**
	 * Restri��o em Criteria (Hibernate/JPA) para impedir a busca de objetos incidentes ocultos.
	 *
	 * O retorno dessa fun��o dever� ser adicionado ao criteria original.
	 * 
	 * IMPORTANTE: o criteria original dever� ter uma refer�ncia (alias) do objeto incidente
	 * que dever� ser passada como par�metro.	  
	 * 
	 * @param aliasObjetoIncidente O alias do objeto incidente no criteria original.
	 * 
	 * @author Tiago da Costa Peixoto
	 */	
	public static Criterion restringirProcessoOculto_Criterion(String aliasObjetoIncidente, Boolean recuperarOcultos) {
		
		if (BooleanUtils.isTrue(recuperarOcultos)) {
			return Restrictions.sqlRestriction("1 = 1");
		}
		
		String oiTipoConfidencialidade = aliasObjetoIncidente+".tipoConfidencialidade";
		
		/* 
		 * Restri��o para n�o recuperar processos ocultos
		 * -- IMPORTANTE: Pela defini��o da Dra. Ana L�cia, Secret�ria Judici�ria,
		 * -- somente processos ocultos n�o dever�o ser inclu�dos. Os demais, 
		 * -- SJ (Segredo de Justi�a) e SG (Sigiloso) N�O devem ficar restritos.
		 * -- Definido em 08/04/2010 
		 */			
		Criterion criterion = 
			Restrictions.or(
				Restrictions.isNull(oiTipoConfidencialidade), 
				Restrictions.not(
						Restrictions.in(oiTipoConfidencialidade, new TipoConfidencialidade[] {TipoConfidencialidade.OCULTO, TipoConfidencialidade.SIGILOSO}) 
				) 
			);
		
		return criterion;
	}
}
