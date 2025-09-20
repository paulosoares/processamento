package br.gov.stf.estf.entidade.localizacao;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(schema = "EGAB", name = "ITEM_CONFIGURACAO_SETOR")
public class ItemConfiguracaoSetor extends ESTFBaseEntity<Long> {

	private ConfiguracaoSetor configuracaoSetor;
	private Andamento tipoAndamento;
	private Secao secao;
	private TipoFaseSetor tipoFaseSetor;
	private Usuario usuario;

	@Id
	@Column(name = "SEQ_ITEM_CONFIGURACAO_SETOR")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EGAB.SEQ_TIPO_CONFIGURACAO_SETOR", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_CONFIGURACAO_SETOR", nullable = true)
	public ConfiguracaoSetor getConfiguracaoSetor() {
		return configuracaoSetor;
	}

	public void setConfiguracaoSetor(ConfiguracaoSetor configuracaoSetor) {
		this.configuracaoSetor = configuracaoSetor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO", nullable = true)
	public Andamento getTipoAndamento() {
		return tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SECAO", nullable = true)
	public Secao getSecao() {
		return secao;
	}

	public void setSecao(Secao secao) {
		this.secao = secao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_FASE_SETOR", nullable = true)
	public TipoFaseSetor getTipoFaseSetor() {
		return tipoFaseSetor;
	}

	public void setTipoFaseSetor(TipoFaseSetor tipoFaseSetor) {
		this.tipoFaseSetor = tipoFaseSetor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_USUARIO", nullable = true)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
