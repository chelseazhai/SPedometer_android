/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.group.ScheduleWalkInviteGroupsActivity.WalkInviteInviteeSelectOrWalkControlExtraData;
import com.smartsport.spedometer.group.walk.WalkInviteInviteeSelectActivity.WalkInviteInviteeListViewAdapter.WalkInviteInviteeListViewAdapterKey;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.user.UserInfoModel;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WalkInviteInviteeSelectActivity
 * @descriptor new schedule walk invite group invitee select activity
 * @author Ares
 * @version 1.0
 */
public class WalkInviteInviteeSelectActivity extends SSBaseActivity {

	// user info model
	private UserInfoModel userInfoModel;

	// walk invite invitee listView adapter
	private WalkInviteInviteeListViewAdapter walkInviteInviteeListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize user info model
		userInfoModel = new UserInfoModel();

		// test by ares
		for (int i = 0; i < 20; i++) {
			UserInfoBean _friend = new UserInfoBean();

			_friend.setUserId(10020 + i);
			_friend.setAvatarUrl("/avatar/img_jhsd_sdjhf111");
			_friend.setNickname("好友" + i);
			_friend.setGender(0 == i ? UserGender.GENDER_UNKNOWN
					: 0 == i % 2 ? UserGender.FEMALE : UserGender.MALE);
			_friend.setAge(10 + i);
			_friend.setHeight(170.0f + i);
			_friend.setWeight(70.0f + i);

			userInfoModel.getFriendsInfo().add(_friend);
		}

		// get user friend list from remote server
		userInfoModel.getFriends(123123, "token", new ICMConnector() {

			//

		});

		// set content view
		setContentView(R.layout.activity_walkinvite_invitee_select_layout);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set left bar button item
		setLeftBarButtonItem(new SSBNavTitleBarButtonItem(this,
				R.string.cancel_selectWalkInviteInvitee_barbtnitem_title,
				new CancelSelectWalkInviteInviteeBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(R.string.walkInviteInviteeSelect_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get walk invite invitee listView
		ListView _walkInviteInviteeListView = (ListView) findViewById(R.id.wiis_invitees_listView);

		// set its adapter
		_walkInviteInviteeListView
				.setAdapter(walkInviteInviteeListViewAdapter = new WalkInviteInviteeListViewAdapter(
						this,
						userInfoModel.getFriendsInfo(),
						R.layout.userfriend_listview_item_layout,
						new String[] {
								WalkInviteInviteeListViewAdapterKey.WALKINVITEINVITEE_AVATAR_KEY
										.name(),
								WalkInviteInviteeListViewAdapterKey.WALKINVITEINVITEE_NICKNAME_KEY
										.name() }, new int[] {
								R.id.wiis_item_friendAvatar_imageView,
								R.id.wiis_item_friendNickname_textView }));

		// set its on item click listener
		_walkInviteInviteeListView
				.setOnItemClickListener(new WalkInviteInviteeOnItemClickListener());
	}

	// inner class
	/**
	 * @name CancelSelectWalkInviteInviteeBarBtnItemOnClickListener
	 * @descriptor cancel select walk invite invitee bar button item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class CancelSelectWalkInviteInviteeBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// dismiss the activity with result code
			dismissActivityWithResult();
		}

	}

	/**
	 * @name WalkInviteInviteeListViewAdapter
	 * @descriptor walk invite invitee select invitee listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class WalkInviteInviteeListViewAdapter extends SimpleAdapter {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				WalkInviteInviteeListViewAdapter.class);

		// walk invite invitee listView adapter data list
		private static List<Map<String, Object>> _sDataList;

		/**
		 * @title WalkInviteInviteeListViewAdapter
		 * @descriptor walk invite invitee listView adapter constructor with
		 *             context, user friend list, content view resource, content
		 *             view subview data key, and content view subview id
		 * @param context
		 *            : context
		 * @param userFriends
		 *            : user friend list
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public WalkInviteInviteeListViewAdapter(Context context,
				List<UserInfoBean> userFriends, int resource,
				String[] dataKeys, int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// set user friends as walk invite invitee list
			setFriendsAsWalkInviteInvitees(userFriends);
		}

		/**
		 * @title setFriendsAsWalkInviteInvitees
		 * @descriptor set user new friend list as walk invite invitee list
		 * @param friends
		 *            : user new friend list
		 */
		public void setFriendsAsWalkInviteInvitees(List<UserInfoBean> friends) {
			// check user new friend list
			if (null != friends) {
				// clear walk invite invitee listView adapter data list and add
				// new data
				_sDataList.clear();

				// traversal user friend list
				for (UserInfoBean friend : friends) {
					// define walk invite invitee listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// set data attributes
					_data.put(
							WalkInviteInviteeListViewAdapterKey.WALKINVITEINVITEE_AVATAR_KEY
									.name(), friend.getAvatarUrl());
					_data.put(
							WalkInviteInviteeListViewAdapterKey.WALKINVITEINVITEE_NICKNAME_KEY
									.name(), friend.getNickname());

					// add data to list
					_sDataList.add(_data);
				}

				// notify data set changed
				notifyDataSetChanged();
			} else {
				LOGGER.error("Set user new friend list as walk invite invitee list error, the new set data list is null");
			}
		}

		// inner class
		/**
		 * @name WalkInviteInviteeListViewAdapterKey
		 * @descriptor walk invite invitee listView adapter key enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum WalkInviteInviteeListViewAdapterKey {

			// walk invite invitee avatar and nickname key
			WALKINVITEINVITEE_AVATAR_KEY, WALKINVITEINVITEE_NICKNAME_KEY;

		}

	}

	/**
	 * @name WalkInviteInviteeOnItemClickListener
	 * @descriptor walk invite invitee select item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkInviteInviteeOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// define walk invite invitee select extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put the selected walk invite invitee info to extra data map as
			// param
			_extraMap
					.put(WalkInviteInviteeSelectOrWalkControlExtraData.WIIS_SELECTED_INVITEE_BEAN,
							userInfoModel.getFriendsInfo().get(position));

			// dismiss the activity with result code and extra map
			dismissActivityWithResult(RESULT_OK, _extraMap);
		}

	}

}
