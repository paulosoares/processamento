package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoRelacao;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoTexto;
import br.gov.stf.framework.model.service.ServiceException;

public class RelacaoProcessoDistribuidoPresidenteImpl extends ConteudoBuilder<IncidenteDistribuicao> {


	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos, boolean pesquisarTextos)
			throws ServiceException {
		List<Conteudo> conteudos = new ArrayList<Conteudo>();
		
		// TEXTO DE ABERTURA DA DISTRIBUICAO
		ConteudoTexto conteudoTextoAberturaDistribuicao = new ConteudoTexto();
		byte[] textoAberturaDistribuicao = getTextoAberturaDistribuicaoPresidente(cp);		
		conteudoTextoAberturaDistribuicao.setTexto(textoAberturaDistribuicao);
		conteudoTextoAberturaDistribuicao.setAlinhamentoTexto( ConteudoTexto.ALINHAMENTO_JUSTIFICADO );
		conteudos.add( conteudoTextoAberturaDistribuicao );		
		
		// RELACAO DE PROCESSOS DISTRIBUIDOS
		ConteudoRelacao<IncidenteDistribuicao> conteudoRelacao = new ConteudoRelacao<IncidenteDistribuicao>();
		conteudoRelacao.setRelacao( empacotarItensRelacao(getRelacaoDistribuicao(ordernarProcessos, cp), indiceInicial, pesquisarTextos) );
		conteudos.add( conteudoRelacao );
		
		return conteudos;
	}

	@Override
	protected List<byte[]> pesquisarTextos(IncidenteDistribuicao entidade)
			throws ServiceException {
		String tipoDistribuicaoDj = entidade.getTipoDistribuicaoDJ();
		if ( tipoDistribuicaoDj!=null ) {
			try {
				return Arrays.asList( BuilderHelper.stringToRtf(tipoDistribuicaoDj) );
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
		return null;
	}

	

}
