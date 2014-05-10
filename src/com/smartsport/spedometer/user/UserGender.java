/**
 * 
 */
package com.smartsport.spedometer.user;

import java.util.ArrayList;
import java.util.List;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserGender
 * @descriptor user gender enumeration
 * @author Ares
 * @version 1.0
 */
public enum UserGender {

	// male, female and unknown
	MALE(SSApplication.getContext().getString(R.string.userGender_male)
			.charAt(0), SSApplication.getContext().getString(
			R.string.userGender_male_label)), FEMALE(SSApplication.getContext()
			.getString(R.string.userGender_female).charAt(0), SSApplication
			.getContext().getString(R.string.userGender_female_label)), GENDER_UNKNOWN(
			SSApplication.getContext().getString(R.string.userGender_unknown)
					.charAt(0), SSApplication.getContext().getString(
					R.string.userGender_unknown_label));

	// logger
	private static final SSLogger LOGGER = new SSLogger(UserGender.class);

	// user gender value and label
	private char value;
	private String label;

	/**
	 * @title UserGender
	 * @descriptor user gender enumeration private constructor
	 * @param value
	 *            : user gender value
	 * @param label
	 *            : user gender label
	 * @author Ares
	 */
	private UserGender(char value, String label) {
		// save value and label
		this.value = value;
		this.label = label;
	}

	public char getValue() {
		return value;
	}

	public String getLabel() {
		return label;
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

	/**
	 * @title getGenders
	 * @descriptor get user genders
	 * @return user gender list
	 * @author Ares
	 */
	public static List<String> getGenders() {
		List<String> _genders = new ArrayList<String>();

		// add male, female and unknown to list
		_genders.add(MALE.getLabel());
		_genders.add(FEMALE.getLabel());
		_genders.add(GENDER_UNKNOWN.getLabel());

		return _genders;
	}

}
