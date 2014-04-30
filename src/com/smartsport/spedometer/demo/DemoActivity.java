package com.smartsport.spedometer.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.group.GroupBean;
import com.smartsport.spedometer.group.info.ScheduleGroupInfoBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.user.UserInfoModel;
import com.smartsport.spedometer.utils.SSLogger;

public class DemoActivity extends Activity {

	// logger
	private static final SSLogger LOGGER = new SSLogger(DemoActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.activity_demo);

		// logger test
		LOGGER.debug("DemoActivity - onCreate, @debug");
		LOGGER.info("DemoActivity - onCreate, @info");
		LOGGER.warning("DemoActivity - onCreate, @warning");
		LOGGER.error("DemoActivity - onCreate, @error");

		// get api demo listView
		ListView _apiDemoListView = (ListView) findViewById(R.id.apiDemo_listView);

		// set adapter
		_apiDemoListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.apiDemo)));

		// set on item click listener
		_apiDemoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// user test
				UserInfoModel _userInfoModel = new UserInfoModel();

				// check position
				switch (position) {
				case 0:
					// get user info
					_userInfoModel.getUserInfo(1234, "AJH12AHG33",
							new ICMConnector() {

								//

							});

					//
					break;

				case 1:
					// update user info
					_userInfoModel.updateUserInfo(1234, "AJH12AHG33", 0, null,
							0.0f, 0.0f, new ICMConnector() {

								//

							});

					//
					break;

				case 2:
					try {
						//
						GroupBean _group = new GroupBean(
								new JSONObject(
										"{\"groupId\":\"1233123\",\"info\":{\"beginTime\":\"1397813119\",\"endTime\":\"1397813229\",\"content\":\"让我们一起走吧！\"},\"memberNumber\":\"2\"}"));

						LOGGER.info("@@, _group = " + _group);

						//
						ScheduleGroupInfoBean _scheduleGroupInfo = new ScheduleGroupInfoBean(
								_group,
								new JSONArray(
										"[{\"nickname\":\"小慧\",\"age\":\"18\",\"gender\":\"m\",\"height\":\"180\",\"weight\":\"65\",\"avatarUrl\":\"http://www.sina.com.cn\",\"status\":\"0\"}]"));

						LOGGER.info("@@, _scheduleGroupInfo = "
								+ _scheduleGroupInfo);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				default:
					LOGGER.debug("Api demo listView on item click, position = "
							+ position);
					break;
				}
			}
		});

		//
	}

}
