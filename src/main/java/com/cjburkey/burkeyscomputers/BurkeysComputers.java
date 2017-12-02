package com.cjburkey.burkeyscomputers;

import com.cjburkey.burcore.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(name = ModInfo.MOD_NAME, modid = ModInfo.MOD_ID, version = ModInfo.MOD_VERSION)
public class BurkeysComputers {
	
	@Instance(ModInfo.MOD_ID)
	public static BurkeysComputers instance;
	
	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
	public static CommonProxy proxy;
	
	@EventHandler
	public void construct(FMLConstructionEvent e) {
		proxy.construct(e);
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent e) {
		proxy.preinit(e);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent e) {
		proxy.postinit(e);
	}
	
}