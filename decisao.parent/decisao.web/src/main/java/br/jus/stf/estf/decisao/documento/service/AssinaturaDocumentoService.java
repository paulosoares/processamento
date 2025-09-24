package br.jus.stf.estf.decisao.documento.service;

import java.util.List;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.documento.support.DocumentoNaoAssinadoDto;
import br.jus.stf.estf.decisao.mobile.assinatura.support.DocumentoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

public interface AssinaturaDocumentoService {

	List<TextoDto> recuperarTextosParaAssinar() throws ServiceException;
	
	List<TextoDto> recuperarTextosParaAssinar(List<DocumentoNaoAssinadoDto<TextoDto>> naoAssinados) throws ServiceException;
	
	List<TextoDto> recuperarTextosParaAssinar(List<Long> textos, List<DocumentoNaoAssinadoDto<TextoDto>> textosNaoAssinados) throws ServiceException;

	int recuperarTotalTextosParaAssinar();

	void assinarTextosAutomaticamente(List<TextoDto> textos) throws ServiceException;

	List<ComunicacaoDto> recuperarComunicacoesParaAssinar() throws ServiceException;
	
	List<ComunicacaoDto> recuperarComunicacoesParaAssinar(List<DocumentoNaoAssinadoDto<ComunicacaoDto>> naoAssinados) throws ServiceException;

	int recuperarTotalComunicacoesParaAssinar() throws ServiceException;

	void assinarComunicacoesAutomaticamente(List<ComunicacaoDto> comunicacoes) throws ServiceException;
	
	void assinarDocumentosAutomaticamente(List<TextoDto> textos, List<ComunicacaoDto> comunicacoes) throws ServiceException;
	
	Pesquisa buildPesquisaTextosParaAssinar(Ministro ministro);
	
	Pesquisa buildPesquisaComunicacoesParaAssinar(Ministro ministro);

	List<ComunicacaoDto> recuperarComunicacoesParaAssinar(List<Long> comunicacoes, List<DocumentoNaoAssinadoDto<ComunicacaoDto>> naoAssinados)
			throws ServiceException;

	List<DocumentoDto<TextoDto>> recuperarTextosParaAssinarMantendoNaoPermitidos() throws ServiceException;

	List<DocumentoDto<ComunicacaoDto>> recuperarComunicacoesParaAssinarMantendoNaoPermitidos() throws ServiceException;
	
	List<DocumentoDto<ComunicacaoDto>> recuperarComunicacoesParaAssinarMantendoNaoPermitidosMobile() throws ServiceException;

	List<DocumentoDto<TextoDto>> recuperarTextosParaAssinarMantendoNaoPermitidos(List<Long> textos)
			throws ServiceException;

}
