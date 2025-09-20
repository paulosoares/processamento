package br.gov.stf.estf.assinatura.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.assinatura.service.TagsLivresServiceLocal;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.service.TagsLivresUsuarioService;
import br.gov.stf.estf.documento.model.service.TipoTagsLivresUsuarioService;
import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.estf.entidade.documento.TipoTagsLivresUsuario;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("tagsLivresServiceLocal")
public class TagsLivresServiceLocalImpl implements TagsLivresServiceLocal {

	public static final Long CODIGO_TIPO_TAG_CAMPOS_LIVRES = 1L;
	public static final Long CODIGO_TIPO_TAG_AUTORIDADE = 2L;

	@Autowired
	private TagsLivresUsuarioService tagsLivresUsuarioService;
	@Autowired
	private TipoTagsLivresUsuarioService tipoTagsLivresUsuarioService;

	@Override
	public void alterarTagsAutoridades(TagsLivresUsuario tagsAutoridades) throws RegraDeNegocioException, ServiceLocalException {
		Validate.notNull(tagsAutoridades, "A tag não pode ser nula.");

		if (StringUtils.isVazia(tagsAutoridades.getDscTagLivres())) {
			throw new RegraDeNegocioException("É necessário informar o valor do campo \"descrição\".");
		}

		try {
			tagsLivresUsuarioService.salvar(tagsAutoridades);
		} catch (ServiceException exception) {
			throw new ServiceLocalException(exception);
		}
	}

	@Override
	public void excluirTiposTagsLivresUsuario(List<TipoTagsLivresUsuario> tiposTagsLivresUsuarios) throws RegraDeNegocioException, ServiceLocalException {
		try {
			for (TipoTagsLivresUsuario tipoTagsLivresUsuario : tiposTagsLivresUsuarios) {
				tipoTagsLivresUsuarioService.excluir(tipoTagsLivresUsuario);
			}
		} catch (ServiceException exception) {
			throw new ServiceLocalException(exception);
		}
	}

	@Override
	public List<TagsLivresUsuario> pesquisarTodasTagsAutoridades() throws ServiceLocalException, RegraDeNegocioException {
		return pesquisarTagsAutoridades(new TagsLivresUsuario());
	}

	@Override
	public List<TagsLivresUsuario> pesquisarTagsAutoridades(TagsLivresUsuario exemplo) throws RegraDeNegocioException, ServiceLocalException {
		Validate.notNull(exemplo, "Exemplo nulo!");

		List<TagsLivresUsuario> tagsAutoridades = Collections.emptyList();

		try {
			tagsAutoridades = tagsLivresUsuarioService.pesquisarNomeRotuloOuDescricao(null, CODIGO_TIPO_TAG_AUTORIDADE, exemplo.getDscTagLivres());
		} catch (ServiceException exception) {
			throw new ServiceLocalException(exception);
		}

		return tagsAutoridades;
	}
	
	@Override
	public List<TagsLivresUsuario> pesquisarTodasTagsLivres() throws RegraDeNegocioException, ServiceLocalException {
		List<TagsLivresUsuario> tagsAutoridades = Collections.emptyList();

		try {
			tagsAutoridades = tagsLivresUsuarioService.pesquisarNomeRotuloOuDescricao(null, CODIGO_TIPO_TAG_CAMPOS_LIVRES, null);
		} catch (ServiceException exception) {
			throw new ServiceLocalException(exception);
		}

		return tagsAutoridades;
	}

	@Override
	public List<TipoTagsLivresUsuario> pesquisarTodosTiposTagsLivresUsuario() throws RegraDeNegocioException, ServiceLocalException {
		return pesquisarTiposTagsLivresUsuario(new TipoTagsLivresUsuario());
	}

	@Override
	public List<TipoTagsLivresUsuario> pesquisarTiposTagsLivresUsuario(TipoTagsLivresUsuario exemplo) throws RegraDeNegocioException, ServiceLocalException {
		Validate.notNull(exemplo, "Exemplo nulo!");

		List<TipoTagsLivresUsuario> tagsLivresUsuario = Collections.emptyList();

		try {
			tagsLivresUsuario = tipoTagsLivresUsuarioService.pesquisar(exemplo.getDscTipoTagLivres());
		} catch (ServiceException exception) {
			throw new ServiceLocalException(exception);
		}

		return tagsLivresUsuario;
	}

	@Override
	public void salvarTagsAutoridades(TagsLivresUsuario tagsAutoridades) throws RegraDeNegocioException, ServiceLocalException {
		Validate.notNull(tagsAutoridades, "A tagsAutoridades não pode ser nula.");

		if (StringUtils.isVazia(tagsAutoridades.getDscTagLivres())) {
			throw new RegraDeNegocioException("A autoridade deve ser informada.");
		}

		String codigoTag = tagsAutoridades.getNomeRotulo();
		String campoSemAcento = StringUtils.retirarAcentos(codigoTag);
		String semEspacos = StringUtils.substituiEspacosBrancoPorUnderline(campoSemAcento);
		String tagConcatenada = "@@" + semEspacos + "@@";
		tagsAutoridades.setCodigoRotulo(tagConcatenada);
		tagsAutoridades.setDscTagLivres(tagsAutoridades.getDscTagLivres());
		tagsAutoridades.setNomeRotulo(semEspacos);

		TipoTagsLivresUsuario tipoTags;

		try {
			// recupera somente o tipo com o tipo "autoridade"
			tipoTags = tipoTagsLivresUsuarioService.recuperarPorId(CODIGO_TIPO_TAG_AUTORIDADE);
		} catch (ServiceException exception) {
			throw new ServiceLocalException("Erro ao pesquisar o tipo de campo igual a 'campo livre'.", exception);
		}

		tagsAutoridades.setTipoTagsLivres(tipoTags);

		try {
			tagsLivresUsuarioService.incluir(tagsAutoridades);
		} catch (ServiceException exception) {
			throw new ServiceLocalException(exception);
		}
	}

	@Override
	public void salvarTiposTagsLivresUsuario(TipoTagsLivresUsuario tipoTagsLivresUsuario) throws RegraDeNegocioException, ServiceLocalException {
		Validate.notNull(tipoTagsLivresUsuario, "O tipoTagsLivresUsuario não pode ser nulo.");

		if (StringUtils.isVazia(tipoTagsLivresUsuario.getDscTipoTagLivres())) {
			throw new RegraDeNegocioException("Favor digitar o valor no campo descrição.");
		} else {
			try {
				tipoTagsLivresUsuarioService.incluir(tipoTagsLivresUsuario);
			} catch (ServiceException exception) {
				throw new ServiceLocalException("Erro ao salvar os dados.");
			}
		}
	}
}
