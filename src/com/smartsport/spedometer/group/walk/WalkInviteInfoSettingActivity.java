/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.group.GroupInviteInfoAttr4Setting;
import com.smartsport.spedometer.group.GroupInviteInfoItemEditorActivity;
import com.smartsport.spedometer.group.GroupInviteInfoItemEditorActivity.GroupInviteInfoItemEditorExtraData;
import com.smartsport.spedometer.group.GroupInviteInfoListViewAdapter;
import com.smartsport.spedometer.group.GroupInviteInfoListViewAdapter.GroupInviteInfo4SettingListViewAdapterKey;
import com.smartsport.spedometer.mvc.ISSBaseActivityResult;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.user.UserInfoBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WalkInviteInfoSettingActivity
 * @descriptor smartsport walk invite info setting activity
 * @author Ares
 * @version 1.0
 */
public class WalkInviteInfoSettingActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			WalkInviteInfoSettingActivity.class);

	// the selected walk invite invitee bean
	private UserInfoBean selectedWalkInviteInvitee;

	// walk invite info listView adapter
	private GroupInviteInfoListViewAdapter walkInviteInfoListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get and check the extra data
		Bundle _extraData = getIntent().getExtras();
		if (null != _extraData) {
			// get the selected walk invite invitee bean
			selectedWalkInviteInvitee = (UserInfoBean) _extraData
					.getSerializable(WalkInviteInfoSettingExtraData.WIIS_SELECTED_INVITEE_BEAN);
		}

		// set content view
		setContentView(R.layout.activity_walkinviteinfo_setting);
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
				R.drawable.img_send_walk_withingroupcompete_invite,
				new SendWalkInviteInfoBarBtnItemOnClickListener()));

		// set title attributes
		setTitle(R.string.walkInviteInfoSetting_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// set walk invite inviter avatar imageView image
		((ImageView) findViewById(R.id.wiis_inviterAvatar_imageView))
				.setImageURI(null);

		// check the selected walk invite invitee then set walk invite invitee
		// avatar imageView image and nickname textView text
		if (null != selectedWalkInviteInvitee) {
			((ImageView) findViewById(R.id.wiis_inviteeAvatar_imageView))
					.setImageURI(Uri.parse(selectedWalkInviteInvitee
							.getAvatarUrl()));
			((TextView) findViewById(R.id.wiis_inviteeNickname_textView))
					.setText(selectedWalkInviteInvitee.getNickname());
		}

		// get walk invite info listView
		ListView _walkInviteInfoListView = (ListView) findViewById(R.id.wiis_walkInviteInfo_listView);

		// set its adapter
		_walkInviteInfoListView
				.setAdapter(walkInviteInfoListViewAdapter = new GroupInviteInfoListViewAdapter(
						this,
						getResources().getStringArray(
								R.array.walk_inviteInfo4Setting_labels),
						R.layout.group_inviteinfo_listview_item_layout,
						new String[] {
								GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_LABEL_KEY
										.name(),
								GroupInviteInfo4SettingListViewAdapterKey.GROUPINVITEINFO_VALUE_KEY
										.name() }, new int[] {
								R.id.gii_label_textView,
								R.id.gii_value_textView }));

		// set its on item click listener
		_walkInviteInfoListView
				.setOnItemClickListener(new WalkInviteInfoOnItemClickListener());
	}

	// inner class
	/**
	 * @name WalkInviteInfoSettingRequestCode
	 * @descriptor walk invite info setting request code
	 * @author Ares
	 * @version 1.0
	 */
	class WalkInviteInfoSettingRequestCode {

		// walk invite info item editor request code
		private static final int WIIS_WALKINVITEINFOITEM_EDITOR_REQCODE = 4000;

	}

	/**
	 * @name WalkInviteInfoSettingExtraData
	 * @descriptor walk invite info setting extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class WalkInviteInfoSettingExtraData {

		// selected walk invite invitee info bean
		public static final String WIIS_SELECTED_INVITEE_BEAN = "walkInviteInfoSetting_selected_walkInviteInvitee_bean";

		// walk invite info setting, editor invite info attribute and editor
		// info value
		public static final String WIIS_EI_ATTRIBUTE = "walkInviteInfoSetting_inviteInfo_attribute";
		public static final String WIIS_EI_VALUE = "walkInviteInfoSetting_userInfo_editorInfo_value";

	}

	/**
	 * @name WIISInviteInfoEditorOnActivityResult
	 * @descriptor walk invite info setting invite info editor on activity
	 *             result
	 * @author Ares
	 * @version 1.0
	 */
	class WIISInviteInfoEditorOnActivityResult implements ISSBaseActivityResult {

		@Override
		public void onActivityResult(int resultCode, Intent data) {
			// check the result code
			if (RESULT_OK == resultCode) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					// get and check editor walk invite info attribute and value
					GroupInviteInfoAttr4Setting _editorAttr = (GroupInviteInfoAttr4Setting) _extraData
							.getSerializable(WalkInviteInfoSettingExtraData.WIIS_EI_ATTRIBUTE);
					String _editorValue = _extraData
							.getString(WalkInviteInfoSettingExtraData.WIIS_EI_VALUE);

					LOGGER.info("@@, _editorAttr = " + _editorAttr
							+ " and _editorValue = " + _editorValue);

					// update editor walk invite info value
					walkInviteInfoListViewAdapter.setData(_editorAttr,
							_editorValue);
				}
			}
		}

	}

	/**
	 * @name SendWalkInviteInfoBarBtnItemOnClickListener
	 * @descriptor send walk invite info bar button item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class SendWalkInviteInfoBarBtnItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			LOGGER.debug("SendWalkInviteInfoBarBtnItemOnClickListener");

			//
		}

	}

	/**
	 * @name WalkInviteInfoOnItemClickListener
	 * @descriptor walk invite info item on click listener
	 * @author Ares
	 * @version 1.0
	 */
	class WalkInviteInfoOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// define walk invite info item editor extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put editor walk invite info attribute, name and info to extra
			// data map as param
			_extraMap.put(GroupInviteInfoItemEditorExtraData.GII_EI_ATTRIBUTE,
					walkInviteInfoListViewAdapter.getItemAttribute(position));
			_extraMap.put(GroupInviteInfoItemEditorExtraData.GII_EI_NAME,
					walkInviteInfoListViewAdapter.getItemLabel(position));
			_extraMap.put(GroupInviteInfoItemEditorExtraData.GII_EI_VALUE,
					walkInviteInfoListViewAdapter.getItemValue(position));

			// go to group invite info item editor activity with extra data map
			pushActivityForResult(
					GroupInviteInfoItemEditorActivity.class,
					_extraMap,
					WalkInviteInfoSettingRequestCode.WIIS_WALKINVITEINFOITEM_EDITOR_REQCODE,
					new WIISInviteInfoEditorOnActivityResult());
		}

	}

}
