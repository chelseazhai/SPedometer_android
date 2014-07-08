/**
 * 
 */
package com.smartsport.spedometer.group;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.network.INetworkAdapter;
import com.smartsport.spedometer.network.NetworkUtils;
import com.smartsport.spedometer.network.handler.AsyncHttpRespFileHandler;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.network.handler.IAsyncHttpRespHandler.NetworkPedometerReqRespStatusConstant;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupInfoNetworkAdapter
 * @descriptor walk and compete group info network adapter
 * @author Ares
 * @version 1.0
 */
public class GroupInfoNetworkAdapter implements INetworkAdapter {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			GroupInfoNetworkAdapter.class);

	/**
	 * @title getUserScheduleGroups
	 * @descriptor get user all schedule groups with type from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupType
	 *            : walk or compete group type
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserScheduleGroups(long userId, String token,
			GroupType groupType,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getUserScheduleGroupsReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user schedule group type to param
		_getUserScheduleGroupsReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getScheduleGroupsReqParam_groupType),
				String.valueOf(groupType.getValue()));

		// send get user schedule group list with type asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getScheduleGroups_url),
				_getUserScheduleGroupsReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title getUserScheduleGroupInfo
	 * @descriptor get user schedule group info from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : walk or compete group id
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserScheduleGroupInfo(long userId, String token,
			String groupId, AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getUserScheduleGroupInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user schedule group id to param
		_getUserScheduleGroupInfoReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getScheduleGroupInfoReqParam_groupId),
				groupId);

		// send get user schedule group info asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getScheduleGroupInfo_url),
				_getUserScheduleGroupInfoReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title shareGroupResult2Moments
	 * @descriptor share walk invite or within group compete walk result
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param resultImg
	 *            : walk invite or within group compete walk result image
	 * @param description
	 *            : walk result share description message
	 * @param label
	 *            : walk result share label
	 * @param specialReminds
	 *            : share the walk result image special remind friends
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void shareGroupResult2Moments(final long userId, final String token,
			Bitmap resultImg, final String description, final String label,
			final List<Long> specialReminds,
			final AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// step 1: upload the walk invite or within group compete walk result
		// image
		// check result image
		if (null != resultImg) {
			// define upload the walk result image handle
			final Handler _uploadResultImgHandle = new Handler();

			// upload the walk invite or within group compete walk result image
			// generate the walk result image key
			final String _resultImgKey = String.valueOf(System
					.currentTimeMillis());

			// define upload walk result image file name
			String _uploadWalkResultImgFileName = _resultImgKey + ".png";

			// upload the walk result image to server
			// get user common request param
			// Map<String, String> _uploadResultImgReqParam = NetworkUtils
			// .genUserComReqParam(userId, token);
			Map<String, String> _uploadResultImgReqParam = new HashMap<String, String>();
			_uploadResultImgReqParam.put("userid", String.valueOf(userId));

			// set result image foreign key, image type, module name, upload
			// type, file name and file map relationship to param
			_uploadResultImgReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_foreignKeyId),
					_resultImgKey);
			_uploadResultImgReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_imgFileType),
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_momentImgFile));
			_uploadResultImgReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_moduleName),
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_momentModuleName));
			_uploadResultImgReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_imgFileUploadType),
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_newAddedImgFile));
			_uploadResultImgReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_uploadFileName),
					_uploadWalkResultImgFileName);
			_uploadResultImgReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_uploadFile),
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFileReqParam_singleMR));

			// create temp upload walk result image file
			File _tmpUploadWalkResultImgFile = new File(NETWORK_ENGINE
					.getContext().getDir(String.valueOf(userId),
							Context.MODE_PRIVATE), _uploadWalkResultImgFileName);

			// define temp upload walk result image file output stream
			FileOutputStream _fops = null;

			try {
				// open file output stream
				_fops = new FileOutputStream(_tmpUploadWalkResultImgFile);

				// compress the walk result image bitmap to temp file
				resultImg.compress(Bitmap.CompressFormat.PNG, 100, _fops);
			} catch (FileNotFoundException e) {
				LOGGER.error("");

				e.printStackTrace();
			} finally {
				// close the opened file output stream
				try {
					_fops.close();
				} catch (IOException e) {
					LOGGER.error("");

					e.printStackTrace();
				}
			}

			LOGGER.info("File path = "
					+ _tmpUploadWalkResultImgFile.getAbsolutePath()
					+ " and length = " + _tmpUploadWalkResultImgFile.length());

			// send upload the walk result image asynchronous post http request
			NETWORK_ENGINE.postWithAPI(
					NETWORK_ENGINE.getContext().getString(
							R.string.uploadFile_url), _uploadResultImgReqParam,
					_tmpUploadWalkResultImgFile,
					new AsyncHttpRespFileHandler() {

						@Override
						public void onSuccess(int statusCode) {
							// delete temp upload walk result image file
							//

							// step 2 : generate user moments and publish it
							// share the walk result image to user moments and
							// publish it
							// get user common request param
							final Map<String, Object> _shareWalkResultImgReqParam = new HashMap<String, Object>(
									NetworkUtils.genUserComReqParam(userId,
											token));

							// set result image primary key, description, label,
							// delete picture list, is public flag, public group
							// list, special remind friends and is synchronous
							// show flag to param
							_shareWalkResultImgReqParam
									.put(NETWORK_ENGINE
											.getContext()
											.getString(
													R.string.newMomentReqParam_primaryKey),
											_resultImgKey);
							_shareWalkResultImgReqParam
									.put(NETWORK_ENGINE
											.getContext()
											.getString(
													R.string.newMomentReqParam_description),
											description);
							_shareWalkResultImgReqParam.put(
									NETWORK_ENGINE.getContext().getString(
											R.string.newMomentReqParam_label),
									label);
							_shareWalkResultImgReqParam
									.put(NETWORK_ENGINE
											.getContext()
											.getString(
													R.string.newMomentReqParam_deletePicIds),
											new ArrayList<String>());
							_shareWalkResultImgReqParam
									.put(NETWORK_ENGINE
											.getContext()
											.getString(
													R.string.newMomentReqParam_isPublic),
											NETWORK_ENGINE
													.getContext()
													.getString(
															R.string.newMomentReqParam_momentPublic));
							_shareWalkResultImgReqParam
									.put(NETWORK_ENGINE
											.getContext()
											.getString(
													R.string.newMomentReqParam_publicGroups),
											new ArrayList<String>());
							_shareWalkResultImgReqParam
									.put(NETWORK_ENGINE
											.getContext()
											.getString(
													R.string.newMomentReqParam_specialRemindFriends),
											specialReminds);
							_shareWalkResultImgReqParam
									.put(NETWORK_ENGINE
											.getContext()
											.getString(
													R.string.newMomentReqParam_isSyncShow),
											NETWORK_ENGINE
													.getContext()
													.getString(
															R.string.newMomentReqParam_syncShow));

							// send share the walk result image to user moments
							// asynchronous post http request
							_uploadResultImgHandle.post(new Runnable() {

								@Override
								public void run() {
									NETWORK_ENGINE
											.postWithAPI(
													NETWORK_ENGINE
															.getContext()
															.getString(
																	R.string.momentsModule),
													NETWORK_ENGINE
															.getContext()
															.getString(
																	R.string.newMoment_url),
													_shareWalkResultImgReqParam,
													asyncHttpRespJSONHandler);
								}

							});
						}

						@Override
						public void onFailure(int statusCode, String errorMsg) {
							// check asynchronous http response json handler
							if (null != asyncHttpRespJSONHandler) {
								asyncHttpRespJSONHandler.onFailure(statusCode,
										errorMsg);
							}
						}

					});
		} else {
			LOGGER.error("The walk invite or within group compete walk result image is null, not need to upload");

			// check asynchronous http response json handler
			if (null != asyncHttpRespJSONHandler) {
				asyncHttpRespJSONHandler.onFailure(
						NetworkPedometerReqRespStatusConstant.NULL_RESPBODY,
						"Upload file is null");
			}
		}
	}

	/**
	 * @title getUserHistoryGroups
	 * @descriptor get user all history groups from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupType
	 *            : walk or compete group type
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserHistoryGroups(long userId, String token,
			GroupType groupType,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getUserHistoryGroupsReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user history group type to param
		_getUserHistoryGroupsReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getHistoryGroupsReqParam_groupType), String
				.valueOf(groupType.getValue()));

		// send get user history group list asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getHistoryGroups_url),
				_getUserHistoryGroupsReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title getUserHistoryGroupInfo
	 * @descriptor get user history group info from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param groupId
	 *            : walk or compete group id
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getUserHistoryGroupInfo(long userId, String token,
			String groupId, AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getUserHistoryGroupInfoReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user history group id to param
		_getUserHistoryGroupInfoReqParam.put(NETWORK_ENGINE.getContext()
				.getString(R.string.getHistoryGroupInfoReqParam_groupId),
				groupId);

		// send get user history group info asynchronous post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getHistoryGroupInfo_url),
				_getUserHistoryGroupInfoReqParam, asyncHttpRespJSONHandler);
	}

}
