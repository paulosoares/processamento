package br.gov.stf.estf.documento.model.service.impl;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.gov.stf.estf.documento.model.util.AssinaturaDto;
import br.gov.stf.estf.entidade.documento.tipofase.TipoTransicaoFaseTexto;
import br.gov.stf.estf.util.DataUtil;

public class TextoServiceImplTest {

	TextoServiceImpl serviceImpl;
	
	@Before
	public void setUp() throws Exception {
		serviceImpl = new TextoServiceImpl(null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	@Test
	public void testGetInfoUsuarioAssinadorCertificado_AssinarNull() throws ParseException {
		String resultadoEsperado = null;		
		AssinaturaDto assinatura = null;

		TipoTransicaoFaseTexto tipoTransicao = TipoTransicaoFaseTexto.CANCELAR_ASSINATURA;
		String retorno = serviceImpl.getInfoUsuarioAssinadorCertificado(assinatura, tipoTransicao);
		assertEquals(resultadoEsperado,retorno);
	}	
	
	@Test
	public void testGetInfoUsuarioAssinadorCertificado_TransicaoNaoAssinar() throws ParseException {
		String resultadoEsperado = null;		
		AssinaturaDto assinatura = new AssinaturaDto();

		TipoTransicaoFaseTexto tipoTransicao = TipoTransicaoFaseTexto.CANCELAR_ASSINATURA;
		String retorno = serviceImpl.getInfoUsuarioAssinadorCertificado(assinatura, tipoTransicao);
		assertEquals(resultadoEsperado,retorno);
	}

	@Ignore
	@Test
	public void testGetInfoUsuarioAssinadorCertificado_assinuraContigencial() throws ParseException {
		Date now = DataUtil.getNowDate();
		String dataString = DataUtil.date2String(now, true);
		Date dataCarimboTempo = null;
		String usuarioLogado = "Usuario Contigencial";
		String resultadoEsperado = usuarioLogado+" "+dataString;
		
		AssinaturaDto assinaturaContigencial = new AssinaturaDto();
		assinaturaContigencial.setUsuarioLogado(usuarioLogado);
		assinaturaContigencial.setDataCarimboTempo(dataCarimboTempo);
		TipoTransicaoFaseTexto tipoTransicao = TipoTransicaoFaseTexto.ASSINAR_DIGITALMENTE;
		String retorno = serviceImpl.getInfoUsuarioAssinadorCertificado(assinaturaContigencial, tipoTransicao);
		assertEquals(resultadoEsperado,retorno);
	}
	
	@Test
	public void testGetInfoUsuarioAssinadorCertificado_assinutaCertificado() throws ParseException {
		String dataString = "22/11/2016 16:16:00";
		Date dataCarimboTempo = DataUtil.string2Date(dataString, true);
		String usuarioLogado = "Usuario Logado";
		String subjectDN  = "CN=FULANO DE TESTE SILVA:0000, OU=SERVIDOR, OU=SUPREMO TRIBUNAL FEDERAL-STF, OU=Autoridade Certificadora da Justica - ACJUS v4, OU=Cert-JUS Institucional - A3, O=ICP-Brasil, C=BR";
		String resultadoEsperado = "FULANO DE TESTE SILVA"+" "+dataString;
		
		AssinaturaDto assinatura = new AssinaturaDto();
		assinatura.setUsuarioLogado(usuarioLogado);
		assinatura.setDataCarimboTempo(dataCarimboTempo);
		assinatura.setSubjectDN(subjectDN);
		TipoTransicaoFaseTexto tipoTransicao = TipoTransicaoFaseTexto.ASSINAR_DIGITALMENTE;
		String retorno = serviceImpl.getInfoUsuarioAssinadorCertificado(assinatura, tipoTransicao);
		assertEquals(resultadoEsperado,retorno);
	}	
	
	@Test
	public void testGetAliasUsuarioCertificado() throws ParseException {
		String dataString = "22/11/2016 16:16:00";
		Date dataCarimboTempo = DataUtil.string2Date(dataString, true);
		String usuarioLogado = "Usuario Logado";
		String subjectDN  = "CN=FULANO DE TESTE SILVA:0000, OU=SERVIDOR, OU=SUPREMO TRIBUNAL FEDERAL-STF, OU=Autoridade Certificadora da Justica - ACJUS v4, OU=Cert-JUS Institucional - A3, O=ICP-Brasil, C=BR";
		String resultadoEsperado = "FULANO DE TESTE SILVA";
		
		AssinaturaDto assinatura = new AssinaturaDto();
		assinatura.setUsuarioLogado(usuarioLogado);
		assinatura.setDataCarimboTempo(dataCarimboTempo);
		assinatura.setSubjectDN(subjectDN);
		
		String retorno = serviceImpl.getAliasUsuarioCertificado(assinatura);
		assertEquals(resultadoEsperado,retorno);
	}
}