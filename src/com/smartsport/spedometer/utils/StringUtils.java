/**
 * 
 */
package com.smartsport.spedometer.utils;

import java.util.Vector;

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

	/**
	 * @title split
	 * @descriptor split the string with the given split word
	 * @param string
	 *            : the string will be splitted
	 * @param splitWord
	 *            : the split word
	 * @return the splitted string array
	 * @author Ares
	 */
	public static String[] split(String string, String splitWord) {
		// define return string array
		String[] _ret = null;

		// define sentences vector
		Vector<String> _sentences = new Vector<String>();

		// append split word to end for splitting if not ended with it
		if (!string.endsWith(splitWord)) {
			string += splitWord;
		}

		// process not null or empty string
		if (null != string && !string.equals("")) {
			for (int i = 0, j = 0;;) {
				i = string.indexOf(splitWord, j);

				if (i >= 0 && i > j) {
					_sentences.addElement(string.substring(j, i));
				}

				if (i < 0 || i == (string.length() - splitWord.length())) {
					break;
				}

				j = i + splitWord.length();
			}

		}

		// reset return result
		if (_sentences.size() > 0) {
			_ret = new String[_sentences.size()];
			_sentences.copyInto(_ret);
		}

		return _ret;
	}

}
