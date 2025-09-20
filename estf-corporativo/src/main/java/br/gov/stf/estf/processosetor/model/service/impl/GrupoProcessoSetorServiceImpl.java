package br.gov.stf.estf.processosetor.model.service.impl;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processosetor.GrupoProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.processosetor.model.dataaccess.GrupoProcessoSetorDao;
import br.gov.stf.estf.processosetor.model.service.GrupoProcessoSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.model.util.TipoOperacaoServico;

@Service("grupoProcessoSetorService")
public class GrupoProcessoSetorServiceImpl extends GenericServiceImpl<GrupoProcessoSetor, Long, GrupoProcessoSetorDao>  
implements GrupoProcessoSetorService {

	public GrupoProcessoSetorServiceImpl(GrupoProcessoSetorDao dao)
	{
		super(dao);
	}
	
	public GrupoProcessoSetor recuperarGrupoProcessoSetor(Long id)
	throws ServiceException
	{
		if(id == null)
			throw new NullPointerException("Identificação do grupo de processo nula.");
		
		GrupoProcessoSetor grupoProcessoSetor = null;
		
		try
		{
			grupoProcessoSetor = dao.recuperarGrupoProcessoSetor(id);
		}
		catch(DaoException e)
		{
			throw new ServiceException(e);
		}
		
		return grupoProcessoSetor;
	}
	
	public List<GrupoProcessoSetor> pesquisarGrupoProcessoSetor(String nomeGrupo, Boolean ativo, 
			Long idSetor, Long idGrupo, String siglaClasseProcessual, Long numeroProcessual)
	throws ServiceException
	{
		List<GrupoProcessoSetor> gruposProcessoSetor = null;
		
		try
		{
			gruposProcessoSetor = dao.pesquisarGrupoProcessoSetor(nomeGrupo, ativo, idSetor, 
					idGrupo, siglaClasseProcessual, numeroProcessual);
		}
		catch(DaoException e)
		{
			throw new ServiceException(e);
		}
		
		return gruposProcessoSetor;
	}
	
	public Boolean persistirGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor)
	throws ServiceException
	{
		Boolean persistido = Boolean.FALSE;
		
		validarGrupoProcessoSetor(grupoProcessoSetor, TipoOperacaoServico.PERSISTIR);
		
		try
		{
			persistido = dao.persistirGrupoProcessoSetor(grupoProcessoSetor);
		}
		catch(DaoException e)
		{
			throw new ServiceException(e);
		}
		
		return persistido;
	}
	
	public Boolean excluirGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor)
	throws ServiceException
	{
		Boolean excluido = Boolean.FALSE;
		
		validarGrupoProcessoSetor(grupoProcessoSetor, TipoOperacaoServico.EXCLUIR);
		
		try
		{
			excluido = dao.excluirGrupoProcessoSetor(grupoProcessoSetor);
		}
		catch(DaoException e) {
			throw new ServiceException(e);
		}
		
		return excluido;
	}
	
	public Boolean excluirGruposProcessoSetor(List<GrupoProcessoSetor> gruposProcessoSetor)
	throws ServiceException
	{
		Boolean excluidos = Boolean.FALSE;
		
		if(gruposProcessoSetor == null || gruposProcessoSetor.size() <= 0)
			throw new ServiceException("A lista de grupos de processos/protocolos não possui elementos.");
		
		for(GrupoProcessoSetor grupo : gruposProcessoSetor)
		{
			try
			{
				excluidos = excluirGrupoProcessoSetor(grupo);
			}
			catch(Exception e)
			{
				throw new ServiceException("Grupo " + grupo.getNomeGrupo(), e);
			}
		}
		
		return excluidos;
	}
	
	public Boolean incluirProcessosSetorGrupo(GrupoProcessoSetor grupoProcessoSetor, 
			List<ProcessoSetor> processosSetorInclusao)
	throws ServiceException
	{
		Boolean incluido = Boolean.FALSE;
		
		validarGrupoProcessoSetor(grupoProcessoSetor, TipoOperacaoServico.ALTERAR);
		
		if(processosSetorInclusao == null || processosSetorInclusao.size() <= 0)
			throw new ServiceException("Lista de processos não contém elementos");
		
		for(ProcessoSetor procSet : processosSetorInclusao)
		{
			if(grupoProcessoSetor.getProcessosSetor().contains(procSet))
				throw new ServiceException("Grupo já possui um ou mais processos/protocolos informados.");
		}
		
		if(grupoProcessoSetor.getProcessosSetor() == null)
			grupoProcessoSetor.setProcessosSetor(new LinkedList<ProcessoSetor>());
		
		if(grupoProcessoSetor.getProcessosSetor().addAll(processosSetorInclusao));
			incluido = persistirGrupoProcessoSetor(grupoProcessoSetor);
		
		return incluido;
	}
	
	public Boolean excluirProcessosSetorGrupo(GrupoProcessoSetor grupoProcessoSetor, 
			List<ProcessoSetor> processosSetorExclusao)
	throws ServiceException
	{
		Boolean excluido = Boolean.FALSE;
		
		validarGrupoProcessoSetor(grupoProcessoSetor, TipoOperacaoServico.ALTERAR);
		
		if(processosSetorExclusao == null || processosSetorExclusao.size() <= 0)
			throw new ServiceException("Lista de processos/protocolos não contém elementos.");
		
		if(grupoProcessoSetor.getProcessosSetor() == null || 
				grupoProcessoSetor.getProcessosSetor().size() <= 0)
			throw new ServiceException("Grupo não possui nenhum de processo/protocolo.");
		
		if(grupoProcessoSetor.getProcessosSetor().removeAll(processosSetorExclusao));
			excluido = persistirGrupoProcessoSetor(grupoProcessoSetor);
		
		return excluido;
	}
	
	private void validarGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor, 
			TipoOperacaoServico tipoOperacao)
	throws ServiceException
	{
		if(grupoProcessoSetor == null){
			throw new NullPointerException("Grupo de Processo nulo.");
		}	
		
		if(grupoProcessoSetor.getId() == null && (tipoOperacao.equals(TipoOperacaoServico.EXCLUIR) ||
				tipoOperacao.equals(TipoOperacaoServico.ALTERAR))){
			throw new NullPointerException("Id do Grupo de Processo nula.");
		}	
		
		if((grupoProcessoSetor.getNomeGrupo() == null || 
				grupoProcessoSetor.getNomeGrupo().trim().equals("")) && 
				(tipoOperacao.equals(TipoOperacaoServico.PERSISTIR) || 
						tipoOperacao.equals(TipoOperacaoServico.ALTERAR))){
			throw new InvalidParameterException("Nome do grupo de processo/protocolo não informado.");
		}	
		
		if(grupoProcessoSetor.getAtivo() == null && 
				(tipoOperacao.equals(TipoOperacaoServico.PERSISTIR) || 
						tipoOperacao.equals(TipoOperacaoServico.ALTERAR))){
			throw new NullPointerException("Flag de atividade do grupo de processo/protocolo nulo.");
		}
		
		if(grupoProcessoSetor.getSetor() == null && 
				(tipoOperacao.equals(TipoOperacaoServico.PERSISTIR) || 
						tipoOperacao.equals(TipoOperacaoServico.ALTERAR))){
			throw new NullPointerException("Setor do grupo de processo/protocolo nulo");
		}
		
		if(grupoProcessoSetor.getObservacoes()!=null){
			grupoProcessoSetor.setObservacoes(grupoProcessoSetor.getObservacoes().toUpperCase());
		}
		
		grupoProcessoSetor.setNomeGrupo(grupoProcessoSetor.getNomeGrupo().toUpperCase());
		
		if(tipoOperacao.equals(TipoOperacaoServico.PERSISTIR) || 
				tipoOperacao.equals(TipoOperacaoServico.ALTERAR))
		{
			try
			{
				if(!dao.verificarUnicidadeGrupoProcessoSetor(
						grupoProcessoSetor.getId(), 
                        grupoProcessoSetor.getSetor().getId(),
                        grupoProcessoSetor.getNomeGrupo()))
					throw new ServiceException("Grupo '" + grupoProcessoSetor.getNomeGrupo() + 
							"' já cadastrado para o setor/gabinete '"+
                            grupoProcessoSetor.getSetor().getNome()+"'.");
			}
			catch(DaoException e) {
				throw new ServiceException(e);
			}
		}
	}

}
