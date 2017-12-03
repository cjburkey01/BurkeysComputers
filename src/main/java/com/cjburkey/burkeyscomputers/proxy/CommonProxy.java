package com.cjburkey.burkeyscomputers.proxy;

import com.cjburkey.burkeyscomputers.BurkeysComputers;
import com.cjburkey.burkeyscomputers.event.WorldLoaded;
import com.cjburkey.burkeyscomputers.gui.ModGuiHandler;
import com.cjburkey.burkeyscomputers.packet.ModPacketHandler;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	
	public void construct(FMLConstructionEvent e) {
		
	}
	
	public void preinit(FMLPreInitializationEvent e) {
		ModPacketHandler.initNetwork();
		MinecraftForge.EVENT_BUS.register(new WorldLoaded());
	}
	
	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(BurkeysComputers.instance, new ModGuiHandler());
		TileEntity.register("tile_computer", TileEntityComputer.class);
	}
	
	public void postinit(FMLPostInitializationEvent e) {
		
	}
	
}