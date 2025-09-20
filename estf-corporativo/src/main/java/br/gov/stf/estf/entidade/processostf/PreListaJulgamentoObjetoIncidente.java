package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(schema = "JUDICIARIO", name = "LISTA_OBJETO_INCIDENTE") /* Na issue BD-633 foi pedida a alteração do nome da tabela para ficar condizente com o nome da entidade, porém, o pedido não foi aceito. */
public class PreListaJulgamentoObjetoIncidente extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private PreListaJulgamento preListaJulgamento;
	private ObjetoIncidente<?> objetoIncidente;
	private Boolean revisado;
	private PreListaJulgamentoMotivoAlteracao motivo;
	private Usuario usuarioRevisor;
	private Date dataRevisao;
	private String observacao;
	private ListaJulgamento listaJulgamento;
	
	@Id
	@Column(name = "SEQ_LISTA_OBJETO_INCIDENTE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_LISTA_OBJETO_INCIDENTE", allocationSize = 1)	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_LISTA")
	public PreListaJulgamento getPreListaJulgamento() {
		return preListaJulgamento;
	}

	public void setPreListaJulgamento(PreListaJulgamento preListaJulgamento) {
		this.preListaJulgamento = preListaJulgamento;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@Column(name = "FLG_REVISADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRevisado() {
		return revisado;
	}
	
	public void setRevisado(Boolean revisado) {
		this.revisado = revisado;
	}
	
	@Column(name = "COD_MOTIVO_ALTERACAO_LISTA", updatable = false)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao"),
			@Parameter(name = "idClass", value = "java.lang.Long") })
	public PreListaJulgamentoMotivoAlteracao getMotivo() {
		return motivo;
	}

	public void setMotivo(PreListaJulgamentoMotivoAlteracao motivo) {
		this.motivo = motivo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SIG_USUARIO_REVISAO")
	public Usuario getUsuarioRevisor() {
		return usuarioRevisor;
	}

	public void setUsuarioRevisor(Usuario usuarioRevisor) {
		this.usuarioRevisor = usuarioRevisor;
	}

	@Column(name="DAT_REVISAO")
	public Date getDataRevisao() {
		return dataRevisao;
	}

	public void setDataRevisao(Date dataRevisao) {
		this.dataRevisao = dataRevisao;
	}
	
	@Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_LISTA_JULGAMENTO")
	public ListaJulgamento getListaJulgamento() {
		return listaJulgamento;
	}

	public void setListaJulgamento(ListaJulgamento listaJulgamento) {
		this.listaJulgamento = listaJulgamento;
	}
}