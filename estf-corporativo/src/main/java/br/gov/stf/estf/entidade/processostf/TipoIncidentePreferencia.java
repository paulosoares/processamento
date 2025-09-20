package br.gov.stf.estf.entidade.processostf;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="TIPO_PREFERENCIA", schema="JUDICIARIO")
public class TipoIncidentePreferencia extends ESTFBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6384486270143047957L;
	private String descricao;
	private String sigla;
	private Set<IncidentePreferencia> incidentePreferencia;

	
	@Id
	@Column(name="SEQ_TIPO_PREFERENCIA")
	public Long getId() {
		return id;
	}

	@Column(name="DSC_PREFERENCIA")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name="SIG_PREFERENCIA")
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@OneToMany(cascade={}, fetch = FetchType.LAZY, mappedBy = "tipoPreferencia")
	public Set<IncidentePreferencia> getIncidentePreferencia() {
		return incidentePreferencia;
	}

	public void setIncidentePreferencia(
			Set<IncidentePreferencia> incidentePreferencia) {
		this.incidentePreferencia = incidentePreferencia;
	}
	
	public enum TipoPreferenciaContante {
		MEDIDA_LIMINAR ("MEDIDA LIMINAR", "ML", (short) 8),
		TUTELA_PROVISORIA ("TUTELA PROVISORIA", "TP", (short) 41),
		TUTELA_ANTECIPADA ("TUTELA ANTECIPADA", "TA", (short) 9),
		CRIMINAL ("CRIMINAL", "CR", (short) 2),
		ELEITORAL ("ELEITORAL", "EL", (short) 3),
		MILITAR_COM_ASSUNTO_CRIMINAL ("MILITAR COM ASSUNTO CRIMINAL", "MA", (short) 4),
		RE_AGRAVO_PROVIDO ("RE DE AGRAVO PROVIDO", "RP", (short) 5),
		OUTROS ("OUTROS", "OU", (short) 6),
		MAIOR_65_ANOS ("MAIOR QUE 65 ANOS", "MQ", (short) 1),
		INDIGENA ("INDÍGENA", "IN", (short) 7),
		COVID_19 ("COVID-19", "CO", (short) 16);
		
		private String descricao;
		private String sigla;
		private Short codigo;
		
		private TipoPreferenciaContante(String descricao, String sigla, Short codigo) {
			this.descricao = descricao;
			this.sigla = sigla;
			this.codigo = codigo;
		}
		
		public String getDescricao() {
			return descricao;
		}
		public String getSigla() {
			return sigla;
		}
		public Short getCodigo() {
			return codigo;
		}
		
	}
	
	
}
