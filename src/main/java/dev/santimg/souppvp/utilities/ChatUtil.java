package dev.santimg.souppvp.utilities;

import java.util.*;

import org.bukkit.*;

import com.google.common.collect.Lists;

import lombok.experimental.UtilityClass;

/**
 * Chat utilities
 * 
 * @author SantiMG
 */
@UtilityClass
public final class ChatUtil {

	public static final String LINE;
	public static final String SHORT_LINE;
	public static final String MENU_LINE;
	public static final String SB_LINE;

	static {
		LINE = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "-----------------------------------------------------";
		SHORT_LINE = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "---------------------------------------";
		MENU_LINE = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------------";
		SB_LINE = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "---------------------";
	}

	/**
	 * Translate a text using color codes
	 * 
	 * @param text - the text string
	 * @return - the translated text
	 */
	public static String translate(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	/**
	 * Translate a text list using color codes
	 * 
	 * @param text - the text string list
	 * @return - the translated text list
	 */
	public static List<String> translate(List<String> text) {
		List<String> toReturn = Lists.newArrayList();

		text.forEach(line -> {
			toReturn.add(translate(line));
		});

		return toReturn;
	}

	/**
	 * Broadcast a colored message in the server
	 * 
	 * @param message - the message
	 */
	public static void broadcast(String message) {
		Bukkit.broadcastMessage(translate(message));
	}

	/**
	 * Broadcast a colored message list in the server
	 * 
	 * @param message - the message list
	 */
	public static void broadcast(List<String> message) {
		message.forEach(line -> {
			broadcast(line);
		});
	}

	/**
	 * Broadcast multiple colored messages in the server
	 * 
	 * @param message - the messages
	 */
	public static void broadcast(String... message) {
		broadcast(Arrays.asList(message));
	}

	/**
	 * Print a colored message in the console
	 * 
	 * @param text - the text
	 */
	public static void log(String text) {
		Bukkit.getConsoleSender().sendMessage(translate(text));
	}

	/**
	 * Build a text by arguments
	 * 
	 * @param arguments - the arguments
	 * @param separator - the separator between arguments
	 * @return - the built text
	 */
	public static String buildText(List<String> arguments, String separator) {
		StringBuilder text = new StringBuilder();
		Iterator<String> argumentsIterator = arguments.iterator();

		while (argumentsIterator.hasNext()) {
			text.append(argumentsIterator.next());

			if (argumentsIterator.hasNext()) {
				text.append(separator);
			}
		}

		return text.toString();
	}

	/**
	 * Build a text by arguments
	 * 
	 * @param arguments - the arguments
	 * @return - the built text
	 */
	public static String buildText(List<String> arguments) {
		return buildText(arguments, " ");
	}

	/**
	 * Build a text by arguments
	 * 
	 * @param arguments - the arguments
	 * @param separator - the separator between arguments
	 * @return - the built text
	 */
	public static String buildText(String[] arguments, String separator) {
		return buildText(Arrays.asList(arguments), separator);
	}

	/**
	 * Build a text by arguments
	 * 
	 * @param arguments - the arguments
	 * @return - the built text
	 */
	public static String buildText(String[] arguments) {
		return buildText(arguments, " ");
	}
}
