package kr.loveholy.exastris.gui;



import kr.loveholy.exastris.ExAstrisData;
import kr.loveholy.exastris.block.tileentity.TileEntityAutomaticSieve;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class GuiAutomaticSieve extends GuiContainer{
	
	public static final ResourceLocation texture = new ResourceLocation(ExAstrisData.MODID, "textures/gui/guiAutomatic.png");
	public TileEntityAutomaticSieve te;
	public GuiAutomaticSieve(InventoryPlayer invPlayer, TileEntityAutomaticSieve tileentity) {
		super(new ContainerAutomaticSieve(invPlayer, tileentity));
		te = tileentity;
		xSize = 176;
		ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		
		int i1 = (int) ((float) te.getEnergy() / te.MAX_ENERGY * 49.0);
		this.drawTexturedModalRect(k + 152, l + 9 + (49 - i1), 176 + 15, 0, 16, i1);
		
		int i2 = (int) ((float) te.getWork() / te.getWorkMax() * 15.0);
		this.drawTexturedModalRect(k + 43, l + 36, 176, 0, i2, 15);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		if (mouseX > k + 151 && mouseX < k + 151 + 18 && mouseY > l + 9 && mouseY < l + 9 + 51)
		{
			String hoveringText = "";
			hoveringText = this.te.getEnergy() + " / " + this.te.MAX_ENERGY;
			this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(hoveringText)), mouseX-k+10, mouseY-l+10);
		}
	}

}
