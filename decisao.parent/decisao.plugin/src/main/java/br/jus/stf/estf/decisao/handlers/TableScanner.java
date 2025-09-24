/**
 * 
 */
package br.jus.stf.estf.decisao.handlers;

import org.apache.log4j.Logger;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.comp.beans.NoConnectionException;
import com.sun.star.comp.beans.OOoBean;
import com.sun.star.comp.beans.OfficeDocument;
import com.sun.star.container.XIndexAccess;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextTablesSupplier;
import com.sun.star.uno.UnoRuntime;

/**
 * @author Paulo.Estevao
 * @since 12.10.2011
 */
public class TableScanner extends Scanner {

	private static final Logger logger = Logger.getLogger(TableScanner.class);
	
	public TableScanner(OfficeDocument document) {
		super(document);
	}
	
	public boolean existeTabela(OOoBean bean) throws ValidacaoEstiloException {
		try {
			XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, bean.getDocument());
			
			// Get the TextTablesSupplier interface of the document
			XTextTablesSupplier xTableSupplier = (XTextTablesSupplier) UnoRuntime
					.queryInterface(XTextTablesSupplier.class, xTextDocument);

			// Get an XIndexAccess of TextTables
			XIndexAccess xTables = (XIndexAccess) UnoRuntime.queryInterface(
					XIndexAccess.class, xTableSupplier.getTextTables());

			int tablesCount = xTables.getCount();

			for (int i = 0; i < tablesCount; i++) {
				XTextTable xTextTable = (XTextTable) UnoRuntime.queryInterface(
						XTextTable.class, xTables.getByIndex(i));
				XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, xTextTable);
				if (!isSecaoProtegida(xServiceInfo)) {
					return true;
				}
			}
			return false;
		} catch (NoConnectionException e) {
			logger.error(e.getMessage(), e);
			throw new ValidacaoEstiloException(e);
		} catch (IndexOutOfBoundsException e) {
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

}
