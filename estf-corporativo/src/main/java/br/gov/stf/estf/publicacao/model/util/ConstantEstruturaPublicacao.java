package br.gov.stf.estf.publicacao.model.util;

public enum ConstantEstruturaPublicacao {
	
	CAPITULO_PLENARIO((short)2),
	  MATERIA_REPERCUSSAO_GERAL((short)7), 
	   
	CAPITULO_SECRETARIA_JUDICIARIA((short)6),
	  MATERIA_PROCESSOS_ORIGINARIOS((short)2),
	  MATERIA_RECURSOS((short)3),	                                	                 
	  MATERIA_REPUBLICACOES((short)5),	                           	                 
	  MATERIA_PROCESSOS_COMPETENCIA_PRESIDENCIA((short)7),	 	                 
	  MATERIA_DESPACHOS_DECISOES_PRESIDENTE((short)10),	        	                 
	  MATERIA_PETICOES_AVULSAS((short)11),	                        	                 
	  MATERIA_PROTOCOLO((short)12),	                               	                 
	  MATERIA_REPUBLICACOES_PROTOCOLOS((short)13),
	  
	   CONTEUDO_RELACAO_PROCESSO((short)50);
	

	private Short codigo;

	private ConstantEstruturaPublicacao(Short codigo){
		this.codigo = codigo;
	}

	public Short getCodigo(){
		return this.codigo;
	}
}
