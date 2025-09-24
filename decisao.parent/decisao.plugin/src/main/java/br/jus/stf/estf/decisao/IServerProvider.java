package br.jus.stf.estf.decisao;

import org.picocontainer.injectors.Provider;

import br.jus.stf.estf.decisao.config.DecisaoParameters;
import br.jus.stf.estf.decisao.inter.IServer;
import br.jus.stf.stfoffice.bootstrap.ArgumentosRequisicao;
import br.jus.stf.stfoffice.client.remoting.RemoteServiceProxyFactory;
import br.jus.stf.stfoffice.client.remoting.RemotingException;

public class IServerProvider implements Provider { 
	public IServer provide(RemoteServiceProxyFactory factory, ArgumentosRequisicao argumentos,
			DecisaoParameters dparams) throws RemotingException {
		return factory.createProxy(dparams.getDecisaoServiceUrl(), argumentos.getCookie(),
				IServer.class, DecisaoService.class.getClassLoader());
		
	}
}

