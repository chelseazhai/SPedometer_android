/**
 * 
 */
package com.smartsport.spedometer.localstorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name AppInterSqliteDBHelper
 * @descriptor smartsport application internal sqlite database helper
 * @author Ares
 * @version 1.0
 */
public class AppInterSqliteDBHelper extends SQLiteOpenHelper {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			AppInterSqliteDBHelper.class);

	// application inter local storage sqlite database name and current version
	private static final String LOCALSTORAGE_DBNAME = "spedometer.db3";
	private int sqliteDBVersion;

	// singleton instance
	private static volatile AppInterSqliteDBHelper _sSingletonSqliteDBHelper;

	// context
	private Context context;

	/**
	 * @title AppInterSqliteDBHelper
	 * @descriptor smartsport application internal sqlite database helper
	 *             private constructor with context and sqlite database version
	 * @param context
	 *            : context
	 * @param version
	 *            : sqlite database version
	 * @author Ares
	 */
	private AppInterSqliteDBHelper(Context context, int version) {
		super(context, LOCALSTORAGE_DBNAME, null, version);

		// save context and sqlite database version
		this.context = context;
		sqliteDBVersion = version;
	}

	/**
	 * @title getInstance
	 * @descriptor get sqlite database helper singleton instance with database
	 *             version
	 * @return sqlite database helper singleton instance
	 * @author Ares
	 */
	public static AppInterSqliteDBHelper getInstance(int version) {
		if (null == _sSingletonSqliteDBHelper) {
			synchronized (AppInterSqliteDBHelper.class) {
				if (null == _sSingletonSqliteDBHelper
						|| _sSingletonSqliteDBHelper.sqliteDBVersion != version) {
					_sSingletonSqliteDBHelper = new AppInterSqliteDBHelper(
							SSApplication.getContext(), version);
				}
			}
		}

		return _sSingletonSqliteDBHelper;
	}

	/**
	 * @title getInstance
	 * @descriptor get sqlite database helper singleton instance
	 * @return sqlite database helper singleton instance
	 * @author Ares
	 */
	public static AppInterSqliteDBHelper getInstance() {
		// sqlite database version is 1
		return getInstance(1);
	}

	/**
	 * @title tables
	 * @descriptor get sqlite database helper singleton instance
	 * @return sqlite database helper singleton instance
	 * @author Ares
	 */
	public static AppInterSqliteDBHelper tables() {
		//

		return _sSingletonSqliteDBHelper;
	}

	public int getSqliteDBVersion() {
		return sqliteDBVersion;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LOGGER.info("Local storage sqlite database = " + db + " on create");

		// create smartsport pedometer user personal walk record table
		db.execSQL(getSqlStatementFromAssets(""));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LOGGER.debug("Local storage sqlite database = " + db
				+ " on upgrade from old version = " + oldVersion
				+ " to new version = " + newVersion);

		// TODO Auto-generated method stub

	}

	/**
	 * @title getSqlStatementFromAssets
	 * @descriptor get sql statement from assets
	 * @param sqlFileName
	 *            : sql statement file name
	 * @author Ares
	 */
	private String getSqlStatementFromAssets(String sqlFileName) {
		// define sql statement and file input stream
		String _sqlStatement = "";
		InputStreamReader _isReader;

		try {
			// check sql file name
			if (!sqlFileName.endsWith(".sql")) {
				sqlFileName += ".sql";
			}

			// open assets file
			_isReader = new InputStreamReader(context.getResources()
					.getAssets().open(sqlFileName));

			// read a line
			BufferedReader _bufReader = new BufferedReader(_isReader);
			String _line = "";

			while (null != (_line = _bufReader.readLine())) {
				_sqlStatement += _line;
			}
		} catch (IOException e) {
			LOGGER.error("Get sql statement from assets file error, exception message = "
					+ e.getMessage());

			e.printStackTrace();
		}

		return _sqlStatement;
	}

	// inner class
	/**
	 * @name ISimpleBaseColumns
	 * @descriptor simple sqlite database base columns interface
	 * @author Ares
	 * @version 1.0
	 */
	public interface ISimpleBaseColumns extends BaseColumns {

		// selection "and" string
		public static final String _AND_SELECTION = " and ";

		// data count projection
		public static final String _COUNT_PROJECTION = "count(*) as " + _COUNT;

		// data order ascension and descent
		public static final String _ORDER_ASC = " asc";
		public static final String _ORDER_DESC = " desc";

	}

}
