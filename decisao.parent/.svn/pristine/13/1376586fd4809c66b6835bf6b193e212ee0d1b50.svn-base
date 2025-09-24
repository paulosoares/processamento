package br.jus.stf.estf.decisao.mobile.assinatura.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;

public class SetorDto {

	private Long id;

	private String sigla;

	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static List<SetorDto> from(Collection<Setor> setores) {
		List<SetorDto> listaSetores = new ArrayList<SetorDto>();
		for (Setor s : setores) {
			listaSetores.add(from(s));
		}
		return listaSetores;
	}

	public static SetorDto from(Setor setor) {
		SetorDto dto = new SetorDto();
		dto.setId(setor.getId());
		dto.setSigla(setor.getSigla());
		dto.setNome(setor.getNome());
		return dto;
	}

}
