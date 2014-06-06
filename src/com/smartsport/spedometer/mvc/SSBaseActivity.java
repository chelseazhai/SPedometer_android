package com.smartsport.spedometer.mvc;

import java.io.Serializable;
import java.util.HashMap;
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

	// smartsport base activity push navigation activity or present activity
	// with request code, on activity result map(key: request code and value: on
	// activity result interface)
	private SparseArray<ISSBaseActivityResult> NAVPUSH_PRESENT_WITHREQCODE_ONACTIVITYRS_MAP = new SparseArray<ISSBaseActivityResult>();

	// smartsport base activity push navigation target activity class and extra
	// data map keys
	private static final String NAV_TARGETACTIVITY_CLASS_KEY = "nav_targetActivity_class";
	private static final String NAV_TARGETACTIVITY_EXTRADATA_KEY = "nav_targetActivity_extraData";

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
		} else {
			leftBarBtnItem.setImage(null);
		}

		// initialize activity content view UI
		initContentViewUI();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// get and check on activity result method with request code from the
		// navigation activity push or the activity present with request code,
		// on activity result map
		ISSBaseActivityResult _onActivityResult = NAVPUSH_PRESENT_WITHREQCODE_ONACTIVITYRS_MAP
				.get(requestCode);

		if (null != _onActivityResult) {
			// check result code
			if (resultCode >= RESULT_FIRST_USER) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					// get navigation target activity class and extra data
					@SuppressWarnings("unchecked")
					Class<? extends Activity> _targetActivityCls = (Class<? extends Activity>) _extraData
							.getSerializable(NAV_TARGETACTIVITY_CLASS_KEY);
					@SuppressWarnings("unchecked")
					Map<String, ?> _targetActivityExtraData = (Map<String, ?>) _extraData
							.getSerializable(NAV_TARGETACTIVITY_EXTRADATA_KEY);

					// check navigation target activity class then start
					// activity with extra data and request code to navigation
					// activity stack
					if (null != _targetActivityCls) {
						pushActivity(_targetActivityCls,
								_targetActivityExtraData, requestCode,
								_onActivityResult);
					}
				} else {
					LOGGER.error("Go to navigation target activity error, the data is null");
				}
			} else {
				// perform on activity result method
				_onActivityResult.onActivityResult(resultCode, data);
			}
		} else {
			LOGGER.warning("Get on activity result interface with request code = "
					+ requestCode
					+ " from the navigation activity push or the activity present with request code, on activity result map = "
					+ NAVPUSH_PRESENT_WITHREQCODE_ONACTIVITYRS_MAP
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
			for (String _extraDataKey : extraData.keySet()) {
				// check extra data key, if it equals NAV_ACTIVITY_PARAM_KEY,
				// skip it
				if (NAV_ACTIVITY_PARAM_BACKBARBTNITEM_KEY
						.equalsIgnoreCase(_extraDataKey)) {
					continue;
				}

				// get extra data value
				Object _extraDataKeyValue = extraData.get(_extraDataKey);

				// check extra data value type
				if (_extraDataKeyValue instanceof Short) {
					_targetIntent.putExtra(_extraDataKey,
							(Short) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Integer) {
					_targetIntent.putExtra(_extraDataKey,
							(Integer) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Long) {
					_targetIntent.putExtra(_extraDataKey,
							(Long) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Float) {
					_targetIntent.putExtra(_extraDataKey,
							(Float) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Double) {
					_targetIntent.putExtra(_extraDataKey,
							(Double) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Character) {
					_targetIntent.putExtra(_extraDataKey,
							(Character) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Byte) {
					_targetIntent.putExtra(_extraDataKey,
							(Byte) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Boolean) {
					_targetIntent.putExtra(_extraDataKey,
							(Boolean) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof String) {
					_targetIntent.putExtra(_extraDataKey,
							(String) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof CharSequence) {
					_targetIntent.putExtra(_extraDataKey,
							(CharSequence) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Serializable) {
					_targetIntent.putExtra(_extraDataKey,
							(Serializable) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Parcelable) {
					_targetIntent.putExtra(_extraDataKey,
							(Parcelable) _extraDataKeyValue);
				} else {
					// others, not implementation now
					LOGGER.warning("Push activity, its class = "
							+ activityCls
							+ " with extra data value incompleted, extra data value type = "
							+ extraData.get(_extraDataKey).getClass().getName()
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
				NAVPUSH_PRESENT_WITHREQCODE_ONACTIVITYRS_MAP.put(requestCode,
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
	 * @title popPushActivity
	 * @descriptor pop the activity from navigation activity stack then start
	 *             another activity with extra data to navigation activity stack
	 * @param activityCls
	 *            : target activity class
	 * @param extraData
	 *            : start activity extra data
	 * @author Ares
	 */
	public void popPushActivity(Class<? extends Activity> activityCls,
			Map<String, ?> extraData) {
		// finish the activity
		finish();

		// start the target activity with extra data to navigation activity
		// stack
		pushActivity(activityCls, extraData, null, null);
	}

	/**
	 * @title popPushActivity
	 * @descriptor pop the activity from navigation activity stack then start
	 *             another activity to navigation activity stack
	 * @param activityCls
	 *            : target activity class
	 * @author Ares
	 */
	public void popPushActivity(Class<? extends Activity> activityCls) {
		popPushActivity(activityCls, null);
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
	 * @title popPushActivityForResult
	 * @descriptor pop the activity from navigation activity stack with result
	 *             code then start another activity for result with extra data
	 *             to navigation activity stack
	 * @param activityCls
	 *            : target activity class
	 * @param extraData
	 *            : start activity for result extra data
	 * @author Ares
	 */
	public void popPushActivityForResult(Class<? extends Activity> activityCls,
			Map<String, ?> extraData) {
		// save target activity class using intent extra bundle
		getIntent().putExtra(NAV_TARGETACTIVITY_CLASS_KEY, activityCls);

		// check and process extra data
		if (null != extraData) {
			// save target activity extra data hash map using intent extra
			// bundle
			getIntent().putExtra(NAV_TARGETACTIVITY_EXTRADATA_KEY,
					(HashMap<String, ?>) extraData);
		}

		// set result and then finish the activity
		setResult(RESULT_FIRST_USER, getIntent());
		finish();
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
			for (String _extraDataKey : extraData.keySet()) {
				// check extra data key, if it equals NAV_ACTIVITY_PARAM_KEY,
				// skip it
				if (NAV_ACTIVITY_PARAM_BACKBARBTNITEM_KEY
						.equalsIgnoreCase(_extraDataKey)) {
					continue;
				}

				// get extra data value
				Object _extraDataKeyValue = extraData.get(_extraDataKey);

				// check extra data value type
				if (_extraDataKeyValue instanceof Short) {
					getIntent().putExtra(_extraDataKey,
							(Short) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Integer) {
					getIntent().putExtra(_extraDataKey,
							(Integer) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Long) {
					getIntent().putExtra(_extraDataKey,
							(Long) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Float) {
					getIntent().putExtra(_extraDataKey,
							(Float) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Double) {
					getIntent().putExtra(_extraDataKey,
							(Double) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Character) {
					getIntent().putExtra(_extraDataKey,
							(Character) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Byte) {
					getIntent().putExtra(_extraDataKey,
							(Byte) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Boolean) {
					getIntent().putExtra(_extraDataKey,
							(Boolean) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof String) {
					getIntent().putExtra(_extraDataKey,
							(String) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof CharSequence) {
					getIntent().putExtra(_extraDataKey,
							(CharSequence) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Serializable) {
					getIntent().putExtra(_extraDataKey,
							(Serializable) _extraDataKeyValue);
				} else {
					// others, not implementation now
					LOGGER.warning("Pop activity with result with extra data value incompleted, extra data value type = "
							+ extraData.get(_extraDataKey).getClass().getName()
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
	 * @title presentActivity
	 * @descriptor start activity with extra data and request code
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
	private void presentActivity(Class<? extends Activity> activityCls,
			Map<String, ?> extraData, Integer requestCode,
			ISSBaseActivityResult onActivityResult) {
		// define the target activity intent
		Intent _targetIntent = new Intent(this, activityCls);

		// process extra data
		if (null != extraData) {
			for (String _extraDataKey : extraData.keySet()) {
				// get extra data value
				Object _extraDataKeyValue = extraData.get(_extraDataKey);

				// check extra data value type
				if (_extraDataKeyValue instanceof Short) {
					_targetIntent.putExtra(_extraDataKey,
							(Short) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Integer) {
					_targetIntent.putExtra(_extraDataKey,
							(Integer) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Long) {
					_targetIntent.putExtra(_extraDataKey,
							(Long) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Float) {
					_targetIntent.putExtra(_extraDataKey,
							(Float) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Double) {
					_targetIntent.putExtra(_extraDataKey,
							(Double) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Character) {
					_targetIntent.putExtra(_extraDataKey,
							(Character) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Byte) {
					_targetIntent.putExtra(_extraDataKey,
							(Byte) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Boolean) {
					_targetIntent.putExtra(_extraDataKey,
							(Boolean) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof String) {
					_targetIntent.putExtra(_extraDataKey,
							(String) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof CharSequence) {
					_targetIntent.putExtra(_extraDataKey,
							(CharSequence) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Serializable) {
					_targetIntent.putExtra(_extraDataKey,
							(Serializable) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Parcelable) {
					_targetIntent.putExtra(_extraDataKey,
							(Parcelable) _extraDataKeyValue);
				} else {
					// others, not implementation now
					LOGGER.warning("Present activity, its class = "
							+ activityCls
							+ " with extra data value incompleted, extra data value type = "
							+ extraData.get(_extraDataKey).getClass().getName()
							+ " not implementation now");
				}
			}
		}

		// check start activity request code and then go to the target activity
		if (null == requestCode) {
			startActivity(_targetIntent);
		} else {
			// check on activity result
			if (null != onActivityResult) {
				// add on activity result method to the activity present with
				// request code, on activity result map
				NAVPUSH_PRESENT_WITHREQCODE_ONACTIVITYRS_MAP.put(requestCode,
						onActivityResult);
			}

			// start activity for result with request code
			startActivityForResult(_targetIntent, requestCode);
		}

		// set present activity animation
		overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
	}

	/**
	 * @title presentActivity
	 * @descriptor start activity with extra data
	 * @param activityCls
	 *            : target activity class
	 * @param extraData
	 *            : start activity extra data
	 * @author Ares
	 */
	public void presentActivity(Class<? extends Activity> activityCls,
			Map<String, ?> extraData) {
		presentActivity(activityCls, extraData, null, null);
	}

	/**
	 * @title presentActivity
	 * @descriptor start activity
	 * @param activityCls
	 *            : target activity class
	 * @author Ares
	 */
	public void presentActivity(Class<? extends Activity> activityCls) {
		presentActivity(activityCls, null);
	}

	/**
	 * @title presentActivityForResult
	 * @descriptor start activity for result with extra data and request code
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
	public void presentActivityForResult(Class<? extends Activity> activityCls,
			Map<String, ?> extraData, int requestCode,
			ISSBaseActivityResult onActivityResult) {
		presentActivity(activityCls, extraData, requestCode, onActivityResult);
	}

	/**
	 * @title presentActivityForResult
	 * @descriptor start activity for result with extra data and request code
	 * @param activityCls
	 *            : target activity class
	 * @param extraData
	 *            : start activity for result extra data
	 * @param requestCode
	 *            : start activity for result request code
	 * @author Ares
	 */
	public void presentActivityForResult(Class<? extends Activity> activityCls,
			Map<String, ?> extraData, int requestCode) {
		presentActivityForResult(activityCls, extraData, requestCode, null);
	}

	/**
	 * @title presentActivityForResult
	 * @descriptor start activity for result with request code
	 * @param activityCls
	 *            : target activity class
	 * @param requestCode
	 *            : start activity for result request code
	 * @author Ares
	 */
	public void presentActivityForResult(Class<? extends Activity> activityCls,
			int requestCode) {
		presentActivityForResult(activityCls, null, requestCode);
	}

	/**
	 * @title dismissActivity
	 * @descriptor dismiss the activity
	 * @author Ares
	 */
	public void dismissActivity() {
		// finish the activity
		finish();

		// set dismiss activity animation
		overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom);
	}

	/**
	 * @title dismissActivityWithResult
	 * @descriptor dismiss the activity with result code and extra data then
	 *             previous activity using onActivityResult method to process
	 *             them
	 * @param resultCode
	 *            : dismiss activity with result code
	 * @param extraData
	 *            : dismiss activity with result extra data
	 * @author Ares
	 */
	public void dismissActivityWithResult(Integer resultCode,
			Map<String, ?> extraData) {
		// check and process extra data
		if (null != extraData) {
			for (String _extraDataKey : extraData.keySet()) {
				// get extra data value
				Object _extraDataKeyValue = extraData.get(_extraDataKey);

				// check extra data value type
				if (_extraDataKeyValue instanceof Short) {
					getIntent().putExtra(_extraDataKey,
							(Short) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Integer) {
					getIntent().putExtra(_extraDataKey,
							(Integer) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Long) {
					getIntent().putExtra(_extraDataKey,
							(Long) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Float) {
					getIntent().putExtra(_extraDataKey,
							(Float) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Double) {
					getIntent().putExtra(_extraDataKey,
							(Double) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Character) {
					getIntent().putExtra(_extraDataKey,
							(Character) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Byte) {
					getIntent().putExtra(_extraDataKey,
							(Byte) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Boolean) {
					getIntent().putExtra(_extraDataKey,
							(Boolean) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof String) {
					getIntent().putExtra(_extraDataKey,
							(String) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof CharSequence) {
					getIntent().putExtra(_extraDataKey,
							(CharSequence) _extraDataKeyValue);
				} else if (_extraDataKeyValue instanceof Serializable) {
					getIntent().putExtra(_extraDataKey,
							(Serializable) _extraDataKeyValue);
				} else {
					// others, not implementation now
					LOGGER.warning("Dismiss activity with result with extra data value incompleted, extra data value type = "
							+ extraData.get(_extraDataKey).getClass().getName()
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

		// set dismiss activity animation
		overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom);
	}

	/**
	 * @title dismissActivityWithResult
	 * @descriptor dismiss the activity with extra data and result code:
	 *             RESULT_CANCELED then previous activity using onActivityResult
	 *             method to process them
	 * @param extraData
	 *            : dismiss activity with result extra data
	 * @author Ares
	 */
	public void dismissActivityWithResult(Map<String, ?> extraData) {
		dismissActivityWithResult(null, extraData);
	}

	/**
	 * @title dismissActivityWithResult
	 * @descriptor dismiss the activity with result code: RESULT_CANCELED then
	 *             previous activity using onActivityResult method to process
	 *             them
	 * @author Ares
	 */
	public void dismissActivityWithResult() {
		dismissActivityWithResult(null);
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
