/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSActionSheet
 * @descriptor smartsport action sheet
 * @author Ares
 * @version 1.0
 */
public abstract class SSActionSheet extends PopupWindow {

	// logger
	private static final SSLogger LOGGER = new SSLogger(SSActionSheet.class);

	// layout inflater
	private static final LayoutInflater LAYOUTINFLATER = (LayoutInflater) SSApplication
			.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	// action sheet parent view(dependent)
	private View parentView;

	// action sheet content view(as viewGroup)
	private ViewGroup contentView;

	// action sheet(android popup window) animation style
	private final int ACTIONSHEET_ANIMATIONSTYLE = getAnimationStyle();

	// action sheet show and dismiss animation flag
	private boolean animationShowAndDismiss;

	/**
	 * @title SSActionSheet
	 * @descriptor smartsport action sheet constructor with layout resource,
	 *             width, height, focusable and use default action
	 * @param resource
	 *            : action sheet layout resource
	 * @param width
	 *            : action sheet width
	 * @param height
	 *            : action sheet height
	 * @param focusable
	 *            :
	 * @param defaultAction
	 *            :
	 * @author Ares
	 */
	public SSActionSheet(int resource, int width, int height,
			boolean focusable, boolean defaultAction) {
		super(LAYOUTINFLATER.inflate(R.layout.ss_actionsheet_layout, null),
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, focusable);

		// get content view(as viewGroup)
		contentView = (ViewGroup) getContentView();

		// add action sheet to content view
		// _contentView.addView(LAYOUTINFLATER.inflate(resource, null), width,
		// height);
		LAYOUTINFLATER.inflate(resource, contentView);

		// check if or not use default action
		if (defaultAction) {
			// initialize the content view on touch listener
			OnTouchListener _contentViewOnTouchListener = new ContentViewOnTouchListener();

			// set it as content view and its subview on touch listener
			contentView.setOnTouchListener(_contentViewOnTouchListener);
			for (int i = 0; i < contentView.getChildCount(); i++) {
				contentView.getChildAt(i).setOnTouchListener(
						_contentViewOnTouchListener);
			}
		}

		// initialize action sheet content view UI
		initContentViewUI();
	}

	/**
	 * @title SSActionSheet
	 * @descriptor smartsport action sheet constructor with layout resource,
	 *             width and height
	 * @param resource
	 *            : action sheet layout resource
	 * @param width
	 *            : action sheet width
	 * @param height
	 *            : action sheet height
	 * @author Ares
	 */
	public SSActionSheet(int resource, int width, int height) {
		this(resource, width, height, true, true);
	}

	/**
	 * @title SSActionSheet
	 * @descriptor smartsport action sheet constructor with layout resource
	 * @param resource
	 *            : action sheet layout resource
	 * @author Ares
	 */
	public SSActionSheet(int resource) {
		this(resource, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * @title findViewById
	 * @descriptor finds a view that was identified by the id attribute from the
	 *             XML that was processed in action sheet content view
	 * @param id
	 *            : the subview id attribute from the action sheet content view
	 *            XML
	 * @return the view if found or null otherwise
	 * @author Ares
	 */
	public View findViewById(int id) {
		return contentView.findViewById(id);
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		// save parent view
		parentView = parent;

		// reset animation style
		setAnimationStyle(ACTIONSHEET_ANIMATIONSTYLE);

		super.showAtLocation(parent, gravity, x, y);
	}

	/**
	 * @title showAtLocationAnimation
	 * @descriptor display the action sheet(content view in a popup window) at
	 *             the specified location
	 * @param parent
	 *            : a parent view for dependent
	 * @param gravity
	 *            : the gravity which controls the placement of the action sheet
	 * @param x
	 *            : the action sheet's x location offset
	 * @param y
	 *            : the action sheet's y location offset
	 * @author Ares
	 */
	public void showAtLocationAnimation(View parent, int gravity, int x, int y) {
		// save parent view
		parentView = parent;

		// set show and dismiss animation flag
		animationShowAndDismiss = true;

		// set default enter and exit animation
		setAnimationStyle(R.style.SSActionSheetAnimationPopupStyle);

		// set animation for all child views of content view for showing
		for (int i = 0; i < contentView.getChildCount(); i++) {
			contentView.getChildAt(i).startAnimation(
					AnimationUtils.loadAnimation(parent.getContext(),
							R.anim.actionsheet_slide_in_bottom));
		}

		super.showAtLocation(parent, gravity, x, y);
	}

	@Override
	public void dismiss() {
		// reset animation style
		setAnimationStyle(ACTIONSHEET_ANIMATIONSTYLE);

		// dismiss the action sheet and clear its content view UI cache
		dismissAndClear();
	}

	/**
	 * @title dismissAnimation
	 * @descriptor dispose of the action sheet(the popup window)
	 * @author Ares
	 */
	public void dismissAnimation() {
		// reset show and dismiss animation flag
		animationShowAndDismiss = false;

		// set default enter and exit animation
		setAnimationStyle(ACTIONSHEET_ANIMATIONSTYLE);

		// get the action sheet slide out bottom dismiss animation
		Animation _dismissAnimation = AnimationUtils.loadAnimation(
				parentView.getContext(), R.anim.actionsheet_slide_out_bottom);

		// set animation for all child views of content view for hiding
		for (int i = 0; i < contentView.getChildCount(); i++) {
			contentView.getChildAt(i).startAnimation(_dismissAnimation);
		}

		// dismiss the action sheet and clear its content view UI cache using
		// animation duration
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				dismissAndClear();
			}

		}, _dismissAnimation.getDuration());
	}

	/**
	 * @title getDismissDuration
	 * @descriptor get dismiss the action sheet duration time with using
	 *             animation flag
	 * @param usingAnimation
	 *            : dismiss the action sheet using animation or not
	 * @return dismiss the action sheet duration time
	 * @author Ares
	 */
	public long getDismissDuration(boolean usingAnimation) {
		long _duration = 0L;

		// check using animation flag
		if (usingAnimation) {
			_duration = AnimationUtils.loadAnimation(parentView.getContext(),
					R.anim.actionsheet_slide_out_bottom).getDuration();
		}

		return _duration;
	}

	/**
	 * @title initContentViewUI
	 * @descriptor initialize action sheet content view UI
	 * @author Ares
	 */
	protected abstract void initContentViewUI();

	/**
	 * @title clearContentViewUICache
	 * @descriptor clear action sheet content view UI cache
	 * @author Ares
	 */
	protected abstract void clearContentViewUICache();

	/**
	 * @title dismissAndClear
	 * @descriptor dismiss the action sheet and clear its content view UI cache
	 * @author Ares
	 */
	private void dismissAndClear() {
		// dismiss the action sheet
		super.dismiss();

		// clear the action sheet content view UI cache using an new handle
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				clearContentViewUICache();
			}

		}, 0);
	}

	// inner class
	/**
	 * @name ContentViewOnTouchListener
	 * @descriptor smartsport action sheet content view on touch listener
	 * @author Ares
	 * @version 1.0
	 */
	class ContentViewOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			boolean _eventConsumed = false;

			// check the view response the on touch event
			if (contentView.equals(v)) {
				// check the event action
				if (MotionEvent.ACTION_UP == event.getAction()) {
					// dismiss the action sheet animation if needed
					if (animationShowAndDismiss) {
						dismissAnimation();
					} else {
						dismiss();
					}
				}
			} else {
				LOGGER.debug("Action sheet not content view on touch, consumed the event");

				// consumed the on touch event
				_eventConsumed = true;
			}

			return _eventConsumed;
		}

	}

}
