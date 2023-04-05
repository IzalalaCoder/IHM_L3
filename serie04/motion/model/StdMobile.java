package motion.model;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import util.Contract;

/**
 * Implantation standard de <code>Mobile</code>.
 */
public class StdMobile implements Mobile {

    // ATTRIBUTS

    /**
     * Le rectangle statique : le grand rectangle à l'intérieur duquel
     *  le rectangle mobile évolue.
     */
    private Rectangle statRect;

    /**
     * Le rectangle mobile : le petit rectangle qui se meut à l'intérieur
     *  du rectangle statique.
     */
    private Rectangle movRect;

    /**
     * L'accroissement horizontal lors du déplacement.
     * positif : déplacement vers la droite de l'écran
     * négatif : déplacement vers la gauche de l'écran
     */
    private int dx;

    /**
     * L'accroissement vertical lors du déplacement.
     * positif : déplacement vers le bas de l'écran
     * négatif : déplacement vers le haut de l'écran
     */
    private int dy;

    /**
     * Détermine si le rectangle mobile est déplaçable.
     */
    private boolean movable;

    private EventListenerList listenerList;
    private ChangeEvent changeEvent;

    // CONSTRUCTEURS

    public StdMobile(Rectangle sr, Rectangle mr) {
        Contract.checkCondition(mr != null && mr.width > 0 && mr.height > 0);
        Contract.checkCondition(sr != null);
        Contract.checkCondition(sr.contains(mr));

        statRect = sr;
        movRect = mr;
        dx = 0;
        dy = 0;
        movable = true;
        listenerList = new EventListenerList();
    }

    // REQUETES

    @Override
    public Point getCenter() {
        Point c = movRect.getLocation();
        c.x += movRect.width / 2;
        c.y += movRect.height / 2;
        return c;
    }

    @Override
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }

    @Override
    public int getHorizontalShift() {
        return dx;
    }

    @Override
    public Rectangle getMovingRect() {
        return new Rectangle(movRect);
    }

    @Override
    public Rectangle getStaticRect() {
        return new Rectangle(statRect);
    }

    @Override
    public int getVerticalShift() {
        return dy;
    }

    @Override
    public boolean isMovable() {
        return movable;
    }

    @Override
    public boolean isValidCenterPosition(Point p) {
        if (p == null) {
            return false;
        }
        return getStaticRect().contains(
                p.x - movRect.width / 2,
                p.y - movRect.height / 2,
                movRect.width,
                movRect.height);
    }

    // COMMANDES

    @Override
    public void addChangeListener(ChangeListener listener) {
        Contract.checkCondition(listener != null);

        listenerList.add(ChangeListener.class, listener);
    }

    @Override
    public void move() {
        Contract.checkCondition(movable);

        /*
         * Position du coin sup gauche du rectangle mobile avant déplacement.
         */
        Point oldTlc = movRect.getLocation();
        /*
         * Position du coin sup gauche du rectangle mobile après déplacement
         *  d'un pas (dx, dy).
         * Initialement, on imagine qu'il n'y a pas de choc avec la paroi.
         * Si c'est faux, on rectifiera avant de déplacer effectivement movRect.
         */
        Point newTlc = new Point(oldTlc.x + dx, oldTlc.y + dy);
        /*
         * Position du coin inférieur droit du rectangle mobile après
         *  déplacement, en imaginant qu'il n'y a pas de choc avec la paroi.
         */
        Point newBrc = new Point(
            newTlc.x + movRect.width,
            newTlc.y + movRect.height
        );
        if (newTlc.y <= statRect.y) {
            // movRect veut sortir de statRect par le bord haut
            newTlc.y = statRect.y;
            // on change de sens dans la direction verticale
            dy = -dy;
        } else if (newBrc.y >= statRect.y + statRect.height) {
            // movRect veut sortir de statRect par le bord bas
            newTlc.y = statRect.y + statRect.height - movRect.height;
            // on change de sens dans la direction verticale
            dy = -dy;
        }
        if (newTlc.x <= statRect.x) {
            // movRect veut sortir de statRect par le bord gauche
            newTlc.x = statRect.x;
            // on change de sens dans la direction horizontale
            dx = -dx;
        } else if (newBrc.x >= statRect.x + statRect.width) {
            // movRect veut sortir de statRect par le bord droit
            newTlc.x = statRect.x + statRect.width - movRect.width;
            // on change de sens dans la direction horizontale
            dx = -dx;
        }
        movRect.setLocation(newTlc);
        fireStateChanged();
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        Contract.checkCondition(listener != null);

        listenerList.remove(ChangeListener.class, listener);
    }

    @Override
    public void setCenter(Point c) {
        Contract.checkCondition(isValidCenterPosition(c));

        Point tlc = new Point(
                c.x - movRect.width / 2, c.y - movRect.height / 2);
        movRect.setLocation(tlc);
        fireStateChanged();
    }

    @Override
    public void setHorizontalShift(int hs) {
        dx = hs;
    }

    @Override
    public void setVerticalShift(int vs) {
        dy = vs;
    }

    @Override
    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    // OUTILS

    /**
     * Notifie de manière optimisée la totalité des écouteurs préalablement
     *  enregistrés sur le modèle.
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
