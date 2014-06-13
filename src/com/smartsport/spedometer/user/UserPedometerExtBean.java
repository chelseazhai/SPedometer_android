/**
 * 
 */
package com.smartsport.spedometer.user;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserPedometerExtBean
 * @descriptor user pedometer extension bean
 * @author Ares
 * @version 1.0
 */
public class UserPedometerExtBean extends UserBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2415924399314310680L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserPedometerExtBean.class);

	/**
	 * @title getUserId
	 * @descriptor get user id
	 * @return user id
	 * @author Ares
	 */
	public Long getUserId() {
		// define user id
		Long _userId = 0L;

		// get and check extension user id
		Object _extUserId = getExtValue(UserPedometerExtensionKeys.PEDOMETER_USER_ID
				.name());
		if (null != _extUserId && _extUserId instanceof Long) {
			_userId = (Long) _extUserId;
		} else {
			LOGGER.error("Get user extension user id error, extension user id = "
					+ _extUserId);
		}

		return _userId;
	}

	/**
	 * @title setUserId
	 * @descriptor set user id
	 * @param userId
	 *            : user id
	 * @author Ares
	 */
	public void setUserId(Long userId) {
		setExtValue(UserPedometerExtensionKeys.PEDOMETER_USER_ID.name(), userId);
	}

	/**
	 * @title getAvatarUrl
	 * @descriptor get user avatar url
	 * @return user avatar url
	 * @author Ares
	 */
	public String getAvatarUrl() {
		// define user avatar url
		String _userAvatarUrl = null;

		// get and check extension user avatar url
		Object _extUserAvatarUrl = getExtValue(UserPedometerExtensionKeys.PEDOMETER_USER_AVATARURL
				.name());
		if (null != _extUserAvatarUrl && _extUserAvatarUrl instanceof String) {
			_userAvatarUrl = (String) _extUserAvatarUrl;
		} else {
			LOGGER.error("Get user extension user avatar url error, extension user avatar url = "
					+ _extUserAvatarUrl);
		}

		return _userAvatarUrl;
	}

	/**
	 * @title setAvatarUrl
	 * @descriptor set user avatar url
	 * @param avatarUrl
	 *            : user avatar url
	 * @author Ares
	 */
	public void setAvatarUrl(String avatarUrl) {
		setExtValue(UserPedometerExtensionKeys.PEDOMETER_USER_AVATARURL.name(),
				avatarUrl);
	}

	// inner class
	/**
	 * @name UserPedometerExtensionKeys
	 * @descriptor user pedometer extension keys
	 * @author Ares
	 * @version 1.0
	 */
	enum UserPedometerExtensionKeys {

		// pedometer user id and avatar url
		PEDOMETER_USER_ID, PEDOMETER_USER_AVATARURL;

	}

}
