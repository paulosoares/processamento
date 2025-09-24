package br.jus.stf.estf.decisao.documento.support;

public class DocumentoNaoAssinadoDto<T extends Documento> {

	private T documento;
	private String motivo;
	private String descricao;

	public T getDocumento() {
		return documento;
	}

	public void setDocumento(T documento) {
		this.documento = documento;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
