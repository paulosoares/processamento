package br.gov.stf.estf.localizacao.model.service;

// default package
// Generated 18/03/2008 11:04:46 by Hibernate Tools 3.1.0.beta5
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.localizacao.model.dataaccess.OrigemDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Service interface for domain model class Origem.
 * 
 * @see .Origem
 * @author Hibernate Tools
 */

public interface OrigemService extends GenericService<Origem, Long, OrigemDao> {

	public List<Origem> recuperarOrigemPorIdOuDescricao(String id) throws ServiceException;
	
	public List<Origem> recuperarOrigemPorId(Long id,Boolean ativo) throws ServiceException;
	
	public List<Origem> recuperarOrigemPorDescricao(String id, Boolean ativo) throws ServiceException;

	public List<Origem> recuperarApenasPgr() throws ServiceException;
	
	public List<Origem> recuperarApenasAgu() throws ServiceException;

	public List<Origem> pesquisarOrigensAtivas(Orgao orgao, Procedencia procedencia) throws ServiceException;
	
	public Boolean isOrigemIntegrada(Origem origem) throws ServiceException;
	
	public List<Long> recuperaUsuarioExternoESTF(Long origem) throws ServiceException;

	public Long recuperaUsuarioExternoOrigemESTF(Long seqUsuarioExterno)
			throws ServiceException;

	public List<Origem> recuperarTodasOrigens(Boolean ativo) throws ServiceException;
	
	public Boolean isOrigemAptaParaParaNotificacao(Long id) throws ServiceException;

	public List<Origem> recuperarApenasDpf() throws ServiceException;

	public Procedencia pesquisarProcedenciaPadrao(Origem origem) throws ServiceException;
	
}
