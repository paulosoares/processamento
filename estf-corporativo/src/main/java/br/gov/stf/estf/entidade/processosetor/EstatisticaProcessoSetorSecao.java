package br.gov.stf.estf.entidade.processosetor;

import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.localizacao.TipoStatusSetor;
import br.gov.stf.estf.entidade.usuario.Usuario;

public class EstatisticaProcessoSetorSecao {
	
	private Long quantidade;
	private SecaoSetor secao;
	private TipoStatusSetor statusSetor;
	private TipoFaseSetor faseSetor;
    private Usuario usuario;
	
	protected Usuario getUsuario() {
        return usuario;
    }
    protected void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public TipoFaseSetor getFaseSetor() {
		return faseSetor;
	}
	public void setFaseSetor(TipoFaseSetor faseSetor) {
		this.faseSetor = faseSetor;
	}
	public TipoStatusSetor getStatusSetor() {
		return statusSetor;
	}
	public void setStatusSetor(TipoStatusSetor statusSetor) {
		this.statusSetor = statusSetor;
	}
	public Long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	public SecaoSetor getSecao() {
		return secao;
	}
	public void setSecao(SecaoSetor secao) {
		this.secao = secao;
	}
}
