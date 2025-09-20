package br.gov.stf.estf.processostf.model.dataaccess;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoPlenario;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProcessoPlenarioDao extends GenericDao<ProcessoPlenario, Long> {

	public ProcessoPlenario recuperarPorObjetoIncidente(ObjetoIncidente<?> objetoIncidente);

}
