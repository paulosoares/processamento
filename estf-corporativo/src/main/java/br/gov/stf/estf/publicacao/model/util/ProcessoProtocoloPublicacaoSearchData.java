package br.gov.stf.estf.publicacao.model.util;

import java.util.List;

import br.gov.stf.framework.util.SearchData;

public class ProcessoProtocoloPublicacaoSearchData extends SearchData {
	
	public String siglaClasseProcessual;
	public Long numeroProcesso;
	public Long numeroProtocolo;
	public Short anoProtocolo;
	public Long numeroMateria;
	public Short anoMateria;
	public Short codigoCapitulo;
	public List<Short> listaCodigoMateria;
	public Short codigoConteudo;
	public boolean retornoProcessoPublicado;
	
	
	/**
	 * true = retona o objeto ProcessoPublicado
	 * false = retona o objeto ProtocoloPublicado
	 * @return
	 */
	public boolean getRetornoProcessoPublicado(){
		if( (siglaClasseProcessual != null && siglaClasseProcessual.trim().length() > 0) ||
				numeroProcesso != null){
			return true;
		}else if( numeroProtocolo != null || anoProtocolo != null ){
			return false;
		}
		
		return retornoProcessoPublicado;
	}
	
	public boolean getPossuiInformacaoProcessual(){
		if( (siglaClasseProcessual != null && siglaClasseProcessual.trim().length() > 0) ||
				numeroProcesso != null){
			return true;
		}
		
		return false;
	}
	
	public boolean getPossuiInformacaoProtocolo(){
		if( numeroProtocolo != null || anoProtocolo != null ){
			return true;
		}
		return false;
	}
	
}
