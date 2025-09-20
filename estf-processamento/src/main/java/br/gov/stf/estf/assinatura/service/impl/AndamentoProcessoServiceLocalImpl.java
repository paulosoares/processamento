package br.gov.stf.estf.assinatura.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.assinatura.dataaccess.DeslocaProcessoDaoLocal;
import br.gov.stf.estf.assinatura.relatorio.service.ProcessamentoRelatorioService;
import br.gov.stf.estf.assinatura.service.AndamentoProcessoServiceLocal;
import br.gov.stf.estf.assinatura.service.DeslocaProcessoServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento.AbstractBeanRegistrarAndamento;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfo;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos.ProcessoEAndamentoProcesso;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.stfoffice.handler.HandlerException;
import br.jus.stf.util.jasperreports.UtilJasperReports;

@Service("andamentoProcessoServiceLocal")
public class AndamentoProcessoServiceLocalImpl implements AndamentoProcessoServiceLocal{

	@Autowired
	private AndamentoProcessoService andamentoProcessoService;
	
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private ProcessamentoRelatorioService processamentoRelatorioService;
	
	@Autowired
	private ArquivoProcessoEletronicoService arquivoProcessoEletronicoService;
	
	@Autowired
	private TextoAndamentoProcessoService textoAndamentoProcessoService;
	
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public void salvarAndamentoBaixa(List<ContainerGuiaProcessos> containerDeGuias, Setor setor) throws ServiceException, Exception {
	
		//Salva os andamentos e deslocamentos
		andamentoProcessoService.salvarAndamentoBaixa(containerDeGuias);
		
		//grava as certidões
		gerarCertidaoBaixa(containerDeGuias, setor);
	}

	//Gera a certidão de baixa, andamento e deslocamento, agrupando os processo de mesma origem em uma única guia.
		public void gerarCertidaoBaixa(List<ContainerGuiaProcessos> containerDeGuias, Setor setor)throws ServiceException {
			
			
			//Para cada guia e seus respectivos processos executa o seguinte
			for (ContainerGuiaProcessos containerGuia : containerDeGuias) {
				
				//Faz um loop para gerar a certidão para cada processos
				for (ProcessoEAndamentoProcesso pap : containerGuia.getProcessosEAndamentosProcessos()){
					
				
					Processo processo = pap.getProcesso();
					AndamentoProcesso andamentoProcesso = pap.getAndamentoProcesso();
					AndamentoProcessoInfo andamentoProcessoInfo = pap.getAndamentoProcessoInfo();
			
					try {
						String idProcesso = processo.getSiglaClasseProcessual() + " " + processo.getNumeroProcessual();
						String descOrigem = "";
						String titulo = "";
						String corpo = "";
						String siglaTipoPeca = "";
						String nomeSecretario = AbstractBeanRegistrarAndamento.NOME_SECRETARIO;
						String descricaoCargo = AbstractBeanRegistrarAndamento.DESCRICAO_CARGO;
						
						if (andamentoProcessoInfo.getOrigem() != null) {
							descOrigem = andamentoProcessoInfo.getOrigem().getDescricao();
						}
						
						if (andamentoProcessoInfo.getAndamento().getId().equals(7104L)) {
							titulo = "TERMO DE BAIXA DEFINITIVA";
							corpo = "Faço a baixa deste processo e a transmissão eletrônica das peças processuais ao (à) " + descOrigem + ".";
							siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_TERMO_BAIXA;
						} else if (andamentoProcessoInfo.getAndamento().getId().equals(7101L) || andamentoProcessoInfo.getAndamento().getId().equals(7108L)) {
							titulo = "TERMO DE REMESSA EXTERNA";
							corpo = "Faço a remessa destes autos com a transmissão eletrônica das peças processuais ao (à) " + descOrigem + ".";
							siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_TERMO_REMESSA;
						}
						if (siglaTipoPeca.equals("")) {
							throw new ServiceException("Não foi possível recuperar a sigla do tipo da peça.");
						}
						
						ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperarPorId(processo.getId()); 
						
						// Recuperar o caminho do brasao
						String pathBrasao = processamentoRelatorioService.recuperarPathImagens("images/brasao.gif"); 
						String pathAssinatura = processamentoRelatorioService.recuperarPathImagens("images/assinatura_secretario_judiciario.JPG"); 
						
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("PATH_BRASAO", pathBrasao);
						params.put("PATH_ASSINATURA", pathAssinatura);
						params.put("ID_PROCESSO", idProcesso);
						params.put("TITULO", titulo);
						params.put("CORPO", corpo);
						params.put("NOME_SECRETARIO", nomeSecretario);
						params.put("DESCRICAO_CARGO", descricaoCargo);
				
						String relatorio = "relatorios/RelatorioTermoBaixa.jasper";
						System.out.println("relatorio:"+relatorio);
						System.out.println("PATH_BRASAO:"+params.get("PATH_BRASAO"));
						System.out.println("PATH_ASSINATURA:"+params.get("PATH_ASSINATURA"));
						System.out.println("ID_PROCESSO:"+params.get("ID_PROCESSO"));
						System.out.println("TITULO:"+params.get("TITULO"));
						System.out.println("CORPO:"+params.get("CORPO"));
						System.out.println("NOME_SECRETARIO:"+params.get("NOME_SECRETARIO"));
						System.out.println("DESCRICAO_CARGO:"+params.get("DESCRICAO_CARGO"));
						byte[] resultRel = UtilJasperReports.criarRelatorioPdf(relatorio, null, params);
						
						String nomeArq = "TERMO_BAIXA_"+idProcesso.replaceAll(" ", "") + "_";
						
						if (resultRel!=null && andamentoProcessoInfo !=null){
							
							arquivoProcessoEletronicoService.salvarPecaEletronica(resultRel, siglaTipoPeca, objetoIncidente, setor, andamentoProcesso);
							String nomePDFBaixa = processamentoRelatorioService.gerarPDF(nomeArq, resultRel);
							andamentoProcesso.setListaTextoAndamentoProcessos(textoAndamentoProcessoService.recuperarTextoAndamentoProcesso(andamentoProcesso.getId(),null));
						
						} else{
							throw new ServiceException("Ocorreu um erro ao montar o Termo de Baixa/Remessa!");
						}
					} catch (IOException e) {
						throw new ServiceException("Ocorreu um erro ao montar o pdf do Termo de Baixa/Remessa!", e);
					} catch (JRException e) {
						throw new ServiceException("Ocorreu um erro ao montar o pdf do Termo de Baixa/Remessa!", e);
					} catch (Exception e) {
						throw new ServiceException("Ocorreu um erro ao gerar o Termo de Baixa/Remessa!", e);
					}
				}
			}
		}
}
