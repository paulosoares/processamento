package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.BadLocationException;

import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ItemRelacaoWrapper;
import br.gov.stf.framework.model.service.ServiceException;

public class RelacaoProtocoloImpl extends ConteudoBuilder<TextoPeticao> {

	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos, boolean pesquisarTextos)
			throws ServiceException {
		
		try {
			List<ItemRelacaoWrapper<TextoPeticao>> itens = empacotarItensRelacaoIguais(getRelacaoTextoProtocolo(ordernarProcessos, cp),indiceInicial, null, pesquisarTextos );
			return recuperarCapitulosComTitulosIguais(cp, itens);
		} catch (Exception e) {
			throw new ServiceException(e);
		}	
	}

	@Override
	protected List<Long> pesquisarTextos(TextoPeticao entidade)
			throws ServiceException {
		return Arrays.asList( entidade.getArquivoEletronico().getId() );
	}
	
	@Override
	protected int adicionarTextosIdenticos(
			List<ItemRelacaoWrapper<TextoPeticao>> itens, int comeco,
			int indiceInicial, Long seq,
			List<ItemRelacaoWrapper<TextoPeticao>> itensFinal,
			byte[] textoSubstituto) throws IOException, BadLocationException {
		
		
		int ii = indiceInicial;
		for ( int i=comeco ; i<itens.size() ; i++ ) {
			ItemRelacaoWrapper<TextoPeticao> itemRelacao = itens.get(i);
			Long seqArquivo = (Long) itemRelacao.getTextos().get(0);			
			
			if ( seqArquivo.equals( seq ) ) {
				itemRelacao.setIndice( ii++ );
				itemRelacao.setTextos( Arrays.asList( montarTextoSubstituto ( itemRelacao.getWrappedObject() , indiceInicial-1) ));
				itensFinal.add( itemRelacao );
				itens.remove(i--);
			}
		}		
		return ii;
	}
	
	private byte[] montarTextoSubstituto (TextoPeticao textoPeticao, int indice) throws IOException, BadLocationException {
		byte[] texto = null;
		if ( TextoPeticao.TIPO_TEXTO_DECISAO.equals( textoPeticao.getTipoTexto() ) ) {
			texto = BuilderHelper.montarTextoDecisaoIdentico();
		} else {
			texto = BuilderHelper.montarTextoDespachoIdentico();
		}
		
		return BuilderHelper.apendarRtfTextoIgual(texto, indice);
	}

}
