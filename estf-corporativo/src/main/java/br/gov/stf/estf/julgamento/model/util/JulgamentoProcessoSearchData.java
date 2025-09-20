package br.gov.stf.estf.julgamento.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoJulgamento;
import br.gov.stf.framework.util.SearchData;

public class JulgamentoProcessoSearchData extends SearchData{
	
	private static final long serialVersionUID = 1L;
	
	public Long id;
	public String siglaClasse;
	public Long numeroProcesso;
	public Long codigoMinistroRelator;
	public Long numeroSessao;
	public Short anoSessao;
	public Date dataInicioSessao;
	public Date dataFimSessao;
	public Date dataPrevistaInicioSessao;
	public Date dataPrevistaFimSessao;
	public DateRange dataInicioSessaoDateRange;
	public DateRange dataFimSessaoDateRange;
	public DateRange dataPrevistaFimSessaoDateRange;
	public DateRange dataPrevistaInicioSessaoDateRange;
	public TipoSessaoConstante tipoSessao;
	public String colegiado;
	public TipoAmbienteConstante tipoAmbiente;
	public TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant tipoSituacaoJulgamento;
	
}
