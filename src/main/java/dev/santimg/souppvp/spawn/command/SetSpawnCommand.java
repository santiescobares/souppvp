package dev.santimg.souppvp.spawn.command;

import org.bukkit.entity.Player;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.TaskUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class SetSpawnCommand {

	@Command(name = "setSpawn", permission = "souppvp.command.setspawn", playersOnly = true)
	public void execute(CommandArgs cmd) {
		Player player = cmd.getPlayer();

		// We will run this command asynchronous to prevent the server lagging on file
		// saving
		TaskUtil.runAsync(() -> {
			SoupPvP.getInstance().getSpawnManager().saveSpawn(player.getLocation().clone());

			player.sendMessage(ChatUtil.translate("&aSpawn set!"));
		});
	}
}
