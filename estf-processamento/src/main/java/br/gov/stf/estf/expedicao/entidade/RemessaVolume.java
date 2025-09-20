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
@Table(schema = "EXPEDICAO", name = "REMESSA_VOLUME")
public class RemessaVolume extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Integer numeroVolume;
    private Integer pesoGramas;
    private String numeroEtiquetaCorreios;
    private Remessa remessa;

    public RemessaVolume() {
    }

    @Id
    @Column(name = "SEQ_REMESSA_VOLUME")
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence", sequenceName = "EXPEDICAO.SEQ_REMESSA_VOLUME", allocationSize = 1)
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NUM_VOLUME")
    public Integer getNumeroVolume() {
        return numeroVolume;
    }

    public void setNumeroVolume(Integer numeroVolume) {
        this.numeroVolume = numeroVolume;
    }

    @Column(name = "QTD_PESO_G")
    public Integer getPesoGramas() {
        return pesoGramas;
    }

    public void setPesoGramas(Integer pesoGramas) {
        this.pesoGramas = pesoGramas;
    }

    @Column(name = "NUM_ETIQUETA_CORREIOS")
    public String getNumeroEtiquetaCorreios() {
        return numeroEtiquetaCorreios;
    }

    public void setNumeroEtiquetaCorreios(String numeroEtiquetaCorreios) {
        this.numeroEtiquetaCorreios = numeroEtiquetaCorreios;
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