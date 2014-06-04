/**
 * 
 */
package com.smartsport.spedometer.group.compete;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.customwidget.SSHorizontalListView;
import com.smartsport.spedometer.group.GroupInviteInviteeListViewAdapter;
import com.smartsport.spedometer.group.GroupInviteInviteeListViewAdapter.GroupInviteInviteeListViewAdapterKey;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.group.compete.WithinGroupCompeteInviteeSelectActivity.SelectedInviteesHorizontalListViewAdapter.SelectedInviteesHorizontalListViewAdapterKey;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.user.UserInfoModel;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WithinGroupCompeteInviteeSelectActivity
 * @descriptor smartsport within group compete invitee select activity
 * @author Ares
 * @version 1.0
 */
public class WithinGroupCompeteInviteeSelectActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			WithinGroupCompeteInviteeSelectActivity.class);

	// user info model
	private UserInfoModel userInfoModel;

	// within group compete selected invitee id list
	private List<Integer> selectedInviteesIds;

	// within group compete invitee listView
	private ListView withinGroupCompeteInviteeListView;

	// within group compete invitee listView adapter
	private GroupInviteInviteeListViewAdapter withinGroupCompeteInviteeListViewAdapter;

	// within group compete invitee select selected invitee list
	private List<UserInfoBean> selectedInviteeList;

	// within group compete invitee select confirm button
	private Button inviteeSelectConfirmBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize user info model
		userInfoModel = new UserInfoModel();

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the selected within group compete invitee id list
			selectedInviteesIds = _extraData
					.getIntegerArrayList(WithinGroupCompeteInviteeSelectExtraData.WIGCIS_SELECTEDINVITEES_ID);
		}

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
		withinGroupCompeteInviteeListView = (ListView) findViewById(R.id.wigciis_invitees_listView);

		// set its adapter
		withinGroupCompeteInviteeListView
				.setAdapter(withinGroupCompeteInviteeListViewAdapter = new GroupInviteInviteeListViewAdapter(
						this,
						GroupType.COMPETE_GROUP,
						userInfoModel.getFriendsInfo(),
						R.layout.userfriend_listview_item_layout,
						new String[] {
								GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_SELECTEDFLAG_KEY
										.name(),
								GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_AVATAR_KEY
										.name(),
								GroupInviteInviteeListViewAdapterKey.GROUPINVITEINVITEE_NICKNAME_KEY
										.name() }, new int[] {
								R.id.userfriend_item_selectState_imageView,
								R.id.userfriend_item_friendAvatar_imageView,
								R.id.userfriend_item_friendNickname_textView }));

		// check within group compete selected invitee id list and set selected
		// invitees be selected
		if (null != selectedInviteesIds) {
			for (int i = 0; i < userInfoModel.getFriendsInfo().size(); i++) {
				// get each friend
				UserInfoBean _friend = userInfoModel.getFriendsInfo().get(i);

				// check it if is or not be selected
				if (selectedInviteesIds.contains(_friend.getUserId())) {
					// set the friend be selected
					withinGroupCompeteInviteeListView.setItemChecked(i, true);
					withinGroupCompeteInviteeListViewAdapter
							.setFriendSelectState(i, true);
				}
			}
		}

		// set its on item click listener
		withinGroupCompeteInviteeListView
				.setOnItemClickListener(new WithinGroupCompeteInviteeOnItemClickListener());

		// get within group compete invitee select selected invitees horizontal
		// listView
		SSHorizontalListView _selectedInviteesHorizontalListView = (SSHorizontalListView) findViewById(R.id.wigciis_selectedInvitees_horizontalListView);

		// set its adapter
		_selectedInviteesHorizontalListView
				.setAdapter(new SelectedInviteesHorizontalListViewAdapter(
						this,
						selectedInviteeList,
						R.layout.selectedinvitee_horizontallistview_item_layout,
						new String[] { SelectedInviteesHorizontalListViewAdapterKey.SELECTEDINVITEE_AVATAR_KEY
								.name() },
						new int[] { R.id.wigcis_selectedInviteeAvatar_imageView }));

		// set its on item click listener
		_selectedInviteesHorizontalListView
				.setOnItemClickListener(new SelectedInviteesOnItemClickListener());

		// get within group compete invitee select confirm button
		inviteeSelectConfirmBtn = (Button) findViewById(R.id.wigciis_selectedInvitees_confirm_button);

		// set its on click listener
		inviteeSelectConfirmBtn
				.setOnClickListener(new WithinGroupCompeteInviteeSelectConfirmBtnOnClickListener());

		// update within group compete invitee select confirm button
		updateWithinGroupCompeteInviteeSelectConfirmBtn();
	}

	/**
	 * @title updateWithinGroupCompeteInviteeSelectConfirmBtn
	 * @descriptor update within group compete invitee select confirm button
	 * @author Ares
	 */
	private void updateWithinGroupCompeteInviteeSelectConfirmBtn() {
		// check the checked within group compete invitee listView item count
		switch (withinGroupCompeteInviteeListView.getCheckedItemCount()) {
		case 0:
			// disable within group compete invitee select confirm button and
			// set its text
			inviteeSelectConfirmBtn.setEnabled(false);
			inviteeSelectConfirmBtn
					.setText(R.string.withinGroupCompete_inviteeSelect_confirm_button_disableText);
			break;

		default:
			// enable within group compete invitee select confirm button and set
			// its text
			inviteeSelectConfirmBtn.setEnabled(true);
			inviteeSelectConfirmBtn
					.setText(String
							.format(getString(R.string.withinGroupCompete_inviteeSelect_confirm_button_text_format),
									withinGroupCompeteInviteeListView
											.getCheckedItemCount()));
			break;
		}
	}

	// inner class
	/**
	 * @name WithinGroupCompeteInviteeSelectExtraData
	 * @descriptor within group compete invitee select extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class WithinGroupCompeteInviteeSelectExtraData {

		// the selected within group compete invitee id list
		public static final String WIGCIS_SELECTEDINVITEES_ID = "withinGroupCompeteInviteeSelect_selectedInviteeId_list";

	}

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

	/**
	 * @name WithinGroupCompeteInviteeOnItemClickListener
	 * @descriptor within group compete invitee select item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class WithinGroupCompeteInviteeOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// set the friend be selected
			withinGroupCompeteInviteeListViewAdapter.setFriendSelectState(
					position, withinGroupCompeteInviteeListView
							.getCheckedItemPositions().get(position));

			// update within group compete invitee select confirm button
			updateWithinGroupCompeteInviteeSelectConfirmBtn();
		}

	}

	/**
	 * @name SelectedInviteesHorizontalListViewAdapter
	 * @descriptor selected invitees horizontal listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class SelectedInviteesHorizontalListViewAdapter extends
			SimpleAdapter {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				SelectedInviteesHorizontalListViewAdapter.class);

		// selected invitees horizontal listView adapter data list
		private static List<Map<String, Object>> _sDataList;

		/**
		 * @title SelectedInviteesHorizontalListViewAdapter
		 * @descriptor selected invitees horizontal listView adapter constructor
		 *             with context, selected invitee list, content view
		 *             resource, content view subview data key, and content view
		 *             subview id
		 * @param context
		 *            : context
		 * @param selectedInvitees
		 *            : selected invitee list
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public SelectedInviteesHorizontalListViewAdapter(Context context,
				List<UserInfoBean> selectedInvitees, int resource,
				String[] dataKeys, int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// check the selected invitee list
			if (null != selectedInvitees) {
				// traversal the selected within group compete invitees
				for (UserInfoBean _selectedInvitee : selectedInvitees) {
					// define the selected invitees horizontal listView adapter
					// data
					Map<String, Object> _data = new HashMap<String, Object>();

					// set data attributes
					_data.put(
							SelectedInviteesHorizontalListViewAdapterKey.SELECTEDINVITEE_AVATAR_KEY
									.name(), R.drawable.img_default_avatar);

					// add data to list
					_sDataList.add(_data);
				}

				// notify data set changed
				notifyDataSetChanged();
			} else {
				LOGGER.warning("There is no selected invitees for within group compete invitee selecting");
			}
		}

		// inner class
		/**
		 * @name SelectedInviteesHorizontalListViewAdapterKey
		 * @descriptor selected invitees horizontal listView adapter key
		 *             enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum SelectedInviteesHorizontalListViewAdapterKey {

			// selected invitee avatar key
			SELECTEDINVITEE_AVATAR_KEY;

		}

	}

	/**
	 * @name SelectedInviteesOnItemClickListener
	 * @descriptor within group compete invitee select selected invitees item on
	 *             click listener
	 * @author Ares
	 * @version 1.0
	 */
	class SelectedInviteesOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			LOGGER.info("SelectedInviteesOnItemClickListener - onItemClick, parent = "
					+ parent
					+ ", view = "
					+ view
					+ ", position = "
					+ position
					+ " and id = " + id);

			//
		}

	}

	/**
	 * @name WithinGroupCompeteInviteeSelectConfirmBtnOnClickListener
	 * @descriptor within group compete invitee select confirm button on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class WithinGroupCompeteInviteeSelectConfirmBtnOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			LOGGER.info("WithinGroupCompeteInviteeSelectConfirmBtnOnClickListener");

			for (int i = 0; i < withinGroupCompeteInviteeListView.getAdapter()
					.getCount(); i++) {
				if (withinGroupCompeteInviteeListView.getCheckedItemPositions()
						.get(i)) {
					LOGGER.info("@@, iii = " + i);
				}
			}

			//
		}

	}

}
