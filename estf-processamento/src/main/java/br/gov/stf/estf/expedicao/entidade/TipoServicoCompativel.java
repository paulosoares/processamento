package br.gov.stf.estf.expedicao.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "TIPO_SERVICO_COMPATIVEL")
public class TipoServicoCompativel extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Boolean obrigatorio;
    private TipoServico tipoServicoNecessario;
    private TipoServico tipoServicoAdicional;
    private TipoServico tipoServicoPrincipal;

    public TipoServicoCompativel() {
    }

    @Id
    @Column(name = "SEQ_TIPO_SERVICO_COMPATIVEL")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "FLG_OBRIGATORIO")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    @JoinColumn(name = "SEQ_TIPO_SERVICO_NECESSARIO", referencedColumnName = "SEQ_TIPO_SERVICO")
    @ManyToOne
    public TipoServico getTipoServicoNecessario() {
        return tipoServicoNecessario;
    }

    public void setTipoServicoNecessario(TipoServico tipoServicoNecessario) {
        this.tipoServicoNecessario = tipoServicoNecessario;
    }

    @JoinColumn(name = "SEQ_TIPO_SERVICO_ADICIONAL", referencedColumnName = "SEQ_TIPO_SERVICO")
    @ManyToOne(optional = false)
    public TipoServico getTipoServicoAdicional() {
        return tipoServicoAdicional;
    }

    public void setTipoServicoAdicional(TipoServico tipoServicoAdicional) {
        this.tipoServicoAdicional = tipoServicoAdicional;
    }

    @JoinColumn(name = "SEQ_TIPO_SERVICO_PRINCIPAL", referencedColumnName = "SEQ_TIPO_SERVICO")
    @ManyToOne(optional = false)
    public TipoServico getTipoServicoPrincipal() {
        return tipoServicoPrincipal;
    }

    public void setTipoServicoPrincipal(TipoServico tipoServicoPrincipal) {
        this.tipoServicoPrincipal = tipoServicoPrincipal;
    }
}