package br.jus.stf.estf.decisao.objetoincidente.support;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.TipoListaJulgamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.jus.stf.estf.decisao.objetoincidente.web.LiberarParaJulgamentoActionFacesBean.PrevisaoSustentacaoOralDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;

public class DadosAgendamentoDto {

	private ObjetoIncidenteDto objetoIncidenteDto;
	private ListaIncidentesDto listaIncidentesDto;
	private PreListaJulgamento preListaJulgamento;
	private Ministro ministro;
	private TipoAgendamento tipoAgendamento;
	private TipoColegiadoAgendamento tipoColegiadoAgendamento;
	private boolean pautaDirigida;
	private String observacao;
	private Usuario usuario;
	private boolean liberarVoto;
	private Setor setorDoUsuario;
	private TipoListaJulgamento tipoListaJulgamento;
	private List<ObjetoIncidenteDto> julgamentoConjunto;
	private List<ObjetoIncidenteDto> precedentes;
	private boolean repercussaoGeral;
	private boolean pautaExtra;
	private Date dataJulgamentoSugerida;
	private List<PrevisaoSustentacaoOralDto> sustentacoesOrais;
	private String observacaoDataJulgamento;
	private Sessao sessao;
	private List<Sessao> sessoesEmAberto;
	private boolean listaComPedidoDeVista;
	private boolean listaComPedidoDeDestaque;
	private boolean avulso;
	private String nomeLista;
	private Ministro ministroVistor;
	private Ministro ministroDestaque;
	private Long idConclusao;
	private Ministro ministroDoGabinete;
	private String cabecalho;
	private String cabecalhoVistor;
	private String idTipoAmbienteColegiadoEscolhido;
	private Boolean admiteSustentacaoOral;
	private Boolean julgamentoTese;	
	private Boolean julgamentoModulacao;
	private Boolean sessaoExtraordinaria;
	private boolean agendamentoAutomatico = false;
	
	private List<ObjetoIncidente<?>> listaObjetoIncidente; // usado quando os objetos incidentes não vêm de PreListaJulgamento e nem de ListaProcesso
	
	public boolean isLiberarVoto() {
		return liberarVoto;
	}

	public void setLiberarVoto(boolean liberarVoto) {
		this.liberarVoto = liberarVoto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public ObjetoIncidenteDto getObjetoIncidenteDto() {
		return objetoIncidenteDto;
	}

	public void setObjetoIncidenteDto(ObjetoIncidenteDto objetoIncidenteDto) {
		this.objetoIncidenteDto = objetoIncidenteDto;
	}

	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public TipoAgendamento getTipoAgendamento() {
		return tipoAgendamento;
	}

	public void setTipoAgendamento(TipoAgendamento tipoAgendamento) {
		this.tipoAgendamento = tipoAgendamento;
	}

	public TipoColegiadoAgendamento getTipoColegiadoAgendamento() {
		return tipoColegiadoAgendamento;
	}

	public void setTipoColegiadoAgendamento(TipoColegiadoAgendamento tipoColegiadoAgendamento) {
		this.tipoColegiadoAgendamento = tipoColegiadoAgendamento;
	}

	public boolean isPautaDirigida() {
		return pautaDirigida;
	}

	public void setPautaDirigida(boolean pautaDirigida) {
		this.pautaDirigida = pautaDirigida;
	}

	public Setor getSetorDoUsuario() {
		if (setorDoUsuario != null) {
			return setorDoUsuario;
		}
		
		if (getUsuario() != null) {
			return getUsuario().getSetor();
		}
		
		return null;
	}
	
	public void setSetorDoUsuario(Setor setorDoUsuario) {
		this.setorDoUsuario = setorDoUsuario;
	}

	public List<ObjetoIncidenteDto> getJulgamentoConjunto() {
		return julgamentoConjunto;
	}

	public void setJulgamentoConjunto(List<ObjetoIncidenteDto> julgamentoConjunto) {
		this.julgamentoConjunto = julgamentoConjunto;
	}

	public List<ObjetoIncidenteDto> getPrecedentes() {
		return precedentes;
	}

	public void setPrecedentes(List<ObjetoIncidenteDto> precedentes) {
		this.precedentes = precedentes;
	}

	public boolean isRepercussaoGeral() {
		return repercussaoGeral;
	}

	public void setRepercussaoGeral(boolean repercussaoGeral) {
		this.repercussaoGeral = repercussaoGeral;
	}

	public Date getDataJulgamentoSugerida() {
		return dataJulgamentoSugerida;
	}

	public void setDataJulgamentoSugerida(Date dataJulgamentoSugerida) {
		this.dataJulgamentoSugerida = dataJulgamentoSugerida;
	}

	public List<PrevisaoSustentacaoOralDto> getSustentacoesOrais() {
		return sustentacoesOrais;
	}

	public void setSustentacoesOrais(
			List<PrevisaoSustentacaoOralDto> sustentacoesOrais) {
		this.sustentacoesOrais = sustentacoesOrais;
	}
	
	public String getObservacaoDataJulgamento() {
		return observacaoDataJulgamento;
	}

	public void setObservacaoDataJulgamento(String observacaoDataJulgamento) {
		this.observacaoDataJulgamento = observacaoDataJulgamento;
	}
	
	public ListaIncidentesDto getListaIncidentesDto() {
		return listaIncidentesDto;
	}

	public void setListaIncidentesDto(ListaIncidentesDto listaIncidentesDto) {
		this.listaIncidentesDto = listaIncidentesDto;
	}
	
	public Sessao getSessao() {
		return sessao;
	}
	
	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	public boolean isPautaExtra() {
		return pautaExtra;
	}
	
	public void setPautaExtra(boolean pautaExtra) {
		this.pautaExtra = pautaExtra;
	}

	public List<Sessao> getSessoesEmAberto() {
		return sessoesEmAberto;
	}

	public void setSessoesEmAberto(List<Sessao> sessoesEmAberto) {
		this.sessoesEmAberto = sessoesEmAberto;
	}

	public PreListaJulgamento getPreListaJulgamento() {
		return preListaJulgamento;
	}

	public void setPreListaJulgamento(PreListaJulgamento preListaJulgamento) {
		this.preListaJulgamento = preListaJulgamento;
	}

	public TipoListaJulgamento getTipoListaJulgamento() {
		return tipoListaJulgamento;
	}

	public void setTipoListaJulgamento(TipoListaJulgamento tipoListaJulgamento) {
		this.tipoListaJulgamento = tipoListaJulgamento;
	}

	public String getNomeLista() {
		return nomeLista;
	}

	public void setNomeLista(String nomeLista) {
		this.nomeLista = nomeLista;
	}

	public boolean isAvulso() {
		return avulso;
	}

	public void setAvulso(boolean avulso) {
		this.avulso = avulso;
	}

	public boolean isListaComPedidoDeVista() {
		return listaComPedidoDeVista;
	}

	public void setListaComPedidoDeVista(boolean listaComPedidoDeVista) {
		this.listaComPedidoDeVista = listaComPedidoDeVista;
	}

	public Long getIdConclusao() {
		return idConclusao;
	}

	public void setIdConclusao(Long idConclusao) {
		this.idConclusao = idConclusao;
	}

	public Ministro getMinistroVistor() {
		return ministroVistor;
	}

	public void setMinistroVistor(Ministro ministroVistor) {
		this.ministroVistor = ministroVistor;
	}
	public Ministro getMinistroDoGabinete() {
		return ministroDoGabinete;
	}

	public void setMinistroDoGabinete(Ministro ministroDoGabinete) {
		this.ministroDoGabinete = ministroDoGabinete;
	}

	public String getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	public String getIdTipoAmbienteColegiadoEscolhido() {
		return idTipoAmbienteColegiadoEscolhido;
	}

	public void setIdTipoAmbienteColegiadoEscolhido(
			String idTipoAmbienteColegiadoEscolhido) {
		this.idTipoAmbienteColegiadoEscolhido = idTipoAmbienteColegiadoEscolhido;
	}

	public Boolean getAdmiteSustentacaoOral() {
		return admiteSustentacaoOral;
	}

	public void setAdmiteSustentacaoOral(Boolean admiteSustentacaoOral) {
		this.admiteSustentacaoOral = admiteSustentacaoOral;
	}

	public Boolean getJulgamentoTese() {
		return julgamentoTese;
	}

	public void setJulgamentoTese(Boolean julgamentoTese) {
		this.julgamentoTese = julgamentoTese;
	}

	public Boolean getJulgamentoModulacao() {
		return julgamentoModulacao;
	}

	public void setJulgamentoModulacao(Boolean julgamentoModulacao) {
		this.julgamentoModulacao = julgamentoModulacao;
	}

	public Boolean getSessaoExtraordinaria() {
		return sessaoExtraordinaria;
	}

	public void setSessaoExtraordinaria(Boolean sessaoExtraordinaria) {
		this.sessaoExtraordinaria = sessaoExtraordinaria;
	}

	public List<ObjetoIncidente<?>> getListaObjetoIncidente() {
		return listaObjetoIncidente;
	}

	public void setListaObjetoIncidente(List<ObjetoIncidente<?>> listaObjetoIncidente) {
		this.listaObjetoIncidente = listaObjetoIncidente;
	}

	public boolean isListaComPedidoDeDestaque() {
		return listaComPedidoDeDestaque;
	}

	public void setListaComPedidoDeDestaque(boolean listaComPedidoDeDestaque) {
		this.listaComPedidoDeDestaque = listaComPedidoDeDestaque;
	}

	public Ministro getMinistroDestaque() {
		return ministroDestaque;
	}

	public void setMinistroDestaque(Ministro ministroDestaque) {
		this.ministroDestaque = ministroDestaque;
	}

	public String getCabecalhoVistor() {
		return cabecalhoVistor;
	}

	public void setCabecalhoVistor(String cabecalhoVistor) {
		this.cabecalhoVistor = cabecalhoVistor;
	}

	public boolean isAgendamentoAutomatico() {
		return agendamentoAutomatico;
	}

	public void setAgendamentoAutomatico(boolean agendamentoAutomatico) {
		this.agendamentoAutomatico = agendamentoAutomatico;
	}
}