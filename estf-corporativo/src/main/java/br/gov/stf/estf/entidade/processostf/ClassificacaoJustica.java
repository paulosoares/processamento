package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

public class ClassificacaoJustica extends GenericEnum<Long, ClassificacaoJustica> {

	private static final long serialVersionUID = -7758322351294622981L;

	public static final ClassificacaoJustica JUSTICA_COMUM_ESTADUAL = new ClassificacaoJustica(61, "JUSTIÇA COMUM ESTADUAL", "JCE");
	public static final ClassificacaoJustica MINISTERIO_PUBLICO_UNIAO = new ClassificacaoJustica(62, "MINISTÉRIO PUBLICO DA UNIAO", "MPU");
	public static final ClassificacaoJustica MINISTERIO_PUBLICO_ESTADUAL = new ClassificacaoJustica(63, "MINISTÉRIO PÚBLICO ESTADUAL", "MPE");
	public static final ClassificacaoJustica JUSTICA_TRABALHO = new ClassificacaoJustica(64, "JUSTIÇA DO TRABALHO", "JTR");
	public static final ClassificacaoJustica JUSTICA_MILITAR_UNIAO = new ClassificacaoJustica(65, "JUSTIÇA MILITAR DA UNIÃO", "JMU");
	public static final ClassificacaoJustica JUSTICA_MILITAR = new ClassificacaoJustica(66, "JUSTIÇA MILITAR", "JMI");
	public static final ClassificacaoJustica JUSTICA_FEDERAL = new ClassificacaoJustica(67, "JUSTIÇA FEDERAL", "JFE");
	public static final ClassificacaoJustica JUSTICA_ELEITORAL = new ClassificacaoJustica(68, "JUSTIÇA ELEITORAL", "JEL");

	private final String sigla;
	private final String descricao;

	private ClassificacaoJustica(Long codigo) {
		this(codigo, "Classificacao Justiça " + codigo, null);
	}
	
	private ClassificacaoJustica(long codigo, String descricao, String sigla) {
		super(codigo);		
		this.descricao = descricao;
		this.sigla = sigla;
	}
	
	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static ClassificacaoJustica valueOf(Long codigo) {
		return valueOf(ClassificacaoJustica.class, codigo);		
	}
	
	public static ClassificacaoJustica[] values() {
		return values(new ClassificacaoJustica[0], ClassificacaoJustica.class);
	}

}
