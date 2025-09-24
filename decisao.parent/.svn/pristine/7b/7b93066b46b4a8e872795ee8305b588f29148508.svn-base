package br.jus.stf.estf.decisao.handlers;

import static br.jus.stf.estf.decisao.handlers.ConstantesDocumento.*;

import java.text.MessageFormat;
import org.apache.log4j.Logger;

import com.sun.star.beans.*;
import com.sun.star.container.*;
import com.sun.star.lang.*;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.text.*;
import com.sun.star.uno.*;

class WordPropertyChecker {

	private static final char CHAR_UNBREAKABLE_SPACE = (char)0xA0;

	private static final Logger logger = Logger.getLogger(WordPropertyChecker.class);

	private XServiceInfo xInfo;
	private boolean changeToDefault;

	public WordPropertyChecker(XServiceInfo xInfo, boolean changeToDefault) {
		this.xInfo = xInfo;
		this.changeToDefault = changeToDefault;
	}

	public void check() throws ValidacaoEstiloException, EstiloDespadronizadoException {

		try {
			XEnumerationAccess xWord = (XEnumerationAccess) UnoRuntime.queryInterface(XEnumerationAccess.class, xInfo);
			XEnumeration xParaEnum = xWord.createEnumeration();
			while (xParaEnum.hasMoreElements()) {
				XTextRange range;
				range = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xParaEnum.nextElement());
				logger.debug(MessageFormat.format("Validando a formatação da palavra: [{0}]", range.getString()));
				XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, range);
				if (isTextoComEstiloAutomatico(xCursorProps)) {
					// Caso tenha algum estilo automático, garante que as
					// propriedades
					// abaixo serão padronizadas.
					verificaValoresDefaultDoTexto(xCursorProps, changeToDefault);
					if (changeToDefault) {
						validaPropriedadesAsianComplex(xCursorProps);
					}
				}
				//verificaUnbreakableSpace(range, changeToDefault);
			}
		} catch (NoSuchElementException e) {
			throw new ValidacaoEstiloException(e);
		} catch (WrappedTargetException e) {
			throw new ValidacaoEstiloException(e);
		} catch (UnknownPropertyException e) {
			throw new ValidacaoEstiloException(e);
		} catch (PropertyVetoException e) {
			throw new ValidacaoEstiloException(e);
		} catch (IllegalArgumentException e) {
			throw new ValidacaoEstiloException(e);
		}

	}

	/**
	 * Verifica se o caracter unbreakable space está sendo utilizado. Caso esteja, e o sistema estiver apenas fazendo uma verificação (changeToDefaul = false),
	 *  alerta o usuário. Caso contrário, substitui o caracter por um espaço normal.
	 * @param palavra
	 * @param changeToDefault
	 * @throws EstiloDespadronizadoException 
	 */
	private void verificaUnbreakableSpace(XTextRange range, boolean changeToDefault)
			throws EstiloDespadronizadoException {
		if (containsUnbreakableSpace(range)) {
			if (changeToDefault) {
				logger.debug(MessageFormat.format("Substituindo o valor unbreakable space por espaço normal na palavra {0}...",
						range.getString()));
				String texto = range.getString();
				range.setString(texto.replace(CHAR_UNBREAKABLE_SPACE, ' '));
			} else {
				throw new EstiloDespadronizadoException("Caracter unbreakable space localizado!");
			}
		}

	}

	/**
	 * Verifica se a palavra selecionada é um unbreakable space (Código 160 da tabela ASCII)
	 * @param range
	 * @return
	 */
	private boolean containsUnbreakableSpace(XTextRange range) {
		String palavra = range.getString();
		return palavra.contains(String.valueOf(CHAR_UNBREAKABLE_SPACE));
	}

	private void validaPropriedadesAsianComplex(XPropertySet xCursorProps) throws UnknownPropertyException,
			WrappedTargetException, PropertyVetoException, IllegalArgumentException {
		logger.debug("Igualando as propriedades Asian e Complex...");
		String[] propriedadesAsianComplex = { PROPRIEDADE_UNO_ITALICO, PROPRIEDADE_UNO_ITALICO, CHAR_FONT_FAMILY,
				CHAR_FONT_NAME, CHAR_HEIGHT };
		for (String propriedade : propriedadesAsianComplex) {
			igualaPropriedadesAsianComplex(xCursorProps, propriedade);
		}
	}

	private void igualaPropriedadesAsianComplex(XPropertySet xCursorProps, String propriedade)
			throws UnknownPropertyException, WrappedTargetException, PropertyVetoException, IllegalArgumentException {
		igualaValorDaPropriedade(xCursorProps, propriedade, PROPRIEDADE_UNO_COMPLEX);
		igualaValorDaPropriedade(xCursorProps, propriedade, PROPRIEDADE_UNO_ASIAN);
	}

	/**
	 * Iguala o valor das propriedades complex e asian para o valor padrão. Esse método é necessário para evitar os problemas que ocorrem com a conversão de RTFs antigos para ODT. 
	 * @param xCursorProps As propriedades do texto
	 * @param propriedade O nome da propriedade padrão
	 * @param complemento Qual o tipo (complex e asian) que deve ser igualado
	 * @throws UnknownPropertyException
	 * @throws WrappedTargetException
	 * @throws PropertyVetoException
	 * @throws IllegalArgumentException
	 */
	private void igualaValorDaPropriedade(XPropertySet xCursorProps, String propriedade, String complemento)
			throws UnknownPropertyException, WrappedTargetException, PropertyVetoException, IllegalArgumentException {
		String propriedadeComplex = propriedade + complemento;
		Object valorPadrao = getValorDaPropriedade(xCursorProps, propriedade);
		Object valorComplex = getValorDaPropriedade(xCursorProps, propriedadeComplex);
		if (valorComplex != null && !valorComplex.equals(valorPadrao)) {
			xCursorProps.setPropertyValue(propriedadeComplex, valorPadrao);
		}
	}

	private void verificaValoresDefaultDoTexto(XPropertySet xCursorProps, boolean changeToDefault)
			throws ValidacaoEstiloException, EstiloDespadronizadoException {
		XPropertyState xCursorPropsState = (XPropertyState) UnoRuntime.queryInterface(XPropertyState.class,
				xCursorProps);
		for (String propriedade : PROPRIEDADES_TEXTO_PARA_PADRONIZAR) {
			/* No caso das palavras, deve ser utilizado PropertyState.DEFAULT_VALUE_value */
			BeanPropertyUtil.verificaPropriedadeComValorDefault(propriedade, xCursorPropsState, PropertyState.DEFAULT_VALUE_value, changeToDefault);
		}
	}

	/**
	 * Verifica se o texto possui a propriedade "CharAutoStyleName". Caso tenha, indica que existe algum tipo de formatação
	 * específica, que sobrepõe a formatação padrão.
	 * @param propriedades
	 * @return
	 * @throws UnknownPropertyException
	 * @throws WrappedTargetException
	 */
	private boolean isTextoComEstiloAutomatico(XPropertySet propriedades) throws UnknownPropertyException,
			WrappedTargetException {
		Object estiloAutomatico = getValorDaPropriedade(propriedades, PROPRIEDADE_AUTO_STYLE_NAME);
		return estiloAutomatico != null && !estiloAutomatico.toString().trim().equals("");
	}

	private Object getValorDaPropriedade(XPropertySet xCursorProps, String propriedade)
			throws UnknownPropertyException, WrappedTargetException {
		return xCursorProps.getPropertyValue(propriedade);
	}

}
