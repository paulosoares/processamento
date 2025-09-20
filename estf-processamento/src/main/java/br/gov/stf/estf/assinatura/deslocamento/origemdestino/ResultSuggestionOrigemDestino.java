package br.gov.stf.estf.assinatura.deslocamento.origemdestino;


import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.util.JurisdicionadoResult;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.estf.entidade.localizacao.Setor;

public class ResultSuggestionOrigemDestino {

	private OrigemDestino origemDestino;
	private String urlIcone;
	
	public OrigemDestino getOrigemDestino() {
		return origemDestino;
	}
	public void setOrigemDestino(OrigemDestino origemDestino) {
		this.origemDestino = origemDestino;
	}
	public String getUrlIcone() {
		if (origemDestino.getTipoOrigemDestino() == 1) { // advogado
			setUrlIcone("/images/icone_advogado.png");
		} else if (origemDestino.getTipoOrigemDestino() == 2) { // setor stf
			setUrlIcone("/images/icone_setores.png");
		} else { // órgãos externos
			setUrlIcone("/images/icone_orgao_externo.png");
		}
		return urlIcone;
	}
	public void setUrlIcone(String urlIcone) {
		this.urlIcone = urlIcone;
	}
	
	
	public static ResultSuggestionOrigemDestino build(Object objeto){
		OrigemDestino origemDestino = new OrigemDestino();
		ResultSuggestionOrigemDestino resultSuggestionOrigemDestino = new ResultSuggestionOrigemDestino();
		
		if (objeto instanceof Jurisdicionado){
			origemDestino.setId(((Jurisdicionado) objeto).getId());
			origemDestino.setDescricao(((Jurisdicionado) objeto).getNome());
			origemDestino.setTipoOrigemDestino((short) 1);
			
			resultSuggestionOrigemDestino.setOrigemDestino(origemDestino);
			return resultSuggestionOrigemDestino;
		}else if (objeto instanceof JurisdicionadoResult){
				origemDestino.setId(((JurisdicionadoResult) objeto).getId());
				origemDestino.setDescricao(((JurisdicionadoResult) objeto).getNome());
				origemDestino.setTipoOrigemDestino((short) 1);
				
				resultSuggestionOrigemDestino.setOrigemDestino(origemDestino);
				return resultSuggestionOrigemDestino;
		}else if (objeto instanceof Setor){
			origemDestino.setId(((Setor) objeto).getId());
			origemDestino.setDescricao(((Setor) objeto).getNome());
			origemDestino.setTipoOrigemDestino((short) 2);
			
			resultSuggestionOrigemDestino.setOrigemDestino(origemDestino);
			return resultSuggestionOrigemDestino;
		}else if (objeto instanceof Origem){
			origemDestino.setId(((Origem) objeto).getId());
			origemDestino.setDescricao(((Origem) objeto).getDescricao());
			origemDestino.setTipoOrigemDestino((short) 3);
			
			resultSuggestionOrigemDestino.setOrigemDestino(origemDestino);
			return resultSuggestionOrigemDestino;
		}
		throw new IllegalArgumentException("O objeto não pode ser mapeado para Destinatario!");
		
		
	}
	
	
}
