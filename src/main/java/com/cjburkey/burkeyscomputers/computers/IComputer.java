package com.cjburkey.burkeyscomputers.computers;

import net.minecraft.util.math.BlockPos;

public interface IComputer {
	
	char[] getScreen();
	void clearScreen();
	void setCharacter(int column, int row, char character);
	char getCharacter(int column, int row);
	void keyTyped(int code, char typed);
	BlockPos getPos();
	
}