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
			// reversal json object keys
			for (Iterator<?> _iterator = object.keys(); _iterator.hasNext();) {
				// add json object key in set
				_keys.add((String) _iterator.next());
			}
		} else {
			LOGGER.error("Json object is null, can't get all keys set");
		}

		return _keys;
	}

	/**
	 * @title objectFromJSONObject
	 * @descriptor get json object value with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @param valueCls
	 *            : json object value class
	 * @return json object value
	 * @author Ares
	 */
	private static Object objectFromJSONObject(JSONObject object, String key,
			Class<? extends Object> valueCls) {
		// define json object value object
		Object _value = null;

		// check json object
		if (null != object) {
			try {
				// check value class and get json object value with key
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
				LOGGER.error("Get json object = " + object
						+ " value with key = " + key
						+ " error, exception message = " + e.getMessage());

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
	 * @descriptor get json object integer value from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object integer value
	 * @author Ares
	 */
	public static int getIntFromJSONObject(JSONObject object, String key) {
		return (Integer) objectFromJSONObject(object, key, Integer.class);
	}

	/**
	 * @title getLongFromJSONObject
	 * @descriptor get json object long value from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object long value
	 * @author Ares
	 */
	public static long getLongFromJSONObject(JSONObject object, String key) {
		return (Long) objectFromJSONObject(object, key, Long.class);
	}

	/**
	 * @title getDoubleFromJSONObject
	 * @descriptor get json object double from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object double value
	 * @author Ares
	 */
	public static Double getDoubleFromJSONObject(JSONObject object, String key) {
		return (Double) objectFromJSONObject(object, key, Double.class);
	}

	/**
	 * @title getBooleanFromJSONObject
	 * @descriptor get json object boolean from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object boolean value
	 * @author Ares
	 */
	public static Boolean getBooleanFromJSONObject(JSONObject object, String key) {
		return (Boolean) objectFromJSONObject(object, key, Boolean.class);
	}

	/**
	 * @title getStringFromJSONObject
	 * @descriptor get json object string from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object string value
	 * @author Ares
	 */
	public static String getStringFromJSONObject(JSONObject object, String key) {
		return (String) objectFromJSONObject(object, key, String.class);
	}

	/**
	 * @title getJSONObjectFromJSONObject
	 * @descriptor get json object json object from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object json object value
	 * @author Ares
	 */
	public static JSONObject getJSONObjectFromJSONObject(JSONObject object,
			String key) {
		return (JSONObject) objectFromJSONObject(object, key, JSONObject.class);
	}

	/**
	 * @title getJSONArrayFromJSONObject
	 * @descriptor get json object json array from json object with key
	 * @param object
	 *            : json object
	 * @param key
	 *            : json object key
	 * @return json object json array value
	 * @author Ares
	 */
	public static JSONArray getJSONArrayFromJSONObject(JSONObject object,
			String key) {
		return (JSONArray) objectFromJSONObject(object, key, JSONArray.class);
	}

	/**
	 * @title putObject2JSONObject
	 * @descriptor put json object value to json object with key
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
				// put json object value with key to json object
				object.put(key, value);
			} catch (JSONException e) {
				LOGGER.error("Put object = " + value + " with key = " + key
						+ " to json object = " + object
						+ " error, exception message = " + e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Json object is null, can't put json object value = "
					+ value + " with key = " + key + " to json object");
		}

		return object;
	}

	/**
	 * @title objectFromJSONArray
	 * @descriptor get json array element from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : element index in the json array
	 * @param elementCls
	 *            : json array element class
	 * @return json array element
	 * @author Ares
	 */
	private static Object objectFromJSONArray(JSONArray array, int index,
			Class<? extends Object> valueCls) {
		// define json array element
		Object _element = null;

		// check json array
		if (null != array) {
			try {
				// check json array element class and get element at index
				if (Integer.class == valueCls) {
					_element = array.getInt(index);
				} else if (Long.class == valueCls) {
					_element = array.getLong(index);
				} else if (Double.class == valueCls) {
					_element = array.getDouble(index);
				} else if (Boolean.class == valueCls) {
					_element = array.getBoolean(index);
				} else if (String.class == valueCls) {
					_element = array.getString(index);
				} else if (JSONObject.class == valueCls) {
					_element = array.getJSONObject(index);
				} else if (JSONArray.class == valueCls) {
					_element = array.getJSONArray(index);
				} else {
					_element = array.get(index);
				}
			} catch (JSONException e) {
				LOGGER.error("Get json array = " + array
						+ " element at index = " + index
						+ " error, exception message = " + e.getMessage());

				e.printStackTrace();
			}
		} else {
			LOGGER.error("Json array is null, can't get array element at index = "
					+ index);
		}

		return _element;
	}

	/**
	 * @title getIntFromJSONArray
	 * @descriptor get integer element from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : element index in the json array
	 * @return json array integer element
	 * @author Ares
	 */
	public static int getIntFromJSONArray(JSONArray array, int index) {
		return (Integer) objectFromJSONArray(array, index, Integer.class);
	}

	/**
	 * @title getLongFromJSONArray
	 * @descriptor get long element from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : element index in the json array
	 * @return json array long element
	 * @author Ares
	 */
	public static long getLongFromJSONArray(JSONArray array, int index) {
		return (Long) objectFromJSONArray(array, index, Long.class);
	}

	/**
	 * @title getDoubleFromJSONArray
	 * @descriptor get double element from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : element index in the json array
	 * @return json array double element
	 * @author Ares
	 */
	public static Double getDoubleFromJSONArray(JSONArray array, int index) {
		return (Double) objectFromJSONArray(array, index, Double.class);
	}

	/**
	 * @title getBooleanFromJSONArray
	 * @descriptor get boolean element from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : element index in the json array
	 * @return json array boolean element
	 * @author Ares
	 */
	public static Boolean getBooleanFromJSONArray(JSONArray array, int index) {
		return (Boolean) objectFromJSONArray(array, index, Boolean.class);
	}

	/**
	 * @title getStringFromJSONArray
	 * @descriptor get string element from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : element index in the json array
	 * @return json array string element
	 * @author Ares
	 */
	public static String getStringFromJSONArray(JSONArray array, int index) {
		return (String) objectFromJSONArray(array, index, String.class);
	}

	/**
	 * @title getJSONObjectFromJSONArray
	 * @descriptor get json object element from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : element index in the json array
	 * @return json array json object element
	 * @author Ares
	 */
	public static JSONObject getJSONObjectFromJSONArray(JSONArray array,
			int index) {
		return (JSONObject) objectFromJSONArray(array, index, JSONObject.class);
	}

	/**
	 * @title getJSONArrayFromJSONArray
	 * @descriptor get json array element from json array at index
	 * @param array
	 *            : json array
	 * @param index
	 *            : element index in the json array
	 * @return json array json array element
	 * @author Ares
	 */
	public static JSONArray getJSONArrayFromJSONArray(JSONArray array, int index) {
		return (JSONArray) objectFromJSONArray(array, index, JSONArray.class);
	}

}
