package com.cjburkey.burkeyscomputers.container;

import com.cjburkey.burkeyscomputers.computers.IComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerComputer extends Container {
	
	private IComputer computer;
	
	public ContainerComputer(IComputer computer) {
		this.computer = computer;
	}

	public boolean canInteractWith(EntityPlayer ply) {
		return true;
	}
	
	public IComputer getComputer() {
		return computer;
	}
	
}