package com.cjburkey.burkeyscomputers.block;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.burkeyscomputers.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks {
	
	private static final List<Block> blocks = new ArrayList<>();
	
	public static Block blockComputer;
	
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		blockComputer = registerBlock(event, new Block(Material.IRON), "block_computer");
	}
	
	private static Block registerBlock(RegistryEvent.Register<Block> e, Block block, String name) {
		block.setUnlocalizedName(name);
		block.setRegistryName(ModInfo.MOD_ID, name);
		e.getRegistry().register(block);
		blocks.add(block);
		return block;
	}
	
	public static Block[] getRegisteredBlocks() {
		return blocks.toArray(new Block[blocks.size()]);
	}
	
}