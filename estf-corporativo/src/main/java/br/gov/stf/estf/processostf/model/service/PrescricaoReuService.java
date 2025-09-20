package br.gov.stf.estf.processostf.model.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.PrescricaoReu;
import br.gov.stf.estf.entidade.processostf.ProcessoPrescricaoParte;
import br.gov.stf.estf.processostf.model.dataaccess.PrescricaoReuDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


public interface PrescricaoReuService extends GenericService<PrescricaoReu, Long, PrescricaoReuDao> {

	public List<ProcessoPrescricaoParte> pesquisarProcessosPrescricao (Long idObjetoIncidente, Date dtPrescricaoInicial, Date dtPrescricaoFinal, Long codigoDestino,
			Long idMinistro, String codigoPena, Boolean filtroEmTramitacao) throws ServiceException, ParseException;
}
