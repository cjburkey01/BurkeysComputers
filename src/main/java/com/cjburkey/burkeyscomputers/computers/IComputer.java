package com.cjburkey.burkeyscomputers.computers;

import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface IComputer {
	
	long getUniqueId();
	TermCell[] getScreen();
	TermCell getCell(TermPos pos);
	void clearScreen();
	void keyTyped(int code, char typed);
	boolean fitsOnScreen(TermPos pos);
	BlockPos getPos();
	ComputerFileSystem getFileSystem();
	CommandHandler getTerminalCommandHandler();
	NBTTagCompound writeToNBT(NBTTagCompound nbt);
	void readFromNBT(NBTTagCompound nbt);
	
}