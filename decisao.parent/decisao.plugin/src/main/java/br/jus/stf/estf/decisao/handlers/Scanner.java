/**
 * 
 */
package br.jus.stf.estf.decisao.handlers;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.beans.OfficeDocument;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.uno.UnoRuntime;

/**
 * @author Paulo.Estevao
 *
 */
public class Scanner {

	protected static final String PROPRIEDADE_IS_PROTECTED = "IsProtected";
	protected static final String PROPRIEDADE_TEXT_SECTION = "TextSection";

	protected OfficeDocument document;
	
	public Scanner(OfficeDocument document) {
		this.document = document;
	}
	
	/**
	 * Verifica se a seção de texto do elemento passado é protegida. Caso o texto não esteja sob nenhuma seção, assume que 
	 * o texto é desprotegido.
	 * @param xServiceInfo
	 * @return
	 * @throws UnknownPropertyException
	 * @throws WrappedTargetException
	 */
	protected boolean isSecaoProtegida(XServiceInfo xServiceInfo) throws UnknownPropertyException,
			WrappedTargetException {
		if (xServiceInfo != null) {
			// the PropertySet from the cursor contains the text attributes
			XPropertySet xPropertySet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(
					com.sun.star.beans.XPropertySet.class, xServiceInfo);

			XPropertySet props = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class,
					xPropertySet.getPropertyValue(PROPRIEDADE_TEXT_SECTION));
			// Se não estiver dentro de alguma seção, a propriedade será
			// nula.
			if (props != null && props.getPropertyValue(PROPRIEDADE_IS_PROTECTED) != null) {
				return props.getPropertyValue(PROPRIEDADE_IS_PROTECTED).toString().equals("true");
			}
			return false;
		}
		return false;
	}

}
