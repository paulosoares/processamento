package br.gov.stf.estf.publicacao.confirmardj.builderimpl;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.publicacao.confirmardj.builder.ConfirmarMateriaBuilder;
import br.gov.stf.framework.model.service.ServiceException;

public class AndamentoProtocoloImpl extends ConfirmarMateriaBuilder<TextoPeticao> {
	private Date dataAtual = new Date();
	public AndamentoProtocoloImpl(ConteudoPublicacao conteudoPublicacao) {
		super(conteudoPublicacao);
	}

	@Override
	public void confirmar(Publicacao publicacao, String siglaUsuario,
			Setor setor, TextoPeticao entidade, String observacao)
			throws ServiceException {
		AndamentoProtocolo andamentoProtocolo = new AndamentoProtocolo();
		andamentoProtocolo.setCodigoAndamento( getCodigoAndamento(entidade) );
		andamentoProtocolo.setDataAndamento(dataAtual);
		andamentoProtocolo.setDataHoraSistema(dataAtual);
		andamentoProtocolo.setNumeroSequencia( getNumeroSequencial(entidade.getObjetoIncidente())  );
		andamentoProtocolo.setProtocolo(entidade.getProtocolo());
		andamentoProtocolo.setSiglaUsuario( siglaUsuario );
		andamentoProtocolo.setSetor(setor);
		andamentoProtocolo.setDescricaoObservacaoAndamento( montarDescricaoObservacaoProtocolo( publicacao ));
		andamentoProtocolo.setValido(true);
		
		inserirAndamentoProtocolo(andamentoProtocolo);
		
	}

	@Override
	public Long getCodigoAndamento(TextoPeticao entidade) {
		return 7909L;
	}

	@Override
	public List<TextoPeticao> pesquisar(ConteudoPublicacao conteudoPublicacao)
			throws ServiceException {
		return pesquisarTextoPeticaos(conteudoPublicacao);
	}

}
