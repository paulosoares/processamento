package br.gov.stf.estf.repercussaogeral.model.service.impl;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import br.gov.stf.estf.repercussaogeral.model.service.impl.RepercussaoGeralServiceImpl.Decisao.TipoDecisao;
import br.gov.stf.framework.model.service.ServiceException;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class RepercussaoGeralServiceImplTest {

	@Test
	@FileParameters("src/test/resources/tabelaCombinacoesQC.csv")
	public void placarQCTest(int caso, int ha, int naoHa, int impedidos, int ausentes, String situacao) {
		assertEquals(TipoDecisao.valueOf(situacao), RepercussaoGeralServiceImpl.placarQC(ha, naoHa, impedidos, ausentes));
	}
	
	@Test
	@FileParameters("src/test/resources/tabelaCombinacoesRG.csv")
	public void placarRGTest(int caso, int ha, int naoHa, int impedidos, int ausentes, String situacao) {
		assertEquals(TipoDecisao.valueOf(situacao), RepercussaoGeralServiceImpl.placarRG(ha, naoHa, impedidos, ausentes));
	}
	
	@Test
	@FileParameters("src/test/resources/tabelaCombinacoesRJ.csv")
	public void placarRJTest(int caso, int ha, int naoHa, int impedidos, int ausentes, String situacao) {
		assertEquals(TipoDecisao.valueOf(situacao), RepercussaoGeralServiceImpl.placarRJ(ha, naoHa, impedidos, ausentes));
	}
}
