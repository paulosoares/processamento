package br.jus.stf.estf.decisao.pesquisa.web.pfb;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.context.ApplicationContext;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;

import br.jus.stf.estf.decisao.mocker.PrincipalMocker;
import br.jus.stf.estf.decisao.pesquisa.web.PrincipalFacesBean;
import br.jus.stf.estf.decisao.support.action.support.ActionController;
import br.jus.stf.estf.decisao.support.action.support.ActionHolder;
import br.jus.stf.estf.decisao.support.controller.context.ContextConfiguration;
import br.jus.stf.estf.decisao.support.controller.context.FacesBean;
import br.jus.stf.estf.decisao.support.query.Selectable;
import br.jus.stf.estf.decisao.support.util.ApplicationContextUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SecurityContextHolder.class, ContextConfiguration.class,ApplicationContextUtils.class })
public abstract class AbstractPrincipalPesquisarTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private SecurityContext securityContext;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ApplicationContext applicationContext;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ActionHolder<Selectable> ah;

	private PrincipalMocker preparadorPrincipal = new PrincipalMocker();

	protected PrincipalFacesBean pfb;

	@Before
	public void setUp() {
		pfb = new PrincipalFacesBean();
		PowerMockito.mockStatic(SecurityContextHolder.class);
		BDDMockito.given(SecurityContextHolder.getContext()).willReturn(securityContext);

		PowerMockito.mockStatic(ContextConfiguration.class);

		PowerMockito.mockStatic(ApplicationContextUtils.class);
		BDDMockito.given(ApplicationContextUtils.getApplicationContext()).willReturn(applicationContext);

		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(preparadorPrincipal.preparar());
		Whitebox.setInternalState(pfb, "actionHolder", ah);

	}

	protected PrincipalMocker mockPrincipal() {
		return preparadorPrincipal;
	}

	protected <T> void addMockFacesBean(Class<T> clazz, FacesBean<T> fb) {
		BDDMockito.given(ContextConfiguration.getFacesBean(clazz)).willReturn(fb);
	}

	protected <T extends Selectable> void addMockActionController( Class<T> clazz, ActionController<T> ac) {
		Mockito.when(applicationContext.getBean("actionController",new Object[] { clazz })).thenReturn(ac);
	}

}
