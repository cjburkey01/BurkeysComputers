package com.cjburkey.burkeyscomputers.terminal.bersh;

import java.util.Stack;

public class ProcessHandler {
	
	private final Stack<CmdProcess> processes = new Stack<>();
	
	public void addProcess(BaseCommand cmd, EnumCommandResponse resp, String... args) {
		addProcess(new CmdProcess(cmd, args, resp));
	}
	
	public void addProcess(CmdProcess process) {
		processes.push(process);
	}
	
	public CmdProcess getNext() {
		if (isEmpty()) {
			return null;
		}
		return processes.pop();
	}
	
	public boolean isEmpty() {
		return processes.empty();
	}
	
}