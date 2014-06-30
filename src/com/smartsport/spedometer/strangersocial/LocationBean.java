/**
 * 
 */
package com.smartsport.spedometer.strangersocial;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name LocationBean
 * @descriptor location bean(including longitude and latitude)
 * @author Ares
 * @version 1.0
 */
public class LocationBean implements Serializable {

	/**
	 * location bean serial version UID
	 */
	private static final long serialVersionUID = -3095897305646045655L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(LocationBean.class);

	// longitude and latitude
	private double longitude;
	private double latitude;

	/**
	 * @title LocationBean
	 * @descriptor location bean constructor
	 * @author Ares
	 */
	public LocationBean() {
		super();
	}

	/**
	 * @title LocationBean
	 * @descriptor location bean constructor with longitude and latitude info
	 * @param longitude
	 *            : location longitude info
	 * @param latitude
	 *            : location latitude info
	 * @author Ares
	 */
	public LocationBean(double longitude, double latitude) {
		this();

		// save location info:longitude and latitude
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 * @title LocationBean
	 * @descriptor location bean constructor with json object
	 * @param info
	 *            : user walk path location json object
	 * @author Ares
	 */
	public LocationBean(JSONObject info) {
		this();

		// parse user walk path location info
		// check parsed user walk path location json object
		if (null != info) {
			// get context
			Context _context = SSApplication.getContext();

			// get user walk path location longitude and latitude value
			try {
				double _walkPathLocationLongitude = Double
						.parseDouble(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.walkLocationInfo_longitude)));
				double _walkPathLocationLatitude = Double
						.parseDouble(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.walkLocationInfo_latitude)));

				LOGGER.debug("Parse user walk path location json object successful, user walk path location longitude = "
						+ _walkPathLocationLongitude
						+ " and latitude = "
						+ _walkPathLocationLatitude);

				// save location info:longitude and latitude
				longitude = _walkPathLocationLongitude;
				latitude = _walkPathLocationLatitude;
			} catch (NumberFormatException e) {
				LOGGER.error("Parse user walk path location json object error, exception message = "
						+ e.getMessage());

				e.printStackTrace();

				// clear location info, using invalid value
				longitude = latitude = Double.MIN_VALUE;
			}
		} else {
			LOGGER.error("Parse user walk path location json object error, the info with longitude and latitude is null");
		}
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "LocationBean [longitude=" + longitude + " and latitude="
				+ latitude + "]";
	}

	/**
	 * @title toLatLonPoint
	 * @descriptor convert the location bean to latLonPoint
	 * @return the location latLonPoint
	 * @author Ares
	 */
	public LatLonPoint toLatLonPoint() {
		return new LatLonPoint(latitude, longitude);
	}

	/**
	 * @title getDistance
	 * @descriptor get the distance with the two location point
	 * @param location
	 *            : another location point
	 * @return the distance between the two location point
	 * @author Ares
	 */
	public double getDistance(LocationBean location) {
		// define the distance
		double _distance = 0.0;

		// check another location point
		if (null != location) {
			// define and calculate the distance between two latitude, longitude
			// points
			float[] _results = new float[] { 0 };
			Location.distanceBetween(getLatitude(), getLongitude(),
					location.getLatitude(), location.getLongitude(), _results);

			// check the results and set distance
			if (null != _results && 0 < _results.length) {
				_distance = _results[0];
			}
		} else {
			LOGGER.error("Get the distance with the two location point error, another location point is null");
		}

		return _distance;
	}

}
