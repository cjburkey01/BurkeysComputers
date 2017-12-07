package com.cjburkey.burkeyscomputers.computers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.lwjgl.input.Keyboard;
import com.cjburkey.burkeyscomputers.ModInfo;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.ModUtil;
import com.cjburkey.burkeyscomputers.terminal.bersh.CmdProcess;
import com.cjburkey.burkeyscomputers.terminal.bersh.CommandHandler;
import com.cjburkey.burkeyscomputers.terminal.bersh.DefaultDriver;
import com.cjburkey.burkeyscomputers.terminal.bersh.EnumCommandResponse;
import com.cjburkey.burkeyscomputers.terminal.bersh.ProcessHandler;
import net.minecraft.nbt.NBTTagCompound;

public abstract class BaseComputer {
	
	public static final int cols = 33;
	public static final int rows = 14;
	
	private static final List<Class<? extends BaseComputer>> comps = new ArrayList<>();
	
	private long uniqueId = -1;
	private TermCell[] screen;
	private ComputerFileSystem fs;
	private CommandHandler ch;
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
	
	public BaseComputer(int dummy) {
		clearScreen();
		fs = new ComputerFileSystem(this);
		ch = new CommandHandler(null);
		ch.addCommands(new DefaultDriver());
		processHandler = new ProcessHandler();
		resetDisplay();
	}

	public static String getSaveIdentifier() {
		return "dmmy";
	}
	public abstract Object[] saveToString();
	public abstract void loadFromString(String[] data);
	
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
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (getUniqueId() ^ (getUniqueId() >>> 32));
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
		BaseComputer other = (BaseComputer) obj;
		if (getUniqueId() != other.getUniqueId()) {
			return false;
		}
		return true;
	}
	
	public static TermCell[] getNewEmptyScreen() {
		TermCell[] newArr = new TermCell[cols * rows];
		for (int i = 0; i < newArr.length; i ++) {
			newArr[i] = new TermCell();
		}
		return newArr;
	}
	
	public static boolean fitsOnScreen(TermPos pos) {
		return !(pos.col < 0 || pos.col >= cols || pos.row < 0 || pos.row >= rows);
	}
	
	public static BaseComputer loadFromData(String extra) {
		String[] split = extra.trim().split(Pattern.quote(";"));
		String start = split[0];
		BaseComputer found = getComputerFromIdentifier(start);
		if (found != null) {
			ModLog.info("Loaded computer from save identifier: " + start + " in data: " + extra);
			found.loadFromString(ModUtil.removeFirstArrayItem(split, new String[split.length - 1]));
			return found;
		}
		return null;
	}
	
	public static void addComputer(Class<? extends BaseComputer> computer) {
		System.out.println("Added computer to identifier list: " + computer.getClass());
		comps.add(computer);
	}
	
	// TODO: FIX THIS, ME
	public static BaseComputer getComputerFromIdentifier(String id) {
		for (Class<? extends BaseComputer> cl : comps) {
			try {
				Method method = cl.getMethod("getSaveIdentifier");
				String identity = (String) method.invoke(null);
				if (identity.equals(id)) {
					BaseComputer comp = cl.newInstance();
					return comp;
				}
			} catch (Exception e) {
			}
		}
		ModLog.error("Failed to create computer for save identifier of: " + id);
		return null;
	}
	
}