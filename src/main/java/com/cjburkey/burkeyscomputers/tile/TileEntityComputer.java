package com.cjburkey.burkeyscomputers.tile;

import java.util.Random;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.computers.ComputerFileSystem;
import com.cjburkey.burkeyscomputers.computers.IComputer;
import com.cjburkey.burkeyscomputers.computers.TermCell;
import com.cjburkey.burkeyscomputers.computers.TermPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityComputer extends TileEntity implements IComputer {
	
	public static final int cols = 33;
	public static final int rows = 14;

	private long uniqueId;
	private TermCell[] screen;
	private ComputerFileSystem fs;
	
	public TileEntityComputer() {
		uniqueId = new Random().nextLong();
		clearScreen();
		fs = new ComputerFileSystem(this);
	}
	
	public long getUniqueId() {
		return uniqueId;
	}
	
	public TermCell[] getScreen() {
		return screen.clone();
	}
	
	public void clearScreen() {
		screen = getNewEmpty();
	}
	
	public TermCell getCell(TermPos pos) {
		if (!fitsOnScreen(pos)) {
			return TermCell.ERROR;
		}
		return screen[getIndex(pos)];
	}
	
	public void keyTyped(int code, char typed) {
		if ((typed >= 32 && typed <= 126) || (typed >= 160 && typed <= 255)) {
			int firstEmptyX = -1;
			int firstEmptyY = -1;
			for (int y = 0; y < TileEntityComputer.rows && firstEmptyY < 0; y ++) {
				for (int x = 0; x < TileEntityComputer.cols; x ++) {
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
			markDirty();
		}
	}
	
	public boolean fitsOnScreen(TermPos pos) {
		return !(pos.col < 0 || pos.col >= cols || pos.row < 0 || pos.row >= rows);
	}
	
	public ComputerFileSystem getFileSystem() {
		return fs;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < screen.length; i ++) {
			NBTTagCompound at = new NBTTagCompound();
			at.setInteger("index", i);
			TermCell.writeToNbt(at, screen[i]);
			list.appendTag(at);
		}
		nbt.setTag("cells", list);
		nbt.setLong("uid", uniqueId);
		return super.writeToNBT(nbt);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt == null) {
			return;
		}
		if (!nbt.hasKey("cells")) {
			return;
		}
		NBTTagList list = nbt.getTagList("cells", 10);
		TermCell[] tmp = getNewEmpty();
		for (int i = 0; i < cols * rows; i ++) {
			if (list.get(i) == null || !(list.get(i) instanceof NBTTagCompound)) {
				ModLog.info("Read: tag is null or not compound type: " + i);
				return;
			}
			NBTTagCompound tag = (NBTTagCompound) list.get(i);
			if (!tag.hasKey("index") || tag.getInteger("index") != i) {
				ModLog.info("Read: nbt doesn't have index, or it doesn't match: " + i);
				return;
			}
			TermCell cell = TermCell.readFromNbt(tag);
			if (cell == null) {
				ModLog.info("Read: nbt cell is null: " + i);
				return;
			}
			tmp[i] = cell;
		}
		if (nbt.hasKey("uid")) {
			uniqueId = nbt.getLong("uid");
		}
		screen = tmp;
		super.readFromNBT(nbt);
	}
	
	private int getIndex(TermPos pos) {
		if (!fitsOnScreen(pos)) {
			return -1;
		}
		return pos.row * cols + pos.col;
	}
	
	public static TermCell[] getNewEmpty() {
		TermCell[] newArr = new TermCell[cols * rows];
		for (int i = 0; i < newArr.length; i ++) {
			newArr[i] = new TermCell();
		}
		return newArr;
	}
	
}