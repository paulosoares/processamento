package br.gov.stf.estf.documento.model.service;

import br.gov.stf.estf.documento.model.dataaccess.ArquivoEletronicoDao;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ArquivoEletronicoView;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ArquivoEletronicoService extends GenericService<ArquivoEletronico, Long, ArquivoEletronicoDao> {
	public ArquivoEletronico recuperarArquivoEletronico(Long id) throws ServiceException;

	/**
	 * Recupera um documento espec�fico no formato bin�rio e bloqueia o
	 * documento 
	 * - Verifica inicialmente se este documento est� bloqueado. 
	 * - Se o documento estiver bloqueado, retorna o usu�rio que bloqueou. 
	 * Sempre retorna o documento, independente do bloqueio.

	 * @param id
	 * @throws ServiceException
	 */
	ArquivoEletronico recuperarBloquearArquivoEletronico(Long id) throws ServiceException;

	/**
	 *  Desbloqueia um documento.
	 * Se o documento estiver bloqueado e o usu�rio da conex�o for o mesmo usu�rio que bloqueou o documento, ent�o desbloqueia.
	 * Se o documento estiver bloqueado por outro usu�rio, lan�a exce��o
	 * @param id
	 * @throws ServiceException
	 */
	void desbloquearArquivoEletronico(Long id) throws ServiceException;

	/**
	 * Recupera um ArquivoEletronicoView pelo Id.
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	ArquivoEletronicoView recuperarArquivoEletronicoViewPeloId(Long id) throws ServiceException;
	
	/**
	 * Desbloqueia um documento independetemente do usu�rio que o tenha bloqueado.
	 * @param id
	 * @throws ServiceException
	 */
	void desbloquearArquivoEletronicoAdmin(Long id) throws ServiceException;
	
}
