/**
 * 
 */
package com.smartsport.spedometer.group;

import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.group.ScheduleWalkInviteGroupsActivity.ScheduleWalkInviteGroupListViewAdapter.ScheduleWalkInviteGroupListViewAdapterKey;
import com.smartsport.spedometer.group.walk.WalkInviteInviteeSelectActivity;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.ISSBaseActivityResult;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name ScheduleWalkInviteGroupsActivity
 * @descriptor smartsport schedule walk invite group list activity
 * @author Ares
 * @version 1.0
 */
public class ScheduleWalkInviteGroupsActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			ScheduleWalkInviteGroupsActivity.class);

	// group info model
	private GroupInfoModel groupInfoModel;

	// schedule walk invite group list
	private List<GroupBean> scheduleWalkInviteGroupList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize group info model and schedule walk invite group list
		groupInfoModel = new GroupInfoModel();
		scheduleWalkInviteGroupList = new ArrayList<GroupBean>();

		// test by ares
		for (int i = 0; i < 10; i++) {
			//
		}

		// set content view
		setContentView(R.layout.activity_schedule_walkinvite_groups);

		// get schedule walk invite group list from remote server
		groupInfoModel.getUserScheduleGroups(123123, "token",
				GroupType.WALK_GROUP, new ICMConnector() {

					//

				});
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
				R.drawable.img_new_walkinvite_group,
				new NewWalkInviteGroupBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(R.string.scheduleWalkInviteGroups_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get schedule walk invite group listView
		ListView _scheduleWalkInviteGroupListView = (ListView) findViewById(R.id.swigs_scheduleGroups_listView);

		// set its adapter
		_scheduleWalkInviteGroupListView
				.setAdapter(new ScheduleWalkInviteGroupListViewAdapter(
						this,
						scheduleWalkInviteGroupList,
						0,
						new String[] {
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_TITLE_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_SCHEDULEBEGINENDTIME_KEY
										.name(),
								ScheduleWalkInviteGroupListViewAdapterKey.WALKINVITEGROUP_STATUS_KEY
										.name() }, new int[] {}));

		// set its on item click listener
		_scheduleWalkInviteGroupListView
				.setOnItemClickListener(new ScheduleWalkInviteGroupOnItemClickListener());
	}

	// inner class
	/**
	 * @name ScheduleWalkInviteGroupsRequestCode
	 * @descriptor schedule walk invite groups request code
	 * @author Ares
	 * @version 1.0
	 */
	class ScheduleWalkInviteGroupsRequestCode {

		// select walk invite invitee request code
		private static final int SWIG_SELECT_WALKINVITEINVITEE_REQCODE = 3000;

	}

	/**
	 * @name WalkInviteInviteeSelectExtraData
	 * @descriptor walk invite invitee select extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class WalkInviteInviteeSelectExtraData {

		// selected walk invite invitee info bean
		public static final String WIIS_SELECTED_INVITEE_BEAN = "walkInviteInviteeSelect_selected_walkInviteInvitee_bean";

	}

	/**
	 * @name WIISSelectInviteeOnActivityResult
	 * @descriptor walk invite invitee select select invitee on activity result
	 * @author Ares
	 * @version 1.0
	 */
	class WIISSelectInviteeOnActivityResult implements ISSBaseActivityResult {

		@Override
		public void onActivityResult(int resultCode, Intent data) {
			// check the result code
			if (RESULT_OK == resultCode) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					// get and check the selected walk invite invitee bean
					UserInfoBean _selectedInviteeBean = (UserInfoBean) _extraData
							.getSerializable(WalkInviteInviteeSelectExtraData.WIIS_SELECTED_INVITEE_BEAN);
					if (null != _selectedInviteeBean) {
						LOGGER.info("@@@, _selectedInviteeBean = "
								+ _selectedInviteeBean);

						//
					} else {
						LOGGER.error("");
					}
				} else {
					LOGGER.error("Select walk invite invitee error, the return extra data is null");
				}
			}
		}

	}

	/**
	 * @name NewWalkInviteGroupBarBtnItemOnClickListener
	 * @descriptor new schedule walk invite group bar button item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class NewWalkInviteGroupBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// go to walk invite invitee select activity
			presentActivityForResult(
					WalkInviteInviteeSelectActivity.class,
					null,
					ScheduleWalkInviteGroupsRequestCode.SWIG_SELECT_WALKINVITEINVITEE_REQCODE,
					new WIISSelectInviteeOnActivityResult());
		}

	}

	/**
	 * @name ScheduleWalkInviteGroupListViewAdapter
	 * @descriptor schedule walk invite group listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class ScheduleWalkInviteGroupListViewAdapter extends SimpleAdapter {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				ScheduleWalkInviteGroupListViewAdapter.class);

		// schedule walk invite group begin and end time keys
		private static final String WALKINVITEGROUP_SCHEDULEBEGINTIME_KEY = "";
		private static final String WALKINVITEGROUP_SCHEDULEENDTIME_KEY = "";

		// context
		private Context context;

		// schedule walk invite group listView adapter data list
		private static List<Map<String, Object>> _sDataList;

		/**
		 * @title ScheduleWalkInviteGroupListViewAdapter
		 * @descriptor schedule walk invite group listView adapter constructor
		 *             with context, schedule walk invite group list, content
		 *             view resource, content view subview data key, and content
		 *             view subview id
		 * @param context
		 *            : context
		 * @param scheduleWalkInviteGroups
		 *            : schedule walk invite group list
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public ScheduleWalkInviteGroupListViewAdapter(Context context,
				List<GroupBean> scheduleWalkInviteGroups, int resource,
				String[] dataKeys, int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// save context
			this.context = context;

			//
		}

		// inner class
		/**
		 * @name ScheduleWalkInviteGroupListViewAdapterKey
		 * @descriptor schedule walk invite group listView adapter key
		 *             enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum ScheduleWalkInviteGroupListViewAdapterKey {

			// schedule walk invite group title, schedule begin, end time and
			// status key
			WALKINVITEGROUP_TITLE_KEY, WALKINVITEGROUP_SCHEDULEBEGINENDTIME_KEY, WALKINVITEGROUP_STATUS_KEY;

		}

	}

	/**
	 * @name ScheduleWalkInviteGroupOnItemClickListener
	 * @descriptor schedule walk invite group item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class ScheduleWalkInviteGroupOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

		}

	}

}
