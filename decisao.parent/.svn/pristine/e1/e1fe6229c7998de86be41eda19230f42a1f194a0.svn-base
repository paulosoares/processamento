package br.jus.stf.estf.decisao;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.picocontainer.MutablePicoContainer;

import br.jus.stf.estf.decisao.handlers.DocumentoUtil;
import br.jus.stf.estf.decisao.handlers.ValidacaoEstiloException;
import br.jus.stf.stfoffice.client.ui.DocumentTabController;
import br.jus.stf.stfoffice.client.ui.PluginDispatchSelectionListener;
import br.jus.stf.stfoffice.client.ui.StfOfficeStandardToolBar;
import br.jus.stf.stfoffice.client.uno.BeanUnoWrapperException;
import br.jus.stf.stfoffice.client.uno.Estilo;
import br.jus.stf.stfoffice.client.uno.StyleLoader;
import br.jus.stf.stfoffice.client.uno.Sublinhado;
import br.jus.stf.stfoffice.client.uno.SublinhadoEnum;

public class DecisaoStandardToolBar extends StfOfficeStandardToolBar {

	private final File document;
	private static final Log log = LogFactory.getLog(DecisaoStandardToolBar.class);
	private PluginDispatchSelectionListener salvarDispatch;

	public DecisaoStandardToolBar(MutablePicoContainer picoContainer, File document) {
		super(picoContainer);
		this.document = document;

	}

	public List<Estilo> getEstilos() {
		try {
			StyleLoader styleLoader = new StyleLoader(document);
			return styleLoader.getEstilos();
		} catch (Exception e) {
			log.error("Não foi possível carregar estilos", e);
		}

		return null;
	}

	public List<Sublinhado> getSublinhados() {
		return SublinhadoEnum.carregarTodosSublinhados();
	}

	@Override
	public void init(final DocumentTabController tabController, final Composite composite, Boolean disabled, Boolean disabledSalvar) {
		final Image imgBarBack = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("barraMenuHeader.png"));

		final Image imgSalvar = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("salvarIco.png"));

		final Image imgEstilo = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("estiloIco.png"));

		final Image imgNegrito = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("negritoIco.png"));

		final Image imgItalico = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("italicoIco.png"));

		final Image imgSublinhado = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("sublinhadoIco.png"));

		final Image imgRodape = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("rodapeIco.png"));

		final Image imgLeftAlign = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("leftAlignIco.png"));

		final Image imgCenterAlign = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("centerAlignIco.png"));

		final Image imgRightAlign = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("rightAlignIco.png"));

		final Image imgJustifyAlign = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("justifyAlignIco.png"));

		final Image imgRealcar = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("realcarIco.png"));

		final Image imgSemPreenchimento = new Image(composite.getDisplay(), DecisaoStandardToolBar.class
				.getClassLoader().getResourceAsStream("semPreenchimentoIco.png"));

		final Image imgBinoculo = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("binoculoIco.png"));

		final Image imgTesoura = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("tesouraIco.png"));

		final Image imgCopiar = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("copiarIco.png"));

		final Image imgColar = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("colarIco.png"));

		final Image imgSelecionarTudo = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("selecionarTudoIco.png"));

		final Image imgDesfazer = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("desfazerIco.png"));

		final Image imgRefazer = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("refazerIco.png"));

		final Image imgCaixaAlta = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("caixaAltaIco.png"));

		final Image imgSemCaixaAlta = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("semCaixaAltaIco.png"));

		final Image imgcolarEspecial = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("colarEspecialIco.png"));

		final Image imgZoom = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("zoomIco.png"));

		final Image imgConfSublinhado = new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("confSublinhadoIco.png"));
		
		final Image imgPadronizarFormatacao= new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("padronizarIco.png"));

		final Image imgSublinharSomentePalavras= new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("sublinharSomentePalavrasIco.png"));
		
		final Image imgContarPalavras= new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("contarPalavrasIco.png"));

		final Image imgConverterTabelaTexto= new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("converterTabelaTextoIco.png"));

		final Image imgCaracteresNaoImprimiveis= new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("caracteresNaoImprimiveis.png"));
		
		final Image imgIniciaisMaiscula= new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("maiusculaMinuscula.png"));
		
		final Image imgUppercase= new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("uppercase.png"));
		
		final Image imgLowercase= new Image(composite.getDisplay(), DecisaoStandardToolBar.class.getClassLoader()
				.getResourceAsStream("lowercase.png"));
		
		GridData barGridData = new GridData();
		barGridData.heightHint = 24;
		barGridData.grabExcessHorizontalSpace = true;
		barGridData.horizontalAlignment = SWT.FILL;
		composite.setLayoutData(barGridData);
		composite.setBackgroundImage(imgBarBack);

		boolean enableToolItem = true;
		if (disabled.equals(Boolean.TRUE)) {
			enableToolItem = false;
		}

		final ToolBar toolBar = new ToolBar(composite, SWT.HORIZONTAL | SWT.FLAT);

		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem salvarItem = new ToolItem(toolBar, SWT.PUSH);
		if(disabledSalvar)
			salvarItem.setEnabled(disabledSalvar);
		else
			salvarItem.setEnabled(enableToolItem);
		salvarItem.setImage(imgSalvar);
		salvarItem.setToolTipText("Salvar");
		salvarItem.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				salvarDispatch.widgetSelected(e);
			}

		});

		new ToolItem(toolBar, SWT.SEPARATOR);

		/* MENU ESTILOS */

		final Menu estilosMenu = new Menu(composite.getShell(), SWT.POP_UP);
		final ToolItem estiloItem = new ToolItem(toolBar, SWT.DROP_DOWN);

		try {
			// styleLoader = new StyleLoader("d:\\modelo1.ott");

			for (Estilo estilo : getEstilos()) {
				final MenuItem menuItem = new MenuItem(estilosMenu, SWT.RADIO);
				menuItem.setText(estilo.getDescricao());
				menuItem.setData(estilo.getDescricao(), estilo);
				menuItem.addSelectionListener(new SelectionListener() {

					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub

					}

					public void widgetSelected(SelectionEvent e) {
						if (log.isInfoEnabled()) {
							log.info("Aplicando estilo: " + menuItem.getText());
						}
						// menuItem.setImage(imgChecked);
						// for(MenuItem itemMenu : estilosMenu.getItems()) {
						// if(!menuItem.getText().equals(itemMenu.getText()))
						// itemMenu.setImage(null);
						// }
						final Estilo estilo = (Estilo) menuItem.getData(menuItem.getText());
						// estiloItem.setText(estilo.getDescricao());
						estiloItem.setData(estilo);
						DecisaoStandardToolBar.this.unoWrapper.aplicarEstiloSelecao(estilo.getId());
					}

				});

			}
			estiloItem.setEnabled(enableToolItem);
			estiloItem.setImage(imgEstilo);
			estiloItem.setToolTipText("Alterar estilo");
			estiloItem.addListener(SWT.Selection, new Listener() {

				private void arrowEvent(ToolItem item) {
					// System.out.println(org.eclipse.swt.SWT.Activate);
					Rectangle rect = item.getBounds();
					Point pt = new Point(rect.x, rect.y + rect.height);
					pt = toolBar.toDisplay(pt);
					estilosMenu.setLocation(pt.x, pt.y);
					estilosMenu.setVisible(true);
				}

				public void handleEvent(Event event) {
					ToolItem item = (ToolItem) event.widget;
					arrowEvent(item);
					// if (event.detail == SWT.ARROW) {
					// arrowEvent(item);
					// } else {
					// if (log.isInfoEnabled()) {
					// log.info("Aplicando estilo: " + item.getText());
					// }
					// Estilo estilo = (Estilo) item.getData();
					// if (estilo != null) {
					// unoWrapper.aplicarEstiloSelecao(estilo.getId());
					// } else {
					// arrowEvent(item);
					// }
					// }
				}

			});

			new ToolItem(toolBar, SWT.SEPARATOR);

			ToolItem negritoItem = new ToolItem(toolBar, SWT.CHECK);
			negritoItem.setEnabled(enableToolItem);
			negritoItem.setImage(imgNegrito);
			negritoItem.setToolTipText("Negrito");
			negritoItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Aplicando Negrito");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper
								.alterarEstiloFonte(br.jus.stf.stfoffice.client.uno.BeanUnoWrapper.Fonte.Estilo.NEGRITO);
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao aplicar negrito", e1);
					}
				}

			});

			ToolItem italicoItem = new ToolItem(toolBar, SWT.CHECK);
			italicoItem.setEnabled(enableToolItem);
			italicoItem.setImage(imgItalico);
			italicoItem.setToolTipText("Itálico");
			italicoItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Aplicando Itálico");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper
								.alterarEstiloFonte(br.jus.stf.stfoffice.client.uno.BeanUnoWrapper.Fonte.Estilo.ITALICO);
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao aplicar itálico", e1);
					}
				}

			});

			ToolItem sublinhadoItem = new ToolItem(toolBar, SWT.CHECK);
			sublinhadoItem.setEnabled(enableToolItem);
			sublinhadoItem.setImage(imgSublinhado);
			sublinhadoItem.setToolTipText("Sublinhar");
			sublinhadoItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Aplicando Sublinhado");
					}
					try {

						DecisaoStandardToolBar.this.unoWrapper
								.alterarEstiloFonte(br.jus.stf.stfoffice.client.uno.BeanUnoWrapper.Fonte.Estilo.SUBLINHADO);
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao aplicar sublinhado", e1);
					}
				}

			});

			if (tabController.getDocumentInfo().isRodape()) {
				new ToolItem(toolBar, SWT.SEPARATOR);

				ToolItem rodapeItem = new ToolItem(toolBar, SWT.PUSH);
				rodapeItem.setEnabled(enableToolItem);
				rodapeItem.setImage(imgRodape);
				rodapeItem.setToolTipText("Rodapé");
				rodapeItem.addSelectionListener(new SelectionListener() {

					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub

					}

					public void widgetSelected(SelectionEvent e) {
						if (log.isInfoEnabled()) {
							log.info("Adicionando nota de rodapé");
						}
						try {
							DecisaoStandardToolBar.this.unoWrapper.inserirNotaRodape();
						} catch (BeanUnoWrapperException e1) {
							log.error("Erro ao inserir nota de rodapé", e1);
						}
					}

				});

			}
			
			ToolItem sublinharSomentePalavrasItem = new ToolItem(toolBar, SWT.PUSH);
			sublinharSomentePalavrasItem .setEnabled(enableToolItem);
			sublinharSomentePalavrasItem .setImage(imgSublinharSomentePalavras);
			sublinharSomentePalavrasItem .setToolTipText("Sublinhar somente palavras");
			sublinharSomentePalavrasItem .addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Sublinhando somente as palavras...");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.sublinharSomentePalavras();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao sublinhar somente as palavras!", e1);
					} 
				}

			});

			
			
			/* MENU SUBLINHADOS */
/*			final Menu sublinhadosMenu = new Menu(composite.getShell(), SWT.POP_UP);
			final ToolItem sublinhadoItemMenu = new ToolItem(toolBar, SWT.DROP_DOWN);

			for (Sublinhado sublinhado : getSublinhados()) {
				final MenuItem sublinhadoMenuItem = new MenuItem(sublinhadosMenu, SWT.RADIO);
				sublinhadoMenuItem.setText(sublinhado.getDescricao());
				sublinhadoMenuItem.setData(sublinhado.getDescricao(), sublinhado);
				sublinhadoMenuItem.addSelectionListener(new SelectionListener() {

					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub

					}

					public void widgetSelected(SelectionEvent e) {
						if (log.isInfoEnabled()) {
							log.info("Aplicando sublinhado: " + sublinhadoMenuItem.getText());
						}

						// menuItem.setImage(imgChecked);
						// for(MenuItem itemMenu : sublinhadosMenu.getItems()) {
						// if(!menuItem.getText().equals(itemMenu.getText()))
						// itemMenu.setImage(null);
						// }
						final Sublinhado sublinhado = (Sublinhado) sublinhadoMenuItem.getData(sublinhadoMenuItem
								.getText());
						sublinhadoItemMenu.setData(sublinhado);
						DecisaoStandardToolBar.this.unoWrapper.aplicarSublinhadoSelecao(sublinhado
								.getValorComandoUnderine());
					}

				});

			}

			final MenuItem sublinhadoSomentePalavrasMenuItem = new MenuItem(sublinhadosMenu, SWT.CHECK);
			sublinhadoSomentePalavrasMenuItem.setText("Somente palavras");
			sublinhadoSomentePalavrasMenuItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Aplicar ou remover sublinhado somente palavras.");
					}

					try {
						if (sublinhadoSomentePalavrasMenuItem.getSelection()) {
							if (log.isInfoEnabled()) {
								log.info("Aplicando sublinhado somente palavras.");
							}
							DecisaoStandardToolBar.this.unoWrapper.sublinharSomentePalavras(true);
						} else {
							if (log.isInfoEnabled()) {
								log.info("Removendo sublinhado somente palavras.");
							}
							DecisaoStandardToolBar.this.unoWrapper.sublinharSomentePalavras(false);
						}
					} catch (BeanUnoWrapperException e1) {
						e1.printStackTrace();
					}
				}

			});

			sublinhadoItemMenu.setEnabled(enableToolItem);
			sublinhadoItemMenu.setImage(imgConfSublinhado);
			sublinhadoItemMenu.setToolTipText("Alterar tipo sublinhado");
			sublinhadoItemMenu.addListener(SWT.Selection, new Listener() {

				private void arrowEvent(ToolItem item) {
					Rectangle rect = item.getBounds();
					Point pt = new Point(rect.x, rect.y + rect.height);
					pt = toolBar.toDisplay(pt);
					sublinhadosMenu.setLocation(pt.x, pt.y);
					sublinhadosMenu.setVisible(true);
				}

				public void handleEvent(Event event) {
					ToolItem item = (ToolItem) event.widget;
					arrowEvent(item);
				}

			});
*/
			/*
			 * ToolItem somentePalavraItem = new ToolItem(toolBar,
			 * SWT.SEPARATOR);
			 * 
			 * final Button somentePalavraButton = new Button(toolBar,
			 * SWT.CHECK); somentePalavraButton.setText("Somente palavras");
			 * somentePalavraButton.setSelection(false);
			 * somentePalavraButton.setBackgroundImage(imgBarBack);
			 * somentePalavraButton.setLayoutData(new GridData(SWT.CENTER,
			 * SWT.CENTER, false, false)); somentePalavraButton.pack();
			 * somentePalavraItem.setWidth(105);
			 * somentePalavraItem.setControl(somentePalavraButton);
			 * somentePalavraButton.addSelectionListener(new SelectionListener()
			 * {
			 * 
			 * @Override public void widgetSelected(SelectionEvent e) { if
			 * (log.isInfoEnabled()) { log.info("Utilizar somente palavras"); }
			 * 
			 * try { if(somentePalavraButton.getSelection()) { if
			 * (log.isInfoEnabled()) {
			 * log.info("Aplicando sublinhar somente palavras"); }
			 * DecisaoStandardToolBar
			 * .this.unoWrapper.sublinharSomentePalavras(); } else { if
			 * (log.isInfoEnabled()) {
			 * log.info("Retirando sublinhar somente palavras"); }
			 * DecisaoStandardToolBar
			 * .this.unoWrapper.removerSublinhadoSomentePalavras(); } } catch
			 * (BeanUnoWrapperException e1) {
			 * log.error("Erro ao utilizar somente palavras", e1); }
			 * 
			 * }
			 * 
			 * @Override public void widgetDefaultSelected(SelectionEvent e) {
			 * // TODO Auto-generated method stub
			 * 
			 * } });
			 */

			new ToolItem(toolBar, SWT.SEPARATOR);

			ToolItem realcarItem = new ToolItem(toolBar, SWT.CHECK);
			realcarItem.setEnabled(enableToolItem);
			realcarItem.setImage(imgRealcar);
			realcarItem.setToolTipText("Realçar");
			realcarItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Aplicando realce");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.realcarTexto();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao aplicar realce", e1);
					}
				}

			});

			ToolItem semPreenchimentoItem = new ToolItem(toolBar, SWT.CHECK);
			semPreenchimentoItem.setEnabled(enableToolItem);
			semPreenchimentoItem.setImage(imgSemPreenchimento);
			semPreenchimentoItem.setToolTipText("Sem realce (preenchimento))");
			semPreenchimentoItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Retirando preenchimento");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.semPreenchimento();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao retirar preenchimento", e1);
					}
				}

			});

			new ToolItem(toolBar, SWT.SEPARATOR);

			// VERSALETE - SMALL CAPS
			ToolItem caixaAltaItem = new ToolItem(toolBar, SWT.CHECK);
			caixaAltaItem.setEnabled(enableToolItem);
			caixaAltaItem.setImage(imgCaixaAlta);
			caixaAltaItem.setToolTipText("Caixa alta (versalete)");
			caixaAltaItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				// Este método simula um atalho no teclado, pois não estava
				// conseguindo executar o comando uno para aplicar o versalete
				// (small caps),
				// então gravei uma macro e um atalho para esta macro
				// (CTRL+SHIFT+V) e ao clicar no botão, simulo no teclado:
				// CTRL+SHFT+V.
				// Existe alguma diferença na execução das macros no BrOffice e
				// no sistema ESTF-DECISÃO.
				// RODRIGO.LISBOA

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Aplicando caixa alta");
					}

					// try {
					// DecisaoStandardToolBar.this.unoWrapper.caixaAlta(4);
					// } catch (BeanUnoWrapperException e1) {
					// log.error("Erro ao aplicar caixa alta", e1);
					// }

					try {
						// simula as teclas CTRL + SHIFT + V
						Robot robot = new Robot();
						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_SHIFT);
						robot.keyPress(KeyEvent.VK_V);
						robot.keyRelease(KeyEvent.VK_V);
						robot.keyRelease(KeyEvent.VK_SHIFT);
						robot.keyRelease(KeyEvent.VK_CONTROL);

					} catch (AWTException e2) {
						log.error("Erro ao aplicar versalete", e2);
					}
				}

			});

			// VERSALETE - SMALL CAPS
			ToolItem semCaixaAltaItem = new ToolItem(toolBar, SWT.CHECK);
			semCaixaAltaItem.setEnabled(enableToolItem);
			semCaixaAltaItem.setImage(imgSemCaixaAlta);
			semCaixaAltaItem.setToolTipText("Sem caixa alta (versalete)");
			semCaixaAltaItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				// Este método simula um atalho no teclado, pois não estava
				// conseguindo executar o comando uno para aplicar o versalete
				// (small caps),
				// então gravei uma macro e um atalho para esta macro
				// (CTRL+SHIFT+S) e ao clicar no botão, simulo no teclado:
				// CTRL+SHFT+S.
				// Existe alguma diferença na execução das macros no BrOffice e
				// no sistema ESTF-DECISÃO.
				// RODRIGO.LISBOA

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Retirando caixa alta");
					}
					// try {
					// DecisaoStandardToolBar.this.unoWrapper.caixaAlta(0);
					// } catch (BeanUnoWrapperException e1) {
					// log.error("Erro ao retirar caixa alta", e1);
					// }

					try {
						// simula as teclas CTRL + SHIFT + S
						Robot robot = new Robot();
						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_SHIFT);
						robot.keyPress(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_S);
						robot.keyRelease(KeyEvent.VK_SHIFT);
						robot.keyRelease(KeyEvent.VK_CONTROL);

					} catch (AWTException e2) {
						log.error("Erro ao retirar versalete", e2);
					}

				}

			});
			/*
			 * Para o botão de aplicar e retirar versalete, estas 2 macros
			 * abaixo devem estar no Module1.xml (arquivo de macro):
			 * 
			 * sub AplicarVersalete rem
			 * ------------------------------------------
			 * ---------------------------- rem define variables dim document as
			 * object dim dispatcher as object rem
			 * ------------------------------
			 * ---------------------------------------- rem get access to the
			 * document document = ThisComponent.CurrentController.Frame
			 * dispatcher =
			 * createUnoService(&quot;com.sun.star.frame.DispatchHelper&quot;)
			 * 
			 * rem
			 * --------------------------------------------------------------
			 * -------- dim args1(0) as new com.sun.star.beans.PropertyValue
			 * args1(0).Name = &quot;CaseMap&quot; args1(0).Value = 4
			 * 
			 * dispatcher.executeDispatch(document, &quot;.uno:CaseMap&quot;,
			 * &quot;&quot;, 0, args1()) end sub
			 * 
			 * 
			 * sub RetirarVersalete rem
			 * ------------------------------------------
			 * ---------------------------- rem define variables dim document as
			 * object dim dispatcher as object rem
			 * ------------------------------
			 * ---------------------------------------- rem get access to the
			 * document document = ThisComponent.CurrentController.Frame
			 * dispatcher =
			 * createUnoService(&quot;com.sun.star.frame.DispatchHelper&quot;)
			 * 
			 * rem
			 * --------------------------------------------------------------
			 * -------- dim args1(0) as new com.sun.star.beans.PropertyValue
			 * args1(0).Name = &quot;CaseMap&quot; args1(0).Value = 0
			 * 
			 * dispatcher.executeDispatch(document, &quot;.uno:CaseMap&quot;,
			 * &quot;&quot;, 0, args1()) end sub
			 */

			new ToolItem(toolBar, SWT.SEPARATOR);

			ToolItem localizarSubstituirItem = new ToolItem(toolBar, SWT.PUSH);
			localizarSubstituirItem.setEnabled(true);
			localizarSubstituirItem.setImage(imgBinoculo);
			localizarSubstituirItem.setToolTipText("Localizar e substituir");
			localizarSubstituirItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Abrindo a caixa de Localizar e Substituir");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.localizarSubstituir();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao abrir a caixa de Localizar e Substituir", e1);
					}
				}

			});

			ToolItem zoomItem = new ToolItem(toolBar, SWT.PUSH);
			zoomItem.setEnabled(true);
			zoomItem.setImage(imgZoom);
			zoomItem.setToolTipText("Zoom");
			zoomItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
				}

				// Este método simula um atalho no teclado, pois ao executar o
				// comando uno para zoom, estava travando a tela.
				// Ao clicar no botão, simiula simula as teclas CTRL+SHIFT+N.
				// Existe alguma diferença na execução das macros no BrOffice e
				// no sistema ESTF-DECISÃO.
				// RODRIGO.LISBOA
				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Abrindo a caixa de Zoom");
					}
					// try {
					// DecisaoStandardToolBar.this.unoWrapper.zoom();
					// } catch (BeanUnoWrapperException e1) {
					// log.error("Erro ao abrir a caixa de Zoom", e1);
					// }

					try {
						// DecisaoStandardToolBar.this.unoWrapper.localizarEspacoEmBranco();
						DecisaoStandardToolBar.this.unoWrapper.setFoco();

						Robot robot = new Robot();
						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_SHIFT);
						robot.keyPress(KeyEvent.VK_N);
						robot.keyRelease(KeyEvent.VK_N);
						robot.keyRelease(KeyEvent.VK_SHIFT);
						robot.keyRelease(KeyEvent.VK_CONTROL);
					} catch (AWTException e1) {
						log.error("Erro ao abrir a caixa de Zoom", e1);
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao setar o foco no documento", e1);
					}

				}

			});

			new ToolItem(toolBar, SWT.SEPARATOR);

			ToolItem recortarItem = new ToolItem(toolBar, SWT.PUSH);
			recortarItem.setEnabled(enableToolItem);
			recortarItem.setImage(imgTesoura);
			recortarItem.setToolTipText("Recortar");
			recortarItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Recortando");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.recortar();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao recortar", e1);
					}
				}

			});

			ToolItem copiarItem = new ToolItem(toolBar, SWT.PUSH);
			copiarItem.setEnabled(true);
			copiarItem.setImage(imgCopiar);
			copiarItem.setToolTipText("Copiar");
			copiarItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Copiando");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.copiar();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao copiar", e1);
					}
				}

			});

			ToolItem colarItem = new ToolItem(toolBar, SWT.PUSH);
			colarItem.setEnabled(enableToolItem);
			colarItem.setImage(imgColar);
			colarItem.setToolTipText("Colar");
			colarItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Colando");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.colar();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao colar", e1);
					}
				}

			});

			ToolItem colarEspecialItem = new ToolItem(toolBar, SWT.PUSH);
			colarEspecialItem.setEnabled(enableToolItem);
			colarEspecialItem.setImage(imgcolarEspecial);
			colarEspecialItem.setToolTipText("Colar especial");
			colarEspecialItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Colando especial");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.colarEspecial();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao colar especial", e1);
					}
				}

			});

			ToolItem selecionarTudoItem = new ToolItem(toolBar, SWT.PUSH);
			selecionarTudoItem.setEnabled(true);
			selecionarTudoItem.setImage(imgSelecionarTudo);
			selecionarTudoItem.setToolTipText("Selecionar tudo");
			selecionarTudoItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Selecionando tudo");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.selecionarTudo();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao selecionar tudo", e1);
					}
				}

			});

			ToolItem desfazerItem = new ToolItem(toolBar, SWT.PUSH);
			desfazerItem.setEnabled(enableToolItem);
			desfazerItem.setImage(imgDesfazer);
			desfazerItem.setToolTipText("Desfazer");
			desfazerItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Desfazendo");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.desfazer();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao desfazer", e1);
					}
				}

			});

			ToolItem refazerItem = new ToolItem(toolBar, SWT.PUSH);
			refazerItem.setEnabled(enableToolItem);
			refazerItem.setImage(imgRefazer);
			refazerItem.setToolTipText("Refazer");
			refazerItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Refazendo");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.refazer();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao refazer", e1);
					}
				}

			});

			ToolItem padronizarFormatacaoItem = new ToolItem(toolBar, SWT.PUSH);
			padronizarFormatacaoItem.setEnabled(enableToolItem);
			padronizarFormatacaoItem.setImage(imgPadronizarFormatacao);
			padronizarFormatacaoItem.setToolTipText("Padronizar formatação");
			padronizarFormatacaoItem.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Padronizando a formatação...");
					}
					try {
						DocumentoUtil.padronizarFormatacao(DecisaoStandardToolBar.this.unoWrapper.getBean());
					} catch (ValidacaoEstiloException e1) {
						log.error("Erro ao padronizar a formatação!", e1);
					}
				}

			});
			
			ToolItem contarPalavras = new ToolItem(toolBar, SWT.PUSH);
			contarPalavras.setEnabled(enableToolItem);
			contarPalavras.setImage(imgContarPalavras);
			contarPalavras.setToolTipText("Contar palavras");
			contarPalavras.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Abrindo a caixa de contagem de palavras.");
					}
					
					try {
						DecisaoStandardToolBar.this.unoWrapper.setFoco();

						Robot robot = new Robot();
						
						// Atualiza os dados de contagem
						robot.keyPress(KeyEvent.VK_F9);
						robot.keyRelease(KeyEvent.VK_F9);
						
						// Aciona atalho responsável por realizar a contagem.
						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_SHIFT);
						robot.keyPress(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_W);
						robot.keyRelease(KeyEvent.VK_SHIFT);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						
					} catch (AWTException e1) {
						log.error("Erro ao abrir a caixa de contagem de palavras", e1);
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao setar o foco no documento", e1);
					}
				}

			});

			// Adiciona botão para converter tabela em texto
			ToolItem converterTabelaTexto = new ToolItem(toolBar, SWT.PUSH);
			converterTabelaTexto.setEnabled(enableToolItem);
			converterTabelaTexto.setImage(imgConverterTabelaTexto);
			converterTabelaTexto.setToolTipText("Converter tabela em texto");
			converterTabelaTexto.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Convertendo tabela em texto.");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.converterTabelaTexto();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro ao converter tabela em texto.", e1);
					}
					
				}

			});
			
			
			
			/* Adiciona botão para acionar Caracteres Não imprimíveis. */
			/*
			ToolItem caracteresNaoImprimiveis = new ToolItem(toolBar, SWT.PUSH);
			caracteresNaoImprimiveis.setEnabled( enableToolItem );
			caracteresNaoImprimiveis.setImage( imgCaracteresNaoImprimiveis );
			caracteresNaoImprimiveis.setToolTipText("Caracteres não-imprimíveis(Ctrl+F10)");
			caracteresNaoImprimiveis.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
				}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) {
						log.info("Caracteres não-imprimíveis.");
					}
					try {
						DecisaoStandardToolBar.this.unoWrapper.caracteresNaoImprimiveis();
					} catch (BeanUnoWrapperException e1) {
						log.error("Erro a mostrar caracteres não imprimíveis.", e1);
					}
				}

			});
			*/
			
			new ToolItem(toolBar, SWT.SEPARATOR);
			
			/* Adiciona botão para colocar em maíusculas as letras iniciais. */
			ToolItem uppercase = new ToolItem(toolBar, SWT.PUSH);
			uppercase.setEnabled( enableToolItem );
			uppercase.setImage( imgUppercase );
			uppercase.setToolTipText("Iniciais em maíucula");
			uppercase.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {}

				public void widgetSelected(SelectionEvent e) {
					if (log.isInfoEnabled()) 
						log.info("Convertendo iniciais para maiúscula.");
					
					unoWrapper.executeDispatch(".uno:ChangeCaseToTitleCase", null);
				}
			});
			
			/* Adiciona botão para colocar em minúsculas as letras iniciais. */			
			ToolItem lowercase = new ToolItem(toolBar, SWT.PUSH);
			lowercase.setEnabled( enableToolItem );
			lowercase.setImage( imgLowercase );
			lowercase.setToolTipText("Iniciais em minúscula");
			lowercase.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {}

				public void widgetSelected(SelectionEvent e) {					
					if (log.isInfoEnabled()) 
						log.info("Convertendo iniciais para minúscula.");
					
					unoWrapper.executeDispatch(".uno:ChangeCaseToLower", null);					
				}
			});
				


		} catch (java.lang.Exception e) {
			log.error("Erro ao carregar modelo", e);
		}

		toolBar.setBackgroundImage(imgBarBack);

		toolBar.pack();
		composite.pack();

		salvarDispatch = new PluginDispatchSelectionListener(tabController, DecisaoActions.ACAO_SALVAR_DOCUMENTO);
	}

}