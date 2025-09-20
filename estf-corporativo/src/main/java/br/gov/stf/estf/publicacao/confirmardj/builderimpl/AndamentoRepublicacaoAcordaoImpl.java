package br.gov.stf.estf.publicacao.confirmardj.builderimpl;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.publicacao.confirmardj.builder.ConfirmarMateriaBuilder;
import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoRepublicacaoAcordaoImpl extends ConfirmarMateriaBuilder<ProcessoPublicado> {
	private String descricaoObservacao;
	private Date dataAtual = new Date();
	public AndamentoRepublicacaoAcordaoImpl(
			ConteudoPublicacao conteudoPublicacao) {
		super(conteudoPublicacao);
		this.descricaoObservacao = montarDescricaoObservacaoAcordaoRepublicado();
	}

	@Override
	public void confirmar(Publicacao publicacao, String siglaUsuario,
			Setor setor, ProcessoPublicado entidade, String observacao)
			throws ServiceException {
		ObjetoIncidente objetoIncidente = entidade.getObjetoIncidente();
		
		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		andamentoProcesso.setCodigoAndamento( getCodigoAndamento(entidade) );
		andamentoProcesso.setCodigoUsuario( siglaUsuario );
		andamentoProcesso.setDataAndamento( dataAtual );
		andamentoProcesso.setDataHoraSistema( dataAtual );
		andamentoProcesso.setDescricaoObservacaoAndamento( descricaoObservacao );
		andamentoProcesso.setNumeroSequencia( getNumeroSequencial( objetoIncidente ) );
		andamentoProcesso.setObjetoIncidente( objetoIncidente );
		andamentoProcesso.setSetor( setor );
		
		inserirAndamentoProcesso(andamentoProcesso, publicacao, observacao);
		
		if ( ((Processo)objetoIncidente.getPrincipal()).getTipoMeioProcesso().equals( TipoMeioProcesso.ELETRONICO ) ) {
			deslocarProcessoEletronico((Processo)objetoIncidente.getPrincipal(), setor);
		}
		
		inserirTextoAndamentoAcordao(entidade, andamentoProcesso);
		
	}

	@Override
	public Long getCodigoAndamento(ProcessoPublicado entidade) {
		return 7901L;
	}

	@Override
	public List<ProcessoPublicado> pesquisar(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return pesquisarProcessosPublicados(conteudoPublicacao);
	}

}
