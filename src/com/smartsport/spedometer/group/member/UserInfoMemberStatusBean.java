/**
 * 
 */
package com.smartsport.spedometer.group.member;

import org.json.JSONObject;

import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserInfoMemberStatusBean
 * @descriptor user info bean with member status in schedule group extension
 * @author Ares
 * @version 1.0
 */
public class UserInfoMemberStatusBean extends UserInfoBean {

	/**
	 * user info member status extension bean serial version UID
	 */
	private static final long serialVersionUID = 6283069591590625076L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserInfoMemberStatusBean.class);

	/**
	 * @title UserInfoMemberStatusBean
	 * @descriptor user info member status extension bean constructor
	 * @author Ares
	 */
	public UserInfoMemberStatusBean() {
		super();
	}

	/**
	 * @title UserInfoMemberStatusBean
	 * @descriptor user info member status extension bean constructor with json
	 *             object
	 * @param info
	 *            : user info with member status extension json object
	 * @author Ares
	 */
	public UserInfoMemberStatusBean(JSONObject info) {
		super(info);
	}

	// member status
	//

}
