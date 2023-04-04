package gened.utils;

import static gened.utils.GenCellRenderer.FONT_NAME;
import static gened.utils.GenCellRenderer.FONT_SIZE;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

public class GenCellEditor extends DefaultCellEditor {

    // CONSTANTES

	private static final long serialVersionUID = 1L;

	public static final char FIELD_SEP = ',';

    private static final Gender[] GENDERS = Gender.values();
    
    // CONSTRUCTEURS
    
    public GenCellEditor() {
        super(new JTextField());
        /*
         * Le JTextField passé au constructeur est l'éditeur de cellules
         *  (l'attribut editorComponent du DefaultCellEditor).
         */
        final JTextField tf = (JTextField) editorComponent;
        tf.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
        tf.setBorder(UIManager.getBorder("Tree.editorBorder"));
        /*
         * Le constructeur de DefaultCellEditor a associé à tf un EditorDelegate
         *  qui ne sait pas gérer l'édition comme demandé dans l'énoncé (avec
         *  le genre), donc on le remplace par un autre EditorDelegate mieux
         *  adapté à la situation.
         */
        tf.removeActionListener(delegate);
        delegate = new EditorDelegate() {
        	
			private static final long serialVersionUID = 1L;
			/*
             * C'est à travers cette méthode que l'on transforme la valeur de
             *  editorComponent en une instance de Person.
             */
            @Override
            public Person getCellEditorValue() {
                return toPerson(tf.getText());
            }
            @Override
            public void setValue(Object value) {
                tf.setText((value != null) ? value.toString() : "");
            }
        };
        tf.addActionListener(delegate);
        setClickCountToStart(1);
    }

    // REQUETES

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value,
            boolean isSelected,
            boolean expanded,
            boolean leaf, int row) {
        Person p = (Person) ((DefaultMutableTreeNode) value).getUserObject();
        String sv = toString(p);
        return super.getTreeCellEditorComponent(tree, sv, isSelected, expanded,
                leaf, row);
    }
    
    // OUTILS

    /**
     * Reconstruit une personne à partir d'une chaîne de caractères.
     * Si la chaîne est de la forme "<nom><FIELD_SEP><ordinal>" la personne
     *  retournée a pour genre FEMALE (0) ou MALE (1).
     * Sinon, value est le nom, et le genre est FEMALE par défaut.
     */
    private static Person toPerson(String value) {
        String[] parts = value.split(String.valueOf(FIELD_SEP));
        int ordinal = getOrdinal(parts);
        String name = value;
        if (ordinal >= 0) {
            // le dernier élément de parts était un ordinal,
            // on reconstruit name en supprimant cet ordinal
            name = concatButLast(parts);
        } else {
            // le dernier élément de parts n'était pas un ordinal,
            // ordinal prend la valeur 0 et name reste inchangé
            ordinal = 0;
        }
        return new Person(GENDERS[ordinal], name);
    }

    /**
     * Retourne l'ordinal présent dans la dernière partie du tableau.
     * Si le tableau est vide ou si sa dernière partie ne contient pas d'ordinal
     *  alors retourne -1.
     */
    private static int getOrdinal(String[] parts) {
        if (parts.length == 0) {
            return -1;
        }
        
        int ordinal = -1;
        try {
            String val = parts[parts.length - 1].trim();
            ordinal = Integer.valueOf(val);
        } catch (NumberFormatException e) {
            // rien : ordinal reste à -1
        }
        if (ordinal < -1 || ordinal >= GENDERS.length) {
            ordinal = -1;
        }
        return ordinal;
    }

    /**
     * Retourne la concaténation des éléments de parts sauf le dernier, ou la
     *  chaîne vide si parts est vide.
     */
    private static String concatButLast(String[] parts) {
        StringBuilder s = new StringBuilder("");
        if (parts.length > 0) {
            s.append(parts[0]);
            for (int i = 1; i < parts.length - 1; i++) {
                s.append(FIELD_SEP).append(parts[i]);
            }
        }
        return s.toString();
    }

    /**
     * Construit une représentation textuelle de p de la forme :
     *  "<nom><FIELD_SEP><ordinal>".
     */
    private static String toString(Person p) {
        return p.getName() + FIELD_SEP + p.getGender().ordinal();
    }
}
