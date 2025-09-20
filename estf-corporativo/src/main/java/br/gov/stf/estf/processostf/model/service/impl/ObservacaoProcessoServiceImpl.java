package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObservacaoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.ObservacaoProcessoDao;
import br.gov.stf.estf.processostf.model.service.ObservacaoProcessoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("observacaoProcessoService")
public class ObservacaoProcessoServiceImpl extends GenericServiceImpl<ObservacaoProcesso, Long, ObservacaoProcessoDao> implements ObservacaoProcessoService{

	public ObservacaoProcessoServiceImpl(ObservacaoProcessoDao dao) {
		super(dao);
	}

	public ObservacaoProcesso pesquisarObservacaoProcesso(ObjetoIncidente<?> objetoIncidente, Setor setor) {
		return dao.pesquisarObservacaoProcesso(objetoIncidente, setor);
	}


}
