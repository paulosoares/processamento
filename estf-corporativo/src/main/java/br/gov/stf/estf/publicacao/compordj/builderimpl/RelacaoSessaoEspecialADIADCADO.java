package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoRelacao;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoTexto;
import br.gov.stf.framework.model.service.ServiceException;

@SuppressWarnings("unchecked")
public class RelacaoSessaoEspecialADIADCADO extends ConteudoBuilder<ESTFBaseEntity> {
	private ConteudoPublicacao conteudoPublicacao;
	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos, boolean pesquisarTextos)
			throws ServiceException {
		this.conteudoPublicacao = cp;
		try {
			List<Conteudo> conteudos = new ArrayList<Conteudo>();
			
			List acordaos = getRelacaoAcordaosSessaoEspecialADIADCADO(ordernarProcessos);
			List processos = getRelacaoProcessosSessaoEspecialADIADCADO(ordernarProcessos);
			
			if ( processos!=null && processos.size()>0 ) {
				conteudos.add( new ConteudoTexto(BuilderHelper.stringToRtf("JULGAMENTOS"), ConteudoTexto.ALINHAMENTO_CENTRALIZADO) );
				List itens = empacotarItensRelacao(processos, indiceInicial, pesquisarTextos);
				indiceInicial += itens.size();
				conteudos.add( new ConteudoRelacao( itens ) );
			}
			
			if ( acordaos!=null && acordaos.size()>0 ) {
				conteudos.add( new ConteudoTexto(BuilderHelper.stringToRtf("ACÓRDÃOS"), ConteudoTexto.ALINHAMENTO_CENTRALIZADO) );
				List itens = empacotarItensRelacao(acordaos, indiceInicial, pesquisarTextos);
				conteudos.add( new ConteudoRelacao( itens ) );
			}
			
			//TEXTO DE FECHAMENTO DA PUBLICAÇÃO ESPECIAL
			ConteudoTexto conteudoTextoFechamento = new ConteudoTexto();
			conteudoTextoFechamento.setTexto( getTextoFechamentoSessaoEspecial() );
			conteudoTextoFechamento.setAlinhamentoTexto( ConteudoTexto.ALINHAMENTO_CENTRALIZADO );
			conteudos.add( conteudoTextoFechamento );
			
			return conteudos;
		} catch ( Exception e ) {
			throw new ServiceException(e);
		}
	}  

	@Override
	protected List pesquisarTextos(ESTFBaseEntity entidade)
			throws ServiceException {
		if ( entidade instanceof AcordaoAgendado ) {
			AcordaoAgendado acordaoAgendado = (AcordaoAgendado) entidade;
			List<Texto> textos = new ArrayList<Texto>();
			textos.addAll( getRelacaoTextoDecisaoAcordaoAgendado(acordaoAgendado, conteudoPublicacao.getDataCriacao()) );
			textos.add( getTextoEmentaAcordaoAgendado(acordaoAgendado) );
			return textos;
		} else if ( entidade instanceof ProcessoPublicado ) {
			ProcessoPublicado processoPublicado = (ProcessoPublicado) entidade;
			List textos = new ArrayList<Texto>();
			textos.addAll( getRelacaoTextoDecisaoProcessoPublicado( processoPublicado, getDataCriacaoMateria(processoPublicado) ) );
			//textos.add( getTextoEmentaProcessoPublicado(processoPublicado) );
			// SE FOR UMA REPUBLICAÇÃO
			if ( processoPublicado.getCodigoMateria()==5 ) {
				textos.add( processoPublicado.getArquivoEletronicoObs().getId() );
			}
			return textos;
		}
		return null;
	}

}
