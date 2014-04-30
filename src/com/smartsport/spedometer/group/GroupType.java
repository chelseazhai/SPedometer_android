/**
 * 
 */
package com.smartsport.spedometer.group;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupType
 * @descriptor walk or compete group type enumeration
 * @author Ares
 * @version 1.0
 */
public enum GroupType {

	// walk and compete
	WALK_GROUP(Integer.parseInt(SSApplication.getContext().getString(
			R.string.groupType_walk))), COMPETE_GROUP(Integer
			.parseInt(SSApplication.getContext().getString(
					R.string.groupType_compete)));

	// logger
	private static final SSLogger LOGGER = new SSLogger(GroupType.class);

	// walk or compete group value
	private int value;

	/**
	 * @title GroupType
	 * @descriptor walk or compete group type enumeration private constructor
	 * @param value
	 *            : walk or compete group value
	 * @author Ares
	 */
	private GroupType(int value) {
		// save value
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * @title getGroupType
	 * @descriptor get walk or compete group type from remote server return
	 *             value
	 * @param rsRetValue
	 *            : remote server return group type value
	 * @return walk or compete group type
	 * @author Ares
	 */
	public static GroupType getGroupType(String rsRetValue) {
		// define default walk or compete group type
		GroupType _groupType = WALK_GROUP;

		// check remote server return value
		if (null != rsRetValue) {
			if (String.valueOf(COMPETE_GROUP.value)
					.equalsIgnoreCase(rsRetValue)) {
				_groupType = COMPETE_GROUP;
			} else if (!String.valueOf(WALK_GROUP.value).equalsIgnoreCase(
					rsRetValue)) {
				LOGGER.error("Get walk or compete group type from remote server return value error, remote server return value = "
						+ rsRetValue + " unrecognized");
			}
		} else {
			LOGGER.error("Get walk or compete group type from remote server return value error, remote server return value is null");
		}

		return _groupType;
	}

}
