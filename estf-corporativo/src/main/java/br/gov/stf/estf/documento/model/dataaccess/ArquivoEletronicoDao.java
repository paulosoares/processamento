package br.gov.stf.estf.documento.model.dataaccess;

import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ArquivoEletronicoView;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ArquivoEletronicoDao extends GenericDao<ArquivoEletronico, Long> {
	public ArquivoEletronico recuperarArquivoEletronico(Long id) throws DaoException;

	ArquivoEletronico recuperarBloquearArquivoEletronico(Long id) throws DaoException;

	void desbloquearArquivoEletronico(Long id) throws DaoException;
	
	void desbloquearArquivoEletronicoAdmin(Long id) throws DaoException;

	public ArquivoEletronicoView recuperarArquivoEletronicoViewPeloId(Long id) throws DaoException;
	
}
