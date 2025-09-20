package br.gov.stf.estf.documento.model.service.impl;

import static br.gov.stf.estf.entidade.documento.DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoComunicacaoDao;
import br.gov.stf.estf.documento.model.dataaccess.hibernate.DocumentoComunicacaoDaoHibernate;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.DeslocamentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaAcessoDocumento;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("documentoComunicacaoService")
public class DocumentoComunicacaoServiceImpl extends GenericServiceImpl<DocumentoComunicacao, Long, DocumentoComunicacaoDao> implements
		DocumentoComunicacaoService {

	private static final long CODIGO_ANDAMENTO_EDITAL_ENCAMINHADO_PUBLICACAO = 8451L;
	private static final String SIGLA_TIPO_DOCUMENTO_COMUNICACAO = "COMUNICA";
	private static final String SIGLA_CERTIDAO_TRANSITO_JULGADO = "CERTTRAN";
	private static final String SIGLA_CONTRARRAZAO = "CONTRRAZAO";
	private static final String SIGLA_CONTRAMINUTA = "CONTRMINUTA";
	private static final String SIGLA_CONTRARRAZOES_DECURSO_PRAZO = "DECUR_CONTRRAZAO";
	private static final String SIGLA_RESPOSTA_DECURSO_PRAZO = "DECUR_RESPOSTA";
	
	private static final String DESCRICAO_MODELO_CERTIDAO_TRANSITO_JULGADO = "Certidão Trânsito Julgado";
	private static final String DESCRICAO_MODELO_CONTRARRAZAO = "Contrarrazão de Agravo";
	private static final String DESCRICAO_MODELO_CONTRAMINUTA = "Contraminuta de Embargo";	
	private static final String DESCRICAO_CONTRARRAZOES_DECURSO_PRAZO = "Decurso de prazo para contrarrazões";
	private static final String DESCRICAO_RESPOSTA_DECURSO_PRAZO = "Decurso de prazo para resposta";
		

	private final DocumentoEletronicoService documentoEletronicoService;
	private final PecaProcessoEletronicoService pecaProcessoEletronicoService;
	private final FaseComunicacaoService faseComunicacaoService;
	private final AndamentoProcessoService andamentoProcessoService;
	private final UsuarioService usuarioService;
	private final ArquivoProcessoEletronicoService arquivoProcessoEletronicoService;
	private final SetorService setorService;
	private final DeslocamentoComunicacaoService deslocamentoComunicacaoService;
	private final ComunicacaoIncidenteService comunicacaoIncidenteService;

	public DocumentoComunicacaoServiceImpl(DocumentoComunicacaoDao dao, PecaProcessoEletronicoService pecaProcessoEletronicoService,
			DocumentoEletronicoService documentoEletronicoService, AssinaturaDigitalService assinaturaDigitalService,
			FaseComunicacaoService faseComunicacaoService, AndamentoProcessoService andamentoProcessoService, UsuarioService usuarioService,
			ArquivoProcessoEletronicoService arquivoProcessoEletronicoService, DeslocamentoComunicacaoService deslocamentoComunicacaoService,
			SetorService setorService, ComunicacaoIncidenteService comunicacaoIncidenteService) {
		super(dao);
		this.documentoEletronicoService = documentoEletronicoService;
		this.pecaProcessoEletronicoService = pecaProcessoEletronicoService;
		this.faseComunicacaoService = faseComunicacaoService;
		this.andamentoProcessoService = andamentoProcessoService;
		this.usuarioService = usuarioService;
		this.arquivoProcessoEletronicoService = arquivoProcessoEletronicoService;
		this.deslocamentoComunicacaoService = deslocamentoComunicacaoService;
		this.setorService = setorService;
		this.comunicacaoIncidenteService = comunicacaoIncidenteService;
	}

	@Override
	public DocumentoComunicacao recuperarNaoCancelado(Comunicacao comunicacao) throws ServiceException {
		DocumentoComunicacao documentoComunicacao = null;
		try {
			documentoComunicacao = dao.recuperarNaoCancelado(comunicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return documentoComunicacao;
	}

	@Override
	public Boolean cancelarDocumentos(DocumentoComunicacao documentoComunicacao, String motivoCancelamento, String userName) throws ServiceException {
		TipoSituacaoDocumento situacaoDocumento = TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO;
		return cancelarDocumentos(documentoComunicacao, motivoCancelamento, situacaoDocumento, userName);
	}

	public Boolean cancelarDocumentos(DocumentoComunicacao documentoComunicacao, String motivoCancelamento, TipoSituacaoDocumento situacaoDocumento,
			String userName) throws ServiceException {
		try {

			documentoComunicacao.setTipoSituacaoDocumento(situacaoDocumento);
			documentoComunicacao.setDscObservacao(motivoCancelamento);
			dao.salvar(documentoComunicacao);
			documentoEletronicoService.cancelarDocumento(documentoComunicacao.getDocumentoEletronico(), motivoCancelamento);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return true;
	}

	@Override
	public void devolverDocumentoeSTFDecisao(DocumentoComunicacao documentoComunicacao, String observacao, String usuario) throws ServiceException {

		// cancela o PDF
		cancelarDocumentos(documentoComunicacao, observacao, usuario);

		// lança a fase de correção
		faseComunicacaoService.incluirFase(TipoFaseComunicacao.CORRECAO, documentoComunicacao.getComunicacao(), observacao, null);

		// desloca para o setor que criou o documento
		deslocamentoComunicacaoService.incluirDeslocamento(documentoComunicacao.getComunicacao(), documentoComunicacao.getComunicacao().getSetor().getId(),
				null);

	}

	@Override
	public void salvarDocumentoComunicacaoAssinadoContingencialmenteeSTFDecisao(DocumentoComunicacao documentoComunicacao, byte[] pdfAssinado, Date dataAssinatura, String usuario,
			String siglaSistema) throws ServiceException {
		documentoEletronicoService.salvarDocumentoEletronicoAssinadoContingencialmente(documentoComunicacao.getDocumentoEletronico(), pdfAssinado);
		alterarFaseComunicacaoAssinadaeSTFDecisao(documentoComunicacao, dataAssinatura, usuario);
	}

	@Override
	public void salvarDocumentoComunicacaoAssinadoeSTFDecisao(DocumentoComunicacao documentoComunicacao, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, String usuario, String siglaSistema, FlagTipoAssinatura tipoAssinatura) throws ServiceException {

		Comunicacao comunicacao = documentoComunicacao.getComunicacao();
		ObjetoIncidente<?> objetoIncidente = comunicacao.getObjetoIncidenteUnico();

		if (documentoComunicacao == null || pdfAssinado == null || pdfAssinado.length == 0 || assinatura == null || assinatura.length == 0
				|| carimboTempo == null || carimboTempo.length == 0) {

			throw new IllegalArgumentException("Faltando parâmetros para salvar o documento.");
		}

		/**
		 * Salvando o novo array de bytes do PDF
		 */
		documentoEletronicoService.salvarDocumentoEletronicoAssinado(documentoComunicacao.getDocumentoEletronico()
																	,pdfAssinado
																	,assinatura
																	,carimboTempo
																	,dataCarimboTempo
																	,tipoAssinatura);
		alterarFaseComunicacaoAssinadaeSTFDecisao(documentoComunicacao, dataCarimboTempo, usuario);
	}

	@Override
	public void salvarDocumentoComunicacaoAssinadoeSTFDecisao(DocumentoComunicacao documentoComunicacao,
			byte[] pdfAssinado, byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo, String usuario,
			String siglaSistema) throws ServiceException {
		salvarDocumentoComunicacaoAssinadoeSTFDecisao(documentoComunicacao, pdfAssinado, assinatura, carimboTempo,
				dataCarimboTempo, usuario, siglaSistema, FlagTipoAssinatura.PADRAO);
	}
	
	private void alterarFaseComunicacaoAssinadaeSTFDecisao(DocumentoComunicacao documentoComunicacao, Date dataAssinatura, String usuario) throws ServiceException {
		Comunicacao comunicacao = documentoComunicacao.getComunicacao();
		ObjetoIncidente<?> objetoIncidente = comunicacao.getObjetoIncidenteUnico();
		
		documentoComunicacao.setTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE);
		alterar(documentoComunicacao);

		if (objetoIncidente != null && objetoIncidente.getId() > 0) {

			Setor setor = new Setor();
			setor = usuarioService.recuperarUsuario(usuario.toUpperCase()).getSetor();

			// Gera a FASE ASSINADO
			faseComunicacaoService.incluirFase(TipoFaseComunicacao.ASSINADO, comunicacao, null, null);

			// verifica se existe processo em lote para gerar o andamento e juntada
			List<ComunicacaoIncidente> listaProcessoLote = new ArrayList<ComunicacaoIncidente>();
			listaProcessoLote.addAll(verificaListaProcessoLote(comunicacao));
			
			String observacaoAndamento;
			if (comunicacao.getObsAndamento() != null){
				observacaoAndamento = comunicacao.getObsAndamento();
			}else{
				observacaoAndamento =  construirObservacaoAndamentoComunicacao(comunicacao);
			}
			
			// Prevalece que o que estiver na comunicação. Se estiver nulo, olha o modelo
			// Lança o ANDAMENTO Padrão do Documento Comunicação para o Processo e se existir para
			// os processos vinculados
			
			boolean gerarAndamento = false;
			if(comunicacao.getModeloComunicacao().getAndamento() != null){			
				gerarAndamento = true;
				if(comunicacao.getModeloComunicacao().getFlagSemAndamento() !=null && comunicacao.getModeloComunicacao().getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S)) {
					gerarAndamento = false;
				}
				if(comunicacao.getFlagSemAndamento() != null && comunicacao.getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S )) {
					gerarAndamento = false;
				}
				if(comunicacao.getFlagSemAndamento() != null && comunicacao.getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.N )) {
					gerarAndamento = true;
				}
				
				
				
			}
			
			if(gerarAndamento) {
				lancaAndamentoProcesso(dataAssinatura, usuario, setor, observacaoAndamento, comunicacao.getComunicacaoIncidentePrincipal(), listaProcessoLote, comunicacao);
			}
			
			
			
			/* if(comunicacao.getModeloComunicacao().getAndamento() != null 
								&& (comunicacao.getModeloComunicacao().getFlagSemAndamento() !=null || comunicacao.getModeloComunicacao().getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S))) {
							lancaAndamentoProcesso(dataAssinatura, usuario, setor, observacaoAndamento,
									comunicacao.getComunicacaoIncidentePrincipal(), listaProcessoLote, comunicacao);
						} */
			
			// Lança a JUNTADA do Documento Comunicação para o Processo.
			inserirDocumentoComunicacaoJuntado(documentoComunicacao, SIGLA_TIPO_DOCUMENTO_COMUNICACAO, objetoIncidente, setor, listaProcessoLote);

			// LANCA O DESLOCAMENTO
			// Verifica se existe setor de destino no modelo, se não existir utilizar o setor de
			// baixa e expedição como padrão.
			Long setorDestino = null;
			if (comunicacao.getModeloComunicacao().getSetorDestino() == null) {
				setorDestino = Setor.CODIGO_SETOR_BAIXA_EXPEDICAO;
			} else {
				setorDestino = comunicacao.getModeloComunicacao().getSetorDestino().getId();
			}

			if (setorDestino != null) {
				deslocamentoComunicacaoService.incluirDeslocamento(comunicacao, setorDestino, null);
			} else {
				throw new ServiceException(
						"Não é possível assinar o documento pois o Modelo de Comunicação não possui Setor de Destino associado. Entre em contato com a Secretaria Judiciária.");
			}
		}

	}

	@Override
	public DocumentoComunicacao salvarDocumentoComunicacaoAssinado(DocumentoComunicacao documentoComunicacao, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo, String usuario) throws ServiceException {
		if (documentoComunicacao == null || pdfAssinado == null || pdfAssinado.length == 0 || assinatura == null || assinatura.length == 0
				|| carimboTempo == null || carimboTempo.length == 0) {

			throw new IllegalArgumentException("Faltando parâmetros para salvar o documento.");
		}

		Boolean assinaturaMinistroDecisao = false;

		Comunicacao comunicacao = documentoComunicacao.getComunicacao();

		assinaturaMinistroDecisao = comunicacao.getModeloComunicacao().getFlagAssinaturaMinistro().equals(FlagGenericaModeloComunicacao.S) ? true : false;

		// salva o PDF assinado e a package gera fase para que o decisão possa assinar caso o
		// parâmetro
		// assinaturaMinistroDecisao for true
		documentoEletronicoService.salvarDocumentoEletronicoAssinadoPeloMinistroDecisao(documentoComunicacao.getDocumentoEletronico(), pdfAssinado, assinatura,
				carimboTempo, dataCarimboTempo, assinaturaMinistroDecisao);

		documentoComunicacao.setTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE);

		alterar(documentoComunicacao);

		// Gera a FASE ASSINADO
		faseComunicacaoService.incluirFase(TipoFaseComunicacao.ASSINADO, comunicacao, null, null);

		if (assinaturaMinistroDecisao) {
			faseComunicacaoService.incluirFase(TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO, comunicacao, null, null);
		} else {

			ObjetoIncidente<?> objetoIncidente = comunicacao.getObjetoIncidenteUnico();

			if (objetoIncidente != null && objetoIncidente.getId() > 0) {

				Setor setor = new Setor();
				setor = usuarioService.recuperarUsuario(usuario.toUpperCase()).getSetor();

				// verifica se existe processo em lote para gerar o andamento e juntada
				List<ComunicacaoIncidente> listaProcessoLote = new ArrayList<ComunicacaoIncidente>();
				listaProcessoLote.addAll(verificaListaProcessoLote(comunicacao));
				
				String observacaoAndamento;
				if (comunicacao.getObsAndamento() != null){
					observacaoAndamento = comunicacao.getObsAndamento();
				}else{
					observacaoAndamento =  construirObservacaoAndamentoComunicacao(comunicacao);
				}
				
				if(isPermitidoLancarAndamentoProcesso(comunicacao)){
					this.lancaAndamentoProcesso(dataCarimboTempo, usuario, setor, observacaoAndamento,
							comunicacao.getComunicacaoIncidentePrincipal(), listaProcessoLote, comunicacao);
				}
				
				// Lança a JUNTADA do Documento Comunicação para o Processo.				
				String siglaTipoDocumentoComunicacao = recuperaSiglaTipoDocumentoComunicacao(comunicacao);
				
				inserirDocumentoComunicacaoJuntado(documentoComunicacao, siglaTipoDocumentoComunicacao, objetoIncidente, setor, listaProcessoLote);
			}
		}
		return documentoComunicacao;
	}
	
	private String recuperaSiglaTipoDocumentoComunicacao(Comunicacao comunicacao){
		if(isCertidaoTransitoJulgado(comunicacao)){
			return SIGLA_CERTIDAO_TRANSITO_JULGADO;
		}else if(isContrarrazao(comunicacao)){
			return SIGLA_CONTRARRAZAO;
		}else if(isContraminuta(comunicacao)){
			return SIGLA_CONTRAMINUTA;
		}else if(isContrarazaoDecursoDePrazo(comunicacao)){
			return SIGLA_CONTRARRAZOES_DECURSO_PRAZO;
		}else if(isRespostaDecursoDePrazo(comunicacao)){
			return SIGLA_RESPOSTA_DECURSO_PRAZO;
		}
		
		return SIGLA_TIPO_DOCUMENTO_COMUNICACAO;
	}

	private boolean isPermitidoLancarAndamentoProcesso(Comunicacao comunicacao) {
		List<String> modelosRestritos = modelosComunicacaoRestritos();
		String dscModelo = descricaoDoModeloComunicacao(comunicacao);		
		/*
		boolean gerarAndamento = false;
		if(comunicacao.getModeloComunicacao().getAndamento() != null){			
			if(comunicacao.getFlagSemAndamento() == null ) {
				if(comunicacao.getModeloComunicacao().getFlagSemAndamento() !=null && comunicacao.getModeloComunicacao().getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S)) {
					gerarAndamento = true;
				}else {
					gerarAndamento = false;
				}
			}else {
				if(comunicacao.getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S)) {
					gerarAndamento = true;
				}else {
					gerarAndamento = false;
				}
			}
			
		}
		*/
		
		
		
		boolean gerarAndamento = true;
		if(comunicacao.getModeloComunicacao().getFlagSemAndamento() !=null && comunicacao.getModeloComunicacao().getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S)) {
			gerarAndamento = false;
		}
		if(comunicacao.getFlagSemAndamento() != null && comunicacao.getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S )) {
			gerarAndamento = false;
		}
		if(comunicacao.getFlagSemAndamento() != null && comunicacao.getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.N )) {
			gerarAndamento = true;
		}
		
		
		
		
		
		
		
		if(gerarAndamento) {
			return true;
		}else {
			return !(dscModelo != null && modelosRestritos.contains(dscModelo))	;
		}
		
		
		/* 	if( comunicacao.getModeloComunicacao().getFlagSemAndamento().equals(FlagGenericaModeloComunicacao.S) ) {
			return false;
		}else {
			return !(dscModelo != null && modelosRestritos.contains(dscModelo))	;
		} */
	}

	private List<String> modelosComunicacaoRestritos() {
		List<String> modelosRestritos = new ArrayList<String>();
		modelosRestritos.add(DESCRICAO_MODELO_CERTIDAO_TRANSITO_JULGADO);	
		modelosRestritos.add(DESCRICAO_MODELO_CONTRARRAZAO);
		modelosRestritos.add(DESCRICAO_MODELO_CONTRAMINUTA);
		modelosRestritos.add(DESCRICAO_CONTRARRAZOES_DECURSO_PRAZO);
		modelosRestritos.add(DESCRICAO_RESPOSTA_DECURSO_PRAZO);
		return modelosRestritos;
	}

	private boolean isCertidaoTransitoJulgado(Comunicacao comunicacao) {
		return DESCRICAO_MODELO_CERTIDAO_TRANSITO_JULGADO.equals(descricaoDoModeloComunicacao(comunicacao));
	}
	
	private boolean isContrarrazao(Comunicacao comunicacao) {
		return DESCRICAO_MODELO_CONTRARRAZAO.equalsIgnoreCase(descricaoDoModeloComunicacao(comunicacao));
	}
	
	private boolean isContraminuta(Comunicacao comunicacao) {
		return DESCRICAO_MODELO_CONTRAMINUTA.equalsIgnoreCase(descricaoDoModeloComunicacao(comunicacao));
	}
	
	private boolean isContrarazaoDecursoDePrazo(Comunicacao comunicacao) {
		return DESCRICAO_CONTRARRAZOES_DECURSO_PRAZO.equalsIgnoreCase(descricaoDoModeloComunicacao(comunicacao));
	}
	
	private boolean isRespostaDecursoDePrazo(Comunicacao comunicacao) {
		return DESCRICAO_RESPOSTA_DECURSO_PRAZO.equalsIgnoreCase(descricaoDoModeloComunicacao(comunicacao));
	}
	
	private String descricaoDoModeloComunicacao(Comunicacao comunicacao){		
		if(comunicacao.getModeloComunicacao() != null){
			return comunicacao.getModeloComunicacao().getDscModelo();
		}
		return null;
	}

	private String construirObservacaoAndamentoComunicacao(Comunicacao comunicacao) {
		ModeloComunicacao modelo = comunicacao.getModeloComunicacao();
		FlagGenericaAcessoDocumento flagAcessoDocumento = modelo.getFlagTipoAcessoDocumentoPeca();
		String observacao;

		if (flagAcessoDocumento == FlagGenericaAcessoDocumento.I) {
			observacao = null;
		} else {
			observacao = modelo.getDscModelo();
		}

		return observacao;
	}

	/**
	 * Gera andamentos para o processo principal e para os processos em lote se existirem
	 */
	private void lancaAndamentoProcesso(Date dataCarimboTempo, String usuario, Setor setor, String observacao, ComunicacaoIncidente comIncidente,
			List<ComunicacaoIncidente> listaProcessoLote, Comunicacao comunicacao) throws ServiceException {
		if(comunicacao.getModeloComunicacao().getAndamento() != null) {
			AndamentoProcesso andamentoProcesso = salvaAndamentoProcesso(dataCarimboTempo, comIncidente.getObjetoIncidente().getPrincipal(), usuario, 
					setor, observacao, comunicacao);
			atualizarComunicacaoIncidente(comIncidente, andamentoProcesso);
	
			if (CollectionUtils.isNotEmpty(listaProcessoLote)) {
				for (ComunicacaoIncidente ci : listaProcessoLote) {
					andamentoProcesso = salvaAndamentoProcesso(dataCarimboTempo, ci.getObjetoIncidente().getPrincipal(), usuario, setor, 
							observacao, comunicacao);
					atualizarComunicacaoIncidente(ci, andamentoProcesso);
				}
			}
		}
	}

	public AndamentoProcesso salvaAndamentoProcesso(Date dataCarimboTempo, ObjetoIncidente<?> objetoIncidente, String usuario, 
			Setor setor, String observacao, Comunicacao comunicacao)
			throws ServiceException {
		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		andamentoProcesso.setCodigoAndamento(comunicacao.getModeloComunicacao().getAndamento().getId());
		andamentoProcesso.setDataAndamento(dataCarimboTempo);
		andamentoProcesso.setObjetoIncidente(objetoIncidente);

		Long numSequencia = andamentoProcessoService.recuperarProximoNumeroSequencia(objetoIncidente.getPrincipal());
		andamentoProcesso.setDescricaoObservacaoAndamento(observacao);
		andamentoProcesso.setNumeroSequencia(numSequencia);
		andamentoProcesso.setCodigoUsuario(usuario.toUpperCase());
		andamentoProcesso.setSetor(setor);
		andamentoProcessoService.incluir(andamentoProcesso);

		return andamentoProcesso;
	}

	private void atualizarComunicacaoIncidente(ComunicacaoIncidente comunicacaoIncidente, AndamentoProcesso ap) {
		comunicacaoIncidente.setAndamentoProcesso(ap);

		try {
			comunicacaoIncidenteService.alterar(comunicacaoIncidente);
		} catch (ServiceException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Verifica se existe processo em lote para a comunicação
	 */
	public List<ComunicacaoIncidente> verificaListaProcessoLote(Comunicacao comunicacao) {
		List<ComunicacaoIncidente> listaProcessosLoteVinculados = Collections.emptyList();

		try {
			listaProcessosLoteVinculados = comunicacaoIncidenteService.verificaSeExisteProcessosVinculados(comunicacao);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return listaProcessosLoteVinculados;
	}

	/**
	 * Junta a comunicação ao processo principal e aos processos em lote se existirem
	 */
	public void inserirDocumentoComunicacaoJuntado(DocumentoComunicacao documentoComunicacao, String siglaTipoPecaProcesso, ObjetoIncidente<?> objetoIncidente,
			Setor setor, List<ComunicacaoIncidente> listaProcessoLote) throws ServiceException {
		TipoSituacaoPeca situacao;
		
		/*
		boolean juntarPeca = true;
		if(documentoComunicacao.getComunicacao().getFlagJuntadaPecaProc() == null){			
				if(documentoComunicacao.getComunicacao().getModeloComunicacao().getFlagJuntadaPecaProc() !=null && documentoComunicacao.getComunicacao().getModeloComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.S)) {
					juntarPeca = false;
				}else {
					juntarPeca = true;
				}
			}else {
				if(documentoComunicacao.getComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.S)) {
					juntarPeca = false;
				}else {
					juntarPeca = true;
				}
			}
			
		*/
		
		boolean juntarPeca = true;
		if(documentoComunicacao.getComunicacao().getModeloComunicacao().getFlagJuntadaPecaProc() !=null && documentoComunicacao.getComunicacao().getModeloComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.S)) {
			juntarPeca = false;
		}
		if(documentoComunicacao.getComunicacao().getFlagSemAndamento() != null && documentoComunicacao.getComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.S )) {
			juntarPeca = false;
		}
		if(documentoComunicacao.getComunicacao().getFlagSemAndamento() != null && documentoComunicacao.getComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.N )) {
			juntarPeca = true;
		}
		
		if(juntarPeca) {
			situacao = TipoSituacaoPeca.JUNTADA;
		}else{
			situacao = TipoSituacaoPeca.PENDENTE;
			setor = null;
		}
		
		/*if(documentoComunicacao.getComunicacao().getModeloComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.S)){
			situacao = TipoSituacaoPeca.PENDENTE;
			setor = null;
		}else{
			situacao = TipoSituacaoPeca.JUNTADA;
		} */
		
		PecaProcessoEletronico pecaProcessoEletronico = incluiPecaParaJuntada(siglaTipoPecaProcesso, objetoIncidente, setor, documentoComunicacao
				.getComunicacao().getDscNomeDocumento(), situacao);
		if(situacao.equals(situacao = TipoSituacaoPeca.PENDENTE)) {
			pecaProcessoEletronico.setNumeroOrdemPeca(null);
		}
		insereArquivoProcessoEletronico(pecaProcessoEletronico, documentoComunicacao.getDocumentoEletronico());
		documentoComunicacao.setTipoSituacaoDocumento(TipoSituacaoDocumento.JUNTADO);

		this.alterar(documentoComunicacao);

		if (listaProcessoLote != null && listaProcessoLote.size() > 0L) {
			for (ComunicacaoIncidente ci : listaProcessoLote) {
				PecaProcessoEletronico pecaProcessoEletronicoL = incluiPecaParaJuntada(siglaTipoPecaProcesso, ci.getObjetoIncidente(), setor,
						documentoComunicacao.getComunicacao().getDscNomeDocumento(), situacao);
				if(situacao.equals(situacao = TipoSituacaoPeca.PENDENTE)) {
					pecaProcessoEletronicoL.setNumeroOrdemPeca(null);
				}
				insereArquivoProcessoEletronico(pecaProcessoEletronicoL, documentoComunicacao.getDocumentoEletronico());
			}
		}
	}

	private PecaProcessoEletronico incluiPecaParaJuntada(String siglaTipoPecaProcesso, ObjetoIncidente<?> objetoIncidente, Setor setor, String observacao, TipoSituacaoPeca situacao)
			throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = montaPecaParaJuntada(siglaTipoPecaProcesso, objetoIncidente, setor, observacao, situacao);
		pecaProcessoEletronicoService.incluir(pecaProcessoEletronico);
		return pecaProcessoEletronico;
	}

	private PecaProcessoEletronico montaPecaParaJuntada(String siglaTipoPecaProcesso, ObjetoIncidente<?> objetoIncidente, Setor setor, String observacao, TipoSituacaoPeca situacao)
			throws ServiceException {
		PecaProcessoEletronico pecaProcessoEletronico = pecaProcessoEletronicoService.montaPecaParaJuntada(siglaTipoPecaProcesso, objetoIncidente,
				situacao, setor);
		pecaProcessoEletronico.setNumeroPagInicio(1L);
		pecaProcessoEletronico.setNumeroPagFim(1L);
		pecaProcessoEletronico.setDescricaoPeca(observacao);
		return pecaProcessoEletronico;
	}

	private void insereArquivoProcessoEletronico(PecaProcessoEletronico pecaProcessoEletronico, DocumentoEletronico documentoEletronico)
			throws ServiceException {
		ArquivoProcessoEletronico arquivoProcessoEletronico = new ArquivoProcessoEletronico();
		arquivoProcessoEletronico.setDocumentoEletronico(documentoEletronico);
		arquivoProcessoEletronico.setPecaProcessoEletronico(pecaProcessoEletronico);
		arquivoProcessoEletronico.setNumeroOrdem(1L);
		arquivoProcessoEletronicoService.salvar(arquivoProcessoEletronico);
	}

	@Override
	public Long recuperarSequencialDoDocumentoEletronico(Long idDocumentoComunicacao) throws ServiceException {
		DocumentoEletronico documentoEletronico = recuperarPorId(idDocumentoComunicacao).getDocumentoEletronico();
		if (documentoEletronico != null) {
			if (!documentoEletronico.getDescricaoStatusDocumento().equals(SIGLA_DESCRICAO_STATUS_ASSINADO)) {
				// Se existe um documento ativo e ele não foi assinado, devemos
				// substitui-lo. Dessa forma, vamos retornar o id já existente.
				return documentoEletronico.getId();
			}
		}
		// Nos outros casos, o documento deverá ser um novo DocumentoEletronico.
		// Se estiver assinado, o documento deverá ser cancelado antes.
		return documentoEletronicoService.recuperarProximaSequenceDocumentoEletronico();
	}

	@Override
	public void encaminharParaDJe(Comunicacao comunicacao, String usuario) throws ServiceException {
		Date data = null;

		try {
			data = comunicacaoIncidenteService.recuperarDataAtual();
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao recuperar data atual;", exception);
		}

		try {
			comunicacaoIncidenteService.lancarAndamento(data, comunicacao, CODIGO_ANDAMENTO_EDITAL_ENCAMINHADO_PUBLICACAO, usuario);
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao lançar andamento.", exception);
		}

		try {
			deslocamentoComunicacaoService.incluirDeslocamento(comunicacao, Setor.CODIGO_SETOR_COORDENADORIA_APOIO_TECNICO, null);
		} catch (ServiceException exception) {
			throw new ServiceException("Erro ao deslocar o documento.", exception);
		}
	}
	public void recarregarDocumentoComunicacao(DocumentoComunicacao documentoComunicacao) throws DaoException{
		((DocumentoComunicacaoDaoHibernate)dao).recarregarDocumentoComunicacao(documentoComunicacao);
	}
}
