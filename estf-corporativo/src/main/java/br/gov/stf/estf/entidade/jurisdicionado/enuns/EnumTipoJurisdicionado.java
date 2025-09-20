package br.gov.stf.estf.entidade.jurisdicionado.enuns;

public enum EnumTipoJurisdicionado {
	
	PARTE() {
		public Long getValue() {
			return 1L;
		}
		
		public String getDescription() {
			return "Parte";
		}
	},
	
	ADVOGADO() {
		public Long getValue() {
			return 2L;
		}
		
		public String getDescription() {
			return "Advogado";
		}
	},
	
	ESTAGIARIO() {
		public Long getValue() {
			return 2L;
		}
		
		public String getDescription() {
			return "Estagiário";
		}
	};
	
	public abstract Long getValue();
	
	public abstract String getDescription();	

}
