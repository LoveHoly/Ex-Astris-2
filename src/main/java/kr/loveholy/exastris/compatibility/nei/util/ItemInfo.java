package kr.loveholy.exastris.compatibility.nei.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemInfo {

	private Item item;
	private int meta;



	public ItemInfo(ItemStack itemStack)
	{
		if (itemStack != null && itemStack.getItem() != null)
		{
			this.item = itemStack.getItem();
			this.meta = itemStack.getItemDamage();
		}
	}
	
	public ItemInfo(Block block, int meta)
	{
		this.item = Item.getItemFromBlock(block);
		this.meta = meta;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + meta;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemInfo other = (ItemInfo) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (meta != other.meta)
			return false;
		return true;
	}

	public ItemInfo(Item item, int meta)
	{
		this.item = item;
		this.meta = meta;
	}
	
	public ItemStack getItemStack()
	{
		return new ItemStack(this.item, 1, this.meta);
	}



}