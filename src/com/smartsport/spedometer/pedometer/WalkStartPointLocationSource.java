/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import android.content.Context;
import android.graphics.Color;
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
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
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

	// walk start and stop point location marker options
	private final MarkerOptions WALK_STARTPOINTLOCATION_MARKER_OPTIONS = new MarkerOptions()
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.img_walk_startpoint_marker_icon));
	private final MarkerOptions WALK_STOPPOINTLOCATION_MARKER_OPTIONS = new MarkerOptions()
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.img_walk_stoppoint_marker_icon));

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

	// walk path point location changed listener
	private IWalkPathPointLocationChangedListener walkPathPointLocationChangedListener;

	// walk path polyline options
	private PolylineOptions walkPathPolylineOptions;

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
	 * @title setWalkPathPointLocationChangedListener
	 * @descriptor set walk path point location changed listener
	 * @param walkPathPointLocationChangedListener
	 *            : walk path point location changed listener
	 * @author Ares
	 */
	public void setWalkPathPointLocationChangedListener(
			IWalkPathPointLocationChangedListener walkPathPointLocationChangedListener) {
		this.walkPathPointLocationChangedListener = walkPathPointLocationChangedListener;
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
	 * @title startMarkWalkPath
	 * @descriptor start mark walk path
	 * @author Ares
	 */
	public void startMarkWalkPath() {
		// check located flag
		if (isLocated) {
			// get walk start point
			LatLng _walkStartPoint = new LatLng(walkLatLonPoint.getLatitude(),
					walkLatLonPoint.getLongitude());

			// generate walk start point location marker with position and add
			// it to autoNavi map
			autoNaviMap.addMarker(WALK_STARTPOINTLOCATION_MARKER_OPTIONS)
					.setPosition(_walkStartPoint);

			// generate walk path polyline options
			walkPathPolylineOptions = genWalkPathPolylineOptions(_walkStartPoint);
		} else {
			LOGGER.warning("AutoNavi map not located user location, so can't mark user walk start point");
		}
	}

	/**
	 * @title stopMarkWalkPath
	 * @descriptor stop mark walk path
	 * @author Ares
	 */
	public void stopMarkWalkPath() {
		// clear walk path point location changed listener
		walkPathPointLocationChangedListener = null;

		// check located flag
		if (isLocated) {
			// generate walk stop point location marker with position and add it
			// to autoNavi map
			autoNaviMap.addMarker(WALK_STOPPOINTLOCATION_MARKER_OPTIONS)
					.setPosition(
							new LatLng(walkLatLonPoint.getLatitude(),
									walkLatLonPoint.getLongitude()));

			// clear walk path polyline options
			walkPathPolylineOptions = null;
		} else {
			LOGGER.warning("AutoNavi map not located user location, so can't mark user walk stop point");
		}
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

	/**
	 * @title genWalkPathPolylineOptions
	 * @descriptor generate walk path polyline options
	 * @param walkPathPoint
	 *            : walk path point
	 * @return walk path polyline options
	 * @author Ares
	 */
	private PolylineOptions genWalkPathPolylineOptions(LatLng walkPathPoint) {
		PolylineOptions _walkPathPolylineOptions = null;

		// check walk path point
		if (null != walkPathPoint) {
			// new walk path polyline options
			_walkPathPolylineOptions = new PolylineOptions();

			// set its width and color
			_walkPathPolylineOptions.width(6.0f);
			_walkPathPolylineOptions.color(Color.GREEN);

			// add walk path location point
			_walkPathPolylineOptions.add(walkPathPoint);
		}

		return _walkPathPolylineOptions;
	}

	// inner class
	/**
	 * @name AutoNaviMapLocationListener
	 * @descriptor smartsport walk invite autoNavi map location listener
	 * @author Ares
	 * @version 1.0
	 */
	class AutoNaviMapLocationListener implements AMapLocationListener {

		// meters per kilometer
		private final int METERS_PER_KILOMETER = 1000;

		// meters per second to kilometers per hour scale
		private final float METERS_PER_SECOND2KILOMETERS_PER_HOUR_SCALE = 3.6f;

		// last walk location point
		private LatLonPoint lastWalkLatLonPoint;

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
			walkSpeed = autoNaviMapLocation.getSpeed()
					* METERS_PER_SECOND2KILOMETERS_PER_HOUR_SCALE;

			// check last walk location point then calculate the walk distance
			// and save it
			if (null == lastWalkLatLonPoint) {
				walkDistance = 0.0;
			} else {
				// define and calculate the distance
				float[] _results = new float[] { 0 };
				Location.distanceBetween(lastWalkLatLonPoint.getLatitude(),
						lastWalkLatLonPoint.getLongitude(),
						walkLatLonPoint.getLatitude(),
						walkLatLonPoint.getLongitude(), _results);

				// check the results and save distance
				if (null != _results && 0 < _results.length) {
					walkDistance = _results[0] / METERS_PER_KILOMETER;
				} else {
					walkDistance = 0.0;
				}
			}

			// update last walk location point
			lastWalkLatLonPoint = walkLatLonPoint;

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

			// check walk path point location changed listener then update walk
			// path point location, walk speed and distance
			if (null != walkPathPointLocationChangedListener) {
				walkPathPointLocationChangedListener.onLocationChanged(
						walkLatLonPoint, walkSpeed, walkDistance);
			}

			// check mark user walk path and then draw the walk path polyline
			if (null != walkPathPolylineOptions) {
				// get user walk current location point
				LatLng _walkCurrentLocationPoint = new LatLng(
						walkLatLonPoint.getLatitude(),
						walkLatLonPoint.getLongitude());

				// add user walk current location point then add walk path
				// location polyline to autoNavi map and invalidate
				walkPathPolylineOptions.add(_walkCurrentLocationPoint);
				autoNaviMap.addPolyline(walkPathPolylineOptions);
				autoNaviMap.postInvalidate();

				// reset walk path polyline options
				walkPathPolylineOptions = genWalkPathPolylineOptions(_walkCurrentLocationPoint);
			}
		}

	}

}
