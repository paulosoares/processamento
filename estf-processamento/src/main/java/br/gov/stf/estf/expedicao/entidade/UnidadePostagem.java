package br.gov.stf.estf.expedicao.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "UNIDADE_POSTAGEM")
public class UnidadePostagem extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String codigoUnidadePostagem;
    private String nomeUnidadePostagem;
    private Boolean principal;
    private Boolean ativo;

    public UnidadePostagem() {
    }

    @Id
    @Column(name = "SEQ_UNIDADE_POSTAGEM")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "COD_UNIDADE_POSTAGEM")
    public String getCodigoUnidadePostagem() {
        return codigoUnidadePostagem;
    }

    public void setCodigoUnidadePostagem(String codigoUnidadePostagem) {
        this.codigoUnidadePostagem = codigoUnidadePostagem;
    }

    @Basic(optional = false)
    @Column(name = "NOM_UNIDADE_POSTAGEM")
    public String getNomeUnidadePostagem() {
        return nomeUnidadePostagem;
    }

    public void setNomeUnidadePostagem(String nomeUnidadePostagem) {
        this.nomeUnidadePostagem = nomeUnidadePostagem;
    }

    @Basic(optional = false)
    @Column(name = "FLG_PRINCIPAL")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    @Column(name = "FLG_ATIVO")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}