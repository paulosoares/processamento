package br.gov.stf.estf.entidade;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.usuario.PessoaEmail;
import br.gov.stf.estf.entidade.usuario.PessoaTelefone;
import br.gov.stf.estf.util.DataUtil;

public class InscricoesJulgamento {

	private Boolean sustentacaoOralPresencial;
	private Boolean participacaoJulgamentoPresencial;
	private Date dataEnvio;
	private String advogado;
	private String parte;
	private Long seqObjetoIncidente;
	private String siglaClasseProcesso;
	private Long numeroProcesso;
	private String relator;
	private Long sessaoId;
	private String colegiado;
	private Long ordemPeca;
	private String descricaoPeca;
	private Long seqDocumento;
	private Long pessoaAdvogado;
	private List<PessoaTelefone> advogadoTelefones;
	private List<PessoaEmail> advogadoEmails;


	public InscricoesJulgamento(Boolean sustentacaoOralPresencial,
			Boolean participacaoJulgamentoPresencial, Date dataEnvio,
			String advogado, String parte, Long seqObjetoIncidente, 
			String siglaClasseProcesso,
			Long numeroProcesso, String relator, 
			Long sessaoId, String colegiado, Long ordemPeca,
			String descricaoPeca, Long seqDocumento, Long pessoaAdvogado) {
		super();
		this.sustentacaoOralPresencial = sustentacaoOralPresencial;
		this.participacaoJulgamentoPresencial = participacaoJulgamentoPresencial;
		this.dataEnvio = dataEnvio;
		this.advogado = advogado;
		this.parte = parte;
		this.seqObjetoIncidente = seqObjetoIncidente;
		this.siglaClasseProcesso = siglaClasseProcesso;
		this.numeroProcesso = numeroProcesso;
		this.relator = relator;
		this.sessaoId = sessaoId;
		this.colegiado = colegiado;
		this.ordemPeca = ordemPeca;
		this.descricaoPeca = descricaoPeca;
		this.seqDocumento = seqDocumento;
		this.pessoaAdvogado = pessoaAdvogado;
	}
	public InscricoesJulgamento(){
		
	}

	public Boolean getSustentacaoOralPresencial() {
		return sustentacaoOralPresencial;
	}

	public void setSustentacaoOralPresencial(Boolean sustentacaoOralPresencial) {
		this.sustentacaoOralPresencial = sustentacaoOralPresencial;
	}

	public Boolean getParticipacaoJulgamentoPresencial() {
		return participacaoJulgamentoPresencial;
	}

	public void setParticipacaoJulgamentoPresencial(
			Boolean participacaoJulgamentoPresencial) {
		this.participacaoJulgamentoPresencial = participacaoJulgamentoPresencial;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getAdvogado() {
		return advogado;
	}

	public void setAdvogado(String advogado) {
		this.advogado = advogado;
	}

	public String getParte() {
		return parte;
	}

	public void setParte(String parte) {
		this.parte = parte;
	}

	public String getSiglaClasseProcesso() {
		return siglaClasseProcesso;
	}
	
	

	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}
	public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}
	public void setSiglaClasseProcesso(String siglaClasseProcesso) {
		this.siglaClasseProcesso = siglaClasseProcesso;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getRelator() {
		return relator;
	}

	public void setRelator(String relator) {
		this.relator = relator;
	}

	public Long getSessaoId() {
		return sessaoId;
	}

	public void setSessaoId(Long sessaoId) {
		this.sessaoId = sessaoId;
	}

	public String getColegiado() {
		return colegiado;
	}

	public void setColegiado(String colegiado) {
		this.colegiado = colegiado;
	}

	public Long getOrdemPeca() {
		return ordemPeca;
	}

	public void setOrdemPeca(Long ordemPeca) {
		this.ordemPeca = ordemPeca;
	}

	public Long getSeqDocumento() {
		return seqDocumento;
	}

	public void setSeqDocumento(Long seqDocumento) {
		this.seqDocumento = seqDocumento;
	}

	public String getDescricaoPeca() {
		return descricaoPeca;
	}

	public void setDescricaoPeca(String descricaoPeca) {
		this.descricaoPeca = descricaoPeca;
	}
	
	

	public Long getPessoaAdvogado() {
		return pessoaAdvogado;
	}
	public void setPessoaAdvogado(Long pessoaAdvogado) {
		this.pessoaAdvogado = pessoaAdvogado;
	}
	
	public List<PessoaTelefone> getAdvogadoTelefones() {
		return advogadoTelefones;
	}
	public void setAdvogadoTelefones(List<PessoaTelefone> advogadoTelefones) {
		this.advogadoTelefones = advogadoTelefones;
	}
	public List<PessoaEmail> getAdvogadoEmails() {
		return advogadoEmails;
	}
	public void setAdvogadoEmails(List<PessoaEmail> advogadoEmails) {
		this.advogadoEmails = advogadoEmails;
	}
	public String getApresentacaoPeca() {
		return this.getOrdemPeca().toString().concat(" - ")
				.concat(this.descricaoPeca);
	}

	public String getTextoSustentacaoOralPresencial() {
		if (sustentacaoOralPresencial)
			return "SUSTENTAÇÃO ORAL";
		return new String();
	}

	public String getTextopPrticipacaoJulgamentoPresencial() {
		if (participacaoJulgamentoPresencial)
			return "APENAS PARTICIPAÇÃO EM JULGAMENTO";
		return new String();
	}

	public String getProcesso() {
		return siglaClasseProcesso.concat(" ")
				.concat(numeroProcesso.toString());
	}

	public String getApresentacaoData() {
		return DataUtil.date2String(dataEnvio, true);
	}

	public String getTipoParticipacao() {
		boolean ambosVazio = (getTextoSustentacaoOralPresencial().isEmpty() && getTextopPrticipacaoJulgamentoPresencial()
				.isEmpty());
		boolean ambosPreenhchidos = (!getTextoSustentacaoOralPresencial()
				.isEmpty() && !getTextopPrticipacaoJulgamentoPresencial()
				.isEmpty());

		if (ambosVazio)
			return new String();
		else if (ambosPreenhchidos)
			return (getTextoSustentacaoOralPresencial().concat("/")
					.concat(getTextopPrticipacaoJulgamentoPresencial()));

		return !getTextoSustentacaoOralPresencial().isEmpty() ? getTextoSustentacaoOralPresencial()
				: getTextopPrticipacaoJulgamentoPresencial();
	}
}
