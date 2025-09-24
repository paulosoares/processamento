package br.jus.stf.estf.decisao.handlers;

import java.util.*;

public class ConstantesDocumento {

	public static final String CHAR_HEIGHT = "CharHeight";
	public static final String CHAR_FONT_FAMILY = "CharFontFamily";
	public static final String CHAR_FONT_NAME = "CharFontName";
	public static final String ESTILO_RODAPE = "footnote";
	public static final String VALOR_ATRIBUTO_PADRAO_PARAGRAFO = "false";
	public static final String UNIDADE_TAMANHO_FONTE = "pt";
	public static final String PREFIXO_ESTILO_STF = "STF-";
	public static final String ESTILO_STF_PADRAO = PREFIXO_ESTILO_STF + "Padrão";
	public static final String ESTILO_STANDARD = "Standard";
	public static final String ESTILO_STF_CENTRALIZADO = ESTILO_STF_PADRAO + "Centralizado";
	public static final String ALINHAMENTO_PARAGRAFO = "text-align";
	public static final String ATRIBUTO_NEGRITO = "font-weight";
	public static final String ATRIBUTO_ITALICO = "font-style";
	public static final String ATRIBUTO_VERSALETE = "font-variant";
	public static final String NOME_ESTILO = "name";
	public static final String PROPRIEDADES_PARAGRAFO = "paragraph-properties";
	public static final String TAMANHO_FONTE = "font-size";
	public static final String NOME_FONTE = "font-name";
	public static final String PROPRIEDADES_TEXTO = "text-properties";
	public static final String ATRIBUTO_ITALICO_ASIAN = "font-style-asian";
	public static final String ATRIBUTO_ITALICO_COMPLEX = "font-style-complex";
	public static final String ATRIBUTO_NEGRITO_ASIAN = "font-weight-asian";
	public static final String ATRIBUTO_NEGRITO_COMPLEX = "font-weight-complex";
	public static final String ATRIBUTO_SUBLINHADO_ESTILO = "text-underline-style";
	public static final String ATRIBUTO_SUBLINHADO_LARGURA = "text-underline-width";
	public static final String ATRIBUTO_SUBLINHADO_COR = "text-underline-color";
	public static final String ATRIBUTO_SUBLINHADO_MODE = "text-underline-mode";
	public static final String ATRIBUTO_SUBLINHADO_THROUGH_MODE = "text-line-through-mode";
	public static final String ATRIBUTO_BREAK_BEFORE = "break-before";
	public static final String ATRIBUTO_FAMILY = "family";
	public static final String PARAGRAPH = "paragraph";
	public static final String ATRIBUTO_ESTILO_PAI = "parent-style-name";
	public static final String ATRIBUTO_REALCE = "background-color";
	// Atributos de parágrafo adicionados com a importação do RTF.
	public static final String ATRIBUTO_PAGE_NUMBER = "page-number";
	public static final String ATRIBUTO_WRITING_MODE = "writing-mode";
	public static final String ATRIBUTO_LINE_HEIGHT = "line-height";
	public static final String ATRIBUTO_MARGIN_LEFT = "margin-left";
	public static final String ATRIBUTO_MARGIN_RIGHT = "margin-right";
	public static final String ATRIBUTO_MARGIN_TOP = "margin-top";
	public static final String ATRIBUTO_MARGIN_BOTTOM = "margin-bottom";

	// Atributos de texto adicionados com a importação do RTF
	public static final String ATRIBUTO_FONT_VARIANT = "font-variant";
	public static final String NOME_FONTE_ASIAN = "font-name-asian";
	public static final String NOME_FONTE_COMPLEX = "font-name-complex";
	public static final String FONT_SIZE_ASIAN = "font-size-asian";
	public static final String FONT_SIZE_COMPLEX = "font-size-complex";

	public static final String USE_WINDOW_FONT_COLOR = "use-window-font-color";
	public static final String COLOR = "color";

	// Essa propriedade é colocada como default em qualquer estilo de parágrafo
	public static final String ATRIBUTO_PADRAO_PARAGRAFO = "justify-single-word";

	// Valores padrão dos atributos
	public static final String VALOR_NEGRITO = "bold";
	public static final String VALOR_ITALICO = "italic";
	public static final String VALOR_SUBLINHADO_ESTILO = "solid";
	public static final String VALOR_SUBLINHADO_COR = "font-color";
	public static final String VALOR_SUBLINHADO_LARGURA = "auto";
	public static final String VALOR_PADRAO_FONTE = "Palatino Linotype";
	
	/**
	 * Propriedades que devem sempre estar padronizadas:
	 * CharFontName, CharFontNameAsian,CharFontNameComplex - Nome da fonte
	 * CharFontStyleName - Estilo da fonte
	 * CharFontFamily, CharFontFamilyAsian, CharFontFamilyComplex - Família da fonte
	 * CharColor - Cor da fonte
	 * CharHeight, CharHeightAsian, CharHeightComplex - Tamanho da fonte 
	 */
//	public static final List<String> PROPRIEDADES_TEXTO_PARA_PADRONIZAR = Arrays.asList(CHAR_FONT_NAME,
//			"CharFontNameAsian", "CharFontNameComplex", CHAR_FONT_FAMILY, "CharFontFamilyAsian",
//			"CharFontFamilyComplex", "CharColor", CHAR_HEIGHT, "CharHeightAsian", "CharHeightComplex","CharWordMode");
	
	public static final List<String> PROPRIEDADES_TEXTO_PARA_PADRONIZAR = Arrays.asList(CHAR_FONT_NAME,
			 CHAR_FONT_FAMILY, "CharColor", "CharWordMode", CHAR_HEIGHT);
	
	public static final String PROPRIEDADE_UNO_ASIAN = "Asian";
	public static final String PROPRIEDADE_UNO_COMPLEX = "Complex";
	public static final String PROPRIEDADE_UNO_NEGRITO = "CharWeight";
	public static final String PROPRIEDADE_UNO_ITALICO = "CharPosture";
	public static final String PROPRIEDADE_AUTO_STYLE_NAME = "CharAutoStyleName";


}