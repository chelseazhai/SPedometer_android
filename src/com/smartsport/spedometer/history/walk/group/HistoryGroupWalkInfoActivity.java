/**
 * 
 */
package com.smartsport.spedometer.history.walk.group;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.group.compete.WithinGroupCompeteModel;
import com.smartsport.spedometer.group.info.HistoryGroupInfoBean;
import com.smartsport.spedometer.group.walk.WalkInviteModel;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HistoryGroupWalkInfoActivity
 * @descriptor smartsport walk invite or within group compete history group walk
 *             info activity
 * @author Ares
 * @version 1.0
 */
public class HistoryGroupWalkInfoActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HistoryGroupWalkInfoActivity.class);

	// pedometer login user
	private UserPedometerExtBean loginUser = (UserPedometerExtBean) UserManager
			.getInstance().getLoginUser();

	// group info, walk invite and within group compete model
	private GroupInfoModel groupInfoModel = GroupInfoModel.getInstance();
	private WalkInviteModel walkInviteModel = WalkInviteModel.getInstance();
	private WithinGroupCompeteModel withinGroupCompeteModel = WithinGroupCompeteModel
			.getInstance();

	// the history group id, type, topic, walk start and stop time
	private String historyGroupId;
	private GroupType historyGroupType;
	private String historyGroupTopic;
	private Long historyGroupWalkStartTime;
	private Long historyGroupWalkStopTime;

	// history group info progress dialog
	private SSProgressDialog historyGroupInfoProgDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the history group id, type, topic, walk start and stop time
			historyGroupId = _extraData
					.getString(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_ID);
			historyGroupType = (GroupType) _extraData
					.getSerializable(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_TYPE);
			historyGroupTopic = _extraData
					.getString(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_TOPIC);
			historyGroupWalkStartTime = _extraData
					.getLong(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_WALKSTARTTIME);
			historyGroupWalkStopTime = _extraData
					.getLong(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_WALKSTOPTIME);
		}

		// set content view
		setContentView(R.layout.activity_historygroup_walkinfo);

		// check the history group id and then get its walk info from remote
		// server
		if (null != historyGroupId) {
			// show get history group walk info progress dialog
			historyGroupInfoProgDlg = SSProgressDialog
					.show(this,
							historyGroupType == GroupType.WALK_GROUP ? R.string.procMsg_getWalkInviteHistoryGroupWalkInfo
									: R.string.procMsg_getWithinGroupCompeteHistoryGroupWalkInfo);

			groupInfoModel.getUserHistoryGroupInfo(loginUser.getUserId(),
					loginUser.getUserKey(), historyGroupId, new ICMConnector() {

						@Override
						public void onSuccess(Object... retValue) {
							// dismiss get history group walk info progress
							// dialog
							historyGroupInfoProgDlg.dismiss();

							// check return values
							if (null != retValue
									&& 0 < retValue.length
									&& retValue[retValue.length - 1] instanceof HistoryGroupInfoBean) {
								// get the history group info
								HistoryGroupInfoBean _historyGroupInfo = (HistoryGroupInfoBean) retValue[retValue.length - 1];

								LOGGER.info("The history group, type = "
										+ historyGroupType + " info = "
										+ _historyGroupInfo);

								//
							} else {
								LOGGER.error("Update history group, type = "
										+ historyGroupType
										+ " invitee walk info UI error");
							}
						}

						@Override
						public void onFailure(int errorCode, String errorMsg) {
							LOGGER.error("Get history group, type = "
									+ historyGroupType
									+ " walk info from remote server error, error code = "
									+ errorCode + " and message = " + errorMsg);

							// dismiss get history group walk info progress
							// dialog
							historyGroupInfoProgDlg.dismiss();

							// check error code and process hopeRun business
							// error
							if (errorCode < 100) {
								// show error message toast
								Toast.makeText(
										HistoryGroupWalkInfoActivity.this,
										errorMsg, Toast.LENGTH_SHORT).show();

								// test by ares
								//
							}
						}

					});
		}
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(null != historyGroupTopic ? historyGroupTopic : "");
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// TODO Auto-generated method stub

	}

	// inner class
	/**
	 * @name HistoryGroupWalkInfoExtraData
	 * @descriptor walk invite or within group compete history group walk info
	 *             extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class HistoryGroupWalkInfoExtraData {

		// the history group id, type, topic, walk start and stop time
		public static final String HGWI_HISTORYGROUP_ID = "historyGroupWalkInfo_historyGroup_id";
		public static final String HGWI_HISTORYGROUP_TYPE = "historyGroupWalkInfo_historyGroup_type";
		public static final String HGWI_HISTORYGROUP_TOPIC = "historyGroupWalkInfo_historyGroup_topic";
		public static final String HGWI_HISTORYGROUP_WALKSTARTTIME = "historyGroupWalkInfo_historyGroup_walkStartTime";
		public static final String HGWI_HISTORYGROUP_WALKSTOPTIME = "historyGroupWalkInfo_historyGroup_walkStopTime";

	}

}
