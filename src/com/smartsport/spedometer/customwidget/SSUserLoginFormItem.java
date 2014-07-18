/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

	// input method manager
	private InputMethodManager inputMethodManager;

	// show soft input timer and timer task
	private final Timer SHOW_SOFTINPUT_TIMER = new Timer();
	private TimerTask showSoftInputTimerTask;

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

		// get input method manager
		inputMethodManager = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

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

		// get input method manager
		inputMethodManager = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

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

		// get input method manager
		inputMethodManager = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		// initialize smartsport user login form item attributes
		initAttrs(context, null);
	}

	public String getUserInputEditText() {
		return userInputEditText.getText().toString();
	}

	public void setUserInputEditText(String userInputEditText) {
		// check user input edit text and set its editText text
		if (null != userInputEditText) {
			this.userInputEditText.setText(userInputEditText);
		}
	}

	/**
	 * @title addTextChangedListener
	 * @descriptor add smartsport user login form item user input editText text
	 *             changed listener
	 * @param userInputEditTextChangedWatcher
	 *            : smartsport user login form item user input editText text
	 *            changed watcher
	 */
	public void addTextChangedListener(
			ISSULFIUserInputEditTextTextWatcher userInputEditTextChangedWatcher) {
		// check user input editText text changed listener
		if (null != userInputEditTextChangedWatcher) {
			// add user input editText text changed watcher
			userInputEditText
					.addTextChangedListener(new UserInputEditTextTextWatcher(
							userInputEditTextChangedWatcher));
		} else {
			LOGGER.error("Smartsport user login form item user input editText text changed watcher is null");
		}
	}

	/**
	 * @title setAsFocus
	 * @descriptor set smartsport user login form item user input editText as
	 *             focus
	 */
	public void setAsFocus() {
		// set user input editText focusable
		userInputEditText.setFocusable(true);
		userInputEditText.setFocusableInTouchMode(true);
		userInputEditText.requestFocus();

		// show soft input after 250 milliseconds
		SHOW_SOFTINPUT_TIMER.schedule(showSoftInputTimerTask = new TimerTask() {

			@Override
			public void run() {
				inputMethodManager.showSoftInput(userInputEditText, 0);
			}

		}, 250);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

		// check and cancel show soft input timer task when when it lost focus
		if (!gainFocus) {
			if (null != showSoftInputTimerTask) {
				showSoftInputTimerTask.cancel();

				showSoftInputTimerTask = null;
			}
		}
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
		// define user login form item typedArray, label, icon image resource,
		// frameLayout percent, input editText hint and input type
		TypedArray _typedArray = null;
		String _label = null;
		int _iconImgResourceId = 0;
		float _labelPecent = 0.0f;
		String _inputEditTextHint = null;
		int _inputEditTextInputType = 0;

		try {
			// get smartsport user login form item typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.ss_userLogin_formItem, 0, 0);

			// get smartsport user login form item label, icon image resource,
			// frameLayout percent, input editText hint and input type
			// attributes
			_label = _typedArray
					.getString(R.styleable.ss_userLogin_formItem_label);
			_iconImgResourceId = _typedArray.getResourceId(
					R.styleable.ss_userLogin_formItem_labelIconSrc, 0);
			_labelPecent = _typedArray.getFloat(
					R.styleable.ss_userLogin_formItem_labelPercent, 0.0f);
			_inputEditTextHint = _typedArray
					.getString(R.styleable.ss_userLogin_formItem_android_hint);
			_inputEditTextInputType = _typedArray.getInt(
					R.styleable.ss_userLogin_formItem_android_inputType, 0);
		} catch (Exception e) {
			LOGGER.error("Get smartsport user login form item label, icon image resource, frameLayout percent, input editText hint or input type attributes error, exception massage = "
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

		// get smartsport user login form item label textView
		TextView _labelTextView = (TextView) findViewById(R.id.ssulfi_userLogin_formItem_label_textView);

		// check smartsport user login form item label and set its text
		if (null != _label) {
			// set smartsport user login form item label textView text and then
			// show
			_labelTextView.setText(_label);
			_labelTextView.setVisibility(View.VISIBLE);
		}

		// check smartsport user login form item label icon image resource and
		// set its image
		if (0 != _iconImgResourceId) {
			// get smartsport user login form item label icon imageView
			ImageView _labelIconImgView = (ImageView) findViewById(R.id.ssulfi_userLogin_formItem_label_imageView);

			// set its image resource and then show
			_labelIconImgView.setImageResource(_iconImgResourceId);
			_labelIconImgView.setVisibility(View.VISIBLE);

			// hide smartsport user login form item label textView
			_labelTextView.setVisibility(View.GONE);
		}

		// get smartsport user login form item input editText
		userInputEditText = (EditText) findViewById(R.id.ssulfi_userLogin_formItem_userInput_editText);

		// check smartsport user login form item label percent
		if (0.0 != _labelPecent) {
			// get smartsport user login form item label frameLayout
			FrameLayout _labelFrameLayout = (FrameLayout) findViewById(R.id.ssulfi_userLogin_formItem_label_frameLayout);

			// get its linearLayout params and update its percentage
			LayoutParams _labelFrameLayoutLayoutParams = (LayoutParams) _labelFrameLayout
					.getLayoutParams();
			_labelFrameLayoutLayoutParams.weight = _labelPecent;

			// set its linearLayout params
			_labelFrameLayout.setLayoutParams(_labelFrameLayoutLayoutParams);
		}

		// check smartsport user login form item input editText hint and set its
		// hint
		if (null != _inputEditTextHint) {
			// set smartsport user login form item input editText hint
			userInputEditText.setHint(_inputEditTextHint);
		}

		// check smartsport user login form item input editText input type and
		// set its type
		if (0 != _inputEditTextInputType) {
			// set smartsport user login form item input editText input type
			userInputEditText.setInputType(_inputEditTextInputType);
		}
	}

	// inner class
	/**
	 * @name ISSULFIUserInputEditTextTextWatcher
	 * @descriptor smartsport user login form item user input editText text
	 *             watcher interface
	 * @author Ares
	 * @version 1.0
	 */
	public static interface ISSULFIUserInputEditTextTextWatcher {

		/**
		 * @title beforeTextChanged
		 * @descriptor before smartsport user login form item user input
		 *             editText text changed
		 * @param userLoginFormItem
		 *            : smartsport user login form item
		 * @param s
		 *            : user input editText text
		 * @param start
		 *            : user input editText text changed start index
		 * @param count
		 *            : user input editText text changed count
		 * @param after
		 *            : user input editText text after changed index
		 * @author Ares
		 */
		public void beforeTextChanged(SSUserLoginFormItem userLoginFormItem,
				CharSequence s, int start, int count, int after);

		/**
		 * @title onTextChanged
		 * @descriptor on smartsport user login form item user input editText
		 *             text changed
		 * @param userLoginFormItem
		 *            : smartsport user login form item
		 * @param s
		 *            : user input editText text
		 * @param start
		 *            : user input editText text changed start index
		 * @param before
		 *            : user input editText text bafore changed index
		 * @param count
		 *            : user input editText text changed count
		 * @author Ares
		 */
		public void onTextChanged(SSUserLoginFormItem userLoginFormItem,
				CharSequence s, int start, int before, int count);

		/**
		 * @title afterTextChanged
		 * @descriptor after smartsport user login form item user input editText
		 *             text changed
		 * @param userLoginFormItem
		 *            : smartsport user login form item
		 * @param s
		 *            : user input editText text
		 * @author Ares
		 */
		public void afterTextChanged(SSUserLoginFormItem userLoginFormItem,
				Editable s);

	}

	/**
	 * @name UserInputEditTextTextWatcher
	 * @descriptor user login form item user input editText text changed watcher
	 * @author Ares
	 * @version 1.0
	 */
	class UserInputEditTextTextWatcher implements TextWatcher {

		// logger
		private final SSLogger LOGGER = new SSLogger(
				UserInputEditTextTextWatcher.class);

		// user login form item user input editText text watcher
		private ISSULFIUserInputEditTextTextWatcher userLoginFormItemUserInputEditTextTextWatcher;

		/**
		 * @title UserInputEditTextTextWatcher
		 * @descriptor user login form item user input editText text changed
		 *             watcher constructor with user login form item user input
		 *             editText text watcher
		 * @param userLoginFormItemUserInputEditTextTextWatcher
		 *            : user login form item user input editText text watcher
		 */
		public UserInputEditTextTextWatcher(
				ISSULFIUserInputEditTextTextWatcher userLoginFormItemUserInputEditTextTextWatcher) {
			super();

			// save user login form item user input editText text watcher
			this.userLoginFormItemUserInputEditTextTextWatcher = userLoginFormItemUserInputEditTextTextWatcher;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// check user login form item user input editText text watcher and
			// then set it before text changed
			if (null != userLoginFormItemUserInputEditTextTextWatcher) {
				userLoginFormItemUserInputEditTextTextWatcher
						.beforeTextChanged(SSUserLoginFormItem.this, s, start,
								count, after);
			} else {
				LOGGER.warning("User login form item user input editText text watcher is null, not throw up method:(void)beforeTextChanged(CharSequence, int, int, int)");
			}
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// check user login form item user input editText text watcher and
			// then set it on text changed
			if (null != userLoginFormItemUserInputEditTextTextWatcher) {
				userLoginFormItemUserInputEditTextTextWatcher.onTextChanged(
						SSUserLoginFormItem.this, s, start, before, count);
			} else {
				LOGGER.warning("User login form item user input editText text watcher is null, not throw up method:(void)onTextChanged(CharSequence, int, int, int)");
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// check user login form item user input editText text watcher and
			// then set it after text changed
			if (null != userLoginFormItemUserInputEditTextTextWatcher) {
				userLoginFormItemUserInputEditTextTextWatcher.afterTextChanged(
						SSUserLoginFormItem.this, s);
			} else {
				LOGGER.warning("User login form item user input editText text watcher is null, not throw up method:(void)afterTextChanged(Editable)");
			}
		}

	}

}
