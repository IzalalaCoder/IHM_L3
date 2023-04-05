package motion;

import javax.swing.SwingUtilities;

import motion.gui.MotionAppli;
import motion.model.Animator;
import motion.model.TimerAnimator;
import motion.view.Animable;
import motion.view.MobileComponent;
import motion.view.SimpleComponent;

/**
 * L'argument attendu dans main() en première position (args[0]) est un mot.
 * Dans ce mot, le caractère 'S' signifie que l'on souhaite exécuter
 *  l'application avec un SimpleComponent, et le caractère 'M' indique
 *  un MobileComponent.
 * Si un même caractère est rencontré plusieurs fois dans args[0], ou si ce
 *  dernier contient les deux caractères, c'est le premier trouvé en partant
 *  de la gauche qui est pris en compte.
 * Et si aucun des deux caractères n'est rencontré, l'absence est comblée
 *  par DEFAULT.
 * Quelques exemples :
 * - "s" -> "S"
 * - "M" -> "M"
 * - "smack" -> "S"
 * - "mars" -> "M"
 * - "euh..." ou "bonjour" ou "@#!!$?" -> DEFAULT
 */
public final class Main04 {
    
    @SuppressWarnings("unused")
	private static final String USAGE =
            "\n ==>\n"
            + " ==> MESSAGE IMPORTANT :\n"
            + " ==> Si vous avez fini de développer la classe %1$s,\n"
            + " ==> * cliquez sur le numéro de ligne indiqué au sommet de la"
            + " pile d'erreurs ci-dessous,\n"
            + " ==> * allez à la ligne au-dessus de celle où vous êtes"
            + " arrivé,\n"
            + " ==> * supprimez le premier caractère '/' (ce qui inverse les"
            + " commentaires),\n"
            + " ==> * importez le type %1$s,\n"
            + " ==> * relancez l'application.\n"
            + " ==>";

    private static final String DEFAULT = "M";
    private static final String ALTERNATIVE = "S";
    
    private Main04() {
        // rien
    }
    
    // POINT D'ENTREE

    public static void main(final String[] args) {
        String name = name(args);
        Creator c = Creator.valueOf(name);
        final Animable animable = c.createAnimable();
        final Animator animator = c.createAnimator();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MotionAppli(animable, animator).display();
            }
        });
    }
    
    private static String name(String[] args) {
        if (args.length == 0) {
            return DEFAULT;
        }
        
        StringBuilder name = new StringBuilder(DEFAULT);
        String arg = args[0].trim().toUpperCase() + DEFAULT;
        int iD0 = arg.indexOf(DEFAULT.charAt(0));
        int iA0 = arg.indexOf(ALTERNATIVE.charAt(0));
        if (iA0 >= 0 && iA0 < iD0) {
            name.setCharAt(0, ALTERNATIVE.charAt(0));
        }
        return name.toString();
    }
    
    /**
     * Permet la création de l'animable selon le choix défini à l'exécution
     *  (voir paramètre du constructeur, transmis par la méthode Main.main).
     */
    public enum Creator {
        S {
            @Override Animator createAnimator() {
                return ta(SIMPLE_RANGE);
            }
            @Override Animable createAnimable() {
                return sc();
            }
        },
        M {
            @Override Animator createAnimator() {
                return ta(MOBILE_RANGE);
            }
            @Override Animable createAnimable() {
                return mc();
            }
        };
        
        private static final int SIMPLE_RANGE = 10;
        
        private static final int MOBILE_RANGE = 100;
        private static final int WIDTH = 419;
        private static final int HEIGHT = 211;
        private static final int MOBILE_RAY = 10;
        
        abstract Animator createAnimator();
        abstract Animable createAnimable();
        
        static Animable sc() {
            return new SimpleComponent(WIDTH, HEIGHT);
        }
        static Animable mc() {
            /*
            throw new UnsupportedOperationException(
                    String.format(USAGE, "MobileComponent"));
            /*/
            return new MobileComponent(WIDTH, HEIGHT, MOBILE_RAY);
            //*/
        }
        static Animator ta(int range) {
            /*
            throw new UnsupportedOperationException(
                    String.format(USAGE, "TimerAnimator"));
            /*/
            return new TimerAnimator(range);
            //*/
        }
    }
}
