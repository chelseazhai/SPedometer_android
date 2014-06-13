/**
 * 
 */
package com.smartsport.spedometer.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @name UserBean
 * @descriptor user bean
 * @author Ares
 * @version 1.0
 */
public class UserBean implements Serializable {

	/**
	 * user bean serial version UID
	 */
	private static final long serialVersionUID = -1026786675673514058L;

	// user login name, password and user key
	private String loginName;
	private String loginPwd;
	private String userKey;

	// extension map
	private final Map<String, Object> EXT = new HashMap<String, Object>();

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	/**
	 * @title getExtValue
	 * @descriptor get user extension value object with key
	 * @param extKey
	 *            : user extension key
	 * @return user extension value
	 * @author Ares
	 */
	protected Object getExtValue(String extKey) {
		return EXT.get(extKey);
	}

	/**
	 * @title setExtValue
	 * @descriptor set user extension value object with key
	 * @param extKey
	 *            : user extension key
	 * @param extValue
	 *            : user extension value
	 * @author Ares
	 */
	protected void setExtValue(String extKey, Object extValue) {
		EXT.put(extKey, extValue);
	}

	@Override
	public String toString() {
		return "UserBean [loginName=" + loginName + ", loginPwd=" + loginPwd
				+ ", userKey=" + userKey + " and extension map=" + EXT + "]";
	}

}
