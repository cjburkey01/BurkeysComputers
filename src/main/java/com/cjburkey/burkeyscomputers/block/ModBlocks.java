package com.cjburkey.burkeyscomputers.block;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.burkeyscomputers.ModInfo;
import com.cjburkey.burkeyscomputers.ModLog;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks {
	
	private static final List<Block> blocks = new ArrayList<>();
	
	public static BlockComputer blockComputer;
	
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModLog.info("Registering blocks.");
		blockComputer = (BlockComputer) registerBlock(event, new BlockComputer(), "block_computer");
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