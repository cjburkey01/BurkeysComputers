package com.cjburkey.burkeyscomputers.container;

import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerComputer extends Container {
	
	private TileEntityComputer computer;
	
	public ContainerComputer(TileEntityComputer computer) {
		this.computer = computer;
	}

	public boolean canInteractWith(EntityPlayer ply) {
		return true;
	}
	
	public TileEntityComputer getComputer() {
		return computer;
	}
	
}