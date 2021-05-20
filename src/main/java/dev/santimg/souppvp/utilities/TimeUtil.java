package dev.santimg.souppvp.utilities;

import java.text.SimpleDateFormat;
import java.util.*;

import lombok.experimental.UtilityClass;

/**
 * Time utilities
 * 
 * @author SantiMG
 */
@UtilityClass
public final class TimeUtil {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public static final Date NOW;

	static {
		NOW = new Date();
	}

	/**
	 * Format the date now using the short date format
	 */
	public static String formatNow() {
		return DATE_FORMAT.format(NOW);
	}

	/**
	 * Format the date now using the full date format
	 */
	public static String formatFullNow() {
		return FULL_DATE_FORMAT.format(NOW);
	}

	/**
	 * Format a date using the short date format
	 */
	public static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}

	/**
	 * Format a date using the full date format
	 */
	public static String formatFullDate(Date date) {
		return FULL_DATE_FORMAT.format(date);
	}

	/**
	 * Get the time difference in milliseconds between two given time stamps
	 */
	public static long getMillisBetween(long dateTime1, long dateTime2) {
		return Math.max(dateTime1, dateTime2) - Math.min(dateTime1, dateTime2);
	}

	/**
	 * Get the time difference in milliseconds between two given dates
	 */
	public static long getMillisBetween(Date date1, Date date2) {
		return getMillisBetween(date1.getTime(), date2.getTime());
	}

	/**
	 * Convert milliseconds to a 0.0 number
	 * 
	 * @param timeInMilliseconds - the time in milliseconds
	 * @return - the formatted number
	 */
	public static double roundMillisecondsToDouble(long timeInMilliseconds) {
		double seconds = timeInMilliseconds / 1000D;
		return Math.round(10D * seconds) / 10D;
	}

	/**
	 * Format seconds to a 00:00 time format (days:hours:minutes:seconds)
	 * 
	 * @param timeInSeconds - the time to format in seconds
	 * @return - the formatted time string
	 */
	public static String formatSecondsTo00_00(long timeInSeconds) {
		long days = timeInSeconds / 86400;
		long hours = (timeInSeconds % 86400) / 3600;
		long minutes = (timeInSeconds % 3600) / 60;
		long seconds = (timeInSeconds % 3600) % 60;

		StringBuilder formattedTime = new StringBuilder();

		if (days >= 1) {
			formattedTime.append(days >= 10 ? days : "0" + days).append(":");
			formattedTime.append(hours >= 10 ? hours : "0" + hours).append(":");
		} else {
			if (hours >= 1) {
				formattedTime.append(hours >= 10 ? hours : "0" + hours).append(":");
			}
		}

		formattedTime.append(minutes >= 10 ? minutes : "0" + minutes).append(":");
		formattedTime.append(seconds >= 10 ? seconds : "0" + seconds);

		return formattedTime.toString();
	}

	/**
	 * Format milliseconds to a 00:00 time format (days:hours:minutes:seconds)
	 * 
	 * @param timeInSeconds - the time to format in milliseconds
	 * @return - the formatted time string
	 */
	public static String formatMillisTo00_00(long timeInMillis) {
		return formatSecondsTo00_00(timeInMillis / 1000L);
	}

	/**
	 * Check if a duration is considered as "Permanent"
	 */
	public static boolean isPermanent(String duration) {
		return duration.equalsIgnoreCase("Permanent") || duration.equalsIgnoreCase("Perm");
	}

	/**
	 * Check if a duration string uses the correct format
	 */
	public static boolean isValidDuration(String duration) {
		duration = duration.toLowerCase();

		if (isPermanent(duration)) {
			return true;
		}

		if (!duration.contains("s") && !duration.contains("m") && !duration.contains("h") && !duration.contains("d") && !duration.contains("w") && !duration.contains("y")) {
			return false;
		}

		try {
			duration = duration.replaceFirst("s", "");
			duration = duration.replaceFirst("m", "");
			duration = duration.replaceFirst("h", "");
			duration = duration.replaceFirst("d", "");
			duration = duration.replaceFirst("w", "");
			duration = duration.replaceFirst("y", "");

			Long.parseLong(duration);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Convert a duration string to seconds
	 * 
	 * @param duration - the duration string
	 * @return - the duration in seconds
	 */
	public static long getSecondsFrom(String duration) {
		duration = duration.toLowerCase();

		if (isPermanent(duration)) {
			return -1L;
		}

		if (duration.contains("s")) {
			long time = Long.parseLong(duration.replace("s", ""));
			return time;
		}

		if (duration.contains("m")) {
			long time = Long.parseLong(duration.replace("m", ""));
			return time * 60;
		}

		if (duration.contains("h")) {
			long time = Long.parseLong(duration.replace("h", ""));
			return time * 60 * 60;
		}

		if (duration.contains("d")) {
			long time = Long.parseLong(duration.replace("d", ""));
			return time * 60 * 60 * 24;
		}

		if (duration.contains("w")) {
			long time = Long.parseLong(duration.replace("w", ""));
			return time * 60 * 60 * 24 * 7;
		}

		if (duration.contains("y")) {
			long time = Long.parseLong(duration.replace("y", ""));
			return time * 60 * 60 * 24 * 365;
		}

		return 0L;
	}

	/**
	 * Convert a duration string to milliseconds
	 * 
	 * @param duration - the duration string
	 * @return - the duration in milliseconds
	 */
	public static long getMillisecondsFrom(String duration) {
		return getSecondsFrom(duration) * 1000L;
	}

	/**
	 * Format seconds to a years, weeks, days, hours, minutes and seconds string
	 * 
	 * @param timeInSeconds - the time in seconds
	 * @return - the full duration string
	 */
	public static String secondsToYWDHMS(long timeInSeconds) {
		long years = timeInSeconds / (86400 * 365);
		long weeks = timeInSeconds / (86400 * 7);
		long days = timeInSeconds / 86400;
		long hours = (timeInSeconds % 86400) / 3600;
		long minutes = (timeInSeconds % 3600) / 60;
		long seconds = (timeInSeconds % 3600) % 60;

		StringBuilder formattedTime = new StringBuilder();

		if (years >= 1) {
			formattedTime.append(years + (years == 1 ? " year" : " years"));
		}

		if (weeks >= 1) {
			if (!formattedTime.toString().isEmpty()) {
				formattedTime.append(", ");
			}

			formattedTime.append(weeks + (weeks == 1 ? " week" : " weeks"));
		}

		if (days >= 1) {
			if (!formattedTime.toString().isEmpty()) {
				formattedTime.append(", ");
			}

			formattedTime.append(days + (days == 1 ? " day" : " days"));
		}

		if (hours >= 1) {
			if (!formattedTime.toString().isEmpty()) {
				formattedTime.append(", ");
			}

			formattedTime.append(hours + (hours == 1 ? " hour" : " hours"));
		}

		if (minutes >= 1) {
			if (!formattedTime.toString().isEmpty()) {
				formattedTime.append(", ");
			}

			formattedTime.append(minutes + (minutes == 1 ? " minute" : " minutes"));
		}

		if (seconds >= 1) {
			if (!formattedTime.toString().isEmpty()) {
				formattedTime.append(", ");
			}

			formattedTime.append(seconds + (seconds == 1 ? " second" : " seconds"));
		}

		if (formattedTime.toString().isEmpty()) {
			if (seconds <= 0) {
				formattedTime.append("0 seconds");
			}
		}

		return formattedTime.toString();
	}

	/**
	 * Format milliseconds to a years, weeks, days, hours, minutes and seconds
	 * string
	 * 
	 * @param timeInMilliseconds - the time in milliseconds
	 * @return - the full duration string
	 */
	public static String millisecondsToYWDHMS(long timeInMilliseconds) {
		return secondsToYWDHMS(timeInMilliseconds / 1000L);
	}

	/**
	 * Add a string time duration to a Date
	 * 
	 * @param date - the date to add time
	 * @param time - the time to add
	 * @return - a new Date with the time added
	 */
	public static Date addToDate(Date date, String time) {
		if (!isValidDuration(time)) {
			return date;
		}

		if (isPermanent(time)) {
			return date;
		}

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.add(Calendar.SECOND, (int) getSecondsFrom(time));

		return calendar.getTime();
	}
}
