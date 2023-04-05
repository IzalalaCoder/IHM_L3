package motion.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JLabel;

public class SimpleComponent extends JLabel implements Animable {

    // ATTRIBUTS
	
	static final long serialVersionUID = 1L;
	private final Random r;

    // CONSTRUCTEURS

    public SimpleComponent(int w, int h) {
        super(" ");
        setOpaque(true);
        setPreferredSize(new Dimension(w, h));
        r = new Random();
        setBackground(new Color(r.nextInt()));
    }

    // COMMANDE

    @Override
    public void animate() {
        setBackground(new Color(r.nextInt()));
    }
}
