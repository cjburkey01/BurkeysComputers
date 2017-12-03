package com.cjburkey.burkeyscomputers.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerComputer extends Container {
	
	private long computer;
	
	public ContainerComputer(long computer) {
		this.computer = computer;
	}

	public boolean canInteractWith(EntityPlayer ply) {
		return true;
	}
	
	public long getComputer() {
		return computer;
	}
	
}