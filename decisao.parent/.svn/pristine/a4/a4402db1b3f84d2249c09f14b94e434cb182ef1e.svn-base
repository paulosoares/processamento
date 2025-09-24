package br.jus.stf.estf.decisao.handlers;

import org.apache.log4j.Logger;

import br.jus.stf.stfoffice.client.uno.*;

import com.sun.star.beans.*;

public class BeanPropertyUtil {

	private static final Logger logger = Logger.getLogger(BeanPropertyUtil.class);
	
	/**
	 * Verifica se a propriedade apresentada está no seu valor default
	 * @param propriedade O nome da propriedade
	 * @param estadosDasPropriedades O objeto que contém os estados das propriedades
	 * @param pespectivaDaPropriedade indica se a propriedade é referente a um parágrafo ou a uma palavra.
	 * @param changeToDefault Indica se o método deve modificar o estado para o valor default ou apenas lançar a exceção.
	 * @throws EstiloDespadronizadoException
	 * @throws BeanUnoWrapperException
	 */
	public static void verificaPropriedadeComValorDefault(String propriedade, XPropertyState estadosDasPropriedades,
			int pespectivaDaPropriedade, boolean changeToDefault) throws EstiloDespadronizadoException, ValidacaoEstiloException {
		try {
			PropertyState propertyState = estadosDasPropriedades.getPropertyState(propriedade);
			if ( pespectivaDaPropriedade != propertyState.getValue() ) {
				if (changeToDefault) {
					modificaPropriedadeParaValorDefault(propriedade, estadosDasPropriedades);
				} else {
					throw new EstiloDespadronizadoException("A propriedade " + propriedade + " está despadronizada!");
				}
			}
		} catch (UnknownPropertyException e) {
			throw new ValidacaoEstiloException(e);
		}
	}

	private static void modificaPropriedadeParaValorDefault(String propriedade, XPropertyState estadosDasPropriedades)
			throws ValidacaoEstiloException {
		try {
			estadosDasPropriedades.setPropertyToDefault(propriedade);
		} catch (UnknownPropertyException e) {
			throw new ValidacaoEstiloException(e);
		}
	}

}
