/**
 * 
 */
package com.smartsport.spedometer.group.info.member;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name MemberStatus
 * @descriptor member status in schedule group enumeration
 * @author Ares
 * @version 1.0
 */
public enum MemberStatus {

	// online and offline
	MEM_ONLINE(Integer.parseInt(SSApplication.getContext().getString(
			R.string.memberStatus_inScheduleGroup_online))), MEM_OFFLINE(
			Integer.parseInt(SSApplication.getContext().getString(
					R.string.memberStatus_inScheduleGroup_offline)));

	// logger
	private static final SSLogger LOGGER = new SSLogger(MemberStatus.class);

	// member online and offline in schedule group value
	private int value;

	/**
	 * @title MemberStatus
	 * @descriptor member status in schedule group enumeration private
	 *             constructor
	 * @param value
	 *            : member online and offline in schedule group value
	 * @author Ares
	 */
	private MemberStatus(int value) {
		// save value
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * @title getMemberStatus
	 * @descriptor get member status in schedule group from remote server return
	 *             value
	 * @param rsRetValue
	 *            : remote server return member status in schedule group value
	 * @return member status in schedule group
	 * @author Ares
	 */
	public static MemberStatus getMemberStatus(String rsRetValue) {
		// define default member status in schedule group
		MemberStatus _memberStatus = MEM_OFFLINE;

		// check remote server return value
		if (null != rsRetValue) {
			if (String.valueOf(MEM_ONLINE.value).equalsIgnoreCase(rsRetValue)) {
				_memberStatus = MEM_ONLINE;
			} else if (!String.valueOf(MEM_OFFLINE.value).equalsIgnoreCase(
					rsRetValue)) {
				LOGGER.error("Get member status in schedule group from remote server return value error, remote server return value = "
						+ rsRetValue + " unrecognized");
			}
		} else {
			LOGGER.error("Get member status in schedule group from remote server return value error, remote server return value is null");
		}

		return _memberStatus;
	}

}
