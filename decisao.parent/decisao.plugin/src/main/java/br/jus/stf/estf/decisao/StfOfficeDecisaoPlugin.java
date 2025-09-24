package br.jus.stf.estf.decisao;

import static org.picocontainer.Characteristics.CACHE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.ProviderAdapter;

import br.jus.stf.estf.decisao.config.DecisaoParameters;
import br.jus.stf.estf.decisao.handlers.AbrirDocumentoHandler;
import br.jus.stf.stfoffice.ContainerIntializationException;
import br.jus.stf.stfoffice.PluginActionHandler;
import br.jus.stf.stfoffice.StfOfficePlugin;
import br.jus.stf.stfoffice.bootstrap.ArgumentosRequisicao;
import br.jus.stf.stfoffice.client.ui.menu.MenuTransformations;

public class StfOfficeDecisaoPlugin implements StfOfficePlugin {
	private static final Log log = LogFactory.getLog(StfOfficeDecisaoPlugin.class);

	public StfOfficeDecisaoPlugin() {
	
	}

	public String getName() {
		return "decisao";
	}

	public void start() {
		// TODO Auto-generated method stub
	}

	public Map<String, PluginActionHandler> getActionHandlerMap() {
		Map<String, PluginActionHandler> m = new HashMap<String, PluginActionHandler>();
		AbrirDocumentoHandler abrirDocumentoHandler = new AbrirDocumentoHandler();
		m.put(DecisaoActions.ACAO_ABRIR_DOCUMENTO, abrirDocumentoHandler);
		m.put(DecisaoActions.ACAO_NOVO_DOCUMENTO, abrirDocumentoHandler);
		m.put(DecisaoActions.ACAO_SALVAR_DOCUMENTO, abrirDocumentoHandler);
		m.put(DecisaoActions.ACAO_FECHAR_DOCUMENTO, abrirDocumentoHandler);
		m.put(DecisaoActions.ACAO_GERAR_PDF, abrirDocumentoHandler);
		m.put(DecisaoActions.ACAO_RECUPERAR_VERSOES_DOCUMENTO, abrirDocumentoHandler);
		m.put(DecisaoActions.ACAO_MANTER_SESSAO_USUARIO, abrirDocumentoHandler);

		return m;
	}

	public void initRequestContainer(MutablePicoContainer container) throws ContainerIntializationException {
		container.as(CACHE).addComponent(DecisaoParameters.class);
		MenuTransformations mt = new MenuTransformations(StfOfficeDecisaoPlugin.class);
		mt.addFromClasspath("transform-menu-decisao.xml");
		container.as(CACHE).addComponent(mt);
		String uriRequisicao = container.getComponent(ArgumentosRequisicao.class).getUriRequisicao();
		try {
			URI uri = new URI(uriRequisicao);
			container.addComponent(new StfOfficeDecisaoURI(uri));
		} catch (URISyntaxException e) {
			throw new ContainerIntializationException("Erro em uri de requisicao!: " + uriRequisicao, e);
		}
		container.as(CACHE).addAdapter(new ProviderAdapter(new IServerProvider()));
		container.as(CACHE).addComponent(DecisaoService.class);
	}

}
