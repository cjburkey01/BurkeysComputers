package com.cjburkey.burkeyscomputers.computers;

import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.ModUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public final class TermCell {
	
	public static final int characterWidth = 6;
	public static final int characterHeight = 9;
	public static final TermCell ERROR = new TermCell(false);
	
	private final boolean det;
	private int foregroundColor;
	private int backgroundColor;
	private char character;
	
	public TermCell() {
		this((char) 0);
	}
	
	protected TermCell(boolean det) {
		this.det = det;
	}
	
	public TermCell(char character) {
		this(0x55FF55, character);
	}
	
	public TermCell(int foregroundColor, char character) {
		this(foregroundColor, 0x2A2A2A, character);
	}
	
	public TermCell(int foregroundColor, int backgroundColor, char character) {
		this(false, foregroundColor, backgroundColor, character);
	}
	
	protected TermCell(boolean det, int foregroundColor, int backgroundColor, char character) {
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.character = character;
		this.det = det;
	}

	public void setForegroundColor(int foregroundcolor) {
		this.foregroundColor = foregroundcolor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public int getForegroundColor() {
		return foregroundColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	public int getDrawingForegroundColor() {
		return foregroundColor + 0xFF000000;
	}
	
	public int getDrawingBackgroundColor() {
		return backgroundColor + 0xFF000000;
	}

	public char getCharacter() {
		return character;
	}
	
	public boolean isError() {
		return !det;
	}
	
	public boolean isEmpty() {
		return character == 0;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + backgroundColor;
		result = prime * result + character;
		result = prime * result + (det ? 1231 : 1237);
		result = prime * result + foregroundColor;
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
		TermCell other = (TermCell) obj;
		if (backgroundColor != other.backgroundColor) {
			return false;
		}
		if (character != other.character) {
			return false;
		}
		if (det != other.det) {
			return false;
		}
		if (foregroundColor != other.foregroundColor) {
			return false;
		}
		return true;
	}
	
	public static void writeToNbt(NBTTagCompound nbt, TermCell cell) {
		nbt.setBoolean("det", cell.det);
		nbt.setInteger("foregroundColor", cell.foregroundColor);
		nbt.setInteger("backgroundColor", cell.backgroundColor);
		nbt.setString("character", "" + cell.character);
	}
	
	public static TermCell readFromNbt(NBTTagCompound nbt) {
		if (!nbt.hasKey("det") || !nbt.hasKey("foregroundColor") || !nbt.hasKey("backgroundColor") || !nbt.hasKey("character")) {
			return null;
		}
		try {
			boolean det = nbt.getBoolean("det");
			int foregroundColor = nbt.getInteger("foregroundColor");
			int backgroundColor = nbt.getInteger("backgroundColor");
			char character = nbt.getString("character").charAt(0);
			return new TermCell(det, foregroundColor, backgroundColor, character);
		} catch(Exception e) {
			ModLog.error("Failed to create TermCell from nbt: " + e.getMessage());
		}
		return null;
	}
	
	public static void writeToBuffer(ByteBuf buf, TermCell cell) {
		buf.writeBoolean(cell.det);
		buf.writeInt(cell.foregroundColor);
		buf.writeInt(cell.backgroundColor);
		buf.writeChar(cell.character);
	}
	
	public static TermCell loadFromBuffer(ByteBuf buf, int cols, int rows) {
		try {
			boolean det = buf.readBoolean();
			int foregroundColor = buf.readInt();
			int backgroundColor = buf.readInt();
			char character = buf.readChar();
			return new TermCell(det, foregroundColor, backgroundColor, character);
		} catch(Exception e) {
		}
		return null;
	}
	
}