package br.gov.stf.estf.assinatura.deslocamento.destinatario;

import br.gov.stf.estf.entidade.localizacao.Advogado;
//import br.gov.stf.estf.entidade.processostf.Jurisdicionado;

public class DestinatarioAdvogadoAdapter implements Destinatario {

	private Advogado advogado;

	DestinatarioAdvogadoAdapter(Advogado advogado) {
		this.advogado = advogado;
	}

	@Override
	public Long getCodigo() {
		return advogado.getId();
	}

	@Override
	public String getDescricao() {
		return advogado.getNome();
	}

}
