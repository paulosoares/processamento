package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.util.List;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.framework.model.service.ServiceException;

/*
 * 
 * Builder "mock", nao faz nada. Deve ser utilizado quando nao e necessario
 * gerar o conteudo (conteudo desativado no builderDjProperties ou inexistente) 
 * */
@SuppressWarnings("unchecked")
public class MockImpl extends ConteudoBuilder {

	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos, boolean pesquisarTextos)
			throws ServiceException {
		return null;
	}

	@Override
	public List pesquisarTextos(ESTFBaseEntity entidade)
			throws ServiceException {
		return null;
	}

	

	

}
