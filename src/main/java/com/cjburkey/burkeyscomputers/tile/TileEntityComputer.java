package com.cjburkey.burkeyscomputers.tile;

import com.cjburkey.burkeyscomputers.computers.IComputer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityComputer extends TileEntity implements IComputer {
	
	public static final int cols = 33;
	public static final int rows = 14;
	
	private char[] screen;
	
	public TileEntityComputer() {
		screen = empty();
		for (int i = 0; i < screen.length; i ++) {
			screen[i] = 0;
		}
	}
	
	public char getCharacter(int col, int row) {
		if (!onScreen(col, row)) {
			return 0;
		}
		return screen[getIndex(col, row)];
	}
	
	public String getCharacterS(int col, int row) {
		if (!onScreen(col, row)) {
			return null;
		}
		return "" + getCharacter(col, row);
	}
	
	public void setCharacter(int col, int row, char character) {
		if (!onScreen(col, row)) {
			return;
		}
		screen[getIndex(col, row)] = character;
	}
	
	public char[] getScreen() {
		return screen.clone();
	}
	
	public void clearScreen() {
		screen = empty();
	}
	
	public boolean onScreen(int col, int row) {
		return getIndex(col, row) >= 0;
	}
	
	private int getIndex(int col, int row) {
		if (col < 0 || col > cols || row < 0 || row > rows) {
			return -1;
		}
		return row * cols + col;
	}
	
	public static char[] empty() {
		return new char[cols * rows];
	}
	
	public void keyTyped(int code, char typed) {
		if ((typed >= 32 && typed <= 126) || (typed >= 160 && typed <= 255)) {
			int firstEmptyX = -1;
			int firstEmptyY = -1;
			for (int y = 0; y < TileEntityComputer.rows && firstEmptyY < 0; y ++) {
				for (int x = 0; x < TileEntityComputer.cols; x ++) {
					if (getCharacter(x, y) == 0) {
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
			setCharacter(firstEmptyX, firstEmptyY, typed);
		}
	}
	
}