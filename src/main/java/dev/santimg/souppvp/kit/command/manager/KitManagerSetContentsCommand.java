package dev.santimg.souppvp.kit.command.manager;

import org.bukkit.entity.Player;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.kit.Kit;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.TaskUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class KitManagerSetContentsCommand extends KitManagerCommand {

	@Command(name = "kitManager.setContents", permission = "souppvp.command.kitmanager", playersOnly = true)
	public void execute(CommandArgs cmd) {
		KitManager kitManager = SoupPvP.getInstance().getKitManager();

		String[] args = cmd.getArgs();
		Player player = cmd.getPlayer();

		if (args.length >= 1) {
			Kit kit = kitManager.get(args[0]);

			if (kit == null) {
				player.sendMessage(ChatUtil.translate("&cKit \"" + args[0] + "\" not found!"));
				return;
			}

			player.updateInventory();

			// We will delay the contents set by 2 ticks to prevent a bug that set all
			// items' amount in your inventory to 0
			TaskUtil.runLater(() -> {
				kit.setContents(player.getInventory().getContents());
				kit.setArmorContents(player.getInventory().getArmorContents());

				player.sendMessage(ChatUtil.translate("&b" + kit.getName() + "&f's contents &aupdated&f!"));
			}, 2L);
		} else {
			player.sendMessage(ChatUtil.translate("&cUsage: /kitManager setContents <kitName>"));
		}
	}
}
