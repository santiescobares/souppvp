package dev.santimg.souppvp.kit.command.manager;

import org.bukkit.command.CommandSender;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.kit.Kit;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class KitManagerSetDefaultCommand extends KitManagerCommand {

	@Command(name = "kitManager.setDefault", permission = "souppvp.command.kitmanager")
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

			// !kit.isDefault() is the opposite of kit.isDefault()
			kit.setDefault(!kit.isDefault());

			sender.sendMessage(ChatUtil.translate("&b" + kit.getName() + " &fis " + (kit.isDefault() ? "&anow" : "&cno longer") + " &fset as default kit!"));
		} else {
			sender.sendMessage(ChatUtil.translate("&cUsage: /kitManager setDefault <kitName>"));
		}
	}
}
