package com.cjburkey.burkeyscomputers.packet;

import java.nio.charset.Charset;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.gui.GuiComputer;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketComputerToClient implements IMessage {
	
	private char[] chars;
	
	public PacketComputerToClient() {
	}
	
	public PacketComputerToClient(TileEntityComputer te) {
		chars = te.getScreen();
	}
	
	public void fromBytes(ByteBuf buf) {
		int length = TileEntityComputer.cols * TileEntityComputer.rows;
		char[] tmp  = buf.readCharSequence(length, Charset.forName("UTF-8")).toString().toCharArray();
		if (tmp.length != length) {
			ModLog.info("Failed to parse computer screen.");
			return;
		}
		chars = tmp;
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeCharSequence(new String(chars), Charset.forName("UTF-8"));
	}
	
	public static class Handler implements IMessageHandler<PacketComputerToClient, IMessage> {
		
		public IMessage onMessage(PacketComputerToClient msg, MessageContext ctx) {
			GuiComputer.updateContents(msg.chars);
			return null;
		}
		
	}
	
}