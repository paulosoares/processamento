package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "DOC", name = "VW_PESQUISA_TEXTUAL_OTIMIZADA")
public class PesquisaTextualOtimizada extends ESTFBaseEntity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3973312917913161341L;
	
	private Long id;
	
	@Id
	@Override
	@Column(name = "SEQ_ARQUIVO_ELETRONICO")
	public Long getId() {
		return id;
	}
}
