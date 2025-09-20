package br.gov.stf.estf.assinatura.visao.util;

import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class CheckableDataTableRowWrapperDeslocaProcesso extends
		CheckableDataTableRowWrapper {

	private static final long serialVersionUID = -5696636656499593697L;

	private DeslocaProcessoId deslocaProcessoId;

	public CheckableDataTableRowWrapperDeslocaProcesso(Object wrappedObject) {
		super(wrappedObject);
	}

	public CheckableDataTableRowWrapperDeslocaProcesso(Object wrappedObject,
			int indice) {
		super(wrappedObject, indice);
	}

	public CheckableDataTableRowWrapperDeslocaProcesso(Object wrappedObject,
			Long id) {
		super(wrappedObject, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) {
			return false;
		}
		DeslocaProcesso deslocaProcesso = (DeslocaProcesso) this
				.getWrappedObject();

		CheckableDataTableRowWrapper other = (CheckableDataTableRowWrapper) obj;
		DeslocaProcesso outroDeslocaProcesso = (DeslocaProcesso) other
				.getWrappedObject();

		if (deslocaProcesso.getId() == null
				|| deslocaProcesso.getId().getProcesso() == null
				|| deslocaProcesso.getId().getProcesso().getId() == null) {
			if (outroDeslocaProcesso.getId() != null
					|| outroDeslocaProcesso.getId().getProcesso() != null
					|| outroDeslocaProcesso.getId().getProcesso().getId() != null)
				return false;
		} else if (!deslocaProcesso.getId().equals(outroDeslocaProcesso.getId())){
			return false;
		}
		return true;
	}
}
