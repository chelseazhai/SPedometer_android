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
	 * @descriptor constructor SSLog object with parameter
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

	/**
	 * @title getLogTag
	 * @descriptor get log tag
	 * @return log tag string
	 * @author Ares
	 */
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
	 * @param debugLog
	 *            : debug log
	 * @author Ares
	 */
	public void debug(String debugLog) {
		Log.d(getLogTag(), debugLog);
	}

	/**
	 * @title info
	 * @descriptor log info message
	 * @param infoLog
	 *            : info log
	 * @author Ares
	 */
	public void info(String infoLog) {
		Log.i(getLogTag(), infoLog);
	}

	/**
	 * @title warning
	 * @descriptor log warning message
	 * @param warningLog
	 *            : warning log
	 * @author Ares
	 */
	public void warning(String warningLog) {
		Log.w(getLogTag(), warningLog);
	}

	/**
	 * @title error
	 * @descriptor log error message
	 * @param errorLog
	 *            : error log
	 * @author Ares
	 */
	public void error(String errorLog) {
		Log.e(getLogTag(), errorLog);
	}

}
