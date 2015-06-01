package kr.loveholy.exastris.proxy;

import exnihilo2.blocks.sieves.renderer.SieveRenderer;
import kr.loveholy.exastris.ExAstrisBlock;
import kr.loveholy.exastris.ExAstrisData;
import kr.loveholy.exastris.ExAstrisItem;
import kr.loveholy.exastris.block.tileentity.TileEntityAutomaticSieve;
import kr.loveholy.exastris.compatibility.nei.ModNEI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderer() {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(
				Item.getItemFromBlock(ExAstrisBlock.blockAutomaticSieve),
				0,
				new ModelResourceLocation(ExAstrisData.MODID + ":"
						+ ExAstrisData.BLOCK_AUTOMATIC_SIEVE, "inventory"));

		ClientRegistry.bindTileEntitySpecialRenderer(
				TileEntityAutomaticSieve.class, new SieveRenderer());
	}
	
	@Override
	public void initNEI()
	{
		ModNEI.init();
	}
}
