package br.gov.stf.estf.intimacao.visao.dto;

import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;

public class ProcessoObjetoIncidenteDTO {

	private Long idProcessoPrincipal;

	private Long id;

	private TipoObjetoIncidente tipoObjetoIncidente;

	private String tipoMeio;

	private String identificacao;

	public boolean getEletronico() {
		return getTipoMeio().equals("E");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((tipoObjetoIncidente == null) ? 0 : tipoObjetoIncidente
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object processoIncidente) {
		boolean result = false;
		if (this.getTipoObjetoIncidente().getCodigo() == ((ProcessoObjetoIncidenteDTO) processoIncidente)
				.getTipoObjetoIncidente().getCodigo()) {
			result = true;
		}
		return result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdProcessoPrincipal() {
		return idProcessoPrincipal;
	}

	public void setIdProcessoPrincipal(Long idProcessoPrincipal) {
		this.idProcessoPrincipal = idProcessoPrincipal;
	}

	public TipoObjetoIncidente getTipoObjetoIncidente() {
		return tipoObjetoIncidente;
	}

	public void setTipoObjetoIncidente(TipoObjetoIncidente tipoObjetoIncidente) {
		this.tipoObjetoIncidente = tipoObjetoIncidente;
	}

	public String getTipoMeio() {
		return tipoMeio;
	}

	public void setTipoMeio(String tipoMeio) {
		this.tipoMeio = tipoMeio;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

}
