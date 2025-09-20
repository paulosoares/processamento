package br.gov.stf.estf.intimacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.intimacao.model.dataaccess.PecaProcessoEletronicoLocalDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PecaProcessoEletronicoLocalService extends
					GenericService<PecaProcessoEletronico, Long, PecaProcessoEletronicoLocalDao> {
	
	public List<PecaProcessoEletronico> pesquisarPecasPorProcessoIncidente(Long idProcessoIncidente,
			Boolean incluirCancelados) throws ServiceException;

	List<PecaProcessoEletronico> pesquisarPecasAndamento8507( Long idProcessoIncidente )throws ServiceException;
	
	List<TextoAndamentoProcesso> pesquisarPecasUtilizadasEmVista(Long idProcessoIncidente)throws ServiceException;

}
