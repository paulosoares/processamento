package br.gov.stf.estf.entidade.jurisdicionado.enuns;

public enum EnumTipoIdentificacao {

	CPF() {
		@Override
		public String getSigla() {
			return "CPF";
		}

		@Override
		public String getDescricao() {
			return "CPF";
		}
		
		@Override
		public Long getId() {
			return 2L;
		}
	},

	OAB() {
		@Override
		public String getSigla() {
			return "OAB";
		}

		@Override
		public String getDescricao() {
			return "OAB";
		}
		@Override
		public Long getId() {
			return 3L;
		}
	},

	CNPJ() {
		@Override
		public String getSigla() {
			return "CNPJ";
		}

		@Override
		public String getDescricao() {
			return "CNPJ";
		}
		@Override
		public Long getId() {
			return 4L;
		}
	},
	CN(){
		@Override
		public String getSigla() {
			return "CN";
		}

		@Override
		public String getDescricao() {
			return "Certidão de Nascimento";
		}
		@Override
		public Long getId() {
			return 5L;
		}
	},
	PASS(){
		@Override
		public String getSigla() {
			return "PASS";
		}

		@Override
		public String getDescricao() {
			return "Passaporte";
		}
		@Override
		public Long getId() {
			return 1L;
		}
	},
	RG(){
		@Override
		public String getSigla() {
			return "RG";
		}

		@Override
		public String getDescricao() {
			return "Identidade";
		}
		@Override
		public Long getId() {
			return 22L;
		}
	};
	
	public abstract String getSigla();

	public abstract Long getId();

	public abstract String getDescricao();
}

