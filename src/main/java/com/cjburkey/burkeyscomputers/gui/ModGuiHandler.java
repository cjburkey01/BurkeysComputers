package com.cjburkey.burkeyscomputers.gui;

import com.cjburkey.burkeyscomputers.container.ContainerComputer;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GuiComputer.id) {
			return new ContainerComputer(TileEntityComputer.getAt(world, new BlockPos(x, y, z)).getComputer());
		}
		return null;
	}

	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GuiComputer.id) {
			return new GuiComputer(new ContainerComputer(TileEntityComputer.getAt(world, new BlockPos(x, y, z)).getComputer()));
		}
		return null;
	}
	
}