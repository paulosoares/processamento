package br.gov.stf.estf.expedicao.entidade;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "CONTRATO_POSTAGEM")
public class ContratoPostagem extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String numero;
    private String cartao;
    private String codigoAdministrativo;
    private String numeroDiretoriaRegional;
    private String usuarioAutenticacaoWS;
    private String senhaAutenticacaoWS;
    private Date dataVigenciaInicial;
    private Date dataVigenciaFinal;

    public ContratoPostagem() {
    }

    @Id
    @Column(name = "SEQ_CONTRATO_POSTAGEM")
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence", sequenceName = "EXPEDICAO.SEQ_CONTRATO_POSTAGEM", allocationSize = 1)
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NUM_CONTRATO_POSTAGEM")
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Basic(optional = false)
    @Column(name = "NUM_CARTAO_POSTAGEM")
    public String getCartao() {
        return cartao;
    }

    public void setCartao(String cartao) {
        this.cartao = cartao;
    }

    @Basic(optional = false)
    @Column(name = "COD_ADMINISTRATIVO")
    public String getCodigoAdministrativo() {
        return codigoAdministrativo;
    }

    public void setCodigoAdministrativo(String codigoAdministrativo) {
        this.codigoAdministrativo = codigoAdministrativo;
    }

    @Basic(optional = false)
    @Column(name = "NUM_DIRETORIA_REGIONAL")
    public String getNumeroDiretoriaRegional() {
		return numeroDiretoriaRegional;
	}

	public void setNumeroDiretoriaRegional(String numeroDiretoriaRegional) {
		this.numeroDiretoriaRegional = numeroDiretoriaRegional;
	}

	@Basic(optional = false)
    @Column(name = "SIG_USUARIO_AUTENTICACAO")
    public String getUsuarioAutenticacaoWS() {
        return usuarioAutenticacaoWS;
    }

    public void setUsuarioAutenticacaoWS(String usuarioAutenticacaoWS) {
        this.usuarioAutenticacaoWS = usuarioAutenticacaoWS;
    }

    @Basic(optional = false)
    @Column(name = "DSC_SENHA_AUTENTICACAO")
    public String getSenhaAutenticacaoWS() {
        return senhaAutenticacaoWS;
    }

    public void setSenhaAutenticacaoWS(String senhaAutenticacaoWS) {
        this.senhaAutenticacaoWS = senhaAutenticacaoWS;
    }

    @Basic(optional = false)
    @Column(name = "DAT_VIGENCIA_INICIAL")
	public Date getDataVigenciaInicial() {
		return dataVigenciaInicial;
	}

	public void setDataVigenciaInicial(Date dataVigenciaInicial) {
		this.dataVigenciaInicial = dataVigenciaInicial;
	}

    @Basic(optional = true)
    @Column(name = "DAT_VIGENCIA_FINAL")
	public Date getDataVigenciaFinal() {
		return dataVigenciaFinal;
	}

	public void setDataVigenciaFinal(Date dataVigenciaFinal) {
		this.dataVigenciaFinal = dataVigenciaFinal;
	}
}