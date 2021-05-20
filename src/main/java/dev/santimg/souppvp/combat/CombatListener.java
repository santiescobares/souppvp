package dev.santimg.souppvp.combat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.ChatUtil;

public class CombatListener implements Listener {

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		CombatManager combatManager = SoupPvP.getInstance().getCombatManager();

		// We don't want to put a player in combat if they are in spawn for example
		if (event.isCancelled()) {
			return;
		}

		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getEntity();

		if (event.getDamager() == null) {
			return;
		}

		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();

			if (damager == player) {
				return;
			}

			combatManager.setInCombat(player);
			combatManager.setInCombat(damager);

			return;
		}

		if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();

			if (projectile.getShooter() != null && projectile.getShooter() instanceof Player) {
				Player shooter = (Player) projectile.getShooter();

				// Imagine the player shoots itself
				if (shooter == player) {
					return;
				}

				combatManager.setInCombat(player);
				combatManager.setInCombat(shooter);
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		CombatManager combatManager = SoupPvP.getInstance().getCombatManager();
		Player player = event.getEntity();

		if (!combatManager.isInCombat(player)) {
			return;
		}

		// We only want to drop soups
		event.getDrops().removeIf(item -> item != null && item.getType() != Material.MUSHROOM_SOUP);

		combatManager.remove(player);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		CombatManager combatManager = SoupPvP.getInstance().getCombatManager();
		Player player = event.getPlayer();

		if (!combatManager.isInCombat(player)) {
			return;
		}

		// We make sure that the player will drop their soups if they quit (like a
		// normal death)
		List<ItemStack> drops = Arrays.asList(player.getInventory().getContents()).stream().filter(item -> item != null && item.getType() == Material.MUSHROOM_SOUP)
				.collect(Collectors.toList());

		drops.forEach(drop -> {
			player.getWorld().dropItemNaturally(player.getLocation(), drop);
		});

		// Clear player's inventory to prevent duplicating items
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setHealth(0.0D);

		combatManager.remove(player);
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		CombatManager combatManager = SoupPvP.getInstance().getCombatManager();
		Player player = event.getPlayer();

		if (!combatManager.isInCombat(player)) {
			return;
		}

		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}

		List<String> blockedCommands = SoupPvP.getInstance().getConfig().getStringList("COMBAT_BLOCKED_COMMANDS");
		String command = event.getMessage();

		for (String cmd : blockedCommands) {
			if (command.toLowerCase().startsWith(cmd.toLowerCase())) {
				event.setCancelled(true);
				player.sendMessage(ChatUtil.translate("&cYou can't use this command while in combat!"));
				return;
			}
		}
	}
}
