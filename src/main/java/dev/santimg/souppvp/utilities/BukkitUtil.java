package dev.santimg.souppvp.utilities;

import java.io.*;

import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.*;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import lombok.experimental.UtilityClass;

/**
 * Bukkit utilities
 * 
 * @author SantiMG
 */
@UtilityClass
public final class BukkitUtil {

	/**
	 * Serialize a bukkit location into a string
	 * 
	 * @param location - the location
	 * @return - the serialized location string
	 */
	public static String serializeLocation(Location location) {
		if (location == null) {
			return "";
		}

		StringBuilder serializedData = new StringBuilder();

		serializedData.append(location.getWorld().getName()).append(", ");
		serializedData.append(location.getX()).append(", ");
		serializedData.append(location.getY()).append(", ");
		serializedData.append(location.getZ()).append(", ");
		serializedData.append(location.getYaw()).append(", ");
		serializedData.append(location.getPitch());

		return serializedData.toString();
	}

	/**
	 * Deserialize a string into a bukkit location
	 * 
	 * @param data - the serialized string
	 * @return - the bukkit location
	 */
	public static Location deserializeLocation(String data) {
		String[] splittedData = data.split(", ");

		if (splittedData.length < 6) {
			return null;
		}

		try {
			World world = Bukkit.getWorld(splittedData[0]);
			double x = Double.parseDouble(splittedData[1]);
			double y = Double.parseDouble(splittedData[2]);
			double z = Double.parseDouble(splittedData[3]);
			float yaw = Float.parseFloat(splittedData[4]);
			float pitch = Float.parseFloat(splittedData[5]);

			return new Location(world, x, y, z, yaw, pitch);
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * Serialize a bukkit item stack into a string
	 * 
	 * @param item - the item stack
	 * @return - the serialized item stack string in Base64
	 */
	public static String serializeItemStack(ItemStack item) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			dataOutput.writeObject(item);
			dataOutput.close();

			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Deserialize a Base64 string into a bukkit item stack
	 * 
	 * @param data - the serialized string
	 * @return - the bukkit item stack
	 */
	public static ItemStack deserializeItemStack(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack item = (ItemStack) dataInput.readObject();

			dataInput.close();

			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Serialize a bukkit item stack array into a String
	 * 
	 * @param items - the item stack array
	 * @return - the serialized item stack array string in Base64
	 */
	public static String serializeItemStackArray(ItemStack[] items) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			dataOutput.writeInt(items.length);

			for (int i = 0; i < items.length; i++) {
				dataOutput.writeObject(items[i]);
			}

			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Deserialize a Base64 string into a bukkit item stack array
	 * 
	 * @param data - the serialized string
	 * @return - the bukkit item stack array
	 */
	public static ItemStack[] deserializeItemStackArray(String data) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack[] items = new ItemStack[dataInput.readInt()];

			for (int i = 0; i < items.length; i++) {
				items[i] = (ItemStack) dataInput.readObject();
			}

			dataInput.close();
			return items;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ItemStack[0];
	}

	/**
	 * Get the NMS version
	 * 
	 * @return - the version string
	 */
	public static String getServerVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}
}
