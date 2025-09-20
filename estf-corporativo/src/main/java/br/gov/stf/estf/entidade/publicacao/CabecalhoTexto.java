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
 * Representa o cabeçalho do texto. Será gerado a cada transição de fase do
 * texto. Por exemplo, quando um texto é enviado de elaboração para revisão, um
 * novo cabeçalho é gerado. Esse cabeçalho é, então, associado à entidade
 * representando a fase de elaboração. Isso permitirá que o cabeçalho usado na
 * elaboração possa ser recuperado a qualquer tempo.
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
