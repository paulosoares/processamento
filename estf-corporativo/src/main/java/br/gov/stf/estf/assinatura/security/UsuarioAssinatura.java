package br.gov.stf.estf.assinatura.security;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.security.user.User;

public class UsuarioAssinatura extends User {

	private static final long serialVersionUID = -4003147736046169918L;

	private Boolean ativo;
	private Setor setor;

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}
}
