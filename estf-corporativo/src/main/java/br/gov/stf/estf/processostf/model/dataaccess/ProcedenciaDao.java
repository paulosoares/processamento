package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProcedenciaDao extends GenericDao<Procedencia, Long> {
	public List pesquisarProcedencia() throws DaoException;

	/**
	 * Retorna a primeira prodecedência do objeto incidente com histórico do tipo 'Origem' ou 'Processo'.
	 */
	public String pesquisarProcedencia(Long objetoIncidenteId) throws DaoException;

	public List<Procedencia> pesquisarProcedenciasAtivas() throws DaoException;
	
	public List<Procedencia> pesquisarProcedenciasComOrigemAtiva(Orgao orgao) throws DaoException;
	
	public List<Procedencia> pesquisarProcedenciasDescricaoAtivas(String descricao) throws DaoException;
	
}
