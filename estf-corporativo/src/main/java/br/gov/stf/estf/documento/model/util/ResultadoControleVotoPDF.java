package br.gov.stf.estf.documento.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;

public class ResultadoControleVotoPDF {
	private ControleVoto controleVoto;
	private DocumentoTexto documentoTexto;
	private Date dataPublicacaoAcordao;
	private Setor localizacaoAtualProcesso;
	private Long seqInteiroTeor;
	private String descricaoStatusDocumento;
	public ResultadoControleVotoPDF(ControleVoto controleVoto, DocumentoTexto documentoTexto, Date dataPublicacaoAcordao) {
		super();
		this.controleVoto = controleVoto;
		this.documentoTexto = documentoTexto;
		this.dataPublicacaoAcordao = dataPublicacaoAcordao;
	}
	
	public ResultadoControleVotoPDF(ControleVoto controleVoto, DocumentoTexto documentoTexto, Date dataPublicacaoAcordao, Setor localizacaoAtualProcesso, Long seqInteiroTeor) {
		super();
		this.controleVoto = controleVoto;
		this.documentoTexto = documentoTexto;
		this.dataPublicacaoAcordao = dataPublicacaoAcordao;
		this.localizacaoAtualProcesso = localizacaoAtualProcesso;
		this.seqInteiroTeor = seqInteiroTeor;
		this.descricaoStatusDocumento = null;
	}
	
	public ResultadoControleVotoPDF(ControleVoto controleVoto, DocumentoTexto documentoTexto, Date dataPublicacaoAcordao, Setor localizacaoAtualProcesso, Long seqInteiroTeor, String descricaoStatusDocumento) {
		super();
		this.controleVoto = controleVoto;
		this.documentoTexto = documentoTexto;
		this.dataPublicacaoAcordao = dataPublicacaoAcordao;
		this.localizacaoAtualProcesso = localizacaoAtualProcesso;
		this.seqInteiroTeor = seqInteiroTeor;
		this.descricaoStatusDocumento = descricaoStatusDocumento;
	}
	
	public Date getDataPublicacaoAcordao() {
		return dataPublicacaoAcordao;
	}
	public void setDataPublicacaoAcordao(Date dataPublicacaoAcordao) {
		this.dataPublicacaoAcordao = dataPublicacaoAcordao;
	}
	public ControleVoto getControleVoto() {
		return controleVoto;
	}
	public void setControleVoto(ControleVoto controleVoto) {
		this.controleVoto = controleVoto;
	}
	public DocumentoTexto getDocumentoTexto() {
		return documentoTexto;
	}
	public void setDocumentoTexto(DocumentoTexto documentoTexto) {
		this.documentoTexto = documentoTexto;
	}
	
	public Setor getLocalizacaoAtualProcessoFisico() {
		return localizacaoAtualProcesso;
	}
	
	public void setLocalizacaoAtualProcessoFisico(Setor localizacaoAtualProcessoFisico) {
		this.localizacaoAtualProcesso = localizacaoAtualProcessoFisico;
	}

	public Long getSeqInteiroTeor() {
		return seqInteiroTeor;
	}

	public void setSeqInteiroTeor(Long seqInteiroTeor) {
		this.seqInteiroTeor = seqInteiroTeor;
	}
	
	public String getDescricaoStatusDocumento() {
		return descricaoStatusDocumento;
	}
	
	public void setDescricaoStatusDocumento(String descricaoStatusDocumento) {
		this.descricaoStatusDocumento = descricaoStatusDocumento;
	}
}
