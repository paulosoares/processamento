package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.Processo;

public class SessaoServiceImplTestRepo {
	
	public Colegiado getColegiado(String id){
		Colegiado colegiado = new Colegiado();
		colegiado.setId(id);
		return colegiado;
	}	
	
	public Sessao getSessao(long id){
		Colegiado colegiado = this.getColegiado(Colegiado.TRIBUNAL_PLENO);
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
	
	public Processo getObjetoIncidente(int id, String numeroRegistro) {
		Processo processo = new Processo();
		processo.setId((long) id);
		processo.setNumeroRegistro(numeroRegistro);
		
		Classe classe = new Classe();
		classe.setDescricao(Classe.SIGLA_RECLAMACAO);
		processo.setClasseProcessual(classe);
		
		return processo;
	}
	
	public JulgamentoProcesso getJulgamentoProcesso(long id){
		JulgamentoProcesso julgamentoProcesso = new JulgamentoProcesso();
		julgamentoProcesso.setId(id);
		return julgamentoProcesso;
	}
	
	public JulgamentoProcesso getJulgamentoProcesso(long id,TipoSituacaoProcessoSessao tipoSituacaoProcesso) {
		JulgamentoProcesso julgamentoProcesso = this.getJulgamentoProcesso(id);
		julgamentoProcesso.setSituacaoProcessoSessao(tipoSituacaoProcesso);
		return julgamentoProcesso;
	}
	
	public Ministro getMinistro (long id){
		Ministro ministro = new Ministro();
		ministro.setId(id);
		return ministro;
	}
	
	public Texto getTexto (long id){
		Texto texto = new Texto();
		texto.setId(id);
		return texto;
	}
	
	public Texto getTextoComMinstro (long id, Ministro ministroTexto){
		Texto texto = this.getTexto(id);
		texto.setMinistro(ministroTexto);
		return texto;
	}
	
	public Texto getTextoComMinstroTipoTexto (long id, Ministro ministroTexto, TipoTexto tipoTexto){
		Texto texto = this.getTextoComMinstro(id,ministroTexto);
		texto.setTipoTexto(tipoTexto);
		return texto;
	}	
	
	public VotoJulgamentoProcesso getVotoJulgamentoProcesso (long id){
		VotoJulgamentoProcesso votoJulgamentoProcesso = new VotoJulgamentoProcesso();
		return votoJulgamentoProcesso;
	}

	public List<Texto> getListTexto1elemento(TipoTexto tipoTexto) {
		Texto texto01 = new Texto();
		texto01.setTipoTexto(tipoTexto);
		
		List<Texto> ListTextos = new ArrayList<Texto>();
		ListTextos.add(texto01);
		
		return ListTextos;
	}
}
