package br.gov.stf.estf.entidade.processosetor;

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
import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Tarefa;
import br.gov.stf.estf.entidade.usuario.Usuario;


@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true) 
@Table(schema="EGAB", name="HISTORICO_DESLOCAMENTO_PETICAO" )
public class HistoricoDeslocamentoPeticao 
extends ESTFBaseEntity<Long>
{
	
	private Date dataRecebimento;
	private Date dataRemessa;
	private Boolean flagAtualizado;
	private String numeroArmario;
	private String numeroEstante;
	private String numeroPrateleira;
	private String numeroSala;
	private String numeroColuna;
	private String observacao;	
	private Usuario usuarioOrigem;
	private Usuario usuarioDestino;
	private Secao secaoOrigem;
	private Secao secaoDestino;
	private PeticaoSetor peticaoSetor;
	private Tarefa tarefa;
	
	//private Setor setor;
	//private Setor setorDestino;	
	//private ProcessoSetor processoSetor;
	
	public enum TipoDeslocamento {
		RECEBIMENTO("Recebimento"),
		REMESSA("Remessa");

		private String descricao;

		private TipoDeslocamento(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return this.descricao;
		}
	}

	@Id
	@Column( name="SEQ_HIST_DESLOCAMENTO_PETICAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_HIST_DESLOCAMENTO_PETICAO", allocationSize=1 )		
	public Long getId() {
		return id;

	}	

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TAREFA", unique=false, nullable=true, insertable=true, updatable=true)
	public Tarefa getTarefa() {
		return this.tarefa; 
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}

/*
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_PROCESSO_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public ProcessoSetor getProcessoSetor() {
		return this.processoSetor;
	}

	public void setProcessoSetor(ProcessoSetor processoSetor) {
		this.processoSetor = processoSetor;
	}
*/
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_RECEBIMENTO", unique=false, nullable=true, insertable=true, updatable=true)
	public Date getDataRecebimento() {
		return this.dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_REMESSA", unique=false, nullable=true, insertable=true, updatable=true)
	public Date getDataRemessa() {
		return this.dataRemessa;
	}

	public void setDataRemessa(Date dataRemessa) {
		this.dataRemessa = dataRemessa;
	}

	@Column(name="OBS_DESLOCAMENTO", unique=false, nullable=true, insertable=true, updatable=true, length=240)
	public String getObservacao() {
		return this.observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_SECAO_DESTINO", unique=false, nullable=true, insertable=true, updatable=true)
	public Secao getSecaoDestino() {
		return secaoDestino;
	}

	public void setSecaoDestino(Secao secaoDestino) {
		this.secaoDestino = secaoDestino;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_SECAO_ORIGEM", unique=false, nullable=true, insertable=true, updatable=true)	
	public Secao getSecaoOrigem() {
		return secaoOrigem;
	}

	public void setSecaoOrigem(Secao secaoOrigem) {
		this.secaoOrigem = secaoOrigem;
	}

/*
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_SETOR", unique=false, nullable=true, insertable=true, updatable=true)	
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_SETOR_DESTINO", unique=false, nullable=true, insertable=true, updatable=true)	
	public Setor getSetorDestino() {
		return setorDestino;
	}

	public void setSetorDestino(Setor setorDestino) {
		this.setorDestino = setorDestino;
	}
*/
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO_DESTINO", unique=false, nullable=true, insertable=true, updatable=true)	
	public Usuario getUsuarioDestino() {
		return usuarioDestino;
	}

	
	public void setUsuarioDestino(Usuario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SIG_USUARIO_ORIGEM", unique=false, nullable=true, insertable=true, updatable=true)	
	public Usuario getUsuarioOrigem() {
		return usuarioOrigem;
	}

	public void setUsuarioOrigem(Usuario usuarioOrigem) {
		this.usuarioOrigem = usuarioOrigem;
	}

	@Column(name="NUM_ARMARIO", unique=false, nullable=true, insertable=true, updatable=true, length=20)
	public String getNumeroArmario() {
		return this.numeroArmario;
	}

	public void setNumeroArmario(String numeroArmario) {
		this.numeroArmario = numeroArmario;
	}

	@Column(name="NUM_ESTANTE", unique=false, nullable=true, insertable=true, updatable=true, length=20)
	public String getNumeroEstante() {
		return this.numeroEstante;
	}

	public void setNumeroEstante(String numeroEstante) {
		this.numeroEstante = numeroEstante;
	}

	@Column(name="NUM_PRATELEIRA", unique=false, nullable=true, insertable=true, updatable=true, length=20)
	public String getNumeroPrateleira() {
		return this.numeroPrateleira;
	}

	public void setNumeroPrateleira(String numeroPrateleira) {
		this.numeroPrateleira = numeroPrateleira;
	}

	@Column(name="NUM_SALA", unique=false, nullable=true, insertable=true, updatable=true, length=20)
	public String getNumeroSala() {
		return this.numeroSala;
	}

	public void setNumeroSala(String numeroSala) {
		this.numeroSala = numeroSala;
	}

	@Column(name="NUM_COLUNA", unique=false, nullable=true, insertable=true, updatable=true, length=20)
	public String getNumeroColuna() {
		return this.numeroColuna;
	}

	public void setNumeroColuna(String numeroColuna) {
		this.numeroColuna = numeroColuna;
	}
	
	@Column(name="FLG_ATUALIZADO")   
    @org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")	
	public Boolean getFlagAtualizado() {
		return this.flagAtualizado;
	}
    
	public void setFlagAtualizado(Boolean flagAtualizado) {
		this.flagAtualizado = flagAtualizado;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PETICAO_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public PeticaoSetor getPeticaoSetor() {
		return peticaoSetor;
	}

	public void setPeticaoSetor(PeticaoSetor peticaoSetor) {
		this.peticaoSetor = peticaoSetor;
	}     
    
	@Transient
	public String getLocalizacao(){
		StringBuffer localizacao = new StringBuffer("");
		if(numeroSala!=null&&!numeroSala.equals("")){
			localizacao.append("Sala: "+numeroSala);
		}
		if(numeroArmario!=null&&!numeroArmario.equals("")){
			localizacao.append(" Armário: "+numeroArmario);
		}
		if(numeroEstante!=null&&!numeroEstante.equals("")){
			localizacao.append(" Estante: "+numeroEstante);
		}
		if(numeroPrateleira!=null&&!numeroPrateleira.equals("")){
			localizacao.append(" Prateleira: "+numeroPrateleira);
		}
		if(numeroColuna!=null&&!numeroColuna.equals("")){
			localizacao.append(" Coluna: "+numeroColuna);
		}
		return localizacao.toString();
	}
/*	
	@Transient
	public Boolean isHistoricoDeslocamentoProcessoSetor() {
		if(processoSetor != null && peticaoSetor != null)
			throw new IllegalStateException("O deslocamento deve conter um ProcessoSetor ou uma PeticaoSetor");
		
		if(processoSetor != null)
			return true;
		else if(peticaoSetor != null)
			return false;
		else 
			return null;
	}
*/		
}