package com.cjburkey.burkeyscomputers.tile;

import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.computers.ComputerHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityComputer extends TileEntity implements ITickable {
	
	private long comp = -1;
	private boolean firstTick = true;
	
	public TileEntityComputer() {
	}
	
	public void update() {
		if (comp >= 0) {
			ComputerHandler.get(getWorld()).getComputer(comp).tick();
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		nbt.setLong("compLongID", comp);
		return super.writeToNBT(nbt);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("compLongID")) {
			comp = nbt.getLong("compLongID");
		}
	}
	
	public void setComputer(long newComp) {
		comp = newComp;
		markDirty();
	}
	
	public long getComputer() {
		return comp;
	}
	
	public static TileEntityComputer getAt(World world, BlockPos pos) {
		TileEntity ent = world.getTileEntity(pos);
		if (ent == null) {
			return null;
		}
		if (!(ent instanceof TileEntityComputer)) {
			return null;
		}
		return (TileEntityComputer) ent;
	}
	
}