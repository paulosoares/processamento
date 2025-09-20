package br.gov.stf.estf.assinatura.visao.util;

import java.util.EventListener;
import java.util.EventObject;

import org.ajax4jsf.event.PushEventListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.jus.stf.assinadorweb.api.util.PageRefresher;

@Scope("session")
@Component("refreshController")
public class RefreshController implements PageRefresher{

	private PushEventListener listenerRefresh;
	

	public void executarRefreshPagina() {
		refreshPagina();
	}
	
	private void refreshPagina() {
		if ( listenerRefresh!=null ) {
			listenerRefresh.onEvent(new EventObject(this));
		}
	}
	
	public void adicionarListenerRefresh(EventListener listener) {
        synchronized (listener) {
            if (this.listenerRefresh != listener) {
                this.listenerRefresh = (PushEventListener) listener;
            }
        }
    }
	
}
