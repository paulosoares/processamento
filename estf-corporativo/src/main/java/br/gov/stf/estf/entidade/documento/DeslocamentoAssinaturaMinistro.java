package br.gov.stf.estf.entidade.documento;


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
import br.gov.stf.estf.entidade.ministro.Ministro;

@Entity
@Table(name="DESL_ASSINATURA_MINISTRO", schema="JUDICIARIO")
public class DeslocamentoAssinaturaMinistro extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = 3117802185249858172L;
	private Long id;
	private Ministro ministro;
	private DeslocamentoComunicacao deslocamento;
		
	
	@Id
	@Column(name = "SEQ_DESL_ASSINATURA_MINISTRO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_DESL_ASSINATURA_MINISTRO", allocationSize = 1)
	public Long getId() {
		
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO", unique = false, nullable = true, insertable = true, updatable = true)
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DESLOCAMENTO_COMUNICACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public DeslocamentoComunicacao getDeslocamento() {
		return deslocamento;
	}

	public void setDeslocamento(DeslocamentoComunicacao deslocamento) {
		this.deslocamento = deslocamento;
	}
	
	
	







}
