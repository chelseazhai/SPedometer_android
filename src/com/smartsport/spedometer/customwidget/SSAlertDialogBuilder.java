/**
 * 
 */
package com.smartsport.spedometer.customwidget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SSAlertDialogBuilder
 * @descriptor smartsport alert dialog builder
 * @author Ares
 * @version 1.0
 */
public class SSAlertDialogBuilder extends Builder {

	// layout inflate
	private final LayoutInflater LAYOUTINFLATER = (LayoutInflater) getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	// smartsport alert dialog positive and neutral button foreground color span
	private final ForegroundColorSpan POSITIVEBTN_FOREGROUNDCOLOR_SPAN = new ForegroundColorSpan(
			SSApplication.getContext().getResources()
					.getColor(android.R.color.white));
	private final ForegroundColorSpan NEUTRALBTN_FOREGROUNDCOLOR_SPAN = new ForegroundColorSpan(
			SSApplication.getContext().getResources()
					.getColor(android.R.color.black));

	// smartsport alert dialog header relativeLayout, icon imageView, title
	// textView, content viewGroup, message textView, operate positive or
	// neutral and negative button
	private RelativeLayout headerRelativeLayout;
	private ImageView iconImgView;
	private TextView titleTextView;
	private ViewGroup contentViewGroup;
	private TextView messageTextView;
	private Button positiveOrNeutralBtn;
	private Button negativeBtn;

	// smartsport alert dialog icon drawable, title, message, positive or
	// neutral, negative button text and their on click listener
	private Drawable iconDrawable;
	private String title;
	private String message;
	private SpannableString positiveOrNeutralBtnText;
	private OnClickListener positiveOrNeutralBtnOnClickListener;
	private String negativeBtnText;
	private OnClickListener negativeBtnOnClickListener;

	// smartsport alert dialog custom view layout resource id
	private Integer customViewLayoutResId;

	/**
	 * @title SSAlertDialogBuilder
	 * @descriptor smartsport alert dialog builder constructor with context and
	 *             theme
	 * @param context
	 *            : context
	 * @param theme
	 *            : theme
	 * @author Ares
	 */
	public SSAlertDialogBuilder(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * @title SSAlertDialogBuilder
	 * @descriptor smartsport alert dialog builder constructor with context
	 * @param context
	 *            : context
	 * @author Ares
	 */
	public SSAlertDialogBuilder(Context context) {
		// new smartsport alert dialog using default theme
		// super(context, R.style.SSAlertDialogTheme);
		// test by ares
		super(context);
	}

	@Override
	public Builder setIcon(int iconId) {
		return this.setIcon(getContext().getResources().getDrawable(iconId));
	}

	@Override
	public Builder setIcon(Drawable icon) {
		// save smartsport alert dialog icon drawable
		iconDrawable = icon;

		// clear super alert dialog icon
		return super.setIcon(null);
	}

	@Override
	public Builder setTitle(int titleId) {
		return this.setTitle(getContext().getString(titleId));
	}

	@Override
	public Builder setTitle(CharSequence title) {
		// save smartsport alert dialog title
		this.title = (String) title;

		// clear super alert dialog title
		return super.setTitle(null);
	}

	@Override
	public Builder setMessage(int messageId) {
		return this.setMessage(getContext().getString(messageId));
	}

	@Override
	public Builder setMessage(CharSequence message) {
		// save smartsport alert dialog message
		this.message = (String) message;

		// clear super alert dialog message
		return super.setMessage(null);
	}

	@Override
	public Builder setPositiveButton(int textId, OnClickListener listener) {
		return this.setPositiveButton(getContext().getString(textId), listener);
	}

	@Override
	public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
		// save smartsport alert dialog positive button text and its on click
		// listener
		positiveOrNeutralBtnText = new SpannableString(text);
		positiveOrNeutralBtnText.setSpan(POSITIVEBTN_FOREGROUNDCOLOR_SPAN, 0,
				positiveOrNeutralBtnText.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		positiveOrNeutralBtnOnClickListener = listener;

		// clear super alert dialog positive button text and its on click
		// listener
		return super.setPositiveButton(null, null);
	}

	@Override
	public Builder setNeutralButton(int textId, OnClickListener listener) {
		return this.setNeutralButton(getContext().getString(textId), listener);
	}

	@Override
	public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
		// save smartsport alert dialog neutral button text and its on click
		// listener
		positiveOrNeutralBtnText = new SpannableString(text);
		positiveOrNeutralBtnText.setSpan(NEUTRALBTN_FOREGROUNDCOLOR_SPAN, 0,
				positiveOrNeutralBtnText.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		positiveOrNeutralBtnOnClickListener = listener;

		// clear super alert dialog neutral button text and its on click
		// listener
		return super.setNeutralButton(null, null);
	}

	@Override
	public Builder setNegativeButton(int textId, OnClickListener listener) {
		return this.setNegativeButton(getContext().getString(textId), listener);
	}

	@Override
	public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
		// save smartsport alert dialog negative button text and its on click
		// listener
		negativeBtnText = (String) text;
		negativeBtnOnClickListener = listener;

		// clear super alert dialog negative button text and its on click
		// listener
		return super.setNegativeButton(null, null);
	}

	@Override
	public AlertDialog create() {
		// get smartsport alert dialog view and set as smartsport alert dialog
		// builder view
		ViewGroup _ssAlertDialogView = (ViewGroup) LAYOUTINFLATER.inflate(
				R.layout.ss_alertdialog_layout, null);
		super.setView(_ssAlertDialogView);

		// check smartsport alert dialog icon
		if (null != iconDrawable) {
			// check smartsport alert dialog header relativeLayout and show
			if (null == headerRelativeLayout) {
				// get smartsport alert dialog header relativeLayout
				headerRelativeLayout = (RelativeLayout) _ssAlertDialogView
						.findViewById(R.id.ssad_header_relativeLayout);
			}
			headerRelativeLayout.setVisibility(View.VISIBLE);

			// get smartsport alert dialog icon imageView
			iconImgView = (ImageView) _ssAlertDialogView
					.findViewById(R.id.ssad_icon_imageView);

			// set icon imageView image drawable and show it
			iconImgView.setImageDrawable(iconDrawable);
			iconImgView.setVisibility(View.VISIBLE);
		}

		// check smartsport alert dialog title
		if (null != title) {
			// check smartsport alert dialog header relativeLayout and show
			if (null == headerRelativeLayout) {
				// get smartsport alert dialog header relativeLayout
				headerRelativeLayout = (RelativeLayout) _ssAlertDialogView
						.findViewById(R.id.ssad_header_relativeLayout);
			}
			headerRelativeLayout.setVisibility(View.VISIBLE);

			// get smartsport alert dialog title textView
			titleTextView = (TextView) _ssAlertDialogView
					.findViewById(R.id.ssad_title_textView);

			// set title textView text and show it
			titleTextView.setText(title);
			titleTextView.setVisibility(View.VISIBLE);
		}

		// get smartsport content viewGroup and message textView
		contentViewGroup = (ViewGroup) _ssAlertDialogView
				.findViewById(R.id.ssad_contentView);
		messageTextView = (TextView) contentViewGroup
				.findViewById(R.id.ssad_message_textView);
		// check smartsport alert dialog message and custom view layout resource
		// id
		if (null != message) {
			// set message textView text and show content viewGroup
			messageTextView.setText(message);
			contentViewGroup.setVisibility(View.VISIBLE);
		} else if (null != customViewLayoutResId) {
			// add custom view as subView to content view
			LAYOUTINFLATER.inflate(customViewLayoutResId, contentViewGroup);

			// hide message textView and show content viewGroup
			messageTextView.setVisibility(View.GONE);
			contentViewGroup.setVisibility(View.VISIBLE);
		}

		// generate smartsport alert dialog operate button on click listener
		SSAlertDialogOperateBtnOnClickListener _operateBtnOnClickListener = new SSAlertDialogOperateBtnOnClickListener();

		// get smartsport alert dialog positive or neutral button
		positiveOrNeutralBtn = (Button) _ssAlertDialogView
				.findViewById(R.id.ssad_positiveOrNeutral_button);
		// check smartsport alert dialog positive or neutral button text
		if (null != positiveOrNeutralBtnText) {
			// set positive or neutral button text and its on click listener
			positiveOrNeutralBtn.setText(positiveOrNeutralBtnText);
			positiveOrNeutralBtn.setOnClickListener(_operateBtnOnClickListener);

			// get and check positive or neutral button text spannable string
			// foreground color spans
			ForegroundColorSpan[] _foregroundColorSpans = positiveOrNeutralBtnText
					.getSpans(0, positiveOrNeutralBtnText.length(),
							ForegroundColorSpan.class);
			if (null != _foregroundColorSpans
					&& 0 < _foregroundColorSpans.length) {
				// get foreground color and update positive or neutral button
				// background
				int _foregroundColor = _foregroundColorSpans[0]
						.getForegroundColor();
				if (getContext().getResources().getColor(android.R.color.white) == _foregroundColor) {
					positiveOrNeutralBtn
							.setBackgroundResource(R.drawable.ss_alertdialog_positive_button_bg);
				} else if (getContext().getResources().getColor(
						android.R.color.black) == _foregroundColor) {
					positiveOrNeutralBtn
							.setBackgroundResource(R.drawable.ss_alertdialog_neutral_button_bg);
				}
			}
		}

		// check smartsport alert dialog negative button text
		if (null != negativeBtnText) {
			// get smartsport alert dialog negative button
			negativeBtn = (Button) _ssAlertDialogView
					.findViewById(R.id.ssad_negative_button);

			// set negative button text, its on click listener and show it
			negativeBtn.setText(negativeBtnText);
			negativeBtn.setOnClickListener(_operateBtnOnClickListener);
			negativeBtn.setVisibility(View.VISIBLE);
		}

		// create smartsport alert dialog and then set it as operate button on
		// click listener alert dialog
		final AlertDialog _alertDialog = super.create();
		_operateBtnOnClickListener.setAlertDialog(_alertDialog);

		// check smartsport alert dialog positive or neutral button text again
		if (null == positiveOrNeutralBtnText) {
			// set negative button on click listener
			positiveOrNeutralBtn
					.setOnClickListener(new android.view.View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// dismiss smartsport alert dialog
							_alertDialog.dismiss();
						}

					});
		}

		return _alertDialog;
	}

	/**
	 * @title setView
	 * @descriptor set a custom view to be the contents of the dialog
	 * @param layoutResID
	 *            : the custom view layout resource id
	 * @return this builder object to allow for chaining of calls to set methods
	 * @author Ares
	 */
	public Builder setView(int layoutResID) {
		// save custom view layout resource id
		customViewLayoutResId = layoutResID;

		return super.setView(LAYOUTINFLATER.inflate(layoutResID, null));
	}

	/**
	 * @title showTip
	 * @descriptor show smartsport alert dialog header tip
	 * @return this builder object to allow for chaining of calls to set methods
	 * @author Ares
	 */
	public Builder showTip() {
		// set icon and title
		setIcon(R.drawable.ic_launcher);
		setTitle(R.string.ssad_default_title_tip);

		return this;
	}

	// inner class
	/**
	 * @name SSAlertDialogOperateBtnOnClickListener
	 * @descriptor smartsport alert dialog operate button on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class SSAlertDialogOperateBtnOnClickListener implements
			android.view.View.OnClickListener {

		// logger
		private final SSLogger LOGGER = new SSLogger(
				SSAlertDialogOperateBtnOnClickListener.class);

		// smartsport alert dialog
		private AlertDialog ssAlertDialog;

		public void setAlertDialog(AlertDialog alertDialog) {
			ssAlertDialog = alertDialog;
		}

		@Override
		public void onClick(View v) {
			// check smartsport alert dialog
			if (null != ssAlertDialog) {
				// check operate button id
				switch (v.getId()) {
				case R.id.ssad_negative_button:
					// cancel smartsport alert dialog
					ssAlertDialog.cancel();

					// check smartsport alert dialog negative button on click
					// listener and perform it
					if (null != negativeBtnOnClickListener) {
						negativeBtnOnClickListener.onClick(ssAlertDialog,
								v.getId());
					} else {
						LOGGER.warning("Not need to throw up smartsport alert dialog negative button on click event");
					}
					break;

				case R.id.ssad_positiveOrNeutral_button:
				default:
					// dismiss smartsport alert dialog
					ssAlertDialog.dismiss();

					// check smartsport alert dialog positive or neutral button
					// on click listener and perform it
					if (null != positiveOrNeutralBtnOnClickListener) {
						positiveOrNeutralBtnOnClickListener.onClick(
								ssAlertDialog, v.getId());
					} else {
						LOGGER.warning("Not need to throw up smartsport alert dialog positive or neutral button on click event");
					}
					break;
				}
			} else {
				LOGGER.error("Smartsport alert dialog operate button clicked error, its alert dialog is null");
			}
		}

	}

}
