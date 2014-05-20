/**
 * 
 */
package com.smartsport.spedometer.utils;

/**
 * @name StringUtils
 * @descriptor string utils
 * @author Ares
 * @version 1.0
 */
public class StringUtils {

	// logger
	private static final SSLogger LOGGER = new SSLogger(StringUtils.class);

	/**
	 * @title sTrim
	 * @descriptor trim string from the string
	 * @param string
	 *            : the string will be trimmed
	 * @param trim
	 *            : the trim string
	 * @return the string had been trimmed
	 * @author Ares
	 */
	public static String sTrim(String string, String trim) {
		// check the string and the trim string
		if (null != string && null != trim) {
			// trim the head and end trim string
			while (string.startsWith(trim)) {
				string = string.substring(trim.length());
			}
			while (string.endsWith(trim)) {
				string = string.substring(0, string.length() - trim.length());
			}
		} else {
			LOGGER.error("Trim string from the string error, the string = "
					+ string + " and the trim = " + trim);
		}

		return string;
	}

	/**
	 * @title strip
	 * @descriptor strip string from the string
	 * @param string
	 *            : the string will be stripped
	 * @param strip
	 *            : the strip string
	 * @return the string had been stripped
	 * @author Ares
	 */
	public static String strip(String string, String strip) {
		// check the string and the strip string
		if (null != string && null != strip) {
			// replace all strip string using empty string
			string = string.replaceAll(strip, "");
		} else {
			LOGGER.error("Strip string from the string error, the string = "
					+ string + " and the strip = " + strip);
		}

		return string;
	}

	/**
	 * @title cStrip
	 * @descriptor strip characters string from the string
	 * @param string
	 *            : the string will be stripped
	 * @param stripChars
	 *            : the strip characters string
	 * @return the string had been stripped
	 * @author Ares
	 */
	public static String cStrip(String string, String stripChars) {
		// define the string had been stripped string builder
		StringBuilder _strippedStringBuilder = null;

		// check the string and the strip characters string
		if (null != string) {
			// initialize the stripped string builder
			_strippedStringBuilder = new StringBuilder(null != stripChars ? ""
					: string);

			if (null != stripChars) {
				// traversal the string
				for (int i = 0; i < string.length(); i++) {
					// get the character
					char _char = string.charAt(i);

					// check the character
					if (!stripChars.contains(new String(new char[] { _char }))) {
						// append to string builder
						_strippedStringBuilder.append(_char);
					}
				}
			}
		} else {
			LOGGER.error("Strip string from the characters string error, the string = "
					+ string + " and the strip characters = " + stripChars);
		}

		return (String) (null != _strippedStringBuilder ? _strippedStringBuilder
				.toString() : _strippedStringBuilder);
	}

}
