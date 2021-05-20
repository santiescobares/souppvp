package dev.santimg.souppvp.spawn;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.TaskUtil;

public class SpawnListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		// We will only teleport the player when join if they didn't join before
		if (player.hasPlayedBefore()) {
			return;
		}

		// We have to delay the teleport at least 1 tick later after join
		TaskUtil.runLater(() -> SoupPvP.getInstance().getSpawnManager().teleportToSpawn(player), 1L);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		// We have to delay the teleport at least 1 tick later after respawn
		TaskUtil.runLater(() -> SoupPvP.getInstance().getSpawnManager().teleportToSpawn(event.getPlayer()), 1L);
	}
}
