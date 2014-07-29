/**
 * 
 */
package com.smartsport.spedometer.history;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.customwidget.SSSimpleAdapterViewBinder;
import com.smartsport.spedometer.group.GroupBean;
import com.smartsport.spedometer.group.GroupInfoModel;
import com.smartsport.spedometer.group.GroupType;
import com.smartsport.spedometer.history.HistoryInfoActivity.PPWalkInfoCursorAdapter.WalkInfoCursorAdapterKey;
import com.smartsport.spedometer.history.HistoryInfoActivity.PatStrangerListViewAdapter.PatStrangerListViewAdapterKey;
import com.smartsport.spedometer.history.patstranger.PatStrangerActivity;
import com.smartsport.spedometer.history.patstranger.PatStrangerActivity.PatStrangerExtraData;
import com.smartsport.spedometer.history.walk.group.HistoryGroupListViewAdapter;
import com.smartsport.spedometer.history.walk.group.HistoryGroupListViewAdapter.HistoryGroupListViewAdapterKey;
import com.smartsport.spedometer.history.walk.group.HistoryGroupWalkInfoActivity;
import com.smartsport.spedometer.history.walk.group.HistoryGroupWalkInfoActivity.HistoryGroupWalkInfoExtraData;
import com.smartsport.spedometer.history.walk.personal.PersonalWalkHistoryRecordActivity;
import com.smartsport.spedometer.history.walk.personal.PersonalWalkHistoryRecordActivity.PersonalWalkHistoryRecordExtraData;
import com.smartsport.spedometer.localstorage.AppInterSqliteDBHelper.ISimpleBaseColumns;
import com.smartsport.spedometer.localstorage.pedometer.SPPersonalWalkRecordContentProvider.UserPersonalWalkRecords.UserPersonalWalkRecord;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.pedometer.PersonalWalkRecordBean;
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

	// user pat strangers info(user pat list and listView adapter)
	private List<UserInfoPatLocationExtBean> patStrangerList;
	private PatStrangerListViewAdapter patStrangerListViewAdapter;

	// get history info progress dialog
	private SSProgressDialog getHistoryInfoProgDlg;

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

		// initialize user pat stranger list
		patStrangerList = new ArrayList<UserInfoPatLocationExtBean>();

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

		// get history info listView
		personalPedometerWalkInfoListView = (ListView) personalPedometerWalkInfoView
				.findViewById(R.id.hi_common_historyInfo_listView);

		// set its adapter
		personalPedometerWalkInfoListView
				.setAdapter(new PPWalkInfoCursorAdapter(
						this,
						R.layout.personal_walkrecord_listview_item_layout,
						getContentResolver()
								.query(UserPersonalWalkRecord.PERSONALWALKRECORDS_CONTENT_URI,
										null,
										UserPersonalWalkRecord.USERPERSONAL_WALKRECORDS_WITHLOGINNAME_CONDITION,
										new String[] { String.valueOf(loginUser
												.getUserId()) },
										UserPersonalWalkRecord.START_TIME
												+ ISimpleBaseColumns._ORDER_DESC),
						new String[] {
								WalkInfoCursorAdapterKey.WALKRECORD_STARTTIME_KEY
										.name(),
								WalkInfoCursorAdapterKey.WALKRECORD_DURATIONTIME_KEY
										.name(),
								WalkInfoCursorAdapterKey.WALKRECORD_TOTALSTEP_KEY
										.name(),
								WalkInfoCursorAdapterKey.WALKRECORD_TOTALDISTANCE_KEY
										.name(),
								WalkInfoCursorAdapterKey.WALKRECORD_ENERGY_KEY
										.name() }, new int[] {
								R.id.pwr_startTime_textView,
								R.id.pwr_durationTime_textView,
								R.id.pwr_totalStep_textView,
								R.id.pwr_totalDistance_textView,
								R.id.pwr_energy_textView }));

		// set its on item click listener
		personalPedometerWalkInfoListView
				.setOnItemClickListener(new PPWalkInfoOnItemClickListener());
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

				// show get walk invite history group list progress dialog
				getHistoryInfoProgDlg = SSProgressDialog.show(this,
						R.string.procMsg_getWalkInviteHistoryGroups);

				// get walk invite history groups
				groupInfoModel.getUserHistoryGroups(loginUser.getUserId(),
						loginUser.getUserKey(), GroupType.WALK_GROUP,
						new ICMConnector() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(Object... retValue) {
								// dismiss get walk invite history groups
								// progress dialog
								getHistoryInfoProgDlg.dismiss();

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

								// dismiss get walk invite history groups
								// progress dialog
								getHistoryInfoProgDlg.dismiss();

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

				// show get within group compete history group list progress
				// dialog
				getHistoryInfoProgDlg = SSProgressDialog.show(this,
						R.string.procMsg_getWithinGroupCompeteHistoryGroups);

				// get within group compete history groups
				groupInfoModel.getUserHistoryGroups(loginUser.getUserId(),
						loginUser.getUserKey(), GroupType.COMPETE_GROUP,
						new ICMConnector() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(Object... retValue) {
								// dismiss get within group compete history
								// groups progress dialog
								getHistoryInfoProgDlg.dismiss();

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

								// dismiss get within group compete history
								// groups progress dialog
								getHistoryInfoProgDlg.dismiss();

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
			// check, get user pat stranger list view and then show it
			if (null == patStrangerView) {
				patStrangerView = ((ViewStub) findViewById(R.id.hi_strangerPat_historyPatStranger_viewStub))
						.inflate();

				// get user pat strangers history list view
				ListView _userPatStrangersHistoryListView = (ListView) patStrangerView
						.findViewById(R.id.hi_common_historyInfo_listView);

				// set its adapter
				_userPatStrangersHistoryListView
						.setAdapter(patStrangerListViewAdapter = new PatStrangerListViewAdapter(
								this,
								patStrangerList,
								R.layout.patstranger_listview_item_layout,
								new String[] {
										PatStrangerListViewAdapterKey.PATSTRANGER_AVATAR_KEY
												.name(),
										PatStrangerListViewAdapterKey.PATSTRANGER_NICKNAME_KEY
												.name(),
										PatStrangerListViewAdapterKey.PATSTRANGER_GENDER_KEY
												.name(),
										PatStrangerListViewAdapterKey.PATSTRANGER_PATCOUNT_KEY
												.name() },
								new int[] { R.id.psi_strangerAvatar_imageView,
										R.id.psi_strangerNickname_textView,
										R.id.psi_strangerGender_imageView,
										R.id.psi_strangerBePattedCount_textView }));

				// set its on item click listener
				_userPatStrangersHistoryListView
						.setOnItemClickListener(new PatStrangerOnItemClickListener());

				// show get user pat stranger list progress dialog
				getHistoryInfoProgDlg = SSProgressDialog.show(this,
						R.string.procMsg_getUserPatStrangers);

				// get user pat strangers
				strangerPatModel.getPatStrangers(loginUser.getUserId(),
						loginUser.getUserKey(), new ICMConnector() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(Object... retValue) {
								// dismiss get user pat strangers progress
								// dialog
								getHistoryInfoProgDlg.dismiss();

								// check return values
								if (null != retValue
										&& 0 < retValue.length
										&& retValue[retValue.length - 1] instanceof List) {
									// set user pat stranger list
									patStrangerListViewAdapter
											.setPatStrangers((List<UserInfoPatLocationExtBean>) retValue[retValue.length - 1]);
								} else {
									LOGGER.error("Update user pat strangers listView error");
								}
							}

							@Override
							public void onFailure(int errorCode, String errorMsg) {
								LOGGER.error("Get user pat strangers from remote server error, error code = "
										+ errorCode
										+ " and message = "
										+ errorMsg);

								// dismiss get user pat strangers progress
								// dialog
								getHistoryInfoProgDlg.dismiss();

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
	 * @name PPWalkInfoCursorAdapter
	 * @descriptor user personal pedometer walk info cursor adapter
	 * @author Ares
	 * @version 1.0
	 */
	@SuppressLint("SimpleDateFormat")
	static class PPWalkInfoCursorAdapter extends CursorAdapter {

		// logger
		private final SSLogger LOGGER = new SSLogger(
				PPWalkInfoCursorAdapter.class);

		// milliseconds per second and seconds per minute
		private final int MILLISECONDS_PER_SECONDS = 1000;
		private final int SECONDS_PER_MINUTED = 60;

		// walk start time date
		private final SimpleDateFormat WALKSTARTTIME_DATEFORMAT = new SimpleDateFormat(
				SSApplication
						.getContext()
						.getString(
								R.string.personalWalkHistoryRecord_walkStartTime_format));

		// context
		protected Context context;

		// layout inflater
		protected LayoutInflater layoutInflater;

		// items layout resource id
		protected int itemsLayoutResId;

		// data keys and items component resource identities
		protected String[] dataKeys;
		protected int[] itemsComponentResIds;

		// cursor data list
		protected List<Object> dataList;

		// cursor
		private Cursor cursor;

		/**
		 * @title PPWalkInfoCursorAdapter
		 * @descriptor user personal walk record listView adapter constructor
		 *             with context, content view resource, data cursor, content
		 *             view subview data key, and content view subview id
		 * @param context
		 *            : context
		 * @param resource
		 *            : resource id
		 * @param cursor
		 *            : personal walk record data cursor
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public PPWalkInfoCursorAdapter(Context context, int itemsLayoutResId,
				Cursor cursor, String[] dataKeys, int[] itemsComponentResIds) {
			super(context, cursor, true);

			// save context, layout inflater, items layout resource id, data
			// keys and items component resource identities
			this.context = context;
			layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.itemsLayoutResId = itemsLayoutResId;
			this.dataKeys = dataKeys;
			this.itemsComponentResIds = itemsComponentResIds;

			// initialize cursor data list
			dataList = new ArrayList<Object>();

			// save the cursor
			this.cursor = cursor;
		}

		public void setDataList(List<Object> dataList) {
			this.dataList = dataList;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// define and initalize view holder object
			AdapterViewHolder _viewHolder = new AdapterViewHolder();

			// inflate convert view
			View _convertView = layoutInflater.inflate(itemsLayoutResId,
					parent, false);

			// set item component view subViews
			for (int i = 0; i < itemsComponentResIds.length; i++) {
				_viewHolder.getViews4Holding().append(itemsComponentResIds[i],
						_convertView.findViewById(itemsComponentResIds[i]));
			}

			// set tag
			_convertView.setTag(_viewHolder);

			return _convertView;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// check cursor
			if (this.cursor != cursor) {
				// reset data with cursor
				// clear old cursor data list
				dataList.clear();

				// append cursor data
				do {
					appendCursorData(dataList, cursor);
				} while (cursor.moveToPrevious());

				// reverse data list
				Collections.reverse(dataList);

				// cursor recover
				cursor.move(dataList.size());

				// update cursor
				this.cursor = cursor;
			} else {
				// check cursor position and append cursor data
				if (cursor.getPosition() >= dataList.size()) {
					appendCursorData(dataList, cursor);
				} else {
					LOGGER.debug("Rollback, mustn't append cursor data");
				}
			}

			// set item component view subViews
			for (int i = 0; i < itemsComponentResIds.length; i++) {
				// recombination data and bind item component view data
				bindView(
						((AdapterViewHolder) view.getTag()).getViews4Holding()
								.get(itemsComponentResIds[i]),
						recombinationData(dataKeys[i],
								dataList.get(cursor.getPosition())),
						dataKeys[i]);
			}
		}

		@Override
		protected void onContentChanged() {
			// auto requery
			super.onContentChanged();

			// TODO Auto-generated method stub

		}

		@Override
		public Object getItem(int position) {
			// return super.getItem(position);
			return dataList.get(position);
		}

		/**
		 * @title getWalkStartTime
		 * @descriptor get walk start time
		 * @param position
		 *            : walk history record listView selected item position
		 * @return the selected walk history record walk start time
		 * @author Ares
		 */
		public String getWalkStartTime(int position) {
			// get walk start time
			return WALKSTARTTIME_DATEFORMAT
					.format(((PersonalWalkRecordBean) getItem(position))
							.getStartTime() * MILLISECONDS_PER_SECONDS);
		}

		/**
		 * @title appendCursorData
		 * @descriptor append cursor data
		 * @param data
		 *            : new added data
		 * @param cursor
		 *            : the cursor
		 * @author Ares
		 */
		protected void appendCursorData(List<Object> data, Cursor cursor) {
			// check the cursor
			if (null != cursor) {
				// get user personal walk record bean and append to data list
				data.add(new PersonalWalkRecordBean(cursor));
			} else {
				LOGGER.error("Query user login history all walk records error, cursor = "
						+ cursor);
			}
		}

		/**
		 * @title recombinationData
		 * @descriptor recombination data
		 * @param dataKey
		 *            : data key
		 * @param dataObject
		 *            : data object
		 * @return recombination data
		 * @author Ares
		 */
		protected Map<String, ?> recombinationData(String dataKey,
				Object dataObject) {
			// define return data map and the data value for key in data object
			Map<String, Object> _dataMap = new HashMap<String, Object>();
			Object _dataValue = null;

			// check data object and convert to user personal walk record object
			try {
				// convert data object to user personal walk record
				PersonalWalkRecordBean _walkRecordObject = (PersonalWalkRecordBean) dataObject;

				// check data key and get data value for it
				if (WalkInfoCursorAdapterKey.WALKRECORD_STARTTIME_KEY.name()
						.equalsIgnoreCase(dataKey)) {
					// walk start time
					_dataValue = WALKSTARTTIME_DATEFORMAT
							.format(_walkRecordObject.getStartTime()
									* MILLISECONDS_PER_SECONDS);
				} else if (WalkInfoCursorAdapterKey.WALKRECORD_DURATIONTIME_KEY
						.name().equalsIgnoreCase(dataKey)) {
					// walk duration time
					_dataValue = String
							.format(context
									.getString(R.string.personalWalkHistoryRecord_walkDurationTime_format),
									_walkRecordObject.getDurationTime()
											/ SECONDS_PER_MINUTED,
									_walkRecordObject.getDurationTime()
											% SECONDS_PER_MINUTED);
				} else if (WalkInfoCursorAdapterKey.WALKRECORD_TOTALSTEP_KEY
						.name().equalsIgnoreCase(dataKey)) {
					// walk total step
					_dataValue = _walkRecordObject.getTotalStep();
				} else if (WalkInfoCursorAdapterKey.WALKRECORD_TOTALDISTANCE_KEY
						.name().equalsIgnoreCase(dataKey)) {
					// walk total distance
					_dataValue = _walkRecordObject.getTotalDistance();
				} else if (WalkInfoCursorAdapterKey.WALKRECORD_ENERGY_KEY
						.name().equalsIgnoreCase(dataKey)) {
					// walk energy
					_dataValue = _walkRecordObject.getEnergy();
				} else {
					LOGGER.error("Recombination data error, data key = "
							+ dataKey + " and data object = " + dataObject);
				}
			} catch (Exception e) {
				LOGGER.error("Convert data object to user personal walk record bean object error, data = "
						+ dataObject);

				e.printStackTrace();
			}

			// put data value to map and return
			_dataMap.put(dataKey, _dataValue);
			return _dataMap;
		}

		/**
		 * @title bindView
		 * @descriptor bind view and data
		 * @param view
		 *            : the binded view
		 * @param dataMap
		 *            : data
		 * @param dataKey
		 *            : data key
		 * @author Ares
		 */
		protected void bindView(View view, Map<String, ?> dataMap,
				String dataKey) {
			// get item data object
			Object _itemData = dataMap.get(dataKey);

			// check view type
			// textView
			if (view instanceof TextView) {
				// generate view text
				SpannableString _viewNewText = new SpannableString(
						null == _itemData ? "" : _itemData.toString());

				// check data class name
				if (_itemData instanceof SpannableString) {
					_viewNewText
							.setSpan(
									new ForegroundColorSpan(
											context.getResources()
													.getColor(
															android.R.color.holo_green_light)),
									0, _viewNewText.length(),
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}

				// set view text
				((TextView) view).setText(_viewNewText);
			} else {
				LOGGER.error("Bind view error, view = " + view
						+ " not recognized, data key = " + dataKey
						+ " and data map = " + dataMap);
			}
		}

		// inner class
		/**
		 * @name WalkInfoCursorAdapterKey
		 * @descriptor walk info listView cursor adapter key enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum WalkInfoCursorAdapterKey {

			// walk start time, duration time, walk total step, total distance
			// and energy
			WALKRECORD_STARTTIME_KEY, WALKRECORD_DURATIONTIME_KEY, WALKRECORD_TOTALSTEP_KEY, WALKRECORD_TOTALDISTANCE_KEY, WALKRECORD_ENERGY_KEY;

		}

		/**
		 * @name AdapterViewHolder
		 * @descriptor adapter view holder
		 * @author Ares
		 * @version 1.0
		 */
		class AdapterViewHolder {

			// views for holding
			private SparseArray<View> views4Holding;

			public AdapterViewHolder() {
				super();

				// init views sparse array for holding
				views4Holding = new SparseArray<View>();
			}

			public SparseArray<View> getViews4Holding() {
				return views4Holding;
			}

		}

	}

	/**
	 * @name PPWalkInfoOnItemClickListener
	 * @descriptor user personal pedometer walk info item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class PPWalkInfoOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// define personal walk history record item extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// get the walk history record listView adapter
			PPWalkInfoCursorAdapter _walkHistoryRecordListViewAdapter = (PPWalkInfoCursorAdapter) personalPedometerWalkInfoListView
					.getAdapter();

			// put the selected user personal walk record title and its
			// bean(start time, duration time, walk total step, total distance
			// and energy) to extra data map as param
			_extraMap
					.put(PersonalWalkHistoryRecordExtraData.PPWHR_SELECTED_WALKRECORD_TITLE,
							_walkHistoryRecordListViewAdapter
									.getWalkStartTime(position));
			_extraMap
					.put(PersonalWalkHistoryRecordExtraData.PPWHR_SELECTED_WALKRECORD_BEAN,
							_walkHistoryRecordListViewAdapter.getItem(position));

			// go to personal walk history record activity with extra data map
			pushActivity(PersonalWalkHistoryRecordActivity.class, _extraMap);
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
	 * @name PatStrangerListViewAdapter
	 * @descriptor user pat stranger listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class PatStrangerListViewAdapter extends SimpleAdapter {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				PatStrangerListViewAdapter.class);

		// user pat stranger info key
		private static final String USERPATSTRANGER_BEAN_KEY = "userPatStranger_bean_key";

		// user pat stranger listView adapter data list
		private static List<Map<String, Object>> _sDataList;

		/**
		 * @title PatStrangerListViewAdapter
		 * @descriptor user pat stranger listView adapter constructor with
		 *             context, pat stranger list, content view resource,
		 *             content view subview data key, and content view subview
		 *             id
		 * @param context
		 *            : context
		 * @param patStrangers
		 *            : user pat stranger with pat location list
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public PatStrangerListViewAdapter(Context context,
				List<UserInfoPatLocationExtBean> patStrangers, int resource,
				String[] dataKeys, int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// set view binder
			setViewBinder(new SSSimpleAdapterViewBinder());

			// set user pat stranger list
			setPatStrangers(patStrangers);
		}

		/**
		 * @title setPatStrangers
		 * @descriptor set user new pat stranger list
		 * @param patStrangers
		 *            : new set user pat stranger list
		 */
		public void setPatStrangers(
				List<UserInfoPatLocationExtBean> patStrangers) {
			// check new set user pat stranger list
			if (null != patStrangers) {
				// clear user pat stranger listView adapter data list and add
				// new data
				_sDataList.clear();

				// traversal user pat stranger list
				for (UserInfoPatLocationExtBean _patStranger : patStrangers) {
					// define user pat stranger listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// set data attributes
					_data.put(USERPATSTRANGER_BEAN_KEY, _patStranger);
					_data.put(
							PatStrangerListViewAdapterKey.PATSTRANGER_AVATAR_KEY
									.name(), _patStranger.getAvatarUrl());
					_data.put(
							PatStrangerListViewAdapterKey.PATSTRANGER_NICKNAME_KEY
									.name(), _patStranger.getNickname());
					switch (_patStranger.getGender()) {
					case MALE:
						_data.put(
								PatStrangerListViewAdapterKey.PATSTRANGER_GENDER_KEY
										.name(), R.drawable.img_gender_male);
						break;

					case FEMALE:
						_data.put(
								PatStrangerListViewAdapterKey.PATSTRANGER_GENDER_KEY
										.name(), R.drawable.img_gender_female);
						break;

					default:
						// nothing to do
						break;
					}
					// get stranger be patted count
					int _bePattedCount = _patStranger.getPatCount();
					if (0 != _bePattedCount) {
						_data.put(
								PatStrangerListViewAdapterKey.PATSTRANGER_PATCOUNT_KEY
										.name(), _bePattedCount);
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

		/**
		 * @title getPatStranger
		 * @descriptor get the selected user pat stranger info
		 * @param position
		 *            : the user pat stranger list position
		 * @return the user pat stranger info
		 */
		public UserInfoPatLocationExtBean getPatStranger(int position) {
			return (UserInfoPatLocationExtBean) _sDataList.get(position).get(
					USERPATSTRANGER_BEAN_KEY);
		}

		// inner class
		/**
		 * @name PatStrangerListViewAdapterKey
		 * @descriptor user pat stranger listView adapter key enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum PatStrangerListViewAdapterKey {

			// user pat stranger avatar, nickname, gender and pat count key
			PATSTRANGER_AVATAR_KEY, PATSTRANGER_NICKNAME_KEY, PATSTRANGER_GENDER_KEY, PATSTRANGER_PATCOUNT_KEY;

		}

	}

	/**
	 * @name PatStrangerOnItemClickListener
	 * @descriptor user pat stranger listView item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class PatStrangerOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// define user pat stranger item extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put the selected user pat stranger bean to extra data map as
			// param
			_extraMap.put(PatStrangerExtraData.PS_SI_BEAN,
					patStrangerListViewAdapter.getPatStranger(position));

			// go to user pat stranger activity with extra data map
			pushActivity(PatStrangerActivity.class, _extraMap);
		}

	}

}
