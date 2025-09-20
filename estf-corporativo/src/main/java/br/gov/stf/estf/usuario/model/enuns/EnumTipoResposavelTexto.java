package br.gov.stf.estf.usuario.model.enuns;

public enum EnumTipoResposavelTexto {
	USUARIO() {
		@Override
		public String getCodigo() {
			return "1";
		}

		@Override
		public String getDescricao() {
			return "Usuários do Setor";
		}
	},

	GRUPO() {
		@Override
		public String getCodigo() {
			return "2";
		}

		@Override
		public String getDescricao() {
			return "Grupos do e-Gab";
		}
	},

	AMBOS() {
		@Override
		public String getCodigo() {
			return "3";
		}

		@Override
		public String getDescricao() {
			return "Ambos";
		}
	};
	
	public abstract String getCodigo();

	public abstract String getDescricao();
	
	public static EnumTipoResposavelTexto getTipoByCodigo(String codigo){
		for (EnumTipoResposavelTexto tipoResposavel : EnumTipoResposavelTexto.values()) {
			if (tipoResposavel.getCodigo().equals(codigo)) {
				return tipoResposavel;
			}
		}
		return null;
	}
}

