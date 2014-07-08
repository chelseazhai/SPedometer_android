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
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.customwidget.SSActionSheet;
import com.smartsport.spedometer.customwidget.SSBNavImageBarButtonItem;
import com.smartsport.spedometer.customwidget.SSProgressDialog;
import com.smartsport.spedometer.customwidget.SSSimpleAdapterViewBinder;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.mvc.ISSBaseActivityResult;
import com.smartsport.spedometer.mvc.SSBaseActivity;
import com.smartsport.spedometer.strangersocial.NearbyStrangersActivity.NearbyStrangerListViewAdapter.NearbyStrangerListViewAdapterKey;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatActivity;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatActivity.StrangerPatExtraData;
import com.smartsport.spedometer.strangersocial.pat.StrangerPatModel;
import com.smartsport.spedometer.strangersocial.pat.UserInfoPatLocationExtBean;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.user.UserManager;
import com.smartsport.spedometer.user.UserPedometerExtBean;
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
	private static StrangerPatModel sStrangerPatModel = StrangerPatModel
			.getInstance();

	// nearby strangers gender
	private static UserGender sStrangerGender;

	// user location info
	private static LocationBean sUserLocation;

	// nearby stranger listView adapter
	private static NearbyStrangerListViewAdapter nearbyStrangerListViewAdapter;

	// nearby strangers progress dialog
	private static SSProgressDialog nearbyStrangersProgDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get location manager
		LocationManager _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// load nearby strangers gender and user location info from local
		// storage
		// test by ares
		sStrangerGender = null;
		sUserLocation = new LocationBean(118.769790, 32.066336);
		//
		// get and check location
		Location _location = _locationManager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if (null != _location) {
			sUserLocation = new LocationBean(_location.getLongitude(),
					_location.getLatitude());
		}

		// // test by ares
		// for (int i = 0; i < 12; i++) {
		// UserInfoPatLocationExtBean _uipl = new UserInfoPatLocationExtBean();
		//
		// _uipl.setUserId(10010 + i);
		// _uipl.setAvatarUrl("/avatar/img_123jhsd_sdjhf");
		// _uipl.setNickname("用户" + i);
		// _uipl.setGender(0 == i ? UserGender.GENDER_UNKNOWN
		// : 0 == i % 2 ? UserGender.FEMALE : UserGender.MALE);
		// _uipl.setAge(10 + i);
		// _uipl.setHeight(170.0f + i);
		// _uipl.setWeight(70.0f + i);
		// _uipl.setLocation(new LocationBean(sUserLocation.getLongitude()
		// + (0 == i ? 10 : i / 100.0), sUserLocation.getLatitude()
		// + i / 100.0));
		// _uipl.setPatCount(i);
		//
		// sStrangerPatModel.getNearbyStrangersInfo().add(_uipl);
		// }

		// set content view
		setContentView(R.layout.activity_nearby_strangers);

		// get user nearby strangers info
		getNearbyStrangers(this);
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
				new NearbyStrangersMoreOperationBarBtnItemOnClickListener()));

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
						sStrangerPatModel.getNearbyStrangersInfo(),
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
								R.id.nsi_strangerAvatar_imageView,
								R.id.nsi_strangerNickname_textView,
								R.id.nsi_strangerGender_imageView,
								R.id.nsi_strangerDistance_textView,
								R.id.nsi_strangerPatCount_textView }));

		// set its on item click listener
		_nearbyStrangerListView
				.setOnItemClickListener(new NearbyStrangerItemOnClickListener());
	}

	/**
	 * @title getNearbyStrangers
	 * @descriptor get user nearby strangers info
	 * @param context
	 *            : context
	 * @author Ares
	 */
	private static void getNearbyStrangers(final Context context) {
		// get pedometer login user
		UserPedometerExtBean _loginUser = (UserPedometerExtBean) UserManager
				.getInstance().getLoginUser();

		// show get user nearby strangers progress dialog
		nearbyStrangersProgDlg = SSProgressDialog.show(context,
				R.string.procMsg_getUserNearbyStrangers);

		// get nearby strangers with user location info from remote server
		sStrangerPatModel.getNearbyStrangers(_loginUser.getUserId(),
				_loginUser.getUserKey(), sStrangerGender, sUserLocation,
				new ICMConnector() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object... retValue) {
						// dismiss get user nearby strangers progress dialog
						nearbyStrangersProgDlg.dismiss();

						// check return values
						if (null != retValue
								&& 0 < retValue.length
								&& retValue[retValue.length - 1] instanceof List) {
							// set user nearby stranger list for patting
							nearbyStrangerListViewAdapter
									.setNearbyStrangers((List<UserInfoPatLocationExtBean>) retValue[retValue.length - 1]);
						} else {
							LOGGER.error("Update user nearby strangers listView error");
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMsg) {
						LOGGER.error("Get user nearby strangers info from remote server error, error code = "
								+ errorCode + " and message = " + errorMsg);

						// dismiss get user nearby strangers progress dialog
						nearbyStrangersProgDlg.dismiss();

						// check error code and process hopeRun business error
						if (errorCode < 100) {
							// show error message toast
							Toast.makeText(context, errorMsg,
									Toast.LENGTH_SHORT).show();

							// test by ares
							//
						}
					}

				});
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
						for (int i = 0; i < sStrangerPatModel
								.getNearbyStrangersInfo().size(); i++) {
							// get each be pat stranger info and compare
							UserInfoPatLocationExtBean __bePatStrangerInfo = sStrangerPatModel
									.getNearbyStrangersInfo().get(i);
							if (_bePatStrangerInfo.getUserId() == __bePatStrangerInfo
									.getUserId()) {
								// update the nearby stranger be pat count
								sStrangerPatModel.getNearbyStrangersInfo().set(
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
	 * @name NearbyStrangersMoreOperationBarBtnItemOnClickListener
	 * @descriptor nearby strangers more operation bar button item on click
	 *             listener
	 * @author Ares
	 * @version 1.0
	 */
	class NearbyStrangersMoreOperationBarBtnItemOnClickListener implements
			OnClickListener {

		// nearby strangers more operation action sheet
		private NearbyStrangersMoreOperationActionSheet moreOperationActionSheet;

		@Override
		public void onClick(View v) {
			// check the nearby strangers more operation action sheet
			if (null == moreOperationActionSheet) {
				moreOperationActionSheet = new NearbyStrangersMoreOperationActionSheet(
						v.getContext());
			}

			// show nearby strangers more operation action sheet animation
			moreOperationActionSheet.showAtLocationAnimation(v, Gravity.CENTER,
					0, 0);
		}

	}

	/**
	 * @name NearbyStrangersMoreOperationActionSheet
	 * @descriptor nearby strangers more operation action sheet
	 * @author Ares
	 * @version 1.0
	 */
	static class NearbyStrangersMoreOperationActionSheet extends SSActionSheet {

		// context
		private static Context _sContext;

		/**
		 * @title NearbyStrangersMoreOperationActionSheet
		 * @descriptor nearby strangers more operation action sheet constructor
		 * @param context
		 *            : context
		 * @author Ares
		 */
		public NearbyStrangersMoreOperationActionSheet(Context context) {
			super(R.layout.nearbystrangers_moreoperation_actionsheet_layout);

			// save context
			_sContext = context;
		}

		@Override
		protected void initContentViewUI() {
			// get operate gridView
			GridView _operateGridView = (GridView) findViewById(R.id.nss_moreOperationActionSheet_operation_gridView);

			// set its adapter
			_operateGridView
					.setAdapter(new OperateGridViewAdapter(
							_operateGridView.getContext(),
							new int[] {
									R.drawable.img_nearbystrangers_gender_maleonly,
									R.drawable.img_nearbystrangers_gender_femaleonly,
									R.drawable.img_nearbystrangers_gender_all,
									R.drawable.img_nearbystrangers_patyou,
									R.drawable.img_clear_locationinfo,
									R.array.nearbyStrangerMoreOperation_operate_tips },
							R.layout.nearbystrangers_moreoperation_actionsheet_operationgridview_item_layout,
							new String[] {
									OperateGridViewAdapter.OperateGridViewAdapterKey.MOREOPERATION_OPERATE_ICON_KEY
											.name(),
									OperateGridViewAdapter.OperateGridViewAdapterKey.MOREOPERATION_OPERATE_TIP_KEY
											.name() }, new int[] {
									R.id.nss_moas_og_operate_icon_imageView,
									R.id.nss_moas_og_operate_tip_textView }));

			// set its on item click listener
			_operateGridView
					.setOnItemClickListener(new OperateGridViewOnItemClickListener());

			// set cancel button on click listener
			((Button) findViewById(R.id.nss_moreOperationActionSheet_cancel_button))
					.setOnClickListener(new CancelBtnOnClickListener());
		}

		@Override
		protected void clearContentViewUICache() {
			// nothing to do
		}

		// inner class
		/**
		 * @name OperateGridViewAdapter
		 * @descriptor nearby strangers more operation action sheet operate
		 *             gridView adapter
		 * @author Ares
		 * @version 1.0
		 */
		static class OperateGridViewAdapter extends SimpleAdapter {

			// logger
			private static final SSLogger LOGGER = new SSLogger(
					OperateGridViewAdapter.class);

			// nearby stranger more operation action sheet operate gridView
			// adapter data list
			private static List<Map<String, Object>> _sDataList;

			// nearby stranger more operation action sheet operate action map
			private final SparseArray<OnClickListener> OPERATEACTION_MAP = new SparseArray<View.OnClickListener>();

			/**
			 * @title OperateGridViewAdapter
			 * @descriptor nearby stranger more operation action sheet operate
			 *             gridView adapter constructor with context, operate
			 *             resource id list, content view resource, content view
			 *             subview data key, and content view subview id
			 * @param context
			 *            : context
			 * @param operateResIds
			 *            : operate icon resource id list and tip array id
			 * @param resource
			 *            : resource id
			 * @param dataKeys
			 *            : content view subview data key array
			 * @param ids
			 *            : content view subview id array
			 */
			public OperateGridViewAdapter(Context context, int[] operateResIds,
					int resource, String[] dataKeys, int[] ids) {
				super(context,
						_sDataList = new ArrayList<Map<String, Object>>(),
						resource, dataKeys, ids);

				// check the operate resource id array
				if (null != operateResIds && 2 <= operateResIds.length) {
					// get and check operate tip array
					String[] _operateTips = context.getResources()
							.getStringArray(
									operateResIds[operateResIds.length - 1]);
					if (null != _operateTips
							&& operateResIds.length - 1 == _operateTips.length) {
						// define get nearby strangers with gender operate
						// action and operate action
						GetNearbyStrangersWithGenderOperate _getNearbyStrangersWithGenderOperateAction = new GetNearbyStrangersWithGenderOperate();
						OnClickListener _operateAction = _getNearbyStrangersWithGenderOperateAction;

						for (int i = 0; i < _operateTips.length; i++) {
							// define more operation action sheet operate
							// gridView adapter data
							Map<String, Object> _data = new HashMap<String, Object>();

							// set data attributes
							_data.put(
									OperateGridViewAdapterKey.MOREOPERATION_OPERATE_ICON_KEY
											.name(), operateResIds[i]);
							_data.put(
									OperateGridViewAdapterKey.MOREOPERATION_OPERATE_TIP_KEY
											.name(), _operateTips[i]);

							// add data to list
							_sDataList.add(_data);

							// set nearby stranger more operation action sheet
							// operate action
							switch (i) {
							case 0:
							case 1:
							case 2:
								// get nearby strangers with user select gender
								_operateAction = _getNearbyStrangersWithGenderOperateAction;
								break;

							case 3:
								// get pat you nearby all strangers
								_operateAction = new GetPatYouNearbyStrangersOperate();
								break;

							case 4:
								// clear user location info and exit
								_operateAction = new ClearUserLocationInfoOperate();
								break;
							}

							// put operate action to operate action map
							OPERATEACTION_MAP.put(i, _operateAction);
						}

						// notify data set changed
						notifyDataSetChanged();
					} else {
						LOGGER.error("Initialize nearby strangers operation action sheet operate gridView adapter error, operate resource id array = "
								+ operateResIds
								+ " not matched, operate icon array count = "
								+ (operateResIds.length - 1)
								+ " and operate tip array count = "
								+ _operateTips.length);
					}
				} else {
					LOGGER.error("Initialize nearby strangers operation action sheet operate gridView adapter error, operate resouce id array = "
							+ operateResIds + " count less");
				}
			}

			/**
			 * @title getOperateAction
			 * @descriptor get nearby strangers more operation operate action
			 *             with position
			 * @param position
			 *            : position of the item whose data we want within the
			 *            adapter's data set
			 * @return more operation operate action
			 * @author Ares
			 */
			public OnClickListener getOperateAction(int position) {
				// get the operate action with position
				OnClickListener _operateAction = OPERATEACTION_MAP
						.get(position);

				// check the position and set get nearby strangers gender
				if (getCount() - 2 > position) {
					((GetNearbyStrangersWithGenderOperate) _operateAction)
							.setGender(UserGender.values()[position]);
				}

				return _operateAction;
			}

			// inner class
			/**
			 * @name OperateGridViewAdapterKey
			 * @descriptor nearby strangers more operation action sheet operate
			 *             gridView adapter key enumeration
			 * @author Ares
			 * @version 1.0
			 */
			public enum OperateGridViewAdapterKey {

				// more operation action sheet operate icon and tip key
				MOREOPERATION_OPERATE_ICON_KEY, MOREOPERATION_OPERATE_TIP_KEY;

			}

			/**
			 * @name GetNearbyStrangersWithGenderOperate
			 * @descriptor get nearby strangers with gender operate on click
			 *             listener
			 * @author Ares
			 * @version 1.0
			 */
			class GetNearbyStrangersWithGenderOperate implements
					OnClickListener {

				// nearby strangers gender
				private UserGender gender;

				public void setGender(UserGender gender) {
					this.gender = gender;
				}

				@Override
				public void onClick(View v) {
					// save user select nearby strangers gender to local storage
					// test by ares
					sStrangerGender = gender;
					//

					// get user nearby strangers info
					getNearbyStrangers(_sContext);
				}

			}

			/**
			 * @name GetPatYouNearbyStrangersOperate
			 * @descriptor get pat you nearby strangers operate on click
			 *             listener
			 * @author Ares
			 * @version 1.0
			 */
			class GetPatYouNearbyStrangersOperate implements OnClickListener {

				@Override
				public void onClick(View v) {
					LOGGER.info("get pat you all nearby strangers");

					//
				}

			}

			/**
			 * @name ClearUserLocationInfoOperate
			 * @descriptor clear user location info and exit operate on click
			 *             listener
			 * @author Ares
			 * @version 1.0
			 */
			class ClearUserLocationInfoOperate implements OnClickListener {

				@Override
				public void onClick(View v) {
					// clear user location info from local storage
					// test by ares
					sUserLocation = null;
					//
				}

			}

		}

		/**
		 * @name OperateGridViewOnItemClickListener
		 * @descriptor nearby strangers more operation action sheet operate
		 *             gridView on item click listener
		 * @author Ares
		 * @version 1.0
		 */
		class OperateGridViewOnItemClickListener implements OnItemClickListener {

			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, long id) {
				// dismiss more operation action sheet animation
				dismissAnimation();

				// nearby strangers more operation action sheet operate
				// performed using an new handle
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						((OperateGridViewAdapter) parent.getAdapter())
								.getOperateAction(position).onClick(view);
					}

				}, getDismissDuration(true));
			}

		}

		/**
		 * @name CancelBtnOnClickListener
		 * @descriptor nearby strangers more operation action sheet cancel
		 *             button on click listener
		 * @author Ares
		 * @version 1.0
		 */
		class CancelBtnOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// dismiss more operation action sheet animation
				dismissAnimation();
			}

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

		// context
		private Context context;

		// nearby stranger listView adapter data list
		private static List<Map<String, Object>> _sDataList;

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

			// set view binder
			setViewBinder(new SSSimpleAdapterViewBinder());

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
				for (UserInfoPatLocationExtBean _nearbyStranger : nearbyStrangers) {
					// define nearby stranger listView adapter data
					Map<String, Object> _data = new HashMap<String, Object>();

					// set data attributes
					_data.put(
							NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_AVATAR_KEY
									.name(), _nearbyStranger.getAvatarUrl());
					_data.put(
							NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_NICKNAME_KEY
									.name(), _nearbyStranger.getNickname());
					switch (_nearbyStranger.getGender()) {
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
					double _distance = sUserLocation
							.getDistance(_nearbyStranger.getLocation());
					_data.put(
							NearbyStrangerListViewAdapterKey.NEARBYSTRANGER_DISTANCE_KEY
									.name(),
							String.format(
									context.getString(R.string.nearbyStrangerItem_strangerDistance_format),
									(0.0 == _distance % _step ? (int) (_distance / _step)
											: (int) (_distance / _step) + 1)
											* _step));
					// get stranger pat count
					int _patCount = _nearbyStranger.getPatCount();
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

			// put the selected nearby stranger info, distance and user pat
			// location to extra data map as param
			_extraMap.put(StrangerPatExtraData.SP_SI_BEAN, sStrangerPatModel
					.getNearbyStrangersInfo().get(position));
			_extraMap.put(StrangerPatExtraData.SP_STRANGER_DISTANCE,
					nearbyStrangerListViewAdapter.getDistance(position));
			_extraMap.put(StrangerPatExtraData.SP_USER_PATLOCATION,
					sUserLocation);

			// go to stranger pat activity with extra data map
			pushActivityForResult(StrangerPatActivity.class, _extraMap,
					NearbyStrangersRequestCode.SP_STRANGERPAT_REQCODE,
					new NSSStrangerPatOnActivityResult());
		}

	}

}
