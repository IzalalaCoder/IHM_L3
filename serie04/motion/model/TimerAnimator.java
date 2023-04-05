package motion.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import util.Contract;

public class TimerAnimator extends AbstractAnimator {
	
	// CONSTANTES
	
	private final int DELAY_INITIAL = 0;
	
	// ATTRIBUTS
	
	private Timer time;
	private int speed;
	private boolean hasStarted;
	private boolean hasStopped;
	
	// CONSTRUCTEURS
	
	public TimerAnimator(int max) {
		super(max);
		this.time = new Timer(sleepDuration(), new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fireTickOccured();
			}
			
		});
		this.time.setInitialDelay(DELAY_INITIAL);
		this.speed = max / 2;
		this.hasStarted = false;
		this.hasStarted = false;
	}

	// REQUETES
	
	@Override
	public int getSpeed() {
		return this.speed;
	}

	@Override
	public boolean hasStarted() {
		return this.hasStarted;
	}

	@Override
	public boolean hasStopped() {
		return  this.hasStarted && this.hasStopped;
	}

	@Override
	public boolean isPaused() {
		return this.isRunning() && !time.isRunning();
	}

	@Override
	public boolean isResumed() {
		return this.isRunning() && time.isRunning();
	}

	@Override
	public boolean isRunning() {
		return hasStarted && !hasStopped;
	}
	
	// COMMANDES

	@Override
	public void pause() {
		Contract.checkCondition(this.isRunning());
		this.time.stop();
		fireStateChanged();
	}

	@Override
	public void resume() {
		Contract.checkCondition(this.isRunning());
		this.time.restart();
		fireStateChanged();
	}

	@Override
	public void setSpeed(int d) {
		Contract.checkCondition(d >= 0 && d <= getMaxSpeed());
		this.speed = d;
		this.time.setDelay(sleepDuration());
		fireStateChanged();
	}

	@Override
	public void start() {
		this.time.start();
		this.hasStarted = true;
		fireStateChanged();
	}

	@Override
	public void stop() {
		Contract.checkCondition(this.isRunning());
		this.time.stop();
		this.hasStopped = true;
		fireStateChanged();
	}

}
