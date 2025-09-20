package br.gov.stf.estf.processosetor.model.service.impl;

import br.gov.stf.framework.util.SearchData;

public class ProcessoSetorEletronicoSearchData extends SearchData{


	/**
	 *  Se inserir um novo parâmetro de pesquisa, lembrar de adicionar na classe ProcessoSetorXMLBind
	 */
	private static final long serialVersionUID = -6272190092778078252L;
	
	public static enum TipoQTD{
		QTD_PROCESSO,
		QTD_MC,
		QTD_RECURSO,
		QTD_TOTAL,
		QTD_SEM_RESPONSAVEL,
		QTD_SEM_FASE,
		QTD_INCIDENTE_USUARIO,
		QTD_PROCESSO_INATIVO
	}
	
	public static enum TipoOrdem{
		ID_PROCESSO,
		DT_ENTRADA
	}
	
	public boolean crescente = true;
	public TipoOrdem tipoOrdem;
	
	public TipoQTD tipoQTD;
	public Long codSetor;
	public String idUsuarioAutenticado;
	public Integer quantidade;
	 
}
