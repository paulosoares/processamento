package br.gov.stf.estf.entidade.jurisdicionado.util;

public class JurisdicionadoResult {

	private Long id;
	private String nome;
	private String cpf;
	private String papel;
	private Boolean autorizador;
	private Long codigoAutorizador;
	// armazena id autorizado + id autorizador
	private Long idAutorizadoAutorizador;
	// armazena o id da associação, caso exista.
	private Long idAssociacao;
	private Long idPapel;
	private Boolean entidadeGovernamental;
	private String urlIcone;
	
	
	public void setPapel(String papel) {
		this.papel = papel;
	}
	public String getPapel() {
		return papel;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getCpf() {
		return cpf;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNome() {
		return nome;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setAutorizador(Boolean autorizador) {
		this.autorizador = autorizador;
	}
	public Boolean getAutorizador() {
		return autorizador;
	}
	public void setCodigoAutorizador(Long codigoAutorizador) {
		this.codigoAutorizador = codigoAutorizador;
	}
	public Long getCodigoAutorizador() {
		return ((codigoAutorizador==null) ? 0 : codigoAutorizador );
	}
	public void setIdAutorizadoAutorizador(Long idAutorizadoAutorizador) {
		this.idAutorizadoAutorizador = idAutorizadoAutorizador;
	}
	public Long getIdAutorizadoAutorizador() {
		return idAutorizadoAutorizador;
	}
	public void setIdAssociacao(Long idAssociacao) {
		this.idAssociacao = idAssociacao;
	}
	public Long getIdAssociacao() {
		return idAssociacao;
	}
	public void setIdPapel(Long idPapel) {
		this.idPapel = idPapel;
	}
	public Long getIdPapel() {
		return idPapel;
	}
	public Boolean getEntidadeGovernamental() {
		return entidadeGovernamental;
	}
	public void setEntidadeGovernamental(Boolean entidadeGovernamental) {
		this.entidadeGovernamental = entidadeGovernamental;
	}
	public String getUrlIcone() {
		if (!entidadeGovernamental) { // pessoas (advogados/estagiários)
			setUrlIcone("/images/icone_advogado.png");
		} else { // órgãos externos
			setUrlIcone("/images/icone_orgao_externo.png");
		}
		return urlIcone;
	}
	public void setUrlIcone(String urlIcone) {
		this.urlIcone = urlIcone;
	}

}
