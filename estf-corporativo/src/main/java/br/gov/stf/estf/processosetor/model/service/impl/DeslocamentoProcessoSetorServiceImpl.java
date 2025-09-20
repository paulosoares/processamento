package br.gov.stf.estf.processosetor.model.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamento;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamentoPeticao;
import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.localizacao.model.service.SecaoSetorService;
import br.gov.stf.estf.processosetor.model.dataaccess.DeslocamentoProcessoSetorDao;
import br.gov.stf.estf.processosetor.model.service.DeslocamentoProcessoSetorService;
import br.gov.stf.estf.processosetor.model.service.PeticaoSetorService;
import br.gov.stf.estf.processosetor.model.service.ProcessoSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.security.AcegiSecurityUtils;
import br.gov.stf.framework.security.user.UserHolder;

@Service("deslocamentoService")
public class DeslocamentoProcessoSetorServiceImpl extends GenericServiceImpl<HistoricoDeslocamento, Long, DeslocamentoProcessoSetorDao>  
implements DeslocamentoProcessoSetorService {

	private final ProcessoSetorService processoSetorService;
	private final PeticaoSetorService peticaoSetorService;
	private final SecaoSetorService secaoSetorService;
	
	protected DeslocamentoProcessoSetorServiceImpl(DeslocamentoProcessoSetorDao dao,
			ProcessoSetorService processoSetorService, 
			PeticaoSetorService peticaoSetorService, 
			SecaoSetorService secaoSetorService) {
		super(dao);
		this.processoSetorService = processoSetorService; 
		this.peticaoSetorService = peticaoSetorService; 
		this.secaoSetorService = secaoSetorService;
	}

	

	public HistoricoDeslocamento recuperarDeslocamento(Long id)
	throws ServiceException {

		HistoricoDeslocamento deslocamentoSetor = null;

		try {
			deslocamentoSetor = dao.recuperarDeslocamento(id);
		}
		catch(DaoException e) {
			throw new ServiceException(e);
		}

		return deslocamentoSetor;
	}

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, ProcessoSetor processoSetor,
			PeticaoSetor peticaoSetor, Date dataRemessa, String numeroSala, String numeroEstante, 
			String numeroPrateleira, String numeroArmario, String numeroColuna) throws ServiceException{

		List<HistoricoDeslocamento> deslocamentos = null;

		try {
			deslocamentos = dao.pesquisarDeslocamentos(id, setor,
					processoSetor, peticaoSetor, dataRemessa, numeroSala, numeroEstante, numeroPrateleira,
					numeroArmario, numeroColuna);

			return deslocamentos;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor,String siglaClasseProcessual, Date dataRecebimento, Secao secaoOrigem, 
            Date dataRemessa, Secao secaoDestino, String numeroSala, String numeroEstante, String numeroPrateleira, 
            String numeroArmario, String numeroColuna, Boolean semLocalizacao, Date dataInicio, 
            Date dataFim,Boolean emTramite) throws ServiceException {
		List<HistoricoDeslocamento> deslocamentos = null;

		try {
			deslocamentos = dao.pesquisarDeslocamentos(id,setor,siglaClasseProcessual,dataRecebimento,secaoOrigem,dataRemessa,
					secaoDestino,numeroSala,numeroEstante,numeroPrateleira,numeroArmario, 
					numeroColuna, semLocalizacao, dataInicio,dataFim,emTramite);

			return deslocamentos;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, ProcessoSetor processoSetor, Date dataRecebimento, 
			Secao secaoOrigem, Date dataRemessa, Secao secaoDestino, String numeroSala, 
			String numeroEstante, String numeroPrateleira, String numeroArmario, 
			String numeroColuna, Boolean dataRecebimentoNula, Boolean localizadoSetor) throws ServiceException {
		List<HistoricoDeslocamento> deslocamentos = null;

		try {
			deslocamentos = dao.pesquisarDeslocamentos(id, setor,processoSetor,dataRecebimento, 
					secaoOrigem,dataRemessa,secaoDestino,numeroSala,numeroEstante,numeroPrateleira,numeroArmario, 
					numeroColuna, localizadoSetor);

			List<HistoricoDeslocamento> resultado = new LinkedList<HistoricoDeslocamento>();
			if(dataRecebimentoNula!=null){
				for(HistoricoDeslocamento deslocamento : deslocamentos){
					if(dataRecebimentoNula!=null){
						if(dataRecebimentoNula.booleanValue()){
							if( deslocamento.getDataRecebimento() == null ) {
								resultado.add(deslocamento);
							}
						}
						else if( deslocamento.getDataRecebimento() != null ) {
							resultado.add(deslocamento);
						}
					}
				}
			} else {
				resultado = deslocamentos;
			}

			return resultado;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	} 
	
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NOVA IMPLEMENTAÇÃO
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public Boolean registrarRemessa(List<ProcessoSetor> listaProcessoSetor, 
    		HistoricoDeslocamento historicoDeslocamento) 
    throws ServiceException {
        
        Boolean result = Boolean.FALSE; 
        
        if( listaProcessoSetor == null || listaProcessoSetor.size() == 0 ){
            throw new ServiceException("Os processos/protocolos devem ser informados.");
        } 
        else if( historicoDeslocamento == null ){
            throw new ServiceException("O deslocamento deve ser informado.");
        }

        for(ProcessoSetor processoSetor: listaProcessoSetor ) {
            if( !registrarRemessa(instanciarHistoricoDeslocamento(historicoDeslocamento, processoSetor) ) ) {
                
                StringBuffer error = new StringBuffer("Não foi possível registrar o deslocamento para o ");
                
                if( processoSetor.getPossuiIndentificacaoProcessual() )
                    error.append("processo: " + processoSetor.getIdentificacaoProcessual());
                else
                    error.append("protocolo: " + processoSetor.getIdentificacaoProtocolo());
                
                throw new ServiceException(error.toString());
            }
        }
        
        result = Boolean.TRUE;
        
        return result;
    }
    
    public Boolean registrarRemessaPeticao(List<PeticaoSetor> listaPeticaoSetor, 
			HistoricoDeslocamentoPeticao historicoDeslocamento) 
	throws ServiceException {

		Boolean result = Boolean.FALSE; 

		if(listaPeticaoSetor == null || listaPeticaoSetor.size() == 0) {
			throw new ServiceException("As Petições devem ser informadas.");
		} 
		else if(historicoDeslocamento == null) {
			throw new ServiceException("O deslocamento deve ser informado.");
		}

		for(PeticaoSetor peticaoSetor : listaPeticaoSetor) {
			if(!registrarRemessaPeticao(
					instanciarHistoricoDeslocamentoPeticao(historicoDeslocamento, peticaoSetor))) {

				String error = "Não foi possível registrar o deslocamento para a petição " + 
						peticaoSetor.getIdentificacaoPeticao();

				throw new ServiceException(error.toString());
			}
		}

		result = Boolean.TRUE;

		return result;
	}
    
    public Boolean registrarRemessa(HistoricoDeslocamento historicoDeslocamento) 
    throws ServiceException {
        
    	//peticaoSetorService = (IpeticaoSetorService) getServico("peticaoSetorService");
    	
        if( historicoDeslocamento == null )
            throw new NullPointerException("Deslocamento nulo ao remeter.");
        
        Boolean result = Boolean.FALSE;

        try {
            
            validarRemessa(historicoDeslocamento);
                        
            UsuarioEGab usuario = (UsuarioEGab) UserHolder.get();
            SecaoSetor secaoUsuario = secaoSetorService.recuperarSecaoSetor(null, usuario, null, 
            		usuario.getSetor().getId(), true, Boolean.TRUE);

            if( secaoUsuario == null || secaoUsuario.getSecao() == null || secaoUsuario.getSecao().getId() == null 
                    || secaoUsuario.getSetor() == null || secaoUsuario.getSetor().getId() == null )
                throw new ServiceException("Usuário não se encontra em alguma seção desse setor.");            
            
            boolean secaoOrigemDefinida = false;
            
            HistoricoDeslocamento deslocamentoAtual = null;
            
            //boolean deslocamentoProcesso = historicoDeslocamento.isHistoricoDeslocamentoProcessoSetor();
            
            // Verifica se há algum deslocamento atual
            //if(deslocamentoProcesso)
            	deslocamentoAtual = historicoDeslocamento.getProcessoSetor().getDeslocamentoAtual();
            /*else
            	deslocamentoAtual = historicoDeslocamento.getPeticaoSetor().getDeslocamentoAtual();
            */
            if( deslocamentoAtual != null ) {
                
                // Verifica se a data de recebimento é nula
                if( deslocamentoAtual.getDataRecebimento() == null ) {
                    
                    // Verifica se a seção de destino foi informada
                    if( deslocamentoAtual.getSecaoDestino() != null ) {
                    	String mensagemErro = "";
                    	
                    	//if(deslocamentoProcesso)
                    		mensagemErro = "O processo/protocolo '" +
                					historicoDeslocamento.getProcessoSetor().getIdentificacao() +
                					"' ainda não foi recebido no destino.";
                    	/*else
                    		mensagemErro = "A petição '" + 
                    				historicoDeslocamento.getPeticaoSetor().getIdentificacaoPeticao() +
                    				"' ainda não foi recebida no destino.";*/
                    		
                		throw new ServiceException(mensagemErro);
                    }
                }
                else {
                    if( deslocamentoAtual.getSecaoDestino() != null &&
                        deslocamentoAtual.getSecaoDestino().getId() != null ) {
                        
                        if( !(secaoUsuario.getSecao().getId().equals(deslocamentoAtual.getSecaoDestino().getId()) &&
                              secaoUsuario.getSetor().getId().equals(deslocamentoAtual.getSetor().getId()) ) ) {
                            
                            if( !usuario.getPossuiPerfilGestao().booleanValue() ) {
                                
                            	StringBuffer mensagemErro = new StringBuffer();
                            	
                            	//if(deslocamentoProcesso)
                            		mensagemErro.append("O processo/protocolo '" +
                        					historicoDeslocamento.getProcessoSetor().getIdentificacao());
                            	/*else
                            		mensagemErro.append("A petição '" + 
                            				historicoDeslocamento.getPeticaoSetor().getIdentificacaoPeticao());
                            	*/
                            	mensagemErro.append("' não se encontra na seção do usuário.");
                            	
                            	throw new ServiceException(mensagemErro.toString());                                
                            }
                            else {
                                historicoDeslocamento.setSecaoOrigem(deslocamentoAtual.getSecaoDestino());
                                historicoDeslocamento.setSetor(deslocamentoAtual.getSetor());    
                                
                                secaoOrigemDefinida = true;
                            }
                        }
                    }
                }
            }

            if( !secaoOrigemDefinida ) {
                historicoDeslocamento.setSecaoOrigem(secaoUsuario.getSecao());
                historicoDeslocamento.setSetor(secaoUsuario.getSetor());
            }
            
            historicoDeslocamento.setUsuarioOrigem(usuario.getUsuario());
            historicoDeslocamento.setDataRemessa(new Date());
            
            if( historicoDeslocamento.getSecaoDestino() == null ) {
                historicoDeslocamento.setDataRecebimento(historicoDeslocamento.getDataRemessa());
                historicoDeslocamento.setUsuarioDestino(null);
                
                //if(deslocamentoProcesso) {
                	if( !historicoDeslocamento.getProcessoSetor().adicionarHistoricoDeslocamento(
                			historicoDeslocamento) )
                		throw new ServiceException("Não foi possível adicionar um deslocamento para o processo/protocolo '" +
                				historicoDeslocamento.getProcessoSetor().getIdentificacao()+"'.");
                //}
                /*else {
                	if(!historicoDeslocamento.getPeticaoSetor().adicionarHistoricoDeslocamento(
                			historicoDeslocamento))
                		throw new ServiceException("Não foi possível adicionar um deslocamento para a petição '" +
                				historicoDeslocamento.getProcessoSetor().getIdentificacao() + "'.");
                }*/
            }
            else {
                SecaoSetor secaoSetorDestino = secaoSetorService.recuperarSecaoSetor(null, 
                    historicoDeslocamento.getSecaoDestino().getId(), historicoDeslocamento.getSetor().getId(), Boolean.TRUE);
            
                if( secaoSetorDestino.getParametro().getRecebimentoAutomatico().booleanValue() ) {
                    historicoDeslocamento.setDataRecebimento(historicoDeslocamento.getDataRemessa());
                    historicoDeslocamento.setUsuarioDestino(null);
                
                    //if(deslocamentoProcesso) {
                    	if( !historicoDeslocamento.getProcessoSetor().adicionarHistoricoDeslocamento(
                    			historicoDeslocamento) )
                    		throw new ServiceException("Não foi possível adicionar um deslocamento para o processo/protocolo '" +
                    				historicoDeslocamento.getProcessoSetor().getIdentificacao()+"'.");
                    //}
                    /*else {
                    	if(!historicoDeslocamento.getPeticaoSetor().adicionarHistoricoDeslocamento(
                    			historicoDeslocamento))
                    		throw new ServiceException("Não foi possível adicionar um deslocamento para a petição '" +
                    				historicoDeslocamento.getProcessoSetor().getIdentificacao() + "'.");
                    }*/
                    
                    if( secaoSetorDestino.getParametro().getRemessaAutomatica().booleanValue() ) {
 
                    	HistoricoDeslocamento deslocamentoRemessaAutomatica;
                    	
                    	// Força a inclusão do registro no banco de dados para que a ordem dos andamentos 
                    	// fique correta.
                    	//if(deslocamentoProcesso) {
                        	result = processoSetorService.persistirProcessoSetor(
                        			historicoDeslocamento.getProcessoSetor());
                        	
                        	if( !result.booleanValue() )
                        		throw new ServiceException("Não foi possível registrar a remessa.");                        

                        	deslocamentoRemessaAutomatica = instanciarHistoricoDeslocamento(historicoDeslocamento, 
                        				historicoDeslocamento.getProcessoSetor());
                        /*}
                        else {
                        	result = peticaoSetorService.persistirPeticaoSetor(
                        			historicoDeslocamento.getPeticaoSetor());
                        	
                        	if(!result.booleanValue())
                        		throw new ServiceException("Não foi possível registrar a remessa.");                        

                        	deslocamentoRemessaAutomatica = instanciarHistoricoDeslocamento(historicoDeslocamento, 
                        				historicoDeslocamento.getPeticaoSetor());
                        }*/
                        
	                        deslocamentoRemessaAutomatica.setSecaoOrigem(historicoDeslocamento.getSecaoDestino());
	                        deslocamentoRemessaAutomatica.setSecaoDestino(null);
	                        deslocamentoRemessaAutomatica.setUsuarioDestino(null);                    	
	                    	
	                        //if(deslocamentoProcesso) {
	                        	if( !deslocamentoRemessaAutomatica.getProcessoSetor().adicionarHistoricoDeslocamento(
	                        			deslocamentoRemessaAutomatica) )
	                        		throw new ServiceException("Não foi possível adicionar um deslocamento para o " +
	                        				"processo/protocolo '" +
	                        				deslocamentoRemessaAutomatica.getProcessoSetor().getIdentificacao()+"'.");
	                        //}
	                        /*else {
	                        	if(!deslocamentoRemessaAutomatica.getPeticaoSetor().adicionarHistoricoDeslocamento(
	                        			deslocamentoRemessaAutomatica))
	                        		throw new ServiceException("Não foi possível adicionar um deslocamento para a " +
	                        				"petição '" +
	                        				deslocamentoRemessaAutomatica.getPeticaoSetor().getIdentificacaoPeticao() + 
	                        				"'.");
	                        }*/
                    	//}
                    }
                }
                else {
                	//if(deslocamentoProcesso) {
                		if( !historicoDeslocamento.getProcessoSetor().adicionarHistoricoDeslocamento(
                				historicoDeslocamento) )
                			throw new ServiceException("Não foi possível adicionar um deslocamento para o " +
                					"processo/protocolo '" +
                					historicoDeslocamento.getProcessoSetor().getIdentificacao()+"'.");
                	//}
                	/*else {
                		if(!historicoDeslocamento.getPeticaoSetor().adicionarHistoricoDeslocamento(
                				historicoDeslocamento))
                			throw new ServiceException("Não foi possível adicionar um deslocamento para a " +
                					"petição '" +
                					historicoDeslocamento.getPeticaoSetor().getIdentificacaoPeticao() + "'.");
                	}*/
                }
            }
            
            //if(deslocamentoProcesso)
            	result = processoSetorService.persistirProcessoSetor(historicoDeslocamento.getProcessoSetor());
            /*else
            	result = peticaoSetorService.persistirPeticaoSetor(historicoDeslocamento.getPeticaoSetor());*/
        }
        catch( ServiceException e ) {
            throw e;
        }

        return result;        
    }
    
    public Boolean registrarRemessaPeticao(HistoricoDeslocamentoPeticao historicoDeslocamentoPeticao) 
    throws ServiceException {
    	
        if( historicoDeslocamentoPeticao == null )
            throw new NullPointerException("Deslocamento nulo ao remeter.");
        
        Boolean result = Boolean.FALSE;

        try {
            
            validarRemessaPeticao(historicoDeslocamentoPeticao);
                        
            UsuarioEGab usuario = (UsuarioEGab) UserHolder.get();
            SecaoSetor secaoUsuario = secaoSetorService.recuperarSecaoSetor(null, usuario, null, 
            		usuario.getSetor().getId(), true, Boolean.TRUE);

            if( secaoUsuario == null || secaoUsuario.getSecao() == null || secaoUsuario.getSecao().getId() == null 
                    || secaoUsuario.getSetor() == null || secaoUsuario.getSetor().getId() == null )
                throw new ServiceException("Usuário não se encontra em alguma seção desse setor.");            
            
            boolean secaoOrigemDefinida = false;
            
            //conferir
            HistoricoDeslocamentoPeticao deslocamentoAtual = null;
            
            //boolean deslocamentoPeticao = historicoDeslocamentoPeticao.isHistoricoDeslocamentoPeticaoSetor();
            
            // Verifica se há algum deslocamento atual
            //if(deslocamentoPeticao)
            	deslocamentoAtual = historicoDeslocamentoPeticao.getPeticaoSetor().getDeslocamentoAtual();
            	
            if( deslocamentoAtual != null ) {                
                // Verifica se a data de recebimento é nula
                if( deslocamentoAtual.getDataRecebimento() == null ) {
                    
                    // Verifica se a seção de destino foi informada
                    if( deslocamentoAtual.getSecaoDestino() != null ) {
                    	String mensagemErro = "";
                    	
                    	//if(deslocamentoPeticao)
                    		mensagemErro = "A petição '" + 
                    				historicoDeslocamentoPeticao.getPeticaoSetor().getIdentificacaoPeticao() +
                    				"' ainda não foi recebida no destino.";
                    		
                		throw new ServiceException(mensagemErro);
                    }
                }
                else {
                    if( deslocamentoAtual.getSecaoDestino() != null &&
                        deslocamentoAtual.getSecaoDestino().getId() != null ) {
                        
                    	//alterei aqui
                        if( !(secaoUsuario.getSecao().getId().equals(deslocamentoAtual.getSecaoDestino().getId()) &&
                              secaoUsuario.getSetor().getId().equals(deslocamentoAtual.getPeticaoSetor().getSetor().getId()) ) ) {
                            
                            if( !usuario.getPossuiPerfilGestao().booleanValue() ) {
                                
                            	StringBuffer mensagemErro = new StringBuffer();
                            	
                            	//if(deslocamentoPeticao)
                            		mensagemErro.append("A petição '" + 
                            				historicoDeslocamentoPeticao.getPeticaoSetor().getIdentificacaoPeticao());
                            	
                            	mensagemErro.append("' não se encontra na seção do usuário.");
                            	
                            	throw new ServiceException(mensagemErro.toString());                                
                            }
                            else {
                                historicoDeslocamentoPeticao.setSecaoOrigem(deslocamentoAtual.getSecaoDestino());
                                //alterei aqui
                                //historicoDeslocamentoPeticao.setSetor(deslocamentoAtual.getSetor());    
                                
                                secaoOrigemDefinida = true;
                            }
                        }
                    }
                }
            }

            if( !secaoOrigemDefinida ) {
                historicoDeslocamentoPeticao.setSecaoOrigem(secaoUsuario.getSecao());
                //alterei aqui
                //historicoDeslocamentoPeticao.setSetor(secaoUsuario.getSetor());
            }
            
            historicoDeslocamentoPeticao.setUsuarioOrigem(usuario.getUsuario());
            historicoDeslocamentoPeticao.setDataRemessa(new Date());
            
            if( historicoDeslocamentoPeticao.getSecaoDestino() == null ) {
            	historicoDeslocamentoPeticao.setDataRecebimento(historicoDeslocamentoPeticao.getDataRemessa());
            	historicoDeslocamentoPeticao.setUsuarioDestino(null);
                
                //if(deslocamentoPeticao) {
                	if(!historicoDeslocamentoPeticao.getPeticaoSetor().adicionarHistoricoDeslocamento(
                			historicoDeslocamentoPeticao))
                		throw new ServiceException("Não foi possível adicionar um deslocamento para a petição '" +
                				historicoDeslocamentoPeticao.getPeticaoSetor().getIdentificacaoPeticao() + "'.");
                //}
            }
            else {
            	//alterei aqui
                SecaoSetor secaoSetorDestino = secaoSetorService.recuperarSecaoSetor(null, 
                    historicoDeslocamentoPeticao.getSecaoDestino().getId(), 
                    historicoDeslocamentoPeticao.getPeticaoSetor().getSetor().getId(), Boolean.TRUE);
            
                if( secaoSetorDestino.getParametro().getRecebimentoAutomatico().booleanValue() ) {
                	historicoDeslocamentoPeticao.setDataRecebimento(historicoDeslocamentoPeticao.getDataRemessa());
                	historicoDeslocamentoPeticao.setUsuarioDestino(null);
                
                    //if(deslocamentoPeticao) {
                    	if(!historicoDeslocamentoPeticao.getPeticaoSetor().adicionarHistoricoDeslocamento(
                    			historicoDeslocamentoPeticao))
                    		throw new ServiceException("Não foi possível adicionar um deslocamento para a petição '" +
                    				historicoDeslocamentoPeticao.getPeticaoSetor().getIdentificacaoPeticao() + "'.");
                    //}
                    
                    if( secaoSetorDestino.getParametro().getRemessaAutomatica().booleanValue() ) {
 
                    	HistoricoDeslocamentoPeticao deslocamentoRemessaAutomatica = null;
                    	
                    	// Força a inclusão do registro no banco de dados para que a ordem dos andamentos 
                    	// fique correta.
                    	//if(deslocamentoPeticao) {
                        	result = peticaoSetorService.persistirPeticaoSetor(
                        			historicoDeslocamentoPeticao.getPeticaoSetor());
                        	
                        	if(!result.booleanValue())
                        		throw new ServiceException("Não foi possível registrar a remessa.");                        

                        	deslocamentoRemessaAutomatica = instanciarHistoricoDeslocamentoPeticao(
                        										historicoDeslocamentoPeticao, 
                        										historicoDeslocamentoPeticao.getPeticaoSetor());
                        //}
                        
                        deslocamentoRemessaAutomatica.setSecaoOrigem(historicoDeslocamentoPeticao.getSecaoDestino());
                        deslocamentoRemessaAutomatica.setSecaoDestino(null);
                        deslocamentoRemessaAutomatica.setUsuarioDestino(null);
                        
                        //if(deslocamentoPeticao) {
                        	if(!deslocamentoRemessaAutomatica.getPeticaoSetor().adicionarHistoricoDeslocamento(
                        			deslocamentoRemessaAutomatica))
                        		throw new ServiceException("Não foi possível adicionar um deslocamento para a " +
                        				"petição '" +
                        				deslocamentoRemessaAutomatica.getPeticaoSetor().getIdentificacaoPeticao() + 
                        				"'.");
                        //}
                    }
                }
                else {
                	//if(deslocamentoPeticao) {
                		if(!historicoDeslocamentoPeticao.getPeticaoSetor().adicionarHistoricoDeslocamento(
                				historicoDeslocamentoPeticao))
                			throw new ServiceException("Não foi possível adicionar um deslocamento para a " +
                					"petição '" +
                					historicoDeslocamentoPeticao.getPeticaoSetor().getIdentificacaoPeticao() + "'.");
                	//}
                }
            }
            
            //if(deslocamentoPeticao)
            	result = peticaoSetorService.persistirPeticaoSetor(historicoDeslocamentoPeticao.getPeticaoSetor());
        }
        catch( ServiceException e ) {
            throw e;
        }

        return result;        
    }  
    
    private void validarRemessa(HistoricoDeslocamento deslocamento) 
    throws ServiceException {
        
        if( deslocamento == null )
            throw new NullPointerException("Objeto que representa o deslocamento é nulo.");
/*        
        if(deslocamento.isHistoricoDeslocamentoProcessoSetor() == null)
        	throw new RegraDeNegocioException("O deslocamento deve possuir um Processo.");
        
        if(deslocamento.getProcessoSetor() != null)
        	throw new RegraDeNegocioException("O deslocamento deve conter um Processo exclusivamente.");
      
        if(deslocamento.isHistoricoDeslocamentoProcessoSetor() && 
        		deslocamento.getProcessoSetor().getDataSaida() != null )
            throw new RegraDeNegocioException("O processo/protocolo ao qual o deslocamento está associado não " +
            		"mais se encontra no setor.");
*/            		
            		
        if(deslocamento.getProcessoSetor().getDataSaida() != null )
            throw new ServiceException("O processo/protocolo ao qual o deslocamento está associado não " +
            		"mais se encontra no setor.");
        
        /*
        if(!deslocamento.isHistoricoDeslocamentoProcessoSetor() && 
        		deslocamento.getPeticaoSetor().getDataSaidaSetor() != null )
            throw new RegraDeNegocioException("A petição ao qual o deslocamento está associado não " +
            		"mais se encontra no setor.");
        */
        /*
        if( deslocamento.getSetor() == null )
            throw new RegraDeNegocioException("O deslocamento precisa estar associado a um setor.");        
 		*/
    } 
    
    private void validarRemessaPeticao(HistoricoDeslocamentoPeticao deslocamento) 
    throws ServiceException {
        
        if( deslocamento == null )
            throw new NullPointerException("Objeto que representa o deslocamento é nulo.");
/*        
        if(deslocamento.isHistoricoDeslocamentoPeticaoSetor() == null)
        	throw new RegraDeNegocioException("O deslocamento deve possuir uma Petição.");
       
        if(deslocamento.getPeticaoSetor() != null)
        	throw new RegraDeNegocioException("O deslocamento deve conter uma Petição exclusivamente.");      
       
        if(!deslocamento.isHistoricoDeslocamentoPeticaoSetor() && 
        		deslocamento.getPeticaoSetor().getDataSaidaSetor() != null )
            throw new RegraDeNegocioException("A petição ao qual o deslocamento está associado não " +
            		"mais se encontra no setor.");
*/        
        
        if(deslocamento.getPeticaoSetor().getDataSaidaSetor() != null )
            throw new ServiceException("A petição ao qual o deslocamento está associado não " +
            		"mais se encontra no setor.");                
        
        /*
        if( deslocamento.getSetor() == null )
            throw new RegraDeNegocioException("O deslocamento precisa estar associado a um setor.");        
 		*/
    }         
    
    private HistoricoDeslocamento instanciarHistoricoDeslocamento(HistoricoDeslocamento deslocamento, 
    		ESTFBaseEntity modeloDocJudiciario) {
        HistoricoDeslocamento novoDeslocamento = new HistoricoDeslocamento();

        if(modeloDocJudiciario instanceof ProcessoSetor)
        	novoDeslocamento.setProcessoSetor((ProcessoSetor) modeloDocJudiciario);
        else
        	throw new IllegalArgumentException("O objeto passado deve ser um ProcessoSetor ou uma PeticaoSetor.");
        
        novoDeslocamento.setSecaoOrigem(deslocamento.getSecaoOrigem());
        novoDeslocamento.setSecaoDestino(deslocamento.getSecaoDestino());
        novoDeslocamento.setUsuarioOrigem(deslocamento.getUsuarioOrigem());        
        novoDeslocamento.setUsuarioDestino(deslocamento.getUsuarioDestino());
        novoDeslocamento.setDataRemessa(deslocamento.getDataRemessa());        
        novoDeslocamento.setDataRecebimento(deslocamento.getDataRecebimento());        
        novoDeslocamento.setSetor(deslocamento.getSetor());
        novoDeslocamento.setTarefa(deslocamento.getTarefa());        
        novoDeslocamento.setObservacao(deslocamento.getObservacao());
        novoDeslocamento.setNumeroArmario(deslocamento.getNumeroArmario());
        novoDeslocamento.setNumeroSala(deslocamento.getNumeroSala());
        novoDeslocamento.setNumeroEstante(deslocamento.getNumeroEstante());
        novoDeslocamento.setNumeroPrateleira(deslocamento.getNumeroPrateleira());
        novoDeslocamento.setNumeroColuna(deslocamento.getNumeroColuna());
        novoDeslocamento.setObjetoIncidente(novoDeslocamento.getProcessoSetor().getObjetoIncidente());
        
        return novoDeslocamento;
    }    
    
    private HistoricoDeslocamentoPeticao instanciarHistoricoDeslocamentoPeticao(HistoricoDeslocamentoPeticao deslocamentoPeticao, 
    		ESTFBaseEntity modeloDocJudiciario) {
        HistoricoDeslocamentoPeticao novoDeslocamento = new HistoricoDeslocamentoPeticao();

        if(modeloDocJudiciario instanceof PeticaoSetor)
        	novoDeslocamento.setPeticaoSetor((PeticaoSetor) modeloDocJudiciario);
        else
        	throw new IllegalArgumentException("O objeto passado deve ser uma PeticaoSetor.");
        
        novoDeslocamento.setSecaoOrigem(deslocamentoPeticao.getSecaoOrigem());
        novoDeslocamento.setSecaoDestino(deslocamentoPeticao.getSecaoDestino());
        novoDeslocamento.setUsuarioOrigem(deslocamentoPeticao.getUsuarioOrigem());        
        novoDeslocamento.setUsuarioDestino(deslocamentoPeticao.getUsuarioDestino());
        novoDeslocamento.setDataRemessa(deslocamentoPeticao.getDataRemessa());        
        novoDeslocamento.setDataRecebimento(deslocamentoPeticao.getDataRecebimento());
        //alterei aqui
        //novoDeslocamento.setSetor(deslocamentoPeticao.getSetor());
        novoDeslocamento.setTarefa(deslocamentoPeticao.getTarefa());        
        novoDeslocamento.setObservacao(deslocamentoPeticao.getObservacao());
        novoDeslocamento.setNumeroArmario(deslocamentoPeticao.getNumeroArmario());
        novoDeslocamento.setNumeroSala(deslocamentoPeticao.getNumeroSala());
        novoDeslocamento.setNumeroEstante(deslocamentoPeticao.getNumeroEstante());
        novoDeslocamento.setNumeroPrateleira(deslocamentoPeticao.getNumeroPrateleira());
        novoDeslocamento.setNumeroColuna(deslocamentoPeticao.getNumeroColuna());
        
        return novoDeslocamento;
    }   
    
    public Boolean registrarRecebimento(List<ProcessoSetor> listaProcessoSetor, HistoricoDeslocamento historicoDeslocamento) 
    throws ServiceException {
        
        Boolean result = Boolean.FALSE; 
        
        if( listaProcessoSetor == null || listaProcessoSetor.size() == 0 ){
            throw new ServiceException("Os processos/protocolos devem ser informados.");
        } 

        for(ProcessoSetor processoSetor: listaProcessoSetor ) {
            if( !registrarRecebimento(instanciarHistoricoDeslocamento(historicoDeslocamento, processoSetor) ) ) {
                
                StringBuffer error = new StringBuffer("Não foi possível registrar o recebimento para o ");
                
                if( processoSetor.getPossuiIndentificacaoProcessual() )
                    error.append("processo: "+processoSetor.getIdentificacaoProcessual());
                else
                    error.append("protocolo: "+processoSetor.getIdentificacaoProtocolo());
                
                throw new ServiceException(error.toString());
            }
        }
        
        result = Boolean.TRUE;
        
        return result;
    }
    
    public Boolean registrarRecebimentoPeticao(List<PeticaoSetor> listaPeticaoSetor, 
    		HistoricoDeslocamentoPeticao historicoDeslocamento) 
    throws ServiceException {

    	Boolean result = Boolean.FALSE; 

    	if(listaPeticaoSetor == null || listaPeticaoSetor.size() == 0){
    		throw new ServiceException("As petições devem ser informadas.");
    	} 

    	for(PeticaoSetor peticaoSetor: listaPeticaoSetor) {

    		if(!registrarRecebimentoPeticao(instanciarHistoricoDeslocamentoPeticao(historicoDeslocamento, peticaoSetor))) {

    			StringBuffer error = new StringBuffer("Não foi possível registrar o recebimento para a ");
    			error.append("petição: " + peticaoSetor.getIdentificacaoPeticao());

    			throw new ServiceException(error.toString());
    		}
    	}

    	result = Boolean.TRUE;

    	return result;
    }
    
    public Boolean registrarRecebimento(HistoricoDeslocamento historicoDeslocamento) 
    throws ServiceException {
        
    	//peticaoSetorService = (IpeticaoSetorService) getServico("peticaoSetorService");
    	
        if( historicoDeslocamento == null )
            throw new NullPointerException("Deslocamento nulo ao receber.");        
        
        Boolean result = Boolean.FALSE;

        try {
            
            UsuarioEGab usuario = (UsuarioEGab) UserHolder.get();
            SecaoSetor secaoUsuario = secaoSetorService.recuperarSecaoSetor(null, usuario, null, 
                    usuario.getSetor().getId(), true, Boolean.TRUE);

            if( secaoUsuario == null || secaoUsuario.getSecao() == null || secaoUsuario.getSecao().getId() == null 
                    || secaoUsuario.getSetor() == null || secaoUsuario.getSetor().getId() == null )
                throw new ServiceException("Usuário não se encontra em alguma seção desse setor.");            
            
            //boolean deslocamentoProcesso = historicoDeslocamento.isHistoricoDeslocamentoProcessoSetor();
            
            // Verifica se há algum deslocamento atual
            HistoricoDeslocamento deslocamentoAtual = null;
            
            //if(deslocamentoProcesso)
            	deslocamentoAtual = historicoDeslocamento.getProcessoSetor().getDeslocamentoAtual();
            /*else
            	deslocamentoAtual = historicoDeslocamento.getPeticaoSetor().getDeslocamentoAtual();*/
            
            if( deslocamentoAtual == null ) {
            	
            	StringBuffer msgErro = new StringBuffer();
            	
            	//if(deslocamentoProcesso)
            		msgErro.append("O processo/protocolo '" +
            				deslocamentoAtual.getProcessoSetor().getIdentificacaoProcessual());
            	/*else
            		msgErro.append("A petição '" +
            				deslocamentoAtual.getPeticaoSetor().getIdentificacaoPeticao());*/
            	
            	msgErro.append("' não possui deslocamento.");
            	
            	throw new ServiceException(msgErro.toString());
            }
            else {
                
            	if( deslocamentoAtual.getDataRecebimento() != null ) {
                	
            		//if(deslocamentoProcesso)
                		throw new ServiceException("O processo/protocolo '" +
                				deslocamentoAtual.getProcessoSetor().getIdentificacaoProcessual()+
                				"' já foi recebido.");
            		/*else
            			throw new RegraDeNegocioException("A petição '" +
                				deslocamentoAtual.getPeticaoSetor().getIdentificacaoPeticao()+
                				"' já foi recebida.");*/
                }
                else {
                                        
                    if( deslocamentoAtual.getSecaoDestino() != null &&
                        deslocamentoAtual.getSecaoDestino().getId() != null ) {
                        
                        if( !(secaoUsuario.getSecao().getId().equals(deslocamentoAtual.getSecaoDestino().getId()) &&
                              secaoUsuario.getSetor().getId().equals(deslocamentoAtual.getSetor().getId()) ) ) {
                            
                            if( !AcegiSecurityUtils.isUserInSomeRole(usuario,
                            		new String[]{
                            		UsuarioEGab.PERFIL_MASTER, 
                            		UsuarioEGab.PERFIL_ADMINISTRACAO, 
                            		UsuarioEGab.PERFIL_GESTAO})) {
                                
                            	//if(deslocamentoProcesso)
                            		throw new ServiceException("O processo/protocolo '" +
                            				deslocamentoAtual.getProcessoSetor().getIdentificacao()+
                            				"' não foi remetido para a seção do usuário.");
                            	/*else
                            		throw new RegraDeNegocioException("A petição '" +
                            				deslocamentoAtual.getPeticaoSetor().getIdentificacaoPeticao()+
                            				"' não foi remetida para a seção do usuário.");*/
                            }
                        }
                    }
                }
            }
            
            // Atualiza informações de recebimento
            deslocamentoAtual.setUsuarioDestino(usuario.getUsuario());
            deslocamentoAtual.setDataRecebimento(new Date());        
            
            // Atualiza informações de localização e tarefa
            deslocamentoAtual.setTarefa(historicoDeslocamento.getTarefa());        
            deslocamentoAtual.setObservacao(historicoDeslocamento.getObservacao());
            deslocamentoAtual.setNumeroArmario(historicoDeslocamento.getNumeroArmario());
            deslocamentoAtual.setNumeroSala(historicoDeslocamento.getNumeroSala());
            deslocamentoAtual.setNumeroEstante(historicoDeslocamento.getNumeroEstante());
            deslocamentoAtual.setNumeroColuna(historicoDeslocamento.getNumeroColuna());
            deslocamentoAtual.setNumeroPrateleira(historicoDeslocamento.getNumeroPrateleira());
            
            
            if( deslocamentoAtual.getSecaoDestino() != null ) {
                SecaoSetor secaoSetorDestino = secaoSetorService.recuperarSecaoSetor(null, 
                        deslocamentoAtual.getSecaoDestino().getId(), deslocamentoAtual.getSetor().getId(), Boolean.TRUE);
                    
                if( secaoSetorDestino != null && secaoSetorDestino.getParametro().getRemessaAutomatica().booleanValue() ) {
                    
                    // Força a inclusão do registro no banco de dados para que a ordem dos andamentos fique correta.
                    //if(deslocamentoProcesso)
                    	result = processoSetorService.persistirProcessoSetor(deslocamentoAtual.getProcessoSetor());
                    /*else
                    	result = peticaoSetorService.persistirPeticaoSetor(deslocamentoAtual.getPeticaoSetor());*/
                    
                    if( !result.booleanValue() )
                        throw new ServiceException("Não foi possível registrar a remessa.");                      
                    
                    HistoricoDeslocamento deslocamentoRemessaAutomatica = null;
                        
                    //if(deslocamentoProcesso)
                    	deslocamentoRemessaAutomatica = instanciarHistoricoDeslocamento(deslocamentoAtual, 
                    			deslocamentoAtual.getProcessoSetor());
                    /*else
                    	deslocamentoRemessaAutomatica = instanciarHistoricoDeslocamento(deslocamentoAtual, 
                    			deslocamentoAtual.getPeticaoSetor());*/
                    
                    deslocamentoRemessaAutomatica.setSecaoOrigem(deslocamentoAtual.getSecaoDestino());
                    deslocamentoRemessaAutomatica.setUsuarioOrigem(deslocamentoAtual.getUsuarioDestino());
                    deslocamentoRemessaAutomatica.setSecaoDestino(null);
                    deslocamentoRemessaAutomatica.setUsuarioDestino(null);
                    deslocamentoRemessaAutomatica.setDataRemessa(deslocamentoAtual.getDataRecebimento());
                    
                    //if(deslocamentoProcesso) {
                    	if( !deslocamentoRemessaAutomatica.getProcessoSetor().adicionarHistoricoDeslocamento(
                    			deslocamentoRemessaAutomatica) )
                    		throw new ServiceException("Não foi possível adicionar um deslocamento para o processo/protocolo '" +
                    				deslocamentoRemessaAutomatica.getProcessoSetor().getIdentificacao()+"'.");
                    //}
                    /*else {
                    	if(!deslocamentoRemessaAutomatica.getPeticaoSetor().adicionarHistoricoDeslocamento(
                    			deslocamentoRemessaAutomatica))
                    		throw new ServiceException("Não foi possível adicionar um deslocamento para a petição '" +
                    				deslocamentoRemessaAutomatica.getPeticaoSetor().getIdentificacaoPeticao() + "'.");
                    }*/
                }
            }            

            //if(deslocamentoProcesso)
            	result = processoSetorService.persistirProcessoSetor(deslocamentoAtual.getProcessoSetor());
            /*else
            	result = peticaoSetorService.persistirPeticaoSetor(deslocamentoAtual.getPeticaoSetor());*/
        }
        catch( ServiceException e ) {
            throw e;
        }

        return result;        
    }
    
    public Boolean registrarRecebimentoPeticao(HistoricoDeslocamentoPeticao historicoDeslocamentoPeticao) 
    throws ServiceException {
    	
        if( historicoDeslocamentoPeticao == null )
            throw new NullPointerException("Deslocamento nulo ao receber.");        
        
        Boolean result = Boolean.FALSE;

        try {
            
            UsuarioEGab usuario = (UsuarioEGab) UserHolder.get();
            SecaoSetor secaoUsuario = secaoSetorService.recuperarSecaoSetor(null, usuario, null, 
                    usuario.getSetor().getId(), true, Boolean.TRUE);

            if( secaoUsuario == null || secaoUsuario.getSecao() == null || secaoUsuario.getSecao().getId() == null 
                    || secaoUsuario.getSetor() == null || secaoUsuario.getSetor().getId() == null )
                throw new ServiceException("Usuário não se encontra em alguma seção desse setor.");            
            
            //boolean deslocamentoPeticao = historicoDeslocamentoPeticao.isHistoricoDeslocamentoPeticaoSetor();
            
            // Verifica se há algum deslocamento atual
            HistoricoDeslocamentoPeticao deslocamentoAtual = null;
            
            //if(deslocamentoPeticao)
            	deslocamentoAtual = historicoDeslocamentoPeticao.getPeticaoSetor().getDeslocamentoAtual();
            
            if( deslocamentoAtual == null ) {
            	
            	StringBuffer msgErro = new StringBuffer();
            	
            	//if(deslocamentoPeticao)
            		msgErro.append("A petição '" +
            				deslocamentoAtual.getPeticaoSetor().getIdentificacaoPeticao());
            	
            	msgErro.append("' não possui deslocamento.");
            	
            	throw new ServiceException(msgErro.toString());
            }
            else {
                
            	if( deslocamentoAtual.getDataRecebimento() != null ) {
                	
            		//if(deslocamentoPeticao)
            			throw new ServiceException("A petição '" +
                				deslocamentoAtual.getPeticaoSetor().getIdentificacaoPeticao()+
                				"' já foi recebida.");
                }
                else {
                                        
                    if( deslocamentoAtual.getSecaoDestino() != null &&
                        deslocamentoAtual.getSecaoDestino().getId() != null ) {
                        
                    	//alterei aqui
                        if( !(secaoUsuario.getSecao().getId().equals(deslocamentoAtual.getSecaoDestino().getId()) &&
                              secaoUsuario.getSetor().getId().equals(deslocamentoAtual.getPeticaoSetor().getSetor().getId()) ) ) {
                            
                            if( !AcegiSecurityUtils.isUserInSomeRole(usuario, 
                            		new String[]{
                            		UsuarioEGab.PERFIL_MASTER, 
                            		UsuarioEGab.PERFIL_ADMINISTRACAO, 
                            		UsuarioEGab.PERFIL_GESTAO}) ) {
                                
                            	//if(deslocamentoPeticao)
                            		throw new ServiceException("A petição '" +
                            				deslocamentoAtual.getPeticaoSetor().getIdentificacaoPeticao()+
                            				"' não foi remetida para a seção do usuário.");
                            }
                        }
                    }
                }
            }
            
            // Atualiza informações de recebimento
            deslocamentoAtual.setUsuarioDestino(usuario.getUsuario());
            deslocamentoAtual.setDataRecebimento(new Date());        
            
            // Atualiza informações de localização e tarefa
            deslocamentoAtual.setTarefa(historicoDeslocamentoPeticao.getTarefa());        
            deslocamentoAtual.setObservacao(historicoDeslocamentoPeticao.getObservacao());
            deslocamentoAtual.setNumeroArmario(historicoDeslocamentoPeticao.getNumeroArmario());
            deslocamentoAtual.setNumeroSala(historicoDeslocamentoPeticao.getNumeroSala());
            deslocamentoAtual.setNumeroEstante(historicoDeslocamentoPeticao.getNumeroEstante());
            deslocamentoAtual.setNumeroColuna(historicoDeslocamentoPeticao.getNumeroColuna());
            deslocamentoAtual.setNumeroPrateleira(historicoDeslocamentoPeticao.getNumeroPrateleira());
            
            
            if( deslocamentoAtual.getSecaoDestino() != null ) {
                SecaoSetor secaoSetorDestino = secaoSetorService.recuperarSecaoSetor(null, 
                        deslocamentoAtual.getSecaoDestino().getId(), deslocamentoAtual.getPeticaoSetor().getSetor().getId(), Boolean.TRUE);
                    
                if( secaoSetorDestino.getParametro().getRemessaAutomatica().booleanValue() ) {
                    
                    // Força a inclusão do registro no banco de dados para que a ordem dos andamentos fique correta.
                    //if(deslocamentoPeticao)
                    	result = peticaoSetorService.persistirPeticaoSetor(deslocamentoAtual.getPeticaoSetor());
                    
                    if( !result.booleanValue() )
                        throw new ServiceException("Não foi possível registrar a remessa.");                      
                    
                    HistoricoDeslocamentoPeticao deslocamentoRemessaAutomatica = null;
                        
                    //if(deslocamentoPeticao)
                    	deslocamentoRemessaAutomatica = instanciarHistoricoDeslocamentoPeticao(deslocamentoAtual, 
                    			deslocamentoAtual.getPeticaoSetor());
                    
                    deslocamentoRemessaAutomatica.setSecaoOrigem(deslocamentoAtual.getSecaoDestino());
                    deslocamentoRemessaAutomatica.setUsuarioOrigem(deslocamentoAtual.getUsuarioDestino());
                    deslocamentoRemessaAutomatica.setSecaoDestino(null);
                    deslocamentoRemessaAutomatica.setUsuarioDestino(null);
                    deslocamentoRemessaAutomatica.setDataRemessa(deslocamentoAtual.getDataRecebimento());
                    
                    //if(deslocamentoPeticao) {
                    	if(!deslocamentoRemessaAutomatica.getPeticaoSetor().adicionarHistoricoDeslocamento(
                    			deslocamentoRemessaAutomatica))
                    		throw new ServiceException("Não foi possível adicionar um deslocamento para a petição '" +
                    				deslocamentoRemessaAutomatica.getPeticaoSetor().getIdentificacaoPeticao() + "'.");
                    //}
                }
            }            

            //if(deslocamentoPeticao)
            	result = peticaoSetorService.persistirPeticaoSetor(deslocamentoAtual.getPeticaoSetor());
        }
        catch( ServiceException e ) {
            throw e;
        }

        return result;        
    }        
    
    public Boolean persistirHistoricoDeslocamento(HistoricoDeslocamento historicoDeslocamento) 
    throws ServiceException {
    	Boolean alterado = Boolean.FALSE;

		try {

			alterado = dao.persistirHistoricoDeslocamento(historicoDeslocamento);

		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}

		return alterado;                
    }
    
    public Boolean persistirHistoricoDeslocamentoPeticao(HistoricoDeslocamentoPeticao historicoDeslocamento) 
    throws ServiceException {
    	Boolean alterado = Boolean.FALSE;

		try {

			alterado = dao.persistirHistoricoDeslocamentoPeticao(historicoDeslocamento);

		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}

		return alterado;                
    }    
    
	public Boolean excluirHistoricoDeslocamento(HistoricoDeslocamento deslocamento) 
	throws ServiceException{
    	Boolean alterado = Boolean.FALSE;

		try {

			alterado = dao.excluirHistoricoDeslocamento(deslocamento);

		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}

		return alterado;   		
	}

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long idSetor, 
            Secao secaoOrigem, Secao secaoDestino)
    throws ServiceException {
		List<HistoricoDeslocamento> lista = new LinkedList<HistoricoDeslocamento>();
		
		try{
			lista = dao.pesquisarDeslocamentos(idSetor, secaoOrigem, 
					secaoDestino);
			return lista;
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
}