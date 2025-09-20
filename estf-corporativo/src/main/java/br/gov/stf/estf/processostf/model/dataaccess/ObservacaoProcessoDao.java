package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObservacaoProcesso;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ObservacaoProcessoDao extends GenericDao<ObservacaoProcesso, Long>{

	ObservacaoProcesso pesquisarObservacaoProcesso(ObjetoIncidente<?> objetoIncidente,
			Setor setor);

}
