package dev.santimg.souppvp.kit.command.manager;

import org.bukkit.command.CommandSender;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.kit.Kit;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class KitManagerCreateCommand extends KitManagerCommand {

	@Command(name = "kitManager.create", permission = "souppvp.command.kitmanager")
	public void execute(CommandArgs cmd) {
		KitManager kitManager = SoupPvP.getInstance().getKitManager();

		String[] args = cmd.getArgs();
		CommandSender sender = cmd.getSender();

		if (args.length >= 1) {
			Kit kit = kitManager.get(args[0]);

			if (kit != null) {
				sender.sendMessage(ChatUtil.translate("&cThis kit already exists!"));
				return;
			}

			kit = new Kit(args[0]);

			kitManager.getKits().add(kit);

			sender.sendMessage(ChatUtil.translate("Kit &b" + args[0] + " &acreated&f!"));
		} else {
			sender.sendMessage(ChatUtil.translate("&cUsage: /kitManager create <kitName>"));
		}
	}
}
