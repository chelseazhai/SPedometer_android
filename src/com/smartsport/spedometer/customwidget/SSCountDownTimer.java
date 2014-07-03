/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.os.CountDownTimer;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name CountDownTimer
 * @descriptor smartsport count down timer
 * @author Ares
 * @version 1.0
 */
public class SSCountDownTimer extends CountDownTimer {

	// logger
	private static final SSLogger LOGGER = new SSLogger(SSCountDownTimer.class);

	// milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;

	// remain time
	private long remainTime;

	// on tick listener
	private OnTickListener onTickListener;

	// on finish listener
	private OnFinishListener onFinishListener;

	/**
	 * @title SSCountDownTimer
	 * @descriptor smartsport count down timer constructor with milliseconds in
	 *             future and count down interval
	 * @param millisInFuture
	 *            : milliseconds in future
	 * @param countDownInterval
	 *            : count down interval
	 * @author Ares
	 */
	public SSCountDownTimer(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);

		// save remain time
		remainTime = millisInFuture;
	}

	/**
	 * @title SSCountDownTimer
	 * @descriptor smartsport count down timer constructor with milliseconds in
	 *             future
	 * @param millisInFuture
	 *            : milliseconds in future
	 * @author Ares
	 */
	public SSCountDownTimer(long millisInFuture) {
		this(millisInFuture, MILLISECONDS_PER_SECOND);
	}

	public long getRemainTime() {
		return remainTime;
	}

	public void setOnTickListener(OnTickListener onTickListener) {
		this.onTickListener = onTickListener;
	}

	public void setOnFinishListener(OnFinishListener onFinishListener) {
		this.onFinishListener = onFinishListener;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// update remain time
		remainTime = millisUntilFinished;

		// check smartsport count down timer on tick listener and tick it
		if (null != onTickListener) {
			onTickListener.onTick(remainTime);
		} else {
			LOGGER.warning("Not need to notify smartsport count down timer on tick listener interval occurred");
		}
	}

	@Override
	public void onFinish() {
		// clear remain time
		remainTime = 0L;

		// check smartsport count down timer on finish listener and finish it
		if (null != onFinishListener) {
			onFinishListener.onFinish();
		} else {
			LOGGER.warning("Not need to notify smartsport count down timer on finish listener time is over");
		}
	}

	// inner class
	/**
	 * @name OnTickListener
	 * @descriptor smartsport count down timer on tick listener
	 * @author Ares
	 * @version 1.0
	 */
	public static interface OnTickListener {

		/**
		 * @title onTick
		 * @descriptor smartsport count down timer on tick listener on tick
		 * @param remainMillis
		 *            : remain milliseconds
		 * @author Ares
		 */
		public void onTick(long remainMillis);

	}

	/**
	 * @name OnFinishListener
	 * @descriptor smartsport count down timer on finish listener
	 * @author Ares
	 * @version 1.0
	 */
	public static interface OnFinishListener {

		/**
		 * @title onFinish
		 * @descriptor smartsport count down timer on finish listener on finish
		 * @author Ares
		 */
		public void onFinish();

	}

}
