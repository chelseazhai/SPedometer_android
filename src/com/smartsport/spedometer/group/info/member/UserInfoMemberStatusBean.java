/**
 * 
 */
package com.smartsport.spedometer.group.info.member;

import org.json.JSONObject;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.JSONUtils;
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

	// member status in schedule group
	private MemberStatus memberSratus;

	public MemberStatus getMemberSratus() {
		return memberSratus;
	}

	public void setMemberSratus(MemberStatus memberSratus) {
		this.memberSratus = memberSratus;
	}

	@Override
	public String toString() {
		return "UserInfoMemberStatusBean [user info=" + super.toString()
				+ " and memberSratus=" + memberSratus + "]";
	}

	@Override
	public UserInfoMemberStatusBean parseUserInfo(JSONObject info) {
		// parse user info
		UserInfoMemberStatusBean _userInfo = (UserInfoMemberStatusBean) super
				.parseUserInfo(info);

		// parse user member status in schedule group info
		// check parsed user info member status extension json object
		if (null != info) {
			memberSratus = MemberStatus
					.getMemberStatus(JSONUtils.getStringFromJSONObject(
							info,
							context.getString(R.string.getScheduleGroupInfoReqResp_memStatus)));
		} else {
			LOGGER.error("Parse user info member status extension json object error, the info with member status is null");
		}

		return _userInfo;
	}

}
