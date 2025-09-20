package br.gov.stf.estf.publicacao.model.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoPublicadoMinistroQuery;
import br.gov.stf.estf.entidade.processostf.ProcessoPublicadoMinistroResult;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.util.ProcessoPublicadoRelatorioQuery;
import br.gov.stf.estf.processostf.model.util.ConsultaObjetoIncidente;
import br.gov.stf.estf.publicacao.model.dataaccess.ProcessoPublicadoDao;
import br.gov.stf.estf.publicacao.model.service.ProcessoPublicadoService;
import br.gov.stf.estf.publicacao.model.util.IConsultaDePautaDeJulgamento;
import br.gov.stf.estf.publicacao.model.util.ProcessoEmPautaDynamicRestriction;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoResult;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoVO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("processoPublicadoService")
public class ProcessoPublicadoServiceImpl extends GenericServiceImpl<ProcessoPublicado, Long, ProcessoPublicadoDao> implements ProcessoPublicadoService {
	public ProcessoPublicadoServiceImpl(ProcessoPublicadoDao dao) {
		super(dao);
	}

	public Boolean isPublicado(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.isPublicado(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<ProcessoPublicado> pesquisarProcessosAta(Integer capitulo, Integer materia, Short ano, Integer numero, String siglaProcessual, Long numeroProcessual,
			Long tipoRecurso, Long tipoJulgamento, Date dataSessao, Long codigoSetor, Boolean recuperarOcultos) throws ServiceException {
		List<ProcessoPublicado> result = null;
		try {
			result = dao.pesquisarProcessosAta(capitulo, materia, ano, numero, siglaProcessual, numeroProcessual, tipoRecurso, tipoJulgamento, dataSessao, codigoSetor,
					recuperarOcultos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;
	}

	public List<ProcessoPublicado> pesquisarProcessosDj(Integer capitulo, Integer materia, Short ano, Integer numero, Boolean recuperarOcultos) throws ServiceException {
		List<ProcessoPublicado> result = null;
		try {
			result = dao.pesquisarProcessosDj(capitulo, materia, ano, numero, recuperarOcultos);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;
	}

	public List<ProcessoPublicado> pesquisarSessaoEspecial(Boolean recuperarOcultos, String... siglaClasseProcessual) throws ServiceException {
		List<ProcessoPublicado> processos = null;
		try {
			processos = dao.pesquisarSessaoEspecial(recuperarOcultos, siglaClasseProcessual);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return processos;
	}

	public List<ProcessoPublicadoMinistroResult> pesquisarProcessosAta(ProcessoPublicadoMinistroQuery query) throws ServiceException {
		List<ProcessoPublicadoMinistroResult> ppRes = null;
		ConsultaObjetoIncidente coi = new ConsultaObjetoIncidente(query.getSiglaProcessual(), query.getNumeroProcessual(), query.getTipoRecurso(), query.getTipoJulgamento());

		try {
			List<Object[]> resp = dao.pesquisarProcessosAta(query, coi, query.getRecuperarOcultos());
			if (resp != null && resp.size() > 0) {
				ppRes = new ArrayList<ProcessoPublicadoMinistroResult>();
				for (Object[] lObj : resp) {
					ppRes.add(new ProcessoPublicadoMinistroResult((ProcessoPublicado) lObj[0], (Ministro) lObj[1], (Texto) lObj[2]));
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return ppRes;
	}

	public List<ProcessoPublicadoMinistroResult> pesquisarProcessosAtaSemTexto(ProcessoPublicadoMinistroQuery query) throws ServiceException {
		List<ProcessoPublicadoMinistroResult> ppRes = null;
		ConsultaObjetoIncidente coi = new ConsultaObjetoIncidente(query.getSiglaProcessual(), query.getNumeroProcessual(), query.getTipoRecurso(), query.getTipoJulgamento());

		try {
			// SETA TODAS AS MATERIAS QUANDO O CAPITULO É DO ACORDAO
			if (query.getCapitulo().equals(EstruturaPublicacao.COD_CAPITULO_ACORDAOS)) {
				query.setMateria(new Integer[] { 1, 2, 3 });
			}

			List<Object[]> resp = dao.pesquisarProcessosAtaSemTexto(query, coi, query.getRecuperarOcultos());
			if (resp != null && resp.size() > 0) {
				ppRes = new ArrayList<ProcessoPublicadoMinistroResult>();
				for (Object[] lObj : resp) {
					ppRes.add(new ProcessoPublicadoMinistroResult((ProcessoPublicado) lObj[0], (Ministro) lObj[1], null));
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return ppRes;
	}

	public List<ProcessoPublicado> pesquisarProcessosRelatorio(ProcessoPublicadoRelatorioQuery query) throws ServiceException {
		List<ProcessoPublicado> processos = null;
		try {
			processos = dao.pesquisarProcessosRelatorio(query);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return processos;
	}

	public List<ProcessoPublicado> pesquisarProcessoEmPautaDeJulgamento(IConsultaDePautaDeJulgamento consulta) throws ServiceException {
		try {
			ProcessoEmPautaDynamicRestriction consultaDinamica = montaConsultaDinamicaDeProcessoEmPauta(consulta);
			return dao.pesquisarProcessosEmPautaDeJulgamento(consultaDinamica);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private ProcessoEmPautaDynamicRestriction montaConsultaDinamicaDeProcessoEmPauta(IConsultaDePautaDeJulgamento consulta) {
		ProcessoEmPautaDynamicRestriction consultaDinamica = new ProcessoEmPautaDynamicRestriction();
		consultaDinamica.setCodigoMateria(consulta.getCodigoDaMateria());
		consultaDinamica.setCodigosDosCapitulos(EstruturaPublicacao.COD_CAPITULO_PLENARIO, EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA,
				EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA);
		consultaDinamica.setSequencialObjetoIncidente(consulta.getSequencialObjetoIncidente());
		return consultaDinamica;
	}

	public ProcessoPublicado recuperar(Integer codigoCapitulo, Integer codigoMateria, Integer numeroMateria, Short anoMateria, String siglaProcesso, Long numeroProcesso,
			Long tipoRecurso, Long tipoJulgamento, Boolean recuperarOcultos) throws ServiceException {
		ProcessoPublicado processo = null;
		try {
			processo = dao.recuperar(codigoCapitulo, codigoMateria, numeroMateria, anoMateria, siglaProcesso, numeroProcesso, tipoRecurso, tipoJulgamento, recuperarOcultos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return processo;
	}

	@Override
	public ProcessoPublicado recuperar(Integer codigoCapitulo, Integer codigoMateria, Integer numeroMateria, Short anoMateria, ObjetoIncidente<?> objetoIncidente,
			ArquivoEletronico arquivoEletronicoTexto) throws ServiceException {
		ProcessoPublicado processo = null;
		try {
			processo = dao.recuperar(codigoCapitulo, codigoMateria, numeroMateria, anoMateria, objetoIncidente, arquivoEletronicoTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return processo;
	}

	public List<ProcessoPublicado> pesquisar(Integer codigoCapitulo, Integer codigoMateria, ObjetoIncidente<?> objetoIncidente, Boolean recuperarOcultos) throws ServiceException {
		List<ProcessoPublicado> processos = null;
		try {
			processos = dao.pesquisar(codigoCapitulo, codigoMateria, objetoIncidente, recuperarOcultos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return processos;
	}

	public List<ProcessoPublicadoResult> pesquisarProcessoPublicadoAcordao(Date dataPublicacao, Short numeroDJe, Short anoDJe) throws ServiceException {
		try {
			return dao.pesquisarProcessoPublicadoAcordao(dataPublicacao, numeroDJe, anoDJe);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ProcessoPublicadoVO> pesquisar(Date dataInicialDivulgacaoDJe, Date dataFinalDivulgacaoDJe) throws ServiceException {
		Validate.notNull(dataInicialDivulgacaoDJe, "A data inicial não pode ser nula!");
		Validate.notNull(dataFinalDivulgacaoDJe, "A data final não pode ser nula!");

		List<ProcessoPublicadoVO> processoPublicadoResults = Collections.emptyList();

		try {
			processoPublicadoResults = dao.pesquisar(dataInicialDivulgacaoDJe, dataFinalDivulgacaoDJe);
		} catch (DaoException e) {
			throw new ServiceException(MessageFormat.format("Erro ao pesquisar processos publicados no intervalo de data especificado: [{0}, {1}].", dataInicialDivulgacaoDJe,
					dataFinalDivulgacaoDJe), e);
		}

		return processoPublicadoResults;
	}

	@Override
	public List<ProcessoPublicado> pesquisarAcordaosPublicados(ObjetoIncidente<?> objetoIncidente)
			throws ServiceException {
		try {
			return dao.pesquisarProcessosPublicados(EstruturaPublicacao.COD_CAPITULO_ACORDAOS, objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Boolean existeObjetoIncidente(ProcessoPublicado processo) throws ServiceException{
		try {
			return dao.existeObjetoIncidente(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
