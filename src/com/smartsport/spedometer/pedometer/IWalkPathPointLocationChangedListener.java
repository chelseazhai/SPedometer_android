/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import com.amap.api.services.core.LatLonPoint;

/**
 * @name IWalkPathPointLocationChangedListener
 * @descriptor walk path point location changed listener interface
 * @author Ares
 * @version 1.0
 */
public interface IWalkPathPointLocationChangedListener {

	/**
	 * @title onLocationChanged
	 * @descriptor walk path point location changed
	 * @param walkPathPoint
	 *            : walk path point
	 * @param walkSpeed
	 *            : walk speed
	 * @param walkDistance
	 *            : walk distance
	 * @author Ares
	 */
	public void onLocationChanged(LatLonPoint walkPathPoint, double walkSpeed,
			double walkDistance);

}
