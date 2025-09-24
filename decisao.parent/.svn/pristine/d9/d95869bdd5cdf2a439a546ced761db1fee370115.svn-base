package br.jus.stf.estf.decisao.pesquisa.web;

import java.util.EventListener;
import java.util.EventObject;

import org.ajax4jsf.event.PushEventListener;
import org.springframework.stereotype.Component;

@Component("messageRefreshController")
public class MessageRefreshController {

	private PushEventListener listenerRefresh;

	public void executarRefreshPagina () {
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
