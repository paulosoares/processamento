package br.jus.stf.estf.decisao.documento.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.jus.stf.estf.decisao.documento.service.PermissaoAssinaturaDocumentoService;
import br.jus.stf.estf.decisao.documento.support.ValidacaoPermissaoAssinaturaDocumento;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;

@Service("permissaoAssinaturaComunicacaoService")
public class PermissaoAssinaturaComunicacaoServiceImpl implements PermissaoAssinaturaDocumentoService<ComunicacaoDto> {

	/**
	 * Qualquer comunicação pode ser assinada.
	 * 
	 */
	@Override
	public ValidacaoPermissaoAssinaturaDocumento<ComunicacaoDto> documentoPodeSerAssinado(ComunicacaoDto comunicacaoDto) {
		return ValidacaoPermissaoAssinaturaDocumento.podeAssinar(comunicacaoDto);
	}

	@Override
	public List<ValidacaoPermissaoAssinaturaDocumento<ComunicacaoDto>> documentosPodemSerAssinados(
			List<ComunicacaoDto> documentos) {
		List<ValidacaoPermissaoAssinaturaDocumento<ComunicacaoDto>> resultado = 
				new ArrayList<ValidacaoPermissaoAssinaturaDocumento<ComunicacaoDto>>();
		for (ComunicacaoDto documento : documentos){
			resultado.add(ValidacaoPermissaoAssinaturaDocumento.podeAssinar(documento));
		}
		return resultado;
	}
	
	

}
