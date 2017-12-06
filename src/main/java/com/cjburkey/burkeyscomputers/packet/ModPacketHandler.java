package com.cjburkey.burkeyscomputers.packet;

import com.cjburkey.burkeyscomputers.ModInfo;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModPacketHandler {
	
	private static SimpleNetworkWrapper network;
	
	public static void initNetwork() {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID);
		
		network.registerMessage(PacketUserTyped.Handler.class, PacketUserTyped.class, 0, Side.SERVER);				// Sends data to server about keypress.
		network.registerMessage(PacketUpdateToClient.Handler.class, PacketUpdateToClient.class, 1, Side.CLIENT);	// Sends info the client about what to show.
		
		network.registerMessage(PacketGetID.Handler.class, PacketGetID.class, 2, Side.CLIENT);						// Sends the ID of the computer to the client.
		
		network.registerMessage(PacketRequestUpdate.Handler.class, PacketRequestUpdate.class, 3, Side.SERVER);		// Requests an update be sent to the client.
	}
	
	public static SimpleNetworkWrapper getNetwork() {
		return network;
	}
	
}