package br.gov.stf.estf.entidade.publicacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * Representa o cabe�alho do texto. Ser� gerado a cada transi��o de fase do
 * texto. Por exemplo, quando um texto � enviado de elabora��o para revis�o, um
 * novo cabe�alho � gerado. Esse cabe�alho �, ent�o, associado � entidade
 * representando a fase de elabora��o. Isso permitir� que o cabe�alho usado na
 * elabora��o possa ser recuperado a qualquer tempo.
 * 
 * @author Rodrigo Barreiros
 * @since 26.05.2009
 */
@Entity
@Table(name = "CABECALHO_TEXTO", schema = "DOC")
public class CabecalhoTexto extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 8602732765718073890L;

	private Long id;
	private String xml;
	
	@Id
	@Column(name = "SEQ_CABECALHO_TEXTO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "DOC.SEQ_CABECALHO_TEXTO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Lob
	@Column(name = "XML_CABECALHO")
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
	
}
