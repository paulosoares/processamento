package br.gov.stf.estf.documento.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoTextoPeticaoDao;
import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoPeticaoService;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("documentoTextoPeticaoService")
public class DocumentoTextoPeticaoServiceImpl extends GenericServiceImpl<DocumentoTextoPeticao, Long, DocumentoTextoPeticaoDao> 
	implements DocumentoTextoPeticaoService {

	
	private  final DocumentoEletronicoService documentoEletronicoService;
	private  final AssinaturaDigitalService assinaturaDigitalService;
	
	public DocumentoTextoPeticaoServiceImpl(DocumentoTextoPeticaoDao dao, 	
			DocumentoEletronicoService documentoEletronicoService,
			AssinaturaDigitalService assinaturaDigitalService) {
		super(dao);
		this.documentoEletronicoService = documentoEletronicoService;
		this.assinaturaDigitalService = assinaturaDigitalService;
	}

	public List<DocumentoTextoPeticao> pesquisarDocumentosSetor(Setor setor, TipoSituacaoDocumento tipoSituacaoDocumento, Date dataInicio, Date dataFim, Short anoProtocolo, Long numeroProtocolo) 
			throws ServiceException {
		try {
			return dao.pesquisarDocumentosSetor(setor, tipoSituacaoDocumento, dataInicio, dataFim, anoProtocolo, numeroProtocolo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Boolean cancelarDocumentos(List<DocumentoTextoPeticao> listaDocumentoTextoPeticao, String motivoCancelamento) 
			throws ServiceException {		
		try{
						
			for( DocumentoTextoPeticao documentoTextoProtocolo : listaDocumentoTextoPeticao ){								
				documentoTextoProtocolo.setTipoSituacaoDocumento(TipoSituacaoDocumento.CANCELADO_PELO_MINISTRO );
				dao.salvar( documentoTextoProtocolo );
				documentoEletronicoService.cancelarDocumento( documentoTextoProtocolo.getDocumentoEletronico(), motivoCancelamento );								
			}			
			
		}catch (DaoException e) {
			throw new ServiceException(e);
		}
		
		return true;
		
	}
	
	public DocumentoTextoPeticao salvarDocumentoTextoPeticaoAssinado (DocumentoTextoPeticao documentoTextoPeticao, byte[] pdfAssinado, byte[] assinatura, byte[] carimboTempo,  Date dataCarimboTempo)
			throws ServiceException {
		
		if (documentoTextoPeticao == null || pdfAssinado == null || pdfAssinado.length == 0 || assinatura == null
				|| assinatura.length == 0 || carimboTempo == null || carimboTempo.length == 0) {

			throw new IllegalArgumentException("Faltando parâmetros para salvar o documento.");
		}

		documentoEletronicoService.salvarDocumentoEletronicoAssinado(documentoTextoPeticao.getDocumentoEletronico(), pdfAssinado,
				assinatura, carimboTempo, dataCarimboTempo);

		
		documentoTextoPeticao.setTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE);

		alterar(documentoTextoPeticao);

		return documentoTextoPeticao;
	}		
	
	public DocumentoTextoPeticao recuperar(DocumentoEletronico documentoEletronico)
			throws ServiceException {
		try {
			return dao.recuperar(documentoEletronico);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}		
	


}
