package crazy.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import util.Contract;

/**
 * Implémentation standard de PodiumModel.
 */
public class StdPodiumModel<E> implements PodiumModel<E> {

    // ATTRIBUTS

    private List<E> data;
    private int capacity;
    private EventListenerList lst;
    private ChangeEvent event;

    // CONSTRUCTEURS

    public StdPodiumModel(List<E> init, int capacity) {
        Contract.checkCondition(init != null);
        Contract.checkCondition(!init.contains(null));

        this.capacity = capacity;
        this.lst = new EventListenerList();
        data = new ArrayList<E>(init);
    }

    public StdPodiumModel() {
        this(new ArrayList<E>(), 0);
    }

    // REQUETES

    @Override
    public E bottom() {
        Contract.checkCondition(size() > 0);

        return data.get(0);
    }

    @Override
    public E elementAt(int i) {
        Contract.checkCondition(0 <= i && i < capacity());

        return i < size() ? data.get(i) : null;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public boolean similar(PodiumModel<E> that) {
        Contract.checkCondition(that != null);

        int dataSize = data.size();
        boolean result = (that.capacity() == capacity)
                && (that.size() == dataSize);
        for (int i = 0; result && (i < dataSize); i++) {
            result = that.elementAt(i).equals(data.get(i));
        }
        return result;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public E top() {
        Contract.checkCondition(size() > 0);

        return data.get(data.size() - 1);
    }

    @Override
    public String toString() {
        String res = "[";
        int dataSize = data.size();
        for (int i = 0; i < dataSize; i++) {
            res += data.get(i) + "|";
        }
        return capacity + "/" + res + "]";
    }

	@Override
	public ChangeListener[] getChangeListeners() {
		return lst.getListeners(ChangeListener.class);
	}

    // COMMANDES

    @Override
    public void addTop(E elem) {
        Contract.checkCondition(elem != null);
        Contract.checkCondition(size() < capacity());

        data.add(elem);
        this.fireStateChanged();
    }

    @Override
    public void removeBottom() {
        Contract.checkCondition(size() > 0);

        data.remove(0);
        this.fireStateChanged();
    }

    @Override
    public void removeTop() {
        Contract.checkCondition(size() > 0);
        
        data.remove(size() - 1);
        this.fireStateChanged();
    }

	@Override
	public void addChangeListener(ChangeListener cl) {
		Contract.checkCondition(cl != null);
		
		this.lst.add(ChangeListener.class, cl);
	}

	@Override
	public void removeChangeListener(ChangeListener cl) {
		Contract.checkCondition(cl != null);
		
		this.lst.remove(ChangeListener.class, cl);
	}
	
	// OUTILS
	
	private void fireStateChanged() {
		Object[] list = lst.getListenerList();
		for (int i = list.length - 2; i >= 0; i -= 2) {
			if (list[i] == ChangeListener.class) {
				if (event == null) {
					event = new ChangeEvent(this);
				}
				((ChangeListener) list[i + 1]).stateChanged(event);
			}
		}
	}
}
