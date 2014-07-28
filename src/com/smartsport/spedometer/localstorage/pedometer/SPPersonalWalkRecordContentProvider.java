/**
 * 
 */
package com.smartsport.spedometer.localstorage.pedometer;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.smartsport.spedometer.localstorage.AppInterSqliteDBHelper.ISimpleBaseColumns;
import com.smartsport.spedometer.localstorage.AppInterSqliteDBHelper.SSContentProvider;
import com.smartsport.spedometer.localstorage.pedometer.SPPersonalWalkRecordContentProvider.UserPersonalWalkRecords.UserPersonalWalkRecord;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name SPPersonalWalkRecordContentProvider
 * @descriptor smartsport user personal walk record content provider
 * @author Ares
 * @version 1.0
 */
public class SPPersonalWalkRecordContentProvider extends SSContentProvider {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			SPPersonalWalkRecordContentProvider.class);

	// register custom uri for uri matcher
	static {
		URI_MATCHER.addURI(UserPersonalWalkRecords.AUTHORITY,
				"personalWalkRecords",
				UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORDS);
		URI_MATCHER.addURI(UserPersonalWalkRecords.AUTHORITY,
				"personalWalkRecord",
				UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORD);
		URI_MATCHER.addURI(UserPersonalWalkRecords.AUTHORITY,
				"personalWalkRecord/#",
				UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORD_ID);
	}

	@Override
	protected String[] tables() {
		// smartsport user personal walk records table
		return new String[] { UserPersonalWalkRecords.USERPERSONAL_WALKRECORDS_TABLE };
	}

	@Override
	public String getType(Uri uri) {
		String _contentType = null;

		// check uri
		switch (URI_MATCHER.match(uri)) {
		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORDS:
			_contentType = UserPersonalWalkRecord.PERSONALWALKRECORDS_CONTENT_TYPE;
			break;

		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORD:
		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORD_ID:
			_contentType = UserPersonalWalkRecord.PERSONALWALKRECORD_CONTENT_TYPE;
			break;

		default:
			throw new IllegalArgumentException(
					"Unknown content provider content uri = " + uri);
		}

		return _contentType;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		LOGGER.info("Insert for uri = " + uri + " and new insert values = "
				+ values);

		// define new insert walk record uri
		Uri _newInsertWalkRecordUri = null;

		// get sqlite database writable instance
		SQLiteDatabase _lswDB = sqliteDBHelper.getWritableDatabase();

		// define insert table name
		String _insertTableName = null;

		// check uri
		switch (URI_MATCHER.match(uri)) {
		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORDS:
		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORD_ID:
			// nothing to do
			break;

		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORD:
			// set user personal walk record table as insert table
			_insertTableName = UserPersonalWalkRecords.USERPERSONAL_WALKRECORDS_TABLE;
			break;

		default:
			throw new IllegalArgumentException(
					"Unknown content provider content uri = " + uri);
		}

		LOGGER.debug("Insert user personal walk record with values = " + values);

		// insert user personal walk record into its sqlite database table and
		// return new insert row id
		long _newInsertRowId = _lswDB.insert(_insertTableName,
				ISimpleBaseColumns._ID, values);

		// check the insert process result
		if (0 <= _newInsertRowId) {
			// update new insert walk record uri
			_newInsertWalkRecordUri = ContentUris.withAppendedId(uri,
					_newInsertRowId);

			// notify data has been changed
			getContext().getContentResolver().notifyChange(
					_newInsertWalkRecordUri, null);
		} else {
			LOGGER.debug("Insert user personal walk record to sqlite database table error, values = "
					+ values);
		}

		return _newInsertWalkRecordUri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		LOGGER.info("");

		// define query cursor
		Cursor _queryCursor = null;

		// get sqlite database readable instance
		SQLiteDatabase _lsrDB = sqliteDBHelper.getReadableDatabase();

		// check uri
		switch (URI_MATCHER.match(uri)) {
		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORDS:
		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORD:
			// nothing to do
			break;

		case UserPersonalWalkRecordTableAccessType.PERSONALWALKRECORD_ID:
			// get query user personal walk record id and generate where
			// condition
			String _where = ISimpleBaseColumns._ID + "="
					+ ContentUris.parseId(uri);

			// check and update selection
			if (null != selection && !"".equalsIgnoreCase(selection)) {
				selection += ISimpleBaseColumns._AND_SELECTION + _where;
			} else {
				selection = _where;
			}
			break;

		default:
			throw new IllegalArgumentException(
					"Unknown content provider content uri = " + uri);
		}

		LOGGER.debug("Query user personal walk record with selection = "
				+ selection);

		// query user personal walk records from user personal walk record table
		// with projection, selection and order
		_queryCursor = _lsrDB.query(
				UserPersonalWalkRecords.USERPERSONAL_WALKRECORDS_TABLE,
				projection, selection, selectionArgs, null, null, sortOrder);

		// set notification with uri
		_queryCursor
				.setNotificationUri(
						getContext().getContentResolver(),
						UserPersonalWalkRecord.PERSONALWALKRECORDS_NOTIFICATION_CONTENT_URI);

		return _queryCursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		LOGGER.debug("Update for uri = " + uri + ", update values = " + values
				+ ", selection = " + selection + " and selection args = "
				+ selectionArgs + " not implement");

		// TODO Auto-generated method stub

		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		LOGGER.debug("Delete for uri = " + uri + ", selection = " + selection
				+ " and selection args = " + selectionArgs + " not implement");

		// TODO Auto-generated method stub

		return 0;
	}

	// inner class
	/**
	 * @name UserPersonalWalkRecordTableAccessType
	 * @descriptor user personal walk record table access type
	 * @author Ares
	 * @version 1.0
	 */
	static class UserPersonalWalkRecordTableAccessType {

		// user personal walk record table access type
		private static final int PERSONALWALKRECORDS = 10;
		private static final int PERSONALWALKRECORD = 11;
		private static final int PERSONALWALKRECORD_ID = 12;

	}

	/**
	 * @name UserPersonalWalkRecords
	 * @descriptor user personal walk records
	 * @author Ares
	 * @version 1.0
	 */
	public static final class UserPersonalWalkRecords {

		// user personal walk record content provider authority
		private static final String AUTHORITY = SPPersonalWalkRecordContentProvider.class
				.getCanonicalName();

		// user personal walk record table name
		public static final String USERPERSONAL_WALKRECORDS_TABLE = "sp_personal_walkrecord";

		// inner class
		/**
		 * @name UserPersonalWalkRecord
		 * @descriptor user personal walk record table constant
		 * @author Ares
		 * @version 1.0
		 */
		public static final class UserPersonalWalkRecord implements
				ISimpleBaseColumns {

			// user personal walk record content provider process data columns
			public static final String USER_ID = "userId";
			public static final String START_TIME = "startTimestamp";
			public static final String DURATION_TIME = "durationTime";
			public static final String TOTALSTEP = "totalStep";
			public static final String TOTALDISTANCE = "totalDistance";
			public static final String ENERGY = "energy";

			// content uri
			private static final Uri PERSONALWALKRECORDS_NOTIFICATION_CONTENT_URI = Uri
					.parse("content://" + AUTHORITY);
			public static final Uri PERSONALWALKRECORDS_CONTENT_URI = Uri
					.parse("content://" + AUTHORITY + "/personalWalkRecords");
			public static final Uri PERSONALWALKRECORD_CONTENT_URI = Uri
					.parse("content://" + AUTHORITY + "/personalWalkRecord");

			// content type
			private static final String PERSONALWALKRECORDS_CONTENT_TYPE = "vnd.android.cursor.dir/"
					+ UserPersonalWalkRecords.class.getCanonicalName();
			private static final String PERSONALWALKRECORD_CONTENT_TYPE = "vnd.android.cursor.item/"
					+ UserPersonalWalkRecords.class.getCanonicalName();

			// user personal walk records condition
			public static final String USERPERSONAL_WALKRECORDS_WITHLOGINNAME_CONDITION = USER_ID
					+ "=?";

		}

	}

}
