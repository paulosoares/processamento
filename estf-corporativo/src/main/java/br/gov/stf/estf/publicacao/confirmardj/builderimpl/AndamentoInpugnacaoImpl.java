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

public class AndamentoInpugnacaoImpl extends ConfirmarMateriaBuilder<ProcessoPublicado> {
	private Date dataAtual = new Date();
	public AndamentoInpugnacaoImpl(ConteudoPublicacao conteudoPublicacao) {
		super(conteudoPublicacao);
	}

	@Override
	public void confirmar(Publicacao publicacao, String siglaUsuario,
			Setor setor, ProcessoPublicado entidade, String observacao) throws ServiceException {
		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		andamentoProcesso.setCodigoAndamento( getCodigoAndamento(entidade) );
		andamentoProcesso.setCodigoUsuario( siglaUsuario );
		andamentoProcesso.setDataAndamento( dataAtual );
		andamentoProcesso.setDataHoraSistema( dataAtual );
		andamentoProcesso.setDescricaoObservacaoAndamento( "" );
		andamentoProcesso.setNumeroSequencia( getNumeroSequencial( entidade.getObjetoIncidente() ) );
		andamentoProcesso.setObjetoIncidente( entidade.getObjetoIncidente() );
		andamentoProcesso.setSetor( setor );
		
		inserirAndamentoProcesso(andamentoProcesso, publicacao, observacao);
	}

	@Override
	public Long getCodigoAndamento(ProcessoPublicado entidade) {
		return 7801L;
	}

	@Override
	public List<ProcessoPublicado> pesquisar(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return pesquisarProcessosPublicados(conteudoPublicacao);
	}

}
