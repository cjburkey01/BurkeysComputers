package com.cjburkey.burkeyscomputers.terminal.bersh.cmds;

import com.cjburkey.burkeyscomputers.computers.BaseComputer;
import com.cjburkey.burkeyscomputers.computers.TermPos;
import com.cjburkey.burkeyscomputers.terminal.bersh.BaseCommand;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;

public class CommandClear extends BaseCommand {
	
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

	public EnumCommandResponse onCall(BaseComputer computer, String[] args) {
		computer.clearScreen();
		computer.setCursor(new TermPos(0, 0));
		return EnumCommandResponse.CANCEL_RESPONSE;
	}
	
}