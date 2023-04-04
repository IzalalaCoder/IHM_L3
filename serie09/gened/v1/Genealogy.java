package gened.v1;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import javax.swing.SwingUtilities;

import javax.swing.tree.DefaultMutableTreeNode;

public class Genealogy {
	
	// CONSTANTES
	
	private final String NAME_NODE = "Toto";
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
	}
	
	private DefaultMutableTreeNode generateTree(int position, int imbrication) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(NAME_NODE);
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
