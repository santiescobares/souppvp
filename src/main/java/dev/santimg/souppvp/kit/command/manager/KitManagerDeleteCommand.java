package dev.santimg.souppvp.kit.command.manager;

import org.bukkit.command.CommandSender;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.kit.Kit;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class KitManagerDeleteCommand extends KitManagerCommand {

	@Command(name = "kitManager.delete", permission = "souppvp.command.kitmanager")
	public void execute(CommandArgs cmd) {
		KitManager kitManager = SoupPvP.getInstance().getKitManager();

		String[] args = cmd.getArgs();
		CommandSender sender = cmd.getSender();

		if (args.length >= 1) {
			Kit kit = kitManager.get(args[0]);

			if (kit == null) {
				sender.sendMessage(ChatUtil.translate("&cKit \"" + args[0] + "\" not found!"));
				return;
			}

			kitManager.getKits().remove(kit);

			sender.sendMessage(ChatUtil.translate("Kit &b" + kit.getName() + " &cdeleted&f!"));
		} else {
			sender.sendMessage(ChatUtil.translate("&cUsage: /kitManager delete <kitName>"));
		}
	}
}
