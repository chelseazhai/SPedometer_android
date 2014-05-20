package com.smartsport.spedometer.mvc;

import java.io.Serializable;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavBarButtonItem;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSBaseActivity
 * @descriptor smartsport base activity
 * @author Ares
 * @version 1.0
 */
public abstract class SSBaseActivity extends Activity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(SSBaseActivity.class);

	// smartsport base activity onCreate method parameter key
	private static final String NAV_ACTIVITY_PARAM_BACKBARBTNITEM_KEY = "nav_back_btn_default_title";
	private static final String NAV_ACTIVITY_PARAM_START4RESULT_KEY = "nav_activity_start4result";

	// smartsport base activity push navigation activity with request code, on
	// activity result map(key: request code and value: on activity result
	// interface)
	private SparseArray<ISSBaseActivityResult> NAV_PUSHWITHREQCODE_ONACTIVITYRS_MAP = new SparseArray<ISSBaseActivityResult>();

	// navigation bar
	private RelativeLayout navBar;

	// navigation bar back bar button item
	private SSBNavBarButtonItem backBarBtnItem;

	// navigation bar left and right bar button item
	private SSBNavBarButtonItem leftBarBtnItem;
	private SSBNavBarButtonItem rightBarBtnItem;

	// title textView and custom view
	private TextView titleTextView;
	private RelativeLayout titleCustomView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get and check the pushed activity back bar button item key from
			// intent extra data
			String _backBarBtnItemKey = _data
					.getString(NAV_ACTIVITY_PARAM_BACKBARBTNITEM_KEY);

			if (null != _backBarBtnItemKey) {
				// initialize the navigation bar back bar button item
				backBarBtnItem = new SSBNavImageBarButtonItem(this,
						R.drawable.img_navbar_backbarbtnitem,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// set activity start with request code as back
								// bar button item tag
								backBarBtnItem.setTag(_data
										.getBoolean(NAV_ACTIVITY_PARAM_START4RESULT_KEY));

								// perform back bar button item on click
								onBackBarButtonItemClick(backBarBtnItem);
							}

						});
			}
		}
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.activity_ss_base);

		// initialize navigation bar
		// get navigation bar
		navBar = (RelativeLayout) findViewById(R.id.ssb_navigationBar);

		// get navigation bar left and right bar button item
		leftBarBtnItem = (SSBNavBarButtonItem) findViewById(R.id.ssb_navBar_leftBarBtnItem);
		rightBarBtnItem = (SSBNavBarButtonItem) findViewById(R.id.ssb_navBar_rightBarBtnItem);

		// get title textView and custom view
		titleTextView = (TextView) findViewById(R.id.ssb_navBar_titleTextView);
		titleCustomView = (RelativeLayout) findViewById(R.id.ssb_navBar_titleCustomView_relativeLayout);

		// set parameter layout to navigation content frameLayout
		getLayoutInflater().inflate(layoutResID,
				(ViewGroup) findViewById(R.id.ssb_contentFrameLayout));

		// set navigation bar back button item, if needed(it is not null)
		if (null != backBarBtnItem) {
			setLeftBarButtonItem(backBarBtnItem);
		}

		// initialize activity content view UI
		initContentViewUI();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// get and check on activity result method with request code from the
		// navigation activity push with request code, on activity result map
		ISSBaseActivityResult _onActivityResult = NAV_PUSHWITHREQCODE_ONACTIVITYRS_MAP
				.get(requestCode);
		if (null != _onActivityResult) {
			// perform on activity result method
			_onActivityResult.onActivityResult(resultCode, data);
		} else {
			LOGGER.warning("Get on activity result interface with request code = "
					+ requestCode
					+ " from the navigation activity push with request code, on activity result map = "
					+ NAV_PUSHWITHREQCODE_ONACTIVITYRS_MAP
					+ " error, please use method onActivityResult(\"int, int, intent\") to catch the event when you want");

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * @title setNavbarBackgroundColor
	 * @descriptor set navigation bar background color
	 * @param color
	 *            : navigation bar background color
	 * @author Ares
	 */
	public void setNavbarBackgroundColor(int color) {
		navBar.setBackgroundColor(color);
	}

	/**
	 * @title setNavbarBackgroundResource
	 * @descriptor set navigation bar background resource
	 * @param resid
	 *            : navigation bar background resource id
	 * @author Ares
	 */
	public void setNavbarBackgroundResource(int resid) {
		navBar.setBackgroundResource(resid);
	}

	/**
	 * @title setLeftBarButtonItem
	 * @descriptor set navigation bar left bar button item
	 * @param leftBarBtnItem
	 *            : navigation bar left bar button item
	 * @author Ares
	 */
	public void setLeftBarButtonItem(SSBNavBarButtonItem leftBarBtnItem) {
		// check left bar button item
		if (null != leftBarBtnItem) {
			if (leftBarBtnItem instanceof SSBNavImageBarButtonItem) {
				// image bar button item
				// set image bar button item image
				this.leftBarBtnItem
						.setImage(((SSBNavImageBarButtonItem) leftBarBtnItem)
								.getImage());

				// set image bar button item on lick listener
				this.leftBarBtnItem.setOnClickListener(leftBarBtnItem
						.getOnClickListener());
			} else if (leftBarBtnItem instanceof SSBNavTitleBarButtonItem) {
				// title bar button item
				// set title bar button item text
				this.leftBarBtnItem
						.setText(((SSBNavTitleBarButtonItem) leftBarBtnItem)
								.getText());

				// get and check title bar button item text color
				int _titleTextColor = ((SSBNavTitleBarButtonItem) leftBarBtnItem)
						.getTextColor();
				if (0 != _titleTextColor) {
					// set title bar button item text color
					this.leftBarBtnItem.setTextColor(_titleTextColor);
				}

				// set title bar button item on lick listener
				this.leftBarBtnItem.setOnClickListener(leftBarBtnItem
						.getOnClickListener());
			} else {
				LOGGER.warning("Can't set navigation bar left bar button item, left bar button item = "
						+ leftBarBtnItem + " unrecognized");
			}
		} else {
			LOGGER.warning("Can't set navigation bar left bar button item, left bar button item is null");
		}
	}

	/**
	 * @title setRightBarButtonItem
	 * @descriptor set navigation bar right bar button item
	 * @param rightBarBtnItem
	 *            : navigation bar right bar button item
	 * @author Ares
	 */
	public void setRightBarButtonItem(SSBNavBarButtonItem rightBarBtnItem) {
		// check right bar button item
		if (null != rightBarBtnItem) {
			if (rightBarBtnItem instanceof SSBNavImageBarButtonItem) {
				// image bar button item
				// set image bar button item image
				this.rightBarBtnItem
						.setImage(((SSBNavImageBarButtonItem) rightBarBtnItem)
								.getImage());

				// set image bar button item on lick listener
				this.rightBarBtnItem.setOnClickListener(rightBarBtnItem
						.getOnClickListener());
			} else if (rightBarBtnItem instanceof SSBNavTitleBarButtonItem) {
				// title bar button item
				// set title bar button item text
				this.rightBarBtnItem
						.setText(((SSBNavTitleBarButtonItem) rightBarBtnItem)
								.getText());

				// get and check title bar button item text color
				int _titleTextColor = ((SSBNavTitleBarButtonItem) rightBarBtnItem)
						.getTextColor();
				if (0 != _titleTextColor) {
					// set title bar button item text color
					this.rightBarBtnItem.setTextColor(_titleTextColor);
				}

				// set title bar button item on lick listener
				this.rightBarBtnItem.setOnClickListener(rightBarBtnItem
						.getOnClickListener());
			} else {
				LOGGER.warning("Can't set navigation bar right bar button item, right bar button item = "
						+ rightBarBtnItem + " unrecognized");
			}
		} else {
			LOGGER.warning("Can't set navigation bar right bar button item, right bar button item is null");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		// set title textView text
		titleTextView.setText(title);

		super.setTitle(title);
	}

	@Override
	public void setTitle(int titleId) {
		// set title textView text
		titleTextView.setText(titleId);

		super.setTitle(titleId);
	}

	@Override
	public void setTitleColor(int textColor) {
		// set title textView text color
		titleTextView.setTextColor(textColor);

		super.setTitleColor(textColor);
	}

	/**
	 * @title setTitleSize
	 * @descriptor set title textView text size
	 * @param textSize
	 *            : title textView text size
	 * @author Ares
	 */
	public void setTitleSize(float textSize) {
		// set title textView text size
		titleTextView.setTextSize(textSize);
	}

	/**
	 * @title setShadow
	 * @descriptor set title textView text shadow
	 * @param radius
	 *            : title textView text shadow radius
	 * @param dx
	 *            : title textView text shadow dx
	 * @param dy
	 *            : title textView text shadow dy
	 * @param color
	 *            : title textView text shadow color
	 * @author Ares
	 */
	public void setShadow(float radius, float dx, float dy, int color) {
		// set title textView shadow layer
		titleTextView.setShadowLayer(radius, dx, dy, color);
	}

	/**
	 * @title setTitleView
	 * @descriptor set custom title view with fake title
	 * @param titleView
	 *            : custom title view
	 * @param title
	 *            : custom title view fake title
	 * @author Ares
	 */
	public void setTitleView(View titleView, String title) {
		// set custom title view fake title
		super.setTitle(title);

		// add custom title view to its relativeLayout
		titleCustomView.addView(titleView);
	}

	/**
	 * @title setTitleView
	 * @descriptor set custom title view
	 * @param titleView
	 *            : custom title view
	 * @author Ares
	 */
	public void setTitleView(View titleView) {
		// set custom title view
		setTitleView(titleView, getTitle().toString());
	}

	/**
	 * @title pushActivity
	 * @descriptor start activity with extra data and request code to navigation
	 *             activity stack
	 * @param activityCls
	 *            : target activity class
	 * @param extraData
	 *            : start activity extra data
	 * @param requestCode
	 *            : start activity request code
	 * @param onActivityResult
	 *            : on activity result interface
	 * @author Ares
	 */
	private void pushActivity(Class<? extends Activity> activityCls,
			Map<String, ?> extraData, Integer requestCode,
			ISSBaseActivityResult onActivityResult) {
		// define the target activity intent
		Intent _targetIntent = new Intent(this, activityCls);

		// set intent extra parameter data
		_targetIntent.putExtra(NAV_ACTIVITY_PARAM_BACKBARBTNITEM_KEY,
				(String) this.getTitle());

		// process extra data
		if (null != extraData) {
			for (String extraDataKey : extraData.keySet()) {
				// check extra data key, if it equals NAV_ACTIVITY_PARAM_KEY,
				// skip it
				if (NAV_ACTIVITY_PARAM_BACKBARBTNITEM_KEY
						.equalsIgnoreCase(extraDataKey)) {
					continue;
				}

				// get extra data value
				Object _extraDataKeyValue = extraData.get(extraDataKey);

				// check extra data value type
				if (_extraDataKeyValue instanceof Short) {
					_targetIntent.putExtra(extraDataKey,
							(Short) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Integer) {
					_targetIntent.putExtra(extraDataKey,
							(Integer) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Long) {
					_targetIntent.putExtra(extraDataKey,
							(Long) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Float) {
					_targetIntent.putExtra(extraDataKey,
							(Float) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Double) {
					_targetIntent.putExtra(extraDataKey,
							(Double) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Character) {
					_targetIntent.putExtra(extraDataKey,
							(Character) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Byte) {
					_targetIntent.putExtra(extraDataKey,
							(Byte) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Boolean) {
					_targetIntent.putExtra(extraDataKey,
							(Boolean) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof String) {
					_targetIntent.putExtra(extraDataKey,
							(String) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof CharSequence) {
					_targetIntent.putExtra(extraDataKey,
							(CharSequence) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Serializable) {
					_targetIntent.putExtra(extraDataKey,
							(Serializable) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Parcelable) {
					_targetIntent.putExtra(extraDataKey,
							(Parcelable) _extraDataKeyValue);
				} else {
					// others, not implementation now
					LOGGER.warning("Push activity, its class = "
							+ activityCls
							+ " with extra data value incompleted, extra data value type = "
							+ extraData.get(extraDataKey).getClass().getName()
							+ " not implementation now");
				}
			}
		}

		// check start activity request code and then go to the target activity
		if (null == requestCode) {
			startActivity(_targetIntent);
		} else {
			// set start for result key as target intent extra parameter data
			_targetIntent.putExtra(NAV_ACTIVITY_PARAM_START4RESULT_KEY, true);

			// check on activity result
			if (null != onActivityResult) {
				// add on activity result method to the navigation activity push
				// with request code, on activity result map
				NAV_PUSHWITHREQCODE_ONACTIVITYRS_MAP.put(requestCode,
						onActivityResult);
			}

			// start activity for result with request code
			startActivityForResult(_targetIntent, requestCode);
		}

		// set push activity animation
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	/**
	 * @title pushActivity
	 * @descriptor start activity with extra data to navigation activity stack
	 * @param activityCls
	 *            : target activity class
	 * @param extraData
	 *            : start activity extra data
	 * @author Ares
	 */
	public void pushActivity(Class<? extends Activity> activityCls,
			Map<String, ?> extraData) {
		pushActivity(activityCls, extraData, null, null);
	}

	/**
	 * @title pushActivity
	 * @descriptor start activity to navigation activity stack
	 * @param activityCls
	 *            : target activity class
	 * @author Ares
	 */
	public void pushActivity(Class<? extends Activity> activityCls) {
		pushActivity(activityCls, null);
	}

	/**
	 * @title pushActivityForResult
	 * @descriptor start activity for result with extra data and request code to
	 *             navigation activity stack
	 * @param activityCls
	 *            : target activity class
	 * @param extraData
	 *            : start activity for result extra data
	 * @param requestCode
	 *            : start activity for result request code
	 * @param onActivityResult
	 *            : on activity result interface
	 * @author Ares
	 */
	public void pushActivityForResult(Class<? extends Activity> activityCls,
			Map<String, ?> extraData, int requestCode,
			ISSBaseActivityResult onActivityResult) {
		pushActivity(activityCls, extraData, requestCode, onActivityResult);
	}

	/**
	 * @title pushActivityForResult
	 * @descriptor start activity for result with extra data and request code to
	 *             navigation activity stack
	 * @param activityCls
	 *            : target activity class
	 * @param extraData
	 *            : start activity for result extra data
	 * @param requestCode
	 *            : start activity for result request code
	 * @author Ares
	 */
	public void pushActivityForResult(Class<? extends Activity> activityCls,
			Map<String, ?> extraData, int requestCode) {
		pushActivityForResult(activityCls, extraData, requestCode, null);
	}

	/**
	 * @title pushActivityForResult
	 * @descriptor start activity for result with request code to navigation
	 *             activity stack
	 * @param activityCls
	 *            : target activity class
	 * @param requestCode
	 *            : start activity for result request code
	 * @author Ares
	 */
	public void pushActivityForResult(Class<? extends Activity> activityCls,
			int requestCode) {
		pushActivityForResult(activityCls, null, requestCode);
	}

	/**
	 * @title popActivity
	 * @descriptor pop the activity from navigation activity stack
	 * @author Ares
	 */
	public void popActivity() {
		// finish the activity
		finish();

		// set pop activity animation
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	/**
	 * @title popActivityWithResult
	 * @descriptor pop the activity from navigation activity stack with result
	 *             code and extra data then previous activity using
	 *             onActivityResult method to process them
	 * @param resultCode
	 *            : pop activity with result code
	 * @param extraData
	 *            : pop activity with result extra data
	 * @author Ares
	 */
	public void popActivityWithResult(Integer resultCode,
			Map<String, ?> extraData) {
		// check and process extra data
		if (null != extraData) {
			for (String extraDataKey : extraData.keySet()) {
				// check extra data key, if it equals NAV_ACTIVITY_PARAM_KEY,
				// skip it
				if (NAV_ACTIVITY_PARAM_BACKBARBTNITEM_KEY
						.equalsIgnoreCase(extraDataKey)) {
					continue;
				}

				// get extra data value
				Object _extraDataKeyValue = extraData.get(extraDataKey);

				// check extra data value type
				if (_extraDataKeyValue instanceof Short) {
					getIntent().putExtra(extraDataKey,
							(Short) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Integer) {
					getIntent().putExtra(extraDataKey,
							(Integer) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Long) {
					getIntent().putExtra(extraDataKey,
							(Long) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Float) {
					getIntent().putExtra(extraDataKey,
							(Float) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Double) {
					getIntent().putExtra(extraDataKey,
							(Double) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Character) {
					getIntent().putExtra(extraDataKey,
							(Character) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Byte) {
					getIntent().putExtra(extraDataKey,
							(Byte) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Boolean) {
					getIntent().putExtra(extraDataKey,
							(Boolean) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof String) {
					getIntent().putExtra(extraDataKey,
							(String) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof CharSequence) {
					getIntent().putExtra(extraDataKey,
							(CharSequence) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Serializable) {
					getIntent().putExtra(extraDataKey,
							(Serializable) _extraDataKeyValue);
				} else {
					// others, not implementation now
					LOGGER.warning("Pop activity with result with extra data value incompleted, extra data value type = "
							+ extraData.get(extraDataKey).getClass().getName()
							+ " not implementation now");
				}
			}
		}

		// check the result code and set result
		if (null == resultCode) {
			setResult(RESULT_CANCELED, getIntent());
		} else {
			setResult(resultCode, getIntent());
		}

		// finish the activity
		finish();

		// set pop activity animation
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	/**
	 * @title popActivityWithResult
	 * @descriptor pop the activity from navigation activity stack with extra
	 *             data and result code: RESULT_CANCELED then previous activity
	 *             using onActivityResult method to process them
	 * @param extraData
	 *            : pop activity with result extra data
	 * @author Ares
	 */
	public void popActivityWithResult(Map<String, ?> extraData) {
		popActivityWithResult(null, extraData);
	}

	/**
	 * @title popActivityWithResult
	 * @descriptor pop the activity from navigation activity stack with result
	 *             code: RESULT_CANCELED then previous activity using
	 *             onActivityResult method to process them
	 * @author Ares
	 */
	public void popActivityWithResult() {
		popActivityWithResult(null);
	}

	/**
	 * @title onBackBarButtonItemClick
	 * @descriptor navigation bar back bar button item on click
	 * @param backBarBtnItem
	 *            : back bar button item
	 * @author Ares
	 */
	protected void onBackBarButtonItemClick(SSBNavBarButtonItem backBarBtnItem) {
		// get and check back bar button item tag as activity start with request
		// code
		Boolean _isPushedActivityStartWithReqCode = (Boolean) backBarBtnItem
				.getTag();
		if (null != _isPushedActivityStartWithReqCode
				&& true == _isPushedActivityStartWithReqCode) {
			popActivityWithResult();
		} else {
			popActivity();
		}
	}

	/**
	 * @title initContentViewUI
	 * @descriptor initialize activity content view UI
	 * @author Ares
	 */
	protected abstract void initContentViewUI();

}
