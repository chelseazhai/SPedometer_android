/**
 * 
 */
package com.smartsport.spedometer.strangersocial;

import java.io.Serializable;

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

	// the earth radius
	private static final double EARTH_RADIUS = 6378.137;

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
			// get two location longitude radius D-value
			double _lngDValue = getRadius(getLongitude())
					- getRadius(location.getLongitude());

			// get two location latitude radius and D-value
			double _lat1Radius = getRadius(getLatitude());
			double _lat2Radius = getRadius(location.getLatitude());
			double _latDValue = _lat1Radius - _lat2Radius;

			// calculate the distance
			_distance = Math.round(2
					* Math.asin(Math.sqrt(Math.pow(Math.sin(_latDValue / 2), 2)
							+ Math.cos(_lat1Radius) * Math.cos(_lat2Radius)
							* Math.pow(Math.sin(_lngDValue / 2), 2)))
					* EARTH_RADIUS * 10000) / 10000;
		} else {
			LOGGER.error("Get the distance with the two location point error, another location point is null");
		}

		return _distance;
	}

	/**
	 * @title getRadius
	 * @descriptor get the earth radius with the location point info(longitude
	 *             or latitude)
	 * @param locationLngLat
	 *            : location point longitude or latitude
	 * @return the location point latitude earth radius
	 * @author Ares
	 */
	private double getRadius(double locationLngLat) {
		return locationLngLat * Math.PI / 180.0;
	}

}
