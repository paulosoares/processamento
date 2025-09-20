package br.gov.stf.estf.entidade.julgamento;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.ministro.Ministro;

@Entity
@Table(name = "LEITURA_MANIFESTACAO", schema = "JULGAMENTO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ManifestacaoLeitura extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = -8698292440366498336L;
	private Long id;
	private ManifestacaoRepresentante manifestacaoRepresentante;
	private Ministro ministro;
	private Date dataLeitura;
	
	@Id
	@Column(name = "SEQ_LEITURA_MANIFESTACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JULGAMENTO.SEQ_LEITURA_MANIFESTACAO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_MANIFESTACAO_REPRESENTANTE", insertable = false, updatable = false)
	public ManifestacaoRepresentante getManifestacaoRepresentante() {
		return manifestacaoRepresentante;
	}
	public void setManifestacaoRepresentante(ManifestacaoRepresentante manifestacaoRepresentante) {
		this.manifestacaoRepresentante = manifestacaoRepresentante;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO")
	public Ministro getMinistro() {
		return ministro;
	}
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	@Column(name = "DAT_LEITURA")
	public Date getDataLeitura() {
		return dataLeitura;
	}
	public void setDataLeitura(Date dataLeitura) {
		this.dataLeitura = dataLeitura;
	}
	

}
