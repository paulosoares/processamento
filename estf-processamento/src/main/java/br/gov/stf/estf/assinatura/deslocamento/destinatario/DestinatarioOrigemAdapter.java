package br.gov.stf.estf.assinatura.deslocamento.destinatario;

import br.gov.stf.estf.entidade.localizacao.Origem;

public class DestinatarioOrigemAdapter implements Destinatario {

	private Origem origem;

	DestinatarioOrigemAdapter(Origem origem) {
		this.origem = origem;
	}

	@Override
	public Long getCodigo() {
		return origem.getId();
	}

	@Override
	public String getDescricao() {
		return origem.getDescricao();
	}

}
