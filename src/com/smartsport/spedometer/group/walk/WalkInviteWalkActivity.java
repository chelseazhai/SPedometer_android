/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WalkInviteWalkActivity
 * @descriptor smartsport walk invite invite activity
 * @author Ares
 * @version 1.0
 */
public class WalkInviteWalkActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			WalkInviteWalkActivity.class);

	// walk invite walk saved instance state
	private Bundle walkInviteWalkSavedInstanceState;

	// walk invite inviter walk path mapView and autoNavi map
	private MapView inviterWalkPathMapView;
	private AMap autoNaviMap;

	// autoNavi map location manager proxy
	private LocationManagerProxy locationManagerProxy;

	// inviter walk location changed listener
	private OnLocationChangedListener inviterWalkLocationChangedListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(walkInviteWalkSavedInstanceState = savedInstanceState);

		//

		// set content view
		setContentView(R.layout.activity_walkinvite_walk);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle("");
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get walk invite inviter walk path mapView
		inviterWalkPathMapView = (MapView) findViewById(R.id.wiw_inviter_walkPath_mapView);

		// inviter walk path mapView perform onCreate method
		inviterWalkPathMapView.onCreate(walkInviteWalkSavedInstanceState);

		// get autoNavi map
		autoNaviMap = inviterWalkPathMapView.getMap();

		// set my location style
		autoNaviMap.setMyLocationStyle(new MyLocationStyle()
				.myLocationIcon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.img_poi_mylocation))
				.radiusFillColor(
						getResources().getColor(
								R.color.quarter_black_transparent))
				.strokeWidth(10)
				.strokeColor(
						getResources().getColor(android.R.color.transparent)));

		// set location source
		autoNaviMap.setLocationSource(new WalkStartPointLocationSource());

		// enable get my location, compass and hidden location, zoom controls
		// button
		autoNaviMap.getUiSettings().setMyLocationButtonEnabled(false);
		autoNaviMap.setMyLocationEnabled(true);
		autoNaviMap.getUiSettings().setCompassEnabled(true);
		autoNaviMap.getUiSettings().setZoomControlsEnabled(false);

		//
	}

	@Override
	protected void onResume() {
		super.onResume();

		// inviter walk path mapView perform onResume method
		inviterWalkPathMapView.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// inviter walk path mapView perform onSaveInstanceState method
		inviterWalkPathMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// inviter walk path mapView perform onPause method
		inviterWalkPathMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// inviter walk path mapView perform onDestroy method
		inviterWalkPathMapView.onDestroy();
	}

	// inner class
	/**
	 * @name WalkStartPointLocationSource
	 * @descriptor smartsport walk start point location source
	 * @author Ares
	 * @version 1.0
	 */
	class WalkStartPointLocationSource implements LocationSource {

		// logger
		private final SSLogger LOGGER = new SSLogger(
				WalkStartPointLocationSource.class);

		// autoNavi map location listener
		private AutoNaviMapLocationListener autoNaviMapLocationListener;

		@Override
		public void activate(OnLocationChangedListener locationChangedListener) {
			// save inviter walk location changed listener
			inviterWalkLocationChangedListener = locationChangedListener;

			// check autoNavi map location manager proxy and initialize
			if (null == locationManagerProxy) {
				locationManagerProxy = LocationManagerProxy
						.getInstance(WalkInviteWalkActivity.this);

				// request location update
				locationManagerProxy
						.requestLocationUpdates(
								LocationProviderProxy.AMapNetwork,
								2000,
								10,
								autoNaviMapLocationListener = new AutoNaviMapLocationListener());
			}
		}

		@Override
		public void deactivate() {
			// clear inviter walk location changed listener
			inviterWalkLocationChangedListener = null;

			// check autoNavi map location manager proxy and clear
			if (null != locationManagerProxy) {
				locationManagerProxy.removeUpdates(autoNaviMapLocationListener);
				locationManagerProxy.destory();
			}
			locationManagerProxy = null;
		}

		// inner class
		/**
		 * @name AutoNaviMapLocationListener
		 * @descriptor smartsport walk invite autoNavi map location listener
		 * @author Ares
		 * @version 1.0
		 */
		class AutoNaviMapLocationListener implements AMapLocationListener {

			@Override
			@Deprecated
			public void onLocationChanged(Location location) {
				// nothing to do
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// nothing to do
			}

			@Override
			public void onProviderEnabled(String provider) {
				// nothing to do
			}

			@Override
			public void onProviderDisabled(String provider) {
				// nothing to do
			}

			@Override
			public void onLocationChanged(AMapLocation autoNaviMapLocation) {
				LOGGER.info("@@@, autoNaviMapLocation = " + autoNaviMapLocation);

				// check inviter walk location changed listener and autoNavi map
				// location
				if (null != inviterWalkLocationChangedListener
						&& null != autoNaviMapLocation) {
					// show location
					inviterWalkLocationChangedListener
							.onLocationChanged(autoNaviMapLocation);

					// animate camera
					autoNaviMap
							.animateCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(autoNaviMapLocation
											.getLatitude(), autoNaviMapLocation
											.getLongitude()),
									getResources()
											.getInteger(
													R.integer.config_autoNaviMap_zoomLevel)));
				} else {
					LOGGER.error("AutoNavi mapView location error, inviter walk location changed listener = "
							+ inviterWalkLocationChangedListener
							+ " and autoNavi map location = "
							+ autoNaviMapLocation);
				}
			}

		}

	}

}
