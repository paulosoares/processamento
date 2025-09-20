package br.gov.stf.estf.publicacao.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoPublicadoMinistroQuery;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.util.ProcessoPublicadoRelatorioQuery;
import br.gov.stf.estf.processostf.model.util.ConsultaObjetoIncidente;
import br.gov.stf.estf.publicacao.model.util.ProcessoEmPautaDynamicRestriction;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoResult;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoVO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProcessoPublicadoDao extends GenericDao<ProcessoPublicado, Long> {
	public List<ProcessoPublicado> pesquisarProcessosAta(Integer capitulo, Integer materia, Short ano, Integer numero, String siglaProcessual, Long numeroProcessual,
			Long tipoRecurso, Long tipoJulgamento, Date dataSessao, Long codigoSetor, Boolean recuperarOcultos) throws DaoException;

	public List<ProcessoPublicado> pesquisarProcessosDj(Integer capitulo, Integer materia, Short ano, Integer numero, Boolean recuperarOcultos) throws DaoException;

	public List<ProcessoPublicado> pesquisarSessaoEspecial(Boolean recuperarOcultos, String... siglaClasseProcessual) throws DaoException;

	public List<Object[]> pesquisarProcessosAta(ProcessoPublicadoMinistroQuery query, ConsultaObjetoIncidente consultaObjetoIncidente, Boolean recuperarOcultos)
			throws DaoException;

	public List<Object[]> pesquisarProcessosAtaSemTexto(ProcessoPublicadoMinistroQuery query, ConsultaObjetoIncidente coi, Boolean recuperarOcultos) throws DaoException;

	public List<ProcessoPublicado> pesquisarProcessosEmPautaDeJulgamento(ProcessoEmPautaDynamicRestriction consultaDinamica) throws DaoException;

	public List<ProcessoPublicado> pesquisarProcessosRelatorio(ProcessoPublicadoRelatorioQuery query) throws DaoException;

	public ProcessoPublicado recuperar(Integer codigoCapitulo, Integer codigoMateria, Integer numeroMateria, Short anoMateria, String siglaProcesso, Long numeroProcesso,
			Long tipoRecurso, Long tipoJulgamento, Boolean recuperarOcultos) throws DaoException;

	public ProcessoPublicado recuperar(Integer codigoCapitulo, Integer codigoMateria, Integer numeroMateria, Short anoMateria, ObjetoIncidente<?> objetoIncidente,
			ArquivoEletronico arquivoEletronicoTexto) throws DaoException;

	public List<ProcessoPublicado> pesquisar(Integer codigoCapitulo, Integer codigoMateria, ObjetoIncidente<?> objetoIncidente, Boolean recuperarOcultos) throws DaoException;

	public List<ProcessoPublicadoResult> pesquisarProcessoPublicadoAcordao(Date dataPublicacao, Short numeroDJe, Short anoDJe) throws DaoException;

	public List<ProcessoPublicadoVO> pesquisar(Date dataInicialPublicacaoDje, Date dataFinalPublicacaoDJe) throws DaoException;

	public List<ProcessoPublicado> pesquisarProcessosPublicados(Integer codCapituloAcordaos,
			ObjetoIncidente<?> objetoIncidente) throws DaoException;
	
	public Boolean isPublicado(ObjetoIncidente<?> objetoIncidente) throws DaoException;
	
	public Boolean existeObjetoIncidente(ProcessoPublicado processo) throws DaoException;
}
