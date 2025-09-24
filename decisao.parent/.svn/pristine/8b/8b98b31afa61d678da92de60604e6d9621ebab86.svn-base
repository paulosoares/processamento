package br.jus.stf.estf.decisao.mobile.assinatura.service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.springframework.security.context.SecurityContextHolder;

import br.gov.stf.estf.entidade.documento.ArquivoEletronicoView;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.estf.entidade.usuario.Responsavel;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
import br.jus.stf.estf.decisao.support.util.TextoUtils;

/**
 * Bean para prover a funcionalidade do Texto Faces Bean para o mobile.
 * 
 * @author Tomas.Godoi
 *
 */
@Name("textoFacesBeanMobile")
@Scope(ScopeType.CONVERSATION)
public class TextoMobileService {

	@Logger
	private Log logger;

	@In("#{textoServiceLocal}")
	private br.jus.stf.estf.decisao.texto.service.TextoService textoServiceLocal;

	public String loadConteudo(TextoDto dto) throws ServiceException {
		Texto texto = null;
		String conteudo = null;

		if (dto != null && dto.getId() != null) {
			texto = textoServiceLocal.recuperarTextoPorId(dto.getId());
			String rtf = null;
			if (texto != null) {
				dto = TextoDto.valueOf(texto, true);

				// Carregar os dados de criação/alteração do arquivo eletronico
				ArquivoEletronicoView vwArquivoEletronico = textoServiceLocal.recuperarArquivoEletronicoViewPeloId(texto.getArquivoEletronico().getId());
				if (vwArquivoEletronico != null) {
					try {
						rtf = new String(texto.getArquivoEletronico().getConteudo(), "ISO-8859-1");
					} catch (UnsupportedEncodingException e) {
						throw new NestedRuntimeException(e);
					}
					conteudo = TextoUtils.convertRtfToHtml(rtf);
				}
			} else {
				dto = null;
			}
		}

		textoServiceLocal.validaAcessoTextosRestritos(getPrincipal(), Arrays.asList(dto));
		return conteudo;
	}

	public void suspenderLiberacao(TextoDto texto) throws ServiceException {
		try {
			Set<Long> textosProcessados = new HashSet<Long>();
			textoServiceLocal.alterarFase(texto, getDestinoSuspenderLiberacao(), textosProcessados, getObservacaoSuspenderLiberacao(),
					getResponsavelSuspenderLiberacao());
		} catch (Exception e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	private TipoTransicaoFaseTexto getDestinoSuspenderLiberacao() {
		return TipoTransicaoFaseTexto.SUSPENDER_LIBERACAO;
	}

	/**
	 * Vazio por padrão. Não vai atualizar.
	 * 
	 * @return
	 */
	private Responsavel getResponsavelSuspenderLiberacao() {
		return null;
	}

	/**
	 * Vazio por padrão.
	 * 
	 * @return
	 */
	private String getObservacaoSuspenderLiberacao() {
		return "";
	}

	private Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
