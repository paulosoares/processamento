package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Roberio.Fernandes
 */
public class UtilTest {
    
    public UtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of primeiroDiaAno method, of class Util.
     */
    @Test
    public void testPrimeiroDiaAno() {
        System.out.println("primeiroDiaAno");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        calendar.set(ano, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date resultadoEsperado = calendar.getTime();

        Date resultadoEncontrado = Util.primeiroDiaAno(ano);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of ultimoDiaAno method, of class Util.
     */
    @Test
    public void testUltimoDiaAno() {
        System.out.println("ultimoDiaAno");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        calendar.set(ano + 1, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MILLISECOND, -1);
        Date resultadoEsperado = calendar.getTime();

        Date resultadoEncontrado = Util.ultimoDiaAno(ano);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of inicioDia method, of class Util.
     */
    @Test
    public void testInicioDia() {
        System.out.println("inicioDia");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        calendar.set(ano, mes, dia, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date resultadoEsperado = calendar.getTime();

        Date resultadoEncontrado = Util.inicioDia(data);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of fimDia method, of class Util.
     */
    @Test
    public void testFimDia() {
        System.out.println("fimDia");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        calendar.set(ano, mes, dia + 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MILLISECOND, -1);
        Date resultadoEsperado = calendar.getTime();

        Date resultadoEncontrado = Util.fimDia(data);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of getDiaMes method, of class Util.
     */
    @Test
    public void testGetDiaMes() {
        System.out.println("getDiaMes");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        int resultado = Util.getDiaMes(data);
        Assert.assertEquals(dia, resultado);
    }

    /**
     * Test of getMes method, of class Util.
     */
    @Test
    public void testGetMes() {
        System.out.println("getMes");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        int resultadoEsperado = mes + 1;
        int resultadoEncontrado = Util.getMes(data);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of getAno method, of class Util.
     */
    @Test
    public void testGetAno() {
        System.out.println("getAno");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        int resultado = Util.getAno(data);
        Assert.assertEquals(ano, resultado);
    }

    /**
     * Test of getHora method, of class Util.
     */
    @Test
    public void testGetHora() {
        System.out.println("getHora");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        int resultado = Util.getHora(data);
        Assert.assertEquals(hora, resultado);
    }

    /**
     * Test of getMinuto method, of class Util.
     */
    @Test
    public void testGetMinuto() {
        System.out.println("getMinuto");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        int resultado = Util.getMinuto(data);
        Assert.assertEquals(minuto, resultado);
    }

    /**
     * Test of getSegundo method, of class Util.
     */
    @Test
    public void testGetSegundo() {
        System.out.println("getSegundo");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        int resultado = Util.getSegundo(data);
        Assert.assertEquals(segundo, resultado);
    }

    /**
     * Test of getDiaAno method, of class Util.
     */
    @Test
    public void testGetDiaAno() {
        System.out.println("getDiaAno");
        Calendar calendar = Calendar.getInstance();
        int ano = 2015;
        int mes = Calendar.JUNE;
        int dia = 10;
        int hora = 11;
        int minuto = 45;
        int segundo = 20;
        calendar.set(ano, mes, dia, hora, minuto, segundo);
        Date data = calendar.getTime();

        int resultadoEsperado = 161;
        int resultadoEncontrado = Util.getDiaAno(data);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isStringNulaOuVazia method, of class Util.
     */
    @Test
    public void testIsStringNulaOuVaziaVazia() {
        System.out.println("testIsStringNulaOuVaziaVazia");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.isStringNulaOuVazia("");
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isStringNulaOuVazia method, of class Util.
     */
    @Test
    public void testIsStringNulaOuVaziaNull() {
        System.out.println("testIsStringNulaOuVaziaNull");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.isStringNulaOuVazia(null);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isStringNulaOuVazia method, of class Util.
     */
    @Test
    public void testIsStringNulaOuVazia() {
        System.out.println("testIsStringNulaOuVazia");
        boolean resultadoEsperado = false;
        boolean resultadoEncontrado = Util.isStringNulaOuVazia("123");
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isPossuiStringNulaOuVazia method, of class Util.
     */
    @Test
    public void testIsPossuiStringNulaOuVaziaPossui() {
        System.out.println("testIsPossuiStringNulaOuVaziaPossui");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.isPossuiStringNulaOuVazia("123", "");
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isPossuiStringNulaOuVazia method, of class Util.
     */
    @Test
    public void testIsPossuiStringNulaOuVaziaNaoPossui() {
        System.out.println("testIsPossuiStringNulaOuVaziaNaoPossui");
        boolean resultadoEsperado = false;
        boolean resultadoEncontrado = Util.isPossuiStringNulaOuVazia("123", "321");
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testIsObjetoNulloOuTextoVazioNull() {
        System.out.println("testIsObjetoNulloOuTextoVazioNull");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.isObjetoNulloOuTextoVazio(null);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testIsObjetoNulloOuTextoVazioVazio() {
        System.out.println("testIsObjetoNulloOuTextoVazioVazio");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.isObjetoNulloOuTextoVazio("");
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testIsObjetoNulloOuTextoVazio() {
        System.out.println("testIsObjetoNulloOuTextoVazio");
        boolean resultadoEsperado = false;
        boolean resultadoEncontrado = Util.isObjetoNulloOuTextoVazio("123");
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isPossuiObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testIsPossuiObjetoNulloOuTextoVazioPossui() {
        System.out.println("testIsPossuiObjetoNulloOuTextoVazioPossui");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.isPossuiObjetoNulloOuTextoVazio("123",
				null);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of isPossuiObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testIsPossuiObjetoNulloOuTextoVazioNaoPossui() {
        System.out.println("testIsPossuiObjetoNulloOuTextoVazioNaoPossui");
        boolean resultadoEsperado = false;
        boolean resultadoEncontrado = Util.isPossuiObjetoNulloOuTextoVazio("123",
				5);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of verificarObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testVerificarObjetoNulloOuTextoVazioEVazio() {
        System.out.println("testVerificarObjetoNulloOuTextoVazioEVazio");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.verificarObjetoNulloOuTextoVazio(true,
				"",
				"");
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of verificarObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testVerificarObjetoNulloOuTextoVazioENulo() {
        System.out.println("testVerificarObjetoNulloOuTextoVazioENulo");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.verificarObjetoNulloOuTextoVazio(true,
				null,
				null);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of verificarObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testVerificarObjetoNulloOuTextoVazioEVazioNulo() {
        System.out.println("testVerificarObjetoNulloOuTextoVazioEVazioNulo");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.verificarObjetoNulloOuTextoVazio(true,
				"",
				null);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of verificarObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testVerificarObjetoNulloOuTextoVazioOuVazioNulo() {
        System.out.println("testVerificarObjetoNulloOuTextoVazioOuVazioNulo");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.verificarObjetoNulloOuTextoVazio(false,
				"",
				null);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }

    /**
     * Test of verificarObjetoNulloOuTextoVazio method, of class Util.
     */
    @Test
    public void testVerificarObjetoNulloOuTextoVazioOuNaoVazioNulo() {
        System.out.println("testVerificarObjetoNulloOuTextoVazioOuNaoVazioNulo");
        boolean resultadoEsperado = true;
        boolean resultadoEncontrado = Util.verificarObjetoNulloOuTextoVazio(false,
				"123",
				null);
        Assert.assertEquals(resultadoEsperado, resultadoEncontrado);
    }
}