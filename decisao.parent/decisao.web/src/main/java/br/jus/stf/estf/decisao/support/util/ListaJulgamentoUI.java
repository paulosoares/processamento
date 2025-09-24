package br.jus.stf.estf.decisao.support.util;

import java.util.Date;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.enuns.SituacaoListaJulgamento;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.util.DataUtil;

public class ListaJulgamentoUI implements Comparable {
	private boolean selected;
	private String nome;
	private String ambiente;
	private String colegiado;
	private String sessao;
	private Date dataSessao;
	private SituacaoListaJulgamento situacao;
	private ListaJulgamento instancia;
	private String descricaoAndamentoLiberacao;
	private String descricaoTipoLista;	
	
	public static Long TIPO_ANDAMENTO_LIBERACAO_MESA = 7600L;
	public static Long TIPO_ANDAMENTO_LIBERACAO_PAUTA = 7601L;
	
	public ListaJulgamentoUI(ListaJulgamento instancia, String descricaoColegiado, String descricaoAmbiente) {
		this.setSelected(false);
		this.setInstancia(instancia);
		this.setColegiado(descricaoColegiado);
		this.setAmbiente(descricaoAmbiente);
	}
	
	public ListaJulgamentoUI(ListaJulgamento instancia) {
		this.setSelected(false);
		this.instancia = instancia;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ListaJulgamento getInstancia() {
		return instancia;
	}

	public void setInstancia(ListaJulgamento instancia) {
		Date dataPrevistaInicio = instancia.getSessao().getDataPrevistaInicio();
		
		this.instancia = instancia;
		this.nome      = this.instancia.getNome();
		//dataSessao é a informação que aparece na tela do usuario.
		this.dataSessao = dataPrevistaInicio;

		//sessao é a data no formato invertido para facilitar 
		this.sessao     = DataUtil.date2StringInvertido(dataPrevistaInicio, true);
		this.situacao   = this.instancia.getSituacaoListaJulgamento();
		if (this.getInstancia().getAndamentoLiberacao() != null){
			Andamento andamentoLiberacao = this.getInstancia().getAndamentoLiberacao();
			if (andamentoLiberacao.getId().equals(TIPO_ANDAMENTO_LIBERACAO_MESA))
				this.descricaoAndamentoLiberacao = "Mesa";
			else if (andamentoLiberacao.getId().equals(TIPO_ANDAMENTO_LIBERACAO_PAUTA))
				this.descricaoAndamentoLiberacao = "Pauta";
			else
				this.descricaoAndamentoLiberacao = this.getInstancia().getAndamentoLiberacao().getDescricao();
		}
		if (instancia.getTipoListaJulgamento() != null) {
			this.setDescricaoTipoLista(instancia.getTipoListaJulgamento().getDescricao());
		}
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}
	
	public String getColegiado() {
		return colegiado;
	}

	public void setColegiado(String colegiado) {
		this.colegiado = colegiado;
	}

	public String getSessao() {
		return sessao;
	}

	public void setSessao(String sessao) {
		this.sessao = sessao;
	}

	public Date getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}

	public SituacaoListaJulgamento getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoListaJulgamento situacao) {
		this.situacao = situacao;
	}

	public String getDescricaoAndamentoLiberacao() {
		return descricaoAndamentoLiberacao;
	}

	public void setDescricaoAndamentoLiberacao(String descricaoAndamentoLiberacao) {
		this.descricaoAndamentoLiberacao = descricaoAndamentoLiberacao;
	}

	public String getDescricaoTipoLista() {
		return descricaoTipoLista;
	}

	public void setDescricaoTipoLista(String descricaoTipoLista) {
		this.descricaoTipoLista = descricaoTipoLista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListaJulgamentoUI other = (ListaJulgamentoUI) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			return 0;
		if (getClass() != obj.getClass())
			return 0;
		
		ListaJulgamentoUI other = (ListaJulgamentoUI) obj;
		
		if (nome == null || other.nome == null)
			return 0;
		
		return nome.compareTo(other.nome);
	}
}
