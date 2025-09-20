package br.gov.stf.estf.entidade.processostf;



public enum CaracteristicaDistribuicao {

	EXCLUSAO_TURMA(5L,"EXCLUSÃO DA TURMA"),
	EXCLUSAO_MINISTROS_ELEITORAL(9L,"EXCLUSÃO DOS MINISTROS DO ELEITORAL"),
	COMUM(1L,"COMUM"),
	IMPEDIMENTO(6L,"IMPEDIMENTO"),
	EXCLUSAO_RELATOR_REVISOR(4L,"EXCLUSÃO DO RELATOR/REVISOR"),
	EXCEPC_68_1(8L,"EXCEPC. (ART 68.1 - RISTF)"),
	PREVENCAO_RELATOR_SUCESSOR(2L,"PREVENÇÃO DO RELATOR/SUCESSOR"),
	EXCEPRC_68(7L,"EXCEPC. (ART 68 - RISTF)"),
	PREVENCAO_TURMA(3L,"PREVENÇÃO DA TURMA");
	
	private Long codigo;
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	private String descricao;
	private CaracteristicaDistribuicao(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public static CaracteristicaDistribuicao valueOf(Long codigo) {
		if (codigo != null) {
			for (CaracteristicaDistribuicao cd : values()) {
				if (codigo.equals(cd.getCodigo())) {
					return cd;
				}
			}
		}
		throw new RuntimeException("Nao existe CaracteristicaDistribuicao com codigo: " + codigo);
	}
}

