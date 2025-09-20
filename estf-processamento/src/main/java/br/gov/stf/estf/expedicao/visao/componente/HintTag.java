package br.gov.stf.estf.expedicao.visao.componente;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class HintTag extends UIComponentELTag {

	private Object value;

	@Override
	public String getComponentType() {
		return "br.gov.stf.UIHint";
	}

	@Override
	public String getRendererType() {
		return null;
	}

	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		if (value != null) {
			component.getAttributes().put("value", value);
		}
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}