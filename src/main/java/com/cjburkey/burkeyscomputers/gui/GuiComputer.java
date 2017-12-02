package com.cjburkey.burkeyscomputers.gui;

import com.cjburkey.burkeyscomputers.ModInfo;
import com.cjburkey.burkeyscomputers.container.ContainerComputer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiComputer extends GuiContainer {

	public static final int id = 0;
	
	public GuiComputer(ContainerComputer container) {
		super(container);
		
		xSize = 175;
		ySize = 165;
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
		fontRenderer.drawString("Hello", 10, 10, 0xFFFFFF);
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}