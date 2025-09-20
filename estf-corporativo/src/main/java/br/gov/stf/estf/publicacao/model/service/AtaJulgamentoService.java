package br.gov.stf.estf.publicacao.model.service;

import java.util.Date;

import br.gov.stf.estf.entidade.publicacao.AtaJulgamento;
import br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta;
import br.gov.stf.estf.publicacao.model.dataaccess.AtaJulgamentoDao;
import br.gov.stf.framework.model.service.GenericService;

/**
 * Interface de serviços de negócio para Ata de Julgamento.
 * 
 * @author Rodrigo Barreiros
 * @since 08.06.2009
 */
public interface AtaJulgamentoService extends GenericService<AtaJulgamento, AtaJulgamento.AtaJulgamentoId, AtaJulgamentoDao> {
	
	/**
	 * Recupera a Ata, Índice ou Pauta de Julgamento gerada em uma dada seção em uma determinada data.
	 * 
	 * @param dataSessao a data da sessão de julgamento
	 * @param colegiado a Seção/Capitulo que produziu o documento. Valores: 2 - Plenário; 3 - 1ª Turma; 4 - 2ª Turma.
	 * @param categoria o tipo da ata de julgamento. Valores: A - Ata, AE - Ata Extraordinária, AO - Ata Ordinaria, I - Índice, P - Pauta.
	 * @return a ata de julgamento
	 */
	public AtaJulgamento recuperarPor(Date dataSessao, int colegiado, CategoriaAta categoria);

}
