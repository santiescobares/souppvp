package dev.santimg.souppvp.utilities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.santimg.souppvp.SoupPvP;
import lombok.experimental.UtilityClass;

/**
 * Bukkit task utilities
 * 
 * @author SantiMG
 */
@UtilityClass
public final class TaskUtil {

	private static final JavaPlugin plugin;

	static {
		plugin = SoupPvP.getInstance();
	}

	/**
	 * Run a synchronized task
	 * 
	 * @param task - the task
	 */
	public static void run(Runnable task) {
		Bukkit.getScheduler().runTask(plugin, task);
	}

	/**
	 * Run an non-synchronized task
	 * 
	 * @param task - the task
	 */
	public static void runAsync(Runnable task) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
	}

	/**
	 * Run a delayed synchronized task
	 * 
	 * @param task  - the task
	 * @param delay - the delay amount
	 */
	public static void runLater(Runnable task, long delay) {
		Bukkit.getScheduler().runTaskLater(plugin, task, delay);
	}

	/**
	 * Run a delayed non-synchronized task
	 * 
	 * @param task  - the task
	 * @param delay - the delay amount
	 */
	public static void runAsyncLater(Runnable task, long delay) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
	}

	/**
	 * Run a repeating synchronized task
	 * 
	 * @param task   - the task
	 * @param delay  - the delay amount
	 * @param period - the period
	 */
	public static void runTimer(Runnable task, long delay, long period) {
		Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
	}

	/**
	 * Run a repeating non-synchronized task
	 * 
	 * @param task   - the task
	 * @param delay  - the delay amount
	 * @param period - the period
	 */
	public static void runAsyncTimer(Runnable task, long delay, long period) {
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
	}
}
