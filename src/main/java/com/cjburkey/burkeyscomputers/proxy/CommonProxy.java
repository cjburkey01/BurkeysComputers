package com.cjburkey.burkeyscomputers.proxy;

import com.cjburkey.burkeyscomputers.BurkeysComputers;
import com.cjburkey.burkeyscomputers.gui.ModGuiHandler;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	
	public void construct(FMLConstructionEvent e) {
		
	}
	
	public void preinit(FMLPreInitializationEvent e) {
		
	}
	
	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(BurkeysComputers.instance, new ModGuiHandler());
		TileEntity.register("tile_computer", TileEntityComputer.class);
	}
	
	public void postinit(FMLPostInitializationEvent e) {
		
	}
	
}