package br.gov.stf.estf.entidade.alerta;


public enum ConcatenarFiltroConstante {

		E("E"),
		OU("OU");

		private String	codigo;

		private ConcatenarFiltroConstante(String codigo) {
			this.codigo = codigo;
		}

		public String getCodigo() {
			return this.codigo;
		}


		public ConcatenarFiltroConstante getConcatenarFiltroConstante(String codigo) {
			if (ConcatenarFiltroConstante.E.getCodigo().equals(codigo)) {
				return ConcatenarFiltroConstante.E;
			} else {
				return ConcatenarFiltroConstante.OU;
			}
		}
	}
