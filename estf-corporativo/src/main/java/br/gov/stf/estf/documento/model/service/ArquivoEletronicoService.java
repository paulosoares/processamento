package br.gov.stf.estf.documento.model.service;

import br.gov.stf.estf.documento.model.dataaccess.ArquivoEletronicoDao;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ArquivoEletronicoView;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ArquivoEletronicoService extends GenericService<ArquivoEletronico, Long, ArquivoEletronicoDao> {
	public ArquivoEletronico recuperarArquivoEletronico(Long id) throws ServiceException;

	/**
	 * Recupera um documento específico no formato binário e bloqueia o
	 * documento 
	 * - Verifica inicialmente se este documento está bloqueado. 
	 * - Se o documento estiver bloqueado, retorna o usuário que bloqueou. 
	 * Sempre retorna o documento, independente do bloqueio.

	 * @param id
	 * @throws ServiceException
	 */
	ArquivoEletronico recuperarBloquearArquivoEletronico(Long id) throws ServiceException;

	/**
	 *  Desbloqueia um documento.
	 * Se o documento estiver bloqueado e o usuário da conexão for o mesmo usuário que bloqueou o documento, então desbloqueia.
	 * Se o documento estiver bloqueado por outro usuário, lança exceção
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
	 * Desbloqueia um documento independetemente do usuário que o tenha bloqueado.
	 * @param id
	 * @throws ServiceException
	 */
	void desbloquearArquivoEletronicoAdmin(Long id) throws ServiceException;
	
}
