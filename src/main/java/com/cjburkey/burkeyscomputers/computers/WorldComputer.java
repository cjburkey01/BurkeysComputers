package com.cjburkey.burkeyscomputers.computers;

import java.util.regex.Pattern;
import org.lwjgl.input.Keyboard;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.terminal.bersh.CmdProcess;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandSet;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;
import com.cjburkey.burkeyscomputers.terminal.bersh.ProcessHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class WorldComputer implements IComputer {
	
	private long uniqueId = -1;
	private TermCell[] screen;
	private ComputerFileSystem fs;
	private CommandHandler ch;
	private BlockPos pos;
	private int world;
	private TermPos prevCursor;
	private MutTermPos cursorPos;
	private ProcessHandler processHandler;
	private boolean updateAvailable = false;
	
	private String command = "";
	
	public WorldComputer(BlockPos pos, int world) {
		this.pos = pos;
		this.world = world;
		clearScreen();
		fs = new ComputerFileSystem(this);
		ch = new CommandHandler(null);
		ch.addCommand(new CommandSet());
		processHandler = new ProcessHandler();
		resetDisplay();
	}
	
	public boolean hasUpdated() {
		boolean tmp = updateAvailable;
		updateAvailable = false;
		return tmp;
	}
	
	public void tick() {
		ch.cpuCycle(this, 1);
	}
	
	public void resetDisplay() {
		clearScreen();
		cursorPos = new MutTermPos(0, 0);
		setupMainInputLine();
	}
	
	public void setupMainInputLine() {
		getCell(new TermPos(0, cursorPos.row)).setCharacter('>');
		setCursor(1, cursorPos.row);
	}
	
	private void verifyCursorPos(int resetCol) {
		if (cursorPos.col >= IComputer.cols) {
			cursorPos.col = resetCol;
			cursorPos.row ++;
		}
		if (cursorPos.row >= IComputer.rows) {
			scrollDown();
			setCursor(cursorPos.col, IComputer.rows - 1);
		}
	}
	
	private void verifyCursorPos() {
		verifyCursorPos(0);
	}
	
	public void updateId(long id) {
		uniqueId = id;
	}
	
	public long getUniqueId() {
		return uniqueId;
	}
	
	public TermCell[] getScreen() {
		return screen.clone();
	}
	
	public TermCell getCell(TermPos pos) {
		if (!IComputer.fitsOnScreen(pos)) {
			return TermCell.ERROR;
		}
		return screen[getIndex(pos)];
	}
	
	public void clearScreen() {
		screen = IComputer.getNewEmptyScreen();
	}
	
	public void keyTyped(int code, char typed) {
		updateAvailable = true;
		if (code == Keyboard.KEY_NUMPADENTER || code == Keyboard.KEY_RETURN) {
			setCursor(0, cursorPos.row + 1);
			ch.addCommandToExectionStack(this, command);
			command = "";
			return;
		}
		if ((typed >= 32 && typed <= 126) || (typed >= 160 && typed <= 255)) {
			getCell(cursorPos.getImmutPos()).setCharacter(typed);
			setCursor(cursorPos.col + 1, cursorPos.row);
			command += typed;
		}
	}
	
	public void drawStringAtCursor(String in) {
		if (in == null || in.isEmpty()) {
			return;
		}
		char[] s = in.toCharArray();
		for (char c : s) {
			getCell(cursorPos.getImmutPos()).setCharacter(c);
			setCursor(cursorPos.col + 1, cursorPos.row);
		}
	}
	
	private void scrollDown() {
		for (int y = 0; y < IComputer.rows; y ++) {
			for (int x = 0; x < IComputer.cols; x ++) {
				getCell(new TermPos(x, y)).setCharacter((y < IComputer.rows - 1) ? (getCell(new TermPos(x, y + 1)).getCharacter()) : ((char) 0));
			}
		}
	}
	
	public ComputerFileSystem getFileSystem() {
		return fs;
	}
	
	public CommandHandler getTerminalCommandHandler() {
		return ch;
	}
	
	public ProcessHandler getProcessHandler() {
		return processHandler;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
	}
	
	private int getIndex(TermPos pos) {
		if (!IComputer.fitsOnScreen(pos)) {
			return -1;
		}
		return pos.row * cols + pos.col;
	}
	
	public String save() {
		return "world;" + world + ";" + pos.getX() + ";" + pos.getY() + ";" + pos.getZ();
	}
	
	public void load(String data) {
		String[] splt = data.split(Pattern.quote(";"));
		if (splt.length != 5) {
			return;
		}
		try {
			int world = Integer.parseInt(splt[1]);
			int x = Integer.parseInt(splt[2]);
			int y = Integer.parseInt(splt[3]);
			int z = Integer.parseInt(splt[4]);
			this.world = world;
			this.pos = new BlockPos(x, y, z);
		} catch(Exception e) {
			ModLog.error("Failed to parse: \"" + data + "\"");
		}
	}

	public MutTermPos getCursor() {
		return cursorPos;
	}

	public void setCursor(TermPos pos) {
		setCursor(pos.col, pos.row);
	}
	
	public void setCursor(int col, int row) {
		cursorPos.col = col;
		cursorPos.row = row;
		verifyCursorPos();
	}
	
	public void resetCursor() {
		setCursor(new TermPos());
	}
	
	public void onCommandResponse(EnumCommandResponse res, CmdProcess process) {
		if (res.equals(EnumCommandResponse.CMD_NOT_FOUND)) {
			drawStringAtCursor("Command not found");
		} else if(res.equals(EnumCommandResponse.ARGS_SHORT) || res.equals(EnumCommandResponse.ARGS_LONG)) {
			drawStringAtCursor("Usage: " + ch.getUsage(ch.getCommandFromLine(command)));
		} else if(res.equals(EnumCommandResponse.FAIL)) {
			drawStringAtCursor("Command failed");
		}
		if (!res.equals(EnumCommandResponse.CANCEL_RESPONSE)) {
			setCursor(new TermPos(0, cursorPos.row + 1));
			setupMainInputLine();
		}
		updateAvailable = true;
	}
	
}