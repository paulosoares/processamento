package br.gov.stf.estf.julgamento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.julgamento.model.dataaccess.ColegiadoDao;
import br.gov.stf.estf.julgamento.model.service.ColegiadoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("colegiadoService")
public class ColegiadoServiceImpl extends GenericServiceImpl<Colegiado, String, ColegiadoDao>
		implements ColegiadoService {

	public ColegiadoServiceImpl(ColegiadoDao dao) {
		super(dao);
	}

	@Override
	public String obterColegiadoEstruturaPublicacao(Setor setor) {
		if (setor != null) {
			if (EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA.equals(setor.getCodigoCapitulo())) {
				return "1T";
			} else if (EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA.equals(setor.getCodigoCapitulo())) {
				return "2T";
			} else {
				return "TP";
			}
		}
		return "";
	}

	@Override
	public TipoSessaoControleVoto obterColegiadoTipoSessaoControleVoto(Setor setor) {
		if (setor != null) {
			if (EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA.equals(setor.getCodigoCapitulo())) {
				return TipoSessaoControleVoto.PRIMEIRA_TURMA;
			} else if (EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA.equals(setor.getCodigoCapitulo())) {
				return TipoSessaoControleVoto.SEGUNDA_TURMA;
			} else {
				return TipoSessaoControleVoto.PLENARIO;
			}
		}
		return null;

	}
	
	@Override
	public TipoColegiadoConstante obterColegiadoTipoColegiadoConstante(Setor setor) {

		if (setor != null) {
			if (EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA.equals(setor.getCodigoCapitulo())) {
				return TipoColegiadoConstante.PRIMEIRA_TURMA;
			} else if (EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA.equals(setor.getCodigoCapitulo())) {
				return TipoColegiadoConstante.SEGUNDA_TURMA;
			} else
				return TipoColegiadoConstante.SESSAO_PLENARIA;
		}
	
		return null;
	}
	

}
