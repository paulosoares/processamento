package br.gov.stf.estf.publicacao.compordj.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.text.BadLocationException;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoRelacao;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoTexto;
import br.gov.stf.estf.publicacao.compordj.modelo.ItemRelacaoWrapper;
import br.gov.stf.estf.publicacao.model.service.MontaDjService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.ApplicationFactory;

/**
 * 
 * @author leonardo.borges
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class ConteudoBuilder<T extends ESTFBaseEntity> {
	private MontaDjService montaDjService;
	
	public ConteudoBuilder () {
		montaDjService = (MontaDjService) ApplicationFactory.getInstance().getServiceLocator().getService("montaDjService");
	}
	
	public abstract List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordenarProcessos, boolean pesquisarTextos) throws ServiceException;
	
	/**
	 * 
	 * @param entidade
	 * @return Pode retorna um array de: bytes, Long ou Texto
	 * @throws ServiceException
	 */
	protected abstract List pesquisarTextos (T entidade) throws ServiceException;
	
	protected List<Conteudo> recuperarCapitulosComTitulosIguais (ConteudoPublicacao conteudoPublicacao, List<ItemRelacaoWrapper<T>> lista) throws BadLocationException, IOException {
		List<Conteudo> conteudos = new ArrayList<Conteudo>();
		int tamLista = lista.size();
		int ultimoCabeca = 0, i;
		for ( i=0 ; i<tamLista ; i++ ) {
			ItemRelacaoWrapper<T> item = lista.get(i);
			if ( item.isCabecaIguais() ) {
				conteudos.add( new ConteudoRelacao<T>(lista.subList(ultimoCabeca, i)) );
				conteudos.add( new ConteudoTexto( recuperarTituloListaIguais(conteudoPublicacao, item.getWrappedObject()), ConteudoTexto.ALINHAMENTO_ESQUERDA)) ;
				ultimoCabeca = i;
			}
		}
		conteudos.add( new ConteudoRelacao<T>(lista.subList(ultimoCabeca, i)) );
		
		
		return conteudos;
	}
	
	private byte[] recuperarTituloListaIguais (ConteudoPublicacao conteudoPublicacao, T entidade) throws BadLocationException, IOException {
		String texto = null;
		Integer codigoCapitulo = conteudoPublicacao.getCodigoCapitulo();
		
		if ( EstruturaPublicacao.COD_CAPITULO_ACORDAOS.equals( codigoCapitulo ) ) {
			texto = "Processos com Ementas idênticas:";
		} else if ( EstruturaPublicacao.COD_CAPITULO_PLENARIO.equals( codigoCapitulo )  ) {
			texto = "Processos com Decisões Idênticas:";
		} else if ( EstruturaPublicacao.COD_CAPITULO_SECRETARIA_JUDICIARIA.equals( codigoCapitulo ) && entidade instanceof TextoPeticao ) {
			TextoPeticao textoPeticao = (TextoPeticao) entidade;
			if ( TextoPeticao.TIPO_TEXTO_DECISAO.equals( textoPeticao.getTipoTexto() ) ) {
				texto = "Protocolos com Decisões Idênticas:";
			} else {
				texto = "Protocolos com Despachos Idênticos:";
			}
		} else {
			texto = "Processos com Despachos Idênticos:";
		}
			
			
		return BuilderHelper.montarTextoInicialTextoIdentico(texto);
	}
	
	protected List<ItemRelacaoWrapper<T>> empacotarItensRelacao (List<T> relacao, Integer indiceInicial, boolean pesquisarTextos) throws ServiceException {
		List<ItemRelacaoWrapper<T>> lista = new ArrayList<ItemRelacaoWrapper<T>>();
		int i = indiceInicial;
		for ( T t: relacao ) {
			List textos = null;
			if ( pesquisarTextos ) {
				textos = pesquisarTextos(t);
			}
			ItemRelacaoWrapper<T> item = new ItemRelacaoWrapper<T>(i++,t,textos);
			
			lista.add( item );
		}
		return lista;
	}
	
	protected int adicionarTextosIdenticos (List<ItemRelacaoWrapper<T>> itens, int comeco, int indiceInicial, Long seq, List<ItemRelacaoWrapper<T>> itensFinal, byte[] textoSubstituto) throws IOException, BadLocationException {
		int ii = indiceInicial;
		for ( int i=comeco ; i<itens.size() ; i++ ) {
			ItemRelacaoWrapper<T> itemRelacao = itens.get(i);
			List objPesquisados = itemRelacao.getTextos();
			Long seqArquivo = null;
			if ( objPesquisados.size()==1 ) {
				Object obj = objPesquisados.get(0);
				
				if ( obj instanceof Texto ) {
					Texto texto = (Texto) obj;
					if ( texto.getTextosIguais() ) {
						seqArquivo = texto.getArquivoEletronico().getId();
					}
				} else if ( obj instanceof Long ) {
					seqArquivo = (Long) obj;
				}					
			}
			
			if ( seqArquivo!=null && seqArquivo.equals( seq ) ) {
				itemRelacao.setIndice( ii++ );
				itemRelacao.setTextos( Arrays.asList( BuilderHelper.apendarRtfTextoIgual(textoSubstituto,indiceInicial-1) ));
				itensFinal.add( itemRelacao );
				itens.remove(i--);
			}
		}		
		return ii;
	}
	
	protected List<ItemRelacaoWrapper<T>> empacotarItensRelacaoIguais (List<T> relacao, Integer indiceInicial, byte[] textoSubstituto, boolean pesquisarTextos) throws ServiceException {
		// EMPACOTA PREVIAMENTE OS ITENS, MENOS O INDICE
		List<ItemRelacaoWrapper<T>> itens = empacotarItensRelacao(relacao, indiceInicial, pesquisarTextos);
		if ( pesquisarTextos ) {
			List<ItemRelacaoWrapper<T>> itensFinal = new ArrayList<ItemRelacaoWrapper<T>>();
			int ii = indiceInicial;
			for ( int i = 0 ; i<itens.size() ; i++ ) {
				ItemRelacaoWrapper<T> itemRelacao = itens.get(i);
				List objPesquisados = itemRelacao.getTextos();
				Long seqArquivo = null;
				if ( objPesquisados.size()==1 ) {
					Object obj = objPesquisados.get(0);
					
					if ( obj instanceof Texto ) {
						Texto texto = (Texto) obj;
						if ( texto.getTextosIguais() ) {
							seqArquivo = texto.getArquivoEletronico().getId();
						}
					} else if ( obj instanceof Long ) {
						seqArquivo = (Long) obj;
					}					
				}
				
				itemRelacao.setIndice( ii++ );
				itensFinal.add( itemRelacao );
				
				if ( seqArquivo!=null ) {				
					try {
						int iiAntes = ii;
						ii = adicionarTextosIdenticos(itens, i+1, ii, seqArquivo, itensFinal, textoSubstituto);
						if ( iiAntes!=ii ) {
							itemRelacao.setCabecaIguais(true);
						}
					} catch (Exception e) {
						throw new ServiceException(e);
					}			
				}		
				
			}
			return itensFinal;
		} else { 
			return itens;
		}
	}
	
	
	protected List<IncidenteDistribuicao> getRelacaoDistribuicao (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException {		
		return montaDjService.pesquisarRelacaoProcessoDistribuido(ordenar, conteudoPublicacao);		
	}
	
	protected byte[] getTextoAberturaDistribuicao (ConteudoPublicacao conteudoPublicacao) throws ServiceException {		
		return montaDjService.recuperarTextoAberturaDistribuicao(conteudoPublicacao);		
	}
	
	protected byte[] getTextoAberturaDistribuicaoPresidente (ConteudoPublicacao conteudoPublicacao) throws ServiceException {		
		return montaDjService.recuperarTextoAberturaDistribuicaoPresidente(conteudoPublicacao);		
	}
	
	protected byte[] getTextoAberturaSessaoEspecial () throws ServiceException {
		return montaDjService.recuperarTextoAberturaSessaoEspecial();
	}
	
	protected byte[] getTextoFechamentoSessaoEspecial () throws ServiceException {
		return montaDjService.recuperarTextoFechamentoSessaoEspecial();
	}
	
	protected byte[] getTextoAberturaSessaoEspecialADPF () throws ServiceException {
		return montaDjService.recuperarTextoAberturaSessaoEspecialADPF();
	}
	
	protected byte[] getTextoFechamentoDistribuicao (ConteudoPublicacao conteudoPublicacao) throws ServiceException {		
		return montaDjService.recuperarTextoFechamentoDistribuicao(conteudoPublicacao);		
	}
	
	protected List<ProcessoPublicado> getRelacaoProcessoPublicado (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return montaDjService.pesquisarRelacaoProcessoPublicado(ordenar, conteudoPublicacao);
	}
	
	protected List<ProtocoloPublicado> getRelacaoProtocoloPublicado (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return montaDjService.pesquisarRelacaoProtocoloPublicado(ordenar, conteudoPublicacao);
	}
	
	protected List<Texto> getRelacaoTextoDecisaoProcessoPublicado (ProcessoPublicado processoPublicado, Date dataSessao) throws ServiceException {
		return montaDjService.pesquisarTextoDecisaoProcessoPublicado(processoPublicado, dataSessao);
	}
	
	protected Texto getTextoDecisaoProcessoPublicadoAta (ProcessoPublicado processoPublicado) throws ServiceException {
		return montaDjService.recuperarTextoDecisaoProcessoPublicadoAta(processoPublicado);
	}
	
	protected List<Texto> getRelacaoTextoEditalProcessoPublicado (ProcessoPublicado processoPublicado, Date dataSessao) throws ServiceException {
		return montaDjService.pesquisarTextoEditalProcessoPublicado(processoPublicado, dataSessao);
	}
	
	protected List<Texto> getRelacaoTextoDecisaoAcordaoAgendado (AcordaoAgendado acordaoAgendado, Date dataSessao) throws ServiceException {
		return montaDjService.pesquisarRelacaoTextoDecisaoAcordaoAgendado(acordaoAgendado, dataSessao);
	}
	
	protected Texto getTextoEmentaAcordaoAgendado (AcordaoAgendado acordaoAgendado) throws ServiceException {
		return montaDjService.recuperarTextoEmentaAcordaoAgendado(acordaoAgendado);
	}
	
	protected Texto getTextoEmentaProcessoPublicado (ProcessoPublicado processoPublicado) throws ServiceException {
		return montaDjService.recuperarTextoEmentaProcessoPublicado(processoPublicado);
	}
	
	protected List<Texto> getRelacaoTextoDecisaoRepercussaoGeral (ProcessoPublicado processoPublicado, Date dataSessao) throws ServiceException {
		return montaDjService.pesquisarTextoDecisaoRepercussaoGeral(processoPublicado, dataSessao);
	}
	
	protected Texto getTextoEmentaRepercussaoGeral (ProcessoPublicado processoPublicado) throws ServiceException {
		return montaDjService.recuperarTextoEmentaRepercussaoGeral(processoPublicado);
	}
	
	protected TextoAssociadoProtocolo getTextoRepublicacaoProtocoloPublicado (ProtocoloPublicado protocoloPublicado) throws ServiceException {
		return montaDjService.recuperarTextoRepublicacaoProtocoloPublicado(protocoloPublicado);
	}
	
	protected TextoAssociadoProtocolo getTextoObservacaoProtocoloPublicado (ProtocoloPublicado protocoloPublicado) throws ServiceException {
		return montaDjService.recuperarTextoObservacaoProtocoloPublicado(protocoloPublicado);
	}
	
	protected Date getDataCriacaoMateria(ProcessoPublicado processoPublicado) throws ServiceException {
		return montaDjService.recuperarDataCriacaoMateria(processoPublicado);
	}
	
	public List<TextoPeticao> getRelacaoTextoPeticao (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return montaDjService.pesquisarTextoPeticao(ordenar, conteudoPublicacao);
	}
	
	public List<TextoPeticao> getRelacaoTextoProtocolo (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return montaDjService.pesquisarTextoProtocolo(ordenar, conteudoPublicacao);
	}
	
	public List<AcordaoAgendado> getRelacaoAcordaosSessaoEspecialADIADCADO (boolean ordenar) throws ServiceException {
		return montaDjService.pesquisarAcordaosSessaoEspecial(ordenar, Classe.SIGLA_ACAO_DIRETA_DE_INCONSTITUCIONALIDADE, Classe.SIGLA_ACAO_DECLARATORIA_DE_CONSTITUCIONALIDADE, Classe.SIGLA_ACAO_DIRETA_DE_INCONSTITUCIONALIDADE_POR_EMISSAO);
	}
	
	public List<AcordaoAgendado> getRelacaoAcordaosSessaoEspecialADPF (boolean ordenar) throws ServiceException {
		return montaDjService.pesquisarAcordaosSessaoEspecial(ordenar, Classe.SIGLA_ARGUICAO_DE_DESCUMPRIMENTO_DE_PRECEITO_FUNDAMENTAL);
	}
	
	protected List<ProcessoPublicado> getRelacaoProcessosSessaoEspecialADIADCADO (boolean ordenar) throws ServiceException {
		return montaDjService.pesquisarProcessosSessaoEspecial(ordenar, Classe.SIGLA_ACAO_DIRETA_DE_INCONSTITUCIONALIDADE, Classe.SIGLA_ACAO_DECLARATORIA_DE_CONSTITUCIONALIDADE,Classe.SIGLA_ACAO_DIRETA_DE_INCONSTITUCIONALIDADE_POR_EMISSAO);
	}
	
	protected List<ProcessoPublicado> getRelacaoProcessosSessaoEspecialADPF (boolean ordenar) throws ServiceException {
		return montaDjService.pesquisarProcessosSessaoEspecial(ordenar, Classe.SIGLA_ARGUICAO_DE_DESCUMPRIMENTO_DE_PRECEITO_FUNDAMENTAL);
	}
	
	protected Ministro getMinistroRelator(Processo processo) throws ServiceException {
		return montaDjService.recuperarMinistroRelator(processo);
	}
	
	protected Assunto getAssuntoProcesso (Processo processo) throws ServiceException {
		return montaDjService.recuperarAssuntoProcesso(processo);
	}
	
	public MontaDjService getMontaDjService() {
		return montaDjService;
	}

	public void setMontaDjService(MontaDjService montaDjService) {
		this.montaDjService = montaDjService;
	}
}
