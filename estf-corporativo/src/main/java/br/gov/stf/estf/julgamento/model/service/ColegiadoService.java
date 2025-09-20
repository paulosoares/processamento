package br.gov.stf.estf.julgamento.model.service;

import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.julgamento.model.dataaccess.ColegiadoDao;
import br.gov.stf.framework.model.service.GenericService;

public interface ColegiadoService extends GenericService<Colegiado, String, ColegiadoDao> {

	public String obterColegiadoEstruturaPublicacao(Setor setor);

	public TipoSessaoControleVoto obterColegiadoTipoSessaoControleVoto(Setor setor);

	public TipoColegiadoConstante obterColegiadoTipoColegiadoConstante(Setor setor);

}
