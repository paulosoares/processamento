package br.gov.stf.estf.jurisdicionado.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.AssociacaoJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.PapelJurisdicionado;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumTipoAssociacao;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.AssociacaoJurisdicionadoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AssociacaoJurisdicionadoService extends GenericService<AssociacaoJurisdicionado, Long, AssociacaoJurisdicionadoDao> {
	public List<AssociacaoJurisdicionado> pesquisarAssociacoes(Jurisdicionado jurisdicionado) throws ServiceException;
	public List<AssociacaoJurisdicionado> pesquisarAssociacoesMembro(Jurisdicionado jurisdicionado) throws ServiceException;
	public AssociacaoJurisdicionado recuperarPorGrupoMembro(PapelJurisdicionado grupo, PapelJurisdicionado membro, EnumTipoAssociacao tipoAssociacao) throws ServiceException;

}
