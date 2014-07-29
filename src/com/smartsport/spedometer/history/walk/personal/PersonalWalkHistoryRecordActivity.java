/**
 * 
 */
package com.smartsport.spedometer.history.walk.personal;

import android.graphics.Color;
import android.os.Bundle;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.pedometer.PersonalWalkRecordBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name PersonalWalkHistoryRecordActivity
 * @descriptor smartsport personal walk history record activity
 * @author Ares
 * @version 1.0
 */
public class PersonalWalkHistoryRecordActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			PersonalWalkHistoryRecordActivity.class);

	// the walk record title and its bean
	private String walkRecordTitle;
	private PersonalWalkRecordBean walkRecordBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the walk record title and its bean
			walkRecordTitle = _extraData
					.getString(PersonalWalkHistoryRecordExtraData.PPWHR_SELECTED_WALKRECORD_TITLE);
			walkRecordBean = (PersonalWalkRecordBean) _extraData
					.getSerializable(PersonalWalkHistoryRecordExtraData.PPWHR_SELECTED_WALKRECORD_BEAN);
		}

		// set content view
		setContentView(R.layout.activity_personal_walk_historyrecord);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(null != walkRecordTitle ? walkRecordTitle : "");
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// TODO Auto-generated method stub

	}

	// inner class
	/**
	 * @name PersonalWalkHistoryRecordExtraData
	 * @descriptor personal walk history record extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class PersonalWalkHistoryRecordExtraData {

		// selected walk record title and its bean
		public static final String PPWHR_SELECTED_WALKRECORD_TITLE = "personalPedometerWalkHistoryRecord_selected_walkRecord_title";
		public static final String PPWHR_SELECTED_WALKRECORD_BEAN = "personalPedometerWalkHistoryRecord_selected_walkRecord_bean";

	}

}
