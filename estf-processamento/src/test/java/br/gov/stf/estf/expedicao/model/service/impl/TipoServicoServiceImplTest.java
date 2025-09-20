package br.gov.stf.estf.expedicao.model.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.model.dataaccess.TipoServicoDao;
import br.gov.stf.estf.expedicao.model.util.TipoEntregaEnum;
import br.gov.stf.estf.expedicao.model.util.TipoServicoEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import junit.framework.Assert;

/**
 *
 * @author Roberio.Fernandes
 */
public class TipoServicoServiceImplTest {

    private final TipoServicoServiceImpl tipoServicoServiceImpl;
    private boolean isEntregaCorreiosEncontrado;
    private TipoServicoEnum tipoServicoEncontrado;

    private class AnswerListarTiposServicoPorTipoEntrega implements Answer<List<TipoServico>> {

        @Override
        public List<TipoServico> answer(InvocationOnMock invocation) throws Throwable {
            isEntregaCorreiosEncontrado = (Boolean) invocation.getArguments()[0];
            tipoServicoEncontrado = (TipoServicoEnum) invocation.getArguments()[1];
            return null;
        }
    }

    public TipoServicoServiceImplTest() throws DaoException {
        TipoServicoDao tipoServicoDao = Mockito.mock(TipoServicoDao.class);
        Answer<List<TipoServico>> answer = new AnswerListarTiposServicoPorTipoEntrega();
        when(tipoServicoDao.listarTiposServicoPorTipoEntrega(anyBoolean(), any(TipoServicoEnum.class))).thenAnswer(answer);
        tipoServicoServiceImpl = new TipoServicoServiceImpl(tipoServicoDao);
    }

    private boolean testlistarTiposServicoPorTipoEntrega(TipoEntregaEnum tipoEntrega, boolean isEntregaCorreiosEsperado, TipoServicoEnum tipoServicoEsperado) {
        boolean resultado = false;
        try {
            tipoServicoServiceImpl.listarTiposServicoPorTipoEntrega(tipoEntrega);
            resultado = (isEntregaCorreiosEsperado == isEntregaCorreiosEncontrado)
                    && ((tipoServicoEsperado == null && tipoServicoEncontrado == null)
                    || (tipoServicoEsperado != null && tipoServicoEncontrado != null && tipoServicoEsperado.equals(tipoServicoEncontrado)));
        } catch (ServiceException ex) {
        }
        return resultado;
    }

    @Test
    public void testlistarTiposServicoPorTipoEntregaCorreios() {
        boolean isEntregaCorreiosEsperado = true;
        TipoServicoEnum tipoServicoEsperado = TipoServicoEnum.POSTAGEM;
        TipoEntregaEnum tipoEntrega = TipoEntregaEnum.CORREIOS;
        Assert.assertTrue(testlistarTiposServicoPorTipoEntrega(tipoEntrega, isEntregaCorreiosEsperado, tipoServicoEsperado));
    }

    @Test
    public void testlistarTiposServicoPorTipoEntregaEntregaPortaria() {
        boolean isEntregaCorreiosEsperado = false;
        TipoServicoEnum tipoServicoEsperado = null;
        TipoEntregaEnum tipoEntrega = TipoEntregaEnum.ENTREGA_PORTARIA;
        Assert.assertTrue(testlistarTiposServicoPorTipoEntrega(tipoEntrega, isEntregaCorreiosEsperado, tipoServicoEsperado));
    }

    @Test
    public void testlistarTiposServicoPorTipoEntregaMalote() {
        boolean isEntregaCorreiosEsperado = true;
        TipoServicoEnum tipoServicoEsperado = TipoServicoEnum.MALOTE;
        TipoEntregaEnum tipoEntrega = TipoEntregaEnum.MALOTE;
        Assert.assertTrue(testlistarTiposServicoPorTipoEntrega(tipoEntrega, isEntregaCorreiosEsperado, tipoServicoEsperado));
    }
}