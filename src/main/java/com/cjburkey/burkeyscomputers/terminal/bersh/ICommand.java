package com.cjburkey.burkeyscomputers.terminal.bersh;

public interface ICommand {
	
	String getName();
	String[] getAllArgs();
	int getRequiredArgs();
	CommandHandler getSubCommandHandler();
	void onCall(String[] args);
	
}