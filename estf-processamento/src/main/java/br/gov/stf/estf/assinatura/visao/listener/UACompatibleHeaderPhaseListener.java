package br.gov.stf.estf.assinatura.visao.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 * Listener que renderiza as páginas de modo que force o browser a rodar em modo de compatibilidade.
 * 
 * @author thiago.miranda
 * @since 3.17.4
 */
public class UACompatibleHeaderPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		final FacesContext facesContext = event.getFacesContext();
		final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addHeader("X-UA-Compatible", "IE=EmulateIE7");
	}

	@Override
	public void afterPhase(PhaseEvent event) {

	}
}