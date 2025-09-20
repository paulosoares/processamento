package br.gov.stf.estf.publicacao.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoPublicadoMinistroQuery;
import br.gov.stf.estf.entidade.processostf.ProcessoPublicadoMinistroResult;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.util.ProcessoPublicadoRelatorioQuery;
import br.gov.stf.estf.publicacao.model.dataaccess.ProcessoPublicadoDao;
import br.gov.stf.estf.publicacao.model.util.IConsultaDePautaDeJulgamento;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoResult;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoVO;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProcessoPublicadoService extends GenericService<ProcessoPublicado, Long, ProcessoPublicadoDao> {
	public List<ProcessoPublicado> pesquisarProcessosAta(Integer capitulo, Integer materia, Short ano, Integer numero, String siglaProcessual, Long numeroProcessual,
			Long tipoRecurso, Long tipoJulgamento, Date dataSessao, Long codigoSetor, Boolean recuperarOcultos) throws ServiceException;

	public List<ProcessoPublicadoMinistroResult> pesquisarProcessosAta(ProcessoPublicadoMinistroQuery query) throws ServiceException;

	public List<ProcessoPublicadoMinistroResult> pesquisarProcessosAtaSemTexto(ProcessoPublicadoMinistroQuery query) throws ServiceException;

	public List<ProcessoPublicado> pesquisarProcessosDj(Integer capitulo, Integer materia, Short ano, Integer numero, Boolean recuperarOcultos) throws ServiceException;

	public List<ProcessoPublicado> pesquisarSessaoEspecial(Boolean recuperarOcultos, String... siglaClasseProcessual) throws ServiceException;

	List<ProcessoPublicado> pesquisarProcessoEmPautaDeJulgamento(IConsultaDePautaDeJulgamento consulta) throws ServiceException;

	public List<ProcessoPublicado> pesquisarProcessosRelatorio(ProcessoPublicadoRelatorioQuery query) throws ServiceException;

	public ProcessoPublicado recuperar(Integer codigoCapitulo, Integer codigoMateria, Integer numeroMateria, Short anoMateria, String siglaProcesso, Long numeroProcesso,
			Long tipoRecurso, Long tipoJulgamento, Boolean recuperarOcultos) throws ServiceException;

	public List<ProcessoPublicado> pesquisar(Integer codigoCapitulo, Integer codigoMateria, ObjetoIncidente<?> objetoIncidente, Boolean recuperarOcultos) throws ServiceException;

	public List<ProcessoPublicadoResult> pesquisarProcessoPublicadoAcordao(Date dataPublicacao, Short numeroDJe, Short anoDJe) throws ServiceException;

	/**
	 * Recupera todos os processos que foram divulgados no DJe no intervalo de data especificado.
	 * 
	 * @param dataInicialDivulgacaoDJe
	 * @param dataFinalDivulgacaoDJe
	 * @return
	 * @throws ServiceException
	 */
	public List<ProcessoPublicadoVO> pesquisar(Date dataInicialDivulgacaoDJe, Date dataFinalDivulgacaoDJe) throws ServiceException;

	ProcessoPublicado recuperar(Integer codigoCapitulo, Integer codigoMateria, Integer numeroMateria, Short anoMateria,
			ObjetoIncidente<?> objetoIncidente, ArquivoEletronico arquivoEletronicoTexto) throws ServiceException;

	List<ProcessoPublicado> pesquisarAcordaosPublicados(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public Boolean isPublicado(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public Boolean existeObjetoIncidente(ProcessoPublicado processo) throws ServiceException;

}
