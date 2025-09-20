package br.gov.stf.estf.processostf.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoDao;
import br.gov.stf.estf.processostf.model.dataaccess.TransacaoIntegracaoDao;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.estf.processostf.model.service.ProcessoException;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.util.IdentificacaoProcessoResolver;
import br.gov.stf.estf.processostf.model.util.ProcessoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.SearchResult;

@Service("processoService")
public class ProcessoServiceImpl extends GenericServiceImpl<Processo, Long, ProcessoDao> implements ProcessoService {

	private final GuiaService guiaService;
	private final DeslocaProcessoService deslocaProcessoService;
	private final SetorService setorService;
	private final ProcessoDao processoDao;
	private final TextoAndamentoProcessoService textoAndamentoProcessoService;
	private final MinistroService ministroService;
	private final TransacaoIntegracaoDao transacaoIntegracaoDao;

	private IdentificacaoProcessoResolver idResolver;

	public ProcessoServiceImpl(ProcessoDao dao, GuiaService guiaService, DeslocaProcessoService deslocaProcessoService,
			SetorService setorService, ProcessoDao processoDao, TextoAndamentoProcessoService textoAndamentoProcessoService,
			MinistroService ministroService, TransacaoIntegracaoDao transacaoIntegracaoDao) {
		super(dao);
		this.guiaService = guiaService;
		this.deslocaProcessoService = deslocaProcessoService;
		this.setorService = setorService;
		this.processoDao = processoDao;
		this.textoAndamentoProcessoService = textoAndamentoProcessoService;
		this.ministroService = ministroService;
		this.idResolver = new IdentificacaoProcessoResolver();
		this.transacaoIntegracaoDao = transacaoIntegracaoDao;
	}

	public Processo recuperarProcesso(String classeProcessual, Long numeroProcesso) throws ServiceException, ProcessoException {
		Processo proc = null;
		try {
			if (classeProcessual == null || classeProcessual.trim().length() == 0) {
				throw new ProcessoException("A classe processual deve ser informada");
			} else if (numeroProcesso == null) {
				throw new ProcessoException("O número do processo deve ser informado");
			}
			proc = dao.recuperarProcesso(classeProcessual, numeroProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return proc;
	}

	public void alterarDeslocamentoProcessoEletronico(Processo processo, Setor setor) throws ServiceException {

		// DEFINE O SETOR PARA DESLOCAMENTO DO PROCESSO
		Short codigoTurmaMinistro = ministroService.recuperarMinistroRelator(processo).getCodigoTurma();
		Long codigoSetorDeslocamento = null;

		if (codigoTurmaMinistro == Ministro.CODIGO_PRIMEIRA_TURMA) {
			codigoSetorDeslocamento = Setor.CODIGO_SETOR_SECAO_RE_PRIMEIRA_TURMA;
		} else if (codigoTurmaMinistro == Ministro.CODIGO_SEGUNDA_TURMA) {
			codigoSetorDeslocamento = Setor.CODIGO_SETOR_SECAO_RE_SEGUNDA_TURMA;
		} else {
			return;
		}

		// Long orgaoDestino = processo.getOrgaoDestino();
		/*
		 * Comentado por não haver sentido (código baseado no textual) if (
		 * orgaoDestino==null || orgaoDestino != setor.getId() ) { return; }
		 */

		// INCREMENTA A GUIA DO PROCESSO
		setor.setNumeroProximaGuia(setor.getNumeroProximaGuia() + 1);
		setorService.salvar(setor);

		// INSERE UMA NOVA GUIA
		Guia guia = new Guia();
		GuiaId idGuia = new GuiaId();
		idGuia.setCodigoOrgaoOrigem(setor.getId());
		idGuia.setAnoGuia(setor.getNumeroAnoGuia());
		idGuia.setNumeroGuia(setor.getNumeroProximaGuia() - 1);
		guia.setId(idGuia);
		guia.setCodigoOrgaoDestino(codigoSetorDeslocamento);
		guia.setDataRemessa(new Date());
		guia.setQuantidadeProcesso(1);
		guia.setTipoOrgaoDestino(2);

		guiaService.incluir(guia);

		// INSERE DESLOCAMENTO PROCESSO
		DeslocaProcesso deslocaProcesso = new DeslocaProcesso();
		DeslocaProcessoId idDeslocaProcesso = new DeslocaProcessoId();
		idDeslocaProcesso.setAnoGuia(setor.getNumeroAnoGuia());
		idDeslocaProcesso.setCodigoOrgaoOrigem(setor.getId());
		idDeslocaProcesso.setNumeroGuia(setor.getNumeroProximaGuia() - 1);
		idDeslocaProcesso.setProcesso(processo);
		deslocaProcesso.setId(idDeslocaProcesso);
		deslocaProcesso.setCodigoOrgaoDestino(codigoSetorDeslocamento);
		deslocaProcesso.setDataRecebimento(new Date());
		deslocaProcesso.setNumeroSequencia(1);
		deslocaProcesso.setQuantidadeApensos(null);
		deslocaProcesso.setQuantidadeJuntadaLinha(null);
		deslocaProcesso.setQuantidadeVolumes(null);
		deslocaProcesso.setQuantidadeJuntadaLinha(null);
		deslocaProcesso.setQuantidadeVolumes(null);
		deslocaProcesso.setTipoDeslocamento("EL");

		deslocaProcessoService.incluir(deslocaProcesso);

	}

	public void deslocarProcessoEletronico(Processo processoDoTexto, Setor setorDeOrigem, Setor setorDeDestino)
			throws ServiceException {

		Guia guiaDeDeslocamento = montaGuiaDeDeslocamento(setorDeOrigem, setorDeDestino);
		guiaService.incluir(guiaDeDeslocamento);
		DeslocaProcesso deslocaProcesso = montaDeslocamentoDoProcesso(processoDoTexto, guiaDeDeslocamento);
		deslocaProcessoService.incluir(deslocaProcesso);
		setorService.incrementarGuiaDoSetor(setorDeOrigem);
	}

	private DeslocaProcesso montaDeslocamentoDoProcesso(Processo processoDoTexto, Guia guiaDeDeslocamento) {
		DeslocaProcesso deslocaProcesso = new DeslocaProcesso();
		DeslocaProcessoId idDeslocaProcesso = new DeslocaProcessoId();
		idDeslocaProcesso.setNumeroGuia(guiaDeDeslocamento.getId().getNumeroGuia());
		idDeslocaProcesso.setAnoGuia(guiaDeDeslocamento.getId().getAnoGuia());
		idDeslocaProcesso.setCodigoOrgaoOrigem(guiaDeDeslocamento.getId().getCodigoOrgaoOrigem());
		idDeslocaProcesso.setProcesso(processoDoTexto);
		deslocaProcesso.setId(idDeslocaProcesso);
		deslocaProcesso.setCodigoOrgaoDestino(guiaDeDeslocamento.getCodigoOrgaoDestino());
		deslocaProcesso.setDataRecebimento(new Date());
		deslocaProcesso.setNumeroSequencia(1);
		deslocaProcesso.setQuantidadeApensos(null);
		deslocaProcesso.setQuantidadeJuntadaLinha(null);
		deslocaProcesso.setQuantidadeVolumes(null);
		deslocaProcesso.setTipoDeslocamento(DeslocaProcesso.TIPO_DESLOCAMENTO_EL);

		return deslocaProcesso;
	}

	private Guia montaGuiaDeDeslocamento(Setor setorDeOrigem, Setor setorDeDestino) {
		Guia guiaDeDeslocamento = new Guia();
		GuiaId idDaGuia = new GuiaId();
		idDaGuia.setCodigoOrgaoOrigem(setorDeOrigem.getId());
		idDaGuia.setNumeroGuia(setorDeOrigem.getNumeroProximaGuia());
		idDaGuia.setAnoGuia(setorDeOrigem.getNumeroAnoGuia());
		guiaDeDeslocamento.setId(idDaGuia);
		guiaDeDeslocamento.setDataRemessa(new Date());
		guiaDeDeslocamento.setTipoOrgaoDestino(Guia.CODIGO_TIPO_ORGAO_PARA_GUIA);
		guiaDeDeslocamento.setCodigoOrgaoDestino(setorDeDestino.getId());
		guiaDeDeslocamento.setTipoOrgaoOrigem(Guia.CODIGO_TIPO_ORGAO_PARA_GUIA);
		guiaDeDeslocamento.setQuantidadeProcesso(1);
		return guiaDeDeslocamento;
	}

	public Processo recuperarProcessoSTF(String sigla, Long numero) throws ServiceException {

		Processo processoSTF = null;

		try {

			processoSTF = processoDao.recuperarProcessoSTF(sigla, numero);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return processoSTF;
	}

	public Processo recuperarProcessoSTF(Long numeroProcotolo, Short anoProtocolo) throws ServiceException {
		Processo processoSTF = null;

		try {

			processoSTF = processoDao.recuperarProcessoSTF(numeroProcotolo, anoProtocolo);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return processoSTF;

	}

	public Processo recuperarProcessoSTF(Long codigoOrigem, String siglaClasseProcedencia, String numeroProcessoProcedencia)
			throws ServiceException {
		Processo processoSTF = null;

		try {

			processoSTF = processoDao.recuperarProcessoSTF(codigoOrigem, siglaClasseProcedencia, numeroProcessoProcedencia);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return processoSTF;
	}

	public SituacaoMinistroProcesso recuperarDataDistribuicaoSTF(Long seqObjetoIncidente, String sigla, Long numeroProcesso,
			Long codigoMinistroRelator) throws ServiceException {

		SituacaoMinistroProcesso dataDistribuicaoSTF = null;

		try {
			dataDistribuicaoSTF = processoDao.recuperarDataDistribuicaoSTF(seqObjetoIncidente, sigla, numeroProcesso,
					codigoMinistroRelator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return dataDistribuicaoSTF;
	}

	public Texto persistirTexto(AndamentoProcesso andamento, DocumentoTexto documentoTexto) throws ServiceException {

		Texto novoTexto = new Texto();
		novoTexto.setObjetoIncidente(andamento.getObjetoIncidente());

		novoTexto.setPublico(Boolean.TRUE);
		novoTexto.setTipoTexto(TipoTexto.DEBATE_4);// Recibo //TODO: MIGRACAO:
		// porque 306? 306 parecer
		// ser DEBATE IV. Não tem
		// nenhum RECIBIO na tabela
		// de tipos de textos

		novoTexto.adicionarDocumentoTexto(documentoTexto);

		textoAndamentoProcessoService.persistirTexto(novoTexto);

		return novoTexto;
	}

	public List<SituacaoMinistroProcesso> pesquisarSituacaoMinistroProcesso(Processo processo) throws ServiceException {
		List<SituacaoMinistroProcesso> listaSituacaoMinistroProcesso = null;

		try {
			listaSituacaoMinistroProcesso = processoDao.pesquisarSituacaoMinistroProcesso(processo);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar os relatores do processo", e);
		}

		return listaSituacaoMinistroProcesso;
	}

	@Override
	public Ministro pesquisarRelatorAtual(ObjetoIncidente objetoIncidente) throws ServiceException {
		try {
			return processoDao.pesquisarRelatorAtual(objetoIncidente); 	
		} catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar os relatores do processo", e);
		}
	}
	
	
	public SearchResult<Processo> pesquisarProcesso(ProcessoSearchData processoSearchData) throws ServiceException {
		try {
			return dao.pesquisarProcesso(processoSearchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Processo recuperar(Peticao peticao) throws ServiceException {
		try {
			return dao.recuperar(peticao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void alterarBaixaProcesso(Processo processo) throws ServiceException {
		try {
			dao.alterarBaixaProcesso(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}
	
	public void alterarBaixaProcesso(Processo processo, Boolean flag) throws ServiceException {
		try {
			dao.alterarBaixaProcesso(processo, flag);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public boolean isProcessoFindo(Processo processo) throws ServiceException {
		try {
			return dao.isProcessoFindo(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Processo> pesquisarProcesso(String identificacao) throws ServiceException {
		return pesquisarProcessos(identificacao, null);
	}

	@Override
	public List<Processo> pesquisarProcessos(String identificacao, Boolean isFisico) throws ServiceException {

		try {
			if (idResolver.isValid(identificacao)) {
				if (idResolver.getSigla(identificacao).length() > 0) {
					ArrayList<Processo> processos = new ArrayList<Processo>();
//					processos.add(dao.recuperarProcesso(idResolver.getSigla(identificacao), idResolver.getNumero(identificacao)));
					if (dao.recuperarProcesso(idResolver.getSigla(identificacao), idResolver.getNumero(identificacao)) == null) {
						return processos;
					} else {
						processos.add(dao.recuperarProcesso(idResolver.getSigla(identificacao), idResolver.getNumero(identificacao)));
					}
					return processos;
				} else {
					return dao.pesquisarProcessos(idResolver.getNumero(identificacao), isFisico);
				}
			} else{
				return new ArrayList<Processo>();
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean isProcessoDistribuido(Processo processo) throws ServiceException {
		try {
			return dao.isProcessoDistribuido(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean validarNumeroUnico(String numeroUnicoProcesso) {

		Boolean valido = true;

		try {
			if (numeroUnicoProcesso.length() != 20) {
				return false;
			}

			long sequencial = obterCampo(numeroUnicoProcesso, 0, 7);
			long digito = obterCampo(numeroUnicoProcesso, 7, 9);
			long ano = obterCampo(numeroUnicoProcesso, 9, 13);
			long jtr = obterCampo(numeroUnicoProcesso, 13, 16);
			long origem = obterCampo(numeroUnicoProcesso, 16, 20);

			if (!validaJTR(jtr)) {
				return false;
			}

			String valor1 = "";
			long resto1 = 0;
			String valor2 = "";
			long resto2 = 0;
			String valor3 = "";

			valor1 = preencherZeros(sequencial, 7);
			resto1 = Long.parseLong(valor1) % 97;
			valor2 = preencherZeros(resto1, 2) + preencherZeros(ano, 4) + preencherZeros(jtr, 3);
			resto2 = Long.parseLong(valor2) % 97;
			valor3 = preencherZeros(resto2, 2) + preencherZeros(origem, 4) + preencherZeros(digito, 2);
			valido = ((Long.parseLong(valor3) % 97) == 1);

		} catch (Throwable t) {
			valido = false;
		}
		return valido;
	}

	private long obterCampo(String numeroUnico, Integer inicio, Integer fim) {

		String campo = numeroUnico.substring(inicio, fim).replaceAll("^0*", "");

		if (campo.length() == 0) {
			campo = "0";
		}

		return Long.parseLong(campo);
	}

	private String preencherZeros(long numero, long quantidade) {

		String temp = String.valueOf(numero);
		String retorno = "";

		if (quantidade < temp.length()) {
			return temp;
		} else {
			for (int i = 0; i < (quantidade - temp.length()); i++) {
				retorno = "0" + retorno;
			}
			return retorno + temp;
		}
	}

	// Validação de Tipo de Justiça e Tribunal
	private boolean validaJTR(Long jtr) {

		if (((jtr > 100) && (jtr < 200)) || ((jtr > 200) && (jtr < 300)) || ((jtr > 300) && (jtr < 401)) || ((jtr > 405) && (jtr < 500))
				|| ((jtr > 524) && (jtr < 600)) || ((jtr > 627) && (jtr < 700)) || ((jtr > 712) && (jtr < 801)) || ((jtr > 827) && (jtr < 901))
				|| ((jtr > 927))) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public boolean isBloqueadoBaixa(Processo processo) throws ServiceException {
		try {
			return (dao.isBloqueadoBaixa(processo));
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Boolean houveRemessa(ObjetoIncidente<?> objetoIncidente) throws ServiceException{
		try {
			return transacaoIntegracaoDao.houveRemessa(objetoIncidente.getId());
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
