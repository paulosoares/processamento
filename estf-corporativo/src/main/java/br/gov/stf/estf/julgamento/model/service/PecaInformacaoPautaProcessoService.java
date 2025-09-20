package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PecaInformacaoPautaProcesso;
import br.gov.stf.estf.julgamento.model.dataaccess.PecaInformacaoPautaProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PecaInformacaoPautaProcessoService extends GenericService<PecaInformacaoPautaProcesso, Long, PecaInformacaoPautaProcessoDao> {
	
	public List<PecaInformacaoPautaProcesso> recuperar(InformacaoPautaProcesso informacaoPautaProcesso, boolean somenteDocumentoExterno) throws ServiceException;
	public List<PecaInformacaoPautaProcesso> recuperar(InformacaoPautaProcesso informacaoPautaProcesso, PecaProcessoEletronico pecaProcessoEletronico) throws ServiceException;

}
