/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import android.os.Handler;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.ImageView;

import com.smartsport.spedometer.network.NetworkEngine;
import com.smartsport.spedometer.network.handler.AsyncHttpRespBinaryHandler;
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

	// http and https uri scheme
	private static final String HTTPURI_SCHEME = "http";
	private static final String HTTPSURI_SCHEME = "https";

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
				// _paintCanvas.drawRoundRect(_drawRectHolds,
				// imgViewRadius, imgViewRadius, _paint);
				// test by ares
				_paintCanvas.drawRoundRect(_drawRectHolds,
						imgViewRadius * 0.84f, imgViewRadius * 0.84f, _paint);

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
			// get and check image uri scheme
			String _imgUriScheme = uri.getScheme();
			LOGGER.info("ImageView set image with uri, its scheme = "
					+ _imgUriScheme);
			if (HTTPURI_SCHEME.equalsIgnoreCase(_imgUriScheme)
					|| HTTPSURI_SCHEME.equalsIgnoreCase(_imgUriScheme)) {
				// // test by ares
				// this.setImageResource(R.drawable.img_test_walkresult);

				// image view load image
				AsyncImageLoader.getInstance().loadImg(this, uri.toString());
			} else {
				LOGGER.info("Not http image uri, use super method:(void)setImageURI(Uri)");

				super.setImageURI(uri);
			}
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
			// get gradient drawable radius
			getGradientDrawableRadius((GradientDrawable) _backgroundDrawable);
		} else if (_backgroundDrawable instanceof StateListDrawable
				&& _backgroundDrawable.getCurrent() instanceof GradientDrawable) {
			// get gradient drawable radius
			getGradientDrawableRadius((GradientDrawable) _backgroundDrawable
					.getCurrent());
		} else {
			LOGGER.warning("The smartsport image view background drawable is not gradient drawable");
		}
	}

	/**
	 * @title getGradientDrawableRadius
	 * @descriptor get image view gradient background drawable
	 * @param bgDrawable
	 *            : image view gradient background drawable
	 * @author Ares
	 */
	private void getGradientDrawableRadius(GradientDrawable bgDrawable) {
		try {
			// get gradient state class
			@SuppressWarnings("rawtypes")
			Class _gradientStateCls = Class.forName(GradientDrawable.class
					.getCanonicalName() + "$GradientState");

			// get radius field
			Field _radiusField = _gradientStateCls.getField("mRadius");

			// save background radius
			imgViewRadius = _radiusField
					.getFloat(((GradientDrawable) bgDrawable)
							.getConstantState());
			LOGGER.info("@@, ImageView background drawable radius = "
					+ imgViewRadius);
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
	}

	// inner class
	/**
	 * @name AsyncImageLoader
	 * @descriptor asynchronous image loader
	 * @author Ares
	 * @version 1.0
	 */
	static class AsyncImageLoader {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				AsyncImageLoader.class);

		// image view asynchronous image loader handle
		private final Handler ASYNCIMGLOADER_HANDLER = new Handler();

		// singleton instance
		private static volatile AsyncImageLoader _sSingletonAsyncImageLoader;

		// image memory cache
		private LruCache<String, SoftReference<Bitmap>> imgMemoryCache;

		// image loader thread pool
		private ExecutorService imgLoaderThreadPool;

		/**
		 * @title AsyncImageLoader
		 * @descriptor asynchronous image loader private constructor
		 * @author Ares
		 */
		private AsyncImageLoader() {
			super();

			// get the application max memory size and calculate the image cache
			// size(distribute 1/8 of application max memory size)
			int _cacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

			// initialize image memory cache
			imgMemoryCache = new LruCache<String, SoftReference<Bitmap>>(
					_cacheSize) {

				@Override
				protected int sizeOf(String key, SoftReference<Bitmap> value) {
					int _size = super.sizeOf(key, value);

					// get and check cache bitmap
					Bitmap _bitmap = value.get();
					if (null != _bitmap) {
						// re-calculate its size
						_size = _bitmap.getRowBytes() * _bitmap.getHeight();
					}

					return _size;
				}

			};

			// initialize image loader thread pool(using 10 threads)
			imgLoaderThreadPool = Executors.newFixedThreadPool(10);
		}

		/**
		 * @title getInstance
		 * @descriptor get asynchronous image loader singleton instance
		 * @return asynchronous image loader singleton instance
		 * @author Ares
		 */
		public static AsyncImageLoader getInstance() {
			if (null == _sSingletonAsyncImageLoader) {
				synchronized (AsyncImageLoader.class) {
					if (null == _sSingletonAsyncImageLoader) {
						_sSingletonAsyncImageLoader = new AsyncImageLoader();
					}
				}
			}

			return _sSingletonAsyncImageLoader;
		}

		/**
		 * @title loadImg
		 * @descriptor load image view image with image url
		 * @param imageView
		 *            : image view
		 * @param imageUrl
		 *            : image url
		 * @author Ares
		 */
		public void loadImg(final ImageView imageView, final String imageUrl) {
			// get and check image bitmap from image memory cache
			SoftReference<Bitmap> _memCacheBitmap = imgMemoryCache
					.get(imageUrl);
			if (null != _memCacheBitmap && null != _memCacheBitmap.get()) {
				// set image view bitmap
				imageView.setImageBitmap(_memCacheBitmap.get());
			} else {
				// load image view image with url
				imgLoaderThreadPool.submit(new Runnable() {

					@Override
					public void run() {
						ASYNCIMGLOADER_HANDLER.post(new Runnable() {

							@Override
							public void run() {
								// get image from remote server with url
								NetworkEngine.getInstance().getWithAPI(
										imageUrl,
										new AsyncHttpRespBinaryHandler() {

											@Override
											public void onSuccess(
													int statusCode,
													String filePath) {
												// nothing to do
											}

											@Override
											public void onSuccess(
													int statusCode,
													final Bitmap imgBitmap) {
												LOGGER.info("Doload image from remote server with url = "
														+ imageUrl
														+ " successful, status code = "
														+ statusCode
														+ " and the got image bitmap = "
														+ imgBitmap);

												// put the got image bitmap to
												// image memory cache
												imgMemoryCache
														.put(imageUrl,
																new SoftReference<Bitmap>(
																		imgBitmap));

												// check image view
												if (null != imageView) {
													// set image view image
													// bitmap
													imageView
															.setImageBitmap(imgBitmap);
												} else {
													LOGGER.warning("Set image view = "
															+ imageView
															+ " image error");

													//
												}
											}

											@Override
											public void onFailure(
													int statusCode,
													String errorMsg) {
												LOGGER.info("Doload image from remote server with url = "
														+ imageUrl
														+ " failed, status code = "
														+ statusCode
														+ " and error message = "
														+ errorMsg);
											}

										});
							}

						});
					}

				});
			}
		}

	}

}
