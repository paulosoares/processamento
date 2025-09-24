package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoIdentificacao;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.Categoria;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.Agendamento.AgendamentoId;
import br.gov.stf.estf.entidade.processostf.enuns.TipoPolo;
import br.jus.stf.estf.decisao.pesquisa.domain.AgrupadorLocal;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;

public class AgrupadorRepo {
	
	public AgrupadorLocal getAgrupadorLocal (int id, String Descricao, boolean Selected, int getQtdObjIncidentes) {		
		Agrupador agrupador = getAgrupador(id, Descricao);
		AgrupadorLocal agrupadorLocal = new AgrupadorLocal(agrupador);
		agrupadorLocal.setSelected(Selected);
		agrupadorLocal.setQtdObjIncidentes(getQtdObjIncidentes);
		return agrupadorLocal;
	}

	public Agrupador getAgrupador(int id, String Descricao) {
		Agrupador agrupador = new Agrupador();
		agrupador.setId((long) id);
		agrupador.setDescricao(Descricao);
		return agrupador;
	}
	
	public Processo getObjetoIncidente(int id, String numeroRegistro) {
		Processo processo = new Processo();
		processo.setId((long) id);
		processo.setNumeroRegistro(numeroRegistro);
		
		Classe classe = new Classe();
		classe.setDescricao(Classe.SIGLA_RECLAMACAO);
		processo.setClasseProcessual(classe);
		
		
		return processo;
	}
	
	public Processo getObjetoIncidente(int id, String numeroRegistro, String siglaClasse) {
		Processo processo = this.getObjetoIncidente(id, numeroRegistro);
		
		Classe classe = new Classe();
		classe.setDescricao(siglaClasse);
		classe.setId(siglaClasse);
		processo.setClasseProcessual(classe);		
		
		return processo;
	}	
	
	public Processo getHabeasCorpus() {
		Processo processo = new Processo();
		Classe classe = new Classe();
		classe.setDescricao(Classe.SIGLA_HABEAS_CORPUS);
		processo.setClasseProcessual(classe);
		
		return processo;
	}
	
	public ObjetoIncidenteDto getObjetoIncidenteDto(int id, Long numeroProcesso) {
		ObjetoIncidenteDto processo = new ObjetoIncidenteDto();
		processo.setId((long) id);
		processo.setNumeroProcesso(numeroProcesso);
		return processo;
	}

	public PreListaJulgamento getPreListaJulgamento(int id, String nome) {
		PreListaJulgamento preListaJulgamento = new PreListaJulgamento();
		preListaJulgamento.setId((long) id);
		preListaJulgamento.setNome(nome);
		return preListaJulgamento;
	}	
	
	public PreListaJulgamentoObjetoIncidente getPreListaJulgamentoObjetoIncidente(long idPreListaJulgamentoOI, int idOI){
		Processo objetoIncidente = this.getObjetoIncidente(idOI,null);
		
		PreListaJulgamentoObjetoIncidente preListaJulgamentoObjetoIncidente = new PreListaJulgamentoObjetoIncidente();
		preListaJulgamentoObjetoIncidente.setId(idPreListaJulgamentoOI);
		preListaJulgamentoObjetoIncidente.setObjetoIncidente(objetoIncidente);
		preListaJulgamentoObjetoIncidente.setRevisado(Boolean.TRUE);
		
		return preListaJulgamentoObjetoIncidente;
	}
	
	public Colegiado getColegiado(String id){
		Colegiado colegiado = new Colegiado();
		colegiado.setId(id);
		return colegiado;
	}	
	
	public Sessao getSessao(long id){
		Colegiado colegiado = getColegiado(Colegiado.TRIBUNAL_PLENO);
		Sessao sessaoRetorno = new Sessao();
		sessaoRetorno.setId(id);
		Date dataPrevistaInicio = new Date();
		sessaoRetorno.setDataInicio(dataPrevistaInicio);
		sessaoRetorno.setDataPrevistaInicio(dataPrevistaInicio);
		sessaoRetorno.setColegiado(colegiado);
		sessaoRetorno.setTipoAmbiente(TipoAmbienteConstante.PRESENCIAL.getSigla());
		sessaoRetorno.setTipoSessao(TipoSessaoConstante.ORDINARIA.getSigla());
		return sessaoRetorno;
	}

	public void injetarParte(Processo processo, TipoPolo tipoPolo) {
		
		Parte parte = new Parte();
		Categoria categoria = new Categoria();
		categoria.setTipo(tipoPolo);
		parte.setCategoria(categoria);
		
		Jurisdicionado jurisdicionado = new Jurisdicionado();
		parte.setJurisdicionado(jurisdicionado);
		
		if (processo.getPartesVinculadas() == null)
			processo.setPartesVinculadas(new ArrayList<Parte>());
		
		processo.getPartesVinculadas().add(parte);
	}	
	
	public void injetarRepresentante(Processo processo, TipoPolo tipoPolo, String oab) {
		Parte representante = new Parte();
		Categoria categoria = new Categoria();
		categoria.setId(Categoria.COD_CATEGORIA_ADVOGADO);
		categoria.setTipo(tipoPolo);
		representante.setCategoria(categoria);
		
		IdentificacaoPessoa identificacaoPessoa  = new IdentificacaoPessoa();
		TipoIdentificacao tipoIdentificacao = new TipoIdentificacao();
		tipoIdentificacao.setSiglaTipoIdentificacao(EnumTipoIdentificacao.OAB.getSigla());
		tipoIdentificacao.setDescricaoTipoIdentificacao(EnumTipoIdentificacao.OAB.getDescricao());
		identificacaoPessoa.setTipoIdentificacao(tipoIdentificacao);
		identificacaoPessoa.setDescricaoIdentificacao(oab);
		
		Jurisdicionado jurisdicionado = new Jurisdicionado();
		jurisdicionado.setIdentificadoresJurisdicionado(Arrays.asList(identificacaoPessoa));
		
		identificacaoPessoa.setJurisdicionado(jurisdicionado);
		
		representante.setJurisdicionado(jurisdicionado);
		
		if (processo.getPartesVinculadas() == null)
			processo.setPartesVinculadas(new ArrayList<Parte>());
		
		processo.getPartesVinculadas().add(representante);
	}
	
	public ListaJulgamento getListaJulgamento(long id, String nome) {
		Sessao sessao = this.getSessao(id);
		ListaJulgamento listaJulgamentoRetorno = new ListaJulgamento();
		listaJulgamentoRetorno.setId(id);
		listaJulgamentoRetorno.setNome(nome);
		listaJulgamentoRetorno.setSessao(sessao);
		return listaJulgamentoRetorno;
	}
	
	public Agendamento getAgendamento(Integer codAgendamento) {
		AgendamentoId agendamentoId = new AgendamentoId();
		agendamentoId.setCodigoMateria(codAgendamento);
		
		Agendamento agendamento = new Agendamento();
		agendamento.setId(agendamentoId);
		return agendamento;
	}	
	
}
