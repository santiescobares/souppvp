package dev.santimg.souppvp.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class PreventionListener implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.getPlayer().hasMetadata("BuildMode")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().hasMetadata("BuildMode")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		if (event.getPlayer().hasMetadata("BuildMode")) {
			return;
		}

		Material type = event.getBlock().getType();

		if (type == Material.WHEAT || type == Material.CARROT || type == Material.SEEDS || type == Material.PUMPKIN_SEEDS || type == Material.MELON_SEEDS
				|| type == Material.POTATO) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockFade(BlockFadeEvent event) {
		event.setCancelled(true);
	}
}
