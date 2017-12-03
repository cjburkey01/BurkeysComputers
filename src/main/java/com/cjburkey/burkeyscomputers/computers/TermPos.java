package com.cjburkey.burkeyscomputers.computers;

public class TermPos {
	
	public final int col;
	public final int row;
	
	public TermPos(int col, int row) {
		this.col = col;
		this.row = row;
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
		TermPos other = (TermPos) obj;
		if (col != other.col) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}
	
}