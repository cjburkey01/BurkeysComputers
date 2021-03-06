package com.cjburkey.burkeyscomputers.terminal.bersh.cmds;

import com.cjburkey.burkeyscomputers.computers.BaseComputer;
import com.cjburkey.burkeyscomputers.terminal.bersh.BaseCommand;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;

public class CommandPrint extends BaseCommand {
	
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

	public EnumCommandResponse onCall(BaseComputer computer, String[] args) {
		computer.drawStringAtCursor(args[0]);
		return EnumCommandResponse.SUCCESS;
	}
	
}