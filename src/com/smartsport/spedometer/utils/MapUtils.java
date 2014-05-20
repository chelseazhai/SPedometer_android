/**
 * 
 */
package com.smartsport.spedometer.utils;

import java.util.Map;

/**
 * @name MapUtils
 * @descriptor map utils
 * @author Ares
 * @version 1.0
 */
public class MapUtils {

	// logger
	private static final SSLogger LOGGER = new SSLogger(MapUtils.class);

	/**
	 * @title pop
	 * @descriptor pop the value of the mapping with the specified key
	 * @param map
	 *            : the map
	 * @param key
	 *            : the key
	 * @return the value of the mapping with the specified key, or null if no
	 *         mapping for the specified key is found
	 * @author Ares
	 * @return
	 */
	public static Object pop(Map<String, ?> map, String key) {
		Object _value = null;

		// check the map and the key
		if (null != map && null != key) {
			// get the value of the map with the key
			_value = map.get(key);

			// remove the value of the map with the key
			map.remove(key);
		} else {
			LOGGER.equals("Pop the value of the map = " + map + " with key = "
					+ key + " error, the map or the key is null");
		}

		return _value;
	}

}
