package br.gov.stf.estf.ministro.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.ministro.model.dataaccess.MinistroDao;
import br.gov.stf.estf.usuario.model.util.TipoTurma;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface MinistroService extends GenericService<Ministro, Long, MinistroDao> {

	public Ministro recuperarMinistro(Setor setor) throws ServiceException;

	/**
	 * 
	 * @param ativo
	 *            - se o ministro encontra-se ativo
	 * @param incluirPresidente
	 *            - além dos ministros, incluir a presidência
	 * @param primeiraTurma
	 *            - limitar pela 1ª turma
	 * @param segundaTurma
	 *            - limitar pela 2ª turma
	 * @param sessaoPlenaria
	 *            - limitar pela sessão plenária
	 * @return
	 * @throws ServiceException
	 * @author Rodrigo.Lisboa
	 */
	public List<Ministro> pesquisarMinistros(Boolean ativo, Boolean incluirGabinetePresidencia, Boolean primeiraTurma,
			Boolean segundaTurma, Boolean sessaoPlenaria) throws ServiceException;

	public Ministro recuperarMinistroRelator(String siglaClasse, Long numeroProcessual, Long tipoRecurso, Long tipoJulgamento)
			throws ServiceException;
	
	public Ministro recuperarMinistroRelator(Processo processo)
		throws ServiceException;
	
	public Ministro recuperarMinistroRelator(ObjetoIncidente objetoIncidente)
		throws ServiceException;
	
	public Ministro recuperarRelatorAcordao(ObjetoIncidente objetoIncidente)
	throws ServiceException;

	public Ministro recuperarMinistro(String nome, Long id) throws ServiceException;

	public Ministro recuperarPresidente(Boolean incluirGabinetePresidencia, Boolean primeiraTurma, Boolean segundaTurma,
			Boolean sessaoPlenaria) throws ServiceException;

	public List<Ministro> pesquisarMinistrosAtivos() throws ServiceException;
	
	// o método mais abaixo pesquisarMinistro faz o mesmo
	public List<Ministro> pesquisarMinistros(String nomeMinistro, Boolean ativo) throws ServiceException;

	boolean isMinistroTemRelatoriaDaPresidencia(Ministro ministro, Processo processo) throws ServiceException;

	boolean isMinistroRelatorDoProcesso(Ministro ministroDoGabinete, Processo processo) throws ServiceException;

	// TODO já existe, mas com parâmetros diferentes
    public List<Ministro> pesquisarMinistro(Long codigoMinistro,String sigla,String nome,Long codigoSetor, 
    		TipoTurma tipoTurma, Boolean ativo,Boolean semMinistroPresidente) throws ServiceException;
    
    public Ministro retornaMinistroPresidenteAtual() throws ServiceException;
    
    Ministro recuperarMinistroRelatorIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

    Ministro recuperarMinistroRelatorIncidenteDataJulgamento(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
    
    Ministro recuperarMinistroRevisorIncidente(Long idObjetoIncidente) throws ServiceException;

    /**
     * Recuperar a lista de ministros que podem ser o Presidente Interino.
     */
    public List<Ministro> pesquisarMinistrosPresidenteInterino() throws ServiceException;

	/**
	 * Recupera o ministro presidente em uma data.
	 * @param data
	 * @return
	 * @throws ServiceException
	 */
	public Ministro recuperarMinistroPresidente(Date data) throws ServiceException;

	Ministro recuperarRedatorAcordao(ObjetoIncidente objetoIncidente) throws ServiceException;


}
