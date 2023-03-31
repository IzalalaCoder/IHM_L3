package serie01;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowFocusListener;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.SwingUtilities;

public class EventsTester {
	
	// CONSTANTES
	
	private final String TITLE_MAIN = "Tests sur les évènements "
			+ "- ZONE D'AFFICHAGE";
	private final String TITLE_BTN = "Nouvelle fenêtre";
	private final String TITLE_TEST = "Zone de test";
	private final String TITLE_RAZ = "RAZ Compteur";
	private final String TITLE_Q = "Destruction de fenêtre en cours";
	private final String CONTENT_Q = "Voulez-vous vraiment détruire"
			+ " la fenêtre de test en cours ? ";
	
	// ATTRIBUTS 
	
	private JFrame mainFrame;
	private JFrame testFrame;
	private JButton btnNewTestFrame;
	private JButton razCount;
	private static Map<String, JTextArea> events = new HashMap<String, JTextArea>();
	private static int count = 0;
	private int countRAZ;
	
	// CONSTRUCTEURS
	
	public EventsTester() {
		this.countRAZ = 0;
		this.initMap();
		this.createView();
		this.placeComponents();
		this.createController();
	}
	
	// REQUETES
	
	// COMMANDES
	
	public void display() {
		this.mainFrame.setPreferredSize(new Dimension(600, 660));
		this.mainFrame.pack();
		this.mainFrame.setLocationRelativeTo(null);
		this.mainFrame.setVisible(true);
	}
	
	// OUTILS
	
	private void initMap() {
		for (Event e : Event.values()) {
			JTextArea txt = new JTextArea(10, 20);
			txt.setEditable(false);
			events.put(e.getLabel(), txt);
		}
	}
	
	private void createView() {
		mainFrame = new JFrame(TITLE_MAIN);
		mainFrame.setResizable(false);
		btnNewTestFrame = new JButton(TITLE_BTN);
		razCount = new JButton(TITLE_RAZ);
	}
	
	private void placeComponents() {
		JPanel p = new JPanel();
		{ // --
			p.add(this.btnNewTestFrame);
			p.add(this.razCount);
		} 
		mainFrame.add(p, BorderLayout.NORTH);
		p = new JPanel(new GridLayout(0, 3));
		p.setPreferredSize(new Dimension(500, 500));
		{ // --  
			for (String str : events.keySet()) {
				JPanel q = new JPanel();
				//q.setSize(new Dimension(300, 300));
				{ // --
					JScrollPane scroll = new JScrollPane(events.get(str));
					q.add(scroll);
				}
				q.setBorder(BorderFactory.createTitledBorder(str));
				p.add(q);
			}
		}
		JScrollPane s = new JScrollPane(p);
		mainFrame.add(s, BorderLayout.CENTER);
	}
	
	private void createController() {
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.razCount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				count = 0;
				countRAZ += 1;
				for (String str : events.keySet()) {
					events.get(str).append("--- RAZ " + countRAZ + " ---\n");
				}
			}
			
		});
		
		this.btnNewTestFrame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (testFrame != null) {
					int response = JOptionPane.showConfirmDialog(mainFrame, 
							CONTENT_Q, TITLE_Q, 
							JOptionPane.YES_NO_CANCEL_OPTION, 
							JOptionPane.QUESTION_MESSAGE);
					if (response != JOptionPane.YES_OPTION) {
						testFrame.toFront();
						testFrame.requestFocus();
						return;
					} else {
						removeControllerTest();
						testFrame.dispose();
						for (String str : events.keySet()) {
							events.get(str).setText("");
						}
						count = 0;
					}
				}
				createNewTestFrame();
				createControllerTest();
				testFrame.pack();
				testFrame.setLocationRelativeTo(mainFrame);
				testFrame.setVisible(true);
			}
			
		});
	}
	
	private void createNewTestFrame() {
		this.testFrame = new JFrame(TITLE_TEST);
		testFrame.setPreferredSize(new Dimension(200, 100));
	}
	
	private void createControllerTest() {
		this.testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		for (Event e : Event.values()) {
			e.addEvent(testFrame);
		}
	}
	
	private void removeControllerTest() {
		for (Event e : Event.values()) {
			e.remEvent(testFrame);
		}
	}
	
	// POINT D'ENTREE
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				EventsTester tester = new EventsTester();
				tester.display();
			}
			
		});
	}
	
	private static void generateEvent(String str, String msg) {
		count += 1;
		events.get(str).append(count + " " + msg);
	}
	
	// TYPE IMBRIQUE
	
	private enum Event {
		MOUSE("MouseListener"){
			@Override
			public void addEvent(JFrame frame) {
				frame.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent e) {
						generateEvent("MouseListener", "MOUSE_CLICKED\n");
					}

					@Override
					public void mousePressed(MouseEvent e) {
						generateEvent("MouseListener", "MOUSE_PRESSED\n");
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						generateEvent("MouseListener", "MOUSE_RELEASED\n");
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						generateEvent("MouseListener", "MOUSE_ENTERED\n");
					}

					@Override
					public void mouseExited(MouseEvent e) {
						generateEvent("MouseListener", "MOUSE_EXITED\n");
					}
					
				}); 
			}
			
			@Override
			public void remEvent(JFrame frame) {
				for (MouseListener l : frame.getMouseListeners()) {
					frame.removeMouseListener(l);
				}
			}
		},
		WINDOWFOCUS("WindowFocusListener") {
			@Override
			public void addEvent(JFrame frame) {
				frame.addWindowFocusListener(new WindowFocusListener() {

					@Override
					public void windowGainedFocus(WindowEvent e) {
						generateEvent("WindowFocusListener", "WINDOW_GAINED_FOCUS\n");
					}

					@Override
					public void windowLostFocus(WindowEvent e) {
						generateEvent("WindowFocusListener", "WINDOW_LOST_LISTENER\n");
					}
					
				});
			}
			
			@Override
			public void remEvent(JFrame frame) {
				for (WindowFocusListener l : frame.getWindowFocusListeners()) {
					frame.removeWindowFocusListener(l);
				}
			}
		},
		WINDOW("WindowListener") {
			@Override
			public void addEvent(JFrame frame) {
				frame.addWindowListener(new WindowListener() {

					@Override
					public void windowOpened(WindowEvent e) {
						generateEvent("WindowListener", "WINDOW_OPENED\n");
					}

					@Override
					public void windowClosing(WindowEvent e) {
						generateEvent("WindowListener", "WINDOW_CLOSING\n");
					}

					@Override
					public void windowClosed(WindowEvent e) {
						generateEvent("WindowListener", "WINDOW_CLOSED\n");
					}

					@Override
					public void windowIconified(WindowEvent e) {
						generateEvent("WindowListener", "WINDOW_ICONIFIED\n");
					}

					@Override
					public void windowDeiconified(WindowEvent e) {
						generateEvent("WindowListener", "WINDOW_DEICONIFIED\n");
					}

					@Override
					public void windowActivated(WindowEvent e) {
						generateEvent("WindowListener", "WINDOW_ACTIVATED\n");
					}

					@Override
					public void windowDeactivated(WindowEvent e) {
						generateEvent("WindowListener", "WINDOW_DEACTIVATED\n");
					}
					
				});
			}
			
			@Override
			public void remEvent(JFrame frame) {
				for (WindowListener l : frame.getWindowListeners()) {
					frame.removeWindowListener(l);
				}
			}
		},
		KEY("KeyListener") {
			public void addEvent(JFrame frame) {
				frame.addKeyListener(new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {
						generateEvent("KeyListener", "KEY_TYPED\n");
					}

					@Override
					public void keyPressed(KeyEvent e) {
						generateEvent("KeyListener", "KEY_PRESSED\n");
					}

					@Override
					public void keyReleased(KeyEvent e) {
						generateEvent("KeyListener", "KEY_RELEASED\n");
					}
					
				});
			}
			
			@Override
			public void remEvent(JFrame frame) {
				for (KeyListener l : frame.getKeyListeners()) {
					frame.removeKeyListener(l);
				}
			}
		}, 
		WINDOWSTATE("WindowStateListener") {
			@Override
			public void addEvent(JFrame frame) {
				frame.addWindowStateListener(new WindowStateListener() {

					@Override
					public void windowStateChanged(WindowEvent e) {
						generateEvent("WindowStateListener", "WINDOW_STATE_CHANGED\n");
					}
					
				});
			}
			
			@Override
			public void remEvent(JFrame frame) {
				for (WindowStateListener l : frame.getWindowStateListeners()) {
					frame.removeWindowStateListener(l);
				}
			}
		}, 
		MOUSEWHEEL("MouseWheelListener") {
			@Override
			public void addEvent(JFrame frame) {
				frame.addMouseWheelListener(new MouseWheelListener() {

					@Override
					public void mouseWheelMoved(MouseWheelEvent e) {
						generateEvent("MouseWheelListener", "MOUSE_WHEEL_MOVED\n");
					}
					
				});
			}
			
			@Override
			public void remEvent(JFrame frame) {
				for (MouseWheelListener l : frame.getMouseWheelListeners()) {
					frame.removeMouseWheelListener(l);
				}
			}
		},
		MOUSEMOTION("MouseMotionListener") {
			@Override
			public void addEvent(JFrame frame) {
				frame.addMouseMotionListener(new MouseMotionListener() {

					@Override
					public void mouseDragged(MouseEvent e) {
						generateEvent("MouseMotionListener", "MOUSE_DRAGGED\n");
					}

					@Override
					public void mouseMoved(MouseEvent e) {
						generateEvent("MouseMotionListener", "MOUSE_MOVED\n");
					}
					
				});
			}
			
			@Override
			public void remEvent(JFrame frame) {
				for (MouseMotionListener l : frame.getMouseMotionListeners()) {
					frame.removeMouseMotionListener(l);
				}
			}
		};
		
		// ATTRIBUTS
		
		private String label;
		
		// CONSTRUCTEUR
		
		private Event(String l) {
			this.label = l;
		}
		
		// REQUETES
		
		public String getLabel() {
			return this.label;
		}
		
		// COMMANDES 
		
		abstract public void addEvent(JFrame frame);
		abstract public void remEvent(JFrame frame);
	}
}
