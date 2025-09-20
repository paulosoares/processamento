package br.gov.stf.estf.entidade.usuario;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "USUARIO_EXTE_INTE_DOCUMENTO", schema = "ESTF")
public class IntegracaoDocumento extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = -4216300307980864607L;
	
	private ChaveComposta chave;
	private Date dataInclusao;

	@Override
	@Id
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_INCLUSAO")
	public Date getDataInclusao() {
		return dataInclusao;
	}
	
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public ChaveComposta getChave() {
		return chave;
	}
	
	public void setChave(ChaveComposta chave) {
		this.chave = chave;
	}

	@Embeddable
	public class ChaveComposta implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4151406438162701253L;
		private long seqUsuario;
		private long seqDocumento;
		
		public void setSeqUsuario(long seqUsuario) {
			this.seqUsuario = seqUsuario;
		}
		@Column(name="SEQ_USUARIO_EXTERNO")
		public long getSeqUsuario() {
			return seqUsuario;
		}
		public void setSeqDocumento(long seqDocumento) {
			this.seqDocumento = seqDocumento;
		}
		@Column(name="SEQ_DOCUMENTO")
		public long getSeqDocumento() {
			return seqDocumento;
		}
	}

}
