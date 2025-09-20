package br.gov.stf.estf.julgamento.model.service;

import br.gov.stf.estf.entidade.julgamento.ManifestacaoLeitura;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.julgamento.model.dataaccess.ManifestacaoLeituraDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ManifestacaoLeituraService extends GenericService<ManifestacaoLeitura, Long, ManifestacaoLeituraDao> {

	ManifestacaoLeitura gravarLeituraSustentacaoOral(Long sustentacaoOralId, Ministro ministroAutenticado) throws ServiceException;

}
