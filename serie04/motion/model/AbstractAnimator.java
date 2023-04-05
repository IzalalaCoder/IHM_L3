package motion.model;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import motion.util.TickListener;
import motion.util.TickEvent;

import util.Contract;

public abstract class AbstractAnimator implements Animator {
    
    // ATTRIBUTS STATIQUES
    
    private static final int MS_PER_SEC = 1000;

    // ATTRIBUTS

    private final int maxSpeed;
    
    private EventListenerList listenerList;
    private ChangeEvent changeEvent;
    private TickEvent tickEvent;

    // CONSTRUCTEURS

    /**
     * @pre <pre>
     *     max > 0 </pre>
     * @post <pre>
     *     getMaxSpeed() == max </pre>
     */
    public AbstractAnimator(int max) {
        Contract.checkCondition(max > 0);
        
        maxSpeed = max;
        listenerList = new EventListenerList();
    }

    // REQUETES

    @Override
    public int getMaxSpeed() {
        return maxSpeed;
    }

    // COMMANDES

    @Override
    public void addChangeListener(ChangeListener listener) {
        Contract.checkCondition(listener != null);

        listenerList.add(ChangeListener.class, listener);
    }

    @Override
    public void addTickListener(TickListener listener) {
        Contract.checkCondition(listener != null);
        
        listenerList.add(TickListener.class, listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        Contract.checkCondition(listener != null);
        
        listenerList.remove(ChangeListener.class, listener);
    }

    @Override
    public void removeTickListener(TickListener listener) {
        Contract.checkCondition(listener != null);
        
        listenerList.remove(TickListener.class, listener);
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
    
    /**
     * Notifie de manière optimisée la totalité des écouteurs préalablement
     *  enregistrés sur le modèle.
     */
    protected void fireTickOccured() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TickListener.class) {
                if (tickEvent == null) {
                    tickEvent = new TickEvent(this);
                }
                ((TickListener) listeners[i + 1]).tickOccurred(tickEvent);
            }
        }
    }
    
    /**
     * Calcule le temps de pause (en ms) avant l'affichage du prochain mouvement
     *  élémentaire.
     * @post <pre>
     *     0 <= result <= MS_PER_SEC
     *     Let s ::= getSpeed()
     *     s == 0 ==> result == MS_PER_SEC
     *     s != 0 ==> result == ceil((1.0 / s) * MS_PER_SEC) </pre>
     */
    protected int sleepDuration() {
        int d;
        int s = getSpeed();
        if (s > 0) {
            d = (int) Math.ceil((1.0 / s) * MS_PER_SEC);
        } else {
            d = MS_PER_SEC;
        }
        return d;
    }
}
