package gened.v5;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import gened.utils.DefaultTreeCellEditor2;
import gened.utils.GenCellEditor;
import gened.utils.GenCellRenderer;
import gened.utils.Person;

public class GenTree extends JTree {

    // ATTRIBUTS

    // tous les éléments du menu surgissant (qui correspondent aux
    // constantes de Item)
    private Map<Item, JMenuItem> menuItems;
    private JPopupMenu menu;

    // CONSTRUCTEURS

    public GenTree() {
        super((TreeNode) null);
        createView();
        createController();
        updateMenuItems();
    }

    // REQUETES

    @Override
    public DefaultTreeModel getModel() {
        return (DefaultTreeModel) super.getModel();
    }

    // OUTILS

    private void createView() {
        /*****************/
        /** A COMPLETER **/
        /*****************/
    }

    private JPopupMenu createAndGetConfiguredPopupMenu() {
        /*****************/
        /** A COMPLETER **/
        /*****************/
    }
    
    private Map<Item, JMenuItem> getMenuItemsMap() {
        /*****************/
        /** A COMPLETER **/
        /*****************/
    }

    private void createController() {
        // écoute les changements de sélection sur l'arbre,
        // pour mettre à jour l'état d'activabilité des éléments de menu
        /*****************/
        /** A COMPLETER **/
        /*****************/
        
        // écoute les éléments de menu,
        // pour appliquer leur comportement sur l'arbre
        /*****************/
        /** A COMPLETER **/
        /*****************/
    }

    /**
     * Activation ou désactivation de chaque JMenuItem selon l'état de l'arbre.
     */
    private void updateMenuItems() {
        for (Map.Entry<Item, JMenuItem> entry : menuItems.entrySet()) {
            Item i = entry.getKey();
            JMenuItem jmi = entry.getValue();
            jmi.setEnabled(i.getEnabledValue(this));
        }
    }
    
    // TYPES IMBRIQUES

    private enum Item {
        CREATE_ROOT() {
            /**
             * Retourne true ssi t n'a pas de racine.
             */
            @Override boolean getEnabledValue(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
            /**
             * @pre t n'a pas de racine
             * @post t possède une nouvelle racine (de genre aléatoire),
             *       et cette racine est maintenant sélectionnée
             */
            @Override void applyOn(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
        },
        CREATE_BROTHER_BEFORE() {
            /**
             * Retourne true ssi un nœud de t est sélectionné, et ce n'est pas
             *  la racine.
             */
            @Override boolean getEnabledValue(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
            /**
             * @pre un nœud de t est sélectionné, et ce n'est pas la racine
             * @post t possède un nouveau nœud (de genre aléatoire) juste avant
             *       le nœud précédemment sélectionné, et ce nouveau nœud est
             *       maintenant sélectionné
             */
            @Override void applyOn(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
        },
        CREATE_BROTHER_AFTER() {
            /**
             * Retourne true ssi un nœud de t est sélectionné, et ce n'est pas
             *  la racine.
             */
            @Override boolean getEnabledValue(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
            /**
             * @pre un nœud de t est sélectionné, et ce n'est pas la racine
             * @post t possède un nouveau nœud (de genre aléatoire) juste après
             *       le nœud précédemment sélectionné, et ce nouveau nœud est
             *       maintenant sélectionné
             */
            @Override void applyOn(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
        },
        CREATE_FIRST_SON() {
            /**
             * Retourne true ssi un nœud de t est sélectionné, et qu'il n'a pas
             *  de fils.
             */
            @Override boolean getEnabledValue(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
            /**
             * @pre un nœud de t est sélectionné, et il n'a pas de fils
             * @post t possède un nouveau nœud (de genre aléatoire) juste
             *       au-dessous du nœud précédemment sélectionné, et ce nouveau
             *       nœud est maintenant sélectionné
             */
            @Override void applyOn(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
        },
        DELETE_SELECTION() {
            /**
             * Retourne true ssi un nœud de t est sélectionné.
             */
            @Override boolean getEnabledValue(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
            /**
             * @pre un nœud de t est sélectionné
             * @post le nœud qui était sélectionné dans t a été retiré,
             *       et plus aucun nœud de t n'est sélectionné
             */
            @Override void applyOn(GenTree t) {
                /*****************/
                /** A COMPLETER **/
                /*****************/
            }
        };

        static final Item[] STRUCT = new Item[] {
                CREATE_ROOT, null,
                CREATE_FIRST_SON, null,
                CREATE_BROTHER_BEFORE, CREATE_BROTHER_AFTER, null,
                DELETE_SELECTION
        };
        
        @Override
        public String toString() {
            return name().toLowerCase().replace('_', ' ');
        }
        
        /**
         * Indique si le JMenuItem correspondant à this doit être activable
         *  ou non, en fonction de l'état de t.
         */
        abstract boolean getEnabledValue(GenTree t);

        /**
         * Applique sur t le comportement du JMenuItem correspondant à this.
         */
        abstract void applyOn(GenTree t);
    }
}
