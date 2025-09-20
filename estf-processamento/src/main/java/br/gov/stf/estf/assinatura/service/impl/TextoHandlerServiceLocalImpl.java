package br.gov.stf.estf.assinatura.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.ODSingleXMLDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.assinatura.service.TextoHandlerServiceLocal;
import br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirTexto;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoComunicacaoService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.stfoffice.handler.HandlerException;
import br.gov.stf.stfoffice.handler.HandlerInconsistenciaException;

@Service("textoHandlerServiceLocal")
public class TextoHandlerServiceLocalImpl implements TextoHandlerServiceLocal {

	@Autowired
	private FaseComunicacaoService faseComunicacaoService;
	@Autowired
	private DocumentoComunicacaoService documentoComunicacaoService;
	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;
	@Autowired
	private ComunicacaoService comunicacaoService;
	@Autowired
	private ComunicacaoIncidenteService comunicacaoIncidenteService;
	@Autowired
	private PecaProcessoEletronicoComunicacaoService pecaProcessoEletronicoComunicacaoService;

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.assinatura.service.impl.TextoHandlerServiceLocal#gerarPDF(byte[], br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirTexto)
	 */
	@Override
	@SuppressWarnings("unused")
	@Transactional(rollbackFor = HandlerException.class)
	public void gerarPDF(byte[] pdf, RequisicaoAbrirTexto requisicao) throws HandlerException {
		DocumentoComunicacao documentoComunicacaoNaoCancelado = null;
		DocumentoComunicacao novoDocumentoTexto = new DocumentoComunicacao();
		Boolean documentoCancelado = false;
		FaseComunicacao fase = new FaseComunicacao();
		Comunicacao comunicacao = requisicao.getComunicacao();

		if (comunicacao != null) {

			FaseComunicacao faseCo = new FaseComunicacao();

			try {
				faseCo = faseComunicacaoService.pesquisarFaseAtual(comunicacao.getId());
			} catch (ServiceException e) {
				throw new HandlerException("Erro ao pesquisar a fase da comunicacao no momento da geração do PDF.");
			}

			if (faseCo != null && faseCo.equals(TipoFaseComunicacao.EXCLUIDO)) {
				throw new HandlerException("Não é possível gerar PDF de um expediente (comunicação) excluído.");
			}

			try {
				documentoComunicacaoNaoCancelado = documentoComunicacaoService.recuperarNaoCancelado(comunicacao);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

		if (documentoComunicacaoNaoCancelado != null ){
			if (!(documentoComunicacaoNaoCancelado.getTipoSituacaoDocumento().getCodigo().equals(TipoSituacaoDocumento.CANCELADO_AUTOMATICAMENTE.getCodigo())
						|| documentoComunicacaoNaoCancelado.getTipoSituacaoDocumento().getCodigo().equals(TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO.getCodigo())) 
				){
					throw new HandlerException("Erro ao gerar PDF: Já existe PDF gerado para a comunicação. Primeiro cancele o atual. Comunicacao: " + documentoComunicacaoNaoCancelado.getComunicacao().getId());			
			}
		}
		
		if (documentoComunicacaoNaoCancelado != null) {
			try {
				documentoCancelado = documentoComunicacaoService.cancelarDocumentos(documentoComunicacaoNaoCancelado,
						"Documento cancelado para a criação do um novo PDF pelo Expediente Automatizado.", requisicao
								.getUsuarioSetor().getUsername());
			} catch (ServiceException e) {
				throw new HandlerException("Erro ao cancelar o documento");
			}
		}

		if (documentoComunicacaoNaoCancelado == null && comunicacao != null) {
			try {
				DocumentoEletronico documentoEletronico = documentoEletronicoService.criaESalvaDocumentoEletronicoAssinador(pdf,
						"PROCESSAMENTO", comunicacao.getModeloComunicacao());
				novoDocumentoTexto.setComunicacao(comunicacao);
				novoDocumentoTexto.setDocumentoEletronico(documentoEletronico);
				novoDocumentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);

				documentoComunicacaoService.salvar(novoDocumentoTexto);

				// lança a fase PDF_GERADO
				String descricaoFaseAtual = null;
				
				try{
					descricaoFaseAtual = comunicacao.getObservacaoFaseAtual();
				} catch (NullPointerException e){
					//Não tem descrição, tem que ficar null
				}
				faseComunicacaoService.incluirFase(TipoFaseComunicacao.PDF_GERADO, comunicacao, descricaoFaseAtual, null);

			} catch (ServiceException e) {
				throw new HandlerException("Erro ao salvar PDF");
			}
		} else if(documentoComunicacaoNaoCancelado != null && comunicacao == null){
			throw new HandlerException("Erro ao salvar PDF: texto vazio");
		}
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.estf.assinatura.service.impl.TextoHandlerServiceLocal#salvarNovoTexto(byte[], br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirTexto)
	 */
	@Override
	@Transactional(rollbackFor = HandlerException.class)
	public void salvarNovoTexto(byte[] odt, RequisicaoAbrirTexto requisicaoNovoTexto) throws HandlerException {

		try {
			 // Verifica as inconsistências o documento estar buscando informações
			 // de sessões de outros usuários
			ODSingleXMLDocument single = geraODSingleXMLDocumentoParaProcura(odt);
			Processo proc = (Processo) requisicaoNovoTexto.getObjetoIncidenteProcurado().getPrincipal();
			
			// primeira restrição inserida para resolver o problema de duplicação das sessões do hibernate no
			//momento da abertura do stfoffice. A segunda restrição foi colocada porque os usuários 
			// estão gerando vários documentos de certidão com conteúdos diferentes sem ter o número do processo
			// no conteúdo. O processo escolhido por eles foi o Pet10000. Então quando o documento
			// não possuir o número do processo no conteúdo e não for PET, o sistema exibirá a mensagem.
			if (!single.asString().contains(proc.getNumeroProcessual().toString()) && (!proc.getSiglaClasseProcessual().equals("Pet"))) {
				if (!single.asString().replace(".", "").contains(proc.getNumeroProcessual().toString()) && (!proc.getSiglaClasseProcessual().equals("Pet"))) {
					throw new HandlerInconsistenciaException("O texto que você está tentando visualizar está em conflito com o texto de outro usuário. Favor fechar e abrir o texto novamente. ");
				}
			}
			
			
			// método responsável em salvar o Arquivo Eletronico e complementa
			// as informações do objeto incidente e comunicacao
			// salvando no formato ODT
			ComunicacaoIncidente comunicacaoIncidente = comunicacaoService.salvaArquivoEletronico(
					requisicaoNovoTexto.getCodigoModelo(), requisicaoNovoTexto.getCodigoMinistroTexto(),
					requisicaoNovoTexto.getObjetoIncidenteProcurado(), odt, "ODT");

			// seta o nome do documento
			Comunicacao comunicacao = comunicacaoIncidente.getComunicacao();
			comunicacao.setDscNomeDocumento(requisicaoNovoTexto.getNomeDoDocumento());
			// seta o setor do usuário
			comunicacao.setSetor(requisicaoNovoTexto.getUsuarioSetor().getSetor());
			// seta o usuário de criação
			comunicacao.setUsuarioCriacao(requisicaoNovoTexto.getUsuarioCriacao().toUpperCase());
			
			if (requisicaoNovoTexto.getIsNumeracaoUnica()){
				comunicacao.setNumeroComunicacao(requisicaoNovoTexto.getNumeracao());
			}
			
			if (requisicaoNovoTexto.getObsComunicacao() != null){
				comunicacao.setObsComunicacao(requisicaoNovoTexto.getObsComunicacao());
			}
			
			if (requisicaoNovoTexto.getObservacaoAndamento() != null){
				comunicacao.setObsAndamento(requisicaoNovoTexto.getObservacaoAndamento());
			}
			
			// salva o documento
			comunicacaoService.salvar(comunicacao);
			// seta o objeto incidente salvo na comunicacaoIncidente como principal
			comunicacaoIncidente.setTipoVinculo(FlagProcessoLote.P);
			// salva a tabela associativa com as informações de comunicação e
			// objeto incidente
			comunicacaoIncidenteService.salvar(comunicacaoIncidente);

			requisicaoNovoTexto.setComunicacao(comunicacao);

			// salva os processo em lote se existirem
			if (requisicaoNovoTexto.getListaProcessosObjetoIncidenteLote() != null
					&& requisicaoNovoTexto.getListaProcessosObjetoIncidenteLote().size() > 0) {
				for (ObjetoIncidente obLote : requisicaoNovoTexto.getListaProcessosObjetoIncidenteLote()) {
					ComunicacaoIncidente comIncidenteVinculado = new ComunicacaoIncidente();
					comIncidenteVinculado.setComunicacao(requisicaoNovoTexto.getComunicacao());
					comIncidenteVinculado.setTipoVinculo(FlagProcessoLote.V);
					comIncidenteVinculado.setObjetoIncidente(obLote);
					comunicacaoIncidenteService.salvar(comIncidenteVinculado);
				}
			}

			// lança a fase em elaboração
			faseComunicacaoService.incluirFase(TipoFaseComunicacao.EM_ELABORACAO, requisicaoNovoTexto.getComunicacao(),
					null, null);
			// vincula as peças ao documento recém criado. Somente entrará neste método caso a lista
			// esteja preenchida.
			if (requisicaoNovoTexto.getListaPecasVinculadasAoDocumento() != null
					&& requisicaoNovoTexto.getListaPecasVinculadasAoDocumento().size() > 0) {

				pecaProcessoEletronicoComunicacaoService.vincularPecasAoDocumento(
						requisicaoNovoTexto.getListaPecasVinculadasAoDocumento(), requisicaoNovoTexto.getComunicacao());
			}

		} catch (ServiceException e) {
			e.printStackTrace();
			throw new HandlerException("Erro ao inserir novo documento");
		}
	}
	
	
	public ODSingleXMLDocument geraODSingleXMLDocumentoParaProcura(byte[] conteudoModelo) {

		ByteArrayInputStream byteArray = new ByteArrayInputStream(conteudoModelo);

		ODPackage pacote = null;

		try {
			pacote = new ODPackage(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pacote.toSingle();
	}

}
