package br.gov.stf.estf.assinatura.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.assinatura.service.ComunicacaoDocumentoBase;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocalBase;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DeslocamentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoPaginatorResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.FaseComunicacao.FlagFaseAtual;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.estf.intimacao.model.dataaccess.ComunicacaoLocalDao;
import br.gov.stf.estf.intimacao.model.service.AndamentoProcessoComunicacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.ModeloComunicacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.ProcessoLocalService;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.estf.intimacao.model.vo.TipoRecebimentoComunicacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ComunicacaoExternaDTO;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ParteService;
import br.gov.stf.estf.usuario.model.dataaccess.PessoaDao;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public abstract class ComunicacaoServiceLocalBaseImpl implements
		ComunicacaoServiceLocalBase {

	private static final Log LOG = LogFactory
			.getLog(ComunicacaoServiceLocalBaseImpl.class);

	private static final String DSC_TIPO_PECA_INTEIRO_TEOR = "Inteiro teor do acórdão";
	private static final String DSC_TXT_COMUNICACAO = "A Secretaria Judiciária informa o retorno do processo ao STF, tendo em vista novo despacho/decisão nos autos";

	@Autowired
	private ComunicacaoService comunicacaoService;
	@Autowired
	private ComunicacaoIncidenteService comunicacaoIncidenteService;
	@Autowired
	private DeslocamentoComunicacaoService deslocamentoComunicacaoService;
	@Autowired
	private DocumentoComunicacaoService documentoComunicacaoService;
	@Autowired
	private FaseComunicacaoService faseComunicacaoService;
	@Autowired
	private MinistroService ministroService;
	@Autowired
	private PecaProcessoEletronicoService pecaProcessoEletronicoService;
	@Autowired
	private SetorService setorService;
	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;
	@Autowired
	private ModeloComunicacaoLocalService modeloComunicacaoServiceLocal;
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	@Autowired
	private PecaProcessoEletronicoComunicacaoService pecaProcessoEletronicoComunicacaoService;
	@Autowired
	private AndamentoProcessoComunicacaoLocalService andamentoProcessoComunicacaoLocalService;
	@Autowired
	private ComunicacaoLocalDao comunicacaoLocalDao;
	@Autowired
	private PessoaDao pessoaDao;
	@Autowired
	private ProcessoLocalService processoLocalServiceIntimacao;
	
	@Autowired
	private ParteService parteService;

	@Override
	public List<ComunicacaoDocumentoResult> pesquisarComunicacoes(
			String siglaProcesso, Long numeroProcesso,
			Long codigoFaseDocumento, Long codigoSetorAtual,
			Long numeracaoUnica, Long anoNumeracaoUnica, String dataInicial,
			String dataFinal) throws ServiceLocalException,
			RegraDeNegocioException {

		limparSessao(comunicacaoService);

		List<ComunicacaoDocumentoResult> documentos;

		try {
			documentos = comunicacaoService.pesquisarDocumentosElaborados(
					siglaProcesso, numeroProcesso, codigoFaseDocumento,
					codigoSetorAtual, numeracaoUnica, anoNumeracaoUnica,
					dataInicial, dataFinal);
		} catch (ServiceException exception) {
			throw new ServiceLocalException(
					MessageFormat.format(
							"Erro ao pesquisar documentos elaborados para o processo {0} {1} com a fase (cód.) {2} cujo setor atual é o (cód.) {3}.",
							siglaProcesso, numeroProcesso, codigoFaseDocumento,
							codigoSetorAtual), exception);
		}

		return documentos;
	}

	@Override
	public List<ComunicacaoDocumentoResult> pesquisarDocumentos(
			Long codigoFaseDocumento, Setor setor)
			throws ServiceLocalException, RegraDeNegocioException {
		FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
		filtro.setSetor(setor);
		filtro.setFaseDocumento(codigoFaseDocumento);
		ComunicacaoDocumentoPaginatorResult resultado = pesquisarDocumentos(filtro);
		return resultado.getLista();
	}

	@Override
	public ComunicacaoDocumentoPaginatorResult pesquisarDocumentos(
			FiltroPesquisarDocumentosAssinatura filtro)
			throws ServiceLocalException, RegraDeNegocioException {
		List<Long> situacaoDoPdf = new LinkedList<Long>();

		if (filtro.getFaseDocumento().equals(
				TipoFaseComunicacao.AGUARDANDO_ASSINATURA.getCodigoFase())
				|| filtro.getFaseDocumento().equals(
						TipoFaseComunicacao.PDF_GERADO.getCodigoFase())
				|| filtro.getFaseDocumento().equals(
						TipoFaseComunicacao.EM_REVISAO.getCodigoFase())
				|| filtro.getFaseDocumento().equals(
						TipoFaseComunicacao.REVISADO.getCodigoFase())) {

			// procurar os documentos que está com a situação de PDF gerado e
			// prontos para assinar
			situacaoDoPdf.add(TipoSituacaoDocumento.GERADO.getCodigo());

		} else if (filtro.getFaseDocumento().equals(
				TipoFaseComunicacao.ASSINADO.getCodigoFase())) {

			// buscar os documentos com PDF assinado e fase ASSINADO
			situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE
					.getCodigo());
			situacaoDoPdf.add(TipoSituacaoDocumento.JUNTADO.getCodigo());

		} else if (filtro.getFaseDocumento().equals(
				TipoFaseComunicacao.AGUARDANDO_ASSINATURA_MINISTRO
						.getCodigoFase())) {

			// buscar os documentos onde o PDF pode estar assinado ou não.
			situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE
					.getCodigo());
			situacaoDoPdf.add(TipoSituacaoDocumento.GERADO.getCodigo());

		} else if (filtro.getFaseDocumento().equals(
				TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO
						.getCodigoFase())) {

			// buscar os documentos com PDF assinado e fase Aguardando
			// Encaminhamento para o eSTF-Decisão
			situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE
					.getCodigo());
		}

		filtro.setListaTipoSituacaoDocumento(situacaoDoPdf);

		ComunicacaoDocumentoPaginatorResult resultado = efetuarPesquisarDocumentos(filtro);

		return resultado;
	}

	private ComunicacaoDocumentoPaginatorResult efetuarPesquisarDocumentos(
			FiltroPesquisarDocumentosAssinatura filtro)
			throws ServiceLocalException {
		limparSessao(comunicacaoService);

		ComunicacaoDocumentoPaginatorResult result;

		try {
			result = comunicacaoService.pesquisarDocumentosAssinatura(filtro);
			if (filtro.isCarregarFilhos()) {
				result.setLista(carregarFilhos(result.getLista()));
			}
		} catch (ServiceException exception) {
			throw new ServiceLocalException(MessageFormat.format(
					"Erro ao pesquisar documentos com a fase {0}.",
					TipoFaseComunicacao.valueOf(filtro.getFaseDocumento())
							.getDescricao()), exception);
		}

		return result;
	}

	private List<ComunicacaoDocumentoResult> carregarFilhos(
			List<ComunicacaoDocumentoResult> documentos) {
		for (ComunicacaoDocumentoResult documento : documentos) {
			Comunicacao comunicacao = documento.getComunicacao();
			for (ComunicacaoIncidente comunicacaoIncidente : comunicacao
					.getComunicacaoIncidente()) {
				comunicacaoIncidente.getComunicacao().getId();
				comunicacaoIncidente.getObjetoIncidente().getId();
				comunicacaoIncidente.getObjetoIncidente().getPrincipal()
						.getId();
			}
			if (documento.getListaPecasProcessoEletronicoComunicacao() != null) {
				for (PecaProcessoEletronicoComunicacao pecaProcessoEletronicoComunicacao : documento
						.getListaPecasProcessoEletronicoComunicacao()) {
					if (pecaProcessoEletronicoComunicacao
							.getPecaProcessoEletronico() != null) {
						pecaProcessoEletronicoComunicacao
								.getPecaProcessoEletronico()
								.getObjetoIncidente().getId();
						ObjetoIncidente objetoIncidente = pecaProcessoEletronicoComunicacao
								.getPecaProcessoEletronico()
								.getObjetoIncidente();
						objetoIncidente.getIdentificacao();
					}
				}
			}
		}
		return documentos;
	}

	@Override
	public List<ComunicacaoDocumentoResult> pesquisarDocumentos(Date data,
			Setor setor) throws ServiceLocalException, RegraDeNegocioException {
		validarDataParaPesquisa(data);

		limparSessao(comunicacaoService);

		List<ComunicacaoDocumentoResult> documentos = Collections.emptyList();

		try {
			documentos = comunicacaoService.pesquisarDocumentosUnidade(data,
					setor.getId());
		} catch (ServiceException exception) {
			throw new ServiceLocalException(exception);
		}

		return documentos;
	}

	private void validarDataParaPesquisa(Date data)
			throws RegraDeNegocioException {
		if (data == null) {
			throw new RegraDeNegocioException("É necessário informar uma data!");
		}

		Date dataAtual = recuperarDataAtual();
		if (data.after(dataAtual)) {
			throw new RegraDeNegocioException(
					"Não é possível pesquisar por data futura!");
		}
	}

	private void limparSessao(GenericService<?, ?, ?> service)
			throws ServiceLocalException {
		try {
			service.limparSessao();
		} catch (ServiceException exception) {
			throw new ServiceLocalException(
					"Erro ao limpar o cache da sessão.", exception);
		}
	}

	private Date recuperarDataAtual() {
		Date data = null;

		try {
			data = comunicacaoService.recuperarDataAtual();
		} catch (ServiceException exception) {
			LOG.info("Erro ao recuperar data atual do banco de dados.",
					exception);
		}

		// se não conseguir recuperar a data pelo banco, recuperar a data local
		if (data == null) {
			data = new Date();
		}

		return data;
	}

	@Override
	public void atualizarSessao() {
		recuperarDataAtual();
	}

	@Override
	public void inserirFaseComunicacao(Comunicacao comunicacao)
			throws ServiceException {
		FaseComunicacao faseComunicacao = new FaseComunicacao();
		faseComunicacao.setTipoFase(TipoFaseComunicacao.EXCLUIDO);
		faseComunicacao.setFlagFaseAtual(FlagFaseAtual.S);
		faseComunicacao.setDataLancamento(new Date());
		faseComunicacao.setComunicacao(comunicacao);
		faseComunicacaoService.incluir(faseComunicacao);
	}

	@Override
	public List pesquisarDocumentosPorComunicacao(Setor setor,
			Boolean buscaSomenteDocumentoFaseGerados) throws ServiceException {
		List lista = comunicacaoService.pesquisarDocumentos(null, null, setor,
				null, buscaSomenteDocumentoFaseGerados);
		carregarFilhos(lista);
		return lista;
	}

	@Override
	public List pesquisarDocumentosAssinatura(Setor setor,
			List<Long> tipoSituacaoDocumento, Long filtro)
			throws ServiceException {
		List lista = comunicacaoService.pesquisarDocumentosAssinatura(setor,
				tipoSituacaoDocumento, filtro);
		carregarFilhos(lista);
		return lista;
	}

	@Override
	public List<ComunicacaoExternaDTO> pesquisarComunicacaoExterna(
			String idParte,
			TipoRecebimentoComunicacaoEnum tipoRecebimentoComunicacaoEnum,
			String descricaoTipoComunicacao, String descricaoModelo,
			Date periodoEnvioInicio, Date periodoEnvioFim, Long idProcesso,
			Long idPreferemcia) throws ServiceException {
		try {
			return comunicacaoLocalDao.buscar(idParte,
					tipoRecebimentoComunicacaoEnum, descricaoTipoComunicacao,
					descricaoModelo, periodoEnvioInicio, periodoEnvioFim,
					idProcesso, idPreferemcia);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Long recuperarCodigoOrigemDestinatario(Long idComunicacao)
			throws ServiceException {
		try {
			return comunicacaoLocalDao
					.recuperarCodigoOrigemDestinatario(idComunicacao);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void encaminharParaAssinaturaSetor(
			List<? extends ComunicacaoDocumentoBase> comunicacaoDocumentos,
			Long idSetorDestino, boolean permitirGabinete,
			boolean incluirFaseNoDeslocamento,
			boolean naoIncluirFaseDeslocamentoSeNaoForGabinete,
			TipoFaseComunicacao... tiposFasesPermitidos)
			throws ServiceLocalException, RegraDeNegocioException {

		encaminharParaSetor(comunicacaoDocumentos, idSetorDestino,
				permitirGabinete, incluirFaseNoDeslocamento,
				naoIncluirFaseDeslocamentoSeNaoForGabinete,
				TipoFaseComunicacao.AGUARDANDO_ASSINATURA, tiposFasesPermitidos);

	}

	private void encaminharParaSetor(
			List<? extends ComunicacaoDocumentoBase> comunicacaoDocumentos,
			Long idSetorDestino, boolean permitirGabinete,
			boolean incluirFaseNoDeslocamento,
			boolean naoIncluirFaseDeslocamentoSeNaoForGabinete,
			TipoFaseComunicacao novaFase,
			TipoFaseComunicacao... tiposFasesPermitidos)
			throws ServiceLocalException, RegraDeNegocioException {

		if (idSetorDestino == null) {
			throw new RegraDeNegocioException(
					"É necessário informar o setor de destino!");
		}

		boolean isGabinete = isGabinete(idSetorDestino);
		if (LOG.isDebugEnabled()) {
			LOG.info("Encaminhando documentos. O setor de destino é um gabinete? "
					+ isGabinete);
		}

		validarDocumentosParaEncaminhamento(comunicacaoDocumentos,
				permitirGabinete, isGabinete, tiposFasesPermitidos);

		if (LOG.isDebugEnabled()) {
			LOG.debug("incluirFaseNoDeslocamento = "
					+ incluirFaseNoDeslocamento
					+ ", naoIncluirFaseDeslocamentoSeNaoForGabinete = "
					+ naoIncluirFaseDeslocamentoSeNaoForGabinete);
		}

		for (ComunicacaoDocumentoBase doc : comunicacaoDocumentos) {
			encaminharParaSetor(doc, idSetorDestino, isGabinete,
					incluirFaseNoDeslocamento,
					naoIncluirFaseDeslocamentoSeNaoForGabinete, novaFase);
		}
	}

	private void encaminharParaSetor(ComunicacaoDocumentoBase doc,
			Long idSetorDestino, boolean isGabinete,
			boolean incluirFaseNoDeslocamento,
			boolean naoIncluirFaseDeslocamentoSeNaoForGabinete,
			TipoFaseComunicacao novaFase) throws ServiceLocalException {
		try {
			if ((incluirFaseNoDeslocamento
					&& naoIncluirFaseDeslocamentoSeNaoForGabinete && !isGabinete)
					|| !incluirFaseNoDeslocamento) {
				deslocamentoComunicacaoService.incluirDeslocamento(
						doc.getComunicacao(), idSetorDestino, null);
			} else {
				deslocamentoComunicacaoService.incluirDeslocamento(
						doc.getComunicacao(), idSetorDestino, novaFase);
			}
		} catch (ServiceException exception) {
			throw new ServiceLocalException(exception);
		}
	}

	private boolean isGabinete(Long idSetorDestino)
			throws ServiceLocalException {
		boolean isGabinete = false;

		try {
			isGabinete = setorService.isSetorGabinete(setorService
					.recuperarPorId(idSetorDestino));
		} catch (ServiceException exception) {
			throw new ServiceLocalException("Erro ao processar o setor atual.",
					exception);
		}

		return isGabinete;
	}

	private void validarDocumentosParaEncaminhamento(
			List<? extends ComunicacaoDocumentoBase> comunicacaoDocumentos,
			boolean permitirGabinete, boolean isGabinete,
			TipoFaseComunicacao... tiposFasesPermitidos)
			throws RegraDeNegocioException {
		for (ComunicacaoDocumentoBase doc : comunicacaoDocumentos) {
			validarFaseComunicacaoParaEncaminhamento(doc, tiposFasesPermitidos);

			// Verifica se o documento está sendo encaminhado para um Gabinete e
			// não necessita de assinatura do Ministro
			if (permitirGabinete && isGabinete
					&& !doc.isNecessariaAssinaturaMinistroComunicacao()) {
				throw new RegraDeNegocioException(
						"O(s) documento(s) selecionado(s) não necessita(m) de assinatura do Ministro, portanto não deve(m) ser encaminhado(s) para um Gabinete.");
			} else if (!permitirGabinete && isGabinete) {
				throw new RegraDeNegocioException(
						"O(s) documento(s) selecionado(s) não deve(m) ser encaminhado(s) para Gabinetes.");
			}
		}
	}

	private void validarFaseComunicacaoParaEncaminhamento(
			ComunicacaoDocumentoBase doc,
			TipoFaseComunicacao... tiposFasesPermitidos)
			throws RegraDeNegocioException {
		if (!doc.isFaseAtualComunicacao(tiposFasesPermitidos)) {
			throw new RegraDeNegocioException(
					MessageFormat
							.format("Somente documentos na(s) seguinte(s) fase(s) podem ser encaminhados: {0}. Documento não permitido: {1}.",
									toString(tiposFasesPermitidos),
									doc.getIdentificacaoComunicacao()));
		}
	}

	private String toString(TipoFaseComunicacao[] tiposFasesPermitidos) {
		StringBuilder builder = new StringBuilder();

		for (TipoFaseComunicacao tipoFaseComunicacao : tiposFasesPermitidos) {
			builder.append("\"").append(tipoFaseComunicacao.getDescricao())
					.append("\"").append("; ");
		}

		// remove o último ponto-e-vírgula
		builder.replace(builder.length() - 2, builder.length(), "");

		return builder.toString();
	}
	
	@Override
	public Comunicacao criarComunicacaoIntimacao(Date dataEnvioComunicacao,
			Setor setor, String usuarioCriador, Long codigoPessoaDestinatario,
			ModeloComunicacao modeloComunicacao,
			Collection<Long> idsObjetoIncidente,
			TipoFaseComunicacao tipoFaseComunicacao, List<?> pecas,
			List<AndamentoProcesso> andamentos, Long numeroDocumento,
			String descricaoComunicacao, byte[] arquivoEletronico,
			DocumentoEletronico documentoAcordao) throws ServiceException {
		return criarComunicacaoIntimacao(dataEnvioComunicacao, setor, usuarioCriador, codigoPessoaDestinatario, null, idsObjetoIncidente, tipoFaseComunicacao, pecas, andamentos, numeroDocumento, descricaoComunicacao, arquivoEletronico, documentoAcordao, modeloComunicacao);
	}
	
	@Override
	public Comunicacao criarComunicacaoIntimacao(Date dataEnvioComunicacao,
			Setor setor, String usuarioCriador, Long codigoPessoaDestinatario,
			ModeloComunicacaoEnum modeloComunicacaoEnum,
			Collection<Long> idsObjetoIncidente,
			TipoFaseComunicacao tipoFaseComunicacao, List<?> pecas,
			List<AndamentoProcesso> andamentos, Long numeroDocumento,
			String descricaoComunicacao, byte[] arquivoEletronico,
			DocumentoEletronico documentoAcordao) throws ServiceException {
		
		return criarComunicacaoIntimacao(dataEnvioComunicacao, setor, usuarioCriador, codigoPessoaDestinatario, modeloComunicacaoEnum, idsObjetoIncidente, tipoFaseComunicacao, pecas, andamentos, numeroDocumento, descricaoComunicacao, arquivoEletronico, documentoAcordao, null);
	}

	private Comunicacao criarComunicacaoIntimacao(Date dataEnvioComunicacao,
			Setor setor, String usuarioCriador, Long codigoPessoaDestinatario,
			ModeloComunicacaoEnum modeloComunicacaoEnum,
			Collection<Long> idsObjetoIncidente,
			TipoFaseComunicacao tipoFaseComunicacao, List<?> pecas,
			List<AndamentoProcesso> andamentos, Long numeroDocumento,
			String descricaoComunicacao, byte[] arquivoEletronico,
			DocumentoEletronico documentoAcordao, ModeloComunicacao modeloComunicacao) throws ServiceException {

		Comunicacao comunicacao = new Comunicacao();

		if (modeloComunicacao == null)
			modeloComunicacao = modeloComunicacaoServiceLocal.buscar(modeloComunicacaoEnum);

		comunicacao.setDscNomeDocumento(ModeloComunicacaoEnum.VISTA_A_PGR == modeloComunicacaoEnum || ModeloComunicacaoEnum.NOTIFICACA_REQUISICAO_PROCESSO == modeloComunicacaoEnum  ? descricaoComunicacao : modeloComunicacao.getDscModelo());
		comunicacao.setModeloComunicacao(modeloComunicacao);
		comunicacao.setDataEnvio(dataEnvioComunicacao);
		comunicacao.setSetor(setor);
		if (codigoPessoaDestinatario == null) {
			throw new ServiceException(
					"Um usuário ou uma pessoa deve ser informada como destinatário da comunicação.");
		}
		Pessoa pessoaDestinatario;
		try {
			pessoaDestinatario = pessoaDao.recuperarPorId(codigoPessoaDestinatario);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao buscar pessoa destinatário.",
					ex);
		}
		comunicacao.setPessoaDestinataria(pessoaDestinatario);
		comunicacao.setUsuarioCriacao(usuarioCriador);
		comunicacao.setNumeroComunicacao(numeroDocumento);
		comunicacao.setObsComunicacao(ModeloComunicacaoEnum.NOTIFICACA_REQUISICAO_PROCESSO == modeloComunicacaoEnum ? DSC_TXT_COMUNICACAO : null);
		comunicacao = comunicacaoService.salvar(comunicacao);
		
		Long objetoIncidente = ((List<Long>) idsObjetoIncidente).get(0);
			
		if (ModeloComunicacaoEnum.NOTIFICACA_REQUISICAO_PROCESSO == modeloComunicacaoEnum || Arrays.asList(ModeloComunicacao.INCLUIDO_NA_PAUTA, ModeloComunicacao.EXCLUIDO_DA_PAUTA).contains(modeloComunicacao))
			criarComunicacaoObjetoIncidente(objetoIncidente, comunicacao, FlagProcessoLote.P);
		
		
		if(ModeloComunicacaoEnum.NOTIFICACA_REQUISICAO_PROCESSO != modeloComunicacaoEnum && pecas != null ){
			associarPecaProcessoEletronicoComunicacao(pecas, comunicacao,
					documentoAcordao);
		}
		

		criarFaseComunicacao(dataEnvioComunicacao, tipoFaseComunicacao, comunicacao);

		if (arquivoEletronico != null) {
			criarDocumentoIntimacao(comunicacao, arquivoEletronico);
		}

		return comunicacao;
	}
	
	@Override
	public void associarAndamentoProcessoComunicacao(List<AndamentoProcesso> andamentos, Comunicacao comunicacao) throws ServiceException {
        for (AndamentoProcesso andamentoProcesso : andamentos) {
            AndamentoProcessoComunicacao andamentoProcessoComunicacao = new AndamentoProcessoComunicacao();
            andamentoProcessoComunicacao.setAndamentoProcesso(andamentoProcesso);
            andamentoProcessoComunicacao.setComunicacao(comunicacao);
            andamentoProcessoComunicacao.setTipoVinculoAndamento(TipoVinculoAndamento.RELACIONADO);
            andamentoProcessoComunicacaoLocalService.salvar(andamentoProcessoComunicacao);
        }
    }

	@Override
	public ComunicacaoIncidente criarComunicacaoObjetoIncidente(
			Long idObjetoIncidente, Comunicacao comunicacao,
			FlagProcessoLote tipoVinculo) throws ServiceException {
		ObjetoIncidente objetoIncidente = objetoIncidenteService
				.recuperarPorId(idObjetoIncidente);
		List<AndamentoProcesso> andamentoProcessos;
		Processo processo;
		if(objetoIncidente instanceof IncidenteJulgamento || objetoIncidente instanceof RecursoProcesso){
			processo = processoLocalServiceIntimacao.recuperarProcessoEletronicoPorIdProcessoPrincipal(objetoIncidente.getPrincipal().getId());
			andamentoProcessos =  andamentoProcessoComunicacaoLocalService.pesquisarAndamentoProcesso(processo.getSiglaClasseProcessual(), processo.getNumeroProcessual());
		}else{
			processo = (Processo) objetoIncidente;
			andamentoProcessos =  andamentoProcessoComunicacaoLocalService.pesquisarAndamentoProcesso(processo.getSiglaClasseProcessual(), processo.getNumeroProcessual());
		}
		ComunicacaoIncidente comunicacaoIncidente = new ComunicacaoIncidente();
		comunicacaoIncidente.setObjetoIncidente(objetoIncidente);
		comunicacaoIncidente.setTipoVinculo(tipoVinculo);
		comunicacaoIncidente.setComunicacao(comunicacao);
		comunicacaoIncidente.setAndamentoProcesso(andamentoProcessos.get(0));
		return comunicacaoIncidenteService.salvar(comunicacaoIncidente);
	}

	/**
	 * Associa pecas processo eletrônico comunicação a comunicação
	 *
	 * @param pecas
	 * @param comunicacao
	 * @return
	 * @throws ServiceException
	 */
	private void associarPecaProcessoEletronicoComunicacao(List<?> pecas,
			Comunicacao comunicacao, DocumentoEletronico documentoAcordao)
			throws ServiceException {

		List<PecaProcessoEletronico> listaPecas = new ArrayList<PecaProcessoEletronico>();
		Set<Long> idsPecas = new HashSet<Long>();
		if (!pecas.isEmpty()) {
			Object peca = (Object) pecas.get(0);
			if (peca instanceof PecaDTO) {
				for (Object objetoPeca : pecas) {
					PecaDTO pecaDto = (PecaDTO) objetoPeca;
					if (!idsPecas.contains(pecaDto.getId())) {
						idsPecas.add(pecaDto.getId());
						PecaProcessoEletronico pecaProcessoEletronico = new PecaProcessoEletronico();
						pecaProcessoEletronico.setId(pecaDto.getId());
						pecaProcessoEletronico.setNumeroPagInicio(pecaDto
								.getNumeroPagInicio());
						pecaProcessoEletronico.setNumeroPagFim(pecaDto
								.getNumeroPagFim());
						pecaProcessoEletronico.setNumeroOrdemPeca(pecaDto
								.getNumeroOrdemPeca());
						pecaProcessoEletronico.setAndamentoProtocolo(pecaDto
								.getAndamentoProtocolo());
						pecaProcessoEletronico.setSetor(pecaDto.getSetor());
						pecaProcessoEletronico.setTipoOrigemPeca(pecaDto
								.getTipoOrigemPeca());
						pecaProcessoEletronico.setDescricaoPeca(pecaDto
								.getDescricaoPeca());
						pecaProcessoEletronico.setDocumentos(pecaDto
								.getDocumentos());
						pecaProcessoEletronico.setLembretes(pecaDto
								.getLembretes());
						pecaProcessoEletronico.setTipoSituacaoPeca(pecaDto
								.getTipoSituacaoPeca());
						pecaProcessoEletronico.setTipoPecaProcesso(pecaDto
								.getTipoPecaProcesso());
						pecaProcessoEletronico.setDocumentosEletronicos(pecaDto
								.getDocumentosEletronicos());
						pecaProcessoEletronico.setObjetoIncidente(pecaDto
								.getObjetoIncidente());
						pecaProcessoEletronico.setDataAlteracao(pecaDto
								.getDataAlteracao());
						pecaProcessoEletronico.setDataInclusao(pecaDto
								.getDataInclusao());

						listaPecas.add(pecaProcessoEletronico);
					}
				}
			} else if (peca instanceof PecaProcessoEletronico) {
				listaPecas = (List<PecaProcessoEletronico>) pecas;
			}
		}

		for (PecaProcessoEletronico pecaProcessoEletronico : listaPecas) {
			PecaProcessoEletronicoComunicacao pecaProcessoEletronicoComunicacao = new PecaProcessoEletronicoComunicacao();
			pecaProcessoEletronicoComunicacao
					.setPecaProcessoEletronico(pecaProcessoEletronico);

			if (pecaProcessoEletronico.getTipoPecaProcesso().getDescricao()
					.equals(DSC_TIPO_PECA_INTEIRO_TEOR)
					&& documentoAcordao != null) {
				pecaProcessoEletronicoComunicacao
						.setDocumentoAcordao(documentoAcordao);
			}

			pecaProcessoEletronicoComunicacao.setComunicacao(comunicacao);
			pecaProcessoEletronicoComunicacao
					.setDataVinculacao(pecaProcessoEletronico
							.getDataAlteracao() == null ? new Date() : pecaProcessoEletronico
							.getDataAlteracao());
		
			pecaProcessoEletronicoComunicacaoService
					.salvar(pecaProcessoEletronicoComunicacao);
		}
	}

	/**
	 * Cria uma fase de comunicação e define como única fase d lista de fases da
	 * comunicação informada.
	 *
	 * @param comunicacao
	 * @param pdf
	 * @param usuario
	 * @return
	 * @throws ServiceException
	 */
	private void criarFaseComunicacao(Date dataEnvioComunicacao,
			TipoFaseComunicacao tipoFaseComunicacao, Comunicacao comunicacao)
			throws ServiceException {
		FaseComunicacao faseComunicacao = new FaseComunicacao();
		faseComunicacao.setComunicacao(comunicacao);
		faseComunicacao.setTipoFase(tipoFaseComunicacao);
		faseComunicacao.setDataLancamento(dataEnvioComunicacao);
		faseComunicacao.setFlagFaseAtual(FaseComunicacao.FlagFaseAtual.S);
		faseComunicacaoService.salvar(faseComunicacao);
		List<FaseComunicacao> fases = new ArrayList<FaseComunicacao>();
		fases.add(faseComunicacao);
		comunicacao.setFases(fases);
	}

	/**
	 * Cria um objeto do tipo DocumentoEletronico para intimação e o associa à
	 * comunicação informada.
	 *
	 * @param comunicacao
	 * @param pdf
	 * @return
	 * @throws ServiceException
	 */
	private DocumentoComunicacao criarDocumentoIntimacao(
			Comunicacao comunicacao, byte[] pdf) throws ServiceException {
		DocumentoEletronico documentoEletronico = new DocumentoEletronico();
		documentoEletronico.setArquivo(pdf);
		documentoEletronico.setTipoArquivo(br.gov.stf.estf.entidade.documento.TipoArquivo.PDF);
		documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_AGUARDANDO);
		documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
		documentoEletronico.setSiglaSistema("PROCESSAMENTO");
		documentoEletronico = documentoEletronicoService.salvar(documentoEletronico);

		DocumentoComunicacao documentoComunicacao = new DocumentoComunicacao();
		documentoComunicacao.setComunicacao(comunicacao);
		documentoComunicacao.setDocumentoEletronico(documentoEletronico);
		documentoComunicacao
				.setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);
		return documentoComunicacaoService.salvar(documentoComunicacao);
	}

	@Override
	public void encaminharParaRevisaoSetor(
			List<ComunicacaoDocumentoBase> comunicacaoDocumentos,
			Long idSetorDestino, boolean permitirGabinete,
			boolean incluirFaseNoDeslocamento,
			boolean naoIncluirFaseDeslocamentoSeNaoForGabinete,
			TipoFaseComunicacao... tiposFasesPermitidos)
			throws ServiceLocalException, RegraDeNegocioException {

		encaminharParaSetor(comunicacaoDocumentos, idSetorDestino,
				permitirGabinete, incluirFaseNoDeslocamento,
				naoIncluirFaseDeslocamentoSeNaoForGabinete,
				TipoFaseComunicacao.EM_REVISAO, tiposFasesPermitidos);

	}

	@Override
	public Long pesquisarSetorDestinoPadrao(
			List<? extends ComunicacaoDocumentoBase> comunicacaoDocumentos)
			throws ServiceLocalException, RegraDeNegocioException {
		Long idSetorDestino;
		ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteUnico(comunicacaoDocumentos);

		if (objetoIncidente != null
				&& isNecessariaAssinaturaMinistro(comunicacaoDocumentos)) {
			Ministro ministro = recuperarMinistroRelator(objetoIncidente);
			Setor setor = ministro.getSetor();

			if (setor != null) {
				idSetorDestino = setor.getId();
			} else {
				idSetorDestino = Setor.CODIGO_GABINETE_SECRETARIA_JUDICIARIA;
			}
		} else {
			idSetorDestino = Setor.CODIGO_GABINETE_SECRETARIA_JUDICIARIA;
		}

		return idSetorDestino;
	}

	private Ministro recuperarMinistroRelator(ObjetoIncidente<?> objetoIncidente)
			throws ServiceLocalException {
		Ministro ministro = null;

		try {
			ministro = ministroService
					.recuperarMinistroRelator(objetoIncidente);
		} catch (ServiceException exception) {
			throw new ServiceLocalException(MessageFormat.format(
					"Erro ao recuperar ministro relator do incidente: {0}.",
					objetoIncidente.getIdentificacao()), exception);
		}

		return ministro;
	}

	private boolean isNecessariaAssinaturaMinistro(
			List<? extends ComunicacaoDocumentoBase> comunicacaoDocumentos) {
		boolean assinaturaNecessaria = false;

		for (ComunicacaoDocumentoBase comunicacaoDocumento : comunicacaoDocumentos) {
			if (comunicacaoDocumento
					.isNecessariaAssinaturaMinistroComunicacao()) {
				assinaturaNecessaria = true;
				break;
			}
		}

		return assinaturaNecessaria;
	}

	private ObjetoIncidente<?> getObjetoIncidenteUnico(
			List<? extends ComunicacaoDocumentoBase> comunicacaoDocumentos) {
		return CollectionUtils.isNotVazia(comunicacaoDocumentos) ? comunicacaoDocumentos
				.get(0).getComunicacao().getObjetoIncidenteUnico()
				: null;
	}
}
