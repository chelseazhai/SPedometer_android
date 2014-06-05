/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.text.SpannableString;
import android.view.View;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSSimpleAdapterViewBinder
 * @descriptor smartsport simple adapter view binder
 * @author Ares
 * @version 1.0
 */
public class SSSimpleAdapterViewBinder implements ViewBinder {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			SSSimpleAdapterViewBinder.class);

	@Override
	public boolean setViewValue(View view, Object data,
			String textRepresentation) {
		boolean _ret = false;

		// check view type
		// textView
		if (view instanceof TextView) {
			// set view text
			((TextView) view).setText(null == data ? ""
					: data instanceof SpannableString ? (SpannableString) data
							: data.toString());

			// update return flag
			_ret = true;
		}

		return _ret;
	}

}
