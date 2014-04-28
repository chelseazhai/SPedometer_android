/**
 * 
 */
package com.smartsport.spedometer.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @name JSONUtils
 * @descriptor json utils
 * @author Ares
 * @version 1.0
 */
public class JSONUtils {

	// logger
	private static final SSLogger LOGGER = new SSLogger(JSONUtils.class);

	/**
	 * @title jsonObjectKeys
	 * @descriptor get json object all keys set
	 * @param object
	 *            : json object
	 * @return json object all keys set
	 * @author Ares
	 */
	public static Set<String> jsonObjectKeys(JSONObject object) {
		// define json object all keys set
		Set<String> _keys = new HashSet<String>();

		// check json object
		if (null != object) {
			for (Iterator<?> iterator = object.keys(); iterator.hasNext();) {
				// add json object key in set
				_keys.add((String) iterator.next());
			}
		} else {
			LOGGER.error("Json object is null, can't get all keys set");
		}

		return _keys;
	}

	/**
	 * @title getFromJSONObject
	 * @descriptor get json object value from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @param valueCls
	 *            : json object value class
	 * @return json object value
	 * @author Ares
	 */
	private static Object getFromJSONObject(JSONObject object, String key,
			Class<? extends Object> valueCls) {
		// define json object value object
		Object _value = null;

		// check json object
		if (null != object) {
			try {
				// check value class and get object for key
				if (Integer.class == valueCls) {
					_value = object.getInt(key);
				} else if (Long.class == valueCls) {
					_value = object.getLong(key);
				} else if (Double.class == valueCls) {
					_value = object.getDouble(key);
				} else if (Boolean.class == valueCls) {
					_value = object.getBoolean(key);
				} else if (String.class == valueCls) {
					_value = object.getString(key);
				} else if (JSONObject.class == valueCls) {
					_value = object.getJSONObject(key);
				} else if (JSONArray.class == valueCls) {
					_value = object.getJSONArray(key);
				} else {
					_value = object.get(key);
				}
			} catch (JSONException e) {
				LOGGER.error("Get json object = " + object + " with key = "
						+ key + " error, exception message = " + e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Json object is null, can't get object for key = "
					+ key);
		}

		return _value;
	}

	/**
	 * @title getIntFromJSONObject
	 * @descriptor get integer from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object integer value
	 * @author Ares
	 */
	public static int getIntFromJSONObject(JSONObject object, String key) {
		return (Integer) getFromJSONObject(object, key, Integer.class);
	}

	/**
	 * @title getLongFromJSONObject
	 * @descriptor get long from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object long value
	 * @author Ares
	 */
	public static long getLongFromJSONObject(JSONObject object, String key) {
		return (Long) getFromJSONObject(object, key, Long.class);
	}

	/**
	 * @title getDoubleFromJSONObject
	 * @descriptor get double from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object double value
	 * @author Ares
	 */
	public static Double getDoubleFromJSONObject(JSONObject object, String key) {
		return (Double) getFromJSONObject(object, key, Double.class);
	}

	/**
	 * @title getBooleanFromJSONObject
	 * @descriptor get boolean from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object boolean value
	 * @author Ares
	 */
	public static Boolean getBooleanFromJSONObject(JSONObject object, String key) {
		return (Boolean) getFromJSONObject(object, key, Boolean.class);
	}

	/**
	 * @title getStringFromJSONObject
	 * @descriptor get string from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object string value
	 * @author Ares
	 */
	public static String getStringFromJSONObject(JSONObject object, String key) {
		return (String) getFromJSONObject(object, key, String.class);
	}

	/**
	 * @title getJSONObjectFromJSONObject
	 * @descriptor get json object from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object json object value
	 * @author Ares
	 */
	public static JSONObject getJSONObjectFromJSONObject(JSONObject object,
			String key) {
		return (JSONObject) getFromJSONObject(object, key, JSONObject.class);
	}

	/**
	 * @title getJSONArrayFromJSONObject
	 * @descriptor get json array from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object json array value
	 * @author Ares
	 */
	public static JSONArray getJSONArrayFromJSONObject(JSONObject object,
			String key) {
		return (JSONArray) getFromJSONObject(object, key, JSONArray.class);
	}

	/**
	 * @title putObject2JSONObject
	 * @descriptor put object value to json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @param value
	 *            : json object value
	 * @return json object
	 * @author Ares
	 */
	public static JSONObject putObject2JSONObject(JSONObject object,
			String key, Object value) {
		// check json object
		if (null != object) {
			try {
				// put object with key to json object
				object.put(key, value);
			} catch (JSONException e) {
				LOGGER.error("Put object = " + value + " with key = " + key
						+ " to json object = " + object
						+ " error, exception message = " + e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Json object is null, can't put object = " + value
					+ " with key = " + key + " to json object");
		}

		return object;
	}

	/**
	 * @title getFromJSONArray
	 * @descriptor get json array value from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : json array object value index
	 * @param valueCls
	 *            : json array object value class
	 * @return json array object value
	 * @author Ares
	 */
	private static Object getFromJSONArray(JSONArray array, int index,
			Class<? extends Object> valueCls) {
		// define json array object value
		Object _value = null;

		// check json array
		if (null != array) {
			try {
				// check value class and get object at index
				if (Integer.class == valueCls) {
					_value = array.getInt(index);
				} else if (Long.class == valueCls) {
					_value = array.getLong(index);
				} else if (Double.class == valueCls) {
					_value = array.getDouble(index);
				} else if (Boolean.class == valueCls) {
					_value = array.getBoolean(index);
				} else if (String.class == valueCls) {
					_value = array.getString(index);
				} else if (JSONObject.class == valueCls) {
					_value = array.getJSONObject(index);
				} else if (JSONArray.class == valueCls) {
					_value = array.getJSONArray(index);
				} else {
					_value = array.get(index);
				}
			} catch (JSONException e) {
				LOGGER.error("Get json array = " + array + " at index = "
						+ index + " error, exception message = "
						+ e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Json array is null, can't get array object at index = "
					+ index);
		}

		return _value;
	}

	/**
	 * @title getIntFromJSONArray
	 * @descriptor get integer from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : json array object value index
	 * @return json array integer value
	 * @author Ares
	 */
	public static int getIntFromJSONArray(JSONArray array, int index) {
		return (Integer) getFromJSONArray(array, index, Integer.class);
	}

	/**
	 * @title getLongFromJSONArray
	 * @descriptor get long from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : json array object value index
	 * @return json array long value
	 * @author Ares
	 */
	public static long getLongFromJSONArray(JSONArray array, int index) {
		return (Long) getFromJSONArray(array, index, Long.class);
	}

	/**
	 * @title getDoubleFromJSONArray
	 * @descriptor get double from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : json array object value index
	 * @return json array double value
	 * @author Ares
	 */
	public static Double getDoubleFromJSONArray(JSONArray array, int index) {
		return (Double) getFromJSONArray(array, index, Double.class);
	}

	/**
	 * @title getBooleanFromJSONArray
	 * @descriptor get boolean from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : json array object value index
	 * @return json array boolean value
	 * @author Ares
	 */
	public static Boolean getBooleanFromJSONArray(JSONArray array, int index) {
		return (Boolean) getFromJSONArray(array, index, Boolean.class);
	}

	/**
	 * @title getStringFromJSONArray
	 * @descriptor get string from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : json array object value index
	 * @return json array string value
	 * @author Ares
	 */
	public static String getStringFromJSONArray(JSONArray array, int index) {
		return (String) getFromJSONArray(array, index, String.class);
	}

	/**
	 * @title getJSONObjectFromJSONArray
	 * @descriptor get json object from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : json array object value index
	 * @return json array json object value
	 * @author Ares
	 */
	public static JSONObject getJSONObjectFromJSONArray(JSONArray array,
			int index) {
		return (JSONObject) getFromJSONArray(array, index, JSONObject.class);
	}

	/**
	 * @title getJSONArrayFromJSONArray
	 * @descriptor get json array from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : json array object value index
	 * @return json array json array value
	 * @author Ares
	 */
	public static JSONArray getJSONArrayFromJSONArray(JSONArray array, int index) {
		return (JSONArray) getFromJSONArray(array, index, JSONArray.class);
	}

}
