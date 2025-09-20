package br.gov.stf.estf.expedicao.entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.expedicao.visao.Util;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "VW_SERVIDOR_ASSINADOR")
public class VwServidorAssinador extends ESTFBaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private long orgaoEmp;
    private String nomeFuncionario;
    private String ramal;

    @Id
    @Basic(optional = false)
    @Column(name = "NUM_MATRICULA")
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "SEQ_ORGAO_EMP")
    public long getOrgaoEmp() {
        return orgaoEmp;
    }

    public void setOrgaoEmp(long orgaoEmp) {
        this.orgaoEmp = orgaoEmp;
    }

    @Basic(optional = false)
    @Column(name = "NOM_FUNCIONARIO")
    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    @Column(name = "RAMAL")
    public String getRamal() {
        return ramal;
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    @Transient
    public String getSiglasNomeFuncionario() {
    	return Util.getSiglasNomeFuncionario(nomeFuncionario);
    }
}