/**
 * 
 */
package br.gov.stf.estf.entidade.tesauro;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 12.07.2012
 */
@Entity
@Table(schema = "TESAURO", name = "TERMO")
public class Termo extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7704765885089571341L;
	
	private Long id;
	private String descricao;
	private String descritor;
	private TipoSituacaoTermo situacao;
	private Date dataCriacaoTermo;
	private Date dataAlteracaoTermo;
	private List<TermoRelacao> termosRelacao;
	
	@Id
	@Column(name = "NUM_TERMO")
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DSC_TERMO")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "FLG_DESCRITOR")
	public String getDescritor() {
		return descritor;
	}
	
	public void setDescritor(String descritor) {
		this.descritor = descritor;
	}
	
	@Column(name = "TIP_SITUACAO_TERMO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.tesauro.TipoSituacaoTermo"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "identifierMethod", value = "getSigla"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla") })
	public TipoSituacaoTermo getSituacao() {
		return situacao;
	}
	
	public void setSituacao(TipoSituacaoTermo situacao) {
		this.situacao = situacao;
	}
	
	@Column(name = "DAT_CRIACAO_TERMO")
	@Temporal(TemporalType.DATE)
	public Date getDataCriacaoTermo() {
		return dataCriacaoTermo;
	}
	
	public void setDataCriacaoTermo(Date dataCriacaoTermo) {
		this.dataCriacaoTermo = dataCriacaoTermo;
	}
	
	@Column(name = "DAT_ALTERACAO_TERMO")
	@Temporal(TemporalType.DATE)
	public Date getDataAlteracaoTermo() {
		return dataAlteracaoTermo;
	}
	
	public void setDataAlteracaoTermo(Date dataAlteracaoTermo) {
		this.dataAlteracaoTermo = dataAlteracaoTermo;
	}
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name = "NUM_TERMO")
	public List<TermoRelacao> getTermosRelacao() {
		return termosRelacao;
	}
	
	public void setTermosRelacao(List<TermoRelacao> termosRelacao) {
		this.termosRelacao = termosRelacao;
	}
	
	// Categoria USE - 10
	@Transient
	public Termo getTermoParaUsar() {
		for (TermoRelacao termoRelacao : getTermosRelacao()) {
			if (termoRelacao.getId().getCategoria().equals(TermoRelacao.NUMERO_CATEGORIA_USE)) {
				return termoRelacao.getId().getTermoRelacionado();
			}
		}
		return null;
	}
}
