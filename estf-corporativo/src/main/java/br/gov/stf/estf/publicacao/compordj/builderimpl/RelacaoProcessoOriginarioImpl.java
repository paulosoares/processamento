package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ItemRelacaoWrapper;
import br.gov.stf.framework.model.service.ServiceException;

public class RelacaoProcessoOriginarioImpl extends ConteudoBuilder<ProcessoPublicado>{

	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos, boolean pesquisarTextos)
			throws ServiceException {
		
		try {
			List<ItemRelacaoWrapper<ProcessoPublicado>> itens = empacotarItensRelacaoIguais(getRelacaoProcessoPublicado(ordernarProcessos, cp), indiceInicial, BuilderHelper.montarTextoDespachoIdentico(), pesquisarTextos);
			return recuperarCapitulosComTitulosIguais(cp, itens);
		} catch (Exception e) {
			throw new ServiceException(e);
		}	
	}

	@Override
	protected List<Long> pesquisarTextos(ProcessoPublicado entidade)
			throws ServiceException {
		List<Long> textos = new ArrayList<Long>();
		textos.add( entidade.getArquivoEletronicoTexto().getId() );
		
		// QUANDO HOUVER UMA OBSERVAÇÃO
		if ( entidade.getArquivoEletronicoObs() != null ) {
			textos.add( entidade.getArquivoEletronicoObs().getId() );
		}
		return  textos;
	}

}
