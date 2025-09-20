package br.gov.stf.estf.documento.model.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.PecaProcessoEletronicoComunicacaoDao;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoComunicacaoService;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("pecaProcessoEletronicoComunicacaoService")
public class PecaProcessoEletronicoComunicacaoServiceImpl extends
      GenericServiceImpl<PecaProcessoEletronicoComunicacao, Long, PecaProcessoEletronicoComunicacaoDao> implements
      PecaProcessoEletronicoComunicacaoService {

   public PecaProcessoEletronicoComunicacaoServiceImpl(PecaProcessoEletronicoComunicacaoDao dao) {
      super(dao);
   }

   public void vincularPecasAoDocumento(List<ArquivoProcessoEletronico> listaArquivoProcessoEletronico, Comunicacao comunicacao) throws ServiceException {
      for (ArquivoProcessoEletronico arquivoProcessoEletronico : listaArquivoProcessoEletronico) {
         PecaProcessoEletronicoComunicacao pecaProcessoEletronicoComunicacao = new PecaProcessoEletronicoComunicacao();
         pecaProcessoEletronicoComunicacao.setComunicacao(comunicacao);
         pecaProcessoEletronicoComunicacao.setPecaProcessoEletronico(arquivoProcessoEletronico.getPecaProcessoEletronico());
         pecaProcessoEletronicoComunicacao.setDataVinculacao(Calendar.getInstance().getTime());
         try {
            dao.salvar(pecaProcessoEletronicoComunicacao);
         } catch (DaoException e) {
            throw new ServiceException(e);
         }
      }
   }

   @Override
   public void alterarPecasVinculadasComunicacao(List<ArquivoProcessoEletronico> listaArquivoProcessoEletronico, Comunicacao comunicacao)
         throws ServiceException {

      List<PecaProcessoEletronicoComunicacao> listaPecas = new ArrayList<PecaProcessoEletronicoComunicacao>();
      // pesquisa todas as comunicações existentes na tabelas para poder apagar
      try {
         listaPecas = dao.pesquisarPecasPelaComunicacao(comunicacao);
      } catch (DaoException e) {
         throw new ServiceException(e);
      }

      // remove as comunicações para posteriormente cadastrar aos novos vínculos
      // de peças
      try {
         for (PecaProcessoEletronicoComunicacao peca : listaPecas) {
            dao.excluir(peca);
         }
      } catch (DaoException e) {
         throw new ServiceException(e);
      }

      // salva a nova lista de peças ao documento
      vincularPecasAoDocumento(listaArquivoProcessoEletronico, comunicacao);
   }

   @Override
   public List<PecaProcessoEletronicoComunicacao> pesquisarPecasPelaComunicacao(Comunicacao comunicacao) throws ServiceException {

      List<PecaProcessoEletronicoComunicacao> listaPecas = new ArrayList<PecaProcessoEletronicoComunicacao>();
      try {
         listaPecas = dao.pesquisarPecasPelaComunicacao(comunicacao);
      } catch (DaoException e) {
         throw new ServiceException(e);
      }

      return listaPecas;
   }


}
