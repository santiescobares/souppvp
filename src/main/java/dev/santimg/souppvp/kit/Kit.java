package dev.santimg.souppvp.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

import dev.santimg.souppvp.utilities.ChatUtil;
import dev.santimg.souppvp.utilities.PlayerUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kit {

	private final String name;

	private ItemStack[] contents;
	private ItemStack[] armorContents;
	private long price;

	private boolean Default = false;

	public Kit(String name) {
		Preconditions.checkNotNull(name, "Name can't be null.");

		this.name = name;

		this.contents = new ItemStack[36];
		this.armorContents = new ItemStack[4];
		this.price = 0L;
	}

	/**
	 * Apply the kit to a player
	 * 
	 * @param player - the player
	 * @param notify - should or shouldn't send a message to the player
	 */
	public void apply(Player player, boolean notify) {
		PlayerUtil.resetInventory(player);

		player.getInventory().setContents(this.contents);
		player.getInventory().setArmorContents(this.armorContents);
		player.updateInventory();

		if (notify) {
			player.sendMessage(ChatUtil.translate("You have &aloaded &fthe &b" + this.name + " &fkit!"));
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Kit)) {
			return false;
		}

		return ((Kit) obj).getName().equals(this.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
