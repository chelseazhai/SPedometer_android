/**
 * 
 */
package com.smartsport.spedometer.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.network.handler.AsyncHttpRespFileHandler;
import com.smartsport.spedometer.network.handler.AsyncHttpRespStringHandler;
import com.smartsport.spedometer.network.handler.IAsyncHttpRespHandler;
import com.smartsport.spedometer.network.handler.IAsyncHttpRespHandler.NetworkPedometerReqRespStatusConstant;
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
	 * @descriptor send asynchronous get http request with module name, api
	 *             relative url and parameter, then handle with response json
	 *             handler
	 * @param module
	 *            : module name
	 * @param api
	 *            : api relative url
	 * @param parameter
	 *            : asynchronous get http request parameter
	 * @param asyncHttpRespStringHandler
	 *            : asynchronous http response string handler
	 * @author Ares
	 */
	public void getWithAPI(String module, String api,
			Map<String, String> parameter,
			AsyncHttpRespStringHandler asyncHttpRespStringHandler) {
		LOGGER.debug("Send asynchronous get http request, module name = "
				+ module + " , api relative url = " + api + ", parameter = "
				+ parameter + " and response string handler = "
				+ asyncHttpRespStringHandler);

		// get asynchronous http request
		asyncHttpClient.get(genHttpRequestUrl(module, api), new RequestParams(
				parameter), new TextAsyncHttpRespStringHandler(
				asyncHttpRespStringHandler));
	}

	/**
	 * @title postWithAPI
	 * @descriptor send asynchronous post http request with module name, api
	 *             relative url and parameter, then handle with response json
	 *             handler
	 * @param module
	 *            : module name
	 * @param api
	 *            : api relative url
	 * @param parameter
	 *            : asynchronous post http request parameter
	 * @param asyncHttpRespStringHandler
	 *            : asynchronous http response string handler
	 * @author Ares
	 */
	public void postWithAPI(String module, String api,
			Map<String, ?> parameter,
			AsyncHttpRespStringHandler asyncHttpRespStringHandler) {
		LOGGER.debug("Send asynchronous post http request, module name = "
				+ module + " , api relative url = " + api + ", parameter = "
				+ parameter + " and response string handler = "
				+ asyncHttpRespStringHandler);

		// get, check json string parameter and then post asynchronous http
		// request
		StringEntity _jsonStringEntity = new HopeRunHttpReqEntity(parameter)
				.genReqStringEntity();
		try {
			LOGGER.info("Http request string entity = "
					+ EntityUtils.toString(_jsonStringEntity));
		} catch (ParseException e) {
			LOGGER.error("Print hopeRun http request json string entity parse error, exception message = "
					+ e.getMessage());

			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error("Print hopeRun http request json string entity error, exception message = "
					+ e.getMessage());

			e.printStackTrace();
		}
		if (null != _jsonStringEntity) {
			asyncHttpClient.post(context, genHttpRequestUrl(module, api),
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
	 * @descriptor send asynchronous pedometer post http request with api
	 *             relative url and parameter, then handle with response json
	 *             handler
	 * @param api
	 *            : api relative url
	 * @param parameter
	 *            : asynchronous post http request parameter
	 * @param asyncHttpRespStringHandler
	 *            : asynchronous http response string handler
	 * @author Ares
	 */
	public void postWithAPI(String api, Map<String, ?> parameter,
			AsyncHttpRespStringHandler asyncHttpRespStringHandler) {
		postWithAPI(context.getString(R.string.pedometerModule), api,
				parameter, asyncHttpRespStringHandler);
	}

	/**
	 * @title postWithAPI
	 * @descriptor send asynchronous post http request with module name, api
	 *             relative url, parameter and upload file, then handle with
	 *             response json handler
	 * @param module
	 *            : module name
	 * @param api
	 *            : api relative url
	 * @param parameter
	 *            : asynchronous post http request parameter
	 * @param file
	 *            : upload file
	 * @param asyncHttpRespFileHandler
	 *            : asynchronous http response file handler
	 * @author Ares
	 */
	public void postWithAPI(String module, String api,
			Map<String, String> parameter, File file,
			AsyncHttpRespFileHandler asyncHttpRespFileHandler) {
		// generate request parameters with parameter
		RequestParams _reqParams = new RequestParams(parameter);

		try {
			// put file to request parameters
			// test by ares
			_reqParams.put("image", file,
					RequestParams.APPLICATION_OCTET_STREAM);

			LOGGER.info("File upload asynchronous post http request parameters = "
					+ _reqParams);

			// post file upload asynchronous http request
			asyncHttpClient
					.post(genHttpRequestUrl(module, api), _reqParams,
							new TextAsyncHttpRespStringHandler(
									asyncHttpRespFileHandler));
		} catch (FileNotFoundException e) {
			LOGGER.error("The need to upload file not found");

			e.printStackTrace();
		}
	}

	/**
	 * @title postWithAPI
	 * @descriptor send asynchronous post http request with api relative url,
	 *             parameter and upload file, then handle with response json
	 *             handler
	 * @param api
	 *            : api relative url
	 * @param parameter
	 *            : asynchronous post http request parameter
	 * @param file
	 *            : upload file
	 * @param asyncHttpRespFileHandler
	 *            : asynchronous http response file handler
	 * @author Ares
	 */
	public void postWithAPI(String api, Map<String, String> parameter,
			File file, AsyncHttpRespFileHandler asyncHttpRespFileHandler) {
		postWithAPI(context.getString(R.string.commonModule), api, parameter,
				file, asyncHttpRespFileHandler);
	}

	/**
	 * @title genHttpRequestUrl
	 * @descriptor generate http request url with module name and api relative
	 *             url
	 * @param module
	 *            : module name
	 * @param api
	 *            : api relative url
	 * @return http request url
	 * @author Ares
	 */
	private String genHttpRequestUrl(String module, String api) {
		// define http request url string builder
		StringBuilder _httpRequestUrl = new StringBuilder(
				context.getString(R.string.server_rootUrl));

		// check http request root url
		if (!_httpRequestUrl.toString().startsWith(HTTPREQ_URLPREFIX)
				&& !_httpRequestUrl.toString().startsWith(HTTPSREQ_URLPREFIX)) {
			// add http request url prefix at first
			_httpRequestUrl.insert(0, HTTPREQ_URLPREFIX);
		}

		// check module and api
		if (null != module) {
			_httpRequestUrl.append(module);
		}
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

		// asynchronous http response handler
		private IAsyncHttpRespHandler asyncHttpRespHandler;

		/**
		 * @title TextAsyncHttpRespStringHandler
		 * @descriptor text asynchronous http response string handler
		 *             constructor
		 * @param asyncHttpRespHandler
		 *            : asynchronous http response handler
		 */
		private TextAsyncHttpRespStringHandler(
				IAsyncHttpRespHandler asyncHttpRespHandler) {
			super("UTF-8");

			// save asynchronous http response handler
			this.asyncHttpRespHandler = asyncHttpRespHandler;
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				String responseBody) {
			LOGGER.debug("Text asynchronous http response string handler handle successful, status code = "
					+ statusCode
					+ ", headers = "
					+ headers
					+ " and response body = " + responseBody);

			// check asynchronous http response handler
			if (null != asyncHttpRespHandler) {
				try {
					// get hope run http request response entity
					HopeRunHttpReqRespEntity _hopeRunHttpReqRespEntity = new HopeRunHttpReqRespEntity(
							statusCode, new JSONObject(responseBody));

					// get and check status code
					Integer _statusCode = _hopeRunHttpReqRespEntity
							.getStatusCode();
					if (statusCode == _statusCode) {
						// asynchronous http response handle successful
						asyncHttpRespHandler.onSuccess(statusCode,
								_hopeRunHttpReqRespEntity.getBody().getBytes());
					} else {
						// asynchronous http response handle failed
						asyncHttpRespHandler.onFailure(_statusCode,
								_hopeRunHttpReqRespEntity.getStatusMsg());
					}
				} catch (JSONException e) {
					LOGGER.error("Get hope run http request response entity json object error, exception message = "
							+ e.getMessage());

					e.printStackTrace();

					// response body unrecognized(not json object)
					asyncHttpRespHandler.onFailure(
							HttpStatus.SC_INTERNAL_SERVER_ERROR, responseBody);
				}
			} else {
				LOGGER.warning("Text asynchronous http response handler is null, not need to throw up");
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
					// reset timeout status code
					statusCode = NetworkPedometerReqRespStatusConstant.REQUEST_TIMEOUT;

					// show request timeout toast
					Toast.makeText(context,
							R.string.nwErrorMsg_request_timeout,
							Toast.LENGTH_LONG).show();
				} else if (error instanceof UnknownHostException
						|| error instanceof HttpHostConnectException) {
					// host exception
					// reset host exception status code
					statusCode = NetworkPedometerReqRespStatusConstant.REQUEST_HOSTEXCEPTION;

					// show host exception toast
					Toast.makeText(context,
							R.string.nwErrorMsg_remoteServer_unavailable,
							Toast.LENGTH_LONG).show();
				}
			} else {
				// check http request response status code
				switch (statusCode / 100) {
				case 4:
				case 5:
					// remote background server http request error
					LOGGER.error("Http request error, response status code = "
							+ statusCode);

					// show request error toast
					Toast.makeText(context,
							R.string.nwErrorMsg_remoteServer_exception,
							Toast.LENGTH_LONG).show();
					break;
				}
			}

			// check asynchronous http response handler
			if (null != asyncHttpRespHandler) {
				// asynchronous http response handle failed
				asyncHttpRespHandler.onFailure(statusCode, error.getMessage());
			} else {
				LOGGER.warning("Text asynchronous http response handler is null, not need to throw up");
			}

			// super.onFailure(statusCode, headers, responseBody, error);
		}

	}

}
