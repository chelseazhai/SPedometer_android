/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSAlterDialog
 * @descriptor smartsport alter dialog
 * @author Ares
 * @version 1.0
 */
public class SSAlterDialog extends AlertDialog {

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	public SSAlterDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param theme
	 */
	public SSAlterDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 */
	public SSAlterDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	// inner class
	/**
	 * @name SSAlterDialogBuilder
	 * @descriptor smartsport alter dialog builder
	 * @author Ares
	 * @version 1.0
	 */
	public static class SSAlterDialogBuilder extends Builder {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				SSAlterDialogBuilder.class);

		/**
		 * @title SSAlterDialogBuilder
		 * @descriptor smartsport alter dialog builder constructor with context
		 *             and theme
		 * @param context
		 *            : context
		 * @param theme
		 *            : theme
		 * @author Ares
		 */
		public SSAlterDialogBuilder(Context context, int theme) {
			super(context, theme);
		}

		/**
		 * @title SSAlterDialogBuilder
		 * @descriptor smartsport alter dialog builder constructor with context
		 * @param context
		 *            : context
		 * @author Ares
		 */
		public SSAlterDialogBuilder(Context context) {
			// new smartsport alter dialog using default theme
			// super(context, R.style.SSAlterDialogTheme);
			// test by ares
			super(context);
		}

		@Override
		public AlertDialog create() {
			// TODO Auto-generated method stub
			return super.create();
		}

		@Override
		public AlertDialog show() {
			// TODO Auto-generated method stub
			return super.show();
		}

		/**
		 * @title setView
		 * @descriptor set a custom view to be the contents of the dialog
		 * @param layoutResID
		 *            : the custom view layout resource id
		 * @return this builder object to allow for chaining of calls to set
		 *         methods
		 * @author Ares
		 */
		public Builder setView(int layoutResID) {
			// get layout inflate
			LayoutInflater _layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// get smartsport alter dialog view
			ViewGroup _ssAlterDialogView = (ViewGroup) _layoutInflater.inflate(
					R.layout.ss_alterdialog_layout, null);

			// inflate content view and add to smartsport alter dialog view
			try {
				// _layoutInflater.inflate(layoutResID, _ssAlterDialogView);
			} catch (InflateException e) {
				LOGGER.equals("Set smartsport alter dialog content view error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}

			// return smartsport alter dialog builder view
			return super.setView(_ssAlterDialogView);
		}

	}

}
