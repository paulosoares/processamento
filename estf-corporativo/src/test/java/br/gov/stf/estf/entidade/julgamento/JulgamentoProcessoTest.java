package br.gov.stf.estf.entidade.julgamento;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class JulgamentoProcessoTest {
	
	private JulgamentoProcesso julgamentoProcesso;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		julgamentoProcesso = new JulgamentoProcesso();
	}
	
	@Test
	public void testGetQtdObservacoes(){
		List<VotoJulgamentoProcesso> listas = new ArrayList<VotoJulgamentoProcesso>();
		
		julgamentoProcesso = Mockito.spy(julgamentoProcesso);
		Mockito.doReturn(new HashSet<VotoJulgamentoProcesso>(listas)).when(julgamentoProcesso).getListaVotoJulgamentoProcesso();
		Mockito.doReturn(listas).when(julgamentoProcesso).getListaVotoJulgamentoDefinitivo();
		
		assertEquals(0, julgamentoProcesso.getQtdObservacoes());
		
		VotoJulgamentoProcesso voto = new VotoJulgamentoProcesso();
		VotoJulgamentoProcesso votoSemObservacao = new VotoJulgamentoProcesso();
		
		voto.setObservacao("teste");
		
		listas.add(voto);
		listas.add(voto);
		listas.add(votoSemObservacao);
		
		assertEquals(2, julgamentoProcesso.getQtdObservacoes());
	}

}
