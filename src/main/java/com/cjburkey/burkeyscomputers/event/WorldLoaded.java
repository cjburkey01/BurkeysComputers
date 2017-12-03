package com.cjburkey.burkeyscomputers.event;

import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.computers.ComputerHandler;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoaded {
	
	@SubscribeEvent
	public void onWorldLoaded(Load e) {
		ModLog.info("Loaded world.");
		if (!e.getWorld().isRemote) {
			ComputerHandler.get(e.getWorld());
		}
	}
	
}