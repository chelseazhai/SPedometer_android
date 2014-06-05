/**
 * 
 */
package com.smartsport.spedometer.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSSimpleAdapterViewBinder;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupInviteInviteeListViewAdapter
 * @descriptor walk or within group compete invite invitee select invitee
 *             listView adapter
 * @author Ares
 * @version 1.0
 */
public class GroupInviteInviteeListViewAdapter extends SimpleAdapter {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			GroupInviteInviteeListViewAdapter.class);

	// walk or within group compete invite invitee listView adapter data list
	private static List<Map<String, Object>> _sDataList;

	// walk or within group compete invite group type
	private GroupType groupType;

	/**
	 * @title GroupInviteInviteeListViewAdapter
	 * @descriptor walk or within group compete invite invitee listView adapter
	 *             constructor with context, user friend list, content view
	 *             resource, content view subview data key, and content view
	 *             subview id
	 * @param context
	 *            : context
	 * @param groupType
	 *            : walk or within group compete invite group type
	 * @param userFriends
	 *            : user friend list
	 * @param resource
	 *            : resource id
	 * @param dataKeys
	 *            : content view subview data key array
	 * @param ids
	 *            : content view subview id array
	 */
	public GroupInviteInviteeListViewAdapter(Context context,
			GroupType groupType, List<UserInfoBean> userFriends, int resource,
			String[] dataKeys, int[] ids) {
		super(context, _sDataList = new ArrayList<Map<String, Object>>(),
				resource, dataKeys, ids);

		// save walk or within group compete invite group type
		this.groupType = groupType;

		// set view binder
		setViewBinder(new GroupInviteInviteeListViewAdapterViewBinder());

		// set user friends as walk or within group compete invite invitee list
		setFriendsAsGroupInviteInvitees(userFriends);
	}

	/**
	 * @title setFriendsAsGroupInviteInvitees
	 * @descriptor set user new friend list as walk or within group compete
	 *             invite invitee list
	 * @param friends
	 *            : user new friend list
	 */
	public void setFriendsAsGroupInviteInvitees(List<UserInfoBean> friends) {
		// check user new friend list
		if (null != friends) {
			// clear walk or within group compete invite invitee listView
			// adapter data list and add new data
			_sDataList.clear();

			// traversal user friend list
			for (UserInfoBean _friend : friends) {
				// define walk or within group compete invite invitee listView
				// adapter data
				Map<String, Object> _data = new HashMap<String, Object>();

				// set data attributes
				if (null != groupType && GroupType.COMPETE_GROUP == groupType) {
					_data.put(
							GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_SELECTEDFLAG_KEY
									.name(), Boolean.valueOf(false));
				}
				_data.put(
						GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_AVATAR_KEY
								.name(), _friend.getAvatarUrl());
				_data.put(
						GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_NICKNAME_KEY
								.name(), _friend.getNickname());

				// add data to list
				_sDataList.add(_data);
			}

			// notify data set changed
			notifyDataSetChanged();
		} else {
			LOGGER.error("Set user new friend list as walk or within group compete invite invitee list error, the new set data list is null");
		}
	}

	/**
	 * @title setFriendSelectState
	 * @descriptor set the friend select state at the position
	 * @param position
	 *            : the selected friend position
	 * @param isSelected
	 *            : friend selected flag
	 */
	public void setFriendSelectState(int position, boolean isSelected) {
		// get the selected within group compete invite invitee listView adapter
		// data
		Map<String, Object> _data = _sDataList.get(position);

		// set data attributes
		_data.put(
				GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_SELECTEDFLAG_KEY
						.name(), isSelected);

		// add data to list
		_sDataList.set(position, _data);

		// notify data set changed
		notifyDataSetChanged();
	}

	// inner class
	/**
	 * @name GroupInviteInviteeListViewAdapterKey
	 * @descriptor walk or within group compete invite invitee listView adapter
	 *             key enumeration
	 * @author Ares
	 * @version 1.0
	 */
	public enum GroupInviteInviteeListViewAdapterKey {

		// walk or within group compete invite invitee selected flag, invitee
		// avatar and nickname key
		GROUPINVITEINVITEE_SELECTEDFLAG_KEY, GROUPINVITEINVITEE_AVATAR_KEY, GROUPINVITEINVITEE_NICKNAME_KEY;

	}

	/**
	 * @name GroupInviteInviteeListViewAdapterViewBinder
	 * @descriptor walk or within group compete invite invitee listView adapter
	 *             view binder
	 * @author Ares
	 * @version 1.0
	 */
	class GroupInviteInviteeListViewAdapterViewBinder extends
			SSSimpleAdapterViewBinder {

		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			boolean _ret = false;

			// check view type
			// imageView with its visibility, and image resource
			if (view instanceof ImageView && data instanceof Boolean) {
				// show the view
				view.setVisibility(View.VISIBLE);

				// convert data to boolean
				Boolean _booleanData = (Boolean) data;

				// check the boolean data and get resource id
				int _imgResourceId = R.drawable.img_withingroupcompete_invitee_unselected;
				if (null != _booleanData && _booleanData) {
					_imgResourceId = R.drawable.img_withingroupcompete_invitee_selected;
				}

				// set the imageView image resource
				((ImageView) view).setImageResource(_imgResourceId);

				// update return flag
				_ret = true;
			} else {
				// update return flag
				_ret = super.setViewValue(view, data, textRepresentation);
			}

			return _ret;
		}

	}

}
