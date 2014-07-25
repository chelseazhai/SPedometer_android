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
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatLocationExtBean;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatModel;
import com.smartsport.spedometer.strangersocial.pat.UserInfoPatLocationExtBean;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
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

	// stranger pat model
	private StrangerPatModel strangerPatModel = StrangerPatModel.getInstance();

	// the user pat stranger info bean and be patted location list
	private UserInfoPatLocationExtBean strangerInfo;
	private List<StrangerPatLocationExtBean> patLocationList;

	// pat location saved instance state
	private Bundle patLocationSavedInstanceState;

	// pat location mapView and autoNavi map
	private MapView patLocationMapView;
	private AMap autoNaviMap;

	// pat location autoNavi geocode search
	private GeocodeSearch autoNaviGeocoderSearch;

	// stranger last pat time
	private Long strangerLastPatTime;

	// stranger pat info textView
	private TextView strangerPatInfoTextView;

	// get stranger pat location progress dialog
	private static SSProgressDialog getStrangerPatLocationProgDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(patLocationSavedInstanceState = savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the selected user pat stranger info bean
			strangerInfo = (UserInfoPatLocationExtBean) _extraData
					.getSerializable(PatStrangerExtraData.PS_SI_BEAN);
		}

		// set content view
		setContentView(R.layout.activity_pat_stranger);

		// check the user pat stranger id and then get pat location from remote
		// server
		if (null != strangerInfo) {
			// show get stranger pat location progress dialog
			getStrangerPatLocationProgDlg = SSProgressDialog.show(this,
					R.string.procMsg_getStrangerPatLocation);

			// get login user
			UserPedometerExtBean _loginUser = (UserPedometerExtBean) UserManager
					.getInstance().getLoginUser();

			strangerPatModel.getStrangerPatLocation(_loginUser.getUserId(),
					_loginUser.getUserKey(), strangerInfo.getUserId(),
					new ICMConnector() {

						@SuppressWarnings("unchecked")
						@Override
						public void onSuccess(Object... retValue) {
							// dismiss get stranger pat location progress dialog
							getStrangerPatLocationProgDlg.dismiss();

							// check return values
							if (null != retValue
									&& 0 < retValue.length
									&& retValue[retValue.length - 1] instanceof List) {
								// get the user pat stranger be patted location
								// list
								patLocationList = (List<StrangerPatLocationExtBean>) retValue[retValue.length - 1];

								LOGGER.info("The user pat stranger be patted location list = "
										+ patLocationList);

								// check the user pat stranger be patted
								// location list
								if (null != patLocationList
										&& 0 < patLocationList.size()) {
									// get the last pat location
									StrangerPatLocationExtBean _lastPatLocationExtBean = patLocationList
											.get(patLocationList.size() - 1);

									// save stranger last pat time
									strangerLastPatTime = _lastPatLocationExtBean
											.getPatTime();

									// search stranger last pat location address
									autoNaviGeocoderSearch
											.getFromLocationAsyn(new RegeocodeQuery(
													new LatLonPoint(
															_lastPatLocationExtBean
																	.getLatitude(),
															_lastPatLocationExtBean
																	.getLongitude()),
													200, GeocodeSearch.AMAP));
								}

								// add stranger pat location maker
								//
							} else {
								LOGGER.error("Update the user pat stranger be patted locations UI error");
							}
						}

						@Override
						public void onFailure(int errorCode, String errorMsg) {
							LOGGER.error("Get stranger pat location from remote server error, error code = "
									+ errorCode + " and message = " + errorMsg);

							// dismiss get stranger pat location progress dialog
							getStrangerPatLocationProgDlg.dismiss();

							// check error code and process hopeRun business
							// error
							if (errorCode < 100) {
								// show error message toast
								Toast.makeText(PatStrangerActivity.this,
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
		strangerPatInfoTextView = (TextView) findViewById(R.id.ps_strangerPatInfo_textView);

		// get pat location mapView
		patLocationMapView = (MapView) findViewById(R.id.ps_stranger_patLocation_mapView);

		// pat stranger pat location mapView perform onCreate method
		patLocationMapView.onCreate(patLocationSavedInstanceState);

		// get autoNavi map
		autoNaviMap = patLocationMapView.getMap();

		// get autoNavi geocode search
		autoNaviGeocoderSearch = new GeocodeSearch(this);

		// set its on geocode search listener
		autoNaviGeocoderSearch
				.setOnGeocodeSearchListener(new StrangerPatLocationOnGeocodeSearchListener());
	}

	// inner class
	/**
	 * @name PatStrangerExtraData
	 * @descriptor user pat stranger extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class PatStrangerExtraData {

		// stranger info bean
		public static final String PS_SI_BEAN = "patStranger_strangerInfo_bean";

	}

	/**
	 * @name StrangerPatLocationOnGeocodeSearchListener
	 * @descriptor stranger pat location on geocode search listener
	 * @author Ares
	 * @version 1.0
	 */
	class StrangerPatLocationOnGeocodeSearchListener implements
			OnGeocodeSearchListener {

		@Override
		public void onGeocodeSearched(GeocodeResult result, int code) {
			// nothing to do
		}

		@Override
		public void onRegeocodeSearched(RegeocodeResult result, int code) {
			// TODO Auto-generated method stub

			// test by ares
			// update stranger pat info textView text
			strangerPatInfoTextView
					.setText(String.format(
							getString(R.string.patStranger_lastPatInfo_format),
							strangerInfo.getPatCount(), "14-12-22 12:23",
							"南京市下关区汽轮四村"));
		}

	}

}
