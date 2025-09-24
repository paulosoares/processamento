package br.jus.stf.estf.decisao.handlers;

import static br.jus.stf.estf.decisao.handlers.ConstantesDocumento.ATRIBUTO_LINE_HEIGHT;
import static br.jus.stf.estf.decisao.handlers.ConstantesDocumento.ATRIBUTO_NEGRITO;
import static br.jus.stf.estf.decisao.handlers.ConstantesDocumento.PROPRIEDADES_PARAGRAFO;
import static br.jus.stf.estf.decisao.handlers.ConstantesDocumento.PROPRIEDADES_TEXTO_PARA_PADRONIZAR;
import static br.jus.stf.estf.decisao.handlers.ConstantesDocumento.VALOR_NEGRITO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;
import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.ODXMLDocument;

import br.jus.stf.stfoffice.client.uno.BeanUnoWrapperException;

import com.sun.star.beans.PropertyState;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.XPropertyState;
import com.sun.star.comp.beans.NoConnectionException;
import com.sun.star.comp.beans.OOoBean;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.uno.UnoRuntime;

public class DocumentoUtil {

	private static final String PROPRIEDADE_ALINHAMENTO_PARAGRAFO = "ParaAdjust";

	private static final Logger logger = Logger.getLogger(DocumentoUtil.class);

	private static final List<String> PROPRIEDADES_PARAGRAFO_PARA_PADRONIZAR = Arrays.asList(PROPRIEDADE_ALINHAMENTO_PARAGRAFO,
			"ParaLineSpacing", "ParaFirstLineIndent");

	private static final String PROPRIEDADE_SUBLINHADO = "CharUnderline";
	
	private static final String PROPRIEDADE_IS_PROTECTED = "IsProtected";
	private static final String PROPRIEDADE_TEXT_SECTION = "TextSection";

	/**
	 * Texto:
	CharWeight - Negrito
	CharPosture - Italico
	CharUnderline - Sublinhado
	CharStrikeout - Strike-throught
	CharWordMode - Sublinhar somente palavras
	CharCaseMap - Versalete
	CharBackColor - Backgroud (realce)
	ParaAdjust - Alinhamento 
	CharBackTransparent - Transparencia do background - Realce
	ParaBackColor - Realce do parágrafo 
	ParaBackTransparent - Transparencia do background - Realce
	 */
	private static final List<String> PROPRIEDADES_PERMITIDAS = Arrays.asList("CharWeight", "CharWeightAsian",
			"CharWeightComplex", "CharPosture", "CharPostureAsian", "CharPostureComplex", PROPRIEDADE_SUBLINHADO,
			"CharStrikeout", "CharWordMode", "CharCaseMap", "CharBackColor", PROPRIEDADE_ALINHAMENTO_PARAGRAFO, "CharBackTransparent",
			"ParaBackColor", "ParaBackTransparent", "CharUnderlineHasColor", "CharUnderlineColor");
	private static final String PROPRIEDADE_ESTILO_PARAGRAFO = "ParaStyleName";
	private static final String PREFIXO_ESTILO_STF = "STF-";
	private static final String STF_PADRAO = PREFIXO_ESTILO_STF + "Padrão";
	private static final String STF_PADRAO_CENTRALIZADO = PREFIXO_ESTILO_STF + "PadrãoCentralizado";

	public static final Namespace NAMESPACE_STYLE = Namespace.getNamespace("style",
			"urn:oasis:names:tc:opendocument:xmlns:style:1.0");
	private static final Namespace NAMESPACE_FO = Namespace.getNamespace("fo",
			"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0");

	private static Element getElementoDoParagrafo(Element e) {
		return getElementoAtributo(PROPRIEDADES_PARAGRAFO, e);
	}

	public static String getAttributeValue(String nomeAtributo, Element element) {
		List<Attribute> atributos = element.getAttributes();
		String valor = null;
		for (Attribute att : atributos) {
			if (att.getName().equals(nomeAtributo)) {
				valor = att.getValue();
				break;
			}
		}
		return valor;
	}

	private static Element getElementoAtributo(String nome, Element e) {
		List<Element> filhos = e.getChildren();
		Element texto = null;
		for (Element f : filhos) {
			if (f.getName().equals(nome)) {
				texto = f;
				break;
			}
		}
		return texto;
	}


	public static void padronizaElementosDoStandard(Element node) {
		Element paragrafo = getElementoDoParagrafo(node);
		if (paragrafo == null) {
			paragrafo = new Element(PROPRIEDADES_PARAGRAFO, NAMESPACE_STYLE);
			node.addContent(paragrafo);
		}
		limpaAtributosDoElemento(paragrafo);
		insereAtributo(paragrafo, "100%", ATRIBUTO_LINE_HEIGHT, NAMESPACE_FO);

	}


	private static void limpaAtributosDoElemento(Element elemento) {
		elemento.getAttributes().clear();
		// List<Attribute> atributos = ListUtils.union(new
		// ArrayList<Attribute>(), elemento.getAttributes());
		// for (Attribute attribute : atributos) {
		// elemento.removeAttribute(attribute);
		// }
	}


	public static void insereAtributoNegrito(Element texto) {
		insereAtributo(texto, VALOR_NEGRITO, ATRIBUTO_NEGRITO, NAMESPACE_FO);
	}

	public static void insereAtributo(Element elemento, Object atributo, String nomeAtributo, Namespace namespace) {
		if (atributo != null) {
			Attribute attribute = new Attribute(nomeAtributo, atributo.toString(), namespace);
			elemento.setAttribute(attribute);
		}

	}

	public static File removeBookMarksDoDocumento(File arquivo) throws IOException, JDOMException {
		ODPackage odt = new ODPackage(arquivo);
		ODXMLDocument content = odt.getContent();
		Element root = content.getDocument().getRootElement();
		XPath path = content.getXPath("//office:text//text:bookmark-start | //office:text//text:bookmark-end");
		List<Element> nos = path.selectNodes(root);
		for (Element e : nos) {
			e.detach();
		}
		return odt.save();
	}
	
	public static boolean existeTextoDespadronizado(OOoBean bean) throws ValidacaoEstiloException  {
		try {
			ParagraphScanner scanner = new ParagraphScanner(bean.getDocument()) {
				@Override
				public void executeAction(XServiceInfo xInfo) throws ValidacaoEstiloException {
					verificaPadronizacaoDoParagrafo(xInfo, false);
				}

			};
			scanner.scan();
			return false;
		} catch (EstiloDespadronizadoException e) {
			logger.warn(e.getMessage());
			return true;
		} catch (NoConnectionException e) {
			logger.error(e.getMessage(), e);
			throw new ValidacaoEstiloException(e);
		}
	}
	
	public static boolean existeTabela(OOoBean bean) throws ValidacaoEstiloException {
		try {
			TableScanner scanner = new TableScanner(bean.getDocument());
			return scanner.existeTabela(bean);
		} catch (NoConnectionException e) {
			logger.error(e.getMessage(), e);
			throw new ValidacaoEstiloException(e);
		}
		
	}
	
	public static void padronizarFormatacao(OOoBean bean) throws ValidacaoEstiloException {
		try {
			ParagraphScanner scanner = new ParagraphScanner(bean.getDocument()) {
				@Override
				public void executeAction(XServiceInfo xInfo) throws ValidacaoEstiloException {
					try {
						padronizaFormatacaoDoParagrafo(xInfo);
					} catch (UnknownPropertyException e) {
						throw new ValidacaoEstiloException(e);
					} catch (WrappedTargetException e) {
						throw new ValidacaoEstiloException(e);
					} catch (PropertyVetoException e) {
						throw new ValidacaoEstiloException(e);
					} catch (IllegalArgumentException e) {
						throw new ValidacaoEstiloException(e);
					}
				}
			};
			scanner.scan();
		} catch (NoConnectionException e) {
			throw new ValidacaoEstiloException(e);
		}
	}

	/**
	 * Padroniza a formatação do parágrafo, retirando elementos que sejam despadronizados.
	 * @param xInfo
	 * @param estiloPadrao 
	 * @throws BeanUnoWrapperException 
	 * @throws WrappedTargetException 
	 * @throws UnknownPropertyException 
	 * @throws IllegalArgumentException 
	 * @throws PropertyVetoException 
	 */
	private static void padronizaFormatacaoDoParagrafo(XServiceInfo xInfo) throws ValidacaoEstiloException,
			UnknownPropertyException, WrappedTargetException, PropertyVetoException, IllegalArgumentException {
		aplicaEstiloPadronizado(xInfo);
		verificaPadronizacaoDoParagrafo(xInfo, true);
	}

	/**
	 * 
	 * @param xInfo
	 * @throws UnknownPropertyException
	 * @throws WrappedTargetException
	 * @throws PropertyVetoException
	 * @throws IllegalArgumentException
	 */
	private static void aplicaEstiloPadronizado(XServiceInfo xInfo) throws UnknownPropertyException, WrappedTargetException,
			PropertyVetoException, IllegalArgumentException {
		XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xInfo);
		verificaEstiloPadraoDoParagrafo(xCursorProps);
	}

	private static void verificaEstiloPadraoDoParagrafo(XPropertySet xCursorProps) throws UnknownPropertyException,
			WrappedTargetException, PropertyVetoException, IllegalArgumentException {
		String nomeDoEstilo = getNomeDoEstilo(xCursorProps);
		// Recupera o alinhamento do parágrafo
		String alinhamentoDoParagrafo = getValorDaPropriedade(xCursorProps, PROPRIEDADE_ALINHAMENTO_PARAGRAFO);
		if (isNomeDoEstiloDoParagrafoDespadronizado(nomeDoEstilo)) {
			if (isParagrafoCentralizado(alinhamentoDoParagrafo)) {
				nomeDoEstilo = STF_PADRAO_CENTRALIZADO;
			} else {
				nomeDoEstilo = STF_PADRAO;
			}
			aplicaEstiloDoParagrafo(xCursorProps, nomeDoEstilo);
			// Verifica se o estilo centralizado está com o ajuste de parágrafo
			// diferente de centralizado. Caso esteja, indica que houve a
			// colagem de um texto que pegou, por algum motivo ilógico, o padrão
			// centralizado.
		} else if (nomeDoEstilo.equals(STF_PADRAO_CENTRALIZADO) && !isParagrafoCentralizado(alinhamentoDoParagrafo)) {
			nomeDoEstilo = STF_PADRAO;
			aplicaEstiloDoParagrafo(xCursorProps, nomeDoEstilo);
		}
	}

	private static void aplicaEstiloDoParagrafo(XPropertySet xCursorProps, String nomeDoEstilo)
			throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		Map<String, Object> mapaDeFormatacoesMantidas = montaMapaDeFormatacoesDoParagrafo(xCursorProps);
		xCursorProps.setPropertyValue(PROPRIEDADE_ESTILO_PARAGRAFO, nomeDoEstilo);
		aplicaFormatacoesDoParagrafo(xCursorProps, mapaDeFormatacoesMantidas);

	}

	private static Map<String, Object> montaMapaDeFormatacoesDoParagrafo(XPropertySet xCursorProps)
			throws UnknownPropertyException, WrappedTargetException {
		Map<String, Object> mapaDePropriedadesMantidas = new HashMap<String, Object>();
		XPropertyState xCursorPropsState = (XPropertyState) UnoRuntime.queryInterface(XPropertyState.class,
				xCursorProps);
		for (String propriedade : PROPRIEDADES_PERMITIDAS) {
			PropertyState state = xCursorPropsState.getPropertyState(propriedade);
			if (state != PropertyState.DEFAULT_VALUE) {
				mapaDePropriedadesMantidas.put(propriedade, xCursorProps.getPropertyValue(propriedade));
			}
		}
		return mapaDePropriedadesMantidas;
	}

	private static void aplicaFormatacoesDoParagrafo(XPropertySet xCursorProps, Map<String, Object> mapaDePropriedadesMantidas)
			throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		for (String chave : mapaDePropriedadesMantidas.keySet()) {
			xCursorProps.setPropertyValue(chave, mapaDePropriedadesMantidas.get(chave));
		}
	}

	private static boolean isNomeDoEstiloDoParagrafoDespadronizado(String nomeDoEstiloDoParagrafo) {
		return nomeDoEstiloDoParagrafo == null || nomeDoEstiloDoParagrafo.trim().equals("")
				|| !nomeDoEstiloDoParagrafo.startsWith(PREFIXO_ESTILO_STF);
	}

	private static boolean isParagrafoCentralizado(String alinhamentoDoParagrafo) {
		return String.valueOf(ParagraphAdjust.CENTER_value).equals(alinhamentoDoParagrafo);
	}

	private static String getNomeDoEstilo(XPropertySet xCursorProps) throws UnknownPropertyException, WrappedTargetException {
		return getValorDaPropriedade(xCursorProps, PROPRIEDADE_ESTILO_PARAGRAFO);
	}

	private static String getValorDaPropriedade(XPropertySet xCursorProps, String propriedade)
			throws UnknownPropertyException, WrappedTargetException {
		return xCursorProps.getPropertyValue(propriedade).toString();
	}

	/**
	 * Verifica se o parágrafo está padronizado.
	 * @param xInfo Objeto que contém os dados do parágrafo
	 * @param changeToDefault Indica se o valor deve ser modificado para o valor default.
	 * @throws EstiloDespadronizadoException 
	 */
	private static  void verificaPadronizacaoDoParagrafo(XServiceInfo xInfo, boolean changeToDefault)
			throws ValidacaoEstiloException, EstiloDespadronizadoException {
		try {
			XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xInfo);
			String nomeDoEstilo = getNomeDoEstilo(xCursorProps);
			// Recupera o alinhamento do parágrafo
			if (isNomeDoEstiloDoParagrafoDespadronizado(nomeDoEstilo)) {
				throw new EstiloDespadronizadoException("Existe um parágrafo despadronizado com o estilo ["
						+ nomeDoEstilo + "]!");
			}
			verificaValoresDefaultDoParagrafo(xCursorProps, changeToDefault);
			verificaPadronizacaoDasPalavras(xInfo, changeToDefault);
		} catch (UnknownPropertyException e) {
			throw new ValidacaoEstiloException(e);
		} catch (WrappedTargetException e) {
			throw new ValidacaoEstiloException(e);
		}

	}

	/**
	 * Varre as propriedades do parágrafo que devem ser padronizadas
	 * @param xCursorProps Objeto com as propriedades do parágrafo.
	 * @param changeToDefault Indica se o valor deve ser modificado para seu valor default.
	 * @throws BeanUnoWrapperException
	 */
	private static void verificaValoresDefaultDoParagrafo(XPropertySet xCursorProps, boolean changeToDefault)
			throws ValidacaoEstiloException {
		XPropertyState xCursorPropsState = (XPropertyState) UnoRuntime.queryInterface(XPropertyState.class,
				xCursorProps);
		List<String> propriedadeDeParagrafo = new ArrayList<String>();
		propriedadeDeParagrafo.addAll(PROPRIEDADES_PARAGRAFO_PARA_PADRONIZAR);
		// Adiciona as propriedades de texto, já que os estilos do parágrafo
		// podem ter elementos que modificam os textos também.
		propriedadeDeParagrafo.addAll(PROPRIEDADES_TEXTO_PARA_PADRONIZAR);
		for (String propriedade : propriedadeDeParagrafo) {
			if ( changeToDefault ){
				BeanPropertyUtil.verificaPropriedadeComValorDefault(propriedade, xCursorPropsState, PropertyState.AMBIGUOUS_VALUE_value, changeToDefault);
			} else {
				BeanPropertyUtil.verificaPropriedadeComValorDefault(propriedade, xCursorPropsState, PropertyState.DEFAULT_VALUE_value, changeToDefault);
			}
		}
	}
	

	private static void verificaPadronizacaoDasPalavras(XServiceInfo xInfo, boolean changeToDefault)
			throws ValidacaoEstiloException {
		WordPropertyChecker scanner = new WordPropertyChecker(xInfo, changeToDefault);
		scanner.check();
	}


}