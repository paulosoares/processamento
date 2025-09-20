package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ibm.icu.util.Calendar;

import br.gov.stf.estf.expedicao.entidade.ContratoPostagem;
import br.gov.stf.estf.expedicao.model.dataaccess.ContratoPostagemDao;
import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;
import br.gov.stf.estf.expedicao.model.service.ContratoPostagemService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("contratoPostagemService")
public class ContratoPostagemServiceImpl extends GenericServiceImpl<ContratoPostagem, Long, ContratoPostagemDao> implements ContratoPostagemService {

	public static final long serialVersionUID = 1L;

    public ContratoPostagemServiceImpl(ContratoPostagemDao dao) {
		super(dao);
	}

    @Override
	public ContratoPostagem incluir(ContratoPostagem entidade) throws ServiceException {
    	encerrarContratoVigente(entidade);
    	super.incluir(entidade);
    	return entidade;
    }

	private void encerrarContratoVigente(ContratoPostagem contratoNovo) throws ServiceException {
        try {
			List<ContratoPostagem> contratosIniciadosPosteriormente = dao.listarIniciadosApos(contratoNovo.getDataVigenciaInicial());
			if (contratosIniciadosPosteriormente != null && !contratosIniciadosPosteriormente.isEmpty()) {
				throw new ServiceException("Existem contratos iniciados após a data informada.");
			}
			ContratoPostagem contratoPostagemVigente = buscarVigente(contratoNovo.getDataVigenciaInicial());
			if (contratoPostagemVigente != null && contratoPostagemVigente.getDataVigenciaFinal() != null) {
				throw new ServiceException("Existe um contrato vigente na data de início informada.");
			}
	    	if (contratoPostagemVigente != null) {
	    		Calendar dataFimVigenciaContratoAtual = Calendar.getInstance();
	    		dataFimVigenciaContratoAtual.setTime(contratoNovo.getDataVigenciaInicial());
	    		dataFimVigenciaContratoAtual.add(Calendar.DAY_OF_YEAR, -1);
	    		Date dataInicioVigenciaContratoVigente = contratoPostagemVigente.getDataVigenciaInicial();
	    		Date dataFimVigenciaContratoVigente = dataFimVigenciaContratoAtual.getTime();
	    		dataInicioVigenciaContratoVigente = Util.inicioDia(dataInicioVigenciaContratoVigente);
	    		dataFimVigenciaContratoVigente = Util.inicioDia(dataFimVigenciaContratoVigente);
	    		if (dataInicioVigenciaContratoVigente.before(dataFimVigenciaContratoVigente)) {
	        		contratoPostagemVigente.setDataVigenciaFinal(dataFimVigenciaContratoVigente);
	        		if (dataInicioVigenciaContratoVigente.compareTo(dataFimVigenciaContratoVigente) >= 0) {
	        			throw new ServiceException("Existe um contrato vigente na data de início informada.");
	        		}
	        		alterar(contratoPostagemVigente);
	    		} else {
	    			throw new ServiceException("Existe um contrato vigente na data de início informada.");
	    		}
	    	}
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}

    @Override
	public ContratoPostagem buscarVigente() throws ServiceException {
        try {
        	ContratoPostagem contratoPostagem = null;
        	List<ContratoPostagem> contratosSemDataVigencia = dao.listarContratosSemDataVigencia();
        	if(!contratosSemDataVigencia.isEmpty()) {
        		if (contratosSemDataVigencia.size() > 1) {
        			throw new ServiceException("Existe mais de um contrato de postagem cadastrado vigente (sem data de fim de vigência).");
        		} else {
        			contratoPostagem = contratosSemDataVigencia.get(0);
        		}
        	}
        	return contratoPostagem;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}

    @Override
	public ContratoPostagem buscarVigente(Date data) throws ServiceException {
        try {
        	return dao.buscarContratoVigenteEm(data);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
	public List<ContratoPostagem> listarContratosEncerrados() throws ServiceException {
        try {
        	return dao.listarContratosEncerrados();
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}

    @Override
	public List<ContratoPostagem> listar() throws ServiceException {
        try {
        	return dao.listar();
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}
}