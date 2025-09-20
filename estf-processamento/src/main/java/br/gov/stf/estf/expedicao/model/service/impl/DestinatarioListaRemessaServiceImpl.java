package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.estf.expedicao.model.dataaccess.DestinatarioListaRemessaDao;
import br.gov.stf.estf.expedicao.model.service.DestinatarioListaRemessaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("destinatarioListaRemessaService")
public class DestinatarioListaRemessaServiceImpl extends GenericServiceImpl<DestinatarioListaRemessa, Long, DestinatarioListaRemessaDao> implements DestinatarioListaRemessaService {

    public static final long serialVersionUID = 1L;

    public DestinatarioListaRemessaServiceImpl(DestinatarioListaRemessaDao dao) {
        super(dao);
    }

    @Override
    public List<DestinatarioListaRemessa> pesquisarVariosCampos(String texto) throws ServiceException {
        try {
            return dao.pesquisarVariosCampos(texto);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public List<DestinatarioListaRemessa> pesquisar(DestinatarioListaRemessa destinatario, String siglaUf) throws ServiceException {
        try {
            return dao.pesquisar(destinatario, siglaUf);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

	@Override
	public DestinatarioListaRemessa copiar(Long id) throws ServiceException {
		DestinatarioListaRemessa destinatarioListaRemessa = recuperarPorId(id);
		return copiar(destinatarioListaRemessa);
	}

	@Override
	public DestinatarioListaRemessa copiar(DestinatarioListaRemessa destinatarioListaRemessa) throws ServiceException {
		DestinatarioListaRemessa copia = new DestinatarioListaRemessa();

		copia.setDescricaoAnterior(destinatarioListaRemessa.getDescricaoAnterior());
		copia.setDescricaoPrincipal(destinatarioListaRemessa.getDescricaoPrincipal());
		copia.setDescricaoPosterior(destinatarioListaRemessa.getDescricaoPosterior());
		copia.setLogradouro(destinatarioListaRemessa.getLogradouro());
		copia.setNumero(destinatarioListaRemessa.getNumero());
		copia.setComplemento(destinatarioListaRemessa.getComplemento());
		copia.setBairro(destinatarioListaRemessa.getBairro());
		copia.setMunicipio(destinatarioListaRemessa.getMunicipio());
		copia.setCep(destinatarioListaRemessa.getCep());
		copia.setNomeContato(destinatarioListaRemessa.getNomeContato());
		copia.setEmail(destinatarioListaRemessa.getEmail());
		copia.setCodigoAreaTelefone(destinatarioListaRemessa.getCodigoAreaTelefone());
		copia.setNumeroTelefone(destinatarioListaRemessa.getNumeroTelefone());
		copia.setCodigoAreaFax(destinatarioListaRemessa.getCodigoAreaFax());
		copia.setNumeroFax(destinatarioListaRemessa.getNumeroFax());
		copia.setAgrupador(destinatarioListaRemessa.getAgrupador());
		copia.setCodigoOrigem(destinatarioListaRemessa.getCodigoOrigem());

		return copia;
	}
}