package br.gov.stf.estf.intimacao.visao.dto;

public class PecaIntimacaoDto {

    private Long seqPecaProcessoEletronico;
    private long codigoMateria;
    private long numeroMateria;
    private long seqDocumento;
    private Long seqDocumentoAcordao;
    private Long idTipoPecaProcessual;
    private String descricaoTipoPecaProcessual;
    private boolean intimado;
    

    public PecaIntimacaoDto() {
    }

    public Long getSeqPecaProcessoEletronico() {
        return seqPecaProcessoEletronico;
    }

    public void setSeqPecaProcessoEletronico(Long seqPecaProcessoEletronico) {
        this.seqPecaProcessoEletronico = seqPecaProcessoEletronico;
    }

    public long getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(long codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public long getNumeroMateria() {
        return numeroMateria;
    }

    public void setNumeroMateria(long numeroMateria) {
        this.numeroMateria = numeroMateria;
    }

    public long getSeqDocumento() {
        return seqDocumento;
    }

    public void setSeqDocumento(long seqDocumento) {
        this.seqDocumento = seqDocumento;
    }

    public Long getSeqDocumentoAcordao() {
        return seqDocumentoAcordao;
    }

    public void setSeqDocumentoAcordao(Long seqDocumentoAcordao) {
        this.seqDocumentoAcordao = seqDocumentoAcordao;
    }

    public Long getIdTipoPecaProcessual() {
        return idTipoPecaProcessual;
    }

    public void setIdTipoPecaProcessual(Long idTipoPecaProcessual) {
        this.idTipoPecaProcessual = idTipoPecaProcessual;
    }

    public String getDescricaoTipoPecaProcessual() {
        return descricaoTipoPecaProcessual;
    }

    public void setDescricaoTipoPecaProcessual(String descricaoTipoPecaProcessual) {
        this.descricaoTipoPecaProcessual = descricaoTipoPecaProcessual;
    }

    public boolean isIntimado() {
        return intimado;
    }

    public void setIntimado(boolean intimado) {
        this.intimado = intimado;
    }
    
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PecaIntimacaoDto) {
			if (this.seqPecaProcessoEletronico.equals(((PecaIntimacaoDto) obj).getSeqPecaProcessoEletronico())) {
				return true;
			}			
		}
		return false;
	}
	
	@Override
    public int hashCode() {
        return seqPecaProcessoEletronico.hashCode();
    }

}
