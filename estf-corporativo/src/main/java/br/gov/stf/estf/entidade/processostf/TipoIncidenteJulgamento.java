package br.gov.stf.estf.entidade.processostf;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("IJ")
public class TipoIncidenteJulgamento extends TipoRecurso {

	private static final long serialVersionUID = 3517824244630465347L;
	public static final String SIGLA_REPERCUSSAO_GERAL = "RG";
	public static final String SIGLA_TUTELA_PROVISORIA = "TP";
	public static final String SIGLA_QUESTAO_ORDEM = "QO";
	public static final String SIGLA_MEDIDA_CAUTELAR = "MC";
	public static final String SIGLA_QUESTAO_CONSTITUCIONAL = "QC";
	public static final String SIGLA_MERITO = "M";
	public static final String SIGLA_REPERCUSSAO_GERAL_QUESTAO_ORDEM = "RG-QO";
	public static final String SIGLA_REFERENDO = "Ref";
	public static final String SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO = "RG2JULG";
	public static final String SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO = "QC2JULG";

	public static final TipoIncidenteJulgamento MERITO = new TipoIncidenteJulgamento(0L, "Mérito", SIGLA_MERITO);

	public TipoIncidenteJulgamento() {
		super();
	}

	private TipoIncidenteJulgamento(Long id, String descricao, String sigla) {
		setId(id);
		setDescricao(descricao);
		setSigla(sigla);
	}

}
