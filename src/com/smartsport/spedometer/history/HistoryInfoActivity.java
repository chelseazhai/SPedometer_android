/**
 * 
 */
package com.smartsport.spedometer.history;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatModel;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HistoryInfoActivity
 * @descriptor smartsport history info activity
 * @author Ares
 * @version 1.0
 */
public class HistoryInfoActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HistoryInfoActivity.class);

	// pedometer login user
	private UserPedometerExtBean loginUser = (UserPedometerExtBean) UserManager
			.getInstance().getLoginUser();

	// group info and stranger pat model
	private GroupInfoModel groupInfoModel = GroupInfoModel.getInstance();
	private StrangerPatModel strangerPatModel = StrangerPatModel.getInstance();

	// user personal pedometer walk info, walk invite history group, within
	// group compete history group and pat stranger list view
	private View personalPedometerWalkInfoListView;
	private View walkInviteHistoryGroupListView;
	private View withinGroupCompeteHistoryGroupListView;
	private View patStrangerListView;

	// visible history info view
	private View visibleHistoryInfoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.activity_history_info);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(R.string.historyInfo_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get user history info segment redioGroup
		RadioGroup _userHistoryInfoSegRadioGroup = (RadioGroup) findViewById(R.id.hi_user_historyInfo_segment_radioGroup);

		// set its on checked change listener
		_userHistoryInfoSegRadioGroup
				.setOnCheckedChangeListener(new UserHistoryInfoSegRadioGroupOnCheckedChangeListener());

		// update the user selected history info(personal pedometer, walk invite
		// group, within group compete group and pat stranger)
		updateUserHistoryInfo(_userHistoryInfoSegRadioGroup
				.getCheckedRadioButtonId());
	}

	/**
	 * @title updateUserHistoryInfo
	 * @descriptor update the user history info(personal pedometer, walk invite
	 *             group, within group compete group and pat stranger)
	 * @param checkedSegmentId
	 *            : checked user history info segment radioButton id
	 * @author Ares
	 */
	private void updateUserHistoryInfo(int checkedSegmentId) {
		// check the checked attendees walk trend segment radioButton id
		switch (checkedSegmentId) {
		case R.id.hi_personalPedometer_historyInfo_segment:
			// check, get personal pedometer walk info list view and then show
			// it
			if (null == personalPedometerWalkInfoListView) {
				personalPedometerWalkInfoListView = ((ViewStub) findViewById(R.id.hi_personalPedometer_historyInfo_viewStub))
						.inflate();

				//
			} else {
				personalPedometerWalkInfoListView.setVisibility(View.VISIBLE);
			}

			//

			// hide visible history info view if needed
			if (null != visibleHistoryInfoView) {
				visibleHistoryInfoView.setVisibility(View.GONE);
			}

			// update visible history info view
			visibleHistoryInfoView = personalPedometerWalkInfoListView;
			break;

		case R.id.hi_walkInvite_historyGroup_segment:
			// check, get walk invite history group list view and then show it
			if (null == walkInviteHistoryGroupListView) {
				walkInviteHistoryGroupListView = ((ViewStub) findViewById(R.id.hi_walkInvite_historyGroup_viewStub))
						.inflate();

				//

				// get walk invite history groups
				groupInfoModel.getUserHistoryGroups(loginUser.getUserId(),
						loginUser.getUserKey(), new ICMConnector() {

							@Override
							public void onSuccess(Object... retValue) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								// TODO Auto-generated method stub

							}

						});
			} else {
				walkInviteHistoryGroupListView.setVisibility(View.VISIBLE);
			}

			// hide visible history info view and if needed
			if (null != visibleHistoryInfoView) {
				visibleHistoryInfoView.setVisibility(View.GONE);
			}

			// update visible history info view
			visibleHistoryInfoView = walkInviteHistoryGroupListView;
			break;

		case R.id.hi_withinGroupCompete_historyGroup_segment:
			// check, get within group compete list view and then show it
			if (null == withinGroupCompeteHistoryGroupListView) {
				withinGroupCompeteHistoryGroupListView = ((ViewStub) findViewById(R.id.hi_withinGroupCompete_historyGroup_viewStub))
						.inflate();
			} else {
				withinGroupCompeteHistoryGroupListView
						.setVisibility(View.VISIBLE);
			}

			// update it and then hide visible history info view and if needed
			//
			if (null != visibleHistoryInfoView) {
				visibleHistoryInfoView.setVisibility(View.GONE);
			}

			// update visible history info view
			visibleHistoryInfoView = withinGroupCompeteHistoryGroupListView;
			break;

		case R.id.hi_strangerPat_historyPatStranger_segment:
			// check, get pat stranger list view and then show it
			if (null == patStrangerListView) {
				patStrangerListView = ((ViewStub) findViewById(R.id.hi_strangerPat_historyPatStranger_viewStub))
						.inflate();

				//

				// get pat strangers
				strangerPatModel.getPatStrangers(loginUser.getUserId(),
						loginUser.getUserKey(), new ICMConnector() {

							@Override
							public void onSuccess(Object... retValue) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								// TODO Auto-generated method stub

							}

						});
			} else {
				patStrangerListView.setVisibility(View.VISIBLE);
			}

			// hide visible history info view if needed
			if (null != visibleHistoryInfoView) {
				visibleHistoryInfoView.setVisibility(View.GONE);
			}

			// update visible history info view
			visibleHistoryInfoView = patStrangerListView;
			break;
		}
	}

	// inner class
	/**
	 * @name UserHistoryInfoSegRadioGroupOnCheckedChangeListener
	 * @descriptor user history info segment radioGroup on checked change
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class UserHistoryInfoSegRadioGroupOnCheckedChangeListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// update the user selected history info(personal pedometer, walk
			// invite group, within group compete group and pat stranger)
			updateUserHistoryInfo(checkedId);
		}

	}

}
