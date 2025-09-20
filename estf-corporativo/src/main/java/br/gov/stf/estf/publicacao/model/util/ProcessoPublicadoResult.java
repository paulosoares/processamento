/**
 * 
 */
package br.gov.stf.estf.publicacao.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem.TipoColecao;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;

/**
 * @author Paulo.Estevao
 * @since 07.11.2011
 */
public class ProcessoPublicadoResult {

	private br.gov.stf.estf.entidade.processostf.ObjetoIncidente<?> objetoIncidente;
	private Processo processo;
	private Ministro ministroRelator;
	private Date dataJulgamento;
	private Date dataPublicacao;
	private ProcessoImagem processoImagem;

	public ProcessoPublicadoResult(br.gov.stf.estf.entidade.processostf.ObjetoIncidente<?> objetoIncidente, Processo processo, Ministro ministroRelator, Date dataJulgamento,
			Date dataPublicacao, ProcessoImagem processoImagem) {
		this.objetoIncidente = objetoIncidente;
		this.processo = processo;
		this.ministroRelator = ministroRelator;
		this.dataJulgamento = dataJulgamento;
		this.dataPublicacao = dataPublicacao;
		this.processoImagem = processoImagem;
	}

	public ProcessoPublicadoResult(br.gov.stf.estf.entidade.processostf.ObjetoIncidente<?> objetoIncidente, Ministro ministroRelator, Date dataPublicacao) {
		this.objetoIncidente = objetoIncidente;
		this.ministroRelator = ministroRelator;
		this.dataPublicacao = dataPublicacao;
	}

	public br.gov.stf.estf.entidade.processostf.ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(br.gov.stf.estf.entidade.processostf.ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Ministro getMinistroRelator() {
		return ministroRelator;
	}

	public void setMinistroRelator(Ministro ministroRelator) {
		this.ministroRelator = ministroRelator;
	}

	public Date getDataJulgamento() {
		return dataJulgamento;
	}

	public void setDataJulgamento(Date dataJulgamento) {
		this.dataJulgamento = dataJulgamento;
	}

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public ProcessoImagem getProcessoImagem() {
		return processoImagem;
	}

	public void setProcessoImagem(ProcessoImagem processoImagem) {
		this.processoImagem = processoImagem;
	}

	public boolean isEletronico() {
		return TipoMeioProcesso.ELETRONICO.equals(((Processo) objetoIncidente.getPrincipal()).getTipoMeioProcesso());
	}

	public String getTipoColecao() {
		if (processoImagem != null && processoImagem.getTipoColecao() == TipoColecao.ELETRONICO) {
			return "Eletrônico";
		} else {
			return "Ementário";
		}
	}
}
