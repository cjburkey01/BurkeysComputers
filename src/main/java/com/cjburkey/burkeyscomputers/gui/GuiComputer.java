package com.cjburkey.burkeyscomputers.gui;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import com.cjburkey.burkeyscomputers.ModInfo;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.computers.IComputer;
import com.cjburkey.burkeyscomputers.container.ContainerComputer;
import com.cjburkey.burkeyscomputers.packet.ModPacketHandler;
import com.cjburkey.burkeyscomputers.packet.PacketComputerToServer;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiComputer extends GuiContainer {

	public static final int id = 0;
	
	private final int startDrawX;
	private final int startDrawY;
	private final int characterWidth;
	private final int characterHeight;
	private final int characterPadding;
	
	private final IComputer computer;
	
	private static char[] chars = TileEntityComputer.empty();
	
	public GuiComputer(ContainerComputer container) {
		super(container);
		computer = container.getComputer();
		
		xSize = 239;
		ySize = 148;
		startDrawX = 5;
		startDrawY = 5;
		characterWidth = 6;
		characterHeight = 9;
		characterPadding = 1;
	}
	
	public void initGui() {
		super.initGui();
		updateScreenContents(0, (char) 0);
	}
	
	public static void updateContents(char[] updated) {
		if (updated == null) {
			return;
		}
		if (chars == null) {
			chars = updated;
			return;
		}
		if (updated.length == chars.length) {
			chars = updated.clone();
		}
	}
	
	public void updateScreenContents(int key, char character) {
		ModPacketHandler.getNetwork().sendToServer(new PacketComputerToServer(computer, key, character));
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
		drawTerminal();
	}
	
	private void drawTerminal() {
		IComputer comp = computer;
		int cols = TileEntityComputer.cols;
		int rows = TileEntityComputer.rows;
		for (int x = 0; x < cols; x ++) {
			for (int y = 0; y < rows; y ++) {
				int drawX = startDrawX + (characterWidth + characterPadding) * x;
				int drawY = startDrawY + (characterHeight + characterPadding) * y;
				String a = "" + chars[y * cols + x];
				fontRenderer.drawString(a, drawX + (characterWidth - fontRenderer.getStringWidth(a)) / 2, drawY + 1, 0xFF55FF55);
			}
		}
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}