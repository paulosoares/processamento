package br.gov.stf.estf.processostf.model.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Advogado;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PrescricaoReu;
import br.gov.stf.estf.entidade.processostf.ProcessoPrescricaoParte;
import br.gov.stf.estf.localizacao.model.service.AdvogadoService;
import br.gov.stf.estf.localizacao.model.service.OrigemService;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.processostf.model.dataaccess.PrescricaoReuDao;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.PrescricaoReuService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("prescricaoReuService")
public class PrescricaoReuServiceImpl extends GenericServiceImpl<PrescricaoReu, Long, PrescricaoReuDao> 
	implements PrescricaoReuService {
	
	private final DeslocaProcessoService deslocaProcessoService;
	private final SetorService setorService;
	private final AdvogadoService advogadoService;
	private final OrigemService origemService;

	public PrescricaoReuServiceImpl(PrescricaoReuDao dao, DeslocaProcessoService deslocaProcessoService, 
			SetorService setorService, AdvogadoService advogadoService, OrigemService origemService) {
		super(dao);
		this.deslocaProcessoService = deslocaProcessoService;
		this.setorService = setorService;
		this.advogadoService = advogadoService;
		this.origemService = origemService;
	}

	@Override
	public List<ProcessoPrescricaoParte> pesquisarProcessosPrescricao(Long idObjetoIncidente,
			Date dtPrescricaoInicial, Date dtPrescricaoFinal, Long codigoDestino, 
			Long idMinistro, String codigoPena, Boolean filtroEmTramitacao) throws ServiceException, ParseException {

		List<ProcessoPrescricaoParte> listaProcessoPrescricao = new LinkedList<ProcessoPrescricaoParte>();
		List<PrescricaoReu> listaPrescricaoReu = new LinkedList<PrescricaoReu>();
		
		try {
			listaPrescricaoReu = dao.pesquisarProcessosPrescricao(idObjetoIncidente, dtPrescricaoInicial, dtPrescricaoFinal,
				idMinistro, codigoPena, filtroEmTramitacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
		
		
		if (listaPrescricaoReu.size() > 0){
			ObjetoIncidente<?> objetoIncidenAnterior = null;
			for (PrescricaoReu presReu : listaPrescricaoReu){
				ProcessoPrescricaoParte procPrescParte = new ProcessoPrescricaoParte();
				
				if (presReu.getReferenciaPrescricao().getObjetoIncidente().equals(objetoIncidenAnterior)){
					procPrescParte.setMesmoProcesso(true);
				}else{
					procPrescParte.setMesmoProcesso(false);
					//TODO
					//recupera o nome do último setor de deslocamento (metodo crítico caso aconteça inconsistência de dados no banco)
					DeslocaProcesso deslocaProcesso = new DeslocaProcesso();
					deslocaProcesso = recuperaCodigoUltimoDeslocamento(presReu.getReferenciaPrescricao().
							getObjetoIncidente());
					procPrescParte.setCodigoUltimoSetorDeslocamento(deslocaProcesso.getCodigoOrgaoDestino());
					procPrescParte.setNomeUltimoSetorDeslocamento(recuperaSetorProcesso(deslocaProcesso));
					
				}
				
				procPrescParte.setPrescricaoReu(presReu);
				if (presReu.getDataPrescricaoPenaMinima() != null && presReu.getDataPrescricaoPenaMaxima() != null){
					procPrescParte.setTipoPena("Abstrata");
					procPrescParte.setDataPrescricaoPenaOrdenacao(presReu.getDataPrescricaoPenaMinima());
					procPrescParte.setEhAbstrata(true);
					procPrescParte.setTempoRestante(calculaTempoRestante(true, presReu));
					procPrescParte.setTempoRestanteParaOrdenacao(calculaTempoRestanteParaOrdenacao(true, presReu));
				}else{
					procPrescParte.setTipoPena("Concreta");
					procPrescParte.setDataPrescricaoPenaOrdenacao(presReu.getDataPrescricao());
					procPrescParte.setPrescricaoReu(presReu);
					procPrescParte.setEhConcreta(true);
					procPrescParte.setTempoRestante(calculaTempoRestante(false, presReu));
					procPrescParte.setTempoRestanteParaOrdenacao(calculaTempoRestanteParaOrdenacao(false, presReu));

				}
				
				objetoIncidenAnterior = presReu.getReferenciaPrescricao().getObjetoIncidente();
				listaProcessoPrescricao.add(procPrescParte);
			}
		}
		List<ProcessoPrescricaoParte> listaProcessoPrescCodDestino = new LinkedList<ProcessoPrescricaoParte>();
		// o filtro do destinatario foi feito no serviço pois este campo não está mapeado na entidade processo.
		// Então o aplicativo processamento busca todos os registros e depois recupera o ultimo deslocamento
		// para cada registro.
		if (codigoDestino != null && listaProcessoPrescricao != null && listaProcessoPrescricao.size() > 0){
			for (ProcessoPrescricaoParte procPresParte : listaProcessoPrescricao){
				if (procPresParte.getCodigoUltimoSetorDeslocamento() != null){
					if (procPresParte.getCodigoUltimoSetorDeslocamento().equals(codigoDestino)){
						listaProcessoPrescCodDestino.add(procPresParte);
					}
				}
			}
			return listaProcessoPrescCodDestino;
		}else{
			return listaProcessoPrescricao;	
		}
		
	}	
	
	private String calculaTempoRestante(Boolean ehAbstrata, PrescricaoReu presReu) throws ParseException{
		if (ehAbstrata){
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");    
            GregorianCalendar dataAtual = new GregorianCalendar();  
            dataAtual.setTime(dateFormat.parse(formatarData(new Date())));  
            StringBuffer tempoRestanteConcatenado = new StringBuffer();
            
            GregorianCalendar dataPenaMaxima = new GregorianCalendar();  
            dataPenaMaxima.setTime(dateFormat.parse(formatarData(presReu.getDataPrescricaoPenaMaxima()))); 
            
            GregorianCalendar dataPenaMinima = new GregorianCalendar();  
            dataPenaMinima.setTime(dateFormat.parse(formatarData(presReu.getDataPrescricaoPenaMinima())));  
			
            
            if ((dataPenaMinima.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR) == 0)){
            	if ((dataPenaMinima.get(GregorianCalendar.MONTH)- dataAtual.get(GregorianCalendar.MONTH) < 0)){
            		tempoRestanteConcatenado.append("- - ");
            	}else if ((dataPenaMinima.get(GregorianCalendar.MONTH) - dataAtual.get(GregorianCalendar.MONTH) == 0)){
            		if ((dataPenaMinima.get(GregorianCalendar.DAY_OF_MONTH) - dataAtual.get(GregorianCalendar.DAY_OF_MONTH) <= 0)){
            			tempoRestanteConcatenado.append("- - ");
            		}else{
            			tempoRestanteConcatenado.append(exibeDiasFaltantes(dataAtual, dataPenaMinima));
            		}
            	}else{
            		tempoRestanteConcatenado.append(exibeDiasFaltantes(dataAtual, dataPenaMinima));
            	}
            }else if (( dataPenaMinima.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR) > 0)){
            	tempoRestanteConcatenado.append(exibeDiasFaltantes(dataAtual, dataPenaMinima));
            }else{
            	tempoRestanteConcatenado.append("- - ");
            }
            
            if (tempoRestanteConcatenado != null && tempoRestanteConcatenado.length() > 0){
            	tempoRestanteConcatenado.append("|");
            }
            
            if ((dataPenaMaxima.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR) == 0)){
            	if ((dataPenaMaxima.get(GregorianCalendar.MONTH)- dataAtual.get(GregorianCalendar.MONTH) < 0)){
            		return "prescrito";
            	}else if ((dataPenaMaxima.get(GregorianCalendar.MONTH) - dataAtual.get(GregorianCalendar.MONTH) == 0)){
            		if ((dataPenaMaxima.get(GregorianCalendar.DAY_OF_MONTH) - dataAtual.get(GregorianCalendar.DAY_OF_MONTH) <= 0)){
            			return "prescrito";
            		}else{
            			tempoRestanteConcatenado.append(exibeDiasFaltantes(dataAtual, dataPenaMaxima));
            		}
            	}else{
            		tempoRestanteConcatenado.append(exibeDiasFaltantes(dataAtual, dataPenaMaxima));
            	}
            }else if ((dataPenaMaxima.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR) > 0)){
            	tempoRestanteConcatenado.append(exibeDiasFaltantes(dataAtual, dataPenaMaxima));
            }else{
            	return "prescrito";
            }
            return tempoRestanteConcatenado.toString();
		}else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");    
            GregorianCalendar dataAtual = new GregorianCalendar();  
            dataAtual.setTime(dateFormat.parse(formatarData(new Date())));  
            
            GregorianCalendar dataPenaPrescricao = new GregorianCalendar();  
            dataPenaPrescricao.setTime(dateFormat.parse(formatarData(presReu.getDataPrescricao()))); 
            
            if ((dataAtual.get(GregorianCalendar.YEAR)- dataPenaPrescricao.get(GregorianCalendar.YEAR) == 0)){
            	if ((dataPenaPrescricao.get(GregorianCalendar.MONTH)- dataAtual.get(GregorianCalendar.MONTH) < 0)){
            		return "prescrito";
            	}else if ((dataPenaPrescricao.get(GregorianCalendar.MONTH) - dataAtual.get(GregorianCalendar.MONTH) == 0)){
            		if ((dataPenaPrescricao.get(GregorianCalendar.DAY_OF_MONTH) - dataAtual.get(GregorianCalendar.DAY_OF_MONTH) <= 0)){
            			return "prescrito";
            		}else{
            			return exibeDiasFaltantes(dataAtual, dataPenaPrescricao);
            		}
            	}else{
            		return exibeDiasFaltantes(dataAtual, dataPenaPrescricao);
            	}
            }else if ((dataPenaPrescricao.get(GregorianCalendar.YEAR)- dataAtual.get(GregorianCalendar.YEAR) > 0)){
            	return exibeDiasFaltantes(dataAtual, dataPenaPrescricao);
            }else{
            	return "prescrito";
            }
		}
	}
	
	private String exibeDiasFaltantes (GregorianCalendar dataAtual, GregorianCalendar dataCalc){
		GregorianCalendar dif = new GregorianCalendar();  
        dif.setTimeInMillis(dataCalc.getTimeInMillis() - dataAtual.getTimeInMillis());
		return (dataCalc.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR)) + "a"    
        + (dif.get(GregorianCalendar.MONTH)) + "m"    
        + dif.get(GregorianCalendar.DAY_OF_MONTH) + "d";
	}
	
//Metodo responsavel para calcular o tempo total para prescricao em dias 	
	
	private int calculaTempoRestanteParaOrdenacao(Boolean ehAbstrata, PrescricaoReu presReu) throws ParseException{
		if (ehAbstrata){
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");    
            GregorianCalendar dataAtual = new GregorianCalendar();  
            dataAtual.setTime(dateFormat.parse(formatarData(new Date())));  
            StringBuffer tempoRestanteConcatenado = new StringBuffer();
            
            GregorianCalendar dataPenaMaxima = new GregorianCalendar();  
            dataPenaMaxima.setTime(dateFormat.parse(formatarData(presReu.getDataPrescricaoPenaMaxima()))); 
            
            GregorianCalendar dataPenaMinima = new GregorianCalendar();  
            dataPenaMinima.setTime(dateFormat.parse(formatarData(presReu.getDataPrescricaoPenaMinima())));  
			
            
            if ((dataPenaMinima.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR) == 0)){
            	if ((dataPenaMinima.get(GregorianCalendar.MONTH)- dataAtual.get(GregorianCalendar.MONTH) < 0)){
            		tempoRestanteConcatenado.append("0");
            	}else if ((dataPenaMinima.get(GregorianCalendar.MONTH) - dataAtual.get(GregorianCalendar.MONTH) == 0)){
            		if ((dataPenaMinima.get(GregorianCalendar.DAY_OF_MONTH) - dataAtual.get(GregorianCalendar.DAY_OF_MONTH) <= 0)){
            			tempoRestanteConcatenado.append("0");
            		}else{
            			tempoRestanteConcatenado.append(exibeTotalDiasFaltantes(dataAtual, dataPenaMinima));
            		}
            	}else{
            		tempoRestanteConcatenado.append(exibeTotalDiasFaltantes(dataAtual, dataPenaMinima));
            	}
            }else if (( dataPenaMinima.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR) > 0)){
            	tempoRestanteConcatenado.append(exibeTotalDiasFaltantes(dataAtual, dataPenaMinima));
            }else{
            	tempoRestanteConcatenado.append("0");
            }
            
            if (tempoRestanteConcatenado != null && tempoRestanteConcatenado.length() > 0){
            	tempoRestanteConcatenado.append("0");
            }
            
            if ((dataPenaMaxima.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR) == 0)){
            	if ((dataPenaMaxima.get(GregorianCalendar.MONTH)- dataAtual.get(GregorianCalendar.MONTH) < 0)){
            		return 0;
            	}else if ((dataPenaMaxima.get(GregorianCalendar.MONTH) - dataAtual.get(GregorianCalendar.MONTH) == 0)){
            		if ((dataPenaMaxima.get(GregorianCalendar.DAY_OF_MONTH) - dataAtual.get(GregorianCalendar.DAY_OF_MONTH) <= 0)){
            			return 0;
            		}else{
                    	tempoRestanteConcatenado.append(exibeTotalDiasFaltantes (dataAtual,dataPenaMaxima));
            		}
            	}else{
                	tempoRestanteConcatenado.append(exibeTotalDiasFaltantes (dataAtual,dataPenaMaxima));
            	}
            }else if ((dataPenaMaxima.get(GregorianCalendar.YEAR) - dataAtual.get(GregorianCalendar.YEAR) > 0)){
            	tempoRestanteConcatenado.append(exibeTotalDiasFaltantes(dataAtual, dataPenaMaxima));
            }else{
            	return 0;
            }
            return exibeTotalDiasFaltantes(dataAtual, dataPenaMaxima);
		}else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");    
            GregorianCalendar dataAtual = new GregorianCalendar();  
            dataAtual.setTime(dateFormat.parse(formatarData(new Date())));  
            
            GregorianCalendar dataPenaPrescricao = new GregorianCalendar();  
            dataPenaPrescricao.setTime(dateFormat.parse(formatarData(presReu.getDataPrescricao()))); 
            
            if ((dataAtual.get(GregorianCalendar.YEAR)- dataPenaPrescricao.get(GregorianCalendar.YEAR) == 0)){
            	if ((dataPenaPrescricao.get(GregorianCalendar.MONTH)- dataAtual.get(GregorianCalendar.MONTH) < 0)){
            		return 0;
            	}else if ((dataPenaPrescricao.get(GregorianCalendar.MONTH) - dataAtual.get(GregorianCalendar.MONTH) == 0)){
            		if ((dataPenaPrescricao.get(GregorianCalendar.DAY_OF_MONTH) - dataAtual.get(GregorianCalendar.DAY_OF_MONTH) <= 0)){
            			return 0;
            		}else{
            			return exibeTotalDiasFaltantes (dataAtual,dataPenaPrescricao);
            		}
            	}else{
            		return exibeTotalDiasFaltantes (dataAtual,dataPenaPrescricao);
            	}
            }else if ((dataPenaPrescricao.get(GregorianCalendar.YEAR)- dataAtual.get(GregorianCalendar.YEAR) > 0)){
            	return exibeTotalDiasFaltantes (dataAtual,dataPenaPrescricao);
            }else{
            	exibeTotalDiasFaltantes (dataAtual,dataPenaPrescricao);
            	return 0;
            }
		}
	}	
	
	
	public int exibeTotalDiasFaltantes (GregorianCalendar dataAtual, GregorianCalendar dataCalc){
		long m = dataCalc.getTimeInMillis() - dataAtual.getTimeInMillis();
		return (int) (m / (1000*60*60*24));
	}

	
	// formatar data
	public String formatarData(Date data) throws ParseException {
		if (data == null)
			return null;
		return DateFormatUtils.format(data, "dd/MM/yyyy");
	}
	
	private String recuperaSetorProcesso(DeslocaProcesso ultimoDeslocamento) {


		if (ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(1)) {
			return recuperaDescricaoAdvogadoProcesso(ultimoDeslocamento.getCodigoOrgaoDestino());
		} else if (ultimoDeslocamento.getGuia().getTipoOrgaoDestino().equals(2)) {
			return recuperaDescricaoProcessoOrgaoInterno(ultimoDeslocamento.getCodigoOrgaoDestino());
		} else {
			return recuperaDescricaoOrgaoExternoProcesso(ultimoDeslocamento.getCodigoOrgaoDestino());
		}
		
	}
	
	
	private DeslocaProcesso recuperaCodigoUltimoDeslocamento(ObjetoIncidente<?> processo){
		DeslocaProcesso ultimoDeslocamento = null;
		try {
			ultimoDeslocamento = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ultimoDeslocamento;
	}

	public String recuperaDescricaoOrgaoExternoProcesso(Long codigoSetor) {
		Origem origem = new Origem();
		try {
			origem = origemService.recuperarPorId(codigoSetor);
			if (origem == null) {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return origem.getDescricao();
	}

	public String recuperaDescricaoProcessoOrgaoInterno(Long codigoSetor) {
		Setor setor = new Setor();
		try {
			setor = setorService.recuperarPorId(codigoSetor);
			if (setor == null) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return setor.getNome();
	}

	//TODO
	public String recuperaDescricaoAdvogadoProcesso(Long codigoSetor) {
		Advogado advogado = new Advogado();
		
		if (codigoSetor == null){
			return null;
		}
		
		try {
			advogado = advogadoService.recuperarPorId(codigoSetor);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return advogado.getNome();
	}
}
