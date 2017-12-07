package com.cjburkey.burkeyscomputers.terminal.bersh;

public class CmdProcess {
	
	private BaseCommand cmd;
	private String[] args;
	private EnumCommandResponse cmdResp;
	
	public CmdProcess(BaseCommand cmd, String[] args, EnumCommandResponse cmdResp) {
		this.cmd = cmd;
		this.args = args;
		this.cmdResp = cmdResp;
	}
	
	public BaseCommand getCommand() {
		return cmd;
	}
	
	public String[] getArguments() {
		return args;
	}
	
	public EnumCommandResponse getPredeterminedResult() {
		return cmdResp;
	}
	
}