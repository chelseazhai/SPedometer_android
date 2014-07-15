/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSBNavBarButtonItem
 * @descriptor smartsport base activity navigation bar bar button item
 * @author Ares
 * @version 1.0
 */
public class SSBNavBarButtonItem extends FrameLayout {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			SSBNavBarButtonItem.class);

	// smartsport base activity navigation bar bar button item title textView
	// and imageButton
	protected TextView titleTextView;
	protected ImageButton imageButton;

	// smartsport base activity navigation bar bar button item on click listener
	protected OnClickListener onClickListener;

	/**
	 * @title SSBNavBarButtonItem
	 * @descriptor smartsport base activity navigation bar bar button item
	 *             constructor with context, attributes and default style
	 * @param context
	 *            : context
	 * @param attrs
	 *            : widget attributes
	 * @param defStyle
	 *            : widget default style
	 */
	public SSBNavBarButtonItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// initialize smartsport base activity navigation bar bar button item
		// attributes
		initAttrs(context, attrs);
	}

	/**
	 * @title SSBNavBarButtonItem
	 * @descriptor smartsport base activity navigation bar bar button item
	 *             constructor with context and attributes
	 * @param context
	 *            : context
	 * @param attrs
	 *            : widget attributes
	 */
	public SSBNavBarButtonItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		// initialize smartsport base activity navigation bar bar button item
		// attributes
		initAttrs(context, attrs);
	}

	/**
	 * @title SSBNavBarButtonItem
	 * @descriptor smartsport base activity navigation bar bar button item
	 *             constructor with context
	 * @param context
	 *            : context
	 */
	public SSBNavBarButtonItem(Context context) {
		super(context);

		// initialize smartsport base activity navigation bar bar button item
		// attributes
		initAttrs(context, null);
	}

	/**
	 * @title setText
	 * @descriptor set navigation bar bar button item text
	 * @param text
	 *            : navigation bar bar button item text
	 * @author Ares
	 */
	public void setText(CharSequence text) {
		// set navigation bar bar button item title textView text
		titleTextView.setText(text);

		// show navigation bar bar button item if needed
		if (View.VISIBLE != getVisibility()) {
			setVisibility(View.VISIBLE);
		}
	}

	/**
	 * @title setTextColor
	 * @descriptor set navigation bar bar button item text color
	 * @param textColor
	 *            : navigation bar bar button item text color
	 * @author Ares
	 */
	public void setTextColor(int textColor) {
		// set navigation bar bar button item title textView text color
		titleTextView.setTextColor(textColor);
	}

	/**
	 * @title setImage
	 * @descriptor set navigation bar bar button item image
	 * @param imageDrawable
	 *            : navigation bar bar button item image drawable
	 * @author Ares
	 */
	public void setImage(Drawable imageDrawable) {
		// set navigation bar bar button item imageButton image drawable
		imageButton.setImageDrawable(imageDrawable);

		// show navigation bar bar button item if needed
		if (View.VISIBLE != getVisibility()) {
			setVisibility(View.VISIBLE);
		}
	}

	/**
	 * @title setImage
	 * @descriptor set navigation bar bar button item image
	 * @param imageResId
	 *            : navigation bar bar button item image resource id
	 * @author Ares
	 */
	public void setImage(int imageResId) {
		// set navigation bar bar button item imageButton image resource id
		setImage(getResources().getDrawable(imageResId));
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		// save navigation bar bar button item on click listener
		onClickListener = l;

		// check navigation bar bar button item on click listener and set it as
		// navigation bar bar button item imageButton on click listener
		if (null != l) {
			imageButton.setOnClickListener(l);
		}
	}

	/**
	 * @title initAttrs
	 * @descriptor initialize smartsport base activity navigation bar bar button
	 *             item attributes
	 * @param context
	 *            : context
	 * @param attrs
	 *            : attribute set
	 * @author Ares
	 */
	private void initAttrs(Context context, AttributeSet attrs) {
		// define smartsport base activity navigation bar bar button item
		// typedArray, bar button item text, text color, image resource and
		// background resource
		TypedArray _typedArray = null;
		String _text = null;
		int _textColor = 0;
		int _imageResourceId = 0;
		int _backgroundResourceId = 0;

		try {
			// get smartsport base activity navigation bar bar button item
			// typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.ssb_navBar_barButtonItem, 0, 0);

			// get smartsport base activity navigation bar bar button item text,
			// text color, image resource and background resource
			_text = _typedArray
					.getString(R.styleable.ssb_navBar_barButtonItem_text);
			_textColor = _typedArray.getColor(
					R.styleable.ssb_navBar_barButtonItem_textColor, 0);
			_imageResourceId = _typedArray.getResourceId(
					R.styleable.ssb_navBar_barButtonItem_src, 0);
			_backgroundResourceId = _typedArray.getResourceId(
					R.styleable.ssb_navBar_barButtonItem_background, 0);
		} catch (Exception e) {
			LOGGER.error("Get smartsport base activity navigation bar bar button item text, text color, image resource and background resource attributes error, exception massage = "
					+ e.getMessage());

			e.printStackTrace();
		} finally {
			// recycle smartsport base activity navigation bar bar button item
			// typedArray
			if (null != _typedArray) {
				_typedArray.recycle();
			}
		}

		// inflate smartsport base activity navigation bar bar button item
		// layout
		LayoutInflater.from(context).inflate(
				R.layout.ss_base_navbar_barbtnitem_layout, this);

		// get smartsport base activity navigation bar bar button item title
		// textView and imageButton
		titleTextView = (TextView) findViewById(R.id.ssb_navBar_barBtnItem_titleTextView);
		imageButton = (ImageButton) findViewById(R.id.ssb_navBar_barBtnItem);

		// check smartsport base activity navigation bar bar button item text
		// and text color then set its text attributes
		if (null != _text) {
			// set smartsport base activity navigation bar bar button item title
			// textView text
			titleTextView.setText(_text);
		}
		if (0 != _textColor) {
			// set smartsport base activity navigation bar bar button item title
			// textView text color
			titleTextView.setTextColor(_textColor);
		}

		// check smartsport base activity navigation bar bar button item image
		// resource and background then set its attributes
		if (0 != _imageResourceId) {
			// set smartsport base activity navigation bar bar button item
			// imageButton image resource
			imageButton.setImageResource(_imageResourceId);
		}
		if (0 != _backgroundResourceId) {
			// set smartsport base activity navigation bar bar button item
			// imageButton background resource
			imageButton.setBackgroundResource(_backgroundResourceId);
		}
	}

}
