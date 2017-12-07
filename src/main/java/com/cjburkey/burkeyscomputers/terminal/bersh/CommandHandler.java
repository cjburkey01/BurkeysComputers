package com.cjburkey.burkeyscomputers.terminal.bersh;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.cjburkey.burkeyscomputers.ModUtil;
import com.cjburkey.burkeyscomputers.computers.BaseComputer;

public class CommandHandler {

	private final BaseCommand parent;
	private final List<BaseCommand> commands;
	
	public CommandHandler(BaseCommand parent) {
		this.parent = parent;
		commands = new ArrayList<>();
	}
	
	public void addCommand(BaseCommand cmd) {
		if (commandExists(cmd.getName())) {
			return;
		}
		commands.add(cmd);
	}
	
	public void addCommands(BaseDriver cmds) {
		for (BaseCommand cmd : cmds.getCommands()) {
			addCommand(cmd);
		}
	}
	
	public void removeCommand(BaseCommand cmd) {
		if (!commandExists(cmd.getName())) {
			return;
		}
		commands.remove(cmd);
	}
	
	public void cpuCycle(BaseComputer computer, int operationsPerCycle) {
		for (int i = 0; i < operationsPerCycle; i ++) {
			if (computer.getProcessHandler().isEmpty()) {
				return;
			}
			executeCommand(computer, computer.getProcessHandler().getNext());
		}
	}
	
	private void executeCommand(BaseComputer computer, CmdProcess process) {
		if (process == null) {
			return;
		}
		if (process.getPredeterminedResult().equals(EnumCommandResponse.UNPROCESSED)) {
			EnumCommandResponse ret = process.getCommand().onCall(computer, process.getArguments());
			if (ret.equals(EnumCommandResponse.UNFINISHED)) {
				computer.getProcessHandler().addProcess(process);
			} else {
				computer.onCommandResponse(ret, process);
			}
			return;
		}
		computer.onCommandResponse(process.getPredeterminedResult(), process);
	}
	
	public void addCommandToExectionStack(BaseComputer computer, String line) {
		computer.getProcessHandler().addProcess(registerCommandExecution(computer, line));
	}
	
	private CmdProcess registerCommandExecution(BaseComputer computer, String line) {
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
	
	private CmdProcess process(BaseComputer computer, String[] segments) {
		if (segments.length < 1) {
			return new CmdProcess(null, new String[0], EnumCommandResponse.EMPTY);
		}
		BaseCommand cmd = getCommand(segments[0]);
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
		/*String[] args = new String[segments.length - 1];
		for (int i = 1; i < segments.length; i ++) {
			args[i - 1] = segments[i];
		}*/
		return ModUtil.removeFirstArrayItem(segments, new String[segments.length - 1]);
	}
	
	public String getUsage(BaseCommand cmd) {
		if (cmd == null) {
			return "FATAL ERR";
		}
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
			BaseCommand[] cmds = sub.getCommands();
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
	
	public BaseCommand getParentCommand() {
		return parent;
	}
	
	public boolean hasParentCommand() {
		return getParentCommand() != null;
	}
	
	public BaseCommand getCommand(String name) {
		for (BaseCommand cmd : commands) {
			if (cmd.getName().equalsIgnoreCase(name)) {
				return cmd;
			}
		}
		return null;
	}
	
	public BaseCommand getCommandFromLine(String line) {
		String[] s = line.trim().split(Pattern.quote(" "));
		return getCommand(s[0].trim());
	}
	
	public BaseCommand[] getCommands() {
		return commands.toArray(new BaseCommand[commands.size()]);
	}
	
	public boolean commandExists(String name) {
		return getCommand(name) != null;
	}
	
}