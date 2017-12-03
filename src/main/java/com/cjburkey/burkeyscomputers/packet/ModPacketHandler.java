package com.cjburkey.burkeyscomputers.packet;

import com.cjburkey.burkeyscomputers.ModInfo;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModPacketHandler {
	
	private static SimpleNetworkWrapper network;
	
	public static void initNetwork() {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID);
		
		network.registerMessage(PacketTypedOnClient.Handler.class, PacketTypedOnClient.class, 0, Side.SERVER);
		network.registerMessage(PacketUpdateClient.Handler.class, PacketUpdateClient.class, 1, Side.CLIENT);
		
		network.registerMessage(PacketReturnID.Handler.class, PacketReturnID.class, 2, Side.CLIENT);
	}
	
	public static SimpleNetworkWrapper getNetwork() {
		return network;
	}
	
}