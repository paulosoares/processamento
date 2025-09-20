package br.gov.stf.estf.intimacao.visao.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Lembrete;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.Usuario;

public class PecaDTO {

    private boolean checked;

    private Long id;
    private Long numeroPagInicio;
    private Long numeroPagFim;
    private Long numeroOrdemPeca;
    private Long seqDocumentoPeca;
    private Long seqDocumentoAcordaoPeca;
    private Integer andamentoProtocolo;
    private Setor setor;
    private String tipoOrigemPeca;
    private String descricaoPeca;
    private List<ArquivoProcessoEletronico> documentos;
    private Set<Lembrete> lembretes = new HashSet<Lembrete>();
    private TipoSituacaoPeca tipoSituacaoPeca;
    private TipoPecaProcesso tipoPecaProcesso;
    private Set<ArquivoProcessoEletronico> documentosEletronicos = new HashSet<ArquivoProcessoEletronico>();
    private String urlDownloadPeca;
    private Boolean intimado;

    private Date dataInclusao;
    private Usuario usuarioInclusao;
    private Date dataAlteracao;
    private Usuario usuarioAlteracao;

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Usuario getUsuarioAlteracao() {
        return usuarioAlteracao;
    }

    public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }
    private ObjetoIncidente<?> objetoIncidente;

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

    public Long getNumeroPagInicio() {
        return numeroPagInicio;
    }

    public void setNumeroPagInicio(Long numeroPagInicio) {
        this.numeroPagInicio = numeroPagInicio;
    }

    public Long getNumeroPagFim() {
        return numeroPagFim;
    }

    public void setNumeroPagFim(Long numeroPagFim) {
        this.numeroPagFim = numeroPagFim;
    }

    public Long getNumeroOrdemPeca() {
        return numeroOrdemPeca;
    }

    public void setNumeroOrdemPeca(Long numeroOrdemPeca) {
        this.numeroOrdemPeca = numeroOrdemPeca;
    }

    public Integer getAndamentoProtocolo() {
        return andamentoProtocolo;
    }

    public void setAndamentoProtocolo(Integer andamentoProtocolo) {
        this.andamentoProtocolo = andamentoProtocolo;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public String getTipoOrigemPeca() {
        return tipoOrigemPeca;
    }

    public void setTipoOrigemPeca(String tipoOrigemPeca) {
        this.tipoOrigemPeca = tipoOrigemPeca;
    }

    public String getDescricaoPeca() {
        return descricaoPeca;
    }

    public void setDescricaoPeca(String descricaoPeca) {
        this.descricaoPeca = descricaoPeca;
    }

    public List<ArquivoProcessoEletronico> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<ArquivoProcessoEletronico> documentos) {
        this.documentos = documentos;
    }

    public Set<Lembrete> getLembretes() {
        return lembretes;
    }

    public void setLembretes(Set<Lembrete> lembretes) {
        this.lembretes = lembretes;
    }

    public TipoSituacaoPeca getTipoSituacaoPeca() {
        return tipoSituacaoPeca;
    }

    public void setTipoSituacaoPeca(TipoSituacaoPeca tipoSituacaoPeca) {
        this.tipoSituacaoPeca = tipoSituacaoPeca;
    }

    public TipoPecaProcesso getTipoPecaProcesso() {
        return tipoPecaProcesso;
    }

    public void setTipoPecaProcesso(TipoPecaProcesso tipoPecaProcesso) {
        this.tipoPecaProcesso = tipoPecaProcesso;
    }

    public String getUrlDownloadPeca() {
        return urlDownloadPeca;
    }

    public void setUrlDownloadPeca(String urlDownloadPeca) {
        this.urlDownloadPeca = urlDownloadPeca;
    }

    public Set<ArquivoProcessoEletronico> getDocumentosEletronicos() {
        return documentosEletronicos;
    }

    public void setDocumentosEletronicos(Set<ArquivoProcessoEletronico> documentosEletronicos) {
        this.documentosEletronicos = documentosEletronicos;
    }

    public ObjetoIncidente<?> getObjetoIncidente() {
        return objetoIncidente;
    }

    public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
        this.objetoIncidente = objetoIncidente;
    }

	public Long getSeqDocumentoPeca() {
		return seqDocumentoPeca;
	}

	public void setSeqDocumentoPeca(Long seqDocumentoPeca) {
		this.seqDocumentoPeca = seqDocumentoPeca;
	}
    
	public Long getSeqDocumentoAcordaoPeca() {
		return seqDocumentoAcordaoPeca;
	}

	public void setSeqDocumentoAcordaoPeca(Long seqDocumentoPeca) {
		this.seqDocumentoAcordaoPeca = seqDocumentoPeca;
	}

	public Boolean getIntimado() {
		return intimado;
	}

	public void setIntimado(Boolean intimado) {
		this.intimado = intimado;
	}

	@Override
	public String toString() {
		Processo processo = (Processo) objetoIncidente;
		String resultado = processo.getClasseProcessual().getId()
                + " "
                + processo.getNumeroProcessual();
		return resultado;
	}
}