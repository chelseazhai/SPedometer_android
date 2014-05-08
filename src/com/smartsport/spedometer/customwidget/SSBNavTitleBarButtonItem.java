/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.content.Context;

/**
 * @name SSBNavTitleBarButtonItem
 * @descriptor smartsport base activity navigation bar title bar button item
 * @author Ares
 * @version 1.0
 */
public class SSBNavTitleBarButtonItem extends SSBNavBarButtonItem {

	// smartsport base activity navigation bar bar button item title textView
	// text color
	private int titleTextViewTextColor;

	/**
	 * @title SSBNavTitleBarButtonItem
	 * @descriptor smartsport base activity navigation bar title bar button item
	 *             constructor with context
	 * @param context
	 *            : context
	 */
	public SSBNavTitleBarButtonItem(Context context) {
		super(context);
	}

	/**
	 * @title SSBNavTitleBarButtonItem
	 * @descriptor smartsport base activity navigation bar title bar button item
	 *             constructor with context, title text, text color and on click
	 *             listener
	 * @param context
	 *            : context
	 * @param resId
	 *            : title resource id
	 * @param titleColor
	 *            : title color
	 * @param listener
	 *            : on click listener
	 */
	public SSBNavTitleBarButtonItem(Context context, int resId, int titleColor,
			OnClickListener listener) {
		this(context);

		// save smartsport base activity navigation bar title bar button item
		// title text, text color and on click listener
		titleTextView.setText(resId);
		titleTextViewTextColor = titleColor;
		onClickListener = listener;
	}

	/**
	 * @title SSBNavTitleBarButtonItem
	 * @descriptor smartsport base activity navigation bar title bar button item
	 *             constructor with context, title text and on click listener
	 * @param context
	 *            : context
	 * @param resId
	 *            : title resource id
	 * @param listener
	 *            : on click listener
	 */
	public SSBNavTitleBarButtonItem(Context context, int resId,
			OnClickListener listener) {
		this(context);

		// save smartsport base activity navigation bar title bar button item
		// title text and on click listener
		titleTextView.setText(resId);
		onClickListener = listener;
	}

	/**
	 * @title getText
	 * @descriptor get navigation bar bar button item text
	 * @return navigation bar bar button item text
	 * @author Ares
	 */
	public CharSequence getText() {
		// get navigation bar bar button item title textView text
		return titleTextView.getText();
	}

	/**
	 * @title getTextColor
	 * @descriptor get navigation bar bar button item text color
	 * @return navigation bar bar button item text color
	 * @author Ares
	 */
	public int getTextColor() {
		return titleTextViewTextColor;
	}

	@Override
	public void setTextColor(int textColor) {
		// save navigation bar bar button item title textView text color
		titleTextViewTextColor = textColor;

		super.setTextColor(textColor);
	}

}
