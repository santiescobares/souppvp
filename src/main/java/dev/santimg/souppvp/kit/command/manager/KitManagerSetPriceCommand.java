package dev.santimg.souppvp.kit.command.manager;

import org.bukkit.command.CommandSender;

import dev.santimg.souppvp.SoupPvP;
import dev.santimg.souppvp.kit.Kit;
import dev.santimg.souppvp.kit.KitManager;
import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.command.Command;
import dev.santimg.souppvp.utilities.command.CommandArgs;

public class KitManagerSetPriceCommand extends KitManagerCommand {

	@Command(name = "kitManager.setPrice", permission = "souppvp.command.kitmanager")
	public void execute(CommandArgs cmd) {
		KitManager kitManager = SoupPvP.getInstance().getKitManager();

		String[] args = cmd.getArgs();
		CommandSender sender = cmd.getSender();

		if (args.length >= 2) {
			Kit kit = kitManager.get(args[0]);
			long price;

			if (kit == null) {
				sender.sendMessage(ChatUtil.translate("&cKit \"" + args[0] + "\" not found!"));
				return;
			}

			try {
				price = Long.parseLong(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatUtil.translate("&cYou must enter a valid number!"));
				return;
			}

			// We don't want negative prices
			if (price < 0L) {
				sender.sendMessage(ChatUtil.translate("&cPrice can't be lower than 0!"));
				return;
			}

			kit.setPrice(price);

			sender.sendMessage(ChatUtil.translate("&b" + kit.getName() + "&f's price &aset &fto &b" + price + "&f!"));
		} else {
			sender.sendMessage(ChatUtil.translate("&cUsage: /kitManager setPrice <kitName> <price>"));
		}
	}
}
