/**
 * 
 */
package com.smartsport.spedometer.group.compete;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.group.walk.WalkInviteInviteeSelectActivity.WalkInviteInviteeListViewAdapter;
import com.smartsport.spedometer.group.walk.WalkInviteInviteeSelectActivity.WalkInviteInviteeOnItemClickListener;
import com.smartsport.spedometer.group.walk.WalkInviteInviteeSelectActivity.WalkInviteInviteeListViewAdapter.WalkInviteInviteeListViewAdapterKey;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.user.UserInfoModel;

/**
 * @name WithinGroupCompeteInviteeSelectActivity
 * @descriptor smartsport within group compete invitee select activity
 * @author Ares
 * @version 1.0
 */
public class WithinGroupCompeteInviteeSelectActivity extends SSBaseActivity {

	// user info model
	private UserInfoModel userInfoModel;
	
	// within group compete invitee listView adapter
		private WalkInviteInviteeListViewAdapter withinGroupCompeteInviteeListViewAdapter;

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
		setContentView(R.layout.activity_withingroupcompete_invitee_select_layout);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set left bar button item
		setLeftBarButtonItem(new SSBNavTitleBarButtonItem(
				this,
				R.string.cancel_selectWithinGroupCompeteInvitee_barbtnitem_title,
				new CancelSelectWithinGroupCompeteInviteeBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(R.string.withinGroupCompeteInviteeSelect_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);
		
		// get within group compete invitee listView
				ListView _withinGroupCompeteInviteeListView = (ListView) findViewById(R.id.wigciis_invitees_listView);

				// set its adapter
				_withinGroupCompeteInviteeListView
						.setAdapter(walkInviteInviteeListViewAdapter = new WalkInviteInviteeListViewAdapter(
								this,
								userInfoModel.getFriendsInfo(),
								R.layout.userfriend_listview_item_layout,
								new String[] {
										WalkInviteInviteeListViewAdapterKey.WALKINVITEINVITEE_AVATAR_KEY
												.name(),
										WalkInviteInviteeListViewAdapterKey.WALKINVITEINVITEE_NICKNAME_KEY
												.name() }, new int[] {
										R.id.userfriend_item_friendAvatar_imageView,
										R.id.userfriend_item_friendNickname_textView }));

				// set its on item click listener
				_withinGroupCompeteInviteeListView
						.setOnItemClickListener(new WalkInviteInviteeOnItemClickListener());

		//
	}

	// inner class
	/**
	 * @name CancelSelectWithinGroupCompeteInviteeBarBtnItemOnClickListener
	 * @descriptor cancel select within group compete invitee bar button item on
	 *             click listener
	 * @author Ares
	 * @version 1.0
	 */
	class CancelSelectWithinGroupCompeteInviteeBarBtnItemOnClickListener
			implements OnClickListener {

		@Override
		public void onClick(View v) {
			// dismiss the activity with result code
			dismissActivityWithResult();
		}

	}

}
