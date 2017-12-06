package com.cjburkey.burkeyscomputers.terminal.bersh;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.cjburkey.burkeyscomputers.computers.IComputer;

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
	
	public void cpuCycle(IComputer computer, int operationsPerCycle) {
		for (int i = 0; i < operationsPerCycle; i ++) {
			if (computer.getProcessHandler().isEmpty()) {
				return;
			}
			executeCommand(computer, computer.getProcessHandler().getNext());
		}
	}
	
	private void executeCommand(IComputer computer, CmdProcess process) {
		if (process == null) {
			return;
		}
		if (process.getPredeterminedResult().equals(EnumCommandResponse.UNPROCESSED)) {
			computer.onCommandResponse(process.getCommand().onCall(computer, process.getArguments()), process);
			return;
		}
		computer.onCommandResponse(process.getPredeterminedResult(), process);
	}
	
	public void addCommandToExectionStack(IComputer computer, String line) {
		computer.getProcessHandler().addProcess(registerCommandExecution(computer, line));
	}
	
	private CmdProcess registerCommandExecution(IComputer computer, String line) {
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
	
	private CmdProcess process(IComputer computer, String[] segments) {
		if (segments.length < 1) {
			return new CmdProcess(null, new String[0], EnumCommandResponse.EMPTY);
		}
		ICommand cmd = getCommand(segments[0]);
		if (cmd == null) {
			return new CmdProcess(null, new String[0], EnumCommandResponse.CMD_NOT_FOUND);
		}
		if (cmd.getSubCommandHandler() != null) {
			if (segments.length < 2) {
				return new CmdProcess(cmd, new String[0], EnumCommandResponse.ARGS_SHORT);
			}
			return cmd.getSubCommandHandler().process(computer, extractArgsArray(segments));
		}
		if (segments.length - 1 < cmd.getRequiredArgs()) {
			return new CmdProcess(cmd, new String[0], EnumCommandResponse.ARGS_SHORT);
		}
		if (segments.length - 1 > cmd.getAllArgs().length) {
			return new CmdProcess(cmd, new String[0], EnumCommandResponse.ARGS_LONG);
		}
		return new CmdProcess(cmd, extractArgsArray(segments), EnumCommandResponse.UNPROCESSED);
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