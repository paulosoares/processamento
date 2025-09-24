package br.jus.stf.estf.decisao.handlers;

import org.apache.log4j.Logger;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.comp.beans.NoConnectionException;
import com.sun.star.comp.beans.OfficeDocument;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XText;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;

/**
 * Classe que permite que uma determinada ação seja realizada para cada parágrafo desbloqueado do texto.
 * @author Demetrius.Jube
 *
 */
abstract class ParagraphScanner extends Scanner {
	
	private static final Logger logger = Logger.getLogger(ParagraphScanner.class);

	public ParagraphScanner(OfficeDocument document) {
		super(document);
	}

	/**
	 * Recupera um objeto que implemente a interface XServiceInfo, que indica quais serviços são suportados
	 * por aquele objeto
	 * @param objeto
	 * @return
	 */
	private XServiceInfo recuperaServiceInfo(Object objeto) {
		return (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, objeto);
	}

	/**
	 * Recupera os elementos que representam os parágrafos do texto.
	 * @return
	 * @throws NoConnectionException
	 */
	private XEnumeration recuperaParagrafosDoTexto() throws NoConnectionException {
		XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, document);
		XText xText = xTextDocument.getText();

		// Recupera a enumeração de parágrafos do texto
		XEnumerationAccess xParaAccess = (XEnumerationAccess) UnoRuntime.queryInterface(XEnumerationAccess.class,
				xText);
		XEnumeration xParaEnum = xParaAccess.createEnumeration();
		return xParaEnum;
	}
	
	/**
	 * Verifica se o objeto é realmente um parágrafo. Para isso, testa se o XServiceInfo
	 * suporta o serviço com.sun.star.text.TextTable. Caso não seja, segundo a API, é seguro
	 * assumir que é um parágrafo. 
	 * @param xInfo
	 * @return
	 */
	private boolean isParagrafo(XServiceInfo xInfo) {
		return !xInfo.supportsService("com.sun.star.text.TextTable");
	}

	public void scan() throws ValidacaoEstiloException {
		try {

			XEnumeration xParaEnum = recuperaParagrafosDoTexto();
			// Varre os parágrafos
			while (xParaEnum.hasMoreElements()) {
				XServiceInfo xInfo = recuperaServiceInfo(xParaEnum.nextElement());
				if (isParagrafo(xInfo)) {
					// Só faz alguma alteração caso a seção não seja
					// protegida.
					// Isso evita que os dados do cabeçalho do template
					// sejam
					// alterados.
					if (!isSecaoProtegida(xInfo)) {
						executeAction(xInfo);
					}
				}
			}
		} catch (NoConnectionException e) {
			logger.error(e.getMessage(), e);
			throw new ValidacaoEstiloException(e);
		} catch (NoSuchElementException e) {
			logger.error(e.getMessage(), e);
			throw new ValidacaoEstiloException(e);
		} catch (WrappedTargetException e) {
			logger.error(e.getMessage(), e);
			throw new ValidacaoEstiloException(e);
		} catch (UnknownPropertyException e) {
			logger.error(e.getMessage(), e);
			throw new ValidacaoEstiloException(e);
		}
	}

	public abstract void executeAction(XServiceInfo xInfo) throws ValidacaoEstiloException;
}
