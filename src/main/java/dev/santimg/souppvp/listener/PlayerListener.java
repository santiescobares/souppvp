package dev.santimg.souppvp.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Objects;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.ChatUtil;

public class PlayerListener implements Listener {

	private final double SOUP_HEAL_AMOUNT = 3.5D * 2.0D; // In hearts amount

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		event.setJoinMessage(null);

		SoupPvP.getInstance().getConfig().getStringList("JOIN_MOTD").forEach(line -> {
			line = line.replaceAll("<player>", player.getName());

			player.sendMessage(ChatUtil.translate(line));
		});
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (!event.getAction().name().contains("RIGHT_CLICK")) {
			return;
		}

		if (Objects.equal(event.getMaterial(), Material.MUSHROOM_SOUP)) {
			if (player.getHealth() == player.getMaxHealth()) {
				return;
			}

			event.setCancelled(true);
			
			double newHealth = player.getHealth() + this.SOUP_HEAL_AMOUNT;

			player.setHealth(Math.min(newHealth, player.getMaxHealth()));
			player.setItemInHand(new ItemStack(Material.BOWL));
			player.updateInventory();
		}
	}
}
