package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;

/**
 * Representa a entidade Protocolo Taquigrafia.
 *
 * @author Almir Leite de Oliveira
 * @since 14.12.2011 
 * 
 * TODO MAPEAR OS DEMAIS ATRIBUTOS DA ENTIDADE */
@Entity
@Table(name = "PROTOCOLO_TAQUIGRAFIA", schema = "JULGAMENTO")
public class ProtocoloTaquigrafia extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 8119715537305907963L;

	private Long id;
	private ListaJulgamento listaJulgamento;
	
	@Id
	@Column( name="SEQ_PROTOCOLO_TAQUIGRAFIA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_PROTOCOLO_TAQUIGRAFIA", allocationSize=1 )
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_LISTA_JULGAMENTO", insertable = false, updatable = false)
	public ListaJulgamento getListaJulgamento() {
		return listaJulgamento;
	}
	public void setListaJulgamento(ListaJulgamento listaJulgamento) {
		this.listaJulgamento = listaJulgamento;
	}

}
