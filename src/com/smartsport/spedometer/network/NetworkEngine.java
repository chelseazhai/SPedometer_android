/**
 * 
 */
package com.smartsport.spedometer.network;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.network.handler.AsyncHttpRespFileHandler;
import com.smartsport.spedometer.network.handler.AsyncHttpRespStringHandler;
import com.smartsport.spedometer.network.hoperun.HopeRunHttpReqEntity;
import com.smartsport.spedometer.network.hoperun.HopeRunHttpReqRespEntity;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name NetworkEngine
 * @descriptor network engine
 * @author Ares
 * @version 1.0
 */
public class NetworkEngine {

	// logger
	private static final SSLogger LOGGER = new SSLogger(NetworkEngine.class);

	// http and https request url prefix
	private static final String HTTPREQ_URLPREFIX = "http://";
	private static final String HTTPSREQ_URLPREFIX = "https://";

	// singleton instance
	private static volatile NetworkEngine _sSingletonNetworkEngine;

	// context
	private Context context;

	// asynchronous http client
	private AsyncHttpClient asyncHttpClient;

	/**
	 * @title NetworkEngine
	 * @descriptor network engine private constructor
	 * @author Ares
	 */
	private NetworkEngine() {
		super();

		// get context
		context = SSApplication.getContext();

		// initialize asynchronous http client
		asyncHttpClient = new AsyncHttpClient();
	}

	/**
	 * @title getInstance
	 * @descriptor get network engine singleton instance
	 * @return network engine singleton instance
	 * @author Ares
	 */
	public static NetworkEngine getInstance() {
		if (null == _sSingletonNetworkEngine) {
			synchronized (NetworkEngine.class) {
				if (null == _sSingletonNetworkEngine) {
					_sSingletonNetworkEngine = new NetworkEngine();
				}
			}
		}

		return _sSingletonNetworkEngine;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * @title getWithAPI
	 * @descriptor send asynchronous get http request with relative url and
	 *             parameter, then handle with response json handler
	 * @param api
	 *            : relative url
	 * @param parameter
	 *            : asynchronous get http request parameter
	 * @param asyncHttpRespStringHandler
	 *            : asynchronous http response string handler
	 * @author Ares
	 */
	public void getWithAPI(String api, Map<String, String> parameter,
			AsyncHttpRespStringHandler asyncHttpRespStringHandler) {
		LOGGER.debug("Send asynchronous get http request, relative url = "
				+ api + ", parameter = " + parameter
				+ " and response string handler = "
				+ asyncHttpRespStringHandler);

		// get asynchronous http request
		asyncHttpClient.get(genHttpRequestUrl(api),
				new RequestParams(parameter),
				new TextAsyncHttpRespStringHandler(asyncHttpRespStringHandler));
	}

	/**
	 * @title postWithAPI
	 * @descriptor send asynchronous post http request with relative url and
	 *             parameter, then handle with response json handler
	 * @param api
	 *            : relative url
	 * @param parameter
	 *            : asynchronous post http request parameter
	 * @param asyncHttpRespStringHandler
	 *            : asynchronous http response string handler
	 * @author Ares
	 */
	public void postWithAPI(String api, Map<String, String> parameter,
			AsyncHttpRespStringHandler asyncHttpRespStringHandler) {
		LOGGER.debug("Send asynchronous post http request, relative url = "
				+ api + ", parameter = " + parameter
				+ " and response string handler = "
				+ asyncHttpRespStringHandler);

		// get, check json string parameter and then post asynchronous http
		// request
		StringEntity _jsonStringEntity = new HopeRunHttpReqEntity(parameter)
				.genReqStringEntity();
		try {
			LOGGER.info("Http request string entity = "
					+ EntityUtils.toString(_jsonStringEntity));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != _jsonStringEntity) {
			asyncHttpClient.post(context, genHttpRequestUrl(api),
					_jsonStringEntity, "text/json charset=\"utf-8\"",
					new TextAsyncHttpRespStringHandler(
							asyncHttpRespStringHandler));
		}

		// asyncHttpClient.post(genHttpRequestUrl(api), new RequestParams(
		// parameter), new TextAsyncHttpRespStringHandler(
		// asyncHttpRespStringHandler));
	}

	/**
	 * @title postWithAPI
	 * @descriptor send asynchronous post http request with relative url,
	 *             parameter and upload files, then handle with response json
	 *             handler
	 * @param api
	 *            : relative url
	 * @param parameter
	 *            : asynchronous post http request parameter
	 * @param files
	 *            : upload files
	 * @param asyncHttpRespFileHandler
	 *            : asynchronous http response file handler
	 * @author Ares
	 */
	public void postWithAPI(String api, Map<String, String> parameter,
			List<File> files, AsyncHttpRespFileHandler asyncHttpRespFileHandler) {
		//
	}

	/**
	 * @title genHttpRequestUrl
	 * @descriptor generate http request url with relative url
	 * @param api
	 *            : relative url
	 * @return http request url
	 * @author Ares
	 */
	private String genHttpRequestUrl(String api) {
		// define http request url string builder
		StringBuilder _httpRequestUrl = new StringBuilder(
				context.getString(R.string.server_rootUrl));

		// check http request root url
		if (!_httpRequestUrl.toString().startsWith(HTTPREQ_URLPREFIX)
				&& !_httpRequestUrl.toString().startsWith(HTTPSREQ_URLPREFIX)) {
			// add http request url prefix at first
			_httpRequestUrl.insert(0, HTTPREQ_URLPREFIX);
		}

		// check api
		if (null != api) {
			_httpRequestUrl.append(api);
		}

		return _httpRequestUrl.toString();
	}

	// inner class
	/**
	 * @name TextAsyncHttpRespStringHandler
	 * @descriptor text asynchronous http response string handler
	 * @author Ares
	 * @version 1.0
	 */
	class TextAsyncHttpRespStringHandler extends TextHttpResponseHandler {

		// logger
		private final SSLogger LOGGER = new SSLogger(
				TextAsyncHttpRespStringHandler.class);

		// asynchronous http response string handler
		private AsyncHttpRespStringHandler asyncHttpRespStringHandler;

		/**
		 * @title TextAsyncHttpRespStringHandler
		 * @descriptor text asynchronous http response string handler
		 *             constructor
		 * @param asyncHttpRespStringHandler
		 *            : asynchronous http response string handler
		 */
		private TextAsyncHttpRespStringHandler(
				AsyncHttpRespStringHandler asyncHttpRespStringHandler) {
			super("UTF-8");

			// save asynchronous http response string handler
			this.asyncHttpRespStringHandler = asyncHttpRespStringHandler;
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				String responseBody) {
			LOGGER.debug("Text asynchronous http response string handler handle successful, status code = "
					+ statusCode
					+ ", headers = "
					+ headers
					+ " and response body = " + responseBody);

			// check asynchronous http response string handler
			if (null != asyncHttpRespStringHandler) {
				try {
					// get hope run http request response entity
					HopeRunHttpReqRespEntity _hopeRunHttpReqRespEntity = new HopeRunHttpReqRespEntity(
							statusCode, new JSONObject(responseBody));

					// get and check status code
					Integer _statusCode = _hopeRunHttpReqRespEntity
							.getStatusCode();
					if (statusCode == _statusCode) {
						// asynchronous http response string handle successful
						asyncHttpRespStringHandler.onSuccess(statusCode,
								_hopeRunHttpReqRespEntity.getBody());
					} else {
						// asynchronous http response string handle failed
						asyncHttpRespStringHandler.onFailure(_statusCode,
								_hopeRunHttpReqRespEntity.getStatusMsg());
					}
				} catch (JSONException e) {
					LOGGER.error("Get hope run http request response entity json object error, exception message = "
							+ e.getMessage());

					e.printStackTrace();
				}
			} else {
				LOGGER.warning("Text asynchronous http response string handler is null, not need to throw up");
			}

			// super.onSuccess(statusCode, headers, responseBody);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseBody, Throwable error) {
			LOGGER.debug("Text asynchronous http response string handler handle failed, status code = "
					+ statusCode
					+ ", headers = "
					+ headers
					+ ", response body = "
					+ responseBody
					+ " and error message = " + error.getMessage());

			error.printStackTrace();

			// remote background server http request error process
			if (0 == statusCode) {
				// check error
				if (error instanceof ConnectTimeoutException
						|| error instanceof SocketTimeoutException) {
					// timeout
					//
				} else if (error instanceof UnknownHostException) {
					// unknown host
					//
				}
			} else {
				// check http request response status code
				switch (statusCode / 100) {
				case 4:
				case 5:
					// remote background server http request error
					//
					break;
				}
			}

			// check asynchronous http response string handler
			if (null != asyncHttpRespStringHandler) {
				// asynchronous http response string handle failed
				asyncHttpRespStringHandler.onFailure(statusCode,
						error.getMessage());
			} else {
				LOGGER.warning("Text asynchronous http response string handler is null, not need to throw up");
			}

			// super.onFailure(statusCode, headers, responseBody, error);
		}

	}

}
