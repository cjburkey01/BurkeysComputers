package com.cjburkey.burkeyscomputers.terminal.bersh.cmds;

import com.cjburkey.burkeyscomputers.ModUtil;
import com.cjburkey.burkeyscomputers.computers.BaseComputer;
import com.cjburkey.burkeyscomputers.computers.TermPos;
import com.cjburkey.burkeyscomputers.terminal.bersh.BaseCommand;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;

public class CommandRandomCrap extends BaseCommand {
	
	public String getName() {
		return "virus";
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
		for (int x = 0; x < BaseComputer.cols; x ++) {
			for (int y = 0; y < BaseComputer.rows; y ++) {
				computer.setCursorColor(ModUtil.random(0x000000, 0x666666), ModUtil.random(0x666666, 0xFFFFFF));
				computer.setCharacter(x, y, (char) (ModUtil.random(32, 127)), true);
			}
		}
		String deathOs = "DEATH OS";
		computer.setCursor(new TermPos((BaseComputer.cols - deathOs.length()) / 2, BaseComputer.rows / 2));
		computer.drawStringAtCursor(0xD81212, 0x114545, deathOs);
		computer.setCursor(new TermPos(-1, -1));
		return EnumCommandResponse.UNFINISHED;
	}
	
}