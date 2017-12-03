package com.cjburkey.burkeyscomputers.computers;

import java.util.ArrayList;
import java.util.List;

public class FileContents {
	
	private final List<String> lines = new ArrayList<>();
	
	public String[] getAllLines() {
		return lines.toArray(new String[lines.size()]);
	}
	
	public String getLine(int line) {
		return lines.get(line);
	}
	
	public int getLineCount() {
		return lines.size();
	}
	
	public void erase() {
		lines.clear();
	}
	
	public void setContents(String[] inLines) {
		erase();
		for (String line : inLines) {
			lines.add(line);
		}
	}
	
	public void setContents(String contents) {
		String[] split = contents.split("\n");
		setContents(split);
	}
	
	public void addLine(String line, int index) {
		lines.add(index, line);
	}
	
	public void append(String line) {
		addLine(line, lines.size() - 1);
	}
	
}