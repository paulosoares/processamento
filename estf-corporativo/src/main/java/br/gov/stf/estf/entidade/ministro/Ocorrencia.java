package br.gov.stf.estf.entidade.ministro;

import java.io.Serializable;

///**
// * Representa as possíveis "ocorrências" de ministro em relação ao processo. 
// * Exemplos: ausente, prevenção, relator, etc.
// * 
// * @author Rodrigo Barreiros
// * @since 26.05.2009
// */
//@Entity
//@Table(name="OCORRENCIAS", schema="STF")
//public class Ocorrencia extends ESTFBaseEntity<String> {
//
//	private static final long serialVersionUID = -2209636937662065058L;
//	
//	public static final String CODIGO_OCORRENCIA_RELATOR_ACORDAO = "RA";
//
//	private String descricao;
//	
//	@Id
//	@Column(name = "COD_OCORRENCIA")
//	public String getId() {
//		return id;
//	}
//
//	@Column(name = "DSC_OCORRENCIA")
//	public String getDescricao() {
//		return descricao;
//	}
//
//	public void setDescricao(String descricao) {
//		this.descricao = descricao;
//	}
//
//}

public enum Ocorrencia implements Serializable {
	
	CONCORRE_A_DISTRIBUICAO ("CD","CONCORRE A DISTRIBUICAO"),
	EXCLUSAO ("EX","EXCLUSAO"),
	AUSENCIA ("AU","AUSENCIA"),
	PREVENCAO ("PR","PREVENCAO"),
	ELEITORAL ("EL","ELEITORAL"),
	RELATOR ("RE","RELATOR"),
	REDATOR_ACORDAO ("RA","REDATOR ACORDAO"),
	RELATOR_SUBSTITUTO ("SU","RELATOR SUBSTITUTO"),
	LICENCIADO_ATE_30_DIAS ("L3","LICENCIADO ATE 30 DIAS"),
	LICENCIADO_POR_MAIS_DE_30_DIAS ("LM","LICENCIADO POR MAIS DE 30 DIAS"),
	APOSENTADO ("AP","APOSENTADO"),
	PRESIDENTE_DA_SESSAO ("PS","PRESIDENTE DA SESSAO"),
	PRESIDENTE_DO_STF ("MP","PRESIDENTE DO STF"),
	IMPEDIDO ("IM","IMPEDIDO"),
	REGISTRADO ("RG","REGISTRADO"),
	VICE_PRESIDENTE_DO_STF ("VP","VICE-PRESIDENTE DO STF"),
	REGISTRADO_AO_PRESIDENTE_DA_TURMA ("RT","REGISTRADO AO PRESIDENTE DA TURMA"),
	PRESIDENTE_INTERINO_DO_STF ("PI","PRESIDENTE INTERINO DO STF"),
	SUBSTITUICAO_TEMPORARIA ("WW","Substituicao Temporaria"),
	REDATOR_ACORDAO_RISTF ("RR","REDATOR ACORDAO RISTF"),
	REVISOR ("RV","REVISOR"),
	REVISOR_SUBSTITUTO ("RS","REVISOR SUBSTITUTO"),
	PRESIDENTE_DA_COMISSÃO_DE_JURISPRUDÊNCIA ("PJ","PRESIDENTE DA COMISSÃO DE JURISPRUDÊNCIA"),
	PRESIDENTE_DO_CNJ ("PC","PRESIDENTE DO CNJ"),
	RELATOR_DO_INCIDENTE ("RI","RELATOR DO INCIDENTE");

        
    private String codigo = null;        
	private String descricao = null;        
	
	private Ocorrencia(String codigo, String descricao) {
	    this.codigo = codigo;
            this.descricao = descricao;
	}
        
    public String getCodigo() {
        return codigo;
    }
	
	public String getDescricao() {
            return descricao;
	}
       
	public static Ocorrencia valueOfCodigo(String codigo) {
		if (codigo != null) {
			for (Ocorrencia ocorrencia : values()) {
				if (codigo.equals(ocorrencia.getCodigo())) {
					return ocorrencia;
				}
			}
		}
		throw new RuntimeException("Nao existe Ocorrencia com codigo: " + codigo);
	}
}
