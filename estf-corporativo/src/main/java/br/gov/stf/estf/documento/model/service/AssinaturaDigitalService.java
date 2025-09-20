package br.gov.stf.estf.documento.model.service;

import br.gov.stf.estf.documento.model.dataaccess.AssinaturaDigitalDao;
import br.gov.stf.estf.entidade.documento.AssinaturaDigital;
import br.gov.stf.framework.model.service.GenericService;

public interface AssinaturaDigitalService extends GenericService<AssinaturaDigital, Long, AssinaturaDigitalDao> {
	static final String SIGLA_SISTEMA = "ESTFDECISAO";
	static final String PATH_ASSINADOR = "/assinador.application";
}
