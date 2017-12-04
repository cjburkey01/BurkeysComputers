package com.cjburkey.burkeyscomputers.terminal.bersh;

import com.cjburkey.burkeyscomputers.computers.IComputer;

public interface ICommand {
	
	String getName();
	String[] getAllArgs();
	int getRequiredArgs();
	CommandHandler getSubCommandHandler();
	EnumCommandResponse onCall(IComputer computer, String[] args);
	boolean equals(Object other);
	int hashCode();
	
}