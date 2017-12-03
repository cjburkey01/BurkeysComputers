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

public class PacketTypedOnClient implements IMessage {
	
	private long id;
	private int code;
	private char typed;
	
	public PacketTypedOnClient() {
	}
	
	public PacketTypedOnClient(long cmp, int code, char typed) {
		id = cmp;
		this.code = code;
		this.typed = typed;
	}
	
	public void fromBytes(ByteBuf buf) {
		String s = ByteBufUtils.readUTF8String(buf);
		if (s == null) {
			return;
		}
		String[] split = s.split(Pattern.quote(";"));
		if (split.length != 3) {
			return;
		}
		try  {
			id = Long.parseLong(split[0]);
			code = Integer.parseInt(split[1]);
			typed = split[2].charAt(0);
		} catch(Exception e) {
			ModLog.error("Failed to read from bytes: \"" + s + "\".");
		}
	}
	
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, id + ";" + code + ";" + typed);
	}
	
	public static class Handler implements IMessageHandler<PacketTypedOnClient, PacketUpdateClient> {
		
		// Run on server
		public PacketUpdateClient onMessage(PacketTypedOnClient msg, MessageContext ctx) {
			World world = ctx.getServerHandler().player.world;
			IComputer at = ComputerHandler.get(world).getComputer(msg.id);
			at.keyTyped(msg.code, msg.typed);
			return new PacketUpdateClient(world, msg.id);
		}
		
	}
	
}