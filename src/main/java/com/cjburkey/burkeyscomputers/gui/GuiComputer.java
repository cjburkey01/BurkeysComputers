package com.cjburkey.burkeyscomputers.gui;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import com.cjburkey.burkeyscomputers.ModInfo;
import com.cjburkey.burkeyscomputers.computers.ComputerOpener;
import com.cjburkey.burkeyscomputers.computers.IComputer;
import com.cjburkey.burkeyscomputers.computers.TermCell;
import com.cjburkey.burkeyscomputers.computers.TermPos;
import com.cjburkey.burkeyscomputers.container.ContainerComputer;
import com.cjburkey.burkeyscomputers.packet.ModPacketHandler;
import com.cjburkey.burkeyscomputers.packet.PacketTypedOnClient;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiComputer extends GuiContainer {

	public static final int id = 0;
	public static final int startDrawX = 12;
	public static final int startDrawY = 5;
	public static final int characterPadding = 1;
	
	private long computer;
	
	private static TermCell[] drawnCells = IComputer.getNewEmptyScreen();
	private static TermPos cursorPos;
	
	public GuiComputer(ContainerComputer container) {
		super(container);
		
		xSize = 246;
		ySize = 148;
	}
	
	public void initGui() {
		super.initGui();
		updateScreenContents(0, (char) 0);
	}
	
	public static void updateContents(TermPos cursor, TermCell[] updated) {
		if (updated == null) {
			return;
		}
		if (drawnCells == null || updated.length == drawnCells.length) {
			drawnCells = updated;
		}
		if (cursor == null) {
			return;
		}
		cursorPos = cursor;
	}
	
	public void updateScreenContents(int key, char character) {
		computer = ComputerOpener.getClientComputer();
		if (computer >= 0) {
			ModPacketHandler.getNetwork().sendToServer(new PacketTypedOnClient(computer, key, character));
		}
	}
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.player.closeScreen();
			return;
		}
		updateScreenContents(keyCode, typedChar);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.MOD_ID, "textures/gui/gui_computer.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		drawTerminal(mouseX, mouseY);
	}
	
	private void drawTerminal(int mx, int my) {
		int cols = IComputer.cols;
		int rows = IComputer.rows;
		for (int x = 0; x < cols; x ++) {
			for (int y = 0; y < rows; y ++) {
				TermCell cell = drawnCells[y * cols + x];
				drawCellBackground(x, y, cell);
				drawCellForeground(x, y, cell);
			}
		}
		TermPos pos = pixelToCell(mx - guiLeft, my - guiTop);
		if (IComputer.fitsOnScreen(pos)) {
			fillCell(pos.col, pos.row, 0x44FFFFFF);
		}
	}
	
	private TermPos pixelToCell(int x, int y) {
		int col = (int) Math.floor((float) (x - startDrawX) / (float) (TermCell.characterWidth + characterPadding));
		int row = (int) Math.floor((float) (y - startDrawY) / (float) (TermCell.characterHeight + characterPadding));
		return new TermPos(col, row);
	}
	
	private TermPos cellToPixel(int col, int row) {
		int x = startDrawX + (TermCell.characterWidth + characterPadding) * col;
		int y = startDrawY + (TermCell.characterHeight + characterPadding) * row;
		return new TermPos(x, y);
	}
	
	private void fillCell(int col, int row, int drawColor) {
		TermPos p = cellToPixel(col, row);
		drawRect(((col == 0) ? -1 : 0) + p.col, ((row == 0) ? -1 : 0) + p.row, p.col + TermCell.characterWidth + 1, p.row + TermCell.characterHeight + 1, drawColor);
	}
	
	private void drawCellBackground(int col, int row, TermCell cell) {
		fillCell(col, row, cell.getDrawingBackgroundColor());
	}
	
	private void drawCellForeground(int col, int row, TermCell cell) {
		TermPos p = cellToPixel(col, row);
		String a = "" + cell.getCharacter();
		int x = p.col + (TermCell.characterWidth - fontRenderer.getStringWidth(a)) / 2;
		int y = p.row + 1;
		fontRenderer.drawString(a, x, y, cell.getDrawingForegroundColor());
		if (cursorPos != null && cursorPos.col == col && cursorPos.row == row) {
			fontRenderer.drawString("_", x, y, cell.getDrawingForegroundColor());
		}
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}