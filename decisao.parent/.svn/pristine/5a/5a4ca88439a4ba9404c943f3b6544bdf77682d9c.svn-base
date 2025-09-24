package br.jus.stf.estf.decisao.handlers.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.MutablePicoContainer;

import br.jus.stf.estf.decisao.DecisaoVersaoInfo;
import br.jus.stf.estf.decisao.StfOfficeDecisaoURI;


public class VersaoTextoDialog extends JDialog {

	private static final long serialVersionUID = 2029397786239184007L;

	private static final Log log = LogFactory.getLog(VersaoTextoDialog.class);

	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private final JList jListVersoes;
	private final List<DecisaoVersaoInfo> listaVersoes;
	private final MutablePicoContainer docContainer;
	private final StfOfficeDecisaoURI documentUri;

	private static final String LIST_NONE = "N„o h· versıes registradas";

	/**
	 * Mapeia a descricao com o respectivo objeto da lista
	 */
	private final Map<String, DecisaoVersaoInfo> descMap = new HashMap<String, DecisaoVersaoInfo>();

	private void buildUi() {
		setSize(new Dimension(320, 200));
		setAlwaysOnTop(true);

		JScrollPane listScroller = new JScrollPane(jListVersoes);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);

		// label, scrollpane
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Selecione uma vers„o:");
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0, 5)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Bot√µes
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (log.isInfoEnabled()) {
					log.info("Fechando janela de versıes");
				}
				fecharJanela();
			}

		});
		buttonPane.add(cancelar);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton selecionar = new JButton("Selecionar");
		selecionar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				if (jListVersoes.getSelectedValue() != null && jListVersoes.getSelectedValue() != LIST_NONE) {
//					try {
//						if (log.isInfoEnabled()) {
//							log.info("Abrindo vers„o do documento: " + jListVersoes.getSelectedValue());
//						}
//						
////						ProcessadorRequisicao processaRequisicao = docContainer.getComponent(ProcessadorRequisicao.class);
////						processaRequisicao.processaRequisicao(docContainer, documentUri.getUri());
////						final PluginActionHandlerDispatcher dispatcher = docContainer.getComponent(PluginActionHandlerDispatcher.class);
////							dispatcher.dispatch(documentUri.getUri().toString(), docContainer);
//					}catch (URISyntaxException e1) {
//						log.error(e1);
//					} catch (ContainerIntializationException e1) {
//						log.error(e1);
//					} catch (HandlerMethodNotFoundException e1) {
//						log.error(e1);
//					}
//				}
			}

		});
		buttonPane.add(selecionar);

		// Put everything together, using the content pane's BorderLayout.
		Container contentPane = getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
	}

	public VersaoTextoDialog(List<DecisaoVersaoInfo> listaVersoes, StfOfficeDecisaoURI documentUri,
			MutablePicoContainer docContainer) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			log.error(e1);
		}
		jListVersoes = new JList();
		this.docContainer = docContainer;
		this.listaVersoes = listaVersoes;
		this.documentUri = documentUri;
		buildUi();
		popularListaUi();

		centralizar();
		setVisible(true);
	}

	private void popularListaUi() {
		String[] listOptions;
		if (listaVersoes.size() == 0) {
			listOptions = new String[1];
			listOptions[0] = LIST_NONE;
		} else {
			listOptions = new String[listaVersoes.size()];

			for (int i = 0; i < listaVersoes.size(); i++) {
				listOptions[i] = listaVersoes.get(i).getNome();
				descMap.put(listOptions[i], listaVersoes.get(i));
			}
		}

		jListVersoes.setListData(listOptions);
	}

	private void fecharJanela() {
		dispose();
	}

	private void centralizar() {
		int screenWidth = toolkit.getScreenSize().width;
		int screenHeight = toolkit.getScreenSize().height;

		int width = (int) ((screenWidth / 2.0f) - (getWidth() / 2.0f));
		int height = (int) ((screenHeight / 2.0f) - (getHeight() / 2.0f));
		setLocation(width, height);
	}

}