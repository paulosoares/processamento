package br.jus.stf.estf.decisao.support.util;

public class NestedRuntimeException extends org.springframework.core.NestedRuntimeException {

	private static final long serialVersionUID = 1L;

	public NestedRuntimeException(Throwable e) {
		super(e.getMessage(), e);
	}
	
	public NestedRuntimeException(String m) {
		super(m);
	}

	@Override
	public String getMessage() {
		if (getCause() != null) {
			return getCause().getMessage();
		} else {
			return super.getMessage();
		}
	}

}
