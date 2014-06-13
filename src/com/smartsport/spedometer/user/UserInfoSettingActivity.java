/**
 * 
 */
package com.smartsport.spedometer.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.ISSBaseActivityResult;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserInfoItemEditorActivity.UserInfoItemEditorExtraData;
import com.smartsport.spedometer.user.UserInfoSettingActivity.UserInfo4SettingListViewAdapter.UserInfo4SettingListViewAdapterKey;
import com.smartsport.spedometer.utils.SSLogger;
import com.smartsport.spedometer.utils.StringUtils;

/**
 * @name UserInfoSettingActivity
 * @descriptor smartsport user info setting activity
 * @author Ares
 * @version 1.0
 */
public class UserInfoSettingActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserInfoSettingActivity.class);

	// pedometer login user
	private UserPedometerExtBean loginUser = (UserPedometerExtBean) UserManager
			.getInstance().getLoginUser();

	// user info model
	private UserInfoModel userInfoModel = UserInfoModel.getInstance();

	// user info for setting listView adapter
	private UserInfo4SettingListViewAdapter userInfo4SettingListViewAdapter;

	// user step length and its calculate type
	private Float userStepLength;
	private UserStepLenCalcType userStepLenCalcType;

	// user step length relativeLayout and its indicator imageView
	private RelativeLayout userStepLengthRelativeLayout;
	private ImageView userStepLengthIndicatorImgView;

	// user step length textView
	private TextView userStepLengthTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// load user info with step length and its calculate type from local
		// storage
		// test by ares
		userStepLenCalcType = UserStepLenCalcType.MANUAL_CALC_SETPLEN;
		//

		// set content view
		setContentView(R.layout.activity_userinfo_setting);

		// get user info from remote server
		userInfoModel.getUserInfo(loginUser.getUserId(),
				loginUser.getUserKey(), new ICMConnector() {

					@Override
					public void onSuccess(Object... retValue) {
						// check return values
						if (null != retValue
								&& 0 < retValue.length
								&& retValue[retValue.length - 1] instanceof UserInfoBean) {
							// get the user info object and update its info for
							// setting
							updateUserInfo((UserInfoBean) retValue[retValue.length - 1]);
						} else {
							LOGGER.error("Update user info for setting UI error");
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMsg) {
						LOGGER.error("Get user info from remote server error, error code = "
								+ errorCode + " and message = " + errorMsg);

						//
					}

				});
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(R.string.userInfo_setting_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get user info for setting listView
		ListView _userInfo4SettingListView = (ListView) findViewById(R.id.uis_userInfo_listView);

		// set its adapter
		_userInfo4SettingListView
				.setAdapter(userInfo4SettingListViewAdapter = new UserInfo4SettingListViewAdapter(
						this,
						userInfoModel.getUserInfo(),
						R.layout.userinfo_setting_item_layout,
						new String[] {
								UserInfo4SettingListViewAdapterKey.USERINFO_LABEL_KEY
										.name(),
								UserInfo4SettingListViewAdapterKey.USERINFO_VALUE_KEY
										.name() }, new int[] {
								R.id.uisi_label_textView,
								R.id.uisi_info_textView }));

		// set its on item click listener
		_userInfo4SettingListView
				.setOnItemClickListener(new UserInfo4SettingItemOnClickListener());

		// get user step length relativeLayout
		userStepLengthRelativeLayout = (RelativeLayout) findViewById(R.id.uis_userStepLength_relativeLayout);

		// set its on click listener
		userStepLengthRelativeLayout
				.setOnClickListener(new StepLenRelativeLayoutOnClickListener());

		// get user step length indicator imageView
		userStepLengthIndicatorImgView = (ImageView) findViewById(R.id.uis_userStepLength_indicator_imageView);

		// get user step length
		userStepLengthTextView = (TextView) findViewById(R.id.uis_userStepLength_textView);

		// check user step length and set user step length
		if (null != userStepLength) {
			userStepLengthTextView.setText(userStepLength
					+ getString(R.string.userStepLength_unit));
		}

		// get user step length calculate switch
		Switch _userStepLenCalcSwitch = (Switch) findViewById(R.id.uis_userStepLength_calculate_switch);

		// check user step length calculate type
		if (UserStepLenCalcType.MANUAL_CALC_SETPLEN == userStepLenCalcType) {
			// set user step length calculate switch not checked
			_userStepLenCalcSwitch.setChecked(false);

			// set user step length relativeLayout clickable
			userStepLengthRelativeLayout.setClickable(true);

			// show user step length indicator imageView
			userStepLengthIndicatorImgView.setVisibility(View.VISIBLE);
		}

		// set its on checked changed listener
		_userStepLenCalcSwitch
				.setOnCheckedChangeListener(new StepLenCalcSwitchOnCheckedChangeListener());
	}

	/**
	 * @title updateUserInfo
	 * @descriptor update user info for setting listView UI
	 * @param userInfo
	 *            : new set user info
	 * @author Ares
	 */
	private void updateUserInfo(UserInfoBean userInfo) {
		// update user info for setting listView UI
		// gender
		if (null != userInfo.getGender()) {
			userInfo4SettingListViewAdapter.setData(
					UserInfoAttr4Setting.USER_GENDER, userInfo.getGender()
							.getLabel());
		}
		// age
		if (0 <= userInfo.getAge()) {
			userInfo4SettingListViewAdapter.setData(
					UserInfoAttr4Setting.USER_AGE,
					String.valueOf(userInfo.getAge()));
		}
		// height
		if (0 <= userInfo.getHeight()) {
			userInfo4SettingListViewAdapter.setData(
					UserInfoAttr4Setting.USER_HEIGHT,
					String.valueOf(userInfo.getHeight()));
		}
		// weight
		if (0 <= userInfo.getWeight()) {
			userInfo4SettingListViewAdapter.setData(
					UserInfoAttr4Setting.USER_WEIGHT,
					String.valueOf(userInfo.getWeight()));
		}
	}

	// inner class
	/**
	 * @name UserInfoSettingRequestCode
	 * @descriptor user info setting request code
	 * @author Ares
	 * @version 1.0
	 */
	class UserInfoSettingRequestCode {

		// user info item editor request code
		private static final int UIS_USERINFOITEM_EDITOR_REQCODE = 1000;

	}

	/**
	 * @name UserInfoSettingExtraData
	 * @descriptor user info setting extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class UserInfoSettingExtraData {

		// user info setting, editor user info attribute and editor info value
		public static final String UIS_EI_ATTRIBUTE = "userInfoSetting_userInfo_attribute";
		public static final String UIS_EI_VALUE = "userInfoSetting_userInfo_editorInfo_value";

	}

	/**
	 * @name UISUserInfoEditorOnActivityResult
	 * @descriptor user info setting user info editor on activity result
	 * @author Ares
	 * @version 1.0
	 */
	class UISUserInfoEditorOnActivityResult implements ISSBaseActivityResult {

		@Override
		public void onActivityResult(int resultCode, Intent data) {
			// check the result code
			if (RESULT_OK == resultCode) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					// get and check editor user info attribute and value
					UserInfoAttr4Setting _editorAttr = (UserInfoAttr4Setting) _extraData
							.getSerializable(UserInfoSettingExtraData.UIS_EI_ATTRIBUTE);
					String _editorValue = _extraData
							.getString(UserInfoSettingExtraData.UIS_EI_VALUE);

					switch (_editorAttr) {
					case USER_GENDER:
					case USER_AGE:
					case USER_HEIGHT:
					case USER_WEIGHT:
						// update editor user info value
						userInfo4SettingListViewAdapter.setData(_editorAttr,
								_editorValue);

						// check editor user info value and update user info to
						// remote server
						if (!"".equalsIgnoreCase(_editorValue)) {
							userInfoModel
									.updateUserInfo(
											loginUser.getUserId(),
											loginUser.getUserKey(),
											UserInfoAttr4Setting.USER_AGE == _editorAttr
													&& null != _editorValue ? Integer
													.parseInt(_editorValue)
													: (0 < userInfoModel
															.getUserInfo()
															.getAge() ? userInfoModel
															.getUserInfo()
															.getAge() : 0),
											UserInfoAttr4Setting.USER_GENDER == _editorAttr
													&& null != _editorValue ? UserGender
													.getGenderWithLabel(_editorValue)
													: (null != userInfoModel
															.getUserInfo()
															.getGender() ? userInfoModel
															.getUserInfo()
															.getGender()
															: UserGender
																	.getGender("")),
											UserInfoAttr4Setting.USER_HEIGHT == _editorAttr
													&& null != _editorValue ? Float
													.parseFloat(_editorValue)
													: (0 < userInfoModel
															.getUserInfo()
															.getHeight() ? userInfoModel
															.getUserInfo()
															.getHeight() : 0.0f),
											UserInfoAttr4Setting.USER_WEIGHT == _editorAttr
													&& null != _editorValue ? Float
													.parseFloat(_editorValue)
													: (0 < userInfoModel
															.getUserInfo()
															.getWeight() ? userInfoModel
															.getUserInfo()
															.getWeight() : 0.0f),
											new ICMConnector() {

												@Override
												public void onSuccess(
														Object... retValue) {
													// check return values
													if (null != retValue
															&& 0 < retValue.length
															&& retValue[retValue.length - 1] instanceof UserInfoBean) {
														// get the update user
														// info object and
														// update its info for
														// setting
														updateUserInfo((UserInfoBean) retValue[retValue.length - 1]);
													} else {
														LOGGER.error("Update user info for setting UI error");
													}
												}

												@Override
												public void onFailure(
														int errorCode,
														String errorMsg) {
													LOGGER.error("Update user info error, error code = "
															+ errorCode
															+ " and message = "
															+ errorMsg);

													//
												}

											});
						}
						break;

					case USER_STEPLENGTH:
						// check editor user step length and save
						userStepLength = null == _editorValue
								|| "".equalsIgnoreCase(_editorValue) ? null
								: Float.parseFloat(_editorValue);

						// check user step length and update its textView text
						userStepLengthTextView
								.setText(null != userStepLength ? userStepLength
										+ getString(R.string.userStepLength_unit)
										: "");

						// save user step length to local storage
						//
						break;

					default:
						// nothing to do
						LOGGER.warning("User info attribute = " + _editorAttr
								+ " for editor not implementation now");
						break;
					}
				} else {
					LOGGER.error("Update editor user info error, the return extra data is null");
				}
			}
		}

	}

	/**
	 * @name UserInfo4SettingListViewAdapter
	 * @descriptor user info for setting listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class UserInfo4SettingListViewAdapter extends SimpleAdapter {

		// user info for setting attribute and value selector content data key
		private static final String USERINFO4SETTING_ATTRIBUTE_KEY = "userInfo_attribute_key";
		private static final String USERINFO4SETTING_VALUE_SELECTORCONTENT_KEY = "userInfo_value_selectorContent_key";

		// context
		private Context context;

		// user info for setting listView adapter data list
		private static List<Map<String, Object>> _sDataList;

		/**
		 * @title UserInfo4SettingListViewAdapter
		 * @descriptor user info for setting listView adapter constructor with
		 *             context, user info for setting, content view resource,
		 *             content view subview data key, and content view subview
		 *             id
		 * @param context
		 *            : context
		 * @param userInfo
		 *            : user info for setting
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public UserInfo4SettingListViewAdapter(Context context,
				UserInfoBean userInfo, int resource, String[] dataKeys,
				int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// save context
			this.context = context;

			// check for setting user info bean
			if (null != userInfo) {
				// define user info for setting attribute and value
				UserInfoAttr4Setting _attribute = null;
				String _value = null;

				// get user info for setting label array
				String[] _labels = context.getResources().getStringArray(
						R.array.userInfo4Setting_labels);

				for (int i = 0; i < _labels.length; i++) {
					// define user info for setting listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// get user info for setting attribute, label and value
					String _label = _labels[i];
					if (context.getString(R.string.userInfo_gender_label)
							.equalsIgnoreCase(_label)) {
						_attribute = UserInfoAttr4Setting.USER_GENDER;
						// get and check user gender
						UserGender _gender = userInfo.getGender();
						_value = null != _gender ? _gender.getLabel() : null;
					} else if (context.getString(R.string.userInfo_age_label)
							.equalsIgnoreCase(_label)) {
						_attribute = UserInfoAttr4Setting.USER_AGE;
						// get and check user age
						int _age = userInfo.getAge();
						_value = 0 <= _age ? _age
								+ context.getString(R.string.userAge_unit)
								: null;
					} else if (context
							.getString(R.string.userInfo_height_label)
							.equalsIgnoreCase(_label)) {
						_attribute = UserInfoAttr4Setting.USER_HEIGHT;
						// get and check user height
						float _height = userInfo.getHeight();
						_value = 0 <= _height ? _height
								+ context.getString(R.string.userHeight_unit)
								: null;
					} else if (context
							.getString(R.string.userInfo_weight_label)
							.equalsIgnoreCase(_label)) {
						_attribute = UserInfoAttr4Setting.USER_WEIGHT;
						// get and check user weight
						float _weight = userInfo.getWeight();
						_value = 0 <= _weight ? _weight
								+ context.getString(R.string.userWeight_unit)
								: null;
					}

					// set data attributes
					_data.put(USERINFO4SETTING_ATTRIBUTE_KEY, _attribute);
					_data.put(
							UserInfo4SettingListViewAdapterKey.USERINFO_LABEL_KEY
									.name(), _label);
					_data.put(
							UserInfo4SettingListViewAdapterKey.USERINFO_VALUE_KEY
									.name(), _value);
					if (context.getString(R.string.userInfo_gender_label)
							.equalsIgnoreCase(_label)) {
						_data.put(USERINFO4SETTING_VALUE_SELECTORCONTENT_KEY,
								UserGender.getGenders());
					}

					// add data to list
					_sDataList.add(_data);
				}

				// notify data set changed
				notifyDataSetChanged();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map<String, ?> getItem(int position) {
			return (Map<String, ?>) super.getItem(position);
		}

		/**
		 * @title getItemLabel
		 * @descriptor get editor user info item label textView text
		 * @param position
		 *            : editor user info listView position
		 * @return editor user info item label
		 * @author Ares
		 */
		public String getItemLabel(int position) {
			// get label with position
			String _label = (String) getItem(position).get(
					UserInfo4SettingListViewAdapterKey.USERINFO_LABEL_KEY
							.name());

			// return the label
			return StringUtils.sTrim(_label,
					context.getString(R.string.userInfo_label_suffix));
		}

		/**
		 * @title getItemAttribute
		 * @descriptor get editor user info attribute
		 * @param position
		 *            : editor user info listView position
		 * @return editor user info attribute
		 * @author Ares
		 */
		public UserInfoAttr4Setting getItemAttribute(int position) {
			// return the attribute
			return (UserInfoAttr4Setting) getItem(position).get(
					USERINFO4SETTING_ATTRIBUTE_KEY);
		}

		/**
		 * @title getItemValue
		 * @descriptor get editor user info item info textView text
		 * @param position
		 *            : editor user info listView position
		 * @return editor user info item value
		 * @author Ares
		 */
		public String getItemValue(int position) {
			// get value with position
			String _value = (String) getItem(position).get(
					UserInfo4SettingListViewAdapterKey.USERINFO_VALUE_KEY
							.name());

			// return the value
			return null == _value ? ""
					: StringUtils
							.cStrip(_value,
									new StringBuilder()
											.append(context
													.getString(R.string.userStepLength_unit))
											.append(context
													.getString(R.string.userAge_unit))
											.append(context
													.getString(R.string.userHeight_unit))
											.append(context
													.getString(R.string.userWeight_unit))
											.toString());
		}

		/**
		 * @title getItemValueSelectorContent
		 * @descriptor get editor user info item value selector content save on
		 *             data list
		 * @param position
		 *            : editor user info listView position
		 * @return editor user info item value selector content
		 * @author Ares
		 */
		@SuppressWarnings("unchecked")
		public List<String> getItemValueSelectorContent(int position) {
			// return the value selector content
			return (List<String>) getItem(position).get(
					USERINFO4SETTING_VALUE_SELECTORCONTENT_KEY);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// get content view
			View _contentView = super.getView(position, convertView, parent);

			// get data count
			int _dataCount = getCount();

			// check position and set content view background
			if (1 < _dataCount) {
				if (0 == position) {
					// first
					_contentView
							.setBackgroundResource(R.drawable.common_form_multipletop_item_bg);
				} else if (_dataCount - 1 == position) {
					// last
					_contentView
							.setBackgroundResource(R.drawable.common_form_multiplebottom_item_bg);
				} else {
					// middle
					_contentView
							.setBackgroundResource(R.drawable.common_form_multiplemiddle_item_bg);
				}
			}

			return _contentView;
		}

		/**
		 * @title setData
		 * @descriptor set editor user info data
		 * @param attr
		 *            : editor user info attribute
		 * @param value
		 *            : editor user info value
		 * @author Ares
		 */
		public void setData(UserInfoAttr4Setting attr, String value) {
			// traversal editor user info data list
			for (Map<String, Object> _editorUserInfoData : _sDataList) {
				// get and check editor user info attribute
				if (null != attr
						&& (UserInfoAttr4Setting) _editorUserInfoData
								.get(USERINFO4SETTING_ATTRIBUTE_KEY) == attr) {
					// check the value
					if (null != value && !"".equalsIgnoreCase(value)) {
						// update editor user info value with attribute
						switch (attr) {
						case USER_AGE:
							value += context.getString(R.string.userAge_unit);
							break;

						case USER_HEIGHT:
							value += context
									.getString(R.string.userHeight_unit);
							break;

						case USER_WEIGHT:
							value += context
									.getString(R.string.userWeight_unit);
							break;

						default:
							// nothing to do
							break;
						}
					}
					_editorUserInfoData
							.put(UserInfo4SettingListViewAdapterKey.USERINFO_VALUE_KEY
									.name(), value);

					// break immediately
					break;
				}
			}

			// notify data set changed
			notifyDataSetChanged();
		}

		// inner class
		/**
		 * @name UserInfo4SettingListViewAdapterKey
		 * @descriptor user info for setting listView adapter key enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum UserInfo4SettingListViewAdapterKey {

			// user info label and value key
			USERINFO_LABEL_KEY, USERINFO_VALUE_KEY;

		}

	}

	/**
	 * @name UserInfoSettingItemOnClickListener
	 * @descriptor user info for setting listView on item click listener
	 * @author Ares
	 * @version 1.0
	 */
	class UserInfo4SettingItemOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// define user info item editor extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put editor user info attribute, name, info and its selector
			// content to extra data map as param
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_ATTRIBUTE,
					userInfo4SettingListViewAdapter.getItemAttribute(position));
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_NAME,
					userInfo4SettingListViewAdapter.getItemLabel(position));
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_VALUE,
					userInfo4SettingListViewAdapter.getItemValue(position));
			List<String> _valueSelectorContent = userInfo4SettingListViewAdapter
					.getItemValueSelectorContent(position);
			if (null != _valueSelectorContent) {
				_extraMap.put(
						UserInfoItemEditorExtraData.UI_EI_VALUESELECTORCONTENT,
						_valueSelectorContent);
			}

			// go to user info item editor activity with extra data map
			pushActivityForResult(UserInfoItemEditorActivity.class, _extraMap,
					UserInfoSettingRequestCode.UIS_USERINFOITEM_EDITOR_REQCODE,
					new UISUserInfoEditorOnActivityResult());
		}

	}

	/**
	 * @name StepLenRelativeLayoutOnClickListener
	 * @descriptor user step length relativeLayout on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class StepLenRelativeLayoutOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// define user info item editor extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put editor user step length attribute, name and info to extra
			// data map as param
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_ATTRIBUTE,
					UserInfoAttr4Setting.USER_STEPLENGTH);
			_extraMap
					.put(UserInfoItemEditorExtraData.UI_EI_NAME,
							StringUtils
									.sTrim(getString(R.string.userStepLength_label_textView_text),
											getString(R.string.userInfo_label_suffix)));
			_extraMap.put(
					UserInfoItemEditorExtraData.UI_EI_VALUE,
					null == userStepLength ? "" : String
							.valueOf(userStepLength));

			// go to user info item editor activity with extra data map
			pushActivityForResult(UserInfoItemEditorActivity.class, _extraMap,
					UserInfoSettingRequestCode.UIS_USERINFOITEM_EDITOR_REQCODE,
					new UISUserInfoEditorOnActivityResult());
		}

	}

	/**
	 * @name StepLenCalcSwitchOnCheckedChangeListener
	 * @descriptor user step length calculate switch on checked changed listener
	 * @author Ares
	 * @version 1.0
	 */
	class StepLenCalcSwitchOnCheckedChangeListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// check compound button checked
			if (isChecked) {
				// set user step length relativeLayout unclickable if needed
				if (userStepLengthRelativeLayout.isClickable()) {
					userStepLengthRelativeLayout.setClickable(false);
				}

				// hide user step length indicator imageView if needed
				if (View.VISIBLE == userStepLengthIndicatorImgView
						.getVisibility()) {
					userStepLengthIndicatorImgView.setVisibility(View.GONE);
				}

				// save editor user step length
				// test by ares
				userStepLength = Float.parseFloat("22.0");

				// update user step length
				userStepLengthTextView
						.setText(null != userStepLength ? userStepLength
								+ getString(R.string.userStepLength_unit)
								: getString(R.string.userStepLength_notSet));
			} else {
				// set user step length relativeLayout clickable
				userStepLengthRelativeLayout.setClickable(true);

				// user step length relativeLayout perform click event
				userStepLengthRelativeLayout.performClick();

				// show user step length indicator imageView
				userStepLengthIndicatorImgView.setVisibility(View.VISIBLE);
			}
		}

	}

}
