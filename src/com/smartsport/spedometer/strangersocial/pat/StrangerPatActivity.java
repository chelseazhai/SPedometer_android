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
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.NearbyStrangersActivity.NearbyStrangersExtraData;
import com.smartsport.spedometer.user.UserGender;
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
	private StrangerPatModel strangerPatModel;

	// the nearby stranger info bean, distance and be pat count
	private UserInfoPatLocationExtBean strangerInfo;
	private String strangerDistance;
	private int strangerBePatCount;

	// the nearby stranger be pat flag
	private boolean hadBeenPatStranger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the selected nearby stranger info bean and distance
			strangerInfo = (UserInfoPatLocationExtBean) _extraData
					.getSerializable(StrangerPatExtraData.SP_SI_BEAN);
			strangerDistance = _extraData
					.getString(StrangerPatExtraData.SP_STRANGER_DISTANCE);
		}

		// initialize stranger pat model
		strangerPatModel = new StrangerPatModel();

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
		TextView _strangerPatCountTextView = (TextView) findViewById(R.id.sp_strangerPatCount_textView);

		// set its text
		_strangerPatCountTextView.setText(String.format(
				getString(R.string.patStranger_count_format),
				strangerBePatCount));

		// get pat the stranger button
		Button _patStrangerBtn = (Button) findViewById(R.id.sp_patStranger_button);

		// set its on click listener
		_patStrangerBtn.setOnClickListener(new PatStrangerBtnOnClickListener());

		// get add the stranger as friend button
		Button _addAsFriendBtn = (Button) findViewById(R.id.sp_addAsFriend_button);

		// set its on click listener
		_addAsFriendBtn
				.setOnClickListener(new AddStrangerAsFriendBtnOnClickListener());

		// check stranger pat count
		if (0 >= _getStrangerInfoRemainPatCount) {
			// show add the stranger as friend button
			_addAsFriendBtn.setVisibility(View.VISIBLE);
		} else {
			// update the stranger pat count textView text
			_strangerPatCountTextView
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

		// stranger info bean and distance
		public static final String SP_SI_BEAN = "strangerPat_strangerInfo_bean";
		public static final String SP_STRANGER_DISTANCE = "strangerPat_stranger_distance";

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
			// check the pat stranger info
			if (null != strangerInfo) {
				// pat the nearby stranger
				strangerPatModel.patStranger(123123, "token",
						strangerInfo.getUserId(), null, new ICMConnector() {

							//

						});

				// test by ares
				strangerInfo.setPatCount(++strangerBePatCount);
				hadBeenPatStranger = true;
				Toast.makeText(StrangerPatActivity.this, "成功拍肩一次(测试@Ares)",
						Toast.LENGTH_LONG).show();
			} else {
				LOGGER.error("Pat stranger error, the nearby stranger info bean is null");
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
			// TODO Auto-generated method stub

			// test by ares
			Toast.makeText(StrangerPatActivity.this, "添加好友请求发送成功(测试@Ares)",
					Toast.LENGTH_LONG).show();
		}

	}

}
