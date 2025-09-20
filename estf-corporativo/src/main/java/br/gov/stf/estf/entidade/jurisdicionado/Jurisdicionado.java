/**
 * 
 */
package br.gov.stf.estf.entidade.jurisdicionado;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoIdentificacao;

/**
 * @author Paulo.Estevao
 * @since 07.02.2011
 */
@Entity
@Table(schema = "JUDICIARIO", name = "JURISDICIONADO")
@SecondaryTable(schema = "JUDICIARIO", name = "INFORMACAO_JURISDICIONADO", 
		pkJoinColumns = @PrimaryKeyJoinColumn(name = "SEQ_JURISDICIONADO"))
public class Jurisdicionado extends ESTFBaseEntity<Long> {

		private static final long serialVersionUID = 1L;
		private Long id;
		private String nome;
		private Boolean ativo;
		private Jurisdicionado jurisdicionadoPai;
		private List<IdentificacaoPessoa> identificadoresJurisdicionado;
		private List<EnderecoJurisdicionado> enderecosJurisdicionado;
		private List<TelefoneJurisdicionado> telefonesJurisdicionado;
		private List<PapelJurisdicionado> papeisJurisdicionado;
		private String tipoPessoa;
		private String tipoMeioIntimacao;
		private Date dataValidadeCadastro;
		private String email;
		private Boolean entidadeGovernamental;
		private String observacao;
	//	private String siglaUsuario;

		@Id
		@Column(name = "SEQ_JURISDICIONADO")
		@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
		@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_JURISDICIONADO", allocationSize = 1)
		public Long getId() {
			return this.id;
		}

		public void setId(Long identifier) {
			this.id = identifier;
		}

		@Column(name = "NOM_JURISDICIONADO", nullable = false)
		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}
		
		@Column(name = "TIP_PESSOA")
		public String getTipoPessoa() {
			return tipoPessoa;
		}

		public void setTipoPessoa(String tipoPessoa) {
			this.tipoPessoa = tipoPessoa;
		}
		

		@Column(name = "FLG_ATIVO")
		@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
		public Boolean getAtivo() {
			return ativo;
		}

		public void setAtivo(Boolean ativo) {
			this.ativo = ativo;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SEQ_JURISDICIONADO_PAI", unique = false, nullable = true, insertable = true, updatable = true)
		public Jurisdicionado getJurisdicionadoPai() {
			return jurisdicionadoPai;
		}

		public void setJurisdicionadoPai(Jurisdicionado jurisdicionadoPai) {
			this.jurisdicionadoPai = jurisdicionadoPai;
		}
		
		@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER, mappedBy = "jurisdicionado")
		public List<IdentificacaoPessoa> getIdentificadoresJurisdicionado() {
			return identificadoresJurisdicionado;
		}

		public void setIdentificadoresJurisdicionado(
				List<IdentificacaoPessoa> identificadoresJurisdicionado) {
			this.identificadoresJurisdicionado = identificadoresJurisdicionado;
		}

		@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy = "jurisdicionado")
		@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
		public List<EnderecoJurisdicionado> getEnderecosJurisdicionado() {
			return enderecosJurisdicionado;
		}

		public void setEnderecosJurisdicionado(List<EnderecoJurisdicionado> enderecosJurisdicionado) {
			this.enderecosJurisdicionado = enderecosJurisdicionado;
		}

		@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy = "jurisdicionado")
		@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
		public List<TelefoneJurisdicionado> getTelefonesJurisdicionado() {
			return telefonesJurisdicionado;
		}

		public void setTelefonesJurisdicionado(List<TelefoneJurisdicionado> telefonesJurisdicionado) {
			this.telefonesJurisdicionado = telefonesJurisdicionado;
		}

		@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy = "jurisdicionado")
		public List<PapelJurisdicionado> getPapeisJurisdicionado() {
			return papeisJurisdicionado;
		}

		public void setPapeisJurisdicionado(
				List<PapelJurisdicionado> papeisJurisdicionado) {
			this.papeisJurisdicionado = papeisJurisdicionado;
		}

		@Column(name="TIP_MEIO_INTIMACAO")
		public String getTipoMeioIntimacao() {
			return tipoMeioIntimacao;
		}
		
		public void setTipoMeioIntimacao(String tipoMeioIntimacao) {
			this.tipoMeioIntimacao = tipoMeioIntimacao;
		}

		@Temporal(TemporalType.DATE)
		@Column(name = "DAT_VALIDADE_CADASTRO", unique = false, nullable = true, insertable = true, updatable = true)
		public Date getDataValidadeCadastro() {
			return dataValidadeCadastro;
		}

		public void setDataValidadeCadastro(Date dataValidadeCadastro) {
			this.dataValidadeCadastro = dataValidadeCadastro;
		}

		@Column(name = "DSC_CORREIO_ELETRONICO", table = "INFORMACAO_JURISDICIONADO")
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@Column(name = "FLG_ENTIDADE_GOVERNAMENTAL")
		@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
		public Boolean getEntidadeGovernamental() {
			return entidadeGovernamental;
		}

		public void setEntidadeGovernamental(Boolean entidadeGovernamental) {
			this.entidadeGovernamental = entidadeGovernamental;
		}
		
		@Transient
		public String getCpf() {
			IdentificacaoPessoa identificadorCpf = getIdentificacaoPessoa(EnumTipoIdentificacao.CPF
					.getSigla());
			if (identificadorCpf != null) {
				return identificadorCpf.getDescricaoIdentificacao();
			}
			return "";
		}
		
		@Transient
		public String getOab() {
			IdentificacaoPessoa identificadorOab = getIdentificacaoPessoa(EnumTipoIdentificacao.OAB
					.getSigla());
			if (identificadorOab != null){
				if (identificadorOab.getSiglaUfOrgaoExpedidor() != null) {
					return identificadorOab.getDescricaoIdentificacao() + "/" +identificadorOab.getSiglaUfOrgaoExpedidor();
				}else{
					return identificadorOab.getDescricaoIdentificacao();
				}
			}	 
			return "";
		}


		@Transient
		public List<IdentificacaoPessoa> getOabs() {
			List<IdentificacaoPessoa> identificadorOab = new ArrayList<IdentificacaoPessoa>();
			for (IdentificacaoPessoa item : getIdentificadoresJurisdicionado()) {
				if (item != null && item.getTipoIdentificacao().getId().equals(EnumTipoIdentificacao.OAB.getId())) {
					identificadorOab.add(item);
					}
				}
			return identificadorOab;
		}
		
		
		@Transient
		public IdentificacaoPessoa getIdentificacaoPessoa(String siglaTipoIdentificacao) {
			if (getIdentificadoresJurisdicionado() != null
					&& getIdentificadoresJurisdicionado().size() > 0) {
				for (IdentificacaoPessoa item : getIdentificadoresJurisdicionado()) {
					if (siglaTipoIdentificacao.equals(item.getTipoIdentificacao().getSiglaTipoIdentificacao())) {
						return item;
					}
				}
			}
			return null;
		}

		@Column(name = "TXT_OBSERVACAO", nullable = false)
		public String getObservacao() {
			return observacao;
		}

		public void setObservacao(String observacao) {
			this.observacao = observacao;
		}
		/*
		@Formula("(SELECT sig_usuario FROM (SELECT u.sig_usuario, ROW_NUMBER() OVER (PARTITION BY u.seq_pessoa ORDER BY u.dat_alteracao DESC) linha FROM corporativo.usuario u WHERE u.seq_pessoa = SEQ_JURISDICIONADO AND u.flg_ativo = 'S' AND u.flg_usuario_externo = 'S') WHERE linha = 1)")
		public String getSiglaUsuario() {
			return siglaUsuario;
		}
		
		public void setSiglaUsuario(String siglaUsuario) {
			this.siglaUsuario = siglaUsuario;
		}
*/
		// ======================================================================================
		// Comparators
		// ======================================================================================
		/**
		 * Comparator utilizado para ordenar os registro em ordem alfabética
		 * crescente pelo nome do jurisdicionado
		 */
		public static class NomeJurisdicionadoAscComparator implements
				Comparator<Jurisdicionado> {
			public int compare(Jurisdicionado jurisdicionado1,
					Jurisdicionado jurisdicionado2) {
				if (jurisdicionado1 != null && jurisdicionado2 != null
						&& jurisdicionado1.getNome() != null
						&& jurisdicionado2.getNome() != null) {
					return jurisdicionado1.getNome().compareTo(
							jurisdicionado2.getNome());
				} else if (jurisdicionado1 == null && jurisdicionado2 == null) {
					return 0;
				} else if (jurisdicionado1 == null) {
					return 1;
				} else if (jurisdicionado2 == null) {
					return -1;
				} else if (jurisdicionado1.getNome() == null
						&& jurisdicionado2.getNome() == null) {
					return 0;
				} else if (jurisdicionado1.getNome() == null) {
					return 1;
				} else {
					return -1;
				}
			}
		}

	}