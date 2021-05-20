package dev.santimg.souppvp.spawn.command.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.ChatUtil;

public class SpawnTeleportListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.getPlayer().removeMetadata("Teleporting", SoupPvP.getInstance());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) {
			return;
		}

		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getEntity();

		if (!player.hasMetadata("Teleporting")) {
			return;
		}

		player.removeMetadata("Teleporting", SoupPvP.getInstance());
		player.sendMessage(ChatUtil.translate("&cThe teleport was cancelled because you were damaged!"));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		this.onEntityDamage(event);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (!player.hasMetadata("Teleporting")) {
			return;
		}

		Location from = event.getFrom();
		Location to = event.getTo();

		if (to.getBlockX() != from.getBlockX() || to.getBlockZ() != from.getBlockZ() || (to.getBlockY() >= from.getBlockY() + 1 || to.getBlockY() <= from.getBlockY() - 1)) {
			player.removeMetadata("Teleporting", SoupPvP.getInstance());
			player.sendMessage(ChatUtil.translate("&cThe teleport was cancelled because you moved!"));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		this.onPlayerMove(event);
	}
}
