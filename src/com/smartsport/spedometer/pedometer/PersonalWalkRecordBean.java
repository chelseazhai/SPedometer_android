/**
 * 
 */
package com.smartsport.spedometer.pedometer;

import java.io.Serializable;

import android.content.Context;
import android.database.Cursor;

import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.localstorage.pedometer.SPPersonalWalkRecordContentProvider.UserPersonalWalkRecords.UserPersonalWalkRecord;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name PersonalWalkRecordBean
 * @descriptor personal walk record bean
 * @author Ares
 * @version 1.0
 */
public class PersonalWalkRecordBean implements Serializable {

	/**
	 * personal walk record bean serial version UID
	 */
	private static final long serialVersionUID = -3704854367115754830L;

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			PersonalWalkRecordBean.class);

	// context
	protected transient Context context;

	// row id, user id, start time, duration time, total step, total distance
	// and energy
	private Long rowId;
	private Long userId;
	private Long startTime;
	private Integer durationTime;
	private Integer totalStep;
	private Double totalDistance;
	private Float energy;

	/**
	 * @title PersonalWalkRecordBean
	 * @descriptor personal walk record bean constructor
	 * @author Ares
	 */
	public PersonalWalkRecordBean() {
		super();

		// initialize context
		context = SSApplication.getContext();
	}

	/**
	 * @title PersonalWalkRecordBean
	 * @descriptor personal walk record bean constructor with cursor
	 * @author Ares
	 */
	public PersonalWalkRecordBean(Cursor cursor) {
		this();

		// check the cursor
		if (null != cursor) {
			// set personal walk record attributes
			// row id
			rowId = cursor.getLong(cursor
					.getColumnIndex(UserPersonalWalkRecord._ID));

			// user id
			userId = cursor.getLong(cursor
					.getColumnIndex(UserPersonalWalkRecord.USER_ID));

			// start time
			startTime = cursor.getLong(cursor
					.getColumnIndex(UserPersonalWalkRecord.START_TIME));

			// duration time
			durationTime = cursor.getInt(cursor
					.getColumnIndex(UserPersonalWalkRecord.DURATION_TIME));

			// total step
			totalStep = cursor.getInt(cursor
					.getColumnIndex(UserPersonalWalkRecord.TOTALSTEP));

			// total distance
			totalDistance = cursor.getDouble(cursor
					.getColumnIndex(UserPersonalWalkRecord.TOTALDISTANCE));

			// energy
			energy = cursor.getFloat(cursor
					.getColumnIndex(UserPersonalWalkRecord.ENERGY));
		} else {
			LOGGER.error("New personal walk record with cursor error, cursor = "
					+ cursor);
		}
	}

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Integer getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(Integer durationTime) {
		this.durationTime = durationTime;
	}

	public Integer getTotalStep() {
		return totalStep;
	}

	public void setTotalStep(Integer totalStep) {
		this.totalStep = totalStep;
	}

	public Double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}

	public Float getEnergy() {
		return energy;
	}

	public void setEnergy(Float energy) {
		this.energy = energy;
	}

	@Override
	public String toString() {
		return "PersonalWalkRecordBean [rowId=" + rowId + ", userId=" + userId
				+ ", startTime=" + startTime + ", durationTime=" + durationTime
				+ ", totalStep=" + totalStep + ", totalDistance="
				+ totalDistance + " and energy=" + energy + "]";
	}

}
