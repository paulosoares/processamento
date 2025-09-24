package br.jus.stf.estf.decisao.mobile.assinatura.service;

import java.io.IOException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * Bean para prover a funcionalidade da Comunicação Faces Bean para o mobile.
 * 
 * @author Tomas.Godoi
 *
 */
@Name("comunicacaoFacesBeanMobile")
@Scope(ScopeType.CONVERSATION)
public class ComunicacaoMobileService {

	@Logger
	private Log logger;

	@In("#{documentoComunicacaoService}")
	private DocumentoComunicacaoService documentoComunicacaoService;

	public byte[] gerarPDFComunicacao(ComunicacaoDto dto) throws ServiceException {
		try {
			if (dto.getId() != null) {
				DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(dto.getIdDocumentoComunicacao());
				if (documentoComunicacao != null && documentoComunicacao.getDocumentoEletronico().getArquivo() != null) {
					return documentoComunicacao.getDocumentoEletronico().getArquivo();
				} else {
					throw new ServiceException("Não foi possível gerar o arquivo PDF.");
				}
			} else {
				throw new ServiceException("Nenhuma comunicação foi informada.");
			}
		} catch (ServiceException e) {
			logger.error(e, dto.getId());
			throw e;
		}
	}

	public long totalPaginasConteudo(ComunicacaoDto cDto) throws ServiceException {
		byte[] pdf = gerarPDFComunicacao(cDto);

		RandomAccessFileOrArray pdfFile = new RandomAccessFileOrArray(pdf);
		PdfReader reader;
		try {
			reader = new PdfReader(pdfFile, null);
			int total = reader.getNumberOfPages();
			reader.close();
			return total;
		} catch (IOException e) {
			throw new ServiceException("Erro ao contar a quantidade de páginas do pdf.", e);
		}
	}

}
