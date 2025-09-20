package br.gov.stf.estf.entidade.usuario;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "TRANSACAO", schema = "GLOBAL")
public class Transacao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	private String descricao;
	private boolean ativa;
	private String siglaSistema;
	private int numOrdem;
	private String descricaoCaminhoInterface;
	private Set<Perfil> perfis;

	@Id
	@Column(name = "SEQ_TRANSACAO", unique = false, nullable = false, insertable = true, updatable = true, precision = 6, scale = 0)
	public Long getId() {
		return id;
	}

	@Column(name = "DSC_TRANSACAO", unique = false, nullable = false, insertable = true, updatable = true, length = 500)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "FLG_ATIVO", unique = false, nullable = false, insertable = true, updatable = true)
	protected char getAtivaChar() {
		return isAtiva() ? 'S' : 'N';
	}

	protected void setAtivaChar(char c) {
		setAtiva(c == 'S');
	}

	@Transient
	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	@Column(name = "SIG_SISTEMA", unique = false, nullable = false, insertable = false, updatable = false)
	public String getSiglaSistema() {
		return siglaSistema;
	}

	public void setSiglaSistema(String siglaSistema) {
		this.siglaSistema = siglaSistema;
	}

	@Column(name = "NUM_ORDEM", unique = false, nullable = false, insertable = false, updatable = false, precision = 4, scale = 0)
	public int getNumOrdem() {
		return numOrdem;
	}

	public void setNumOrdem(int numOrdem) {
		this.numOrdem = numOrdem;
	}

	@Column(name = "DSC_CAMINHO_INTERFACE")
	public String getDescricaoCaminhoInterface() {
		return descricaoCaminhoInterface;
	}

	public void setDescricaoCaminhoInterface(String descricaoCaminhoInterface) {
		this.descricaoCaminhoInterface = descricaoCaminhoInterface;
	}

	@ManyToMany(targetEntity = Perfil.class, fetch = FetchType.LAZY)
	@JoinTable(schema = "GLOBAL", name = "TRANSACAO_PERFIL", joinColumns = @JoinColumn(name = "SEQ_TRANSACAO"), inverseJoinColumns = @JoinColumn(name = "SEQ_PERFIL"))
	public Set<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(Set<Perfil> perfis) {
		this.perfis = perfis;
	}
}
