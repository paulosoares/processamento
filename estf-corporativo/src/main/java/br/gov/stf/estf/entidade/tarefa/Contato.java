package br.gov.stf.estf.entidade.tarefa;


public class Contato {
	
	private TarefaSetor tarefaSetor;
	private String forma;
	private String ligacao;
	private String destino;
	private String telefone;
	private String nome;
	private String data;
	private String hora;
	private String horaRetorno;
	private String dataRetorno;
	private String origem;
	private String observacao;
	private String providencia;
	
	public enum TipoContato{
		LIGACAO((long)241,"Ligações"),
		TELEFONE((long)242,"Telefone"),
		NOME((long)243, "Nome"),
		DATA((long)244, "Data"),
		ORIGEM((long)245, "Origem"),
		OBSERVACAO((long)246,"Observação"),
		HORA((long)247,"Hora"),
		FORMA((long)248,"Forma"),
		HORA_RETORNO((long)249,"Hora retorno"),
		DATA_RETORNO((long)250,"Data retorno"),
		PROVIDENCIA((long)251,"Providência"),
		DESTINO((long)501,"Destino");
	
		private Long codigo;
		private String descricao;

		private TipoContato(Long codigo,String descricao){
			this.codigo = codigo;
			this.descricao = descricao;
		}

		public Long getCodigo(){
			return this.codigo;
		}

		public String getDescricao(){
			return this.descricao;
		}

	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public TarefaSetor getTarefaSetor() {
		return tarefaSetor;
	}
	public void setTarefaSetor(TarefaSetor tarefaSetor) {
		this.tarefaSetor = tarefaSetor;
	}
	public String getForma() {
		return forma;
	}
	public void setForma(String forma) {
		this.forma = forma;
	}
	public String getLigacao() {
		return ligacao;
	}
	public void setLigacao(String ligacao) {
		this.ligacao = ligacao;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getHoraRetorno() {
		return horaRetorno;
	}
	public void setHoraRetorno(String horaRetorno) {
		this.horaRetorno = horaRetorno;
	}
	public String getDataRetorno() {
		return dataRetorno;
	}
	public void setDataRetorno(String dataRetorno) {
		this.dataRetorno = dataRetorno;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getProvidencia() {
		return providencia;
	}
	public void setProvidencia(String providencia) {
		this.providencia = providencia;
	}
	
	
	
	

	
}
