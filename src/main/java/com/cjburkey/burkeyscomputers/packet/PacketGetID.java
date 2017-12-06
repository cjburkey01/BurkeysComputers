package com.cjburkey.burkeyscomputers.packet;

import java.util.regex.Pattern;
import com.cjburkey.burkeyscomputers.computers.ComputerOpener;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketGetID implements IMessage {
	
	private long id;
	
	public PacketGetID() {
	}
	
	public PacketGetID(long id) {
		this.id = id;
	}
	
	public void fromBytes(ByteBuf buf) {
		id = buf.readLong();
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeLong(id);
	}
	
	public static class Handler implements IMessageHandler<PacketGetID, IMessage> {
		
		public PacketGetID onMessage(PacketGetID msg, MessageContext ctx) {
			ComputerOpener.setClientComputer(msg.id);
			return null;
		}
		
	}
	
}