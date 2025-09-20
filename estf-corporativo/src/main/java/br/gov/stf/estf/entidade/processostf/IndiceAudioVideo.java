package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;

/**
 * Representa a entidade Indice_AV.
 *
 * @author Almir Leite de Oliveira
 * @since 15.12.2011 
 * 
 * TODO MAPEAR OS DEMAIS ATRIBUTOS DA ENTIDADE */
@Entity
@Table(name = "INDICE_AV", schema = "AUDIO_VIDEO")
public class IndiceAudioVideo extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 8119715537305907963L;

	private Long id;
	private SessaoAudioEVideo sessaoAudioEVideo;
	private ListaJulgamento listaJulgamento;
	
	@Id
	@Column( name="SEQ_INDICE_AV" )
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name="SEQ_SESSAO_AV" )
	public SessaoAudioEVideo getSessaoAudioEVideo() {
		return sessaoAudioEVideo;
	}
	
	public void setSessaoAudioEVideo(SessaoAudioEVideo sessaoAudioEVideo) {
		this.sessaoAudioEVideo = sessaoAudioEVideo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_LISTA_JULGAMENTO")
	public ListaJulgamento getListaJulgamento() {
		return listaJulgamento;
	}
	public void setListaJulgamento(ListaJulgamento listaJulgamento) {
		this.listaJulgamento = listaJulgamento;
	}


}
