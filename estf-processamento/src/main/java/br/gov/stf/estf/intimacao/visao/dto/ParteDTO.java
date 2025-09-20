package br.gov.stf.estf.intimacao.visao.dto;

import br.gov.stf.estf.entidade.processostf.Categoria;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

public class ParteDTO {

	
	private boolean checked;
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private String nomeJurisdicionado;
	private Categoria categoria;
	private Long seqJurisdicionado;
	private Integer numeroOrdem;
	private String dscTipoImpressao;
	private String cadastroRatificado;
	private String prerrogativaIntimacao;
	private String login;
	private String tipoPessoa;
	private String qualificacao;

	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	public String getNomeJurisdicionado() {
		return nomeJurisdicionado;
	}
	public void setNomeJurisdicionado(String nomeJurisdicionado) {
		this.nomeJurisdicionado = nomeJurisdicionado;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Long getSeqJurisdicionado() {
		return seqJurisdicionado;
	}
	public void setSeqJurisdicionado(Long seqJurisdicionado) {
		this.seqJurisdicionado = seqJurisdicionado;
	}
	public Integer getNumeroOrdem() {
		return numeroOrdem;
	}
	public void setNumeroOrdem(Integer numeroOrdem) {
		this.numeroOrdem = numeroOrdem;
	}
	public String getDscTipoImpressao() {
		return dscTipoImpressao;
	}
	public void setDscTipoImpressao(String dscTipoImpressao) {
		this.dscTipoImpressao = dscTipoImpressao;
	}
	public String getCadastroRatificado() {
		return cadastroRatificado;
	}
	public void setCadastroRatificado(String cadastroRatificado) {
		this.cadastroRatificado = cadastroRatificado;
	}
	public String getPrerrogativaIntimacao() {
		return prerrogativaIntimacao;
	}
	public void setPrerrogativaIntimacao(String prerrogativaIntimacao) {
		this.prerrogativaIntimacao = prerrogativaIntimacao;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getTipoPessoa() {
		return tipoPessoa;
	}
	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	public String getQualificacao() {
		return qualificacao;
	}
	public void setQualificacao(String qualificacao) {
		this.qualificacao = qualificacao;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ParteDTO) {
			if (this.id.equals(((ParteDTO) obj).getId())) {
				return true;
			}			
		}
		return false;

	}
	
	@Override
    public int hashCode() {
        return id.hashCode();
    }
}