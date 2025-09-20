package br.gov.stf.estf.processosetor.model.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.TipoConfiguracaoSetor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processosetor.ControleDistribuicao;
import br.gov.stf.estf.entidade.processosetor.HistoricoDistribuicao;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.UsuarioDistribuicaoClasseTipoJulgamento;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.UsuarioDistribuicao;
import br.gov.stf.estf.processosetor.model.dataaccess.DistribuicaoProcessoSetorDao;
import br.gov.stf.estf.processosetor.model.service.DistribuicaoProcessoSetorService;
import br.gov.stf.estf.processosetor.model.service.ProcessoSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("distribuicaoProcessoSetorService")
public class DistribuicaoProcessoSetorServiceImpl extends GenericServiceImpl<ControleDistribuicao, Long, DistribuicaoProcessoSetorDao>  
implements DistribuicaoProcessoSetorService {
    
    private final ProcessoSetorService processoSetorService;
    
    public DistribuicaoProcessoSetorServiceImpl(DistribuicaoProcessoSetorDao dao, ProcessoSetorService processoSetorService) {
        super(dao);
        this.processoSetorService = processoSetorService;
    }
    
    //####################################################################################################################################//         
    // DISTRIBUIÇÃO PROCESSO SETOR																										  //
    //####################################################################################################################################//   
	
	@SuppressWarnings("unchecked")
	public Boolean distribuirProcessoSetorAutomaticamente(List<ProcessoSetor> listaProcessoSetor, 
            List<UsuarioDistribuicao> listaUsuarios) 
	throws ServiceException {

		Boolean distribuido = Boolean.FALSE;
		
		validarDistribuicaoAutomatica(listaProcessoSetor, listaUsuarios);

		listaProcessoSetor = ordenarProcessosPorClasseNumeroRecurso(listaProcessoSetor);
		
		HistoricoDistribuicao distribuicao = new HistoricoDistribuicao();
        distribuicao.setDataDistribuicao(new Date());	 	
        
	 			
		Random random = new Random(new Date().getTime());
		boolean primeiroProcessoDistribuir = true;
		ProcessoSetor processoSetorAnterior = null;
		UsuarioDistribuicao usuarioSorteado = null;
		UsuarioDistribuicao usuarioSorteadoAnteriormente = null;
		
		for(ProcessoSetor processoSetor: listaProcessoSetor ) {
			
		    List usuariosParticipantes = getUsuariosComMenorCarga(listaUsuarios,processoSetor);
			if(usuariosParticipantes!=null && usuariosParticipantes.size()>0){ 
				int sorteio = random.nextInt(usuariosParticipantes.size());
				
				if(!primeiroProcessoDistribuir){					   
				    if(processoSetor.getProcesso() != null && processoSetor.getProcesso().getClasseProcessual().getId().equals(processoSetorAnterior.getProcesso().getClasseProcessual().getId())&&
						       processoSetor.getProcesso().getNumeroProcessual().equals(processoSetorAnterior.getProcesso().getNumeroProcessual())){
				    	usuarioSorteado = usuarioSorteadoAnteriormente;
				    } else {
						usuarioSorteado = (UsuarioDistribuicao)usuariosParticipantes.get(sorteio);
				    }					
				}else{
					usuarioSorteado = (UsuarioDistribuicao)usuariosParticipantes.get(sorteio);
				}
		        
		        distribuicao.setUsuario(usuarioSorteado.getUsuario());				
		        distribuido = distribuirProcessoSetor(instanciarHistoricoDistribuicao(distribuicao, processoSetor));
				if( distribuido==null||!distribuido.booleanValue() ) {
					throw new ServiceException("Não foi possível distribuir o processo/protocolo "+
                            processoSetor.getIdentificacao());		 					
				}

				usuarioSorteado.setCargaVirtual(usuarioSorteado.getCargaVirtual() + 1);
				processoSetorAnterior = processoSetor;
				usuarioSorteadoAnteriormente = usuarioSorteado;
				primeiroProcessoDistribuir = false;
			}
		}
 			
 		return distribuido;
	} 
	
	private List<UsuarioDistribuicao> getUsuariosComMenorCarga(List<UsuarioDistribuicao> listaUsuarios,
            ProcessoSetor processoSetor) {
		
		List<UsuarioDistribuicao> usuariosComMenorCarga = new LinkedList<UsuarioDistribuicao>();
		
		long menorCarga = getMenorCarga(listaUsuarios,processoSetor);
		
		for( UsuarioDistribuicao usuario: listaUsuarios ) {
			if( processoSetor.getDistribuicaoAtual() != null &&
			    processoSetor.getDistribuicaoAtual().getUsuario() != null &&
			    processoSetor.getDistribuicaoAtual().getUsuario().getId().equals(usuario.getUsuario().getId())){
				continue;
			}
			long cargaUsuario = usuario.getCargaVirtual();
			
			if( cargaUsuario == menorCarga )
				usuariosComMenorCarga.add(usuario);
		}
		
		return usuariosComMenorCarga;
	}
	
	public Boolean distribuirProcessoSetor(HistoricoDistribuicao distribuicao) 
	throws ServiceException {
		
		Boolean distribuiu = Boolean.FALSE;

		try {

			ProcessoSetor processoSetor = processoSetorService.recuperarPorId(distribuicao.getProcessoSetor().getId());
			distribuicao.setProcessoSetor(processoSetor);
			
			validarDistribuicao(distribuicao);

			processoSetor.setDistribuicaoAtual(distribuicao);
			
			List<HistoricoDistribuicao> historico = processoSetor.getHistoricoDistribuicoes();
			
			historico.add(0, distribuicao);
			
			distribuiu = processoSetorService.persistirProcessoSetor(processoSetor);
			processoSetorService.flushSession();
		}
		catch( ServiceException e ) {
			throw e;
		}

		return distribuiu;
	}	
	
	private HistoricoDistribuicao instanciarHistoricoDistribuicao(HistoricoDistribuicao distribuicao, ProcessoSetor processoSetor) {
		HistoricoDistribuicao novaDistribuicao = new HistoricoDistribuicao();
		
		novaDistribuicao.setProcessoSetor(processoSetor);
		novaDistribuicao.setUsuario(distribuicao.getUsuario());
		novaDistribuicao.setGrupoUsuario(distribuicao.getGrupoUsuario());		
		novaDistribuicao.setDataDistribuicao(distribuicao.getDataDistribuicao());
		novaDistribuicao.setObjetoIncidente(processoSetor.getObjetoIncidente());
		
		return novaDistribuicao;
	}
	
	private void validarDistribuicaoAutomatica(List<ProcessoSetor> listaProcessoSetor, 
			List<UsuarioDistribuicao> listaUsuarios) 
	throws ServiceException {
		
		if( listaProcessoSetor == null || listaProcessoSetor.size() == 0 ){
			throw new ServiceException("Os processos/protocolos devem ser informados.");
		} 
		else if(listaUsuarios==null||listaUsuarios.size()==0){
			throw new ServiceException("Os usuários devem ser informados.");
		}
		
		Boolean distribuicaoNaoLocalizadoSetor = 
			listaProcessoSetor.get(0).getSetor().contemConfiguracao(TipoConfiguracaoSetor.REGISTRAR_DISTRIBUICAO_PARA_NAO_LOCALIZADO_SETOR);
		if( listaProcessoSetor != null && listaProcessoSetor.size() > 0 && 
				(distribuicaoNaoLocalizadoSetor == null || !distribuicaoNaoLocalizadoSetor)){
			StringBuffer mensagem = new StringBuffer();
		
			int contador = 0;
			for(ProcessoSetor processoSetor : listaProcessoSetor){
				if(processoSetor.getDataSaida()!=null){
					contador++;
					if(contador==1)
						mensagem.append("Os seguintes processos/protocolos não estão no setor do usuário: \n");
	                
	                mensagem.append(processoSetor.getIdentificacao()+" \n");
				}
			}
			if(contador>0){
				throw new ServiceException(mensagem.toString());
			}
		}	
	}
	
	private void validarDistribuicao(HistoricoDistribuicao distribuicao) 
	throws ServiceException {
		if( distribuicao == null )
			throw new NullPointerException("Objeto que representa a distribuição é nulo.");
		
		if( distribuicao.getProcessoSetor() == null )
			throw new ServiceException("A distribuição precisa estar associada a um processo/protocolo.");
		
		Boolean distribuicaoNaoLocalizadoSetor = 
			distribuicao.getProcessoSetor().getSetor().contemConfiguracao(TipoConfiguracaoSetor.REGISTRAR_DISTRIBUICAO_PARA_NAO_LOCALIZADO_SETOR);
		
		if( distribuicao.getProcessoSetor().getDataSaida() != null && ( distribuicaoNaoLocalizadoSetor == null || !distribuicaoNaoLocalizadoSetor) )
			throw new ServiceException("O processo/protocolo ao qual a distribuição está associada não mais se encontra no setor.");
		
		if( distribuicao.getUsuario() == null )
			throw new ServiceException("O usuário da distribuição não foi informado.");		
		
		if( distribuicao.getDataDistribuicao() == null )
			throw new ServiceException("A data da distribuição não foi informada.");		
        
        if( distribuicao.getProcessoSetor().getDistribuicaoAtual() != null && 
                distribuicao.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId().equals(
                        distribuicao.getUsuario().getId()) )
            throw new ServiceException("Não é permitido redistribuir um processo/protocolo para o mesmo usuário.");
	}		
	
	@SuppressWarnings("unchecked")
	private long getMenorCarga(List<UsuarioDistribuicao> listaUsuarios,ProcessoSetor processoSetor) {
		long menorCarga = Long.MAX_VALUE;
		
		for( Iterator it = listaUsuarios.iterator(); it.hasNext(); ) {
			
			UsuarioDistribuicao usuario = (UsuarioDistribuicao) it.next();
			
			if( processoSetor.getDistribuicaoAtual() != null &&
				    processoSetor.getDistribuicaoAtual().getUsuario() != null &&
				    processoSetor.getDistribuicaoAtual().getUsuario().getId().equals(usuario.getUsuario().getId())){			
				continue;
			}
		
			long cargaUsuario = usuario.getCargaVirtual();
			
			if( cargaUsuario < menorCarga )
				menorCarga = cargaUsuario;
		}		
		
		return menorCarga;
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessoSetor> ordenarProcessosPorClasseNumeroRecurso(List<ProcessoSetor> listaProcessoSetor){
	    Collections.sort(listaProcessoSetor, new ClasseComparator());
	    Collections.sort(listaProcessoSetor, new ClasseNumeroComparator());
	    Collections.sort(listaProcessoSetor, new ClasseNumeroRecursoComparator());
	    return listaProcessoSetor;
	}
	
	
	@SuppressWarnings("unchecked")
	private static class ClasseComparator implements Comparator {

	    public int compare(Object obj1, Object obj2) {

	        if( !(obj1 instanceof HistoricoDistribuicao) || !(obj2 instanceof HistoricoDistribuicao) )
	            return 0;

	        HistoricoDistribuicao doc1 = (HistoricoDistribuicao) obj1;
	        HistoricoDistribuicao doc2 = (HistoricoDistribuicao) obj2;

	        if( doc1 == null || doc2 == null || doc1.getProcessoSetor() == null || doc2.getProcessoSetor() == null || 
	        		doc1.getProcessoSetor().getProcesso() == null || doc2.getProcessoSetor().getProcesso() == null )
	            return 0;

	        return doc1.getProcessoSetor().getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcessoSetor().getProcesso().getClasseProcessual().getId());

	    }
	}

	@SuppressWarnings("unchecked")
	private static class ClasseNumeroComparator implements Comparator {

	    public int compare(Object obj1, Object obj2) {

	        if( !(obj1 instanceof ProcessoSetor) || !(obj2 instanceof ProcessoSetor) )
	            return 0;

	        ProcessoSetor doc1 = (ProcessoSetor) obj1;
	        ProcessoSetor doc2 = (ProcessoSetor) obj2;

	        if( doc1 == null || doc2 == null ||	doc1.getProcesso() == null || doc2.getProcesso() == null )
	            return 0;

	        if(doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId())==0){
	            return doc1.getProcesso().getNumeroProcessual().compareTo(doc2.getProcesso().getNumeroProcessual());
	        }else{
	            return doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId());
	        }
	    }
	}


	@SuppressWarnings("unchecked")
	private static class ClasseNumeroRecursoComparator implements Comparator {

	    public int compare(Object obj1, Object obj2) {

	        if( !(obj1 instanceof ProcessoSetor) || !(obj2 instanceof ProcessoSetor) )
	            return 0;

	        ProcessoSetor doc1 = (ProcessoSetor) obj1;
	        ProcessoSetor doc2 = (ProcessoSetor) obj2;

	        if( doc1 == null || doc2 == null ||	doc1.getProcesso() == null || doc2.getProcesso() == null )
	            return 0;


	        if(doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId()) == 0){
	            if(doc1.getProcesso().getNumeroProcessual().compareTo(doc2.getProcesso().getNumeroProcessual()) == 0){
                	return ProcessoSetor.getCodigoRecursoProcesso(doc1.getObjetoIncidente()).compareTo(ProcessoSetor.getCodigoRecursoProcesso(doc2.getObjetoIncidente()));
	            } else {
	            	return doc1.getProcesso().getNumeroProcessual().compareTo(doc2.getProcesso().getNumeroProcessual());
	            }	
	        }else{
	            return doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId());
	        }
	    }
	}
	
	// ##################### DISTRIBUICAO POR CLASSE E TIPO DE JULGAMENTO ##################### //
	
	/**
	 * método resposanvel por distribuir processos por classe e tipo de julgamento
	 * @author GuilhermeA
	 */
	public List<UsuarioDistribuicaoClasseTipoJulgamento> previaDistribuicaoProcessoSetorTipoJulgamento(List<HistoricoDistribuicao> listaProcesso, 
			List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuarioDistribuicao) throws ServiceException {

//		if( listaUsuarioDistribuicao == null || listaUsuarioDistribuicao.size() == 0 )
//			throw new RegraDeNegocioException("Os usuários devem informados.");
		
		for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){
			for( ControleDistribuicao controle : usuario.getControles() ){
				controle.setQtdDebitoAnterior(controle.getQtdDebito());
				if( controle.getDistribuicao() == null )
					controle.setDistribuicao(0);
				if( controle.getDistribuido() == null )
					controle.setDistribuido(0);
			}
		}
		
		//Ordena os processos por classe e tipo de julgamento
		if( listaProcesso != null && listaProcesso.size() > 0 ){
			ordenarProcessosPorClasseTipoJulgamentoNumero(listaProcesso);
		}

		// recuperar as classes e tipo de julgamento q irão participar da distribuição
		List<ControleDistribuicao> listaMapaClasseTipoJulgamento = recuperarMapasClasseTipoJulgamento(listaProcesso,listaUsuarioDistribuicao.get(0));

		// instancia a classe que irá fazer o sorteio
		Random random = new Random(new Date().getTime());

		List<HistoricoDistribuicao> listaProcessoMerito = new LinkedList<HistoricoDistribuicao>();
		
		Boolean possuiConfiguracaoSetor = listaUsuarioDistribuicao.get(0).getUsuarioEGab().getSetor().
		contemConfiguracao(TipoConfiguracaoSetor.NAO_DECREMENTAR_MERITO_QUANDO_HOUVER_MEDIDA_CAUTELA);
		
		if( possuiConfiguracaoSetor != null && possuiConfiguracaoSetor ){
			listaProcessoMerito = recuperarProcessoMerito(listaProcesso);
			listaProcesso.removeAll(listaProcessoMerito);
		}

		// varre a lista de classe e tipo de julgamento que serão distribuidos
		for( ControleDistribuicao controleClasseTpJulg : listaMapaClasseTipoJulgamento ){
			
			if( listaProcesso != null && listaProcesso.size() > 0 ){
				//recuperar os processos que tem a classe e tipo de julgamento do mapaDistribuicao atual
				List<HistoricoDistribuicao> listaProcessoParticipante = 
					recuperarProcessoParticipantePorClasseTipoJulgamento(controleClasseTpJulg, listaProcesso); 
				
				List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuarioParticipante = null;
				
				// varre os processo participantes
				for( HistoricoDistribuicao processo : listaProcessoParticipante ){

					// Recupera os usuários que tem maior pendência para a classe tipo de julgamento do mapaDistribuicao atual.
					listaUsuarioParticipante = recuperarUsuarioComMaiorPendencia(controleClasseTpJulg,listaUsuarioDistribuicao);
										
					// sorteia os usuários que têm a carga de pendência igual
					int sorteio = random.nextInt(listaUsuarioParticipante.size());
					UsuarioDistribuicaoClasseTipoJulgamento usuarioSorteado = listaUsuarioParticipante.get(sorteio);
					
					// Caso a distribuição atual do usuário selecionado for igual ao do processo q esta sendo distribuido,
					// será sorteado outro usuario.
					if( processo.getProcessoSetor().getDistribuicaoAtual() != null && processo.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId().
							equals(usuarioSorteado.getUsuarioEGab().getUsuario().getId()) ){
						
						List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuario = new LinkedList<UsuarioDistribuicaoClasseTipoJulgamento>();
						
						if( listaUsuarioParticipante.size() > 1 || listaUsuarioDistribuicao.size() > 1){
							if( listaUsuarioParticipante.size() == 1 ){
								listaUsuarioParticipante = listaUsuarioDistribuicao;
							}
							for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioParticipante ){
								if( !usuario.getUsuarioEGab().getUsuario().getId().
										equals(usuarioSorteado.getUsuarioEGab().getUsuario().getId()) ){
									listaUsuario.add(usuario);
								}
							}
							
							if( listaUsuario.size() > 0 ){
								sorteio = random.nextInt(listaUsuario.size());
								usuarioSorteado = listaUsuario.get(sorteio);
							}
							
						}
						
					}
					
					// Para o usuario que recebeu o processo irá haver um decrécimo do debido 
					// quando o valor devedor for maior que zero
					boolean debitoZero = false;
					for( ControleDistribuicao controle :  usuarioSorteado.getControles() ){
						if( controle.getIndentificacaoClasseTipoJulgamento().
								equals(controleClasseTpJulg.getIndentificacaoClasseTipoJulgamento()) ){
							if( controle.getQtdDebito() > 0 ){
								controle.setQtdDebito( controle.getQtdDebito() - 1);
							}else if( controle.getQtdDebito() == 0 ){
								controle.setDistribuicao(controle.getDistribuicao() + 1);
								debitoZero = true;
							}	
							
							if( controle.getProcessosDistribuidos() == null )
								controle.setProcessosDistribuidos(new LinkedList<HistoricoDistribuicao>());
							controle.getProcessosDistribuidos().add(processo);
							break;
						}
					}
					
					
					
					if( debitoZero ){
						for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioParticipante ){
								if( !usuario.getUsuarioEGab().getUsuario().getId().
										equals(usuarioSorteado.getUsuarioEGab().getUsuario().getId())){
									for( ControleDistribuicao controle : usuario.getControles() ){
										if( controle.getIndentificacaoClasseTipoJulgamento().
												equals(controleClasseTpJulg.getIndentificacaoClasseTipoJulgamento()) ){
											controle.setQtdDebito(controle.getQtdDebito() + 1);
										}
									}
								}
						}
						
						for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){
							if( !usuario.getParticipante() && usuario.getCompensar() ){
								if( !usuario.getUsuarioEGab().getUsuario().getId().
										equals(usuarioSorteado.getUsuarioEGab().getUsuario().getId())){
									for( ControleDistribuicao controle : usuario.getControles() ){
										if( controle.getIndentificacaoClasseTipoJulgamento().
												equals(controleClasseTpJulg.getIndentificacaoClasseTipoJulgamento()) ){
											controle.setQtdDebito(controle.getQtdDebito() + 1);
										}
									}
								}
							}
						}
					}
					
					//colocar debito para o usuario que esta com o processo antes da distribuição
					if( processo.getProcessoSetor().getDistribuicaoAtual() != null && 
							!usuarioSorteado.getUsuarioEGab().getUsuario().getId().
							equals(processo.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId()) ){
						for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){
							if( usuario.getUsuarioEGab().getUsuario().getId().
									equals(processo.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId()) ){
								for( ControleDistribuicao controle : usuario.getControles() ){
									if( controleClasseTpJulg.getIndentificacaoClasseTipoJulgamento().
											equals(controle.getIndentificacaoClasseTipoJulgamento()) ){
										controle.setQtdDebito(controle.getQtdDebito() + 1);
									}
								}
							}
						}
					}
					
				}
			}
			
       }
	   
	   if( possuiConfiguracaoSetor != null && possuiConfiguracaoSetor && listaProcessoMerito != null && listaProcessoMerito.size() > 0 ){
		   
		   for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){
			   for(ControleDistribuicao controle : usuario.getControles() ){
				   if( controle.getTipoJulgamento().equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MEDIDA_CAUTELAR.getSigla()) ){
					   if( controle.getDistribuido() > 0 ){
						   for( HistoricoDistribuicao hist : controle.getProcessosDistribuidos() ){
							   for( HistoricoDistribuicao histMerito : listaProcessoMerito ){
								   if( hist.getProcessoSetor().getProcesso().getClasseProcessual().getId().
											equals(histMerito.getProcessoSetor().getProcesso().getClasseProcessual().getId()) &&
											hist.getProcessoSetor().getProcesso().getNumeroProcessual().equals(histMerito.getProcessoSetor().getProcesso().getNumeroProcessual())){
									   for( ControleDistribuicao controleMerito : usuario.getControles()  ){
										   
										   if( controleMerito.getSiglaClasseProcessual().
												   equals(hist.getProcessoSetor().getProcesso().getClasseProcessual().getId()) &&
												   controleMerito.getTipoJulgamento().equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla())){
											   if( controleMerito.getProcessosDistribuidos() == null )
												   controleMerito.setProcessosDistribuidos(new LinkedList<HistoricoDistribuicao>());
											   controleMerito.getProcessosDistribuidos().add(histMerito);
										   }
									   }
								   }
							   }
						   }
					   }
				   }
			   }
		   }
		   caucularDebitoMerito(listaProcessoMerito, listaProcesso, listaUsuarioDistribuicao);
	   }
		
		return listaUsuarioDistribuicao;	
	}
	
	/**
	 * metodo responsavel por recuperar os processos que tenham a mesma identificação processual e que possua mérito e medidda cautelar 
	 * retornando somente o mérito.
	 * @param lista
	 * @return
	 */
	public List<HistoricoDistribuicao> recuperarProcessoMerito(List<HistoricoDistribuicao> lista){
		List<HistoricoDistribuicao> listaMerito = new LinkedList<HistoricoDistribuicao>();
		for( HistoricoDistribuicao historico : lista ){
			if( ProcessoSetor.getSiglaTipoJulgamento(historico.getProcessoSetor().getObjetoIncidente()).
					equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MEDIDA_CAUTELAR.getSigla()) ){
				for( HistoricoDistribuicao historicoMerito : lista ){
					if( ProcessoSetor.getSiglaTipoJulgamento(historicoMerito.getProcessoSetor().getObjetoIncidente()).
							equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla()) ){
						if( historico.getProcessoSetor().getProcesso().getClasseProcessual().getId().
								equals(historicoMerito.getProcessoSetor().getProcesso().getClasseProcessual().getId()) &&
								historico.getProcessoSetor().getProcesso().getNumeroProcessual().
								equals(historicoMerito.getProcessoSetor().getProcesso().getNumeroProcessual())){
							listaMerito.add(historicoMerito);
						}
					}
				}
			}
			
		}
		return listaMerito;
	}
	
	/**
	 * Calcula o debito do usuário para a distribuição manual
	 * @author GuilhermeA
	 */
	public List<UsuarioDistribuicaoClasseTipoJulgamento> calcularDebitoDistribuicaoManual(List<UsuarioDistribuicaoClasseTipoJulgamento> 
	listaUsuarioDistribuicao)
	throws ServiceException {

		if( listaUsuarioDistribuicao == null || listaUsuarioDistribuicao.size() == 0 )
			throw new ServiceException("Os usuários devem informados.");

		List<HistoricoDistribuicao> listaProcesso = new LinkedList<HistoricoDistribuicao>();
		for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){
			for( ControleDistribuicao controle : usuario.getControles() ){
				controle.setQtdDebitoAnterior(controle.getQtdDebito());
				if( controle.getDistribuicao() == null )
					controle.setDistribuicao(0);
				if( controle.getDistribuido() == null ){
					controle.setDistribuido(0);
				}else if( controle.getDistribuido() > 0 ){
					listaProcesso.addAll(controle.getProcessosDistribuidos());
				}

			}
		}

		//Ordena os processos por classe e tipo de juglamento
		if( listaProcesso != null && listaProcesso.size() > 0 ){
			ordenarProcessosPorClasseTipoJulgamentoNumero(listaProcesso);
		}

		Boolean possuiConfiguracaoSetor = listaUsuarioDistribuicao.get(0).getUsuarioEGab().getSetor().
		contemConfiguracao(TipoConfiguracaoSetor.NAO_DECREMENTAR_MERITO_QUANDO_HOUVER_MEDIDA_CAUTELA);
		
		List<HistoricoDistribuicao> listaProcessoMerito = null;
		if( possuiConfiguracaoSetor != null && possuiConfiguracaoSetor ){
			listaProcessoMerito = recuperarProcessoMeritoManual(listaProcesso,listaUsuarioDistribuicao);
			listaProcesso.removeAll(listaProcessoMerito);
		}

		if( listaProcesso != null && listaProcesso.size() > 0 ){
			 

			// varre os processos participantes
			for( HistoricoDistribuicao processo : listaProcesso ){
				
				UsuarioDistribuicaoClasseTipoJulgamento usuarioProcesso = null;
				ControleDistribuicao controleDistribuicao = null;
				for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){
					for( ControleDistribuicao controle : usuario.getControles() ){
						if( controle.getDistribuido() > 0 ){
							for( HistoricoDistribuicao hist : controle.getProcessosDistribuidos() ){
								if( hist.getProcessoSetor().getId().equals(processo.getProcessoSetor().getId()) ){
									usuarioProcesso = usuario;
									controleDistribuicao = controle;
									break;
								}
							}
						}
					}
				}
				
				
				// Para o usuario que recebeu o processo irá haver um decrécimo do debido 
				// quando o valor devedor for maior que zero
				boolean debitoZero = false;
				for( ControleDistribuicao controle :  usuarioProcesso.getControles() ){
					if( controle.getIndentificacaoClasseTipoJulgamento().
							equals(controleDistribuicao.getIndentificacaoClasseTipoJulgamento()) ){
						if( controle.getQtdDebito() > 0 ){
							controle.setQtdDebito( controle.getQtdDebito() - 1);
						}else if( controle.getQtdDebito() == 0 ){
							controle.setDistribuicao(controle.getDistribuicao() + 1);
							debitoZero = true;
						}	
						break;
					}
				}



				if( debitoZero ){
					for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){
							if( !usuario.getUsuarioEGab().getUsuario().getId().
									equals(usuarioProcesso.getUsuarioEGab().getUsuario().getId())){
								for( ControleDistribuicao controle : usuario.getControles() ){
									if( controle.getIndentificacaoClasseTipoJulgamento().
											equals(controleDistribuicao.getIndentificacaoClasseTipoJulgamento()) ){
										if( usuario.getParticipante() || usuario.getCompensar() )
											controle.setQtdDebito(controle.getQtdDebito() + 1);
									}
								}
							}
					}

					/*for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){

						if( !usuario.getUsuarioEGab().getUsuario().getSigla().
								equals(usuarioProcesso.getUsuarioEGab().getUsuario().getSigla())){
							for( ControleDistribuicao controle : usuario.getControles() ){
								if( controle.getIndentificacaoClasseTipoJulgamento().
										equals(controleDistribuicao.getIndentificacaoClasseTipoJulgamento()) ){

									if( usuario.getParticipante() || usuario.getCompensar() )
										controle.setQtdDebito(controle.getQtdDebito() + 1);
								}
							}
						}
					}*/
				}

				//colocar debito para o usuario que esta com o processo antes da distribuição
				if( processo.getProcessoSetor().getDistribuicaoAtual() != null && 
						!usuarioProcesso.getUsuarioEGab().getUsuario().getId().
						equals(processo.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId()) ){
					for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicao ){
						if( usuario.getUsuarioEGab().getUsuario().getId().
								equals(processo.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId()) ){
							for( ControleDistribuicao controle : usuario.getControles() ){
								if( controleDistribuicao.getIndentificacaoClasseTipoJulgamento().
										equals(controle.getIndentificacaoClasseTipoJulgamento()) ){
									controle.setQtdDebito(controle.getQtdDebito() + 1);
								}
							}
						}
					}
				}
			}
			//calcula o débito do merito
			if( possuiConfiguracaoSetor != null && possuiConfiguracaoSetor && listaProcessoMerito != null ){
				caucularDebitoMerito(listaProcessoMerito, listaProcesso, listaUsuarioDistribuicao);
			}
			
		}
		return listaUsuarioDistribuicao;	
	}
	
	/**
	 * Quando o merito e a medida cautelar do processo foi distribuida para pessoas diferentes e na distribuição atual
	 * os processos forem para a mesma pessoa e acrescido um debido para o usuario que estava com o merito.
	 * o debido da cautelar e calculado normamente no processo de distribuição
	 * @param listaProcessoMerito
	 * @param listaProcesso
	 * @param listaUsuarioDistribuicao
	 */
	private void caucularDebitoMerito(List<HistoricoDistribuicao> listaProcessoMerito,List<HistoricoDistribuicao> listaProcessoMC, 
			List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuarioDistribuicao ){
		List<HistoricoDistribuicao> listaProcesso = new LinkedList<HistoricoDistribuicao>();
		listaProcesso.addAll(listaProcessoMerito);
		listaProcesso.addAll(listaProcessoMC);

		if( listaProcesso != null && listaProcesso.size() > 0 ){
			for( HistoricoDistribuicao histMerito : listaProcesso ){
				if( ProcessoSetor.getSiglaTipoJulgamento(histMerito.getProcessoSetor().getObjetoIncidente()).equals
						(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla()) ){
					for( HistoricoDistribuicao histMC : listaProcesso ){
						if( ProcessoSetor.getSiglaTipoJulgamento(histMC.getProcessoSetor().getObjetoIncidente()).equals
								(ProcessoSetor.TipoIncidenteJulgamentoConstante.MEDIDA_CAUTELAR.getSigla()) ){
							if( histMC.getProcessoSetor().getProcesso().getClasseProcessual().getId().
									equals(histMerito.getProcessoSetor().getProcesso().getClasseProcessual().getId()) &&
									histMC.getProcessoSetor().getProcesso().getNumeroProcessual().
									equals(histMerito.getProcessoSetor().getProcesso().getNumeroProcessual())){

								UsuarioDistribuicaoClasseTipoJulgamento usuarioMerito = 
									recuperarUsuarioDistribuicao(histMerito,listaUsuarioDistribuicao,null);
								UsuarioDistribuicaoClasseTipoJulgamento usuarioMC =  
									recuperarUsuarioDistribuicao(histMC,listaUsuarioDistribuicao,null);

								if( usuarioMerito.getUsuarioEGab().getUsuario().getId().
										equals(usuarioMC.getUsuarioEGab().getUsuario().getId()) ){

									if( histMerito.getProcessoSetor().getDistribuicaoAtual() != null ){

										boolean debita = false;
										if( histMC.getProcessoSetor().getDistribuicaoAtual() != null &&
												!histMerito.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId().
												equals(histMC.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId()) ){
											debita = true;
										}

										if( !debita ){
											if( histMerito.getProcessoSetor().getDistribuicaoAtual() != null &&
													histMC.getProcessoSetor().getDistribuicaoAtual() == null ){
												debita = true;
											}
										}

										if( debita ){

											UsuarioDistribuicaoClasseTipoJulgamento usuarioDebito =  
												recuperarUsuarioDistribuicao(null,listaUsuarioDistribuicao,
														histMerito.getProcessoSetor().getDistribuicaoAtual().getUsuario().getId());

											if( usuarioDebito != null ){
												for( ControleDistribuicao controle : usuarioDebito.getControles() ){
													if( controle.getSiglaClasseProcessual().equals
															(histMerito.getProcessoSetor().getProcesso().getSiglaClasseProcessual()) &&
															controle.getTipoJulgamento().equals
															(ProcessoSetor.getSiglaTipoJulgamento(histMerito.getProcessoSetor().getObjetoIncidente()))){

														controle.setQtdDebito(controle.getQtdDebito() + 1);
													}

												}
											}
										}


									}

								}
							}
						}
					}
				}
			}	
		}
	}
	
	
	private UsuarioDistribuicaoClasseTipoJulgamento recuperarUsuarioDistribuicao(HistoricoDistribuicao hist, 
			List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuario, String siglaUsuario){
		
		for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuario ){
			if( siglaUsuario != null && siglaUsuario.trim().length() > 0 ){
				if( usuario.getUsuarioEGab().getUsuario().getId().equals(siglaUsuario) ){
					return usuario;
				}
			}else{
				for(ControleDistribuicao controle : usuario.getControles()){
					if( controle.getDistribuido() > 0 ){
							for( HistoricoDistribuicao histDist : controle.getProcessosDistribuidos() ){
								if( histDist.getProcessoSetor().getId().equals(hist.getProcessoSetor().getId()) ){
									return usuario;
								}
							}
					}
				}
			}
			
		}
		return null;
	}
	
	
	/**
	 * metodo responsavel por recuperar os processo com merito.
	 * - se o merito e a medida cautela de uma mesma indentificação processual esta com o mesmo usuario, 
	 * o processo com o mérito será adicionado a lista. 
	 * @return
	 */
	public List<HistoricoDistribuicao> recuperarProcessoMeritoManual(List<HistoricoDistribuicao> lista, List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuario){
		
		List<HistoricoDistribuicao> listaMerito =  new LinkedList<HistoricoDistribuicao>();
		for( HistoricoDistribuicao hist : lista ){
			if( ProcessoSetor.getSiglaTipoJulgamento(hist.getProcessoSetor().getObjetoIncidente()).equals
					(ProcessoSetor.TipoIncidenteJulgamentoConstante.MEDIDA_CAUTELAR.getSigla()) ){
				for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuario ){
					for( ControleDistribuicao controle : usuario.getControles() ){
						if( controle.getDistribuido() > 0 ){
							boolean possuiMedidaCautelar = false;
							if( controle.getSiglaClasseProcessual().equals(hist.getProcessoSetor().getProcesso().getSiglaClasseProcessual()) &&
									controle.getTipoJulgamento().equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MEDIDA_CAUTELAR.getSigla())){
								for( HistoricoDistribuicao histCautelar : controle.getProcessosDistribuidos() ){
									if( hist.getProcessoSetor().getId().equals(histCautelar.getProcessoSetor().getId()) ){
										possuiMedidaCautelar = true;
									}
								}
							}
							
							if( possuiMedidaCautelar ){
								for( ControleDistribuicao controleMerito : usuario.getControles() ){
									if( controleMerito.getDistribuido() > 0  ){
										if( controleMerito.getSiglaClasseProcessual().equals(hist.getProcessoSetor().getProcesso().getSiglaClasseProcessual()) &&
												controleMerito.getTipoJulgamento().equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla())){
											for( HistoricoDistribuicao histMerito : controleMerito.getProcessosDistribuidos() ){
												if( hist.getProcessoSetor().getProcesso().getSiglaClasseProcessual().
														equals(histMerito.getProcessoSetor().getProcesso().getSiglaClasseProcessual()) && 
														hist.getProcessoSetor().getProcesso().getNumeroProcessual().equals
														(histMerito.getProcessoSetor().getProcesso().getNumeroProcessual())){
													listaMerito.add(histMerito);
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return listaMerito;
		
	}
	


	

	
	/**
	 * Metodo responsavel por recuperar os usuários q tem maior pendencia na classe e tipo de julgamento informado no mapaClasseTipoJulgamento
	 * @param mapaClasseTipoJulgamento
	 * @param listaUsuario
	 * @return
	 */
	public List<UsuarioDistribuicaoClasseTipoJulgamento> recuperarUsuarioComMaiorPendencia(ControleDistribuicao mapaClasseTipoJulgamento, 
			List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuario){
		
		 List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuarioMaiorCarga = new LinkedList<UsuarioDistribuicaoClasseTipoJulgamento>();
		 Integer debitoMaior = null; 
		for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuario ){
			
			if( usuario.getParticipante() ){
				for(ControleDistribuicao mapa : usuario.getControles() ){
					if( mapa.getIndentificacaoClasseTipoJulgamento().equals(mapaClasseTipoJulgamento.getIndentificacaoClasseTipoJulgamento())){
							if( debitoMaior == null ){
								debitoMaior = mapa.getQtdDebito();
								listaUsuarioMaiorCarga.add(usuario);
							}else{
								if( mapa.getQtdDebito().intValue() > debitoMaior.intValue() ){
									listaUsuarioMaiorCarga = new LinkedList<UsuarioDistribuicaoClasseTipoJulgamento>();
									listaUsuarioMaiorCarga.add(usuario);
									debitoMaior = mapa.getQtdDebito();
								}else if( mapa.getQtdDebito().intValue() == debitoMaior.intValue() ){
									listaUsuarioMaiorCarga.add(usuario);
								}
							}
						break;
					}
				}
			}
		}
		return listaUsuarioMaiorCarga;
		
	}
	
	/**
	 * metodo resposnavel por recuperar os processos por classe e tipo de julgamento q irão participar da distribuicao.
	 * @param mapaClasseTipoJulgamento
	 * @param listaProcesso
	 * @return
	 * @author GuilhermeA
	 */
	public List<HistoricoDistribuicao> recuperarProcessoParticipantePorClasseTipoJulgamento(
			ControleDistribuicao mapaClasseTipoJulgamento,List<HistoricoDistribuicao> listaProcesso ){
		
		List<HistoricoDistribuicao> listaParticipante = new LinkedList<HistoricoDistribuicao>();
		for( HistoricoDistribuicao processo : listaProcesso ){
			if( processo.getProcessoSetor().getProcesso().getClasseProcessual().getId().equals(mapaClasseTipoJulgamento.getSiglaClasseProcessual()) &&
					ProcessoSetor.getSiglaTipoJulgamento(processo.getProcessoSetor().getObjetoIncidente()).equals(mapaClasseTipoJulgamento.getTipoJulgamento()) ){
				listaParticipante.add(processo);
			}
		}
		
		return listaParticipante;
		
	}
	
	public Boolean confirmarDistribuicaoProcessoSetorClasseTipoJulgamento(
			List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuarioDistribuicaoClasseTipoJulgamento)throws 
			ServiceException {
		      try{
		    	  if( listaUsuarioDistribuicaoClasseTipoJulgamento == null || listaUsuarioDistribuicaoClasseTipoJulgamento.size() == 0  ){
		    		  throw new ServiceException("A lista de usuário distribuicao deve ser informada.");
		    	  }
		    	  
		    	  for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicaoClasseTipoJulgamento ){
		    		   if( usuario.getControles() == null || usuario.getControles().size() == 0 ){
		    			   throw new ServiceException("O mapa distribuicao dos usuários devem ser informados.");
		    		   }
		    	  }
		    	  boolean confirmado = false;
		    	  for( UsuarioDistribuicaoClasseTipoJulgamento usuario : listaUsuarioDistribuicaoClasseTipoJulgamento ){
		    		  for( ControleDistribuicao mapa : usuario.getControles() ){
		    			  
		    			  confirmado= dao.persistirMapaDistribuicao(mapa);
		    			  
		    			  for( HistoricoDistribuicao hist : mapa.getProcessosDistribuidos()){
		    				  
		    				  hist.setGrupoUsuario(mapa.getGrupoUsuario());
		    				  hist.setUsuario(mapa.getUsuario());
		    				  hist.setDataDistribuicao(Calendar.getInstance().getTime());
		    				  confirmado = distribuirProcessoSetor(hist);
		    		          
		    			  }
		    		  }
		    	  }
		    	  
		    	  return confirmado;
		    	  
		    	  
		    	  
		    	  
		    	  
		      }catch(ServiceException e){
		    	  e.printStackTrace();
		    	  throw e;
		      }catch(DaoException e){
		    	  e.printStackTrace();
		    	  throw new ServiceException(e);
		      }
			  
	}
	
	/**
	 * metodo reponsavel por recuperar as classes e tipo de julgamento q irão participar da distribuição
	 * @return
	 * @author GuilhermeA
	 */
	@SuppressWarnings("unchecked")
	private List<ControleDistribuicao> recuperarMapasClasseTipoJulgamento( List<HistoricoDistribuicao> listaProcesso ,
			UsuarioDistribuicaoClasseTipoJulgamento usuario){
		
		List<ControleDistribuicao> listaMapa = new LinkedList<ControleDistribuicao>();
		if( listaProcesso != null && listaProcesso.size() > 0 ){
			for( HistoricoDistribuicao processo : listaProcesso  ){
				boolean contem = false;
				for(ControleDistribuicao mapa : listaMapa ){

					if( processo.getProcessoSetor().getProcesso().getClasseProcessual() != null && 
							ProcessoSetor.getSiglaTipoJulgamento(processo.getProcessoSetor().getObjetoIncidente()) != null &&
							processo.getProcessoSetor().getProcesso().getClasseProcessual().getId().equals(mapa.getSiglaClasseProcessual()) && 
							ProcessoSetor.getSiglaTipoJulgamento(processo.getProcessoSetor().getObjetoIncidente()).equals(mapa.getTipoJulgamento()) ) {
						contem = true;
					}

				}

				if( !contem ){
					ControleDistribuicao mapaColuna = new ControleDistribuicao();
					mapaColuna.setSiglaClasseProcessual(processo.getProcessoSetor().getProcesso().getClasseProcessual().getId());
					mapaColuna.setTipoJulgamento(ProcessoSetor.getSiglaTipoJulgamento(processo.getProcessoSetor().getObjetoIncidente()));
					listaMapa.add(mapaColuna);
				}

			}
		}else if( usuario != null ){
			for( ControleDistribuicao controle : usuario.getControles() ){
				ControleDistribuicao mapaColuna = new ControleDistribuicao();
				mapaColuna.setSiglaClasseProcessual(controle.getSiglaClasseProcessual());
				mapaColuna.setTipoJulgamento(controle.getTipoJulgamento());
				listaMapa.add(mapaColuna);
			}
		}
		
		Collections.sort(listaMapa, new MapaDistribuicaoComparator());
		
		return listaMapa;
	}
	
	
	@SuppressWarnings("unchecked")
	public static class MapaDistribuicaoComparator implements Comparator {

		public int compare(Object obj1, Object obj2) {

			if( !(obj1 instanceof ControleDistribuicao) || !(obj2 instanceof ControleDistribuicao) )
				return 0;
			ControleDistribuicao mapa1 = (ControleDistribuicao) obj1;
			ControleDistribuicao mapa2 = (ControleDistribuicao) obj2;
			
			if( mapa1 == null || mapa2 == null )
	            return 0;

			return mapa1.getIndentificacaoClasseTipoJulgamento().compareTo(mapa2.getIndentificacaoClasseTipoJulgamento());
		}

	}
	
	
	
	/**
	 * metodo reponsavel por ordernar os processo pro classe, tipo de julgamento e numero do processo
	 * @param listaProcessoSetor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void ordenarProcessosPorClasseTipoJulgamentoNumero( List<HistoricoDistribuicao> listaProcessoSetor) {
		Collections.sort( listaProcessoSetor, new ClasseComparator() );		
		Collections.sort( listaProcessoSetor, new ClasseTipoJulgamentoComparator() );
		Collections.sort( listaProcessoSetor, new ClasseTipoJulgamentoNumeroComparator() );
	}
	
	@SuppressWarnings("unchecked")
	private static class ClasseTipoJulgamentoComparator implements Comparator {
		public int compare(Object obj1, Object obj2) {
			if( !(obj1 instanceof ProcessoSetor) || !(obj2 instanceof ProcessoSetor) )
				return 0;
			
			ProcessoSetor doc1 = (ProcessoSetor) obj1;
			ProcessoSetor doc2 = (ProcessoSetor) obj2;
			
			if( doc1 == null || doc2 == null || doc1.getProcesso() == null || doc2.getProcesso() == null)
	            return 0;
			
			if( doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId()) == 0 ) {
				return ProcessoSetor.getSiglaTipoJulgamento(doc1.getObjetoIncidente()).compareTo(ProcessoSetor.getSiglaTipoJulgamento(doc2.getObjetoIncidente()));
			} else {
				return doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId());
			}
			
		}
	}

	@SuppressWarnings("unchecked")
	private static class ClasseTipoJulgamentoNumeroComparator implements Comparator {
		public int compare(Object obj1, Object obj2) {
			if( !(obj1 instanceof ProcessoSetor) || !(obj2 instanceof ProcessoSetor) )
				return 0;
			
			ProcessoSetor doc1 = (ProcessoSetor) obj1;
			ProcessoSetor doc2 = (ProcessoSetor) obj2;
			
	        if( doc1 == null || doc2 == null || doc1.getProcesso() == null || doc2.getProcesso() == null)
	            return 0;
			
			if( doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId()) == 0 ) {
				if( ProcessoSetor.getSiglaTipoJulgamento(doc1.getObjetoIncidente()).
						compareTo(ProcessoSetor.getSiglaTipoJulgamento(doc2.getObjetoIncidente())) == 0 ) {
					return doc1.getProcesso().getNumeroProcessual().compareTo(doc2.getProcesso().getNumeroProcessual());
				} else {
					return ProcessoSetor.getSiglaTipoJulgamento(doc1.getObjetoIncidente()).
						compareTo(ProcessoSetor.getSiglaTipoJulgamento(doc2.getObjetoIncidente()));
				}
			} else {
				return doc1.getProcesso().getClasseProcessual().getId().compareTo(doc2.getProcesso().getClasseProcessual().getId());
			}
		}
	}
	
	public Boolean cancelarDistribuicao(List<ProcessoSetor> processos,
			boolean atualizarControleDistribuicao ) throws ServiceException {
		boolean cancelado = false;
		try {
			if( processos == null || processos.size() == 0 )
				throw new ServiceException("Informe os processos que deseja cancelar.");
			
			Boolean possuiConfiguracaoSetor = processos.get(0).getSetor().
			contemConfiguracao(TipoConfiguracaoSetor.NAO_DECREMENTAR_MERITO_QUANDO_HOUVER_MEDIDA_CAUTELA);
			
			List<ProcessoSetor> listaProcessoMerito = null;
			if( possuiConfiguracaoSetor != null && possuiConfiguracaoSetor ){
				listaProcessoMerito = recuperarProcessoMeritoCancelamento(processos);
				processos.removeAll(listaProcessoMerito);
			}
			
			for( ProcessoSetor processo : processos ){
				
				if( processo.getDistribuicaoAtual() != null && processo.getDistribuicaoAtual().getUsuario() != null){
					
					if( atualizarControleDistribuicao ){
						ControleDistribuicao controle = recuperarControleDistribuicao(null, null, processo.getProcesso().getClasseProcessual().getId(), 
								ProcessoSetor.getSiglaTipoJulgamento(processo.getObjetoIncidente()), processo.getDistribuicaoAtual().getUsuario().getId());
						if( controle != null){
							controle.setQtdDebito(controle.getQtdDebito() + 1);
							cancelado = dao.persistirMapaDistribuicao(controle);
						}
					}
					
					processo.setDistribuicaoAtual(null);
					processoSetorService.persistirProcessoSetor(processo);
				}
			}
			
			if( possuiConfiguracaoSetor != null && possuiConfiguracaoSetor && listaProcessoMerito != null && listaProcessoMerito.size() > 0){
				
				for( ProcessoSetor processo : listaProcessoMerito ){
					
					if( processo.getDistribuicaoAtual() != null && processo.getDistribuicaoAtual().getUsuario() != null){
						
						processo.setDistribuicaoAtual(null);
						processoSetorService.persistirProcessoSetor(processo);
					}
				}
				
			}
			
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return cancelado;

	}
	
	public List<ProcessoSetor> recuperarProcessoMeritoCancelamento(List<ProcessoSetor> processos){
		List<ProcessoSetor> listaMerito = new LinkedList<ProcessoSetor>();
		for( ProcessoSetor processoMC : processos ){
			if( ProcessoSetor.getSiglaTipoJulgamento(processoMC.getObjetoIncidente()).equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MEDIDA_CAUTELAR.getSigla()) ){
				for( ProcessoSetor processoMerito : processos ){
					if( ProcessoSetor.getSiglaTipoJulgamento(processoMerito.getObjetoIncidente()).equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla()) ){
						if( processoMC.getProcesso().getClasseProcessual().getId().equals(processoMerito.getProcesso().getClasseProcessual().getId()) &&
								processoMC.getProcesso().getNumeroProcessual().equals(processoMerito.getProcesso().getNumeroProcessual())){
							
							if( processoMC.getDistribuicaoAtual() != null && processoMerito.getDistribuicaoAtual() != null &&
									processoMC.getDistribuicaoAtual().getUsuario().getId().
									equals(processoMerito.getDistribuicaoAtual().getUsuario().getId())){
								listaMerito.add(processoMerito);
							}
							
						}
					}
				}
			}
		}
		return listaMerito;
	}
	
	
	
	// ######################## CONTROLE DISTRIBUICAO ######################## //
	
	public List<ControleDistribuicao> pesquisarControleDistribuicao(Long id,
			Long idGrupoUsuario, String sigClasse, String tipoJuglamento,
			String sigUsuario) throws ServiceException {
		
		try {
			return dao.pesquisarControleDistribuicao(id, idGrupoUsuario, sigClasse, tipoJuglamento, sigUsuario);
		} catch (DaoException e) {
			throw new ServiceException(e);
			
		}
		
	}

	public ControleDistribuicao recuperarControleDistribuicao(Long id,
			Long idGrupoUsuario, String sigClasse, String tipoJuglamento,
			String sigUsuario) throws ServiceException {
		
		List<ControleDistribuicao> lista = pesquisarControleDistribuicao(id, idGrupoUsuario, sigClasse, tipoJuglamento, sigUsuario);
		
		if( lista != null && lista.size() == 1 ){
			return lista.get(0);
		}
		return null;
	}
	
	public Boolean excluirControleDistribuicao(
			ControleDistribuicao controleDistribuicao) throws ServiceException {
		try{
			return dao.excluirControleDistribuicao(controleDistribuicao);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
		
	}

	@Override
	public Long pesquisarGrupoDistribuicao(Processo processo) throws ServiceException {
		try {
			return dao.pesquisarGrupoDistribuicao(processo);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void subtrairMapaDistribuicao(Long quantidade, Long grupoDistribuicao, Ministro ministro) throws ServiceException {
		try {
			dao.subtrairMapaDistribuicao(quantidade, grupoDistribuicao, ministro);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}


}
