package com.cjburkey.burkeyscomputers.packet;

import java.util.regex.Pattern;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.computers.IComputer;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTypedOnClient implements IMessage {
	
	private BlockPos pos;
	private int code;
	private char typed;
	
	public PacketTypedOnClient() {
	}
	
	public PacketTypedOnClient(IComputer cmp, int code, char typed) {
		pos = cmp.getPos();
		this.code = code;
		this.typed = typed;
	}
	
	public void fromBytes(ByteBuf buf) {
		String s = ByteBufUtils.readUTF8String(buf);
		if (s == null) {
			return;
		}
		String[] split = s.split(Pattern.quote(";"));
		if (split.length != 5) {
			return;
		}
		try  {
			pos = new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
			code = Integer.parseInt(split[3]);
			typed = split[4].charAt(0);
		} catch(Exception e) {
			ModLog.info("Failed to read from bytes: \"" + s + "\".");
		}
	}
	
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, pos.getX() + ";" + pos.getY() + ";" + pos.getZ() + ";" + code + ";" + typed);
	}
	
	public static class Handler implements IMessageHandler<PacketTypedOnClient, PacketUpdateClient> {
		
		public PacketUpdateClient onMessage(PacketTypedOnClient msg, MessageContext ctx) {
			if (msg == null) {
				ModLog.info("Packet doesn't exist.");
				return null;
			}
			TileEntity ent = ctx.getServerHandler().player.world.getTileEntity(msg.pos);
			if (ent == null) {
				ModLog.info("Tile entity doesn't exist at" + msg.pos);
				return null;
			}
			TileEntityComputer cmptr = (TileEntityComputer) ent;
			cmptr.keyTyped(msg.code, msg.typed);
			return new PacketUpdateClient(cmptr);
		}
		
	}
	
}