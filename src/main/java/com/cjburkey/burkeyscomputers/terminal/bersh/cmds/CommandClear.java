package com.cjburkey.burkeyscomputers.terminal.bersh.cmds;

import com.cjburkey.burkeyscomputers.computers.IComputer;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;
import com.cjburkey.burkeyscomputers.terminal.bersh.ICommand;

public class CommandClear extends CommandBase {
	
	public String getName() {
		return "clear";
	}

	public String[] getAllArgs() {
		return new String[0];
	}

	public int getRequiredArgs() {
		return 0;
	}

	public CommandHandler getSubCommandHandler() {
		return null;
	}

	public EnumCommandResponse onCall(IComputer computer, String[] args) {
		computer.resetDisplay();
		return EnumCommandResponse.CANCEL_RESPONSE;
	}
	
}