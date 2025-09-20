package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.processostf.model.dataaccess.ProtocoloPublicadoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProtocoloPublicadoService extends GenericService<ProtocoloPublicado, Long, ProtocoloPublicadoDao> {
	public List<ProtocoloPublicado> pesquisar (ConteudoPublicacao conteudoPublicacao, Boolean recuperarOcultos) throws ServiceException;

}
