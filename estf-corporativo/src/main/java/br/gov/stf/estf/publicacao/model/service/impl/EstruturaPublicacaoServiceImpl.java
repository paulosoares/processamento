package br.gov.stf.estf.publicacao.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao.EstruturaPublicacaoId;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.ClasseService;
import br.gov.stf.estf.publicacao.model.dataaccess.EstruturaPublicacaoDao;
import br.gov.stf.estf.publicacao.model.service.EstruturaPublicacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("estruturaPublicacaoService")
public class EstruturaPublicacaoServiceImpl extends GenericServiceImpl<EstruturaPublicacao, EstruturaPublicacaoId, EstruturaPublicacaoDao> 
		implements EstruturaPublicacaoService {
	
	@Autowired
	public ClasseService classeService;
	
	@Autowired
	public MinistroService ministroService;
	
	final ArquivoEletronicoService arquivoEletronicoService;
	
    public EstruturaPublicacaoServiceImpl( EstruturaPublicacaoDao estruturaPublicacaoDao, 
    		                               ArquivoEletronicoService arquivoEletronicoService,
    		                               ClasseService classeService,
    		                               MinistroService ministroService ) {
    	super(estruturaPublicacaoDao);
    	this.arquivoEletronicoService = arquivoEletronicoService;
    	this.classeService = classeService;
    	this.ministroService = ministroService;
    }
	
	public String recuperarDescricao(Integer codigoCapitulo, Integer codigoMateria)
			throws ServiceException {
		String resp = null;

		try {			
			resp = dao.recuperarDescricao(codigoCapitulo, codigoMateria);	
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}

		return resp;
	}

	public byte[] recuperarTitulo(Integer codigoCapitulo, Integer codigoMateria)
			throws ServiceException {
		EstruturaPublicacao estruturaPublicacao = recuperar(codigoCapitulo, codigoMateria, null);
		byte[] titulo = null;
		if ( estruturaPublicacao!=null ) {
			ArquivoEletronico arquivoEletronico = estruturaPublicacao.getArquivoEletronico();
			if ( arquivoEletronico!=null ) {
				titulo = arquivoEletronico.getConteudo();
			}
		}
		return titulo;
		
	}

	public EstruturaPublicacao recuperar(Integer codCapitulo,
			Integer codMateria, Integer codConteudo) throws ServiceException {
		if ( codCapitulo==null ) {
			throw new IllegalArgumentException("codCapitulo must not be null");
		}
		if ( codMateria==null ) {
			codMateria = 0;
		}
		if ( codConteudo==null ) {
			codConteudo = 0;
		}
		EstruturaPublicacao estruturaPublicacao = null;
		try {
			estruturaPublicacao = dao.recuperar(codCapitulo, codMateria, codConteudo);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return estruturaPublicacao;
	}

	public List<EstruturaPublicacao> pesquisar(Integer codigoCapitulo,
			Integer codigoMaterias, Integer codigoConteudo)
			throws ServiceException {
		List<EstruturaPublicacao> capitulos = null;
		try {
			capitulos = dao.pesquisar(codigoCapitulo, codigoMaterias, codigoConteudo);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return capitulos;
	}
	
	/** Define a Estrutura de Publicação de uma Ata.
	 *  Aqui é definido: Capítulo, Matéria e Conteúdo.
	 *  @param texto
	 *  @return
	 *  @throws ServiceException */
	public EstruturaPublicacao definirEstruturaDePublicacaoDeAta(Texto texto, Boolean isRepublicacao, Boolean isDespachoDecisaoPresidente) throws ServiceException{
		EstruturaPublicacaoId ep = new EstruturaPublicacaoId();
		ep.setCodigoCapitulo( classificarTextoPorCapitulo( texto ) );
		ep.setCodigoMateria( classificarTextoPorMateria( texto, ep.getCodigoCapitulo(), isRepublicacao, isDespachoDecisaoPresidente ) );
		ep.setCodigoConteudo( classificarTextoPorConteudo( texto, ep.getCodigoCapitulo(), ep.getCodigoMateria() ) );
		/* Baseado na classificação, uma Estrutura de Publicação em banco é recuperada */
		return recuperar( ep.getCodigoCapitulo(), ep.getCodigoMateria(), ep.getCodigoConteudo() );
	}
	
	/** Define o Capítulo no qual determinado 
	 *  Texto deve ser classificado.
	 *  @param texto
	 *  @return
	 *  @throws ServiceException */
	private Integer classificarTextoPorCapitulo(Texto texto) throws ServiceException{
		if(texto.getTipoTexto().equals( TipoTexto.DESPACHO ) ||
			texto.getTipoTexto().equals( TipoTexto.DECISAO_MONOCRATICA )){
			return EstruturaPublicacao.COD_CAPITULO_SECRETARIA_JUDICIARIA;
		} else {
			return 0;
		}
	}
	
	/** Define a Matéria no qual determinado 
	 *  Texto deve ser classificado.
	 *  @param texto
	 *  @return
	 *  @throws ServiceException 
	 *  A classificação leva em consideração as prioridades definidas como 1ª, 2ª e 3ª*/
	private Integer classificarTextoPorMateria(Texto texto, Integer codigoCapitulo, Boolean isRepublicacao, Boolean isDespachoDecisaoPresidente) throws ServiceException{
		Classe classe = new Classe();
		/* A 1ª classificação deve ser relacionada a Republicação. */
		if ( codigoCapitulo.equals( EstruturaPublicacao.COD_CAPITULO_SECRETARIA_JUDICIARIA ) && isRepublicacao ) {
			return EstruturaPublicacao.COD_MATERIA_REPUBLICACOES;
		}		
		/* A 2ª classificação deve ser relacionada a Presidência. */
		Ministro ministroRelator = ministroService.recuperarMinistroRelator(texto.getObjetoIncidente());		
		if ( ministroRelator != null ) {
			if ( ministroRelator.getId().equals( Ministro.COD_MINISTRO_PRESIDENTE ) || 
                 ministroRelator.getId().equals( Ministro.COD_MINISTRO_VICE_PRESIDENTE ) ) {
				return EstruturaPublicacao.COD_MATERIA_PROCESSOS_DE_COMPETENCIA_DA_PRESIDENCIA;	
			}
		} else {
			/* Significa que não há relator sendo, assim, de responsabilidade da Presidência */
			if ( isDespachoDecisaoPresidente != null && isDespachoDecisaoPresidente ) {
				return EstruturaPublicacao.COD_MATERIA_DECISOES_E_DESPACHOS_DO_PRESIDENTE;
			} else {
				/* A 3ª classificação deve ser relacionada a Processo Originários e Recursos. */
				try {
					Processo processo = (Processo) texto.getObjetoIncidente();
					classe = classeService.recuperarPorId( processo.getSiglaClasseProcessual() );
				} catch (ServiceException e) {
					throw new ServiceException( e );
				}
				if(codigoCapitulo.equals(EstruturaPublicacao.COD_CAPITULO_SECRETARIA_JUDICIARIA)){
					if( classe.getOriginario() ){
			    		return EstruturaPublicacao.COD_MATERIA_ORIGINARIOS;
			    	} else {
			    		return EstruturaPublicacao.COD_MATERIA_RECURSOS;
			    	}
				}
			}
		}
		/* Caso o texto não seja classificado, seja por haver Relator (Presidente ou Vice-Presidente)
		 * ou por não haver, ele será classificado segundo a Classe, no entanto, ainda pode haver
		 * a possibilidade da Flag que indique Despacho ou Decisão do Presidente ter sido selecionada.
		 * Esse caso ocorre quando o texto é gerado pela Presidência ou Vice-Presidência. */
		if ( isDespachoDecisaoPresidente != null && isDespachoDecisaoPresidente ) {
			return EstruturaPublicacao.COD_MATERIA_DECISOES_E_DESPACHOS_DO_PRESIDENTE;
		}
		/* A 3ª classificação deve ser relacionada a Processo Originários e Recursos. */
		try {
			classe = classeService.recuperarPorId( texto.getSiglaClasseProcessual() );
		} catch (ServiceException e) {
			throw new ServiceException( e );
		}
		if(codigoCapitulo.equals(EstruturaPublicacao.COD_CAPITULO_SECRETARIA_JUDICIARIA)){
			if( classe.getOriginario() ){
	    		return EstruturaPublicacao.COD_MATERIA_ORIGINARIOS;
	    	} else {
	    		return EstruturaPublicacao.COD_MATERIA_RECURSOS;
	    	}
		}
		return 0;
	}
	
	/** Define o Conteúdo no qual determinado 
	 *  Texto deve ser classificado.
	 *  @param texto
	 *  @return
	 *  @throws ServiceException */
	private Integer classificarTextoPorConteudo(Texto texto, Integer codCapitulo, Integer codMateria) throws ServiceException{
		/* Classifica o conteúdo do Capítulo da Secretaria Judiciária */
		if( codCapitulo.equals(EstruturaPublicacao.COD_CAPITULO_SECRETARIA_JUDICIARIA) ){
			if( codMateria.equals(EstruturaPublicacao.COD_MATERIA_ORIGINARIOS) ||
				codMateria.equals(EstruturaPublicacao.COD_MATERIA_RECURSOS)	||
				codMateria.equals(EstruturaPublicacao.COD_MATERIA_REPUBLICACOES) ||
				codMateria.equals(EstruturaPublicacao.COD_MATERIA_PROCESSOS_DE_COMPETENCIA_DA_PRESIDENCIA) ||
				codMateria.equals(EstruturaPublicacao.COD_MATERIA_DECISOES_E_DESPACHOS_DO_PRESIDENTE) ){
				return 50;				
			} else {
				return 0;
			}
		}
		/* Só há classificação para 00 ou 50, não foi definido regra para 90 e 10 */
		return 0;
	}

}
