/**
 * 
 */
package com.smartsport.spedometer.history.patstranger;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.LocationBean;
import com.smartsport.spedometer.strangersocial.pat.UserInfoPatLocationExtBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name PatStrangerActivity
 * @descriptor smartsport user pat stranger activity
 * @author Ares
 * @version 1.0
 */
public class PatStrangerActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			PatStrangerActivity.class);

	// the user pat stranger info bean and be patted location list
	private UserInfoPatLocationExtBean strangerInfo;
	private List<LocationBean> patLocationList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the selected user pat stranger info bean
			strangerInfo = (UserInfoPatLocationExtBean) _extraData
					.getSerializable(PatStrangerExtraData.PS_SI_BEAN);
			patLocationList = (List<LocationBean>) _extraData
					.getSerializable(PatStrangerExtraData.PS_USER_PATLOCATIONS);
		}

		// set content view
		setContentView(R.layout.activity_pat_stranger);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(R.string.patStranger_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// TODO Auto-generated method stub

	}

	// inner class
	/**
	 * @name PatStrangerExtraData
	 * @descriptor user pat stranger extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class PatStrangerExtraData {

		// stranger info bean, be patted location list
		public static final String PS_SI_BEAN = "patStranger_strangerInfo_bean";
		public static final String PS_USER_PATLOCATIONS = "patStranger_user_patLocationList";

	}

}
