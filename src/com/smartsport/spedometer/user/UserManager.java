/**
 * 
 */
package com.smartsport.spedometer.user;

/**
 * @name UserManager
 * @descriptor user manager
 * @author Ares
 * @version 1.0
 */
public class UserManager {

	// singleton instance
	private static volatile UserManager _singletonInstance;

	// login user
	private UserBean loginUser;

	/**
	 * @title UserManager
	 * @descriptor user manager private constructor
	 * @author Ares
	 */
	private UserManager() {
		super();

		// initialize login user
		loginUser = new UserBean();
	}

	// get user manager singleton instance
	public static UserManager getInstance() {
		if (null == _singletonInstance) {
			synchronized (UserManager.class) {
				if (null == _singletonInstance) {
					_singletonInstance = new UserManager();
				}
			}
		}

		return _singletonInstance;
	}

	public UserBean getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(UserBean loginUser) {
		this.loginUser = loginUser;
	}

}
