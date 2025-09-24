package br.jus.stf.estf.decisao.objetoincidente.web.support;

import java.net.URLEncoder;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.jus.stf.estf.decisao.DocNovaDecisaoId;

public class STFOfficeUriBuilder {
	private final Texto texto;
	private final Usuario usuario;
	private final Setor setor;
	private final FacesContext facesContext;

	public STFOfficeUriBuilder(Texto texto, Usuario usuario, Setor setor, FacesContext facesContext) {
		this.texto = texto;
		this.usuario = usuario;
		this.setor = setor;
		this.facesContext = facesContext;
	}

	public String getURI() throws ErroMontagemUriException {
		try {
			DocNovaDecisaoId id = new DocNovaDecisaoId();
			id.setObjetoIncidente(texto.getObjetoIncidente().getId());
			id.setResponsavel(texto.getResponsavel().getId().toString());
			id.setObservacao(texto.getObservacao());
			id.setTipoTexto(texto.getTipoTexto().getCodigo());
			id.setCodigoSetor(setor.getId());
			id.setRodape(isPermiteRodape(texto));
			StringBuffer nome = montaNomeDoTexto(texto);
			id.setNome(nome.toString());
			id.setUserId(usuario.getId());
			
			if (texto.getMinistroDivergente() != null)
				id.setMinistroDivergente(texto.getMinistroDivergente().getId());
			
			if (texto.getTipoVoto() != null)
				id.setTipoVotoId(String.valueOf(texto.getTipoVoto().getId()));
			
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
//			id.setSessionId(recuperarSessionId(request));
			String uri = URLEncoder.encode(id.toURI(), "utf-8");
			if (uri != null && uri.trim().length() > 0)
				return "/stfOfficeServlet?uri=" + uri;
			return uri;
		} catch (Exception e) {
			throw new ErroMontagemUriException("Ocorreu um erro ao montar a URI do STFOffice", e);
		}

	}

	/**
	 * Recupera o identificador da sessão
	 * 
	 * @param request
	 * @return
	 */
	public static String recuperarSessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("JSESSIONID")) {
					return cookies[i].getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Método que verifica se o texto permite rodapé ou não. Os documentos que não permitem são
	 * o despacho e a ementa.
	 * @return
	 */
	private boolean isPermiteRodape(Texto texto) {
		return !(texto.getTipoTexto() == TipoTexto.EMENTA || texto.getTipoTexto() == TipoTexto.DESPACHO || texto.getTipoTexto() == TipoTexto.DECISAO_MONOCRATICA);
	}

	private StringBuffer montaNomeDoTexto(Texto texto) {
		StringBuffer nome = new StringBuffer();
		nome.append(texto.getIdentificacaoCompleta());
		if (texto.getObservacao() != null) {
			nome.append(" - ");
			nome.append(texto.getObservacao());
		}
		return nome;
	}

}
