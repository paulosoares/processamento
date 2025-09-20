package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoRelacao;
import br.gov.stf.framework.model.service.ServiceException;

public class RelacaoProcessoDistribuidoRepublicadoObservacaoImpl extends ConteudoBuilder<ProcessoPublicado> {

	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos, boolean pesquisarTextos)
			throws ServiceException {
		List<Conteudo> conteudos = new ArrayList<Conteudo>();
		ConteudoRelacao<ProcessoPublicado> conteudoRelacao = new ConteudoRelacao<ProcessoPublicado>();
		conteudoRelacao.setRelacao( empacotarItensRelacao(getRelacaoProcessoPublicado(ordernarProcessos, cp), indiceInicial, pesquisarTextos) );
		conteudos.add( conteudoRelacao );
		return conteudos;
	}

	@Override
	protected List<Long> pesquisarTextos(ProcessoPublicado entidade)
			throws ServiceException {
		return Arrays.asList( entidade.getArquivoEletronicoObs().getId() );
	}

}
