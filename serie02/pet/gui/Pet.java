package pet.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;

import pet.model.PetModel;
import pet.model.StdPetModel;

public class Pet {
    
    private JFrame frame;
    private JLabel statusBar;
    private JTextArea editor;
    private JScrollPane scroller;
    private PetModel model;
    private Map<Item, JMenuItem> menuItems;

    // CONSTRUCTEUR
    
    public Pet() {
        createModel();
        createView();
        placeMenuItemsAndMenus();
        placeComponents();
        createController();
    }
    
    //COMMANDE
    
    public void display() {
        setItemsEnabledState();
        updateStatusBar();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // OUTILS
    
    private void createModel() {
        model = new StdPetModel();
    }

    private void createView() {
        final Dimension prefSize = new Dimension(640, 480);
        final int fontSize = 14;

        frame = new JFrame("Petit Éditeur de Texte");
        frame.setPreferredSize(prefSize);
        
        editor = new JTextArea();
        editor.setBackground(Color.BLACK);
        editor.setForeground(Color.LIGHT_GRAY);
        editor.setCaretColor(Color.RED);
        editor.setFont(new Font("Courier New", Font.PLAIN, fontSize));
        
        scroller = new JScrollPane();
        
        statusBar = new JLabel();
        
        menuItems = getMenuItemsMap();
    }
    
    /**
     * Création de la correspondance Item -> JMenuItem.
     */
    private Map<Item, JMenuItem> getMenuItemsMap() {
        /*****************/
        /** A COMPLETER **/
    	Map<Item, JMenuItem> m = new HashMap<Item, JMenuItem>();
    	for (Item it : Item.values()) {
    		m.put(it, new JMenuItem(it.label()));
    	}
    	return m;
        /*****************/
    }
    
    /**
     * Place les menus et les éléments de menu sur une barre de menus, et cette
     *  barre de menus sur la fenêtre principale.
     */
    private void placeMenuItemsAndMenus() {
        /*****************/
        /** A COMPLETER **/
    	JMenuBar menuBar = new JMenuBar();
    	for (Menu m : Menu.STRUCT.keySet()) {
    		JMenu menu = new JMenu(m.label());
    		for (Item it : Menu.STRUCT.get(m)) {
    			if (it == null) {
    				menu.addSeparator();
    			} else {
    				menu.add(this.menuItems.get(it));
    			}
    		}
    		menuBar.add(menu);
    	}
    	this.frame.setJMenuBar(menuBar);
        /*****************/
    }
    
    private void placeComponents() {
        frame.add(scroller, BorderLayout.CENTER);
        
        JPanel p = new JPanel(new GridLayout(1, 0));
        { //--
            p.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                    BorderFactory.createEmptyBorder(3, 5, 3, 5)
                )
            );
            p.add(statusBar);
        } //--
        
        frame.add(p, BorderLayout.SOUTH);
    }
    
    private void createController() {
        /*
         * L'opération de fermeture par défaut ne doit rien faire car on se
         *  charge de tout dans un écouteur qui demande confirmation puis
         *  libère les ressources de la fenêtre en cas de réponse positive.
         */
        /*****************/
        /** A COMPLETER **/
    	this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        /*****************/
        
        /*
         * Observateur du modèle.
         */
        /*****************/
        /** A COMPLETER **/
    	model.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				setItemsEnabledState();
        		updateStatusBar();
        		updateScrollerAndEditorComponents();
			}
    		
    	});
        /*****************/
        
        /*
         * Écouteurs des items du menu
         */
        menuItems.get(Item.NEW).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*****************/
                /** A COMPLETER **/
            	if (confirmAction()) {
            		model.setNewDocWithoutFile();
            	}
                /*****************/
            }
        });
        menuItems.get(Item.NEW_FROM_FILE).addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    /*****************/
                    /** A COMPLETER **/
                	if (confirmAction()) {
                		try {
							model.setNewDocFromFile(selectLoadFile());
						} catch (IOException exception) {
							displayError("Erreur d'ouverture du fichier");
						}
                	}
                    /*****************/
                }
            }
        );
        menuItems.get(Item.OPEN).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*****************/
                /** A COMPLETER **/
            	if (confirmAction()) {
            		try {
						model.setNewDocAndNewFile(selectLoadFile());
					} catch (IOException exception) {
						displayError("Erreur d'ouverture du fichier");
					}
            	}
                /*****************/
            }
        });
        menuItems.get(Item.REOPEN).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*****************/
                /** A COMPLETER **/
            	if (confirmAction()) {
            		try {
						model.setNewDocAndNewFile(model.getFile());
					} catch (IOException exception) {
						displayError("Erreur d'ouverture de fichier");
					}
            	}
                /*****************/
            }
        });
        menuItems.get(Item.SAVE).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*****************/
                /** A COMPLETER **/
            	try {
					model.saveCurrentDocIntoCurrentFile();
				} catch (IOException exception) {
					displayError("Erreur de sauvegarde");
				}
                /*****************/
            }
        });
        menuItems.get(Item.SAVE_AS).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*****************/
                /** A COMPLETER **/
            	try {
            		File f = selectSaveFile();
            		if (!confirmReplaceContent(f)) {
            			return;
            		}
					model.saveCurrentDocIntoFile(f);
				} catch (IOException exception) {
					displayError("Erreur de sauvegarde");
				}
                /*****************/
            }
        });
        menuItems.get(Item.CLOSE).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*****************/
                /** A COMPLETER **/
            	if (confirmAction()) {
            		model.removeDocAndFile();
            	}
                /*****************/
            }
        });
        menuItems.get(Item.CLEAR).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*****************/
                /** A COMPLETER **/
            	if (confirmAction()) {
            		model.clearDocument();
            	}
                /*****************/
            }
        });
        menuItems.get(Item.QUIT).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*****************/
                /** A COMPLETER **/
            	if (confirmAction()) {
            		frame.dispose();
            	}
                /*****************/
            }
        });
    }
    
    /**
     * Gère l'état de la disponibilité des éléments du menu en fonction de
     *  l'état du modèle.
     */
    private void setItemsEnabledState() {
        /*****************/
        /** A COMPLETER **/
    	
    	Document doc = model.getDocument();
    	boolean hasDoc = model.getDocument() != null;
    	boolean hasFile = model.getFile() != null;
    	boolean isSync = model.isSynchronized();
    	
    	// Effacer
    	boolean enabledClear = hasDoc && doc.getLength() != 0;
    	menuItems.get(Item.CLEAR).setEnabled(enabledClear);
    	
    	// Reouvrir et Sauvegarder
    	boolean enabledReopenAndSave = hasDoc && hasFile && !isSync;
    	menuItems.get(Item.REOPEN).setEnabled(enabledReopenAndSave);
    	menuItems.get(Item.SAVE).setEnabled(enabledReopenAndSave);
    	
    	// Sauvegarder comme et fermer
    	menuItems.get(Item.CLOSE).setEnabled(hasDoc);
    	menuItems.get(Item.SAVE_AS).setEnabled(hasDoc);
        /*****************/
    }
    
    /**
     * Met à jour le Viewport du JScrollPane en fonction de la présence d'un
     *  document dans le modèle.
     * Remplace le document de la zone de texte par celui du modèle quand c'est
     *  nécessaire.
     */
    private void updateScrollerAndEditorComponents() {
        /*****************/
        /** A COMPLETER **/
    	if (model.getDocument() == null) {
    		scroller.setViewportView(null);
    	} else {
    		if (!editor.getDocument().equals(model.getDocument())) {
    			editor.setDocument(model.getDocument());
    		}
    		scroller.setViewportView(editor);
    	}
        /*****************/
    }
    
    /**
     * Met à jour la barre d'état.
     */
    private void updateStatusBar() {
        /*****************/
        /** A COMPLETER **/
    	final boolean hasDoc = model.getDocument() != null;
    	final boolean hasFile = model.getFile() != null;
    	final boolean isSync = model.isSynchronized();
    	
    	final String notSave = ((hasDoc && !hasFile) || (!isSync)) ?
    			"* " : "";
    	final String path = hasFile ? 
    			model.getFile().getAbsolutePath() : "<aucun>";
    	this.statusBar.setText("Fichier : " + notSave + path);
        /*****************/
    }
    
    /**
     * Demande une confirmation de poursuite d'action.
     * @post
     *     result == true <==>
     *         le modèle était synchronisé
     *         || il n'y avait pas de document dans le modèle
     *         || le document était en cours d'édition mais l'utilisateur
     *            a répondu positivement à la demande de confirmation
     */
    private boolean confirmAction() {
        /*****************/
        /** A COMPLETER **/
    	final String title = "Confirmation de poursuite";
    	final String msg = "Voulez-vous continuer ?";
    	final int optMessage = JOptionPane.YES_NO_OPTION;
    	final int typeMessage =  JOptionPane.QUESTION_MESSAGE;
    	int result = JOptionPane.showConfirmDialog(frame, msg, 
    			title, optMessage, typeMessage);
    	return result == JOptionPane.YES_OPTION;
        /*****************/
    }
    
    /**
     * Demande une confirmation d'écrasement de fichier.
     * @pre
     *     f != null
     * @post
     *     result == true <==>
     *         le fichier n'existe pas
     *         || le fichier existe mais l'utilisateur a répondu positivement
     *            à la demande de confirmation
     */
    private boolean confirmReplaceContent(File f) {
        /*****************/
        /** A COMPLETER **/
    	if (!f.exists()) {
    		return true;
    	}
    	final String title = "Confirmation d'�crasement de fichier";
    	final String msg = "Voulez-vous �craser le fichier ?";
    	final int optMessage = JOptionPane.YES_NO_OPTION;
    	final int typeMessage =  JOptionPane.QUESTION_MESSAGE;
    	int result = JOptionPane.showConfirmDialog(frame, msg, 
    			title, optMessage, typeMessage);
    	return result == JOptionPane.YES_OPTION;
        /*****************/
    }
    
    /**
     * Toute erreur inexpliquée de l'application doit être interceptée et
     *  transformée en message présenté dans une boite de dialogue.
     */
    private void displayError(String m) {
        /*****************/
        /** A COMPLETER **/
    	final String title = "Erreur !";
    	final int optMessage = JOptionPane.OK_OPTION;
    	final int typeMessage =  JOptionPane.ERROR_MESSAGE;
    	JOptionPane.showConfirmDialog(frame, m, 
    			title, optMessage, typeMessage);
        /*****************/
    }
    
    /**
     * Permet au client de sélectionner un fichier de sauvegarde en choisissant
     *  un nom de fichier à l'aide d'un JFileChooser.
     * Si le nom de fichier choisi n'existe pas encore, un nouveau fichier est
     *  créé avec ce nom.
     * Retourne null si et seulement si :
     * - l'utilisateur a annulé l'opération,
     * - le nom choisi correspond à un élément préexistant du système de fichier
     *   mais cet élément n'est pas un fichier
     * - le nom choisi ne correspond pas à un élément préexistant du système de
     *   fichier mais le fichier n'a pas pu être créé.
     * Dans les deux derniers cas, une boite de dialogue vient de plus décrire
     *  le problème.
     */
    private File selectSaveFile() {
        /*****************/
        /** A COMPLETER **/
    	JFileChooser chooser = new JFileChooser();
    	chooser.showSaveDialog(frame);
    	return chooser.getSelectedFile();
        /*****************/
    }
    
    /**
     * Permet au client de sélectionner un fichier à charger en choisissant
     *  un nom de fichier à l'aide d'un JFileChooser.
     * Retourne null si et seulement si :
     * - l'utilisateur a annulé l'opération,
     * - le nom choisi ne correspond pas un fichier existant.
     * Dans ce dernier cas, une boite de dialogue vient de plus décrire
     *  le problème.
     */
    private File selectLoadFile() {
        /*****************/
        /** A COMPLETER **/
    	JFileChooser chooser = new JFileChooser();
    	chooser.showOpenDialog(frame);
    	return chooser.getSelectedFile();
        /*****************/
    }
}
