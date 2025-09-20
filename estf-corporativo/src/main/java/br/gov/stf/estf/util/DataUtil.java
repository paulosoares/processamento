package br.gov.stf.estf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.publicacao.Feriado;

public class DataUtil {
	private DataUtil() {
		// Classe utilitária não necessita ser instanciada.
	}
	
	public static SimpleDateFormat getFormatterDatePadrao(boolean mostrarHora) {
		SimpleDateFormat formatter = null;
		if(mostrarHora){
			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		}else{
			formatter = new SimpleDateFormat("dd/MM/yyyy");
		}
		return formatter;
	}
	
	/**
	 * Transforma Date em string com formato yyyy/MM/dd ou yyyy/MM/dd HH:mm:ss
	 * @param dataDate
	 * @param mostrarHora
	 * @return
	 */
	public static String date2StringInvertido(Date dataDate,boolean mostrarHora) {
		SimpleDateFormat formatter = null;
		if(mostrarHora){
			formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}else{
			formatter = new SimpleDateFormat("yyyy/MM/dd");
		}		
		String dataString = formatter.format(dataDate);
		return dataString;
	}
	
	public String date2StringPonto(Date dataDate,boolean mostrarHora) {
		SimpleDateFormat formatter = null;
		if(mostrarHora){
			formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		}else{
			formatter = new SimpleDateFormat("dd.MM.yyyy");
		}	
		String dataString = formatter.format(dataDate);
		return dataString;
	}
	
	/**
	 * Transforma Date em string com formato dd/MM/yyyy ou dd/MM/yyyy HH:mm:ss
	 * @param dataDate
	 * @param mostrarHora
	 * @return
	 */
	public static String date2String(Date dataDate,boolean mostrarHora) {
		String dataString = getFormatterDatePadrao(mostrarHora).format(dataDate);
		return dataString;
	}

	/**
	 * Transforma String no tipo Date com formato dd/MM/yyyy ou dd/MM/yyyy HH:mm:ss 
	 * @param dataString
	 * @param mostrarHora se deve ter hora ou não
	 * @return
	 */	
	public static Date string2Date(String dataString,boolean mostrarHora) {
		SimpleDateFormat formatter = getFormatterDatePadrao(mostrarHora);
		Date date = null;
		try {
			date = formatter.parse(dataString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * Recebe um Date e retira a hora deixando apenas dd/MM/yyyy
	 * @param dataDate
	 * @return
	 */
	public static Date dateSemHora(Date dataDate) {
		String dataString = date2String(dataDate, false);
		Date dateRetorno = string2Date(dataString, false);
		return dateRetorno;
	}	
	
	public static Date string2Date(String dataString) {
		return string2Date(dataString,true);
	}
	
	/**
	 * Transforma String no formato dd/MM/yyyy HH:mm:ss em GregorianCalendar
	 * @param dataString
	 * @return
	 */
	public static GregorianCalendar string2GregorianCalendar(String dataString) {
		Date date = string2Date(dataString,true);
		GregorianCalendar dataRetorno = date2GregorianCalendar(date);
		return dataRetorno;
	}	
	
	public static GregorianCalendar date2GregorianCalendar(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal;
	}
	
	public static GregorianCalendar getNow() {
		GregorianCalendar hoje = new GregorianCalendar();
		hoje = (GregorianCalendar) Calendar.getInstance().clone();
		return hoje;
	}
	
	public static Date getNowDate() {
		GregorianCalendar now = getNow();
		Date nowDate = now.getTime();
		return nowDate;
	}	
	
	/**
	 * Retorna o primeiro momento do dia informado, hora 0:0:0.
	 *
	 * @param data
	 * @return
	 */
	public static Date inicioDia(Date data) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(data);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}

	/**
	 * Retorna o ultimo momento do dia informado, hora 23:59:59.
	 *
	 * @param data
	 * @return
	 */
	public static Date fimDia(Date data) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(inicioDia(data));
	    calendar.add(Calendar.DAY_OF_YEAR, 1);
	    calendar.add(Calendar.MILLISECOND, -1);
	    return calendar.getTime();
	}	
	
	/**
	 * Informa se é um dia util ou não. Considerando: finais de semana; feriados informados; férias ministros STF;
	 * @param calendarHoje
	 * @param feriados
	 * @return
	 */
	public static boolean isDiaUtil(Calendar hoje, List<Calendar> feriados) {
		Calendar calendarHoje = (Calendar) hoje.clone();
		calendarHoje.set(Calendar.HOUR_OF_DAY, 0);
		calendarHoje.set(Calendar.MINUTE, 0);
		calendarHoje.set(Calendar.SECOND, 0);
		calendarHoje.set(Calendar.MILLISECOND, 0);
		
		int diaDoMes = calendarHoje.get(Calendar.DAY_OF_MONTH);
		int diaSemana = calendarHoje.get(Calendar.DAY_OF_WEEK);
		int mes       = calendarHoje.get(Calendar.MONTH);
		
		// Sábado e Domingo não é considerado dia útil
		if ((diaSemana == Calendar.SUNDAY) || (diaSemana == Calendar.SATURDAY))
			return false;
		
		if (feriados != null && feriados.contains(calendarHoje))
			return false;
		
		// Ferias Ministros
		if(mes == Calendar.JANUARY)
			return false;
		
		// O recesso de julho vai de 2 a 31
		if (mes == Calendar.JULY && diaDoMes != 1)
			return false;
		
		// Recesso de 20/12 até o final do ano
		if (mes == Calendar.DECEMBER && diaDoMes >= 20)
			return false;
		
		return true;
	}
	
	public static List<Calendar> tipoFeriado2Calendar(List<Feriado> feriados) {
		List<Calendar> feriadosCalendar = new ArrayList<Calendar>();
		for (Feriado feriado : feriados) {
			int dia = Integer.valueOf(feriado.getDia());
			int mes =  Integer.valueOf(feriado.getId().substring(0, 2));
			int ano =  Integer.valueOf(feriado.getId().substring(2, feriado.getId().length()));
			
			Calendar feriadoCalendar = Calendar.getInstance();
			feriadoCalendar.set(Calendar.DAY_OF_MONTH, dia);
			feriadoCalendar.set(Calendar.MONTH, mes-1);	
			feriadoCalendar.set(Calendar.YEAR, ano);
			feriadoCalendar.set(Calendar.HOUR_OF_DAY, 0);
			feriadoCalendar.set(Calendar.MINUTE, 0);
			feriadoCalendar.set(Calendar.SECOND, 0);
			feriadoCalendar.set(Calendar.MILLISECOND, 0);
			
			feriadosCalendar.add(feriadoCalendar);
		}		
		return feriadosCalendar;
	}
	
	/**
	 * ATENCAO: MES recebe valores de 1 a 12, porém para o Calendar Janeiro é 0 e Dezembro é 11. 
	 * @param ano
	 * @param mes valor de 1 a 12. 
	 * @return
	 */
	public static String recuperaMesAno(int ano, int mes){
		String stringMes = null;
		if((mes > 0) && (mes < 10) ){
			stringMes = "0"+String.valueOf(mes);
		}else{
			stringMes = String.valueOf(mes);
		}
		String mesAno = stringMes + String.valueOf(ano);
		
		return mesAno;
	}
	
	/**
	 * Retorna a Data inicial da Sessao, pois na sessão VIRTUAL pode ter apena o inicio
	 * previsto pois a sessao ainda não ocorreu.
	 * @param sessao
	 * @return
	 */
	public static Date getDataInicioSessao(Sessao sessao) {
		Date dataInicio = null;
		String tipoAmbiente = sessao.getTipoAmbiente();
		if(TipoAmbienteConstante.PRESENCIAL.getSigla().equals(tipoAmbiente)){
			dataInicio = sessao.getDataInicio();
			if(dataInicio == null){
				dataInicio = sessao.getDataPrevistaInicio();
			}
		}else{
			dataInicio = sessao.getDataInicio();
			if(dataInicio == null){
				dataInicio = sessao.getDataPrevistaInicio();
			}
		}		
		return dataInicio;
	}	
	
	/**
	 * Retorna a Data final da Sessao, pois na sessão VIRTUAL pode ter apena o inicio
	 * previsto pois a sessao ainda não ocorreu.
	 * @param sessao
	 * @return
	 */
	public static Date getDataFimSessao(Sessao sessao) {
		Date dataInicio = null;
		String tipoAmbiente = sessao.getTipoAmbiente();
		if(TipoAmbienteConstante.PRESENCIAL.getSigla().equals(tipoAmbiente)){
			dataInicio = sessao.getDataFim();
			if(dataInicio == null){
				dataInicio = sessao.getDataPrevistaFim();
			}
		}else{
			dataInicio = sessao.getDataFim();
			if(dataInicio == null){
				dataInicio = sessao.getDataPrevistaFim();
			}
		}		
		return dataInicio;
	}	
	
	public static Calendar date2Calendar(Date date) {
		return date2GregorianCalendar(date);
	}

	/**
	 * Define a data e hora do fim da sessão virtual. Sempre as quintas-feiras as 23:59:00
	 * @param dataPrevistaInicio
	 * @param feriados 
	 * @return
	 */
	public static Date datSessaoFim(Date dataPrevistaInicio, List<Calendar> feriados) {
		int diasUteis = 6; // Quantidade de dias de duração da sessão
		
		Calendar calendarRetorno = date2Calendar(dataPrevistaInicio);

		int qtdDias = 0;
		
		if (DataUtil.isDiaUtil(calendarRetorno, feriados))
			qtdDias++;
		
		while (qtdDias < diasUteis) {
			calendarRetorno.add(Calendar.DAY_OF_MONTH, 1);
			
			if (DataUtil.isDiaUtil(calendarRetorno, feriados))
				qtdDias++;
		}
		
		calendarRetorno.set(Calendar.HOUR_OF_DAY, 23); //Dia com 24 horas, se usar HOUR será dia AM e PM
		calendarRetorno.set(Calendar.MINUTE, 59);
		calendarRetorno.set(Calendar.SECOND, 59);
		calendarRetorno.set(Calendar.MILLISECOND, 999);
	    
		return calendarRetorno.getTime();
	}
	
	/**
	 * Define qual é data inicial do slote da sessao virtual. Sempre iniciando
	 * na proxima sexta, depois do dateInformado 
	 * @param dateInformado
	 * @return
	 */
	public static Date dateSessaoInicio(Date dateInformado) {
		return dateSessaoInicio(dateInformado, null);
	}
	
	public static Date dateSessaoInicio(Date dateInformado, List<Calendar> feriados) {
		int diaInicioSessao = Calendar.FRIDAY;
		
		Calendar calendarInformado = date2Calendar(dateInformado);
		calendarInformado.set(Calendar.HOUR_OF_DAY, 00);
        calendarInformado.set(Calendar.MINUTE, 00);
        calendarInformado.set(Calendar.SECOND, 00);
        calendarInformado.set(Calendar.MILLISECOND, 0);
        
        int diaSemana = calendarInformado.get(Calendar.DAY_OF_WEEK);
        
        if (diaSemana == diaInicioSessao && DataUtil.isDiaUtil(calendarInformado, feriados))
        	return calendarInformado.getTime();
        
	    do {
	    	diaSemana = calendarInformado.get(Calendar.DAY_OF_WEEK);
	    			
	        switch (diaSemana) {
		        case Calendar.SUNDAY:
		        	calendarInformado.add(Calendar.DAY_OF_MONTH, 5);
		            break;
		        case Calendar.MONDAY:
		        	calendarInformado.add(Calendar.DAY_OF_MONTH, 4);
		            break;
		        case Calendar.TUESDAY:
		        	calendarInformado.add(Calendar.DAY_OF_MONTH, 3);
		            break;
		        case Calendar.WEDNESDAY:
		        	calendarInformado.add(Calendar.DAY_OF_MONTH, 2);
		            break;
		        case Calendar.THURSDAY:
		        	calendarInformado.add(Calendar.DAY_OF_MONTH, 1);
		            break;
		        case Calendar.FRIDAY:
		        	calendarInformado.add(Calendar.DAY_OF_MONTH, 7);
		            break;
		         case Calendar.SATURDAY:
		        	calendarInformado.add(Calendar.DAY_OF_MONTH, 6);
		            break;
	        }
        } while (!DataUtil.isDiaUtil(calendarInformado, null)); // Enquanto não encontrar um dia fora dos recessos (ignora feriados, ou seja, pode iniciar a sessão em dia de feriado)
        
        return  calendarInformado.getTime();
	}
	
	public static Short getAnoSessaoVirtual(Date dataPrevistaInicio) {
		Calendar anoCalendar = date2Calendar(dataPrevistaInicio);
		Short ano = (short) anoCalendar.get(Calendar.YEAR);
		return ano;
	}
}