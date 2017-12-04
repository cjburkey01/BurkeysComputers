package com.cjburkey.burkeyscomputers.terminal.bersh;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandClear;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandPrint;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandPrintAt;

public class CommandSet {
	
	private List<ICommand> cmds = new ArrayList<>();
	
	public CommandSet() {
		cmds.add(new CommandPrint());
		cmds.add(new CommandPrintAt());
		cmds.add(new CommandClear());
	}
	
	public ICommand[] getCommands() {
		return cmds.toArray(new ICommand[cmds.size()]);
	}
	
}