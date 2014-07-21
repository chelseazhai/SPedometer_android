/**
 * 
 */
package com.smartsport.spedometer.history.patstranger;

import java.util.List;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.LocationBean;
import com.smartsport.spedometer.strangersocial.pat.UserInfoPatLocationExtBean;
import com.smartsport.spedometer.user.info.UserGender;
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

	// pat location saved instance state
	private Bundle patLocationSavedInstanceState;

	// pat location mapView and autoNavi map
	private MapView patLocationMapView;
	private AMap autoNaviMap;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(patLocationSavedInstanceState = savedInstanceState);

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

		// get the stranger avatar imageView, nickname textView and gender
		// imageView
		ImageView _avatarImgView = (ImageView) findViewById(R.id.ps_strangerAvatar_imageView);
		TextView _nicknameTextView = (TextView) findViewById(R.id.ps_strangerNickname_textView);
		ImageView _genderImgView = (ImageView) findViewById(R.id.ps_strangerGender_imageView);

		// check the pat stranger info
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
			LOGGER.error("The pat stranger info bean is null");
		}

		// set the stranger last pat info textView text
		// test by ares
		((TextView) findViewById(R.id.ps_strangerPatInfo_textView))
				.setText(String.format(
						getString(R.string.patStranger_lastPatInfo_format),
						strangerInfo.getPatCount(), "14-12-22 12:23",
						"南京市下关区汽轮四村"));

		// get pat location mapView
		patLocationMapView = (MapView) findViewById(R.id.ps_stranger_patLocation_mapView);

		// pat stranger pat location mapView perform onCreate method
		patLocationMapView.onCreate(patLocationSavedInstanceState);

		// get autoNavi map
		autoNaviMap = patLocationMapView.getMap();

		//
		//
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
