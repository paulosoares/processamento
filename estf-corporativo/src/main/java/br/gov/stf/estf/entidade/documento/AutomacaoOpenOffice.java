package br.gov.stf.estf.entidade.documento;

import br.gov.stf.estf.entidade.localizacao.Setor;

/**
 * Classe criada para recuperar do banco as definições de macros e atalhos para
 * o OpenOffice, definidas para cada setor
 * 
 * @author Demetrius.Jube
 * 
 */
// @Entity
// @Table(schema = "STF", name = "AUTOMACAO_OPENOFFICE")
public class AutomacaoOpenOffice {// extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7023032841227932153L;
	private byte[] macro;
	private byte[] atalho;
	private Setor setor;

	// @Override
	// @Column(name = "SEQ_AUTOMACAO_OPENOFFICE")
	// @GeneratedValue(generator = "sequence", strategy =
	// GenerationType.SEQUENCE)
	// @SequenceGenerator(name = "sequence", sequenceName =
	// "STF.SEQ_AUTOMACAO_OPENOFFICE", allocationSize = 1)
	// public Long getId() {
	// return id;
	// }

	// @Lob
	// @Basic(fetch = FetchType.LAZY)
	// @Column(name = "BIN_MACRO")
	public byte[] getMacro() {
		return macro;
	}

	public void setMacro(byte[] macro) {
		this.macro = macro;
	}

	// @Lob
	// @Basic(fetch = FetchType.LAZY)
	// @Column(name = "BIN_ATALHO")
	public byte[] getAtalho() {
		return atalho;
	}

	public void setAtalho(byte[] atalho) {
		this.atalho = atalho;
	}

	// @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	// @JoinColumn(name = "COD_SETOR", unique = false, nullable = true,
	// insertable = true, updatable = true)
	public Setor getSetor() {
		return this.setor;
	}

	public void setSetor(Setor codigoSetor) {
		this.setor = codigoSetor;
	}

}
