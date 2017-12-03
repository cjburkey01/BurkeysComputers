package com.cjburkey.burkeyscomputers.computers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ReferenceFile {
	
	private final String[] path;
	private final String name;
	private final boolean dir;
	
	public ReferenceFile(String inPath) {
		inPath = inPath.trim().replaceAll(Pattern.quote("\n"), "");
		inPath = inPath.replaceAll(Pattern.quote("\t"), "");
		String[] tmp = inPath.split(Pattern.quote("/"));
		List<String> out = new ArrayList<String>();
		for (int i = 0; i < tmp.length - 1; i ++) {
			if (tmp[i] == null) {
				continue;
			}
			tmp[i] = tmp[i].trim();
			if (tmp[i].isEmpty()) {
				continue;
			}
			out.add(tmp[i]);
		}
		path = out.toArray(new String[out.size()]);
		name = tmp[tmp.length - 1];
		dir = name.indexOf('.') < 0;
	}
	
	public String getPath() {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < path.length; i ++) {
			out.append(path[i]);
			out.append('/');
		}
		out.append(name);
		return out.toString();
	}
	
	public String[] getPathStems() {
		return path.clone();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasExtension() {
		return dir;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(path);
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
		ReferenceFile other = (ReferenceFile) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (!Arrays.equals(path, other.path)) {
			return false;
		}
		return true;
	}
	
}