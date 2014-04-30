/**
 * 
 */
package com.smartsport.spedometer.group.info;

import org.json.JSONArray;
import org.json.JSONObject;

import com.smartsport.spedometer.group.GroupBean;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name HistoryGroupInfoBean
 * @descriptor history walk or compete group info with group result info bean
 * @author Ares
 * @version 1.0
 */
public class HistoryGroupInfoBean extends GroupInfoBean {

	/**
	 * history walk or compete group info with group result info bean serial
	 * version UID
	 */
	private static final long serialVersionUID = -6915296742292958798L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			HistoryGroupInfoBean.class);

	// history walk or compete group start time, duration, stop time and result
	// info
	//

	public HistoryGroupInfoBean(GroupBean group) {
		super(group);
	}

	@Override
	protected GroupInfoBean parseGroupInfo(JSONObject info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	protected GroupInfoBean parseGroupInfo(JSONArray info) {
		// nothing to do
		return null;
	}

}
