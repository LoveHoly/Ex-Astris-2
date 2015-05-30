package kr.loveholy.exastris.registry;

import exnihilo2.items.EN2Items;
import kr.loveholy.exastris.ExAstrisBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RegistryRecipe {
	public static void register()
	{
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(ExAstrisBlock.blockAutomaticSieve, 1),
				"xzx",
				"xxx",
				"y y",
				'x', new ItemStack(Blocks.iron_block, 1), 
				'y', new ItemStack(Items.iron_ingot, 1),
				'z', new ItemStack(EN2Items.mesh_silk_white, 1)
				));
	}
}
