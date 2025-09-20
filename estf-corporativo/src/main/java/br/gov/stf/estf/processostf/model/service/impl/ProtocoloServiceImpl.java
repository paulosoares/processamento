package br.gov.stf.estf.processostf.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.processostf.model.dataaccess.ProtocoloDao;
import br.gov.stf.estf.processostf.model.service.ProtocoloService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("protocoloService")
public class ProtocoloServiceImpl extends GenericServiceImpl<Protocolo, Long, ProtocoloDao> 
implements ProtocoloService {

	protected ProtocoloServiceImpl(ProtocoloDao dao) {
		super(dao);
	}

	public List<Protocolo> recuperar(String tipoMeioProcesso) throws ServiceException {
		List<Protocolo> lista = null;
		try {
			lista = dao.recuperar(tipoMeioProcesso);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return lista;
	}

	public Protocolo gerarProtocoloAnoNumero() throws ServiceException {
		Protocolo protocolo = null;
		try {
			protocolo = dao.gerarProtocoloAnoNumero();
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return protocolo;
	}

	public void persistirProtocolo(Protocolo protocolo) throws ServiceException {
		try {
			dao.persistirProtocolo(protocolo);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}

	public Protocolo recuperarProtocolo(Long numero, Short ano)
			throws ServiceException {
		Protocolo protocolo = null;
		try {
			protocolo = dao.recuperarProtocolo(numero, ano);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return protocolo;
	}

	public Protocolo recuperarProtocolo(String siglaClasseProcedencia,
			String numeroProcessoProcedencia, Integer codigoOrgao)
			throws ServiceException {
		Protocolo protocolo = null;
		try {
			protocolo = dao.recuperarProtocolo(siglaClasseProcedencia, numeroProcessoProcedencia, codigoOrgao);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return protocolo;
	}

	public List<Protocolo> recuperarProtocolo(Integer codigoOrigem,
			String siglaClasseProcedencia, String numeroProcessoProcedencia,
			String siglaClasse, Long numeroProcesso, Long numeroProtocolo,
			Short anoProtocolo, String tipoMeioFisico, Date dataInicial,
			Date dataFinal) throws ServiceException {
		List<Protocolo> lista = null;
		try {
			lista = dao.recuperarProtocolo(codigoOrigem,
					siglaClasseProcedencia, numeroProcessoProcedencia,
					siglaClasse, numeroProcesso, numeroProtocolo,
					anoProtocolo, tipoMeioFisico, dataInicial,
					dataFinal);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return lista;
	}

	public void removerObjetoSessaoHibernate(Object objeto,
			boolean executarFlush) {
		dao.removerObjetoSessaoHibernate(objeto, executarFlush);
	}
	
}
