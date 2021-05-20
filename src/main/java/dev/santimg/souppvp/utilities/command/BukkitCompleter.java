package dev.santimg.souppvp.utilities.command;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;

import com.google.common.collect.Maps;

import org.bukkit.command.*;

/**
 * Command Framework - BukkitCompleter <br>
 * An implementation of the TabCompleter class allowing for multiple tab
 * completers per command
 * 
 * @author minnymin3
 * 
 */
public class BukkitCompleter implements TabCompleter {

	private Map<String, Entry<Method, Object>> completers = Maps.newHashMap();

	public void addCompleter(String label, Method m, Object obj) {
		completers.put(label, new AbstractMap.SimpleEntry<Method, Object>(m, obj));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		for (int i = args.length; i >= 0; i--) {
			StringBuilder builder = new StringBuilder();

			builder.append(label.toLowerCase());

			for (int x = 0; x < i; x++) {
				if (!args[x].equals("") && !args[x].equals(" ")) {
					builder.append("." + args[x].toLowerCase());
				}
			}

			String cmdLabel = builder.toString();

			if (completers.containsKey(cmdLabel)) {
				Entry<Method, Object> entry = completers.get(cmdLabel);

				try {
					return (List<String>) entry.getKey().invoke(entry.getValue(), new CommandArgs(sender, command, label, args, cmdLabel.split("\\.").length - 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
