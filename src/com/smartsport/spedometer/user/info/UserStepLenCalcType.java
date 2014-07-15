/**
 * 
 */
package com.smartsport.spedometer.user.info;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserStepLenCalcType
 * @descriptor user step length calculate type
 * @author Ares
 * @version 1.0
 */
public enum UserStepLenCalcType {

	// auto and manual
	AUTO_CALC_SETPLEN(0), MANUAL_CALC_SETPLEN(1);

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserStepLenCalcType.class);

	// user step length calculate type value
	private int value;

	/**
	 * @title UserStepLenCalcType
	 * @descriptor user step length calculate type enumeration private
	 *             constructor
	 * @param value
	 *            : user step length calculate type value
	 * @author Ares
	 */
	private UserStepLenCalcType(int value) {
		// save value
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * @title getStepLenCalcType
	 * @descriptor get user step length calculate type from local storage value
	 * @param lsValue
	 *            : local storage saved user step length calculate type value
	 * @return user step length calculate type
	 * @author Ares
	 */
	public static UserStepLenCalcType getStepLenCalcType(Integer lsValue) {
		// define default user step length calculate type
		UserStepLenCalcType _stepLenCalcType = AUTO_CALC_SETPLEN;

		// check local storage saved value
		if (null != lsValue) {
			if (MANUAL_CALC_SETPLEN.value == lsValue.intValue()) {
				_stepLenCalcType = MANUAL_CALC_SETPLEN;
			} else if (AUTO_CALC_SETPLEN.value != lsValue.intValue()) {
				LOGGER.error("Get user step length calculate type from local storage value error, local storage saved value = "
						+ lsValue + " unrecognized");
			}
		} else {
			LOGGER.error("Get user step length calculate type from local storage value error, local storage saved value is null");
		}

		return _stepLenCalcType;
	}

}
