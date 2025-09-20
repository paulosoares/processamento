package br.gov.stf.estf.documento.model.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.ExtratoAtaService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.framework.model.service.ServiceException;

@Service("extratoAtaService")
public class ExtratoAtaServiceImpl implements ExtratoAtaService {
	private final TextoService textoService;
	private final DocumentoTextoService documentoTextoService;

	public ExtratoAtaServiceImpl(TextoService textoService,

	DocumentoTextoService documentoTextoService) {
		super();
		this.textoService = textoService;
		this.documentoTextoService = documentoTextoService;
	}

	public void salvarExtratoAta(Long numeroProcessual, String siglaClasse,
			Long tipoJulgamento, Long codRecurso, Date dataAta, byte[] pdf)
			throws ServiceException {
		Texto texto = textoService.recuperarDecisaoAta(siglaClasse,
				numeroProcessual, codRecurso, tipoJulgamento, dataAta);

		// Persiste o PDF no banco de dados
		DocumentoEletronico documentoEletronico = new DocumentoEletronico();
		documentoEletronico.setArquivo(pdf);
		documentoEletronico.setDescricaoStatusDocumento("RAS");
		documentoEletronico.setSiglaSistema("ESTFSESSOES");
		documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
		documentoEletronico.setTipoArquivo(TipoArquivo.PDF);
		documentoEletronico.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_INTERNO);

		// documentoEletronicoService.incluir( documentoEletronico );

		DocumentoTexto documentoTexto = new DocumentoTexto();
		documentoTexto.setDocumentoEletronico(documentoEletronico);
		documentoTexto.setTexto(texto);
		documentoTexto.setTipoDocumentoTexto(null);

		documentoTexto.setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);

		documentoTextoService.salvar(documentoTexto);
	}

}
