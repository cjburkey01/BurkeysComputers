package com.cjburkey.burkeyscomputers.terminal.bersh;

public class CmdProcess {
	
	private ICommand cmd;
	private String[] args;
	private EnumCommandResponse cmdResp;
	
	public CmdProcess(ICommand cmd, String[] args, EnumCommandResponse cmdResp) {
		this.cmd = cmd;
		this.args = args;
		this.cmdResp = cmdResp;
	}
	
	public ICommand getCommand() {
		return cmd;
	}
	
	public String[] getArguments() {
		return args;
	}
	
	public EnumCommandResponse getPredeterminedResult() {
		return cmdResp;
	}
	
}