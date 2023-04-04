package gened.utils;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public enum Gender {
    FEMALE("fille", new Color(255, 175, 175), new Color(191, 88, 88)),
    MALE("fils", new Color(200, 210, 255), new Color(99, 130, 191));

    /*
     * Attention : pour que les images soient chargeables, le répertoire
     *  "images" doit être au même niveau que "gened" et dans le chemin de
     *  génération !
     */
    private static final ImageIcon[] ICONS = new ImageIcon[] {
        // ces icones proviennent de https://www.fatcow.com/free-icons
        createImageIcon("/images/women.png"),
        createImageIcon("/images/man.png")
    };

    // ATTRIBUTS

    private String desc;
    private Color background;
    private Color border;

    // CONSTRUCTEURS

    Gender(String d, Color bk, Color bd) {
        desc = d;
        background = bk;
        border = bd;
    }

    // REQUETES

    public String getDesc() {
        return desc;
    }

    public Color getBackgroundSelectionColor() {
        return background;
    }

    public Color getBorderSelectionColor() {
        return border;
    }

    public ImageIcon getImage() {
        return ICONS[ordinal()];
    }

    // OUTILS

    private static ImageIcon createImageIcon(String path) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        java.net.URL imgURL = Gender.class.getResource(path);
        if (imgURL == null) {
            System.err.println("Aucune ressource de nom " + path);
            return null;
        }
        return new ImageIcon(tk.getImage(imgURL));
    }
}
