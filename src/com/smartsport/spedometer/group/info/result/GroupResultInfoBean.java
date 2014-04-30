/**
 * 
 */
package com.smartsport.spedometer.group.info.result;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.Context;

import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.JSONUtils;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name GroupResultInfoBean
 * @descriptor walk or compete group result info bean
 * @author Ares
 * @version 1.0
 */
public class GroupResultInfoBean implements Serializable {

	/**
	 * walk or compete group result info bean serial version UID
	 */
	private static final long serialVersionUID = 7538862158103132527L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			GroupResultInfoBean.class);

	// walk total distance and step
	private int distance;
	private int steps;

	/**
	 * @title GroupResultInfoBean
	 * @descriptor walk or compete group result info bean constructor with json
	 *             object
	 * @param info
	 *            : walk or compete group result info json object
	 * @author Ares
	 */
	public GroupResultInfoBean(JSONObject info) {
		super();

		// get context
		Context _context = SSApplication.getContext();

		// check parsed walk or compete group result info json object
		if (null != info) {
			// set walk or compete group result info attributes
			try {
				// total distance
				distance = Integer
						.parseInt(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.groupWalkResultInfo_totalDistance)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get group result info total distance from json info object = "
						+ info
						+ " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}

			try {
				// total step
				steps = Integer
						.parseInt(JSONUtils.getStringFromJSONObject(
								info,
								_context.getString(R.string.groupWalkResultInfo_totalStep)));
			} catch (NumberFormatException e) {
				LOGGER.error("Get group result info total step from json info object = "
						+ info
						+ " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Constructor walk or compete group result info with json object error, the info is null");
		}
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	@Override
	public String toString() {
		return "GroupResultBean [distance=" + distance + " and steps=" + steps
				+ "]";
	}

}
