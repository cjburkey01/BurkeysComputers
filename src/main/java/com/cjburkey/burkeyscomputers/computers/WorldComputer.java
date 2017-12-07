package com.cjburkey.burkeyscomputers.computers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.lwjgl.input.Keyboard;
import com.cjburkey.burkeyscomputers.ModInfo;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.terminal.bersh.CmdProcess;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.DefaultDriver;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;
import com.cjburkey.burkeyscomputers.terminal.bersh.ProcessHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class WorldComputer extends BaseComputer {
	
	private long uniqueId = -1;
	private TermCell[] screen;
	private ComputerFileSystem fs;
	private CommandHandler ch;
	private BlockPos pos;
	private int world;
	private TermPos prevCursor;
	private MutTermPos cursorPos;
	private int cursorForeColor = TermCell.getDefaultForegroundColor();
	private int cursorBackColor = TermCell.getDefaultBackgroundColor();
	private ProcessHandler processHandler;
	private boolean updateAvailable = false;
	
	private final List<String> previous = new ArrayList<>();
	private final StringBuilder command = new StringBuilder();
	private int cmdIndex = 0;
	private int prevIndex = 0;
	
	public WorldComputer(BlockPos pos, int world) {
		this.pos = pos;
		this.world = world;
		clearScreen();
		fs = new ComputerFileSystem(this);
		ch = new CommandHandler(null);
		ch.addCommands(new DefaultDriver());
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
		String version = "BERSH v" + ModInfo.MOD_VERSION;
		cursorPos = new MutTermPos((BaseComputer.cols - version.length()) / 2, 0);
		drawStringAtCursor(0xD81212, 0x114545, version);
		cursorPos.col = 0;
		cursorPos.row = 2;
		setupMainInputLine();
	}
	
	public void setupMainInputLine() {
		setCharacter(0, cursorPos.row, '>', true);
		setCursor(1, cursorPos.row);
	}
	
	private void verifyCursorPos(int resetCol) {
		if (cursorPos.col >= BaseComputer.cols) {
			cursorPos.col = resetCol;
			cursorPos.row ++;
		}
		if (cursorPos.row >= BaseComputer.rows) {
			scrollDown();
			setCursor(cursorPos.col, BaseComputer.rows - 1);
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
		if (!BaseComputer.fitsOnScreen(pos)) {
			return TermCell.ERROR;
		}
		return screen[getIndex(pos)];
	}
	
	public void clearScreen() {
		if (screen == null || screen.length < BaseComputer.cols * BaseComputer.rows) {
			screen = BaseComputer.getNewEmptyScreen();
		}
		for (int x = 0; x < BaseComputer.cols; x ++) {
			for (int y = 0; y < BaseComputer.rows; y ++) {
				setCharacter(x, y, (char) 0, true);
			}
		}
	}
	
	public void keyTyped(int code, char typed) {
		if (processHandler.isEmpty()) {
			if (code == Keyboard.KEY_NUMPADENTER || code == Keyboard.KEY_RETURN) {
				setCursor(0, cursorPos.row + 1);
				String cmd = command.toString();
				ch.addCommandToExectionStack(this, cmd);
				previous.add(cmd);
				command.setLength(0);
				cmdIndex = 0;
				prevIndex = 0;
				return;
			}
			if (code == Keyboard.KEY_UP) {
				if (previous.size() - 1 >= prevIndex) {
					String prev = previous.get(previous.size() - 1 - prevIndex);
					prevIndex ++;
					for (int x = 1; x < BaseComputer.cols; x ++) {
						setCharacter(x, cursorPos.row, (char) 0, true);
					}
					command.setLength(0);
					command.append(prev);
					cmdIndex = command.length();
					setCursor(1, cursorPos.row);
					drawStringAtCursor(prev);
					setCursor(1 + cmdIndex, cursorPos.row);
				}
				return;
			}
			prevIndex = 0;
			if (command.length() > 0) {
				if (code == Keyboard.KEY_BACK) {
					setCursor(cursorPos.col - 1, cursorPos.row);
					//setCharacter(cursorPos.row, cursorPos.row, (char) 0, true);
					getCell(cursorPos.getImmutPos()).setCharacter((char) 0);
					cmdIndex --;
					command.deleteCharAt(cmdIndex);
					return;
				}
				if (code == Keyboard.KEY_LEFT && cmdIndex > 0) {
					setCursor(cursorPos.col - 1, cursorPos.row);
					cmdIndex --;
					return;
				}
				if (code == Keyboard.KEY_RIGHT && cmdIndex < command.length()) {
					setCursor(cursorPos.col + 1, cursorPos.row);
					cmdIndex ++;
					return;
				}
			}
			if ((typed >= 32 && typed <= 126) || (typed >= 160 && typed <= 255)) {
				setCharacter(cursorPos.col, cursorPos.row, typed, true);
				setCursor(cursorPos.col + 1, cursorPos.row);
				if (cmdIndex >= command.length()) {
					command.setLength(command.length() + 1);
				}
				command.setCharAt(cmdIndex, typed);
				cmdIndex ++;
			}
		}
	}
	
	public void drawStringAtCursor(String in) {
		if (in == null || in.isEmpty()) {
			return;
		}
		char[] s = in.toCharArray();
		for (char c : s) {
			setCharacter(cursorPos.col, cursorPos.row, c, true);
			setCursor(cursorPos.col + 1, cursorPos.row);
		}
	}
	
	public void drawStringAtCursor(int fcolor, String in) {
		int prevFore = cursorForeColor;
		setCursorColor(cursorBackColor, fcolor);
		drawStringAtCursor(in);
		cursorForeColor = prevFore;
	}
	
	public void drawStringAtCursor(int fcolor, int bcolor, String in) {
		int prevFore = cursorForeColor;
		int prevBack = cursorBackColor;
		setCursorColor(bcolor, fcolor);
		drawStringAtCursor(in);
		cursorForeColor = prevFore;
		cursorBackColor = prevBack;
	}
	
	private void scrollDown() {
		int prevFore = cursorForeColor;
		int prevBack = cursorBackColor;
		for (int y = 0; y < BaseComputer.rows; y ++) {
			for (int x = 0; x < BaseComputer.cols; x ++) {
				boolean top = y < BaseComputer.rows - 1;
				TermCell below = getCell(new TermPos(x, y + 1));
				if (top) {
					cursorForeColor = below.getForegroundColor();
					cursorBackColor = below.getBackgroundColor();
				}
				setCharacter(x, y, (top) ? (below.getCharacter()) : ((char) 0), true);
			}
		}
		cursorForeColor = prevFore;
		cursorBackColor = prevBack;
	}
	
	public void setCharacter(int col, int row, char character, boolean color) {
		updateAvailable = true;
		TermCell cell = getCell(new TermPos(col, row));
		if (cell == null) {
			return;
		}
		if (color) {
			cell.setBackgroundColor(cursorBackColor);
			cell.setForegroundColor(cursorForeColor);
		}
		cell.setCharacter(character);
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
		if (!BaseComputer.fitsOnScreen(pos)) {
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
		updateAvailable = true;
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
			drawStringAtCursor("Usage: " + ch.getUsage(process.getCommand()));
		} else if(res.equals(EnumCommandResponse.FAIL)) {
			drawStringAtCursor("Command failed");
		}
		if (!res.equals(EnumCommandResponse.CANCEL_RESPONSE)) {
			setCursor(new TermPos(0, cursorPos.row + 1));
		}
		setupMainInputLine();
		updateAvailable = true;
	}
	
	public int getCursorBackground() {
		return cursorBackColor;
	}
	
	public int getCursorForeground() {
		return cursorForeColor;
	}
	
	public void setCursorColor(int back, int fore) {
		cursorBackColor = back;
		cursorForeColor = fore;
	}
	
}