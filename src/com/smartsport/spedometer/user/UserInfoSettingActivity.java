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
import android.util.SparseArray;
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
import com.smartsport.spedometer.user.UserInfoItemEditorActivity.UserInfoEditorType;
import com.smartsport.spedometer.user.UserInfoItemEditorActivity.UserInfoItemEditorExtraData;
import com.smartsport.spedometer.user.UserInfoSettingActivity.UserInfo4SettingListViewAdapter.UserInfo4SettingListViewAdapterKey;
import com.smartsport.spedometer.utils.SSLogger;

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

	// user info model
	private UserInfoModel userInfoModel;

	// user info item editor widget id, widget info textView map(key: editor
	// widget id and value: widget info textView)
	private final SparseArray<TextView> UI_EDITORWIDGETIDINFOTEXTVIEW_MAP = new SparseArray<TextView>();

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

		// initialize user info model
		userInfoModel = new UserInfoModel();

		// load user info with step length and its calculate type from local
		// storage
		// test by ares
		userInfoModel.getUserInfo().setGender(UserGender.GENDER_UNKNOWN);
		userInfoModel.getUserInfo().setAge(20);
		userInfoModel.getUserInfo().setHeight(173.0f);
		userInfoModel.getUserInfo().setWeight(72.0f);
		userStepLength = 22.0f;
		userStepLenCalcType = UserStepLenCalcType.MANUAL_CALC_SETPLEN;
		//

		// set content view
		setContentView(R.layout.activity_userinfo_setting);

		// get user info
		userInfoModel.getUserInfo(123123, "token", new ICMConnector() {

			//

		});
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(R.string.userinfo_setting_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(26.0f);

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

		// set user step length
		userStepLengthTextView.setText(null != userStepLength ? userStepLength
				+ getString(R.string.userStepLength_unit)
				: getString(R.string.userStepLength_notSet));

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

	// inner class
	/**
	 * @name UserInfoSettingRequestCode
	 * @descriptor user info setting request code
	 * @author Ares
	 * @version 1.0
	 */
	class UserInfoSettingRequestCode {

		// user info item editor request code
		private static final int UII_EDITOR_REQCODE = 1000;

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
			LOGGER.info("UISUserInfoEditorOnActivityResult - resultCode = "
					+ resultCode + " and data = " + data);

			//
		}

	}

	/**
	 * @name UserInfo4SettingListViewAdapter
	 * @descriptor user info for setting listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class UserInfo4SettingListViewAdapter extends SimpleAdapter {

		// user info for setting value selector content data key
		private static final String USERINFO4SETTING_VALUE_SELECTORCONTENT_KEY = "userInfo_value_selectorContent_key";

		// user info for setting listView adapter data list
		private static List<Map<String, ?>> _sDataList;

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
			super(context, _sDataList = new ArrayList<Map<String, ?>>(),
					resource, dataKeys, ids);

			// check for setting user info bean
			if (null != userInfo) {
				// define user info for setting value
				String _value = null;

				// get user info for setting label array
				String[] _labels = context.getResources().getStringArray(
						R.array.userInfo4Setting_labels);

				for (int i = 0; i < _labels.length; i++) {
					// define user info for setting listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// get user info for setting label and value
					String _label = _labels[i];
					if (context.getString(R.string.userInfo_gender_label)
							.equalsIgnoreCase(_label)) {
						_value = userInfo.getGender().getLabel();
					} else if (context.getString(R.string.userInfo_age_label)
							.equalsIgnoreCase(_label)) {
						_value = userInfo.getAge()
								+ context.getString(R.string.userAge_unit);
					} else if (context
							.getString(R.string.userInfo_height_label)
							.equalsIgnoreCase(_label)) {
						_value = userInfo.getHeight()
								+ context.getString(R.string.userHeight_unit);
					} else if (context
							.getString(R.string.userInfo_weight_label)
							.equalsIgnoreCase(_label)) {
						_value = userInfo.getWeight()
								+ context.getString(R.string.userWeight_unit);
					}

					// set data attributes
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

		public String getItemLabel(int position) {
			// get label with position
			String _label = (String) getItem(position).get(
					UserInfo4SettingListViewAdapterKey.USERINFO_LABEL_KEY
							.name());

			// return the label
			return _label;
		}

		public String getItemValue(int position) {
			// get value with position
			String _value = (String) getItem(position).get(
					UserInfo4SettingListViewAdapterKey.USERINFO_VALUE_KEY
							.name());

			// return the value
			return _value;
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
			// get user info value textView
			TextView _userInfoValueTextView = (TextView) view
					.findViewById(R.id.uisi_info_textView);

			// add user info value textView to user info editor widget id, info
			// textView map
			UI_EDITORWIDGETIDINFOTEXTVIEW_MAP.put(
					_userInfoValueTextView.hashCode(), _userInfoValueTextView);

			// define user info item editor extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put user info item editor widget id, name, type, info and its
			// selector content to extra data map as param
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_WIDGETID,
					_userInfoValueTextView.hashCode());
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_NAME,
					userInfo4SettingListViewAdapter.getItemLabel(position));
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_TYPE,
					UserInfoEditorType.AGE_PICKER);
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_VALUE,
					userInfo4SettingListViewAdapter.getItemValue(position));
			_extraMap.put(
					UserInfoItemEditorExtraData.UI_EI_VALUESELECTORCONTENT,
					UserGender.getGenders());

			// go to user info item editor activity with extra data map
			pushActivityForResult(UserInfoItemEditorActivity.class, _extraMap,
					UserInfoSettingRequestCode.UII_EDITOR_REQCODE,
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
			// get step length info textView
			TextView _stepLengthInfoTextView = (TextView) v
					.findViewById(R.id.uis_userStepLength_textView);

			// add step length info textView to user info editor widget id, info
			// textView map
			UI_EDITORWIDGETIDINFOTEXTVIEW_MAP
					.put(_stepLengthInfoTextView.hashCode(),
							_stepLengthInfoTextView);

			// define user info item editor extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put user step length editor widget id, name, type and info to
			// extra data map as param
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_WIDGETID,
					_stepLengthInfoTextView.hashCode());
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_NAME, "步长");
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_TYPE,
					UserInfoEditorType.HWSL_EDITTEXT);
			_extraMap.put(UserInfoItemEditorExtraData.UI_EI_VALUE,
					String.valueOf(userStepLength));

			// go to user info item editor activity with extra data map
			pushActivityForResult(UserInfoItemEditorActivity.class, _extraMap,
					UserInfoSettingRequestCode.UII_EDITOR_REQCODE,
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
