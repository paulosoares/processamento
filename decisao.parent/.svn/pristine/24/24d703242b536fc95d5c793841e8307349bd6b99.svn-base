package br.jus.stf.estf.decisao.pesquisa.web;

import java.util.EventListener;
import java.util.EventObject;

import org.ajax4jsf.event.PushEventListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.jus.stf.assinadorweb.api.util.PageRefresher;

@Scope("session")
@Component("refreshController")
public class RefreshController implements PageRefresher {

	private PushEventListener listenerRefresh;

	private Long textoId;

	public void executarRefreshPagina() {
		textoId = null;
		refreshPagina();
	}

	private void refreshPagina() {
		if (listenerRefresh != null) {
			listenerRefresh.onEvent(new EventObject(this));
		}
	}

	public void executarRefreshPagina(Long id) {
		textoId = id;
		refreshPagina();
	}

	public void adicionarListenerRefresh(EventListener listener) {
		synchronized (listener) {
			if (this.listenerRefresh != listener) {
				this.listenerRefresh = (PushEventListener) listener;
			}
		}
	}

	public Long getTextoId() {
		return textoId;
	}

}
