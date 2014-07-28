/**
 * 
 */
package com.smartsport.spedometer.localstorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.ContentProvider;
import android.content.Context;
import android.content.UriMatcher;
import android.database.SQLException;
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

	// sqlite database table name list
	private static Set<String> _sDBTableNames = new HashSet<String>();

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
		if (null == _sSingletonSqliteDBHelper
				|| _sSingletonSqliteDBHelper.sqliteDBVersion != version) {
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
	 * @descriptor set sqlite database names and return sqlite database helper
	 *             singleton instance
	 * @param tableNames
	 *            : the sqlite database names
	 * @return sqlite database helper singleton instance
	 * @author Ares
	 */
	public static AppInterSqliteDBHelper tables(String... tableNames) {
		// check table name array and then save the sqlite database names
		if (null != tableNames) {
			for (String _tableName : tableNames) {
				// check each table name
				if (null != _tableName && !"".equalsIgnoreCase(_tableName)) {

					_sDBTableNames.add(_tableName);
				} else {
					LOGGER.error("Ignore table name = " + _tableName
							+ " can't add to list");
				}
			}

			// check singleton instance and initialize it
			if (null == _sSingletonSqliteDBHelper) {
				getInstance();
			} else {
				getInstance(Integer.MAX_VALUE);
			}
		} else {
			LOGGER.warning("The sqlite database table name = " + tableNames
					+ " is null, so the singleton instance is equivocal");
		}

		return _sSingletonSqliteDBHelper;
	}

	public int getSqliteDBVersion() {
		return sqliteDBVersion;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LOGGER.info("Local storage sqlite database = " + db + " on create");

		// create smartsport pedometer tables if needed
		for (Iterator<String> _tableNameIterator = _sDBTableNames.iterator(); _tableNameIterator
				.hasNext();) {
			// get sql table name
			String _sqltableName = (String) _tableNameIterator.next();

			// get table create sql statement and execute the statement
			String _tableCreateSqlStatement = getSqlStatementFromAssets(_sqltableName);
			try {
				db.execSQL(_tableCreateSqlStatement);
			} catch (SQLException e) {
				LOGGER.error("Create sql table, its name = " + _sqltableName
						+ " error, the table create sql statement is = "
						+ _tableCreateSqlStatement);

				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LOGGER.debug("Local storage sqlite database = " + db
				+ " on upgrade from old version = " + oldVersion
				+ " to new version = " + newVersion);

		// check new version
		if (Integer.MAX_VALUE == newVersion) {
			// just for new add tables
			// create smartsport pedometer tables if needed
			for (Iterator<String> _tableNameIterator = _sDBTableNames
					.iterator(); _tableNameIterator.hasNext();) {
				// get sql table name
				String _sqltableName = (String) _tableNameIterator.next();

				// get table create sql statement and execute the statement
				String _tableCreateSqlStatement = getSqlStatementFromAssets(_sqltableName);
				try {
					db.execSQL(_tableCreateSqlStatement);
				} catch (SQLException e) {
					LOGGER.error("Create sql table for new added, its name = "
							+ _sqltableName
							+ " error, the table create sql statement is = "
							+ _tableCreateSqlStatement);

					e.printStackTrace();
				}
			}

			// recover the sqlite database version
			sqliteDBVersion = oldVersion;
		} else {
			// for sqlite database tables upgrade
			// TODO Auto-generated method stub

		}
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

			// get sql statement
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
	 * @name SSContentProvider
	 * @descriptor smartsport content provider
	 * @author Ares
	 * @version 1.0
	 */
	public static abstract class SSContentProvider extends ContentProvider {

		// uri matcher
		protected static final UriMatcher URI_MATCHER = new UriMatcher(
				UriMatcher.NO_MATCH);

		// application inter sqlite database helper
		protected AppInterSqliteDBHelper sqliteDBHelper;

		@SuppressWarnings("static-access")
		@Override
		public boolean onCreate() {
			// get application inter sqlite database helper
			sqliteDBHelper = AppInterSqliteDBHelper.tables(tables())
					.getInstance();

			return true;
		}

		/**
		 * @title tables
		 * @descriptor set the smartsport content provider needs table names
		 * @return the smartsport content provider needs table names
		 * @author Ares
		 */
		protected abstract String[] tables();

	}

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
