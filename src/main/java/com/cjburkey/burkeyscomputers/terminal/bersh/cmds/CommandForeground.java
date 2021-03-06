package com.cjburkey.burkeyscomputers.terminal.bersh.cmds;

import com.cjburkey.burkeyscomputers.computers.BaseComputer;
import com.cjburkey.burkeyscomputers.terminal.bersh.BaseCommand;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;

public class CommandForeground extends BaseCommand {
	
	public String getName() {
		return "textcolor";
	}

	public String[] getAllArgs() {
		return new String[] { "color" };
	}

	public int getRequiredArgs() {
		return 1;
	}

	public CommandHandler getSubCommandHandler() {
		return null;
	}

	public EnumCommandResponse onCall(BaseComputer computer, String[] args) {
		try {
			computer.setCursorColor(computer.getCursorBackground(), Integer.parseInt(args[0], 16));
			return EnumCommandResponse.CANCEL_RESPONSE;
		} catch (Exception e) {
		}
		computer.drawStringAtCursor(0xFF0000, 0xFFFFFF, "Please enter a valid hex code");
		return EnumCommandResponse.SUCCESS;
	}
	
}