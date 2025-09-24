/**
 * 
 */
package br.jus.stf.estf.decisao.pesquisa.web;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Paulo.Estevao
 * @since 08.09.2010
 */
@XmlRootElement
public class PesquisaXMLBind {

	// Processo
	private String identificacaoProcesso;
	private Long idObjetoIncidente;
	private Boolean objetoIncidenteFake;
	private String siglaProcesso;
	private Long numeroProcesso;
	private String nomeRelatorAtual;
	private Long idRelatorAtual;
	private Long idTipoIncidente;
	private String originario;
	private String repercussaoGeral;
	private String controversiaOrigem;
	private String tipoProcesso;
	private String nomeListaIncidentes;
	private Long idListaIncidentes;
	private String situacaoJulgamento;
	
	// Julgamento
	private String agendamento;
	private Long colegiado;
	private Date inicioDataSessaoJulgamento;
	private Date fimDataSessaoJulgamento;
	private String pautaExtra;
	private Long idListaJulgamento;
	private String controleVoto;
	
	// Texto
	private String nomeMinistroTexto;
	private Long idMinistroTexto;
	private Long idTipoTexto;
	private List<Long> tiposTexto;
	private Date inicioDataInclusao;
	private Date fimDataInclusao;
	private Date inicioDataSessao;
	private Date fimDataSessao;
	private String textosIguais;
	private Long idFaseTexto;
	private Date inicioDataFase;
	private Date fimDataFase;
	private Boolean ultimaFase;
	private String nomeResponsavel;
	private String idResponsavel;
	private String nomeListaTextos;
	private Long idListaTextos;
	private String palavraChave;
	private String observacao;
	
	// Assunto
	private String idAssunto;
	private String descricaoAssunto;
	
	// Parte
	private Long idCategoriaParte;
	private String nomeParte;
	
	// Outras Opções
	private String ordenacao;
	private Boolean painelVisualizacao;
	
	// Tipo de pesquisa
	private Class tipoPesquisa;
	
	// Quantidade de registros por página
	private Integer pageSize;
	
	
	// GETTERS AND SETTERS
	
	public String getIdentificacaoProcesso() {
		return identificacaoProcesso;
	}

	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		this.identificacaoProcesso = identificacaoProcesso;
	}

	public Long getIdObjetoIncidente() {
		return idObjetoIncidente;
	}

	public void setIdObjetoIncidente(Long idObjetoIncidente) {
		this.idObjetoIncidente = idObjetoIncidente;
	}
	
	public Boolean getObjetoIncidenteFake() {
		return objetoIncidenteFake;
	}

	public void setObjetoIncidenteFake(Boolean objetoIncidenteFake) {
		this.objetoIncidenteFake = objetoIncidenteFake;
	}

	public String getSiglaProcesso() {
		return siglaProcesso;
	}

	public void setSiglaProcesso(String siglaProcesso) {
		this.siglaProcesso = siglaProcesso;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getNomeRelatorAtual() {
		return nomeRelatorAtual;
	}

	public void setNomeRelatorAtual(String nomeRelatorAtual) {
		this.nomeRelatorAtual = nomeRelatorAtual;
	}

	public Long getIdRelatorAtual() {
		return idRelatorAtual;
	}

	public void setIdRelatorAtual(Long idRelatorAtual) {
		this.idRelatorAtual = idRelatorAtual;
	}

	public Long getIdTipoIncidente() {
		return idTipoIncidente;
	}

	public void setIdTipoIncidente(Long idTipoIncidente) {
		this.idTipoIncidente = idTipoIncidente;
	}

	public String getOriginario() {
		return originario;
	}

	public void setOriginario(String originario) {
		this.originario = originario;
	}

	public String getRepercussaoGeral() {
		return repercussaoGeral;
	}

	public void setRepercussaoGeral(String repercussaoGeral) {
		this.repercussaoGeral = repercussaoGeral;
	}

	public String getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(String tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public String getNomeListaIncidentes() {
		return nomeListaIncidentes;
	}

	public void setNomeListaIncidentes(String nomeListaIncidentes) {
		this.nomeListaIncidentes = nomeListaIncidentes;
	}

	public Long getIdListaIncidentes() {
		return idListaIncidentes;
	}

	public void setIdListaIncidentes(Long idListaIncidentes) {
		this.idListaIncidentes = idListaIncidentes;
	}
	
	public String getSituacaoJulgamento() {
		return situacaoJulgamento;
	}

	public void setSituacaoJulgamento(String situacaoJulgamento) {
		this.situacaoJulgamento = situacaoJulgamento;
	}

	public String getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(String agendamento) {
		this.agendamento = agendamento;
	}

	public Long getColegiado() {
		return colegiado;
	}

	public void setColegiado(Long colegiado) {
		this.colegiado = colegiado;
	}

	public String getNomeMinistroTexto() {
		return nomeMinistroTexto;
	}

	public void setNomeMinistroTexto(String nomeMinistroTexto) {
		this.nomeMinistroTexto = nomeMinistroTexto;
	}

	public Long getIdMinistroTexto() {
		return idMinistroTexto;
	}

	public void setIdMinistroTexto(Long idMinistroTexto) {
		this.idMinistroTexto = idMinistroTexto;
	}

	public Long getIdTipoTexto() {
		return idTipoTexto;
	}

	public void setIdTipoTexto(Long idTipoTexto) {
		this.idTipoTexto = idTipoTexto;
	}

	public List<Long> getTiposTexto() {
		return tiposTexto;
	}

	public void setTiposTexto(List<Long> tiposTexto) {
		this.tiposTexto = tiposTexto;
	}

	public Date getInicioDataInclusao() {
		return inicioDataInclusao;
	}

	public void setInicioDataInclusao(Date inicioDataInclusao) {
		this.inicioDataInclusao = inicioDataInclusao;
	}

	public Date getFimDataInclusao() {
		return fimDataInclusao;
	}

	public void setFimDataInclusao(Date fimDataInclusao) {
		this.fimDataInclusao = fimDataInclusao;
	}

	public Date getInicioDataSessao() {
		return inicioDataSessao;
	}

	public void setInicioDataSessao(Date inicioDataSessao) {
		this.inicioDataSessao = inicioDataSessao;
	}

	public Date getFimDataSessao() {
		return fimDataSessao;
	}

	public void setFimDataSessao(Date fimDataSessao) {
		this.fimDataSessao = fimDataSessao;
	}

	public String getTextosIguais() {
		return textosIguais;
	}

	public void setTextosIguais(String textosIguais) {
		this.textosIguais = textosIguais;
	}

	public Long getIdFaseTexto() {
		return idFaseTexto;
	}

	public void setIdFaseTexto(Long idFaseTexto) {
		this.idFaseTexto = idFaseTexto;
	}

	public Date getInicioDataFase() {
		return inicioDataFase;
	}

	public void setInicioDataFase(Date inicioDataFase) {
		this.inicioDataFase = inicioDataFase;
	}

	public Date getFimDataFase() {
		return fimDataFase;
	}

	public void setFimDataFase(Date fimDataFase) {
		this.fimDataFase = fimDataFase;
	}

	public Boolean getUltimaFase() {
		return ultimaFase;
	}

	public void setUltimaFase(Boolean ultimaFase) {
		this.ultimaFase = ultimaFase;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getIdResponsavel() {
		return idResponsavel;
	}

	public void setIdResponsavel(String idResponsavel) {
		this.idResponsavel = idResponsavel;
	}

	public String getNomeListaTextos() {
		return nomeListaTextos;
	}

	public void setNomeListaTextos(String nomeListaTextos) {
		this.nomeListaTextos = nomeListaTextos;
	}

	public Long getIdListaTextos() {
		return idListaTextos;
	}

	public void setIdListaTextos(Long idListaTextos) {
		this.idListaTextos = idListaTextos;
	}

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getIdAssunto() {
		return idAssunto;
	}

	public void setIdAssunto(String idAssunto) {
		this.idAssunto = idAssunto;
	}

	public String getDescricaoAssunto() {
		return descricaoAssunto;
	}

	public void setDescricaoAssunto(String descricaoAssunto) {
		this.descricaoAssunto = descricaoAssunto;
	}

	public Long getIdCategoriaParte() {
		return idCategoriaParte;
	}

	public void setIdCategoriaParte(Long idCategoriaParte) {
		this.idCategoriaParte = idCategoriaParte;
	}

	public String getNomeParte() {
		return nomeParte;
	}

	public void setNomeParte(String nomeParte) {
		this.nomeParte = nomeParte;
	}

	public String getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Boolean getPainelVisualizacao() {
		return painelVisualizacao;
	}

	public void setPainelVisualizacao(Boolean painelVisualizacao) {
		this.painelVisualizacao = painelVisualizacao;
	}

	public Class getTipoPesquisa() {
		return tipoPesquisa;
	}

	public void setTipoPesquisa(Class tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Date getInicioDataSessaoJulgamento() {
		return inicioDataSessaoJulgamento;
	}
	
	public void setInicioDataSessaoJulgamento(Date inicioDataSessaoJulgamento) {
		this.inicioDataSessaoJulgamento = inicioDataSessaoJulgamento;
	}
	
	public Date getFimDataSessaoJulgamento() {
		return fimDataSessaoJulgamento;
	}
	
	public void setFimDataSessaoJulgamento(Date fimDataSessaoJulgamento) {
		this.fimDataSessaoJulgamento = fimDataSessaoJulgamento;
	}
	
	public String getPautaExtra() {
		return pautaExtra;
	}
	
	public void setPautaExtra(String pautaExtra) {
		this.pautaExtra = pautaExtra;
	}

	public Long getIdListaJulgamento() {
		return idListaJulgamento;
	}

	public void setIdListaJulgamento(Long idListaJulgamento) {
		this.idListaJulgamento = idListaJulgamento;
	}

	public String getControversiaOrigem() {
		return controversiaOrigem;
	}

	public void setControversiaOrigem(String controversiaOrigem) {
		this.controversiaOrigem = controversiaOrigem;
	}

	public String getControleVoto() {
		return controleVoto;
	}

	public void setControleVoto(String controleVoto) {
		this.controleVoto = controleVoto;
	}

}
