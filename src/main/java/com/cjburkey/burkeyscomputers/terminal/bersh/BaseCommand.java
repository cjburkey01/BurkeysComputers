package com.cjburkey.burkeyscomputers.terminal.bersh;

import com.cjburkey.burkeyscomputers.computers.BaseComputer;

public abstract class BaseCommand {
	
	public abstract String getName();
	public abstract String[] getAllArgs();
	public abstract int getRequiredArgs();
	public abstract CommandHandler getSubCommandHandler();
	public abstract EnumCommandResponse onCall(BaseComputer computer, String[] args);
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseCommand other = (BaseCommand) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		return true;
	}
	
}