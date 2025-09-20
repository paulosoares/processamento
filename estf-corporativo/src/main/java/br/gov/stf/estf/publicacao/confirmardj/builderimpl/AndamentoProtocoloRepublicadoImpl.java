package br.gov.stf.estf.publicacao.confirmardj.builderimpl;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.publicacao.confirmardj.builder.ConfirmarMateriaBuilder;
import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoProtocoloRepublicadoImpl extends ConfirmarMateriaBuilder<ProtocoloPublicado> {
	private Date dataAtual = new Date();
	public AndamentoProtocoloRepublicadoImpl(ConteudoPublicacao conteudoPublicacao) {
		super(conteudoPublicacao);
	}

	@Override
	public void confirmar(Publicacao publicacao, String siglaUsuario,
			Setor setor, ProtocoloPublicado entidade, String observacao)
			throws ServiceException {
		AndamentoProtocolo andamentoProtocolo = new AndamentoProtocolo();
		andamentoProtocolo.setCodigoAndamento( getCodigoAndamento(entidade) );
		andamentoProtocolo.setDataAndamento(dataAtual);
		andamentoProtocolo.setDataHoraSistema(dataAtual);
		andamentoProtocolo.setNumeroSequencia( getNumeroSequencial(entidade.getProtocolo())  );
		andamentoProtocolo.setProtocolo(entidade.getProtocolo());
		andamentoProtocolo.setSiglaUsuario( siglaUsuario );
		andamentoProtocolo.setSetor(setor);
		andamentoProtocolo.setDescricaoObservacaoAndamento( montarDescricaoObservacaoProtocoloRepublicado( publicacao ));
		andamentoProtocolo.setValido(true);
		
		inserirAndamentoProtocolo(andamentoProtocolo);
		
	}

	@Override
	public Long getCodigoAndamento(ProtocoloPublicado entidade) {
		return 7910L;
	}

	@Override
	public List<ProtocoloPublicado> pesquisar(ConteudoPublicacao conteudoPublicacao)
			throws ServiceException {
		return pesquisarProtocoloPublicado(conteudoPublicacao);
	}

}
