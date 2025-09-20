package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.PecaProcessoEletronicoComunicacaoDao;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PecaProcessoEletronicoComunicacaoService extends GenericService<PecaProcessoEletronicoComunicacao, Long, PecaProcessoEletronicoComunicacaoDao>{

	public void vincularPecasAoDocumento(List<ArquivoProcessoEletronico> listaArquivoProcessoEletronico, Comunicacao comunicacao) throws ServiceException;
	
	public void alterarPecasVinculadasComunicacao(List<ArquivoProcessoEletronico> listaArquivoProcessoEletronico, Comunicacao comunicacao) throws ServiceException;

	public List<PecaProcessoEletronicoComunicacao> pesquisarPecasPelaComunicacao(Comunicacao comunicacao) throws ServiceException;
	
}
