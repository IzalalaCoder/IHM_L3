package crazy.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import crazy.model.Order;
import crazy.model.PodiumManager;
import crazy.model.PodiumModel;
import crazy.model.StdPodiumManager;
import crazy.model.PodiumManager.Rank;
import crazy.view.Drawable;
import crazy.view.Podium;

public class CrazyCircus<E extends Drawable> {

    // ATTRIBUTS

    private PodiumManager<E> manager;
    
    private JFrame frame;
    private Map<Rank, Podium<E>> allPodiums;
    private Map<Order, JButton> commandButtons;
    private JButton restart;
    private JTextArea output;
    private JCheckBox soAllower;

    // CONSTRUCTEURS

    public CrazyCircus(Set<E> drawables) {
        createModel(drawables);
        createView();
        placeComponents();
        createController();
    }

    // COMMANDES

    public void display() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // OUTILS

    private void createModel(Set<E> drawables) {
        manager = new StdPodiumManager<E>(drawables);
    }
    
    private void createView() {
        frame = new JFrame("Crazy Circus");
        commandButtons = getCommandButtons();
        restart = new JButton("Nouvelle Partie");
        output = getOutput();
        soAllower = new JCheckBox("Autoriser SO");
        allPodiums = getAllPodiums();
    }
    
    private JTextArea getOutput() {
        final int outRowsNb = 4;
        final int outColsNb = 10;

        JTextArea out = new JTextArea(outRowsNb, outColsNb);
        out.setEditable(false);
        out.setLineWrap(true);
        return out;
    }
    
    private Map<Order, JButton> getCommandButtons() {
        Map<Order, JButton> cmds = new EnumMap<Order, JButton>(Order.class);
        for (Order o : Order.values()) {
            JButton b = new JButton(o.label());
            b.setName(o.name());
            cmds.put(o, b);
        }
        return cmds;
    }
    
    private Map<Rank, Podium<E>> getAllPodiums() {
        Map<Rank, PodiumModel<E>> models = manager.getModels();
        Map<Rank, Podium<E>> podiums = new EnumMap<Rank, Podium<E>>(Rank.class);
        for (Rank r : Rank.values()) {
            podiums.put(r, new Podium<E>(models.get(r)));
        }
        return podiums;
    }

    private void placeComponents() {
        JPanel p = new JPanel(new BorderLayout());
        { //--
            JPanel q = new JPanel(new GridLayout(0, 1));
            { //--
                for (Order o : Order.values()) {
                    q.add(commandButtons.get(o));
                }
                JPanel r = new JPanel();
                { //--
                    r.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    r.add(soAllower);
                } //--
                q.add(r);
                q.add(restart);
            } //--
            p.add(q, BorderLayout.NORTH);
        } //--
        frame.add(p, BorderLayout.EAST);

        p = new JPanel(new BorderLayout());
        { //--
            JPanel q = new JPanel(new GridLayout(1, 0));
            { //--
                for (Rank r : Rank.values()) {
                    q.add(allPodiums.get(r));
                    if (r == Rank.WORK_RIGHT) {
                        q.add(new JLabel(""));
                    }
                }
            } //--
            p.add(q, BorderLayout.CENTER);
            q = new JPanel(new GridLayout(1, 0));
            { //--
                q.add(createMinorLabel("Travail"));
                q.add(new JLabel(""));
                q.add(createMinorLabel("Objectif"));
            } //--
            p.add(q, BorderLayout.SOUTH);
        } //--
        frame.add(p, BorderLayout.WEST);

        frame.add(new JScrollPane(output), BorderLayout.SOUTH);
    }

    private JLabel createMinorLabel(String text) {
        JLabel result = new JLabel(text);
        result.setHorizontalAlignment(SwingConstants.CENTER);
        return result;
    }

    private void createController() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton b = ((JButton) e.getSource());
                Order o = Order.valueOf(b.getName());
                try {
					manager.executeOrder(o);
				} catch (PropertyVetoException e1) {
					output.append("* ");
				}
            }
        };
        
        for (Order o : Order.values()) {
            commandButtons.get(o).addActionListener(al);
        }

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.reinit();
                reinitPodiums();
                output.setText("");
            }
        });
        
        manager.addPropertyChangeListener(PodiumManager.PROP_FINISHED, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ((boolean) evt.getNewValue()) {
					enableButtons(false);
					final int step = manager.getShotsNb();
					final long time = manager.getTimeDelta();
					output.append("\nGagné en " + step + 
							" coups et " + time + "s.\n");
				} else {
					enableButtons(true);
				}
			}
        	
        });
        
        manager.addVetoableChangeListener(PodiumManager.PROP_LAST_ORDER, new VetoableChangeListener() {

			@Override
			public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
				Order o = (Order) evt.getNewValue();
				if (o == Order.SO && !soAllower.isSelected()) {
					throw new PropertyVetoException("commande non acceptée", evt);
				}
			}
        	
        });
        
        manager.addPropertyChangeListener(PodiumManager.PROP_LAST_ORDER, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Order o = (Order) evt.getNewValue();
				output.append(" " + o.name());
			}
        	
        });
    } 
    
    private void enableButtons(boolean ok) {
        for (Order o : Order.values()) {
            commandButtons.get(o).setEnabled(ok);
        }
    }
    
    private void reinitPodiums() {
        Map<Rank, PodiumModel<E>> models = manager.getModels();
        for (Rank r : Rank.values()) {
            allPodiums.get(r).setModel(models.get(r));
        }
    }
}
