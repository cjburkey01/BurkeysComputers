package com.cjburkey.burkeyscomputers.terminal.bersh;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import scala.actors.threadpool.Arrays;

public class CommandHandler {
	
	private final List<ICommand> commands;
	
	public CommandHandler() {
		commands = new ArrayList<>();
	}
	
	public void addCommand(ICommand cmd) {
		if (commandExists(cmd.getName())) {
			return;
		}
		commands.add(cmd);
	}
	
	public void removeCommand(ICommand cmd) {
		if (!commandExists(cmd.getName())) {
			return;
		}
		commands.remove(cmd);
	}
	
	public void callCommand(String line) {
		List<String> pieces = new ArrayList<>();
		String regex = "\"([^\"]*)\"|(\\S+)";
		Matcher m = Pattern.compile(regex).matcher(line);
		while (m.find()) {
			if (m.group(1) != null) {
				pieces.add(m.group(1));
			} else if (m.group(2) != null) {
				pieces.add(m.group(2));
			}
		}
		process(pieces.toArray(new String[pieces.size()]));
	}
	
	private void process(String[] segments) {
		System.out.println(Arrays.toString(segments));
	}
	
	public ICommand getCommand(String name) {
		for (ICommand cmd : commands) {
			if (cmd.getName().equalsIgnoreCase(name)) {
				return cmd;
			}
		}
		return null;
	}
	
	public boolean commandExists(String name) {
		return getCommand(name) != null;
	}
	
}