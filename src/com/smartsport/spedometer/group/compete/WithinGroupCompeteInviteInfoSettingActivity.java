/**
 * 
 */
package com.smartsport.spedometer.group.compete;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.group.GroupInviteInfoAttr4Setting;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
import com.smartsport.spedometer.group.GroupInviteInfoItemEditorActivity;
import com.smartsport.spedometer.group.GroupInviteInfoItemEditorActivity.GroupInviteInfoItemEditorExtraData;
import com.smartsport.spedometer.group.GroupInviteInfoListViewAdapter;
import com.smartsport.spedometer.group.GroupInviteInfoListViewAdapter.GroupInviteInfo4SettingListViewAdapterKey;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.group.compete.WithinGroupCompeteInviteInfoSettingActivity.WithinGroupCompeteInviteAttendeeOperateGridViewAdapters.OperateGridViewAdapterKey;
import com.smartsport.spedometer.group.compete.WithinGroupCompeteInviteeSelectActivity.WithinGroupCompeteInviteeSelectExtraData;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.ISSBaseActivityResult;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WithinGroupCompeteInviteInfoSettingActivity
 * @descriptor smartsport within group compete invite info setting activity
 * @author Ares
 * @version 1.0
 */
public class WithinGroupCompeteInviteInfoSettingActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			WithinGroupCompeteInviteInfoSettingActivity.class);

	// within group compete model
	private WithinGroupCompeteModel withinGroupCompeteModel;

	// the selected within group compete invitees bean list
	private List<UserInfoBean> selectedWithinGroupCompeteInvitees;

	// within group compete invite info listView adapter
	private GroupInviteInfoListViewAdapter withinGroupCompeteInviteInfoListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize within group compete model
		withinGroupCompeteModel = new WithinGroupCompeteModel();

		// initialize within group compete invitees list
		selectedWithinGroupCompeteInvitees = new ArrayList<UserInfoBean>();

		// test by ares
		for (int i = 0; i < 3; i++) {
			UserInfoBean _competeInvitee = new UserInfoBean();
			_competeInvitee.setUserId(10020 + i);
			_competeInvitee.setAvatarUrl("/img/avatar12" + i);
			_competeInvitee.setGender(0 == i % 2 ? UserGender.MALE
					: UserGender.FEMALE);
			_competeInvitee.setNickname("参与者" + (i + 1));

			selectedWithinGroupCompeteInvitees.add(_competeInvitee);
		}

		// set content view
		setContentView(R.layout.activity_withingroupcompete_inviteinfo_setting);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
				R.drawable.img_send_walk_withingroupcompete_invite,
				new SendWithinGroupCompeteInviteInfoBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(R.string.withinGroupCompeteInviteInfoSetting_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get within group compete walk invite invitee operation gridView
		GridView _withinGroupCompeteInviteInviteeOperationGridView = (GridView) findViewById(R.id.wigciis_attendeeOperation_gridView);

		// set its adapter
		_withinGroupCompeteInviteInviteeOperationGridView
				.setAdapter(new WithinGroupCompeteInviteAttendeeOperateGridViewAdapters(
						this,
						selectedWithinGroupCompeteInvitees,
						R.layout.withingroupcompete_inviteattendee_operationgridview_item_layout,
						new String[] {
								OperateGridViewAdapterKey.ATTENDEEOPERATION_OPERATE_ICON_KEY
										.name(),
								OperateGridViewAdapterKey.ATTENDEEOPERATION_OPERATE_TIP_KEY
										.name() }, new int[] {
								R.id.wigc_ia_og_operate_icon_imageView,
								R.id.wigc_ia_og_operate_tip_textView }));

		// set its on item click listener
		_withinGroupCompeteInviteInviteeOperationGridView
				.setOnItemClickListener(new WithinGroupCompeteInviteInviteeOperationOnItemClickListener());

		// get within group compete invite info listView
		ListView _withinGroupCompeteInviteInfoListView = (ListView) findViewById(R.id.wigciis_walkInviteInfo_listView);

		// set its adapter
		_withinGroupCompeteInviteInfoListView
				.setAdapter(withinGroupCompeteInviteInfoListViewAdapter = new GroupInviteInfoListViewAdapter(
						this,
						getResources()
								.getStringArray(
										R.array.withinGroupCompete_inviteInfo4Setting_labels),
						R.layout.group_inviteinfo_listview_item_layout,
						new String[] {
								GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_LABEL_KEY
										.name(),
								GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_VALUE_KEY
										.name() }, new int[] {
								R.id.gii_label_textView,
								R.id.gii_value_textView }));

		// set its on item click listener
		_withinGroupCompeteInviteInfoListView
				.setOnItemClickListener(new WithinGroupCompeteInviteInfoOnItemClickListener());
	}

	/**
	 * @title getWithinGroupCompeteInviteeIds
	 * @descriptor get within group compete invitee id list
	 * @return the within group compete invitee id list
	 * @author Ares
	 */
	private List<Integer> getWithinGroupCompeteInviteeIds() {
		// define within group compete invitee id list
		List<Integer> _competeInviteesIdList = new ArrayList<Integer>();

		// traversal selected within group compete invitees
		for (UserInfoBean _selectedCompeteInvitee : selectedWithinGroupCompeteInvitees) {
			_competeInviteesIdList.add(_selectedCompeteInvitee.getUserId());
		}

		return _competeInviteesIdList;
	}

	// inner class
	/**
	 * @name WithinGroupCompeteInviteInfoSettingRequestCode
	 * @descriptor within group compete invite info setting request code
	 * @author Ares
	 * @version 1.0
	 */
	class WithinGroupCompeteInviteInfoSettingRequestCode {

		// within group compete select invite invitee and invite info item
		// editor request code
		private static final int WIGCIIS_SELECT_WITHINGROUPCOMPETEINVITEINVITEE_REQCODE = 5000;
		private static final int WIGCIIS_WITHINGROUPCOMPETEINVITEINFOITEM_EDITOR_REQCODE = 5010;

	}

	/**
	 * @name WithinGroupCompeteInviteInfoSettingExtraData
	 * @descriptor within group compete invite info setting extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class WithinGroupCompeteInviteInfoSettingExtraData {

		// selected within group compete invite invitees info bean list
		public static final String WIGCIIS_SELECTED_INVITEES_BEAN_LIST = "withinGroupCompeteInviteInfoSetting_selected_withinGroupCompeteInviteInvitees_bean_list";

		// within group compete invite info setting, editor invite info
		// attribute and editor info value
		public static final String WIGCIIS_EI_ATTRIBUTE = "withinGroupCompeteInviteInfoSetting_inviteInfo_attribute";
		public static final String WIGCIIS_EI_VALUE = "withinGroupCompeteInviteInfoSetting_editorInfo_value";

	}

	/**
	 * @name WIGCIISSelectInviteInviteeOnActivityResult
	 * @descriptor select within group compete invite invitee on activity result
	 * @author Ares
	 * @version 1.0
	 */
	class WIGCIISSelectInviteInviteeOnActivityResult implements
			ISSBaseActivityResult {

		@Override
		public void onActivityResult(int resultCode, Intent data) {
			// check the result code
			if (RESULT_OK == resultCode) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					//
				}
			}
		}

	}

	/**
	 * @name WIGCIISInviteInfoEditorOnActivityResult
	 * @descriptor within group compete invite info setting invite info editor
	 *             on activity result
	 * @author Ares
	 * @version 1.0
	 */
	class WIGCIISInviteInfoEditorOnActivityResult implements
			ISSBaseActivityResult {

		@Override
		public void onActivityResult(int resultCode, Intent data) {
			// check the result code
			if (RESULT_OK == resultCode) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					// get and check editor within group compete invite info
					// attribute and value
					GroupInviteInfoAttr4Setting _editorAttr = (GroupInviteInfoAttr4Setting) _extraData
							.getSerializable(WithinGroupCompeteInviteInfoSettingExtraData.WIGCIIS_EI_ATTRIBUTE);
					String _editorValue = _extraData
							.getString(WithinGroupCompeteInviteInfoSettingExtraData.WIGCIIS_EI_VALUE);

					// update editor within group compete invite info value
					withinGroupCompeteInviteInfoListViewAdapter.setData(
							_editorAttr, _editorValue);
				}
			}
		}

	}

	/**
	 * @name SendWithinGroupCompeteInviteInfoBarBtnItemOnClickListener
	 * @descriptor send within group compete invite info bar button item on
	 *             click listener
	 * @author Ares
	 * @version 1.0
	 */
	class SendWithinGroupCompeteInviteInfoBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// get and check within group compete invite topic
			String _topic = withinGroupCompeteInviteInfoListViewAdapter
					.getGroupTopic();
			if (null == _topic || "".equalsIgnoreCase(_topic)) {
				LOGGER.error("Within group compete invite topic is null");

				// show within group compete invite topic is null toast
				Toast.makeText(
						WithinGroupCompeteInviteInfoSettingActivity.this,
						R.string.toast_withinGroupCompete_inviteInfo_topic_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// get and check within group compete duration time
			String _withinGroupCompeteDurationTime = withinGroupCompeteInviteInfoListViewAdapter
					.getWithinGroupCompeteInviteDurationTime();
			if (null == _withinGroupCompeteDurationTime
					|| "".equalsIgnoreCase(_withinGroupCompeteDurationTime)) {
				// show within group compete invite duration time is null toast
				Toast.makeText(
						WithinGroupCompeteInviteInfoSettingActivity.this,
						R.string.toast_withinGroupCompete_inviteInfo_durationTime_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// check the selected invitees and within group compete duration
			// time
			if (null != selectedWithinGroupCompeteInvitees) {
				try {
					// generate within group compete invite info
					GroupInviteInfoBean _withinGroupCompeteInviteInfo = new GroupInviteInfoBean();
					_withinGroupCompeteInviteInfo.setTopic(_topic);
					_withinGroupCompeteInviteInfo.setDuration(Long
							.parseLong(_withinGroupCompeteDurationTime));

					// send within group compete invite info with the selected
					// user friends info list to remote server
					withinGroupCompeteModel.inviteWithinGroupCompete(123123,
							"token", getWithinGroupCompeteInviteeIds(),
							_withinGroupCompeteInviteInfo, new ICMConnector() {

								//

							});
				} catch (NumberFormatException e) {
					LOGGER.error("Generate within group compete invite info with schedule duration time error, within group compete invite schedule duration time = "
							+ _withinGroupCompeteDurationTime);

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @name WithinGroupCompeteInviteAttendeeOperateGridViewAdapters
	 * @descriptor within group compete invite attendee operate gridView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class WithinGroupCompeteInviteAttendeeOperateGridViewAdapters extends
			SimpleAdapter {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				WithinGroupCompeteInviteAttendeeOperateGridViewAdapters.class);

		// within group compete invite attendee operate gridView adapter data
		// list
		private static List<Map<String, Object>> _sDataList;

		/**
		 * @title WithinGroupCompeteInviteAttendeeOperateGridViewAdapters
		 * @descriptor within group compete invite attendee operate gridView
		 *             adapter constructor with context, invitee list, content
		 *             view resource, content view subview data key, and content
		 *             view subview id
		 * @param context
		 *            : context
		 * @param competeInvitees
		 *            : within group compete invite invitee list
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public WithinGroupCompeteInviteAttendeeOperateGridViewAdapters(
				Context context, List<UserInfoBean> competeInvitees,
				int resource, String[] dataKeys, int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// define within group compete invite attendee operate gridView
			// adapter data for inviter
			Map<String, Object> _inviterData = new HashMap<String, Object>();

			// set data attributes
			_inviterData
					.put(OperateGridViewAdapterKey.ATTENDEEOPERATION_OPERATE_ICON_KEY
							.name(), null);
			_inviterData
					.put(OperateGridViewAdapterKey.ATTENDEEOPERATION_OPERATE_TIP_KEY
							.name(),
							context.getString(R.string.withinGroupCompeteInvite_inviterNickname));

			// add inviter data to list
			_sDataList.add(_inviterData);

			// check within group compete invite invitee list and add to list
			if (null != competeInvitees) {
				// get within group compete invite invitee list size
				int _competeInviteeListSize = competeInvitees.size();
				int _competeInviteeLimitCount = context
						.getResources()
						.getInteger(
								R.integer.config_limitCount_withinGroupCompeteInvitees);

				for (UserInfoBean _competeInvitee : competeInvitees
						.subList(
								0,
								_competeInviteeLimitCount > _competeInviteeListSize ? _competeInviteeListSize
										: _competeInviteeLimitCount)) {
					// define within group compete invite attendee operate
					// gridView adapter data for invitee
					Map<String, Object> _inviteeData = new HashMap<String, Object>();

					// set data attributes
					_inviteeData
							.put(OperateGridViewAdapterKey.ATTENDEEOPERATION_OPERATE_ICON_KEY
									.name(),
									R.drawable.img_default_middle_avatar);
					_inviteeData
							.put(OperateGridViewAdapterKey.ATTENDEEOPERATION_OPERATE_TIP_KEY
									.name(), _competeInvitee.getNickname());

					// add inviter data to list
					_sDataList.add(_inviteeData);
				}
			} else {
				LOGGER.warning("No within group compete invite invitee was selected");
			}

			// define within group compete invite attendee operate gridView
			// adapter data for operate
			Map<String, Object> _operateData = new HashMap<String, Object>();

			// set data attributes
			_operateData
					.put(OperateGridViewAdapterKey.ATTENDEEOPERATION_OPERATE_ICON_KEY
							.name(),
							R.drawable.img_withingroupcompete_inviteattendee_operate);
			_operateData.put(
					OperateGridViewAdapterKey.ATTENDEEOPERATION_OPERATE_TIP_KEY
							.name(), null);

			// add inviter data to list
			_sDataList.add(_operateData);

			// notify data set changed
			notifyDataSetChanged();
		}

		// inner class
		/**
		 * @name OperateGridViewAdapterKey
		 * @descriptor within group compete invite attendee operate gridView
		 *             adapter key enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum OperateGridViewAdapterKey {

			// within group compete invite attendee operate icon and tip key
			ATTENDEEOPERATION_OPERATE_ICON_KEY, ATTENDEEOPERATION_OPERATE_TIP_KEY;

		}

	}

	/**
	 * @name WithinGroupCompeteInviteInviteeOperationOnItemClickListener
	 * @descriptor within group compete invite invitee operation item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class WithinGroupCompeteInviteInviteeOperationOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// check the position and process the within group compete invite
			// invitee operation item
			if (parent.getAdapter().getCount() - 1 == position) {
				// define within group compete invitee select extra data map
				Map<String, Object> _extraMap = new HashMap<String, Object>();

				// put selected within group compete invitee id list to extra
				// data map as param
				_extraMap
						.put(WithinGroupCompeteInviteeSelectExtraData.WIGCIS_SELECTEDINVITEES_ID,
								getWithinGroupCompeteInviteeIds());

				// go to within group compete invitee select activity with extra
				// data map
				presentActivityForResult(
						WithinGroupCompeteInviteeSelectActivity.class,
						_extraMap,
						WithinGroupCompeteInviteInfoSettingRequestCode.WIGCIIS_SELECT_WITHINGROUPCOMPETEINVITEINVITEE_REQCODE,
						new WIGCIISSelectInviteInviteeOnActivityResult());
			}
		}

	}

	/**
	 * @name WithinGroupCompeteInviteInfoOnItemClickListener
	 * @descriptor within group compete invite info item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class WithinGroupCompeteInviteInfoOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// define within group compete invite info item editor extra data
			// map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put the editor group type to extra data map as param
			_extraMap.put(
					GroupInviteInfoItemEditorExtraData.GII_EDITORGROUPTYPE,
					GroupType.COMPETE_GROUP);

			// put editor within group compete invite info attribute, name and
			// info to extra data map as param
			_extraMap.put(GroupInviteInfoItemEditorExtraData.GII_EI_ATTRIBUTE,
					withinGroupCompeteInviteInfoListViewAdapter
							.getItemAttribute(position));
			_extraMap.put(GroupInviteInfoItemEditorExtraData.GII_EI_NAME,
					withinGroupCompeteInviteInfoListViewAdapter
							.getItemLabel(position));
			_extraMap.put(GroupInviteInfoItemEditorExtraData.GII_EI_VALUE,
					withinGroupCompeteInviteInfoListViewAdapter
							.getItemValue(position));

			// go to group invite info item editor activity with extra data map
			pushActivityForResult(
					GroupInviteInfoItemEditorActivity.class,
					_extraMap,
					WithinGroupCompeteInviteInfoSettingRequestCode.WIGCIIS_WITHINGROUPCOMPETEINVITEINFOITEM_EDITOR_REQCODE,
					new WIGCIISInviteInfoEditorOnActivityResult());
		}

	}

}
