package br.gov.stf.estf.publicacao.confirmardj.builderimpl;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.publicacao.confirmardj.builder.ConfirmarMateriaBuilder;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Impl para os andamentos tipo 2 - Atas
 * @author leonardo.borges
 *
 */
public class AndamentoAtaImpl extends ConfirmarMateriaBuilder<ProcessoPublicado> {
	private String descricaoObservaocao;
	private Date dataAtual = new Date();
	public AndamentoAtaImpl(ConteudoPublicacao conteudoPublicacao) {
		super(conteudoPublicacao);
		this.descricaoObservaocao = montarDescricaoObservacaoAta();
		
	}

	@Override
	public void confirmar(Publicacao publicacao, String siglaUsuario, Setor setor,
			ProcessoPublicado entidade, String observacao) throws ServiceException {
		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		andamentoProcesso.setCodigoAndamento( getCodigoAndamento(entidade) );
		andamentoProcesso.setCodigoUsuario( siglaUsuario );
		andamentoProcesso.setDataAndamento( dataAtual );
		andamentoProcesso.setDataHoraSistema( dataAtual );
		andamentoProcesso.setDescricaoObservacaoAndamento( descricaoObservaocao );
		andamentoProcesso.setNumeroSequencia( getNumeroSequencial( entidade.getObjetoIncidente() ) );
		andamentoProcesso.setObjetoIncidente( entidade.getObjetoIncidente() );
		andamentoProcesso.setSetor( setor );
		
		inserirAndamentoProcesso(andamentoProcesso, publicacao, observacao);
		
	}

	@Override
	public List<ProcessoPublicado> pesquisar(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return pesquisarProcessosPublicados(conteudoPublicacao);
	}

	@Override
	public Long getCodigoAndamento(ProcessoPublicado entidade) {
		Integer materia = entidade.getCodigoMateria();
		if ( materia==3 || materia==4 ) {
			// A partir da SATE 135237, o código do andamento deve ser 
			// modificado para 7915 quando for publicação de ata de julgamento 
			return 7915L;
			//return 7904L;
		} else if ( materia==5 ) {
			return 7910L;
		} else {
			return null;
		}
	}

}
