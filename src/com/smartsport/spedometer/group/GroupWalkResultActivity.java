/**
 * 
 */
package com.smartsport.spedometer.group;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupWalkResultActivity
 * @descriptor smartsport walk invite or within group compete walk result
 *             activity
 * @author Ares
 * @version 1.0
 */
public class GroupWalkResultActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			GroupWalkResultActivity.class);

	// group type(walk or within group compete), walk start, stop time and
	// attendees walk result
	private GroupType groupType;
	private long walkStartTime;
	private long walkStopTime;
	private List<UserInfoGroupResultBean> attendeesWalkResult;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the group type, walk start, stop time and attendees walk
			// result
			groupType = (GroupType) _extraData
					.getSerializable(GroupWalkResultExtraData.GWR_GROUP_TYPE);
			walkStartTime = _extraData
					.getLong(GroupWalkResultExtraData.GWR_GROUP_WALK_STARTTIME);
			walkStopTime = _extraData
					.getLong(GroupWalkResultExtraData.GWR_GROUP_WALK_STOPTIME);
			attendeesWalkResult = (List<UserInfoGroupResultBean>) _extraData
					.getSerializable(GroupWalkResultExtraData.GWR_GROUP_ATTENDEES_WALKRESULT);
		}

		// set content view
		setContentView(R.layout.activity_group_walkresult);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
				R.drawable.img_walkresult_share,
				new GroupWalkResultShareBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(null == groupType ? ""
				: getString(GroupType.WALK_GROUP == groupType ? R.string.walkInvite_walkResult_title
						: R.string.withinGroupCompete_walkResult_title));
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		//
	}

	// inner class
	/**
	 * @name GroupWalkResultExtraData
	 * @descriptor walk invite or within group compete walk result extra data
	 *             constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class GroupWalkResultExtraData {

		// the group type, group walk start, stop time and attendees walk result
		public static final String GWR_GROUP_TYPE = "groupWalkResult_group_type";
		public static final String GWR_GROUP_WALK_STARTTIME = "groupWalkResult_group_walk_startTime";
		public static final String GWR_GROUP_WALK_STOPTIME = "groupWalkResult_group_walk_stopTime";
		public static final String GWR_GROUP_ATTENDEES_WALKRESULT = "groupWalkResult_group_attendees_walkResult";

	}

	/**
	 * @name GroupWalkResultShareBarBtnItemOnClickListener
	 * @descriptor walk invite or within group compete walk result bar button
	 *             item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class GroupWalkResultShareBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// share the walk invite or within group compete walk result
			LOGGER.debug("Share the walk invite or within group compete walk result");

			Toast.makeText(GroupWalkResultActivity.this, "", Toast.LENGTH_LONG)
					.show();

			// // test by ares
			// // define walk invite walk result extra data map
			// Map<String, Object> _extraMap = new HashMap<String, Object>();
			//
			// // put walk invite walk stop control flag to extra data map as
			// param
			// _extraMap
			// .put(WalkInviteInviteeSelectOrWalkControlExtraData.WIWC_WALK_STARTORSTOP_FLAG,
			// Boolean.valueOf(true));
			//
			// // pop the activity with result code and extra map
			// popActivityWithResult(RESULT_OK, _extraMap);
		}

	}

}
