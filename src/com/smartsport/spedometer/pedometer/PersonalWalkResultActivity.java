/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.group.info.result.UserInfoGroupResultBean;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name PersonalWalkResultActivity
 * @descriptor smartsport personal walk result activity
 * @author Ares
 * @version 1.0
 */
public class PersonalWalkResultActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			PersonalWalkResultActivity.class);

	// user walk start, stop time, walk path location point list and walk result
	private long walkStartTime;
	private long walkStopTime;
	private List<LatLonPoint> walkPathLocationPoints;
	private UserInfoGroupResultBean walkResult;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the user walk start, stop time, walk path location points and
			// walk result
			walkStartTime = _extraData
					.getLong(PersonalWalkResultExtraData.PWR_USER_WALK_STARTTIME);
			walkStopTime = _extraData
					.getLong(PersonalWalkResultExtraData.PWR_USER_WALK_STOPTIME);
			walkPathLocationPoints = (List<LatLonPoint>) _extraData
					.getSerializable(PersonalWalkResultExtraData.PWR_USER_WALKPATH_LOCATIONPOINTS);
			walkResult = (UserInfoGroupResultBean) _extraData
					.getSerializable(PersonalWalkResultExtraData.PWR_USER_WALKRESULT);
		}

		// set content view
		setContentView(R.layout.activity_personalpedometer_walkresult);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set left bar button item
		setLeftBarButtonItem(new SSBNavTitleBarButtonItem(this,
				R.string.cancel_share_walkResult_barbtnitem_title,
				new CancelShareWalkResultBarBtnItemOnClickListener()));

		// set right bar button item
		setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
				R.drawable.img_walkresult_share,
				new PersonalWalkResultShareBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(R.string.personal_walkResult_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		//
	}

	// inner class
	/**
	 * @name PersonalWalkResultExtraData
	 * @descriptor personal pedometer user walk result extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class PersonalWalkResultExtraData {

		// personal walk start, stop time, walk path location point list and
		// walk result
		public static final String PWR_USER_WALK_STARTTIME = "personalWalkResult_user_walk_startTime";
		public static final String PWR_USER_WALK_STOPTIME = "personalWalkResult_user_walk_stopTime";
		public static final String PWR_USER_WALKPATH_LOCATIONPOINTS = "personalWalkResult_user_walkPathLocationPoints";
		public static final String PWR_USER_WALKRESULT = "personalWalkResult_user_walkResult";

	}

	/**
	 * @name CancelShareWalkResultBarBtnItemOnClickListener
	 * @descriptor cancel share walk result bar button item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class CancelShareWalkResultBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// dismiss the activity
			dismissActivity();
		}

	}

	/**
	 * @name PersonalWalkResultShareBarBtnItemOnClickListener
	 * @descriptor personal pedometer user walk result bar button item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class PersonalWalkResultShareBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// share the user walk result
			LOGGER.debug("Share the user walk result");

			Toast.makeText(PersonalWalkResultActivity.this, "分享走路计步结果成功",
					Toast.LENGTH_LONG).show();

			// test by ares
			// dismiss personal walk result activity
			dismissActivity();
		}

	}

}
