package com.cjburkey.burkeyscomputers.packet;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.burkeyscomputers.computers.ComputerHandler;
import com.cjburkey.burkeyscomputers.computers.IComputer;
import com.cjburkey.burkeyscomputers.computers.TermCell;
import com.cjburkey.burkeyscomputers.computers.TermPos;
import com.cjburkey.burkeyscomputers.gui.GuiComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateClient implements IMessage {
	
	private TermCell[] screen;
	private TermPos cursor;
	
	public PacketUpdateClient() {
	}
	
	public PacketUpdateClient(World world, long computer) {
		IComputer comp = ComputerHandler.get(world).getComputer(computer);
		screen = comp.getScreen();
		cursor = comp.getCursor().getImmutPos();
	}
	
	public void fromBytes(ByteBuf buf) {
		cursor = TermPos.fromByteBuf(buf);
		int length = IComputer.cols * IComputer.rows;
		List<TermCell> cells = new ArrayList<>();
		TermCell tmp;
		while ((tmp = TermCell.loadFromBuffer(buf, IComputer.cols, IComputer.rows)) != null) {
			cells.add(tmp);
		}
		if (screen == null || cells.size() == screen.length) {
			screen = cells.toArray(new TermCell[cells.size()]);
		}
	}
	
	public void toBytes(ByteBuf buf) {
		cursor.writeToBuf(buf);
		for (TermCell cell : screen) {
			TermCell.writeToBuffer(buf, cell);
		}
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateClient, IMessage> {
		 
		// Run on client
		public IMessage onMessage(PacketUpdateClient msg, MessageContext ctx) {
			GuiComputer.updateContents(msg.cursor, msg.screen);
			return null;
		}
		
	}
	
}