/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.group.GroupInviteInviteeListViewAdapter;
import com.smartsport.spedometer.group.GroupInviteInviteeListViewAdapter.GroupInviteInviteeListViewAdapterKey;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.group.ScheduleWalkInviteGroupsActivity.WalkInviteInviteeSelectOrWalkControlExtraData;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
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

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			WalkInviteInviteeSelectActivity.class);

	// user info model
	private UserInfoModel userInfoModel;

	// walk invite invitee listView adapter
	private GroupInviteInviteeListViewAdapter walkInviteInviteeListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize user info model
		userInfoModel = new UserInfoModel();

		// // test by ares
		// for (int i = 0; i < 20; i++) {
		// UserInfoBean _friend = new UserInfoBean();
		//
		// _friend.setUserId(10020 + i);
		// _friend.setAvatarUrl("/avatar/img_jhsd_sdjhf111");
		// _friend.setNickname("好友" + i);
		// _friend.setGender(0 == i ? UserGender.GENDER_UNKNOWN
		// : 0 == i % 2 ? UserGender.FEMALE : UserGender.MALE);
		// _friend.setAge(10 + i);
		// _friend.setHeight(170.0f + i);
		// _friend.setWeight(70.0f + i);
		//
		// userInfoModel.getFriendsInfo().add(_friend);
		// }

		// set content view
		setContentView(R.layout.activity_walkinvite_invitee_select_layout);

		// get user friend list from remote server
		userInfoModel.getFriends(1002, "token", new ICMConnector() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object... retValue) {
				// check return values
				if (null != retValue && 0 < retValue.length
						&& retValue[retValue.length - 1] instanceof List) {
					// get the user info object and update its info for setting
					walkInviteInviteeListViewAdapter
							.setFriendsAsGroupInviteInvitees((List<UserInfoBean>) retValue[retValue.length - 1]);
				} else {
					LOGGER.error("Update user friends for walk invite invitee select UI error");
				}
			}

			@Override
			public void onFailure(int errorCode, String errorMsg) {
				LOGGER.error("Get user friends from remote server error, error code = "
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
				.setAdapter(walkInviteInviteeListViewAdapter = new GroupInviteInviteeListViewAdapter(
						this,
						GroupType.WALK_GROUP,
						userInfoModel.getFriendsInfo(),
						R.layout.userfriend_listview_item_layout,
						new String[] {
								GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_AVATAR_KEY
										.name(),
								GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_NICKNAME_KEY
										.name() }, new int[] {
								R.id.userfriend_item_friendAvatar_imageView,
								R.id.userfriend_item_friendNickname_textView }));

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
