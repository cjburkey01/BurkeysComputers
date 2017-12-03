package com.cjburkey.burkeyscomputers.computers;

import com.cjburkey.burkeyscomputers.BurkeysComputers;
import com.cjburkey.burkeyscomputers.gui.GuiComputer;
import com.cjburkey.burkeyscomputers.packet.ModPacketHandler;
import com.cjburkey.burkeyscomputers.packet.PacketReturnID;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ComputerOpener {
	
	private static long computer;
	
	public static void openForPlayer(EntityPlayer ply, World world, BlockPos pos) {
		if (!world.isRemote) {
			TileEntityComputer at = TileEntityComputer.getAt(world, pos);
			if (at != null) {
				ModPacketHandler.getNetwork().sendTo(new PacketReturnID(at.getComputer()), (EntityPlayerMP) ply);
				ply.openGui(BurkeysComputers.instance, GuiComputer.id, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
	}
	
	public static void setClientComputer(long in) {
		computer = in;
	}
	
	public static long getClientComputer() {
		return computer;
	}
	
}