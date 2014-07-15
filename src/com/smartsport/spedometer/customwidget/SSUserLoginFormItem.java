/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSUserLoginFormItem
 * @descriptor smartsport user login form item
 * @author Ares
 * @version 1.0
 */
public class SSUserLoginFormItem extends LinearLayout {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			SSUserLoginFormItem.class);

	// user login form item user input editText
	private EditText userInputEditText;

	/**
	 * @title SSUserLoginFormItem
	 * @descriptor smartsport user login form item constructor with context,
	 *             attributes and default style
	 * @param context
	 *            : context
	 * @param attrs
	 *            : widget attributes
	 * @param defStyle
	 *            : widget default style
	 */
	public SSUserLoginFormItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// initialize smartsport user login form item attributes
		initAttrs(context, attrs);
	}

	/**
	 * @title SSUserLoginFormItem
	 * @descriptor smartsport user login form item constructor with context and
	 *             attributes
	 * @param context
	 *            : context
	 * @param attrs
	 *            : widget attributes
	 */
	public SSUserLoginFormItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		// initialize smartsport user login form item attributes
		initAttrs(context, attrs);
	}

	/**
	 * @title SSUserLoginFormItem
	 * @descriptor smartsport user login form item constructor with context
	 * @param context
	 *            : context
	 */
	public SSUserLoginFormItem(Context context) {
		super(context);

		// initialize smartsport user login form item attributes
		initAttrs(context, null);
	}

	/**
	 * @title initAttrs
	 * @descriptor initialize smartsport user login form item attributes
	 * @param context
	 *            : context
	 * @param attrs
	 *            : attribute set
	 * @author Ares
	 */
	private void initAttrs(Context context, AttributeSet attrs) {
		// define user login form item typedArray, label, input editText hint
		TypedArray _typedArray = null;
		String _label = "";
		String _inputEditTextHint = "";

		try {
			// get smartsport user login form item typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.ss_userLogin_formItem, 0, 0);

			// get smartsport user login form item label, input editText hint attributes
			_label = _typedArray
					.getString(R.styleable.ss_userLogin_formItem_label);
			_inputEditTextHint = _typedArray
					.getString(R.styleable.ss_userLogin_formItem_inputEditTextHint);
		} catch (Exception e) {
			LOGGER.error("Get smartsport user login form item label, input editText hint attributes error, exception massage = "
					+ e.getMessage());

			e.printStackTrace();
		} finally {
			// recycle smartsport user login form item typedArray
			if (null != _typedArray) {
				_typedArray.recycle();
			}
		}

		// inflate smartsport user login form item layout
		LayoutInflater.from(context).inflate(
				R.layout.ss_user_login_formitem_layout, this);

		// check smartsport user login form item label and set its text
		if (null != _label) {
			// set smartsport user login form item label textView text
			((TextView) findViewById(R.id.ssulfi_userLogin_formItem_label_textView))
					.setText(_label);
		}

		// get smartsport user login form item input editText
		userInputEditText = (EditText) findViewById(R.id.ssulfi_userLogin_formItem_userInput_editText);

		// check smartsport user login form item input editText hint and set its
		// hint
		if (null != _inputEditTextHint) {
			// set smartsport user login form item input editText hint
			userInputEditText.setHint(_inputEditTextHint);
		}

		// check smartsport user login form item input editText input type and
		// set its type
		if (null != _inputEditTextType) {
			// set account login form item input editText input type
			if ("phone".equalsIgnoreCase(_inputEditTextType)) {
				userInputEditText.setInputType(InputType.TYPE_CLASS_PHONE);
			} else if ("textPassword".equalsIgnoreCase(_inputEditTextType)) {
				userInputEditText.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				LOGGER.warning("Smartsport user login form item user input editText type = "
						+ _inputEditTextType
						+ " setting not implement, using default editText text type");

				userInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		}
	}

}
