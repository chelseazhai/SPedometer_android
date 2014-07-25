/**
 * 
 */
package com.smartsport.spedometer.strangersocial.pat;

import org.json.JSONObject;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.strangersocial.UserInfoLocationExtBean;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserInfoPatLocationExtBean
 * @descriptor user info bean with pat location info extension
 * @author Ares
 * @version 1.0
 */
public class UserInfoPatLocationExtBean extends UserInfoLocationExtBean {

	/**
	 * user info pat location extension bean serial version UID
	 */
	private static final long serialVersionUID = 9035806541167204818L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserInfoPatLocationExtBean.class);

	// pat count
	private int patCount;

	/**
	 * @title UserInfoPatLocationExtBean
	 * @descriptor user info pat location extension bean constructor
	 * @author Ares
	 */
	public UserInfoPatLocationExtBean() {
		super();
	}

	/**
	 * @title UserInfoPatLocationExtBean
	 * @descriptor user info pat location extension bean constructor with pat
	 *             info json object
	 * @param info
	 *            : user info with pat location extension json object
	 * @author Ares
	 */
	public UserInfoPatLocationExtBean(JSONObject info) {
		super(info);
	}

	public int getPatCount() {
		return patCount;
	}

	public void setPatCount(int patCount) {
		this.patCount = patCount;
	}

	@Override
	public String toString() {
		return "UserInfoPatLocationExtBean [user info with location="
				+ super.toString() + " and patCount=" + patCount + "]";
	}

	@Override
	public UserInfoPatLocationExtBean parseUserInfo(JSONObject info) {
		// parse user info
		UserInfoPatLocationExtBean _userInfoWithLocation = (UserInfoPatLocationExtBean) super
				.parseUserInfo(info);

		// parse user pat location info
		// check parsed user info pat location extension json object
		if (null != info) {
			try {
				// pat count
				patCount = Integer
						.parseInt(JSONUtils.getStringFromJSONObject(
								info,
								context.getString(R.string.getNearbyStrangerReqResp_patedCount)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get user pat count from json info object = "
						+ info + " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Parse user info pat location extension json object error, the info with pat location and count is null");
		}

		return _userInfoWithLocation;
	}

}
