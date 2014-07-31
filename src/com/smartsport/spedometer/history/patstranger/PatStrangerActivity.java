/**
 * 
 */
package com.smartsport.spedometer.history.patstranger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
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

	// pat location last location marker and others marker
	private final MarkerOptions STRANGER_LASTPATLOCATION_MARKER_OPERTIONS = new MarkerOptions()
			.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
	private final MarkerOptions STRANGER_OTHERSPATLOCATION_MARKER_OPERTIONS = new MarkerOptions()
			.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

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

	// stranger last pat time, location point and marker
	private Long strangerLastPatTime;
	private LatLonPoint lastpatLocationPoint;
	private Marker lastpatLocationMarker;

	// stranger pat location marker list
	private List<Marker> patLocationMarkers;

	// pat location autoNavi geocode search
	private GeocodeSearch autoNaviGeocoderSearch;

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
									// initialize pat location marker list
									patLocationMarkers = new ArrayList<Marker>();
									for (int i = 0; i < patLocationList.size(); i++) {
										// get the stranger pat location
										StrangerPatLocationExtBean _patLocationExtBean = patLocationList
												.get(i);

										// check the index
										if (0 == i) {
											// save stranger last pat time and
											// position point
											strangerLastPatTime = _patLocationExtBean
													.getPatTime();
											lastpatLocationPoint = new LatLonPoint(
													_patLocationExtBean
															.getLatitude(),
													_patLocationExtBean
															.getLongitude());

											// search stranger last pat location
											// address
											autoNaviGeocoderSearch
													.getFromLocationAsyn(new RegeocodeQuery(
															lastpatLocationPoint,
															200,
															GeocodeSearch.AMAP));
										} else {
											// add stranger pat location marker
											// to autoNavi map
											// generate pat location marker
											Marker _patLocationMarker = autoNaviMap
													.addMarker(STRANGER_OTHERSPATLOCATION_MARKER_OPERTIONS);

											// update its position
											_patLocationMarker.setPosition(new LatLng(
													_patLocationExtBean
															.getLatitude(),
													_patLocationExtBean
															.getLongitude()));

											// add stranger pat location to list
											patLocationMarkers
													.add(_patLocationMarker);
										}
									}
								}
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

		// milliseconds per second
		private static final int MILLISECONDS_PER_SECOND = 1000;

		// stranger pat time date format
		@SuppressLint("SimpleDateFormat")
		private final SimpleDateFormat STRANGER_PATTIME_DATEFORMAT = new SimpleDateFormat(
				getString(R.string.long_dateFormat));

		@Override
		public void onGeocodeSearched(GeocodeResult result, int code) {
			// nothing to do
		}

		@Override
		public void onRegeocodeSearched(RegeocodeResult result, int code) {
			// check code
			switch (code) {
			case 0:
				// regeocode searched successful
				// check result and regeocode address
				if (null != result && null != result.getRegeocodeAddress()) {
					// get regeocode format address
					String _regeocodeFormatAddr = result.getRegeocodeAddress()
							.getFormatAddress();

					LOGGER.info("Search stranger pat location successful, regeocode result = "
							+ result
							+ " format address "
							+ _regeocodeFormatAddr);

					// generate last pat location marker
					lastpatLocationMarker = autoNaviMap
							.addMarker(STRANGER_LASTPATLOCATION_MARKER_OPERTIONS);

					// get last pat position
					LatLng _lastPatPositionLatLng = new LatLng(
							lastpatLocationPoint.getLatitude(),
							lastpatLocationPoint.getLongitude());

					// update its position
					lastpatLocationMarker.setPosition(_lastPatPositionLatLng);

					// animate camera
					autoNaviMap.animateCamera(CameraUpdateFactory
							.newLatLngZoom(_lastPatPositionLatLng, 20));

					// update stranger pat info textView text
					strangerPatInfoTextView.setText(String.format(
							getString(R.string.patStranger_lastPatInfo_format),
							strangerInfo.getPatCount(),
							STRANGER_PATTIME_DATEFORMAT
									.format(strangerLastPatTime
											* MILLISECONDS_PER_SECOND),
							_regeocodeFormatAddr));
				} else {
					// no result
					LOGGER.error("Search stranger pat location no result, regeocode result = "
							+ result + " and code = " + code);

					// show stranger last pat location search no result toast
					Toast.makeText(PatStrangerActivity.this,
							R.string.toast_searchPatLocation_noResult,
							Toast.LENGTH_LONG).show();
				}
				break;

			case 27:
				// no network
				LOGGER.error("Search stranger pat location error(no network), regeocode result = "
						+ result + " and code = " + code);

				// show stranger last pat location search error(no network)
				// toast
				Toast.makeText(PatStrangerActivity.this,
						R.string.toast_searchPatLocation_error_noNetwork,
						Toast.LENGTH_LONG).show();
				break;

			case 32:
				// invalid key
				LOGGER.error("Search stranger pat location error(invalid key), regeocode result = "
						+ result + " and code = " + code);

				// show stranger last pat location search error(invalid key)
				// toast
				Toast.makeText(PatStrangerActivity.this,
						R.string.toast_searchPatLocation_error_invalidKey,
						Toast.LENGTH_LONG).show();
				break;

			default:
				// unknown
				LOGGER.error("Search stranger pat location error(unknown), regeocode result = "
						+ result + " and code = " + code);

				// show stranger last pat location search error(unknown) toast
				Toast.makeText(PatStrangerActivity.this,
						R.string.toast_searchPatLocation_error_unknown,
						Toast.LENGTH_LONG).show();
				break;
			}
		}

	}

}
