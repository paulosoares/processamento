package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoRelacao;
import br.gov.stf.framework.model.service.ServiceException;

public class RelacaoRepublicacaoProtocoloImpl extends ConteudoBuilder<ProtocoloPublicado>{

	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial,
			boolean ordenarProcessos, boolean pesquisarTextos)
			throws ServiceException {
		List<Conteudo> conteudos = new ArrayList<Conteudo>();
		ConteudoRelacao<ProtocoloPublicado> conteudoRelacao = new ConteudoRelacao<ProtocoloPublicado>();
		conteudoRelacao.setRelacao( empacotarItensRelacao(getRelacaoProtocoloPublicado(ordenarProcessos ,cp),indiceInicial, pesquisarTextos) );
		conteudos.add( conteudoRelacao );
		return conteudos;
	}

	@Override
	protected List<Long> pesquisarTextos(ProtocoloPublicado entidade)
			throws ServiceException {
		List<Long> textos = new ArrayList<Long>();
		
		TextoAssociadoProtocolo textoRepublicacao = getTextoRepublicacaoProtocoloPublicado (entidade);
		TextoAssociadoProtocolo textoObservacao = getTextoObservacaoProtocoloPublicado (entidade);
		
		textos.add( textoRepublicacao.getArquivoEletronico().getId() );
		textos.add( textoObservacao.getArquivoEletronico().getId() );
		return textos;
	}

	
	
}
