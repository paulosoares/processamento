package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "TIPO_SITUACAO_JULGAMENTO", schema = "JULGAMENTO")
public class TipoSituacaoJulgamento extends ESTFBaseEntity<String> {

	private static final long serialVersionUID = 408591531595054675L;
	private String descricao;

	public enum TipoSitucacaoJulgamentoConstant {
		EM_ANDAMENTO("1","Em andamento"),
		FINALIZADO("2","Finalizado"),
		CANCELADO("3","Cancelado"),
		AGENDADO("4","Agendado"),
		SUSPENSO("5","Suspenso"),
		EM_ABERTO("6","Em aberto");
		
		private String codigo;
		private String descricao;

		private TipoSitucacaoJulgamentoConstant(String codigo, String descricao) {
			this.codigo = codigo;
			this.descricao = descricao;
		}

		public String getCodigo() {
			return this.codigo;
		}

		public String getDescricao() {
			return this.descricao;
		}

	}

	@Id
	@Column(name = "COD_TIPO_SITUACAO_JULGAMENTO")
	public String getId() {
		return id;
	}

	@Column(name = "DSC_TIPO_SITUACAO_JULGAMENTO", insertable = true, updatable = true, unique = false, nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
