package com.cjburkey.burkeyscomputers.terminal.bersh.cmds;

import com.cjburkey.burkeyscomputers.computers.IComputer;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;
import com.cjburkey.burkeyscomputers.terminal.bersh.ICommand;

public class CommandPrint extends CommandBase {
	
	public String getName() {
		return "print";
	}

	public String[] getAllArgs() {
		return new String[] { "message" };
	}

	public int getRequiredArgs() {
		return 1;
	}

	public CommandHandler getSubCommandHandler() {
		return null;
	}

	public EnumCommandResponse onCall(IComputer computer, String[] args) {
		computer.drawStringAtCursor(args[0]);
		return EnumCommandResponse.SUCCESS;
	}
	
}