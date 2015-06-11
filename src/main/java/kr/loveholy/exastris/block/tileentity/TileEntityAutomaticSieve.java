package kr.loveholy.exastris.block.tileentity;

import java.util.ArrayList;
import java.util.Iterator;

import kr.loveholy.exastris.ExAstrisData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import exnihilo2.blocks.sieves.tileentity.TileEntitySieve;
import exnihilo2.registries.sifting.SieveRegistry;
import exnihilo2.registries.sifting.SieveReward;
import exnihilo2.util.enums.EnumMetadataBehavior;

public class TileEntityAutomaticSieve extends TileEntitySieve implements ISidedInventory{

	private ItemStack[] inventory;
	private int energy;
	
	public static final int MAX_ENERGY = 50000;
	public static final int COAL_ENERGY = 500;
	public static final int TICK_PER_ENERGY = 10;
	
	private static final int[] slots_top = new int[] { 1 };
	private static final int[] slots_bottom = new int[] { 2,3,4,5,6,7,8,9 };
	private static final int[] slots_sides = new int[] { 0 };
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public TileEntityAutomaticSieve() {
		super();
		inventory = new ItemStack[10];
	}
	
	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.inventory[index] != null) {
			ItemStack itemstack;

			if (this.inventory[index].stackSize <= count) {
				itemstack = this.inventory[index];

				this.inventory[index] = null;
				return itemstack;
			} else {
				itemstack = this.inventory[index].splitStack(index);

				if (this.inventory[index].stackSize == 0) {
					this.inventory[index] = null;
				}
				return itemstack;
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		if (this.inventory[index] != null) {
			ItemStack itemstack = this.inventory[index];
			this.inventory[index] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventory[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.getPos()) != this ? false : player.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());
		if(index == 0 && block != null && SieveRegistry.isSiftable(block.getStateFromMeta(stack.getMetadata())))
			return true;
		if(index == 1 && stack.getItem() == Items.coal)
			return true;
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.inventory.length; ++i)
        {
            this.inventory[i] = null;
        }
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? slots_bottom : (side == EnumFacing.UP ? slots_top : slots_sides);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			EnumFacing direction) {
		if(index == 1 && direction == EnumFacing.UP)
			return true;
		if(index == 0 && (EnumFacing.EAST == direction || EnumFacing.WEST == direction || EnumFacing.SOUTH == direction || EnumFacing.NORTH == direction))
			return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,
			EnumFacing direction) {
		if(index >= 2)
			return true;
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		energy = compound.getInteger("energy");
		
		NBTTagList nbttaglist = compound.getTagList("Items", 10);
		this.inventory = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.inventory.length) {
				this.inventory[b0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("energy", energy);
		
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.inventory.length; ++i) {
			if (this.inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		compound.setTag("Items", nbttaglist);
	}
	
	public TextureAtlasSprite getMeshTexture()
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("exnihilo2:blocks/sieve_mesh_silk_white");	
	}
	
	@Override
	public void update() {
		super.update();
		if(inventory[1] != null && energy + COAL_ENERGY <= MAX_ENERGY)
		{
			decrStackSize(1,1);
			energy += COAL_ENERGY;
			sync();
		}
		
		if (canWork())
		{
			if(energy > TICK_PER_ENERGY)
			{
				energy-=TICK_PER_ENERGY;
				doWork();
				worldObj.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "step.sand", 0.3f, 0.6f);
			}
		}
		else
		{
			if (inventory[0] != null)
			{
				Block block = Block.getBlockFromItem(inventory[0].getItem());
				
				if (block != null && SieveRegistry.isSiftable(block.getStateFromMeta(inventory[0].getMetadata())))
				{
					ItemStack contents = inventory[0].copy();
					contents.stackSize = 1;

					worldObj.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "step.gravel", 0.5f, 1.0f);
					
					if((inventory[0].stackSize - 1) == 0){
						inventory[0] = null;
					}
					else{
						inventory[0] = new ItemStack(inventory[0].getItem(), (inventory[0].stackSize - 1), inventory[0].getItemDamage());
					}
					
					setContents(contents);
					
				}
			}
			
		}
		
		/*
		if(inventory[0] != null && energy > TICK_PER_ENERGY){
			if(timer % 5 == 0) {
				setContents(inventory[0]);
				spawningParticles = true;
			}
			energy -= TICK_PER_ENERGY;
			timer++;
			if(timer > MAX_TIMER)
			{
				
				ArrayList<SieveReward> rewards = SieveRegistry.getEntryForBlockState(
						Block.getBlockFromItem(inventory[0].getItem()).getBlockState().getBaseState()
						).getRewards();
				if (rewards.size() > 0)
				{
					Iterator<SieveReward> it = rewards.iterator();
					while(it.hasNext())
					{
						SieveReward reward = it.next();
						if (worldObj.rand.nextFloat() <= reward.getBaseChance())
						{
							//
							int outputIndex = 0;
							
							for(int i = 2; i < getSizeInventory() - 1; i++)
							{
								if(inventory[i] == null)
								{
									outputIndex=i;
									break;
								}
								else
								{
									if (ItemStack.areItemsEqual(inventory[i], reward.getItem())
											  && inventory[i].stackSize < inventory[i].getMaxStackSize())
									{
										outputIndex=i;
										break;
									}
								}
							}
							
							if(outputIndex != 0)
							{
								if((inventory[0].stackSize - 1) == 0){
									inventory[0] = null;
								}
								else{
									inventory[0] = new ItemStack(inventory[0].getItem(), (inventory[0].stackSize - 1), inventory[0].getItemDamage());
								}
								if (inventory[outputIndex] != null){
									inventory[outputIndex] = new ItemStack(reward.getItem().getItem(), (inventory[outputIndex].stackSize + 1), reward.getItem().getItemDamage());
								}else{ 
									inventory[outputIndex] = new ItemStack(reward.getItem().getItem(), 1, reward.getItem().getItemDamage());
								}
								timer = 0;
							}
							else
							{
								timer = 0;
							}
							///
						}
					}
				}
			}
		}*/
	}
	
	public int getWork() {
		return work;
	}
	
	public int getWorkMax()
	{
		return workMax;
	}
	
	@Override
	public void doWork()
	{
		if (!this.worldObj.isRemote)
		{
			this.spawningParticles = true;
			if (workThisCycle + workSpeed > workPerCycleLimit)
			{
				this.work += workPerCycleLimit - workThisCycle;
			}
			else
			{
				this.work += workSpeed;
			}
			
			if (work > workMax)
			{
				if (contentsState != null)
				{
					ArrayList<SieveReward> rewards = SieveRegistry.getEntryForBlockState(contentsState, EnumMetadataBehavior.SPECIFIC).getRewards();
					if(rewards != null && rewards.size() > 0)
					{
						Iterator<SieveReward> it = rewards.iterator();
						while(it.hasNext())
						{
							SieveReward reward = it.next();
							if (worldObj.rand.nextFloat() <= reward.getBaseChance())
							{
								int outputIndex = 0;
								
								for(int i = 2; i < getSizeInventory() - 1; i++)
								{
									if(inventory[i] == null)
									{
										outputIndex=i;
										break;
									}
									else
									{
										if (ItemStack.areItemsEqual(inventory[i], reward.getItem())
												  && inventory[i].stackSize < inventory[i].getMaxStackSize())
										{
											outputIndex=i;
											break;
										}
									}
								}
								
								if(outputIndex != 0)
								{
									if (inventory[outputIndex] != null){
										inventory[outputIndex] = new ItemStack(reward.getItem().getItem(), (inventory[outputIndex].stackSize + 1), reward.getItem().getItemDamage());
									}else{ 
										inventory[outputIndex] = new ItemStack(reward.getItem().getItem(), 1, reward.getItem().getItemDamage());
									}
								}
							}
						}
					}
				}
				
				work = 0;
				contents = null;
			}
			
			sync();
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new S35PacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.getNbtCompound();
		readFromNBT(tag);
	}

}
