package com.cjburkey.burkeyscomputers.computers;

import com.cjburkey.burkeyscomputers.terminal.bersh.CmdProcess;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;
import com.cjburkey.burkeyscomputers.terminal.bersh.ProcessHandler;
import net.minecraft.nbt.NBTTagCompound;

public abstract class BaseComputer {
	
	public static final int cols = 33;
	public static final int rows = 14;
	
	public abstract boolean hasUpdated();
	public abstract void tick();
	public abstract void resetDisplay();
	public abstract void updateId(long id);
	public abstract long getUniqueId();
	public abstract TermCell[] getScreen();
	public abstract TermCell getCell(TermPos pos);
	public abstract void setCharacter(int col, int row, char character, boolean color);
	public abstract void clearScreen();
	public abstract void keyTyped(int code, char typed);
	public abstract void drawStringAtCursor(String in);
	public abstract void drawStringAtCursor(int fcolor, String in);
	public abstract void drawStringAtCursor(int fcolor, int bcolor, String in);
	public abstract ComputerFileSystem getFileSystem();
	public abstract CommandHandler getTerminalCommandHandler();
	public abstract ProcessHandler getProcessHandler();
	public abstract NBTTagCompound writeToNBT(NBTTagCompound nbt);
	public abstract void readFromNBT(NBTTagCompound nbt);
	public abstract String save();
	public abstract void load(String data);
	public abstract MutTermPos getCursor();
	public abstract void setCursor(TermPos pos);
	public abstract void resetCursor();
	public abstract void onCommandResponse(EnumCommandResponse response, CmdProcess process);
	public abstract int getCursorBackground();
	public abstract int getCursorForeground();
	public abstract void setCursorColor(int background, int foreground);
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (getUniqueId() ^ (getUniqueId() >>> 32));
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseComputer other = (BaseComputer) obj;
		if (getUniqueId() != other.getUniqueId()) {
			return false;
		}
		return true;
	}
	
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
	
	public static BaseComputer loadFromData(String extra) {
		if (extra.startsWith("world;")) {
			WorldComputer out = new WorldComputer(null, Integer.MIN_VALUE);
			out.load(extra);
			return out;
		}
		return null;
	}
	
}