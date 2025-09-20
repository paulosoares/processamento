package br.gov.stf.estf.processostf.model.dataaccess;

// default package
// Generated 18/03/2008 11:02:23 by Hibernate Tools 3.1.0.beta5
import java.util.List;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * DAO interface for domain model class SituacaoMinistroProcesso.
 * @see .SituacaoMinistroProcesso
 * @author Hibernate Tools
 */

public interface SituacaoMinistroProcessoDao extends GenericDao<SituacaoMinistroProcesso, Long> {
	public List<SituacaoMinistroProcesso> pesquisar(String[] codOcorrencia, String siglaProcesso, Long numProcesso, Long codRecurso, String tipoJulgamento,
			Boolean orderByCodOcorrenciaDesc) throws DaoException;

	@Deprecated
	public Ministro recuperarMinistroRelatorAtual(String siglaClasse, Long numeroProcesso) throws DaoException;

	public Ministro recuperarMinistroRelatorAtual(Processo objetoIncidente) throws DaoException;

	public Ministro recuperarMinistroRelatorAtual(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public List<SituacaoMinistroProcesso> pesquisar(ObjetoIncidente<?> objetoIncidente, Ocorrencia ocorrencia) throws DaoException;

	public void remover(ObjetoIncidente<?> objetoIncidente) throws DaoException;
}
