package br.jus.stf.estf.decisao.support.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.jus.stf.estf.decisao.DocDecisaoId;
import br.jus.stf.estf.decisao.support.security.Principal;


/**
 * Oferece métodos utilitários para manipulação do STFOFFICE.
 * 
 * @author Rodrigo Lisboa
 * @since 08.07.2010
 */
public class STFOfficeUtils extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5172690281206717595L;
	
	private static final String URI_STFOFFICE_SERVLET = "/stfOfficeServlet?uri=";
	
	protected String montarURLContextoESTFOFFICE(boolean incluirContextoAplicacao) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		StringBuffer uri = new StringBuffer();
		if (incluirContextoAplicacao) {
			uri.append(request.getContextPath());
		}
		uri.append(URI_STFOFFICE_SERVLET);
		
		return uri.toString();
	}	
	
	protected String montarURLContextoESTFOFFICE() {
		return montarURLContextoESTFOFFICE(false);
	}
	
	protected void carregarDadosGeraisDoDoc(HttpServletRequest request, Texto texto, DocDecisaoId id, Principal principal) {
		id.setNome(TextoUtils.montarNomeDoTexto(texto));
		id.setUserId(principal.getUsuario().getId().toUpperCase());
		id.setCodigoSetor(principal.getMinistro() == null ? null : principal.getMinistro().getSetor().getId());
//		id.setSessionId(recuperarSessionId(request));
		id.setObjetoIncidente(texto.getObjetoIncidente().getId());
		id.setTipoTexto(texto.getTipoTexto().getCodigo());
		id.setRodape(texto.getTipoTexto() == TipoTexto.EMENTA
				|| texto.getTipoTexto() == TipoTexto.DESPACHO
				|| texto.getTipoTexto() == TipoTexto.DECISAO_MONOCRATICA ? false : true);
	}

}
