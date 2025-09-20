package br.gov.stf.estf.processostf.model.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ItemControle;
import br.gov.stf.estf.entidade.processostf.TipoControle;
import br.gov.stf.estf.entidade.processostf.TipoSituacaoControle;
import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.processostf.model.dataaccess.ItemControleDao;
import br.gov.stf.estf.processostf.model.service.ItemControleService;
import br.gov.stf.estf.processostf.model.service.MapeamentoClasseSetorService;
import br.gov.stf.estf.processostf.model.service.TipoControleService;
import br.gov.stf.estf.processostf.model.service.TipoSituacaoControleService;
import br.gov.stf.estf.processostf.model.service.exception.ItemControleException;
import br.gov.stf.estf.processostf.model.util.ItemControleResult;
import br.gov.stf.estf.usuario.model.service.TipoGrupoUsuarioControleService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("itemControleService")
public class ItemControleServiceImpl extends GenericServiceImpl<ItemControle, Long, ItemControleDao> implements ItemControleService {

	private static final Logger LOGGER = Logger.getLogger(ItemControleServiceImpl.class);

	private TipoControleService tipoControleService;
	private TipoGrupoUsuarioControleService tipoGrupoUsuarioControleService;
	private TipoSituacaoControleService tipoSituacaoControleService;
	private MapeamentoClasseSetorService mapeamentoClasseSetorService;
	private UsuarioService usuarioService;

	public ItemControleServiceImpl(ItemControleDao dao, TipoControleService tipoControleService,
			TipoGrupoUsuarioControleService tipoGrupoUsuarioControleService, TipoSituacaoControleService tipoSituacaoControleService,
			MapeamentoClasseSetorService mapeamentoClasseSetorService, UsuarioService usuarioService) {
		super(dao);
		this.tipoControleService = tipoControleService;
		this.tipoGrupoUsuarioControleService = tipoGrupoUsuarioControleService;
		this.tipoSituacaoControleService = tipoSituacaoControleService;
		this.mapeamentoClasseSetorService = mapeamentoClasseSetorService;
		this.usuarioService = usuarioService;
	}

	//TODO _Não é usado em lugar algum
	/**
	 * Pesquisa todos os itens controles que o usuário possui.
	 * 
	 * @param usuario
	 * @return
	 * @throws ServiceException
	 * @throws ItemControleException
	 */
	@Override
	public SortedMap<TipoControle, List<ItemControleResult>> buscaListaDeItemUsuario(String sigUsuario, Setor setorUsuario, boolean permissaoGabineteSEJ,
			boolean permissaoManterGrupos) throws ServiceException {
		SortedMap<TipoControle, List<ItemControleResult>> listaDeControleUsuario = new TreeMap<TipoControle, List<ItemControleResult>>();
		List<TipoControle> listaControle = null;
		List<TipoGrupoControle> gruposDoUusuario = null;
		List<ItemControle> listaItensControleUsuario = null;
		List<String> listaUsuarioPermissaoManterGrupos = new LinkedList<String>();

		// busca todos os controles
		listaControle = tipoControleService.pesquisarTodosControles();
		if (listaControle.isEmpty()) {
			listaControle = null;
		}

		// busca todos os grupos do usuario logado
		gruposDoUusuario = tipoGrupoUsuarioControleService.buscaGruposDoUsuario(sigUsuario);

		List<String> listaFiltroUsuarioItensControle = new ArrayList<String>();
		TipoSituacaoControle tipoSC = tipoSituacaoControleService.pesquisarSituacaoControle("FECHADO");

		// se não encontrar nenhum grupo não será necessário pesquisar os itens já que não
		// existirá nenhum filtro associado a este usuário.
		if (!permissaoGabineteSEJ) {

			// varre os grupos do usuário para que se possa fazer o filtro com a cláusula DSC_CONSULTA_COMPLEMENTO
			// da entidade TipoGrupoControle e trazer somente os itens controles referentes do usuário logado
			if (!permissaoManterGrupos) {
				for (TipoGrupoControle tgc : gruposDoUusuario) {
					listaFiltroUsuarioItensControle.add(tgc.getDscConsultaComplemento());
				}
				/*if (gruposDoUusuario.size() == 0){
					throw new ItemControleException();
				}	*/
			}

		}

		// busca os itens controle de acordo com filtro dos grupos do usuário
		try {
			listaItensControleUsuario = dao.buscaListaDeItemUsuario(listaFiltroUsuarioItensControle, tipoSC, setorUsuario.getId(), permissaoGabineteSEJ,
					sigUsuario, permissaoManterGrupos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		if (listaControle != null) {
			for (TipoControle tgc : listaControle) {
				List<ItemControleResult> listaItemDeCadaControle = new ArrayList<ItemControleResult>();

				for (ItemControle ic : listaItensControleUsuario) {
					if (ic.getTipoControle() != null) {

						if (tgc.getId().equals(ic.getTipoControle().getId())) {
							ItemControleResult itemControleResult = new ItemControleResult();
							String nomeUltimoSetorDeslocamentoProcesso = "";
							try {

								nomeUltimoSetorDeslocamentoProcesso = dao.pesquisaUltimoDeslocamentoProcesso(ic.getObjetoIncidente().getId());

							} catch (DaoException e) {
								throw new ServiceException(e + "Erro na consulta do ultimo deslocamento do processo " + ic.getId());
							}

							if (ic.getPecaProcessoEletronico() != null) {
								if (ic.getPecaProcessoEletronico().getDocumentos() != null){
									itemControleResult.setArquivoProcessoEletronico(ic.getPecaProcessoEletronico().getDocumentos().get(0));
								}
							}

							itemControleResult.setItemControle(ic);
							itemControleResult.setNomeSetorDestino(nomeUltimoSetorDeslocamentoProcesso);

							listaItemDeCadaControle.add(itemControleResult);
						}
					}
				}

				listaDeControleUsuario.put(tgc, listaItemDeCadaControle);
			}
		}

		return listaDeControleUsuario;
	}

	@Override
	public void chamaPackageItemControle() throws ServiceException {
		try {
			try {
				dao.chamaPackageItemControle();
			} catch (SQLException e) {
				throw new ServiceException(e + "Erro ao carregar a package item controle.");
			}
		} catch (DaoException e) {
			throw new ServiceException(e + "Erro ao carregar a package item controle.");
		}
	}
	
	@Override
	public boolean isItemControleRepublicacao(Long itemControleId) throws ServiceException {
		try {
			return dao.isItemControleRepublicacao(itemControleId);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
