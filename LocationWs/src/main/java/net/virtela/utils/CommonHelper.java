package net.virtela.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.virtela.constants.Constants;

/**
 * @author rreyles@virtela.net
 */

public class CommonHelper {

	public static final String OPERATOR_EQUALS = "=";
	public static final String OPERATOR_LESS_THAN = "<";
	public static final String OPERATOR_LESS_THAN_OR_EQUAL = "<=";
	public static final String OPERATOR_GREATER_THAN = ">";
	public static final String OPERATOR_NOT_EQUAL = "<>";
	public static final String OPERATOR_GREATER_THAN_OR_EQUAL = ">=";

	private static String ID_LIST_STRING_SEPARATOR = "\\;";
	public static final int DIFF_TYPE_MINUTES = 0;
	public static final int DIFF_TYPE_SECONDS = 1;
	public static final int DIFF_TYPE_HOURS = 2;
	public static final int DIFF_TYPE_DAYS = 3;

	public static final String ENCODING_UTF8 = "UTF-8";

	public static boolean hasValidList(@SuppressWarnings("rawtypes") final List list) {
		if (list != null && list.size() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a byte array of the supplied inputStream that can be saved to a blob database table field. This method will return a null if the parameter is
	 * null
	 * 
	 * @param inputStream
	 *            the file inpustream you want to convert to byte array
	 * @return byte array version of the InputStream provided
	 * @see InputStream
	 */
	public static byte[] inputStreamToByteArray(InputStream inputStream) {
		if (inputStream != null) {
			try {
				final byte[] test = IOUtils.toByteArray(inputStream);
				inputStream.close();
				return test;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String urlEncode(String value) {
		if (CommonHelper.hasValidValue(value)) {
			try {
				return URLEncoder.encode(value, ENCODING_UTF8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static BigDecimal toWholeNumber(BigDecimal value) {
		return value.round(new MathContext(value.toBigInteger().toString().length(), RoundingMode.HALF_UP));
	}

	/**
	 * 
	 * @param obj
	 *            any object to test if the value is valid
	 * @return boolean true if value is valude and false if not
	 */
	public static boolean hasValidValue(final Object obj) {

		if (obj != null) {
			if (obj.getClass() == String.class) {
				final String strValue = obj.toString();
				if (strValue.trim().length() != 0 && strValue.equalsIgnoreCase(Constants.EMPTY_STRING) == false) {
					return true;
				}
			} else if (isNumber(obj.toString())) {
				double number = toDecimalNumber(obj.toString());
				if (number > 0) {
					return true;
				}
			} else if (obj.getClass() == Date.class) {
				return true;
			} else if (obj.getClass() == ArrayList.class) {
				return true;
			} else if (obj.getClass() == Timestamp.class) {
				return true;
			} else if (obj.getClass() == Boolean.class) {
				return true;
			} else if (obj.getClass() == Double.class) {
				return obj.toString().length() > 0;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param value
	 * @return return true if enterd String is a valid Number
	 */

	public static boolean isNumber(String value) {

		String regex = "^(\\-)?[0-9]+(\\.[0-9][0-9]?)?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();

	}

	/**
	 * 
	 * @param timeStamp
	 * @return java.sql.Timestamp base form the java.util.date epoch date
	 */

	public static java.sql.Timestamp toSqlTimeStamp(long timeStamp) {
		java.util.Date utilDate = new java.util.Date(timeStamp);
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		java.sql.Timestamp sqlTimeStamp = new java.sql.Timestamp(sqlDate.getTime());
		return sqlTimeStamp;
	}

	/**
	 * 
	 * @param strDate
	 *            enter Date String that has this format MM/dd/yyyy
	 * @return java.util.date value of the string date
	 */

	public static java.util.Date stringToDate(String strDate) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date formatedDate = null;
		try {
			formatedDate = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatedDate;
	}

	/**
	 * 
	 * @param doubleVal
	 * @return return the Long value of a double
	 */

	public static Long longToDouble(Double doubleVal) {
		String[] strArr = doubleVal.toString().split("\\.");
		if (strArr.length != 0) {
			return new Long(strArr[0]);
		}
		return new Long(doubleVal.toString());
	}

	/**
	 * 
	 * @param filePath
	 * @return fileName with the extension name
	 */
	public static String getFileName(String filePath) {
		return new File(filePath).getName();
	}

	/**
	 * 
	 * @param filePath
	 * @return file extension
	 */
	public static String getFileType(String filePath) {
		if (hasValidValue(filePath)) {
			final String fileName = filePath.trim();
			final int mid = fileName.lastIndexOf(".");
			return fileName.substring(mid + 1, fileName.length());
		}
		return Constants.EMPTY_STRING;
	}

	/**
	 * 
	 * @param dateExpresion
	 *            date or timestamp to be converted
	 * @return
	 */
	public static String dateToLongFormat(Object dateExpresion) {
		final String excelDateFormat = "MMMMM dd, yyyy";
		final SimpleDateFormat sdf = new SimpleDateFormat(excelDateFormat);
		return sdf.format(dateExpresion);
	}

	/**
	 * 
	 * @param dateExpresion
	 * @return
	 */
	public static String dateToNoteFormat(Object dateExpresion) {
		final String excelDateFormat = "MM/dd/yy | HH:mm";
		final SimpleDateFormat sdf = new SimpleDateFormat(excelDateFormat);
		return sdf.format(dateExpresion);
	}

	/**
	 * 
	 * @param value
	 *            - number to round off
	 * @param decimalPlaces
	 * @return
	 */
	public static double RoundOf(double value, int decimalPlaces) {
		double p = Math.pow(10, decimalPlaces);
		value = value * p;
		float tmp = Math.round(value);
		return (double) tmp / p;
	}

	/**
	 * 
	 * @param value
	 *            - double number to round off
	 * @return rounded off value to whole number (Long)
	 */
	public static Long roundOffToLong(double value) {
		return (Long) Math.round(value);
	}

	/**
	 * 
	 * @param strNum
	 *            - String to convert to a decimal number
	 * @return
	 */

	public static Double toDecimalNumber(Object strNum) {
		if (hasValidValue(strNum) && isNumber(strNum.toString())) {
			return Double.parseDouble(strNum.toString());
		} else {
			return new Double("0");
		}
	}

	/**
	 * 
	 * @param strNum
	 *            - String to convert to a whole number
	 * @return
	 */
	public static Long toNumber(Object objNum) {
		String numString = cleanNumber(objNum);
		if (isNumber(numString)) {
			final String strNum[] = numString.toString().split("\\.");
			if (strNum.length != 0) {
				return Long.parseLong(strNum[0]);
			} else {
				return Long.parseLong(numString.toString());
			}
		} else {
			return 0l;
		}
	}

	private static String cleanNumber(Object numString) {
		if (numString != null) {
			return numString.toString().trim().replace("$", "");
		}
		return "";
	}

	/**
	 * 
	 * @param strNum
	 *            - String to convert to a whole number
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object objNum) {
		if (hasValidValue(objNum)) {
			if (objNum.getClass() == BigDecimal.class) {
				return (BigDecimal) objNum;
			} else if (objNum.getClass() == Double.class) {
				return new BigDecimal((Double) objNum);
			} else if (isNumber(objNum.toString())) {
				return new BigDecimal(objNum.toString());
			}
		}
		return new BigDecimal("0");

	}

	/**
	 * Case-insensitive Matching
	 * 
	 * @param valueOne
	 * @param valueTwo
	 * @return returns true if two object has the same value
	 */
	public static boolean isEqual(final Object valueOne, final Object valueTwo) {
		if (valueOne == null && valueTwo == null) {
			return true;
		} else if ((valueOne == null && valueTwo != null) || (valueOne != null && valueTwo == null)) {
			return false;
		} else if (valueOne.getClass() == String.class && valueTwo.getClass() == String.class) {
			return valueOne.toString().trim().equalsIgnoreCase(valueTwo.toString().trim());
		} else if (isNumber(valueOne.toString()) && isNumber(valueTwo.toString())) {
			return toDecimalNumber(valueOne.toString()).equals(toDecimalNumber(valueTwo.toString()));
		} else if (valueOne.getClass() == valueTwo.getClass()) {
			return valueOne.toString().equals(valueTwo.toString());
		}
		return false;
	}

	/**
	 * @param emailString
	 * @return
	 */
	public static String replaceCommasWithSemiColon(String emailString) {
		Pattern pattern = Pattern.compile("((\"\\w+,?\\s+\\w+\"\\s+)?(<?(\\w+\\@(.\\w+)+)>?)),?");

		Matcher matcher = pattern.matcher(emailString);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "$1;");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String extractEmails(String source) {

		final String EMAIL_PATTERN = "([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})";
		final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(source);
		long emailCount = 0;
		while (matcher.find()) {
			emailCount += 1;
		}
		matcher = pattern.matcher(source);
		final StringBuffer stringBuffer = new StringBuffer();
		int index = 0;

		while (matcher.find()) {
			final String userName = matcher.group(1);
			final String provider = matcher.group(2);
			final String domain = matcher.group(3);

			index += 1;
			if (emailCount != index) {
				stringBuffer.append(userName + "@" + provider + "." + domain);
				stringBuffer.append(";");
			} else {
				stringBuffer.append(userName + "@" + provider + "." + domain);
			}
		}
		return stringBuffer.toString();
	}

	public static boolean isValidEmail(String source) {

		final String EMAIL_PATTERN = "([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})";
		final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(source);
		return matcher.matches();

	}

	/**
	 * Case insensitive Pattern match
	 * 
	 * @param matchPattern
	 *            - pattern to look for
	 * @param value
	 *            - value to find a match
	 * @return return true if enterd String is a valid Number
	 */
	public static boolean patternMatch(final Object valueMatch, final Object value) {
		if (hasValidValue(valueMatch)) {
			final String tmpValueMatch = regexEscapeCharacters(valueMatch.toString());
			if (hasValidValue(value)) {
				final String tmpValue = regexEscapeCharacters(value.toString());
				Pattern pattern = Pattern.compile("(?i)" + tmpValueMatch);
				Matcher matcher = pattern.matcher(tmpValue);
				return matcher.find();
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Case insensitive exact Pattern match (escapes non Alphanumeric chars)
	 * 
	 * @param value
	 *            - pattern to look for
	 * @param valueMatch
	 *            - value to find an exact match
	 * @return return true if value is an exact match of valueMatch
	 */
	public static boolean exactPatternMatch(final Object value, final Object valueMatch) {
		if (hasValidValue(valueMatch)) {
			final String tmpValueMatch = "\\Q" + valueMatch.toString().toUpperCase() + "\\E";
			if (hasValidValue(value)) {
				final String tmpValue = "\\Q" + value.toString().toUpperCase() + "\\E";
				Pattern pattern = Pattern.compile(tmpValueMatch);
				Matcher matcher = pattern.matcher(tmpValue);
				return matcher.find();
			}
		} else {
			return true;
		}
		return false;
	}

	private static String regexEscapeCharacters(final String value) {
		final String[] regCharcters = { "{", "}", "[", "]", "(", ")", "\\" };
		String tmp = value;
		for (String regCharter : regCharcters) {
			tmp = tmp.replace(regCharter, "\\" + regCharter);
		}
		return tmp;
	}

	/**
	 * Compare two time stamp in days
	 * 
	 * @param timeStampOne
	 * @param timeStampTwo
	 * @return
	 */
	public static boolean compreTimeStampDays(final Timestamp timeStampOne, final Timestamp timeStampTwo) {

		if (timeStampOne != null && timeStampTwo != null) {
			final Timestamp timeStampDaysOne = toExactDate(timeStampOne);
			final Timestamp timeStampDaysTwo = toExactDate(timeStampTwo);
			;
			return (timeStampDaysOne == timeStampDaysTwo);
		}
		return false;

	}

	/**
	 * set Timestamp time to 00:00:00
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Timestamp toExactDate(Timestamp timestamp) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(timestamp);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		final long exactTimStamp = cal.getTime().getTime();
		return new Timestamp(exactTimStamp);
	}

	/**
	 * Get todays date with 00:00:00 time
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Timestamp getExactTimeStampToday() {
		return toExactDate(new Timestamp(System.currentTimeMillis()));
	}

	public static long getOneDayInMillis() {
		return 1 * 24 * 60 * 60 * 1000;
	}

	public static java.util.Date getDateToday() {
		return new java.util.Date(System.currentTimeMillis());
	}

	/**
	 * Extract a valid value from a String, if the object is null it will return a String value of "[NULL]"
	 * 
	 * @param objStr
	 * @return
	 */
	public static String getValidStringValue(Object objStr) {
		if (hasValidValue(objStr)) {
			return objStr.toString();
		}
		return "(Empty)";
	}

	public static String getStringValue(Object objStr) {
		if (hasValidValue(objStr)) {
			if (objStr.getClass() == Double.class) {
				return ((Double) objStr).longValue() + "";
			}
			return objStr.toString().trim();
		}
		return "";
	}

	/**
	 * 
	 * @param dateObj
	 *            - Object To convert toa time stamp
	 * @return
	 */
	public static Timestamp toTimestamp(Object dateObj) {
		if (dateObj == null) {
			return null;
		} else if (dateObj.getClass() == Date.class) {
			final Date tempDate = (Date) dateObj;
			return new Timestamp(tempDate.getTime());
		} else if (dateObj.getClass() == java.sql.Date.class) {
			final java.sql.Date tempDate = (java.sql.Date) dateObj;
			return new Timestamp(tempDate.getTime());
		} else if (dateObj.getClass() == Timestamp.class) {
			return (Timestamp) dateObj;
		}
		return null;
	}

	public static void deleteFile(String filePath) {
		if (hasValidValue(filePath)) {
			final File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public static String toInPrameter(List<String> listOfString) {
		final StringBuffer inParam = new StringBuffer();
		int index = 0;
		for (String param : listOfString) {
			inParam.append("'");
			inParam.append(param.replace("'", "''"));
			inParam.append("'");
			if ((index + 1) < listOfString.size()) {
				inParam.append(", ");
			}
			index += 1;
		}
		return inParam.toString();
	}

	public static String toNumberInPrameter(List<Long> listOfLong) {
		final StringBuffer inParam = new StringBuffer();
		int index = 0;
		for (Long param : listOfLong) {
			inParam.append(param);
			if ((index + 1) < listOfLong.size()) {
				inParam.append(", ");
			}
			index += 1;
		}
		return inParam.toString();
	}

	public static List<String> idListStringToStrList(String idListString) {
		if (CommonHelper.hasValidValue(idListString)) {
			return Arrays.asList(idListString.split(ID_LIST_STRING_SEPARATOR));
		}
		return Collections.emptyList();
	}

	public static String strListToIdListString(List<String> strList) {
		final StringBuffer idListString = new StringBuffer();
		int index = 0;
		if (strList != null) {
			for (String str : strList) {
				idListString.append(str);
				if ((index + 1) < strList.size()) {
					idListString.append(";");
				}
				index += 1;
			}
		}
		return idListString.toString();
	}

	public static String booleanToShortString(String value) {
		if (CommonHelper.hasValidValue(value) && value.equalsIgnoreCase("True")) {
			return "T";
		}
		return "F";
	}

	public static String booleanToShortStringYesNo(String value) {
		if (CommonHelper.hasValidValue(value) && value.equalsIgnoreCase("True")) {
			return "Y";
		}
		return "N";
	}

	public static String translateBooleanShortString(String value) {
		if (CommonHelper.hasValidValue(value) && (value.equalsIgnoreCase("t") || value.equalsIgnoreCase("y"))) {
			return "true";
		}
		return "false";
	}

	public static String toShortStringBoolean(Object flag) {
		String booleanString = getStringValue(flag);
		if (booleanString.equalsIgnoreCase("Yes")) {
			return "Y";
		} else {
			return "N";
		}
	}

	public static String toBooleanString(String strBolean) {
		if (strBolean != null && (strBolean.equalsIgnoreCase("T") || strBolean.equalsIgnoreCase("Y") || strBolean.equalsIgnoreCase("Yes")
		        || strBolean.equalsIgnoreCase("True"))) {
			return "T";
		}
		return "F";
	}

	public static boolean isNonUsCountry(String country) {
		if (!country.equalsIgnoreCase("USA") && !country.equalsIgnoreCase("US") && !country.equalsIgnoreCase("United States")) {
			return true;
		}
		return false;
	}

	public static String booleanYesNoToShortString(String value) {
		if (CommonHelper.hasValidValue(value) && value.equalsIgnoreCase("True")) {
			return "Y";
		}
		return "N";
	}

	public static String removeWhiteSpace(String string) {
		if (hasValidValue(string)) {
			String newString = string.replaceAll(" ", "");
			return newString;
		}
		return "";
	}

	public static void writeToCsv(String path, String message) {
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(path, true));
			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // always close the file
			if (bufferedWriter != null)
				try {
					bufferedWriter.close();
				} catch (IOException ioe2) {
				}
		}
	}

	/**
	 * 
	 * @param strValue
	 *            - String to parse
	 * @return
	 */
	public static Integer getIntegerFromString(String strValue) {
		if (hasValidValue(strValue)) {
			Pattern pattern = Pattern.compile("\\d+");
			Matcher matcher = pattern.matcher(strValue);
			if (matcher.find()) {
				return toNumber(matcher.group()).intValue();
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param strValue
	 *            - String to parse
	 * @return
	 */
	public static Double getDoubleFromString(String strValue) {
		if (hasValidValue(strValue)) {
			Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+))");
			Matcher matcher = pattern.matcher(strValue);
			while (matcher.find()) {
				return Double.parseDouble(matcher.group(1));
			}
		}
		return 0.0;
	}

	/**
	 * 
	 * @param speed
	 *            - Speed, obj will be converted to string, '1G', '200M', '512kb'
	 * @return longVal - '1000000', '512'
	 */
	public static Long convertSpeedStrToLong(Object speed) {
		final String speedStr = getStringValue(speed);
		Double doubleVal = getDoubleFromString(speedStr);
		if (!CommonHelper.hasValidValue(doubleVal)) {
			doubleVal = getIntegerFromString(speedStr).doubleValue();
		}

		long longVal = 0;
		if (speedStr.contains("M")) {
			longVal = toNumber(doubleVal * 1000);
		} else if (speedStr.contains("G")) {
			longVal = toNumber(toNumber(doubleVal) * 1000000);
		} else {
			longVal = toNumber(doubleVal);
		}
		return longVal;
	}

	/**
	 * formats the String before exporting to CSV
	 * 
	 * @param field
	 * @return
	 */
	public static String formatCsvString(String field) {
		if (CommonHelper.hasValidValue(field)) {
			field = field.replaceAll("[\n\r]", " ");
			if (field.contains(",") || (field.contains("(") && field.contains(")"))) {
				field = InsertDoubleQuotation(field);
			}
			return field;
		}
		return "";
	}

	public static String InsertDoubleQuotation(String value) {
		final StringBuffer str = new StringBuffer();
		str.append("\"\'");
		str.append(value);
		str.append("\'\"");
		return str.toString();
	}

	/**
	 * 
	 * @param startDate
	 *            - Should be the smaller date
	 * @param endDate
	 *            - Should be the larger date
	 * @return - returns a long represrantion of the date diffrence in days
	 */
	public static Long computeWorkingdayDifference(Timestamp startDate, Timestamp endDate) {

		startDate = toExactDate(startDate);
		endDate = toExactDate(endDate);

		long workingDays = 0;
		final Calendar startCal = Calendar.getInstance();
		final Calendar endCal = Calendar.getInstance();
		boolean negateResult = false;

		startCal.setTime(startDate);
		endCal.setTime(endDate);

		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(endDate);
			endCal.setTime(startDate);
			negateResult = true;
		}

		while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				workingDays += 1;
			}
			startCal.add(Calendar.DAY_OF_MONTH, 1);
		}

		if (negateResult) {
			return (workingDays - 1) * -1;
		} else {
			return workingDays;
		}
	}

	public static String sqlStringClean(String value) {
		if (CommonHelper.hasValidValue(value)) {
			value = value.replace("'", "''");
			value = value.trim();
		}
		return value;
	}

	/**
	 * 
	 * @param date
	 *            - Start date
	 * @param days
	 *            - days to add the start date
	 * @return
	 */
	public static Timestamp addWorkingDays(Timestamp date, int days) {
		if (date != null && days > 0) {
			final Calendar dateCal = Calendar.getInstance();
			dateCal.setTime(date);
			int index = 1;
			while (index < days) {
				dateCal.add(Calendar.DAY_OF_MONTH, 1);
				if (dateCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && dateCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
					index += 1;
				}
			}
			return new Timestamp(dateCal.getTimeInMillis());
		}
		return null;
	}

	/**
	 * Adds n number of days to a certain timestamp, regardless of whether weekday or weekend
	 * 
	 * @param timestamp
	 *            - days to add the start date
	 * @param numOfDays
	 *            - days to add the start date
	 * @return
	 */
	public static Timestamp addDaysToTimestamp(Timestamp timestamp, int numOfDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(timestamp);
		cal.add(Calendar.DAY_OF_WEEK, numOfDays);
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * 
	 * @param baseDate
	 *            - Base date
	 * @param days
	 *            - days to minus the basis date
	 * @return
	 */
	public static Timestamp minusDayss(Timestamp baseDate, int days, boolean inWorkingDays) {
		if (baseDate != null && days > 0) {
			final Calendar dateCal = Calendar.getInstance();
			dateCal.setTime(baseDate);
			int index = 1;
			while (index <= days) {
				dateCal.add(Calendar.DAY_OF_MONTH, -1);
				if (inWorkingDays
				        || (dateCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && dateCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
					index += 1;
				}
			}
			return new Timestamp(dateCal.getTimeInMillis());
		}
		return null;
	}

	/**
	 * 
	 * @param valueObj
	 *            - current valu
	 * @param maximunObj
	 *            - max value
	 * 
	 *            if Current value is greater than the max value it will be switched
	 * 
	 * @return
	 */
	public static String getPercentage(Object valueObj, Object maximunObj) {
		return computePercentage(valueObj, maximunObj) + "%";
	}

	public static String getPercentageWhole(Object valueObj, Object maximunObj) {
		return roundOffToLong(computePercentage(valueObj, maximunObj)) + "%";
	}

	public static Double computePercentage(Object valueObj, Object maximunObj) {
		double value = toDecimalNumber(valueObj);
		double maximun = toDecimalNumber(maximunObj);
		if (value > maximun) {
			double temp = value;
			value = maximun;
			maximun = temp;
		}
		double percentage = (value / maximun) * 100;
		return RoundOf(percentage, 2);
	}

	public static String getPriceDiffPercentage(Object valueObj, Object maximunObj) {
		double value = toDecimalNumber(valueObj);
		double maximun = toDecimalNumber(maximunObj);
		if (value > maximun) {
			double temp = value;
			value = maximun;
			maximun = temp;
		} else if (value == maximun) {
			return "0%";
		}
		double percentage = (value / maximun) * 100;

		if (percentage == 0) {
			return "100%";
		}

		double priceDiff = 100 - percentage;
		return toNumber(RoundOf(priceDiff, 0)) + "%";
	}

	public static long dateDiff(Timestamp samllerDate, Timestamp biggerDate, int diffType) {
		if (biggerDate == null) {
			biggerDate = new Timestamp((new Date()).getTime());
		}
		long diff = biggerDate.getTime() - samllerDate.getTime();
		if (diffType == DIFF_TYPE_MINUTES) {
			return diff / (60 * 1000);
		} else if (diffType == DIFF_TYPE_SECONDS) {
			return diff / 1000;
		} else if (diffType == DIFF_TYPE_HOURS) {
			return diff / (60 * 60 * 1000);
		} else if (diffType == DIFF_TYPE_DAYS) {
			return diff / (24 * 60 * 60 * 1000);
		}
		return 0;
	}

	public static long dateDayAge(Date date) {
		long diff = date.getTime() - getExactTimeStampToday().getTime();
		return diff / (24 * 60 * 60 * 1000);
	}

	public static String removeLineBreaks(String data) {
		return data.replace("\n", " ").replace("\r", " ");
	}

	public static boolean hasLineBreaks(String data) {
		return data.contains("\n") || data.contains("\r");
	}

	public static String toUsdFormat(long amount) {
		NumberFormat formatter = new DecimalFormat("#,##0");
		if (amount < 0) {
			return "($" + formatter.format(-1 * amount) + ")";
		} else {
			return '$' + formatter.format(amount);
		}

	}

	public static String toNumberFormat(long amount) {
		NumberFormat formatter = new DecimalFormat("#,##0");
		if (amount < 0) {
			return "(" + formatter.format(-1 * amount) + ")";
		} else {
			return formatter.format(amount);
		}
	}

	public static int getNumberOfDays(Timestamp t1, Timestamp t2) throws ParseException {

		Date date1 = new Date(t1.getTime());
		Date date2 = new Date(t2.getTime());

		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	}

	public static String numberToShortSpeedFormat(Object numSpeedObj) {
		final StringBuffer strSpeed = new StringBuffer();

		if (numSpeedObj != null && isNumber(numSpeedObj.toString())) {
			double numSpeed = toDecimalNumber(numSpeedObj);
			DecimalFormat format = new DecimalFormat();
			format.setDecimalSeparatorAlwaysShown(false);
			if (numSpeed > 999 && numSpeed < 999999) {
				strSpeed.append(format.format(numSpeed / 1000));
				strSpeed.append("M");
			} else if (numSpeed > 999999 && numSpeed < 99999999) {
				strSpeed.append(format.format(numSpeed / 1000000));
				strSpeed.append("G");
			} else {
				strSpeed.append(format.format(numSpeed));
				strSpeed.append("K");
			}
		}

		return strSpeed.toString();
	}

	public static Long extraxNumberFromString(Object str) {
		if (CommonHelper.hasValidValue(str)) {
			Pattern p = Pattern.compile("[0-9]+");
			Matcher m = p.matcher(str.toString());
			String strNum = "";
			while (m.find()) {
				strNum += Integer.parseInt(m.group());
			}
			return toNumber(strNum);
		}
		return 0l;
	}

	public static String extractPricingMangerUserName(String name) {
		if (name != null) {
			String tempName = name.trim();
			String[] arrTemNameArr = tempName.split(" ");
			if (arrTemNameArr.length == 2) {
				return arrTemNameArr[0].subSequence(0, 1) + arrTemNameArr[1];
			}
		}
		return "";
	}

	public static boolean isValidDiversity(String value) {
		String regex = "^[0-9]+-+[A-f]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}

	/**
	 * 
	 * @param operator
	 * @param value
	 * @param ruleValue
	 * @return
	 */

	public static boolean isOperatorMatch(String operator, long value, long ruleValue) {
		if (hasValidValue(operator)) {
			boolean isMatch = false;
			if (operator.equals(OPERATOR_EQUALS)) {
				isMatch = ruleValue == value;
			} else if (operator.equals(OPERATOR_GREATER_THAN)) {
				isMatch = value > ruleValue;
			} else if (operator.equals(OPERATOR_GREATER_THAN_OR_EQUAL)) {
				isMatch = value >= ruleValue;
			} else if (operator.equals(OPERATOR_LESS_THAN)) {
				isMatch = value < ruleValue;
			} else if (operator.equals(OPERATOR_LESS_THAN_OR_EQUAL)) {
				isMatch = value <= ruleValue;
			} else if (operator.equals(OPERATOR_NOT_EQUAL)) {
				isMatch = value != ruleValue;
			}
			return isMatch;
		}
		return false;
	}

	public static boolean isOperatorBetweenMatch(long value, long minRange, String minRangeOperator, long maxRange, String maxRangeOperator) {
		if (CommonHelper.hasValidValue(value)) {

			boolean minRangeMatch = false;
			boolean maxRangeMatch = false;

			minRangeMatch = CommonHelper.isOperatorMatch(minRangeOperator, value, minRange);
			maxRangeMatch = CommonHelper.isOperatorMatch(maxRangeOperator, value, maxRange);

			return (minRangeMatch && maxRangeMatch);

		}
		return false;
	}

	public static String translateToYearQuarter(Timestamp date) {
		final Timestamp value = toExactDate(date);
		final SimpleDateFormat extractYear = new SimpleDateFormat("yyyy");
		final long year = toNumber(extractYear.format(value));
		if (value.before(stringToDate("01/02/" + year))) {
			return (year - 1) + "-4";
		} else if (value.after(stringToDate("01/01/" + year)) && value.before(stringToDate("04/02/" + year))) {
			return year + "-1";
		} else if (value.after(stringToDate("04/01/" + year)) && value.before(stringToDate("07/02/" + year))) {
			return year + "-2";
		} else if (value.after(stringToDate("07/01/" + year)) && value.before(stringToDate("10/02/" + year))) {
			return year + "-3";
		} else if (value.after(stringToDate("10/01/" + year)) && value.before(stringToDate("01/02/" + (year + 1)))) {
			return year + "-4";
		}
		return "";
	}

	/**
	 * Specify the range by integer range. Method will return the values from the range give.
	 * 
	 * @param longList
	 * @param from
	 * @param to
	 * @return
	 */
	public static List<Long> getListNumberValuesByRange(List<Long> longList, int from, int to) {
		final List<Long> numberList = new ArrayList<Long>();
		for (int index = from; index < (to + from); index++) {
			if (index < longList.size()) {
				numberList.add(longList.get(index));
			}
		}
		return numberList;
	}

	public static List<String> getListStringValuesByRange(List<String> list, int from, int to) {
		final List<String> stringList = new ArrayList<String>();
		for (int index = from; index < to; index++) {
			if (index < list.size()) {
				stringList.add(list.get(index));
			} else {
				break;
			}
		}
		return stringList;
	}

	private static List<Long> objListToNumberList(List<Object> objList, boolean nonUnique) {
		final List<Long> numberList = new ArrayList<Long>();
		if (objList != null) {
			for (Object obj : objList) {
				if (hasValidValue(obj)) {
					if (nonUnique || numberList.contains(toNumber(obj)) == false) {
						numberList.add(toNumber(obj));
					}
				}
			}
		}
		return numberList;
	}

	public static List<Long> objListToUniqueNumberList(List<Object> objList) {
		return objListToNumberList(objList, false);
	}

	public static List<Long> objListToNumberList(List<Object> objList) {
		return objListToNumberList(objList, true);
	}

	public static String objectToJason(Object obj) {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int countWordsInString(String value) {
		if (CommonHelper.hasValidValue(value)) {
			if (value.contains(" ")) {
				return value.split(" ").length;
			} else {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Return a boolean string flag that is used to determine if flag Active. Case insensitive Matching
	 * 
	 * @param booleanStr
	 *            flag to format to the correct flag value
	 * @return a String Flag based on the provided String parameter
	 */
	public static String formatStrBoolean(final Object booleanFlag) {
		if (CommonHelper.hasValidValue(booleanFlag)) {
			final String strBooleanFlag = booleanFlag.toString().toUpperCase();
			if (isEqual(strBooleanFlag, "1") || (isEqual(strBooleanFlag, "T") || isEqual(strBooleanFlag, "Y"))) {
				return Constants.BOOLEAN_STR_TRUE;
			}
		}
		return Constants.BOOLEAN_STR_FALSE;
	}

	/**
	 * 
	 * @param booleanStr
	 *            boolean String flag to test
	 * @return True if the boolean String flag is Active
	 */
	public static boolean isBooleanFlagActive(Object booleanFlag) {
		return isEqual(formatStrBoolean(booleanFlag), Constants.BOOLEAN_STR_TRUE);
	}

	/**
	 * @param an
	 *            object that will be casted to java.sql.Clob
	 * @return an aggregated String value of the object
	 * @see Clob
	 */
	public static String convertClobToString(Object obj) {
		final StringBuffer clobString = new StringBuffer();
		if (obj != null) {
			final Clob clobData = (Clob) obj;
			try {
				final BufferedReader bufferedReader = new BufferedReader(clobData.getCharacterStream());
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					clobString.append(line);
				}
				bufferedReader.close();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}

		}
		return clobString.toString();
	}

	/**
	 * Translates a True/False or Yes/No value to int value
	 * 
	 * @param strBoolean
	 * @return 1 = true, 0 = false
	 */
	public static int toBooleanInt(String strBoolean) {
		if (hasValidValue(strBoolean) && (strBoolean.equalsIgnoreCase("T") || strBoolean.equalsIgnoreCase("Y") || strBoolean.equalsIgnoreCase("Yes")
		        || strBoolean.equalsIgnoreCase("True"))) {
			return Constants.BOOLEAN_INT_TRUE;
		}
		return Constants.BOOLEAN_INT_FALSE;
	}

	/**
	 * Translates a 1/0 value to String flag value
	 * 
	 * @param intBoolean
	 * @return T = true, F = false
	 */
	public static String toBooleanStrFlag(Integer intBoolean) {
		if (hasValidValue(intBoolean) && intBoolean == 1) {
			return Constants.BOOLEAN_STR_TRUE;
		}
		return Constants.BOOLEAN_STR_FALSE;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public static Integer toInteger(Object object) {
		if (object != null && isNumber(getStringValue(object))) {
			return Integer.parseInt(getStringValue(object));
		}
		return 0;
	}
	/**
	 * Removes spaces, alphabets or special characters using regex so that only numbers will remain
	 * 
	 * @param numberStr
	 * @return a String consisted only of numbers
	 */
	public static String cleanStrNumber(Object numberStr) {
		if (numberStr != null) {
			return getStringValue(numberStr).replaceAll("[^\\d]", "");
		}
		return Constants.EMPTY;
	}

	/**
	 *
	 * @param filePath
	 * @return file extension
	 */
	public static String getFileExtension(String filePath) {
		final String fileName = filePath;
		final int mid = fileName.lastIndexOf(".");
		return fileName.substring(mid + 1, fileName.length());
	}

	/**
	 * Fist check if DIR exist, if does DIR will be deleted before create a new DIR
	 * 
	 * @param filePath
	 */
	public static void createDir(String filePath) {
		if (hasValidValue(filePath)) {
			final File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			new File(filePath).mkdir();
		}
	}

	public static void zipFolder(String source, String savePath) {
		try {
			final ZipFile zipFile = new ZipFile(savePath);
			final ZipParameters parameters = new ZipParameters();
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			zipFile.addFolder(source, parameters);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	public static long generateUniqueId() {
		return (((System.currentTimeMillis() / 12) / 02) / 12);
	}

	public static String mapKeyToFormat(String format, Map<String, String> keyMapping) {
		if (CommonHelper.hasValidValue(format) && keyMapping != null) {
			for (String mapKey : keyMapping.keySet()) {
				format = format.replace(mapKey, keyMapping.get(mapKey));
			}
		}
		return format;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<?> removeDuplicates(List list) {
		if (hasValidList(list)) {
			final List distinctList = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				Object object = (Object) list.get(i);
				if (distinctList.contains(object) == false) {
					distinctList.add(object);
				}
			}
			return distinctList;
		}
		return null;
	}

}
