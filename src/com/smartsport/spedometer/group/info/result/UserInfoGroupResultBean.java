/**
 * 
 */
package com.smartsport.spedometer.group.info.result;

import org.json.JSONObject;

import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserInfoGroupResultBean
 * @descriptor user info bean with group result of walk or compete extension
 * @author Ares
 * @version 1.0
 */
public class UserInfoGroupResultBean extends UserInfoBean {

	/**
	 * user info group result extension bean serial version UID
	 */
	private static final long serialVersionUID = -2219795481050852145L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserInfoGroupResultBean.class);

	// walk or compete group result
	private GroupResultInfoBean result;

	/**
	 * @title UserInfoGroupResultBean
	 * @descriptor user info group result extension bean constructor
	 * @author Ares
	 */
	public UserInfoGroupResultBean() {
		super();
	}

	/**
	 * @title UserInfoGroupResultBean
	 * @descriptor user info group result extension bean constructor with json
	 *             object
	 * @param info
	 *            : user info with group result extension json object
	 * @author Ares
	 */
	public UserInfoGroupResultBean(JSONObject info) {
		super(info);
	}

	public GroupResultInfoBean getResult() {
		return result;
	}

	public void setResult(GroupResultInfoBean result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "UserInfoGroupResultBean [user info=" + super.toString()
				+ " and result=" + result + "]";
	}

	@Override
	public UserInfoGroupResultBean parseUserInfo(JSONObject info) {
		// parse user info
		UserInfoGroupResultBean _userInfo = (UserInfoGroupResultBean) super
				.parseUserInfo(info);

		// parse user group status of walk or compete group info
		// check parsed user info group status extension json object
		if (null != info) {
			result = new GroupResultInfoBean(info);
		} else {
			LOGGER.error("Parse user info group status extension json object error, the info with group status is null");
		}

		return _userInfo;
	}

}
