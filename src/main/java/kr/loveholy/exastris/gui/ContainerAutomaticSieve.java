package kr.loveholy.exastris.gui;

import kr.loveholy.exastris.block.tileentity.TileEntityAutomaticSieve;
import kr.loveholy.exastris.gui.slot.SlotAutomaticSieve;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutomaticSieve extends Container{
	public TileEntityAutomaticSieve te;
	public ContainerAutomaticSieve(InventoryPlayer invPlayer, TileEntityAutomaticSieve tileentity) {
		te = tileentity;
		
		int i;

		this.addSlotToContainer(new SlotAutomaticSieve(te, 0, 18, 35));
		this.addSlotToContainer(new SlotAutomaticSieve(te, 1, 152, 62));
		
		for (i = 0; i < 2; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				this.addSlotToContainer(new SlotAutomaticSieve(te, j + i * 4 + 2, 66 + j * 18, 27 + i * 18));
			}
		}
		
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
		}
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.isUseableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
	{
		ItemStack stack = null;
		Slot slot = this.getSlot(slotNum);
		Slot inputBlockSlot = this.getSlot(0);
		Slot inputCoalSlot = this.getSlot(1);
		if (slot != null & slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();
			if (slotNum < te.getSizeInventory())
			{
				if (!this.mergeItemStack(stackInSlot, te.getSizeInventory(), te.getSizeInventory() + 36, true))
					return null;
			} else if (inputBlockSlot.isItemValid(stackInSlot)) {
				if (!this.mergeItemStack(stackInSlot, 0, 1, false))
					return null;
			} else if (inputCoalSlot.isItemValid(stackInSlot)) {
				if (!this.mergeItemStack(stackInSlot, 1, 2, false))
					return null;
			}
			
			if (stackInSlot.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, stackInSlot);
			
		}

		return stack;
	}
}
