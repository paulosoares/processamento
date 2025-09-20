package br.gov.stf.estf.documento.model.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.ArquivoProcessoEletronicoDao;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoPeticaoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.documento.model.service.TipoPecaProcessoService;
import br.gov.stf.estf.documento.model.util.ArquivoProcessoEletronicoSearchData;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.documento.util.ComparatorPecaProcessoEletronico;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PeticaoService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("arquivoProcessoEletronicoService")
public class ArquivoProcessoEletronicoServiceImpl
		extends
		GenericServiceImpl<ArquivoProcessoEletronico, Long, ArquivoProcessoEletronicoDao>
		implements ArquivoProcessoEletronicoService {

	private final DocumentoEletronicoService documentoEletronicoService;
	private final DocumentoTextoPeticaoService documentoTextoPeticaoService;
	private final DocumentoTextoService documentoTextoService;
	private final TipoPecaProcessoService tipoPecaProcessoService;
	private final PecaProcessoEletronicoService pecaProcessoEletronicoService;
	private final ObjetoIncidenteService objetoIncidenteService;
	private final TextoAndamentoProcessoService textoAndamentoProcessoService;

	@Autowired
	private ArquivoProcessoEletronicoServiceImpl(
			ArquivoProcessoEletronicoDao dao,
			DocumentoEletronicoService documentoEletronicoService,
			DocumentoTextoPeticaoService documentoTextoPeticaoService,
			DocumentoTextoService documentoTextoService,
			TipoPecaProcessoService tipoPecaProcessoService,
			PecaProcessoEletronicoService pecaProcessoEletronicoService,
			ObjetoIncidenteService objetoIncidenteService,
			PeticaoService peticaoService, ProcessoService processoService,
			TextoAndamentoProcessoService textoAndamentoProcessoService) {
		super(dao);
		this.documentoEletronicoService = documentoEletronicoService;
		this.documentoTextoPeticaoService = documentoTextoPeticaoService;
		this.documentoTextoService = documentoTextoService;
		this.tipoPecaProcessoService = tipoPecaProcessoService;
		this.pecaProcessoEletronicoService = pecaProcessoEletronicoService;
		this.objetoIncidenteService = objetoIncidenteService;
		this.textoAndamentoProcessoService = textoAndamentoProcessoService;
	}

	public List<ArquivoProcessoEletronico> pesquisarArquivoProcessoEletronico(
			String siglaClasseProcessual, Long numeroProcessual,
			Long numeroProtocolo, Short anoProtocolo, Long idDocumentoEletronico)
			throws ServiceException {

		List<ArquivoProcessoEletronico> result = null;

		try {
			result = dao.pesquisarArquivoProcessoEletronico(
					siglaClasseProcessual, numeroProcessual, numeroProtocolo,
					anoProtocolo, idDocumentoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return result;
	}

	public List<ArquivoProcessoEletronico> pesquisarArquivoProcessoEletronico(
			ArquivoProcessoEletronicoSearchData searchData)
			throws ServiceException {
		List<ArquivoProcessoEletronico> result = Collections.emptyList();

		// regra: nunca retornar documentos cancelados
		searchData.situacoesDocumentoEletronicoExcluidas.add(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO);

		try {
			result = dao.pesquisarArquivoProcessoEletronico(searchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return result;
	}

	public ArquivoProcessoEletronico salvarArquivoProcessoEletronicoAssinado(
			ArquivoProcessoEletronico arquivoProcessoEletronico,
			byte[] pdfAssinado, byte[] assinatura, byte[] carimboTempo,
			Date dataCarimboTempo) throws ServiceException {
		if (arquivoProcessoEletronico == null || pdfAssinado == null
				|| pdfAssinado.length == 0 || assinatura == null
				|| assinatura.length == 0 || carimboTempo == null
				|| carimboTempo.length == 0) {

			throw new IllegalArgumentException(
					"Faltando parâmetros para salvar o documento.");
		}

		if (arquivoProcessoEletronico.getDocumentoEletronico()
				.getDescricaoStatusDocumento()
				.equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO)) {
			throw new ServiceException("Peça processual assinada anteriomente.");
		}

		if (arquivoProcessoEletronico.getDocumentoEletronico()
				.getDescricaoStatusDocumento()
				.equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_CANCELADO)) {
			throw new ServiceException("Peça processual cancelada.");
		}



		DocumentoEletronico de = arquivoProcessoEletronico
				.getDocumentoEletronico();
		documentoEletronicoService.salvarDocumentoEletronicoAssinado(de,
				pdfAssinado, assinatura, carimboTempo, dataCarimboTempo);

		DocumentoTextoPeticao docTextoPet = null;
		DocumentoTexto docTexto = null;

		try {
			docTextoPet = documentoTextoPeticaoService.recuperar(de);
		} catch (ServiceException e) {
		}

		try {
			docTexto = documentoTextoService.recuperar(de);
		} catch (ServiceException e) {
		}

		if (docTexto != null || docTextoPet != null) {

			if (docTexto != null && docTexto.getTipoSituacaoDocumento() != null) {
				if ( !docTexto.getTipoSituacaoDocumento().equals(TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO) && 
					 !docTexto.getTipoSituacaoDocumento().equals(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE)) {
						docTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE);
						documentoTextoService.alterar(docTexto);
				}
			} else if (docTextoPet != null) {
				if (!docTextoPet.getTipoSituacaoDocumento().equals(
						TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO)
						&& !docTextoPet.getTipoSituacaoDocumento().equals(
								TipoSituacaoDocumento.ASSINADO_DIGITALMENTE)) {
					docTextoPet.setTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE);
					documentoTextoPeticaoService.alterar(docTextoPet);
				}
			}
		}

		/**
		 * Recuperando o tipo de situação do texto - Assinado digitalmente
		 */

		// alterar( arquivoProcessoEletronico );
		return arquivoProcessoEletronico;

	}

	@SuppressWarnings("rawtypes")
	public void inserirArquivoProcessoEletronicoJuntado(
			DocumentoTexto documentoTexto, String siglaTipoPecaProcesso,
			ObjetoIncidente objetoIncidente, Setor setor)
			throws ServiceException {
		try {
			PecaProcessoEletronico pecaProcessoEletronico = incluiPecaParaJuntada(
					siglaTipoPecaProcesso, objetoIncidente, setor);
			insereArquivoProcessoEletronico(pecaProcessoEletronico,
					documentoTexto.getDocumentoEletronico());
			alteraSituacaoDoDocumento(documentoTexto,
					TipoSituacaoDocumento.JUNTADO);
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}

	}

	private void alteraSituacaoDoDocumento(DocumentoTexto documentoTexto,
			TipoSituacaoDocumento tipoSituacaoDocumento)
			throws ServiceException {
		documentoTexto.setTipoSituacaoDocumento(tipoSituacaoDocumento);
		documentoTextoService.salvar(documentoTexto);
	}

	@SuppressWarnings("rawtypes")
	private PecaProcessoEletronico incluiPecaParaJuntada(String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente,	Setor setor) throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = montaPecaParaJuntada(siglaTipoPecaProcesso, objetoIncidente, setor);
		pecaProcessoEletronicoService.incluir(pecaProcessoEletronico);
		return pecaProcessoEletronico;
	}

	@SuppressWarnings("rawtypes")
	private PecaProcessoEletronico montaPecaParaJuntada(String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente, Setor setor) throws ServiceException {
		return montaPecaParaJuntada(siglaTipoPecaProcesso, objetoIncidente, setor, null);
	}
	
	@SuppressWarnings("rawtypes")
	private PecaProcessoEletronico montaPecaParaJuntada(String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente, Setor setor, String descricaoPecao) throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = montaPecaProcessoEletronico(siglaTipoPecaProcesso, objetoIncidente,	TipoSituacaoPeca.JUNTADA, setor, descricaoPecao);
		// Jubé - Inclusão de dados devido ao STF-Decisão. Dados definidos pelo
		// textual
		Long numeroDeOrdemDaPeca = pecaProcessoEletronicoService.recuperarProximoNumeroDeOrdem(objetoIncidente);
		pecaProcessoEletronico.setNumeroOrdemPeca(numeroDeOrdemDaPeca);
		pecaProcessoEletronico.setNumeroPagInicio(1L);
		pecaProcessoEletronico.setNumeroPagFim(1L);
		return pecaProcessoEletronico;
	}	

	@SuppressWarnings("rawtypes")
	public void inserirArquivoProcessoEletronicoPendenteJuntada(byte[] pdf,
			String siglaTipoPecaProcesso, String siglaClasse,
			Long numeroProcesso, Long codigoRecurso, Long tipoJulgamento,
			Setor setor) throws ServiceException {
		try {
			ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperar(
					siglaClasse, numeroProcesso, codigoRecurso, tipoJulgamento);
			inserirArquivoProcessoEletronicoPendenteJuntada(pdf,
					siglaTipoPecaProcesso, objetoIncidente, setor);
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}

	}

	@SuppressWarnings("rawtypes")
	public ArquivoProcessoEletronico inserirArquivoProcessoEletronicoPendenteJuntada(byte[] pdf,
			String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente,
			Setor setor) throws ServiceException {
		try {
			// Exclui peça anterior antes de incluir uma nova peça
			ArquivoProcessoEletronico ape = recuperarDocumentoPeca(
					objetoIncidente, siglaTipoPecaProcesso);
			if (ape != null) {
				excluirPeca(ape);
			}

			// Inclusão de nova peça
			PecaProcessoEletronico pecaProcessoEletronico = inserePecaPendenteDeJuntada(siglaTipoPecaProcesso, objetoIncidente, setor);
			DocumentoEletronico documentoEletronico = insereDocumentoEletronico(pdf);
			return insereArquivoProcessoEletronico(pecaProcessoEletronico, documentoEletronico);
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}
	
	

	private ArquivoProcessoEletronico insereArquivoProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico,
			DocumentoEletronico documentoEletronico) throws ServiceException {
		ArquivoProcessoEletronico arquivoProcessoEletronico = new ArquivoProcessoEletronico();
		arquivoProcessoEletronico.setDocumentoEletronico(documentoEletronico);
		arquivoProcessoEletronico.setPecaProcessoEletronico(pecaProcessoEletronico);
		arquivoProcessoEletronico.setNumeroOrdem(1L);
		return salvar(arquivoProcessoEletronico);
	}

	private DocumentoEletronico insereDocumentoEletronico(byte[] pdf)
			throws ServiceException {
		DocumentoEletronico documentoEletronico = new DocumentoEletronico();
		documentoEletronico.setArquivo(pdf);
		documentoEletronico.setTipoArquivo(TipoArquivo.PDF);
		documentoEletronico.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_INTERNO);
		documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_RASCUNHO);
		documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
		documentoEletronico.setSiglaSistema("PROCESSAMENTO");
		return documentoEletronicoService.salvar(documentoEletronico);
	}
	
	private DocumentoEletronico insereDocumentoEletronicoAguardandoAssinatura(byte[] pdf) throws ServiceException {
		DocumentoEletronico documentoEletronico = new DocumentoEletronico();
		documentoEletronico.setArquivo(pdf);
		documentoEletronico.setTipoArquivo(TipoArquivo.PDF);
		documentoEletronico.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_PUBLICO);
		documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_AGUARDANDO);
		documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
		documentoEletronico.setSiglaSistema("PROCESSAMENTO");
		return documentoEletronicoService.salvar(documentoEletronico);
	}	

	@SuppressWarnings("rawtypes")
	private PecaProcessoEletronico inserePecaPendenteDeJuntada(String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente, Setor setor) throws ServiceException {		
		return inserePecaPendenteDeJuntada(siglaTipoPecaProcesso,objetoIncidente,setor,null);
	}
	
	@SuppressWarnings("rawtypes")
	private PecaProcessoEletronico inserePecaPendenteDeJuntada(String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente, Setor setor, String descricaoPeca) throws ServiceException {		
		PecaProcessoEletronico pecaProcessoEletronico = montaPecaProcessoEletronico(siglaTipoPecaProcesso, objetoIncidente, TipoSituacaoPeca.PENDENTE, setor, descricaoPeca);
		pecaProcessoEletronicoService.incluir(pecaProcessoEletronico);
		return pecaProcessoEletronico;
	}

	@SuppressWarnings("rawtypes")
	private PecaProcessoEletronico montaPecaProcessoEletronico(String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente, TipoSituacaoPeca tipoSituacao, Setor setor, String descricaoPeca) throws ServiceException {		
		PecaProcessoEletronico pecaProcessoEletronico = new PecaProcessoEletronico();
		pecaProcessoEletronico.setObjetoIncidente(objetoIncidente);
		TipoPecaProcesso tipoPecaProcesso = tipoPecaProcessoService.recuperar(siglaTipoPecaProcesso);
		pecaProcessoEletronico.setTipoPecaProcesso(tipoPecaProcesso);
		pecaProcessoEletronico.setTipoSituacaoPeca(tipoSituacao);
		Long numeroDeOrdemDaPeca = pecaProcessoEletronicoService.recuperarProximoNumeroDeOrdem(objetoIncidente);
		pecaProcessoEletronico.setNumeroOrdemPeca(numeroDeOrdemDaPeca);
		pecaProcessoEletronico.setTipoOrigemPeca(PecaProcessoEletronico.TIPO_ORIGEM_INTERNA);
		pecaProcessoEletronico.setSetor(setor);
		pecaProcessoEletronico.setDescricaoPeca(descricaoPeca);
		return pecaProcessoEletronico;
	}

	public ArquivoProcessoEletronico recuperarDocumentoPeca(
			ObjetoIncidente objetoIncidente, String siglaTipoPecaProcesso)
			throws ServiceException {
		ArquivoProcessoEletronico peca = null;
		try {
			peca = dao.recuperarDocumentoPeca(objetoIncidente,
					siglaTipoPecaProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return peca;
	}

	public ArquivoProcessoEletronico recuperarDocumentoPeca(String siglaClasse,
			Long numeroProcesso, Long codigoRecurso, String tipoJulgamento,
			String siglaTipoPecaProcesso) throws ServiceException {
		ArquivoProcessoEletronico peca = null;
		try {
			peca = dao.recuperarDocumentoPeca(siglaClasse, numeroProcesso,
					codigoRecurso, tipoJulgamento, siglaTipoPecaProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return peca;
	}

	public void excluirPeca(ArquivoProcessoEletronico arquivoProcessoEletronico)
			throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = arquivoProcessoEletronico
				.getPecaProcessoEletronico();
		TipoSituacaoPeca tipoSituacaoPeca = TipoSituacaoPeca.EXCLUIDA;
		pecaProcessoEletronico.setTipoSituacaoPeca(tipoSituacaoPeca);
		pecaProcessoEletronicoService.alterar(pecaProcessoEletronico);

		DocumentoEletronico documentoEletronico = arquivoProcessoEletronico
				.getDocumentoEletronico();
		documentoEletronicoService.cancelarDocumento(documentoEletronico,
				"Peça excluída");

	}

	public ArquivoProcessoEletronico recuperarArquivoDoDocumentoTexto(
			DocumentoTexto documentoTexto) {
		return dao.recuperarArquivoDoDocumentoTexto(documentoTexto);

	}

	public List<ArquivoProcessoEletronico> pesquisarPecas(
			ObjetoIncidente objetoIncidente, String... siglaTipoPeca)
			throws ServiceException {
		List<ArquivoProcessoEletronico> pecas = null;
		try {
			pecas = dao.pesquisarPecas(objetoIncidente, siglaTipoPeca);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return pecas;
	}

	/**
	 * Método criado para recuperar as peças de acordo com um lista de objetos
	 * incidentes
	 */
	public List<ArquivoProcessoEletronico> pesquisarPecasPeloIdObjetoIncidente(
			Long idObjetoIncidentePrincipal) throws ServiceException {

		List<ArquivoProcessoEletronico> pecas = null;

		try {
			pecas = dao
					.pesquisarPecasPeloIdObjetoIncidente(idObjetoIncidentePrincipal);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return pecas;
	}

	public List<Long> pesquisarPecasSetor(Long codSetor,
			List<String> tiposAcesso) throws ServiceException {
		try {
			return dao.pesquisarPecasSetor(codSetor, tiposAcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<Long> pesquisarPecasSetor(Long codSetor,
			List<String> tiposAcesso, Boolean gabineteSEJ, String siglaClasse,
			Long numeroProcesso) throws ServiceException {
		try {
			return dao.pesquisarPecasSetor(codSetor, tiposAcesso, gabineteSEJ,
					siglaClasse, numeroProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<ArquivoProcessoEletronico> pesquisar(List<Long> idsArquivos)
			throws ServiceException {
		try {
			return dao.pesquisar(idsArquivos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int countPecasProcesso(Processo processo) throws ServiceException {
		try {
			return dao.countPecasProcesso(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<PecaProcessoEletronico> pesquisarPecasPelosDocumentos(
			List<Long> listaSeqDocumento) throws ServiceException {
		try {
			return dao.pesquisarPecasPelosDocumentos(listaSeqDocumento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}


	@Override
	public void copiarPecasEletronicas(Processo processoOrigem,
			ObjetoIncidente<?> objetoIncidenteDestino,
			List<PecaProcessoEletronico> pecasSelecionadas,
			Boolean apagarOrigem, Boolean inserirDescricao, Boolean inicioFim)
			throws ServiceException {

		List<PecaProcessoEletronico> listaPecasDestino = pecaProcessoEletronicoService.
																pesquisarPorProcesso((Processo) objetoIncidenteDestino.getPrincipal(),
																					 true);	
		
		Collections.sort(listaPecasDestino,	new ComparatorPecaProcessoEletronico(true));
		Collections.sort(pecasSelecionadas,new ComparatorPecaProcessoEletronico(true));

		List<PecaProcessoEletronico> pecasEletronicas = new ArrayList<PecaProcessoEletronico>();
		
		pecasEletronicas.addAll(pecasSelecionadas);

		Long numeroOrdemPeca = 1L;
		
		List<Long> listaNumeroOrdemPeca = new ArrayList<Long>();

		if (inicioFim) {
		
			for (PecaProcessoEletronico pecasEletronica : pecasEletronicas) {
				listaNumeroOrdemPeca.add(numeroOrdemPeca);
				numeroOrdemPeca++;
			}
			
			for (PecaProcessoEletronico pecaDestino : listaPecasDestino) {
				pecaDestino.setNumeroOrdemPeca(numeroOrdemPeca);
				numeroOrdemPeca++;
			}
			
			pecaProcessoEletronicoService.alterarTodos(listaPecasDestino);
			
		} else {
			if (listaPecasDestino.size() != 0) {
				numeroOrdemPeca = listaPecasDestino.get(listaPecasDestino.size() - 1).getNumeroOrdemPeca() + 1;
			}

			for (PecaProcessoEletronico pecasEletronica : pecasEletronicas) {
				listaNumeroOrdemPeca.add(numeroOrdemPeca);
				numeroOrdemPeca++;
			}
		}
		
		int indexListaNumeroOrdemPeca = 0;
		
		for (PecaProcessoEletronico ppe : pecasEletronicas) {
			StringBuffer descricao = new StringBuffer("");
			
			descricao.append((ppe.getDescricaoPeca() != null) ? ppe.getDescricaoPeca() : "");

			if (inserirDescricao) {
				descricao.append("(");
				descricao.append(processoOrigem.getIdentificacao());
				descricao.append(")");
			}
			
			PecaProcessoEletronico pecaProcessoEletronico = pecaProcessoEletronicoService.copiarPeca(ppe, objetoIncidenteDestino,	
											listaNumeroOrdemPeca.get(indexListaNumeroOrdemPeca), descricao.toString());
			
			
			for (ArquivoProcessoEletronico ape : ppe.getDocumentosEletronicos()) {
				ArquivoProcessoEletronico arquivoProcessoEletronico = new ArquivoProcessoEletronico();

				arquivoProcessoEletronico.setPecaProcessoEletronico(pecaProcessoEletronico);
				arquivoProcessoEletronico.setNumeroOrdem(pecaProcessoEletronico.getNumeroOrdemPeca());
				arquivoProcessoEletronico.setDocumentoEletronico(ape.getDocumentoEletronico());
				salvar(arquivoProcessoEletronico);
			}
			
			if(apagarOrigem){
				pecaProcessoEletronicoService.excluir(ppe, "", true, false);
			}
			
			indexListaNumeroOrdemPeca++;
			
		}
		
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public DocumentoEletronico inserirArquivoProcessoEletronicoPendenteJuntadaComTextoAndamentoProcesso(byte[] pdf,
			String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente,
			Setor setor, AndamentoProcesso andamentoProcesso, String descricaoPeca) throws ServiceException {
		try {
			// Exclui peça anterior antes de incluir uma nova peça
			ArquivoProcessoEletronico ape = recuperarDocumentoPeca(objetoIncidente, siglaTipoPecaProcesso);
			if (ape != null) {
				excluirPeca(ape);
			}

			// Inclusão de nova peça
			DocumentoEletronico documentoEletronico = insereDocumentoEletronicoAguardandoAssinatura(pdf);
			TextoAndamentoProcesso textoAndamentoProcesso = new TextoAndamentoProcesso();
			textoAndamentoProcesso.setAndamentoProcesso(andamentoProcesso);
			textoAndamentoProcesso.setSeqDocumento(documentoEletronico.getId());
			salvarTextoAndamentoProcesso(textoAndamentoProcesso);
			return documentoEletronico;
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}	

	/**
	 * salva uma peça eletrônica para um processo.
	 * @return 
	 * @throws ServiceException 
	 */
	public ArquivoProcessoEletronico salvarPecaEletronica(byte[] pdf, String siglaTipoPecaProcesso, ObjetoIncidente<?> objetoIncidente,
			Setor setor, AndamentoProcesso andamentoProcesso) throws ServiceException{
		DocumentoEletronico documentoEletronico = insereDocumentoEletronico(pdf, DocumentoEletronico.TIPO_ACESSO_PUBLICO);
		PecaProcessoEletronico pecaProcessoEletronico = incluiPecaParaJuntada(siglaTipoPecaProcesso, objetoIncidente, setor);
		ArquivoProcessoEletronico arquivoProcessoEletronico = insereArquivoProcessoEletronico(pecaProcessoEletronico, documentoEletronico);
		TextoAndamentoProcesso textoAndamentoProcesso = new TextoAndamentoProcesso();
		textoAndamentoProcesso.setAndamentoProcesso(andamentoProcesso);
		textoAndamentoProcesso.setSeqDocumento(documentoEletronico.getId());
		salvarTextoAndamentoProcesso(textoAndamentoProcesso);
		return arquivoProcessoEletronico;
	}

	private DocumentoEletronico insereDocumentoEletronico(byte[] pdf, String tipoVisualizacao) throws ServiceException {
		DocumentoEletronico documentoEletronico = new DocumentoEletronico();
		documentoEletronico.setArquivo(pdf);
		documentoEletronico.setTipoArquivo(TipoArquivo.PDF);
		documentoEletronico.setTipoAcesso(tipoVisualizacao);
		documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO);
		documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
		documentoEletronico.setSiglaSistema("PROCESSAMENTO");
		return documentoEletronicoService.salvar(documentoEletronico);
	}
	
	private void salvarTextoAndamentoProcesso(TextoAndamentoProcesso textoAndamentoProcesso) throws ServiceException{
		textoAndamentoProcessoService.persistirTextoAndamentoProcesso(textoAndamentoProcesso);
	}
	
	@Override
	public List<ArquivoProcessoEletronico> recuperarDocumentosPeca(Long id) throws ServiceException {
		List<ArquivoProcessoEletronico> result = null;

		try {
			result = dao.recuperarDocumentosPeca(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return result;
	}
}
