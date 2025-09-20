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

/**
 * Impl para os andamento tipo 5 - Acordaos
 * 
 * @author leonardo.borges
 * 
 */
public class AndamentoAcordaoImpl extends ConfirmarMateriaBuilder<ProcessoPublicado> {
	private String descricaoObservacao;
	private Date dataAtual = new Date();

	public AndamentoAcordaoImpl(ConteudoPublicacao conteudoPublicacao) {
		super(conteudoPublicacao);
		this.descricaoObservacao = montarDescricaoObservacaoAcordao();
	}

	@Override
	public void confirmar(Publicacao publicacao, String siglaUsuario, Setor setor, ProcessoPublicado entidade, String observacao)
			throws ServiceException {

		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		andamentoProcesso.setCodigoAndamento(getCodigoAndamento(entidade));
		andamentoProcesso.setCodigoUsuario(siglaUsuario);
		andamentoProcesso.setDataAndamento(dataAtual);
		andamentoProcesso.setDataHoraSistema(dataAtual);
		andamentoProcesso.setDescricaoObservacaoAndamento(descricaoObservacao);
		andamentoProcesso.setNumeroSequencia(getNumeroSequencial(entidade.getObjetoIncidente()));
		andamentoProcesso.setObjetoIncidente( entidade.getObjetoIncidente() );
		andamentoProcesso.setSetor(setor);

		inserirAndamentoProcesso(andamentoProcesso, publicacao, observacao);
		
		ObjetoIncidente objetoIncidente = entidade.getObjetoIncidente().getPrincipal();
		
		if ( objetoIncidente instanceof Processo ) {
			alterarBaixaProcesso((Processo) objetoIncidente);
		}

		if (((Processo)entidade.getObjetoIncidente().getPrincipal()).getTipoMeioProcesso().equals( TipoMeioProcesso.ELETRONICO )) {
			deslocarProcessoEletronico((Processo)entidade.getObjetoIncidente().getPrincipal(), setor);
		}

		inserirTextoAndamentoAcordao(entidade, andamentoProcesso);
	}

	@Override
	public List<ProcessoPublicado> pesquisar(ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return pesquisarProcessosPublicados(conteudoPublicacao);
	}

	@Override
	public Long getCodigoAndamento(ProcessoPublicado entidade) {
		return 7900L;
	}

}
