/**
 * 
 */
package com.smartsport.spedometer.strangersocial;

import org.json.JSONObject;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserInfoLocationExtBean
 * @descriptor user info bean with location info extension
 * @author Ares
 * @version 1.0
 */
public class UserInfoLocationExtBean extends UserInfoBean {

	/**
	 * user info location extension bean serial version UID
	 */
	private static final long serialVersionUID = -7340786651774264539L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserInfoLocationExtBean.class);

	// user location longitude and latitude
	private LatLonPoint location;

	public LatLonPoint getLocation() {
		return location;
	}

	public void setLocation(LatLonPoint location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "UserInfoLocationExtBean [user info=" + super.toString()
				+ " and location=" + location + "]";
	}

	@Override
	public UserInfoLocationExtBean parseUserInfo(JSONObject info) {
		// parse user info
		UserInfoLocationExtBean _userInfo = (UserInfoLocationExtBean) super
				.parseUserInfo(info);

		// parse user location info
		// check parsed user info location extension json object
		if (null != info) {
			// initialize user location
			location = new LatLonPoint(0.0, 0.0);

			try {
				// longitude
				location.setLongitude(Double.parseDouble(JSONUtils.getStringFromJSONObject(
						info,
						context.getString(R.string.getNearbyUserReqResp_userLongitude))));
			} catch (NumberFormatException e) {
				LOGGER.error("Get user location longitude from json info object = "
						+ info
						+ " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}

			try {
				// latitude
				location.setLatitude(Double.parseDouble(JSONUtils.getStringFromJSONObject(
						info,
						context.getString(R.string.getNearbyUserReqResp_userLatitude))));
			} catch (NumberFormatException e) {
				LOGGER.error("Get user location latitude from json info object = "
						+ info
						+ " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Parse user info location extension json object error, the info with lolation is null");
		}

		return _userInfo;
	}

}
