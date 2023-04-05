package motion.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import motion.model.Mobile;
import motion.model.StdMobile;
import util.Contract;

public class MobileComponent extends JComponent implements Animable {

	
	private static final long serialVersionUID = 1L;

	/**
     * La couleur (bleu) du rectangle statique du modèle.
     */
    private static final Color STAT_COLOR = Color.BLUE;

    /**
     * La couleur (rouge) du rectangle mobile du modèle.
     */
    private static final Color MOV_COLOR = Color.RED;

    // ATTRIBUTS

    // Le modèle du MobileComponent.
    private Mobile model;
	
	private Point lastPoint;
	private Point recentPoint;

    // CONSTRUCTEURS

    /**
     * @pre <pre>
     *     width > 0 && height > 0
     *     0 < ray <= min(width, height) / 2 </pre>
     * @post <pre>
     *     model.isMovable()
     *     model.getStaticRect() est un rectangle (0, 0, width, height)
     *     model.getMovingRect() est un rectangle (0, 0, 2 * ray, 2 * ray)
     *     model.getHorizontalShift() == 0
     *     model.getVerticalShift() == 0
     *     getPreferredSize().width == width
     *     getPreferredSize().height == height </pre>
     */
    public MobileComponent(int width, int height, int ray) {
        Contract.checkCondition(width > 0 && height > 0);
        Contract.checkCondition(ray > 0 && ray <= Math.min(width, height) / 2);

        createModel(width, height, ray);
        createView();
        createController();
    }

    // COMMANDES

    @Override
    public void animate() {
        /*****************/
        /** A COMPLETER **/
    	if (model.isMovable()) {
    		this.model.move();
    	}
        /*****************/
    }

    // OUTILS

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(STAT_COLOR);
        Rectangle sr = model.getStaticRect();
        g.fillRect(0, 0, sr.width, sr.height);
        g.setColor(MOV_COLOR);
        Rectangle mr = model.getMovingRect();
        Point tlc = mr.getLocation();
        g.fillOval(tlc.x, tlc.y, mr.width, mr.height);
        // Pour être certain du vidage immédiat du buffer d'affichage
        //  ce qui pose problème aux gestionnaires de fenêtres sous Linux et
        //  probablement aussi sous mac d'après ce que j'ai lu, mais pas sous
        //  Windows...
        Toolkit.getDefaultToolkit().sync();
    }

    private void createModel(int width, int height, int ray) {
        Dimension dim = new Dimension(width, height);
        Rectangle sr = new Rectangle(dim);
        Rectangle mr = new Rectangle(new Dimension(2 * ray, 2 * ray));
        model = new StdMobile(sr, mr);
        model.setHorizontalShift(ray / 2);
        model.setVerticalShift(ray / 2);
    }
    
    private void createView() {
        Rectangle r = model.getStaticRect();
        Dimension d = new Dimension(r.width, r.height);
        setPreferredSize(d);
    }

    private void createController() {
        /*****************/
        /** A COMPLETER **/
    	this.model.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				paintComponent(getGraphics());
			}
    		
    	});
    	
    	this.addMouseListener(new MouseHandler());
    	this.addMouseMotionListener(new MouseHandler());
        /*****************/
    }
    
    // TYPES IMBRIQUES

    private class MouseHandler extends MouseAdapter {
    	
        /*****************/
        /** A COMPLETER **/
    	
    	@Override
    	public void mousePressed(MouseEvent e) {
    		Rectangle rect = model.getMovingRect();
    		Point ptRect = rect.getLocation();
    		Point ptClick = e.getPoint();
    		Dimension dimRect = rect.getSize();
    		if (ptRect.getX() <= ptClick.getX() 
    				&& (ptRect.getX() + dimRect.getWidth()) 
    				>= ptClick.getX()) {
    			if (ptRect.getY() <= ptClick.getY() 
        				&& (ptRect.getY() + dimRect.getHeight()) 
        				>= ptClick.getY()) {
    				lastPoint = ptClick;
    				recentPoint = ptClick;
    				model.setMovable(false);    				
    			}
    		}
    		
    	}
    	
    	@Override
    	public void mouseDragged(MouseEvent e) {
    		Point p = e.getPoint();
    		if (!model.isMovable() && model.isValidCenterPosition(p)) {
    			model.setCenter(p);
    			lastPoint = recentPoint;
    			recentPoint = p;
    		}
    	}

    	@Override
    	public void mouseReleased(MouseEvent e) {
    		if (!model.isMovable()) {
    			int hs = (int) (recentPoint.getX() - lastPoint.getX());
        		int vs = (int) (recentPoint.getY() - lastPoint.getY());
        		model.setHorizontalShift(hs);
        		model.setVerticalShift(vs);
        		model.setMovable(true);
    		}
    	}
    	
        /*****************/
    }
}
