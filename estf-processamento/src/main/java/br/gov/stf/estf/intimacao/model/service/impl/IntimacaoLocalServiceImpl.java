package br.gov.stf.estf.intimacao.model.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento;
import br.gov.stf.estf.intimacao.model.dataaccess.ComunicacaoLocalDao;
import br.gov.stf.estf.intimacao.model.dto.ImpressaoDocumentoDto;
import br.gov.stf.estf.intimacao.model.dto.ProcessoPecaDto;
import br.gov.stf.estf.intimacao.model.service.IntimacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.ModeloComunicacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.TipoComunicacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.exception.AndamentoNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoGerouIntimacaoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteNaoRatificadaException;
import br.gov.stf.estf.intimacao.model.service.exception.ParteSemAceiteInitmacaoEletronicaException;
import br.gov.stf.estf.intimacao.model.service.exception.PecaNaoPertencenteProcessoException;
import br.gov.stf.estf.intimacao.model.service.exception.PessoaSemUsuarioException;
import br.gov.stf.estf.intimacao.model.service.exception.ProcessoNaoEletronicoException;
import br.gov.stf.estf.intimacao.model.service.exception.TipoModeloComunicacaoEnumInvalidoException;
import br.gov.stf.estf.intimacao.model.vo.TipoRecebimentoComunicacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ComunicacaoExternaDTO;
import br.gov.stf.estf.intimacao.visao.dto.ParteProcessoIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoComunicacaoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfoImpl;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.util.jasperreports.UtilJasperReports;

/**
 *
 * @author Roberio.Fernandes
 */
@Service("intimacaoLocalService")
public class IntimacaoLocalServiceImpl implements IntimacaoLocalService {

	 	@Autowired
	    private ModeloComunicacaoLocalService modeloComunicacaoServiceLocal;
	    @Autowired
	    private ComunicacaoServiceLocal comunicacaoServiceLocal;
	    @Autowired
	    private TipoComunicacaoLocalService tipoComunicacaoServiceLocal;
		@Autowired
		private ObjetoIncidenteService objetoIncidenteService;
		@Autowired
		private ProcessoService processoService;
		@Autowired
		private AndamentoProcessoService andamentoProcessoService;
		@Autowired
		private AndamentoProcessoComunicacaoService andamentoProcessoComunicacaoService;
		@Autowired
		private AndamentoService andamentoService;
		@Autowired
		private ComunicacaoLocalDao comunicacaoLocalDao;
		

	    @Override
	    public List<Comunicacao> criarIntimacoesFisicas(String usuarioCriador,
	            ModeloComunicacaoEnum modeloComunicacaoEnum, Date dataIntimacao,
	            Parte parte, Setor setor,
	            Map<String, List<PecaDTO>> mapProcessos,
	            String responsavel, String cargoResponsavel, Long numeroDJ,
	            TipoMeioProcesso tipoMeioProcesso)
	            throws ParteNaoPertencenteProcessoException,
	            ParteNaoGerouIntimacaoException, ServiceException {
	        List<Comunicacao> listComunicacao = new ArrayList<Comunicacao>();

	        Map<Long, List<PecaDTO>> mapSetorProcesso = mapearSetorProcesso(mapProcessos);

	        List<ImpressaoDocumentoDto> listImpressaoDocumentoDto = montarObjetoImpressao(mapSetorProcesso,
	                parte,
	                dataIntimacao,
	                modeloComunicacaoEnum,
	                numeroDJ);

	        for (ImpressaoDocumentoDto impressaoDocumentoDto : listImpressaoDocumentoDto) {
	            try {
	                gerarPDF(listComunicacao,
	                        impressaoDocumentoDto,
	                        0,
	                        setor,
	                        responsavel,
	                        cargoResponsavel,
	                        usuarioCriador, tipoMeioProcesso);
	            } catch (DaoException de) {
	                throw new ServiceException(de);
	            }
	        }

	        return listComunicacao;
	    }

	    private Map<Long, List<PecaDTO>> mapearSetorProcesso(Map<String, List<PecaDTO>> processos) {
	        Map<Long, List<PecaDTO>> mapSetor = new TreeMap<Long, List<PecaDTO>>();
	        for (Map.Entry<String, List<PecaDTO>> entry : processos.entrySet()) {
	            List<PecaDTO> listaPeca = entry.getValue();
	            for (PecaDTO pecaDto : listaPeca) {
	                Processo processo = (Processo) pecaDto.getObjetoIncidente();
	                if (mapSetor.get(processo.getSetorRecebimento().getId()) == null) {
	                    List<PecaDTO> listaProcesso = new ArrayList<PecaDTO>();
	                    pecaDto.setSetor(processo.getSetorRecebimento());
	                    listaProcesso.add(pecaDto);
	                    mapSetor.put(processo.getSetorRecebimento().getId(),
	                            listaProcesso);
	                } else {
	                    List<PecaDTO> listaProcesso = mapSetor.get(processo.getSetorRecebimento().getId());
	                    pecaDto.setSetor(processo.getSetorRecebimento());
	                    listaProcesso.add(pecaDto);
	                    mapSetor.put(processo.getSetorRecebimento().getId(), listaProcesso);
	                }
	            }
	        }
	        return mapSetor;
	    }

	    private List<ImpressaoDocumentoDto> montarObjetoImpressao(Map<Long, List<PecaDTO>> map,
	            Parte parte,
	            Date dataIntimacao,
	            ModeloComunicacaoEnum modeloComunicacaoEnum,
	            Long numeroDJ) {
	        List<ImpressaoDocumentoDto> listImpressaoDocumentoDto = new ArrayList<ImpressaoDocumentoDto>();
	        DateFormat formatador = DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR"));
	        for (Map.Entry<Long, List<PecaDTO>> entry : map.entrySet()) {
	            List<PecaDTO> pecas = entry.getValue();
	            PecaDTO pecaDto = pecas.get(0);
	            ImpressaoDocumentoDto impressaoDocumentoDto = new ImpressaoDocumentoDto();
	            impressaoDocumentoDto.setNumeroDJ(numeroDJ);
	            impressaoDocumentoDto.setSeqParteProcessual(parte.getId());
	            impressaoDocumentoDto.setNomeParte(parte.getNomeJurisdicionado());
	            impressaoDocumentoDto.setDataDivulgacaoExtenso(formatador.format(dataIntimacao));
	            impressaoDocumentoDto.setDataAtualExtenso(formatador.format(new Date()));
	            impressaoDocumentoDto.setDataDivulgacao(dataIntimacao);
	            impressaoDocumentoDto.setModeloComunicacaoEnum(modeloComunicacaoEnum);
	            impressaoDocumentoDto.setDescricaoSetor(pecaDto.getSetor().getNome());
	            impressaoDocumentoDto.setPecas(pecas);
	            listImpressaoDocumentoDto.add(impressaoDocumentoDto);
	        }
	        return listImpressaoDocumentoDto;
	    }

	    private void gerarPDF(List<Comunicacao> listComunicacao,
	            ImpressaoDocumentoDto impressaoDocumentoDto,
	            int indexProcesso,
	            Setor setor,
	            String responsavel,
	            String cargoResponsavel,
	            String usuarioCriador, TipoMeioProcesso tipoMeioProcesso) throws ServiceException, DaoException {
	        try {
	            Processo processo = (Processo) impressaoDocumentoDto.getPecas().get(0).getObjetoIncidente();

	            String descricaoComunicacao = impressaoDocumentoDto
	                    .getModeloComunicacaoEnum().getDescricaoModelo()
	                    + " - Processo "
	                    + processo.getTipoMeioProcesso().getDescricao();

	            ModeloComunicacao modeloComunicacao = modeloComunicacaoServiceLocal.buscar(impressaoDocumentoDto.getModeloComunicacaoEnum());

	            StringBuilder descricaoSetor = formartarDescricaoSetor(impressaoDocumentoDto.getDescricaoSetor());
	            StringBuilder relatorio = new StringBuilder("relatorios/");
	            int quantidadeProcessoPorDocumento = carregarNomeRelatorioDescricaoSetor(impressaoDocumentoDto, relatorio, descricaoSetor, tipoMeioProcesso);
	            relatorio.append(".jasper");

	            HashMap<String, Object> params = new HashMap<String, Object>();

	            Long numeroDocumento = tipoComunicacaoServiceLocal.gerarProximoNumeroComunicacao(modeloComunicacao.getTipoComunicacao().getId());

	            StringBuilder numeroDocumentoFormatado = new StringBuilder(String.valueOf(numeroDocumento));
	            numeroDocumentoFormatado.append("/");
	            numeroDocumentoFormatado.append(new SimpleDateFormat("yyyy").format(new Date()));

	            ClassLoader carregarImagem = Thread.currentThread().getContextClassLoader();
	            params.put("brasaoRepublicaSTF", carregarImagem.getResource("relatorios/Brasao_Republica_STF.jpg").toString());

	            params.put("nomeParte", impressaoDocumentoDto.getNomeParte());
	            params.put("numeroDJe", (impressaoDocumentoDto.getNumeroDJ() != null ? impressaoDocumentoDto.getNumeroDJ() : "S/N"));
	            params.put("dataDivulgacaoDJ", impressaoDocumentoDto.getDataDivulgacaoExtenso().toLowerCase());

	            params.put("dataEmissao", impressaoDocumentoDto.getDataAtualExtenso().toLowerCase());
	            params.put("numeroDocumento", numeroDocumentoFormatado);

	            params.put("nomeOcupanteCargo", responsavel);
	            params.put("cargoOcupante", cargoResponsavel);

	            params.put("turmaSecao", "(" + descricaoSetor.toString() + ")");

	            List<ParteProcessoIntimacaoDto> dados = new ArrayList<ParteProcessoIntimacaoDto>();
	            List<Long> idsObjetoIncidente = new ArrayList<Long>();
	            List<ProcessoPecaDto> listProcessoPecaDtoCompleta =  listaProcessoImpressao(impressaoDocumentoDto.getPecas());
	            
	            List<ProcessoPecaDto> listaProcessoPeca = carregarDados(listProcessoPecaDtoCompleta, indexProcesso, quantidadeProcessoPorDocumento, idsObjetoIncidente, dados);
	            
	            byte[] arquivo = UtilJasperReports.criarRelatorioPdf(relatorio.toString(), dados, params);

	            List<PecaDTO> pecas = new ArrayList<PecaDTO>();
	            for(int i=0; i<listaProcessoPeca.size(); i++){
	            	PecaDTO peca = listaProcessoPeca.get(i).getPeca();    
	            	pecas.add(peca);
	            }
	            
	            Comunicacao comunicao = comunicacaoServiceLocal.criarComunicacaoIntimacao(impressaoDocumentoDto.getDataDivulgacao(),
	                            usuarioCriador,
	                            setor,
	                            null,
	                            impressaoDocumentoDto.getModeloComunicacaoEnum(),
	                            idsObjetoIncidente,
	                            TipoFaseComunicacao.PDF_GERADO,
	                            pecas,
	                            new ArrayList<AndamentoProcesso>(),
	                            numeroDocumento,
	                            descricaoComunicacao,
	                            arquivo);

	            listComunicacao.add(comunicao);
	            
	            indexProcesso++;
	            if ((indexProcesso * quantidadeProcessoPorDocumento) < listProcessoPecaDtoCompleta.size()) {
	                gerarPDF(listComunicacao,
	                        impressaoDocumentoDto,
	                        indexProcesso,
	                        setor,
	                        responsavel,
	                        cargoResponsavel,
	                        usuarioCriador, tipoMeioProcesso);
	            }
	        } catch (DaoException de) {
	            throw new DaoException(de);
	        } catch (ServiceException se) {
	            throw new ServiceException(se);
	        } catch (Exception e) {
	            throw new ServiceException(e);
	        }
	    }

		private void gerarPDF(List<Comunicacao> listComunicacao,
				ImpressaoDocumentoDto impressaoDocumentoDto, int indexProcesso,
				Setor setor, String responsavel, String cargoResponsavel,
				String usuarioCriador, DocumentoEletronico documentoAcordao, TipoMeioProcesso tipoMeioProcesso)
				throws ServiceException, DaoException {
			try {
				Processo processo = (Processo) impressaoDocumentoDto.getPecas()
						.get(0).getObjetoIncidente();

				String descricaoComunicacao = impressaoDocumentoDto
						.getModeloComunicacaoEnum().getDescricaoModelo()
						+ " - Processo "
						+ processo.getTipoMeioProcesso().getDescricao();

				ModeloComunicacao modeloComunicacao = modeloComunicacaoServiceLocal.buscar(impressaoDocumentoDto.getModeloComunicacaoEnum());

				StringBuilder descricaoSetor = formartarDescricaoSetor(impressaoDocumentoDto
						.getDescricaoSetor());
				StringBuilder relatorio = new StringBuilder("relatorios/");
				int quantidadeProcessoPorDocumento = carregarNomeRelatorioDescricaoSetor(
						impressaoDocumentoDto, relatorio, descricaoSetor, tipoMeioProcesso);
				relatorio.append(".jasper");

				HashMap<String, Object> params = new HashMap<String, Object>();

				Long numeroDocumento = tipoComunicacaoServiceLocal
						.gerarProximoNumeroComunicacao(modeloComunicacao
								.getTipoComunicacao().getId());

				StringBuilder numeroDocumentoFormatado = new StringBuilder(
						String.valueOf(numeroDocumento));
				numeroDocumentoFormatado.append("/");
				numeroDocumentoFormatado.append(new SimpleDateFormat("yyyy")
						.format(new Date()));

				ClassLoader carregarImagem = Thread.currentThread()
						.getContextClassLoader();
				params.put(
						"brasaoRepublicaSTF",
						carregarImagem.getResource(
								"relatorios/Brasao_Republica_STF.jpg").toString());

				params.put("nomeParte", impressaoDocumentoDto.getNomeParte());
				params.put(
						"numeroDJe",
						(impressaoDocumentoDto.getNumeroDJ() != null ? impressaoDocumentoDto
								.getNumeroDJ() : "S/N"));
				params.put("dataDivulgacaoDJ", impressaoDocumentoDto
						.getDataDivulgacaoExtenso().toLowerCase());

				params.put("dataEmissao", impressaoDocumentoDto
						.getDataAtualExtenso().toLowerCase());
				params.put("numeroDocumento", numeroDocumentoFormatado);

				params.put("nomeOcupanteCargo", responsavel);
				params.put("cargoOcupante", cargoResponsavel);

				params.put("turmaSecao", "(" + descricaoSetor.toString() + ")");

				List<ParteProcessoIntimacaoDto> dados = new ArrayList<ParteProcessoIntimacaoDto>();
				List<ProcessoPecaDto> listProcessoPecaDtoCompleta = listaProcessoImpressao(impressaoDocumentoDto
						.getPecas());

				List<Processo> processos = new ArrayList<Processo>();
				List<ProcessoPecaDto> listaProcessoPeca = carregarDadosProc(
						listProcessoPecaDtoCompleta, indexProcesso,
						quantidadeProcessoPorDocumento, processos, dados);
				Collections.sort(processos, new Comparator<Processo>() {
					@Override
					public int compare(Processo processo1, Processo processo2) {
						String classeNumeroProcesso1 = processo1
								.getClasseProcessual().getId()
								+ " "
								+ processo1.getNumeroProcessual();
						String classeNumeroProcesso2 = processo2
								.getClasseProcessual().getId()
								+ " "
								+ processo2.getNumeroProcessual();
						return classeNumeroProcesso1
								.compareToIgnoreCase(classeNumeroProcesso2
										.toUpperCase());
					}
				});
				List<Long> idsObjetoIncidente = new ArrayList<Long>();
				for (Processo processoOrdenado : processos) {
					idsObjetoIncidente.add(processoOrdenado.getId());
				}

				byte[] arquivo = UtilJasperReports.criarRelatorioPdf(
						relatorio.toString(), dados, params);

				List<PecaDTO> pecas = new ArrayList<PecaDTO>();
				for (int indice = 0; indice < listaProcessoPeca.size(); indice++) {
					PecaDTO peca = listaProcessoPeca.get(indice).getPeca();
					pecas.add(peca);
				}

				Comunicacao comunicacao = comunicacaoServiceLocal
						.criarComunicacaoIntimacao(
								impressaoDocumentoDto.getDataDivulgacao(), setor,
								usuarioCriador,
								impressaoDocumentoDto.getSeqPessoa(),
								impressaoDocumentoDto.getModeloComunicacaoEnum(),
								idsObjetoIncidente, TipoFaseComunicacao.PDF_GERADO,
								pecas, new ArrayList<AndamentoProcesso>(),
								numeroDocumento, descricaoComunicacao, arquivo,
								documentoAcordao);
				
				comunicacaoServiceLocal.associarAndamentoProcessoComunicacao( new ArrayList<AndamentoProcesso>(), comunicacao);
				
				
				FlagProcessoLote flagProcessoLote = FlagProcessoLote.P;
		        for (Long idObjetoIncidente : idsObjetoIncidente) {
		        	comunicacaoServiceLocal.criarComunicacaoObjetoIncidente(idObjetoIncidente, comunicacao, flagProcessoLote);
		            flagProcessoLote = FlagProcessoLote.V;
		        }
				
				

				listComunicacao.add(comunicacao);

				indexProcesso++;
				if ((indexProcesso * quantidadeProcessoPorDocumento) < listProcessoPecaDtoCompleta
						.size()) {
					gerarPDF(listComunicacao, impressaoDocumentoDto, indexProcesso,
							setor, responsavel, cargoResponsavel, usuarioCriador,
							documentoAcordao, tipoMeioProcesso);
				}
			} catch (DaoException de) {
				throw new DaoException(de);
			} catch (ServiceException se) {
				throw new ServiceException(se);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
		
	    
	    private int carregarNomeRelatorioDescricaoSetor(ImpressaoDocumentoDto impressaoDocumentoDto, StringBuilder relatorio, StringBuilder descricaoSetor,
	    		TipoMeioProcesso tipoMeioProcesso) {
	        int quantidadeProcessoPorDocumento = 0;
	        if (impressaoDocumentoDto.getModeloComunicacaoEnum() == ModeloComunicacaoEnum.MANDADO) {
	            quantidadeProcessoPorDocumento = 20;
	            relatorio.append("mandadoDeIntimacao");
	            if (((Processo) impressaoDocumentoDto.getPecas().get(0).getObjetoIncidente()).getTipoMeioProcesso() == TipoMeioProcesso.ELETRONICO) {
	                descricaoSetor.append(" - ");
	                descricaoSetor.append(TipoMeioProcesso.ELETRONICO.getCodigo());
	            }
	        } else if (impressaoDocumentoDto.getModeloComunicacaoEnum() == ModeloComunicacaoEnum.CARTA) {
	            quantidadeProcessoPorDocumento = 15;
	            if (TipoMeioProcesso.ELETRONICO.equals(tipoMeioProcesso))
	            	relatorio.append("cartaDeIntimacaoEletronica");
	            else
	            	relatorio.append("cartaDeIntimacaoFisica");
	            if (((Processo) impressaoDocumentoDto.getPecas().get(0).getObjetoIncidente()).getTipoMeioProcesso() == TipoMeioProcesso.ELETRONICO) {
	                descricaoSetor.append(" - ");
	                descricaoSetor.append(TipoMeioProcesso.ELETRONICO.getCodigo());
	            }
	        }
	        return quantidadeProcessoPorDocumento;
	    }

	    private List<ProcessoPecaDto> carregarDados(List<ProcessoPecaDto> listProcessoPecaDto,
	            int indexProcesso,
	            int quantidadeProcessoPorDocumento,
	            List<Long> idsObjetoIncidente,
	            List<ParteProcessoIntimacaoDto> dados) {
	        int indice = 1;
	        List<ProcessoPecaDto> listProcessoPecaDto2 =  new ArrayList<ProcessoPecaDto>();
	        for (int indiceProcesso = (indexProcesso * quantidadeProcessoPorDocumento); indiceProcesso < listProcessoPecaDto.size(); indiceProcesso++) {
	            if (indiceProcesso < (indexProcesso + 1) * quantidadeProcessoPorDocumento) {
	                ParteProcessoIntimacaoDto parteProcessoIntimacaoDto = new ParteProcessoIntimacaoDto();
	                Processo processo1 = listProcessoPecaDto.get(indiceProcesso).getProcesso();
	                idsObjetoIncidente.add(processo1.getId());
	                parteProcessoIntimacaoDto.setNomeProcesso(indice
	                        + "."
	                        + processo1.getClasseProcessual().getDescricao().toUpperCase()
	                        + " nº "
	                        + processo1.getNumeroProcessual());
	                dados.add(parteProcessoIntimacaoDto);
	                listProcessoPecaDto2.add(listProcessoPecaDto.get(indiceProcesso));
	            } else {
	                break;
	            }
	            indice++;
	        }
	        return listProcessoPecaDto2;
	    }

		private List<ProcessoPecaDto> carregarDadosProc(
				List<ProcessoPecaDto> listProcessoPecaDto, int indexProcesso,
				int quantidadeProcessoPorDocumento, List<Processo> processos,
				List<ParteProcessoIntimacaoDto> dados) {
			int indice = 1;
			List<ProcessoPecaDto> listProcessoPecaDto2 = new ArrayList<ProcessoPecaDto>();
			Set<String> processosSelecionados = new HashSet<String>();
			Set<Long> idsObjetoIncidente = new HashSet<Long>();
			for (int indiceProcesso = (indexProcesso * quantidadeProcessoPorDocumento); indiceProcesso < listProcessoPecaDto
					.size(); indiceProcesso++) {
				if (indiceProcesso < (indexProcesso + 1)
						* quantidadeProcessoPorDocumento) {
					ParteProcessoIntimacaoDto parteProcessoIntimacaoDto = new ParteProcessoIntimacaoDto();
					Processo processo = listProcessoPecaDto.get(indiceProcesso)
							.getProcesso();
					listProcessoPecaDto2.add(listProcessoPecaDto
							.get(indiceProcesso));
					if (idsObjetoIncidente.add(processo.getId())) {
						processos.add(processo);
						String descricao = processo.getClasseProcessual()
								.getDescricao().toUpperCase()
								+ " nº " + processo.getNumeroProcessual();
						if (!processosSelecionados.contains(descricao)) {
							processosSelecionados.add(descricao);
							parteProcessoIntimacaoDto.setNomeProcesso(indice + "."
									+ descricao);
							dados.add(parteProcessoIntimacaoDto);
							indice++;
						}
						
					}
				} else {
					break;
				}
			}
			return listProcessoPecaDto2;
		}
	    
	    private List<ProcessoPecaDto> listaProcessoImpressao(List<PecaDTO> pecas) {
	    	List<ProcessoPecaDto> listProcessoPecaDto = new ArrayList<ProcessoPecaDto>();
	        for (PecaDTO pecaDto : pecas) {
	            ProcessoPecaDto processoPecaDto = new ProcessoPecaDto();
	            processoPecaDto.setProcesso((Processo) pecaDto.getObjetoIncidente());
	            processoPecaDto.setPeca(pecaDto);
	            listProcessoPecaDto.add(processoPecaDto);
	        }
	       return listProcessoPecaDto;
	    }

	    private StringBuilder formartarDescricaoSetor(String descricaoSetor) {
	        StringBuilder descricaoSetorFormatado = new StringBuilder(descricaoSetor.toLowerCase());
	        for (int i = 0; i < descricaoSetorFormatado.toString().length(); i++) {
	            if (i == 0 || descricaoSetorFormatado.toString().charAt(i - 1) == ' ') {
	                descricaoSetorFormatado.replace(i, (i + 1), String.valueOf(descricaoSetor.charAt(i)).toUpperCase());
	            }
	        }

	        return descricaoSetorFormatado;
	    }


	@Override
	public Comunicacao criarIntimacoesEletronicas(String usuarioCriador,
			Setor setor, Date dataIntimacao,
			ModeloComunicacaoEnum modeloComunicacaoEnum,
			ObjetoIncidente objetoIncidente, List<Parte> partes,
			Set<ArquivoProcessoEletronico> pecas, Set<Andamento> andamentos)
			throws TipoModeloComunicacaoEnumInvalidoException,
			ProcessoNaoEletronicoException,
			ParteSemAceiteInitmacaoEletronicaException,
			ParteNaoRatificadaException, ParteNaoPertencenteProcessoException,
			PecaNaoPertencenteProcessoException,
			AndamentoNaoPertencenteProcessoException, ServiceException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Comunicacao> criarIntimacoesFisicas(long pessoaDestinatario,
			String usuarioCriador, ModeloComunicacaoEnum modeloComunicacaoEnum,
			Date dataIntimacao, Parte parte, Setor setor,
			Collection<PecaDTO> pecasProcessosIntimar, String responsavel,
			String cargoResponsavel, Long numeroDJ,
			DocumentoEletronico documentoAcordao, TipoMeioProcesso tipoMeioProcesso)
			throws ParteNaoPertencenteProcessoException,
			ParteNaoGerouIntimacaoException, ServiceException {
		List<Comunicacao> listComunicacao = new ArrayList<Comunicacao>();
		
		Map<Long, List<PecaDTO>> mapSetorProcesso = mapearSetorProcesso(pecasProcessosIntimar);

		List<ImpressaoDocumentoDto> listImpressaoDocumentoDto = montarObjetoImpressao(
				mapSetorProcesso, pessoaDestinatario, parte, dataIntimacao,
				modeloComunicacaoEnum, numeroDJ);

		for (ImpressaoDocumentoDto impressaoDocumentoDto : listImpressaoDocumentoDto) {
			try {
				gerarPDF(listComunicacao, impressaoDocumentoDto, 0, setor,
						responsavel, cargoResponsavel, usuarioCriador,
						documentoAcordao, tipoMeioProcesso);
			} catch (DaoException de) {
				throw new ServiceException(de);
			}
		}

		return listComunicacao;
	}
	
	private Map<Long, List<PecaDTO>> mapearSetorProcesso(
			Collection<PecaDTO> pecasProcessosIntimar) {
		Map<Long, List<PecaDTO>> mapSetor = new TreeMap<Long, List<PecaDTO>>();
		for (PecaDTO pecaDto : pecasProcessosIntimar) {
			Processo processo = (Processo) pecaDto.getObjetoIncidente();
			if (mapSetor.get(processo.getSetorRecebimento().getId()) == null) {
				List<PecaDTO> listaProcesso = new ArrayList<PecaDTO>();
				pecaDto.setSetor(processo.getSetorRecebimento());
				listaProcesso.add(pecaDto);
				mapSetor.put(processo.getSetorRecebimento().getId(),
						listaProcesso);
			} else {
				List<PecaDTO> listaProcesso = mapSetor.get(processo
						.getSetorRecebimento().getId());
				listaProcesso.add(pecaDto);
			}
		}
		return mapSetor;
	}

	private List<ImpressaoDocumentoDto> montarObjetoImpressao(
			Map<Long, List<PecaDTO>> map, long pessoaDestinatario, Parte parte,
			Date dataIntimacao, ModeloComunicacaoEnum modeloComunicacaoEnum,
			Long numeroDJ) {
		List<ImpressaoDocumentoDto> listImpressaoDocumentoDto = new ArrayList<ImpressaoDocumentoDto>();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.LONG,
				new Locale("pt", "BR"));
		for (Map.Entry<Long, List<PecaDTO>> entry : map.entrySet()) {
			List<PecaDTO> pecas = entry.getValue();
			PecaDTO pecaDto = pecas.get(0);
			ImpressaoDocumentoDto impressaoDocumentoDto = new ImpressaoDocumentoDto();
			impressaoDocumentoDto.setNumeroDJ(numeroDJ);
			impressaoDocumentoDto.setSeqPessoa(pessoaDestinatario);
			impressaoDocumentoDto.setSeqParteProcessual(parte.getId());
			impressaoDocumentoDto.setNomeParte(parte.getNomeJurisdicionado());
			impressaoDocumentoDto.setDataDivulgacaoExtenso(formatador
					.format(dataIntimacao));
			impressaoDocumentoDto.setDataAtualExtenso(formatador
					.format(new Date()));
			impressaoDocumentoDto.setDataDivulgacao(dataIntimacao);
			impressaoDocumentoDto.setModeloComunicacaoEnum(modeloComunicacaoEnum);
			impressaoDocumentoDto.setDescricaoSetor(pecaDto.getSetor()
					.getNome());
			impressaoDocumentoDto.setPecas(pecas);
			listImpressaoDocumentoDto.add(impressaoDocumentoDto);
		}
		return listImpressaoDocumentoDto;
	}
	

	@Override
	public Comunicacao criarIntimacao(String usuarioCriador, Setor setor,
			Date dataIntimacao, ModeloComunicacaoEnum modeloComunicacaoEnum,
			Set<Long> objetoIncidente, Long codigoPessoaDestinatario,
			List<PecaProcessoEletronico> pecas,
			List<AndamentoProcesso> andamentos,
			TipoFaseComunicacao tipoFaseComunicacao,
			String descricaoComunicacao, DocumentoEletronico documentoAcordao)
			throws TipoModeloComunicacaoEnumInvalidoException,
			ProcessoNaoEletronicoException,
			ParteSemAceiteInitmacaoEletronicaException,
			PessoaSemUsuarioException, ParteNaoPertencenteProcessoException,
			PecaNaoPertencenteProcessoException,
			AndamentoNaoPertencenteProcessoException, ServiceException {
		if (codigoPessoaDestinatario == null) {
			throw new PessoaSemUsuarioException();
		}

		ModeloComunicacao modeloComunicacao = modeloComunicacaoServiceLocal.buscar(modeloComunicacaoEnum);
		TipoComunicacao tipoComunicacao = modeloComunicacao.getTipoComunicacao();
		Long numeroDocumento = tipoComunicacaoServiceLocal.gerarProximoNumeroComunicacao(tipoComunicacao.getId());

		Comunicacao comunicacao = comunicacaoServiceLocal.criarComunicacaoIntimacao(dataIntimacao, setor,
				usuarioCriador, codigoPessoaDestinatario, modeloComunicacaoEnum, objetoIncidente, tipoFaseComunicacao,
				pecas, andamentos, numeroDocumento, descricaoComunicacao, null, documentoAcordao);

		comunicacaoServiceLocal.associarAndamentoProcessoComunicacao(andamentos, comunicacao);
		
		FlagProcessoLote flagProcessoLote = FlagProcessoLote.P;
		
        for (Long idObjetoIncidente : objetoIncidente) {
        	comunicacaoServiceLocal.criarComunicacaoObjetoIncidente(idObjetoIncidente, comunicacao, flagProcessoLote);
            flagProcessoLote = FlagProcessoLote.V;
        }
        
		if (!ModeloComunicacaoEnum.NOTIFICACAO_DE_PAUTA.equals(modeloComunicacaoEnum))
			geraAndamentoComunicacao(comunicacao, setor, usuarioCriador, objetoIncidente);

		return comunicacao;
	}

	private void geraAndamentoComunicacao(Comunicacao comunicacao, Setor setor,
			String usuarioCriador, Collection<Long> objetosIncidente) {
		try {
			AndamentoProcessoInfoImpl andamentoProcessoInfo = montarAndamentoProcessoInfo(
					comunicacao, setor, usuarioCriador);

			ObjetoIncidente<?> objetoIncidente = null;

			objetoIncidente = objetoIncidenteService
					.recuperarPorId(new ArrayList<Long>(objetosIncidente)
							.get(0));

			Processo processoPrincipal = processoService
					.recuperarPorId(objetoIncidente.getPrincipal().getId());

			AndamentoProcesso andamentoProcesso = andamentoProcessoService
					.salvarAndamento(andamentoProcessoInfo, processoPrincipal,
							objetoIncidente);

			AndamentoProcessoComunicacao andamentoProcessoComunicacao = new AndamentoProcessoComunicacao();
			andamentoProcessoComunicacao.setComunicacao(comunicacao);
			andamentoProcessoComunicacao
					.setAndamentoProcesso(andamentoProcesso);
			andamentoProcessoComunicacao
					.setTipoVinculoAndamento(TipoVinculoAndamento.GERADO);

			andamentoProcessoComunicacaoService
					.salvar(andamentoProcessoComunicacao);

		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	private AndamentoProcessoInfoImpl montarAndamentoProcessoInfo(
			Comunicacao comunicacao, Setor setor, String usuarioCriador)
			throws ServiceException {

		Andamento andamentoComunicacao = null;

		if (comunicacao.getModeloComunicacao().getDscModelo()
				.equals(ModeloComunicacaoEnum.CITACAO.getDescricaoModelo())) {
			andamentoComunicacao = andamentoService
					.recuperarPorId(AssinadorBaseBean.CODIGO_CITACAO_ELETRONICA_DISPONIBILIZADA);
		} else
			andamentoComunicacao = andamentoService
					.recuperarPorId(AssinadorBaseBean.CODIGO_INTIMACAO_ELETRONICA_DISPONIBILIZADA);

		String observacaoAndamento = comunicacao.getModeloComunicacao()
				.getDscModelo()
				+ " - "
				+ comunicacao.getPessoaDestinataria().getNome();

		AndamentoProcessoInfoImpl andamentoProcessoInfo = new AndamentoProcessoInfoImpl();
		andamentoProcessoInfo.setAndamento(andamentoComunicacao);
		andamentoProcessoInfo.setCodigoUsuario(usuarioCriador.toUpperCase());
		andamentoProcessoInfo.setSetor(setor);
		andamentoProcessoInfo.setObservacao(observacaoAndamento);

		return andamentoProcessoInfo;
	}
	
	
	@Override
	public List<ComunicacaoExternaDTO> pesquisarComunicacaoExterna(
			String idParte,
			TipoRecebimentoComunicacaoEnum tipoRecebimentoComunicacaoEnum,
			String descricaoTipoComunicacao, String descricaoModelo,
			Date periodoEnvioInicio, Date periodoEnvioFim, Long idProcesso,
			Long idPreferemcia) throws ServiceException {

		List<ComunicacaoExternaDTO> lista = comunicacaoServiceLocal
				.pesquisarComunicacaoExterna(idParte,
						tipoRecebimentoComunicacaoEnum,
						descricaoTipoComunicacao, descricaoModelo,
						periodoEnvioInicio, periodoEnvioFim, idProcesso,
						idPreferemcia);

		return lista;
	}

	@Override
	public List<TipoIncidentePreferencia> buscaTodasPreferencias()
			throws DaoException {
		return comunicacaoLocalDao.buscaTodasPreferencias();
	}
}
