package com.cjburkey.burkeyscomputers.terminal.bersh.cmds;

import com.cjburkey.burkeyscomputers.computers.BaseComputer;
import com.cjburkey.burkeyscomputers.computers.TermCell;
import com.cjburkey.burkeyscomputers.terminal.bersh.BaseCommand;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;

public class CommandResetColor extends BaseCommand {
	
	public String getName() {
		return "resetcolor";
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

	public EnumCommandResponse onCall(BaseComputer computer, String[] args) {
		computer.setCursorColor(TermCell.getDefaultBackgroundColor(), TermCell.getDefaultForegroundColor());
		return EnumCommandResponse.CANCEL_RESPONSE;
	}
	
}