package dev.santimg.souppvp.playerdata;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.PlayerUtil;
import dev.santimg.souppvp.utilities.TaskUtil;

public class PlayerDataListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST) // Highest runs first of all
	public void onPlayerLogin(PlayerLoginEvent event) {
		PlayerDataManager playerDataManager = SoupPvP.getInstance().getPlayerDataManager();
		UUID uuid = event.getPlayer().getUniqueId();

		// We prevent players loading their data while the server is whitelisted, yes,
		// this happens although they are kicked
		if (!this.canJoin(uuid)) {
			return;
		}

		TaskUtil.runAsync(() -> {
			PlayerData playerData = playerDataManager.getRepository().load(uuid);

			playerDataManager.getLoadedDataMap().put(uuid, playerData);
		});
	}

	@EventHandler(priority = EventPriority.MONITOR) // Monitor runs last of all
	public void onPlayerQuit(PlayerQuitEvent event) {
		PlayerDataManager playerDataManager = SoupPvP.getInstance().getPlayerDataManager();
		UUID uuid = event.getPlayer().getUniqueId();

		PlayerData playerData = playerDataManager.get(uuid);

		// This shouldn't happens, but anyways...
		if (playerData == null) {
			return;
		}

		TaskUtil.runAsync(() -> {
			playerDataManager.getRepository().save(playerData);
			playerDataManager.getLoadedDataMap().remove(uuid);
		});
	}

	private boolean canJoin(UUID uuid) {
		if (!Bukkit.hasWhitelist()) {
			return true;
		}

		OfflinePlayer player = PlayerUtil.getOfflinePlayer(uuid);

		return Bukkit.getWhitelistedPlayers().contains(player) || player.isOp();
	}
}
