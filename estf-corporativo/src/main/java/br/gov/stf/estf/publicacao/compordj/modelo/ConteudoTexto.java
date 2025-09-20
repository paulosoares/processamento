package br.gov.stf.estf.publicacao.compordj.modelo;


public class ConteudoTexto extends Conteudo {

	private static final long serialVersionUID = 1965308076071972747L;
	
	public static final int ALINHAMENTO_ESQUERDA = 0;
	public static final int ALINHAMENTO_CENTRALIZADO = 1;
	public static final int ALINHAMENTO_DIREITA = 2;
	public static final int ALINHAMENTO_JUSTIFICADO = 3;
	
	
	private byte[] texto;
	private int alinhamentoTexto;
	
		
	public int getAlinhamentoTexto() {
		return alinhamentoTexto;
	}

	public void setAlinhamentoTexto(int alinhamentoTexto) {
		this.alinhamentoTexto = alinhamentoTexto;
	}

	public void setTexto(byte[] texto){
		this.texto = texto;
	}

	public byte[] getTexto(){
		return this.texto;
	}

	public ConteudoTexto(byte[] texto, int alinhamentoTexto) {
		super();
		this.texto = texto;
		this.alinhamentoTexto = alinhamentoTexto;
	}
	
	public ConteudoTexto() {
		super();
	}
	
}