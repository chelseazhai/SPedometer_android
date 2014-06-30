/**
 * 
 */
package com.smartsport.spedometer.group.info.member;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name MemberWalkVelocityBean
 * @descriptor member walk velocity bean(including occur timestamp and velocity)
 * @author Ares
 * @version 1.0
 */
public class MemberWalkVelocityBean implements Serializable {

	/**
	 * member walk velocity bean serial version UID
	 */
	private static final long serialVersionUID = 5957592860979780567L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			MemberWalkVelocityBean.class);

	// occur timestamp and velocity
	private long timestamp;
	private double velocity;

	/**
	 * @title MemberWalkVelocityBean
	 * @descriptor member walk velocity bean constructor with json object
	 * @param info
	 *            : member walk velocity json object
	 * @author Ares
	 */
	public MemberWalkVelocityBean(JSONObject info) {
		super();

		// parse member walk velocity info
		// check parsed member walk velocity json object
		if (null != info) {
			// get context
			Context _context = SSApplication.getContext();

			// get member walk velocity occur timestamp and velocity
			try {
				long _walkVelocityOccurTimestamp = Long
						.parseLong(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.getWithinGroupCompeteWalkInfoReqResp_velocityTimestamp)));
				double _walkVelocity = Double
						.parseDouble(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.getWithinGroupCompeteWalkInfoReqResp_velocity)));

				LOGGER.debug("Parse member walk velocity json object successful, member walk velocity occur timestamp = "
						+ _walkVelocityOccurTimestamp
						+ " and velocity = "
						+ _walkVelocity);

				// save member walk velocity occur timestamp and velocity
				timestamp = _walkVelocityOccurTimestamp;
				velocity = _walkVelocity;
			} catch (NumberFormatException e) {
				LOGGER.error("Parse memeber walk velocity json object error, exception message = "
						+ e.getMessage());

				e.printStackTrace();

				// clear member walk velocity, using invalid value
				timestamp = Long.MIN_VALUE;
				velocity = Double.MIN_VALUE;
			}
		} else {
			LOGGER.error("Parse member walk velocity json object error, the info with occur timestamp and velocity is null");
		}
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

}
