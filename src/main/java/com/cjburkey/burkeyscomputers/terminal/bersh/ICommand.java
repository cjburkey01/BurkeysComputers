package com.cjburkey.burkeyscomputers.terminal.bersh;

public interface ICommand {
	
	String getName();
	String[] getAllArgs();
	int getRequiredArgs();
	CommandHandler getSubCommandHandler();
	EnumCommandResponse onCall(String[] args);
	boolean equals(Object other);
	int hashCode();
	
}