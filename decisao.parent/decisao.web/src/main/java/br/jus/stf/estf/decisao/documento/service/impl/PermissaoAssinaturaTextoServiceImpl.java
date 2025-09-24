package br.jus.stf.estf.decisao.documento.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.documento.service.PermissaoAssinaturaDocumentoService;
import br.jus.stf.estf.decisao.documento.support.ValidacaoPermissaoAssinaturaDocumento;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.jus.stf.estf.decisao.texto.support.TextoBloqueadoException;

@Service("permissaoAssinaturaTextoService")
public class PermissaoAssinaturaTextoServiceImpl implements PermissaoAssinaturaDocumentoService<TextoDto> {

	@Autowired
	private TextoService textoService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	/**
	 * Aplica as regras de neg�cio para verificar se um texto pode ser assinado.
	 * 
	 */
	@Override
	public ValidacaoPermissaoAssinaturaDocumento<TextoDto> documentoPodeSerAssinado(TextoDto textoDto) {
		Texto texto = textoService.recuperarTextoPorId(textoDto.getId());
		try {
			textoService.verificaTextoBloqueado(texto);
			if (texto.getTipoTexto().equals(TipoTexto.ACORDAO)) {
				Texto ementa = verificaEmentaHabilitadaParaAssinatura(texto);
				if (ementa.getTipoFaseTextoDocumento().equals(FaseTexto.LIBERADO_ASSINATURA)) {
					return buildValidoParaAssinar(textoDto, TextoDto.valueOf(ementa));
				}
			} else if (texto.getTipoTexto().equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL)) {
				Texto ementaRepGeral = verificaEmentaRepercussaoGeralHabilitadaParaAssinatura(texto);
				if (ementaRepGeral.getTipoFaseTextoDocumento().equals(FaseTexto.LIBERADO_ASSINATURA)) {
					return buildValidoParaAssinar(textoDto, TextoDto.valueOf(ementaRepGeral));
				}
			}
			return buildValidoParaAssinar(textoDto);
		} catch (ServiceException e) {
			return buildNaoValidoParaAssinar(texto, e.getMessage());
		} catch (TextoBloqueadoException e) {
			return buildNaoValidoParaAssinar(texto, e.getMessage());
		}
	}

	@Override
	public List<ValidacaoPermissaoAssinaturaDocumento<TextoDto>> documentosPodemSerAssinados(
			List<TextoDto> documentos) throws ServiceException{
		try{
			//List<Texto> textos = textoService.recuperarListaTextos(documentos);
			List<ValidacaoPermissaoAssinaturaDocumento<TextoDto>> listaValidacoes = new ArrayList<ValidacaoPermissaoAssinaturaDocumento<TextoDto>>();
			for (TextoDto textoDto : documentos){				
				try {
					TextoDto deveSerAssinadoJunto = null;
					textoService.verificaTextoBloqueado(textoDto);
					if (textoDto.getTipoTexto().equals(TipoTexto.ACORDAO)) {						
						Texto ementa = verificaEmentaHabilitadaParaAssinatura(textoDto);
						if (ementa.getTipoFaseTextoDocumento().equals(FaseTexto.LIBERADO_ASSINATURA)) {
							deveSerAssinadoJunto = TextoDto.valueOf(ementa);
						}
					} else if (textoDto.getTipoTexto().equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL)) {
						Texto ementaRepGeral = verificaEmentaRepercussaoGeralHabilitadaParaAssinatura(textoService.recuperarTextoPorId(textoDto.getId()));
						if (ementaRepGeral.getTipoFaseTextoDocumento().equals(FaseTexto.LIBERADO_ASSINATURA)) {
							deveSerAssinadoJunto = TextoDto.valueOf(ementaRepGeral);
						}
					}
					if (deveSerAssinadoJunto != null) {
						listaValidacoes.add(buildValidoParaAssinar(textoDto, deveSerAssinadoJunto));
					} else {
						listaValidacoes.add(buildValidoParaAssinar(textoDto));
					}
				} catch (ServiceException e) {
					listaValidacoes.add(buildNaoValidoParaAssinar(textoService.recuperarTextoPorId(textoDto.getId()), e.getMessage()));
				} catch (TextoBloqueadoException e) {
					listaValidacoes.add(buildNaoValidoParaAssinar(textoService.recuperarTextoPorId(textoDto.getId()), e.getMessage()));
				}
			}						
			
			return listaValidacoes;
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	private ValidacaoPermissaoAssinaturaDocumento<TextoDto> buildNaoValidoParaAssinar(Texto texto, String motivo) {
		return ValidacaoPermissaoAssinaturaDocumento.naoPodeAssinar(TextoDto.valueOf(texto), motivo, texto.getIdentificacaoCompleta());
	}
	
	private ValidacaoPermissaoAssinaturaDocumento<TextoDto> buildValidoParaAssinar(TextoDto textoDto) {
		return ValidacaoPermissaoAssinaturaDocumento.podeAssinar(textoDto);
	}

	private ValidacaoPermissaoAssinaturaDocumento<TextoDto> buildValidoParaAssinar(TextoDto textoDto,TextoDto deveAssinarJunto) {
		return ValidacaoPermissaoAssinaturaDocumento.podeAssinar(textoDto, Arrays.asList(deveAssinarJunto));
	}
	
	/**
	 * Verifica se o ac�rd�o possui uma ementa gerada, e se ela est� em uma fase maior ou igual a Liberado Para Assinatura.
	 * 
	 * @param texto
	 * @return
	 * @throws ServiceException
	 */
	private Texto verificaEmentaHabilitadaParaAssinatura(Texto texto) throws ServiceException {
		Texto ementa = textoService.recuperarEmenta(texto, usuarioLogadoService.getMinistro());
		if (ementa == null) {
			throw new ServiceException("O ac�rd�o n�o poder� ser assinado pois n�o existe ementa gerada para o processo!");
		}
		if (isFaseMenorLiberadoParaAssinatura(ementa.getTipoFaseTextoDocumento())) {
			throw new ServiceException("O acord�o n�o poder� ser assinado pois a ementa n�o foi liberada para assinatura!");
		}
		return ementa;
	}
	
	private Texto verificaEmentaHabilitadaParaAssinatura(TextoDto textoDto) throws ServiceException {
		Texto ementa = textoService.recuperarEmenta(textoDto, usuarioLogadoService.getMinistro());
		if (ementa == null) {
			throw new ServiceException("O ac�rd�o n�o poder� ser assinado pois n�o existe ementa gerada para o processo!");
		}
		if (isFaseMenorLiberadoParaAssinatura(ementa.getTipoFaseTextoDocumento())) {
			throw new ServiceException("O acord�o n�o poder� ser assinado pois a ementa n�o foi liberada para assinatura!");
		}
		return ementa;
	}

	/**
	 * Verifica se a decis�o sobre repercuss�o geral possui uma ementa sobre repercuss�o geral gerada, e se ela est� em uma fase maior ou igual a Liberado Para
	 * Assinatura.
	 * 
	 * @param texto
	 * @return
	 * @throws ServiceException
	 */
	protected Texto verificaEmentaRepercussaoGeralHabilitadaParaAssinatura(Texto texto) throws ServiceException {
		Texto ementaRepGeral = textoService.recuperarEmentaRepercussaoGeral(texto, usuarioLogadoService.getMinistro());
		if (ementaRepGeral == null) {
			throw new ServiceException(
					"A decis�o sobre repercuss�o geral n�o poder� ser assinada pois n�o existe ementa sobre repercuss�o gerada para o processo!");
		}
		if (isFaseMenorLiberadoParaAssinatura(ementaRepGeral.getTipoFaseTextoDocumento())) {
			throw new ServiceException(
					"A decis�o sobre repercuss�o geral n�o poder� ser assinada pois a ementa sobre repercuss�o n�o foi liberada para assinatura!");
		}
		return ementaRepGeral;
	}

	private boolean isFaseMenorLiberadoParaAssinatura(FaseTexto fase) {
		return fase.compareTo(FaseTexto.LIBERADO_ASSINATURA) < 0;
	}

}
