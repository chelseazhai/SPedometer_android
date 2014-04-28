/**
 * 
 */
package com.smartsport.spedometer.user;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserInfoBean
 * @descriptor user info bean
 * @author Ares
 * @version 1.0
 */
public class UserInfoBean implements Serializable {

	/**
	 * user info bean serial version UID
	 */
	private static final long serialVersionUID = 667857253264558837L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(UserInfoBean.class);

	// context
	protected transient Context context;

	// user id, nickname, avatar url, age, gender, height and weight
	private int userId;
	private String nickname;
	private String avatarUrl;
	private int age;
	private UserGender gender;
	private float height;
	private float weight;

	/**
	 * @title UserInfoBean
	 * @descriptor user info bean constructor
	 * @author Ares
	 */
	public UserInfoBean() {
		super();

		// initialize context
		context = SSApplication.getContext();
	}

	/**
	 * @title UserInfoBean
	 * @descriptor user info bean constructor with json object
	 * @param info
	 *            : user info json object
	 * @author Ares
	 */
	public UserInfoBean(JSONObject info) {
		this();

		// parse user info json object
		parseUserInfo(info);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public UserGender getGender() {
		return gender;
	}

	public void setGender(UserGender gender) {
		this.gender = gender;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "UserInfoBean [userId=" + userId + ", nickname=" + nickname
				+ ", avatarUrl=" + avatarUrl + ", age=" + age + ", gender="
				+ gender + ", height=" + height + " and weight=" + weight + "]";
	}

	/**
	 * @title parseUserInfo
	 * @descriptor parse user info json object to user info object
	 * @param info
	 *            : user info json object
	 * @return user info object
	 * @author Ares
	 */
	public UserInfoBean parseUserInfo(JSONObject info) {
		// check parsed user info json object
		if (null != info) {
			// set user info attributes
			// user id
			// check is or not has user id
			if (JSONUtils.jsonObjectKeys(info).contains(
					context.getString(R.string.userInfoReqResp_userId))) {
				try {
					userId = Integer
							.parseInt(JSONUtils.getStringFromJSONObject(
									info,
									context.getString(R.string.userInfoReqResp_userId)));
				} catch (NumberFormatException e) {
					LOGGER.error("Get user id from json info object = " + info
							+ " error, exception message = " + e.getMessage());

					e.printStackTrace();
				}
			}

			// nickname
			nickname = JSONUtils.getStringFromJSONObject(info,
					context.getString(R.string.userInfoReqResp_userNickname));

			// avater url
			avatarUrl = JSONUtils.getStringFromJSONObject(info,
					context.getString(R.string.userInfoReqResp_userAvatarUrl));

			try {
				// age
				age = Integer.parseInt(JSONUtils.getStringFromJSONObject(info,
						context.getString(R.string.userInfoReqResp_userAge)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get user age from json info object = " + info
						+ " error, exception message = " + e.getMessage());

				e.printStackTrace();
			}

			// gender
			gender = UserGender.getGender(JSONUtils.getStringFromJSONObject(
					info,
					context.getString(R.string.userInfoReqResp_userGender)));

			try {
				// height
				height = Float
						.parseFloat(JSONUtils.getStringFromJSONObject(
								info,
								context.getString(R.string.userInfoReqResp_userHeight)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get user height from json info object = " + info
						+ " error, exception message = " + e.getMessage());

				e.printStackTrace();
			}

			try {
				// weight
				weight = Float
						.parseFloat(JSONUtils.getStringFromJSONObject(
								info,
								context.getString(R.string.userInfoReqResp_userWeight)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get user weight from json info object = " + info
						+ " error, exception message = " + e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Parse user info json object error, the info is null");
		}

		return this;
	}

}
