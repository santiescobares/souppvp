package dev.santimg.souppvp.spawn.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.spawn.SpawnManager;
import dev.santimg.souppvp.spawn.command.listener.SpawnTeleportListener;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class SpawnCommand {

	public SpawnCommand() {
		Bukkit.getPluginManager().registerEvents(new SpawnTeleportListener(), SoupPvP.getInstance());
	}

	@Command(name = "spawn", permission = "souppvp.command.spawn", playersOnly = true)
	public void execute(CommandArgs cmd) {
		SpawnManager spawnManager = SoupPvP.getInstance().getSpawnManager();
		Player player = cmd.getPlayer();

		if (spawnManager.getSpawn() == null) {
			player.sendMessage(ChatUtil.translate("&cThe spawn is not defined yet!"));
			return;
		}

		// We will only teleport the player instantly if they are in Creative mode
		if (player.getGameMode() == GameMode.CREATIVE) {
			spawnManager.teleportToSpawn(player);

			player.sendMessage(ChatUtil.translate("&aTeleported to the spawn..."));
			return;
		}

		// If the player is not in Creative mode we will teleport them after 3 seconds
		player.setMetadata("Teleporting", new FixedMetadataValue(SoupPvP.getInstance(), true));
		player.sendMessage(ChatUtil.translate("You will be &ateleported &fto the Spawn in &b3 &fseconds... Stay still and don't take damage."));

		new BukkitRunnable() {
			int i = 3;

			@Override
			public void run() {
				if (!player.isOnline() || !player.hasMetadata("Teleporting")) {
					this.cancel();
					return;
				}

				if (i == 0) {
					this.cancel();

					// First we remove the metadata to prevent sending a cancel message from the
					// PlayerTeleportEvent
					player.removeMetadata("Teleporting", SoupPvP.getInstance());

					spawnManager.teleportToSpawn(player);

					player.sendMessage(ChatUtil.translate("&aTeleported to the spawn..."));
					return;
				}

				player.sendMessage(ChatUtil.translate("&b" + i + "..."));

				i--;
			}

		}.runTaskTimer(SoupPvP.getInstance(), 20L, 20L);
	}
}
