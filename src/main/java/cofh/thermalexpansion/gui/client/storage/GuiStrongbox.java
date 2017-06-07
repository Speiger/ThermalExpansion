package cofh.thermalexpansion.gui.client.storage;

import cofh.core.gui.GuiCore;
import cofh.core.gui.element.TabInfo;
import cofh.core.gui.element.TabSecurity;
import cofh.core.init.CoreProps;
import cofh.core.util.helpers.SecurityHelper;
import cofh.lib.gui.element.TabBase;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.block.storage.TileStrongbox;
import cofh.thermalexpansion.gui.container.storage.ContainerStrongbox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.UUID;

public class GuiStrongbox extends GuiCore {

	protected TileStrongbox myTile;
	protected UUID playerName;
	protected int storageIndex;

	protected TabBase securityTab;

	public GuiStrongbox(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerStrongbox(inventory, tile));

		myTile = (TileStrongbox) tile;
		playerName = SecurityHelper.getID(inventory.player);
		storageIndex = myTile.getStorageIndex();
		texture = CoreProps.TEXTURE_STORAGE[storageIndex];
		name = myTile.getName();

		xSize = 14 + 18 * MathHelper.clamp(storageIndex, 9, 14);
		ySize = 112 + 18 * MathHelper.clamp(storageIndex, 2, 9);

		myInfo = StringHelper.localize("tab.thermalexpansion.storage.strongbox.0");

		if (myTile.enchantHolding <= 0) {
			myInfo += "\n\n" + StringHelper.localize("tab.thermalexpansion.storage.enchant");
		}
	}

	@Override
	public void initGui() {

		super.initGui();

		addTab(new TabInfo(this, myInfo));
		if (myTile.enableSecurity() && myTile.isSecured()) {
			addTab(new TabSecurity(this, myTile, playerName));
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {

		GlStateManager.color(1, 1, 1, 1);
		bindTexture(texture);

		if (xSize > 256 || ySize > 256) {
			drawSizedTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
		} else {
			drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		}
		mouseX = x - guiLeft;
		mouseY = y - guiTop;

		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0.0F);
		drawElements(partialTick, false);
		drawTabs(partialTick, false);
		GlStateManager.popMatrix();
	}

	@Override
	public void updateScreen() {

		super.updateScreen();

		if (!myTile.canAccess()) {
			this.mc.thePlayer.closeScreen();
		}
		securityTab.setVisible(myTile.enableSecurity() && myTile.isSecured());
	}

}
