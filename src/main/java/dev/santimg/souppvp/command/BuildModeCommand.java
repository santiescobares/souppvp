package dev.santimg.souppvp.command;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class BuildModeCommand {

	@Command(name = "buildMode", aliases = { "build" }, permission = "souppvp.command.buildmode", playersOnly = true)
	public void execute(CommandArgs cmd) {
		Player player = cmd.getPlayer();

		if (!player.hasMetadata("BuildMode")) {
			player.setMetadata("BuildMode", new FixedMetadataValue(SoupPvP.getInstance(), true));
			player.sendMessage(ChatUtil.translate("&aBuild Mode enabled!"));
		} else {
			player.removeMetadata("BuildMode", SoupPvP.getInstance());
			player.sendMessage(ChatUtil.translate("&cBuild Mode disabled!"));
		}
	}
}
