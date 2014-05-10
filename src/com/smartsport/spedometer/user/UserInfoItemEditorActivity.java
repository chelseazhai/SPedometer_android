/**
 * 
 */
package com.smartsport.spedometer.user;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name UserInfoItemEditorActivity
 * @descriptor user info item editor activity
 * @author Ares
 * @version 1.0
 */
public class UserInfoItemEditorActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			UserInfoItemEditorActivity.class);

	// input method manager
	private InputMethodManager inputMethodManager;

	// show soft input timer and timer task
	private Timer SHOW_SOFTINPUT_TIMER = new Timer();
	private TimerTask showSoftInputTimerTask;

	// user info editor item widget id, editor info name, type, value and its
	// selector content
	private int editorItemWidgetId;
	private String editorInfoName;
	private UserInfoEditorType editorType;
	private String editorInfoValue;
	private List<String> editorInfoValueSelectorContent;

	// user info editor edittext
	private EditText editorEditText;
	private NumberPicker ageNumberPicker;
	private TextView ageDisplayTextView;
	private ListView genderSpinnerListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the editor user info item widget id, editor name, type, value
			// and its selector content
			editorItemWidgetId = _extraData
					.getInt(UserInfoItemEditorExtraData.UI_EI_WIDGETID);
			editorInfoName = _extraData
					.getString(UserInfoItemEditorExtraData.UI_EI_NAME);
			editorType = (UserInfoEditorType) _extraData
					.getSerializable(UserInfoItemEditorExtraData.UI_EI_TYPE);
			editorInfoValue = _extraData
					.getString(UserInfoItemEditorExtraData.UI_EI_VALUE);
			editorInfoValueSelectorContent = _extraData
					.getStringArrayList(UserInfoItemEditorExtraData.UI_EI_VALUESELECTORCONTENT);
		}

		// get input method manager
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// set content view
		setContentView(R.layout.activity_userinfo_item_editor);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// check editor user info type and set right bar button item if needed
		if (UserInfoEditorType.GENDER_SPINNER != editorType) {
			setRightBarButtonItem(new SSBNavTitleBarButtonItem(this,
					R.string.save_editorUserInfo_barbtnitem_title,
					new EditorUserInfoSaveBarBtnItemOnClickListener()));
		}

		// set title attributes
		setTitle(editorInfoName);
		setTitleColor(Color.WHITE);
		setTitleSize(26.0f);

		// check editor user info type again and get the editor operator widget
		if (null != editorType) {
			switch (editorType) {
			case GENDER_SPINNER:
				// get user gender spinner listView
				genderSpinnerListView = (ListView) findViewById(R.id.uiid_genderSpinner_listView);

				// check editor user age value selector content
				if (null != editorInfoValueSelectorContent) {
					// set its adapter
					genderSpinnerListView
							.setAdapter(new GenderSpinnerListViewAdapter(
									this,
									R.layout.usergender_spinner_listview_item_layout,
									R.id.userGender_spinnerItem_label_textView,
									editorInfoValueSelectorContent));

					// set its choice mode
					genderSpinnerListView
							.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

					// check editor gender value and set the user selected item
					// if needed
					if (null != editorInfoValue) {
						// get and check the index of editor gender value in its
						// selector content and set the default selected item
						// checked
						int _index = editorInfoValueSelectorContent
								.indexOf(editorInfoValue);
						if (-1 != _index) {
							genderSpinnerListView.setItemChecked(_index, true);
						}
					}

					// set its on item click listener
					genderSpinnerListView
							.setOnItemClickListener(new GenderItemOnClickListener());

					// set its visible
					genderSpinnerListView.setVisibility(View.VISIBLE);
				}
				break;

			case AGE_PICKER:
				// get user age numberPicker
				ageNumberPicker = (NumberPicker) findViewById(R.id.uiie_age_numberPicker);

				// set its min and max value
				ageNumberPicker.setMinValue(10);
				ageNumberPicker.setMaxValue(100);

				// define age picker on value changed listener
				AgePickerOnValueChangeListener _agePickerOnValueChangeListener = new AgePickerOnValueChangeListener();

				// set its on value changed listener
				ageNumberPicker
						.setOnValueChangedListener(_agePickerOnValueChangeListener);

				// get user age display textView
				ageDisplayTextView = (TextView) findViewById(R.id.uiie_age_display_textView);

				// check editor age value and set the user input value if needed
				if (null != editorInfoValue) {
					// get user input value
					int _userInputValue = Integer.parseInt(editorInfoValue);

					// set age numberPicker value
					ageNumberPicker.setValue(_userInputValue);

					// perform age picker on value changed listener on value
					// change method
					_agePickerOnValueChangeListener.onValueChange(
							ageNumberPicker, ageNumberPicker.getMinValue(),
							_userInputValue);
				}

				// show user age picker relativeLayout
				findViewById(R.id.uiie_agePicker_relativeLayout).setVisibility(
						View.VISIBLE);
				break;

			case HWSL_EDITTEXT:
			default:
				// get user info editor edittext
				editorEditText = (EditText) findViewById(R.id.uiie_editor_editText);

				// check editor user info value and set it as its text
				if (null != editorInfoValue) {
					editorEditText.setText(editorInfoValue);
				}

				// set its visible
				editorEditText.setVisibility(View.VISIBLE);
				break;
			}
		} else {
			LOGGER.error("Edit user info error, the editor user info type is null");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// check editor user info type
		if (UserInfoEditorType.HWSL_EDITTEXT == editorType) {
			// set editor editText focusable
			editorEditText.setFocusable(true);
			editorEditText.setFocusableInTouchMode(true);
			editorEditText.requestFocus();

			// show soft input after 250 milliseconds
			SHOW_SOFTINPUT_TIMER.schedule(
					showSoftInputTimerTask = new TimerTask() {

						@Override
						public void run() {
							inputMethodManager.showSoftInput(editorEditText, 0);
						}

					}, 250);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// check and cancel show soft input timer task
		if (null != showSoftInputTimerTask) {
			showSoftInputTimerTask.cancel();

			showSoftInputTimerTask = null;
		}
	}

	// inner class
	/**
	 * @name UserInfoEditorType
	 * @descriptor user info editor type
	 * @author Ares
	 * @version 1.0
	 */
	public enum UserInfoEditorType {

		// gender spinner, age picker, height, weight and step length edittext
		GENDER_SPINNER, AGE_PICKER, HWSL_EDITTEXT;

	}

	/**
	 * @name UserInfoItemEditorExtraData
	 * @descriptor user info item editor extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class UserInfoItemEditorExtraData {

		// user info item widget id, editor info name, type, info value and its
		// selector content
		public static final String UI_EI_WIDGETID = "userInfo_editorItem_widgetId";
		public static final String UI_EI_NAME = "userInfo_editorInfo_name";
		public static final String UI_EI_TYPE = "userInfo_editorInfo_type";
		public static final String UI_EI_VALUE = "userInfo_editorInfo_value";
		public static final String UI_EI_VALUESELECTORCONTENT = "userInfo_editorInfo_value_selectorContent";

	}

	/**
	 * @name EditorUserInfoSaveBarBtnItemOnClickListener
	 * @descriptor editor user info save bar button item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class EditorUserInfoSaveBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// check editor user info type
			if (UserInfoEditorType.HWSL_EDITTEXT == editorType) {
				//
			} else if (UserInfoEditorType.AGE_PICKER == editorType) {
				//
			}

			//
		}

	}

	/**
	 * @name AgePickerOnValueChangeListener
	 * @descriptor editor user age numberPicker on value changed listener
	 * @author Ares
	 * @version 1.0
	 */
	class AgePickerOnValueChangeListener implements OnValueChangeListener {

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			// update editor user age display textView text
			ageDisplayTextView
					.setText(String
							.format(getString(R.string.userAge_editor_displayTextView_text_format),
									newVal));
		}

	}

	/**
	 * @name GenderSpinnerListViewAdapter
	 * @descriptor user gender spinner listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	class GenderSpinnerListViewAdapter extends ArrayAdapter<String> {

		/**
		 * @title GenderSpinnerListViewAdapter
		 * @descriptor user gender spinner listView adapter constructor with
		 *             context and resource
		 * @param context
		 *            : context
		 * @param resource
		 *            : resource id
		 * @param textViewResourceId
		 *            : content view label textView id
		 * @param objects
		 *            : data list
		 */
		public GenderSpinnerListViewAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// get content view
			View _contentView = super.getView(position, convertView, parent);

			// get data count
			int _dataCount = getCount();

			// check position and set content view background
			if (1 < _dataCount) {
				if (0 == position) {
					// first
					_contentView
							.setBackgroundResource(R.drawable.common_form_multipletop_item_bg);
				} else if (_dataCount - 1 == position) {
					// last
					_contentView
							.setBackgroundResource(R.drawable.common_form_multiplebottom_item_bg);
				} else {
					// middle
					_contentView
							.setBackgroundResource(R.drawable.common_form_multiplemiddle_item_bg);
				}
			}

			return _contentView;
		}

	}

	/**
	 * @name GenderItemOnClickListener
	 * @descriptor user gender spinner listView on item click listener
	 * @author Ares
	 * @version 1.0
	 */
	class GenderItemOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//
		}

	}

}
