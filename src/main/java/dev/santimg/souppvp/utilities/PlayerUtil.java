package dev.santimg.souppvp.utilities;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import lombok.*;
import lombok.experimental.UtilityClass;

/**
 * Player utilities
 * 
 * @author SantiMG
 */
@UtilityClass
public final class PlayerUtil {

	@Getter
	@AllArgsConstructor
	public static enum Direction {
		SOUTH("South", "S"), NORTH("North", "N"), WEST("West", "W"), EAST("East", "E"), SOUTH_WEST("South West", "SW"), SOUTH_EAST("South East", "SE"),
		NORTH_WEST("North West", "NW"), NORTH_EAST("North East", "NE");

		private final String name;
		private final String shortName;

		public Direction opposite() {
			switch (this) {
			case SOUTH:
				return NORTH;
			case NORTH:
				return SOUTH;
			case WEST:
				return EAST;
			case EAST:
				return WEST;
			case SOUTH_WEST:
				return NORTH_WEST;
			case SOUTH_EAST:
				return NORTH_EAST;
			case NORTH_WEST:
				return SOUTH_WEST;
			case NORTH_EAST:
				return SOUTH_EAST;
			default:
				return this;
			}
		}
	}

	/**
	 * Get a player's ping
	 */
	public static int getPing(Player player) {
		if (player == null) {
			return -1;
		}

		try {
			String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
			Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(player);

			return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Color a ping amount
	 */
	public static String colorPing(int ping) {
		return ping < 100 ? ChatColor.GREEN.toString() : ping >= 100 && ping < 200 ? ChatColor.YELLOW.toString() : ChatColor.RED.toString();
	}

	/**
	 * Color a player's ping amount
	 */
	public static String colorPing(Player player) {
		return colorPing(getPing(player));
	}

	/**
	 * Reset a player's inventory and armor contents
	 * 
	 * @param player - the player
	 */
	public static void resetInventory(Player player) {
		if (player == null) {
			return;
		}

		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.updateInventory();
	}

	/**
	 * Reset a player's inventory
	 * 
	 * @param player - the player
	 */
	public static void resetInventoryOnly(Player player) {
		if (player == null) {
			return;
		}

		player.getInventory().clear();
		player.updateInventory();
	}

	/**
	 * Reset a player (inventory, health, food, etc) with default values
	 * 
	 * @param player - the player
	 */
	public static void reset(Player player) {
		if (player == null) {
			return;
		}

		resetInventory(player);

		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setExp(0.0F);
		player.setExhaustion(-5.0F);
		player.setFireTicks(0);
	}

	/**
	 * Reset a player (inventory, health, food, etc) with specific values
	 * 
	 * @param player     - the player
	 * @param gameMode   - new game mode
	 * @param disableFly - disable player's allow flight
	 */
	public static void reset(Player player, GameMode gameMode, boolean disableFly) {
		if (player == null) {
			return;
		}

		resetInventory(player);

		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
		player.setGameMode(gameMode);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setExp(0.0F);
		player.setExhaustion(-5.0F);
		player.setFireTicks(0);

		if (disableFly) {
			player.setAllowFlight(false);
			player.setFlying(false);
		}
	}

	/**
	 * Count the empty slots on a player's inventory
	 * 
	 * @param player - the player
	 * @return - empty slots count
	 */
	public static int countEmptySlots(Player player) {
		if (player == null) {
			return -1;
		}

		int emptySlots = 0;

		for (ItemStack item : player.getInventory().getArmorContents()) {
			if (item == null || item.getType() == Material.AIR) {
				emptySlots++;
			}
		}

		return emptySlots;
	}

	/**
	 * Check if a player has a specific amount of empty slots
	 */
	public static boolean hasEmptySlots(Player player, int slots) {
		return countEmptySlots(player) >= slots;
	}

	/**
	 * Get the distance between two locations
	 * 
	 * @param loc1 - the first location
	 * @param loc2 - the second location
	 * @return - the distance in block count between the locations
	 */
	public static int getDistance(Location loc1, Location loc2) {
		if (!loc1.getWorld().getName().equals(loc2.getWorld().getName())) {
			return -1;
		}

		int distance = 0;

		int playerX = loc1.getBlockX();
		int targetX = loc2.getBlockX();

		distance += Math.max(playerX, targetX) - Math.min(playerX, targetX);

		int playerZ = loc1.getBlockZ();
		int targetZ = loc2.getBlockZ();

		distance += Math.max(playerZ, targetZ) - Math.min(playerZ, targetZ);

		return distance;
	}

	/**
	 * Get a List with all offline players
	 */
	public static List<OfflinePlayer> getOfflinePlayers() {
		return Arrays.asList(Bukkit.getOfflinePlayers());
	}

	/**
	 * Get a List with all online players
	 */
	public static List<Player> getOnlinePlayers() {
		return Lists.newArrayList(Bukkit.getOnlinePlayers());
	}

	/**
	 * Get an OfflinePlayer by their name (insensitive case)
	 */
	@SuppressWarnings("deprecation")
	public static OfflinePlayer getOfflinePlayer(String playerName) {
		for (OfflinePlayer player : getOfflinePlayers()) {
			if (player.getName().equalsIgnoreCase(playerName)) {
				return player;
			}
		}

		return Bukkit.getOfflinePlayer(playerName);
	}

	/**
	 * Get an OfflinePlayer by their uuid
	 */
	public static OfflinePlayer getOfflinePlayer(UUID uuid) {
		if (uuid == null) {
			return null;
		}

		return Bukkit.getOfflinePlayer(uuid);
	}

	/**
	 * Get a player's uuid by their name (insensitive case)
	 * 
	 * @param playerName - the player's name
	 * @return - the player's uuid
	 */
	public static UUID uuid(String playerName) {
		if (Bukkit.getPlayer(playerName) != null) {
			return Bukkit.getPlayer(playerName).getUniqueId();
		}

		return getOfflinePlayer(playerName).getUniqueId();
	}

	/**
	 * Get a player's name by their uuid
	 * 
	 * @param uuid - the player's uuid
	 * @return - the player's name
	 */
	public static String name(UUID uuid) {
		if (uuid == null) {
			return "Unknown";
		}

		if (Bukkit.getPlayer(uuid) != null) {
			return Bukkit.getPlayer(uuid).getName();
		}

		String name = getOfflinePlayer(uuid).getName();
		return name == null ? "Unknown" : name;
	}

	/**
	 * Teleport a player to a specific location
	 * 
	 * @param player   - the player
	 * @param location - the location
	 * @return - true if the teleport was successful or false if not
	 */
	public static boolean teleport(Player player, Location location) {
		if (player == null || location == null) {
			return false;
		}

		return player.teleport(location);
	}

	/**
	 * Get the cardinal direction of a yaw
	 */
	public static Direction getDirection(float yaw) {
		double rotation = (yaw - 90.0F) % 360.0F;

		if (rotation < 0) {
			rotation += 360.0F;
		}

		if (0 <= rotation && rotation < 22.5) {
			return Direction.NORTH;
		}

		if (22.5 <= rotation && rotation < 67.5) {
			return Direction.NORTH_EAST;
		}

		if (67.5 <= rotation && rotation < 112.5) {
			return Direction.EAST;
		}

		if (112.5 <= rotation && rotation < 157.5) {
			return Direction.SOUTH_EAST;
		}

		if (157.5 <= rotation && rotation < 202.5) {
			return Direction.SOUTH;
		}

		if (202.5 <= rotation && rotation < 247.5) {
			return Direction.SOUTH_WEST;
		}

		if (247.5 <= rotation && rotation < 292.5) {
			return Direction.WEST;
		}

		if (292.5 <= rotation && rotation < 337.5) {
			return Direction.NORTH_WEST;
		}

		if (337.5 <= rotation && rotation <= 360) {
			return Direction.NORTH;
		}

		return Direction.SOUTH;
	}

	/**
	 * Get the cardinal direction of a player
	 */
	public static Direction getDirection(Player player) {
		if (player == null) {
			return Direction.SOUTH;
		}

		return getDirection(player.getLocation().getYaw());
	}
}
