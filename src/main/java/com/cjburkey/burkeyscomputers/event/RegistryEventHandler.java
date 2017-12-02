package com.cjburkey.burkeyscomputers.event;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryEventHandler {
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> e) {
		
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> e) {
		
	}
	
	@SubscribeEvent
	public void registerRenders(ModelRegistryEvent e) {
		
	}
	
}