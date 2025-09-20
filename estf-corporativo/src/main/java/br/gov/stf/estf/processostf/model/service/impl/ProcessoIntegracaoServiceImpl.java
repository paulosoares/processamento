package br.gov.stf.estf.processostf.model.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoIntegracao;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoIntegracaoDao;
import br.gov.stf.estf.processostf.model.service.ProcessoIntegracaoService;
import br.gov.stf.estf.util.MNIParams;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.security.user.UserHolder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service("processoIntegracaoService")
public class ProcessoIntegracaoServiceImpl extends GenericServiceImpl<ProcessoIntegracao, Long, ProcessoIntegracaoDao>
	implements ProcessoIntegracaoService {
	
	@Autowired
	private MNIParams mniParams;
	
	private Log logger = LogFactory.getLog(getClass());
	
    public ProcessoIntegracaoServiceImpl(ProcessoIntegracaoDao dao) { super(dao); }

	@Override
	public ProcessoIntegracao pesquisar(Long idAndamentoProcesso, String classeProcesso, Long numeroProcesso) throws ServiceException {

		try {
			return dao.pesquisar(idAndamentoProcesso, classeProcesso, numeroProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ProcessoIntegracao> pesquisar(Integer codOrgao, String tipoSituacao, Date dataInicial, Date dataFinal, Integer numProcesso, String siglaProcesso, Integer... tipoComunicacao) throws ServiceException {
		try {
			return dao.pesquisar(codOrgao, tipoSituacao, dataInicial, dataFinal, numProcesso, siglaProcesso, tipoComunicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean isAvisoLido(AndamentoProcesso andamentoProcesso) throws ServiceException {

		try {
			return dao.isAvisoLido(andamentoProcesso);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao ler o aviso da baixa.", e);
		}
	}

	@Override
	public List<ProcessoIntegracao> pesquisar(AndamentoProcesso andamentoProcesso) throws ServiceException {
		
		try {
			return dao.pesquisar(andamentoProcesso);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar os Processos Integração.", e);
		}
	}
	
	@Override
	public void excluir(ProcessoIntegracao processoIntegracao) throws ServiceException{
		try{
			dao.excluir(processoIntegracao);
		}catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void inserirEncaminhadoPorMidia(ProcessoIntegracao processoIntegracao, Long usuarioExternoESTF, String descricao)
			throws ServiceException {
		try{
			dao.inserirProcessoIntegracaoUsuario(processoIntegracao, usuarioExternoESTF);
			dao.inserirProcessoIntegracaoLog(processoIntegracao, descricao);
		}catch (DaoException e) {
			throw new ServiceException("Erro ao inserir processo no banco de dados.");
		}
		
	}

	@Override
	public void chamaPrcPreparaInterop(ObjetoIncidente<?> objetoIncidente)
			throws ServiceException {
		try{
			dao.chamaPrcPreparaInterop(objetoIncidente);
		}catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}

	@Override
	public void incluirEncaminhadoPorMidia(ProcessoIntegracao processoIntegracao, Processo processo, AndamentoProcesso andamentoProcesso,Long usuarioExternoESTF, String observacao) throws ServiceException{
		try{
			dao.incluir(processoIntegracao);
			ProcessoIntegracao proIntegracao = dao.pesquisar(andamentoProcesso.getId(),processo.getSiglaClasseProcessual(), processo.getNumeroProcessual());
			inserirEncaminhadoPorMidia(proIntegracao, usuarioExternoESTF, observacao);
			
		}catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	
	@Override
    public void excluirAvisosAndamentoDeBaixa(AndamentoProcesso andamentoProcesso) throws ServiceException {
                   
         List<ProcessoIntegracao> processosIntegracao = new LinkedList<ProcessoIntegracao>();
                   
         try {
             processosIntegracao = dao.pesquisar(andamentoProcesso);
         } catch (DaoException e) {
          	 throw new ServiceException(e);
         }
                  
         if (processosIntegracao.size() > 0){
            for (ProcessoIntegracao pi : processosIntegracao){
               try{
                   dao.excluirParametroIntegracao(pi);
                   dao.excluirProcessoIntegracaoUsuarios(pi);
               }catch (DaoException e) {
                   throw new ServiceException(e);
               }
             }
          }
                   
         try{
             dao.excluirAvisosAndamentoDeBaixa(andamentoProcesso.getId());
         }catch (DaoException e) {
             throw new ServiceException(e);
         }
    }
	
	@Override
	public void atualizaSession() throws DaoException{
		dao.flushSession();
	}
	
	@Override
	public Long pesquisarQtdAvisosLidos(Integer seqUsuarioExterno) throws ServiceException {
		try {
			return dao.pesquisarQtdAvisosLidos(seqUsuarioExterno);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao ler o quantitativo de avisos pendentes!", e);
		}
	}
	
	public int notificarBaixaMNI(Long objetoIncidenteId, Long seqAndamentoProcesso) {
		   int code = 0;
		   
		   String url = mniParams.getServidor()
				   .replaceAll("\\{objetoIncidenteId\\}", objetoIncidenteId.toString())
		   		   .replaceAll("\\{seqAndamentoProcesso\\}",seqAndamentoProcesso.toString());
			
			try {
				GetMethod getMethod = new GetMethod(url);
				getMethod.addRequestHeader("Autorizacao", gerarTokenAutuacao());
				HttpClientParams params = new HttpClientParams();
				params.setSoTimeout(Integer.valueOf(mniParams.getTimeout()));
				
				code = new HttpClient(params).executeMethod(getMethod);
				
				String path = null;
				InputStream inputStream = getMethod.getResponseBodyAsStream();
				byte[] buffer = IOUtils.toByteArray(inputStream);
				path = new String(buffer);
				
				logger.info("A chamada ao MNI (URL: "+url+") retornou o code " + code + " com o seguinte conteúdo: \n" + path);
//				if (code == HttpStatus.SC_OK)
//					return true;
//				
//				if (code == HttpStatus.SC_NOT_FOUND)
//					return false;
			} catch (Exception e) {
				logger.error("Erro ao acessar o MNI na URL: " + url, e);
			}
			
			return code;
		}
	   
	private String gerarTokenAutuacao() {
		String token = null;
		try {
			token = Jwts.builder() //
					.setSubject(UserHolder.get().getUsername().toUpperCase()) //
					.signWith(SignatureAlgorithm.HS256, mniParams.getChavePrivada()) //
					.compact();
		} catch (Exception e) {
			throw new SecurityException("Não foi possível gerar o token.", e);
		}
		return token;
	}	
	
}
