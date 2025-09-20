package br.gov.stf.estf.usuario.model.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.GrantedAuthority;

import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.CargaClasseProcessualTipoJulgamento;
import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.estf.entidade.usuario.GrupoUsuario;
import br.gov.stf.estf.entidade.usuario.PessoaEmail;
import br.gov.stf.estf.entidade.usuario.PessoaTelefone;
import br.gov.stf.estf.entidade.usuario.TipoConfiguracaoUsuario;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.usuario.UsuarioDistribuicao;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioDao;
import br.gov.stf.estf.usuario.model.util.TipoUsuario;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface UsuarioService extends GenericService<Usuario, String, UsuarioDao>{
	GrantedAuthority[] pesquisarRoles(String siglaUsuario) throws ServiceException;	
	GrantedAuthority[] pesquisarRolesSIAA(String siglaUsuario) throws ServiceException;	
	List<Usuario> pesquisaUsuario(String id,String nome,Boolean ativo,Long idSetor)throws ServiceException;
	
	public Usuario recuperarUsuario(String sigla) 
	throws ServiceException;
	
	public UsuarioEGab recuperarUsuarioEGab(String sigla, Boolean padrao) throws ServiceException;
	
	public List<Usuario> pesquisarUsuariosEgab(Long codigoSetor, Boolean ativo) throws ServiceException;
	
	public List<Usuario> pesquisarUsuariosEgabPlantao(Long codigoSetor, Boolean ativo) throws ServiceException;
	
	public List<UsuarioEGab> recuperarTodosUsuariosEGab(String sigla) throws ServiceException;
	
	public Boolean adicionarUsuarioGrupo(UsuarioEGab usuario, GrupoUsuario grupoUsuario) throws ServiceException;
	
	/**
	 * metodo responsavel por pesquisar os usuarios da secao
	 * @param codigoSetor codigo do setor de lotação usuário
	 * @param usuarioNaoCadastrado true retorna a lista de usuario daquele setor que não estão cadastrados na seção informada
	 * 							   FALSE retorna os usuarios que estão cadastrados na seção	
	 * @return lista de usuarios
	 * @throws ServiceException
	 */
	public List<UsuarioEGab> pesquisarUsuarioSecao(Long codigoSetor,Long codigoSecao,boolean usuarioNaoCadastrado) throws ServiceException;
	
	public List<UsuarioEGab> pesquisarUsuarios(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo) throws ServiceException;
	
	// TODO método parecido como o: pesquisaUsuario
	public List<Usuario> pesquisarUsuariosSTF(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo , Boolean cadastradoEgab) throws ServiceException;
		
	public List<UsuarioEGab> pesquisarUsuariosGrupo(Long grupoId, Boolean usuarioAtivo) throws ServiceException;
	
	public Boolean persistirGrupoUsuario (GrupoUsuario grupoUsuario) throws ServiceException;
	
	public List<GrupoUsuario> pesquisarGruposUsuario(Long codigoSetor, String descricao ) throws ServiceException;
	
	public Boolean removerUsuarioNoGrupo(GrupoUsuario grupoUsuario,UsuarioEGab usuario)throws ServiceException;

    // usuario
    public List<UsuarioDistribuicao> pesquisarUsuariosDistribuicaoGrupo(Long grupoId, Long idSetor, Boolean usuarioAtivo,
            Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual, Boolean zeraContagem, String dataContagem) throws ServiceException;
	
	public UsuarioDistribuicao recuperarUsuarioDistribuicao(String siglaUsuario,Long idSetor, Boolean contagemSaidaSetor,
            Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual) throws ServiceException;
	
	public List<UsuarioDistribuicao> pesquisarUsuariosDistribuicao(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo,
            Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual, Boolean zeraContagem, String dataContagem ) throws ServiceException;
	
	public Boolean persistirUsuarioEGab( UsuarioEGab usuarioEGab )	throws ServiceException;
		
	public Boolean excluirGrupoUsuario(List <GrupoUsuario> listaGrupoUsuario)throws ServiceException;
	
	public Boolean excluirUsuarioEGab(List<UsuarioEGab> listaUsuarioEGab)throws ServiceException;
	
	public List<Setor> pesquisarSetoresEGab(String usuario) throws ServiceException;
	
	public Boolean alterarUsuarioPadraoEGab(List<UsuarioEGab> listaUsuarioEGab) throws ServiceException;
	
	public Boolean excluirUsuarioSecao(SecaoSetor secaoSetor) throws ServiceException;
	
	public Boolean recuperarUsuarioSecao(SecaoSetor secaoSetor) throws ServiceException;
	
	public List<CargaClasseProcessualTipoJulgamento> recuperarCargaClasse
			( Long codigoSetor, TipoUsuario tipoUsuario, String siglaUsuario, Boolean contagemSaidaSetor, 
			  Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual ) throws ServiceException;
	
	// configuracao usuario
	public List<ConfiguracaoUsuario> pesquisarConfiguracaoUsuario(Long id, String sigUsuario, Long codigoTipoConfiguracao) 
	throws ServiceException;
	
	public TipoConfiguracaoUsuario recuperarTipoConfiguracaoUsuario(Long id) throws ServiceException;
	
	public Boolean persistirConfiguracaoUsuario(ConfiguracaoUsuario configuracaoUsuario)throws ServiceException;
	
	public GrupoUsuario recuperarGrupoUsuario(Long idGrupo)	throws ServiceException;
	
	List<GrantedAuthority> pesquisarRoles(String idUsuario, String siglaSistema, String rolePrefix) throws ServiceException;
	public boolean hasRoleEditarObservacao(String siglaUsuario) throws ServiceException;
	
	public List<GrupoUsuario> pesquisarGruposUsuario(Long idSetor, String nome, Boolean ativo) throws ServiceException;
	
	/**
	 * Retorna os grupos do egab do usuário passado como parâmetro
	 * @param idUsuarioLogado é o login do usuário
	 * @return uma lista de grupos do egab nos quais o usuário faz parte
	 * @throws ServiceException 
	 */
	public Set<GrupoUsuario> recuperarGrupoUsuario(Usuario usuario) throws ServiceException;
	
	public List<PessoaTelefone> pesquisarTelefones(Long seqPessoa) throws ServiceException;
	
	public List<PessoaEmail> pesquisarEmails(Long seqPessoa) throws ServiceException;
}
