package dev.santimg.souppvp.combat;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.TaskUtil;
import lombok.Getter;

public class CombatManager {

	@Getter
	private final Map<UUID, Long> combatTagMap;

	private final long COMBAT_TIME_MILLIS = 30L * 1000L; // 30 seconds

	public CombatManager() {
		// We will use a ConcurrentHashMap for this situation to remove players from
		// it once their combat tag expires
		this.combatTagMap = Maps.newConcurrentMap();

		Bukkit.getPluginManager().registerEvents(new CombatListener(), SoupPvP.getInstance());

		TaskUtil.runAsyncTimer(() -> {
			for (Map.Entry<UUID, Long> combatTagEntry : this.combatTagMap.entrySet()) {
				UUID uuid = combatTagEntry.getKey();
				long timeleft = combatTagEntry.getValue();

				if (System.currentTimeMillis() > timeleft) {
					this.combatTagMap.remove(uuid);

					Player player = Bukkit.getPlayer(uuid);

					// If the player is online we will notify them
					if (player != null) {
						player.sendMessage(ChatUtil.translate("&aYour combat tag has expired!"));
					}
				}
			}
		}, 20L, 20L);
	}

	/**
	 * Set a player in combat tag
	 * 
	 * @param player - the player
	 */
	public void setInCombat(Player player) {
		// We will only send the message to the player if they weren't in combat before
		if (!this.combatTagMap.containsKey(player.getUniqueId())) {
			player.sendMessage(ChatUtil.translate("You are now in &ccombat &ffor &b" + (this.COMBAT_TIME_MILLIS / 1000L) + " &fseconds!"));
		}

		this.combatTagMap.put(player.getUniqueId(), System.currentTimeMillis() + this.COMBAT_TIME_MILLIS);
	}

	/**
	 * Remove a player from combat tag
	 * 
	 * @param player - the player
	 */
	public void remove(Player player) {
		this.combatTagMap.remove(player.getUniqueId());
	}

	/**
	 * Get a player's combat tag time left in milliseconds
	 * 
	 * @param player - the player
	 * @return - the time left
	 */
	public long getMillisLeft(Player player) {
		long timeleft = this.combatTagMap.getOrDefault(player.getUniqueId(), 0L);

		return timeleft == 0L ? 0L : timeleft - System.currentTimeMillis();
	}

	/**
	 * Check if a player is combat tagged
	 * 
	 * @param player - the player
	 * @return - is combat tagged value
	 */
	public boolean isInCombat(Player player) {
		return this.combatTagMap.containsKey(player.getUniqueId());
	}
}
