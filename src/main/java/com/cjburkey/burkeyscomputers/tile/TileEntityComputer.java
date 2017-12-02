package com.cjburkey.burkeyscomputers.tile;

import net.minecraft.tileentity.TileEntity;

public class TileEntityComputer extends TileEntity {
	
	private final int cols;
	private final int rows;
	private char[] screen;
	
	public TileEntityComputer(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
		screen = new char[cols * rows];
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
	
	public void setCharacter(int col, int row, char character) {
		if (!onScreen(col, row)) {
			return;
		}
		screen[getIndex(col, row)] = character;
	}
	
	public int getColumns() {
		return cols;
	}
	
	public int getRows() {
		return rows;
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
	
}