package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.entidade.processostf.ClasseUnificada;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem.ProcessoImagemId;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoImagemDao;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoException;
import br.gov.stf.estf.processostf.model.service.ProcessoImagemService;
import br.gov.stf.estf.processostf.model.util.ProcessoImagemSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.SearchResult;

@Service("processoImagemService")
public class ProcessoImagemServiceImpl extends GenericServiceImpl<ProcessoImagem, Long, ProcessoImagemDao> implements ProcessoImagemService {

	private final ObjetoIncidenteService objetoIncidenteService;
	private final DocumentoEletronicoService documentoEletronicoService;

	public ProcessoImagemServiceImpl(ProcessoImagemDao dao, ObjetoIncidenteService objetoIncidenteService,
			DocumentoEletronicoService documentoEletronicoService) {
		super(dao);
		this.objetoIncidenteService = objetoIncidenteService;
		this.documentoEletronicoService = documentoEletronicoService;
	}

	public ProcessoImagem recuperarProcessoImagem(ObjetoIncidente<?> objetoIncidente) throws ServiceException, ProcessoException {
		ProcessoImagem proc = null;
		try {
			if (objetoIncidente == null || objetoIncidente.getId().longValue() == 0) {
				throw new ProcessoException("Número do objeto incidente deve ser informado");
			}
			proc = dao.recuperarProcessoImagem(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return proc;
	}
	
	public boolean salvarProcessoImagem (ProcessoImagem processoImagem) throws ServiceException {
		try {
			salvar(processoImagem);
			return true;
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	public SearchResult<ProcessoImagem> pesquisarProcessoImagem(ProcessoImagemSearchData processoImagemSearchData) throws ServiceException {
		try {
			return dao.pesquisarProcessoImagem(processoImagemSearchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ClasseUnificada recuperarClasseUnificada(ProcessoImagem processoImagem) throws ServiceException {
		try {
			return dao.recuperarClasseUnificada(processoImagem);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar classe processual unificada do processo.", e);
		}
	}
	
	@Override
	public Long recuperarQuantidadePaginasProcessoImagem(ProcessoImagem processoImagem) throws ServiceException {
		byte[] arquivo = processoImagem.getSeqDocumento() != null ? processoImagem.getSeqDocumento().getArquivo() : processoImagem.getSeqInteiroTeor().getBinArquivo();
		
		return documentoEletronicoService.recuperarQuantidadePaginasPdf(arquivo);
	}

	@Override
	public List<ProcessoImagem> pesquisarProcessoImagemPorClasseNumero(String siglaClasse, Long numeroProcesso)	throws ServiceException {
		 try {
			return dao.pesquisarProcessoImagemPorClasseNumero(siglaClasse, numeroProcesso);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar o processo imagem.", e);
		}
	}
	
	@Override
	public ProcessoImagem recuperarProcessoImagemLiberadoPorId(ProcessoImagemId id) throws ServiceException{
		 try {
			return dao.recuperarProcessoImagemLiberadoPorId(id);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar o processo imagem.", e);
		}		
	}	
}
