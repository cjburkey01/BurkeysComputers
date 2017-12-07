package com.cjburkey.burkeyscomputers.terminal.bersh;

import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandBackground;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandClear;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandForeground;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandPrint;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandPrintAt;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandRandomCrap;
import com.cjburkey.burkeyscomputers.terminal.bersh.cmds.CommandResetColor;

public class DefaultDriver extends BaseDriver {
	
	public BaseCommand[] getCommands() {
		return new BaseCommand[] {
			new CommandPrint(),
			new CommandPrintAt(),
			new CommandClear(),
			new CommandForeground(),
			new CommandBackground(),
			new CommandResetColor(),
			new CommandRandomCrap()
		};
	}
	
}