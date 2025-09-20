package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.BadLocationException;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoRelacao;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoTexto;
import br.gov.stf.estf.publicacao.compordj.modelo.ItemRelacaoWrapper;
import br.gov.stf.estf.publicacao.compordj.util.ChaveMinistro;
import br.gov.stf.framework.model.service.ServiceException;

@SuppressWarnings("unchecked")
public class RelacaoProcessoPublicadoImpl extends ConteudoBuilder<ProcessoPublicado> {
	
	private static final ChaveMinistro NOME_MINISTRO_VAZIO = new ChaveMinistro("MIN",0L,false);
	private ConteudoPublicacao conteudoPublicacao;
	
	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos,boolean pesquisarTextos)
			throws ServiceException {
		this.conteudoPublicacao = cp;
		try {
			
			List<Conteudo> conteudos = new ArrayList<Conteudo>();
			conteudos.add( new ConteudoTexto(BuilderHelper.stringToRtf("JULGAMENTOS"), ConteudoTexto.ALINHAMENTO_CENTRALIZADO) );
			
			if ( pesquisarTextos ) {
			
				Map<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>> mapaRelatorDespachoIdentico = empacotarItensDespachoIguais(getRelacaoProcessoPublicado(ordernarProcessos, cp), indiceInicial, BuilderHelper.montarTextoDecisaoIdentico(), pesquisarTextos);
				
				// MONTA A LISTA DE CONTEUDOS E SEUS RESPECTIVOS TITULOS DE ABERTURA A PARTIR DO MAPA JÁ MONTADO E ORDENADO
				Set<ChaveMinistro> chaves = mapaRelatorDespachoIdentico.keySet();
				List<ChaveMinistro> listaChaves = new ArrayList<ChaveMinistro>(chaves);
				Collections.sort(listaChaves);
				
				for ( ChaveMinistro key: listaChaves ) {
					List<ItemRelacaoWrapper<ProcessoPublicado>> itensRelacao = mapaRelatorDespachoIdentico.get(key);
					if ( !NOME_MINISTRO_VAZIO.equals( key ) ) {
						ConteudoTexto conteudoTexto = new ConteudoTexto();					
						conteudoTexto.setTexto( BuilderHelper.montarTextoInicialDecisoesIdenticas( key.getNomeMinistro(), key.isFemea() ) );
						conteudoTexto.setAlinhamentoTexto( ConteudoTexto.ALINHAMENTO_ESQUERDA );
						conteudos.add( conteudoTexto );
						
					}
					ConteudoRelacao<ProcessoPublicado> conteudosRelacao = new ConteudoRelacao<ProcessoPublicado>();
					conteudosRelacao.setRelacao( itensRelacao );
					conteudos.add( conteudosRelacao );
				}
				
			} else {
				ConteudoRelacao<ProcessoPublicado> conteudoRelacao = new ConteudoRelacao<ProcessoPublicado>();
				conteudoRelacao.setRelacao( empacotarItensRelacao(getRelacaoProcessoPublicado(ordernarProcessos, cp), indiceInicial,  pesquisarTextos) );
				conteudos.add( conteudoRelacao );
			}
				
				
			return conteudos;			
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	//OK
	private List<ItemRelacaoWrapper<ProcessoPublicado>> ordenarItensDespachoIguais (List<ProcessoPublicado> relacao, int indiceInicial, boolean pesquisarTextos) throws ServiceException, IOException, BadLocationException {
		// EMPACOTA PREVIAMENTE OS ITENS, MENOS O INDICE
		List<ItemRelacaoWrapper<ProcessoPublicado>> itens = empacotarItensRelacao(relacao, indiceInicial, pesquisarTextos);
		
		Map<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>> mapaRelatorDespachoIdentico = new HashMap<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>>();
		while ( itens.size()>0 ) {
			ItemRelacaoWrapper<ProcessoPublicado> itemRelacao = itens.get(0);
			
		
			Long seqArquivo = null;
			
			if ( itemRelacao.getTextos().size()==1 ) {
				Texto texto = (Texto) itemRelacao.getTextos().get(0);
				if ( texto.getTextosIguais() ) {
					seqArquivo = texto.getArquivoEletronico().getId();
				}
			}
			
			ordenarTextosIdenticos(itemRelacao, seqArquivo, mapaRelatorDespachoIdentico, itens);
			
		}
		
		Set<ChaveMinistro> chaves = mapaRelatorDespachoIdentico.keySet();
		List<ChaveMinistro> listaChaves = new ArrayList<ChaveMinistro>(chaves);
		Collections.sort(listaChaves);
		List<ItemRelacaoWrapper<ProcessoPublicado>> itensFinal = new ArrayList<ItemRelacaoWrapper<ProcessoPublicado>>();
		
		for ( ChaveMinistro key: listaChaves ) {
			itensFinal.addAll( mapaRelatorDespachoIdentico.get(key) );
		}
		
		return itensFinal;
	}
	
	// OK
	private void ordenarTextosIdenticos (ItemRelacaoWrapper<ProcessoPublicado> itemAtual, Long seq, Map<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>> mapaRelatorDespachoIdentico, List<ItemRelacaoWrapper<ProcessoPublicado>> itens) throws IOException, BadLocationException, ServiceException {
		List<ItemRelacaoWrapper<ProcessoPublicado>> itensIdenticos = new ArrayList<ItemRelacaoWrapper<ProcessoPublicado>>();
		itensIdenticos.add( itemAtual );
		itens.remove(0);
		for ( int i=0 ; i<itens.size() ; i++ ) {
			ItemRelacaoWrapper<ProcessoPublicado> itemRelacao = itens.get(i);
			
			Long seqArquivo = null;
			
			if ( itemRelacao.getTextos().size()==1 ) {
				Texto texto = (Texto) itemRelacao.getTextos().get(0);
				if ( texto.getTextosIguais() ) {
					seqArquivo = texto.getArquivoEletronico().getId();
				}
			}
			
			if ( seqArquivo!=null && seqArquivo.equals( seq ) ) {
				itensIdenticos.add( itemRelacao );
				itens.remove(i--);
			}
		}
		
		if ( itensIdenticos.size()==1 ) {
			adicionarListaMapa(NOME_MINISTRO_VAZIO, itensIdenticos, mapaRelatorDespachoIdentico);
		} else {
			Ministro relator = getMinistroRelator((Processo) itemAtual.getWrappedObject().getObjetoIncidente().getPrincipal());
			adicionarListaMapa(relator, itensIdenticos, mapaRelatorDespachoIdentico);
		}
	}
	
	/**
	 * monta o mapa
	 * @param relacao
	 * @param indiceInicial
	 * @param textoSubstituto
	 * @param pesquisarTextos
	 * @return
	 * @throws ServiceException
	 * @throws IOException
	 * @throws BadLocationException
	 */
	private Map<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>> empacotarItensDespachoIguais (List<ProcessoPublicado> relacao, Integer indiceInicial, byte[] textoSubstituto, boolean pesquisarTextos) throws ServiceException, IOException, BadLocationException {
		
		// LISTA COM TODOS OS ITENS - AS LISTAS DE TEXTOS IDENTICOS AO FINAL
		List<ItemRelacaoWrapper<ProcessoPublicado>> itens = ordenarItensDespachoIguais(relacao, indiceInicial, pesquisarTextos);
		
		Map<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>> mapaRelatorDespachoIdentico = new HashMap<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>>();
		
		int ii = indiceInicial;
		try {
			while ( itens.size()>0 ) {
				ItemRelacaoWrapper<ProcessoPublicado> itemRelacao = itens.get(0);
				
				Long seqArquivo = null;
				
				if ( itemRelacao.getTextos().size()==1 ) {
					Texto texto = (Texto) itemRelacao.getTextos().get(0);
					if ( texto.getTextosIguais() ) {
						seqArquivo = texto.getArquivoEletronico().getId();
					}
				}
				
				itemRelacao.setIndice( ii++ );
				
				ii = adicionarTextosIdenticos(itemRelacao, ii, seqArquivo, textoSubstituto, mapaRelatorDespachoIdentico, itens);	
				
			}
		} catch ( Exception e ) {
			throw new ServiceException(e);
		}
		
		return mapaRelatorDespachoIdentico;
		
	}
	
	
	
	private int adicionarTextosIdenticos (ItemRelacaoWrapper<ProcessoPublicado> itemAtual, int indiceInicial, Long seq, byte[] textoSubstituto, Map<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>> mapaRelatorDespachoIdentico, List<ItemRelacaoWrapper<ProcessoPublicado>> itens) throws IOException, BadLocationException, ServiceException {
		int ii = indiceInicial;
		List<ItemRelacaoWrapper<ProcessoPublicado>> itensIdenticos = new ArrayList<ItemRelacaoWrapper<ProcessoPublicado>>();
		itensIdenticos.add( itemAtual );
		itens.remove(0);
		for ( int i=0 ; i<itens.size() ; i++ ) {
			ItemRelacaoWrapper<ProcessoPublicado> itemRelacao = itens.get(i);
			
			Long seqArquivo = null;
			
			if ( itemRelacao.getTextos().size()==1 ) {
				Texto texto = (Texto) itemRelacao.getTextos().get(0);
				if ( texto.getTextosIguais() ) {
					seqArquivo = texto.getArquivoEletronico().getId();
				}
			}
			
			if ( seqArquivo!=null && seqArquivo.equals( seq ) ) {
				itemRelacao.setIndice( ii++ );
				itemRelacao.setTextos( Arrays.asList( BuilderHelper.apendarRtfTextoIgual(textoSubstituto,indiceInicial-1) ));
				itensIdenticos.add( itemRelacao );
				itens.remove(i--);
			}
		}
		
		if ( itensIdenticos.size()==1 ) {
			adicionarListaMapa(NOME_MINISTRO_VAZIO, itensIdenticos, mapaRelatorDespachoIdentico);
		} else {
			Ministro relator = getMinistroRelator((Processo) itemAtual.getWrappedObject().getObjetoIncidente().getPrincipal());
			adicionarListaMapa(relator, itensIdenticos, mapaRelatorDespachoIdentico);
		}
		return ii;
	}
	
	private void adicionarListaMapa (Ministro ministro, List<ItemRelacaoWrapper<ProcessoPublicado>> itens, Map<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>> mapaRelatorDespachoIdentico) {
		ChaveMinistro chave = new ChaveMinistro(ministro);
		adicionarListaMapa(chave, itens, mapaRelatorDespachoIdentico);
	}
	
	private void adicionarListaMapa (ChaveMinistro ministro, List<ItemRelacaoWrapper<ProcessoPublicado>> itens, Map<ChaveMinistro, List<ItemRelacaoWrapper<ProcessoPublicado>>> mapaRelatorDespachoIdentico) {
		List<ItemRelacaoWrapper<ProcessoPublicado>> itensMapa = mapaRelatorDespachoIdentico.get(ministro);
		if ( itensMapa==null ) {
			mapaRelatorDespachoIdentico.put(ministro, itens);
		} else {
			itensMapa.addAll(itens);
			mapaRelatorDespachoIdentico.put(ministro, itensMapa);
		}
	}
	
	

	/*@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos, boolean pesquisarTextos) throws ServiceException {
		
		try {
			List<ItemRelacaoWrapper<ProcessoPublicado>> itens = empacotarItensRelacaoIguais(getRelacaoProcessoPublicado(ordernarProcessos, cp), indiceInicial, BuilderHelper.stringToRtf("Decisão idêntica a: "), pesquisarTextos);
			List<Conteudo> conteudos = new ArrayList<Conteudo>();
			conteudos.add( new ConteudoTexto(BuilderHelper.stringToRtf("JULGAMENTOS"), ConteudoTexto.ALINHAMENTO_CENTRALIZADO) );
			conteudos.addAll( recuperarCapitulosComTitulosIguais(cp, itens) );
			return conteudos;
		} catch (Exception e) {
			throw new ServiceException(e);
		}	
		
		
	}*/
	
	@Override
	protected List pesquisarTextos(ProcessoPublicado entidade)
			throws ServiceException {
		
		List decisoes = getRelacaoTextoDecisaoProcessoPublicado(entidade, conteudoPublicacao.getDataCriacao());
		return decisoes;
		
	}
	

	
	
	

}
