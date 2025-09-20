package br.gov.stf.estf.documento.model.service;

import java.util.Date;

import br.gov.stf.estf.documento.model.util.ControleDeVotoDTO;
import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.service.ServiceException;

public interface ControleVotoTextoService {
	public void criarControleVoto(ControleDeVotoDTO controleVoto) throws ServiceException;
	public void criarControleVotoRepercussaoGeral(Long seqObjetoIncidente) throws ServiceException;
	public boolean criarControleVotoEmentaAcordao(ObjetoIncidente objetoIncidente, Date dataSessao,TipoSessaoControleVoto tipoSessao, TipoTexto tipoTexto, Sessao sesao) throws ServiceException;
}