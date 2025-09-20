package br.gov.stf.estf.publicacao.model.util;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import br.gov.stf.estf.entidade.processostf.ProcessoImagem.TipoColecao;

public class ProcessoPublicadoVO {

	private int codigoMateria;
	private int codigoCapitulo;
	private int numeroMateria;
	private int anoMateria;
	private int seqArquivoEletronico;
	private int objetoIncidente;

	private Date dataCriacaoMateria;
	private Date dataPublicacaoDj;
	private Date dataDivulgacaoDje;

	private String tipoColecao;

	private String tipoObjetoIncidente;
	private String tipoMeioProcesso;
	private String siglaClasse;
	private String complementoClasse;

	private Long idProcessoPublicado;
	private Long seqObjetoIncidente;
	private Long numeroProcesso;

	// Utilizado pelo eSTF-Publicação
	public ProcessoPublicadoVO(ResultSet rs) throws SQLException {
		codigoMateria = rs.getInt("cod_materia");
		codigoCapitulo = rs.getInt("cod_capitulo");
		numeroMateria = rs.getInt("num_materia");
		anoMateria = rs.getInt("ano_materia");
		objetoIncidente = rs.getInt("seq_objeto_incidente");
		dataCriacaoMateria = rs.getDate("dat_criacao");
		tipoMeioProcesso = rs.getString("tip_meio_processo");
		seqArquivoEletronico = rs.getInt("seq_arquivo_eletronico_texto");
		tipoObjetoIncidente = rs.getString("tip_objeto_incidente");
		idProcessoPublicado = rs.getLong("seq_processo_publicados");
	}

	public ProcessoPublicadoVO() {

	}

	public String getIdentificacaoProcesso() {
		String complementoClasse = getComplementoClasse();
		return getSiglaClasse() + " " + getNumeroProcesso() + (StringUtils.isNotBlank(complementoClasse) ? " " + complementoClasse : "");
	}

	public String getNumeroAnoMateria() {
		return getNumeroMateria() + "/" + getAnoMateria();
	}

	public boolean isPublicadoInternet() {
		return getDataDivulgacaoDje() != null;
	}

	public int getCodigoMateria() {
		return codigoMateria;
	}

	public void setCodigoMateria(int codigoMateria) {
		this.codigoMateria = codigoMateria;
	}

	public int getCodigoCapitulo() {
		return codigoCapitulo;
	}

	public void setCodigoCapitulo(int codigoCapitulo) {
		this.codigoCapitulo = codigoCapitulo;
	}

	public int getNumeroMateria() {
		return numeroMateria;
	}

	public void setNumeroMateria(int numeroMateria) {
		this.numeroMateria = numeroMateria;
	}

	public int getAnoMateria() {
		return anoMateria;
	}

	public void setAnoMateria(int anoMateria) {
		this.anoMateria = anoMateria;
	}

	public Date getDataCriacaoMateria() {
		return dataCriacaoMateria;
	}

	public void setDataCriacaoMateria(Date dataCriacaoMateria) {
		this.dataCriacaoMateria = dataCriacaoMateria;
	}

	public String getTipoMeioProcesso() {
		return tipoMeioProcesso;
	}

	public void setTipoMeioProcesso(String tipoMeioProcesso) {
		this.tipoMeioProcesso = tipoMeioProcesso;
	}

	public int getSeqArquivoEletronico() {
		return seqArquivoEletronico;
	}

	public void setSeqArquivoEletronico(int seqArquivoEletronico) {
		this.seqArquivoEletronico = seqArquivoEletronico;
	}

	public int getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(int objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public String getTipoObjetoIncidente() {
		return tipoObjetoIncidente;
	}

	public void setTipoObjetoIncidente(String tipoObjetoIncidente) {
		this.tipoObjetoIncidente = tipoObjetoIncidente;
	}

	public Long getIdProcessoPublicado() {
		return idProcessoPublicado;
	}

	public void setIdProcessoPublicado(Long idProcessoPublicado) {
		this.idProcessoPublicado = idProcessoPublicado;
	}

	public Date getDataPublicacaoDj() {
		return dataPublicacaoDj;
	}

	public void setDataPublicacaoDj(Date dataPublicacaoDj) {
		this.dataPublicacaoDj = dataPublicacaoDj;
	}

	public Date getDataDivulgacaoDje() {
		return dataDivulgacaoDje;
	}

	public void setDataDivulgacaoDje(Date dataDivulgacaoDje) {
		this.dataDivulgacaoDje = dataDivulgacaoDje;
	}

	public String getTipoColecao() {
		return tipoColecao;
	}

	public TipoColecao getTipoColecaoObject() {
		return getTipoColecao() != null ? TipoColecao.valueOf(getTipoColecao()) : null;
	}

	public void setTipoColecao(String tipoColecao) {
		this.tipoColecao = tipoColecao;
	}

	public String getSiglaClasse() {
		return siglaClasse;
	}

	public void setSiglaClasse(String siglaClasse) {
		this.siglaClasse = siglaClasse;
	}

	public String getComplementoClasse() {
		return complementoClasse;
	}

	public void setComplementoClasse(String complementoClasse) {
		this.complementoClasse = complementoClasse;
	}

	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}

	public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
}
