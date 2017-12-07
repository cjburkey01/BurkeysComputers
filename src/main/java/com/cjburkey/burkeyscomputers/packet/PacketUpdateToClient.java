package com.cjburkey.burkeyscomputers.packet;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.burkeyscomputers.computers.ComputerHandler;
import com.cjburkey.burkeyscomputers.computers.BaseComputer;
import com.cjburkey.burkeyscomputers.computers.TermCell;
import com.cjburkey.burkeyscomputers.computers.TermPos;
import com.cjburkey.burkeyscomputers.gui.GuiComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateToClient implements IMessage {

	private boolean working;
	private TermPos cursor;
	private TermCell[] screen;
	
	public PacketUpdateToClient() {
	}
	
	public PacketUpdateToClient(World world, long computer) {
		BaseComputer comp = ComputerHandler.get(world).getComputer(computer);
		working = !comp.getProcessHandler().isEmpty();
		cursor = comp.getCursor().getImmutPos();
		screen = comp.getScreen();
	}
	
	public void fromBytes(ByteBuf buf) {
		working = buf.readBoolean();
		cursor = TermPos.fromByteBuf(buf);
		int length = BaseComputer.cols * BaseComputer.rows;
		List<TermCell> cells = new ArrayList<>();
		TermCell tmp;
		while ((tmp = TermCell.loadFromBuffer(buf, BaseComputer.cols, BaseComputer.rows)) != null) {
			cells.add(tmp);
		}
		if (screen == null || cells.size() == screen.length) {
			screen = cells.toArray(new TermCell[cells.size()]);
		}
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(working);
		cursor.writeToBuf(buf);
		for (TermCell cell : screen) {
			TermCell.writeToBuffer(buf, cell);
		}
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateToClient, IMessage> {
		 
		// Run on client
		public IMessage onMessage(PacketUpdateToClient msg, MessageContext ctx) {
			GuiComputer.updateContents(msg.working, msg.cursor, msg.screen);
			return null;
		}
		
	}
	
}