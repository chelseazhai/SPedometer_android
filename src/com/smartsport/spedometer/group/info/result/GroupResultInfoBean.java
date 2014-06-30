/**
 * 
 */
package com.smartsport.spedometer.group.info.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.smartsport.spedometer.R;
import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.group.info.member.MemberWalkVelocityBean;
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

	// walk path locations
	private List<LatLonPoint> walkPathLocationList;

	// walk velocities
	private List<MemberWalkVelocityBean> walkVelocityList;

	/**
	 * @title GroupResultInfoBean
	 * @descriptor walk or compete group result info bean constructor
	 * @author Ares
	 */
	public GroupResultInfoBean() {
		super();

		// initialize walk path locations and walk velocities
		walkPathLocationList = new ArrayList<LatLonPoint>();
		walkVelocityList = new ArrayList<MemberWalkVelocityBean>();
	}

	/**
	 * @title GroupResultInfoBean
	 * @descriptor walk or compete group result info bean constructor with json
	 *             object
	 * @param info
	 *            : walk or compete group result info json object
	 * @author Ares
	 */
	public GroupResultInfoBean(JSONObject info) {
		this();

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

	public List<LatLonPoint> getWalkPathLocationList() {
		return walkPathLocationList;
	}

	public void addWalkPathLocation(LatLonPoint walkPathLocation) {
		this.walkPathLocationList.add(walkPathLocation);
	}

	public void clearWalkPathLocationList() {
		this.walkPathLocationList.clear();
	}

	public List<MemberWalkVelocityBean> getWalkVelocityList() {
		return walkVelocityList;
	}

	public void addWalkVelocity(MemberWalkVelocityBean walkVelocity) {
		this.walkVelocityList.add(walkVelocity);
	}

	public void clearWalkVelocityList() {
		this.walkVelocityList.clear();
	}

	@Override
	public String toString() {
		return "GroupResultInfoBean [distance=" + distance + ", steps=" + steps
				+ ", walkPathLocationList=" + walkPathLocationList
				+ " and walkVelocityList=" + walkVelocityList + "]";
	}

}
