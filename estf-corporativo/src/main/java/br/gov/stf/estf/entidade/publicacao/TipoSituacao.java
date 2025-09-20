package br.gov.stf.estf.entidade.publicacao;

import java.util.Date;

import br.gov.stf.framework.util.DateTimeHelper;

public enum TipoSituacao {
	EM_ELABORACAO("Em elaboração","em"),
	COMPOSICAO_PARCIAL("Composição parcial","em"),
	COMPOSICAO_DJ("DJ composto","em"),
	PREVISTO_DJ("DJ previsto","para"),
	PUBLICACAO_DJ("DJ publicado","em");
	
	private String descricao;
	private String prefixo;
	
	private TipoSituacao( String descricao, String prefixo){
		this.descricao = descricao;
		this.prefixo = prefixo;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	public String getDescricaoData(Date data){
		if( data != null ){
			return this.descricao +" "+ this.prefixo + " "+ DateTimeHelper.getDataString(data);
		}else{
			return getDescricao();
		}
	}
}
