package br.gov.stf.estf.assinatura.deslocamento.destinatario;

import br.gov.stf.estf.entidade.localizacao.Setor;

public class DestinatarioSetorAdapter implements Destinatario {

	private Setor setor;

	DestinatarioSetorAdapter(Setor setor) {
		this.setor = setor;
	}

	@Override
	public Long getCodigo() {
		return setor.getId();
	}

	@Override
	public String getDescricao() {
		return setor.getNome();
	}

}
