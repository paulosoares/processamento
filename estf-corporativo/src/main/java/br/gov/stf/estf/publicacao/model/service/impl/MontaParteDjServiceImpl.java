package br.gov.stf.estf.publicacao.model.service.impl;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.model.GerarCabecalhoResponse;
import br.gov.stf.estf.cabecalho.model.InformacaoProcedencia;
import br.gov.stf.estf.cabecalho.model.InformacoesOrigem.InformacaoOrigem;
import br.gov.stf.estf.cabecalho.model.InformacoesParte.InformacaoParte;
import br.gov.stf.estf.cabecalho.model.OcorrenciasMinistro.OcorrenciaMinistro;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.CabecalhoTexto;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.estf.publicacao.model.service.MontaParteDjService;
import br.gov.stf.estf.publicacao.model.util.Partes;
import br.gov.stf.estf.publicacao.model.util.Partes.Parte;
import br.gov.stf.framework.model.service.ServiceException;

@Service("montaParteDjService")
public class MontaParteDjServiceImpl implements MontaParteDjService {
	private final CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;
	
	private static final boolean INCLUIR_DESCRICAO_ORIGEM = true;
	
	private static final String[] CATEGORIAS_PARA_INDICE = {"ADV", "PROC", "DPU"};	
	
	
	public MontaParteDjServiceImpl(
			CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService) {
		super();
		this.cabecalhoObjetoIncidenteService = cabecalhoObjetoIncidenteService;
	}


	private String getDescricaoObjetoIncidente (CabecalhoObjetoIncidente cabecalho, TipoObjetoIncidente tipoOi) {
		if ( TipoObjetoIncidente.PETICAO==tipoOi ) {
			return "PETIÇÃO AVULSA "+editarNumero(cabecalho.getInformacaoPeticao().getNumeroPeticao())+"/"+cabecalho.getInformacaoPeticao().getAnoPeticao();
		} else if ( TipoObjetoIncidente.PROTOCOLO==tipoOi ) {
			return "PROTOCOLO "+editarNumero(cabecalho.getInformacaoPeticao().getNumeroPeticao())+"/"+cabecalho.getInformacaoPeticao().getAnoPeticao();
		} else {
			return cabecalho.getInformacaoProcessoRecurso().getDescricaoCadeia()+" "+editarNumero(cabecalho.getInformacaoProcessoRecurso().getNumeroProcesso());
		}
	}
	
	private String editarNumero(long numProc) {
		String sb = "";
		String n = String.valueOf(numProc);
		int tam = n.length();
		int letras = 0;
		for (int i = tam - 1; i >= 0; i--) {
			letras++;
			if (letras % 4 == 0 && i != tam - 1) {
				letras = 1;
				sb = '.' + sb;
			}
			sb = n.charAt(i) + sb;
		}
		return sb.toString();
	}


	public byte[] recuperarCabecalhoPartesDj(ObjetoIncidente<?> objetoIncidente)
			throws ServiceException {
		return recuperarCabecalhoPartesDj(objetoIncidente, true);
	}


	private static boolean startsWithCategoriaParaIndice(String valorParaComparacao) {
		boolean result = false;
		
		if (valorParaComparacao != null) {
			for (String categoriaIndice : CATEGORIAS_PARA_INDICE) {
				if (valorParaComparacao.startsWith(categoriaIndice)) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}


	public byte[] recuperarCabecalhoPartesDj(
			ObjetoIncidente<?> objetoIncidente, boolean inserirOrigem)
			throws ServiceException {
		CabecalhoObjetoIncidente cabecalhoObjetoIncidente = cabecalhoObjetoIncidenteService.recuperarCabecalho( objetoIncidente.getId() );
		try {
			return ajustarCabecalhoIndice(objetoIncidente, inserirOrigem, cabecalhoObjetoIncidente);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	@Override
	public byte[] recuperarCabecalhoPartesDj(Texto texto, boolean inserirOrigem)
			throws ServiceException {
		CabecalhoTexto cabecalhoTexto = null;
		if (texto.getFasesTextoProcesso() != null) {
			for (FaseTextoProcesso transicaoFase : texto.getFasesTextoProcesso()) {
				if (transicaoFase.getTipoFaseTextoDocumentoDestino().equals(FaseTexto.LIBERADO_PUBLICACAO)) {
					cabecalhoTexto = transicaoFase.getCabecalhoTexto();
					break;
				}
			}
		}
		
		CabecalhoObjetoIncidente cabecalhoObjetoIncidente = null;
		if (cabecalhoTexto != null) {
			try {
				cabecalhoObjetoIncidente = recuperarCabecalhoObjetoIncidente(cabecalhoTexto.getXml().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		} else {
			cabecalhoObjetoIncidente = cabecalhoObjetoIncidenteService.recuperarCabecalho(texto.getObjetoIncidente().getId());
		}
		
		try {
			return ajustarCabecalhoIndice(texto.getObjetoIncidente(), inserirOrigem, cabecalhoObjetoIncidente);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	/**
	 * @param texto
	 * @param inserirOrigem
	 * @param cabecalhoObjetoIncidente
	 * @return
	 * @throws JAXBException 
	 * @throws UnsupportedEncodingException 
	 * @throws ServiceException 
	 */
	private byte[] ajustarCabecalhoIndice(
			ObjetoIncidente<?> objetoIncidente, boolean inserirOrigem,
			CabecalhoObjetoIncidente cabecalhoObjetoIncidente) throws UnsupportedEncodingException, JAXBException, ServiceException {
		br.gov.stf.estf.publicacao.model.util.ObjetoIncidente oi = getObjetoIncidenteParaXml(
				objetoIncidente, cabecalhoObjetoIncidente);
		
		Partes partes = new Partes();
		
		insereDadosDaOrigem(inserirOrigem, cabecalhoObjetoIncidente, partes);
		
		insereDadosDosMinistros(cabecalhoObjetoIncidente, partes);
		
		insereInformacoesDePartes(cabecalhoObjetoIncidente, partes);
		
		oi.setPartes( partes );
		
		try {
			return oi.toXML();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	/**
	 * @param objetoIncidente
	 * @param cabecalhoObjetoIncidente
	 * @return
	 */
	private br.gov.stf.estf.publicacao.model.util.ObjetoIncidente getObjetoIncidenteParaXml(
			ObjetoIncidente<?> objetoIncidente,
			CabecalhoObjetoIncidente cabecalhoObjetoIncidente) {
		br.gov.stf.estf.publicacao.model.util.ObjetoIncidente oi = new br.gov.stf.estf.publicacao.model.util.ObjetoIncidente();
		oi.setObjetoIncidente( cabecalhoObjetoIncidente.getSequencialObjetoIncidente() );
		oi.setDescricao( getDescricaoObjetoIncidente(cabecalhoObjetoIncidente, objetoIncidente.getTipoObjetoIncidente()) );
		return oi;
	}


	/**
	 * @param cabecalhoObjetoIncidente
	 * @param partes
	 */
	private void insereInformacoesDePartes(
			CabecalhoObjetoIncidente cabecalhoObjetoIncidente, Partes partes) {
		if ( cabecalhoObjetoIncidente.getInformacoesParte()!=null ) {
			// PARTES
			List<InformacaoParte> infoPartes = cabecalhoObjetoIncidente.getInformacoesParte().getInformacaoParte();
			int nPartes = infoPartes.size();
			for ( int i=0 ; i<nPartes ; i++) {
				InformacaoParte ip = infoPartes.get(i);
				Parte p = new Parte();
				p.setCategoria( ip.getCategoriaParte() );
				
				String nomeParte = ip.getApresentacaoParte();
				String tipoImpressao = ip.getDescricaoTipoImpressao(); 
				if ( tipoImpressao != null && tipoImpressao.length() > 0 ) {
					nomeParte = nomeParte + " " + tipoImpressao;
				}
				p.setNome( nomeParte );
				p.setIndice(true);
				
//				if (ip.getDescricaoTipoImpressao() != null && ip.getDescricaoTipoImpressao().length() > 0) {
//					p.setNome( ip.getApresentacaoParte()+" "+ip.getDescricaoTipoImpressao() );	
//				} else {
//					p.setNome( ip.getApresentacaoParte() );
//				}
				
				
				
				/*
				 * A rotina abaixo não funcionará se houver 3 partes "do mesmo lado". Por exemplo:
				 * - Impte. : "Fulano 1"
				 * - Impte. : "Beltrano 1"
				 * - Impte. : "Ciclano 1"
				 * - Adv.   : "Advogado xpto"
				 * - Imptdo.: "Fulano 2"
				 * 
				 * obs.: na verdade, se houver mais de uma parte "do mesmo lado" com um ou mais advogados,
				 * a rotina abaixo também incluirá, de acordo com o exemplo acima, o "Fulano 1" e o "Beltrano 1"
				 * no índice, assim como o "Advogado xpto".
				 */
//				if ( (startsWithCategoriaParaIndice(p.getCategoria())) || 
//					 (i+1 < nPartes && !startsWithCategoriaParaIndice(infoPartes.get(i+1).getCategoriaParte())) ) {
//					p.setIndice( true );
//				}
				
				// a parte atual nao e advogado e a proxima parte e advogado
				if ( !startsWithCategoriaParaIndice(p.getCategoria()) && 
							i+1 < nPartes && 
							startsWithCategoriaParaIndice(infoPartes.get(i+1).getCategoriaParte()) ) {
					p.setIndice(false);
				}
				
				
				
//				if ( (p.getCategoria().startsWith("ADV")) || 
//					 (i+1 < nPartes && !infoPartes.get(i+1).getCategoriaParte().startsWith("ADV")) ) {
//					p.setIndice( true );
//				}
									
				
				partes.getParte().add( p );
			}
		}
	}


	/**
	 * @param cabecalhoObjetoIncidente
	 * @param partes
	 */
	private void insereDadosDosMinistros(
			CabecalhoObjetoIncidente cabecalhoObjetoIncidente, Partes partes) {
		if ( cabecalhoObjetoIncidente.getOcorrenciasMinistro()!=null ) {
		
			// RELATORES
			List<OcorrenciaMinistro> ministros = cabecalhoObjetoIncidente.getOcorrenciasMinistro().getOcorrenciaMinistro();
			for ( OcorrenciaMinistro om: ministros ) {
				Parte parteMinistro = new Parte();
				parteMinistro.setCategoria( om.getCategoriaMinistro() );
				parteMinistro.setNome( om.getApresentacaoMinistro() );
				partes.getParte().add( parteMinistro );
			}
		}
	}


	/**
	 * @param inserirOrigem
	 * @param cabecalhoObjetoIncidente
	 * @param partes
	 */
	private void insereDadosDaOrigem(boolean inserirOrigem,
			CabecalhoObjetoIncidente cabecalhoObjetoIncidente, Partes partes) {
		if ( cabecalhoObjetoIncidente.getInformacoesOrigem()!=null ) {
			InformacaoOrigem origem = cabecalhoObjetoIncidente.getInformacoesOrigem().getInformacaoOrigem().get( cabecalhoObjetoIncidente.getInformacoesOrigem().getInformacaoOrigem().size()-1 );
			if ( inserirOrigem ) {

				// ORIGEM
				Parte parteOrigem = new Parte();
				parteOrigem.setCategoria( "ORIGEM" );
				
				StringBuffer sbNomeOrigem = new StringBuffer();
				sbNomeOrigem.append(origem.getSiglaClasseOrigem());
				if(sbNomeOrigem.toString() != null && sbNomeOrigem.toString().length() > 0 &&
				   origem.getNumeroProcessoOrigem() != null && origem.getNumeroProcessoOrigem().length() > 0) {
					sbNomeOrigem.append(" - ");
					sbNomeOrigem.append(origem.getNumeroProcessoOrigem());
				}
				
				if (INCLUIR_DESCRICAO_ORIGEM) {
					if(sbNomeOrigem.toString() != null && sbNomeOrigem.toString().length() > 0 &&
					   origem.getDescricaoOrigem() != null && origem.getDescricaoOrigem().length() > 0) {
						sbNomeOrigem.append(" - ");
						sbNomeOrigem.append(origem.getDescricaoOrigem());
					}	
				} else {
					if(sbNomeOrigem.toString() != null && sbNomeOrigem.toString().length() > 0 &&
					   origem.getSiglaOrigem() != null && origem.getSiglaOrigem().length() > 0) {
						sbNomeOrigem.append(" - ");
						sbNomeOrigem.append(origem.getSiglaOrigem());
					}				
				}
				
				parteOrigem.setNome( sbNomeOrigem.toString() );
				partes.getParte().add( parteOrigem );	
			}
			
			// PROCEDENCIA
			InformacaoProcedencia procedencia = origem.getInformacaoProcedencia();
			Parte parteProcedencia = new Parte();
			parteProcedencia.setCategoria( "PROCED." );
			parteProcedencia.setNome( procedencia.getDescricaoProcedencia() );
			partes.getParte().add( parteProcedencia );			
		}
	}
	
	@SuppressWarnings("unchecked")
	private CabecalhoObjetoIncidente recuperarCabecalhoObjetoIncidente(
			byte[] xml) throws ServiceException {
		GerarCabecalhoResponse response = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(GerarCabecalhoResponse.class.getPackage().getName());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<GerarCabecalhoResponse> element = (JAXBElement<GerarCabecalhoResponse>) unmarshaller
					.unmarshal(new ByteArrayInputStream(xml));
			response = element.getValue();
		} catch (JAXBException e) {
			throw new ServiceException("Erro ao desserializar resposta do cabeçalho", e);
		}

		List<CabecalhoObjetoIncidente> cabecalhos = null;
		if (response.isFlagSucesso()) {
			cabecalhos = response.getCabecalhosObjetoIncidente().getCabecalhoObjetoIncidente();
		} else {
			throw new ServiceException(response.getMensagem());
		}
		CabecalhoObjetoIncidente cabecalho = cabecalhos.get(0); 
		return cabecalho;
	}
}
