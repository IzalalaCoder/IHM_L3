package motion.model;

import javax.swing.event.ChangeListener;

import motion.util.TickListener;

/**
 * @inv
 *     hasStopped() ==> hasStarted()
 *     isRunning() <==> hasStarted() && !hasStopped()
 *     isPaused() ==> isRunning()
 *     isResumed() ==> isRunning()
 *     isRunning() ==> (isPaused() <==> !isResumed())
 *     !isRunning() ==> (!isPaused() && !isResumed())
 *     
 * @cons <pre>
 *     $ARGS$ int max
 *     $PRE$
 *         max > 0
 *     $POST$
 *         getMaxSpeed() == max
 *         getSpeed() == max / 2
 *         !hasStarted() </pre>
 */
public interface Animator {

    // REQUETES
    
    /**
     * La vitesse maximale pour cette animateur.
     */
    int getMaxSpeed();

    /**
     * La vitesse actuelle de cet animateur.
     */
    int getSpeed();

    /**
     * Indique si l'animateur a été démarré.
     */
    boolean hasStarted();

    /**
     * Indique si l'animateur a été démarré, puis arrêté par la suite.
     */
    boolean hasStopped();

    /**
     * Indique si l'animateur a été démarré, pas encore arrêté, et désactivé.
     */
    boolean isPaused();

    /**
     * Indique si l'animateur a été démarré, pas encore arrêté, et activé.
     */
    boolean isResumed();

    /**
     * Indique si l'animateur a été démarré, mais pas encore arrêté.
     */
    boolean isRunning();

    // COMMANDES

    /**
     * Ajoute un ChangeListener.
     * @pre
     *     listener != null
     */
    void addChangeListener(ChangeListener listener);

    /**
     * Ajoute un TickListener.
     * @pre
     *     listener != null
     */
    void addTickListener(TickListener listener);

    /**
     * Place cet animateur en pause.
     * @pre
     *     isRunning()
     * @post
     *     isPaused()
     */
    void pause();

    /**
     * Retire un ChangeListener.
     * @pre
     *     listener != null
     */
    void removeChangeListener(ChangeListener listener);

    /**
     * Retire un TickListener.
     * @pre
     *     listener != null
     */
    void removeTickListener(TickListener listener);

    /**
     * Redémarre cet animateur.
     * @pre
     *     isRunning()
     * @post
     *     isResumed()
     */
    void resume();

    /**
     * Fixe la vitesse de cet animateur.
     * @pre
     *     0 <= d <= getMaxSpeed()
     * @post
     *     getSpeed() == d
     */
    void setSpeed(int d);

    /**
     * Démarre cet animateur.
     * @post
     *     isRunning() && !isPaused()
     */
    void start();

    /**
     * Stoppe définitivement l'activité de cet animateur.
     * @pre
     *     isRunning()
     * @post
     *     hasStopped()
     */
    void stop();
}
