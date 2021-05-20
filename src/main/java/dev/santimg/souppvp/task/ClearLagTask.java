package dev.santimg.souppvp.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearLagTask extends BukkitRunnable {

	@Override
	public void run() {
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity instanceof Item) {
					entity.remove();
				}
			}
		}
	}
}
