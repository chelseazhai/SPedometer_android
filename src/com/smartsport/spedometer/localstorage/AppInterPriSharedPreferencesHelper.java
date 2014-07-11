/**
 * 
 */
package com.smartsport.spedometer.localstorage;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.smartsport.spedometer.SSApplication;
import com.smartsport.spedometer.utils.SSLogger;

/**
 * @name AppInterPriSharedPreferencesHelper
 * @descriptor smartsport application internal private shared preferences helper
 * @author Ares
 * @version 1.0
 */
public class AppInterPriSharedPreferencesHelper {

	// logger
	private static final SSLogger LOGGER = new SSLogger(
			AppInterPriSharedPreferencesHelper.class);

	// shared preferences xml file default name
	private static final String SHARED_PREFERENCES_DEFAULTNAME = "defaultSharedPreferences";

	// shared preferences xml file name suffix
	private static final String SHARED_PREFERENCES_NAMESUFFIX = "_sharedPreferences";

	// singleton instance
	private static volatile AppInterPriSharedPreferencesHelper _sSingletonSharedPreferencesHelper;

	// context
	private Context context;

	/**
	 * @title AppInterPriSharedPreferencesHelper
	 * @descriptor smartsport application internal private shared preferences
	 *             helper private constructor
	 * @author Ares
	 */
	private AppInterPriSharedPreferencesHelper() {
		super();

		// initialize context
		context = SSApplication.getContext();
	}

	/**
	 * @title getInstance
	 * @descriptor get shared preferences helper singleton instance
	 * @return shared preferences helper singleton instance
	 * @author Ares
	 */
	public static AppInterPriSharedPreferencesHelper getInstance() {
		if (null == _sSingletonSharedPreferencesHelper) {
			synchronized (AppInterPriSharedPreferencesHelper.class) {
				if (null == _sSingletonSharedPreferencesHelper) {
					_sSingletonSharedPreferencesHelper = new AppInterPriSharedPreferencesHelper();
				}
			}
		}

		return _sSingletonSharedPreferencesHelper;
	}

	/**
	 * @title putValue
	 * @descriptor put value with key to local storage shared preferences with
	 *             its xml file name
	 * @param key
	 *            : the value key
	 * @param value
	 *            : the value only for String, Integer, Long, Float and Boolean
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @author Ares
	 */
	public void putValue(String key, Object value, String fileName) {
		// put value with key to shared preferences editor
		putValue2SharedPreferences(
				context.getSharedPreferences(
						null == fileName ? SHARED_PREFERENCES_DEFAULTNAME
								: fileName + SHARED_PREFERENCES_NAMESUFFIX,
						Context.MODE_PRIVATE).edit(), key, value).commit();
	}

	/**
	 * @title putValue
	 * @descriptor put value with key to local storage shared preferences
	 * @param key
	 *            : the value key
	 * @param value
	 *            : the value only for String, Integer, Long, Float and Boolean
	 * @author Ares
	 */
	public void putValue(String key, Object value) {
		// put value with key to local storage shared preferences with default
		// xml file name
		putValue(key, value, SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title putMap
	 * @descriptor put values map each element to local storage shared
	 *             preferences with its xml file name
	 * @param valuesMap
	 *            : the values map, each element value only for String, Integer,
	 *            Long, Float and Boolean
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @author Ares
	 */
	public void putMap(Map<String, Object> valuesMap, String fileName) {
		// check values map
		if (null != valuesMap) {
			// get shared preferences editor
			SharedPreferences.Editor _sharedPreferencesEditor = context
					.getSharedPreferences(
							null == fileName ? SHARED_PREFERENCES_DEFAULTNAME
									: fileName + SHARED_PREFERENCES_NAMESUFFIX,
							Context.MODE_PRIVATE).edit();

			// put value with key to shared preferences editor
			for (String _valueKey : valuesMap.keySet()) {
				putValue2SharedPreferences(_sharedPreferencesEditor, _valueKey,
						valuesMap.get(_valueKey));
			}

			// commit to restore in shared preferences storage
			_sharedPreferencesEditor.commit();
		} else {
			LOGGER.error("Put values map = "
					+ valuesMap
					+ " each element to local storage shared preferences, its file name = "
					+ fileName + " error");
		}
	}

	/**
	 * @title putMap
	 * @descriptor put values map each element to local storage shared
	 *             preferences
	 * @param valuesMap
	 *            : the values map, each element value only for String, Integer,
	 *            Long, Float and Boolean
	 * @author Ares
	 */
	public void putMap(Map<String, Object> valuesMap) {
		// put values map each element to local storage shared preferences with
		// default xml file name
		putMap(valuesMap, SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title getString
	 * @descriptor get string value with key from local storage shared
	 *             preferences with its xml file name
	 * @param key
	 *            : the value key
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @return the string value
	 * @author Ares
	 */
	public String getString(String key, String fileName) {
		return (String) getValue(key, String.class, fileName);
	}

	/**
	 * @title getString
	 * @descriptor get string value with key from local storage shared
	 *             preferences
	 * @param key
	 *            : the value key
	 * @return the string value
	 * @author Ares
	 */
	public String getString(String key) {
		return getString(key, SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title getInteger
	 * @descriptor get integer value with key from local storage shared
	 *             preferences with its xml file name
	 * @param key
	 *            : the value key
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @return the integer value
	 * @author Ares
	 */
	public Integer getInteger(String key, String fileName) {
		return (Integer) getValue(key, Integer.class, fileName);
	}

	/**
	 * @title getInteger
	 * @descriptor get integer value with key from local storage shared
	 *             preferences
	 * @param key
	 *            : the value key
	 * @return the integer value
	 * @author Ares
	 */
	public Integer getInteger(String key) {
		return getInteger(key, SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title getLong
	 * @descriptor get long value with key from local storage shared preferences
	 *             with its xml file name
	 * @param key
	 *            : the value key
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @return the long value
	 * @author Ares
	 */
	public Long getLong(String key, String fileName) {
		return (Long) getValue(key, Long.class, fileName);
	}

	/**
	 * @title getLong
	 * @descriptor get long value with key from local storage shared preferences
	 * @param key
	 *            : the value key
	 * @return the long value
	 * @author Ares
	 */
	public Long getLong(String key) {
		return getLong(key, SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title getFloat
	 * @descriptor get float value with key from local storage shared
	 *             preferences with its xml file name
	 * @param key
	 *            : the value key
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @return the float value
	 * @author Ares
	 */
	public Float getFloat(String key, String fileName) {
		return (Float) getValue(key, Float.class, fileName);
	}

	/**
	 * @title getFloat
	 * @descriptor get float value with key from local storage shared
	 *             preferences
	 * @param key
	 *            : the value key
	 * @return the float value
	 * @author Ares
	 */
	public Float getFloat(String key) {
		return getFloat(key, SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title getBoolean
	 * @descriptor get boolean value with key from local storage shared
	 *             preferences with its xml file name
	 * @param key
	 *            : the value key
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @return the boolean value
	 * @author Ares
	 */
	public Boolean getBoolean(String key, String fileName) {
		return (Boolean) getValue(key, Boolean.class, fileName);
	}

	/**
	 * @title getBoolean
	 * @descriptor get boolean value with key from local storage shared
	 *             preferences
	 * @param key
	 *            : the value key
	 * @return the boolean value
	 * @author Ares
	 */
	public Boolean getBoolean(String key) {
		return getBoolean(key, SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title getAll
	 * @descriptor get all values map from local storage shared preferences with
	 *             its xml file name
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @return all values map
	 * @author Ares
	 */
	public Map<String, ?> getAll(String fileName) {
		return context.getSharedPreferences(
				null == fileName ? SHARED_PREFERENCES_DEFAULTNAME : fileName
						+ SHARED_PREFERENCES_NAMESUFFIX, Context.MODE_PRIVATE)
				.getAll();
	}

	/**
	 * @title getAll
	 * @descriptor get all values map from local storage shared preferences
	 * @return all values map
	 * @author Ares
	 */
	public Map<String, ?> getAll() {
		return getAll(SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title removeValue
	 * @descriptor remove value with key from local storage shared preferences
	 *             with its xml file name
	 * @param key
	 *            : the value key
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @author Ares
	 */
	public void removeValue(String key, String fileName) {
		context.getSharedPreferences(
				null == fileName ? SHARED_PREFERENCES_DEFAULTNAME : fileName
						+ SHARED_PREFERENCES_NAMESUFFIX, Context.MODE_PRIVATE)
				.edit().remove(key).commit();
	}

	/**
	 * @title removeValue
	 * @descriptor remove value with key from local storage shared preferences
	 * @param key
	 *            : the value key
	 * @author Ares
	 */
	public void removeValue(String key) {
		removeValue(key, SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title clearAll
	 * @descriptor remove all values from local storage shared preferences with
	 *             its xml file name
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @author Ares
	 */
	public void clearAll(String fileName) {
		context.getSharedPreferences(
				null == fileName ? SHARED_PREFERENCES_DEFAULTNAME : fileName
						+ SHARED_PREFERENCES_NAMESUFFIX, Context.MODE_PRIVATE)
				.edit().clear().commit();
	}

	/**
	 * @title clearAll
	 * @descriptor remove all values from local storage shared preferences
	 * @author Ares
	 */
	public void clearAll() {
		clearAll(SHARED_PREFERENCES_DEFAULTNAME);
	}

	/**
	 * @title putValue2SharedPreferences
	 * @descriptor put value with key to local storage shared preferences with
	 *             its editor
	 * @param sharedPreferencesEditor
	 *            : shared preferences editor
	 * @param key
	 *            : the value key
	 * @param value
	 *            : the value only for String, Integer, Long, Float and Boolean
	 * @author Ares
	 */
	private SharedPreferences.Editor putValue2SharedPreferences(
			SharedPreferences.Editor sharedPreferencesEditor, String key,
			Object value) {
		// check value type
		if (value instanceof String) {
			sharedPreferencesEditor.putString(key, (String) value);
		} else if (value instanceof Integer) {
			sharedPreferencesEditor.putInt(key, (Integer) value);
		} else if (value instanceof Long) {
			sharedPreferencesEditor.putLong(key, (Long) value);
		} else if (value instanceof Float) {
			sharedPreferencesEditor.putFloat(key, (Float) value);
		} else if (value instanceof Boolean) {
			sharedPreferencesEditor.putBoolean(key, (Boolean) value);
		} else {
			LOGGER.warning("Put value = "
					+ value
					+ " with key = "
					+ key
					+ " to shared preferences editor = "
					+ sharedPreferencesEditor
					+ " error, the value type = "
					+ value.getClass()
					+ " not implement for shared preferences local storage, using others local storage instead");
		}

		return sharedPreferencesEditor;
	}

	/**
	 * @title getValue
	 * @descriptor get value with key from local storage shared preferences with
	 *             its xml file name
	 * @param key
	 *            : the value key
	 * @param valueCls
	 *            : the value class only for String, Integer, Long, Float and
	 *            Boolean
	 * @param fileName
	 *            : the shared preferences xml file name
	 * @return value storage in local storage shared preferences
	 * @author Ares
	 */
	private Object getValue(String key, Class<? extends Object> valueCls,
			String fileName) {
		// define return value
		Object _value = null;

		// check value class
		if (valueCls.equals(String.class) || valueCls.equals(Integer.class)
				|| valueCls.equals(Long.class) || valueCls.equals(Float.class)
				|| valueCls.equals(Boolean.class)) {
			// get shared preferences
			SharedPreferences _sharedPreferences = context
					.getSharedPreferences(
							null == fileName ? SHARED_PREFERENCES_DEFAULTNAME
									: fileName + SHARED_PREFERENCES_NAMESUFFIX,
							Context.MODE_PRIVATE);

			// check key
			if (_sharedPreferences.contains(key)) {
				// check value class again
				if (valueCls.equals(String.class)) {
					_value = _sharedPreferences.getString(key, null);
				} else if (valueCls.equals(Integer.class)) {
					_value = _sharedPreferences.getInt(key, 0);
				} else if (valueCls.equals(Long.class)) {
					_value = _sharedPreferences.getLong(key, 0L);
				} else if (valueCls.equals(Float.class)) {
					_value = _sharedPreferences.getFloat(key, 0.0f);
				} else if (valueCls.equals(Boolean.class)) {
					_value = _sharedPreferences.getBoolean(key, false);
				}
			} else {
				LOGGER.warning("Get value with key = "
						+ key
						+ " from local storage shared preferences, its xml file name = "
						+ fileName + " error, not found in local storage");
			}
		} else {
			LOGGER.error("Get value with key = "
					+ key
					+ " from local storage shared preferences, its xml file name = "
					+ fileName + " error, value type = " + valueCls
					+ " not support for shared preferences");
		}

		return _value;
	}

}
