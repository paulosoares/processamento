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
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name = "TIPO_PERMISSAO_MODELO", schema = "JUDICIARIO")
public class TipoPermissaoModeloComunicacao extends ESTFBaseEntity<Long> {

	public static final Long CODIGO_PERMISSAO_INSTITUCIONAL = 1L;

	private static final long serialVersionUID = 1433769301386908268L;

	private Long id;
	private String descricao;
	private Setor setor;

	@Id
	@Column(name = "SEQ_TIPO_PERMISSAO_MODELO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_TIPO_PERMISSAO_MODELO", allocationSize = 1)
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DSC_TIPO_PERMISSAO", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Transient
	public Boolean isInstitucional() {
		return getId() != null
				&& getId().equals(CODIGO_PERMISSAO_INSTITUCIONAL);
	}
}
