package br.gov.stf.estf.processostf.model.util;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;

public interface AndamentoProcessoInfo {

	public Andamento getAndamento();

	public Setor getSetor();

	public List<Processo> getProcessosPrincipais();

	public Peticao getPeticao();

	public Long getIdOrigemDecisao();

	public Long getIdTipoDevolucao();

	public String getObservacao();

	public String getCodigoUsuario();

	public Long getIdPresidenteInterino();

	public AndamentoProcesso getUltimoAndamento();
	
	public Origem getOrigem();

	public abstract String getObservacaoInterna();
	
	public abstract Comunicacao getComunicacao();
	
	public List<ProcessoTema> getProcessosTemas();

	Processo getProcesso();
	
	

}
