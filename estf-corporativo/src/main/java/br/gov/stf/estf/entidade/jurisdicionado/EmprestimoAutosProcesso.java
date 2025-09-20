package br.gov.stf.estf.entidade.jurisdicionado;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;

@Entity
@Table(schema = "JUDICIARIO", name = "EMPRESTIMO_AUTOS_PROCESSO")
public class EmprestimoAutosProcesso extends ESTFBaseEntity<Long> {


	private static final long serialVersionUID = -5797562859667399988L;
	
	private Long id;
	private DeslocaProcesso deslocaRetirada;
	private DeslocaProcesso deslocaDevolucao;
	private PapelJurisdicionado papelJurisdicionado;
	private AssociacaoJurisdicionado associacaoJurisdicionado;
	private Date dataEmprestimo;
	private Date dataDevolucaoPrevista;
	private Integer quantidadeCobrancaDevolucao;
	
	
	@Id
	@Column(name = "SEQ_EMPRESTIMO_AUTOS_PROC")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_EMPRESTIMO_AUTOS_PROC", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_DESLOCAMENTO_RETIRADA", referencedColumnName="SEQ_DESLOCA_PROCESSOS", unique = false, insertable = true, updatable = true)
	public DeslocaProcesso getDeslocaRetirada() {
		return deslocaRetirada;
	}
	
	public void setDeslocaRetirada(DeslocaProcesso deslocaRetirada) {
		this.deslocaRetirada = deslocaRetirada;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_DESLOCAMENTO_DEVOLUCAO", referencedColumnName="SEQ_DESLOCA_PROCESSOS", unique = false, insertable = true, updatable = true)
	public DeslocaProcesso getDeslocaDevolucao() {
		return deslocaDevolucao;
	}
	
	public void setDeslocaDevolucao(DeslocaProcesso deslocaDevolucao) {
		this.deslocaDevolucao = deslocaDevolucao;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_PAPEL_JURIS_ADVOGADO", unique = false, insertable = true, updatable = true)
	public PapelJurisdicionado getPapelJurisdicionado() {
		return papelJurisdicionado;
	}
	
	public void setPapelJurisdicionado(PapelJurisdicionado papelJurisdicionado) {
		this.papelJurisdicionado = papelJurisdicionado;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_ASSOCIACAO_JURISDICIONADO", unique = false, insertable = true, updatable = true)
	public AssociacaoJurisdicionado getAssociacaoJurisdicionado() {
		return associacaoJurisdicionado;
	}
	
	public void setAssociacaoJurisdicionado(
			AssociacaoJurisdicionado associacaoJurisdicionado) {
		this.associacaoJurisdicionado = associacaoJurisdicionado;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_EMPRESTIMO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(Date dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_DEVOLUCAO_PREVISTA", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataDevolucaoPrevista() {
		return dataDevolucaoPrevista;
	}
	
	public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) {
		this.dataDevolucaoPrevista = dataDevolucaoPrevista;
	}
	
	@Column(name = "QTD_COBRANCA_DEVOLUCAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getQuantidadeCobrancaDevolucao() {
		return quantidadeCobrancaDevolucao;
	}
	public void setQuantidadeCobrancaDevolucao(Integer quantidadeCobrancaDevolucao) {
		this.quantidadeCobrancaDevolucao = quantidadeCobrancaDevolucao;
	}
	
	@Transient
	public String getNomeAutorizadoDaCarga(){
		if (getAssociacaoJurisdicionado() == null){
			return "";
		}
		return getAssociacaoJurisdicionado().getMembro().getJurisdicionado().getNome();	
	}
	
	@Transient
	public String getNomeAutorizadorDaCarga(){
		if (getPapelJurisdicionado() == null){
			return "";
		}else{
			return getPapelJurisdicionado().getJurisdicionado().getNome();
		}
	}
	
	@Transient
	public String getNomeJurisdicionadoDaCarga(){
		if (getPapelJurisdicionado() == null){
			return getAssociacaoJurisdicionado().getMembro().getJurisdicionado().getNome();
		}else{
			return getPapelJurisdicionado().getJurisdicionado().getNome();
		}
	}
	
	/**
	 * 
	 * @return Retorna o nome do autorizador e entre parênteses o autorizado quando o empréstimo for realizado para o autorizado.
	 * 			se o empréstimo for realizado para um advogado (autorizador) somente o nome deste será retornado. 
	 */
	@Transient
	public String getNomeAutorizadorAutorizado(){
		if (getPapelJurisdicionado() == null){
			return getAssociacaoJurisdicionado().getGrupo().getJurisdicionado().getNome().toUpperCase() + 
			       " (" + getAssociacaoJurisdicionado().getMembro().getJurisdicionado().getNome().toUpperCase() + ")" ;
		}else{
			return getPapelJurisdicionado().getJurisdicionado().getNome().toUpperCase();
		}
	}
	
}
