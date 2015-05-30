package kr.loveholy.exastris.gui;

import kr.loveholy.exastris.block.tileentity.TileEntityAutomaticSieve;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID == 0) {
			TileEntityAutomaticSieve te = (TileEntityAutomaticSieve) world.getTileEntity(new BlockPos(x,y,z));
			return new ContainerAutomaticSieve(player.inventory, te);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID == 0) {
			TileEntityAutomaticSieve te = (TileEntityAutomaticSieve) world.getTileEntity(new BlockPos(x,y,z));
			return new GuiAutomaticSieve(player.inventory, te);
		}
		return null;
	}

}
