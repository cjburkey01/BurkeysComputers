package com.cjburkey.burkeyscomputers.terminal.bersh;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.cjburkey.burkeyscomputers.computers.IComputer;
import scala.actors.threadpool.Arrays;

public class CommandHandler {

	private final ICommand parent;
	private final List<ICommand> commands;
	
	public CommandHandler(ICommand parent) {
		this.parent = parent;
		commands = new ArrayList<>();
	}
	
	public void addCommand(ICommand cmd) {
		if (commandExists(cmd.getName())) {
			return;
		}
		commands.add(cmd);
	}
	
	public void addCommand(CommandSet cmds) {
		for (ICommand cmd : cmds.getCommands()) {
			addCommand(cmd);
		}
	}
	
	public void removeCommand(ICommand cmd) {
		if (!commandExists(cmd.getName())) {
			return;
		}
		commands.remove(cmd);
	}
	
	public EnumCommandResponse callCommand(IComputer computer, String line) {
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
		return process(computer, pieces.toArray(new String[pieces.size()]));
	}
	
	private EnumCommandResponse process(IComputer computer, String[] segments) {
		if (segments.length < 1) {
			return EnumCommandResponse.EMPTY;
		}
		ICommand cmd = getCommand(segments[0]);
		if (cmd == null) {
			return EnumCommandResponse.CMD_NOT_FOUND;
		}
		if (cmd.getSubCommandHandler() != null) {
			if (segments.length < 2) {
				return EnumCommandResponse.ARGS_SHORT;
			}
			return cmd.getSubCommandHandler().process(computer, extractArgsArray(segments));
		}
		if (segments.length - 1 < cmd.getRequiredArgs()) {
			return EnumCommandResponse.ARGS_SHORT;
		}
		if (segments.length - 1 > cmd.getAllArgs().length) {
			return EnumCommandResponse.ARGS_LONG;
		}
		return cmd.onCall(computer, extractArgsArray(segments));
	}
	
	private String[] extractArgsArray(String[] segments) {
		String[] args = new String[segments.length - 1];
		for (int i = 1; i < segments.length; i ++) {
			args[i - 1] = segments[i];
		}
		return args;
	}
	
	public String getUsage(ICommand cmd) {
		StringBuilder out = new StringBuilder();
		out.append(cmd.getName());
		out.append(' ');
		CommandHandler sub = cmd.getSubCommandHandler();
		if (sub == null) {
			String[] args = cmd.getAllArgs();
			for (int i = 0; i < args.length; i ++) {
				boolean req = i < cmd.getRequiredArgs();
				out.append((req) ? '<' : '[');
				out.append(args[i]);
				out.append((req) ? '>' : ']');
				if (i < args.length - 1) {
					out.append(' ');
				}
			}
		} else {
			out.append('<');
			ICommand[] cmds = sub.getCommands();
			for (int i = 0; i < cmds.length; i ++) {
				out.append(cmds[i].getName());
				if (i < cmds.length - 1) {
					out.append('/');
				}
			}
			out.append('>');
		}
		return out.toString();
	}
	
	public ICommand getParentCommand() {
		return parent;
	}
	
	public boolean hasParentCommand() {
		return getParentCommand() != null;
	}
	
	public ICommand getCommand(String name) {
		for (ICommand cmd : commands) {
			if (cmd.getName().equalsIgnoreCase(name)) {
				return cmd;
			}
		}
		return null;
	}
	
	public ICommand getCommandFromLine(String line) {
		String[] s = line.trim().split(Pattern.quote(" "));
		return getCommand(s[0].trim());
	}
	
	public ICommand[] getCommands() {
		return commands.toArray(new ICommand[commands.size()]);
	}
	
	public boolean commandExists(String name) {
		return getCommand(name) != null;
	}
	
}