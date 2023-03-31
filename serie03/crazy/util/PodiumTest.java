package crazy.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import crazy.gui.AnimalColor;
import crazy.model.PodiumModel;
import crazy.model.StdPodiumModel;
import crazy.view.Podium;

public class PodiumTest {
    
    // ATTRIBUTS
    
    private JFrame frame;
    private JButton test, change;
    private Podium<AnimalColor> podium;

    // CONSTRUCTEURS
    
    PodiumTest() {
        createView();
        placeComponents();
        createController();
    }

    // COMMANDES
    
    void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // OUTILS

    private PodiumModel<AnimalColor> createModel(int n) {
        List<AnimalColor> animals = new ArrayList<AnimalColor>();
        AnimalColor[] vals = AnimalColor.values();
        for (int i = 0; i < Math.min(n, vals.length); i++) {
            animals.add(vals[i]);
        }
        return new StdPodiumModel<AnimalColor>(animals, n + 2);
    }
    
    private void createView() {
        podium = new Podium<AnimalColor>(createModel(3));
        frame = new JFrame();
        test = new JButton("Test");
        change = new JButton("Change Model");
    }

    private void placeComponents() {
        JPanel p = new JPanel();
        { //--
            p.add(test);
            p.add(change);
        } //--
        frame.add(p, BorderLayout.NORTH);
        p = new JPanel();
        { //--
            p.add(podium);
        } //--
        frame.add(p);
    }

    private void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PodiumModel<AnimalColor> m = podium.getModel();
                AnimalColor animal = m.bottom();
                m.removeBottom();
                m.addTop(animal);
            }
        });
        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = (int) (Math.random() * 4) + 3;
                podium.setModel(createModel(n));
                refreshFrame();
            }
        });
    }
    
    /**
     * Actualise le conteneur du podium, ainsi que la fenêtre si sa taille
     *  préférée a changé.
     */
    private void refreshFrame() {
        ((JPanel) podium.getParent()).revalidate();
        Dimension old = frame.getSize();
        Dimension d = frame.getPreferredSize();
        if (!d.equals(old)) {
            frame.setSize(d);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PodiumTest().display();
            }
        });
    }
}
