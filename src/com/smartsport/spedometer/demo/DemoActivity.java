package com.smartsport.spedometer.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.user.UserGender;
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
				// user
				case 0:
					// get user info
					_userInfoModel.getUserInfo(123123, "翟绍虎@smartsport.com",
							new ICMConnector() {

								//

							});
					break;

				case 1:
					// update user info
					_userInfoModel.updateUserInfo(123123, "AJH12AHG33", null,
							UserGender.FEMALE, null, null, new ICMConnector() {

								//

							});
					break;

				case 2:
					// get user friend list
					_userInfoModel.getFriends(12123, "sdjh@s",
							new ICMConnector() {

								//

							});
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
