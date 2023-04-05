package motion.util;

import java.util.EventObject;

/**
 * Un TickEvent est utilisé lors de la notification par sa source des
 *  écouteurs préalablement enregistrés auprès d'elle.
 */
public class TickEvent extends EventObject {
    
	private static final long serialVersionUID = 1L;

	public TickEvent(Object source) {
        super(source);
    }
}
