package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.transform.ResultTransformer;

public abstract class ParteProcessoResultTransformerAbstract implements ResultTransformer{

	protected Long getLong(Object data) {
		if (data != null && NumberUtils.isNumber(data.toString())) {
			return NumberUtils.createLong(data.toString());
		}
		return null;
	}

	protected Short getShort(Object data) {
		if (data != null && NumberUtils.isNumber(data.toString())) {
			return NumberUtils.toShort(data.toString());
		}
		return null;
	}

	protected Integer getInteger(Object data) {
		if (data != null && NumberUtils.isNumber(data.toString())) {
			return NumberUtils.createInteger(data.toString());
		}
		return null;
	}

	protected String getString(Object data) {
		if (data != null) {
			return data.toString();
		}
		return "";
	}

	protected Date getDate(Object data) {
		if (data != null) {
			return (Date) data;
		}
		return null;
	}
	
}
