/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.HashMap;
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
import com.smartsport.spedometer.group.ScheduleWalkInviteGroupsActivity.WalkInviteInviteeSelectExtraData;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserInfoBean;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		_walkInviteInviteeListView.setAdapter(null);

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
	 * @descriptor walk invite invitee item on click listener
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
					.put(WalkInviteInviteeSelectExtraData.WIIS_SELECTED_INVITEE_BEAN,
							new UserInfoBean());

			// dismiss the activity with result code and extra map
			dismissActivityWithResult(RESULT_OK, _extraMap);
		}

	}

}
