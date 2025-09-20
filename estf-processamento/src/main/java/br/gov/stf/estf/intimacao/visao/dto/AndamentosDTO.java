package br.gov.stf.estf.intimacao.visao.dto;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.TipoDevolucao;

public class AndamentosDTO {

	private boolean checked;	
	private Long id;
	private Long codigoAndamento;
	private Andamento tipoAndamento;
	private Date dataAndamento;
	private ObjetoIncidente objetoIncidente;
	private Date dataHoraSistema;
	private String descricaoObservacaoAndamento;
	private Long numeroSequencia;
	private Setor setor;
	private String codigoUsuario;
	private String descricaoObservacaoInterna;
	private OrigemAndamentoDecisao origemAndamentoDecisao;
	private Long numeroSequenciaErrado;
	private Boolean ultimoAndamento;
	private Boolean lancamentoIndevido;
	private String sigClasseProces;
	private Long numProcesso;
	private Ministro presidenteInterino;
	private TipoDevolucao tipoDevolucao;
	private Long recurso;
	private List<TextoAndamentoProcesso> listaTextoAndamentoProcessos;
	
	private Date dataInclusao;
	private Date dataAlteracao;
	private String usuarioInclusao;
	private String usuarioAlteracao;
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
	public Long getCodigoAndamento() {
		return codigoAndamento;
	}
	public void setCodigoAndamento(Long codigoAndamento) {
		this.codigoAndamento = codigoAndamento;
	}
	public Andamento getTipoAndamento() {
		return tipoAndamento;
	}
	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}
	public Date getDataAndamento() {
		return dataAndamento;
	}
	public void setDataAndamento(Date dataAndamento) {
		this.dataAndamento = dataAndamento;
	}
	public ObjetoIncidente getObjetoIncidente() {
		return objetoIncidente;
	}
	public void setObjetoIncidente(ObjetoIncidente objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	public Date getDataHoraSistema() {
		return dataHoraSistema;
	}
	public void setDataHoraSistema(Date dataHoraSistema) {
		this.dataHoraSistema = dataHoraSistema;
	}
	public String getDescricaoObservacaoAndamento() {
		return descricaoObservacaoAndamento;
	}
	public void setDescricaoObservacaoAndamento(String descricaoObservacaoAndamento) {
		this.descricaoObservacaoAndamento = descricaoObservacaoAndamento;
	}
	public Long getNumeroSequencia() {
		return numeroSequencia;
	}
	public void setNumeroSequencia(Long numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}
	public Setor getSetor() {
		return setor;
	}
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	public String getDescricaoObservacaoInterna() {
		return descricaoObservacaoInterna;
	}
	public void setDescricaoObservacaoInterna(String descricaoObservacaoInterna) {
		this.descricaoObservacaoInterna = descricaoObservacaoInterna;
	}
	public OrigemAndamentoDecisao getOrigemAndamentoDecisao() {
		return origemAndamentoDecisao;
	}
	public void setOrigemAndamentoDecisao(
			OrigemAndamentoDecisao origemAndamentoDecisao) {
		this.origemAndamentoDecisao = origemAndamentoDecisao;
	}
	public Long getNumeroSequenciaErrado() {
		return numeroSequenciaErrado;
	}
	public void setNumeroSequenciaErrado(Long numeroSequenciaErrado) {
		this.numeroSequenciaErrado = numeroSequenciaErrado;
	}
	public Boolean getUltimoAndamento() {
		return ultimoAndamento;
	}
	public void setUltimoAndamento(Boolean ultimoAndamento) {
		this.ultimoAndamento = ultimoAndamento;
	}
	public Boolean getLancamentoIndevido() {
		return lancamentoIndevido;
	}
	public void setLancamentoIndevido(Boolean lancamentoIndevido) {
		this.lancamentoIndevido = lancamentoIndevido;
	}
	public String getSigClasseProces() {
		return sigClasseProces;
	}
	public void setSigClasseProces(String sigClasseProces) {
		this.sigClasseProces = sigClasseProces;
	}
	public Long getNumProcesso() {
		return numProcesso;
	}
	public void setNumProcesso(Long numProcesso) {
		this.numProcesso = numProcesso;
	}
	public Ministro getPresidenteInterino() {
		return presidenteInterino;
	}
	public void setPresidenteInterino(Ministro presidenteInterino) {
		this.presidenteInterino = presidenteInterino;
	}
	public TipoDevolucao getTipoDevolucao() {
		return tipoDevolucao;
	}
	public void setTipoDevolucao(TipoDevolucao tipoDevolucao) {
		this.tipoDevolucao = tipoDevolucao;
	}
	public Long getRecurso() {
		return recurso;
	}
	public void setRecurso(Long recurso) {
		this.recurso = recurso;
	}
	public List<TextoAndamentoProcesso> getListaTextoAndamentoProcessos() {
		return listaTextoAndamentoProcessos;
	}
	public void setListaTextoAndamentoProcessos(
			List<TextoAndamentoProcesso> listaTextoAndamentoProcessos) {
		this.listaTextoAndamentoProcessos = listaTextoAndamentoProcessos;
	}
	public Date getDataInclusao() {
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	public String getUsuarioInclusao() {
		return usuarioInclusao;
	}
	public void setUsuarioInclusao(String usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}
	public String getUsuarioAlteracao() {
		return usuarioAlteracao;
	}
	public void setUsuarioAlteracao(String usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}
		
}
