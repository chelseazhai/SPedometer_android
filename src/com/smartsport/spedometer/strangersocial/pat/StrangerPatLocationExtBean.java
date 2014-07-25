/**
 * 
 */
package com.smartsport.spedometer.strangersocial.pat;

import org.json.JSONObject;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.strangersocial.LocationBean;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name StrangerPatLocationExtBean
 * @descriptor location with stranger be patted time extension
 * @author Ares
 * @version 1.0
 */
public class StrangerPatLocationExtBean extends LocationBean {

	/**
	 * location with stranger be patted time extension bean serial version UID
	 */
	private static final long serialVersionUID = 1207902354254841110L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			StrangerPatLocationExtBean.class);

	// pat time
	private long patTime;

	/**
	 * @title StrangerPatLocationExtBean
	 * @descriptor stranger pat location bean constructor with json object
	 * @param info
	 *            : user pat stranger be patted location json object
	 * @author Ares
	 */
	public StrangerPatLocationExtBean(JSONObject info) {
		super(info);

		// parse user pat stranger be patted location info
		// check parsed user pat stranger be patted location json object
		if (null != info) {
			// get user pat stranger be patted timestamp value
			try {
				long _strangerBePattedTime = Long
						.parseLong(JSONUtils
								.getStringFromJSONObject(
										info,
										SSApplication
												.getContext()
												.getString(
														R.string.getStrangerPatLocationReqResp_patTime)));

				LOGGER.debug("Parse user pat stranger be patted location json object successful, user pat time = "
						+ _strangerBePattedTime);

				// save pat location info:pat time
				patTime = _strangerBePattedTime;
			} catch (NumberFormatException e) {
				LOGGER.error("Parse user pat stranger be patted location json object error, exception message = "
						+ e.getMessage());

				e.printStackTrace();

				// clear pat time, using invalid value
				patTime = Long.MIN_VALUE;
			}
		} else {
			LOGGER.error("Parse user pat stranger be patted location json object error, the info with pat location info is null");
		}
	}

	public long getPatTime() {
		return patTime;
	}

	public void setPatTime(long patTime) {
		this.patTime = patTime;
	}

	@Override
	public String toString() {
		return "StrangerPatLocationExtBean [location=" + super.toString()
				+ " and patTime=" + patTime + "]";
	}

}
