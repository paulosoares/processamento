package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.localizacao.model.dataaccess.SecaoSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SecaoSetorService extends GenericService<SecaoSetor, Long, SecaoSetorDao> {

	   /**
         * Método responsavel por recuperar a secao do Setor
         * @param id codigo da secao Setor
         * @return Objeto SecaoSetor
         * @throws ServiceException 
         * @since 1.0
         * @athor guilhermea
         */
        public SecaoSetor recuperarSecaoSetor(Long id,Long idSecao,Long idSetor, Boolean ativo) throws ServiceException;
        
            /**
         * Método reposnsavel por recuperar a secaoSetor em que o usuario se encontra
         * @param id codigo da seção localizacao
         * @param usuario usuario que esta logado 
         * @param Secao Secao do usuario
         * @param validarExistenciaSecaoUsuario valida se existe uma seçãoSetor para o usuario e/ou localizacao informado
         * @return retorna seção localizacao
         * @throws ServiceException
         * @since 1.0
         * @athor guilhermea
         */
        public SecaoSetor recuperarSecaoSetor(Long id, UsuarioEGab usuario,Secao secao,Long idSetor, boolean validarExistenciaSecaoUsuario, Boolean ativo) throws ServiceException;
        
        /**
         * metodo resposanvel por pesquisar a secao
         * @param id do secaoSetor
         * @param sigUsuario sigla do usuario
         * @param idSecao codigo da secao
         * @param idSetor codigo do localizacao
         * @athor guilherma
         * @return lista de SecaoSetor
         * @throws ServiceException
         */
        public List<SecaoSetor> pesquisarSecaoSetor(Long id, String sigUsuario, String descricaoSecao, Long idSecao, Long idSetor, Boolean ativo) throws ServiceException;
        
        public List<Secao> pesquisarSecao(Long id, String sigUsuario, String descricaoSecao, Long idSetor, Boolean ativo) throws ServiceException;        
        
        public List<SecaoSetor> pesquisarTarefaSecao(Long id,Long idTarefa,String descricaoTarefa,String descricaoSecao,Long idSecao,Long idSetor) throws ServiceException;
        
        
        
        public List pesquisarSecaoSetor(Long id,Secao secao, Boolean ativo) throws ServiceException;
        
        public Boolean persistirSecaoSetor(SecaoSetor secaoSetor) throws ServiceException;
        
        /**
         * método resposanvel por excluir a secaoSetor
         * caso a secao esteja cadastrado somente na localizacao do usuario ela tb será excluida
         * @param secaoSetor
         * @return Boolean
         * @throws ServiceException
         */
        public Boolean excluirSecaoSetor(SecaoSetor secaoSetor) throws ServiceException;
}
