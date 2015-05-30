package kr.loveholy.exastris.block;

import kr.loveholy.exastris.ExAstris;
import kr.loveholy.exastris.ExAstrisData;
import kr.loveholy.exastris.block.tileentity.TileEntityAutomaticSieve;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.GameRegistry;
import exnihilo2.blocks.sieves.BlockSieve;

public class BlockAutomaticSieve extends BlockSieve{

	public BlockAutomaticSieve() {
		super(Material.iron);
		GameRegistry.registerTileEntity(TileEntityAutomaticSieve.class, ExAstrisData.MODID + "." + ExAstrisData.BLOCK_AUTOMATIC_SIEVE);
	}
	
	@Override
	public String getUnlocalizedName() {
		return ExAstrisData.MODID + "." + ExAstrisData.BLOCK_AUTOMATIC_SIEVE;
	}
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,
    		IBlockState state, EntityPlayer playerIn, EnumFacing side,
    		float hitX, float hitY, float hitZ) {
		PlayerInteractEvent e = new PlayerInteractEvent(playerIn,
				Action.RIGHT_CLICK_BLOCK, pos, side, worldIn);
    	
		if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Result.DENY
				|| e.useBlock == Result.DENY)
			return false;

		if (!playerIn.isSneaking()) {
			playerIn.openGui(ExAstris.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityAutomaticSieve();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		
		ISidedInventory inv = (ISidedInventory) tile;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				EntityItem entityitem = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(),
						inv.getStackInSlot(i));
				worldIn.spawnEntityInWorld(entityitem);
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
	

}
