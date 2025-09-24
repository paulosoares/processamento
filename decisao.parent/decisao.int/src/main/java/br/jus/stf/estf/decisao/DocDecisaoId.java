package br.jus.stf.estf.decisao;

import java.io.Serializable;
import java.net.URL;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.jus.stf.stfoffice.PluginActionHandler;
import br.jus.stf.stfoffice.servlet.DocumentoId;

public abstract class DocDecisaoId extends DocumentoId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7241060742317486308L;

	public static final String ACAO_ABRIR_DOCUMENTO = "abrirDocumento";
	public static final String ACAO_NOVO_DOCUMENTO = "novoDocumento";
	public static final String ACAO_SALVAR_DOCUMENTO = PluginActionHandler.ACTION_SALVAR;
	public static final String ACAO_GERAR_PDF = "gerarPDF";
	public static final String ACAO_FECHAR_DOCUMENTO = PluginActionHandler.ACTION_FECHAR;

	private URL wsEndpoint;
	private Long seqTexto;
	private String userId;

	private Long seqFaseTextoProcesso;
	private Long codigoSetor;
	private Long objetoIncidente;
	private Long tipoTexto;
	private Boolean rodape;
	private Boolean somenteLeitura = Boolean.FALSE;
	private String tipoVotoId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public URL getWsEndpoint() {
		return wsEndpoint;
	}

	public void setWsEndpoint(URL wsEndpoint) {
		this.wsEndpoint = wsEndpoint;
	}

	public Long getSeqTexto() {
		return seqTexto;
	}

	public void setSeqTexto(Long seqTexto) {
		this.seqTexto = seqTexto;
	}

	public Long getSeqFaseTextoProcesso() {
		return seqFaseTextoProcesso;
	}

	public void setSeqFaseTextoProcesso(Long seqFaseTextoProcesso) {
		this.seqFaseTextoProcesso = seqFaseTextoProcesso;
	}

	public Long getCodigoSetor() {
		return codigoSetor;
	}

	public void setCodigoSetor(Long codigoSetor) {
		this.codigoSetor = codigoSetor;
	}

	public Boolean getRodape() {
		return rodape;
	}

	public void setRodape(Boolean rodape) {
		this.rodape = rodape;
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public Long getTipoTexto() {
		return tipoTexto;
	}

	public void setTipoTexto(Long tipoTexto) {
		this.tipoTexto = tipoTexto;
	}
	
	public Boolean getSomenteLeitura() {
		return somenteLeitura;
	}

	public void setSomenteLeitura(Boolean somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}

	public int compareTo(DocumentoId o) {
		if (o == null || !(o instanceof DocDecisaoId))
			return -1;
		DocDecisaoId d = (DocDecisaoId) o;
		CompareToBuilder cb = new CompareToBuilder();
		cb.append(seqTexto, d.seqTexto);
		cb.append(seqFaseTextoProcesso, d.seqFaseTextoProcesso);
		cb.append(objetoIncidente, d.objetoIncidente);
		cb.append(codigoSetor, d.codigoSetor);
		cb.append(tipoTexto, d.tipoTexto);
		cb.append(rodape, d.rodape);
		return cb.toComparison();

	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(seqTexto).append(seqFaseTextoProcesso).append(objetoIncidente)
				.append(codigoSetor).append(tipoTexto).append(rodape).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DocDecisaoId))
			return false;
		DocDecisaoId d = (DocDecisaoId) obj;
		return new EqualsBuilder().append(seqTexto, d.seqTexto).append(seqFaseTextoProcesso, d.seqFaseTextoProcesso)
				.append(objetoIncidente, d.objetoIncidente).append(codigoSetor, d.codigoSetor)
				.append(tipoTexto, d.tipoTexto).append(rodape, d.rodape).isEquals();
	}
	
	public String getTipoVotoId() {
		return tipoVotoId;
	}

	public void setTipoVotoId(String tipoVotoId) {
		this.tipoVotoId = tipoVotoId;
	}

}
