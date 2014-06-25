/**
 * 
 */
package com.smartsport.spedometer.strangersocial.pat;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavBarButtonItem;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.LocationBean;
import com.smartsport.spedometer.strangersocial.NearbyStrangersActivity.NearbyStrangersExtraData;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name StrangerPatActivity
 * @descriptor smartsport stranger pat activity
 * @author Ares
 * @version 1.0
 */
public class StrangerPatActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			StrangerPatActivity.class);

	// stranger pat model
	private StrangerPatModel strangerPatModel = StrangerPatModel.getInstance();

	// the nearby stranger info bean, distance, be pat count and user pat
	// location
	private UserInfoPatLocationExtBean strangerInfo;
	private String strangerDistance;
	private int strangerBePatCount;
	private LocationBean userPatLocation;

	// stranger pat count textView
	private TextView strangerPatCountTextView;

	// the nearby stranger be pat flag
	private boolean hadBeenPatStranger;

	// add the stranger as friend button
	private Button addStrangerAsFriendBtn;

	// stranger pat progress dialog
	private static SSProgressDialog strangerPatProgDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the selected nearby stranger info bean, distance and user pat
			// location
			strangerInfo = (UserInfoPatLocationExtBean) _extraData
					.getSerializable(StrangerPatExtraData.SP_SI_BEAN);
			strangerDistance = _extraData
					.getString(StrangerPatExtraData.SP_STRANGER_DISTANCE);
			userPatLocation = (LocationBean) _extraData
					.getSerializable(StrangerPatExtraData.SP_USER_PATLOCATION);
		}

		// set content view
		setContentView(R.layout.activity_stranger_pat);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(R.string.strangerPat_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get the stranger avatar imageView, nickname textView and gender
		// imageView
		ImageView _avatarImgView = (ImageView) findViewById(R.id.sp_strangerAvatar_imageView);
		TextView _nicknameTextView = (TextView) findViewById(R.id.sp_strangerNickname_textView);
		ImageView _genderImgView = (ImageView) findViewById(R.id.sp_strangerGender_imageView);

		// check the nearby stranger info
		if (null != strangerInfo) {
			// set the stranger avatar imageView image
			_avatarImgView.setImageURI(Uri.parse(strangerInfo.getAvatarUrl()));

			// set the stranger nickname textView text
			_nicknameTextView.setText(strangerInfo.getNickname());

			// get the nearby stranger gender
			UserGender _nearbyGender = strangerInfo.getGender();

			// set the stranger gender imageView image
			if (UserGender.MALE == _nearbyGender) {
				_genderImgView.setImageResource(R.drawable.img_gender_male);
			} else if (UserGender.FEMALE == _nearbyGender) {
				_genderImgView.setImageResource(R.drawable.img_gender_female);
			}
		} else {
			LOGGER.error("The nearby stranger info bean is null");
		}

		// set the stranger distance textView text
		((TextView) findViewById(R.id.sp_strangerDistance_textView))
				.setText(strangerDistance);

		// get the stranger pat count and need to get info remain pat count
		strangerBePatCount = strangerInfo.getPatCount();
		int _getStrangerInfoRemainPatCount = getResources().getInteger(
				R.integer.config_limitCount_strangerPat)
				- strangerBePatCount;

		// get the stranger pat count textView
		strangerPatCountTextView = (TextView) findViewById(R.id.sp_strangerPatCount_textView);

		// set its text
		strangerPatCountTextView.setText(String.format(
				getString(R.string.patStranger_count_format),
				strangerBePatCount));

		// get pat the stranger button
		Button _patStrangerBtn = (Button) findViewById(R.id.sp_patStranger_button);

		// set its on click listener
		_patStrangerBtn.setOnClickListener(new PatStrangerBtnOnClickListener());

		// get add the stranger as friend button
		addStrangerAsFriendBtn = (Button) findViewById(R.id.sp_addAsFriend_button);

		// set its on click listener
		addStrangerAsFriendBtn
				.setOnClickListener(new AddStrangerAsFriendBtnOnClickListener());

		// check stranger pat count
		if (0 >= _getStrangerInfoRemainPatCount) {
			// show add the stranger as friend button
			addStrangerAsFriendBtn.setVisibility(View.VISIBLE);
		} else {
			// update the stranger pat count textView text
			strangerPatCountTextView
					.setText(String
							.format(getString(R.string.addStrangerAsFriend_limitPatCount_format),
									_getStrangerInfoRemainPatCount));
		}
	}

	@Override
	protected void onBackBarButtonItemClick(SSBNavBarButtonItem backBarBtnItem) {
		// check the nearby stranger be pat
		if (hadBeenPatStranger) {
			// define stranger pat extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put the nearby stranger had been pat info to extra data map as
			// param
			_extraMap.put(NearbyStrangersExtraData.NSS_SI_BEAN, strangerInfo);

			// pop the activity with result code and extra map
			popActivityWithResult(RESULT_OK, _extraMap);
		} else {
			super.onBackBarButtonItemClick(backBarBtnItem);
		}
	}

	// inner class
	/**
	 * @name StrangerPatExtraData
	 * @descriptor stranger pat extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class StrangerPatExtraData {

		// stranger info bean, distance and user pat location
		public static final String SP_SI_BEAN = "strangerPat_strangerInfo_bean";
		public static final String SP_STRANGER_DISTANCE = "strangerPat_stranger_distance";
		public static final String SP_USER_PATLOCATION = "strangerPat_user_patLocation";

	}

	/**
	 * @name PatStrangerBtnOnClickListener
	 * @descriptor pat stranger button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class PatStrangerBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// check the pat stranger info and user pat location
			if (null != strangerInfo && null != userPatLocation) {
				// get pedometer login user
				UserPedometerExtBean _loginUser = (UserPedometerExtBean) UserManager
						.getInstance().getLoginUser();

				// show pat nearby stranger progress dialog
				strangerPatProgDlg = SSProgressDialog.show(
						StrangerPatActivity.this,
						R.string.procMsg_patNearbyStranger);

				// pat the nearby stranger
				strangerPatModel.patStranger(_loginUser.getUserId(),
						_loginUser.getUserKey(), strangerInfo.getUserId(),
						userPatLocation.toLatLonPoint(), new ICMConnector() {

							@Override
							public void onSuccess(Object... retValue) {
								// dismiss pat nearby stranger progress dialog
								strangerPatProgDlg.dismiss();

								// check return values
								if (null != retValue
										&& 1 < retValue.length
										&& (retValue[0] instanceof Integer && retValue[retValue.length - 1] instanceof Integer)) {
									// mark the stranger you had patted
									strangerInfo
											.setPatCount(++strangerBePatCount);
									hadBeenPatStranger = true;

									// get stranger pat count
									int _patCount = (Integer) retValue[0];

									// get and check stranger pat remain require
									// pat count
									int _remainRequirePatCount = (Integer) retValue[retValue.length - 1]
											- _patCount;
									if (0 >= _remainRequirePatCount) {
										// update the stranger pat count
										// textView text
										strangerPatCountTextView.setText(String
												.format(getString(R.string.patStranger_count_format),
														_patCount));

										// show add the stranger as friend
										// button
										addStrangerAsFriendBtn
												.setVisibility(View.VISIBLE);
									} else {
										// update the stranger pat count
										// textView text
										strangerPatCountTextView.setText(String
												.format(getString(R.string.addStrangerAsFriend_limitPatCount_format),
														_remainRequirePatCount));
									}

									// show tip message
									Toast.makeText(
											StrangerPatActivity.this,
											String.format(
													getString(R.string.toast_strangerPatCount_format),
													_patCount),
											Toast.LENGTH_LONG).show();
								} else {
									LOGGER.error("Pat stranger error");
								}
							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								LOGGER.error("Pat nearby stranger error, error code = "
										+ errorCode
										+ " and message = "
										+ errorMsg);

								// dismiss pat nearby stranger progress dialog
								strangerPatProgDlg.dismiss();

								// check error code and process hopeRun business
								// error
								if (errorCode < 100) {
									// show error message toast
									Toast.makeText(StrangerPatActivity.this,
											errorMsg, Toast.LENGTH_SHORT)
											.show();

									// test by ares
									//
								}
							}

						});
			} else {
				LOGGER.error("Pat stranger error, the nearby stranger info bean = "
						+ strangerInfo
						+ " and user pat location = "
						+ userPatLocation);
			}
		}

	}

	/**
	 * @name AddStrangerAsFriendBtnOnClickListener
	 * @descriptor add the stranger as friend button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class AddStrangerAsFriendBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// test by ares
			Toast.makeText(StrangerPatActivity.this, "添加好友请求发送成功(测试@Ares)",
					Toast.LENGTH_LONG).show();

			// TODO Auto-generated method stub
			//
		}

	}

}
