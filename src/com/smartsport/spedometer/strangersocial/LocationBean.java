/**
 * 
 */
package com.smartsport.spedometer.strangersocial;

import java.io.Serializable;

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

}
