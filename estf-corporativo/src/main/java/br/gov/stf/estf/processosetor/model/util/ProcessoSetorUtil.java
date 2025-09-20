package br.gov.stf.estf.processosetor.model.util;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.framework.util.SearchData;

public class ProcessoSetorUtil {

	public Object recuperaIndentificacaoProcesso(String identificacao, boolean numero, List<Classe> listaClasse, List<ClasseConversao> listaClasseAntiga){
		
		if( !SearchData.stringNotEmpty(identificacao) )
			return null;
		
		String[] arrayProcesso;
		String classe = "";
		String numProc = "";
		Long tstNum = null;
		String tstClasse = null;

//		identificacao = identificacao.replace(";", "");
		arrayProcesso= identificacao.split(";");
		
		for(Short x = 0; x < arrayProcesso.length; x++) { //varre o arrayProcesso
			String identificacaoTemp = arrayProcesso[x];
			
			if( SearchData.stringNotEmpty(identificacaoTemp) ){
				identificacaoTemp = identificacaoTemp.trim().replace("/", "");
				
				for (int i = 0; i < identificacaoTemp.length(); i++) {
					if( ( identificacaoTemp.codePointAt(i) >= 65 && identificacaoTemp.codePointAt(i) <= 90 )
					    || (identificacaoTemp.codePointAt(i) >= 97 && identificacaoTemp.codePointAt(i) <= 122 ) ) {				 
						classe = classe + identificacaoTemp.charAt(i);
					}
				}
				
				numProc = identificacaoTemp.replace(classe, "");
				numProc = numProc.trim();
				
			}
			if( numero ){
				if( numProc.trim().length() > 0 ) {
					if( arrayProcesso[x].length() == 14 || numProc.trim().length() == 8 )
						tstNum = Long.parseLong(numProc.substring(0, numProc.length() - 1).trim());
					else
						tstNum = Long.parseLong(numProc.trim());
				}
				
			}else{
				if( classe.trim().length() > 0)
					tstClasse = convertClasseProcessual(classe.trim(), listaClasse, listaClasseAntiga);
			}
		}
		
		if( numero )
			return tstNum;
		else
			return tstClasse;
	}
	
	public String convertClasseProcessual( String classe, List<Classe> listaClasse, List<ClasseConversao> listaClasseAntiga ){
		if( listaClasse != null && listaClasseAntiga != null && classe != null && classe.trim().length() > 0){
			for( Classe nova : listaClasse ){
				if( nova.getId().equalsIgnoreCase(classe) ){
					return nova.getId();
				}
			}
			
			for( ClasseConversao antiga : listaClasseAntiga ){
				if( antiga.getId().equalsIgnoreCase(classe) ){
					return antiga.getClasseNova();
				}
			}
		}
		return classe;
	}
	
	public boolean possuiLetra(String identificacao){
		if( identificacao != null ){
			for (int i = 0; i < identificacao.length(); i++) {
				if( ( identificacao.codePointAt(i) >= 65 && identificacao.codePointAt(i) <= 90 )
						|| (identificacao.codePointAt(i) >= 97 && identificacao.codePointAt(i) <= 122 ) ) {				 
					return true;
				}
			}
		}
		return false;
	}
	
	public Object recuperaIndentificacaoProtocolo(String identificacao, boolean numero){
		String numProt = "";
		String ano = "";
		if( SearchData.stringNotEmpty(identificacao) ){
			
			if( identificacao.contains("/") ){
				int posicao = identificacao.indexOf("/");
				numProt = identificacao.substring(0,posicao);
				ano = identificacao.substring(posicao+1, identificacao.trim().length());
			}else{
				numProt = identificacao.substring(0, identificacao.length() - 4);
				ano = identificacao.substring(identificacao.length() - 4, identificacao.length());
			}
			
		}
		if( numero ){
			if( numProt.trim().length() > 0)
				return Long.parseLong(numProt.trim());
			
		}else{
			if( ano.trim().length() > 0)
				return Short.parseShort(ano.trim());
		}
		return null;
	}
	
}
