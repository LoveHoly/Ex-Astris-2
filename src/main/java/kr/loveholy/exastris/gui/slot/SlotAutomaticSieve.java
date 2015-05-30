package kr.loveholy.exastris.gui.slot;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import exnihilo2.registries.hammering.HammerRegistry;
import exnihilo2.registries.sifting.SieveRegistry;

public class SlotAutomaticSieve extends Slot{

	private int index;
	
	public SlotAutomaticSieve(IInventory inventoryIn, int index,
			int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);

		this.index = index;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack) {
		
		if(index == 0 && Block.getBlockFromItem(itemstack.getItem()) != null && SieveRegistry.isSiftable(Block.getBlockFromItem(itemstack.getItem()).getBlockState().getBaseState()))
			return true;
		if(index == 1 && itemstack.getItem() == Items.coal)
			return true;
		return false;
		
	}

}
