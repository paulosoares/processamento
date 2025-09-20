package br.gov.stf.estf.assinatura.visao.util.commons;


/**
 * Classe utilitária para operações envolvendo números.
 * 
 * @author thiago.miranda
 * 
 */
public class NumberUtils {

	private static final String NUMEROS = "0123456789";

	private NumberUtils() {

	}

	/**
	 * Verifica se um determinado número é nulo ou igual a zero.<br />
	 * <br />
	 * 
	 * Exemplos de situações em que esse método pode ser chamado:
	 * 
	 * <pre>
	 * NumberUtils.isZero(null)		= true
	 * NumberUtils.isZero(0)		= true
	 * NumberUtils.isZero(15)		= false
	 * NumberUtils.isZero(new Integer(-5))	= false
	 * NumberUtils.isZero(new Long(0L))	= true
	 * </pre>
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isZero(Number number) {
		return number == null || number.longValue() == 0L;
	}

	/**
	 * Funciona de modo contrário ao método {@link #isZero(Number)}, verificando
	 * se um determinado número não é nulo nem igual a zero.<br />
	 * <br />
	 * 
	 * Exemplos de situações em que esse método pode ser chamado:
	 * 
	 * <pre>
	 * NumberUtils.isNotZero(null)		= false
	 * NumberUtils.isNotZero(0)			= false
	 * NumberUtils.isNotZero(15)			= true
	 * NumberUtils.isNotZero(new Integer(-5))	= true
	 * NumberUtils.isNotZero(new Long(0L))		= false
	 * </pre>
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNotZero(Number number) {
		return !isZero(number);
	}

	/**
	 * Verifica se um dado objeto {@link Long} é não-nulo e possui valor igual a
	 * zero e, caso afirmativo, retorna nulo.
	 * 
	 * @param l
	 * @return
	 */
	public static Long getLongNuloSeIgualZero(Long l) {
		Long retorno = l;

		if (l != null && l.intValue() == 0) {
			retorno = null;
		}

		return retorno;
	}

	/**
	 * Verifica se um determinado número é nulo ou possui valor menor ou igual a
	 * zero.<br />
	 * <br />
	 * 
	 * Exemplos de utilização:
	 * 
	 * <pre>
	 * NumberUtils.isNullOuMenorIgualZero(null)		= true
	 * NumberUtils.isNullOuMenorIgualZero(5)		= false
	 * NumberUtils.isNullOuMenorIgualZero(-8)		= true
	 * NumberUtils.isNullOuMenorIgualZero(100.50f)		= false
	 * NumberUtils.isNullOuMenorIgualZero(new Byte(10)	= false
	 * </pre>
	 * 
	 * @param numero
	 * @return
	 */
	public static boolean isNullOuMenorIgualZero(Number numero) {
		return numero == null || numero.longValue() <= 0L;
	}

	/**
	 * Verifica se uma String na sua totalidade tem somente números.<br />
	 * <br />
	 * 
	 * <pre>
	 *   NumberUtils.soNumeros("") = false
	 *   NumberUtils.soNumeros("123") = true
	 *   NumberUtils.soNumeros("1B23")= false
	 * NumberUtils.soNumeros("ABC") = false
	 * 
	 * <pre>
	 * @param String
	 * @return
	 */

	public static boolean soNumeros(String valor) {
		
		if(valor == null || valor.length() == 0 ){
			return false;
		}
		
		for(int i = 0 ; i < valor.length() ; i++){
			if(!Character.isDigit(valor.charAt(i))){
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean validacaoCPF(String cpf) {  
        int     d1, d2;  
        int     digito1, digito2, resto;  
        int     digitoCPF;  
        String  nDigResult;  

        d1 = d2 = 0;  
        digito1 = digito2 = resto = 0;  

        for (int nCount = 1; nCount < cpf.length() -1; nCount++)  
        {  
           digitoCPF = Integer.valueOf (cpf.substring(nCount -1, nCount)).intValue();  

           //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.  
           d1 = d1 + ( 11 - nCount ) * digitoCPF;  

           //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.  
           d2 = d2 + ( 12 - nCount ) * digitoCPF;  
        };  

        //Primeiro resto da divisão por 11.  
        resto = (d1 % 11);  

        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.  
        if (resto < 2)  
           digito1 = 0;  
        else  
           digito1 = 11 - resto;  

        d2 += 2 * digito1;  

        //Segundo resto da divisão por 11.  
        resto = (d2 % 11);  

        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.  
        if (resto < 2)  
           digito2 = 0;  
        else  
           digito2 = 11 - resto;  

        //Digito verificador do CPF que está sendo validado.  
        String nDigVerific = cpf.substring (cpf.length()-2, cpf.length());  

        //Concatenando o primeiro resto com o segundo.  
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);  

        //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.  
        return nDigVerific.equals(nDigResult);  
     }  
}
