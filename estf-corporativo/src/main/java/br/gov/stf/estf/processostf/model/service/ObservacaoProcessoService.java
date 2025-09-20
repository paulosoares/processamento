package br.gov.stf.estf.processostf.model.service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObservacaoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.ObservacaoProcessoDao;
import br.gov.stf.framework.model.service.GenericService;

public interface ObservacaoProcessoService extends GenericService<ObservacaoProcesso, Long, ObservacaoProcessoDao>{

	ObservacaoProcesso pesquisarObservacaoProcesso(ObjetoIncidente<?> objetoIncidente, Setor setor);

	
}
