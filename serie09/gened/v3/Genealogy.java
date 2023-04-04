package gened.v3;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import gened.utils.GenCellRenderer;
import gened.utils.Gender;
import gened.utils.Person;

public class Genealogy {
	
	// CONSTANTES
	
	private final int NBR_POSITION = 3;
	private final int NBR_IMBRICATION = 3;
	
	// ATTRIBUTS
	
	private JFrame frame;
	private JTree tree;
	
	// CONSTRUCTEURS
	
	public Genealogy() {
		this.createView();
		this.placeComponents();
		this.createController();
	}
	
	// REQUETES
	
	// COMMANDES
	
	public void display() {
		this.frame.setPreferredSize(new Dimension(300, 400));
		this.frame.setLocationRelativeTo(null);
		this.frame.pack();
		this.frame.setVisible(true);
	}
	
	// OUTILS
	
	private void createView() {
		this.frame = new JFrame();
		this.tree = new JTree(generateTree(NBR_POSITION, NBR_IMBRICATION));
		this.tree.setCellRenderer(new GenCellRenderer());
	}
	
	private DefaultMutableTreeNode generateTree(int position, int imbrication) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new Person());
		if (imbrication == 0) {
			return node;
		}
		for (int i = 0; i < position; i++) {
			node.add(generateTree(position, imbrication - 1));
		}
		return node;
	}
	
	private void placeComponents() {
		JScrollPane scroll = new JScrollPane(this.tree);
		this.frame.add(scroll);
	}
	
	private void createController() {
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// recherche de la première sélection
				DefaultMutableTreeNode node = searchFirstSelection(e);
				
				if (node == null) {
					frame.setTitle("");
					return;
				}
				
				// Ecriture du titre de la fenêtre
				String t = node.toString();
				Gender g = ((Person) node.getUserObject()).getGender();
				node = (DefaultMutableTreeNode) node.getParent();
				while (node != null) {
					Person p = (Person) node.getUserObject();
					t += ", " + g.getDesc() + " de " + p.getName();
					g = p.getGender();
					node = (DefaultMutableTreeNode) node.getParent();
				}
				frame.setTitle(t);
			}
			
		});
	}
	
	private DefaultMutableTreeNode searchFirstSelection(TreeSelectionEvent e) {
		int maxImbrication = -1;
		for (Integer x : tree.getSelectionRows()) {
			if (x >= maxImbrication) {
				maxImbrication = x;
			}
		}
		
		return (DefaultMutableTreeNode) tree.getPathForRow(maxImbrication).getLastPathComponent();
	}
	
	// POINT D'ENTREE
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Genealogy().display();
			}
			
		});
	}
}
