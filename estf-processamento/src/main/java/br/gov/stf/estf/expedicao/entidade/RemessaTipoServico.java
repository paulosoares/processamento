package br.gov.stf.estf.expedicao.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "REMESSA_TIPO_SERVICO")
public class RemessaTipoServico extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private TipoServico tipoServico;
    private Remessa remessa;

    public RemessaTipoServico() {
    }

    @Id
    @Column(name = "SEQ_REMESSA_TIPO_SERVICO")
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence", sequenceName = "EXPEDICAO.SEQ_REMESSA_TIPO_SERVICO", allocationSize = 1)
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @JoinColumn(name = "SEQ_TIPO_SERVICO", referencedColumnName = "SEQ_TIPO_SERVICO")
    @ManyToOne(optional = false)
    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    @JoinColumn(name = "SEQ_REMESSA", referencedColumnName = "SEQ_REMESSA")
    @ManyToOne(optional = false)
    public Remessa getRemessa() {
        return remessa;
    }

    public void setRemessa(Remessa remessa) {
        this.remessa = remessa;
    }
}