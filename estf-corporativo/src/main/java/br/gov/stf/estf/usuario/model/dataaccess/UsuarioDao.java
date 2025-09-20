package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.List;
import java.util.Set;

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
import br.gov.stf.estf.usuario.model.util.TipoUsuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface UsuarioDao extends GenericDao<Usuario, String> {

	public List<Usuario> pesquisaUsuario(String id,String nome,Boolean ativo,Long idSetor)throws DaoException;
	
	public Usuario recuperarUsuario(String sigla) throws DaoException;
	
	public UsuarioEGab recuperarUsuarioEGab(String sigla, Boolean padrao) throws DaoException;
	
	public List<UsuarioEGab> recuperarTodosUsuariosEGab(String sigla) throws DaoException;	
	
	public Boolean verificarUniciadadeGrupoUsuario(String descricao,Long idSetor)throws DaoException;

	public List<UsuarioEGab> pesquisarUsuarios(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo) throws DaoException;
	
	public List<Usuario> pesquisarUsuariosSTF(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo , Boolean cadastradoEgab) throws DaoException;
	
	public List<GrupoUsuario> pesquisarGruposUsuario(Long codigoSetor, String descricao, Boolean ativo) throws DaoException;
	
	public List<GrupoUsuario> pesquisarGruposUsuario(Long codigoSetor, String descricao) throws DaoException;
	
	public List<UsuarioEGab> pesquisarUsuarioSecao(Long codigoSetor,Long codigoSecao,boolean usuarioNaoCadastrado) throws DaoException;
	
	public List<UsuarioEGab> pesquisarUsuariosGrupo(Long grupoId, Boolean usuarioAtivo) throws DaoException;
	
	public List<Usuario> pesquisarUsuariosEgab(Long codigoSetor, Boolean ativo) throws DaoException;

	public List<Usuario> pesquisarUsuariosEgabPlantao(Long codigoSetor, Boolean ativo) throws DaoException;
	
	public List<UsuarioDistribuicao> pesquisarUsuariosDistribuicaoGrupo(Long grupoId, Long idSetor, Boolean usuarioAtivo,
            Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual,  Boolean zeraContagem, String dataContagem) throws DaoException;
	
	public List<UsuarioDistribuicao> pesquisarUsuariosDistribuicao(Long codigoSetor, TipoUsuario tipoUsuario, Boolean ativo,
            Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual, Boolean zeraContagem, String dataContagem) throws DaoException;	

	public UsuarioDistribuicao recuperarUsuarioDistribuicao(String siglaUsuario, Long idSetor,
            Boolean contagemSaidaSetor, Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual) throws DaoException;

	public Boolean persistirGrupoUsuario (GrupoUsuario grupoUsuario) throws DaoException;
	
	public Boolean persistirUsuarioEGab( UsuarioEGab usuarioEGab )	throws DaoException;
	
	public Boolean adicionarUsuarioGrupo(UsuarioEGab usuario, GrupoUsuario grupoUsuario)throws DaoException;
	
	//remove usuario do grupo
	public Boolean removeUsuarioGrupo (UsuarioEGab usuario, GrupoUsuario grupoUsuario)throws DaoException;
	
	public Boolean excluirGrupoUsuario(GrupoUsuario grupoUsuario)throws DaoException;
	
	public Boolean excluirUsuarioEGab(UsuarioEGab usuarioEGab)throws DaoException;
	
	public List<Setor> pesquisarSetoresEGab(String usuario) throws DaoException;
	
	public Boolean alterarUsuarioPadraoEGab(UsuarioEGab usuarioEGab) throws DaoException;
	
	public Boolean excluirUsuarioSecao(SecaoSetor secaoSetor) throws DaoException;
	
	public Boolean recuperarUsuarioSecao(SecaoSetor secaoSetor) throws DaoException;
	
	public GrupoUsuario recuperarGrupoUsuario(Long idGrupo) throws DaoException;
	
	public List<CargaClasseProcessualTipoJulgamento> recuperarCargaClasse
		( Long codigoSetor, TipoUsuario tipoUsuario, String siglaUsuario, Boolean contagemSaidaSetor, 
				Boolean contagemFimTramite, Boolean cargaDistribuicaoFaseAtual )
	  throws DaoException;
	
	
	public List<ConfiguracaoUsuario> pesquisarConfiguracaoUsuario(Long id, String sigUsuario, Long codigoTipoConfiguracao) 
	throws DaoException;
	
	public TipoConfiguracaoUsuario recuperarTipoConfiguracaoUsuario(Long id) throws DaoException;
	
	public Boolean persistirConfiguracaoUsuario(ConfiguracaoUsuario configuracaoUsuario)throws DaoException;

	public Set<GrupoUsuario> pesquisarGruposUsuario(Usuario usuario) throws DaoException;
	
	public List<PessoaTelefone> pesquisarTelefones(Long seqPessoa) throws DaoException;
		
	public List<PessoaEmail> pesquisarEmails(Long seqPessoa) throws DaoException;
	
	
}
