package br.gov.stf.estf.expedicao.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "CONFIGURACAO_ENCAMINHAMENTO")
public class ConfiguracaoEncaminhamento extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private short codigoAndamento;
    private String titulo;
    private String mensagem;

    @Id
    @Basic(optional = false)
    @Column(name = "SEQ_CONFIGURACAO_ENCAMINHAMENT")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "COD_ANDAMENTO")
    public short getCodigoAndamento() {
        return codigoAndamento;
    }

    public void setCodigoAndamento(short codigoAndamento) {
        this.codigoAndamento = codigoAndamento;
    }

    @Column(name = "DSC_TITULO")
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Column(name = "DSC_MENSAGEM")
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}