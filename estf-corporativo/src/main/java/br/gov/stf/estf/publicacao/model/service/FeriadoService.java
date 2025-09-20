package br.gov.stf.estf.publicacao.model.service;

import java.util.Calendar;
import java.util.List;

import br.gov.stf.estf.entidade.publicacao.Feriado;
import br.gov.stf.estf.publicacao.model.dataaccess.FeriadoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface FeriadoService extends GenericService<Feriado, String, FeriadoDao> {
	
	public List<Feriado> recuperar(String mesAno) throws ServiceException;
	
	public List<Feriado> recuperar(String mesAno, String dia) throws ServiceException;

	/**
	 * 
	 * @param hoje
	 * @param meses quantidade de meses futuros que o método deve considerar (inclui o mês atual)
	 * @return
	 * @throws ServiceException
	 */
	List<Calendar> recuperarProximosFeriados(Calendar hoje, int meses) throws ServiceException;
	
}
