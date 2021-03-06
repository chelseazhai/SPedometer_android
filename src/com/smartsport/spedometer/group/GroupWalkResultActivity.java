/**
 * 
 */
package com.smartsport.spedometer.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavBarButtonItem;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.group.ScheduleWalkInviteGroupsActivity.WalkInviteInviteeSelectOrWalkControlExtraData;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
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

	// group info model
	private GroupInfoModel groupInfoModel = GroupInfoModel.getInstance();

	// group type(walk or within group compete), walk start, stop time and
	// attendees walk result
	private GroupType groupType;
	private long walkStartTime;
	private long walkStopTime;
	private List<UserInfoGroupResultBean> attendeesWalkResult;

	// group walk result progress dialog
	private SSProgressDialog groupWalkResultProgDlg;

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

	@Override
	protected void onBackBarButtonItemClick(SSBNavBarButtonItem backBarBtnItem) {
		// define walk invite walk result extra data map
		Map<String, Object> _extraMap = new HashMap<String, Object>();

		// put walk invite walk stop control flag to extra data map as param
		_extraMap
				.put(WalkInviteInviteeSelectOrWalkControlExtraData.WIWC_WALK_STARTORSTOP_FLAG,
						Boolean.valueOf(true));

		// pop the activity with result code and extra map
		popActivityWithResult(RESULT_OK, _extraMap);
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
			// show share walk invite or within group compete walk result
			// progress dialog
			groupWalkResultProgDlg = SSProgressDialog.show(
					GroupWalkResultActivity.this,
					R.string.procMsg_shareGroupWalkResult);

			// get pedometer login user
			UserPedometerExtBean _loginUser = (UserPedometerExtBean) UserManager
					.getInstance().getLoginUser();

			// test by ares
			// share walk invite or within group compete walk result to user
			// moments
			groupInfoModel.shareGroupResult2Moments(_loginUser.getUserId(),
					_loginUser.getUserKey(), BitmapFactory.decodeResource(
							getResources(), R.drawable.img_test_walkresult),
					"哈哈， 我厉害吧...", "约走结果", new ArrayList<Long>(),
					new ICMConnector() {

						@Override
						public void onSuccess(Object... retValue) {
							// dismiss share the walk invite or within group
							// compete walk result progress dialog
							groupWalkResultProgDlg.dismiss();

							// show share walk result toast
							Toast.makeText(
									GroupWalkResultActivity.this,
									null == groupType ? "分享失败"
											: GroupType.WALK_GROUP == groupType ? "分享约走结果成功"
													: "分享多人组内竞赛结果成功",
									Toast.LENGTH_LONG).show();

							// pop walk invite or within group compete walk
							// result activity
							popActivityWithResult();
						}

						@Override
						public void onFailure(int errorCode, String errorMsg) {
							LOGGER.error("Share the walk invite or within group compete walk result error, error code = "
									+ errorCode + " and message = " + errorMsg);

							// dismiss share the walk invite or within group
							// compete walk result progress dialog
							groupWalkResultProgDlg.dismiss();

							// check error code and process hopeRun business
							// error
							if (errorCode < 100) {
								// show error message toast
								Toast.makeText(GroupWalkResultActivity.this,
										errorMsg, Toast.LENGTH_SHORT).show();

								// test by ares
								//
							}
						}

					});
		}

	}

}
