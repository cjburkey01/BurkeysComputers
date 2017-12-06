package com.cjburkey.burkeyscomputers.packet;

import java.util.regex.Pattern;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.computers.ComputerHandler;
import com.cjburkey.burkeyscomputers.computers.IComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestUpdate implements IMessage {
	
	private long id;
	
	public PacketRequestUpdate() {
	}
	
	public PacketRequestUpdate(long cmp) {
		id = cmp;
	}
	
	public void fromBytes(ByteBuf buf) {
		id = buf.readLong();
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeLong(id);
	}
	
	public static class Handler implements IMessageHandler<PacketRequestUpdate, PacketUpdateToClient> {
		
		// Run on server
		public PacketUpdateToClient onMessage(PacketRequestUpdate msg, MessageContext ctx) {
			World world = ctx.getServerHandler().player.world;
			IComputer at = ComputerHandler.get(world).getComputer(msg.id);
			if (at != null) {
				if (at.hasUpdated()) {
					return new PacketUpdateToClient(world, msg.id);
				} else {
					return null;
				}
			}
			ModLog.error("Computer is null: " + msg.id);
			return null;
		}
		
	}
	
}