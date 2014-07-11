/**
 * 
 */
package com.smartsport.spedometer.localstorage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;

import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name AppInterPriFileHelper
 * @descriptor smartsport application internal private file helper
 * @author Ares
 * @version 1.0
 */
public class AppInterPriFileHelper {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			AppInterPriFileHelper.class);

	// file default dictionary
	private static final String FILE_DEFAULTDIR = "sPedometerApp";

	// singleton instance
	private static volatile AppInterPriFileHelper _sSingletonFileHelper;

	// context
	private Context context;

	/**
	 * @title AppInterPriFileHelper
	 * @descriptor smartsport application internal private file helper private
	 *             constructor
	 * @author Ares
	 */
	private AppInterPriFileHelper() {
		super();

		// initialize context
		context = SSApplication.getContext();
	}

	/**
	 * @title getInstance
	 * @descriptor get file helper singleton instance
	 * @return file helper singleton instance
	 * @author Ares
	 */
	public static AppInterPriFileHelper getInstance() {
		if (null == _sSingletonFileHelper) {
			synchronized (AppInterPriFileHelper.class) {
				if (null == _sSingletonFileHelper) {
					_sSingletonFileHelper = new AppInterPriFileHelper();
				}
			}
		}

		return _sSingletonFileHelper;
	}

	/**
	 * @title write2File
	 * @descriptor write data to local storage file with its storage dictionary
	 *             and file name
	 * @param data
	 *            : the data
	 * @param fileDir
	 *            : the file storage dictionary
	 * @param fileName
	 *            : the storage file name
	 * @return the storage file
	 * @author Ares
	 */
	public File write2File(byte[] data, String fileDir, String fileName) {
		// define local storage file
		File _localStorageFile = null;

		// check the data
		if (null != data) {
			// initialize local storage file, if local storage dictionary is
			// null using the default, and if the file name is null or "" using
			// system current timestamp as file name
			_localStorageFile = new File(context.getDir(
					null == fileDir ? FILE_DEFAULTDIR : fileDir,
					Context.MODE_PRIVATE), null == fileName
					|| "".equalsIgnoreCase(fileName) ? String.valueOf(System
					.currentTimeMillis()) : fileName);

			// delete the local storage file if exists
			if (_localStorageFile.exists()) {
				_localStorageFile.delete();
			}

			// define the local storage file buffered output stream
			BufferedOutputStream _lsFileBufferedOutputStream = null;

			try {
				// open the local storage file buffered output stream
				_lsFileBufferedOutputStream = new BufferedOutputStream(
						new FileOutputStream(_localStorageFile));

				// white data to local storage file and flush the buffer
				_lsFileBufferedOutputStream.write(data);
				_lsFileBufferedOutputStream.flush();
			} catch (FileNotFoundException e) {
				LOGGER.error("Open the local storage file error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			} catch (IOException e) {
				LOGGER.error("Write data to local storage file error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			} finally {
				// close the opened local storage file buffered output stream
				try {
					_lsFileBufferedOutputStream.close();
				} catch (IOException e) {
					LOGGER.error("Close the opened local storage file buffered output stream error, exception message = "
							+ e.getMessage());

					e.printStackTrace();
				}
			}
		} else {
			LOGGER.error("Write data = " + data
					+ " to local storage file error, the data is null");
		}

		return _localStorageFile;
	}

	/**
	 * @title write2File
	 * @descriptor write data to local storage file with its name
	 * @param data
	 *            : the data
	 * @param fileName
	 *            : the storage file name
	 * @return the storage file
	 * @author Ares
	 */
	public File write2File(byte[] data, String fileName) {
		// write data to local storage file with default storage dictionary and
		// its name
		return write2File(data, FILE_DEFAULTDIR, fileName);
	}

	/**
	 * @title write2File
	 * @descriptor write bitmap to local storage file with its storage
	 *             dictionary and file name
	 * @param data
	 *            : the bitmap
	 * @param fileDir
	 *            : the file storage dictionary
	 * @param fileName
	 *            : the storage file name
	 * @return the storage file
	 * @author Ares
	 */
	public File write2File(Bitmap data, String fileDir, String fileName) {
		// define local storage image file
		File _localStorageImgFile = null;

		// check the bitmap data
		if (null != data) {
			// initialize local storage image file, if local storage dictionary
			// is null using the default, and if the file name is null or ""
			// using system current timestamp as file name
			_localStorageImgFile = new File(context.getDir(
					null == fileDir ? FILE_DEFAULTDIR : fileDir,
					Context.MODE_PRIVATE), null == fileName
					|| "".equalsIgnoreCase(fileName) ? String.valueOf(System
					.currentTimeMillis()) : fileName);

			// delete the local storage image file if exists
			if (_localStorageImgFile.exists()) {
				_localStorageImgFile.delete();
			}

			// define the local storage image file output stream
			FileOutputStream _lsImgFileOutputStream = null;

			try {
				// open the local storage image file output stream
				_lsImgFileOutputStream = new FileOutputStream(
						_localStorageImgFile);

				// compress the bitmap data to the local storage image file
				data.compress(Bitmap.CompressFormat.PNG, 100,
						_lsImgFileOutputStream);
			} catch (FileNotFoundException e) {
				LOGGER.error("Open the local storage image file error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			} finally {
				// close the opened local storage image file output stream
				try {
					_lsImgFileOutputStream.close();
				} catch (IOException e) {
					LOGGER.error("Close the opened local storage image file output stream error, exception message = "
							+ e.getMessage());

					e.printStackTrace();
				}
			}
		} else {
			LOGGER.error("Write image bitmap = " + data
					+ " to local storage file error, the image bitmap is null");
		}

		return _localStorageImgFile;
	}

	/**
	 * @title write2File
	 * @descriptor write bitmap to local storage file with its name
	 * @param data
	 *            : the bitmap
	 * @param fileName
	 *            : the storage file name
	 * @return the storage file
	 * @author Ares
	 */
	public File write2File(Bitmap data, String fileName) {
		// write bitmap to local storage file with default storage dictionary
		// and its name
		return write2File(data, FILE_DEFAULTDIR, fileName);
	}

}
