/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;

/**
 * @name WalkMyLocationStyle
 * @descriptor smartsport walk my location style
 * @author Ares
 * @version 1.0
 */
public class WalkMyLocationStyle extends MyLocationStyle {

	// walk my location style
	public static final MyLocationStyle WALK_MYLOCATION_STYLE = new MyLocationStyle()
			.myLocationIcon(
					BitmapDescriptorFactory
							.fromResource(R.drawable.img_poi_mylocation))
			.radiusFillColor(
					SSApplication.getContext().getResources()
							.getColor(R.color.quarter_black_transparent))
			.strokeWidth(10)
			.strokeColor(
					SSApplication.getContext().getResources()
							.getColor(android.R.color.transparent));

}
