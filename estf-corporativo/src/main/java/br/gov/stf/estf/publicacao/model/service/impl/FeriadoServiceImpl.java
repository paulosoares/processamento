package br.gov.stf.estf.publicacao.model.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.publicacao.Feriado;
import br.gov.stf.estf.publicacao.model.dataaccess.FeriadoDao;
import br.gov.stf.estf.publicacao.model.service.FeriadoService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@SuppressWarnings("unchecked")
@Service("feriadoService")
public class FeriadoServiceImpl extends GenericServiceImpl<Feriado, String, FeriadoDao> implements FeriadoService{

	public FeriadoServiceImpl(FeriadoDao dao) {
		super(dao);
	}

	@Override
	public List<Feriado> recuperar(String mesAno) throws ServiceException {
		List<Feriado> feriado = null;
		try {			
			feriado = dao.recuperar(mesAno);	
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return feriado;
	}
	
	@Override
	public List<Feriado> recuperar(String mesAno, String dia) throws ServiceException {
		List<Feriado> feriado = null;
		try {			
			feriado = dao.recuperar(mesAno, dia);	
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return feriado;
	}
	
	@Override
	public List<Calendar> recuperarProximosFeriados(Calendar hoje, int quantidadeMeses) throws ServiceException {
		if (hoje != null) {
			Calendar calendarHoje = (Calendar) hoje.clone();
			List<Feriado> feriados = new ArrayList<Feriado>();
			
			for (int i=0; i<quantidadeMeses; i++) {
				int mes = calendarHoje.get(Calendar.MONTH);
				int ano = calendarHoje.get(Calendar.YEAR);
				String mesAno = DataUtil.recuperaMesAno(ano, mes+1); //Adicionado 1, pois no Calendar Janeiro é 0 e Dezembro é 11;
				feriados.addAll(recuperar(mesAno));
				calendarHoje.add(Calendar.MONTH, 1);
			}
			
			return DataUtil.tipoFeriado2Calendar(feriados);
		} else {
			return null;
		}
	}
	
}
