/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.net.Uri;
import android.text.Spannable;
import android.view.View;
import android.widget.ImageView;
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
			LOGGER.info("Overwrite textView data view binder");

			// set view text
			((TextView) view).setText(null == data ? ""
					: data instanceof Spannable ? (Spannable) data : data
							.toString());

			// update return flag
			_ret = true;
		}
		// imageView
		else if (view instanceof ImageView) {
			// check data type
			if (data instanceof String) {
				LOGGER.info("Overwrite imageView data view binder");

				// set imageView image uri
				((ImageView) view).setImageURI(Uri.parse((String) data));

				// update return flag
				_ret = true;
			} else {
				LOGGER.warning("ImageView set image warning, image data = "
						+ data);
			}
		}

		return _ret;
	}

}
