package com.cjburkey.burkeyscomputers.computers;

import java.util.regex.Pattern;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandPrint;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldComputer implements IComputer {
	
	private long uniqueId = -1;
	private TermCell[] screen;
	private ComputerFileSystem fs;
	private CommandHandler ch;
	private BlockPos pos;
	private int world;
	
	public WorldComputer(BlockPos pos, int world) {
		this.pos = pos;
		this.world = world;
		clearScreen();
		fs = new ComputerFileSystem(this);
		ch = new CommandHandler(null);
	}
	
	public void updateId(long id) {
		uniqueId = id;
	}
	
	public long getUniqueId() {
		return uniqueId;
	}
	
	public TermCell[] getScreen() {
		return screen.clone();
	}
	
	public TermCell getCell(TermPos pos) {
		if (!IComputer.fitsOnScreen(pos)) {
			return TermCell.ERROR;
		}
		return screen[getIndex(pos)];
	}
	
	public void clearScreen() {
		screen = IComputer.getNewEmptyScreen();
	}
	
	public void keyTyped(int code, char typed) {
		if ((typed >= 32 && typed <= 126) || (typed >= 160 && typed <= 255)) {
			int firstEmptyX = -1;
			int firstEmptyY = -1;
			for (int y = 0; y < rows && firstEmptyY < 0; y ++) {
				for (int x = 0; x < cols; x ++) {
					if (getCell(new TermPos(x, y)).isEmpty()) {
						firstEmptyX = x;
						firstEmptyY = y;
						break;
					}
				}
			}
			if (firstEmptyX < 0 || firstEmptyY < 0) {
				firstEmptyX = 0;
				firstEmptyY = 0;
				clearScreen();
			}
			getCell(new TermPos(firstEmptyX, firstEmptyY)).setCharacter(typed);
		}
	}
	
	public ComputerFileSystem getFileSystem() {
		return fs;
	}
	
	public CommandHandler getTerminalCommandHandler() {
		return ch;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
	}
	
	private int getIndex(TermPos pos) {
		if (!IComputer.fitsOnScreen(pos)) {
			return -1;
		}
		return pos.row * cols + pos.col;
	}
	
	public String save() {
		return "world;" + world + ";" + pos.getX() + ";" + pos.getY() + ";" + pos.getZ();
	}
	
	public void load(String data) {
		String[] splt = data.split(Pattern.quote(";"));
		if (splt.length != 5) {
			return;
		}
		try {
			int world = Integer.parseInt(splt[1]);
			int x = Integer.parseInt(splt[2]);
			int y = Integer.parseInt(splt[3]);
			int z = Integer.parseInt(splt[4]);
			this.world = world;
			this.pos = new BlockPos(x, y, z);
		} catch(Exception e) {
			ModLog.error("Failed to parse: \"" + data + "\"");
		}
	}
	
}