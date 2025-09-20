package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.SituacaoMinistroProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SituacaoMinistroProcessoService extends GenericService<SituacaoMinistroProcesso, Long, SituacaoMinistroProcessoDao> {

	@Deprecated
	public List<SituacaoMinistroProcesso> pesquisar(String[] codOcorrencia, String siglaProcesso, Long numProcesso, Long codRecurso, String tipoJulgamento,
			Boolean orderByCodOcorrenciaDesc) throws ServiceException;

	/**
	 * Recupera o ministro relator atual para o processo informado.
	 * 
	 * @param siglaClasse
	 * @param numeroProcesso
	 * @return
	 * @throws ServiceException
	 */
	@Deprecated
	public Ministro recuperarMinistroRelatorAtual(String siglaClasse, Long numeroProcesso) throws ServiceException;

	/**
	 * Recupera o ministro relator atual para o Processo informado.
	 * 
	 * @param siglaClasse
	 * @param numeroProcesso
	 * @return
	 * @throws ServiceException
	 */
	public Ministro recuperarMinistroRelatorAtual(Processo processo) throws ServiceException;

	public Ministro recuperarMinistroRelatorAtual(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	public List<SituacaoMinistroProcesso> pesquisar(ObjetoIncidente<?> objetoIncidente, Ocorrencia relator) throws ServiceException;

	/**
	 * Remover os ministros relacionados com o objeto incidente.
	 */
	public void remover(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public void designarNovoRelatorAcordao(ObjetoIncidente<?> objetoIncidente, Ministro ministro, Ocorrencia ocorrencia) throws ServiceException;
	

}
