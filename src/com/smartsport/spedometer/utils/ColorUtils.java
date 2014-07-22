/**
 * 
 */
package com.smartsport.spedometer.utils;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;

import com.smartsport.spedometer.SSApplication;

/**
 * @name ColorUtils
 * @descriptor color utils
 * @author Ares
 * @version 1.0
 */
public class ColorUtils {

	// logger
	private static final SSLogger LOGGER = new SSLogger(ColorUtils.class);

	/**
	 * @title darkenColor
	 * @descriptor get darken color with original color
	 * @param color
	 *            : the original color
	 * @return the original color darken color
	 * @author Ares
	 */
	public static int darkenColor(int color) {
		// define HSV
		float[] _hsv = new float[3];

		// get original color HSV and set saturation 80% percents
		Color.colorToHSV(color, _hsv);
		_hsv[2] *= 0.8f;

		return Color.HSVToColor(_hsv);
	}

	/**
	 * @title colors
	 * @descriptor get the color array with its resource id
	 * @param colorArrResId
	 *            : the color array resource id
	 * @return the color array
	 * @author Ares
	 */
	public static int[] colors(int colorArrResId) {
		// define color array
		int[] _colors = null;

		// get resource
		Resources _res = SSApplication.getContext().getResources();

		// get and check color typed array
		TypedArray _colorTypedArray = _res.obtainTypedArray(colorArrResId);
		if (null != _colorTypedArray) {
			// initialize color array
			_colors = new int[_colorTypedArray.length()];

			// parse each got color
			for (int i = 0; i < _colors.length; i++) {
				_colors[i] = _res.getColor(_colorTypedArray.getResourceId(i,
						android.R.color.transparent));
			}

			// recycle color typed array
			_colorTypedArray.recycle();
		} else {
			LOGGER.error("Get color array with resouce id error, the color typed array = "
					+ _colorTypedArray);
		}

		return _colors;
	}
}
