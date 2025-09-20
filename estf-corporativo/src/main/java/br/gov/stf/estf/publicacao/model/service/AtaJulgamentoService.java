package br.gov.stf.estf.publicacao.model.service;

import java.util.Date;

import br.gov.stf.estf.entidade.publicacao.AtaJulgamento;
import br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta;
import br.gov.stf.estf.publicacao.model.dataaccess.AtaJulgamentoDao;
import br.gov.stf.framework.model.service.GenericService;

/**
 * Interface de servi�os de neg�cio para Ata de Julgamento.
 * 
 * @author Rodrigo Barreiros
 * @since 08.06.2009
 */
public interface AtaJulgamentoService extends GenericService<AtaJulgamento, AtaJulgamento.AtaJulgamentoId, AtaJulgamentoDao> {
	
	/**
	 * Recupera a Ata, �ndice ou Pauta de Julgamento gerada em uma dada se��o em uma determinada data.
	 * 
	 * @param dataSessao a data da sess�o de julgamento
	 * @param colegiado a Se��o/Capitulo que produziu o documento. Valores: 2 - Plen�rio; 3 - 1� Turma; 4 - 2� Turma.
	 * @param categoria o tipo da ata de julgamento. Valores: A - Ata, AE - Ata Extraordin�ria, AO - Ata Ordinaria, I - �ndice, P - Pauta.
	 * @return a ata de julgamento
	 */
	public AtaJulgamento recuperarPor(Date dataSessao, int colegiado, CategoriaAta categoria);

}
