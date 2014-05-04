/**
 * 
 */
package com.smartsport.spedometer.group.walk;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.group.GroupInviteInfoBean;
import com.smartsport.spedometer.mvc.ICMConnector;
import com.smartsport.spedometer.network.NetworkAdapter;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name WalkInviteModel
 * @descriptor walk invite model
 * @author Ares
 * @version 1.0
 */
public class WalkInviteModel {

	// logger
	private static final SSLogger LOGGER = new SSLogger(WalkInviteModel.class);

	//

	/**
	 * @title inviteWalk
	 * @descriptor invite one of user friends to walk together
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param inviteeId
	 *            : the friend id who been invite for walking together
	 * @param inviteInfo
	 *            : walk invite info
	 * @param executant
	 *            :
	 * @author Ares
	 */
	public void inviteWalk(int userId, String token, final int inviteeId,
			GroupInviteInfoBean inviteInfo, ICMConnector executant) {
		// invite one of user friends to walk together with his user id and walk
		// invite info
		((WalkInviteNetworkAdapter) NetworkAdapter.getInstance()
				.getWorkerNetworkAdapter(WalkInviteNetworkAdapter.class))
				.inviteWalk(userId, token, inviteeId, inviteInfo,
						new AsyncHttpRespJSONHandler() {

							@Override
							public void onSuccess(int statusCode,
									JSONArray respJSONArray) {
								// nothing to do
							}

							@Override
							public void onSuccess(int statusCode,
									JSONObject respJSONObject) {
								LOGGER.info("Invite one of user friends, whose id = "
										+ inviteeId
										+ " to walk together successful, status code = "
										+ statusCode
										+ " and response json object = "
										+ respJSONObject);

								// check invite one of user friends to walk
								// together response json object
								if (null != respJSONObject) {
									// get context
									Context _context = SSApplication
											.getContext();

									try {
										// get and check response result code
										int _respResultCode = Integer.parseInt(JSONUtils
												.getStringFromJSONObject(
														respJSONObject,
														_context.getString(R.string.walkInviteReqResp_serverAcceptResultCode)));
										if (Integer.parseInt(_context
												.getString(R.string.walkInviteReqResp_serverAccepted)) == _respResultCode) {
											// invite one of user friends to
											// walk together successful, remote
											// server accept the user's walk
											// invite
											//
										} else if (Integer.parseInt(_context
												.getString(R.string.walkInviteReqResp_serverRefused)) == _respResultCode) {
											// invite one of user friends to
											// walk together failed, remote
											// server refuse the user's walk
											// invite
											//
										} else {
											LOGGER.error("Invite one of user friends, whose id = "
													+ inviteeId
													+ " to walk together failed, response result code = "
													+ _respResultCode);

											// invite one of user friends to
											// walk together failed
											//
										}
									} catch (NumberFormatException e) {
										LOGGER.error("Invite one of user friends, whose id = "
												+ inviteeId
												+ " to walk together failed, exception message = "
												+ e.getMessage());

										// invite one of user friends to walk
										// together failed
										//

										e.printStackTrace();
									}
								} else {
									LOGGER.error("Invite one of user friends, whose id = "
											+ inviteeId
											+ " to walk together response json object is null");
								}
							}

							@Override
							public void onFailure(int statusCode,
									String errorMsg) {
								LOGGER.info("Invite one of user friends, whose id = "
										+ inviteeId
										+ " to walk together failed, status code = "
										+ statusCode
										+ " and error message = "
										+ errorMsg);

								// invite one of user friends to walk together
								// failed
								//
							}

						});
	}

}
