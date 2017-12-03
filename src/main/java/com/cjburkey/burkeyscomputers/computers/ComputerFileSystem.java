package com.cjburkey.burkeyscomputers.computers;

import java.util.HashMap;
import java.util.Map;

public class ComputerFileSystem {
	
	private final IComputer parent;
	private final Map<ReferenceFile, FileContents> files;
	
	public ComputerFileSystem(IComputer parent) {
		this.parent = parent;
		files = new HashMap<>();
	}
	
	public boolean fileExists(ReferenceFile file) {
		return files.containsKey(file);
	}
	
	public boolean fileExists(String file) {
		return fileExists(new ReferenceFile(file));
	}
	
	public FileContents getFileContents(ReferenceFile file) {
		if (!fileExists(file)) {
			return null;
		}
		return files.get(file);
	}
	
	public FileContents getFileContents(String file) {
		return getFileContents(new ReferenceFile(file));
	}
	
	public void deleteFile(ReferenceFile file) {
		if (!fileExists(file)) {
			return;
		}
		getFileContents(file).erase();
		files.remove(file);
	}
	
	public void deleteFile(String file) {
		deleteFile(new ReferenceFile(file));
	}
	
}