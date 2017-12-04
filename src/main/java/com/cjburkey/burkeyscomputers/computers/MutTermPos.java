package com.cjburkey.burkeyscomputers.computers;

public class MutTermPos {
	
	public int col;
	public int row;
	
	public MutTermPos() {
		this(0, 0);
	}
	
	public MutTermPos(int col, int row) {
		this.col = col;
		this.row = row;
	}
	
	public TermPos getImmutPos() {
		return new TermPos(col, row);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
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
		MutTermPos other = (MutTermPos) obj;
		if (col != other.col) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}
	
}