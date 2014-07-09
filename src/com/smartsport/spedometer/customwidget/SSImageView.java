/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSImageView
 * @descriptor smartsport imageView
 * @author Ares
 * @version 1.0
 */
public class SSImageView extends ImageView {

	// logger
	private static final SSLogger LOGGER = new SSLogger(SSImageView.class);

	// image view radius
	private float imgViewRadius;

	/**
	 * @title SSImageView
	 * @descriptor smartsport image view constructor with context, attributes
	 *             and default style
	 * @param context
	 *            : context
	 * @param attrs
	 *            : widget attributes
	 * @param defStyle
	 *            : widget default style
	 * @author Ares
	 */
	public SSImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// initialize smartsport image view attributes
		initAttrs(context, attrs);
	}

	/**
	 * @title SSImageView
	 * @descriptor smartsport image view constructor with context and attributes
	 * @param context
	 *            : context
	 * @param attrs
	 *            : widget attributes
	 * @author Ares
	 */
	public SSImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// initialize smartsport image view attributes
		initAttrs(context, attrs);
	}

	/**
	 * @title SSImageView
	 * @descriptor smartsport image view constructor with context
	 * @param context
	 *            : context
	 * @author Ares
	 */
	public SSImageView(Context context) {
		super(context);

		// initialize smartsport image view attributes
		initAttrs(context, null);
	}

	@Override
	public void setImageResource(int resId) {
		// this.setImageDrawable(getResources().getDrawable(resId));

		// test by ares
		this.setImageBitmap(((BitmapDrawable) getResources().getDrawable(resId))
				.getBitmap());
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		// // check image drawable
		// if (null != drawable && drawable instanceof BitmapDrawable) {
		// this.setImageBitmap(((BitmapDrawable) drawable).getBitmap());
		// } else {
		// super.setImageDrawable(drawable);
		// }

		// test by ares
		super.setImageDrawable(drawable);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// check image view radius
		if (0 < imgViewRadius) {
			// bitmap rounded corner
			try {
				// create rounded corner bitmap
				Bitmap _roundedCornerBitmap = Bitmap.createBitmap(
						bm.getWidth(), bm.getHeight(), Config.ARGB_8888);

				// get paint canvas
				Canvas _paintCanvas = new Canvas(_roundedCornerBitmap);

				// create paint and set anti alias
				Paint _paint = new Paint();
				_paint.setAntiAlias(true);

				// create draw rectangle and its holds
				Rect _drawRect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
				RectF _drawRectHolds = new RectF(_drawRect);

				// clear paint canvas and draw round rectangle
				_paintCanvas.drawARGB(0, 0, 0, 0);
				_paintCanvas.drawRoundRect(_drawRectHolds, imgViewRadius,
						imgViewRadius, _paint);

				// set paint xfermode
				_paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

				// draw bitmap
				_paintCanvas.drawBitmap(bm, _drawRect, _drawRect, _paint);

				// reset bitmap using rounded corner bitmap
				bm = _roundedCornerBitmap;
			} catch (IllegalArgumentException e) {
				LOGGER.error("Bitmap = " + bm
						+ " rounded corner error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}
		}

		super.setImageBitmap(bm);
	}

	@Override
	public void setImageURI(Uri uri) {
		// check uri
		if (null != uri) {
			LOGGER.info("uri scheme = " + uri.getScheme());

			//

			// test by ares
			this.setImageResource(R.drawable.img_test_walkresult);
		} else {
			super.setImageURI(uri);
		}
	}

	// initialize smartsport image view attributes
	private void initAttrs(Context context, AttributeSet attrs) {
		// define smartsport image view typedArray, attributes array, image view
		// background drawable
		TypedArray _typedArray = null;
		int[] _attrsArray = new int[] { android.R.attr.background };
		Drawable _backgroundDrawable = null;

		try {
			// get smartsport image view typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					_attrsArray, 0, 0);

			// get smartsport image view background drawable
			_backgroundDrawable = _typedArray
					.getDrawable(_attrsArray.length - 1);
		} catch (Exception e) {
			LOGGER.error("Get smartsport image view background drawable attributes error, exception massage = "
					+ e.getMessage());

			e.printStackTrace();
		} finally {
			// recycle smartsport image view typedArray
			if (null != _typedArray) {
				_typedArray.recycle();
			}
		}

		// check background drawable
		if (_backgroundDrawable instanceof GradientDrawable) {
			// test by ares
			//StateListDrawable
			
			try {
				// get gradient state class
				@SuppressWarnings("rawtypes")
				Class _gradientStateCls = Class.forName(GradientDrawable.class
						.getCanonicalName() + "$GradientState");

				// get radius field
				Field _radiusField = _gradientStateCls.getField("mRadius");

				// save background radius
				imgViewRadius = _radiusField
						.getFloat(((GradientDrawable) _backgroundDrawable)
								.getConstantState());
				LOGGER.info("@@, ImageView radius = " + imgViewRadius);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Found GradientState class error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				LOGGER.error("Found GradientState class member mRadius error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			} catch (IllegalAccessException e) {
				LOGGER.error("No permission to get GradientState class member mRadius value, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				LOGGER.error("Get GradientState class member mRadius value error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.warning("The smartsport image view background drawable is not gradient drawable");
		}
	}

}
