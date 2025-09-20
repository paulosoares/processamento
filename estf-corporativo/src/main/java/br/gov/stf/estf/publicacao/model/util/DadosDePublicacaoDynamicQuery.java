package br.gov.stf.estf.publicacao.model.util;

import br.gov.stf.framework.model.util.query.DynamicQuery;
import br.gov.stf.framework.model.util.query.SQLConnective;

public class DadosDePublicacaoDynamicQuery extends DynamicQuery {

	public DadosDePublicacaoDynamicQuery() {
		insereCondicaoTextual("cp.ano = pp.anoMateria", SQLConnective.AND);
		insereCondicaoTextual("cp.numero = pp.numeroMateria", SQLConnective.AND);
		insereCondicaoTextual("cp.codigoMateria = pp.codigoMateria", SQLConnective.AND);
		insereCondicaoTextual("cp.codigoCapitulo = pp.codigoCapitulo", SQLConnective.AND);
		insereCondicao("cp.codigoConteudo", 50);
	}

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		if (sequencialObjetoIncidente != null) {
			insereCondicao("pp.objetoIncidente.id", sequencialObjetoIncidente);
		}
	}

	public void setSequencialDoArquivoEletronico(Long sequencialDoArquivoEletronico) {
		if (sequencialDoArquivoEletronico != null) {
			insereCondicao("pp.arquivoEletronicoTexto.id", sequencialDoArquivoEletronico);
		}else{
			//Indica que n�o � uma consulta de despacho. Nesse caso, deve incluir a restri��o por tipo de mat�ria
			//No caso, s� pode ser das mat�rias:
			//1 - Plen�rio
			//2 - Primeira turma
			//3 - Segunda turma
			//Alterado em 18/08/2010 por Dem�trius Jub�
			insereCondicaoIn("cp.codigoMateria", SQLConnective.AND, 1,2,3);
		}
	}

	public void setMateriaCodigoCapitulo(Integer codigoCapituloMateria) {
		if (codigoCapituloMateria != null) {
			insereCondicao("cp.codigoCapitulo", codigoCapituloMateria);
		}
	}

	public void setProcessoCodigoCapitulo(Integer codigoCapituloProcesso) {
		if (codigoCapituloProcesso != null) {
			insereCondicao("pp.codigoCapitulo", codigoCapituloProcesso);
		}
	}
	

}
