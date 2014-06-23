/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSProgressDialog
 * @descriptor smartsport progress dialog
 * @author Ares
 * @version 1.0
 */
public class SSProgressDialog extends ProgressDialog {

	// logger
	private static final SSLogger LOGGER = new SSLogger(SSProgressDialog.class);

	// progress dialog tip textView
	private TextView tipTextView;

	/**
	 * @title SSProgressDialog
	 * @descriptor smartsport progress dialog constructor
	 * @param context
	 *            : context
	 * @author Ares
	 */
	@Deprecated
	public SSProgressDialog(Context context) {
		super(context);
	}

	/**
	 * @title SSProgressDialog
	 * @descriptor smartsport progress dialog private constructor with theme
	 * @param context
	 *            : context
	 * @param theme
	 *            : theme
	 * @author Ares
	 */
	private SSProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.ss_progress_dialog);

		// initialize content view
		// get progress dialog tip textView
		// tipTextView = (TextView)
		// getWindow().getDecorView().findViewById(R.id.sspd_progressTip_textView);
		tipTextView = (TextView) findViewById(R.id.sspd_progressTip_textView);
	}

	@Override
	public void setMessage(CharSequence message) {
		tipTextView = (TextView) findViewById(R.id.sspd_progressTip_textView);
		
		// check message and set tip
		if (null != message && null != tipTextView) {
			// update progress dialog tip textView text
			tipTextView.setText(message);
		} else {
			LOGGER.error("Set smartsport progress dialog message error, tip textView = "
					+ tipTextView + " and message = " + message);
		}
	}

	/**
	 * @title show
	 * @descriptor show smartsport progress dialog with tip, indeterminate,
	 *             cancelable and cancel listener
	 * @param context
	 *            : context
	 * @param tip
	 *            : progress tip message
	 * @param indeterminate
	 *            : the indeterminate
	 * @param cancelable
	 *            : the cancelable
	 * @param cancelListener
	 *            : cancel listener
	 * @author Ares
	 */
	public static SSProgressDialog show(Context context, CharSequence tip,
			boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		// new smartsport progress dialog
		SSProgressDialog _progressDialog = new SSProgressDialog(context);

		// set the progress dialog attributes
		_progressDialog.setMessage(tip);
		_progressDialog.setIndeterminate(indeterminate);
		_progressDialog.setCancelable(cancelable);
		_progressDialog.setOnCancelListener(cancelListener);

		// show progress dialog
		_progressDialog.show();

		// return the smartsport progress dialog
		return _progressDialog;
	}

	/**
	 * @title show
	 * @descriptor show smartsport progress dialog with tip, indeterminate and
	 *             cancelable
	 * @param context
	 *            : context
	 * @param tip
	 *            : progress tip message
	 * @param indeterminate
	 *            : the indeterminate
	 * @param cancelable
	 *            : the cancelable
	 * @author Ares
	 */
	public static SSProgressDialog show(Context context, CharSequence tip,
			boolean indeterminate, boolean cancelable) {
		return show(context, tip, indeterminate, cancelable, null);
	}

	/**
	 * @title show
	 * @descriptor show smartsport progress dialog with tip and indeterminate
	 * @param context
	 *            : context
	 * @param tip
	 *            : progress tip message
	 * @param indeterminate
	 *            : the indeterminate
	 * @author Ares
	 */
	public static SSProgressDialog show(Context context, CharSequence tip,
			boolean indeterminate) {
		return show(context, tip, indeterminate, false);
	}

	/**
	 * @title show
	 * @descriptor show smartsport progress dialog with tip
	 * @param context
	 *            : context
	 * @param tip
	 *            : progress tip message
	 * @author Ares
	 */
	public static SSProgressDialog show(Context context, CharSequence tip) {
		return show(context, tip, false);
	}

}
