/**
 * 
 */
package com.smartsport.spedometer.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavTitleBarButtonItem;
import com.smartsport.spedometer.group.walk.WalkInviteInfoSettingActivity.WalkInviteInfoSettingExtraData;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupInviteInfoItemEditorActivity
 * @descriptor smartsport walk or within group compete invite info item editor
 *             activity
 * @author Ares
 * @version 1.0
 */
public class GroupInviteInfoItemEditorActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			GroupInviteInfoItemEditorActivity.class);

	// input method manager
	private InputMethodManager inputMethodManager;

	// show soft input timer and timer task
	private Timer SHOW_SOFTINPUT_TIMER = new Timer();
	private TimerTask showSoftInputTimerTask;

	// group invite info attribute, editor info name and value
	private GroupInviteInfoAttr4Setting editorAttr;
	private String editorInfoName;
	private String editorInfoValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the editor group invite info attribute, editor name and value
			editorAttr = (GroupInviteInfoAttr4Setting) _extraData
					.getSerializable(GroupInviteInfoItemEditorExtraData.GII_EI_ATTRIBUTE);
			editorInfoName = _extraData
					.getString(GroupInviteInfoItemEditorExtraData.GII_EI_NAME);
			editorInfoValue = _extraData
					.getString(GroupInviteInfoItemEditorExtraData.GII_EI_VALUE);
		}

		// get input method manager
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// set content view
		setContentView(R.layout.activity_groupinviteinfo_item_editor);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavTitleBarButtonItem(this,
				R.string.save_editorGroupInviteInfo_barbtnitem_title,
				new EditorGroupInviteInfoSaveBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(editorInfoName);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		//
	}

	// inner class
	/**
	 * @name GroupInviteInfoItemEditorExtraData
	 * @descriptor group invite info item editor extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class GroupInviteInfoItemEditorExtraData {

		// group invite info attribute, editor info name and info value
		public static final String GII_EI_ATTRIBUTE = "groupInviteInfo_attribute";
		public static final String GII_EI_NAME = "groupInviteInfo_editorInfo_name";
		public static final String GII_EI_VALUE = "groupInviteInfo_editorInfo_value";

	}

	/**
	 * @name EditorGroupInviteInfoSaveBarBtnItemOnClickListener
	 * @descriptor editor group invite info save bar button item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class EditorGroupInviteInfoSaveBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// define group invite info editor extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put editor group invite info attribute to extra data map as param
			_extraMap.put(WalkInviteInfoSettingExtraData.WIIS_EI_ATTRIBUTE,
					editorAttr);

			// check editor group invite info attribute and put editor group
			// invite info value to extra data map as param
			switch (editorAttr) {
			case GROUP_TOPIC:
				// group invite topic
				_extraMap.put(WalkInviteInfoSettingExtraData.WIIS_EI_VALUE, "");
				break;

			case WALKINVITE_SCHEDULETIME:
				// walk invite schedule time(schedule begin and end time)
				_extraMap.put(WalkInviteInfoSettingExtraData.WIIS_EI_VALUE, "");
				break;

			case WITHINGROUPCOMPETE_DURATIONTIME:
				// within group compete duration time
				_extraMap.put(WalkInviteInfoSettingExtraData.WIIS_EI_VALUE, "");
				break;
			}

			// pop the activity with result code and extra map
			popActivityWithResult(RESULT_OK, _extraMap);
		}

	}

}
