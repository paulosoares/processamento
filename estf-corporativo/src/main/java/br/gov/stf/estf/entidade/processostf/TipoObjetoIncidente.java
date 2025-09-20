package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

/**
 * Tipo do Objeto Incidente. Pode ter os valores: PR (Processo), PI (Protocolo), PA (Petição), 
 * RC (Recurso) ou IJ (Incidente Julgamento).
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
public class TipoObjetoIncidente extends GenericEnum<String, TipoObjetoIncidente> {

	private static final long serialVersionUID = 7527403056484898944L;
	
	public static final TipoObjetoIncidente PROCESSO = new TipoObjetoIncidente("PR", "Processo");
	public static final TipoObjetoIncidente PROTOCOLO = new TipoObjetoIncidente("PI", "Protocolo");
	public static final TipoObjetoIncidente PETICAO = new TipoObjetoIncidente("PA", "Petição");
	public static final TipoObjetoIncidente PETICAO_ELETRONICA = new TipoObjetoIncidente("PE", "Petição Eletrônica");
	public static final TipoObjetoIncidente RECURSO = new TipoObjetoIncidente("RC", "Recurso");
	public static final TipoObjetoIncidente INCIDENTE_JULGAMENTO = new TipoObjetoIncidente("IJ", "Incidente Julgamento");
	public static final TipoObjetoIncidente SUMULA = new TipoObjetoIncidente("SU", "Súmula");

	private final String descricao;
	private String sigla;

	private TipoObjetoIncidente(String sigla) {
		this(sigla, "Tipo Objeto Incidente:" + sigla);
	}

	private TipoObjetoIncidente(String sigla, String descricao) {
		super(sigla);
		this.descricao = descricao;
	}
	
	public String getSigla(){
		return sigla;
	}

	public static TipoObjetoIncidente valueOf(String tipoObjetoIncidente) {
		return valueOf(TipoObjetoIncidente.class, tipoObjetoIncidente);
	}

	public static TipoObjetoIncidente[] values() {
		return values(new TipoObjetoIncidente[0], TipoObjetoIncidente.class);
	}

	public String getDescricao() {
		return descricao;
	}

}
