package br.gov.stf.estf.assinatura.visao.util;

import java.util.List;

import br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento.InforParte;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Usuario;


public class InfoPecaVinculadoAndamentoDTO {
		
	private final byte[] doc;		
	private final ObjetoIncidente<?> objetoIncidente;
	private final Setor setorUsuario;
	private final AndamentoProcesso andamentoProcesso;
	private final Usuario usuario;
	private final TipoPecaProcesso tipoPecaProcesso;
	private final ModeloComunicacao modeloComunicacao;
	private final String siglaTipoPecaProcesso;
	private final Long idAndamentoSelecionado;
	private final List<InforParte> listaPartes;
	private final String descricaoPeca;
	
	private InfoPecaVinculadoAndamentoDTO(
			final byte[] doc,		
			final ObjetoIncidente<?> objetoIncidente,
			final Setor setorUsuario,
			final Usuario usuario,
			final AndamentoProcesso andamentoProcesso,
			final TipoPecaProcesso tipoPecaProcesso,
			final ModeloComunicacao modeloComunicacao,
			final String siglaTipoPecaProcesso,
			final Long idAndamentoSelecionado,
			final List<InforParte> listaPartes,
			final String descricaoPeca){
		this.doc = doc;
		this.objetoIncidente = objetoIncidente;
		this.setorUsuario = setorUsuario;
		this.usuario = usuario;
		this.andamentoProcesso = andamentoProcesso;
		this.tipoPecaProcesso = tipoPecaProcesso;
		this.modeloComunicacao = modeloComunicacao;
		this.siglaTipoPecaProcesso = siglaTipoPecaProcesso;
		this.idAndamentoSelecionado = idAndamentoSelecionado;
		this.listaPartes = listaPartes;
		this.descricaoPeca = descricaoPeca;
	}	
	

	public byte[] getDoc() {
		return doc;
	}

	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public Setor getSetorUsuario() {
		return setorUsuario;
	}

	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public TipoPecaProcesso getTipoPecaProcesso() {
		return tipoPecaProcesso;
	}

	public ModeloComunicacao getModeloComunicacao() {
		return modeloComunicacao;
	}
	
	public String getSiglaTipoPecaProcesso() {
		return siglaTipoPecaProcesso;
	}
	
	public Long getIdAndamentoSelecionado() {
		return idAndamentoSelecionado;
	}
	
	public List<InforParte> getListaPartes() {
		return listaPartes;
	}
	
	public String getDescricaoPeca() {
		return descricaoPeca;
	}
	
	public boolean isDtoPreenchido(){
		return getDoc() != null 
			&& getObjetoIncidente() != null 
			&& getSetorUsuario() != null 
			&& getAndamentoProcesso() != null
			&& getUsuario() != null 
			&& getTipoPecaProcesso() != null 
			&& getModeloComunicacao() != null 
			&& getSiglaTipoPecaProcesso() != null
			&& getIdAndamentoSelecionado() != null;
	}
	
	public static class Builder{
		private byte[] doc;		
		private ObjetoIncidente<?> objetoIncidente;
		private Setor setorUsuario;
		private AndamentoProcesso andamentoProcesso;
		private Usuario usuario;
		private TipoPecaProcesso tipoPecaProcesso;
		private ModeloComunicacao modeloComunicacao;
		private String siglaTipoPecaProcesso;
		private Long idAndamentoSelecionado;
		private List<InforParte> listaPartes;
		private String descricaoPeca;
		public Builder setDoc(byte[] doc){
			this.doc = doc;
			return this;
		}
		
		public Builder setObjetoIncidente(ObjetoIncidente<?> objetoIncidente){
			this.objetoIncidente = objetoIncidente;
			return this;
		}
		
		public Builder setSetorUsuario(Setor setorUsuario){
			this.setorUsuario = setorUsuario;
			return this;
		}		

		public Builder setUsuario(Usuario usuario){
			this.usuario = usuario;
			return this;
		}
		
		public Builder setAndamentoProcesso(AndamentoProcesso andamentoProcesso){
			this.andamentoProcesso = andamentoProcesso;
			return this;
		}
		
		public Builder setTipoPecaProcesso(TipoPecaProcesso tipoPecaProcesso){
			this.tipoPecaProcesso = tipoPecaProcesso;
			return this;
		}
		
		public Builder setModeloComunicacao(ModeloComunicacao modeloComunicacao) {
			this.modeloComunicacao = modeloComunicacao;
			return this;
		}
		
		public Builder setSiglaTipoPecaProcesso(String siglaTipoPecaProcesso) {
			this.siglaTipoPecaProcesso = siglaTipoPecaProcesso;
			return this;
		}
		
		public Builder setIdAndamentoSelecionado(Long idAndamentoSelecionado) {
			this.idAndamentoSelecionado = idAndamentoSelecionado;
			return this;
		}
		
		public Builder setListaPartes(List<InforParte> listaPartes) {
			this.listaPartes = listaPartes;
			return this;
		}
		
		public Builder setDescricaoPeca(String descricaoPeca) {
			this.descricaoPeca = descricaoPeca;
			return this;
		}
		
		public InfoPecaVinculadoAndamentoDTO builder(){
			return new InfoPecaVinculadoAndamentoDTO(doc,objetoIncidente,setorUsuario,usuario,andamentoProcesso,tipoPecaProcesso,modeloComunicacao,siglaTipoPecaProcesso,idAndamentoSelecionado,listaPartes,descricaoPeca);
		}

	}
	
	

}
