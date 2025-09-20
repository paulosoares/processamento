package br.gov.stf.estf.processosetor.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processosetor.GrupoProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.processosetor.model.dataaccess.GrupoProcessoSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface GrupoProcessoSetorService extends GenericService<GrupoProcessoSetor, Long, GrupoProcessoSetorDao> {
	
	public GrupoProcessoSetor recuperarGrupoProcessoSetor(Long id)
	throws ServiceException;
	
	public List<GrupoProcessoSetor> pesquisarGrupoProcessoSetor(String nomeGrupo, Boolean ativo, 
			Long idSetor, Long idGrupo, String siglaClasseProcessual, Long numeroProcessual)
	throws ServiceException;
	
	public Boolean persistirGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor)
	throws ServiceException;
	
	public Boolean excluirGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor)
	throws ServiceException;
	
	public Boolean excluirGruposProcessoSetor(List<GrupoProcessoSetor> gruposProcessoSetor)
	throws ServiceException;
	
	public Boolean incluirProcessosSetorGrupo(GrupoProcessoSetor grupoProcessoSetor, 
			List<ProcessoSetor> processosSetorInclusao)
	throws ServiceException;
	
	public Boolean excluirProcessosSetorGrupo(GrupoProcessoSetor grupoProcessoSetor, 
			List<ProcessoSetor> processosSetorExclusao)
	throws ServiceException;
}
