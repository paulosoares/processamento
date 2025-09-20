package br.gov.stf.estf.documento.model.util;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.util.SearchData;

public class TextoSearchData extends SearchData {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ObjetoIncidente<?> objetoIncidente;
	public TipoTexto tipoTexto;
	public List<TipoTexto> listaTipoTexto;
	public TipoOrdem ordem;
	public Long codigoMinistro;
	public Boolean ministroDiferenteAutenticado;
	public Boolean publico;
	public Boolean textosIguais;
	public Long idArquivoEletronico;
	
	
	public static enum TipoOrdem{
		DATA_SESSAO,
		TIPO_TEXTO,
		TIPO_TEXTO_DATA_SESSAO
	}
}
