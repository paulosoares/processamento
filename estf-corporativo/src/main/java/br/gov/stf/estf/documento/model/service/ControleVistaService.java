package br.gov.stf.estf.documento.model.service;
        
import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.ControleVistaDao;
import br.gov.stf.estf.entidade.documento.ControleVista;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ControleVistaService extends GenericService<ControleVista, Long, ControleVistaDao> {
	public List<ControleVista> recuperar(String siglaClasseProcessual, Long numeroProcesso,	Long codigoMinistro) throws ServiceException;
	public List<ControleVista> recuperar(String siglaClasseProcessual, Long numeroProcesso) throws ServiceException;
	public List<ControleVista> recuperar(Long objetoIncidente) throws ServiceException;
	public Boolean validarPedidoVista(String siglaClasseProcessual, Long numeroProcesso, Long codigoMinistro) throws ServiceException;
	public List<ControleVista> listarVistasVencidas() throws ServiceException;
}
