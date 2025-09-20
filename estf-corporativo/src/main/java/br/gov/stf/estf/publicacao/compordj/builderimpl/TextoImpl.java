package br.gov.stf.estf.publicacao.compordj.builderimpl;

import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoTexto;
import br.gov.stf.framework.model.service.ServiceException;

public class TextoImpl extends ConteudoBuilder<Texto> {

	@Override
	public List<Conteudo> gerarConteudo(ConteudoPublicacao cp, Integer indiceInicial, boolean ordernarProcessos, boolean pesquisarTextos) throws ServiceException {
		List<Conteudo> conteudos = new ArrayList<Conteudo>();
		ConteudoTexto conteudoTexto = new ConteudoTexto();
		conteudoTexto.setTexto( cp.getArquivoEletronico().getConteudo() );
		
		int alinhamento = ConteudoTexto.ALINHAMENTO_JUSTIFICADO;
		Integer codigoCapitulo = cp.getCodigoCapitulo();
		Integer codigoMateira = cp.getCodigoMateria();
		Integer codigoConteudo = cp.getCodigoConteudo();
		if ( codigoConteudo==90 && codigoCapitulo>=2 && codigoCapitulo<=5 && codigoMateira<=4 ) {
			alinhamento = ConteudoTexto.ALINHAMENTO_CENTRALIZADO;
		}
		conteudoTexto.setAlinhamentoTexto( alinhamento );
		conteudos.add( conteudoTexto );
		return conteudos;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List pesquisarTextos(Texto entidade) throws ServiceException {
		return null;
	}

}
