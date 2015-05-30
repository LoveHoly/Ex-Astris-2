package kr.loveholy.exastris;

import kr.loveholy.exastris.block.BlockAutomaticSieve;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ExAstrisBlock {

	public static Block blockAutomaticSieve;

	public static void init() {
		blockAutomaticSieve = new BlockAutomaticSieve();
    	GameRegistry.registerBlock(blockAutomaticSieve, ExAstrisData.BLOCK_AUTOMATIC_SIEVE);
	}
}
