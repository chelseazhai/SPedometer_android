/**
 * 
 */
package com.smartsport.spedometer.user;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserGender
 * @descriptor user gender enumeration
 * @author Ares
 * @version 1.0
 */
public enum UserGender {

	// male, female and unknown
	MALE('m'), FEMALE('f'), GENDER_UNKNOWN('n');

	// logger
	private static final SSLogger LOGGER = new SSLogger(UserGender.class);

	// user gender value
	private char value;

	/**
	 * @title UserGender
	 * @descriptor user gender enumeration private constructor
	 * @param value
	 *            : user gender value
	 * @author Ares
	 */
	private UserGender(char value) {
		// save value
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	/**
	 * @title getGender
	 * @descriptor get user gender from remote server return value
	 * @param rsRetValue
	 *            : remote server return user gender value
	 * @return user gender
	 * @author Ares
	 */
	public static UserGender getGender(String rsRetValue) {
		// define default user gender
		UserGender _gender = GENDER_UNKNOWN;

		// check remote server return value
		if (null != rsRetValue) {
			if (String.valueOf(MALE.value).equalsIgnoreCase(rsRetValue)) {
				_gender = MALE;
			} else if (String.valueOf(FEMALE.value)
					.equalsIgnoreCase(rsRetValue)) {
				_gender = FEMALE;
			} else if (!String.valueOf(GENDER_UNKNOWN.value).equalsIgnoreCase(
					rsRetValue)) {
				LOGGER.error("Get user gender from remote server return value error, remote server return value = "
						+ rsRetValue + " unrecognized");
			}
		} else {
			LOGGER.error("Get user gender from remote server return value error, remote server return value is null");
		}

		return _gender;
	}

}
