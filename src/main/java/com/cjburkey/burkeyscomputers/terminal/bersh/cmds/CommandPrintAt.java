package com.cjburkey.burkeyscomputers.terminal.bersh.cmds;

import com.cjburkey.burkeyscomputers.computers.BaseComputer;
import com.cjburkey.burkeyscomputers.computers.TermPos;
import com.cjburkey.burkeyscomputers.terminal.bersh.BaseCommand;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;

public class CommandPrintAt extends BaseCommand {
	
	public String getName() {
		return "printat";
	}

	public String[] getAllArgs() {
		return new String[] { "message", "column", "row" };
	}

	public int getRequiredArgs() {
		return 3;
	}

	public CommandHandler getSubCommandHandler() {
		return null;
	}

	public EnumCommandResponse onCall(BaseComputer computer, String[] args) {
		try {
			int col = Integer.parseInt(args[1]);
			int row = Integer.parseInt(args[2]);
			computer.setCursor(new TermPos(col, row));
			computer.drawStringAtCursor(args[0]);
			return EnumCommandResponse.SUCCESS;
		} catch(Exception e) {  }
		computer.drawStringAtCursor("Please enter valid rows/columns");
		return EnumCommandResponse.SUCCESS;
	}
	
}