/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.LatLng;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WalkStartPointLocationSource
 * @descriptor smartsport walk start point location source
 * @author Ares
 * @version 1.0
 */
public class WalkStartPointLocationSource implements LocationSource {

	// logger
	private final SSLogger LOGGER = new SSLogger(
			WalkStartPointLocationSource.class);

	// activity context
	private Context context;

	// autoNavi map
	private AMap autoNaviMap;

	// autoNavi map location changed listener
	private OnLocationChangedListener locationChangedListener;

	// autoNavi map location manager proxy
	private LocationManagerProxy locationManagerProxy;

	// autoNavi map location listener
	private AutoNaviMapLocationListener autoNaviMapLocationListener;

	/**
	 * @title WalkStartPointLocationSource
	 * @descriptor walk start point location source constructor
	 * @author Ares
	 */
	@Deprecated
	public WalkStartPointLocationSource() {
		super();
	}

	/**
	 * @title WalkStartPointLocationSource
	 * @descriptor walk start point location source constructor with activity
	 *             context and autoNavi map
	 * @param context
	 *            : activity context
	 * @param autoNaviMap
	 *            : autoNavi map
	 * @author Ares
	 */
	public WalkStartPointLocationSource(Context context, AMap autoNaviMap) {
		super();

		// save activity context and autoNavi map
		this.context = context;
		this.autoNaviMap = autoNaviMap;
	}

	@Override
	public void activate(OnLocationChangedListener locationChangedListener) {
		// save autoNavi map location changed listener
		this.locationChangedListener = locationChangedListener;

		// check autoNavi map location manager proxy and initialize
		if (null == locationManagerProxy) {
			locationManagerProxy = LocationManagerProxy.getInstance(context);

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
		// clear autoNavi map location changed listener
		locationChangedListener = null;

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
		public void onStatusChanged(String provider, int status, Bundle extras) {
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

			// check autoNavi map location changed listener and autoNavi map
			// location
			if (null != locationChangedListener && null != autoNaviMapLocation) {
				// show location
				locationChangedListener.onLocationChanged(autoNaviMapLocation);

				// animate camera
				autoNaviMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(autoNaviMapLocation.getLatitude(),
								autoNaviMapLocation.getLongitude()),
						context.getResources().getInteger(
								R.integer.config_autoNaviMap_zoomLevel)));
			} else {
				LOGGER.error("AutoNavi mapView location error, location changed listener = "
						+ locationChangedListener
						+ " and autoNavi map location = " + autoNaviMapLocation);
			}
		}

	}

}
