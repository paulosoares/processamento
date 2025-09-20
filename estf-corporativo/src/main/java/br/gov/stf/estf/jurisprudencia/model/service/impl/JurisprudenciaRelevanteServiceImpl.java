/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.JurisprudenciaRelevante;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.JurisprudenciaRelevanteDao;
import br.gov.stf.estf.jurisprudencia.model.service.JurisprudenciaRelevanteService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("jurisprudenciaRelevanteService")
public class JurisprudenciaRelevanteServiceImpl extends GenericServiceImpl<JurisprudenciaRelevante, Long, JurisprudenciaRelevanteDao> implements
	JurisprudenciaRelevanteService {
	
	protected JurisprudenciaRelevanteServiceImpl(JurisprudenciaRelevanteDao dao) {
		super(dao);
	}

}