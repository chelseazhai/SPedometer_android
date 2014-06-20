/**
 * 
 */
package com.smartsport.spedometer.history;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatModel;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HistoryInfoActivity
 * @descriptor smartsport history info activity
 * @author Ares
 * @version 1.0
 */
public class HistoryInfoActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HistoryInfoActivity.class);

	// input method manager
	private InputMethodManager inputMethodManager;

	// pedometer login user
	private UserPedometerExtBean loginUser = (UserPedometerExtBean) UserManager
			.getInstance().getLoginUser();

	// group info and stranger pat model
	private GroupInfoModel groupInfoModel = GroupInfoModel.getInstance();
	private StrangerPatModel strangerPatModel = StrangerPatModel.getInstance();

	// user personal pedometer walk info, walk invite history group, within
	// group compete history group and pat stranger list view
	private View personalPedometerWalkInfoView;
	private View walkInviteHistoryGroupView;
	private View withinGroupCompeteHistoryGroupView;
	private View patStrangerView;

	// visible history info view
	private View visibleHistoryInfoView;

	// user personal pedometer walk info(search editText, search cancel button
	// and listView)
	private EditText personalPedometerWalkInfoSearchEditText;
	private Button personalPedometerWalkInfoSearchCancelBtn;
	private ListView personalPedometerWalkInfoListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get input method manager
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// set content view
		setContentView(R.layout.activity_history_info);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set title attributes
		setTitle(R.string.historyInfo_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get user history info segment redioGroup
		RadioGroup _userHistoryInfoSegRadioGroup = (RadioGroup) findViewById(R.id.hi_user_historyInfo_segment_radioGroup);

		// set its on checked change listener
		_userHistoryInfoSegRadioGroup
				.setOnCheckedChangeListener(new UserHistoryInfoSegRadioGroupOnCheckedChangeListener());

		// update the user selected history info(personal pedometer, walk invite
		// group, within group compete group and pat stranger)
		updateUserHistoryInfo(_userHistoryInfoSegRadioGroup
				.getCheckedRadioButtonId());
	}

	/**
	 * @title initPersonalPedometerWalkInfoView
	 * @descriptor initialize personal pedometer walk info list view UI
	 * @author Ares
	 */
	private void initPersonalPedometerWalkInfoView() {
		// get history info search editText
		personalPedometerWalkInfoSearchEditText = (EditText) personalPedometerWalkInfoView
				.findViewById(R.id.hi_common_historyInfo_search_editText);

		// set its on focus change listener
		personalPedometerWalkInfoSearchEditText
				.setOnFocusChangeListener(new PPWalkInfoSearchEditTextOnFocusChangeListener());

		// set its on touch listener
		personalPedometerWalkInfoSearchEditText
				.setOnTouchListener(new PPWalkInfoSearchEditTextOnTouchListener());

		// get clear search editText text drawable, set its bounds and set it as
		// history info search editText tag for saving
		Drawable _clearSearchEditTextTextDrawable = getResources().getDrawable(
				R.drawable.img_search_edittext_rightclear);
		_clearSearchEditTextTextDrawable.setBounds(0, 0,
				_clearSearchEditTextTextDrawable.getIntrinsicWidth(),
				_clearSearchEditTextTextDrawable.getIntrinsicHeight());
		personalPedometerWalkInfoSearchEditText
				.setTag(_clearSearchEditTextTextDrawable);

		// add its text changed listener
		personalPedometerWalkInfoSearchEditText
				.addTextChangedListener(new PPWalkInfoSearchEditTextTextWatcher());

		// set its on editor action listener
		personalPedometerWalkInfoSearchEditText
				.setOnEditorActionListener(new PPWalkInfoSearchEditTextOnEditorActionListener());

		// get history info cancel search button
		personalPedometerWalkInfoSearchCancelBtn = (Button) personalPedometerWalkInfoView
				.findViewById(R.id.hi_common_historyInfo_cancelSearch_button);

		// set its on click listener
		personalPedometerWalkInfoSearchCancelBtn
				.setOnClickListener(new PPWalkInfoCancelSearchBtnOnClickListener());

		//
	}

	/**
	 * @title updateUserHistoryInfo
	 * @descriptor update the user history info(personal pedometer, walk invite
	 *             group, within group compete group and pat stranger)
	 * @param checkedSegmentId
	 *            : checked user history info segment radioButton id
	 * @author Ares
	 */
	private void updateUserHistoryInfo(int checkedSegmentId) {
		// check the checked attendees walk trend segment radioButton id
		switch (checkedSegmentId) {
		case R.id.hi_personalPedometer_historyInfo_segment:
			// check, get personal pedometer walk info list view and then show
			// it
			if (null == personalPedometerWalkInfoView) {
				personalPedometerWalkInfoView = ((ViewStub) findViewById(R.id.hi_personalPedometer_historyInfo_viewStub))
						.inflate();

				// initialize personal pedometer walk info list view UI
				initPersonalPedometerWalkInfoView();
			} else {
				personalPedometerWalkInfoView.setVisibility(View.VISIBLE);
			}

			//

			// hide visible history info view if needed
			if (null != visibleHistoryInfoView) {
				visibleHistoryInfoView.setVisibility(View.GONE);
			}

			// update visible history info view
			visibleHistoryInfoView = personalPedometerWalkInfoView;
			break;

		case R.id.hi_walkInvite_historyGroup_segment:
			// check, get walk invite history group list view and then show it
			if (null == walkInviteHistoryGroupView) {
				walkInviteHistoryGroupView = ((ViewStub) findViewById(R.id.hi_walkInvite_historyGroup_viewStub))
						.inflate();

				//

				// get walk invite history groups
				groupInfoModel.getUserHistoryGroups(loginUser.getUserId(),
						loginUser.getUserKey(), GroupType.WALK_GROUP,
						new ICMConnector() {

							@Override
							public void onSuccess(Object... retValue) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								// TODO Auto-generated method stub

							}

						});
			} else {
				walkInviteHistoryGroupView.setVisibility(View.VISIBLE);
			}

			// hide visible history info view and if needed
			if (null != visibleHistoryInfoView) {
				visibleHistoryInfoView.setVisibility(View.GONE);
			}

			// update visible history info view
			visibleHistoryInfoView = walkInviteHistoryGroupView;
			break;

		case R.id.hi_withinGroupCompete_historyGroup_segment:
			// check, get within group compete list view and then show it
			if (null == withinGroupCompeteHistoryGroupView) {
				withinGroupCompeteHistoryGroupView = ((ViewStub) findViewById(R.id.hi_withinGroupCompete_historyGroup_viewStub))
						.inflate();

				//

				// get within group compete history groups
				groupInfoModel.getUserHistoryGroups(loginUser.getUserId(),
						loginUser.getUserKey(), GroupType.COMPETE_GROUP,
						new ICMConnector() {

							@Override
							public void onSuccess(Object... retValue) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								// TODO Auto-generated method stub

							}

						});
			} else {
				withinGroupCompeteHistoryGroupView.setVisibility(View.VISIBLE);
			}

			// hide visible history info view and if needed
			if (null != visibleHistoryInfoView) {
				visibleHistoryInfoView.setVisibility(View.GONE);
			}

			// update visible history info view
			visibleHistoryInfoView = withinGroupCompeteHistoryGroupView;
			break;

		case R.id.hi_strangerPat_historyPatStranger_segment:
			// check, get pat stranger list view and then show it
			if (null == patStrangerView) {
				patStrangerView = ((ViewStub) findViewById(R.id.hi_strangerPat_historyPatStranger_viewStub))
						.inflate();

				//

				// get pat strangers
				strangerPatModel.getPatStrangers(loginUser.getUserId(),
						loginUser.getUserKey(), new ICMConnector() {

							@Override
							public void onSuccess(Object... retValue) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								// TODO Auto-generated method stub

							}

						});
			} else {
				patStrangerView.setVisibility(View.VISIBLE);
			}

			// hide visible history info view if needed
			if (null != visibleHistoryInfoView) {
				visibleHistoryInfoView.setVisibility(View.GONE);
			}

			// update visible history info view
			visibleHistoryInfoView = patStrangerView;
			break;
		}
	}

	// inner class
	/**
	 * @name UserHistoryInfoSegRadioGroupOnCheckedChangeListener
	 * @descriptor user history info segment radioGroup on checked change
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class UserHistoryInfoSegRadioGroupOnCheckedChangeListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// update the user selected history info(personal pedometer, walk
			// invite group, within group compete group and pat stranger)
			updateUserHistoryInfo(checkedId);
		}

	}

	/**
	 * @name PPWalkInfoSearchEditTextOnFocusChangeListener
	 * @descriptor user personal pedometer walk info search editText on focus
	 *             change listener
	 * @author Ares
	 * @version 1.0
	 */
	class PPWalkInfoSearchEditTextOnFocusChangeListener implements
			OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// check user personal pedometer walk info search editText
			if (null != personalPedometerWalkInfoSearchEditText
					&& personalPedometerWalkInfoSearchEditText == v) {
				// check focusable
				if (hasFocus) {
					// show soft input
					inputMethodManager.showSoftInput(v, 0);

					// check and show user personal pedometer walk info search
					// cancel button if needed
					if (null != personalPedometerWalkInfoSearchCancelBtn
							&& View.VISIBLE != personalPedometerWalkInfoSearchCancelBtn
									.getVisibility()) {
						personalPedometerWalkInfoSearchCancelBtn
								.setVisibility(View.VISIBLE);
					}
				} else {
					// hide soft input
					inputMethodManager.hideSoftInputFromWindow(
							v.getWindowToken(), 0);
				}
			}
		}

	}

	/**
	 * @name PPWalkInfoSearchEditTextOnTouchListener
	 * @descriptor user personal pedometer walk info search editText on touch
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class PPWalkInfoSearchEditTextOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// check user personal pedometer walk info search editText and
			// motion event action
			if (null != personalPedometerWalkInfoSearchEditText
					&& personalPedometerWalkInfoSearchEditText == v
					&& MotionEvent.ACTION_DOWN == event.getAction()) {
				// set user personal pedometer walk info search editText
				// focusable
				personalPedometerWalkInfoSearchEditText.setFocusable(true);
				personalPedometerWalkInfoSearchEditText
						.setFocusableInTouchMode(true);

				// get, check clear user personal pedometer walk info search
				// editText text able and clear its text
				boolean _clearable = event.getX() > (personalPedometerWalkInfoSearchEditText
						.getWidth() - personalPedometerWalkInfoSearchEditText
						.getTotalPaddingRight())
						&& event.getX() < (personalPedometerWalkInfoSearchEditText
								.getWidth() - personalPedometerWalkInfoSearchEditText
								.getPaddingRight())
						&& event.getY() > personalPedometerWalkInfoSearchEditText
								.getPaddingTop()
						&& event.getY() < (personalPedometerWalkInfoSearchEditText
								.getHeight() - personalPedometerWalkInfoSearchEditText
								.getPaddingBottom());
				if (_clearable) {
					personalPedometerWalkInfoSearchEditText.setText("");
				}
			}

			return false;
		}
	}

	/**
	 * @name PPWalkInfoSearchEditTextTextWatcher
	 * @descriptor user personal pedometer walk info search editText text
	 *             watcher
	 * @author Ares
	 * @version 1.0
	 */
	class PPWalkInfoSearchEditTextTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// nothing to do
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// nothing to do
		}

		@Override
		public void afterTextChanged(Editable s) {
			// check history info search editText and its editable text
			if (null != personalPedometerWalkInfoSearchEditText) {
				// get user personal pedometer walk history info search editText
				// compound drawable array
				Drawable[] _compoundDrawables = personalPedometerWalkInfoSearchEditText
						.getCompoundDrawables();

				// reset its right drawable(index 3 for right)
				if (!"".equalsIgnoreCase(s.toString())) {
					// get and check history info search editText tag
					Object _tag = personalPedometerWalkInfoSearchEditText
							.getTag();
					if (null != _tag && _tag instanceof Drawable) {
						_compoundDrawables[2] = (Drawable) _tag;
					}
				} else {
					// clear
					_compoundDrawables[2] = null;
				}

				// update user personal pedometer walk history info search
				// editText compound drawables
				personalPedometerWalkInfoSearchEditText.setCompoundDrawables(
						_compoundDrawables[0], _compoundDrawables[1],
						_compoundDrawables[2], _compoundDrawables[3]);
			}
		}
	}

	/**
	 * @name PPWalkInfoSearchEditTextOnEditorActionListener
	 * @descriptor user personal pedometer walk info search editText on editor
	 *             action listener
	 * @author Ares
	 * @version 1.0
	 */
	class PPWalkInfoSearchEditTextOnEditorActionListener implements
			OnEditorActionListener {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// define return value
			boolean _retValue = false;

			// check history info search editText and ime action id
			if (null != personalPedometerWalkInfoSearchEditText
					&& EditorInfo.IME_ACTION_SEARCH == actionId) {
				// check history info search editText text
				if (!"".equalsIgnoreCase(personalPedometerWalkInfoSearchEditText
						.getText().toString())) {
					// set history info search editText unfocusable
					personalPedometerWalkInfoSearchEditText.setFocusable(false);

					// reset return value
					_retValue = true;
				} else {
					LOGGER.warning("No user input, needn't search");

					// show no user input, needn't search toast
					Toast.makeText(HistoryInfoActivity.this,
							R.string.toast_search_userInput_null,
							Toast.LENGTH_SHORT).show();
				}
			}

			return _retValue;
		}

	}

	/**
	 * @name PPWalkInfoCancelSearchBtnOnClickListener
	 * @descriptor user personal pedometer walk info cancel search button on
	 *             click listener
	 * @author Ares
	 * @version 1.0
	 */
	class PPWalkInfoCancelSearchBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// hide user personal pedometer walk info search cancel button
			v.setVisibility(View.GONE);

			// check, clear history info search editText text and set it
			// unfocusable
			if (null != personalPedometerWalkInfoSearchEditText) {
				personalPedometerWalkInfoSearchEditText.setText("");

				personalPedometerWalkInfoSearchEditText.setFocusable(false);
			}
		}

	}

}
