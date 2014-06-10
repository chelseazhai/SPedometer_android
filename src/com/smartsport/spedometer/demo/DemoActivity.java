package com.smartsport.spedometer.demo;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.user.UserGender;
import com.smartsport.spedometer.user.UserInfoModel;
import com.smartsport.spedometer.utils.JSONUtils;
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
					// // get user info
					// _userInfoModel.getUserInfo(123123, "翟绍虎@smartsport.com",
					// new ICMConnector() {
					//
					// //
					//
					// });

					// param
					JSONObject _param = new JSONObject();

					// head
					JSONObject _head = new JSONObject();
					JSONUtils.putObject2JSONObject(_head, "retStatus", "");
					JSONUtils.putObject2JSONObject(_head, "retMessage", "");
					JSONUtils.putObject2JSONObject(_head, "devId", "");
					JSONUtils.putObject2JSONObject(_head, "devType", "");
					JSONUtils.putObject2JSONObject(_head, "userType", "");
					JSONUtils.putObject2JSONObject(_head, "appId", "");
					JSONUtils.putObject2JSONObject(_head, "funcId", "");
					JSONUtils.putObject2JSONObject(_head, "userId", "");
					JSONUtils.putObject2JSONObject(_head, "accessToken", "");
					JSONUtils
							.putObject2JSONObject(_head, "appVersion", "1.1.1");
					JSONUtils.putObject2JSONObject(_head, "osVersion", "");

					JSONUtils.putObject2JSONObject(_param, "header", _head);

					// body
					@SuppressWarnings("unused")
					String _bodyString = "this is body";

					JSONUtils.putObject2JSONObject(_param, "body", null);

					// send test post http request
					try {
						new AsyncHttpClient()
								.post(DemoActivity.this,
										"http://192.168.0.81:8080/qmjs_FEP/place/queryDistrictsList.action",
										new StringEntity(_param.toString(),
												"UTF-8"),
										"text/json charset=\"utf-8\"",
										new TextHttpResponseHandler("UTF-8") {

											@Override
											public void onFailure(
													String responseBody,
													Throwable error) {
												super.onFailure(responseBody,
														error);

												LOGGER.info("Test http request failed, responseBody = "
														+ responseBody
														+ " and error = "
														+ error);

												//
											}

											@Override
											public void onSuccess(
													int statusCode,
													Header[] headers,
													String responseBody) {
												super.onSuccess(statusCode,
														headers, responseBody);

												LOGGER.info("Test http request successful, statusCode = "
														+ statusCode
														+ ", header = "
														+ headers
														+ " and responseBody = "
														+ responseBody);

												//
											}

										});

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//
					break;

				case 1:
					// update user info
					_userInfoModel.updateUserInfo(123123, "AJH12AHG33", null,
							UserGender.FEMALE, null, null, new ICMConnector() {

								@Override
								public void onSuccess(Object... retValue) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onFailure(int errorCode,
										String errorMsg) {
									// TODO Auto-generated method stub

								}

								//

							});
					break;

				case 2:
					// get user friend list
					_userInfoModel.getFriends(12123, "sdjh@s",
							new ICMConnector() {

								@Override
								public void onSuccess(Object... retValue) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onFailure(int errorCode,
										String errorMsg) {
									// TODO Auto-generated method stub

								}

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
