package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.processostf.model.dataaccess.ProcedenciaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProcedenciaService extends GenericService<Procedencia, Long, ProcedenciaDao> {
	public List pesquisarProcedencia() throws ServiceException;

	/**
	 * Retorna a primeira prodecedência do objeto incidente com histórico do tipo 'Origem' ou 'Processo'.
	 */
	public String pesquisarProcedencia(Long objetoIncidenteId) throws ServiceException;

	public List<Procedencia> pesquisarProcedenciasAtivas() throws ServiceException;
	
	public List<Procedencia> pesquisarProcedenciasComOrigemAtiva(Orgao orgao) throws ServiceException;
	
	public List<Procedencia> pesquisarProcedenciasDescricaoAtivas(String descricao) throws ServiceException;
	

}
