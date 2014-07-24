/**
 * 
 */
package com.smartsport.spedometer.localstorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.smartsport.spedometer.utils.SSLogger;

/**
 * @author Ares
 * 
 */
public class AppInterSqliteDBHelper extends SQLiteOpenHelper {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			AppInterSqliteDBHelper.class);

	// context
	private Context context;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
	public AppInterSqliteDBHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public AppInterSqliteDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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

}
