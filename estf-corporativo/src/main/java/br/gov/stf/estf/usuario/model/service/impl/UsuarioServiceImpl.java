package br.gov.stf.estf.usuario.model.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.CargaClasseProcessualTipoJulgamento;
import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.estf.entidade.usuario.GrupoUsuario;
import br.gov.stf.estf.entidade.usuario.Perfil;
import br.gov.stf.estf.entidade.usuario.PessoaEmail;
import br.gov.stf.estf.entidade.usuario.PessoaTelefone;
import br.gov.stf.estf.entidade.usuario.TipoConfiguracaoUsuario;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.usuario.UsuarioDistribuicao;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioDao;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.estf.usuario.model.util.TipoUsuario;
import br.gov.stf.estf.usuario.security.RolesPopulator;
import br.gov.stf.estf.usuario.security.RolesPopulatorSIAA;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("usuarioService")
public class UsuarioServiceImpl  extends GenericServiceImpl<Usuario, String, UsuarioDao> implements UsuarioService {
	@Autowired(required=true)
    public UsuarioServiceImpl(UsuarioDao dao) { 
		super(dao); 
	}
	
	public GrantedAuthority[] pesquisarRoles(String siglaUsuario) throws ServiceException {		
		Usuario usuario;
		try {
			usuario = dao.recuperarPorId(siglaUsuario.toUpperCase());
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar usuario: " + siglaUsuario.toUpperCase(), e);
		}
		
		if(usuario == null) return new GrantedAuthority[0];
		
		Set<Perfil> perfis = usuario.getPerfis();
		
		if ( perfis!=null && perfis.size()>0 ) {
			GrantedAuthority[] grants = new GrantedAuthorityImpl[perfis.size()];
			Iterator<Perfil> it = perfis.iterator();
			for ( int i=0 ; i<grants.length ; i++ ) {				
				grants[i] = new GrantedAuthorityImpl( RolesPopulator.ROLE_PREFIX+it.next().getDescricao() );
			}
			return grants;
		}
		
		return null;
	}
	
	public List<GrantedAuthority> pesquisarRoles(String idUsuario, String siglaSistema, String rolePrefix) throws ServiceException {		
		Usuario usuario;
		try {
			usuario = dao.recuperarPorId(idUsuario.toUpperCase());
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar usuario: " + idUsuario.toUpperCase(), e);
		}
		
		if(usuario == null) {
			return new ArrayList<GrantedAuthority>();
		}
		
		Set<Perfil> perfis = usuario.getPerfis();
		
		if ( perfis!=null && perfis.size()>0 ) {
			List<GrantedAuthority> grants = new ArrayList<GrantedAuthority>();
			for (Perfil perfil : perfis) {
				if (perfil.getSistema().equalsIgnoreCase(siglaSistema)) {
					grants.add(new GrantedAuthorityImpl(rolePrefix + perfil.getDescricao()));
				}
			}
			return grants;
		}
		
		return null;
	}
	
	public GrantedAuthority[] pesquisarRolesSIAA(String siglaUsuario) throws ServiceException {		
		Usuario usuario;
		try {
			usuario = dao.recuperarPorId(siglaUsuario.toUpperCase());
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar usuario: " + siglaUsuario.toUpperCase(), e);
		}
		
		if(usuario == null) return new GrantedAuthority[0];
		
		Set<Perfil> perfis = usuario.getPerfis();
		
		if ( perfis!=null && perfis.size()>0 ) {
			GrantedAuthority[] grants = new GrantedAuthorityImpl[perfis.size()];
			Iterator<Perfil> it = perfis.iterator();
			for ( int i=0 ; i<grants.length ; i++ ) {
				Perfil perfil = it.next();	
				grants[i] = new GrantedAuthorityImpl( RolesPopulatorSIAA.ROLE_PREFIX_SIAA+perfil.getSistema()+"-"+perfil.getDescricao());
			}
			return grants;
		}
		
		return null;
	}
	
	@Override
	public boolean hasRoleEditarObservacao(String siglaUsuario) throws ServiceException {
		try {
			Usuario usuario = dao.recuperarPorId(siglaUsuario.toUpperCase());
			Set<Perfil> perfis = usuario.getPerfis();
			for (Perfil perfil: perfis) {
				if ( "EDITAR_OBSERVACAO_ANDAMENTO".equals(perfil.getDescricao()) ) {
					return true;
				}
			}
			return false;
		} catch (DaoException e) {
		  throw new ServiceException(e);	
		}
	}

	public List<Usuario> pesquisaUsuario(String id, String nome, Boolean ativo,
			Long idSetor) throws ServiceException {
		try {
			return dao.pesquisaUsuario(id, nome, ativo, idSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
    public Usuario recuperarUsuario(String sigla) 
    throws ServiceException {
    	
        Usuario usuario = null;
    	
        try {
        	
        	usuario = dao.recuperarUsuario(sigla);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuario;
    }
    
    public UsuarioEGab recuperarUsuarioEGab(String sigla, Boolean padrao) 
    throws ServiceException {
    	
        UsuarioEGab usuario = null;
    	
        try {
        	
        	usuario = dao.recuperarUsuarioEGab(sigla, padrao);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuario;
    }
    
    public List<UsuarioEGab> recuperarTodosUsuariosEGab(String sigla) 
    throws ServiceException {
    	
        List<UsuarioEGab> listaUsuarios = null;
    	
        try {
        	
        	listaUsuarios = dao.recuperarTodosUsuariosEGab(sigla);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return listaUsuarios;
    }     
     
    public List<UsuarioEGab> pesquisarUsuarioSecao(Long codigoSetor, Long codigoSecao, boolean usuarioNaoCadastrado) 
    throws ServiceException {
        List<UsuarioEGab> usuario = null;
    	try{
            usuario = dao.pesquisarUsuarioSecao(codigoSetor, codigoSecao, usuarioNaoCadastrado);
    	}
    	catch(DaoException e){
    		throw new ServiceException (e);
    	}
    	return usuario;
	}
    
	public List<UsuarioEGab> pesquisarUsuarios(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo) throws ServiceException {
		
    	List<UsuarioEGab> usuarios = null;
    	
        try {
        	
        	usuarios = dao.pesquisarUsuarios(codigoSetor, tipoUsuario, ativo);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuarios;
	}
	
	public List<Usuario> pesquisarUsuariosEgab(Long codigoSetor, Boolean ativo) throws ServiceException {
		
    	List<Usuario> usuarios = null;
    	
        try {
        	
        	usuarios = dao.pesquisarUsuariosEgab(codigoSetor, ativo);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuarios;
	}	
	
	public List<Usuario> pesquisarUsuariosEgabPlantao(Long codigoSetor, Boolean ativo) throws ServiceException {
		
    	List<Usuario> usuarios = null;
    	
        try {
        	
        	usuarios = dao.pesquisarUsuariosEgabPlantao(codigoSetor, ativo);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuarios;
	}	
	
	public List<Usuario> pesquisarUsuariosSTF(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo ,  Boolean cadastradoEgab) throws ServiceException {
		
    	List<Usuario> usuarios = null;
    	
        try {
        	
        	usuarios = dao.pesquisarUsuariosSTF( codigoSetor, tipoUsuario, ativo, cadastradoEgab ) ;

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuarios;
	}
	
	@Override
	public List<GrupoUsuario> pesquisarGruposUsuario(Long codigoSetor, String descricao) throws ServiceException {
		return pesquisarGruposUsuario(codigoSetor, descricao, null);
	}
	
	@Override
	public List<GrupoUsuario> pesquisarGruposUsuario(Long codigoSetor, String descricao, Boolean ativo) throws ServiceException {
		
		List<GrupoUsuario> gruposUsuario = null;
    	
        try {
        	gruposUsuario = dao.pesquisarGruposUsuario(codigoSetor, descricao, ativo);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return gruposUsuario;		
	}
	
	public List<UsuarioEGab> pesquisarUsuariosGrupo(Long grupoId, Boolean usuarioAtivo) throws ServiceException {
    	List<UsuarioEGab> usuarios = null;
    	
        try {
        	
        	usuarios = dao.pesquisarUsuariosGrupo(grupoId, usuarioAtivo);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuarios;		
	}
	
	public List<UsuarioDistribuicao> pesquisarUsuariosDistribuicaoGrupo(Long grupoId, Long idSetor, Boolean usuarioAtivo,
            Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual, Boolean zeraContagem, String dataContagem) throws ServiceException {
    	List<UsuarioDistribuicao> usuarios = null;
    	
        try {
       	
        	usuarios = dao.pesquisarUsuariosDistribuicaoGrupo(grupoId, idSetor, usuarioAtivo, contagemSaidaSetor, contagemFimTramite, cargaDistribuicaoFaseAtual, zeraContagem, dataContagem);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuarios;		
	}
	
	public UsuarioDistribuicao recuperarUsuarioDistribuicao(String siglaUsuario, Long idSetor,Boolean contagemSaidaSetor, 
            Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual) 
	throws ServiceException {
		
		if( siglaUsuario == null )
			throw new ServiceException("Obrigatório informar a sigla do usuário.");
		
    	UsuarioDistribuicao usuario = null;
    	
        try {
        	
        	usuario = dao.recuperarUsuarioDistribuicao(siglaUsuario,idSetor,contagemSaidaSetor, contagemFimTramite, cargaDistribuicaoFaseAtual);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuario;		
	}
	
	public List<UsuarioDistribuicao> pesquisarUsuariosDistribuicao(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo,
            Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual, Boolean zeraContagem, String dataContagem) throws ServiceException {
		
    	List<UsuarioDistribuicao> usuarios = null;
    	
        try {
        	
        	usuarios = dao.pesquisarUsuariosDistribuicao(codigoSetor, tipoUsuario, ativo,
                    contagemSaidaSetor, contagemFimTramite, cargaDistribuicaoFaseAtual, zeraContagem, dataContagem);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return usuarios;
	}
	
	
	public Boolean adicionarUsuarioGrupo(UsuarioEGab usuario, GrupoUsuario grupoUsuario)throws ServiceException{
		   try{

			   if((grupoUsuario == null) || (grupoUsuario.getId() == null)){
				   
	             throw new ServiceException("O grupo deve ser informado.");

			   }else if((usuario == null) || (usuario.getUsuario().getMatricula() == null)){
				   
				   throw new ServiceException("O usuário deve ser informado.");
			   }
			   
			   return dao.adicionarUsuarioGrupo(usuario, grupoUsuario);

		   }catch(DaoException e){
			   throw new ServiceException(e);
		   }
		
		}
	
	
	
	public Boolean removerUsuarioNoGrupo(GrupoUsuario grupoUsuario,UsuarioEGab usuario)throws ServiceException{
		   try{

			   if((grupoUsuario == null) || (grupoUsuario.getId() == null)){
				   
	             throw new ServiceException("O grupo deve ser informado.");

			   }else if((usuario == null) || (usuario.getUsuario().getMatricula() == null)){
				   
				   throw new ServiceException("O usuário deve ser informado.");
			   }

			   if(grupoUsuario.getUsuarios()==null){
				   grupoUsuario.setUsuarios(new HashSet());
			   }
			   grupoUsuario.getUsuarios().remove(usuario);

				return persistirGrupoUsuario(grupoUsuario);

		   }catch(ServiceException e){
			   throw e;
		   }
		
		}	
	
	
	
	public Boolean persistirGrupoUsuario (GrupoUsuario grupoUsuario) throws ServiceException {
		
    	Boolean alterado = Boolean.FALSE;
    	
        try {
        	if(grupoUsuario==null){
        		throw new ServiceException("Objeto nulo grupoUsuario.");
        	}
        	if(grupoUsuario.getDescricao()==null||grupoUsuario.getDescricao().equals("")){
        		throw new ServiceException("A descrição deve ser informada.");
        	}
        	if(grupoUsuario.getSetor()==null){
        		throw new ServiceException("Objeto nulo Setor ");
        	}
        	grupoUsuario.setDescricao(grupoUsuario.getDescricao().toUpperCase());
        	if(grupoUsuario.getObservacao()!=null){
        		grupoUsuario.setObservacao(grupoUsuario.getObservacao().toUpperCase());
        	}
        	if(grupoUsuario.getId()==null){
        		if(dao.verificarUniciadadeGrupoUsuario(grupoUsuario.getDescricao(),grupoUsuario.getSetor().getId()).booleanValue()){
        			throw new ServiceException("Já existe um grupo cadastrado com essa descrição.");
        		}
        	}
        	
        	alterado = dao.persistirGrupoUsuario(grupoUsuario);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return alterado;  				
	}
	
	
	public Boolean alterarUsuarioPadraoEGab(List<UsuarioEGab> listaUsuarioEGab) throws ServiceException {
		
    	Boolean alterado = Boolean.FALSE;
    	
        try {
        	if(listaUsuarioEGab != null){
        		for(UsuarioEGab usuarioEGab : listaUsuarioEGab){
        			dao.alterarUsuarioPadraoEGab(usuarioEGab);
        		}
        	}
        	
        	alterado = Boolean.TRUE;
        	
        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return alterado;  					
	}
		
	
    public Boolean excluirGrupoUsuario(List <GrupoUsuario> listaGrupoUsuario)throws ServiceException{
        
        try {
            if( listaGrupoUsuario != null ){
            	
            	for(GrupoUsuario grupoUsuario : listaGrupoUsuario){
            		dao.excluirGrupoUsuario(grupoUsuario);
            	}
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        catch( DaoException e ) {
        	throw new ServiceException(e);
        }    	
    }
    public Boolean excluirUsuarioEGab(List<UsuarioEGab> listaUsuarioEGab)throws ServiceException{
    	
    	try {
            if( listaUsuarioEGab != null ){
            	
            	for(UsuarioEGab usuarioEgab : listaUsuarioEGab ){
            		dao.excluirUsuarioEGab(usuarioEgab);
            	}
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        catch( DaoException e ) {
        	throw new ServiceException(e);
        }   
    }
    
    public Boolean persistirUsuarioEGab( UsuarioEGab usuarioEGab ) throws ServiceException {
		
    	Boolean alterado = Boolean.FALSE;
    	
    	UsuarioEGab usuario;    	
    	
        try {
        	if(usuarioEGab==null){
        		throw new ServiceException("Objeto nulo UsuarioEgab.");
        	}
        	if(usuarioEGab.getUsuario()==null && usuarioEGab.getUsuario().getId() == null ){
        		throw new ServiceException("O Objeto usuário está nulo.");
        	}
        	if(usuarioEGab.getSetor()==null ){
        		throw new ServiceException("Objeto nulo Setor ");
        	}
        	
        	if( dao.persistirUsuarioEGab( usuarioEGab ) ){
        		alterado = Boolean.TRUE;
        	}
        	/*
        	usuario = recuperarUsuarioEGab(usuarioEGab.getUsuario().getSigla(), Boolean.FALSE);
        	
        	if(usuario == null){
        		
        		dao.persistirUsuarioEGab( usuarioEGab );
        		alterado = Boolean.TRUE;
        		
        	}else{
        		//throw new ServiceException("Usuário já está cadastrado.");
        	}
        	*/   
        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return alterado;  				
	}
  
	public List<Setor> pesquisarSetoresEGab(String usuario) throws ServiceException {
		
		List<Setor> setores = null;
    	
        try {
        	setores = dao.pesquisarSetoresEGab(usuario);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return setores;		
	}    

	public Boolean excluirUsuarioSecao(SecaoSetor secaoSetor) throws ServiceException{
		Boolean alterado = Boolean.FALSE;
		
		try{
			dao.excluirUsuarioSecao(secaoSetor);
			alterado = Boolean.TRUE;
		}
		catch( DaoException e ) {
        	throw new ServiceException(e);
        }    
		return alterado;
	}
	
	public Boolean recuperarUsuarioSecao(SecaoSetor secaoSetor) throws ServiceException{
		Boolean existe = Boolean.FALSE;
		
		try{
			existe = dao.recuperarUsuarioSecao(secaoSetor);
		}
		catch( DaoException e ) {
        	throw new ServiceException(e);
        }    
		return existe;		
	}
	
	public List<CargaClasseProcessualTipoJulgamento> recuperarCargaClasse
			( Long codigoSetor, TipoUsuario tipoUsuario, String siglaUsuario, Boolean contagemSaidaSetor, 
			  Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual )
	throws ServiceException {
		List<CargaClasseProcessualTipoJulgamento> resultado = new ArrayList<CargaClasseProcessualTipoJulgamento>();

		try{
			resultado = dao.recuperarCargaClasse(codigoSetor, tipoUsuario, siglaUsuario, contagemSaidaSetor, contagemFimTramite, cargaDistribuicaoFaseAtual);
		}
		catch( DaoException e ) {
        	throw new ServiceException(e);
        }    
		return resultado;			
	}
	
	public Boolean persistirConfiguracaoUsuario(
			ConfiguracaoUsuario configuracaoUsuario) throws ServiceException {
		try{
			return dao.persistirConfiguracaoUsuario(configuracaoUsuario);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
		
	}

	public List<ConfiguracaoUsuario> pesquisarConfiguracaoUsuario(Long id,
			String sigUsuario, Long codigoTipoConfiguracao)
			throws ServiceException {
		try{
			return dao.pesquisarConfiguracaoUsuario(id, sigUsuario, codigoTipoConfiguracao);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
	}

	public TipoConfiguracaoUsuario recuperarTipoConfiguracaoUsuario(Long id)
	throws ServiceException {
		try{
			return dao.recuperarTipoConfiguracaoUsuario(id);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
	}
	
	public GrupoUsuario recuperarGrupoUsuario(Long idGrupo)
	throws ServiceException {
		try{
			return dao.recuperarGrupoUsuario(idGrupo);
		}catch(DaoException e){
			throw new ServiceException(e);
		}		
	}

	@Override
	public Set<GrupoUsuario> recuperarGrupoUsuario(Usuario usuario) throws ServiceException {
		Set<GrupoUsuario> grupos;
        try {
        	grupos = dao.pesquisarGruposUsuario(usuario); 
        	
        	for (GrupoUsuario grupo : grupos) {
        		Hibernate.initialize(grupo);
        	}
        	
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return grupos;
	}
	
	public List<PessoaTelefone> pesquisarTelefones(Long seqPessoa) throws ServiceException{
		try{
			return dao.pesquisarTelefones(seqPessoa);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<PessoaEmail> pesquisarEmails(Long seqPessoa) throws ServiceException{
		try{
			return dao.pesquisarEmails(seqPessoa);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
