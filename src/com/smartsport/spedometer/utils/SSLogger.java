package com.smartsport.spedometer.utils;

import android.util.Log;

import com.smartsport.spedometer.SSApplication;

/**
 * @name SSLogger
 * @descriptor smartsport logger
 * @author Ares
 * @version 1.0
 */
public class SSLogger {

	// log tag object class and log tag
	private Class<?> clsLogTag;
	private String logTag;

	/**
	 * @title SSLog
	 * @descriptor constructor SSLog object with parameter log tag object class
	 * @param logTagCls
	 *            : log tag object class
	 * @return SSLog object
	 * @author Ares
	 */
	public SSLogger(Class<?> logTagCls) {
		super();

		// save log tag object class
		clsLogTag = logTagCls;
	}

	private String getLogTag() {
		// check log tag
		if (null == logTag) {
			// get android package name
			String _androidPackageName = SSApplication.getContext()
					.getPackageName();

			// check log tag object class
			if (null == clsLogTag) {
				clsLogTag = SSLogger.class;
			}

			// get log tag object canonical name
			String _logTagObjectCanonicalName = clsLogTag.getCanonicalName();

			// initialize log tag
			logTag = _logTagObjectCanonicalName
					.substring(_logTagObjectCanonicalName
							.lastIndexOf(_androidPackageName)
							+ _androidPackageName.length());
		}

		return logTag;
	}

	/**
	 * @title debug
	 * @descriptor log debug message
	 * @param debugMsg
	 *            : debug log message
	 * @author Ares
	 */
	public void debug(String debugMsg) {
		Log.d(getLogTag(), debugMsg);
	}

	/**
	 * @title info
	 * @descriptor log info message
	 * @param infoMsg
	 *            : info log message
	 * @author Ares
	 */
	public void info(String infoMsg) {
		Log.i(getLogTag(), infoMsg);
	}

	/**
	 * @title warning
	 * @descriptor log warning message
	 * @param warningMsg
	 *            : warning log message
	 * @author Ares
	 */
	public void warning(String warningMsg) {
		Log.w(getLogTag(), warningMsg);
	}

	/**
	 * @title error
	 * @descriptor log error message
	 * @param errorMsg
	 *            : error log message
	 * @author Ares
	 */
	public void error(String errorMsg) {
		Log.e(getLogTag(), errorMsg);
	}

}
