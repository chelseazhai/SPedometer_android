/**
 * 
 */
package com.smartsport.spedometer.strangersocial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.ISSBaseActivityResult;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.NearbyStrangersActivity.NearbyStrangerListViewAdapter.NearbyStrangerListViewAdapterKey;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatActivity;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatActivity.StrangerPatExtraData;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatModel;
import com.smartsport.spedometer.strangersocial.pat.UserInfoPatLocationExtBean;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name NearbyStrangersActivity
 * @descriptor smartsport nearby strangers activity
 * @author Ares
 * @version 1.0
 */
public class NearbyStrangersActivity extends SSBaseActivity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			NearbyStrangersActivity.class);

	// stranger pat model
	private StrangerPatModel strangerPatModel;

	// nearby strangers gender
	private UserGender strangerGender;

	// user location info
	private static LocationBean userLocation;

	// nearby stranger listView adapter and its data list
	private NearbyStrangerListViewAdapter nearbyStrangerListViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize stranger pat model
		strangerPatModel = new StrangerPatModel();

		// load nearby strangers gender and user location info from local
		// storage
		// test by ares
		strangerGender = null;
		userLocation = new LocationBean(118.769790, 32.066336);
		//

		// test by ares
		for (int i = 0; i < 12; i++) {
			UserInfoPatLocationExtBean _uipl = new UserInfoPatLocationExtBean();

			_uipl.setUserId(10010 + i);
			_uipl.setAvatarUrl("/avatar/img_123jhsd_sdjhf");
			_uipl.setNickname("用户" + i);
			_uipl.setGender(0 == i ? UserGender.GENDER_UNKNOWN
					: 0 == i % 2 ? UserGender.FEMALE : UserGender.MALE);
			_uipl.setAge(10 + i);
			_uipl.setHeight(170.0f + i);
			_uipl.setWeight(70.0f + i);
			_uipl.setLocation(new LocationBean(userLocation.getLongitude()
					+ (0 == i ? 10 : i / 100.0), userLocation.getLatitude() + i
					/ 100.0));
			_uipl.setPatCount(i);

			strangerPatModel.getNearbyStrangersInfo().add(_uipl);
		}

		// set content view
		setContentView(R.layout.activity_nearby_strangers);

		// get nearby strangers with user location info from remote server
		strangerPatModel.getNearbyStrangers(123123, "token", strangerGender,
				userLocation, new ICMConnector() {

					//

				});
	}

	@Override
	protected void initContentViewUI() {
		// navigation bar
		// set navigation bar color
		setNavbarBackgroundColor(getResources().getColor(
				android.R.color.holo_green_light));

		// set right bar button item
		setRightBarButtonItem(new SSBNavImageBarButtonItem(this,
				R.drawable.img_more_operator,
				new NearbyStrangersGenderSelectOnClickListener()));

		// set title attributes
		setTitle(R.string.nearbyStrangers_activity_title);
		setTitleColor(Color.WHITE);
		setTitleSize(22.0f);
		setShadow(1.0f, 0.6f, 0.8f, Color.GRAY);

		// get nearby stranger listView
		ListView _nearbyStrangerListView = (ListView) findViewById(R.id.ns_nearbyStranger_listView);

		// set its adapter
		_nearbyStrangerListView
				.setAdapter(nearbyStrangerListViewAdapter = new NearbyStrangerListViewAdapter(
						this,
						strangerPatModel.getNearbyStrangersInfo(),
						R.layout.nearbystranger_listview_item_layout,
						new String[] {
								NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_AVATAR_KEY
										.name(),
								NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_NICKNAME_KEY
										.name(),
								NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_GENDER_KEY
										.name(),
								NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_DISTANCE_KEY
										.name(),
								NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_PATCOUNT_KEY
										.name() }, new int[] {
								R.id.nsi_item_strangerAvatar_imageView,
								R.id.nsi_item_strangerNickname_textView,
								R.id.nsi_item_strangerGender_imageView,
								R.id.nsi_item_strangerDistance_textView,
								R.id.nsi_item_strangerPatCount_textView }));

		// set its on item click listener
		_nearbyStrangerListView
				.setOnItemClickListener(new NearbyStrangerItemOnClickListener());
	}

	// inner class
	/**
	 * @name NearbyStrangersRequestCode
	 * @descriptor nearby strangers request code
	 * @author Ares
	 * @version 1.0
	 */
	class NearbyStrangersRequestCode {

		// pat nearby stranger request code
		private static final int SP_STRANGERPAT_REQCODE = 2000;

	}

	/**
	 * @name NearbyStrangersExtraData
	 * @descriptor nearby strangers extra data constant
	 * @author Ares
	 * @version 1.0
	 */
	public static final class NearbyStrangersExtraData {

		// nearby stranger info bean
		public static final String NSS_SI_BEAN = "NearbyStrangers_strangerInfo_bean";

	}

	/**
	 * @name NSSStrangerPatOnActivityResult
	 * @descriptor nearby strangers stranger pat on activity result
	 * @author Ares
	 * @version 1.0
	 */
	class NSSStrangerPatOnActivityResult implements ISSBaseActivityResult {

		@Override
		public void onActivityResult(int resultCode, Intent data) {
			// check the result code
			if (RESULT_OK == resultCode) {
				// get and check the extra data
				Bundle _extraData = data.getExtras();
				if (null != _extraData) {
					// get and check be pat stranger info bean
					UserInfoPatLocationExtBean _bePatStrangerInfo = (UserInfoPatLocationExtBean) _extraData
							.getSerializable(NearbyStrangersExtraData.NSS_SI_BEAN);
					if (null != _bePatStrangerInfo) {
						// traversal the nearby strangers info list
						for (int i = 0; i < strangerPatModel
								.getNearbyStrangersInfo().size(); i++) {
							// get each be pat stranger info and compare
							UserInfoPatLocationExtBean __bePatStrangerInfo = strangerPatModel
									.getNearbyStrangersInfo().get(i);
							if (_bePatStrangerInfo.getUserId() == __bePatStrangerInfo
									.getUserId()) {
								// update the nearby stranger be pat count
								strangerPatModel.getNearbyStrangersInfo().set(
										i, _bePatStrangerInfo);
								nearbyStrangerListViewAdapter.setPatCount(i,
										_bePatStrangerInfo.getPatCount());

								// break immediately
								break;
							}
						}
					} else {
						LOGGER.warning("Stranger pat error, the return be pat stranger info is null");
					}
				} else {
					LOGGER.warning("Stranger pat error, the return extra data is null");
				}
			}
		}

	}

	/**
	 * @name NearbyStrangersGenderSelectOnClickListener
	 * @descriptor nearby strangers gender select bar button item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class NearbyStrangersGenderSelectOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			LOGGER.info("NearbyStrangersGenderSelectOnClickListener");

			// test by ares

			//
		}

	}

	/**
	 * @name NearbyStrangerListViewAdapter
	 * @descriptor nearby stranger listView adapter
	 * @author Ares
	 * @version 1.0
	 */
	static class NearbyStrangerListViewAdapter extends SimpleAdapter {

		// logger
		private static final SSLogger LOGGER = new SSLogger(
				NearbyStrangerListViewAdapter.class);

		// nearby stranger id data key
		private static final String NEARBYSTRANGER_ID_KEY = "nearbyStranger_id_key";

		// context
		private Context context;

		// nearby stranger listView adapter data list
		private static List<Map<String, Object>> _sDataList = new ArrayList<Map<String, Object>>();

		/**
		 * @title NearbyStrangerListViewAdapter
		 * @descriptor nearby stranger listView adapter constructor with
		 *             context, nearby stranger list, content view resource,
		 *             content view subview data key, and content view subview
		 *             id
		 * @param context
		 *            : context
		 * @param nearbyStrangers
		 *            : nearby stranger with pat location list
		 * @param resource
		 *            : resource id
		 * @param dataKeys
		 *            : content view subview data key array
		 * @param ids
		 *            : content view subview id array
		 */
		public NearbyStrangerListViewAdapter(Context context,
				List<UserInfoPatLocationExtBean> nearbyStrangers, int resource,
				String[] dataKeys, int[] ids) {
			super(context, _sDataList = new ArrayList<Map<String, Object>>(),
					resource, dataKeys, ids);

			// save context
			this.context = context;

			// set nearby stranger list
			setNearbyStrangers(nearbyStrangers);
		}

		/**
		 * @title setNearbyStrangers
		 * @descriptor set new nearby stranger list
		 * @param nearbyStrangers
		 *            : new set nearby stranger list
		 */
		public void setNearbyStrangers(
				List<UserInfoPatLocationExtBean> nearbyStrangers) {
			// check new set nearby stranger list
			if (null != nearbyStrangers) {
				// clear nearby stranger listView adapter data list and add new
				// data
				_sDataList.clear();

				// traversal nearby stranger list
				for (UserInfoPatLocationExtBean nearbyStranger : nearbyStrangers) {
					// define nearby stranger listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// set data attributes
					_data.put(NEARBYSTRANGER_ID_KEY, nearbyStranger.getUserId());
					_data.put(
							NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_AVATAR_KEY
									.name(), nearbyStranger.getAvatarUrl());
					_data.put(
							NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_NICKNAME_KEY
									.name(), nearbyStranger.getNickname());
					switch (nearbyStranger.getGender()) {
					case MALE:
						_data.put(
								NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_GENDER_KEY
										.name(), R.drawable.img_gender_male);
						break;

					case FEMALE:
						_data.put(
								NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_GENDER_KEY
										.name(), R.drawable.img_gender_female);
						break;

					default:
						// nothing to do
						break;
					}
					// get step and distance
					int _step = Integer
							.parseInt(context
									.getString(R.string.nearbyStrangerItem_strangerDistance_step));
					double _distance = userLocation.getDistance(nearbyStranger
							.getLocation());
					_data.put(
							NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_DISTANCE_KEY
									.name(),
							String.format(
									context.getString(R.string.nearbyStrangerItem_strangerDistance_format),
									(0.0 == _distance % _step ? (int) (_distance / _step)
											: (int) (_distance / _step) + 1)
											* _step));
					// get stranger pat count
					int _patCount = nearbyStranger.getPatCount();
					if (0 != _patCount) {
						_data.put(
								NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_PATCOUNT_KEY
										.name(), _patCount);
					}

					// add data to list
					_sDataList.add(_data);
				}

				// notify data set changed
				notifyDataSetChanged();
			} else {
				LOGGER.error("Set new nearby stranger list error, the new set data list is null");
			}
		}

		/**
		 * @title getDistance
		 * @descriptor get the nearby stranger distance
		 * @param position
		 *            : the nearby stranger list position
		 * @return the nearby stranger distance
		 */
		public String getDistance(int position) {
			return (String) _sDataList
					.get(position)
					.get(NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_DISTANCE_KEY
							.name());
		}

		/**
		 * @title setPatCount
		 * @descriptor set pat the nearby stranger count
		 * @param position
		 *            : the nearby stranger list position
		 * @param patCount
		 *            : the pat count of pat the nearby stranger
		 */
		public void setPatCount(int position, int patCount) {
			// update the need to update stranger info
			_sDataList
					.get(position)
					.put(NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_PATCOUNT_KEY
							.name(), String.valueOf(patCount));

			// notify data set changed
			notifyDataSetChanged();
		}

		// inner class
		/**
		 * @name NearbyStrangerListViewAdapterKey
		 * @descriptor nearby stranger listView adapter key enumeration
		 * @author Ares
		 * @version 1.0
		 */
		public enum NearbyStrangerListViewAdapterKey {

			// nearby stranger avatar, nickname, gender, distance and pat count
			// key
			NEARBYSTRANGER_AVATAR_KEY, NEARBYSTRANGER_NICKNAME_KEY, NEARBYSTRANGER_GENDER_KEY, NEARBYSTRANGER_DISTANCE_KEY, NEARBYSTRANGER_PATCOUNT_KEY;

		}

	}

	/**
	 * @name NearbyStrangerItemOnClickListener
	 * @descriptor nearby stranger listView on item click listener
	 * @author Ares
	 * @version 1.0
	 */
	class NearbyStrangerItemOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// define nearby stranger info item pat extra data map
			Map<String, Object> _extraMap = new HashMap<String, Object>();

			// put the selected nearby stranger info and distance to extra data
			// map as param
			_extraMap.put(StrangerPatExtraData.SP_SI_BEAN, strangerPatModel
					.getNearbyStrangersInfo().get(position));
			_extraMap.put(StrangerPatExtraData.SP_STRANGER_DISTANCE,
					nearbyStrangerListViewAdapter.getDistance(position));

			// go to stranger pat activity with extra data map
			pushActivityForResult(StrangerPatActivity.class, _extraMap,
					NearbyStrangersRequestCode.SP_STRANGERPAT_REQCODE,
					new NSSStrangerPatOnActivityResult());
		}

	}

}
