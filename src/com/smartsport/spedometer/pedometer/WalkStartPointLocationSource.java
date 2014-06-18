/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
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

	// milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;

	// activity context
	private Context context;

	// autoNavi map
	private AMap autoNaviMap;

	// located flag and locate timeout
	private boolean isLocated;
	private long locateTimeout;

	// autoNavi map location changed listener
	private OnLocationChangedListener locationChangedListener;

	// autoNavi map location manager proxy
	private LocationManagerProxy locationManagerProxy;

	// autoNavi map location listener
	private AutoNaviMapLocationListener autoNaviMapLocationListener;

	// walk point location info, walk speed and total distance
	private LatLonPoint walkLatLonPoint;
	private double walkSpeed;
	private double walkDistance;

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
	public WalkStartPointLocationSource(Context context, AMap autoNaviMap,
			long locateTimtout) {
		super();

		// save activity context and autoNavi map
		this.context = context;
		this.autoNaviMap = autoNaviMap;

		// save locate timeout
		this.locateTimeout = locateTimtout;
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
		// locate default timeout(8 seconds)
		this(context, autoNaviMap, 8 * MILLISECONDS_PER_SECOND);
	}

	public LatLonPoint getWalkLatLonPoint() {
		return walkLatLonPoint;
	}

	public double getWalkSpeed() {
		return walkSpeed;
	}

	public double getWalkDistance() {
		return walkDistance;
	}

	/**
	 * @title getGPSStatus
	 * @descriptor get GPS status
	 * @return GPS status
	 * @author Ares
	 */
	public void getGPSStatus() {
		//
	}

	/**
	 * @title locateSuccess
	 * @descriptor locate successful notification
	 * @author Ares
	 */
	protected void locateSuccess() {
		// nothing to do
	}

	/**
	 * @title locateTimeout
	 * @descriptor locate timeout notification
	 * @author Ares
	 */
	protected void locateTimeout() {
		// nothing to do
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

			// set locate timeout, if locate timeout then stop locate
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// check located flag and set locate timeout
					if (!isLocated) {
						locateTimeout();
					}
				}

			}, locateTimeout);
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
			LOGGER.info("onLocationChanged, auto navi map location = "
					+ autoNaviMapLocation);

			// locate successful
			isLocated = true;
			locateSuccess();

			// save walk latitude, longitude point and walk speed
			walkLatLonPoint = new LatLonPoint(
					autoNaviMapLocation.getLatitude(),
					autoNaviMapLocation.getLongitude());
			walkSpeed = autoNaviMapLocation.getSpeed();

			// check autoNavi map location changed listener and autoNavi map
			// location
			if (null != locationChangedListener && null != autoNaviMapLocation) {
				// show location
				locationChangedListener.onLocationChanged(autoNaviMapLocation);

				// animate camera
				autoNaviMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(walkLatLonPoint.getLatitude(),
								walkLatLonPoint.getLongitude()),
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
