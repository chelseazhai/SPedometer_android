/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @name SSBNavImageBarButtonItem
 * @descriptor smartsport base activity navigation bar image bar button item
 * @author Ares
 * @version 1.0
 */
public class SSBNavImageBarButtonItem extends SSBNavBarButtonItem {

	// smartsport base activity navigation bar bar button item imageButton image
	// drawable
	private Drawable imageButtonImgDrawable;

	/**
	 * @title SSBNavTitleBarButtonItem
	 * @descriptor smartsport base activity navigation bar image bar button item
	 *             constructor with context
	 * @param context
	 *            : context
	 */
	public SSBNavImageBarButtonItem(Context context) {
		super(context);
	}

	/**
	 * @title SSBNavTitleBarButtonItem
	 * @descriptor smartsport base activity navigation bar image bar button item
	 *             constructor with context, image resource id and on click
	 *             listener
	 * @param context
	 *            : context
	 * @param imageResId
	 *            : image resource id
	 * @param listener
	 *            : on click listener
	 */
	public SSBNavImageBarButtonItem(Context context, int imageResId,
			OnClickListener listener) {
		this(context);

		// save smartsport base activity navigation bar image bar button item
		// image and on click listener
		imageButtonImgDrawable = getResources().getDrawable(imageResId);
		onClickListener = listener;
	}

	/**
	 * @title getImage
	 * @descriptor get navigation bar bar button item image
	 * @return navigation bar bar button item image drawable
	 * @author Ares
	 */
	public Drawable getImage() {
		return imageButtonImgDrawable;
	}

	@Override
	public void setImage(int imageResId) {
		// save navigation bar bar button item image
		imageButtonImgDrawable = getResources().getDrawable(imageResId);

		super.setImage(imageResId);
	}

	@Override
	public void setImage(Drawable imageDrawable) {
		// save navigation bar bar button item image
		imageButtonImgDrawable = imageDrawable;

		super.setImage(imageDrawable);
	}

}
