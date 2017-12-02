package com.cjburkey.burkeyscomputers.item;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.burkeyscomputers.ModInfo;
import com.cjburkey.burkeyscomputers.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;

public class ModItems {
	
	private static final List<Item> items = new ArrayList<>();
	
	public static void registerItems(RegistryEvent.Register<Item> event) {
		registerBlockItems(event);
	}
	
	private static void registerBlockItems(RegistryEvent.Register<Item> e) {
		for (Block block : ModBlocks.getRegisteredBlocks()) {
			registerItem(e, new ItemBlock(block), block.getRegistryName().getResourcePath());
		}
	}
	
	private static Item registerItem(RegistryEvent.Register<Item> e, Item item, String name) {
		item.setUnlocalizedName(name);
		item.setRegistryName(ModInfo.MOD_ID, name);
		e.getRegistry().register(item);
		items.add(item);
		return item;
	}
	
	public static Item[] getRegisteredItems() {
		return items.toArray(new Item[items.size()]);
	}
	
}