/**
 * 
 */
package com.smartsport.spedometer.strangersocial.pat;

import java.util.Map;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.network.INetworkAdapter;
import com.smartsport.spedometer.network.NetworkUtils;
import com.smartsport.spedometer.network.handler.AsyncHttpRespJSONHandler;
import com.smartsport.spedometer.strangersocial.LocationBean;
import com.smartsport.spedometer.user.UserGender;

/**
 * @name StrangerPatNetworkAdapter
 * @descriptor stranger pat network adapter
 * @author Ares
 * @version 1.0
 */
public class StrangerPatNetworkAdapter implements INetworkAdapter {

	/**
	 * @title getNearbyStrangers
	 * @descriptor get user nearby all strangers with location info from remote
	 *             server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param strangerGender
	 *            : need to get stranger gender
	 * @param location
	 *            : user location info
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getNearbyStrangers(long userId, String token,
			UserGender strangerGender, LocationBean location,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getNearbyStrangersReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// check user need to get stranger gender and set it to param
		if (null != strangerGender
				&& UserGender.GENDER_UNKNOWN != strangerGender) {
			_getNearbyStrangersReqParam.put(NETWORK_ENGINE.getContext()
					.getString(R.string.getNearbyUserReqParam_gender), String
					.valueOf(strangerGender.getValue()));
		}
		// test by ares
		else {
			_getNearbyStrangersReqParam.put(NETWORK_ENGINE.getContext()
					.getString(R.string.getNearbyUserReqParam_gender), null);
		}

		// check set user location info and set longitude and latitude to param
		if (null != location) {
			_getNearbyStrangersReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.getNearbyUserReqParam_locationLongitude),
					String.valueOf(location.getLongitude()));
			_getNearbyStrangersReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.getNearbyUserReqParam_locationLatitude),
					String.valueOf(location.getLatitude()));
		}

		// send get user nearby stranger list with location info asynchronous
		// post http request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getNearbyStranger_url),
				_getNearbyStrangersReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title patStranger
	 * @descriptor pat one of your nearby stranger with location info
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param strangerId
	 *            : nearby stranger id
	 * @param patLocation
	 *            : pat location info
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void patStranger(long userId, String token, long strangerId,
			LatLonPoint patLocation,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _patStrangerReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set user pat stranger id to param
		_patStrangerReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.patStrangerReqParam_pattedStrangerId),
				String.valueOf(strangerId));

		// check user pat location info and set longitude and latitude to param
		if (null != patLocation) {
			_patStrangerReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.patStrangerReqParam_patLocationLongitude),
					String.valueOf(patLocation.getLongitude()));
			_patStrangerReqParam.put(
					NETWORK_ENGINE.getContext().getString(
							R.string.patStrangerReqParam_patLocationLatitude),
					String.valueOf(patLocation.getLatitude()));
		}

		// send pat nearby stranger with location info asynchronous post
		// http request
		NETWORK_ENGINE
				.postWithAPI(
						NETWORK_ENGINE.getContext().getString(
								R.string.patStranger_url),
						_patStrangerReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title getPatStrangers
	 * @descriptor get all strangers user patted from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	public void getPatStrangers(long userId, String token,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// get user common request param
		Map<String, String> _getPatStrangersReqParam = NetworkUtils
				.genUserComReqParam(userId, token);

		// set stranger pat type to param
		_getPatStrangersReqParam.put(
				NETWORK_ENGINE.getContext().getString(
						R.string.getPattedStrangersReqParam_patType),
				NETWORK_ENGINE.getContext()
						.getString(R.string.patStranger_type));

		// send get pat stranger list who patted the user asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getPattedStrangers_url),
				_getPatStrangersReqParam, asyncHttpRespJSONHandler);
	}

	/**
	 * @title getPattedStrangers
	 * @descriptor get all strangers who patted the user from remote server
	 * @param userId
	 *            : user id
	 * @param token
	 *            : user token
	 * @param asyncHttpRespJSONHandler
	 *            : asynchronous http response json handler
	 * @author Ares
	 */
	@Deprecated
	public void getPattedStrangers(long userId, String token,
			AsyncHttpRespJSONHandler asyncHttpRespJSONHandler) {
		// send get stranger list who patted the user asynchronous post http
		// request
		NETWORK_ENGINE.postWithAPI(
				NETWORK_ENGINE.getContext().getString(
						R.string.getPattedStrangers_url),
				NetworkUtils.genUserComReqParam(userId, token),
				asyncHttpRespJSONHandler);
	}

}
