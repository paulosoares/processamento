package br.gov.stf.estf.expedicao.model.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.correios.dto.DestinatarioDto;
import br.gov.stf.estf.correios.dto.PrePostagemResquest;
import br.gov.stf.estf.correios.dto.RemetenteDto;
import br.gov.stf.estf.correios.dto.ServicoAdicionalDto;
import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.entidade.RemessaVolume;
import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.model.service.PostagemService;
import br.gov.stf.framework.model.service.ServiceException;

@Service("postagemServiceImpl")
public class PostagemServiceImpl implements PostagemService {

	@Autowired
	private CorreiosService correiosService;
	
	@Override
	public void gerarEtiquetas(ListaRemessa listaRemessa) throws ServiceException {
		String codigoServico = listaRemessa.getTipoServico().getCodigoServicoCorreios();
	
		if (!correiosService.getServicos().contains(codigoServico))
			throw new ServiceException("Código de serviço dos correios não encontrado no contrato dos correios ('" + codigoServico + "')");
		
		// Adiciona a etiqueda dos correios
		for (Remessa remessa : listaRemessa.getRemessas()) {
			List<RemessaVolume> volumes = remessa.getVolumes();
            for (RemessaVolume volume : volumes) {
            	PrePostagemResquest prePostagemRequest = new PrePostagemResquest();

            	// Remetente
        		RemetenteDto remetente = new RemetenteDto();
        		prePostagemRequest.setRemetente(remetente);

        		// Destinatário
        		if(remessa.getEmail() !=null && remessa.getEmail().contains(" ENVIAR")) {
        			remessa.setEmail(null);
        		}
        		String ddd = remessa.getCodigoAreaTelefone().replaceAll("[^0-9]+", "");
        		String telefone = remessa.getNumeroTelefone().replaceAll("[^0-9]+", "");
            	DestinatarioDto destinatario = new DestinatarioDto(remessa.getDescricaoPrincipal(), ddd, telefone, remessa.getEmail(), remessa.getLogradouro(), remessa.getNumero(), remessa.getBairro(), remessa.getComplemento(), remessa.getCep(), remessa.getCidade(), remessa.getUf());
        		prePostagemRequest.setDestinatario(destinatario);

        		// Dados da remessa
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            	prePostagemRequest.setDataPrevistaPostagem(sdf.format(new Date()));
        		prePostagemRequest.setCodigoServico(listaRemessa.getTipoServico().getCodigoServicoCorreios());
            	prePostagemRequest.setPesoInformado(String.valueOf((volume.getPesoGramas())));
            	Integer codigoFormatoObjetoInformado = Integer.valueOf(remessa.getTipoEmbalagem().getTipo());
            	prePostagemRequest.setCodigoFormatoObjetoInformado(codigoFormatoObjetoInformado.toString());

            	if (codigoFormatoObjetoInformado.equals(2)) {
	            	prePostagemRequest.setAlturaInformada(remessa.getTipoEmbalagem().getAlturaCm().toString());
	            	prePostagemRequest.setLarguraInformada(remessa.getTipoEmbalagem().getLarguraCm().toString());
	            	prePostagemRequest.setComprimentoInformado(remessa.getTipoEmbalagem().getComprimentoCm().toString());
            	} 
            	
            	// Serviços adicionais
            	List<ServicoAdicionalDto> listaServicoAdicional = new ArrayList<ServicoAdicionalDto>();

            	for (TipoServico tipoServico : remessa.getTiposServico())
            		listaServicoAdicional.add(new ServicoAdicionalDto(tipoServico.getCodigoServicoCorreios()));
				
            	prePostagemRequest.setListaServicoAdicional(listaServicoAdicional);
            	
            	// Declaração de conteúdo
            	ItemDeclaracaoConteudo conteudo = new ItemDeclaracaoConteudo();
            	if (remessa.getTipoComunicacao() == null || remessa.getTipoComunicacao().getDescricao() == null || remessa.getTipoComunicacao().getDescricao().isEmpty())
            		conteudo.setConteudo("Não declarado");
            	else
            		conteudo.setConteudo(remessa.getTipoComunicacao().getDescricao());
            	
            	conteudo.setQuantidade("1");
            	conteudo.setValor("0.00");
            	
            	prePostagemRequest.setItensDeclaracaoConteudo(new HashSet<ItemDeclaracaoConteudo>());
            	prePostagemRequest.getItensDeclaracaoConteudo().add(conteudo);

            	// Envio
            	PrePostagem response = correiosService.enviar(prePostagemRequest);
            	volume.setNumeroEtiquetaCorreios(response.getCodigoObjeto());
			}
		}
	}
}