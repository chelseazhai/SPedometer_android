/**
 * 
 */
package com.smartsport.spedometer.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSSimpleAdapterViewBinder;
import com.smartsport.spedometer.group.GroupBean;
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.history.HistoryInfoActivity.PattedStrangerListViewAdapter.PattedStrangerListViewAdapterKey;
import com.smartsport.spedometer.history.walk.group.HistoryGroupListViewAdapter;
import com.smartsport.spedometer.history.walk.group.HistoryGroupListViewAdapter.HistoryGroupListViewAdapterKey;
import com.smartsport.spedometer.history.walk.group.HistoryGroupWalkInfoActivity;
import com.smartsport.spedometer.history.walk.group.HistoryGroupWalkInfoActivity.HistoryGroupWalkInfoExtraData;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatModel;
import com.smartsport.spedometer.strangersocial.pat.UserInfoPatLocationExtBean;
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

	// walk invite and within group compete history group list
	private List<GroupBean> walkInviteHistoryGroupList;
	private List<GroupBean> withinGroupCompeteHistoryGroupList;

	// history group listView adapter
	private HistoryGroupListViewAdapter historyGroupListViewAdapter;

	// history group listView on item click listener
	private HistoryGroupOnItemClickListener historyGroupOnItemClickListener;

	// stranger pat info(patted list and listView adapter)
	private List<UserInfoPatLocationExtBean> pattedStrangerList;
	private PattedStrangerListViewAdapter pattedStrangerListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get input method manager
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// initialize walk invite, within group compete history group list and
		// their listView adapter
		walkInviteHistoryGroupList = new ArrayList<GroupBean>();
		withinGroupCompeteHistoryGroupList = new ArrayList<GroupBean>();
		historyGroupListViewAdapter = new HistoryGroupListViewAdapter(
				this,
				null,
				R.layout.historygroup_listview_item_layout,
				new String[] {
						HistoryGroupListViewAdapterKey.HISTORYGROUP_TOPIC_KEY
								.name(),
						HistoryGroupListViewAdapterKey.HISTORYGROUP_WALKSTARTTIME_KEY
								.name(),
						HistoryGroupListViewAdapterKey.HISTORYGROUP_DURATIONTIME_LABEL_KEY
								.name(),
						HistoryGroupListViewAdapterKey.HISTORYGROUP_DURATIONTIME_KEY
								.name(),
						HistoryGroupListViewAdapterKey.HISTORYGROUP_ATTENDEESNUMRELATIVELAYOUT_KEY
								.name(),
						HistoryGroupListViewAdapterKey.HISTORYGROUP_ATTENDEESNUM_KEY
								.name() }, new int[] {
						R.id.hgi_groupTopic_textView,
						R.id.hgi_groupWalkStartTime_textView,
						R.id.hgi_groupDurationTime_label_textView,
						R.id.hgi_groupDurationTime_textView,
						R.id.hgi_groupAttendeesNum_relativeLayout,
						R.id.hgi_groupAttendeesNum_textView });

		// initialize history group listView on item click listener
		historyGroupOnItemClickListener = new HistoryGroupOnItemClickListener();

		// initialize patted stranger list
		//

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

				// get walk invite history group list view
				ListView _walkInviteHistoryGroupListView = (ListView) walkInviteHistoryGroupView
						.findViewById(R.id.hi_common_historyInfo_listView);

				// set its adapter
				_walkInviteHistoryGroupListView
						.setAdapter(historyGroupListViewAdapter);

				// set its on item click listener
				_walkInviteHistoryGroupListView
						.setOnItemClickListener(historyGroupOnItemClickListener);

				// get walk invite history groups
				groupInfoModel.getUserHistoryGroups(loginUser.getUserId(),
						loginUser.getUserKey(), GroupType.WALK_GROUP,
						new ICMConnector() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(Object... retValue) {
								// // dismiss get walk invite history groups
								// progress dialog
								// scheduleWalkInviteGroupProgDlg.dismiss();

								// check return values
								if (null != retValue && 0 < retValue.length) {
									// get and check the update walk invite
									// history group list
									if (retValue[retValue.length - 1] instanceof List) {
										// reset walk invite history group list
										walkInviteHistoryGroupList.clear();
										walkInviteHistoryGroupList
												.addAll((List<GroupBean>) retValue[retValue.length - 1]);
										historyGroupListViewAdapter
												.setHistoryGroups(walkInviteHistoryGroupList);
									}
								} else {
									LOGGER.error("Update walk invite history groups UI error");
								}
							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								LOGGER.error("Get walk invite history groups from remote server error, error code = "
										+ errorCode
										+ " and message = "
										+ errorMsg);

								// // dismiss get walk invite history groups
								// progress dialog
								// scheduleWalkInviteGroupProgDlg.dismiss();

								// check error code and process hopeRun business
								// error
								if (errorCode < 100) {
									// show error message toast
									Toast.makeText(HistoryInfoActivity.this,
											errorMsg, Toast.LENGTH_SHORT)
											.show();

									// test by ares
									//
								}
							}

						});
			} else {
				walkInviteHistoryGroupView.setVisibility(View.VISIBLE);

				// set walk invite history group list
				historyGroupListViewAdapter
						.setHistoryGroups(walkInviteHistoryGroupList);
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

				// get within group compete history group list view
				ListView _withinGroupCompeteHistoryGroupListView = (ListView) withinGroupCompeteHistoryGroupView
						.findViewById(R.id.hi_common_historyInfo_listView);

				// set its adapter
				_withinGroupCompeteHistoryGroupListView
						.setAdapter(historyGroupListViewAdapter);

				// set its on item click listener
				_withinGroupCompeteHistoryGroupListView
						.setOnItemClickListener(historyGroupOnItemClickListener);

				// get within group compete history groups
				groupInfoModel.getUserHistoryGroups(loginUser.getUserId(),
						loginUser.getUserKey(), GroupType.COMPETE_GROUP,
						new ICMConnector() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(Object... retValue) {
								// // dismiss get within group compete history
								// groups progress dialog
								// scheduleWalkInviteGroupProgDlg.dismiss();

								// check return values
								if (null != retValue && 0 < retValue.length) {
									// get and check the update within group
									// compete history group list
									if (retValue[retValue.length - 1] instanceof List) {
										// reset within group compete history
										// group list
										withinGroupCompeteHistoryGroupList
												.clear();
										withinGroupCompeteHistoryGroupList
												.addAll((List<GroupBean>) retValue[retValue.length - 1]);
										historyGroupListViewAdapter
												.setHistoryGroups(withinGroupCompeteHistoryGroupList);
									}
								} else {
									LOGGER.error("Update within group compete history groups UI error");
								}
							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								LOGGER.error("Get within group compete history groups from remote server error, error code = "
										+ errorCode
										+ " and message = "
										+ errorMsg);

								// // dismiss get within group compete history
								// groups progress dialog
								// scheduleWalkInviteGroupProgDlg.dismiss();

								// check error code and process hopeRun business
								// error
								if (errorCode < 100) {
									// show error message toast
									Toast.makeText(HistoryInfoActivity.this,
											errorMsg, Toast.LENGTH_SHORT)
											.show();

									// test by ares
									//
								}
							}

						});
			} else {
				withinGroupCompeteHistoryGroupView.setVisibility(View.VISIBLE);

				// set within group compete history group list
				historyGroupListViewAdapter
						.setHistoryGroups(withinGroupCompeteHistoryGroupList);
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

				// get patted strangers history list view
				ListView _pattedStrangersHistoryListView = (ListView) patStrangerView
						.findViewById(R.id.hi_common_historyInfo_listView);

				// set its adapter
				_pattedStrangersHistoryListView
						.setAdapter(pattedStrangerListViewAdapter = new PattedStrangerListViewAdapter(
								this,
								pattedStrangerList,
								R.layout.pattedstranger_listview_item_layout,
								new String[] {
										PattedStrangerListViewAdapterKey.PATTEDSTRANGER_AVATAR_KEY
												.name(),
										PattedStrangerListViewAdapterKey.PATTEDSTRANGER_NICKNAME_KEY
												.name(),
										PattedStrangerListViewAdapterKey.PATTEDSTRANGER_GENDER_KEY
												.name(),
										PattedStrangerListViewAdapterKey.PATTEDSTRANGER_PATTEDCOUNT_KEY
												.name() }, new int[] {
										R.id.psi_strangerAvatar_imageView,
										R.id.psi_strangerNickname_textView,
										R.id.psi_strangerGender_imageView,
										R.id.psi_strangerPattedCount_textView }));

				// set its on item click listener
				_pattedStrangersHistoryListView
						.setOnItemClickListener(new PattedStrangerOnItemClickListener());

				// get pat strangers
				strangerPatModel.getPatStrangers(loginUser.getUserId(),
						loginUser.getUserKey(), new ICMConnector() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(Object... retValue) {
								// // dismiss get user patted strangers progress
								// dialog
								// nearbyStrangersProgDlg.dismiss();

								// check return values
								if (null != retValue
										&& 0 < retValue.length
										&& retValue[retValue.length - 1] instanceof List) {
									// set user patted stranger list
									pattedStrangerListViewAdapter
											.setPattedStrangers((List<UserInfoPatLocationExtBean>) retValue[retValue.length - 1]);
								} else {
									LOGGER.error("Update user patted strangers listView error");
								}
							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								LOGGER.error("Get patted strangers from remote server error, error code = "
										+ errorCode
										+ " and message = "
										+ errorMsg);

								// // dismiss get within group compete history
								// groups progress dialog
								// scheduleWalkInviteGroupProgDlg.dismiss();

								// check error code and process hopeRun business
								// error
								if (errorCode < 100) {
									// show error message toast
									Toast.makeText(HistoryInfoActivity.this,
											errorMsg, Toast.LENGTH_SHORT)
											.show();

									// test by ares
									//
								}
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

	/**
	 * @name HistoryGroupOnItemClickListener
	 * @descriptor walk invite or within group compete history group item on
	 *             click listener
	 * @author Ares
	 * @version 1.0
	 */
	class HistoryGroupOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// define history group item extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put the selected history group id, type, topic, walk start and
			// stop time to extra data map as param
			_extraMap.put(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_ID,
					historyGroupListViewAdapter.getGroupId(position));
			_extraMap.put(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_TYPE,
					historyGroupListViewAdapter.getGroupType(position));
			_extraMap.put(
					HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_TOPIC,
					historyGroupListViewAdapter.getGroupTopic(position));
			_extraMap
					.put(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_WALKSTARTTIME,
							historyGroupListViewAdapter
									.getGroupWalkStartTime(position));
			_extraMap
					.put(HistoryGroupWalkInfoExtraData.HGWI_HISTORYGROUP_WALKSTOPTIME,
							historyGroupListViewAdapter
									.getGroupWalkStopTime(position));

			// go to history group walk info activity with extra data map
			pushActivity(HistoryGroupWalkInfoActivity.class, _extraMap);
		}

	}

	/**
	 * @name PattedStrangerListViewAdapter
	 * @descriptor patted stranger listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class PattedStrangerListViewAdapter extends SimpleAdapter {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				PattedStrangerListViewAdapter.class);

		// patted stranger listView adapter data list
		private static List<Map<String, Object>> _sDataList;

		/**
		 * @title PattedStrangerListViewAdapter
		 * @descriptor patted stranger listView adapter constructor with
		 *             context, patted stranger list, content view resource,
		 *             content view subview data key, and content view subview
		 *             id
		 * @param context
		 *            : context
		 * @param pattedStrangers
		 *            : patted stranger with patted location list
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public PattedStrangerListViewAdapter(Context context,
				List<UserInfoPatLocationExtBean> pattedStrangers, int resource,
				String[] dataKeys, int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// set view binder
			setViewBinder(new SSSimpleAdapterViewBinder());

			// set patted stranger list
			setPattedStrangers(pattedStrangers);
		}

		/**
		 * @title setPattedStrangers
		 * @descriptor set new patted stranger list
		 * @param pattedStrangers
		 *            : new set patted stranger list
		 */
		public void setPattedStrangers(
				List<UserInfoPatLocationExtBean> pattedStrangers) {
			// check new set patted stranger list
			if (null != pattedStrangers) {
				// clear patted stranger listView adapter data list and add new
				// data
				_sDataList.clear();

				// traversal patted stranger list
				for (UserInfoPatLocationExtBean _pattedStranger : pattedStrangers) {
					// define patted stranger listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// set data attributes
					_data.put(
							PattedStrangerListViewAdapterKey.PATTEDSTRANGER_AVATAR_KEY
									.name(), _pattedStranger.getAvatarUrl());
					_data.put(
							PattedStrangerListViewAdapterKey.PATTEDSTRANGER_NICKNAME_KEY
									.name(), _pattedStranger.getNickname());
					switch (_pattedStranger.getGender()) {
					case MALE:
						_data.put(
								PattedStrangerListViewAdapterKey.PATTEDSTRANGER_GENDER_KEY
										.name(), R.drawable.img_gender_male);
						break;

					case FEMALE:
						_data.put(
								PattedStrangerListViewAdapterKey.PATTEDSTRANGER_GENDER_KEY
										.name(), R.drawable.img_gender_female);
						break;

					default:
						// nothing to do
						break;
					}
					// get stranger patted count
					int _pattedCount = _pattedStranger.getPatCount();
					if (0 != _pattedCount) {
						_data.put(
								PattedStrangerListViewAdapterKey.PATTEDSTRANGER_PATTEDCOUNT_KEY
										.name(), _pattedCount);
					}

					// add data to list
					_sDataList.add(_data);
				}

				// notify data set changed
				notifyDataSetChanged();
			} else {
				LOGGER.error("Set new patted stranger list error, the new set data list is null");
			}
		}

		// inner class
		/**
		 * @name PattedStrangerListViewAdapterKey
		 * @descriptor patted stranger listView adapter key enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum PattedStrangerListViewAdapterKey {

			// patted stranger avatar, nickname, gender and patted count key
			PATTEDSTRANGER_AVATAR_KEY, PATTEDSTRANGER_NICKNAME_KEY, PATTEDSTRANGER_GENDER_KEY, PATTEDSTRANGER_PATTEDCOUNT_KEY;

		}

	}

	/**
	 * @name PattedStrangerOnItemClickListener
	 * @descriptor patted stranger listView item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class PattedStrangerOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//
		}

	}

}
