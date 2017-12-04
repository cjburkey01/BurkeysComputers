package com.cjburkey.burkeyscomputers.computers;

import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface IComputer {
	
	public static final int cols = 33;
	public static final int rows = 14;
	
	public static TermCell[] getNewEmptyScreen() {
		TermCell[] newArr = new TermCell[cols * rows];
		for (int i = 0; i < newArr.length; i ++) {
			newArr[i] = new TermCell();
		}
		return newArr;
	}
	
	public static boolean fitsOnScreen(TermPos pos) {
		return !(pos.col < 0 || pos.col >= cols || pos.row < 0 || pos.row >= rows);
	}
	
	public static IComputer loadFromData(String extra) {
		if (extra.startsWith("world;")) {
			WorldComputer out = new WorldComputer(null, Integer.MIN_VALUE);
			out.load(extra);
			return out;
		}
		return null;
	}
	
	void resetDisplay();
	void updateId(long id);
	long getUniqueId();
	TermCell[] getScreen();
	TermCell getCell(TermPos pos);
	void clearScreen();
	void keyTyped(int code, char typed);
	void drawStringAtCursor(String in);
	ComputerFileSystem getFileSystem();
	CommandHandler getTerminalCommandHandler();
	NBTTagCompound writeToNBT(NBTTagCompound nbt);
	void readFromNBT(NBTTagCompound nbt);
	String save();
	void load(String data);
	MutTermPos getCursor();
	void setCursor(TermPos pos);
	void resetCursor();
	
}