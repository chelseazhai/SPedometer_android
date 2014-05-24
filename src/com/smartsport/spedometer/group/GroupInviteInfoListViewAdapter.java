/**
 * 
 */
package com.smartsport.spedometer.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.StringUtils;

/**
 * @name GroupInviteInfoListViewAdapter
 * @descriptor walk or within group compete invite info listView adapter
 * @author Ares
 * @version 1.0
 */
public class GroupInviteInfoListViewAdapter extends SimpleAdapter {

	// walk or within group compete invite info for setting attribute key
	private static final String GROUPINVITEINFO4SETTING_ATTRIBUTE_KEY = "walkOrWithinGroupCompete_inviteInfo_attribute_key";

	// context
	private Context context;

	// walk or within group compete invite info listView adapter data list
	private static List<Map<String, Object>> _sDataList;

	/**
	 * @title GroupInviteInfoListViewAdapter
	 * @descriptor walk or within group compete invite info for setting listView
	 *             adapter constructor with context, label array, content view
	 *             resource, content view subview data key, and content view
	 *             subview id
	 * @param context
	 *            : context
	 * @param labels
	 *            : walk or within group compete invite info for setting label
	 *            array
	 * @param resource
	 *            : resource id
	 * @param dataKeys
	 *            : content view subview data key array
	 * @param ids
	 *            : content view subview id array
	 */
	public GroupInviteInfoListViewAdapter(Context context, String[] labels,
			int resource, String[] dataKeys, int[] ids) {
		super(context, _sDataList = new ArrayList<Map<String, Object>>(),
				resource, dataKeys, ids);

		// save context
		this.context = context;

		// check the for setting walk or within group compete invite info label
		// array
		if (null != labels) {
			// define walk or within group compete invite info for setting attribute and value
			GroupInviteInfoAttr4Setting _attribute = null;
			String _value = null;

			for (int i = 0; i < labels.length; i++) {
				// define walk or within group compete invite info for setting listView adapter data
				Map<String, Object> _data = new HashMap<String, Object>();

				// get walk or within group compete invite info for setting attribute, label and value
				String _label = labels[i];
				if (context.getString(R.string.walkOrWithinGroupCompete_inviteInfo_topic_label)
						.equalsIgnoreCase(_label)) {
					_attribute = GroupInviteInfoAttr4Setting.GROUP_TOPIC;
				} else if (context.getString(R.string.walkOrWithinGroupCompete_inviteInfo_scheduleTime_label)
						.equalsIgnoreCase(_label)) {
					_attribute = GroupInviteInfoAttr4Setting.WALKINVITE_SCHEDULETIME;
				} 

//				// set data attributes
//				_data.put(USERINFO4SETTING_ATTRIBUTE_KEY, _attribute);
//				_data.put(
//						UserInfo4SettingListViewAdapterKey.USERINFO_LABEL_KEY
//								.name(), _label);
//				_data.put(
//						UserInfo4SettingListViewAdapterKey.USERINFO_VALUE_KEY
//								.name(), _value);
//				if (context.getString(R.string.userInfo_gender_label)
//						.equalsIgnoreCase(_label)) {
//					_data.put(USERINFO4SETTING_VALUE_SELECTORCONTENT_KEY,
//							UserGender.getGenders());
//				}

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
	 * @descriptor get editor walk or within group compete invite info item label textView text
	 * @param position : editor walk or within group compete invite info listView position
	 * @return editor walk or within group compete invite info item label
	 * @author Ares
	 */
	public String getItemLabel(int position) {
		// get label with position
		String _label = (String) getItem(position).get(
				GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_LABEL_KEY
						.name());

		// return the label
		return StringUtils.sTrim(_label,
				context.getString(R.string.walkOrWithinGroupCompete_inviteInfo_label_suffix));
	}

	/**
	 * @title getItemAttribute
	 * @descriptor get editor walk or within group compete invite info attribute
	 * @param position : editor walk or within group compete invite info listView position
	 * @return editor walk or within group compete invite info attribute
	 * @author Ares
	 */
	public GroupInviteInfoAttr4Setting getItemAttribute(int position) {
		// return the attribute
		return (GroupInviteInfoAttr4Setting) getItem(position).get(
				GROUPINVITEINFO4SETTING_ATTRIBUTE_KEY);
	}

	/**
	 * @title getItemValue
	 * @descriptor get editor walk or within group compete invite info item info textView text
	 * @param position : editor walk or within group compete invite info listView position
	 * @return editor walk or within group compete invite info item value
	 * @author Ares
	 */
	public String getItemValue(int position) {
		// get value with position
		String _value = (String) getItem(position).get(
				GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_VALUE_KEY
						.name());

		// return the value
		return null == _value ? "" : _value;
	}

//	/**
//	 * @title setData
//	 * @descriptor set editor walk or within group compete invite info data
//	 * @param attr
//	 *            : editor walk or within group compete invite info attribute
//	 * @param value
//	 *            : editor walk or within group compete invite info value
//	 * @author Ares
//	 */
//	public void setData(GroupInviteInfoAttr4Setting attr, String value) {
//		// traversal editor walk or within group compete invite info data list
//		for (Map<String, Object> _editorGroupInfoData : _sDataList) {
//			// get and check editor walk or within group compete invite info attribute
//			if (null != attr
//					&& (GroupInviteInfoAttr4Setting) _editorGroupInfoData
//							.get(GROUPINVITEINFO4SETTING_ATTRIBUTE_KEY) == attr) {
//				// check the value
//				if (null != value && !"".equalsIgnoreCase(value)) {
//					// update editor walk or within group compete invite info value with attribute
//					switch (attr) {
//					case USER_AGE:
//						value += context.getString(R.string.userAge_unit);
//						break;
//
//					case USER_HEIGHT:
//						value += context
//								.getString(R.string.userHeight_unit);
//						break;
//
//					case USER_WEIGHT:
//						value += context
//								.getString(R.string.userWeight_unit);
//						break;
//
//					default:
//						// nothing to do
//						break;
//					}
//				} else {
//					// clear the special item user info
//					value = context.getString(R.string.userInfoItem_notSet);
//				}
//				_editorGroupInfoData
//						.put(GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_VALUE_KEY
//								.name(), value);
//
//				// break immediately
//				break;
//			}
//		}
//
//		// notify data set changed
//		notifyDataSetChanged();
//	}

	// inner class
	/**
	 * @name GroupInviteInfo4SettingListViewAdapterKey
	 * @descriptor walk or within group compete invite info for setting listView
	 *             adapter key enumeration
	 * @author Ares
	 * @version 1.0
	 */
	public enum GroupInviteInfo4SettingListViewAdapterKey {

		// walk or within group compete invite info label and value key
		GROUPINVITEINFO_LABEL_KEY, GROUPINVITEINFO_VALUE_KEY;

	}

}
